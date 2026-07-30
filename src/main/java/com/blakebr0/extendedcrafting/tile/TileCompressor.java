package com.blakebr0.extendedcrafting.tile;

import com.blakebr0.cucumber.energy.EnergyStorageCustom;
import com.blakebr0.cucumber.helper.StackHelper;
import com.blakebr0.extendedcrafting.config.ModConfig;
import com.blakebr0.extendedcrafting.crafting.CompressorRecipe;
import com.blakebr0.extendedcrafting.crafting.CompressorRecipeManager;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class TileCompressor
		extends TileInventoryBase
		implements ISidedInventory, ITickable {

	private NonNullList<ItemStack> inventoryStacks = NonNullList.withSize(2, ItemStack.EMPTY); // 减少到2个槽位
	private final EnergyStorageCustom energy = new EnergyStorageCustom(ModConfig.confCompressorRFCapacity);
	private ItemStack materialStack = ItemStack.EMPTY;
	private int materialCount;
	private int progress;
	private boolean ejecting = false;
	private int oldEnergy;
	private boolean inputLimit = true;

	public TileCompressor() {
		super("compressor");
	}

	private List<CompressorRecipe> getValidRecipes(ItemStack stack) {
		List<CompressorRecipe> valid = new ArrayList<>();
		if (!stack.isEmpty()) {
			for (CompressorRecipe recipe : CompressorRecipeManager.getInstance().getRecipes()) {
				if (recipe.getInput().apply(stack)) {
					valid.add(recipe);
				}
			}
		}
		return valid;
	}

	@Override
	public void update() {
		boolean mark = false;

		if (!this.getWorld().isRemote) {

			ItemStack output = this.getStackInSlot(0);
			ItemStack input = this.getStackInSlot(1);

			if(!input.isEmpty()) {
				if(this.materialStack.isEmpty()) {
					this.materialStack = input.copy();
					mark = true;
				}
			}

			CompressorRecipe recipe = getRecipe();

			// Consuming Input Items
			if (!input.isEmpty() && (!this.inputLimit || (recipe != null && this.materialCount < recipe.getInputCount()))) {
				if (StackHelper.areStacksEqual(input, this.materialStack)) {
					int consumeAmount = input.getCount();
					if (this.inputLimit && recipe != null) {
						consumeAmount = Math.min(consumeAmount, recipe.getInputCount() - this.materialCount);
					}

					StackHelper.decrease(input, consumeAmount, false);
					this.materialCount += consumeAmount;
					mark = true;
				}
			}
			else if(mark) {
				this.materialStack = ItemStack.EMPTY;
			}

			// Progressing output item
			if (recipe != null && this.getEnergy().getEnergyStored() > 0) {
				if (this.materialCount >= recipe.getInputCount()) {
					this.process(recipe);
					mark = true;
					if (this.progress == recipe.getPowerCost()) {
						ItemStack recipeOutput = recipe.getOutput();
						if ((output.isEmpty() || StackHelper.areStacksEqual(output, recipeOutput)) && output.getCount() < recipeOutput.getMaxStackSize()) {
							this.addStackToSlot(0, recipe.getOutput());
							this.progress = 0;
							this.materialCount -= recipe.getInputCount();

							if (this.materialCount <= 0) {
								this.materialStack = ItemStack.EMPTY;
							}
						}
					}
				}
			}

			if (this.ejecting) {
				if (this.materialCount > 0 && !this.materialStack.isEmpty()) {
					ItemStack toAdd = this.materialStack.copy();
					int addCount = Math.min(this.materialCount, toAdd.getMaxStackSize());
					toAdd.setCount(addCount);

					int added = this.addStackToSlot(0, toAdd);
					if (added > 0) {
						this.materialCount -= added;
						if (this.materialCount < 1) {
							this.materialStack = ItemStack.EMPTY;
							this.ejecting = false;
						}
						if (this.progress > 0) {
							this.progress = 0;
						}
						mark = true;
					}
				}
			}
		}

		if (this.oldEnergy != this.energy.getEnergyStored()) {
			this.oldEnergy = this.energy.getEnergyStored();
			mark = true;
		}

		if (mark) {
			this.markDirty();
		}
	}

	@Nullable
	public CompressorRecipe getRecipe() {
		List<CompressorRecipe> recipes = this.getValidRecipes(this.materialStack);
		if (!recipes.isEmpty()) {
			// 直接返回第一个匹配的配方，不再检查催化剂
			return recipes.get(0);
		}
		return null;
	}

	private void process(CompressorRecipe recipe) {
		int extract = recipe.getPowerRate();
		int difference = recipe.getPowerCost() - this.progress;
		if (difference < extract) {
			extract = difference;
		}

		int extracted = this.getEnergy().extractEnergy(extract, false);
		this.progress += extracted;
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound = super.writeToNBT(compound);
		ItemStackHelper.saveAllItems(compound, this.inventoryStacks);
		compound.setInteger("MaterialCount", this.materialCount);
		if (this.materialStack == null) {
			this.materialStack = ItemStack.EMPTY;
		}
		if (!this.materialStack.isEmpty()) {
			compound.setTag("MaterialStack", this.materialStack.serializeNBT());
		}
		compound.setInteger("Progress", this.progress);
		compound.setBoolean("Ejecting", this.ejecting);
		compound.setInteger("Energy", this.energy.getEnergyStored());
		compound.setBoolean("InputLimit", this.inputLimit);
		return compound;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		this.inventoryStacks = NonNullList.withSize(2, ItemStack.EMPTY);
		ItemStackHelper.loadAllItems(compound, this.inventoryStacks);
		this.materialCount = compound.getInteger("MaterialCount");
		this.materialStack = new ItemStack(compound.getCompoundTag("MaterialStack"));
		this.progress = compound.getInteger("Progress");
		this.ejecting = compound.getBoolean("Ejecting");
		this.energy.setEnergy(compound.getInteger("Energy"));
		this.inputLimit = compound.getBoolean("InputLimit");
	}

	public int addStackToSlot(int slot, ItemStack stack) {
		ItemStack slotStack = this.getStackInSlot(slot);
		if (slotStack.isEmpty()) {
			this.setInventorySlotContents(slot, stack);
			return stack.getCount();
		} else {
			if (StackHelper.areStacksEqual(stack, slotStack) && slotStack.getCount() < slotStack.getMaxStackSize()) {
				ItemStack newStack = slotStack.copy();
				int newSize = Math.min(slotStack.getCount() + stack.getCount(), slotStack.getMaxStackSize());
				newStack.setCount(newSize);
				this.setInventorySlotContents(slot, newStack);
				return newSize - slotStack.getCount();
			}
		}
		return 0;
	}

	public EnergyStorageCustom getEnergy() {
		return this.energy;
	}

	public ItemStack getMaterialStack() {
		return this.materialStack;
	}

	public int getMaterialCount() {
		return this.materialCount;
	}

	public boolean isEjecting() {
		return this.ejecting;
	}

	public void toggleEjecting() {
		if (this.materialCount > 0) {
			this.ejecting = !this.ejecting;
			this.markDirty();
		}
	}

	public boolean isLimitingInput() {
		return this.inputLimit;
	}

	public void toggleInputLimit() {
		this.inputLimit = !this.inputLimit;
		this.markDirty();
	}

	public int getProgress() {
		return this.progress;
	}

	@Override
	public int getSizeInventory() {
		return this.inventoryStacks.size();
	}

	@Override
	public boolean isEmpty() {
		return this.inventoryStacks.stream().allMatch(ItemStack::isEmpty);
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		return this.inventoryStacks.get(index);
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		return ItemStackHelper.getAndSplit(this.inventoryStacks, index, count);
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		return ItemStackHelper.getAndRemove(this.inventoryStacks, index);
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		ItemStack itemstack = this.inventoryStacks.get(index);
		boolean flag = !stack.isEmpty() && StackHelper.areStacksEqual(stack, itemstack);
		this.inventoryStacks.set(index, stack);

		if (stack.getCount() > this.getInventoryStackLimit()) {
			stack.setCount(this.getInventoryStackLimit());
		}

		if (index == 0 && !flag) {
			this.markDirty();
		}
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return index != 0; // 只有输出槽不可插入
	}

	@Override
	public void clear() {
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		return side == EnumFacing.DOWN ? new int[]{0} : side == EnumFacing.UP ? new int[]{1} : new int[]{0, 1};
	}

	@Override
	public boolean canInsertItem(int index, ItemStack stack, EnumFacing direction) {
		return this.isItemValidForSlot(index, stack);
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
		return index == 0;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing side) {
		return this.getCapability(capability, side) != null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return (T) new SidedInvWrapper(this, facing);
		} else if (capability == CapabilityEnergy.ENERGY) {
			return CapabilityEnergy.ENERGY.cast(this.energy);
		}

		return super.getCapability(capability, facing);
	}
}
