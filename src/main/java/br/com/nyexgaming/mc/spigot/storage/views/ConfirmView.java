package br.com.nyexgaming.mc.spigot.storage.views;

import br.com.nyexgaming.mc.spigot.NyexPlugin;
import br.com.nyexgaming.mc.spigot.database.models.DeliveryModel;
import br.com.nyexgaming.mc.spigot.storage.Storage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import tk.wesleyramos.mclib.view.View;

public class ConfirmView extends View {

    public final Storage storage;
    public final DeliveryModel shopping;
    public final ProductsView parent;

    private String confirmation;

    public ConfirmView(Storage storage, ProductsView parent, DeliveryModel shopping, int pageId) {
        super(pageId, NyexPlugin.getInstance(), "" /*storage.config.getConfirmTitle()*/, 3);

        this.storage = storage;
        this.parent = parent;
        this.shopping = shopping;

        /*set(storage.config.getActiveItem().setBoth((event) -> {
            Player player = (Player) event.getWhoClicked();

            storage.activeShopping(parent, shopping, player);

            player.closeInventory();
        }));

        set(storage.config.getGiveItem().setBoth((event) -> {
            DonationEvents.LISTENING.put(event.getWhoClicked().getName(), this);

            storage.service.language.send(event.getWhoClicked(), "storage.donation-welcome");

            event.getWhoClicked().closeInventory();
        }));*/
    }

    public boolean chat(AsyncPlayerChatEvent e) {
        if (e.getMessage().equalsIgnoreCase("cancelar")) {
            return true;
        }

        Player player = e.getPlayer();
        String message = e.getMessage();

        if (confirmation == null) {
            if (player.getName().equalsIgnoreCase(message) || Bukkit.getPlayer(message) == null) {
                //storage.service.language.send(player, "storage.donation-not-found");
                return false;
            }

            confirmation = Bukkit.getPlayer(message).getName();

            //storage.service.language.send(player, "storage.donation-confirm");
            return false;
        }

        if (message.equalsIgnoreCase("confirmar")) {
            Player target = Bukkit.getPlayer(confirmation);

            if (target == null) {
//                storage.service.language.send(player, "storage.donation-not-found");
                return true;
            }

            shopping.setPresente(true);
            shopping.setDestinatario(confirmation);

            // storage.service.database.insertOrUpdate(shopping);
            storage.views.get(confirmation.toLowerCase()).update();

            //          storage.service.language.send(player, "storage.donation-sent");
            //        storage.service.language.send(target, "storage.donation-received");
        }

        return false;
    }
}
