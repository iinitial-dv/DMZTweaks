package com.iinitial.dmztweaks;

import com.iinitial.dmztweaks.client.DMZTweaksClient;
import com.iinitial.dmztweaks.common.config.ConfigManager;
import com.iinitial.dmztweaks.common.network.NetworkHandler;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLLoader;

@Mod(DMZTweaks.MOD_ID)
public class DMZTweaks {
    public static final String MOD_ID = "dmztweaks";
    public static final String MOD_VERSION = FMLLoader.getLoadingModList().getModFileById(MOD_ID).versionString();

    public DMZTweaks() {
        // NetworkHandler.CHANNEL.getClass();

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> DMZTweaksClient::init);
        MinecraftForge.EVENT_BUS.addListener(this::onServerStart);
    }

    private void onServerStart(ServerAboutToStartEvent event) {
        ConfigManager.loadServerConfig();
    }
}
