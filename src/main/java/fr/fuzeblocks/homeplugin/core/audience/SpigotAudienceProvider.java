package fr.fuzeblocks.homeplugin.core.audience;

import fr.fuzeblocks.homeplugin.HomePlugin;import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.entity.Player;

public class SpigotAudienceProvider implements AudienceProvider {
    private final BukkitAudiences bukkit;

    public SpigotAudienceProvider(HomePlugin plugin) {
        this.bukkit = BukkitAudiences.create(plugin);
    }

    @Override
    public Audience player(Player player) {
        return bukkit.player(player);
    }


}