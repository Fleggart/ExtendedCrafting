package com.blakebr0.extendedcrafting.config;

import com.blakebr0.extendedcrafting.Tags;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.File;

public class ModConfig {

    public static Configuration config;
    public static ModConfig instance;
    
    public static boolean confEnergyInWaila;

    public static boolean confInterfaceEnabled;
    public static int confInterfaceRFCapacity;
    public static int confInterfaceRFRate;
    public static boolean confInterfaceAcceptGTEU;
    public static boolean confInterfaceRenderer;
    
    public static boolean confTableEnabled;
    public static boolean confTableUseRecipes;
    
    public static boolean confCompressorEnabled;
    public static int confCompressorRFCapacity;
    public static int confCompressorRFRate;
    public static boolean confCompressorAcceptGTEU;
    public static boolean confCompressorRenderer;
    
    public static boolean confSingularityEnabled;
    public static int confSingularityAmount;
    public static int confSingularityRF;
    public static boolean confSingularityRecipes;
    // 已删除: public static boolean confUltimateSingularityRecipe;
    public static String confSingularityCatalyst;

    public static int confEUtoRF;

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals(Tags.MODID)) {
            ModConfig.init();
        }
    }

    public static void init(File file) {
        config = new Configuration(file);
        config.load();
        init();
    }

    public static void init() {
        updateConfig();
        
        String category;
        
        category = "general";
        config.setCategoryComment(category, "Settings for general things.");
    
        confEnergyInWaila = config.getBoolean("energy_in_waila", category, true, "Should WAILA show the current energy of Extended Crafting machines?");
        
        category = "automation_interface";
        config.setCategoryComment(category, "Settings for the Automation Interface.");
        confInterfaceEnabled = config.getBoolean("enabled", category, true, "Should the Automation Interface be enabled?");
        confInterfaceRFCapacity = config.getInt("energy_capacity", category, 1000000, 0, Integer.MAX_VALUE, "How much FE the Automation Interface should hold.");
        confInterfaceRFRate = config.getInt("energy_rate", category, 80, 0, 100000, "How much FE the Automation Interface should use when moving items.");
        confInterfaceAcceptGTEU = config.getBoolean("accept_gteu", category, false, "Should the Automation Interface accept GTEU?");
        confInterfaceRenderer = config.getBoolean("render_item", category, true, "Should the Automation Interface render the result item inside it?");
        
        category = "table_crafting";
        config.setCategoryComment(category, "Settings for the Extended Crafting Tables.");
        confTableEnabled = config.getBoolean("enabled", category, true, "Should the Extended Crafting Tables be enabled?");
        confTableUseRecipes = config.getBoolean("inherit_vanilla_recipes", category, true, "Should the Basic Crafting Table inherit normal crafting recipes?");
        
        category = "quantum_compression";
        config.setCategoryComment(category, "Settings for the Quantum Compressor.");
        confCompressorEnabled = config.getBoolean("enabled", category, true, "Should the Quantum Compressor be enabled?");
        confCompressorRFCapacity = config.getInt("energy_capacity", category, 10000000, 0, Integer.MAX_VALUE, "How much FE the Quantum Compressor should hold.");
        confCompressorRFRate = config.getInt("energy_rate", category, 5000, 0, Integer.MAX_VALUE, "How much FE/t the Quantum Compressor should use when crafting by default.");
        confCompressorAcceptGTEU = config.getBoolean("accept_gteu", category, false, "Should the Quantum Compressor accept GTEU?");
        confCompressorRenderer = config.getBoolean("render_item", category, true, "Should the Quantum Compressor render the result item above it?");
        
        category = "singularity";
        config.setCategoryComment(category, "Settings for the Singularities.");
        confSingularityEnabled = config.getBoolean("enabled", category, true, "Should the Singularities be enabled?");
        confSingularityAmount = config.getInt("material_amount", category, 10000, 1, Integer.MAX_VALUE, "The amount of materials required to create a Singularity, for the default recipes.");
        confSingularityRF = config.getInt("energy_cost", category, 5000000, 0, Integer.MAX_VALUE, "The amount of RF required to craft a Singularity, for the default recipes.");
        confSingularityCatalyst = config.getString("default_catalyst", category, "extendedcrafting:material:11", "The catalyst required for the default Singularity recipes. modid:itemid:metadata");
        confSingularityRecipes = config.getBoolean("default_recipes", category, true, "Should the default Singularity recipes be enabled?");
        // 已删除: confUltimateSingularityRecipe = config.getBoolean("ultimate_singularity_recipe", category, true, "...");
        // ============ 移除所有白名单/黑名单配置 ============
        // 已删除: config.get(category.getName(), "default_singularities", new String[0]);
        // 已删除: config.get(category.getName(), "ultimate_singularity_recipe_blacklist", new String[0]);
        // 已删除: config.get(category.getName(), "custom_singularity_blacklist", new String[0]);

        category = "gregtech";
        config.setCategoryComment(category, "Settings for GregTech compatibility.");
        confEUtoRF = config.getInt("conversion", category, 4, 1, Integer.MAX_VALUE, "How much RF should one GTEU be handled as?");

        if (config.hasChanged()) {
            config.save();
        }
    }
    
    private static void updateConfig() {
        if (config.hasCategory("settings")) {
    
            updateProperty("compressor_rf_capacity", "energy_capacity", "quantum_compression");
            updateProperty("compressor_rf_rate", "energy_rate", "quantum_compression");
            updateProperty("interface_rf_capacity", "energy_capacity", "automation_interface");
            updateProperty("interface_rf_rate", "energy_rate", "automation_interface");
            
            ConfigCategory settings = config.getCategory("settings");
            settings.remove("compressor_item_rate");
            config.removeCategory(settings);
            
            config.renameProperty("singularity", "_singularity_amount", "material_amount");
            config.renameProperty("singularity", "_singularity_rf", "energy_cost");
            config.renameProperty("singularity", "_singularity_catalyst", "default_catalyst");
            config.renameProperty("singularity", "_singularity_recipes", "default_recipes");
            // 已删除: config.renameProperty("singularity", "_ultimate_singularity_recipe", "ultimate_singularity_recipe");
            config.renameProperty("singularity", "_custom_singularities", "custom_singularities");
            // 已删除: config.renameProperty("singularity", "_ultimate_blacklist", "ultimate_singularity_recipe_blacklist");
        }
    }
    
    private static void updateProperty(String oldName, String newName, String newCategory) {
        config.moveProperty("settings", oldName, newCategory);
        config.renameProperty(newCategory, oldName, newName);
    }
    
    // ============ 删除 removeSingularity 方法 ============
    // 不再需要从配置中读取白名单
    // 已删除: public static boolean removeSingularity(String name) { ... }
}
