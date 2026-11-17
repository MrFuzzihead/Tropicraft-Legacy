package net.tropicraft.lint;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

/**
 * Proves the guard in {@link SelfRecursionLint} catches the issue #54 shape and stays quiet on everything that only
 * looks like it (intentional recursion, delegation to a differently named helper, casts, comments and strings
 * containing fake code). Without these fixtures, a scanner that silently stopped matching would still pass the source
 * check in {@code SelfRecursionGuardTest}.
 */
public class SelfRecursionLintTest {

    private static List<SelfRecursionLint.Finding> scan(final String... lines) {
        return SelfRecursionLint.scanSource("Fixture.java", String.join("\n", lines));
    }

    @Test
    public void catchesTheEasterIslandHeadRegression() {
        // Exactly what RenderEIH looked like in 6.2.6: the cast that selected the working EntityLiving overload was
        // "cleaned up", leaving the Entity overload calling itself.
        final List<SelfRecursionLint.Finding> findings = scan(
            "public class Fixture extends RenderLiving {",
            "    public void doRender(final EntityLiving entityliving, final double d, final double d1,",
            "        final double d2, final float f, final float f1) {",
            "        super.doRender(entityliving, d, d1, d2, f, f1);",
            "    }",
            "",
            "    public void doRender(final Entity entity, final double d, final double d1, final double d2,",
            "        final float f, final float f1) {",
            "        this.doRender(entity, d, d1, d2, f, f1);",
            "    }",
            "}");
        Assert.assertEquals("expected the self-delegating overload to be reported", 1, findings.size());
        Assert.assertEquals("Fixture.java:9", findings.get(0).file + ":" + findings.get(0).line);
        Assert.assertEquals(
            "doRender",
            findings.get(0)
                .methodName());
    }

    @Test
    public void catchesTheBareUnqualifiedCall() {
        final List<SelfRecursionLint.Finding> findings = scan(
            "public class Fixture {",
            "    public void draw(final Entity entity, final double x) {",
            "        draw(entity, x);",
            "    }",
            "}");
        Assert.assertEquals(1, findings.size());
    }

    @Test
    public void catchesItEvenWhenTheBodyDoesOtherWorkFirst() {
        final List<SelfRecursionLint.Finding> findings = scan(
            "public class Fixture {",
            "    public boolean test(final String value) {",
            "        if (value == null) { return false; }",
            "        return test(value);",
            "    }",
            "}");
        Assert.assertEquals(1, findings.size());
    }

    @Test
    public void acceptsTheFixedRenderer() {
        final List<SelfRecursionLint.Finding> findings = scan(
            "public class Fixture extends RenderLiving {",
            "    public void renderEIH(final EntityEIH entityeih, final double d, final double d1,",
            "        final double d2, final float f, final float f1) {",
            "        super.doRender(entityeih, d, d1, d2, f, f1);",
            "    }",
            "",
            "    public void doRender(final Entity entity, final double d, final double d1, final double d2,",
            "        final float f, final float f1) {",
            "        this.renderEIH((EntityEIH) entity, d, d1, d2, f, f1);",
            "    }",
            "}");
        Assert.assertTrue(findings.toString(), findings.isEmpty());
    }

    @Test
    public void acceptsTheCastThatKeepsAnOverloadDelegationWorking() {
        // 6.2.5 and earlier. The cast selects the EntityLiving overload, so this is not recursion.
        final List<SelfRecursionLint.Finding> findings = scan(
            "public class Fixture {",
            "    public void doRender(final EntityLiving entityliving, final double d) {",
            "        super.doRender(entityliving, d);",
            "    }",
            "",
            "    public void doRender(final Entity entity, final double d) {",
            "        this.doRender((EntityLiving) entity, d);",
            "    }",
            "}");
        Assert.assertTrue(findings.toString(), findings.isEmpty());
    }

    @Test
    public void acceptsIntentionalRecursionThatMovesTowardABaseCase() {
        final List<SelfRecursionLint.Finding> findings = scan(
            "public class Fixture {",
            "    protected int getDistanceToBase(final int i, final int height) {",
            "        if (i == 5) { return i; }",
            "        if (isLiquid(height - i)) { return this.getDistanceToBase(i + 1, height); }",
            "        return i;",
            "    }",
            "",
            "    protected void generateCaveNode(final long seed, final int section, final int length) {",
            "        while (section < length) {",
            "            this.generateCaveNode(random.nextLong(), section, length - 1);",
            "        }",
            "    }",
            "",
            "    protected void walk(final Node node) {",
            "        this.walk(node.getParent());",
            "    }",
            "}");
        Assert.assertTrue(findings.toString(), findings.isEmpty());
    }

    @Test
    public void acceptsSameNameCallsWithADifferentArity() {
        final List<SelfRecursionLint.Finding> findings = scan(
            "public class Fixture {",
            "    public boolean isPageVisible(final int i) {",
            "        return this.isPageVisible(this.getPageName(i));",
            "    }",
            "",
            "    public boolean isPageVisible(final String entry) {",
            "        return this.progress().isVisible(entry);",
            "    }",
            "",
            "    public void draw(final Entity entity) {",
            "        this.draw(entity, 0.0);",
            "    }",
            "",
            "    public void draw(final Entity entity, final double scale) {",
            "        render(entity, scale);",
            "    }",
            "}");
        Assert.assertTrue(findings.toString(), findings.isEmpty());
    }

    @Test
    public void acceptsSameNameCallsOnAnotherReceiver() {
        final List<SelfRecursionLint.Finding> findings = scan(
            "public class Fixture {",
            "    public void render(final Entity entity) {",
            "        this.delegate.render(entity);",
            "        other.render(entity);",
            "    }",
            "",
            "    public void draw(final int x) {",
            "        super.draw(x);",
            "    }",
            "}");
        Assert.assertTrue(findings.toString(), findings.isEmpty());
    }

    @Test
    public void ignoresCodeInsideCommentsAndStrings() {
        final List<SelfRecursionLint.Finding> findings = scan(
            "public class Fixture {",
            "    // this.doRender(entity, d, d1, d2, f, f1);",
            "    /* private void draw(final Entity e) { this.draw(e); } */",
            "    private void render(final Entity entity) {",
            "        log(\"this.render(entity)\");",
            "        log('{');",
            "    }",
            "}");
        Assert.assertTrue(findings.toString(), findings.isEmpty());
    }

    @Test
    public void reportsEachRecursiveMethodSeparately() {
        final List<SelfRecursionLint.Finding> findings = scan(
            "public class Fixture {",
            "    public void a(final int x) { this.a(x); }",
            "    public void b(final int x) { this.b(x); }",
            "    public void c(final int x) { this.a(x); }",
            "}");
        Assert.assertEquals(findings.toString(), 2, findings.size());
    }
}
