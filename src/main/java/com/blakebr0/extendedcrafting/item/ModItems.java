package com.blakebr0.extendedcrafting.item;

import com.blakebr0.cucumber.registry.ModRegistry;
import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.config.ModConfig;
import com.blakebr0.extendedcrafting.lib.AddonReferenced;

@AddonReferenced
public class ModItems {

    public static final ItemMaterial itemMaterial = new ItemMaterial();
    public static final ItemSingularity itemSingularity = new ItemSingularity();
    public static final ItemSingularityUltimate itemSingularityUltimate = new ItemSingularityUltimate();
    public static final ItemSingularityCustom itemSingularityCustom = new ItemSingularityCustom();

    public static void init() {
        final ModRegistry registry = ExtendedCrafting.REGISTRY;
        registry.register(itemMaterial, "material");
        registry.register(itemSingularity, "singularity");
        registry.register(itemSingularityUltimate, "singularity_ultimate");
        registry.register(itemSingularityCustom, "singularity_custom");
    }
}
