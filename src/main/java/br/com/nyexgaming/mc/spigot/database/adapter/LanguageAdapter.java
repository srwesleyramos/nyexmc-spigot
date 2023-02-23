package br.com.nyexgaming.mc.spigot.database.adapter;

import br.com.nyexgaming.mc.spigot.database.models.LanguageModel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LanguageAdapter {

    public static LanguageModel read(ResultSet result) throws SQLException {
        return new LanguageModel(
                result.getString("name"),
                result.getString("language")
        );
    }

    public static PreparedStatement write(PreparedStatement statement, LanguageModel language) throws SQLException {
        statement.setString(1, language.getName());
        statement.setString(2, language.getLanguage());

        return statement;
    }
}
