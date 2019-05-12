package kr.openbase.adcsmart.web.struts;

import java.util.Map;

import org.apache.struts2.util.StrutsTypeConverter;

import com.google.gson.Gson;

import kr.openbase.adcsmart.web.facade.dto.AdcPoolMemberDto;

@SuppressWarnings("rawtypes")
public class AdcPoolMemberConverter extends StrutsTypeConverter {
	public Object convertFromString(Map context, String[] values, Class toClass) {
		Gson gson = new Gson();
		AdcPoolMemberDto dto = gson.fromJson(values[0], AdcPoolMemberDto.class);
		return dto;
	}

	public String convertToString(Map context, Object o) {
		Gson gson = new Gson();
		return gson.toJson(o);
	}
}
