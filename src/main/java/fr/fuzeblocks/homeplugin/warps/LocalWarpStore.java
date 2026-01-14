package fr.fuzeblocks.homeplugin.warps;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * The type Local warp store.
 */
public class LocalWarpStore implements WarpRequestStore {

    private final Map<String, WarpData> store = new HashMap<>();

    @Override
    public boolean saveWarp(WarpData warpData) {
        if (warpData == null || warpData.getName() == null || warpData.getName().isEmpty()) {
            return false;
        }
        store.put(warpData.getName(), warpData);
        return true;
    }

    @Override
    public boolean deleteWarp(String name) {
        if (name == null || name.isEmpty()) return false;
        return store.remove(name) != null;
    }

    @Override
    public WarpData loadWarp(String name) {
        if (name == null || name.isEmpty()) return null;
        return store.get(name);
    }

    @Override
    public boolean warpExists(String name) {
        if (name == null || name.isEmpty()) return false;
        return store.containsKey(name);
    }

    @Override
    public Map<String, WarpData> loadAllWarps() {
        return Collections.unmodifiableMap(new HashMap<>(store));
    }

    @Override
    public Set<String> getWarpNames() {
        return Collections.unmodifiableSet(store.keySet());
    }
}