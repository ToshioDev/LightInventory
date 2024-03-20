package me.nekocraft.hotbar;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import java.util.List;

public class HotbarItem {
    private final Plugin plugin;
    private ItemStack item;
    private ClickHandler handler;
    private boolean isListenerRegistered = false;

    public HotbarItem(Plugin plugin) {
        this.plugin = plugin;
    }

    public ItemStack getItem() {
        return item;
    }

    public ClickHandler getHandler() {
        return handler;
    }

    public void registerClickListener() {
        if (!isListenerRegistered) {
            plugin.getServer().getPluginManager().registerEvents(new ItemClickListener(this), plugin);
            isListenerRegistered = true;
        }
    }

    public void setCustomItem(ItemStack customItem) {
        this.item = customItem;
    }

    public void createCustomItem(Material material, String name, List<String> lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            meta.setLore(lore);
            item.setItemMeta(meta);
        }
        this.item = item;
    }

    public void setOnClick(ClickHandler handler) {
        this.handler = handler;
    }

    public interface ClickHandler {
        void onClick(Player player, ItemStack item, ClickType clickType);
    }

    public enum ClickType {
        LEFT_CLICK,
        RIGHT_CLICK,
        SHIFT_LEFT_CLICK,
        SHIFT_RIGHT_CLICK,
        MIDDLE_CLICK
    }

    public static class ItemClickListener implements Listener {
        private final HotbarItem hotbarItem;

        public ItemClickListener(HotbarItem hotbarItem) {
            this.hotbarItem = hotbarItem;
        }

        @EventHandler
        public void onInventoryClick(InventoryClickEvent event) {
            if (!(event.getWhoClicked() instanceof Player)) return;
            Player player = (Player) event.getWhoClicked();
            ItemStack currentItem = event.getCurrentItem();
            if (currentItem == null || currentItem.getType() == Material.AIR) return;

            if (!event.getView().getTitle().equals(player.getInventory().getTitle())) return;
            event.setCancelled(true);
            HotbarItem.ClickType clickType = HotbarItem.ClickType.LEFT_CLICK;
            if (event.isRightClick()) clickType = HotbarItem.ClickType.RIGHT_CLICK;
            if (event.isShiftClick()) {
                if (clickType == HotbarItem.ClickType.LEFT_CLICK) {
                    clickType = HotbarItem.ClickType.SHIFT_LEFT_CLICK;
                } else {
                    clickType = ClickType.SHIFT_RIGHT_CLICK;
                }
            }
            if (hotbarItem.handler != null) {
                hotbarItem.handler.onClick(player, currentItem, clickType);
            }
        }

        @EventHandler
        public void onPlayerInteract(PlayerInteractEvent event) {
            if (event.getItem() == null || event.getItem().getType() == Material.AIR) return;
            Player player = event.getPlayer();

            HotbarItem.ClickType clickType = event.getAction() == org.bukkit.event.block.Action.LEFT_CLICK_AIR ||
                    event.getAction() == org.bukkit.event.block.Action.LEFT_CLICK_BLOCK ?
                    HotbarItem.ClickType.LEFT_CLICK : HotbarItem.ClickType.RIGHT_CLICK;
            if (event.isCancelled()) {
                if (event.getAction() == org.bukkit.event.block.Action.RIGHT_CLICK_AIR ||
                        event.getAction() == org.bukkit.event.block.Action.RIGHT_CLICK_BLOCK) {
                    clickType = HotbarItem.ClickType.RIGHT_CLICK;
                }
            }
            if (hotbarItem.handler != null) {
                hotbarItem.handler.onClick(player, event.getItem(), clickType);
            }
        }
    }
}