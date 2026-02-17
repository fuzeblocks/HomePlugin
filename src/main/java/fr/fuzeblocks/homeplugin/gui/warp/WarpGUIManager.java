package fr.fuzeblocks.homeplugin.gui.warp;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.gui.BackItem;
import fr.fuzeblocks.homeplugin.gui.ForwardItem;
import fr.fuzeblocks.homeplugin.gui.warp.item.*;
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
import xyz.xenondevs.invui.window.AnvilWindow;
import xyz.xenondevs.invui.window.Window;

import java.util.Arrays;
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

    public static void openEditWarpGUI(Player player) {
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
                .setTitle(languageManager.getStringWithColor(WARP_LIST + "Warp-modify-header", "&6&lGestion des Warps"))
                .setGui(gui)
                .build();

        window.open();
    }

    public static void openOptionsWarpGUI(Player player,WarpData warpData) {
        Item border = new SimpleItem(new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayName(""));
        Gui gui = Gui.normal()
                .setStructure(
                        "# # # # # # # # #",
                        "# D E P A I C N #",
                        "# S L x x x x x #",
                        "# # # # # # # # #")
                .addIngredient('x', Markers.CONTENT_LIST_SLOT_HORIZONTAL)
                .addIngredient('#', border)
                .addIngredient('D', new DeleteItem(warpData))
                 .addIngredient('E',new ExpirationItem(warpData))
                .addIngredient('P', new PublicItem(warpData))
                .addIngredient('A',new PermissionItem(warpData))
                .addIngredient('I', new IconItem(warpData))
                .addIngredient('C', new CostItem(warpData))
                .addIngredient('N', new RenameItem(warpData))
                .addIngredient('S',new LoreItem(warpData))
                .addIngredient('L', new LocationItem(warpData))


                .build();

        Window window = Window.single()
                .setViewer(player)
                .setTitle(languageManager.getStringWithColor(WARP_LIST + "Warp-modify-header", "&6&lGestion des Warps"))
                .setGui(gui)
                .build();

        window.open();
    }
    public static void openCostWarpGUI(Player player,WarpData warpData) {
        Item border = new SimpleItem(new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayName(""));
        Gui gui = Gui.normal()
                .setStructure(
                        "# # # # # # # # #",
                        "# x + x N x - x #",
                        "# # # # # # # # #",
                        "# # # # # # # # #")
                .addIngredient('x', Markers.CONTENT_LIST_SLOT_HORIZONTAL)
                .addIngredient('#', border)
                .addIngredient('+', new IncreaseCostItem(warpData))
                .addIngredient('-', new DecreaseCostItem(warpData))
                .addIngredient('N', new SimpleItem(new ItemBuilder(Material.EMERALD).setDisplayName(languageManager.getStringWithColor(WARP_LIST + "Warp-modify-cost-current", "&aCoût actuel: &e%cost%").replace("%cost%", String.valueOf(warpData.getCost())))))
                .build();

        Window window = Window.single()
                .setViewer(player)
                .setTitle(languageManager.getStringWithColor(WARP_LIST + "Warp-modify-cost-header", "&6&lDéfinir le coût du Warp"))
                .setGui(gui)
                .build();

        window.open();
    }

    public static void openChangeIconWarpGUI(Player player,WarpData warpData) {
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
                .setContent(Arrays.stream(Material.values()).filter(mat -> mat != Material.AIR).map(mat -> new IconsItem(mat, warpData)).collect(Collectors.toList()))
                .build();

        Window window = Window.single()
                .setViewer(player)
                .setTitle(languageManager.getStringWithColor(WARP_LIST + "Warp-modify-icon-header", "&6&lChanger l'icône du Warp"))
                .setGui(gui)
                .build();

        window.open();
    }



}
