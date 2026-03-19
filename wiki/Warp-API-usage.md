# 🌀 Warp Managers – `fr.fuzeblocks.homeplugin.warps.*`

HomePlugin centralizes warp operations behind a unified facade: `WarpManager`.  
It delegates to the active storage backend so your integrations remain storage‑agnostic.

> ⚠️ **Package note:** The correct package is `fr.fuzeblocks.homeplugin.warps` (with a trailing **s**).
> The alternate spelling `fr.fuzeblocks.homeplugin.warp` (without the **s**) does **not** exist.

- Preferred entry points:
  - `HomePlugin.getWarpManager()` — plugin-level accessor
  - `WarpManager.getInstance()` — static singleton
- Backends are selected by plugin configuration.
- Use concrete implementations only when you need backend-specific behavior.

---

## 📦 Supported Backends

| Backend | Typical Use |
|---------|-------------|
| YAML (local file) | Simple / single-server |
| SQL (database) | Networks / multi-instance |

Tip: For most integrations, use the facade (`WarpManager`) only.

---

## 🗂️ WarpData – `fr.fuzeblocks.homeplugin.warps.WarpData`

All warp operations use `WarpData` as the core data model.  
It is **immutable** — update operations return a new persisted state via the manager.

Key fields (all accessible via getters):

| Field | Type | Description |
|-------|------|-------------|
| `name` | `String` | Warp name |
| `creatorName` | `String` | Display name of the creator |
| `creatorUUID` | `UUID` | UUID of the creator |
| `icon` | `Material` | GUI icon (defaults to `RED_BED`) |
| `lores` | `List<String>` | GUI item lore lines |
| `isPublic` | `boolean` | Whether the warp is accessible to all |
| `deniedPlayers` | `Set<UUID>` | Players explicitly blocked from using this warp |
| `cost` | `double` | Teleport cost |
| `permission` | `String` | Optional permission node |
| `expirationDate` | `Timestamp` | Expiration date, or `null` for permanent |
| `creationDate` | `Timestamp` | Creation date |
| `location` | `Location` | Warp destination |

Convenience constructor (minimal fields, sensible defaults):

```java
WarpData warpData = new WarpData("spawn", player.getName(), player.getUniqueId(), spawnLocation);
// Defaults: icon=RED_BED, lores=[], isPublic=true, deniedPlayers=[], cost=0, permission=null, expiration=null
```

---

## 🔗 Unified Warp API (via `WarpManager`)

`WarpManager` is the recommended integration surface for warp lifecycle operations.  
All methods from the `Warp` interface are delegated to the active backend.

### Creation

```java
// Full-featured creation (all fields)
boolean addWarp(String name, String creatorName, UUID creatorUUID,
                Material icon, List<String> lores, boolean isPublic,
                Set<UUID> allowedPlayers, double cost, String permission,
                Timestamp expirationDate, Timestamp creationDate, Location location);

// Create from a WarpData object (recommended)
boolean addWarp(WarpData warpData);

// Create from serialized string (implementation-dependent format)
boolean addWarp(String serializedData);
```

### Deletion

```java
boolean deleteWarp(String name);
boolean deleteWarp(WarpData warpData);
```

### Rename / Relocate

```java
boolean renameWarp(String oldName, String newName);
boolean renameWarp(WarpData warpData, String newName);

boolean relocateWarp(String name, Location newLocation);
boolean relocateWarp(WarpData warpData, Location newLocation);
```

### Attribute Updates

```java
boolean setWarpIcon(String name, Material newIcon);
boolean setWarpIcon(WarpData warpData, Material newIcon);

boolean setWarpLores(String name, List<String> newLores);
boolean setWarpLores(WarpData warpData, List<String> newLores);

boolean setWarpPublic(String name, boolean isPublic);
boolean setWarpPublic(WarpData warpData, boolean isPublic);

boolean setDeniedPlayers(String name, Set<UUID> deniedPlayers);
boolean setDeniedPlayers(WarpData warpData, Set<UUID> deniedPlayers);

boolean setWarpCost(String name, double cost);
boolean setWarpCost(WarpData warpData, double cost);

boolean setWarpPermission(String name, String permission);
boolean setWarpPermission(WarpData warpData, String permission);

boolean setWarpExpirationDate(String name, Timestamp expirationDate);
boolean setWarpExpirationDate(WarpData warpData, Timestamp expirationDate);
```

### Queries

```java
// Check existence
boolean warpExists(String name);
boolean warpExists(WarpData warpData);

// Fetch a single warp (returns null if not found)
WarpData getWarp(String name);

// Fetch all warps
Map<String, WarpData> getAllWarps();

// List only warp names (returns Set<String>)
Set<String> getWarpNames();

// Check expiry
boolean isExpired(String name);
boolean isExpired(WarpData warpData);
```

### Additional helpers on `WarpManager`

```java
CacheManager getCacheManager();        // access the shared cache layer
boolean isStatus(Player player);       // check player status via StatusManager
```

---

## ✅ Usage (Facade, storage‑agnostic)

```java
import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.warps.WarpData;
import fr.fuzeblocks.homeplugin.warps.WarpManager;
import org.bukkit.Location;
import org.bukkit.Material;

WarpManager warps = HomePlugin.getWarpManager(); // preferred
// or: WarpManager warps = WarpManager.getInstance();

// Create a warp (minimal WarpData constructor)
WarpData newWarp = new WarpData("spawn", player.getName(), player.getUniqueId(), spawnLocation);
warps.addWarp(newWarp);

// Read
boolean exists = warps.warpExists("spawn");
WarpData data   = warps.getWarp("spawn");
Location loc    = data != null ? data.getLocation() : null;

// Rename / move
warps.renameWarp("spawn", "main_spawn");
warps.relocateWarp("main_spawn", otherLocation);

// Update icon / public flag / cost
warps.setWarpIcon("main_spawn", Material.EMERALD);
warps.setWarpPublic("main_spawn", false);
warps.setWarpCost("main_spawn", 50.0);

// List names
for (String name : warps.getWarpNames()) {
    // ...
}

// All warp data
Map<String, WarpData> all = warps.getAllWarps();

// Delete
warps.deleteWarp("main_spawn");
```

---

## ❌ Verification Notes — Inaccuracies in the Prior Draft

The following items from the previously circulated draft wiki do **not** match the `warps-dev` implementation:

| Draft claim | Actual status |
|-------------|---------------|
| Package `fr.fuzeblocks.homeplugin.warp.*` | **Wrong** — correct package is `fr.fuzeblocks.homeplugin.warps.*` (trailing **s**) |
| `warps.setWarp("spawn", spawnLocation)` | **Does not exist** — use `addWarp(WarpData)` instead |
| `warps.exist("spawn")` | **Does not exist** — use `warpExists(String name)` |
| `warps.getWarpLocation("spawn")` returns `Location` | **Does not exist** — use `getWarp(String name)` → `WarpData`, then `.getLocation()` |
| `warps.getWarpsName()` | **Does not exist** — use `getWarpNames()` which returns `Set<String>` |
| `fr.fuzeblocks.homeplugin.warp.offline.OfflineWarp` | **Does not exist** in `warps-dev`. There is no `OfflineWarp` class for warps (only `OfflineHome` for homes). |
| `WarpPermissionManager` | **Does not exist** in `warps-dev`. Only `HomePermissionManager` exists for homes. |

---

## 🧩 Related Components

- `WarpData` — immutable data model for a single warp entry
- `Warp` interface — the contract implemented by `WarpManager`, `WarpYMLManager`, and `WarpSQLManager`
- `WarpRequestStore` / `LocalWarpStore` / `RedisWarpStore` — internal store layer (not part of public API)
- `CacheManager` — sync/cache layer accessed via `WarpManager.getCacheManager()`
- `OnWarpTeleportEvent` — fired on warp teleport (`fr.fuzeblocks.homeplugin.event`)
- `WarpGUIManager` — GUI management layer for warp UIs

---

## 🧭 Integration Guidance

- Prefer **`WarpManager`** (via `HomePlugin.getWarpManager()`) for all in-game and plugin-facing features.
- Use `WarpData` for all warp creation — the simple `new WarpData(name, creatorName, uuid, location)` constructor provides sensible defaults.
- Avoid hard-coding backend assumptions (YAML/SQL) in feature code.
- Keep validation/permission checks in manager/service layer, not command layer only.

---

## 🔁 Migration Notes (older direct-manager usage)

- Move integrations to `HomePlugin.getWarpManager()`.
- Replace calls to non-existent methods with their correct equivalents:
  - `setWarp(name, loc)` → `addWarp(new WarpData(name, creatorName, uuid, loc))`
  - `exist(name)` → `warpExists(name)`
  - `getWarpLocation(name)` → `getWarp(name).getLocation()`
  - `getWarpsName()` → `getWarpNames()`
- Keep backend-specific logic isolated to migration/admin utilities only.
