package com.caiocesarmods.dtbrbiomesmod.systems;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.server.ServerWorld;

public enum Climate {
    TROPICAL,
    SUBTROPICAL,
    TEMPERATE,
    BOREAL,
    TUNDRA;

    public static String getClimate(ServerWorld worldIn, BlockPos pos) {

        Biome biome = worldIn.getBiome(pos);
        float temp = biome.getTemperature(pos);

            if (temp < 0.2F) {
                return "TUNDRA";
            } else if (temp < 0.4F) {
                return "BOREAL";
            } else if (temp < 0.8F){
                return "TEMPERATE";
            } else if (temp < 0.9F) {
                return "SUBTROPICAL";
            } else return "TROPICAL";
    }

}
