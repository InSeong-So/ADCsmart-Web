package kr.openbase.adcsmart.service.snmp;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import kr.openbase.adcsmart.service.database.OBDatabase;
import kr.openbase.adcsmart.service.snmp.alteon.dto.OBDtoOidRptFilterInfo;
import kr.openbase.adcsmart.service.snmp.dto.DtoOidCfgAdcAdditional;
import kr.openbase.adcsmart.service.snmp.dto.DtoOidCfgBaseVrrpInfo;
import kr.openbase.adcsmart.service.snmp.dto.DtoOidCfgInterface;
import kr.openbase.adcsmart.service.snmp.dto.DtoOidCfgNode;
import kr.openbase.adcsmart.service.snmp.dto.DtoOidCfgPool;
import kr.openbase.adcsmart.service.snmp.dto.DtoOidCfgPorts;
import kr.openbase.adcsmart.service.snmp.dto.DtoOidCfgVirtServer;
import kr.openbase.adcsmart.service.snmp.dto.DtoOidCfgVirtService;
import kr.openbase.adcsmart.service.snmp.dto.DtoOidEntity;
import kr.openbase.adcsmart.service.snmp.dto.DtoOidFaultCheckHW;
import kr.openbase.adcsmart.service.snmp.dto.DtoOidFaultCheckL23;
import kr.openbase.adcsmart.service.snmp.dto.DtoOidInfoAdcPerformance;
import kr.openbase.adcsmart.service.snmp.dto.DtoOidInfoAdcResc;
import kr.openbase.adcsmart.service.snmp.dto.DtoOidInfoCfgPAS;
import kr.openbase.adcsmart.service.snmp.dto.DtoOidInfoCfgPASK;
import kr.openbase.adcsmart.service.snmp.dto.DtoOidInfoHealthCheck;
import kr.openbase.adcsmart.service.snmp.dto.DtoOidInfoNetworkClass;
import kr.openbase.adcsmart.service.snmp.dto.DtoOidInfoSessionTable;
import kr.openbase.adcsmart.service.snmp.dto.DtoOidInfoSystem;
import kr.openbase.adcsmart.service.snmp.dto.DtoOidRptOPEtc;
import kr.openbase.adcsmart.service.snmp.dto.DtoOidRptOPGen;
import kr.openbase.adcsmart.service.snmp.dto.DtoOidRptOPL2;
import kr.openbase.adcsmart.service.snmp.dto.DtoOidRptOPL3;
import kr.openbase.adcsmart.service.snmp.dto.DtoOidRptOPL4;
import kr.openbase.adcsmart.service.snmp.dto.DtoOidRptOPL7;
import kr.openbase.adcsmart.service.snmp.dto.DtoOidRptOPSys;
import kr.openbase.adcsmart.service.snmp.dto.DtoOidStatPoolMembers;
import kr.openbase.adcsmart.service.snmp.dto.DtoOidStatVirtServer;
import kr.openbase.adcsmart.service.snmp.dto.DtoOidStatVirtService;
import kr.openbase.adcsmart.service.snmp.dto.DtoOidStateVirtService;
import kr.openbase.adcsmart.service.snmp.dto.DtoOidTrap;
import kr.openbase.adcsmart.service.snmp.dto.OBDtoOidRptInspection;
import kr.openbase.adcsmart.service.utility.OBCommon;
import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.service.utility.OBParser;
import kr.openbase.adcsmart.service.utility.OBSystemLog;

public class OBSnmpOidDB {
	public static int OID_TYPE_INFO_SYSTEM = 1;
	public static int OID_TYPE_INFO_ADCRESC = 2;

	public static int OID_TYPE_STAT_VSERVER = 10;
	public static int OID_TYPE_STAT_VSERVICE = 11;
	public static int OID_TYPE_STAT_POOLGROUP = 12;
	public static int OID_TYPE_STAT_NODE = 13;
	public static int OID_TYPE_STAT_POOLMEMBER = 14;

	public static int OID_TYPE_STATE_VSERVICE = 15;

	public static int OID_TYPE_PORTS = 19; // ports info
	public static int OID_TYPE_PERFORMANCE = 20;

	public static int OID_TYPE_CFG_NODE = 21;
	public static int OID_TYPE_CFG_POOL = 22;
	public static int OID_TYPE_CFG_VSERVER = 23;
	public static int OID_TYPE_CFG_VSERVICE = 24;
	// public static int OID_TYPE_CFG_VRRP = 25;
	public static int OID_TYPE_CFG_INTERFACE = 26;
	public static int OID_TYPE_CFG_FILTER = 27;
	// public static int OID_TYPE_CFG_HEALTHCHECK =

	public static int OID_TYPE_RPT_OP_GEN = 31;
	public static int OID_TYPE_RPT_OP_SYS = 32;
	public static int OID_TYPE_RPT_OP_L2 = 33;
	public static int OID_TYPE_RPT_OP_L3 = 34;
	public static int OID_TYPE_RPT_OP_L4 = 35;
	public static int OID_TYPE_RPT_OP_L7 = 36;
	public static int OID_TYPE_RPT_OP_ETC = 37;
	public static int OID_TYPE_ADC_ADDITIONAL = 39;
	public static int OID_TYPE_CFG_HEALTHCHECK = 40;
	public static int OID_TYPE_CFG_NETWORKCLASS = 41;

	public static int OID_TYPE_CFG_PAS = 51; // PAS 제품에 대한 OID를 추출한다.
	public static int OID_TYPE_CFG_PASK = 52; // PASK 제품에 대한 OID를 추출한다.

	public static int OID_TYPE_FAULT_HW = 61; // 장애 진단 관련 OID를 추출한다.
	public static int OID_TYPE_FAULT_L23 = 62; // 장애 진단 관련 OID를 추출한다.
	public static int OID_TYPE_SESSION_TABLE = 63; // 장애 진단 관련 OID를 추출한다.

	public static int OID_TYPE_RPT_INSPECTION = 71; // 정기점검 보고소 용.;

	public static int OID_TYPE_SNMPTRAP = 81; // ADCsmart SNMP 트랩 추가, trap으로 보낼 경보 oid
	public static int OID_TYPE_SNMPTRAP_VARIABLE = 82; // ADCsmart SNMP 트랩 추가, trap variable oid
	public static String VERSION_DEFAULT = "default";

	public DtoOidFaultCheckL23 getFaultCheckL23Info(Integer vendorType, String swVersion) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
				String.format("start. vendorType:%d, swVersion:%s", vendorType, swVersion));
		String version = VERSION_DEFAULT;
		if (swVersion != null && swVersion.isEmpty() == false) {
			version = swVersion;
		}
		String sqlText = "";

		try {
			// default OID를 깐다.
			sqlText = String.format(" SELECT NAME, OID FROM MNG_SNMPOID WHERE VENDOR=%d AND SWVERSION=%s AND TYPE=%d ",
					vendorType, OBParser.sqlString(VERSION_DEFAULT), OID_TYPE_FAULT_L23);
		} catch (Exception e) {
			throw new OBException(e.getMessage());
		}

		DtoOidFaultCheckL23 oids = getFaultCheckL23InfoData(null, sqlText); // default를 구해옴
		// 기본을 깔았으니, 특정 버전에 별도 준비한 OID 해당하는 것이 있으면 덮어쓴다.
		if (version.equals(VERSION_DEFAULT) == false) // default가 아닌 버전이면 업데이트할 oid를 찾는다.
		{
			try {
				sqlText = String.format(
						" SELECT NAME, OID FROM MNG_SNMPOID  " + " WHERE VENDOR=%d AND TYPE=%d        "
								+ "     AND (NAME, SWVERSION) IN       "
								+ "     ( SELECT NAME, MAX(SWVERSION) FROM MNG_SNMPOID "
								+ "       WHERE VENDOR=%d AND TYPE=%d AND SWVERSION <> %s AND SWVERSION <= %s "
								+ "       GROUP BY NAME ) ",
						vendorType, OID_TYPE_FAULT_L23, vendorType, OID_TYPE_FAULT_L23,
						OBParser.sqlString(VERSION_DEFAULT), OBParser.sqlString(version));
			} catch (Exception e) {
				throw new OBException(e.getMessage());
			}
			oids = getFaultCheckL23InfoData(oids, sqlText); // 업데이트한다.
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", oids));
		return oids;
	}

	private DtoOidFaultCheckL23 getFaultCheckL23InfoData(DtoOidFaultCheckL23 curOids, String sql) throws OBException {
		if (curOids == null) // 업데이트가 아니라 처음 조회면 객체를 만든다.
		{
			curOids = new DtoOidFaultCheckL23();
		}
		ResultSet rs;
		OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			rs = db.executeQuery(sql);
			while (rs.next()) {
				String name = db.getString(rs, "NAME");
				String oid = db.getString(rs, "OID");
				if (name != null && name.isEmpty() == false) {
					if (name.compareToIgnoreCase("vlanInfoId") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setVlanInfoId(oid);
						}
					} else if (name.compareToIgnoreCase("vrrpInfoVirtRtrState") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setVrrpInfoVirtRtrState(oid);
						}
					} else if (name.compareToIgnoreCase("vlanInfoName") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setVlanInfoName(oid);
						}
					} else if (name.compareToIgnoreCase("vlanInfoStatus") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setVlanInfoStatus(oid);
						}
					} else if (name.compareToIgnoreCase("vlanInfoJumbo") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setVlanInfoJumbo(oid);
						}
					} else if (name.compareToIgnoreCase("vlanInfoBwmContract") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setVlanInfoBwmContract(oid);
						}
					} else if (name.compareToIgnoreCase("vlanInfoLearn") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setVlanInfoLearn(oid);
						}
					} else if (name.compareToIgnoreCase("vlanInfoPorts") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setVlanInfoPorts(oid);
						}
					} else if (name.compareToIgnoreCase("stgCurCfgState") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setStgCurCfgState(oid);
						}
					} else if (name.compareToIgnoreCase("stgCurCfgVlanBmap") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setStgCurCfgVlanBmap(oid);
						}
					} else if (name.compareToIgnoreCase("sysSelfIpAddr") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setSysSelfIpAddr(oid);
						}
					} else if (name.compareToIgnoreCase("sysSelfIpNetmask") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setSysSelfIpNetmask(oid);
						}
					} else if (name.compareToIgnoreCase("sysSelfIpIsFloating") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setSysSelfIpIsFloatging(oid);
						}
					} else if (name.compareToIgnoreCase("sysSelfIpVlanName") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setSysSelfIpVlanName(oid);
						}
					} else if (name.compareToIgnoreCase("arpInfoDestIp") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setArpInfoDestIp(oid);
						}
					} else if (name.compareToIgnoreCase("arpInfoMacAddr") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setArpInfoMacAddr(oid);
						}
					} else if (name.compareToIgnoreCase("arpInfoSrcPort") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setArpInfoSrcPort(oid);
						}
					} else if (name.compareToIgnoreCase("arpInfoVlan") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setArpInfoVlanInfo(oid);
						}
					} else if (name.compareToIgnoreCase("vlanInterfaceType") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setVlanInterfaceType(oid);
						}
					}
				}
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY, String.format("%s, sqlText:%s", e.getMessage(), sql));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", curOids));
		return curOids;
	}

	public DtoOidFaultCheckHW getFaultCheckHWInfo(Integer vendorType, String swVersion) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
				String.format("start. vendorType:%d, swVersion:%s", vendorType, swVersion));

		String version = VERSION_DEFAULT;
		if (swVersion != null && swVersion.isEmpty() == false) {
			version = swVersion;
		}
		String sqlText;

		// DtoOidFaultCheckHW obj = new DtoOidFaultCheckHW();
		try
		// default OID를 깐다.
		{
			sqlText = String.format(" SELECT NAME, OID FROM MNG_SNMPOID WHERE VENDOR=%d AND SWVERSION=%s AND TYPE=%d ",
					vendorType, OBParser.sqlString(VERSION_DEFAULT), OID_TYPE_FAULT_HW);
		} catch (Exception e) {
			throw new OBException(e.getMessage());
		}

		DtoOidFaultCheckHW oids = DtoOidFaultCheckHWData(null, sqlText); // default를 구해옴
		// 기본을 깔았으니, 특정 버전에 별도 준비한 OID 해당하는 것이 있으면 덮어쓴다.
		if (version.equals(VERSION_DEFAULT) == false) // default가 아닌 버전이면 업데이트할 oid를 찾는다.
		{
			try {
				sqlText = String.format(
						" SELECT NAME, OID FROM MNG_SNMPOID WHERE VENDOR=%d AND TYPE=%d AND (NAME, SWVERSION) IN ( SELECT NAME, MAX(SWVERSION) FROM MNG_SNMPOID WHERE VENDOR=%d AND TYPE=%d AND SWVERSION <> %s AND SWVERSION <= %s GROUP BY NAME ) ",
						vendorType, OID_TYPE_FAULT_HW, vendorType, OID_TYPE_FAULT_HW,
						OBParser.sqlString(VERSION_DEFAULT), OBParser.sqlString(version));
			} catch (Exception e) {
				throw new OBException(e.getMessage());
			}
			oids = DtoOidFaultCheckHWData(oids, sqlText);
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", oids));
		return oids;
	}

	private DtoOidFaultCheckHW DtoOidFaultCheckHWData(DtoOidFaultCheckHW curOids, String sql) throws OBException {
		if (curOids == null) // 업데이트가 아니라 처음 조회면 객체를 만든다.
		{
			curOids = new DtoOidFaultCheckHW();
		}
		ResultSet rs;

		OBDatabase db = new OBDatabase();

		try {
			db.openDB();

			rs = db.executeQuery(sql);
			while (rs.next()) {
				String name = db.getString(rs, "NAME");
				String oid = db.getString(rs, "OID");
				if (name != null && name.isEmpty() == false) {
					if (name.compareToIgnoreCase("powerSupply") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setPowerSupply(oid);
						}
					} else if (name.compareToIgnoreCase("hwFanSpeed") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setHwFanSpeed(oid);
						}
					} else if (name.compareToIgnoreCase("uptime") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setUptime(oid);
						}
					} else if (name.compareToIgnoreCase("license") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setLicense(oid);
						}
					} else if (name.compareToIgnoreCase("portInfoSpeed") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setPortInfoSpeed(oid);
						}
					} else if (name.compareToIgnoreCase("portInfoMode") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setPortInfoMode(oid);
						}
					} else if (name.compareToIgnoreCase("portInfoLink") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setPortInfoLink(oid);
						}
					} else if (name.compareToIgnoreCase("portInfoPhyIfLastChange") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setPortInfoPhyIfLastChange(oid);
						}
					} else if (name.compareToIgnoreCase("portStatsPhyIfInOctets") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setPortStatsPhyIfInOctets(oid);
						}
					} else if (name.compareToIgnoreCase("portStatsPhyIfInUcastPkts") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setPortStatsPhyIfInUcastPkts(oid);
						}
					} else if (name.compareToIgnoreCase("portStatsPhyIfInNUcastPkts") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setPortStatsPhyIfInNUcastPkts(oid);
						}
					} else if (name.compareToIgnoreCase("portStatsPhyIfInDiscards") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setPortStatsPhyIfInDiscards(oid);
						}
					} else if (name.compareToIgnoreCase("portStatsPhyIfInErrors") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setPortStatsPhyIfInErrors(oid);
						}
					} else if (name.compareToIgnoreCase("portStatsPhyIfInUnknownProtos") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setPortStatsPhyIfInUnknownProtos(oid);
						}
					} else if (name.compareToIgnoreCase("mpCpuStats1Sec") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setMpCpuStats1Sec(oid);
						}
					} else if (name.compareToIgnoreCase("mpCpuStats4Sec") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setMpCpuStats4Sec(oid);
						}
					} else if (name.compareToIgnoreCase("mpCpuStats64Sec") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setMpCpuStats64Sec(oid);
						}
					} else if (name.compareToIgnoreCase("spCpuStats1Sec") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setSpCpuStats1Sec(oid);
						}
					} else if (name.compareToIgnoreCase("spCpuStats4Sec") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setSpCpuStats4Sec(oid);
						}
					} else if (name.compareToIgnoreCase("spCpuStats64Sec") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setSpCpuStats64Sec(oid);
						}
					} else if (name.compareToIgnoreCase("mpMemStatsTotal") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setMpMemStatsTotal(oid);
						}
					} else if (name.compareToIgnoreCase("mpMemStatsFree") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setMpMemStatsFree(oid);
						}
					} else if (name.compareToIgnoreCase("hwTemperatureStatus") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setHwTemperatureStatus(oid);
						}
					} else if (name.compareToIgnoreCase("hwFanStatus") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setHwFanStatus(oid);
						}
					} else if (name.compareToIgnoreCase("adcLog") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setAdcLog(oid);
						}
					} else if (name.compareToIgnoreCase("spCpuStats64CurBindings") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setSpCpuStats64CurBindings(oid);
						}
					} else if (name.compareToIgnoreCase("spCpuSessionMax") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setSpCpuSessionMax(oid);
						}
					}
				}
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY, String.format("%s, sqlText:%s", e.getMessage(), sql));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", curOids));
		return curOids;
	}

	// public static void main(String[] args)
	// {
	// OBDatabase db = new OBDatabase();
	// try
	// {
	// db.openDB();
	// }
	// catch(Exception e)
	// {
	// return;
	// }
	//
	// try
	// {
	// // DtoOidCfgInterface resc = new
	// OBSnmpOidDB().getCfgInterface(OBDefine.ADC_TYPE_ALTEON, "", db);
	//
	// DtoOidFaultCheckHW resc = new
	// OBSnmpOidDB().getFaultCheckHWInfo(OBDefine.ADC_TYPE_ALTEON, "", db);
	// System.out.println(resc.toString());
	// }
	// catch(OBException e)
	// {
	// e.printStackTrace();
	// }
	//
	// db.closeDB();
	// }

	public DtoOidRptOPEtc getRptOPEtcInfo(Integer vendorType, String swVersion) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
				String.format("start. vendorType:%d, swVersion:%s", vendorType, swVersion));
		String version = VERSION_DEFAULT;
		if (swVersion != null && swVersion.isEmpty() == false) {
			version = swVersion;
		}
		String sqlText;

		try {
			sqlText = String.format("SELECT NAME, OID FROM MNG_SNMPOID WHERE VENDOR=%d AND SWVERSION=%s AND TYPE=%d;",
					vendorType, OBParser.sqlString(VERSION_DEFAULT), OID_TYPE_RPT_OP_ETC);
		} catch (Exception e) {
			throw new OBException(e.getMessage());
		}
		DtoOidRptOPEtc oids = getRptOPEtcInfoData(null, sqlText); // default를 구해옴

		if (version.equals(VERSION_DEFAULT) == false) // default가 아닌 버전이면 업데이트할 oid를 찾는다.
		{
			try {
				sqlText = String.format(" SELECT NAME, OID FROM MNG_SNMPOID " + " WHERE VENDOR=%d AND TYPE=%d       "
						+ "     AND (NAME, SWVERSION ) IN     " + "     ( SELECT NAME, MAX(SWVERSION) FROM MNG_SNMPOID "
						+ "       WHERE VENDOR=%d AND TYPE=%d AND SWVERSION <> %s AND SWVERSION <= %s "
						+ "       GROUP BY NAME ) ", vendorType, OID_TYPE_RPT_OP_ETC, vendorType, OID_TYPE_RPT_OP_ETC,
						OBParser.sqlString(VERSION_DEFAULT), OBParser.sqlString(version));

				OBSystemLog.sql(OBDefine.LOGFILE_DEBUG, String.format("sqlText:%s", sqlText));
			} catch (Exception e) {
				throw new OBException(e.getMessage());
			}
			oids = getRptOPEtcInfoData(oids, sqlText);
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", oids));
		return oids;
	}

	private DtoOidRptOPEtc getRptOPEtcInfoData(DtoOidRptOPEtc curOids, String sql) throws OBException {
		if (curOids == null) // 업데이트가 아니라 처음 조회면 객체를 만든다.
		{
			curOids = new DtoOidRptOPEtc();
		}
		ResultSet rs;
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			rs = db.executeQuery(sql);
			while (rs.next()) {
				String name = db.getString(rs, "NAME");
				String oid = db.getString(rs, "OID");

				if (name != null && name.isEmpty() == false) {
					if (name.compareToIgnoreCase("etcSyslogHost1") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setSyslogHost1(oid);
						}
					} else if (name.compareToIgnoreCase("etcSyslogFacility1") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setSyslogFacility1(oid);
						}
					} else if (name.compareToIgnoreCase("etcSyslogSeverity1") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setSyslogSeverity1(oid);
						}
					} else if (name.compareToIgnoreCase("etcSyslogHost2") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setSyslogHost2(oid);
						}
					} else if (name.compareToIgnoreCase("etcSyslogFacility2") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setSyslogFacility2(oid);
						}
					} else if (name.compareToIgnoreCase("etcSyslogSeverity2") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setSyslogSeverity2(oid);
						}
					} else if (name.compareToIgnoreCase("etcSyslogHost3") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setSyslogHost3(oid);
						}
					} else if (name.compareToIgnoreCase("etcSyslogFacility3") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setSyslogFacility3(oid);
						}
					} else if (name.compareToIgnoreCase("etcSyslogSeverity3") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setSyslogSeverity3(oid);
						}
					} else if (name.compareToIgnoreCase("etcSyslogHost4") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setSyslogHost4(oid);
						}
					} else if (name.compareToIgnoreCase("etcSyslogFacility4") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setSyslogFacility4(oid);
						}
					} else if (name.compareToIgnoreCase("etcSyslogSeverity4") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setSyslogSeverity4(oid);
						}
					} else if (name.compareToIgnoreCase("etcSyslogHost5") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setSyslogHost5(oid);
						}
					} else if (name.compareToIgnoreCase("etcSyslogFacility5") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setSyslogFacility5(oid);
						}
					} else if (name.compareToIgnoreCase("etcSyslogSeverity5") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setSyslogSeverity5(oid);
						}
					} else if (name.compareToIgnoreCase("etcNtpServer") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setNtpServer(oid);
						}
					} else if (name.compareToIgnoreCase("etcNtpInterval") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setNtpInterval(oid);
						}
					} else if (name.compareToIgnoreCase("etcNtpTimezone") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setNtpTimezone(oid);
						}
					} else if (name.compareToIgnoreCase("etcNtpEnabled") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setNtpEnabled(oid);
						}
					} else if (name.compareToIgnoreCase("etcLogCnt") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setLogCnt(oid);
						}
					} else if (name.compareToIgnoreCase("etcLogLast") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setLogLast(oid);
						}
					}
				}
			}
		} catch (Exception e) {
			throw new OBException(e.getMessage());
		} finally {
			if (db != null) {
				db.closeDB();
			}
		}

		return curOids;
	}

	public DtoOidRptOPL7 getRptOPL7Info(Integer vendorType, String swVersion) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
				String.format("start. vendorType:%d, swVersion:%s", vendorType, swVersion));
		String version = VERSION_DEFAULT;
		if (swVersion != null && swVersion.isEmpty() == false) {
			version = swVersion;
		}

		String sqlText;
		try {
			sqlText = String.format("SELECT NAME, OID FROM MNG_SNMPOID WHERE VENDOR=%d AND SWVERSION=%s AND TYPE=%d;",
					vendorType, OBParser.sqlString(VERSION_DEFAULT), OID_TYPE_RPT_OP_L7);
		} catch (Exception e) {
			throw new OBException(e.getMessage());
		}
		DtoOidRptOPL7 oids = getRptOPL7InfoData(null, sqlText); // default를 구해옴

		if (version.equals(VERSION_DEFAULT) == false) // default가 아닌 버전이면 업데이트할 oid를 찾는다.
		{
			try {
				sqlText = String.format(" SELECT NAME, OID FROM MNG_SNMPOID " + " WHERE VENDOR=%d AND TYPE=%d       "
						+ "     AND (NAME, SWVERSION ) IN     " + "     ( SELECT NAME, MAX(SWVERSION) FROM MNG_SNMPOID "
						+ "       WHERE VENDOR=%d AND TYPE=%d AND SWVERSION <> %s AND SWVERSION <= %s "
						+ "       GROUP BY NAME ) ", vendorType, OID_TYPE_RPT_OP_L7, vendorType, OID_TYPE_RPT_OP_L7,
						OBParser.sqlString(VERSION_DEFAULT), OBParser.sqlString(version));

				OBSystemLog.sql(OBDefine.LOGFILE_DEBUG, String.format("sqlText:%s", sqlText));
			} catch (Exception e) {
				throw new OBException(e.getMessage());
			}
			oids = getRptOPL7InfoData(oids, sqlText);
		}

		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", oids));
		return oids;
	}

	private DtoOidRptOPL7 getRptOPL7InfoData(DtoOidRptOPL7 curOids, String sql) throws OBException {
		if (curOids == null) // 업데이트가 아니라 처음 조회면 객체를 만든다.
		{
			curOids = new DtoOidRptOPL7();
		}
		ResultSet rs;
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			rs = db.executeQuery(sql);
			while (rs.next()) {
				String name = db.getString(rs, "NAME");
				String oid = db.getString(rs, "OID");
				if (name != null && name.isEmpty() == false) {
					if (name.compareToIgnoreCase("l7iRuleName") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setiRuleName(oid);
						}
					} else if (name.compareToIgnoreCase("l7VSIPAddress") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setVsIPAddr(oid);
						}
					} else if (name.compareToIgnoreCase("l7VSiRuleName") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setVsiRuleName(oid);
						}
					} else if (name.compareToIgnoreCase("l7OneConnectName") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setOneConnect(oid);
						}
					} else if (name.compareToIgnoreCase("l7RamcacheName") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setRamcache(oid);
						}
					} else if (name.compareToIgnoreCase("l7CompressionName") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setCompression(oid);
						}
					} else if (name.compareToIgnoreCase("l7SSLAccelName") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setSslAccel(oid);
						}
					} else if (name.compareToIgnoreCase("l7VSProfileName") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setVsProfileName(oid);
						}
					}
				}
			}
		} catch (Exception e) {
			throw new OBException(e.getMessage());
		} finally {
			if (db != null) {
				db.closeDB();
			}
		}
		return curOids;
	}

	public DtoOidRptOPL4 getRptOPL4Info(Integer vendorType, String swVersion) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
				String.format("start. vendorType:%d, swVersion:%s", vendorType, swVersion));
		String version = VERSION_DEFAULT;
		if (swVersion != null && swVersion.isEmpty() == false) {
			version = swVersion;
		}

		// DtoOidRptOPL4 obj = new DtoOidRptOPL4();
		String sqlText;
		try {
			sqlText = String.format("SELECT NAME, OID FROM MNG_SNMPOID WHERE VENDOR=%d AND SWVERSION=%s AND TYPE=%d;",
					vendorType, OBParser.sqlString(VERSION_DEFAULT), OID_TYPE_RPT_OP_L4);
		} catch (Exception e) {
			throw new OBException(e.getMessage());
		}
		DtoOidRptOPL4 oids = getRptOPL4InfoData(null, sqlText); // default를 구해옴

		if (version.equals(VERSION_DEFAULT) == false) // default가 아닌 버전이면 업데이트할 oid를 찾는다.
		{
			try {
				sqlText = String.format(" SELECT NAME, OID FROM MNG_SNMPOID " + " WHERE VENDOR=%d AND TYPE=%d       "
						+ "     AND (NAME, SWVERSION ) IN     " + "     ( SELECT NAME, MAX(SWVERSION) FROM MNG_SNMPOID "
						+ "       WHERE VENDOR=%d AND TYPE=%d AND SWVERSION <> %s AND SWVERSION <= %s "
						+ "       GROUP BY NAME ) ", vendorType, OID_TYPE_RPT_OP_L4, vendorType, OID_TYPE_RPT_OP_L4,
						OBParser.sqlString(VERSION_DEFAULT), OBParser.sqlString(version));

				OBSystemLog.sql(OBDefine.LOGFILE_DEBUG, String.format("sqlText:%s", sqlText));
			} catch (Exception e) {
				throw new OBException(e.getMessage());
			}
			oids = getRptOPL4InfoData(oids, sqlText);
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", oids));
		return oids;
	}

	private DtoOidRptOPL4 getRptOPL4InfoData(DtoOidRptOPL4 curOids, String sql) throws OBException {
		if (curOids == null) // 업데이트가 아니라 처음 조회면 객체를 만든다.
		{
			curOids = new DtoOidRptOPL4();
		}
		ResultSet rs;
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			rs = db.executeQuery(sql);
			while (rs.next()) {
				String name = db.getString(rs, "NAME");
				String oid = db.getString(rs, "OID");
				if (name != null && name.isEmpty() == false) {
					if (name.compareToIgnoreCase("l4PoolMemberStatusAvail") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setPoolMemberStatusAvail(oid);
						}
					} else if (name.compareToIgnoreCase("l4PoolMemberStatusEnabled") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setPoolMemberStatusEnabled(oid);
						}
					} else if (name.compareToIgnoreCase("l4PoolMemberAddr") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setPoolMemberAddr(oid);
						}
					} else if (name.compareToIgnoreCase("l4PoolMemberIndex") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setPoolMemberIndex(oid);
						}
					} else if (name.compareToIgnoreCase("l4VSIndex") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setVsIndex(oid);
						}
					} else if (name.compareToIgnoreCase("l4VSAddr") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setVsAddr(oid);
						}
					} else if (name.compareToIgnoreCase("l4VSStatusAvail") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setVsStatusAvail(oid);
						}
					} else if (name.compareToIgnoreCase("l4VSStatusEnabled") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setVsStatusEnabled(oid);
						}
					} else if (name.compareToIgnoreCase("l4ConcurrentConn") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setConcurrentConn(oid);
						}
					} else if (name.compareToIgnoreCase("l4MaxConn") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setMaxConn(oid);
						}
					} else if (name.compareToIgnoreCase("l4DirectMode") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setDirectMode(oid);
						}
					}
				}
			}
		} catch (Exception e) {
			throw new OBException(e.getMessage());
		} finally {
			if (db != null) {
				db.closeDB();
			}
		}

		return curOids;
	}

	public DtoOidRptOPL3 getRptOPL3Info(Integer vendorType, String swVersion) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
				String.format("start. vendorType:%d, swVersion:%s", vendorType, swVersion));
		String version = VERSION_DEFAULT;
		if (swVersion != null && swVersion.isEmpty() == false) {
			version = swVersion;
		}
		String sqlText;

		// DtoOidRptOPL3 obj = new DtoOidRptOPL3();

		try {
			sqlText = String.format("SELECT NAME, OID FROM MNG_SNMPOID WHERE VENDOR=%d AND SWVERSION=%s AND TYPE=%d;",
					vendorType, OBParser.sqlString(VERSION_DEFAULT), OID_TYPE_RPT_OP_L3);
		} catch (Exception e) {
			throw new OBException(e.getMessage());
		}
		DtoOidRptOPL3 oids = getRptOPL3InfoData(null, sqlText); // default를 구해옴

		if (version.equals(VERSION_DEFAULT) == false) // default가 아닌 버전이면 업데이트할 oid를 찾는다.
		{
			try {
				sqlText = String.format(" SELECT NAME, OID FROM MNG_SNMPOID " + " WHERE VENDOR=%d AND TYPE=%d       "
						+ "     AND (NAME, SWVERSION ) IN     " + "     ( SELECT NAME, MAX(SWVERSION) FROM MNG_SNMPOID "
						+ "       WHERE VENDOR=%d AND TYPE=%d AND SWVERSION <> %s AND SWVERSION <= %s "
						+ "       GROUP BY NAME ) ", vendorType, OID_TYPE_RPT_OP_L3, vendorType, OID_TYPE_RPT_OP_L3,
						OBParser.sqlString(VERSION_DEFAULT), OBParser.sqlString(version));

				OBSystemLog.sql(OBDefine.LOGFILE_DEBUG, String.format("sqlText:%s", sqlText));
			} catch (Exception e) {
				throw new OBException(e.getMessage());
			}
			oids = getRptOPL3InfoData(oids, sqlText);
		}

		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", oids));
		return oids;
	}

	private DtoOidRptOPL3 getRptOPL3InfoData(DtoOidRptOPL3 curOids, String sql) throws OBException {
		if (curOids == null) // 업데이트가 아니라 처음 조회면 객체를 만든다.
		{
			curOids = new DtoOidRptOPL3();
		}
		ResultSet rs;
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			rs = db.executeQuery(sql);
			while (rs.next()) {
				String name = db.getString(rs, "NAME");
				String oid = db.getString(rs, "OID");
				if (name != null && name.isEmpty() == false) {
					if (name.compareToIgnoreCase("l3IntIndex") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setIntIndex(oid);
						}
					} else if (name.compareToIgnoreCase("l3IntAddr") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setIntAddr(oid);
						}
					} else if (name.compareToIgnoreCase("l3IntNetmask") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setIntNetmask(oid);
						}
					} else if (name.compareToIgnoreCase("l3IntBcastAddr") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setIntBcastAddr(oid);
						}
					} else if (name.compareToIgnoreCase("l3IntVlan") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setIntVlan(oid);
						}
					} else if (name.compareToIgnoreCase("l3IntStatus") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setIntStatus(oid);
						}
					} else if (name.compareToIgnoreCase("l3GatewayIndex") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setGwIndex(oid);
						}
					} else if (name.compareToIgnoreCase("l3GatewayAddr") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setGwAddr(oid);
						}
					} else if (name.compareToIgnoreCase("l3GatewayVlan") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setGwVlan(oid);
						}
					} else if (name.compareToIgnoreCase("l3GatewayStatus") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setGwStatus(oid);
						}
					} else if (name.compareToIgnoreCase("l3GatewayType") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setGwType(oid);
						}
					}
				}
			}
		} catch (Exception e) {
			throw new OBException(e.getMessage());
		} finally {
			if (db != null) {
				db.closeDB();
			}
		}
		return curOids;
	}

	public DtoOidRptOPL2 getRptOPL2Info(Integer vendorType, String swVersion) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
				String.format("start. vendorType:%d, swVersion:%s", vendorType, swVersion));

		String version = VERSION_DEFAULT;
		if (swVersion != null && swVersion.isEmpty() == false) {
			version = swVersion;
		}

		String sqlText;
		try {
			sqlText = String.format("SELECT NAME, OID FROM MNG_SNMPOID WHERE VENDOR=%d AND SWVERSION=%s AND TYPE=%d;",
					vendorType, OBParser.sqlString(VERSION_DEFAULT), OID_TYPE_RPT_OP_L2);
		} catch (Exception e) {
			throw new OBException(e.getMessage());
		}
		DtoOidRptOPL2 oids = getRptOPL2InfoData(null, sqlText); // default를 구해옴

		if (version.equals(VERSION_DEFAULT) == false) // default가 아닌 버전이면 업데이트할 oid를 찾는다.
		{
			try {
				sqlText = String.format(" SELECT NAME, OID FROM MNG_SNMPOID " + " WHERE VENDOR=%d AND TYPE=%d       "
						+ "     AND (NAME, SWVERSION ) IN     " + "     ( SELECT NAME, MAX(SWVERSION) FROM MNG_SNMPOID "
						+ "       WHERE VENDOR=%d AND TYPE=%d AND SWVERSION <> %s AND SWVERSION <= %s "
						+ "       GROUP BY NAME ) ", vendorType, OID_TYPE_RPT_OP_L2, vendorType, OID_TYPE_RPT_OP_L2,
						OBParser.sqlString(VERSION_DEFAULT), OBParser.sqlString(version));

				OBSystemLog.sql(OBDefine.LOGFILE_DEBUG, String.format("sqlText:%s", sqlText));
			} catch (Exception e) {
				throw new OBException(e.getMessage());
			}
			oids = getRptOPL2InfoData(oids, sqlText);
		}

		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", oids));
		return oids;
	}

	private DtoOidRptOPL2 getRptOPL2InfoData(DtoOidRptOPL2 curOids, String sql) throws OBException {
		if (curOids == null) // 업데이트가 아니라 처음 조회면 객체를 만든다.
		{
			curOids = new DtoOidRptOPL2();
		}
		ResultSet rs;
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			rs = db.executeQuery(sql);
			while (rs.next()) {
				String name = db.getString(rs, "NAME");
				String oid = db.getString(rs, "OID");
				if (name != null && name.isEmpty() == false) {
					if (name.compareToIgnoreCase("l2LinkUpInfo") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setLinkUpInfo(oid);
						}
					} else if (name.compareToIgnoreCase("l2LinkStateChanged") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setLinkStateChanged(oid);
						}
					} else if (name.compareToIgnoreCase("l2LinkDiscardsIn") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setLinkDiscardsIn(oid);
						}
					} else if (name.compareToIgnoreCase("l2LinkDiscardsOut") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setLinkDiscardsOut(oid);
						}
					} else if (name.compareToIgnoreCase("l2LinkErrorIn") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setLinkErrorIn(oid);
						}
					} else if (name.compareToIgnoreCase("l2LinkErrorOut") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setLinkErrorOut(oid);
						}
					} else if (name.compareToIgnoreCase("l2VlanId") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setVlanId(oid);
						}
					} else if (name.compareToIgnoreCase("l2VlanName") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setVlanName(oid);
						}
					} else if (name.compareToIgnoreCase("l2VlanPorts") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setVlanPorts(oid);
						}
					} else if (name.compareToIgnoreCase("l2VlanState") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setVlanState(oid);
						}
					} else if (name.compareToIgnoreCase("l2STPState") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setStpState(oid);
						}
					} else if (name.compareToIgnoreCase("l2STPInfo") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setStpInfo(oid);
						}
					} else if (name.compareToIgnoreCase("l2TrunkName") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setTrunkName(oid);
						}
					} else if (name.compareToIgnoreCase("l2TrunkStatus") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setTrunkStatus(oid);
						}
					}
				}
			}
		} catch (Exception e) {
			throw new OBException(e.getMessage());
		} finally {
			if (db != null) {
				db.closeDB();
			}
		}
		return curOids;
	}

	public DtoOidRptOPSys getRptOPSystemInfo(Integer vendorType, String swVersion) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
				String.format("start. vendorType:%d, swVersion:%s", vendorType, swVersion));
		String version = VERSION_DEFAULT;
		if (swVersion != null && swVersion.isEmpty() == false) {
			version = swVersion;
		}
		String sqlText;
		try {
			sqlText = String.format("SELECT NAME, OID FROM MNG_SNMPOID WHERE VENDOR=%d AND SWVERSION=%s AND TYPE=%d;",
					vendorType, OBParser.sqlString(VERSION_DEFAULT), OID_TYPE_RPT_OP_SYS);
		} catch (Exception e) {
			throw new OBException(e.getMessage());
		}

		DtoOidRptOPSys oids = getRptOPSystemInfoData(null, sqlText); // default를 구해옴
		if (version.equals(VERSION_DEFAULT) == false) // default가 아닌 버전이면 업데이트할 oid를 찾는다.
		{
			try {
				sqlText = String.format(" SELECT NAME, OID FROM MNG_SNMPOID " + " WHERE VENDOR=%d AND TYPE=%d       "
						+ "     AND (NAME, SWVERSION ) IN     " + "     ( SELECT NAME, MAX(SWVERSION) FROM MNG_SNMPOID "
						+ "       WHERE VENDOR=%d AND TYPE=%d AND SWVERSION <> %s AND SWVERSION <= %s "
						+ "       GROUP BY NAME ) ", vendorType, OID_TYPE_RPT_OP_SYS, vendorType, OID_TYPE_RPT_OP_SYS,
						OBParser.sqlString(VERSION_DEFAULT), OBParser.sqlString(version));

				OBSystemLog.sql(OBDefine.LOGFILE_DEBUG, String.format("sqlText:%s", sqlText));
			} catch (Exception e) {
				throw new OBException(e.getMessage());
			}
			oids = getRptOPSystemInfoData(oids, sqlText);
		}

		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", oids));
		return oids;
	}

	private DtoOidRptOPSys getRptOPSystemInfoData(DtoOidRptOPSys curOids, String sql) throws OBException {
		if (curOids == null) // 업데이트가 아니라 처음 조회면 객체를 만든다.
		{
			curOids = new DtoOidRptOPSys();
		}
		ResultSet rs;
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			rs = db.executeQuery(sql);
			while (rs.next()) {
				String name = db.getString(rs, "NAME");
				String oid = db.getString(rs, "OID");
				if (name != null && name.isEmpty() == false) {
					if (name.compareToIgnoreCase("sysUpTime") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setUpTime(oid);
						}
					} else if (name.compareToIgnoreCase("sysLastApplyTime") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setLastApplyTime(oid);
						}
					} else if (name.compareToIgnoreCase("sysCpuMPUtil64") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setCpuMPUtil64(oid);
						}
					} else if (name.compareToIgnoreCase("sysCpuSPUtil64") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setCpuSPUtil64(oid);
						}
					} else if (name.compareToIgnoreCase("sysMemMPUsed") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setMemMPUsed(oid);
						}
					} else if (name.compareToIgnoreCase("sysMemMPTotal") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setMemMPTotal(oid);
						}
					} else if (name.compareToIgnoreCase("sysMemSPUsed") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setMemSPUsed(oid);
						}
					} else if (name.compareToIgnoreCase("sysMemSPTotal") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setMemSPTotal(oid);
						}
					} else if (name.compareToIgnoreCase("sysMemTmmUsed") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setMemTmmUsed(oid);
						}
					} else if (name.compareToIgnoreCase("sysMemTmmTotal") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setMemTmmTotal(oid);
						}
					} else if (name.compareToIgnoreCase("sysFanStatus") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setFanStatus(oid);
						}
					} else if (name.compareToIgnoreCase("sysPowerSupplyStatus") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setPowerSupplyStatus(oid);
						}
					}
				}
			}
		} catch (Exception e) {
			throw new OBException(e.getMessage());
		} finally {
			if (db != null) {
				db.closeDB();
			}
		}
		return curOids;
	}

	public DtoOidRptOPGen getRptOPGeneralInfo(Integer vendorType, String swVersion) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
				String.format("start. vendorType:%d, swVersion:%s", vendorType, swVersion));
		String version = VERSION_DEFAULT;
		if (swVersion != null && swVersion.isEmpty() == false) {
			version = swVersion;
		}

		String sqlText;
		try {
			sqlText = String.format("SELECT NAME, OID FROM MNG_SNMPOID WHERE VENDOR=%d AND SWVERSION=%s AND TYPE=%d;",
					vendorType, OBParser.sqlString(VERSION_DEFAULT), OID_TYPE_RPT_OP_GEN);
		} catch (Exception e) {
			throw new OBException(e.getMessage());
		}
		DtoOidRptOPGen oids = getRptOPGeneralInfoData(null, sqlText); // default를 구해옴

		if (version.equals(VERSION_DEFAULT) == false) // default가 아닌 버전이면 업데이트할 oid를 찾는다.
		{
			try {
				sqlText = String.format(" SELECT NAME, OID FROM MNG_SNMPOID " + " WHERE VENDOR=%d AND TYPE=%d       "
						+ "     AND (NAME, SWVERSION ) IN     " + "     ( SELECT NAME, MAX(SWVERSION) FROM MNG_SNMPOID "
						+ "       WHERE VENDOR=%d AND TYPE=%d AND SWVERSION <> %s AND SWVERSION <= %s "
						+ "       GROUP BY NAME ) ", vendorType, OID_TYPE_RPT_OP_GEN, vendorType, OID_TYPE_RPT_OP_GEN,
						OBParser.sqlString(VERSION_DEFAULT), OBParser.sqlString(version));

				OBSystemLog.sql(OBDefine.LOGFILE_DEBUG, String.format("sqlText:%s", sqlText));
			} catch (Exception e) {
				throw new OBException(e.getMessage());
			}
			oids = getRptOPGeneralInfoData(oids, sqlText);
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", oids));
		return oids;
	}

	private DtoOidRptOPGen getRptOPGeneralInfoData(DtoOidRptOPGen curOids, String sql) throws OBException {
		if (curOids == null) // 업데이트가 아니라 처음 조회면 객체를 만든다.
		{
			curOids = new DtoOidRptOPGen();
		}
		ResultSet rs;
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			rs = db.executeQuery(sql);
			while (rs.next()) {
				String name = db.getString(rs, "NAME");
				String oid = db.getString(rs, "OID");
				if (name != null && name.isEmpty() == false) {
					if (name.compareToIgnoreCase("genHostName") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setHostName(oid);
						}
					} else if (name.compareToIgnoreCase("genModelName") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setModelName(oid);
						}
					} else if (name.compareToIgnoreCase("genOsVersion") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setOsVersion(oid);
						}
					} else if (name.compareToIgnoreCase("genLicenseKey") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setLicenseKey(oid);
						}
					} else if (name.compareToIgnoreCase("genLicenseType") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setLicenseType(oid);
						}
					} else if (name.compareToIgnoreCase("genSerialNum") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setSerialNum(oid);
						}
					} else if (name.compareToIgnoreCase("genIpAddress") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setIpAddress(oid);
						}
					} else if (name.compareToIgnoreCase("genMacAddress") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setMacAddress(oid);
						}
					} else if (name.compareToIgnoreCase("genUpTime") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setUpTime(oid);
						}
					} else if (name.compareToIgnoreCase("genActiveStandby") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setActiveStandby(oid);
						}
					}
				}
			}
		} catch (Exception e) {
			throw new OBException(e.getMessage());
		} finally {
			if (db != null) {
				db.closeDB();
			}
		}

		return curOids;
	}

	public DtoOidCfgPorts getCfgPortInfo(Integer vendorType, String swVersion) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
				String.format("start. vendorType:%d, swVersion:%s", vendorType, swVersion));
		String version = VERSION_DEFAULT;
		if (swVersion != null && swVersion.isEmpty() == false) {
			version = swVersion;
		}
		String sqlText;

		try {
			sqlText = String.format("SELECT NAME, OID FROM MNG_SNMPOID WHERE VENDOR=%d AND SWVERSION=%s AND TYPE=%d;",
					vendorType, OBParser.sqlString(VERSION_DEFAULT), OID_TYPE_PORTS);
		} catch (Exception e) {
			throw new OBException(e.getMessage());
		}
		DtoOidCfgPorts oids = getCfgPortInfoData(null, sqlText); // default를 구해옴
		if (version.equals(VERSION_DEFAULT) == false) // default가 아닌 버전이면 업데이트할 oid를 찾는다.
		{
			try {
				sqlText = String.format(" SELECT NAME, OID FROM MNG_SNMPOID " + " WHERE VENDOR=%d AND TYPE=%d       "
						+ "     AND (NAME, SWVERSION ) IN     " + "     ( SELECT NAME, MAX(SWVERSION) FROM MNG_SNMPOID "
						+ "       WHERE VENDOR=%d AND TYPE=%d AND SWVERSION <> %s AND SWVERSION <= %s "
						+ "       GROUP BY NAME ) ", vendorType, OID_TYPE_PORTS, vendorType, OID_TYPE_PORTS,
						OBParser.sqlString(VERSION_DEFAULT), OBParser.sqlString(version));

				OBSystemLog.sql(OBDefine.LOGFILE_DEBUG, String.format("sqlText:%s", sqlText));
			} catch (Exception e) {
				throw new OBException(e.getMessage());
			}
			oids = getCfgPortInfoData(oids, sqlText);
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", oids));
		return oids;
	}

	private DtoOidCfgPorts getCfgPortInfoData(DtoOidCfgPorts curOids, String sql) throws OBException {
		if (curOids == null) // 업데이트가 아니라 처음 조회면 객체를 만든다.
		{
			curOids = new DtoOidCfgPorts();
		}
		ResultSet rs;

		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			rs = db.executeQuery(sql);
			while (rs.next()) {
				String name = db.getString(rs, "NAME");
				String oid = db.getString(rs, "OID");
				if (name != null && name.isEmpty() == false) {
					if (name.compareToIgnoreCase("portName") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setPortName(oid);
						}
					} else if (name.compareToIgnoreCase("portDuplex") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setPortDuplex(oid);
						}
					} else if (name.compareToIgnoreCase("portSpeed") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setPortSpeed(oid);
						}
					} else if (name.compareToIgnoreCase("portStatus") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setPortStatus(oid);
						}
					} else if (name.compareToIgnoreCase("portPktsIn") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setPortPktsIn(oid);
						}
					} else if (name.compareToIgnoreCase("portPktsOut") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setPortPktsOut(oid);
						}
					} else if (name.compareToIgnoreCase("portPktsDiscardsIn") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setPortPktsDiscardsIn(oid);
						}
					} else if (name.compareToIgnoreCase("portPktsDiscardsOut") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setPortPktsDiscardsOut(oid);
						}
					} else if (name.compareToIgnoreCase("portPktsErrorsIn") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setPortPktsErrorsIn(oid);
						}
					} else if (name.compareToIgnoreCase("portPktsErrorsOut") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setPortPktsErrorsOut(oid);
						}
					} else if (name.compareToIgnoreCase("portBytesIn") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setPortBytesIn(oid);
						}
					} else if (name.compareToIgnoreCase("portBytesOut") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setPortBytesOut(oid);
						}
					} else if (name.compareToIgnoreCase("portAliasName") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setPortAliasName(oid);
						}
					} else if (name.compareToIgnoreCase("portPktsMultiIn") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setPortPktsMultiIn(oid);
						}
					} else if (name.compareToIgnoreCase("portPktsMultiOut") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setPortPktsMultiOut(oid);
						}
					} else if (name.compareToIgnoreCase("portPktsBroadIn") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setPortPktsBroadIn(oid);
						}
					} else if (name.compareToIgnoreCase("portPktsBroadOut") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setPortPktsBroadOut(oid);
						}
					} else if (name.compareToIgnoreCase("portPktsUnknownProtos") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setPortPktsUnknownProtos(oid);
						}
					} else if (name.compareToIgnoreCase("portConnType") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setPortConnType(oid);
						}
					} else if (name.compareToIgnoreCase("portMgmtState") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setPortMgmtState(oid);
						}
					} else if (name.compareToIgnoreCase("portMgmtStatus") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setPortMgmtStatus(oid);
						}
					} else if (name.compareToIgnoreCase("portMgmtSpeed") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setPortMgmtSpeed(oid);
						}
					} else if (name.compareToIgnoreCase("portMgmtBytesIn") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setPortMgmtBytesIn(oid);
						}
					} else if (name.compareToIgnoreCase("portMgmtBytesOut") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setPortMgmtBytesOut(oid);
						}
					} else if (name.compareToIgnoreCase("portMgmtPacketsIn") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setPortMgmtPacketsIn(oid);
						}
					} else if (name.compareToIgnoreCase("portMgmtPacketsOut") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setPortMgmtPacketsOut(oid);
						}
					} else if (name.compareToIgnoreCase("portMgmtErrorsIn") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setPortMgmtErrorsIn(oid);
						}
					} else if (name.compareToIgnoreCase("portMgmtErrorsOut") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setPortMgmtErrorsOut(oid);
						}
					} // 이하는 v26에서만 쓰는 pps, bps oid, v26 snmp 숫자 데이터가 32 비트여서 누적값을 쓰면 0 순환이 일어나 보정이
						// 필요했다.
					else if (name.compareToIgnoreCase("portBytesInPerSecV26") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setPortBytesInPerSec(oid);
						}
					} else if (name.compareToIgnoreCase("portBytesOutPerSecV26") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setPortBytesOutPerSec(oid);
						}
					}
				}
			}
		} catch (Exception e) {
			throw new OBException(e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
		return curOids;
	}

	public DtoOidCfgInterface getCfgInterface(Integer vendorType, String swVersion) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
				String.format("start. vendorType:%d, swVersion:%s", vendorType, swVersion));
		String version = VERSION_DEFAULT;
		if (swVersion != null && swVersion.isEmpty() == false) {
			version = swVersion;
		}
		String sqlText;

		// DtoOidCfgInterface obj = new DtoOidCfgInterface();
		try {
			sqlText = String.format("SELECT NAME, OID FROM MNG_SNMPOID WHERE VENDOR=%d AND SWVERSION=%s AND TYPE=%d;",
					vendorType, OBParser.sqlString(VERSION_DEFAULT), OID_TYPE_CFG_INTERFACE);
		} catch (Exception e) {
			throw new OBException(e.getMessage());
		}

		DtoOidCfgInterface oids = getCfgInterfaceData(null, sqlText); // default를 구해옴
		if (version.equals(VERSION_DEFAULT) == false) // default가 아닌 버전이면 업데이트할 oid를 찾는다.
		{
			try {
				sqlText = String.format(" SELECT NAME, OID FROM MNG_SNMPOID " + " WHERE VENDOR=%d AND TYPE=%d       "
						+ "     AND (NAME, SWVERSION ) IN     " + "     ( SELECT NAME, MAX(SWVERSION) FROM MNG_SNMPOID "
						+ "       WHERE VENDOR=%d AND TYPE=%d AND SWVERSION <> %s AND SWVERSION <= %s "
						+ "       GROUP BY NAME ) ", vendorType, OID_TYPE_CFG_INTERFACE, vendorType,
						OID_TYPE_CFG_INTERFACE, OBParser.sqlString(VERSION_DEFAULT), OBParser.sqlString(version));
			} catch (Exception e) {
				throw new OBException(e.getMessage());
			}
			OBSystemLog.sql(OBDefine.LOGFILE_DEBUG, String.format("sqlText:%s", sqlText));
			oids = getCfgInterfaceData(oids, sqlText);
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", oids));
		return oids;
	}

	private DtoOidCfgInterface getCfgInterfaceData(DtoOidCfgInterface curOids, String sql) throws OBException {
		if (curOids == null) // 업데이트가 아니라 처음 조회면 객체를 만든다.
		{
			curOids = new DtoOidCfgInterface();
		}
		ResultSet rs;
		OBDatabase db = new OBDatabase();
		try {
			db.openDB();

			rs = db.executeQuery(sql);

			while (rs.next()) {
				String name = db.getString(rs, "NAME");
				String oid = db.getString(rs, "OID");
				if ((name != null) && name.isEmpty() == false) {
					if (name.compareToIgnoreCase("intfIndex") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setIntfIndex(oid);
						}
					} else if (name.compareToIgnoreCase("intfAddr") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setIntfAddr(oid);
						}
					} else if (name.compareToIgnoreCase("intfMask") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setIntfMask(oid);
						}
					} else if (name.compareToIgnoreCase("intfState") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setIntfState(oid);
						}
					}
				}
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY, String.format("%s, sqlText:%s", e.getMessage(), sql));
		} catch (Exception e) {
			throw new OBException(e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
		return curOids;
	}

	public DtoOidCfgAdcAdditional getCfgAdcAdditional(Integer vendorType, String swVersion) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
				String.format("start. vendorType:%d, swVersion:%s", vendorType, swVersion));
		String version = VERSION_DEFAULT;
		if (swVersion != null && swVersion.isEmpty() == false) {
			version = swVersion;
		}
		String sqlText;

		try {
			sqlText = String.format("SELECT NAME, OID FROM MNG_SNMPOID WHERE VENDOR=%d AND SWVERSION=%s AND TYPE=%d;",
					vendorType, OBParser.sqlString(VERSION_DEFAULT), OID_TYPE_ADC_ADDITIONAL);
		} catch (Exception e) {
			throw new OBException(e.getMessage());
		}

		DtoOidCfgAdcAdditional oids = getCfgAdcAdditionalData(null, sqlText); // default 구함

		if (version.equals(VERSION_DEFAULT) == false) // default가 아닌 버전이면 업데이트할 oid를 찾는다.
		{
			try {
				sqlText = String.format(" SELECT NAME, OID FROM MNG_SNMPOID " + " WHERE VENDOR=%d AND TYPE=%d       "
						+ "     AND (NAME, SWVERSION ) IN     " + "     ( SELECT NAME, MAX(SWVERSION) FROM MNG_SNMPOID "
						+ "       WHERE VENDOR=%d AND TYPE=%d AND SWVERSION <> %s AND SWVERSION <= %s "
						+ "       GROUP BY NAME ) ", vendorType, OID_TYPE_ADC_ADDITIONAL, vendorType,
						OID_TYPE_ADC_ADDITIONAL, OBParser.sqlString(VERSION_DEFAULT), OBParser.sqlString(version));
				OBSystemLog.sql(OBDefine.LOGFILE_DEBUG, String.format("sqlText:%s", sqlText));
			} catch (Exception e) {
				throw new OBException(e.getMessage());
			}
			oids = getCfgAdcAdditionalData(oids, sqlText);
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", oids));
		return oids;
	}

	private DtoOidCfgAdcAdditional getCfgAdcAdditionalData(DtoOidCfgAdcAdditional curOids, String sql)
			throws OBException {
		if (curOids == null) // 업데이트가 아니라 처음 조회면 객체를 만든다.
		{
			curOids = new DtoOidCfgAdcAdditional();
		}
		ResultSet rs;
		OBDatabase db = new OBDatabase();
		try {
			db.openDB();

			rs = db.executeQuery(sql);

			while (rs.next()) {
				String name = db.getString(rs, "NAME");
				String oid = db.getString(rs, "OID");
				if ((name != null) && (name.isEmpty() == false)) {
					if (name.compareToIgnoreCase("confSyncState") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setConfSyncState(oid);
						}
					} else if (name.compareToIgnoreCase("confSyncPeerIP") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setConfSyncPeerIP(oid);
						}
					} else if (name.compareToIgnoreCase("vrrpID") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setVrrpID(oid);
						}
					} else if (name.compareToIgnoreCase("vrrpAddr") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setVrrpAddr(oid);
						}
					} else if (name.compareToIgnoreCase("vrrpIfIndex") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setVrrpIfIndex(oid);
						}
					} else if (name.compareToIgnoreCase("vrrpPriority") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setVrrpPriority(oid);
						}
					} else if (name.compareToIgnoreCase("vrrpShare") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setVrrpShare(oid);
						}
					} else if (name.compareToIgnoreCase("vrrpTrackVrs") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setVrrpTrackVrs(oid);
						}
					} else if (name.compareToIgnoreCase("vrrpTrackIfs") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setVrrpTrackIfs(oid);
						}
					} else if (name.compareToIgnoreCase("vrrpTrackPorts") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setVrrpTrackPorts(oid);
						}
					} else if (name.compareToIgnoreCase("vrrpTrackL4pts") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setVrrpTrackL4pts(oid);
						}
					} else if (name.compareToIgnoreCase("vrrpTrackReals") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setVrrpTrackReals(oid);
						}
					} else if (name.compareToIgnoreCase("vrrpTrackHsrp") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setVrrpTrackHsrp(oid);
						}
					} else if (name.compareToIgnoreCase("vrrpTrackHsrv") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setVrrpTrackHsrv(oid);
						}
					} else if (name.compareToIgnoreCase("vrrpState") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setVrrpState(oid);
						}
					}
				}
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY, String.format("%s, sqlText:%s", e.getMessage(), sql));
		} catch (Exception e) {
			throw new OBException(e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
		return curOids;
	}

	// public DtoOidCfgVrrpInfo getCfgVrrpInfo(Integer vendorType, String swVersion,
	// OBDatabase db) throws OBException
	// {
	// OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. vendorType:%d,
	// swVersion:%s", vendorType, swVersion));
	// String version = VERSION_DEFAULT;
	// if(swVersion != null && swVersion.isEmpty()==false)
	// {
	// version = swVersion;
	// }
	// String sqlText;
	// try
	// {
	// sqlText = String.format(
	// "SELECT NAME, OID FROM MNG_SNMPOID WHERE VENDOR=%d AND SWVERSION=%s AND
	// TYPE=%d;",
	// vendorType,
	// OBParser.sqlString(VERSION_DEFAULT),
	// OID_TYPE_ADC_ADDITIONAL);
	// }
	// catch(Exception e)
	// {
	// throw new OBException(e.getMessage());
	// }
	// DtoOidCfgVrrpInfo oids = getCfgVrrpInfoData(null, sqlText, db); //default를
	// 구해옴
	//
	// if(version.equals(VERSION_DEFAULT)==false) //default가 아닌 버전이면 업데이트할 oid를 찾는다.
	// {
	// try
	// {
	// sqlText = String.format(
	// " SELECT NAME, OID FROM MNG_SNMPOID " +
	// " WHERE VENDOR=%d AND TYPE=%d " +
	// " AND (NAME, SWVERSION ) IN " +
	// " ( SELECT NAME, MAX(SWVERSION) FROM MNG_SNMPOID " +
	// " WHERE VENDOR=%d AND TYPE=%d AND SWVERSION <> %s AND SWVERSION <= %s " +
	// " GROUP BY NAME ) "
	// , vendorType, OID_TYPE_ADC_ADDITIONAL
	// , vendorType, OID_TYPE_ADC_ADDITIONAL, OBParser.sqlString(VERSION_DEFAULT),
	// OBParser.sqlString(version)
	// );
	//
	// OBSystemLog.sql(OBDefine.LOGFILE_DEBUG, String.format("sqlText:%s",
	// sqlText));
	// }
	// catch(Exception e)
	// {
	// throw new OBException(e.getMessage());
	// }
	// oids = getCfgVrrpInfoData(oids, sqlText, db);
	// }
	//
	// OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s",
	// oids));
	// return oids;
	// }
	// private DtoOidCfgVrrpInfo getCfgVrrpInfoData(DtoOidCfgVrrpInfo curOids,
	// String sql, OBDatabase db) throws OBException
	// {
	// if(curOids==null) //업데이트가 아니라 처음 조회면 객체를 만든다.
	// {
	// curOids = new DtoOidCfgVrrpInfo();
	// }
	// ResultSet rs;
	// try
	// {
	// rs = db.executeQuery(sql);
	// while(rs.next())
	// {
	// String name = db.getString(rs, "NAME");
	// String oid = db.getString(rs, "OID");
	// if(name != null && name.isEmpty()==false)
	// {
	// if((name != null) && (name.isEmpty() == false) &&
	// (name.compareToIgnoreCase("virtRtrIndex")==0))
	// {
	// if((oid != null) && (oid.isEmpty() == false))
	// {
	// curOids.setVirtRtrIndex(oid);
	// }
	// }
	// else if(name.compareToIgnoreCase("virtRtrID")==0)
	// {
	// if((oid != null) && (oid.isEmpty() == false))
	// {
	// curOids.setVirtRtrID(oid);
	// }
	// }
	// else if(name.compareToIgnoreCase("virtRtrAddr")==0)
	// {
	// if((oid != null) && (oid.isEmpty() == false))
	// {
	// curOids.setVirtRtrAddr(oid);
	// }
	// }
	// else if(name.compareToIgnoreCase("virtRtrIfIndex")==0)
	// {
	// if((oid != null) && (oid.isEmpty() == false))
	// {
	// curOids.setVirtRtrIfIndex(oid);
	// }
	// }
	// else if(name.compareToIgnoreCase("virtRtrPriority")==0)
	// {
	// if((oid != null) && (oid.isEmpty() == false))
	// {
	// curOids.setVirtRtrPriority(oid);
	// }
	// }
	// else if(name.compareToIgnoreCase("virtRtrState")==0)
	// {
	// if((oid != null) && (oid.isEmpty() == false))
	// {
	// curOids.setVirtRtrState(oid);
	// }
	// }
	// }
	// }
	// }
	// catch(Exception e)
	// {
	// throw new OBException(e.getMessage());
	// }
	// return curOids;
	// }

	public DtoOidCfgBaseVrrpInfo getCfgVrrp(Integer vendorType, String swVersion) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
				String.format("start. vendorType:%d, swVersion:%s", vendorType, swVersion));
		String version = VERSION_DEFAULT;
		if (swVersion != null && swVersion.isEmpty() == false) {
			version = swVersion;
		}
		String sqlText;
		try {
			sqlText = String.format("SELECT NAME, OID FROM MNG_SNMPOID WHERE VENDOR=%d AND SWVERSION=%s AND TYPE=%d;",
					vendorType, OBParser.sqlString(VERSION_DEFAULT), OID_TYPE_ADC_ADDITIONAL);
		} catch (Exception e) {
			throw new OBException(e.getMessage());
		}
		DtoOidCfgBaseVrrpInfo oids = getCfgVrrpData(null, sqlText); // default를 구해옴

		if (version.equals(VERSION_DEFAULT) == false) // default가 아닌 버전이면 업데이트할 oid를 찾는다.
		{
			try {
				sqlText = String.format(" SELECT NAME, OID FROM MNG_SNMPOID " + " WHERE VENDOR=%d AND TYPE=%d       "
						+ "     AND (NAME, SWVERSION ) IN     " + "     ( SELECT NAME, MAX(SWVERSION) FROM MNG_SNMPOID "
						+ "       WHERE VENDOR=%d AND TYPE=%d AND SWVERSION <> %s AND SWVERSION <= %s "
						+ "       GROUP BY NAME ) ", vendorType, OID_TYPE_ADC_ADDITIONAL, vendorType,
						OID_TYPE_ADC_ADDITIONAL, OBParser.sqlString(VERSION_DEFAULT), OBParser.sqlString(version));

				OBSystemLog.sql(OBDefine.LOGFILE_DEBUG, String.format("sqlText:%s", sqlText));
			} catch (Exception e) {
				throw new OBException(e.getMessage());
			}
			oids = getCfgVrrpData(oids, sqlText);
		}

		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", oids));
		return oids;
	}

	private DtoOidCfgBaseVrrpInfo getCfgVrrpData(DtoOidCfgBaseVrrpInfo curOids, String sql) throws OBException {
		if (curOids == null) // 업데이트가 아니라 처음 조회면 객체를 만든다.
		{
			curOids = new DtoOidCfgBaseVrrpInfo();
		}
		ResultSet rs;
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			rs = db.executeQuery(sql);
			while (rs.next()) {
				String name = db.getString(rs, "NAME");
				String oid = db.getString(rs, "OID");
				if (name != null && name.isEmpty() == false) {
					if ((name != null) && (name.isEmpty() == false)
							&& (name.compareToIgnoreCase("vrrpPriority") == 0)) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setVrrpPriority(oid);
						}
					} else if (name.compareToIgnoreCase("vrrpShare") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setVrrpShare(oid);
						}
					} else if (name.compareToIgnoreCase("vrrpTrackVrs") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setVrrpTrackVrs(oid);
						}
					} else if (name.compareToIgnoreCase("vrrpTrackIfs") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setVrrpTrackIfs(oid);
						}
					} else if (name.compareToIgnoreCase("vrrpTrackPorts") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setVrrpTrackPorts(oid);
						}
					} else if (name.compareToIgnoreCase("vrrpTrackL4pts") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setVrrpTrackL4pts(oid);
						}
					} else if (name.compareToIgnoreCase("vrrpTrackReals") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setVrrpTrackReals(oid);
						}
					} else if (name.compareToIgnoreCase("vrrpTrackHsrp") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setVrrpTrackHsrp(oid);
						}
					} else if (name.compareToIgnoreCase("vrrpTrackHsrv") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setVrrpTrackHsrv(oid);
						}
					}
				}
			}
		} catch (Exception e) {
			throw new OBException(e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
		return curOids;
	}

	public String getSysIdByNum(Integer vendorType, String swVersion) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
				String.format("start. vendorType:%d, swVersion:%s", vendorType, swVersion));
		String oid = null;
		if (vendorType != OBDefine.ADC_TYPE_ALTEON || swVersion == null || swVersion.isEmpty())
			return null;
		if (!OBCommon.checkSysIdByAlteon(swVersion)) {
			return null;
		}
		String sqlText = "";
		ResultSet rs;
		OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			sqlText = String.format("SELECT NAME, OID FROM MNG_SNMPOID WHERE VENDOR=%d AND SWVERSION=%s AND TYPE=%d;",
					vendorType, OBParser.sqlString("29.5.0.0"), OID_TYPE_INFO_SYSTEM);
			OBSystemLog.sql(OBDefine.LOGFILE_DEBUG, String.format("sqlText:%s", sqlText));

			rs = db.executeQuery(sqlText);
			if (rs.next()) {
				oid = db.getString(rs, "OID");
			}
		} catch (Exception e) {
			throw new OBException(e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
		return oid;
	}

	public DtoOidCfgNode getCfgNode(Integer vendorType, String swVersion) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
				String.format("start. vendorType:%d, swVersion:%s", vendorType, swVersion));

		String version = VERSION_DEFAULT;
		if (swVersion != null && swVersion.isEmpty() == false) {
			version = swVersion;
		}

		String sqlText;
		try {
			sqlText = String.format("SELECT NAME, OID FROM MNG_SNMPOID WHERE VENDOR=%d AND SWVERSION=%s AND TYPE=%d;",
					vendorType, OBParser.sqlString(VERSION_DEFAULT), OID_TYPE_CFG_NODE);
		} catch (Exception e) {
			throw new OBException(e.getMessage());
		}

		DtoOidCfgNode oids = getCfgNodeData(null, sqlText);

		if (version.equals(VERSION_DEFAULT) == false) // default가 아닌 버전이면 업데이트할 oid를 찾는다.
		{
			try { // default가 아니면서 지금 adc swversion보다 swversion이 작은 것
				sqlText = String.format(
						" SELECT NAME, OID FROM MNG_SNMPOID  " + " WHERE VENDOR=%d AND TYPE=%d        "
								+ "     AND (NAME, SWVERSION) IN       "
								+ "     ( SELECT NAME, MAX(SWVERSION) FROM MNG_SNMPOID "
								+ "       WHERE VENDOR=%d AND TYPE=%d AND SWVERSION <> %s AND SWVERSION <= %s "
								+ "       GROUP BY NAME ) ",
						vendorType, OID_TYPE_CFG_NODE, vendorType, OID_TYPE_CFG_NODE,
						OBParser.sqlString(VERSION_DEFAULT), OBParser.sqlString(version));
			} catch (Exception e) {
				throw new OBException(e.getMessage());
			}
			oids = getCfgNodeData(oids, sqlText);
		}

		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", oids));
		return oids;
	}

	private DtoOidCfgNode getCfgNodeData(DtoOidCfgNode curOids, String sql) throws OBException {
		if (curOids == null) // 업데이트가 아니라 처음 조회면 객체를 만든다.
		{
			curOids = new DtoOidCfgNode();
		}
		ResultSet rs;
		OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			rs = db.executeQuery(sql);
			while (rs.next()) {
				String name = db.getString(rs, "NAME");
				String oid = db.getString(rs, "OID");
				if ((name != null) && name.isEmpty() == false) {
					if (name.compareToIgnoreCase("nodeID") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setNodeID(oid);
						}
					} else if (name.compareToIgnoreCase("nodeIpAddr") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setNodeIpAddr(oid);
						}
					} else if (name.compareToIgnoreCase("nodeState") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setNodeState(oid);
						}
					} else if (name.compareToIgnoreCase("nodeName") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setNodeName(oid);
						}
					} else if (name.compareToIgnoreCase("nodeAvail") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setNodeAvail(oid);
						}
					} else if (name.compareToIgnoreCase("nodeStatus") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setNodeStatus(oid);
						}
					} else if (name.compareToIgnoreCase("nodePort") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setNodePort(oid);
						}
					} else if (name.compareToIgnoreCase("nodeInterval") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setNodeInterval(oid);
						}
					} else if (name.compareToIgnoreCase("nodeFailRetry") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setNodeFailRetry(oid);
						}
					} else if (name.compareToIgnoreCase("nodeBackup") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setNodeBackup(oid);
						}
					} else if (name.compareToIgnoreCase("nodeWeight") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setNodeWeight(oid);
						}
					} else if (name.compareToIgnoreCase("nodeMaxConns") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setNodeMaxConns(oid);
						}
					} else if (name.compareToIgnoreCase("nodeMaxConMode") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setNodeMaxConMode(oid);
						}
					} else if (name.compareToIgnoreCase("nodeTimeout") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setNodeTimeout(oid);
						}
					}
				}
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY, String.format("%s, sqlText:%s", e.getMessage(), sql));
		} catch (Exception e) {
			throw new OBException(e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
		return curOids;
	}

	public DtoOidCfgPool getCfgPool(Integer vendorType, String swVersion) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
				String.format("start. vendorType:%d, swVersion:%s", vendorType, swVersion));

		String version = VERSION_DEFAULT;
		if (swVersion != null && swVersion.isEmpty() == false) {
			version = swVersion;
		}
		String sqlText;

		try { // default OID를 깐다.
			sqlText = String.format("SELECT NAME, OID FROM MNG_SNMPOID WHERE VENDOR=%d AND SWVERSION=%s AND TYPE=%d;",
					vendorType, OBParser.sqlString(VERSION_DEFAULT), OID_TYPE_CFG_POOL);
		} catch (Exception e) {
			throw new OBException(e.getMessage());
		}

		DtoOidCfgPool oids = getCfgPoolData(null, sqlText); // default를 구해옴

		// 기본을 깔았으니, 특정 버전에 별도 준비한 OID 해당하는 것이 있으면 덮어쓴다.
		if (version.equals(VERSION_DEFAULT) == false) // default가 아닌 버전이면 업데이트할 oid를 찾는다.
		{
			try {
				sqlText = String.format(
						" SELECT NAME, OID FROM MNG_SNMPOID  " + " WHERE VENDOR=%d AND TYPE=%d        "
								+ "     AND (NAME, SWVERSION) IN       "
								+ "     ( SELECT NAME, MAX(SWVERSION) FROM MNG_SNMPOID "
								+ "       WHERE VENDOR=%d AND TYPE=%d AND SWVERSION <> %s AND SWVERSION <= %s "
								+ "       GROUP BY NAME ) ",
						vendorType, OID_TYPE_CFG_POOL, vendorType, OID_TYPE_CFG_POOL,
						OBParser.sqlString(VERSION_DEFAULT), OBParser.sqlString(version));
			} catch (Exception e) {
				throw new OBException(e.getMessage());
			}
			oids = getCfgPoolData(oids, sqlText);
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", oids));
		return oids;
	}

	private DtoOidCfgPool getCfgPoolData(DtoOidCfgPool curOids, String sql) throws OBException {
		if (curOids == null) // 업데이트가 아니라 처음 조회면 객체를 만든다.
		{
			curOids = new DtoOidCfgPool();
		}
		ResultSet rs;

		OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			rs = db.executeQuery(sql);
			while (rs.next()) {
				String name = db.getString(rs, "NAME");
				String oid = db.getString(rs, "OID");
				if (name != null && name.isEmpty() == false) {
					if (name.compareToIgnoreCase("poolID") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setPoolID(oid);
						}
					} else if (name.compareToIgnoreCase("poolName") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setPoolName(oid);
						}
					} else if (name.compareToIgnoreCase("poolLbMethod") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setPoolLBMethod(oid);
						}
					} else if (name.compareToIgnoreCase("poolMemberID") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setPoolMemberID(oid);
						}
					} else if (name.compareToIgnoreCase("poolHealthCheck") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setPoolHealthCheck(oid);
						}
					} else if (name.compareToIgnoreCase("poolMemberState") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setPoolMemberState(oid);
						}
					} else if (name.compareToIgnoreCase("groupBackup") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setPoolBackup(oid);
						}
					} else if (name.compareToIgnoreCase("groupRealBackup") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setPoolRealBackup(oid);
						}
					}
				}
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY, String.format("%s, sqlText:%s", e.getMessage(), sql));
		} catch (Exception e) {
			throw new OBException(e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
		return curOids;
	}

	public DtoOidCfgVirtServer getCfgVirtServer(Integer vendorType, String swVersion) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
				String.format("start. vendorType:%d, swVersion:%s", vendorType, swVersion));
		String version = VERSION_DEFAULT;
		if (swVersion != null && swVersion.isEmpty() == false) {
			version = swVersion;
		}
		String sqlText;

		try {
			sqlText = String.format("SELECT NAME, OID FROM MNG_SNMPOID WHERE VENDOR=%d AND SWVERSION=%s AND TYPE=%d ",
					vendorType, OBParser.sqlString(VERSION_DEFAULT), OID_TYPE_CFG_VSERVER);
		} catch (Exception e) {
			throw new OBException(e.getMessage());
		}
		DtoOidCfgVirtServer oids = getCfgVirtServerData(null, sqlText); // default를 구해옴

		// 기본을 깔았으니, 특정 버전에 별도 준비한 OID가 있으면 덮어쓴다.
		if (version.equals(VERSION_DEFAULT) == false) // default가 아닌 버전이면 업데이트할 oid를 찾는다.
		{
			try {
				sqlText = String.format(" SELECT NAME, OID FROM MNG_SNMPOID " + " WHERE VENDOR=%d AND TYPE=%d       "
						+ "     AND (NAME, SWVERSION ) IN     " + "     ( SELECT NAME, MAX(SWVERSION) FROM MNG_SNMPOID "
						+ "       WHERE VENDOR=%d AND TYPE=%d AND SWVERSION <> %s AND SWVERSION <= %s "
						+ "       GROUP BY NAME ) ", vendorType, OID_TYPE_CFG_VSERVER, vendorType, OID_TYPE_CFG_VSERVER,
						OBParser.sqlString(VERSION_DEFAULT), OBParser.sqlString(version));

				OBSystemLog.sql(OBDefine.LOGFILE_DEBUG, String.format("sqlText:%s", sqlText));
			} catch (Exception e) {
				throw new OBException(e.getMessage());
			}
			oids = getCfgVirtServerData(oids, sqlText);
		}

		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", oids));
		return oids;
	}

	private DtoOidCfgVirtServer getCfgVirtServerData(DtoOidCfgVirtServer curOids, String sql) throws OBException {
		if (curOids == null) // 업데이트가 아니라 처음 조회면 객체를 만든다.
		{
			curOids = new DtoOidCfgVirtServer();
		}
		ResultSet rs;
		OBDatabase db = new OBDatabase();
		try {
			db.openDB();

			rs = db.executeQuery(sql);
			while (rs.next()) {
				String name = db.getString(rs, "NAME");
				String oid = db.getString(rs, "OID");
				if (name != null && name.isEmpty() == false) {
					if (name.compareToIgnoreCase("vsID") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setVsID(oid);
						}
					} else if (name.compareToIgnoreCase("vsIpAddress") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setVsIpAddress(oid);
						}
					} else if (name.compareToIgnoreCase("vsName") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setVsName(oid);
						}
					} else if (name.compareToIgnoreCase("vsAvail") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setVsAvail(oid);
						}
					} else if (name.compareToIgnoreCase("vsStatus") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setVsStatus(oid);
						}
					} else if (name.compareToIgnoreCase("vsState") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setVsState2(oid);
						}
					} else if (name.compareToIgnoreCase("vsVirtPort") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setVsVirtPort(oid);
						}
					} else if (name.compareToIgnoreCase("vsSrcNwClass") == 0) // Alteon v29부터
					{
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setVsSrcNwClass(oid);
						}
					}
				}
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY, String.format("%s, sqlText:%s", e.getMessage(), sql));
		} catch (Exception e) {
			throw new OBException(e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
		return curOids;
	}

	public DtoOidCfgVirtService getCfgVirtService(Integer vendorType, String swVersion) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
				String.format("start. vendorType:%d, swVersion:%s", vendorType, swVersion));
		String version = VERSION_DEFAULT;
		if (swVersion != null && swVersion.isEmpty() == false) {
			version = swVersion;
		}

		String sqlText;
		try {
			sqlText = String.format("SELECT NAME, OID FROM MNG_SNMPOID WHERE VENDOR=%d AND SWVERSION=%s AND TYPE=%d;",
					vendorType, OBParser.sqlString(VERSION_DEFAULT), OID_TYPE_CFG_VSERVICE);
		} catch (Exception e) {
			throw new OBException(e.getMessage());
		}
		DtoOidCfgVirtService oids = getCfgVirtServiceData(null, sqlText);
		if (version.equals(VERSION_DEFAULT) == false) // default가 아닌 버전이면 업데이트할 oid를 찾는다.
		{
			try {
				sqlText = String.format(" SELECT NAME, OID FROM MNG_SNMPOID " + " WHERE VENDOR=%d AND TYPE=%d       "
						+ "     AND (NAME, SWVERSION ) IN     " + "     ( SELECT NAME, MAX(SWVERSION) FROM MNG_SNMPOID "
						+ "       WHERE VENDOR=%d AND TYPE=%d AND SWVERSION <> %s AND SWVERSION <= %s "
						+ "       GROUP BY NAME ) ", vendorType, OID_TYPE_CFG_VSERVICE, vendorType,
						OID_TYPE_CFG_VSERVICE, OBParser.sqlString(VERSION_DEFAULT), OBParser.sqlString(version));

				OBSystemLog.sql(OBDefine.LOGFILE_DEBUG, String.format("sqlText:%s", sqlText));
			} catch (Exception e) {
				throw new OBException(e.getMessage());
			}
			oids = getCfgVirtServiceData(oids, sqlText);
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", oids));
		return oids;
	}

	private DtoOidCfgVirtService getCfgVirtServiceData(DtoOidCfgVirtService curOids, String sql) throws OBException {
		if (curOids == null) // 업데이트가 아니라 처음 조회면 객체를 만든다.
		{
			curOids = new DtoOidCfgVirtService();
		}
		ResultSet rs;
		OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			rs = db.executeQuery(sql);
			while (rs.next()) {
				String name = db.getString(rs, "NAME");
				String oid = db.getString(rs, "OID");
				if (name != null && name.isEmpty() == false) {
					if (name.compareToIgnoreCase("vsID") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setVsrvID(oid);
						}
					} else if (name.compareToIgnoreCase("vsrvIndex") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setVsrvIndex(oid);
						}
					} else if (name.compareToIgnoreCase("vsrvVirtPort") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setVsrvVirtPort(oid);
						}
					} else if (name.compareToIgnoreCase("vsrvRealPort") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setVsrvRealPort(oid);
						}
					} else if (name.compareToIgnoreCase("vsrvPoolID") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setVsrvPoolID(oid);
						}
					} else if (name.compareToIgnoreCase("vsrvStatus") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setVsrvStatus(oid);
						}
					}
				}
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY, String.format("%s, sqlText:%s", e.getMessage(), sql));
		} catch (Exception e) {
			throw new OBException(e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
		return curOids;
	}

	// Alteon group 트래픽 oid 가져오기. 함수 구조상 vendor를 받지만 Alteon만 호출된다. FLB group 모니터링에서.
	public ArrayList<DtoOidEntity> getStatPoolGroup(Integer vendorType, String swVersion) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
				String.format("start. vendorType:%d, swVersion:%s", vendorType, swVersion));
		String version = VERSION_DEFAULT;
		if (swVersion != null && swVersion.isEmpty() == false) {
			version = swVersion;
		}
		String sqlText;
		try {
			sqlText = String.format("SELECT NAME, OID FROM MNG_SNMPOID WHERE VENDOR=%d AND SWVERSION=%s AND TYPE=%d;",
					vendorType, OBParser.sqlString(VERSION_DEFAULT), OID_TYPE_STAT_POOLGROUP);
		} catch (Exception e) {
			throw new OBException(e.getMessage());
		}
		ArrayList<DtoOidEntity> oids = getOidData(null, sqlText); // default를 구해옴
		if (version.equals(VERSION_DEFAULT) == false) // default가 아닌 버전이면 업데이트할 oid를 찾는다.
		{
			try {
				sqlText = String.format(" SELECT NAME, OID FROM MNG_SNMPOID " + " WHERE VENDOR=%d AND TYPE=%d       "
						+ "     AND (NAME, SWVERSION ) IN     " + "     ( SELECT NAME, MAX(SWVERSION) FROM MNG_SNMPOID "
						+ "       WHERE VENDOR=%d AND TYPE=%d AND SWVERSION <> %s AND SWVERSION <= %s "
						+ "       GROUP BY NAME ) ", vendorType, OID_TYPE_STAT_POOLGROUP, vendorType,
						OID_TYPE_STAT_POOLGROUP, OBParser.sqlString(VERSION_DEFAULT), OBParser.sqlString(version));

				OBSystemLog.sql(OBDefine.LOGFILE_DEBUG, String.format("sqlText:%s", sqlText));
			} catch (Exception e) {
				throw new OBException(e.getMessage());
			}
			oids = getOidData(oids, sqlText);
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", oids));
		return oids;
	}

	// Alteon real 트래픽 oid 가져오기. 함수 구조상 vendor를 받지만Alteon만 호출된다. FLB group 모니터링에서.
	public ArrayList<DtoOidEntity> getStatNode(Integer vendorType, String swVersion) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
				String.format("start. vendorType:%d, swVersion:%s", vendorType, swVersion));
		String version = VERSION_DEFAULT;
		if (swVersion != null && swVersion.isEmpty() == false) {
			version = swVersion;
		}
		String sqlText;
		try {
			sqlText = String.format("SELECT NAME, OID FROM MNG_SNMPOID WHERE VENDOR=%d AND SWVERSION=%s AND TYPE=%d;",
					vendorType, OBParser.sqlString(VERSION_DEFAULT), OID_TYPE_STAT_NODE);
		} catch (Exception e) {
			throw new OBException(e.getMessage());
		}
		ArrayList<DtoOidEntity> oids = getOidData(null, sqlText); // default를 구해옴
		if (version.equals(VERSION_DEFAULT) == false) // default가 아닌 버전이면 업데이트할 oid를 찾는다.
		{
			try {
				sqlText = String.format(" SELECT NAME, OID FROM MNG_SNMPOID " + " WHERE VENDOR=%d AND TYPE=%d       "
						+ "     AND (NAME, SWVERSION ) IN     " + "     ( SELECT NAME, MAX(SWVERSION) FROM MNG_SNMPOID "
						+ "       WHERE VENDOR=%d AND TYPE=%d AND SWVERSION <> %s AND SWVERSION <= %s "
						+ "       GROUP BY NAME ) ", vendorType, OID_TYPE_STAT_NODE, vendorType, OID_TYPE_STAT_NODE,
						OBParser.sqlString(VERSION_DEFAULT), OBParser.sqlString(version));

				OBSystemLog.sql(OBDefine.LOGFILE_DEBUG, String.format("sqlText:%s", sqlText));
			} catch (Exception e) {
				throw new OBException(e.getMessage());
			}
			oids = getOidData(oids, sqlText);
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", oids));
		return oids;
	}

	public DtoOidStatVirtServer getStatVirtServer(Integer vendorType, String swVersion) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
				String.format("start. vendorType:%d, swVersion:%s", vendorType, swVersion));
		String version = VERSION_DEFAULT;
		if (swVersion != null && swVersion.isEmpty() == false) {
			version = swVersion;
		}
		String sqlText;
		try {
			sqlText = String.format("SELECT NAME, OID FROM MNG_SNMPOID WHERE VENDOR=%d AND SWVERSION=%s AND TYPE=%d;",
					vendorType, OBParser.sqlString(VERSION_DEFAULT), OID_TYPE_STAT_VSERVER);
		} catch (Exception e) {
			throw new OBException(e.getMessage());
		}
		DtoOidStatVirtServer oids = getStatVirtServerData(null, sqlText); // default를 구해옴
		if (version.equals(VERSION_DEFAULT) == false) // default가 아닌 버전이면 업데이트할 oid를 찾는다.
		{
			try {
				sqlText = String.format(" SELECT NAME, OID FROM MNG_SNMPOID " + " WHERE VENDOR=%d AND TYPE=%d       "
						+ "     AND (NAME, SWVERSION ) IN     " + "     ( SELECT NAME, MAX(SWVERSION) FROM MNG_SNMPOID "
						+ "       WHERE VENDOR=%d AND TYPE=%d AND SWVERSION <> %s AND SWVERSION <= %s "
						+ "       GROUP BY NAME ) ", vendorType, OID_TYPE_STAT_VSERVER, vendorType,
						OID_TYPE_STAT_VSERVER, OBParser.sqlString(VERSION_DEFAULT), OBParser.sqlString(version));

				OBSystemLog.sql(OBDefine.LOGFILE_DEBUG, String.format("sqlText:%s", sqlText));
			} catch (Exception e) {
				throw new OBException(e.getMessage());
			}
			oids = getStatVirtServerData(oids, sqlText);
		}
		return oids;
	}

	private DtoOidStatVirtServer getStatVirtServerData(DtoOidStatVirtServer curOids, String sql) throws OBException {
		if (curOids == null) // 업데이트가 아니라 처음 조회면 객체를 만든다.
		{
			curOids = new DtoOidStatVirtServer();
		}
		ResultSet rs;
		OBDatabase db = new OBDatabase();
		try {
			db.openDB();

			rs = db.executeQuery(sql);
			while (rs.next()) {
				String name = db.getString(rs, "NAME");
				String oid = db.getString(rs, "OID");
				if (name != null && name.isEmpty() == false) {
					if (name.compareToIgnoreCase("statPktsIn") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setPktsIn(oid);
						}
					} else if (name.compareToIgnoreCase("statBytesIn") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setBytesIn(oid);
						}
					} else if (name.compareToIgnoreCase("statPktsOut") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setPktsOut(oid);
						}
					} else if (name.compareToIgnoreCase("statBytesOut") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setBytesOut(oid);
						}
					} else if (name.compareToIgnoreCase("statMaxConns") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setMaxConns(oid);
						}
					} else if (name.compareToIgnoreCase("statTotConns") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setTotConns(oid);
						}
					} else if (name.compareToIgnoreCase("statCurConns") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setCurConns(oid);
						}
					} else if (name.compareToIgnoreCase("vsAvailState") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setStatus(oid);
						}
					} else if (name.compareToIgnoreCase("vsID") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setVsID(oid);
						}
					} else if (name.compareToIgnoreCase("vsName") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setVsName(oid);
						}
					} else if (name.compareToIgnoreCase("vsIpAddress") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setVsIPAddress(oid);
						}
					} else if (name.compareToIgnoreCase("vsEnabledState") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setEnabled(oid);
						}
					}
				}
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY, String.format("%s, sqlText:%s", e.getMessage(), sql));
		} catch (Exception e) {
			throw new OBException(e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
		return curOids;
	}

	public DtoOidStatPoolMembers getStatPoolMembers(Integer vendorType, String swVersion) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
				String.format("start. vendorType:%d, swVersion:%s", vendorType, swVersion));
		String version = VERSION_DEFAULT;
		if (swVersion != null && swVersion.isEmpty() == false) {
			version = swVersion;
		}
		String sqlText;
		try {
			sqlText = String.format(" SELECT NAME, OID FROM MNG_SNMPOID WHERE VENDOR=%d AND SWVERSION=%s AND TYPE=%d;",
					vendorType, OBParser.sqlString(VERSION_DEFAULT), OID_TYPE_STAT_POOLMEMBER);
		} catch (Exception e) {
			throw new OBException(e.getMessage());
		}
		DtoOidStatPoolMembers oids = getStatPoolMembersData(null, sqlText); // default를 구해옴
		if (version.equals(VERSION_DEFAULT) == false) // default가 아닌 버전이면 업데이트할 oid를 찾는다.
		{
			try {
				sqlText = String.format(" SELECT NAME, OID FROM MNG_SNMPOID " + " WHERE VENDOR=%d AND TYPE=%d       "
						+ "     AND (NAME, SWVERSION ) IN     " + "     ( SELECT NAME, MAX(SWVERSION) FROM MNG_SNMPOID "
						+ "       WHERE VENDOR=%d AND TYPE=%d AND SWVERSION <> %s AND SWVERSION <= %s "
						+ "       GROUP BY NAME ) ", vendorType, OID_TYPE_STAT_POOLMEMBER, vendorType,
						OID_TYPE_STAT_POOLMEMBER, OBParser.sqlString(VERSION_DEFAULT), OBParser.sqlString(version));

				OBSystemLog.sql(OBDefine.LOGFILE_DEBUG, String.format("sqlText:%s", sqlText));
			} catch (Exception e) {
				throw new OBException(e.getMessage());
			}
			oids = getStatPoolMembersData(oids, sqlText);
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", oids));
		return oids;
	}

	private DtoOidStatPoolMembers getStatPoolMembersData(DtoOidStatPoolMembers curOids, String sql) throws OBException {
		if (curOids == null) // 업데이트가 아니라 처음 조회면 객체를 만든다.
		{
			curOids = new DtoOidStatPoolMembers();
		}
		ResultSet rs;
		OBDatabase db = new OBDatabase();
		try {
			db.openDB();

			rs = db.executeQuery(sql);
			while (rs.next()) {
				String name = db.getString(rs, "NAME");
				String oid = db.getString(rs, "OID");
				if (name != null && name.isEmpty() == false) {
					if (name.compareToIgnoreCase("statPktsIn") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setPktsIn(oid);
						}
					} else if (name.compareToIgnoreCase("statBytesIn") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setBytesIn(oid);
						}
					} else if (name.compareToIgnoreCase("statPktsOut") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setPktsOut(oid);
						}
					} else if (name.compareToIgnoreCase("statBytesOut") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setBytesOut(oid);
						}
					} else if (name.compareToIgnoreCase("statMaxConns") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setMaxConns(oid);
						}
					} else if (name.compareToIgnoreCase("statTotConns") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setTotConns(oid);
						}
					} else if (name.compareToIgnoreCase("statCurConns") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setCurConns(oid);
						}
					} else if (name.compareToIgnoreCase("nodeIPAddress") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setNodeIPAddress(oid);
						}
					}
				}
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY, String.format("%s, sqlText:%s", e.getMessage(), sql));
		} catch (Exception e) {
			throw new OBException(e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
		return curOids;
	}

	public DtoOidStatVirtService getStatVirtService(Integer vendorType, String swVersion) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
				String.format("start. vendorType:%d, swVersion:%s", vendorType, swVersion));
		String version = VERSION_DEFAULT;
		if (swVersion != null && swVersion.isEmpty() == false) {
			version = swVersion;
		}
		String sqlText;
		try {
			sqlText = String.format(" SELECT NAME, OID FROM MNG_SNMPOID WHERE VENDOR=%d AND SWVERSION=%s AND TYPE=%d;",
					vendorType, OBParser.sqlString(VERSION_DEFAULT), OID_TYPE_STAT_VSERVICE);
		} catch (Exception e) {
			throw new OBException(e.getMessage());
		}
		DtoOidStatVirtService oids = getStatVirtServiceData(null, sqlText); // default를 구해옴
		if (version.equals(VERSION_DEFAULT) == false) // default가 아닌 버전이면 업데이트할 oid를 찾는다.
		{
			try {
				sqlText = String.format(" SELECT NAME, OID FROM MNG_SNMPOID " + " WHERE VENDOR=%d AND TYPE=%d       "
						+ "     AND (NAME, SWVERSION ) IN     " + "     ( SELECT NAME, MAX(SWVERSION) FROM MNG_SNMPOID "
						+ "       WHERE VENDOR=%d AND TYPE=%d AND SWVERSION <> %s AND SWVERSION <= %s "
						+ "       GROUP BY NAME ) ", vendorType, OID_TYPE_STAT_VSERVICE, vendorType,
						OID_TYPE_STAT_VSERVICE, OBParser.sqlString(VERSION_DEFAULT), OBParser.sqlString(version));

				OBSystemLog.sql(OBDefine.LOGFILE_DEBUG, String.format("sqlText:%s", sqlText));
			} catch (Exception e) {
				throw new OBException(e.getMessage());
			}
			oids = getStatVirtServiceData(oids, sqlText);
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", oids));
		return oids;
	}

	private DtoOidStatVirtService getStatVirtServiceData(DtoOidStatVirtService curOids, String sql) throws OBException {
		if (curOids == null) // 업데이트가 아니라 처음 조회면 객체를 만든다.
		{
			curOids = new DtoOidStatVirtService();
		}
		ResultSet rs;
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			rs = db.executeQuery(sql);
			while (rs.next()) {
				String name = db.getString(rs, "NAME");
				String oid = db.getString(rs, "OID");
				if (name != null && name.isEmpty() == false) {
					if (name.compareToIgnoreCase("statPktsIn") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setPktsIn(oid);
						}
					} else if (name.compareToIgnoreCase("statBytesIn") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setBytesIn(oid);
						}
					} else if (name.compareToIgnoreCase("statPktsOut") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setPktsOut(oid);
						}
					} else if (name.compareToIgnoreCase("statBytesOut") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setBytesOut(oid);
						}
					} else if (name.compareToIgnoreCase("statMaxConns") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setMaxConns(oid);
						}
					} else if (name.compareToIgnoreCase("statTotConns") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setTotConns(oid);
						}
					} else if (name.compareToIgnoreCase("statCurConns") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setCurConns(oid);
						}
					} else if (name.compareToIgnoreCase("srvPort") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setSrvPort(oid);
						}
					}
				}
			}
		} catch (Exception e) {
			throw new OBException(e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
		return curOids;
	}

	public DtoOidStateVirtService getStateVirtService(Integer vendorType, String swVersion) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
				String.format("start. vendorType:%d, swVersion:%s", vendorType, swVersion));
		String version = VERSION_DEFAULT;
		if (swVersion != null && swVersion.isEmpty() == false) {
			version = swVersion;
		}

		String sqlText;
		try {
			sqlText = String.format(" SELECT NAME, OID FROM MNG_SNMPOID WHERE VENDOR=%d AND SWVERSION=%s AND TYPE=%d;",
					vendorType, OBParser.sqlString(VERSION_DEFAULT), OID_TYPE_STATE_VSERVICE);
		} catch (Exception e) {
			throw new OBException(e.getMessage());
		}
		DtoOidStateVirtService oids = getStateVirtServiceData(null, sqlText); // default를 구해옴
		if (version.equals(VERSION_DEFAULT) == false) // default가 아닌 버전이면 업데이트할 oid를 찾는다.
		{
			try {
				sqlText = String.format(" SELECT NAME, OID FROM MNG_SNMPOID " + " WHERE VENDOR=%d AND TYPE=%d       "
						+ "     AND (NAME, SWVERSION ) IN     " + "     ( SELECT NAME, MAX(SWVERSION) FROM MNG_SNMPOID "
						+ "       WHERE VENDOR=%d AND TYPE=%d AND SWVERSION <> %s AND SWVERSION <= %s "
						+ "       GROUP BY NAME ) ", vendorType, OID_TYPE_STATE_VSERVICE, vendorType,
						OID_TYPE_STATE_VSERVICE, OBParser.sqlString(VERSION_DEFAULT), OBParser.sqlString(version));

				OBSystemLog.sql(OBDefine.LOGFILE_DEBUG, String.format("sqlText:%s", sqlText));
			} catch (Exception e) {
				throw new OBException(e.getMessage());
			}
			oids = getStateVirtServiceData(oids, sqlText);
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", oids));
		return oids;
	}

	private DtoOidStateVirtService getStateVirtServiceData(DtoOidStateVirtService curOids, String sql)
			throws OBException {
		if (curOids == null) // 업데이트가 아니라 처음 조회면 객체를 만든다.
		{
			curOids = new DtoOidStateVirtService();
		}
		ResultSet rs;
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			rs = db.executeQuery(sql);

			while (rs.next()) {
				String name = db.getString(rs, "NAME");
				String oid = db.getString(rs, "OID");
				if ((name != null) && (name.isEmpty() == false)
						&& (name.compareToIgnoreCase("virtServiceState") == 0)) {
					if ((oid != null) && (oid.isEmpty() == false)) {
						curOids.setVirtServiceState(oid);
					}
				}
			}
		} catch (Exception e) {
			throw new OBException(e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
		return curOids;
	}

	public DtoOidInfoSystem getInfoSystem(Integer vendorType, String swVersion) throws OBException {
		String version = VERSION_DEFAULT;
		if (swVersion != null && swVersion.isEmpty() == false) {
			version = swVersion;
		}
		String sqlText;
		try {
			sqlText = String.format(" SELECT NAME, OID FROM MNG_SNMPOID WHERE SWVERSION=%s AND TYPE=%d;",
					OBParser.sqlString(VERSION_DEFAULT), OID_TYPE_INFO_SYSTEM);
		} catch (Exception e) {
			throw new OBException(e.getMessage());
		}

		DtoOidInfoSystem oids = getInfoSystemData(null, sqlText); // default를 구해옴

		if (version.equals(VERSION_DEFAULT) == false) // default가 아닌 버전이면 업데이트할 oid를 찾는다.
		{
			try {
				sqlText = String.format(" SELECT NAME, OID FROM MNG_SNMPOID " + " WHERE VENDOR=%d AND TYPE=%d       "
						+ "     AND (NAME, SWVERSION ) IN     " + "     ( SELECT NAME, MAX(SWVERSION) FROM MNG_SNMPOID "
						+ "       WHERE VENDOR=%d AND TYPE=%d AND SWVERSION <> %s AND SWVERSION <= %s "
						+ "       GROUP BY NAME ) ", vendorType, OID_TYPE_INFO_SYSTEM, vendorType, OID_TYPE_INFO_SYSTEM,
						OBParser.sqlString(VERSION_DEFAULT), OBParser.sqlString(version));

				OBSystemLog.sql(OBDefine.LOGFILE_DEBUG, String.format("sqlText:%s", sqlText));
			} catch (Exception e) {
				throw new OBException(e.getMessage());
			}
			oids = getInfoSystemData(oids, sqlText);
		}

		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", oids));
		return oids;
	}

	private DtoOidInfoSystem getInfoSystemData(DtoOidInfoSystem curOids, String sql) throws OBException {
		OBDatabase db = new OBDatabase();

		if (curOids == null) // 업데이트가 아니라 처음 조회면 객체를 만든다.
		{
			curOids = new DtoOidInfoSystem();
		}
		ResultSet rs;
		try {
			db.openDB();

			rs = db.executeQuery(sql);

			while (rs.next()) {
				String name = db.getString(rs, "NAME");
				String oid = db.getString(rs, "OID");
				if (name != null && name.isEmpty() == false) {
					if (name.compareToIgnoreCase("sysDescr") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setSysDescr(oid);
						}
					} else if (name.compareToIgnoreCase("SysObjectID") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setSysObjectID(oid);
						}
					} else if (name.compareToIgnoreCase("sysUpTime") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setSysUpTime(oid);
						}
					} else if (name.compareToIgnoreCase("sysCpuIdle") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setSysCpuIdle(oid);
						}
					} else if (name.compareToIgnoreCase("sysMemTotal") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setSysMemTotal(oid);
						}
					} else if (name.compareToIgnoreCase("sysMemAvail") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setSysMemAvail(oid);
						}
					} else if (name.compareToIgnoreCase("sysMemBuffer") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setSysMemBuffered(oid);
						}
					} else if (name.compareToIgnoreCase("sysMemCached") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setSysMemCached(oid);
						}
					} else if (name.compareToIgnoreCase("sysModel") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setSysModel(oid);
						}
					} else if (name.compareToIgnoreCase("sysHddTotal") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setSysHddTotal(oid);
						}
					} else if (name.compareToIgnoreCase("sysHddUsed") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setSysHddUsed(oid);
						}
					} else if (name.compareToIgnoreCase("sysDiskPath") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setSysDiskPath(oid);
						}
					}
				}
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY, String.format("%s, sqlText:%s", e.getMessage(), sql));
		} catch (Exception e) {
			throw new OBException(e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
		return curOids;
	}

	// public static void main(String[] args)
	// {
	// OBDatabase db = new OBDatabase();
	// try
	// {
	// db.openDB();
	// }
	// catch(Exception e)
	// {
	// return;
	// }
	//
	// try
	// {
	// DtoOidInfoSystemResc resc = new
	// OBSnmpOidDB().getSystemResc(OBDefine.ADC_TYPE_ALTEON, "", db);
	// System.out.println(resc);
	// }
	// catch(OBException e)
	// {
	// e.printStackTrace();
	// }
	//
	// db.closeDB();
	// }

	public DtoOidInfoAdcResc getAdcResource(Integer vendorType, String swVersion) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
				String.format("start. vendorType:%d, swVersion:%s", vendorType, swVersion));
		String version = VERSION_DEFAULT;

		if (swVersion != null && swVersion.isEmpty() == false) {
			version = swVersion;
		}
		String sqlText;
		try {
			sqlText = String.format("SELECT NAME, OID FROM MNG_SNMPOID WHERE VENDOR=%d AND SWVERSION=%s AND TYPE=%d;",
					vendorType, OBParser.sqlString(VERSION_DEFAULT), OID_TYPE_INFO_ADCRESC);
		} catch (Exception e) {
			throw new OBException(e.getMessage());
		}

		DtoOidInfoAdcResc oids = getAdcResourceData(null, sqlText); // default를 구해옴
		if (version.equals(VERSION_DEFAULT) == false) // default가 아닌 버전이면 업데이트할 oid를 찾는다.
		{
			try {
				sqlText = String.format(" SELECT NAME, OID FROM MNG_SNMPOID " + " WHERE VENDOR=%d AND TYPE=%d       "
						+ "     AND (NAME, SWVERSION ) IN     " + "     ( SELECT NAME, MAX(SWVERSION) FROM MNG_SNMPOID "
						+ "       WHERE VENDOR=%d AND TYPE=%d AND SWVERSION <> %s AND SWVERSION <= %s "
						+ "       GROUP BY NAME ) ", vendorType, OID_TYPE_INFO_ADCRESC, vendorType,
						OID_TYPE_INFO_ADCRESC, OBParser.sqlString(VERSION_DEFAULT), OBParser.sqlString(version));

				OBSystemLog.sql(OBDefine.LOGFILE_DEBUG, String.format("sqlText:%s", sqlText));
			} catch (Exception e) {
				throw new OBException(e.getMessage());
			}
			oids = getAdcResourceData(oids, sqlText);
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", oids));
		return oids;
	}

	private DtoOidInfoAdcResc getAdcResourceData(DtoOidInfoAdcResc curOids, String sql) throws OBException {
		OBDatabase db = new OBDatabase();

		if (curOids == null) // 업데이트가 아니라 처음 조회면 객체를 만든다.
		{
			curOids = new DtoOidInfoAdcResc();
		}
		ResultSet rs;
		try {
			db.openDB();

			rs = db.executeQuery(sql);

			while (rs.next()) {
				String name = db.getString(rs, "NAME");
				String oid = db.getString(rs, "OID");
				if (name != null && name.isEmpty() == false) {
					if (name.compareToIgnoreCase("adcCpuIdle") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setAdcCpuIdle(oid);
						}
					} else if (name.compareToIgnoreCase("adcMemTotal") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setAdcMemTotal(oid);
						}
					} else if (name.compareToIgnoreCase("adcMemUsed") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setAdcMemUsed(oid);
						}
					} else if (name.compareToIgnoreCase("adcMemFree") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setAdcMemFree(oid);
						}
					} else if (name.compareToIgnoreCase("tmmMemTotal") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setTmmMemTotal(oid);
						}
					} else if (name.compareToIgnoreCase("tmmMemUsed") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setTmmMemUsed(oid);
						}
					} else if (name.compareToIgnoreCase("adcCurConns") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setAdcCurConns(oid);
						}
					} else if (name.compareToIgnoreCase("adcSlbCurConns") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setAdcSlbCurConns(oid);
						}
					} else if (name.compareToIgnoreCase("adcMaxConns") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setAdcMaxConns(oid);
						}
					} else if (name.compareToIgnoreCase("adcPktsIn") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setAdcPktsIn(oid);
						}
					} else if (name.compareToIgnoreCase("adcPktsOut") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setAdcPktsOut(oid);
						}
					} else if (name.compareToIgnoreCase("adcBytesIn") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setAdcBytesIn(oid);
						}
					} else if (name.compareToIgnoreCase("adcBytesOut") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setAdcBytesOut(oid);
						}
					} else if (name.compareToIgnoreCase("adcModel") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setAdcModel(oid);
						}
					} else if (name.compareToIgnoreCase("adcTypeModel") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setAdcTypeModel(oid);
						}
					} else if (name.compareToIgnoreCase("adcSwVersion") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setAdcSwVersion(oid);
						}
					} else if (name.compareToIgnoreCase("adcSerialNum") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setAdcSerailNum(oid);
						}
					} else if (name.compareToIgnoreCase("adcName") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setAdcName(oid);
						}
					} else if (name.compareToIgnoreCase("adcUpTime") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setAdcUpTime(oid);
						}
					} else if (name.compareToIgnoreCase("adcDescr") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setAdcDescr(oid);
						}
					} else if (name.compareToIgnoreCase("vsAvailState") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setVsAvaliableState(oid);
						}
					} else if (name.compareToIgnoreCase("vsEnabledState") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setVssEnabledState(oid);
						}
					} else if (name.compareToIgnoreCase("nodeAvailState") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setNodeAvaliableState(oid);
						}
					} else if (name.compareToIgnoreCase("nodeEnabledState") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setNodeEnabledState(oid);
						}
					} else if (name.compareToIgnoreCase("nodeState") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setNodeState(oid);
						}
					} else if (name.compareToIgnoreCase("serviceVirtPort") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setServiceVirtPort(oid);
						}
					} else if (name.compareToIgnoreCase("adcSPUsage") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setAdcSPUsage(oid);
						}
					} else if (name.compareToIgnoreCase("nodeIPAddress") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setNodeIPAddress(oid);
						}
					} else if (name.compareToIgnoreCase("adcMgmtSyslog") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setAdcMgmtSyslog(oid);
						}
					} else if (name.compareToIgnoreCase("adcMgmtTftp") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setAdcMgmtTftp(oid);
						}
					}
				}
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY, String.format("%s, sqlText:%s", e.getMessage(), sql));
		} catch (Exception e) {
			throw new OBException(e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
		return curOids;
	}

	public DtoOidInfoAdcPerformance getAdcPerformance(Integer vendorType, String swVersion) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
				String.format("start. vendorType:%d, swVersion:%s", vendorType, swVersion));

		String version = VERSION_DEFAULT;
		if (swVersion != null && swVersion.isEmpty() == false) {
			version = swVersion;
		}
		String sqlText;
		try {
			sqlText = String.format("SELECT NAME, OID FROM MNG_SNMPOID WHERE VENDOR=%d AND SWVERSION=%s AND TYPE=%d;",
					vendorType, OBParser.sqlString(VERSION_DEFAULT), OID_TYPE_PERFORMANCE);
			OBSystemLog.sql(OBDefine.LOGFILE_DEBUG, String.format("sqlText:%s", sqlText));
		} catch (Exception e) {
			throw new OBException(e.getMessage());
		}

		DtoOidInfoAdcPerformance oids = getAdcPerformanceData(null, sqlText); // default를 구해옴
		if (version.equals(VERSION_DEFAULT) == false) // default가 아닌 버전이면 업데이트할 oid를 찾는다.
		{
			try {
				sqlText = String.format(" SELECT NAME, OID FROM MNG_SNMPOID " + " WHERE VENDOR=%d AND TYPE=%d       "
						+ "     AND (NAME, SWVERSION ) IN     " + "     ( SELECT NAME, MAX(SWVERSION) FROM MNG_SNMPOID "
						+ "       WHERE VENDOR=%d AND TYPE=%d AND SWVERSION <> %s AND SWVERSION <= %s "
						+ "       GROUP BY NAME ) ", vendorType, OID_TYPE_PERFORMANCE, vendorType, OID_TYPE_PERFORMANCE,
						OBParser.sqlString(VERSION_DEFAULT), OBParser.sqlString(version));

				OBSystemLog.sql(OBDefine.LOGFILE_DEBUG, String.format("sqlText:%s", sqlText));
			} catch (Exception e) {
				throw new OBException(e.getMessage());
			}
			oids = getAdcPerformanceData(oids, sqlText);
		}

		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", oids));
		return oids;
	}

	private DtoOidInfoAdcPerformance getAdcPerformanceData(DtoOidInfoAdcPerformance curOids, String sql)
			throws OBException {
		if (curOids == null) // 업데이트가 아니라 처음 조회면 객체를 만든다.
		{
			curOids = new DtoOidInfoAdcPerformance();
		}
		ResultSet rs;
		OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			rs = db.executeQuery(sql);

			while (rs.next()) {
				String name = db.getString(rs, "NAME");
				String oid = db.getString(rs, "OID");
				if ((name != null) && (name.isEmpty() == false)) {
					if (name.compareToIgnoreCase("sysClientSslTotNativeConns") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setClientSslTotNativeConns(oid);
						}
					} else if (name.compareToIgnoreCase("sysClientSslTotCompactConns") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setClientSslTotCompactConns(oid);
						}
					} else if (name.compareToIgnoreCase("sysHttpStatGetReqs") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setHttpStatGetReqs(oid);
						}
					} else if (name.compareToIgnoreCase("sysStatClientCurConns") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setClientCurConns(oid);
						}
					} else if (name.compareToIgnoreCase("sysStatClientBytesIn") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setClientBytesIn(oid);
						}
					} else if (name.compareToIgnoreCase("sysStatClientBytesOut") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setClientBytesOut(oid);
						}
					} else if (name.compareToIgnoreCase("sysStatClientPktsIn") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setClientPktsIn(oid);
						}
					} else if (name.compareToIgnoreCase("sysStatClientPktsOut") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setClientPktsOut(oid);
						}
					}
				}
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY, String.format("%s, sqlText:%s", e.getMessage(), sql));
		} catch (Exception e) {
			throw new OBException(e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
		return curOids;
	}

	// public static void main(String[] args)
	// {
	// OBDatabase db = new OBDatabase();
	// try
	// {
	// db.openDB();
	// }
	// catch(Exception e)
	// {
	// return;
	// }
	//
	// try
	// {
	// DtoOidInfoSystemResc resc = new
	// OBSnmpOidDB().getSystemResc(OBDefine.ADC_TYPE_ALTEON, "", db);
	// System.out.println(resc);
	// }
	// catch(OBException e)
	// {
	// e.printStackTrace();
	// }
	//
	// db.closeDB();
	// }

	public DtoOidInfoHealthCheck getHealthCheckInfo(Integer vendorType, String swVersion) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
				String.format("start. vendorType:%d, swVersion:%s", vendorType, swVersion));
		String version = VERSION_DEFAULT;
		if (swVersion != null && swVersion.isEmpty() == false) {
			version = swVersion;
		}

		String sqlText;
		try { // default OID를 깐다.
			sqlText = String.format("SELECT NAME, OID FROM MNG_SNMPOID WHERE VENDOR=%d AND SWVERSION=%s AND TYPE=%d;",
					vendorType, OBParser.sqlString(VERSION_DEFAULT), OID_TYPE_CFG_HEALTHCHECK);
			OBSystemLog.sql(OBDefine.LOGFILE_DEBUG, String.format("sqlText:%s", sqlText));
		} catch (Exception e) {
			throw new OBException(e.getMessage());
		}

		DtoOidInfoHealthCheck oids = getHealthCheckInfoData(null, sqlText); // default를 구해옴
		// 기본을 깔았으니, 특정 버전에 별도 준비한 OID가 있으면 덮어쓴다.
		if (version.equals(VERSION_DEFAULT) == false) // default가 아닌 버전이면 업데이트할 oid를 찾는다.
		{
			try {
				sqlText = String.format(" SELECT NAME, OID FROM MNG_SNMPOID " + " WHERE VENDOR=%d AND TYPE=%d       "
						+ "     AND (NAME, SWVERSION ) IN     " + "     ( SELECT NAME, MAX(SWVERSION) FROM MNG_SNMPOID "
						+ "       WHERE VENDOR=%d AND TYPE=%d AND SWVERSION <> %s AND SWVERSION <= %s "
						+ "       GROUP BY NAME ) ", vendorType, OID_TYPE_CFG_HEALTHCHECK, vendorType,
						OID_TYPE_CFG_HEALTHCHECK, OBParser.sqlString(VERSION_DEFAULT), OBParser.sqlString(version));

				OBSystemLog.sql(OBDefine.LOGFILE_DEBUG, String.format("sqlText:%s", sqlText));
			} catch (Exception e) {
				throw new OBException(e.getMessage());
			}
			oids = getHealthCheckInfoData(oids, sqlText);
		}

		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", oids));
		return oids;
	}

	private DtoOidInfoHealthCheck getHealthCheckInfoData(DtoOidInfoHealthCheck curOids, String sql) throws OBException {
		if (curOids == null) // 업데이트가 아니라 처음 조회면 객체를 만든다.
		{
			curOids = new DtoOidInfoHealthCheck();
		}
		ResultSet rs;
		OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			rs = db.executeQuery(sql);
			while (rs.next()) {
				String name = db.getString(rs, "NAME");
				String oid = db.getString(rs, "OID");
				if (name != null && name.isEmpty() == false) {
					if (name.compareToIgnoreCase("slbHealthID") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setSlbHealthID(oid);
						}
					} else if (name.compareToIgnoreCase("slbHealthName") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setSlbHealthName(oid);
						}
					} else if (name.compareToIgnoreCase("slbHealthType") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setSlbHealthType(oid);
						}
					} else if (name.compareToIgnoreCase("slbHealthDestinationIp") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setSlbHealthDestinationIp(oid);
						}
					} else if (name.compareToIgnoreCase("slbHealthLogicalExpression") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setSlbHealthLogicalExpression(oid);
						}
					}
				}
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY, String.format("%s, sqlText:%s", e.getMessage(), sql));
		} catch (Exception e) {
			throw new OBException(e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
		return curOids;
	}

	public DtoOidInfoNetworkClass getNetworkClassInfo(Integer vendorType, String swVersion) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
				String.format("start. vendorType:%d, swVersion:%s", vendorType, swVersion));
		String version = VERSION_DEFAULT;
		if (swVersion != null && swVersion.isEmpty() == false) {
			version = swVersion;
		}
		String sqlText;

		try { // default OID를 깐다.
			sqlText = String.format("SELECT NAME, OID FROM MNG_SNMPOID WHERE VENDOR=%d AND SWVERSION=%s AND TYPE=%d;",
					vendorType, OBParser.sqlString(VERSION_DEFAULT), OID_TYPE_CFG_NETWORKCLASS);
		} catch (Exception e) {
			throw new OBException(e.getMessage());
		}
		DtoOidInfoNetworkClass oids = getNetworkClassInfoData(null, sqlText); // default를 구해옴

		if (version.equals(VERSION_DEFAULT) == false) // default가 아닌 버전이면 업데이트할 oid를 찾는다.
		{
			try {
				sqlText = String.format(" SELECT NAME, OID FROM MNG_SNMPOID " + " WHERE VENDOR=%d AND TYPE=%d       "
						+ "     AND (NAME, SWVERSION ) IN     " + "     ( SELECT NAME, MAX(SWVERSION) FROM MNG_SNMPOID "
						+ "       WHERE VENDOR=%d AND TYPE=%d AND SWVERSION <> %s AND SWVERSION <= %s "
						+ "       GROUP BY NAME ) ", vendorType, OID_TYPE_CFG_NETWORKCLASS, vendorType,
						OID_TYPE_CFG_NETWORKCLASS, OBParser.sqlString(VERSION_DEFAULT), OBParser.sqlString(version));

				OBSystemLog.sql(OBDefine.LOGFILE_DEBUG, String.format("sqlText:%s", sqlText));
			} catch (Exception e) {
				throw new OBException(e.getMessage());
			}
			oids = getNetworkClassInfoData(oids, sqlText);
		}

		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", oids));
		return oids;
	}

	private DtoOidInfoNetworkClass getNetworkClassInfoData(DtoOidInfoNetworkClass curOids, String sql)
			throws OBException {
		if (curOids == null) // 업데이트가 아니라 처음 조회면 객체를 만든다.
		{
			curOids = new DtoOidInfoNetworkClass();
		}
		ResultSet rs;
		OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			rs = db.executeQuery(sql);
			while (rs.next()) {
				String name = db.getString(rs, "NAME");
				String oid = db.getString(rs, "OID");
				if (name != null && name.isEmpty() == false) {
					if (name.compareToIgnoreCase("slbNwClassBase") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setSlbNwClassBase(oid);
						}
					} else if (name.compareToIgnoreCase("slbNwClassBaseID") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setSlbNwClassBaseID(oid);
						}
					} else if (name.compareToIgnoreCase("slbNwClassBaseName") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setSlbNwClassBaseName(oid);
						}
					} else if (name.compareToIgnoreCase("slbNwClassElement") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setSlbNwClassElement(oid);
						}
					} else if (name.compareToIgnoreCase("slbNwClassElementNetId") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setSlbNwClassElementNetId(oid);
						}
					} else if (name.compareToIgnoreCase("slbNwClassElementNetType") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setSlbNwClassElementNetType(oid);
						}
					} else if (name.compareToIgnoreCase("slbNwClassElementIpSingle") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setSlbNwClassElementIpSingle(oid);
						}
					} else if (name.compareToIgnoreCase("slbNwClassElementIpMask") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setSlbNwClassElementIpMask(oid);
						}
					} else if (name.compareToIgnoreCase("slbNwClassElementIpFrom") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setSlbNwClassElementIpFrom(oid);
						}
					} else if (name.compareToIgnoreCase("slbNwClassElementIpTo") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setSlbNwClassElementIpTo(oid);
						}
					} else if (name.compareToIgnoreCase("slbNwClassElementMatchType") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setSlbNwClassElementMatchType(oid);
						}
					} else if (name.compareToIgnoreCase("slbNwClassElementNwClassId") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setSlbNwClassElementNwClassId(oid);
						}
					}
				}
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY, String.format("%s, sqlText:%s", e.getMessage(), sql));
		} catch (Exception e) {
			throw new OBException(e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
		return curOids;
	}

	/**
	 * PAS 시스템에 대한 cfg 정보를 추출한다.
	 * 
	 * @param vendorType
	 * @param swVersion
	 * @param db
	 * @return
	 * @throws OBException
	 */
	public DtoOidInfoCfgPAS getCfgPAS(Integer vendorType, String swVersion) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
				String.format("start. vendorType:%d, swVersion:%s", vendorType, swVersion));
		String version = VERSION_DEFAULT;
		if (swVersion != null && swVersion.isEmpty() == false) {
			version = swVersion;
		}
		String sqlText;
		try {
			sqlText = String.format("SELECT NAME, OID FROM MNG_SNMPOID WHERE VENDOR=%d AND SWVERSION=%s AND TYPE=%d;",
					vendorType, OBParser.sqlString(VERSION_DEFAULT), OID_TYPE_CFG_PAS);

			OBSystemLog.sql(OBDefine.LOGFILE_DEBUG, String.format("sqlText:%s", sqlText));
		} catch (Exception e) {
			throw new OBException(e.getMessage());
		}

		DtoOidInfoCfgPAS oids = getCfgPASData(null, sqlText);
		if (version.equals(VERSION_DEFAULT) == false) // default가 아닌 버전이면 업데이트할 oid를 찾는다.
		{
			try {
				sqlText = String.format(" SELECT NAME, OID FROM MNG_SNMPOID " + " WHERE VENDOR=%d AND TYPE=%d       "
						+ "     AND (NAME, SWVERSION ) IN     " + "     ( SELECT NAME, MAX(SWVERSION) FROM MNG_SNMPOID "
						+ "       WHERE VENDOR=%d AND TYPE=%d AND SWVERSION <> %s AND SWVERSION <= %s "
						+ "       GROUP BY NAME ) ", vendorType, OID_TYPE_CFG_PAS, vendorType, OID_TYPE_CFG_PAS,
						OBParser.sqlString(VERSION_DEFAULT), OBParser.sqlString(version));

				OBSystemLog.sql(OBDefine.LOGFILE_DEBUG, String.format("sqlText:%s", sqlText));
			} catch (Exception e) {
				throw new OBException(e.getMessage());
			}
			oids = getCfgPASData(oids, sqlText);
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", oids)); // default를 구해옴
		return oids;
	}

	private DtoOidInfoCfgPAS getCfgPASData(DtoOidInfoCfgPAS curOids, String sql) throws OBException {
		if (curOids == null) // 업데이트가 아니라 처음 조회면 객체를 만든다.
		{
			curOids = new DtoOidInfoCfgPAS();
		}
		ResultSet rs;
		OBDatabase db = new OBDatabase();
		try {
			db.openDB();

			rs = db.executeQuery(sql);

			while (rs.next()) {
				String name = db.getString(rs, "NAME");
				String oid = db.getString(rs, "OID");
				if ((name != null) && (name.isEmpty() == false)) {
					if (name.compareToIgnoreCase("slbServiceName") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setSlbServiceName(oid);
						}
					} else if (name.compareToIgnoreCase("slbServiceVip") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setSlbServiceVip(oid);
						}
					} else if (name.compareToIgnoreCase("slbServiceVport") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setSlbServiceVport(oid);
						}
					} else if (name.compareToIgnoreCase("slbServiceLBMethod") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setSlbServiceLBMethod(oid);
						}
					} else if (name.compareToIgnoreCase("slbServiceState") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setSlbServiceState(oid);
						}
					} else if (name.compareToIgnoreCase("slbServiceActConn") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setSlbServiceActConn(oid);
						}
					} else if (name.compareToIgnoreCase("slbServiceInpps") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setSlbServiceInpps(oid);
						}
					} else if (name.compareToIgnoreCase("slbServiceOutpps") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setSlbServiceOutpps(oid);
						}
					} else if (name.compareToIgnoreCase("slbServiceInbps") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setSlbServiceInbps(oid);
						}
					} else if (name.compareToIgnoreCase("slbServiceOutbps") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setSlbServiceOutbps(oid);
						}
					} else if (name.compareToIgnoreCase("slbRealID") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setSlbRealID(oid);
						}
					} else if (name.compareToIgnoreCase("slbRealName") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setSlbRealName(oid);
						}
					} else if (name.compareToIgnoreCase("slbRealIP") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setSlbRealIP(oid);
						}
					} else if (name.compareToIgnoreCase("slbRealPort") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setSlbRealPort(oid);
						}
					} else if (name.compareToIgnoreCase("slbRealState") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setSlbRealState(oid);
						}
					} else if (name.compareToIgnoreCase("slbRealActStatus") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setSlbRealActStatus(oid);
						}
					} else if (name.compareToIgnoreCase("slbRealActConn") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setSlbRealActConn(oid);
						}
					} else if (name.compareToIgnoreCase("slbRealInpps") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setSlbRealInpps(oid);
						}
					} else if (name.compareToIgnoreCase("slbRealOutpps") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setSlbRealOutpps(oid);
						}
					} else if (name.compareToIgnoreCase("slbRealInbps") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setSlbRealInbps(oid);
						}
					} else if (name.compareToIgnoreCase("slbRealOutbps") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setSlbRealOutbps(oid);
						}
					} else if (name.compareToIgnoreCase("slbHealthID") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setSlbHealthID(oid);
						}
					} else if (name.compareToIgnoreCase("slbHealthType") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setSlbHealthType(oid);
						}
					}
				}
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY, String.format("%s, sqlText:%s", e.getMessage(), sql));
		} catch (Exception e) {
			throw new OBException(e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
		return curOids;
	}

	public DtoOidInfoCfgPASK getCfgPASK(Integer vendorType, String swVersion) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
				String.format("start. vendorType:%d, swVersion:%s", vendorType, swVersion));
		String version = VERSION_DEFAULT;
		if (swVersion != null && swVersion.isEmpty() == false) {
			version = swVersion;
		}
		String sqlText;
		try {
			sqlText = String.format("SELECT NAME, OID FROM MNG_SNMPOID WHERE VENDOR=%d AND SWVERSION=%s AND TYPE=%d;",
					vendorType, OBParser.sqlString(VERSION_DEFAULT), OID_TYPE_CFG_PASK);

			OBSystemLog.sql(OBDefine.LOGFILE_DEBUG, String.format("sqlText:%s", sqlText));
		} catch (Exception e) {
			throw new OBException(e.getMessage());
		}
		DtoOidInfoCfgPASK oids = getCfgPASKData(null, sqlText); // default를 구해옴

		if (version.equals(VERSION_DEFAULT) == false) // default가 아닌 버전이면 업데이트할 oid를 찾는다.
		{
			try {
				sqlText = String.format(" SELECT NAME, OID FROM MNG_SNMPOID " + " WHERE VENDOR=%d AND TYPE=%d       "
						+ "     AND (NAME, SWVERSION ) IN     " + "     ( SELECT NAME, MAX(SWVERSION) FROM MNG_SNMPOID "
						+ "       WHERE VENDOR=%d AND TYPE=%d AND SWVERSION <> %s AND SWVERSION <= %s "
						+ "       GROUP BY NAME ) ", vendorType, OID_TYPE_CFG_PASK, vendorType, OID_TYPE_CFG_PASK,
						OBParser.sqlString(VERSION_DEFAULT), OBParser.sqlString(version));

				OBSystemLog.sql(OBDefine.LOGFILE_DEBUG, String.format("sqlText:%s", sqlText));
			} catch (Exception e) {
				throw new OBException(e.getMessage());
			}
			oids = getCfgPASKData(oids, sqlText);
		}

		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", oids));
		return oids;
	}

	private DtoOidInfoCfgPASK getCfgPASKData(DtoOidInfoCfgPASK curOids, String sql) throws OBException {
		if (curOids == null) // 업데이트가 아니라 처음 조회면 객체를 만든다.
		{
			curOids = new DtoOidInfoCfgPASK();
		}
		ResultSet rs;
		OBDatabase db = new OBDatabase();
		try {
			db.openDB();

			rs = db.executeQuery(sql);
			while (rs.next()) {
				String name = db.getString(rs, "NAME");
				String oid = db.getString(rs, "OID");

				if ((name != null) && name.isEmpty() == false) {
					if (name.compareToIgnoreCase("slbServiceName") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setSlbServiceName(oid);
						}
					} else if (name.compareToIgnoreCase("slbServiceActConn") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setSlbServiceActConn(oid);
						}
					} else if (name.compareToIgnoreCase("slbServiceInpps") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setSlbServiceInpps(oid);
						}
					} else if (name.compareToIgnoreCase("slbServiceOutpps") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setSlbServiceOutpps(oid);
						}
					} else if (name.compareToIgnoreCase("slbServiceInbps") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setSlbServiceInbps(oid);
						}
					} else if (name.compareToIgnoreCase("slbServiceOutbps") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setSlbServiceOutbps(oid);
						}
					} else if (name.compareToIgnoreCase("slbRealActConn") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setSlbRealActConn(oid);
						}
					} else if (name.compareToIgnoreCase("slbRealInpps") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setSlbRealInpps(oid);
						}
					} else if (name.compareToIgnoreCase("slbRealOutpps") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setSlbRealOutpps(oid);
						}
					} else if (name.compareToIgnoreCase("slbRealInbps") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setSlbRealInbps(oid);
						}
					} else if (name.compareToIgnoreCase("slbRealOutbps") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setSlbRealOutbps(oid);
						}
					} else if (name.compareToIgnoreCase("resManCpuUsage") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setResManCpuUsage(oid);
						}
					} else if (name.compareToIgnoreCase("resManMemUsage") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setResManMemUsage(oid);
						}
					} else if (name.compareToIgnoreCase("resManMemTotal") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setResManMemTotal(oid);
						}
					} else if (name.compareToIgnoreCase("resManMemUsed") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setResManMemUsed(oid);
						}
					} else if (name.compareToIgnoreCase("resManMemFree") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setResManMemFree(oid);
						}
					} else if (name.compareToIgnoreCase("resPktCpuUsage") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setResPktCpuUsage(oid);
						}
					} else if (name.compareToIgnoreCase("resPktMemUsage") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setResPktMemUsage(oid);
						}
					} else if (name.compareToIgnoreCase("resPktMemTotal") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setResPktMemTotal(oid);
						}
					} else if (name.compareToIgnoreCase("resPktMemUsed") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setResPktMemUsed(oid);
						}
					} else if (name.compareToIgnoreCase("resPktMemFree") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setResPktMemFree(oid);
						}
					}
				}
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY, String.format("%s, sqlText:%s", e.getMessage(), sql));
		} catch (Exception e) {
			throw new OBException(e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
		return curOids;
	}

	public DtoOidInfoSessionTable getMaintSessionTableInfo(Integer vendorType, String swVersion) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
				String.format("start. vendorType:%d, swVersion:%s", vendorType, swVersion));
		String version = VERSION_DEFAULT;
		if (swVersion != null && swVersion.isEmpty() == false) {
			version = swVersion;
		}
		String sqlText;
		try {
			sqlText = String.format("SELECT NAME, OID FROM MNG_SNMPOID WHERE VENDOR=%d AND SWVERSION=%s AND TYPE=%d;",
					vendorType, OBParser.sqlString(VERSION_DEFAULT), OID_TYPE_SESSION_TABLE);

			OBSystemLog.sql(OBDefine.LOGFILE_DEBUG, String.format("sqlText:%s", sqlText));
		} catch (Exception e) {
			throw new OBException(e.getMessage());
		}
		DtoOidInfoSessionTable oids = getCfgSessionTableData(null, sqlText); // default를 구해옴

		if (version.equals(VERSION_DEFAULT) == false) // default가 아닌 버전이면 업데이트할 oid를 찾는다.
		{
			try {
				sqlText = String.format(" SELECT NAME, OID FROM MNG_SNMPOID " + " WHERE VENDOR=%d AND TYPE=%d       "
						+ "     AND (NAME, SWVERSION ) IN     " + "     ( SELECT NAME, MAX(SWVERSION) FROM MNG_SNMPOID "
						+ "       WHERE VENDOR=%d AND TYPE=%d AND SWVERSION <> %s AND SWVERSION <= %s "
						+ "       GROUP BY NAME ) ", vendorType, OID_TYPE_SESSION_TABLE, vendorType,
						OID_TYPE_SESSION_TABLE, OBParser.sqlString(VERSION_DEFAULT), OBParser.sqlString(version));

				OBSystemLog.sql(OBDefine.LOGFILE_DEBUG, String.format("sqlText:%s", sqlText));
			} catch (Exception e) {
				throw new OBException(e.getMessage());
			}
			oids = getCfgSessionTableData(oids, sqlText);
		}

		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", oids));
		return oids;
	}

	private DtoOidInfoSessionTable getCfgSessionTableData(DtoOidInfoSessionTable curOids, String sql)
			throws OBException {
		if (curOids == null) // 업데이트가 아니라 처음 조회면 객체를 만든다.
		{
			curOids = new DtoOidInfoSessionTable();
		}
		ResultSet rs;
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			rs = db.executeQuery(sql);
			while (rs.next()) {
				String name = db.getString(rs, "NAME");
				String oid = db.getString(rs, "OID");

				if ((name != null) && name.isEmpty() == false) {
					if (name.compareToIgnoreCase("maintAllocFails") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setMaintAllocFails(oid);
						}
					} else if (name.compareToIgnoreCase("auxSessIndex") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setAuxIndex(oid);
						}
					} else if (name.compareToIgnoreCase("auxSessCurrConn") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setAuxCurrConn(oid);
						}
					} else if (name.compareToIgnoreCase("auxSessMaxConn") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setAuxMaxConn(oid);
						}
					} else if (name.compareToIgnoreCase("auxSessAllocFails") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							curOids.setAuxAllocFails(oid);
						}
					}
				}
			}
		} catch (Exception e) {
			throw new OBException(e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
		return curOids;
	}

	// Alteon Filter 정보 가져오기. 함수 구조상 vendor를 받지만 현재(2014.4) Alteon만 유효하다.
	public ArrayList<DtoOidEntity> getCfgFilter(Integer vendorType, String swVersion) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
				String.format("start. vendorType:%d, swVersion:%s", vendorType, swVersion));
		String version = VERSION_DEFAULT;
		if (swVersion != null && swVersion.isEmpty() == false) {
			version = swVersion;
		}
		String sqlText;

		try {
			sqlText = String.format("SELECT NAME, OID FROM MNG_SNMPOID WHERE VENDOR=%d AND SWVERSION=%s AND TYPE=%d;",
					vendorType, OBParser.sqlString(VERSION_DEFAULT), OID_TYPE_CFG_FILTER);
		} catch (Exception e) {
			throw new OBException(e.getMessage());
		}
		ArrayList<DtoOidEntity> oids = getOidData(null, sqlText); // default를 구해옴

		if (version.equals(VERSION_DEFAULT) == false) // default가 아닌 버전이면 업데이트할 oid를 찾는다.
		{
			try {
				sqlText = String.format(" SELECT NAME, OID FROM MNG_SNMPOID " + " WHERE VENDOR=%d AND TYPE=%d       "
						+ "     AND (NAME, SWVERSION ) IN     " + "     ( SELECT NAME, MAX(SWVERSION) FROM MNG_SNMPOID "
						+ "       WHERE VENDOR=%d AND TYPE=%d AND SWVERSION <> %s AND SWVERSION <= %s "
						+ "       GROUP BY NAME ) ", vendorType, OID_TYPE_CFG_FILTER, vendorType, OID_TYPE_CFG_FILTER,
						OBParser.sqlString(VERSION_DEFAULT), OBParser.sqlString(version));

				OBSystemLog.sql(OBDefine.LOGFILE_DEBUG, String.format("sqlText:%s", sqlText));
			} catch (Exception e) {
				throw new OBException(e.getMessage());
			}
			oids = getOidData(oids, sqlText);
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", oids));
		return oids;
	}

	private ArrayList<DtoOidEntity> getOidData(ArrayList<DtoOidEntity> curOids, String sql) throws OBException { // 2014.4
																													// oid
																													// 가져오는
																													// 방식을
																													// 아래와
																													// 같이
																													// 개선했다.
																													// 나른
																													// 애들한테도
																													// 모두
																													// 적용해야
																													// 할텐데...
		if (curOids == null) // 업데이트가 아니라 처음 조회면 객체를 만든다.
		{
			curOids = new ArrayList<DtoOidEntity>();
		}
		ResultSet rs;
		OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			rs = db.executeQuery(sql);
			boolean isFound = false;
			while (rs.next()) {
				String name = db.getString(rs, "NAME");
				String oid = db.getString(rs, "OID");

				if (name != null && name.isEmpty() == false) {
					if ((oid != null) && (oid.isEmpty() == false)) {
						isFound = false;
						for (DtoOidEntity curOid : curOids) {
							if (curOid.getName().equals(name)) {
								curOid.setValue(oid); // 기존에 있는데 들어왔으면 업데이트한다.
								isFound = true;
								break;
							}
						}
						if (isFound == false) // 새 oid 항목이면 새로 넣는다.
						{
							DtoOidEntity newOid = new DtoOidEntity();
							newOid.setName(name);
							newOid.setValue(oid);
							curOids.add(newOid);
						}
					}
				}
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY, String.format("%s, sqlText:%s", e.getMessage(), sql));
		} catch (Exception e) {
			throw new OBException(e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
		return curOids;
	}

	public OBDtoOidRptFilterInfo getRptFilterInfo(Integer vendorType, String swVersion) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
				String.format("start. vendorType:%d, swVersion:%s", vendorType, swVersion));
		String version = VERSION_DEFAULT;
		if (swVersion != null && swVersion.isEmpty() == false) {
			version = swVersion;
		}
		String sqlText;
		OBDtoOidRptFilterInfo retVal = new OBDtoOidRptFilterInfo();

		try {
			sqlText = String.format("SELECT NAME, OID FROM MNG_SNMPOID WHERE VENDOR=%d AND SWVERSION=%s AND TYPE=%d;",
					vendorType, OBParser.sqlString(VERSION_DEFAULT), OID_TYPE_CFG_FILTER);

			HashMap<String, DtoOidEntity> oids = getOidDataHashMap(null, sqlText); // default를 구해옴

			if (version.equals(VERSION_DEFAULT) == false) // default가 아닌 버전이면 업데이트할 oid를 찾는다.
			{
				sqlText = String.format(" SELECT NAME, OID FROM MNG_SNMPOID " + " WHERE VENDOR=%d AND TYPE=%d       "
						+ "     AND (NAME, SWVERSION ) IN     " + "     ( SELECT NAME, MAX(SWVERSION) FROM MNG_SNMPOID "
						+ "       WHERE VENDOR=%d AND TYPE=%d AND SWVERSION <> %s AND SWVERSION <= %s "
						+ "       GROUP BY NAME ) ", vendorType, OID_TYPE_CFG_FILTER, vendorType, OID_TYPE_CFG_FILTER,
						OBParser.sqlString(VERSION_DEFAULT), OBParser.sqlString(version));

				OBSystemLog.sql(OBDefine.LOGFILE_DEBUG, String.format("sqlText:%s", sqlText));
				oids = getOidDataHashMap(oids, sqlText);
			}

			retVal.setPortFiltBmap(oids.get("AlteonFilterPortMatching").getValue());
			retVal.setPortState(oids.get("AlteonFilterEnabledPort").getValue());
		} catch (Exception e) {
			throw new OBException(e.getMessage());
		} finally {
		}

		// OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s",
		// oids));
		return retVal;
	}

	public DtoOidEntity getStatFilter(Integer vendorType, String swVersion) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
				String.format("start. vendorType:%d, swVersion:%s", vendorType, swVersion));
		String version = VERSION_DEFAULT;
		if (swVersion != null && swVersion.isEmpty() == false) {
			version = swVersion;
		}
		String sqlText;
		DtoOidEntity retVal = new DtoOidEntity();

		try {
			sqlText = String.format(
					"SELECT NAME, OID FROM MNG_SNMPOID WHERE VENDOR=%d AND SWVERSION=%s AND TYPE=%d AND NAME=%s;",
					vendorType, OBParser.sqlString(VERSION_DEFAULT), OID_TYPE_CFG_FILTER,
					OBParser.sqlString("AlteonFilterCurrConn"));

			HashMap<String, DtoOidEntity> oids = getOidDataHashMap(null, sqlText); // default를 구해옴

			if (version.equals(VERSION_DEFAULT) == false) // default가 아닌 버전이면 업데이트할 oid를 찾는다.
			{
				sqlText = String.format(" SELECT NAME, OID FROM MNG_SNMPOID " + " WHERE VENDOR=%d AND TYPE=%d       "
						+ "     AND (NAME, SWVERSION ) IN     " + "     ( SELECT NAME, MAX(SWVERSION) FROM MNG_SNMPOID "
						+ "       WHERE VENDOR=%d AND TYPE=%d AND SWVERSION <> %s AND SWVERSION <= %s AND NAME=%s"
						+ "       GROUP BY NAME ) ", vendorType, OID_TYPE_CFG_FILTER, vendorType, OID_TYPE_CFG_FILTER,
						OBParser.sqlString(VERSION_DEFAULT), OBParser.sqlString(version),
						OBParser.sqlString("AlteonFilterCurrConn"));

				OBSystemLog.sql(OBDefine.LOGFILE_DEBUG, String.format("sqlText:%s", sqlText));
				oids = getOidDataHashMap(oids, sqlText);
				retVal = oids.get("AlteonFilterCurrConn");
			}
		} catch (Exception e) {
			StackTraceElement[] ste = e.getStackTrace();
			String className = ste[0].getClassName();
			String meThodName = ste[0].getMethodName();
			int lineNumber = ste[0].getLineNumber();
			String fileName = ste[0].getFileName();
			System.out.println("line:" + className + "." + meThodName + " " + fileName + " " + lineNumber + "line");
			throw new OBException(e.getMessage());
		} finally {
		}

		// OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s",
		// oids));
		return retVal;
	}

	private HashMap<String, DtoOidEntity> getOidDataHashMap(HashMap<String, DtoOidEntity> curOids, String sql)
			throws OBException { // 2014.4 oid 가져오는 방식을 아래와 같이 개선했다. 나른 애들한테도 모두 적용해야 할텐데...
		if (curOids == null) // 업데이트가 아니라 처음 조회면 객체를 만든다.
		{
			curOids = new HashMap<String, DtoOidEntity>();
		}
		ResultSet rs;
		OBDatabase db = new OBDatabase();
		try {
			db.openDB();

			rs = db.executeQuery(sql);
			// boolean isFound = false;
			while (rs.next()) {
				String name = db.getString(rs, "NAME");
				String oid = db.getString(rs, "OID");

				if (name != null && name.isEmpty() == false) {
					if ((oid != null) && (oid.isEmpty() == false)) {
						DtoOidEntity entity = new DtoOidEntity();
						entity.setName(name);
						entity.setValue(oid);
						curOids.put(name, entity);
						// isFound = false;
						// for(DtoOidEntity curOid:curOids)
						// {
						// if(curOid.getName().equals(name))
						// {
						// curOid.setValue(oid); //기존에 있는데 들어왔으면 업데이트한다.
						// isFound = true;
						// break;
						// }
						// }
						// if(isFound==false) //새 oid 항목이면 새로 넣는다.
						// {
						// DtoOidEntity newOid = new DtoOidEntity();
						// newOid.setName(name);
						// newOid.setValue(oid);
						// curOids.add(newOid);
						// }
					}
				}
			}
		} catch (Exception e) {
			throw new OBException(e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
		return curOids;
	}

//    public static void main(String[] args)
//    {
//        OBDatabase db = new OBDatabase();
//        try
//        {
//            db.openDB();
//        }
//        catch(Exception e)
//        {
//            return;
//        }
//
//        try
//        {
//            // DtoOidCfgInterface resc = new OBSnmpOidDB().getCfgInterface(OBDefine.ADC_TYPE_ALTEON, "", db);
//
//            OBDtoOidRptInspection resc = new OBSnmpOidDB().getRptInspection(OBDefine.ADC_TYPE_F5, "");
//            System.out.println(resc.toString());
//        }
//        catch(OBException e)
//        {
//            e.printStackTrace();
//        }
//
//        db.closeDB();
//    }

	public OBDtoOidRptInspection getRptInspection(Integer vendorType, String swVersion) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
				String.format("start. vendorType:%d, swVersion:%s", vendorType, swVersion));
		String version = VERSION_DEFAULT;
		if (swVersion != null && swVersion.isEmpty() == false) {
			version = swVersion;
		}
		String sqlText;
		OBDtoOidRptInspection retVal = new OBDtoOidRptInspection();

		try {
			sqlText = String.format("SELECT NAME, OID FROM MNG_SNMPOID WHERE VENDOR=%d AND SWVERSION=%s AND TYPE=%d;",
					vendorType, OBParser.sqlString(VERSION_DEFAULT), OID_TYPE_RPT_INSPECTION);

			HashMap<String, DtoOidEntity> oids = getOidDataHashMap(null, sqlText); // default를 구해옴

			if (version.equals(VERSION_DEFAULT) == false) // default가 아닌 버전이면 업데이트할 oid를 찾는다.
			{
				sqlText = String.format(" SELECT NAME, OID FROM MNG_SNMPOID " + " WHERE VENDOR=%d AND TYPE=%d       "
						+ "     AND (NAME, SWVERSION ) IN     " + "     ( SELECT NAME, MAX(SWVERSION) FROM MNG_SNMPOID "
						+ "       WHERE VENDOR=%d AND TYPE=%d AND SWVERSION <> %s AND SWVERSION <= %s "
						+ "       GROUP BY NAME ) ", vendorType, OID_TYPE_RPT_INSPECTION, vendorType,
						OID_TYPE_RPT_INSPECTION, OBParser.sqlString(VERSION_DEFAULT), OBParser.sqlString(version));
				OBSystemLog.sql(OBDefine.LOGFILE_DEBUG, String.format("sqlText:%s", sqlText));
				oids = getOidDataHashMap(oids, sqlText);
			}

			if (oids.get("rptHotFix") != null)
				retVal.setHotFix(oids.get("rptHotFix").getValue());
			if (oids.get("rptCpuMPUtil64") != null)
				retVal.setCupLoad(oids.get("rptCpuMPUtil64").getValue());
			if (oids.get("rptFanStatus") != null)
				retVal.setFanStatus(oids.get("rptFanStatus").getValue());
			if (oids.get("rptHostName") != null)
				retVal.setHostName(oids.get("rptHostName").getValue());
			if (oids.get("rptLinkUpInfo") != null)
				retVal.setLinkupInfo(oids.get("rptLinkUpInfo").getValue());
			if (oids.get("rptModelName") != null)
				retVal.setModel(oids.get("rptModelName").getValue());
			if (oids.get("rptOsVersion") != null)
				retVal.setOsVersion(oids.get("rptOsVersion").getValue());
			if (oids.get("rptPoolMemberStatusAvail") != null)
				retVal.setPoolMemberStatus(oids.get("rptPoolMemberStatusAvail").getValue());
			if (oids.get("rptStatsPhyIfCollisions") != null)
				retVal.setPortColls(oids.get("rptStatsPhyIfCollisions").getValue());
			if (oids.get("rptPortInfoMode") != null)
				retVal.setPortDuplex(oids.get("rptPortInfoMode").getValue());
			if (oids.get("rptStatsPhyIfErrorsIn") != null)
				retVal.setPortErrorIn(oids.get("rptStatsPhyIfErrorsIn").getValue());
			if (oids.get("rptStatsPhyIfErrorsOut") != null)
				retVal.setPortErrorOut(oids.get("rptStatsPhyIfErrorsOut").getValue());
			if (oids.get("rptPortInfoSpeed") != null)
				retVal.setPortSpeedInfo(oids.get("rptPortInfoSpeed").getValue());
			if (oids.get("rptPowerSupplyStatus") != null)
				retVal.setPowerStatus(oids.get("rptPowerSupplyStatus").getValue());
			if (oids.get("rptSerialNum") != null)
				retVal.setSerialNo(oids.get("rptSerialNum").getValue());
			if (oids.get("rptUpTime") != null)
				retVal.setUpTime(oids.get("rptUpTime").getValue());
			if (oids.get("rptVSStatusAvail") != null)
				retVal.setVirtualServerStatus(oids.get("rptVSStatusAvail").getValue());
			if (oids.get("rptVlanName") != null)
				retVal.setVlanInfo(oids.get("rptVlanName").getValue());

			if (oids.get("rptHAStatus") != null)
				retVal.setHaStatus(oids.get("rptHAStatus").getValue());
			if (oids.get("rptGateway") != null)
				retVal.setGateway(oids.get("rptGateway").getValue());
			if (oids.get("rptConnMax") != null)
				retVal.setConnMax(oids.get("rptConnMax").getValue());
			if (oids.get("rptConnCurr") != null)
				retVal.setConnCurr(oids.get("rptConnCurr").getValue());
			if (oids.get("rptMemTotal") != null)
				retVal.setMemTotal(oids.get("rptMemTotal").getValue());
			if (oids.get("rptMemUsed") != null)
				retVal.setMemUsed(oids.get("rptMemUsed").getValue());

			if (oids.get("rptFilterEnabledPort") != null)
				retVal.setFilterEnabledPort(oids.get("rptFilterEnabledPort").getValue());
			if (oids.get("rptFilterPortMatching") != null)
				retVal.setFilterPortMatching(oids.get("rptFilterPortMatching").getValue());
			if (oids.get("rptStatsPhyIfDiscardsIn") != null)
				retVal.setPortDiscardsIn(oids.get("rptStatsPhyIfDiscardsIn").getValue());
			if (oids.get("rptStatsPhyIfDiscardsOut") != null)
				retVal.setPortDiscardsOut(oids.get("rptStatsPhyIfDiscardsOut").getValue());

			if (oids.get("hwTemperatureStatus") != null)
				retVal.setChassisTempStatus(oids.get("hwTemperatureStatus").getValue());
		} catch (Exception e) {
			throw new OBException(e.getMessage());
		} finally {
		}

		// OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s",
		// oids));
		return retVal;
	}

	// 특정벤더의 특정 트랩 oid를 이름으로 찾아 리턴한다.
	public DtoOidTrap getSnmpTrap(Integer vendorType, String swVersion, String name) throws OBException {
		// 트랩 전체 목록을 구하는 것이 아니라, 특정 이름의 트랩하나를 구한다. 트랩은 보통 그룹단위로 꺼내 쓰지 않고 한개 OID만 사용하기
		// 때문에.
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
				String.format("start. vendorType:%d, swVersion:%s", vendorType, swVersion));
		String version = VERSION_DEFAULT;
		if (swVersion != null && swVersion.isEmpty() == false) {
			version = swVersion;
		}
		String sqlText;
		// DEFAULT 구하기, trap은 3.1.1부터 시작, default가 없다. 생략
//        try
//        {
//            sqlText = String.format("SELECT NAME, OID FROM MNG_SNMPOID WHERE VENDOR=%d AND SWVERSION=%s AND TYPE=%d AND NAME = %s ;",
//                            vendorType, OBParser.sqlString(VERSION_DEFAULT), OID_TYPE_SNMPTRAP, OBParser.sqlString(name));
//        }
//        catch(Exception e)
//        {
//            throw new OBException(e.getMessage());
//        }
//        DtoOidStatVirtServer oids = getStatVirtServerData(null, sqlText);
		try {
			sqlText = String.format(
					" SELECT NAME, OID FROM MNG_SNMPOID " + " WHERE VENDOR=%d AND TYPE=%d AND NAME = %s "
							+ "     AND (NAME, SWVERSION ) IN ( SELECT NAME, MAX(SWVERSION) FROM MNG_SNMPOID "
							+ "         WHERE VENDOR=%d AND TYPE=%d AND NAME = %s AND SWVERSION <= %s GROUP BY NAME ) "
							+ " UNION SELECT NAME, OID FROM MNG_SNMPOID WHERE VENDOR=%d AND TYPE=%d ", // trap variable
																										// oid들을 한번에
																										// 뽑는다.
					vendorType, OID_TYPE_SNMPTRAP, OBParser.sqlString(name), vendorType, OID_TYPE_SNMPTRAP,
					OBParser.sqlString(name), OBParser.sqlString(version), vendorType, OID_TYPE_SNMPTRAP_VARIABLE);

			OBSystemLog.sql(OBDefine.LOGFILE_DEBUG, String.format("sqlText:%s", sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		ResultSet rs;
		OBDatabase db = new OBDatabase();
		String nameFromDB = null, oid = null;
		DtoOidTrap ret = new DtoOidTrap();
		try {
			db.openDB();
			rs = db.executeQuery(sqlText);

			while (rs.next()) {
				nameFromDB = db.getString(rs, "NAME");
				oid = db.getString(rs, "OID");
				ret.setNotiName(name);
				if (nameFromDB != null && nameFromDB.isEmpty() == false) {
					if (nameFromDB.compareToIgnoreCase(name) == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							ret.setNotiOid(oid);
						}
					} else if (nameFromDB.compareToIgnoreCase("variableMsg") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							ret.setVarMsgOid(oid);
						}
					} else if (nameFromDB.compareToIgnoreCase("variableObject") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							ret.setVarObjectOid(oid);
						}
					} else if (nameFromDB.compareToIgnoreCase("variableLevel") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							ret.setVarLevelOid(oid);
						}
					} else if (nameFromDB.compareToIgnoreCase("variableType") == 0) {
						if ((oid != null) && (oid.isEmpty() == false)) {
							ret.setVarTypeOid(oid);
						}
					}
				}
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
		return ret;
	}
}
