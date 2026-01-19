package com.ryderbelserion.simpleflags.flags.types;

import com.ryderbelserion.simpleflags.config.impl.Config;
import com.ryderbelserion.simpleflags.flags.builders.StateFlagBuilder;
import com.ryderbelserion.simpleflags.flags.enums.CustomFlags;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;

public class DrownFlag extends StateFlagBuilder {

    private StateFlag flag;

    @Override
    public void register() {
        if (!isEnabled()) return;

        try {
            getRegistry().register(this.flag = new StateFlag("prevent-drowning", true));
        } catch (FlagConflictException exception) {
            Flag<?> existingFlag = getRegistry().get("prevent-drowning");

            if (existingFlag instanceof StateFlag stateFlag) {
                this.flag = stateFlag;

                return;
            }

            this.logger.warn("An error has occurred registering {} flag. Reason: {}", getName(), exception.getMessage());
        }
    }

    @Override
    public String getName() {
        return CustomFlags.DROWN_FLAG.getName();
    }

    @Override
    public StateFlag getFlag() {
        return this.flag;
    }

    @Override
    public boolean isEnabled() {
        return this.config.getProperty(Config.drowning_flag);
    }
}