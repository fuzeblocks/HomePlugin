package fr.fuzeblocks.homeplugin.gui.modern.warp.item;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.core.language.LanguageManager;
import fr.fuzeblocks.homeplugin.core.warps.WarpData;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.invui.Click;
import xyz.xenondevs.invui.item.AbstractItem;
import xyz.xenondevs.invui.item.ItemBuilder;
import xyz.xenondevs.invui.item.ItemProvider;

public class WarpListItem extends AbstractItem {


    private final String WARP_LIST = "Warp.List.";
    private final LanguageManager languageManager = HomePlugin.getLanguageManager();
    private final String displayName;
    private final String warpName;

    public WarpListItem(String warpName, String displayName) {
        this.warpName = warpName;
        this.displayName = displayName;
    }

    @Override
    public ItemProvider getItemProvider(Player player) {
        WarpData warpData = HomePlugin.getWarpManager().getWarp(warpName);
        Location location = warpData.getLocation();
        int x = location.getBlockX();
        int y = location.getBlockY();
        int z = location.getBlockZ();
        String world = location.getWorld() != null ? location.getWorld().getName() : "Unknown";
        return new ItemBuilder(Material.RED_BED).setName(displayName).addLoreLines(languageManager.getStringWithColor(WARP_LIST + "Warp-location", "&9Position : &6X: %x% Y: %y% Z: %z% Monde: %world%").replace("%x%", String.valueOf(x)).replace("%y%", String.valueOf(y)).replace("%z%", String.valueOf(z)).replace("%world%", world), languageManager.getStringWithColor(WARP_LIST + "Warp-teleport-hover", "&eCliquez pour vous téléporter."));

    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull Click click) {
        player.performCommand("warp " + warpName);
    }
}
