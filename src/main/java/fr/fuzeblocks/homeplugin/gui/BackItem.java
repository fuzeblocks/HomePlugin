package fr.fuzeblocks.homeplugin.gui;


import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.language.LanguageManager;
import org.bukkit.Material;
import xyz.xenondevs.invui.gui.PagedGui;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.controlitem.PageItem;

/**
 * The type Back item.
 */
public class BackItem extends PageItem {

    private final LanguageManager languageManager = HomePlugin.getLanguageManager();
    private final String ITEM_KEY = "Language.Gui.BackItem.";

    /**
     * Instantiates a new Back item.
     */
    public BackItem() {
        super(false);
    }

    @Override
    public ItemProvider getItemProvider(PagedGui<?> gui) {
        ItemBuilder builder = new ItemBuilder(Material.RED_STAINED_GLASS_PANE);
        builder.setDisplayName(languageManager.getStringWithColor(ITEM_KEY + "Name", "&cPage précédente"))
                .addLoreLines(gui.hasPreviousPage()
                        ? languageManager.getStringWithColor(ITEM_KEY + "ChangePage", "Aller à la page %current_page% / %total_pages%").replace("%current_page%",String.valueOf(gui.getCurrentPage())).replace("%total_pages%",String.valueOf(gui.getPageAmount()))
                        : languageManager.getStringWithColor(ITEM_KEY + "MaxBack", "&cVous ne pouvez pas revenir plus loin !"));

        return builder;
    }

}