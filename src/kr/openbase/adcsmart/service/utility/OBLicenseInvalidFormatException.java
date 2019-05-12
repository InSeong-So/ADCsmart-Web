package kr.openbase.adcsmart.service.utility;

public class OBLicenseInvalidFormatException extends Exception
{
	private String str;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5652688539033839317L;
	
	public OBLicenseInvalidFormatException(String str)
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
