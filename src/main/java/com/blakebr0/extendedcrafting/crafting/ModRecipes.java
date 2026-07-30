package com.blakebr0.extendedcrafting.crafting;

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
                        'N', Items.NETHER_STAR,
                        'I', "ingotIron",
                        'G', "ingotGold"
                )
        );

        ModItems.itemSingularity.initRecipes();
        ModItems.itemSingularityUltimate.initRecipe();
        ModItems.itemSingularityCustom.initRecipes();
    }
}
