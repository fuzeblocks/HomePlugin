package fr.fuzeblocks.homeplugin.gui.modern;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.core.home.HomeManager;
import fr.fuzeblocks.homeplugin.core.language.LanguageManager;
import fr.fuzeblocks.homeplugin.core.warps.WarpData;
import fr.fuzeblocks.homeplugin.gui.GuiBridge;
import fr.fuzeblocks.homeplugin.gui.modern.warp.item.*;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.gui.Markers;
import xyz.xenondevs.invui.gui.PagedGui;
import xyz.xenondevs.invui.item.Item;
import xyz.xenondevs.invui.item.ItemBuilder;
import xyz.xenondevs.invui.window.Window;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * InvUI v2 implementation of the GUI bridge.
 * Uses the 2.x builder API, Component titles, and modern click handling.
 */
public final class ModernGuiBridge implements GuiBridge {
    private static final String WARP_LIST = "Warp.List.";
    private static final String HOME = "Home.";
    private static final LanguageManager LANGUAGE_MANAGER = HomePlugin.getLanguageManager();
    private static final LegacyComponentSerializer LEGACY = LegacyComponentSerializer.legacyAmpersand();

    private static boolean isMaterialValidForIcon(Material material) {
        String name = material.name();
        return !name.equals("AIR") && !name.endsWith("_AIR");
    }


    @Override
    public void openWarpListGUI(Player player) {
        List<Item> warpItems = getWarpListItems();
        if (warpItems.isEmpty()) {
            player.sendMessage(LANGUAGE_MANAGER.getStringWithColor(WARP_LIST + "No-warps", "&cAucun warp n'est défini pour le moment."));
            return;
        }
        AtomicReference<PagedGui<Item>> guiRef = new AtomicReference<>();
        BackItem backItem = new BackItem();
        ForwardItem forwardItem = new ForwardItem();
        PagedGui<Item> gui = PagedGui.itemsBuilder()
                .setStructure(
                        "# # # # # # # # #",
                        "# x x x x x x x #",
                        "# x x x x x x x #",
                        "# # # < # > # # #")
                .addIngredient('x', Markers.CONTENT_LIST_SLOT_HORIZONTAL)
                .addIngredient('#', borderItem())
                .addIngredient('<', backItem)
                .addIngredient('>', forwardItem)
                .setContent(warpItems)
                .build();
        guiRef.set(gui);
        backItem.setGui(gui);
        forwardItem.setGui(gui);
        openWindow(player, gui, component(LANGUAGE_MANAGER.getStringWithColor(WARP_LIST + "Warp-list-header", "&6&lListe des Warps")));
    }

    @Override
    public void openEditWarpGUI(Player player) {
        List<Item> warpItems = getWarpModifyItems();
        if (warpItems.isEmpty()) {
            player.sendMessage(LANGUAGE_MANAGER.getStringWithColor(WARP_LIST + "No-warps", "&cAucun warp n'est défini pour le moment."));
            return;
        }
        AtomicReference<PagedGui<Item>> guiRef = new AtomicReference<>();
        BackItem backItem = new BackItem();
        ForwardItem forwardItem = new ForwardItem();
        PagedGui<Item> gui = PagedGui.itemsBuilder()
                .setStructure(
                        "# # # # # # # # #",
                        "# x x x x x x x #",
                        "# x x x x x x x #",
                        "# # # < # > # # #")
                .addIngredient('x', Markers.CONTENT_LIST_SLOT_HORIZONTAL)
                .addIngredient('#', borderItem())
                .addIngredient('<', backItem)
                .addIngredient('>', forwardItem)
                .setContent(warpItems)
                .build();
        guiRef.set(gui);
        backItem.setGui(gui);
        forwardItem.setGui(gui);
        openWindow(player, gui, component(LANGUAGE_MANAGER.getStringWithColor(WARP_LIST + "Warp-modify-header", "&6&lGestion des Warps")));
    }

    @Override
    public void openOptionsWarpGUI(Player player, WarpData warpData) {
        Gui gui = Gui.builder()
                .setStructure(
                        "# # # # # # # # #",
                        "# D E P A I C N #",
                        "# S L B x x x x #",
                        "# # # # # # # # #")
                .addIngredient('#', borderItem())
                .addIngredient('x', Markers.CONTENT_LIST_SLOT_HORIZONTAL)
                .addIngredient('D', new DeleteItem(warpData))
                .addIngredient('E', new ExpirationItem(warpData))
                .addIngredient('P', new PublicItem(warpData))
                .addIngredient('A', new PermissionItem(warpData))
                .addIngredient('I', new IconItem(warpData))
                .addIngredient('C', new CostItem(warpData))
                .addIngredient('N', new RenameItem(warpData))
                .addIngredient('S', new LoreItem(warpData))
                .addIngredient('L', new LocationItem(warpData))
                .addIngredient('B', new BlackListItem(warpData))
                .build();
        openWindow(player, gui, component(LANGUAGE_MANAGER.getStringWithColor(WARP_LIST + "Warp-modify-header", "&6&lGestion des Warps")));
    }

    @Override
    public void openCostWarpGUI(Player player, WarpData warpData) {
        Gui gui = Gui.builder()
                .setStructure(
                        "# # # # # # # # #",
                        "# x + x N x - x #",
                        "# # # # # # # # #",
                        "# # # # # # # # #")
                .addIngredient('#', borderItem())
                .addIngredient('x', Markers.CONTENT_LIST_SLOT_HORIZONTAL)
                .addIngredient('+', new IncreaseCostItem(warpData))
                .addIngredient('-', new DecreaseCostItem(warpData))
                .addIngredient('N', currentCostItem(warpData))
                .build();
        openWindow(player, gui, component(LANGUAGE_MANAGER.getStringWithColor(WARP_LIST + "Warp-modify-cost-header", "&6&lDéfinir le coût du Warp")));
    }

    @Override
    public void openChangeIconWarpGUI(Player player, WarpData warpData) {

        AtomicReference<PagedGui<Item>> guiRef = new AtomicReference<>();
        BackItem backItem = new BackItem();
        ForwardItem forwardItem = new ForwardItem();
        PagedGui<Item> gui = PagedGui.itemsBuilder()
                .setStructure(
                        "# # # # # # # # #",
                        "# x x x x x x x #",
                        "# x x x x x x x #",
                        "# # # < # > # # #")
                .addIngredient('x', Markers.CONTENT_LIST_SLOT_HORIZONTAL)
                .addIngredient('#', borderItem())
                .addIngredient('<', backItem)
                .addIngredient('>', forwardItem)
                .setContent(Arrays.stream(Material.values()).filter(ModernGuiBridge::isMaterialValidForIcon).map(mat -> new fr.fuzeblocks.homeplugin.gui.modern.warp.item.IconsItem(mat, warpData)).collect(Collectors.toList()))
                .build();
        guiRef.set(gui);
        backItem.setGui(gui);
        forwardItem.setGui(gui);
        openWindow(player, gui, component(LANGUAGE_MANAGER.getStringWithColor(WARP_LIST + "Warp-modify-icon-header", "&6&lChanger l'icône du Warp")));
    }

    @Override
    public void openHomeGui(Player player) {
        List<Item> homeItems = getHomeItems(player);
        if (homeItems.isEmpty()) {
            player.sendMessage(LANGUAGE_MANAGER.getStringWithColor(HOME + "Have-no-home"));
            return;
        }
        AtomicReference<PagedGui<Item>> guiRef = new AtomicReference<>();
        BackItem backItem = new BackItem();
        ForwardItem forwardItem = new ForwardItem();
        PagedGui<Item> gui = PagedGui.itemsBuilder()
                .setStructure(
                        "# # # # # # # # #",
                        "# x x x x x x x #",
                        "# x x x x x x x #",
                        "# # # < # > # # #")
                .addIngredient('x', Markers.CONTENT_LIST_SLOT_HORIZONTAL)
                .addIngredient('#', borderItem())
                .addIngredient('<', backItem)
                .addIngredient('>', forwardItem)
                .setContent(homeItems)
                .build();
        guiRef.set(gui);
        backItem.setGui(gui);
        forwardItem.setGui(gui);
        openWindow(player, gui, component(LANGUAGE_MANAGER.getStringWithColor(HOME + "Home-gui-title").replace("%player%", player.getName())));
    }

    private List<Item> getHomeItems(Player player) {
        HomeManager homeManager = HomePlugin.getHomeManager();
        return homeManager.getHomesName(player).stream()
                .filter(Objects::nonNull)
                .map(HomeItem::new)
                .collect(Collectors.toList());
    }

    private List<Item> getWarpListItems() {
        Map<String, WarpData> warps = HomePlugin.getWarpManager().getAllWarps();
        return warps.entrySet().stream()
                .filter(entry -> entry.getKey() != null && entry.getValue() != null)
                .map(entry -> new WarpListItem(entry.getKey(), LANGUAGE_MANAGER.getStringWithColor(WARP_LIST + "Warp-name", "&eNom du warp : %warp%").replace("%warp%", entry.getValue().getName())))
                .collect(Collectors.toList());
    }

    private List<Item> getWarpModifyItems() {
        Map<String, WarpData> warps = HomePlugin.getWarpManager().getAllWarps();
        return warps.entrySet().stream()
                .filter(entry -> entry.getKey() != null && entry.getValue() != null)
                .map(entry -> new WarpModifyItem(entry.getValue()))
                .collect(Collectors.toList());
    }



    private Item currentCostItem(WarpData warpData) {
        return Item.simple(new ItemBuilder(Material.EMERALD)
                .setName(component(LANGUAGE_MANAGER.getStringWithColor(WARP_LIST + "Warp-modify-cost-current", "&aCout actuel: &e%cost%").replace("%cost%", String.valueOf(warpData.getCost())))));
    }


    private Item borderItem() {
        return Item.simple(new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setName(component("")));
    }



    private void openWindow(Player player, Gui gui, Component title) {
        Window.builder()
                .setViewer(player)
                .setTitle(title)
                .setUpperGui(gui)
                .build()
                .open();
    }

    public static Component component(String text) {
        return LEGACY.deserialize(text == null ? "" : text);
    }


}

