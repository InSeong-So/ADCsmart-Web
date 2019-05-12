package kr.openbase.adcsmart.service.snmp;

import java.io.IOException;

import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.PDUv1;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.IpAddress;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.TimeTicks;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import kr.openbase.adcsmart.service.impl.OBLicenseImpl;
import kr.openbase.adcsmart.service.snmp.dto.DtoOidTrap;
import kr.openbase.adcsmart.service.snmp.system.OBSnmpSystem;
import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBException;

public class OBSnmpTrap {
	private Integer snmpVersion = SnmpConstants.version2c; // default version
	private String host;
	private String protocol = "UDP";
	private Integer trapPort = 162;
	private String community = "public";
	private Integer timeout = 1000;
	private Integer retry = 2;

	@Override
	public String toString() {
		return "OBSnmpTrap [snmpVersion=" + snmpVersion + ", host=" + host + ", protocol=" + protocol + ", trapPort="
				+ trapPort + ", community=" + community + ", timeout=" + timeout + ", retry=" + retry + "]";
	}

	public Integer getSnmpVersion() {
		return snmpVersion;
	}

	public void setSnmpVersion(Integer snmpVersion) {
		this.snmpVersion = snmpVersion;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public Integer getTrapPort() {
		return trapPort;
	}

	public void setTrapPort(Integer trapPort) {
		this.trapPort = trapPort;
	}

	public String getCommunity() {
		return community;
	}

	public void setCommunity(String community) {
		this.community = community;
	}

	public Integer getTimeout() {
		return timeout;
	}

	public void setTimeout(Integer timeout) {
		this.timeout = timeout;
	}

	public Integer getRetry() {
		return retry;
	}

	public void setRetry(Integer retry) {
		this.retry = retry;
	}
	// ----- end of getters/setters -----

	// 파라미터 없는 default constructor 차단
//  public OBSnmpTrap()
//  {
//  }

	public OBSnmpTrap(Integer version, String host, Integer port, String community) throws Exception {
		if (host == null || host.isEmpty() == true) // 전달된 주소가 없으면 오류로 처리한다.
		{
			throw new Exception("SNMP trap host is not assigned.");
		} else {
			this.setHost(host);
			if (version != null) {
				if (version.equals(1)) {
					this.setSnmpVersion(SnmpConstants.version1);
				} else if (version.equals(2)) {
					this.setSnmpVersion(SnmpConstants.version2c);
				}
				// else default, v2
			}
			this.setTrapPort(port);
			this.setCommunity(community);
		}
	}

	// 전송 테스트
//    public static void main(String[] args) throws Exception
//    {
//        OBSnmpTrap trap;
//        //v2
//        trap = new OBSnmpTrap(SnmpConstants.version2c, "172.172.2.153", 162, "public");
//        
//        //v1
//        //trap = new OBSnmpTrap(SnmpConstants.version1, "172.172.2.153", 162, "public");
//        
//        //send
//        trap.sendTrap("1.2.3.4.5", "system reboot");
//    }

	// ADCsmart SNMP trap send call
	public void sendAlarmTrap(String name, String msg, String adcName, String level) throws OBException {
		String version = new OBLicenseImpl().getVersion();
		String[] temp = null;
		if (version != null) {
			temp = version.split("p");
			version = temp[0];
		} else {
			version = "3.1.1"; // snmp 개시 버전은 3.1.1이므로 버전구하기에 오류가 있으면 3.1.1을 쓴다.
		}
		DtoOidTrap oid = new OBSnmpOidDB().getSnmpTrap(OBDefine.ADC_TYPE_ADCSMART, version, name);
		if (adcName == null) // adcName은 안 줄 수도 있기 때문에 빈문자열 처리한다.
		{
			adcName = "";
		}
		if (this.getSnmpVersion().equals(SnmpConstants.version2c)) {
			// Create v2 common PDU
			PDU pdu = new PDU();
			pdu.setType(PDU.TRAP); // pdu.setType(PDU.NOTIFICATION)??

			// localhost uptime을 구함
			TimeTicks uptime = new TimeTicks(new OBSnmpSystem("localhost").getSysUptimeElapsed());

			// 다음 두개는 v2 기본 항목
			pdu.add(new VariableBinding(SnmpConstants.sysUpTime, uptime));
			pdu.add(new VariableBinding(SnmpConstants.snmpTrapOID, new OID(oid.getNotiOid())));
			// variable 추가
			pdu.add(new VariableBinding(new OID(oid.getVarMsgOid()), new OctetString(msg)));
			pdu.add(new VariableBinding(new OID(oid.getVarObjectOid()), new OctetString(adcName)));
			pdu.add(new VariableBinding(new OID(oid.getVarLevelOid()), new OctetString(level)));

			sendTrapV2(pdu);
		} else if (this.getSnmpVersion().equals(SnmpConstants.version1)) {
			// V1 pdu 생성 및 v1 기본 필드 구성
			PDUv1 pdu = new PDUv1();
			pdu.setType(PDU.V1TRAP);
			pdu.setEnterprise(new OID(oid.getNotiOid()));
			pdu.setGenericTrap(PDUv1.ENTERPRISE_SPECIFIC);
			pdu.setSpecificTrap(1);
			pdu.setAgentAddress(new IpAddress(this.getHost()));

			// variable 추가
			pdu.add(new VariableBinding(SnmpConstants.snmpTrapOID, new OID(oid.getNotiOid())));
			pdu.add(new VariableBinding(new OID(oid.getVarMsgOid()), new OctetString(msg)));
			pdu.add(new VariableBinding(new OID(oid.getVarObjectOid()), new OctetString(adcName)));
			pdu.add(new VariableBinding(new OID(oid.getVarLevelOid()), new OctetString(level)));

			sendTrapV1(pdu);
		}
//      else //v3
//      {
//      }
	}

	private void sendTrapV1(PDUv1 pdu) throws OBException {
		try {
			// Create Transport Mapping
			@SuppressWarnings("rawtypes")
			TransportMapping transport = new DefaultUdpTransportMapping();
			transport.listen();

			// 수신측 설정
			Address targetAddress = new UdpAddress(this.getHost() + "/" + this.getTrapPort());
			CommunityTarget target = new CommunityTarget();
			target.setCommunity(new OctetString(this.getCommunity()));
			target.setVersion(SnmpConstants.version1);
			target.setAddress(targetAddress);
			target.setRetries(this.getRetry());
			target.setTimeout(this.getTimeout());

			// Send PDUv1
			@SuppressWarnings("unchecked")
			Snmp snmp = new Snmp(transport);
			// System.out.println("Sending V1 Trap to " + ipAddress + " on Port " + port);
			snmp.send(pdu, target);
			snmp.close();
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, "SNMP trap v1 send error:" + e.getMessage());
		}
	}

	private void sendTrapV2(PDU pdu) throws OBException {
		// 수신측 설정
		Address targetAddress = new UdpAddress(this.getHost() + "/" + this.getTrapPort());
		CommunityTarget target = new CommunityTarget();
		target.setCommunity(new OctetString(this.getCommunity()));
		target.setVersion(SnmpConstants.version2c);
		target.setAddress(targetAddress);
		target.setRetries(this.getRetry());
		target.setTimeout(this.getTimeout());

		// Send PDU(v2 common PDU)
		Snmp snmp;
		try {
			snmp = new Snmp(new DefaultUdpTransportMapping());
			snmp.send(pdu, target, null, null);
			snmp.close();
		} catch (IOException e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, "SNMP trap v2 send error:" + e.getMessage());
		}
	}

//	private void sendTrap(String trapOid, String message) throws OBException {
//		if (this.getSnmpVersion().equals(SnmpConstants.version1)) {
//			sendTrapV1(trapOid, message);
//		} else {
//			sendTrapV2(trapOid, message);
//		}
//	}

//	private void sendTrapV1(String trapOid, String message) throws OBException {
//		Snmp snmp = null;
//		try {
//			// Create Transport Mapping
//			TransportMapping transport = new DefaultUdpTransportMapping();
//			transport.listen();
//
//			// 수신측 설정
//			Address targetAddress = new UdpAddress(this.getHost() + "/" + this.getTrapPort());
//			CommunityTarget target = new CommunityTarget();
//			target.setCommunity(new OctetString(this.getCommunity()));
//			target.setVersion(SnmpConstants.version1);
//			target.setAddress(targetAddress);
//			target.setRetries(this.getRetry());
//			target.setTimeout(this.getTimeout());
//
//			// Create PDU for V1
//			PDUv1 pdu = new PDUv1();
//			pdu.setType(PDU.V1TRAP);
//			pdu.setEnterprise(new OID(trapOid));
//			pdu.setGenericTrap(PDUv1.ENTERPRISE_SPECIFIC);
//			pdu.setSpecificTrap(1);
//			pdu.setAgentAddress(new IpAddress(this.getHost()));
//
//			// Add vairable
//			pdu.add(new VariableBinding(SnmpConstants.snmpTrapOID, new OID(trapOid)));
//			pdu.add(new VariableBinding(new OID(trapOid), new OctetString(message)));
//
//			// Send PDU
//			snmp = new Snmp(transport);
//			snmp.send(pdu, target);
//			snmp.close();
//		} catch (Exception e) {
//			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, "SNMP trap v1 send error:" + e.getMessage());
//		} finally {
//			if (snmp != null) {
//				try {
//					snmp.close();
//				} catch (IOException e) { // 종료 오류면 trap은 전송됐으므로 throw하지 않고 오류만 기록한다.
//					OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, "snmp close fail: " + e.getMessage());
//				}
//			}
//		}
//	}
//
//	private void sendTrapV2(String trapOid, String message) throws OBException {
//		// Trap v2 PDU는 common PDU이다.
//		PDU pdu = new PDU();
//		pdu.setType(PDU.TRAP); // pdu.setType(PDU.NOTIFICATION)??
//
//		// localhost uptime을 구함
//		TimeTicks uptime = new TimeTicks(new OBSnmpSystem("localhost").getSysUptimeElapsed());
//
//		// 수신측 설정
//		Address targetAddress = new UdpAddress(this.getHost() + "/" + this.getTrapPort());
//		CommunityTarget target = new CommunityTarget();
//		target.setCommunity(new OctetString(this.getCommunity()));
//		target.setVersion(SnmpConstants.version2c);
//		target.setAddress(targetAddress);
//		target.setRetries(this.getRetry());
//		target.setTimeout(this.getTimeout());
//
//		pdu.add(new VariableBinding(SnmpConstants.sysUpTime, uptime));
//		pdu.add(new VariableBinding(SnmpConstants.snmpTrapOID, new OID(trapOid)));
//
//		// Add Variables
//		pdu.add(new VariableBinding(SnmpConstants.snmpTrapOID, new OID(trapOid)));
//		pdu.add(new VariableBinding(new OID(trapOid), new OctetString(message)));
//
//		// Send
//		Snmp snmp = null;
//		try {
//			snmp = new Snmp(new DefaultUdpTransportMapping());
//			snmp.send(pdu, target, null, null);
//			snmp.close();
//		} catch (IOException e) {
//			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, "SNMP trap v2 send error:" + e.getMessage());
//		} finally {
//			if (snmp != null) {
//				try {
//					snmp.close();
//				} catch (IOException e) { // 종료 오류면 trap은 전송됐으므로 throw하지 않고 오류만 기록한다.
//					OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, "snmp close fail: " + e.getMessage());
//				}
//			}
//		}
//	}
}
