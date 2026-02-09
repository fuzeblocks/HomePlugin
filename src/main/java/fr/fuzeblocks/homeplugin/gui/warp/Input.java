package fr.fuzeblocks.homeplugin.gui.warp;

import fr.fuzeblocks.homeplugin.warps.WarpData;

import java.util.UUID;

public final class Input {
    private UUID playerUUID;
    private InputsSession session;
    private WarpData warpData;

    public Input(UUID playerUUID, InputsSession session, WarpData warpData) {
        this.playerUUID = playerUUID;
        this.session = session;
        this.warpData = warpData;
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }

    public InputsSession getSession() {
        return session;
    }

    public WarpData getWarpData() {
        return warpData;
    }
}
