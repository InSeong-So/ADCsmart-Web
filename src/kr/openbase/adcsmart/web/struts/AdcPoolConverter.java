package kr.openbase.adcsmart.web.struts;

import java.util.Map;

import org.apache.struts2.util.StrutsTypeConverter;

import com.google.gson.Gson;

import kr.openbase.adcsmart.web.facade.dto.AdcPoolDto;

@SuppressWarnings("rawtypes")
public class AdcPoolConverter extends StrutsTypeConverter {
	public Object convertFromString(Map context, String[] values, Class toClass) {
		Gson gson = new Gson();
		AdcPoolDto dto = gson.fromJson(values[0], AdcPoolDto.class);
		return dto;
	}

	public String convertToString(Map context, Object o) {
		Gson gson = new Gson();
		return gson.toJson(o);
	}
}
