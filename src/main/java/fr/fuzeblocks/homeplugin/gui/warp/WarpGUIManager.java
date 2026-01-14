package fr.fuzeblocks.homeplugin.gui.warp;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.gui.BackItem;
import fr.fuzeblocks.homeplugin.gui.ForwardItem;
import fr.fuzeblocks.homeplugin.gui.HomeItem;
import fr.fuzeblocks.homeplugin.home.HomeManager;
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


    private static List<Item> getWarpItems() {
        Map<String, WarpData> warps = HomePlugin.getWarpManager().getAllWarps();

        return warps.keySet()
                .stream()
                .filter(name -> name != null)
                .map(WarpItem::new)
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
                .setContent(getWarpItems())
                .build();

        Window window = Window.single()
                .setViewer(player)
                .setTitle(HomePlugin.getLanguageManager().getStringWithColor("Warp List"))
                .setGui(gui)
                .build();

        window.open();
        }
    public static void openEditWarpGUI(Player player, String warpName) {
        WarpData warpData = HomePlugin.getWarpManager().getWarp(warpName);
    }

}
