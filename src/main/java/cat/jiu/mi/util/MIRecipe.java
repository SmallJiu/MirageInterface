package cat.jiu.mi.util;

import java.util.Map.Entry;

import com.google.gson.JsonElement;

import cat.jiu.core.util.JiuUtils;
import cat.jiu.mi.MI;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.CraftingHelper.ShapedPrimer;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.registries.GameData;

public class MIRecipe {
	public static void register() {
		if(!RegisterCustomCapability.unregister.isEmpty()) {
			for(Entry<Integer, JsonElement> cap : RegisterCustomCapability.unregister.entrySet()) {
				MI.getCapability(cap.getKey()).setCraftStack(Loader.isModLoaded("jiucore") ? JiuUtils.item.toStack(cap.getValue()) : JsonToStackUtil.toStack(cap.getValue()));
			}
		}
		
		for(int i = 0; i < MI.capabilitySize(); i++) {
			addCapabilityRecipe(new ItemStack(MI.capabilitys, 1, i), MI.getCapability(i).getCraftStack());
		}
		
		addShapedRecipes(new ItemStack(MI.mirage_stabilizer),
				new ItemStack(Items.DIAMOND), new ItemStack(Items.FISH), new ItemStack(Items.DIAMOND),
				new ItemStack(Items.QUARTZ), new ItemStack(Items.ENDER_EYE), new ItemStack(Items.MAGMA_CREAM),
				new ItemStack(Items.DIAMOND), new ItemStack(Items.BLAZE_POWDER), new ItemStack(Items.DIAMOND)
		);
		
		addShapedRecipe(new ItemStack(MI.face),
				"X",
				"Y",
				"X",
				'X', new ItemStack(Items.SHULKER_SHELL),
				'Y', new ItemStack(MI.mirage_stabilizer)
		);
		
		addShapedRecipes(new ItemStack(MI.mirage_connector),
				new ItemStack(Blocks.YELLOW_FLOWER), ItemStack.EMPTY, new ItemStack(Blocks.REDSTONE_TORCH),
				new ItemStack(Items.CHORUS_FRUIT_POPPED), new ItemStack(Items.ENDER_EYE), new ItemStack(Items.CHORUS_FRUIT_POPPED),
				new ItemStack(Items.CHORUS_FRUIT_POPPED), new ItemStack(Items.CHORUS_FRUIT_POPPED), new ItemStack(Items.CHORUS_FRUIT_POPPED)
		);
	}
	
	private static void addShapedRecipes(ItemStack output, ItemStack input1, ItemStack input2, ItemStack input3, ItemStack input4, ItemStack input5, ItemStack input6, ItemStack input7, ItemStack input8, ItemStack input9) {
		addShapedRecipe(output,
				"ABC",
				"DEF",
				"GHI",
				'A', input1,
				'B', input2,
				'C', input3,
				
				'D', input4,
				'E', input5,
				'F', input6,
				
				'G', input7,
				'H', input8,
				'I', input9
		);
	}
	
	private static final ItemStack paper = new ItemStack(Items.PAPER);
	private static void addCapabilityRecipe(ItemStack output, ItemStack input) {
		addShapedRecipe(output,
				" X ",
				"XYX",
				" X ",
				'Y', input,
				'X', paper);
    }
	
	private static void addShapedRecipe(ItemStack output, Object... input) {
		for(Object object : input) 
			if(object == null) return;
		ResourceLocation recipeName = getNameForRecipe(output);
		ShapedPrimer primer = CraftingHelper.parseShaped(input);
		ShapedRecipes recipe = new ShapedRecipes(output.getItem().getRegistryName().toString(), primer.width, primer.height, primer.input, output);
		GameData.register_impl(recipe.setRegistryName(recipeName));
	}
	
	private static ResourceLocation getNameForRecipe(ItemStack output) {
		ModContainer con = Loader.instance().activeModContainer();
		ResourceLocation t = new ResourceLocation(con.getModId(), output.getItem().getRegistryName().getResourcePath());
		ResourceLocation recipe = t;
		int i = 0;
		while (CraftingManager.REGISTRY.containsKey(recipe)) {
			i++;
			recipe = new ResourceLocation(MI.MODID, t.getResourcePath() + "." + i);
		}
		return recipe;
	}
}
