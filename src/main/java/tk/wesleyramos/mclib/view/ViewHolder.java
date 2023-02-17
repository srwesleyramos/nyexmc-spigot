package tk.wesleyramos.mclib.view;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class ViewHolder implements InventoryHolder {

    private final View view;

    public ViewHolder(View view) {
        this.view = view;
    }

    @Override
    public Inventory getInventory() {
        return null;
    }

    public int getPageId() {
        return view.getId();
    }

    public int getPaginationId() {
        return view.getPagination() != null ? view.getPagination().getId() : -1;
    }

    public View getView() {
        return view;
    }
}
