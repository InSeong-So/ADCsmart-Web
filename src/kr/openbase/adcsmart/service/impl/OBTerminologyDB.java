package kr.openbase.adcsmart.service.impl;

public class OBTerminologyDB
{
//	public static final String	TYPE_NOT_APPLY 			= "N/A";//
//	public static final String	TYPE_LICENSE_UNLIMIT 	= "영구";
//	public static final String	TYPE_LICENSE_TEMP 		= "임시";
//	public static final String	TYPE_LICENSE_DELETE 	= "삭제";
//	public static final String	TYPE_LICENSE_EXPIRE 	= "만료";
//	public static final String	TYPE_GENERAL_AVG 		= "평균";
//	public static final String	TYPE_GENERAL_NORMAL 	= "정상";
//	public static final String	TYPE_GENERAL_ABNORMAL 	= "비정상";
//	public static final String	TYPE_GENERAL_TOTAL 		= "전체"; 
//	public static final String	TYPE_GENERAL_USED		 = "사용";
//	public static final String	TYPE_GENERAL_NOTUSED 	= "사용안함";
//	public static final String	TYPE_GENERAL_OCCURTIME 	= "발생시간";  
//	public static final String	TYPE_GENERAL_CONTENT 	= "내용";  
//	public static final String	TYPE_GENERAL_DAY 		= "일";
//	public static final String	TYPE_GENERAL_COUNT		= "개";
//	public static final String	TYPE_GENERAL_PORT 		= "포트";
//	public static final String	TYPE_GENERAL_UNTAGPORT 	= "Untagged";
//	public static final String	TYPE_GENERAL_TAGPORT 	= "Tagged";
//	public static final String	TYPE_GENERAL_UNAVAILPORT= "Unavail";
//	public static final String	TYPE_GENERAL_DISCARDS 	= "Discards  "; 
//	public static final String	TYPE_GENERAL_UNDERSIZE 	= "Undersizes"; 
//	public static final String	TYPE_GENERAL_ERRORS 	= "Errors    "; 
//	public static final String	TYPE_GENERAL_OVERSIZE 	= "Oversizes "; 
//	public static final String	TYPE_GENERAL_COLLISIONS	= "Collisions"; 
//	public static final String	TYPE_GENERAL_CRCERRORS 	= "CRC Errors"; 
//	public static final String	TYPE_GENERAL_FRAGMENTS 	= "Fragments "; 
//	
//	public static final String	TYPE_GENERAL_VLAN 		= "VLAN";
//	public static final String	TYPE_GENERAL_STATUS 	= "상태"; 
//	public static final String	TYPE_GENERAL_ID 		= "ID";
//	public static final String	TYPE_GENERAL_NAME 		= "이름";
//	public static final String	TYPE_GENERAL_BLOCKING 	= "Blocking"; 
//	public static final String	TYPE_GENERAL_TRUNK 		= "Trunk"; 
//	public static final String	TYPE_GENERAL_TRUNK_NAME	= "Name"; 
//	public static final String	TYPE_GENERAL_TRUNK_ALG 	= "Algorithm"; 
//	public static final String	TYPE_GENERAL_TRUNK_PORT	= "Port"; 
//	public static final String	TYPE_GENERAL_IPADDRESS 	= "IP";
//	public static final String	TYPE_GENERAL_NETMASK 	= "Netmask"; 
//	public static final String	TYPE_GENERAL_MAC 		= "Mac"; 
//	public static final String	TYPE_GENERAL_BCAST 		= "Bcast"; 
//	public static final String	TYPE_GENERAL_UP 		= "Up";
//	public static final String	TYPE_GENERAL_DOWN 		= "Down";
//	public static final String	TYPE_GENERAL_DISALBED 	= "Disabled";
//	public static final String	TYPE_GENERAL_INOPERATIVE= "Inoperative";
//	public static final String	TYPE_GENERAL_MAX 		= "최대";
//	public static final String	TYPE_GENERAL_CURRENT 	= "현재";
//	public static final String	TYPE_GENERAL_USAGE 		= "사용율";
//	public static final String	TYPE_GENERAL_VSNAME		= "VS 이름";
//	public static final String	TYPE_GENERAL_DESTINATION= "Destination";
//	public static final String	TYPE_GENERAL_GATEWAY	= "Gateway";
//	public static final String	TYPE_GENERAL_INTERFACE	= "Interface";
//	// 보고서 관련 
//	public static final String	RPT_ETC_SYSLOGINFO 		= "Syslog 정보";
//	public static final String	RPT_ETC_NTPINFO 		= "NTP 정보";
//	public static final String	RPT_ETC_LOGINFO 		= "Log 정보";
//	public static final String	RPT_ETC_NTP_NAME 		= "NTP 서버";
//	public static final String	RPT_ETC_NTP_INTERVAL 	= "Interval";
//	public static final String	RPT_ETC_NTP_TIMEZONE 	= "Timezone";
//	public static final String	RPT_ETC_SYSLOG_SEVERITY = "Severity";
//	public static final String	RPT_ETC_SYSLOG_FACILITY = "Facility";
//	public static final String	RPT_BASIC_UPTIME 		= "Uptime";
//	public static final String	RPT_BASIC_LASTAPPLY 	= "Last Apply";
//	public static final String	RPT_BASIC_CPUINFO 		= "CPU 정보";
//	public static final String	RPT_BASIC_MEMINFO 		= "Memory 정보";
//	public static final String	RPT_BASIC_POWERINFO 	= "Power 정보";
//	public static final String	RPT_BASIC_FANINFO 		= "Fan 정보"; 
//	public static final String	RPT_BASIC_MPCPU 		= "MP"; 
//	public static final String	RPT_BASIC_SPCPU 		= "SP";
//	public static final String	RPT_L2_LINKUP 			= "Link Up 상태";
//	public static final String	RPT_L2_PORTSTATUS 		= "포트 상태";  
//	public static final String	RPT_L2_VLANINFO 		= "VLAN 정보";
//	public static final String	RPT_L2_STPINFO 			= "STP 정보";
//	public static final String	RPT_L2_TRUNKINFO 		= "Trunk 정보";
//	public static final String	RPT_L3_INTERFACE 		= "인터페이스 정보";
//	public static final String	RPT_L3_GATEWAY 			= "게이트웨이 정보";
//	public static final String	RPT_L4_PMSTAT 			= "Pool Member 상태";
//	public static final String	RPT_L4_VSSTAT 			= "Virtual Server 상태"; 
//	public static final String	RPT_L4_CONNECTION 		= "Connection 상태"; 
//	public static final String	RPT_L4_DIRECT 			= "Direct 기능 상태";  
//	public static final String	RPT_L7_IRULE 			= "iRule 상태"; 
//	public static final String	RPT_L7_ONECONNECT 		= "Oneconnect 상태"; 
//	public static final String	RPT_L7_RAMCACHE 		= "Ramcache 상태"; 
//	public static final String	RPT_L7_COMPRESSION 		= "Compression 상태"; 
//	public static final String	RPT_L7_SSLACCEL 		= "SSL 가속 상태 ";
	
//	public String getName(int type, int code) throws OBException
//	{
//		OBDatabase db=new OBDatabase();
//		try
//		{
//			db.openDB();
//		}
//		catch(Exception e)
//		{
//			throw new OBException(e.getMessage());
//		}
//		
//		String result="";
//		try
//		{
//			result = getName(type, code, db);
//		}
//		catch(Exception e)
//		{
//			throw new OBException(e.getMessage());
//		}
//		db.closeDB();
//		return result;
//	}
//
//	public String getName(int type, int code, OBDatabase db) throws OBException
//	{
//		String sqlText="";
//		
//		try
//		{
//			sqlText = String.format("SELECT " +
//									"NAME " +
//									"FROM MNG_TERMINOLOGY " +
//									"WHERE TYPE=%d AND CODE=%d;",
//									type, code);
//
//			ResultSet rs;
//
//			rs = db.executeQuery(sqlText);
//		
//			if(rs.next() == false)
//			{
//				return String.format("not defined type:%d, code:%d", type, code);
//			}
//			return db.getString(rs, "NAME");
//		}
//		catch(SQLException e)
//		{
//			throw new OBException(OBException.ERRCODE_DB_QEURY, e.getMessage());
//		}		
//		catch(Exception e)
//		{
//			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
//		}
//	}
}
