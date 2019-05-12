package kr.openbase.adcsmart.service.impl.f5.handler;

import java.util.ArrayList;

import kr.openbase.adcsmart.service.dto.fault.OBDtoSessionSearchOption;
import kr.openbase.adcsmart.service.utility.OBException;

public class OBAdcF5HandlerV11 extends OBAdcF5Handler {
	public OBAdcF5HandlerV11() {
	}

	private final static String CMND_STRG_INFO_VLAN = "tmsh show net vlan";
	private final static String CMND_STRG_INFO_INTERFACE = "tmsh show net interface";
	private final static String CMND_STRG_INFO_STG = "tmsh show net stp";
	private final static String CMND_STRG_INFO_TRUNK = "tmsh show net trunk";
	private final static String CMND_STRG_INFO_FAILOVER = "tmsh show sys failover";
	private final static String CMND_STRG_INFO_SELFIP = "tmsh show net self";
	private final static String CMND_STRG_INFO_SESSION = "tmsh show sys conn";
	private final static String CMND_STRG_INFO_ARP = "tmsh show net arp";
	private final static String CMND_STRG_INFO_FDB = "tmsh show net fdb";
	private final static String CMND_STRG_LIST_SYSLOG = "tmsh list sys syslog";
	private final static String CMND_STRG_LIST_HTTPS = "tmsh list sys httpd";
	private final static String CMND_STRG_LIST_SSHD = "tmsh list sys sshd";

	private final static String CMND_CMND_CLIENT_IP = "cs-client-addr";
	private final static String CMND_CMND_SERVER_IP = "cs-server-addr";
	private final static String CMND_CMND_REAL_IP = "ss-server-addr";
	private final static String CMND_CMND_CLIENT_PORT = "cs-client-port";
	private final static String CMND_CMND_SERVER_PORT = "cs-server-port";
	private final static String CMND_CMND_REAL_PORT = "ss-server-port";

	public String cmndListSshd() throws OBException {
		return sshSendCommand(CMND_STRG_LIST_SSHD);
	}

	public String cmndListHttps() throws OBException {
		return sshSendCommand(CMND_STRG_LIST_HTTPS);
	}

	public String cmndListSyslog() throws OBException {
		return sshSendCommand(CMND_STRG_LIST_SYSLOG);
	}

//     public static void  main(String[] args)
//     {
//          try
//          {
//               OBAdcF5HandlerV11 handler = new OBAdcF5HandlerV11();
//               handler.setConnectionInfo("192.168.200.120", "root", "default", OBDefine.SERVICE.SSH);
//               handler.sshLogin();
//
////             String retVal = "";
////             retVal = handler.cmndInfoVlan();
////             System.out.println(retVal);
////             retVal = handler.cmndInfoDate();
////             System.out.println(retVal);
//               ArrayList<OBDtoSessionSearchOption> seach = new ArrayList<OBDtoSessionSearchOption>();
//               
//               OBDtoSessionSearchOption sourcIP = new OBDtoSessionSearchOption();
//               sourcIP.setContent("172.172.2.110");
//               sourcIP.setType(OBDtoSessionSearchOption.OPTION_TYPE_SRC_IP);
//               seach.add(sourcIP);
//            
////             OBDtoSessionSearchOption destinationcIP = new OBDtoSessionSearchOption();
////             destinationcIP.setContent("127.0.0.1");
////             destinationcIP.setType(OBDtoSessionSearchOption.OPTION_TYPE_DST_IP);
////             seach.add(destinationcIP);
//               OBDtoSessionSearchOption protocol = new OBDtoSessionSearchOption();
//               protocol.setContent("udp");
//               protocol.setType(OBDtoSessionSearchOption.OPTION_TYPE_PROTOCOL);
//               seach.add(protocol);
//               
////             String cip = "172.172.2.110";
////             String sip = "192.168.200.120";
//
////             String cm = CMND_STRG_INFO_SESSION + " " + CMND_STRG_CLIENT_ADDRESS + " "+ cip + " "+ CMND_STRG_SERVER_ADDRESS +" " + sip ;
////             String cm  = CMND_STRG_INFO_SESSION + SPACE + CMND_CMND_CLIENTSIDE_IP + SPACE + cip + SPACE + CMND_CMND_SERVERSIDE_IP + SPACE + sip;
//             
//               String ret =  handler.sessionSearchInfo(seach);
//
//                ArrayList<OBDtoFaultSessionInfo> list = new OBCLIParserF5().parseSessionInfoList(1,ret);
//                ArrayList<OBDtoFaultSessionInfo> tolist = new OBCLIParserF5().remanufactoringParsedSessionList(1,list, seach);              
//                System.out.println("tolist" + tolist);
//                handler.disconnect();
//          }
//          catch(Exception e)
//          {
//              e.getMessage();
//          }
//     }
	public String sessionSearchInfo(ArrayList<OBDtoSessionSearchOption> seachKeyList) throws OBException {
		String srcIP = "";
		String dstIP = "";
		String srcPort = "";
		String dstPort = "";
		String realPort = "";
		String realIP = "";

		String cmnd = "";
		// SRC IP와 DST IP는 필수로 입력을 받고 나머지 키워드는 검색된 정보를 filtering 하여 처리한다.
		for (OBDtoSessionSearchOption option : seachKeyList) {
			if (option.getType() == OBDtoSessionSearchOption.OPTION_TYPE_DST_IP) {
				dstIP = option.getContent();
			} else if (option.getType() == OBDtoSessionSearchOption.OPTION_TYPE_SRC_IP) {
				srcIP = option.getContent();
			} else if (option.getType() == OBDtoSessionSearchOption.OPTION_TYPE_DST_PORT) {
				dstPort = option.getContent();
			} else if (option.getType() == OBDtoSessionSearchOption.OPTION_TYPE_SRC_PORT) {
				srcPort = option.getContent();
			} else if (option.getType() == OBDtoSessionSearchOption.OPTION_TYPE_REAL_PORT) {
				realPort = option.getContent();
			} else if (option.getType() == OBDtoSessionSearchOption.OPTION_TYPE_REAL_IP) {
				realIP = option.getContent();
			}
		}
		cmnd = String.format("%s", CMND_STRG_INFO_SESSION);

		if (srcIP != null && !srcIP.isEmpty()) {
			cmnd += String.format(" %s %s", CMND_CMND_CLIENT_IP, srcIP);
		}
		if (dstIP != null && !dstIP.isEmpty()) {
			cmnd += String.format(" %s %s", CMND_CMND_SERVER_IP, dstIP);
		}
		if (realIP != null && !realIP.isEmpty()) {
			cmnd += String.format(" %s %s", CMND_CMND_REAL_IP, realIP);
		}
		if (srcPort != null && !srcPort.isEmpty()) {
			cmnd += String.format(" %s %s", CMND_CMND_CLIENT_PORT, srcPort);
		}
		if (dstPort != null && !dstPort.isEmpty()) {
			cmnd += String.format(" %s %s", CMND_CMND_SERVER_PORT, dstPort);
		}
		if (realPort != null && !realPort.isEmpty()) {
			cmnd += String.format(" %s %s", CMND_CMND_REAL_PORT, realPort);
		}
		return sshSendCommand(cmnd);
	}

	public String cmndInfoArp() throws OBException {
		return sshSendCommand(CMND_STRG_INFO_ARP);
	}

	public String cmndInfoFdb() throws OBException {
		return sshSendCommand(CMND_STRG_INFO_FDB);
	}

	public String cmndInfoFailover() throws OBException {
		return sshSendCommand(CMND_STRG_INFO_FAILOVER);
	}

	public String cmndInfoSession() throws OBException {
		return sshSendCommand(CMND_STRG_INFO_SESSION);
	}

	public String cmndInfoVlan() throws OBException {
		return sshSendCommand(CMND_STRG_INFO_VLAN);
	}

	public String cmndInfoInterface() throws OBException {
		return sshSendCommand(CMND_STRG_INFO_INTERFACE);
	}

	public String cmndInfoStg() throws OBException {
		return sshSendCommand(CMND_STRG_INFO_STG);
	}

	public String cmndInfoTrunk() throws OBException {
		return sshSendCommand(CMND_STRG_INFO_TRUNK);
	}

	public String cmndInfoSelfIP() throws OBException {
		return sshSendCommand(CMND_STRG_INFO_SELFIP);
	}
}