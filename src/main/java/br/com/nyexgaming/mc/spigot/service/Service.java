package br.com.nyexgaming.mc.spigot.service;

import br.com.nyexgaming.mc.spigot.NyexPlugin;
import br.com.nyexgaming.mc.spigot.database.Database;
import br.com.nyexgaming.mc.spigot.modules.broadcast.Broadcast;
import br.com.nyexgaming.mc.spigot.modules.language.Language;
import br.com.nyexgaming.mc.spigot.modules.storage.Storage;
import br.com.nyexgaming.sdk.NyexGaming;
import br.com.nyexgaming.sdk.http.exceptions.NetworkErrorException;
import br.com.nyexgaming.sdk.http.exceptions.RequestFailedException;
import br.com.nyexgaming.sdk.http.exceptions.TokenFailureException;
import org.bukkit.Bukkit;
import tk.wesleyramos.mclib.Config;

import java.sql.SQLException;

public class Service {

    public final Broadcast broadcast;
    public final Config config;
    public final Database database;
    public final ServiceExecutor executor;
    public final Language language;
    public final Storage storage;
    public final ServiceTask task;

    public NyexGaming sdk;

    public Service() {
        this.config = new Config(NyexPlugin.getInstance(), "config.yml").saveDefaultFile();

        this.broadcast = new Broadcast(this);
        this.database = new Database(this);
        this.executor = new ServiceExecutor(this);
        this.language = new Language(this);
        this.storage = new Storage(this);

        this.task = new ServiceTask(this);
        this.task.start();
    }

    public void reload() {
        try {
            config.reload();
            database.reload();
            language.reload();
            broadcast.reload();
            storage.reload();

            sdk = new NyexGaming(
                    this.config.getString("credentials.access-store"),
                    this.config.getString("credentials.access-server")
            );
            sdk.getTransactions();

            Bukkit.getConsoleSender().sendMessage("§9[Nyex Gaming]: §fTodos os módulos foram carregados e ativos.");
        } catch (NetworkErrorException | RequestFailedException | TokenFailureException | SQLException |
                 ClassNotFoundException e) {
            Bukkit.getConsoleSender().sendMessage("§4[Nyex Gaming]: §cOcorreu um erro ao iniciar os nossos módulos.");
            Bukkit.getConsoleSender().sendMessage("§4[Nyex Gaming]: §cMensagem: §4" + e.getMessage());

            sdk = null;
        }
    }

    public void unload() {
        sdk = null;

        task.interrupt();
        storage.unload();
        broadcast.unload();
        language.unload();
        database.unload();

        Bukkit.getConsoleSender().sendMessage("§9[Nyex Gaming]: §fTodos os módulos foram descarregados.");
    }

    public boolean isBroadcastActivated() {
        return config.getBoolean("modules.broadcast.status");
    }

    public boolean isStorageActivated() {
        return config.getBoolean("modules.storage.status");
    }

    public boolean isDonationsActivated() {
        return config.getBoolean("modules.storage.donations");
    }

    public boolean isNpcsActivated() {
        return config.getBoolean("modules.storage.npcs") && Bukkit.getPluginManager().isPluginEnabled("Citizens");
    }

    public ServiceExecutor getExecutor() {
        return isStorageActivated() ? storage : executor;
    }
}
