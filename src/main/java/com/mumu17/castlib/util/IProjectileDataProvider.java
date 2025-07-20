package com.mumu17.castlib.util;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;

public interface IProjectileDataProvider {
    boolean isEnabled();
    Entity getTargetEntity();
    InteractionHand getHand();
    BlockHitResult getBlockHitResult();
    ItemStack getCurrentGun();
}
