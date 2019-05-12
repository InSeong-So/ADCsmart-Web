package kr.openbase.adcsmart.service.utility;

import java.io.Serializable;

public class OBExceptionVersion extends Exception implements Serializable 
{
	private String str;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public OBExceptionVersion(String str)
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
