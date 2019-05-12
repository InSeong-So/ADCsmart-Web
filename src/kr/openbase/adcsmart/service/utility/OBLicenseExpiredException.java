package kr.openbase.adcsmart.service.utility;

public class OBLicenseExpiredException extends Exception
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7543957796645002881L;
	private String str;
	
	public OBLicenseExpiredException(String str)
	{
		OBSystemLog.error3(OBDefine.LOGFILE_SYSTEM, String.format("%s", str));
		this.str=str;
	}
	
	@Override
	public String getMessage() 
	{
		return this.str;
	}	
}
