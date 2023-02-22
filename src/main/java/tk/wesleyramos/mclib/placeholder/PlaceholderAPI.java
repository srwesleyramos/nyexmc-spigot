package tk.wesleyramos.mclib.placeholder;

import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlaceholderAPI {

    public static final String CHAR_LEFT = "<", CHAR_RIGHT = ">";
    public static final PlaceholderAPI SERVER = new PlaceholderAPI();

    private final Map<Plugin, List<Placeholder>> placeholders = new HashMap<>();

    public void register(Placeholder placeholder) {
        if (placeholder == null || placeholder.getPlugin() == null) return;

        List<Placeholder> list = placeholders.getOrDefault(placeholder.getPlugin(), new ArrayList<>());
        list.add(placeholder);
        placeholders.put(placeholder.getPlugin(), list);
    }

    public void unregister(Plugin plugin, String name) {
        if (plugin == null || name == null) return;

        List<Placeholder> list = placeholders.getOrDefault(plugin, new ArrayList<>());
        list.removeIf(p -> name.equals(p.getName()));
        placeholders.put(plugin, list);
    }

    public String replace(OfflinePlayer player, String string) {
        if (string == null) return "";

        Pattern pattern = Pattern.compile("(?<=" + CHAR_LEFT + ")(.*?)(?=" + CHAR_RIGHT + ")");
        Matcher matcher = pattern.matcher(string);

        while (matcher.find()) {
            String displayName = matcher.group();
            Placeholder placeholder = getByName(displayName.toLowerCase());

            if (placeholder == null) continue;

            string = string.replace(CHAR_LEFT + displayName + CHAR_RIGHT, placeholder.getResult(player));
        }

        return string.replace("&", "§");
    }

    public Placeholder getByName(String name) {
        for (List<Placeholder> l : placeholders.values()) {
            for (Placeholder p : l) {
                if (name.equals(p.getName())) {
                    return p;
                }
            }
        }

        return null;
    }
}
