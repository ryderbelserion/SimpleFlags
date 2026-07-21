package com.ryderbelserion.simpleflags.paper.config.impl;

import com.ryderbelserion.fusion.core.api.FusionKey;
import com.ryderbelserion.fusion.core.api.config.ConfigBuilder;
import com.ryderbelserion.fusion.core.api.config.properties.PropertyManager;
import com.ryderbelserion.fusion.core.api.enums.Level;
import com.ryderbelserion.fusion.core.api.registry.message.MessageRegistry;
import com.ryderbelserion.fusion.files.FileManager;
import com.ryderbelserion.fusion.paper.FusionPaper;
import com.ryderbelserion.simpleflags.paper.SimpleFlags;
import com.ryderbelserion.simpleflags.paper.api.enums.messages.Message;
import com.ryderbelserion.simpleflags.paper.config.impl.types.RootKeys;
import com.ryderbelserion.simpleflags.paper.api.enums.FileKeys;
import org.bukkit.plugin.java.JavaPlugin;
import org.jspecify.annotations.NonNull;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.loader.HeaderMode;
import org.spongepowered.configurate.yaml.NodeStyle;
import java.nio.file.Path;
import java.util.List;
import static com.ryderbelserion.simpleflags.paper.SimpleFlags.namespace;

public final class ConfigManager {

    private final SimpleFlags platform = JavaPlugin.getPlugin(SimpleFlags.class);

    private final MessageRegistry registry = this.platform.getMessageRegistry();

    private final FileManager fileManager = this.platform.getFileManager();

    private final FusionPaper fusion = this.platform.getFusion();

    private final Path path = this.platform.getDataPath();

    private PropertyManager config;

    public void init() {
        this.config = ConfigBuilder.withYamlPath(this.path.resolve("config.yml"))
                .configuration(RootKeys.class)
                .withNodeStyle(NodeStyle.BLOCK)
                .withHeaderMode(HeaderMode.PRESERVE)
                .withIndent(1)
                .create();

        for (final FileKeys key : FileKeys.values()) {
            key.load();
        }
    }

    public void loadMessages() {
        final List<Path> paths = this.fileManager.getFilesByPath(this.path.resolve("locale"), ".yml");

        paths.add(this.path.resolve("messages.yml")); // add to list

        this.registry.init(action -> {
            for (final Path path : paths) {
                this.fileManager.getYamlFile(path).ifPresentOrElse(customFile -> {
                    final String fileName = customFile.getFileName();

                    final FusionKey key = FusionKey.key(namespace, fileName.equalsIgnoreCase("messages.yml") ? "default" : fileName.toLowerCase());

                    final CommentedConfigurationNode configuration = customFile.getConfiguration();

                    for (final Message message : Message.values()) {
                        message.addKey(action, configuration, key);
                    }
                }, () -> this.fusion.log(Level.INFO, "Path %s not found in cache.".formatted(path)));
            }
        });
    }

    public void reload() {
        this.config.reload();

        for (final FileKeys key : FileKeys.values()) {
            key.reload();
        }

        loadMessages();
    }

    public @NonNull PropertyManager getConfig() {
        return this.config;
    }
}