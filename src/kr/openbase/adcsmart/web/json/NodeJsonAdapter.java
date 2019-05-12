package kr.openbase.adcsmart.web.json;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import kr.openbase.adcsmart.service.dto.OBDtoAdcNodeF5;

public class NodeJsonAdapter implements JsonDeserializer<OBDtoAdcNodeF5> 
{
	@Override
	public OBDtoAdcNodeF5 deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException 
	{
	    OBDtoAdcNodeF5 ele = new OBDtoAdcNodeF5();
		JsonObject jobj = json.getAsJsonObject();
		ele.setIndex(jobj.get("index").getAsString());
		ele.setIpAddress(jobj.get("ipaddress").getAsString());
		ele.setName(jobj.get("name").getAsString());
		ele.setRatio(jobj.get("ratio").getAsInt());
		ele.setState(jobj.get("state").getAsInt());
		ele.setGroupIndex(jobj.get("groupIndex").getAsInt());
		ele.setAlteonID(jobj.get("alteonID").getAsString());
		
		return ele;
	}
}
