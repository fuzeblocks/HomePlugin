package fr.fuzeblocks.homeplugin.gui.warp.item;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.warps.WarpData;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.impl.AbstractItem;

public class WarpModifyItem extends AbstractItem {

    private final WarpData warpData;

    public WarpModifyItem(WarpData warpData) {
        this.warpData = warpData;
    }

    @Override
    public ItemProvider getItemProvider() {
        return WarpData.toItemBuilderUsingLanguage(warpData, HomePlugin.getLanguageManager());
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent inventoryClickEvent) {

    }
}
