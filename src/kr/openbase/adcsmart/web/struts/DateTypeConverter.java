package kr.openbase.adcsmart.web.struts;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.struts2.util.StrutsTypeConverter;

public class DateTypeConverter extends StrutsTypeConverter {
	public static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	@SuppressWarnings("rawtypes")
	@Override
	public Object convertFromString(Map context, String[] values, Class toClass) {
		// TODO Auto-generated method stub
		if (toClass == Date.class) {
            try {
                return sdf.parse(values[0]);
             } catch (ParseException e) {
                throw new IllegalArgumentException(e);
             }
        }

		return null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public String convertToString(Map context, Object o) {
		// TODO Auto-generated method stub
		Date date = (Date) o;
        return sdf.format(date);

	}
}
