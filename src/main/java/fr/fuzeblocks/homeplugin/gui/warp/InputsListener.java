package fr.fuzeblocks.homeplugin.gui.warp;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.language.LanguageManager;
import fr.fuzeblocks.homeplugin.warps.WarpData;
import fr.fuzeblocks.homeplugin.warps.WarpManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class InputsListener implements Listener {

    private final InputsManager inputsManager;
    private final WarpManager warpManager;
    private final LanguageManager languageManager = HomePlugin.getLanguageManager();
    private final String INPUTS_PREFIX = "Warp.Inputs.";

    public InputsListener() {
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
                case BLACKLIST:
                    handleBlackListInput(player, message, warpData);
                    break;
            }
        } finally {
            inputsManager.removeInputsForPlayer(uuid);
        }
    }

    /* ---------------- LOCATION ---------------- */

    private void handleLocationInput(Player player, String message, WarpData warpData) {

        if (!isValidLocationInput(message)) {
            player.sendMessage(languageManager.getStringWithColor(INPUTS_PREFIX + "LocationInput.InvalidLocationInput","&cEntrée invalide ! Format: x=100 y=64 z=200 ou 'here' ou 'cancel'."));
            return;
        }

        Location location = parseLocation(player, message, warpData);
        warpManager.relocateWarp(warpData, location);
        player.sendMessage(languageManager.getStringWithColor(INPUTS_PREFIX + "LocationInput.SuccessLocationInput","&aLe warp a été déplacé à la nouvelle position !"));
    }

    private boolean isValidLocationInput(String message) {
        message = message.trim();

        if (message.equalsIgnoreCase("here")) return true;

        if (message.equalsIgnoreCase("cancel")) return true;

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
        if (message.equalsIgnoreCase("cancel")) {
            player.sendMessage(languageManager.getStringWithColor(INPUTS_PREFIX + "CancelModification","&cModification annulée !"));
            return warpData.getLocation();
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

        return new Location(warpData.getLocation().getWorld(), x, y, z);
    }

    /* ---------------- NAME ---------------- */

    private void handleNameInput(Player player, String message, WarpData warpData) {

        String name = message.trim();

        if (name.equalsIgnoreCase("cancel") || name.startsWith("/")) {
            player.sendMessage(languageManager.getStringWithColor(INPUTS_PREFIX + "CancelModification","&cModification annulée !"));
            return;
        }

        if (name.isEmpty() || name.contains(" ")) {
            player.sendMessage(languageManager.getStringWithColor(INPUTS_PREFIX + "NameInput.InvalidNameInput","&cEntrée invalide ! Nom sans espaces."));
            return;
        }

        if (warpManager.warpExists(name)) {
            player.sendMessage(languageManager.getStringWithColor(INPUTS_PREFIX + "NameInput.WarpExists","&cUn warp avec ce nom existe déjà !"));
            handleNameInput(player, message, warpData);
            return;
        }

        warpManager.renameWarp(warpData, name);

        String successRename = languageManager.getStringWithColor(INPUTS_PREFIX + "NameInput.SuccessNameInput","&aLe warp a été renommé en &e" + name + " &a!");
        successRename = successRename.replace("{warp}", name).replace("%warp%", name);
        player.sendMessage(successRename);
    }

    /* ---------------- LORE ---------------- */

    private void handleLoreInput(Player player, String message, WarpData warpData) {

        String lore = message.trim();

        if (lore.equalsIgnoreCase("cancel") || lore.startsWith("/")) {
            player.sendMessage(languageManager.getStringWithColor(INPUTS_PREFIX + "CancelModification","&cModification annulée !"));
            return;
        }

        warpManager.setWarpLores(warpData, Collections.singletonList(lore));
        player.sendMessage(languageManager.getStringWithColor(INPUTS_PREFIX + "LoreInput.SuccessLoreInput","&aLa lore du warp a été mise à jour !"));
    }

    /* ---------------- PERMISSION ---------------- */

    private void handlePermissionInput(Player player, String message, WarpData warpData) {

        String permission = message.trim();

        if (permission.equalsIgnoreCase("cancel") || permission.startsWith("/")) {
            player.sendMessage(languageManager.getStringWithColor(INPUTS_PREFIX + "CancelModification","&cModification annulée !"));
            return;
        }

        if (permission.equalsIgnoreCase("none")) {
            warpManager.setWarpPermission(warpData, null);
            player.sendMessage(languageManager.getStringWithColor(INPUTS_PREFIX + "PermissionInput.NonePermissionInput","&aAucune permission ne sera obligatoire pour ce warp !"));
            return;
        }

        if (!permission.matches("[a-zA-Z0-9._-]+")) {
            player.sendMessage(languageManager.getStringWithColor(INPUTS_PREFIX + "PermissionInput.InvalidPermissionInput","&cEntrée invalide ! Veuillez entrer une permission valide ou 'none' pour aucune permission."));
            return;
        }

        warpManager.setWarpPermission(warpData, permission);
        player.sendMessage(languageManager.getStringWithColor(INPUTS_PREFIX + "PermissionInput.SuccessPermissionInput","&aLa permission du warp a été mise à jour !"));
    }

    /* ---------------- EXPIRATION ---------------- */

    private void handleExpirationInput(Player player, String message, WarpData warpData) {

        String input = message.trim().toLowerCase();

        if (input.equals("cancel")) {
            player.sendMessage(languageManager.getStringWithColor(INPUTS_PREFIX + "CancelModification","&cModification annulée !"));
            return;
        }

        if (input.equals("never")) {
            warpManager.setWarpExpirationDate(warpData, null);
            player.sendMessage(languageManager.getStringWithColor(INPUTS_PREFIX + "ExpirationInput.NeverExpirationInput","&aLe warp n'expirera jamais !"));
            return;
        }

        long seconds = parseDurationToSeconds(input);
        if (seconds <= 0) {
            player.sendMessage(languageManager.getStringWithColor(INPUTS_PREFIX + "ExpirationInput.InvalidExpirationInput","&cFormat invalide ! Exemples: 10m, 2h, 7d, never"));
            return;
        }

        long futureMillis = System.currentTimeMillis() + (seconds * 1000);
        java.sql.Timestamp expiration = new java.sql.Timestamp(futureMillis);

        warpManager.setWarpExpirationDate(warpData, expiration);
        player.sendMessage(languageManager.getStringWithColor(INPUTS_PREFIX + "ExpirationInput.SuccessExpirationInput","&aLa durée d'expiration du warp a été mise à jour !"));
    }

    /* ---------------- BLACKLIST---------------- */
    private void handleBlackListInput(Player player, String message, WarpData warpData) {

        String targetName = message.trim();

        if (targetName.equalsIgnoreCase("cancel") || targetName.startsWith("/")) {
            player.sendMessage(languageManager.getStringWithColor(INPUTS_PREFIX + "CancelModification","&cModification annulée !"));
            return;
        }

        Player targetPlayer = Bukkit.getPlayerExact(targetName);
        if (targetPlayer == null) {
            player.sendMessage(languageManager.getStringWithColor(INPUTS_PREFIX + "BlackListInput.PlayerNotFound","&cJoueur introuvable ! Assurez-vous que le nom est correct et que le joueur est en ligne."));
            return;
        }

        UUID targetUUID = targetPlayer.getUniqueId();
        Set<UUID> deniedPlayers = new HashSet<>(warpData.getDeniedPlayers());
        if (deniedPlayers.contains(targetUUID)) {
            deniedPlayers.remove(targetUUID);
            warpManager.setDeniedPlayers(warpData, deniedPlayers);
            player.sendMessage(languageManager.getStringWithColor(INPUTS_PREFIX + "BlackListInput.RemovedFromBlacklist","&aLe joueur &e%player% &aa été retiré de la blacklist du warp !").replace("%player%", targetName));
        } else {
            deniedPlayers.add(targetUUID);
            warpManager.setDeniedPlayers(warpData, deniedPlayers);
            player.sendMessage(languageManager.getStringWithColor(INPUTS_PREFIX + "BlackListInput.AddedToBlacklist","&aLe joueur &e%player% &aa été ajouté à la blacklist du warp !").replace("%player%", targetName));
        }
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
}
