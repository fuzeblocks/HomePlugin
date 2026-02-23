package fr.fuzeblocks.homeplugin.warps.yml;

import fr.fuzeblocks.homeplugin.warps.Warp;
import fr.fuzeblocks.homeplugin.warps.WarpData;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The type Warp yml manager.
 */
public class WarpYMLManager implements Warp {

    private final File file;
    private final YamlConfiguration yaml;

    /**
     * Instantiates a new Warp yml manager.
     *
     * @param file the file
     */
    public WarpYMLManager(File file) {
        this.file = file;
        this.yaml = YamlConfiguration.loadConfiguration(file);
    }

    private String keyOf(String name) {
        return "Warps." + name;
    }

    private boolean saveSafely() {
        try {
            yaml.save(file);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private WarpData getWarpInternal(String name) {
        if (name == null || name.isEmpty()) return null;
        String key = keyOf(name);
        if (!yaml.contains(key)) return null;

        String serialized = yaml.getString(key);
        if (serialized == null) return null;
        if (!WarpData.isValidSerializedData(serialized)) return null;

        return WarpData.deserialize(serialized);
    }

    private String getNameFromSerialized(String serializedData) {
        if (!WarpData.isValidSerializedData(serializedData)) return null;
        return WarpData.deserialize(serializedData).getName();
    }

    // ----------------- CREATION -----------------

    @Override
    public boolean addWarp(String name,
                           String creatorName,
                           java.util.UUID creatorUUID,
                           Material icon,
                           java.util.List<String> lores,
                           boolean isPublic,
                           java.util.Set<java.util.UUID> allowedPlayers,
                           double cost,
                           String permission,
                           Timestamp expirationDate,
                           Timestamp creationDate,
                           Location location) {

        if (name == null || name.isEmpty()) return false;
        String key = keyOf(name);
        if (yaml.contains(key)) return false; // already exists

        WarpData warpData = new WarpData(name, creatorName, creatorUUID, icon, lores,
                isPublic, allowedPlayers, cost, permission, expirationDate, creationDate, location);

        yaml.set(key, warpData.serialize());
        return saveSafely();
    }

    @Override
    public boolean addWarp(String serializedData) {
        if (!WarpData.isValidSerializedData(serializedData)) return false;

        WarpData warpData = WarpData.deserialize(serializedData);
        String name = warpData.getName();
        if (name == null || name.isEmpty()) return false;

        String key = keyOf(name);
        if (yaml.contains(key)) return false; // already exists

        yaml.set(key, serializedData);
        return saveSafely();
    }

    @Override
    public boolean addWarp(WarpData warpData) {
        if (warpData == null || warpData.getName() == null || warpData.getName().isEmpty()) return false;

        String key = keyOf(warpData.getName());
        if (yaml.contains(key)) return false;

        String serializedData = warpData.serialize();
        if (!WarpData.isValidSerializedData(serializedData)) return false;

        yaml.set(key, serializedData);
        return saveSafely();
    }

    // ----------------- DELETION -----------------

    @Override
    public boolean deleteWarp(String name) {
        if (name == null || name.isEmpty()) return false;
        String key = keyOf(name);
        if (!yaml.contains(key)) return false;

        yaml.set(key, null);
        return saveSafely();
    }

    @Override
    public boolean deleteWarp(WarpData warpData) {
        if (warpData == null) return false;
        return deleteWarp(warpData.getName());
    }

    // ----------------- RENAME -----------------

    @Override
    public boolean renameWarp(String oldName, String newName) {
        if (oldName == null || newName == null || oldName.isEmpty() || newName.isEmpty()) return false;

        String oldKey = keyOf(oldName);
        String newKey = keyOf(newName);

        if (!yaml.contains(oldKey)) return false;
        if (yaml.contains(newKey)) return false; // cannot overwrite

        String serialized = yaml.getString(oldKey);
        if (serialized == null || !WarpData.isValidSerializedData(serialized)) return false;

        WarpData oldData = WarpData.deserialize(serialized);
        WarpData newData = new WarpData(
                newName,
                oldData.getCreatorName(),
                oldData.getCreatorUUID(),
                oldData.getIcon(),
                oldData.getLores(),
                oldData.isPublic(),
                oldData.getAllowedPlayers(),
                oldData.getCost(),
                oldData.getPermission(),
                oldData.getExpirationDate(),
                oldData.getCreationDate(),
                oldData.getLocation()
        );

        yaml.set(oldKey, null);
        yaml.set(newKey, newData.serialize());
        return saveSafely();
    }

    @Override
    public boolean renameWarp(WarpData warpData, String newName) {
        if (warpData == null) return false;
        return renameWarp(warpData.getName(), newName);
    }

    // ----------------- RELOCATE -----------------

    @Override
    public boolean relocateWarp(String name, Location newLocation) {
        if (name == null || newLocation == null) return false;
        WarpData data = getWarpInternal(name);
        if (data == null) return false;

        WarpData updated = new WarpData(
                data.getName(),
                data.getCreatorName(),
                data.getCreatorUUID(),
                data.getIcon(),
                data.getLores(),
                data.isPublic(),
                data.getAllowedPlayers(),
                data.getCost(),
                data.getPermission(),
                data.getExpirationDate(),
                data.getCreationDate(),
                newLocation
        );

        yaml.set(keyOf(name), updated.serialize());
        return saveSafely();
    }

    @Override
    public boolean relocateWarp(WarpData warpData, Location newLocation) {
        if (warpData == null) return false;
        return relocateWarp(warpData.getName(), newLocation);
    }

    // ----------------- SET ICON -----------------

    @Override
    public boolean setWarpIcon(String name, Material newIcon) {
        if (name == null || newIcon == null) return false;
        WarpData data = getWarpInternal(name);
        if (data == null) return false;

        WarpData updated = new WarpData(
                data.getName(),
                data.getCreatorName(),
                data.getCreatorUUID(),
                newIcon,
                data.getLores(),
                data.isPublic(),
                data.getAllowedPlayers(),
                data.getCost(),
                data.getPermission(),
                data.getExpirationDate(),
                data.getCreationDate(),
                data.getLocation()
        );

        yaml.set(keyOf(name), updated.serialize());
        return saveSafely();
    }

    @Override
    public boolean setWarpIcon(WarpData warpData, Material newIcon) {
        if (warpData == null) return false;
        return setWarpIcon(warpData.getName(), newIcon);
    }

    // ----------------- SET LORES -----------------

    @Override
    public boolean setWarpLores(String serializedData, java.util.List<String> newLores) {
        String name = getNameFromSerialized(serializedData);
        if (name == null) return false;
        return setWarpLoresByName(name, newLores);
    }

    private boolean setWarpLoresByName(String name, java.util.List<String> newLores) {
        if (name == null) return false;
        WarpData data = getWarpInternal(name);
        if (data == null) return false;

        WarpData updated = new WarpData(
                data.getName(),
                data.getCreatorName(),
                data.getCreatorUUID(),
                data.getIcon(),
                newLores,
                data.isPublic(),
                data.getAllowedPlayers(),
                data.getCost(),
                data.getPermission(),
                data.getExpirationDate(),
                data.getCreationDate(),
                data.getLocation()
        );

        yaml.set(keyOf(name), updated.serialize());
        return saveSafely();
    }

    @Override
    public boolean setWarpLores(WarpData warpData, java.util.List<String> newLores) {
        if (warpData == null) return false;
        return setWarpLoresByName(warpData.getName(), newLores);
    }

    // ----------------- SET PUBLIC -----------------

    @Override
    public boolean setWarpPublic(String name, boolean isPublic) {
        if (name == null) return false;
        WarpData data = getWarpInternal(name);
        if (data == null) return false;

        WarpData updated = new WarpData(
                data.getName(),
                data.getCreatorName(),
                data.getCreatorUUID(),
                data.getIcon(),
                data.getLores(),
                isPublic,
                data.getAllowedPlayers(),
                data.getCost(),
                data.getPermission(),
                data.getExpirationDate(),
                data.getCreationDate(),
                data.getLocation()
        );

        yaml.set(keyOf(name), updated.serialize());
        return saveSafely();
    }

    @Override
    public boolean setWarpPublic(WarpData warpData, boolean isPublic) {
        if (warpData == null) return false;
        return setWarpPublic(warpData.getName(), isPublic);
    }

    // ----------------- SET ALLOWED PLAYERS -----------------

    @Override
    public boolean setWarpAllowedPlayers(String name, java.util.Set<java.util.UUID> allowedPlayers) {
        if (name == null) return false;
        WarpData data = getWarpInternal(name);
        if (data == null) return false;

        WarpData updated = new WarpData(
                data.getName(),
                data.getCreatorName(),
                data.getCreatorUUID(),
                data.getIcon(),
                data.getLores(),
                data.isPublic(),
                allowedPlayers,
                data.getCost(),
                data.getPermission(),
                data.getExpirationDate(),
                data.getCreationDate(),
                data.getLocation()
        );

        yaml.set(keyOf(name), updated.serialize());
        return saveSafely();
    }

    @Override
    public boolean setWarpAllowedPlayers(WarpData warpData, java.util.Set<java.util.UUID> allowedPlayers) {
        if (warpData == null) return false;
        return setWarpAllowedPlayers(warpData.getName(), allowedPlayers);
    }

    // ----------------- SET COST -----------------

    @Override
    public boolean setWarpCost(String name, double cost) {
        if (name == null) return false;
        WarpData data = getWarpInternal(name);
        if (data == null) return false;

        WarpData updated = new WarpData(
                data.getName(),
                data.getCreatorName(),
                data.getCreatorUUID(),
                data.getIcon(),
                data.getLores(),
                data.isPublic(),
                data.getAllowedPlayers(),
                cost,
                data.getPermission(),
                data.getExpirationDate(),
                data.getCreationDate(),
                data.getLocation()
        );

        yaml.set(keyOf(name), updated.serialize());
        return saveSafely();
    }

    @Override
    public boolean setWarpCost(WarpData warpData, double cost) {
        if (warpData == null) return false;
        return setWarpCost(warpData.getName(), cost);
    }

    // ----------------- SET PERMISSION -----------------

    @Override
    public boolean setWarpPermission(String name, String permission) {
        if (name == null) return false;
        WarpData data = getWarpInternal(name);
        if (data == null) return false;

        WarpData updated = new WarpData(
                data.getName(),
                data.getCreatorName(),
                data.getCreatorUUID(),
                data.getIcon(),
                data.getLores(),
                data.isPublic(),
                data.getAllowedPlayers(),
                data.getCost(),
                permission,
                data.getExpirationDate(),
                data.getCreationDate(),
                data.getLocation()
        );

        yaml.set(keyOf(name), updated.serialize());
        return saveSafely();
    }

    @Override
    public boolean setWarpPermission(WarpData warpData, String permission) {
        if (warpData == null) return false;
        return setWarpPermission(warpData.getName(), permission);
    }

    // ----------------- SET EXPIRATION DATE -----------------

    @Override
    public boolean setWarpExpirationDate(String name, Timestamp expirationDate) {
        if (name == null) return false;
        WarpData data = getWarpInternal(name);
        if (data == null) return false;

        WarpData updated = new WarpData(
                data.getName(),
                data.getCreatorName(),
                data.getCreatorUUID(),
                data.getIcon(),
                data.getLores(),
                data.isPublic(),
                data.getAllowedPlayers(),
                data.getCost(),
                data.getPermission(),
                expirationDate,
                data.getCreationDate(),
                data.getLocation()
        );

        yaml.set(keyOf(name), updated.serialize());
        return saveSafely();
    }

    @Override
    public boolean setWarpExpirationDate(WarpData warpData, Timestamp expirationDate) {
        if (warpData == null) return false;
        return setWarpExpirationDate(warpData.getName(), expirationDate);
    }

    // ----------------- QUERIES -----------------

    @Override
    public boolean warpExists(String name) {
        if (name == null || name.isEmpty()) return false;
        return yaml.contains(keyOf(name));
    }

    @Override
    public boolean warpExists(WarpData warpData) {
        if (warpData == null) return false;
        return warpExists(warpData.getName());
    }

    @Override
    public WarpData getWarp(String name) {
        return getWarpInternal(name);
    }

    @Override
    public Map<String, WarpData> getAllWarps() {
        ConfigurationSection section = yaml.getConfigurationSection("Warps");
        if (section == null) return Collections.emptyMap();

        Map<String, WarpData> result = new HashMap<>();

        for (String name : section.getKeys(false)) {
            String key = keyOf(name);
            String serialized = yaml.getString(key);
            if (serialized == null) continue;
            if (!WarpData.isValidSerializedData(serialized)) continue;

            WarpData data = WarpData.deserialize(serialized);
            if (data != null) {
                result.put(name, data);
            }
        }

        return Collections.unmodifiableMap(result);
    }

    @Override
    public Set<String> getWarpNames() {
        ConfigurationSection section = yaml.getConfigurationSection("Warps");
        if (section == null) return Collections.emptySet();

        return Collections.unmodifiableSet(
                section.getKeys(false)
                        .stream()
                        .collect(Collectors.toSet())
        );
    }
    @Override
    public boolean isExpired(String name) {
        WarpData data = getWarpInternal(name);
        if (data == null) return false;

        Timestamp expiration = data.getExpirationDate();
        if (expiration == null) return false;

        Timestamp now = new Timestamp(System.currentTimeMillis());
        return now.after(expiration);
    }
    @Override
    public boolean isExpired(WarpData warpData) {
        if (warpData == null) return false;
        return isExpired(warpData.getName());
    }
}