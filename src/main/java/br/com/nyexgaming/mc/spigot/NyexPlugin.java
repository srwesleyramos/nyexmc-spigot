package br.com.nyexgaming.mc.spigot;

import br.com.nyexgaming.mc.spigot.modules.service.Service;
import br.com.nyexgaming.mc.spigot.modules.service.ServiceCommand;
import br.com.nyexgaming.mc.spigot.modules.storage.StorageCommand;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import tk.wesleyramos.mclib.view.ViewEvents;

public class NyexPlugin extends JavaPlugin {

    private Service service;

    public static NyexPlugin getInstance() {
        return getPlugin(NyexPlugin.class);
    }

    @Override
    public void onEnable() {
        Bukkit.getConsoleSender().sendMessage("§9[Nyex Bungee]: §b------------------------------------------------");
        Bukkit.getConsoleSender().sendMessage("§9[Nyex Bungee]: §b                                                ");
        Bukkit.getConsoleSender().sendMessage("§9[Nyex Bungee]: §d              Nyex Gaming - Spigot              ");
        Bukkit.getConsoleSender().sendMessage("§9[Nyex Bungee]: §d                    V 1.0.0                     ");
        Bukkit.getConsoleSender().sendMessage("§9[Nyex Bungee]: §b                                                ");
        Bukkit.getConsoleSender().sendMessage("§9[Nyex Bungee]: §e ⇝ Loja: §fhttps://nyexgaming.com.br/           ");
        Bukkit.getConsoleSender().sendMessage("§9[Nyex Bungee]: §e ⇝ Discord: §fhttps://comunidade.nyexgaming.com.br/");
        Bukkit.getConsoleSender().sendMessage("§9[Nyex Bungee]: §b                                                ");
        Bukkit.getConsoleSender().sendMessage("§9[Nyex Bungee]: §b------------------------------------------------");
        Bukkit.getConsoleSender().sendMessage("§9[Nyex Bungee]: §b                                                ");

        this.service = new Service();
        this.service.reload();

        getCommand("nyexgaming").setExecutor(new ServiceCommand(this.service));
        getCommand("nyexgaming").setTabCompleter(new ServiceCommand(this.service));

        getCommand("nyexstorage").setExecutor(new StorageCommand(this.service));
        getCommand("nyexstorage").setTabCompleter(new StorageCommand(this.service));

        getServer().getPluginManager().registerEvents(new ViewEvents(this), this);

        Bukkit.getConsoleSender().sendMessage("§9[Nyex Bungee]: §b                                                ");
        Bukkit.getConsoleSender().sendMessage("§9[Nyex Bungee]: §b------------------------------------------------");
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage("§9[Nyex Bungee]: §b------------------------------------------------");
        Bukkit.getConsoleSender().sendMessage("§9[Nyex Bungee]: §b                                                ");
        Bukkit.getConsoleSender().sendMessage("§9[Nyex Bungee]: §d              Nyex Gaming - Spigot              ");
        Bukkit.getConsoleSender().sendMessage("§9[Nyex Bungee]: §d                    V 1.0.0                     ");
        Bukkit.getConsoleSender().sendMessage("§9[Nyex Bungee]: §b                                                ");
        Bukkit.getConsoleSender().sendMessage("§9[Nyex Bungee]: §e ⇝ Loja: §fhttps://nyexgaming.com.br/           ");
        Bukkit.getConsoleSender().sendMessage("§9[Nyex Bungee]: §e ⇝ Discord: §fhttps://comunidade.nyexgaming.com.br/ ");
        Bukkit.getConsoleSender().sendMessage("§9[Nyex Bungee]: §b                                                ");
        Bukkit.getConsoleSender().sendMessage("§9[Nyex Bungee]: §b------------------------------------------------");

        this.service.unload();

        Bukkit.getConsoleSender().sendMessage("§9[Nyex Bungee]: §b                                                ");
        Bukkit.getConsoleSender().sendMessage("§9[Nyex Bungee]: §b------------------------------------------------");
    }

    public Service getService() {
        return this.service;
    }
}
