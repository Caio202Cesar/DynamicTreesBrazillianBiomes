package com.caiocesarmods.dtbrbiomesmod.Models;

import com.caiocesarmods.dtbrbiomesmod.BrazillianBiomesDTAddon;
import com.caiocesarmods.dtbrbiomesmod.Models.baked_models.PalmFrondsBakedModel;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

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
    }
}
