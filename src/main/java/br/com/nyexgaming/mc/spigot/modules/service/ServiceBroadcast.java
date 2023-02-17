package br.com.nyexgaming.mc.spigot.modules.service;

import br.com.nyexgaming.sdk.endpoints.transactions.Transaction;
import org.bukkit.entity.Player;

public class ServiceBroadcast {

    private final Service service;

    public ServiceBroadcast(Service service) {
        this.service = service;
    }

    public void execute(Player player, Transaction transaction) {
        if (!this.service.isBroadcastActivated()) return;

        // hum...
    }
}
