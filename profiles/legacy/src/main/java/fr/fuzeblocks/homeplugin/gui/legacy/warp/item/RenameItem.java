package fr.fuzeblocks.homeplugin.gui.legacy.warp.item;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.core.warps.input.Input;
import fr.fuzeblocks.homeplugin.core.warps.input.InputsSession;
import fr.fuzeblocks.homeplugin.core.warps.WarpData;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.AbstractItem;

public class RenameItem extends AbstractItem {

    private WarpData warpData;

    public RenameItem(WarpData warpData) {
        this.warpData = warpData;
    }

    @Override
    public ItemProvider getItemProvider() {
        return new ItemBuilder(Material.ACACIA_SIGN).setDisplayName(HomePlugin.getLanguageManager().getStringWithColor("Warp.Modify.Rename-item-name", "&eRenommer le warp"));
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent inventoryClickEvent) {
        HomePlugin.getInputsManager().createInputsForPlayer(player.getUniqueId(), new Input(player.getUniqueId(), InputsSession.NAME, warpData));
        player.closeInventory();
        player.sendMessage(HomePlugin.getLanguageManager().getStringWithColor("Warp.Modify.Rename-item-message", "&eVeuillez entrer le nouveau nom du warp dans le chat. (cancel pour annuler)"));
    }
}
