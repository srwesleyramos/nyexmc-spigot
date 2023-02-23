package br.com.nyexgaming.mc.spigot.database.adapter;

import br.com.nyexgaming.mc.spigot.database.models.UserModel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserAdapter {

    public static UserModel read(ResultSet result) throws SQLException {
        return new UserModel(
                result.getString("name"),
                result.getString("language")
        );
    }

    public static PreparedStatement write(PreparedStatement statement, UserModel language) throws SQLException {
        statement.setString(1, language.getName());
        statement.setString(2, language.getLanguage());

        return statement;
    }
}
