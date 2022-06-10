package cat.jiu.mi.util;

import cat.jiu.mi.MI;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@EventBusSubscriber
public class Register {
	@SubscribeEvent
	public static void onBlockRegister(RegistryEvent.Register<Block> event) {
		event.getRegistry().register(MI.face);
	}

	@SubscribeEvent
	public static void onItemRegister(RegistryEvent.Register<Item> event) {
		event.getRegistry().register(MI.mirage_stabilizer);
		event.getRegistry().register(MI.mirage_connector);
		event.getRegistry().register(MI.face.itemblock);
		event.getRegistry().register(MI.capabilitys);
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void onModelRegister(ModelRegistryEvent event) {
		register(MI.mirage_stabilizer, 0, "mirage_stabilizer");
		register(Item.getItemFromBlock(MI.face), 0, "mirage_interface");
		register(MI.mirage_connector, 0, "mirage_connector");
		
		for(int i = 0; i < MI.capabilitySize(); i++) {
			String name = MI.getCapability(i).name.toLowerCase().replaceAll(" ", "_");
			register(MI.capabilitys, i, "capabilitys/" + name);
		}
	}

	@SideOnly(Side.CLIENT)
	private static void register(Item item, int meta, String path) {
		ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(new ResourceLocation(MI.MODID, path), "inventory"));
	}
}
