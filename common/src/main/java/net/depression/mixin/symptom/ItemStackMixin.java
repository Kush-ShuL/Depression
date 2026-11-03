package net.depression.mixin.symptom;

import net.depression.item.MedicineItem;
import net.depression.util.Tools;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @Inject(method = "getUseDuration", at = @At("RETURN"), cancellable = true)
    private void depression$useDuration(LivingEntity entity, CallbackInfoReturnable<Integer> cir) {
        ItemStack stack = (ItemStack) (Object) this;
        if (entity instanceof Player player && stack.has(DataComponents.FOOD) && !(stack.getItem() instanceof MedicineItem)) {
            int level = Tools.getPlayerMentalHealthLevel(player);
            if (level >= 2) cir.setReturnValue(cir.getReturnValue() * level);
        }
    }
}
