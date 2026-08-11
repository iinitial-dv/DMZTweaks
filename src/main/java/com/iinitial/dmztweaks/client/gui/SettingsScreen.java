package com.iinitial.dmztweaks.client.gui;

import com.dragonminez.client.gui.buttons.SwitchButton;
import com.dragonminez.client.gui.character.util.BaseMenuScreen;
import com.dragonminez.client.util.ScrollbarState;
import com.dragonminez.client.util.TextUtil;
import com.dragonminez.common.init.MainSounds;
import com.iinitial.dmztweaks.common.config.ConfigManager;
import com.iinitial.dmztweaks.common.network.NetworkHandler;
import com.iinitial.dmztweaks.common.network.packets.UpdateServerConfigC2S;
import com.iinitial.dmztweaks.common.network.packets.ViewClientServerConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

public class SettingsScreen extends BaseMenuScreen {
    private static final ResourceLocation MENU = ResourceLocation.fromNamespaceAndPath("dmztweaks", "textures/gui/menu/settings_menu.png");
    private static final int MENU_WIDTH = 256;
    private static final int MENU_HEIGHT = 182;
    private static final int TEX_WIDTH = 256;
    private static final int TEX_HEIGHT = 256;
    private static final int TAB_WIDTH = 90;
    private static final int TAB_HEIGHT = 13;
    private static final int TAB_Y = 21;
    private static final int MAX_VISIBLE_ROWS = 6;
    private static final int ROW_HEIGHT = 20;
    private static final int SCROLLBAR_WIDTH = 4;
    private static final int SCROLLBAR_FROM_RIGHT = 23;
    private final ScrollbarState scrollBar = new ScrollbarState();
    private final List<SwitchButton> settingSwitches = new ArrayList<>();
    private final List<String> settingLabels = new ArrayList<>();
    private Tab currentTab = Tab.CLIENT;
    private boolean isOp;
    private int menuX, menuY;
    private int scrollOffset = 0;
    private int maxScroll = 0;
    private int totalRows = 0;

    public SettingsScreen() {
        super(Component.literal("dmztweaks settings menu"));
    }

    @Override
    protected void init() {
        super.init();
        this.isOp = Minecraft.getInstance().player != null && Minecraft.getInstance().player.hasPermissions(2);
        // getUiWidth()/getUiHeight() are NOT the same as this.width/this.height.
        // They're the already scaled dimensions ScaledScreen uses so the menu
        // renders at a consistent size regardless of the player's actual window
        // size / GUI scale setting.
        this.menuX = this.getUiWidth() / 2 - MENU_WIDTH / 2;
        this.menuY = this.getUiHeight() / 2 - MENU_HEIGHT / 2;
        this.initTabs();
        this.rebuildSettingsList();
    }

    private void initTabs() {
        int tabY = menuY + TAB_Y;
        int tabsStartX = menuX + (MENU_WIDTH - (TAB_WIDTH * 2)) / 2;

        // client tab
        this.addRenderableWidget(new TabButton(
                tabsStartX - 5, tabY, Tab.CLIENT,
                0, 183, 0, 197,
                b -> switchTab(Tab.CLIENT)));

        // server tab
        if (isOp) {
            this.addRenderableWidget(new TabButton(
                    tabsStartX + 5 + TAB_WIDTH, tabY, Tab.SERVER,
                    91, 183, 91, 197,
                    b -> switchTab(Tab.SERVER)));
        }
    }

    private void switchTab(Tab tab) {
        if (tab == Tab.SERVER && !isOp) return; // shouldn't happen, but just in case
        this.currentTab = tab;
        this.scrollOffset = 0; // reset scroll so switching tabs doesn't leave you scrolled into empty space on the shorter list
        rebuildSettingsList();
    }

    private void rebuildSettingsList() {
        // clear out the old tab's switches before adding the new tab's. this is
        // what makes tabs "replace" each other
        for (SwitchButton button : settingSwitches) this.removeWidget(button);
        settingSwitches.clear();
        settingLabels.clear();

        // figure out how much there is to scroll through for whichever tab is active
        List<ToggleRow> rows = currentTab == Tab.CLIENT ? buildClientRows() : buildServerRows();
        this.totalRows = rows.size();
        this.maxScroll = Math.max(0, rows.size() - MAX_VISIBLE_ROWS);
        this.scrollOffset = Math.min(this.scrollOffset, this.maxScroll); // clamp in case rows shrank

        int startY = menuY + TAB_Y + TAB_HEIGHT + 14;
        int switchX = menuX + MENU_WIDTH - 45;
        // only build widgets for rows currently scrolled into view. everything
        // above/below the visible window doesn't get a widget at all
        int visibleEnd = Math.min(scrollOffset + MAX_VISIBLE_ROWS, rows.size());
        int trackHeight = MAX_VISIBLE_ROWS * ROW_HEIGHT;

        // tells the scrollbar where its track lives
        scrollBar.update(menuX + MENU_WIDTH - SCROLLBAR_FROM_RIGHT, SCROLLBAR_WIDTH, startY - 5, trackHeight, maxScroll);

        for (int i = scrollOffset; i < visibleEnd; i++) {
            ToggleRow row = rows.get(i);
            int y = startY + ((i - scrollOffset) * ROW_HEIGHT);
            boolean isOn = row.getter().getAsBoolean();

            SwitchButton switchButton = new SwitchButton(switchX, y, isOn, Component.empty(), button -> {
                boolean current = button.isActive();
                boolean newValue = !current;
                row.setter().accept(newValue);
                ((SwitchButton) button).setActive(newValue);

                var player = Minecraft.getInstance().player;
                if (player != null) {
                    player.playSound(current ? MainSounds.SWITCH_OFF.get() : MainSounds.SWITCH_ON.get());
                }
            });
            settingSwitches.add(switchButton);
            settingLabels.add(row.label());
            this.addRenderableWidget(switchButton);
        }
    }

    private List<ToggleRow> buildClientRows() {
        var client = ConfigManager.client();
        List<ToggleRow> rows = new ArrayList<>();
        rows.add(new ToggleRow("Show Telegraphed Attacks", client::isTelegraphedAttacksShown, client::setTelegraphedAttacks));
        rows.add(new ToggleRow("Show Precise Health Gain", client::isPreciseHealthGainShown, client::setPreciseHealthGain));
        rows.add(new ToggleRow("Show Precise Stamina Gain", client::isPreciseStaminaGainShown, client::setPreciseStaminaGain));
        rows.add(new ToggleRow("Show Precise Ki Gain", client::isPreciseKiGainShown, client::setPreciseKiGain));
        rows.add(new ToggleRow("Show Health Gain in HUD", client::isHealthGainInHudShown, client::setHealthGainInHud));
        rows.add(new ToggleRow("Show Stamina Gain in HUD", client::isStaminaGainInHudShown, client::setStaminaGainInHud));
        rows.add(new ToggleRow("Show Ki Gain in HUD", client::isKiGainInHudShown, client::setKiGainInHud));
        return rows;
    }

    private List<ToggleRow> buildServerRows() {
        // Reads from ViewClientServerConfig (the last value the server pushed to us
        // via SyncServerConfigS2C), NOT ConfigManager.server(). on a dedicated
        // server that's a separate instance only on the client.
        List<ToggleRow> rows = new ArrayList<>();
        rows.add(new ToggleRow("Enable Balanced Classes", ViewClientServerConfig::isBalancedClassesEnabled, v -> sendServerToggle("enableBalancedClasses", v)));
        rows.add(new ToggleRow("Enable Telegraphed Attacks", ViewClientServerConfig::isTelegraphedAttacksEnabled, v -> sendServerToggle("enableTelegraphedAttacks", v)));
        rows.add(new ToggleRow("Enable Better Minigames", ViewClientServerConfig::isBetterMinigamesEnabled, v -> sendServerToggle("enableBetterMinigames", v)));
        rows.add(new ToggleRow("Enable Precise Health Gain", ViewClientServerConfig::isPreciseHealthGainEnabled, v -> sendServerToggle("enablePreciseHealthGain", v)));
        rows.add(new ToggleRow("Enable Precise Stamina Gain", ViewClientServerConfig::isPreciseStaminaGainEnabled, v -> sendServerToggle("enablePreciseStaminaGain", v)));
        rows.add(new ToggleRow("Enable Precise Ki Gain", ViewClientServerConfig::isPreciseKiGainEnabled, v -> sendServerToggle("enablePreciseKiGain", v)));
        return rows;
    }

    private void sendServerToggle(String key, boolean value) {
        // Server re-checks op permission on its end before applying this.
        // this client-side gating (isOp / the Server tab not existing for
        // non-ops) is UX only, never the actual security boundary.
        NetworkHandler.INSTANCE.send(
                net.minecraftforge.network.PacketDistributor.SERVER.noArg(),
                new UpdateServerConfigC2S(key, value)
        );
    }

    private int[] scrollRegion() {
        int left = menuX + 19;
        int right = menuX + MENU_WIDTH - 19;
        int trackY = menuY + TAB_Y + TAB_HEIGHT + 9;
        int trackHeight = MAX_VISIBLE_ROWS * ROW_HEIGHT;
        return new int[]{left, right, trackY, trackY + trackHeight};
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        double uiMouseX = toUiX(mouseX);
        double uiMouseY = toUiY(mouseY);

        if (scrollBar.tryStartDrag(uiMouseX, uiMouseY)) {
            scrollOffset = Math.round(scrollBar.scrollFor(uiMouseY));
            rebuildSettingsList();
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        double uiMouseY = toUiY(mouseY);

        if (scrollBar.isDragging()) {
            scrollOffset = Math.round(scrollBar.scrollFor(uiMouseY));
            rebuildSettingsList();
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (scrollBar.isDragging()) {
            scrollBar.stopDrag();
            return true;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        double uiMouseX = toUiX(mouseX);
        double uiMouseY = toUiY(mouseY);

        int[] region = scrollRegion();
        boolean overRegion = uiMouseX >= region[0] && uiMouseX <= region[1]
                && uiMouseY >= region[2] && uiMouseY <= region[3];

        if (overRegion && maxScroll > 0) {
            int amount = (int) Math.signum(delta);
            int newOffset = Math.max(0, Math.min(maxScroll, scrollOffset - amount));
            if (newOffset != scrollOffset) {
                scrollOffset = newOffset;
                rebuildSettingsList();
            }
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, delta);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        if (isNotAnimating()) this.renderBackground(graphics);
        int uiMouseX = (int) Math.round(toUiX(mouseX));
        int uiMouseY = (int) Math.round(toUiY(mouseY));

        beginUiScale(graphics);
        applyZoom(graphics, partialTick);

        graphics.blit(MENU, menuX, menuY, 0, 0, MENU_WIDTH, MENU_HEIGHT, TEX_WIDTH, TEX_HEIGHT);
        super.render(graphics, uiMouseX, uiMouseY, partialTick);

        // labels live outside SwitchButton itself (it's just the switch graphic,
        // no text), so draw them manually next to each one every frame
        for (int i = 0; i < settingSwitches.size(); i++) {
            SwitchButton button = settingSwitches.get(i);
            String label = settingLabels.get(i);
            TextUtil.drawStringWithBorder(graphics, this.font, label,
                    menuX + 24, button.getY() + 1, 0xFFFFFFFF);
        }

        // only draw a scrollbar at all if there's actually more content than fits
        if (maxScroll > 0) {
            int barX = menuX + MENU_WIDTH - SCROLLBAR_FROM_RIGHT;
            int trackY = menuY + TAB_Y + TAB_HEIGHT + 9;
            int trackHeight = MAX_VISIBLE_ROWS * ROW_HEIGHT;

            // scrollbar track - three fills to fake a simple beveled border
            graphics.fill(barX, trackY + 1, barX + 1, trackY + trackHeight - 1, 0xFF13101A);
            graphics.fill(barX + 1, trackY, barX + SCROLLBAR_WIDTH - 1, trackY + trackHeight, 0xFF13101A);
            graphics.fill(barX + SCROLLBAR_WIDTH - 1, trackY + 1, barX + SCROLLBAR_WIDTH, trackY + trackHeight - 1, 0xFF13101A);

            // scrollbar handle - sized to how much of the list is visible,
            // positioned based on how scrolled you are
            float visiblePercent = (float) MAX_VISIBLE_ROWS / totalRows;
            int handleHeight = Math.max(20, (int) (trackHeight * visiblePercent));
            float scrollPercent = (float) scrollOffset / maxScroll;
            int handleY = trackY + (int) ((trackHeight - handleHeight) * scrollPercent);

            graphics.fill(barX, handleY + 2, barX + 1, handleY + handleHeight - 2, 0xFF4E3f6B);
            graphics.fill(barX + 1, handleY + 1, barX + SCROLLBAR_WIDTH - 1, handleY + handleHeight - 1, 0xFF4E3f6B);
            graphics.fill(barX + SCROLLBAR_WIDTH - 1, handleY + 2, barX + SCROLLBAR_WIDTH, handleY + handleHeight - 2, 0xFF4E3f6B);
        }
        endUiScale(graphics);
    }

    private enum Tab {CLIENT, SERVER}

    private record ToggleRow(String label, BooleanSupplier getter, Consumer<Boolean> setter) {
    }

    // Uses my "selected" texture whenever it's the active tab, or you're just hovering over it
    private class TabButton extends Button {
        private final Tab tab;
        private final int uUnselected, vUnselected, uSelected, vSelected;

        TabButton(int x, int y, Tab tab, int uUnselected, int vUnselected, int uSelected, int vSelected, OnPress onPress) {
            super(x, y, TAB_WIDTH, TAB_HEIGHT, Component.empty(), onPress, DEFAULT_NARRATION);
            this.tab = tab;
            this.uUnselected = uUnselected;
            this.vUnselected = vUnselected;
            this.uSelected = uSelected;
            this.vSelected = vSelected;
        }

        @Override
        protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
            boolean showSelected = tab == currentTab || this.isHovered();
            int u = showSelected ? uSelected : uUnselected;
            int v = showSelected ? vSelected : vUnselected;
            graphics.blit(MENU, this.getX(), this.getY(), u, v, TAB_WIDTH, TAB_HEIGHT, TEX_WIDTH, TEX_HEIGHT);
        }

        @Override
        public void playDownSound(SoundManager handler) {
            handler.play(SimpleSoundInstance.forUI(MainSounds.UI_MENU_SWITCH.get(), 1.0F));
        }
    }

}