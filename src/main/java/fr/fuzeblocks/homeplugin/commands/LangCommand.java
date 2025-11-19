package fr.fuzeblocks.homeplugin.commands;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.language.LanguageManager;
import fr.fuzeblocks.homeplugin.language.LanguageMerge;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

/**
 * Command for managing language files.
 * Supports update, merge, and set operations.
 */
public class LangCommand implements CommandExecutor {

    private final Plugin plugin;
    private static final String LANG = "LangCommand.";
    private static final LanguageManager languageManager = HomePlugin.getLanguageManager();

    /**
     * Instantiates a new Lang command.
     *
     * @param plugin the plugin
     */
    public LangCommand(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (args.length < 1) {
            sender.sendMessage(languageManager.getStringWithColor(LANG + "Lang-usage-message"));
            return true;
        }

        String sub = args[0].toLowerCase();

        switch (sub) {
            case "update":
                return handleUpdate(sender, args);

            case "merge":
                return handleMerge(sender, args);

            case "set":
                return handleSet(sender, args);

            default:
                sender.sendMessage(languageManager.getStringWithColor(LANG + "Lang-unknown-subcommand"));
                return true;
        }
    }

    /**
     * Handles the update subcommand.
     *
     * @param sender The command sender
     * @param args Command arguments
     * @return true always
     */
    private boolean handleUpdate(CommandSender sender, String[] args) {
        if (args.length != 1) {
            sender.sendMessage(languageManager.getStringWithColor(LANG + "Lang-update-usage"));
            return true;
        }

        String langName = languageManager.getLanguage().name().toLowerCase();
        File oldFile = new File(plugin.getDataFolder(), langName + ".yml");

        if (!oldFile.exists()) {
            sender.sendMessage(languageManager.getStringWithColor(LANG + "Lang-file-not-exist")
                    .replace("%file%", oldFile.getName()));
            return true;
        }

        File backupFile = new File(plugin.getDataFolder(), langName + "_backup.yml");
        if (backupFile.exists() && !backupFile.delete()) {
            sender.sendMessage(languageManager.getStringWithColor(LANG + "Lang-backup-delete-fail"));
            return true;
        }

        if (!oldFile.renameTo(backupFile)) {
            sender.sendMessage(languageManager.getStringWithColor(LANG + "Lang-backup-fail"));
            return true;
        }

        if (!languageManager.regenerate()) {
            sender.sendMessage(languageManager.getStringWithColor(LANG + "Lang-regeneration-fail"));
            if (!backupFile.renameTo(oldFile)) {
                sender.sendMessage(languageManager.getStringWithColor(LANG + "Lang-restore-fail"));
            }
            return true;
        }

        File newFile = new File(plugin.getDataFolder(), langName + ".yml");

        if (!newFile.exists()) {
            sender.sendMessage(languageManager.getStringWithColor(LANG + "Lang-new-file-missing")
                    .replace("%file%", newFile.getName()));
            if (!backupFile.renameTo(oldFile)) {
                sender.sendMessage(languageManager.getStringWithColor(LANG + "Lang-restore-fail"));
            }
            return true;
        }

        LanguageMerge merger = new LanguageMerge(backupFile, newFile);
        merger.mergeAddOnly();

        if (!merger.pushTo(newFile)) {
            sender.sendMessage(languageManager.getStringWithColor(LANG + "Lang-merge-error"));
            return true;
        }

        sender.sendMessage(languageManager.getStringWithColor(LANG + "Lang-merge-success"));
        return true;
    }

    /**
     * Handles the merge subcommand.
     *
     * @param sender The command sender
     * @param args Command arguments
     * @return true always
     */
    private boolean handleMerge(CommandSender sender, String[] args) {
        if (args.length < 3) {
            sender.sendMessage(languageManager.getStringWithColor(LANG + "Lang-merge-usage"));
            return true;
        }

        File editedFile = new File(plugin.getDataFolder(), args[1]);
        File latestFile = new File(plugin.getDataFolder(), args[2]);

        if (!editedFile.exists()) {
            sender.sendMessage(languageManager.getStringWithColor(LANG + "Lang-merge-edited-missing")
                    .replace("%file%", args[1]));
            return true;
        }

        if (!latestFile.exists()) {
            sender.sendMessage(languageManager.getStringWithColor(LANG + "Lang-merge-latest-missing")
                    .replace("%file%", args[2]));
            return true;
        }

        LanguageMerge merger = new LanguageMerge(editedFile, latestFile);
        merger.mergeAddOnly();

        if (merger.pushChanges()) {
            sender.sendMessage(languageManager.getStringWithColor(LANG + "Lang-merge-saved"));
        } else {
            sender.sendMessage(languageManager.getStringWithColor(LANG + "Lang-merge-fail"));
        }
        return true;
    }

    /**
     * Handles the set subcommand.
     *
     * @param sender The command sender
     * @param args Command arguments
     * @return true always
     */
    private boolean handleSet(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(languageManager.getStringWithColor(LANG + "Lang-set-usage"));
            return true;
        }

        String newLang = args[1].toUpperCase();

        File configFile = new File(plugin.getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            sender.sendMessage(languageManager.getStringWithColor(LANG + "Lang-config-missing"));
            return true;
        }

        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        config.set("Config.Language", newLang);

        try {
            config.save(configFile);
            plugin.reloadConfig();
            sender.sendMessage(languageManager.getStringWithColor(LANG + "Lang-set-success")
                    .replace("%lang%", newLang));
        } catch (IOException e) {
            sender.sendMessage(languageManager.getStringWithColor(LANG + "Lang-config-save-fail"));
            e.printStackTrace();
        }
        return true;
    }
}