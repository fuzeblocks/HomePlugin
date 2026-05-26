package fr.fuzeblocks.homeplugin.gui.modern.warp.item;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.core.warps.WarpData;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.invui.Click;import xyz.xenondevs.invui.item.AbstractItem;import xyz.xenondevs.invui.item.ItemBuilder;import xyz.xenondevs.invui.item.ItemProvider;


public class WarpModifyItem extends AbstractItem {

    private final WarpData warpData;

    public WarpModifyItem(WarpData warpData) {
        this.warpData = warpData;
    }

    @Override
    public ItemProvider getItemProvider(Player player) {
        return new ItemBuilder(WarpData.toItemStackUsingLanguage(warpData, HomePlugin.getLanguageManager()));
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull Click click) {
        HomePlugin.getGuiManager().openOptionsWarpGUI(player, warpData);
    }
}
