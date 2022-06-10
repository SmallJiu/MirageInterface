package cat.jiu.mi;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import cat.jiu.mi.ui.GuiHandler;
import cat.jiu.mi.util.CapabilityType;
import cat.jiu.mi.util.MIRecipe;
import cat.jiu.mi.util.RegisterCustomCapability;
import cat.jiu.mi.blocks.BlockMirageInterface;
import cat.jiu.mi.blocks.tile.TileMirageInterface;
import cat.jiu.mi.items.ItemCapabilitys;
import cat.jiu.mi.proxy.ServerProxy;
import cat.jiu.mi.items.ItemMirageConnector;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.model.animation.CapabilityAnimation;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.items.CapabilityItemHandler;

@Mod(
	modid = MI.MODID,
	name = MI.NAME,
	version = "1.0.0",
	useMetadata = true,
	guiFactory = "cat.jiu.mi.util.ConfigGuiFactory",
	dependencies = "after:jiucore@[1.1.0-20220608013004,);",
	acceptedMinecraftVersions = "[1.12.2]"
)
public class MirageInterface {
	public static final String MODID = "mi";
	public static final String NAME = "MirageInterface";
	public static final String OWNER = "small_jiu";
	
	public static final Item mirage_stabilizer = new Item()
													.setRegistryName(MI.MODID, "mirage_stabilizer")
													.setUnlocalizedName(MI.MODID + ".mirage_stabilizer")
													.setCreativeTab(CreativeTabs.TRANSPORTATION);
	public static final ItemMirageConnector mirage_connector = new ItemMirageConnector();
	public static final ItemCapabilitys capabilitys = new ItemCapabilitys();
	public static final BlockMirageInterface face = new BlockMirageInterface();
	
	@SidedProxy(serverSide = "cat.jiu.mi.proxy.ServerProxy", clientSide = "cat.jiu.mi.proxy.ClientProxy", modId = MI.MODID)
	public static ServerProxy proxy;
	
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		proxy.preInit(event);
		addCapability(CapabilityEnergy.ENERGY, "Forge Energy", new ItemStack(Items.REDSTONE));
		addCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, "Item", new ItemStack(Blocks.COBBLESTONE));
		addCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, "Fluid", new ItemStack(Items.WATER_BUCKET));
		addCapability(CapabilityAnimation.ANIMATION_CAPABILITY, "Animation Machine", new ItemStack(Items.IRON_INGOT));
		
		try {
			RegisterCustomCapability.register();
		}catch(NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException | JsonIOException | JsonSyntaxException | FileNotFoundException e) {
			e.printStackTrace();
		}
		
		NetworkRegistry.INSTANCE.registerGuiHandler(MI.MODID, new GuiHandler());
		GameRegistry.registerTileEntity(TileMirageInterface.class, new ResourceLocation(MI.MODID + ":" + "interface"));
	}
	
	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		MIRecipe.register();
	}
	
	private static final Map<Integer, CapabilityType> capability_map = Maps.newHashMap();
	private static final List<Capability<?>> caps = Lists.newArrayList();
	
	public static int capabilitySize() {
		return capability_map.size();
	}
	
	private static void addCapability(Capability<?> cap, String name, ItemStack craftStack) {
		addCapability(cap, name, craftStack, null, false);
	}
	public static void addCapability(Capability<?> cap, String name, ItemStack craftStack, JsonElement texture, boolean isCustom) {
		capability_map.put(capability_map.size(), new CapabilityType(cap, name, craftStack, texture, isCustom));
		caps.add(cap);
	}
	
	public static boolean hasCapability(int id){
		return capability_map.containsKey(id);
	}
	public static boolean hasCapability(Capability<?> cap){
		return caps.contains(cap);
	}
	public static boolean hasCapability(String capName){
		for(Entry<Integer, CapabilityType> caps : capability_map.entrySet()) {
			String name = caps.getValue().name.toLowerCase().replaceAll(" ", "_");
			if(name.equalsIgnoreCase(capName)) return true;
		}
		return false;
	}
	public static CapabilityType getCapability(String capName){
		for(Entry<Integer, CapabilityType> caps : capability_map.entrySet()) {
			String name = caps.getValue().name.replaceAll(" ", "_");
			if(name.equalsIgnoreCase(capName)) return caps.getValue();
		}
		return null;
	}
	public static CapabilityType getCapability(int id){
		return capability_map.get(id);
	}
}
