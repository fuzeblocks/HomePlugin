package fr.fuzeblocks.homeplugin.gui;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

public class GUI {
    private String guiName;
    private int guiSize;
    private boolean canPut;
    private boolean canTake;
    private boolean canInteract;
    private Inventory inventory;

    public GUI(String guiName, int guiSize, boolean canPut, boolean canTake, boolean canInteract) {
        this.guiName = guiName;
        this.guiSize = guiSize;
        this.canPut = canPut;
        this.canTake = canTake;
        this.canInteract = canInteract;
        inventory = Bukkit.createInventory(null,guiSize,guiName);
    }

    public int guiSize() {
        return guiSize;
    }

    public String guiName() {
        return guiName;
    }

    public boolean canPut() {
        return canPut;
    }

    public boolean canTake() {
        return canTake;
    }

    public boolean canInteract() {
        return canInteract;
    }

    public Inventory getInventory() {
        return inventory;
    }
}
