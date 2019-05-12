package kr.openbase.adcsmart.service.impl.alteon.handler;

import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;

import kr.openbase.adcsmart.service.dto.OBDtoAdcNodeAlteon;
import kr.openbase.adcsmart.service.dto.OBDtoAdcPoolAlteonChanged;
import kr.openbase.adcsmart.service.dto.OBDtoAdcPoolMemberAlteon;
import kr.openbase.adcsmart.service.dto.OBDtoAdcPoolMemberAlteonChanged;
import kr.openbase.adcsmart.service.dto.OBDtoAdcVServerAlteon;
import kr.openbase.adcsmart.service.dto.OBDtoAdcVService;
import kr.openbase.adcsmart.service.dto.OBDtoAdcVServiceChanged;
import kr.openbase.adcsmart.service.dto.OBDtoVrrpInfo;
import kr.openbase.adcsmart.service.impl.alteon.handler.dto.OBDtoAlteonVrrp;
import kr.openbase.adcsmart.service.impl.alteon.vdirect.OBVdirectServiceImpl;
import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.service.utility.OBParser;
import kr.openbase.adcsmart.service.utility.OBSystemLog;
import kr.openbase.adcsmart.service.vdirect.dto.ConfigSlbDto;
import kr.openbase.adcsmart.service.vdirect.dto.ServerEnableDisableDto;

public class OBAdcAlteonV30 extends OBAdcAlteonHandler {
	static final int HTTP_PORT = 80;
	static final int HTTPS_PORT = 443;

	final String SUFFIX_ERROR = "Error: ";

//	public static void main(String[] args)
//	{
//		OBAdcAlteonV30 alteon = new OBAdcAlteonV30();
//		alteon.setConnectionInfo("192.168.100.11", "admin", "admin", OBDefine.SERVICE.TELNET, OBDefine.SERVICE.TELNET);
//		try
//		{
//			alteon.login();
//			
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
//		catch(OBException e)
//		{
//			
//		}
//	}

	public void addVirtualServer(String vsID, String vsName, String vIP, Integer useYN, String ipaddress)
			throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start(%s). vsID:%s. vsName:%s, vIP:%s, useYN:%d",
				this.serverName, vsID, vsName, vIP, useYN));
		String output;

		ConfigSlbDto service = new ConfigSlbDto();

		service.setAlteonName(ipaddress);
		service.setVirtualServerID(vsID);
		if ((vsName != null) && (!vsName.isEmpty())) {
			service.setVirtualServerName(vsName);
		} else {
			service.setVirtualServerName("none");
		}
		service.setVirtualServerIp(vIP);

		output = new OBVdirectServiceImpl().createVirtualServer(service);
		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("host:%s, command:%s", this.serverName, service));
		if (!output.equals("200")) {
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
					String.format("parsing error(virtual Server create, result:%s)", SUFFIX_ERROR));
		}

		if (useYN != null) {
			ServerEnableDisableDto state = new ServerEnableDisableDto();
			state.setAlteonName(ipaddress);

			ArrayList<String> virtualServerID = new ArrayList<String>();
			virtualServerID.add(vsID);
			String virtualServerState = OBParser.getstate(useYN);
			state.setState(virtualServerState);

			output = new OBVdirectServiceImpl().createVirtualServer(service);
			if (!output.equals("200")) {
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						String.format("parsing error(ip : %s, vsID : %s, virtual Server state failed : %s, result:%s)",
								ipaddress, vsID, virtualServerState, SUFFIX_ERROR));
			}
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end"));
	}

	public void setVirtualServer(String vsID, String vsName, String vIP, Integer useYN, String ipaddress)
			throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start(%s). vsID:%s. vsName:%s, vIP:%s, useYN:%d",
				this.serverName, vsID, vsName, vIP, useYN));
		String output;

		ConfigSlbDto service = new ConfigSlbDto();

		service.setAlteonName(ipaddress);
		service.setVirtualServerID(vsID);
		service.setVirtualServerIp(vIP);
		if ((vsName != null) && (!vsName.isEmpty())) {
			service.setVirtualServerName(vsName);
		} else {
			service.setVirtualServerName("none");
		}

		output = new OBVdirectServiceImpl().createVirtualServer(service);
		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("host:%s, command:%s", this.serverName, service));
		if (!output.equals("200")) {
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
					String.format("parsing error(virtual Server create, result:%s)", SUFFIX_ERROR));
		}

		if (useYN != null) {
			ServerEnableDisableDto state = new ServerEnableDisableDto();
			state.setAlteonName(ipaddress);

			ArrayList<String> virtualServerID = new ArrayList<String>();
			virtualServerID.add(vsID);
			String virtualServerState = OBParser.getstate(useYN);
			state.setState(virtualServerState);

			output = new OBVdirectServiceImpl().createVirtualServer(service);
			if (!output.equals("200")) {
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						String.format("parsing error(ip : %s, vsID : %s, virtual Server state failed : %s, result:%s)",
								ipaddress, vsID, virtualServerState, SUFFIX_ERROR));
			}
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end"));
	}

	public void delVirtualServer(OBDtoAdcVServerAlteon obj, String ipaddress) throws OBException {
		String output;

		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start(%s). %s", this.serverName, obj.toString()));
		ConfigSlbDto server = new ConfigSlbDto();
		server.setAlteonName(ipaddress);
		server.setVirtualServerID(obj.getIndex());

		output = new OBVdirectServiceImpl().deleteVirtualServer(server);

		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("host:%s", this.serverName));

		if (!output.equals("200")) {// 비 정상 상태.
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
					String.format("parsing error(virtual server delete failed result:%s)", "Error"));
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end(%s). ", this.serverName));
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end(%s).", this.serverName));
	}

	public void addRealserverList(ArrayList<OBDtoAdcNodeAlteon> list, String ipaddress) throws OBException {
		ArrayList<String> realServerIDs = new ArrayList<String>();

		ConfigSlbDto realserver = new ConfigSlbDto();

		realserver.setAlteonName(ipaddress);
		realserver.setRealServerIDs(realServerIDs);
		for (int i = 0; i < list.size(); i++) {
			realServerIDs.add(list.get(i).getAlteonId() + "/rip " + list.get(i).getIpAddress());
		}

		String output = new OBVdirectServiceImpl().createRealServer(realserver);
		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("host:%s, command:%s", this.serverName, realserver));
		if (!output.equals("200")) {
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
					String.format("parsing error(virtual Server create, result:%s)", SUFFIX_ERROR));
		}
	}

	public void addVirtualServiceList(String vsID, ArrayList<OBDtoAdcVService> list, String ipaddress)
			throws OBException {
		for (int i = 0; i < list.size(); i++) {
			OBDtoAdcVService obj = list.get(i);
			addVirtualService(vsID, obj.getServicePort(), obj.getRealPort(), obj.getPool().getAlteonId(), ipaddress);
		}
	}

	private final static String CMND_SLB_VIRT_SERVICE_SET_GROUP = "/cfg/slb/virt %s/service %d/rport %d/group %s";
	private final static String CMND_SLB_VIRT_SERVICE_SET_GROUP_NOTICE = "/cfg/slb/virt %s/service %d/group %s";
	private final static String CMND_SLB_VIRT_SERVICE_SET_GROUP_HTTPS = "/cfg/slb/virt %s/service %d https/rport %d/group %s";
	private final static String CMND_SLB_VIRT_SERVICE_SET_GROUP_HTTP_NOTICE = "/cfg/slb/virt %s/service %d http/group %s";
	private final static String CMND_SLB_VIRT_SERVICE_SET_GROUP_HTTPS_NOTICE = "/cfg/slb/virt %s/service %d https/group %s";
	private final static String CMND_SLB_VIRT_SERVICE_SET_GROUP_BASIC = "/cfg/slb/virt %s/service %d basic-slb/rport %d/group %s";
	private final static String CMND_SLB_VIRT_SERVICE_SET_GROUP_BASIC_NOTICE = "/cfg/slb/virt %s/service %d basic-slb/group %s";

	public void addVirtualService(String vsID, Integer servicePort, Integer realPort, String poolID, String ipaddress)
			throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start(%s). vsID:%s, servicePort:%d, real:%d, poolID:%s",
				this.serverName, vsID, servicePort, realPort, poolID));
		String output;
		String command;
		String resultPattern;
		ConfigSlbDto service = new ConfigSlbDto();

		if (true == isWellKnownPort(servicePort)) {
			command = String.format(CMND_SLB_VIRT_SERVICE_SET_GROUP, vsID, servicePort, realPort, poolID); // v29부터 443
																											// 지정필수,
																											// v28은 없으면
																											// https지만
																											// 지정해도 됨

			service.setAlteonName(ipaddress);
			service.setVirtualServerID(vsID);
			service.setService(Integer.toString(servicePort));
			service.setRport(Integer.toString(realPort));
			service.setGroupID(poolID);
			service.setType("");
		} else if (servicePort.equals(HTTPS_PORT)) {
			command = String.format(CMND_SLB_VIRT_SERVICE_SET_GROUP_HTTPS, vsID, servicePort, realPort, poolID); // v29부터
																													// 443
																													// 지정필수,
																													// v28은
																													// 없으면
																													// https지만
																													// 지정해도
																													// 됨
			service.setAlteonName(ipaddress);
			service.setVirtualServerID(vsID);
			service.setService(Integer.toString(servicePort));
			service.setRport(Integer.toString(realPort));
			service.setGroupID(poolID);
			service.setType("https");
		} else {
			command = String.format(CMND_SLB_VIRT_SERVICE_SET_GROUP_BASIC, vsID, servicePort, realPort, poolID);
			service.setAlteonName(ipaddress);
			service.setVirtualServerID(vsID);
			service.setService(Integer.toString(servicePort));
			service.setRport(Integer.toString(realPort));
			service.setGroupID(poolID);
			service.setType("basic-slb");
		}

		output = new OBVdirectServiceImpl().createVirtualService(service);
		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("host:%s, command:%s", this.serverName, service));
		resultPattern = String.format("Error: ");
		if (!output.equals("200")) {// 비 정상 상태.
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
					String.format("parsing error(command:%s, result:%s)", command, resultPattern));
		}

		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end(%s).", this.serverName));
	}

	public void delVirtualService(String vsID, ArrayList<OBDtoAdcVService> list, String ipaddress) throws OBException {
		for (int i = 0; i < list.size(); i++) {
			OBDtoAdcVService obj = list.get(i);
			delVirtualService(vsID, obj.getServicePort(), ipaddress);
		}
	}

	private final static String CMND_SLB_VIRT_SERVICE_DEL_GROUP = "/cfg/slb/virt %s/service %d/del";
	private final static String CMND_SLB_VIRT_SERVICE_DEL_GROUP_HTTPS = "/cfg/slb/virt %s/service %d https/del";
	private final static String CMND_SLB_VIRT_SERVICE_DEL_GROUP_BASIC = "/cfg/slb/virt %s/service %d basic-slb/del";

	public void delVirtualService(String vsID, Integer servicePort, String ipaddress) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
				String.format("start(%s). vsID:%s, servicePort:%d", this.serverName, vsID, servicePort));
		String output;
		String command;
		String resultPattern;
		ConfigSlbDto service = new ConfigSlbDto();

		if (true == isWellKnownPort(servicePort)) {
			command = String.format(CMND_SLB_VIRT_SERVICE_DEL_GROUP, vsID, servicePort);

			service.setAlteonName(ipaddress);
			service.setVirtualServerID(vsID);
			service.setService(Integer.toString(servicePort));
		} else if (servicePort.equals(HTTPS_PORT)) {
			command = String.format(CMND_SLB_VIRT_SERVICE_DEL_GROUP_HTTPS, vsID, servicePort); // v29부터 443 지정필수, v28은
																								// 없으면 https지만 지정해도 됨

			service.setAlteonName(ipaddress);
			service.setVirtualServerID(vsID);
			service.setService(Integer.toString(servicePort) + " https");
		} else {
			command = String.format(CMND_SLB_VIRT_SERVICE_DEL_GROUP_BASIC, vsID, servicePort);

			service.setAlteonName(null);
			service.setVirtualServerID(vsID);
			service.setService(Integer.toString(servicePort) + " basic-slb");
		}

		output = new OBVdirectServiceImpl().deleteVirtualService(service);

		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("host:%s, command:%s", this.serverName, command));

		resultPattern = String.format("Error: ");
		if (!output.equals("200")) {// 비 정상 상태.
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
					String.format("parsing error(command:%s, result:%s)", command, resultPattern));
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end(%s). ", this.serverName));
	}

	private final static String CMND_SLB_GROUP_SET_DISABLE = "/cfg/slb/group %s/dis %s";
	private final static String CMND_SLB_GROUP_SET_ENABLE = "/cfg/slb/group %s/ena %s";

	private void changedPoolMemberState(String poolID, String nodeID, int state, String ipaddress) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
				String.format("start(%s). poolID:%s, nodeID:%s, state:%d", this.serverName, poolID, nodeID, state));
		String output;
		String command;
		ServerEnableDisableDto group = new ServerEnableDisableDto();
		group.setAlteonName(ipaddress);

		// State 설정.
		if (state == OBDefine.STATE_DISABLE) {
			group.setGroupID(poolID);
			group.setState("dis " + nodeID);
			command = String.format(CMND_SLB_GROUP_SET_DISABLE, poolID, nodeID);
		} else {
			group.setGroupID(poolID);
			group.setState("ena " + nodeID);
			command = String.format(CMND_SLB_GROUP_SET_ENABLE, poolID, nodeID);
		}
		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("host:%s, command:%s", this.serverName, command));
		output = new OBVdirectServiceImpl().changeGroupState(group);

		if (!output.equals("200")) {// 비 정상 상태.
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
					String.format("parsing error(command:%s, result:%s)", command, output));
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end(%s). ", this.serverName));
	}

	private final static String CMND_SLB_GROUP_SET_NAME = "/cfg/slb/group %s/name %s";

	private void changePoolName(String poolID, String name) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
				String.format("start(%s). poolID:%s, name:%s", this.serverName, poolID, name));

		String output;
		String command;

		if (name.isEmpty())
			command = String.format(CMND_SLB_GROUP_SET_NAME, poolID, "none");
		else
			command = String.format(CMND_SLB_GROUP_SET_NAME, poolID, name);
		output = sendCommand(command);
		if (output.indexOf(SUFFIX_ERROR) >= 0) {// 비 정상 상태.
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
					String.format("parsing error(command:%s, result:%s)", command, output));
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end(%s).", this.serverName));
	}

	private final static String CMND_SLB_GROUP_SET_METRIC_LEASTCONNS = "/cfg/slb/group %s/metric leastconns";
	private final static String CMND_SLB_GROUP_SET_METRIC_ROUNDROBIN = "/cfg/slb/group %s/metric roundrobin";
	private final static String CMND_SLB_GROUP_SET_METRIC_HASH = "/cfg/slb/group %s/metric hash";

	private void changePoolLBMethod(String poolID, int lbMethod) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
				String.format("start(%s). poolID:%s, lbMethod:%d", this.serverName, poolID, lbMethod));

		String output;
		String command = "";

		// slb metric 설정.
		switch (lbMethod) {
		case OBDefine.LB_METHOD_LEAST_CONNECTION_MEMBER:// Least connections.
			command = String.format(CMND_SLB_GROUP_SET_METRIC_LEASTCONNS, poolID);
			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("host:%s, command:%s", this.serverName, command));
			output = sendCommand(command);
			if (output.indexOf(SUFFIX_ERROR) >= 0) {// 비 정상 상태.
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						String.format("parsing error(command:%s, result:%s)", command, output));
			}
			break;
		case OBDefine.LB_METHOD_ROUND_ROBIN:// Round Robin
			command = String.format(CMND_SLB_GROUP_SET_METRIC_ROUNDROBIN, poolID);
			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("host:%s, command:%s", this.serverName, command));
			output = sendCommand(command);
			if (output.indexOf(SUFFIX_ERROR) >= 0) {// 비 정상 상태.
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						String.format("parsing error(command:%s, result:%s)", command, output));
			}
			break;
		case OBDefine.LB_METHOD_HASH:// Hash
			command = String.format(CMND_SLB_GROUP_SET_METRIC_HASH, poolID);
			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("host:%s, command:%s", this.serverName, command));
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

	private final static String CMND_SLB_GROUP_SET_HEALTH = "/cfg/slb/group %s/health %s";

	private void changePoolHealthCheckMethod(String poolId, String healthcheckId) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
				String.format("start(%s). poolId:%s, healthcheckId:%s", this.serverName, poolId, healthcheckId));

		String output;
		String command = "";

		// health check
		command = String.format(CMND_SLB_GROUP_SET_HEALTH, poolId, healthcheckId);
		output = sendCommand(command);
		if (output.indexOf(SUFFIX_ERROR) >= 0) {// 비 정상 상태.
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
					String.format("parsing error(command:%s, result:%s)", command, output));
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
						changedPoolMemberState(pool.getAlteonId(), member.getAlteonNodeID(), member.getState(),
								ipaddress);
				}
			}
		}
	}

	private final static String CMND_SLB_VIRT_SERVICE_SET_RPORT = "/cfg/slb/virt %s/service %d/rport %d";
	private final static String CMND_SLB_VIRT_SERVICE_SET_RPORT_HTTPS = "/cfg/slb/virt %s/service %d https/rport %d";
	private final static String CMND_SLB_VIRT_SERVICE_SET_RPORT_BASIC = "/cfg/slb/virt %s/service %d basic-slb/rport %d";

	public void changeServiceList(Integer vsID, Integer servicePort, Integer realPort) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
				String.format("start(%s). vsID:%s, service:%d, real:%d", this.serverName, vsID, servicePort, realPort));
		String output;
		String command;
		String resultPattern;
		if (true == isWellKnownPort(servicePort))
			command = String.format(CMND_SLB_VIRT_SERVICE_SET_RPORT, vsID, servicePort, realPort);
		else if (servicePort.equals(HTTPS_PORT))
			command = String.format(CMND_SLB_VIRT_SERVICE_SET_RPORT_HTTPS, vsID, servicePort, realPort); // v29부터 443
																											// 지정필수,
																											// v28은 없으면
																											// https지만
																											// 지정해도 됨
		else
			command = String.format(CMND_SLB_VIRT_SERVICE_SET_RPORT_BASIC, vsID, servicePort, realPort);

		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("host:%s, command:%s", this.serverName, command));
		output = sendCommand(command);
		resultPattern = String.format("Error: ");
		if (output.indexOf(resultPattern) >= 0) {// 비 정상 상태.
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
					String.format("parsing error(command:%s, result:%s)", command, resultPattern));
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end(%s).", this.serverName));
	}

	public void changeServicePool(String vsID, Integer servicePort, String groupID) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start(%s). vsID:%s, service:%d, groupID:%s",
				this.serverName, vsID, servicePort, groupID));
		String output;
		String command;
		String resultPattern;

		if (true == isWellKnownPortNotice(servicePort))
			command = String.format(CMND_SLB_VIRT_SERVICE_SET_GROUP_NOTICE, vsID, servicePort, groupID);
		else if (servicePort.equals(HTTP_PORT))
			command = String.format(CMND_SLB_VIRT_SERVICE_SET_GROUP_HTTP_NOTICE, vsID, servicePort, groupID); // v29부터
																												// 443
																												// 지정필수,
																												// v28은
																												// 없으면
																												// https지만
																												// 지정해도
																												// 됨
		else if (servicePort.equals(HTTPS_PORT))
			command = String.format(CMND_SLB_VIRT_SERVICE_SET_GROUP_HTTPS_NOTICE, vsID, servicePort, groupID); // v29부터
																												// 443
																												// 지정필수,
																												// v28은
																												// 없으면
																												// https지만
																												// 지정해도
																												// 됨
		else
			command = String.format(CMND_SLB_VIRT_SERVICE_SET_GROUP_BASIC_NOTICE, vsID, servicePort, groupID);

		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("host:%s, command:%s", this.serverName, command));
		output = sendCommand(command);
		resultPattern = String.format("Error: ");
		if (output.indexOf(resultPattern) >= 0) {// 비 정상 상태.
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
					String.format("parsing error(command:%s, result:%s)", command, resultPattern));
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end(%s).", this.serverName));
	}

	public void addPoolMemberList(String poolID, ArrayList<OBDtoAdcPoolMemberAlteon> list, String ipaddress)
			throws OBException {
		for (int i = 0; i < list.size(); i++) {
			OBDtoAdcPoolMemberAlteon obj = list.get(i);
			addPoolMember(poolID, obj.getAlteonNodeID(), obj.getState(), ipaddress);
		}
	}

	private final static String CMND_STRG_ADD_POOL_MEMBER = "/cfg/slb/group %s/add %s";

	private void addPoolMember(String poolID, String nodeID, Integer state, String ipaddress) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
				String.format("start(%s). poolID:%s, nodeID:%s", this.serverName, poolID, nodeID));
		String output;
		String command;
		ConfigSlbDto group = new ConfigSlbDto();

		group.setAlteonName(ipaddress);
		group.setGroupID(poolID);
		group.setRealServerID(nodeID);

		// ID 추가.
		command = String.format(CMND_STRG_ADD_POOL_MEMBER, poolID, nodeID);

		output = new OBVdirectServiceImpl().createGroupServer(group);
		if (!output.equals("200")) {// 비 정상 상태.
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
					String.format("parsing error(command:%s, result:%s)", command, output));
		}

		// pool member 속성 부여한다.
		changedPoolMemberState(poolID, nodeID, state, ipaddress);
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end(%s).", this.serverName));
	}

	private final static String CMND_STRG_DEL_POOL_MEMBER = "/cfg/slb/group %s/rem %s";

	public void delPoolMember(String poolID, ArrayList<OBDtoAdcPoolMemberAlteon> list, String ipaddress)
			throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start(%s). %s", this.serverName, list.toString()));
		String output;
		String command;
		ConfigSlbDto group = new ConfigSlbDto();
		group.setAlteonName(ipaddress);
		group.setGroupID(poolID);

		for (int i = 0; i < list.size(); i++) {
			OBDtoAdcPoolMemberAlteon obj = list.get(i);
			group.setRealServerID(obj.getAlteonNodeID());
			command = String.format(CMND_STRG_DEL_POOL_MEMBER, poolID, obj.getAlteonNodeID());
			output = new OBVdirectServiceImpl().deleteGroupServer(group);

			if (!output.equals("200")) {// 비 정상 상태.
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						String.format("parsing error(command:%s, result:%s)", command, output));
			}
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end(%s).", this.serverName));
	}

	public void changeVServerStatus(String vsId, Integer status, String ipaddress) throws OBException {
		ServerEnableDisableDto state = new ServerEnableDisableDto();
		state.setAlteonName(ipaddress);

		ArrayList<String> virtualServerID = new ArrayList<String>();
		virtualServerID.add(vsId);
		String virtualServerState = OBParser.getstate(status);
		state.setState(virtualServerState);

		String output = new OBVdirectServiceImpl().changeVirtualServerState(state);
		if (!output.equals("200")) {
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
					String.format("parsing error(ip : %s, vsID : %s, virtual Server state failed : %s, result:%s)",
							ipaddress, vsId, virtualServerState, SUFFIX_ERROR));
		}
	}

	public void changeRealServerStatus(String rsId, Integer status, String ipaddress) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start(%s). rsId:%s", this.serverName, rsId));

		ServerEnableDisableDto reals = new ServerEnableDisableDto();
		reals.setAlteonName(ipaddress);

		ArrayList<String> serverID = new ArrayList<String>();
		serverID.add(rsId);

		reals.setServerID(serverID);
		reals.setState(OBParser.getstate(status));

		String output = new OBVdirectServiceImpl().changeRealSeverStatus(reals);

		if (!output.equals("200")) {// 비 정상 상태.
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
					String.format("parsing error(real server change failed, result:%s)", output));
		}

		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end(%s). ", this.serverName));
	}

	private final static String CMND_VRRP_SET_VRID = "/cfg/l3/vrrp/vr %d/vrid %d";
	private final static String CMND_VRRP_SET_ADDR = "/cfg/l3/vrrp/vr %d/addr %s";
	private final static String CMND_VRRP_SET_INTERFACE = "/cfg/l3/vrrp/vr %d/if %d";
	private final static String CMND_VRRP_SET_PRIORITY = "/cfg/l3/vrrp/vr %d/prio %d";
	private final static String CMND_VRRP_SET_TRACK_HSRP_ENABLE = "/cfg/l3/vrrp/vr %d/track/hsrp enable";
	private final static String CMND_VRRP_SET_TRACK_HSRV_ENABLE = "/cfg/l3/vrrp/vr %d/track/hsrv enable";
	private final static String CMND_VRRP_SET_TRACK_IFS_ENABLE = "/cfg/l3/vrrp/vr %d/track/ifs enable";
	private final static String CMND_VRRP_SET_TRACK_L4PTS_ENABLE = "/cfg/l3/vrrp/vr %d/track/l4pts enable";
	private final static String CMND_VRRP_SET_TRACK_PORTS_ENABLE = "/cfg/l3/vrrp/vr %d/track/ports enable";
	private final static String CMND_VRRP_SET_TRACK_REALS_ENABLE = "/cfg/l3/vrrp/vr %d/track/reals enable";
	private final static String CMND_VRRP_SET_TRACK_VRS_ENABLE = "/cfg/l3/vrrp/vr %d/track/vrs enable";
	private final static String CMND_VRRP_SET_SHARE_ENABLE = "/cfg/l3/vrrp/vr %d/share enable";
	private final static String CMND_VRRP_SET_SHARE_DISABLE = "/cfg/l3/vrrp/vr %d/share disable";
	private final static String CMND_VRRP_SET_VR_ENABLE = "/cfg/l3/vrrp/vr %d/ena";

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
	public OBDtoAlteonVrrp addVrrp(String vIP, Integer routerID, Integer vrrpID, Integer ifNum, OBDtoVrrpInfo vrrpInfo,
			String ipaddress) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
				String.format("start(%s). start. vIP:%s, routerID:%s, vrrpID:%s, ifNum:%d, vrrpInfo:%s\n",
						this.serverName, vIP, routerID, vrrpID, ifNum, vrrpInfo));

		if (vrrpInfo == null) {
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "failed to get vrrp info.");
		}
		String output;
		String command;

		ConfigSlbDto slb = new ConfigSlbDto();
		HashMap<String, String> vrrp = new HashMap<String, String>();
		slb.setVrrp(vrrp);
		slb.setAlteonName(ipaddress);

		command = String.format(CMND_VRRP_SET_VRID, routerID, vrrpID);
		vrrp.put("vrid", command);
		// virtual ip 설정.
		command = String.format(CMND_VRRP_SET_ADDR, routerID, vIP);
		vrrp.put("addr", command);

		command = String.format(CMND_VRRP_SET_INTERFACE, routerID, ifNum);
		vrrp.put("interface", command);

		command = String.format(CMND_VRRP_SET_PRIORITY, routerID, vrrpInfo.getPriority());
		vrrp.put("priority", command);

		// track 설정.
		if (vrrpInfo.getTrackHsrp() == OBDefine.STATE_ENABLE) {
			command = String.format(CMND_VRRP_SET_TRACK_HSRP_ENABLE, routerID);
			vrrp.put("hsrp", command);
		} else {
			vrrp.put("hsrp", "");
		}

		if (vrrpInfo.getTrackHsrv() == OBDefine.STATE_ENABLE) {
			command = String.format(CMND_VRRP_SET_TRACK_HSRV_ENABLE, routerID);
			vrrp.put("hsrv", command);
		} else {
			vrrp.put("hsrv", "");
		}

		if (vrrpInfo.getTrackInt() == OBDefine.STATE_ENABLE) {
			command = String.format(CMND_VRRP_SET_TRACK_IFS_ENABLE, routerID);
			vrrp.put("ifs", command);
		} else {
			vrrp.put("ifs", "");
		}

		if (vrrpInfo.getTrackL4pts() == OBDefine.STATE_ENABLE) {
			command = String.format(CMND_VRRP_SET_TRACK_L4PTS_ENABLE, routerID);
			vrrp.put("l4pts", command);
		} else {
			vrrp.put("l4pts", "");
		}

		if (vrrpInfo.getTrackPorts() == OBDefine.STATE_ENABLE) {
			command = String.format(CMND_VRRP_SET_TRACK_PORTS_ENABLE, routerID);
			vrrp.put("ports", command);
		} else {
			vrrp.put("ports", "");
		}

		if (vrrpInfo.getTrackReals() == OBDefine.STATE_ENABLE) {
			command = String.format(CMND_VRRP_SET_TRACK_REALS_ENABLE, routerID);
			vrrp.put("reals", "");
		} else {
			vrrp.put("reals", "");
		}

		if (vrrpInfo.getTrackVrs() == OBDefine.STATE_ENABLE) {
			command = String.format(CMND_VRRP_SET_TRACK_VRS_ENABLE, routerID);
			vrrp.put("vrs", command);
		} else {
			vrrp.put("vrs", "");
		}

		// share옵션 설정. Alteon에서 확장된 VRRP의 개념으로 Backup인 상태에서도 VRRP VIP에 대한 패킷을 처리하여
		// Active-Active 모드로 동작하게 됨. default로 enable되어 있음
		// 이전에 설정된 값을 참조해서 그대로 설정한다.
		int share = vrrpInfo.getSharing();
		if (share == OBDefine.STATE_ENABLE)
			command = String.format(CMND_VRRP_SET_SHARE_ENABLE, routerID);
		else
			command = String.format(CMND_VRRP_SET_SHARE_DISABLE, routerID);

		vrrp.put("share", command);

		// vrrp 활성화.
		command = String.format(CMND_VRRP_SET_VR_ENABLE, routerID);
		vrrp.put("vr", command);

		output = new OBVdirectServiceImpl().createVrrp(slb);
		if (!output.equals("200")) {
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
					String.format("parsing error(ip : %s, vrrp config failed)", ipaddress));
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
	public void delVrrp(Integer vrID, String ipaddress) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start(%s). vrID:%d", this.serverName, vrID));
		String output;
		String command;

		command = String.format("/cfg/l3/vrrp/vr %d/del", vrID);
		ConfigSlbDto slb = new ConfigSlbDto();
		HashMap<String, String> vrrp = new HashMap<String, String>();
		slb.setVrrp(vrrp);
		slb.setAlteonName(ipaddress);
		vrrp.put("vid", command);

		output = new OBVdirectServiceImpl().deleteVrrp(slb);
		if (!output.equals("200")) {
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String
					.format("parsing error(ip : %s, vrrp config delete failed, command : %s)", ipaddress, command));
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end(%s).", this.serverName));
	}

	private boolean isWellKnownPort(Integer portNum) {
		boolean retVal = false;

		switch (portNum) {
		case 20:// ftp-data
		case 21:// ftp
		case 22:// ssh
		case 23:// telnet
		case 25:// smtp
		case 37:// time
		case 42:// name
		case 43:// whois
		case 53:// domain
		case 69:// tftp
		case 79:// finger
		case 80:// http
		case 109:// pop2
		case 110:// pop3
		case 119:// nntp
		case 123:// ntp
		case 143:// imap
		case 144:// news
		case 161:// snmp
		case 162:// snmptrap
		case 179:// bgp
		case 194:// irc
		case 389:// ldap
//		case 443://https - 443은 v28에서는 virtual service에 자동으로 https로 할당되지만 v29부터는 ssl or https라서 선택해야 한다. 그런데 v28에도 https라고 지정해도 되므로 통일성을 꾀하고자 기본포트 아닌 직접입력 처리를 한다.
		case 520:// rip
		case 554:// itsp
		case 1812:// radius-auth
		case 1813:// radius-acc
		case 1985:// hsrp
			retVal = true;
		}
		return retVal;
	}

	private boolean isWellKnownPortNotice(Integer portNum) {
		boolean retVal = false;

		switch (portNum) {
		case 20:// ftp-data
		case 21:// ftp
		case 22:// ssh
		case 23:// telnet
		case 25:// smtp
		case 37:// time
		case 42:// name
		case 43:// whois
		case 53:// domain
		case 69:// tftp
		case 79:// finger
//        case 80://http
		case 109:// pop2
		case 110:// pop3
		case 119:// nntp
		case 123:// ntp
		case 143:// imap
		case 144:// news
		case 161:// snmp
		case 162:// snmptrap
		case 179:// bgp
		case 194:// irc
		case 389:// ldap
//      case 443://https - 443은 v28에서는 virtual service에 자동으로 https로 할당되지만 v29부터는 ssl or https라서 선택해야 한다. 그런데 v28에도 https라고 지정해도 되므로 통일성을 꾀하고자 기본포트 아닌 직접입력 처리를 한다.
		case 520:// rip
		case 554:// itsp
		case 1812:// radius-auth
		case 1813:// radius-acc
		case 1985:// hsrp
			retVal = true;
		}
		return retVal;
	}

}
