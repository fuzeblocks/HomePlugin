# 🌀 Warp Managers – `fr.fuzeblocks.homeplugin.warps.*`

HomePlugin now centralizes warp operations behind a unified facade: `WarpManager`.  
It delegates to the active storage backend so your integrations remain storage‑agnostic.

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

## 🔗 Unified Warp API (via `WarpManager`)

`WarpManager` is the recommended integration surface for warp lifecycle operations.

Typical capabilities include:

- creating a warp by name and location (via `WarpData`)
- renaming a warp
- relocating a warp
- listing warp names
- fetching full warp data by name
- checking existence
- deleting a warp
- accessing cache layer (`getCacheManager()`)

> Keep your code manager-driven (not backend-driven) so your plugin remains compatible with YAML/SQL setups.

---

## ✅ Usage (Facade, storage‑agnostic)

```java
import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.warps.WarpData;
import fr.fuzeblocks.homeplugin.warps.WarpManager;
import org.bukkit.Location;

WarpManager warps = HomePlugin.getWarpManager(); // preferred

// Create/update
warps.addWarp(new WarpData("spawn", player.getName(), player.getUniqueId(), spawnLocation));

// Read
boolean exists = warps.warpExists("spawn");
Location location = warps.getWarp("spawn").getLocation();

// Rename / move
warps.renameWarp("spawn", "main_spawn");
warps.relocateWarp("main_spawn", otherLocation);

// List
for (String name : warps.getWarpNames()) {
    // ...
}

// Delete
warps.deleteWarp("main_spawn");
```

---

## 📴 Offline Warp Management

> **Note:** An `OfflineWarp` interface (`fr.fuzeblocks.homeplugin.warps.offline`) is **not present** in the current `warps-dev` branch.  
> For UUID-based or offline-safe warp lookups, use `WarpManager` directly — all warp operations are name-based and do not require an online `Player`.  
> For offline *home* operations, see `fr.fuzeblocks.homeplugin.home.offline.OfflineHome`.

---

## 🧩 Related Components

- `WarpData` — immutable data model for a single warp (`fr.fuzeblocks.homeplugin.warps.WarpData`)
- `Warp` interface — contract implemented by `WarpManager`, `WarpYMLManager`, and `WarpSQLManager`
- `CacheManager` — sync/cache layer used by managers (`WarpManager.getCacheManager()`)
- `OnWarpTeleportEvent` — teleport hook (`fr.fuzeblocks.homeplugin.event`)
- `WarpGUIManager` — GUI layer for warp management UIs

---

## 🧭 Integration Guidance

- Prefer **`WarpManager`** for all in-game and plugin-facing features.
- Avoid hard-coding backend assumptions (YAML/SQL) in feature code.
- Keep validation/permission checks in manager/service layer, not command layer only.

---

## 🔁 Migration Notes (older direct-manager usage)

- Move integrations to `HomePlugin.getWarpManager()` (or `WarpManager.getInstance()`).
- Replace direct backend calls with facade methods for:
  - create/set → `addWarp(new WarpData(name, creatorName, uuid, location))`
  - rename → `renameWarp(oldName, newName)`
  - relocate → `relocateWarp(name, newLocation)`
  - query/list → `warpExists(name)`, `getWarp(name)`, `getWarpNames()`, `getAllWarps()`
  - delete → `deleteWarp(name)`
- Keep backend-specific logic isolated to migration/admin utilities only.
