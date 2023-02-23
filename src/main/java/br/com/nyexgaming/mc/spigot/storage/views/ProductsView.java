package br.com.nyexgaming.mc.spigot.storage.views;

import br.com.nyexgaming.mc.spigot.NyexPlugin;
import br.com.nyexgaming.mc.spigot.storage.Storage;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import tk.wesleyramos.mclib.builder.ItemBuilder;
import tk.wesleyramos.mclib.view.Pagination;
import tk.wesleyramos.mclib.view.ViewHolder;
import tk.wesleyramos.mclib.view.ViewItem;

public class ProductsView extends Pagination {

    public final String name;
    public final Storage parent;

    public StorageOrder order = StorageOrder.DATE;
    public StorageFilter filter = StorageFilter.ALL;

    public ProductsView(String name, Storage parent) {
        super(NyexPlugin.getInstance(), parent.config.getProductsTitle(), 6, new int[]{
                10, 11, 12, 13, 14, 15, 16,
                19, 20, 21, 22, 23, 24, 25,
                28, 29, 30, 31, 32, 33, 34
        });

        this.name = name;
        this.parent = parent;

        set(parent.config.getOrderItem(this).setBoth(e -> {
            order = (order == StorageOrder.PRICE ? StorageOrder.DATE : StorageOrder.PRICE);

            update();
        }));

        set(parent.config.getRefreshItem().setBoth((e) -> update()));

        set(parent.config.getFilterItem(this).setBoth(e -> {
            filter = (filter != StorageFilter.ALL ? (filter == StorageFilter.PRODUCTS ? StorageFilter.DONATIONS : StorageFilter.ALL) : StorageFilter.PRODUCTS);

            update();
        }));

        previousButton(18, new ItemBuilder(Material.ARROW).setDisplayName(""));

        nextButton(26, new ItemBuilder(Material.ARROW).setDisplayName(""));
    }

    @Override
    public ViewItem[] getItems() {
        return parent.products.values().stream()
                .filter(s -> {
                    if (s.getEntregue() == 1 || s.getStatus() > 1 || s.getIdentificador() == null) {
                        return false;
                    }

                    if (filter == StorageFilter.PRODUCTS) {
                        return !s.isPresente() && name.equalsIgnoreCase(s.getIdentificador());
                    }

                    if (filter == StorageFilter.DONATIONS) {
                        return s.isPresente() && name.equalsIgnoreCase(s.getDestinatario());
                    }

                    return name.equalsIgnoreCase(s.isPresente() ? s.getDestinatario() : s.getIdentificador());
                })

                .sorted((s1, s2) -> {
                    if (order == StorageOrder.DATE) {
                        Long d1 = s1.getAtualizado_em();
                        Long d2 = s2.getAtualizado_em();

                        return d1.compareTo(d2);
                    }

                    Long p1 = (long) s1.getValor();
                    Long p2 = (long) s2.getValor();

                    return p2.compareTo(p1);
                })

                .map(shopping -> (shopping.isPresente() ? parent.config.getGiftItem(parent.getPlaceholder(shopping)) : parent.config.getProductItem(parent.getPlaceholder(shopping))).setBoth((event) -> {
                    Player player = (Player) event.getWhoClicked();

                    /*if (!player.getName().equalsIgnoreCase(shopping.target())) {
                        parent.service.language.getAndSend(player, "storage.read-only");
                        return;
                    }*/

                    if (parent.service.isDonationsActivated()) {
                        new ConfirmView(
                                parent,
                                this,
                                shopping,
                                ((ViewHolder) player.getOpenInventory().getTopInventory().getHolder()).getPageId()
                        ).create(player);
                        return;
                    }

                    parent.activeShopping(this, shopping, player);
                })).toArray(ViewItem[]::new);
    }

    public enum StorageFilter {
        ALL, PRODUCTS, DONATIONS
    }

    public enum StorageOrder {
        DATE, PRICE
    }
}
