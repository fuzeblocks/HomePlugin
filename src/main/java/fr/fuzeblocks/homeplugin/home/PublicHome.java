package fr.fuzeblocks.homeplugin.home;

import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PublicHome {
    private UUID owner;
    private Location location;
    private String name;
    private String description;
    private boolean isPublic;
    private boolean isEnabled;
    private List<UUID> allowedPlayers = new ArrayList<>();
    private List<UUID> deniedPlayers = new ArrayList<>();

    public PublicHome(UUID owner, Location location, String name, String description, boolean isPublic, boolean isEnabled) {
        this.owner = owner;
        this.location = location;
        this.name = name;
        this.description = description;
        this.isPublic = isPublic;
        this.isEnabled = isEnabled;
    }

    public UUID owner() {
        return owner;
    }

    public void setOwner(UUID owner) {
        this.owner = owner;
    }

    public Location location() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String name() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String description() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public void addAllowedPlayer(UUID player) {
        if (!allowedPlayers.contains(player)) allowedPlayers.add(player);
    }

    public void removeAllowedPlayer(UUID player) {
        allowedPlayers.remove(player);
    }

    public boolean isPlayerAllowed(UUID player) {
        return allowedPlayers.contains(player) && !deniedPlayers.contains(player);
    }

    public void addDeniedPlayer(UUID player) {
        if (!deniedPlayers.contains(player)) deniedPlayers.add(player);
    }

    public void removeDeniedPlayer(UUID player) {
        deniedPlayers.remove(player);
    }

    public List<UUID> getAllowedPlayers() {
        return allowedPlayers;
    }

    public void setAllowedPlayers(List<UUID> allowedPlayers) {
        this.allowedPlayers = allowedPlayers;
    }

    public List<UUID> getDeniedPlayersdeniedPlayers() {
        return deniedPlayers;
    }

    public void setDeniedPlayers(List<UUID> deniedPlayers) {
        this.deniedPlayers = deniedPlayers;
    }
}