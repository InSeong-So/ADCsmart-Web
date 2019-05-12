package kr.openbase.adcsmart.web.json;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import kr.openbase.adcsmart.service.dto.OBDtoAdcVSGroup;

public class AdcVSAdapter implements JsonDeserializer<OBDtoAdcVSGroup>
{   
    @Override
    public OBDtoAdcVSGroup deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException
    {
        OBDtoAdcVSGroup ele = new OBDtoAdcVSGroup();
        JsonObject jobj = json.getAsJsonObject();
        ele.setAdcIndex(jobj.get("adcIndex").getAsInt());
        ele.setVsIndex(jobj.get("vsIndex").getAsString());
        ele.setAdcType(jobj.get("adcType").getAsInt());
        ele.setVsIp(jobj.get("vsIp").getAsString());
        
        return ele;
    }
}
