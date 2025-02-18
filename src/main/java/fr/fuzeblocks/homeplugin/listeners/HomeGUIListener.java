package fr.fuzeblocks.homeplugin.listeners;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.home.HomeManager;
import fr.fuzeblocks.homeplugin.task.TaskManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class HomeGUIListener implements Listener {
    @EventHandler
    private void onInteract(InventoryClickEvent event) {
        if (!event.getView().getTitle().equalsIgnoreCase("Homes")) return;

        event.setCancelled(true);

        ItemStack clicked = event.getCurrentItem();
        if (clicked == null || clicked.getType() == Material.AIR) return;

        String itemName = clicked.getItemMeta() != null ? clicked.getItemMeta().getDisplayName() : "";
        if (itemName.equals("Next Page")) {
            handleNavigation(true);
        } else if (itemName.equals("Previous Page")) {
            handleNavigation(false);
        } else if (itemName.startsWith("§l§a")) {
            Player player = (Player) event.getWhoClicked();
            String homeName = itemName.substring(4);
            player.sendMessage("§aYou selected the home: " + homeName);
            TaskManager taskManager = new TaskManager(HomePlugin.getPlugin(HomePlugin.class));
            taskManager.homeTask(homeName,player, HomeManager.getInstance().getHomeLocation(player,homeName));
            taskManager.startTeleportTask();

        }
    }
}
