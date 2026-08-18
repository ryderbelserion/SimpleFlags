package com.ryderbelserion.simpleflags.paper.api;

import com.ryderbelserion.fusion.paper.FusionPaper;
import com.ryderbelserion.fusion.paper.builders.commands.PaperCommand;
import com.ryderbelserion.simpleflags.paper.SimpleFlags;
import com.ryderbelserion.simpleflags.paper.config.impl.ConfigManager;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class SimpleFlagsCommand extends PaperCommand {

    protected final SimpleFlags platform = JavaPlugin.getPlugin(SimpleFlags.class);

    protected final ConfigManager configManager = this.platform.getConfigManager();

    protected final FusionPaper fusion = this.platform.getFusion();

}