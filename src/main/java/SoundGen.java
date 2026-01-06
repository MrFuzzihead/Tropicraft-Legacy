import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.*;

public class SoundGen {

    public static void main(final String[] args) {
        try {
            final ZipFile zip = new ZipFile("F:/dev/code/old workspaces/forge-1.7.10-tropicraft/pkg/assets.zip");
            final Enumeration<? extends ZipEntry> entries = zip.entries();
            final ArrayList<String> soundNames = new ArrayList<String>();
            final String initialPath = "assets/tropicraft/";
            while (entries.hasMoreElements()) {
                final ZipEntry entry = entries.nextElement();
                if (!entry.isDirectory() && entry.toString()
                    .endsWith(".ogg")) {
                    String name = entry.toString();
                    name = name.substring(initialPath.length());
                    if (name.startsWith("sounds")) {
                        name = name.substring("sounds".length() + 1);
                    }
                    soundNames.add(name.substring(0, name.length() - 4));
                }
            }
            final HashMap<String, ArrayList<String>> numbered = new HashMap<String, ArrayList<String>>();
            for (String soundName : soundNames) {
                final String oriName;
                String name2 = oriName = soundName;
                boolean isNum = true;
                boolean isNumbered = false;
                while (isNum) {
                    try {
                        Integer.parseInt(name2.substring(name2.length() - 1));
                        name2 = name2.substring(0, name2.length() - 1);
                        isNumbered = true;
                    } catch (NumberFormatException e3) {
                        isNum = false;
                    }
                }
                if (isNumbered) {
                    ArrayList<String> numbers = numbered.get(name2);
                    if (numbers == null) {
                        numbers = new ArrayList<>();
                        numbered.put(name2, numbers);
                    }
                    numbers.add(oriName);
                    Collections.sort(numbers);
                }
            }
            for (int i = soundNames.size() - 1; i >= 0; --i) {
                final String name2 = soundNames.get(i);
                for (final Map.Entry<String, ArrayList<String>> e : numbered.entrySet()) {
                    if (name2.startsWith(e.getKey()) && !name2.equals(e.getKey())) {
                        soundNames.remove(i);
                        if (soundNames.contains(e.getKey())) {
                            continue;
                        }
                        soundNames.add(i, e.getKey());
                    }
                }
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("\n\n");
            sb.append("{\n");
            for (int j = 0; j < soundNames.size(); ++j) {
                final String oriName2;
                final String name3 = oriName2 = soundNames.get(j);
                final boolean isRecord = name3.startsWith("records");
                final String category = isRecord ? "record" : (name3.contains("wpn_portal") ? "player" : "neutral");
                final String ingameName = name3.replaceAll("\\/", ".");
                sb.append("  \"")
                    .append(ingameName)
                    .append("\": { \n");
                sb.append("    \"category\": \"")
                    .append(category)
                    .append("\",\n");
                sb.append("    \"sounds\": [\n");
                if (isRecord) {
                    sb.append("      {\n");
                    sb.append("        \"name\": \"")
                        .append(name3)
                        .append("\",\n        \"stream\": true\n");
                    sb.append("      }\n");
                } else {
                    ArrayList<String> soundPaths = numbered.get(oriName2);
                    if (soundPaths == null) {
                        soundPaths = new ArrayList<>();
                        soundPaths.add(name3);
                    }
                    for (int k = 0; k < soundPaths.size(); ++k) {
                        sb.append("      \"")
                            .append(soundPaths.get(k))
                            .append("\"");
                        if (k != soundPaths.size() - 1) {
                            sb.append(",\n");
                        } else {
                            sb.append("\n");
                        }
                    }
                }
                sb.append("    ]\n");
                sb.append("  }");
                if (j != soundNames.size() - 1) {
                    sb.append(",\n");
                } else {
                    sb.append("\n");
                }
            }
            sb.append("}");
            System.out.println(sb);
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }
}
