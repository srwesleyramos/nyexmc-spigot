package br.com.nyexgaming.mc.spigot.service;

import br.com.nyexgaming.mc.spigot.database.models.Shopping;
import br.com.nyexgaming.sdk.endpoints.products.Product;
import br.com.nyexgaming.sdk.endpoints.products.ProductCommand;
import br.com.nyexgaming.sdk.endpoints.transactions.TransactionStatus;
import br.com.nyexgaming.sdk.http.exceptions.NetworkErrorException;
import br.com.nyexgaming.sdk.http.exceptions.RequestFailedException;
import br.com.nyexgaming.sdk.http.exceptions.TokenFailureException;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ServiceExecutor {

    public final Service service;

    public ServiceExecutor(Service service) {
        this.service = service;
    }

    public void execute(Shopping[] transactions) throws NetworkErrorException, RequestFailedException, TokenFailureException {
        for (Shopping shopping : transactions) {
            Player player = Bukkit.getPlayer(shopping.target());

            if (player == null || shopping.entregue || shopping.status() != TransactionStatus.PAID) {
                continue;
            }

            service.sdk.update(shopping.delivered(true));

            for (Product product : shopping.produtos) {
                for (int i = 0; i < product.quantidade; i++) {
                    for (ProductCommand command : product.comandos) {
                        Bukkit.dispatchCommand(
                                command.console ? Bukkit.getConsoleSender() : player,
                                command.cmd.replace("<jogador>", player.getName())
                        );
                    }
                }
            }

            service.broadcast.execute(player, shopping);
        }
    }
}
