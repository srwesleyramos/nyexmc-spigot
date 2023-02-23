package br.com.nyexgaming.mc.spigot.database.adapter;

import br.com.nyexgaming.mc.spigot.database.models.CitizenModel;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.StringJoiner;
import java.util.UUID;

public class CitizenAdapter {

    public static CitizenModel read(ResultSet result) throws SQLException {
        String[] data = result.getString("location").split("; ");

        return new CitizenModel(
                result.getLong("id"),
                result.getLong("createdAt"),

                new Location(
                        Bukkit.getWorld(UUID.fromString(data[0])),
                        Double.parseDouble(data[1]),
                        Double.parseDouble(data[2]),
                        Double.parseDouble(data[3]),
                        Float.parseFloat(data[4]),
                        Float.parseFloat(data[5])
                ));
    }

    public static PreparedStatement write(PreparedStatement statement, CitizenModel citizen) throws SQLException {
        StringJoiner joiner = new StringJoiner("; ")
                .add(citizen.getLocation().getWorld().getUID().toString())
                .add(Double.toString(citizen.getLocation().getX()))
                .add(Double.toString(citizen.getLocation().getY()))
                .add(Double.toString(citizen.getLocation().getZ()))
                .add(Float.toString(citizen.getLocation().getYaw()))
                .add(Float.toString(citizen.getLocation().getPitch()));

        statement.setLong(1, citizen.getId());
        statement.setLong(2, citizen.getCreatedAt());
        statement.setString(3, joiner.toString());

        return statement;
    }
}
