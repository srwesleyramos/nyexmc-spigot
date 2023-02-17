package tk.wesleyramos.mclib.view;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class ViewItem {

    private int slot;
    private Sound sound;
    private ViewAction left, right;

    public ViewItem(int slot) {
        this.slot = slot;
    }

    public ViewItem(int slot, Sound sound) {
        this.slot = slot;
        this.sound = sound;
    }

    public abstract ItemStack getItem(Player player);

    public int getSlot() {
        return slot;
    }

    public ViewItem setSlot(int slot) {
        this.slot = slot;
        return this;
    }

    public Sound getSound() {
        return sound;
    }

    public ViewItem setSound(Sound sound) {
        this.sound = sound;
        return this;
    }

    public ViewItem setBoth(ViewAction action) {
        this.left = action;
        this.right = action;
        return this;
    }

    public ViewAction getLeft() {
        return left;
    }

    public ViewItem setLeft(ViewAction left) {
        this.left = left;
        return this;
    }

    public ViewAction getRight() {
        return right;
    }

    public ViewItem setRight(ViewAction right) {
        this.right = right;
        return this;
    }
}
