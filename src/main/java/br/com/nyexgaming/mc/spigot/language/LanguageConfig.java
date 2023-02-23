package br.com.nyexgaming.mc.spigot.language;

import br.com.nyexgaming.mc.spigot.NyexPlugin;
import br.com.nyexgaming.mc.spigot.storage.Storage;
import br.com.nyexgaming.mc.spigot.storage.views.ProductsView;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import tk.wesleyramos.mclib.Config;
import tk.wesleyramos.mclib.Yaml;
import tk.wesleyramos.mclib.placeholder.PlaceholderAPI;
import tk.wesleyramos.mclib.view.ViewItem;

public class LanguageConfig {

    public final Storage parent;

    public final Config donations;
    public final Config storage;

    public LanguageConfig(Storage parent) {
        this.parent = parent;

        this.donations = new Config(NyexPlugin.getInstance(), "donations.yml").saveDefaultFile();
        this.storage = new Config(NyexPlugin.getInstance(), "storage.yml").saveDefaultFile();
    }

    public void reload() {
        donations.reload();
        storage.reload();
    }

    public String getProductsTitle() {
        return storage.getString("config.title").replace("&", "§");
    }

    public String getConfirmTitle() {
        return donations.getString("config.title").replace("&", "§");
    }

    public ViewItem getActiveItem() {
        return new ViewItem(11) {
            @Override
            public ItemStack getItem(Player player) {
                return Yaml.ITEM_STACK.getYamlFile(donations.getSection("items.active"), null);
            }
        };
    }

    public ViewItem getGiveItem() {
        return new ViewItem(15) {
            @Override
            public ItemStack getItem(Player player) {
                return Yaml.ITEM_STACK.getYamlFile(donations.getSection("items.give"), null);
            }
        };
    }

    public ViewItem getGiftItem(PlaceholderAPI placeholder) {
        return new ViewItem(-1) {
            @Override
            public ItemStack getItem(Player player) {
                return Yaml.ITEM_STACK.getYamlFile(storage.getSection("items.gift"), placeholder);
            }
        };
    }

    public ViewItem getProductItem(PlaceholderAPI placeholder) {
        return new ViewItem(-1) {
            @Override
            public ItemStack getItem(Player player) {
                return Yaml.ITEM_STACK.getYamlFile(storage.getSection("items.product"), placeholder);
            }
        };
    }

    public ViewItem getRefreshItem() {
        return new ViewItem(49) {
            @Override
            public ItemStack getItem(Player player) {
                return Yaml.ITEM_STACK.getYamlFile(storage.getSection("items.refresh"), null);
            }
        };
    }

    public ViewItem getOrderItem(ProductsView view) {
        return new ViewItem(48) {
            @Override
            public ItemStack getItem(Player player) {
                return Yaml.ITEM_STACK.getYamlFile(storage.getSection("items.order." + view.order.name().toLowerCase()), null);
            }
        };
    }

    public ViewItem getFilterItem(ProductsView view) {
        return new ViewItem(50) {
            @Override
            public ItemStack getItem(Player player) {
                return Yaml.ITEM_STACK.getYamlFile(storage.getSection("items.filter." + view.filter.name().toLowerCase()), null);
            }
        };
    }
}
