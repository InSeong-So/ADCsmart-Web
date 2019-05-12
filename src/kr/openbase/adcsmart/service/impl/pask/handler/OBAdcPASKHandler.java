package kr.openbase.adcsmart.service.impl.pask.handler;

import java.util.ArrayList;

import kr.openbase.adcsmart.service.dto.history.OBDtoAdcConfigChunkPASK;
import kr.openbase.adcsmart.service.dto.history.OBDtoAdcConfigNodePASK;
import kr.openbase.adcsmart.service.dto.history.OBDtoAdcConfigPoolMemberPASK;
import kr.openbase.adcsmart.service.dto.history.OBDtoAdcConfigVServerPASK;
import kr.openbase.adcsmart.service.impl.OBSShCmndExec;
import kr.openbase.adcsmart.service.impl.OBTelnetCmndExecV2;
import kr.openbase.adcsmart.service.impl.OBVServerDB;
import kr.openbase.adcsmart.service.impl.pask.dto.OBDtoAdcVServerPASK;
import kr.openbase.adcsmart.service.utility.OBCommon;
import kr.openbase.adcsmart.service.utility.OBDateTime;
import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.service.utility.OBExceptionLogin;
import kr.openbase.adcsmart.service.utility.OBExceptionUnreachable;
import kr.openbase.adcsmart.service.utility.OBParser;
import kr.openbase.adcsmart.service.utility.OBSystemLog;

public class OBAdcPASKHandler {
	private OBTelnetCmndExecV2 telnet;
	private OBSShCmndExec ssh;

	private final String SUFFIX_LOGIN = "login: ";
	private final String SUFFIX_PASSWD = "Password: ";
	private final String SUFFIX_PROMPT_OK = "# ";
	private static final String PASK_TERMINAL_MORE = "\\[7m--More--\\[m\\x0D*"; // escape를 포함한 정규식, 실제는 "[7m--More--[m"에
																				// |0D|가 0개 이상인 문자열
	private static final String PASK_TERMINAL_MORE1 = "--More-- "; // escape를 포함한 정규식, 실제는 "[7m--More--[m"에 |0D|가 0개 이상인
																	// 문자열
	private static final String PASK_TERMINAL_MORE2 = "--More--";
	private final int MAX_WAIT_TIME = 1000;// write memory 와 같은 경우 명령 수행 후 결과값 수신에 수초~수십초 소요된다. 이럴 경우에는 사용되는 timeout
											// 값이다.

	private String user = "";
	private String password = "";
	private String server = "";
	private int connService = OBDefine.SERVICE.TELNET;
	private int connPort = OBDefine.SERVICE.TELNET;

	public OBAdcPASKHandler() {
	}

//	private OBAdcPASKHandler(String server, String user, String password, int connService, int connPort) {
//		this.setServer(server);
//		this.setUser(user);
//		this.setPassword(password);
//		this.setConnService(connService);
//		this.setConnPort(connPort);
//	}

	public void setConnectionInfo(String server, String user, String password, int connService, int connPort) {
		if (connService == OBDefine.SERVICE.TELNET) {
			this.telnet = new OBTelnetCmndExecV2();
			this.telnet.setConnectionInfo(server, user, password, connPort);
			this.ssh = null;
		} else if (connService == OBDefine.SERVICE.SSH) {
			this.ssh = new OBSShCmndExec();
			this.ssh.setConnectionInfo(server, user, password, connPort);
			this.telnet = null;
		}

		this.server = server;
		this.user = user;
		this.password = password;
		this.connService = connService;
		this.connPort = connPort;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public int getConnService() {
		return connService;
	}

	public void setConnService(int connService) {
		this.connService = connService;
	}

	public int getConnPort() {
		return connPort;
	}

	public void setConnPort(int connPort) {
		this.connPort = connPort;
	}

	public synchronized void login() throws OBExceptionUnreachable, OBExceptionLogin, OBException {
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
		this.telnet.setConnectionInfo(this.server, this.user, this.password);
		this.telnet.setPromptInfo(this.SUFFIX_LOGIN, this.SUFFIX_PASSWD, this.SUFFIX_PROMPT_OK);
		this.telnet.login();
		OBSystemLog.writePASKCmnd(String.format("telnet login:%s", this.server), "success");
	}

	private synchronized void sshLogin() throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		if (this.ssh == null) {
			this.ssh = new OBSShCmndExec();
		}
		this.ssh.setConnectionInfo(this.server, this.user, this.password, this.connPort);
		this.ssh.sshLogin();
		OBSystemLog.writePASKCmnd(String.format("ssh login:%s", this.server), "success");
	}

	public synchronized void disconnect() {
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
	private synchronized String sendCommand(String command) throws OBException {
		OBSystemLog.writePASKCmnd(command, null);
		String out;
		if (this.telnet != null) {
			out = this.telnet.sendCommand(command, SUFFIX_PROMPT_OK);
		} else {
			out = this.ssh.sendCommand(command, SUFFIX_PROMPT_OK);
		}
		OBSystemLog.writePASKCmnd(null, out);
		return out;
	}

	private synchronized String sendCommandTimeout(String command, int timeOut) throws OBException {
		OBSystemLog.writePASKCmnd(command, null);
		String out;
		if (this.telnet != null) {
			out = this.telnet.sendCommandTimeout(command, SUFFIX_PROMPT_OK, timeOut);
		} else {
			out = this.ssh.sendCommandTimeout(command, SUFFIX_PROMPT_OK, timeOut);
		}
		OBSystemLog.writePASKCmnd(null, out);
		return out;
	}

	private synchronized String sendCommandOnce(String command, String prompt) throws OBException {
		OBSystemLog.writePASKCmnd(command, null);
		String out;
		if (this.telnet != null) {
			out = this.telnet.sendCommandOnce(command, prompt);
		} else {
			out = this.ssh.sendCommandOnce(command, prompt);
		}
		OBSystemLog.writePASKCmnd(null, out);
		return out;
	}

	private synchronized String sendCommandTimeout(String command, String prompt, int timeOut) throws OBException {
		OBSystemLog.writePASKCmnd(command, null);
		String out;
		if (this.telnet != null) {
			out = this.telnet.sendCommandTimeout(command, prompt, timeOut);
		} else {
			out = this.ssh.sendCommandTimeout(command, prompt, timeOut);
		}
		OBSystemLog.writePASKCmnd(null, out);
		return out;
	}

	private synchronized String sendCommand(String command, String prompt) throws OBException {
		OBSystemLog.writePASKCmnd(command, null);
		String out;
		if (this.telnet != null) {
			out = this.telnet.sendCommand(command, prompt);
		} else {
			out = this.ssh.sendCommand(command, prompt);
		}
		OBSystemLog.writePASKCmnd(null, out);
		return out;
	}

	private void beginConfig() throws OBException {
		String command = String.format("config");
		String output = sendCommand(command);
		if (isResultOk(output) == false) {
			throw new OBException(OBException.ERRCODE_SEND_COMMAND, command);// , String.format("command:configure,
																				// message:%s", output));
		}
	}

	// PASK 설정 순서 : 1.health-check (ADCSMART는 건드리지 않는다)
	// 2.realserver
	// 3.virtual server
	// virtual server 설정
	private void enterVirtualServer(String vsName) throws OBException {
		// virtual server 진입 -- slb VSNAME
		String command = String.format("slb %s", vsName);
		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "[config] " + command);
		String output = sendCommand(command);
		if (isResultOk(output) == false) {
			throw new OBException(OBException.ERRCODE_SEND_COMMAND, command);
		}
	}

	private void setVirtualServerIpProtocolPort(String vsIp, int vsProtocol, int vsPort) throws OBException {
		String command = String.format("vip %s protocol %s vport %d", vsIp,
				OBCommon.convertProtocolInteger2String(vsProtocol), vsPort);
		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "[config] " + command);
		String output = sendCommand(command);
		if (isResultOk(output) == false) {
			throw new OBException(OBException.ERRCODE_SEND_COMMAND, command);// String.format("command:%s, message:%s",
																				// command, output));
		}
	}

	// 원래 개개 구성요소를 지우기 우한 FULL 삭제 명령은 "no vip 1.1.1.1 protocol udp vport 80"인데,
	private void unsetVirtualServerIp(String vsIp) throws OBException {
		String command = String.format("no vip %s", vsIp);
		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "[config] " + command);
		String output = sendCommand(command);
		if (isResultOk(output) == false) {
			throw new OBException(OBException.ERRCODE_SEND_COMMAND, command);// , String.format("command:%s,
																				// message:%s", command, output));
		}
	}

	private void unsetVirtualServerIpProtocol(String vsIp, int vsProtocol) throws OBException {
		String command = String.format("no vip %s protocol %s", vsIp,
				OBCommon.convertProtocolInteger2String(vsProtocol));
		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "[config] " + command);
		String output = sendCommand(command);
		if (isResultOk(output) == false) {
			throw new OBException(OBException.ERRCODE_SEND_COMMAND, command);// , String.format("command:%s,
																				// message:%s", command, output));
		}
	}

	private void unsetVirtualServerIpProtocolPort(String vsIp, int vsProtocol, int vsPort) throws OBException {
		String command = String.format("no vip %s protocol %s vport %d", vsIp,
				OBCommon.convertProtocolInteger2String(vsProtocol), vsPort);
		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "[config] " + command);
		String output = sendCommand(command);
		if (isResultOk(output) == false) {
			throw new OBException(OBException.ERRCODE_SEND_COMMAND, command);// , String.format("command:%s,
																				// message:%s", command, output));
		}
	}

	public void setLoadbalancing(int lbMethod) throws OBException {
		String command = String.format("lb-method %s", OBCommon.convertLBMethodInteger2StringPASK(lbMethod));
		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "[config] " + command);
		String output = sendCommand(command);
		if (isResultOk(output) == false) {
			throw new OBException(OBException.ERRCODE_SEND_COMMAND, command);// , String.format("command:%s,
																				// message:%s", command, output));
		}
	}

	private void delVirtualServer(String vsName) throws OBException {
		String command = String.format("no slb %s", vsName);
		String output = sendCommand(command);
		if (isResultOk(output) == false) {
			throw new OBException(OBException.ERRCODE_SEND_COMMAND, command);// , String.format("command:%s,
																				// message:%s", command, output));
		}
	}

	private void unsetHealthcheck(ArrayList<Integer> healthcheckIdList) throws OBException {
		String idList = OBParser.removeSpaces(OBParser.convertList2CommaString(healthcheckIdList));
		if (idList.isEmpty() == false) {
			String command = String.format("no health %s", idList);
			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "[config]" + command);
			String output = sendCommand(command);
			if (isResultOk(output) == false) {
				throw new OBException(OBException.ERRCODE_SEND_COMMAND, command);// , String.format("command:%s,
																					// message:%s", command, output));
			}
		}
	}

	// health-check 등록, 이미 있는 것을 넣으면 에러가 나는데 밖에서 중복 체크는 미리 한다.
	public void setHealthcheck(ArrayList<Integer> healthcheckIdList) throws OBException {
		String idList = OBParser.removeSpaces(OBParser.convertList2CommaString(healthcheckIdList));
		if (idList.isEmpty() == false) {
			String command = String.format("health %s", idList);
			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "[config] " + command);
			String output = sendCommand(command);
			if (isResultOk(output) == false) {
				throw new OBException(OBException.ERRCODE_SEND_COMMAND, command);// , String.format("command:%s,
																					// message:%s", command, output));
			}
		}
	}

	private void unsetRealServer(ArrayList<Integer> realServerIdList) throws OBException {
		String ridList = OBParser.removeSpaces(OBParser.convertList2CommaString(realServerIdList));
		if (ridList.isEmpty() == false) {
			String command = String.format("no real %s", ridList);
			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "[config] " + command);
			String output = sendCommand(command);
			if (isResultOk(output) == false) {
				throw new OBException(OBException.ERRCODE_SEND_COMMAND, command);// , String.format("command:%s,
																					// message:%s", command, output));
			}
		}
	}

	public void setRealServer(ArrayList<Integer> realServerIdList) throws OBException {
		String ridList = OBParser.removeSpaces(OBParser.convertList2CommaString(realServerIdList));
		if (ridList.isEmpty() == false) {
			String command = String.format("real %s", ridList);
			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "[config] " + command);
			String output = sendCommand(command);
			if (isResultOk(output) == false) {
				throw new OBException(OBException.ERRCODE_SEND_COMMAND, command);// , String.format("command:%s,
																					// message:%s", command, output));
			}
		}
	}

	private void enterRealServer(int realServerId) throws OBException {
		String command = String.format("real %d", realServerId);
		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "[config] " + command);
		String output = sendCommand(command);
		if (isResultOk(output) == false) {
			throw new OBException(OBException.ERRCODE_SEND_COMMAND, command);// String.format("command:%s, message:%s",
																				// command, output));
		}
	}

	private void configRealServerDefaultName(int realServerId) throws OBException {
		String command = String.format("name real%d", realServerId);
		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "[config] " + command);
		String output = sendCommand(command);
		if (isResultOk(output) == false) {
			throw new OBException(OBException.ERRCODE_SEND_COMMAND, command);// , String.format("command:%s,
																				// message:%s", command, output));
		}
	}

	private void configRealServerIp(String realServerIp) throws OBException {
		String command = String.format("rip %s", realServerIp);
		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "[config] " + command);
		String output = sendCommand(command);
		if (isResultOk(output) == false) {
			throw new OBException(OBException.ERRCODE_SEND_COMMAND, command);// , String.format("command:%s,
																				// message:%s", command, output));
		}
	}

	private void configRealServerPort(int realServerPort) throws OBException {
		String command = String.format("rport %d", realServerPort);
		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "[config] " + command);
		String output = sendCommand(command);
		if (isResultOk(output) == false) {
			throw new OBException(OBException.ERRCODE_SEND_COMMAND, command);// , String.format("command:%s,
																				// message:%s", command, output));
		}
	}

	public void setState(int state) throws OBException {
		String command;
		if (state == OBDefine.STATE_ENABLE) {
			command = "status enable";
		} else {
			command = "status disable";
		}
		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "[config] " + command);
		String output = sendCommand(command);
		if (isResultOk(output) == false) {
			throw new OBException(OBException.ERRCODE_SEND_COMMAND, command);// , String.format("command:%s,
																				// message:%s", command, output));
		}
	}

	public void apply() throws OBException {
		String command = "apply";
		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "[config]apply vs");
		String output = sendCommand(command);
		if (isResultOk(output) == false) {
			throw new OBException(OBException.ERRCODE_SEND_COMMAND, command);// , String.format("command:%s,
																				// message:%s", command, output));
		}
	}

	private void exit() throws OBException {
		String command = "exit";
		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "[config] " + command);
		sendCommand(command);
		// exit은 성공/실패를 판단하지 않는다.
		// 1. 중요도가 높지 않다.
		// 2. 최상단으로 가기 위해 최대 2회의 exit을 해야 하는데, 현재 위치를 관리하지 않기 때문에 무조건 2회 exit을 해야 한다.
		// 그럴때는 2회째 실패가 나오지만 문제가 되지 않는다.
//		if(isResultOk(output)==false)
//		{
//			throw new OBException(OBException.ERRCODE_SEND_COMMAND, command);//, String.format("command:%s, message:%s", command, output));
//		}
	}

	public void writeMemory() throws OBException {
		exit();
		exit();
		beginConfig();
		String command = "write-memory";
		String output = sendCommandTimeout(command, MAX_WAIT_TIME);
		if (isResultOk(output) == false) {
			throw new OBException(OBException.ERRCODE_SEND_COMMAND, command);// , String.format("command:%s,
																				// message:%s", command, output));
		}
	}

	public int configSlb(OBDtoAdcConfigChunkPASK configChunk) throws OBException {
		// vsConfig 중 다음 두 항목은 이 루틴에서 처리하지 않는다.
		// 1. vsConfig.getPoolChange(): pool이 바뀌는 것을 표시하는데, PAS,PASK는 pool을 바꾸지 않는다.
		// 2. vsConfig.getStateChange(): enable/disable은 별도 루틴으로 처리한다.
		OBDtoAdcConfigVServerPASK vsConfig = configChunk.getVsConfig();

		OBDtoAdcVServerPASK vsNew = configChunk.getVsConfig().getVsNew();
		OBDtoAdcVServerPASK vsOld = configChunk.getVsConfig().getVsOld();

		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "vsNew = " + vsNew);
		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "vsOld = " + vsOld);

		OBVServerDB vsDb = new OBVServerDB();

		boolean checkResult = vsDb.isExistVirtualServer(vsNew.getAdcIndex(), vsNew.getName());

		if (vsConfig.getChange() == OBDefine.CHANGE_TYPE_ADD && checkResult == true) { // vs add인데 이미 있는 vs면 문제
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
					String.format("Existing virtual server can't be added.(adc index:%d, virtual server: %s)",
							vsNew.getAdcIndex(), vsNew.getName()));
		} else if (vsConfig.getChange() == OBDefine.CHANGE_TYPE_EDIT && checkResult == false) { // vs edit인데 없는 vs면 문제
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
					String.format("Target virtual server is not found.(adc index:%d, virtual server: %s)",
							vsNew.getAdcIndex(), vsNew.getName()));
		}
		// vs 추가/수정 작업만 한다.
		if (vsConfig.getChange() != OBDefine.CHANGE_TYPE_ADD && vsConfig.getChange() != OBDefine.CHANGE_TYPE_EDIT) {
			if (vsConfig.getChange() == OBDefine.CHANGE_TYPE_NONE) {
				OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "[config] Nothing changed.");
			} else {
				OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "[config] Invalid Access");
			}
			return OBDefine.CHANGE_TYPE_NONE;
		}

		// config 시작 !!!! 여기부터 exception 처리 주의
		// ADC에 적용되므로, 적용시점마다 적용내역을 기록해놓고, exception을 잡으면 처리 한다.
		ArrayList<Integer> setMemberList = new ArrayList<Integer>();
		ArrayList<Integer> unsetMemberList = new ArrayList<Integer>();
		try {
			beginConfig(); // ADC의 config 모드 개시, 오류시 OBException
			// real server 등록 작업을 먼저 한다.
			if (vsConfig.getNodeConfigList() != null) // 추가할 real이 없으면 null일 수 있다.?
			{
				if (vsConfig.getNodeConfigList().size() > 0) {
					ArrayList<OBDtoAdcConfigNodePASK> realChangeList = vsConfig.getNodeConfigList();
					OBDtoAdcConfigNodePASK tempReal = null; // 작업 임시 변수
					int i = 0, rid = 0, ridIndex = 0;
					OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "realChangeList = " + realChangeList);
					// 변경 멤버 개수만큼 available real id를 구해온다. 모두 쓰지 않을 수 있지만 최대 개수를 준비한다.
					ArrayList<Integer> availableRidList = vsDb.getAvailableRealServerIndexPASK(vsNew.getAdcIndex(),
							realChangeList.size());
					for (i = 0; i < realChangeList.size(); i++) {
						tempReal = realChangeList.get(i);
						if (tempReal.getChange() == OBDefine.CHANGE_TYPE_ADD) {
							rid = availableRidList.get(ridIndex); // 빈 real id 중 최소값을 구한다.
							// real 설정 진입
							enterRealServer(rid);
							// 추가면 rid로 만든 기본이름을 넣어준다. "real1", "real2", "real3"...
							configRealServerDefaultName(rid); // 이름에 중복 제약이 없다
							// real server ipaddress
							configRealServerIp(tempReal.getNodeNew().getIpAddress());
							// real server port
							configRealServerPort(tempReal.getNodeNew().getPort());
							// real state
							if (tempReal.getNodeNew().getState().equals(OBDefine.STATE_DISABLE) == true) { // enable은
																											// 기본으로 되기
																											// 때문에 안
																											// 해준다.
								setState(tempReal.getNodeNew().getState());
							}
							// real 저장. 이상하게도 경로변경이 없으므로 인위로 exit해야 한다.
							apply();

							exit();

							tempReal.getNodeNew().setId(rid); // id가 확정되었으니 넣는다.
							setMemberList.add(rid); // 추가할 멤버
							ridIndex++;
						}
					}
				}
			}

			if (vsConfig.getMemberChange() == OBDefine.CHANGE_TYPE_EDIT) {
				int i = 0, rid = 0;

				ArrayList<OBDtoAdcConfigPoolMemberPASK> memberChangeList = vsConfig.getMemberConfigList();
				OBDtoAdcConfigPoolMemberPASK tempMember = null; // 작업 임시 변수

				int memberChangeCount = memberChangeList.size();
				OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "memberChangeList = " + memberChangeList);

				for (i = 0; i < memberChangeCount; i++) {
					tempMember = memberChangeList.get(i);
					if (tempMember.getChange() == OBDefine.CHANGE_TYPE_EDIT) // PASK는 real의 state만 수정할 수 있다.
					{
						// real state
						if (tempMember.getMemberNew().getState()
								.equals(tempMember.getMemberOld().getState()) == false) {
							rid = tempMember.getMemberNew().getId();
							enterRealServer(rid);
							setState(tempMember.getMemberNew().getState());
							// real 저장. 이상하게도 경로변경이 없으므로 인위로 exit해야 한다.
							apply();
							exit();
						}
					} else if (tempMember.getChange() == OBDefine.CHANGE_TYPE_ADD
							&& tempMember.getMemberNew().getId() != null) // 추가할 멤버, null인 것은 node에서 처리
					{
						OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "member to add = " + tempMember.getMemberNew());
						setMemberList.add(tempMember.getMemberNew().getId());
					} else if (tempMember.getChange() == OBDefine.CHANGE_TYPE_DELETE) {
						unsetMemberList.add(tempMember.getMemberOld().getId());
					}
				}
			}

			// virtual server 진입 -- slb VSNAME
			enterVirtualServer(vsNew.getName());

			// virtual server IP, port, protocol //vs IP도 바꿀 수 있다
			if (vsConfig.getIpChange() == OBDefine.CHANGE_TYPE_EDIT
					|| vsConfig.getPortChange() == OBDefine.CHANGE_TYPE_EDIT
					|| vsConfig.getProtocolChange() == OBDefine.CHANGE_TYPE_EDIT) {
				setVirtualServerIpProtocolPort(vsNew.getvIP(), vsNew.getSrvProtocol(), vsNew.getSrvPort());
				if (vsConfig.getChange() == OBDefine.CHANGE_TYPE_EDIT) // 추가일 때는 지우지 않음
				{
					if (vsConfig.getIpChange() == OBDefine.CHANGE_TYPE_EDIT) { // IP가 바뀐 경우는 새 IP가 추가되면서 기존 IP엔트리가 잉여가
																				// 되므로 기존IP 엔트리를 전부 날린다. 참고:수정은 하나일때만
																				// 한다.
						unsetVirtualServerIp(vsOld.getvIP());
					} else if (vsConfig.getProtocolChange() == OBDefine.CHANGE_TYPE_EDIT) { // protocol이 바뀌면
																							// protocol엔트리가 생기게 되므로
																							// protocol로 지운다. 단 이전에
																							// protocol이 없었던 거면 추가되지 않으니
																							// 안 지운다.
						if (vsOld.getSrvProtocol().equals(OBDefine.PROTOCOL_NA) == false) { // 전에 protocol이 있었는데 바뀌었으니
																							// 기존 것을 protocol level에서
																							// 지운다.
							unsetVirtualServerIpProtocol(vsOld.getvIP(), vsOld.getSrvProtocol());
						}
					} else if (vsConfig.getPortChange() == OBDefine.CHANGE_TYPE_EDIT) {
						if (vsOld.getSrvPort().equals(OBDefine.PORT_NA) == false) { // 기존에 port가 있었을 때만 지운다.
							unsetVirtualServerIpProtocolPort(vsOld.getvIP(), vsOld.getSrvProtocol(),
									vsOld.getSrvPort());
						}
					}
				}
			}

			// lb-method -- lb-method rr ("rr" or "lc" or "hash")
			if (vsConfig.getLbMethodChange() == OBDefine.CHANGE_TYPE_EDIT) {
				setLoadbalancing(vsNew.getPool().getLbMethod());
			}

			// 처리 안하는 부분 설명 : virtual server state:수정에서 state는 건드리지 않는다. SKIP!

			// healthcheck 변경, PASK에서 healthcheck 복수 지정이 가능하나, ADCSMART는 1개를 set,unset,
			// replace만 할 수 있다.
			if (vsConfig.getHealthCheckChange() != OBDefine.CHANGE_TYPE_NONE) {
				ArrayList<Integer> healthUnsetList = new ArrayList<Integer>();
				ArrayList<Integer> healthSetList = new ArrayList<Integer>();
				if (vsConfig.getHealthCheckChange() == OBDefine.CHANGE_TYPE_ADD
						|| vsConfig.getHealthCheckChange() == OBDefine.CHANGE_TYPE_EDIT) {
					healthSetList.add(vsNew.getPool().getHealthCheckInfo().getId());
				}
				if (vsConfig.getHealthCheckChange() == OBDefine.CHANGE_TYPE_DELETE
						|| vsConfig.getHealthCheckChange() == OBDefine.CHANGE_TYPE_EDIT) {
					healthUnsetList.add(vsOld.getPool().getHealthCheckInfo().getId());
				}
				unsetHealthcheck(healthUnsetList);
				setHealthcheck(healthSetList);
			}

			// real server: VS 설정에서는 set/unset만 가능하다. 가져다 쓰던가 빼던가
			if (vsConfig.getMemberChange() == OBDefine.CHANGE_TYPE_EDIT) {
				unsetRealServer(unsetMemberList);
				setRealServer(setMemberList);
			}
			apply();
			writeMemory();
		} catch (OBException e) {
			throw e;
		}
		return vsConfig.getChange();
	}

//	public void undoConfigSlb(OBDtoAdcConfigChunkPAS configChunk, Apply apply) throws OBException
//	{
//		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "begin");
//		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "end");
//	}

	public void enableDisableVirtualServer(ArrayList<String> virtualServerNames, Integer state) throws OBException {
		if (virtualServerNames == null || virtualServerNames.isEmpty()) {
			return;
		}
		beginConfig();

		for (String vsName : virtualServerNames) {
			enterVirtualServer(vsName); // virtual server slb 진입 -- slb VSNAME1
			setState(state);
			apply();// 적용후 virtual server --> configure로 이동
			exit();
		}
		writeMemory();
	}

	public void delVirtualServer(ArrayList<String> virtualServerNames) throws OBException {
		beginConfig();

		for (String vsName : virtualServerNames) {
			delVirtualServer(vsName); // apply 필요없음
		}
		writeMemory();
	}

	private boolean isResultOk(String result) {
		String resultLower = result.toLowerCase();

		if (resultLower.contains("error")) {
			return false;
		} else if (resultLower.contains("incomplete command")) {
			return false;
		} else if (resultLower.contains("ambiguous command")) {
			return false;
		} else if (resultLower.contains("unknown command")) {
			return false;
		} else {
			return true;
		}
	}

//	public static void main(String [] args) 
//	{
//		try
//		{
//			OBAdcPASK handler = new OBAdcPASK("192.168.200.110", "root", "admin");
//			//handler.enter();
//			handler.login();
//			String cfg = handler.cmndSlbDump();
//			System.out.println(cfg);
//			handler.disconnect();
////			OBDtoLicenseInfoPAS info = new OBCLIParserPAS().parseLicense(cfg);
////			System.out.println(info);
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}

	/**
	 * 터미널 페이징에서 나오는 "[7m--More--[m"를 지운다. 페이징은 PASK1.5에서는 아예 기능이 없었고, PASK1.7에서 생긴
	 * 기능인데, 버퍼데이터 처리를 표준화한다는 의미로 PASK 전체 소스에 적용한다. 제거 패턴 comment: \x0D*는,
	 * "[7m--More--[m"뒤에 보이지 않지만 \x0D가 1~2개 붙어있기 때문에. vi로 보면 ^M으로 표시됨
	 * 
	 * @param buffer
	 * @return
	 */
	private static String removeTerminalMore(String buffer) {
		if (buffer == null) {
			return null;
		}
		String temp = buffer.replaceAll(PASK_TERMINAL_MORE, "").replaceAll(PASK_TERMINAL_MORE1, "")
				.replaceAll(PASK_TERMINAL_MORE2, "");
		return temp;
	}
//	public static void main(String[] args) //removeTerminalMore() test 
//	{
//		String input = "  exit[K\n[7m--More--[m\n    type include\n[7m--More--[m\n  sticky time 60\n[7m--More--[m aaaaID Type Port Status\n  [7m--More--[m bbbb ";
//		
//		String result = removeTerminalMore(OBParser.trimSpaces(input));
//		System.out.println(String.format("input[%s],\nresult[%s]", input, result));
//	}

	public String cmndSlbDump() throws OBException {
		return removeTerminalMore(OBParser.trimAndSimplifySpaces(command(OBAdcPASKHandler.CMND_STRG_SLB_DUMP)));
	}

	private String command(String cmndString) throws OBException {
		OBSystemLog.writePASKCmnd(cmndString, null);
		String out = sendCommand(cmndString, SUFFIX_PROMPT_OK);
		OBSystemLog.writePASKCmnd(null, out);
		return out;
	}

	private String command(String cmndString, int timeout) throws OBException {
		OBSystemLog.writePASKCmnd(cmndString, null);
		String out = sendCommandTimeout(cmndString, SUFFIX_PROMPT_OK, timeout);
		OBSystemLog.writePASKCmnd(null, out);
		return out;
	}

	private String commandOnce(String cmndString) throws OBException {
		OBSystemLog.writePASKCmnd(cmndString, null);
		String out = sendCommandOnce(cmndString, SUFFIX_PROMPT_OK);
		OBSystemLog.writePASKCmnd(null, out);
		return out;
	}

//	public static void main(String [] args) 
//	{
//		try
//		{
//			OBAdcPASK handler = new OBAdcPASK("192.168.200.110", "root", "admin");
//			//handler.enter();
//			handler.login();
//			String cfg = handler.cmndLicense();
//			System.out.println(cfg);
//			handler.disconnect();
////			OBDtoLicenseInfoPAS info = new OBCLIParserPAS().parseLicense(cfg);
////			System.out.println(info);
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}
//	-----------------------------------------------------
//	Warranty License Information
//	-----------------------------------------------------
//	Warranty License Status  = Demo Warranty License
//	Expiration Date          = 2013.05.19
//	-----------------------------------------------------		
	// 라이센스 확인.
	public String cmndLicense() throws OBException {
		return removeTerminalMore(OBParser.trimAndSimplifySpaces(command(OBAdcPASKHandler.CMND_STRG_LICENSE)));
	}

//	public static void main(String [] args) 
//	{
//		try
//		{
//			OBAdcPASK handler = new OBAdcPASK("192.168.200.110", "root", "admin");
//			//handler.enter();
//			handler.login();
//			String cfg = handler.cmndSystem();
//			System.out.println(cfg);
//			handler.disconnect();
////			OBDtoLicenseInfoPAS info = new OBCLIParserPAS().parseLicense(cfg);
////			System.out.println(info);
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}		
//	-----------------------------------------------------------------------------
//	System information
//	-----------------------------------------------------------------------------
//	Product Name                  = PAS 5216
//	Serial Number                 = R208S10000A06650
//	Version                       = 10.7.60 (10.7.60)
//	Bootloader Version            = 1.10.14
//	Worm Protection Version       = 3.1.10
//	E-mail Worm Signature Version = 3.2.40
//	Intrusion Prevention Version  = 3.4.39
//	Current Boot Flash            = primary
//	Mgmt Port MAC Address         = 00:06:C4:70:1D:BA
//	Mgmt Port IP Address          = 192.168.100.1/24
//	SDRAM Size                    = 5120 MB
//	Flash Size                    =  128 MB
//	System Uptime                 = 8 days 5 hours 6 minutes 33 seconds
//	-----------------------------------------------------------------------------	
	// system 확인.
	public String cmndSystem() throws OBException {
		return removeTerminalMore(OBParser.trimAndSimplifySpaces(command(OBAdcPASKHandler.CMND_STRG_SYSTEM)));
	}

//	public static void main(String [] args) 
//	{
//		try
//		{
//			OBAdcPASK handler = new OBAdcPASK("192.168.200.110", "root", "admin");
//			//handler.enter();
//			handler.login();
//			String cfg = handler.cmndInterface();
//			handler.disconnect();
//			System.out.println(cfg);
//			ArrayList<OBDtoInterfaceInfoPASK> info = new OBCLIParserPASK().parseInterface(cfg);
//			System.out.println(info);
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}
	public String cmndInterface() throws OBException {
		return removeTerminalMore(OBParser.trimAndSimplifySpaces(command(OBAdcPASKHandler.CMND_STRG_INTERFACE)));
	}

//	public static void main(String [] args) 
//	{
//		try
//		{
//			OBAdcPASK handler = new OBAdcPASK("192.168.200.110", "root", "admin");
//			//handler.enter();
//			handler.login();
//			String cfg = handler.cmndGateway();
//			System.out.println(cfg);
//			handler.disconnect();
//			ArrayList<OBDtoGatewayInfoPASK> info = new OBCLIParserPASK().parseGateway(cfg);
//			System.out.println(info);
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}
	public String cmndGateway() throws OBException {
		return removeTerminalMore(OBParser.trimAndSimplifySpaces(command(OBAdcPASKHandler.CMND_STRG_GATEWAY)));
	}

//	public static void main(String [] args) 
//	{
//		try
//		{
//			OBAdcPASK handler = new OBAdcPASK("192.168.200.110", "root", "admin");
//			//handler.enter();
//			handler.login();
//			String cfg = handler.cmndResources();
//			System.out.println(cfg);
//			handler.disconnect();
//			OBDtoResourceInfoPASK info = new OBCLIParserPASK().parseResources(cfg);
//			System.out.println(info);
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}
	public String cmndResources() throws OBException {
		return removeTerminalMore(OBParser.trimAndSimplifySpaces(command(OBAdcPASKHandler.CMND_STRG_RESOURCES)));
	}

//	public static void main(String [] args) 
//	{
//		try
//		{
//			OBAdcPASK handler = new OBAdcPASK("192.168.200.110", "root", "admin");
//			//handler.enter();
//			handler.login();
//			String cfg = handler.cmndPortInfo();
//			System.out.println(cfg);
//			handler.disconnect();
//			ArrayList<OBDtoPortInfoPASK> info = new OBCLIParserPASK().parsePortUpdown(cfg);
//			System.out.println(info);
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}
	public String cmndPortInfo() throws OBException {
		return removeTerminalMore(OBParser.trimAndSimplifySpaces(command(OBAdcPASKHandler.CMND_STRG_SHOW_PORT)));
	}

//	public static void main(String [] args) 
//	{
//		try
//		{
//			OBAdcPASK handler = new OBAdcPASK("192.168.200.110", "root", "admin");
//			//handler.enter();
//			handler.login();
//			String cfg = handler.cmndVlanInfo();
//			System.out.println(cfg);
//			handler.disconnect();
//			ArrayList<OBDtoVLanInfoPASK> info = new OBCLIParserPASK().parseVlanInfo(cfg);
//			System.out.println(info);
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}
	public String cmndVlanInfo() throws OBException {
		return removeTerminalMore(OBParser.trimAndSimplifySpaces(command(OBAdcPASKHandler.CMND_STRG_SHOW_VLAN)));
	}

//	public static void main(String [] args) 
//	{
//		try
//		{
//			OBAdcPASK handler = new OBAdcPASK("192.168.200.110", "root", "admin");
//			//handler.enter();
//			handler.login();
//			String cfg = handler.cmndSyslogInfo();
//			System.out.println(cfg);
//			handler.disconnect();
//			ArrayList<OBDtoSyslogInfoPASK>  info = new OBCLIParserPASK().parseSyslogInfo(cfg);
//			System.out.println(info);
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}
	public String cmndSyslogInfo() throws OBException {
		return removeTerminalMore(OBParser.trimAndSimplifySpaces(command(OBAdcPASKHandler.CMND_STRG_SHOW_SYSLOG)));
	}

//	public static void main(String [] args) 
//	{
//		try
//		{
//			OBAdcPASK handler = new OBAdcPASK("192.168.200.110", "root", "admin");
//			//handler.enter();
//			handler.login();
//			String cfg = handler.cmndNTPInfo();
//			System.out.println(cfg);
//			handler.disconnect();
//			OBDtoNTPInfoPASK info = new OBCLIParserPASK().parseNTPInfo(cfg);
//			System.out.println(info);
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}
	public String cmndNTPInfo() throws OBException {
		return removeTerminalMore(OBParser.trimAndSimplifySpaces(command(OBAdcPASKHandler.CMND_STRG_SHOW_NTP)));
	}

//	public static void main(String [] args) 
//	{
//		try
//		{
//			OBAdcPASK handler = new OBAdcPASK("192.168.200.110", "root", "admin");
//			//handler.enter();
//			handler.login();
//			String cfg = handler.cmndPortStatistics();
//			System.out.println(cfg);
//			handler.disconnect();
//			ArrayList<OBDtoPortStatPASK> info = new OBCLIParserPASK().parsePortStatistics(cfg);
//			System.out.println(info);
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}
	public String cmndPortStatistics() throws OBException {
		return removeTerminalMore(OBParser.trimAndSimplifySpaces(command(OBAdcPASKHandler.CMND_STRG_PORT_STATISTICS)));
	}

//	public static void main(String [] args) 
//	{
//		try
//		{
//			OBAdcPASK handler = new OBAdcPASK("192.168.200.110", "root", "admin");
//			//handler.enter();
//			handler.login();
//			String cfg = handler.cmndHwStatistics();
//			System.out.println(cfg);
//			handler.disconnect();
//			OBDtoHWStatPASK info = new OBCLIParserPASK().parseHWStatistics(cfg);
//			System.out.println(info);
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}
	public String cmndHwStatistics() throws OBException {
		return removeTerminalMore(OBParser.trimAndSimplifySpaces(command(OBAdcPASKHandler.CMND_STRG_HW_STATISTICS)));
	}

//	public static void main(String [] args) 
//	{
//		try
//		{
//			OBAdcPASK handler = new OBAdcPASK("192.168.200.110", "root", "admin");
//			//handler.enter();
//			handler.login();
//			String cfg = handler.cmndSnmpInfo();
//			System.out.println(cfg);
//			handler.disconnect();
//			OBDtoSnmpInfoPASK info = new OBCLIParserPASK().parseSnmpInfo(cfg);
//			System.out.println(info);
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}
	public String cmndSnmpInfo() throws OBException {
		return removeTerminalMore(OBParser.trimAndSimplifySpaces(command(OBAdcPASKHandler.CMND_STRG_SHOW_SNMP)));
	}

	/*
	 * private boolean isResultOk(String result) { String resultLower =
	 * result.toLowerCase();
	 * 
	 * if(resultLower.contains("error")) { return false; } else
	 * if(resultLower.contains("incomplete command")) { return false; } else
	 * if(resultLower.contains("ambiguous command")) { return false; } else
	 * if(resultLower.contains("unknown command")) { return false; } else { return
	 * true; } }
	 */
	public String cmndSnmpStatus(String config) throws OBException {
		OBDateTime.Sleep(500);
		String output = command(OBAdcPASKHandler.CMND_STRG_CFG);
		if (isResultOk(output) == false) {
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
					String.format("parsing error(command:%s, result:%s)", OBAdcPASKHandler.CMND_STRG_CFG, output));
		}

		OBDateTime.Sleep(500);
		output = command(OBAdcPASKHandler.CMND_STRG_CFG_SNMP);
		if (isResultOk(output) == false) {
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
					String.format("parsing error(command:%s, result:%s)", OBAdcPASKHandler.CMND_STRG_CFG_SNMP, output));
		}

		OBDateTime.Sleep(500);
		String cmnnd = String.format(OBAdcPASKHandler.CMND_STRG_STATUS, config);
		return removeTerminalMore(OBParser.trimAndSimplifySpaces(command(cmnnd)));
	}

	public String cmndSnmpRComm() throws OBException {
		OBDateTime.Sleep(500);
		String output = command(OBAdcPASKHandler.CMND_STRG_CFG);
		if (isResultOk(output) == false) {
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
					String.format("parsing error(command:%s, result:%s)", OBAdcPASKHandler.CMND_STRG_CFG, output));
		}

		OBDateTime.Sleep(500);
		output = command(OBAdcPASKHandler.CMND_STRG_CFG_SNMP);
		if (isResultOk(output) == false) {
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
					String.format("parsing error(command:%s, result:%s)", OBAdcPASKHandler.CMND_STRG_CFG_SNMP, output));
		}

		OBDateTime.Sleep(500);
		return removeTerminalMore(OBParser.trimAndSimplifySpaces(command(OBAdcPASKHandler.CMND_STRG_SHOW_COMMUNITY)));
	}

	public String cmndSyslogStatus(String config) throws OBException {
		OBDateTime.Sleep(500);
		String output = command(OBAdcPASKHandler.CMND_STRG_CFG);
		if (isResultOk(output) == false) {
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
					String.format("parsing error(command:%s, result:%s)", OBAdcPASKHandler.CMND_STRG_CFG, output));
		}

		OBDateTime.Sleep(500);
		output = command(OBAdcPASKHandler.CMND_STRG_LOGGING);
		if (isResultOk(output) == false) {
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
					String.format("parsing error(command:%s, result:%s)", OBAdcPASKHandler.CMND_STRG_LOGGING, output));
		}

		OBDateTime.Sleep(500);
		String cmnnd = String.format(OBAdcPASKHandler.CMND_STRG_SERVER_STATUS, config);
		return removeTerminalMore(OBParser.trimAndSimplifySpaces(command(cmnnd)));
	}

	public String cmndSyslogList(String config) throws OBException {
		OBDateTime.Sleep(500);
		String output = command(OBAdcPASKHandler.CMND_STRG_CFG);
		if (isResultOk(output) == false) {
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
					String.format("parsing error(command:%s, result:%s)", OBAdcPASKHandler.CMND_STRG_CFG, output));
		}

		OBDateTime.Sleep(500);
		output = command(OBAdcPASKHandler.CMND_STRG_LOGGING);
		if (isResultOk(output) == false) {
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
					String.format("parsing error(command:%s, result:%s)", OBAdcPASKHandler.CMND_STRG_LOGGING, output));
		}

		OBDateTime.Sleep(500);
		String cmnnd = String.format(OBAdcPASKHandler.CMND_STRG_SERVER, config);
		return removeTerminalMore(OBParser.trimAndSimplifySpaces(command(cmnnd)));
	}
//	public static void main(String [] args) 
//	{
//		try
//		{
//			OBAdcPASK handler = new OBAdcPASK("192.168.200.110", "root", "admin");
//			//handler.enter();
//			handler.login();
//			String cfg = handler.cmndSlbStatus();
//			System.out.println(cfg);
//			handler.disconnect();
////			OBDtoLicenseInfoPAS info = new OBCLIParserPAS().parseLicense(cfg);
////			System.out.println(info);
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}

//	public static void main(String [] args) 
//	{
//		try
//		{
//			OBAdcPASKHandler handler = new OBAdcPASKHandler("192.168.200.110", "root", "admin", OBDefine.SERVICE.TELNET, OBDefine.SERVICE.TELNET);
//			//handler.enter();
//			handler.login();
//			String cfg = handler.cmndSnmpStatus("enable");
//			handler.apply();
//			System.out.println(cfg);
//			handler.disconnect();
//			ArrayList<OBDtoLoggingBufferPASK>  info = new OBCLIParserPASK().parseLoggingBuffer(cfg);
//			System.out.println(info);
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}
	public String cmndLoggingBuffer() throws OBException {
		return removeTerminalMore(commandOnce(OBAdcPASKHandler.CMND_STRG_LOGGINGBUFFER)); // 로그는 trimSpaces를 하지 않고
																							// Terminal More만 지운다.
	}

	public String cmndRealDump() throws OBException {
		return removeTerminalMore(OBParser.trimAndSimplifySpaces(command(OBAdcPASKHandler.CMND_STRG_REAL_DUMP)));
	}

	public String cmndHealthDump() throws OBException {
		return removeTerminalMore(OBParser.trimAndSimplifySpaces(command(OBAdcPASKHandler.CMND_STRG_HEALTH_DUMP)));
	}

	public String cmndCfgDump() throws OBException {
		return removeTerminalMore(OBParser.trimAndSimplifySpaces(command(OBAdcPASKHandler.CMND_STRG_DUMP_CONFIG)));
	}

	public String cmndShowHostname() throws OBException {
		return removeTerminalMore(OBParser.trimAndSimplifySpaces(command(OBAdcPASKHandler.CMND_STRG_SHOW_HOSTNAME)));
	}

	public String cmndShowInfoSlb() throws OBException {
		return removeTerminalMore(
				OBParser.trimAndSimplifySpaces(command(OBAdcPASKHandler.CMND_STRG_SHOW_INFO_SLB, MAX_WAIT_TIME)));
	}

	public String cmndShowTrunkInfo() throws OBException {
		return removeTerminalMore(OBParser.trimAndSimplifySpaces(command(OBAdcPASKHandler.CMND_STRG_SHOW_TRUNK)));
	}

	public String cmndShowStpInfo() throws OBException {
		return removeTerminalMore(OBParser.trimAndSimplifySpaces(command(OBAdcPASKHandler.CMND_STRG_SHOW_STP)));
	}

	private final static String CMND_STRG_LOGGINGBUFFER = "show log";// show statistics port 1
	private final static String CMND_STRG_SHOW_INFO_SLB = "show info slb";
	private final static String CMND_STRG_SHOW_SNMP = "show snmp";// show statistics port 1
	private final static String CMND_STRG_HW_STATISTICS = "show hardwarestatus";// show statistics port 1
	private final static String CMND_STRG_PORT_STATISTICS = "show port-statistics";// show statistics port 1
	private final static String CMND_STRG_SHOW_SYSLOG = "show logging";//
	private final static String CMND_STRG_SHOW_VLAN = "show vlan";//
	private final static String CMND_STRG_SHOW_PORT = "show port";// port up/down 상태.
	private final static String CMND_STRG_LICENSE = "show warranty-license";
	private final static String CMND_STRG_SYSTEM = "show system";
	private final static String CMND_STRG_DUMP_CONFIG = "show running-config";
	private final static String CMND_STRG_SLB_DUMP = "show running-config slb";
	private final static String CMND_STRG_REAL_DUMP = "show running-config real";
	private final static String CMND_STRG_HEALTH_DUMP = "show running-config health-check";
	private final static String CMND_STRG_INTERFACE = "show interface";
	private final static String CMND_STRG_GATEWAY = "show route";
	private final static String CMND_STRG_RESOURCES = "show resource";
	private final static String CMND_STRG_SHOW_HOSTNAME = "show hostname";
	private final static String CMND_STRG_SHOW_NTP = "show ntp";
	private final static String CMND_STRG_SHOW_TRUNK = "show trunk";
	private final static String CMND_STRG_SHOW_STP = "show stp";
	private final static String CMND_STRG_SHOW_COMMUNITY = "show community";
	private final static String CMND_STRG_CFG = "configure";
//	private final static String		CMND_STRG_CFG_CURRENT	= "current";
	private final static String CMND_STRG_CFG_SNMP = "snmp";
	private final static String CMND_STRG_STATUS = "status %s";
	private final static String CMND_STRG_LOGGING = "logging";
	private final static String CMND_STRG_SERVER_STATUS = "server-status %s";
	private final static String CMND_STRG_SERVER = "server %s";
}
