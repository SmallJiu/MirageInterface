package cat.jiu.mi.util.client;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map.Entry;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class CapabilityModel {
	protected final JsonElement texture;
	private final JsonObject json;
	public CapabilityModel(JsonElement texture) {
		this.texture = texture;
		this.json = new JsonObject();
	}

	protected void genData(JsonObject json) {
		json.addProperty("parent", "item/generated");
		JsonObject textures = new JsonObject();
		
		if(this.texture.isJsonPrimitive()) {
			textures.addProperty("layer0", this.texture.getAsString());
		}else if(this.texture.isJsonObject()) {
			for(Entry<String, JsonElement> texture : this.texture.getAsJsonObject().entrySet()) {
				textures.add(texture.getKey(), texture.getValue());
			}
		}else if(this.texture.isJsonArray()) {
			for(int i = 0; i < this.texture.getAsJsonArray().size(); i++) {
				JsonElement e = this.texture.getAsJsonArray().get(i);
				textures.add("layer"+i, e);
			}
		}
		json.add("textures", textures);
	}
	
	public final InputStream toSteam() {
		this.genData(this.json);
		return new ByteArrayInputStream(this.json.toString().getBytes(StandardCharsets.UTF_8));
	}
}
