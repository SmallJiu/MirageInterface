package cat.jiu.mi.items;

import cat.jiu.mi.MI;

import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemCapabilitys extends Item {
	public ItemCapabilitys() {
		this.setRegistryName(MI.MODID, "capability");
		this.setUnlocalizedName(MI.MODID + ".capability");
		this.setCreativeTab(CreativeTabs.TRANSPORTATION);
		this.setHasSubtypes(true);
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public final String getItemStackDisplayName(ItemStack stack) {
		String name = MI.getCapability(stack.getMetadata()).name.toLowerCase().replaceAll(" ", "_");
		String unloc = this.getUnlocalizedName() + "." + name + ".name";
		String loc = I18n.format(unloc);
		
		if(loc.equalsIgnoreCase(unloc)) {
			return MI.getCapability(stack.getMetadata()).name + " (" + unloc + ")";
		}
		return loc;
	}
	
	@Override
	public final void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
		if(!this.isInCreativeTab(tab)) return;
		for(int i = 0; i < MI.capabilitySize(); i++) {
			items.add(new ItemStack(this, 1, i));
		}
	}
}
