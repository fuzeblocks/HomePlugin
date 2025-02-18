package fr.fuzeblocks.homeplugin.gui;


import org.bukkit.entity.Player;

import java.util.HashMap;

public class HomeGUIManager {


    private static HashMap<Player,HomeGUI> playerHomeGUIHashMap = new HashMap<>();
    public static void addGUI(Player player,HomeGUI homeGUI) {
        playerHomeGUIHashMap.computeIfAbsent(player,gui -> homeGUI);
    }
    public static void updateGUI(Player player,HomeGUI homeGUI) {
        playerHomeGUIHashMap.computeIfPresent(player, (player1,gui) -> homeGUI);
    }
    public static HomeGUI getGUI(Player player) {
        return playerHomeGUIHashMap.get(player);
    }

}
