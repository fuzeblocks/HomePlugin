package fr.fuzeblocks.homeplugin.gui.modern.warp.item;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.core.warps.WarpData;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.invui.Click;import xyz.xenondevs.invui.item.AbstractItem;
import xyz.xenondevs.invui.item.ItemBuilder;
import xyz.xenondevs.invui.item.ItemProvider;


public class IconsItem extends AbstractItem {

    private final Material material;
    private final Material warpMaterial;
    private final WarpData warpData;

    public IconsItem(Material material, WarpData warpData) {
        this.material = material;
        this.warpMaterial = warpData.getIcon();
        this.warpData = warpData;
    }

    @Override
    public ItemProvider getItemProvider(Player player) {
      //  return warpMaterial == material ? new ItemBuilder(material).addModifier(ItemFlag.HIDE_ENCHANTS).addEnchantment(Enchantment.DURABILITY, 1, true) : new ItemBuilder(material);
        return null;
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull Click click) {
        if (warpMaterial == material) {
        } else {
            HomePlugin.getWarpManager().setWarpIcon(warpData, material);
            player.closeInventory();
        }
    }
}
