package kr.openbase.adcsmart.service.impl.f5.handler;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;

import kr.openbase.adcsmart.service.dto.fault.OBDtoFaultSessionInfo;
import kr.openbase.adcsmart.service.dto.fault.OBDtoSessionSearchOption;
import kr.openbase.adcsmart.service.impl.dto.OBDtoFaultFileSizeInfo;
import kr.openbase.adcsmart.service.impl.f5.handler.dto.OBDtoDaemStatus;
import kr.openbase.adcsmart.service.impl.f5.handler.dto.OBDtoFdbInfo;
import kr.openbase.adcsmart.service.impl.f5.handler.dto.OBDtoNtpInfoF5;
import kr.openbase.adcsmart.service.snmp.dto.OBDtoArpInfo;
import kr.openbase.adcsmart.service.utility.OBCommon;
import kr.openbase.adcsmart.service.utility.OBDateTime;
import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.service.utility.OBParser;
import kr.openbase.adcsmart.service.utility.OBSystemLog;
import kr.openbase.adcsmart.service.utility.OBUtility;

public class OBCLIParserF5 {
//	 public static void main(String args[])
//	 {
//		 String input = "121212"; 		 
//		 int outPut = 0;
//		 
//		 if(OBUtility.isInteger(input))
//		 {
//			 System.out.println(input);
//		 }
//		 else
//		 {
//			 System.out.println("djdjdjdfjdj");
//		 }
//		 
//		 outPut = Integer.parseInt(input);
//		 
//		 System.out.println("output:" + outPut);
//	 }
	public HashMap<String, OBDtoFaultFileSizeInfo> parseFileSizeInfo(String input) throws OBException {
		HashMap<String, OBDtoFaultFileSizeInfo> retVal = new HashMap<String, OBDtoFaultFileSizeInfo>();
		try {
			String lines[] = input.split("\n");
			for (String line : lines) {
				if (line.isEmpty())
					continue;
				line = OBParser.convertMultipleSpaces2SingleSpace(line);

				String element[] = line.split(" ");// space를 기준으로 분리.//-rw------- 1 root root 1074820 Oct 14 13:48
													// aaa.pcap
				if (element.length > 10) {
					OBSystemLog.info(OBDefine.LOGFILE_SYSTEM, String.format("failed to parse a file length(%s)", line));
					continue;
				}
				// 다섯번째에 length, 아홉번째에는 이름 정보가 있음.
				String fileName = element[element.length - 1];
				Long fileSize = Long.parseLong(element[4]);
				OBDtoFaultFileSizeInfo obj = new OBDtoFaultFileSizeInfo();
				obj.setFileName(fileName);
				obj.setFileSize(fileSize);
				retVal.put(fileName, obj);
			}
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);
		}
		return retVal;
	}

	/*
	 * [root@portal-dev5:Standby] config # b arp show ARP 192.168.200.1 -
	 * 74:8E:F8:14:30:C0 VLAN ext expire 83s resolved ARP 192.168.200.14 -
	 * 00:01:D7:78:33:83 VLAN ext expire 211s resolved ARP 192.168.201.1 -
	 * 00:1B:25:77:FB:00 VLAN int expire 42s resolved ARP 192.168.201.14 -
	 * 00:01:D7:78:33:85 VLAN int expire 150s resolved
	 */
	public ArrayList<OBDtoArpInfo> parseInfoArp(String input) throws OBException {
		ArrayList<OBDtoArpInfo> retVal = new ArrayList<OBDtoArpInfo>();
		String lines[] = input.split("\n");
		for (String line : lines) {
			line = OBParser.convertMultipleSpaces2SingleSpace(line);
			if (line.isEmpty()) {
				continue;
			}
			String element[] = line.split(" "); // " "(공백으로 구분한다. 위의 Parser에서 멀티 스패이스로 공백처리 이미 함)
			if (element.length != 9) {
				continue;
			}
			OBDtoArpInfo obj = new OBDtoArpInfo();
			obj.setDstIPAddr(element[1]);
			obj.setMacAddr(element[3]);
			obj.setVlanInfo(element[5]);
			obj.setPortNum("");
			retVal.add(obj);
		}
		return retVal;
	}

//     [root@bigip:Active] config # b vlan all fdb show    
//     VLAN ext - L2 forwarding table entry:
//     +-> L2 FORWARD STAT ext/E0:69:95:C6:EA:7D interface 1.1 dynamic
//     +-> L2 FORWARD STAT ext/00:15:99:47:F9:45 interface 1.1 dynamic
//     +-> L2 FORWARD STAT ext/10:60:4B:5C:BB:26 interface 1.1 dynamic
//     +-> L2 FORWARD STAT ext/88:51:FB:4A:D5:BB interface 1.1 dynamic
//     +-> L2 FORWARD STAT ext/5C:26:0A:5E:1C:1E interface 1.1 dynamic
//     +-> L2 FORWARD STAT ext/A0:B3:CC:44:7E:94 interface 1.1 dynamic
//     +-> L2 FORWARD STAT ext/00:1E:67:86:33:F1 interface 1.1 dynamic
//     +-> L2 FORWARD STAT ext/88:51:FB:4A:C3:4D interface 1.1 dynamic
//     +-> L2 FORWARD STAT ext/F0:DE:F1:95:D7:00 interface 1.1 dynamic
//     +-> L2 FORWARD STAT ext/28:D2:44:55:7D:28 interface 1.1 dynamic
//     +-> L2 FORWARD STAT ext/76:8E:F8:AB:02:EB interface 1.1 dynamic
//     +-> L2 FORWARD STAT ext/28:D2:44:58:FD:9C interface 1.1 dynamic
//     +-> L2 FORWARD STAT ext/98:4B:E1:AF:5F:57 interface 1.1 dynamic
//     +-> L2 FORWARD STAT ext/00:0B:AB:65:82:84 interface 1.1 dynamic
//     +-> L2 FORWARD STAT ext/10:60:4B:78:2B:9F interface 1.1 dynamic
//     +-> L2 FORWARD STAT ext/08:2E:5F:71:77:3E interface 1.1 dynamic
//     +-> L2 FORWARD STAT ext/00:11:32:0B:A9:65 interface 1.1 dynamic
//     +-> L2 FORWARD STAT ext/00:1E:67:C2:7E:C9 interface 1.1 dynamic
//     +-> L2 FORWARD STAT ext/00:24:38:14:49:8B interface 1.1 dynamic
//     +-> L2 FORWARD STAT ext/18:67:B0:29:D3:91 interface 1.1 dynamic
//     +-> L2 FORWARD STAT ext/3C:97:0E:0B:F0:5F interface 1.1 dynamic
//     +-> L2 FORWARD STAT ext/00:11:32:24:A0:2B interface 1.1 dynamic
//     +-> L2 FORWARD STAT ext/00:25:90:23:02:73 interface 1.1 dynamic
//     +-> L2 FORWARD STAT ext/74:8E:F8:AB:02:E0 interface 1.1 dynamic
//     +-> L2 FORWARD STAT ext/00:26:B9:96:F9:1B interface 1.1 dynamic
//     +-> L2 FORWARD STAT ext/28:92:4A:B3:BC:5D interface 1.1 dynamic
//     +-> L2 FORWARD STAT ext/24:F5:AA:C7:DB:1A interface 1.1 dynamic
//     +-> L2 FORWARD STAT ext/00:20:6B:6D:4F:D5 interface 1.1 dynamic
//     +-> L2 FORWARD STAT ext/60:EB:69:78:E5:C0 interface 1.1 dynamic
//     +-> L2 FORWARD STAT ext/20:89:84:37:DF:96 interface 1.1 dynamic

	public ArrayList<OBDtoFdbInfo> parseInfoFdb(String input) throws OBException {
		ArrayList<OBDtoFdbInfo> retVal = new ArrayList<OBDtoFdbInfo>();
		String lines[] = input.split("\n");
		for (String line : lines) {
			line = OBParser.convertMultipleSpaces2SingleSpace(line);
			if (line.isEmpty()) {
				continue;
			}
			String element[] = line.split(" "); // " "(공백으로 구분한다. 위의 Parser에서 멀티 스패이스로 공백처리 이미 함)
			if (element.length != 8) {
				continue;
			}
			String vlanMac[] = element[4].split("/");
			OBDtoFdbInfo obj = new OBDtoFdbInfo();
			obj.setMacAddress(vlanMac[1]);
			obj.setPort(element[6]);
			obj.setVlan(vlanMac[0]);
			retVal.add(obj);
		}
		return retVal;
	}

//     VLAN ext - L2 forwarding table entry:
//         +-> FDB ext/00:01:D7:D6:A1:84 interface 1.1 dynamic
//         +-> FDB ext/00:03:B2:CE:5E:40 interface 1.1 dynamic
//         +-> FDB ext/00:0C:29:10:91:82 interface 1.1 dynamic
//         +-> FDB ext/00:0C:29:15:9D:DA interface 1.1 dynamic
//         +-> FDB ext/00:0C:29:5F:23:FD interface 1.1 dynamic
//         +-> FDB ext/00:0C:29:6B:BA:18 interface 1.1 dynamic
//         +-> FDB ext/E8:03:9A:9E:A0:AB interface 1.1 dynamic
//         +-> FDB ext/EC:9A:74:93:B5:A3 interface 1.1 dynamic
//         +-> FDB ext/F0:DE:F1:90:35:4D interface 1.1 dynamic
//         +-> FDB ext/F0:DE:F1:95:D7:00 interface 1.1 dynamic
//         +-> FDB ext/F8:A9:63:57:1E:47 interface 1.1 dynamic
//         +-> FDB ext/FC:15:B4:A0:5E:54 interface 1.1 dynamic
//         +-> FDB ext/FC:F8:AE:CC:F8:B2 interface 1.1 dynamic
//         VLAN vlan_trunk - L2 forwarding table entry:
//         +-> FDB vlan_trunk/00:01:D7:D6:DD:03 trunk trunk dynamic
//         +-> FDB vlan_trunk/00:03:B2:CE:5E:40 trunk trunk dynamic
//         +-> FDB vlan_trunk/00:0C:29:10:91:82 trunk trunk dynamic
//         +-> FDB vlan_trunk/00:0C:29:15:9D:DA trunk trunk dynamic
//         +-> FDB vlan_trunk/00:0C:29:5F:23:FD trunk trunk dynamic
//         +-> FDB vlan_trunk/00:0C:29:6B:BA:18 trunk trunk dynamic
//         +-> FDB vlan_trunk/00:0C:29:74:AB:7D trunk trunk dynamic
//         +-> FDB vlan_trunk/00:0C:29:97:72:81 trunk trunk dynamic
//         +-> FDB vlan_trunk/00:11:32:0B:A9:65 trunk trunk dynamic
//         +-> FDB vlan_trunk/00:11:32:24:A0:2B trunk trunk dynamic
//         +-> FDB vlan_trunk/00:12:F2:7C:F5:30 trunk trunk dynamic
//         +-> FDB vlan_trunk/00:15:17:3A:43:98 trunk trunk dynamic
//         +-> FDB vlan_trunk/00:15:99:47:F9:45 trunk trunk dynamic
//         +-> FDB vlan_trunk/00:1D:42:12:70:23 trunk trunk dynamic
//         +-> FDB vlan_trunk/00:23:E9:6B:27:44 trunk trunk dynamic
//         +-> FDB vlan_trunk/3C:97:0E:18:AB:CB trunk trunk dynamic
//         +-> FDB vlan_trunk/5C:26:0A:5E:1C:1E trunk trunk dynamic
//         +-> FDB vlan_trunk/60:EB:69:78:E5:C0 trunk trunk dynamic
//         +-> FDB vlan_trunk/74:8E:F8:14:30:C0 trunk trunk dynamic
//         +-> FDB vlan_trunk/74:8E:F8:AB:02:E0 trunk trunk dynamic
//         +-> FDB vlan_trunk/B4:B5:2F:D9:D0:91 trunk trunk dynamic
//         +-> FDB vlan_trunk/E0:3F:49:4A:EC:27 trunk trunk dynamic
//         +-> FDB vlan_trunk/E0:69:95:C6:EA:7D trunk trunk dynamic
//         +-> FDB vlan_trunk/E8:03:9A:9E:A0:AB trunk trunk dynamic
	public ArrayList<OBDtoFdbInfo> parseInfoFdbV10(String input) throws OBException {
		ArrayList<OBDtoFdbInfo> retVal = new ArrayList<OBDtoFdbInfo>();
		String lines[] = input.split("\n");
		for (String line : lines) {
			line = OBParser.convertMultipleSpaces2SingleSpace(line);
			if (line.isEmpty()) {
				continue;
			}
			String element[] = line.split(" "); // " "(공백으로 구분한다. 위의 Parser에서 멀티 스패이스로 공백처리 이미 함)
			if (element.length != 6) {
				continue;
			}
			String vlanMac[] = element[2].split("/");
			OBDtoFdbInfo obj = new OBDtoFdbInfo();
			obj.setMacAddress(vlanMac[1]);
			obj.setPort(element[4]);
			obj.setVlan(vlanMac[0]);
			retVal.add(obj);
		}
		return retVal;
	}

	// Thu Oct 10 11:47:02 KST 2013
	public Timestamp parseInfoDate(String input) throws OBException {
		try {
			String lines[] = input.split("\n");
			for (String line : lines) {
				if (line.isEmpty())
					continue;

				if (line.length() < 28)//
				{
					continue;
				}
				try {
					Timestamp time = OBDateTime.toTimestamp("EEE MMM dd HH:mm:ss ZZZ yyyy", line);
					if (time != null)
						return time;
				} catch (Exception e) {
				}
			}
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);
		}
		return null;
	}

	private final static String SUFFIX_FAILOVER_ACTIVE = "active";// cpuUtil1Second: 17%
	private final static String SUFFIX_FAILOVER_STANDBY = "standby";// cpuUtil1Second: 17%

	public int parseFailoverType(String input) throws OBException {
		try {
			int strIndex = 0;
			String lines[] = input.split("\n");

			for (String line : lines) {
				if (line.isEmpty())
					continue;
				// sp cpu usage 검출.
				strIndex = line.indexOf(SUFFIX_FAILOVER_ACTIVE);
				if (strIndex >= 0) {
					return 1;
				}
				strIndex = line.indexOf(SUFFIX_FAILOVER_STANDBY);
				if (strIndex >= 0) {
					return 2;
				}
			}
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);
		}
		return 0;
	}

	public ArrayList<OBDtoFaultSessionInfo> remanufactoringParsedSessionList(Integer adcIndex,
			ArrayList<OBDtoFaultSessionInfo> sessionList, ArrayList<OBDtoSessionSearchOption> seachKeyList)
			throws OBException {
		String srcPort = null;
		String dstPort = null;
		String realPort = null;
		String dstIP = "";
		String protocolToString = "";
		String srcIP = "";
		Integer protocol = 0;
		try {
			for (OBDtoSessionSearchOption option : seachKeyList) {
				if (option.getType() == OBDtoSessionSearchOption.OPTION_TYPE_DST_PORT) {
					dstPort = option.getContent();
				} else if (option.getType() == OBDtoSessionSearchOption.OPTION_TYPE_SRC_IP) {
					srcIP = option.getContent();
				} else if (option.getType() == OBDtoSessionSearchOption.OPTION_TYPE_DST_IP) {
					dstIP = option.getContent();
				} else if (option.getType() == OBDtoSessionSearchOption.OPTION_TYPE_SRC_PORT) {
					srcPort = option.getContent();
				} else if (option.getType() == OBDtoSessionSearchOption.OPTION_TYPE_REAL_PORT) {
					realPort = option.getContent();
				} else if (option.getType() == OBDtoSessionSearchOption.OPTION_TYPE_PROTOCOL) {
					protocolToString = option.getContent();
					if (OBDefine.STRING_TO_PROTOCOL_TCP == protocolToString) {
						protocol = OBDefine.INT_TO_PROTOCOL_TCP;
					} else if (OBDefine.STRING_TO_PROTOCOL_UDP == protocolToString) {
						protocol = OBDefine.INT_TO_PROTOCOL_UDP;
					} else if (OBDefine.STRING_TO_PROTOCOL_ICMP == protocolToString) {
						protocol = OBDefine.INT_TO_PROTOCOL_ICMP;
					}
				}
			}
		} catch (IllegalArgumentException e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL,
					"IllegalArgumentException: " + e.getMessage());
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, "GeneralException: " + e.getMessage());
		}
//        Integer dPort = convertPort(dstPort);
		ArrayList<OBDtoFaultSessionInfo> retVal = new ArrayList<OBDtoFaultSessionInfo>();
		for (OBDtoFaultSessionInfo info : sessionList) {
			if (srcPort != null) {
				if (srcPort.equals(info.getSrcPort()) == false)
					continue;
			}
			if (srcIP != null && srcIP.isEmpty() == false) {
				if (srcIP.equals(info.getSrcIP()) == false)
					continue;
			}
			if (dstPort != null) {
				if (dstPort.equals(info.getDstPort()) == false)
					continue;
			}
			if (realPort != null) {
				if (realPort.equals(info.getRealPort()) == false)
					continue;
			}
			if (dstIP != null && dstIP.isEmpty() == false) {
				if (dstIP.equals(info.getDstIP()) == false)
					continue;
			}
			if (protocol != null && protocol != 0) {
				if (protocol.equals(info.getProtocol()) == false) {
					continue;
				}
			}
			retVal.add(info);
		}
		return retVal;
	}

	private String convertPort(String port) throws OBException {
		String portNumber;
		if (OBUtility.isInteger(port)) {
			return port;
		} else {
			portNumber = OBCommon.getWellknownPort(port);// new OBAdcF5HandlerV11().getWellknownPort(port);
		}
		return portNumber;
	}

	public ArrayList<OBDtoFaultSessionInfo> parseSessionInfoList(Integer adcIndex, String input) throws OBException {
		String protocol = "";

		ArrayList<OBDtoFaultSessionInfo> retVal = new ArrayList<OBDtoFaultSessionInfo>();
		String lines[] = input.split("\n");

		for (String line : lines) {
			// CONN client 172.172.2.110:59152 server 192.168.200.14:snmp ss client
			// 172.172.2.110:59152 ss server 192.168.200.14:snmp local protocol udp age 2 -
			// Idle Timeout: 300
			// CONN client 172.172.2.110:59190 server 192.168.200.14:snmp ss client
			// 172.172.2.110:59190 ss server 192.168.200.14:snmp local protocol udp age 301
			// - Idle Timeout: 300
			// CONN client 172.172.2.110:59296 server 192.168.200.14:snmp ss client
			// 172.172.2.110:59296 ss server 192.168.200.14:snmp local protocol udp age 122
			// - Idle Timeout: 300

//	    	192.168.200.11:36175 <-> any%65535 <-> 192.168.100.12:http   tcp 1/1
//	    	172.172.2.209:51276 <-> 192.168.200.11:shell <-> any%65535   udp 1/1
//	    	172.172.2.209:51276 <-> 192.168.200.11:snmp <-> 192.168.200.11:snmp   udp 1/1	    	
			line = OBParser.convertMultipleSpaces2SingleSpace(line);
			if (line.isEmpty()) {
				continue;
			}
			String element[] = line.split(" "); // " "(공백으로 구분한다. 위의 Parser에서 멀티 스패이스로 공백처리 이미 함)
			if (element.length < 6) {
				continue;
			}

			if (element[0].length() < 10)
				continue;

			OBDtoFaultSessionInfo obj = new OBDtoFaultSessionInfo();
			obj.setAdcIndex(adcIndex);

			// src info (index 0 )

			String srcInfo[] = element[0].split(":");
			if (srcInfo.length == 2) {
				obj.setSrcIP(srcInfo[0]);
				obj.setSrcPort(convertPort(srcInfo[1])); // 특수 문자의 경우 exception발생 double은 발생 안함.
			} else {
				obj.setSrcIP(srcInfo[0]);
				obj.setSrcPort("0"); // 둘중 한쪽에서는 포트를 입력 하도록 한다.
			}

			// dst info (index 2)
			String dstInfo[] = element[2].split(":");
			if (dstInfo.length == 2) // ":" 이 없이 문자열만 있을경우 - length가 1의 경우 처리
			{
				obj.setDstIP(dstInfo[0]);
				obj.setDstPort(convertPort(dstInfo[1])); // 둘중 한쪽에서는 포트를 입력 하도록 한다.
			} else {
				obj.setDstIP(dstInfo[0]);
				obj.setDstPort("0"); // 둘중 한쪽에서는 포트를 입력 하도록 한다.
			}

			// real server (index 4)
			String realIPInfo[] = element[4].split(":");
			if (realIPInfo.length == 2) // ":" 이 없이 문자열만 있을경우 - length가 1의 경우 처리
			{
				obj.setRealIP(realIPInfo[0]);
				obj.setRealPort(convertPort(realIPInfo[1])); // 둘중 한쪽에서는 포트를 입력 하도록 한다.
			} else {
				obj.setRealIP(realIPInfo[0]);
				obj.setRealPort("0"); // 둘중 한쪽에서는 포트를 입력 하도록 한다.
			}

			// protocol info (index 5)
			protocol = element[5];

			// aging time
			obj.setAgingTime(-1);// 이 버전에서는 지원하지 않는다.

//		String cpuInfo[] = element[6].split("/");
			if (element.length >= 7)// 9 버전에서는 cpu 정보가 출력 되지 않음.
				obj.setSpNumber(element[6]);

			obj.setProtocol(0);
			if (protocol.equals(OBDefine.STRING_TO_PROTOCOL_TCP)) {
				obj.setProtocol(OBDefine.INT_TO_PROTOCOL_TCP);
			}
			if (protocol.equals(OBDefine.STRING_TO_PROTOCOL_UDP)) {
				obj.setProtocol(OBDefine.INT_TO_PROTOCOL_UDP);
			}
			if (protocol.equals(OBDefine.STRING_TO_PROTOCOL_ICMP)) {
				obj.setProtocol(OBDefine.INT_TO_PROTOCOL_ICMP);
			}
			obj.setAgingTime(0);
			retVal.add(obj);
		}
		return retVal;
	}

	private final static String SUFFIX_SYSLOG_HOST = "host ";//
	private final static String SUFFIX_SYSLOG_HOST_9V = "remote server ";//

	public ArrayList<String> parseSyslogList(String input) throws OBException {
		ArrayList<String> retVal = new ArrayList<String>();
		int strIndex = 0;
		int hostSize = 0;
		input = OBParser.trimAndSimplifySpaces(input);
		String lines[] = input.split("\n");
		for (String line : lines) {
			if (line.contains(SUFFIX_SYSLOG_HOST_9V)) {
				strIndex = line.indexOf(SUFFIX_SYSLOG_HOST_9V);
				hostSize = SUFFIX_SYSLOG_HOST_9V.length();
			} else {
				strIndex = line.indexOf(SUFFIX_SYSLOG_HOST);
				hostSize = SUFFIX_SYSLOG_HOST.length();
			}
			if (strIndex >= 0) {
				String host = line.substring(strIndex + hostSize);
				retVal.add(host);
			}
		}
		return retVal;
	}

	/*
	 * [root@portal-dev4:Active] config # b sshd list sshd { allow "ALL" }
	 * 
	 * [root@portal-dev4:Active] config # b httpd list httpd { allow {
	 * "172.172.1.0/255.255.255.0" "All" } authpamidletimeout 14400
	 * browsercachetimeout 120 maxclients 10 }
	 */
	private final static String SUFFIX_ALLOW_START = "allow {";//
	private final static String SUFFIX_ALLOW_END = "}";//
	public final static String SUFFIX_ALLOW_ALL = "all";//
	private final static String SUFFIX_ALLOW_HTTP_ALL = "httpd {}";//
	private final static String SUFFIX_ALLOW_SSHD_ALL = "sshd {}";//

	public ArrayList<String> parseHttpsList(String input) throws OBException {
		ArrayList<String> retVal = new ArrayList<String>();
		int strIndex = 0;
		boolean startFlag = false;
		input = OBParser.trimAndSimplifySpaces(input);
		String lines[] = input.split("\n");
		for (String line : lines) {
			if (startFlag == false) {
				strIndex = line.indexOf(SUFFIX_ALLOW_START);
				if (strIndex >= 0) {
					startFlag = true;
					continue;
				}
				strIndex = line.toLowerCase().indexOf(SUFFIX_ALLOW_HTTP_ALL);
				if (strIndex >= 0) {
					retVal.add(SUFFIX_ALLOW_ALL);
					continue;
				}
				continue;
			}

			strIndex = line.indexOf(SUFFIX_ALLOW_END);
			if (strIndex >= 0) {// end of parsing
				break;
			}
			String host = line.replace("\"", "");
			retVal.add(host.toLowerCase());
		}

		if (retVal.size() == 0)
			retVal.add(SUFFIX_ALLOW_ALL.toLowerCase());// 아무것도 없을 경우에는 무조건 all.
		return retVal;
	}

	public ArrayList<String> parseSshdList(String input) throws OBException {
		ArrayList<String> retVal = new ArrayList<String>();
		int strIndex = 0;
		boolean startFlag = false;
		input = OBParser.trimAndSimplifySpaces(input);
		String lines[] = input.split("\n");
		for (String line : lines) {
			if (startFlag == false) {
				strIndex = line.indexOf(SUFFIX_ALLOW_START);
				if (strIndex >= 0) {
					startFlag = true;
					continue;
				}
				strIndex = line.toLowerCase().indexOf(SUFFIX_ALLOW_SSHD_ALL);
				if (strIndex >= 0) {
					retVal.add(SUFFIX_ALLOW_ALL.toLowerCase());
					continue;
				}
				continue;
			}

			strIndex = line.indexOf(SUFFIX_ALLOW_END);
			if (strIndex >= 0) {// end of parsing
				break;
			}
			String host = line.replace("\"", "");
			retVal.add(host.toLowerCase());
		}

		if (retVal.size() == 0)
			retVal.add(SUFFIX_ALLOW_ALL.toLowerCase());// 아무것도 없을 경우에는 무조건 all.
		return retVal;
	}

//    public static void main(String args[]) throws OBException, OBExceptionUnreachable, OBExceptionLogin
//    {
//        OBDtoAdcInfo adcInfo = new OBAdcManagementImpl().getAdcInfo(4);
//        OBAdcF5Handler handler = OBCommon.getValidF5Handler(adcInfo.getSwVersion()); 
//        handler.setConnectionInfo(adcInfo.getAdcIpAddress(), adcInfo.getAdcCliAccount(), adcInfo.getAdcCliPasswordDecrypt(), adcInfo.getConnPort());
//        handler.sshLogin();
//        String info = handler.cmndNtpInfo();
//        System.out.println(info);
//        
//        OBDtoNtpInfoF5 aaaa = new OBCLIParserF5().parseNtpInfo(info);
//        System.out.println(aaaa);
//        
//        handler.disconnect();
//    }

	/*
	 * [root@gtm2:Active:Standalone] config # ntpq -np remote refid st t when poll
	 * reach delay offset jitter
	 * =============================================================================
	 * = 127.127.1.0 .LOCL. 10 l 47 64 377 0.000 0.000 0.000
	 * [root@gtm2:Active:Standalone] config #
	 */
	private final static String SUFFIX_NTP_INFO_START = "====================================================================";//

	public OBDtoNtpInfoF5 parseNtpInfo(String input) throws OBException {
		OBDtoNtpInfoF5 retVal = new OBDtoNtpInfoF5();
		try {
			int strIndex = 0;
			boolean startFlag = false;
			input = OBParser.trimAndSimplifySpaces(input);
			String lines[] = input.split("\n");
			for (String line : lines) {
				if (startFlag == false) {
					strIndex = line.indexOf(SUFFIX_NTP_INFO_START);
					if (strIndex >= 0) {
						startFlag = true;
						continue;
					}
					continue;
				}

				String elements[] = line.split(" ");
				if (elements.length == 10) {
					retVal.setReachTime(Integer.parseInt(elements[6]));
					retVal.setReferenceID(elements[1]);
					retVal.setRemoteIPAddress(elements[0]);
				} else {
					retVal = null;
				}
				break;
			}
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, "general: " + e.getMessage());
		}
		return retVal;
	}

	/**
	 * [root@gtm2:Active:Standalone] config # bigstart status acctd down, Not
	 * provisioned aced down, Not provisioned alertd run (pid 8006) 39 days
	 * antserver down, Not provisioned apd down, Not provisioned apmd down, Not
	 * provisioned asm down, Not provisioned avrd run (pid 6651) 39 days big3d run
	 * (pid 6929) 39 days bigd run (pid 7422) 39 days cbrd run (pid 6612) 39 days
	 * chmand run (pid 6236) 39 days clusterd down, not required csyncd run (pid
	 * 7423) 39 days datastor down, Not provisioned dedup_admin down, Not
	 * provisioned devmgmtd run (pid 6621) 39 days diskevent run (pid 7989) 39 days
	 * dnscached down, Not provisioned dosl7d down, Not provisioned
	 */
	private final static String SUFFIX_DAEMON_STATUS_TMM = "tmm";//
	private final static String SUFFIX_DAEMON_STATUS_BCM = "bcm56xxd";//
	private final static String SUFFIX_DAEMON_STATUS_BIG3D = "big3d";//
	private final static String SUFFIX_DAEMON_STATUS_BIGD = "bigd";//
	private final static String SUFFIX_DAEMON_STATUS_CBRD = "cbrd";//
	private final static String SUFFIX_DAEMON_STATUS_FPDD = "fpdd";//
	private final static String SUFFIX_DAEMON_STATUS_LACPD = "LACPD";//
	private final static String SUFFIX_DAEMON_STATUS_LOGSTATD = "logstatd";//
	private final static String SUFFIX_DAEMON_STATUS_MCPD = "mcpd";//
	private final static String SUFFIX_DAEMON_STATUS_SOD = "sod";//
	private final static String SUFFIX_DAEMON_STATUS_RUN = "run";//

//	public static void main(String args[]) throws OBException, OBExceptionUnreachable, OBExceptionLogin
//	{
//		OBDtoAdcInfo adcInfo = new OBAdcManagementImpl().getAdcInfo(4);
//		OBAdcF5Handler handler = OBCommon.getValidF5Handler(adcInfo.getSwVersion());
//		handler.setConnectionInfo(adcInfo.getAdcIpAddress(), adcInfo.getAdcCliAccount(),
//				adcInfo.getAdcCliPasswordDecrypt(), adcInfo.getConnPort());
//		handler.sshLogin();
//		String info = handler.cmndDaemonStatus();
//		System.out.println(info);
//
//		ArrayList<OBDtoDaemStatus> aaaa = new OBCLIParserF5().parseDaemonStatus(info);
//		System.out.println(aaaa);
//
//		handler.disconnect();
//	}

	public ArrayList<OBDtoDaemStatus> parseDaemonStatus(String input) throws OBException {
		ArrayList<OBDtoDaemStatus> retVal = new ArrayList<OBDtoDaemStatus>();
		try {
			input = OBParser.trimAndSimplifySpaces(input);
			String lines[] = input.split("\n");
			for (String line : lines) {
				String elements[] = line.split(" ", 3);// 크게 3부분으로 나눈다.
				if (elements.length != 3) {
					continue;
				}

				if (elements[0].compareToIgnoreCase(SUFFIX_DAEMON_STATUS_TMM) == 0) {
					OBDtoDaemStatus daemon = new OBDtoDaemStatus();
					daemon.setProcessName(SUFFIX_DAEMON_STATUS_TMM);
					daemon.setStatus(OBDtoDaemStatus.STATUS_DOWN);
					if (elements[1].contains(SUFFIX_DAEMON_STATUS_RUN))
						daemon.setStatus(OBDtoDaemStatus.STATUS_RUN);
					retVal.add(daemon);
				} else if (elements[0].compareToIgnoreCase(SUFFIX_DAEMON_STATUS_BCM) == 0) {
					OBDtoDaemStatus daemon = new OBDtoDaemStatus();
					daemon.setProcessName(SUFFIX_DAEMON_STATUS_BCM);
					daemon.setStatus(OBDtoDaemStatus.STATUS_DOWN);
					if (elements[1].contains(SUFFIX_DAEMON_STATUS_RUN))
						daemon.setStatus(OBDtoDaemStatus.STATUS_RUN);
					retVal.add(daemon);
				} else if (elements[0].compareToIgnoreCase(SUFFIX_DAEMON_STATUS_BIG3D) == 0) {
					OBDtoDaemStatus daemon = new OBDtoDaemStatus();
					daemon.setProcessName(SUFFIX_DAEMON_STATUS_BIG3D);
					daemon.setStatus(OBDtoDaemStatus.STATUS_DOWN);
					if (elements[1].contains(SUFFIX_DAEMON_STATUS_RUN))
						daemon.setStatus(OBDtoDaemStatus.STATUS_RUN);
					retVal.add(daemon);
				} else if (elements[0].compareToIgnoreCase(SUFFIX_DAEMON_STATUS_BIGD) == 0) {
					OBDtoDaemStatus daemon = new OBDtoDaemStatus();
					daemon.setProcessName(SUFFIX_DAEMON_STATUS_BIGD);
					daemon.setStatus(OBDtoDaemStatus.STATUS_DOWN);
					if (elements[1].contains(SUFFIX_DAEMON_STATUS_RUN))
						daemon.setStatus(OBDtoDaemStatus.STATUS_RUN);
					retVal.add(daemon);
				} else if (elements[0].compareToIgnoreCase(SUFFIX_DAEMON_STATUS_CBRD) == 0) {
					OBDtoDaemStatus daemon = new OBDtoDaemStatus();
					daemon.setProcessName(SUFFIX_DAEMON_STATUS_CBRD);
					daemon.setStatus(OBDtoDaemStatus.STATUS_DOWN);
					if (elements[1].contains(SUFFIX_DAEMON_STATUS_RUN))
						daemon.setStatus(OBDtoDaemStatus.STATUS_RUN);
					retVal.add(daemon);
				} else if (elements[0].compareToIgnoreCase(SUFFIX_DAEMON_STATUS_FPDD) == 0) {
					OBDtoDaemStatus daemon = new OBDtoDaemStatus();
					daemon.setProcessName(SUFFIX_DAEMON_STATUS_FPDD);
					daemon.setStatus(OBDtoDaemStatus.STATUS_DOWN);
					if (elements[1].contains(SUFFIX_DAEMON_STATUS_RUN))
						daemon.setStatus(OBDtoDaemStatus.STATUS_RUN);
					retVal.add(daemon);
				} else if (elements[0].compareToIgnoreCase(SUFFIX_DAEMON_STATUS_LACPD) == 0) {
					OBDtoDaemStatus daemon = new OBDtoDaemStatus();
					daemon.setProcessName(SUFFIX_DAEMON_STATUS_LACPD);
					daemon.setStatus(OBDtoDaemStatus.STATUS_DOWN);
					if (elements[1].contains(SUFFIX_DAEMON_STATUS_RUN))
						daemon.setStatus(OBDtoDaemStatus.STATUS_RUN);
					retVal.add(daemon);
				} else if (elements[0].compareToIgnoreCase(SUFFIX_DAEMON_STATUS_LOGSTATD) == 0) {
					OBDtoDaemStatus daemon = new OBDtoDaemStatus();
					daemon.setProcessName(SUFFIX_DAEMON_STATUS_LOGSTATD);
					daemon.setStatus(OBDtoDaemStatus.STATUS_DOWN);
					if (elements[1].contains(SUFFIX_DAEMON_STATUS_RUN))
						daemon.setStatus(OBDtoDaemStatus.STATUS_RUN);
					retVal.add(daemon);
				} else if (elements[0].compareToIgnoreCase(SUFFIX_DAEMON_STATUS_MCPD) == 0) {
					OBDtoDaemStatus daemon = new OBDtoDaemStatus();
					daemon.setProcessName(SUFFIX_DAEMON_STATUS_MCPD);
					daemon.setStatus(OBDtoDaemStatus.STATUS_DOWN);
					if (elements[1].contains(SUFFIX_DAEMON_STATUS_RUN))
						daemon.setStatus(OBDtoDaemStatus.STATUS_RUN);
					retVal.add(daemon);
				} else if (elements[0].compareToIgnoreCase(SUFFIX_DAEMON_STATUS_SOD) == 0) {
					OBDtoDaemStatus daemon = new OBDtoDaemStatus();
					daemon.setProcessName(SUFFIX_DAEMON_STATUS_SOD);
					daemon.setStatus(OBDtoDaemStatus.STATUS_DOWN);
					if (elements[1].contains(SUFFIX_DAEMON_STATUS_RUN))
						daemon.setStatus(OBDtoDaemStatus.STATUS_RUN);
					retVal.add(daemon);
				}
			}
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, "general: " + e.getMessage());
		}
		return retVal;
	}
}