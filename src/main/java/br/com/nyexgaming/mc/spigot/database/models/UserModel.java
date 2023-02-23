package br.com.nyexgaming.mc.spigot.database.models;

public class UserModel {

    private final String name;
    private String language;

    public UserModel(String name, String language) {
        this.name = name;
        this.language = language;
    }

    public String getName() {
        return name;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
