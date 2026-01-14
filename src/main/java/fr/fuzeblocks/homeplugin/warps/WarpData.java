package fr.fuzeblocks.homeplugin.warps;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

public class WarpData {

    private final String name;
    private final String creatorName;
    private final UUID creatorUUID;
    private final Material icon;
    private final List<String> lores;
    private final boolean isPublic;
    private final Set<UUID> allowedPlayers;
    private final double cost;
    private final String permission;
    private final Timestamp expirationDate;
    private final Timestamp creationDate;
    private final Location location;

    public WarpData(String name,
                    String creatorName,
                    UUID creatorUUID,
                    Material icon,
                    List<String> lores,
                    boolean isPublic,
                    Set<UUID> allowedPlayers,
                    double cost,
                    String permission,
                    Timestamp expirationDate,
                    Timestamp creationDate,
                    Location location) {
        this.name = name;
        this.creatorName = creatorName;
        this.creatorUUID = creatorUUID;
        this.icon = icon;
        this.lores = lores == null ? Collections.emptyList() : Collections.unmodifiableList(new ArrayList<>(lores));
        this.isPublic = isPublic;
        this.allowedPlayers = allowedPlayers == null ? Collections.emptySet() : Collections.unmodifiableSet(new HashSet<>(allowedPlayers));
        this.cost = cost;
        this.permission = permission;
        this.expirationDate = expirationDate;
        this.creationDate = creationDate;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public UUID getCreatorUUID() {
        return creatorUUID;
    }

    public Material getIcon() {
        return icon;
    }

    public List<String> getLores() {
        return lores;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public Set<UUID> getAllowedPlayers() {
        return allowedPlayers;
    }

    public double getCost() {
        return cost;
    }

    public String getPermission() {
        return permission;
    }

    public Timestamp getExpirationDate() {
        return expirationDate;
    }

    public Timestamp getCreationDate() {
        return creationDate;
    }

    public Location getLocation() {
        return location;
    }

    /**
     * Serialize this WarpData into a single string.
     *
     * Format:
     * 0  name
     * 1  creatorName
     * 2  creatorUUID
     * 3  icon (Material name)
     * 4  lores joined by ","
     * 5  isPublic
     * 6  allowedPlayers UUIDs joined by ","
     * 7  cost
     * 8  permission
     * 9  expirationDate millis or "null"
     * 10 creationDate millis or "null"
     * 11 location: world,x,y,z,yaw,pitch (comma-separated)
     */
    public String serialize() {
        String loresPart = String.join(",", lores);
        String allowedPlayersPart = allowedPlayers.stream()
                .map(UUID::toString)
                .collect(Collectors.joining(","));

        String expirationPart = (expirationDate != null) ? String.valueOf(expirationDate.getTime()) : "null";
        String creationPart = (creationDate != null) ? String.valueOf(creationDate.getTime()) : "null";

        String locationPart =
                location.getWorld().getName() + "," +
                location.getX() + "," +
                location.getY() + "," +
                location.getZ() + "," +
                location.getYaw() + "," +
                location.getPitch();

        return String.join(";",
                name,
                creatorName,
                creatorUUID.toString(),
                icon.name(),
                loresPart,
                String.valueOf(isPublic),
                allowedPlayersPart,
                String.valueOf(cost),
                permission == null ? "" : permission,
                expirationPart,
                creationPart,
                locationPart
        );
    }

    public static WarpData deserialize(String serializedData) {
        String[] parts = serializedData.split(";", -1); // -1 to keep empty strings
        if (parts.length != 12) {
            throw new IllegalArgumentException("Invalid warp data format: " + serializedData);
        }

        String name = parts[0];
        String creatorName = parts[1];
        UUID creatorUUID = UUID.fromString(parts[2]);
        Material icon = Material.valueOf(parts[3]);

        // Lores
        List<String> lores;
        if (parts[4].isEmpty()) {
            lores = Collections.emptyList();
        } else {
            lores = Arrays.asList(parts[4].split(",", -1));
        }

        boolean isPublic = Boolean.parseBoolean(parts[5]);

        // Allowed players
        Set<UUID> allowedPlayers;
        if (parts[6].isEmpty()) {
            allowedPlayers = Collections.emptySet();
        } else {
            allowedPlayers = Arrays.stream(parts[6].split(",", -1))
                    .map(UUID::fromString)
                    .collect(Collectors.toSet());
        }

        double cost = Double.parseDouble(parts[7]);
        String permission = parts[8].isEmpty() ? null : parts[8];

        Timestamp expirationDate = "null".equals(parts[9])
                ? null
                : new Timestamp(Long.parseLong(parts[9]));
        Timestamp creationDate = "null".equals(parts[10])
                ? null
                : new Timestamp(Long.parseLong(parts[10]));

        String[] locParts = parts[11].split(",", -1);
        if (locParts.length != 6) {
            throw new IllegalArgumentException("Invalid location format in warp data: " + serializedData);
        }

        Location location = new Location(
                Bukkit.getWorld(locParts[0]),
                Double.parseDouble(locParts[1]),
                Double.parseDouble(locParts[2]),
                Double.parseDouble(locParts[3]),
                Float.parseFloat(locParts[4]),
                Float.parseFloat(locParts[5])
        );

        return new WarpData(
                name,
                creatorName,
                creatorUUID,
                icon,
                lores,
                isPublic,
                allowedPlayers,
                cost,
                permission,
                expirationDate,
                creationDate,
                location
        );
    }

    public static boolean isValidSerializedData(String serializedData) {
        if (serializedData == null || serializedData.isEmpty()) return false;
        String[] parts = serializedData.split(";", -1);
        return parts.length == 12;
    }
}