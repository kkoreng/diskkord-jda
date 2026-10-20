# Diskkord

A lightweight framework for [JDA](https://github.com/discord-jda/JDA) that takes care of registering
commands, listeners and interactions, so your bot code only describes what each one does.

```java
Diskkord.builder(JDABuilder.createDefault(token))
        .scan("com.example.bot")
        .build();
```

## ✨ Features

- **Slash commands** with subcommands, subcommand groups and autocomplete
- **Context menu commands** for messages and users
- **Text commands** with aliases
- **Buttons, select menus and modals** routed by custom ID, with arguments carried in the ID
- **Annotation scanning** or manual registration
- **Guild commands** for instant updates while developing

## 📋 Requirements

- Java 21 or newer
- JDA 6 (included as a dependency)

## 📦 Installation

**Gradle (Kotlin)**

```kotlin
dependencies {
    implementation("com.kkoreng:diskkord:0.1.0")
}
```

**Maven**

```xml
<dependency>
    <groupId>com.kkoreng</groupId>
    <artifactId>diskkord</artifactId>
    <version>0.1.0</version>
</dependency>
```

JDA comes with Diskkord, so you don't need to add it separately.
Add a logging backend such as `logback-classic` if you want to see logs.

## 🚀 Quick start

```java
public class Bot {
    public static void main(String[] args) {
        JDABuilder jda = JDABuilder.createDefault(System.getenv("DISCORD_TOKEN"));

        Diskkord.builder(jda)
                .scan("com.example.bot")
                .build();
    }
}
```

```java
@RegisterCommand
public class PingCommand implements SlashCommand {

    @Override
    public SlashCommandData data() {
        return Commands.slash("ping", "Shows the gateway ping");
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        event.reply("Pong! " + event.getJDA().getGatewayPing() + "ms").queue();
    }
}
```

That's it. `/ping` is registered with Discord once the bot is ready.

## 🗂️ Registering

There are two ways to register commands, handlers and listeners. You can mix them.

**Scanning** registers every annotated class in a package and its subpackages.
Scanned classes need a no-args constructor.

| Annotation | Use it on |
|---|---|
| `@RegisterCommand` | `SlashCommand`, `TextCommand`, `MessageContextCommand`, `UserContextCommand` |
| `@RegisterListener` | Interaction handlers and JDA `EventListener`s |

**Manual registration** is for classes that need constructor arguments.

```java
Diskkord.builder(jda)
        .scan("com.example.bot")
        .register(new ShopCommand(productRepository))
        .register(new BuyButtonHandler(productRepository))
        .build();
```

Don't annotate a class that you also register manually, or it will be registered twice.

## ⚡ Slash commands

### Subcommands

Return subcommands from `subcommands()` or `groups()`. Diskkord adds them to the command data
and routes each one to its own class.

```java
@RegisterCommand
public class FruitCommand implements SlashCommand {

    @Override
    public SlashCommandData data() {
        return Commands.slash("fruit", "Fruit commands");
    }

    @Override
    public List<SubCommand> subcommands() {
        return List.of(new Pick(), new ListAll());
    }

    static class Pick implements SubCommand {
        @Override
        public SubcommandData data() {
            return new SubcommandData("pick", "Picks a fruit")
                    .addOption(OptionType.STRING, "name", "Fruit name", true, true);
        }

        @Override
        public void execute(SlashCommandInteractionEvent event) {
            event.reply("You picked " + event.getOption("name").getAsString()).queue();
        }

        @Override
        public void autocomplete(CommandAutoCompleteInteractionEvent event) {
            event.replyChoices(new Command.Choice("apple", "apple")).queue();
        }
    }

    static class ListAll implements SubCommand {
        @Override
        public SubcommandData data() {
            return new SubcommandData("list", "Lists every fruit");
        }

        @Override
        public void execute(SlashCommandInteractionEvent event) {
            event.reply("apple, banana, cherry").queue();
        }
    }
}
```

A command that only has subcommands doesn't need to implement `execute()`.

### Dynamic options

For values that change at runtime, such as products in a database, use autocomplete
and look the value up again in `execute()`, since users can type any value.

## 🖱️ Context menu commands

Shown when right-clicking a message or a user, under **Apps**.

```java
@RegisterCommand
public class QuoteMessage implements MessageContextCommand {

    @Override
    public String name() {
        return "Quote Message";
    }

    @Override
    public void execute(MessageContextInteractionEvent event) {
        event.reply("> " + event.getTarget().getContentDisplay()).queue();
    }
}
```

Use `UserContextCommand` for the user menu. Override `data()` to set permissions or localizations.

## 💬 Text commands

```java
@RegisterCommand
public class HelpCommand implements TextCommand {

    @Override
    public String name() {
        return "!help";
    }

    @Override
    public String[] aliases() {
        return new String[] { "!h" };
    }

    @Override
    public void execute(MessageReceivedEvent event) {
        event.getMessage().reply("Here to help!").queue();
    }
}
```

Names and aliases are matched case-insensitively against the first word of a message,
so include your prefix in the name. Messages from bots are ignored.
Text commands need the `MESSAGE_CONTENT` intent.

## 🔘 Buttons, select menus and modals

Implement a handler for each component type and give it an ID.

| Component | Handler |
|---|---|
| Button | `ButtonInteractionHandler` |
| String select menu | `StringSelectInteractionHandler` |
| Entity select menu | `EntitySelectInteractionHandler` |
| Modal | `ModalInteractionHandler` |

Values you need later can be carried in the custom ID. Build it with `InteractionHandler.idOf`
and read the values back from `Args`.

```java
@RegisterListener
public class CounterButton implements ButtonInteractionHandler {

    public static final String ID = "counter";

    @Override
    public String id() {
        return ID;
    }

    @Override
    public void handle(ButtonInteractionEvent event, Args args) {
        int value = args.getInt(0) + 1;
        event.editMessage("Count: " + value)
                .setComponents(ActionRow.of(button(value)))
                .queue();
    }

    public static Button button(int value) {
        return Button.primary(InteractionHandler.idOf(ID, value), "+1"); // "counter:3"
    }
}
```

```java
event.reply("Count: 0").setComponents(ActionRow.of(CounterButton.button(0))).queue();
```

Since the state lives in the message, buttons keep working after the bot restarts.

`Args` provides `getString`, `getInt`, `getLong`, `getBoolean`, `getEnum`, `size` and `isEmpty`.

**Custom ID rules**

- A handler ID cannot contain `:`, which separates the ID from its arguments.
- Arguments cannot contain `:` or be `null`. Enums are stored by name.
- The whole custom ID must be 100 characters or fewer.

## 👂 Event listeners

Any JDA `EventListener` or `ListenerAdapter` can be registered the same way.

```java
@RegisterListener
public class WelcomeListener extends ListenerAdapter {

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        // ...
    }
}
```

## 🏠 Guild commands

Global commands can take a while to show up. While developing, register every command
in your test servers instead, where changes appear immediately.

```java
Diskkord.builder(jda)
        .guilds(TEST_GUILD_ID)
        .scan("com.example.bot")
        .build();
```

Without `guilds(...)`, commands are registered globally.

## ⚠️ Known limitations

- Diskkord replaces the bot's application commands on startup, so commands registered elsewhere
  are removed.
- When switching from guild commands back to global commands, the old guild commands stay in those
  servers until removed.
- Text commands have no prefix setting or argument parsing yet.
- Exceptions thrown by handlers are logged by JDA; there is no error hook yet.
- `ShardManager` and `AnnotatedEventManager` are not supported.

## 🤝 Contributing

Contributions are very welcome! Whether it's a bug report, a feature idea, documentation
or code, every bit helps.

- **Found a bug or have an idea?** [Open an issue](https://github.com/kkoreng/diskkord-jda/issues).
- **Want to contribute code?** Fork the repository, create a branch, and open a pull request.
  Please run `./gradlew build` before submitting.

Not sure where to start? Feel free to open an issue and ask.

## 📄 License

Diskkord is released under the [MIT License](LICENSE).
