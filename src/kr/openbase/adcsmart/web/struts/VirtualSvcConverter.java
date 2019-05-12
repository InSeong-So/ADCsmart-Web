package kr.openbase.adcsmart.web.struts;

import java.util.Map;

import org.apache.struts2.util.StrutsTypeConverter;

import com.google.gson.Gson;

import kr.openbase.adcsmart.web.facade.dto.VirtualSvcDto;

@SuppressWarnings("rawtypes")
public class VirtualSvcConverter extends StrutsTypeConverter {
	public Object convertFromString(Map context, String[] values, Class toClass) {
		Gson gson = new Gson();
		VirtualSvcDto dto = gson.fromJson(values[0], VirtualSvcDto.class);
		return dto;
	}

	public String convertToString(Map context, Object o) {
		Gson gson = new Gson();
		return gson.toJson(o);
	}
}
