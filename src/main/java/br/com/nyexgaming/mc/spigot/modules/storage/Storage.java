package br.com.nyexgaming.mc.spigot.modules.storage;

import br.com.nyexgaming.mc.spigot.NyexPlugin;
import br.com.nyexgaming.mc.spigot.modules.service.Service;
import br.com.nyexgaming.mc.spigot.modules.service.ServiceExecutor;
import br.com.nyexgaming.sdk.endpoints.transactions.Transaction;
import org.bukkit.entity.Player;
import tk.wesleyramos.mclib.Config;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Storage extends ServiceExecutor {

    private final Config config;
    private final Map<UUID, StorageView> views;

    private Transaction[] transactions;

    public Storage(Service service) {
        super(service);

        this.config = new Config(NyexPlugin.getInstance(), "storage.yml").saveDefaultFile();
        this.views = new HashMap<>();
    }

    @Override
    public void execute(Transaction[] transactions) {
        this.transactions = transactions;

        for (StorageView view : this.views.values()) {
            view.update();
        }
    }

    public void create(Player player) {
        if (views.containsKey(player.getUniqueId())) {
            views.get(player.getUniqueId()).createPage(player, 1);
            return;
        }

        StorageView view = new StorageView(player.getName(), this);

        view.update();
        view.createPage(player, 1);

        views.put(player.getUniqueId(), view);
    }

    public Config getConfig() {
        return config;
    }

    public Map<UUID, StorageView> getViews() {
        return views;
    }

    public Transaction[] getTransactions() {
        return transactions;
    }
}
