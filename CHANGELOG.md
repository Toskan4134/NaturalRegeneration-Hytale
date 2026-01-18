# Changelog

All notable changes to this project will be documented in this file.

## [1.1.0] - 2025-01-18

### Added

#### Health Cap
- Added configurable health cap to limit regeneration
- Supports absolute values (`80` = won't heal above 80 HP)
- Supports percentage values (`80%` = won't heal above 80% of max HP)
- New command: `/nr healthcap <value>` to configure in-game
- Set to `none` or empty to disable (default behavior)

#### Update Checker
- Automatically checks for new versions from GitHub and CurseForge
- Runs on server startup and every 12 hours while running
- Logs update notifications to console when new version is available
- Notifies operators (players with `*` permission) when they join
- Can be disabled via `CheckForUpdates` config option

### New Configuration Options

| Option | Default | Description |
|--------|---------|-------------|
| `HealthCap` | `""` | Max HP to regenerate to (absolute or percentage) |
| `CheckForUpdates` | `true` | Enable/disable update checking |

### New Commands

| Command | Description |
|---------|-------------|
| `/nr healthcap <value>` | Set health cap (`80`, `80%`, or `none`) |

---

## [1.0.0] - 2025-01-17

### Added
- Initial release
- Automatic health regeneration after configurable delay
- Configurable regeneration amount and interval
- In-game commands for configuration (`/nr`)
- Persistent JSON configuration
- Optimized for server performance
