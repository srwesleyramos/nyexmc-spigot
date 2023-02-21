package br.com.nyexgaming.mc.spigot.modules.storage;

import br.com.nyexgaming.mc.spigot.NyexPlugin;
import br.com.nyexgaming.mc.spigot.modules.storage.views.ProductsView;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import tk.wesleyramos.mclib.Config;
import tk.wesleyramos.mclib.Yaml;
import tk.wesleyramos.mclib.placeholder.PlaceholderAPI;
import tk.wesleyramos.mclib.view.ViewItem;

public class StorageConfig {

    public final Storage parent;

    public final Config donations;
    public final Config storage;

    public StorageConfig(Storage parent) {
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
        return new ViewItem(11, Sound.LAVA_POP) {
            @Override
            public ItemStack getItem(Player player) {
                return Yaml.ITEM_STACK.getYamlFile(donations.getSection("items.active"), null);
            }
        };
    }

    public ViewItem getGiveItem() {
        return new ViewItem(15, Sound.LAVA_POP) {
            @Override
            public ItemStack getItem(Player player) {
                return Yaml.ITEM_STACK.getYamlFile(donations.getSection("items.give"), null);
            }
        };
    }

    public ViewItem getGiftItem(PlaceholderAPI placeholder) {
        return new ViewItem(-1, Sound.LAVA_POP) {
            @Override
            public ItemStack getItem(Player player) {
                return Yaml.ITEM_STACK.getYamlFile(storage.getSection("items.gift"), placeholder);
            }
        };
    }

    public ViewItem getProductItem(PlaceholderAPI placeholder) {
        return new ViewItem(-1, Sound.LAVA_POP) {
            @Override
            public ItemStack getItem(Player player) {
                return Yaml.ITEM_STACK.getYamlFile(storage.getSection("items.product"), placeholder);
            }
        };
    }

    public ViewItem getRefreshItem() {
        return new ViewItem(49, Sound.LAVA_POP) {
            @Override
            public ItemStack getItem(Player player) {
                return Yaml.ITEM_STACK.getYamlFile(storage.getSection("items.refresh"), null);
            }
        };
    }

    public ViewItem getOrderItem(ProductsView view) {
        return new ViewItem(48, Sound.LAVA_POP) {
            @Override
            public ItemStack getItem(Player player) {
                return Yaml.ITEM_STACK.getYamlFile(storage.getSection("items.order." + view.order.name().toLowerCase()), null);
            }
        };
    }

    public ViewItem getFilterItem(ProductsView view) {
        return new ViewItem(50, Sound.LAVA_POP) {
            @Override
            public ItemStack getItem(Player player) {
                return Yaml.ITEM_STACK.getYamlFile(storage.getSection("items.filter." + view.filter.name().toLowerCase()), null);
            }
        };
    }
}
