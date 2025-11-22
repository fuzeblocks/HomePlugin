package fr.fuzeblocks.homeplugin.bluemap;

import de.bluecolored.bluemap.api.gson.MarkerGson;
import de.bluecolored.bluemap.api.markers.Marker;
import de.bluecolored.bluemap.api.markers.MarkerSet;
import de.bluecolored.bluemap.api.markers.POIMarker;
import org.bukkit.Location;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class BlueMapPublicHomeManager {

    private static File markerFile;
    private static BlueMapPublicHomeManager instance;
    private static MarkerSet markerSet;

    public BlueMapPublicHomeManager(File file) {
        markerFile = file;
        instance = this;
        loadMarker();
    }

    public static BlueMapPublicHomeManager getInstance() {
        if (instance == null) throw new IllegalStateException("BlueMapPublicHomeManager is not initialized yet.");
        return instance;
    }

    public static void addPublicHomeMarker(String homeName, Location location, String ownerName) {
         POIMarker marker = POIMarker.builder()
                .label(homeName + " (Public Home of " + ownerName + ")")
                .position(location.getX(), location.getY(), location.getZ())
                .build();
         markerSet.put(marker.getLabel(),marker);
    }

    public static void removePublicHomeMarker(String homeName, String ownerName) {
        String markerLabel = homeName + " (Public Home of " + ownerName + ")";
        markerSet.remove(markerLabel);
    }

    public static POIMarker getPublicHomeMarker(String homeName, String ownerName) {
        return (POIMarker) get(homeName, ownerName);
    }

    public static void exit() {
        saveMarker();
        markerSet = null;
        instance = null;
        markerFile = null;
    }


    private static Marker get(String homeName, String ownerName) {
        if (markerSet != null) {
              return markerSet.get(homeName + " (Public Home of " + ownerName + ")");
        }
        return null;
    }

    private static void loadMarker() {
         try (FileReader reader = new FileReader(markerFile)) {
            markerSet = MarkerGson.INSTANCE.fromJson(reader, MarkerSet.class);
        } catch (IOException ex) {
             ex.printStackTrace();
         }
    }

    private static void saveMarker() {
        try (FileWriter writer = new FileWriter(markerFile)) {
            MarkerGson.INSTANCE.toJson(markerSet, writer);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
