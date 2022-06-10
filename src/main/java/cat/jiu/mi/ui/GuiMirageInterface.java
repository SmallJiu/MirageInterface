package cat.jiu.mi.ui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiMirageInterface extends GuiContainer {
	private static final ResourceLocation TEXTURES = new ResourceLocation("textures/gui/container/dispenser.png");
	private final EnumFacing side;
	private final InventoryPlayer playerInv;
	private final World world;
	private final BlockPos pos;
	
	public GuiMirageInterface(EntityPlayer player, World world, BlockPos pos, EnumFacing side) {
		super(new ContainerMirageInterface(player, world, pos, side));
		this.world = world;
		this.pos = pos;
		this.side = side;
		this.playerInv = player.inventory;
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		super.renderHoveredToolTip(mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;
		
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(TEXTURES);
		this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		this.fontRenderer.drawString(I18n.format(this.world.getBlockState(this.pos).getBlock().getUnlocalizedName() + ".name"), 7, 6, 4210752);
		String s = I18n.format("info.mi." + this.side);
		this.fontRenderer.drawString(s, this.xSize-6-this.fontRenderer.getStringWidth(s), 6, 4210752);
		this.fontRenderer.drawString(playerInv.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 4210752);
	}
}
