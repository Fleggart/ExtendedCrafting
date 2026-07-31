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
	
	public static final Set<Integer> ultimateMetas = new HashSet<>(Arrays.asList(13, 19));

	public static ItemStack itemBlackIron;
	public static ItemStack itemBlackIronSlate;

	public static ItemStack itemBasicCatalyst;
	public static ItemStack itemAdvancedCatalyst;
	public static ItemStack itemEliteCatalyst;
	public static ItemStack itemUltimateCatalyst;

	public static ItemStack itemBasicComponent;
	public static ItemStack itemAdvancedComponent;
	public static ItemStack itemEliteComponent;
	public static ItemStack itemUltimateComponent;

	public static ItemStack itemCrystaltineIngot;

	public ItemMaterial() {
		super("ec.material", ExtendedCrafting.REGISTRY);
		this.setCreativeTab(ExtendedCrafting.CREATIVE_TAB);
	}

	@Override
	public void init() {
		itemBlackIron = addItem(0, "black_iron", "ingotBlackIron");
		itemBlackIronSlate = addItem(2, "black_iron_slate");
		itemBasicCatalyst = addItem(8, "basic_catalyst");
		itemAdvancedCatalyst = addItem(9, "advanced_catalyst");
		itemEliteCatalyst = addItem(10, "elite_catalyst");
		itemUltimateCatalyst = addItem(11, "ultimate_catalyst");
		itemBasicComponent = addItem(14, "basic_component");
		itemAdvancedComponent = addItem(15, "advanced_component");
		itemEliteComponent = addItem(16, "elite_component");
		itemUltimateComponent = addItem(17, "ultimate_component");
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
		default:
			break;
		}
	}
}
