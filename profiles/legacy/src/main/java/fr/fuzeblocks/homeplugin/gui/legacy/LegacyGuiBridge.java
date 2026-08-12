package fr.fuzeblocks.homeplugin.gui.legacy;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.core.home.HomeManager;
import fr.fuzeblocks.homeplugin.core.language.LanguageManager;
import fr.fuzeblocks.homeplugin.core.warps.WarpData;
import fr.fuzeblocks.homeplugin.gui.GuiBridge;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.gui.PagedGui;
import xyz.xenondevs.invui.gui.structure.Markers;
import xyz.xenondevs.invui.item.Item;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.SimpleItem;
import xyz.xenondevs.invui.window.Window;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public final class LegacyGuiBridge implements GuiBridge {

    private static final String WARP_LIST = "Warp.List.";
    private static final String HOME = "Home.";
    private static final LanguageManager languageManager = HomePlugin.getLanguageManager();

    private static List<Item> getWarpListItems() {
        Map<String, WarpData> warps = HomePlugin.getWarpManager().getAllWarps();

        return warps.keySet()
                .stream()
                .filter(Objects::nonNull)
                .map(name -> new fr.fuzeblocks.homeplugin.gui.legacy.warp.item.WarpListItem(name, languageManager.getStringWithColor(WARP_LIST + "Warp-name", "&eNom du warp : %warp%").replace("%warp%", name)))
                .collect(Collectors.toList());
    }

    private static List<Item> getWarpModifyItems() {
        Map<String, WarpData> warps = HomePlugin.getWarpManager().getAllWarps();

        return warps.keySet()
                .stream()
                .filter(name -> name != null)
                .map(name -> new fr.fuzeblocks.homeplugin.gui.legacy.warp.item.WarpModifyItem(warps.get(name)))
                .collect(Collectors.toList());
    }

    private static boolean isMaterialValidForIcon(Material material) {
        String name = material.name();
        return (!(name.equals("AIR") || name.endsWith("_AIR")));
    }

    @Override
    public void openWarpListGUI(Player player) {
        List<Item> warpItems = getWarpListItems();

        if (warpItems.isEmpty()) {
            player.sendMessage(languageManager.getStringWithColor(WARP_LIST + "No-warps", "&cAucun warp n'est défini pour le moment."));
            return;
        }

        Item border = new SimpleItem(new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayName(""));
        Gui gui = PagedGui.items()
                .setStructure(
                        "# # # # # # # # #",
                        "# x x x x x x x #",
                        "# x x x x x x x #",
                        "# # # < # > # # #")
                .addIngredient('x', Markers.CONTENT_LIST_SLOT_HORIZONTAL)
                .addIngredient('#', border)
                .addIngredient('<', new fr.fuzeblocks.homeplugin.gui.legacy.BackItem())
                .addIngredient('>', new fr.fuzeblocks.homeplugin.gui.legacy.ForwardItem())
                .setContent(warpItems)
                .build();

        Window window = Window.single()
                .setViewer(player)
                .setTitle(HomePlugin.getLanguageManager().getStringWithColor(WARP_LIST + "Warp-list-header", "&6&lListe des Warps"))
                .setGui(gui)
                .build();

        window.open();
    }

    @Override
    public void openEditWarpGUI(Player player) {
        List<Item> warpItems = getWarpModifyItems();
        if (warpItems.isEmpty()) {
            player.sendMessage(languageManager.getStringWithColor(WARP_LIST + "No-warps", "&cAucun warp n'est défini pour le moment."));
            return;
        }

        Item border = new SimpleItem(new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayName(""));
        Gui gui = PagedGui.items()
                .setStructure(
                        "# # # # # # # # #",
                        "# x x x x x x x #",
                        "# x x x x x x x #",
                        "# # # < # > # # #")
                .addIngredient('x', Markers.CONTENT_LIST_SLOT_HORIZONTAL)
                .addIngredient('#', border)
                .addIngredient('<', new fr.fuzeblocks.homeplugin.gui.legacy.BackItem())
                .addIngredient('>', new fr.fuzeblocks.homeplugin.gui.legacy.ForwardItem())
                .setContent(warpItems)
                .build();

        Window window = Window.single()
                .setViewer(player)
                .setTitle(languageManager.getStringWithColor(WARP_LIST + "Warp-modify-header", "&6&lGestion des Warps"))
                .setGui(gui)
                .build();

        window.open();
    }

    @Override
    public void openOptionsWarpGUI(Player player, WarpData warpData) {
        Item border = new SimpleItem(new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayName(""));
        Gui gui = Gui.normal()
                .setStructure(
                        "# # # # # # # # #",
                        "# D E P A I C N #",
                        "# S L B x x x x #",
                        "# # # # # # # # #")
                .addIngredient('x', Markers.CONTENT_LIST_SLOT_HORIZONTAL)
                .addIngredient('#', border)
                .addIngredient('D', new fr.fuzeblocks.homeplugin.gui.legacy.warp.item.DeleteItem(warpData))
                .addIngredient('E', new fr.fuzeblocks.homeplugin.gui.legacy.warp.item.ExpirationItem(warpData))
                .addIngredient('P', new fr.fuzeblocks.homeplugin.gui.legacy.warp.item.PublicItem(warpData))
                .addIngredient('A', new fr.fuzeblocks.homeplugin.gui.legacy.warp.item.PermissionItem(warpData))
                .addIngredient('I', new fr.fuzeblocks.homeplugin.gui.legacy.warp.item.IconItem(warpData))
                .addIngredient('C', new fr.fuzeblocks.homeplugin.gui.legacy.warp.item.CostItem(warpData))
                .addIngredient('N', new fr.fuzeblocks.homeplugin.gui.legacy.warp.item.RenameItem(warpData))
                .addIngredient('S', new fr.fuzeblocks.homeplugin.gui.legacy.warp.item.LoreItem(warpData))
                .addIngredient('L', new fr.fuzeblocks.homeplugin.gui.legacy.warp.item.LocationItem(warpData))
                .addIngredient('B', new fr.fuzeblocks.homeplugin.gui.legacy.warp.item.BlackListItem(warpData))


                .build();

        Window window = Window.single()
                .setViewer(player)
                .setTitle(languageManager.getStringWithColor(WARP_LIST + "Warp-modify-header", "&6&lGestion des Warps"))
                .setGui(gui)
                .build();

        window.open();
    }

    @Override
    public void openCostWarpGUI(Player player, WarpData warpData) {
        Item border = new SimpleItem(new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayName(""));
        Gui gui = Gui.normal()
                .setStructure(
                        "# # # # # # # # #",
                        "# x + x N x - x #",
                        "# # # # # # # # #",
                        "# # # # # # # # #")
                .addIngredient('x', Markers.CONTENT_LIST_SLOT_HORIZONTAL)
                .addIngredient('#', border)
                .addIngredient('+', new fr.fuzeblocks.homeplugin.gui.legacy.warp.item.IncreaseCostItem(warpData))
                .addIngredient('-', new fr.fuzeblocks.homeplugin.gui.legacy.warp.item.DecreaseCostItem(warpData))
                .addIngredient('N', new SimpleItem(new ItemBuilder(Material.EMERALD).setDisplayName(languageManager.getStringWithColor(WARP_LIST + "Warp-modify-cost-current", "&aCoût actuel: &e%cost%").replace("%cost%", String.valueOf(warpData.getCost())))))
                .build();

        Window window = Window.single()
                .setViewer(player)
                .setTitle(languageManager.getStringWithColor(WARP_LIST + "Warp-modify-cost-header", "&6&lDéfinir le coût du Warp"))
                .setGui(gui)
                .build();

        window.open();
    }

    @Override
    public void openChangeIconWarpGUI(Player player, WarpData warpData) {
        Item border = new SimpleItem(new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayName(""));
        Gui gui = PagedGui.items()
                .setStructure(
                        "# # # # # # # # #",
                        "# x x x x x x x #",
                        "# x x x x x x x #",
                        "# # # < # > # # #")
                .addIngredient('x', Markers.CONTENT_LIST_SLOT_HORIZONTAL)
                .addIngredient('#', border)
                .addIngredient('<', new fr.fuzeblocks.homeplugin.gui.legacy.BackItem())
                .addIngredient('>', new fr.fuzeblocks.homeplugin.gui.legacy.ForwardItem())
                .setContent(Arrays.stream(Material.values()).filter(LegacyGuiBridge::isMaterialValidForIcon).map(mat -> new fr.fuzeblocks.homeplugin.gui.legacy.warp.item.IconsItem(mat, warpData)).collect(Collectors.toList()))
                .build();

        Window window = Window.single()
                .setViewer(player)
                .setTitle(languageManager.getStringWithColor(WARP_LIST + "Warp-modify-icon-header", "&6&lChanger l'icône du Warp"))
                .setGui(gui)
                .build();

        window.open();
    }

    @Override
    public void openHomeGui(Player player) {
        Item border = new SimpleItem(new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayName(""));

        List<Item> homeItems = getHomeItems(player);

        if (homeItems.isEmpty()) {
            player.sendMessage(HomePlugin.getLanguageManager().getStringWithColor(HOME + "Have-no-home"));
            return;
        }

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
                .setContent(homeItems)
                .build();

        Window window = Window.single()
                .setViewer(player)
                .setTitle(HomePlugin.getLanguageManager().getStringWithColor(HOME + "Home-gui-title")
                        .replace("%player%", player.getName()))
                .setGui(gui)
                .build();

        window.open();
    }
    @Override
    public void openDeleteHome(Player player, String homeName) {

    }
    @Override
    public void openDeleteWarp(Player player, WarpData warpData) {

    }

    /**
     * Gets the list of home items for the GUI.
     *
     * @param player The player
     * @return List of home items
     */
    private List<Item> getHomeItems(Player player) {
        HomeManager homeManager = HomePlugin.getHomeManager();
        List<String> homeNames = homeManager.getHomesName(player);

        return homeNames.stream()
                .filter(name -> name != null)
                .map(HomeItem::new)
                .collect(Collectors.toList());
    }

}