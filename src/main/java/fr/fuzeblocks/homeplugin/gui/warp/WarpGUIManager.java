package fr.fuzeblocks.homeplugin.gui.warp;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.gui.BackItem;
import fr.fuzeblocks.homeplugin.gui.ForwardItem;
import fr.fuzeblocks.homeplugin.language.LanguageManager;
import fr.fuzeblocks.homeplugin.warps.WarpData;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.gui.PagedGui;
import xyz.xenondevs.invui.gui.structure.Markers;
import xyz.xenondevs.invui.item.Item;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.SimpleItem;
import xyz.xenondevs.invui.window.Window;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class WarpGUIManager {

    private static final String WARP_LIST = "Warp.List.";
    private static final LanguageManager languageManager = HomePlugin.getLanguageManager();

    private static List<Item> getWarpListItems() {
        Map<String, WarpData> warps = HomePlugin.getWarpManager().getAllWarps();

        return warps.keySet()
                .stream()
                .filter(name -> name != null)
                .map(name -> new WarpListItem(name, languageManager.getStringWithColor(WARP_LIST + "Warp-name", "&eNom du warp : %warp%").replace("{warp}", name)))
                .collect(Collectors.toList());
    }

    private static List<Item> getWarpModifyItems() {
        Map<String, WarpData> warps = HomePlugin.getWarpManager().getAllWarps();

        return warps.keySet()
                .stream()
                .filter(name -> name != null)
                .map(name -> new WarpModifyItem(warps.get(name)))
                .collect(Collectors.toList());
    }

    public static void openWarpListGUI(Player player) {
        Item border = new SimpleItem(new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayName(""));
        Gui gui = PagedGui.items()
                .setStructure(
                        "# # # # # # # # #",
                        "# x x x x x x x #",
                        "# x x x x x x x #",
                        "# # # < # > # # #")
                .addIngredient('x', Markers.CONTENT_LIST_SLOT_HORIZONTAL)
                .addIngredient('#', border)
                .addIngredient('<', new BackItem())
                .addIngredient('>', new ForwardItem())
                .setContent(getWarpListItems())
                .build();

        Window window = Window.single()
                .setViewer(player)
                .setTitle(HomePlugin.getLanguageManager().getStringWithColor(WARP_LIST + "Warp-list-header", "&6&lListe des Warps"))
                .setGui(gui)
                .build();

        window.open();
    }

    public static void openEditWarpGUI(Player player, String warpName) {
        WarpData warpData = HomePlugin.getWarpManager().getWarp(warpName);
        Item border = new SimpleItem(new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayName(""));
        Gui gui = PagedGui.items()
                .setStructure(
                        "# # # # # # # # #",
                        "# x x x x x x x #",
                        "# x x x x x x x #",
                        "# # # < # > # # #")
                .addIngredient('x', Markers.CONTENT_LIST_SLOT_HORIZONTAL)
                .addIngredient('#', border)
                .addIngredient('<', new BackItem())
                .addIngredient('>', new ForwardItem())
                .setContent(getWarpModifyItems())
                .build();

        Window window = Window.single()
                .setViewer(player)
                .setTitle(HomePlugin.getLanguageManager().getStringWithColor(languageManager.getStringWithColor(WARP_LIST + "Warp-modify-header", "&6&lGestion des Warps")))
                .setGui(gui)
                .build();

        window.open();
    }

}
