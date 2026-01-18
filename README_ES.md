# Natural Regeneration

Un mod para servidores Hytale que regenera pasivamente la salud del jugador después de no recibir daño durante un período de tiempo configurable.

## Características

- Regeneración automática de la salud tras un período de retraso.
- Totalmente configurable mediante comandos del juego.
- Configuración persistente guardada en JSON.
- Optimizado para el rendimiento del servidor.
- **Límite de Salud** - Limita la regeneración a un valor máximo de HP (absoluto o porcentaje)
- **Verificador de Actualizaciones** - Comprueba automáticamente GitHub y CurseForge en busca de nuevas versiones al iniciar y cada 12 horas, notifica a los operadores cuando se conectan

## Cómo funciona

1. Cuando un jugador recibe daño, se reinicia su temporizador de regeneración.
2. Tras el retraso configurado (por defecto: 10 segundos), comienza la regeneración de salud.
3. La salud se regenera a la velocidad configurada hasta que el jugador alcanza la salud máxima o vuelve a recibir daño.

## Comandos

| Comando | Descripción |
|---------|-------------|
| `/nr` | Muestra la ayuda y los subcomandos disponibles |
| `/nr status` | Muestra la configuración actual |
| `/nr toggle` | Activa o desactiva la regeneración |
| `/nr delay <segundos>` | Establece el retraso antes de que comience la regeneración |
| `/nr amount <hp>` | Establece los puntos de salud regenerados por tick |
| `/nr interval <segundos>` | Establece el tiempo entre ticks de regeneración |
| `/nr healthcap <valor>` | Establece el límite de salud (`80` absoluto, `80%` porcentaje, `none` para desactivar) |

**Alias:** `/naturalregeneration`, `/naturalregen`, `/nr`

## Configuración

La configuración se guarda automáticamente en `Server/mods/Toskan4134_NaturalRegeneration/NaturalRegeneration.json`.

| Opción | Predeterminado | Descripción |
|--------|---------|-------------|
| `Enabled` | `true` | Si la regeneración está activa |
| `DelaySeconds` | `10.0` | Segundos tras el daño antes de que comience la regeneración |
| `AmountHP` |  `1.0` | HP restaurados por cada tick de regeneración |
| `IntervalSeconds` | `1.0` | Segundos entre ticks de regeneración |
| `HealthCap` | `""` | HP máximo a regenerar (`"80"` absoluto, `"80%"` porcentaje, `""` sin límite) |
| `CheckForUpdates` | `true` | Si se comprueba actualizaciones del plugin |

### Ejemplo de configuración

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

### Ejemplos de Límite de Salud

- `"80"` - No curará por encima de 80 HP (valor absoluto)
- `"80%"` - No curará por encima del 80% del HP máximo (porcentaje)
- `""` - Sin límite, cura hasta la salud máxima (predeterminado)

### Verificador de Actualizaciones

El plugin comprueba automáticamente actualizaciones desde GitHub y CurseForge:
- Comprueba al iniciar el servidor
- Comprueba cada 12 horas mientras el servidor está en ejecución
- Registra en la consola cuando hay una nueva versión disponible
- Notifica a los operadores (jugadores con permiso `*`) cuando se conectan

## Instalación

1. Compila el archivo JAR del complemento.
2. Coloca el JAR en la carpeta `mods` de tu servidor.
3. Inicia o reinicia el servidor.
4. Configura mediante comandos del juego o edita el archivo de configuración.

## Compilación

```bash
./gradlew build
```

El JAR compilado se encontrará en `build/libs/`.

## Licencia

Licencia MIT

## Autor

Toskan4134