package com.iinitial.dmztweaks.common.network.packets;

import com.iinitial.dmztweaks.common.config.ServerConfig;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SyncServerConfigS2C {
    private final boolean enableBalancedClasses;
    private final boolean enableTelegraphedAttacks;
    private final boolean enableBetterMinigames;
    private final boolean enablePreciseHealthGain;
    private final boolean enablePreciseKiGain;
    private final boolean enablePreciseStaminaGain;

    public SyncServerConfigS2C(ServerConfig config) {
        this.enableBalancedClasses = config.enableBalancedClasses;
        this.enableTelegraphedAttacks = config.enableTelegraphedAttacks;
        this.enableBetterMinigames = config.enableBetterMinigames;
        this.enablePreciseHealthGain = config.enablePreciseHealthGain;
        this.enablePreciseKiGain = config.enablePreciseKiGain;
        this.enablePreciseStaminaGain = config.enablePreciseStaminaGain;
    }

    public SyncServerConfigS2C(FriendlyByteBuf buffer) {
        this.enableBalancedClasses = buffer.readBoolean();
        this.enableTelegraphedAttacks = buffer.readBoolean();
        this.enableBetterMinigames = buffer.readBoolean();
        this.enablePreciseHealthGain = buffer.readBoolean();
        this.enablePreciseKiGain = buffer.readBoolean();
        this.enablePreciseStaminaGain = buffer.readBoolean();
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeBoolean(enableBalancedClasses);
        buffer.writeBoolean(enableTelegraphedAttacks);
        buffer.writeBoolean(enableBetterMinigames);
        buffer.writeBoolean(enablePreciseHealthGain);
        buffer.writeBoolean(enablePreciseKiGain);
        buffer.writeBoolean(enablePreciseStaminaGain);
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            ViewClientServerConfig.update(
                    enableBalancedClasses,
                    enableTelegraphedAttacks,
                    enableBetterMinigames,
                    enablePreciseHealthGain,
                    enablePreciseKiGain,
                    enablePreciseStaminaGain
            );
        });
        context.setPacketHandled(true);
    }

}
