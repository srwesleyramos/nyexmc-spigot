package br.com.nyexgaming.mc.spigot.language;

import br.com.nyexgaming.mc.spigot.NyexPlugin;
import br.com.nyexgaming.mc.spigot.database.models.UserModel;
import br.com.nyexgaming.mc.spigot.service.Service;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.SQLException;

public class LanguageEvents implements Listener {

    private final Service service;

    public LanguageEvents(Service service) {
        this.service = service;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        UserModel user = service.database.getUserByName(e.getPlayer().getName());

        if (user != null) return;

        new BukkitRunnable() {
            @Override
            public void run() {
                String locale = e.getPlayer().spigot().getLocale().toLowerCase();

                if (service.language.getTranslation(locale) == null) {
                    locale = locale.split("_")[0];
                }

                try {
                    service.database.update(new UserModel(e.getPlayer().getName(), locale));
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }.runTaskLater(NyexPlugin.getInstance(), 2L);
    }
}
