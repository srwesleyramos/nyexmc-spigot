package tk.wesleyramos.mclib.view;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.List;
import java.util.stream.Collectors;

public class View {

    private final Plugin plugin;
    private final String title;
    private final int id, rows;
    private final ViewItem[] items;
    private final Pagination pagination;

    public View(Plugin plugin, String title, int rows) {
        this(-1, plugin, title, rows);
    }

    public View(int id, Plugin plugin, String title, int rows) {
        this.id = id;
        this.pagination = null;
        this.plugin = plugin;
        this.title = title;
        this.rows = rows;
        this.items = new ViewItem[rows * 9];
    }

    public View(int id, Pagination pagination) {
        this.id = id;
        this.pagination = pagination;
        this.plugin = pagination.getPlugin();
        this.title = pagination.getTitle();
        this.rows = pagination.getRows();
        this.items = new ViewItem[rows * 9];
    }

    public View create(Player player) {
        player.openInventory(Bukkit.createInventory(new ViewHolder(this), rows * 9, title));
        return this.update(player);
    }

    public View update() {
        getViewers().forEach(this::update);
        return this;
    }

    public View update(Player player) {
        Inventory inventory = player.getOpenInventory().getTopInventory();

        if (inventory == null || !inventory.getTitle().equals(title) || (inventory.getSize() / 9) != rows) {
            this.create(player);
            return this;
        }

        for (int i = 0; i < rows * 9; i++) {
            ItemStack item = items[i] != null ? items[i].getItem(player) : null;

            if (item != null && !item.equals(inventory.getItem(i))) {
                inventory.setItem(i, item);
            } else if (item == null && inventory.getItem(i) != null) {
                inventory.setItem(i, null);
            }
        }

        return this;
    }

    public View set(ViewItem item) {
        items[item.getSlot()] = item;
        return this;
    }

    public ViewItem get(int slot) {
        return items[slot];
    }

    public View remove(int slot) {
        items[slot] = null;
        return this;
    }

    public List<Player> getViewers() {
        return Bukkit.getOnlinePlayers().stream().filter(p ->
                p.getOpenInventory() != null &&
                        p.getOpenInventory().getTopInventory() != null &&
                        p.getOpenInventory().getTopInventory().getHolder() instanceof ViewHolder &&
                        ((ViewHolder) p.getOpenInventory().getTopInventory().getHolder()).getView().equals(this)
        ).collect(Collectors.toList());
    }

    public Plugin getPlugin() {
        return plugin;
    }

    public String getTitle() {
        return title;
    }

    public int getId() {
        return id;
    }

    public int getRows() {
        return rows;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public ViewItem[] getItems() {
        return items;
    }
}
