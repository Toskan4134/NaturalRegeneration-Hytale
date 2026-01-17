package org.toskan4134.NaturalRegeneration;

import com.hypixel.hytale.logger.HytaleLogger;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Utility class that tracks when an entity receives damage.
 * Stores the timestamp of the last damage received for each entity.
 */
public class DamageTracker {

    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();

    // Map storing last damage time by entity index
    private static final Map<Integer, Long> lastDamageTime = new ConcurrentHashMap<>();

    // Maximum time to keep records (10 minutes)
    private static final long MAX_RECORD_AGE_MS = 600_000L;

    // Counter for periodic cleanup
    private static long lastCleanupTime = 0L;
    private static final long CLEANUP_INTERVAL_MS = 60_000L; // Cleanup every minute

    /**
     * Records that an entity has received damage.
     * @param entityIndex Entity index in the ECS
     */
    public static void onDamageReceived(int entityIndex) {
        lastDamageTime.put(entityIndex, System.currentTimeMillis());
        cleanupIfNeeded();
    }

    /**
     * Records that an entity has received damage with amount.
     * @param entityIndex Entity index
     * @param damageAmount Amount of damage received
     */
    public static void onDamageReceived(int entityIndex, float damageAmount) {
        lastDamageTime.put(entityIndex, System.currentTimeMillis());
        cleanupIfNeeded();
    }

    /**
     * Gets the time of the last damage received by an entity.
     * @param entityIndex Entity index
     * @return Timestamp of last damage, or 0 if never damaged
     */
    public static long getLastDamageTime(int entityIndex) {
        return lastDamageTime.getOrDefault(entityIndex, 0L);
    }

    /**
     * Checks if an entity can regenerate health.
     * @param entityIndex Entity index
     * @param currentTimeMs Current time in milliseconds
     * @param delayMs Required delay in milliseconds since last damage
     * @return true if enough time has passed since last damage
     */
    public static boolean canRegenerate(int entityIndex, long currentTimeMs, long delayMs) {
        Long lastDamage = lastDamageTime.get(entityIndex);
        if (lastDamage == null) {
            return true; // Never received damage
        }
        return (currentTimeMs - lastDamage) >= delayMs;
    }

    /**
     * Clears the record for an entity.
     * @param entityIndex Entity index
     */
    public static void clearEntity(int entityIndex) {
        lastDamageTime.remove(entityIndex);
    }

    /**
     * Cleans up old records if enough time has passed.
     */
    private static void cleanupIfNeeded() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastCleanupTime < CLEANUP_INTERVAL_MS) {
            return;
        }
        lastCleanupTime = currentTime;

        // Clean old entries
        Iterator<Map.Entry<Integer, Long>> iterator = lastDamageTime.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, Long> entry = iterator.next();
            if (currentTime - entry.getValue() > MAX_RECORD_AGE_MS) {
                iterator.remove();
            }
        }

        LOGGER.atFine().log("DamageTracker cleanup: " + lastDamageTime.size() + " active entries");
    }

    /**
     * Clears all records.
     */
    public static void clearAll() {
        lastDamageTime.clear();
    }

    /**
     * Gets the number of active entries.
     */
    public static int getActiveCount() {
        return lastDamageTime.size();
    }
}
