package net.depression.mixin.client;

import net.minecraft.client.gui.components.AbstractSliderButton;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(AbstractSliderButton.class)
public interface AbstractSliderButtonMixin {
    @Invoker("getSprite")
    public net.minecraft.resources.ResourceLocation invokeGetTextureY();
    @Invoker("getHandleSprite")
    public net.minecraft.resources.ResourceLocation invokeGetHandleTextureY();
}