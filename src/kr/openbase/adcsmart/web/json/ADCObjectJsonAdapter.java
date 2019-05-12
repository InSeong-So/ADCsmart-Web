package kr.openbase.adcsmart.web.json;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import kr.openbase.adcsmart.service.dto.OBDtoADCObject;

public class ADCObjectJsonAdapter implements JsonDeserializer<OBDtoADCObject>
{
	public OBDtoADCObject deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException
	{
		OBDtoADCObject adcObject = new OBDtoADCObject();
		JsonObject jobj = json.getAsJsonObject();
		if (jobj.get("category") != null && !jobj.get("category").isJsonNull())
			adcObject.setCategory(jobj.get("category").getAsInt());
		
		if (jobj.get("index") != null && !jobj.get("index").isJsonNull())
			adcObject.setIndex(jobj.get("index").getAsInt());
		
		if (jobj.get("strIndex") != null && !jobj.get("strIndex").isJsonNull())
			adcObject.setStrIndex(jobj.get("strIndex").getAsString());
		
		if (jobj.get("name") != null && !jobj.get("name").isJsonNull())
			adcObject.setName(jobj.get("name").getAsString());
		
		if (jobj.get("vendor") != null && !jobj.get("vendor").isJsonNull())
			adcObject.setVendor(jobj.get("vendor").getAsInt());
		
		if (jobj.get("status") != null && !jobj.get("status").isJsonNull())
			adcObject.setStatus(jobj.get("status").getAsInt());
		
		if (jobj.get("desciption") != null && !jobj.get("desciption").isJsonNull())
			adcObject.setDesciption(jobj.get("desciption").getAsString());		
		return adcObject;
	}
}