package fr.fuzeblocks.homeplugin.gui;

import fr.fuzeblocks.homeplugin.core.warps.WarpData;
import org.bukkit.entity.Player;

import java.util.List;

public interface GuiBridge {
    List<String> supportedVersions();
    void openWarpListGUI(Player player);

    void openEditWarpGUI(Player player);

    void openOptionsWarpGUI(Player player, WarpData warpData);

    void openCostWarpGUI(Player player, WarpData warpData);

    void openChangeIconWarpGUI(Player player, WarpData warpData);

    void openHomeGui(Player player);
    void openDeleteHome(Player player, String homeName);
    void openDeleteWarp(Player player, WarpData warpData);
}