package fr.fuzeblocks.homeplugin.gui;

import fr.fuzeblocks.homeplugin.core.warps.WarpData;
import org.bukkit.entity.Player;

public final class GUIManager {
    private final GuiBridge bridge;

    public GUIManager(GuiBridge bridge) {
        this.bridge = bridge;
    }

    public void openWarpListGUI(Player player) {
        bridge.openWarpListGUI(player);
    }

    public void openEditWarpGUI(Player player) {
        bridge.openEditWarpGUI(player);
    }

    public void openOptionsWarpGUI(Player player, fr.fuzeblocks.homeplugin.core.warps.WarpData warpData) {
        bridge.openOptionsWarpGUI(player, warpData);
    }

    public void openCostWarpGUI(Player player, fr.fuzeblocks.homeplugin.core.warps.WarpData warpData) {
        bridge.openCostWarpGUI(player, warpData);
    }

    public void openChangeIconWarpGUI(Player player, fr.fuzeblocks.homeplugin.core.warps.WarpData warpData) {
        bridge.openChangeIconWarpGUI(player, warpData);
    }


    public void openHomeGui(Player player) {
        bridge.openHomeGui(player);
    }

    public void openDeleteHome(Player player, String homeName) {
        bridge.openDeleteHome(player, homeName);
    }
    public void openDeleteWarp(Player player, WarpData warpName) {
        bridge.openDeleteWarp(player, warpName);
    }


}