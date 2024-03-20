package fr.fuzeblocks.homeplugin.spawn;

import fr.fuzeblocks.homeplugin.HomePlugin;

public class SpawnGetter {

    public static Object getSpawnManager() {
        if (HomePlugin.getRegistrationType() == 1) {
            return HomePlugin.getSpawnSQLManager();
        } else {
            return HomePlugin.getSpawnManager();
        }
    }

    private SpawnGetter() {
    }
}
