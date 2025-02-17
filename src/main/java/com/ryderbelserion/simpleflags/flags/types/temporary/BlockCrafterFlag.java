package com.ryderbelserion.simpleflags.flags.types.temporary;

import com.ryderbelserion.simpleflags.config.impl.Config;
import com.ryderbelserion.simpleflags.flags.builders.StateFlagBuilder;
import com.ryderbelserion.simpleflags.flags.enums.CustomFlags;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;

import java.util.logging.Level;

public class BlockCrafterFlag extends StateFlagBuilder {

    private StateFlag flag;

    @Override
    public void register() {
        if (!isEnabled()) return;

        try {
            getRegistry().register(this.flag = new StateFlag("prevent-crafter-block", true));
        } catch (FlagConflictException exception) {
            Flag<?> existingFlag = getRegistry().get("prevent-crafter-block");

            if (existingFlag instanceof StateFlag stateFlag) {
                this.flag = stateFlag;

                return;
            }

            this.plugin.getLogger().log(Level.WARNING, "An error has occurred registering " + getName() + " flag.", exception);
        }
    }

    @Override
    public String getName() {
        return CustomFlags.CRAFTER_BLOCK_FLAG.getName();
    }

    @Override
    public StateFlag getFlag() {
        return this.flag;
    }

    @Override
    public boolean isEnabled() {
        return this.config.getProperty(Config.crafter_flag_place);
    }
}