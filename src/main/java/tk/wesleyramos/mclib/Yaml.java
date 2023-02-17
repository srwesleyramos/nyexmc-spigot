package tk.wesleyramos.mclib;

import net.minecraft.server.v1_8_R3.NBTTagByteArray;
import net.minecraft.server.v1_8_R3.NBTTagIntArray;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffectType;
import tk.wesleyramos.mclib.builder.ItemBuilder;
import tk.wesleyramos.mclib.placeholder.PlaceholderAPI;

import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public class Yaml {

    public static final String CHAR_SEPARATOR = "; ";
    public static final YamlConvertible<ItemStack> ITEM_STACK = new YamlConvertible<ItemStack>() {

        public final List<String> ITEM_IGNORED_NBT_TAGS = Arrays.asList("CustomPotionEffects", "display", "ench", "HideFlags", "SkullOwner");

        @Override
        public void setYamlFile(ConfigurationSection section, ItemStack object) {
            ItemBuilder builder = new ItemBuilder(object);

            section.set("type", builder.getType().name());
            section.set("data", builder.getDurability());
            section.set("amount", builder.getAmount());

            section.set("meta.display-name", builder.getItemMeta().getDisplayName());
            section.set("meta.lore", builder.getItemMeta().getLore());

            if (!builder.getItemMeta().getEnchants().isEmpty()) {
                section.set("meta.enchants", builder.getItemMeta().getEnchants().entrySet().stream()
                        .map(entry -> entry.getKey().getName() + CHAR_SEPARATOR + entry.getValue()).collect(Collectors.toList()));
            }

            if (!builder.getItemMeta().getItemFlags().isEmpty()) {
                section.set("meta.flags", builder.getItemMeta().getItemFlags().stream().map(ItemFlag::toString).collect(Collectors.toList()));
            }

            if (builder.getItemMeta() instanceof LeatherArmorMeta) {
                section.set("leather.color", ((LeatherArmorMeta) builder.getItemMeta()).getColor().asRGB());
            }

            if (builder.getItemMeta() instanceof PotionMeta) {
                section.set("potion.custom-effects", ((PotionMeta) builder.getItemMeta()).getCustomEffects().stream()
                        .map(effect -> effect.getType().getName() + CHAR_SEPARATOR + effect.getDuration() + CHAR_SEPARATOR + effect.getAmplifier())
                        .collect(Collectors.toList()));
            }

            if (builder.getItemMeta() instanceof SkullMeta) {
                section.set("skull.player", ((SkullMeta) builder.getItemMeta()).getOwner());
                section.set("skull.texture", builder.getSkullTexture());
            }

            builder.getNBTTags().stream().filter(tag -> !ITEM_IGNORED_NBT_TAGS.contains(tag)).forEach(tag -> {
                Object handle = builder.getNBTTag(component -> component.get(tag));

                switch (handle.getClass().getSimpleName()) {
                    case "NBTTagString":
                    case "NBTTagByte":
                    case "NBTTagShort":
                    case "NBTTagInt":
                    case "NBTTagLong":
                    case "NBTTagFloat":
                    case "NBTTagDouble":
                        section.set("nbt-tags." + tag + ".type", handle.getClass().getSimpleName());
                        section.set("nbt-tags." + tag + ".data", handle.toString());
                        break;
                    case "NBTTagByteArray":
                        StringJoiner joiner1 = new StringJoiner(CHAR_SEPARATOR);
                        NBTTagByteArray byteArray = (NBTTagByteArray) handle;

                        for (int i = 0; i < byteArray.c().length; i++) {
                            joiner1.add(((Byte) byteArray.c()[i]).toString());
                        }

                        section.set("nbt-tags." + tag + ".type", handle.getClass().getSimpleName());
                        section.set("nbt-tags." + tag + ".data", joiner1.toString());
                        break;
                    case "NBTTagIntArray":
                        StringJoiner joiner2 = new StringJoiner(CHAR_SEPARATOR);
                        NBTTagIntArray intArray = (NBTTagIntArray) handle;

                        for (int i = 0; i < intArray.c().length; i++) {
                            joiner2.add(((Integer) intArray.c()[i]).toString());
                        }

                        section.set("nbt-tags." + tag + ".type", handle.getClass().getSimpleName());
                        section.set("nbt-tags." + tag + ".data", joiner2.toString());
                        break;
                }
            });
        }

        @Override
        public ItemStack getYamlFile(ConfigurationSection section, PlaceholderAPI placeholder) {
            ItemBuilder builder = new ItemBuilder(Material.valueOf(section.getString("type").toUpperCase()), (short) section.getInt("data"));

            if (placeholder != null) {
                builder.setPlaceholder(placeholder);
            }

            if (section.contains("amount")) {
                builder.setAmount(section.getInt("amount"));
            }

            if (section.contains("meta.display-name")) {
                builder.setDisplayName(section.getString("meta.display-name"));
            }

            if (section.contains("meta.lore")) {
                builder.setLore(section.getStringList("meta.lore"));
            }

            if (section.contains("meta.enchants")) {
                section.getStringList("meta.enchants").forEach(enchantment -> {
                    String[] data = enchantment.split(CHAR_SEPARATOR);
                    builder.addEnchant(Enchantment.getByName(data[0].toUpperCase()), Integer.parseInt(data[1]));
                });
            }

            if (section.contains("meta.flags")) {
                section.getStringList("meta.flags").forEach(flag -> builder.addItemFlag(ItemFlag.valueOf(flag.toUpperCase())));
            }

            if (section.contains("leather.color")) {
                builder.setLeatherColor(Color.fromRGB(section.getInt("leather.color")));
            }

            if (section.contains("potion.custom-effects")) {
                section.getStringList("potion.custom-effects").forEach(effect -> {
                    String[] data = effect.split(CHAR_SEPARATOR);
                    builder.addCustomPotionEffect(PotionEffectType.getByName(data[0].toUpperCase()), Integer.parseInt(data[1]), Integer.parseInt(data[2]));
                });
            }

            if (section.contains("skull.player")) {
                builder.setSkullOwner(section.getString("skull.player"));
            }

            if (section.contains("skull.texture")) {
                builder.setSkullTexture(section.getString("skull.texture"));
            }

            if (section.contains("nbt-tags")) {
                for (String tag : section.getConfigurationSection("nbt-tags").getKeys(false)) {
                    String type = section.getString("nbt-tags." + tag + ".type");
                    String data = section.getString("nbt-tags." + tag + ".data");

                    switch (type) {
                        case "NBTTagString":
                            builder.setNBTTag(consumer -> consumer.setString(tag, data));
                            break;
                        case "NBTTagByte":
                            builder.setNBTTag(consumer -> consumer.setByte(tag, Byte.parseByte(data.endsWith("b") ? data.substring(0, data.length() - 1) : data)));
                            break;
                        case "NBTTagShort":
                            builder.setNBTTag(consumer -> consumer.setShort(tag, Short.parseShort(data.endsWith("s") ? data.substring(0, data.length() - 1) : data)));
                            break;
                        case "NBTTagInt":
                            builder.setNBTTag(consumer -> consumer.setInt(tag, Integer.parseInt(data)));
                            break;
                        case "NBTTagLong":
                            builder.setNBTTag(consumer -> consumer.setLong(tag, Long.parseLong(data.endsWith("L") ? data.substring(0, data.length() - 1) : data)));
                            break;
                        case "NBTTagFloat":
                            builder.setNBTTag(consumer -> consumer.setFloat(tag, Float.parseFloat(data.endsWith("f") ? data.substring(0, data.length() - 1) : data)));
                            break;
                        case "NBTTagDouble":
                            builder.setNBTTag(consumer -> consumer.setDouble(tag, Double.parseDouble(data.endsWith("d") ? data.substring(0, data.length() - 1) : data)));
                            break;
                        case "NBTTagByteArray":
                            String[] split1 = data.split(CHAR_SEPARATOR);
                            byte[] array1 = new byte[split1.length];

                            for (int i = 0; i < split1.length; i++) {
                                array1[i] = Byte.parseByte(split1[i]);
                            }

                            builder.setNBTTag(consumer -> consumer.setByteArray(tag, array1));
                            break;
                        case "NBTTagIntArray":
                            String[] split2 = data.split(CHAR_SEPARATOR);
                            int[] array2 = new int[split2.length];

                            for (int i = 0; i < split2.length; i++) {
                                array2[i] = Integer.parseInt(split2[i]);
                            }

                            builder.setNBTTag(consumer -> consumer.setIntArray(tag, array2));
                            break;
                    }
                }
            }

            return builder;
        }
    };

    public interface YamlConvertible<T> {

        void setYamlFile(ConfigurationSection section, T object);

        T getYamlFile(ConfigurationSection section, PlaceholderAPI placeholder);
    }
}
