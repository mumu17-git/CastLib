package com.mumu17.castlib.mixin.ars;

import com.hollingsworth.arsnouveau.api.spell.SpellContext;
import com.hollingsworth.arsnouveau.api.spell.SpellResolver;
import com.mumu17.castlib.util.ProviderRegistry;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(SpellResolver.class)
public class SpellResolverMixin {

    @Shadow(remap = false) public SpellContext spellContext;

    @ModifyVariable(method = "getResolveCost", at = @At(value = "STORE", ordinal = 0), remap = false)
    public int getResolveCost(int original) {
        LivingEntity caster = spellContext.getUnwrappedCaster();
        String castMod = caster.getPersistentData().contains(ProviderRegistry.CAST_MOD_TAG) ? caster.getPersistentData().getString(ProviderRegistry.CAST_MOD_TAG) : null;
        if (castMod != null) {
            var provider = ProviderRegistry.getProjectileProvider(castMod);
            if (provider != null && provider.isEnabled(caster)) {
                return 0;
            }
        }
        return original;
    }
}
