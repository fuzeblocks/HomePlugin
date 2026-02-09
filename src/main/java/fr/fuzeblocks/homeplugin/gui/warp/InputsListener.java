package fr.fuzeblocks.homeplugin.gui.warp;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.warps.WarpData;
import fr.fuzeblocks.homeplugin.warps.WarpManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Collections;
import java.util.UUID;

public class InputsListener implements Listener {

    private final InputsManager inputsManager;
    private final WarpManager warpManager;

    public InputsListener(HomePlugin plugin) {
        this.inputsManager = HomePlugin.getInputsManager();
        this.warpManager = HomePlugin.getWarpManager();
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {

        UUID uuid = event.getPlayer().getUniqueId();
        if (!inputsManager.hasInputsForPlayer(uuid)) return;

        event.setCancelled(true);

        Input input = inputsManager.getInputsForPlayer(uuid);

        Bukkit.getScheduler().runTask(
                HomePlugin.getPlugin(HomePlugin.class),
                () -> handleInput(
                        event.getPlayer(),
                        event.getMessage(),
                        input.getSession(),
                        input.getWarpData(),
                        uuid
                )
        );
    }

    private void handleInput(Player player,
                             String message,
                             InputsSession session,
                             WarpData warpData,
                             UUID uuid) {

        try {
            switch (session) {
                case LOCATION:
                    handleLocationInput(player, message, warpData);
                    break;
                case NAME:
                    handleNameInput(player, message, warpData);
                    break;
                case LORE:
                    handleLoreInput(player, message, warpData);
                    break;
                case PERMISSION:
                    handlePermissionInput(player, message, warpData);
                    break;
                case EXPIRATION:
                    handleExpirationInput(player, message, warpData);
                    break;
            }
        } finally {
            inputsManager.removeInputsForPlayer(uuid);
        }
    }

    /* ---------------- LOCATION ---------------- */

    private void handleLocationInput(Player player, String message, WarpData warpData) {

        if (!isValidLocationInput(message)) {
            player.sendMessage(color("&cEntrée invalide ! Format: x=100 y=64 z=200 ou 'here'."));
            return;
        }

        Location location = parseLocation(player, message, warpData);
        warpManager.relocateWarp(warpData, location);
    }

    private boolean isValidLocationInput(String message) {
        message = message.trim();

        if (message.equalsIgnoreCase("here")) return true;

        String[] parts = message.split(" ");
        if (parts.length != 3) return false;

        for (String part : parts) {
            if (!part.matches("[xyzXYZ]=-?\\d{1,7}")) return false;
        }
        return true;
    }

    private Location parseLocation(Player player, String message, WarpData warpData) {

        if (message.equalsIgnoreCase("here")) {
            return player.getLocation();
        }

        double x = 0, y = 0, z = 0;

        for (String part : message.split(" ")) {
            char axis = Character.toLowerCase(part.charAt(0));
            double value = Double.parseDouble(part.substring(2));

            switch (axis) {
                case 'x': x = value; break;
                case 'y': y = value; break;
                case 'z': z = value; break;
            }
        }

        // Conserve le monde du warp
        return new Location(warpData.getLocation().getWorld(), x, y, z);
    }

    /* ---------------- NAME ---------------- */

    private void handleNameInput(Player player, String message, WarpData warpData) {

        String name = message.trim();

        if (name.equalsIgnoreCase("cancel")) {
            player.sendMessage(color("&cModification annulée !"));
            return;
        }

        if (name.isEmpty() || name.contains(" ")) {
            player.sendMessage(color("&cEntrée invalide ! Nom sans espaces."));
            return;
        }

        warpManager.renameWarp(warpData, name);
    }

    /* ---------------- LORE ---------------- */

    private void handleLoreInput(Player player, String message, WarpData warpData) {

        String lore = message.trim();

        if (lore.equalsIgnoreCase("cancel")) {
            player.sendMessage(color("&cModification annulée !"));
            return;
        }

        // Si ton manager attend une List<String>
        warpManager.setWarpLores(warpData, Collections.singletonList(lore));
    }

    /* ---------------- PERMISSION ---------------- */

    private void handlePermissionInput(Player player, String message, WarpData warpData) {

        String permission = message.trim();

        if (permission.equalsIgnoreCase("cancel")) {
            player.sendMessage(color("&cModification annulée !"));
            return;
        }

        if (permission.equalsIgnoreCase("none")) {
            warpManager.setWarpPermission(warpData, null);
            return;
        }

        if (!permission.matches("[a-zA-Z0-9._-]+")) {
            player.sendMessage(color("&cPermission invalide !"));
            return;
        }

        warpManager.setWarpPermission(warpData, permission);
    }

    /* ---------------- EXPIRATION ---------------- */

    private void handleExpirationInput(Player player, String message, WarpData warpData) {

    String input = message.trim().toLowerCase();

    if (input.equals("cancel")) {
        player.sendMessage(color("&cModification annulée !"));
        return;
    }

    if (input.equals("never")) {
        warpManager.setWarpExpirationDate(warpData, null);
        return;
    }

    long seconds = parseDurationToSeconds(input);
    if (seconds <= 0) {
        player.sendMessage(color("&cFormat invalide ! Exemples: 10m, 2h, 7d, never"));
        return;
    }

    long futureMillis = System.currentTimeMillis() + (seconds * 1000);
    java.sql.Timestamp expiration = new java.sql.Timestamp(futureMillis);

    warpManager.setWarpExpirationDate(warpData, expiration);
}


    private long parseDurationToSeconds(String input) {

        if (!input.matches("\\d+[smhd]")) return -1;

        long value = Long.parseLong(input.substring(0, input.length() - 1));
        char unit = input.charAt(input.length() - 1);

        switch (unit) {
            case 's': return value;
            case 'm': return value * 60;
            case 'h': return value * 3600;
            case 'd': return value * 86400;
            default: return -1;
        }
    }

    /* ---------------- UTILS ---------------- */

    private String color(String msg) {
        return msg.replace("&", "§");
    }
}
