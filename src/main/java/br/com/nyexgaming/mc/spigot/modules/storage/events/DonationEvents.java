package br.com.nyexgaming.mc.spigot.modules.storage.events;

import br.com.nyexgaming.mc.spigot.NyexPlugin;
import br.com.nyexgaming.mc.spigot.modules.storage.Storage;
import br.com.nyexgaming.mc.spigot.modules.storage.views.ConfirmView;
import br.com.nyexgaming.mc.spigot.modules.storage.views.ProductsView;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;
import tk.wesleyramos.mclib.view.ViewHolder;

import java.util.HashMap;
import java.util.Map;

public class DonationEvents implements Listener {

    public static Map<String, ConfirmView> LISTENING = new HashMap<>();

    private final Storage storage;

    public DonationEvents(Storage storage) {
        this.storage = storage;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        if (!storage.views.containsKey(e.getPlayer().getName().toLowerCase())) {
            storage.views.put(
                    e.getPlayer().getName().toLowerCase(),
                    new ProductsView(e.getPlayer().getName(), storage)
            );
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        if (!storage.service.isDonationsActivated() || !LISTENING.containsKey(e.getPlayer().getName())) {
            return;
        }

        e.setCancelled(true);

        if (LISTENING.get(e.getPlayer().getName()).chat(e)) {
            ConfirmView view = LISTENING.remove(e.getPlayer().getName());

            view.parent.update().createPage(e.getPlayer(), view.getId());
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        if (!(e.getInventory().getHolder() instanceof ViewHolder)) {
            return;
        }

        ViewHolder holder = (ViewHolder) e.getInventory().getHolder();

        if (!(holder.getView() instanceof ConfirmView)) {
            return;
        }

        if (LISTENING.containsKey(e.getPlayer().getName())) {
            return;
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                ((ConfirmView) holder.getView()).parent.createPage((Player) e.getPlayer(), holder.getPageId());
            }
        }.runTaskLater(NyexPlugin.getInstance(), 1L);
    }
}
