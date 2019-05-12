package kr.openbase.adcsmart.service.utility;

import java.net.SocketException;

public class OBExceptionTimeout extends SocketException 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7235862924228011989L;
	private String str;
	/**
	 * 
	 */
	
	public OBExceptionTimeout(String str)
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
