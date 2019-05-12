package kr.openbase.adcsmart.service.utility;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OBEmailValidator
{
	private Pattern pattern;
	private Matcher matcher;

	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	public OBEmailValidator()
	{
		pattern = Pattern.compile(EMAIL_PATTERN);
	}
	  
	/**
	 * 이메일의 유효성을 검사한다.
	 * 
	 * @param hex
	 *			-- 이메일 주소.
	 *
	 * @return true: 유효한 이메일 주소인 경우. false: 유효하지 않은 이메일인 경우.
	 * 
	 */ 	  
	  public boolean validate(final String hex)
	  {
		  matcher = pattern.matcher(hex);
		  return matcher.matches();
	  }
}
