package fr.fuzeblocks.homeplugin.gui;

import fr.fuzeblocks.homeplugin.core.warps.WarpData;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

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

    public List<String> supportedVersions() {
        return bridge.supportedVersions();
    }

    public boolean isGuiSupported() {
        String currentVersion = org.bukkit.Bukkit.getBukkitVersion();
        return isVersionSupported(currentVersion);
    }
    private boolean isVersionSupported(String version) {
        if (version == null || version.isBlank()) {
            return false;
        }

        String cleanVersion = version.toLowerCase(Locale.ROOT)
                .split("[-_]")[0]
                .replaceAll("\\.build\\..*", "");

        List<String> supported = supportedVersions();
        if (supported == null || supported.isEmpty()) {
            return false;
        }

        return supported.stream()
                .filter(Objects::nonNull)
                .anyMatch(sup -> {
                    String target = sup.toLowerCase(Locale.ROOT);
                    if (target.endsWith(".x")) {
                        String prefix = target.substring(0, target.length() - 2);
                        return cleanVersion.equals(prefix) || cleanVersion.startsWith(prefix + ".");
                    }
                    return cleanVersion.equals(target);
                });
    }


}