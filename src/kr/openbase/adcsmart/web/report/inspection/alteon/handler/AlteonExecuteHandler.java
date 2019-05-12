package kr.openbase.adcsmart.web.report.inspection.alteon.handler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import kr.openbase.adcsmart.service.dto.OBDtoAdcInfo;
import kr.openbase.adcsmart.service.impl.OBSShCmndExec;
import kr.openbase.adcsmart.service.impl.OBTelnetCmndExecV2;
import kr.openbase.adcsmart.service.snmp.alteon.OBSnmpAlteon;
import kr.openbase.adcsmart.service.snmp.alteon.dto.OBDtoRptInspectionSnmpAlteon;
import kr.openbase.adcsmart.service.utility.OBCommon;
import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.service.utility.OBExceptionLogin;
import kr.openbase.adcsmart.service.utility.OBExceptionUnreachable;
import kr.openbase.adcsmart.service.utility.OBSystemLog;

/**
 * CLI 환경에서 명령을 실행한다.
 * 
 * @author 최영조
 */
public class AlteonExecuteHandler {
	/* 아래 상수들은 차후 버전 별로 파일로 만드는 것이 좋아보인다. */
	private final static String CMND_STRG_CFG_SNMP = "/cfg/sys/ssnmp/cur"; // ADC 정보를 갖고 오기 위해서 필요하다.

	// 기본항목
	private final static String CMND_STRG_SYS_GENERAL = "/info/sys/general"; // LastBoot 날짜/시간
	private final static String CMND_STRG_SYS_PS = "/info/sys/ps"; // Power 상태
	private final static String CMND_STRG_SYS_FAN = "/info/sys/fan"; // Fan 상태
	private final static String CMND_STRG_SYS_TEMP = "/info/sys/temp"; // 온도 상태
	private final static String CMND_STRG_CFG_SYSLOG = "/cfg/sys/syslog/cur"; // Syslog 사용여부
	private final static String CMND_STRG_STATS_MP_CPU = "/stats/mp/cpu"; // CPU(MP) load 상태
	private final static String CMND_STRG_STATS_SP_CPU = "/stats/sp %d/cpu"; // CPU(SP) load 상태

	// L2
	private final static String CMND_STRG_INFO_LINK = "/info/link"; // Link Up 정보
	private final static String CMND_STRG_INFO_VLAN = "/info/l2/vlan"; // VLAN 정보
	private final static String CMND_STRG_INFO_STG = "/info/l2/stg"; // STP 사용 여부
	private final static String CMND_STRG_INFO_TRUNK = "/info/l2/trunk"; // Trunk 사용 여부
	private final static String CMND_STRG_STAT_PORT_IF = "/stat/port %d/if"; // Port 상태

	// L3
	private final static String CMND_STRG_INFO_L3IP = "/info/l3/ip"; // Interface 상태, Gateway 상태
	private final static String CMND_STRG_INFO_L3VRRP = "/info/l3/vrrp"; // vrrp 이중화 상태

	// L4
	private final static String CMND_STRG_CFG_SLB_ADV_CUR = "/cfg/slb/adv/cur"; // Direct 기능
	private final static String CMND_STRG_STAT_MAINT = "/stat/slb/maint"; // SLB 세션 통계

	// 기타
	private final static String CMND_STRG_CFG_CUR = "/cfg/sys/ntp/cur"; // NTP 정보

	private final static int MAX_CPU_COUNT = 32;

	private SessionManager session;

	public void setConnectionInfo(String server, String user, String password, int connService, int connPort) {
		if (connService == OBDefine.SERVICE.TELNET) {
			session = new Telnet();
		} else {
			session = new Ssh();
		}

		session.setConnectionInfo(server, user, password, connPort);
	}

	public synchronized void login() throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		if (session != null) {
			session.login();
		}
	}

	public synchronized void disconnect() {
		if (session != null) {
			session.disconnect();
		}
	}

	public synchronized String sendCommand(String command) throws OBException {
		if (session != null) {
			return session.sendCommand(command);
		}

		return "";
	}

	public String cmnd(final String command) throws OBException {
		return sendCommand(command);
	}

	// Last Boot 날짜/시간
	public String cmndSysGeneral() throws OBException {
		return sendCommand(CMND_STRG_SYS_GENERAL);
	}

	public String cmndSysPs() throws OBException {
		return sendCommand(CMND_STRG_SYS_PS);
	}

	public String cmndSysFan() throws OBException {
		return sendCommand(CMND_STRG_SYS_FAN);
	}

	public String cmndSysTemp() throws OBException {
		return sendCommand(CMND_STRG_SYS_TEMP);
	}

	public String cmndStatCPUSPUsage() throws OBException {
		// 일부러 불가능한 cpu 번호를 명령어로 날려 최대 cpu 번호를 알아낸다.
		final String output = sendCommand("/stat/sp 999/cpu");

		final String regex = "between \\d+ and (\\d+)";
		final Pattern pattern = Pattern.compile(regex);
		final Matcher match = pattern.matcher(output);

		int maxCount = MAX_CPU_COUNT;

		if (match.find()) {
			try {
				maxCount = Integer.parseInt(match.group(1));
			} catch (Exception e) {
				// Do nothing
			}
		}

		String retVal = "";
		for (int i = 1; i <= maxCount; i++) {
			String cmnd = String.format(CMND_STRG_STATS_SP_CPU, i);
			retVal += sendCommand(cmnd);
		}
		return retVal;
	}

	public String cmndCPUMPUsage() throws OBException {
		return sendCommand(CMND_STRG_STATS_MP_CPU);
	}

	public String cmndInfoLink() throws OBException {
		return sendCommand(CMND_STRG_INFO_LINK);
	}

	public String cmndInfoVlan() throws OBException {
		return sendCommand(CMND_STRG_INFO_VLAN);
	}

	public String cmndInfoStg() throws OBException {
		return sendCommand(CMND_STRG_INFO_STG);
	}

	public String cmndInfoTrunk() throws OBException {
		return sendCommand(CMND_STRG_INFO_TRUNK);
	}

	public String cmndInfoL3IP() throws OBException {
		return sendCommand(CMND_STRG_INFO_L3IP);
	}

	public String cmndInfoL3Vrrp() throws OBException {
		return sendCommand(CMND_STRG_INFO_L3VRRP);
	}

	public String cmndCfgSyslog() throws OBException {
		return sendCommand(CMND_STRG_CFG_SYSLOG);
	}

	public String cmndCfgSlbVstatCur() throws OBException {
		return sendCommand(CMND_STRG_CFG_SLB_ADV_CUR);
	}

	public String cmndCfgSnmp() throws OBException {
		return sendCommand(CMND_STRG_CFG_SNMP);
	}

	public String cmndCfgNtpCur() throws OBException {
		return sendCommand(CMND_STRG_CFG_CUR);
	}

	public String cmndStatPort() throws OBException {
		return sendCommand(CMND_STRG_STAT_PORT_IF);
	}

	public String cmndStatMaint() throws OBException {
		return sendCommand(CMND_STRG_STAT_MAINT);
	}

	public OBDtoRptInspectionSnmpAlteon getRtpInspection(OBDtoAdcInfo adcInfo) throws OBException {
		OBSnmpAlteon snmp = OBCommon.getValidSnmpAlteonHandler(adcInfo.getSwVersion(), adcInfo.getAdcIpAddress(),
				adcInfo.getSnmpInfo());
		return snmp.getRptInspection(adcInfo.getAdcType(), adcInfo.getSwVersion());
	}

//    public static void main(String[] args) throws OBExceptionUnreachable, OBExceptionLogin, OBException
//    {
//        AlteonExecuteHandler handler = new AlteonExecuteHandler();
//
//        handler.setConnectionInfo("192.168.100.14", "admin", "admin", OBDefine.SERVICE.TELNET, 23);
//
//        handler.login();
//
//        long now = System.currentTimeMillis();
//
//        System.out.println(handler.cmndCfgSyslog());
//
//        System.out.println(System.currentTimeMillis() - now);
//
//        handler.disconnect();
//    }
}

interface SessionManager {
	String SUFFIX_LOGIN = "username: ";
	String SUFFIX_PASSWD = "password: ";
	String SUFFIX_PROMPT_OK = "# ";

	void login();

	void disconnect();

	void setConnectionInfo(String server, String user, String password, int connPort);

	String sendCommand(String command);
}

class Telnet implements SessionManager {
	private String userName;
	private String password;
	private String serverName;
	private int connPort = OBDefine.SERVICE.TELNET;
	private OBTelnetCmndExecV2 telnet;

	@Override
	public void login() {
		if (this.telnet == null) {
			this.telnet = new OBTelnetCmndExecV2();
		}
		telnet.setConnectionInfo(this.serverName, this.userName, this.password, this.connPort);
		telnet.setPromptInfo(SessionManager.SUFFIX_LOGIN, SessionManager.SUFFIX_PASSWD,
				SessionManager.SUFFIX_PROMPT_OK);
		try {
			telnet.login();
		} catch (OBExceptionUnreachable | OBExceptionLogin | OBException e) {
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, e.getMessage());
		}
		OBSystemLog.writeAlteonCmnd(String.format("telnet login:%s", this.serverName), "success");
	}

	@Override
	public void disconnect() {
		try {
			this.telnet.sendCommandExit();
			this.telnet.disconnect();
		} catch (Exception e) {
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, e.getMessage());
		}
	}

	@Override
	public void setConnectionInfo(String server, String user, String password, int connPort) {
		this.telnet = new OBTelnetCmndExecV2();
		this.telnet.setConnectionInfo(server, user, password);

		this.serverName = server;
		this.userName = user;
		this.password = password;
		this.connPort = connPort;
	}

	@Override
	public String sendCommand(String command) {
		OBSystemLog.writeAlteonCmnd(command, null);
		String out = "";
		try {
			out = telnet.sendCommand(command, SUFFIX_PROMPT_OK);
		} catch (OBException e) {
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, e.getMessage());
		}
		OBSystemLog.writeAlteonCmnd(null, out);
		return out;
	}

}

class Ssh implements SessionManager {
	private String userName;
	private String password;
	private String serverName;
	private int connPort = OBDefine.SERVICE.TELNET;
	private OBSShCmndExec ssh;

	@Override
	public void login() {
		try {
			if (ssh == null) {
				ssh = new OBSShCmndExec();
				ssh.setConnectionInfo(this.serverName, this.userName, this.password, this.connPort);
			}
			ssh.sshLogin();
			OBSystemLog.writeAlteonCmnd(String.format("ssh login:%s", this.serverName), "success");
		} catch (OBExceptionUnreachable | OBExceptionLogin e) {
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, e.getMessage());
		}
	}

	@Override
	public void disconnect() {
		try {
			this.ssh.sendCommandExit();
			this.ssh.sshDisconnect();
		} catch (Exception e) {
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, e.getMessage());
		}
	}

	@Override
	public void setConnectionInfo(String server, String user, String password, int connPort) {
		this.ssh = new OBSShCmndExec();
		this.ssh.setConnectionInfo(server, user, password, connPort);

		this.serverName = server;
		this.userName = user;
		this.password = password;
		this.connPort = connPort;
	}

	@Override
	public String sendCommand(String command) {
		OBSystemLog.writeAlteonCmnd(command, null);
		String out = "";
		try {
			out = ssh.sendCommand(command, SUFFIX_PROMPT_OK);
		} catch (OBException e) {
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, e.getMessage());
		}
		OBSystemLog.writeAlteonCmnd(null, out);
		return out;
	}

}
