package fr.fuzeblocks.homeplugin.plugin;

import java.util.ArrayList;
import java.util.List;

public class PluginManager implements PluginLoader {
    private static PluginManager pluginManager = null;
    private static List<fr.fuzeblocks.homeplugin.plugin.HomePlugin> homePlugins = new ArrayList<>();
    @Override
    public void loadPlugin(fr.fuzeblocks.homeplugin.plugin.HomePlugin homePlugin) {
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
    public static PluginManager getInstance() {
        if (pluginManager == null) {
            pluginManager = new PluginManager();
        }
        return pluginManager;
    }
}
