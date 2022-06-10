package cat.jiu.mi.util.client;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

import com.google.common.collect.Sets;

import cat.jiu.mi.MI;
import cat.jiu.mi.util.CapabilityType;

import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.client.resources.data.MetadataSerializer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.FMLFileResourcePack;
import net.minecraftforge.fml.client.FMLFolderResourcePack;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;

public class CapabilityModelLoad implements IResourcePack {
	private final IResourcePack modpack;
	public CapabilityModelLoad() {
		File file = Loader.instance().getIndexedModList().get(MI.MODID).getSource();
		ModContainer mod = Loader.instance().getIndexedModList().get(MI.MODID);
		
		if(file.isDirectory()) {
			// for dev
			this.modpack = new FMLFolderResourcePack(mod);
		}else {
			this.modpack = new FMLFileResourcePack(mod);
		}
	}
	
	@Override
	public InputStream getInputStream(ResourceLocation location) throws IOException {
		if(this.modpack.resourceExists(location)) return this.modpack.getInputStream(location);
		
		String resource = String.format("%s/%s/%s", "assets", location.getResourceDomain(), location.getResourcePath());
		String name = this.getName(resource);
		
		if(MI.hasCapability(name)) {
			CapabilityType type = MI.getCapability(name);
			
			if(type == null || !type.isCustom || type.textures == null) return this.modpack.getInputStream(location);
			return new CapabilityModel(MI.getCapability(name).textures).toSteam();
		}
		
		return this.modpack.getInputStream(location);
	}
	
	@Override
	public boolean resourceExists(ResourceLocation location) {
		String resource = String.format("%s/%s/%s", "assets", location.getResourceDomain(), location.getResourcePath());
		if(resource.startsWith("assets/mi/models/item/capabilitys") && !resource.endsWith(".json.mcmeta")) {
			if(MI.hasCapability(this.getName(resource))) return true;
		}
		
		return this.modpack.resourceExists(location);
	}
	
	protected String getName(String resource) {
		String[] locations = resource.split("/");
		return locations[locations.length-1].split("\\.")[0];
	}

	@Override
	public Set<String> getResourceDomains() {
		return Sets.newHashSet("mi");
	}

	@Override
	public <T extends IMetadataSection> T getPackMetadata(MetadataSerializer metadataSerializer, String metadataSectionName) throws IOException {
		return this.modpack.getPackMetadata(metadataSerializer, metadataSectionName);
	}

	@Override
	public BufferedImage getPackImage() throws IOException {
		return this.modpack.getPackImage();
	}

	@Override
	public String getPackName() {
		return "MirageInterface CapabilityModelLoad";
	}
}
