package fr.fuzeblocks.homeplugin.gui;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.home.HomeManager;
import fr.fuzeblocks.homeplugin.status.StatusManager;
import fr.fuzeblocks.homeplugin.task.TaskManager;
import fr.fuzeblocks.homeplugin.task.TeleportationManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.AbstractItem;

import java.util.Arrays;

import static fr.fuzeblocks.homeplugin.task.TaskSaveUtils.setTaskManagerInstance;

public class HomeItem extends AbstractItem {

    private final String homeName;
    private final String HOME = "Home.";
    private boolean deleted = false;


    public HomeItem(String homeName) {
        this.homeName = homeName;
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent inventoryClickEvent) {
            HomeManager homeManager = HomePlugin.getHomeManager();

            if (homeManager.exist(player,homeName)) {
                if (clickType.isRightClick()) {
                    player.sendMessage(HomePlugin.getLanguageManager().getStringWithColor(HOME + "Home-deleted-with-name").replace("{homeName}", homeName));
                    // Logic to delete the home goes here
                    homeManager.deleteHome(player, homeName);
                    this.deleted = true;
                    notifyWindows();
                } else {
                    // Logic to teleport the player to the home goes here
                    player.closeInventory();
                    TeleportationManager.teleportPlayerToHome(player, homeName);


                }
            } else {
                player.sendMessage(HomePlugin.getLanguageManager().getStringWithColor(HOME + "Home-does-not-exist"));
                player.closeInventory();
            }
    }


    @Override
    public ItemProvider getItemProvider() {
        if (deleted) {
            return new ItemBuilder(Material.AIR);
        } else {
            String displayNameTemplate = HomePlugin.getLanguageManager().getStringWithColor(HOME + "Home-item-displayname");
            String displayName = displayNameTemplate.replace("{homeName}", homeName);

            String loreTeleport = HomePlugin.getLanguageManager().getStringWithColor(HOME + "Home-item-lore-teleport");
            String loreDelete = HomePlugin.getLanguageManager().getStringWithColor(HOME + "Home-item-lore-delete");

            return new ItemBuilder(getRandomBedColor())
                    .setDisplayName(displayName)
                    .setLegacyLore(Arrays.asList(loreTeleport, loreDelete));
        }
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
