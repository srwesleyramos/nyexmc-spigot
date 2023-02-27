package br.com.nyexgaming.mc.spigot.language.lang;

import br.com.nyexgaming.mc.spigot.NyexPlugin;
import br.com.nyexgaming.mc.spigot.language.impl.MessageImpl;
import br.com.nyexgaming.mc.spigot.language.impl.PreviewImpl;
import br.com.nyexgaming.mc.spigot.language.impl.StorageImpl;
import org.bukkit.command.CommandSender;
import tk.wesleyramos.mclib.Config;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

public class Translation implements MessageImpl, PreviewImpl, StorageImpl {


    private final String name;
    private final Config messages;
    private final Config preview;
    private final Config storage;

    public Translation(File folder) {
        this.name = folder.getName().toLowerCase();

        this.messages = new Config(NyexPlugin.getInstance(), "languages/" + folder.getName() + "/messages.yml");
        this.preview = new Config(NyexPlugin.getInstance(), "languages/" + folder.getName() + "/preview.yml");
        this.storage = new Config(NyexPlugin.getInstance(), "languages/" + folder.getName() + "/storage.yml");
    }

    @Override
    public List<String> getMessage(String path, String... placeholders) {
        return messages.getStringList(path).stream().map(text -> {
            if (placeholders == null || placeholders.length < 2) {
                return text.replace("&", "§");
            }

            for (int i = 0; i < placeholders.length; i += 2) {
                text = text.replace(placeholders[i], placeholders[i + 1]);
            }

            return text.replace("&", "§");
        }).collect(Collectors.toList());
    }

    @Override
    public void sendMessage(CommandSender player, String path, String... placeholders) {
        getMessage(path, placeholders).forEach(player::sendMessage);
    }

    public String getName() {
        return name;
    }
}
