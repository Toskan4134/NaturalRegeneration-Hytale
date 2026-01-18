package org.toskan4134.NaturalRegeneration;

import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.tick.EntityTickingSystem;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.modules.entitystats.EntityStatMap;
import com.hypixel.hytale.server.core.modules.entitystats.EntityStatValue;
import com.hypixel.hytale.server.core.modules.entitystats.EntityStatsModule;
import com.hypixel.hytale.server.core.modules.entitystats.asset.DefaultEntityStatTypes;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.core.util.Config;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * System that passively regenerates health for players.
 * Only regenerates if enough time has passed since last damage.
 * Optimized to run only on players and minimize allocations.
 */
public class RegenerationTickSystem extends EntityTickingSystem<EntityStore> {

    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();

    private final Config<RegenConfig> config;

    // Cache for health index to avoid looking it up every tick
    private int cachedHealthIndex = Integer.MIN_VALUE;

    // Time accumulator per entity to control interval
    private final Map<Integer, Float> timeAccumulators = new ConcurrentHashMap<>();

    public RegenerationTickSystem(Config<RegenConfig> config) {
        this.config = config;
    }

    @Nonnull
    @Override
    public Query<EntityStore> getQuery() {
        // Only players that have EntityStatMap
        return Query.and(Player.getComponentType(), EntityStatMap.getComponentType());
    }

    @Override
    public boolean isParallel(int archetypeChunkSize, int taskCount) {
        // Don't run in parallel to avoid race conditions
        return false;
    }

    @Override
    public void tick(float dt,
                     int entityIndex,
                     @Nonnull ArchetypeChunk<EntityStore> chunk,
                     @Nonnull Store<EntityStore> store,
                     @Nonnull CommandBuffer<EntityStore> commandBuffer) {

        RegenConfig cfg = config.get();

        // Check if system is enabled
        if (!cfg.isEnabled()) {
            return;
        }

        // Accumulate time for this entity
        float accumulated = timeAccumulators.getOrDefault(entityIndex, 0f) + dt;
        float intervalSec = cfg.getIntervalSeconds();

        // Only process if interval has passed
        if (accumulated < intervalSec) {
            timeAccumulators.put(entityIndex, accumulated);
            return;
        }

        // Reset accumulator (keep excess for precision)
        timeAccumulators.put(entityIndex, accumulated - intervalSec);

        // Cache health index
        if (cachedHealthIndex == Integer.MIN_VALUE) {
            cachedHealthIndex = DefaultEntityStatTypes.getHealth();
            if (cachedHealthIndex == Integer.MIN_VALUE) {
                return;
            }
        }

        // Get entity stat map
        EntityStatMap statMap = chunk.getComponent(entityIndex, EntityStatsModule.get().getEntityStatMapComponentType());
        if (statMap == null) {
            return;
        }

        // Get health value
        EntityStatValue healthValue = statMap.get(cachedHealthIndex);
        if (healthValue == null) {
            return;
        }

        float currentHealth = healthValue.get();
        float maxHealth = healthValue.getMax();

        // Calculate effective health cap
        float healthCap = cfg.getEffectiveHealthCap(maxHealth);
        // Cap cannot exceed max health
        float effectiveCap = Math.min(healthCap, maxHealth);

        // If already at or above the effective cap, do nothing
        if (currentHealth >= effectiveCap) {
            return;
        }

        // Check if entity can regenerate (using delay from config)
        if (!DamageTracker.canRegenerate(entityIndex, System.currentTimeMillis(), cfg.getDelayMs())) {
            return;
        }

        // Calculate regeneration amount, capping at the effective cap
        float regenAmount = cfg.getAmountHP();
        float newHealth = currentHealth + regenAmount;
        if (newHealth > effectiveCap) {
            regenAmount = effectiveCap - currentHealth;
        }

        // Only regenerate if there's actually something to heal
        if (regenAmount <= 0) {
            return;
        }

        // Apply regeneration
        statMap.addStatValue(cachedHealthIndex, regenAmount);

        LOGGER.atFine().log("Regenerating " + regenAmount + " HP to player " + entityIndex +
                " (" + healthValue.get() + "/" + maxHealth + ")" +
                (cfg.hasHealthCap() ? " [cap: " + cfg.getHealthCap() + "]" : ""));
    }
}
