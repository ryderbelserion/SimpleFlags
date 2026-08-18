package com.ryderbelserion.simpleflags.paper.api.registry.flags.types.blocks;

import com.ryderbelserion.simpleflags.paper.config.impl.types.RootKeys;
import com.ryderbelserion.simpleflags.paper.api.registry.flags.builders.StateFlagBuilder;
import com.ryderbelserion.simpleflags.paper.api.registry.flags.enums.CustomFlags;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;

public class InteractionCrafterFlag extends StateFlagBuilder {

    private StateFlag flag;

    @Override
    public void register() {
        if (!isEnabled()) return;

        try {
            getRegistry().register(this.flag = new StateFlag("prevent-crafter-interaction", true));
        } catch (FlagConflictException exception) {
            Flag<?> existingFlag = getRegistry().get("prevent-crafter-interaction");

            if (existingFlag instanceof StateFlag stateFlag) {
                this.flag = stateFlag;

                return;
            }

            this.logger.warn("An error has occurred registering {} flag. Reason: {}", getName(), exception.getMessage());
        }
    }

    @Override
    public String getName() {
        return CustomFlags.CRAFTER_INTERACTION_FLAG.getName();
    }

    @Override
    public StateFlag getFlag() {
        return this.flag;
    }

    @Override
    public boolean isEnabled() {
        return this.config.getProperty(RootKeys.is_crafter_interaction_enabled);
    }
}