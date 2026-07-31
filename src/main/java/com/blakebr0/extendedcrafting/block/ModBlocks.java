package com.blakebr0.extendedcrafting.block;

import com.blakebr0.cucumber.registry.ModRegistry;
import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.block.craftingtable.BlockAdvancedTable;
import com.blakebr0.extendedcrafting.block.craftingtable.BlockBasicTable;
import com.blakebr0.extendedcrafting.block.craftingtable.BlockEliteTable;
import com.blakebr0.extendedcrafting.block.craftingtable.BlockUltimateTable;
import com.blakebr0.extendedcrafting.lib.AddonReferenced;

@AddonReferenced
public class ModBlocks {
    public static final BlockAutomationInterface blockAutomationInterface = new BlockAutomationInterface();
    @AddonReferenced public static final BlockBasicTable blockBasicTable = new BlockBasicTable();
    @AddonReferenced public static final BlockAdvancedTable blockAdvancedTable = new BlockAdvancedTable();
    @AddonReferenced public static final BlockEliteTable blockEliteTable = new BlockEliteTable();
    @AddonReferenced public static final BlockUltimateTable blockUltimateTable = new BlockUltimateTable();

    public static final BlockCompressor blockCompressor = new BlockCompressor();
    public static void init() {
        final ModRegistry registry = ExtendedCrafting.REGISTRY;
        registry.register(blockAutomationInterface, "interface");
        registry.register(blockBasicTable, "table_basic");
        registry.register(blockAdvancedTable, "table_advanced");
        registry.register(blockEliteTable, "table_elite");
        registry.register(blockUltimateTable, "table_ultimate");
        registry.register(blockCompressor, "compressor");
    }
}
