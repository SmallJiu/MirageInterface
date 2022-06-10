package cat.jiu.mi.ui;

import java.util.List;

import cat.jiu.mi.blocks.tile.TileMirageInterface;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerMirageInterface extends Container {
	protected final InventoryPlayer inventory;
	protected final World world;
	protected final BlockPos pos;
	protected final TileMirageInterface te;
	protected final EnumFacing side;
	
	public ContainerMirageInterface(EntityPlayer player, World world, BlockPos pos, EnumFacing side) {
		this.inventory = player.inventory;
		this.world = world;
		this.pos = pos;
		this.side = side;
		
		TileEntity posTe = world.getTileEntity(pos);
		if(posTe instanceof TileMirageInterface) {
			this.te = (TileMirageInterface) posTe;
		}else {
			this.te = null;
		}
		this.addHandlerSlot(this.te.getSlot(side), 62, 17);
		this.addPlayerInventorySlot(8, 84);
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int index) {
		Slot slot = inventorySlots.get(index);

		if(slot == null || !slot.getHasStack()) {
			return ItemStack.EMPTY;
		}

		ItemStack newStack = slot.getStack(),
				  oldStack = newStack.copy();

		boolean isMerged = false;

		if(index >= 0 && index <= 8) {
			isMerged = slot.getHasStack() && this.mergeItemStack(newStack, 9, 44, false);
		}else if(index >= 9 && index <= 44) {
			for(int machineSlotIndex = 0; machineSlotIndex < 9; machineSlotIndex++) {
				Slot machineSlot = this.inventorySlots.get(machineSlotIndex);
				if(!machineSlot.getHasStack() && !this.hasRepeatItem(this.inventorySlots, newStack) && machineSlot.isItemValid(newStack)) {
					machineSlot.putStack(new ItemStack(newStack.getItem(), 1, newStack.getMetadata()));
					newStack.shrink(1);
					isMerged = true;
					break;
				}
			}
		}

		if(!isMerged) {
			return ItemStack.EMPTY;
		}

		if(newStack.getCount() == 0) {
			slot.putStack(ItemStack.EMPTY);
		}else {
			slot.onSlotChanged();
		}

		slot.onTake(player, newStack);

		return oldStack;
	}
	
	protected boolean hasRepeatItem(List<Slot> inventorySlots, ItemStack other) {
		for(int i = 0; i < 9; i++) {
			Slot slot = inventorySlots.get(i);
			if(slot.getHasStack()) {
				ItemStack slotStack = slot.getStack();
				if(slotStack.getItem() == other.getItem()) {
					if(slotStack.getMetadata() == other.getMetadata()) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer player) {
		boolean haveBlock = player.world.getBlockState(this.pos).getBlock() != Blocks.AIR;
		return player.world.equals(this.world) && player.getDistanceSq(this.pos) <= 32.0 && haveBlock;
	}
	
	protected void addHandlerSlot(ItemStackHandler handler, int x, int y) {
		int slotIndex = 0;
		for(int slotY = 0; slotY < 3; slotY++) {
			for(int slotX = 0; slotX < 3; slotX++) {
				this.addSlotToContainer(new SlotItemHandler(handler, slotIndex, x + 18 * slotX, y + (18 * slotY)));
				slotIndex += 1;
			}	
		}
	}
	
	protected void addPlayerInventorySlot(int x, int y) {
		int slotIndex = 0;
		for(int slotX = 0; slotX < 9; slotX++) {
			this.addSlotToContainer(new Slot(this.inventory, slotIndex, x + 18 * slotX, y + (18 * 2) + 22));
			slotIndex += 1;
		}
		
		for(int slotY = 0; slotY < 3; slotY++) {
			for(int slotX = 0; slotX < 9; slotX++) {
				this.addSlotToContainer(new Slot(this.inventory, slotIndex, x + 18 * slotX, y + (18 * slotY)));
				slotIndex += 1;
			}
		}
	}
}
