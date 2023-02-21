package br.com.nyexgaming.mc.spigot.database;

import br.com.nyexgaming.mc.spigot.database.models.Shopping;
import br.com.nyexgaming.mc.spigot.service.Service;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import tk.wesleyramos.mclib.Serializer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Database {

    public final Service service;

    public Connection connection;

    public Database(Service service) {
        this.service = service;
    }

    public void reload() throws SQLException, ClassNotFoundException {
        try {
            if (this.connection != null && !this.connection.isClosed()) {
                this.connection.close();
            }

            Class.forName("org.sqlite.JDBC");

            this.connection = DriverManager.getConnection(
                    this.service.config.getString("database.jdbcUrl"),
                    this.service.config.getString("database.user"),
                    this.service.config.getString("database.password")
            );

            this.connection.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS `nyex_citizens` (`id` INTEGER PRIMARY KEY AUTOINCREMENT, `location` TEXT NOT NULL);"
            ).execute();

            this.connection.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS `nyex_shopping` (`id` INTEGER PRIMARY KEY AUTOINCREMENT, `donated` BOOLEAN, `recipient` VARCHAR(16));"
            ).execute();

            Bukkit.getConsoleSender().sendMessage("§9[Nyex Spigot]: §aV §fo módulo §7banco de dados §finiciou corretamente.");
        } catch (ClassNotFoundException | SQLException exception) {
            Bukkit.getConsoleSender().sendMessage("§9[Nyex Spigot]: §4X §co módulo §4banco de dados §capresentou erros ao iniciar.");
            throw exception;
        }
    }

    public void unload() {
        try {
            this.connection.close();
            this.connection = null;
        } catch (SQLException ignored) {
        }

        Bukkit.getConsoleSender().sendMessage("§9[Nyex Spigot]: §aV §fo módulo §7banco de dados §ffoi finalizado.");
    }

    public void insertOrDelete(int id, Location location) {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO `nyex_citizens` VALUES (?, ?);");
            statement.setInt(1, id);
            statement.setString(2, Serializer.LOCATION.serialize(location));
            statement.execute();
        } catch (SQLException ignored) {
            try {
                PreparedStatement statement = connection.prepareStatement("DELETE FROM `nyex_citizens` WHERE `id` = ?;");
                statement.setInt(1, id);
                statement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void insertOrUpdate(Shopping shopping) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO `nyex_shopping` VALUES (?, ?, ?);"
            );

            statement.setLong(1, shopping.id_transacao);
            statement.setBoolean(2, shopping.donated);
            statement.setString(3, shopping.recipient);
            statement.execute();
        } catch (SQLException ignored) {
            try {
                PreparedStatement statement = connection.prepareStatement(
                        "UPDATE `nyex_shopping` SET `donated` = ?, `recipient` = ? WHERE `id` = ?;"
                );
                statement.setBoolean(1, shopping.donated);
                statement.setString(2, shopping.recipient);
                statement.setLong(3, shopping.id_transacao);
                statement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
