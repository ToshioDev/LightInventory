package me.nekocraft.hotbar;

import java.util.HashMap;
import java.util.Map;

public class HotbarManager {
    private final Map<InventoryType, Hotbar> hotbars;

    public HotbarManager() {
        this.hotbars = new HashMap<>();
    }

    public void setHotbar(Hotbar hotbar, InventoryType inventoryType) {
        hotbars.put(inventoryType, hotbar);
    }

    public Hotbar getHotbar(InventoryType inventoryType) {
        return hotbars.get(inventoryType);
    }

    public void clearHotbar(InventoryType inventoryType) {
        hotbars.remove(inventoryType);
    }

    public enum InventoryType {
        PREGAME_HOTBAR // Agrega más tipos de inventario según sea necesario
    }
}