package kr.openbase.adcsmart.service.impl.pas.handler;

import java.util.ArrayList;

import kr.openbase.adcsmart.service.dto.history.OBDtoAdcConfigChunkPAS;
import kr.openbase.adcsmart.service.dto.history.OBDtoAdcConfigPoolMemberPAS;
import kr.openbase.adcsmart.service.dto.history.OBDtoAdcConfigPoolPAS;
import kr.openbase.adcsmart.service.dto.history.OBDtoAdcConfigVServerPAS;
import kr.openbase.adcsmart.service.impl.OBSShCmndExec;
import kr.openbase.adcsmart.service.impl.OBTelnetCmndExecV2;
import kr.openbase.adcsmart.service.impl.OBVServerDB;
import kr.openbase.adcsmart.service.impl.pas.dto.OBDtoAdcHealthCheckPAS;
import kr.openbase.adcsmart.service.impl.pas.dto.OBDtoAdcPoolMemberPAS;
import kr.openbase.adcsmart.service.impl.pas.dto.OBDtoAdcPoolPAS;
import kr.openbase.adcsmart.service.impl.pas.dto.OBDtoAdcVServerPAS;
import kr.openbase.adcsmart.service.impl.pas.dto.OBDtoConfigResultPAS;
import kr.openbase.adcsmart.service.impl.pas.dto.OBDtoConfigResultSetPAS;
import kr.openbase.adcsmart.service.utility.OBCommon;
import kr.openbase.adcsmart.service.utility.OBDateTime;
import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.service.utility.OBExceptionLogin;
import kr.openbase.adcsmart.service.utility.OBExceptionUnreachable;
import kr.openbase.adcsmart.service.utility.OBParser;
import kr.openbase.adcsmart.service.utility.OBSystemLog;

public class OBAdcPASHandler {
	private OBTelnetCmndExecV2 telnet;
	private OBSShCmndExec ssh;
	private final String SUFFIX_LOGIN = "login: ";
	private final String SUFFIX_PASSWD = "Password: ";
	private final String SUFFIX_PROMPT_OK = "# ";
	private final int MAX_WAIT_TIME = 1000;// write memory 와 같은 경우 명령 수행 후 결과값 수신에 수초~수십초 소요된다. 이럴 경우에는 사용되는 timeout
											// 값이다. 100msec*1000 == 100sec
	private String user = "";
	private String password = "";
	private String server = "";
	private int connService = OBDefine.SERVICE.TELNET;
	private int connPort = OBDefine.SERVICE.TELNET;

	public OBAdcPASHandler() {
	}

	public OBAdcPASHandler(String server, String user, String password, int connService, int connPort) {
		this.setServer(server);
		this.setUser(user);
		this.setPassword(password);
		this.setConnService(connService);
		this.setConnPort(connPort);
	}

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

//	public OBTelnetCmndExecV2 getTelnet()
//	{
//		return telnet;
//	}
//	public void setTelnet(OBTelnetCmndExecV2 telnet)
//	{
//		this.telnet = telnet;
//	}
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

	// telnet 명령 실행
	private synchronized String sendCommand(String command) throws OBException {
		OBSystemLog.writePASCmnd(command, null);
		String out;
		if (this.telnet != null) {
			out = this.telnet.sendCommand(command, SUFFIX_PROMPT_OK);
		} else {
			out = this.ssh.sendCommand(command, SUFFIX_PROMPT_OK);
		}
		OBSystemLog.writePASCmnd(null, out);
		return out;
	}

	private synchronized String sendCommandTimeout(String command, int timeOut) throws OBException {
		OBSystemLog.writePASCmnd(command, null);
		String out;
		if (this.telnet != null) {
			out = this.telnet.sendCommandTimeout(command, SUFFIX_PROMPT_OK, timeOut);
		} else {
			out = this.ssh.sendCommandTimeout(command, SUFFIX_PROMPT_OK, timeOut);
		}
		OBSystemLog.writePASCmnd(null, out);
		return out;
	}

	private synchronized String sendCommand(String command, String prompt) throws OBException {
		OBSystemLog.writePASCmnd(command, null);
		String out;
		if (this.telnet != null) {
			out = this.telnet.sendCommand(command, prompt);
		} else {
			out = this.ssh.sendCommand(command, prompt);
		}
		OBSystemLog.writePASCmnd(null, out);
		return out;
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
		OBSystemLog.writePASCmnd(String.format("login:%s", this.server), "success");
	}

	private synchronized void sshLogin() throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		if (this.ssh == null) {
			this.ssh = new OBSShCmndExec();
		}
		this.ssh.setConnectionInfo(this.server, this.user, this.password, this.connPort);
		this.ssh.sshLogin();
		OBSystemLog.writePASCmnd(String.format("ssh login:%s", this.server), "success");
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

	private void beginConfig() throws OBException {
		String command = "config";
		String output = sendCommand(command);
		if (isResultOk(output) == false) {
			throw new OBException(OBException.ERRCODE_SEND_COMMAND, command);// , String.format("command:configure,
																				// message:%s", output));
		}
	}

	private void enterVirtualServer(String vsName) throws OBException {
		// virtual server 진입 -- slb VSNAME
		String command = String.format("slb %s", vsName);
		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "[config] " + command);
		String output = sendCommand(command);
		if (isResultOk(output) == false) {
			throw new OBException(OBException.ERRCODE_SEND_COMMAND, command);// , String.format("command:%s,
																				// message:%s", command, output));
		}
	}

	public void setVirtualServerIp(String vsIp) throws OBException {
		String command = String.format("vip %s", vsIp);
		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "[config] " + command);
		String output = sendCommand(command);
		if (isResultOk(output) == false) {
			throw new OBException(OBException.ERRCODE_SEND_COMMAND, command);// , String.format("command:%s,
																				// message:%s", command, output));
		}
	}

	private void setVirtualServerPort(int protocol, int port) throws OBException {
		String command = String.format("vport %s:%s", OBCommon.convertProtocolInteger2String(protocol), port);
		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "[config] " + command);
		String output = sendCommand(command);
		if (isResultOk(output) == false) {
			throw new OBException(OBException.ERRCODE_SEND_COMMAND, command);// , String.format("command:%s,
																				// message:%s", command, output));
		}
	}

	public void setLoadbalancing(int lbMethod) throws OBException {
		String command = String.format("lb-method %s", getLBMethodString(lbMethod));
		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "[config] " + command);
		String output = sendCommand(command);
		if (isResultOk(output) == false) {
			throw new OBException(OBException.ERRCODE_SEND_COMMAND, command);// , String.format("command:%s,
																				// message:%s", command, output));
		}
	}

	private void delVirtualServer(String vsName) throws OBException {
		String command = String.format("no slb %s", vsName);
		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "[config]" + command);
		// String output = sendCommand(command);
		String output = sendCommandTimeout(command, MAX_WAIT_TIME); // 오래 걸린다!!
		if (isResultOk(output) == false) {
			throw new OBException(OBException.ERRCODE_SEND_COMMAND, command);// , String.format("command:%s,
																				// message:%s", command, output));
		}
	}

	private void delHealthcheck(int healthcheckId) throws OBException {
		String command = String.format("no health %d", healthcheckId);
		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "[config]" + command);
		String output = sendCommand(command);
		if (isResultOk(output) == false) {
			throw new OBException(OBException.ERRCODE_SEND_COMMAND, command);// , String.format("command:%s,
																				// message:%s", command, output));
		}
	}

	private void enterHealthcheck(int healthId) throws OBException {
		String command = String.format("health %d", healthId);
		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "[config]" + command);
		String output = sendCommand(command);
		if (isResultOk(output) == false) {
			throw new OBException(OBException.ERRCODE_SEND_COMMAND, command);// , String.format("command:%s,
																				// message:%s", command, output));
		}
	}

	public void setHealthcheckType(int healthcheckType) throws OBException {
		String command = String.format("type %s", OBCommon.convertHealthCheckTypeInteger2String(healthcheckType));
		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "[config] " + command);
		String output = sendCommand(command);
		if (isResultOk(output) == false) {
			throw new OBException(OBException.ERRCODE_SEND_COMMAND, command);// , String.format("command:%s,
																				// message:%s", command, output));
		}
	}

	private void delRealServer(int realServerId) throws OBException { // 기존 real을 제거, apply가 필요없고 경로이동을 하지 않는다.
		String command = String.format("no real %d", realServerId);
		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "[config] " + command);
		String output = sendCommand(command);
		if (isResultOk(output) == false) {
			throw new OBException(OBException.ERRCODE_SEND_COMMAND, command);// , String.format("command:%s,
																				// message:%s", command, output));
		}
	}

	private void enterRealServer(int realServerId) throws OBException {
		String command = String.format("real %d", realServerId);
		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "[config] " + command);
		String output = sendCommand(command);
		if (isResultOk(output) == false) {
			throw new OBException(OBException.ERRCODE_SEND_COMMAND, command);// , String.format("command:%s,
																				// message:%s", command, output));
		}
	}

	public void setRealServerDefaultName(int realServerId) throws OBException {
		String command = String.format("name real%d", realServerId);
		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "[config] " + command);
		String output = sendCommand(command);
		if (isResultOk(output) == false) {
			throw new OBException(OBException.ERRCODE_SEND_COMMAND, command);// , String.format("command:%s,
																				// message:%s", command, output));
		}
	}

	public void setRealServerIp(String realServerIp) throws OBException {
		String command = String.format("rip %s", realServerIp);
		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "[config] " + command);
		String output = sendCommand(command);
		if (isResultOk(output) == false) {
			throw new OBException(OBException.ERRCODE_SEND_COMMAND, command);// , String.format("command:%s,
																				// message:%s", command, output));
		}
	}

	public void setRealServerPort(int realServerPort) throws OBException {
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
			command = "enable";
		} else {
			command = "disable";
		}
		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "[config] " + command);
		String output = sendCommand(command);
		if (isResultOk(output) == false) {
			throw new OBException(OBException.ERRCODE_SEND_COMMAND, command);// , String.format("command:%s,
																				// message:%s", command, output));
		}
	}

	private void apply() throws OBException {
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
		String output = sendCommand(command);
		if (isResultOk(output) == false) {
			throw new OBException(OBException.ERRCODE_SEND_COMMAND, command);// , String.format("command:%s,
																				// message:%s", command, output));
		}
	}

	private void cdHome() throws OBException {
		String command = "/";
		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "[config] " + command);
		String output = sendCommand(command);
		if (isResultOk(output) == false) {
			throw new OBException(OBException.ERRCODE_SEND_COMMAND, command);// , String.format("command:%s,
																				// message:%s", command, output));
		}
	}

	public void writeMemory() throws OBException {
		String command = "write memory";
		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "[config] " + command + "start");
		String output = sendCommandTimeout("write memory", MAX_WAIT_TIME);
		// String output = sendCommand("write memory");
		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "[config] " + command + "done1");
		if (isResultOk(output) == false) {
			throw new OBException(OBException.ERRCODE_SEND_COMMAND, command);// , String.format("command:%s,
																				// message:%s", command, output));
		}
		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "[config] " + command + "done2");
	}

	public OBDtoConfigResultPAS configSlb(OBDtoAdcConfigChunkPAS configChunk) throws OBException {
		// vsConfig 중 다음 두 항목은 이 루틴에서 처리하지 않는다.
		// 1. vsConfig.getPoolChange(): pool이 바뀌는 것을 표시하는데, PAS는 pool을 바꾸지 않는다.
		// 2. vsConfig.getStateChange(): enable/disable은 별도 루틴으로 처리한다.
		OBDtoAdcConfigVServerPAS vsConfig = configChunk.getVsConfig();

		OBDtoAdcConfigPoolPAS poolConfig = configChunk.getVsConfig().getPoolConfig();
		OBDtoAdcVServerPAS vsNew = configChunk.getVsConfig().getVsNew();
		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "vsNew == " + vsNew);
		OBDtoAdcVServerPAS vsOld = configChunk.getVsConfig().getVsOld();

		OBDtoAdcHealthCheckPAS healthOld;
		OBDtoAdcHealthCheckPAS healthNew = vsNew.getPool().getHealthCheckInfo();

		//
		boolean applyVs = false;
		boolean applyHealth = false;
		ArrayList<Integer> applyRealList = new ArrayList<Integer>();

		// 결과값 초기화
		OBDtoConfigResultPAS result = new OBDtoConfigResultPAS();
		result.setChange(0);
		result.setWriteMemory(false);

		boolean tempChange = false; // apply할 것이 있는지 확인하는 임시변수

		if (vsOld != null) // vs 삭제, or vs
		{
			healthOld = vsOld.getPool().getHealthCheckInfo();
		} else {
			healthOld = null;
		}
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
			return result;
		}

		// config 시작 !!!! 여기부터 exception 처리 주의
		// ADC에 적용되므로, 적용시점마다 적용내역을 기록해놓고, exception을 잡으면 처리 한다.
		try {
			beginConfig(); // ADC의 config 모드 개시, 오류시 OBException

			// virtual server 진입 -- slb VSNAME
			enterVirtualServer(vsNew.getName());

			// virtual server ip -- vip 1.1.1.1 //기존 vs의 IP도 바꿀 수 있으므로 명령을 열어놓는다.
			if (vsConfig.getIpChange() == OBDefine.CHANGE_TYPE_EDIT) {
				setVirtualServerIp(vsNew.getvIP());
				tempChange = true;
			}
			// virtual server port -- vport tcp:80
			if (vsConfig.getPortChange() == OBDefine.CHANGE_TYPE_EDIT
					|| vsConfig.getProtocolChange() == OBDefine.CHANGE_TYPE_EDIT) {
				setVirtualServerPort(vsNew.getSrvProtocol(), vsNew.getSrvPort());
				tempChange = true;
			}
			// lb-method -- lb-method rr ("rr" or "lc" or "hash")
			if (poolConfig.getLbMethodChange() == OBDefine.CHANGE_TYPE_EDIT) {
				setLoadbalancing(vsNew.getPool().getLbMethod());
				tempChange = true;
			}

			// 처리 안하는 부분 설명 : virtual server state:수정에서 state는 건드리지 않는다. SKIP!

			if (tempChange == true) // 1st apply - vs에 바뀐 것이 있으면 적용한다. 여기서 해야한다. 경로가 바뀌지 않으므로 경로 보정은 안 한다.
			{
				apply();
				applyVs = true; // vs level apply가 됐으면 바뀐 상태를 mark한다.
			}

			if (poolConfig.getHealthCheckChange() == OBDefine.CHANGE_TYPE_DELETE) {
				delHealthcheck(healthOld.getId());
				// no health는 apply가 필요없다.
				applyHealth = true; // healthcheck level apply를 했으므로 상태를 표시한다.
			} else if (poolConfig.getHealthCheckChange() != OBDefine.CHANGE_TYPE_NONE) // add or edit
			{
				int healthId = 0; // health id#: 1~16)
				ArrayList<Integer> availableIdList = vsDb.getAvailableHealthCheckIndexPAS(vsNew.getDbIndex());
				// 새 health이거나 health check type이 바뀌었으면
				if (poolConfig.getHealthCheckChange() == OBDefine.CHANGE_TYPE_ADD) {
					healthId = availableIdList.get(0); // 빈 health id 중 최소값 지정한다.
					healthNew.setId(healthId);
				} else if (poolConfig.getHealthCheckChange() == OBDefine.CHANGE_TYPE_EDIT
						&& healthOld.getType().equals(healthNew.getType()) == false) {
					healthId = healthNew.getId();
				} else {
					healthId = 0; // do nothing
				}

				if (healthId != 0) {
					enterHealthcheck(healthId);
					setHealthcheckType(vsNew.getPool().getHealthCheckInfo().getType());
					apply(); // 2nd apply - health check 적용 해야한다. 경로가 상위로 바뀐다. vs level로
					applyHealth = true; // healthcheck level apply를 했으므로 상태를 표시한다.
					OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "[config]health apply");
				}

				// health check 아래 항목들은 수정에서 지금은 건드리지 않으므로 막는다.
				// health check port
				// health check timeout -- timeout # (#: 1~10)
				// health check interval -- interval # (#:1~60)
				// health check state enable/disable
			}

			if (poolConfig.getMemberChange() == OBDefine.CHANGE_TYPE_EDIT) {
				int i = 0, rid = 0, ridIndex = 0;
				ArrayList<OBDtoAdcConfigPoolMemberPAS> changeList = poolConfig.getMemberConfigList();
				OBDtoAdcConfigPoolMemberPAS real = null;
				int changeCount = changeList.size();
				// 변경 멤버 개수만큼 available real id를 구해온다. 모두 쓰지 않을 수 있지만 최대 개수를 준비한다.
				ArrayList<Integer> availableRidList = vsDb.getAvailableRealServerIndexPAS(vsNew.getDbIndex(),
						changeCount);

				// test code: 설정 중간 종료시 부분 적용하는 기능을 테스트 할 때 open한다.
//				if(poolConfig.getHealthCheckChange()==OBDefine.CHANGE_TYPE_ADD || poolConfig.getHealthCheckChange()==OBDefine.CHANGE_TYPE_EDIT)
//				{
//					throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "Intentional exception");
//				}

				for (i = 0; i < changeCount; i++) {
					real = changeList.get(i);
					if (real.getChange() == OBDefine.CHANGE_TYPE_DELETE) {
						// 기존 real을 제거, old의 real id로 지운다. apply가 필요없고 경로이동을 하지 않는다.
						delRealServer(real.getMemberOld().getId());
						applyRealList.add(i); // index i인 member change까지 적용했다.
					} else if (real.getChange() == OBDefine.CHANGE_TYPE_ADD) {
						// 추가면 available minimum id를 구한다.
						rid = availableRidList.get(ridIndex); // 빈 real id 중 최소값을 구한다.
						// real 설정 진입, 앞에 no real 실행을 했어도 경로 이동이 없어서 새 real로 작업할 수 있다.
						enterRealServer(rid);

						// vsResult에 추가 준비. 결정된 rid를 changeList에 넣는다.
						real.getMemberNew().setId(rid);

						// 추가면 rid로 만든 기본이름을 넣어준다. "real1", "real2", "real3"...
						setRealServerDefaultName(rid);
						// real server ipaddress
						setRealServerIp(real.getMemberNew().getIpAddress());
						// real server port
						setRealServerPort(real.getMemberNew().getPort());
						// real state: 추가할 때는 기본 enable 이므로, disable이면 인위적으로 설정하러 간다.
						if (real.getMemberNew().getState().equals(OBDefine.STATUS_DISABLE)) {
							setState(real.getMemberNew().getState());
						}
						// real 저장, 상위로 이동함
						apply();
						applyRealList.add(i); // apply 했으므로 변경 목록에 반영(추가)한다.

						ridIndex++;
					} else if (real.getChange() == OBDefine.CHANGE_TYPE_EDIT) {
						tempChange = false;
						rid = real.getMemberNew().getId(); // 수정일 때는 쓰던 id
						// real 설정 진입, 앞에 no real 실행을 했어도 경로 이동이 없어서 새 real로 작업할 수 있다.
						enterRealServer(rid);

						// name명령 건너뜀: 수정(set)일 때는 "name" 명령을 쓸 수 없다. 즉 이미 있는 이름을 고칠 수 없다.

						// rip, rport
						if (real.getMemberNew().getIpAddress().equals(real.getMemberOld().getIpAddress()) == false) {
							// real server ipaddress
							setRealServerIp(real.getMemberNew().getIpAddress());
							tempChange = true;
						}
						if (real.getMemberNew().getPort().equals(real.getMemberOld().getPort()) == false) {
							// real server port
							setRealServerPort(real.getMemberNew().getPort());
							tempChange = true;
						}

						// real state
						if (real.getMemberNew().getState().equals(real.getMemberOld().getPort()) == false) {
							setState(real.getMemberNew().getState());
							tempChange = true;
						}

						if (tempChange == true) {
							apply(); // real apply, 상위로 exit
							applyRealList.add(i); // apply 했으므로 변경 목록에 반영(추가)한다.
							OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "[config]real apply");
						}
						// else //바뀐게 없어서 apply를 안 한다. 상위로 경로가 이동하지 않는다.
					}
				}
			}
			result.setChange(2);
			result.setVirtualServer(vsNew); // 설정 완전 변경, 중간에 중단되지 않았음. 편집한 vs가 아닌 원래 new로 한다. 그대로 모두 적용했으므로. write
											// memory 아직 안함
			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "[config]완전 설정");
		} catch (Exception e) // catch(OBException e)
		{
			if (vsConfig.getChange() == OBDefine.CHANGE_TYPE_ADD) {
				result.setVirtualServer(new OBDtoAdcVServerPAS()); // 빈 vs에서
			} else // edit 수정
			{
				result.setVirtualServer(vsOld); // 기존 vs에서
			}
			OBDtoAdcVServerPAS vs = result.getVirtualServer(); // 적용된 데 까지의 실제 VS 상태 apply할 때 마다 vsResultTemp에서 반영한다.

			if (applyVs == true) {
				vs.setAdcIndex(vsNew.getAdcIndex());
				vs.setApplyTime(vsNew.getApplyTime());
				vs.setDbIndex(vsNew.getDbIndex());
				vs.setName(vsNew.getName());
				vs.setSrvPort(vsNew.getSrvPort());
				vs.setSrvProtocol(vsNew.getSrvProtocol());
				vs.setState(vsNew.getState());
				vs.setStatus(vsNew.getStatus());
				vs.setvIP(vsNew.getvIP());
				vs.setPool(new OBDtoAdcPoolPAS());
				vs.getPool().setDbIndex(vsNew.getPool().getDbIndex());
				vs.getPool().setName(vsNew.getPool().getName());
				vs.getPool().setLbMethod(vsNew.getPool().getLbMethod()); // 여기까지 apply했다.
				vs.getPool().setHealthCheckInfo(null); // 아직 apply 안 했다.
				vs.getPool().setMemberList(new ArrayList<OBDtoAdcPoolMemberPAS>()); // 아직 구성 안 했다.
			}
			if (applyHealth == true) {
				vs.getPool().setHealthCheckInfo(new OBDtoAdcHealthCheckPAS(healthNew)); // 추가/수정/삭제로 바뀐 결과를 반영한다.
			}
			if (applyRealList.size() > 0) {
				ArrayList<OBDtoAdcPoolMemberPAS> memberList = vs.getPool().getMemberList(); // vs 추가든 수정이든 앞에서 처리됏으므로
																							// 문제없다. 추가면 empty list
				ArrayList<OBDtoAdcConfigPoolMemberPAS> changeList = poolConfig.getMemberConfigList();

				for (int index : applyRealList) {
					OBDtoAdcConfigPoolMemberPAS change = changeList.get(index);
					if (change.getChange() == OBDefine.CHANGE_TYPE_ADD) {
						memberList.add(change.getMemberNew());
					} else if (change.getChange() == OBDefine.CHANGE_TYPE_DELETE) {
						for (OBDtoAdcPoolMemberPAS member : memberList) {
							if (member.getId().equals(change.getMemberNew().getId())) {
								memberList.remove(member);
								break;
							}
						}
					} else if (change.getChange() == OBDefine.CHANGE_TYPE_EDIT) {
						for (OBDtoAdcPoolMemberPAS member : memberList) {
							if (member.getId().equals(change.getMemberNew().getId())) {
								memberList.remove(member); // 있던 것을 지우고
								memberList.add(change.getMemberNew()); // 새것을 넣는다.
								break;
							}
						}
					}
				}
			}
			result.setChange(1); // 설정 불완전 변경, write memory 못함
			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "[config]불완전 설정" + e.getMessage());
		}

		try {
			if (result.getChange() != 0) // 변경이 잇으면 write memory 한다.
			{
				cdHome(); // write memory를 할 수 잇는 경로인 config main으로 간다.
				OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "[config]write memory start");
				writeMemory();
				result.setWriteMemory(true);
				OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "[config]write memory end");
			}
		} catch (OBException e) {
			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "[config]write memory 실패 종료");
			return result; // write memory를 못 한 상태로 종료
		}
		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "[config]write memory 성공 종료");
		return result; // write memory 하고 종료. 설정 이행은 불완전 할 수 있음
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
			apply(); // 적용후 virtual server --> configure로 이동
			exit();
		}
		writeMemory();
	}

	public OBDtoConfigResultSetPAS delVirtualServer(ArrayList<OBDtoAdcConfigChunkPAS> configChunkList)
			throws Exception {
		beginConfig();

		OBDtoConfigResultSetPAS resultSet = new OBDtoConfigResultSetPAS();
		resultSet.setChange(0);
		resultSet.setWriteMemory(false);
		resultSet.setWriteHistory(false);
		resultSet.setWriteConfig(false);

		ArrayList<OBDtoConfigResultPAS> resultList = new ArrayList<OBDtoConfigResultPAS>();
		int i = 0;
		int listSize = configChunkList.size();
		try {
			for (i = 0; i < listSize; i++) {
				OBDtoAdcVServerPAS virtualServer = new OBDtoAdcVServerPAS(
						configChunkList.get(i).getVsConfig().getVsOld());
				// 결과값 초기화
				OBDtoConfigResultPAS result = new OBDtoConfigResultPAS();
				result.setChange(0);
				result.setWriteConfig(false);
				result.setWriteHistory(false);
				result.setWriteMemory(false); // 개별로 기록하지 않는다.
				result.setVirtualServer(virtualServer);
				result.setChange(2); // 삭제는 한 단계만 있으므로 완전 설정
				resultList.add(result); // 삭제를 완수하면 result로 인정한다. 일단 명령을 던지면 응답이 늦게 오더라도 성공하므로 성공한 명령으로 본다.
				delVirtualServer(virtualServer.getName()); // 이름으로 지움. apply 필요없음
//				if(i==4) //지우지 말 것, 중간 실패 테스트 코드
//				{
//					throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "Intentional exception");
//				}
			}
			resultSet.setChange(2); // loop을 다 돌았으므로 완전 설정
		} catch (Exception e) // catch(OBException e)
		{
			resultSet.setChange(2); // 불완전 설정
			// 중요!! 삭제하다 오류 났어도 exception을 던지지 않는다.
		}

		try {
			if (resultList.size() > 0) {
				// cdHome(); // write memory를 할 수 잇는 경로인 config main으로 간다.
				OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "[config]write memory start");
				writeMemory();
				resultSet.setWriteMemory(true); // 모든 entry가 write 성공함
				OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "[config]write memory 성공 종료");
			}
		} catch (OBException e) {
			resultSet.setWriteMemory(false); // 모든 entry가 write 성공함
			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "[config]write memory 실패 종료");
		}

		resultSet.setResultList(resultList);
		return resultSet;
	}

	// Load Balancing Method
	private String getLBMethodString(Integer lbMethod) throws OBException {
		if (lbMethod == null) {
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
					String.format("Error: Loadbalancing method is null."));
		} else if (lbMethod == OBDefine.LB_METHOD_ROUND_ROBIN) {
			return "rr";
		} else if (lbMethod == OBDefine.LB_METHOD_LEAST_CONNECTION_MEMBER) {
			return "lc";
		} else if (lbMethod == OBDefine.LB_METHOD_HASH) {
			return "hash";
		} else {
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
					String.format("Error: Loadbalancing method value is impossible.(%d)", lbMethod));
		}
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

	public String cmndDumpcfg() throws OBException {
		return OBParser.trimAndSimplifySpaces(command(OBAdcPASHandler.CMND_STRG_SHOW_SLB));

//		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("OBAdcAlteon:cmndDumpcfg(%s). start.", this.server));
////		cmndEnter();
//		String command = "show running-config slb";
////		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "cmd = " + command);
//		String out = sendCommand(command, "#");
//		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("OBAdcAlteon:cmndDumpcfg(%s). end:%s.", this.server, out));
//		return out;
	}

	private String command(String cmndString) throws OBException {
		OBSystemLog.writePASCmnd(cmndString, null);
		String out = sendCommand(cmndString, SUFFIX_PROMPT_OK);
		OBSystemLog.writePASCmnd(null, out);
		return out;
	}

//	public static void main(String [] args) 
//	{
//		try
//		{
//			OBAdcPAS handler = new OBAdcPAS("192.168.200.120", "root", "admin");
//			//handler.enter();
//			handler.telnetLogin();
//			String cfg = handler.cmndLicense();
//			handler.telnet.disconnect();
//			OBDtoLicenseInfoPAS info = new OBCLIParserPAS().parseLicense(cfg);
//			System.out.println(info);
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
//			OBAdcPAS handler = new OBAdcPAS("192.168.200.120", "root", "admin");
//			//handler.enter();
//			handler.login();
//			String cfg = handler.cmndSystem();
//			handler.disconnect();
//			OBDtoSystemInfoPAS info = new OBCLIParserPAS().parseSystem(cfg);
//			System.out.println(info);
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
		return OBParser.trimAndSimplifySpaces(command(OBAdcPASHandler.CMND_STRG_SYSTEM));
	}

//	public static void main(String [] args) 
//	{
//		try
//		{
//			OBAdcPAS handler = new OBAdcPAS("192.168.200.120", "root", "admin");
//			//handler.enter();
//			handler.telnetLogin();
//			String cfg = handler.cmndInterface();
//			handler.telnet.disconnect();
//			ArrayList<OBDtoInterfaceInfoPAS> info = new OBCLIParserPAS().parseInterface(cfg);
//			System.out.println(info);
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}
	public String cmndInterface() throws OBException {
		return OBParser.trimAndSimplifySpaces(command(OBAdcPASHandler.CMND_STRG_INTERFACE));
	}

//	public static void main(String [] args) 
//	{
//		try
//		{
//			OBAdcPAS handler = new OBAdcPAS("192.168.200.120", "root", "admin");
//			//handler.enter();
//			handler.login();
//			String cfg = handler.cmndGateway();
//			handler.disconnect();
//			ArrayList<OBDtoGatewayInfoPAS> info = new OBCLIParserPAS().parseGateway(cfg);
//			System.out.println(info);
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}
	public String cmndGateway() throws OBException {
		return OBParser.trimAndSimplifySpaces(command(OBAdcPASHandler.CMND_STRG_GATEWAY));
	}

//	public static void main(String [] args) 
//	{
//		try
//		{
//			OBAdcPAS handler = new OBAdcPAS("192.168.200.120", "root", "admin");
//			//handler.enter();
//			handler.login();
//			String cfg = handler.cmndResources();
//			handler.disconnect();
//			OBDtoResourceInfoPAS info = new OBCLIParserPAS().parseResources(cfg);
//			System.out.println(info);
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}
	public String cmndResources() throws OBException {
		return OBParser.trimAndSimplifySpaces(command(OBAdcPASHandler.CMND_STRG_RESOURCES));
	}

//	public static void main(String [] args) 
//	{
//		try
//		{
//			OBAdcPAS handler = new OBAdcPAS("192.168.200.120", "root", "admin");
//			//handler.enter();
//			handler.login();
//			String cfg = handler.cmndPortInfo();
//			handler.disconnect();
//			ArrayList<OBDtoPortInfoPAS> info = new OBCLIParserPAS().parsePortUpdown(cfg);
//			System.out.println(info);
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}
	public String cmndPortInfo() throws OBException {
		return OBParser.trimAndSimplifySpaces(command(OBAdcPASHandler.CMND_STRG_SHOW_PORT));
	}

//	public static void main(String [] args) 
//	{
//		try
//		{
//			OBAdcPAS handler = new OBAdcPAS("192.168.200.120", "root", "admin");
//			//handler.enter();
//			handler.login();
//			String cfg = handler.cmndVlanInfo();
//			handler.disconnect();
//			ArrayList<OBDtoVLanInfoPAS> info = new OBCLIParserPAS().parseVlanInfo(cfg);
//			System.out.println(info);
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}
	public String cmndVlanInfo() throws OBException {
		return OBParser.trimAndSimplifySpaces(command(OBAdcPASHandler.CMND_STRG_SHOW_VLAN));
	}

//	public static void main(String [] args) 
//	{
//		try
//		{
//			OBAdcPASHandler handler = new OBAdcPASHandler("192.168.200.120", "root", "admin", OBDefine.SERVICE.TELNET, OBDefine.SERVICE.TELNET);
//			//handler.enter();
//			handler.login();
//			String cfg = handler.cmndSyslogInfo();
//			System.out.println(cfg);
//			handler.cmndDumpcfg();
//			handler.cmndSnmpInfo();
//			handler.cmndLoggingBuffer();
//			handler.disconnect();
////			ArrayList<OBDtoSyslogInfoPAS> info = new OBCLIParserPAS().parseSyslogInfo(cfg);
////			System.out.println(info);
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}
	public String cmndSyslogInfo() throws OBException {
		return OBParser.trimAndSimplifySpaces(command(OBAdcPASHandler.CMND_STRG_SHOW_SYSLOG));
	}

//	public static void main(String [] args) 
//	{
//		try
//		{
//			OBAdcPAS handler = new OBAdcPAS("192.168.200.120", "root", "admin");
//			//handler.enter();
//			handler.login();
//			String cfg = handler.cmndNTPInfo();
//			handler.disconnect();
//			OBDtoNTPInfoPAS info = new OBCLIParserPAS().parseNTPInfo(cfg);
//			System.out.println(info);
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}
	public String cmndNTPInfo() throws OBException {
		String retVal = "";
		OBDateTime.Sleep(500);
		String output = command(OBAdcPASHandler.CMND_STRG_CONFIG);
		if (isResultOk(output) == false) {
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
					String.format("parsing error(command:%s, result:%s)", OBAdcPASHandler.CMND_STRG_CONFIG, output));
		}

		OBDateTime.Sleep(500);
		output = command(OBAdcPASHandler.CMND_STRG_CFG_NTP);
		if (isResultOk(output) == false) {
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
					String.format("parsing error(command:%s, result:%s)", OBAdcPASHandler.CMND_STRG_CFG_NTP, output));
		}
		OBDateTime.Sleep(500);
		retVal = OBParser.trimAndSimplifySpaces(command(OBAdcPASHandler.CMND_STRG_CFG_CURRENT));

		OBDateTime.Sleep(500);
		output = command(OBAdcPASHandler.CMND_STRG_CFG_EXIT);
		if (isResultOk(output) == false) {
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
					String.format("parsing error(command:%s, result:%s)", OBAdcPASHandler.CMND_STRG_CFG_EXIT, output));
		}
		OBDateTime.Sleep(500);
		output = command(OBAdcPASHandler.CMND_STRG_CFG_EXIT);// exit 는 두번 수행 필요..
		if (isResultOk(output) == false) {
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
					String.format("parsing error(command:%s, result:%s)", OBAdcPASHandler.CMND_STRG_CFG_EXIT, output));
		}
		return retVal;
	}

//	public static void main(String [] args) 
//	{
//		try
//		{
//			OBAdcPAS handler = new OBAdcPAS("192.168.200.120", "root", "admin");
//			//handler.enter();
//			handler.login();
//			String cfg = handler.cmndPortStatistics("1");
//			handler.disconnect();
//			OBDtoPortStatPAS info = new OBCLIParserPAS().parsePortStatistics(cfg);
//			System.out.println(info);
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}
	public String cmndPortStatistics(String portName) throws OBException {
		return OBParser.trimAndSimplifySpaces(command(OBAdcPASHandler.CMND_STRG_PORT_STATISTICS + " " + portName));
	}

//	public static void main(String [] args) 
//	{
//		try
//		{
//			OBAdcPAS handler = new OBAdcPAS("192.168.200.120", "root", "admin");
//			//handler.enter();
//			handler.login();
//			String cfg = handler.cmndHwStatistics();
//			handler.disconnect();
//			OBDtoHWStatPAS info = new OBCLIParserPAS().parseHWStatistics(cfg);
//			System.out.println(info);
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}
	public String cmndHwStatistics() throws OBException {
		return OBParser.trimAndSimplifySpaces(command(OBAdcPASHandler.CMND_STRG_HW_STATISTICS));
	}

//	public static void main(String [] args) 
//	{
//		try
//		{
//			OBAdcPAS handler = new OBAdcPAS("192.168.200.120", "root", "admin");
//			//handler.enter();
//			handler.login();
//			String cfg = handler.cmndSnmpInfo();
//			handler.disconnect();
//			OBDtoSnmpInfoPAS info = new OBCLIParserPAS().parseSnmpInfo(cfg);
//			System.out.println(info);
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}
	public String cmndSnmpInfo() throws OBException {
		return OBParser.trimAndSimplifySpaces(command(OBAdcPASHandler.CMND_STRG_SHOW_SNMP));
	}

//	public static void main(String [] args) 
//	{
//		try
//		{
//			OBAdcPAS handler = new OBAdcPAS("192.168.200.120", "root", "admin");
//			//handler.enter();
//			handler.login();
//			String cfg = handler.cmndSlbStatus();
//			handler.disconnect();
//			ArrayList<OBDtoAdcVServerPAS> info = new OBCLIParserPAS().parseSlbStatus(cfg);
//			System.out.println(info);
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}
	public String cmndSlbStatus() throws OBException {
		return OBParser.trimAndSimplifySpaces(command(OBAdcPASHandler.CMND_STRG_SHOW_SLBINFO));
	}

//	public static void main(String [] args) 
//	{
//		try
//		{
//			OBAdcPAS handler = new OBAdcPAS("192.168.200.120", "root", "admin");
//			//handler.enter();
//			handler.login();
//			String cfg = handler.cmndLoggingBuffer();
//			handler.disconnect();
//			ArrayList<OBDtoLoggingBufferPAS> info = new OBCLIParserPAS().parseLoggingBuffer(cfg);
//			System.out.println(info);
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}
	public String cmndLoggingBuffer() throws OBException {
		return OBParser.trimAndSimplifySpaces(command(OBAdcPASHandler.CMND_STRG_LOGGINGBUFFER));
	}

	public String cmndSyslogStatus(String config) throws OBException {
		String retVal = "";
		OBDateTime.Sleep(500);
		String output = command(OBAdcPASHandler.CMND_STRG_CONFIG);
		if (isResultOk(output) == false) {
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
					String.format("parsing error(command:%s, result:%s)", OBAdcPASHandler.CMND_STRG_CONFIG, output));
		}

		OBDateTime.Sleep(500);
		String cmnnd = String.format(OBAdcPASHandler.CMND_STRG_LOGGING_SERVER, config);
		retVal = OBParser.trimAndSimplifySpaces(command(cmnnd));

		return retVal;
	}

	public String cmndSnmpStatus(String config) throws OBException {
		String retVal = "";
		OBDateTime.Sleep(500);
		String output = command(OBAdcPASHandler.CMND_STRG_CONFIG);
		if (isResultOk(output) == false) {
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
					String.format("parsing error(command:%s, result:%s)", OBAdcPASHandler.CMND_STRG_CONFIG, output));
		}

		OBDateTime.Sleep(500);
		String cmnnd = String.format(OBAdcPASHandler.CMND_STRG_SNMP, config);
		retVal = OBParser.trimAndSimplifySpaces(command(cmnnd));

		return retVal;
	}

	private final static String CMND_STRG_LOGGINGBUFFER = "show logging buffer";// show statistics port 1
	private final static String CMND_STRG_SHOW_SLBINFO = "show info slb";// show statistics port 1
	private final static String CMND_STRG_SHOW_SNMP = "show snmp";// show statistics port 1
	private final static String CMND_STRG_HW_STATISTICS = "show hardware-status";// show statistics port 1
	private final static String CMND_STRG_PORT_STATISTICS = "show statistics port";// show statistics port 1
	private final static String CMND_STRG_CFG_EXIT = "exit";//
	private final static String CMND_STRG_CFG_CURRENT = "current";//
	private final static String CMND_STRG_CFG_NTP = "ntp";//
	private final static String CMND_STRG_CONFIG = "configure";//
	private final static String CMND_STRG_SHOW_SYSLOG = "show logging";//
	private final static String CMND_STRG_SHOW_VLAN = "show vlan";//
	private final static String CMND_STRG_SHOW_PORT = "show port";// port up/down 상태.
	private final static String CMND_STRG_SYSTEM = "show system";
	private final static String CMND_STRG_SHOW_SLB = "show running-config slb";
	private final static String CMND_STRG_INTERFACE = "show ip interface";
	private final static String CMND_STRG_GATEWAY = "show ip route";
	private final static String CMND_STRG_RESOURCES = "show resources";
	private final static String CMND_STRG_LOGGING_SERVER = "logging server %s";//
	private final static String CMND_STRG_SNMP = "snmp %s";//
}
