package com.mumu17.castlib.mixin.ars;

import com.hollingsworth.arsnouveau.api.util.SpellUtil;
import com.llamalad7.mixinextras.sugar.Local;
import com.mumu17.castlib.util.ProviderRegistry;
import net.minecraft.nbt.Tag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SpellUtil.class)
public class SpellUtilMixin {
    @Inject(method = "rayTrace", at = @At(value = "HEAD"), cancellable = true, remap = false)
    private static void rayTrace(CallbackInfoReturnable<HitResult> cir, @Local(argsOnly = true) Entity caster) {
        if (caster == null) return;
        String castMod = caster.getPersistentData().contains(ProviderRegistry.CAST_MOD_TAG, Tag.TAG_STRING) ? caster.getPersistentData().getString(ProviderRegistry.CAST_MOD_TAG) : null;
        var provider = ProviderRegistry.getProjectileProvider(castMod);
        if (provider != null && provider.isEnabled((LivingEntity) caster)) {
            Entity entity = provider.getTargetEntity((LivingEntity) caster);
            BlockHitResult blockHitResult = provider.getBlockHitResult((LivingEntity) caster);
            InteractionHand hand = provider.getHand((LivingEntity) caster);
            if (hand != InteractionHand.MAIN_HAND) {
                if (entity != null) {
                    HitResult hitResult = new EntityHitResult(entity);
                    cir.setReturnValue(hitResult);
                    cir.cancel();
                } else if (blockHitResult != null) {
                    cir.setReturnValue(blockHitResult);
                    cir.cancel();
                }
            }
        }
    }
}
