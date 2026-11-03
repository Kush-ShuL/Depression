package net.depression.network;

import dev.architectury.networking.NetworkManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

/** Supplies registry context required by 1.21 custom payloads. */
public final class PacketBuffers {
    private PacketBuffers() {}

    public static void sendToPlayer(ServerPlayer player, ResourceLocation id, FriendlyByteBuf buf) {
        NetworkManager.sendToPlayer(player, id, new RegistryFriendlyByteBuf(buf, player.registryAccess()));
    }

    public static ResourceLocation serverboundId(ResourceLocation id) {
        return id.withSuffix("_c2s");
    }

    @Environment(EnvType.CLIENT)
    public static void sendToServer(ResourceLocation id, FriendlyByteBuf buf) {
        var connection = Minecraft.getInstance().getConnection();
        if (connection != null) {
            NetworkManager.sendToServer(serverboundId(id), new RegistryFriendlyByteBuf(buf, connection.registryAccess()));
        }
    }
}
