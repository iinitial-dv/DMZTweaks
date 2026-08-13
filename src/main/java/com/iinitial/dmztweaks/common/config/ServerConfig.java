package com.iinitial.dmztweaks.common.config;

public class ServerConfig {
    // Balance changes made to classes so that tank isn't the most picked class.
    public boolean enableBalancedClasses = true;
    // Arrows above enemies before they attack.
    public boolean enableTelegraphedAttacks = true;
    // Changes minigames so that they're possible at higher levels.
    public boolean enableBetterMinigames = true;
    // Changes the calculated health/stamina/ki gain to be precise instead of rounding to the whole number when applying
    // to character. The displayed number will still be a whole number. The change is behind the scenes.
    public boolean enablePreciseHealthGain = true;
    public boolean enablePreciseKiGain = true;
    public boolean enablePreciseStaminaGain = true;

    // Getters for Server related Configs
    public boolean isBalancedClassesEnabled() {
        return enableBalancedClasses;
    }

    public boolean isTelegraphedAttacksEnabled() {
        return enableTelegraphedAttacks;
    }

    public boolean isBetterMinigamesEnabled() {
        return enableBetterMinigames;
    }

    public boolean isPreciseHealthGainEnabled() {
        return enablePreciseHealthGain;
    }

    public boolean isPreciseKiGainEnabled() {
        return enablePreciseKiGain;
    }

    public boolean isPreciseStaminaGainEnabled() {
        return enablePreciseStaminaGain;
    }

    // Setters for Server related Configs
    public void setBalancedClasses(boolean value) {
        enableBalancedClasses = value;
        ConfigManager.saveServerConfig();
    }

    public void setTelegraphedAttacks(boolean value) {
        enableTelegraphedAttacks = value;
        ConfigManager.saveServerConfig();
    }

    public void setBetterMinigames(boolean value) {
        enableBetterMinigames = value;
        ConfigManager.saveServerConfig();
    }

    public void setPreciseHealthGain(boolean value) {
        enablePreciseHealthGain = value;
        ConfigManager.saveServerConfig();
    }

    public void setPreciseKiGain(boolean value) {
        enablePreciseKiGain = value;
        ConfigManager.saveServerConfig();
    }

    public void setPreciseStaminaGain(boolean value) {
        enablePreciseStaminaGain = value;
        ConfigManager.saveServerConfig();
    }
}
