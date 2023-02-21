package br.com.nyexgaming.mc.spigot.modules.storage.views;

import br.com.nyexgaming.mc.spigot.NyexPlugin;
import br.com.nyexgaming.mc.spigot.database.models.Shopping;
import br.com.nyexgaming.mc.spigot.modules.storage.Storage;
import br.com.nyexgaming.mc.spigot.modules.storage.events.DonationEvents;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import tk.wesleyramos.mclib.view.View;

public class ConfirmView extends View {

    public final Storage storage;
    public final Shopping shopping;
    public final ProductsView parent;

    private String confirmation;

    public ConfirmView(Storage storage, ProductsView parent, Shopping shopping, int pageId) {
        super(pageId, NyexPlugin.getInstance(), storage.config.getConfirmTitle(), 3);

        this.storage = storage;
        this.parent = parent;
        this.shopping = shopping;

        set(storage.config.getActiveItem().setBoth((event) -> {
            Player player = (Player) event.getWhoClicked();

            storage.activeShopping(parent, shopping, player);

            player.closeInventory();
        }));

        set(storage.config.getGiveItem().setBoth((event) -> {
            DonationEvents.LISTENING.put(event.getWhoClicked().getName(), this);

            event.getWhoClicked().sendMessage("");
            event.getWhoClicked().sendMessage("  §9NyexGaming - Minhas compras");
            event.getWhoClicked().sendMessage("  §7Digite §fo nome do jogador §7que você deseja fazer a doação.");
            event.getWhoClicked().sendMessage("  §7Digite §fcancelar §7para cancelar a doação.");
            event.getWhoClicked().sendMessage("");

            event.getWhoClicked().closeInventory();
        }));
    }

    public boolean chat(AsyncPlayerChatEvent e) {
        if (e.getMessage().equalsIgnoreCase("cancelar")) {
            return true;
        }

        Player player = e.getPlayer();
        String message = e.getMessage();

        if (confirmation == null) {
            if (player.getName().equalsIgnoreCase(message) || Bukkit.getPlayer(message) == null) {
                player.sendMessage("§4Nyex Gaming ⇝ §cO jogador está offline, ele precisa estar online para receber o presente.");
                return false;
            }

            confirmation = Bukkit.getPlayer(message).getName();

            player.sendMessage("");
            player.sendMessage("  §9NyexGaming - Minhas compras");
            player.sendMessage("  §7Escreva §fconfirmar §7para continuar.");
            player.sendMessage("  §7Escreva §fcancelar §7para cancelar a doação.");
            player.sendMessage("");

            return false;
        }

        if (message.equalsIgnoreCase("confirmar")) {
            Player target = Bukkit.getPlayer(confirmation);

            if (target == null) {
                player.sendMessage("§4Nyex Gaming ⇝ §cO jogador ficou offline, ele precisa estar online para receber o presente. A operação foi cancelada.");
                return true;
            }

            storage.service.database.insertOrUpdate(shopping.donated(true, confirmation));
            storage.views.get(confirmation.toLowerCase()).update();

            player.sendMessage("§9Nyex Gaming ⇝ §fO presente foi enviado ao jogador, avise-o para recarregar suas entregas.");
            target.sendMessage("§9Nyex Gaming ⇝ §fVocê recebeu um novo presente, verifique seu armazém.");
        }

        return false;
    }
}
