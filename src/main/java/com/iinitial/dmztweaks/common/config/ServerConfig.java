package com.iinitial.dmztweaks.common.config;

public class ServerConfig {
    // Balance changes made to classes so that tank isn't the most picked class.
    public boolean enableBalancedClasses;
    // Arrows above enemies before they attack.
    public boolean enableTelegraphedAttacks;
    // Changes the calculated health/stamina/ki gain to be precise instead of rounding to the whole number when applying
    // to character. The displayed number will still be a whole number. The change is behind the scenes.
    public boolean enablePreciseHealthGain;
    public boolean enablePreciseStaminaGain;
    public boolean enablePreciseKiGain;

    // Getters for Server related Configs
    public boolean isBalancedClassesEnabled() { return enableBalancedClasses; }
    public boolean isTelegraphedAttacksEnabled() { return enableTelegraphedAttacks; }
    public boolean isPreciseHealthGainEnabled() { return enablePreciseHealthGain; }
    public boolean isPreciseStaminaGainEnabled() { return enablePreciseStaminaGain; }
    public boolean isPreciseKiGainEnabled() { return enablePreciseKiGain; }

    // Setters for Server related Configs
    public void setBalancedClasses(boolean value) {
        enableBalancedClasses = value;
        ConfigManager.saveServerConfig();
    }
    public void setTelegraphedAttacks(boolean value) {
        enableTelegraphedAttacks = value;
        ConfigManager.saveServerConfig();
    }
    public void setPreciseHealthGain(boolean value) {
        enablePreciseHealthGain = value;
        ConfigManager.saveServerConfig();
    }
    public void setPreciseStaminaGain(boolean value) {
        enablePreciseStaminaGain = value;
        ConfigManager.saveServerConfig();
    }
    public void setPreciseKiGain(boolean value) {
        enablePreciseKiGain = value;
        ConfigManager.saveServerConfig();
    }
}
