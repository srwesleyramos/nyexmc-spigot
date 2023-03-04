package br.com.nyexgaming.mc.spigot.service;

import br.com.nyexgaming.mc.spigot.NyexPlugin;
import br.com.nyexgaming.mc.spigot.database.models.Shopping;
import br.com.nyexgaming.sdk.endpoints.products.Product;
import br.com.nyexgaming.sdk.endpoints.products.ProductCommand;
import br.com.nyexgaming.sdk.endpoints.transactions.TransactionStatus;
import br.com.nyexgaming.sdk.http.exceptions.NetworkErrorException;
import br.com.nyexgaming.sdk.http.exceptions.RequestFailedException;
import br.com.nyexgaming.sdk.http.exceptions.TokenFailureException;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class ServiceExecutor {

    public final Service service;

    public ServiceExecutor(Service service) {
        this.service = service;
    }

    public void execute(Shopping[] transactions) throws NetworkErrorException, RequestFailedException, TokenFailureException {
        List<String> chargebackCommands = service.config.getStringList("chargeback-commands");

        for (Shopping shopping : transactions) {
            Player player = Bukkit.getPlayer(shopping.target());

            if (player == null || shopping.entregue == 2) continue;

            if (shopping.status() == TransactionStatus.REVERSED) {
                if (shopping.entregue == 1) {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            for (String command : chargebackCommands) {
                                Bukkit.dispatchCommand(
                                        Bukkit.getConsoleSender(),
                                        command.replace("<jogador>", player.getName())
                                );
                            }
                        }
                    }.runTaskLater(NyexPlugin.getInstance(), 0L);
                }

                service.sdk.update(shopping.delivered(2));
                continue;
            }

            if (shopping.entregue == 1 || shopping.status != 1) continue;

            service.sdk.update(shopping.delivered(1));

            new BukkitRunnable() {
                @Override
                public void run() {
                    for (Product product : shopping.produtos) {
                        for (int i = 0; i < product.quantidade; i++) {
                            for (ProductCommand command : product.comandos) {
                                Bukkit.dispatchCommand(
                                        command.console ? Bukkit.getConsoleSender() : player,
                                        command.comando.replace("<jogador>", player.getName())
                                );
                            }
                        }
                    }
                }
            }.runTaskLater(NyexPlugin.getInstance(), 0L);

            service.broadcast.execute(player, shopping);
        }
    }
}
