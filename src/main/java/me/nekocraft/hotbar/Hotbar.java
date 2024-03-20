package me.nekocraft.hotbar;

import org.bukkit.entity.Player;
import java.util.HashMap;
import java.util.Map;

public class Hotbar {
    private final Map<Integer, HotbarItem> items;

    public Hotbar() {
        items = new HashMap<>();
    }

    public void addItem(int slot, HotbarItem hotbarItem) {
        if (slot >= 0 && slot < 9) {
            this.items.put(slot, hotbarItem);
        } else {
            System.out.println("El slot " + slot + " está fuera del rango de la hotbar.");
        }
    }

    public void setHotbar(Player player) {
        player.getInventory().clear();
        this.items.forEach((slot, hotbarItem) -> {
            player.getInventory().setItem(slot, hotbarItem.getItem());
            hotbarItem.registerClickListener();
        });
    }
}