package com.caiocesarmods.dtbrbiomesmod.Models.baked_models;

import com.ferreusveritas.dynamictrees.compat.seasons.SeasonHelper;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SeasonalIpeLeavesModel implements IBakedModel {

    private final IBakedModel baseModel;
    private final TextureAtlasSprite blossomTexture;
    private final TextureAtlasSprite dormantTexture;

    public SeasonalIpeLeavesModel(IBakedModel baseModel, TextureAtlasSprite blossomTexture, TextureAtlasSprite dormantTexture) {
        this.baseModel = baseModel;
        this.blossomTexture = blossomTexture;
        this.dormantTexture = dormantTexture;

    }

    @Override
    public List<BakedQuad> getQuads(BlockState state, Direction side, Random rand) {
        List<BakedQuad> quads = new ArrayList<>(baseModel.getQuads(state, side, rand));

        World world = Minecraft.getInstance().world;

        float biomeTemp = 0.8f;

        if (world != null) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player != null) {
                BlockPos pos = mc.player.getPosition();
                biomeTemp = world.getBiome(pos).getTemperature(pos);
            }
        }

        float season = SeasonHelper.getSeasonValue(world, BlockPos.ZERO);

        if (isFloweringSeason(season, biomeTemp)) {
            quads.addAll(createBlossomOverlayQuads(side, rand));
        } else if (isDormantSeason(season, biomeTemp)) {
            quads.addAll(createDormantOverlayQuads(side, rand));
        }

        return quads;
    }

    private boolean isTropical(float temp) {
        return temp >= 0.9f;
    }

    private boolean isFloweringSeason(float s, float biomeTemp) {
        if (isTropical(biomeTemp)) {
            return isFloweringSeason_Tropical(s);
        } else {
            return isFloweringSeason_Subtropical(s);
        }
    }

    private boolean isDormantSeason(float s, float biomeTemp) {
        if (isTropical(biomeTemp)) {
            return isDormantSeason_Tropical(s);
        } else {
            return isDormantSeason_Subtropical(s);
        }
    }

    //Flowering seasons
    private boolean isFloweringSeason_Tropical(float s) {
        return (s >= 2.5f && s < 3f); //mid fall to winter (late dry season)
    }

    private boolean isFloweringSeason_Subtropical(float s) {
        return s >= 0.0f && s < 1.0f; //spring
    }

    //Dormant seasons
    private boolean isDormantSeason_Tropical(float s) {
        return (s >= 1.5f && s < 2.5f); //mid summer to mid fall
    }

    private boolean isDormantSeason_Subtropical(float s) {
        return s >= 3.0f && s < 4.0f; //winter
    }

    private List<BakedQuad> createBlossomOverlayQuads(Direction side, Random rand) {
        List<BakedQuad> overlay = new ArrayList<>();

        for (BakedQuad quad : baseModel.getQuads(null, side, rand)) {
            overlay.add(retextureQuad(quad, blossomTexture));
        }

        return overlay;
    }

    private List<BakedQuad> createDormantOverlayQuads(Direction side, Random rand) {
        List<BakedQuad> overlay = new ArrayList<>();

        for (BakedQuad quad : baseModel.getQuads(null, side, rand)) {
            overlay.add(retextureQuad(quad, dormantTexture));
        }

        return overlay;
    }

    private BakedQuad retextureQuad(BakedQuad quad, TextureAtlasSprite newSprite) {
        int[] data = quad.getVertices().clone();

        TextureAtlasSprite oldSprite = quad.getSprite();

        // Each vertex = 8 ints
        for (int i = 0; i < 4; i++) {
            int index = i * 8;

            float u = Float.intBitsToFloat(data[index + 4]);
            float v = Float.intBitsToFloat(data[index + 5]);

            // Convert from old sprite space → normalized (0–1)
            float un = (u - oldSprite.getU0()) / (oldSprite.getU1() - oldSprite.getU0());
            float vn = (v - oldSprite.getV0()) / (oldSprite.getV1() - oldSprite.getV0());

            // Apply to new sprite
            float newU = newSprite.getU(un * 16f);
            float newV = newSprite.getV(vn * 16f);

            data[index + 4] = Float.floatToRawIntBits(newU);
            data[index + 5] = Float.floatToRawIntBits(newV);
        }

        return new BakedQuad(
                data,
                quad.getTintIndex(),
                quad.getDirection(),
                newSprite,
                quad.isShade()
        );
    }

    @Override
    public boolean isAmbientOcclusion() {
        return false;
    }

    @Override
    public boolean isGui3d() {
        return false;
    }

    @Override
    public boolean isSideLit() {
        return false;
    }

    @Override
    public boolean isBuiltInRenderer() {
        return false;
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return null;
    }

    @Override
    public ItemOverrideList getOverrides() {
        return null;
    }
}
