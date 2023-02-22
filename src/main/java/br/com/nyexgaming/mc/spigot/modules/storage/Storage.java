package br.com.nyexgaming.mc.spigot.modules.storage;

import br.com.nyexgaming.mc.spigot.NyexPlugin;
import br.com.nyexgaming.mc.spigot.database.models.Shopping;
import br.com.nyexgaming.mc.spigot.modules.storage.views.ProductsView;
import br.com.nyexgaming.mc.spigot.service.Service;
import br.com.nyexgaming.mc.spigot.service.ServiceExecutor;
import br.com.nyexgaming.sdk.endpoints.transactions.TransactionStatus;
import br.com.nyexgaming.sdk.http.exceptions.NetworkErrorException;
import br.com.nyexgaming.sdk.http.exceptions.RequestFailedException;
import br.com.nyexgaming.sdk.http.exceptions.TokenFailureException;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import tk.wesleyramos.mclib.Serializer;
import tk.wesleyramos.mclib.placeholder.Placeholder;
import tk.wesleyramos.mclib.placeholder.PlaceholderAPI;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class Storage extends ServiceExecutor {

    public final StorageConfig config;

    public final Map<Integer, Location> npcs;
    public final Map<String, ProductsView> views;
    public final Map<Long, Shopping> products;

    public Storage(Service service) {
        super(service);

        this.config = new StorageConfig(this);

        this.products = new HashMap<>();
        this.npcs = new HashMap<>();
        this.views = new HashMap<>();
    }

    public void reload() throws SQLException {
        if (!service.isStorageActivated()) {
            Bukkit.getConsoleSender().sendMessage("§9[Nyex Spigot]: §4X §co módulo §4armazém §cnão foi ativo pois está desativado.");
            Bukkit.getConsoleSender().sendMessage("§9[Nyex Spigot]: §4X §co módulo §4armazém/doadores §cnão foi ativo pois o armazém está desativado.");
            Bukkit.getConsoleSender().sendMessage("§9[Nyex Spigot]: §4X §co módulo §4armazém/npcs §cnão foi ativo pois o armazém está desativado.");
            return;
        }

        this.config.reload();

        this.reloadDonations();
        this.reloadNpcs();

        Bukkit.getConsoleSender().sendMessage("§9[Nyex Spigot]: §aV §fo módulo §7armazém §finiciou corretamente.");
    }

    public void reloadDonations() throws SQLException {
        if (!service.isDonationsActivated()) {
            Bukkit.getConsoleSender().sendMessage("§9[Nyex Spigot]: §4X §co módulo §4armazém/doadores §cnão foi ativo pois está desativado.");
            return;
        }

        PreparedStatement statement = service.database.connection.prepareStatement("SELECT * FROM `nyex_shopping`;");
        ResultSet query = statement.executeQuery();

        while (query.next()) {
            Shopping shopping = new Shopping();
            shopping.id_transacao = query.getLong("id");
            shopping.donated = query.getBoolean("donated");
            shopping.recipient = query.getString("recipient");

            products.put(shopping.id_transacao, shopping);
        }

        Bukkit.getConsoleSender().sendMessage("§9[Nyex Spigot]: §aV §fo módulo §7armazém/doadores §finiciou corretamente.");
    }

    public void reloadNpcs() throws SQLException {
        if (!service.isNpcsActivated()) {
            Bukkit.getConsoleSender().sendMessage("§9[Nyex Spigot]: §4X §co módulo §4armazém/npcs §cnão foi ativo pois está desativado.");
            return;
        }

        PreparedStatement statement = service.database.connection.prepareStatement("SELECT * FROM `nyex_citizens`;");
        ResultSet query = statement.executeQuery();

        while (query.next()) {
            npcs.put(query.getInt("id"), Serializer.LOCATION.deserialize(query.getString("location")));
        }

        Bukkit.getConsoleSender().sendMessage("§9[Nyex Spigot]: §aV §fo módulo §7armazém/npcs §finiciou corretamente.");
    }

    public void unload() {
        if (!service.isStorageActivated()) return;

        if (service.isDonationsActivated()) {
            Bukkit.getConsoleSender().sendMessage("§9[Nyex Spigot]: §aV §fo módulo §7armazém/doadores §ffoi finalizado.");
        }

        if (service.isNpcsActivated()) {
            Bukkit.getConsoleSender().sendMessage("§9[Nyex Spigot]: §aV §fo módulo §7armazém/npcs §ffoi finalizado.");
        }

        Bukkit.getConsoleSender().sendMessage("§9[Nyex Spigot]: §aV §fo módulo §7armazém §ffoi finalizado.");
    }

    public void activeShopping(ProductsView view, Shopping shopping, Player player) {
        if (shopping.entregue || shopping.status() != TransactionStatus.PAID) {
            return;
        }

        if (!player.getName().equalsIgnoreCase(shopping.target())) {
            service.language.getAndSend(player, "storage.read-only");
            return;
        }

        try {
            service.executor.execute(new Shopping[]{shopping});

            service.language.getAndSend(player, "storage.activated", "<transacao:id>", shopping.id_transacao);
        } catch (NetworkErrorException | RequestFailedException | TokenFailureException ex) {
            shopping.delivered(false);

            service.language.getAndSend(player, "storage.api-error");
        }

        view.update();
    }

    public void createViewer(Player player, String name) {
        if (views.containsKey(name.toLowerCase())) {
            views.get(name.toLowerCase()).createPage(player, 1);
            return;
        }

        ProductsView view = new ProductsView(name, this);

        view.update();
        view.createPage(player, 1);

        views.put(name.toLowerCase(), view);
    }

    @Override
    public void execute(Shopping[] transactions) {
        for (Shopping shopping : transactions) {
            if (!views.containsKey(shopping.target().toLowerCase())) {
                views.put(shopping.target().toLowerCase(), new ProductsView(shopping.target(), this));
            }

            if (!products.containsKey(shopping.id_transacao)) {
                products.put(shopping.id_transacao, shopping);
                continue;
            }

            Shopping memory = products.get(shopping.id_transacao);

            if (shopping.equals(memory)) {
                continue;
            }

            for (Field field : Shopping.class.getFields()) {
                if (memory.entregue && field.getName().equals("entregue")) {
                    continue;
                }

                if (memory.donated && (field.getName().equals("donated") || field.getName().equals("recipient"))) {
                    continue;
                }

                try {
                    field.setAccessible(true);
                    field.set(memory, field.get(shopping));
                } catch (IllegalAccessException ignored) {
                }
            }

            products.put(shopping.id_transacao, memory);
        }

        for (ProductsView view : views.values()) {
            view.update();
        }
    }

    public PlaceholderAPI getPlaceholder(Shopping shopping) {
        PlaceholderAPI placeholder = new PlaceholderAPI();

        placeholder.register(new Placeholder(NyexPlugin.getInstance(), "transacao:id") {
            @Override
            public String getResult(OfflinePlayer player) {
                return Long.toString(shopping.id_transacao);
            }
        });

        placeholder.register(new Placeholder(NyexPlugin.getInstance(), "transacao:status") {
            @Override
            public String getResult(OfflinePlayer player) {
                return shopping.status().name();
            }
        });

        placeholder.register(new Placeholder(NyexPlugin.getInstance(), "transacao:price") {
            @Override
            public String getResult(OfflinePlayer player) {
                return shopping.valor;
            }
        });

        placeholder.register(new Placeholder(NyexPlugin.getInstance(), "presente:sender") {
            @Override
            public String getResult(OfflinePlayer player) {
                return shopping.identificador;
            }
        });

        return placeholder;
    }
}
