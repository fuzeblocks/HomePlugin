package fr.fuzeblocks.homeplugin.gui;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class HomeGUI extends GUI  {
    private Inventory inventory;
    private int page = 1;
    private final List<String> loadedHomes = new ArrayList<>();
    private final List<String> homes;

    public HomeGUI(List<String> homes) {
        super("Homes", 27, false, false, true);
        inventory = super.getInventory();
        this.homes = homes;
        loadHomes(getHomesPage());
    }


    public void handleNavigation(boolean nextPage) {
        if (nextPage) {
            refreshGUI(page + 1);
        } else {
            refreshGUI(page - 1);
        }
    }
    public Inventory getInventory() {
        return inventory;
    }

    private int getMaxIndex() {
        return 25;
    }
    private void refreshGUI(int i) {
        page = i;
        loadHomes(getHomesPage());
    }
    private void loadHomes(List<String> homes) {
        inventory.clear();
        loadedHomes.clear();

        int index = 0;
        for (String home : homes) {
            if (index < 25) {
                inventory.setItem(index, getIcon(Material.BLUE_BED, "§l§a" + home));
                loadedHomes.add(home);
                index++;
            } else {
                break;
            }
        }

        if (page > 1) {
            inventory.setItem(18, getIcon(Material.ARROW, "Previous Page"));
        }
        if (getStartIndex(page + 1) < this.homes.size()) {
            inventory.setItem(26, getIcon(Material.ARROW, "Next Page"));
        }
    }



    private int getStartIndex(int page) {
        return (page - 1) * getMaxIndex();
    }

    private List<String> getHomesPage() {
        List<String> homesPage = new ArrayList<>();
        int start = getStartIndex(page);
        int end = Math.min(start + getMaxIndex(), homes.size());

        for (int i = start; i < end; i++) {
            homesPage.add(homes.get(i));
        }
        return homesPage;
    }

    private ItemStack getIcon(Material material, String name) {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta != null) {
            itemMeta.setDisplayName(name);
            itemStack.setItemMeta(itemMeta);
        }
        return itemStack;
    }
}
