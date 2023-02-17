package br.com.nyexgaming.mc.spigot.modules.service;

import br.com.nyexgaming.sdk.endpoints.products.Product;
import br.com.nyexgaming.sdk.endpoints.products.ProductCommand;
import br.com.nyexgaming.sdk.endpoints.transactions.Transaction;
import br.com.nyexgaming.sdk.endpoints.transactions.TransactionStatus;
import br.com.nyexgaming.sdk.http.exceptions.NetworkErrorException;
import br.com.nyexgaming.sdk.http.exceptions.RequestFailedException;
import br.com.nyexgaming.sdk.http.exceptions.TokenFailureException;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ServiceExecutor {

    private final Service service;

    public ServiceExecutor(Service service) {
        this.service = service;
    }

    public void execute(Transaction[] transactions) throws NetworkErrorException, RequestFailedException, TokenFailureException {
        for (Transaction transaction : transactions) {
            Player player = Bukkit.getPlayer(transaction.identificador);

            if (player == null || transaction.status != TransactionStatus.PAID.statusCode) continue;

            transaction.entregue = true;

            this.service.getSdk().update(transaction);

            for (Product product : transaction.produtos) {
                for (int i = 0; i < product.quantidade; i++) {
                    for (ProductCommand command : product.comandos) {
                        Bukkit.dispatchCommand(
                                command.console ? Bukkit.getConsoleSender() : player,
                                format(product, command.cmd.replace("{{jogador}}", player.getName()))
                        );
                    }
                }
            }

            this.service.getBroadcast().execute(player, transaction);
        }
    }

    public String format(Product product, String text) {
        text = text.replace("{{nome}}", product.nome);
        text = text.replace("{{detalhes}}", product.detalhes);
        text = text.replace("{{preco}}", product.preco);

        return text.replace("&", "§");
    }
}
