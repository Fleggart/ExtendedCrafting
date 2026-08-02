package com.blakebr0.extendedcrafting.item;

import com.blakebr0.cucumber.helper.ResourceHelper;
import com.blakebr0.cucumber.iface.IEnableable;
import com.blakebr0.cucumber.iface.IModelHelper;
import com.blakebr0.cucumber.item.ItemMeta;
import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.Tags;
import com.blakebr0.extendedcrafting.config.ModConfig;
import com.blakebr0.extendedcrafting.crafting.CompressorRecipeManager;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.text.WordUtils;

import java.util.HashMap;
import java.util.Map;

public class ItemSingularityCustom extends ItemMeta implements IModelHelper, IEnableable {

    // ============ 固定能量消耗 ============
    private static final int FIXED_ENERGY_COST = 1000;

    public static final Map<Integer, Integer> singularityColors = new HashMap<>();
    public static final Map<Integer, Object> singularityMaterials = new HashMap<>();

    public ItemSingularityCustom() {
        super("ec.singularity_custom", ExtendedCrafting.REGISTRY);
        this.setCreativeTab(ExtendedCrafting.CREATIVE_TAB);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        String localizedMaterialName = "Invalid";
        int meta = stack.getMetadata();
        if (items.containsKey(meta)) {
            String materialName = items.get(meta).getName();
            String assumedTranslationKey = "item.ec.singularity." + materialName;

            if(I18n.canTranslate(assumedTranslationKey))
                localizedMaterialName = I18n.translateToLocal(assumedTranslationKey);
            else
                localizedMaterialName = WordUtils.capitalize(materialName.replaceAll("_"," "));
        }
        return I18n.translateToLocalFormatted("item.ec.singularity.name", localizedMaterialName);
    }

    @Override
    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.UNCOMMON;
    }

    public void configure(Configuration config) {
        // 不需要再解析，直接从 ModConfig 读取
    }

    @Override
    public void init() {
        String[] configEntries = ModConfig.confCustomSingularities;
        
        if (configEntries == null || configEntries.length == 0) {
            ExtendedCrafting.LOGGER.info("No custom singularities configured.");
            return;
        }

        ExtendedCrafting.LOGGER.info("Loading " + configEntries.length + " custom singularities from config...");

        for (String entry : configEntries) {
            String[] parts = entry.split(";");

            if (parts.length != 4) {
                ExtendedCrafting.LOGGER.error("Invalid custom singularity syntax: " + entry);
                continue;
            }

            try {
                int meta = Integer.parseInt(parts[0].trim());
                String name = parts[1].trim();
                String material = parts[2].trim();
                int color = Integer.parseInt(parts[3].trim(), 16);

                addSingularity(meta, name, material, color);
                ExtendedCrafting.LOGGER.info("Loaded custom singularity: " + name + " (meta=" + meta + ")");
            } catch (NumberFormatException e) {
                ExtendedCrafting.LOGGER.error("Invalid number in custom singularity: " + entry);
            }
        }
    }

    @Override
    public void initModels() {
        for (Map.Entry<Integer, MetaItem> item : items.entrySet()) {
            ModelLoader.setCustomModelResourceLocation(this, item.getKey(), 
                ResourceHelper.getModelResource(Tags.MODID, "singularity", "inventory"));
        }
    }

    @Override
    public boolean isEnabled() {
        return ModConfig.confSingularityEnabled;
    }

    public void addSingularity(int meta, String name, String material, int color) {
        singularityColors.put(meta, color);
        singularityMaterials.put(meta, material);
        addItem(meta, name, true);
    }

    public void initRecipes() {
        if (!this.isEnabled()) {
            return;
        }

        ExtendedCrafting.LOGGER.info("Generating recipes for " + singularityMaterials.size() + " custom singularities...");

        for (Map.Entry<Integer, Object> obj : singularityMaterials.entrySet()) {
            Object value = obj.getValue();
            int meta = obj.getKey();
            
            if (!(value instanceof String)) {
                continue;
            }
            
            String materialStr = (String) value;
            
            if ("none".equalsIgnoreCase(materialStr)) {
                ExtendedCrafting.LOGGER.info("Skipping recipe for custom singularity meta=" + meta + " (material=none)");
                continue;
            }
            
            // 矿物词典: ore:ingotIron
            if (materialStr.startsWith("ore:")) {
                String oreName = materialStr.substring(4);
                if (OreDictionary.doesOreNameExist(oreName)) {
                    if (!OreDictionary.getOres(oreName).isEmpty()) {
                        // ============ 修改: 使用固定值 1000 ============
                        CompressorRecipeManager.getInstance().addRecipe(
                            new ItemStack(this, 1, meta),
                            CraftingHelper.getIngredient(oreName),
                            ModConfig.confSingularityAmount,
                            FIXED_ENERGY_COST
                        );
                        ExtendedCrafting.LOGGER.info("Added ore recipe for custom singularity meta=" + meta);
                    }
                }
                continue;
            }
            
            // 物品 ID: minecraft:potato 或 minecraft:stone:3
            String[] parts = materialStr.split(":");
            if (parts.length >= 2) {
                Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(parts[0], parts[1]));
                if (item != null) {
                    int itemMeta = parts.length == 3 ? Integer.parseInt(parts[2]) : 0;
                    ItemStack stack = new ItemStack(item, 1, itemMeta);
                    // ============ 修改: 使用固定值 1000 ============
                    CompressorRecipeManager.getInstance().addRecipe(
                        new ItemStack(this, 1, meta),
                        Ingredient.fromStacks(stack),
                        ModConfig.confSingularityAmount,
                        FIXED_ENERGY_COST
                    );
                    ExtendedCrafting.LOGGER.info("Added item recipe for custom singularity meta=" + meta);
                } else {
                    ExtendedCrafting.LOGGER.warn("Item not found: " + materialStr);
                }
            }
        }
    }

    public static class ColorHandler implements IItemColor {
        @Override
        public int colorMultiplier(ItemStack stack, int tintIndex) {
            return singularityColors.getOrDefault(stack.getMetadata(), 0xFFFFFF);
        }
    }
}
