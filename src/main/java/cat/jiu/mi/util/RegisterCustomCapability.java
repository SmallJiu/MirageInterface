package cat.jiu.mi.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;

import cat.jiu.core.util.JiuUtils;
import cat.jiu.mi.MI;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.Loader;

public class RegisterCustomCapability {
	public static final Map<Integer, JsonElement> unregister = Maps.newHashMap();
	
	public static void register() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, JsonIOException, JsonSyntaxException, FileNotFoundException {
		File config = new File("./capabilitys.json");
		HashMap<String, Capability<?>> capMap = getCapabilitys();
		if(config.exists()) {
			JsonObject file = new JsonParser().parse(new FileReader(config)).getAsJsonObject();
			
			for(Map.Entry<String, JsonElement> jobj : file.entrySet()) {
				JsonArray array = jobj.getValue().getAsJsonArray();
				for(int i = 0; i < array.size(); i++) {
					JsonObject capability = array.get(i).getAsJsonObject();
					String handler = capability.get("handler").getAsString();
					
					if(capMap.containsKey(handler)) {
						Capability<?> hand = capMap.get(handler);
						String name = capability.get("name").getAsString();
						JsonElement itemE = capability.get("item");
						JsonElement texture = new JsonPrimitive("minecraft:items/structure_void");
						if(capability.has("texture")) texture = capability.get("texture");
						
						ItemStack stack = Loader.isModLoaded("jiucore") ? JiuUtils.item.toStack(itemE) : JsonToStackUtil.toStack(itemE);
						
						if(stack == null || stack.isEmpty()) {
							unregister.put(MI.capabilitySize(), itemE);
							MI.addCapability(hand, name, ItemStack.EMPTY, texture, true);
						}else {
							MI.addCapability(hand, name, stack, texture, true);
						}
					}
				}
			}
		}
		
		List<String> uninitCap = Lists.newArrayList();
		uninitCap.add("This list is not load on Mirage Interface, you can use it to add");
		uninitCap.add("");
		for(Entry<String, Capability<?>> initCaps : capMap.entrySet()) {
			if(!MI.hasCapability(initCaps.getValue())) {
				uninitCap.add(initCaps.getKey());
				continue;
			}
		}
		writeCapability(uninitCap);
	}

	@SuppressWarnings("all")
	private static HashMap<String, Capability<?>> getCapabilitys() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Field map = CapabilityManager.class.getDeclaredField("providers");
		map.setAccessible(true);
		return Maps.newHashMap((IdentityHashMap<String, Capability<?>>) map.get(CapabilityManager.INSTANCE));
	}
	
	private static void writeCapability(List<String> initCap) {
		File file = new File("./capabilitys.txt");
		if(file.exists()) {
			file.delete();
		}
		try {
			file.createNewFile();
			OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(file, true));
			for(String cap : initCap) {
				out.write(cap + "\n");
			}
			out.close();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
}
