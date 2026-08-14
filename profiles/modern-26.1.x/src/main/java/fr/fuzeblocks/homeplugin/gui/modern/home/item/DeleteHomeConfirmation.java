package fr.fuzeblocks.homeplugin.gui.modern.home.item;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.core.language.LanguageManager;
import fr.fuzeblocks.homeplugin.gui.modern.ModernGuiBridge;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import xyz.xenondevs.invui.Click;
import xyz.xenondevs.invui.item.AbstractItem;
import xyz.xenondevs.invui.item.ItemBuilder;
import xyz.xenondevs.invui.item.ItemProvider;

import java.util.List;

public class DeleteHomeConfirmation extends AbstractItem {
    private final String homeName;
    private final LanguageManager languageManager = HomePlugin.getLanguageManager();
    private static final String HOME = "Home.";
    public DeleteHomeConfirmation(String homeName) {
        this.homeName = homeName;
    }

    @Override
    public ItemProvider getItemProvider(Player viewer) {
        String displayName = languageManager
                .getStringWithColor(HOME + "Home-delete-confirmation-title","&cConfirmer la suppression de %home%")
                .replace("%home%", homeName);

        String lore = languageManager.getStringWithColor(HOME + "Home-delete-confirmation-lore","&cÊtes-vous sûr de vouloir supprimer ce home ? Cette action ne peut pas être annulée !");


        return new ItemBuilder(Material.BARRIER)
                .setName(ModernGuiBridge.component(displayName))
                .setLore(List.of(
                        ModernGuiBridge.component(lore)
                ));
    }

    @Override
    public void handleClick(ClickType clickType, Player player, Click click) {
        HomePlugin.getHomeManager().deleteHome(player, homeName);
        player.sendMessage(languageManager.getStringWithColor(HOME + "Home-deleted"));
        player.closeInventory();
    }
}
