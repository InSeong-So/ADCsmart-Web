/*
 * sw version: 28.1.5.0
 */
package kr.openbase.adcsmart.service.impl.alteon.handler;

import java.util.ArrayList;

import kr.openbase.adcsmart.service.dto.OBDtoAdcPoolAlteonChanged;
import kr.openbase.adcsmart.service.dto.OBDtoAdcPoolMemberAlteonChanged;
import kr.openbase.adcsmart.service.dto.OBDtoAdcVService;
import kr.openbase.adcsmart.service.dto.OBDtoAdcVServiceChanged;
import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.service.utility.OBSystemLog;

public class OBAdcAlteonV28 extends OBAdcAlteonHandler {
	static final int HTTP_PORT = 80;
	static final int HTTPS_PORT = 443;

//	public static void main(String[] args)
//	{
//		OBAdcAlteonV28 alteon = new OBAdcAlteonV28();
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

	public void addVirtualServiceList(String vsID, ArrayList<OBDtoAdcVService> list, String ipaddress)
			throws OBException {
		for (int i = 0; i < list.size(); i++) {
			OBDtoAdcVService obj = list.get(i);
			addVirtualService(vsID, obj.getServicePort(), obj.getRealPort(), obj.getPool().getAlteonId());
		}
	}

	private final static String CMND_SLB_VIRT_SERVICE_SET_GROUP = "/cfg/slb/virt %s/service %d/rport %d/group %s";
	private final static String CMND_SLB_VIRT_SERVICE_SET_GROUP_NOTICE = "/cfg/slb/virt %s/service %d/group %s";
	private final static String CMND_SLB_VIRT_SERVICE_SET_GROUP_HTTPS = "/cfg/slb/virt %s/service %d https/rport %d/group %s";
	private final static String CMND_SLB_VIRT_SERVICE_SET_GROUP_HTTP_NOTICE = "/cfg/slb/virt %s/service %d http/group %s";
	private final static String CMND_SLB_VIRT_SERVICE_SET_GROUP_HTTPS_NOTICE = "/cfg/slb/virt %s/service %d https/group %s";
	private final static String CMND_SLB_VIRT_SERVICE_SET_GROUP_BASIC = "/cfg/slb/virt %s/service %d basic-slb/rport %d/group %s";
	private final static String CMND_SLB_VIRT_SERVICE_SET_GROUP_BASIC_NOTICE = "/cfg/slb/virt %s/service %d basic-slb/group %s";

	public void addVirtualService(String vsID, Integer servicePort, Integer realPort, String poolID)
			throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start(%s). vsID:%s, servicePort:%d, real:%d, poolID:%s",
				this.serverName, vsID, servicePort, realPort, poolID));
		String output;
		String command;
		String resultPattern;

		if (true == isWellKnownPort(servicePort))
			command = String.format(CMND_SLB_VIRT_SERVICE_SET_GROUP, vsID, servicePort, realPort, poolID);
		else if (servicePort.equals(HTTPS_PORT))
			command = String.format(CMND_SLB_VIRT_SERVICE_SET_GROUP_HTTPS, vsID, servicePort, realPort, poolID); // v29부터
																													// 443
																													// 지정필수,
																													// v28은
																													// 없으면
																													// https지만
																													// 지정해도
																													// 됨
		else
			command = String.format(CMND_SLB_VIRT_SERVICE_SET_GROUP_BASIC, vsID, servicePort, realPort, poolID);

		output = sendCommand(command);
		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("host:%s, command:%s", this.serverName, command));
		resultPattern = String.format("Error: ");
		if (output.indexOf(resultPattern) >= 0) {// 비 정상 상태.
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
					String.format("parsing error(command:%s, result:%s)", command, resultPattern));
		}

		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end(%s).", this.serverName));
	}

	public void delVirtualService(String vsID, ArrayList<OBDtoAdcVService> list, String ipaddress) throws OBException {
		for (int i = 0; i < list.size(); i++) {
			OBDtoAdcVService obj = list.get(i);
			delVirtualService(vsID, obj.getServicePort());
		}
	}

	private final static String CMND_SLB_VIRT_SERVICE_DEL_GROUP = "/cfg/slb/virt %s/service %d/del";
	private final static String CMND_SLB_VIRT_SERVICE_DEL_GROUP_HTTPS = "/cfg/slb/virt %s/service %d https/del";
	private final static String CMND_SLB_VIRT_SERVICE_DEL_GROUP_BASIC = "/cfg/slb/virt %s/service %d basic-slb/del";

	public void delVirtualService(String vsID, Integer servicePort) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
				String.format("start(%s). vsID:%s, servicePort:%d", this.serverName, vsID, servicePort));
		String output;
		String command;
		String resultPattern;
		if (true == isWellKnownPort(servicePort))
			command = String.format(CMND_SLB_VIRT_SERVICE_DEL_GROUP, vsID, servicePort);
		else if (servicePort.equals(HTTPS_PORT))
			command = String.format(CMND_SLB_VIRT_SERVICE_DEL_GROUP_HTTPS, vsID, servicePort); // v29부터 443 지정필수, v28은
																								// 없으면 https지만 지정해도 됨
		else
			command = String.format(CMND_SLB_VIRT_SERVICE_DEL_GROUP_BASIC, vsID, servicePort);

		output = sendCommand(command);

		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("host:%s, command:%s", this.serverName, command));

		resultPattern = String.format("Error: ");
		if (output.indexOf(resultPattern) >= 0) {// 비 정상 상태.
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
					String.format("parsing error(command:%s, result:%s)", command, resultPattern));
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end(%s). ", this.serverName));
	}

	private final static String CMND_SLB_GROUP_SET_DISABLE = "/cfg/slb/group %s/dis %s";
	private final static String CMND_SLB_GROUP_SET_ENABLE = "/cfg/slb/group %s/ena %s";

	private void changedPoolMemberState(String poolID, String nodeID, int state) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
				String.format("start(%s). poolID:%s, nodeID:%s, state:%d", this.serverName, poolID, nodeID, state));
		String output;
		String command;
		String resultPattern;

		// State 설정.
		if (state == OBDefine.STATE_DISABLE) {
			command = String.format(CMND_SLB_GROUP_SET_DISABLE, poolID, nodeID);
		} else {
			command = String.format(CMND_SLB_GROUP_SET_ENABLE, poolID, nodeID);
		}
		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("host:%s, command:%s", this.serverName, command));
		output = sendCommand(command);
		resultPattern = String.format("Error: ");
		if (output.indexOf(resultPattern) >= 0) {// 비 정상 상태.
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
					String.format("parsing error(command:%s, result:%s)", command, resultPattern));
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
						changedPoolMemberState(pool.getAlteonId(), member.getAlteonNodeID(), member.getState());
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
