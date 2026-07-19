package com.ryderbelserion.simpleflags.paper.api.registry.flags;

import com.ryderbelserion.fusion.core.api.config.properties.PropertyManager;
import com.ryderbelserion.simpleflags.paper.SimpleFlags;
import com.ryderbelserion.simpleflags.paper.config.impl.ConfigManager;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class FlagBuilder<T extends Flag> {

    protected final SimpleFlags plugin = JavaPlugin.getPlugin(SimpleFlags.class);

    protected final ComponentLogger logger = this.plugin.getComponentLogger();

    protected final ConfigManager configManager = this.plugin.getConfigManager();

    protected final PropertyManager config = this.configManager.getConfig();

    public abstract void register();

    public abstract String getName();

    public abstract T getFlag();

    public abstract boolean isEnabled();

    /**
     * @return A query of regions.
     */
    protected RegionQuery getQuery() {
        return this.plugin.getRegions().createQuery();
    }

    /**
     * @return The flag registry.
     */
    protected FlagRegistry getRegistry() {
        return this.plugin.getWorldGuard().getFlagRegistry();
    }

}