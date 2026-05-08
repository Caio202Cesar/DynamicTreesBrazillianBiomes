package com.caiocesarmods.dtbrbiomesmod.systems;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

import java.util.Map;

public class SeasonalLeafReloadListener extends JsonReloadListener {

    private static final Gson GSON =
            new GsonBuilder().create();

    public SeasonalLeafReloadListener() {

        super(
                GSON,
                "seasonal_leaves"
        );
    }

    @Override
    protected void apply(
            Map<ResourceLocation, JsonElement> object,
            IResourceManager resourceManager,
            IProfiler profiler
    ) {

        SeasonalLeafRegistry.clear();

        object.forEach((location, jsonElement) -> {

            try {

                JsonObject json =
                        jsonElement.getAsJsonObject();

                SeasonalLeafConfig config =
                        GSON.fromJson(
                                json,
                                SeasonalLeafConfig.class
                        );

                SeasonalLeafRegistry.register(config);

            } catch (Exception e) {

                e.printStackTrace();
            }
        });
    }
}
