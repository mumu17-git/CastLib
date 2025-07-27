package com.mumu17.castlib.mixin.ars;

import com.hollingsworth.arsnouveau.api.spell.SpellContext;
import com.hollingsworth.arsnouveau.api.spell.SpellResolver;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.mumu17.castlib.util.ProviderRegistry;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SpellResolver.class)
public class SpellResolverMixin {

    @Shadow(remap = false) public SpellContext spellContext;

    @ModifyReturnValue(method = "getResolveCost", at = @At("RETURN"), remap = false)
    public int getResolveCost(int original) {
        if (spellContext.getCaster() instanceof LivingEntity livingEntity) {
            String castMod = livingEntity.getPersistentData().getString(ProviderRegistry.CAST_MOD_TAG);
            var provider = ProviderRegistry.getProjectileProvider(castMod);
            if (provider != null && provider.isEnabled(livingEntity)) {
                return 0;
            }
        }
        return original;
    }
}
