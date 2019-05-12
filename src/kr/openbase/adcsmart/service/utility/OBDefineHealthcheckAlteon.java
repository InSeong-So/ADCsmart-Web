package kr.openbase.adcsmart.service.utility;

import java.util.LinkedHashMap;

public class OBDefineHealthcheckAlteon
{
	public static final int NOT_ALLOWED = -1;
	public static final String NOT_ALLOWED_ID = "Not Allowed";
	
	public static final String V2_START_VERSION = "29";
	
	public static class OLD  //예전 체계에서 쓰던 adcsmart healthcheck값, 설정에서는 안 쓰고 옛날 이력을 읽을 때만 쓴다.
	{
		public static final int TCP = 1;
		public static final int LINK = 8;
		public static final int ARP = 7;
		
		public static final LinkedHashMap <Integer, String> TYPE;
		static
		{
			TYPE = new LinkedHashMap <Integer, String>();
			TYPE.put(NOT_ALLOWED, NOT_ALLOWED_ID);
			TYPE.put(OLD.TCP, "tcp"); 
			TYPE.put(OLD.LINK, "link");
			TYPE.put(OLD.ARP, "arp");
		}
	}
	public static class V1  //v28까지, group의 healthcheck layer snmp에 근거해 정의
	{
		public static final int TCP = 2;
		public static final int LINK = 28;
		public static final int ARP = 33;
		
		public static final LinkedHashMap <Integer, String> TYPE;
		static
		{
			TYPE = new LinkedHashMap <Integer, String>();
			TYPE.put(NOT_ALLOWED, NOT_ALLOWED_ID);
			TYPE.put(V1.TCP, "tcp"); 
			TYPE.put(V1.LINK, "link");
			TYPE.put(V1.ARP, "arp");
		}
	}
	public static class V2  //v29부터 쓰는 adv hc(healthcheck) snmp에 근거해 정의
	{
		public static final int NOT_ALLOWED = -1;
		public static final int NONE = 0;
		public static final int ARP = 1;
		public static final int DHCP = 2;
		public static final int DNS = 3;
		public static final int FTP = 4;
		public static final int HTTP = 5;
		public static final int IMAP = 6;
		public static final int LDAP = 7;
		public static final int DB = 8;
		public static final int NNTP = 9;
		public static final int ICMP = 10;
		public static final int POP3 = 11;
		public static final int RADIUS = 12;
		public static final int RTSP = 13;
		public static final int SIP = 14;
		public static final int SMTP = 15;
		public static final int SNMP = 16;
		public static final int SSL = 17;
		public static final int TCP = 18;
		public static final int TFTP = 19;
		public static final int UDP = 20;
		public static final int WAP = 21;
		public static final int WTS = 22;
		public static final int SCRIPT = 23;
		public static final int LINK = 24;
		public static final int LOGEXP = 25;
		
		public static final LinkedHashMap <Integer, String> TYPE;
		static
		{
			TYPE = new LinkedHashMap <Integer, String>();
			TYPE.put(NOT_ALLOWED, NOT_ALLOWED_ID);
			TYPE.put(V2.NONE, "none"); 
			TYPE.put(V2.ARP, "arp");
			TYPE.put(V2.DHCP, "dhcp");
			TYPE.put(V2.DNS, "dns");
			TYPE.put(V2.FTP, "ftp");
			TYPE.put(V2.HTTP, "http");
			TYPE.put(V2.IMAP, "imap");
			TYPE.put(V2.LDAP, "ldap");
			TYPE.put(V2.DB, "db");
			TYPE.put(V2.NNTP, "nntp");
			TYPE.put(V2.ICMP, "icmp");
			TYPE.put(V2.POP3, "pop3");
			TYPE.put(V2.RADIUS, "radius");
			TYPE.put(V2.RTSP, "rtsp");
			TYPE.put(V2.SIP, "sip");
			TYPE.put(V2.SMTP, "smtp");
			TYPE.put(V2.SNMP, "snmp");
			TYPE.put(V2.SSL, "ssl");
			TYPE.put(V2.TCP, "tcp");
			TYPE.put(V2.TFTP, "tftp");
			TYPE.put(V2.UDP, "udp");
			TYPE.put(V2.WAP, "wap");
			TYPE.put(V2.WTS, "wts");
			TYPE.put(V2.SCRIPT, "script");
			TYPE.put(V2.LINK, "link");
			TYPE.put(V2.LOGEXP, "logexp");
		}
	}
	public String getHealthcheckIdAlteon(String swVersion, String id)
	{
		String temp;
		if(swVersion.compareTo(V2_START_VERSION)<0)
		{
			temp = (String)V1.TYPE.get(Integer.parseInt(id));
			if(temp==null)
			{
				temp = (String)V1.TYPE.get(NOT_ALLOWED);
			}
			return temp;
		}
		else
		{
			return id;
		}
	}
}
