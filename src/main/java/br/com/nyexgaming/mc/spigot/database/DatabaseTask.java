package br.com.nyexgaming.mc.spigot.database;

import java.sql.SQLException;

public class DatabaseTask extends Thread {

    private final Database database;

    public DatabaseTask(Database database) {
        this.database = database;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(10000L);
            } catch (InterruptedException ignored) {
            }

            try {
                database.saveAll();
            } catch (SQLException ignored) {
            }
        }
    }
}
