package cat.jiu.mi.util;

import com.google.gson.JsonElement;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;

public class CapabilityType {
	public final Capability<?> capability;
	public final String name;
	public final JsonElement textures;
	public final boolean isCustom;
	private ItemStack craftStack;
	
	public CapabilityType(Capability<?> cap, String name, ItemStack craftStack, boolean isCustom) {
		this(cap, name, craftStack, null, isCustom);
	}
	public CapabilityType(Capability<?> cap, String name, ItemStack craftStack, JsonElement textures, boolean isCustom) {
		this.capability = cap;
		this.name = name == null ? cap.getName() : name;
		this.craftStack = craftStack;
		this.textures = textures;
		this.isCustom = isCustom;
	}
	public ItemStack getCraftStack() {
		return craftStack;
	}
	public void setCraftStack(ItemStack craftStack) {
		this.craftStack = craftStack;
	}
	@Override
	public String toString() {
		return "Name: " + this.name + ", isCustom:" + this.isCustom +  ", Stack: " + this.craftStack + ", Capability: " + this.capability.getName() + ", Texture: " + this.textures;
	}
}
