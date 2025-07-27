package com.mumu17.castlib.util;

import java.util.HashMap;
import java.util.Map;

public class ProviderRegistry {
    public static final Map<String, IProjectileDataProvider> projectileDataProviders = new HashMap<>();
    public static final Map<String, IArmsNbtProvider> armsNbtProviders = new HashMap<>();
    public static final Map<String, IDamageAmplifierProvider> damageAmplifierProviders = new HashMap<>();
    public static final String CAST_MOD_TAG = "CastMod";

    public static void registerProjectileProvider(String id, IProjectileDataProvider provider) {
        projectileDataProviders.put(id, provider);
    }

    public static void registerArmsNbtProvider(String id, IArmsNbtProvider provider) {
        armsNbtProviders.put(id, provider);
    }

    public static void registerDamageAmplifierProvider(String id, IDamageAmplifierProvider provider) {
        damageAmplifierProviders.put(id, provider);
    }

    public static IProjectileDataProvider getProjectileProvider(String id) {
        return projectileDataProviders.get(id);
    }

    public static IArmsNbtProvider getArmsNbtProvider(String id) {
        return armsNbtProviders.get(id);
    }

    public static IDamageAmplifierProvider getDamageAmplifierProvider(String id) {
        return damageAmplifierProviders.get(id);
    }

}
