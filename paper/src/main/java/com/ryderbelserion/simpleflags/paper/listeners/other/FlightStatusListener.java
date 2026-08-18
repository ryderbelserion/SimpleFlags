package com.ryderbelserion.simpleflags.paper.listeners.other;

import com.ryderbelserion.fusion.paper.FusionPaper;
import com.ryderbelserion.simpleflags.paper.SimpleFlags;
import com.ryderbelserion.simpleflags.paper.api.registry.flags.FlagManager;
import com.ryderbelserion.simpleflags.paper.api.registry.flags.builders.StateFlagBuilder;
import com.ryderbelserion.simpleflags.paper.api.registry.flags.enums.CustomFlags;
import com.sk89q.worldguard.protection.flags.StateFlag;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class FlightStatusListener implements Listener {

    private final SimpleFlags plugin = JavaPlugin.getPlugin(SimpleFlags.class);

    private final FlagManager flagManager = this.plugin.getFlagManager();

    private final FusionPaper fusion = this.plugin.getFusion();

    @EventHandler
    public void onPlayerToggleFlight(final PlayerToggleFlightEvent event) {
        this.flagManager.getByFlag(CustomFlags.FLIGHT_FLAG.getName()).ifPresent(index -> {
            final StateFlagBuilder builder = (StateFlagBuilder) index;

            if (!builder.isEnabled()) return;

            final StateFlag flag = builder.getFlag();

            if (flag == null) return;

            final Player player = event.getPlayer();

            if (!builder.testLocation(player, flag)) {
                event.setCancelled(true);
            }
        });
    }
}