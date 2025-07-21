package com.mumu17.castlib.util;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;

public interface IProjectileDataProvider {
    boolean isEnabled(LivingEntity player);
    Entity getTargetEntity(LivingEntity player);
    InteractionHand getHand(LivingEntity player);
    BlockHitResult getBlockHitResult(LivingEntity player);
    ItemStack getCurrentGun(LivingEntity player);
}
