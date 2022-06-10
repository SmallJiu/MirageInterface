package cat.jiu.mi.items;

import java.util.List;

import cat.jiu.mi.MI;
import cat.jiu.mi.blocks.tile.TileMirageInterface;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

public class ItemMirageConnector extends Item {
	public ItemMirageConnector() {
		this.setRegistryName(MI.MODID, "mirage_connector");
		this.setUnlocalizedName(MI.MODID + ".mirage_connector");
		this.setCreativeTab(CreativeTabs.TRANSPORTATION);
		this.setMaxStackSize(1);
		this.setNoRepair();
	}
	
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> infos, ITooltipFlag flagIn) {
		NBTTagCompound nbt = stack.getTagCompound();
		if(nbt == null || nbt.getSize() == 0) return;
		
		NBTTagCompound nbtPos = nbt.getCompoundTag("pos");
		infos.add(I18n.format("info.mi.connector.bind"));
		StringBuilder s = new StringBuilder();
		
		s.append("Dimension: ");
		s.append(DimensionManager.getWorld(nbt.getInteger("dim")).provider.getDimensionType().getName());
		s.append("(");
		s.append(nbt.getInteger("dim"));
		s.append(")");
		infos.add(s.toString());
		s.setLength(0);
		
		s.append("XYZ: ");
		s.append(nbtPos.getInteger("x"));
		s.append(" / ");
		s.append(nbtPos.getInteger("y"));
		s.append(" / ");
		s.append(nbtPos.getInteger("z"));
		infos.add(s.toString());
	}
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if(!world.isRemote && player.isSneaking()) {
			TileEntity tile = world.getTileEntity(pos);
			ItemStack stack = player.getHeldItem(hand);
			if(tile != null) {
				if(tile.getClass() == TileMirageInterface.class) {
					TileMirageInterface te = (TileMirageInterface) tile;
					NBTTagCompound nbt = stack.getTagCompound();
					if(nbt != null && nbt.getSize() > 0) {
						te.set(nbt.getInteger("dim"), MI.fromNBT(nbt.getCompoundTag("pos")));
						stack.setTagCompound(null);
						player.sendStatusMessage(new TextComponentString(I18n.format("info.mi.connector.connect") + " Dim: " + player.dimension + ", XYZ: " + pos.getX() + "/" + pos.getY() + "/" + pos.getZ()), true);
						
						return EnumActionResult.SUCCESS;
					}
				}else {
					NBTTagCompound nbt = stack.getTagCompound();
					if(nbt == null) nbt = new NBTTagCompound();
					nbt.setInteger("dim", player.dimension);
					nbt.setTag("pos", MI.toNBT(pos));
					stack.setTagCompound(nbt);
					player.sendStatusMessage(new TextComponentString(I18n.format("info.mi.connector.bind") + " Dim: " + player.dimension + ", XYZ: " + pos.getX() + "/" + pos.getY() + "/" + pos.getZ()), true);
					
					return EnumActionResult.SUCCESS;
				}
			}
		}
		
		return EnumActionResult.PASS;
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return "item." + MI.MODID + ".mirage_connector." + stack.getMetadata();
	}
}
