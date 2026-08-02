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
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.text.WordUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ItemSingularityCustom extends ItemMeta implements IModelHelper, IEnableable {

    public static final ArrayList<CustomSingularity> singularities = new ArrayList<>();
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

    // ============ 修复: 正确解析配置并添加到 singularities 列表 ============
    public void configure(Configuration config) {
        ConfigCategory category = config.getCategory("singularity");
        
        // 获取 custom_singularities 配置
        String[] values = config.get(category.getName(), "custom_singularities", new String[0]).getStringList();
        
        // 设置配置注释
        category.get("custom_singularities").setComment("Here you can add your own custom Singularities."
                + "\n- Syntax: meta;name;material;color"
                + "\n- Example: 12;super_potato;minecraft:carrot;123456"
                + "\n- 'meta' must be different for each, and should not be changed."
                + "\n- 'name' should be lower case with underscores for spaces. Singularity is added automatically."
                + "\n- Example: 'lots_of_spaghetti' would show 'Lots Of Spaghetti Singularity'."
                + "\n- 'material' is an item id or ore dictionary entry. This is for the generic crafting recipe."
                + "\n- Note: if you plan on adding your own recipe with the CraftTweaker integration, put 'none'."
                + "\n- Examples: 'minecraft:stone' for stone, 'ore:ingotIron' for the ore dictionary entry 'ingotIron'."
                + "\n- Note: you can also specify meta for item ids, by adding them to the end of the item id."
                + "\n- Example: minecraft:stone:3 for a meta of 3. Make the meta 32767 for wildcard value."
                + "\n- 'color' the color of the singularity as a hex value. http://htmlcolorcodes.com/"
                + "\n- Example: 123456 would color it as whatever that color is."
                + "\n - Use the localization key \"item.ec.singularity.<name>\" to set the name of your custom Singularity."
                + "\n - Example: item.ec.singularity.carrot=Carrot in your resources/extendedcrafting/lang/en_us.lang"
                + "\n - and item.ec.singularity.carrot=морковь in your resources/extendedcrafting/lang/ru_ru.lang"
                + "\n - Note however that you will need a way to load these resources, such as the mod ResourceLoader.");

        // 清空旧列表，避免重复
        singularities.clear();

        // ============ 关键: 解析配置并添加到 singularities 列表 ============
        for (String value : values) {
            String[] parts = value.split(";");

            if (parts.length != 4) {
                ExtendedCrafting.LOGGER.error("Invalid custom singularity syntax length: " + value);
                continue;
            }

            int meta;
            String name = parts[1].trim();
            String material = parts[2].trim();
            int color;

            try {
                meta = Integer.parseInt(parts[0].trim());
                color = Integer.parseInt(parts[3].trim(), 16);
            } catch (NumberFormatException e) {
                ExtendedCrafting.LOGGER.error("Invalid custom singularity syntax ints: " + value);
                continue;
            }

            ExtendedCrafting.LOGGER.info("Adding custom singularity: meta=" + meta + ", name=" + name + ", material=" + material + ", color=" + Integer.toHexString(color));
            singularities.add(new CustomSingularity(meta, name, material, color));
        }
    }

    // ============ 修复: 从 singularities 列表读取并注册 ============
    @Override
    public void init() {
        ExtendedCrafting.LOGGER.info("Loading " + singularities.size() + " custom singularities...");
        
        for (CustomSingularity sing : singularities) {
            addSingularity(sing.meta, sing.name, sing.material, sing.color);
        }
        
        ExtendedCrafting.LOGGER.info("Loaded " + items.size() + " custom singularity items.");
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

    // ============ 修复: 正确注册奇点到物品系统 ============
    public void addSingularity(int meta, String name, String material, int color) {
        // 保存颜色和材料
        singularityColors.put(meta, color);
        singularityMaterials.put(meta, material);
        
        // ============ 关键: 调用 addItem 注册到物品系统 ============
        addItem(meta, name, true);
        
        ExtendedCrafting.LOGGER.info("Registered custom singularity: " + name + " (meta=" + meta + ")");
    }

    // ============ 修复: 生成压缩机配方 ============
    public void initRecipes() {
        if (!this.isEnabled()) {
            ExtendedCrafting.LOGGER.info("Custom singularities disabled, skipping recipes.");
            return;
        }

        ExtendedCrafting.LOGGER.info("Generating recipes for " + singularityMaterials.size() + " custom singularities...");

        for (Map.Entry<Integer, Object> obj : singularityMaterials.entrySet()) {
            Object value = obj.getValue();
            int meta = obj.getKey();
            
            if (value instanceof String) {
                String materialStr = (String) value;
                
                // 跳过 "none" (用户想手动添加配方)
                if ("none".equalsIgnoreCase(materialStr)) {
                    ExtendedCrafting.LOGGER.info("Skipping recipe for custom singularity meta=" + meta + " (material=none)");
                    continue;
                }
                
                // 检查是否是矿物词典
                if (materialStr.startsWith("ore:")) {
                    String oreName = materialStr.substring(4);
                    if (OreDictionary.doesOreNameExist(oreName)) {
                        if (!OreDictionary.getOres(oreName).isEmpty()) {
                            CompressorRecipeManager.getInstance().addRecipe(
                                new ItemStack(this, 1, meta),
                                CraftingHelper.getIngredient(oreName),
                                ModConfig.confSingularityAmount,
                                ModConfig.confSingularityRF
                            );
                            ExtendedCrafting.LOGGER.info("Added ore recipe for custom singularity meta=" + meta + " (ore:" + oreName + ")");
                        } else {
                            ExtendedCrafting.LOGGER.warn("No ores found for ore:" + oreName);
                        }
                    } else {
                        ExtendedCrafting.LOGGER.warn("Ore name does not exist: " + oreName);
                    }
                } else {
                    // 物品 ID 格式: modid:item 或 modid:item:meta
                    String[] parts = materialStr.split(":");
                    Item item;
                    ItemStack stack;
                    
                    try {
                        if (parts.length == 3) {
                            // 带元数据: modid:item:meta
                            int matMeta = Integer.parseInt(parts[2]);
                            item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(parts[0], parts[1]));
                            if (item != null) {
                                stack = new ItemStack(item, 1, matMeta);
                                CompressorRecipeManager.getInstance().addRecipe(
                                    new ItemStack(this, 1, meta),
                                    Ingredient.fromStacks(stack),
                                    ModConfig.confSingularityAmount,
                                    ModConfig.confSingularityRF
                                );
                                ExtendedCrafting.LOGGER.info("Added item recipe for custom singularity meta=" + meta + " (" + materialStr + ")");
                            } else {
                                ExtendedCrafting.LOGGER.warn("Item not found: " + materialStr);
                            }
                        } else if (parts.length == 2) {
                            // 不带元数据: modid:item
                            item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(parts[0], parts[1]));
                            if (item != null) {
                                stack = new ItemStack(item);
                                CompressorRecipeManager.getInstance().addRecipe(
                                    new ItemStack(this, 1, meta),
                                    Ingredient.fromStacks(stack),
                                    ModConfig.confSingularityAmount,
                                    ModConfig.confSingularityRF
                                );
                                ExtendedCrafting.LOGGER.info("Added item recipe for custom singularity meta=" + meta + " (" + materialStr + ")");
                            } else {
                                ExtendedCrafting.LOGGER.warn("Item not found: " + materialStr);
                            }
                        } else {
                            ExtendedCrafting.LOGGER.warn("Invalid material format: " + materialStr);
                        }
                    } catch (NumberFormatException e) {
                        ExtendedCrafting.LOGGER.warn("Invalid meta in material: " + materialStr);
                    }
                }
            } else {
                ExtendedCrafting.LOGGER.warn("Invalid material type for custom singularity meta=" + meta);
            }
        }
    }

    public static class CustomSingularity {
        public final int meta;
        public final String name;
        public final String material;
        public final int color;

        public CustomSingularity(int meta, String name, String material, int color) {
            this.meta = meta;
            this.name = name;
            this.material = material;
            this.color = color;
        }
    }

    public static class ColorHandler implements IItemColor {
        @Override
        public int colorMultiplier(ItemStack stack, int tintIndex) {
            return singularityColors.getOrDefault(stack.getMetadata(), 0xFFFFFF);
        }
    }
}
