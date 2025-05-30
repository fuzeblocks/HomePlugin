package fr.fuzeblocks.homeplugin.gui;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.home.HomeManager;
import fr.fuzeblocks.homeplugin.status.StatusManager;
import fr.fuzeblocks.homeplugin.task.TaskManager;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.AbstractItem;

import java.util.Arrays;
import java.util.Collections;

import static fr.fuzeblocks.homeplugin.task.TaskSaveUtils.setTaskManagerInstance;

public class HomeItem extends AbstractItem {

    private final String homeName;
    private final HomePlugin homePlugin;
    private final Player player;
    private final String key = "Language.";
    private boolean deleted = false;


    public HomeItem(String homeName,Player player,HomePlugin homePlugin) {
        this.homeName = homeName;
        this.homePlugin = homePlugin;
        this.player = player;
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent event) {
        event.setCancelled(true);

        HomeManager homeManager = HomePlugin.getHomeManager();

        if (homeManager.exist(player, homeName)) {

            if (clickType.isShiftClick()) {
                HomeManager.homeRenameStatus.put(player, true);
                player.closeInventory();

                new AnvilGUI.Builder()
                        .onClick((slot, stateSnapshot) -> {
                            if (slot != AnvilGUI.Slot.OUTPUT) {
                                return Collections.emptyList();
                            }
                            String text = stateSnapshot.getText();
                            if (text == null || text.isEmpty() || text.length() > 16 || !text.matches("^[a-zA-Z0-9_-]+$")) {
                                return Arrays.asList(AnvilGUI.ResponseAction.replaceInputText(
                                        HomePlugin.getLanguageManager().getStringWithColor(key + "Home-invalid-name")));
                            }

                            if (HomePlugin.getHomeManager().exist(player, text)) {
                                return Arrays.asList(AnvilGUI.ResponseAction.replaceInputText(
                                        HomePlugin.getLanguageManager().getStringWithColor(key + "Home-already-exists")));
                            }

                            HomePlugin.getHomeManager().renameHome(player, homeName, text);
                            player.sendMessage(HomePlugin.getLanguageManager().getStringWithColor(key + "Home-renamed").replace("{newName}", text));
                            this.deleted = true;
                            notifyWindows();

                            return Arrays.asList(AnvilGUI.ResponseAction.close());
                        })
                        .title(HomePlugin.getLanguageManager().getStringWithColor(key + "Home-rename"))
                        .plugin(homePlugin)
                        .text(homeName)
                        .open(player);


            } else if (clickType.isRightClick() && !clickType.isShiftClick()) {
                player.sendMessage(
                        HomePlugin.getLanguageManager().getStringWithColor(key + "Home-deleted-with-name")
                                .replace("{homeName}", homeName)
                );

                homeManager.deleteHome(player, homeName);
                this.deleted = true;
                notifyWindows();

            } else if (clickType.isLeftClick() && !clickType.isShiftClick()) {
                player.closeInventory();
                player.sendMessage(
                        HomePlugin.getLanguageManager().getStringWithColor(key + "Start-of-teleportation") + " " + homeName
                );

                StatusManager.setPlayerStatus(player, true);

                TaskManager taskManager = new TaskManager(homePlugin);
                taskManager.homeTask(homeName, player, homeManager.getHomeLocation(player, homeName));
                taskManager.startTeleportTask();

                setTaskManagerInstance(player, taskManager);
            }

        } else {
            player.sendMessage(HomePlugin.getLanguageManager().getStringWithColor(key + "Home-does-not-exist"));
            player.closeInventory();
        }
    }

    @Override
    public ItemProvider getItemProvider() {
        if (deleted) {
            return new ItemBuilder(Material.AIR);
        }

        String keyPrefix = "HomeGui.";
        var langManager = HomePlugin.getLanguageManager();

        String displayName = langManager.getStringWithColor(keyPrefix + "Home-item-displayname")
                .replace("{homeName}", "§a" + homeName + "§r");

        String loreTeleport = "§b" + langManager.getStringWithColor(keyPrefix + "Home-item-lore-teleport") + "§r";
        String loreDelete = "§c" + langManager.getStringWithColor(keyPrefix + "Home-item-lore-delete") + "§r";
        String loreRename = langManager.getStringWithColor(keyPrefix + "Home-item-lore-rename");

        Location location = HomePlugin.getHomeManager().getHomeLocation(player, homeName);
        String worldName = location.getWorld().getName();

        String coordinates = "§e" + langManager.getStringWithColor(keyPrefix + "Home-world-name")
                .replace("%world%", worldName) + " §3X: " + location.getBlockX()
                + " §3Y: " + location.getBlockY()
                + " §3Z: " + location.getBlockZ() + "§r";

        return new ItemBuilder(getRandomBedColor())
                .setDisplayName(displayName)
                .setLegacyLore(Arrays.asList(loreTeleport, loreDelete, loreRename, coordinates)); // Ajout de loreRename ici
    }



    private Material getRandomBedColor() {
        Material[] bedColors = {
            Material.RED_BED, Material.GREEN_BED, Material.BLUE_BED,
            Material.YELLOW_BED, Material.PINK_BED, Material.BLACK_BED,
            Material.WHITE_BED, Material.ORANGE_BED, Material.CYAN_BED
        };
        return bedColors[(int) (Math.random() * bedColors.length)];
    }

}
