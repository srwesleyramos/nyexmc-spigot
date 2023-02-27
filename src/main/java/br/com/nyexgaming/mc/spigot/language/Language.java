package br.com.nyexgaming.mc.spigot.language;

import br.com.nyexgaming.mc.spigot.NyexPlugin;
import br.com.nyexgaming.mc.spigot.database.models.UserModel;
import br.com.nyexgaming.mc.spigot.language.lang.Translation;
import br.com.nyexgaming.mc.spigot.service.Service;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.zip.ZipFile;

public class Language {

    private final Map<String, Translation> translations = new HashMap<>();
    private final Service service;

    public Language(Service service) {
        this.service = service;
    }

    public void load() throws IOException {
        File folder = new File(service.config.getFile().getParentFile(), "languages/");

        if (!folder.exists()) {
            saveDefaultFiles();
        }

        loadLanguages(folder);

        Bukkit.getConsoleSender().sendMessage("§9[Nyex Spigot]: §bOK! §7The §blanguage module §7started correctly.");
    }

    public void loadLanguages(File folder) {
        for (File file : Objects.requireNonNull(folder.listFiles())) {
            if (!file.isDirectory()) continue;

            translations.put(file.getName().toLowerCase(), new Translation(file));
        }
    }

    public void unload() {
        translations.clear();

        Bukkit.getConsoleSender().sendMessage("§9[Nyex Spigot]: §bOK! §7The §blanguage §fmodule has been terminated.");
    }

    public void saveDefaultFiles() throws IOException {
        ZipFile zipFile = new ZipFile(NyexPlugin.getInstance().getCurrentFile());

        zipFile.stream().filter(e -> e.getName().startsWith("languages/")).forEach(e -> {
            File file = new File(service.config.getFile().getParentFile(), e.getName());

            if (e.isDirectory()) return;

            file.getParentFile().mkdirs();

            try {
                Files.copy(zipFile.getInputStream(e), file.toPath());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        zipFile.close();
    }

    public Translation getTranslationByUser(CommandSender player) {
        return getTranslationByUser(player.getName());
    }

    public Translation getTranslationByUser(String player) {
        UserModel user = service.database.getUserByName(player);

        if (getTranslation(user.getLanguage()) == null) {
            user.setLanguage(service.config.getString("languages.default"));
        }

        return getTranslation(user.getLanguage());
    }

    public Translation getTranslation(String name) {
        return translations.get(name.toLowerCase());
    }

    public Translation getDefaultTranslation() {
        return getTranslation(service.config.getString("languages.default"));
    }

    public Collection<Translation> getTranslations() {
        return translations.values();
    }
}
