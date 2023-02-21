package br.com.nyexgaming.mc.spigot.modules.broadcast;

import br.com.nyexgaming.mc.spigot.database.models.Shopping;
import br.com.nyexgaming.mc.spigot.service.Service;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Broadcast {

    public final Service service;

    public Broadcast(Service service) {
        this.service = service;
    }

    public void reload() {
        if (service.isBroadcastActivated()) {
            Bukkit.getConsoleSender().sendMessage("§9[Nyex Spigot]: §5X §fo módulo §7anúncios §fnão foi ativo pois está em desenvolvimento.");
        } else {
            Bukkit.getConsoleSender().sendMessage("§9[Nyex Spigot]: §4X §co módulo §4anúncios §cnão foi ativo pois está desativado.");
        }
    }

    public void unload() {
    }

    public void execute(Player player, Shopping shopping) {
        if (!this.service.isBroadcastActivated()) return;

        // hum...
    }
}
