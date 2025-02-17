package com.ryderbelserion.simpleflags.flags.builders;

import com.ryderbelserion.simpleflags.flags.FlagBuilder;
import com.sk89q.worldguard.protection.flags.SetFlag;

public abstract class StringFlagBuilder extends FlagBuilder<SetFlag<?>> {

    @Override
    public abstract SetFlag<?> getFlag();

}