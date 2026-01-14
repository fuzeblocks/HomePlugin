package fr.fuzeblocks.homeplugin.metrics;

import fr.fuzeblocks.homeplugin.language.Language;
import fr.fuzeblocks.homeplugin.plugin.HomePlugin;
import fr.fuzeblocks.homeplugin.sync.SyncMethod;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SimplePie;

public class MetricsPlugin implements HomePlugin {

    private final Metrics metrics = fr.fuzeblocks.homeplugin.HomePlugin.getMetrics();


    @Override
    public String getName() {
        return "Metrics-Internal";
    }

    @Override
    public String getVersion() {
        return "0.1";
    }

    @Override
    public String getAuthor() {
        return "fuzeblocks (Author)";
    }

    @Override
    public String[] getAuthors() {
        return new String[0];
    }

    @Override
    public void initialize() {
        final Language resolvedLanguage = fr.fuzeblocks.homeplugin.HomePlugin.getLanguageManager().getLanguage();
        metrics.addCustomChart(new SimplePie("used_language", resolvedLanguage::name));
        metrics.addCustomChart(new SimplePie("storage_type", () -> {
            if (fr.fuzeblocks.homeplugin.HomePlugin.getRegistrationType().equals(SyncMethod.MYSQL)) {
                return "MySQL";
            } else {
                return "Yaml";
            }
        }));
    }

    @Override
    public void stop() {

    }
}
