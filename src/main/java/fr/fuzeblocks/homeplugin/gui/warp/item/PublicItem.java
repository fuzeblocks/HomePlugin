package fr.fuzeblocks.homeplugin.gui.warp.item;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.gui.warp.WarpGUIManager;
import fr.fuzeblocks.homeplugin.language.LanguageManager;
import fr.fuzeblocks.homeplugin.warps.WarpData;
import fr.fuzeblocks.homeplugin.warps.WarpManager;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.AbstractItem;

public class PublicItem extends AbstractItem {

    private final LanguageManager languageManager = HomePlugin.getLanguageManager();
    private final WarpData warpData;
    public PublicItem(WarpData warpData) {
        this.warpData = warpData;
    }

    @Override
    public ItemProvider getItemProvider() {
        return new ItemBuilder(Material.END_PORTAL_FRAME)
                .setDisplayName(languageManager.getStringWithColor("Warp.Modify.Public-item-name", "&aRendre le warp public"));
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent inventoryClickEvent) {
        WarpManager warpManager = HomePlugin.getWarpManager();
        warpManager.setWarpPublic(warpData,true);
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING,1.0f,1.0f);
        WarpGUIManager.openOptionsWarpGUI(player,warpManager.getWarp(warpData.getName()));
    }
}
