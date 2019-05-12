package kr.openbase.adcsmart.service.impl.pas.handler;

import java.util.ArrayList;
import java.util.HashMap;

import kr.openbase.adcsmart.service.impl.pas.dto.OBDtoAdcHealthCheckPAS;
import kr.openbase.adcsmart.service.impl.pas.dto.OBDtoAdcPoolMemberPAS;
import kr.openbase.adcsmart.service.impl.pas.dto.OBDtoAdcPoolPAS;
import kr.openbase.adcsmart.service.impl.pas.dto.OBDtoAdcVServerPAS;
import kr.openbase.adcsmart.service.impl.pas.handler.dto.OBDtoGatewayInfoPAS;
import kr.openbase.adcsmart.service.impl.pas.handler.dto.OBDtoHWStatPAS;
import kr.openbase.adcsmart.service.impl.pas.handler.dto.OBDtoInterfaceInfoPAS;
import kr.openbase.adcsmart.service.impl.pas.handler.dto.OBDtoLoggingBufferPAS;
import kr.openbase.adcsmart.service.impl.pas.handler.dto.OBDtoNTPInfoPAS;
import kr.openbase.adcsmart.service.impl.pas.handler.dto.OBDtoParserInfoPAS;
import kr.openbase.adcsmart.service.impl.pas.handler.dto.OBDtoPortInfoPAS;
import kr.openbase.adcsmart.service.impl.pas.handler.dto.OBDtoPortStatPAS;
import kr.openbase.adcsmart.service.impl.pas.handler.dto.OBDtoResourceInfoPAS;
import kr.openbase.adcsmart.service.impl.pas.handler.dto.OBDtoSnmpInfoPAS;
import kr.openbase.adcsmart.service.impl.pas.handler.dto.OBDtoSyslogInfoPAS;
import kr.openbase.adcsmart.service.impl.pas.handler.dto.OBDtoSystemInfoPAS;
import kr.openbase.adcsmart.service.impl.pas.handler.dto.OBDtoVLanInfoPAS;
import kr.openbase.adcsmart.service.snmp.pas.dto.OBDtoAdcConfigSlbPAS;
import kr.openbase.adcsmart.service.utility.OBCommon;
import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.service.utility.OBParser;

/*
 * CLI 명령에 의해 출력된 데이터를 파싱한다.
 */
public class OBCLIParserPAS {
	private final static String SUFFIX_MORE = "More--[m\r";

	private final static int PARSE_STATE_INIT = 0;
	private final static int PARSE_STATE_SLB = 1;
	private final static int PARSE_STATE_REAL = 2;
	private final static int PARSE_STATE_HEALTH = 3;

	public OBCLIParserPAS() {
//		healthList = new ArrayList<OBDtoHealthCheckInfoPAS>();
//		vsList = new ArrayList<OBDtoVirtServerInfoPAS>();
//		realList = new ArrayList<OBDtoRealServerInfoPAS>();
	}

	private String removeMoreText(String line) {
		String retVal = line;
		int strIndex = 0;
		strIndex = line.indexOf(SUFFIX_MORE);
		if (strIndex >= 0) {
			retVal = line.substring(strIndex + SUFFIX_MORE.length());
		}
		return retVal;
	}

//	public static void main(String [] args) 
//	{
//		try
//		{
////			String input = "Current configuration (ver 10.7.60):\n"+
////							"!\n"+
////							"! SLB configuration\n"+
////							"!\n"+
////							"! Define SLB service 'yyh_test_pio1818'\n"+
////							"slb yyh_test_pio1818\n"+
////							"  priority 50\n"+
////							"  slb-session-timeout 0\n"+
////							"  slb-unassured-timeout 0\n"+
////							"  slb-close-timeout 0\n"+
////							"  lb-method rr\n"+
////							"  sticky 60\n"+
////							"  sticky source-subnet 255.255.255.255\n"+
////							"  vip 21.21.21.255\n"+
////							"  vport tcp:80\n"+
////							"  natmode dest-nat\n"+
////							"  no session-sync\n"+
////							"  no backup\n"+
////							"  fail-skip disable\n"+
////							"  enable\n"+
////							"  apply\n"+
////							"! Define Dynamic-Weight of SLB service 'yyh_test_pio1818'\n"+
////							"! Define Reals of SLB service 'yyh_test_pio1818'\n"+
////							"! Define Healthcheck of SLB service 'yyh_test_pio1818'\n"+
////							"  ..\n"+
////							"! Define SLB service 'web_80'\n"+
////							"slb web_80\n"+
////							"  priority 50\n"+
////							"  slb-session-timeout 0\n"+
////							"  slb-unassured-timeout 0\n"+
////							"  slb-close-timeout 0\n"+
////							"  lb-method rr\n"+
////							"  sticky 60\n"+
////							"  sticky source-subnet 255.255.255.255\n"+
////							"  vip 192.168.201.116\n"+
////							"  vport tcp:80\n"+
////							"  natmode dest-nat\n"+
////							"  no session-sync\n"+
////							"  no backup\n"+
////							"  fail-skip disable\n"+
////							"  enable\n"+
////							"  apply\n"+
////							"! Define Dynamic-Weight of SLB service 'web_80'\n"+
////							"! Define Reals of SLB service 'web_80'\n"+
////							"  real 1\n"+
////							"    name r1\n"+
////							"    rip 192.168.199.41\n"+
////							"    rport 80\n"+
////							"    weight 1\n"+
////							"    graceful-shutdown disable\n"+
////							"    graceful-shutdown persist disable\n"+
////							"    max-connection 0\n"+
////							"    enable\n"+
////							"    apply\n"+
////							"  real 2\n"+
////							"    name r2\n"+
////							"    rip 192.168.199.42\n"+
////							"    rport 80\n"+
////							"    weight 1\n"+
////							"    graceful-shutdown disable\n"+
////							"    graceful-shutdown persist disable\n"+
////							"    max-connection 0\n"+
////							"    enable\n"+
////							"    apply\n"+
////							"! Define Healthcheck of SLB service 'web_80'\n"+
////							"  health 1\n"+
////							"    type tcp\n"+
////							"    timeout 3\n"+
////							"    interval 5\n"+
////							"    retry 3\n"+
////							"    recover 0\n"+
////							"    port 80\n"+
////							"    half-open disable\n"+
////							"    enable\n"+
////							"    apply\n"+
////							"  ..\n"+
////							"! Define SLB service 'aaatest'";
//	
////			System.out.println(new OBCLIParserPAS().getSlbInfo(1, input));
//			
//			
//			OBAdcPASHandler handler = new OBAdcPASHandler("192.168.200.120", "root", "admin", OBDefine.SERVICE.TELNET, OBDefine.SERVICE.TELNET);
//			//handler.enter();
//			handler.login();
//			System.out.println("login OK");
//			
//			String result = handler.cmndDumpcfg();//
//			System.out.println(result);
//			System.out.println("command OK");
//			
//			System.out.println(new OBCLIParserPAS().getSlbInfo(2, result));
//			
//			handler.disconnect();
//			System.out.println("logout OK");
//			
////			OBDateTime.Sleep(1000);
////			OBAdcPAS handler2 = new OBAdcPAS("192.168.200.120", "root", "admin");
////			handler2.login();
////			System.out.println("login OK");
////			
////			result = handler2.cmndDumpcfg();//
//////			System.out.println(result);
////			System.out.println("command OK");
////
////			handler2.disconnect();
////			System.out.println("logout OK");
//
//			//			OBCLIParserPAS pas = new OBCLIParserPAS();
////			String inText = "================================================================================\n"+
////							"SLB Configuration \n"+
////							"------------------------------------------------------------------------------ \n"+
////							"  Name   Priority LB Method Session-sync Status  Health  VIP             Protocol Vport\n"+    
////							"  web_80 50       rr        disable      enable  ACT     192.168.201.111 tcp      80     \n"+  
////							"  ykkim1 50       rr        disable      enable  INACT   10.10.10.111    tcp      80       \n"+
////							"                                                         10.10.10.112    tcp      8080,9999\n"+
////							"  ykkim2 50       rr		 disable      disable UNKNOWN 192.168.201.112 tcp      80       \n"+
////							"                                                         192.168.201.113 tcp      8080     \n"+
////							"  ykkim3 50       rr        disable      enable  ACT     192.168.201.112 tcp      8080     \n"+
////							"================================================================================\n";
////			
////			ArrayList<OBDtoVSStatusPAS> list = pas.getVSStatus(inText);
////			System.out.println(list);
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}
//	
	/*
	 * ! ! Health-Check configuration ! health-check 1 type tcp timeout 3 interval 5
	 * retry 3 recover 0 status enable port 80 increase-icmp-id disable uri /
	 * packets 1 status-code 200 apply exit health-check 2 type icmp timeout 3
	 * interval 5 retry 3 recover 0 status disable port 0 increase-icmp-id disable
	 * uri / packets 1 status-code 200 apply exit ! ! Real configuration ! real 1
	 * rip 192.168.199.41 name r1 rport 80 priority 0 weight 1 graceful-shutdown
	 * disable max-connection 0 pool-size 10000 pool-age 3600 pool-reuse 100
	 * pool-srcmask 32 status enable ip-ver ipv4 apply exit
	 * 
	 * real 2 rip 192.168.199.42 name r2 rport 80 priority 0 weight 1
	 * graceful-shutdown disable max-connection 0 pool-size 10000 pool-age 3600
	 * pool-reuse 100 pool-srcmask 32 status enable ip-ver ipv4 apply exit
	 * 
	 * ! ! Slb configuration ! slb web_80 priority 50 nat-mode dnat lb-method rr
	 * session-sync disable fail-skip enable status enable health-check 1 vip
	 * 192.168.201.111 protocol tcp vport 80 sticky time 60 sticky source-subnet
	 * 255.255.255.255 real 1 real 2 slow-start rate 5 slow-start timer 60 apply
	 * 
	 * slb ykkim1 priority 50 nat-mode dnat lb-method rr session-sync disable
	 * fail-skip disable status enable health-check 1,2 vip 10.10.10.111 protocol
	 * tcp vport 80 vip 10.10.10.112 protocol tcp vport 8080,9999 sticky time 60
	 * sticky source-subnet 255.255.255.255 slow-start rate 5 slow-start timer 60
	 * apply
	 * 
	 * slb ykkim2 priority 50 nat-mode dnat lb-method rr session-sync disable
	 * fail-skip disable status disable health-check 1 vip 192.168.201.112 protocol
	 * tcp vport 80 vip 192.168.201.113 protocol tcp vport 8080 sticky time 60
	 * sticky source-subnet 255.255.255.255 real 1 real 2 slow-start rate 5
	 * slow-start timer 60 apply
	 */

//	private ArrayList<OBDtoHealthCheckInfoPAS> getSlbInfoHealthCheck(String input) throws OBException
//	{
//		return healthList;
//	}
//	
//	private ArrayList<OBDtoVirtServerInfoPAS> getSlbInfoVirtServer(String input) throws OBException
//	{
//		return vsList;
//	}
//	
//	private ArrayList<OBDtoRealServerInfoPAS> getSlbInfoRealServer(String input) throws OBException
//	{
//		return realList;
//	}

//    final static int PARSE_STATE_INIT = 0;
//    final static int PARSE_STATE_SERV = 1;
//    final static int PARSE_STATE_SLB = 2;
//    final static int PARSE_STATE_REAL = 3;
//    final static int PARSE_STATE_HEALTH = 4;
//    
	private final static String SUFFIX_SLB_SERVICE = "! Define SLB service ";
	private final static String SUFFIX_REAL_SERVICE = "! Define Reals of SLB ";
	private final static String SUFFIX_HEALTH_CHECK = "! Define Healthcheck ";
	private final static String SUFFIX_END_SLB = "..";

	private int checkCurrentState(int currState, String input) {
		int strIndex = 0;
		strIndex = input.indexOf(SUFFIX_SLB_SERVICE);
		if (strIndex >= 0) {
			return PARSE_STATE_SLB;
		}

		strIndex = input.indexOf(SUFFIX_REAL_SERVICE);
		if (strIndex >= 0) {
			return PARSE_STATE_REAL;
		}

		strIndex = input.indexOf(SUFFIX_HEALTH_CHECK);
		if (strIndex >= 0) {
			return PARSE_STATE_HEALTH;
		}

		strIndex = input.indexOf(SUFFIX_END_SLB);
		if (strIndex >= 0) {
			return PARSE_STATE_INIT;
		}

		return currState;
	}

	private OBDtoAdcVServerPAS vsObj = null;
	private OBDtoAdcHealthCheckPAS healthObj = null;
	private OBDtoAdcPoolMemberPAS memObj = null;

	private final static String SUFFIX_HEALTH_ID = "health ";
	private final static String SUFFIX_HEALTH_TYPE = "type ";
	private final static String SUFFIX_HEALTH_ENABLE = "enable";
	private final static String SUFFIX_HEALTH_DISALBE = "disable";
	private final static String SUFFIX_HEALTH_APPLY = "apply";

	private void procHealthState(String line, HashMap<String, OBDtoAdcHealthCheckPAS> healthMap) throws OBException {
		try {
			String rmWhiteSpace = OBParser.removeFirstWhitespace(line);
			int strIndex = 0;
			// id
			strIndex = rmWhiteSpace.indexOf(SUFFIX_HEALTH_ID);
			if (strIndex >= 0) {
				String input = rmWhiteSpace.substring(strIndex + SUFFIX_HEALTH_ID.length());
				// health 1
				String element[] = input.split(" ");// space를 기준으로 분리.
				if (element.length != 1) {
					return;
				}
				this.healthObj = new OBDtoAdcHealthCheckPAS();// health 정보 시작.

				this.healthObj.setId(Integer.parseInt(element[0]));
				this.healthObj.setName(this.vsObj.getName());
				String dbIndex = OBCommon.makeHealthDbIndexPAS(this.vsObj.getAdcIndex(), this.vsObj.getDbIndex());
				this.healthObj.setDbIndex(dbIndex);
				return;
			}

			// type
			strIndex = rmWhiteSpace.indexOf(SUFFIX_HEALTH_TYPE);
			if (strIndex >= 0) {
				String input = rmWhiteSpace.substring(strIndex + SUFFIX_HEALTH_TYPE.length());
				// type tcp
				String element[] = input.split(" ");// space를 기준으로 분리.
				if (element.length != 1) {
					return;
				}
				this.healthObj.setType(OBCommon.convertHealthCheckTypeString2Integer(element[0]));
				return;
			}

			// enable
			strIndex = rmWhiteSpace.indexOf(SUFFIX_HEALTH_ENABLE);
			if (strIndex >= 0) {
				// enable
				this.healthObj.setState(OBDefine.STATE_ENABLE);
				return;
			}

			// disalbe
			strIndex = rmWhiteSpace.indexOf(SUFFIX_HEALTH_DISALBE);
			if (strIndex >= 0) {
				// disable
				this.healthObj.setState(OBDefine.STATE_DISABLE);
				return;
			}

			// apply
			strIndex = rmWhiteSpace.indexOf(SUFFIX_HEALTH_APPLY);
			if (strIndex >= 0) {
				if (this.vsObj.getPool().getHealthCheckInfo() == null) {
					this.vsObj.getPool().setHealthCheckInfo(this.healthObj);
				}
				String mapKey = this.vsObj.getAdcIndex() + "_" + this.vsObj.getName() + "_" + this.healthObj.getId();
				healthMap.put(mapKey, this.healthObj);
				this.healthObj = null;
				return;
			}
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

	private final static String SUFFIX_REAL_ID = "real ";
	private final static String SUFFIX_REAL_NAME = "name ";
	private final static String SUFFIX_REAL_RIP = "rip ";
	private final static String SUFFIX_REAL_RPORT = "rport ";
	private final static String SUFFIX_REAL_ENABLE = "enable";
	private final static String SUFFIX_REAL_DISALBE = "disable";
	private final static String SUFFIX_REAL_APPLY = "apply";

	private void procRealState(String line) throws OBException {
		String rmWhiteSpace = OBParser.removeFirstWhitespace(line);
		int strIndex = 0;
		// id
		strIndex = rmWhiteSpace.indexOf(SUFFIX_REAL_ID);
		if (strIndex >= 0) {
			String input = rmWhiteSpace.substring(strIndex + SUFFIX_REAL_ID.length());
			// real 1
			String element[] = input.split(" ");// space를 기준으로 분리.
			if (element.length != 1) {
				return;
			}
			this.memObj.setId(Integer.parseInt(element[0]));
			String dbIndex = OBCommon.makeNodeIndexPAS(this.vsObj.getAdcIndex(), this.vsObj.getPool().getName(),
					this.memObj.getId());
			this.memObj.setDbIndex(dbIndex);
			return;
		}
		// name
		strIndex = rmWhiteSpace.indexOf(SUFFIX_REAL_NAME);
		if (strIndex >= 0) {
			String input = rmWhiteSpace.substring(strIndex + SUFFIX_REAL_NAME.length());
			// name real1
			String element[] = input.split(" ");// space를 기준으로 분리.
			if (element.length != 1) {
				return;
			}
			this.memObj.setName(element[0]);
			return;
		}

		// rip
		strIndex = rmWhiteSpace.indexOf(SUFFIX_REAL_RIP);
		if (strIndex >= 0) {
			String input = rmWhiteSpace.substring(strIndex + SUFFIX_REAL_RIP.length());
			// name real1
			String element[] = input.split(" ");// space를 기준으로 분리.
			if (element.length != 1) {
				return;
			}
			this.memObj.setIpAddress(element[0]);
			return;
		}

		// rport
		strIndex = rmWhiteSpace.indexOf(SUFFIX_REAL_RPORT);
		if (strIndex >= 0) {
			String input = rmWhiteSpace.substring(strIndex + SUFFIX_REAL_RPORT.length());
			// name real1
			String element[] = input.split(" ");// space를 기준으로 분리.
			if (element.length != 1) {
				return;
			}
			this.memObj.setPort(Integer.parseInt(element[0]));
			return;
		}

		// enable
		strIndex = rmWhiteSpace.indexOf(SUFFIX_REAL_ENABLE);
		if (strIndex >= 0) {
			// enable
			this.memObj.setState(OBDefine.STATE_ENABLE);
			return;
		}

		// disalbe
		strIndex = rmWhiteSpace.indexOf(SUFFIX_REAL_DISALBE);
		if (strIndex >= 0) {
			// disable
			this.memObj.setState(OBDefine.STATE_DISABLE);
			return;
		}

		// apply
		strIndex = rmWhiteSpace.indexOf(SUFFIX_REAL_APPLY);
		if (strIndex >= 0) {
			if (this.vsObj.getPool().getMemberList() == null) {
				ArrayList<OBDtoAdcPoolMemberPAS> memList = new ArrayList<OBDtoAdcPoolMemberPAS>();
				this.vsObj.getPool().setMemberList(memList);
			}
			this.vsObj.getPool().getMemberList().add(this.memObj);
			this.memObj = new OBDtoAdcPoolMemberPAS();// 신규 등록..
			return;
		}
	}

	private Integer convertProtocol(String port) {
		if (port.compareTo(OBDefine.PROTOCOL_TCP_STRING) == 0)
			return OBDefine.PROTOCOL_TCP;
		if (port.compareTo(OBDefine.PROTOCOL_UDP_STRING) == 0)
			return OBDefine.PROTOCOL_UDP;
		if (port.compareTo(OBDefine.PROTOCOL_ICMP_STRING) == 0)
			return OBDefine.PROTOCOL_ICMP;
		if (port.compareTo(OBDefine.PROTOCOL_ALL_STRING) == 0)
			return OBDefine.PROTOCOL_ALL;
		return OBDefine.PROTOCOL_TCP;
	}

	private Integer convertLBMethod(String method) {
		if (method.compareTo("rr") == 0)
			return OBDefine.LB_METHOD_ROUND_ROBIN;
		if (method.compareTo("lc") == 0)
			return OBDefine.LB_METHOD_LEAST_CONNECTION_MEMBER;
		if (method.compareTo("hash") == 0)
			return OBDefine.LB_METHOD_HASH;
		return OBDefine.COMMON_NOT_ALLOWED; // 관리 대상외 항목은 Not Allowed로 못 건드리게 한다.
	}

	private final static String SUFFIX_SLB_NAME = "slb ";
	private final static String SUFFIX_SLB_VIP = "vip ";
	private final static String SUFFIX_SLB_VPORT = "vport ";
	private final static String SUFFIX_SLB_LBMETHOD = "lb-method ";
	private final static String SUFFIX_SLB_ENABLE = "enable";
	private final static String SUFFIX_SLB_DISALBE = "disable";

	// slb 시작 문구를 찾는다.
	private void procSlbState(String line) throws OBException {
		String rmWhiteSpace = OBParser.removeFirstWhitespace(line);
		int strIndex = 0;
		// name
		strIndex = rmWhiteSpace.indexOf(SUFFIX_SLB_NAME);
		if (strIndex >= 0) {
			String input = rmWhiteSpace.substring(strIndex + SUFFIX_SLB_NAME.length());
			// slb 이름을 추출한다. ex: slb yyh_test_pio1818
			String element[] = input.split(" ");// space를 기준으로 분리.
			if (element.length != 1) {
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						String.format("parse error(vsName). line:%s", input));
			}
			this.vsObj.setName(element[0]);
			// pool 이름은 vs 이름과 동일하게 한다.
			this.vsObj.getPool().setName(element[0]);

			// vs db index 설정.
			String vsDbIndex = OBCommon.makeVSIndexPAS(this.vsObj.getAdcIndex(), this.vsObj.getName());
			this.vsObj.setDbIndex(vsDbIndex);

			// pool db index 설정.
			String poolDbIndex = OBCommon.makePoolIndex(this.vsObj.getAdcIndex(), this.vsObj.getPool().getName());
			this.vsObj.getPool().setDbIndex(poolDbIndex);
			return;
		}

		// lb-method
		strIndex = rmWhiteSpace.indexOf(SUFFIX_SLB_LBMETHOD);
		if (strIndex >= 0) {
			String input = rmWhiteSpace.substring(strIndex + SUFFIX_SLB_LBMETHOD.length());
			// lb-method rr
			String element[] = input.split(" ");// space를 기준으로 분리.
			if (element.length != 1) {
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						String.format("parse error(lbmethod). line:%s", input));
			}
			this.vsObj.getPool().setLbMethod(convertLBMethod(element[0]));
			return;
		}

		// vip
		strIndex = rmWhiteSpace.indexOf(SUFFIX_SLB_VIP);
		if (strIndex >= 0) {
			String input = rmWhiteSpace.substring(strIndex + SUFFIX_SLB_VIP.length());
			// vip 21.21.21.255
			String element[] = input.split(" ");// space를 기준으로 분리.
			if (element.length != 1) {
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						String.format("parse error(vip). line:%s", input));
			}
			this.vsObj.setvIP(element[0]);
			return;
		}

		// vport
		strIndex = rmWhiteSpace.indexOf(SUFFIX_SLB_VPORT);
		if (strIndex >= 0) {
			String input = rmWhiteSpace.substring(strIndex + SUFFIX_SLB_VPORT.length());
			// vport tcp:80
			String element[] = input.split(" ");// space를 기준으로 분리.
			if (element.length != 1) {
				return;
			}

			// proto:port 또는 proto:port,proto:port형태로 전달됨.
			String[] protPort = element[0].split(":");
			if (protPort.length != 2) {
				this.vsObj.setSrvPort(0);
				this.vsObj.setSrvProtocol(convertProtocol(protPort[0]));
				return;
			}
			this.vsObj.setSrvPort(Integer.parseInt(protPort[1]));
			this.vsObj.setSrvProtocol(convertProtocol(protPort[0]));

			return;
		}

		// enable
		strIndex = rmWhiteSpace.indexOf(SUFFIX_SLB_ENABLE);
		if (strIndex >= 0) {
			// enable
			this.vsObj.setState(OBDefine.STATE_ENABLE);
			return;
		}

		// disalbe
		strIndex = rmWhiteSpace.indexOf(SUFFIX_SLB_DISALBE);
		if (strIndex >= 0) {
			// enable
			this.vsObj.setState(OBDefine.STATE_DISABLE);
			return;
		}
	}

//	// service 시작 문구를 찾는다.
//	private int procInitState(int currState, String input)
//	{
//		int strIndex = input.indexOf(SUFFIX_START_SERVICE);
//		if(strIndex>=0)
//			return PARSE_STATE_SERV;
//		
//		return currState;
//	}
	/**
	 * Telnet으로 입력 받은 데이터를 파싱하여 SLB 정보를 추출한다.
	 * 
	 * @param input
	 * @return
	 * @throws OBException
	 */
	public OBDtoAdcConfigSlbPAS getSlbInfo(int adcIndex, String input) throws OBException {
		String lines[] = input.split("\n");
		ArrayList<OBDtoAdcVServerPAS> vsList = new ArrayList<OBDtoAdcVServerPAS>();
		HashMap<String, OBDtoAdcHealthCheckPAS> healthMap = new HashMap<String, OBDtoAdcHealthCheckPAS>();// key:
																											// vsName+id

		int currState = PARSE_STATE_INIT;
		for (String line : lines) {
			if (line.isEmpty())
				continue;
			line = removeMoreText(line);
			int tmpState = checkCurrentState(currState, line);

			switch (tmpState) {
			case PARSE_STATE_INIT:
				if (currState == PARSE_STATE_HEALTH) {// 하나의 slb 구성이 끝난 경우임.
					vsList.add(vsObj);
					currState = tmpState;
					continue;
				}
				break;
			case PARSE_STATE_SLB:// slb 정보 추출 상태.
				if (currState == PARSE_STATE_INIT) {// slb 시작.
					this.vsObj = new OBDtoAdcVServerPAS();
					OBDtoAdcPoolPAS poolObj = new OBDtoAdcPoolPAS();
					ArrayList<OBDtoAdcPoolMemberPAS> memberList = new ArrayList<OBDtoAdcPoolMemberPAS>();
					poolObj.setMemberList(memberList);
					this.vsObj.setPool(poolObj);
					this.vsObj.setAdcIndex(adcIndex);
					currState = tmpState;
					continue;
				}
				procSlbState(line);
				break;
			case PARSE_STATE_REAL:// real 정보 추출 상태.
				if (currState == PARSE_STATE_SLB) {// slb 시작.
					this.memObj = new OBDtoAdcPoolMemberPAS();
					currState = tmpState;
					continue;
				}
				procRealState(line);
				currState = tmpState;
				break;
			case PARSE_STATE_HEALTH:// health 정보 추출 상태.
				if (currState == PARSE_STATE_REAL) {// slb 시작.
//	    			this.healthObj = new OBDtoAdcHealthCheckPAS();
					currState = tmpState;
					continue;
				}
				procHealthState(line, healthMap);
				currState = tmpState;
				break;
			default:
//	    		parseState = procInitState(parseState, line);
//	    		if(parseState == PARSE_STATE_SERV)
//	    			vsObj = new OBDtoAdcVServerPAS();
				break;
			}
		}

		OBDtoAdcConfigSlbPAS retVal = new OBDtoAdcConfigSlbPAS();
		retVal.setVsList(vsList);
		retVal.setHealthList(new ArrayList<OBDtoAdcHealthCheckPAS>(healthMap.values()));

		return retVal;
	}

//	public static void main(String [] args) 
//	{
//		try
//		{
//			OBCLIParserPAS pas = new OBCLIParserPAS();
//			String inText = "================================================================================\n"+
//							"SLB Configuration \n"+
//							"------------------------------------------------------------------------------ \n"+
//							"  Name   Priority LB Method Session-sync Status  Health  VIP             Protocol Vport\n"+    
//							"  web_80 50       rr        disable      enable  ACT     192.168.201.111 tcp      80     \n"+  
//							"  ykkim1 50       rr        disable      enable  INACT   10.10.10.111    tcp      80       \n"+
//							"                                                         10.10.10.112    tcp      8080,9999\n"+
//							"  ykkim2 50       rr		 disable      disable UNKNOWN 192.168.201.112 tcp      80       \n"+
//							"                                                         192.168.201.113 tcp      8080     \n"+
//							"  ykkim3 50       rr        disable      enable  ACT     192.168.201.112 tcp      8080     \n"+
//							"================================================================================\n";
//			
//			ArrayList<OBDtoVSStatusPAS> list = pas.getVSStatus(inText);
//			System.out.println(list);
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}

	private final static String SUFFIX_SYSTEM_PNAME = "Product Name = ";// product name
	private final static String SUFFIX_SYSTEM_SERIAL = "Serial Number = ";// serial number
	private final static String SUFFIX_SYSTEM_VERSION = "Version = ";// version
	private final static String SUFFIX_SYSTEM_UPTIME = "System Uptime = ";// system uptime

	public OBDtoSystemInfoPAS parseSystem(String input) throws OBException {
		OBDtoSystemInfoPAS retVal = new OBDtoSystemInfoPAS();

		String lines[] = input.split("\n");
		for (String line : lines) {
			if (line.isEmpty())
				continue;
			int strIndex = 0;
			// status
			strIndex = line.indexOf(SUFFIX_SYSTEM_PNAME);
			if (strIndex >= 0) {
				String item = line.substring(strIndex + SUFFIX_SYSTEM_PNAME.length());
				retVal.setProductName(item);
				continue;
			}
			strIndex = line.indexOf(SUFFIX_SYSTEM_SERIAL);
			if (strIndex >= 0) {
				String item = line.substring(strIndex + SUFFIX_SYSTEM_SERIAL.length());
				retVal.setSerialNum(item);
				continue;
			}
			strIndex = line.indexOf(SUFFIX_SYSTEM_VERSION);
			if (strIndex >= 0) {
				String item = line.substring(strIndex + SUFFIX_SYSTEM_VERSION.length());
				retVal.setVersion(item);
				continue;
			}
			strIndex = line.indexOf(SUFFIX_SYSTEM_UPTIME);
			if (strIndex >= 0) {
				String item = line.substring(strIndex + SUFFIX_SYSTEM_UPTIME.length());
				retVal.setUpTime(item);
				continue;
			}
		}
		return retVal;
	}

	// Version = 10.7.60 (10.7.60)
	public String parseSwVersion(String input) throws OBException {
		String lines[] = input.split("\n");
		for (String line : lines) {
			if (line.isEmpty())
				continue;
			int strIndex = 0;

			strIndex = line.indexOf(SUFFIX_SYSTEM_VERSION);
			if (strIndex >= 0) {
				String item = line.substring(strIndex + SUFFIX_SYSTEM_VERSION.length());
				return item;
			}

		}
		return "";
	}

//	PAS# show ip interface 
//	-------------------------------------------------------------
//	IP Address                                                   
//	-------------------------------------------------------------
//	Interface Name = mgmt
//	IP State       = up
//	MAC Address    = 00:06:c4:70:1d:ba
//	IP Address     = 192.168.100.1/24 Broadcast 192.168.100.255
//	MTU            = 1500
//	-------------------------------------------------------------
//	Interface Name = default
//	IP State       = down
//	MAC Address    = 00:06:c4:70:1d:bb
//	IP Address     = 
//	MTU            = 1500
//	-------------------------------------------------------------
	private final static String SUFFIX_INTERFACE_NAME = "Interface Name = ";// interface name
	private final static String SUFFIX_INTERFACE_STATUS = "IP State = ";// status
	private final static String SUFFIX_INTERFACE_IPADDRESS = "IP Address = ";// ip addr
	private final static String SUFFIX_INTERFACE_MACADDR = "MAC Address = ";// mac addr

	public ArrayList<OBDtoInterfaceInfoPAS> parseInterface(String input) throws OBException {
		ArrayList<OBDtoInterfaceInfoPAS> retVal = new ArrayList<OBDtoInterfaceInfoPAS>();
		OBDtoInterfaceInfoPAS obj = null;
		String lines[] = input.split("\n");
		for (String line : lines) {
			if (line.isEmpty())
				continue;
			int strIndex = 0;
			// status
			strIndex = line.indexOf(SUFFIX_INTERFACE_NAME);
			if (strIndex >= 0) {
				String item = line.substring(strIndex + SUFFIX_INTERFACE_NAME.length());
				obj = new OBDtoInterfaceInfoPAS();
				obj.setName(item);
			}
			strIndex = line.indexOf(SUFFIX_INTERFACE_STATUS);
			if (strIndex >= 0) {
				String item = line.substring(strIndex + SUFFIX_INTERFACE_STATUS.length());
				if (item.equals("down") == true)
					obj.setStatus(OBDefine.L2_LINK_STATUS_DOWN);
				else
					obj.setStatus(OBDefine.L2_LINK_STATUS_UP);
			}
			strIndex = line.indexOf(SUFFIX_INTERFACE_MACADDR);
			if (strIndex >= 0) {
				String item = line.substring(strIndex + SUFFIX_INTERFACE_MACADDR.length());
				obj.setMacAddr(item);
			}
			strIndex = line.indexOf(SUFFIX_INTERFACE_IPADDRESS);
			if (strIndex >= 0) {
				String item = line.substring(strIndex + SUFFIX_INTERFACE_IPADDRESS.length());
				if (!item.isEmpty()) {
					obj.setIpAddr(item);
					retVal.add(obj);
				}
			}
		}
		return retVal;
	}

//	PAS# show ip route 
//	--------------------------------------------------------------------------
//	Destination        Gateway          Interface  Metric  Category  Uptime
//	--------------------------------------------------------------------------
//	0.0.0.0/0          192.168.200.1    wan        000     Static   
//	192.168.100.0/24   0.0.0.0          mgmt       000     Connected
//	192.168.200.0/24   0.0.0.0          wan        000     Connected
//	192.168.201.0/24   0.0.0.0          lan        000     Connected
//	--------------------------------------------------------------------------
	private final static String SUFFIX_GATEWAY_START = "Destination";// mac addr

	public ArrayList<OBDtoGatewayInfoPAS> parseGateway(String input) throws OBException {
		ArrayList<OBDtoGatewayInfoPAS> retVal = new ArrayList<OBDtoGatewayInfoPAS>();
		String lines[] = input.split("\n");
		boolean startFlag = false;
		for (String line : lines) {
			if (line.isEmpty())
				continue;
			if (startFlag == false) {
				int strIndex = 0;
				// status
				strIndex = line.indexOf(SUFFIX_GATEWAY_START);
				if (strIndex >= 0) {
					startFlag = true;
					continue;
				}
				continue;
			}
			String rmWhiteSpace = OBParser.removeFirstWhitespace(line);
			String element[] = rmWhiteSpace.split(" ");// space를 기준으로 분리.
			if (element.length != 5)
				continue;
			OBDtoGatewayInfoPAS obj = new OBDtoGatewayInfoPAS();
			obj.setDestination(element[0]);
			obj.setGateway(element[1]);
			obj.setInterfaceName(element[2]);
			retVal.add(obj);
		}
		return retVal;
	}

//	PAS# show resources 
//	-----------------------------------------------------------------
//	Management-system resources      |  Packet-processor resources      
//	-----------------------------------------------------------------
//	CPU Usage       =    0.00 %      |  CPU Usage       =    0.00 %
//	-----------------------------------------------------------------
//	Total Memory    = 981168 KB      |  Total Memory    = 2441660 KB
//	Used Memory     =  54004 KB      |  Used Memory     = 1231296 KB
//	Free Memory     = 927164 KB      |  Free Memory     = 1210364 KB
//	Memory Usage    =    5.50 %      |  Memory Usage    =   50.43 %
//	-----------------------------------------------------------------	
	private final static String SUFFIX_RESOURCE_CPU_USAGE = "CPU Usage =";// cpu usage
	private final static String SUFFIX_RESOURCE_TOTAL_MEMORY = "Total Memory =";// cpu usage
	private final static String SUFFIX_RESOURCE_USED_MEMORY = "Used Memory =";// cpu usage
	private final static String SUFFIX_RESOURCE_FREE_MEMORY = "Free Memory =";// cpu usage
	private final static String SUFFIX_RESOURCE_MEM_USAGE = "Memory Usage =";// cpu usage

	public OBDtoResourceInfoPAS parseResources(String input) throws OBException {
		OBDtoResourceInfoPAS retVal = new OBDtoResourceInfoPAS();
		String lines[] = input.split("\n");
		for (String line : lines) {
			if (line.isEmpty())
				continue;
			int strIndex = 0;
			strIndex = line.indexOf(SUFFIX_RESOURCE_CPU_USAGE);
			if (strIndex >= 0) {
				String item = line.substring(strIndex + SUFFIX_RESOURCE_CPU_USAGE.length());
				String rmWhiteSpace = OBParser.removeFirstWhitespace(item);
				String element[] = rmWhiteSpace.split(" ");// space를 기준으로 분리.
				if (element.length != 8)
					continue;
				int mpValue = (int) (Float.parseFloat(element[0]));
				retVal.setCpuUsageMP(mpValue);
				int spValue = (int) (Float.parseFloat(element[6]));
				retVal.setCpuUsageSP(spValue);
				continue;
			}
			strIndex = line.indexOf(SUFFIX_RESOURCE_TOTAL_MEMORY);
			if (strIndex >= 0) {
				String item = line.substring(strIndex + SUFFIX_RESOURCE_TOTAL_MEMORY.length());
				String rmWhiteSpace = OBParser.removeFirstWhitespace(item);
				String element[] = rmWhiteSpace.split(" ");// space를 기준으로 분리.
				if (element.length != 8)
					continue;
				long mpValue = Long.parseLong(element[0]) * 1000L;
				retVal.setTotalMemMP(mpValue);
				long spValue = Long.parseLong(element[6]) * 1000L;
				retVal.setTotalMemSP(spValue);
				continue;
			}
			strIndex = line.indexOf(SUFFIX_RESOURCE_USED_MEMORY);
			if (strIndex >= 0) {
				String item = line.substring(strIndex + SUFFIX_RESOURCE_USED_MEMORY.length());
				String rmWhiteSpace = OBParser.removeFirstWhitespace(item);
				String element[] = rmWhiteSpace.split(" ");// space를 기준으로 분리.
				if (element.length != 8)
					continue;
				long mpValue = Long.parseLong(element[0]) * 1000L;
				retVal.setUsedMemMP(mpValue);
				long spValue = Long.parseLong(element[6]) * 1000L;
				retVal.setUsedMemSP(spValue);
				continue;
			}
			strIndex = line.indexOf(SUFFIX_RESOURCE_FREE_MEMORY);
			if (strIndex >= 0) {
				String item = line.substring(strIndex + SUFFIX_RESOURCE_FREE_MEMORY.length());
				String rmWhiteSpace = OBParser.removeFirstWhitespace(item);
				String element[] = rmWhiteSpace.split(" ");// space를 기준으로 분리.
				if (element.length != 8)
					continue;
				long mpValue = Long.parseLong(element[0]) * 1000L;
				retVal.setFreeMemMP(mpValue);
				long spValue = Long.parseLong(element[6]) * 1000L;
				retVal.setFreeMemSP(spValue);
				continue;
			}
			strIndex = line.indexOf(SUFFIX_RESOURCE_MEM_USAGE);
			if (strIndex >= 0) {
				String item = line.substring(strIndex + SUFFIX_RESOURCE_MEM_USAGE.length());
				String rmWhiteSpace = OBParser.removeFirstWhitespace(item);
				String element[] = rmWhiteSpace.split(" ");// space를 기준으로 분리.
				if (element.length != 8)
					continue;
				int mpValue = (int) (Float.parseFloat(element[0]));
				retVal.setMemUsageMP(mpValue);
				int spValue = (int) (Float.parseFloat(element[6]));
				retVal.setMemUsageSP(spValue);
				continue;
			}
		}
		return retVal;
	}

	private final static String SUFFIX_PORT_UPDOWN_START = "Port Type Link Admin";//
	private final static String SUFFIX_PORT_UPDOWN_END = "Session-sync port =";//

	public ArrayList<OBDtoPortInfoPAS> parsePortUpdown(String input) throws OBException {
		ArrayList<OBDtoPortInfoPAS> retVal = new ArrayList<OBDtoPortInfoPAS>();
		String lines[] = input.split("\n");
		boolean startFlag = false;
		for (String line : lines) {
			if (line.isEmpty())
				continue;
			int strIndex = 0;
			strIndex = line.indexOf(SUFFIX_PORT_UPDOWN_START);
			if (strIndex >= 0) {
				startFlag = true;
				continue;
			}
			strIndex = line.indexOf(SUFFIX_PORT_UPDOWN_END);
			if (strIndex >= 0) {
				break;
			}
			if (startFlag == false)
				continue;

			String rmWhiteSpace = OBParser.removeFirstWhitespace(line);
			String element[] = rmWhiteSpace.split(" ");// space를 기준으로 분리. //1 GE up up auto(1000) auto(full) auto(MDI)
														// wan - off copper
			if (element.length != 11)
				continue;

			OBDtoPortInfoPAS obj = new OBDtoPortInfoPAS();

			obj.setPortName(element[0]);
			if (element[2].endsWith("up") == true)
				obj.setLinkStatus(OBDefine.L2_LINK_STATUS_UP);
			else
				obj.setLinkStatus(OBDefine.L2_LINK_STATUS_DOWN);
			obj.setSpeed(element[4]);
			obj.setDuplex(element[5]);
			retVal.add(obj);
		}
		return retVal;
	}

	private ArrayList<String> parseVlanTaggedPortList(String input) throws OBException {
		ArrayList<String> retVal = new ArrayList<String>();
		byte[] byteArray = input.getBytes();
		for (int i = 0; i < byteArray.length; i++) {
			byte portIndex = byteArray[i];
			if (portIndex == 'T') {
				Integer index = i + 1;
				retVal.add(index.toString());
			}
		}
		return retVal;
	}

	private ArrayList<String> parseVlanUntaggedPortList(String input) throws OBException {
		ArrayList<String> retVal = new ArrayList<String>();
		byte[] byteArray = input.getBytes();
		for (int i = 0; i < byteArray.length; i++) {
			byte portIndex = byteArray[i];
			if (portIndex == 'U') {
				Integer index = i + 1;
				retVal.add(index.toString());
			}
		}
		return retVal;
	}

	private ArrayList<String> parseVlanUnavailabledPortList(String input) throws OBException {
		ArrayList<String> retVal = new ArrayList<String>();
		byte[] byteArray = input.getBytes();
		for (int i = 0; i < byteArray.length; i++) {
			byte portIndex = byteArray[i];
			if (portIndex == 'X') {
				Integer index = i + 1;
				retVal.add(index.toString());
			}
		}
		return retVal;
	}

//	PAS# show vlan 
//	--------------------------------------------------------
//	                                    1                   
//	VLAN Name  VLAN ID  State  1234567890123456  Transparent
//	--------------------------------------------------------
//	default    1        down   UUUUUUUUUUUUUUUU  disabled
//	wan        10       up     U...............  disabled
//	lan        20       up     .U..............  disabled
//	--------------------------------------------------------
//	(T:tagged port, U:untagged port, X:unavailable port)	
	private final static String SUFFIX_VLAN_INFO_START = "VLAN Name ";//

	public ArrayList<OBDtoVLanInfoPAS> parseVlanInfo(String input) throws OBException {
		ArrayList<OBDtoVLanInfoPAS> retVal = new ArrayList<OBDtoVLanInfoPAS>();
		String lines[] = input.split("\n");
		boolean startFlag = false;
		for (String line : lines) {
			if (line.isEmpty())
				continue;
			int strIndex = 0;
			strIndex = line.indexOf(SUFFIX_VLAN_INFO_START);
			if (strIndex >= 0) {
				startFlag = true;
				continue;
			}
			if (startFlag == false)
				continue;

			String rmWhiteSpace = OBParser.removeFirstWhitespace(line);
			String element[] = rmWhiteSpace.split(" ");// space를 기준으로 분리. //1 GE up up auto(1000) auto(full) auto(MDI)
														// wan - off copper
			if (element.length != 5)
				continue;

			OBDtoVLanInfoPAS obj = new OBDtoVLanInfoPAS();

			obj.setName(element[0]);
			obj.setId(Integer.parseInt(element[1]));
			if (element[2].equals("up"))
				obj.setStatus(OBDefine.L2_VLAN_STATE_ENABLED);
			else
				obj.setStatus(OBDefine.L2_VLAN_STATE_DISABLED);

			obj.setUntaggedPortList(parseVlanUntaggedPortList(element[3]));
			obj.setTaggedPortList(parseVlanTaggedPortList(element[3]));
			obj.setUnavailabledPortList(parseVlanUnavailabledPortList(element[3]));

			retVal.add(obj);
		}
		return retVal;
	}

//	PAS# show logging 
//	------------------------------------------------------------------
//	Logging configuration
//	------------------------------------------------------------------
//	Buffer Size        = 100 KB
//	Rotate Time        = 00:00 Everyday
//	Facility           = all
//	Level              = notice (5)
//	------------------------------------------------------------------
//	Logging Email From = 
//	SMTP Server        = Not defined
//	Logging Email To   = 
//	------------------------------------------------------------------
//	Logging Server     = enabled
//
//	Index  IP Address       Facility  Level        Agent Facility
//	1      172.172.2.222    all       notice       local7
//	2      172.172.2.222    all       information  local7
//	3      172.172.2.209    all       information  local7
//	4      172.172.2.209    all       alert        
//	5      172.172.2.209    all       critical     
//	6      172.172.2.209    all       emergency    
//	7      172.172.2.209    all       error        
//	8      172.172.2.209    all       notice       
//	9      172.172.2.209    all       warning      
//	10     172.172.2.222    all       alert        
//	11     172.172.2.222    all       critical     
//	12     172.172.2.222    all       emergency    
//	13     172.172.2.222    all       error        
//	14     172.172.2.222    all       notice       
//	15     172.172.2.222    all       warning      
//	16     172.172.2.56     syslog    alert        
//	17     172.172.2.56     syslog    information  
//	------------------------------------------------------------------	
	private final static String SUFFIX_SYSINFO_INFO_START = "Index IP Address";//
	private final static String SUFFIX_SYSINFO_ENABLE = "Logging Server =";//

	public ArrayList<OBDtoSyslogInfoPAS> parseSyslogInfo(String input) throws OBException {
		ArrayList<OBDtoSyslogInfoPAS> retVal = new ArrayList<OBDtoSyslogInfoPAS>();
		String lines[] = input.split("\n");
		boolean startFlag = false;
		int enableDisable = OBDefine.STATE_DISABLE;
		for (String line : lines) {
			if (line.isEmpty())
				continue;
			line = removeMoreText(line);

			int strIndex = 0;
			strIndex = line.indexOf(SUFFIX_SYSINFO_ENABLE);
			if (strIndex >= 0) {
				String item = line.substring(strIndex + SUFFIX_SYSINFO_ENABLE.length());
				String rmWhiteSpace = OBParser.removeFirstWhitespace(item);
				String element[] = rmWhiteSpace.split(" ");// space를 기준으로 분리.
				if (element[0].equals("enabled") == true)
					enableDisable = OBDefine.STATE_ENABLE;
				continue;
			}

			strIndex = line.indexOf(SUFFIX_SYSINFO_INFO_START);
			if (strIndex >= 0) {
				startFlag = true;
				continue;
			}
			if (startFlag == false)
				continue;

			String rmWhiteSpace = OBParser.removeFirstWhitespace(line);
			String element[] = rmWhiteSpace.split(" ");// space를 기준으로 분리.
			if (element.length < 4)
				continue;

			OBDtoSyslogInfoPAS obj = new OBDtoSyslogInfoPAS();
			obj.setIndex(Integer.parseInt(element[0]));
			obj.setIpAddress(element[1]);
			obj.setFacility(element[2]);
			obj.setLevel(element[3]);
			obj.setStatus(enableDisable);
			retVal.add(obj);
		}
		return retVal;
	}

//	PAS(config-ntp)# current 
//	----------------------------------------------
//	Current NTP client configuration
//	----------------------------------------------
//	Status                 = disabled
//	Interval               = 900 sec
//	Primary Server         = 
//	Secondary Server       = 
//	Timeout                = 5 sec
//	----------------------------------------------
	private final static String SUFFIX_NTPINFO_STATUS = "Status = ";//
	private final static String SUFFIX_NTPINFO_INTERVAL = "Interval = ";//
	private final static String SUFFIX_NTPINFO_PRIMARY = "Primary Server = ";//
	private final static String SUFFIX_NTPINFO_SECONDARY = "Secondary Server = ";//
	private final static String SUFFIX_NTPINFO_TIMEOUT = "Timeout = ";//

	public OBDtoNTPInfoPAS parseNTPInfo(String input) throws OBException {
		OBDtoNTPInfoPAS retVal = new OBDtoNTPInfoPAS();
		String lines[] = input.split("\n");
		for (String line : lines) {
			if (line.isEmpty())
				continue;
			int strIndex = 0;
			line = removeMoreText(line);
			strIndex = line.indexOf(SUFFIX_NTPINFO_STATUS);
			if (strIndex >= 0) {
				String item = line.substring(strIndex + SUFFIX_NTPINFO_STATUS.length());
				String rmWhiteSpace = OBParser.removeFirstWhitespace(item);
				String element[] = rmWhiteSpace.split(" ");// space를 기준으로 분리.
				if (element.length != 1)
					continue;
				if (element[0].endsWith("disabled") == true)
					retVal.setStatus(OBDefine.NTP_STATE_DISABLED);
				else
					retVal.setStatus(OBDefine.NTP_STATE_ENABLED);
				continue;
			}
			strIndex = line.indexOf(SUFFIX_NTPINFO_INTERVAL);
			if (strIndex >= 0) {
				String item = line.substring(strIndex + SUFFIX_NTPINFO_INTERVAL.length());
				String rmWhiteSpace = OBParser.removeFirstWhitespace(item);
				String element[] = rmWhiteSpace.split(" ");// space를 기준으로 분리.
				if (element.length != 2)
					continue;
				retVal.setInterval(Integer.parseInt(element[0]));
				continue;
			}
			strIndex = line.indexOf(SUFFIX_NTPINFO_PRIMARY);
			if (strIndex >= 0) {
				String item = line.substring(strIndex + SUFFIX_NTPINFO_PRIMARY.length());
				String rmWhiteSpace = OBParser.removeFirstWhitespace(item);
				String element[] = rmWhiteSpace.split(" ");// space를 기준으로 분리.
				if (element.length != 1)
					continue;
				retVal.setPrimary(element[0]);
				continue;
			}
			strIndex = line.indexOf(SUFFIX_NTPINFO_SECONDARY);
			if (strIndex >= 0) {
				String item = line.substring(strIndex + SUFFIX_NTPINFO_SECONDARY.length());
				String rmWhiteSpace = OBParser.removeFirstWhitespace(item);
				String element[] = rmWhiteSpace.split(" ");// space를 기준으로 분리.
				if (element.length != 1)
					continue;
				retVal.setSecondary(element[0]);
				continue;
			}
			strIndex = line.indexOf(SUFFIX_NTPINFO_TIMEOUT);
			if (strIndex >= 0) {
				String item = line.substring(strIndex + SUFFIX_NTPINFO_TIMEOUT.length());
				String rmWhiteSpace = OBParser.removeFirstWhitespace(item);
				String element[] = rmWhiteSpace.split(" ");// space를 기준으로 분리.
				if (element.length != 2)
					continue;
				retVal.setTimeout(Integer.parseInt(element[0]));
				continue;
			}
		}
		return retVal;
	}

	private final static String SUFFIX_PORTSTAT_RXOCTETS = "RxOctets";//
	private final static String SUFFIX_PORTSTAT_RXPKTS = "RxNoErrors";//
	private final static String SUFFIX_PORTSTAT_RXDISCARDS = "RxDiscards";//
	private final static String SUFFIX_PORTSTAT_TXOCTETS = "TRxOctets";//
	private final static String SUFFIX_PORTSTAT_TXPKTS = "TxNoErrors";//
	private final static String SUFFIX_PORTSTAT_TXDISCARDS = "TxDiscards";//
	private final static String SUFFIX_PORTSTAT_OVERSIZEPKTS = "OversizePkts";//
	private final static String SUFFIX_PORTSTAT_UNDERIZEPKTS = "UndersizePkts";//
	private final static String SUFFIX_PORTSTAT_FRAGMENTPKTS = "Fragments";//
	private final static String SUFFIX_PORTSTAT_CRCPTKTS = "CRC";// crc error
	private final static String SUFFIX_PORTSTAT_COLLISIONS = "Collisions";//

	public OBDtoPortStatPAS parsePortStatistics(String input) throws OBException {
		OBDtoPortStatPAS retVal = new OBDtoPortStatPAS();
		String lines[] = input.split("\n");
		for (String line : lines) {
			if (line.isEmpty())
				continue;
			int strIndex = 0;
			line = removeMoreText(line);
			strIndex = line.indexOf(SUFFIX_PORTSTAT_COLLISIONS);
			if (strIndex >= 0) {
				String item = line.substring(strIndex + SUFFIX_PORTSTAT_COLLISIONS.length());
				String rmWhiteSpace = OBParser.removeFirstWhitespace(item);
				String element[] = rmWhiteSpace.split(" ");// space를 기준으로 분리.
				if (element.length == 0)
					continue;
				retVal.setCollisions(Long.parseLong(element[0]));
				continue;
			}
			strIndex = line.indexOf(SUFFIX_PORTSTAT_CRCPTKTS);
			if (strIndex >= 0) {
				String item = line.substring(strIndex + SUFFIX_PORTSTAT_CRCPTKTS.length());
				String rmWhiteSpace = OBParser.removeFirstWhitespace(item);
				String element[] = rmWhiteSpace.split(" ");// space를 기준으로 분리.
				if (element.length == 0)
					continue;
				retVal.setCrcErrors(Long.parseLong(element[0]));
				continue;
			}
			strIndex = line.indexOf(SUFFIX_PORTSTAT_FRAGMENTPKTS);
			if (strIndex >= 0) {
				String item = line.substring(strIndex + SUFFIX_PORTSTAT_FRAGMENTPKTS.length());
				String rmWhiteSpace = OBParser.removeFirstWhitespace(item);
				String element[] = rmWhiteSpace.split(" ");// space를 기준으로 분리.
				if (element.length == 0)
					continue;
				retVal.setFragments(Long.parseLong(element[0]));
				continue;
			}
			strIndex = line.indexOf(SUFFIX_PORTSTAT_UNDERIZEPKTS);
			if (strIndex >= 0) {
				String item = line.substring(strIndex + SUFFIX_PORTSTAT_UNDERIZEPKTS.length());
				String rmWhiteSpace = OBParser.removeFirstWhitespace(item);
				String element[] = rmWhiteSpace.split(" ");// space를 기준으로 분리.
				if (element.length == 0)
					continue;
				retVal.setUndersize(Long.parseLong(element[0]));
				continue;
			}
			strIndex = line.indexOf(SUFFIX_PORTSTAT_OVERSIZEPKTS);
			if (strIndex >= 0) {
				String item = line.substring(strIndex + SUFFIX_PORTSTAT_OVERSIZEPKTS.length());
				String rmWhiteSpace = OBParser.removeFirstWhitespace(item);
				String element[] = rmWhiteSpace.split(" ");// space를 기준으로 분리.
				if (element.length == 0)
					continue;
				retVal.setOversize(Long.parseLong(element[0]));
				continue;
			}
			strIndex = line.indexOf(SUFFIX_PORTSTAT_RXOCTETS);
			if (strIndex >= 0) {
				String item = line.substring(strIndex + SUFFIX_PORTSTAT_RXOCTETS.length());
				String rmWhiteSpace = OBParser.removeFirstWhitespace(item);
				String element[] = rmWhiteSpace.split(" ");// space를 기준으로 분리.
				if (element.length == 0)
					continue;
				retVal.setRxBytes(Long.parseLong(element[0]));
				continue;
			}
			strIndex = line.indexOf(SUFFIX_PORTSTAT_RXPKTS);
			if (strIndex >= 0) {
				String item = line.substring(strIndex + SUFFIX_PORTSTAT_RXPKTS.length());
				String rmWhiteSpace = OBParser.removeFirstWhitespace(item);
				String element[] = rmWhiteSpace.split(" ");// space를 기준으로 분리.
				if (element.length == 0)
					continue;
				retVal.setRxPkts(Long.parseLong(element[0]));
				continue;
			}
			strIndex = line.indexOf(SUFFIX_PORTSTAT_RXDISCARDS);
			if (strIndex >= 0) {
				String item = line.substring(strIndex + SUFFIX_PORTSTAT_RXDISCARDS.length());
				String rmWhiteSpace = OBParser.removeFirstWhitespace(item);
				String element[] = rmWhiteSpace.split(" ");// space를 기준으로 분리.
				if (element.length == 0)
					continue;
				retVal.setRxDiscards(Long.parseLong(element[0]));
				continue;
			}
			strIndex = line.indexOf(SUFFIX_PORTSTAT_TXOCTETS);
			if (strIndex >= 0) {
				String item = line.substring(strIndex + SUFFIX_PORTSTAT_TXOCTETS.length());
				String rmWhiteSpace = OBParser.removeFirstWhitespace(item);
				String element[] = rmWhiteSpace.split(" ");// space를 기준으로 분리.
				if (element.length != 0)
					continue;
				retVal.setTxBytes(Long.parseLong(element[0]));
				continue;
			}
			strIndex = line.indexOf(SUFFIX_PORTSTAT_TXPKTS);
			if (strIndex >= 0) {
				String item = line.substring(strIndex + SUFFIX_PORTSTAT_TXPKTS.length());
				String rmWhiteSpace = OBParser.removeFirstWhitespace(item);
				String element[] = rmWhiteSpace.split(" ");// space를 기준으로 분리.
				if (element.length != 0)
					continue;
				retVal.setTxPkts(Long.parseLong(element[0]));
				continue;
			}
			strIndex = line.indexOf(SUFFIX_PORTSTAT_TXDISCARDS);
			if (strIndex >= 0) {
				String item = line.substring(strIndex + SUFFIX_PORTSTAT_TXDISCARDS.length());
				String rmWhiteSpace = OBParser.removeFirstWhitespace(item);
				String element[] = rmWhiteSpace.split(" ");// space를 기준으로 분리.
				if (element.length != 0)
					continue;
				retVal.setTxDiscards(Long.parseLong(element[0]));
				continue;
			}
		}
		return retVal;
	}

//	PAS(config)# show hardware-status 
//	---------------------------------------------
//	Temperature
//	---------------------------------------------
//	Management       CPU      = 72/95 degree
//	Packet-processor CPU1     = 46/95 degree
//	---------------------------------------------
//	Power
//	---------------------------------------------
//	AC1                       = ON 
//	AC2                       = OFF 
//	---------------------------------------------
//	FAN
//	---------------------------------------------
//	Fan1                      = ON
//	Fan2                      = ON
//	Fan3                      = ON
//	Fan4                      = ON
//	Fan5                      = ON
//	---------------------------------------------
//	Status
//	---------------------------------------------
//	Standby LED               = OFF
//	Status LED                = OFF
//	SSL Accelerator           = OFF
//	---------------------------------------------
//	Last Reset                = Soft Reset
//	---------------------------------------------
	private final static String SUFFIX_HWSTAT_POWER1 = "Power1 ";//
	private final static String SUFFIX_HWSTAT_POWER2 = "Power2 ";//
	private final static String SUFFIX_HWSTAT_POWER3 = "Power3 ";//
	private final static String SUFFIX_HWSTAT_POWER4 = "Power4 ";//
	private final static String SUFFIX_HWSTAT_POWER5 = "Power5 ";//
	private final static String SUFFIX_HWSTAT_POWER6 = "Power6 ";//
	private final static String SUFFIX_HWSTAT_POWER7 = "Power7 ";//
	private final static String SUFFIX_HWSTAT_POWER8 = "Power8 ";//
	private final static String SUFFIX_HWSTAT_POWER9 = "Power9 ";//
	private final static String SUFFIX_HWSTAT_POWER10 = "Power10 ";//
	private final static String SUFFIX_HWSTAT_POWER11 = "Power11 ";//
	private final static String SUFFIX_HWSTAT_POWER12 = "Power12 ";//
	private final static String SUFFIX_HWSTAT_POWER13 = "Power13 ";//
	private final static String SUFFIX_HWSTAT_POWER14 = "Power14 ";//
	private final static String SUFFIX_HWSTAT_POWER15 = "Power15 ";//
	private final static String SUFFIX_HWSTAT_POWER16 = "Power16 ";//
	private final static String SUFFIX_HWSTAT_POWER17 = "Power17 ";//
	private final static String SUFFIX_HWSTAT_POWER18 = "Power18 ";//
	private final static String SUFFIX_HWSTAT_FAN1 = "fan1 = ";//
	private final static String SUFFIX_HWSTAT_FAN2 = "fan2 = ";//
	private final static String SUFFIX_HWSTAT_FAN3 = "fan3 = ";//
	private final static String SUFFIX_HWSTAT_FAN4 = "fan4 = ";//
	private final static String SUFFIX_HWSTAT_FAN5 = "fan5 = ";//

	private OBDtoParserInfoPAS checkPowerIndex(String line) {
		int strIndex = 0;
		strIndex = line.indexOf(SUFFIX_HWSTAT_POWER1);
		if (strIndex >= 0)
			return new OBDtoParserInfoPAS(strIndex, 0, SUFFIX_HWSTAT_POWER1.length());
		strIndex = line.indexOf(SUFFIX_HWSTAT_POWER2);
		if (strIndex >= 0)
			return new OBDtoParserInfoPAS(strIndex, 1, SUFFIX_HWSTAT_POWER2.length());
		strIndex = line.indexOf(SUFFIX_HWSTAT_POWER3);
		if (strIndex >= 0)
			return new OBDtoParserInfoPAS(strIndex, 2, SUFFIX_HWSTAT_POWER3.length());
		strIndex = line.indexOf(SUFFIX_HWSTAT_POWER4);
		if (strIndex >= 0)
			return new OBDtoParserInfoPAS(strIndex, 3, SUFFIX_HWSTAT_POWER4.length());
		strIndex = line.indexOf(SUFFIX_HWSTAT_POWER5);
		if (strIndex >= 0)
			return new OBDtoParserInfoPAS(strIndex, 4, SUFFIX_HWSTAT_POWER5.length());
		strIndex = line.indexOf(SUFFIX_HWSTAT_POWER6);
		if (strIndex >= 0)
			return new OBDtoParserInfoPAS(strIndex, 5, SUFFIX_HWSTAT_POWER6.length());
		strIndex = line.indexOf(SUFFIX_HWSTAT_POWER7);
		if (strIndex >= 0)
			return new OBDtoParserInfoPAS(strIndex, 6, SUFFIX_HWSTAT_POWER7.length());
		strIndex = line.indexOf(SUFFIX_HWSTAT_POWER8);
		if (strIndex >= 0)
			return new OBDtoParserInfoPAS(strIndex, 7, SUFFIX_HWSTAT_POWER8.length());
		strIndex = line.indexOf(SUFFIX_HWSTAT_POWER9);
		if (strIndex >= 0)
			return new OBDtoParserInfoPAS(strIndex, 8, SUFFIX_HWSTAT_POWER9.length());
		strIndex = line.indexOf(SUFFIX_HWSTAT_POWER10);
		if (strIndex >= 0)
			return new OBDtoParserInfoPAS(strIndex, 9, SUFFIX_HWSTAT_POWER10.length());
		strIndex = line.indexOf(SUFFIX_HWSTAT_POWER11);
		if (strIndex >= 0)
			return new OBDtoParserInfoPAS(strIndex, 10, SUFFIX_HWSTAT_POWER11.length());
		strIndex = line.indexOf(SUFFIX_HWSTAT_POWER12);
		if (strIndex >= 0)
			return new OBDtoParserInfoPAS(strIndex, 11, SUFFIX_HWSTAT_POWER12.length());
		strIndex = line.indexOf(SUFFIX_HWSTAT_POWER13);
		if (strIndex >= 0)
			return new OBDtoParserInfoPAS(strIndex, 12, SUFFIX_HWSTAT_POWER13.length());
		strIndex = line.indexOf(SUFFIX_HWSTAT_POWER14);
		if (strIndex >= 0)
			return new OBDtoParserInfoPAS(strIndex, 13, SUFFIX_HWSTAT_POWER14.length());
		strIndex = line.indexOf(SUFFIX_HWSTAT_POWER15);
		if (strIndex >= 0)
			return new OBDtoParserInfoPAS(strIndex, 14, SUFFIX_HWSTAT_POWER15.length());
		strIndex = line.indexOf(SUFFIX_HWSTAT_POWER16);
		if (strIndex >= 0)
			return new OBDtoParserInfoPAS(strIndex, 15, SUFFIX_HWSTAT_POWER16.length());
		strIndex = line.indexOf(SUFFIX_HWSTAT_POWER17);
		if (strIndex >= 0)
			return new OBDtoParserInfoPAS(strIndex, 16, SUFFIX_HWSTAT_POWER17.length());
		strIndex = line.indexOf(SUFFIX_HWSTAT_POWER18);
		if (strIndex >= 0)
			return new OBDtoParserInfoPAS(strIndex, 17, SUFFIX_HWSTAT_POWER18.length());
		return null;
	}

	private OBDtoParserInfoPAS checkFanIndex(String line) {
		int strIndex = 0;
		strIndex = line.indexOf(SUFFIX_HWSTAT_FAN1);
		if (strIndex >= 0)
			return new OBDtoParserInfoPAS(strIndex, 0, SUFFIX_HWSTAT_FAN1.length());
		strIndex = line.indexOf(SUFFIX_HWSTAT_FAN2);
		if (strIndex >= 0)
			return new OBDtoParserInfoPAS(strIndex, 1, SUFFIX_HWSTAT_FAN2.length());
		strIndex = line.indexOf(SUFFIX_HWSTAT_FAN3);
		if (strIndex >= 0)
			return new OBDtoParserInfoPAS(strIndex, 2, SUFFIX_HWSTAT_FAN3.length());
		strIndex = line.indexOf(SUFFIX_HWSTAT_FAN4);
		if (strIndex >= 0)
			return new OBDtoParserInfoPAS(strIndex, 3, SUFFIX_HWSTAT_FAN4.length());
		strIndex = line.indexOf(SUFFIX_HWSTAT_FAN5);
		if (strIndex >= 0)
			return new OBDtoParserInfoPAS(strIndex, 4, SUFFIX_HWSTAT_FAN5.length());

		return null;
	}

	public OBDtoHWStatPAS parseHWStatistics(String input) {
		OBDtoHWStatPAS retVal = new OBDtoHWStatPAS();
		String lines[] = input.split("\n");
		for (String line : lines) {
			if (line.isEmpty())
				continue;
//			int strIndex = 0;
			line = removeMoreText(line);

			OBDtoParserInfoPAS paserInfo = checkFanIndex(line);
			if (paserInfo != null) {
				String item = line.substring(paserInfo.getStrIndex() + paserInfo.getStrLength());
				String rmWhiteSpace = OBParser.removeFirstWhitespace(item);
				String element[] = rmWhiteSpace.split(" ");// space를 기준으로 분리.
				if (element.length != 1)
					continue;
				if (element[0].endsWith("ON") == true)
					retVal.setFanStatus(paserInfo.getExtraIndex(), OBDefine.SYS_FAN_STATUS_OK);
				else
					retVal.setFanStatus(paserInfo.getExtraIndex(), OBDefine.SYS_FAN_STATUS_FAIL);
				continue;
			}

			paserInfo = checkPowerIndex(line);
			if (paserInfo != null) {
				String item = line.substring(paserInfo.getStrIndex() + paserInfo.getStrLength());
				String rmWhiteSpace = OBParser.removeFirstWhitespace(item);
				String element[] = rmWhiteSpace.split(" ");// space를 기준으로 분리.
				if (element.length != 1)
					continue;
				if (element[0].endsWith("ON") == true)
					retVal.setFanStatus(paserInfo.getExtraIndex(), OBDefine.SYS_POWERSUPPLY_STATUS_OK);
				else
					retVal.setFanStatus(paserInfo.getExtraIndex(), OBDefine.SYS_POWERSUPPLY_STATUS_FAIL);
				continue;
			}
		}
		return retVal;
	}

//
//	PAS(config)# show snmp 
//	-------------------------------------------------------------------------
//	SNMP configuration                                    
//	-------------------------------------------------------------------------
//	SNMP State            = enabled
//	Community Name        = default
//	Community Access      = read
//	User Name             = 
//	Load Timeout          = 60
//	Agent Address         = 
//	-------------------------------------------------------------------------
//	System Information
//	=========================================================================
//	Name                  = 
//	Location              = 
//	Contact               = 
//	-------------------------------------------------------------------------
//	Generic Traps Type
//	=========================================================================
//	authentication     disabled| I/F link up          disabled
//	I/F link down      disabled| cold start           disabled
//
//	Enterprise Traps Type
//	=========================================================================
//	failover           disabled| service              disabled
//	filter             disabled| worm                 disabled
//	email-worm         disabled| dos                  disabled
//	mail               disabled| intrusion-prevention disabled
//	power              disabled| fan                  disabled
//	port               disabled| login-fail           disabled
//	temperature        disabled| cpu                  disabled
//	memory             disabled| firmware update      disabled
//	worm update        disabled| email-worm update    disabled
//	packet-processor   disabled|
//	intrusion prevention update disabled
//
//	-------------------------------------------------------------------------
//	Trap Address          = 
//	-------------------------------------------------------------------------	
	private final static String SUFFIX_SNMP_NAME = "Name = ";//

	public OBDtoSnmpInfoPAS parseSnmpInfo(String input) {
		OBDtoSnmpInfoPAS retVal = new OBDtoSnmpInfoPAS();
		String lines[] = input.split("\n");
		for (String line : lines) {
			if (line.isEmpty())
				continue;
			int strIndex = 0;
			line = removeMoreText(line);
			strIndex = line.indexOf(SUFFIX_SNMP_NAME);
			if (strIndex >= 0) {
				String item = line.substring(strIndex + SUFFIX_SNMP_NAME.length());
				String rmWhiteSpace = OBParser.removeFirstWhitespace(item);
				String element[] = rmWhiteSpace.split(" ");// space를 기준으로 분리.
				if (element.length != 1)
					continue;
				retVal.setHostName(element[0]);
				continue;
			}
		}
		return retVal;
	}

//	--------------------------------------------------------------------------------
//	SLB Service :
//	  Name              = hi_teset_1,  enabled
//	  Network-Type      = ipv4
//	  Vip               = 192.168.200.130     
//	  Vport             = tcp:23 
//	  NAT mode          = dest-nat            
//	  Priority          = 50                    LB-method         = lc
//	  Session-sync      = N/A                   Backup            = N/A
//	  Fail-Skip         = disabled              Status            = ACT
//	  Session-timeout   = 0                   
//	  Unassured-timeout = 0                   
//	  close-timeout     = 0                   
//	  Sticky :                                
//	    Time            = 60                  
//	    Src-subnet      = 255.255.255.255
//
//	  Real : 
//	    ID   Name        Rip:Rport            Backup G-shutdown Status  En/Dis
//	    1    real1       192.168.199.41:40    N/A    disabled   INACT   enabled
//	    2    real2       192.168.199.41:60    N/A    disabled   INACT   enabled
//
//	  Dynamic-Weight : 
//
//	  Healthcheck : 
//	    ID   Type                  Port    En/Dis
//	    1    http                  0       enabled
//
//	  Healthcheck status : 
//	    Real                    Healthcheck ID                                  
//	    ID    Name                1
//	    1     real1               X
//	    2     real2               X
//	                               (O:success, X:fail, D:disabled, S:slave)
//
//	  Sessions : 
//	    Real ID   Real Name           Current sessions    Total sessions
//	    1         real1                              0                 0
//	    2         real2                              0                 0
//	    ----------------------------------------------------------------
//	    Service sessions                             0                 0
//
//	--------------------------------------------------------------------------------
	private final static String SUFFIX_SLBSTATUS_SERVICE_START = "SLB Service :";//
	private final static String SUFFIX_SLBSTATUS_SERVICE_END = "--------------------------------------------------------------------------------";//
	private final static String SUFFIX_SLBSTATUS_VSNAME = "Name = ";//
	private final static String SUFFIX_SLBSTATUS_VSVIP = "Vip = ";//
	private final static String SUFFIX_SLBSTATUS_VSPORT = "Vport = ";//
	private final static String SUFFIX_SLBSTATUS_REAL_START = "Real :";//
	private final static String SUFFIX_SLBSTATUS_REAL_END = "Dynamic-Weight :";//

	public ArrayList<OBDtoAdcVServerPAS> parseSlbStatus(String input) throws OBException {
		try {
			String lines[] = input.split("\n");
			ArrayList<OBDtoAdcVServerPAS> retVal = new ArrayList<OBDtoAdcVServerPAS>();

			int currState = PARSE_STATE_INIT;
			for (String line : lines) {
				if (line.isEmpty())
					continue;
				line = removeMoreText(line);
				int tmpState = checkCurState4SlbInfo(currState, line);

				switch (tmpState) {
				case PARSE_STATE_INIT:
					if (currState == PARSE_STATE_REAL) {// 하나의 slb 구성이 끝난 경우임.
						retVal.add(vsObj);
						currState = tmpState;
						continue;
					}
					break;
				case PARSE_STATE_SLB:// slb 정보 추출 상태.
					if (currState == PARSE_STATE_INIT) {// slb 시작.
						this.vsObj = new OBDtoAdcVServerPAS();
						OBDtoAdcPoolPAS poolObj = new OBDtoAdcPoolPAS();
						ArrayList<OBDtoAdcPoolMemberPAS> memberList = new ArrayList<OBDtoAdcPoolMemberPAS>();
						poolObj.setMemberList(memberList);
						this.vsObj.setPool(poolObj);
						currState = tmpState;
						continue;
					}
					procSlbState4SlbInfo(line);
					break;
				case PARSE_STATE_REAL:// real 정보 추출 상태.
					if (currState == PARSE_STATE_SLB) {// real 시작.
						this.memObj = new OBDtoAdcPoolMemberPAS();
						currState = tmpState;
						continue;
					}
					procRealState4SlbInfo(line);
					currState = tmpState;
					break;
				default:
					break;
				}
			}

			return retVal;
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

	private Integer convertNodeStatus(String state) {
		if (state.compareTo("ACT") == 0)
			return OBDefine.STATUS_AVAILABLE;
		if (state.compareTo("INACT") == 0)
			return OBDefine.STATUS_UNAVAILABLE;
		if (state.compareTo("UNKNOWN") == 0)
			return OBDefine.STATUS_DISABLE;
		return OBDefine.STATUS_UNAVAILABLE;
	}

	private void procRealState4SlbInfo(String line) throws OBException {
		String rmWhiteSpace = OBParser.removeFirstWhitespace(line);

		String element[] = rmWhiteSpace.split(" ");// space를 기준으로 분리.//1 real1 1.1.1.1:80 N/A disabled UNKNOWN enabled
		if (element.length != 7) {
			return;
		}
		try {
			if (Integer.parseInt(element[0]) > 0) {
				this.memObj.setId(Integer.parseInt(element[0]));
			}
		} catch (Exception e) {
			return;
		}

		this.memObj.setName(element[1]);
		// rip:rport
		String[] ripport = element[2].split(":");
		if (ripport.length != 2) {
			return;
		}
		this.memObj.setIpAddress(ripport[0]);
		this.memObj.setPort(Integer.parseInt(ripport[1]));
		this.memObj.setStatus(convertNodeStatus(element[5]));

		if (element[6].equals("enabled"))
			this.memObj.setState(OBDefine.STATE_ENABLE);
		else
			this.memObj.setState(OBDefine.STATE_DISABLE);

		this.vsObj.getPool().getMemberList().add(this.memObj);
		return;
	}

	// slb 시작 문구를 찾는다.
	private void procSlbState4SlbInfo(String line) throws OBException {
//		String rmWhiteSpace = OBParser.removeFirstWhitespace(line);
		int strIndex = 0;
		// name
		strIndex = line.indexOf(SUFFIX_SLBSTATUS_VSNAME);
		if (strIndex >= 0)// Name = test1, enabled
		{
			String input = line.substring(strIndex + SUFFIX_SLBSTATUS_VSNAME.length());
			// slb 이름을 추출한다. ex: slb yyh_test_pio1818
			String element[] = input.split(",");// space를 기준으로 분리.
			if (element.length != 2) {
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						String.format("parse error(vsName). line:%s", input));
			}
			this.vsObj.setName(element[0]);
			// pool 이름은 vs 이름과 동일하게 한다.
			this.vsObj.getPool().setName(element[0]);

			// state 정보를 설정한다.
			if (element[1].endsWith("enabled") == true)
				this.vsObj.setState(OBDefine.STATE_ENABLE);
			else
				this.vsObj.setState(OBDefine.STATE_DISABLE);
			return;
		}

		// vip
		strIndex = line.indexOf(SUFFIX_SLBSTATUS_VSVIP);
		if (strIndex >= 0) {
			String input = line.substring(strIndex + SUFFIX_SLBSTATUS_VSVIP.length());
			// vip 21.21.21.255
			String element[] = input.split(" ");// space를 기준으로 분리.
			if (element.length != 1) {
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						String.format("parse error(vip). line:%s", input));
			}
			this.vsObj.setvIP(element[0]);
			return;
		}

		// vport
		strIndex = line.indexOf(SUFFIX_SLBSTATUS_VSPORT);
		if (strIndex >= 0) {
			String input = line.substring(strIndex + SUFFIX_SLBSTATUS_VSPORT.length());
			// vport tcp:80
			String element[] = input.split(" ");// space를 기준으로 분리.
			if (element.length != 1) {
				return;
			}

			// proto:port 형태로 전달됨.
			String[] protPort = element[0].split(":");
			if (protPort.length != 2) {
				return;
			}
			this.vsObj.setSrvPort(Integer.parseInt(protPort[1]));
			this.vsObj.setSrvProtocol(convertProtocol(protPort[0]));
			return;
		}
	}

	private int checkCurState4SlbInfo(int currState, String input) {
		int strIndex = 0;
		strIndex = input.indexOf(SUFFIX_SLBSTATUS_SERVICE_START);
		if (strIndex >= 0) {
			return PARSE_STATE_SLB;
		}

		strIndex = input.indexOf(SUFFIX_SLBSTATUS_REAL_START);
		if (strIndex >= 0) {
			return PARSE_STATE_REAL;
		}

		strIndex = input.indexOf(SUFFIX_SLBSTATUS_REAL_END);
		if (strIndex >= 0) {
			return PARSE_STATE_SLB;
		}

		strIndex = input.indexOf(SUFFIX_SLBSTATUS_SERVICE_END);
		if (strIndex >= 0) {
			return PARSE_STATE_INIT;
		}

		return currState;
	}

//	PAS(config)# show logging buffer 
//	------------------------------------------------------------------------------
//	Logging buffer (entire messages)
//	------------------------------------------------------------------------------
//	[17:42:55 05/06/2013] (notice) login[368]: "root login  on `ttyp0' from `172.172.2.222'"
//	[17:41:55 05/06/2013] (notice) login[32704]: "root login  on `ttyp0' from `172.172.2.222'"
//	[17:40:55 05/06/2013] (notice) login[32574]: "root login  on `ttyp0' from `172.172.2.222'"
//	[17:39:55 05/06/2013] (notice) login[32447]: "root login  on `ttyp0' from `172.172.2.222'"
//	[17:39:44 05/06/2013] (notice) login[32395]: "root login  on `ttyp0' from `172.172.2.206'"
	private final static String SUFFIX_LOGGING_BUFFER_DATE = "\\] \\(";// [17:42:55 05/06/2013] (notice) login[368]:
																		// "root login on `ttyp0' from `172.172.2.222'"

	public ArrayList<OBDtoLoggingBufferPAS> parseLoggingBuffer(String input) throws OBException {
		ArrayList<OBDtoLoggingBufferPAS> retVal = new ArrayList<OBDtoLoggingBufferPAS>();
		String lines[] = input.split("\n");
		for (String line : lines) {
			if (line.isEmpty())
				continue;
			line = removeMoreText(line);
			String element[] = line.split(SUFFIX_LOGGING_BUFFER_DATE);// space를 기준으로 분리.
			if (element.length != 2)
				continue;

			OBDtoLoggingBufferPAS obj = new OBDtoLoggingBufferPAS();
			if (element[0].isEmpty())
				continue;
			if (element[1].isEmpty())
				continue;
			obj.setDate(element[0].substring(1));
			obj.setContent(element[1]);
			retVal.add(obj);
		}
		return retVal;
	}

	private final static String SUFFIX_SYSLOG_ENABLED = "Logging Server = ";//

	public Integer parseSyslogState(String input) throws OBException {
		int strIndex = 0;
		String lines[] = input.split("\n");
		for (String line : lines) {
			strIndex = line.indexOf(SUFFIX_SYSLOG_ENABLED);
			if (strIndex >= 0) {
				String item = line.substring(strIndex + SUFFIX_SYSLOG_ENABLED.length());
				String rmWhiteSpace = OBParser.removeFirstWhitespace(item);
				String element[] = rmWhiteSpace.split(" ");// space를 기준으로 분리.
				if (element[0].equals("enabled") == true)
					return OBDefine.STATE_ENABLE;
				else
					return OBDefine.STATE_DISABLE;
			}
		}
		return OBDefine.STATE_DISABLE;
	}

	private final static String SUFFIX_SYSLOG_HOST_START = "Index IP Address Facility Level Agent Facility";//

	public ArrayList<String> parseSyslogHost(String input) throws OBException {
		ArrayList<String> retVal = new ArrayList<String>();
		int strIndex = 0;
		String lines[] = input.split("\n");
		boolean startFlag = false;
		for (String line : lines) {
			if (startFlag == false) {
				strIndex = line.indexOf(SUFFIX_SYSLOG_HOST_START);
				if (strIndex >= 0) {
					startFlag = true;
				}
				continue;
			}
			String element[] = line.split(" ", 3);
			if (element.length != 3)
				continue;
			retVal.add(element[1]);// 두번째 필드가 IP 정보가 있음.
		}
		return retVal;
	}

	private final static String SUFFIX_SNMP_ENABLED = "SNMP State = ";//

	public String parseSnmpState(String input) throws OBException {
		int strIndex = 0;
		String lines[] = input.split("\n");
		for (String line : lines) {
			strIndex = line.indexOf(SUFFIX_SNMP_ENABLED);
			if (strIndex >= 0) {
				return line.substring(strIndex + SUFFIX_SNMP_ENABLED.length());
			}
		}
		return "";
	}

	private final static String SUFFIX_SNMP_RCOMM = "Community Name = ";//

	public String parseSnmpRCommString(String input) throws OBException {
		int strIndex = 0;
		String lines[] = input.split("\n");
		for (String line : lines) {
			strIndex = line.indexOf(SUFFIX_SNMP_RCOMM);
			if (strIndex >= 0) {
				return line.substring(strIndex + SUFFIX_SNMP_RCOMM.length()).replace("\n", "");
			}
		}
		return "";
	}
}
