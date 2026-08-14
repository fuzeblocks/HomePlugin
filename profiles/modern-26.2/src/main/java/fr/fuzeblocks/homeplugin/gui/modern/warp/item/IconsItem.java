package fr.fuzeblocks.homeplugin.gui.modern.warp.item;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.core.warps.WarpData;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.TestOnly;
import xyz.xenondevs.invui.Click;
import xyz.xenondevs.invui.item.AbstractItem;
import xyz.xenondevs.invui.item.ItemBuilder;
import xyz.xenondevs.invui.item.ItemProvider;

import java.util.function.Function;


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
    @TestOnly
    public ItemProvider getItemProvider(Player player) {
        if (warpMaterial.equals(material)) {
          return new ItemBuilder(material)
                    .addModifier(new Function<ItemStack, ItemStack>() {
                        @Override
                        public ItemStack apply(ItemStack itemStack) {
                            ItemStack i = new ItemStack(material, 1);
                            i.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                            i.addEnchantment(Enchantment.UNBREAKING,1);
                            return i;
                        }
                    });

        }
        return new ItemBuilder(material);
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull Click click) {
        if (warpMaterial != material) {
            HomePlugin.getWarpManager().setWarpIcon(warpData, material);
            player.closeInventory();
        }
    }
}
