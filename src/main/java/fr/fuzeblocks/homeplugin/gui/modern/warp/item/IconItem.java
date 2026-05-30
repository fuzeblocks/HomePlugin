package fr.fuzeblocks.homeplugin.gui.modern.warp.item;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.core.language.LanguageManager;
import fr.fuzeblocks.homeplugin.core.warps.WarpData;
import fr.fuzeblocks.homeplugin.gui.modern.ModernGuiBridge;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.invui.Click;
import xyz.xenondevs.invui.item.AbstractItem;
import xyz.xenondevs.invui.item.ItemBuilder;
import xyz.xenondevs.invui.item.ItemProvider;


public class IconItem extends AbstractItem {
    private final WarpData warpData;
    private final LanguageManager languageManager = HomePlugin.getLanguageManager();

    public IconItem(WarpData warpData) {
        this.warpData = warpData;
    }

    @Override
    public ItemProvider getItemProvider(Player player) {
        return new ItemBuilder(Material.ITEM_FRAME)
                .setName(ModernGuiBridge.component(languageManager.getStringWithColor("Warp.Modify.Icon-item-name", "&eChanger l'icône du warp")));
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull Click click) {
       HomePlugin.getGuiManager().openChangeIconWarpGUI(player, warpData);
    }
}
