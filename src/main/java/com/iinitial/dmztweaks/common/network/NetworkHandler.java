package com.iinitial.dmztweaks.common.network;

import com.iinitial.dmztweaks.common.network.packets.SyncServerConfigS2C;
import com.iinitial.dmztweaks.common.network.packets.UpdateServerConfigC2S;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class NetworkHandler {
    public static SimpleChannel INSTANCE;
    private static int packetId = 0;

    private static int id() {
        return packetId++;
    }

    public static void register() {
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(ResourceLocation.fromNamespaceAndPath("dmztweaks", "network"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        INSTANCE = net;

        // CLIENT -> SERVER
        net.messageBuilder(UpdateServerConfigC2S.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(UpdateServerConfigC2S::new)
                .encoder(UpdateServerConfigC2S::encode)
                .consumerMainThread(UpdateServerConfigC2S::handle)
                .add();

        // SERVER -> CLIENT
        net.messageBuilder(SyncServerConfigS2C.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(SyncServerConfigS2C::new)
                .encoder(SyncServerConfigS2C::encode)
                .consumerMainThread(SyncServerConfigS2C::handle)
                .add();
    }
}