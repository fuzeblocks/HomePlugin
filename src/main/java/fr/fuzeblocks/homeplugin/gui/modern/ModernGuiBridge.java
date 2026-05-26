package fr.fuzeblocks.homeplugin.gui.modern;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.core.economy.EconomyManager;
import fr.fuzeblocks.homeplugin.core.home.HomeManager;
import fr.fuzeblocks.homeplugin.core.language.LanguageManager;
import fr.fuzeblocks.homeplugin.core.task.TeleportationManager;
import fr.fuzeblocks.homeplugin.core.warps.WarpData;
import fr.fuzeblocks.homeplugin.core.warps.input.Input;
import fr.fuzeblocks.homeplugin.core.warps.input.InputsSession;
import fr.fuzeblocks.homeplugin.gui.GuiBridge;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import xyz.xenondevs.invui.Click;
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
import java.util.concurrent.atomic.AtomicBoolean;
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

    private static Material getRandomBedColor() {
        Material[] bedColors = {
                Material.RED_BED, Material.GREEN_BED, Material.BLUE_BED,
                Material.YELLOW_BED, Material.PINK_BED, Material.BLACK_BED,
                Material.WHITE_BED, Material.ORANGE_BED, Material.CYAN_BED
        };
        return bedColors[(int) (Math.random() * bedColors.length)];
    }

    @Override
    public void openWarpListGUI(Player player) {
        List<Item> warpItems = getWarpListItems();
        if (warpItems.isEmpty()) {
            player.sendMessage(LANGUAGE_MANAGER.getStringWithColor(WARP_LIST + "No-warps", "&cAucun warp n'est défini pour le moment."));
            return;
        }
        AtomicReference<PagedGui<Item>> guiRef = new AtomicReference<>();
        PagedGui<Item> gui = PagedGui.itemsBuilder()
                .setStructure(
                        "# # # # # # # # #",
                        "# x x x x x x x #",
                        "# x x x x x x x #",
                        "# # # < # > # # #")
                .addIngredient('x', Markers.CONTENT_LIST_SLOT_HORIZONTAL)
                .addIngredient('#', borderItem())
                .addIngredient('<', backButton(guiRef))
                .addIngredient('>', forwardButton(guiRef))
                .setContent(warpItems)
                .build();
        guiRef.set(gui);
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
        PagedGui<Item> gui = PagedGui.itemsBuilder()
                .setStructure(
                        "# # # # # # # # #",
                        "# x x x x x x x #",
                        "# x x x x x x x #",
                        "# # # < # > # # #")
                .addIngredient('x', Markers.CONTENT_LIST_SLOT_HORIZONTAL)
                .addIngredient('#', borderItem())
                .addIngredient('<', backButton(guiRef))
                .addIngredient('>', forwardButton(guiRef))
                .setContent(warpItems)
                .build();
        guiRef.set(gui);
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
                .addIngredient('x', borderItem())
                .addIngredient('D', deleteWarpItem(warpData))
                .addIngredient('E', expirationItem(warpData))
                .addIngredient('P', publicItem(warpData))
                .addIngredient('A', permissionItem(warpData))
                .addIngredient('I', iconItem(warpData))
                .addIngredient('C', costItem(warpData))
                .addIngredient('N', renameItem(warpData))
                .addIngredient('S', loreItem(warpData))
                .addIngredient('L', locationItem(warpData))
                .addIngredient('B', blacklistItem(warpData))
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
                .addIngredient('x', borderItem())
                .addIngredient('+', increaseCostItem(warpData))
                .addIngredient('-', decreaseCostItem(warpData))
                .addIngredient('N', currentCostItem(warpData))
                .build();
        openWindow(player, gui, component(LANGUAGE_MANAGER.getStringWithColor(WARP_LIST + "Warp-modify-cost-header", "&6&lDéfinir le coût du Warp")));
    }

    @Override
    public void openChangeIconWarpGUI(Player player, WarpData warpData) {
        List<Item> iconItems = Arrays.stream(Material.values())
                .filter(ModernGuiBridge::isMaterialValidForIcon)
                .map(material -> iconChoiceItem(material, warpData))
                .collect(Collectors.toList());
        AtomicReference<PagedGui<Item>> guiRef = new AtomicReference<>();
        PagedGui<Item> gui = PagedGui.itemsBuilder()
                .setStructure(
                        "# # # # # # # # #",
                        "# x x x x x x x #",
                        "# x x x x x x x #",
                        "# # # < # > # # #")
                .addIngredient('x', Markers.CONTENT_LIST_SLOT_HORIZONTAL)
                .addIngredient('#', borderItem())
                .addIngredient('<', backButton(guiRef))
                .addIngredient('>', forwardButton(guiRef))
                .setContent(iconItems)
                .build();
        guiRef.set(gui);
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
        PagedGui<Item> gui = PagedGui.itemsBuilder()
                .setStructure(
                        "# # # # # # # # #",
                        "# x x x x x x x #",
                        "# x x x x x x x #",
                        "# # # < # > # # #")
                .addIngredient('x', Markers.CONTENT_LIST_SLOT_HORIZONTAL)
                .addIngredient('#', borderItem())
                .addIngredient('<', backButton(guiRef))
                .addIngredient('>', forwardButton(guiRef))
                .setContent(homeItems)
                .build();
        guiRef.set(gui);
        openWindow(player, gui, component(LANGUAGE_MANAGER.getStringWithColor(HOME + "Home-gui-title").replace("%player%", player.getName())));
    }

    private List<Item> getHomeItems(Player player) {
        HomeManager homeManager = HomePlugin.getHomeManager();
        return homeManager.getHomesName(player).stream()
                .filter(Objects::nonNull)
                .map(this::createHomeItem)
                .collect(Collectors.toList());
    }

    private List<Item> getWarpListItems() {
        Map<String, WarpData> warps = HomePlugin.getWarpManager().getAllWarps();
        return warps.entrySet().stream()
                .filter(entry -> entry.getKey() != null && entry.getValue() != null)
                .map(entry -> createWarpListItem(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    private List<Item> getWarpModifyItems() {
        Map<String, WarpData> warps = HomePlugin.getWarpManager().getAllWarps();
        return warps.entrySet().stream()
                .filter(entry -> entry.getKey() != null && entry.getValue() != null)
                .map(entry -> createWarpModifyItem(entry.getValue()))
                .collect(Collectors.toList());
    }

    private Item createHomeItem(String homeName) {
        AtomicBoolean deleted = new AtomicBoolean(false);
        return Item.builder()
                .setItemProvider(player -> {
                    if (deleted.get()) {
                        return new ItemBuilder(Material.AIR);
                    }
                    String displayName = LANGUAGE_MANAGER.getStringWithColor(HOME + "Home-item-displayname").replace("{homeName}", homeName);
                    String loreTeleport = LANGUAGE_MANAGER.getStringWithColor(HOME + "Home-item-lore-teleport");
                    String loreDelete = LANGUAGE_MANAGER.getStringWithColor(HOME + "Home-item-lore-delete");
                    return new ItemBuilder(getRandomBedColor())
                            .setName(component(displayName))
                            .setLore(Arrays.asList(component(loreTeleport), component(loreDelete)));
                })
                .addClickHandler((item, click) -> {
                    Player clickedPlayer = click.player();
                    HomeManager homeManager = HomePlugin.getHomeManager();
                    if (!homeManager.exist(clickedPlayer, homeName)) {
                        clickedPlayer.sendMessage(LANGUAGE_MANAGER.getStringWithColor(HOME + "Home-does-not-exist"));
                        clickedPlayer.closeInventory();
                        return;
                    }
                    if (click.clickType().isRightClick()) {
                        clickedPlayer.sendMessage(LANGUAGE_MANAGER.getStringWithColor(HOME + "Home-deleted-with-name").replace("{homeName}", homeName));
                        homeManager.deleteHome(clickedPlayer, homeName);
                        deleted.set(true);
                        item.notifyWindows();
                        return;
                    }
                    clickedPlayer.closeInventory();
                    double cost = EconomyManager.getHomeTeleportPrice();
                    if (cost > 0 && !EconomyManager.pay(clickedPlayer, cost)) {
                        clickedPlayer.sendMessage(LANGUAGE_MANAGER.getStringWithColor("Language.Not-Enough-Money"));
                    } else {
                        TeleportationManager.teleportPlayerToHome(clickedPlayer, homeName);
                    }
                })
                .build();
    }

    private Item createWarpListItem(String warpName, WarpData warpData) {
        return Item.builder()
                .setItemProvider(player -> {
                    Location location = warpData.getLocation();
                    String world = location.getWorld() != null ? location.getWorld().getName() : "Unknown";
                    String displayName = LANGUAGE_MANAGER.getStringWithColor(WARP_LIST + "Warp-name", "&eNom du warp : %warp%").replace("%warp%", warpName);
                    String locationLine = LANGUAGE_MANAGER.getStringWithColor(WARP_LIST + "Warp-location", "&9Position : &6X: %x% Y: %y% Z: %z% Monde: %world%")
                            .replace("%x%", String.valueOf(location.getBlockX()))
                            .replace("%y%", String.valueOf(location.getBlockY()))
                            .replace("%z%", String.valueOf(location.getBlockZ()))
                            .replace("%world%", world);
                    return new ItemBuilder(Material.RED_BED)
                            .setName(component(displayName))
                            .setLore(Arrays.asList(
                                    component(locationLine),
                                    component(LANGUAGE_MANAGER.getStringWithColor(WARP_LIST + "Warp-teleport-hover", "&eCliquez pour vous teleporter."))));
                })
                .addClickHandler((item, click) -> TeleportationManager.teleportPlayerToWarp(click.player(), warpName))
                .build();
    }

    private Item createWarpModifyItem(WarpData warpData) {
        return Item.builder()
                .setItemProvider(player -> new ItemBuilder(WarpData.toItemStackUsingLanguage(warpData, LANGUAGE_MANAGER)))
                .addClickHandler((item, click) -> HomePlugin.getGuiManager().openOptionsWarpGUI(click.player(), warpData))
                .build();
    }

    private Item deleteWarpItem(WarpData warpData) {
        return actionItem(Material.BARRIER,
                LANGUAGE_MANAGER.getStringWithColor("Warp.Modify.Delete-item-name", "&cSupprimer le warp"),
                click -> {
                    HomePlugin.getWarpManager().deleteWarp(warpData);
                    click.player().closeInventory();
                });
    }

    private Item expirationItem(WarpData warpData) {
        return inputActionItem(Material.CLOCK,
                "Warp.Modify.Expiration-item-name",
                "&eChanger la duree d'expiration du warp",
                InputsSession.EXPIRATION,
                warpData,
                "Warp.Modify.Expiration-item-message",
                "&eVeuillez entrer la nouvelle duree d'expiration du warp dans le chat avec pour format s,h,d,m (ex 1d,2m,3d,4s) . (cancel pour annuler ou never pour ne jamais faire expirer)");
    }

    private Item publicItem(WarpData warpData) {
        return actionItem(Material.END_PORTAL_FRAME,
                LANGUAGE_MANAGER.getStringWithColor("Warp.Modify.Public-item-name", "&aChanger la visibilite du warp"),
                click -> {
                    HomePlugin.getWarpManager().setWarpPublic(warpData, !warpData.isPublic());
                    click.player().playSound(click.player().getLocation(), org.bukkit.Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f);
                    HomePlugin.getGuiManager().openOptionsWarpGUI(click.player(), HomePlugin.getWarpManager().getWarp(warpData.getName()));
                });
    }

    private Item permissionItem(WarpData warpData) {
        return inputActionItem(Material.REDSTONE_BLOCK,
                "Warp.Modify.Permission-item-name",
                "&eChanger la permission du warp",
                InputsSession.PERMISSION,
                warpData,
                "Warp.Modify.Permission-item-message",
                "&eVeuillez entrer la nouvelle permission du warp dans le chat. (none pour aucune permission, cancel pour annuler)");
    }

    private Item iconItem(WarpData warpData) {
        return actionItem(Material.ITEM_FRAME,
                LANGUAGE_MANAGER.getStringWithColor("Warp.Modify.Icon-item-name", "&eChanger l'icone du warp"),
                click -> HomePlugin.getGuiManager().openChangeIconWarpGUI(click.player(), warpData));
    }

    private Item costItem(WarpData warpData) {
        return actionItem(Material.EMERALD,
                LANGUAGE_MANAGER.getStringWithColor("Warp.Modify.Cost-item-name", "&aDefinir le cout du warp"),
                click -> HomePlugin.getGuiManager().openCostWarpGUI(click.player(), warpData));
    }

    private Item renameItem(WarpData warpData) {
        return inputActionItem(Material.ACACIA_SIGN,
                "Warp.Modify.Rename-item-name",
                "&eRenommer le warp",
                InputsSession.NAME,
                warpData,
                "Warp.Modify.Rename-item-message",
                "&eVeuillez entrer le nouveau nom du warp dans le chat. (cancel pour annuler)");
    }

    private Item loreItem(WarpData warpData) {
        return inputActionItem(Material.PAINTING,
                "Warp.Modify.Lore-item-name",
                "&eRenommer la lore du warp",
                InputsSession.LORE,
                warpData,
                "Warp.Modify.Lore-item-message",
                "&eVeuillez entrer la nouvelle lore du warp dans le chat. (cancel pour annuler)");
    }

    private Item locationItem(WarpData warpData) {
        return inputActionItem(Material.COMPASS,
                "Warp.Modify.Location-item-name",
                "&eDefinir la location du warp",
                InputsSession.LOCATION,
                warpData,
                "Warp.Modify.Location-item-message",
                "&eVeuillez entrer la location du warp dans le chat. (Format: x=100 y=64 z=200 ou 'here')");
    }

    private Item blacklistItem(WarpData warpData) {
        return inputActionItem(Material.ENDER_PEARL,
                "Warp.Modify.BlackList-item-name",
                "&eGerer la blacklist du warp",
                InputsSession.BLACKLIST,
                warpData,
                "Warp.Modify.BlackList-item-message",
                "&eVeuillez entrer le nom du joueur a ajouter ou retirer de la blacklist du warp dans le chat. (cancel pour annuler)");
    }

    private Item iconChoiceItem(Material material, WarpData warpData) {
        return actionItem(material,
                material.name().replace('_', ' '),
                click -> {
                    HomePlugin.getWarpManager().setWarpIcon(warpData, material);
                    HomePlugin.getGuiManager().openOptionsWarpGUI(click.player(), HomePlugin.getWarpManager().getWarp(warpData.getName()));
                });
    }

    private Item currentCostItem(WarpData warpData) {
        return Item.simple(new ItemBuilder(Material.EMERALD)
                .setName(component(LANGUAGE_MANAGER.getStringWithColor(WARP_LIST + "Warp-modify-cost-current", "&aCout actuel: &e%cost%").replace("%cost%", String.valueOf(warpData.getCost())))));
    }

    private Item increaseCostItem(WarpData warpData) {
        return actionItem(Material.DIAMOND,
                LANGUAGE_MANAGER.getStringWithColor("Warp.Modify.Increase-Cost-item-name", "&aAugmenter le cout du warp"),
                click -> {
                    double cost = warpData.getCost();
                    HomePlugin.getWarpManager().setWarpCost(warpData, cost + 1);
                    click.player().playSound(click.player().getLocation(), org.bukkit.Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
                    HomePlugin.getGuiManager().openCostWarpGUI(click.player(), HomePlugin.getWarpManager().getWarp(warpData.getName()));
                });
    }

    private Item decreaseCostItem(WarpData warpData) {
        return actionItem(Material.REDSTONE_BLOCK,
                LANGUAGE_MANAGER.getStringWithColor("Warp.Modify.Decrease-cost-item-name", "&cDiminuer le cout du warp"),
                click -> {
                    double currentCost = warpData.getCost();
                    if (currentCost > 0.0) {
                        HomePlugin.getWarpManager().setWarpCost(warpData, currentCost - 1);
                        click.player().playSound(click.player().getLocation(), org.bukkit.Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
                        HomePlugin.getGuiManager().openCostWarpGUI(click.player(), HomePlugin.getWarpManager().getWarp(warpData.getName()));
                    }
                });
    }

    private Item inputActionItem(Material material, String nameKey, String defaultName, InputsSession session, WarpData warpData, String messageKey, String defaultMessage) {
        return actionItem(material, LANGUAGE_MANAGER.getStringWithColor(nameKey, defaultName), click -> {
            Player clickedPlayer = click.player();
            HomePlugin.getInputsManager().createInputsForPlayer(clickedPlayer.getUniqueId(), new Input(clickedPlayer.getUniqueId(), session, warpData));
            clickedPlayer.closeInventory();
            clickedPlayer.sendMessage(LANGUAGE_MANAGER.getStringWithColor(messageKey, defaultMessage));
        });
    }

    private Item actionItem(Material material, String displayName, java.util.function.Consumer<Click> action) {
        return Item.builder()
                .setItemProvider(player -> new ItemBuilder(material).setName(component(displayName)))
                .addClickHandler((item, click) -> action.accept(click))
                .build();
    }

    private Item borderItem() {
        return Item.simple(new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setName(component("")));
    }

    private Item backButton(AtomicReference<PagedGui<Item>> guiRef) {
        return Item.builder()
                .setItemProvider(player -> {
                    PagedGui<Item> gui = guiRef.get();
                    boolean hasPrevious = gui != null && gui.getPage() > 0;
                    String display = LANGUAGE_MANAGER.getStringWithColor("Language.Gui.BackItem.Name", "&cPage precedente");
                    String lore = hasPrevious
                            ? LANGUAGE_MANAGER.getStringWithColor("Language.Gui.BackItem.ChangePage", "Aller a la page %current_page% / %total_pages%")
                              .replace("%current_page%", String.valueOf(gui.getPage() + 1))
                              .replace("%total_pages%", String.valueOf(gui.getPageCount()))
                            : LANGUAGE_MANAGER.getStringWithColor("Language.Gui.BackItem.MaxBack", "&cVous ne pouvez pas revenir plus loin !");
                    return new ItemBuilder(Material.RED_STAINED_GLASS_PANE)
                            .setName(component(display))
                            .setLore(Arrays.asList(component(lore)));
                })
                .addClickHandler((item, click) -> {
                    PagedGui<Item> gui = guiRef.get();
                    if (gui != null && gui.getPage() > 0) {
                        gui.setPage(gui.getPage() - 1);
                    }
                })
                .build();
    }

    private Item forwardButton(AtomicReference<PagedGui<Item>> guiRef) {
        return Item.builder()
                .setItemProvider(player -> {
                    PagedGui<Item> gui = guiRef.get();
                    boolean hasNext = gui != null && gui.getPage() < gui.getPageCount() - 1;
                    String display = LANGUAGE_MANAGER.getStringWithColor("Language.Gui.ForwardItem.Name", "&aPage suivante");
                    String lore = hasNext
                            ? LANGUAGE_MANAGER.getStringWithColor("Language.Gui.ForwardItem.ChangePage", "Aller a la page %current_page% / %total_pages%")
                              .replace("%current_page%", String.valueOf(gui.getPage() + 2))
                              .replace("%total_pages%", String.valueOf(gui.getPageCount()))
                            : LANGUAGE_MANAGER.getStringWithColor("Language.Gui.ForwardItem.MaxForward", "&cVous etes deja a la derniere page !");
                    return new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE)
                            .setName(component(display))
                            .setLore(Arrays.asList(component(lore)));
                })
                .addClickHandler((item, click) -> {
                    PagedGui<Item> gui = guiRef.get();
                    if (gui != null && gui.getPage() < gui.getPageCount() - 1) {
                        gui.setPage(gui.getPage() + 1);
                    }
                })
                .build();
    }

    private void openWindow(Player player, Gui gui, Component title) {
        Window.builder()
                .setViewer(player)
                .setTitle(title)
                .setUpperGui(gui)
                .build()
                .open();
    }

    private Component component(String text) {
        return LEGACY.deserialize(text == null ? "" : text);
    }
}

