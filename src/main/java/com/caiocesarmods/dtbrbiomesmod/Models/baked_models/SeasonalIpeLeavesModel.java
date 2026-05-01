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
import net.minecraftforge.client.extensions.IForgeBakedModel;
import net.minecraftforge.client.model.data.IModelData;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SeasonalIpeLeavesModel implements IBakedModel {

    private final IBakedModel baseModel;
    private final TextureAtlasSprite blossomTexture;
    private final TextureAtlasSprite dormantTexture;

    public SeasonalIpeLeavesModel(IBakedModel baseModel,
                                  TextureAtlasSprite blossomTexture,
                                  TextureAtlasSprite dormantTexture) {
        this.baseModel = baseModel;
        this.blossomTexture = blossomTexture;
        this.dormantTexture = dormantTexture;
    }

    // =========================================================
    // MAIN RENDER METHOD (FORGE PATH)
    // =========================================================

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state,
                                    @Nullable Direction side,
                                    Random rand,
                                    IModelData extraData) {

        List<BakedQuad> baseQuads = ((IForgeBakedModel) baseModel)
                .getQuads(state, side, rand, extraData);

        List<BakedQuad> result = new ArrayList<>(baseQuads);

        World world = Minecraft.getInstance().world;
        if (world == null) return result;

        BlockPos pos = getReferencePos();

        float biomeTemp = world.getBiome(pos).getTemperature(pos);
        float season = SeasonHelper.getSeasonValue(world, pos);

        if (isFloweringSeason(season, biomeTemp)) {
            result.addAll(createOverlay(baseQuads, blossomTexture));
        } else if (isDormantSeason(season, biomeTemp)) {
            result.clear();
            result.addAll(createOverlay(baseQuads, dormantTexture));
        }

        return result;
    }

    // =========================================================
    // FALLBACK (VANILLA PATH)
    // =========================================================

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state,
                                    @Nullable Direction side,
                                    Random rand) {
        return baseModel.getQuads(state, side, rand);
    }

    // =========================================================
    // POSITION HELPER
    // =========================================================

    private BlockPos getReferencePos() {
        Minecraft mc = Minecraft.getInstance();

        if (mc.player != null) {
            return mc.player.getPosition();
        }

        return BlockPos.ZERO;
    }

    // =========================================================
    // SEASON LOGIC
    // =========================================================

    private boolean isTropical(float temp) {
        return temp >= 0.9f;
    }

    private boolean isFloweringSeason(float s, float temp) {
        return isTropical(temp)
                ? (s >= 2.5f && s < 3.0f)   // tropical dry season bloom
                : (s >= 0.0f && s < 1.0f); // subtropical spring bloom
    }

    private boolean isDormantSeason(float s, float temp) {
        return isTropical(temp)
                ? (s >= 1.5f && s < 2.5f)
                : (s >= 3.0f && s < 4.0f);
    }

    // =========================================================
    // OVERLAY GENERATION
    // =========================================================

    private List<BakedQuad> createOverlay(List<BakedQuad> baseQuads,
                                          TextureAtlasSprite texture) {

        List<BakedQuad> overlay = new ArrayList<>(baseQuads.size());

        for (BakedQuad quad : baseQuads) {
            overlay.add(retextureQuad(quad, texture));
        }

        return overlay;
    }

    // =========================================================
    // TEXTURE SWAP (CORE LOGIC)
    // =========================================================

    private BakedQuad retextureQuad(BakedQuad quad, TextureAtlasSprite newSprite) {

        int[] data = quad.getVertexData().clone();
        TextureAtlasSprite oldSprite = quad.getSprite();

        for (int i = 0; i < 4; i++) {

            int index = i * 8;

            float u = Float.intBitsToFloat(data[index + 4]);
            float v = Float.intBitsToFloat(data[index + 5]);

            float un = oldSprite.getInterpolatedU(u);
            float vn = oldSprite.getInterpolatedV(v);

            float newU = newSprite.getInterpolatedU(un);
            float newV = newSprite.getInterpolatedV(vn);

            data[index + 4] = Float.floatToRawIntBits(newU);
            data[index + 5] = Float.floatToRawIntBits(newV);
        }

        return new BakedQuad(
                data,
                quad.getTintIndex(),
                quad.getFace(),
                newSprite,
                quad.applyDiffuseLighting()
        );
    }

    // =========================================================
    // REQUIRED MODEL METHODS
    // =========================================================

    @Override
    public boolean isAmbientOcclusion() {
        return baseModel.isAmbientOcclusion();
    }

    @Override
    public boolean isGui3d() {
        return baseModel.isGui3d();
    }

    @Override
    public boolean isSideLit() {
        return baseModel.isSideLit();
    }

    @Override
    public boolean isBuiltInRenderer() {
        return baseModel.isBuiltInRenderer();
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return baseModel.getParticleTexture();
    }

    @Override
    public ItemOverrideList getOverrides() {
        return baseModel.getOverrides();
    }
}