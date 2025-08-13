package com.mumu17.castlib.util;

import net.minecraft.nbt.CompoundTag;

public class CastLibTags {
    public static void saveCastModIDToTag(CompoundTag tag, String modId) {
        if (modId == null || tag == null) return;
        tag.putString(ProviderRegistry.CAST_MOD_TAG, modId);
    }

    public static String loadCastModIDFromTag(CompoundTag tag) {
        if (tag == null || !tag.contains(ProviderRegistry.CAST_MOD_TAG)) return null;
        return tag.getString(ProviderRegistry.CAST_MOD_TAG);
    }
}
