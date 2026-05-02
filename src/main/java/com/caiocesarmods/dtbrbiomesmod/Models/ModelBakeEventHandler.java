package com.caiocesarmods.dtbrbiomesmod.Models;

import com.caiocesarmods.dtbrbiomesmod.BrazillianBiomesDTAddon;
import com.caiocesarmods.dtbrbiomesmod.Models.baked_models.PalmFrondsBakedModel;
import com.caiocesarmods.dtbrbiomesmod.Models.baked_models.SeasonalIpeLeavesModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber(modid = BrazillianBiomesDTAddon.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModelBakeEventHandler {

    @SubscribeEvent
    public static void onModelRegistryEvent(ModelRegistryEvent event) {
        ModelLoaderRegistry.registerLoader(new ResourceLocation(BrazillianBiomesDTAddon.MOD_ID, "palm_fronds"),
                new PalmFrondsModelLoader(1));
        ModelLoaderRegistry.registerLoader(new ResourceLocation(BrazillianBiomesDTAddon.MOD_ID, "medium_palm_fronds"),
                new PalmFrondsModelLoader(2));
    }

    @SubscribeEvent
    public static void onModelBake(ModelBakeEvent event) {
        // Setup fronds models
        PalmFrondsBakedModel.INSTANCES.forEach(PalmFrondsBakedModel::setupModels);

        event.getModelRegistry().forEach((location, model) -> {

            String path = location.getPath();

            // 👉 Only affect ipê leaves
            if (!path.contains("ipe")) return;

            String speciesKey = getSpeciesKey(path);
            if (speciesKey == null) return;

            String blossomPath = BLOSSOM_TEXTURES.get(speciesKey);
            String dormantPath = DORMANT_TEXTURES.get(speciesKey);

            if (blossomPath == null || dormantPath == null) return;

            TextureAtlasSprite blossom = getSprite(blossomPath);
            TextureAtlasSprite dormant = getSprite(dormantPath);

            IBakedModel seasonal = new SeasonalIpeLeavesModel(model, blossom, dormant);

            event.getModelRegistry().put(location, seasonal);
        });
    }

    private static TextureAtlasSprite getSprite(String path) {
        return Minecraft.getInstance()
                .getModelManager()
                .getAtlasTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE)
                .getSprite(new ResourceLocation(path));
    }

    private static String getSpeciesKey(String path) {

        if (path.contains("ipe_yellow")) return "ipe_yellow";
        if (path.contains("ipe_pink")) return "ipe_pink";

        return null;
    }

    private static final Map<String, String> BLOSSOM_TEXTURES = new HashMap<>();
    private static final Map<String, String> DORMANT_TEXTURES = new HashMap<>();

    static {
        // 🌼 Ipê Amarelo (Yellow)
        BLOSSOM_TEXTURES.put("ipe_yellow", "brbiomesmod:block/yellow_ipe_blossom");
        DORMANT_TEXTURES.put("ipe_yellow", "brbiomesmod:block/ipe_dried_branches");

        // 🌸 Ipê Rosa (Pink)
        BLOSSOM_TEXTURES.put("ipe_pink", "brbiomesmod:block/pink_ipe_blossom");
        DORMANT_TEXTURES.put("ipe_pink", "brbiomesmod:block/ipe_dried_branches");

        // 🤍 Ipê Branco (White)
        BLOSSOM_TEXTURES.put("ipe_white", "brbiomesmod:block/white_ipe_blossom");
        DORMANT_TEXTURES.put("ipe_white", "brbiomesmod:block/yellow_ipe_blossom");

        // 💜 Ipê Roxo (Purple)
        BLOSSOM_TEXTURES.put("ipe_purple", "brbiomesmod:block/purple_ipe_blossom");
        DORMANT_TEXTURES.put("ipe_purple", "brbiomesmod:block/yellow_ipe_blossom");
    }
}
