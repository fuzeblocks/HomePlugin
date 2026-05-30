package fr.fuzeblocks.homeplugin.gui.modern.warp.item;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.core.warps.WarpData;
import fr.fuzeblocks.homeplugin.core.warps.input.Input;
import fr.fuzeblocks.homeplugin.core.warps.input.InputsSession;
import fr.fuzeblocks.homeplugin.gui.modern.ModernGuiBridge;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.invui.Click;
import xyz.xenondevs.invui.item.AbstractItem;
import xyz.xenondevs.invui.item.ItemBuilder;
import xyz.xenondevs.invui.item.ItemProvider;


public class PermissionItem extends AbstractItem {
    private final WarpData warpData;

    public PermissionItem(WarpData warpData) {
        this.warpData = warpData;
    }

    @Override
    public ItemProvider getItemProvider(Player player) {
        return new ItemBuilder(Material.REDSTONE_BLOCK).setName(ModernGuiBridge.component(HomePlugin.getLanguageManager().getStringWithColor("Warp.Modify.Permission-item-name", "&eChanger la permission du warp")));
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull Click click) {
        HomePlugin.getInputsManager().createInputsForPlayer(player.getUniqueId(), new Input(player.getUniqueId(), InputsSession.PERMISSION, warpData));
        player.closeInventory();
        player.sendMessage(HomePlugin.getLanguageManager().getStringWithColor("Warp.Modify.Permission-item-message", "&eVeuillez entrer la nouvelle permission du warp dans le chat. (none pour aucune permission, cancel pour annuler)"));
    }
}
