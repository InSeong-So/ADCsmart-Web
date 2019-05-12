package kr.openbase.adcsmart.web.json;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import kr.openbase.adcsmart.web.facade.dto.AdcPoolMemberDto;

public class AdcPoolMemberJsonAdapter implements
		JsonDeserializer<AdcPoolMemberDto>
{
	@Override
	public AdcPoolMemberDto deserialize(JsonElement json, Type type,
			JsonDeserializationContext context) throws JsonParseException
	{
		AdcPoolMemberDto member = new AdcPoolMemberDto();
		JsonObject jobj = json.getAsJsonObject();		
		if (jobj.get("index") != null)
		{
			if (jobj.get("index").getAsString().equals("null"))
			{
				member.setIndex(null);
			}
			else
			{
				member.setIndex(jobj.get("index").getAsString());				
			}
		}
		member.setIp(jobj.get("ip").getAsString());
		
		if (jobj.get("port") != null && !jobj.get("port").isJsonNull() && !jobj.get("port").getAsString().isEmpty())
		{
			member.setPort(jobj.get("port").getAsInt());
		}
		else
		{
			member.setPort(null);
		}
		
		if(jobj.get("alteonNodeId")!=null || jobj.get("id") !=null)
		{
			member.setIsEnabled(jobj.get("isEnabled").getAsBoolean());
		}
		else
		{
			member.setMemStatus(jobj.get("memStatus").getAsInt());
		}
		
		if (jobj.get("alteonNodeId") != null && !jobj.get("alteonNodeId").isJsonNull() && !jobj.get("alteonNodeId").getAsString().isEmpty())
		{
			if(!(jobj.get("alteonNodeId").toString().contains("undefined")))
			{
					member.setAlteonNodeId(jobj.get("alteonNodeId").getAsString());		
			}
			else
			{
				member.setAlteonNodeId(null);
			}
		}
		else
		{
			member.setAlteonNodeId(null);
		}
		
		/* Member id 가 0 보다 크다면 기존 장비에 추가되있던 Member 이기 때문에 id를 그대로 넘겨주지만
		 * ADCSmart 에서 추가한 Member 의 id 는 -1로 id를 null로 보낸다.
		 */
		if (jobj.get("id") != null)
		{	
			if (jobj.get("id").getAsInt() > 0)
			{
				member.setId(jobj.get("id").getAsInt());
			}
			else //if (jobj.get("id").getAsInt() <= 0)
			{
				member.setId(null);
			}
		}		
		return member;
	}
}