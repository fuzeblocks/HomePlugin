package fr.fuzeblocks.homeplugin.gui.legacy.home.item;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.core.language.LanguageManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;import xyz.xenondevs.invui.item.impl.AbstractItem;

import java.util.List;

public class CancelHomeDeleteConfirmation extends AbstractItem {
    private final String homeName;
    private final LanguageManager languageManager = HomePlugin.getLanguageManager();
    private static final String HOME = "Home.";
    public CancelHomeDeleteConfirmation(String homeName) {
        this.homeName = homeName;
    }

    @Override
    public ItemProvider getItemProvider(Player viewer) {
        String displayName = languageManager
                .getStringWithColor(HOME + "Home-cancel-deletion-title","&aAnnuler la suppression de %home%")
                .replace("%home%", homeName);

        String lore = languageManager.getStringWithColor(HOME + "Home-cancel-deletion-lore","&aÊtes-vous sûr de vouloir annuler la suppression de ce home ?");


        return new ItemBuilder(Material.EMERALD_BLOCK)
                .setDisplayName(displayName)
                .setLegacyLore(List.of(
                        lore
                ));
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent event) {
        player.closeInventory();
        HomePlugin.getGuiManager().openDeleteHome(player, homeName);
        player.sendMessage(languageManager.getStringWithColor(HOME + "Home-delete-canceled","&aSuppression annulée"));
    }
}
