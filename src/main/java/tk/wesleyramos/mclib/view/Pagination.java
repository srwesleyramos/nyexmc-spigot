package tk.wesleyramos.mclib.view;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

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
        boolean reaming = true;
        ViewItem[] items = getItems();

        for (int pageId = 0; pageId < 100; pageId++) {
            if (!reaming) {
                if (pages[pageId] == null) {
                    break;
                }

                pages[pageId].getViewers().forEach(p -> createPage(p, 1));
                pages[pageId] = null;
                continue;
            }

            View view = pages[pageId] == null ? new View(pageId + 1, this) : pages[pageId];

            for (int i = 0; i < slots.length; i++) {
                int index = pageId * slots.length + i;

                if (index >= items.length) {
                    reaming = false;
                    view.remove(slots[i]);
                    continue;
                }

                ViewItem item = items[index];
                item.setSlot(slots[i]);
                view.set(item);
            }

            for (ViewItem staticItem : staticItems) {
                if (staticItem != null) {
                    view.set(staticItem);
                }
            }

            if (previousButton != null && pageId > 0) {
                view.set(previousButton);
            }

            if (nextButton != null && reaming) {
                view.set(nextButton);
            }

            pages[pageId] = view.update();
        }
        return this;
    }

    public Pagination nextButton(int slot, ItemStack item) {
        this.nextButton = new ViewItem(slot) {
            @Override
            public ItemStack getItem(Player player) {
                return item;
            }
        };
        this.nextButton.setLeft(e -> createPage((Player) e.getWhoClicked(), ((ViewHolder) e.getWhoClicked().getOpenInventory().getTopInventory().getHolder()).getPageId() + 1));
        this.nextButton.setRight(e -> createPage((Player) e.getWhoClicked(), ((ViewHolder) e.getWhoClicked().getOpenInventory().getTopInventory().getHolder()).getPageId() + 1));
        return this;
    }

    public Pagination previousButton(int slot, ItemStack item) {
        this.previousButton = new ViewItem(slot) {
            @Override
            public ItemStack getItem(Player player) {
                return item;
            }
        };
        this.previousButton.setLeft(e -> createPage((Player) e.getWhoClicked(), ((ViewHolder) e.getWhoClicked().getOpenInventory().getTopInventory().getHolder()).getPageId() - 1));
        this.previousButton.setRight(e -> createPage((Player) e.getWhoClicked(), ((ViewHolder) e.getWhoClicked().getOpenInventory().getTopInventory().getHolder()).getPageId() - 1));
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
