package cat.jiu.mi.util;

import java.util.List;
import java.util.StringJoiner;
import java.util.Map.Entry;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * @see cat.jiu.core.util.helpers.ItemUtils#toStack(JsonElement)
 * @author small_jiu
 */
public final class JsonToStackUtil {
	public static ItemStack toStack(JsonElement e) {
		if(e.isJsonObject()) {
			return toStack(e.getAsJsonObject());
		}else if(e.isJsonPrimitive()) {
			return toStack(e.getAsString());
		}
		return null;
	}
	
	private static ItemStack toStack(JsonObject obj) {
		Item item = Item.getByNameOrId(obj.get("name").getAsString());
		if(item == null) return null;
		int count = obj.has("count") ? obj.get("count").getAsInt() : obj.has("amount") ? obj.get("amount").getAsInt() : 1;
		int meta = obj.has("meta") ? obj.get("meta").getAsInt() : obj.has("data") ? obj.get("data").getAsInt() : 0;
		NBTTagCompound nbt = obj.has("nbt") ? getNBT(obj.get("nbt").getAsJsonObject()) : null;

		return setNBT(new ItemStack(item, count, meta), nbt);
	}
	
	private static NBTTagCompound getNBT(JsonObject obj) {
		NBTTagCompound nbt = new NBTTagCompound();
		if(obj.has("string")) {
			for(Entry<String, JsonElement> nbts : obj.get("string").getAsJsonObject().entrySet()) {
				nbt.setString(nbts.getKey(), nbts.getValue().getAsString());
			}
		}
		if(obj.has("boolean")) {
			for(Entry<String, JsonElement> nbts : obj.get("boolean").getAsJsonObject().entrySet()) {
				nbt.setBoolean(nbts.getKey(), nbts.getValue().getAsBoolean());
			}
		}
		if(obj.has("int")) {
			for(Entry<String, JsonElement> nbts : obj.get("int").getAsJsonObject().entrySet()) {
				nbt.setInteger(nbts.getKey(), nbts.getValue().getAsInt());
			}
		}
		if(obj.has("long")) {
			for(Entry<String, JsonElement> nbts : obj.get("long").getAsJsonObject().entrySet()) {
				nbt.setLong(nbts.getKey(), nbts.getValue().getAsLong());
			}
		}
		if(obj.has("float")) {
			for(Entry<String, JsonElement> nbts : obj.get("float").getAsJsonObject().entrySet()) {
				nbt.setFloat(nbts.getKey(), nbts.getValue().getAsFloat());
			}
		}
		if(obj.has("double")) {
			for(Entry<String, JsonElement> nbts : obj.get("double").getAsJsonObject().entrySet()) {
				nbt.setDouble(nbts.getKey(), nbts.getValue().getAsDouble());
			}
		}
		if(obj.has("byte")) {
			for(Entry<String, JsonElement> nbts : obj.get("byte").getAsJsonObject().entrySet()) {
				nbt.setByte(nbts.getKey(), nbts.getValue().getAsByte());
			}
		}
		if(obj.has("short")) {
			for(Entry<String, JsonElement> nbts : obj.get("short").getAsJsonObject().entrySet()) {
				nbt.setShort(nbts.getKey(), nbts.getValue().getAsShort());
			}
		}
		if(obj.has("int_array")) {
			for(Entry<String, JsonElement> nbts : obj.get("int_array").getAsJsonObject().entrySet()) {
				JsonArray array = nbts.getValue().getAsJsonArray();
				int[] int_array = new int[array.size()];
				for(int i = 0; i < array.size(); i++) {
					int_array[i] = array.get(i).getAsInt();
				}
				nbt.setIntArray(nbts.getKey(), int_array);
			}
		}
		if(obj.has("short_array")) {
			for(Entry<String, JsonElement> nbts : obj.get("short_array").getAsJsonObject().entrySet()) {
				JsonArray array = nbts.getValue().getAsJsonArray();
				short[] num_array = new short[array.size()];
				for(int i = 0; i < array.size(); i++) {
					num_array[i] = array.get(i).getAsShort();
				}
				setNBT(nbt, nbts.getKey(), num_array);
			}
		}
		if(obj.has("byte_array")) {
			for(Entry<String, JsonElement> nbts : obj.get("byte_array").getAsJsonObject().entrySet()) {
				JsonArray array = nbts.getValue().getAsJsonArray();
				byte[] num_array = new byte[array.size()];
				for(int i = 0; i < array.size(); i++) {
					num_array[i] = array.get(i).getAsByte();
				}
				nbt.setByteArray(nbts.getKey(), num_array);
			}
		}
		if(obj.has("double_array")) {
			for(Entry<String, JsonElement> nbts : obj.get("double_array").getAsJsonObject().entrySet()) {
				JsonArray array = nbts.getValue().getAsJsonArray();
				double[] num_array = new double[array.size()];
				for(int i = 0; i < array.size(); i++) {
					num_array[i] = array.get(i).getAsDouble();
				}
				setNBT(nbt, nbts.getKey(), num_array);
			}
		}
		if(obj.has("float_array")) {
			for(Entry<String, JsonElement> nbts : obj.get("float_array").getAsJsonObject().entrySet()) {
				JsonArray array = nbts.getValue().getAsJsonArray();
				float[] num_array = new float[array.size()];
				for(int i = 0; i < array.size(); i++) {
					num_array[i] = array.get(i).getAsFloat();
				}
				setNBT(nbt, nbts.getKey(), num_array);
			}
		}
		if(obj.has("tags")) {
			for(Entry<String, JsonElement> tags : obj.get("tags").getAsJsonObject().entrySet()) {
				nbt.setTag(tags.getKey(), getNBT(tags.getValue().getAsJsonObject()));
				
			}
		}
		
		return nbt;
	}
	
	private static ItemStack toStack(String stack) {
		if(stack.contains("@")) {
			String[] name = stack.split("@");
			Item item = Item.getByNameOrId(name[0]);
			int meta = 0;
			int amount = 1;
			NBTTagCompound nbt = null;
			
			switch(name.length) {
				case 4:
					nbt = getNBT(new JsonParser().parse(name[3]).getAsJsonObject());
				case 3:
					meta = Integer.parseInt(name[2]);
				case 2:
					amount = Integer.parseInt(name[1]);
					break;
			}
			if(item != null) {
				return setNBT(new ItemStack(item, amount, meta), nbt);
			}
		}else {
			return new ItemStack(Item.getByNameOrId(stack));
		}
		return null;
	}
	
	private static NBTTagCompound setNBT(NBTTagCompound nbt, String nbtName, double[] value) {
		nbt.setString(nbtName, "double_array@" + toString(toArray(value)));
		return nbt;
	}
	private static NBTTagCompound setNBT(NBTTagCompound nbt, String nbtName, float[] value) {
		nbt.setString(nbtName, "float_array@" + toString(toArray(value)));
		return nbt;
	}
	private static NBTTagCompound setNBT(NBTTagCompound nbt, String nbtName, short[] value) {
		nbt.setString(nbtName, "short_array@" + toString(toArray(value)));
		return nbt;
	}
	
	private static ItemStack setNBT(ItemStack stack, NBTTagCompound nbt) {
		if(nbt != null) stack.setTagCompound(nbt);
		return stack;
	}
	
	private static <T> String toString(T[] args) {
		if(args == null || args.length == 0) {
			return "null";
		}
		List<String> l = Lists.newArrayList();
		for(T i : args) {
			l.add(i.toString());
		}
		return toString(l.toArray(new String[0]));
	}
	
	private static Short[] toArray(short[] args) {
		Short[] arg = new Short[args.length];
		for (int i = 0; i < arg.length; i++) {
			arg[i] = args[i];
		}
		return arg;
	}
	
	private static Double[] toArray(double[] args) {
		Double[] arg = new Double[args.length];
		for (int i = 0; i < arg.length; i++) {
			arg[i] = args[i];
		}
		return arg;
	}
	
	private static Float[] toArray(float[] args) {
		Float[] arg = new Float[args.length];
		for (int i = 0; i < arg.length; i++) {
			arg[i] = args[i];
		}
		return arg;
	}
	
	private static String toString(String[] args) {
		if(args == null || args.length == 0) {
			return "null";
		}
		StringJoiner j = new StringJoiner(",");
		for(String arg : args) {
			j.add(arg);
		}
		return j.toString();
	}
}
