package br.com.nyexgaming.mc.spigot.service;

import br.com.nyexgaming.mc.spigot.database.models.DeliveryModel;
import br.com.nyexgaming.sdk.endpoints.products.ProductCommand;
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

    public void execute(DeliveryModel[] transactions) throws NetworkErrorException, RequestFailedException, TokenFailureException {
        for (DeliveryModel shopping : transactions) {
            Player player = Bukkit.getPlayer(shopping.isPresente() ? shopping.getDestinatario() : shopping.getIdentificador());

            if (player == null || shopping.getEntregue() == 1 || shopping.getStatus() != 1) {
                continue;
            }

            shopping.setEntregue(1);

            //service.sdk.update(shopping);

            for (br.com.nyexgaming.sdk.endpoints.products.Product product : shopping.getProdutos()) {
                for (int i = 0; i < product.quantidade; i++) {
                    for (ProductCommand command : product.comandos) {
                        Bukkit.dispatchCommand(
                                command.console ? Bukkit.getConsoleSender() : player,
                                command.cmd.replace("<jogador>", player.getName())
                        );
                    }
                }
            }

            // service.broadcast.execute(player, shopping);
        }
    }
}
