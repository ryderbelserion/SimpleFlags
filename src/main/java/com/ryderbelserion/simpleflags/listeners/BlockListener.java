package com.ryderbelserion.simpleflags.listeners;

import com.ryderbelserion.simpleflags.SimpleFlags;
import com.ryderbelserion.simpleflags.flags.FlagManager;
import com.ryderbelserion.simpleflags.flags.builders.StateFlagBuilder;
import com.ryderbelserion.simpleflags.flags.enums.CustomFlags;
import com.sk89q.worldguard.protection.flags.StateFlag;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class BlockListener implements Listener {

    private final SimpleFlags plugin = JavaPlugin.getPlugin(SimpleFlags.class);

    private final FlagManager flagManager = this.plugin.getFlagManager();

    @EventHandler
    public void onBlockPlace(final BlockPlaceEvent event) {
        final Block block = event.getBlock();

        final Material material = block.getType();

        if (material != Material.CRAFTER) return;

        final String flagName = CustomFlags.CRAFTER_BLOCK_FLAG.getName();

        if (!this.flagManager.hasFlag(flagName)) return;

        final StateFlagBuilder flag = (StateFlagBuilder) this.flagManager.getFlag(flagName);

        if (!flag.isEnabled()) return;

        final StateFlag stateFlag = flag.getFlag();

        if (stateFlag == null) return;

        if (!flag.testLocation(event.getPlayer(), stateFlag)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInteractEvent(final PlayerInteractEvent event) {
        final Block block = event.getClickedBlock();

        if (block == null || block.isEmpty()) return;

        final Material material = block.getType();

        if (material != Material.CRAFTER) return;

        final String flagName = CustomFlags.CRAFTER_INTERACTION_FLAG.getName();

        if (!this.flagManager.hasFlag(flagName)) return;

        final StateFlagBuilder flag = (StateFlagBuilder) this.flagManager.getFlag(flagName);

        if (!flag.isEnabled()) return;

        final StateFlag stateFlag = flag.getFlag();

        if (stateFlag == null) return;

        if (!flag.testLocation(event.getPlayer(), stateFlag)) {
            event.setCancelled(true);
        }
    }
}
