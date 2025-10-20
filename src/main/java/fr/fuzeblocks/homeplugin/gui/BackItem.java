package fr.fuzeblocks.homeplugin.gui;


import org.bukkit.Material;
import xyz.xenondevs.invui.gui.PagedGui;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.controlitem.PageItem;

/**
 * The type Back item.
 */
public class BackItem extends PageItem {

    /**
     * Instantiates a new Back item.
     */
    public BackItem() {
        super(false);
    }

    @Override
    public ItemProvider getItemProvider(PagedGui<?> gui) {
        ItemBuilder builder = new ItemBuilder(Material.RED_STAINED_GLASS_PANE);
        builder.setDisplayName("Previous page")
                .addLoreLines(gui.hasPreviousPage()
                        ? "Go to page " + gui.getCurrentPage() + "/" + gui.getPageAmount()
                        : "You can't go further back");

        return builder;
    }

}