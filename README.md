# Lootable Bodies Reanimated

A Minecraft 1.7.10 fork of DrCyano's Lootable Bodies. The runtime mod ID remains
`lootablebodies` for compatibility with existing worlds.

## Requirements

- Minecraft 1.7.10 and Forge 10.13.4.1614
- Java 8 JDK for development

## Development

Use the included Gradle 4.4.1 wrapper with a Java 8 JDK. The build uses the
anatawa12 ForgeGradle 1.2 fork. Set `JAVA_HOME` to the JDK before running:

```text
gradlew.bat clean --refresh-dependencies
gradlew.bat setupDecompWorkspace
gradlew.bat build
gradlew.bat runClient
gradlew.bat runServer
```

Gradle places the production JAR in `build/libs/` and uses `run/` for development
launches. Set the release version in `version.properties`.
The development server creates `run/eula.txt` on its first launch; accept the
Minecraft EULA there before starting a local server.

## License

The original source tree includes the CC0 1.0 Universal dedication in [LICENSE](LICENSE).
