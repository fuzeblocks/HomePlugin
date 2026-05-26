package fr.fuzeblocks.homeplugin.gui.modern.warp.item;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.core.warps.WarpData;
import fr.fuzeblocks.homeplugin.core.warps.input.Input;import fr.fuzeblocks.homeplugin.core.warps.input.InputsSession;import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.invui.Click;
import xyz.xenondevs.invui.item.AbstractItem;
import xyz.xenondevs.invui.item.ItemBuilder;
import xyz.xenondevs.invui.item.ItemProvider;


public class ExpirationItem extends AbstractItem {
    private final WarpData warpData;

    public ExpirationItem(WarpData warpData) {
        this.warpData = warpData;
    }

    @Override
    public ItemProvider getItemProvider(Player player) {
        return new ItemBuilder(Material.CLOCK).setName(HomePlugin.getLanguageManager().getStringWithColor("Warp.Modify.Expiration-item-name", "&eChanger la durée d'expiration du warp"));
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull Click click) {
        HomePlugin.getInputsManager().createInputsForPlayer(player.getUniqueId(), new Input(player.getUniqueId(), InputsSession.EXPIRATION, warpData));
        player.closeInventory();
        player.sendMessage(HomePlugin.getLanguageManager().getStringWithColor("Warp.Modify.Expiration-item-message", "&eVeuillez entrer la nouvelle durée d'expiration du warp dans le chat avec pour format s,h,d,m (ex 1d,2m,3d,4s) . (cancel pour annuler ou never pour ne jamais faire expirer)"));
    }
}
