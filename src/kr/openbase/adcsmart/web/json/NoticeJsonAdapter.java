package kr.openbase.adcsmart.web.json;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import kr.openbase.adcsmart.service.dto.OBDtoAdcVServerNotice;

public class NoticeJsonAdapter implements JsonDeserializer<OBDtoAdcVServerNotice>
{

    @Override
    public OBDtoAdcVServerNotice deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException
    {
        OBDtoAdcVServerNotice ele = new OBDtoAdcVServerNotice();
        JsonObject jobj = json.getAsJsonObject();
        ele.setIndex(jobj.get("index").getAsString());
        ele.setVsStatus(jobj.get("vsStatus").getAsInt());
        ele.setVsName(jobj.get("vsName").getAsString());
        ele.setVirtualIp(jobj.get("virtualIp").getAsString());
        ele.setServicePort(jobj.get("servicePort").getAsString());
        ele.setServicePoolIndex(jobj.get("servicePoolIndex").getAsString());
        ele.setServicePoolName(jobj.get("servicePoolName").getAsString());
        ele.setNoticePoolIndex(jobj.get("noticePoolIndex").getAsString());
        ele.setNoticePoolName(jobj.get("noticePoolName").getAsString());
        if (jobj.get("alteonID") != null)
        {
            ele.setAlteonID(jobj.get("alteonID").getAsString());
            ele.setServicePoolAlteonID(jobj.get("servicePoolAlteonID").getAsString());
        }
        ele.setIsNotice(jobj.get("isNotice").getAsBoolean());
        
        return ele;
    }

}
