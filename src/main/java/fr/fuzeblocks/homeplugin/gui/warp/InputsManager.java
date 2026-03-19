package fr.fuzeblocks.homeplugin.gui.warp;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class InputsManager {
    private final Map<UUID,Input> inputMap = new HashMap<>();

    public void createInputsForPlayer(UUID playerUUID,Input input) {
        inputMap.put(playerUUID, input);
    }
    public boolean hasInputsForPlayer(UUID playerUUID) {
        return inputMap.containsKey(playerUUID);
    }
    public Input getInputsForPlayer(UUID playerUUID) {
        return inputMap.get(playerUUID);
    }
    public void removeInputsForPlayer(UUID playerUUID) {
        inputMap.remove(playerUUID);
    }
}
