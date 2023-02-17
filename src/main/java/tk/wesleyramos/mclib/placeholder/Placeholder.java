package tk.wesleyramos.mclib.placeholder;

import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;

public abstract class Placeholder {

    private final Plugin plugin;
    private final String name;

    public Placeholder(Plugin plugin, String name) {
        this.plugin = plugin;
        this.name = name;
    }

    public abstract String getResult(OfflinePlayer player);

    public Plugin getPlugin() {
        return plugin;
    }

    public String getName() {
        return name;
    }
}
