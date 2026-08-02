package com.blakebr0.extendedcrafting.client.gui;

import com.blakebr0.cucumber.helper.ResourceHelper;
import com.blakebr0.cucumber.util.Utils;
import com.blakebr0.extendedcrafting.Tags;
import com.blakebr0.extendedcrafting.client.container.ContainerCompressor;
import com.blakebr0.extendedcrafting.crafting.CompressorRecipe;
import com.blakebr0.extendedcrafting.tile.TileCompressor;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GuiCompressor extends GuiContainer {

    private static final ResourceLocation GUI = ResourceHelper.getResource(Tags.MODID, "textures/gui/compressor.png");

    private final TileCompressor tile;

    public GuiCompressor(TileCompressor tile, ContainerCompressor container) {
        super(container);
        this.tile = tile;
        this.xSize = 176;
        this.ySize = 194;
    }

    private int getEnergyBarScaled(int pixels) {
        int i = this.tile.getEnergy().getEnergyStored();
        int j = this.tile.getEnergy().getMaxEnergyStored();
        return (int) (j != 0 && i != 0 ? (long) i * pixels / j : 0);
    }

    private int getMaterialBarScaled(int pixels) {
        CompressorRecipe recipe = this.tile.getRecipe();
        if (recipe == null) {
            return 0;
        }
        int i = MathHelper.clamp(this.tile.getMaterialCount(), 0, recipe.getInputCount());
        int j = recipe.getInputCount();
        return j != 0 && i != 0 ? i * pixels / j : 0;
    }

    private int getProgressBarScaled(int pixels) {
        int i = this.tile.getProgress();
        CompressorRecipe recipe = this.tile.getRecipe();
        if (recipe == null) {
            return 0;
        }
        int j = recipe.getPowerCost();
        return (int) (j != 0 && i != 0 ? (long) i * pixels / j : 0);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String s = Utils.localize("container.ec.compressor");
        this.fontRenderer.drawString(s, this.xSize / 2 - this.fontRenderer.getStringWidth(s) / 2, 6, 4210752);
        this.fontRenderer.drawString(Utils.localize("container.inventory"), 8, this.ySize - 94, 4210752);
    }

    @Override
    public void initGui() {
        super.initGui();
        // 删除所有按钮添加代码
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        int left = this.guiLeft;

        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);

        if (mouseX > left + 7 && mouseX < guiLeft + 20 && mouseY > this.guiTop + 17 && mouseY < this.guiTop + 94) {
            this.drawHoveringText(Collections.singletonList(Utils.format(this.tile.getEnergy().getEnergyStored()) + " FE"), mouseX, mouseY);
        }

        if (mouseX > left + 60 && mouseX < guiLeft + 85 && mouseY > this.guiTop + 74 && mouseY < this.guiTop + 83) {
            List<String> l = new ArrayList<>();
            if (this.tile.getMaterialCount() < 1) {
                l.add(Utils.localize("tooltip.ec.empty"));
            } else {
                if (!this.tile.getMaterialStack().isEmpty()) {
                    l.add(this.tile.getMaterialStack().getDisplayName());
                }
                l.add(Utils.format(this.tile.getMaterialCount()) + " / " + Utils.format((this.tile.getRecipe() != null ? this.tile.getRecipe().getInputCount() : 0)));
            }
            this.drawHoveringText(l, mouseX, mouseY);
        }

        // 删除所有按钮相关的 Tooltip 代码
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(GUI);
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);

        int i1 = this.getEnergyBarScaled(78);
        this.drawTexturedModalRect(x + 7, y + 95 - i1, 178, 78 - i1, 15, i1 + 1);

        if (this.tile != null && this.tile.getRecipe() != null) {
            if (this.tile.getMaterialCount() > 0 && this.tile.getRecipe().getInputCount() > 0) {
                int i2 = getMaterialBarScaled(26);
                this.drawTexturedModalRect(x + 60, y + 74, 194, 19, i2 + 1, 10);
            }
            if (this.tile.getProgress() > 0 && this.tile.getRecipe().getPowerCost() > 0) {
                int i2 = getProgressBarScaled(24);
                this.drawTexturedModalRect(x + 96, y + 47, 194, 0, i2 + 1, 16);
            }
        }

        // 删除所有按钮状态绘制代码
    }
}
