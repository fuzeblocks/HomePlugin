package fr.fuzeblocks.homeplugin.plugin;

import java.util.List;

public interface PluginLoader {
    void loadPlugin(HomePlugin homePlugin);
    List<HomePlugin> getHomePlugin();
    void unregisterPlugin(HomePlugin homePlugin);
}
