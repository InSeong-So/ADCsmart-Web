package kr.openbase.adcsmart.service.impl.alteon.handler;

import java.io.*;
import java.net.SocketException;
import java.sql.Timestamp;
import java.util.ArrayList;

import kr.openbase.adcsmart.service.dto.*;
import kr.openbase.adcsmart.service.impl.OBSShCmndExec;
import kr.openbase.adcsmart.service.impl.OBTelnetCmndExecV2;
import kr.openbase.adcsmart.service.impl.alteon.handler.dto.OBDtoAdcTimeAlteon;
import kr.openbase.adcsmart.service.impl.alteon.handler.dto.OBDtoAlteonVrrp;
import kr.openbase.adcsmart.service.utility.*;

public class OBAdcAlteonHandler
{
//	public static void main(String[] args) throws Exception
//	{
//		OBAdcAlteonHandler alteon = new OBAdcAlteonHandler();
//		alteon.setConnectionInfo("192.168.100.11", "admin", "admin", OBDefine.SERVICE.SSH, OBDefine.SERVICE.SSH);
//		try
//		{
////			String command;
//			alteon.login();
////			String cfgDump = alteon.cmndDumpInfoSlb();
////			String cmnd = alteon.getCommandString();
////			String retCmnd = alteon.getCmndRetString();
////			System.out.println(cfgDump);
////			System.out.println("1111111"+cmnd);
////			System.out.println("2222222"+retCmnd);
////			cfgDump = alteon.cmndCfgNtp();
////			cmnd = alteon.getCommandString();
////			retCmnd = alteon.getCmndRetString();
////			System.out.println(cfgDump);
////			System.out.println("1111111"+cmnd);
////			System.out.println("2222222"+retCmnd);
//			
//			
//            ArrayList<OBDtoSessionSearchOption> seach = new ArrayList<OBDtoSessionSearchOption>();
//            
////            OBDtoSessionSearchOption sourcIP = new OBDtoSessionSearchOption();
////            sourcIP.setContent("192.168.200.11");
////            sourcIP.setType(OBDtoSessionSearchOption.OPTION_TYPE_SRC_IP);
////            seach.add(sourcIP);
//            
//            OBDtoSessionSearchOption protocol = new OBDtoSessionSearchOption();
//            protocol.setContent("http");
//            protocol.setType(OBDtoSessionSearchOption.OPTION_TYPE_PROTOCOL);
//            seach.add(protocol);
//			
//			String cliRet = alteon.cmndInfoSessCip("172.172.2.110");
//			
//			 ArrayList<OBDtoFaultSessionInfo> list = OBCommon.getValidAlteonCLIParser("").parseSeesionDumpList(2, cliRet);//new OBCLIParserAlteon().parseSeesionDumpList(2, cliRet);
//			 ArrayList<OBDtoFaultSessionInfo> tolist = OBCommon.getValidAlteonCLIParser("").remanufactoringParsedSessionList(2, list, seach);//new OBCLIParserAlteon().remanufactoringParsedSessionList(2, list, seach);
//			
//			 System.out.println("tolist" + tolist);
//			alteon.disconnect();
//
//		}
//		catch(OBExceptionUnreachable e)
//		{
//			e.printStackTrace();
//		}
//		catch(OBExceptionLogin e)
//		{
//			e.printStackTrace();
//		}
//	}
 
	private Integer downloadMaxWait100msec = 300;
	private OBTelnetCmndExecV2 telnet;
	private OBSShCmndExec ssh;
	
	private final String SUFFIX_LOGIN		="username: ";
	private final String SUFFIX_PASSWD		="password: ";
	private final String SUFFIX_PROMPT_OK 	= "# ";
	
	private final String SUFFIX_PROMPT_ADCSMART_PORT_OK 	= "OpenSSH";// telnet xxx.xxx.xxx.xxx 22의 결과.
	
	private  final String SUFFIX_CANT_DEL	="Can't delete ";
	final String SUFFIX_ERROR		="Error: ";
	private  final String SUFFIX_IN_PCAP_PROGRESS ="Packet capture is in progress";
	private  final String SUFFIX_IN_SEND_PROGRESS ="SCP transfer is currently in progress";
	
	private  final String SUFFIX_TRY_LATER	="Try later: ";
	
	private final int	 MAX_WAIT_TIME 		= 120;// write memory 와 같은 경우 명령 수행 후 결과값 수신에 수초~수십초 소요된다. 이럴 경우에는 사용되는 timeout 값이다.
	
	private String userName					="";
	private String password					="";
	String serverName				="";
	private int    connService               =OBDefine.SERVICE.TELNET;
	private int    connPort                  =OBDefine.SERVICE.TELNET;
	
	OBAdcAlteonHandler(String server, String user, String password, int connService, int connPort)
	{
		this.serverName=server;
		this.userName=user;
		this.password=password;
		this.connService=connService;
		this.connPort=connPort;
	}

	public OBAdcAlteonHandler()
	{
		String propertyValue="";
		try
		{
			propertyValue = OBParser.propertyToString(OBDefine.PROPERTY_FILE_NAME, OBDefine.PROPERTY_KEY_DWONLOAD_MAX_WAIT_100MSEC);
			if(propertyValue!=null && !propertyValue.isEmpty())
				this.downloadMaxWait100msec = Integer.parseInt(propertyValue);
		}
		catch(Exception e)
		{
			e.getMessage();
		}
	}
	
	public String getCommandString() throws OBException
	{
		if(this.telnet!=null)
		{
			return this.telnet.getCommand();
		}
		else if(this.ssh!=null)
		{
			return this.ssh.getCommandString();
		}
		else
		{
			return "";
		}
	}
	
	public String getCmndRetString() throws OBException
	{
		if(this.telnet!=null)
		{
			return this.telnet.getRetMssage();
		}
		else if(this.ssh!=null)
		{
			return this.ssh.getCmndRetString();
		}
		else
		{
			return "";
		}
	}

	public void setConnectionInfo(String server, String user, String password, int connService, int connPort)
	{
		if(connService==OBDefine.SERVICE.TELNET)
		{
			this.telnet = new OBTelnetCmndExecV2();
			this.telnet.setConnectionInfo(server, user, password);
			this.ssh = null;
		}
		else //(connService==OBDefine.SERVICE.SSH)
		{
			this.ssh = new OBSShCmndExec();
			this.ssh.setConnectionInfo(server, user, password, connPort);
			this.telnet = null;
		}
		
		this.serverName=server;
		this.userName=user;
		this.password=password;
		this.connService=connService;
		this.connPort=connPort;
	}
	public synchronized void login() throws OBExceptionUnreachable, OBExceptionLogin, OBException
	{
		if(this.connService==OBDefine.SERVICE.TELNET)
		{
			telnetLogin();
		}
		else //connService==OBDefine.SERVICE.SSH
		{
			sshLogin();
		}
	}
	
	private synchronized void telnetLogin() throws OBExceptionUnreachable, OBExceptionLogin, OBException
	{
		if(this.telnet == null)
		{
			this.telnet = new OBTelnetCmndExecV2();
		}		
		this.telnet.setConnectionInfo(this.serverName, this.userName, this.password, this.connPort);
		this.telnet.setPromptInfo(this.SUFFIX_LOGIN, this.SUFFIX_PASSWD, this.SUFFIX_PROMPT_OK);
		this.telnet.login();
		OBSystemLog.writeAlteonCmnd(String.format("telnet login:%s", this.serverName), "success");
	}
	private synchronized void sshLogin() throws OBExceptionUnreachable, OBExceptionLogin, OBException
	{
		if(this.ssh == null)
		{
			this.ssh = new OBSShCmndExec();
		}
		this.ssh.setConnectionInfo(this.serverName, this.userName, this.password, this.connPort);
		this.ssh.sshLogin();
		OBSystemLog.writeAlteonCmnd(String.format("ssh login:%s", this.serverName), "success");
	}
	
	public synchronized void disconnect()
	{
		if(this.telnet!=null)
		{
			telnetDisconnect();
		}
		else
		{
			sshDisconnect();
		}
	}
	private synchronized void telnetDisconnect()
	{
		try
		{
			this.telnet.sendCommandExit();
			this.telnet.disconnect();
		}
		catch(Exception e)
		{
			e.getMessage(); //ykk__ ?? 
		}
	}
	private synchronized void sshDisconnect()
	{
		try
		{
			this.ssh.sendCommandExit();
			this.ssh.sshDisconnect();
		}
		catch(Exception e)
		{
		}
	}
	//telnet 명령 실행
	synchronized String sendCommand(String command) throws OBException
	{
		try
		{
			OBSystemLog.writeAlteonCmnd(command, null);
			String out;
			if(this.telnet!=null)
			{
				out = this.telnet.sendCommand(command, SUFFIX_PROMPT_OK);
			}
			else
			{
				out = this.ssh.sendCommand(command, SUFFIX_PROMPT_OK);
			}
			OBSystemLog.writeAlteonCmnd(null, out);
			return out;
		}
		catch(OBException e)
		{
			throw e;
		}
		catch(Exception e)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
		}
	}
	private synchronized String sendCommandForLimitedResult(String command, int resultLimit) throws OBException
	{
		try
		{
			OBSystemLog.writeAlteonCmnd(command, null);
			String out;
			if(this.telnet!=null)
			{
				out = this.telnet.sendCommandForLimitedResult(command, SUFFIX_PROMPT_OK, resultLimit);
			}
			else
			{
				out = this.ssh.sendCommandForLimitedResult(command, SUFFIX_PROMPT_OK, resultLimit);
			}
			OBSystemLog.writeAlteonCmnd(null, out);
			return out;
		}
		catch(OBException e)
		{
			throw e;
		}
		catch(Exception e)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
		}
	}
	private synchronized String sendAdcsmartPortCommand(String command, int timeOut) throws OBException
	{
		try
		{
			OBSystemLog.writeAlteonCmnd(command, null);
			String out;
			
			if(this.telnet!=null)
			{
			    // endswith를 사용하지 않고 contains를 사용하기 위해서 새로 메소드를 만들었다.
			    // 기존에는 sendCommandTimeOut 메소드를 사용했다.
			    // OpenSSH 버전을 6.7로 업그레이드하면서 SUFFIX 문자열이 달라지면서 변경할 필요가 생겼다.
			    // 작업 5535를 참고하면 된다.
				out = this.telnet.sendCommand(command, SUFFIX_PROMPT_ADCSMART_PORT_OK, timeOut);
			}
			else
			{
				out = this.ssh.sendCommandTimeout(command, SUFFIX_PROMPT_ADCSMART_PORT_OK, timeOut);
			}
			OBSystemLog.writeAlteonCmnd(null, out);
			return out;
		}
		catch(OBException e)
		{
			throw e;
		}
		catch(Exception e)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
		}
	}
	
	private synchronized String sendCommandTimeout(String command, int timeOut) throws OBException
	{
		try
		{
			OBSystemLog.writeAlteonCmnd(command, null);
			String out;
			if(this.telnet!=null)
			{
				out = this.telnet.sendCommandTimeout(command, SUFFIX_PROMPT_OK, timeOut);
			}
			else
			{
				out = this.ssh.sendCommandTimeout(command, SUFFIX_PROMPT_OK, timeOut);
			}
			OBSystemLog.writeAlteonCmnd(null, out);
			return out;
		}
		catch(OBException e)
		{
			throw e;
		}
		catch(Exception e)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
		}
	}
	
	synchronized String sendCommand(String command, String prompt) throws OBException
	{
		try
		{
			OBSystemLog.writeAlteonCmnd(command, null);
			String out;
			if(this.telnet!=null)
			{
				out = this.telnet.sendCommand(command, prompt);
			}
			else
			{
				out = this.ssh.sendCommand(command, prompt);
			}
			OBSystemLog.writeAlteonCmnd(null, out);
			return out;
		}
		catch(OBException e)
		{
			throw e;
		}
		catch(Exception e)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
		}
	}
	
	/*
>> 5412_28.1.11_#1 - Standalone ADC - Main# /info/l2/fdb/dump 
 MAC address    VLAN Port   Trunk State Age Referenced SPs Learned port Referenced ports
----------------- ---- ------ ----- ----- --- -------------- ------------ ----------------
00:00:5e:00:01:0a    1 1                      FWD       1 2            1            1              
00:00:5e:00:01:26    1               VSR   P   1-7                         empty          
00:00:5e:00:01:34    1               VSR   P   1-7                         empty          
00:00:5e:00:01:35    1               VSR   P   1-7                         empty        
	 */
	public String cmndDumpInfoFdb() throws OBException
	{// return 값을 trim함으로  세밀히 분석 필요.
		return OBParser.trimAndSimplifySpaces(command(OBAdcAlteonHandler.CMND_STRG_INFO_FDB_DUMP));
	}
	
	public String cmndDumpInfoSlb() throws OBException
	{
		return OBParser.trimAndSimplifySpaces(command(OBAdcAlteonHandler.CMND_STRG_SLB_DUMP));
	}
	
	public String cmndDumpcfg() throws OBException
	{
		return OBParser.trimAndSimplifySpaces(command(OBAdcAlteonHandler.CMND_STRG_CFG_DUMP));
	}
	
	public void cmndApply() throws OBException
    {
		String out = commandTimeout(OBAdcAlteonHandler.CMND_STRG_APPLY, MAX_WAIT_TIME);
		if(out.indexOf(SUFFIX_ERROR) >= 0)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("failure in applying configurations.(err:%s)", out));
		}
    }

	public void cmndRevert() throws OBException
	{
		try
		{
			String out = commandTimeout(CMND_STRG_REVERT, MAX_WAIT_TIME);
			if(out.indexOf(SUFFIX_ERROR) >= 0)
			{
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("failure in reverting configurations.(err:%s)", out));
			}
			if(out.indexOf(SUFFIX_TRY_LATER) >= 0)
			{
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("failure in reverting configurations.(err:%s)", out));
			}
		}
		catch(OBException e)
		{
			throw e;
		}
		catch(Exception e)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
		}
	}
	
	public void cmndRevertApply() throws OBException
	{
		//20초 대기.
		String out = commandTimeout(CMND_STRG_REVERT_APPLY, MAX_WAIT_TIME);
		if(out.indexOf(SUFFIX_ERROR) >= 0)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("failure in reverting configurations.(err:%s)", out));
		}
		if(out.indexOf(SUFFIX_TRY_LATER) >= 0)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("failure in reverting configurations.(err:%s)", out));
		}
	}

	public boolean cmndSave() throws OBException
	{//20초 대기.
		try
		{
			String out = commandTimeout(CMND_STRG_SAVE, MAX_WAIT_TIME);
			if(out.indexOf(SUFFIX_ERROR) >= 0)
			{
				OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("failed to save a alteon's config.(err:%s)", out));
				return false;
			}
		}
		catch(OBException e)
		{
			throw e;
		}
		catch(Exception e)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
		}
		return true;
	}

	private final static String		CMND_STRG_PKT_DUMP_START	= "/maint/pktcap/capture";
	private final static String		CMND_STRG_PKT_DUMP_STATUS	= "/maint/pktcap/dumpcap";
	private final static String		CMND_STRG_PKT_DUMP_STOP		= "/maint/pktcap/stop";
	private final static String		CMND_STRG_PKT_DUMP_SEND	    = "/maint/pktcap/putcap";
	
	// [-l/-live] [-p/-i <port range>]* [-v/vlan <vlan number>] [-s <len>] [-c <count>] [-enxAO] <pcap filter string>
	public boolean cmndPktDumpStartOption(String interfaceName, String option) throws OBException
	{// tcp만 수집한다.
		try
		{
			// 수집하기 전에 자동으로 stop한다.
			cmndPktDumpStop();

			String cmnd = String.format("%s %s", CMND_STRG_PKT_DUMP_START, option);
			if(interfaceName!=null && !interfaceName.isEmpty())
				cmnd = String.format("%s -i %s %s", CMND_STRG_PKT_DUMP_START, interfaceName, option);
			String out = command(cmnd);
			OBSystemLog.info(OBDefine.LOGFILE_SYSTEM, String.format("cmnd:%s", cmnd));
			if(out.indexOf(SUFFIX_ERROR) >= 0)
			{
				OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("improper command. command:%s, out:%s", cmnd, out));
				return false;
			}
		}
		catch(OBException e)
		{
			throw e;
		}
		catch(Exception e)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
		}
		return true;
	}

	public boolean cmndPktDumpStartStatus(int start, int count) throws OBException
	{// tcp만 수집한다.
		try
		{
			String cmnd = String.format("%s -s %d -c %d", CMND_STRG_PKT_DUMP_STATUS, start, count);
			OBSystemLog.info(OBDefine.LOGFILE_SYSTEM, String.format("cmnd:%s", cmnd));
			String out = command(cmnd);
			if(out.indexOf(SUFFIX_ERROR) >= 0)
			{
				OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("improper command. command:%s, out:%s", cmnd, out));
				return false;
			}
		}
		catch(OBException e)
		{
			throw e;
		}
		catch(Exception e)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
		}
		return true;
	}
	
	public boolean cmndPktDumpStop() throws OBException
	{
		try
		{
			String out = command(CMND_STRG_PKT_DUMP_STOP);
			OBSystemLog.info(OBDefine.LOGFILE_SYSTEM, String.format("cmnd:%s", CMND_STRG_PKT_DUMP_STOP));
			if(out.indexOf(SUFFIX_ERROR) >= 0)
			{
				OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("improper command. command:%s, out:%s", CMND_STRG_PKT_DUMP_STOP, out));
				return false;
			}
		}
		catch(OBException e)
		{
			throw e;
		}
		catch(Exception e)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
		}
		return true;
	}

//	public static void main(String[] args) throws Exception
//	{
//		OBAdcAlteonHandler alteon = new OBAdcAlteonHandler();
//		alteon.setConnectionInfo("192.168.100.13", "admin", "admin", OBDefine.SERVICE.TELNET, OBDefine.SERVICE.TELNET);
//		try
//		{
////			alteon.login();
////			System.out.println(OBDateTime.now());
////			ArrayList<String> ipAddressList = OBNetwork.getAllIPAddress();
//			ArrayList<String> ipAddressList = new ArrayList<String>();
//			ipAddressList.add("1.1.1.1");
//			ipAddressList.add("2.1.1.1");
//			ipAddressList.add("3.1.1.1");
//			ipAddressList.add("172.172.2.206");
//			for(String ipAddress:ipAddressList)
//			{
//				alteon.login();
//				System.out.println(alteon.isAdcsmartReachable(ipAddress, 22));
//				alteon.disconnect();
//			}
//			System.out.println(OBDateTime.now());
//			alteon.disconnect();
//			System.out.println(OBDateTime.now());
//		}
//		catch(OBExceptionUnreachable e)
//		{
//			System.out.println(OBDateTime.now());
//			e.printStackTrace();
//		}
//		catch(OBExceptionLogin e)
//		{
//			System.out.println(OBDateTime.now());
//			e.printStackTrace();
//		}
//	}
	
	/**
	 * adc 장비에서 adcsmart 장비로 접속이 되는지 검사한다. 
	 * @param adcsmartIPAddress
	 * @param portNum
	 * @return 1: 접속됨. 0: 접속안됨.
	 * @throws OBException
	 */
	public Integer isAdcsmartReachable(String adcsmartIPAddress, Integer portNum, Integer mgmtMode) throws OBException
	{
		try
		{
		    String cmnd = "";
		    if(mgmtMode == OBDefine.MGMT_MODE)
		        cmnd = String.format("telnet %s %d -m", adcsmartIPAddress, portNum);
		    else
		        cmnd = String.format("telnet %s %d", adcsmartIPAddress, portNum);
			String retString = sendAdcsmartPortCommand(cmnd, 30);//50 means 5 seconds

			if(retString.toLowerCase().contains(SUFFIX_PROMPT_ADCSMART_PORT_OK.toLowerCase()))
			    return OBDefine.STATUS_REACHABLE;
			
			return OBDefine.STATUS_UNREACHABLE;
		}
		catch(OBException e)
		{// timeout 걸린 경우.. 응답을 못 받은것으로간주함. 즉 포트가 오픈되어 있지 않다고 판단함.
			return OBDefine.STATUS_UNREACHABLE;
		}
		catch(Exception e)
		{// 오류 상황.
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
		}
	}
		
	private final static String		ACCOUNT_NAME_TCPDUMP01	= "tcpdump01";
	private final static String		ACCOUNT_PASSWD_TCPDUMP01	= "tcpdump12#$";
	public Integer cmndPktDumpSend(String fileName, String adcsmartIPAddress) throws OBException
	{
		try
		{
			String cmnd = String.format("%s %s %s %s %s -scp", CMND_STRG_PKT_DUMP_SEND, adcsmartIPAddress, fileName, ACCOUNT_NAME_TCPDUMP01, ACCOUNT_PASSWD_TCPDUMP01);
	    	
			OBSystemLog.info(OBDefine.LOGFILE_SYSTEM, String.format("cmnd:%s", cmnd));
			
			String out = commandTimeout(cmnd, this.downloadMaxWait100msec);//300 means 30 seconds
			if(out.indexOf(SUFFIX_ERROR) >= 0)
			{
				OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("improper command. command:%s, out:%s", cmnd, out));
				return 1;
			}
			if(out.indexOf(SUFFIX_IN_PCAP_PROGRESS) >= 0)
			{
				OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("improper command. command:%s, out:%s", cmnd, out));
				return 2;
			}
			if(out.indexOf(SUFFIX_IN_SEND_PROGRESS) >= 0)
			{
				OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("improper command. command:%s, out:%s", cmnd, out));
				return 3;
			}
			OBSystemLog.info(OBDefine.LOGFILE_SYSTEM, String.format("end of send dump:%s", cmnd));
		}
		catch(OBException e)
		{
			throw e;
		}
		catch(Exception e)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
		}
		return 0;		
	}

	public String cmndCfgSnmp() throws OBException
	{
		return OBParser.trimAndSimplifySpaces(command(CMND_STRG_CFG_SNMP));
	}
	
	public String cmndSysGeneral() throws OBException
	{
		return OBParser.trimAndSimplifySpaces(command(CMND_STRG_SYS_GENERAL));
	}
	
	public String cmndStatSlbMaint() throws OBException
	{
		return OBParser.trimAndSimplifySpaces(command(CMND_STRG_STATS_SLB_MAINT));
	}
	
	public String cmndStatSlbAux() throws OBException
	{
		return OBParser.trimAndSimplifySpaces(command(CMND_STRG_STATS_SLB_AUX));
	}

	public String cmndStatSPCpuResources() throws OBException
	{
		String retVal="";
		for(int i=1;i<=36;i++)
		{//sp 4개까지 검사.
			String cmnd=String.format(CMND_STRG_STATS_SP_CPU, i);
			retVal+=command(cmnd);
		}
		return OBParser.trimAndSimplifySpaces(retVal);
	}
	
	public String cmndSWKey() throws OBException
	{
		String retVal="";
		retVal=OBParser.trimAndSimplifySpaces(command(CMND_STRG_INFO_SWKEY));
		return retVal;
	}
	
	public String cmndStatMPResources() throws OBException
	{
		String retVal="";
		retVal=command(CMND_STRG_STATS_MP_CPU);
		retVal+=command(CMND_STRG_STATS_MP_MEM);
		return OBParser.trimAndSimplifySpaces(retVal);
	}

	public String cmndStatFans() throws OBException
	{		// fan 상태 검사.
		return OBParser.trimAndSimplifySpaces(command(CMND_STRG_STATS_FAN));
	}

	public String cmndInfoLicense() throws OBException
	{		
		return OBParser.trimAndSimplifySpaces(command(CMND_STRG_INFO_LICENSE));
	}

	public String cmndInfoLink() throws OBException
	{		
		return OBParser.trimAndSimplifySpaces(command(CMND_STRG_INFO_LINK));
	}
	
	public String cmndStatPort(int portNum) throws OBException
	{		
		String cmnd=String.format(CMND_STRG_STAT_PORT, portNum);
		return OBParser.trimAndSimplifySpaces(command(cmnd));
	}

	public String cmndInfoVlan() throws OBException
	{		
		return OBParser.trimAndSimplifySpaces(command(CMND_STRG_INFO_VLAN));
	}
	
    public String cmndInfoCurrntUser() throws OBException
    {       
        return OBParser.trimAndSimplifySpaces(command(CMND_STRG_INFO_USER));
    }
	
	public String cmndInfoStg() throws OBException
	{		
		return OBParser.trimAndSimplifySpaces(command(CMND_STRG_INFO_STG));
	}

	public String cmndInfoTrunk() throws OBException
	{		
		return OBParser.trimAndSimplifySpaces(command(CMND_STRG_INFO_TRUNK));
	}

	public String cmndInfoL3IP() throws OBException
	{		
		return OBParser.trimAndSimplifySpaces(command(CMND_STRG_INFO_L3IP));
	}
	
	public String cmndInfoL3Route() throws OBException
	{		
		return OBParser.trimAndSimplifySpaces(command(CMND_STRG_INFO_ROUTE));
	}
	
	public String cmndInfoL3Vrrp() throws OBException
	{		
		return OBParser.trimAndSimplifySpaces(command(CMND_STRG_INFO_VRRP));
	}
	public String cmndInfoL3Arp() throws OBException
	{		
		return OBParser.trimAndSimplifySpaces(command(CMND_STRG_INFO_ARP));
	}
	
	public String cmndCfgSyslog() throws OBException
	{		
		return OBParser.trimAndSimplifySpaces(command(CMND_STRG_CFG_SYSLOG));
	}

	public String cmndCfgNtp() throws OBException
	{		
		return OBParser.trimAndSimplifySpaces(command(CMND_STRG_CFG_NTP));
	}

	public String cmndInfoLog() throws OBException
	{		
		return OBParser.trimAndSimplifySpaces(command(CMND_STRG_INFO_LOG));
	}
	
	public String cmndStatSlbDump() throws OBException
	{		
		return OBParser.trimAndSimplifySpaces(command(CMND_STRG_STAT_SLB_DUMP));
	}
	
	public String cmndInfoSessDump() throws OBException
	{
		return OBParser.trimAndSimplifySpaces(command(CMND_STRG_INFO_SESS_DUMP));
	}
	
	public String cmndInfoSessCip(String ip, int resultLimit) throws OBException
	{
		String cmnd=String.format(CMND_STRG_INFO_SESS_CIP, ip);
		return OBParser.trimAndSimplifySpaces(commandForLimitedResult(cmnd, resultLimit));
	}
	
	public String cmndInfoSessDip(String ip, int resultLimit) throws OBException
	{
		String cmnd=String.format(CMND_STRG_INFO_SESS_DIP, ip);
		return OBParser.trimAndSimplifySpaces(commandForLimitedResult(cmnd, resultLimit));
	}
	
	// real ip 를 기준으로 검색.
	public String cmndInfoSessRip(String ip, int resultLimit) throws OBException
	{
		String cmnd=String.format(CMND_STRG_INFO_SESS_RIP, ip);
		return OBParser.trimAndSimplifySpaces(commandForLimitedResult(cmnd, resultLimit));
	}

	public String cmndCfgSlbVstatCur() throws OBException
	{
		String cmnd=String.format(CMND_STRG_CFG_SLB_ADV_CUR);
		return OBParser.trimAndSimplifySpaces(command(cmnd));
	}
	
	public String cmndCfgSlbVstat(String checkType) throws OBException
	{
		String cmnd=String.format(CMND_STRG_CFG_SLB_VSTAT, checkType);
		return OBParser.trimAndSimplifySpaces(command(cmnd));
	}
	
	public String cmndCfgSnmpSnmpv3(String checkType) throws OBException
	{
		String cmnnd = String.format(CMND_STRG_CFG_SNMP_SNMPV3, checkType);
		return OBParser.trimAndSimplifySpaces(command(cmnnd));
	}
	
	public String cmndCfgSnmpRcomm(String communityString) throws OBException
	{
		String cmnnd = String.format(CMND_STRG_CFG_SYS_SNMP_RCOMM, communityString);
		return OBParser.trimAndSimplifySpaces(command(cmnnd));
	}
	
	public String cmndCfgSysSyslog (String checkType) throws OBException
	{
		String cmnnd = String.format(CMND_STRG_CFG_SYS_SYSLOG, checkType);
		return OBParser.trimAndSimplifySpaces(command(cmnnd));
	}
	
	public String cmndInfoFan() throws OBException
	{
		return OBParser.trimAndSimplifySpaces(command(CMND_STRG_INFO_FAN));
	}	
	
	private final static String		CMND_STRG_SLB_DUMP	= "/info/slb/dump";
	private final static String		CMND_STRG_CFG_DUMP	= "/cfg/dump";
	private final static String		CMND_STRG_APPLY		= "apply";
	private final static String		CMND_STRG_REVERT	= "revert";
    private final static String     CMND_STRG_REVERT_APPLY    = "revert apply";
	private final static String		CMND_STRG_SAVE		= "save";
	private final static String		CMND_STRG_CFG_SNMP	= "/cfg/sys/ssnmp/cur";
	private final static String		CMND_STRG_SYS_GENERAL	  = "/info/sys/general";
	private final static String		CMND_STRG_STATS_SLB_MAINT = "/stats/slb/maint";
	private final static String		CMND_STRG_STATS_SLB_AUX   = "/stats/slb/aux";
	private final static String		CMND_STRG_INFO_SWKEY	  = "/info/swkey";
	private final static String		CMND_STRG_STATS_MP_CPU	  = "/stats/mp/cpu";
	private final static String		CMND_STRG_STATS_SP_CPU	  = "/stats/sp %d/cpu";
	private final static String		CMND_STRG_STATS_MP_MEM	  = "/stats/mp/mem";
	private final static String		CMND_STRG_STATS_FAN		  = "/info/sys/fan";
	private final static String		CMND_STRG_INFO_LICENSE	  = "/info/swkey";
	private final static String		CMND_STRG_INFO_LINK		  = "/info/link";
	private final static String		CMND_STRG_STAT_PORT		  = "/stats/port %d/dump";
	private final static String		CMND_STRG_INFO_VLAN		  = "/info/l2/vlan";
	private final static String     CMND_STRG_INFO_USER       = "w";
	private final static String		CMND_STRG_INFO_STG		  = "/info/l2/stg";
	private final static String		CMND_STRG_INFO_TRUNK	  = "/info/l2/trunk";
	private final static String		CMND_STRG_INFO_FDB_DUMP	  = "/info/l2/fdb/dump";
	private final static String		CMND_STRG_INFO_L3IP		  = "/info/l3/ip";
	private final static String		CMND_STRG_INFO_VRRP		  = "/info/l3/vrrp";
	private final static String     CMND_STRG_INFO_ARP        = "/info/l3/arp/dump";
	private final static String     CMND_STRG_INFO_ROUTE      = "/info/l3/route/dump";
	private final static String		CMND_STRG_CFG_SYSLOG	  = "/cfg/sys/syslog/cur";
	private final static String		CMND_STRG_CFG_NTP		  = "/cfg/sys/ntp/cur";
	private final static String		CMND_STRG_INFO_LOG		  = "/info/sys/log";
	private final static String		CMND_STRG_STAT_SLB_DUMP	  = "/stats/slb/dump";
	private final static String     CMND_STRG_INFO_SESS_DUMP  = "/info/slb/sess/dump";
	private final static String     CMND_STRG_INFO_SESS_CIP   = "/info/slb/sess/cip %s";
	private final static String     CMND_STRG_INFO_SESS_DIP   = "/info/slb/sess/dip %s";
	private final static String     CMND_STRG_INFO_SESS_RIP   = "/info/slb/sess/real %s";
	private final static String     CMND_STRG_CFG_SLB_VSTAT   = "/cfg/slb/adv/vstat %s";
	private final static String   	CMND_STRG_CFG_SNMP_SNMPV3 = "cfg/sys/ssnmp/snmpv3/v1v2 %s";
	private final static String   	CMND_STRG_CFG_SYS_SYSLOG  = "cfg/sys/syslog/log %s";
	private final static String 	CMND_STRG_CFG_SYS_SNMP_RCOMM = "cfg/sys/ssnmp/rcomm %s";
	private final static String 	CMND_STRG_CFG_SLB_ADV_CUR = "/cfg/slb/adv/cur";
	private final static String		CMND_STRG_INFO_FAN = "/info/sys/fan";
	
	public String getHostname() throws OBException
	{
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start(%s). start.", this.serverName));
		String out = cmndCfgSnmp();
		
		if(!out.isEmpty())
		{
			// 각 항목별 파싱 진행.
			String value;
			value = OBParser.removeString(OBParser.parseKeywordValue(out, "sysName:     ", " ", 0, 1), "\"");
			OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end(%s). result:%s", this.serverName, value));
			return value;
		}
		else
		{
			OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end(%s). result:null", this.serverName));
			return null;
		}
	}
	
	public Timestamp getApplytime() throws OBException
	{
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start(%s).", this.serverName));
		String out = cmndSysGeneral();
		
		if(!out.isEmpty())
		{
			// 각 항목별 파싱 진행.
			String value;
//			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("OBAdcAlteonV2:getApplytime(%s). before parsing data", this.serverName));
			value = OBParser.parseKeywordValue(out, "Last apply: ", " ", 0, 5);
			
			if(value == null)
			{
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("faild to get applytime.parsing error:%s.", out));
			}
//			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("OBAdcAlteonV2:getApplytime(%s). before converting time. value:%s", this.serverName, value));
			Timestamp time = OBDateTime.toTimestamp("HH:mm:ss EEE MMM dd, yyyy", value);
			OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end(%s). time:%s", this.serverName, OBDateTime.toString(time)));
			return time;
		}
		else
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "faild to get applytime.result of telnet command is empty.");
		}
	}

	public void setSocketTimeout(int tmOut)
	{// dummy
		
	}
	
//	public OBDtoAdcSystemInfo getGeneralInfo() throws OBException
//	{
////		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("start(%s).", this.serverName));
//		String out = sendCommand("/info/sys/general");
//		String hostName = cmndSysGeneral();
//		
//		if(!out.isEmpty())
//		{
//			// 각 항목별 파싱 진행.
//			String value;
//			OBDtoAdcSystemInfo alteonGeneral = new OBDtoAdcSystemInfo();
//			value = OBParser.parseKeywordValue(out, "Application Switch ", " ", 0, 1);
//			alteonGeneral.setModel(value);
//			
//			value = OBParser.parseKeywordValue(out, "Last boot: ", " ", 0, 5);// 11:15:56 Wed Mar 14, 2012
//			alteonGeneral.setLastBootTime(OBDateTime.toTimestamp("HH:mm:ss EEE MMM dd, yyyy", value));
//
//			value = OBParser.parseKeywordValue(out, "Last apply: ", " ", 0, 5);
//			alteonGeneral.setLastApplyTime(OBDateTime.toTimestamp("HH:mm:ss EEE MMM dd, yyyy", value));
//			
//			value = OBParser.parseKeywordValue(out, "Last save: ", " ", 0, 6);
//			alteonGeneral.setLastSaveTime(OBDateTime.toTimestamp("HH:mm:ss EEE MMM dd, yyyy", value));
//
//			value = OBParser.parseKeywordValue(out, "Software Version ", " ", 0, 1);
//			alteonGeneral.setSwVersion(value);
//
//			alteonGeneral.setHostName(hostName);
////			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("end(%s). result:%s", this.serverName, alteonGeneral));
//			return alteonGeneral;
//		}
//		else
//		{
//			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "failed to get general system info.");
//		}
//	}
	
	public OBDtoAdcTimeAlteon getSystemTimeInfo() throws OBException
	{
//		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("start(%s).", this.serverName));
		String out = cmndSysGeneral();
		
		if(!out.isEmpty())
		{
			// 각 항목별 파싱 진행.
			String value;
			OBDtoAdcTimeAlteon time = new OBDtoAdcTimeAlteon();
			
			value = OBParser.parseKeywordValue(out, "Last boot: ", " ", 0, 5);// 11:15:56 Wed Mar 14, 2012
			time.setBootTime(OBDateTime.toTimestamp("HH:mm:ss EEE MMM dd, yyyy", value));

			value = OBParser.parseKeywordValue(out, "Last apply: ", " ", 0, 5);
			time.setApplyTime(OBDateTime.toTimestamp("HH:mm:ss EEE MMM dd, yyyy", value));
			
			value = OBParser.parseKeywordValue(out, "Last save: ", " ", 0, 6);
			time.setSaveTime(OBDateTime.toTimestamp("HH:mm:ss EEE MMM dd, yyyy", value));

//			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("end(%s). result:%s", this.serverName, time));
			return time;
		}
		else
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "failed to get general system info.");
		}
	}
	
    private final static String        CMND_VRRP_SET_VRID = "/cfg/l3/vrrp/vr %d/vrid %d";
    private final static String        CMND_VRRP_SET_ADDR = "/cfg/l3/vrrp/vr %d/addr %s";
    private final static String        CMND_VRRP_SET_INTERFACE = "/cfg/l3/vrrp/vr %d/if %d";
    private final static String        CMND_VRRP_SET_PRIORITY = "/cfg/l3/vrrp/vr %d/prio %d";
    private final static String        CMND_VRRP_SET_TRACK_HSRP_ENABLE = "/cfg/l3/vrrp/vr %d/track/hsrp enable";
    private final static String        CMND_VRRP_SET_TRACK_HSRV_ENABLE = "/cfg/l3/vrrp/vr %d/track/hsrv enable";
    private final static String        CMND_VRRP_SET_TRACK_IFS_ENABLE = "/cfg/l3/vrrp/vr %d/track/ifs enable";
    private final static String        CMND_VRRP_SET_TRACK_L4PTS_ENABLE = "/cfg/l3/vrrp/vr %d/track/l4pts enable";
    private final static String        CMND_VRRP_SET_TRACK_PORTS_ENABLE = "/cfg/l3/vrrp/vr %d/track/ports enable";
    private final static String        CMND_VRRP_SET_TRACK_REALS_ENABLE = "/cfg/l3/vrrp/vr %d/track/reals enable";
    private final static String        CMND_VRRP_SET_TRACK_VRS_ENABLE = "/cfg/l3/vrrp/vr %d/track/vrs enable";
    private final static String        CMND_VRRP_SET_SHARE_ENABLE = "/cfg/l3/vrrp/vr %d/share enable";
    private final static String        CMND_VRRP_SET_SHARE_DISABLE = "/cfg/l3/vrrp/vr %d/share disable";
    private final static String        CMND_VRRP_SET_VR_ENABLE = "/cfg/l3/vrrp/vr %d/ena";
	/**
	 * 이중화 구성일 경우에는 가상서버를 추가시에 반드시 Vrrp 설정도 같이 진행해 주어야 한다. 신규 vrrp를 추가한다.
	 * 
	 * @param vIP
	 * @param isActive
	 * @return
	 * @throws SocketException
	 * @throws IOException
	 * @throws Exception
	 */
	public OBDtoAlteonVrrp addVrrp(String vIP, Integer routerID, Integer vrrpID, Integer ifNum, OBDtoVrrpInfo vrrpInfo, String ipaddress) throws OBException
	{
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start(%s). start. vIP:%s, routerID:%s, vrrpID:%s, ifNum:%d, vrrpInfo:%s\n", this.serverName, vIP, routerID, vrrpID, ifNum, vrrpInfo));

		if(vrrpInfo == null)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "failed to get vrrp info.");
		}
		String output;
		String command;
		// VRRP Virtual ID 설정
		command = String.format(CMND_VRRP_SET_VRID, routerID, vrrpID);
		output = sendCommand(command);
		if(output.indexOf(SUFFIX_ERROR) >= 0)
		{// 비 정상 상태.
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("the result of command is error(command:%s, result:%s)", command, output));
		}
		
		// virtual ip 설정.
		command = String.format(CMND_VRRP_SET_ADDR, routerID, vIP);
		output = sendCommand(command);
		if(output.indexOf(SUFFIX_ERROR) >= 0)
		{// 비 정상 상태.
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("the result of command is error(command:%s, result:%s)", command, output));
		}
		
		command = String.format(CMND_VRRP_SET_INTERFACE, routerID, ifNum);
		output = sendCommand(command);
		if(output.indexOf(SUFFIX_ERROR) >= 0)
		{// 비 정상 상태.
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("the result of command is error(command:%s, result:%s)", command, output));
		}		
		
		// priority 설정.
		command = String.format(CMND_VRRP_SET_PRIORITY, routerID, vrrpInfo.getPriority());
		output = sendCommand(command);
		if(output.indexOf(SUFFIX_ERROR)>=0)
		{// 비 정상 상태.
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("the result of command is error(command:%s, result:%s)", command, output));
		}		
		
		// track 설정.
		if(vrrpInfo.getTrackHsrp()==OBDefine.STATE_ENABLE)
		{
			command = String.format(CMND_VRRP_SET_TRACK_HSRP_ENABLE, routerID);
			output = sendCommand(command);
			if(output.indexOf(SUFFIX_ERROR)>=0)
			{// 비 정상 상태.
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("the result of command is error(command:%s, result:%s)", command, output));
			}
		}		
		if(vrrpInfo.getTrackHsrv()==OBDefine.STATE_ENABLE)
		{
			command = String.format(CMND_VRRP_SET_TRACK_HSRV_ENABLE, routerID);
			output = sendCommand(command);
			if(output.indexOf(SUFFIX_ERROR)>=0)
			{// 비 정상 상태.
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("the result of command is error(command:%s, result:%s)", command, output));
			}
		}		
		if(vrrpInfo.getTrackInt()==OBDefine.STATE_ENABLE)
		{
			command = String.format(CMND_VRRP_SET_TRACK_IFS_ENABLE, routerID);
			output = sendCommand(command);
			if(output.indexOf(SUFFIX_ERROR) >= 0)
			{// 비 정상 상태.
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("the result of command is error(command:%s, result:%s)", command, output));
			}
		}		
		if(vrrpInfo.getTrackL4pts()==OBDefine.STATE_ENABLE)
		{
			command = String.format(CMND_VRRP_SET_TRACK_L4PTS_ENABLE, routerID);
			output = sendCommand(command);
			if(output.indexOf(SUFFIX_ERROR) >= 0)
			{// 비 정상 상태.
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("the result of command is error(command:%s, result:%s)", command, output));
			}
		}		
		if(vrrpInfo.getTrackPorts()==OBDefine.STATE_ENABLE)
		{
			command = String.format(CMND_VRRP_SET_TRACK_PORTS_ENABLE, routerID);
			output = sendCommand(command);
			if(output.indexOf(SUFFIX_ERROR) >= 0)
			{// 비 정상 상태.
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("the result of command is error(command:%s, result:%s)", command, output));
			}
		}		
		if(vrrpInfo.getTrackReals()==OBDefine.STATE_ENABLE)
		{
			command = String.format(CMND_VRRP_SET_TRACK_REALS_ENABLE, routerID);
			output = sendCommand(command);
			if(output.indexOf(SUFFIX_ERROR) >= 0)
			{// 비 정상 상태.
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("the result of command is error(command:%s, result:%s)", command, output));
			}
		}		
		if(vrrpInfo.getTrackVrs()==OBDefine.STATE_ENABLE)
		{
			command = String.format(CMND_VRRP_SET_TRACK_VRS_ENABLE, routerID);
			output = sendCommand(command);
			if(output.indexOf(SUFFIX_ERROR) >= 0)
			{// 비 정상 상태.
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("the result of command is error(command:%s, result:%s)", command, output));
			}
		}		

		// share옵션 설정. Alteon에서 확장된 VRRP의 개념으로 Backup인 상태에서도 VRRP VIP에 대한 패킷을 처리하여 Active-Active 모드로 동작하게 됨. default로 enable되어 있음
		// 이전에 설정된 값을 참조해서 그대로 설정한다.
		int share = vrrpInfo.getSharing();
		if(share==OBDefine.STATE_ENABLE)
			command = String.format(CMND_VRRP_SET_SHARE_ENABLE, routerID);
		else
			command = String.format(CMND_VRRP_SET_SHARE_DISABLE, routerID);
		
		output = sendCommand(command);
		if(output.indexOf(SUFFIX_ERROR) >= 0)
		{// 비 정상 상태.
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("the result of command is error(command:%s, result:%s)", command, output));
		}	
		
		// vrrp 활성화.
		command = String.format(CMND_VRRP_SET_VR_ENABLE, routerID);
		output = sendCommand(command);
		if(output.indexOf(SUFFIX_ERROR) >= 0)
		{// 비 정상 상태.
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("the result of command is error(command:%s, result:%s)", command, output));
		}		
		
		OBDtoAlteonVrrp resultInfo = new OBDtoAlteonVrrp();
		resultInfo.setEnableDisable(1);
		resultInfo.setIfNum(ifNum);
		resultInfo.setIpVer(4);
		resultInfo.setPriority(vrrpInfo.getPriority());
		resultInfo.setShare(share);
		resultInfo.setVID(routerID);
		resultInfo.setVIP(vIP);
		resultInfo.setVrID(vrrpID);
		resultInfo.setTrackVrs(vrrpInfo.getTrackVrs());
		resultInfo.setTrackIfs(vrrpInfo.getTrackInt());
		resultInfo.setTrackPorts(vrrpInfo.getTrackPorts());
		resultInfo.setTrackL4pts(vrrpInfo.getTrackL4pts());
		resultInfo.setTrackReals(vrrpInfo.getTrackReals());
		resultInfo.setTrackHsrp(vrrpInfo.getTrackHsrp());
		resultInfo.setTrackHsrp(vrrpInfo.getTrackHsrv());

		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end(%s). result:%s", this.serverName, resultInfo));
		return resultInfo;
	}
	
	/**
	 * 이중화 구성일 경우에는 가상서버를 추가시에 반드시 Vrrp 설정도 같이 진행해 주어야 한다. 신규 vrrp를 추가한다.
	 * 
	 * @param vIP
	 * @param isActive
	 * @return
	 * @throws SocketException
	 * @throws IOException
	 * @throws Exception
	 */
	public void delVrrp(Integer vrID, String ipaddress) throws OBException
	{
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start(%s). vrID:%d", this.serverName, vrID));
		String output;
		String command;
		// VRRP Virtual ID 설정
		
		command = String.format("/cfg/l3/vrrp/vr %d/del", vrID);
		output = sendCommand(command);
		if(output.indexOf(SUFFIX_ERROR) >= 0)
		{// 비 정상 상태.
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("the result of command is error(command:%s, result:%s)", command, output));
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end(%s).", this.serverName));
	}	

	/**
	 * virtual server의 status 를 변경한다.
	 * 
	 * @param vsId
	 * @param status
	 * 			-- 0: disable, 1: enable
	 * @throws SocketException
	 * @throws IOException
	 * @throws Exception
	 */
	public void changeVServerStatus(String vsId, Integer status, String ipaddress) throws OBException
	{
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start(%s). vsId:%s", this.serverName, vsId));		
		String command;
		
		if(status == OBDefine.STATE_DISABLE)
			command = String.format(CMND_STRG_ADD_VSERVER_DISABLE, vsId);
		else
			command = String.format(CMND_STRG_ADD_VSERVER_ENABLE, vsId);
		String output = sendCommand(command);
		if(output.indexOf(SUFFIX_ERROR) >= 0)
		{// 비 정상 상태.
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("parsing error(command:%s, result:%s)", command, output));
		}
		
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end(%s). ", this.serverName));		
	}
	
	public void changeVrrpStatus(Integer vrrpID, Integer status) throws OBException
	{
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start(%s). vrrpID:%d, status:%d", this.serverName, vrrpID, status));		
		String output;
		String command;
		
		if(status == OBDefine.STATE_DISABLE)
			command = String.format("/cfg/l3/vrrp/vr %d/dis", vrrpID);
		else
			command = String.format("/cfg/l3/vrrp/vr %d/ena", vrrpID);
		
		output = sendCommand(command);
		if(output.indexOf(SUFFIX_ERROR) >= 0)
		{// 비 정상 상태.
			cmndRevert();
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("the result of command is error(command:%s, result:%s)", command, output));
		}		
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end(%s).", this.serverName));		
	}
	
	public void addPoolMemberList(String poolID, ArrayList<OBDtoAdcPoolMemberAlteon> list, String ipaddress) throws OBException
	{
		for(int i=0; i<list.size(); i++)
		{
			OBDtoAdcPoolMemberAlteon obj=list.get(i);
			addPoolMember(poolID, obj.getAlteonNodeID(), obj.getState());
		}
	}
	
	private final static String        CMND_STRG_ADD_POOL_MEMBER  = "/cfg/slb/group %s/add %s";
	private void addPoolMember(String poolID, String nodeID, Integer state) throws OBException
	{
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start(%s). poolID:%s, nodeID:%s", this.serverName, poolID, nodeID));		
		String output;
		String command;

		// ID 추가.
		command = String.format(CMND_STRG_ADD_POOL_MEMBER, poolID, nodeID);
		output = sendCommand(command);
		if(output.indexOf(SUFFIX_ERROR) >= 0)
		{// 비 정상 상태.
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("parsing error(command:%s, result:%s)", command, output));
		}
		
		// pool member 속성 부여한다.
		changedPoolMemberState(poolID, nodeID, state);
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end(%s).", this.serverName));		
	}

	private final static String        CMND_STRG_DEL_POOL_MEMBER  = "/cfg/slb/group %s/rem %s";
	public void delPoolMember(String poolID, ArrayList<OBDtoAdcPoolMemberAlteon> list, String ipaddress) throws OBException
	{
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start(%s). %s", this.serverName, list.toString()));		
		String output;
		String command;
		
		for(int i=0; i<list.size(); i++)
		{
			OBDtoAdcPoolMemberAlteon obj = list.get(i);
			command = String.format(CMND_STRG_DEL_POOL_MEMBER, poolID, obj.getAlteonNodeID());
			output = sendCommand(command);
			if(output.indexOf(SUFFIX_ERROR) >= 0)
			{// 비 정상 상태.
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("parsing error(command:%s, result:%s)", command, output));
			}
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end(%s).", this.serverName));		
	}

	public void addServerGroupList(ArrayList<OBDtoAdcPoolAlteon> list) throws OBException
	{
		for(int i=0; i<list.size(); i++)
		{
			OBDtoAdcPoolAlteon obj = list.get(i);
			
			addServerGroup(obj.getAlteonId(), obj.getName(), obj.getLbMethod(), obj.getHealthCheckV2().getId());
		}
	}
	
	private final static String        CMND_STRG_ADD_SERVER_GROUP          = "/cfg/slb/group %s/name %s";
    private final static String        CMND_STRG_SET_METRIC_LEASTCONNS  = "/cfg/slb/group %s/metric leastconns";
    private final static String        CMND_STRG_SET_METRIC_ROUNDROBIN  = "/cfg/slb/group %s/metric roundrobin";
    private final static String        CMND_STRG_SET_METRIC_HASH        = "/cfg/slb/group %s/metric hash";
    private final static String        CMND_STRG_SET_GROUP_HEALTH       = "/cfg/slb/group %s/health %s";
    
	private void addServerGroup(String poolID, String poolName, Integer lbMethod, String healthCheckId) throws OBException
	{
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start(%s). poolID:%s, poolName:%s, lbMethod:%d", this.serverName, poolID, poolName, lbMethod));		
		String output;
		String command;
		
		// name 추가.
		if((poolName!=null) && (!poolName.isEmpty()))
		{
			command = String.format(CMND_STRG_ADD_SERVER_GROUP, poolID, poolName);
			output = sendCommand(command);
			if(output.indexOf(SUFFIX_ERROR) >= 0)
			{// 비 정상 상태.
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("parsing error(command:%s, result:%s)", command, output));
			}
		}
		
		// slb metric 설정.
		if(lbMethod!=null)
		{
			switch(lbMethod)
			{
			case OBDefine.LB_METHOD_LEAST_CONNECTION_MEMBER:// Least connections.
				command = String.format(CMND_STRG_SET_METRIC_LEASTCONNS, poolID);
				output = sendCommand(command);
				if(output.indexOf(SUFFIX_ERROR) >= 0)
				{// 비 정상 상태.
					throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("parsing error(command:%s, result:%s)", command, output));
				}			
				break;
			case OBDefine.LB_METHOD_ROUND_ROBIN:// Round Robin
				command = String.format(CMND_STRG_SET_METRIC_ROUNDROBIN, poolID);
				output = sendCommand(command);
				if(output.indexOf(SUFFIX_ERROR) >= 0)
				{// 비 정상 상태.
					throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("parsing error(command:%s, result:%s)", command, output));
				}			
				break;
			case OBDefine.LB_METHOD_HASH:// Hash
				command = String.format(CMND_STRG_SET_METRIC_HASH, poolID);
				output = sendCommand(command);
				if(output.indexOf(SUFFIX_ERROR) >= 0)
				{// 비 정상 상태.
					throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("parsing error(command:%s, result:%s)", command, output));
				}			
				break;
			default:
				OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("not supported lb method"));
				return;
			}
		}		

		// health check
		if(healthCheckId!=null && healthCheckId.isEmpty()==false)
		{
			command = String.format(CMND_STRG_SET_GROUP_HEALTH, poolID, healthCheckId);
			output = sendCommand(command);
			if(output.indexOf(SUFFIX_ERROR) >= 0)
			{// 비 정상 상태.
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("parsing error(command:%s, result:%s)", command, output));
			}
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end(%s).", this.serverName));		
	}	

	

	public void addVirtualServiceList(String vsID, ArrayList<OBDtoAdcVService> list, String ipAddress) throws OBException
	{
		for(int i=0; i<list.size(); i++)
		{
			OBDtoAdcVService obj = list.get(i);
			addVirtualService(vsID, obj.getServicePort(), obj.getRealPort(), obj.getPool().getAlteonId(), obj.getPool().getName());
		}
	}

	private final static String        CMND_STRG_ADD_VSERVICE_GROUP = "/cfg/slb/virt %s/service %d/group %s";
    private final static String        CMND_STRG_ADD_VSERVICE_RPORT = "/cfg/slb/virt %s/service %d/rport %d";
    private final static String        CMND_STRG_SET_GROUP_NAME     = "/cfg/slb/group %s/name %s";
    
	private void addVirtualService(String vsID, Integer servicePort, Integer realPort, String poolID, String poolName) throws OBException
	{
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start(%s). vsID:%s, servicePort:%d, realPort:%d, poolID:%s", this.serverName, vsID, servicePort, realPort, poolID));		
		String output;
		String command;
		
		command = String.format(CMND_STRG_ADD_VSERVICE_GROUP, vsID, servicePort, poolID);
			
		output = sendCommand(command);
		if(output.indexOf(SUFFIX_ERROR) >= 0)
		{// 비 정상 상태.
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("parsing error(command:%s, result:%s)", command, output));
		}
		
		if(realPort!=null && realPort.intValue()!=0)
		{// real port 설정.
			command = String.format(CMND_STRG_ADD_VSERVICE_RPORT, vsID, servicePort, realPort);
			output = sendCommand(command);
			if(output.indexOf(SUFFIX_ERROR) >= 0)
			{// 비 정상 상태.
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("parsing error(command:%s, result:%s)", command, output));
			}
		}
		
		//pool 이름 설정. 
		if(poolName==null || poolName.isEmpty())
			command = String.format(CMND_STRG_SET_GROUP_NAME, poolID, "none");
		else
			command = String.format(CMND_STRG_SET_GROUP_NAME, poolID, poolName);
		output = sendCommand(command);
		if(output.indexOf(SUFFIX_ERROR) >= 0)
		{// 비 정상 상태.
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("parsing error(command:%s, result:%s)", command, output));
		}
		
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end(%s).", this.serverName));		
	}	

	public void delVirtualService(String vsID, ArrayList<OBDtoAdcVService> list, String ipaddress) throws OBException
	{
		for(int i=0; i<list.size(); i++)
		{
			OBDtoAdcVService obj = list.get(i);
			delVirtualService(vsID, obj.getServicePort());
		}
	}	
	
	private final static String        CMND_STRG_DEL_VSERVICE     = "/cfg/slb/virt %s/service %d/del";
	public void delVirtualService(String vsID, Integer servicePort) throws OBException
	{
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start(%s). vsID:%s, servicePort:%d", this.serverName, vsID, servicePort));		
		String output;
		String command;
		command = String.format(CMND_STRG_DEL_VSERVICE, vsID, servicePort);
		
		output = sendCommand(command);
		
		if(output.indexOf(SUFFIX_ERROR) >= 0)
		{// 비 정상 상태.
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("parsing error(command:%s, result:%s)", command, output));
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end(%s). ", this.serverName));		
	}
	
	public void cmndCRLF() throws OBException
	{
		String command;
		
		// State 설정.
		command = String.format("/");
		sendCommand(command);
	}	

	private final static String        CMND_STRG_GROUP_DISABLE         = "/cfg/slb/group %s/dis %s";
    private final static String        CMND_STRG_GROUP_ENABLE       = "/cfg/slb/group %s/ena %s";
	
	private void changedPoolMemberState(String poolID, String nodeID, int state) throws OBException
	{
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start(%s). poolID:%s, nodeID:%s, state:%d", this.serverName, poolID, nodeID, state));		
		String output;
		String command;
		
		// State 설정.
		if(state == OBDefine.STATE_DISABLE)
		{
			command = String.format(CMND_STRG_GROUP_DISABLE, poolID, nodeID);
		}
		else
		{
			command = String.format(CMND_STRG_GROUP_ENABLE, poolID, nodeID);
		}
		output = sendCommand(command);
		if(output.indexOf(SUFFIX_ERROR) >= 0)
		{// 비 정상 상태.
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("parsing error(command:%s, result:%s)", command, output));
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end(%s). ", this.serverName));	
	}
	
	private void changePoolName(String poolID, String name) throws OBException
	{
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start(%s). poolID:%s, name:%s", this.serverName, poolID, name));		
		
		String output;
		String command;
		
		if(name.isEmpty())
			command = String.format(CMND_STRG_SET_GROUP_NAME, poolID, "none");
		else
			command = String.format(CMND_STRG_SET_GROUP_NAME, poolID, name);
		output = sendCommand(command);
		if(output.indexOf(SUFFIX_ERROR) >= 0)
		{// 비 정상 상태.
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("parsing error(command:%s, result:%s)", command, output));
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end(%s).", this.serverName));		
	}
	    
	private void changePoolLBMethod(String poolID, int lbMethod) throws OBException
	{
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start(%s). poolID:%s, lbMethod:%d", this.serverName, poolID, lbMethod));		
		
		String output;
		String command="";
		
		// slb metric 설정.
		switch(lbMethod)
		{
		case OBDefine.LB_METHOD_LEAST_CONNECTION_MEMBER:// Least connections.
			command = String.format(CMND_STRG_SET_METRIC_LEASTCONNS, poolID);
			output = sendCommand(command);
			if(output.indexOf(SUFFIX_ERROR) >= 0)
			{// 비 정상 상태.
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("parsing error(command:%s, result:%s)", command, output));
			}
			break;
		case OBDefine.LB_METHOD_ROUND_ROBIN:// Round Robin
			command = String.format(CMND_STRG_SET_METRIC_ROUNDROBIN, poolID);
			output = sendCommand(command);
			if(output.indexOf(SUFFIX_ERROR) >= 0)
			{// 비 정상 상태.
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("parsing error(command:%s, result:%s)", command, output));
			}
			break;
		case OBDefine.LB_METHOD_HASH:// Hash
			command = String.format(CMND_STRG_SET_METRIC_HASH, poolID);
			output = sendCommand(command);
			if(output.indexOf(SUFFIX_ERROR) >= 0)
			{// 비 정상 상태.
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("parsing error(command:%s, result:%s)", command, output));
			}
			break;
		default:
			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("not supported lb mehod"));		
			OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end(%s).", this.serverName));		
			return;
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end(%s).", this.serverName));		
	}

	private void changePoolHealthCheckMethod(String poolId, String healthcheckId) throws OBException
	{
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start(%s). poolId:%s, healthcheck:%s", this.serverName, poolId, healthcheckId));		
		
		String output;
		String command="";
		
		// health check
		// health check
		command = String.format(CMND_STRG_SET_GROUP_HEALTH, poolId, healthcheckId);
		output = sendCommand(command);
		if(output.indexOf(SUFFIX_ERROR) >= 0)
		{// 비 정상 상태.
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("parsing error(command:%s, result:%s)", command, output));
		}				
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end(%s).", this.serverName));		
	}

	public void changeServiceList(String vsID, ArrayList<OBDtoAdcVServiceChanged> list, String ipaddress) throws OBException
	{
		for(OBDtoAdcVServiceChanged vsrv:list)
		{
			if(vsrv.isRealPortChanged()==true)
				changeServiceList(vsID, vsrv.getServicePort(), vsrv.getRealPort());
			//pool 속성 변경 반영.
				
			OBDtoAdcPoolAlteonChanged pool=vsrv.getPool();
			if(pool!=null)
			{
				if(vsrv.isPoolChanged()==true)
					changeServicePool(vsID, vsrv.getServicePort(), pool.getAlteonId());
				if(pool.isHealthCheckChanged())
					changePoolHealthCheckMethod(pool.getAlteonId(), pool.getHealthCheckV2());
				if(pool.isLbMethodChanged())
					changePoolLBMethod(pool.getAlteonId(), pool.getLbMethod());
				if(pool.isNameChanged())
					changePoolName(pool.getAlteonId(), pool.getName());
				// pool member의 속성 변경한다.
				ArrayList<OBDtoAdcPoolMemberAlteonChanged> memberList=pool.getMemberList();
				for(OBDtoAdcPoolMemberAlteonChanged member:memberList)
				{
					if(member.isStateChanged())
						changedPoolMemberState(pool.getAlteonId(), member.getAlteonNodeID(), member.getState());
				}
			}
		}
	}
	
	public void changeServiceList(String vsID, Integer servicePort, Integer realPort) throws OBException
	{
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start(%s). vsID:%s, service:%d, real:%d", this.serverName, vsID, servicePort, realPort));		
		String output;
		String command;

		command = String.format(CMND_STRG_ADD_VSERVICE_RPORT, vsID, servicePort, realPort);
		
		output = sendCommand(command);
		if(output.indexOf(SUFFIX_ERROR) >= 0)
		{// 비 정상 상태.
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("parsing error(command:%s, result:%s)", command, output));
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end(%s).", this.serverName));		
	}
	
	public void changeServicePool(String vsID, Integer servicePort, String groupID) throws OBException
	{
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start(%s). vsID:%s, service:%d, groupID:%s", this.serverName, vsID, servicePort, groupID));		
		String output;
		String command;

		command = String.format(CMND_STRG_ADD_VSERVICE_GROUP, vsID, servicePort, groupID);
		output = sendCommand(command);
		if(output.indexOf(SUFFIX_ERROR) >= 0)
		{// 비 정상 상태.
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("parsing error(command:%s, result:%s)", command, output));
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end(%s).", this.serverName));		
	}
	
	private final static String        CMND_STRG_ADD_VSERVER_ID    = "/cfg/slb/virt %s/";
    private final static String        CMND_STRG_ADD_VSERVER_NAME  = "/cfg/slb/virt %s/vname %s";
    private final static String        CMND_STRG_ADD_VSERVER_VIP   = "/cfg/slb/virt %s/vip %s";
    private final static String        CMND_STRG_ADD_VSERVER_ENABLE= "/cfg/slb/virt %s/ena";
    private final static String        CMND_STRG_ADD_VSERVER_DISABLE= "/cfg/slb/virt %s/dis";
    
	public void addVirtualServer(String vsID, String vsName, String vIP, Integer useYN, String ipAddress) throws OBException
	{
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start(%s). vsID:%s. vsName:%s, vIP:%s, useYN:%d", this.serverName, vsID, vsName, vIP, useYN));		
		String output;
		String command;
	
		// ID 추가.
		command = String.format(CMND_STRG_ADD_VSERVER_ID, vsID);
		output = sendCommand(command);
		if(output.indexOf(SUFFIX_ERROR) >= 0)
		{// 비 정상 상태.
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("parsing error(command:%s, result:%s)", command, output));
		}
		
		// Name 추가.
		if((vsName!=null) &&(!vsName.isEmpty()))
		{
			command = String.format(CMND_STRG_ADD_VSERVER_NAME, vsID, vsName);
			output = sendCommand(command);
			if(output.indexOf(SUFFIX_ERROR) >= 0)
			{// 비 정상 상태.
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("parsing error(command:%s, result:%s)", command, output));
			}
		}
		else
		{
			command = String.format(CMND_STRG_ADD_VSERVER_NAME, vsID, "none");
			output = sendCommand(command);
			if(output.indexOf(SUFFIX_ERROR) >= 0)
			{// 비 정상 상태.
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("parsing error(command:%s, result:%s)", command, output));
			}
		}
		// virtual IP 주소 추가.
		if((vIP!=null) &&(!vIP.isEmpty()))
		{
			command = String.format(CMND_STRG_ADD_VSERVER_VIP, vsID, vIP);
			output = sendCommand(command);
			if(output.indexOf(SUFFIX_ERROR) >= 0)
			{// 비 정상 상태.
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("parsing error(command:%s, result:%s)", command, output));
			}
		}

		// State 설정.
		if(useYN==null)
		{
			command = String.format(CMND_STRG_ADD_VSERVER_ENABLE, vsID);
			output = sendCommand(command);
			if(output.indexOf(SUFFIX_ERROR) >= 0)
			{// 비 정상 상태.
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("parsing error(command:%s, result:%s)", command, output));
			}
		}
		else
		{	
			if(useYN == OBDefine.STATE_DISABLE)
			{// disabled
				command = String.format(CMND_STRG_ADD_VSERVER_DISABLE, vsID);
				output = sendCommand(command);
				if(output.indexOf(SUFFIX_ERROR) >= 0)
				{// 비 정상 상태.
					throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("parsing error(command:%s, result:%s)", command, output));
				}
			}
			else if(useYN == OBDefine.STATE_ENABLE)
			{// disabled
				command = String.format(CMND_STRG_ADD_VSERVER_ENABLE, vsID);
				output = sendCommand(command);
				if(output.indexOf(SUFFIX_ERROR) >= 0)
				{// 비 정상 상태.
					throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("parsing error(command:%s, result:%s)", command, output));
				}
			}
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end"));		
	}	

	public void setVirtualServer(String vsID, String vsName, String vIP, Integer useYN, String ipaddress) throws OBException
	{
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start(%s). vsID:%s. vsName:%s, vIP:%s, useYN:%d", this.serverName, vsID, vsName, vIP, useYN));		
		String output;
		String command;
	
		// Name 추가.
		if((vsName!=null) &&(!vsName.isEmpty()))
		{
			command = String.format(CMND_STRG_ADD_VSERVER_NAME, vsID, vsName);
			output = sendCommand(command);
			if(output.indexOf(SUFFIX_ERROR) >= 0)
			{// 비 정상 상태.
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("parsing error(command:%s, result:%s)", command, output));
			}
		}
		else
		{
			command = String.format(CMND_STRG_ADD_VSERVER_NAME, vsID, "none");
			output = sendCommand(command);
			if(output.indexOf(SUFFIX_ERROR) >= 0)
			{// 비 정상 상태.
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("parsing error(command:%s, result:%s)", command, output));
			}
		}
		// virtual IP 주소 추가.
		if((vIP!=null) &&(!vIP.isEmpty()))
		{
			command = String.format(CMND_STRG_ADD_VSERVER_VIP, vsID, vIP);
			output = sendCommand(command);
			if(output.indexOf(SUFFIX_ERROR) >= 0)
			{// 비 정상 상태.
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("parsing error(command:%s, result:%s)", command, output));
			}
		}

		// State 설정.
		if(useYN!=null)
		{
			if(useYN == OBDefine.STATE_DISABLE)
			{// disabled
				command = String.format(CMND_STRG_ADD_VSERVER_DISABLE, vsID);
				output = sendCommand(command);
				if(output.indexOf(SUFFIX_ERROR) >= 0)
				{// 비 정상 상태.
					throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("parsing error(command:%s, result:%s)", command, output));
				}
			}
			else if(useYN == OBDefine.STATE_ENABLE)
			{// disabled
				command = String.format(CMND_STRG_ADD_VSERVER_ENABLE, vsID);
				output = sendCommand(command);
				if(output.indexOf(SUFFIX_ERROR) >= 0)
				{// 비 정상 상태.
					throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("parsing error(command:%s, result:%s)", command, output));
				}
			}
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end"));		
	}	

	private final static String        CMND_STRG_DEL_VSERVER= "/cfg/slb/virt %s/del";
	public void delVirtualServer(OBDtoAdcVServerAlteon obj, String ipaddress) throws OBException
	{
		String output;
		String command;
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start(%s). %s", this.serverName, obj.toString()));		
		// ID 추가.
		command = String.format(CMND_STRG_DEL_VSERVER, obj.getAlteonId());
		output = sendCommand(command);
		if(output.indexOf(SUFFIX_ERROR) >= 0)
		{// 비 정상 상태.
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("parsing error(command:%s, result:%s)", command, output));
		}
		if(output.indexOf(SUFFIX_CANT_DEL) >= 0)
		{// 비 정상 상태.
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("parsing error(command:%s, result:%s)", command, output));
		}		
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end(%s).", this.serverName));		
	}	
	
	public void addRealserverList(ArrayList<OBDtoAdcNodeAlteon> list, String ipaddress) throws OBException
	{
		for(int i=0; i<list.size(); i++)
		{
			addRealserver(list.get(i));
		}
	}	

	private final static String        CMND_STRG_ADD_REAL_ID= "/cfg/slb/real %s/";
    private final static String        CMND_STRG_ADD_REAL_NAME= "/cfg/slb/real %s/";
    private final static String        CMND_STRG_ADD_REAL_RIP= "/cfg/slb/real %s/rip %s";
    private final static String        CMND_STRG_SET_REAL_ENABLE= "/cfg/slb/real %s/ena";
    private final static String        CMND_STRG_SET_REAL_DISABLE= "/cfg/slb/real %s/dis";
	private void addRealserver(OBDtoAdcNodeAlteon real) throws OBException
	{
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start(%s). %s", this.serverName, real.toString()));		
		
		String output;
		String command;

		// ID 추가.
		command = String.format(CMND_STRG_ADD_REAL_ID, real.getAlteonId());

		output = sendCommand(command);
		if(output.indexOf(SUFFIX_ERROR) >= 0)
		{// 비 정상 상태.
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("parsing error(command:%s, result:%s)", command, output));
		}
		
		// Name 추가. IP주소와 동일하게 한다.
		command = String.format(CMND_STRG_ADD_REAL_NAME, real.getAlteonId(), real.getIpAddress());
		output = sendCommand(command);
		if(output.indexOf(SUFFIX_ERROR) >= 0)
		{// 비 정상 상태.
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("parsing error(command:%s, result:%s)", command, output));
		}

		// IP 주소 추가.
		if(!real.getIpAddress().isEmpty())
		{
			command = String.format(CMND_STRG_ADD_REAL_RIP, real.getAlteonId(), real.getIpAddress());
			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("host:%s, command:%s", this.serverName, command));		
			output = sendCommand(command);
			if(output.indexOf(SUFFIX_ERROR) >= 0)
			{// 비 정상 상태.
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("parsing error(command:%s, result:%s)", command, output));
			}
		}
		
		command = String.format(CMND_STRG_SET_REAL_ENABLE, real.getAlteonId());
		
		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("host:%s, command:%s", this.serverName, command));		

		output = sendCommand(command);
		if(output.indexOf(SUFFIX_ERROR) >= 0)
		{// 비 정상 상태.
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("parsing error(command:%s, result:%s)", command, output));
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end(%s).", this.serverName));		
	}
	
    /**
    * Real server의 status 를 변경한다.
    * 
    * @param rsId
    * @param status
    *          -- 0: disable, 1: enable
    * @throws SocketException
    * @throws IOException
    * @throws Exception
    */
    public void changeRealServerStatus(String rsId, Integer status, String ipaddress) throws OBException
    {
        OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start(%s). rsId:%s", this.serverName, rsId));       
        String command;
        
        if(status == OBDefine.STATE_ENABLE)
            command = String.format(CMND_STRG_SET_REAL_ENABLE, rsId);
        else
            command = String.format(CMND_STRG_SET_REAL_DISABLE, rsId);
        String output = sendCommand(command);
        if(output.indexOf(SUFFIX_ERROR) >= 0)
        {// 비 정상 상태.
            throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("parsing error(command:%s, result:%s)", command, output));
        }
      
        OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end(%s). ", this.serverName));
    }
	
	private String command(String cmndString) throws OBException
	{
		String out = sendCommand(cmndString);
		return out;
	}
	private String commandForLimitedResult(String cmndString, int resultLimit) throws OBException
	{
		String out = sendCommandForLimitedResult(cmndString, resultLimit);
		return out;
	}
	private String commandTimeout(String cmndString, int timeOut) throws OBException
	{
		String out = sendCommandTimeout(cmndString, timeOut);
		return out;
	}
}

