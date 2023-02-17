package br.com.nyexgaming.mc.spigot.modules.service;

import br.com.nyexgaming.mc.spigot.NyexPlugin;
import br.com.nyexgaming.mc.spigot.modules.storage.Storage;
import br.com.nyexgaming.sdk.NyexGaming;
import br.com.nyexgaming.sdk.endpoints.transactions.Transaction;
import br.com.nyexgaming.sdk.http.exceptions.NetworkErrorException;
import br.com.nyexgaming.sdk.http.exceptions.RequestFailedException;
import br.com.nyexgaming.sdk.http.exceptions.TokenFailureException;
import org.bukkit.Bukkit;
import tk.wesleyramos.mclib.Config;

public class Service {

    private final Config config;

    private NyexGaming sdk;
    private ServiceTask task;

    private ServiceBroadcast broadcast;
    private ServiceExecutor executor;

    public Service() {
        this.config = new Config(NyexPlugin.getInstance(), "config.yml").saveDefaultFile();
    }

    public void reload() {
        this.config.reload();

        if (this.task != null && this.task.isAlive()) {
            this.task.interrupt();
        }

        this.sdk = new NyexGaming(this.config.getString("credentials.token-loja"), this.config.getString("credentials.token-server"));

        try {
            Transaction[] transactions = this.sdk.getTransactions();

            this.broadcast = new ServiceBroadcast(this);

            this.executor = this.isStorageActivated() ? new Storage(this) : new ServiceExecutor(this);
            this.executor.execute(transactions);

            this.task = new ServiceTask(this);
            this.task.start();

            Bukkit.getConsoleSender().sendMessage("§9[Nyex Gaming]: §fTodos os modulos foram carregados com sucesso.");
        } catch (NetworkErrorException | RequestFailedException | TokenFailureException e) {
            Bukkit.getConsoleSender().sendMessage("§4[Nyex Gaming]: §cOcorreu um erro ao comunicar-se com nossos servidores.");
            Bukkit.getConsoleSender().sendMessage("§4[Nyex Gaming]: §c" + e.getMessage());
        }
    }

    public void unload() {
        if (this.task != null && this.task.isAlive()) {
            this.task.interrupt();
        }

        Bukkit.getConsoleSender().sendMessage("§9[Nyex Gaming]: §fTodos os modulos foram descarregados com sucesso.");
    }

    public boolean isBroadcastActivated() {
        return this.config.getBoolean("modules.broadcast.status");
    }

    public boolean isStorageActivated() {
        return this.config.getBoolean("modules.storage.status");
    }

    public boolean isDonationsActivated() {
        return this.config.getBoolean("modules.storage.donations");
    }

    public Config getConfig() {
        return config;
    }

    public NyexGaming getSdk() {
        return sdk;
    }

    public ServiceTask getTask() {
        return task;
    }

    public ServiceBroadcast getBroadcast() {
        return broadcast;
    }

    public ServiceExecutor getExecutor() {
        return executor;
    }
}
