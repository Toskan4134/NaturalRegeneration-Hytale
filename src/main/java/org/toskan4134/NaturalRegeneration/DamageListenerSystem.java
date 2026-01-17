package org.toskan4134.NaturalRegeneration;

import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.modules.entity.damage.Damage;
import com.hypixel.hytale.server.core.modules.entity.damage.DamageEventSystem;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nullable;

/**
 * ECS system that listens for damage events to track when entities receive damage.
 * Extends DamageEventSystem to receive Damage events.
 */
public class DamageListenerSystem extends DamageEventSystem {

    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();

    @Nullable
    @Override
    public Query<EntityStore> getQuery() {
        return Query.any();
    }

    @Override
    public void handle(int entityIndex,
                       ArchetypeChunk<EntityStore> chunk,
                       Store<EntityStore> store,
                       CommandBuffer<EntityStore> commandBuffer,
                       Damage damage) {

        // Get damage info
        float damageAmount = damage.getAmount();

        if (damageAmount <= 0) {
            return; // Not real damage
        }

        // Record damage in tracker
        // entityIndex is the index of the entity that received damage
        DamageTracker.onDamageReceived(entityIndex, damageAmount);

        LOGGER.atFine().log("Entity " + entityIndex + " received " + damageAmount + " damage");
    }
}
