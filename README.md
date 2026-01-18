# Natural Regeneration

A Hytale server plugin that passively regenerates player health after not taking damage for a configurable period of time.

## Features

- Automatic health regeneration after a delay period
- Fully configurable via in-game commands
- Persistent configuration saved to JSON
- Optimized for server performance
- **Health Cap** - Limit regeneration to a maximum HP value (absolute or percentage)
- **Update Checker** - Automatically checks GitHub and CurseForge for new versions on startup and every 12 hours, notifies operators when they join

## How It Works

1. When a player takes damage, their regeneration timer resets
2. After the configured delay (default: 10 seconds), health regeneration begins
3. Health regenerates at the configured rate until the player reaches max health or takes damage again

## Commands

| Command | Description |
|---------|-------------|
| `/nr` | Shows help and available subcommands |
| `/nr status` | Display current configuration |
| `/nr toggle` | Enable or disable regeneration |
| `/nr delay <seconds>` | Set delay before regeneration starts |
| `/nr amount <hp>` | Set HP regenerated per tick |
| `/nr interval <seconds>` | Set time between regeneration ticks |
| `/nr healthcap <value>` | Set health cap (`80` for absolute, `80%` for percentage, `none` to disable) |

**Aliases:** `/naturalregeneration`, `/naturalregen`, `/nr`

## Configuration

Configuration is automatically saved to `Server/mods/Toskan4134_NaturalRegeneration/NaturalRegeneration.json`

| Option | Default | Description |
|--------|---------|-------------|
| `Enabled` | `true` | Whether regeneration is active |
| `DelaySeconds` | `10.0` | Seconds after damage before regen starts |
| `AmountHP` | `1.0` | HP restored per regeneration tick |
| `IntervalSeconds` | `1.0` | Seconds between regeneration ticks |
| `HealthCap` | `""` | Max HP to regenerate to (`"80"` for absolute, `"80%"` for percentage, `""` for no cap) |
| `CheckForUpdates` | `true` | Whether to check for plugin updates |

### Example Configuration

```json
{
    "Enabled": true,
    "DelaySeconds": 10.0,
    "AmountHP": 1.0,
    "IntervalSeconds": 1.0,
    "HealthCap": "80%",
    "CheckForUpdates": true
}
```

### Health Cap Examples

- `"80"` - Won't heal above 80 HP (absolute value)
- `"80%"` - Won't heal above 80% of max HP (percentage)
- `""` - No cap, heal to full health (default)

### Update Checker

The plugin automatically checks for updates from GitHub and CurseForge:
- Checks on server startup
- Checks every 12 hours while the server is running
- Logs to console when a new version is available
- Notifies operators (players with `*` permission) when they join

## Installation

1. Build the plugin JAR file
2. Place the JAR in your server's `mods` folder
3. Start/restart the server
4. Configure using in-game commands or edit the config file

## Building

```bash
./gradlew build
```

The compiled JAR will be located in `build/libs/`

## License

MIT License

## Author

Toskan4134
