package cat.jiu.mi.util;

import java.util.Collections;
import java.util.Set;

import cat.jiu.mi.MI;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.IModGuiFactory;
import net.minecraftforge.fml.client.config.GuiConfig;

public class ConfigGuiFactory implements IModGuiFactory {
	public void initialize(Minecraft mc) {}
	public boolean hasConfigGui() {return true;}
	public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {return Collections.emptySet();}

	@Override
	public GuiScreen createConfigGui(GuiScreen parent) {
		return new GuiConfig(parent, ConfigElement.from(MIConfigs.class).getChildElements(), MI.MODID, false, false, "MultipleCompressedStuffs");
	}
}