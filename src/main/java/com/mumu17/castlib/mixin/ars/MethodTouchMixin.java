package com.mumu17.castlib.mixin.ars;

import com.hollingsworth.arsnouveau.api.registry.GlyphRegistry;
import com.hollingsworth.arsnouveau.api.spell.AbstractAugment;
import com.hollingsworth.arsnouveau.api.spell.AbstractSpellPart;
import com.hollingsworth.arsnouveau.api.spell.SpellResolver;
import com.hollingsworth.arsnouveau.common.lib.GlyphLib;
import com.hollingsworth.arsnouveau.common.spell.method.MethodTouch;
import com.mumu17.castlib.util.ProviderRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.ArrayList;
import java.util.List;

@Mixin(MethodTouch.class)
public class MethodTouchMixin {

    @ModifyVariable(method = "onCastOnEntity", at = @At(value = "HEAD"), ordinal = 0, remap = false, argsOnly = true)
    public Entity onCastOnEntity_entity(Entity original) {
        var provider = ProviderRegistry.projectileDataProvider;
        if (provider != null && provider.isEnabled()) {
            Entity entity = provider.getTargetEntity();
            InteractionHand hand = provider.getHand();
            if (hand == InteractionHand.OFF_HAND) {
                if (entity != null) {
                    return entity;
                }
            }
        }
        return original;
    }

    @ModifyVariable(method = "onCastOnEntity", at = @At(value = "HEAD"), ordinal = 0, argsOnly = true, remap = false)
    public InteractionHand onCastOnEntity_hand(InteractionHand original) {
        var provider = ProviderRegistry.projectileDataProvider;
        if (provider != null && provider.isEnabled()) {
            Entity entity = provider.getTargetEntity();
            InteractionHand hand = provider.getHand();
            if (hand == InteractionHand.OFF_HAND) {
                if (entity != null) {
                    return hand;
                }
            }
        }
        return original;
    }

    @ModifyVariable(method = "onCastOnEntity", at = @At(value = "HEAD"), ordinal = 0, argsOnly = true, remap = false)
    public SpellResolver onCastOnEntity_spellResolver(SpellResolver original) {
        var projectileProvider = ProviderRegistry.projectileDataProvider;
        var cooldownProvider = ProviderRegistry.gunItemCooldownProvider;
        if (projectileProvider != null && cooldownProvider != null && projectileProvider.isEnabled()) {
            Entity entity = projectileProvider.getTargetEntity();
            InteractionHand hand = projectileProvider.getHand();
            ItemStack gun = projectileProvider.getCurrentGun();
            double damageMultiply = ProviderRegistry.damageAmplifierProvider.getAmplifier(
                    ProviderRegistry.gunItemCooldownProvider.getGunDamage(gun)
            );
            if (hand == InteractionHand.OFF_HAND) {
                if (entity != null) {
                    List<AbstractSpellPart> modified_parts = new ArrayList<>(List.copyOf(original.spell.recipe));
                    for (AbstractSpellPart part : original.spell.recipe) {
                        if (!(part.getGlyph().spellPart instanceof AbstractAugment)) {
                            modified_parts.add(part);
                        } else {
                            modified_parts.add(part);
                        }
                    }
                    for (int i = 0; i < damageMultiply;i++) {
                        modified_parts.add(GlyphRegistry.getSpellPart(new ResourceLocation("ars_nouveau", GlyphLib.AugmentAmplifyID)));
                    }
                    original.spell.setRecipe(modified_parts);
                    return original;
                }
            }
        }
        return original;
    }

    @ModifyVariable(method = "onCastOnBlock(Lnet/minecraft/world/item/context/UseOnContext;Lcom/hollingsworth/arsnouveau/api/spell/SpellStats;Lcom/hollingsworth/arsnouveau/api/spell/SpellContext;Lcom/hollingsworth/arsnouveau/api/spell/SpellResolver;)Lcom/hollingsworth/arsnouveau/api/spell/CastResolveType;", at = @At(value = "HEAD"), ordinal = 0, remap = false, argsOnly = true)
    public UseOnContext onCastOnBlock_UseOnContext(UseOnContext value) {
        var provider = ProviderRegistry.projectileDataProvider;
        if (provider != null && provider.isEnabled()) {
            BlockHitResult blockHR = provider.getBlockHitResult();
            InteractionHand hand = provider.getHand();
            if (hand == InteractionHand.OFF_HAND) {
                if (blockHR != null && value != null && value.getPlayer() != null) {
                    return new UseOnContext(value.getPlayer(), hand, blockHR);
                }
            }
        }
        return value;
    }

    @ModifyVariable(method = "onCastOnBlock(Lnet/minecraft/world/item/context/UseOnContext;Lcom/hollingsworth/arsnouveau/api/spell/SpellStats;Lcom/hollingsworth/arsnouveau/api/spell/SpellContext;Lcom/hollingsworth/arsnouveau/api/spell/SpellResolver;)Lcom/hollingsworth/arsnouveau/api/spell/CastResolveType;", at = @At(value = "HEAD"), ordinal = 0, argsOnly = true, remap = false)
    public SpellResolver onCastOnBlock_spellResolver(SpellResolver original) {
        var projectileProvider = ProviderRegistry.projectileDataProvider;
        var cooldownProvider = ProviderRegistry.gunItemCooldownProvider;
        if (projectileProvider != null && cooldownProvider != null && projectileProvider.isEnabled()) {
            BlockHitResult blockHR = projectileProvider.getBlockHitResult();
            InteractionHand hand = projectileProvider.getHand();
            ItemStack gun = projectileProvider.getCurrentGun();
            double damageMultiply = ProviderRegistry.damageAmplifierProvider.getAmplifier(
                    ProviderRegistry.gunItemCooldownProvider.getGunDamage(gun)
            );
            if (hand == InteractionHand.OFF_HAND) {
                if (blockHR != null) {
                    List<AbstractSpellPart> modified_parts = new ArrayList<>(List.copyOf(original.spell.recipe));
                    for (AbstractSpellPart part : original.spell.recipe) {
                        if (!(part.getGlyph().spellPart instanceof AbstractAugment)) {
                            modified_parts.add(part);
                        } else {
                            modified_parts.add(part);
                        }
                    }
                    for (int i = 0; i < damageMultiply;i++) {
                        modified_parts.add(GlyphRegistry.getSpellPart(new ResourceLocation("ars_nouveau", GlyphLib.AugmentAmplifyID)));
                    }
                    original.spell.setRecipe(modified_parts);
                    return original;
                }
            }
        }
        return original;
    }

    @ModifyVariable(method = "onCastOnBlock(Lnet/minecraft/world/phys/BlockHitResult;Lnet/minecraft/world/entity/LivingEntity;Lcom/hollingsworth/arsnouveau/api/spell/SpellStats;Lcom/hollingsworth/arsnouveau/api/spell/SpellContext;Lcom/hollingsworth/arsnouveau/api/spell/SpellResolver;)Lcom/hollingsworth/arsnouveau/api/spell/CastResolveType;", at = @At(value = "HEAD"), ordinal = 0, remap = false, argsOnly = true)
    public BlockHitResult onCastOnBlock_BlockHitResult(BlockHitResult original) {
        var provider = ProviderRegistry.projectileDataProvider;
        if (provider != null && provider.isEnabled()) {
            BlockHitResult blockHR = provider.getBlockHitResult();
            InteractionHand hand = provider.getHand();
            if (hand == InteractionHand.OFF_HAND) {
                if (blockHR != null) {
                    return blockHR;
                }
            }
        }
        return original;
    }

}
