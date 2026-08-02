package com.blakebr0.extendedcrafting.crafting;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

public class CompressorRecipe {

	protected final ItemStack output;
	protected final Ingredient input;
	protected final int inputCount;
	protected final int powerCost;
	
	public CompressorRecipe(ItemStack output, Ingredient input, int inputCount, int powerCost) {
		this.output = output;
		this.input = input;
		this.inputCount = inputCount;
		this.powerCost = powerCost;
	}

	public ItemStack getOutput() {
		return this.output.copy();
	}

	public Ingredient getInput() {
		return this.input;
	}

	public int getInputCount() {
		return this.inputCount;
	}

	public int getPowerCost() {
		return this.powerCost;
	}
	
}
