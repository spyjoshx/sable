package dev.ryanhcode.sable.mixin.plot.lighting;

import dev.ryanhcode.sable.mixinterface.plot.lighting.LevelLightEngineExtension;
import net.minecraft.world.level.chunk.LightChunkGetter;
import net.minecraft.world.level.lighting.LevelLightEngine;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelLightEngine.class)
public class LevelLightEngineMixin implements LevelLightEngineExtension {

    @Unique
    private boolean sable$hasBlockLight;
    @Unique
    private boolean sable$hasSkylight;

    @Inject(method = "<init>", at = @At("TAIL"))
    public void init(final LightChunkGetter lightChunkGetter, final boolean blockLight, final boolean skyLight, final CallbackInfo ci) {
        this.sable$hasBlockLight = blockLight;
        this.sable$hasSkylight = skyLight;
    }

    @Override
    public boolean sable$hasBlockLight() {
        return this.sable$hasBlockLight;
    }

    @Override
    public boolean sable$hasSkyight() {
        return this.sable$hasSkylight;
    }
}
