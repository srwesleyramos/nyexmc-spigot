package tk.wesleyramos.mclib.view;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;

public class ViewEvents implements Listener {

    private final Plugin plugin;

    public ViewEvents(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getClickedInventory() == null) {
            return;
        }

        Inventory inventory = e.getClickedInventory();

        if (inventory.getHolder() instanceof ViewHolder) {
            View view = ((ViewHolder) inventory.getHolder()).getView();
            ViewItem item = view.get(e.getSlot());

            e.setCancelled(true);

            if (item == null || !view.getPlugin().equals(plugin)) {
                return;
            }

            if (item.getSound() != null && e.getWhoClicked() instanceof Player) {
                ((Player) e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), item.getSound(), 2.5F, 2.5F);
            }

            ViewAction action = null;

            if (e.getClick() == ClickType.LEFT || e.getClick() == ClickType.SHIFT_LEFT) {
                action = item.getLeft();
            } else if (e.getClick() == ClickType.RIGHT || e.getClick() == ClickType.SHIFT_RIGHT) {
                action = item.getRight();
            }

            if (action != null) {
                action.click(e);
            }
        }
    }
}
