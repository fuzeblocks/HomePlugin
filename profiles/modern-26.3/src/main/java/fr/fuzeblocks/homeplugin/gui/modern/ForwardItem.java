package fr.fuzeblocks.homeplugin.gui.modern;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.core.language.LanguageManager;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.invui.Click;
import xyz.xenondevs.invui.gui.PagedGui;
import xyz.xenondevs.invui.item.AbstractItem;
import xyz.xenondevs.invui.item.Item;
import xyz.xenondevs.invui.item.ItemBuilder;
import xyz.xenondevs.invui.item.ItemProvider;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class ForwardItem extends AbstractItem {


    private final LanguageManager languageManager = HomePlugin.getLanguageManager();
    private final String ITEM_KEY = "Language.Gui.ForwardItem.";

    private PagedGui<?> gui;

    public ForwardItem() {

    }
    public void setGui(PagedGui<?> gui) {
        this.gui = gui;
        gui.addPageChangeHandler((oldPage, newPage) -> notifyWindows());
        gui.addPageCountChangeHandler((oldCount, newCount) -> notifyWindows());
    }

    @Override
    public ItemProvider getItemProvider(Player viewer) {
        String name = languageManager.getStringWithColor(ITEM_KEY + "Name", "&aPage suivante");

        boolean hasNext = gui.getPage() < gui.getPageCount() - 1;

        String lore = hasNext
                ? languageManager.getStringWithColor(ITEM_KEY + "ChangePage", "Aller à la page %current_page% / %total_pages%")
                        .replace("%current_page%", String.valueOf(gui.getPage() + 2))
                        .replace("%total_pages%", String.valueOf(gui.getPageCount()))
                : languageManager.getStringWithColor(ITEM_KEY + "MaxForward", "&cVous êtes déjà à la dernière page !");

        return new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE)
                .setName(ModernGuiBridge.component((name)))
                .setLore(List.of(ModernGuiBridge.component((lore))));
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull Click click) {
        if (gui.getPage() < gui.getPageCount() - 1) {
            gui.setPage(gui.getPage() + 1);
        }
    }
}