/*
 * ping a host
 */
package kr.openbase.adcsmart.service.utility;

import java.net.*;

public class OBPingHost
{
	/**
	 * 입력된 호스트에 네트워크 접근이 되는지 확인한다.
	 * 
	 * @param ip
	 *			-- ip 주소. 예: 192.168.1.1
	 *
	 * @return true: 네트워크 접근 가능. false: 네트워크에 접근 불가능.
	 * 
	 */ 	
	public static boolean isReachable(String ip)
	{
		try
		{
			boolean reachable = false;
			InetAddress address = InetAddress.getByName(ip);
			reachable = address.isReachable(5000);
			return reachable;
		}
		catch(UnknownHostException e)
		{
			return false;
		}
		catch(Exception e)
		{
			return false;
		}
	}
}
