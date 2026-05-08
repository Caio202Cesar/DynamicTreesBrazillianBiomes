package com.caiocesarmods.dtbrbiomesmod.systems;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.resources.IResource;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

public class SeasonalLeafReloadListener {

    private static final Gson GSON =
            new GsonBuilder().setPrettyPrinting().create();

    public static void reload(IResourceManager manager) {

        SeasonalLeafRegistry.clear();

        try {

            Collection<ResourceLocation> resources =
                    manager.getAllResourceLocations(
                            "seasonal_leaves",
                            path -> path.endsWith(".json")
                    );

            for (ResourceLocation resource : resources) {

                try {

                    IResource iResource =
                            manager.getResource(resource);

                    BufferedReader reader =
                            new BufferedReader(
                                    new InputStreamReader(
                                            iResource.getInputStream(),
                                            StandardCharsets.UTF_8
                                    )
                            );

                    SeasonalLeafConfig config =
                            GSON.fromJson(
                                    reader,
                                    SeasonalLeafConfig.class
                            );

                    SeasonalLeafRegistry.register(config);

                    reader.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
