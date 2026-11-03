package net.depression.neoforge;

import net.depression.Depression;
import net.depression.neoforge.world.NeoForgeVillageAddition;
import net.neoforged.fml.common.Mod;

@Mod(Depression.MOD_ID)
public final class DepressionNeoForge {
    public DepressionNeoForge() {
        Depression.init();
        NeoForgeVillageAddition.register();
    }
}
