package kr.openbase.adcsmart.service.utility;

import java.net.SocketException;

/**
 * 소켓 통신시 접속이 안되는 오류가 발생시.
 * 
 * @author bwpark
 *
 */
public class OBExceptionUnreachable extends SocketException
{
	private String str;
	/**
	 * 
	 */
	private static final long serialVersionUID = -2153213805998711372L;
	
	public OBExceptionUnreachable(String str)
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
