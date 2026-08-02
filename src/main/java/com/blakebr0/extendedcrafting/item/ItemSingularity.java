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
import net.minecraftforge.common.crafting.CraftingHelper;

import java.util.HashMap;
import java.util.Map;

public class ItemSingularity extends ItemMeta implements IEnableable {

    // ============ 固定能量消耗 ============
    private static final int FIXED_ENERGY_COST = 1000;

    public static final Map<Integer, Integer> singularityColors = new HashMap<>();
    public static final Map<Integer, Object> singularityMaterials = new HashMap<>();

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
        addSingularity(0, "coal", new ItemStack(Items.COAL), 0x1B1B1B);
        addSingularity(1, "iron", "ingotIron", 0x969696);
        addSingularity(2, "lapis_lazuli", new ItemStack(Items.DYE, 1, 4), 0x345EC3);
        addSingularity(3, "redstone", new ItemStack(Items.REDSTONE), 0x720000);
        addSingularity(4, "glowstone", new ItemStack(Items.GLOWSTONE_DUST), 0x868600);
        addSingularity(5, "gold", "ingotGold", 0xDEDE00);
        addSingularity(6, "diamond", "gemDiamond", 0x2CCDB1);
        addSingularity(7, "emerald", "gemEmerald", 0x00A835);
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
        addSingularity(48, "signalum", "ingotSignalum", 0xDD3B00);
        addSingularity(49, "lumium", "ingotLumium", 0xDEE59C);
        addSingularity(50, "enderium", "ingotEnderium", 0x0B4D4E);
        addSingularity(64, "ardite", "ingotArdite", 0xDA4817);
        addSingularity(65, "cobalt", "ingotCobalt", 0x023C9B);
        addSingularity(66, "manyullyn", "ingotManyullyn", 0x5C268A);
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

    public void addSingularity(int meta, String name, ItemStack material, int color) {
        singularityColors.put(meta, color);
        singularityMaterials.put(meta, material);
        addItem(meta, name, true);
    }

    public void addSingularity(int meta, String name, String oreName, int color) {
        singularityColors.put(meta, color);
        singularityMaterials.put(meta, oreName);
        addItem(meta, name, true);
    }

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
                        FIXED_ENERGY_COST  // ============ 使用固定值 1000 ============
                    );
                }
            } else if (value instanceof String) {
                CompressorRecipeManager.getInstance().addRecipe(
                    new ItemStack(this, 1, meta), 
                    CraftingHelper.getIngredient(value), 
                    ModConfig.confSingularityAmount, 
                    FIXED_ENERGY_COST  // ============ 使用固定值 1000 ============
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
