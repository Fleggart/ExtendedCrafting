package com.blakebr0.extendedcrafting.client.tesr;

import com.blakebr0.extendedcrafting.tile.TileAutomationInterface;
import com.blakebr0.extendedcrafting.tile.TileCompressor;


import net.minecraftforge.fml.client.registry.ClientRegistry;

public class ModRenders {

    public static void init() {
        ClientRegistry.bindTileEntitySpecialRenderer(TileCompressor.class, new RenderCompressor());
        ClientRegistry.bindTileEntitySpecialRenderer(TileAutomationInterface.class, new RenderAutomationInterface());
    }
}
