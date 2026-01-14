package fr.fuzeblocks.homeplugin.warps;

import java.util.Map;
import java.util.Set;

public interface WarpRequestStore {

    /**
     * Sauvegarde ou met à jour un warp.
     * @param warpData données du warp
     * @return true si succès
     */
    boolean saveWarp(WarpData warpData);

    /**
     * Supprime un warp par son nom.
     * @param name nom du warp
     * @return true si un warp a été supprimé
     */
    boolean deleteWarp(String name);

    /**
     * Charge un warp par son nom.
     * @param name nom du warp
     * @return WarpData ou null si inexistant
     */
    WarpData loadWarp(String name);


    /**
     * Vérifie si un warp existe.
     * @param name nom du warp
     * @return true si présent
     */
    boolean warpExists(String name);

    /**
     * Charge tous les warps.
     * @return Map nom -> WarpData
     */
    Map<String, WarpData> loadAllWarps();

    /**
     * Retourne tous les noms de warps.
     */
    Set<String> getWarpNames();
}