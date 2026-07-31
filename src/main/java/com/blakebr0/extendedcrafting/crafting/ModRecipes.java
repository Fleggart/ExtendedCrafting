package com.blakebr0.extendedcrafting.crafting;

import com.blakebr0.extendedcrafting.crafting.table.TableRecipeManager;
import com.blakebr0.extendedcrafting.item.ModItems;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.crafting.CraftingHelper;

public class ModRecipes {
    public static void init() {
        ModItems.itemSingularity.initRecipes();
        ModItems.itemSingularityUltimate.initRecipe();
        ModItems.itemSingularityCustom.initRecipes();
    }
}
