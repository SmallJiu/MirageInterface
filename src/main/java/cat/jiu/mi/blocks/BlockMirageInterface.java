package cat.jiu.mi.blocks;

import java.time.LocalDateTime;
import java.util.List;

import cat.jiu.mi.MI;
import cat.jiu.mi.blocks.tile.TileMirageInterface;
import cat.jiu.mi.ui.GuiHandler;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemStackHandler;

public class BlockMirageInterface extends Block implements ITileEntityProvider {
	public final ItemBlock itemblock;
	
	public BlockMirageInterface() {
		super(Material.IRON);
		super.setRegistryName(MI.MODID, "mirage_interface");
		LocalDateTime date = LocalDateTime.now();
		super.setUnlocalizedName(MI.MODID + ".interface." + (isTheDay(date, 11, 27) ? 1 : isTheDay(date, 12, 12) ? 2 : 0));
		super.setCreativeTab(CreativeTabs.TRANSPORTATION);
		super.setHardness(3.0F);
		super.setResistance(5.0F);
		super.setSoundType(SoundType.METAL);
		this.itemblock = (ItemBlock) new ItemBlock(this).setRegistryName(this.getRegistryName());
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		player.openGui(MI.MODID, GuiHandler.side + facing.getIndex(), world, pos.getX(), pos.getY(), pos.getZ());
		return true;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, World player, List<String> infos, ITooltipFlag advanced) {
		LocalDateTime date = LocalDateTime.now();
		if(isTheDay(date, 11, 27)) {
			infos.add(I18n.format("info.wf.mirage.0.name"));
			infos.add(I18n.format("info.wf.mirage.1.name"));
			infos.add(I18n.format("info.wf.mirage.2.name"));
		}else if(isTheDay(date, 12, 12)) {
			infos.add(I18n.format("info.wf.mirage.prime.0.name"));
			infos.add(I18n.format("info.wf.mirage.prime.1.name"));
			infos.add(I18n.format("info.wf.mirage.prime.2.name"));
		}
	}
	
	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		TileEntity tile = world.getTileEntity(pos);
		if(tile instanceof TileMirageInterface) {
			for(EnumFacing side : EnumFacing.values()) {
				ItemStackHandler sideStack = ((TileMirageInterface) tile).getSlot(side);
				for(int i = 0; i < sideStack.getSlots(); i++) {
					ItemStack stack = sideStack.getStackInSlot(i);
					if(!stack.isEmpty()) {
						Block.spawnAsEntity(world, pos, stack);
					}
				}
			}
		}
		super.breakBlock(world, pos, state);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileMirageInterface();
	}
	
	private static boolean isTheDay(LocalDateTime date, int month, int day) {
		return date.getMonthValue() == month && date.getDayOfMonth() == day;
	}
}
