package kr.openbase.adcsmart.web.json;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import kr.openbase.adcsmart.web.facade.dto.AdcALTEONHealthCheckDto;
import kr.openbase.adcsmart.web.facade.dto.AdcPoolDto;
import kr.openbase.adcsmart.web.facade.dto.AdcPoolMemberDto;

public class AdcPoolJsonAdapter implements JsonDeserializer<AdcPoolDto> {

	@Override
	public AdcPoolDto deserialize(JsonElement json, Type type, JsonDeserializationContext context)
			throws JsonParseException {
		AdcPoolDto adcPool = new AdcPoolDto();
		JsonObject jobj = json.getAsJsonObject();
		if (jobj.get("index") != null && !jobj.get("index").isJsonNull())
			adcPool.setIndex(jobj.get("index").getAsString());
		
		adcPool.setName(jobj.get("name").getAsString());
		adcPool.setLoadBalancingType(jobj.get("loadBalancingType").getAsString());
		if (jobj.get("healthCheckType") != null && !jobj.get("healthCheckType").isJsonNull())
			adcPool.setHealthCheckType(jobj.get("healthCheckType").getAsString());
	
		AdcALTEONHealthCheckDto healthCheck = new AdcALTEONHealthCheckDto();	
		if (jobj.get("adcALTEONHealthCheck") != null && !jobj.get("adcALTEONHealthCheck").isJsonNull())
		{
			if((jobj.get("adcALTEONHealthCheck").toString().contains("dbIndex")))
			{//임시 			
				String healthCheckToString = (jobj.get("adcALTEONHealthCheck").toString());
				String healthCheckReplace = healthCheckToString.replace("\"", "");
				String healthCheckParsing[] = healthCheckReplace.split(",");
				String adcAlteonhealthCheckId[] = healthCheckParsing[1].split(":");
				if(healthCheckParsing.length == 5)
				{
					String healthCheckParsing_[] = healthCheckParsing[4].split(":");
					String adcAlteonHealthCHeckExtra[] = healthCheckParsing_[1].split("}");
					healthCheck.setId(adcAlteonhealthCheckId[1]);
					healthCheck.setExtra(adcAlteonHealthCHeckExtra[0]);	
				}
				else
				{
					String healthCheckParsing_[] = healthCheckParsing[5].split(":");
					String adcAlteonHealthCHeckExtra[] = healthCheckParsing_[1].split("}");
					healthCheck.setId(adcAlteonhealthCheckId[1]);
					healthCheck.setExtra(adcAlteonHealthCHeckExtra[0]);	
				}
					
			}
			else
			{
				String healthCheckToString = (jobj.get("adcALTEONHealthCheck").toString());
				String healthCheckReplace = healthCheckToString.replace("\"", "");
				String adcAlteonhealthCheck[] = healthCheckReplace.split("extra:");
				healthCheck.setId(adcAlteonhealthCheck[0]);	
				healthCheck.setExtra(adcAlteonhealthCheck[1]);	
			}
			adcPool.setAdcALTEONHealthCheck(healthCheck);
		}
		if (jobj.get("alteonId") != null && !jobj.get("alteonId").isJsonNull() && !jobj.get("alteonId").getAsString().isEmpty())
			adcPool.setAlteonId(jobj.get("alteonId").getAsString());
		
		Gson gson = new GsonBuilder().registerTypeAdapter(AdcPoolMemberDto.class, new AdcPoolMemberJsonAdapter()).create();
		List<AdcPoolMemberDto> members = new ArrayList<AdcPoolMemberDto>();
		for (JsonElement e : jobj.get("members").getAsJsonArray())
			members.add(gson.fromJson(e, AdcPoolMemberDto.class));
			
		adcPool.setMembers(members);
		return adcPool;
	}

//	@Override
//	public JsonElement serialize(AdcPoolDto src, Type type, JsonSerializationContext context) {
//		JsonObject obj = new JsonObject();
//        obj.addProperty("index", src.getIndex());
//        obj.addProperty("name", src.getName());
//        obj.addProperty("loadBalancingType", src.getLoadBalancingType());
//        obj.addProperty("members", src.getMembers());
//
//        return obj;
//	}

}
