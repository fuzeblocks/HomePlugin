package fr.fuzeblocks.homeplugin.gui.warp.item;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.gui.warp.Input;
import fr.fuzeblocks.homeplugin.gui.warp.InputsSession;
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

public class PermissionItem extends AbstractItem {
    private WarpData warpData;

    public PermissionItem(WarpData warpData) {
            this.warpData = warpData;
    }

    @Override
    public ItemProvider getItemProvider() {
        return new ItemBuilder(Material.PAINTING).setDisplayName(HomePlugin.getLanguageManager().getStringWithColor("Warp.Modify.Permission-item-name", "&eChanger la permission du warp"));
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent inventoryClickEvent) {
            HomePlugin.getInputsManager().createInputsForPlayer(player.getUniqueId(),new Input(player.getUniqueId(), InputsSession.PERMISSION, warpData));
            player.closeInventory();
    }
}
