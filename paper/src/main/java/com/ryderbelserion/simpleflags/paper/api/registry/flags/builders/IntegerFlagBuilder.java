package com.ryderbelserion.simpleflags.paper.api.registry.flags.builders;

import com.ryderbelserion.simpleflags.paper.api.registry.flags.FlagBuilder;
import com.sk89q.worldguard.protection.flags.IntegerFlag;

public abstract class IntegerFlagBuilder extends FlagBuilder<IntegerFlag> {

    @Override
    public abstract IntegerFlag getFlag();

}