package cat.jiu.mi;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class MI extends MirageInterface {
	public static BlockPos fromNBT(NBTTagCompound nbtPos) {
		return new BlockPos(nbtPos.getInteger("x"), nbtPos.getInteger("y"), nbtPos.getInteger("z"));
	}
	
	public static NBTTagCompound toNBT(BlockPos pos) {
		NBTTagCompound nbtPos = new NBTTagCompound();
		nbtPos.setInteger("x", pos.getX());
		nbtPos.setInteger("y", pos.getY());
		nbtPos.setInteger("z", pos.getZ());
		return nbtPos;
	}
	
	public static EnumFacing getSide(int id) {
		for(EnumFacing side : EnumFacing.values()) {
			if(side.getIndex() == id) return side; 
		}
		return null;
	}
}
