package net.depression.client.screen;

import net.minecraft.client.gui.screens.inventory.BookViewScreen;
import net.minecraft.world.item.ItemStack;

/** Marks a diary view so the shared book renderer can select its background. */
public class DiaryAccess extends BookViewScreen {
    public DiaryAccess(ItemStack stack) {
        super(BookViewScreen.BookAccess.fromItem(stack));
    }
}
