package com.iinitial.dmztweaks;

import com.iinitial.dmztweaks.client.DMZTweaksClient;
import com.iinitial.dmztweaks.common.config.ConfigManager;
import com.iinitial.dmztweaks.common.network.NetworkHandler;
import com.iinitial.dmztweaks.common.network.packets.SyncServerConfigS2C;
import com.iinitial.dmztweaks.server.command.DMZTweaksServerCommand;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.network.PacketDistributor;

@Mod(DMZTweaks.MOD_ID)
public class DMZTweaks {
    public static final String MOD_ID = "dmztweaks";
    public static final String MOD_VERSION = FMLLoader.getLoadingModList().getModFileById(MOD_ID).versionString();

    public DMZTweaks() {
        // Sets up the SimpleChannel
        NetworkHandler.register();
        // Safe way to run client code from a class that also loads on dedicated servers
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> DMZTweaksClient::init);
        // Checks if a server (singleplayer or dedicated) is about to start
        MinecraftForge.EVENT_BUS.addListener(this::onServerStart);
        // When server is about to start, register command
        MinecraftForge.EVENT_BUS.addListener(DMZTweaksServerCommand::register);
        // Pushes the current server config to each player the moment they join.
        MinecraftForge.EVENT_BUS.addListener(this::onPlayerLoggedIn);
    }

    // When server is about to start, read server configs and apply to server
    private void onServerStart(ServerAboutToStartEvent event) {
        ConfigManager.loadServerConfig();
    }

    private void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        NetworkHandler.INSTANCE.send(
                PacketDistributor.PLAYER.with(() -> player),
                new SyncServerConfigS2C(ConfigManager.server())
        );
    }
}