package net.depression;

import com.mojang.logging.LogUtils;
import dev.architectury.event.events.common.*;
import dev.architectury.networking.NetworkManager;
import dev.architectury.registry.client.keymappings.KeyMappingRegistry;
import net.depression.block.ModBlocks;
import net.depression.config.ServerConfig;
import net.depression.effect.ModEffects;
import net.depression.item.ModCreativeTabs;
import net.depression.item.ModItems;
import net.depression.key.KeyMappings;
import net.depression.listener.*;
import net.depression.mental.PTSDManager;
import net.depression.network.DiaryUpdatePacket;
import net.depression.network.MentalTraitPacket;
import net.depression.network.PlaySoundPacket;
import net.depression.network.RhythmCraftPacket;
import net.depression.rhythmcraft.PlayingChart;
import net.depression.rhythmcraft.ResourceReader;
import net.depression.rhythmcraft.ResourceWriter;
import net.depression.rhythmcraft.RhythmCraftProfile;
import net.depression.server.Registry;
import net.depression.sound.ModSounds;
import net.depression.util.Tools;
import net.depression.world.VillageAdditions;
import org.slf4j.Logger;

import javax.sound.sampled.spi.AudioFileReader;
import java.util.ServiceLoader;

public final class Depression {
    public static final String MOD_ID = "depression";
    public static final String MOD_VERSION = "0.2.3+1.21.1";

    public static final Logger LOGGER = LogUtils.getLogger();

    public static void init() {
        if (dev.architectury.platform.Platform.getEnvironment() == dev.architectury.utils.Env.SERVER) {
            NetworkManager.registerS2CPayloadType(net.depression.network.MentalStatusPacket.EMOTION_PACKET);
            NetworkManager.registerS2CPayloadType(net.depression.network.MentalStatusPacket.MENTAL_HEALTH_PACKET);
            NetworkManager.registerS2CPayloadType(net.depression.network.ActionbarHintPacket.OVERDOSE_PACKET);
            NetworkManager.registerS2CPayloadType(net.depression.network.ActionbarHintPacket.BIPOLAR_PACKET);
            NetworkManager.registerS2CPayloadType(net.depression.network.ActionbarHintPacket.NEARBY_BLOCK_HEAL_PACKET);
            NetworkManager.registerS2CPayloadType(net.depression.network.ActionbarHintPacket.BREAK_BLOCK_HEAL_PACKET);
            NetworkManager.registerS2CPayloadType(net.depression.network.ActionbarHintPacket.KILL_ENTITY_HEAL_PACKET);
            NetworkManager.registerS2CPayloadType(net.depression.network.ActionbarHintPacket.FISH_HEAL_PACKET);
            NetworkManager.registerS2CPayloadType(net.depression.network.ActionbarHintPacket.FEED_ANIMAL_HEAL_PACKET);
            NetworkManager.registerS2CPayloadType(net.depression.network.ActionbarHintPacket.PET_HEAL_PACKET);
            NetworkManager.registerS2CPayloadType(net.depression.network.ActionbarHintPacket.LOOT_HEAL_PACKET);
            NetworkManager.registerS2CPayloadType(net.depression.network.ActionbarHintPacket.PTSD_FROM_PACKET);
            NetworkManager.registerS2CPayloadType(net.depression.network.ActionbarHintPacket.PTSD_DISPERSE_PACKET);
            NetworkManager.registerS2CPayloadType(net.depression.network.ActionbarHintPacket.PTSD_REMISSION_PACKET);
            NetworkManager.registerS2CPayloadType(net.depression.network.ActionbarHintPacket.INSOMNIA_PACKET);
            NetworkManager.registerS2CPayloadType(net.depression.network.ActionbarHintPacket.MENTAL_FATIGUE_PACKET);
            NetworkManager.registerS2CPayloadType(net.depression.network.CloseEyePacket.CLOSE_EYE_PACKET);
            NetworkManager.registerS2CPayloadType(net.depression.network.PTSDOnsetPacket.PTSD_ONSET_PACKET);
            NetworkManager.registerS2CPayloadType(net.depression.network.DiaryUpdatePacket.DIARY_UPDATE_PACKET);
            NetworkManager.registerS2CPayloadType(net.depression.network.PTSDOnsetPacket.PHOTISM_PACKET);
            NetworkManager.registerS2CPayloadType(net.depression.network.MentalTraitPacket.MENTAL_TRAIT_PACKET);
            NetworkManager.registerS2CPayloadType(net.depression.network.RhythmCraftPacket.PROFILE_PACKET);
            NetworkManager.registerS2CPayloadType(net.depression.network.RhythmCraftPacket.PLAY_SONG_PACKET);
            NetworkManager.registerS2CPayloadType(net.depression.network.RhythmCraftPacket.ACCEPT_EDIT_PACKET);
            NetworkManager.registerS2CPayloadType(net.depression.network.RhythmCraftPacket.NOTE_CHANGE_PACKET);
            NetworkManager.registerS2CPayloadType(net.depression.network.RhythmCraftPacket.GAMEPLAY_CHANGE_PACKET);
            NetworkManager.registerS2CPayloadType(net.depression.network.RhythmCraftPacket.TIME_CHANGE_PACKET);
            NetworkManager.registerS2CPayloadType(net.depression.network.RhythmCraftPacket.GAME_END_PACKET);
            NetworkManager.registerS2CPayloadType(net.depression.network.RhythmCraftPacket.SPACE_CHANGE_PACKET);
        }
        ModEffects.register(); //必须先注册效果（因为物品使用了效果），否则物品注册时会报错
        ModBlocks.register();
        ModItems.register();
        ModCreativeTabs.register();
        ModSounds.register(); //必须先注册音效（因为心理医生使用了音效）
        VillageAdditions.register();

        NetworkManager.registerReceiver(NetworkManager.Side.C2S,
                net.depression.network.PacketBuffers.serverboundId(MentalTraitPacket.MENTAL_TRAIT_PACKET), Registry::receiveMentalTraitPacket);
        NetworkManager.registerReceiver(NetworkManager.Side.C2S,
                net.depression.network.PacketBuffers.serverboundId(DiaryUpdatePacket.DIARY_UPDATE_PACKET), Registry::receiveDiaryUpdatePacket);
        NetworkManager.registerReceiver(NetworkManager.Side.C2S,
                net.depression.network.PacketBuffers.serverboundId(PlaySoundPacket.PLAY_SOUND_PACKET), PTSDManager::receivePlaySoundPacket);
        NetworkManager.registerReceiver(NetworkManager.Side.C2S,
                net.depression.network.PacketBuffers.serverboundId(RhythmCraftPacket.PROFILE_REQUEST_PACKET), RhythmCraftProfile::onReceiveRequest);
        NetworkManager.registerReceiver(NetworkManager.Side.C2S,
                net.depression.network.PacketBuffers.serverboundId(RhythmCraftPacket.PROFILE_PACKET), RhythmCraftProfile::onReceiveProfileUpdate);
        NetworkManager.registerReceiver(NetworkManager.Side.C2S,
                net.depression.network.PacketBuffers.serverboundId(RhythmCraftPacket.READ_CHART_PACKET), PlayingChart::onReceiveReadChart);
        NetworkManager.registerReceiver(NetworkManager.Side.C2S,
                net.depression.network.PacketBuffers.serverboundId(RhythmCraftPacket.READY_PACKET), PlayingChart::onReceiveReady);
        NetworkManager.registerReceiver(NetworkManager.Side.C2S,
                net.depression.network.PacketBuffers.serverboundId(RhythmCraftPacket.LOAD_BACK_PACKET), PlayingChart::onReceiveLoadBack);
        NetworkManager.registerReceiver(NetworkManager.Side.C2S,
                net.depression.network.PacketBuffers.serverboundId(RhythmCraftPacket.PAUSE_CHANGE_PACKET), PlayingChart::onReceivePauseChange);
        NetworkManager.registerReceiver(NetworkManager.Side.C2S,
                net.depression.network.PacketBuffers.serverboundId(RhythmCraftPacket.PROGRESS_CHANGE_PACKET), PlayingChart::onReceiveProgressChange);

        CommandRegistrationEvent.EVENT.register(CommandRegistrationListener::registerCommands);
        PlayerEvent.SMELT_ITEM.register(PlayerEventListener::onSmeltItem);
        PlayerEvent.PLAYER_QUIT.register(PlayerEventListener::onPlayerQuit);
        PlayerEvent.ATTACK_ENTITY.register(PlayerEventListener::onAttackEntity);
        PlayerEvent.PLAYER_ADVANCEMENT.register(PlayerEventListener::onPlayerAdvancement);
        BlockEvent.PLACE.register(BlockEventListener::onBlockPlace);
        BlockEvent.BREAK.register(BlockEventListener::onBlockBreak);
        EntityEvent.LIVING_DEATH.register(EntityEventListener::onEntityDeath);
        EntityEvent.LIVING_CHECK_SPAWN.register(EntityEventListener::onEntityCheckSpawn);
        LifecycleEvent.SERVER_BEFORE_START.register(LifeCycleEventListener::onServerStart);
        TickEvent.SERVER_POST.register(TickEventListener::onServerTick);
        TickEvent.SERVER_LEVEL_POST.register(TickEventListener::onServerLevelTick);
        InteractionEvent.RIGHT_CLICK_BLOCK.register(InteractionEventListener::onRightClickBlock);
        ServerConfig.load();
        ResourceWriter.write();
        ResourceReader.read();
        Tools.init();
    }
}
