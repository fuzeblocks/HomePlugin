package fr.fuzeblocks.homeplugin.gui.warp.item;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.language.LanguageManager;
import fr.fuzeblocks.homeplugin.warps.WarpData;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.AbstractItem;

public class DeleteItem extends AbstractItem  {

    private final LanguageManager languageManager = HomePlugin.getLanguageManager();

    private final WarpData warpData;
    public DeleteItem(WarpData warpData) {
        this.warpData = warpData;
    }
    @Override
    public ItemProvider getItemProvider() {
        return new ItemBuilder(Material.BARRIER)
                .setDisplayName(languageManager.getStringWithColor("Warp.Modify.Delete-item-name", "&cSupprimer le warp"));
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent inventoryClickEvent) {
        HomePlugin.getWarpManager().deleteWarp(warpData);
        player.closeInventory();
    }
}
