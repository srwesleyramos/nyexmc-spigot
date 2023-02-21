package br.com.nyexgaming.mc.spigot.modules.storage.views;

import br.com.nyexgaming.mc.spigot.NyexPlugin;
import br.com.nyexgaming.mc.spigot.modules.storage.Storage;
import br.com.nyexgaming.sdk.endpoints.transactions.TransactionStatus;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import tk.wesleyramos.mclib.builder.ItemBuilder;
import tk.wesleyramos.mclib.view.Pagination;
import tk.wesleyramos.mclib.view.ViewHolder;
import tk.wesleyramos.mclib.view.ViewItem;

import java.time.Instant;

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
                    if (s.entregue || s.status > TransactionStatus.PAID.statusCode || s.identificador == null) {
                        return false;
                    }

                    if (filter == StorageFilter.PRODUCTS) {
                        return !s.donated && name.equalsIgnoreCase(s.identificador);
                    }

                    if (filter == StorageFilter.DONATIONS) {
                        return s.donated && name.equalsIgnoreCase(s.recipient);
                    }

                    return name.equalsIgnoreCase(s.target());
                })

                .sorted((s1, s2) -> {
                    if (order == StorageOrder.DATE) {
                        Long d1 = Instant.parse(s1.atualizado_em).toEpochMilli();
                        Long d2 = Instant.parse(s2.atualizado_em).toEpochMilli();

                        return d1.compareTo(d2);
                    }

                    Long p1 = Long.parseLong(s1.valor.replace(",", "."));
                    Long p2 = Long.parseLong(s2.valor.replace(",", "."));

                    return p2.compareTo(p1);
                })

                .map(shopping -> (shopping.donated ? parent.config.getGiftItem(parent.getPlaceholder(shopping)) : parent.config.getProductItem(parent.getPlaceholder(shopping))).setBoth((event) -> {
                    Player player = (Player) event.getWhoClicked();

                    if (!player.getName().equalsIgnoreCase(shopping.target())) {
                        player.sendMessage("§4Nyex Gaming ⇝ §cVocê só possui permissão para §nvisualizar§r§c, não é possível ativar este produto.");
                        return;
                    }

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
