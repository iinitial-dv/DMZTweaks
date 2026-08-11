package com.iinitial.dmztweaks.common.network.packets;

public class ViewClientServerConfig {
    private static boolean enableBalancedClasses;
    private static boolean enableTelegraphedAttacks;
    private static boolean enablePreciseHealthGain;
    private static boolean enablePreciseStaminaGain;
    private static boolean enablePreciseKiGain;
    private static boolean received = false;

    public static void update(boolean balancedClasses, boolean telegraphedAttacks, boolean preciseHealth, boolean preciseStamina, boolean preciseKi) {
        enableBalancedClasses = balancedClasses;
        enableTelegraphedAttacks = telegraphedAttacks;
        enablePreciseHealthGain = preciseHealth;
        enablePreciseStaminaGain = preciseStamina;
        enablePreciseKiGain = preciseKi;
        received = true;
    }

    public static boolean hasReceived() {
        return received;
    }

    public static boolean isBalancedClassesEnabled() { return enableBalancedClasses; }
    public static boolean isEnableTelegraphedAttacks() { return enableTelegraphedAttacks; }
    public static boolean isEnablePreciseHealthGain() { return enablePreciseHealthGain; }
    public static boolean isEnablePreciseStaminaGain() { return enablePreciseStaminaGain; }
    public static boolean isEnablePreciseKiGain() { return enablePreciseKiGain; }
}