package fr.fuzeblocks.homeplugin.gui.warp.item;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.gui.warp.WarpGUIManager;
import fr.fuzeblocks.homeplugin.language.LanguageManager;
import fr.fuzeblocks.homeplugin.warps.WarpData;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.invui.gui.SlotElement;
import xyz.xenondevs.invui.item.Item;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.AbstractItem;

import java.util.function.Supplier;

public class IconItem extends AbstractItem {
    private final WarpData warpData;
    private final LanguageManager languageManager = HomePlugin.getLanguageManager();
    public IconItem(WarpData warpData) {
        this.warpData = warpData;
    }

    @Override
    public ItemProvider getItemProvider() {
        return new ItemBuilder(Material.ITEM_FRAME)
                .setDisplayName(languageManager.getStringWithColor("Warp.Modify.Icon-item-name", "&eChanger l'icône du warp"));
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent inventoryClickEvent) {
        WarpGUIManager.openChangeIconWarpGUI(player,warpData);
    }
}
