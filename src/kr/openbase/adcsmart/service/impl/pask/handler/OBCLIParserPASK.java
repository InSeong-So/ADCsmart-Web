package kr.openbase.adcsmart.service.impl.pask.handler;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import kr.openbase.adcsmart.service.dto.OBDtoAdcInfo;
import kr.openbase.adcsmart.service.impl.OBAdcManagementImpl;
import kr.openbase.adcsmart.service.impl.pask.dto.OBDtoAdcConfigSlbPASK;
import kr.openbase.adcsmart.service.impl.pask.dto.OBDtoAdcHealthCheckPASK;
import kr.openbase.adcsmart.service.impl.pask.dto.OBDtoAdcPoolMemberPASK;
import kr.openbase.adcsmart.service.impl.pask.dto.OBDtoAdcPoolPASK;
import kr.openbase.adcsmart.service.impl.pask.dto.OBDtoAdcVServerPASK;
import kr.openbase.adcsmart.service.impl.pask.dto.OBDtoRealServerInfoPASK;
import kr.openbase.adcsmart.service.impl.pask.handler.dto.DtoRptStpInfoPASK;
import kr.openbase.adcsmart.service.impl.pask.handler.dto.DtoRptTrunkInfoPASK;
import kr.openbase.adcsmart.service.impl.pask.handler.dto.OBDtoHWStatPASK;
import kr.openbase.adcsmart.service.impl.pask.handler.dto.OBDtoInterfaceInfoPASK;
import kr.openbase.adcsmart.service.impl.pask.handler.dto.OBDtoLicenseInfoPASK;
import kr.openbase.adcsmart.service.impl.pask.handler.dto.OBDtoLoggingBufferPASK;
import kr.openbase.adcsmart.service.impl.pask.handler.dto.OBDtoNTPInfoPASK;
import kr.openbase.adcsmart.service.impl.pask.handler.dto.OBDtoPortInfoPASK;
import kr.openbase.adcsmart.service.impl.pask.handler.dto.OBDtoPortStatPASK;
import kr.openbase.adcsmart.service.impl.pask.handler.dto.OBDtoResourceInfoPASK;
import kr.openbase.adcsmart.service.impl.pask.handler.dto.OBDtoSnmpInfoPASK;
import kr.openbase.adcsmart.service.impl.pask.handler.dto.OBDtoSyslogInfoPASK;
import kr.openbase.adcsmart.service.impl.pask.handler.dto.OBDtoSystemInfoPASK;
import kr.openbase.adcsmart.service.impl.pask.handler.dto.OBDtoVLanInfoPASK;
import kr.openbase.adcsmart.service.utility.OBCommon;
import kr.openbase.adcsmart.service.utility.OBDateTime;
import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.service.utility.OBNetwork;
import kr.openbase.adcsmart.service.utility.OBParser;
import kr.openbase.adcsmart.service.utility.OBSystemLog;

/*
 * CLI 명령에 의해 출력된 데이터를 파싱한다.
 */
public class OBCLIParserPASK {
	private final static int PARSE_STATE_INIT = 0;
	private final static int PARSE_STATE_SLB = 1;
	private final static int PARSE_STATE_REAL = 2;

	private final static int PARSE_STATE_VIP = 4;

	// constructor
	public OBCLIParserPASK() {
	}

//	public static void main(String [] args) 
//	{
//		try
//		{
//			OBAdcPAS handler = new OBAdcPAS("192.168.200.110", "root", "admin");
//			//handler.enter();
//			handler.login();
//			System.out.println("login OK");
//			
//			String result = handler.cmndDumpcfg();//
//			System.out.println(result);
//			System.out.println("command OK");
//			
//			System.out.println(new OBCLIParserPASK().getSlbInfo(1, result));
//			
//			handler.disconnect();
//			System.out.println("logout OK");
//
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}

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
//    final static String SUFFIX_SLB_SERVICE = "! Define SLB service ";
//    final static String SUFFIX_REAL_SERVICE = "! Define Reals of SLB ";
//    final static String SUFFIX_HEALTH_CHECK = "! Define Healthcheck ";
//    final static String SUFFIX_END_SLB = " ..";
//    
//    private int checkCurrentState(int currState, String input)
//    {
//    	int strIndex=0;
//    	strIndex = input.indexOf(SUFFIX_SLB_SERVICE);
//		if(strIndex>=0)
//		{
//			return PARSE_STATE_SLB;
//		}
//    	
//		strIndex = input.indexOf(SUFFIX_REAL_SERVICE);
//		if(strIndex>=0)
//		{
//			return PARSE_STATE_REAL;
//		}
//
//		strIndex = input.indexOf(SUFFIX_HEALTH_CHECK);
//		if(strIndex>=0)
//		{
//			return PARSE_STATE_HEALTH;
//		}
//
//		strIndex = input.indexOf(SUFFIX_END_SLB);
//		if(strIndex>=0)
//		{
//			return PARSE_STATE_INIT;
//		}
//
//		return currState;
//    }

//	private OBDtoAdcVServerPAS vsObj=null;
//	private OBDtoAdcHealthCheckPAS healthObj=null;
//	private OBDtoAdcPoolMemberPAS memObj=null;

//	final static String SUFFIX_HEALTH_ID 		= "health ";
//	final static String SUFFIX_HEALTH_TYPE 		= "type ";
//	final static String SUFFIX_HEALTH_ENABLE 	= "enable";
//	final static String SUFFIX_HEALTH_DISALBE	= "disable";
//	final static String SUFFIX_HEALTH_APPLY		= "apply";
//	private void procHealthState(String line, HashMap<String, OBDtoAdcHealthCheckPAS> healthMap) throws OBException
//	{
//		try
//		{
//			String rmWhiteSpace = OBParser.removeFirstWhitespace(line);
//			int strIndex = 0;
//			// id
//			strIndex = rmWhiteSpace.indexOf(SUFFIX_HEALTH_ID);
//			if(strIndex>=0)
//			{
//				String input = rmWhiteSpace.substring(strIndex+SUFFIX_HEALTH_ID.length());
//				//   health 1
//				String element[] = input.split(" ");// space를 기준으로 분리.
//				if(element.length != 1)
//				{
//					return;
//				}
//				this.healthObj = new OBDtoAdcHealthCheckPAS();// health 정보 시작.
//				
//				this.healthObj.setId(Integer.parseInt(element[0]));
//				this.healthObj.setName(this.vsObj.getName());
//				String dbIndex = OBCommon.makeHealthDbIndexPAS(this.vsObj.getAdcIndex(), this.vsObj.getName(), this.healthObj.getId());
//				this.healthObj.setDbIndex(dbIndex);
//				return;
//			}
//			
//			// type
//			strIndex = rmWhiteSpace.indexOf(SUFFIX_HEALTH_TYPE);
//			if(strIndex>=0)
//			{
//				String input = rmWhiteSpace.substring(strIndex+SUFFIX_HEALTH_TYPE.length());
//				//   type tcp
//				String element[] = input.split(" ");// space를 기준으로 분리.
//				if(element.length != 1)
//				{
//					return;
//				}
//				this.healthObj.setType(convertHealthType(element[0]));
//				return;
//			}
//			
//			// enable
//			strIndex = rmWhiteSpace.indexOf(SUFFIX_HEALTH_ENABLE);
//			if(strIndex>=0)
//			{
//				//     enable
//				this.healthObj.setState(OBDefine.STATE_ENABLE);
//				return;
//			}
//			
//			// disalbe
//			strIndex = rmWhiteSpace.indexOf(SUFFIX_HEALTH_DISALBE);
//			if(strIndex>=0)
//			{
//				//     disable
//				this.healthObj.setState(OBDefine.STATE_DISABLE);
//				return;
//			}
//			
//			// apply
//			strIndex = rmWhiteSpace.indexOf(SUFFIX_HEALTH_APPLY);
//			if(strIndex>=0)
//			{
//				if(this.vsObj.getPool().getHealthCheckInfo()==null)
//				{
//					this.vsObj.getPool().setHealthCheckInfo(this.healthObj);
//				}
//				String mapKey = this.vsObj.getAdcIndex()+"_"+this.vsObj.getName()+"_"+this.healthObj.getId();
//				healthMap.put(mapKey, this.healthObj);
//				this.healthObj=null;
//				return;
//			}
//		}
//		catch(Exception e)
//		{
//			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
//		}
//	}

//	final static String SUFFIX_REAL_ID 		= "real ";
//	final static String SUFFIX_REAL_NAME 	= "name ";
//	final static String SUFFIX_REAL_RIP 	= "rip ";
//	final static String SUFFIX_REAL_RPORT	= "rport ";
//	final static String SUFFIX_REAL_ENABLE 	= "enable";
//	final static String SUFFIX_REAL_DISALBE	= "disable";
//	final static String SUFFIX_REAL_APPLY	= "apply";

//	private void procRealState(String line) throws OBException
//	{
//		String rmWhiteSpace = OBParser.removeFirstWhitespace(line);
//		int strIndex = 0;
//		// id
//		strIndex = rmWhiteSpace.indexOf(SUFFIX_REAL_ID);
//		if(strIndex>=0)
//		{
//			String input = rmWhiteSpace.substring(strIndex+SUFFIX_REAL_ID.length());
//			//   real 1
//			String element[] = input.split(" ");// space를 기준으로 분리.
//			if(element.length != 1)
//			{
//				return;
//			}
//			this.memObj.setId(Integer.parseInt(element[0]));
//			String dbIndex = OBCommon.makeNodeIndexPAS(this.vsObj.getAdcIndex(), this.vsObj.getPool().getName(), this.memObj.getId());
//			this.memObj.setDbIndex(dbIndex);
//			return;
//		}
//		// name
//		strIndex = rmWhiteSpace.indexOf(SUFFIX_REAL_NAME);
//		if(strIndex>=0)
//		{
//			String input = rmWhiteSpace.substring(strIndex+SUFFIX_REAL_NAME.length());
//			//     name real1
//			String element[] = input.split(" ");// space를 기준으로 분리.
//			if(element.length != 1)
//			{
//				return;
//			}
//			this.memObj.setName(element[0]);
//			return;
//		}
//		
//		// rip
//		strIndex = rmWhiteSpace.indexOf(SUFFIX_REAL_RIP);
//		if(strIndex>=0)
//		{
//			String input = rmWhiteSpace.substring(strIndex+SUFFIX_REAL_RIP.length());
//			//     name real1
//			String element[] = input.split(" ");// space를 기준으로 분리.
//			if(element.length != 1)
//			{
//				return;
//			}
//			this.memObj.setIpAddress(element[0]);
//			return;
//		}
//		
//		// rport
//		strIndex = rmWhiteSpace.indexOf(SUFFIX_REAL_RPORT);
//		if(strIndex>=0)
//		{
//			String input = rmWhiteSpace.substring(strIndex+SUFFIX_REAL_RPORT.length());
//			//     name real1
//			String element[] = input.split(" ");// space를 기준으로 분리.
//			if(element.length != 1)
//			{
//				return;
//			}
//			this.memObj.setPort(Integer.parseInt(element[0]));
//			return;
//		}
//		
//		// enable
//		strIndex = rmWhiteSpace.indexOf(SUFFIX_REAL_ENABLE);
//		if(strIndex>=0)
//		{
//			//     enable
//			this.memObj.setState(OBDefine.STATE_ENABLE);
//			return;
//		}
//		
//		// disalbe
//		strIndex = rmWhiteSpace.indexOf(SUFFIX_REAL_DISALBE);
//		if(strIndex>=0)
//		{		
//			//     disable
//			this.memObj.setState(OBDefine.STATE_DISABLE);
//			return;
//		}
//		
//		// apply
//		strIndex = rmWhiteSpace.indexOf(SUFFIX_REAL_APPLY);
//		if(strIndex>=0)
//		{
//			if(this.vsObj.getPool().getMemberList()==null)
//			{
//				 ArrayList<OBDtoAdcPoolMemberPAS> memList = new  ArrayList<OBDtoAdcPoolMemberPAS>();
//				 this.vsObj.getPool().setMemberList(memList);
//			}
//			this.vsObj.getPool().getMemberList().add(this.memObj);
//			this.memObj = new OBDtoAdcPoolMemberPAS();// 신규 등록..
//			return;
//		}
//	}

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
		if (method.compareTo("sh") == 0)
			return OBDefine.LB_METHOD_HASH;
		return OBDefine.COMMON_NOT_ALLOWED; // 관리 대상외 항목은 Not Allowed로 못 건드리게 한다.
	}

//	// slb 시작 문구를 찾는다.
//	private void procSlbState(String line) throws OBException
//	{
//		String rmWhiteSpace = OBParser.removeFirstWhitespace(line);
//		int strIndex = 0;
//		// name
//		strIndex = rmWhiteSpace.indexOf(SUFFIX_SLB_NAME);
//		if(strIndex>=0)
//		{
//			String input = rmWhiteSpace.substring(strIndex+SUFFIX_SLB_NAME.length());
//			// slb 이름을 추출한다. ex: slb yyh_test_pio1818
//			String element[] = input.split(" ");// space를 기준으로 분리.
//			if(element.length != 1)
//			{
//				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("parse error(vsName). line:%s", input));
//			}
//			this.vsObj.setName(element[0]);
//			// pool 이름은 vs 이름과 동일하게 한다.
//			this.vsObj.getPool().setName(element[0]);
//
//			// vs db index 설정.
//			String vsDbIndex = OBCommon.makeVSIndexPAS(this.vsObj.getAdcIndex(), this.vsObj.getName());
//			this.vsObj.setDbIndex(vsDbIndex);
//
//			// pool db index 설정.
//			String poolDbIndex = OBCommon.makePoolIndexPAS(this.vsObj.getAdcIndex(), this.vsObj.getPool().getName());
//			this.vsObj.getPool().setDbIndex(poolDbIndex);
//			return;
//		}
//		
//		// lb-method
//		strIndex = rmWhiteSpace.indexOf(SUFFIX_SLB_LBMETHOD);
//		if(strIndex>=0)
//		{
//			String input = rmWhiteSpace.substring(strIndex+SUFFIX_SLB_LBMETHOD.length());
//			//   lb-method rr
//			String element[] = input.split(" ");// space를 기준으로 분리.
//			if(element.length != 1)
//			{
//				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("parse error(lbmethod). line:%s", input));
//			}
//			this.vsObj.getPool().setLbMethod(convertLBMethod(element[0]));
//			return;
//		}
//		
//		// vip
//		strIndex = rmWhiteSpace.indexOf(SUFFIX_SLB_VIP);
//		if(strIndex>=0)
//		{
//			String input = rmWhiteSpace.substring(strIndex+SUFFIX_SLB_VIP.length());		
//			//  vip 21.21.21.255
//			String element[] = input.split(" ");// space를 기준으로 분리.
//			if(element.length != 1)
//			{
//				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("parse error(vip). line:%s", input));
//			}
//			this.vsObj.setvIP(element[0]);
//			return;
//		}
//		
//		// vport
//		strIndex = rmWhiteSpace.indexOf(SUFFIX_SLB_VPORT);
//		if(strIndex>=0)
//		{
//			String input = rmWhiteSpace.substring(strIndex+SUFFIX_SLB_VPORT.length());		
//			//  vport tcp:80
//			String element[] = input.split(" ");// space를 기준으로 분리.
//			if(element.length != 1)
//			{
//				return;
//			}
//			
//			// proto:port 형태로 전달됨.
//			String [] protPort = element[0].split(":");
//			if(protPort.length != 2)
//			{
//				return;
//			}
//			this.vsObj.setSrvPort(Integer.parseInt(protPort[1]));
//			this.vsObj.setSrvProtocol(convertProtocol(protPort[0]));
//			return;
//		}	
//		
//		// enable
//		strIndex = rmWhiteSpace.indexOf(SUFFIX_SLB_ENABLE);
//		if(strIndex>=0)
//		{
//			//  enable
//			this.vsObj.setState(OBDefine.STATE_ENABLE);
//			return;
//		}
//
//		// disalbe
//		strIndex = rmWhiteSpace.indexOf(SUFFIX_SLB_DISALBE);
//		if(strIndex>=0)
//		{
//			//  enable
//			this.vsObj.setState(OBDefine.STATE_DISABLE);
//			return;
//		}
//	}
//	// service 시작 문구를 찾는다.
//	private int procInitState(int currState, String input)
//	{
//		int strIndex = input.indexOf(SUFFIX_START_SERVICE);
//		if(strIndex>=0)
//			return PARSE_STATE_SERV;
//		
//		return currState;
//	}

//	PAS-K# show running-config slb
//	!
//	! PIOLINK switch configuration (PLOS-PASK-v1.5.0.0)
//	! 2013/04/19 15:37:38
//	!
//	! Slb configuration
//	!
//	slb web_80
//	priority 50
//	nat-mode dnat
//	lb-method rr
//	session-sync disable
//	fail-skip enable
//	status enable
//	health-check 1
//	vip 192.168.201.111 protocol tcp vport 80
//	sticky time 60
//	sticky source-subnet 255.255.255.255
//	real 1
//	real 2
//	slow-start rate 5
//	slow-start timer 60
//	apply
//	slb ykkim1
//	priority 50
//	nat-mode dnat
//	lb-method rr
//	session-sync disable
//	fail-skip disable
//	status enable
//	health-check 1,2
//	vip 10.10.10.111 protocol tcp vport 80
//	vip 10.10.10.112 protocol tcp vport 8080,9999
//	sticky time 60
//	sticky source-subnet 255.255.255.255
//	real 1
//	real 2
//	real 3
//	slow-start rate 5
//	slow-start timer 60
//	apply

//	public static void main(String [] args) 
//	{
//		try
//		{
//			OBAdcPASK handler = new OBAdcPASK("192.168.200.110", "root", "admin");
//			//handler.enter();
//			handler.login();
//			System.out.println("login OK");
//			
//			String result = handler.cmndHealthDump();//
//			System.out.println(result);
//			System.out.println("command OK");
//			
//			System.out.println(new OBCLIParserPASK().parseHealthCheckInfo(result));
//			
//			handler.disconnect();
//			System.out.println("logout OK");
//
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}
	private final static String SUFFIX_HEALTH_ID = "health-check ";
	private final static String SUFFIX_HEALTH_TYPE = "type ";
	private final static String SUFFIX_HEALTH_PORT = "port "; // todo
	private final static String SUFFIX_HEALTH_INTERVAL = "interval "; // todo
	private final static String SUFFIX_HEALTH_TIMEOUT = "timeout "; // todo
	private final static String SUFFIX_HEALTH_STATE = "status ";
	private final static String SUFFIX_HEALTH_APPLY = "apply";

	private HashMap<Integer, OBDtoAdcHealthCheckPASK> parseHealthCheckInfo(Integer adcIndex, String input)
			throws OBException {
		HashMap<Integer, OBDtoAdcHealthCheckPASK> retVal = new HashMap<Integer, OBDtoAdcHealthCheckPASK>();// id를 key로
																											// 사용함.
		OBDtoAdcHealthCheckPASK obj = null;
		String lines[] = input.split("\n");
		int strIndex = 0;
		String healthcheckName;

		for (String line : lines) { // id 탐지
			line = line.replace("\r$", "");
			strIndex = line.indexOf(SUFFIX_HEALTH_ID);
			if (strIndex >= 0) {
				String item = line.substring(strIndex + SUFFIX_HEALTH_ID.length());
				obj = new OBDtoAdcHealthCheckPASK();
				obj.setId(Integer.parseInt(item));
				String dbIndex = OBCommon.makeHealthDbIndexPASK(adcIndex, obj.getId());
				obj.setDbIndex(dbIndex);

				continue;
			}
			// type tcp
			strIndex = line.indexOf(SUFFIX_HEALTH_TYPE);
			if (strIndex >= 0) {
				String item = line.substring(strIndex + SUFFIX_HEALTH_TYPE.length());
				String element[] = item.split(" ");// space를 기준으로 분리.
				if (element.length != 1) {
					OBSystemLog.info(OBDefine.LOGFILE_DEBUG, String.format("failed to parse a health cfg(%s)", line));
					continue;
				}
				obj.setType(OBCommon.convertHealthCheckTypeString2Integer(element[0]));
				continue;
			}
			// port 80
			strIndex = line.indexOf(SUFFIX_HEALTH_PORT);
			if (strIndex >= 0) {
				String item = line.substring(strIndex + SUFFIX_HEALTH_PORT.length());
				String element[] = item.split(" ");// space를 기준으로 분리.
				if (element.length != 1) {
					OBSystemLog.info(OBDefine.LOGFILE_DEBUG, String.format("failed to parse a health cfg(%s)", line));
					continue;
				}
				obj.setPort(Integer.valueOf(element[0]));
				continue;
			}
			// timeout 3
			strIndex = line.indexOf(SUFFIX_HEALTH_TIMEOUT);
			if (strIndex >= 0) {
				String item = line.substring(strIndex + SUFFIX_HEALTH_TIMEOUT.length());
				String element[] = item.split(" ");
				if (element.length != 1) {
					OBSystemLog.info(OBDefine.LOGFILE_DEBUG, String.format("failed to parse a health cfg(%s)", line));
					continue;
				}
				obj.setTimeout(Integer.valueOf(element[0]));
				continue;
			}
			// interval 3
			strIndex = line.indexOf(SUFFIX_HEALTH_INTERVAL);
			if (strIndex >= 0) {
				String item = line.substring(strIndex + SUFFIX_HEALTH_INTERVAL.length());
				String element[] = item.split(" ");
				if (element.length != 1) {
					OBSystemLog.info(OBDefine.LOGFILE_DEBUG, String.format("failed to parse a health cfg(%s)", line));
					continue;
				}
				obj.setInterval(Integer.valueOf(element[0]));
				continue;
			}
			// state
			strIndex = line.indexOf(SUFFIX_HEALTH_STATE);
			if (strIndex >= 0) {
				String item = line.substring(strIndex + SUFFIX_HEALTH_STATE.length());
				if (item.equals("enable"))
					obj.setState(OBDefine.STATE_ENABLE);
				else
					obj.setState(OBDefine.STATE_DISABLE);
				continue;
			}
			// apply
			strIndex = line.indexOf(SUFFIX_HEALTH_APPLY);
			if (strIndex >= 0) {
				// name format : 1: tcp/80 timeout 5, interval 3
				healthcheckName = String.format("%d: %s/%d timeout %d, interval %d", obj.getId(),
						OBCommon.convertHealthCheckTypeInteger2String(obj.getType()), obj.getPort(), obj.getTimeout(),
						obj.getInterval());
				obj.setName(healthcheckName);
				retVal.put(obj.getId(), obj);
				continue;
			}
		}
		return retVal;
	}

//	public static void main(String [] args) 
//	{
//		try
//		{
//			OBAdcPASK handler = new OBAdcPASK("192.168.200.110", "root", "admin");
//			//handler.enter();
//			handler.login();
//			System.out.println("login OK");
//			
//			String result = handler.cmndRealDump();//
//			System.out.println(result);
//			System.out.println("command OK");
//			
//			System.out.println(new OBCLIParserPASK().parseRealServerInfo(result));
//			
//			handler.disconnect();
//			System.out.println("logout OK");
//
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}

//	PAS-K# show running-config real 
//	!
//	! PIOLINK switch configuration (PLOS-PASK-v1.5.0.0)
//	! 2013/04/19 15:37:38
//	!
//	! Real configuration
//	!
//	real 1
//	rip 192.168.199.41
//	name r1
//	rport 80
//	priority 0
//	weight 1
//	graceful-shutdown disable
//	max-connection 0
//	pool-size 10000
//	pool-age 3600
//	pool-reuse 100
//	pool-srcmask 32
//	status enable
//	ip-ver ipv4
//	apply
//	exit
//	real 2
//	rip 192.168.199.42
//	name r2
//	rport 80
//	priority 0
//	weight 1
//	graceful-shutdown disable
//	max-connection 0
//	pool-size 10000
//	pool-age 3600
//	pool-reuse 100
//	pool-srcmask 32
//	status enable
//	ip-ver ipv4
//	apply
//	exit
//	real 3
//	rip 10.10.10.111
//	name r3
//	rport 8080
//	priority 0
//	weight 1
//	graceful-shutdown disable
//	max-connection 0
//	pool-size 10000
//	pool-age 3600
//	pool-reuse 100
//	pool-srcmask 32
//	status enable
//	ip-ver ipv4
//	apply
//	exit
//	PAS-K# 

	private final static String SUFFIX_REAL_ID = "real ";
	private final static String SUFFIX_REAL_NAME = "name ";
	private final static String SUFFIX_REAL_RIP = "rip ";
	private final static String SUFFIX_REAL_RPORT = "rport ";
	private final static String SUFFIX_REAL_STATE = "status ";
	private final static String SUFFIX_REAL_APPLY = "apply";

	private HashMap<Integer, OBDtoRealServerInfoPASK> parseRealServerInfo(Integer adcIndex, String input)
			throws OBException {
		HashMap<Integer, OBDtoRealServerInfoPASK> retVal = new HashMap<Integer, OBDtoRealServerInfoPASK>();// id를 key로함.
		OBDtoRealServerInfoPASK obj = null;
		String lines[] = input.split("\n");
		int strIndex = 0;
		for (String line : lines) {
			line = line.replace("\\r", "").replace("\\x20+$", "");
			// id
			strIndex = line.indexOf(SUFFIX_REAL_ID);
			if (strIndex >= 0) {
				String item = line.substring(strIndex + SUFFIX_REAL_ID.length());
				// real 1
				String element[] = item.split(" ");// space를 기준으로 분리.
				if (element.length != 1) {
					OBSystemLog.info(OBDefine.LOGFILE_DEBUG, String.format("failed to parse a real cfg(%s)", line));
					continue;
				}
				obj = new OBDtoRealServerInfoPASK();
				obj.setIndex(Integer.parseInt(element[0]));
				String dbIndex = OBCommon.makeNodeIndexPASK(adcIndex, obj.getIndex());
				obj.setDbIndex(dbIndex);
				continue;
			}
			// name
			strIndex = line.indexOf(SUFFIX_REAL_NAME);
			if (strIndex >= 0) {
				String item = line.substring(strIndex + SUFFIX_REAL_NAME.length());
				// name real1
				String element[] = item.split(" ");// space를 기준으로 분리.
				if (element.length != 1) {
					OBSystemLog.info(OBDefine.LOGFILE_DEBUG, String.format("failed to parse a real cfg(%s)", line));
					continue;
				}
				obj.setName(element[0]);
				continue;
			}
			// rip
			strIndex = line.indexOf(SUFFIX_REAL_RIP);
			if (strIndex >= 0) {
				String item = line.substring(strIndex + SUFFIX_REAL_RIP.length());
				// name real1
				String element[] = item.split(" ");// space를 기준으로 분리.
				if (element.length != 1) {
					OBSystemLog.info(OBDefine.LOGFILE_DEBUG, String.format("failed to parse a real cfg(%s)", line));
					continue;
				}
				obj.setIpAddress(element[0]);
				continue;
			}
			// rport
			strIndex = line.indexOf(SUFFIX_REAL_RPORT);
			if (strIndex >= 0) {
				String item = line.substring(strIndex + SUFFIX_REAL_RPORT.length());
				String element[] = item.split(" ");// space를 기준으로 분리.
				if (element.length != 1) {
					OBSystemLog.info(OBDefine.LOGFILE_DEBUG, String.format("failed to parse a real cfg(%s)", line));
					continue;
				}
				obj.setRport(Integer.parseInt(element[0]));
				continue;
			}
			// state
			strIndex = line.indexOf(SUFFIX_REAL_STATE);
			if (strIndex >= 0) {
				String item = line.substring(strIndex + SUFFIX_REAL_STATE.length());
				// name real1
				String element[] = item.split(" ");// space를 기준으로 분리.
				if (element.length != 1) {
					OBSystemLog.info(OBDefine.LOGFILE_DEBUG,
							String.format("failed to parse a real cfg(%s). len:%d, %s", line, element.length, item));
					continue;
				}
				if (element[0].equals("enable"))
					obj.setState(OBDefine.STATE_ENABLE);
				else
					obj.setState(OBDefine.STATE_DISABLE);
				continue;
			}
			// apply
			strIndex = line.indexOf(SUFFIX_REAL_APPLY);
			if (strIndex >= 0) {
				if (obj.getRport() == null) // rport가 없는 경우, "rport 80"라인이 아예 없으므로 rport가 null 상태이다.
											// OBDefine.PORT_NA(-1)로 바꾼다.
				{
					obj.setRport(OBDefine.PORT_NA);
				}
				retVal.put(obj.getIndex(), obj);
			}
		}
		return retVal;
	}

//	public static void main(String [] args) 
//	{
//		try
//		{
//			OBAdcPASK handler = new OBAdcPASK("192.168.200.110", "root", "admin");
//			//handler.enter();
//			handler.login();
//			System.out.println("login OK");
//			
//			String slbDump = handler.cmndSlbDump();//
//			String healthDump = handler.cmndHealthDump();//
//			String realDump = handler.cmndRealDump();//
////			System.out.println(result);
//			System.out.println("command OK");
//			
//			System.out.println(new OBCLIParserPASK().getSlbInfo(1, slbDump, realDump, healthDump));
//			
//			handler.disconnect();
//			System.out.println("logout OK");
//
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}
//	
//	PAS-K# show running-config slb 
//	!
//	! PIOLINK switch configuration (PLOS-PASK-v1.5.0.0)
//	! 2013/04/19 15:37:38
//	!
//	! Slb configuration
//	!
//	slb web_80
//	priority 50
//	nat-mode dnat
//	lb-method rr
//	session-sync disable
//	fail-skip enable
//	status enable
//	health-check 1
//	vip 192.168.201.111 protocol tcp vport 80
//	sticky time 60
//	sticky source-subnet 255.255.255.255
//	real 1
//	real 2
//	slow-start rate 5
//	slow-start timer 60
//	apply
//	slb ykkim1
	private final static String SUFFIX_SLB_NAME = "slb ";
	private final static String SUFFIX_SLB_VIP = "vip ";

	private final static String SUFFIX_SLB_LBMETHOD = "lb-method ";
	private final static String SUFFIX_SLB_STATE = "status ";
	private final static String SUFFIX_SLB_HEALTHID = "health-check ";
	private final static String SUFFIX_SLB_REALID = "real ";
	private final static String SUFFIX_SLB_APPLY = "apply";

	/**
	 * Telnet으로 입력 받은 데이터를 파싱하여 SLB 정보를 추출한다.
	 * 
	 * @param input
	 * @return
	 * @throws OBException
	 */
	public OBDtoAdcConfigSlbPASK getSlbInfo(int adcIndex, String slbInput, String realInput, String healthInput)
			throws OBException {
		OBDtoAdcConfigSlbPASK retVal = new OBDtoAdcConfigSlbPASK();
		HashMap<Integer, OBDtoRealServerInfoPASK> realMap = parseRealServerInfo(adcIndex, realInput);
		HashMap<Integer, OBDtoAdcHealthCheckPASK> healthMap = parseHealthCheckInfo(adcIndex, healthInput);
		ArrayList<OBDtoAdcVServerPASK> vsList = new ArrayList<OBDtoAdcVServerPASK>();
		OBDtoAdcVServerPASK vsObj = null;
		String lines[] = slbInput.split("\n");
		int strIndex = 0;
		int vipCount = 0; // VS당 virtual ip가 복수일 수 있어서 개수를 파악한다.
		String healthcheckStr = "";

		String currentRange = "NON-SLB"; // 현재 parsing 영역을 나타낸다. NON-SLB, SLB 두개로 나눈다.

		for (String line : lines) {
			if (line.startsWith(SUFFIX_SLB_NAME) == true) // slb 시작
			{
				currentRange = "SLB";
				String input = line.substring(strIndex + SUFFIX_SLB_NAME.length());
				String rmWhiteSpace = OBParser.removeFirstWhitespace(input);
				// slb 이름을 추출한다. ex: slb yyh_test_pio1818
				String element[] = rmWhiteSpace.split(" ");// space를 기준으로 분리.
				if (element.length != 1) {
					OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("failed to parse a slb cfg(%s)", line));
					continue;
				}
				OBDtoAdcPoolPASK poolObj = new OBDtoAdcPoolPASK();
				poolObj.setMemberList(new ArrayList<OBDtoAdcPoolMemberPASK>()); // pool member가 없어도 list가 null이면 안된다.
																				// 객체는 있음
				vsObj = new OBDtoAdcVServerPASK();
				vsObj.setPool(poolObj);
				vsObj.setAdcIndex(adcIndex);
				// pool 이름은 vs 이름과 동일하게 한다.
				vsObj.setName(element[0]);
				poolObj.setName(element[0]);
				// vs db index 설정.
				String vsDbIndex = OBCommon.makeVSIndexPAS(adcIndex, vsObj.getName());
				vsObj.setDbIndex(vsDbIndex);
				// pool db index 설정.
				String poolDbIndex = OBCommon.makePoolIndex(adcIndex, vsObj.getPool().getName());
				poolObj.setDbIndex(poolDbIndex);
				continue;
			}

			// lb-method
			strIndex = line.indexOf(SUFFIX_SLB_LBMETHOD);
			if (strIndex >= 0) {
				String input = line.substring(strIndex + SUFFIX_SLB_LBMETHOD.length());
				String rmWhiteSpace = OBParser.removeFirstWhitespace(input);
				// lb-method rr
				String element[] = rmWhiteSpace.split(" ");// space를 기준으로 분리.
				if (element.length != 1) {
					OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("failed to parse a slb cfg(%s)", line));
					continue;
				}
				vsObj.getPool().setLbMethod(convertLBMethod(element[0]));
				continue;
			}

			// vip
			strIndex = line.indexOf(SUFFIX_SLB_VIP);
			if (strIndex >= 0) {
				String input = line.substring(strIndex + SUFFIX_SLB_VIP.length());
				String rmWhiteSpace = OBParser.removeFirstWhitespace(input);
				// vip 21.21.21.255
				String element[] = rmWhiteSpace.split(" ");// space를 기준으로 분리.
				if (element.length < 1) {
					OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("failed to parse a slb cfg(%s)", line));
					continue;
				}
				vipCount++; // vip 조건 OK

				// vip의 5 구성요소가 모두 들어간 첫번째 엔트리 IP와 port만 유효하다.
				// 아래와 같은 복수의 설정이 같은 vs에 있을 수 있는데 11.11.11.11의 tcp 80을 대표 포트로 뽑는다.
				// vip 2.2.2.2
				// vip 3.3.3.3 protocol udp
				// vip 11.11.11.11 protocol tcp vport 80,8080,8888
				// vip 11.11.11.11 protocol udp vport 80,880,889,8080,8888

				if (vipCount == 1) {
					vsObj.setvIP(element[0]); // ip
					vsObj.setSrvProtocol(0); // 기본값 설정
					vsObj.setSrvPort(0); // 기본값 설정
					vsObj.setSubInfo(line); // TMP_SLB_VSERVER.SUB_INFO에 넣을 정보
				} else {
					vsObj.setSubInfo(String.format("%s\n%s", vsObj.getSubInfo(), line)); // subInfo를 new line으로 붙인다.
				}

				// protocol vport
				if (element.length >= 3) // 없을 수도 잇다.
				{
					if (vipCount == 1) {
						vsObj.setSrvProtocol(convertProtocol(element[2])); // 대표 IP이면 protocol을 설정한다.
					}
				}
				if (element.length >= 5) // 없을 수도 있다.
				{
					// port가 comma로 연결된 여러개일 수 있다. space는 없다.첫번째 것만 쓴다.
					String vports[] = element[4].split(",");
					if (vipCount == 1) {
						vsObj.setSrvPort(Integer.valueOf(vports[0])); // 대표 IP인 경우 port를 설정한다.
					}
				}
				continue;
			}

			// state
			strIndex = line.indexOf(SUFFIX_SLB_STATE);
			if (strIndex >= 0) {
				if (currentRange.equals("SLB") == true) {
					String input = line.substring(strIndex + SUFFIX_SLB_STATE.length());
					String rmWhiteSpace = OBParser.removeFirstWhitespace(input);
					if (rmWhiteSpace.equals("enable"))
						vsObj.setState(OBDefine.STATE_ENABLE);
					else
						vsObj.setState(OBDefine.STATE_DISABLE);
				}
				continue;
			}
			// healthcheck
			strIndex = line.indexOf(SUFFIX_SLB_HEALTHID);
			if (strIndex >= 0) {
				String input = line.substring(strIndex + SUFFIX_SLB_HEALTHID.length());
				String rmWhiteSpace = OBParser.removeFirstWhitespace(input);
				String element[] = rmWhiteSpace.split(",");
				int healthID = Integer.parseInt(element[0]);
				OBDtoAdcHealthCheckPASK healthObj = healthMap.get(healthID);
				vsObj.getPool().setHealthCheckInfo(healthObj);
				healthcheckStr = line; // 나중에 subInfo에 추가해야 하므로 keep해 둔다. healthcheck가 vip 앞서 나오기 때문에 keep
				continue;
			}
			// real server
			strIndex = line.indexOf(SUFFIX_SLB_REALID);
			if (strIndex >= 0) {
				String input = line.substring(strIndex + SUFFIX_SLB_REALID.length());
				String rmWhiteSpace = OBParser.removeFirstWhitespace(input);
				int realID = Integer.parseInt(rmWhiteSpace);
				OBDtoRealServerInfoPASK realObj = realMap.get(realID);

				String nodeIndex = OBCommon.makeNodeIndexPASK(adcIndex, realID);

				OBDtoAdcPoolMemberPASK memObj = new OBDtoAdcPoolMemberPASK();
				String dbIndex = OBCommon.makePoolMemberIndexPASK(adcIndex, vsObj.getDbIndex(), nodeIndex,
						realObj.getRport());
				memObj.setDbIndex(dbIndex);
				memObj.setId(realObj.getIndex());
				memObj.setIpAddress(realObj.getIpAddress());
				memObj.setName(realObj.getName());
				memObj.setPort(realObj.getRport());
				memObj.setState(realObj.getState());

				vsObj.getPool().getMemberList().add(memObj);
				continue;
			}
			// apply
			strIndex = line.indexOf(SUFFIX_SLB_APPLY);
			if (strIndex >= 0) {
				if (currentRange.equals("SLB") == true) {
					if (healthcheckStr.isEmpty() == false) {
						vsObj.setSubInfo(String.format("%s\n%s", vsObj.getSubInfo(), healthcheckStr));
					}
					if (vsObj.getSrvPort() <= 0) {
						vsObj.setSrvPort(OBDefine.PORT_NA);
					}
					if (vsObj.getSrvProtocol() <= 0) {
						vsObj.setSrvProtocol(OBDefine.PROTOCOL_NA);
					}
					vsList.add(vsObj);
					vipCount = 0;
					healthcheckStr = "";
					currentRange = "NON-SLB";
				} else {
					vipCount = 0;
					healthcheckStr = "";
					currentRange = "NON-SLB";
				}
			}
		}
		retVal.setVsList(vsList);
		retVal.setRealList(new ArrayList<OBDtoRealServerInfoPASK>(realMap.values()));
		retVal.setHealthList(new ArrayList<OBDtoAdcHealthCheckPASK>(healthMap.values()));
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

	/**
	 * telnet 데이터를 이용해서 VS별 status 값을 추출한다.
	 * 
	 * @param input
	 * @return
	 * @throws OBException
	 */
//	public ArrayList<OBDtoVSStatusPAS> getVSStatus(String input) throws OBException
//	{
//		ArrayList<OBDtoVSStatusPAS> retVal = new ArrayList<OBDtoVSStatusPAS>();
////		String inText = "================================================================================"+
////				"SLB Configuration "+
////				"------------------------------------------------------------------------------ "+
////				"  Name   Priority LB Method Session-sync Status  Health  VIP             Protocol Vport"+    
////				"  web_80 50       rr        disable      enable  ACT     192.168.201.111 tcp      80     "+  
////				"  ykkim1 50       rr        disable      enable  INACT   10.10.10.111    tcp      80       "+
////				"                                                         10.10.10.112    tcp      8080,9999"+
////				"  ykkim2 50       rr		 disable      disable UNKNOWN 192.168.201.112 tcp      80       "+
////				"                                                         192.168.201.113 tcp      8080     "+
////				"  ykkim3 50       rr        disable      enable  ACT     192.168.201.112 tcp      8080     "+
////				"================================================================================";
//		// 1. 라인별로 처리한다.
//		// 2. 시작 라인을 지정한다.
//		// 3. 지정된 라인을 tab, 공백 문자열로 파싱한다.
//		// 4. 첫번째 문자열을 vsName으로 간주한다.
//		// 5. 6번째 문자열을 status로 간주한다.
//		// 6. ====== 까지 진행한다.
//		
//		String beginString = "SLB Configuration ";
//		int beginIndex = input.indexOf(beginString);
//		if(beginIndex == -1)
//		{// 데이터 없음.   
//			OBSystemLog.warn(OBDefine.LOGFILE_DEBUG, String.format("start_string not found"));
//			return retVal;
//		}
//		String endString = "=============================";
//		
//		String body = input.substring(beginIndex);
//		String lines[] = body.split("\n");
//		boolean startFlag=false;
//		for(String line:lines)
//		{
//			ArrayList<String>items = parseItems(line);
//			
//			if(startFlag==false)
//			{// 시작 포인트 찾음. 첫번째 문자열이 "Name"이고, 두번째 문자열이 "Priority"인 경우에 시작으로 간주함.
//				if(items.get(0).compareToIgnoreCase("Name") == 0)
//				{
//					if(items.get(1).compareToIgnoreCase("Priority") == 0)
//					{
//						startFlag=true;
//					}
//				}
//				continue;
//			}
//			
//			if(items.size()==1)
//			{// 종료 문자열 찾음.
//				if(items.get(0).compareTo(endString) == 0)
//					break;
//			}
//			if(items.size()!=9)
//			{
//				continue;
////				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("parsing error:%s", line));
//			}
//			
//			// 첫번째 문자열이 vsName을 의미하고, 6번째 문자열이 status를 의미한다. "ACT", "INACT", "UNKNOWN", "STANDBY"중에 하나 제공된다.
//			OBDtoVSStatusPAS obj = new OBDtoVSStatusPAS();
//			obj.setVsName(items.get(0));
//			obj.setStatus(convertVSStatus(items.get(5)));
//			retVal.add(obj);
//		}
//		return retVal;
//	}

//	private int convertVSStatus(String status)
//	{
//		if(status.compareTo("ACT")==0)
//			return OBDefine.STATUS_AVAILABLE;
//		if(status.compareTo("INACT")==0)
//			return OBDefine.STATUS_UNAVAILABLE;
//		if(status.compareTo("UNKNOWN")==0)
//			return OBDefine.STATUS_DISABLE;
//		if(status.compareTo("STANDBY")==0)
//			return OBDefine.STATUS_AVAILABLE;
//		return OBDefine.STATUS_AVAILABLE;// 
//	}
//	
//	private ArrayList<String> parseItems(String line) throws OBException
//	{
//		String [] items = line.split(" ");
//		ArrayList<String>retVal=new ArrayList<String>();
//		for(String item:items)
//		{
//			if(item!=null & !item.isEmpty())
//				retVal.add(item);
//		}
//		return retVal;
//	}

//	public static void main(String [] args) 
//	{
//		try
//		{
//			OBAdcPASK handler = new OBAdcPASK("192.168.200.110", "root", "admin");
//			//handler.enter();
//			handler.login();
//			String cfg = handler.cmndLicense();
//			handler.disconnect();
//			System.out.println(new OBCLIParserPASK().parseLicense(cfg));
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}

	private final static String SUFFIX_LICENSE_STATUS = "License status : ";
	private final static String SUFFIX_LICENSE_EXPIRED = "Expired date : ";

	public OBDtoLicenseInfoPASK parseLicense(String input) throws OBException {
		OBDtoLicenseInfoPASK retVal = new OBDtoLicenseInfoPASK();

		String lines[] = input.split("\n");
		for (String line : lines) {
			if (line.isEmpty())
				continue;
			int strIndex = 0;
			// status
			strIndex = line.indexOf(SUFFIX_LICENSE_STATUS);
			if (strIndex >= 0) {
				String item = line.substring(strIndex + SUFFIX_LICENSE_STATUS.length());
				retVal.setStatus(item);
				continue;
			}
			strIndex = line.indexOf(SUFFIX_LICENSE_EXPIRED);
			if (strIndex >= 0) {
				String item = line.substring(strIndex + SUFFIX_LICENSE_EXPIRED.length());
				retVal.setExpiredDate(item);
				continue;
			}
		}
		return retVal;
	}

//	public static void main(String [] args) 
//	{
//		try
//		{
//			OBAdcPASK handler = new OBAdcPASK("192.168.200.110", "root", "admin");
//			//handler.enter();
//			handler.login();
//			String cfg = handler.cmndSystem();
//			handler.disconnect();
//			System.out.println(new OBCLIParserPASK().parseSystem(cfg));
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}

	private final static String SUFFIX_SYSTEM_PNAME = "Product Name : "; // product name
	private final static String SUFFIX_SYSTEM_SERIAL = "Serial Number : ";// serial number
	private final static String SUFFIX_SYSTEM_VERSION = "OS version : "; // version, PASK1.5:"PLOS version" --> PASK1.7:
																			// "OS version", 둘다 수용하게 바꿈.
	private final static String SUFFIX_SYSTEM_UPTIME = "System Uptime : ";// system uptime
//	private final static String SUFFIX_VERSION_HEAD = "PLOS-PASK-v"; // example: "PLOS-PASK-v1.5.0.0" //
	// PLOS-PASK-v1.7.0.0.1 // v1.8.8.0.0

	public OBDtoSystemInfoPASK parseSystem(String input) throws OBException {
		OBDtoSystemInfoPASK retVal = new OBDtoSystemInfoPASK();
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
				String item = line.substring(SUFFIX_SYSTEM_VERSION.length());
				Pattern pattern = Pattern.compile("[^v][0-9.]+");
				Matcher match = pattern.matcher(item);

				String version = null;

				if (match.find()) { // 이미지 태그를 찾았다면,,
					version = match.group(0); // 글 내용 중에 첫번째 이미지 태그를 뽑아옴.
				}

				retVal.setVersion(version);
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

	public String parseSwVersion(String input) throws OBException {
		String lines[] = input.split("\n");

		for (String line : lines) {
			if (line.isEmpty())
				continue;
			int strIndex = 0;

			strIndex = line.indexOf(SUFFIX_SYSTEM_VERSION);
			if (strIndex >= 0) {
				String item = line.substring(SUFFIX_SYSTEM_VERSION.length());
				Pattern pattern = Pattern.compile("[^v][0-9.]+");
				Matcher match = pattern.matcher(item);

				String version = null;

				if (match.find()) { // 이미지 태그를 찾았다면,,
					version = match.group(0); // 글 내용 중에 첫번째 이미지 태그를 뽑아옴.
				}
//				String item = line.substring(SUFFIX_SYSTEM_VERSION.length());
////				String version = item.substring(SUFFIX_VERSION_HEAD.length());
//				String version = item.replace("v", "");
				return version;
			}
		}
		return "";
	}

//	================================================================================
//			INTERFACE Configuration
//			 ------------------------------------------------------------------------------ 
//			  Name    Status MAC Address       IPv4 Address       Broadcast       IPv6 Address
//			  mgmt    up     00:06:c4:84:04:a2 192.168.100.1/24   192.168.100.255             
//			  vlan10  up     00:06:c4:84:04:a3 192.168.200.110/24 192.168.200.255             
//			  vlan20  up     00:06:c4:84:04:a3 192.168.201.110/24 192.168.201.255             
//			  default up     00:06:c4:84:04:a3                                                
//	================================================================================
	private final static String SUFFIX_INTERFACE_START = "Name Status";// Name Status MAC Address IPv4 Address
	private final static String SUFFIX_INTERFACE_END = "===========================";// status

	public ArrayList<OBDtoInterfaceInfoPASK> parseInterface(String input) throws OBException {
		ArrayList<OBDtoInterfaceInfoPASK> retVal = new ArrayList<OBDtoInterfaceInfoPASK>();
		String lines[] = input.split("\n");
		boolean startFlag = false;
		for (String line : lines) {
			if (line.isEmpty())
				continue;
			int strIndex = 0;

			if (startFlag == false) {
				strIndex = line.indexOf(SUFFIX_INTERFACE_START);
				if (strIndex >= 0) {
					startFlag = true;
				}
				continue;
			}
			strIndex = line.indexOf(SUFFIX_INTERFACE_END);
			if (strIndex >= 0) {
				break;
			}

			String rmWhiteSpace = OBParser.removeFirstWhitespace(line);
			String element[] = rmWhiteSpace.split(" ");// space를 기준으로 분리.

			OBDtoInterfaceInfoPASK obj = new OBDtoInterfaceInfoPASK();
			if (element.length > 1) {// name
				obj.setName(element[0]);
			}

			if (element.length > 2) {// status
				if (element[1].equals("up"))
					obj.setStatus(OBDefine.L2_LINK_STATUS_UP);
				else
					obj.setStatus(OBDefine.L2_LINK_STATUS_DOWN);
			}

			if (element.length > 3) {// mac address
				obj.setMacAddr(element[2]);
			}

			if (element.length > 4) {// ipv4 address
				obj.setIpAddr(element[3]);
			}

			retVal.add(obj);
		}
		return retVal;
	}

//	================================================================================
//			  RESOURCE
//			 ------------------------------------------------------------------------------
//			    Management           
//			        Cpu              
//			            Usage        : 3.96%
//
//			        Memory           
//			            Total        : 1536000 kB
//			            Used         : 653496 kB
//			            Free         : 882504 kB
//			            Usage        : 42.55%
//
//			    Packet               
//			        Cpu              
//			            Usage        : 1.98%
//
//			        Memory           
//			            Total        : 2246268 kB
//			            Used         : 1550576 kB
//			            Free         : 695692 kB
//			            Usage        : 69.03%
//			================================================================================

	private final static String SUFFIX_RESOURCE_MP_START = "Management ";//
	private final static String SUFFIX_RESOURCE_SP_START = "Packet ";//
	private final static String SUFFIX_RESOURCE_MP_CPU_START = "Cpu ";//
	private final static String SUFFIX_RESOURCE_SP_CPU_START = "Cpu ";//
	private final static String SUFFIX_RESOURCE_SP_MEM_START = "Memory ";//
	private final static String SUFFIX_RESOURCE_MP_MEM_START = "Memory ";//
	private final static String SUFFIX_RESOURCE_SP_CPU_USAGE = "Usage : ";//
	private final static String SUFFIX_RESOURCE_MP_CPU_USAGE = "Usage : ";//
	private final static String SUFFIX_RESOURCE_MP_MEM_TOTAL = "Total : ";//
	private final static String SUFFIX_RESOURCE_MP_MEM_USED = "Used : ";//
	private final static String SUFFIX_RESOURCE_MP_MEM_FREE = "Free : ";//
	private final static String SUFFIX_RESOURCE_MP_MEM_USAGE = "Usage : ";//
	private final static String SUFFIX_RESOURCE_SP_MEM_TOTAL = "Total : ";//
	private final static String SUFFIX_RESOURCE_SP_MEM_USED = "Used : ";//
	private final static String SUFFIX_RESOURCE_SP_MEM_FREE = "Free : ";//
	private final static String SUFFIX_RESOURCE_SP_MEM_USAGE = "Usage : ";//
	private final static String SUFFIX_RESOURCE_END = "==================================";//

	public OBDtoResourceInfoPASK parseResources(String input) throws OBException {
		OBDtoResourceInfoPASK retVal = new OBDtoResourceInfoPASK();
		String lines[] = input.split("\n");
		int parseStage = 0;// 0:초기. 1:mp start, 2: mp cpu start, 3. mp mem start, 4:sp sart, 5:sp cpu
							// start, 6: sp mem start

		for (String line : lines) {
			if (line.isEmpty())
				continue;
			int strIndex = 0;

			if (parseStage == 0) {// init stage
				strIndex = line.indexOf(SUFFIX_RESOURCE_MP_START);
				if (strIndex >= 0) {
					parseStage = 1;
				}
				continue;
			}

			if (parseStage == 1) {// mp cpu start 탐지.
				strIndex = line.indexOf(SUFFIX_RESOURCE_MP_CPU_START);
				if (strIndex >= 0) {
					parseStage = 2;
					continue;
				}
				continue;
			}

			if (parseStage == 2) {// mp cpu usage 추출.
									// sp cpu usage
				strIndex = line.indexOf(SUFFIX_RESOURCE_MP_CPU_USAGE);
				if (strIndex >= 0) {
					String item = line.substring(strIndex + SUFFIX_RESOURCE_MP_CPU_USAGE.length());
					String rmWhiteSpace = OBParser.removeFirstWhitespace(item);
					String element[] = rmWhiteSpace.split("\\.");// xx.xx% 형태임.
					retVal.setCpuUsageMP(Integer.parseInt(element[0]));
					continue;
				}
				strIndex = line.indexOf(SUFFIX_RESOURCE_MP_MEM_START);
				if (strIndex >= 0) {
					parseStage = 3;
					continue;
				}
				continue;
			}

			if (parseStage == 3) {
				strIndex = line.indexOf(SUFFIX_RESOURCE_SP_START);
				if (strIndex >= 0) {
					parseStage = 4;
					continue;
				}

				// memory total for mp
				strIndex = line.indexOf(SUFFIX_RESOURCE_MP_MEM_TOTAL);
				if (strIndex >= 0) {
					String item = line.substring(strIndex + SUFFIX_RESOURCE_MP_MEM_TOTAL.length());
					String rmWhiteSpace = OBParser.removeFirstWhitespace(item);
					String element[] = rmWhiteSpace.split(" ");
					retVal.setTotalMemMP(Long.parseLong(element[0]));
					continue;
				}

				// memory used
				strIndex = line.indexOf(SUFFIX_RESOURCE_MP_MEM_USED);
				if (strIndex >= 0) {
					String item = line.substring(strIndex + SUFFIX_RESOURCE_MP_MEM_USED.length());
					String rmWhiteSpace = OBParser.removeFirstWhitespace(item);
					String element[] = rmWhiteSpace.split(" ");
					retVal.setUsedMemMP(Long.parseLong(element[0]));
					continue;
				}
				// memory free
				strIndex = line.indexOf(SUFFIX_RESOURCE_MP_MEM_FREE);
				if (strIndex >= 0) {
					String item = line.substring(strIndex + SUFFIX_RESOURCE_MP_MEM_FREE.length());
					String rmWhiteSpace = OBParser.removeFirstWhitespace(item);
					String element[] = rmWhiteSpace.split(" ");
					retVal.setFreeMemMP(Long.parseLong(element[0]));
					continue;
				}
				// memory usage
				strIndex = line.indexOf(SUFFIX_RESOURCE_MP_MEM_USAGE);
				if (strIndex >= 0) {
					String item = line.substring(strIndex + SUFFIX_RESOURCE_MP_MEM_USAGE.length());
					String rmWhiteSpace = OBParser.removeFirstWhitespace(item);
					String element[] = rmWhiteSpace.split("\\.");// xx.xx% 형태임.
					retVal.setMemUsageMP(Integer.parseInt(element[0]));
					continue;
				}
				continue;
			}

			if (parseStage == 4) {// mp cpu start 탐지.
				strIndex = line.indexOf(SUFFIX_RESOURCE_SP_CPU_START);
				if (strIndex >= 0) {
					parseStage = 5;
					continue;
				}
				continue;
			}

			if (parseStage == 5) { // sp cpu usage
				strIndex = line.indexOf(SUFFIX_RESOURCE_SP_CPU_USAGE);
				if (strIndex >= 0) {
					String item = line.substring(strIndex + SUFFIX_RESOURCE_SP_CPU_USAGE.length());
					String rmWhiteSpace = OBParser.removeFirstWhitespace(item);
					String element[] = rmWhiteSpace.split("\\.");// xx.xx% 형태임.
					retVal.setCpuUsageSP(Integer.parseInt(element[0]));
					continue;
				}
				strIndex = line.indexOf(SUFFIX_RESOURCE_SP_MEM_START);
				if (strIndex >= 0) {
					parseStage = 6;
					continue;
				}
				continue;
			}

			if (parseStage == 6) {
				strIndex = line.indexOf(SUFFIX_RESOURCE_END);
				if (strIndex >= 0) {
					break;// 종료..
				}

				// memory total for mp
				strIndex = line.indexOf(SUFFIX_RESOURCE_SP_MEM_TOTAL);
				if (strIndex >= 0) {
					String item = line.substring(strIndex + SUFFIX_RESOURCE_SP_MEM_TOTAL.length());
					String rmWhiteSpace = OBParser.removeFirstWhitespace(item);
					String element[] = rmWhiteSpace.split(" ");//
					retVal.setTotalMemSP(Long.parseLong(element[0]));
					continue;
				}

				// memory used
				strIndex = line.indexOf(SUFFIX_RESOURCE_SP_MEM_USED);
				if (strIndex >= 0) {
					String item = line.substring(strIndex + SUFFIX_RESOURCE_SP_MEM_USED.length());
					String rmWhiteSpace = OBParser.removeFirstWhitespace(item);
					String element[] = rmWhiteSpace.split(" ");//
					retVal.setUsedMemSP(Long.parseLong(element[0]));
					continue;
				}
				// memory free
				strIndex = line.indexOf(SUFFIX_RESOURCE_SP_MEM_FREE);
				if (strIndex >= 0) {
					String item = line.substring(strIndex + SUFFIX_RESOURCE_SP_MEM_FREE.length());
					String rmWhiteSpace = OBParser.removeFirstWhitespace(item);
					String element[] = rmWhiteSpace.split(" ");//
					retVal.setFreeMemSP(Long.parseLong(element[0]));
					continue;
				}
				// memory usage
				strIndex = line.indexOf(SUFFIX_RESOURCE_SP_MEM_USAGE);
				if (strIndex >= 0) {
					String item = line.substring(strIndex + SUFFIX_RESOURCE_SP_MEM_USAGE.length());
					String rmWhiteSpace = OBParser.removeFirstWhitespace(item);
					String element[] = rmWhiteSpace.split("\\.");// xx.xx% 형태임.
					retVal.setMemUsageSP(Integer.parseInt(element[0]));
					continue;
				}
				continue;
			}
		}
		return retVal;
	}

//	PAS-K# show port
//
//	================================================================================
//	PORT Configuration
//	 ------------------------------------------------------------------------------ 
//	  Port  Link  Speed Duplex Auto Nego MDI/MDIX Flow  Status Cable  Description
//	  ge1   down  1000  --     enable    --       off   enable fiber             
//	  ge2   down  1000  --     enable    --       off   enable fiber             
//	  ge3   down  1000  --     enable    --       off   enable fiber             
//	  ge4   down  1000  --     enable    --       off   enable fiber             
//	  ge5   down  1000  --     enable    --       off   enable fiber             
//	  ge6   down  1000  --     enable    --       off   enable fiber             
//	  ge7   down  1000  --     enable    --       off   enable fiber             
//	  ge8   down  1000  --     enable    --       off   enable fiber             
//	  ge9   up    1000  full   enable    auto     off   enable copper            
//	  ge10  down  0     half   enable    auto     off   enable copper            
//	  ge11  down  0     half   enable    auto     off   enable copper            
//	  ge12  down  0     half   enable    auto     off   enable copper            
//	  ge13  up    100   full   enable    auto     off   enable copper            
//	  ge14  down  0     half   enable    auto     off   enable copper            
//	  ge15  down  0     half   enable    auto     off   enable copper            
//	  ge16  down  0     half   enable    auto     off   enable copper            
//	================================================================================	
	private final static String SUFFIX_PORT_UPDOWN_START = "Port Link Speed";//
	private final static String SUFFIX_PORT_UPDOWN_END = "===================";//

	public ArrayList<OBDtoPortInfoPASK> parsePortUpdown(String input) throws OBException {
		ArrayList<OBDtoPortInfoPASK> retVal = new ArrayList<OBDtoPortInfoPASK>();
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
			if (startFlag == false)
				continue;

			strIndex = line.indexOf(SUFFIX_PORT_UPDOWN_END);
			if (strIndex >= 0) {
				break;
			}

			String rmWhiteSpace = OBParser.removeFirstWhitespace(line);
			String element[] = rmWhiteSpace.split(" ");// space를 기준으로 분리. //1 GE up up auto(1000) auto(full) auto(MDI)
														// wan - off copper
			if (element.length < 9)
				continue;

			OBDtoPortInfoPASK obj = new OBDtoPortInfoPASK();

			obj.setPortName(element[0]);
			if (element[1].endsWith("up") == true)
				obj.setLinkStatus(OBDefine.L2_LINK_STATUS_UP);
			else
				obj.setLinkStatus(OBDefine.L2_LINK_STATUS_DOWN);
			obj.setSpeed(element[2]);
			obj.setDuplex(element[3]);
			retVal.add(obj);
		}
		return retVal;
	}

//PAS-K# show vlan
//
//================================================================================
//VLAN Configuration
// ------------------------------------------------------------------------------ 
//  VLAN Name VLAN ID Port  Type    
//  default   1       ge1   untagged
//	                    ge2   untagged
//	                    ge3   untagged
//	                    ge4   untagged
//	                    ge5   untagged
//	                    ge6   untagged
//	                    ge7   untagged
//	                    ge8   untagged
//	                    ge10  untagged
//	                    ge11  untagged
//	                    ge12  untagged
//	                    ge14  untagged
//	                    ge15  untagged
//	                    ge16  untagged
//  vlan10    10      ge9   untagged
//  vlan20    20      ge13  untagged
//================================================================================
//	(T:tagged port, U:untagged port, X:unavailable port)	
	private final static String SUFFIX_VLAN_INFO_START = "VLAN Name ";//
	private final static String SUFFIX_VLAN_INFO_END = "==========";//

	public ArrayList<OBDtoVLanInfoPASK> parseVlanInfo(String input) throws OBException {
		ArrayList<OBDtoVLanInfoPASK> retVal = new ArrayList<OBDtoVLanInfoPASK>();
		String lines[] = input.split("\n");
		OBDtoVLanInfoPASK obj = null;
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
			strIndex = line.indexOf(SUFFIX_VLAN_INFO_END);
			if (strIndex >= 0) {
				if (obj != null)
					retVal.add(obj);
				break;
			}

			String rmWhiteSpace = OBParser.removeFirstWhitespace(line);
			String element[] = rmWhiteSpace.split(" ");// space를 기준으로 분리. //1 GE up up auto(1000) auto(full) auto(MDI)
														// wan - off copper
			if (element.length == 4) {
				obj = new OBDtoVLanInfoPASK();
				obj.setTaggedPortList(new ArrayList<String>());
				obj.setUntaggedPortList(new ArrayList<String>());
				obj.setUnavailabledPortList(new ArrayList<String>());

				obj.setName(element[0]);
				obj.setId(Integer.parseInt(element[1]));
				if (element[3].equals("untagged"))
					obj.getUntaggedPortList().add(element[2]);
				else if (element[3].equals("X:unavailable"))
					obj.getUnavailabledPortList().add(element[2]);
				else
					obj.getUnavailabledPortList().add(element[2]);
				retVal.add(obj);
				continue;
			}
			if (element.length == 2) {
				if (obj == null)
					continue;
				if (element[1].equals("untagged"))
					obj.getUntaggedPortList().add(element[0]);
				else if (element[1].equals("X:unavailable"))
					obj.getUnavailabledPortList().add(element[0]);
				else
					obj.getUnavailabledPortList().add(element[0]);
				continue;
			}
		}
		return retVal;
	}

//	================================================================================
//			  LOGGING
//			 ------------------------------------------------------------------------------
//			    Facility             : all
//			    Level                : notice
//			    Server Status        : enable
//
//			    Server               
//			       IP Address    Facility Level Agent Facility
//			       172.172.2.209                              
//			       172.172.2.222 all      info                
//			                     syslog   info                
//	================================================================================
	private final static String SUFFIX_SYSINFO_ENABLE = "Server Status : ";//
	private final static String SUFFIX_SYSINFO_START = "IP Address Facility";//
	private final static String SUFFIX_SYSINFO_END = "========================================";//

	public ArrayList<OBDtoSyslogInfoPASK> parseSyslogInfo(String input) throws OBException {
		ArrayList<OBDtoSyslogInfoPASK> retVal = new ArrayList<OBDtoSyslogInfoPASK>();
		String lines[] = input.split("\n");
		boolean startFlag = false;
		int enableDisable = OBDefine.STATE_DISABLE;
		String prevIPAddress = "";
		for (String line : lines) {
			if (line.isEmpty())
				continue;

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

			strIndex = line.indexOf(SUFFIX_SYSINFO_START);
			if (strIndex >= 0) {
				startFlag = true;
				continue;
			}
			if (startFlag == false)
				continue;

			strIndex = line.indexOf(SUFFIX_SYSINFO_END);
			if (strIndex >= 0) {
				break;
			}

			String rmWhiteSpace = OBParser.removeFirstWhitespace(line);
			String element[] = rmWhiteSpace.split(" ");// space를 기준으로 분리.
			if (element.length < 1)
				continue;

			boolean isIPAddressExist = false;
			OBDtoSyslogInfoPASK obj = new OBDtoSyslogInfoPASK();
			obj.setStatus(enableDisable);

			String tmpItem[] = element[0].split("\\.");// IP 주소인지 검사.
			if (tmpItem.length > 0) {
				isIPAddressExist = true;
			}

			if (isIPAddressExist == true) {
				obj.setIpAddress(element[0]);
				if (element.length >= 2)
					obj.setFacility(element[1]);
				if (element.length >= 3)
					obj.setLevel(element[2]);
			} else {// facility, level등의 정보만 있는 경
				if (prevIPAddress.isEmpty())
					continue;
				obj.setIpAddress(prevIPAddress);
				if (element.length >= 2)
					obj.setFacility(element[0]);
				if (element.length >= 3)
					obj.setLevel(element[1]);
			}
			retVal.add(obj);
		}
		return retVal;
	}

//	PAS-K# show ntp 
//
//	================================================================================
//	  NTP
//	 ------------------------------------------------------------------------------
//	    Status               : disable
//	    Primary Server       :
//	    Secondary Server     :
//	================================================================================
//
//	PAS-K# 
	private final static String SUFFIX_NTPINFO_START = "-----------------------";//
	private final static String SUFFIX_NTPINFO_STATUS = "Status : ";//
	private final static String SUFFIX_NTPINFO_PRIMARY = "Primary Server : ";//
	private final static String SUFFIX_NTPINFO_END = "=======================";//

	public OBDtoNTPInfoPASK parseNTPInfo(String input) throws OBException {
		OBDtoNTPInfoPASK retVal = new OBDtoNTPInfoPASK();
		String lines[] = input.split("\n");
		boolean startFlag = false;
		for (String line : lines) {
			if (line.isEmpty())
				continue;
			int strIndex = 0;

			if (startFlag == false) {
				strIndex = line.indexOf(SUFFIX_NTPINFO_START);
				if (strIndex >= 0) {
					startFlag = true;
				}
				continue;
			}
			strIndex = line.indexOf(SUFFIX_NTPINFO_END);
			if (strIndex >= 0) {
				break;
			}

			strIndex = line.indexOf(SUFFIX_NTPINFO_STATUS);
			if (strIndex >= 0) {
				String item = line.substring(strIndex + SUFFIX_NTPINFO_STATUS.length());
				if (item.endsWith("disabled") == true)
					retVal.setStatus(OBDefine.NTP_STATE_DISABLED);
				else
					retVal.setStatus(OBDefine.NTP_STATE_ENABLED);
				continue;
			}
			strIndex = line.indexOf(SUFFIX_NTPINFO_PRIMARY);
			if (strIndex >= 0) {
				String item = line.substring(strIndex + SUFFIX_NTPINFO_PRIMARY.length());
				retVal.setPrimary(item);
				continue;
			}
		}
		return retVal;
	}

//	PAS-K# show port-statistics 
//
//	================================================================================
//	  PORT-STATISTICS
//	 ------------------------------------------------------------------------------
//	    Port-Statistics      
//	       Port  RxBytes    RxPackets RxErrs RxDrop RxMulticast TxBytes    TxPackets TxErrs TxDrop
//	       ge1   0          0         0      0      0           0          0         0      0     
//	       ge2   0          0         0      0      0           0          0         0      0     
//	       ge3   0          0         0      0      0           0          0         0      0     
//	       ge4   0          0         0      0      0           0          0         0      0     
//	       ge5   0          0         0      0      0           0          0         0      0     
//	       ge6   0          0         0      0      0           0          0         0      0     
//	       ge7   0          0         0      0      0           0          0         0      0     
//	       ge8   0          0         0      0      0           0          0         0      0     
//	       ge9   306864057  3761498   0      869    1670046     337937272  2002238   0      0     
//	       ge10  0          0         0      0      0           0          0         0      0     
//	       ge11  0          0         0      0      0           0          0         0      0     
//	       ge12  0          0         0      0      0           0          0         0      0     
//	       ge13  1783690702 26045991  0      731    15550156    1281573918 17872896  0      0     
//	       ge14  4544       71        0      0      63          488        5         0      0     
//	       ge15  0          0         0      0      0           0          0         0      0     
//	       ge16  0          0         0      0      0           0          0         0      0     
//	================================================================================
//
//	PAS-K# 
	private final static String SUFFIX_PORTSTAT_START = "Port RxBytes RxPackets";//
	private final static String SUFFIX_PORTSTAT_END = "========================";//

	public ArrayList<OBDtoPortStatPASK> parsePortStatistics(String input) throws OBException {
		ArrayList<OBDtoPortStatPASK> retVal = new ArrayList<OBDtoPortStatPASK>();
		String lines[] = input.split("\n");
		boolean startFlag = false;
		for (String line : lines) {
			if (line.isEmpty())
				continue;
			int strIndex = 0;

			if (startFlag == false) {
				strIndex = line.indexOf(SUFFIX_PORTSTAT_START);
				if (strIndex >= 0) {
					startFlag = true;
				}
				continue;
			}

			strIndex = line.indexOf(SUFFIX_PORTSTAT_END);
			if (strIndex >= 0) {
				break;
			}

			String rmWhiteSpace = OBParser.removeFirstWhitespace(line);
			String element[] = rmWhiteSpace.split(" ");// space를 기준으로 분리.
			if (element.length != 10)
				continue;
			OBDtoPortStatPASK obj = new OBDtoPortStatPASK();
			obj.setPortName(element[0]);
			obj.setRxBytes(Long.parseLong(element[1]));
			obj.setRxPkts(Long.parseLong(element[2]));
			obj.setRxErrros(Long.parseLong(element[3]));
			obj.setRxDiscards(Long.parseLong(element[4]));
			obj.setTxBytes(Long.parseLong(element[6]));
			obj.setTxPkts(Long.parseLong(element[7]));
			obj.setTxErrros(Long.parseLong(element[8]));
			obj.setTxDiscards(Long.parseLong(element[9]));
			retVal.add(obj);
		}
		return retVal;
	}

//	PAS-K# show hardwarestatus 
//
//	================================================================================
//	  HARDWARESTATUS
//	 ------------------------------------------------------------------------------
//	    Temperature          
//	       Core  Degree                                    
//	       0     +43.0 C  (high = +82.0 C, crit = +102.0 C)
//	       1     +37.0 C  (high = +82.0 C, crit = +102.0 C)
//
//	    Voltage              
//	        Power1           : OFF
//	        Power2           : ON
//
//	    Fan                  
//	        Fan1             : ON
//	        Fan2             : ON
//	        Fan3             : ON
//	        Fan4             : ON
//	================================================================================
	private final static String SUFFIX_HWSTAT_AC1 = "Power1 : ";//
	private final static String SUFFIX_HWSTAT_AC2 = "Power2 : ";//
	private final static String SUFFIX_HWSTAT_FAN1 = "Fan1 : ";//
	private final static String SUFFIX_HWSTAT_FAN2 = "Fan2 : ";//
	private final static String SUFFIX_HWSTAT_FAN3 = "Fan3 : ";//
	private final static String SUFFIX_HWSTAT_FAN4 = "Fan4 : ";//

	public OBDtoHWStatPASK parseHWStatistics(String input) {
		OBDtoHWStatPASK retVal = new OBDtoHWStatPASK();
		String lines[] = input.split("\n");
		for (String line : lines) {
			if (line.isEmpty())
				continue;
			int strIndex = 0;

			strIndex = line.indexOf(SUFFIX_HWSTAT_FAN4);
			if (strIndex >= 0) {
				String item = line.substring(strIndex + SUFFIX_HWSTAT_FAN4.length());
				String rmWhiteSpace = OBParser.removeFirstWhitespace(item);
				String element[] = rmWhiteSpace.split(" ");// space를 기준으로 분리.
				if (element.length != 1)
					continue;
				if (element[0].endsWith("ON") == true)
					retVal.setFan4Status(OBDefine.SYS_FAN_STATUS_OK);
				else
					retVal.setFan4Status(OBDefine.SYS_FAN_STATUS_FAIL);
				continue;
			}
			strIndex = line.indexOf(SUFFIX_HWSTAT_FAN3);
			if (strIndex >= 0) {
				String item = line.substring(strIndex + SUFFIX_HWSTAT_FAN3.length());
				String rmWhiteSpace = OBParser.removeFirstWhitespace(item);
				String element[] = rmWhiteSpace.split(" ");// space를 기준으로 분리.
				if (element.length != 1)
					continue;
				if (element[0].endsWith("ON") == true)
					retVal.setFan3Status(OBDefine.SYS_FAN_STATUS_OK);
				else
					retVal.setFan3Status(OBDefine.SYS_FAN_STATUS_FAIL);
				continue;
			}
			strIndex = line.indexOf(SUFFIX_HWSTAT_FAN2);
			if (strIndex >= 0) {
				String item = line.substring(strIndex + SUFFIX_HWSTAT_FAN2.length());
				String rmWhiteSpace = OBParser.removeFirstWhitespace(item);
				String element[] = rmWhiteSpace.split(" ");// space를 기준으로 분리.
				if (element.length != 1)
					continue;
				if (element[0].endsWith("ON") == true)
					retVal.setFan2Status(OBDefine.SYS_FAN_STATUS_OK);
				else
					retVal.setFan2Status(OBDefine.SYS_FAN_STATUS_FAIL);
				continue;
			}
			strIndex = line.indexOf(SUFFIX_HWSTAT_FAN1);
			if (strIndex >= 0) {
				String item = line.substring(strIndex + SUFFIX_HWSTAT_FAN1.length());
				String rmWhiteSpace = OBParser.removeFirstWhitespace(item);
				String element[] = rmWhiteSpace.split(" ");// space를 기준으로 분리.
				if (element.length != 1)
					continue;
				if (element[0].endsWith("ON") == true)
					retVal.setFan1Status(OBDefine.SYS_FAN_STATUS_OK);
				else
					retVal.setFan1Status(OBDefine.SYS_FAN_STATUS_FAIL);
				continue;
			}
			strIndex = line.indexOf(SUFFIX_HWSTAT_AC2);
			if (strIndex >= 0) {
				String item = line.substring(strIndex + SUFFIX_HWSTAT_AC2.length());
				String rmWhiteSpace = OBParser.removeFirstWhitespace(item);
				String element[] = rmWhiteSpace.split(" ");// space를 기준으로 분리.
				if (element.length != 1)
					continue;
				if (element[0].endsWith("ON") == true)
					retVal.setAc2Status(OBDefine.SYS_POWERSUPPLY_STATUS_OK);
				else
					retVal.setAc2Status(OBDefine.SYS_POWERSUPPLY_STATUS_FAIL);
				continue;
			}
			strIndex = line.indexOf(SUFFIX_HWSTAT_AC1);
			if (strIndex >= 0) {
				String item = line.substring(strIndex + SUFFIX_HWSTAT_AC1.length());
				String rmWhiteSpace = OBParser.removeFirstWhitespace(item);
				String element[] = rmWhiteSpace.split(" ");// space를 기준으로 분리.
				if (element.length != 1)
					continue;
				if (element[0].endsWith("ON") == true)
					retVal.setAc1Status(OBDefine.SYS_POWERSUPPLY_STATUS_OK);
				else
					retVal.setAc1Status(OBDefine.SYS_POWERSUPPLY_STATUS_FAIL);
				continue;
			}
		}
		return retVal;
	}

//	PAS-K# show snmp 
//
//	================================================================================
//	  SNMP
//	 ------------------------------------------------------------------------------
//	    Status               : enable
//
//	    Trap                 
//	        Link Up          : disable
//	        Link Down        : disable
//	        Cold Start       : disable
//
//	        Host             : 
//
//	    System               
//	        Name             :
//	        Contact          :
//	        Location         :
//
//	    User                 : 
//	================================================================================
	private final static String SUFFIX_SNMP_NAME = "Name :";//

	public OBDtoSnmpInfoPASK parseSnmpInfo(String input) {
		OBDtoSnmpInfoPASK retVal = new OBDtoSnmpInfoPASK();
		String lines[] = input.split("\n");
		for (String line : lines) {
			if (line.isEmpty())
				continue;
			int strIndex = 0;

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

//	public static void main(String [] args) 
//	{
//		try
//		{
//			OBAdcPASK handler = new OBAdcPASK("192.168.200.110", "root", "admin");
//			//handler.enter();
//			handler.login();
//			System.out.println("login OK");
//			
//			String cfgDump = handler.cmndShowInfoSlb();//
////			System.out.println(result);
//			System.out.println("command OK");
//			
//			System.out.println(new OBCLIParserPASK().parseSlbStatus(9, cfgDump));
//			
//			handler.disconnect();
//			System.out.println("logout OK");
//
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}

	/**
	 * virtual server status를 계산한다. - virtual server state가 disable이면 "꺼짐" - virtual
	 * server state가 enable이면 -- 멤버가 살아 있으면 "정상" -- 멤버가 모두 죽어 있으면 "단절"
	 * 
	 * @param vsObj
	 * @return OBDefine.VS_STATUS.DISABLE / OBDefine.VS_STATUS.AVAILABLE /
	 *         OBDefine.VS_STATUS.UNAVAILABLE
	 * @throws OBException
	 */
	private Integer calcVSStatus(OBDtoAdcVServerPASK vsObj) throws OBException {
		if (vsObj.getState().equals(OBDefine.VS_STATE.DISABLED)) // virtual server가 disable이면 "꺼짐"
		{
			return OBDefine.VS_STATUS.DISABLE;
		} else // virtual server가 enable이면 멤버를 파악해서 "정상"/"단절"
		{
			for (OBDtoAdcPoolMemberPASK memObj : vsObj.getPool().getMemberList()) {
				if (memObj.getStatus() == OBDefine.MEMBER_STATUS.AVAILABLE) {
					return OBDefine.VS_STATUS.AVAILABLE;// 멤버 하나라도 available이면 availabe
				}
			}
			return OBDefine.VS_STATUS.UNAVAILABLE; // 모든 멤버가 멀쩡하지 않다.
		}
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
	private final static String SUFFIX_SLBSTATUS_SERVICE_START = "SLB: ";//
	private final static String SUFFIX_SLBSTATUS_SERVICE_END = "================================================================================";//
	private final static String SUFFIX_SLBSTATUS_VSNAME = "Name : ";//
	private final static String SUFFIX_SLBSTATUS_VSSTATE = "Status : ";//
	private final static String SUFFIX_SLBSTATUS_REAL_START = "ID Name RIP Rport";//
	private final static String SUFFIX_SLBSTATUS_VSIP_START = "VIP Protocol Vport";//
	private final static String SUFFIX_SLBSTATUS_VSIP_END = "Sticky";//
	private final static String SUFFIX_SLBSTATUS_REAL_END = "Health-Check-Info";//
	private final static String SUFFIX_SLBSTATUS_REAL_END_V17 = "Filter";

	public ArrayList<OBDtoAdcVServerPASK> parseSlbStatus(Integer adcIndex, String input) throws OBException {
		try {
			String lines[] = input.split("\n");
//			HashMap<String, OBDtoAdcVServerPASK> retVal	= new HashMap<String, OBDtoAdcVServerPASK>();
			ArrayList<OBDtoAdcVServerPASK> retVal = new ArrayList<OBDtoAdcVServerPASK>();
			OBDtoAdcVServerPASK vsObj = null;
			int currState = PARSE_STATE_INIT;
			for (String line : lines) {
				if (line.isEmpty())
					continue;

				int tmpState = checkCurState4SlbInfo(currState, line);

				switch (tmpState) {
				case PARSE_STATE_INIT:
					if (currState == PARSE_STATE_SLB) {// 하나의 slb 구성이 끝난 경우임.
						Integer vsStatus = calcVSStatus(vsObj);
						vsObj.setStatus(vsStatus);
						String dbIndex = OBCommon.makeVSIndexPASK(adcIndex, vsObj.getName());
						vsObj.setDbIndex(dbIndex);
						retVal.add(vsObj);
						currState = tmpState;
						continue;
					}
					currState = tmpState;
					break;
				case PARSE_STATE_SLB:// slb 정보 추출 상태.
					if (currState == PARSE_STATE_INIT) {// slb 시작.
						vsObj = new OBDtoAdcVServerPASK();
						OBDtoAdcPoolPASK poolObj = new OBDtoAdcPoolPASK();
						ArrayList<OBDtoAdcPoolMemberPASK> memberList = new ArrayList<OBDtoAdcPoolMemberPASK>();
						poolObj.setMemberList(memberList);
						vsObj.setStatus(OBDefine.VS_STATUS.UNAVAILABLE);
						vsObj.setPool(poolObj);
						currState = tmpState;
						continue;
					}
					vsObj = procSlbState4SlbInfo(line, vsObj);
					currState = tmpState;
					break;
				case PARSE_STATE_VIP:// slb 정보 추출 상태.
					if (currState == PARSE_STATE_SLB) {// vip 시작.
						currState = tmpState;
						continue;
					}
					vsObj = procVipState4SlbInfo(line, vsObj);
					currState = tmpState;
					break;
				case PARSE_STATE_REAL:// real 정보 추출 상태.
					if (currState == PARSE_STATE_SLB) {// real 시작.
						currState = tmpState;
						continue;
					}
					vsObj = procRealState4SlbVersion(line, vsObj, adcIndex);
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

	private OBDtoAdcVServerPASK procRealState4SlbVersion(String line, OBDtoAdcVServerPASK vsObj, Integer adcIndex)
			throws OBException {
		OBDtoAdcInfo adcInfo = new OBAdcManagementImpl().getAdcInfo(adcIndex);

		if (adcInfo.getSwVersion().contains("1.8.17.")) {
			procRealState4SlbInfoV1_8_17(line, vsObj, adcIndex);
		} else if (adcInfo.getSwVersion().contains("1.8.11.")) {
			procRealState4SlbInfoV1_8_17(line, vsObj, adcIndex);
		} else if (adcInfo.getSwVersion().contains("1.8.10.")) {
			procRealState4SlbInfoV1_8_17(line, vsObj, adcIndex);
		} else if (adcInfo.getSwVersion().contains("1.8.8.")) {
			procRealState4SlbInfoV1_8_17(line, vsObj, adcIndex);
		} else if (adcInfo.getSwVersion().contains("1.8.6.")) {
			procRealState4SlbInfoV1_8_17(line, vsObj, adcIndex);
		} else if (adcInfo.getSwVersion().contains("1.8.")) {
			procRealState4SlbInfoV1_8_17(line, vsObj, adcIndex);
		} else {
			procRealState4SlbInfo(line, vsObj, adcIndex);
		}

		return vsObj;
	}

//    ID    Name  RIP            Rport Backup Status Health Conns_active
//    1     r1    192.168.199.41 80           enable ACT    0           
//    2     r2    192.168.199.42 80           enable ACT    0           
//    3     r3    10.10.10.111   8080         enable INACT  0  
	private OBDtoAdcVServerPASK procRealState4SlbInfo(String line, OBDtoAdcVServerPASK vsObj, Integer adcIndex)
			throws OBException {
		OBDtoAdcInfo adcInfo = new OBAdcManagementImpl().getAdcInfo(adcIndex);

		String rmWhiteSpace = OBParser.removeFirstWhitespace(line);

		String element[] = rmWhiteSpace.split(" ");// space를 기준으로 분리.//1 real1 1.1.1.1:80 N/A disabled UNKNOWN enabled
		if (adcInfo.getSwVersion().contains("1.8.")) {
			if (element.length == 6) {
				OBDtoAdcPoolMemberPASK memObj = new OBDtoAdcPoolMemberPASK();
				memObj.setId(Integer.parseInt(element[0]));
				memObj.setName(element[1]);
				memObj.setIpAddress(element[2]);
				memObj.setPort(Integer.parseInt(element[3]));
				if (element[4].equals("ACT"))
					memObj.setState(OBDefine.MEMBER_STATE_PASK.ENABLED);
				else
					memObj.setState(OBDefine.MEMBER_STATE_PASK.DISABLED);

				memObj.setStatus(OBDefine.MEMBER_STATUS.DISABLE);
				if (memObj.getState() == OBDefine.MEMBER_STATE_PASK.ENABLED) {
					if (element[5].equals("enable"))
						memObj.setStatus(OBDefine.MEMBER_STATUS.AVAILABLE);
					else
						memObj.setStatus(OBDefine.MEMBER_STATUS.UNAVAILABLE);
				}
				vsObj.getPool().getMemberList().add(memObj);
			}
			if (element.length == 7) {
				OBDtoAdcPoolMemberPASK memObj = new OBDtoAdcPoolMemberPASK();
				memObj.setId(Integer.parseInt(element[0]));
				memObj.setName(element[1]);
				memObj.setIpAddress(element[2]);
				memObj.setPort(Integer.parseInt(element[3]));
				if (element[4].equals("ACT"))
					memObj.setState(OBDefine.MEMBER_STATE_PASK.ENABLED);
				else
					memObj.setState(OBDefine.MEMBER_STATE_PASK.DISABLED);

				memObj.setStatus(OBDefine.MEMBER_STATUS.DISABLE);
				if (memObj.getState() == OBDefine.MEMBER_STATE_PASK.ENABLED) {
					if (element[5].equals("enable"))
						memObj.setStatus(OBDefine.MEMBER_STATUS.AVAILABLE);
					else
						memObj.setStatus(OBDefine.MEMBER_STATUS.UNAVAILABLE);
				}
				vsObj.getPool().getMemberList().add(memObj);
			} else if (element.length == 8) {
				OBDtoAdcPoolMemberPASK memObj = new OBDtoAdcPoolMemberPASK();
				memObj.setId(Integer.parseInt(element[0]));
				memObj.setName(element[1]);
				memObj.setIpAddress(element[2]);
				memObj.setPort(Integer.parseInt(element[3]));
				if (element[5].equals("ACT"))
					memObj.setState(OBDefine.MEMBER_STATE_PASK.ENABLED);
				else
					memObj.setState(OBDefine.MEMBER_STATE_PASK.DISABLED);

				memObj.setStatus(OBDefine.MEMBER_STATUS.DISABLE);
				if (memObj.getState() == OBDefine.MEMBER_STATE_PASK.ENABLED) {
					if (element[6].equals("enable"))
						memObj.setStatus(OBDefine.MEMBER_STATUS.AVAILABLE);
					else
						memObj.setStatus(OBDefine.MEMBER_STATUS.UNAVAILABLE);
				}
				vsObj.getPool().getMemberList().add(memObj);
			}
		} else {
			if (element.length == 7) {
				OBDtoAdcPoolMemberPASK memObj = new OBDtoAdcPoolMemberPASK();
				memObj.setId(Integer.parseInt(element[0]));
				memObj.setName(element[1]);
				memObj.setIpAddress(element[2]);
				memObj.setPort(Integer.parseInt(element[3]));
				if (element[4].equals("enable"))
					memObj.setState(OBDefine.MEMBER_STATE_PASK.ENABLED);
				else
					memObj.setState(OBDefine.MEMBER_STATE_PASK.DISABLED);

				memObj.setStatus(OBDefine.MEMBER_STATUS.DISABLE);
				if (memObj.getState() == OBDefine.MEMBER_STATE_PASK.ENABLED) {
					if (element[5].equals("ACT"))
						memObj.setStatus(OBDefine.MEMBER_STATUS.AVAILABLE);
					else
						memObj.setStatus(OBDefine.MEMBER_STATUS.UNAVAILABLE);
				}
				vsObj.getPool().getMemberList().add(memObj);
			} else if (element.length == 8) {
				OBDtoAdcPoolMemberPASK memObj = new OBDtoAdcPoolMemberPASK();
				memObj.setId(Integer.parseInt(element[0]));
				memObj.setName(element[1]);
				memObj.setIpAddress(element[2]);
				memObj.setPort(Integer.parseInt(element[3]));
				if (element[5].equals("enable"))
					memObj.setState(OBDefine.MEMBER_STATE_PASK.ENABLED);
				else
					memObj.setState(OBDefine.MEMBER_STATE_PASK.DISABLED);

				memObj.setStatus(OBDefine.MEMBER_STATUS.DISABLE);
				if (memObj.getState() == OBDefine.MEMBER_STATE_PASK.ENABLED) {
					if (element[6].equals("ACT"))
						memObj.setStatus(OBDefine.MEMBER_STATUS.AVAILABLE);
					else
						memObj.setStatus(OBDefine.MEMBER_STATUS.UNAVAILABLE);
				}
				vsObj.getPool().getMemberList().add(memObj);
			}
		}

		return vsObj;
	}

	// Real show info slb
	// ID Name RIP Rport Backup Health Status
	// 1 192.168.199.41 80 ACT enable
	// 2 192.168.199.42 80 ACT enable
	// 3 192.168.199.43 80 ACT enable
	// 4 192.168.199.44 80 ACT enable
	private OBDtoAdcVServerPASK procRealState4SlbInfoV1_8_17(String line, OBDtoAdcVServerPASK vsObj, Integer adcIndex)
			throws OBException {
		String rmWhiteSpace = OBParser.removeFirstWhitespace(line);

		String element[] = rmWhiteSpace.split(" ");// space를 기준으로 분리.//1 192.168.199.41 80 ACT enable
		if (element.length == 4) {
			OBDtoAdcPoolMemberPASK memObj = new OBDtoAdcPoolMemberPASK();
			memObj.setId(Integer.parseInt(element[0]));
			memObj.setIpAddress(element[1]);
			if (line.contains("enable"))
				memObj.setState(OBDefine.MEMBER_STATE_PASK.ENABLED);
			else
				memObj.setState(OBDefine.MEMBER_STATE_PASK.DISABLED);

			memObj.setStatus(OBDefine.MEMBER_STATUS.DISABLE);
			if (memObj.getState() == OBDefine.MEMBER_STATE_PASK.ENABLED) {
				if (line.contains("INACT"))
					memObj.setStatus(OBDefine.MEMBER_STATUS.UNAVAILABLE);
				else
					memObj.setStatus(OBDefine.MEMBER_STATUS.AVAILABLE);
			}
			vsObj.getPool().getMemberList().add(memObj);
		} else if (element.length == 5) {
			OBDtoAdcPoolMemberPASK memObj = new OBDtoAdcPoolMemberPASK();
			memObj.setId(Integer.parseInt(element[0]));
			if (OBNetwork.checkIp(element[1])) {
				memObj.setIpAddress(element[1]);
				if (OBNetwork.checkPort(element[2])) {
					memObj.setPort(Integer.parseInt(element[2]));
				} else {
					memObj.setPort(0);
				}
			} else {
				memObj.setName(element[1]);
				memObj.setIpAddress(element[2]);
				if (OBNetwork.checkPort(element[3])) {
					memObj.setPort(Integer.parseInt(element[3]));
				} else {
					memObj.setPort(0);
				}

			}

			if (line.contains("enable"))
				memObj.setState(OBDefine.MEMBER_STATE_PASK.ENABLED);
			else
				memObj.setState(OBDefine.MEMBER_STATE_PASK.DISABLED);

			memObj.setStatus(OBDefine.MEMBER_STATUS.DISABLE);
			if (memObj.getState() == OBDefine.MEMBER_STATE_PASK.ENABLED) {
				if (line.contains("INACT"))
					memObj.setStatus(OBDefine.MEMBER_STATUS.UNAVAILABLE);
				else
					memObj.setStatus(OBDefine.MEMBER_STATUS.AVAILABLE);
			}
			vsObj.getPool().getMemberList().add(memObj);
		} else if (element.length == 6) {
			OBDtoAdcPoolMemberPASK memObj = new OBDtoAdcPoolMemberPASK();
			memObj.setId(Integer.parseInt(element[0]));
			if (OBNetwork.checkIp(element[1])) {
				memObj.setIpAddress(element[1]);
				if (OBNetwork.checkPort(element[2])) {
					memObj.setPort(Integer.parseInt(element[2]));
				} else {
					memObj.setPort(0);
				}
			} else {
				memObj.setName(element[1]);
				memObj.setIpAddress(element[2]);
				if (OBNetwork.checkPort(element[3])) {
					memObj.setPort(Integer.parseInt(element[3]));
				} else {
					memObj.setPort(0);
				}
			}

			if (line.contains("enable"))
				memObj.setState(OBDefine.MEMBER_STATE_PASK.ENABLED);
			else
				memObj.setState(OBDefine.MEMBER_STATE_PASK.DISABLED);

			memObj.setStatus(OBDefine.MEMBER_STATUS.DISABLE);
			if (memObj.getState() == OBDefine.MEMBER_STATE_PASK.ENABLED) {
				if (line.contains("INACT"))
					memObj.setStatus(OBDefine.MEMBER_STATUS.UNAVAILABLE);
				else
					memObj.setStatus(OBDefine.MEMBER_STATUS.AVAILABLE);
			}
			vsObj.getPool().getMemberList().add(memObj);
		} else {
			OBDtoAdcPoolMemberPASK memObj = new OBDtoAdcPoolMemberPASK();
			memObj.setId(Integer.parseInt(element[0]));
			if (OBNetwork.checkIp(element[1])) {
				memObj.setIpAddress(element[1]);
				if (OBNetwork.checkPort(element[2])) {
					memObj.setPort(Integer.parseInt(element[2]));
				} else {
					memObj.setPort(0);
				}
			} else {
				memObj.setName(element[1]);
				memObj.setIpAddress(element[2]);
				if (OBNetwork.checkPort(element[3])) {
					memObj.setPort(Integer.parseInt(element[3]));
				} else {
					memObj.setPort(0);
				}
			}

			if (line.contains("enable"))
				memObj.setState(OBDefine.MEMBER_STATE_PASK.ENABLED);
			else
				memObj.setState(OBDefine.MEMBER_STATE_PASK.DISABLED);

			memObj.setStatus(OBDefine.MEMBER_STATUS.DISABLE);
			if (memObj.getState() == OBDefine.MEMBER_STATE_PASK.ENABLED) {
				if (line.contains("INACT"))
					memObj.setStatus(OBDefine.MEMBER_STATUS.UNAVAILABLE);
				else
					memObj.setStatus(OBDefine.MEMBER_STATUS.AVAILABLE);
			}
			vsObj.getPool().getMemberList().add(memObj);
		}

		return vsObj;
	}

	// slb 시작 문구를 찾는다.
	private OBDtoAdcVServerPASK procSlbState4SlbInfo(String line, OBDtoAdcVServerPASK vsObj) throws OBException {
		int strIndex = 0;
		// name
		strIndex = line.indexOf(SUFFIX_SLBSTATUS_VSNAME);
		if (strIndex >= 0) {
			String input = line.substring(strIndex + SUFFIX_SLBSTATUS_VSNAME.length());
			// slb 이름을 추출한다. ex: slb yyh_test_pio1818
			String element[] = input.split(" ");// space를 기준으로 분리.
			if (element.length < 1) {
				OBSystemLog.warn(OBDefine.LOGFILE_DEBUG, String.format("parse error(vsName). line:%s", input));
				return vsObj;
			}
			vsObj.setName(element[0]);
			// pool 이름도 같이 설정.
			vsObj.getPool().setName(element[0]);
			return vsObj;
		}

		// state
		strIndex = line.indexOf(SUFFIX_SLBSTATUS_VSSTATE);
		if (strIndex >= 0) {
			String input = line.substring(strIndex + SUFFIX_SLBSTATUS_VSSTATE.length());
			// vip 21.21.21.255
			String element[] = input.split(" ");// space를 기준으로 분리.
			if (element.length != 1) {
				OBSystemLog.warn(OBDefine.LOGFILE_DEBUG, String.format("parse error(vs state). line:%s", input));
				return vsObj;

			}
			if (element[0].equals("enable"))
				vsObj.setState(OBDefine.VS_STATE.ENABLED);
			else
				vsObj.setState(OBDefine.VS_STATE.DISABLED);
			return vsObj;
		}
		return vsObj;
	}

//    VIP             Protocol Vport   
//    192.168.200.229 tcp      80,888
	private OBDtoAdcVServerPASK procVipState4SlbInfo(String line, OBDtoAdcVServerPASK vsObj) throws OBException {
		String rmWhiteSpace = OBParser.removeFirstWhitespace(line);

		String element[] = rmWhiteSpace.split(" ");// space를 기준으로 분리.//192.168.200.229 tcp 80,8080
		if (element.length == 3) {
			vsObj.setvIP(element[0]);
		}
		return vsObj;
	}

	private int checkCurState4SlbInfo(int currState, String input) {
		int strIndex = 0;

		if (currState == PARSE_STATE_INIT) {
			strIndex = input.indexOf(SUFFIX_SLBSTATUS_SERVICE_START);
			if (strIndex >= 0) {
				return PARSE_STATE_SLB;
			}
		}

		if (currState == PARSE_STATE_REAL) {
			if (input.indexOf(SUFFIX_SLBSTATUS_REAL_END) >= 0) {
				return PARSE_STATE_SLB;
			}
			if (input.indexOf(SUFFIX_SLBSTATUS_REAL_END_V17) >= 0) {
				return PARSE_STATE_SLB;
			}
		}

		if (currState == PARSE_STATE_VIP) {
			if (input.indexOf(SUFFIX_SLBSTATUS_VSIP_END) >= 0) {
				return PARSE_STATE_SLB;
			}
		}

		if (currState == PARSE_STATE_SLB) {
			strIndex = input.indexOf(SUFFIX_SLBSTATUS_VSIP_START);
			if (strIndex >= 0) {
				return PARSE_STATE_VIP;
			}

			strIndex = input.indexOf(SUFFIX_SLBSTATUS_REAL_START);
			if (strIndex >= 0) {
				return PARSE_STATE_REAL;
			}

			strIndex = input.indexOf(SUFFIX_SLBSTATUS_SERVICE_END);
			if (strIndex >= 0) {
				return PARSE_STATE_INIT;
			}
		}

		return currState;
	}

	public ArrayList<OBDtoLoggingBufferPASK> parseLoggingBuffer(String input) throws OBException {
		ArrayList<OBDtoLoggingBufferPASK> retVal = new ArrayList<OBDtoLoggingBufferPASK>();
		String lines[] = input.split("\n");
		for (String line : lines) {
			if (line.isEmpty())
				continue;

			if (line.length() < 16)
				continue;

			OBDtoLoggingBufferPASK obj = new OBDtoLoggingBufferPASK();
			obj.setDate(line.substring(0, 15));
			obj.setContent(line.substring(16));
			retVal.add(obj);
		}
		return retVal;
	}

//	PAS-K# show hostname 
//
//	================================================================================
//	  HOSTNAME
//	 ------------------------------------------------------------------------------
//	    Hostname             : PAS-K
//	================================================================================
//
//	PAS-K# 
	private final static String SUFFIX_SHOW_HOSTNAME = "Hostname : ";// [17:42:55 05/06/2013] (notice) login[368]: "root
																		// login on `ttyp0' from `172.172.2.222'"

	public String parseHostname(String input) throws OBException {
		String lines[] = input.split("\n");
		int strIndex = 0;
		for (String line : lines) {
			if (line.isEmpty())
				continue;
			strIndex = line.indexOf(SUFFIX_SHOW_HOSTNAME);
			if (strIndex >= 0) {
				String retVal = line.substring(strIndex + SUFFIX_SHOW_HOSTNAME.length());
				return retVal;
			}
		}
		return "";
	}

//	public static void main(String [] args) 
//	{
//		try
//		{
//			OBAdcPASK handler = new OBAdcPASK("192.168.200.110", "root", "admin");
//			//handler.enter();
//			handler.login();
//			System.out.println("login OK");
//			
//			String cfgDump = handler.cmndSlbDump();//
////			System.out.println(result);
//			System.out.println("command OK");
//			
//			System.out.println(new OBCLIParserPASK().parseApplyTime(cfgDump));
//			
//			handler.disconnect();
//			System.out.println("logout OK");
//
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}

	private final static String SUFFIX_APPLYTIME_START = "! PIOLINK switch configuration";
	private final static String SUFFIX_APPLYTIME_TIME = "! ";

	public Timestamp parseApplyTime(String input) throws OBException {
		String lines[] = input.split("\n");
		int strIndex = 0;
		boolean startFlag = false;
		for (String line : lines) {
			if (line.isEmpty())
				continue;
			if (startFlag == false) {
				strIndex = line.indexOf(SUFFIX_APPLYTIME_START);
				if (strIndex >= 0) {
					startFlag = true;
				}
				continue;
			}
			strIndex = line.indexOf(SUFFIX_APPLYTIME_TIME);
			if (strIndex >= 0) {
				String item = line.substring(strIndex + SUFFIX_APPLYTIME_TIME.length());// ! 2013/05/15 11:27:45
				Timestamp time = OBDateTime.toTimestamp("yyyy/MM/dd HH:mm:ss", item);
				return time;
			}

		}
		return null;
	}

//	public static void main(String [] args) 
//	{
//		try
//		{
//			OBAdcPASK handler = new OBAdcPASK("192.168.200.110", "root", "admin");
//			//handler.enter();
//			handler.login();
//			System.out.println("login OK");
//			
//			String cfgDump = handler.cmndShowTrunkInfo();//
////			System.out.println(result);
//			System.out.println("command OK");
//			
//			System.out.println(new OBCLIParserPASK().parseTrunkInfo(cfgDump));
//			
//			handler.disconnect();
//			System.out.println("logout OK");
//
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}

//	PAS-K# show trunk
//	================================================
//	        Error occurred
//	================================================
//	InputError: There is no trunk
//	================================================
//	PAS-K# show trunk
//
//	================================================================================
//	TRUNK Configuration
//	 ------------------------------------------------------------------------------ 
//	  Trunk Name Algorithm   Port   
//	  1          src-dst-mac ge1,ge2
//	================================================================================
	private final static String SUFFIX_TRUNK_START = "Trunk Name Algorithm";
	private final static String SUFFIX_TRUNK_END = "====================";

	public ArrayList<DtoRptTrunkInfoPASK> parseTrunkInfo(String input) throws OBException {
		ArrayList<DtoRptTrunkInfoPASK> retVal = new ArrayList<DtoRptTrunkInfoPASK>();
		String lines[] = input.split("\n");
		int strIndex = 0;
		boolean startFlag = false;
		for (String line : lines) {
			if (line.isEmpty())
				continue;
			if (startFlag == false) {
				strIndex = line.indexOf(SUFFIX_TRUNK_START);
				if (strIndex >= 0) {
					startFlag = true;
				}
				continue;
			}
			strIndex = line.indexOf(SUFFIX_TRUNK_END);
			if (strIndex >= 0) {
				break;
			}

			String rmWhiteSpace = OBParser.removeFirstWhitespace(line);
			String element[] = rmWhiteSpace.split(" ");// space를 기준으로 분리.
			if (element.length != 3)
				continue;
			DtoRptTrunkInfoPASK obj = new DtoRptTrunkInfoPASK();
			obj.setName(element[0]);
			obj.setAlgorithm(element[1]);
//			String portItems[] = element[2].split(",");// space를 기준으로 분리.
//			ArrayList<String> portList = new ArrayList<String>();
//			for(String port:portItems)
//			{
//				portList.add(port);
//			}
			obj.setPortList(element[2]);

			retVal.add(obj);
		}
		return retVal;
	}

//	public static void main(String [] args) 
//	{
//		try
//		{
//////			OBAdcPASK handler = new OBAdcPASK("192.168.200.110", "root", "admin");
//////
//////			handler.login();
//////			System.out.println("login OK");
//////			
//////			String cfgDump = handler.cmndShowStpInfo();//
//////			System.out.println("command OK");
//////			
//////			System.out.println(new OBCLIParserPASK().parseStpInfo(cfgDump));
//////			
//////			handler.disconnect();
//////			System.out.println("logout OK");
////			OBCLIParserPASK parser1 = OBCommon.getValidPASKHandlerParser("1.7.8.9");
////			System.out.println("parser version = " + parser1.SUFFIX_SYSTEM_VERSION);
////			parser1.aaa();
////			OBCLIParserPASK parser2 = OBCommon.getValidPASKHandlerParser("1.5.8.9");
////			System.out.println("parser version = " + parser2.SUFFIX_SYSTEM_VERSION);
////			parser2.aaa();
//		    
//		    String input = "OS version : PLOS-PASK-v1.7.0.0.1";
////		    String input = "OS version : v1.8.8.0.0";
//		    String version = new OBCLIParserPASK().parseSwVersion(input);
//		    System.out.println(version);
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}

//	================================================================================
//			  STP
//			 ------------------------------------------------------------------------------
//			    Status               : disable
//			    Priority             : 8
//			    Instance Name        :
//			    Path Cost            :
//			    Max Age              : 20
//			    Hello Time           : 2
//			    Forward Delay        : 15
//			    Root ID              :
//			    Root Port            :
//			    Bridge ID            :
//			    Bridge Max Age       :
//			    Bridge Hello Time    :
//			    Bridge Forward Delay :
//
//			    Port                 : 
//			================================================================================
//
//			PAS-K# 
	private final static String SUFFIX_STP_START = "--------------------";
	private final static String SUFFIX_STP_STATE = "Status : ";
	private final static String SUFFIX_STP_PORT = "Port : ";
	private final static String SUFFIX_STP_END = "====================";

	public DtoRptStpInfoPASK parseStpInfo(String input) throws OBException {
		DtoRptStpInfoPASK retVal = new DtoRptStpInfoPASK();
		String lines[] = input.split("\n");
		int strIndex = 0;
		boolean startFlag = false;
		for (String line : lines) {
			if (line.isEmpty())
				continue;
			if (startFlag == false) {
				strIndex = line.indexOf(SUFFIX_STP_START);
				if (strIndex >= 0) {
					startFlag = true;
				}
				continue;
			}
			strIndex = line.indexOf(SUFFIX_STP_END);
			if (strIndex >= 0) {
				break;
			}

			strIndex = line.indexOf(SUFFIX_STP_STATE);
			if (strIndex >= 0) {
				String item = line.substring(strIndex + SUFFIX_STP_STATE.length());
				if (item.equals("disable"))
					retVal.setState(OBDefine.L2_STP_STATE_DISABLED);
				else
					retVal.setState(OBDefine.L2_STP_STATE_ENABLED);
				continue;
			}

			strIndex = line.indexOf(SUFFIX_STP_PORT);
			if (strIndex >= 0) {
				String item = line.substring(strIndex + SUFFIX_STP_PORT.length());
				retVal.setPortList(item);
				continue;
			}
		}
		return retVal;
	}

	private final static String SUFFIX_SYSLOG_ENABLED = "Server Status : ";//

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

	private final static String SUFFIX_SYSLOG_HOST_START = "IP Address Facility Level Agent Facility";//

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

	private final static String SUFFIX_SNMP_ENABLED = " Status : ";//

	public String parseSnmpState(String input) throws OBException {
		int strIndex = 0;
		String lines[] = input.split("\n");
		for (String line : lines) {
			strIndex = line.indexOf(SUFFIX_SNMP_ENABLED);
			if (strIndex >= 0) {
				return line;
			}
		}
		return "";
	}

	private final static String SUFFIX_SNMP_RCOMM_START = " Name Policy Limit-Oid";//
	private final static String SUFFIX_SNMP_RCOMM_KEY = "read-only";//

	public String parseSnmpRCommString(String input) throws OBException {
		int strIndex = 0;
		String lines[] = input.split("\n");
		boolean startFlag = false;
		for (String line : lines) {
			if (startFlag == false) {
				strIndex = line.indexOf(SUFFIX_SNMP_RCOMM_START);
				if (strIndex >= 0) {
					startFlag = true;
				}
				continue;
			}
			String element[] = line.split(" ");
			if (element.length >= 3) {
				if (element[2].equals(SUFFIX_SNMP_RCOMM_KEY))
					return element[1];
			}
		}
		return "";
	}
}
