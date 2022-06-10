package cat.jiu.mi.util;

import cat.jiu.mi.MI;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(
	modid = MI.MODID,
	name = "jiu/" + MI.MODID + "/main",
	category = "config_main")
@Mod.EventBusSubscriber(modid = MI.MODID)
public class MIConfigs {
	@Config.LangKey("config.mi.cap.all")
	@Config.Comment("Support all tileentity")
	public static boolean Support_All_Capability = false;
	
	@SubscribeEvent
	public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
		if(event.getModID().equals(MI.MODID)) {
			ConfigManager.sync(MI.MODID, Config.Type.INSTANCE);
		}
	}
}
