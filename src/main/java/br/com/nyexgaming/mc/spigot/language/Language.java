package br.com.nyexgaming.mc.spigot.language;

import br.com.nyexgaming.mc.spigot.NyexPlugin;
import br.com.nyexgaming.mc.spigot.service.Service;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import tk.wesleyramos.mclib.Config;

import java.util.List;
import java.util.stream.Collectors;

public class Language {

    private final Config config;
    private final Service service;

    public Language(Service service) {
        this.service = service;

        this.config = new Config(NyexPlugin.getInstance(), "language.yml").saveDefaultFile();
    }

    public void reload() {
        config.reload();

        Bukkit.getConsoleSender().sendMessage("§9[Nyex Spigot]: §aV §fo módulo §7línguas §finiciou corretamente.");
    }

    public void unload() {
        Bukkit.getConsoleSender().sendMessage("§9[Nyex Spigot]: §aV §fo módulo §7línguas §ffoi finalizado.");
    }

    public List<String> get(String player, String path, Object... placeholders) {
        return config.getStringList(path).stream().map(text -> {
            if (placeholders == null || placeholders.length <= 1) {
                return text.replace("&", "§");
            }

            for (int i = 0; i < placeholders.length; i += 2) {
                text = text.replace(String.valueOf(placeholders[i]), String.valueOf(placeholders[i + 1]));
            }

            return text.replace("&", "§");
        }).collect(Collectors.toList());
    }

    public void send(HumanEntity player, String path, Object... placeholders) {
        get(player.getName(), path, placeholders).forEach(player::sendMessage);
    }
}
