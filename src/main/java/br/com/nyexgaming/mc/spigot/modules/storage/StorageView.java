package br.com.nyexgaming.mc.spigot.modules.storage;

import br.com.nyexgaming.mc.spigot.NyexPlugin;
import br.com.nyexgaming.mc.spigot.modules.service.ServiceExecutor;
import br.com.nyexgaming.sdk.endpoints.transactions.Transaction;
import br.com.nyexgaming.sdk.endpoints.transactions.TransactionStatus;
import br.com.nyexgaming.sdk.http.exceptions.NetworkErrorException;
import br.com.nyexgaming.sdk.http.exceptions.RequestFailedException;
import br.com.nyexgaming.sdk.http.exceptions.TokenFailureException;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import tk.wesleyramos.mclib.Yaml;
import tk.wesleyramos.mclib.builder.ItemBuilder;
import tk.wesleyramos.mclib.placeholder.Placeholder;
import tk.wesleyramos.mclib.placeholder.PlaceholderAPI;
import tk.wesleyramos.mclib.view.Pagination;
import tk.wesleyramos.mclib.view.ViewItem;

import java.time.Instant;
import java.util.Arrays;

public class StorageView extends Pagination {

    private static final ServiceExecutor executor = new ServiceExecutor(NyexPlugin.getInstance().getService());

    private final String name;
    private final Storage storage;

    private StorageOrder order = StorageOrder.DATE;
    private StorageFilter filter = StorageFilter.ALL;

    public StorageView(String name, Storage storage) {
        super(NyexPlugin.getInstance(), storage.getConfig().getString("config.title").replace("&", "§"), 6, new int[]{
                10, 11, 12, 13, 14, 15, 16,
                19, 20, 21, 22, 23, 24, 25,
                28, 29, 30, 31, 32, 33, 34
        });

        this.name = name;
        this.storage = storage;

        this.set(new ViewItem(48) {
            @Override
            public ItemStack getItem(Player player) {
                return Yaml.ITEM_STACK.getYamlFile(
                        storage.getConfig().getSection("items.filter." + filter.name().toLowerCase()), null
                );
            }
        }.setBoth(e -> {
            this.filter = (filter != StorageFilter.ALL ? (filter == StorageFilter.PRODUCTS ? StorageFilter.DONATIONS : StorageFilter.ALL) : StorageFilter.PRODUCTS);

            this.update();
        }).setSound(Sound.LAVA_POP));

        this.set(new ViewItem(49) {
            @Override
            public ItemStack getItem(Player player) {
                return Yaml.ITEM_STACK.getYamlFile(
                        storage.getConfig().getSection("items.refresh"), null
                );
            }
        }.setBoth((e) -> this.update()).setSound(Sound.LAVA_POP));

        this.set(new ViewItem(50) {
            @Override
            public ItemStack getItem(Player player) {
                return Yaml.ITEM_STACK.getYamlFile(
                        storage.getConfig().getSection("items.order." + order.name().toLowerCase()), null
                );
            }
        }.setBoth(e -> {
            this.order = (order == StorageOrder.PRINCE ? StorageOrder.DATE : StorageOrder.PRINCE);

            this.update();
        }).setSound(Sound.LAVA_POP));

        this.previousButton(18, new ItemBuilder(Material.ARROW).setDisplayName(""));
        this.nextButton(26, new ItemBuilder(Material.ARROW).setDisplayName(""));
    }

    @Override
    public ViewItem[] getItems() {
        return Arrays.stream(this.storage.getTransactions())
                .filter(t -> {
                    if (t.entregue || t.status > TransactionStatus.PAID.statusCode) {
                        return false;
                    }

                    return !t.identificador.equals(this.name) && this.filter != StorageFilter.DONATIONS;
                })

                .sorted((t1, t2) -> {
                    if (this.order == StorageOrder.DATE) {
                        Long d1 = Instant.parse(t1.atualizado_em).toEpochMilli();
                        Long d2 = Instant.parse(t2.atualizado_em).toEpochMilli();

                        return d1.compareTo(d2);
                    }

                    Long p1 = Long.parseLong(t1.valor.replace(",", "."));
                    Long p2 = Long.parseLong(t2.valor.replace(",", "."));

                    return p2.compareTo(p1);
                })

                .map(t -> new ViewItem(-1) {
                    @Override
                    public ItemStack getItem(Player player) {
                        return Yaml.ITEM_STACK.getYamlFile(
                                storage.getConfig().getSection("items.product"), getPlaceholder(t)
                        );
                    }
                }.setBoth((e) -> {
                    if (t.entregue || t.status != TransactionStatus.PAID.statusCode) return;

                    try {
                        executor.execute(new Transaction[]{t});
                    } catch (NetworkErrorException | RequestFailedException | TokenFailureException ex) {
                        t.entregue = false;

                        e.getWhoClicked().sendMessage("§4Nyex Gaming ⇝ §cOps... Não é possível ativar o seu produto agora, tente novamente em alguns minutos.");
                    }

                    this.update();
                })).toArray(ViewItem[]::new);
    }

    public PlaceholderAPI getPlaceholder(Transaction t) {
        PlaceholderAPI placeholder = new PlaceholderAPI();

        placeholder.register(new Placeholder(NyexPlugin.getInstance(), "transacao:id") {
            @Override
            public String getResult(OfflinePlayer player) {
                return Long.toString(t.id_transacao);
            }
        });

        placeholder.register(new Placeholder(NyexPlugin.getInstance(), "transacao:status") {
            @Override
            public String getResult(OfflinePlayer player) {
                return storage.getConfig().getString("messages.state." + TransactionStatus.valueOf(t.status).name());
            }
        });

        placeholder.register(new Placeholder(NyexPlugin.getInstance(), "transacao:prince") {
            @Override
            public String getResult(OfflinePlayer player) {
                return t.valor;
            }
        });

        return placeholder;
    }

    public enum StorageFilter {
        ALL, PRODUCTS, DONATIONS
    }

    public enum StorageOrder {
        DATE, PRINCE
    }
}
