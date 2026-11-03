package net.depression.item.diary;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.network.Filterable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.WrittenBookContent;

import java.util.ArrayList;
import java.util.List;

/** Stores diary metadata and keeps vanilla's written-book component in sync. */
public final class DiaryData {
    private DiaryData() {}

    public static CompoundTag read(ItemStack stack) {
        return stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
    }

    public static void write(ItemStack stack, CompoundTag tag) {
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
        List<Filterable<Component>> pages = new ArrayList<>();
        ListTag rawPages = tag.getList("pages", 8);
        for (int i = 0; i < Math.min(rawPages.size(), 100); i++) {
            pages.add(Filterable.passThrough(Component.literal(rawPages.getString(i))));
        }
        String title = tag.getString("title");
        if (title.length() > 32) title = title.substring(0, 32);
        stack.set(DataComponents.WRITTEN_BOOK_CONTENT, new WrittenBookContent(
                Filterable.passThrough(title), tag.getString("author"), 0, List.copyOf(pages), true));
    }
}
