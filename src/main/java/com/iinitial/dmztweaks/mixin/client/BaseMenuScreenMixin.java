package com.iinitial.dmztweaks.mixin.client;

import com.dragonminez.client.gui.buttons.CustomTextureButton;
import com.dragonminez.client.gui.character.*;
import com.dragonminez.client.gui.character.util.BaseMenuScreen;
import com.dragonminez.client.gui.character.util.ScaledScreen;
import com.dragonminez.common.init.MainSounds;
import com.iinitial.dmztweaks.client.gui.SettingsScreen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// remap = false: BaseMenuScreen belongs to dragonminez, not vanilla, so there's
// no SRG mapping for it to look up - without this the launch fails with
// "Unable to locate obfuscation mapping for @Inject target"
@Mixin(value = BaseMenuScreen.class, remap = false)
public abstract class BaseMenuScreenMixin extends ScaledScreen {

    // same texture dragonminez itself uses for the vanilla nav icons
    private static final ResourceLocation SCREEN_BUTTONS =
            ResourceLocation.fromNamespaceAndPath("dragonminez", "textures/gui/buttons/menubuttons.png");
    private static final ResourceLocation TWEAKS_BUTTON =
            ResourceLocation.fromNamespaceAndPath("dmztweaks", "textures/gui/buttons/menu_button.png");

    protected BaseMenuScreenMixin(Component title) {
        super(title);
    }

    // switchMenu() only exists on BaseMenuScreen, not on ScaledScreen, so we
    // need to shadow it to call it from here
    @Shadow
    protected abstract void switchMenu(Screen nextScreen);

    // cancels the vanilla method entirely and rebuilds the row from scratch,
    // so the settings tab can sit dead center instead of tacked on the end
    @Inject(method = "initNavigationButtons", at = @At("HEAD"), cancellable = true)
    private void dmztweaks$rebuildNavigationButtons(CallbackInfo ci) {
        int centerX = getUiWidth() / 2;
        int bottomY = getUiHeight() - 30;

        dmztweaks$navButton(centerX - 130, bottomY, SCREEN_BUTTONS, 120, btn -> switchMenu(new PartyMenuScreen()));
        dmztweaks$navButton(centerX - 90, bottomY, SCREEN_BUTTONS, 0, btn -> switchMenu(new CharacterStatsScreen()));
        dmztweaks$navButton(centerX - 50, bottomY, SCREEN_BUTTONS, 20, btn -> switchMenu(new SkillsMenuScreen()));
        dmztweaks$navButton(centerX - 10, bottomY, SCREEN_BUTTONS, 60, btn -> switchMenu(new QuestTreeScreen()));
        dmztweaks$navButton(centerX + 30, bottomY, SCREEN_BUTTONS, 140, btn -> switchMenu(new MinigamesScreen()));
        dmztweaks$navButton(centerX + 70, bottomY, SCREEN_BUTTONS, 100, btn -> switchMenu(new ConfigMenuScreen()));
        dmztweaks$navButton(centerX + 110, bottomY, TWEAKS_BUTTON, 0, btn -> switchMenu(new SettingsScreen()));

        ci.cancel();
    }

    // u is the normal-state column on the sheet; hover art sits 20px directly below it,
    // same convention BaseMenuScreen's own icons use
    @Unique
    private void dmztweaks$navButton(int x, int y, ResourceLocation texture, int u, Button.OnPress onPress) {
        this.addRenderableWidget(
                new CustomTextureButton.Builder()
                        .position(x, y)
                        .size(20, 20)
                        .texture(texture)
                        .textureSize(20, 20)
                        .textureCoords(u, 0, u, 20)
                        .onPress(onPress)
                        .sound(MainSounds.UI_MENU_SWITCH.get())
                        .build()
        );
    }
}