package fr.fuzeblocks.homeplugin.plugin;

import java.util.List;

/**
 * The interface Plugin loader.
 */
public interface PluginLoader {
    /**
     * Load plugin.
     *
     * @param homePlugin the home plugin
     */
    void loadPlugin(HomePlugin homePlugin);

    /**
     * Gets home plugin.
     *
     * @return the home plugin
     */
    List<HomePlugin> getHomePlugin();

    /**
     * Unregister plugin.
     *
     * @param homePlugin the home plugin
     */
    void unregisterPlugin(HomePlugin homePlugin);
}
