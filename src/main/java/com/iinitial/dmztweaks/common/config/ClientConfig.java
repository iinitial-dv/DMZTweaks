package com.iinitial.dmztweaks.common.config;

public final class ClientConfig {
    // Shows the arrow above enemies when about to attack if the setting is enabled in server config
    public boolean enableTelegraphedAttacks;
    // Shows the actually calculated health/stamina/ki gain or drain instead of the base value.
    public boolean enableHealthGain;
    public boolean enableStaminaGain;
    public boolean enableKiGain;
    // Shows the same calculated health/stamina/ki gain or drain in the hud so players can see it there as well.
    public boolean enableHealthGainInHud;
    public boolean enableStaminaGainInHud;
    public boolean enableKiGainInHud;

    // Getters for Client related Configs
    public boolean isTelegraphedAttacksEnabled() { return enableTelegraphedAttacks; }
    public boolean isHealthGainEnabled() { return enableHealthGain; }
    public boolean isStaminaGainEnabled() { return enableStaminaGain; }
    public boolean isKiGainEnabled() { return enableKiGain; }
    public boolean isHealthGainInHudEnabled() { return enableHealthGainInHud; }
    public boolean isStaminaGainInHudEnabled() { return enableStaminaGainInHud; }
    public boolean isKiGainInHudEnabled() { return enableKiGainInHud; }

    // Setters for Client related Configs
    public void setTelegraphedAttacks(boolean value) {
        enableTelegraphedAttacks = value;
        ConfigManager.saveClientConfig();
    }

    public void setHealthGain(boolean value) {
        enableHealthGain = value;
        ConfigManager.saveClientConfig();
    }
    public void setStaminaGain(boolean value) {
        enableStaminaGain = value;
        ConfigManager.saveClientConfig();
    }
    public void setKiGain(boolean value) {
        enableKiGain = value;
        ConfigManager.saveClientConfig();
    }
    public void setHealthGainInHud(boolean value) {
        enableHealthGainInHud = value;
        ConfigManager.saveClientConfig();
    }
    public void setStaminaGainInHud(boolean value) {
        enableStaminaGainInHud = value;
        ConfigManager.saveClientConfig();
    }
    public void setKiGainInHud(boolean value) {
        enableKiGainInHud = value;
        ConfigManager.saveClientConfig();
    }
}
