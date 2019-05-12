package kr.openbase.adcsmart.service.utility;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.net.util.SubnetUtils;

public class OBNetwork {
//	public static void main(String args[])
//	{
//		try
//		{
//			System.out.println(OBNetwork.isInvolvedIPAddress("171.172.", "171.172.2.3"));
//		} catch (Exception e)
//		{
//		}
//	}

	public static boolean isInvolvedIPAddress(String src, String target) {
		try {
			boolean retVal = false;
			String elements[] = src.split("/");// ip/netmask 형태로 되어 있는 경우.
			if (elements.length == 1) {
				if (src.endsWith(".")) {// 127. 과 같은 경우임.
					if (target.length() < src.length())
						return false;

					String dest = target.substring(0, src.length());
					if (src.equals(dest))
						return true;
					else
						return false;
				}

				if (src.equals(target))
					return true;
				else
					return false;
			}
			if (OBUtility.isInteger(elements[1])) {// ip/cidr 형태.
				SubnetUtils utils = new SubnetUtils(src);
				retVal = utils.getInfo().isInRange(target);
				return retVal;
			}
			// ip/netmask 형태로 되어 있는 경우.
			SubnetUtils utils = new SubnetUtils(elements[0], elements[1]);
			retVal = utils.getInfo().isInRange(target);
			return retVal;
		} catch (Exception e) {
			return false;
		}
	}

//	public static void main(String args[]) throws SocketException 
//	{
//		try
//		{
//			System.out.println(OBNetwork.portIsOpen("192.168.100.13", 161, 200));
//		
//			byte [] bytes = new byte[4];
//			bytes[3] = (byte) 192;
//			bytes[2] = (byte) 168;
//			bytes[1] = (byte) 100;
//			bytes[0] = (byte) 13;
//			InetAddress ip = InetAddress.getByAddress(bytes);
////			InetAddress ip = InetAddress.getByName("192.168.100.13");
//			System.out.println(OBNetwork.scanUDP(ip, 161));
//		}
//		catch( Exception e ) 
//		{ 
//		}
//    }	
//	
//	public static String scanUDP(InetAddress IP, int port)
//	{
//	    try{
//	        byte [] bytes = new byte[128];
//	        DatagramSocket ds = new DatagramSocket();
//	        DatagramPacket dp = new DatagramPacket(bytes, bytes.length, IP, port);
//	        ds.setSoTimeout(1000);
//	        ds.send(dp);
//	        dp = new DatagramPacket(bytes, bytes.length);
//	        ds.receive(dp);
//	        ds.close();
//	    }
//	    catch(InterruptedIOException e){
//	        return "CLOSED";
//	    }
//	    catch(IOException e){
//	        return "CLOSED";
//	    }
//	    return "OPEN";
//	}

	public static boolean portTcpIsOpen(String ip, int port, int timeout) {
		boolean result = false;
		for (int i = 0; i < 3; i++) {
			try {
				Socket socket = new Socket();
				socket.connect(new InetSocketAddress(ip, port), timeout);
				socket.close();
				result = true;
				break;
			} catch (Exception ex) {
				result = false;
			}
			OBDateTime.Sleep(1000);
		}
		return result;

	}

	public static boolean icmpIsOpen(String ip, int port, int timeout) {
		try {
			boolean result = InetAddress.getByName(ip).isReachable(timeout);
			return result;
		} catch (Exception ex) {
			return false;
		}
	}

//	public static void main(String args[])
//	{					//이곳에서 먼저 실행이 된다. Main메소드.
//		long lIpNo = getIpConvertToLong("10.10.115.102");		//1.IP를 Long Type으로 변경
//		long mask = getIpConvertToLong("255.255.255.0");		//1.IP를 Long Type으로 변경
//		long lIpNo2 = getIpConvertToLong("10.10.116.152");		//1.IP를 Long Type으로 변경
//		System.out.println(String.format("addr: %x", lIpNo));
//		System.out.println(String.format("mask: %x, %x, %x", mask, mask&lIpNo, mask&lIpNo2));
//		System.out.println("addr:" + getIpConvertToString( lIpNo ));				//2.Long Type을 IP로 변경
//	}

	public static long getIpConvertToLong(String sIpAddress) {
		String sIpNo = sIpAddress;

		// "."으로 잘라서 리스트에 담는다.
		StringTokenizer st = new StringTokenizer(sIpNo, ".");
		List<Object> lIp = new ArrayList<Object>();

		while (st.hasMoreElements()) {
			lIp.add(st.nextElement());
		}

		int iNo = 0; // 리스트의 값을 담을 변수
		final int iref = lIp.size() - 1; //
		int iNcount = 0; // 256의 제곱변수
		long lResult = 0; // 리스트의 값과 256의 제곱근의 합을 담는 변수

		for (int i = 0; i < lIp.size(); i++) {
			iNcount = iref - i; // 제곱할 수
			iNo = Integer.parseInt(lIp.get(i).toString()); // 리스트의 값을 담는다.
			lResult += iNo * ((long) (Math.pow(256, iNcount))); //
		}

//		System.out.println( "Result Long Type : " +lResult);

		return lResult;
	}

	public static String getIpConvertToString(long lIpAddress) {
		long lIpNo = lIpAddress;
		StringBuffer sbIp = new StringBuffer();

		List<Integer> lIpResult = new ArrayList<Integer>();
		int iN1;
		int iN2;
		int iN3;
//		int iN4;
		int iResult;

		iN1 = (int) (lIpNo / 256); // 000.000.000.XXX : XXX를 구함
		iResult = (int) (lIpNo % 256);
		lIpResult.add(iResult);

		iN2 = iN1 / 256; // 000.000.XXX.000 : XXX를 구함
		iResult = iN1 % 256;
		lIpResult.add(iResult);

		iN3 = iN2 / 256; // 000.XXX.000.000 : XXX를 구함
		iResult = iN2 % 256;
		lIpResult.add(iResult);

//		iN4 = iN3/256;					// XXX.000.000.000 : XXX를 구함
		iResult = iN3 % 256;
		lIpResult.add(iResult);

		for (int k = lIpResult.size() - 1; k >= 0; k--) {
			sbIp.append(lIpResult.get(k));
			if (k != 0)
				sbIp.append(".");
		}

		return sbIp.toString();

	}

//	/**
//	 * 로컬 시스템의 IP 정보를 추출하여 제공한다.
//	 * 
//	 * @param interfaceName
//	 * 			-- NIC 인터페이스 이름.
//	 * @return OBDtoNetwork. 
//	 */
//	public static OBDtoNetwork getLocalNetworkInfo(String interfaceName)
//	{
//		OBDtoNetwork network = new OBDtoNetwork();
//		
//		try
//		{
//			String data = OBStringFile.toString("/etc/sysconfig/network-scripts/ifcfg-" + interfaceName);
//			String[] lines = data.split("\n");
//			for(int i=0;i<lines.length;i++)
//			{
//				String one=lines[i];
//				String[] parse=one.split("=");
//				if(parse.length!=2)
//					continue;
//				if(parse[0].compareToIgnoreCase("IPADDR")==0)
//					network.setIpAddress(parse[1]);
//				else if(parse[0].compareToIgnoreCase("NETMASK")==0)
//					network.setNetmask(parse[1]);
//				else if(parse[0].compareToIgnoreCase("GATEWAY")==0)
//					network.setGateway(parse[1]);
//				else if(parse[0].compareToIgnoreCase("DNS1")==0)
//					network.setDns(parse[1]);
//			}
//		}
//		catch(IOException e)
//		{
//			e.printStackTrace();
//		}
//		return network;
//	}

//	public static ArrayList<OBDtoNetworkExt> getLocalNetworkInfoList() throws OBException
//	{
//		ArrayList<OBDtoNetworkExt> result = new ArrayList<OBDtoNetworkExt>();
//		Enumeration<NetworkInterface> nienum;
//		try
//		{
//			nienum = NetworkInterface.getNetworkInterfaces();
//			while (nienum.hasMoreElements()) 
//			{  
//				NetworkInterface ni = nienum.nextElement();  
//	            byte[] hardwareAddress = ni.getHardwareAddress();  
//	            if(hardwareAddress!=null)
//	            {
//	            	OBDtoNetworkExt net = new OBDtoNetworkExt();
//	            	List<InterfaceAddress> ifList = ni.getInterfaceAddresses();
//	            	for(InterfaceAddress ifAddr:ifList)
//	            	{
//	            		byte[] addr = ifAddr.getAddress().getAddress();
//	            		if(addr.length==4)
//	            		{
//	            			net.setIfName(ni.getDisplayName());
//	            			net.setIpAddr(addr);
//	            			net.setIpAddress(ifAddr.getAddress().getHostAddress());
//	            			break;
//	            		}
//	            	}
//	            	if(net.getIfName()==null)
//	            		continue;
//	            	
//	            	net.setMacAddr(hardwareAddress);
//	    			String macAddr = "";
//	 			   	for (int i = 0; i < hardwareAddress.length; i++) 
//	 			   	{
//	 			   		macAddr += String.format("%02X%s", (int)hardwareAddress[i]&0xff, (i < hardwareAddress.length - 1) ? ":" : "");
//	 			   	}	   
//	 			   	net.setMacAddress(macAddr);
//	 			   	
//	 			   	System.out.println(net);
//	 			   result.add(net);
//	            }
//			}
//		}
//		catch(Exception e)
//		{
//			throw new OBException(e.getMessage());
//		}  
//	
//		return result;
//	}

	public static String getFirstIPAddress() throws OBException {
		try {
			Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
			while (en.hasMoreElements()) {
				NetworkInterface i = (NetworkInterface) en.nextElement();
				for (Enumeration<InetAddress> en2 = i.getInetAddresses(); en2.hasMoreElements();) {
					InetAddress addr = (InetAddress) en2.nextElement();
					if (!addr.isLoopbackAddress()) {
						if (addr instanceof Inet4Address) {
							String result = addr.toString();
							result = result.replace("/", "");
							return result;
						}
					}
				}
			}
			return null;
		} catch (Exception e) {
			throw new OBException(e.getMessage());
		}
	}

	public static ArrayList<OBDtoNetwork> getLocalNetworkInfoList() throws OBException {
		try {
			ArrayList<OBDtoNetwork> result = new ArrayList<OBDtoNetwork>();
			Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
			while (en.hasMoreElements()) {
				NetworkInterface i = (NetworkInterface) en.nextElement();

				i.getInterfaceAddresses().get(0).getNetworkPrefixLength();
				for (int ii = 0; ii < i.getInterfaceAddresses().size(); ii++) {
					InterfaceAddress addr = i.getInterfaceAddresses().get(ii);
//		        	System.out.println(addr);
					if (!addr.getAddress().isLoopbackAddress()) {
						if (addr.getAddress() instanceof Inet4Address) {
							String ipaddr = addr.getAddress().toString();
							ipaddr = ipaddr.replace("/", "");
							String cidr = convertPrefixToCIDR(addr.getNetworkPrefixLength());
							OBDtoNetwork network = new OBDtoNetwork();
							network.setIpAddress(ipaddr);
							network.setNetmask(cidr);
							result.add(network);
						}
					}
				}

//		        for (Enumeration<InetAddress> en2 = i.getInetAddresses(); en2.hasMoreElements();) 
//		        {
//		            InetAddress addr = (InetAddress) en2.nextElement();
//
//		            if (!addr.isLoopbackAddress()) 
//		            {
//		                if (addr instanceof Inet4Address) 
//		                {
//		                	String ipaddr = addr.toString();
//		                	ipaddr = ipaddr.replace("/", "");
//		                	result.add(ipaddr);
//		                }
//		            }
//		        }
			}
			return result;
		} catch (Exception e) {
			throw new OBException(e.getMessage());
		}
	}

	private static String localExceptionHost = "192.168.0.2";

	public static ArrayList<String> getAllIPAddress() throws OBException {
		try {
			try {
				String propertyValue = OBParser.propertyToString(OBDefine.PROPERTY_FILE_NAME,
						OBDefine.PROPERTY_KEY_TELNET_CONNECT_TIMEOUT);
				if (propertyValue != null && !propertyValue.isEmpty())
					localExceptionHost = propertyValue;
			} catch (Exception e) {
			}

			ArrayList<String> result = new ArrayList<String>();
			Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
			while (en.hasMoreElements()) {
				NetworkInterface i = (NetworkInterface) en.nextElement();

				for (Enumeration<InetAddress> en2 = i.getInetAddresses(); en2.hasMoreElements();) {
					InetAddress addr = (InetAddress) en2.nextElement();

					if (!addr.isLoopbackAddress()) {
						if (addr instanceof Inet4Address) {
							String ipaddr = addr.toString();
							ipaddr = ipaddr.replace("/", "");

							if (ipaddr.equals(localExceptionHost))
								continue;
							result.add(ipaddr);
						}
					}
				}
			}
			return result;
		} catch (Exception e) {
			throw new OBException(e.getMessage());
		}
	}

//	public static void main(String args[]) throws SocketException 
//	{
//		try{
//
//		    Process result = Runtime.getRuntime().exec("netstat -rn");
//
//		    BufferedReader output = new BufferedReader
//		        (new InputStreamReader(result.getInputStream()));
//
//		    String line = output.readLine();
//		    while(line != null){
//		        if ( line.startsWith("default") == true )
//		            break;              
//		        line = output.readLine();
//		    }
//
//
//		    StringTokenizer st = new StringTokenizer( line );
//		    st.nextToken();
//		    String gateway = st.nextToken();
//
//		    st.nextToken();
//		    st.nextToken();
//		    st.nextToken();
//
//		    String adapter = st.nextToken();
//		    System.out.print(gateway);
//		    System.out.print(adapter);
//
//		}catch( Exception e ) { 
//		    System.out.println( e.toString() );
//		    String gateway = new String();
//		    String adapter = new String();
//		}
////		try
////		{
////			System.out.println(convertPrefixToCIDR(24));
////			System.out.println(getAllIPAddress());
////		}
////		catch(Exception e)
////		{
////			e.printStackTrace();
////		}
//    }

	private static String convertPrefixToCIDR(int prefix) {
		int mask = 0xffffffff << (32 - prefix);
		int value = mask;
		byte[] bytes = new byte[] { (byte) (value >>> 24), (byte) (value >> 16 & 0xff), (byte) (value >> 8 & 0xff),
				(byte) (value & 0xff) };
		InetAddress netAddr;
		try {
			netAddr = InetAddress.getByAddress(bytes);
			return netAddr.getHostAddress();
		} catch (Exception e) {
			return "0.0.0.0";
		}
	}

//	public static boolean delDefaultGateway(String ipAddress) throws OBException
//	{
//		try
//		{
//			Runtime.getRuntime().exec(String.format("route del default gw %s eth0", ipAddress));
//			return true;
//		}
//		catch(Exception e)
//		{
//			throw new OBException(e.getMessage());
//		}
//	}

//	public static void main(String args[]) throws SocketException 
//	{
//		try
//		{
//			System.out.println(new OBNetwork().getDefaultGateway());
//		}
//		catch( Exception e ) 
//		{ 
//		}
//    }	

//	public String getDefaultGateway() throws OBException
//	{
//		try
//		{
//			Process result = Runtime.getRuntime().exec("netstat -rn");
//
//		    BufferedReader output = new BufferedReader
//		        (new InputStreamReader(result.getInputStream()));
//
//		    String line = output.readLine();
//		    while(line != null)
//		    {
//		        if ( line.startsWith("0.0.0.0") == true )
//		            break;              
//		        line = output.readLine();
//		    }
//
//		    StringTokenizer st = new StringTokenizer( line );
//		    st.nextToken();
//		    String gateway = st.nextToken();
//
//		    return gateway;
//		}
//		catch(Exception e)
//		{
//			throw new OBException(e.getMessage());
//		}
//	}

	public static void setDefaultGateway(String ipAddress) throws OBException {
		try {
			Runtime.getRuntime().exec(String.format("/opt/obshell/bin/obroute set default %s", ipAddress));
			OBDateTime.Sleep(100);
			Runtime.getRuntime().exec(String.format("/opt/obshell/bin/obroute apply"));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, e.getMessage());
		}
	}

	public ArrayList<String> getMacAddress() throws OBException {
		Enumeration<NetworkInterface> nienum;
		try {
			ArrayList<String> result = new ArrayList<String>();
			nienum = NetworkInterface.getNetworkInterfaces();
			while (nienum.hasMoreElements()) {
				NetworkInterface ni = nienum.nextElement();
				byte[] hwa = ni.getHardwareAddress();
				if (hwa != null) {
					String mac = String.format("%02X:%02X:%02X:%02X:%02X:%02X", hwa[0], hwa[1], hwa[2], hwa[3], hwa[4],
							hwa[5]);
					result.add(mac);
				}
			}
			return result;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, e.getMessage());
		}
	}

//	public static void main(String args[])
//	{					//이곳에서 먼저 실행이 된다. Main메소드.
//		try
//		{
//			System.out.println(OBNetwork.getLocalIPAddress());
//		}
//		catch(OBException e)
//		{
//			e.printStackTrace();
//		}
//	}	
	/**
	 * 로컬 시스템의 호스트 이름을 추출하여 제공한다.
	 * 
	 * @return 호스트 이름(String).
	 */
	public static String getHostName() throws OBException {
		try {
			InetAddress addr = InetAddress.getLocalHost();
			return addr.getHostName();
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, e.getMessage());
		}
	}

	public static void setHostName(String hostname) throws OBException {
		File f = new File("/proc/sys/kernel/hostname");
		try {
			FileWriter fileWriter = new FileWriter(f, true);
			BufferedWriter out = new BufferedWriter(fileWriter);
			out.write(hostname);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void setIPAddress(String ipAddress, String netmask) throws OBException {
		try {
			Runtime.getRuntime().exec(String.format("/opt/obshell/bin/obsysip set eth1 %s %s", ipAddress, netmask));
			OBDateTime.Sleep(100);
			Runtime.getRuntime().exec(String.format("/opt/obshell/bin/obsysip apply"));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, e.getMessage());
		}
	}

	public String runSystemCommand(String cmnd) {
		try {
			Process p = Runtime.getRuntime().exec(cmnd);
			p.waitFor();
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String result = "";
			String line = reader.readLine();
			while (line != null) {
//				System.out.println(line); 
				line = reader.readLine();
				result += line;
			}
			return result;
		} catch (Exception e) {
			OBSystemLog.error(OBDefine.LOGFILE_SYSLOG,
					String.format("Failed to run system commnd: %s, error: %s", cmnd, e.getMessage()));
			return "";
		}
	}

	// IP 체크 함수
	public static boolean checkIp(String text) {
		Pattern p = Pattern.compile(
				"^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$");
		Matcher m = p.matcher(text);
		return m.find();
	}

	// PORT 체크 함수
	public static boolean checkPort(String s) {
		try {
			int port = Integer.parseInt(s);
			if (port >= 0 && port <= 63737) {
				return true;
			} else
				return false;

		} catch (NumberFormatException e) {
			return false;
		}
	}
}
