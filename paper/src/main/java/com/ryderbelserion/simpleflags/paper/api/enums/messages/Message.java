package com.ryderbelserion.simpleflags.paper.api.enums.messages;

import com.ryderbelserion.fusion.api.objects.FusionKey;
import com.ryderbelserion.fusion.core.api.config.properties.PropertyManager;
import com.ryderbelserion.fusion.core.api.config.properties.objects.enums.PropertyType;
import com.ryderbelserion.fusion.core.api.registry.message.MessageRegistry;
import com.ryderbelserion.fusion.core.api.registry.message.adapter.YamlMessageAdapter;
import com.ryderbelserion.fusion.core.utils.StringUtils;
import com.ryderbelserion.simpleflags.paper.SimpleFlags;
import com.ryderbelserion.simpleflags.paper.config.impl.ConfigManager;
import com.ryderbelserion.simpleflags.paper.config.impl.types.RootKeys;
import com.ryderbelserion.simpleflags.paper.api.registry.adapters.PaperSenderAdapter;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.spongepowered.configurate.CommentedConfigurationNode;
import java.util.List;
import java.util.Map;
import static com.ryderbelserion.simpleflags.paper.SimpleFlags.namespace;

@ApiStatus.Internal
@NullMarked
public enum Message {

    reload_plugin("root.reload-plugin", "{prefix}<red>You have reloaded the plugin.", "root", "reload-plugin"),
    help_menu("root.help-menu", List.of(
            "<red>SimpleFlags Help Menu",
            "",
            "<red>/simpleflags <gray>- <gold>Shows this menu.",
            "<red>/simpleflags reload <gray>- <gold>Reloads the plugin."
    ), "root", "help-menu");

    private final SimpleFlags platform = JavaPlugin.getPlugin(SimpleFlags.class);

    private final ConfigManager configManager = this.platform.getConfigManager();

    private final PropertyManager pluginConfig = this.configManager.getConfig();

    private final PaperSenderAdapter senderAdapter = this.platform.getSenderAdapter();

    private final String defaultValue;
    private final PropertyType type;
    private final Object[] path;
    private final FusionKey id;

    Message(final String id, final String defaultValue, final Object... path) {
        this.defaultValue = defaultValue;
        this.id = FusionKey.key(namespace, id);
        this.type = PropertyType.STRING;
        this.path = path;
    }

    Message(final String id, final List<String> defaultValue, final Object... path) {
        this.defaultValue = StringUtils.toString(defaultValue);
        this.id = FusionKey.key(namespace, id);
        this.type = PropertyType.STRING_LIST;
        this.path = path;
    }

    public void addKey(final MessageRegistry registry, final CommentedConfigurationNode configuration, final FusionKey id) {
        final YamlMessageAdapter adapter = new YamlMessageAdapter(configuration, this.defaultValue, this.path);

        registry.addKey(
                id,
                this.id,
                adapter
        );
    }

    public final List<String> toList(final String value) {
        return value.lines().toList();
    }

    public void sendMessage(final CommandSender sender, final Map<String, String> placeholders) {
        switch (this.pluginConfig.getProperty(RootKeys.message_state)) {
            case send_message -> this.senderAdapter.sendMessage(sender, this.id, placeholders);
            case send_actionbar -> this.senderAdapter.sendActionBar(sender, this.id, placeholders);
        }
    }

    public void sendMessage(final CommandSender sender, final String placeholder, final String value) {
        sendMessage(sender, Map.of(placeholder, value));
    }

    public void sendMessage(final CommandSender sender) {
        sendMessage(sender, Map.of());
    }

    public final FusionKey getKey() {
        return this.id;
    }
}