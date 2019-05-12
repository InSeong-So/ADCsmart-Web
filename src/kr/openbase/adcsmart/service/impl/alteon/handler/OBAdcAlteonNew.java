package kr.openbase.adcsmart.service.impl.alteon.handler;

import java.sql.Timestamp;
import java.util.ArrayList;

import kr.openbase.adcsmart.service.dto.OBDtoAdcNodeAlteon;
import kr.openbase.adcsmart.service.dto.OBDtoAdcPoolAlteon;
import kr.openbase.adcsmart.service.dto.OBDtoAdcPoolAlteonChanged;
import kr.openbase.adcsmart.service.dto.OBDtoAdcPoolMemberAlteonChanged;
import kr.openbase.adcsmart.service.dto.OBDtoAdcVServerAlteon;
import kr.openbase.adcsmart.service.dto.OBDtoAdcVService;
import kr.openbase.adcsmart.service.dto.OBDtoAdcVServiceChanged;
import kr.openbase.adcsmart.service.impl.OBSShCmndExec;
import kr.openbase.adcsmart.service.impl.OBTelnetCmndExecV2;
import kr.openbase.adcsmart.service.impl.alteon.handler.dto.OBDtoAdcTimeAlteon;
import kr.openbase.adcsmart.service.impl.dto.OBDtoAdcSystemInfo;
import kr.openbase.adcsmart.service.utility.OBDateTime;
import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.service.utility.OBExceptionLogin;
import kr.openbase.adcsmart.service.utility.OBExceptionUnreachable;
import kr.openbase.adcsmart.service.utility.OBParser;
import kr.openbase.adcsmart.service.utility.OBSystemLog;

class OBAdcAlteonNew {
//	public static void main(String[] args) throws Exception
//	{
//		OBAdcAlteonNew alteon = new OBAdcAlteonNew();
//		alteon.setConnectionInfo("192.168.100.11", "admin", "admin", OBDefine.SERVICE.TELNET, OBDefine.SERVICE.TELNET);
//		try
//		{
//			alteon.login();
//			System.out.println("login done");
//
//			String cfgDump = alteon.cmndDumpcfg();
//			System.out.println(cfgDump);
//			
//			cfgDump = alteon.cmndDumpInfoSlb();
//			System.out.println(cfgDump);
//			alteon.disconnect();
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
// 
	private OBTelnetCmndExecV2 telnet;
	private OBSShCmndExec ssh;

	private final String SUFFIX_LOGIN = "username: ";
	private final String SUFFIX_PASSWD = "password: ";
	private final String SUFFIX_PROMPT_OK = "# ";

	public final String SUFFIX_CANT_DEL = "Can't delete ";
	public final String SUFFIX_ERROR = "Error: ";
	public final String SUFFIX_TRY_LATER = "Try later: ";

	public String userName = "";
	public String password = "";
	public String serverName = "";
	public int connService = OBDefine.SERVICE.TELNET;
	public int connPort = OBDefine.SERVICE.TELNET;

	public OBAdcAlteonNew() {
	}

	void setConnectionInfo(String server, String user, String password, int connService, int connPort) {
		if (connService == OBDefine.SERVICE.TELNET) {
			this.telnet = new OBTelnetCmndExecV2();
			this.telnet.setConnectionInfo(server, user, password, connPort);
			this.ssh = null;
		} else // (connService==OBDefine.SERVICE.SSH)
		{
			this.ssh = new OBSShCmndExec();
			this.ssh.setConnectionInfo(server, user, password, connPort);
			this.telnet = null;
		}

		this.serverName = server;
		this.userName = user;
		this.password = password;
		this.connService = connService;
		this.connPort = connPort;
	}

	synchronized void login() throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		if (this.connService == OBDefine.SERVICE.TELNET) {
			telnetLogin();
		} else // connService==OBDefine.SERVICE.SSH
		{
			sshLogin();
		}
	}

	private synchronized void telnetLogin() throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		if (this.telnet == null) {
			this.telnet = new OBTelnetCmndExecV2();
		}
		this.telnet.setConnectionInfo(this.serverName, this.userName, this.password);
		this.telnet.setPromptInfo(this.SUFFIX_LOGIN, this.SUFFIX_PASSWD, this.SUFFIX_PROMPT_OK);
		this.telnet.login();
		OBSystemLog.writeAlteonCmnd(String.format("telnet login:%s", this.serverName), "success");
	}

	private synchronized void sshLogin() throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		if (this.ssh == null) {
			this.ssh = new OBSShCmndExec();
		}
		this.ssh.setConnectionInfo(this.serverName, this.userName, this.password, this.connPort);
		this.ssh.sshLogin();
		OBSystemLog.writeAlteonCmnd(String.format("ssh login:%s", this.serverName), "success");
	}

	synchronized void disconnect() {
		if (this.telnet != null) {
			telnetDisconnect();
		} else {
			sshDisconnect();
		}
	}

	private synchronized void telnetDisconnect() {
		try {
			this.telnet.disconnect();
		} catch (Exception e) {
		}
	}

	private synchronized void sshDisconnect() {
		try {
			this.ssh.sshDisconnect();
		} catch (Exception e) {
		}
	}

	// telnet 명령 실행
	synchronized String sendCommand(String command) throws OBException {
		String out;
		if (this.telnet != null) {
			out = this.telnet.sendCommand(command, SUFFIX_PROMPT_OK);
		} else {
			out = this.ssh.sendCommand(command, SUFFIX_PROMPT_OK);
		}
		OBSystemLog.writeAlteonCmnd(command, out);
		return out;
	}

	synchronized String sendCommand(String command, String prompt) throws OBException {
		String out;
		if (this.telnet != null) {
			out = this.telnet.sendCommand(command, prompt);
		} else {
			out = this.ssh.sendCommand(command, prompt);
		}
		OBSystemLog.writeAlteonCmnd(command, out);
		return out;
	}

//	private String cmndDumpInfoSlb() throws OBException {
//		return OBParser.trimAndSimplifySpaces(command(OBAdcAlteonNew.CMND_STRG_SLB_DUMP));
//	}
//
//	private String cmndDumpcfg() throws OBException {
//		return OBParser.trimAndSimplifySpaces(command(OBAdcAlteonNew.CMND_STRG_CFG_DUMP));
//	}

	private String cmndCfgSnmp() throws OBException {
		return OBParser.trimAndSimplifySpaces(command(CMND_STRG_CFG_SNMP));
	}

	private String cmndSysGeneral() throws OBException {
		return OBParser.trimAndSimplifySpaces(command(CMND_STRG_SYS_GENERAL));
	}

//	private final static String CMND_STRG_SLB_DUMP = "/info/slb/dump";
//	private final static String CMND_STRG_CFG_DUMP = "/cfg/dump";
	private final static String CMND_STRG_CFG_SNMP = "/cfg/sys/ssnmp/cur";
	private final static String CMND_STRG_SYS_GENERAL = "/info/sys/general";

	public String getHostname() throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start(%s). start.", this.serverName));
		String out = cmndCfgSnmp();

		if (!out.isEmpty()) {
			// 각 항목별 파싱 진행.
			String value;
			value = OBParser.removeString(OBParser.parseKeywordValue(out, "sysName:     ", " ", 0, 1), "\"");
			OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end(%s). result:%s", this.serverName, value));
			return value;
		} else {
			OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end(%s). result:null", this.serverName));
			return null;
		}
	}

	public Timestamp getApplytime() throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start(%s).", this.serverName));
		String out = cmndSysGeneral();

		if (!out.isEmpty()) {
			// 각 항목별 파싱 진행.
			String value;
//			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("OBAdcAlteonV2:getApplytime(%s). before parsing data", this.serverName));
			value = OBParser.parseKeywordValue(out, "Last apply: ", " ", 0, 5);

			if (value == null) {
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						String.format("faild to get applytime.parsing error:%s.", out));
			}
//			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("OBAdcAlteonV2:getApplytime(%s). before converting time. value:%s", this.serverName, value));
			Timestamp time = OBDateTime.toTimestamp("HH:mm:ss EEE MMM dd, yyyy", value);
			OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
					String.format("end(%s). time:%s", this.serverName, OBDateTime.toString(time)));
			return time;
		} else {
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
					"faild to get applytime.result of telnet command is empty.");
		}
	}

	public void setSocketTimeout(int tmOut) {// dummy

	}

	public OBDtoAdcSystemInfo getGeneralInfo() throws OBException {
//		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("start(%s).", this.serverName));
		String out = sendCommand("/info/sys/general");
		String hostName = cmndSysGeneral();

		if (!out.isEmpty()) {
			// 각 항목별 파싱 진행.
			String value;
			OBDtoAdcSystemInfo alteonGeneral = new OBDtoAdcSystemInfo();
			value = OBParser.parseKeywordValue(out, "Application Switch ", " ", 0, 1);
			alteonGeneral.setModel(value);

			value = OBParser.parseKeywordValue(out, "Last boot: ", " ", 0, 5);// 11:15:56 Wed Mar 14, 2012
			alteonGeneral.setLastBootTime(OBDateTime.toTimestamp("HH:mm:ss EEE MMM dd, yyyy", value));

			value = OBParser.parseKeywordValue(out, "Last apply: ", " ", 0, 5);
			alteonGeneral.setLastApplyTime(OBDateTime.toTimestamp("HH:mm:ss EEE MMM dd, yyyy", value));

			value = OBParser.parseKeywordValue(out, "Last save: ", " ", 0, 6);
			alteonGeneral.setLastSaveTime(OBDateTime.toTimestamp("HH:mm:ss EEE MMM dd, yyyy", value));

			value = OBParser.parseKeywordValue(out, "Software Version ", " ", 0, 1);
			alteonGeneral.setSwVersion(value);

			alteonGeneral.setHostName(hostName);
//			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("end(%s). result:%s", this.serverName, alteonGeneral));
			return alteonGeneral;
		} else {
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "failed to get general system info.");
		}
	}

	public OBDtoAdcTimeAlteon getSystemTimeInfo() throws OBException {
//		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("start(%s).", this.serverName));
		String out = cmndSysGeneral();

		if (!out.isEmpty()) {
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
		} else {
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "failed to get general system info.");
		}
	}

	public void addServerGroupList(ArrayList<OBDtoAdcPoolAlteon> list) throws OBException {
		for (int i = 0; i < list.size(); i++) {
			OBDtoAdcPoolAlteon obj = list.get(i);

			addServerGroup(obj.getAlteonId(), obj.getName(), obj.getLbMethod(), obj.getHealthCheckV2().getId());
		}
	}

	public void addServerGroup(String poolID, String poolName, Integer lbMethod, String healthCheckId)
			throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start(%s). poolID:%s, poolName:%s, lbMethod:%d",
				this.serverName, poolID, poolName, lbMethod));
		String output;
		String command;

		// name 추가.
		if ((poolName != null) && (!poolName.isEmpty())) {
			command = String.format("/cfg/slb/group %s/name %s", poolID, poolName);
			output = sendCommand(command);
			if (output.indexOf(SUFFIX_ERROR) >= 0) {// 비 정상 상태.
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						String.format("parsing error(command:%s, result:%s)", command, output));
			}
		}

		// slb metric 설정.
		if (lbMethod != null) {
			switch (lbMethod) {
			case OBDefine.LB_METHOD_LEAST_CONNECTION_MEMBER:// Least connections.
				command = String.format("/cfg/slb/group %s/metric leastconns", poolID);
				output = sendCommand(command);
				if (output.indexOf(SUFFIX_ERROR) >= 0) {// 비 정상 상태.
					throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
							String.format("parsing error(command:%s, result:%s)", command, output));
				}
				break;
			case OBDefine.LB_METHOD_ROUND_ROBIN:// Round Robin
				command = String.format("/cfg/slb/group %s/metric roundrobin", poolID);
				output = sendCommand(command);
				if (output.indexOf(SUFFIX_ERROR) >= 0) {// 비 정상 상태.
					throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
							String.format("parsing error(command:%s, result:%s)", command, output));
				}
				break;
			case OBDefine.LB_METHOD_HASH:// Hash
				command = String.format("/cfg/slb/group %s/metric hash", poolID);
				output = sendCommand(command);
				if (output.indexOf(SUFFIX_ERROR) >= 0) {// 비 정상 상태.
					throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
							String.format("parsing error(command:%s, result:%s)", command, output));
				}
				break;
			default:
				OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("not supported lb method"));
				return;
			}
		}

		// health check
		if (healthCheckId != null && healthCheckId.isEmpty() == false) {
			command = String.format("/cfg/slb/group %s/health %s", poolID, healthCheckId);
			output = sendCommand(command);
			if (output.indexOf(SUFFIX_ERROR) >= 0) {// 비 정상 상태.
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						String.format("parsing error(command:%s, result:%s)", command, output));
			}
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end(%s).", this.serverName));
	}

	public void delServerGroup(ArrayList<OBDtoAdcPoolAlteon> list) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start(%s). %s", this.serverName, list.toString()));

		String output;
		String command;
		for (int i = 0; i < list.size(); i++) {
			// ID 추가.
			command = String.format("/cfg/slb/group %s/del", list.get(i).getAlteonId());
			output = sendCommand(command);
			if (output.indexOf(SUFFIX_ERROR) >= 0) {// 비 정상 상태.
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						String.format("parsing error(command:%s, result:%s)", command, output));
			}
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end(%s).", this.serverName));
	}

	public void addVirtualServiceList(String vsID, ArrayList<OBDtoAdcVService> list, String ipaddress)
			throws OBException {
		for (int i = 0; i < list.size(); i++) {
			OBDtoAdcVService obj = list.get(i);
			addVirtualService(vsID, obj.getServicePort(), obj.getRealPort(), obj.getPool().getAlteonId());
		}
	}

	public void addVirtualService(String vsID, Integer servicePort, Integer realPort, String poolID)
			throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
				String.format("start(%s). vsID:%s, servicePort:%d, realPort:%d, poolID:%s", this.serverName, vsID,
						servicePort, realPort, poolID));
		String output;
		String command;

		command = String.format("/cfg/slb/virt %s/service %d/group %s", vsID, servicePort, poolID);

		output = sendCommand(command);
		if (output.indexOf(SUFFIX_ERROR) >= 0) {// 비 정상 상태.
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
					String.format("parsing error(command:%s, result:%s)", command, output));
		}

		if (realPort != null && realPort.intValue() != 0) {// real port 설정.
			command = String.format("/cfg/slb/virt %s/service %d/rport %d", vsID, servicePort, realPort);
			output = sendCommand(command);
			if (output.indexOf(SUFFIX_ERROR) >= 0) {// 비 정상 상태.
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						String.format("parsing error(command:%s, result:%s)", command, output));
			}
		}

		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end(%s).", this.serverName));
	}

	public void delVirtualService(String vsID, ArrayList<OBDtoAdcVService> list, String ipaddress) throws OBException {
		for (int i = 0; i < list.size(); i++) {
			OBDtoAdcVService obj = list.get(i);
			delVirtualService(vsID, obj.getServicePort());
		}
	}

	public void delVirtualService(String vsID, Integer servicePort) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
				String.format("start(%s). vsID:%s, servicePort:%d", this.serverName, vsID, servicePort));
		String output;
		String command;
		command = String.format("/cfg/slb/virt %s/service %d/del", vsID, servicePort);

		output = sendCommand(command);

		if (output.indexOf(SUFFIX_ERROR) >= 0) {// 비 정상 상태.
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
					String.format("parsing error(command:%s, result:%s)", command, output));
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end(%s). ", this.serverName));
	}

	public void changeNodeList(ArrayList<OBDtoAdcNodeAlteon> list) throws OBException {
		for (int i = 0; i < list.size(); i++) {
			changeNodeList(list.get(i));
		}
	}

	public void changeNodeList(OBDtoAdcNodeAlteon obj) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start(%s). %s", this.serverName, obj.toString()));
		String output;
		String command;

		// State 설정.
		if (obj.getState() == 0)
			command = String.format("/cfg/slb/real %s/dis", obj.getAlteonId());
		else
			command = String.format("/cfg/slb/real %s/ena", obj.getAlteonId());

		output = sendCommand(command);
		if (output.indexOf(SUFFIX_ERROR) >= 0) {// 비 정상 상태.
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
					String.format("parsing error(command:%s, result:%s)", command, output));
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end(%s). ", this.serverName));
	}

	public void cmndCRLF() throws OBException {
		String command;

		// State 설정.
		command = String.format("/");
		sendCommand(command);
	}

	public void changePoolList(ArrayList<OBDtoAdcPoolAlteon> list) throws OBException {
		for (int i = 0; i < list.size(); i++) {
			OBDtoAdcPoolAlteon obj = list.get(i);
			changePoolList(obj.getAlteonId(), obj.getLbMethod(), obj.getHealthCheckV2().getId());
		}
	}

	private void changedPoolMemberState(String poolID, String nodeID, int state) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
				String.format("start(%s). poolID:%s, nodeID:%s, state:%d", this.serverName, poolID, nodeID, state));
		String output;
		String command;

		// State 설정.
		if (state == OBDefine.STATE_DISABLE) {
			command = String.format("/cfg/slb/group %d/dis %d", poolID, nodeID);
		} else {
			command = String.format("/cfg/slb/group %d/ena %d", poolID, nodeID);
		}
		output = sendCommand(command);
		if (output.indexOf(SUFFIX_ERROR) >= 0) {// 비 정상 상태.
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
					String.format("parsing error(command:%s, result:%s)", command, output));
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end(%s). ", this.serverName));
	}

	private void changePoolName(String poolID, String name) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
				String.format("start(%s). poolID:%s, name:%s", this.serverName, poolID, name));

		String output;
		String command;

		if (name.isEmpty())
			command = String.format("/cfg/slb/group %d/name %s", poolID, "none");
		else
			command = String.format("/cfg/slb/group %d/name %s", poolID, name);
		output = sendCommand(command);
		if (output.indexOf(SUFFIX_ERROR) >= 0) {// 비 정상 상태.
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
					String.format("parsing error(command:%s, result:%s)", command, output));
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end(%s).", this.serverName));
	}

	private void changePoolLBMethod(String poolID, int lbMethod) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
				String.format("start(%s). poolID:%s, lbMethod:%d", this.serverName, poolID, lbMethod));

		String output;
		String command = "";

		// slb metric 설정.
		switch (lbMethod) {
		case OBDefine.LB_METHOD_LEAST_CONNECTION_MEMBER:// Least connections.
			command = String.format("/cfg/slb/group %d/metric leastconns", poolID);
			output = sendCommand(command);
			if (output.indexOf(SUFFIX_ERROR) >= 0) {// 비 정상 상태.
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						String.format("parsing error(command:%s, result:%s)", command, output));
			}
			break;
		case OBDefine.LB_METHOD_ROUND_ROBIN:// Round Robin
			command = String.format("/cfg/slb/group %d/metric roundrobin", poolID);
			output = sendCommand(command);
			if (output.indexOf(SUFFIX_ERROR) >= 0) {// 비 정상 상태.
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						String.format("parsing error(command:%s, result:%s)", command, output));
			}
			break;
		case OBDefine.LB_METHOD_HASH:// Hash
			command = String.format("/cfg/slb/group %d/metric hash", poolID);
			output = sendCommand(command);
			if (output.indexOf(SUFFIX_ERROR) >= 0) {// 비 정상 상태.
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						String.format("parsing error(command:%s, result:%s)", command, output));
			}
			break;
		default:
			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("not supported lb mehod"));
			OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end(%s).", this.serverName));
			return;
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end(%s).", this.serverName));
	}

	private void changePoolHealthCheckMethod(String poolId, String healthcheckId) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
				String.format("start(%s). poolId:%s, healthcheckId:%s", this.serverName, poolId, healthcheckId));

		String output;
		String command = "";

		// health check
		command = String.format("/cfg/slb/group %s/health %s", poolId, healthcheckId);
		output = sendCommand(command);
		if (output.indexOf(SUFFIX_ERROR) >= 0) {// 비 정상 상태.
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
					String.format("parsing error(command:%s, result:%s)", command, output));
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end(%s).", this.serverName));
	}

	public void changePoolList(String poolID, int lbMethod, String healthCheckId) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
				String.format("start(%s). poolID:%s, lbMethod:%d", this.serverName, poolID, lbMethod));

		String output;
		String command;

		// slb metric 설정.
		switch (lbMethod) {
		case OBDefine.LB_METHOD_LEAST_CONNECTION_MEMBER:// Least connections.
			command = String.format("/cfg/slb/group %s/metric leastconns", poolID);
			output = sendCommand(command);
			if (output.indexOf(SUFFIX_ERROR) >= 0) {// 비 정상 상태.
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						String.format("parsing error(command:%s, result:%s)", command, output));
			}
			break;
		case OBDefine.LB_METHOD_ROUND_ROBIN:// Round Robin
			command = String.format("/cfg/slb/group %s/metric roundrobin", poolID);
			output = sendCommand(command);
			if (output.indexOf(SUFFIX_ERROR) >= 0) {// 비 정상 상태.
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						String.format("parsing error(command:%s, result:%s)", command, output));
			}
			break;
		case OBDefine.LB_METHOD_HASH:// Hash
			command = String.format("/cfg/slb/group %s/metric hash", poolID);
			output = sendCommand(command);
			if (output.indexOf(SUFFIX_ERROR) >= 0) {// 비 정상 상태.
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						String.format("parsing error(command:%s, result:%s)", command, output));
			}
			break;
		default:
			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("not supported lb mehod"));
			break;
		}

		// health check
		if (healthCheckId != null && healthCheckId.isEmpty() == false) {
			command = String.format("/cfg/slb/group %s/health %s", poolID, healthCheckId);
			output = sendCommand(command);
			if (output.indexOf(SUFFIX_ERROR) >= 0) {// 비 정상 상태.
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						String.format("parsing error(command:%s, result:%s)", command, output));
			}
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end(%s).", this.serverName));
	}

	public void changeServiceList(String vsID, ArrayList<OBDtoAdcVServiceChanged> list, String ipaddress)
			throws OBException {
		for (OBDtoAdcVServiceChanged vsrv : list) {
			if (vsrv.isRealPortChanged() == true)
				changeServiceList(vsID, vsrv.getServicePort(), vsrv.getRealPort());
			// pool 속성 변경 반영.

			OBDtoAdcPoolAlteonChanged pool = vsrv.getPool();
			if (pool != null) {
				if (vsrv.isPoolChanged() == true)
					changeServicePool(vsID, vsrv.getServicePort(), pool.getAlteonId());
				if (pool.isHealthCheckChanged())
					changePoolHealthCheckMethod(pool.getAlteonId(), pool.getHealthCheckV2());
				if (pool.isLbMethodChanged())
					changePoolLBMethod(pool.getAlteonId(), pool.getLbMethod());
				if (pool.isNameChanged())
					changePoolName(pool.getAlteonId(), pool.getName());
				// pool member의 속성 변경한다.
				ArrayList<OBDtoAdcPoolMemberAlteonChanged> memberList = pool.getMemberList();
				for (OBDtoAdcPoolMemberAlteonChanged member : memberList) {
					if (member.isStateChanged())
						changedPoolMemberState(pool.getAlteonId(), member.getAlteonNodeID(), member.getState());
				}
			}
		}
	}

	public void changeServiceList(String vsID, Integer servicePort, Integer realPort) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
				String.format("start(%s). vsID:%s, service:%d, real:%d", this.serverName, vsID, servicePort, realPort));
		String output;
		String command;

		command = String.format("/cfg/slb/virt %s/service %d/rport %d", vsID, servicePort, realPort);

		output = sendCommand(command);
		if (output.indexOf(SUFFIX_ERROR) >= 0) {// 비 정상 상태.
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
					String.format("parsing error(command:%s, result:%s)", command, output));
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end(%s).", this.serverName));
	}

	private void changeServicePool(String vsID, Integer servicePort, String groupID) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start(%s). vsID:%s, service:%d, groupID:%s",
				this.serverName, vsID, servicePort, groupID));
		String output;
		String command;

		command = String.format("/cfg/slb/virt %s/service %d/group %d", vsID, servicePort, groupID);
		output = sendCommand(command);
		if (output.indexOf(SUFFIX_ERROR) >= 0) {// 비 정상 상태.
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
					String.format("parsing error(command:%s, result:%s)", command, output));
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end(%s).", this.serverName));
	}

	public void addVirtualServer(String vsID, String vsName, String vIP, Integer useYN) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start(%s). vsID:%s. vsName:%s, vIP:%s, useYN:%d",
				this.serverName, vsID, vsName, vIP, useYN));
		String output;
		String command;

		// ID 추가.
		command = String.format("/cfg/slb/virt %s/", vsID);
		output = sendCommand(command);
		if (output.indexOf(SUFFIX_ERROR) >= 0) {// 비 정상 상태.
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
					String.format("parsing error(command:%s, result:%s)", command, output));
		}

		// Name 추가.
		if ((vsName != null) && (!vsName.isEmpty())) {
			command = String.format("/cfg/slb/virt %s/vname %s", vsID, vsName);
			output = sendCommand(command);
			if (output.indexOf(SUFFIX_ERROR) >= 0) {// 비 정상 상태.
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						String.format("parsing error(command:%s, result:%s)", command, output));
			}
		} else {
			command = String.format("/cfg/slb/virt %s/vname %s", vsID, "none");
			output = sendCommand(command);
			if (output.indexOf(SUFFIX_ERROR) >= 0) {// 비 정상 상태.
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						String.format("parsing error(command:%s, result:%s)", command, output));
			}
		}
		// virtual IP 주소 추가.
		if ((vIP != null) && (!vIP.isEmpty())) {
			command = String.format("/cfg/slb/virt %s/vip %s", vsID, vIP);
			output = sendCommand(command);
			if (output.indexOf(SUFFIX_ERROR) >= 0) {// 비 정상 상태.
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						String.format("parsing error(command:%s, result:%s)", command, output));
			}
		}

		// State 설정.
		if (useYN == null) {
			command = String.format("/cfg/slb/virt %s/ena", vsID);
			output = sendCommand(command);
			if (output.indexOf(SUFFIX_ERROR) >= 0) {// 비 정상 상태.
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						String.format("parsing error(command:%s, result:%s)", command, output));
			}
		} else {
			if (useYN == OBDefine.STATE_DISABLE) {// disabled
				command = String.format("/cfg/slb/virt %d/dis", vsID);
				output = sendCommand(command);
				if (output.indexOf(SUFFIX_ERROR) >= 0) {// 비 정상 상태.
					throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
							String.format("parsing error(command:%s, result:%s)", command, output));
				}
			} else if (useYN == OBDefine.STATE_ENABLE) {// disabled
				command = String.format("/cfg/slb/virt %s/ena", vsID);
				output = sendCommand(command);
				if (output.indexOf(SUFFIX_ERROR) >= 0) {// 비 정상 상태.
					throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
							String.format("parsing error(command:%s, result:%s)", command, output));
				}
			}
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end"));
	}

	public void setVirtualServer(String vsID, String vsName, String vIP, Integer useYN, String ipaddress)
			throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start(%s). vsID:%s. vsName:%s, vIP:%s, useYN:%d",
				this.serverName, vsID, vsName, vIP, useYN));
		String output;
		String command;

		// Name 추가.
		if ((vsName != null) && (!vsName.isEmpty())) {
			command = String.format("/cfg/slb/virt %s/vname %s", vsID, vsName);
			output = sendCommand(command);
			if (output.indexOf(SUFFIX_ERROR) >= 0) {// 비 정상 상태.
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						String.format("parsing error(command:%s, result:%s)", command, output));
			}
		} else {
			command = String.format("/cfg/slb/virt %s/vname %s", vsID, "none");
			output = sendCommand(command);
			if (output.indexOf(SUFFIX_ERROR) >= 0) {// 비 정상 상태.
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						String.format("parsing error(command:%s, result:%s)", command, output));
			}
		}
		// virtual IP 주소 추가.
		if ((vIP != null) && (!vIP.isEmpty())) {
			command = String.format("/cfg/slb/virt %s/vip %s", vsID, vIP);
			output = sendCommand(command);
			if (output.indexOf(SUFFIX_ERROR) >= 0) {// 비 정상 상태.
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						String.format("parsing error(command:%s, result:%s)", command, output));
			}
		}

		// State 설정.
		if (useYN != null) {
			if (useYN == OBDefine.STATE_DISABLE) {// disabled
				command = String.format("/cfg/slb/virt %s/dis", vsID);
				output = sendCommand(command);
				if (output.indexOf(SUFFIX_ERROR) >= 0) {// 비 정상 상태.
					throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
							String.format("parsing error(command:%s, result:%s)", command, output));
				}
			} else if (useYN == OBDefine.STATE_ENABLE) {// disabled
				command = String.format("/cfg/slb/virt %s/ena", vsID);
				output = sendCommand(command);
				if (output.indexOf(SUFFIX_ERROR) >= 0) {// 비 정상 상태.
					throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
							String.format("parsing error(command:%s, result:%s)", command, output));
				}
			}
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end"));
	}

	public void delVirtualServer(ArrayList<OBDtoAdcVServerAlteon> list) throws OBException {
		for (int i = 0; i < list.size(); i++) {
			delVirtualServer(list.get(i));
		}
	}

	public void delVirtualServer(OBDtoAdcVServerAlteon obj) throws OBException {
		String output;
		String command;

		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start(%s). %s", this.serverName, obj.toString()));
		// ID 추가.
		command = String.format("/cfg/slb/virt %s/del", obj.getAlteonId());
		output = sendCommand(command);
		if (output.indexOf(SUFFIX_ERROR) >= 0) {// 비 정상 상태.
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
					String.format("parsing error(command:%s, result:%s)", command, output));
		}
		if (output.indexOf(SUFFIX_CANT_DEL) >= 0) {// 비 정상 상태.
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
					String.format("parsing error(command:%s, result:%s)", command, output));
//			throw new OBException(OBException.ERRCODE_SLB_ALTEON_CMND, String.format("parsing error(command:%s, result:%s)", command, output));
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end(%s).", this.serverName));
	}

	public void addRealserverList(ArrayList<OBDtoAdcNodeAlteon> list) throws OBException {
		for (int i = 0; i < list.size(); i++) {
			addRealserver(list.get(i));
		}
	}

	public void addRealserver(OBDtoAdcNodeAlteon real) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start(%s). %s", this.serverName, real.toString()));

		String output;
		String command;

		// ID 추가.
		command = String.format("/cfg/slb/real %s/", real.getAlteonId());

		output = sendCommand(command);
		if (output.indexOf(SUFFIX_ERROR) >= 0) {// 비 정상 상태.
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
					String.format("parsing error(command:%s, result:%s)", command, output));
		}

		// Name 추가. IP주소와 동일하게 한다.
		command = String.format("/cfg/slb/real %s/name %s", real.getAlteonId(), real.getIpAddress());
		output = sendCommand(command);
		if (output.indexOf(SUFFIX_ERROR) >= 0) {// 비 정상 상태.
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
					String.format("parsing error(command:%s, result:%s)", command, output));
		}

		// IP 주소 추가.
		if (!real.getIpAddress().isEmpty()) {
			command = String.format("/cfg/slb/real %s/rip %s", real.getAlteonId(), real.getIpAddress());
			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("host:%s, command:%s", this.serverName, command));
			output = sendCommand(command);
			if (output.indexOf(SUFFIX_ERROR) >= 0) {// 비 정상 상태.
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						String.format("parsing error(command:%s, result:%s)", command, output));
			}
		}

		command = String.format("/cfg/slb/real %s/ena", real.getAlteonId());

		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("host:%s, command:%s", this.serverName, command));

		output = sendCommand(command);
		if (output.indexOf(SUFFIX_ERROR) >= 0) {// 비 정상 상태.
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
					String.format("parsing error(command:%s, result:%s)", command, output));
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end(%s).", this.serverName));
	}

	public void delRealservers(ArrayList<OBDtoAdcNodeAlteon> list) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start(%s). %s", this.serverName, list.toString()));
		String output;
		String command;

		for (int i = 0; i < list.size(); i++) {
			// ID 추가.
			command = String.format("/cfg/slb/real %s/del", list.get(i).getAlteonId());
			output = sendCommand(command);
			if (output.indexOf(SUFFIX_ERROR) >= 0) {// 비 정상 상태.
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						String.format("parsing error(command:%s, result:%s)", command, output));
			}
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end(%s).", this.serverName));
	}

	public int getWellknownPort(String port) {
		if (port.compareTo("http") == 0) {
			return 80;
		} else if (port.compareTo("dns") == 0) {
			return 53;
		} else if (port.compareTo("https") == 0) {
			return 443;
		} else if (port.compareTo("ntp") == 0) {
			return 123;
		} else if (port.compareTo("nntp") == 0) {
			return 119;
		} else if (port.compareTo("sftp") == 0) {
			return 115;
		} else if (port.compareTo("pop3") == 0) {
			return 110;
		} else if (port.compareTo("ftp") == 0) {
			return 21;
		} else if (port.compareTo("ssh") == 0) {
			return 22;
		} else if (port.compareTo("telnet") == 0) {
			return 23;
		} else if (port.compareTo("smtp") == 0) {
			return 25;
		} else if (port.compareTo("tftp") == 0) {
			return 69;
		} else if (port.compareTo("pop2") == 0) {
			return 109;
		} else if (port.compareTo("netbios") == 0) {
			return 137;
		} else if (port.compareTo("snmp") == 0) {
			return 161;
		} else if (port.compareTo("snmptrap") == 0) {
			return 162;
		} else if (port.compareTo("bgp") == 0) {
			return 179;
		} else if (port.compareTo("rlogin") == 0) {
			return 513;
		} else if (port.compareTo("rpc") == 0) {
			return 530;
		} else if (port.compareTo("rtsp") == 0) {
			return 554;
		} else if (port.compareTo("ftp-data") == 0) {
			return 20;
		} else {
			return 0;
		}
	}

	public int getCodeVSStatus(String msg) {
		if (msg == null)
			return OBDefine.VS_STATUS.UNAVAILABLE;
		if (msg.compareToIgnoreCase("NO SERVICES UP") == 0)
			return OBDefine.VS_STATUS.UNAVAILABLE;
		else if (msg.compareToIgnoreCase("DISABLED") == 0)
			return OBDefine.VS_STATUS.DISABLE;
		else
			return OBDefine.VS_STATUS.AVAILABLE;
	}

	private String command(String cmndString) throws OBException {
		String out = sendCommand(cmndString);
		OBSystemLog.writeAlteonCmnd(cmndString, out);
		return out;
	}
}
