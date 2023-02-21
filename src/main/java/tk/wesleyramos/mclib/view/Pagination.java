package tk.wesleyramos.mclib.view;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;
import java.util.Iterator;

public abstract class Pagination {

    public static int CURRENT_INDEX = 0;

    private final Plugin plugin;
    private final String title;
    private final int id, rows;
    private final int[] slots;
    private final View[] pages;
    private final ViewItem[] staticItems;

    private ViewItem previousButton;
    private ViewItem nextButton;

    public Pagination(Plugin plugin, String title, int rows, int[] slots) {
        this.id = CURRENT_INDEX++;

        this.plugin = plugin;
        this.title = title;
        this.rows = rows;
        this.slots = slots;

        this.pages = new View[100];
        this.staticItems = new ViewItem[rows * 9];
    }

    public Pagination createPage(Player player, int page) {
        if (pages[page - 1] == null) {
            return this;
        }

        pages[page - 1].create(player);
        return this;
    }

    public boolean hasPage(int page) {
        return pages[page - 1] != null;
    }

    public abstract ViewItem[] getItems();

    public Pagination update() {
        Iterator<ViewItem> items = Arrays.stream(getItems()).iterator();

        for (int id = 0; id < 100; id++) {
            if (id != 0 && !items.hasNext()) {
                if (pages[id] == null) {
                    break;
                }

                pages[id].getViewers().forEach(p -> createPage(p, 1));
                pages[id] = null;
                continue;
            }

            View view = pages[id] == null ? new View(id + 1, this) : pages[id];

            for (int slot : slots) {
                if (!items.hasNext()) {
                    view.remove(slot);
                    continue;
                }

                view.set(items.next().setSlot(slot));
            }

            for (ViewItem staticItem : staticItems) {
                if (staticItem != null) {
                    view.set(staticItem);
                }
            }

            if (previousButton != null) {
                if (id > 0) {
                    view.set(previousButton);
                } else {
                    view.remove(previousButton.getSlot());
                }
            }

            if (nextButton != null) {
                if (items.hasNext()) {
                    view.set(nextButton);
                } else {
                    view.remove(nextButton.getSlot());
                }
            }

            pages[id] = view.update();
        }

        return this;
    }

    public Pagination nextButton(int slot, ItemStack item) {
        this.nextButton = new ViewItem(slot) {
            @Override
            public ItemStack getItem(Player player) {
                return item;
            }
        }.setBoth(e -> createPage(
                (Player) e.getWhoClicked(),
                ((ViewHolder) e.getWhoClicked().getOpenInventory().getTopInventory().getHolder()).getPageId() + 1)
        );

        return this;
    }

    public Pagination previousButton(int slot, ItemStack item) {
        this.previousButton = new ViewItem(slot) {
            @Override
            public ItemStack getItem(Player player) {
                return item;
            }
        }.setBoth(e -> createPage(
                (Player) e.getWhoClicked(),
                ((ViewHolder) e.getWhoClicked().getOpenInventory().getTopInventory().getHolder()).getPageId() - 1)
        );

        return this;
    }

    public Pagination set(ViewItem item) {
        staticItems[item.getSlot()] = item;
        return this;
    }

    public ViewItem get(int slot) {
        return staticItems[slot];
    }

    public Pagination remove(int slot) {
        staticItems[slot] = null;
        return this;
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

    public int[] getSlots() {
        return slots;
    }

    public View[] getPages() {
        return pages;
    }

    public ViewItem[] getStaticItems() {
        return staticItems;
    }

    public ViewItem getPreviousButton() {
        return previousButton;
    }

    public ViewItem getNextButton() {
        return nextButton;
    }
}
