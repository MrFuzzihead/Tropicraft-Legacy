package net.tropicraft.lint;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Finds methods that pass their own parameters straight back into the same overload. That is always infinite recursion:
 * the arguments have the same static types as the parameters, the enclosing method is applicable, and the compiler
 * picks
 * the most specific applicable overload - which is the method itself.
 * <p>
 * This is the shape of the Easter Island Head crash in issue #54, where
 * {@code RenderEIH#doRender(Entity, ...)} called {@code this.doRender(entity, ...)}. It compiled, it legally overrode a
 * supertype method, and it only failed when an EIH came into view. It arrived during a "drop the redundant casts"
 * refactor: {@code this.doRender((EntityLiving) entity, ...)} became {@code this.doRender(entity, ...)}, and the cast
 * had been the only thing selecting the working {@code EntityLiving} overload instead of the {@code Entity} one that
 * pointed back at itself.
 * <p>
 * Deliberately conservative, so it can gate the build:
 * </p>
 * <ul>
 * <li>Intentional recursion always changes an argument ({@code getDistanceToBase(i + 1, height)},
 * {@code generateCaveNode(rand.nextLong(), ..., length - 1)}), so it is not reported.</li>
 * <li>Delegating to a differently named helper ({@code this.renderEIH(entity, ...)}) or to {@code super} is not
 * reported - that is the fix.</li>
 * <li>A cast makes the argument text differ, so an explicit upcast such as {@code this.foo((EntityLivingBase) e)} is
 * not
 * reported even though it is the same overload-selection trick.</li>
 * </ul>
 * <p>
 * No parser dependency: comments and string/char literals are blanked out with offsets preserved (line numbers stay
 * exact), then declarations are found by scanning identifier + paren + body brace. That keeps it runnable offline
 * inside
 * the existing JUnit suite on the Java 8 test toolchain.
 * </p>
 */
public final class SelfRecursionLint {

    /** Words that are followed by a paren but are not method declarations. */
    private static final Set<String> NOT_METHODS = Collections.unmodifiableSet(
        new HashSet<>(
            Arrays.asList(
                "if",
                "for",
                "while",
                "switch",
                "catch",
                "synchronized",
                "try",
                "do",
                "else",
                "return",
                "throw",
                "assert",
                "case",
                "new",
                "super",
                "this")));

    private SelfRecursionLint() {}

    /** One recursive method found in a source file. */
    public static final class Finding {

        /** Path relative to the scanned source root, using {@code /}. */
        public final String file;
        /** 1-based line of the offending call. */
        public final int line;
        /** Method name, as used in allowlist keys ({@code path/to/File.java#name}). */
        public final String method;
        /** Declared parameter count, so overloads are distinguishable in the report. */
        public final int arity;

        Finding(final String file, final int line, final String method, final int arity) {
            this.file = file;
            this.line = line;
            this.method = method;
            this.arity = arity;
        }

        public String methodName() {
            return method;
        }

        public String describe() {
            return file + ":"
                + line
                + "  "
                + method
                + "("
                + arity
                + " params)"
                + "  passes its own parameters back into the same overload";
        }

        @Override
        public String toString() {
            return describe();
        }
    }

    /** Scans every {@code .java} file under {@code root}. */
    public static List<Finding> scanDirectory(final Path root) throws IOException {
        if (!Files.isDirectory(root)) {
            return Collections.emptyList();
        }
        final List<Path> files;
        try (Stream<Path> walk = Files.walk(root)) {
            files = walk.filter(
                path -> path.toString()
                    .endsWith(".java"))
                .sorted()
                .collect(Collectors.toList());
        }
        final List<Finding> findings = new ArrayList<>();
        for (final Path file : files) {
            final String relative = root.relativize(file)
                .toString()
                .replace('\\', '/');
            findings.addAll(scanSource(relative, new String(Files.readAllBytes(file), StandardCharsets.UTF_8)));
        }
        return findings;
    }

    /** Scans one source file's text. {@code file} is only used for reporting. */
    public static List<Finding> scanSource(final String file, final String source) {
        final String code = blankCommentsAndLiterals(source);
        final List<Finding> findings = new ArrayList<>();
        final int length = code.length();
        int i = 0;
        while (i < length) {
            if (!isIdentifierStart(code.charAt(i))) {
                i++;
                continue;
            }
            final int nameStart = i;
            int nameEnd = i + 1;
            while (nameEnd < length && isIdentifierPart(code.charAt(nameEnd))) {
                nameEnd++;
            }
            final String name = code.substring(nameStart, nameEnd);
            i = nameEnd;
            if (NOT_METHODS.contains(name)) {
                continue;
            }
            final int paren = skipWhitespace(code, nameEnd);
            if (paren >= length || code.charAt(paren) != '(') {
                continue;
            }
            // A declaration is never qualified. The token before a declaration name is its return type, so only a
            // receiver dot disqualifies it here.
            final int before = previousNonWhitespace(code, nameStart - 1);
            if (before >= 0 && code.charAt(before) == '.') {
                continue;
            }
            final int closeParen = match(code, paren, '(', ')');
            if (closeParen < 0) {
                continue;
            }
            final int openBrace = bodyBrace(code, closeParen + 1);
            if (openBrace < 0) {
                continue;
            }
            final int closeBrace = match(code, openBrace, '{', '}');
            if (closeBrace < 0) {
                continue;
            }
            final List<String> parameterNames = parameterNames(code.substring(paren + 1, closeParen));
            final int bodyStart = openBrace + 1;
            collect(file, code, name, parameterNames, code.substring(bodyStart, closeBrace), bodyStart, findings);
        }
        return findings;
    }

    private static void collect(final String file, final String code, final String name,
        final List<String> parameterNames, final String body, final int bodyStart, final List<Finding> findings) {
        if (parameterNames.isEmpty()) {
            return;
        }
        final String normalizedParameters = normalize(join(parameterNames));
        for (final Call call : callsTo(body, name)) {
            if (call.arguments.size() != parameterNames.size()) {
                continue;
            }
            if (!normalize(call.argumentsText).equals(normalizedParameters)) {
                continue;
            }
            findings.add(new Finding(file, lineOf(code, bodyStart + call.start), name, parameterNames.size()));
        }
    }

    /** A call to the enclosing method's name inside its body. */
    private static final class Call {

        final int start;
        final String argumentsText;
        final List<String> arguments;

        Call(final int start, final String argumentsText, final List<String> arguments) {
            this.start = start;
            this.argumentsText = argumentsText;
            this.arguments = arguments;
        }
    }

    /**
     * Finds {@code name(...)} and {@code this.name(...)} calls in {@code body}. Calls on another receiver
     * ({@code super.name(}, {@code other.name(}) are skipped: those are the fix, not the bug.
     */
    private static List<Call> callsTo(final String body, final String name) {
        final List<Call> calls = new ArrayList<>();
        final int length = body.length();
        int i = 0;
        while (i < length) {
            if (!isIdentifierStart(body.charAt(i))) {
                i++;
                continue;
            }
            final int nameStart = i;
            int nameEnd = i + 1;
            while (nameEnd < length && isIdentifierPart(body.charAt(nameEnd))) {
                nameEnd++;
            }
            final String candidate = body.substring(nameStart, nameEnd);
            i = nameEnd;
            if (!candidate.equals(name)) {
                continue;
            }
            final int paren = skipWhitespace(body, nameEnd);
            if (paren >= length || body.charAt(paren) != '(') {
                continue;
            }
            final int before = previousNonWhitespace(body, nameStart - 1);
            int callStart = nameStart;
            if (before >= 0) {
                final char previous = body.charAt(before);
                if (previous == '.') {
                    final int receiverEnd = previousNonWhitespace(body, before - 1) + 1;
                    final int receiverStart = tokenStart(body, receiverEnd - 1);
                    if (!"this".equals(body.substring(receiverStart, receiverEnd))) {
                        continue;
                    }
                    callStart = receiverStart;
                }
            }
            final int closeParen = match(body, paren, '(', ')');
            if (closeParen < 0) {
                continue;
            }
            final String argumentsText = body.substring(paren + 1, closeParen);
            calls.add(new Call(callStart, argumentsText, splitTopLevel(argumentsText)));
        }
        return calls;
    }

    /** Reads parameters as {@code ["int i", "final String name"]} and returns just the names. */
    private static List<String> parameterNames(final String parameters) {
        final List<String> names = new ArrayList<>();
        for (final String parameter : splitTopLevel(parameters)) {
            final String text = parameter.replaceAll("@[\\w.$]+\\s*(\\([^)]*\\))?", " ")
                .replaceAll("\\bfinal\\b", " ")
                .trim();
            if (text.isEmpty()) {
                continue;
            }
            final String[] tokens = text.replace("...", " ")
                .split("[\\s\\[\\]]+");
            final String last = tokens[tokens.length - 1];
            if (!last.isEmpty()) {
                names.add(last);
            }
        }
        return names;
    }

    /** Splits on top-level commas only. */
    public static List<String> splitTopLevel(final String text) {
        final List<String> parts = new ArrayList<>();
        int depth = 0;
        int start = 0;
        for (int i = 0; i < text.length(); i++) {
            final char c = text.charAt(i);
            if (c == '(' || c == '<' || c == '[' || c == '{') {
                depth++;
            } else if (c == ')' || c == '>' || c == ']' || c == '}') {
                depth--;
            } else if (c == ',' && depth == 0) {
                parts.add(
                    text.substring(start, i)
                        .trim());
                start = i + 1;
            }
        }
        final String tail = text.substring(start)
            .trim();
        if (!tail.isEmpty()) {
            parts.add(tail);
        }
        return parts;
    }

    private static String join(final List<String> values) {
        final StringBuilder builder = new StringBuilder();
        for (final String value : values) {
            if (builder.length() > 0) {
                builder.append(',');
            }
            builder.append(value);
        }
        return builder.toString();
    }

    static String normalize(final String text) {
        return text.replaceAll("\\s+", "");
    }

    /**
     * Replaces comments and string/char literals with spaces. Newlines are kept, so offsets and line numbers stay
     * meaningful and a brace inside a comment or a string cannot break the brace matching.
     */
    public static String blankCommentsAndLiterals(final String source) {
        final StringBuilder out = new StringBuilder(source);
        final int length = source.length();
        int i = 0;
        while (i < length) {
            final char c = source.charAt(i);
            if (c == '/' && i + 1 < length && source.charAt(i + 1) == '/') {
                while (i < length && source.charAt(i) != '\n') {
                    out.setCharAt(i++, ' ');
                }
            } else if (c == '/' && i + 1 < length && source.charAt(i + 1) == '*') {
                out.setCharAt(i++, ' ');
                out.setCharAt(i++, ' ');
                while (i + 1 < length && !(source.charAt(i) == '*' && source.charAt(i + 1) == '/')) {
                    if (source.charAt(i) != '\n') {
                        out.setCharAt(i, ' ');
                    }
                    i++;
                }
                if (i + 1 < length) {
                    out.setCharAt(i++, ' ');
                    out.setCharAt(i++, ' ');
                }
            } else if (c == '"' || c == '\'') {
                final char quote = c;
                out.setCharAt(i++, ' ');
                while (i < length) {
                    final char d = source.charAt(i);
                    if (d == '\\') {
                        out.setCharAt(i++, ' ');
                        if (i < length) {
                            if (source.charAt(i) != '\n') {
                                out.setCharAt(i, ' ');
                            }
                            i++;
                        }
                        continue;
                    }
                    if (d == quote) {
                        out.setCharAt(i++, ' ');
                        break;
                    }
                    if (d != '\n') {
                        out.setCharAt(i, ' ');
                    }
                    i++;
                }
            } else {
                i++;
            }
        }
        return out.toString();
    }

    /** Index of the {@code {} that opens a method body, allowing for a {@code throws} clause; -1 if there is none. */
    private static int bodyBrace(final String code, final int from) {
        int i = skipWhitespace(code, from);
        final int length = code.length();
        while (i < length) {
            final char c = code.charAt(i);
            if (c == '{') {
                return i;
            }
            if (c == ';' || c == '=' || c == ')') {
                return -1;
            }
            if (c == '(') {
                final int close = match(code, i, '(', ')');
                if (close < 0) {
                    return -1;
                }
                i = close + 1;
                continue;
            }
            i++;
        }
        return -1;
    }

    private static int match(final String text, final int open, final char openChar, final char closeChar) {
        int depth = 0;
        for (int i = open; i < text.length(); i++) {
            final char c = text.charAt(i);
            if (c == openChar) {
                depth++;
            } else if (c == closeChar) {
                depth--;
                if (depth == 0) {
                    return i;
                }
            }
        }
        return -1;
    }

    private static int skipWhitespace(final String text, final int from) {
        int i = Math.max(from, 0);
        while (i < text.length() && Character.isWhitespace(text.charAt(i))) {
            i++;
        }
        return i;
    }

    private static int previousNonWhitespace(final String text, final int from) {
        int i = Math.min(from, text.length() - 1);
        while (i >= 0 && Character.isWhitespace(text.charAt(i))) {
            i--;
        }
        return i;
    }

    private static int tokenStart(final String text, final int from) {
        int i = Math.max(from, 0);
        while (i > 0 && isIdentifierPart(text.charAt(i - 1))) {
            i--;
        }
        return i;
    }

    private static boolean isIdentifierStart(final char c) {
        return Character.isJavaIdentifierStart(c);
    }

    private static boolean isIdentifierPart(final char c) {
        return Character.isJavaIdentifierPart(c);
    }

    private static int lineOf(final String text, final int index) {
        int line = 1;
        for (int i = 0; i < index && i < text.length(); i++) {
            if (text.charAt(i) == '\n') {
                line++;
            }
        }
        return line;
    }
}
