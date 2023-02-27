package br.com.nyexgaming.mc.spigot.database;

import br.com.nyexgaming.mc.spigot.database.adapter.CitizenAdapter;
import br.com.nyexgaming.mc.spigot.database.adapter.DeliveryAdapter;
import br.com.nyexgaming.mc.spigot.database.adapter.UserAdapter;
import br.com.nyexgaming.mc.spigot.database.models.CitizenModel;
import br.com.nyexgaming.mc.spigot.database.models.DeliveryModel;
import br.com.nyexgaming.mc.spigot.database.models.UserModel;
import br.com.nyexgaming.mc.spigot.service.Service;
import org.bukkit.Bukkit;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class Database {

    private final Map<Long, CitizenModel> citizens = new HashMap<>();
    private final Map<Long, DeliveryModel> deliveries = new HashMap<>();
    private final Map<String, UserModel> users = new HashMap<>();
    private final Service service;
    private final DatabaseTask task;
    private Connection connection;

    public Database(Service service) {
        this.service = service;

        this.task = new DatabaseTask(this);
        this.task.start();
    }

    public void connect() throws SQLException, ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");

        connection = DriverManager.getConnection(
                service.config.getString("database.jdbcUrl"),
                service.config.getString("database.user"),
                service.config.getString("database.password")
        );

        connection.prepareStatement(
                "CREATE TABLE IF NOT EXISTS `nyex_citizens` (`id` INTEGER PRIMARY KEY AUTOINCREMENT,`createdAt` INTEGER,`location` TEXT);"
        ).execute();

        connection.prepareStatement(
                "CREATE TABLE IF NOT EXISTS `nyex_deliveries` (`id_transacao` INTEGER PRIMARY KEY,`id_cupom` INTEGER,`id_loja` INTEGER,`status` INTEGER,`entregue` INTEGER,`identificador` TEXT,`email` TEXT,`gateway` TEXT,`valor` DOUBLE,`hex_transacao` TEXT,`external_reference` TEXT,`produtos` TEXT,`criado_em` INTEGER,`atualizado_em` INTEGER,`presente` BOOLEAN,`destinatario` TEXT);"
        ).execute();

        connection.prepareStatement(
                "CREATE TABLE IF NOT EXISTS `nyex_users` (`name` VARCHAR(16) PRIMARY KEY,`language` TEXT);"
        ).execute();

        loadAll();

        Bukkit.getConsoleSender().sendMessage("§9[Nyex Spigot]: §bOK! §7The §bdatabase module §7started correctly.");
    }

    public void disconnect() throws SQLException {
        saveAll();

        citizens.clear();
        deliveries.clear();
        users.clear();

        connection.close();
        connection = null;

        Bukkit.getConsoleSender().sendMessage("§9[Nyex Spigot]: §bOK! §7The §bdatabase module §7has been terminated.");
    }

    public void loadAll() throws SQLException {
        loadCitizens();
        loadDeliveries();
        loadUsers();
    }

    public void loadCitizens() throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM `nyex_citizens`;");
        ResultSet result = statement.executeQuery();

        while (result.next()) {
            CitizenModel citizen = CitizenAdapter.read(result);
            citizens.put(citizen.getId(), citizen);
        }
    }

    public void loadDeliveries() throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM `nyex_deliveries`;");
        ResultSet result = statement.executeQuery();

        while (result.next()) {
            DeliveryModel delivery = DeliveryAdapter.read(result);
            deliveries.put(delivery.getId_transacao(), delivery);
        }
    }

    public void loadUsers() throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM `nyex_users`;");
        ResultSet result = statement.executeQuery();

        while (result.next()) {
            UserModel user = UserAdapter.read(result);
            users.put(user.getName().toLowerCase(), user);
        }
    }

    public void saveAll() throws SQLException {
        saveCitizens();
        saveDeliveries();
        saveUsers();
    }

    public void saveCitizens() throws SQLException {
        for (CitizenModel citizen : citizens.values()) {
            update(citizen);
        }
    }

    public void saveDeliveries() throws SQLException {
        for (DeliveryModel delivery : deliveries.values()) {
            update(delivery);
        }
    }

    public void saveUsers() throws SQLException {
        for (UserModel user : users.values()) {
            update(user);
        }
    }

    public void update(CitizenModel citizen) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("INSERT OR REPLACE INTO `nyex_citizens` VALUES (?,?,?)");

        CitizenAdapter.write(statement, citizen);

        statement.execute();

        citizens.putIfAbsent(citizen.getId(), citizen);
    }

    public void update(DeliveryModel delivery) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("INSERT OR REPLACE INTO `nyex_deliveries` VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

        DeliveryAdapter.write(statement, delivery);

        statement.execute();

        deliveries.putIfAbsent(delivery.getId_transacao(), delivery);
    }

    public void update(UserModel user) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("INSERT OR REPLACE INTO `nyex_users` VALUES (?,?)");

        UserAdapter.write(statement, user);

        statement.execute();

        users.putIfAbsent(user.getName().toLowerCase(), user);
    }

    public CitizenModel getCitizenById(long id) {
        return citizens.get(id);
    }

    public DeliveryModel getDeliveryById(long id) {
        return deliveries.get(id);
    }

    public UserModel getUserByName(String name) {
        return users.get(name.toLowerCase());
    }

    public Connection getConnection() {
        return connection;
    }

    public Service getService() {
        return service;
    }

    public DatabaseTask getTask() {
        return task;
    }
}
