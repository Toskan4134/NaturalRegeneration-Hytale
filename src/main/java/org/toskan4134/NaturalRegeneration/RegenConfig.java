package org.toskan4134.NaturalRegeneration;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;

/**
 * Configuration for the natural regeneration plugin.
 * Uses Hytale's Codec system for JSON serialization.
 */
public class RegenConfig {

    // Codec definition for serialization/deserialization
    public static final BuilderCodec<RegenConfig> CODEC = BuilderCodec.builder(RegenConfig.class, RegenConfig::new)
            .append(new KeyedCodec<>("Enabled", Codec.BOOLEAN),
                    (config, value, info) -> config.enabled = value,
                    (config, info) -> config.enabled)
            .add()
            .append(new KeyedCodec<>("DelaySeconds", Codec.FLOAT),
                    (config, value, info) -> config.delaySeconds = value,
                    (config, info) -> config.delaySeconds)
            .add()
            .append(new KeyedCodec<>("AmountHP", Codec.FLOAT),
                    (config, value, info) -> config.amountHP = value,
                    (config, info) -> config.amountHP)
            .add()
            .append(new KeyedCodec<>("IntervalSeconds", Codec.FLOAT),
                    (config, value, info) -> config.intervalSeconds = value,
                    (config, info) -> config.intervalSeconds)
            .add()
            .append(new KeyedCodec<>("HealthCap", Codec.STRING),
                    (config, value, info) -> config.healthCap = value,
                    (config, info) -> config.healthCap)
            .add()
            .append(new KeyedCodec<>("CheckForUpdates", Codec.BOOLEAN),
                    (config, value, info) -> config.checkForUpdates = value,
                    (config, info) -> config.checkForUpdates)
            .add()
            .build();

    // Configuration values with defaults
    private boolean enabled = true;
    private float delaySeconds = 10.0f;
    private float amountHP = 1.0f;
    private float intervalSeconds = 1.0f;
    private String healthCap = ""; // Empty means no cap. Can be "80" (absolute) or "80%" (percentage)
    private boolean checkForUpdates = true;

    public RegenConfig() {
    }

    // Getters
    public boolean isEnabled() {
        return enabled;
    }

    public float getDelaySeconds() {
        return delaySeconds;
    }

    public long getDelayMs() {
        return (long) (delaySeconds * 1000);
    }

    public float getAmountHP() {
        return amountHP;
    }

    public float getIntervalSeconds() {
        return intervalSeconds;
    }

    public long getIntervalMs() {
        return (long) (intervalSeconds * 1000);
    }

    // Setters
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setDelaySeconds(float delaySeconds) {
        this.delaySeconds = Math.max(0, delaySeconds);
    }

    public void setAmountHP(float amountHP) {
        this.amountHP = Math.max(0.1f, amountHP);
    }

    public void setIntervalSeconds(float intervalSeconds) {
        this.intervalSeconds = Math.max(0.1f, intervalSeconds);
    }

    // Health Cap getters and setters
    public String getHealthCap() {
        return healthCap;
    }

    public void setHealthCap(String healthCap) {
        this.healthCap = healthCap != null ? healthCap.trim() : "";
    }

    /**
     * Checks if health cap is enabled (non-empty value).
     */
    public boolean hasHealthCap() {
        return healthCap != null && !healthCap.isEmpty();
    }

    /**
     * Checks if the health cap is a percentage value.
     */
    public boolean isHealthCapPercentage() {
        return healthCap != null && healthCap.endsWith("%");
    }

    /**
     * Gets the effective health cap value for a given max health.
     * Returns Float.MAX_VALUE if no cap is set.
     *
     * @param maxHealth the entity's maximum health
     * @return the effective health cap
     */
    public float getEffectiveHealthCap(float maxHealth) {
        if (!hasHealthCap()) {
            return Float.MAX_VALUE;
        }

        try {
            if (isHealthCapPercentage()) {
                // Parse percentage (e.g., "80%" -> 80)
                String percentStr = healthCap.substring(0, healthCap.length() - 1);
                float percent = Float.parseFloat(percentStr);
                return (percent / 100.0f) * maxHealth;
            } else {
                // Parse absolute value (e.g., "80" -> 80)
                return Float.parseFloat(healthCap);
            }
        } catch (NumberFormatException e) {
            // Invalid format, no cap
            return Float.MAX_VALUE;
        }
    }

    // Update checker getters and setters
    public boolean isCheckForUpdates() {
        return checkForUpdates;
    }

    public void setCheckForUpdates(boolean checkForUpdates) {
        this.checkForUpdates = checkForUpdates;
    }

    public void toggle() {
        this.enabled = !this.enabled;
    }
}
