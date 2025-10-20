package fr.fuzeblocks.homeplugin.gui;

import org.bukkit.Material;
import xyz.xenondevs.invui.gui.PagedGui;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.controlitem.PageItem;

/**
 * The type Forward item.
 */
public class ForwardItem extends PageItem {

    /**
     * Instantiates a new Forward item.
     */
    public ForwardItem() {
        super(true);
    }

    @Override
    public ItemProvider getItemProvider(PagedGui<?> gui) {
        ItemBuilder builder = new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE);
        builder.setDisplayName("Next page")
                .addLoreLines(gui.hasNextPage()
                        ? "Go to page " + (gui.getCurrentPage() + 2) + "/" + gui.getPageAmount()
                        : "There are no more pages");

        return builder;
    }

}