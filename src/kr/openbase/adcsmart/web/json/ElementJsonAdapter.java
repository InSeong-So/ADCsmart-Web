package kr.openbase.adcsmart.web.json;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import kr.openbase.adcsmart.service.dto.OBDtoElement;

public class ElementJsonAdapter implements JsonDeserializer<OBDtoElement> {

	@Override
	public OBDtoElement deserialize(JsonElement json, Type type, JsonDeserializationContext context)
			throws JsonParseException {
		OBDtoElement ele = new OBDtoElement();
		JsonObject jobj = json.getAsJsonObject();
		ele.setIndex(jobj.get("index").getAsInt());
		ele.setState(jobj.get("state").getAsInt());
		
		return ele;
	}

}
