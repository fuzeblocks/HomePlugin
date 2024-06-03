package fr.fuzeblocks.homeplugin.plugin;

import java.util.List;

public interface PluginLoader {
    public void loadPlugin(HomePlugin homePlugin);
    public List<HomePlugin> getHomePlugin();
    public void unregisterPlugin(HomePlugin homePlugin);
}
