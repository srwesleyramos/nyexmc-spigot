package tk.wesleyramos.mclib.builder;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import tk.wesleyramos.mclib.placeholder.PlaceholderAPI;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

public class ItemBuilder extends ItemStack {

    public static final Map<EntityType, String> ENTITY_HEADS = new HashMap<>();

    static {
        ENTITY_HEADS.put(EntityType.BAT, "9e99deef919db66ac2bd28d6302756ccd57c7f8b12b9dca8f41c3e0a04ac1cc");
        ENTITY_HEADS.put(EntityType.BLAZE, "b78ef2e4cf2c41a2d14bfde9caff10219f5b1bf5b35a49eb51c6467882cb5f0");
        ENTITY_HEADS.put(EntityType.CAVE_SPIDER, "41645dfd77d09923107b3496e94eeb5c30329f97efc96ed76e226e98224");
        ENTITY_HEADS.put(EntityType.CHICKEN, "1638469a599ceef7207537603248a9ab11ff591fd378bea4735b346a7fae893");
        ENTITY_HEADS.put(EntityType.COW, "5d6c6eda942f7f5f71c3161c7306f4aed307d82895f9d2b07ab4525718edc5");
        ENTITY_HEADS.put(EntityType.CREEPER, "f4254838c33ea227ffca223dddaabfe0b0215f70da649e944477f44370ca6952");
        ENTITY_HEADS.put(EntityType.ENDERMAN, "96c0b36d53fff69a49c7d6f3932f2b0fe948e032226d5e8045ec58408a36e951");
        ENTITY_HEADS.put(EntityType.GHAST, "7a8b714d32d7f6cf8b37e221b758b9c599ff76667c7cd45bbc49c5ef19858646");
        ENTITY_HEADS.put(EntityType.GIANT, "56fc854bb84cf4b7697297973e02b79bc10698460b51a639c60e5e417734e11");
        ENTITY_HEADS.put(EntityType.GUARDIAN, "932c24524c82ab3b3e57c2052c533f13dd8c0beb8bdd06369bb2554da86c123");
        ENTITY_HEADS.put(EntityType.HORSE, "7bb4b288991efb8ca0743beccef31258b31d39f24951efb1c9c18a417ba48f9");
        ENTITY_HEADS.put(EntityType.IRON_GOLEM, "89091d79ea0f59ef7ef94d7bba6e5f17f2f7d4572c44f90f76c4819a714");
        ENTITY_HEADS.put(EntityType.MAGMA_CUBE, "38957d5023c937c4c41aa2412d43410bda23cf79a9f6ab36b76fef2d7c429");
        ENTITY_HEADS.put(EntityType.MUSHROOM_COW, "d0bc61b9757a7b83e03cd2507a2157913c2cf016e7c096a4d6cf1fe1b8db");
        ENTITY_HEADS.put(EntityType.OCELOT, "5657cd5c2989ff97570fec4ddcdc6926a68a3393250c1be1f0b114a1db1");
        ENTITY_HEADS.put(EntityType.PIG, "621668ef7cb79dd9c22ce3d1f3f4cb6e2559893b6df4a469514e667c16aa4");
        ENTITY_HEADS.put(EntityType.PIG_ZOMBIE, "74e9c6e98582ffd8ff8feb3322cd1849c43fb16b158abb11ca7b42eda7743eb");
        ENTITY_HEADS.put(EntityType.RABBIT, "cec242e667aee44492413ef461b810cac356b74d8718e5cec1f892a6b43e5e1");
        ENTITY_HEADS.put(EntityType.SHEEP, "f31f9ccc6b3e32ecf13b8a11ac29cd33d18c95fc73db8a66c5d657ccb8be70");
        ENTITY_HEADS.put(EntityType.SILVERFISH, "92ec2c3cb95ab77f7a60fb4d160bced4b879329b62663d7a9860642e588ab210");
        ENTITY_HEADS.put(EntityType.SKELETON, "301268e9c492da1f0d88271cb492a4b302395f515a7bbf77f4a20b95fc02eb2");
        ENTITY_HEADS.put(EntityType.SLIME, "a20e84d32d1e9c919d3fdbb53f2b37ba274c121c57b2810e5a472f40dacf004f");
        ENTITY_HEADS.put(EntityType.SPIDER, "cd541541daaff50896cd258bdbdd4cf80c3ba816735726078bfe393927e57f1");
        ENTITY_HEADS.put(EntityType.SQUID, "d8705624daa2956aa45956c81bab5f4fdb2c74a596051e24192039aea3a8b8");
        ENTITY_HEADS.put(EntityType.VILLAGER, "41b830eb4082acec836bc835e40a11282bb51193315f91184337e8d3555583");
        ENTITY_HEADS.put(EntityType.WITCH, "20e13d18474fc94ed55aeb7069566e4687d773dac16f4c3f8722fc95bf9f2dfa");
        ENTITY_HEADS.put(EntityType.WITHER, "ee280cefe946911ea90e87ded1b3e18330c63a23af5129dfcfe9a8e166588041");
        ENTITY_HEADS.put(EntityType.WOLF, "69d1d3113ec43ac2961dd59f28175fb4718873c6c448dfca8722317d67");
        ENTITY_HEADS.put(EntityType.ZOMBIE, "56fc854bb84cf4b7697297973e02b79bc10698460b51a639c60e5e417734e11");
    }

    private PlaceholderAPI placeholder = PlaceholderAPI.SERVER;

    public ItemBuilder(ItemStack itemStack) {
        super(itemStack);
    }

    public ItemBuilder(Material material) {
        super(new ItemStack(material));
    }

    public ItemBuilder(EntityType entityType) {
        super(new ItemStack(Material.SKULL_ITEM, 1, (short) 3));
        this.setSkullTexture(ENTITY_HEADS.get(entityType));
    }

    public ItemBuilder(Material material, int amount) {
        super(new ItemStack(material, amount));
    }

    public ItemBuilder(Material material, short data) {
        super(new ItemStack(material, 1, data));
    }

    public ItemBuilder(Material material, int amount, int data) {
        super(new ItemStack(material, amount, (short) data));
    }

    public ItemBuilder(Material material, int amount, short data) {
        super(new ItemStack(material, amount, data));
    }

    public ItemBuilder setDisplayName(String displayName) {
        ItemMeta itemMeta = getItemMeta();
        itemMeta.setDisplayName(placeholder.replace(null, displayName));
        setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder setLore(String... lore) {
        Arrays.asList(lore).forEach(this::addLoreText);
        return this;
    }

    public ItemBuilder setLore(List<String> lore) {
        lore.forEach(this::addLoreText);
        return this;
    }

    public ItemBuilder addLoreText(String textLine) {
        ItemMeta itemMeta = getItemMeta();
        List<String> lore = itemMeta.getLore() != null ? itemMeta.getLore() : new ArrayList<>();
        lore.add(placeholder.replace(null, textLine));
        itemMeta.setLore(lore);
        setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder clearLore() {
        ItemMeta itemMeta = getItemMeta();
        itemMeta.setLore(new ArrayList<>());
        setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder addEnchant(Enchantment enchantment, int level) {
        addUnsafeEnchantment(enchantment, level);
        return this;
    }

    public ItemBuilder removeEnchant(Enchantment enchantment) {
        removeEnchantment(enchantment);
        return this;
    }

    public ItemBuilder addItemFlag(ItemFlag itemFlag) {
        ItemMeta meta = getItemMeta();
        meta.addItemFlags(itemFlag);
        setItemMeta(meta);
        return this;
    }

    public ItemBuilder removeItemFlag(ItemFlag itemFlag) {
        ItemMeta meta = getItemMeta();
        meta.removeItemFlags(itemFlag);
        setItemMeta(meta);
        return this;
    }

    public Color getLeatherColor() {
        LeatherArmorMeta leatherMeta = (LeatherArmorMeta) getItemMeta();
        return leatherMeta.getColor();
    }

    public ItemBuilder setLeatherColor(Color color) {
        LeatherArmorMeta leatherMeta = (LeatherArmorMeta) getItemMeta();
        leatherMeta.setColor(color);
        setItemMeta(leatherMeta);
        return this;
    }

    public ItemBuilder addCustomPotionEffect(PotionEffectType type, int duration, int amplifier) {
        PotionMeta potionMeta = (PotionMeta) getItemMeta();
        potionMeta.addCustomEffect(new PotionEffect(type, duration, amplifier), true);
        setItemMeta(potionMeta);
        return this;
    }

    public boolean hasCustomPotionEffect(PotionEffectType type) {
        PotionMeta potionMeta = (PotionMeta) getItemMeta();
        return potionMeta.hasCustomEffect(type);
    }

    public ItemBuilder removeCustomPotionEffect(PotionEffectType type) {
        PotionMeta potionMeta = (PotionMeta) getItemMeta();
        potionMeta.removeCustomEffect(type);
        setItemMeta(potionMeta);
        return this;
    }

    public ItemBuilder setSkullOwner(String playerName) {
        SkullMeta m = (SkullMeta) getItemMeta();
        m.setOwner(playerName);
        setItemMeta(m);
        return this;
    }

    public String getSkullTexture() {
        SkullMeta skullMeta = (SkullMeta) getItemMeta();

        try {
            Field profileField = skullMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);

            GameProfile gameProfile = (GameProfile) profileField.get(skullMeta);
            Optional<Property> property = gameProfile.getProperties().get("textures").stream().findFirst();

            if (property.isPresent()) {
                return property.get().getValue();
            }
        } catch (IllegalAccessException | NoSuchFieldException | NoSuchElementException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ItemBuilder setSkullTexture(String texture) {
        SkullMeta skullMeta = (SkullMeta) getItemMeta();

        if (testHttpRequest(texture)) {
            texture = Base64.getEncoder().encodeToString(String.format("{textures:{SKIN:{url:\"%s\"}}}", texture).getBytes());
        } else if (testHttpRequest("https://textures.minecraft.net/texture/" + texture)) {
            texture = Base64.getEncoder().encodeToString(String.format("{textures:{SKIN:{url:\"%s%s\"}}}", "https://textures.minecraft.net/texture/", texture).getBytes());
        }

        try {
            GameProfile gameProfile = new GameProfile(UUID.randomUUID(), null);
            gameProfile.getProperties().put("textures", new Property("textures", texture));

            Field profileField = skullMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(skullMeta, gameProfile);
            setItemMeta(skullMeta);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        return this;
    }

    protected boolean testHttpRequest(String url) {
        try {
            HttpURLConnection request = (HttpURLConnection) new URL(url).openConnection();
            return request.getResponseCode() == 200;
        } catch (IOException ignored) {
            return false;
        }
    }

    public <T> T getNBTTag(Function<NBTTagCompound, T> function) {
        net.minecraft.server.v1_8_R3.ItemStack itemStack = CraftItemStack.asNMSCopy(this);
        NBTTagCompound itemCompound = itemStack.hasTag() ? itemStack.getTag() : new NBTTagCompound();
        return function.apply(itemCompound);
    }

    public Set<String> getNBTTags() {
        net.minecraft.server.v1_8_R3.ItemStack itemStack = CraftItemStack.asNMSCopy(this);
        NBTTagCompound itemCompound = itemStack.hasTag() ? itemStack.getTag() : new NBTTagCompound();
        return itemCompound.c();
    }

    public boolean hasNBTTag(String tag) {
        net.minecraft.server.v1_8_R3.ItemStack itemStack = CraftItemStack.asNMSCopy(this);
        NBTTagCompound itemCompound = itemStack.hasTag() ? itemStack.getTag() : new NBTTagCompound();
        return itemCompound.hasKey(tag);
    }

    public ItemBuilder setNBTTag(Consumer<NBTTagCompound> consumer) {
        net.minecraft.server.v1_8_R3.ItemStack itemStack = CraftItemStack.asNMSCopy(this);
        NBTTagCompound itemCompound = itemStack.hasTag() ? itemStack.getTag() : new NBTTagCompound();
        consumer.accept(itemCompound);
        itemStack.setTag(itemCompound);
        setItemMeta(CraftItemStack.getItemMeta(itemStack));
        return this;
    }

    public PlaceholderAPI getPlaceholder() {
        return placeholder;
    }

    public ItemBuilder setPlaceholder(PlaceholderAPI placeholder) {
        this.placeholder = placeholder;
        return this;
    }

    @Override
    public ItemStack clone() {
        return new ItemBuilder(this);
    }
}
