package cat.jiu.mi.ui;

import cat.jiu.mi.MI;
import cat.jiu.mi.blocks.tile.TileMirageInterface;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {
	public static final int side = 1000;
	
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		int sideID = ID - 1000;
		EnumFacing side = MI.getSide(sideID);
		BlockPos pos = new BlockPos(x, y, z);
		TileEntity te = world.getTileEntity(pos);
		
		switch(ID - sideID) {
			case GuiHandler.side:
				if(te instanceof TileMirageInterface) return new ContainerMirageInterface(player, world, pos, side);
			default: return null;
		}
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		int sideID = ID - 1000;
		EnumFacing side = MI.getSide(sideID);
		BlockPos pos = new BlockPos(x, y, z);
		TileEntity te = world.getTileEntity(pos);
		
		switch(ID - sideID) {
			case GuiHandler.side:
				if(te instanceof TileMirageInterface) return new GuiMirageInterface(player, world, pos, side);
			default: return null;
		}
	}
}
