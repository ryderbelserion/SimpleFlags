package com.ryderbelserion.simpleflags.paper.commands;

import com.mojang.brigadier.tree.LiteralCommandNode;
import com.ryderbelserion.fusion.kyori.permissions.PermissionContext;
import com.ryderbelserion.fusion.paper.builders.commands.context.PaperCommandContext;
import com.ryderbelserion.simpleflags.paper.api.SimpleFlagsCommand;
import com.ryderbelserion.simpleflags.paper.api.enums.messages.Message;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.command.CommandSender;
import org.jspecify.annotations.NonNull;
import java.util.List;

public class BaseCommand extends SimpleFlagsCommand {

    @Override
    public void run(@NonNull final PaperCommandContext context) {
        Message.help_menu.sendMessage(context.getSender());
    }

    @Override
    public @NonNull final LiteralCommandNode<CommandSourceStack> literal() {
        return Commands.literal("simpleflags").requires(this::requirement).build();
    }

    @Override
    public @NonNull final List<PermissionContext> getPermissions() {
        return List.of(
                new PermissionContext(
                        "simpleflags.help",
                        "Gives access to /simpleflags help."
                )
        );
    }

    @Override
    public final boolean requirement(@NonNull final CommandSourceStack context) {
        boolean hasPermission = false;

        final CommandSender sender = context.getSender();

        for (final PermissionContext permissionContext : getPermissions()) {
            final String permission = permissionContext.getPermission();

            hasPermission = sender.hasPermission(permission);

            if (hasPermission) {
                break;
            }
        }

        return hasPermission;
    }
}