package com.iinitial.dmztweaks.server.command;

import com.iinitial.dmztweaks.common.config.ConfigManager;
import com.iinitial.dmztweaks.common.network.NetworkHandler;
import com.iinitial.dmztweaks.common.network.packets.SyncServerConfigS2C;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.network.PacketDistributor;

public final class DMZTweaksServerCommand {
    private DMZTweaksServerCommand() {}

    /*  "/dmztweaks reload"
     *   This is a server command so that clients cannot spoof their permissions to
     *   force reload server configs for this mod
     */
    public static void register(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        dispatcher.register(Commands.literal("dmztweaks")
                .then(Commands.literal("reload")
                        .requires(source -> source.hasPermission(2))
                        .executes(context -> {
                            CommandSourceStack source = context.getSource();
                            ConfigManager.loadServerConfig();
                            source.sendSuccess(() -> Component.literal("dmztweaks has reloaded."), true);

                            if (source.getEntity() instanceof ServerPlayer player) {
                                NetworkHandler.INSTANCE.send(
                                        PacketDistributor.PLAYER.with(() -> player),
                                        new SyncServerConfigS2C(ConfigManager.server())
                                );
                            }
                            return 1;
                        })
                )
        );
    }
}