package fr.fuzeblocks.homeplugin.gui.warp;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.AbstractItem;

public class WarpListItem extends AbstractItem  {


    private String warpName;
    public WarpListItem(String warpName) {
        this.warpName = warpName;
    }

    @Override
    public ItemProvider getItemProvider() {
        return new ItemBuilder(Material.RED_BED).setDisplayName(warpName);
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent inventoryClickEvent) {
        WarpGUIManager.openEditWarpGUI(player, ChatColor.stripColor(warpName));
    }
}
