package com.mumu17.castlib.mixin.ars;

import com.hollingsworth.arsnouveau.api.spell.SpellContext;
import com.hollingsworth.arsnouveau.api.spell.SpellResolver;
import com.mumu17.castlib.util.ProviderRegistry;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SpellResolver.class)
public class SpellResolverMixin {

    @Shadow(remap = false) public SpellContext spellContext;

    @ModifyVariable(method = "expendMana", at = @At("STORE"), ordinal = 0, remap = false)
    public int expendMana_totalCost(int original) {
        var provider = ProviderRegistry.projectileDataProvider;
        if (provider != null && spellContext.getCaster() instanceof LivingEntity livingEntity && provider.isEnabled(livingEntity)) {
            return 0;
        }
        return original;
    }

    @ModifyVariable(method = "enoughMana", at = @At("STORE"), ordinal = 0, remap = false)
    public int enoughMana_totalCost(int original) {
        var provider = ProviderRegistry.projectileDataProvider;
        if (provider != null && spellContext.getCaster() instanceof LivingEntity livingEntity && provider.isEnabled(livingEntity)) {
            return 0;
        }
        return original;
    }

    @Inject(method = "enoughMana", at = @At(value = "INVOKE", target = "Lcom/hollingsworth/arsnouveau/api/mana/IManaCap;getCurrentMana()D"), remap = false, cancellable = true)
    public void enoughMana_currentMana(LivingEntity entity, CallbackInfoReturnable<Boolean> cir) {
        var provider = ProviderRegistry.projectileDataProvider;
        if (provider != null && spellContext.getCaster() instanceof LivingEntity livingEntity && provider.isEnabled(livingEntity)) {
            cir.setReturnValue(true);
            cir.cancel();
        }
    }
}
