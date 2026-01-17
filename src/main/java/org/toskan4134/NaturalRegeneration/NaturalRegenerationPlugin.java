package org.toskan4134.NaturalRegeneration;

import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.util.Config;

import javax.annotation.Nonnull;

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

    private final Config<RegenConfig> config;

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

        getLogger().atInfo().log(this.getName() + " setup complete!");
        getLogger().atInfo().log("Use /naturalregeneration to view and modify configuration");
    }

    @Override
    protected void shutdown() {
        getLogger().atInfo().log("Shutting down " + this.getName());

        // Clear data
        DamageTracker.clearAll();

        getLogger().atInfo().log(this.getName() + " shutdown complete!");
    }
}
