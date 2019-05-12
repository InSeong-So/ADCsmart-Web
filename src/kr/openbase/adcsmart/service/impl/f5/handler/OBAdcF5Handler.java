package kr.openbase.adcsmart.service.impl.f5.handler;

import java.util.ArrayList;

import kr.openbase.adcsmart.service.dto.fault.OBDtoSessionSearchOption;
import kr.openbase.adcsmart.service.impl.OBSShCmndExec;
import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.service.utility.OBExceptionLogin;
import kr.openbase.adcsmart.service.utility.OBExceptionUnreachable;
import kr.openbase.adcsmart.service.utility.OBParser;
import kr.openbase.adcsmart.service.utility.OBSystemLog;

public class OBAdcF5Handler {
	@SuppressWarnings("unused")
	private Integer downloadMaxWait100msec = 300;

	private OBSShCmndExec ssh;

	private final String CLICOMMAND_SESSION_SERCH = "b conn";

	private final static String CMND_CMND_CLIENT_IP = "client";
	private final static String CMND_CMND_SERVER_IP = "server";
	private final static String CMND_CMND_REAL_IP = "ss server";

//     private final int     MAX_WAIT_TIME           = 120;// write memory 와 같은 경우 명령 수행 후 결과값 수신에 수초~수십초 소요된다. 이럴 경우에는 사용되는 timeout 값이다.

	private String userName = "root";
	private String password = "default";
	private String serverName = "";
	private int connPort = 22;

	public OBAdcF5Handler() {
		String propertyValue = "";
		try {
			propertyValue = OBParser.propertyToString(OBDefine.PROPERTY_FILE_NAME,
					OBDefine.PROPERTY_KEY_DWONLOAD_MAX_WAIT_100MSEC);
			if (propertyValue != null && !propertyValue.isEmpty())
				this.downloadMaxWait100msec = Integer.parseInt(propertyValue);
		} catch (Exception e) {

		}
	}

	public void setCommandString(String cmnd) throws OBException {
		if (this.ssh != null)
			this.ssh.setCommandString(cmnd);
	}

	public void setCmndRetString(String retMssage) throws OBException {
		if (this.ssh != null)
			this.ssh.setCmndRetString(retMssage);
	}

	public String getCommandString() throws OBException {
		if (this.ssh != null)
			return this.ssh.getCommandString();
		else
			return "";
	}

	public String getCmndRetString() throws OBException {
		if (this.ssh != null)
			return this.ssh.getCmndRetString();
		return "";
	}

	public void setConnectionInfo(String server, String user, String password, int connPort) {
		this.ssh = new OBSShCmndExec();
		this.ssh.setConnectionInfo(server, user, password, connPort);

		this.serverName = server;
		this.userName = user;
		this.password = password;
		this.connPort = connPort;
	}

	public synchronized void sshLogin() throws OBExceptionUnreachable, OBExceptionLogin {
		if (this.ssh == null) {
			this.ssh = new OBSShCmndExec();
		}
		this.ssh.setConnectionInfo(this.serverName, this.userName, this.password, this.connPort);
		this.ssh.setSSHMode(OBSShCmndExec.SSL_MODE_EXEC);
		this.ssh.sshLogin();
		OBSystemLog.writeF5Cmnd(String.format("login:%s", this.serverName), "success");
	}

	public synchronized void disconnect() {
		if (this.ssh != null) {
			this.sshDisconnect();
			this.ssh = null;
		}
	}

	private synchronized void sshDisconnect() {
		try {
			this.ssh.sshDisconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//     public static void main(String[] args)
//     {
//          try
//          {
//               OBAdcF5Handler handler = new OBAdcF5Handler();
//               handler.setConnectionInfo("192.168.200.14", "root", "default");
//               handler.sshLogin();
////               handler.ssh.executeCommand("ps -A");
////               handler.ssh.executeCommand("ls -al");
//               ArrayList<String> deviceNameList = new ArrayList<String>();
//               deviceNameList.add("ext");
//               deviceNameList.add("int");
//               deviceNameList.add("ha");
//               deviceNameList.add("__pva__");
//              
//               String fileName = "90123456.pcap";
////               String option = "-c 1000 -s 0 tcp and host 172.172.2.209 and host 192.168.200.232";
//               String option = "-c 100 -s 0 tcp and host 172.172.2.209 ";
//               String retVal = handler.cmndTcpdumpStart(fileName, option);
//               System.out.println(retVal);
//               OBDateTime.Sleep(10000);
//               handler.cmndScpDumpfile(fileName, fileName);
////               String retVal1 = handler.sshSendCommand(" ps -A | grep tcpdump");
//////               String retVal1 = handler.sshSendCommand(" ps -A");
////               System.out.println(retVal1);
//              
//               handler.sshDisconnect();
//          }
//          catch(Exception e)
//          {
//               e.printStackTrace();
//          }
//     }
//     public static void  main(String[] args)
//     {
//          try
//          {
//               OBAdcF5Handler handler = new OBAdcF5Handler();
//               handler.setConnectionInfo("192.168.200.11", "root", "default");
//               handler.sshLogin();
//               ArrayList<OBDtoSessionSearchOption> seach = new ArrayList<OBDtoSessionSearchOption>();
//              
//               OBDtoSessionSearchOption sourcIP = new OBDtoSessionSearchOption();
//               sourcIP.setContent("192.168.200.11");
//               sourcIP.setType(OBDtoSessionSearchOption.OPTION_TYPE_SRC_IP);
//               seach.add(sourcIP);
//              
////             OBDtoSessionSearchOption destinationcIP = new OBDtoSessionSearchOption();
////             destinationcIP.setContent("192.168.100.12");
////             destinationcIP.setType(OBDtoSessionSearchOption.OPTION_TYPE_DST_IP);
////             seach.add(destinationcIP);
//              
////             OBDtoSessionSearchOption sourcPort = new OBDtoSessionSearchOption();
////             sourcPort.setContent("33117");
////             sourcPort.setType(OBDtoSessionSearchOption.OPTION_TYPE_SRC_PORT);
////             seach.add(sourcPort);
////              
////             OBDtoSessionSearchOption destiPort = new OBDtoSessionSearchOption();
////             destiPort.setContent("http");
////             destiPort.setType(OBDtoSessionSearchOption.OPTION_TYPE_DST_PORT);
////             seach.add(destiPort);
////              
//               OBDtoSessionSearchOption protocol = new OBDtoSessionSearchOption();
//               protocol.setContent("udp");
//               protocol.setType(OBDtoSessionSearchOption.OPTION_TYPE_PROTOCOL);
//               seach.add(protocol);
//              
//               String retVal = handler.sessionSearchF5CLI(seach);
////             System.out.println("@@@@@@");
////             System.out.println(retVal);
//              
//               ArrayList<OBDtoFaultSessionInfo> list = new OBCLIParserF5().parseSessionInfoListDefault(1,retVal);
//               ArrayList<OBDtoFaultSessionInfo> tolist = new OBCLIParserF5().remanufactoringParsedSessionList(1,list, seach);
//
//               System.out.println("tolist" + tolist);            
//               handler.sshDisconnect();            
//          }
//          catch(Exception e)
//          {
//              e.printStackTrace();
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
		cmnd = String.format("%s", CLICOMMAND_SESSION_SERCH);

		if (srcIP != null && !srcIP.isEmpty()) {
			if (srcPort != null && !srcPort.isEmpty())
				cmnd += String.format(" %s %s:%s", CMND_CMND_CLIENT_IP, srcIP, srcPort);
			else
				cmnd += String.format(" %s %s", CMND_CMND_CLIENT_IP, srcIP);
		}
		if (dstIP != null && !dstIP.isEmpty()) {
			if (dstPort != null && !dstPort.isEmpty())
				cmnd += String.format(" %s %s:%s", CMND_CMND_SERVER_IP, dstIP, dstPort);
			else
				cmnd += String.format(" %s %s", CMND_CMND_SERVER_IP, dstIP);
		}
		if (realIP != null && !realIP.isEmpty()) {
			if (realPort != null && !realPort.isEmpty())
				cmnd += String.format(" %s %s:%s", CMND_CMND_REAL_IP, realIP, realPort);
			else
				cmnd += String.format(" %s %s", CMND_CMND_REAL_IP, realIP);
		}

		return sshSendCommand(cmnd);
	}

	synchronized String sshSendCommand(String command) throws OBException {
		OBSystemLog.writeF5Cmnd(command, null);
		String out = this.ssh.sendCommand(command);
		OBSystemLog.writeF5Cmnd(null, out);
		return out;
	}

//     public static void main(String[] args)
//     {
//          try
//          {
//               OBAdcF5Handler handler = new OBAdcF5Handler();
//               handler.setConnectionInfo("192.168.200.14", "root", "default");
//               handler.sshLogin();
////               handler.ssh.executeCommand("ps -A");
////               handler.ssh.executeCommand("ls -al");
//               boolean retVal = handler.cmndScpDumpfile("/config/kkk.pcap", "/var/lib/adcsmart/pcap/kkkaaa.pcap");
//              
//               System.out.println(retVal);
//               handler.sshDisconnect();
//          }
//          catch(Exception e)
//          {
//               e.printStackTrace();
//          }
//     }

	private final static String CMND_STRG_INFO_VLAN = "bigpipe vlan list";
	private final static String CMND_STRG_INFO_INTERFACE = "bigpipe interface";
	private final static String CMND_STRG_INFO_STG = "bigpipe stp";
	private final static String CMND_STRG_INFO_TRUNK = "bigpipe trunk";
	private final static String CMND_STRG_INFO_FAILOVER = "bigpipe failover show";
	private final static String CMND_STRG_INFO_SELFIP = "bigpipe self";
	private final static String CMND_STRG_INFO_SESSION = "bigpipe conn";
	private final static String CMND_STRG_INFO_ARP = "bigpipe arp";
	private final static String CMND_STRG_INFO_FDB = "bigpipe vlan all fdb show";
	private final static String CMND_STRG_LIST_SYSLOG = "bigpipe syslog list";
	private final static String CMND_STRG_LIST_HTTPS = "bigpipe httpd list";
	private final static String CMND_STRG_LIST_SSHD = "bigpipe sshd list";

	private final static String CMND_STRG_STOP_TCPDUMP = "pkill tcpdump";
	private final static String CMND_STRG_DELETE_TCPDUMP_FILES = "rm -f /root/";
	private final static String CMND_STRG_INFO_DATE = "date";
	private final static String CMND_STRG_INFO_UPTIME = "uptime";
	private final static String CMND_STRG_LIST_ALL = "ls -al";
	private final static String CMND_STRG_PROCESS_NAME = "ps -A | grep \"%s\" | grep -v \"grep\"";
	private final static String CMND_STRG_TCP_DUMP = "tcpdump";
	private final static String CMND_STRG_NTP_INFO = "ntpq -np";
	private final static String CMND_STRG_DAEMON_STATUS = "bigstart status";

	public String cmndListSshd() throws OBException {
		return sshSendCommand(CMND_STRG_LIST_SSHD);
	}

	public String cmndListHttps() throws OBException {
		return sshSendCommand(CMND_STRG_LIST_HTTPS);
	}

	public String cmndListSyslog() throws OBException {
		return sshSendCommand(CMND_STRG_LIST_SYSLOG);
	}

	public String cmndInfoArp() throws OBException {
		return sshSendCommand(CMND_STRG_INFO_ARP);
	}

	public String cmndInfoFdb() throws OBException {
		return sshSendCommand(CMND_STRG_INFO_FDB);
	}

//     public static void  main(String[] args)
//     {
//          try
//          {
//               OBAdcF5Handler handler = new OBAdcF5Handler();
//               handler.setConnectionInfo("192.168.200.14", "root", "default", OBDefine.SERVICE.NOT_DEFINED);
//               handler.sshLogin();
//                    
//               ArrayList<String> fileNameList = new ArrayList<String>();
//               fileNameList.add("3585247555615.pcap");
//               handler.sshDisconnect();            
//          }
//          catch(Exception e)
//          {
//              e.printStackTrace();
//          }
//     }

	public boolean cmndScpDumpfile(String remoteFileName, String localFileName) throws OBException {
		OBSystemLog.info(OBDefine.LOGFILE_SYSTEM,
				String.format("start. remoteFileName:%s, localFileName:%s", remoteFileName, localFileName));

//    	 localFileName = OBDefine.PKT_DUMP_FILE_PATH+localFileName;
		boolean retVal = this.ssh.executeScpFromCommand(remoteFileName, localFileName);
		return retVal;
	}

//     public static void  main(String[] args)
//     {
//          try
//          {
//               OBAdcF5Handler handler = new OBAdcF5Handler();
//               handler.setConnectionInfo("192.168.200.11", "root", "default");
//               handler.sshLogin();
//                    
//               ArrayList<String> fileNameList = new ArrayList<String>();
//               fileNameList.add("9082797440091.pcap");
//               String retVal = handler.cmndCheckFileSize(fileNameList);
//               System.out.println(retVal);
////             System.out.println(retVal);
//              
//               handler.sshDisconnect();            
//          }
//          catch(Exception e)
//          {
//              e.printStackTrace();
//          }
//     }

	public String cmndCheckFileSize(ArrayList<String> fileNameList) throws OBException {
		String fileList = "";
		for (String fileName : fileNameList) {
			if (!fileList.isEmpty())
				fileList += " ";
			fileList += fileName;
		}
		String cmnd = String.format("%s %s", CMND_STRG_LIST_ALL, fileList);
		return sshSendCommand(cmnd);
	}

	public String cmndCheckProcess(String processName) throws OBException {
		String cmnd = String.format(CMND_STRG_PROCESS_NAME, processName);
		return sshSendCommand(cmnd);
	}

	public String cmndTcpdumpStart(String infName, String fileName, String option) throws OBException {
//          cmndStopTcpdump();
		String cmnd = "";
		String retVal = "";

		if (infName == null || infName.isEmpty())
			infName = "0.0";// all interface
		cmnd = String.format("%s -i %s %s -w %s > /dev/null 2>&1 &", CMND_STRG_TCP_DUMP, infName, option, fileName);

		OBSystemLog.info(OBDefine.LOGFILE_SYSTEM, String.format("cmnd:%s", cmnd));

		retVal = sshSendCommand(cmnd);
		return retVal;// sshSendCommand(cmnd);
	}

//     public static void main(String[] args)
//     {
//          try
//          {
//               OBAdcF5Handler handler = new OBAdcF5Handler();
//               handler.setConnectionInfo("192.168.200.14", "root", "default");
//               handler.sshLogin();
////               handler.ssh.executeCommand("ps -A");
////               handler.ssh.executeCommand("ls -al");
//               String retVal = handler.cmndStopTcpdump();
//               System.out.println(retVal);
////               String retVal1 = handler.sshSendCommand(" ps -A | grep tcpdump");
//////               String retVal1 = handler.sshSendCommand(" ps -A");
////               System.out.println(retVal1);
//              
//               handler.sshDisconnect();
//          }
//          catch(Exception e)
//          {
//               e.printStackTrace();
//          }
//     }

	public String cmndInfoSession() throws OBException {
		return sshSendCommand(CMND_STRG_INFO_SESSION);
	}

	public String cmndStopTcpdump() throws OBException {
		return sshSendCommand(CMND_STRG_STOP_TCPDUMP);
	}

	public String cmndRemoveTcpdumpFile(String fileName) throws OBException {
		if (fileName == null || fileName.isEmpty())
			return "";
		String cmnd = String.format("%s%s", CMND_STRG_DELETE_TCPDUMP_FILES, fileName);
		return sshSendCommand(cmnd);
	}

	public String cmndInfoFailover() throws OBException {
		return sshSendCommand(CMND_STRG_INFO_FAILOVER);
	}

	public String cmndInfoDate() throws OBException {
		return sshSendCommand(CMND_STRG_INFO_DATE);
	}

	public String cmndInfoUptime() throws OBException {
		return sshSendCommand(CMND_STRG_INFO_UPTIME);
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

	public String cmndNtpInfo() throws OBException {
		return sshSendCommand(CMND_STRG_NTP_INFO);
	}

	public String cmndDaemonStatus() throws OBException {
		return sshSendCommand(CMND_STRG_DAEMON_STATUS);
	}
}