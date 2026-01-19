package fr.fuzeblocks.homeplugin.plugin;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Plugin manager.
 */
public class PluginManager implements PluginLoader {
    private static final List<fr.fuzeblocks.homeplugin.plugin.HomePlugin> homePlugins = new ArrayList<>();
    private static PluginManager pluginManager = null;

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static PluginManager getInstance() {
        if (pluginManager == null) {
            pluginManager = new PluginManager();
        }
        return pluginManager;
    }

    @Override
    public void loadPlugin(HomePlugin homePlugin) {
        homePlugins.add(homePlugin);
    }

    @Override
    public List<HomePlugin> getHomePlugin() {
        return homePlugins;
    }

    @Override
    public void unregisterPlugin(fr.fuzeblocks.homeplugin.plugin.HomePlugin homePlugin) {
        homePlugins.remove(homePlugin);
    }
}
