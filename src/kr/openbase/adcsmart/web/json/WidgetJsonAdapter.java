package kr.openbase.adcsmart.web.json;

import java.lang.reflect.Type;

import kr.openbase.adcsmart.service.dashboard.dto.OBDtoWidgetInfo;
import kr.openbase.adcsmart.service.dto.OBDtoADCObject;
import kr.openbase.adcsmart.web.json.ADCObjectJsonAdapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class WidgetJsonAdapter implements JsonDeserializer<OBDtoWidgetInfo> {

	@Override
	public OBDtoWidgetInfo deserialize(JsonElement json, Type type, JsonDeserializationContext context)
			throws JsonParseException {
		OBDtoWidgetInfo svc = new OBDtoWidgetInfo();
		JsonObject jobj = json.getAsJsonObject();
		svc.setIndex(jobj.get("index").getAsString());
		svc.setName(jobj.get("name").getAsString());
		svc.setType(jobj.get("type").getAsInt());
		
		Gson gson = new GsonBuilder().registerTypeAdapter(OBDtoADCObject.class, new ADCObjectJsonAdapter()).create();
		svc.setTargetObj(gson.fromJson(jobj.get("targetObj"), OBDtoADCObject.class));		
		svc.setWidthMin(jobj.get("widthMin").getAsInt());
		svc.setWidthMax(jobj.get("widthMax").getAsInt());
		svc.setHeightMin(jobj.get("heightMin").getAsInt());
		svc.setHeightMax(jobj.get("heightMax").getAsInt());
		svc.setWidth(jobj.get("width").getAsInt());
		svc.setHeight(jobj.get("height").getAsInt());
		svc.setxPosition(jobj.get("xPosition").getAsInt());
		svc.setyPosition(jobj.get("yPosition").getAsInt());
		svc.setMoreInfoIndex(jobj.get("moreInfoIndex").getAsInt());
		return svc;
	}
}