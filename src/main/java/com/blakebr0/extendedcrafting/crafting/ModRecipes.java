package com.blakebr0.extendedcrafting.crafting;

import com.blakebr0.extendedcrafting.config.ModConfig;
import com.blakebr0.extendedcrafting.crafting.endercrafter.EnderCrafterRecipeManager;
import com.blakebr0.extendedcrafting.crafting.table.TableRecipeManager;
import com.blakebr0.extendedcrafting.item.ItemMaterial;
import com.blakebr0.extendedcrafting.item.ModItems;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.crafting.CraftingHelper;

public class ModRecipes {

	public static void init() {
		TableRecipeManager.getInstance().addShaped(ItemMaterial.itemCrystaltineIngot,
				CraftingHelper.parseShaped(
						"DLLLLLD",
						"DNIGIND",
						"DNIGIND",
						"DLLLLLD",

						'D', "gemDiamond",
						'L', new ItemStack(Items.DYE, 1, 4),
						'N', Items.NETHER_STAR,  // 原来是 ItemMaterial.itemNetherStarNugget
						'I', "ingotIron",
						'G', "ingotGold"
				)
		);

		EnderCrafterRecipeManager.getInstance().addShaped(ItemMaterial.itemEnderStar, ModConfig.confEnderTimeRequired,
				CraftingHelper.parseShaped(
						" E ",
						"ENE",
						" E ",

						'E', Items.ENDER_EYE,
						'N', Items.NETHER_STAR
				)
		);

		EnderCrafterRecipeManager.getInstance().addShaped(new ItemStack(ModItems.itemMaterial, 4, 48), ModConfig.confEnderTimeRequired,
				CraftingHelper.parseShaped(
						" I ",
						"INI",
						" I ",

						'I', ItemMaterial.itemEnderIngot,
						'N', ItemMaterial.itemEnderStar
				)
		);

		ModItems.itemSingularity.initRecipes();
		ModItems.itemSingularityUltimate.initRecipe();
		ModItems.itemSingularityCustom.initRecipes();
	}
}
