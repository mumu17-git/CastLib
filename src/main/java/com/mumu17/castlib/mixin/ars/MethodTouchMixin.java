package com.mumu17.castlib.mixin.ars;

import com.hollingsworth.arsnouveau.api.registry.GlyphRegistry;
import com.hollingsworth.arsnouveau.api.spell.AbstractAugment;
import com.hollingsworth.arsnouveau.api.spell.AbstractSpellPart;
import com.hollingsworth.arsnouveau.api.spell.SpellResolver;
import com.hollingsworth.arsnouveau.common.lib.GlyphLib;
import com.hollingsworth.arsnouveau.common.spell.method.MethodTouch;
import com.llamalad7.mixinextras.sugar.Local;
import com.mumu17.castlib.util.CastLibTags;
import com.mumu17.castlib.util.ProviderRegistry;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
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
    public Entity onCastOnEntity_entity(Entity original, @Local(argsOnly = true) LivingEntity caster) {
        if (caster == null) {
            return original;
        }
        String castMod = CastLibTags.loadCastModIDFromTag(caster.getPersistentData());
        var provider = ProviderRegistry.getProjectileProvider(castMod);
        if (provider != null && provider.isEnabled(caster)) {
            Entity entity = provider.getTargetEntity(caster);
            InteractionHand hand = provider.getHand(caster);
            if (hand != InteractionHand.MAIN_HAND) {
                if (entity != null) {
                    return entity;
                }
            }
        }
        return original;
    }

    @ModifyVariable(method = "onCastOnEntity", at = @At(value = "HEAD"), ordinal = 0, argsOnly = true, remap = false)
    public InteractionHand onCastOnEntity_hand(InteractionHand original, @Local(argsOnly = true) LivingEntity caster) {
        if (caster == null) {
            return original;
        }
        String castMod = CastLibTags.loadCastModIDFromTag(caster.getPersistentData());
        var provider = ProviderRegistry.getProjectileProvider(castMod);
        if (provider != null && provider.isEnabled(caster)) {
            Entity entity = provider.getTargetEntity(caster);
            InteractionHand hand = provider.getHand(caster);
            if (hand != InteractionHand.MAIN_HAND) {
                if (entity != null) {
                    return hand;
                }
            }
        }
        return original;
    }

    @ModifyVariable(method = "onCastOnEntity", at = @At(value = "HEAD"), ordinal = 0, argsOnly = true, remap = false)
    public SpellResolver onCastOnEntity_spellResolver(SpellResolver original, @Local(argsOnly = true) LivingEntity caster) {
        if (caster == null) {
            return original;
        }
        String castMod = CastLibTags.loadCastModIDFromTag(caster.getPersistentData());
        var projectileProvider = ProviderRegistry.getProjectileProvider(castMod);
        var cooldownProvider = ProviderRegistry.getArmsNbtProvider(castMod);
        var damageAmplifierProvider = ProviderRegistry.getDamageAmplifierProvider(castMod);
        if (projectileProvider != null && cooldownProvider != null && projectileProvider.isEnabled(caster)) {
            Entity entity = projectileProvider.getTargetEntity(caster);
            InteractionHand hand = projectileProvider.getHand(caster);
            ItemStack gun = projectileProvider.getArms(caster);
            double damageMultiply = damageAmplifierProvider.getAmplifier(cooldownProvider.getArmsDamage(gun));
            if (hand != InteractionHand.MAIN_HAND) {
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
        LivingEntity caster = value.getPlayer();
        if (caster == null) {
            return value;
        }
        String castMod = CastLibTags.loadCastModIDFromTag(caster.getPersistentData());
        var provider = ProviderRegistry.getProjectileProvider(castMod);
        if (provider != null && provider.isEnabled(caster)) {
            BlockHitResult blockHR = provider.getBlockHitResult(caster);
            InteractionHand hand = provider.getHand(caster);
            if (hand != InteractionHand.MAIN_HAND) {
                if (blockHR != null && value.getPlayer() != null) {
                    return new UseOnContext(value.getPlayer(), hand, blockHR);
                }
            }
        }
        return value;
    }

    @ModifyVariable(method = "onCastOnBlock(Lnet/minecraft/world/item/context/UseOnContext;Lcom/hollingsworth/arsnouveau/api/spell/SpellStats;Lcom/hollingsworth/arsnouveau/api/spell/SpellContext;Lcom/hollingsworth/arsnouveau/api/spell/SpellResolver;)Lcom/hollingsworth/arsnouveau/api/spell/CastResolveType;", at = @At(value = "HEAD"), ordinal = 0, argsOnly = true, remap = false)
    public SpellResolver onCastOnBlock_spellResolver(SpellResolver original, @Local(argsOnly = true) UseOnContext context) {
        if (context == null) return original;
        LivingEntity caster = context.getPlayer();
        if (caster == null) return original;
        String castMod = CastLibTags.loadCastModIDFromTag(caster.getPersistentData());
        var projectileProvider = ProviderRegistry.getProjectileProvider(castMod);
        var cooldownProvider = ProviderRegistry.getArmsNbtProvider(castMod);
        var damageAmplifierProvider = ProviderRegistry.getDamageAmplifierProvider(castMod);
        if (projectileProvider != null && cooldownProvider != null && projectileProvider.isEnabled(caster)) {
            BlockHitResult blockHR = projectileProvider.getBlockHitResult(caster);
            InteractionHand hand = projectileProvider.getHand(caster);
            ItemStack gun = projectileProvider.getArms(caster);
            double damageMultiply = damageAmplifierProvider.getAmplifier(
                    cooldownProvider.getArmsDamage(gun)
            );
            if (hand != InteractionHand.MAIN_HAND) {
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
    public BlockHitResult onCastOnBlock_BlockHitResult(BlockHitResult original, @Local(argsOnly = true) LivingEntity caster) {
        if (caster == null) return original;
        String castMod = CastLibTags.loadCastModIDFromTag(caster.getPersistentData());
        var provider = ProviderRegistry.getProjectileProvider(castMod);
        if (provider != null && provider.isEnabled(caster)) {
            BlockHitResult blockHR = provider.getBlockHitResult(caster);
            InteractionHand hand = provider.getHand(caster);
            if (hand != InteractionHand.MAIN_HAND) {
                if (blockHR != null) {
                    return blockHR;
                }
            }
        }
        return original;
    }

}
