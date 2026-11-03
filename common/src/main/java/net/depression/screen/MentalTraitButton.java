package net.depression.screen;

import net.depression.Depression;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class MentalTraitButton extends Button {
    public static final ResourceLocation FRAME_LOCATION = ResourceLocation.fromNamespaceAndPath(Depression.MOD_ID, "textures/mental_trait/frame_16.png");
    private final MentalTraitSelectionScreen screen;
    private final int baseY;
    private final String id;
    private final ResourceLocation icon;
    public final int dis;
    public final int disFrame;

    public MentalTraitButton(MentalTraitSelectionScreen screen, int baseY, String id) {
        super(0, 0, screen.screenHeight - 8, screen.screenHeight / 4,
                Component.translatable("depression.mental_trait." + id),
                button -> ((MentalTraitButton) button).onPressButton(), DEFAULT_NARRATION);
        this.screen = screen;
        this.baseY = baseY;
        this.id = id;
        this.icon = ResourceLocation.fromNamespaceAndPath(Depression.MOD_ID, "textures/mental_trait/" + id + ".png");
        this.dis = (height - 16) / 2;
        this.disFrame = (height - 24) / 2;
    }

    public void onPressButton() {
        Minecraft.getInstance().setScreen(new MentalTraitInfoScreen(screen, id, icon));
    }

    @Override
    public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        setX(screen.baseX);
        setY(screen.baseY + baseY - screen.getSliderValue());
        if (getBottom() <= screen.baseY || getY() >= screen.baseY + screen.screenHeight) return;
        graphics.enableScissor(screen.baseX, screen.baseY, screen.baseX + width, screen.baseY + screen.screenHeight);
        ResourceLocation background = ResourceLocation.withDefaultNamespace(!active ? "widget/button_disabled" : isHoveredOrFocused() ? "widget/button_highlighted" : "widget/button");
        graphics.blitSprite(background, getX(), getY(), width, height);
        graphics.blit(FRAME_LOCATION, getX() + disFrame, getY() + disFrame, 0, 0, 24, 24, 24, 24);
        graphics.blit(icon, getX() + dis, getY() + dis, 0, 0, 16, 16, 16, 16);
        int textX = getX() + disFrame + 24 + (width - disFrame - 24) / 2;
        graphics.drawCenteredString(Minecraft.getInstance().font, getMessage(), textX, getY() + height / 2 - 4, 0xFFFFFF);
        graphics.disableScissor();
    }

    @Override
    public boolean mouseClicked(double x, double y, int button) {
        return y >= screen.baseY && y < screen.baseY + screen.screenHeight && super.mouseClicked(x, y, button);
    }
}
