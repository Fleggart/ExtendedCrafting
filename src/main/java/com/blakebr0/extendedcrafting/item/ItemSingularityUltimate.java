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
