# Natural Regeneration

Un mod para servidores Hytale que regenera pasivamente la salud del jugador después de no recibir daño durante un período de tiempo configurable.

## Características

- Regeneración automática de la salud tras un período de retraso.
- Totalmente configurable mediante comandos del juego.
- Configuración persistente guardada en JSON.
- Optimizado para el rendimiento del servidor.

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

**Alias:** `/naturalregeneration`, `/naturalregen`, `/nr`

## Configuración

La configuración se guarda automáticamente en `Server/mods/Toskan4134_NaturalRegeneration/NaturalRegeneration.json`.

| Opción | Predeterminado | Descripción |
|--------|---------|-------------|
| `Enabled` | `true` | Si la regeneración está activa |
| `DelaySeconds` | `10.0` | Segundos tras el daño antes de que comience la regeneración |
| `AmountHP` |  `1.0` | HP restaurados por cada tick de regeneración |
| `IntervalSeconds` | `1.0` | Segundos entre ticks de regeneración |

### Ejemplo de configuración

```json
{
    "Enabled": true,
    "DelaySeconds": 10.0,
    "AmountHP": 1.0,
    "IntervalSeconds": 1.0
}
```

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