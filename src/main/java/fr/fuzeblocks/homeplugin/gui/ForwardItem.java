package fr.fuzeblocks.homeplugin.gui;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.language.LanguageManager;
import org.bukkit.Material;
import xyz.xenondevs.invui.gui.PagedGui;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.controlitem.PageItem;

/**
 * The type Forward item.
 */
public class ForwardItem extends PageItem {

    private final LanguageManager languageManager = HomePlugin.getLanguageManager();
    private final String ITEM_KEY = "Language.Gui.ForwardItem.";

    /**
     * Instantiates a new Forward item.
     */
    public ForwardItem() {
        super(true);
    }

    @Override
    public ItemProvider getItemProvider(PagedGui<?> gui) {
        ItemBuilder builder = new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE);
        builder.setDisplayName(languageManager.getStringWithColor(ITEM_KEY + "Name", "&aPage suivante"))
                .addLoreLines(gui.hasNextPage()
                        ? languageManager.getStringWithColor(ITEM_KEY + "ChangePage","Aller à la page %current_page% / %total_pages%").replace("%current_page%",String.valueOf(gui.getCurrentPage() + 2)).replace("%total_pages%",String.valueOf(gui.getPageAmount()))
                        : languageManager.getStringWithColor(ITEM_KEY + "MaxForward", "&cVous êtes déjà à la dernière page !"));

        return builder;
    }

}