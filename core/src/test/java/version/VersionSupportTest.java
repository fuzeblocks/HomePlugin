package version;

import org.bukkit.Bukkit;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.MockedStatic;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mockStatic;

class VersionSupportTest {

    private final VersionSupportService service = new VersionSupportService() {
        @Override
        protected List<String> supportedVersions() {
            return List.of("1.19.4", "1.20.x", "1.21.x");
        }
    };

    @Nested
    @DisplayName("Tests de la méthode isVersionSupported")
    class IsVersionSupportedTests {

        @ParameterizedTest
        @DisplayName("Doit valider les versions exactes et leurs builds")
        @ValueSource(strings = {
                "1.19.4",
                "1.19.4.build.456"
        })
        void shouldSupportExactVersions(String version) {
            assertTrue(service.isVersionSupported(version));
        }

        @ParameterizedTest
        @DisplayName("Doit valider les versions sous motif wildcard (.x)")
        @ValueSource(strings = {
                "1.20",
                "1.20.1",
                "1.20.4",
                "1.20.4.1",
                "1.20-R0.1-SNAPSHOT",
                "1.21.1-R0.1-STABLE"
        })
        void shouldSupportWildcardVersions(String version) {
            assertTrue(service.isVersionSupported(version));
        }

        @ParameterizedTest
        @DisplayName("Doit rejeter les versions non incluses ou invalides")
        @ValueSource(strings = {
                "1.19.3",
                "1.19.4.1", // 1.19.4 est fixe sans .x, donc 1.19.4.1 ne doit pas passer
                "1.18.2",
                "2.20.1",
                "1.200.1"
        })
        void shouldRejectUnsupportedVersions(String version) {
            assertFalse(service.isVersionSupported(version));
        }
    }

    @Nested
    @DisplayName("Tests de la méthode isGuiSupported avec Bukkit")
    class IsGuiSupportedTests {

        @Test
        @DisplayName("Doit retourner true quand la version Bukkit est supportée")
        void shouldReturnTrueWhenBukkitVersionIsSupported() {
            try (MockedStatic<Bukkit> mockedBukkit = mockStatic(Bukkit.class)) {
                mockedBukkit.when(Bukkit::getBukkitVersion).thenReturn("1.20.4-R0.1-SNAPSHOT");

                assertTrue(service.isGuiSupported());
            }
        }

        @Test
        @DisplayName("Doit retourner false quand la version Bukkit n'est pas supportée")
        void shouldReturnFalseWhenBukkitVersionIsNotSupported() {
            try (MockedStatic<Bukkit> mockedBukkit = mockStatic(Bukkit.class)) {
                mockedBukkit.when(Bukkit::getBukkitVersion).thenReturn("1.16.5-R0.1-SNAPSHOT");

                assertFalse(service.isGuiSupported());
            }
        }
    }
}