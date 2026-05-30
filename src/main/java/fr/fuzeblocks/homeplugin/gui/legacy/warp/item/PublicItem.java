package fr.fuzeblocks.homeplugin.gui.legacy.warp.item;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.core.language.LanguageManager;
import fr.fuzeblocks.homeplugin.core.warps.WarpData;
import fr.fuzeblocks.homeplugin.core.warps.WarpManager;
import fr.fuzeblocks.homeplugin.gui.modern.ModernGuiBridge;
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
                .setDisplayName(languageManager.getStringWithColor("Warp.Modify.Public-item-name", "&aChanger la visibilité du warp"));
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent inventoryClickEvent) {
        WarpManager warpManager = HomePlugin.getWarpManager();
        warpManager.setWarpPublic(warpData, !warpData.isPublic());
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f);
        player.sendMessage(languageManager.getStringWithColor("Warp.Modify.Public-item-message", "&aLe warp est maintenant &e{status}&a !").replace("{status}", String.valueOf(warpData.isPublic())));
        HomePlugin.getGuiManager().openOptionsWarpGUI(player, warpManager.getWarp(warpData.getName()));
    }
}
