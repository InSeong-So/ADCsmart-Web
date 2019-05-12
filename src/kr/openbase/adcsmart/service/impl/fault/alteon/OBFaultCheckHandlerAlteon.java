package kr.openbase.adcsmart.service.impl.fault.alteon;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.snmp4j.smi.VariableBinding;

import kr.openbase.adcsmart.service.dto.OBDtoADCObject;
import kr.openbase.adcsmart.service.dto.OBDtoAdcInfo;
import kr.openbase.adcsmart.service.dto.OBDtoAdcPortStatus;
import kr.openbase.adcsmart.service.dto.OBDtoAuditLogAdcSystem;
import kr.openbase.adcsmart.service.dto.OBDtoElement;
import kr.openbase.adcsmart.service.dto.OBDtoPktDumpInfo;
import kr.openbase.adcsmart.service.dto.OBDtoReturnInfo;
import kr.openbase.adcsmart.service.dto.OBDtoSearch;
import kr.openbase.adcsmart.service.dto.OBDtoSlbNodeInfo;
import kr.openbase.adcsmart.service.dto.OBDtoSlbPoolInfo;
import kr.openbase.adcsmart.service.dto.OBDtoSlbVServerInfo;
import kr.openbase.adcsmart.service.dto.OBDtoUptimeInfo;
import kr.openbase.adcsmart.service.dto.fault.OBDtoFaultCheckLog;
import kr.openbase.adcsmart.service.dto.fault.OBDtoFaultCheckResultContent;
import kr.openbase.adcsmart.service.dto.fault.OBDtoFaultCheckResultElement;
import kr.openbase.adcsmart.service.impl.OBAdcManagementImpl;
import kr.openbase.adcsmart.service.impl.alteon.handler.OBAdcAlteonHandler;
import kr.openbase.adcsmart.service.impl.alteon.handler.OBCLIParserAlteon;
import kr.openbase.adcsmart.service.impl.dto.OBDtoMonL2Ports;
import kr.openbase.adcsmart.service.impl.dto.OBDtoStatsSlbAuxTable;
import kr.openbase.adcsmart.service.impl.dto.OBDtoStatsSlbMaint;
import kr.openbase.adcsmart.service.impl.fault.OBFaultCheckHandler;
import kr.openbase.adcsmart.service.impl.fault.OBFaultMngImpl;
import kr.openbase.adcsmart.service.impl.fault.OBFaultMonitoringDB;
import kr.openbase.adcsmart.service.impl.fault.alteon.dto.OBDtoSWKeyAlteon;
import kr.openbase.adcsmart.service.impl.fault.dto.OBDtoFaultCheckPacketLossInfo;
import kr.openbase.adcsmart.service.impl.fault.dto.OBDtoMinMaxAvgInfo;
import kr.openbase.adcsmart.service.impl.fault.dto.OBDtoPktLossInfo;
import kr.openbase.adcsmart.service.impl.fault.dto.OBDtoRespTimeAnalInfo;
import kr.openbase.adcsmart.service.impl.fault.dto.OBDtoRespTimeTempInfo;
import kr.openbase.adcsmart.service.jna.LibOBPktAnalysisLibrary;
import kr.openbase.adcsmart.service.snmp.OBSnmp;
import kr.openbase.adcsmart.service.snmp.OBSnmpOidDB;
import kr.openbase.adcsmart.service.snmp.alteon.OBSnmpAlteon;
import kr.openbase.adcsmart.service.snmp.dto.DtoOidFaultCheckHW;
import kr.openbase.adcsmart.service.utility.OBCommon;
import kr.openbase.adcsmart.service.utility.OBDateTime;
import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.service.utility.OBFaultCheckMsg;
import kr.openbase.adcsmart.service.utility.OBMessages;
import kr.openbase.adcsmart.service.utility.OBParser;
import kr.openbase.adcsmart.service.utility.OBSystemLog;
import kr.openbase.adcsmart.service.utility.OBUtility;

public class OBFaultCheckHandlerAlteon implements OBFaultCheckHandler {
	private DtoOidFaultCheckHW snmpOidInfoHW = null;
	private Integer vendorType;
	private String swVersion;
	private OBDtoAdcInfo adcInfo;
	// private OBDtoFaultCheckLog logInfo;
	// private long logKey;
	private OBSnmp ClassSnmp;
	private OBAdcAlteonHandler ClassTelnet;

	private Integer packetDumpCount = 1000;
	private Integer packetDumpLength = 0; // all
//	private Integer packetDumpMaxStart = 10000;
	private Integer packetDumpStart = 1;
	private Integer uptimeMaxDiffDate = 300000;

	public OBFaultCheckHandlerAlteon(Integer vendorType, String swVersion) {
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

			propertyValue = OBParser.propertyToString(OBDefine.PROPERTY_FILE_NAME, OBDefine.PROPERTY_KEY_PKT_MAX_START);
//			if (propertyValue != null && !propertyValue.isEmpty())
//				this.packetDumpMaxStart = Integer.parseInt(propertyValue);

			propertyValue = OBParser.propertyToString(OBDefine.PROPERTY_FILE_NAME, OBDefine.PROPERTY_KEY_PKT_START);
			if (propertyValue != null && !propertyValue.isEmpty())
				this.packetDumpStart = Integer.parseInt(propertyValue);

			propertyValue = OBParser.propertyToString(OBDefine.PROPERTY_FILE_NAME,
					OBDefine.PROPERTY_KEY_UPTIME_MAX_DIFF_DATE);
			if (propertyValue != null && !propertyValue.isEmpty())
				this.uptimeMaxDiffDate = Integer.parseInt(propertyValue);
		} catch (Exception e) {
		}
	}

	@Override
	public void setParameter(long logKey, OBDtoFaultCheckLog logInfo, OBDtoAdcInfo adcInfo, Object CLIObj)
			throws OBException {
		this.adcInfo = adcInfo;
		this.ClassSnmp = new OBSnmpAlteon(this.adcInfo.getAdcIpAddress(), this.adcInfo.getSnmpInfo());
		this.ClassTelnet = (OBAdcAlteonHandler) CLIObj;
	}

	@Override
	public void loadSnmpOidHWInfo() throws OBException {
		this.snmpOidInfoHW = new OBSnmpOidDB().getFaultCheckHWInfo(this.vendorType, this.swVersion);
	}

	/**
	 * snmp를 이용해서 추출함.
	 */
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
		} catch (Exception e) {
			retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_FAIL);
			OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
			content.setSummary(String.format(OBFaultCheckMsg.HW_POWERSUPPLY_CHECK.getProcessFailMsg()));
			content.setDetail(String.format(OBFaultCheckMsg.SNMP_QUERY.getFailMsg(), oid));
			resultList.add(content);
			msgCLI += String.format(OBFaultCheckMsg.SNMP_QUERY.getFailMsg(), oid);
			msgCLI += "\n";
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
					String.format("failed to query snmp. adcIndex(%d), old(%s)", this.adcInfo.getIndex(), oid));

			retVal.setMsgCLI(msgCLI);
			retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));
		}

		String modelSeries = "";
		// 장비의 모델에 값이 없으면 series 판별할 수 없음. 그 이전에 model 값이 없으면 단절 상태로 진단을 할 수 없음.
		if (this.adcInfo.getModel() != null && !this.adcInfo.getModel().isEmpty()) {
			modelSeries = this.adcInfo.getModel().substring(this.adcInfo.getModel().length() - 4);
		} else {
			modelSeries = "";
		}

		try {
			if (tmpList != null && tmpList.size() > 0) {
				VariableBinding var = tmpList.get(0);
				Integer status = var.getVariable().toInt();// singlePowerSupplyOK(1), firstFailed(2), secondFailed(3),
															// doubleOK(4), unknownPowerSupplyFailed(5)

				// model을 가지고 series를 구한다. 4408 장비는 최신버전에서도 오류가 잇음 (power가 2개 연결되면 1개로 인식, 1개
				// 연결되면 2개로 인식 - 반대로 인식하는 문제가 있음)
				if (modelSeries != null && !modelSeries.isEmpty()) {
					// 2~3 series 인 경우에는 정상(singlePowerSupplyOK) / 비정상 체크
					if (modelSeries.substring(0, 1).equals("2") || modelSeries.substring(0, 1).equals("3")) {
						if (status.intValue() == 1) {
							retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_SUCC);
							OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
							content.setSummary(OBFaultCheckMsg.HW_POWERSUPPLY_CHECK.getSuccessMsg());
							resultList.add(content);
							msgCLI = String.format(OBFaultCheckMsg.SNMP_QUERY_VALUE.getSuccessMsg(), oid,
									status.toString());
							msgCLI += "\n";
						} else {
							retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_FAIL);
							OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
							content.setSummary(OBFaultCheckMsg.HW_POWERSUPPLY_FIRST_INFO.getFailMsg());
							resultList.add(content);
							msgCLI = String.format(OBFaultCheckMsg.SNMP_QUERY_VALUE.getSuccessMsg(), oid,
									status.toString());
							msgCLI += "\n";
						}
					}
					// 2~3 series 가 아닌 경우 정상(doubleOK) / 비정상(singlePowerSupplyOK, firstFailed,
					// secondFailed, unknownPowerSupplyFailed)
					else {
						if (status.intValue() == 4) {
							retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_SUCC);
							OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
							content.setSummary(OBFaultCheckMsg.HW_POWERSUPPLY_CHECK.getSuccessMsg());
							resultList.add(content);
							msgCLI = String.format(OBFaultCheckMsg.SNMP_QUERY_VALUE.getSuccessMsg(), oid,
									status.toString());
							msgCLI += "\n";
						} else {
							if (status.intValue() == 1) {
								retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_FAIL);
								OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
								content.setSummary(OBFaultCheckMsg.HW_POWERSUPPLY_FIRST_INFO.getFailMsg());
								resultList.add(content);
								msgCLI = String.format(OBFaultCheckMsg.SNMP_QUERY_VALUE.getSuccessMsg(), oid,
										status.toString());
								msgCLI += "\n";
							} else if (status.intValue() == 2) {
								retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_FAIL);
								OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
								content.setSummary(OBFaultCheckMsg.HW_POWERSUPPLY_FIRST_INFO.getFailMsg());
								resultList.add(content);
								msgCLI = String.format(OBFaultCheckMsg.SNMP_QUERY_VALUE.getSuccessMsg(), oid,
										status.toString());
								msgCLI += "\n";
							} else if (status.intValue() == 3) {
								retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_FAIL);
								OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
								content.setSummary(OBFaultCheckMsg.HW_POWERSUPPLY_SECOND_INFO.getFailMsg());
								resultList.add(content);
								msgCLI = String.format(OBFaultCheckMsg.SNMP_QUERY_VALUE.getSuccessMsg(), oid,
										status.toString());
								msgCLI += "\n";
							} else {
								retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_FAIL);
								OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
								content.setSummary(OBFaultCheckMsg.HW_POWERSUPPLY_SECOND_INFO.getFailMsg());
								resultList.add(content);
								msgCLI = String.format(OBFaultCheckMsg.SNMP_QUERY_VALUE.getSuccessMsg(), oid,
										status.toString());
								msgCLI += "\n";
							}
						}
					}
				}
				// model 값이 없는 경우 exception처리 - 진단실패
				else {
					throw new Exception();
					// retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_FAIL);
					// OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
					// content.setSummary(String.format(OBFaultCheckMsg.HW_POWERSUPPLY_CHECK.getProcessFailMsg()));
					// content.setDetail(String.format(OBFaultCheckMsg.PARSE_DATA.getFailMsg()));
					// resultList.add(content);
					// msgCLI = String.format(OBFaultCheckMsg.SNMP_QUERY.getFailMsg(), oid);
					// msgCLI +="\n";
					// OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("failed to query
					// snmp. adcIndex(%d), old(%s)", this.adcInfo.getIndex(), oid));
					//
					// retVal.setMsgCLI(msgCLI);
					// retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));
					// return retVal;
				}
			} else {
				retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_INFO);
				OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
				content.setSummary(String.format(OBFaultCheckMsg.NOT_SUPPORT.getFailMsg()));
				content.setDetail(String.format(OBFaultCheckMsg.NOT_SUPPORT.getFailMsg()));
				resultList.add(content);
				msgCLI = String.format(OBFaultCheckMsg.SNMP_QUERY.getSuccessMsg(), oid);
				msgCLI += "\n";
				// OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("failed to query
				// snmp. adcIndex(%d), old(%s)", this.adcInfo.getIndex(), oid));
			}
		} catch (Exception e) {
			retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_FAIL);
			OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
			content.setSummary(String.format(OBFaultCheckMsg.HW_POWERSUPPLY_CHECK.getProcessFailMsg()));
			content.setDetail(String.format(OBFaultCheckMsg.PARSE_DATA.getFailMsg()));
			resultList.add(content);
			msgCLI = String.format(OBFaultCheckMsg.SNMP_QUERY.getFailMsg(), oid);
			msgCLI += "\n";
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
		try {
			// 데이터 추출.
			retMsg = this.ClassTelnet.cmndSysGeneral();
			msgCLI = this.ClassTelnet.getCmndRetString();
		} catch (OBException e) {
			retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_FAIL);
			OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
			content.setSummary(OBFaultCheckMsg.HW_UPTIME_CHECK.getProcessFailMsg());
			content.setDetail(String.format(OBFaultCheckMsg.TELNET_QUERY.getFailMsg(), retMsg));
			resultList.add(content);
			msgCLI += retMsg;
			msgCLI += "\n";
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
					String.format("failed to get telnet data. adcIndex(%d), cli(%s)", this.adcInfo.getIndex(), msgCLI));

			retVal.setMsgCLI(msgCLI);
			retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));
			return retVal;
		}

		// 데이터 분석.
		OBDtoUptimeInfo upTimeInfo = null;
		try {
			upTimeInfo = OBCommon.getValidAlteonCLIParser(adcInfo.getSwVersion()).parseUptimeInfo(retMsg);
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
			if (Math.abs(currTime.getTime() - upTimeInfo.getCurrentTime().getTime()) < this.uptimeMaxDiffDate) {//
				OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
				content.setSummary(OBFaultCheckMsg.HW_UPTIME_INTERVAL_INFO.getSuccessMsg());

				// [GS] #3984-6 #2: 14.07.29 sw.jung Uptime 측정기준 명시(추가)
				// content.setDetail(msgCLI);
				String detail = String.format("ADCsmart Time: %s\n\n%s", new Date(currTime.getTime()),
						msgCLI.substring(0, msgCLI.indexOf("\n\n\n")));
				content.setDetail(detail);

				resultList.add(content);
			} else {// 5분 이상 차이가 나는 경우.
				retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_FAIL);
				OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
				content.setSummary(OBFaultCheckMsg.HW_UPTIME_INTERVAL_INFO.getFailMsg());

				// [GS] #3984-6 #2: 14.07.29 sw.jung Uptime 측정기준 명시(추가)
				// content.setDetail(msgCLI);
				String detail = String.format("ADCsmart Time: %s\n\n%s", new Date(currTime.getTime()),
						msgCLI.substring(0, msgCLI.indexOf("\n\n\n")));
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
		}

		// booting 원인 분석.
		try {
			if (upTimeInfo.getBootType() == OBDtoUptimeInfo.BOOT_TYPE_PANIC
					|| upTimeInfo.getBootType() == OBDtoUptimeInfo.BOOT_TYPE_RESET) {// (software PANIC) or (console
																						// RESET KEY)에 대해서만 실패로 간주함.
				OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
				content.setSummary(OBFaultCheckMsg.HW_UPTIME_BOOT_INFO.getFailMsg());

				// [GS] #3984-6 #2: 14.08.06 sw.jung Uptime 측정기준 명시(추가)
				content.setDetail(String.format("*Error criteria: software PANIC, console RESET key\n\n%s", msgCLI
						.substring(msgCLI.indexOf("Last boot"), msgCLI.indexOf("\n", msgCLI.indexOf("Last boot")))));
				resultList.add(content);
				retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_FAIL);
			} else {
				OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
				content.setSummary(OBFaultCheckMsg.HW_UPTIME_BOOT_INFO.getSuccessMsg());

				// [GS] #3984-6 #2: 14.08.06 sw.jung Uptime 측정기준 명시(추가)
				content.setDetail(String.format("Error criteria: software PANIC, console RESET key\n\n%s", msgCLI
						.substring(msgCLI.indexOf("Last boot"), msgCLI.indexOf("\n", msgCLI.indexOf("Last boot")))));
				resultList.add(content);
			}
		} catch (Exception e) {
			retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_FAIL);

			OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
			content.setSummary(OBFaultCheckMsg.HW_UPTIME_BOOT_INFO.getProcessFailMsg());
			content.setDetail(OBFaultCheckMsg.PARSE_DATA.getFailMsg());
			resultList.add(content);
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
					String.format("failed to parse data. adcIndex(%d), cli(%s)", this.adcInfo.getIndex(), msgCLI));

			retVal.setMsgCLI(msgCLI);
			retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));
		}

		// uptime 정보 추출.
		OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
		content.setSummary(OBFaultCheckMsg.HW_UPTIME_INFO.getSuccessMsg());
		content.setDetail(msgCLI);
		resultList.add(content);

		retVal.setMsgCLI(msgCLI);
		retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));
		return retVal;
	}

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

		String retMsg = "";
		try {
			// 데이터 추출.
			retMsg = this.ClassTelnet.cmndSWKey();
			msgCLI = this.ClassTelnet.getCmndRetString();
		} catch (OBException e) {
			retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_FAIL);
			OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
			content.setSummary(OBFaultCheckMsg.HW_LICENSE_CHECK.getProcessFailMsg());
			content.setDetail(String.format(OBFaultCheckMsg.TELNET_QUERY.getFailMsg(), retMsg));
			resultList.add(content);
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
					String.format("failed to get telnet data. adcIndex(%d), cli(%s)", this.adcInfo.getIndex(), msgCLI));

			retVal.setMsgCLI(msgCLI);
			retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));
			return retVal;
		}

		// 데이터 분석.
		try {
			OBDtoSWKeyAlteon swKey = new OBCLIParserAlteon().parseSWKey(retMsg);

			if (swKey.getLicense() == 0L) {// 데이터 추출에 실패한 경우. 지원되지 않은 모델인 경우.
				retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_INFO);
				OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
				content.setSummary(OBFaultCheckMsg.NOT_SUPPORT.getSuccessMsg());
				content.setDetail(msgCLI);
				resultList.add(content);
			} else {
				if (swKey.getLicense() > swKey.getCurrentUsage()) {// normal
					retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_SUCC);
					OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
					content.setSummary(OBFaultCheckMsg.HW_LICENSE_CHECK.getSuccessMsg());
					content.setDetail(msgCLI);
					resultList.add(content);
				} else {// abnormal
					OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
					content.setSummary(OBFaultCheckMsg.HW_LICENSE_CHECK.getFailMsg());
					content.setDetail(msgCLI);
					resultList.add(content);
				}
			}
		} catch (OBException e) {
			retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_FAIL);

			OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
			content.setSummary(OBFaultCheckMsg.HW_LICENSE_CHECK.getProcessFailMsg());
			content.setDetail(String.format(OBFaultCheckMsg.PARSE_DATA.getFailMsg()));
			resultList.add(content);
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
					String.format("failed to parse data. adcIndex(%d), cli(%s)", this.adcInfo.getIndex(), msgCLI));

			retVal.setMsgCLI(msgCLI);
			retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));
			return retVal;
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
			String summarySpeedText = "";
			String detailSpeedText = "";
			String summaryHalfText = "";
			String detailHalfText = "";
			Long dropTotal = 0L;
			Long errTotal = 0L;
			String detailDropText = "";
			String detailErrText = "";

			OBSnmpAlteon snmp = OBCommon.getValidSnmpAlteonHandler(this.adcInfo.getSwVersion(),
					this.adcInfo.getAdcIpAddress(), this.adcInfo.getSnmpInfo());
			ArrayList<OBDtoMonL2Ports> portInfoList = snmp.getPortsInfo(this.adcInfo.getAdcType(),
					this.adcInfo.getSwVersion());

			for (OBDtoMonL2Ports info : portInfoList) {
				// mode 검사.
				// Integer mode = info.getDuplex();// 0: any, half:1, full:2
				OBDtoAdcPortStatus prevPortStatus = statusMap.get(info.getPortName());
				if (prevPortStatus != null && prevPortStatus.getMode() != info.getDuplex()) {
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

				// half duplex 검사.
				if (info.getStatus() == OBDtoMonL2Ports.STATUS_UP && info.getDuplex() == OBDefine.DUPLEX_HALF) {// link
																												// up만
																												// 대상으로
																												// 함.
					retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_FAIL);

					if (!summaryHalfText.isEmpty())
						summaryHalfText += ", ";
					if (!detailHalfText.isEmpty())
						detailHalfText += "\n";
					summaryHalfText += info.getPortName();
					detailHalfText += String.format(OBFaultCheckMsg.HW_PORT_DUPLEX_HALF_INFO.getFailMsg(),
							info.getPortName(), OBDefine.DUPLEX_HALF_STR);
				}

				// speed 검사.// OBDefine.PORT_SPEED_10;
				if (info.getStatus() == OBDtoMonL2Ports.STATUS_UP && info.getSpeed() == OBDefine.PORT_SPEED_10) {// link
																													// up만
																													// 대상으로
																													// 함.
					retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_FAIL);

					if (!summarySpeedText.isEmpty())
						summarySpeedText += ", ";
					if (!detailSpeedText.isEmpty())
						detailSpeedText += "\n";
					summarySpeedText += info.getPortName();
					detailSpeedText += String.format(OBFaultCheckMsg.HW_PORT_SPEED_INFO.getFailMsg(),
							info.getPortName(), OBDefine.PORT_SPEED_10);
				}

				// drop 패킷 검사
				if (prevPortStatus != null && info.getDropsIn() != 0
						&& prevPortStatus.getDropsIn() != info.getDropsIn()) {
					dropTotal += info.getDropsIn();

					// retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_FAIL);
					retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_INFO); // DROP 패킷이 있더라도 ERROR 아님 INFO로 변경

					if (!detailDropText.isEmpty())
						detailDropText += "\n";
					Long diff = Math.abs(info.getDropsIn() - prevPortStatus.getDropsIn());
					detailDropText += String.format(OBFaultCheckMsg.HW_PORT_DROP_INFO.getFailMsg(), info.getPortName(),
							diff, info.getDropsIn());
				}

				// err 패킷 검사.
				if (prevPortStatus != null && info.getErrorsIn() != 0
						&& prevPortStatus.getErrorsIn() != info.getErrorsIn()) {
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

			if (summarySpeedText.isEmpty())// retVal.getStatus() == OBDtoFaultCheckResultElement.STATUS_SUCC)
			{
				OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
				content.setSummary(String.format(OBFaultCheckMsg.HW_PORT_SPEED_CHECK.getSuccessMsg()));
				resultList.add(content);
			} else {
				OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
				content.setSummary(String.format(OBFaultCheckMsg.HW_PORT_SPEED_CHECK.getFailMsg(), summarySpeedText));
				content.setDetail(String.format(detailSpeedText));
				resultList.add(content);
			}

			if (summaryHalfText.isEmpty())// retVal.getStatus() == OBDtoFaultCheckResultElement.STATUS_SUCC)
			{
				OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
				content.setSummary(String.format(OBFaultCheckMsg.HW_PORT_DUPLEX_HALF.getSuccessMsg()));
				resultList.add(content);
			} else {
				OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
				content.setSummary(String.format(OBFaultCheckMsg.HW_PORT_DUPLEX_HALF.getFailMsg(), summaryHalfText));
				content.setDetail(String.format(detailHalfText));
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

		if (this.snmpOidInfoHW == null || this.snmpOidInfoHW.getMpCpuStats64Sec().isEmpty()
				|| this.snmpOidInfoHW.getSpCpuStats64Sec().isEmpty())
			return retVal;

		List<VariableBinding> tmpList = null;
		String mpOid = this.snmpOidInfoHW.getMpCpuStats64Sec();
		String spOid = this.snmpOidInfoHW.getSpCpuStats64Sec();
		Integer mpUsage = 0;
		try {
			retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_SUCC);
			// mp 사용량 조회.
			tmpList = this.ClassSnmp.walk(mpOid);
		} catch (Exception e) {
			retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_FAIL);
			OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
			content.setSummary(String.format(OBFaultCheckMsg.HW_CPU_CHECK.getProcessFailMsg()));
			content.setDetail(String.format(OBFaultCheckMsg.SNMP_QUERY.getFailMsg(), mpOid));
			resultList.add(content);
			msgCLI += String.format(OBFaultCheckMsg.SNMP_QUERY.getFailMsg(), mpOid);
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
					String.format("failed to query snmp. adcIndex(%d), old(%s)", this.adcInfo.getIndex(), mpOid));

			retVal.setMsgCLI(msgCLI);
			retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));
			return retVal;
		}

		retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_INFO);

		boolean isOkFlag = true;
		String detailMsg = "";

		try {
			// mp 조사.
			if (tmpList != null && tmpList.size() > 0) {
				VariableBinding var = tmpList.get(0);
				mpUsage = var.getVariable().toInt();//

				msgCLI += String.format(OBFaultCheckMsg.SNMP_QUERY_VALUE.getSuccessMsg(), mpOid, mpUsage.toString());
				msgCLI += "\n";

				if (!detailMsg.isEmpty())
					detailMsg += "\n";
				detailMsg += String.format(OBFaultCheckMsg.HW_MP_CPU_INFO.getSuccessMsg(), mpUsage);
				// OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
				// content.setDetail(String.format(OBFaultCheckMsg.SNMP_QUERY_VALUE.getSuccessMsg(),
				// mpOid, mpUsage.toString()));
				// resultList.add(content);

				if (mpUsage > max)
					isOkFlag = false;
			} else {
				isOkFlag = false;
				if (!detailMsg.isEmpty())
					detailMsg += "\n";
				detailMsg += String.format(OBFaultCheckMsg.HW_MP_CPU_INFO.getProcessFailMsg());

				msgCLI += String.format(OBFaultCheckMsg.SNMP_QUERY_VALUE.getFailMsg(), mpOid, "data not found");
				msgCLI += "\n";
			}
		} catch (Exception e) {
			retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_FAIL);
			OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
			content.setSummary(String.format(OBFaultCheckMsg.HW_CPU_CHECK.getProcessFailMsg()));
			content.setDetail(String.format(OBFaultCheckMsg.PARSE_DATA.getFailMsg()));
			resultList.add(content);

			msgCLI += String.format(OBFaultCheckMsg.PARSE_DATA.getFailMsg());
			msgCLI += "\n";
			return retVal;
		}

		// sp 사용량 조회.
		try {
			tmpList = this.ClassSnmp.walk(spOid);
		} catch (Exception e) {
			retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_FAIL);
			OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
			content.setSummary(String.format(OBFaultCheckMsg.HW_CPU_CHECK.getProcessFailMsg()));
			content.setDetail(String.format(OBFaultCheckMsg.SNMP_QUERY.getFailMsg(), spOid));
			resultList.add(content);
			msgCLI += String.format(OBFaultCheckMsg.SNMP_QUERY.getFailMsg(), mpOid);
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
					String.format("failed to query snmp. adcIndex(%d), old(%s)", this.adcInfo.getIndex(), mpOid));

			retVal.setMsgCLI(msgCLI);
			retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));
			return retVal;
		}

		try {
			if (tmpList == null || tmpList.size() == 0) {
				isOkFlag = false;
				if (!detailMsg.isEmpty())
					detailMsg += "\n";
				detailMsg += String.format(OBFaultCheckMsg.HW_SP_CPU_INFO.getProcessFailMsg());

				msgCLI += String.format(OBFaultCheckMsg.SNMP_QUERY_VALUE.getFailMsg(), mpOid, "data not found");
				msgCLI += "\n";
			} else {
				for (VariableBinding var : tmpList) {
					ArrayList<Integer> oidList = this.ClassSnmp.parseOid(var.getOid().toString(), spOid);
					Integer spIndex = oidList.get(0);
					Integer spValue = var.getVariable().toInt();

					msgCLI += String.format(OBFaultCheckMsg.SNMP_QUERY_VALUE.getSuccessMsg(), spOid,
							spValue.toString());
					msgCLI += "\n";

					if (!detailMsg.isEmpty())
						detailMsg += "\n";
					detailMsg += String.format(OBFaultCheckMsg.HW_SP_CPU_INFO.getSuccessMsg(), spIndex, spValue);

					// OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
					// content.setDetail(String.format(OBFaultCheckMsg.SNMP_QUERY_VALUE.getSuccessMsg(),
					// spOid, spValue.toString()));
					// resultList.add(content);

					if (spValue > max)
						isOkFlag = false;
				}
			}
		} catch (Exception e) {
			retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_FAIL);
			OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
			content.setSummary(String.format(OBFaultCheckMsg.HW_CPU_CHECK.getProcessFailMsg()));
			content.setDetail(String.format(OBFaultCheckMsg.PARSE_DATA.getFailMsg()));
			resultList.add(content);
			msgCLI += String.format(OBFaultCheckMsg.PARSE_DATA.getFailMsg());
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
					String.format("failed to query snmp. adcIndex(%d), old(%s)", this.adcInfo.getIndex(), mpOid));

			retVal.setMsgCLI(msgCLI);
			retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));
			return retVal;
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
		String freeOid = this.snmpOidInfoHW.getMpMemStatsFree();
		String totalOid = this.snmpOidInfoHW.getMpMemStatsTotal();
		Long totalValue = 0L;
		Long freeValue = 0L;

		try {
			// total 조회.
			tmpList = this.ClassSnmp.walk(totalOid);
		} catch (Exception e) {
			retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_NA);
			OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
			content.setSummary(String.format(OBFaultCheckMsg.NOT_SUPPORT.getSuccessMsg()));
			content.setDetail(String.format(OBFaultCheckMsg.NOT_SUPPORT.getSuccessMsg(), totalOid));
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
				content.setDetail(String.format(OBFaultCheckMsg.SNMP_QUERY.getSuccessMsg(), totalOid));
				resultList.add(content);
				msgCLI += String.format(OBFaultCheckMsg.SNMP_QUERY.getFailMsg(), totalOid);
				// OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("failed to query
				// snmp. adcIndex(%d), old(%s)", this.adcInfo.getIndex(), totalOid));
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
			tmpList = this.ClassSnmp.walk(freeOid);
			if (tmpList != null && tmpList.size() > 0) {
				VariableBinding var = tmpList.get(0);
				freeValue = var.getVariable().toLong();//

				msgCLI += String.format(OBFaultCheckMsg.SNMP_QUERY_VALUE.getSuccessMsg(), freeOid,
						freeValue.toString());
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
			Long usage = 100L - (freeValue * 100L / totalValue);
			String[] verElements = swVersion.split("\\."); // 버전 요소 두개까지 문자열로 건진다. 지금은 하나만 쓴다.
			if (Integer.parseInt(verElements[0]) >= 30) {
				max = 95;
			}
			if (usage < max) {// 정상 케이스.
				retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_SUCC);
				content.setSummary(String.format(OBFaultCheckMsg.HW_MEMORY_CHECK.getSuccessMsg(), usage, max));
			} else {
				retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_FAIL);
				content.setSummary(String.format(OBFaultCheckMsg.HW_MEMORY_CHECK.getFailMsg(), usage, max));
			}
			String totalString = OBUtility.convertKMG(totalValue);
			String freeString = OBUtility.convertKMG(freeValue);
			content.setDetail(
					String.format(OBFaultCheckMsg.HW_MEMORY_FREE_INFO.getSuccessMsg(), usage, totalString, freeString));
			// content.setDetail(String.format(OBFaultCheckMsg.HW_MEMORY_FREE_INFO.getSuccessMsg(),
			// usage, totalValue, freeValue));
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
			content.setSummary(String.format(OBFaultCheckMsg.HW_TEMP_NORM_CHECK.getProcessFailMsg()));
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
			content.setDetail(String.format(OBFaultCheckMsg.DATA_NOT_EXIST.getSuccessMsg(), oid));
			resultList.add(content);
			msgCLI += String.format(OBFaultCheckMsg.SNMP_QUERY.getSuccessMsg(), oid);

			retVal.setMsgCLI(msgCLI);
			retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));
			return retVal;
		}

		try {
			if (tmpList != null && tmpList.size() > 0) {
				VariableBinding var = tmpList.get(0);
				Integer status = var.getVariable().toInt();// 온도 상태 검사. Ok(1), exceed(2)

				msgCLI += String.format(OBFaultCheckMsg.SNMP_QUERY_VALUE.getSuccessMsg(), oid, status.toString());
				msgCLI += "\n";

				if (status.intValue() == 1) {
					retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_SUCC);
					OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
					content.setSummary(String.format(OBFaultCheckMsg.HW_TEMP_NORM_CHECK.getSuccessMsg()));
					content.setDetail(OBFaultCheckMsg.HW_TEMP_NORM_INFO.getSuccessMsg());
					resultList.add(content);
				} else {
					retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_FAIL);
					OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
					content.setSummary(String.format(OBFaultCheckMsg.HW_TEMP_NORM_CHECK.getFailMsg()));
					content.setDetail(OBFaultCheckMsg.HW_TEMP_NORM_INFO.getSuccessMsg());
					resultList.add(content);
				}
			} else {
				retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_FAIL);
				OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
				content.setSummary(String.format(OBFaultCheckMsg.HW_TEMP_NORM_CHECK.getProcessFailMsg()));
				content.setDetail(String.format(OBFaultCheckMsg.SNMP_QUERY_VALUE.getFailMsg(), oid, "data not found"));
				resultList.add(content);
				msgCLI += String.format(OBFaultCheckMsg.SNMP_QUERY.getFailMsg(), oid);
				OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
						String.format("failed to query snmp. adcIndex(%d), old(%s)", this.adcInfo.getIndex(), oid));
			}

		} catch (Exception e) {
			retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_FAIL);
			OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
			content.setSummary(String.format(OBFaultCheckMsg.HW_TEMP_NORM_CHECK.getProcessFailMsg()));
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
		String retMsg = "";

		if (this.snmpOidInfoHW == null || this.snmpOidInfoHW.getHwFanStatus().isEmpty())
			return retVal;

		List<VariableBinding> tmpList;
		String oid = this.snmpOidInfoHW.getHwFanStatus();
		try {
			tmpList = this.ClassSnmp.walk(oid);
			retMsg = this.ClassTelnet.cmndInfoFan();
			msgCLI = this.ClassTelnet.getCmndRetString();
		} catch (Exception e) {
			retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_FAIL);
			OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
			content.setSummary(String.format(OBFaultCheckMsg.HW_FAN_NORM_CHECK.getProcessFailMsg()));
			content.setDetail(String.format(OBFaultCheckMsg.SNMP_QUERY.getFailMsg(), oid));
			content.setDetail(OBFaultCheckMsg.PARSE_DATA.getFailMsg());

			resultList.add(content);
			msgCLI += String.format(OBFaultCheckMsg.SNMP_QUERY.getFailMsg(), oid);
			msgCLI += retMsg;
			msgCLI += "\n";
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format(
					"failed to query snmp. adcIndex(%d), old(%s), cli(%s)", this.adcInfo.getIndex(), oid, retMsg));

			retVal.setMsgCLI(msgCLI);
			retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));
			return retVal;
		}

		if (tmpList == null || tmpList.isEmpty()) {
			retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_NA);
			OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
			content.setSummary(String.format(OBFaultCheckMsg.NOT_SUPPORT.getSuccessMsg()));
			content.setDetail(String.format(OBFaultCheckMsg.NOT_SUPPORT.getSuccessMsg(), oid));
			content.setDetail(retMsg);
			resultList.add(content);
			msgCLI += String.format(OBFaultCheckMsg.SNMP_QUERY.getSuccessMsg(), oid);

			retVal.setMsgCLI(msgCLI);
			retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));
			return retVal;
		}

		try {
			if (tmpList != null && tmpList.size() > 0) {
				VariableBinding var = tmpList.get(0);
				Integer status = var.getVariable().toInt();// fan 상태. ok(1), fail(2)

				msgCLI += String.format(OBFaultCheckMsg.SNMP_QUERY_VALUE.getSuccessMsg(), oid, status.toString());
				msgCLI += "\n";

				if (status.intValue() == 1) {
					OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
					if (retMsg.indexOf("failed") != -1) {
						retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_FAIL);
						content.setSummary(String.format(OBFaultCheckMsg.HW_FAN_NORM_CHECK.getFailMsg()));
					} else {
						retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_SUCC);
						content.setSummary(String.format(OBFaultCheckMsg.HW_FAN_RPM_CHECK.getSuccessMsg()));
					}
					// content.setDetail(String.format(OBFaultCheckMsg.SNMP_QUERY_VALUE.getSuccessMsg(),
					// oid, status.toString()));
					content.setDetail(retMsg);
					resultList.add(content);
				} else {
					retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_FAIL);
					OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
					content.setSummary(String.format(OBFaultCheckMsg.HW_FAN_NORM_CHECK.getFailMsg()));
					// content.setDetail(String.format(OBFaultCheckMsg.SNMP_QUERY_VALUE.getSuccessMsg(),
					// oid, status.toString()));
					content.setDetail(retMsg);
					resultList.add(content);
				}
			} else {
				retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_FAIL);
				OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
				content.setSummary(String.format(OBFaultCheckMsg.HW_FAN_NORM_CHECK.getProcessFailMsg()));
				content.setDetail(String.format(OBFaultCheckMsg.SNMP_QUERY_VALUE.getFailMsg(), oid, "data not found"));
				resultList.add(content);
				msgCLI += String.format(OBFaultCheckMsg.SNMP_QUERY.getFailMsg(), oid);
				OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
						String.format("failed to query snmp. adcIndex(%d), old(%s)", this.adcInfo.getIndex(), oid));
			}
		} catch (Exception e) {
			retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_FAIL);
			OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
			content.setSummary(String.format(OBFaultCheckMsg.HW_FAN_NORM_CHECK.getProcessFailMsg()));
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

	/**
	 * DB에서 조회한다.
	 */
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

		String msgCLI = "";

		String retMsg = "";
		try {
			// 데이터 추출.
			retMsg = this.ClassTelnet.cmndInfoVlan();
			msgCLI = this.ClassTelnet.getCmndRetString();
		} catch (Exception e) {
			retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_FAIL);
			OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
			content.setSummary(OBFaultCheckMsg.L23_VLAN_CHECK.getProcessFailMsg());
			content.setDetail(String.format(OBFaultCheckMsg.TELNET_QUERY.getFailMsg(), retMsg));
			resultList.add(content);
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
					String.format("failed to get telnet data. adcIndex(%d), cli(%s)", this.adcInfo.getIndex(), msgCLI));

			retVal.setMsgCLI(msgCLI);
			retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));
			return retVal;
		}

		if (msgCLI != null && !msgCLI.isEmpty()) {
			try {
				// 데이터 분석.
				retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_INFO);

				OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
				content.setSummary(OBFaultCheckMsg.L23_VLAN_CHECK.getSuccessMsg());
				content.setDetail(msgCLI);
				resultList.add(content);
			} catch (Exception e) {
				retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_FAIL);

				OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
				content.setSummary(OBFaultCheckMsg.L23_VLAN_CHECK.getProcessFailMsg());
				content.setDetail(OBFaultCheckMsg.PARSE_DATA.getFailMsg());
				resultList.add(content);
				OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
						String.format("failed to parse data. adcIndex(%d), cli(%s)", this.adcInfo.getIndex(), msgCLI));

				retVal.setMsgCLI(msgCLI);
				retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));
				return retVal;
			}
		}

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
			retMsg = this.ClassTelnet.cmndInfoStg();
			msgCLI = this.ClassTelnet.getCmndRetString();
		} catch (OBException e) {
			retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_FAIL);
			content = new OBDtoFaultCheckResultContent();
			content.setSummary(OBFaultCheckMsg.L23_STP_CHECK.getProcessFailMsg());
			content.setDetail(String.format(OBFaultCheckMsg.TELNET_QUERY.getFailMsg(), retMsg));
			resultList.add(content);
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
					String.format("failed to get telnet data. adcIndex(%d), cli(%s)", this.adcInfo.getIndex(), msgCLI));

			retVal.setMsgCLI(msgCLI);
			retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));
			// return retVal;
		}

		// 데이터 분석.
		if (retMsg == null || retMsg.isEmpty()) {
			retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_FAIL);
			content = new OBDtoFaultCheckResultContent();
			content.setSummary(OBFaultCheckMsg.L23_STP_CHECK.getProcessFailMsg());
			content.setDetail(String.format(OBFaultCheckMsg.DATA_NOT_EXIST.getFailMsg(), retMsg));
			resultList.add(content);
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
					String.format("failed to get ssh data. adcIndex(%d), cli(%s)", this.adcInfo.getIndex(), retMsg));

			retVal.setMsgCLI(retMsg);
			retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));
			return retVal;
		} else {
			content = new OBDtoFaultCheckResultContent();
			int result = analysisL23StpStatus(retMsg);
			if (result == OBDtoFaultCheckResultElement.STATUS_SUCC) {
				content.setSummary(OBFaultCheckMsg.L23_STP_CHECK.getSuccessMsg());
				content.setDetail(retMsg);
				retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_SUCC);
			} else {
				content.setSummary(OBFaultCheckMsg.L23_STP_CHECK.getFailMsg());
				content.setDetail(retMsg);
				retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_FAIL);
			}
			resultList.add(content);
		}
		msgCLI += retMsg;

		// 변화를 감지해야 한다. TODO
		retVal.setMsgCLI(msgCLI);
		retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));
		return retVal;
	}

	private int analysisL23StpStatus(String cliMsg) {
		int ret = OBDtoFaultCheckResultElement.STATUS_SUCC;

		if (cliMsg == null || cliMsg.length() == 0) {
			return OBDtoFaultCheckResultElement.STATUS_FAIL;
		}

		String lines[] = cliMsg.split("\n");

		for (String line : lines) {
			line = OBParser.trimAndSimplifySpaces(line);
			if (line.matches(".+(DISCARD|discard|LISTENING|listening|LEARNING|learning).+")) {
				ret = OBDtoFaultCheckResultElement.STATUS_FAIL;
			}
		}
		return ret;
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

		String msgCLI = "";

		// String cliMsg = "";
		String retMsg = "";

		// trunk 정보 추출.
		try {
			// 데이터 추출.
			retMsg = this.ClassTelnet.cmndInfoTrunk();
			msgCLI = this.ClassTelnet.getCmndRetString();
		} catch (OBException e) {
			retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_FAIL);
			OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
			content.setSummary(OBFaultCheckMsg.L23_TRUNK_CHECK.getProcessFailMsg());
			content.setDetail(String.format(OBFaultCheckMsg.TELNET_QUERY.getFailMsg(), retMsg));
			resultList.add(content);
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
					String.format("failed to get telnet data. adcIndex(%d), cli(%s)", this.adcInfo.getIndex(), msgCLI));

			retVal.setMsgCLI(msgCLI);
			retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));
			// return retVal;
		}

		// 데이터 분석.
		if (msgCLI != null && !msgCLI.isEmpty()) {
			OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
			content.setSummary(OBFaultCheckMsg.L23_TRUNK_CHECK.getSuccessMsg());
			String msg = OBMessages.getMessage(OBMessages.MSG_FCHKMSG_TRUNK_BASIC_WARN_INFO) + "\n\n";
			msg += msgCLI;
			content.setDetail(msg);
			resultList.add(content);
		} else {
			OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
			content.setSummary(OBFaultCheckMsg.L23_TRUNK_CHECK.getFailMsg());
			String msg = OBMessages.getMessage(OBMessages.MSG_FCHKMSG_TRUNK_BASIC_WARN_INFO);
			content.setDetail(msg);
			resultList.add(content);
		}

		// link 정보를 추출한다.
		try {
			// 데이터 추출.
			retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_INFO);
			retMsg = this.ClassTelnet.cmndInfoLink();
			msgCLI = this.ClassTelnet.getCmndRetString();
		} catch (OBException e) {
			retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_FAIL);
			OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
			content.setSummary(OBFaultCheckMsg.L23_LINK_CHECK.getProcessFailMsg());
			content.setDetail(String.format(OBFaultCheckMsg.TELNET_QUERY.getFailMsg(), retMsg));
			resultList.add(content);
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
					String.format("failed to get telnet data. adcIndex(%d), cli(%s)", this.adcInfo.getIndex(), retMsg));

			retVal.setMsgCLI(msgCLI);
			retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));
			return retVal;
		}

		// 데이터 분석.
		if (msgCLI != null && !msgCLI.isEmpty()) {
			retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_INFO);
			OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
			content.setSummary(OBFaultCheckMsg.L23_LINK_CHECK.getSuccessMsg());
			content.setDetail(msgCLI);
			resultList.add(content);
		} else {
			retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_INFO);
			OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
			content.setSummary(OBFaultCheckMsg.L23_LINK_CHECK.getSuccessMsg());
			content.setDetail(msgCLI);
			resultList.add(content);
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

		String msgCLI = "";

		String cliMsg = "";
		String retMsg = "";

		// vrrp 정보 추출.
		try {
			// 데이터 추출.
			retMsg = this.ClassTelnet.cmndInfoL3Vrrp();
			cliMsg += this.ClassTelnet.getCmndRetString();
			cliMsg += "\n";
		} catch (OBException e) {
			retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_FAIL);

			OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
			content.setSummary(OBFaultCheckMsg.L23_VRRP_CHECK.getProcessFailMsg());
			content.setDetail(String.format(OBFaultCheckMsg.TELNET_QUERY.getFailMsg(), cliMsg));
			resultList.add(content);
			msgCLI += cliMsg;
			msgCLI += "\n";
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
					String.format("failed to parse data. adcIndex(%d), cli(%s)", this.adcInfo.getIndex(), cliMsg));

			retVal.setMsgCLI(msgCLI);
			retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));
			return retVal;
		}

		// 데이터 분석.
		if (cliMsg != null && !cliMsg.isEmpty()) {
			OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
			content.setSummary(OBFaultCheckMsg.L23_VRRP_CHECK.getSuccessMsg());
			content.setDetail(retMsg);
			resultList.add(content);
		} else {
			OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
			content.setSummary(OBFaultCheckMsg.L23_VRRP_CHECK.getFailMsg());
			content.setDetail(retMsg);
			resultList.add(content);
		}
		msgCLI += cliMsg;
		msgCLI += "\n";

		// arp 정보를 추출한다.
		try {
			// 데이터 추출.
			retMsg = this.ClassTelnet.cmndInfoL3Arp();
			cliMsg = this.ClassTelnet.getCmndRetString();
		} catch (OBException e) {
			retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_FAIL);

			OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
			content.setSummary(OBFaultCheckMsg.L23_ARP_CHECK.getProcessFailMsg());
			content.setDetail(String.format(OBFaultCheckMsg.TELNET_QUERY.getFailMsg(), cliMsg));
			resultList.add(content);
			msgCLI += cliMsg;
			msgCLI += "\n";
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
					String.format("failed to parse data. adcIndex(%d), cli(%s)", this.adcInfo.getIndex(), cliMsg));

			retVal.setMsgCLI(msgCLI);
			retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));
			return retVal;
		}

		// 데이터 분석.
		if (cliMsg != null && !cliMsg.isEmpty()) {
			OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
			content.setSummary(OBFaultCheckMsg.L23_ARP_CHECK.getSuccessMsg());
			content.setDetail(retMsg);
			resultList.add(content);
		} else {
			OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
			content.setSummary(OBFaultCheckMsg.L23_ARP_CHECK.getFailMsg());
			content.setDetail(retMsg);
			resultList.add(content);
		}
		msgCLI += cliMsg;
		msgCLI += "\n";

		// 이중화 상태 조회..
		try {
			Integer state = OBCommon
					.getValidSnmpAlteonHandler(this.adcInfo.getSwVersion(), this.adcInfo.getAdcIpAddress(),
							this.adcInfo.getSnmpInfo())
					.getActiveStandbyState(this.adcInfo.getAdcType(), this.adcInfo.getSwVersion());
			OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
			if (state == OBDtoAdcInfo.ACTIVE_STANDBY_STATE_STANDBY) {
				content.setSummary(String.format(OBFaultCheckMsg.L23_FAILOVER_CHECK.getSuccessMsg(), "STANDBY"));
				// content.setDetail(OBFaultCheckMsg.L23_FAILOVER_STANDBY_STATUS.getSuccessMsg());
			} else if (state == OBDtoAdcInfo.ACTIVE_STANDBY_STATE_ACTIVE) {
				content.setSummary(String.format(OBFaultCheckMsg.L23_FAILOVER_CHECK.getSuccessMsg(), "ACTIVE"));
				// content.setDetail(OBFaultCheckMsg.L23_FAILOVER_ACTIVE_STATUS.getSuccessMsg());
			} else {
				content.setSummary(String.format(OBFaultCheckMsg.L23_FAILOVER_CHECK.getSuccessMsg(), "UNKNOWN"));
				// content.setDetail(OBFaultCheckMsg.L23_FAILOVER_UNKNOWN_STATUS.getSuccessMsg());
			}

			resultList.add(content);
		} catch (Exception e) {
			retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_FAIL);

			OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
			content.setSummary(OBFaultCheckMsg.L23_FAILOVER_CHECK.getProcessFailMsg());
			content.setDetail(OBFaultCheckMsg.PARSE_DATA.getFailMsg());
			resultList.add(content);
			return retVal;
		}

		// 탐지 방법이 잘못되어 우선은 삭제한다. 2014.2.24
		// // MAC flapping 탐지한다. arp dump 정보를 이용해야 ip+mac 이 지정된 포트가 변경되었을 경우에 탐지한다.
		// try
		// {
		// HashMap<String, OBDtoArpInfo> arpMap = new
		// OBFaultMonitoringDB().getArpInfoList(this.adcInfo.getIndex(), this.db);
		//
		// ArrayList<OBDtoArpInfo> arpInfoList =
		// OBCommon.getValidSnmpAlteonHandler(this.adcInfo.getSwVersion(),
		// this.adcInfo.getAdcIpAddress()).getArpInfo(this.adcInfo.getAdcType(),
		// this.adcInfo.getSwVersion(), this.adcInfo.getSnmpRComm(), this.db);
		// OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
		// content.setSummary(OBFaultCheckMsg.L23_MAC_FLAPPING_CHECK.getSuccessMsg());
		// content.setDetail(cliMsg);
		// String detailMsg = "";
		// for(OBDtoArpInfo info:arpInfoList)
		// {
		// OBDtoArpInfo prevInfo = arpMap.get(info.getDstIPAddr());
		// if(prevInfo==null)
		// continue;
		// if(prevInfo.getMacAddr().equals(info.getMacAddr()) &&
		// !prevInfo.getPortNum().equals(info.getPortNum()))
		// {// flapping 상태임.
		// retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_FAIL);
		// content.setSummary(OBFaultCheckMsg.L23_MAC_FLAPPING_CHECK.getFailMsg());
		// detailMsg += String.format("IP:%s, MAC:%s, PORT:%s\n", info.getDstIPAddr(),
		// info.getMacAddr(), info.getPortNum());
		// }
		// }
		// if(!detailMsg.isEmpty())
		// content.setDetail(detailMsg);
		//
		// resultList.add(content);
		// }
		// catch(Exception e)
		// {
		// retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_FAIL);
		//
		// OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
		// content.setSummary(OBFaultCheckMsg.L23_MAC_FLAPPING_CHECK.getProcessFailMsg());
		// content.setDetail(OBFaultCheckMsg.PARSE_DATA.getFailMsg());
		// resultList.add(content);
		// return retVal;
		// }

		retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_INFO);

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

		String cliMsg = "";
		String retMsg = "";
		try {
			// 데이터 추출.
			retMsg = this.ClassTelnet.cmndInfoL3Route();
			cliMsg = this.ClassTelnet.getCmndRetString();
		} catch (OBException e) {
			retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_FAIL);

			OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
			content.setSummary(OBFaultCheckMsg.L23_ROUTING_CHECK.getProcessFailMsg());
			content.setDetail(OBFaultCheckMsg.PARSE_DATA.getFailMsg());
			resultList.add(content);
			msgCLI += cliMsg;
			msgCLI += "\n";
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
					String.format("failed to parse data. adcIndex(%d), cli(%s)", this.adcInfo.getIndex(), cliMsg));

			retVal.setMsgCLI(msgCLI);
			retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));
		}

		// 데이터 분석.
		if (cliMsg != null && !cliMsg.isEmpty()) {
			OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
			content.setSummary(OBFaultCheckMsg.L23_ROUTING_CHECK.getSuccessMsg());
			content.setDetail(retMsg);
			resultList.add(content);
		} else {
			OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
			content.setSummary(OBFaultCheckMsg.L23_ROUTING_CHECK.getFailMsg());
			content.setDetail(retMsg);
			resultList.add(content);
		}

		retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_INFO);

		msgCLI += cliMsg;
		msgCLI += "\n";

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

		String msgCLI = "";

		String cliMsg = "";
		String retMsg = "";
		try {
			// 데이터 추출.
			retMsg = this.ClassTelnet.cmndInfoL3IP();
			cliMsg = this.ClassTelnet.getCmndRetString();
		} catch (OBException e) {
			retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_FAIL);

			OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
			content.setSummary(OBFaultCheckMsg.L23_INTERFACE_CHECK.getProcessFailMsg());
			content.setDetail(OBFaultCheckMsg.PARSE_DATA.getFailMsg());
			resultList.add(content);
			msgCLI += cliMsg;
			msgCLI += "\n";
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
					String.format("failed to parse data. adcIndex(%d), cli(%s)", this.adcInfo.getIndex(), cliMsg));

			retVal.setMsgCLI(msgCLI);
			retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));
			return retVal;
		}

		// 데이터 분석.
		if (cliMsg != null && !cliMsg.isEmpty()) {
			OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
			content.setSummary(OBFaultCheckMsg.L23_INTERFACE_CHECK.getSuccessMsg());
			content.setDetail(retMsg);
			retVal.setStatus(analysisL23Status(cliMsg));
			resultList.add(content);
		} else {
			OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
			content.setSummary(OBFaultCheckMsg.L23_INTERFACE_CHECK.getFailMsg());
			retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_FAIL);
			resultList.add(content);
		}

		msgCLI += cliMsg;
		msgCLI += "\n";

		retVal.setMsgCLI(msgCLI);

		retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));
		return retVal;
	}

	// 아래 함수가 식별하는 interface와 gateway 부분
	// Interface information:
	// 1: IP4 10.10.60.1 255.255.255.0 10.10.60.255 , vlan 1, DOWN
	// 2: IP4 10.10.40.1 255.255.255.0 10.10.40.255 , vlan 3, up
	// 3: IP4 10.10.50.1 255.255.255.0 10.10.50.255 , vlan 2, up
	//
	// Default gateway information: metric strict
	// 1: 10.10.40.254, vlan any, up
	// 3: 10.10.40.211, vlan any, FAILED
	private int analysisL23Status(String cliMsg) {
		int ret = OBDtoFaultCheckResultElement.STATUS_SUCC;
		final int POS_NONE = 0;
		final int POS_INTERFACE = 1;
		final int POS_GATEWAY = 2;
		int position = POS_NONE;

		if (cliMsg == null || cliMsg.length() == 0) {
			return OBDtoFaultCheckResultElement.STATUS_FAIL;
		}

		String lines[] = cliMsg.split("\n");

		for (String line : lines) {
			line = OBParser.trimAndSimplifySpaces(line);
			if (line.startsWith("Interface information:")) {
				position = POS_INTERFACE;
				continue;
			}
			if (line.startsWith("Default gateway information:")) {
				position = POS_GATEWAY;
				continue;
			}
			if (position == POS_INTERFACE || position == POS_GATEWAY) {
				if (line.matches("^\\d+\\:.*") == false) // 지금 위치가 interface,gateway였다가 바뀌면
				{
					position = POS_NONE;
					continue;
				} else
				// 1:.... 2:....
				{
					if (line.matches("^\\d+\\:.*(DOWN|down|FAILED|failed)")) {
						ret = OBDtoFaultCheckResultElement.STATUS_FAIL;
					}
				}
			}
		}
		return ret;
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
				content.setSummary(String.format(OBFaultCheckMsg.L47_UNUSED_NODE_CHECK.getFailMsg()));// 데이터 없음 나타냄.
				content.setDetail("");
				resultList.add(content);
			} else {
				String msg = "";
				for (OBDtoSlbNodeInfo nodeObj : nodeList) {
					msg += String.format("%-30s %-30s %s", nodeObj.getIndex(), nodeObj.getIpAddress(),
							nodeObj.getName());
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
				for (OBDtoSlbPoolInfo poolObj : poolList) {
					msg += String.format("%-30s %-30s", poolObj.getIndex(), poolObj.getName());
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
				for (OBDtoSlbPoolInfo poolObj : poolList) {
					msg += String.format("%-30s %-30s", poolObj.getIndex(), poolObj.getName());
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

		// 유휴 노드 추출. //분석-진단에서 L4-7 유휴설정에 "SLB : 일 동안 사용하지 않은 REAL SERVER 없음" 기능 막음. SLB
		// real data 수집 기능이 현재 없음.junhyun.ok_GS
		// try
		// {
		// // 데이터 추출.
		// ArrayList<OBDtoSlbNodeInfo> nodeList = new
		// OBFaultMngImpl().getSleepNodeList(this.adcInfo, sleepDay, this.db);
		// if(nodeList.size()==0)
		// {
		// OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
		// content.setSummary(String.format(OBFaultCheckMsg.L47_SLEEP_NODE_CHECK.getFailMsg(),
		// sleepDay));
		// resultList.add(content);
		// }
		// else
		// {
		// String msg = "";
		// for(OBDtoSlbNodeInfo nodeObj: nodeList)
		// {
		// msg += String.format("%-30s %-30s %s", nodeObj.getIndex(),
		// nodeObj.getIpAddress(), nodeObj.getName());
		// msg +="\n";
		// }
		// OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
		// content.setSummary(String.format(OBFaultCheckMsg.L47_SLEEP_NODE_CHECK.getSuccessMsg(),
		// sleepDay, nodeList.size()));
		// content.setDetail(msg);
		// resultList.add(content);
		// }
		// }
		// catch(OBException e)
		// {
		// retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_FAIL);
		// OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
		// content.setSummary(String.format(OBFaultCheckMsg.L47_SLEEP_NODE_CHECK.getProcessFailMsg(),
		// sleepDay));
		// content.setDetail(String.format(OBFaultCheckMsg.DB_QUERY.getFailMsg()));
		// resultList.add(content);
		// msgCLI += String.format(OBFaultCheckMsg.DB_QUERY.getFailMsg());
		// OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("failed to query db.
		// adcIndex(%d)", this.adcInfo.getIndex()));
		//
		// retVal.setMsgCLI(msgCLI);
		// retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));
		// }

		// 유휴 POOL 추출.
		try {
			// 데이터 추출.
			ArrayList<OBDtoSlbPoolInfo> poolList = new OBFaultMngImpl().getSleepPoolList(this.adcInfo, sleepDay);
			if (poolList.size() == 0) {
				OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
				content.setSummary(String.format(OBFaultCheckMsg.L47_SLEEP_POOL_CHECK.getFailMsg(), sleepDay));
				resultList.add(content);
			} else {
				String msg = "";
				for (OBDtoSlbPoolInfo poolObj : poolList) {
					msg += String.format("%-30s %s", poolObj.getIndex(), poolObj.getName());
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

		// Flb 유휴 노드 추출.
		try {
			// 데이터 추출.
			ArrayList<OBDtoSlbNodeInfo> nodeList = new OBFaultMngImpl().getFlbSleepNodeList(this.adcInfo, sleepDay);
			if (nodeList.size() == 0) {
				OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
				content.setSummary(String.format(OBFaultCheckMsg.L47_SLEEP_FLB_NODE_CHECK.getFailMsg(), sleepDay));
				resultList.add(content);
			} else {
				String msg = "";
				for (OBDtoSlbNodeInfo nodeObj : nodeList) {
					msg += String.format("%-30s %-30s %s", nodeObj.getIndex(), nodeObj.getIpAddress(),
							nodeObj.getName());
					msg += "\n";
				}
				OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
				content.setSummary(String.format(OBFaultCheckMsg.L47_SLEEP_FLB_NODE_CHECK.getSuccessMsg(), sleepDay,
						nodeList.size()));
				content.setDetail(msg);
				resultList.add(content);
			}
		} catch (OBException e) {
			retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_FAIL);
			OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
			content.setSummary(String.format(OBFaultCheckMsg.L47_SLEEP_FLB_NODE_CHECK.getProcessFailMsg(), sleepDay));
			content.setDetail(String.format(OBFaultCheckMsg.DB_QUERY.getFailMsg()));
			resultList.add(content);
			msgCLI += String.format(OBFaultCheckMsg.DB_QUERY.getFailMsg());
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
					String.format("failed to query db. adcIndex(%d)", this.adcInfo.getIndex()));

			retVal.setMsgCLI(msgCLI);
			retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));
		}

		// Flb 유휴 POOL 추출.
		try {
			// 데이터 추출.
			ArrayList<OBDtoSlbPoolInfo> poolList = new OBFaultMngImpl().getSleepFlbPoolList(this.adcInfo, sleepDay);
			if (poolList.size() == 0) {
				OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
				content.setSummary(String.format(OBFaultCheckMsg.L47_SLEEP_FLB_POOL_CHECK.getFailMsg(), sleepDay));
				resultList.add(content);
			} else {
				String msg = "";
				for (OBDtoSlbPoolInfo poolObj : poolList) {
					msg += String.format("%-30s %s", poolObj.getIndex(), poolObj.getName());
					msg += "\n";
				}
				OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
				content.setSummary(String.format(OBFaultCheckMsg.L47_SLEEP_FLB_POOL_CHECK.getSuccessMsg(), sleepDay,
						poolList.size()));
				content.setDetail(msg);
				resultList.add(content);
			}
		} catch (OBException e) {
			retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_FAIL);
			OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
			content.setSummary(String.format(OBFaultCheckMsg.L47_SLEEP_FLB_POOL_CHECK.getProcessFailMsg()));
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

	// private void waitUntilDumpStop()
	// {
	// // 패킷 수집 상태를 검사한다.
	// while(1)
	// {
	// try
	// {
	// // 데이터 추출.
	// boolean isOk = this.ClassTelnet.cmndPktDumpStartStatus(10000, 1000);
	// msgCLI = this.ClassTelnet.getCmndRetString();
	// if(isOk == false)
	// {// 패킷 덤프에 실패한 경우.
	// retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_FAIL);
	// OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
	// content.setDetail(OBFaultCheckMsg.SVC_DUMP_PCAP_STATUS.getProcessFailMsg());
	// content.setSummary(msgCLI);
	// resultList.add(content);
	//
	// retVal.setMsgCLI(msgCLI);
	// retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));
	//
	// OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("failed to dump
	// packets. adcIndex(%d), cli(%s)", this.adcInfo.getIndex(), msgCLI));
	// return retVal;
	// }
	// }
	// catch(OBException e)
	// {// 패킷 덤프 과정에서 exception이 발생한 경우.
	// retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_FAIL);
	// OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
	// content.setDetail(OBFaultCheckMsg.SVC_DUMP_PCAP_STATUS.getProcessFailMsg());
	// content.setSummary(OBFaultCheckMsg.INVALID_PARAMETER.getFailMsg());
	// resultList.add(content);
	//
	// retVal.setMsgCLI(msgCLI);
	// retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));
	//
	// OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("failed to get
	// telnet data. adcIndex(%d), cli(%s)", this.adcInfo.getIndex(), msgCLI));
	// return retVal;
	// }
	// }
	// }
	@Override
	public OBDtoFaultCheckResultElement startSvcPacketDump(OBDtoElement obj, Long logKey, String clientIPAddress,
			String serverIPAddress, Integer svcPort) throws OBException {
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
			//
			//
			// retVal.setObj(hashMap.get(OBDefine.FAULT_CHECK_ELEMENT_SVC_PKTDUMP));
			// retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_FAIL);
			// ArrayList<OBDtoFaultCheckResultContent> contentList = new
			// ArrayList<OBDtoFaultCheckResultContent>();
			//
			// OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
			// content.setDetail(retVal.getRetValue());
			// content.setSummary(OBFaultCheckMsg.SVC_DUMP_PCAP_STATUS.getProcessFailMsg());
			// contentList.add(content);
			//
			// retVal.setResultList(contentList);
			// retVal.setMsgCLI(retVal.getRetValue());
			//
			// OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("invalid parameter.
			// adcIndex:%d", this.adcInfo.getIndex()));
			// retVal.setRetCode(OBDtoReturnInfo.CODE_FALSE);
			// retVal.setRetValue("invalid parameter");
			// return retVal;
		}

		try {
			// 데이터 추출.
			String option = String.format("-s %d -c %d host %s", this.packetDumpLength, this.packetDumpCount,
					clientIPAddress);
			msgCLI += "PKT DUMP OPTION : " + option;

			// OBSystemLog.info(OBDefine.LOGFILE_SYSTEM, String.format("option:%s",
			// option));

			// String option = String.format("-s %d -c %d tcp and ( host %s )",
			// this.packetDumpLength, this.packetDumpCount, clientIPAddress);
			boolean isOk = this.ClassTelnet.cmndPktDumpStartOption(null, option);
			String retText = this.ClassTelnet.getCmndRetString();
			if (isOk == false) {// 패킷 덤프에 실패한 경우.
				retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_FAIL);
			} else {
				retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_SUCC);
			}

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
			//
			// String retText = this.ClassTelnet.getCmndRetString();
			// retVal.setRetCode(OBDtoReturnInfo.CODE_FALSE);
			// retVal.setRetValue(retText);
			// OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("failed to dump
			// packets. adcIndex(%d), cli(%s)", this.adcInfo.getIndex(), retText));
			// return retVal;
		} catch (Exception e) {
			retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_FAIL);

			OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
			content.setSummary(OBFaultCheckMsg.SVC_DUMP_PCAP_INFO.getProcessFailMsg());
			content.setDetail(OBException.getExceptionMessage(OBException.ERRCODE_SYSTEM_ILLEGALNULL));
			resultList.add(content);
			msgCLI += OBException.getExceptionMessage(OBException.ERRCODE_SYSTEM_ILLEGALNULL);

			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
					String.format("[%d/%d] failed to dump pkt. send starting command. msg:null pointer error",
							this.adcInfo.getIndex(), logKey));

			retVal.setMsgCLI(msgCLI);
			retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));
			return retVal;
		}
		return retVal;
	}

	@Override
	public OBDtoReturnInfo downloadDumpFile(String remoteFileName, String localFileName,
			String srvIPAddress/* String fileName, String srvIPAddress */) throws OBException {
		// remoteFileName 파라메터는 사용하지 않음.

		OBDtoReturnInfo retVal = new OBDtoReturnInfo();

		if (localFileName == null || localFileName.isEmpty() || srvIPAddress == null || srvIPAddress.isEmpty()) {
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
					String.format("invalid parameter. adcIndex:%d", this.adcInfo.getIndex()));
			retVal.setRetCode(OBDtoReturnInfo.CODE_FALSE);
			retVal.setRetValue("invalid parameter");
			return retVal;
		}

		try {
			// 데이터 추출.
			Integer isOk = this.ClassTelnet.cmndPktDumpSend(localFileName, srvIPAddress);
			String retText = this.ClassTelnet.getCmndRetString();
			if (isOk != 0) {// 패킷 덤프에 실패한 경우.
				retVal.setRetCode(OBDtoReturnInfo.CODE_FALSE);
				retVal.setRetValue(retText);
				OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format(
						"failed to download dump file. adcIndex(%d), cli(%s)", this.adcInfo.getIndex(), retText));
				return retVal;
			}
			retVal.setRetCode(OBDtoReturnInfo.CODE_TRUE);
			retVal.setRetValue(retText);

			// 28버전 이상이면 압축을 해제해야 한다.
			Integer majorVersion = getMajorVersion(this.adcInfo.getSwVersion());
			if (majorVersion.intValue() >= 28) {// tar.gz 형태를 압축 해제 한다.
				String cmndExtract = String
						.format("tar -zxvf /var/lib/adcsmart/pcap/%s.tar.gz -C /var/lib/adcsmart/pcap", localFileName);
				String cmndMove = String.format("mv /var/lib/adcsmart/pcap/pkt.pcap /var/lib/adcsmart/pcap/%s",
						localFileName);
				try {
					OBSystemLog.info(OBDefine.LOGFILE_SYSTEM, String.format("extract file:%s", cmndExtract));
					Runtime.getRuntime().exec(cmndExtract);
					OBDateTime.Sleep(500);
					OBSystemLog.info(OBDefine.LOGFILE_SYSTEM, String.format("move file:%s", cmndMove));
					Runtime.getRuntime().exec(cmndMove);
					OBDateTime.Sleep(500);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (OBException e) {// 패킷 덤프 과정에서 exception이 발생한 경우.
			String retText = this.ClassTelnet.getCmndRetString();
			retVal.setRetCode(OBDtoReturnInfo.CODE_FALSE);
			retVal.setRetValue(retText);
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String
					.format("failed to download dump file. adcIndex(%d), cli(%s)", this.adcInfo.getIndex(), retText));
			return retVal;
		}
		return retVal;
	}

	private Integer getMajorVersion(String swVersion) {
		String[] verElements = swVersion.split("\\.", 2); // 버전 요소 두개까지 문자열로 건진다. 지금은 하나만 쓴다.
		return Integer.parseInt(verElements[0]);
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
				content.setDetail(OBFaultCheckMsg.DATA_NOT_EXIST.getFailMsg());
				resultList.add(content);

				retVal.setMsgCLI(OBFaultCheckMsg.PARSE_PACKET.getSuccessMsg());
				retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));
				// retVal.setExtraInfo(pstRetInfo);
				OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
						String.format("invalid parameter. adcIndex:%d", this.adcInfo.getIndex()));
				return retVal;
			}
			OBDtoRespTimeTempInfo respResultInfo = readRespTimeInfoFromFile(szLocalTempFileName);
			deleteTempWorkFile(szLocalTempFileName);

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
				String detail = String.format(OBFaultCheckMsg.SVC_RESPONSE_TIME_INFO.getSuccessMsg(),
						respTimeInfo.getEndPointAvgTime(), respTimeInfo.getEndPointMinTime(),
						respTimeInfo.getEndPointMaxTime());
				content.setDetail(detail);
				retVal.setMsgCLI(OBFaultCheckMsg.PARSE_PACKET.getSuccessMsg());
			} else {
				retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_FAIL);
				content.setSummary(OBFaultCheckMsg.SVC_RESPONSE_TIME_STATUS.getFailMsg());
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
		// .
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
				// obj.setTimeDiff(Integer.parseInt(element[6]));
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

	// public static void main(String[] args)
	// {
	// try
	// {
	// OBDtoElement object = new OBDtoElement();
	// object.setCategory(4);
	// object.setIndex(4);//[category=4, index=4, name=패킷유실 분석, description=패킷의 유실
	// 여부를 분석합니다., target=15, state=0],
	// //logKey:1387847074360,
	// //fileName:/var/lib/adcsmart/pcap/1387847074360.pcap,
	// //clientIPAddress:172.172.2.209, vsIPAddress:192.168.100.233,
	// realIPAddress:[192.168.199.41, 192.168.199.42, 192.168.199.43]
	// //
	// ArrayList<String>realIPAddress = new ArrayList<String>();
	// realIPAddress.add("192.168.199.41");
	// realIPAddress.add("192.168.199.42");
	// realIPAddress.add("192.168.199.43");
	// OBDtoFaultCheckThreadInfo threadInfo = new OBDtoFaultCheckThreadInfo();
	// threadInfo.setAccntIndex(0);
	// threadInfo.setClientIP("172.172.2.206");
	// threadInfo.setCheckKey(1387542674757L);
	// OBDtoFaultCheckResultElement worker = new OBFaultCheckHandlerAlteon(1, "",
	// null).checkSvcPacketLoss(
	// null, 1387843678081L, "/var/lib/adcsmart/pcap/1387843678081.pcap",
	// "172.172.2.209", "192.168.100.233", realIPAddress);
	// System.out.println(worker);
	// }
	// catch(Exception e)
	// {
	// e.printStackTrace();
	// }
	// }

	private String addExtraSpace(Integer num) {
		String retVal = "";
		for (int i = 0; i <= num; i++) {
			retVal += "|||;";
		}
		return retVal;
	}

	// private Integer isValidPacket(OBDtoFaultCheckPacketLossInfo prevInfo,
	// OBDtoFaultCheckPacketLossInfo currInfo)
	// {// 패킷이 정상적인 검사한다. 0: not defined, 1: valid, 2: invalid
	// Integer retVal = 0;
	//
	// if(prevInfo==null)
	// return retVal;
	//
	// if(currInfo.getDirection()==OBDtoFaultCheckPacketLossInfo.DIR_CLNT_VIP)
	// return 1;
	//
	// // 이전값과 방향이 같으면 0 리턴
	// if(currInfo.getDirection()==prevInfo.getDirection())
	// return 0;
	//
	// switch(currInfo.getDirection())
	// {
	// case OBDtoFaultCheckPacketLossInfo.DIR_CLNT_VIP:// 요청사항. 요청은 초기 상태로 간주함.
	// return 0;
	// case OBDtoFaultCheckPacketLossInfo.DIR_CLNT_REAL:
	// return 2;// client에서 real로 들어가는 상황. 비 정상으로 간
	// case OBDtoFaultCheckPacketLossInfo.DIR_VIP_CLNT:
	// if(prevInfo.getDirection()==currInfo.getDirection())
	// return 0;
	// if(prevInfo.getDirection()==OBDtoFaultCheckPacketLossInfo.DIR_REAL_VIP)
	// return 1;
	// return 2;
	// case OBDtoFaultCheckPacketLossInfo.DIR_REAL_CLNT:
	// return 2;// real 에서 client로 들어가는 상황. 비 정상으로 간주.
	// case OBDtoFaultCheckPacketLossInfo.DIR_VIP_REAL:
	// if(prevInfo.getDirection()==currInfo.getDirection())
	// return 0;
	// if(prevInfo.getDirection()==OBDtoFaultCheckPacketLossInfo.DIR_CLNT_VIP)
	// return 1;
	// return 2;
	// case OBDtoFaultCheckPacketLossInfo.DIR_REAL_VIP:
	// if(prevInfo.getDirection()==currInfo.getDirection())
	// return 0;
	// if(prevInfo.getDirection()==OBDtoFaultCheckPacketLossInfo.DIR_VIP_REAL)
	// return 1;
	// return 2;
	// default:
	// return 0;
	// }
	// }

	final static private Integer MAX_DIV_COUNT = 3;

	private Integer calcArcskipNum(Integer timeDiff, OBDtoMinMaxAvgInfo minMaxAvgInfo) {
		if (timeDiff == 0)
			return 0;
		Integer retVal = 0;
		Integer base = (minMaxAvgInfo.getMax() - minMaxAvgInfo.getMin()) / MAX_DIV_COUNT;

		retVal = (timeDiff - minMaxAvgInfo.getMin()) / base;
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

	private String makePacketLossInfoImgFileAlteon(Long logKey, String clientIP, String vsvcIPAddress,
			ArrayList<OBDtoFaultCheckPacketLossInfo> pktInfoList) throws OBException {// 문법은
																						// http://www.mcternan.co.uk/mscgen/
																						// 참조하기 바람.
		String pngFileName = logKey + ".png";
		// String pngFullPathName =
		// "/opt/apache-tomcat/webapps/adcms/imgs/"+logKey+".png";
		String pngFullPathName = OBDefine.PKT_DUMP_FILE_PATH + logKey + ".png";
		String infFileName = OBDefine.PKT_DUMP_FILE_PATH + logKey + ".inf";
		String retVal = pngFileName;

		try {
			String data = "";
			data += "# MSC for some fictional process\n";
			data += "msc {\n";
			data += "arcgradient = 4;\n";
			// data += "width = 800;";
			data += "\n";
			data += String.format("a [label=\"%s\"],b [label=\"%s\"], c [label=\"%s\"];\n", clientIP, vsvcIPAddress,
					"REAL HOST");
			data += "\n";

			String szDir;
			// OBDtoFaultCheckPacketLossInfo prevInfo = null;
			String redColor = "#ff0000";
			String blackColor = "#000000";

			OBDtoMinMaxAvgInfo minMaxAvgInfo = getMinMaxAvgDiffTime(pktInfoList);

			// float elapseTime = 0;
			String color = "#ff0000";// black:#000000, green:#00ff00, red:#ff0000
			Integer arcskipCount = 0;
			// OBDtoFaultCheckPacketLossInfo prevInfo = null;
			for (OBDtoFaultCheckPacketLossInfo pktInfo : pktInfoList) {
				Integer tmpArcskipNum = calcArcskipNum(pktInfo.getTimeDiff(), minMaxAvgInfo);
				data += addExtraSpace(arcskipCount);
				color = blackColor;
				// if(isValidPacket(prevInfo, pktInfo)==2)
				// color = redColor;
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

				// elapseTime = pktInfo.getTimeDiff();
				data += String.format(
						"%s [label=\"%s(Length:%d)\\nTime:%4.3f msec\", linecolour=\"%s\", arcskip=\"%d\"];\n", szDir,
						pktInfo.getSummary(), pktInfo.getDataLength(), pktInfo.getTimeDiff() / 1000f, color,
						tmpArcskipNum);
				arcskipCount = tmpArcskipNum;
				// prevInfo = pktInfo;
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

			// OBDateTime.Sleep(500);
			// file 생성 검사..
			Long fileSize = OBUtility.fileLength(pngFullPathName);
			if (fileSize == 0L)
				retVal = "";
			// String targetFile = "/opt/apache-tomcat/webapps/adcms/imgs/"+pngFileName;
			// cmnd = String.format("/bin/cp %s %s", pngFullPathName, targetFile);
			// Runtime.getRuntime().exec(cmnd);
		}
		// catch (RuntimeException e)
		// {
		// throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL,
		// "RuntimeException: " + e.getMessage());//
		// }
		catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, "Exception: " + e.getMessage());//
		}
		return retVal;
	}

//    public static void main(String[] args)
//    {
//        try
//        {
//            OBDtoElement object = new OBDtoElement();
//            object.setCategory(4);
//            object.setIndex(4);// [category=4, index=4, name=패킷유실 분석, description=패킷의 유실 여부를 분석합니다., target=15, state=0],
//            // logKey:1387847074360,
//            // fileName:/var/lib/adcsmart/pcap/1387847074360.pcap,
//            // clientIPAddress:172.172.2.209, vsIPAddress:192.168.100.233, realIPAddress:[192.168.199.41, 192.168.199.42, 192.168.199.43]
//            //
//            ArrayList<String> realIPAddress = new ArrayList<String>();
//            realIPAddress.add("192.168.199.41");
//            realIPAddress.add("192.168.199.42");
//            realIPAddress.add("192.168.199.43");
//            OBDtoFaultCheckThreadInfo threadInfo = new OBDtoFaultCheckThreadInfo();
//            threadInfo.setAccntIndex(0);
//            threadInfo.setClientIP("172.172.2.206");
//            threadInfo.setCheckKey(1387542674757L);
//            OBDtoFaultCheckResultElement worker = new OBFaultCheckHandlerAlteon(1, "").checkSvcPacketLoss(null, 1387843678081L, "/var/lib/adcsmart/pcap/1387843678081.pcap", "172.172.2.209", "192.168.100.233", realIPAddress);
//            // null, 1387843678081L, "/var/lib/adcsmart/pcap/pkt.pcap", "172.172.2.209", "192.168.100.233", realIPAddress);
//            System.out.println(worker);
//        }
//        catch(Exception e)
//        {
//            e.printStackTrace();
//        }
//    }

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

			OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
			if (readInfoList.size() > 0) {
				OBDtoPktLossInfo pktLossInfo = new OBAnalPktLossAlteon().analyzePacketLoss(clientIPAddress, vsIPAddress,
						realIPAddress, readInfoList);
				if (pktLossInfo.getPktInfoList() == null || pktLossInfo.getPktInfoList().size() <= 0) {
					retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_FAIL);
					content.setSummary(OBFaultCheckMsg.SVC_PACKET_LOSS_CHECK.getProcessFailMsg());
					String detail = OBFaultCheckMsg.PACKET_NO_DATA.getFailMsg();
					msgCLI += detail;
					content.setDetail(detail);
					retVal.setMsgCLI(OBFaultCheckMsg.PACKET_NO_DATA.getFailMsg());

					resultList.add(content);
				} else {
					String pngFileName = makePacketLossInfoImgFileAlteon(logKey, clientIPAddress, vsIPAddress,
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
				retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_FAIL);
				content.setSummary(OBFaultCheckMsg.SVC_PACKET_LOSS_CHECK.getProcessFailMsg());
				String detail = OBFaultCheckMsg.PACKET_NO_DATA.getFailMsg();
				msgCLI += detail;
				content.setDetail(detail);
				retVal.setMsgCLI(OBFaultCheckMsg.PACKET_NO_DATA.getFailMsg());

				resultList.add(content);
			}

			// retVal.setExtraInfo(readInfoList);

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
	public boolean stopSvcPacketDump() throws OBException {
		boolean retVal = true;
		// String msgCLI = "";

		try {
			// 패킷 덤프 종료.
			boolean isOk = this.ClassTelnet.cmndPktDumpStop();
			// msgCLI = this.ClassTelnet.getCmndRetString();
			if (isOk == false) {// 패킷 덤프에 실패한 경우.
								// retVal.setRetCode(OBDtoReturnInfo.CODE_FALSE);
								// retVal.setRetValue(msgCLI);
								// OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("failed to stop dump
								// packets. adcIndex(%d), cli(%s)", this.adcInfo.getIndex(), msgCLI));
								// return retVal;
				retVal = false;
			}
		} catch (OBException e) {
			// retVal.setRetCode(OBDtoReturnInfo.CODE_FALSE);
			// retVal.setRetValue(msgCLI);
			// OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("failed to stop dump
			// packets. adcIndex(%d), cli(%s)", this.adcInfo.getIndex(), msgCLI));
			retVal = false;
		}

		// // 패킷 분석.
		// try
		// {
		// OBDtoPktDumpInfo pktDumpInfo = new
		// OBCLIParserAlteon().parsePktDumpInfo(msgCLI);
		// if(pktDumpInfo == null)
		// {
		// retVal.setRetCode(OBDtoReturnInfo.CODE_FALSE);
		// retVal.setRetValue(msgCLI);
		// }
		// else
		// {
		// retVal.setDetailInfo(pktDumpInfo);
		// retVal.setRetCode(OBDtoReturnInfo.CODE_TRUE);
		// retVal.setRetValue(msgCLI);
		// }
		// }
		// catch (OBException e)
		// {
		// retVal.setRetCode(OBDtoReturnInfo.CODE_FALSE);
		// retVal.setRetValue(msgCLI);
		// return retVal;
		// }

		return retVal;
	}

	@Override
	public OBDtoReturnInfo isSvcPacketDumpProgress(String dumpFileName) throws OBException {
		OBDtoReturnInfo retVal = new OBDtoReturnInfo();

		String msgCLI = "";

		try {
			// 패킷 덤프 종료.
			boolean isOk = this.ClassTelnet.cmndPktDumpStartStatus(this.packetDumpStart, this.packetDumpCount);
			msgCLI = this.ClassTelnet.getCmndRetString();
			if (isOk == false) {// 패킷 덤프 상태 조회에 실패. 진행중으로 간주한다.
				retVal.setRetCode(OBDtoReturnInfo.CODE_TRUE);
				retVal.setRetValue(msgCLI);
				OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String
						.format("failed to stop dump packets. adcIndex(%d), cli(%s)", this.adcInfo.getIndex(), msgCLI));
				return retVal;
			}
		} catch (OBException e) {
			retVal.setRetCode(OBDtoReturnInfo.CODE_TRUE);
			retVal.setRetValue(msgCLI);
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String
					.format("failed to stop dump packets. adcIndex(%d), cli(%s)", this.adcInfo.getIndex(), msgCLI));
			return retVal;
		}

		// 패킷 분석.
		try {
			OBDtoPktDumpInfo pktDumpInfo = OBCommon.getValidAlteonCLIParser(adcInfo.getSwVersion())
					.parsePktDumpInfo(msgCLI);
			if (pktDumpInfo == null) {
				retVal.setRetCode(OBDtoReturnInfo.CODE_TRUE);
				retVal.setRetValue(msgCLI);
			} else {
				retVal.setDetailInfo(pktDumpInfo);
				retVal.setRetCode(OBDtoReturnInfo.CODE_FALSE);
				retVal.setRetValue(msgCLI);
			}
		} catch (OBException e) {
			retVal.setRetCode(OBDtoReturnInfo.CODE_FALSE);
			retVal.setRetValue(msgCLI);
			return retVal;
		}

		return retVal;
	}

	@Override
	public boolean isSvcPacketDumpSizeExceed(String dumpFileName, Long baseSize) throws OBException {// alteon은 해당 기능이
																										// 없다. 무조건
																										// false.
		return false;
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

		try {
			// 패킷 덤프 종료.
			boolean isOk = this.ClassTelnet.cmndPktDumpStartStatus(this.packetDumpStart, this.packetDumpCount);
			msgCLI = this.ClassTelnet.getCmndRetString();
			if (isOk == false) {// 패킷 덤프 상태 조회에 실패. 진행중으로 간주한다.
				retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_FAIL);

				OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
				content.setSummary(OBFaultCheckMsg.SVC_DUMP_PCAP_CHECK.getProcessFailMsg());
				content.setDetail(msgCLI);
				resultList.add(content);

				retVal.setMsgCLI(msgCLI);
				retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));
				retVal.setExtraInfo(null);
				OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format(
						"[%d/%d] failed to check pkt dump status. msg:%s", this.adcInfo.getIndex(), logKey, msgCLI));
				return retVal;
			}
		} catch (OBException e) {
			retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_FAIL);

			OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
			content.setSummary(OBFaultCheckMsg.SVC_DUMP_PCAP_CHECK.getProcessFailMsg());
			msgCLI = e.getErrorMessage();
			content.setDetail(msgCLI);
			resultList.add(content);

			retVal.setMsgCLI(msgCLI);
			retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));
			retVal.setExtraInfo(null);
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("[%d/%d] failed to check pkt dump status. msg:%s",
					this.adcInfo.getIndex(), logKey, e.getErrorMessage()));
			return retVal;
		}

		// 패킷 분석.
		try {
			OBDtoPktDumpInfo pktDumpInfo = OBCommon.getValidAlteonCLIParser(adcInfo.getSwVersion())
					.parsePktDumpInfo(msgCLI);
			if (pktDumpInfo == null) {
				retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_FAIL);

				OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
				content.setSummary(OBFaultCheckMsg.SVC_DUMP_PCAP_CHECK.getProcessFailMsg());
				String detailMsg = OBFaultCheckMsg.PARSE_DATA.getProcessFailMsg();
				detailMsg += "\n";
				detailMsg += msgCLI;
				content.setDetail(detailMsg);
				resultList.add(content);

				retVal.setMsgCLI(detailMsg);
				retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));
				retVal.setExtraInfo(null);
				OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format(
						"[%d/%d] failed to check pkt dump status. msg:%s", this.adcInfo.getIndex(), logKey, detailMsg));
				return retVal;
			}

			// drop 패킷이 있으면 실패한 것으로 간주한다.
			if (pktDumpInfo.getDropCoun() > 0) {
				retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_FAIL);

				OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
				content.setSummary(OBFaultCheckMsg.SVC_DUMP_PCAP_INFO.getFailMsg());
				String detailMsg = OBFaultCheckMsg.DROP_PKT_EXIST.getFailMsg();
				detailMsg += "\n";
				detailMsg += msgCLI;
				content.setDetail(detailMsg);
				resultList.add(content);

				retVal.setMsgCLI(detailMsg);
				retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));
				retVal.setExtraInfo(null);
				OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format(
						"[%d/%d] failed to check pkt dump status. msg:%s", this.adcInfo.getIndex(), logKey, detailMsg));
				return retVal;
			}

			// 패킷이 없으면 실패한 것으로 간주한다.
			if (pktDumpInfo.getCaptureCount() == 0) {
				retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_FAIL);

				OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
				content.setSummary(OBFaultCheckMsg.SVC_DUMP_PCAP_INFO.getFailMsg());
				String detailMsg = OBFaultCheckMsg.DATA_NOT_EXIST.getFailMsg();
				detailMsg += "\n";
				detailMsg += msgCLI;
				content.setDetail(detailMsg);
				resultList.add(content);

				retVal.setMsgCLI(detailMsg);
				retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));
				retVal.setExtraInfo(null);
				OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format(
						"[%d/%d] failed to check pkt dump status. msg:%s", this.adcInfo.getIndex(), logKey, detailMsg));
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
		} catch (OBException e) {
			retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_FAIL);

			OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
			content.setSummary(OBFaultCheckMsg.SVC_DUMP_PCAP_CHECK.getProcessFailMsg());
			msgCLI = e.getErrorMessage();
			content.setDetail(msgCLI);
			resultList.add(content);

			retVal.setMsgCLI(msgCLI);
			retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));
			retVal.setExtraInfo(null);
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("[%d/%d] failed to check pkt dump status. msg:%s",
					this.adcInfo.getIndex(), logKey, e.getErrorMessage()));
			return retVal;
		}

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

		String normMsg = "";
		String msgCLI = "";
		String rawMsg = "";
		// /stats/slb/maint 검사
		try {
			// 데이터 추출.
			normMsg = this.ClassTelnet.cmndStatSlbMaint();
			rawMsg = this.ClassTelnet.getCmndRetString();
			msgCLI = rawMsg;
		} catch (OBException e) {
			retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_FAIL);
			OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
			content.setSummary(OBFaultCheckMsg.L47_ALLOCATION_FAILURE_CHECK.getProcessFailMsg());
			content.setDetail(String.format(OBFaultCheckMsg.TELNET_QUERY.getFailMsg(), rawMsg));
			resultList.add(content);
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
					String.format("failed to get telnet data. adcIndex(%d), cli(%s)", this.adcInfo.getIndex(), msgCLI));

			retVal.setMsgCLI(msgCLI);
			retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));
			return retVal;
		}

		retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_SUCC);
		// 데이터 분석.
		try {
			OBDtoStatsSlbMaint maintInfo = OBCommon.getValidAlteonCLIParser(adcInfo.getSwVersion())
					.parseStatSlbMaint(normMsg);// normalized 데이터로 파싱을 실시한다.

			// allocation failure 검사.
			if (maintInfo.getAllocFails() != 0) {
				retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_FAIL);

				OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
				content.setSummary(String.format(OBFaultCheckMsg.L47_ALLOCATION_FAILURE_CHECK.getFailMsg(),
						maintInfo.getAllocFails()));
				content.setDetail(rawMsg);// raw 데이터를 제공한다.
				resultList.add(content);
			} else {
				OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
				content.setSummary(OBFaultCheckMsg.L47_ALLOCATION_FAILURE_CHECK.getSuccessMsg());
				content.setDetail(rawMsg);// raw 데이터를 제공한다.
				resultList.add(content);
			}

			// vip pkt drop 검사. (vip pkt drop 은 수집하지않습니다. 2014.04.04 yh.Yang)
			/*
			 * if(maintInfo.getVipPktDrops()!=0) {
			 * retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_FAIL);
			 * 
			 * OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
			 * content.setSummary(String.format(OBFaultCheckMsg.L47_VIP_PKTS_DROP_CHECK.
			 * getFailMsg(), maintInfo.getVipPktDrops())); content.setDetail(rawMsg);// raw
			 * 데이터를 제공한다. resultList.add(content); } else { OBDtoFaultCheckResultContent
			 * content = new OBDtoFaultCheckResultContent();
			 * content.setSummary(OBFaultCheckMsg.L47_VIP_PKTS_DROP_CHECK.getSuccessMsg());
			 * content.setDetail(rawMsg);// raw 데이터를 제공한다. resultList.add(content); }
			 */
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

		// /stats/slb/aux
		try {
			// 데이터 추출.
			normMsg = this.ClassTelnet.cmndStatSlbAux();
			rawMsg = this.ClassTelnet.getCmndRetString();
			msgCLI += "\n";
			msgCLI += rawMsg;
		} catch (OBException e) {
			retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_FAIL);
			OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
			content.setSummary(OBFaultCheckMsg.L47_AUX_TABLE_CHECK.getProcessFailMsg());
			content.setDetail(String.format(OBFaultCheckMsg.TELNET_QUERY.getFailMsg(), rawMsg));
			resultList.add(content);
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
					String.format("failed to get telnet data. adcIndex(%d), cli(%s)", this.adcInfo.getIndex(), msgCLI));

			retVal.setMsgCLI(msgCLI);
			retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));
			return retVal;
		}

		// 데이터 분석.
		try {
			ArrayList<OBDtoStatsSlbAuxTable> auxTableList = OBCommon.getValidAlteonCLIParser(adcInfo.getSwVersion())
					.parseStatSlbAux(normMsg);// normalized 데이터로 파싱을 실시한다.

			// check allocation failure in aux table.
			Long fails = 0L;
			for (OBDtoStatsSlbAuxTable info : auxTableList) {
				if (info.getAllocFails() > 0)
					fails += info.getAllocFails();
			}

			if (fails != 0) {
				retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_FAIL);

				OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
				content.setSummary(String.format(OBFaultCheckMsg.L47_AUX_TABLE_CHECK.getFailMsg(), fails));
				content.setDetail(rawMsg);// raw 데이터를 제공한다.
				resultList.add(content);
			} else {
				OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
				content.setSummary(OBFaultCheckMsg.L47_AUX_TABLE_CHECK.getSuccessMsg());
				content.setDetail(rawMsg);// raw 데이터를 제공한다.
				resultList.add(content);
			}
		} catch (OBException e) {
			retVal.setStatus(OBDtoFaultCheckResultElement.STATUS_FAIL);

			OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
			content.setSummary(OBFaultCheckMsg.L47_AUX_TABLE_CHECK.getProcessFailMsg());
			content.setDetail(OBFaultCheckMsg.PARSE_DATA.getFailMsg());
			resultList.add(content);

			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
					String.format("failed to parse data. adcIndex(%d), cli(%s)", this.adcInfo.getIndex(), msgCLI));

			retVal.setMsgCLI(msgCLI);
			retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));
			return retVal;
		}

		retVal.setMsgCLI(msgCLI);
		retVal.setEndTime(OBDateTime.toTimestamp(OBDateTime.now()));
		retVal.setExtraInfo(null);

		return retVal;
	}

	@Override
	public OBDtoFaultCheckResultElement checkL47UnsedFLB(OBDtoElement obj) throws OBException {
		// TODO Auto-generated method stub
		return null;
	}
}
