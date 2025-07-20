package com.mumu17.castlib.mixin.ars;

import com.hollingsworth.arsnouveau.api.spell.SpellResolver;
import com.mumu17.castlib.util.ProviderRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(SpellResolver.class)
public class SpellResolverMixin {

    @ModifyVariable(method = "expendMana", at = @At("STORE"), ordinal = 0, remap = false)
    public int expendMana_totalCost(int original) {
        var provider = ProviderRegistry.projectileDataProvider;
        if (provider != null && provider.isEnabled()) {
            return 0;
        }
        return original;
    }

    @ModifyVariable(method = "enoughMana", at = @At("STORE"), ordinal = 0, remap = false)
    public int enoughMana_totalCost(int original) {
        var provider = ProviderRegistry.projectileDataProvider;
        if (provider != null && provider.isEnabled()) {
            return 0;
        }
        return original;
    }
}
