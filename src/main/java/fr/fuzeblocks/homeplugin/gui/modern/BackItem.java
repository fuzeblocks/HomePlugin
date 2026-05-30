package fr.fuzeblocks.homeplugin.gui.modern;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.core.language.LanguageManager;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;
import xyz.xenondevs.invui.Click;
import xyz.xenondevs.invui.gui.PagedGui;
import xyz.xenondevs.invui.item.AbstractItem;
import xyz.xenondevs.invui.item.Item;
import xyz.xenondevs.invui.item.ItemBuilder;
import xyz.xenondevs.invui.item.ItemProvider;


import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class BackItem extends AbstractItem {

    private static final LegacyComponentSerializer LEGACY = LegacyComponentSerializer.legacySection();

    private final LanguageManager languageManager = HomePlugin.getLanguageManager();
    private final String ITEM_KEY = "Language.Gui.BackItem.";

    private final PagedGui<Item> gui;


    public BackItem(AtomicReference<PagedGui<Item>> guiRef) {
        this.gui = guiRef.get();
        gui.addPageChangeHandler((oldPage, newPage) -> notifyWindows());
        gui.addPageCountChangeHandler((oldCount, newCount) -> notifyWindows());
    }

    @Override
    public ItemProvider getItemProvider(Player viewer) {

        String name = languageManager.getStringWithColor(ITEM_KEY + "Name", "&cPage précédente");

        boolean hasPrevious = gui.getPage() > 0;

        String lore = hasPrevious
                ? languageManager.getStringWithColor(ITEM_KEY + "ChangePage", "Aller à la page %current_page% / %total_pages%")
                        .replace("%current_page%", String.valueOf(gui.getPage()))
                        .replace("%total_pages%", String.valueOf(gui.getPageCount()))
                : languageManager.getStringWithColor(ITEM_KEY + "MaxBack", "&cVous ne pouvez pas revenir plus loin !");

        return new ItemBuilder(Material.RED_STAINED_GLASS_PANE)
                .setName(LEGACY.deserialize(name))
                .setLore(List.of(LEGACY.deserialize(lore)));
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull Click click) {
        if (gui.getPage() > 0) {
            gui.setPage(gui.getPage() - 1);
        }
    }
}