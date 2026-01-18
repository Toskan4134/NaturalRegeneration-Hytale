package org.toskan4134.NaturalRegeneration;

import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.util.Config;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Natural health regeneration plugin.
 * Passively regenerates health after a period of time since last damage received.
 *
 * Commands:
 *   /naturalregeneration - View configuration
 *   /nr toggle - Enable/disable
 *   /nr delay <sec> - Configure delay
 *   /nr amount <hp> - Configure amount
 *   /nr interval <sec> - Configure interval
 */
public class NaturalRegenerationPlugin extends JavaPlugin {

    private static final long UPDATE_CHECK_INTERVAL_HOURS = 12;

    private final Config<RegenConfig> config;
    @Nullable
    private VersionChecker versionChecker;
    @Nullable
    private PlayerJoinListener playerJoinListener;
    @Nullable
    private ScheduledExecutorService updateCheckScheduler;
    @Nullable
    private ScheduledFuture<?> updateCheckTask;

    public NaturalRegenerationPlugin(@Nonnull JavaPluginInit init) {
        super(init);
        this.config = this.withConfig("NaturalRegeneration", RegenConfig.CODEC);
        getLogger().atInfo().log("Starting " + this.getName() + " v" + this.getManifest().getVersion().toString());
    }

    @Override
    protected void setup() {
        getLogger().atInfo().log("Setting up plugin " + this.getName());

        // Save config to create file with defaults if it doesn't exist
        config.save();

        // Register damage listener system
        this.getEntityStoreRegistry().registerSystem(new DamageListenerSystem());

        // Register regeneration system with config
        this.getEntityStoreRegistry().registerSystem(new RegenerationTickSystem(config));

        // Register command with config
        this.getCommandRegistry().registerCommand(new NaturalRegenerationCommand(config));

        // Check for updates if enabled (initial check + every 12 hours)
        if (config.get().isCheckForUpdates()) {
            startUpdateChecker();
        }

        getLogger().atInfo().log(this.getName() + " setup complete!");
        getLogger().atInfo().log("Use /naturalregeneration to view and modify configuration");
    }

    /**
     * Starts the update checker with initial check and periodic checks every 12 hours.
     */
    private void startUpdateChecker() {
        // Create scheduler for periodic checks
        updateCheckScheduler = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "NaturalRegeneration-UpdateChecker");
            t.setDaemon(true);
            return t;
        });

        // Run initial check immediately, then every 12 hours
        updateCheckTask = updateCheckScheduler.scheduleAtFixedRate(
                this::checkForUpdates,
                0, // Initial delay
                UPDATE_CHECK_INTERVAL_HOURS,
                TimeUnit.HOURS
        );

        getLogger().atInfo().log("Update checker started (checks every " + UPDATE_CHECK_INTERVAL_HOURS + " hours)");
    }

    /**
     * Checks for plugin updates asynchronously.
     * Logs to console if an update is available and registers
     * a listener to notify operators when they join.
     */
    private void checkForUpdates() {
        String currentVersion = this.getManifest().getVersion().toString();

        versionChecker = new VersionChecker(currentVersion);

        // Check for updates asynchronously
        versionChecker.checkForUpdatesAsync().thenAccept(checker -> {
            if (checker.isUpdateAvailable()) {
                // Log to console
                String consoleMessage = checker.getConsoleMessage();
                if (consoleMessage != null) {
                    getLogger().atWarning().log(consoleMessage);
                }

                // Register player join listener for operator notifications (only once)
                if (playerJoinListener == null) {
                    playerJoinListener = new PlayerJoinListener(checker);
                    playerJoinListener.register(this);
                }
            }
        }).exceptionally(ex -> {
            getLogger().atWarning().log("Failed to check for updates: " + ex.getMessage());
            return null;
        });
    }

    @Override
    protected void shutdown() {
        getLogger().atInfo().log("Shutting down " + this.getName());

        // Stop update checker
        if (updateCheckTask != null) {
            updateCheckTask.cancel(false);
            updateCheckTask = null;
        }
        if (updateCheckScheduler != null) {
            updateCheckScheduler.shutdown();
            updateCheckScheduler = null;
        }

        // Clear data
        DamageTracker.clearAll();

        getLogger().atInfo().log(this.getName() + " shutdown complete!");
    }
}
