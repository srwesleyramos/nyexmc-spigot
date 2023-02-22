package br.com.nyexgaming.mc.spigot.modules.language;

import br.com.nyexgaming.mc.spigot.NyexPlugin;
import br.com.nyexgaming.mc.spigot.service.Service;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import tk.wesleyramos.mclib.Config;

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

    public void getAndSend(HumanEntity player, String path, Object... placeholders) {
        for (String text : config.getStringList(path)) {
            if (placeholders == null || placeholders.length <= 1) {
                player.sendMessage(text.replace("&", "§"));
                continue;
            }

            for (int i = 0; i < placeholders.length; i += 2) {
                text = text.replace(String.valueOf(placeholders[i]), String.valueOf(placeholders[i + 1]));
            }

            player.sendMessage(text.replace("&", "§"));
        }
    }
}
