package com.ryderbelserion.simpleflags.listeners;

import com.ryderbelserion.simpleflags.SimpleFlags;
import com.ryderbelserion.simpleflags.flags.FlagManager;
import com.ryderbelserion.simpleflags.flags.builders.StringFlagBuilder;
import com.ryderbelserion.simpleflags.flags.enums.CustomFlags;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.SetFlag;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.Set;

@SuppressWarnings("unchecked")
public class BlockListener implements Listener {

    private final SimpleFlags plugin = JavaPlugin.getPlugin(SimpleFlags.class);

    private final FlagManager flagManager = this.plugin.getFlagManager();

    @EventHandler(ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        final String flagName = CustomFlags.PREVENT_BLOCK_PLACEMENT.getName();

        if (!this.flagManager.hasFlag(flagName)) return;

        final StringFlagBuilder builder = (StringFlagBuilder) this.flagManager.getFlag(flagName);

        if (!builder.isEnabled()) return;

        final SetFlag<String> flag = (SetFlag<String>) builder.getFlag();

        if (flag == null) return;

        final Block block = event.getBlock();

        if (block.isEmpty()) return;

        final String material = block.getType().getKey().value();

        this.plugin.getLogger().warning("Block placement for " + material);

        final Set<String> materialTypes = builder.getQuery().queryValue(BukkitAdapter.adapt(block.getLocation()), WorldGuardPlugin.inst().wrapPlayer(event.getPlayer()), flag);

        if (materialTypes != null && materialTypes.contains(material)) {
            this.plugin.getLogger().warning("Block placement for " + material + " cancelled.");

            event.setCancelled(true);
        }
    }
}