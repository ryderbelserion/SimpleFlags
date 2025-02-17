package com.ryderbelserion.simpleflags.flags.types;

import com.ryderbelserion.simpleflags.config.impl.Config;
import com.ryderbelserion.simpleflags.flags.builders.StringFlagBuilder;
import com.ryderbelserion.simpleflags.flags.enums.CustomFlags;
import com.sk89q.worldguard.protection.flags.CommandStringFlag;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.SetFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import java.util.logging.Level;

@SuppressWarnings("unchecked")
public class BlockFlag extends StringFlagBuilder {

    private SetFlag<String> flag;

    @Override
    public void register() {
        if (!isEnabled()) return;

        try {
            getRegistry().register(this.flag = new SetFlag<>("prevent-block-placement", new CommandStringFlag(null)));
        } catch (FlagConflictException exception) {
            Flag<?> existingFlag = getRegistry().get("prevent-block-placement");

            if (existingFlag != null) {
                this.flag = (SetFlag<String>) getRegistry().get("prevent-block-placement");

                return;
            }

            this.plugin.getLogger().log(Level.WARNING, "An error has occurred registering " + getName() + " flag.", exception);
        }
    }

    @Override
    public String getName() {
        return CustomFlags.PREVENT_BLOCK_PLACEMENT.getName();
    }

    @Override
    public SetFlag<String> getFlag() {
        return this.flag;
    }

    @Override
    public boolean isEnabled() {
        return this.config.getProperty(Config.drowning_flag);
    }
}