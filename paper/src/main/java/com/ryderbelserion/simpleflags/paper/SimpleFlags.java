package com.ryderbelserion.simpleflags.paper;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.ryderbelserion.fusion.core.api.registry.message.MessageRegistry;
import com.ryderbelserion.fusion.core.files.FileManager;
import com.ryderbelserion.fusion.paper.FusionPaper;
import com.ryderbelserion.simpleflags.paper.api.registry.flags.types.other.FlyFlag;
import com.ryderbelserion.simpleflags.paper.commands.BaseCommand;
import com.ryderbelserion.simpleflags.paper.commands.types.ReloadCommand;
import com.ryderbelserion.simpleflags.paper.config.impl.ConfigManager;
import com.ryderbelserion.simpleflags.paper.api.registry.flags.FlagManager;
import com.ryderbelserion.simpleflags.paper.api.registry.flags.types.damage.DrownFlag;
import com.ryderbelserion.simpleflags.paper.api.registry.flags.types.entity.NaturalFlag;
import com.ryderbelserion.simpleflags.paper.api.registry.flags.types.blocks.BlockCrafterFlag;
import com.ryderbelserion.simpleflags.paper.api.registry.flags.types.blocks.InteractionCrafterFlag;
import com.ryderbelserion.simpleflags.paper.listeners.block.CrafterListener;
import com.ryderbelserion.simpleflags.paper.listeners.damage.DrowningListener;
import com.ryderbelserion.simpleflags.paper.listeners.entity.NaturalSpawnListener;
import com.ryderbelserion.simpleflags.paper.api.registry.adapters.PaperSenderAdapter;
import com.ryderbelserion.simpleflags.paper.listeners.other.FlightStatusListener;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jspecify.annotations.NonNull;
import java.util.List;

public class SimpleFlags extends JavaPlugin {

    public static final String namespace = "simpleflags";

    private MessageRegistry messageRegistry;
    private ConfigManager configManager;
    private FileManager fileManager;
    private FusionPaper fusion;

    private PaperSenderAdapter adapter;
    private FlagManager flagManager;
    private WorldGuard worldGuard;

    @Override
    public void onLoad() {
        this.fusion = new FusionPaper(this);
        this.fusion.init();

        this.messageRegistry = this.fusion.getMessageRegistry();
        this.fileManager = this.fusion.getFileManager();

        this.configManager = new ConfigManager();
        this.configManager.init();

        this.adapter = new PaperSenderAdapter();

        this.configManager.loadMessages();

        this.worldGuard = WorldGuard.getInstance();

        this.flagManager = new FlagManager();

        List.of(
                // damage flags
                new DrownFlag(),

                // entity flags
                new NaturalFlag(),

                // status flags
                new FlyFlag(),

                // block flags
                new InteractionCrafterFlag(),
                new BlockCrafterFlag()
        ).forEach(this.flagManager::addFlag);
    }

    @Override
    public void onEnable() {
        if (this.fusion != null) {
            this.fusion.post();
        }

        final PluginManager pluginManager = getServer().getPluginManager();

        List.of(
                // entity listeners
                new NaturalSpawnListener(),

                // damage listeners
                new DrowningListener(),

                // status listeners
                new FlightStatusListener(),

                // crafter listeners
                new CrafterListener()
        ).forEach(event -> pluginManager.registerEvents(event, this));

        final LifecycleEventManager<Plugin> eventManager = getLifecycleManager();

        // Register commands.
        eventManager.registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            LiteralArgumentBuilder<CommandSourceStack> root = new BaseCommand().registerPermissions().literal().createBuilder();

            List.of(
                    new ReloadCommand()
            ).forEach(command -> root.then(command.registerPermissions().literal()));

            event.registrar().register(root.build(), "The base command for SimpleFlags!");
        });
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    public @NonNull PaperSenderAdapter getSenderAdapter() {
        return this.adapter;
    }

    public @NonNull MessageRegistry getMessageRegistry() {
        return this.messageRegistry;
    }

    public @NonNull ConfigManager getConfigManager() {
        return this.configManager;
    }

    public @NonNull RegionContainer getRegions() {
        return this.worldGuard.getPlatform().getRegionContainer();
    }

    public @NonNull FlagManager getFlagManager() {
        return this.flagManager;
    }

    public @NonNull FileManager getFileManager() {
        return this.fileManager;
    }

    public @NonNull WorldGuard getWorldGuard() {
        return this.worldGuard;
    }

    public @NonNull FusionPaper getFusion() {
        return this.fusion;
    }
}