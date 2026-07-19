package com.ryderbelserion.simpleflags.paper.api.registry.adapters;

import com.ryderbelserion.fusion.core.api.FusionKey;
import com.ryderbelserion.fusion.core.api.config.properties.PropertyManager;
import com.ryderbelserion.fusion.core.api.registry.message.MessageRegistry;
import com.ryderbelserion.fusion.paper.FusionPaper;
import com.ryderbelserion.simpleflags.paper.SimpleFlags;
import com.ryderbelserion.simpleflags.paper.config.impl.ConfigManager;
import com.ryderbelserion.simpleflags.paper.config.impl.types.RootKeys;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PaperSenderAdapter {

    private final SimpleFlags platform = JavaPlugin.getPlugin(SimpleFlags.class);

    private final FusionPaper fusion = this.platform.getFusion();

    private final ConfigManager configManager = this.platform.getConfigManager();

    private final PropertyManager pluginConfig = this.configManager.getConfig();

    private final MessageRegistry messageRegistry = this.platform.getMessageRegistry();

    public void sendActionBar(@NotNull final CommandSender sender, @NotNull final FusionKey id, @NotNull final Map<String, String> placeholders) {
        final Component component = getComponent(sender, id, placeholders);

        if (component.equals(Component.empty())) {
            return;
        }

        if (sender instanceof ConsoleCommandSender) {
            sender.sendMessage(component);

            return;
        }

        sender.sendActionBar(component);
    }

    public void sendMessage(@NotNull final CommandSender sender, @NotNull final FusionKey id, @NotNull final Map<String, String> placeholders) {
        final Component component = getComponent(sender, id, placeholders);

        if (component.equals(Component.empty())) {
            return;
        }

        sender.sendMessage(component);
    }

    public Component getComponent(@NotNull final CommandSender sender, @NotNull final FusionKey id, @NotNull final Map<String, String> placeholders) {
        final List<String> values = new ArrayList<>();

        this.messageRegistry.getMessage(id).ifPresent(value -> values.add(value.getValue()));

        if (values.isEmpty()) {
            return Component.empty();
        }

        final String value = values.getFirst();

        if (value.isEmpty()) {
            return Component.empty();
        }

        final Map<String, String> map = new HashMap<>(placeholders);

        final String prefix = this.pluginConfig.getProperty(RootKeys.command_prefix);

        if (!prefix.isEmpty()) {
            map.putIfAbsent("{prefix}", prefix);
        }

        return this.fusion.asComponent(sender, values.getFirst(), map);
    }

    public String getMessage(@NotNull final CommandSender sender, @NotNull final FusionKey id, @NotNull final Map<String, String> placeholders) {
        final List<String> values = new ArrayList<>();

        this.messageRegistry.getMessage(id).ifPresent(value -> values.add(value.getValue()));

        if (values.isEmpty()) {
            return "";
        }

        final String value = values.getFirst();

        if (value.isEmpty()) {
            return "";
        }

        final Map<String, String> map = new HashMap<>(placeholders);

        final String prefix = this.pluginConfig.getProperty(RootKeys.command_prefix);

        if (!prefix.isEmpty()) {
            map.putIfAbsent("{prefix}", prefix);
        }

        return this.fusion.replacePlaceholders(this.fusion.papi(sender, value), map);
    }
}