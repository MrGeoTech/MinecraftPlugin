package net.craftingstore.minestom.commands;

import net.craftingstore.minestom.CraftingStoreExtension;
import net.craftingstore.minestom.Utils;
import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.suggestion.SuggestionEntry;

import java.util.concurrent.ExecutionException;

public class CraftingStoreCommand extends Command {

    public CraftingStoreCommand(CraftingStoreExtension instance) {
        super("csb", instance.getCraftingStore().ADMIN_PERMISSION);

        var action = ArgumentType.String("action");

        action.setSuggestionCallback((sender, context, suggestion) -> {
            suggestion.addEntry(new SuggestionEntry("reload"));
            suggestion.addEntry(new SuggestionEntry("key"));
        });

        addSyntax((sender, context) -> {
            if (!sender.hasPermission(instance.getCraftingStore().ADMIN_PERMISSION)) {
                sender.sendMessage(instance.getPrefix().append(Component.text("You don't have the required permission!")));
                return;
            }
            if (context.get(action).equalsIgnoreCase("reload")) {
                instance.getCraftingStore().reload();
                sender.sendMessage(instance.getPrefix().append(Component.text("The plugin is reloading!")));
            } else if (context.get(action).equalsIgnoreCase("debug")) {
                boolean isDebugging = instance.getCraftingStore().getLogger().isDebugging();
                sender.sendMessage(instance.getPrefix().append(Component.text(String.format(
                        "Debug mode is currently %s.",
                        isDebugging ? "enabled" : "disabled"
                ))));
            } else {
                sender.sendMessage(Utils.color("&7&m-----------------------\n")
                                .append(Utils.color("&8>&7 /csb reload&8 -> &7Reload the config.\n"))
                                .append(Utils.color("&8>&7 /csb key <your key>&8 -> &7Update the key.\n"))
                                .append(Utils.color("&7&m-----------------------")));
            }
        }, action);

        var key = ArgumentType.String("arg");

        addSyntax((sender, context) -> {
            if (context.get(action).equalsIgnoreCase("key")) {
                instance.getConfig().setToken(context.get(key));
                instance.getConfigWrapper().saveConfig();
                MinecraftServer.getSchedulerManager().buildTask(() -> {
                    try {
                        if (instance.getCraftingStore().reload().get()) {
                            sender.sendMessage(instance.getPrefix().append(Component.text("The new API key has been set in the config, and the plugin has been reloaded.")));
                        } else {
                            sender.sendMessage(instance.getPrefix().append(Component.text("The API key is invalid. The plugin will not work until you set a valid API key.")));
                        }
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                }).schedule();
            } else if (context.get(action).equalsIgnoreCase("debug")) {
                boolean isDebugging;
                String debugValue = context.get(key).toLowerCase();
                if (debugValue.equalsIgnoreCase("true")) {
                    isDebugging = true;
                } else if (debugValue.equalsIgnoreCase("false")) {
                    isDebugging = false;
                } else {
                    sender.sendMessage(instance.getPrefix().append(Component.text("Unknown debug value.")));
                    return;
                }
                instance.getCraftingStore().getLogger().setDebugging(isDebugging);
                instance.getConfig().setDebug(isDebugging);
                instance.getConfigWrapper().saveConfig();
                sender.sendMessage(instance.getPrefix().append(Component.text(String.format(
                        "Debug mode has been %s.",
                        isDebugging ? "enabled" : "disabled"
                ))));
            }
        }, action, key);
    }

}
