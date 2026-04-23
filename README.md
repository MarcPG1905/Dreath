# Dreath

is an ultra-realistic survival game, with horrible performance, but a lot of realism and accuracy.
It is currently just a project to learn Kotlin's deeper features like co-routines and just extend my knowledge about game development and concurrency in general.

## How to Compile & Use

To compile the game or any other sub-modules, or directly run the game, you will need to use Gradle.
If you do not have Gradle installed on your system, you can use the provided Gradle script (`./gradlew` on UNIX, `gradlew.bat` on Windows).

### Downloading the Code

To compile and use, or directly run the game, you will first need to clone this repository onto your computer. To do that, I recommend just using the GitHub Desktop app or the IDE of your choice, as they provide a very simple interface, without any manual commands required.

If you do want to use the CLI, you can do this using the `git` command:

```bash
# Clone the Repository (requires git to be installed):
git clone https://github.com/MarcPG1905/Dreath

# Change Directory to the Downloaded Files:
cd Dreath
```

### Compiling

To compile any module, you can use this command and replace `<module>` with the module you want to compile, like `game`, or `mod-example`:

```bash
gradle <module>:clean <module>:build
```

This will clean the build output and then compile a game JAR archive in `./<module>/build/libs/`, which you can then run using an installed `kotlin` or `java -jar` command.
If you want to run the game more easily, you can also use the `./util/launch-game.sh`, which will automatically decide which command to use.

### Running Directly

To run the game directly without needing to compile and use a JAR archive, you can use this command:

```bash
gradle game:runWithDebug
```

This will automatically run the source code and also support debugging it in an IDE like IntelliJ IDEA.
The running directory will also be set to `./game/run/`, to make it more organized.

> [!NOTE]
> Please keep in mind that this only works for the `game` sub-module.
> Other modules like the `mod-example` or `api` do not support this and should be compiled manually.

## Contact

### Discord

You can join my Discord community and just ping me for a quick response: https://discord.gg/HvWhqY3kRG
You can also send me a direct message on Discord: `@marcpg1905`

### Direct Contact

If you don't use Discord or would like to contact me otherwise, please rely on one of these methods:

- E-Mail: [me@marcpg.com](mailto:me@marcpg.com)

## For Developers

### Used Libraries

Dreath uses these libraries:

- [LibPG](https://github.com/MarcPG1905/LibPG) - Various features, like downloads, time formats, json storage, etc.
- [clikt](https://github.com/ajalt/clikt) - CLI argument parsing with a lot of options and works very well with Kotlin.
- [kool](https://github.com/kool-engine/kool) - A Kotlin game engine with Vulkan support.
- [KPresence](https://github.com/vyfor/KPresence) - Custom Discord rich presence which just works.
- Kotlin Extensions:
  - [kotlinx.serialization](https://github.com/Kotlin/kotlinx.serialization) - Kotlin extension for working with JSON and some other formats.
  - [kotlinx.coroutines](https://github.com/Kotlin/kotlinx.coroutines) - Kotlin extension for working with different threads.
  - [kotlinx-datetime](https://github.com/Kotlin/kotlinx-datetime) - Kotlin extension for working with time formats more easily than the default JVM variants.
