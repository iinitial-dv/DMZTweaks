package com.iinitial.dmztweaks.common.config;

public final class ClientConfig {
    // Shows the arrow above enemies when about to attack if the setting is enabled in server config
    public boolean showTelegraphedAttacks;
    // Shows the actually calculated health/stamina/ki gain or drain instead of the base value.
    public boolean showPreciseHealthGain;
    public boolean showPreciseStaminaGain;
    public boolean showPreciseKiGain;
    // Shows the same calculated health/stamina/ki gain or drain in the hud so players can see it there as well.
    public boolean showHealthGainInHud;
    public boolean showStaminaGainInHud;
    public boolean showKiGainInHud;

    // Getters for Client related Configs
    public boolean isTelegraphedAttacksShown() {
        return showTelegraphedAttacks;
    }

    public boolean isPreciseHealthGainShown() {
        return showPreciseHealthGain;
    }

    public boolean isPreciseStaminaGainShown() {
        return showPreciseStaminaGain;
    }

    public boolean isPreciseKiGainShown() {
        return showPreciseKiGain;
    }

    public boolean isHealthGainInHudShown() {
        return showHealthGainInHud;
    }

    public boolean isStaminaGainInHudShown() {
        return showStaminaGainInHud;
    }

    public boolean isKiGainInHudShown() {
        return showKiGainInHud;
    }

    // Setters for Client related Configs
    public void setTelegraphedAttacks(boolean value) {
        showTelegraphedAttacks = value;
        ConfigManager.saveClientConfig();
    }

    public void setPreciseHealthGain(boolean value) {
        showPreciseHealthGain = value;
        ConfigManager.saveClientConfig();
    }

    public void setPreciseStaminaGain(boolean value) {
        showPreciseStaminaGain = value;
        ConfigManager.saveClientConfig();
    }

    public void setPreciseKiGain(boolean value) {
        showPreciseKiGain = value;
        ConfigManager.saveClientConfig();
    }

    public void setHealthGainInHud(boolean value) {
        showHealthGainInHud = value;
        ConfigManager.saveClientConfig();
    }

    public void setStaminaGainInHud(boolean value) {
        showStaminaGainInHud = value;
        ConfigManager.saveClientConfig();
    }

    public void setKiGainInHud(boolean value) {
        showKiGainInHud = value;
        ConfigManager.saveClientConfig();
    }
}
