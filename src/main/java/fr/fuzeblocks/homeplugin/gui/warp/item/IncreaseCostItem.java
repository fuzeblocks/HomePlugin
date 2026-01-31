package fr.fuzeblocks.homeplugin.gui.warp.item;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.language.LanguageManager;
import fr.fuzeblocks.homeplugin.warps.WarpData;
import fr.fuzeblocks.homeplugin.warps.WarpManager;
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

public class IncreaseCostItem extends AbstractItem {
    private final LanguageManager languageManager = HomePlugin.getLanguageManager();

    private final WarpData warpData;
    public IncreaseCostItem(WarpData warpData) {
        this.warpData = warpData;
    }

    @Override
    public ItemProvider getItemProvider() {
        return new ItemBuilder(Material.DIAMOND)
                .setDisplayName(languageManager.getStringWithColor("Warp.Modify.Increase-Cost-item-name", "&aAugmenter le coût du warp"));
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent inventoryClickEvent) {
            double cost = warpData.getCost();
            WarpManager warpManager = HomePlugin.getWarpManager();
            warpManager.setWarpCost(warpData, cost + 1);
            player.playSound(player.getLocation(), "entity.experience_orb.pickup", 1.0f, 1.0f);
    }
}
