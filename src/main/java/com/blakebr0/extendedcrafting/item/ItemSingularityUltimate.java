package com.blakebr0.extendedcrafting.item;

import com.blakebr0.cucumber.iface.IEnableable;
import com.blakebr0.cucumber.item.ItemBase;
import com.blakebr0.cucumber.lib.Colors;
import com.blakebr0.cucumber.util.Utils;
import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.config.ModConfig;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ItemSingularityUltimate extends ItemBase implements IEnableable {

    // 清空列表，不再收集任何奇点
    public static final ArrayList<ItemStack> singularities = new ArrayList<>();
    public static final ArrayList<Integer> blacklistDefaults = new ArrayList<>();
    public static final ArrayList<Integer> blacklistCustoms = new ArrayList<>();

    public ItemSingularityUltimate() {
        super("ec.singularity_ultimate");
        this.setCreativeTab(ExtendedCrafting.CREATIVE_TAB);
        this.setMaxStackSize(16);
    }

    public void configure(Configuration config) {
        // 保留配置读取，但不再使用
        ConfigCategory category = config.getCategory("singularity");
        String[] values = config.get(category.getName(), "ultimate_singularity_recipe_blacklist", new String[0]).getStringList();
        category.get("ultimate_singularity_recipe_blacklist").setComment("Blacklist Singularities from being in the Ultimate Singularity crafting recipe."
                + "\n- Syntax: singularityType;meta"
                + "\n- 'singularityType' can be 'default' or 'custom'."
                + "\n- 'default' for the ones added by the mod by default, 'custom' being the ones defined in '_custom_singularities'."
                + "\n- Example: custom;12");

        for (String value : values) {
            String[] parts = value.split(";");

            if (parts.length != 2) {
                ExtendedCrafting.LOGGER.error("Invalid ultimate singularity blacklist syntax length: " + value);
                continue;
            }

            String type = parts[0];
            int meta;

            if (!type.equals("default") && !type.equals("custom")) {
                ExtendedCrafting.LOGGER.error("Invalid ultimate singularity blacklist type: " + value);
                continue;
            }

            try {
                meta = Integer.parseInt(parts[1]);
            } catch (NumberFormatException e) {
                ExtendedCrafting.LOGGER.error("Invalid ultimate singularity blacklist meta: " + value);
                continue;
            }

            if (type.equals("default")) {
                blacklistDefaults.add(meta);
            } else {
                blacklistCustoms.add(meta);
            }
        }
    }

    @Override
    public boolean isEnabled() {
        return ModConfig.confSingularityEnabled;
    }

    @Override
    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.EPIC;
    }

    @Override
    public boolean hasEffect(ItemStack stack) {
        return true;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
        tooltip.add(Colors.ITALICS + Utils.localize("tooltip.ec.singularity_ultimate"));
    }
}
