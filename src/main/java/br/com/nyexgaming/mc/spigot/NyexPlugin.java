package br.com.nyexgaming.mc.spigot;

import br.com.nyexgaming.mc.spigot.language.LanguageCommand;
import br.com.nyexgaming.mc.spigot.language.LanguageEvents;
import br.com.nyexgaming.mc.spigot.service.Service;
import br.com.nyexgaming.mc.spigot.service.ServiceCommand;
import br.com.nyexgaming.mc.spigot.storage.StorageCommand;
import br.com.nyexgaming.mc.spigot.storage.events.CitizensEvents;
import br.com.nyexgaming.mc.spigot.storage.events.DonationEvents;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.java.JavaPlugin;
import tk.wesleyramos.mclib.view.ViewEvents;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Map;

public class NyexPlugin extends JavaPlugin {

    private Service service;

    public static NyexPlugin getInstance() {
        return getPlugin(NyexPlugin.class);
    }

    public static void register(String name, Command command) {
        try {
            final Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            bukkitCommandMap.setAccessible(true);

            CommandMap commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());
            commandMap.register(name, command);

            System.out.println("[PluginUtils]: O comando " + name + " foi registrado com sucesso.");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("[PluginUtils]: Erro ao tentar registrar o comando: " + name);
        }
    }

    public static void unregister(String name) {
        try {
            final Field f1 = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            f1.setAccessible(true);

            CommandMap commandMap = (CommandMap) f1.get(Bukkit.getServer());

            final Field f = commandMap.getClass().getDeclaredField("knownCommands");
            f.setAccessible(true);

            Map<String, Command> cmds = (Map<String, Command>) f.get(commandMap);
            cmds.remove(name);
            f.set(commandMap, cmds);
        } catch (Exception e) {
            System.out.println("[PluginUtils]: Erro ao tentar desregistrar o comando: " + name);
        }
    }

    @Override
    public void onEnable() {
        Bukkit.getConsoleSender().sendMessage("§9[Nyex Spigot]: §b------------------------------------------------");
        Bukkit.getConsoleSender().sendMessage("§9[Nyex Spigot]: §b                                                ");
        Bukkit.getConsoleSender().sendMessage("§9[Nyex Spigot]: §d              Nyex Gaming - Spigot              ");
        Bukkit.getConsoleSender().sendMessage("§9[Nyex Spigot]: §d                    V 2.0.0                     ");
        Bukkit.getConsoleSender().sendMessage("§9[Nyex Spigot]: §b                                                ");
        Bukkit.getConsoleSender().sendMessage("§9[Nyex Spigot]: §e ⇝ Loja: §fhttps://nyexgaming.com.br/           ");
        Bukkit.getConsoleSender().sendMessage("§9[Nyex Spigot]: §e ⇝ Discord: §fhttps://comunidade.nyexgaming.com.br/");
        Bukkit.getConsoleSender().sendMessage("§9[Nyex Spigot]: §b                                                ");
        Bukkit.getConsoleSender().sendMessage("§9[Nyex Spigot]: §b------------------------------------------------");
        Bukkit.getConsoleSender().sendMessage("§9[Nyex Spigot]: §b                                                ");

        this.service = new Service();
        this.service.reload();

        register("nyexlanguage", new LanguageCommand(service));

        getCommand("nyexgaming").setExecutor(new ServiceCommand(service));
        getCommand("nyexgaming").setTabCompleter(new ServiceCommand(service));

        getCommand("nyexstorage").setExecutor(new StorageCommand(service));
        getCommand("nyexstorage").setTabCompleter(new StorageCommand(service));

        if (Bukkit.getPluginManager().isPluginEnabled("Citizens")) {
            Bukkit.getPluginManager().registerEvents(new CitizensEvents(service.storage), NyexPlugin.getInstance());
        }

        getServer().getPluginManager().registerEvents(new DonationEvents(service.storage), this);
        getServer().getPluginManager().registerEvents(new LanguageEvents(service), this);

        getServer().getPluginManager().registerEvents(new ViewEvents(this), this);

        Bukkit.getConsoleSender().sendMessage("§9[Nyex Spigot]: §b                                                ");
        Bukkit.getConsoleSender().sendMessage("§9[Nyex Spigot]: §b------------------------------------------------");
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage("§9[Nyex Spigot]: §b------------------------------------------------");
        Bukkit.getConsoleSender().sendMessage("§9[Nyex Spigot]: §b                                                ");
        Bukkit.getConsoleSender().sendMessage("§9[Nyex Spigot]: §d              Nyex Gaming - Spigot              ");
        Bukkit.getConsoleSender().sendMessage("§9[Nyex Spigot]: §d                    V 2.0.0                     ");
        Bukkit.getConsoleSender().sendMessage("§9[Nyex Spigot]: §b                                                ");
        Bukkit.getConsoleSender().sendMessage("§9[Nyex Spigot]: §e ⇝ Loja: §fhttps://nyexgaming.com.br/           ");
        Bukkit.getConsoleSender().sendMessage("§9[Nyex Spigot]: §e ⇝ Discord: §fhttps://comunidade.nyexgaming.com.br/ ");
        Bukkit.getConsoleSender().sendMessage("§9[Nyex Spigot]: §b                                                ");
        Bukkit.getConsoleSender().sendMessage("§9[Nyex Spigot]: §b------------------------------------------------");

        this.service.unload();
        this.service.database.getTask().interrupt();

        Bukkit.getConsoleSender().sendMessage("§9[Nyex Spigot]: §b                                                ");
        Bukkit.getConsoleSender().sendMessage("§9[Nyex Spigot]: §b------------------------------------------------");
    }

    public File getCurrentFile() {
        return getFile();
    }

    public Service getService() {
        return this.service;
    }
}
