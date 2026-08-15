package fr.fuzeblocks.homeplugin.gui.legacy.warp.item;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.core.language.LanguageManager;
import fr.fuzeblocks.homeplugin.core.warps.WarpData;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.AbstractItem;

public class WarpListItem extends AbstractItem {


    private final String WARP_LIST = "Warp.List.";
    private final LanguageManager languageManager = HomePlugin.getLanguageManager();
    private final String displayName;
    private String warpName;

    public WarpListItem(String warpName, String displayName) {
        this.warpName = warpName;
        this.displayName = displayName;
    }

    @Override
    public ItemProvider getItemProvider() {
        WarpData warpData = HomePlugin.getWarpManager().getWarp(warpName);
        Location location = warpData.getLocation();
        int x = location.getBlockX();
        int y = location.getBlockY();
        int z = location.getBlockZ();
        String world = location.getWorld() != null ? location.getWorld().getName() : "Unknown";
        ItemBuilder itemBuilder = null;
        if (warpData.getIcon() != null) {
            itemBuilder = new ItemBuilder(warpData.getIcon());
        } else {
            itemBuilder = new ItemBuilder(Material.RED_BED);
        }
        return itemBuilder.setDisplayName(displayName).addLoreLines(languageManager.getStringWithColor(WARP_LIST + "Warp-location", "&9Position : &6X: %x% Y: %y% Z: %z% Monde: %world%").replace("%x%", String.valueOf(x)).replace("%y%", String.valueOf(y)).replace("%z%", String.valueOf(z)).replace("%world%", world), languageManager.getStringWithColor(WARP_LIST + "Warp-teleport-hover", "&eCliquez pour vous téléporter."));


    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent inventoryClickEvent) {
        player.performCommand("warp " + warpName);
    }
}
