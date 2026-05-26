package fr.fuzeblocks.homeplugin.gui.modern;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.core.economy.EconomyManager;
import fr.fuzeblocks.homeplugin.core.home.HomeManager;
import fr.fuzeblocks.homeplugin.core.task.TeleportationManager;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.invui.Click;
import xyz.xenondevs.invui.item.AbstractItem;
import xyz.xenondevs.invui.item.ItemBuilder;
import xyz.xenondevs.invui.item.ItemProvider;

import java.util.List;

public class HomeItem extends AbstractItem {

    private static final String HOME = "Home.";
    private static final LegacyComponentSerializer LEGACY = LegacyComponentSerializer.legacySection();

    private final String homeName;
    private boolean deleted;

    public HomeItem(String homeName) {
        this.homeName = homeName;
    }

    @Override
    public void handleClick(
            @NotNull ClickType clickType,
            @NotNull Player player,
            @NotNull Click click
    ) {
        HomeManager homeManager = HomePlugin.getHomeManager();

        if (!homeManager.exist(player, homeName)) {
            player.sendMessage(HomePlugin.getLanguageManager().getStringWithColor(HOME + "Home-does-not-exist"));
            player.closeInventory();
            return;
        }

        if (clickType.isRightClick()) {
            player.sendMessage(
                    HomePlugin.getLanguageManager()
                            .getStringWithColor(HOME + "Home-deleted-with-name")
                            .replace("{homeName}", homeName)
            );
            homeManager.deleteHome(player, homeName);
            deleted = true;
            notifyWindows();
            return;
        }

        player.closeInventory();
        double cost = EconomyManager.getHomeTeleportPrice();
        if (cost > 0 && !EconomyManager.pay(player, cost)) {
            player.sendMessage(HomePlugin.getLanguageManager().getStringWithColor("Language.Not-Enough-Money"));
            return;
        }

        TeleportationManager.teleportPlayerToHome(player, homeName);
    }

    @Override
    public @NotNull ItemProvider getItemProvider(Player viewer) {
        if (deleted) {
            return new ItemBuilder(Material.AIR);
        }

        String displayName = HomePlugin.getLanguageManager()
                .getStringWithColor(HOME + "Home-item-displayname")
                .replace("{homeName}", homeName);

        String loreTeleport = HomePlugin.getLanguageManager().getStringWithColor(HOME + "Home-item-lore-teleport");
        String loreDelete   = HomePlugin.getLanguageManager().getStringWithColor(HOME + "Home-item-lore-delete");

        return new ItemBuilder(getRandomBedColor())
                .setName(LEGACY.deserialize(displayName))
                .setLore(List.of(
                        LEGACY.deserialize(loreTeleport),
                        LEGACY.deserialize(loreDelete)
                ));
    }

    private Material getRandomBedColor() {
        Material[] bedColors = {
                Material.RED_BED, Material.GREEN_BED, Material.BLUE_BED,
                Material.YELLOW_BED, Material.PINK_BED, Material.BLACK_BED,
                Material.WHITE_BED, Material.ORANGE_BED, Material.CYAN_BED
        };
        return bedColors[(int) (Math.random() * bedColors.length)];
    }
}