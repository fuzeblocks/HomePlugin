import fr.fuzeblocks.homeplugin.core.warps.WarpData;
import fr.fuzeblocks.homeplugin.core.warps.yml.WarpYMLManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.sql.Timestamp;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

class WarpYMLManagerTest {

    @TempDir
    File tempDir;

    private File file;
    private WarpYMLManager manager;

    private MockedStatic<Bukkit> bukkitMock;

    @Mock
    private World world;

    @Mock
    private Location location;

    @BeforeEach
    void setUp() {
        file = new File(tempDir, "warps.yml");
        manager = new WarpYMLManager(file);
        bukkitMock = mockStatic(Bukkit.class);

        MockitoAnnotations.openMocks(this);
        when(world.getName()).thenReturn("world");
        when(location.getWorld()).thenReturn(world);

        bukkitMock.when(() -> Bukkit.getWorld("world"))
                .thenReturn(world);
    }

    @AfterEach
    void tearDown() {
        bukkitMock.close();
    }


    // =========================================================
    // Helpers
    // =========================================================

    private WarpData createWarp(String name) {
        return new WarpData(
                name,
                "Thomas",
                UUID.randomUUID(),
                Material.DIAMOND,
                List.of("Line 1", "Line 2"),
                true,
                new HashSet<>(),
                10.0,
                "homeplugin.warp",
                new Timestamp(System.currentTimeMillis() + 60_000),
                new Timestamp(System.currentTimeMillis()),
                location
        );
    }

    private void addTestWarp(String name) {
        assertTrue(manager.addWarp(createWarp(name)));
    }

    // =========================================================
    // addWarp
    // =========================================================

    @Test
    void addWarp_shouldCreateWarp() {
        WarpData warp = createWarp("spawn");

        assertTrue(manager.addWarp(warp));

        assertTrue(manager.warpExists("spawn"));
        assertNotNull(manager.getWarp("spawn"));
    }

    @Test
    void addWarp_shouldReturnFalseForNullWarp() {
        assertFalse(manager.addWarp((WarpData) null));
    }

    @Test
    void addWarp_shouldReturnFalseForEmptyName() {
        WarpData warp = createWarp("");

        assertFalse(manager.addWarp(warp));
    }

    @Test
    void addWarp_shouldNotOverwriteExistingWarp() {
        WarpData first = createWarp("spawn");
        WarpData second = createWarp("spawn");

        assertTrue(manager.addWarp(first));
        assertFalse(manager.addWarp(second));

        WarpData result = manager.getWarp("spawn");

        assertNotNull(result);
        assertEquals(first.getCreatorName(), result.getCreatorName());
    }

    @Test
    void addWarpSerialized_shouldCreateWarp() {
        WarpData warp = createWarp("spawn");

        assertTrue(manager.addWarp(warp.serialize()));

        assertTrue(manager.warpExists("spawn"));
    }

    @Test
    void addWarpSerialized_shouldReturnFalseForInvalidData() {
        assertFalse(manager.addWarp("invalid"));
    }

    @Test
    void addWarpSerialized_shouldNotOverwriteExistingWarp() {
        WarpData warp = createWarp("spawn");

        assertTrue(manager.addWarp(warp));
        assertFalse(manager.addWarp(warp.serialize()));
    }

    @Test
    void addWarpWithParameters_shouldCreateWarp() {
        UUID creatorUUID = UUID.randomUUID();

        assertTrue(manager.addWarp(
                "spawn",
                "Thomas",
                creatorUUID,
                Material.EMERALD,
                List.of("Spawn"),
                true,
                new HashSet<>(),
                25.0,
                "warp.spawn",
                null,
                new Timestamp(System.currentTimeMillis()),
                location
        ));

        WarpData result = manager.getWarp("spawn");

        assertNotNull(result);
        assertEquals("spawn", result.getName());
        assertEquals("Thomas", result.getCreatorName());
        assertEquals(creatorUUID, result.getCreatorUUID());
        assertEquals(Material.EMERALD, result.getIcon());
        assertEquals(25.0, result.getCost());
        assertTrue(result.isPublic());
    }

    @Test
    void addWarpWithParameters_shouldReturnFalseForNullName() {
        assertFalse(manager.addWarp(
                null,
                "Thomas",
                UUID.randomUUID(),
                Material.DIAMOND,
                List.of(),
                true,
                new HashSet<>(),
                0,
                null,
                null,
                null,
                new Location(null, 0, 0, 0)
        ));
    }

    // =========================================================
    // deleteWarp
    // =========================================================

    @Test
    void deleteWarp_shouldDeleteExistingWarp() {
        addTestWarp("spawn");

        assertTrue(manager.deleteWarp("spawn"));

        assertFalse(manager.warpExists("spawn"));
        assertNull(manager.getWarp("spawn"));
    }

    @Test
    void deleteWarp_shouldReturnFalseWhenWarpDoesNotExist() {
        assertFalse(manager.deleteWarp("unknown"));
    }

    @Test
    void deleteWarp_shouldReturnFalseForNullName() {
        assertFalse(manager.deleteWarp((String) null));
    }

    @Test
    void deleteWarp_shouldReturnFalseForEmptyName() {
        assertFalse(manager.deleteWarp(""));
    }

    @Test
    void deleteWarpData_shouldDeleteWarp() {
        WarpData warp = createWarp("spawn");

        assertTrue(manager.addWarp(warp));
        assertTrue(manager.deleteWarp(warp));

        assertFalse(manager.warpExists("spawn"));
    }

    @Test
    void deleteWarpData_shouldReturnFalseForNullWarp() {
        assertFalse(manager.deleteWarp((WarpData) null));
    }

    // =========================================================
    // renameWarp
    // =========================================================

    @Test
    void renameWarp_shouldRenameWarp() {
        addTestWarp("old");

        assertTrue(manager.renameWarp("old", "new"));

        assertFalse(manager.warpExists("old"));
        assertTrue(manager.warpExists("new"));

        WarpData result = manager.getWarp("new");

        assertNotNull(result);
        assertEquals("new", result.getName());
    }

    @Test
    void renameWarp_shouldPreserveWarpData() {
        WarpData original = createWarp("old");

        assertTrue(manager.addWarp(original));
        assertTrue(manager.renameWarp("old", "new"));

        WarpData result = manager.getWarp("new");

        assertNotNull(result);

        assertEquals("new", result.getName());
        assertEquals(original.getCreatorName(), result.getCreatorName());
        assertEquals(original.getCreatorUUID(), result.getCreatorUUID());
        assertEquals(original.getIcon(), result.getIcon());
        assertEquals(original.getLores(), result.getLores());
        assertEquals(original.isPublic(), result.isPublic());
        assertEquals(original.getDeniedPlayers(), result.getDeniedPlayers());
        assertEquals(original.getCost(), result.getCost());
        assertEquals(original.getPermission(), result.getPermission());
        assertEquals(original.getExpirationDate(), result.getExpirationDate());
        assertEquals(original.getCreationDate(), result.getCreationDate());
    }

    @Test
    void renameWarp_shouldReturnFalseWhenOldWarpDoesNotExist() {
        assertFalse(manager.renameWarp("old", "new"));
    }

    @Test
    void renameWarp_shouldReturnFalseWhenNewNameAlreadyExists() {
        addTestWarp("old");
        addTestWarp("new");

        assertFalse(manager.renameWarp("old", "new"));

        assertTrue(manager.warpExists("old"));
        assertTrue(manager.warpExists("new"));
    }

    @Test
    void renameWarp_shouldReturnFalseForNullOldName() {
        assertFalse(manager.renameWarp((String) null, "new"));
    }

    @Test
    void renameWarp_shouldReturnFalseForNullNewName() {
        assertFalse(manager.renameWarp("old", null));
    }

    @Test
    void renameWarp_shouldReturnFalseForEmptyOldName() {
        assertFalse(manager.renameWarp("", "new"));
    }

    @Test
    void renameWarp_shouldReturnFalseForEmptyNewName() {
        assertFalse(manager.renameWarp("old", ""));
    }

    @Test
    void renameWarpData_shouldRenameWarp() {
        WarpData warp = createWarp("old");

        assertTrue(manager.addWarp(warp));
        assertTrue(manager.renameWarp(warp, "new"));

        assertFalse(manager.warpExists("old"));
        assertTrue(manager.warpExists("new"));
    }

    @Test
    void renameWarpData_shouldReturnFalseForNullWarp() {
        assertFalse(manager.renameWarp((WarpData) null, "new"));
    }

    // =========================================================
    // relocateWarp
    // =========================================================

    @Test
    void relocateWarp_shouldUpdateLocation() {
        addTestWarp("spawn");

        Location newLocation = new Location(location.getWorld(), 100, 70, 200, 45, 10);

        assertTrue(manager.relocateWarp("spawn", newLocation));

        WarpData result = manager.getWarp("spawn");

        assertNotNull(result);
        assertEquals(newLocation.getX(), result.getLocation().getX());
        assertEquals(newLocation.getY(), result.getLocation().getY());
        assertEquals(newLocation.getZ(), result.getLocation().getZ());
        assertEquals(newLocation.getYaw(), result.getLocation().getYaw());
        assertEquals(newLocation.getPitch(), result.getLocation().getPitch());
    }

    @Test
    void relocateWarp_shouldReturnFalseForUnknownWarp() {
        assertFalse(
                manager.relocateWarp(
                        "unknown",
                        new Location(null, 0, 0, 0)
                )
        );
    }

    @Test
    void relocateWarp_shouldReturnFalseForNullLocation() {
        assertFalse(manager.relocateWarp("spawn", null));
    }

    @Test
    void relocateWarpData_shouldUpdateLocation() {
        WarpData warp = createWarp("spawn");
        Location location = new Location(this.location.getWorld(), 50, 80, 100);

        assertTrue(manager.addWarp(warp));
        assertTrue(manager.relocateWarp(warp, location));

        assertEquals(
                location.getX(),
                manager.getWarp("spawn").getLocation().getX()
        );
    }

    @Test
    void relocateWarpData_shouldReturnFalseForNullWarp() {
        assertFalse(
                manager.relocateWarp(
                        (WarpData) null,
                        new Location(null, 0, 0, 0)
                )
        );
    }

    // =========================================================
    // setWarpIcon
    // =========================================================

    @Test
    void setWarpIcon_shouldChangeIcon() {
        addTestWarp("spawn");

        assertTrue(manager.setWarpIcon("spawn", Material.EMERALD));

        assertEquals(
                Material.EMERALD,
                manager.getWarp("spawn").getIcon()
        );
    }

    @Test
    void setWarpIcon_shouldReturnFalseForUnknownWarp() {
        assertFalse(manager.setWarpIcon("unknown", Material.EMERALD));
    }

    @Test
    void setWarpIcon_shouldReturnFalseForNullIcon() {
        assertFalse(manager.setWarpIcon("spawn", null));
    }

    @Test
    void setWarpIcon_shouldReturnFalseForNullName() {
        assertFalse(manager.setWarpIcon((String) null, Material.EMERALD));
    }

    @Test
    void setWarpIconData_shouldChangeIcon() {
        WarpData warp = createWarp("spawn");

        assertTrue(manager.addWarp(warp));
        assertTrue(manager.setWarpIcon(warp, Material.EMERALD));

        assertEquals(
                Material.EMERALD,
                manager.getWarp("spawn").getIcon()
        );
    }

    @Test
    void setWarpIconData_shouldReturnFalseForNullWarp() {
        assertFalse(manager.setWarpIcon((WarpData) null, Material.EMERALD));
    }

    // =========================================================
    // setWarpLores
    // =========================================================

    @Test
    void setWarpLores_shouldChangeLores() {
        addTestWarp("spawn");

        List<String> lores = List.of("New lore", "Second line");

        assertTrue(manager.setWarpLores(createWarp("spawn"), lores));

        assertEquals(
                lores,
                manager.getWarp("spawn").getLores()
        );
    }

    @Test
    void setWarpLores_shouldReturnFalseForUnknownWarp() {
        assertFalse(
                manager.setWarpLores(
                        "unknown",
                        List.of("test")
                )
        );
    }

    @Test
    void setWarpLoresSerialized_shouldChangeLores() {
        WarpData warp = createWarp("spawn");

        assertTrue(manager.addWarp(warp));

        List<String> lores = List.of("Changed");

        assertTrue(
                manager.setWarpLores(
                        warp.serialize(),
                        lores
                )
        );

        assertEquals(
                lores,
                manager.getWarp("spawn").getLores()
        );
    }

    @Test
    void setWarpLoresSerialized_shouldReturnFalseForInvalidData() {
        assertFalse(
                manager.setWarpLores(
                        "invalid",
                        List.of("test")
                )
        );
    }

    @Test
    void setWarpLoresData_shouldChangeLores() {
        WarpData warp = createWarp("spawn");

        assertTrue(manager.addWarp(warp));

        List<String> lores = List.of("Changed");

        assertTrue(manager.setWarpLores(warp, lores));

        assertEquals(
                lores,
                manager.getWarp("spawn").getLores()
        );
    }

    @Test
    void setWarpLoresData_shouldReturnFalseForNullWarp() {
        assertFalse(
                manager.setWarpLores(
                        (WarpData) null,
                        List.of("test")
                )
        );
    }

    // =========================================================
    // setWarpPublic
    // =========================================================

    @Test
    void setWarpPublic_shouldChangeVisibility() {
        addTestWarp("spawn");

        assertTrue(manager.setWarpPublic("spawn", false));

        assertFalse(manager.getWarp("spawn").isPublic());
    }

    @Test
    void setWarpPublic_shouldReturnFalseForUnknownWarp() {
        assertFalse(manager.setWarpPublic("unknown", true));
    }

    @Test
    void setWarpPublic_shouldReturnFalseForNullName() {
        assertFalse(manager.setWarpPublic((String) null, true));
    }

    @Test
    void setWarpPublicData_shouldChangeVisibility() {
        WarpData warp = createWarp("spawn");

        assertTrue(manager.addWarp(warp));
        assertTrue(manager.setWarpPublic(warp, false));

        assertFalse(manager.getWarp("spawn").isPublic());
    }

    @Test
    void setWarpPublicData_shouldReturnFalseForNullWarp() {
        assertFalse(manager.setWarpPublic((WarpData) null, true));
    }

    // =========================================================
    // setDeniedPlayers
    // =========================================================

    @Test
    void setDeniedPlayers_shouldChangeDeniedPlayers() {
        addTestWarp("spawn");

        UUID player1 = UUID.randomUUID();
        UUID player2 = UUID.randomUUID();

        Set<UUID> denied = Set.of(player1, player2);

        assertTrue(manager.setDeniedPlayers("spawn", denied));

        assertEquals(
                denied,
                manager.getWarp("spawn").getDeniedPlayers()
        );
    }

    @Test
    void setDeniedPlayers_shouldReturnFalseForUnknownWarp() {
        assertFalse(
                manager.setDeniedPlayers(
                        "unknown",
                        Set.of(UUID.randomUUID())
                )
        );
    }

    @Test
    void setDeniedPlayers_shouldReturnFalseForNullName() {
        assertFalse(manager.setDeniedPlayers((String) null, Set.of()));
    }

    @Test
    void setDeniedPlayersData_shouldChangeDeniedPlayers() {
        WarpData warp = createWarp("spawn");

        assertTrue(manager.addWarp(warp));

        Set<UUID> denied = Set.of(UUID.randomUUID());

        assertTrue(manager.setDeniedPlayers(warp, denied));

        assertEquals(
                denied,
                manager.getWarp("spawn").getDeniedPlayers()
        );
    }

    // =========================================================
    // setWarpCost
    // =========================================================

    @Test
    void setWarpCost_shouldChangeCost() {
        addTestWarp("spawn");

        assertTrue(manager.setWarpCost("spawn", 50.0));

        assertEquals(
                50.0,
                manager.getWarp("spawn").getCost()
        );
    }

    @Test
    void setWarpCost_shouldReturnFalseForUnknownWarp() {
        assertFalse(manager.setWarpCost("unknown", 50));
    }

    @Test
    void setWarpCost_shouldReturnFalseForNullName() {
        assertFalse(manager.setWarpCost((String) null, 50));
    }

    @Test
    void setWarpCostData_shouldChangeCost() {
        WarpData warp = createWarp("spawn");

        assertTrue(manager.addWarp(warp));
        assertTrue(manager.setWarpCost(warp, 100));

        assertEquals(
                100,
                manager.getWarp("spawn").getCost()
        );
    }

    @Test
    void setWarpCostData_shouldReturnFalseForNullWarp() {
        assertFalse(manager.setWarpCost((WarpData) null, 50));
    }

    // =========================================================
    // setWarpPermission
    // =========================================================

    @Test
    void setWarpPermission_shouldChangePermission() {
        addTestWarp("spawn");

        assertTrue(
                manager.setWarpPermission(
                        "spawn",
                        "warp.spawn"
                )
        );

        assertEquals(
                "warp.spawn",
                manager.getWarp("spawn").getPermission()
        );
    }

    @Test
    void setWarpPermission_shouldReturnFalseForUnknownWarp() {
        assertFalse(
                manager.setWarpPermission(
                        "unknown",
                        "warp.spawn"
                )
        );
    }

    @Test
    void setWarpPermission_shouldReturnFalseForNullName() {
        assertFalse(
                manager.setWarpPermission(
                        (String) null,
                        "warp.spawn"
                )
        );
    }

    @Test
    void setWarpPermissionData_shouldChangePermission() {
        WarpData warp = createWarp("spawn");

        assertTrue(manager.addWarp(warp));
        assertTrue(
                manager.setWarpPermission(
                        warp,
                        "warp.new"
                )
        );

        assertEquals(
                "warp.new",
                manager.getWarp("spawn").getPermission()
        );
    }

    @Test
    void setWarpPermissionData_shouldReturnFalseForNullWarp() {
        assertFalse(
                manager.setWarpPermission(
                        (WarpData) null,
                        "warp.spawn"
                )
        );
    }

    // =========================================================
    // setWarpExpirationDate
    // =========================================================

    @Test
    void setWarpExpirationDate_shouldChangeExpiration() {
        addTestWarp("spawn");

        Timestamp expiration =
                new Timestamp(System.currentTimeMillis() + 120_000);

        assertTrue(
                manager.setWarpExpirationDate(
                        "spawn",
                        expiration
                )
        );

        assertEquals(
                expiration,
                manager.getWarp("spawn").getExpirationDate()
        );
    }

    @Test
    void setWarpExpirationDate_shouldAllowNull() {
        addTestWarp("spawn");

        assertTrue(
                manager.setWarpExpirationDate(
                        "spawn",
                        null
                )
        );

        assertNull(
                manager.getWarp("spawn").getExpirationDate()
        );
    }

    @Test
    void setWarpExpirationDate_shouldReturnFalseForUnknownWarp() {
        assertFalse(
                manager.setWarpExpirationDate(
                        "unknown",
                        new Timestamp(System.currentTimeMillis())
                )
        );
    }

    @Test
    void setWarpExpirationDate_shouldReturnFalseForNullName() {
        assertFalse(
                manager.setWarpExpirationDate(
                        (String) null,
                        null
                )
        );
    }

    @Test
    void setWarpExpirationDateData_shouldChangeExpiration() {
        WarpData warp = createWarp("spawn");

        assertTrue(manager.addWarp(warp));

        Timestamp expiration =
                new Timestamp(System.currentTimeMillis() + 120_000);

        assertTrue(
                manager.setWarpExpirationDate(
                        warp,
                        expiration
                )
        );

        assertEquals(
                expiration,
                manager.getWarp("spawn").getExpirationDate()
        );
    }

    @Test
    void setWarpExpirationDateData_shouldReturnFalseForNullWarp() {
        assertFalse(
                manager.setWarpExpirationDate(
                        (WarpData) null,
                        null
                )
        );
    }

    // =========================================================
    // warpExists
    // =========================================================

    @Test
    void warpExists_shouldReturnTrueForExistingWarp() {
        addTestWarp("spawn");

        assertTrue(manager.warpExists("spawn"));
    }

    @Test
    void warpExists_shouldReturnFalseForUnknownWarp() {
        assertFalse(manager.warpExists("unknown"));
    }

    @Test
    void warpExists_shouldReturnFalseForNullName() {
        assertFalse(manager.warpExists((String) null));
    }

    @Test
    void warpExists_shouldReturnFalseForEmptyName() {
        assertFalse(manager.warpExists(""));
    }

    @Test
    void warpExistsData_shouldReturnTrueForExistingWarp() {
        WarpData warp = createWarp("spawn");

        assertTrue(manager.addWarp(warp));

        assertTrue(manager.warpExists(warp));
    }

    @Test
    void warpExistsData_shouldReturnFalseForNullWarp() {
        assertFalse(manager.warpExists((WarpData) null));
    }

    // =========================================================
    // getWarp
    // =========================================================

    @Test
    void getWarp_shouldReturnExistingWarp() {
        WarpData warp = createWarp("spawn");

        assertTrue(manager.addWarp(warp));

        WarpData result = manager.getWarp("spawn");

        assertNotNull(result);
        assertEquals("spawn", result.getName());
    }

    @Test
    void getWarp_shouldReturnNullForUnknownWarp() {
        assertNull(manager.getWarp("unknown"));
    }

    @Test
    void getWarp_shouldReturnNullForNullName() {
        assertNull(manager.getWarp(null));
    }

    @Test
    void getWarp_shouldReturnNullForEmptyName() {
        assertNull(manager.getWarp(""));
    }

    // =========================================================
    // getAllWarps
    // =========================================================

    @Test
    void getAllWarps_shouldReturnAllWarps() {
        addTestWarp("spawn");
        addTestWarp("shop");
        addTestWarp("arena");

        Map<String, WarpData> result = manager.getAllWarps();

        assertEquals(3, result.size());
        assertTrue(result.containsKey("spawn"));
        assertTrue(result.containsKey("shop"));
        assertTrue(result.containsKey("arena"));
    }

    @Test
    void getAllWarps_shouldReturnEmptyMapWhenNoWarpsExist() {
        Map<String, WarpData> result = manager.getAllWarps();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getAllWarps_shouldReturnUnmodifiableMap() {
        addTestWarp("spawn");

        Map<String, WarpData> result = manager.getAllWarps();

        assertThrows(
                UnsupportedOperationException.class,
                () -> result.put("test", createWarp("test"))
        );
    }

    // =========================================================
    // getWarpNames
    // =========================================================

    @Test
    void getWarpNames_shouldReturnAllNames() {
        addTestWarp("spawn");
        addTestWarp("shop");
        addTestWarp("arena");

        Set<String> names = manager.getWarpNames();

        assertEquals(
                Set.of("spawn", "shop", "arena"),
                names
        );
    }

    @Test
    void getWarpNames_shouldReturnEmptySetWhenNoWarpsExist() {
        assertTrue(manager.getWarpNames().isEmpty());
    }

    @Test
    void getWarpNames_shouldReturnUnmodifiableSet() {
        addTestWarp("spawn");

        Set<String> names = manager.getWarpNames();

        assertThrows(
                UnsupportedOperationException.class,
                () -> names.add("test")
        );
    }

    // =========================================================
    // isExpired
    // =========================================================

    @Test
    void isExpired_shouldReturnTrueForExpiredWarp() {
        WarpData warp = new WarpData(
                "expired",
                "Thomas",
                UUID.randomUUID(),
                Material.DIAMOND,
                List.of(),
                true,
                new HashSet<>(),
                0,
                null,
                new Timestamp(System.currentTimeMillis() - 10_000),
                new Timestamp(System.currentTimeMillis() - 20_000),
                location
        );

        assertTrue(manager.addWarp(warp));

        assertTrue(manager.isExpired("expired"));
    }

    @Test
    void isExpired_shouldReturnFalseForFutureExpiration() {
        WarpData warp = new WarpData(
                "valid",
                "Thomas",
                UUID.randomUUID(),
                Material.DIAMOND,
                List.of(),
                true,
                new HashSet<>(),
                0,
                null,
                new Timestamp(System.currentTimeMillis() + 60_000),
                new Timestamp(System.currentTimeMillis()),
                location
        );

        assertTrue(manager.addWarp(warp));

        assertFalse(manager.isExpired("valid"));
    }

    @Test
    void isExpired_shouldReturnFalseWhenExpirationIsNull() {
        WarpData warp = new WarpData(
                "permanent",
                "Thomas",
                UUID.randomUUID(),
                Material.DIAMOND,
                List.of(),
                true,
                new HashSet<>(),
                0,
                null,
                null,
                new Timestamp(System.currentTimeMillis()),
                location
        );

        assertTrue(manager.addWarp(warp));

        assertFalse(manager.isExpired("permanent"));
    }

    @Test
    void isExpired_shouldReturnFalseForUnknownWarp() {
        assertFalse(manager.isExpired("unknown"));
    }

    @Test
    void isExpiredData_shouldReturnFalseForNullWarp() {
        assertFalse(manager.isExpired((WarpData) null));
    }

    @Test
    void isExpiredData_shouldReturnTrueForExpiredWarp() {
        WarpData warp = new WarpData(
                "expired",
                "Thomas",
                UUID.randomUUID(),
                Material.DIAMOND,
                List.of(),
                true,
                new HashSet<>(),
                0,
                null,
                new Timestamp(System.currentTimeMillis() - 10_000),
                new Timestamp(System.currentTimeMillis() - 20_000),
                location
        );

        assertTrue(manager.addWarp(warp));

        assertTrue(manager.isExpired(warp));
    }
}