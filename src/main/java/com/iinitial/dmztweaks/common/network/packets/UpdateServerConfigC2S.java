package com.iinitial.dmztweaks.common.network.packets;

import com.iinitial.dmztweaks.common.config.ConfigManager;
import com.iinitial.dmztweaks.common.network.NetworkHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class UpdateServerConfigC2S {
    private final String key;
    private final boolean value;

    public UpdateServerConfigC2S(String key, boolean value) {
        this.key = key;
        this.value = value;
    }

    public UpdateServerConfigC2S(FriendlyByteBuf buffer) {
        this.key = buffer.readUtf();
        this.value = buffer.readBoolean();
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeUtf(key);
        buffer.writeBoolean(value);
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player == null) return;

            if (!player.hasPermissions(2)) return;

            var config = ConfigManager.server();
            switch (key) {
                case "enableBalancedClasses" -> config.setBalancedClasses(value);
                case "enableTelegraphedAttacks" -> config.setTelegraphedAttacks(value);
                case "enablePreciseHealthGain" -> config.setPreciseHealthGain(value);
                case "enablePreciseStaminaGain" -> config.setPreciseStaminaGain(value);
                case "enablePreciseKiGain" -> config.setPreciseKiGain(value);
                default -> { /* ignore */ }
            }

            NetworkHandler.INSTANCE.send(
                    net.minecraftforge.network.PacketDistributor.ALL.noArg(),
                    new SyncServerConfigS2C(config)
            );
        });
        context.setPacketHandled(true);
    }
}