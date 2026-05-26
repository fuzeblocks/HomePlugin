package fr.fuzeblocks.homeplugin.gui;

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

    public void openEditHomeGUI(Player player) {
        bridge.openEditWarpGUI(player);
    }

    public void openHomeGui(Player player) {
        bridge.openHomeGui(player);
    }


}