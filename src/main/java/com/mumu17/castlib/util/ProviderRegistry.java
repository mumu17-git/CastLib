package com.mumu17.castlib.util;

public class ProviderRegistry {
    public static IProjectileDataProvider projectileDataProvider;
    public static IGunItemCooldownProvider gunItemCooldownProvider;
    public static IDamageAmplifierProvider damageAmplifierProvider;

    public static void registerProjectileProvider(IProjectileDataProvider provider) {
        projectileDataProvider = provider;
    }

    public static void registerGunItemCooldownProvider(IGunItemCooldownProvider provider) {
        gunItemCooldownProvider = provider;
    }

    public static void registerDamageAmplifierProvider(IDamageAmplifierProvider provider) {
        damageAmplifierProvider = provider;
    }


}
