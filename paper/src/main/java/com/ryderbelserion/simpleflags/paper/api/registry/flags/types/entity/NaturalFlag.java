package com.ryderbelserion.simpleflags.paper.api.registry.flags.types.entity;

import com.ryderbelserion.simpleflags.paper.config.impl.types.RootKeys;
import com.ryderbelserion.simpleflags.paper.api.registry.flags.builders.StateFlagBuilder;
import com.ryderbelserion.simpleflags.paper.api.registry.flags.enums.CustomFlags;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;

public class NaturalFlag extends StateFlagBuilder {

    private StateFlag flag;

    @Override
    public void register() {
        if (!isEnabled()) return;

        try {
            getRegistry().register(this.flag = new StateFlag("prevent-natural-spawning", true));
        } catch (FlagConflictException exception) {
            Flag<?> existingFlag = getRegistry().get("prevent-natural-spawning");

            if (existingFlag instanceof StateFlag stateFlag) {
                this.flag = stateFlag;

                return;
            }

            this.logger.warn("An error has occurred registering {} flag. Reason: {}", getName(), exception.getMessage());
        }
    }

    @Override
    public String getName() {
        return CustomFlags.NATURAL_FLAG.getName();
    }

    @Override
    public StateFlag getFlag() {
        return this.flag;
    }

    @Override
    public boolean isEnabled() {
        return this.config.getProperty(RootKeys.is_natural_spawning_flag_enabled);
    }
}