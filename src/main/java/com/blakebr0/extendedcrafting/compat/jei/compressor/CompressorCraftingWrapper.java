package com.blakebr0.extendedcrafting.compat.jei.compressor;

import com.blakebr0.cucumber.util.Utils;
import com.blakebr0.extendedcrafting.crafting.CompressorRecipe;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

public class CompressorCraftingWrapper implements IRecipeWrapper {

	protected final CompressorRecipe recipe;

	public CompressorCraftingWrapper(CompressorRecipe recipe) {
		this.recipe = recipe;
	}

	@Nonnull
	@Override
	public List<String> getTooltipStrings(int mouseX, int mouseY) {
		// ============ 修改: 只显示总能量消耗，移除 FE/t ============
		if (mouseX > 1 && mouseX < 14 && mouseY > 1 && mouseY < 78) {
			return Collections.singletonList(Utils.format(this.recipe.getPowerCost()) + " FE");
		}
		if (mouseX > 54 && mouseX < 78 && mouseY > 58 && mouseY < 68) {
			return Collections.singletonList(Utils.localize("tooltip.ec.num_items", Utils.format(this.recipe.getInputCount())));
		}
		return Collections.emptyList();
	}

	@Override
	public void getIngredients(IIngredients ingredients) {
		ingredients.setInput(VanillaTypes.ITEM, recipe.getInput().getMatchingStacks()[0]);
		ingredients.setOutput(VanillaTypes.ITEM, this.recipe.getOutput());
	}
}
