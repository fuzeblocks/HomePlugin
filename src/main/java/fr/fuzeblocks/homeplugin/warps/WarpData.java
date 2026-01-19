package fr.fuzeblocks.homeplugin.warps;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import xyz.xenondevs.invui.item.builder.ItemBuilder;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

/**
 * The type Warp data.
 */
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


    /**
     * Instantiates a new Warp data.
     *
     * @param name           the name
     * @param creatorName    the creator name
     * @param creatorUUID    the creator uuid
     * @param icon           the icon
     * @param lores          the lores
     * @param isPublic       the is public
     * @param allowedPlayers the allowed players
     * @param cost           the cost
     * @param permission     the permission
     * @param expirationDate the expiration date
     * @param creationDate   the creation date
     * @param location       the location
     */
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

    /**
     * Instantiates a new Warp data.
     *
     * @param name        the name
     * @param creatorName the creator name
     * @param creatorUUID the creator uuid
     * @param location    the location
     */
    public WarpData(String name,
                    String creatorName,
                    UUID creatorUUID,
                    Location location) {
        this(name, creatorName, creatorUUID, Material.RED_BED, Collections.emptyList(), true, Collections.emptySet(), 0.0, null, null, new Timestamp(System.currentTimeMillis()), location);
    }

    /**
     * Deserialize warp data.
     *
     * @param serializedData the serialized data
     * @return the warp data
     */
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

    /**
     * Is valid serialized data boolean.
     *
     * @param serializedData the serialized data
     * @return the boolean
     */
    public static boolean isValidSerializedData(String serializedData) {
        if (serializedData == null || serializedData.isEmpty()) return false;
        String[] parts = serializedData.split(";", -1);
        return parts.length == 12;
    }

    public static ItemBuilder toItemBuilder(WarpData warpData) {
        org.bukkit.ChatColor titleColor = org.bukkit.ChatColor.GOLD;
        org.bukkit.ChatColor labelColor = org.bukkit.ChatColor.AQUA;
        org.bukkit.ChatColor valueColor = org.bukkit.ChatColor.WHITE;
        org.bukkit.ChatColor accentColor = org.bukkit.ChatColor.YELLOW;

        ItemBuilder itemBuilder = new ItemBuilder(warpData.getIcon())
                .setDisplayName(titleColor + warpData.getName());

        if (!warpData.getLores().isEmpty()) {
            List<String> coloredLores = warpData.getLores().stream()
                    .map(l -> valueColor + l)
                    .collect(Collectors.toList());
            itemBuilder.setLegacyLore(coloredLores);
        }

        itemBuilder.addLegacyLoreLines(Arrays.asList(
                labelColor + "Public: " + valueColor + warpData.isPublic(),
                labelColor + "Coût: " + valueColor + warpData.getCost(),
                labelColor + "Créateur: " + valueColor + warpData.getCreatorName(),
                labelColor + "Créé le: " + valueColor + (warpData.getCreationDate() != null ? warpData.getCreationDate().toString() : "N/A"),
                labelColor + "Expire: " + valueColor + (warpData.getExpirationDate() != null ? warpData.getExpirationDate().toString() : "Jamais"),
                labelColor + "Lieu: " + valueColor + warpData.getLocation().getWorld().getName() + " " +
                        accentColor + "(" + String.format("%.1f, %.1f, %.1f)", warpData.getLocation().getX(), warpData.getLocation().getY(), warpData.getLocation().getZ()) + valueColor,
                labelColor + "Permission: " + valueColor + (warpData.getPermission() != null ? warpData.getPermission() : "Aucune")
        ));

        return itemBuilder;
    }
    //Language keys are missing.
    //Todo
    public static ItemBuilder toItemBuilderUsingLanguage(WarpData warpData, fr.fuzeblocks.homeplugin.language.LanguageManager languageManager) {
        org.bukkit.ChatColor titleColor = org.bukkit.ChatColor.GOLD;
        org.bukkit.ChatColor labelColor = org.bukkit.ChatColor.AQUA;
        org.bukkit.ChatColor valueColor = org.bukkit.ChatColor.WHITE;
        org.bukkit.ChatColor accentColor = org.bukkit.ChatColor.YELLOW;

        ItemBuilder itemBuilder = new ItemBuilder(warpData.getIcon())
                .setDisplayName(titleColor + warpData.getName());

        if (!warpData.getLores().isEmpty()) {
            List<String> coloredLores = warpData.getLores().stream()
                    .map(l -> valueColor + l)
                    .collect(Collectors.toList());
            itemBuilder.setLegacyLore(coloredLores);
        }

        itemBuilder.addLegacyLoreLines(Arrays.asList(
                labelColor + languageManager.getString("Warp.Item.Public") + ": " + valueColor + warpData.isPublic(),
                labelColor + languageManager.getString("Warp.Item.Cost") + ": " + valueColor + warpData.getCost(),
                labelColor + languageManager.getString("Warp.Item.Creator") + ": " + valueColor + warpData.getCreatorName(),
                labelColor + languageManager.getString("Warp.Item.Created-On") + ": " + valueColor + (warpData.getCreationDate() != null ? warpData.getCreationDate().toString() : languageManager.getString("Warp.Item.N/A")),
                labelColor + languageManager.getString("Warp.Item.Expires") + ": " + valueColor + (warpData.getExpirationDate() != null ? warpData.getExpirationDate().toString() : languageManager.getString("Warp.Item.Never")),
                labelColor + languageManager.getString("Warp.Item.Location") + ": " + valueColor + warpData.getLocation().getWorld().getName() + " " +
                        accentColor + "(" + String.format("%.1f, %.1f, %.1f)", warpData.getLocation().getX(), warpData.getLocation().getY(), warpData.getLocation().getZ()) + valueColor,
                labelColor + languageManager.getString("Warp.Item.Permission") + ": " + valueColor + (warpData.getPermission() != null ? warpData.getPermission() : languageManager.getString("Warp.Item.None"))
        ));

        return itemBuilder;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets creator name.
     *
     * @return the creator name
     */
    public String getCreatorName() {
        return creatorName;
    }

    /**
     * Gets creator uuid.
     *
     * @return the creator uuid
     */
    public UUID getCreatorUUID() {
        return creatorUUID;
    }

    /**
     * Gets icon.
     *
     * @return the icon
     */
    public Material getIcon() {
        return icon;
    }

    /**
     * Gets lores.
     *
     * @return the lores
     */
    public List<String> getLores() {
        return lores;
    }

    /**
     * Is public boolean.
     *
     * @return the boolean
     */
    public boolean isPublic() {
        return isPublic;
    }

    /**
     * Gets allowed players.
     *
     * @return the allowed players
     */
    public Set<UUID> getAllowedPlayers() {
        return allowedPlayers;
    }

    /**
     * Gets cost.
     *
     * @return the cost
     */
    public double getCost() {
        return cost;
    }

    /**
     * Gets permission.
     *
     * @return the permission
     */
    public String getPermission() {
        return permission;
    }

    /**
     * Gets expiration date.
     *
     * @return the expiration date
     */
    public Timestamp getExpirationDate() {
        return expirationDate;
    }

    /**
     * Gets creation date.
     *
     * @return the creation date
     */
    public Timestamp getCreationDate() {
        return creationDate;
    }

    /**
     * Gets location.
     *
     * @return the location
     */
    public Location getLocation() {
        return location;
    }

    public boolean canAccess(UUID playerUUID) {
        if (isPublic) {
            return true;
        }
        return allowedPlayers.contains(playerUUID);
    }

    /**
     * Serialize this WarpData into a single string.
     * <p>
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
     *
     * @return the string
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


}