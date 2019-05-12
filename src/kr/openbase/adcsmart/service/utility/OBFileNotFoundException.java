package kr.openbase.adcsmart.service.utility;

public class OBFileNotFoundException extends Exception
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8510536912426050778L;
	private String str;

	public OBFileNotFoundException(String str)
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
