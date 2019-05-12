package kr.openbase.adcsmart.service.utility;

import java.net.SocketException;

public class OBExceptionLogin extends SocketException
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4003916628731964181L;
	private String str;
	/**
	 * 
	 */
	
	public OBExceptionLogin(String str)
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
