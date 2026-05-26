package fr.fuzeblocks.homeplugin.gui.modern.warp.item;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.core.warps.WarpData;
import fr.fuzeblocks.homeplugin.core.warps.input.Input;
import fr.fuzeblocks.homeplugin.core.warps.input.InputsSession;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.invui.Click;
import xyz.xenondevs.invui.item.AbstractItem;
import xyz.xenondevs.invui.item.ItemBuilder;
import xyz.xenondevs.invui.item.ItemProvider;


public class RenameItem extends AbstractItem {

    private final WarpData warpData;

    public RenameItem(WarpData warpData) {
        this.warpData = warpData;
    }

    @Override
    public ItemProvider getItemProvider(Player player) {
        return new ItemBuilder(Material.ACACIA_SIGN).setName(HomePlugin.getLanguageManager().getStringWithColor("Warp.Modify.Rename-item-name", "&eRenommer le warp"));
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull Click click) {
        HomePlugin.getInputsManager().createInputsForPlayer(player.getUniqueId(), new Input(player.getUniqueId(), InputsSession.NAME, warpData));
        player.closeInventory();
        player.sendMessage(HomePlugin.getLanguageManager().getStringWithColor("Warp.Modify.Rename-item-message", "&eVeuillez entrer le nouveau nom du warp dans le chat. (cancel pour annuler)"));
    }
}
