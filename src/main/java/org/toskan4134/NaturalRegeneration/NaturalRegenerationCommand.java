package org.toskan4134.NaturalRegeneration;

import com.hypixel.hytale.protocol.GameMode;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractCommandCollection;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.util.Config;

import javax.annotation.Nonnull;

/**
 * Command collection to configure the NaturalRegeneration plugin.
 *
 * Usage:
 *   /nr status             - Shows current configuration
 *   /nr toggle             - Enable/disable regeneration
 *   /nr delay <seconds>    - Configure delay before regenerating
 *   /nr amount <hp>        - Configure HP amount per tick
 *   /nr interval <seconds> - Configure interval between regenerations
 */
public class NaturalRegenerationCommand extends AbstractCommandCollection {

    public NaturalRegenerationCommand(Config<RegenConfig> config) {
        super("naturalregeneration", "Configure the Natural Regeneration plugin");
        this.setPermissionGroup(GameMode.Adventure);
        this.addAliases("naturalregen", "nr");

        // Add subcommands
        this.addSubCommand(new StatusCommand(config));
        this.addSubCommand(new ToggleCommand(config));
        this.addSubCommand(new DelayCommand(config));
        this.addSubCommand(new AmountCommand(config));
        this.addSubCommand(new IntervalCommand(config));
        this.addSubCommand(new HealthCapCommand(config));
    }

    // Status subcommand - shows current configuration
    private static class StatusCommand extends CommandBase {
        private final Config<RegenConfig> config;

        public StatusCommand(Config<RegenConfig> config) {
            super("status", "Show current configuration");
            this.config = config;
        }

        @Override
        protected void executeSync(@Nonnull CommandContext ctx) {
            RegenConfig cfg = config.get();
            StringBuilder msg = new StringBuilder();
            msg.append("=== Natural Regeneration ===\n\n");
            msg.append("Status: ").append(cfg.isEnabled() ? "ENABLED" : "DISABLED").append("\n");
            msg.append("Delay: ").append(cfg.getDelaySeconds()).append(" sec\n");
            msg.append("Amount: ").append(cfg.getAmountHP()).append(" HP\n");
            msg.append("Interval: ").append(cfg.getIntervalSeconds()).append(" sec\n");
            msg.append("Health Cap: ").append(cfg.hasHealthCap() ? cfg.getHealthCap() : "None").append(" HP");
            ctx.sendMessage(Message.raw(msg.toString()));
        }
    }

    // Toggle subcommand
    private static class ToggleCommand extends CommandBase {
        private final Config<RegenConfig> config;

        public ToggleCommand(Config<RegenConfig> config) {
            super("toggle", "Toggle regeneration on/off");
            this.config = config;
        }

        @Override
        protected void executeSync(@Nonnull CommandContext ctx) {
            RegenConfig cfg = config.get();
            cfg.toggle();
            config.save();
            ctx.sendMessage(Message.raw("Regeneration: " + (cfg.isEnabled() ? "ENABLED" : "DISABLED")));
        }
    }

    // Delay subcommand
    private static class DelayCommand extends CommandBase {
        private final Config<RegenConfig> config;
        private final RequiredArg<Float> secondsArg;

        public DelayCommand(Config<RegenConfig> config) {
            super("delay", "Set delay before regeneration starts");
            this.config = config;
            this.secondsArg = this.withRequiredArg("seconds", "Delay in seconds", ArgTypes.FLOAT);
        }

        @Override
        protected void executeSync(@Nonnull CommandContext ctx) {
            Float seconds = ctx.get(secondsArg);
            if (seconds != null) {
                config.get().setDelaySeconds(seconds);
                config.save();
                ctx.sendMessage(Message.raw("Delay set to " + seconds + " seconds"));
            }
        }
    }

    // Amount subcommand
    private static class AmountCommand extends CommandBase {
        private final Config<RegenConfig> config;
        private final RequiredArg<Float> hpArg;

        public AmountCommand(Config<RegenConfig> config) {
            super("amount", "Set HP regenerated per tick");
            this.config = config;
            this.hpArg = this.withRequiredArg("hp", "HP amount", ArgTypes.FLOAT);
        }

        @Override
        protected void executeSync(@Nonnull CommandContext ctx) {
            Float hp = ctx.get(hpArg);
            if (hp != null) {
                config.get().setAmountHP(hp);
                config.save();
                ctx.sendMessage(Message.raw("Amount set to " + hp + " HP per tick"));
            }
        }
    }

    // Interval subcommand
    private static class IntervalCommand extends CommandBase {
        private final Config<RegenConfig> config;
        private final RequiredArg<Float> secondsArg;

        public IntervalCommand(Config<RegenConfig> config) {
            super("interval", "Set interval between regeneration ticks");
            this.config = config;
            this.secondsArg = this.withRequiredArg("seconds", "Interval in seconds", ArgTypes.FLOAT);
        }

        @Override
        protected void executeSync(@Nonnull CommandContext ctx) {
            Float seconds = ctx.get(secondsArg);
            if (seconds != null) {
                config.get().setIntervalSeconds(seconds);
                config.save();
                ctx.sendMessage(Message.raw("Interval set to " + seconds + " seconds"));
            }
        }
    }

    // Health Cap subcommand
    private static class HealthCapCommand extends CommandBase {
        private final Config<RegenConfig> config;
        private final RequiredArg<String> capArg;

        public HealthCapCommand(Config<RegenConfig> config) {
            super("healthcap", "Set health cap (e.g., '80' for absolute or '80%' for percentage, 'none' to disable)");
            this.config = config;
            this.capArg = this.withRequiredArg("cap", "Health cap value (number, percentage, or 'none')", ArgTypes.STRING);
        }

        @Override
        protected void executeSync(@Nonnull CommandContext ctx) {
            String cap = ctx.get(capArg);
            if (cap != null) {
                if (cap.equalsIgnoreCase("none") || cap.equalsIgnoreCase("off") || cap.equals("0")) {
                    config.get().setHealthCap("");
                    config.save();
                    ctx.sendMessage(Message.raw("Health cap disabled"));
                } else {
                    // Validate the format
                    String testValue = cap.endsWith("%") ? cap.substring(0, cap.length() - 1) : cap;
                    try {
                        float value = Float.parseFloat(testValue);
                        if (value <= 0) {
                            ctx.sendMessage(Message.raw("Health cap must be a positive value"));
                            return;
                        }
                        config.get().setHealthCap(cap);
                        config.save();
                        ctx.sendMessage(Message.raw("Health cap set to " + cap +
                                (cap.endsWith("%") ? " (percentage of max health)" : " HP")));
                    } catch (NumberFormatException e) {
                        ctx.sendMessage(Message.raw("Invalid format. Use a number (e.g., '80') or percentage (e.g., '80%')"));
                    }
                }
            }
        }
    }
}
