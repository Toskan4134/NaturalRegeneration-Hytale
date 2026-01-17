# Natural Regeneration

A Hytale server plugin that passively regenerates player health after not taking damage for a configurable period of time.

## Features

- Automatic health regeneration after a delay period
- Fully configurable via in-game commands
- Persistent configuration saved to JSON
- Optimized for server performance

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

**Aliases:** `/naturalregeneration`, `/naturalregen`, `/nr`

## Configuration

Configuration is automatically saved to `Server/Plugin/Config/NaturalRegeneration.json`

| Option | Default | Description |
|--------|---------|-------------|
| `Enabled` | `true` | Whether regeneration is active |
| `DelaySeconds` | `10.0` | Seconds after damage before regen starts |
| `AmountHP` | `1.0` | HP restored per regeneration tick |
| `IntervalSeconds` | `1.0` | Seconds between regeneration ticks |

### Example Configuration

```json
{
    "Enabled": true,
    "DelaySeconds": 10.0,
    "AmountHP": 1.0,
    "IntervalSeconds": 1.0
}
```

## Installation

1. Build the plugin JAR file
2. Place the JAR in your server's `plugins` folder
3. Start/restart the server
4. Configure using in-game commands or edit the config file

## Building

```bash
./gradlew build
```

The compiled JAR will be located in `build/libs/`

## Requirements

- Hytale Server (compatible version)

## License

MIT License

## Author

Toskan4134
