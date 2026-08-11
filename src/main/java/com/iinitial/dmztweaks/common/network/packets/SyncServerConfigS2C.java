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
    private final boolean enablePreciseStaminaGain;
    private final boolean enablePreciseKiGain;

    public SyncServerConfigS2C(ServerConfig config) {
        this.enableBalancedClasses = config.enableBalancedClasses;
        this.enableTelegraphedAttacks = config.enableTelegraphedAttacks;
        this.enableBetterMinigames = config.enableBetterMinigames;
        this.enablePreciseHealthGain = config.enablePreciseHealthGain;
        this.enablePreciseStaminaGain = config.enablePreciseStaminaGain;
        this.enablePreciseKiGain = config.enablePreciseKiGain;
    }

    public SyncServerConfigS2C(FriendlyByteBuf buffer) {
        this.enableBalancedClasses = buffer.readBoolean();
        this.enableTelegraphedAttacks = buffer.readBoolean();
        this.enableBetterMinigames = buffer.readBoolean();
        this.enablePreciseHealthGain = buffer.readBoolean();
        this.enablePreciseStaminaGain = buffer.readBoolean();
        this.enablePreciseKiGain = buffer.readBoolean();
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeBoolean(enableBalancedClasses);
        buffer.writeBoolean(enableTelegraphedAttacks);
        buffer.writeBoolean(enableBetterMinigames);
        buffer.writeBoolean(enablePreciseHealthGain);
        buffer.writeBoolean(enablePreciseStaminaGain);
        buffer.writeBoolean(enablePreciseKiGain);
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            ViewClientServerConfig.update(
                    enableBalancedClasses,
                    enableTelegraphedAttacks,
                    enableBetterMinigames,
                    enablePreciseHealthGain,
                    enablePreciseStaminaGain,
                    enablePreciseKiGain
            );
        });
        context.setPacketHandled(true);
    }

}
