package com.blakebr0.extendedcrafting.item;

import com.blakebr0.cucumber.item.ItemMeta;
import com.blakebr0.cucumber.lib.Colors;
import com.blakebr0.cucumber.util.Utils;
import com.blakebr0.extendedcrafting.ExtendedCrafting;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ItemMaterial extends ItemMeta {
	
	// 移除了所有Nugget的元数据
	public static final Set<Integer> ultimateMetas = new HashSet<>(Arrays.asList(13, 19, 32, 40, 48));

	// ===== 基础材料 =====
	public static ItemStack itemBlackIron;
	public static ItemStack itemBlackIronSlate;
	public static ItemStack itemBlackIronRod;

	public static ItemStack itemLuminessence;

	// ===== 催化剂 =====
	public static ItemStack itemBasicCatalyst;
	public static ItemStack itemAdvancedCatalyst;
	public static ItemStack itemEliteCatalyst;
	public static ItemStack itemUltimateCatalyst;
	public static ItemStack itemCrystaltineCatalyst;
	public static ItemStack itemTheUltimateCatalyst;

	// ===== 组件 =====
	public static ItemStack itemBasicComponent;
	public static ItemStack itemAdvancedComponent;
	public static ItemStack itemEliteComponent;
	public static ItemStack itemUltimateComponent;
	public static ItemStack itemCrystaltineComponent;
	public static ItemStack itemTheUltimateComponent;

	// ===== 锭 =====
	public static ItemStack itemCrystaltineIngot;
	public static ItemStack itemTheUltimateIngot;
	public static ItemStack itemEnderIngot;
	public static ItemStack itemEnhancedEnderIngot;

	// ===== 星 =====
	public static ItemStack itemEnderStar;

	public ItemMaterial() {
		super("ec.material", ExtendedCrafting.REGISTRY);
		this.setCreativeTab(ExtendedCrafting.CREATIVE_TAB);
	}

	@Override
	public void init() {
		// 黑铁系列
		itemBlackIron = addItem(0, "black_iron", "ingotBlackIron");
		itemBlackIronSlate = addItem(2, "black_iron_slate");
		itemBlackIronRod = addItem(3, "black_iron_rod");

		itemLuminessence = addItem(7, "luminessence");

		// 催化剂
		itemBasicCatalyst = addItem(8, "basic_catalyst");
		itemAdvancedCatalyst = addItem(9, "advanced_catalyst");
		itemEliteCatalyst = addItem(10, "elite_catalyst");
		itemUltimateCatalyst = addItem(11, "ultimate_catalyst");
		itemCrystaltineCatalyst = addItem(12, "crystaltine_catalyst");
		itemTheUltimateCatalyst = addItem(13, "ultimater_catalyst");

		// 组件
		itemBasicComponent = addItem(14, "basic_component");
		itemAdvancedComponent = addItem(15, "advanced_component");
		itemEliteComponent = addItem(16, "elite_component");
		itemUltimateComponent = addItem(17, "ultimate_component");
		itemCrystaltineComponent = addItem(18, "crystaltine_component");
		itemTheUltimateComponent = addItem(19, "ultimater_component");

		// 锭（移除了所有Nugget）
		itemCrystaltineIngot = addItem(24, "crystaltine_ingot", "ingotCrystaltine");
		itemTheUltimateIngot = addItem(32, "ultimate_ingot", "ingotUltimate");
		itemEnderIngot = addItem(36, "ender_ingot");
		itemEnhancedEnderIngot = addItem(48, "enhanced_ender_ingot");

		// 星
		itemEnderStar = addItem(40, "ender_star");
	}
	
	@Override
	public boolean hasEffect(ItemStack stack) {
		return ultimateMetas.contains(stack.getMetadata());
	}

	@Nonnull
	@Override
	public EnumRarity getRarity(ItemStack stack) {
		return ultimateMetas.contains(stack.getMetadata()) ? EnumRarity.EPIC : EnumRarity.COMMON;
	}
	
	@Override
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
		switch (stack.getMetadata()) {
		case 13:
			tooltip.add(Colors.ITALICS + Utils.localize("tooltip.ec.ultimate_catalyst"));
			break;
		case 19:
			tooltip.add(Colors.ITALICS + Utils.localize("tooltip.ec.ultimate_component"));
			break;
		case 32:
			tooltip.add(Colors.ITALICS + Utils.localize("tooltip.ec.ultimate_ingot"));
			break;
		default:
			break;
		}
	}
}
