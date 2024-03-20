package fr.fuzeblocks.homeplugin.home;

import fr.fuzeblocks.homeplugin.HomePlugin;

public class HomeGetter {

    public static Object getHomeManager() {
        if (HomePlugin.getRegistrationType() == 1) {
            return HomePlugin.getHomeSQLManager();
        } else {
            return HomePlugin.getHomeManager();
        }
    }

    private HomeGetter() {
    }
}
