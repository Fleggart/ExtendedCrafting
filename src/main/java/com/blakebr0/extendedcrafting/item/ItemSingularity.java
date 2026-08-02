package com.blakebr0.extendedcrafting.item;

import com.blakebr0.cucumber.helper.ResourceHelper;
import com.blakebr0.cucumber.iface.IEnableable;
import com.blakebr0.cucumber.item.ItemMeta;
import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.Tags;
import com.blakebr0.extendedcrafting.config.ModConfig;
import com.blakebr0.extendedcrafting.crafting.CompressorRecipeManager;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.init.Items;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.crafting.CraftingHelper;

import java.util.HashMap;
import java.util.Map;

public class ItemSingularity extends ItemMeta implements IEnableable {

    public static final Map<Integer, Integer> singularityColors = new HashMap<>();
    public static final Map<Integer, Object> singularityMaterials = new HashMap<>();
    private final Configuration config = ModConfig.config;

    public ItemSingularity() {
        super("ec.singularity", ExtendedCrafting.REGISTRY);
        this.setCreativeTab(ExtendedCrafting.CREATIVE_TAB);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        String localizedMaterialName = "Invalid";
        if (items.containsKey(stack.getMetadata())) {
            String materialName = items.get(stack.getMetadata()).getName();
            localizedMaterialName = I18n.translateToLocal("item.ec.singularity." + materialName);
        }
        return I18n.translateToLocalFormatted("item.ec.singularity.name", localizedMaterialName);
    }

    @Override
    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.UNCOMMON;
    }

    @Override
    public void init() {
        // ============ 移除白名单检查，直接添加所有默认奇点 ============
        
        addSingularity(0, "iron", "ingotIron", 0x969696);
        addSingularity(5, "gold", "ingotGold", 0xDEDE00);
        addSingularity(16, "aluminum", "ingotAluminum", 0xCACCDA);
        addSingularity(17, "copper", "ingotCopper", 0xCE7201);
        addSingularity(18, "tin", "ingotTin", 0x7690A5);
        addSingularity(19, "bronze", "ingotBronze", 0xA87544);
        addSingularity(20, "zinc", "ingotZinc", 0xCFD2CC);
        addSingularity(21, "brass", "ingotBrass", 0xBC8B22);
        addSingularity(22, "silver", "ingotSilver", 0x83AAB2);
        addSingularity(23, "lead", "ingotLead", 0x484F67);
        addSingularity(24, "steel", "ingotSteel", 0x565656);
        addSingularity(25, "nickel", "ingotNickel", 0xBEB482);
        addSingularity(26, "constantan", "ingotConstantan", 0xA98544);
        addSingularity(27, "electrum", "ingotElectrum", 0xA79135);
        addSingularity(28, "invar", "ingotInvar", 0x929D97);
        addSingularity(29, "mithril", "ingotMithril", 0x659ABB);
        addSingularity(30, "tungsten", "ingotTungsten", 0x494E51);
        addSingularity(31, "titanium", "ingotTitanium", 0xA6A7B8);
        addSingularity(32, "uranium", "ingotUranium", 0x46800D);
        addSingularity(33, "chrome", "ingotChrome", 0xC1A9AE);
        addSingularity(34, "platinum", "ingotPlatinum", 0x6FEAEF);
        addSingularity(35, "iridium", "ingotIridium", 0x949FBE);
        addSingularity(65, "cobalt", "ingotCobalt", 0x023C9B);
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

    // ============ 简化 addSingularity 方法 ============
    // 移除白名单检查，直接添加所有奇点
    
    public void addSingularity(int meta, String name, ItemStack material, int color) {
        singularityColors.put(meta, color);
        singularityMaterials.put(meta, material);
        // 已删除: ItemSingularityUltimate.addSingularityToRecipe(new ItemStack(this, 1, meta));
        // 已删除: 白名单/黑名单检查
        addItem(meta, name, true);
    }

    public void addSingularity(int meta, String name, String oreName, int color) {
        singularityColors.put(meta, color);
        singularityMaterials.put(meta, oreName);
        // 已删除: ItemSingularityUltimate.addSingularityToRecipe(new ItemStack(this, 1, meta));
        // 已删除: 白名单/黑名单检查
        addItem(meta, name, true);
    }

    // ============ 移除 checkConfig 和 addToConfig 方法 ============
    // 不再需要检查配置来启用/禁用奇点
    // 已删除: public boolean checkConfig(String name) { ... }
    // 已删除: private void addToConfig(String name) { ... }

    public void initRecipes() {
        if (!ModConfig.confSingularityRecipes || !this.isEnabled())
            return;

        for (Map.Entry<Integer, Object> obj : singularityMaterials.entrySet()) {
            Object value = obj.getValue();
            int meta = obj.getKey();
            
            if (value instanceof ItemStack) {
                ItemStack stack = (ItemStack) value;
                if (!stack.isEmpty()) {
                    CompressorRecipeManager.getInstance().addRecipe(
                        new ItemStack(this, 1, meta), 
                        CraftingHelper.getIngredient(value), 
                        ModConfig.confSingularityAmount, 
                        ModConfig.confSingularityRF
                    );
                }
            } else if (value instanceof String) {
                CompressorRecipeManager.getInstance().addRecipe(
                    new ItemStack(this, 1, meta), 
                    CraftingHelper.getIngredient(value), 
                    ModConfig.confSingularityAmount, 
                    ModConfig.confSingularityRF
                );
            } else {
                ExtendedCrafting.LOGGER.error("Invalid material for singularity: " + value.toString());
            }
        }
    }

    public static class ColorHandler implements IItemColor {
        @Override
        public int colorMultiplier(ItemStack stack, int tintIndex) {
            return singularityColors.get(stack.getMetadata());
        }
    }
}
