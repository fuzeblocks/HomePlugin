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

public class DeleteHomeConfirmation extends AbstractItem {
    private final String homeName;
    private final LanguageManager languageManager = HomePlugin.getLanguageManager();
    private static final String HOME = "Home.";
    public DeleteHomeConfirmation(String homeName) {
        this.homeName = homeName;
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent event) {
        HomePlugin.getHomeManager().deleteHome(player, homeName);
        player.sendMessage(languageManager.getStringWithColor(HOME + "Home-deleted"));
        player.closeInventory();
    }
    @Override
    public ItemProvider getItemProvider() {
        String displayName = languageManager
                .getStringWithColor(HOME + "Home-delete-confirmation-title","&cConfirmer la suppression de %home%")
                .replace("%home%", homeName);

        String lore = languageManager.getStringWithColor(HOME + "Home-delete-confirmation-lore","&cÊtes-vous sûr de vouloir supprimer ce home ? Cette action ne peut pas être annulée !");


        return new ItemBuilder(Material.BARRIER)
                .setDisplayName(displayName)
                .setLegacyLore(List.of(
                        lore
                ));
    }}
