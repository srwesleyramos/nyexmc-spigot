package tk.wesleyramos.mclib;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class Serializer {

    public static final String CHAR_SEPARATOR = "; ";

    public static final Serializable<ItemStack> ITEM_STACK = new Serializable<ItemStack>() {
        @Override
        public String serialize(ItemStack data) {
            return new Gson().toJson(data.serialize());
        }

        @Override
        public ItemStack deserialize(String data) {
            return ItemStack.deserialize(new Gson().fromJson(data, new TypeToken<Map<String, Object>>() {
            }.getType()));
        }
    };

    public static final Serializable<List<String>> LIST = new Serializable<List<String>>() {
        @Override
        public String serialize(List<String> data) {
            if (data == null || data.isEmpty()) return "";

            StringJoiner joiner = new StringJoiner(CHAR_SEPARATOR);
            data.forEach(joiner::add);

            return joiner.toString();
        }

        @Override
        public List<String> deserialize(String data) {
            if (data == null || data.equals("")) return new ArrayList<>();

            return Arrays.asList(data.split(CHAR_SEPARATOR));
        }
    };

    public static final Serializable<Location> LOCATION = new Serializable<Location>() {
        @Override
        public String serialize(Location data) {
            if (data == null || data.getWorld() == null) return "";

            StringJoiner joiner = new StringJoiner(CHAR_SEPARATOR);
            joiner.add(data.getWorld().getUID().toString());
            joiner.add(Double.toString(data.getX()));
            joiner.add(Double.toString(data.getY()));
            joiner.add(Double.toString(data.getZ()));
            joiner.add(Float.toString(data.getYaw()));
            joiner.add(Float.toString(data.getPitch()));

            return joiner.toString();
        }

        @Override
        public Location deserialize(String data) {
            if (data == null || data.equals("")) return null;

            String[] s = data.split(CHAR_SEPARATOR);

            if (Bukkit.getWorld(UUID.fromString(s[0])) != null) {
                return new Location(
                        Bukkit.getWorld(UUID.fromString(s[0])),
                        Double.parseDouble(s[1]),
                        Double.parseDouble(s[2]),
                        Double.parseDouble(s[3]),
                        Float.parseFloat(s[4]),
                        Float.parseFloat(s[5])
                );
            }

            return null;
        }
    };

    public interface Serializable<T> {

        String serialize(T data);

        T deserialize(String data);
    }
}
