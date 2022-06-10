package cat.jiu.mi.blocks.tile;

import cat.jiu.mi.MI;
import cat.jiu.mi.items.ItemCapabilitys;
import cat.jiu.mi.util.MIConfigs;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;

import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.ItemStackHandler;

public class TileMirageInterface extends TileEntity {
	private Integer dim = null;
	private BlockPos toPos = null;
	private WorldServer posWorld = null;
	
	private final MirageSideHandler up = new MirageSideHandler();
	private final MirageSideHandler down = new MirageSideHandler();
	private final MirageSideHandler west = new MirageSideHandler();
	private final MirageSideHandler south = new MirageSideHandler();
	private final MirageSideHandler east = new MirageSideHandler();
	private final MirageSideHandler north = new MirageSideHandler();
	
	public void set(int dim, BlockPos pos) {
		this.dim = dim;
		this.posWorld = DimensionManager.getWorld(dim, true);
		this.toPos = pos;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		if(nbt.hasKey("dim") && nbt.hasKey("pos")) {
			this.set(nbt.getInteger("dim"), MI.fromNBT(nbt.getCompoundTag("pos")));
		}
		this.up.deserializeNBT(nbt.getCompoundTag("up"));
		this.down.deserializeNBT(nbt.getCompoundTag("down"));
		this.west.deserializeNBT(nbt.getCompoundTag("west"));
		this.south.deserializeNBT(nbt.getCompoundTag("south"));
		this.east.deserializeNBT(nbt.getCompoundTag("east"));
		this.north.deserializeNBT(nbt.getCompoundTag("north"));
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		if(this.toPos != null && this.dim != null) {
			nbt.setInteger("dim", this.dim);
			nbt.setTag("pos", MI.toNBT(this.toPos));
		}
		nbt.setTag("up", this.up.serializeNBT());
		nbt.setTag("down", this.down.serializeNBT());
		nbt.setTag("west", this.west.serializeNBT());
		nbt.setTag("south", this.south.serializeNBT());
		nbt.setTag("east", this.east.serializeNBT());
		nbt.setTag("north", this.north.serializeNBT());
		
		return nbt;
	}
	
	@Override
	public boolean hasCapability(Capability<?> cap, EnumFacing facing) {
		if(this.toPos == null || this.dim == null) return false;
		if(this.posWorld == null) this.posWorld = DimensionManager.getWorld(this.dim, true);
		
		if(MIConfigs.Support_All_Capability) {
			if(this.posWorld != null && this.toPos != null && this.posWorld.isBlockLoaded(this.toPos)) {
				TileEntity te = this.posWorld.getTileEntity(this.toPos);
				if(te == null) return false;
				return te.hasCapability(cap, facing);
			}
		}else if(facing == null) {
			if(MI.hasCapability(cap)) {
				for(int i = 0; i < MI.capabilitySize(); i++) {
					if(MI.getCapability(i).capability == cap) {
						if(this.posWorld != null && this.toPos != null && this.posWorld.isBlockLoaded(this.toPos)) {
							TileEntity te = this.posWorld.getTileEntity(this.toPos);
							if(te == null) return false;
							return te.hasCapability(cap, facing);
						}
					}
				}
			}
		}else {
			MirageSideHandler sideSlot = this.getSlot(facing);
			if(sideSlot.isEmpty()) return false;
			for(int i = 0; i < sideSlot.getSlots(); i++) {
				ItemStack stack = sideSlot.getStackInSlot(i);
				if(!stack.isEmpty() && MI.hasCapability(stack.getMetadata())) {
					if(cap == MI.getCapability(stack.getMetadata()).capability) {
						if(this.posWorld != null && this.toPos != null && this.posWorld.isBlockLoaded(this.toPos)) {
							TileEntity te = this.posWorld.getTileEntity(this.toPos);
							if(te == null) return false;
							return te.hasCapability(cap, facing);
						}
					}
				}
			}
		}
		return false;
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if(this.toPos == null || this.dim == null) return null;
		if(this.posWorld == null) this.posWorld = DimensionManager.getWorld(this.dim, true);
		if(this.posWorld != null && this.toPos != null && this.posWorld.isBlockLoaded(this.toPos)) {
			TileEntity te = this.posWorld.getTileEntity(this.toPos);
			if(te == null) return null;
			return te.getCapability(capability, facing);
		}
		
		return null;
	}
	
	public MirageSideHandler getSlot(EnumFacing side) {
		switch(side) {
			case UP: return this.up;
			case NORTH: return this.north;
			case WEST: return this.west;
			case EAST: return this.east;
			case SOUTH: return this.south;
			default: return this.down;
		}
	}
	
	public static class MirageSideHandler extends ItemStackHandler {
		public MirageSideHandler() {super(9);}
		public int getSlotLimit(int slot) {return 1;}
		
		public boolean isEmpty() {
			for(ItemStack slotStack : this.stacks) {
				if(!slotStack.isEmpty()) return false;
			}
			return true;
		}
		
		@Override
		public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
			if(!this.isItemValid(slot, stack)) return stack;
			for(ItemStack slotStack : this.stacks) {
				if(!slotStack.isEmpty() && slotStack.getMetadata() == stack.getMetadata()) {
					return stack;
				}
			}
			return super.insertItem(slot, stack, simulate);
		}
		
		@Override
		public boolean isItemValid(int slot, ItemStack stack) {
			return stack.getItem().getClass() == ItemCapabilitys.class;
		}
	}
}
