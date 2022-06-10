package cat.jiu.mi.proxy;

import java.lang.reflect.Field;
import java.util.List;

import cat.jiu.core.JiuCore;
import cat.jiu.mi.util.client.CapabilityModelLoad;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.SimpleReloadableResourceManager;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends ServerProxy {
	private boolean isEnable = false;
	public ClientProxy() {
		if(Loader.isModLoaded("jiucore")) {
			((cat.jiu.core.proxy.ClientProxy)JiuCore.proxy).addCustomResourcePack(new CapabilityModelLoad());
			this.isEnable = true;
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void preInit(FMLPreInitializationEvent event) {
		super.preInit(event);
		if(!this.isEnable) {
			try {
				Field resourceList = FMLClientHandler.class.getDeclaredField("resourcePackList");
				resourceList.setAccessible(true);
				List<IResourcePack> resourcePackList = (List<IResourcePack>) resourceList.get(FMLClientHandler.instance());
				
				if(resourcePackList != null && Minecraft.getMinecraft().getResourceManager() instanceof SimpleReloadableResourceManager) {
					SimpleReloadableResourceManager manger = ((SimpleReloadableResourceManager)Minecraft.getMinecraft().getResourceManager());
					IResourcePack pack = new CapabilityModelLoad();
					resourcePackList.add(pack);
					manger.reloadResourcePack(pack);
				}
			}catch(NoSuchFieldException | IllegalAccessException | IllegalArgumentException | SecurityException e) {
				e.printStackTrace();
			}
		}
	}
}
