package com.iinitial.dmztweaks.client;

import com.iinitial.dmztweaks.common.config.ConfigManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public final class DMZTweaksClient {
    public static void init() {
        ConfigManager.loadClientConfig();
    }
}
