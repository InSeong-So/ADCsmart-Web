package kr.openbase.adcsmart.web.json;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import kr.openbase.adcsmart.service.dto.fault.OBDtoL2SearchOption;

public class l2SearchOptionJsonAdapter implements JsonDeserializer<OBDtoL2SearchOption>
{
	@Override
	public OBDtoL2SearchOption deserialize(JsonElement json, Type type, JsonDeserializationContext context)
			throws JsonParseException
	{
		OBDtoL2SearchOption svc = new OBDtoL2SearchOption();
		JsonObject jobj = json.getAsJsonObject();
		svc.setContent(jobj.get("content").getAsString());
		svc.setType(jobj.get("type").getAsInt());		
		return svc;
	}
}
