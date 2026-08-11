package com.iinitial.dmztweaks.client.command;

import com.iinitial.dmztweaks.client.gui.SettingsScreen;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraftforge.client.event.RegisterClientCommandsEvent;

public final class DMZTweaksClientCommand {
    private DMZTweaksClientCommand() {}

    // "/dmztweaks settings"
    public static void register(RegisterClientCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        dispatcher.register(Commands.literal("dmztweaks")
                .then(Commands.literal("settings")
                        .executes(context -> {
                            Minecraft.getInstance().setScreen(new SettingsScreen());
                            return 1;
                        })
                )
        );
    }
}