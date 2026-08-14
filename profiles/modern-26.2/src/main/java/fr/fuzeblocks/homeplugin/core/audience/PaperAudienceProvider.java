package fr.fuzeblocks.homeplugin.core.audience;


import net.kyori.adventure.audience.Audience;
import org.bukkit.entity.Player;

public class PaperAudienceProvider implements AudienceProvider {
    @Override
    public Audience player(Player player) {
        return player;
    }

}