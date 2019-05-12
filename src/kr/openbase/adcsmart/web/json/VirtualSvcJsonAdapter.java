package kr.openbase.adcsmart.web.json;

import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import kr.openbase.adcsmart.web.facade.dto.AdcPoolDto;
import kr.openbase.adcsmart.web.facade.dto.VirtualSvcDto;

public class VirtualSvcJsonAdapter implements JsonDeserializer<VirtualSvcDto> {

	@Override
	public VirtualSvcDto deserialize(JsonElement json, Type type, JsonDeserializationContext context)
			throws JsonParseException {
		VirtualSvcDto svc = new VirtualSvcDto();
		JsonObject jobj = json.getAsJsonObject();
		svc.setSvcPort(jobj.get("svcPort").getAsInt());
		svc.setRealPort(jobj.get("realPort").getAsInt());
		
		Gson gson = new GsonBuilder().registerTypeAdapter(AdcPoolDto.class, new AdcPoolJsonAdapter()).create();
		svc.setPool(gson.fromJson(jobj.get("pool"), AdcPoolDto.class));
		return svc;
	}

}
