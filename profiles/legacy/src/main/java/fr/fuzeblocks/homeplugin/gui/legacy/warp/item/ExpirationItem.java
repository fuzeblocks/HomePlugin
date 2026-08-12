package fr.fuzeblocks.homeplugin.gui.legacy.warp.item;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.core.warps.WarpData;
import fr.fuzeblocks.homeplugin.core.warps.input.Input;
import fr.fuzeblocks.homeplugin.core.warps.input.InputsSession;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;

public class ExpirationItem extends xyz.xenondevs.invui.item.impl.AbstractItem {


    private final WarpData warpData;

    public ExpirationItem(WarpData warpData) {
        this.warpData = warpData;
    }

    @Override
    public ItemProvider getItemProvider() {
        String name = HomePlugin.getLanguageManager()
                .getStringWithColor("Warp.Modify.Expiration-item-name", "&eChanger la durée d'expiration du warp");
        return new ItemBuilder(Material.CLOCK)
                .setDisplayName(name);
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull org.bukkit.event.inventory.InventoryClickEvent inventoryClickEvent) {
        HomePlugin.getInputsManager().createInputsForPlayer(
                player.getUniqueId(),
                new Input(player.getUniqueId(), InputsSession.EXPIRATION, warpData)
        );
        player.closeInventory();
        player.sendMessage(HomePlugin.getLanguageManager().getStringWithColor(
                "Warp.Modify.Expiration-item-message",
                "&eVeuillez entrer la nouvelle durée d'expiration du warp dans le chat avec pour format s,h,d,m (ex 1d,2m,3d,4s) . (cancel pour annuler ou never pour ne jamais faire expirer)"
        ));
    }
}