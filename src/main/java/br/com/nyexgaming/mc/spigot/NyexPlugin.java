package br.com.nyexgaming.mc.spigot;

import br.com.nyexgaming.mc.spigot.modules.storage.StorageCommand;
import br.com.nyexgaming.mc.spigot.modules.storage.events.CitizensEvents;
import br.com.nyexgaming.mc.spigot.modules.storage.events.DonationEvents;
import br.com.nyexgaming.mc.spigot.service.Service;
import br.com.nyexgaming.mc.spigot.service.ServiceCommand;
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
        Bukkit.getConsoleSender().sendMessage("§9[Nyex Spigot]: §b------------------------------------------------");
        Bukkit.getConsoleSender().sendMessage("§9[Nyex Spigot]: §b                                                ");
        Bukkit.getConsoleSender().sendMessage("§9[Nyex Spigot]: §d              Nyex Gaming - Spigot              ");
        Bukkit.getConsoleSender().sendMessage("§9[Nyex Spigot]: §d                    V 1.0.0                     ");
        Bukkit.getConsoleSender().sendMessage("§9[Nyex Spigot]: §b                                                ");
        Bukkit.getConsoleSender().sendMessage("§9[Nyex Spigot]: §e ⇝ Loja: §fhttps://nyexgaming.com.br/           ");
        Bukkit.getConsoleSender().sendMessage("§9[Nyex Spigot]: §e ⇝ Discord: §fhttps://comunidade.nyexgaming.com.br/");
        Bukkit.getConsoleSender().sendMessage("§9[Nyex Spigot]: §b                                                ");
        Bukkit.getConsoleSender().sendMessage("§9[Nyex Spigot]: §b------------------------------------------------");
        Bukkit.getConsoleSender().sendMessage("§9[Nyex Spigot]: §b                                                ");

        this.service = new Service();
        this.service.reload();

        getCommand("nyexgaming").setExecutor(new ServiceCommand(service));
        getCommand("nyexgaming").setTabCompleter(new ServiceCommand(service));

        getCommand("nyexstorage").setExecutor(new StorageCommand(service));
        getCommand("nyexstorage").setTabCompleter(new StorageCommand(service));

        if (Bukkit.getPluginManager().isPluginEnabled("Citizens")) {
            Bukkit.getPluginManager().registerEvents(new CitizensEvents(service.storage), NyexPlugin.getInstance());
        }

        getServer().getPluginManager().registerEvents(new DonationEvents(service.storage), this);

        getServer().getPluginManager().registerEvents(new ViewEvents(this), this);

        Bukkit.getConsoleSender().sendMessage("§9[Nyex Spigot]: §b                                                ");
        Bukkit.getConsoleSender().sendMessage("§9[Nyex Spigot]: §b------------------------------------------------");
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage("§9[Nyex Spigot]: §b------------------------------------------------");
        Bukkit.getConsoleSender().sendMessage("§9[Nyex Spigot]: §b                                                ");
        Bukkit.getConsoleSender().sendMessage("§9[Nyex Spigot]: §d              Nyex Gaming - Spigot              ");
        Bukkit.getConsoleSender().sendMessage("§9[Nyex Spigot]: §d                    V 1.0.0                     ");
        Bukkit.getConsoleSender().sendMessage("§9[Nyex Spigot]: §b                                                ");
        Bukkit.getConsoleSender().sendMessage("§9[Nyex Spigot]: §e ⇝ Loja: §fhttps://nyexgaming.com.br/           ");
        Bukkit.getConsoleSender().sendMessage("§9[Nyex Spigot]: §e ⇝ Discord: §fhttps://comunidade.nyexgaming.com.br/ ");
        Bukkit.getConsoleSender().sendMessage("§9[Nyex Spigot]: §b                                                ");
        Bukkit.getConsoleSender().sendMessage("§9[Nyex Spigot]: §b------------------------------------------------");

        this.service.unload();

        Bukkit.getConsoleSender().sendMessage("§9[Nyex Spigot]: §b                                                ");
        Bukkit.getConsoleSender().sendMessage("§9[Nyex Spigot]: §b------------------------------------------------");
    }

    public Service getService() {
        return this.service;
    }
}
