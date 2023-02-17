package tk.wesleyramos.mclib;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;

public class Config {

    private String name;
    private File file;
    private YamlConfiguration config;
    private Plugin plugin;

    public Config(Plugin plugin, String fileName) {
        this.name = fileName;
        this.plugin = plugin;

        this.reload();
    }

    public Config reload() {
        this.file = new File(this.plugin.getDataFolder(), getName());
        this.config = YamlConfiguration.loadConfiguration(getFile());

        return this;
    }

    public Config saveDefaultFile() {
        try {
            if (!this.file.exists()) {
                this.file.getParentFile().mkdirs();

                Files.copy(plugin.getResource(this.name), this.file.toPath());
            }

            reload();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }

    public Config save() {
        try {
            config.save(getFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }

    public Config set(String path, Object value) {
        config.set(path, value);
        return this;
    }

    public boolean contains(String path) {
        return config.contains(path);
    }

    public Object get(String path) {
        return config.get(path);
    }

    public String getString(String path) {
        return config.getString(path);
    }

    public int getInt(String path) {
        return config.getInt(path);
    }

    public long getLong(String path) {
        return config.getLong(path);
    }

    public double getDouble(String path) {
        return config.getDouble(path);
    }

    public byte getByte(String path) {
        return (byte) config.get(path);
    }

    public boolean getBoolean(String path) {
        return config.getBoolean(path);
    }

    public List<?> getList(String path) {
        return config.getList(path);
    }

    public List<Map<?, ?>> getMapList(String path) {
        return config.getMapList(path);
    }

    public List<String> getStringList(String path) {
        return config.getStringList(path);
    }

    public ItemStack getItem(String path) {
        return config.getItemStack(path);
    }

    public ConfigurationSection getSection(String path) {
        return config.getConfigurationSection(path);
    }

    public String getName() {
        return name;
    }

    public Plugin getPlugin() {
        return plugin;
    }

    public File getFile() {
        return file;
    }

    public YamlConfiguration getYaml() {
        return config;
    }
}
