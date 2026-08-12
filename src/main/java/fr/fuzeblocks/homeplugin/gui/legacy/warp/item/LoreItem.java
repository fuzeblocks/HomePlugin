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

public class LoreItem extends AbstractItem {
    private WarpData warpData;

    public LoreItem(WarpData warpData) {
        this.warpData = warpData;
    }

    @Override
    public ItemProvider getItemProvider() {
        return new ItemBuilder(Material.PAINTING).setDisplayName(HomePlugin.getLanguageManager().getStringWithColor("Warp.Modify.Lore-item-name", "&eRenommer la lore du warp"));
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent inventoryClickEvent) {
        HomePlugin.getInputsManager().createInputsForPlayer(player.getUniqueId(), new Input(player.getUniqueId(), InputsSession.LORE, warpData));
        player.closeInventory();
        player.sendMessage(HomePlugin.getLanguageManager().getStringWithColor("Warp.Modify.Lore-item-message", "&eVeuillez entrer la nouvelle lore du warp dans le chat. (cancel pour annuler)"));
    }
}
