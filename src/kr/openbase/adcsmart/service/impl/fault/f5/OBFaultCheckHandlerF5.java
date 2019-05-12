package kr.openbase.adcsmart.service.impl.fault.f5;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.snmp4j.smi.VariableBinding;

import kr.openbase.adcsmart.service.dto.OBDtoADCObject;
import kr.openbase.adcsmart.service.dto.OBDtoAdcInfo;
import kr.openbase.adcsmart.service.dto.OBDtoAdcPerformance;
import kr.openbase.adcsmart.service.dto.OBDtoAdcPortStatus;
import kr.openbase.adcsmart.service.dto.OBDtoAuditLogAdcSystem;
import kr.openbase.adcsmart.service.dto.OBDtoElement;
import kr.openbase.adcsmart.service.dto.OBDtoReturnInfo;
import kr.openbase.adcsmart.service.dto.OBDtoSearch;
import kr.openbase.adcsmart.service.dto.OBDtoSlbNodeInfo;
import kr.openbase.adcsmart.service.dto.OBDtoSlbPoolInfo;
import kr.openbase.adcsmart.service.dto.OBDtoSlbVServerInfo;
import kr.openbase.adcsmart.service.dto.fault.OBDtoFaultCheckLog;
import kr.openbase.adcsmart.service.dto.fault.OBDtoFaultCheckResultContent;
import kr.openbase.adcsmart.service.dto.fault.OBDtoFaultCheckResultElement;
import kr.openbase.adcsmart.service.impl.OBAdcManagementImpl;
import kr.openbase.adcsmart.service.impl.OBEnvManagementImpl;
import kr.openbase.adcsmart.service.impl.OBMonitoringImpl;
import kr.openbase.adcsmart.service.impl.dto.OBDtoFaultFileSizeInfo;
import kr.openbase.adcsmart.service.impl.dto.OBDtoMonL2Ports;
import kr.openbase.adcsmart.service.impl.f5.handler.OBAdcF5Handler;
import kr.openbase.adcsmart.service.impl.f5.handler.OBCLIParserF5;
import kr.openbase.adcsmart.service.impl.fault.OBFaultCheckHandler;
import kr.openbase.adcsmart.service.impl.fault.OBFaultMngImpl;
import kr.openbase.adcsmart.service.impl.fault.OBFaultMonitoringDB;
import kr.openbase.adcsmart.service.impl.fault.dto.OBDtoFaultCheckPacketLossInfo;
import kr.openbase.adcsmart.service.impl.fault.dto.OBDtoFaultMaxPerfInfo;
import kr.openbase.adcsmart.service.impl.fault.dto.OBDtoMinMaxAvgInfo;
import kr.openbase.adcsmart.service.impl.fault.dto.OBDtoPktLossInfo;
import kr.openbase.adcsmart.service.impl.fault.dto.OBDtoRespTimeAnalInfo;
import kr.openbase.adcsmart.service.impl.fault.dto.OBDtoRespTimeTempInfo;
import kr.openbase.adcsmart.service.jna.LibOBPktAnalysisLibrary;
import kr.openbase.adcsmart.service.snmp.OBSnmp;
import kr.openbase.adcsmart.service.snmp.OBSnmpOidDB;
import kr.openbase.adcsmart.service.snmp.dto.DtoOidFaultCheckHW;
import kr.openbase.adcsmart.service.snmp.f5.OBSnmpF5;
import kr.openbase.adcsmart.service.utility.OBCommon;
import kr.openbase.adcsmart.service.utility.OBDateTime;
import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.service.utility.OBFaultCheckMsg;
import kr.openbase.adcsmart.service.utility.OBParser;
import kr.openbase.adcsmart.service.utility.OBSystemLog;
import kr.openbase.adcsmart.service.utility.OBUtility;

public class OBFaultCheckHandlerF5 implements OBFaultCheckHandler {
	private DtoOidFaultCheckHW snmpOidInfoHW = null;
//	private DtoOidFaultCheckL23 snmpOidInfoL23=null;
	private Integer vendorType;
	private String swVersion;
	private OBDtoAdcInfo adcInfo;
//	private OBDtoFaultCheckLog logInfo;
//	private long logKey;
	private OBSnmp ClassSnmp;
	private OBAdcF5Handler ClassSsh;

	private Integer packetDumpCount = 1000;
	private Integer packetDumpLength = 0;// all
//	private Integer packetDumpMaxStart 	= 10000;
	private Integer uptimeMaxDiffDate = 300000;
	private Integer temperatureMax = 65;// 50도.

	public OBFaultCheckHandlerF5() {

	}

	public OBFaultCheckHandlerF5(Integer vendorType, String swVersion) {
		this.vendorType = vendorType;
		this.swVersion = swVersion;

		// 환경 변수를 읽어 들인다.
		try {
			String propertyValue = OBParser.propertyToString(OBDefine.PROPERTY_FILE_NAME,
					OBDefine.PROPERTY_KEY_PKT_COUNT);
			if (propertyValue != null && !propertyValue.isEmpty())
				this.packetDumpCount = Integer.parseInt(propertyValue);

			propertyValue = OBParser.propertyToString(OBDefine.PROPERTY_FILE_NAME, OBDefine.PROPERTY_KEY_PKT_LENGTH);
			if (propertyValue != null && !propertyValue.isEmpty())
				this.packetDumpLength = Integer.parseInt(propertyValue);

//			propertyValue = OBParser.propertyToString(OBDefine.PROPERTY_FILE_NAME, OBDefine.PROPERTY_KEY_PKT_MAX_START);
//			if(propertyValue!=null && !propertyValue.isEmpty())
//				this.packetDumpMaxStart = Integer.parseInt(propertyValue);

			propertyValue = OBParser.propertyToString(OBDefine.PROPERTY_FILE_NAME,
					OBDefine.PROPERTY_KEY_UPTIME_MAX_DIFF_DATE);
			if (propertyValue != null && !propertyValue.isEmpty())
				this.uptimeMaxDiffDate = Integer.parseInt(propertyValue);

			propertyValue = OBParser.propertyToString(OBDefine.PROPERTY_FILE_NAME,
					OBDefine.PROPERTY_KEY_TEMPERATURE_MAX);
			if (propertyValue != null && !propertyValue.isEmpty())
				this.temperatureMax = Integer.parseInt(propertyValue);
		} catch (Exception e) {
		}
	}

	@Override
	public void loadSnmpOidHWInfo() throws OBException {
		this.snmpOidInfoHW = new OBSnmpOidDB().getFaultCheckHWInfo(this.vendorType, this.swVersion);
//		this.snmpOidInfoL23 = new OBSnmpOidDB().getFaultCheckL23Info(this.vendorType, this.swVersion, this.db);
	}

	@Override
	public void setParameter(long logKey, OBDtoFaultCheckLog logInfo, OBDtoAdcInfo adcInfo, Object CLIObj)
			throws OBException {
		this.adcInfo = adcInfo;
		this.ClassSnmp = new OBSnmpF5(this.adcInfo.getAdcIpAddress(), adcInfo.getSnmpInfo());
		this.ClassSsh = (OBAdcF5Handler) CLIObj;
	}

	@Override
	public OBDtoFaultCheckResultElement checkHWPowerSupply(OBDtoElement obj) throws OBException {
		OBDtoFaultCheckResultElement retVal = new OBDtoFaultCheckResultElement();
		retVal.setObj(obj);
		retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_NA);
		ArrayList<OBDtoFaultCheckResultContent> resultList = new ArrayList<OBDtoFaultCheckResultContent>();
		retVal.setResultList(resultList);
		retVal.setMsgCLI("");
		retVal.setStartTime(OBDateTime.toTimestamp(OBDateTime.now()));
		retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));

		String msgCLI = "";

		if (this.snmpOidInfoHW == null || this.snmpOidInfoHW.getPowerSupply().isEmpty())
			return retVal;

		List<VariableBinding> tmpList = null;
		String oid = this.snmpOidInfoHW.getPowerSupply();
		try {
			tmpList = this.ClassSnmp.walk(oid);
		} catch (OBException e) {
			retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_FAIL);
			OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
			content.setSummary(String.format(OBFaultCheckMsg.HW_POWERSUPPLY_CHECK.getProcessFailMsg()));
			content.setDetail(String.format(OBFaultCheckMsg.SNMP_QUERY.getFailMsg(), oid));
			resultList.add(content);
			msgCLI += String.format(OBFaultCheckMsg.SNMP_QUERY.getFailMsg(), oid);
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
					String.format("failed to query snmp. adcIndex(%d), old(%s)", this.adcInfo.getIndex(), oid));

			retVal.setMsgCLI(msgCLI);
			retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));
			return retVal;
		}

		if (tmpList.size() == 0) {// 조회한 데이터가 없는 경우. 지원되지 않은 버전이다.
			retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_NA);
			OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
			content.setSummary(String.format(OBFaultCheckMsg.NOT_SUPPORT.getSuccessMsg()));
			content.setDetail(String.format(OBFaultCheckMsg.SNMP_QUERY.getSuccessMsg(), oid));
			resultList.add(content);
			msgCLI += String.format(OBFaultCheckMsg.SNMP_QUERY.getSuccessMsg(), oid);
//			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("failed to query snmp. adcIndex(%d), old(%s)", this.adcInfo.getIndex(), oid));

			retVal.setMsgCLI(msgCLI);
			retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));
			return retVal;
		}

		try {
			String detailMsg = "";
			int checkStatus = OBDtoFaultCheckResultElement.STATUS_SUCC;
			for (VariableBinding var : tmpList) {
				Integer status = var.getVariable().toInt();// 0:bad, 1:good, 2:notpresent.
															// example:SNMPv2-SMI::enterprises.3375.2.1.3.2.2.2.1.2.101
															// = INTEGER: 1
				ArrayList<Integer> oidList = this.ClassSnmp.parseOid(var.getOid().toString(), oid);
				Integer supplyIndex = oidList.get(0);

				msgCLI += String.format(OBFaultCheckMsg.SNMP_QUERY_VALUE.getSuccessMsg(), oid, status.toString());
				msgCLI += "\n";

				if (status == 0) {// bad
					checkStatus = OBDtoFaultCheckResultElement.STATUS_FAIL;
					detailMsg += String.format(OBFaultCheckMsg.HW_POWERSUPPLY_INFO.getFailMsg(),
							supplyIndex.toString());
					detailMsg += "\n";
				} else if (status == 1) {// good
					detailMsg += String.format(OBFaultCheckMsg.HW_POWERSUPPLY_INFO.getSuccessMsg(),
							supplyIndex.toString());
					detailMsg += "\n";
				} else if (status == 2) {// not present
					if (checkStatus != OBDtoFaultCheckResultElement.STATUS_FAIL)
						checkStatus = OBDtoFaultCheckResultElement.STATUS_INFO;
					detailMsg += String.format(OBFaultCheckMsg.HW_POWERSUPPLY_INFO.getProcessFailMsg(),
							supplyIndex.toString());
					detailMsg += "\n";
				}
			}
			retVal.setStatus(checkStatus);
			OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
			if (checkStatus == OBDtoFaultCheckResultElement.STATUS_FAIL)
				content.setSummary(OBFaultCheckMsg.HW_POWERSUPPLY_CHECK.getFailMsg());
			else
				content.setSummary(OBFaultCheckMsg.HW_POWERSUPPLY_CHECK.getSuccessMsg());
			content.setDetail(detailMsg);
			resultList.add(content);

			retVal.setMsgCLI(msgCLI);
			retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));
			return retVal;
		} catch (Exception e) {
			retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_FAIL);
			OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
			content.setSummary(String.format(OBFaultCheckMsg.HW_POWERSUPPLY_CHECK.getProcessFailMsg()));
			content.setDetail(String.format(OBFaultCheckMsg.PARSE_DATA.getFailMsg()));
			resultList.add(content);
			msgCLI = String.format(OBFaultCheckMsg.PARSE_DATA.getFailMsg(), oid);
			msgCLI += "\n";
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
					String.format("failed to query snmp. adcIndex(%d), old(%s)", this.adcInfo.getIndex(), oid));

			retVal.setMsgCLI(msgCLI);
			retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));
			return retVal;
		}
	}

	@Override
	public OBDtoFaultCheckResultElement checkHWUptime(OBDtoElement obj) throws OBException {
		OBDtoFaultCheckResultElement retVal = new OBDtoFaultCheckResultElement();
		retVal.setObj(obj);
		retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_NA);
		ArrayList<OBDtoFaultCheckResultContent> resultList = new ArrayList<OBDtoFaultCheckResultContent>();
		retVal.setResultList(resultList);
		retVal.setMsgCLI("");
		retVal.setStartTime(OBDateTime.toTimestamp(OBDateTime.now()));
		retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));

		String msgCLI = "";

		String retMsg = "";

		// 현재 시간을 추출한다.
		try {
			// 데이터 추출.
			retMsg = this.ClassSsh.cmndInfoDate();
			msgCLI = this.ClassSsh.getCmndRetString();
		} catch (OBException e) {
			retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_FAIL);
			OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
			content.setSummary(OBFaultCheckMsg.HW_UPTIME_CHECK.getProcessFailMsg());
			content.setDetail(String.format(OBFaultCheckMsg.SSH_QUERY.getFailMsg(), retMsg));
			resultList.add(content);
			msgCLI += retMsg;
			msgCLI += "\n";
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
					String.format("failed to get ssh data. adcIndex(%d), cli(%s)", this.adcInfo.getIndex(), msgCLI));

			retVal.setMsgCLI(msgCLI);
			retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));
			return retVal;
		}

		// 데이터 분석.
		Timestamp dateInfo = null;
		try {
			dateInfo = new OBCLIParserF5().parseInfoDate(retMsg);
		} catch (OBException e) {
			retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_FAIL);

			OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
			content.setSummary(OBFaultCheckMsg.HW_UPTIME_INTERVAL_INFO.getProcessFailMsg());
			content.setDetail(OBFaultCheckMsg.PARSE_DATA.getFailMsg());
			resultList.add(content);

			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
					String.format("failed to parse data. adcIndex(%d), cli(%s)", this.adcInfo.getIndex(), msgCLI));

			retVal.setMsgCLI(msgCLI);
			retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));
			return retVal;
		}

		try {
			Timestamp currTime = OBDateTime.toTimestamp(OBDateTime.now());

			retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_SUCC);
			// 현재 시간 비교. 최대 5분 이상 차이가 나면 실패한 것으로 간주한다.
			if (Math.abs(currTime.getTime() - dateInfo.getTime()) < this.uptimeMaxDiffDate) {//
				OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
				content.setSummary(OBFaultCheckMsg.HW_UPTIME_INTERVAL_INFO.getSuccessMsg());

				// [GS] #3984-6 #2: 14.07.29 sw.jung Uptime 측정기준 출력
//              content.setDetail(msgCLI);
				String detail = String.format("ADCsmart Time: %s\n\n\n%s", new Date(currTime.getTime()), msgCLI);
				content.setDetail(detail);

				resultList.add(content);
			} else {// 5분 이상 차이가 나는 경우.
				retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_FAIL);
				OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
				content.setSummary(OBFaultCheckMsg.HW_UPTIME_INTERVAL_INFO.getFailMsg());

				// [GS] #3984-6 #2: 14.07.29 sw.jung Uptime 측정기준 출력
//              content.setDetail(msgCLI);
				String detail = String.format("ADCsmart Time: %s\n\n\n%s", new Date(currTime.getTime()), msgCLI);
				content.setDetail(detail);

				resultList.add(content);
			}
		} catch (Exception e) {
			retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_FAIL);

			OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
			content.setSummary(OBFaultCheckMsg.HW_UPTIME_INTERVAL_INFO.getProcessFailMsg());
			content.setDetail(OBFaultCheckMsg.PARSE_DATA.getFailMsg());
			resultList.add(content);
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
					String.format("failed to parse data. adcIndex(%d), cli(%s)", this.adcInfo.getIndex(), msgCLI));

			retVal.setMsgCLI(msgCLI);
			retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));
			return retVal;
		}

		// uptime 정보 추출.
		try {
			retMsg = this.ClassSsh.cmndInfoUptime();
			msgCLI += this.ClassSsh.getCmndRetString();
		} catch (Exception e) {
			retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_FAIL);

			OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
			content.setSummary(OBFaultCheckMsg.HW_UPTIME_CHECK.getProcessFailMsg());
			content.setDetail(OBFaultCheckMsg.PARSE_DATA.getFailMsg());
			resultList.add(content);
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
					String.format("failed to parse data. adcIndex(%d), cli(%s)", this.adcInfo.getIndex(), msgCLI));

			retVal.setMsgCLI(msgCLI);
			retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));
			return retVal;
		}

		OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
		content.setSummary(OBFaultCheckMsg.HW_UPTIME_INFO.getSuccessMsg());
		content.setDetail(retMsg);
		resultList.add(content);

		retVal.setMsgCLI(msgCLI);
		retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));
		return retVal;
	}

	/**
	 * 
	 * 현재의 throughput 및 connection 정보를 추출한다. DB에 지정된 값과 비교하여 지정된 임계치를 초과하는지 검사한다.
	 * 초과된 경우에만 비정상으로 간주한다.
	 */
	@Override
	public OBDtoFaultCheckResultElement checkHWLicense(OBDtoElement obj) throws OBException {
		OBDtoFaultCheckResultElement retVal = new OBDtoFaultCheckResultElement();
		retVal.setObj(obj);
		retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_NA);
		ArrayList<OBDtoFaultCheckResultContent> resultList = new ArrayList<OBDtoFaultCheckResultContent>();
		retVal.setResultList(resultList);
		retVal.setMsgCLI("");
		retVal.setStartTime(OBDateTime.toTimestamp(OBDateTime.now()));
		retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));

		String msgCLI = "";

		OBDtoFaultMaxPerfInfo maxPerfInfo = null;
		try {
			maxPerfInfo = new OBFaultMngImpl().getMaxPerfInfo(this.adcInfo);
		} catch (OBException e) {
			retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_FAIL);
			OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
			content.setSummary(OBFaultCheckMsg.HW_LICENSE_CHECK.getProcessFailMsg());
			content.setDetail(String.format(OBFaultCheckMsg.DB_QUERY.getFailMsg()));
			resultList.add(content);
			msgCLI = e.getErrorMessage();
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
					String.format("failed to get db data. adcIndex(%d), cli(%s)", this.adcInfo.getIndex(), msgCLI));

			retVal.setMsgCLI(msgCLI);
			retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));
			return retVal;
		}

		if (maxPerfInfo == null) {// 기초 데이터가 없는 경우.
			retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_INFO);
			OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
			content.setSummary(OBFaultCheckMsg.NOT_SUPPORT.getSuccessMsg());
			content.setDetail(String.format(OBFaultCheckMsg.DATA_NOT_EXIST.getFailMsg()));
			resultList.add(content);
			msgCLI = OBFaultCheckMsg.DATA_NOT_EXIST.getFailMsg();
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
					String.format("failed to get db data. adcIndex(%d), cli(%s)", this.adcInfo.getIndex(), msgCLI));

			retVal.setMsgCLI(msgCLI);
			retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));
			return retVal;
		}
		msgCLI += OBFaultCheckMsg.DB_QUERY.getSuccessMsg();

		// port interface 정보를 조회해서 수신 데이터량을 측정한다.
		OBDtoAdcPerformance currPerfInfo = null;
		try {
			Date endTime = new Date();
			int rescCheckInterval = new OBEnvManagementImpl().getAdcSyncInterval();// 초 단위.
			rescCheckInterval = rescCheckInterval * 2000;
			Date beginTime = new Date(endTime.getTime() - rescCheckInterval);
			currPerfInfo = new OBMonitoringImpl().getAdcPerformance(this.adcInfo.getIndex(), beginTime, endTime);
		} catch (OBException e) {
			retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_FAIL);
			OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
			content.setSummary(OBFaultCheckMsg.HW_LICENSE_CHECK.getProcessFailMsg());
			content.setDetail(String.format(OBFaultCheckMsg.DB_QUERY.getFailMsg()));
			resultList.add(content);
			msgCLI = e.getErrorMessage();
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
					String.format("failed to get db data. adcIndex(%d), cli(%s)", this.adcInfo.getIndex(), msgCLI));

			retVal.setMsgCLI(msgCLI);
			retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));
			return retVal;
		}
		msgCLI += OBFaultCheckMsg.DB_QUERY.getSuccessMsg();

		if (currPerfInfo == null) {
			retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_INFO);
			OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
			content.setSummary(OBFaultCheckMsg.NOT_SUPPORT.getSuccessMsg());
			content.setDetail(String.format(OBFaultCheckMsg.DATA_NOT_EXIST.getFailMsg()));
			resultList.add(content);
			msgCLI = OBFaultCheckMsg.DATA_NOT_EXIST.getFailMsg();
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
					String.format("failed to get db data. adcIndex(%d), cli(%s)", this.adcInfo.getIndex(), msgCLI));

			retVal.setMsgCLI(msgCLI);
			retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));
			return retVal;
		}

		// connection 검사.
		if (maxPerfInfo.getMaxConnection() != 0 && maxPerfInfo.getMaxConnection() <= currPerfInfo.getConnCurr()) {
			retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_FAIL);
			OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
			content.setSummary(OBFaultCheckMsg.HW_LICENSE_CONNECTION_CHECK.getProcessFailMsg());
			content.setDetail(String.format(OBFaultCheckMsg.HW_LICENSE_CONNECTION_INFO.getProcessFailMsg(),
					maxPerfInfo.getMaxConnection().toString(), currPerfInfo.getConnCurr()));
			resultList.add(content);
		} else {
			retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_SUCC);
			OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
			content.setSummary(OBFaultCheckMsg.HW_LICENSE_CONNECTION_CHECK.getSuccessMsg());
			content.setDetail(String.format(OBFaultCheckMsg.HW_LICENSE_CONNECTION_INFO.getSuccessMsg(),
					maxPerfInfo.getMaxConnection().toString(), currPerfInfo.getConnCurr()));
			resultList.add(content);
		}

		// throughput 검사.
		if (maxPerfInfo.getMaxThroughput() != 0 && maxPerfInfo.getMaxThroughput() <= currPerfInfo.getBpsCurr()) {
			retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_FAIL);
			OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
			content.setSummary(OBFaultCheckMsg.HW_LICENSE_CONNECTION_CHECK.getProcessFailMsg());
			content.setDetail(String.format(OBFaultCheckMsg.HW_LICENSE_CONNECTION_INFO.getProcessFailMsg(),
					maxPerfInfo.getMaxConnection().toString(), currPerfInfo.getConnCurr()));
			resultList.add(content);
		} else {
//			retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_SUCC);// 위에서 실패의 경우에는 그대로 승계한다.
			OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
			content.setSummary(OBFaultCheckMsg.HW_LICENSE_TROUGHPUT_CHECK.getSuccessMsg());
			content.setDetail(String.format(OBFaultCheckMsg.HW_LICENSE_TROUGHPUT_INFO.getSuccessMsg(),
					maxPerfInfo.getMaxConnection().toString(), currPerfInfo.getConnCurr()));
			resultList.add(content);
		}
		retVal.setMsgCLI(msgCLI);
		retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));
		return retVal;
	}

	@Override
	public OBDtoFaultCheckResultElement checkHWPortInterface(OBDtoElement obj, Integer adcIndex) throws OBException {
		OBDtoFaultCheckResultElement retVal = new OBDtoFaultCheckResultElement();
		retVal.setObj(obj);
		retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_NA);
		ArrayList<OBDtoFaultCheckResultContent> resultList = new ArrayList<OBDtoFaultCheckResultContent>();
		retVal.setResultList(resultList);
		retVal.setMsgCLI("");
		retVal.setStartTime(OBDateTime.toTimestamp(OBDateTime.now()));
		retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));

		String msgCLI = "";

		if (this.snmpOidInfoHW == null || this.snmpOidInfoHW.getPortInfoLink().isEmpty())// 대표적으로 하나만 검사하자.
			return retVal;

		retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_SUCC);
		HashMap<String, OBDtoAdcPortStatus> statusMap = new OBFaultMonitoringDB().getLastPortInterfaceInfo(adcIndex);

		// mode 변환 조사.
		try {
			String summaryModeText = "";
			String detailModeText = "";
			Long dropTotal = 0L;
			Long errTotal = 0L;
			String detailDropText = "";
			String detailErrText = "";

			OBSnmpF5 snmp = OBCommon.getValidSnmpF5Handler(adcInfo.getSwVersion(), adcInfo.getAdcIpAddress(),
					adcInfo.getSnmpInfo());
			ArrayList<OBDtoMonL2Ports> portInfoList = snmp.getPortsInfo(this.adcInfo.getAdcType(),
					this.adcInfo.getSwVersion());

			for (OBDtoMonL2Ports info : portInfoList) {
				// mode 검사.
//				Integer mode = info.getDuplex();// 0: any, half:1, full:2
				OBDtoAdcPortStatus prevPortStatus = statusMap.get(info.getPortName());
				if (prevPortStatus == null)
					continue;

				if (prevPortStatus.getMode() != info.getDuplex()) {
					retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_FAIL);
					String duplexString = OBDefine.DUPLEX_NONE_STR;
					if (info.getDuplex() == OBDefine.DUPLEX_FULL)
						duplexString = OBDefine.DUPLEX_FULL_STR;
					else if (info.getDuplex() == OBDefine.DUPLEX_HALF)
						duplexString = OBDefine.DUPLEX_HALF_STR;

					String prevDuplexString = OBDefine.DUPLEX_NONE_STR;
					if (prevPortStatus.getMode() == OBDefine.DUPLEX_FULL)
						prevDuplexString = OBDefine.DUPLEX_FULL_STR;
					else if (prevPortStatus.getMode() == OBDefine.DUPLEX_HALF)
						prevDuplexString = OBDefine.DUPLEX_HALF_STR;

					if (!summaryModeText.isEmpty())
						summaryModeText += ", ";
					if (!detailModeText.isEmpty())
						detailModeText += "\n";
					summaryModeText += info.getPortName();
					detailModeText += String.format(OBFaultCheckMsg.HW_PORT_DUPLEX_INFO.getFailMsg(),
							info.getPortName(), prevDuplexString, duplexString);
				}
				// drop 패킷 검사
				if (info.getDropsIn() != 0 && prevPortStatus.getDropsIn() != info.getDropsIn()) {
					dropTotal += info.getDropsIn();

//					retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_FAIL);
					retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_INFO); // DROP 패킷이 있더라도 ERROR 아님 INFO로 변경

					if (!detailDropText.isEmpty())
						detailDropText += "\n";
					Long diff = Math.abs(info.getDropsIn() - prevPortStatus.getDropsIn());
					detailDropText += String.format(OBFaultCheckMsg.HW_PORT_DROP_INFO.getFailMsg(), info.getPortName(),
							diff, info.getDropsIn());
				}

				// err 패킷 검사.
				if (info.getErrorsIn() != 0 && prevPortStatus.getErrorsIn() != info.getErrorsIn()) {
					errTotal += info.getErrorsIn();

					retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_FAIL);

					if (!detailErrText.isEmpty())
						detailErrText += "\n";
					Long diff = Math.abs(info.getErrorsIn() - prevPortStatus.getErrorsIn());
					detailErrText += String.format(OBFaultCheckMsg.HW_PORT_ERROR_INFO.getFailMsg(), info.getPortName(),
							diff, info.getErrorsIn());
				}
			}
			if (summaryModeText.isEmpty())// retVal.getStatus() == OBDtoFaultCheckResultElement.STATUS_SUCC)
			{
				OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
				content.setSummary(String.format(OBFaultCheckMsg.HW_PORT_DUPLEX_STATUS.getSuccessMsg()));
				resultList.add(content);
			} else {
				OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
				content.setSummary(String.format(OBFaultCheckMsg.HW_PORT_DUPLEX_STATUS.getFailMsg(), summaryModeText));
				content.setDetail(String.format(detailModeText));
				resultList.add(content);
			}

			// drop 패킷.
			if (dropTotal == 0L)//
			{
				OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
				content.setSummary(String.format(OBFaultCheckMsg.HW_PORT_DROP_CHECK.getSuccessMsg()));
				resultList.add(content);
			} else {
				OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
				content.setSummary(String.format(OBFaultCheckMsg.HW_PORT_DROP_CHECK.getFailMsg(), dropTotal));
				content.setDetail(String.format(detailDropText));
				resultList.add(content);
			}

			// error 패킷.
			if (errTotal == 0L)//
			{
				OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
				content.setSummary(String.format(OBFaultCheckMsg.HW_PORT_ERROR_CHECK.getSuccessMsg()));
				resultList.add(content);
			} else {
				OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
				content.setSummary(String.format(OBFaultCheckMsg.HW_PORT_ERROR_CHECK.getFailMsg(), errTotal));
				content.setDetail(String.format(detailErrText));
				resultList.add(content);
			}
		} catch (Exception e) {
			retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_FAIL);
			OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
			content.setSummary(String.format(OBFaultCheckMsg.HW_PORT_CHECK.getProcessFailMsg()));
			content.setDetail(String.format(OBFaultCheckMsg.SNMP_QUERY_CHECK.getFailMsg()));
			resultList.add(content);
			msgCLI += String.format(OBFaultCheckMsg.SNMP_QUERY_CHECK.getFailMsg());
			msgCLI += "\n";
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
					String.format("failed to query snmp. adcIndex(%d)", this.adcInfo.getIndex()));

			retVal.setMsgCLI(msgCLI);
			retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));
		}

		retVal.setResultList(resultList);
		retVal.setMsgCLI(msgCLI);
		retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));

		return retVal;
	}

	@Override
	public OBDtoFaultCheckResultElement checkHWCpuStatus(OBDtoElement obj, int max) throws OBException {
		OBDtoFaultCheckResultElement retVal = new OBDtoFaultCheckResultElement();
		retVal.setObj(obj);
		retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_NA);
		ArrayList<OBDtoFaultCheckResultContent> resultList = new ArrayList<OBDtoFaultCheckResultContent>();
		retVal.setResultList(resultList);
		retVal.setMsgCLI("");
		retVal.setStartTime(OBDateTime.toTimestamp(OBDateTime.now()));
		retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));

		String msgCLI = "";

		if (this.snmpOidInfoHW == null || this.snmpOidInfoHW.getMpCpuStats64Sec().isEmpty())
			return retVal;

		List<VariableBinding> tmpList = null;
		String cpuOid = this.snmpOidInfoHW.getMpCpuStats64Sec();
		// cpu 사용량 조회.
		try {
			tmpList = this.ClassSnmp.walk(cpuOid);
		} catch (Exception e) {
			retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_FAIL);
			OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
			content.setSummary(String.format(OBFaultCheckMsg.HW_CPU_CHECK.getProcessFailMsg()));
			content.setDetail(String.format(OBFaultCheckMsg.SNMP_QUERY.getFailMsg(), cpuOid));
			resultList.add(content);
			msgCLI += String.format(OBFaultCheckMsg.SNMP_QUERY.getFailMsg(), cpuOid);
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
					String.format("failed to query snmp. adcIndex(%d), old(%s)", this.adcInfo.getIndex(), cpuOid));

			retVal.setMsgCLI(msgCLI);
			retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));
			return retVal;
		}

		if (tmpList == null || tmpList.size() == 0) {
			retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_FAIL);
			OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
			content.setSummary(String.format(OBFaultCheckMsg.HW_CPU_CHECK.getProcessFailMsg()));
			content.setDetail(String.format(OBFaultCheckMsg.SNMP_QUERY_VALUE.getFailMsg(), cpuOid, "data not found"));
			resultList.add(content);
			msgCLI += String.format(OBFaultCheckMsg.SNMP_QUERY.getFailMsg(), cpuOid);
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
					String.format("failed to query snmp. adcIndex(%d), old(%s)", this.adcInfo.getIndex(), cpuOid));
			retVal.setMsgCLI(msgCLI);
			retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));
			return retVal;
		}

		try {
			retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_INFO);
			boolean isOkFlag = true;
			String detailMsg = "";
			for (VariableBinding var : tmpList) {
				ArrayList<Integer> oidList = this.ClassSnmp.parseOid(var.getOid().toString(), cpuOid);
				Integer spIndex = 0;// 1.48.xx 형태로 구성됨.
				if (oidList.size() >= 3) {
					spIndex = oidList.get(2);
				} else {
					OBSystemLog.warn(OBDefine.LOGFILE_SYSTEM,
							String.format("invalid parse cpu index, adcIndex:%d, old(%s), var(%s)",
									this.adcInfo.getIndex(), cpuOid, var.getOid().toString()));
					continue;
				}
				Integer spValue = var.getVariable().toInt();

				msgCLI += String.format(OBFaultCheckMsg.SNMP_QUERY_VALUE.getSuccessMsg(), cpuOid, spValue.toString());
				msgCLI += "\n";

				if (!detailMsg.isEmpty())
					detailMsg += "\n";

				detailMsg += String.format(OBFaultCheckMsg.HW_CPU_INFO.getSuccessMsg(), spIndex, spValue);

				if (spValue > max) {
					isOkFlag = false;
				}
			}
			OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
			if (isOkFlag == true)
				content.setSummary(String.format(OBFaultCheckMsg.HW_CPU_CHECK.getSuccessMsg(), max));
			else {
				retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_FAIL);
				content.setSummary(String.format(OBFaultCheckMsg.HW_CPU_CHECK.getFailMsg(), max));
			}
			content.setDetail(detailMsg);
			resultList.add(content);
		} catch (Exception e) {
			retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_FAIL);
			OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
			content.setSummary(String.format(OBFaultCheckMsg.HW_CPU_CHECK.getProcessFailMsg()));
			content.setDetail(String.format(OBFaultCheckMsg.PARSE_DATA.getFailMsg()));
			resultList.add(content);
			msgCLI += String.format(OBFaultCheckMsg.PARSE_DATA.getFailMsg());
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
					String.format("failed to query snmp. adcIndex(%d), old(%s)", this.adcInfo.getIndex(), cpuOid));

			retVal.setMsgCLI(msgCLI);
			retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));
		}

		retVal.setMsgCLI(msgCLI);
		retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));
		return retVal;
	}

	@Override
	public OBDtoFaultCheckResultElement checkHWMemoryStatus(OBDtoElement obj, int max) throws OBException {
		OBDtoFaultCheckResultElement retVal = new OBDtoFaultCheckResultElement();
		retVal.setObj(obj);
		retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_NA);
		ArrayList<OBDtoFaultCheckResultContent> resultList = new ArrayList<OBDtoFaultCheckResultContent>();
		retVal.setResultList(resultList);
		retVal.setMsgCLI("");
		retVal.setStartTime(OBDateTime.toTimestamp(OBDateTime.now()));
		retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));

		String msgCLI = "";

		if (this.snmpOidInfoHW == null || this.snmpOidInfoHW.getMpMemStatsFree().isEmpty()
				|| this.snmpOidInfoHW.getMpMemStatsTotal().isEmpty())
			return retVal;

		List<VariableBinding> tmpList = null;
		String usedOid = this.snmpOidInfoHW.getMpMemStatsFree();// f5의 경우에는 used oid임.
		String totalOid = this.snmpOidInfoHW.getMpMemStatsTotal();
		Long totalValue = 0L;
		Long usedValue = 0L;
		try {
			// total 조회.
			tmpList = this.ClassSnmp.walk(totalOid);
		} catch (Exception e) {
			retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_FAIL);
			OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
			content.setSummary(String.format(OBFaultCheckMsg.HW_MEMORY_CHECK.getProcessFailMsg()));
			content.setDetail(String.format(OBFaultCheckMsg.SNMP_QUERY.getFailMsg(), totalOid));
			resultList.add(content);
			msgCLI += String.format(OBFaultCheckMsg.SNMP_QUERY.getFailMsg(), totalOid);
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
					String.format("failed to query snmp. adcIndex(%d), old(%s)", this.adcInfo.getIndex(), totalOid));

			retVal.setMsgCLI(msgCLI);
			retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));
		}

		try {
			if (tmpList != null && tmpList.size() > 0) {
				VariableBinding var = tmpList.get(0);
				totalValue = var.getVariable().toLong();//

				msgCLI += String.format(OBFaultCheckMsg.SNMP_QUERY_VALUE.getSuccessMsg(), totalOid,
						totalValue.toString());
				msgCLI += "\n";
			} else {
				retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_NA);
				OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
				content.setSummary(String.format(OBFaultCheckMsg.NOT_SUPPORT.getSuccessMsg()));
				content.setDetail(String.format(OBFaultCheckMsg.NOT_SUPPORT.getSuccessMsg(), totalOid));
				resultList.add(content);
				msgCLI += String.format(OBFaultCheckMsg.SNMP_QUERY.getFailMsg(), totalOid);
//				OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("failed to query snmp. adcIndex(%d), old(%s)", this.adcInfo.getIndex(), totalOid));

				retVal.setMsgCLI(msgCLI);
				retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));
				return retVal;
			}
		} catch (Exception e) {
			retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_FAIL);
			OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
			content.setSummary(String.format(OBFaultCheckMsg.HW_MEMORY_CHECK.getProcessFailMsg()));
			content.setDetail(String.format(OBFaultCheckMsg.PARSE_DATA.getFailMsg()));
			resultList.add(content);
			msgCLI += String.format(OBFaultCheckMsg.PARSE_DATA.getFailMsg());
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
					String.format("failed to query snmp. adcIndex(%d), old(%s)", this.adcInfo.getIndex(), totalOid));

			retVal.setMsgCLI(msgCLI);
			retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));
			return retVal;
		}

		// free 조회.
		try {
			tmpList = this.ClassSnmp.walk(usedOid);
			if (tmpList != null && tmpList.size() > 0) {
				VariableBinding var = tmpList.get(0);
				usedValue = var.getVariable().toLong();//

				msgCLI += String.format(OBFaultCheckMsg.SNMP_QUERY_VALUE.getSuccessMsg(), usedOid,
						usedValue.toString());
				msgCLI += "\n";
			}

			if (totalValue == 0) {
				retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_FAIL);
				OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
				content.setSummary(String.format(OBFaultCheckMsg.HW_MEMORY_CHECK.getProcessFailMsg()));
				content.setDetail(String.format(OBFaultCheckMsg.PARSE_DATA.getFailMsg()));
				resultList.add(content);
				msgCLI += String.format(OBFaultCheckMsg.PARSE_DATA.getFailMsg());
				OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("failed to query snmp. adcIndex(%d), old(%s)",
						this.adcInfo.getIndex(), totalOid));

				retVal.setMsgCLI(msgCLI);
				retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));
				return retVal;
			}
			OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
			Long usage = usedValue * 100L / totalValue;
			if (usage < max) {// 정상 케이스.
				retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_SUCC);
				content.setSummary(String.format(OBFaultCheckMsg.HW_MEMORY_CHECK.getSuccessMsg(), usage, max));
			} else {
				retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_FAIL);
				content.setSummary(String.format(OBFaultCheckMsg.HW_MEMORY_CHECK.getFailMsg(), usage, max));
			}
			String totalString = OBUtility.convertKMG(totalValue);
			String usedString = OBUtility.convertKMG(usedValue);
			content.setDetail(
					String.format(OBFaultCheckMsg.HW_MEMORY_USED_INFO.getSuccessMsg(), usage, totalString, usedString));
			resultList.add(content);
		} catch (Exception e) {
			retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_FAIL);
			OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
			content.setSummary(String.format(OBFaultCheckMsg.HW_MEMORY_CHECK.getProcessFailMsg()));
			content.setDetail(String.format(OBFaultCheckMsg.PARSE_DATA.getFailMsg()));
			resultList.add(content);
			msgCLI += String.format(OBFaultCheckMsg.PARSE_DATA.getFailMsg());
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
					String.format("failed to query snmp. adcIndex(%d), old(%s)", this.adcInfo.getIndex(), totalOid));

			retVal.setMsgCLI(msgCLI);
			retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));
			return retVal;
		}

		retVal.setMsgCLI(msgCLI);
		retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));
		return retVal;
	}

	@Override
	public OBDtoFaultCheckResultElement checkHWTemperature(OBDtoElement obj) throws OBException {
		OBDtoFaultCheckResultElement retVal = new OBDtoFaultCheckResultElement();
		retVal.setObj(obj);
		retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_NA);
		ArrayList<OBDtoFaultCheckResultContent> resultList = new ArrayList<OBDtoFaultCheckResultContent>();
		retVal.setResultList(resultList);
		retVal.setMsgCLI("");
		retVal.setStartTime(OBDateTime.toTimestamp(OBDateTime.now()));
		retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));

		String msgCLI = "";

		if (this.snmpOidInfoHW == null || this.snmpOidInfoHW.getHwTemperatureStatus().isEmpty())
			return retVal;

		List<VariableBinding> tmpList;
		String oid = this.snmpOidInfoHW.getHwTemperatureStatus();
		try {
			tmpList = this.ClassSnmp.walk(oid);
		} catch (Exception e) {
			retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_FAIL);
			OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
			content.setSummary(String.format(OBFaultCheckMsg.HW_TEMP_GAUGE_CHECK.getProcessFailMsg()));
			content.setDetail(String.format(OBFaultCheckMsg.SNMP_QUERY.getFailMsg(), oid));
			resultList.add(content);
			msgCLI += String.format(OBFaultCheckMsg.SNMP_QUERY.getFailMsg(), oid);
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
					String.format("failed to query snmp. adcIndex(%d), old(%s)", this.adcInfo.getIndex(), oid));

			retVal.setMsgCLI(msgCLI);
			retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));
			return retVal;
		}

		if (tmpList == null || tmpList.isEmpty()) {
			retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_NA);
			OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
			content.setSummary(String.format(OBFaultCheckMsg.DATA_NOT_EXIST.getSuccessMsg()));
			content.setDetail(String.format(OBFaultCheckMsg.SNMP_QUERY.getSuccessMsg(), oid));
			resultList.add(content);
			msgCLI += String.format(OBFaultCheckMsg.SNMP_QUERY.getSuccessMsg(), oid);

			retVal.setMsgCLI(msgCLI);
			retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));
			return retVal;
		}

		try {
			retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_SUCC);
			Integer index = 1;
			String detailMsg = "";
			boolean isOkFlag = true;
			for (VariableBinding var : tmpList) {
				Integer status = var.getVariable().toInt();// 온도 상태 검사. Celsius.

				msgCLI += String.format(OBFaultCheckMsg.SNMP_QUERY_VALUE.getSuccessMsg(), oid, status.toString());
				msgCLI += "\n";

				if (!detailMsg.isEmpty())
					detailMsg += "\n";
				detailMsg += String.format(OBFaultCheckMsg.HW_TEMP_GAUGE_INFO.getSuccessMsg(), index, status);

				if (status > this.temperatureMax) {
					retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_FAIL);
					isOkFlag = false;
				}
				index++;
			}
			OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
			if (isOkFlag == true)
				content.setSummary(
						String.format(OBFaultCheckMsg.HW_TEMP_GAUGE_CHECK.getSuccessMsg(), this.temperatureMax));
			else
				content.setSummary(
						String.format(OBFaultCheckMsg.HW_TEMP_GAUGE_CHECK.getFailMsg(), this.temperatureMax));
			content.setDetail(detailMsg);
			resultList.add(content);
		} catch (Exception e) {
			retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_FAIL);
			OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
			content.setSummary(String.format(OBFaultCheckMsg.HW_TEMP_GAUGE_CHECK.getProcessFailMsg()));
			content.setDetail(String.format(OBFaultCheckMsg.PARSE_DATA.getFailMsg()));
			resultList.add(content);
			msgCLI += String.format(OBFaultCheckMsg.PARSE_DATA.getFailMsg());
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
					String.format("failed to query snmp. adcIndex(%d), old(%s)", this.adcInfo.getIndex(), oid));

			retVal.setMsgCLI(msgCLI);
			retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));
			return retVal;
		}

		retVal.setMsgCLI(msgCLI);
		retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));
		return retVal;
	}

	@Override
	public OBDtoFaultCheckResultElement checkHWFanStatus(OBDtoElement obj, int min, int max) throws OBException {
		OBDtoFaultCheckResultElement retVal = new OBDtoFaultCheckResultElement();
		retVal.setObj(obj);
		retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_NA);
		ArrayList<OBDtoFaultCheckResultContent> resultList = new ArrayList<OBDtoFaultCheckResultContent>();
		retVal.setResultList(resultList);
		retVal.setMsgCLI("");
		retVal.setStartTime(OBDateTime.toTimestamp(OBDateTime.now()));
		retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));

		String msgCLI = "";

		if (this.snmpOidInfoHW == null || this.snmpOidInfoHW.getHwFanStatus().isEmpty())
			return retVal;

		List<VariableBinding> tmpList;
		String oid = this.snmpOidInfoHW.getHwFanSpeed();
		try {
			tmpList = this.ClassSnmp.walk(oid);
		} catch (Exception e) {
			retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_FAIL);
			OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
			content.setSummary(String.format(OBFaultCheckMsg.HW_FAN_RPM_CHECK.getProcessFailMsg()));
			content.setDetail(String.format(OBFaultCheckMsg.SNMP_QUERY.getFailMsg(), oid));
			resultList.add(content);
			msgCLI += String.format(OBFaultCheckMsg.SNMP_QUERY.getFailMsg(), oid);
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
					String.format("failed to query snmp. adcIndex(%d), old(%s)", this.adcInfo.getIndex(), oid));

			retVal.setMsgCLI(msgCLI);
			retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));
			return retVal;
		}

		if (tmpList == null || tmpList.isEmpty()) {
			retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_NA);
			OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
			content.setSummary(String.format(OBFaultCheckMsg.NOT_SUPPORT.getSuccessMsg()));
			content.setDetail(String.format(OBFaultCheckMsg.NOT_SUPPORT.getSuccessMsg(), oid));
			resultList.add(content);
			msgCLI += String.format(OBFaultCheckMsg.SNMP_QUERY.getSuccessMsg(), oid);

			retVal.setMsgCLI(msgCLI);
			retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));
			return retVal;
		}

		try {
			retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_SUCC);
			Integer index = 1;
			String detailMsg = "";
			boolean isOkFlag = true;
			for (VariableBinding var : tmpList) {
				int fanRpm = var.getVariable().toInt();// fan 회전수.

				msgCLI += String.format(OBFaultCheckMsg.SNMP_QUERY_VALUE.getSuccessMsg(), oid, fanRpm);
				msgCLI += "\n";

				if (!detailMsg.isEmpty())
					detailMsg += "\n";
				detailMsg += String.format(OBFaultCheckMsg.HW_FAN_RPM_INFO.getSuccessMsg(), index, fanRpm);

				if (fanRpm >= max || fanRpm < min)
					isOkFlag = false;
				index++;
			}

			OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
			if (isOkFlag == false) {
				retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_FAIL);
				content.setSummary(String.format(OBFaultCheckMsg.HW_FAN_RPM_CHECK.getFailMsg(), min, max));
			} else
				content.setSummary(String.format(OBFaultCheckMsg.HW_FAN_RPM_CHECK.getSuccessMsg(), min, max));
			content.setDetail(detailMsg);
			resultList.add(content);
		} catch (Exception e) {
			retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_FAIL);
			OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
			content.setSummary(String.format(OBFaultCheckMsg.HW_FAN_RPM_CHECK.getProcessFailMsg()));
			content.setDetail(String.format(OBFaultCheckMsg.PARSE_DATA.getFailMsg()));
			resultList.add(content);
			msgCLI += String.format(OBFaultCheckMsg.PARSE_DATA.getFailMsg());
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
					String.format("failed to query snmp. adcIndex(%d), old(%s)", this.adcInfo.getIndex(), oid));

			retVal.setMsgCLI(msgCLI);
			retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));
			return retVal;
		}

		retVal.setMsgCLI(msgCLI);
		retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));
		return retVal;
	}

	@Override
	public OBDtoFaultCheckResultElement checkHWAdclog(OBDtoElement obj, Integer logCount) throws OBException {
		OBDtoFaultCheckResultElement retVal = new OBDtoFaultCheckResultElement();
		retVal.setObj(obj);
		retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_NA);
		ArrayList<OBDtoFaultCheckResultContent> resultList = new ArrayList<OBDtoFaultCheckResultContent>();
		retVal.setResultList(resultList);
		retVal.setMsgCLI("");
		retVal.setStartTime(OBDateTime.toTimestamp(OBDateTime.now()));
		retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));

		String msgCLI = "";

		try {
			OBDtoSearch searchOption = new OBDtoSearch();
			searchOption.setBeginIndex(0);
			searchOption.setEndIndex(logCount - 1);
			OBDtoADCObject adcObject = new OBDtoADCObject();
			adcObject.setCategory(OBDtoADCObject.CATEGORY_ADC);
			adcObject.setIndex(adcInfo.getIndex());

			ArrayList<OBDtoAuditLogAdcSystem> adcLogList = new OBAdcManagementImpl().getAdcAuditLogExOrdering(adcObject,
					searchOption, null, null);

			retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_INFO);
			// adc 로그 목록이 출력된다.
			String detailMsg = "";
			for (OBDtoAuditLogAdcSystem log : adcLogList) {
				detailMsg += OBDateTime.toString(new Timestamp(log.getOccurTime().getTime())) + " :: "
						+ log.getContents() + "\n";
			}
			OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
			if (detailMsg.isEmpty())
				content.setSummary(String.format(OBFaultCheckMsg.HW_ADCLOG_CHECK.getFailMsg()));
			else
				content.setSummary(String.format(OBFaultCheckMsg.HW_ADCLOG_CHECK.getSuccessMsg(), adcLogList.size()));
			content.setDetail(detailMsg);
			resultList.add(content);
		} catch (Exception e) {
			retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_FAIL);
			OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
			content.setSummary(String.format(OBFaultCheckMsg.HW_ADCLOG_CHECK.getProcessFailMsg()));
			content.setDetail(String.format(OBFaultCheckMsg.DB_QUERY.getFailMsg()));
			resultList.add(content);
			msgCLI += String.format(OBFaultCheckMsg.DB_QUERY.getFailMsg());
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
					String.format("failed to query db. adcIndex(%d)", this.adcInfo.getIndex()));

			retVal.setMsgCLI(msgCLI);
			retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));
			return retVal;
		}
		retVal.setMsgCLI(msgCLI);
		retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));
		return retVal;
	}

	// CLI 명령이나 SNMP 질의를 통해 데이터를 수집하여 제공한다.
	//// up된 포트 확인 : b interface
	//// Vlan 리스트 : b vlan list
	@Override
	public OBDtoFaultCheckResultElement checkL23VlanInfo(OBDtoElement obj) throws OBException {
		OBDtoFaultCheckResultElement retVal = new OBDtoFaultCheckResultElement();
		retVal.setObj(obj);
		retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_NA);
		ArrayList<OBDtoFaultCheckResultContent> resultList = new ArrayList<OBDtoFaultCheckResultContent>();
		retVal.setResultList(resultList);
		retVal.setMsgCLI("");
		retVal.setStartTime(OBDateTime.toTimestamp(OBDateTime.now()));
		retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));
		OBDtoFaultCheckResultContent content = null;
		String msgCLI = "";

		String retMsg = "";
		try {
			// 데이터 추출.
			this.ClassSsh.cmndInfoVlan();
			retMsg = this.ClassSsh.getCmndRetString();
		} catch (Exception e) {
			retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_FAIL);
			content = new OBDtoFaultCheckResultContent();
			content.setSummary(OBFaultCheckMsg.L23_VLAN_CHECK.getProcessFailMsg());
			content.setDetail(String.format(OBFaultCheckMsg.SSH_QUERY.getFailMsg(), retMsg));
			resultList.add(content);
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
					String.format("failed to get ssh data. adcIndex(%d), cli(%s)", this.adcInfo.getIndex(), msgCLI));

			retVal.setMsgCLI(msgCLI);
			retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));
			return retVal;
		}

		if (retMsg == null || retMsg.isEmpty()) {
			retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_FAIL);
			content = new OBDtoFaultCheckResultContent();
			content.setSummary(OBFaultCheckMsg.L23_VLAN_CHECK.getFailMsg());
			content.setDetail(String.format(OBFaultCheckMsg.DATA_NOT_EXIST.getFailMsg(), retMsg));
			resultList.add(content);
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
					String.format("failed to get ssh data. adcIndex(%d), cli(%s)", this.adcInfo.getIndex(), msgCLI));

			retVal.setMsgCLI(msgCLI);
			retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));
			return retVal;
		}
		// 데이터 저장.
		retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_INFO);
		content = new OBDtoFaultCheckResultContent();
		content.setSummary(OBFaultCheckMsg.L23_VLAN_CHECK.getSuccessMsg());
		content.setDetail(retMsg);
		resultList.add(content);
		msgCLI += retMsg;

		try {
			// Interface 데이터 추출.
			this.ClassSsh.cmndInfoInterface();
			retMsg = this.ClassSsh.getCmndRetString();
		} catch (Exception e) {
			retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_FAIL);
			content = new OBDtoFaultCheckResultContent();
			content.setSummary(OBFaultCheckMsg.L23_INTERFACE_CHECK.getProcessFailMsg());
			content.setDetail(String.format(OBFaultCheckMsg.SSH_QUERY.getFailMsg(), retMsg));
			resultList.add(content);
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
					String.format("failed to get ssh data. adcIndex(%d), cli(%s)", this.adcInfo.getIndex(), msgCLI));

			retVal.setMsgCLI(msgCLI);
			retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));
			return retVal;
		}

		if (retMsg == null || retMsg.isEmpty()) {
			retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_FAIL);
			content = new OBDtoFaultCheckResultContent();
			content.setSummary(OBFaultCheckMsg.L23_INTERFACE_CHECK.getFailMsg());
			content.setDetail(String.format(OBFaultCheckMsg.DATA_NOT_EXIST.getFailMsg(), retMsg));
			resultList.add(content);
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
					String.format("failed to get ssh data. adcIndex(%d), cli(%s)", this.adcInfo.getIndex(), msgCLI));

			retVal.setMsgCLI(msgCLI);
			retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));
			return retVal;
		}
		// 데이터 저장.
		retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_INFO);
		content = new OBDtoFaultCheckResultContent();
		content.setSummary(OBFaultCheckMsg.L23_INTERFACE_CHECK.getSuccessMsg());
		content.setDetail(retMsg);
		resultList.add(content);
		msgCLI += retMsg;
		retVal.setMsgCLI(msgCLI);
		retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));
		return retVal;
	}

	@Override
	public OBDtoFaultCheckResultElement checkL23StpInfo(OBDtoElement obj) throws OBException {
		OBDtoFaultCheckResultElement retVal = new OBDtoFaultCheckResultElement();
		retVal.setObj(obj);
		retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_NA);
		ArrayList<OBDtoFaultCheckResultContent> resultList = new ArrayList<OBDtoFaultCheckResultContent>();
		retVal.setResultList(resultList);
		retVal.setMsgCLI("");
		retVal.setStartTime(OBDateTime.toTimestamp(OBDateTime.now()));
		retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));
		OBDtoFaultCheckResultContent content = null;
		String msgCLI = "";

		String retMsg = "";
		try {
			// 데이터 추출.
			this.ClassSsh.cmndInfoStg();
			retMsg = this.ClassSsh.getCmndRetString();
		} catch (OBException e) {
			retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_FAIL);
			content = new OBDtoFaultCheckResultContent();
			content.setSummary(OBFaultCheckMsg.L23_STP_CHECK.getProcessFailMsg());
			content.setDetail(String.format(OBFaultCheckMsg.SSH_QUERY.getFailMsg(), retMsg));
			resultList.add(content);
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
					String.format("failed to get telnet data. adcIndex(%d), cli(%s)", this.adcInfo.getIndex(), msgCLI));

			retVal.setMsgCLI(msgCLI);
			retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));
			return retVal;
		}

		// 데이터 분석.
		if (retMsg == null || retMsg.isEmpty()) {
			retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_FAIL);
			content = new OBDtoFaultCheckResultContent();
			content.setSummary(OBFaultCheckMsg.L23_STP_CHECK.getFailMsg());
			content.setDetail(String.format(OBFaultCheckMsg.DATA_NOT_EXIST.getFailMsg(), retMsg));
			resultList.add(content);
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
					String.format("failed to get ssh data. adcIndex(%d), cli(%s)", this.adcInfo.getIndex(), retMsg));

			retVal.setMsgCLI(retMsg);
			retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));
			return retVal;
		}

		retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_INFO);

		content = new OBDtoFaultCheckResultContent();
		content.setSummary(OBFaultCheckMsg.L23_STP_CHECK.getSuccessMsg());
		content.setDetail(retMsg);
		resultList.add(content);
		msgCLI += retMsg;

		// 변화를 감지해야 한다. TODO
		retVal.setMsgCLI(msgCLI);
		retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));
		return retVal;
	}

	@Override
	public OBDtoFaultCheckResultElement checkL23TrunkInfo(OBDtoElement obj) throws OBException {
		OBDtoFaultCheckResultElement retVal = new OBDtoFaultCheckResultElement();
		retVal.setObj(obj);
		retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_NA);
		ArrayList<OBDtoFaultCheckResultContent> resultList = new ArrayList<OBDtoFaultCheckResultContent>();
		retVal.setResultList(resultList);
		retVal.setMsgCLI("");
		retVal.setStartTime(OBDateTime.toTimestamp(OBDateTime.now()));
		retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));
		OBDtoFaultCheckResultContent content = null;
		String msgCLI = "";

//		String cliMsg = "";
		String retMsg = "";

		// trunk 정보 추출.
		try {
			// 데이터 추출.
			this.ClassSsh.cmndInfoTrunk();
			retMsg = this.ClassSsh.getCmndRetString();
		} catch (OBException e) {
			retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_FAIL);
			content = new OBDtoFaultCheckResultContent();
			content.setSummary(OBFaultCheckMsg.L23_TRUNK_CHECK.getProcessFailMsg());
			content.setDetail(String.format(OBFaultCheckMsg.SSH_QUERY.getFailMsg(), retMsg));
			resultList.add(content);
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
					String.format("failed to get telnet data. adcIndex(%d), cli(%s)", this.adcInfo.getIndex(), retMsg));

			retVal.setMsgCLI(retMsg);
			retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));
			return retVal;
		}

		retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_INFO);

		// 데이터 분석.
		if (retMsg == null || retMsg.isEmpty()) {
			content = new OBDtoFaultCheckResultContent();
			content.setSummary(OBFaultCheckMsg.L23_TRUNK_CHECK.getProcessFailMsg());
			resultList.add(content);
		} else {
			content = new OBDtoFaultCheckResultContent();
			content.setSummary(OBFaultCheckMsg.L23_TRUNK_CHECK.getSuccessMsg());
			content.setDetail(retMsg);
			resultList.add(content);
			msgCLI += retMsg;
		}
		retVal.setMsgCLI(msgCLI);
		retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));

		return retVal;
	}

	@Override
	public OBDtoFaultCheckResultElement checkL23VrrpInfo(OBDtoElement obj) throws OBException {
		OBDtoFaultCheckResultElement retVal = new OBDtoFaultCheckResultElement();
		retVal.setObj(obj);
		retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_NA);
		ArrayList<OBDtoFaultCheckResultContent> resultList = new ArrayList<OBDtoFaultCheckResultContent>();
		retVal.setResultList(resultList);
		retVal.setMsgCLI("");
		retVal.setStartTime(OBDateTime.toTimestamp(OBDateTime.now()));
		retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));
		OBDtoFaultCheckResultContent content = null;
		String msgCLI = "";

//		String cliMsg = "";
		String retMsg = "";

		try {
			// 데이터 추출.
			this.ClassSsh.cmndInfoFailover();
			retMsg = this.ClassSsh.getCmndRetString();
		} catch (OBException e) {
			retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_FAIL);
			content = new OBDtoFaultCheckResultContent();
			content.setSummary(OBFaultCheckMsg.L23_FAILOVER_CHECK.getProcessFailMsg());
			content.setDetail(String.format(OBFaultCheckMsg.SSH_QUERY.getFailMsg(), retMsg));
			resultList.add(content);
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
					String.format("failed to get telnet data. adcIndex(%d), cli(%s)", this.adcInfo.getIndex(), retMsg));

			retVal.setMsgCLI(retMsg);
			retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));
			return retVal;
		}

		// 데이터 분석.
		if (retMsg == null || retMsg.isEmpty()) {
			retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_FAIL);
			content = new OBDtoFaultCheckResultContent();
			content.setSummary(OBFaultCheckMsg.L23_FAILOVER_CHECK.getProcessFailMsg());
			content.setDetail(String.format(OBFaultCheckMsg.DATA_NOT_EXIST.getFailMsg(), retMsg));
			resultList.add(content);
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
					String.format("failed to get ssh data. adcIndex(%d), cli(%s)", this.adcInfo.getIndex(), retMsg));

			retVal.setMsgCLI(retMsg);
			retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));
			return retVal;
		}

		int failoverType = 0;
		msgCLI += retMsg;
		try {
			failoverType = new OBCLIParserF5().parseFailoverType(retMsg);
		} catch (OBException e) {
			retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_FAIL);

			content = new OBDtoFaultCheckResultContent();
			content.setSummary(OBFaultCheckMsg.L23_FAILOVER_CHECK.getProcessFailMsg());
			content.setDetail(OBFaultCheckMsg.PARSE_DATA.getFailMsg());
			resultList.add(content);

			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
					String.format("failed to parse data. adcIndex(%d), cli(%s)", this.adcInfo.getIndex(), msgCLI));

			retVal.setMsgCLI(msgCLI);
			retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));
			return retVal;
		}

		if (failoverType == 1)// active
		{
			retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_INFO);

			content = new OBDtoFaultCheckResultContent();
			content.setSummary(String.format(OBFaultCheckMsg.L23_FAILOVER_CHECK.getSuccessMsg(), "ACTIVE"));
			content.setDetail(retMsg);
			resultList.add(content);

			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
					String.format("failed to parse data. adcIndex(%d), cli(%s)", this.adcInfo.getIndex(), msgCLI));

			retVal.setMsgCLI(msgCLI);
			retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));
		} else if (failoverType == 2) {
			retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_INFO);

			content = new OBDtoFaultCheckResultContent();
			content.setSummary(String.format(OBFaultCheckMsg.L23_FAILOVER_CHECK.getSuccessMsg(), "STANDBY"));
			content.setDetail(retMsg);
			resultList.add(content);

			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
					String.format("failed to parse data. adcIndex(%d), cli(%s)", this.adcInfo.getIndex(), msgCLI));

			retVal.setMsgCLI(msgCLI);
			retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));
		} else {
			retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_FAIL);
			content = new OBDtoFaultCheckResultContent();
			content.setSummary(OBFaultCheckMsg.L23_FAILOVER_CHECK.getProcessFailMsg());
			content.setDetail(String.format(OBFaultCheckMsg.DATA_NOT_EXIST.getFailMsg(), retMsg));
			resultList.add(content);
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
					String.format("failed to get ssh data. adcIndex(%d), cli(%s)", this.adcInfo.getIndex(), retMsg));

			retVal.setMsgCLI(retMsg);
			retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));
			return retVal;
		}

		retVal.setMsgCLI(msgCLI);
		retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));

		return retVal;
	}

	@Override
	public OBDtoFaultCheckResultElement checkL23RoutingInfo(OBDtoElement obj) throws OBException {
		OBDtoFaultCheckResultElement retVal = new OBDtoFaultCheckResultElement();
		retVal.setObj(obj);
		retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_NA);
		ArrayList<OBDtoFaultCheckResultContent> resultList = new ArrayList<OBDtoFaultCheckResultContent>();
		retVal.setResultList(resultList);
		retVal.setMsgCLI("");
		retVal.setStartTime(OBDateTime.toTimestamp(OBDateTime.now()));
		retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));
		String msgCLI = "";
		retVal.setMsgCLI(msgCLI);
		retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));
		return retVal;
	}

	@Override
	public OBDtoFaultCheckResultElement checkL23InterfaceInfo(OBDtoElement obj) throws OBException {
		OBDtoFaultCheckResultElement retVal = new OBDtoFaultCheckResultElement();
		retVal.setObj(obj);
		retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_NA);
		ArrayList<OBDtoFaultCheckResultContent> resultList = new ArrayList<OBDtoFaultCheckResultContent>();
		retVal.setResultList(resultList);
		retVal.setMsgCLI("");
		retVal.setStartTime(OBDateTime.toTimestamp(OBDateTime.now()));
		retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));
		OBDtoFaultCheckResultContent content = null;
		String msgCLI = "";

		String retMsg = "";
		try {
			// 데이터 추출.
			this.ClassSsh.cmndInfoSelfIP();
			retMsg = this.ClassSsh.getCmndRetString();
		} catch (OBException e) {
			retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_FAIL);

			content = new OBDtoFaultCheckResultContent();
			content.setSummary(OBFaultCheckMsg.L23_INTERFACE_CHECK.getProcessFailMsg());
			content.setDetail(OBFaultCheckMsg.PARSE_DATA.getFailMsg());
			resultList.add(content);
			msgCLI += retMsg;
			msgCLI += "\n";
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
					String.format("failed to parse data. adcIndex(%d), cli(%s)", this.adcInfo.getIndex(), retMsg));

			retVal.setMsgCLI(msgCLI);
			retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));
			return retVal;
		}

		// 데이터 분석.
		if (retMsg == null || retMsg.isEmpty()) {
			content = new OBDtoFaultCheckResultContent();
			content.setSummary(OBFaultCheckMsg.L23_INTERFACE_CHECK.getProcessFailMsg());
			resultList.add(content);
		} else {
			content = new OBDtoFaultCheckResultContent();
			content.setSummary(OBFaultCheckMsg.L23_INTERFACE_CHECK.getSuccessMsg());
			content.setDetail(retMsg);
			resultList.add(content);
		}
		retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_INFO);
		msgCLI += retMsg;

		retVal.setMsgCLI(retMsg);
		retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));
		return retVal;
	}

	@Override
	public OBDtoFaultCheckResultElement checkL47NotUsedSLB(OBDtoElement obj) throws OBException {
		OBDtoFaultCheckResultElement retVal = new OBDtoFaultCheckResultElement();
		retVal.setObj(obj);
		retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_NA);
		ArrayList<OBDtoFaultCheckResultContent> resultList = new ArrayList<OBDtoFaultCheckResultContent>();
		retVal.setResultList(resultList);
		retVal.setMsgCLI("");
		retVal.setStartTime(OBDateTime.toTimestamp(OBDateTime.now()));
		retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));

		String msgCLI = "";

		retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_INFO);

		// 사용되지 않은 노드(REAL SERVER) 조회중.
		try {
			// 데이터 추출.
			ArrayList<OBDtoSlbNodeInfo> nodeList = new OBFaultMngImpl().getUnusedNodeList(this.adcInfo);
			if (nodeList.size() == 0) {
				OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
				content.setSummary(String.format(OBFaultCheckMsg.L47_UNUSED_NODE_CHECK.getFailMsg()));// 데이터 없음.
				content.setDetail("");
				resultList.add(content);
			} else {
				String msg = "";
				int iIndex = 1;
				for (OBDtoSlbNodeInfo nodeObj : nodeList) {
					msg += String.format("%-2d %-30s", iIndex++, nodeObj.getIpAddress());
					msg += "\n";
				}
				OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
				content.setSummary(
						String.format(OBFaultCheckMsg.L47_UNUSED_NODE_CHECK.getSuccessMsg(), nodeList.size()));
				content.setDetail(msg);
				resultList.add(content);
			}
		} catch (OBException e) {
			retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_FAIL);
			OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
			content.setSummary(String.format(OBFaultCheckMsg.L47_UNUSED_NODE_CHECK.getProcessFailMsg()));
			content.setDetail(String.format(OBFaultCheckMsg.DB_QUERY.getFailMsg()));
			resultList.add(content);
			msgCLI += String.format(OBFaultCheckMsg.DB_QUERY.getFailMsg());
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
					String.format("failed to query db. adcIndex(%d)", this.adcInfo.getIndex()));

			retVal.setMsgCLI(msgCLI);
			retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		// 사용되지 않은 POOL(REAL SERVER GROUP) 조회중..
		try {
			// 데이터 추출.
			ArrayList<OBDtoSlbPoolInfo> poolList = new OBFaultMngImpl().getUnusedPoolList(this.adcInfo);
			if (poolList.size() == 0) {
				OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
				content.setSummary(String.format(OBFaultCheckMsg.L47_UNUSED_POOL_CHECK.getFailMsg()));// 데이터 없음.
				content.setDetail("");
				resultList.add(content);
			} else {
				String msg = "";
				int iIndex = 1;
				for (OBDtoSlbPoolInfo poolObj : poolList) {
					msg += String.format("%-2d %-30s", iIndex++, poolObj.getName());
					msg += "\n";
				}
				OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
				content.setSummary(
						String.format(OBFaultCheckMsg.L47_UNUSED_POOL_CHECK.getSuccessMsg(), poolList.size()));
				content.setDetail(msg);
				resultList.add(content);
			}
		} catch (OBException e) {
			retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_FAIL);
			OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
			content.setSummary(String.format(OBFaultCheckMsg.L47_UNUSED_POOL_CHECK.getProcessFailMsg()));
			content.setDetail(String.format(OBFaultCheckMsg.DB_QUERY.getFailMsg()));
			resultList.add(content);
			msgCLI += String.format(OBFaultCheckMsg.DB_QUERY.getFailMsg());
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
					String.format("failed to query db. adcIndex(%d)", this.adcInfo.getIndex()));

			retVal.setMsgCLI(msgCLI);
			retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		// 노드(REAL SERVER)가 할당되지 않은 POOL(REAL SERVER GROUP) 조회중...
		try {
			// 데이터 추출.
			ArrayList<OBDtoSlbPoolInfo> poolList = new OBFaultMngImpl().getNoMemberPoolList(this.adcInfo);
			if (poolList.size() == 0) {
				OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
				content.setSummary(String.format(OBFaultCheckMsg.L47_NOMEMBER_POOOL_CHECK.getFailMsg()));
				content.setDetail("");
				resultList.add(content);
			} else {
				String msg = "";
				int iIndex = 1;
				for (OBDtoSlbPoolInfo poolObj : poolList) {
					msg += String.format("%-2d %-30s", iIndex++, poolObj.getName());
					msg += "\n";
				}
				OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
				content.setSummary(
						String.format(OBFaultCheckMsg.L47_NOMEMBER_POOOL_CHECK.getSuccessMsg(), poolList.size()));
				content.setDetail(msg);
				resultList.add(content);
			}
		} catch (OBException e) {
			retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_FAIL);
			OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
			content.setSummary(String.format(OBFaultCheckMsg.L47_NOMEMBER_POOOL_CHECK.getProcessFailMsg()));
			content.setDetail(String.format(OBFaultCheckMsg.DB_QUERY.getFailMsg()));
			resultList.add(content);
			msgCLI += String.format(OBFaultCheckMsg.DB_QUERY.getFailMsg());
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
					String.format("failed to query db. adcIndex(%d)", this.adcInfo.getIndex()));

			retVal.setMsgCLI(msgCLI);
			retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		// POOL(REAL SERVER GROUP)이 할당되지 않은 VIRTUAL SERVER 조회중...
		try {
			// 데이터 추출.
			ArrayList<OBDtoSlbVServerInfo> vsList = new OBFaultMngImpl().getNoMemberVServerList(this.adcInfo);
			if (vsList.size() == 0) {
				OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
				content.setSummary(String.format(OBFaultCheckMsg.L47_NOMEMBER_VSERVER_CHECK.getFailMsg()));
				resultList.add(content);
			} else {
				String msg = "";
				for (OBDtoSlbVServerInfo vsObj : vsList) {
					msg += String.format("%-30s %-30s %-30s", vsObj.getIndex(), vsObj.getName(), vsObj.getIpAddress());
					msg += "\n";
				}
				OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
				content.setSummary(
						String.format(OBFaultCheckMsg.L47_NOMEMBER_VSERVER_CHECK.getSuccessMsg(), vsList.size()));
				content.setDetail(msg);
				resultList.add(content);
			}
		} catch (Exception e) {
			retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_FAIL);
			OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
			content.setSummary(String.format(OBFaultCheckMsg.L47_NOMEMBER_VSERVER_CHECK.getProcessFailMsg()));
			content.setDetail(String.format(OBFaultCheckMsg.DB_QUERY.getFailMsg()));
			resultList.add(content);
			msgCLI += String.format(OBFaultCheckMsg.DB_QUERY.getFailMsg());
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
					String.format("failed to query db. adcIndex(%d)", this.adcInfo.getIndex()));

			retVal.setMsgCLI(msgCLI);
			retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));
			return retVal;
		}

		retVal.setMsgCLI(msgCLI);
		retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));
		return retVal;
	}

	@Override
	public OBDtoFaultCheckResultElement checkL47SleepSLB(OBDtoElement obj, Integer sleepDay) throws OBException {
		OBDtoFaultCheckResultElement retVal = new OBDtoFaultCheckResultElement();
		retVal.setObj(obj);
		retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_NA);
		ArrayList<OBDtoFaultCheckResultContent> resultList = new ArrayList<OBDtoFaultCheckResultContent>();
		retVal.setResultList(resultList);
		retVal.setMsgCLI("");
		retVal.setStartTime(OBDateTime.toTimestamp(OBDateTime.now()));
		retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));

		String msgCLI = "";

		retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_INFO);

		/*
		 * // 유휴 노드 추출. //분석-진단에서 L4-7 유휴설정에 "SLB : 일 동안 사용하지 않은 REAL SERVER 없음" 기능 막음.
		 * SLB real data 수집 기능이 현재 없음.junhyun.ok_GS try { // 데이터 추출.
		 * ArrayList<OBDtoSlbNodeInfo> nodeList = new
		 * OBFaultMngImpl().getSleepNodeList(this.adcInfo, sleepDay, this.db);
		 * if(nodeList.size()==0) { OBDtoFaultCheckResultContent content = new
		 * OBDtoFaultCheckResultContent();
		 * content.setSummary(String.format(OBFaultCheckMsg.L47_SLEEP_NODE_CHECK.
		 * getFailMsg(), sleepDay)); content.setDetail(""); resultList.add(content); }
		 * else { String msg = ""; for(OBDtoSlbNodeInfo nodeObj: nodeList) { msg +=
		 * String.format("%-30s", nodeObj.getIndex()); msg +="\n"; }
		 * OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
		 * content.setSummary(String.format(OBFaultCheckMsg.L47_SLEEP_NODE_CHECK.
		 * getSuccessMsg(), sleepDay, nodeList.size())); content.setDetail(msg);
		 * resultList.add(content); } } catch(OBException e) {
		 * retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_FAIL);
		 * OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
		 * content.setSummary(String.format(OBFaultCheckMsg.L47_SLEEP_NODE_CHECK.
		 * getProcessFailMsg()));
		 * content.setDetail(String.format(OBFaultCheckMsg.DB_QUERY.getFailMsg()));
		 * resultList.add(content); msgCLI +=
		 * String.format(OBFaultCheckMsg.DB_QUERY.getFailMsg());
		 * OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
		 * String.format("failed to query db. adcIndex(%d)", this.adcInfo.getIndex()));
		 * 
		 * retVal.setMsgCLI(msgCLI);
		 * retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now())); }
		 */

		// 유휴 POOL 추출.
		try {
			// 데이터 추출.
			ArrayList<OBDtoSlbPoolInfo> poolList = new OBFaultMngImpl().getSleepPoolList(this.adcInfo, sleepDay);
			if (poolList.size() == 0) {
				OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
				content.setSummary(String.format(OBFaultCheckMsg.L47_SLEEP_POOL_CHECK.getFailMsg(), sleepDay));
				content.setDetail("");
				resultList.add(content);
			} else {
				String msg = "";
				Integer index = 1;
				for (OBDtoSlbPoolInfo poolObj : poolList) {
					msg += String.format("%-30s %s", index.toString(), poolObj.getName());
					index++;
					msg += "\n";
				}
				OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
				content.setSummary(
						String.format(OBFaultCheckMsg.L47_SLEEP_POOL_CHECK.getSuccessMsg(), sleepDay, poolList.size()));
				content.setDetail(msg);
				resultList.add(content);
			}
		} catch (OBException e) {
			retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_FAIL);
			OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
			content.setSummary(String.format(OBFaultCheckMsg.L47_SLEEP_POOL_CHECK.getProcessFailMsg()));
			content.setDetail(String.format(OBFaultCheckMsg.DB_QUERY.getFailMsg()));
			resultList.add(content);
			msgCLI += String.format(OBFaultCheckMsg.DB_QUERY.getFailMsg());
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
					String.format("failed to query db. adcIndex(%d)", this.adcInfo.getIndex()));

			retVal.setMsgCLI(msgCLI);
			retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));
		}

		// 유휴 VSERVER 추출.

		retVal.setMsgCLI(msgCLI);
		retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));
		return retVal;
	}

//	private ArrayList<String> getVlanNameList() throws OBException
//	{
//		ArrayList<String> retVal = new ArrayList<String>();
//		if(this.snmpOidInfoL23==null || this.snmpOidInfoL23.getVlanInfoName().isEmpty())
//			return retVal;
//		
//		List<VariableBinding> tmpList=null;
//		String oid = this.snmpOidInfoL23.getVlanInfoName();
//		try
//		{
//			tmpList = this.ClassSnmp.walk(oid, this.adcInfo.getSnmpRComm());
//		}
//		catch(OBException e)
//		{
//			throw e;
//		}
//		
//		try
//		{
//			for(VariableBinding var:tmpList)
//			{
//				String vlanName = var.getVariable().toString();
//				retVal.add(vlanName);
//			}
//			return retVal;
//		}
//		catch(Exception e)
//		{
//			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
//		}
//	}

	private String makeTcpdumpOptionF5(String clientIPAddress, Integer svcPort)// String serverIPAddress)
	{
		String retVal = "";
		retVal = String.format(" -c %d -s %d tcp and host %s and port %s ", this.packetDumpCount, this.packetDumpLength,
				clientIPAddress, svcPort);
		return retVal;
	}

	@Override
	public OBDtoFaultCheckResultElement startSvcPacketDump(OBDtoElement obj, Long logKey, String clientIPAddress,
			String serverIPAddress, Integer svcPort) throws OBException {// 모든 vlan interface를 기준으로 packetdump를 실시한다.
																			// 파일의 이름은
		OBDtoFaultCheckResultElement retVal = new OBDtoFaultCheckResultElement();

		retVal.setObj(obj);
		retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_INFO);
		ArrayList<OBDtoFaultCheckResultContent> resultList = new ArrayList<OBDtoFaultCheckResultContent>();
		retVal.setResultList(resultList);
		retVal.setMsgCLI("");
		retVal.setStartTime(OBDateTime.toTimestamp(OBDateTime.now()));
		retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));

		String msgCLI = "";

		if (clientIPAddress == null || clientIPAddress.isEmpty() || serverIPAddress == null
				|| serverIPAddress.isEmpty()) {
			retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_FAIL);

			OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
			content.setSummary(OBFaultCheckMsg.SVC_DUMP_PCAP_INFO.getProcessFailMsg());
			content.setDetail(OBFaultCheckMsg.INVALID_PARAMETER.getFailMsg());
			resultList.add(content);
			msgCLI = OBFaultCheckMsg.INVALID_PARAMETER.getFailMsg();
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
					String.format("failed to parse data. adcIndex(%d), cli(%s)", this.adcInfo.getIndex(), msgCLI));

			retVal.setMsgCLI(msgCLI);
			retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));
			return retVal;
		}
//		ArrayList<String> fileNameList = new ArrayList<String>();

		String fileName = logKey + ".pcap";
		try {
			// 데이터 추출. 모든 인터페이스를 대상으로 수집.
			String option = makeTcpdumpOptionF5(clientIPAddress, svcPort);// serverIPAddress);
			this.ClassSsh.cmndTcpdumpStart("0.0", fileName, option);
			String retText = this.ClassSsh.getCmndRetString();
			retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_SUCC);

			OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
			content.setSummary(OBFaultCheckMsg.SVC_DUMP_PCAP_INFO.getProcessFailMsg());
			content.setDetail(retText);
			resultList.add(content);
			msgCLI += retText;

			retVal.setMsgCLI(msgCLI);
			retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));
		} catch (OBException e) {// 패킷 덤프 과정에서 exception이 발생한 경우.
			retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_FAIL);

			OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
			content.setSummary(OBFaultCheckMsg.SVC_DUMP_PCAP_INFO.getProcessFailMsg());
			content.setDetail(e.getErrorMessage());
			resultList.add(content);
			msgCLI += e.getErrorMessage();

			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
					String.format("[%d/%d] failed to dump pkt. send starting command. msg:%s", this.adcInfo.getIndex(),
							logKey, e.getErrorMessage()));

			retVal.setMsgCLI(msgCLI);
			retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));
			return retVal;
		}
//		fileNameList.add(fileName);
//		retVal.setDetailInfo(fileName);
		return retVal;
	}

	@Override
	public boolean stopSvcPacketDump() throws OBException {// tcpdump process가 있는 경우에는 강제 종료한다.
		boolean retVal = false;

		try {
			// 패킷 덤프 종료.
			this.ClassSsh.cmndStopTcpdump();
			this.ClassSsh.getCmndRetString();
			retVal = true;
		} catch (OBException e) {
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
					String.format("failed to stop dump packets. adcIndex(%d), cli(%s)", this.adcInfo.getIndex(),
							e.getErrorMessage()));
			return retVal;
		}

		return retVal;
	}

	@Override
	public OBDtoReturnInfo isSvcPacketDumpProgress(String dumpFileName) throws OBException {// 파일의 크기를 추출한다. 2회 연속적으로
																							// 파일의 크기를 추출하여 크기에 변화가 있으면
																							// 진행중인 것으로 판별한다.

		OBDtoReturnInfo retVal = new OBDtoReturnInfo();
		String msgCLI = "";
		ArrayList<String> nameList = new ArrayList<String>();
		nameList.add(dumpFileName);
		String retMsg = "";
		try {
//			this.ClassSsh.cmndCheckFileSize(nameList);
			this.ClassSsh.cmndCheckProcess("tcpdump");// tcpdump 프로세스가 살아 있은지 검사한다.
			retMsg = this.ClassSsh.getCmndRetString();
		} catch (OBException e) {
			retVal.setRetCode(OBDtoReturnInfo.CODE_FALSE);
			retVal.setRetValue(e.getErrorMessage());
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
					String.format("failed to check dump file size. adcIndex(%d), cli(%s)", this.adcInfo.getIndex(),
							e.getErrorMessage()));
			return retVal;
		}
		if (retMsg == null || retMsg.isEmpty())
			retVal.setRetCode(OBDtoReturnInfo.CODE_FALSE);// 프로세스가 없는 경우..
		else
			retVal.setRetCode(OBDtoReturnInfo.CODE_TRUE);// 프로세스가 있는 경우..

		msgCLI += retMsg;
		msgCLI += "\n";

		// parsing.
		HashMap<String, OBDtoFaultFileSizeInfo> fileMap = null;
		try {
			this.ClassSsh.cmndCheckFileSize(nameList);
			retMsg = this.ClassSsh.getCmndRetString();
			fileMap = new OBCLIParserF5().parseFileSizeInfo(retMsg);
		} catch (OBException e) {
			retVal.setRetCode(OBDtoReturnInfo.CODE_FALSE);
			retVal.setRetValue(e.getErrorMessage());
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
					String.format("failed to check dump file size. adcIndex(%d), cli(%s)", this.adcInfo.getIndex(),
							e.getErrorMessage()));
			return retVal;
		}

		if (fileMap == null) {
			retVal.setRetCode(OBDtoReturnInfo.CODE_FALSE);
			retVal.setRetValue(OBException.getExceptionMessage(OBException.ERRCODE_SYSTEM_FILENOTFOUND));
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
					String.format("failed to check dump file size. adcIndex(%d), cli(%s)", this.adcInfo.getIndex(),
							OBException.getExceptionMessage(OBException.ERRCODE_SYSTEM_FILENOTFOUND)));
			return retVal;
		}

//		OBDateTime.Sleep(2000);
//		
//		try
//		{
//			this.ClassSsh.cmndCheckFileSize(nameList);
//			retMsg = this.ClassSsh.getCmndRetString();
//		}
//		catch (OBException e)
//		{
//			retVal.setRetCode(OBDtoReturnInfo.CODE_FALSE);
//			retVal.setRetValue(e.getErrorMessage());
//			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("failed to check dump file size. adcIndex(%d), cli(%s)", this.adcInfo.getIndex(), e.getErrorMessage()));
//			return retVal;
//		}
//		
//		msgCLI += retMsg;
//		msgCLI += "\n";
//		
//		// parsing.
//		HashMap<String, OBDtoFaultFileSizeInfo> fileMap2nd = null;
//		try
//		{
//			fileMap2nd = new OBCLIParserF5().parseFileSizeInfo(retMsg);
//		}
//		catch (OBException e)
//		{
//			retVal.setRetCode(OBDtoReturnInfo.CODE_FALSE);
//			retVal.setRetValue(e.getErrorMessage());
//			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("failed to check dump file size. adcIndex(%d), cli(%s)", this.adcInfo.getIndex(), e.getErrorMessage()));
//			return retVal;
//		}
//		
//		if(fileMap2nd==null)
//		{
//			retVal.setRetCode(OBDtoReturnInfo.CODE_FALSE);
//			retVal.setRetValue(OBException.ERRCODE_SYSTEM_FILENOTFOUND[1]);
//			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("failed to check dump file size. adcIndex(%d), cli(%s)", this.adcInfo.getIndex(), OBException.ERRCODE_SYSTEM_FILENOTFOUND[1]));
//			return retVal;
//		}

//		retVal.setRetCode(OBDtoReturnInfo.CODE_FALSE);
		retVal.setRetValue(msgCLI);
//		retVal.setDetailInfo("0");
		OBDtoFaultFileSizeInfo fileInfo = fileMap.get(dumpFileName);
		retVal.setDetailInfo(fileInfo.getFileSize().toString());
//		
//		ArrayList<OBDtoFaultFileSizeInfo> fileSizeList = new ArrayList<OBDtoFaultFileSizeInfo>(fileMap.values());
//		retVal.setRetValue("0");
//		for(OBDtoFaultFileSizeInfo info:fileSizeList)
//		{
//			OBDtoFaultFileSizeInfo fileInfo = fileMap.get(dumpFileName);
//			if(fileInfo!=null)
//			{
//				if(!fileInfo.getFileSize().equals(prevSize))
//				{
//					retVal.setRetCode(OBDtoReturnInfo.CODE_TRUE);
//					retVal.setDetailInfo(fileInfo.getFileSize().toString());
//				}
//			}
//		}
		return retVal;
	}

	@Override
	public boolean isSvcPacketDumpSizeExceed(String dumpFileName, Long baseSize) throws OBException {// 파일의 크기를 추출한다. 2회
																										// 연속적으로 파일의 크기를
																										// 추출하여 크기에 변화가
																										// 있으면 진행중인 것으로
																										// 판별한다.

		boolean retVal = false;
//		String msgCLI = "";
		ArrayList<String> nameList = new ArrayList<String>();
		nameList.add(dumpFileName);

		String retMsg = "";
		try {
			this.ClassSsh.cmndCheckFileSize(nameList);
			retMsg = this.ClassSsh.getCmndRetString();
		} catch (OBException e) {
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
					String.format("failed to check dump file size. adcIndex(%d), cli(%s)", this.adcInfo.getIndex(),
							e.getErrorMessage()));
			return false;
		}

		// parsing.
		HashMap<String, OBDtoFaultFileSizeInfo> fileMap = null;
		try {
			fileMap = new OBCLIParserF5().parseFileSizeInfo(retMsg);
		} catch (OBException e) {
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
					String.format("failed to check dump file size. adcIndex(%d), cli(%s)", this.adcInfo.getIndex(),
							e.getErrorMessage()));
			return retVal;
		}

		if (fileMap == null) {
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
					String.format("failed to check dump file size. adcIndex(%d), cli(%s)", this.adcInfo.getIndex(),
							OBException.getExceptionMessage(OBException.ERRCODE_SYSTEM_FILENOTFOUND)));
			return false;
		}
		OBDtoFaultFileSizeInfo fileInfo = fileMap.get(dumpFileName);
		if (fileInfo != null) {
			if (fileInfo.getFileSize() >= baseSize) {
				retVal = true;
			}
		}
		return retVal;
	}

	@Override
	public OBDtoReturnInfo downloadDumpFile(String remoteFileName, String localFileName, String srvIPAddress)
			throws OBException {
		// srvIPAddress 파라메터는 사용하지 않음.
		OBDtoReturnInfo retVal = new OBDtoReturnInfo();

		if (remoteFileName == null || remoteFileName.isEmpty() || localFileName == null || localFileName.isEmpty()) {
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
					String.format("invalid parameter. adcIndex:%d", this.adcInfo.getIndex()));
			retVal.setRetCode(OBDtoReturnInfo.CODE_FALSE);
			retVal.setRetValue("invalid parameter");
			return retVal;
		}

		localFileName = OBDefine.PKT_DUMP_FILE_PATH + localFileName;
		try {
			// 파일 다운로드 명령 전송.
			boolean isOk = this.ClassSsh.cmndScpDumpfile(remoteFileName, localFileName);
			if (isOk == false) {// 명령 수행에 실패한 경우..
				retVal.setRetCode(OBDtoReturnInfo.CODE_FALSE);
				OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String
						.format("failed to download dump file. adcIndex(%d), cli(%s)", this.adcInfo.getIndex(), ""));
				return retVal;
			}

			retVal.setRetCode(OBDtoReturnInfo.CODE_TRUE);

			// 수집된 파일을 삭제한다.
			this.ClassSsh.cmndRemoveTcpdumpFile(remoteFileName);
		} catch (OBException e) {//
			String retText = e.getErrorMessage();
			retVal.setRetCode(OBDtoReturnInfo.CODE_FALSE);
			retVal.setRetValue(retText);
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String
					.format("failed to download dump file. adcIndex(%d), cli(%s)", this.adcInfo.getIndex(), retText));
			return retVal;
		}
		return retVal;
	}

	private OBDtoRespTimeTempInfo readRespTimeInfoFromFile(String fileName) throws OBException {
		OBDtoRespTimeTempInfo retVal = new OBDtoRespTimeTempInfo();

		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(fileName));
			String line;
			while ((line = br.readLine()) != null) {
				String element[] = line.split(" ");// space를 기준으로 분리.//-rw------- 1 root root 1074820 Oct 14 13:48
													// aaa.pcap
				if (element.length != 10) {
					continue;
				}
				retVal.setiCVAvgTime(Integer.parseInt(element[0]));
				retVal.setiCVMaxTime(Integer.parseInt(element[1]));
				retVal.setiCVMinTime(Integer.parseInt(element[2]));

				retVal.setiVRAvgTime(Integer.parseInt(element[3]));
				retVal.setiVRMaxTime(Integer.parseInt(element[4]));
				retVal.setiVRMinTime(Integer.parseInt(element[5]));

				retVal.setiCVReqCount(Integer.parseInt(element[6]));
				retVal.setiCVRespCount(Integer.parseInt(element[7]));
				retVal.setiVRReqCount(Integer.parseInt(element[8]));
				retVal.setiVRRespCount(Integer.parseInt(element[9]));
				break;
			}

			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {

		}

		return retVal;
	}

//	public static void main(String[] args)
//	{
//		try
//		{
//			OBDtoFaultCheckThreadInfo threadInfo = new OBDtoFaultCheckThreadInfo();
//			threadInfo.setAccntIndex(0);
//			threadInfo.setClientIP("172.172.2.206");
//			threadInfo.setCheckKey(3582054056353L);
//			OBDtoElement obj = new OBDtoElement();
//			obj.setIndex(1);
//			obj.setName("test");
//			String fileName = "/var/lib/adcsmart/pcap/3582061533509.pcap";
//			ArrayList<String> realIPAddress = new ArrayList<String>();
//			realIPAddress.add("0.0.0.0");
//			OBDtoFaultCheckResultElement worker = new OBFaultCheckHandlerF5().checkSvcResponseTime(obj, fileName, "172.172.2.209", "192.168.200.232", realIPAddress);
//			System.out.println(worker);
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}

	@Override
	public OBDtoFaultCheckResultElement checkSvcResponseTime(OBDtoElement obj, String pszFileName,
			String clientIPAddress, String vsIPAddress, ArrayList<String> realIPAddress) throws OBException {
		OBDtoFaultCheckResultElement retVal = new OBDtoFaultCheckResultElement();
		retVal.setObj(obj);
		retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_NA);
		ArrayList<OBDtoFaultCheckResultContent> resultList = new ArrayList<OBDtoFaultCheckResultContent>();
		retVal.setResultList(resultList);
		retVal.setMsgCLI("");
		retVal.setStartTime(OBDateTime.toTimestamp(OBDateTime.now()));
		retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));

		if (pszFileName == null || pszFileName.isEmpty() || clientIPAddress == null || clientIPAddress.isEmpty()
				|| vsIPAddress == null || vsIPAddress.isEmpty()) {
			retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_FAIL);

			OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
			content.setSummary(OBFaultCheckMsg.SVC_RESPONSE_TIME_STATUS.getProcessFailMsg());
			content.setDetail(OBFaultCheckMsg.INVALID_PARAMETER.getFailMsg());
			resultList.add(content);

			retVal.setMsgCLI(OBFaultCheckMsg.INVALID_PARAMETER.getFailMsg());
			retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));
			retVal.setExtraInfo(null);
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
					String.format("invalid parameter. adcIndex:%d", this.adcInfo.getIndex()));
			return retVal;
		}

		try {
			String szLocalTempFileName = pszFileName + ".tmp";

			String szRealIPAddress = makeHostInfoList(realIPAddress);// new STPktHostInfo();

			Integer ret = LibOBPktAnalysisLibrary.INSTANCE.GetResponseTimeInfo(pszFileName, szLocalTempFileName,
					clientIPAddress, vsIPAddress, szRealIPAddress);
			if (ret != LibOBPktAnalysisLibrary.RET_CODE_OK) {
				retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_FAIL);

				OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
				content.setSummary(OBFaultCheckMsg.SVC_RESPONSE_TIME_STATUS.getProcessFailMsg());
				content.setDetail(OBFaultCheckMsg.PARSE_PACKET.getFailMsg());
				resultList.add(content);

				retVal.setMsgCLI(OBFaultCheckMsg.PARSE_PACKET.getSuccessMsg());
				retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));
//				retVal.setExtraInfo(pstRetInfo);
				OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
						String.format("failed to get response time info. adcIndex:%d", this.adcInfo.getIndex()));
				return retVal;
			}
			OBDtoRespTimeTempInfo respResultInfo = readRespTimeInfoFromFile(szLocalTempFileName);
			deleteTempWorkFile(szLocalTempFileName);
			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("read response time info: %s", respResultInfo));

			OBDtoRespTimeAnalInfo respTimeInfo = new OBDtoRespTimeAnalInfo();
			respTimeInfo.setEndPointAvgTime(respResultInfo.getiCVAvgTime());
			respTimeInfo.setEndPointMaxTime(respResultInfo.getiCVMaxTime());
			respTimeInfo.setEndPointMinTime(respResultInfo.getiCVMinTime());
			respTimeInfo.setDataCenterAvgTime(respResultInfo.getiVRAvgTime());
			respTimeInfo.setDataCenterMaxTime(respResultInfo.getiVRMaxTime());
			respTimeInfo.setDataCenterMinTime(respResultInfo.getiVRMinTime());

			OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
			if (respResultInfo != null && respResultInfo.getiCVReqCount() > 0) {
				retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_SUCC);
				content.setSummary(String.format(OBFaultCheckMsg.SVC_RESPONSE_TIME_STATUS.getSuccessMsg(),
						respTimeInfo.getEndPointAvgTime()));
				String detail = String.format(OBFaultCheckMsg.SVC_RESPONSE_TIME_STATUS.getSuccessMsg(),
						respTimeInfo.getEndPointAvgTime(), respTimeInfo.getEndPointMinTime(),
						respTimeInfo.getEndPointMaxTime());
				content.setDetail(detail);
				retVal.setMsgCLI(OBFaultCheckMsg.PARSE_PACKET.getSuccessMsg());
			} else {
				retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_INFO);
				content.setSummary(OBFaultCheckMsg.PACKET_NO_DATA.getFailMsg());
				String detail = OBFaultCheckMsg.PACKET_NO_DATA.getFailMsg();
				content.setDetail(detail);
				retVal.setMsgCLI(OBFaultCheckMsg.PACKET_NO_DATA.getFailMsg());
			}

			resultList.add(content);

			retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));
			retVal.setExtraInfo(respTimeInfo);
			return retVal;
		} catch (Exception e) {
			retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_FAIL);

			OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
			content.setSummary(OBFaultCheckMsg.SVC_RESPONSE_TIME_STATUS.getProcessFailMsg());
			content.setDetail(OBFaultCheckMsg.PARSE_PACKET.getFailMsg());
			resultList.add(content);

			retVal.setMsgCLI(OBFaultCheckMsg.PARSE_PACKET.getFailMsg());
			retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));
			retVal.setExtraInfo(null);
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
					String.format("invalid parameter. adcIndex:%d", this.adcInfo.getIndex()));
			return retVal;
		}
	}

	@Override
	public OBDtoFaultCheckResultElement checkSvcLoadBalancing(OBDtoElement obj) throws OBException {
		// TODO Auto-generated method stub
		return null;
	}

	private String makeHostInfoList(ArrayList<String> ipList) {
		String retVal = "";
		if (ipList == null)
			return retVal;
		;

		for (String ip : ipList) {
			if (!retVal.isEmpty())
				retVal += " ";

			retVal += ip;
		}
		return retVal;
	}

//	public static void main(String[] args)
//	{
//		try
//		{
//			OBDatabase db = new OBDatabase();
//			db.openDB();
//			ArrayList<OBDtoFaultCheckPacketLossInfo> pktLossList = new OBFaultCheckHandlerF5().readPacketLossInfoFromFile("/var/lib/adcsmart/pcap/test.tmp");
//			System.out.println(pktLossList);
//			
//			new OBFaultMonitoringDB().writeFaultLogSummaryPktLoss(3583866431817L, OBDateTime.toTimestamp(OBDateTime.now()), 3, "3_MONITOR2", pktLossList, db);
//			
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}

	private ArrayList<OBDtoFaultCheckPacketLossInfo> readPacketLossInfoFromFile(String fileName) throws OBException {
		ArrayList<OBDtoFaultCheckPacketLossInfo> retVal = new ArrayList<OBDtoFaultCheckPacketLossInfo>();

		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(fileName));
			String line;
			while ((line = br.readLine()) != null) {
				String element[] = line.split(" ");// space를 기준으로 분리.//-rw------- 1 root root 1074820 Oct 14 13:48
													// aaa.pcap
				if (element.length < 11) {
					continue;
				}
				OBDtoFaultCheckPacketLossInfo obj = new OBDtoFaultCheckPacketLossInfo();
				// Long timeMsec = Long.parseLong(element[0]); //mili sec = msec //처음에는 이 값을
				// 썼으나, 정밀도 향상 요구가 있어서 usec로 대체
				// obj.setRcvTime(new Timestamp(timeMsec));
				Long timeUsec = Long.parseLong(element[1]); // micro sec = usec
				obj.setRcvTime(new Timestamp(timeUsec));

				obj.setTcpFlag(Integer.parseInt(element[2]));
				obj.setDirection(Integer.parseInt(element[3]));
				obj.setSrcIPAddress(element[4]);
				obj.setDstIPAddress(element[5]);
//				obj.setTimeDiff(Integer.parseInt(element[6]));
				obj.setSeqNo(Long.parseLong(element[6]));
				obj.setAckNo(Long.parseLong(element[7]));
				obj.setDataLength(Integer.parseInt(element[8]));
				obj.setSrcPort(Integer.parseInt(element[9]));
				obj.setDstPort(Integer.parseInt(element[10]));
				if (element.length >= 12)
					obj.setSummary(element[11]);
				retVal.add(obj);
			}

			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return retVal;
	}

	private void deleteTempWorkFile(String fileName) throws OBException {
		OBUtility.fileDelete(fileName);
	}

	private String addExtraSpace(Integer num) {
		String retVal = "";
		for (int i = 0; i <= num; i++) {
			retVal += "|||;";
		}
		return retVal;
	}

//	private Integer isValidPacket(OBDtoFaultCheckPacketLossInfo prevInfo, OBDtoFaultCheckPacketLossInfo currInfo)
//	{// 패킷이 정상적인 검사한다. 0: not defined, 1: valid, 2: invalid
//		Integer retVal = 0;
//		
//		if(prevInfo==null)
//			return retVal;
//		
//		if(currInfo.getDirection()==OBDtoFaultCheckPacketLossInfo.DIR_CLNT_VIP)
//			return 1;
//		
//		// 이전값과 방향이 같으면 0 리턴
//		if(currInfo.getDirection()==prevInfo.getDirection())
//			return 0;
//		
//		switch(currInfo.getDirection())
//		{
//		case OBDtoFaultCheckPacketLossInfo.DIR_CLNT_VIP:// 요청사항. 요청은 초기 상태로 간주함.
//			return 0;
//		case OBDtoFaultCheckPacketLossInfo.DIR_CLNT_REAL:
//			return 2;// client에서 real로 들어가는 상황. 비 정상으로 간
//		case OBDtoFaultCheckPacketLossInfo.DIR_VIP_CLNT:
//			if(prevInfo.getDirection()==currInfo.getDirection())
//				return 0;
//			if(prevInfo.getDirection()==OBDtoFaultCheckPacketLossInfo.DIR_REAL_VIP)
//				return 1;
//			return 2;
//		case OBDtoFaultCheckPacketLossInfo.DIR_REAL_CLNT:
//			return 2;// real 에서 client로 들어가는 상황. 비 정상으로 간주.
//		case OBDtoFaultCheckPacketLossInfo.DIR_VIP_REAL:
//			if(prevInfo.getDirection()==currInfo.getDirection())
//				return 0;
//			if(prevInfo.getDirection()==OBDtoFaultCheckPacketLossInfo.DIR_CLNT_VIP)
//				return 1;
//			return 2;
//		case OBDtoFaultCheckPacketLossInfo.DIR_REAL_VIP:
//			if(prevInfo.getDirection()==currInfo.getDirection())
//				return 0;
//			if(prevInfo.getDirection()==OBDtoFaultCheckPacketLossInfo.DIR_VIP_REAL)
//				return 1;
//			return 2;
//		default:
//			return 0;
//		}
//	}

	final static private Integer MAX_DIV_COUNT = 3;

	private Integer calcArcskipNum(Integer timeDiff, OBDtoMinMaxAvgInfo minMaxAvgInfo) {
		if (timeDiff == 0)
			return 0;
		Integer retVal = 0;
		Integer base = (minMaxAvgInfo.getMax() - minMaxAvgInfo.getMin()) / MAX_DIV_COUNT;

		retVal = (timeDiff - minMaxAvgInfo.getMin()) / (base + 1);// Base가 0일 경우 예외 처리
		if (retVal >= 3)
			retVal = MAX_DIV_COUNT - 1;
		return retVal;
	}

	private OBDtoMinMaxAvgInfo getMinMaxAvgDiffTime(ArrayList<OBDtoFaultCheckPacketLossInfo> pktInfoList) {
		OBDtoMinMaxAvgInfo retVal = new OBDtoMinMaxAvgInfo();
		long total = 0;
		for (OBDtoFaultCheckPacketLossInfo info : pktInfoList) {
			if (retVal.getMax() < info.getTimeDiff()) {
				retVal.setMax(info.getTimeDiff());
			}
			if (retVal.getMin() == 0 && info.getTimeDiff() > 0) {
				retVal.setMin(info.getTimeDiff());
			} else if (info.getTimeDiff() > 0) {
				if (retVal.getMin() > info.getTimeDiff())
					retVal.setMin(info.getTimeDiff());
			}
			total += info.getTimeDiff();
		}
		retVal.setAvg((int) (total / pktInfoList.size()));
		return retVal;
	}

	private String makePacketLossInfoImgFileF5(Long logKey, String clientIP, String vsvcIPAddress,
			ArrayList<OBDtoFaultCheckPacketLossInfo> pktInfoList) throws OBException {// 문법은
																						// http://www.mcternan.co.uk/mscgen/
																						// 참조하기 바람.
		String pngFileName = logKey + ".png";
//		String pngFullPathName = "/opt/apache-tomcat/webapps/adcms/imgs/"+logKey+".png";
		String pngFullPathName = OBDefine.PKT_DUMP_FILE_PATH + logKey + ".png";
		String infFileName = OBDefine.PKT_DUMP_FILE_PATH + logKey + ".inf";
		String retVal = pngFileName;

		try {
			String data = "";
			data += "# MSC for some fictional process\n";
			data += "msc {\n";
			data += "arcgradient = 4;\n";
//			data += "width = 800;";
			data += "\n";
			data += String.format("a [label=\"%s\"],b [label=\"%s\"], c [label=\"%s\"];\n", clientIP, vsvcIPAddress,
					"REAL HOST");
			data += "\n";

			String szDir;
//			OBDtoFaultCheckPacketLossInfo prevInfo = null;
			String redColor = "#ff0000";
			String blackColor = "#000000";

			OBDtoMinMaxAvgInfo minMaxAvgInfo = getMinMaxAvgDiffTime(pktInfoList);

//			float elapseTime = 0;
			String color = "#ff0000";// black:#000000, green:#00ff00, red:#ff0000
			Integer arcskipCount = 0;
//			OBDtoFaultCheckPacketLossInfo prevInfo = null;
			for (OBDtoFaultCheckPacketLossInfo pktInfo : pktInfoList) {
				Integer tmpArcskipNum = calcArcskipNum(pktInfo.getTimeDiff(), minMaxAvgInfo);
				data += addExtraSpace(arcskipCount);
				color = blackColor;
//				if(isValidPacket(prevInfo, pktInfo)==2)
//					color = redColor;
				if (pktInfo.isValid() == false)
					color = redColor;
				switch (pktInfo.getDirection()) {
				case OBDtoFaultCheckPacketLossInfo.DIR_CLNT_VIP:// reqest
					if (pktInfo.isValid() == false)
						szDir = "a -x b";
					else
						szDir = "a => b";
					tmpArcskipNum = 0;
					break;
				case OBDtoFaultCheckPacketLossInfo.DIR_CLNT_REAL:
					if (pktInfo.isValid() == false)
						szDir = "a -x c";
					else
						szDir = "a => c";
					break;
				case OBDtoFaultCheckPacketLossInfo.DIR_VIP_CLNT:
					if (pktInfo.isValid() == false)
						szDir = "a x- b";
					else
						szDir = "a <= b";
					break;
				case OBDtoFaultCheckPacketLossInfo.DIR_REAL_CLNT:
					if (pktInfo.isValid() == false)
						szDir = "a x- c";
					else
						szDir = "a <= c";
					break;
				case OBDtoFaultCheckPacketLossInfo.DIR_VIP_REAL:
					if (pktInfo.isValid() == false)
						szDir = "b -x c";
					else
						szDir = "b => c";
					break;
				case OBDtoFaultCheckPacketLossInfo.DIR_REAL_VIP:
					if (pktInfo.isValid() == false)
						szDir = "b x- c";
					else
						szDir = "b <= c";
					break;
				default:
					continue;
				}

//				elapseTime = pktInfo.getTimeDiff();
				data += String.format(
						"%s [label=\"%s(Length:%d)\\nTime:%4.3f msec\", linecolour=\"%s\", arcskip=\"%d\"];\n", szDir,
						pktInfo.getSummary(), pktInfo.getDataLength(), pktInfo.getTimeDiff() / 1000f, color,
						tmpArcskipNum);
				arcskipCount = tmpArcskipNum;
//				prevInfo = pktInfo;
			}

			data += "\n";
			data += "}\n";

			// 파일에 저장함.
			BufferedWriter out = new BufferedWriter(new FileWriter(infFileName));
			out.write(data);
			out.close();

			// png 파일 생성.
			String cmnd = String.format("/usr/bin/mscgen -T png -o %s -i %s", pngFullPathName, infFileName);
			OBSystemLog.info(OBDefine.LOGFILE_SYSTEM, String.format("make png file. cmnd:%s", cmnd));
			Runtime.getRuntime().exec(cmnd).waitFor();

//			OBDateTime.Sleep(500);
			// file 생성 검사..
			Long fileSize = OBUtility.fileLength(pngFullPathName);
			if (fileSize == 0L)
				retVal = "";
//			String targetFile = "/opt/apache-tomcat/webapps/adcms/imgs/"+pngFileName;
//			cmnd = String.format("/bin/cp %s %s", pngFullPathName, targetFile);
//			Runtime.getRuntime().exec(cmnd);
		} catch (RuntimeException e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, "RuntimeException: " + e.getMessage());//
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
		}
		return retVal;
	}

//	public static void main(String[] args)
//	{
//		try
//		{
//			OBDtoElement object = new OBDtoElement();
//			object.setCategory(4);
//			object.setIndex(10);//[category=4, index=4, name=패킷유실 분석, description=패킷의 유실 여부를 분석합니다., target=15, state=0], 
//			//logKey:1387847074360, 
//			//fileName:/var/lib/adcsmart/pcap/1387847074360.pcap, 
//			//clientIPAddress:172.172.2.209, vsIPAddress:192.168.100.233, realIPAddress:[192.168.199.41, 192.168.199.42, 192.168.199.43]
//			//		
//			ArrayList<String>realIPAddress = new ArrayList<String>();
//			realIPAddress.add("192.168.199.47");
//			realIPAddress.add("192.168.199.48");
//			OBDtoFaultCheckThreadInfo threadInfo = new OBDtoFaultCheckThreadInfo();
//			threadInfo.setAccntIndex(0);
//			threadInfo.setClientIP("172.172.2.206");
//			threadInfo.setCheckKey(1387542674757L);
//			OBDtoFaultCheckResultElement worker = new OBFaultCheckHandlerF5(1, "").checkSvcPacketLoss(
//						null, 11283636645270L, "/var/lib/adcsmart/pcap/11283636645270.pcap", "172.172.2.209", "192.168.200.233", realIPAddress);
////						null, 1387843678081L, "/var/lib/adcsmart/pcap/pkt.pcap", "172.172.2.209", "192.168.100.233", realIPAddress);
//			System.out.println(worker);
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}
	@Override
	public OBDtoFaultCheckResultElement checkSvcPacketLoss(OBDtoElement obj, Long logKey, String fileName,
			String clientIPAddress, String vsIPAddress, ArrayList<String> realIPAddress) throws OBException {
		OBDtoFaultCheckResultElement retVal = new OBDtoFaultCheckResultElement();
		retVal.setObj(obj);
		retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_NA);
		ArrayList<OBDtoFaultCheckResultContent> resultList = new ArrayList<OBDtoFaultCheckResultContent>();
		retVal.setResultList(resultList);
		retVal.setMsgCLI("");
		retVal.setStartTime(OBDateTime.toTimestamp(OBDateTime.now()));
		retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));

		OBSystemLog.info(OBDefine.LOGFILE_SYSTEM,
				String.format("obj:%s, logKey:%d, fileName:%s, clientIPAddress:%s, vsIPAddress:%s, realIPAddress:%s",
						obj, logKey, fileName, clientIPAddress, vsIPAddress, realIPAddress));

		if (fileName == null || fileName.isEmpty() || clientIPAddress == null || clientIPAddress.isEmpty()
				|| vsIPAddress == null || vsIPAddress.isEmpty()) {
			retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_FAIL);

			OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
			content.setSummary(OBFaultCheckMsg.SVC_PACKET_LOSS_CHECK.getProcessFailMsg());
			content.setDetail(OBFaultCheckMsg.INVALID_PARAMETER.getFailMsg());
			resultList.add(content);

			retVal.setMsgCLI(OBFaultCheckMsg.INVALID_PARAMETER.getFailMsg());
			retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));
			retVal.setExtraInfo(null);
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
					String.format("invalid parameter. adcIndex:%d", this.adcInfo.getIndex()));
			return retVal;
		}

		String msgCLI = "";
		try {
			String szLocalTempFileName = fileName + ".tmp";

			String szRealIPAddress = makeHostInfoList(realIPAddress);// new STPktHostInfo();

			Integer ret = LibOBPktAnalysisLibrary.INSTANCE.GetPacketLossInfoV2(fileName, szLocalTempFileName,
					clientIPAddress, vsIPAddress, szRealIPAddress);
			if (ret != LibOBPktAnalysisLibrary.RET_CODE_OK) {
				retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_FAIL);

				OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
				content.setSummary(OBFaultCheckMsg.SVC_PACKET_LOSS_CHECK.getProcessFailMsg());
				content.setDetail(OBFaultCheckMsg.DATA_NOT_EXIST.getFailMsg());
				resultList.add(content);

				retVal.setMsgCLI(OBFaultCheckMsg.PARSE_PACKET.getSuccessMsg());
				retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));
				OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
						String.format("invalid parameter. adcIndex:%d", this.adcInfo.getIndex()));
				return retVal;
			}

			// 데이터를 읽어 들인다.
			ArrayList<OBDtoFaultCheckPacketLossInfo> readInfoList = readPacketLossInfoFromFile(szLocalTempFileName);
			deleteTempWorkFile(szLocalTempFileName);
			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("read packet loss info: %s", readInfoList));

			OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
			if (readInfoList.size() > 0) {
				OBDtoPktLossInfo pktLossInfo = new OBAnalPktLossF5().analyzePacketLoss(clientIPAddress, vsIPAddress,
						realIPAddress, readInfoList);
				OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("analyze packet loss info: %s", pktLossInfo));
				if (pktLossInfo.getPktInfoList() == null || pktLossInfo.getPktInfoList().size() <= 0) {
					retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_INFO);
					content.setSummary(OBFaultCheckMsg.PACKET_NO_DATA.getFailMsg());
					String detail = OBFaultCheckMsg.PACKET_NO_DATA.getFailMsg();
					msgCLI += detail;
					content.setDetail(detail);
					retVal.setMsgCLI(OBFaultCheckMsg.PACKET_NO_DATA.getFailMsg());

					resultList.add(content);
				} else {
					String pngFileName = makePacketLossInfoImgFileF5(logKey, clientIPAddress, vsIPAddress,
							pktLossInfo.getPktInfoList());
					if (pngFileName == null || pngFileName.isEmpty()) {
						retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_FAIL);
						content.setSummary(OBFaultCheckMsg.SVC_PACKET_LOSS_CHECK.getProcessFailMsg());
						String detail = OBFaultCheckMsg.PARSE_DATA.getFailMsg();
						msgCLI += detail;
						content.setDetail(detail);
						retVal.setMsgCLI(OBFaultCheckMsg.PARSE_DATA.getFailMsg());

						resultList.add(content);
					} else {
						if (pktLossInfo.getLossPktCount() == 0) {
							retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_SUCC);
							content.setSummary(OBFaultCheckMsg.SVC_PACKET_LOSS_CHECK.getSuccessMsg());
							String detail = String.format(OBFaultCheckMsg.SVC_PACKET_LOSS_INFO.getSuccessMsg(),
									pktLossInfo.getTotalPktCount(), pktLossInfo.getValidPktCount(),
									pktLossInfo.getLossPktCount());
							content.setDetail(detail);
						} else {
							retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_FAIL);
							content.setSummary(OBFaultCheckMsg.SVC_PACKET_LOSS_CHECK.getFailMsg());
							String detail = String.format(OBFaultCheckMsg.SVC_PACKET_LOSS_INFO.getSuccessMsg(),
									pktLossInfo.getTotalPktCount(), pktLossInfo.getValidPktCount(),
									pktLossInfo.getLossPktCount());
							content.setDetail(detail);
						}
						retVal.setMsgCLI(OBFaultCheckMsg.PARSE_PACKET.getSuccessMsg());

						resultList.add(content);
					}
				}
			} else {
				retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_INFO);
				content.setSummary(OBFaultCheckMsg.PACKET_NO_DATA.getFailMsg());
				String detail = OBFaultCheckMsg.PACKET_NO_DATA.getFailMsg();
				msgCLI += detail;
				content.setDetail(detail);
				retVal.setMsgCLI(OBFaultCheckMsg.PACKET_NO_DATA.getFailMsg());

				resultList.add(content);
			}

//			retVal.setExtraInfo(readInfoList);

			retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));
		} catch (Exception e) {
			retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_FAIL);

			OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
			content.setSummary(OBFaultCheckMsg.SVC_PACKET_LOSS_CHECK.getProcessFailMsg());
			content.setDetail(OBFaultCheckMsg.PARSE_PACKET.getFailMsg());
			resultList.add(content);

			retVal.setMsgCLI(OBFaultCheckMsg.PARSE_PACKET.getFailMsg());
			retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));
			retVal.setExtraInfo(null);
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
					String.format("invalid parameter. adcIndex:%d", this.adcInfo.getIndex()));
			return retVal;
		}

		retVal.setMsgCLI(msgCLI);
		retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));
		return retVal;
	}

	@Override
	public OBDtoFaultCheckResultElement checkHWOSInfo(OBDtoElement obj, OBDtoAdcInfo adcInfo) throws OBException {
		OBDtoFaultCheckResultElement retVal = new OBDtoFaultCheckResultElement();
		retVal.setObj(obj);
		retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_NA);
		ArrayList<OBDtoFaultCheckResultContent> resultList = new ArrayList<OBDtoFaultCheckResultContent>();
		retVal.setResultList(resultList);
		retVal.setMsgCLI("");
		retVal.setStartTime(OBDateTime.toTimestamp(OBDateTime.now()));
		retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));

		String msgCLI = "";

		OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
		retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_INFO);
		content.setSummary(String.format(OBFaultCheckMsg.HW_OS_CHECK.getSuccessMsg(), adcInfo.getSwVersion()));
		// adc 로그 목록이 출력된다.
		if (adcInfo.getSwVersion().isEmpty()) {
			retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_FAIL);
			content.setSummary(String.format(OBFaultCheckMsg.HW_OS_CHECK.getProcessFailMsg()));
		}
		content.setDetail(
				String.format(OBFaultCheckMsg.HW_OS_INFO.getSuccessMsg(), adcInfo.getModel(), adcInfo.getSwVersion()));
		resultList.add(content);

		retVal.setMsgCLI(msgCLI);
		retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));
		return retVal;
	}

	// 패킷이 수집되어 사용 가능한지 검사한다.
	@Override
	public OBDtoFaultCheckResultElement isSvcPacketAvaliable(OBDtoElement obj, Long logKey) throws OBException {
		OBDtoFaultCheckResultElement retVal = new OBDtoFaultCheckResultElement();
		retVal.setObj(obj);
		retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_INFO);
		ArrayList<OBDtoFaultCheckResultContent> resultList = new ArrayList<OBDtoFaultCheckResultContent>();
		retVal.setResultList(resultList);
		retVal.setMsgCLI("");
		retVal.setStartTime(OBDateTime.toTimestamp(OBDateTime.now()));
		retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));

		String msgCLI = "";
		String retMsg = "";

		String dumpFileName = logKey + ".pcap";
		ArrayList<String> nameList = new ArrayList<String>();
		nameList.add(dumpFileName);

		HashMap<String, OBDtoFaultFileSizeInfo> fileMap = null;
		try {
			this.ClassSsh.cmndCheckFileSize(nameList);
			retMsg = this.ClassSsh.getCmndRetString();
			fileMap = new OBCLIParserF5().parseFileSizeInfo(retMsg);
		} catch (OBException e) {
			retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_FAIL);

			OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
			content.setSummary(OBFaultCheckMsg.SVC_DUMP_PCAP_CHECK.getProcessFailMsg());
			msgCLI = e.getErrorMessage();
			content.setDetail(msgCLI);
			resultList.add(content);

			retVal.setMsgCLI(msgCLI);
			retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("[%d/%d] failed to check pkt dump status. msg:%s",
					this.adcInfo.getIndex(), logKey, e.getErrorMessage()));
			return retVal;
		}

		if (fileMap == null) {
			retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_INFO);

			OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
			content.setSummary(OBFaultCheckMsg.SVC_DUMP_PCAP_CHECK.getProcessFailMsg());
			msgCLI = OBFaultCheckMsg.PACKET_NO_DATA.getFailMsg();//
			content.setDetail(msgCLI);
			resultList.add(content);

			retVal.setMsgCLI(msgCLI);
			retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format(
					"[%d/%d] failed to check pkt dump status. msg:data not found", this.adcInfo.getIndex(), logKey));
			return retVal;
		}

		OBDtoFaultFileSizeInfo fileInfo = fileMap.get(dumpFileName);
		if (fileInfo == null) {
			retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_INFO);

			OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
			content.setSummary(OBFaultCheckMsg.SVC_DUMP_PCAP_CHECK.getProcessFailMsg());
			msgCLI = OBFaultCheckMsg.PACKET_NO_DATA.getFailMsg();//
			content.setDetail(msgCLI);
			resultList.add(content);

			retVal.setMsgCLI(msgCLI);
			retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format(
					"[%d/%d] failed to check pkt dump status. msg:data not found", this.adcInfo.getIndex(), logKey));
			return retVal;
		}
		if (fileInfo.getFileSize() <= 24) {
			retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_INFO);

			OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
			content.setSummary(OBFaultCheckMsg.SVC_DUMP_PCAP_CHECK.getProcessFailMsg());
			msgCLI = OBFaultCheckMsg.PACKET_NO_DATA.getFailMsg();//
			content.setDetail(msgCLI);
			resultList.add(content);

			retVal.setMsgCLI(msgCLI);
			retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format(
					"[%d/%d] failed to check pkt dump status. msg:data not found", this.adcInfo.getIndex(), logKey));
			return retVal;
		}

		retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_SUCC);

		OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
		content.setSummary(OBFaultCheckMsg.SVC_DUMP_PCAP_CHECK.getSuccessMsg());
		String detailMsg = msgCLI;
		content.setDetail(detailMsg);
		resultList.add(content);

		retVal.setMsgCLI(detailMsg);
		retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));
		return retVal;
	}

	@Override
	public OBDtoFaultCheckResultElement checkL47SessionTable(OBDtoElement obj) throws OBException {
		OBDtoFaultCheckResultElement retVal = new OBDtoFaultCheckResultElement();
		retVal.setObj(obj);
		retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_NA);
		ArrayList<OBDtoFaultCheckResultContent> resultList = new ArrayList<OBDtoFaultCheckResultContent>();
		retVal.setResultList(resultList);
		retVal.setMsgCLI("");
		retVal.setStartTime(OBDateTime.toTimestamp(OBDateTime.now()));
		retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));

		return retVal;
	}

	@Override
	public OBDtoFaultCheckResultElement checkL47UnsedFLB(OBDtoElement obj) throws OBException {
		// TODO Auto-generated method stub
		return null;
	}
}
