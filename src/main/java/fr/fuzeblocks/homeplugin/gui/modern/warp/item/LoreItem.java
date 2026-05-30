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


public class LoreItem extends AbstractItem {
    private final WarpData warpData;

    public LoreItem(WarpData warpData) {
        this.warpData = warpData;
    }

    @Override
    public ItemProvider getItemProvider(Player player) {
        return new ItemBuilder(Material.PAINTING).setName(ModernGuiBridge.component(HomePlugin.getLanguageManager().getStringWithColor("Warp.Modify.Lore-item-name", "&eRenommer la lore du warp")));
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull Click click) {
        HomePlugin.getInputsManager().createInputsForPlayer(player.getUniqueId(), new Input(player.getUniqueId(), InputsSession.LORE, warpData));
        player.closeInventory();
        player.sendMessage(HomePlugin.getLanguageManager().getStringWithColor("Warp.Modify.Lore-item-message", "&eVeuillez entrer la nouvelle lore du warp dans le chat. (cancel pour annuler)"));
    }
}
