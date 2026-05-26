package fr.fuzeblocks.homeplugin.gui;

import fr.fuzeblocks.homeplugin.HomePlugin;

public final class GuiBridgeFactory {

    private GuiBridgeFactory() {
    }

    public static GuiBridge create(HomePlugin plugin) {
        String mode = plugin.getConfig().getString("gui-mode", "legacy").toLowerCase();

        if ("modern".equals(mode)) {
            GuiBridge modern = instantiate("fr.fuzeblocks.homeplugin.gui.modern.ModernGuiBridge");
            if (modern != null) {
                return modern;
            }
            plugin.getLogger().warning("Modern GUI indisponible, fallback legacy.");
        }

        GuiBridge legacy = instantiate("fr.fuzeblocks.homeplugin.gui.legacy.LegacyGuiBridge");
        if (legacy != null) {
            return legacy;
        }

        throw new IllegalStateException("No GUI bridge implementation could be loaded.");
    }

    private static GuiBridge instantiate(String className) {
        try {
            Class<?> type = Class.forName(className);
            return (GuiBridge) type.getDeclaredConstructor().newInstance();
        } catch (Throwable ignored) {
            return null;
        }
    }
}