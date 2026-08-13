package com.iinitial.dmztweaks.client.events;

import com.iinitial.dmztweaks.DMZTweaks;
import com.iinitial.dmztweaks.client.hud.HudManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = DMZTweaks.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class DMZTweaksClientEvents {

    @SubscribeEvent
    public static void registerGuiOverlays(RegisterGuiOverlaysEvent e) {
        e.registerAbove(VanillaGuiOverlay.PLAYER_HEALTH.id(), "regen_hud", HudManager.HUD_REGEN);
    }
}