package fr.fuzeblocks.homeplugin.gui.modern.warp.item;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.core.language.LanguageManager;
import fr.fuzeblocks.homeplugin.core.warps.WarpData;
import fr.fuzeblocks.homeplugin.core.warps.WarpManager;
import fr.fuzeblocks.homeplugin.gui.modern.ModernGuiBridge;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.invui.Click;import xyz.xenondevs.invui.item.AbstractItem;
import xyz.xenondevs.invui.item.ItemBuilder;
import xyz.xenondevs.invui.item.ItemProvider;


public class DecreaseCostItem extends AbstractItem {
    private final LanguageManager languageManager = HomePlugin.getLanguageManager();
    private final WarpData warpData;

    public DecreaseCostItem(WarpData warpData) {
        this.warpData = warpData;
    }

    @Override
    public ItemProvider getItemProvider(Player player) {
        return new ItemBuilder(Material.REDSTONE_BLOCK)
                .setName(ModernGuiBridge.component(languageManager.getStringWithColor("Warp.Modify.Decrease-cost-item-name", "&cDiminuer le coût du warp")));
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull Click click) {
        WarpManager warpManager = HomePlugin.getWarpManager();
        double currentCost = warpData.getCost();
        if (currentCost > 0.0) {
            warpManager.setWarpCost(warpData, currentCost - 1);
            player.playSound(player.getLocation(), "entity.experience_orb.pickup", 1.0f, 1.0f);
             HomePlugin.getGuiManager().openCostWarpGUI(player, warpManager.getWarp(warpData.getName()));
        }
    }


}
