package com.iinitial.dmztweaks.client;

import com.iinitial.dmztweaks.client.command.DMZTweaksClientCommand;
import com.iinitial.dmztweaks.common.config.ConfigManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;

@OnlyIn(Dist.CLIENT)
public final class DMZTweaksClient {
    public static void init() {
        // reads and loads the client config
        ConfigManager.loadClientConfig();
        // registers the "/dmztweaks settings" command
        MinecraftForge.EVENT_BUS.addListener(DMZTweaksClientCommand::register);
    }
}
