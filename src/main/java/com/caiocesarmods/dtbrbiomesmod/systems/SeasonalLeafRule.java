package com.caiocesarmods.dtbrbiomesmod.systems;

import net.minecraft.util.ResourceLocation;

public class SeasonalLeafRule {

    public float peak_season = 2.5F;

    // How wide the transition band is
    public float fade = 0.5F;

    public float temp_min = -10F;
    public float temp_max = 10F;

    // Base probability multiplier
    public float chance = 1.0F;

    public ResourceLocation leaves;

}
