package com.iinitial.dmztweaks.client.hud;

import com.dragonminez.client.util.TextUtil;
import com.dragonminez.common.config.ConfigManager;
import com.dragonminez.common.stats.StatsCapability;
import com.dragonminez.common.stats.StatsProvider;
import com.dragonminez.common.stats.character.Resources;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public class HudManager {
    private static final ResourceLocation XV_HUD = ResourceLocation.fromNamespaceAndPath("dmztweaks", "textures/gui/hud/xenoverse_hud.png");
    private static final int TEX_WIDTH = 256;
    private static final int TEX_HEIGHT = 256;
    private static final int XV_Y = 0;
    private static final int XV_MID_WIDTH = 3;
    private static final int XV_HP_HEIGHT = 9,  XV_KI_HEIGHT = 8,   XV_STM_HEIGHT = 7;
    private static final int XV_HP_WIDTH_1 = 9, XV_KI_WIDTH_1 = 8,  XV_STM_WIDTH_1 = 7;
    private static final int XV_HP_START_X = 0, XV_KI_START_X = 24, XV_STM_START_X = 46;
    private static final int XV_HP_MID_X = 10,  XV_KI_MID_X = 33,   XV_STM_MID_X = 54;
    private static final int XV_HP_END_X = 14,  XV_KI_END_X = 37,   XV_STM_END_X = 58;
    private static final int REGEN_COLOR = 0x55FF7A;
    private static final int NO_REGEN_COLOR = 0xAAAAAA;
    private static final int DRAIN_COLOR = 0xFF5555;
    // Xenoverse HUD
    private static final float XV_BASE_SCALE = 2.25f;
    private static final float XV_BASE_WIDTH = 184.0f;
    private static final float XV_HP_X = 30, XV_HP_Y = 15;
    private static final float XV_HP_WIDTH = 139.0f;
    private static final float XV_KI_X = 28, XV_KI_Y = 23;
    private static final float XV_KI_WIDTH = 116.0f;
    private static final float XV_STM_X = 42, XV_STM_Y = 30;
    private static final float XV_STM_WIDTH = 84.0f;
    private static final float XV_CHIP_CLEARANCE = 2;
    private static final float XV_NUMBER_SCALE = 0.45f;
    private static final float XV_NUMBER_NUDGE_X = 0.5f;
    private static final float XV_NUMBER_NUDGE_Y = 0.35f;
    private static final float XV_CHIP_BG_X = -7;
    private static final float XV_CHIP_BG_Y = -2;
    // Alternate HUD
    private static final float ALT_HUD_SCALE = 1.25f;
    private static final float ALT_HP_X = -95.0f, ALT_HP_Y = -49.0f;
    private static final float ALT_KI_X = -95.0f, ALT_KI_Y = -50.0f;
    private static final float ALT_STM_X = -5.0f, ALT_STM_Y = -50.0f;
    private static final float ALT_BAR_FILL_WIDTH = 76.0f; // AlternativeHUD.BAR_MAX_WIDTH
    private static final int ALT_HP_FILL_X = 2, ALT_HP_WIDTH_ADJUST = 7;
    private static final int ALT_KI_FILL_X = 3, ALT_KI_WIDTH_ADJUST = 7;
    private static final int ALT_STM_FILL_X = 2, ALT_STM_WIDTH_ADJUST = -5;
    private static final int ALT_STM_ENDCAP_X = 77, ALT_STM_ENDCAP_WIDTH = 4;
    private static final float ALT_NUMBER_CLEARANCE = 2;
    private static final float ALT_NUMBER_Y = 3;
    private static final float ALT_NUMBER_SCALE = 0.5f;

    public static final IGuiOverlay HUD_REGEN = (forge, graphics, partialTicks, width, height) -> {
        Minecraft mc = Minecraft.getInstance();
        if (mc.options.renderDebug || mc.player == null) return;

        var tweaksConfig = com.iinitial.dmztweaks.common.config.ConfigManager.client();
        boolean showHp = tweaksConfig.isHealthGainInHudShown();
        boolean showKi = tweaksConfig.isKiGainInHudShown();
        boolean showStm = tweaksConfig.isStaminaGainInHudShown();
        if (!showHp && !showKi && !showStm) return;

        StatsProvider.get(StatsCapability.INSTANCE, mc.player).ifPresent(data -> {
            if (!data.getStatus().isHasCreatedCharacter()) return;

            boolean activeCharging = data.getStatus().isChargingKi();
            Resources resources = data.getResources();
            double netHp = data.getHealthRegenPerSecond() - data.getEffectiveHealthDrain();
            double netKi = data.getEnergyRegenPerSecond(activeCharging) - data.getEffectiveEnergyDrain();
            double netStm = data.getStaminaRegenPerSecond() - data.getEffectiveStaminaDrain();
            boolean hpCapped = mc.player.getHealth() >= data.getMaxHealth();
            boolean kiCapped = resources.getCurrentEnergy() >= data.getMaxEnergy();
            boolean stmCapped = resources.getCurrentStamina() >= data.getMaxStamina();

            if (hpCapped && netHp > 0) netHp = 0.0;
            if (kiCapped && netKi > 0) netKi = 0.0;
            if (stmCapped && netStm > 0) netStm = 0.0;

            double hpRegen = showHp ? netHp : Double.NaN;
            double kiRegen = showKi ? netKi : Double.NaN;
            double stmRegen = showStm ? netStm : Double.NaN;

            boolean alternative = ConfigManager.getUserConfig().getAlternativeHud();
            if (alternative) {
                // renderAlternative(graphics, width, height, hpRegen, kiRegen, stmRegen);
            } else {
                renderXenoverse(graphics, width, hpRegen, kiRegen, stmRegen);
            }
        });
    };

    // Render Xenoverse HUD
    private static void renderXenoverse(GuiGraphics graphics, int width, double hpRegen, double kiRegen, double stmRegen) {
        float maxAllowedWidth = width * 0.50f;
        float userScale = ConfigManager.getUserConfig().getXenoverseHudScale();
        float finalScale = Math.min(XV_BASE_SCALE * userScale, maxAllowedWidth / XV_BASE_WIDTH);

        int anchorX = ConfigManager.getUserConfig().getXenoverseHudPosX();
        int anchorY = ConfigManager.getUserConfig().getXenoverseHudPosY();

        graphics.pose().pushPose();
        graphics.pose().translate(anchorX, anchorY, 0);
        graphics.pose().scale(finalScale, finalScale, 1.0f);

        if (!Double.isNaN(hpRegen)) {
            float chipX = XV_HP_X + XV_HP_WIDTH + XV_CHIP_CLEARANCE;
            renderRegen(graphics, hpRegen, chipX, XV_HP_Y, XV_HP_START_X, XV_HP_MID_X, XV_HP_END_X, XV_HP_WIDTH_1, XV_HP_HEIGHT);
        }
        if (!Double.isNaN(kiRegen)) {
            float chipX = XV_KI_X + XV_KI_WIDTH + XV_CHIP_CLEARANCE;
            renderRegen(graphics, kiRegen, chipX, XV_KI_Y, XV_KI_START_X, XV_KI_MID_X, XV_KI_END_X, XV_KI_WIDTH_1, XV_KI_HEIGHT);
        }
        if (!Double.isNaN(stmRegen)) {
            float chipX = XV_STM_X + XV_STM_WIDTH + XV_CHIP_CLEARANCE;
            renderRegen(graphics, stmRegen, chipX, XV_STM_Y, XV_STM_START_X, XV_STM_MID_X, XV_STM_END_X, XV_STM_WIDTH_1, XV_STM_HEIGHT);
        }

        graphics.pose().popPose();
    }

    // Render Alternative HUD
    /*
    private static void renderAlternative(GuiGraphics graphics, int width, int height, double hpRegen, double kiRegen, double stmRegen) {
        int globalAnchorX = width / 2;
        int globalAnchorY = height;

        float hpOffX = ConfigManager.getUserConfig().getHealthBarPosX() / ALT_HUD_SCALE;
        float hpOffY = ConfigManager.getUserConfig().getHealthBarPosY() / ALT_HUD_SCALE;
        float kiOffX = ConfigManager.getUserConfig().getEnergyBarPosX() / ALT_HUD_SCALE;
        float kiOffY = ConfigManager.getUserConfig().getEnergyBarPosY() / ALT_HUD_SCALE;
        float stmOffX = ConfigManager.getUserConfig().getStaminaBarPosX() / ALT_HUD_SCALE;
        float stmOffY = ConfigManager.getUserConfig().getStaminaBarPosY() / ALT_HUD_SCALE;

        graphics.pose().pushPose();
        graphics.pose().translate(globalAnchorX, globalAnchorY, 0);
        graphics.pose().scale(ALT_HUD_SCALE, ALT_HUD_SCALE, 1.0f);

        if (!Double.isNaN(hpRegen)) {
            float rightEdge = ALT_HP_FILL_X + ALT_HP_WIDTH_ADJUST + ALT_BAR_FILL_WIDTH;
            graphics.pose().pushPose();
            graphics.pose().translate(ALT_HP_X + hpOffX, ALT_HP_Y + hpOffY, 0);
            drawRegenNumber(graphics, hpRegen, rightEdge + ALT_NUMBER_CLEARANCE, ALT_NUMBER_Y, ALT_NUMBER_SCALE);
            graphics.pose().popPose();
        }

        if (!Double.isNaN(kiRegen)) {
            float rightEdge = ALT_KI_FILL_X + ALT_KI_WIDTH_ADJUST + ALT_BAR_FILL_WIDTH;
            graphics.pose().pushPose();
            graphics.pose().translate(ALT_KI_X + kiOffX, ALT_KI_Y + kiOffY, 0);
            drawRegenNumber(graphics, kiRegen, rightEdge + ALT_NUMBER_CLEARANCE, ALT_NUMBER_Y, ALT_NUMBER_SCALE);
            graphics.pose().popPose();
        }

        if (!Double.isNaN(stmRegen)) {
            float endcapRightEdge = ALT_STM_ENDCAP_X + ALT_STM_ENDCAP_WIDTH;
            graphics.pose().pushPose();
            graphics.pose().translate(ALT_STM_X + stmOffX, ALT_STM_Y + stmOffY, 0);
            drawRegenNumber(graphics, stmRegen, endcapRightEdge + ALT_NUMBER_CLEARANCE, ALT_NUMBER_Y, ALT_NUMBER_SCALE);
            graphics.pose().popPose();
        }

        graphics.pose().popPose();
    }
     */

    private static void renderRegen(GuiGraphics graphics, double regen, float x, float y, int startU, int midU, int endU, int tileWidth, int barHeight) {
        graphics.pose().pushPose();
        graphics.pose().translate(x, y, 0);

        String text = String.format("%s%.1f/s", regen > 0 ? "+" : "", regen);
        Font font = Minecraft.getInstance().font;
        int midTileCount = Math.max(0, text.length() - 1);
        float chipWidth = (tileWidth * 2) + (midTileCount * (float) XV_MID_WIDTH);

        renderXVChip(graphics, startU, midU, endU, tileWidth, barHeight, midTileCount);

        float chipCenterX = XV_CHIP_BG_X + (chipWidth / 2f);
        float chipCenterY = XV_CHIP_BG_Y + (barHeight / 2f);
        float textWidth = font.width(text) * XV_NUMBER_SCALE;
        float textHeight = font.lineHeight * XV_NUMBER_SCALE;

        float textX = chipCenterX - (textWidth / 2f) + XV_NUMBER_NUDGE_X;
        float textY = chipCenterY - (textHeight / 2f) + XV_NUMBER_NUDGE_Y;

        drawRegenNumber(graphics, regen, text, textX, textY, XV_NUMBER_SCALE);
        graphics.pose().popPose();
    }

    private static void renderXVChip(GuiGraphics graphics, int sX, int mX, int eX, int tileWidth, int barHeight, int midTileCount) {
        int bgX = (int) XV_CHIP_BG_X;
        int bgY = (int) XV_CHIP_BG_Y;

        graphics.blit(XV_HUD, bgX, bgY, sX, XV_Y, tileWidth, barHeight, TEX_WIDTH, TEX_HEIGHT);
        bgX += tileWidth;
        for (int i = 0; i < midTileCount; i++) {
            graphics.blit(XV_HUD, bgX, bgY, mX, XV_Y, XV_MID_WIDTH, barHeight, TEX_WIDTH, TEX_HEIGHT);
            bgX += XV_MID_WIDTH;
        }
        graphics.blit(XV_HUD, bgX, bgY, eX, XV_Y, tileWidth, barHeight, TEX_WIDTH, TEX_HEIGHT);
    }

    private static void drawRegenNumber(GuiGraphics graphics, double regenPerSecond, float x, float y, float scale) {
        String text = String.format("%s%.1f/s", regenPerSecond > 0 ? "+" : "", regenPerSecond);
        drawRegenNumber(graphics, regenPerSecond, text, x, y, scale);
    }

    private static void drawRegenNumber(GuiGraphics graphics, double regenPerSecond, String text, float x, float y, float scale) {
        int color = regenPerSecond > 0 ? REGEN_COLOR : regenPerSecond < 0 ? DRAIN_COLOR : NO_REGEN_COLOR;

        graphics.pose().pushPose();
        graphics.pose().translate(x, y, 0);
        graphics.pose().scale(scale, scale, 1.0f);
        TextUtil.drawStringWithBorder(graphics, Minecraft.getInstance().font, text, 0, 0, color);
        graphics.pose().popPose();
    }
}