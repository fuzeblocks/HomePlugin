package fr.fuzeblocks.homeplugin.gui.legacy.warp.item;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.core.language.LanguageManager;
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

public class LocationItem extends AbstractItem {
    private final LanguageManager languageManager = HomePlugin.getLanguageManager();

    private final WarpData warpData;

    public LocationItem(WarpData warpData) {
        this.warpData = warpData;
    }

    @Override
    public ItemProvider getItemProvider() {
        return new ItemBuilder(Material.COMPASS).setDisplayName(languageManager.getStringWithColor("Warp.Modify.Location-item-name", "&eDéfinir la location du warp"));
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent inventoryClickEvent) {
        HomePlugin.getInputsManager().createInputsForPlayer(player.getUniqueId(), new Input(player.getUniqueId(), InputsSession.LOCATION, warpData));
        player.closeInventory();
        player.sendMessage(languageManager.getStringWithColor("Warp.Modify.Location-item-message", "&eVeuillez entrer la location du warp dans le chat. (Format: x=100 y=64 z=200 ou 'here')"));
    }
}
