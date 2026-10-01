package version;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

public abstract class VersionSupportService {
    protected abstract List<String> supportedVersions();

    public boolean isGuiSupported() {
        String currentVersion = org.bukkit.Bukkit.getBukkitVersion();
        return isVersionSupported(currentVersion);
    }

    protected boolean isVersionSupported(String version) {
        if (version == null || version.isBlank()) {
            return false;
        }

        String cleanVersion = version.toLowerCase(Locale.ROOT)
                .split("[-_]")[0]
                .replaceAll("\\.build\\..*", "");

        List<String> supported = supportedVersions();
        if (supported == null || supported.isEmpty()) {
            return false;
        }

        return supported.stream()
                .filter(Objects::nonNull)
                .anyMatch(sup -> {
                    String target = sup.toLowerCase(Locale.ROOT);
                    if (target.endsWith(".x")) {
                        String prefix = target.substring(0, target.length() - 2);
                        return cleanVersion.equals(prefix) || cleanVersion.startsWith(prefix + ".");
                    }
                    return cleanVersion.equals(target);
                });
    }
}
