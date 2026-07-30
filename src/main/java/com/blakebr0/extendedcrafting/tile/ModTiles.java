package com.blakebr0.extendedcrafting.tile;

import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.Tags;
import com.blakebr0.extendedcrafting.config.ModConfig;

import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModTiles {

    @SuppressWarnings("deprecation")
    public static void init() {

        if (ModConfig.confInterfaceEnabled) {
            GameRegistry.registerTileEntity(TileAutomationInterface.class, "EC_Automation_Interface");
        }

        if (ModConfig.confTableEnabled) {
            GameRegistry.registerTileEntity(TileBasicCraftingTable.class, "EC_Basic_Table");
            GameRegistry.registerTileEntity(TileAdvancedCraftingTable.class, "EC_Advanced_Table");
            GameRegistry.registerTileEntity(TileEliteCraftingTable.class, "EC_Elite_Table");
            GameRegistry.registerTileEntity(TileUltimateCraftingTable.class, "EC_Ultimate_Table");
        }

        if (ModConfig.confCompressorEnabled) {
            GameRegistry.registerTileEntity(TileCompressor.class, "EC_Compressor");
        }
    }
}
