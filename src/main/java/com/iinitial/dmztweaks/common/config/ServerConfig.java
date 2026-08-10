package com.iinitial.dmztweaks.common.config;

public class ServerConfig {
    // Balance changes made to classes so that tank isn't the most picked class.
    public boolean enableBalancedClasses;
    // Arrows above enemies before they attack.
    public boolean telegraphedAttacksEnabled;
    // Changes the calculated health/stamina/ki gain to be precise instead of rounding to the whole number when applying
    // to character. The displayed number will still be a whole number. The change is behind the scenes.
    public boolean preciseHealthGainEnabled;
    public boolean preciseStaminaGainEnabled;
    public boolean preciseKiGainEnabled;

    // Getters for Server related Configs
    public boolean isBalancedClassesEnabled() { return enableBalancedClasses; }
    public boolean isTelegraphedAttacksEnabled() { return telegraphedAttacksEnabled; }
    public boolean isPreciseHealthGainEnabled() { return preciseHealthGainEnabled; }
    public boolean isPreciseStaminaGainEnabled() { return preciseStaminaGainEnabled; }
    public boolean isPreciseKiGainEnabled() { return preciseKiGainEnabled; }

    // Setters for Server related Configs
    public void setBalancedClassesEnabled(boolean value) {
        enableBalancedClasses = value;
        ConfigManager.saveServerConfig();
    }
    public void setTelegraphedAttacksEnabled(boolean value) {
        telegraphedAttacksEnabled = value;
        ConfigManager.saveServerConfig();
    }
    public void setPreciseHealthGainEnabled(boolean value) {
        preciseHealthGainEnabled = value;
        ConfigManager.saveServerConfig();
    }
    public void setPreciseStaminaGainEnabled(boolean value) {
        preciseStaminaGainEnabled = value;
        ConfigManager.saveServerConfig();
    }
    public void setPreciseKiGainEnabled(boolean value) {
        preciseKiGainEnabled = value;
        ConfigManager.saveServerConfig();
    }
}
