package kr.openbase.adcsmart.service.impl.f5.handler;

import java.util.ArrayList;

import kr.openbase.adcsmart.service.dto.fault.OBDtoFaultSessionInfo;
import kr.openbase.adcsmart.service.impl.f5.handler.dto.OBDtoFdbInfo;
import kr.openbase.adcsmart.service.snmp.dto.OBDtoArpInfo;
import kr.openbase.adcsmart.service.utility.OBCommon;
import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.service.utility.OBParser;
import kr.openbase.adcsmart.service.utility.OBUtility;

public class OBCLIParserF5V11 extends OBCLIParserF5 {
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

	/*
	 * [root@bigip1:Active:Standalone] config # tmsh show net arp
	 * 
	 * -----------------------------------------------------------------------------
	 * ------------ Net::Arp Name Address HWaddress Vlan Expire-in-sec Status
	 * -----------------------------------------------------------------------------
	 * ------------ 10.10.0.101 10.10.0.101 4:f4:bc:c:22:d1 /Common/int 82 resolved
	 * 10.10.0.210 10.10.0.210 0:8:2:6a:a8:dd /Common/int 172 resolved 172.172.2.50
	 * 172.172.2.50 c8:a:a9:ef:8b:e0 /Common/vlan_x15 2 resolved 172.172.2.111
	 * 172.172.2.111 0:1e:67:99:63:e4 /Common/vlan_x15 148 resolved 192.168.0.120
	 * 192.168.0.120 3c:97:e:18:aa:34 /Common/ext 120 resolved
	 */
	private final static String SUFFIX_ARP_START = "Expire-in-sec";

	public ArrayList<OBDtoArpInfo> parseInfoArp(String input) throws OBException {
		ArrayList<OBDtoArpInfo> retVal = new ArrayList<OBDtoArpInfo>();
		Integer strIndex = input.indexOf(SUFFIX_ARP_START);
		String startInput = input;
		if (strIndex >= 0) {
			startInput = input.substring(strIndex + SUFFIX_ARP_START.length());
		}
		String lines[] = startInput.split("\n");
		for (String line : lines) {
			line = OBParser.convertMultipleSpaces2SingleSpace(line);
			if (line.isEmpty()) {
				continue;
			}
			String element[] = line.split(" "); // " "(공백으로 구분한다. 위의 Parser에서 멀티 스패이스로 공백처리 이미 함)
			if (element.length != 6) {
				continue;
			}
			OBDtoArpInfo obj = new OBDtoArpInfo();
			obj.setDstIPAddr(element[0]);
			obj.setMacAddr(element[2]);
			obj.setVlanInfo(element[3]);
			obj.setPortNum("");
			retVal.add(obj);
		}
		return retVal;
	}

//     root@(oblab)(cfg-sync Standalone)(Active)(/Common)(tmos)# show net fdb 
//
//     -----------------------------------------------
//     Net::FDB
//     VLAN  Mac Address        Member         Dynamic
//     -----------------------------------------------
//     int   00:01:d7:d6:ef:83  interface:1.4  yes
//     int   00:1b:25:77:fb:00  interface:1.4  yes
//
//     root@(oblab)(cfg-sync Standalone)(Active)(/Common)(tmos)# 

	public ArrayList<OBDtoFdbInfo> parseInfoFdb(String input) throws OBException {
		ArrayList<OBDtoFdbInfo> retVal = new ArrayList<OBDtoFdbInfo>();
		String lines[] = input.split("\n");
		for (String line : lines) {
			line = OBParser.convertMultipleSpaces2SingleSpace(line);
			if (line.isEmpty()) {
				continue;
			}
			String element[] = line.split(" "); // " "(공백으로 구분한다. 위의 Parser에서 멀티 스패이스로 공백처리 이미 함)
			if (element.length != 4) {
				continue;
			}
			String port[] = element[2].split(":");
			OBDtoFdbInfo obj = new OBDtoFdbInfo();
			obj.setMacAddress(element[1]);
			obj.setPort(port[1]);
			obj.setVlan(element[0]);
			retVal.add(obj);
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
// 	    	172.172.2.111:36934  172.172.2.1:161  172.172.2.111:10717  127.0.0.1:161  udp  62  (tmm: 4)  none
// 	    	172.172.2.111:35819  172.172.2.1:161  172.172.2.111:37358  127.0.0.1:161  udp  122  (tmm: 7)  none
// 	    	172.172.2.111:35371  172.172.2.1:161  172.172.2.111:23031  127.0.0.1:161  udp  247  (tmm: 6)  none
			line = OBParser.convertMultipleSpaces2SingleSpace(line);
			OBDtoFaultSessionInfo obj = new OBDtoFaultSessionInfo();

			if (line.isEmpty()) {
				continue;
			}
			String element[] = line.split(" "); // " "으로 구분.
			if (element.length != 9) {
				continue;
			}

			obj.setAdcIndex(adcIndex);

			// src info - index0
			String srcInfo[] = element[0].split(":");
			if (srcInfo.length == 2) {
				obj.setSrcIP(srcInfo[0]);
				obj.setSrcPort(convertPort((srcInfo[1]))); // 특수 문자의 경우 exception발생 double은 발생 안함.
			} else {
				obj.setSrcIP(srcInfo[0]);
				obj.setSrcPort("0");
			}
			// dst info
			String dstInfo[] = element[1].split(":");
			if (dstInfo.length == 2) {
				obj.setDstIP(dstInfo[0]);
				obj.setDstPort(convertPort(dstInfo[1]));
			} else {
				obj.setDstIP(dstInfo[0]);
				obj.setDstPort("0");
			}

			// real ip info
			String realIPInfo[] = element[3].split(":");
			if (realIPInfo.length == 2) {
				obj.setRealIP(realIPInfo[0]);
				obj.setRealPort(convertPort(realIPInfo[1]));
			} else {
				obj.setRealIP(realIPInfo[0]);
				obj.setRealPort("0");
			}
			// protocol
			if (element[4].length() > 0)
				protocol = element[4];

			// aging time
			if (element[4].length() > 0)
				obj.setAgingTime(Integer.parseInt(element[5]));// 초단위.

			// 6, 7 index
			String cpuInfo = element[6].replace("(", "");
			cpuInfo += " " + element[7].replace(")", "");
			obj.setSpNumber(cpuInfo);

			try {
				if (!OBUtility.isInteger(protocol)) {
					if (protocol.equals(OBDefine.STRING_TO_PROTOCOL_TCP)) {
						obj.setProtocol(OBDefine.INT_TO_PROTOCOL_TCP);
					}
					if (protocol.equals(OBDefine.STRING_TO_PROTOCOL_UDP)) {
						obj.setProtocol(OBDefine.INT_TO_PROTOCOL_UDP);
					}
					if (protocol.equals(OBDefine.STRING_TO_PROTOCOL_ICMP)) {
						obj.setProtocol(OBDefine.INT_TO_PROTOCOL_ICMP);
					}
				} else {
					obj.setProtocol(Integer.parseInt(protocol));
				}
			} catch (NumberFormatException e) {
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						"NumberFormatException: " + e.getMessage());
			} catch (Exception e) {
				throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, "Exception: " + e.getMessage());
			}
			retVal.add(obj);
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
	private final static String SUFFIX_ALLOW_ALL = "All";//
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
}