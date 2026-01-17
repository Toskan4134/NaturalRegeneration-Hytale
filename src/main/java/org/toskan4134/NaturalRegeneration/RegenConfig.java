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
            .build();

    // Configuration values with defaults
    private boolean enabled = true;
    private float delaySeconds = 10.0f;
    private float amountHP = 1.0f;
    private float intervalSeconds = 1.0f;

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

    public void toggle() {
        this.enabled = !this.enabled;
    }
}
