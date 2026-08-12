package fr.fuzeblocks.homeplugin.core.audience;

import net.kyori.adventure.audience.Audience;
import org.bukkit.entity.Player;

public interface AudienceProvider {
    Audience player(Player player);
}