package kr.openbase.adcsmart.service.impl.fault;

import java.util.ArrayList;
import java.util.HashMap;

import kr.openbase.adcsmart.service.database.OBDatabase;
import kr.openbase.adcsmart.service.dto.OBDtoAdcInfo;
import kr.openbase.adcsmart.service.dto.OBDtoElement;
import kr.openbase.adcsmart.service.dto.OBDtoReturnInfo;
import kr.openbase.adcsmart.service.dto.OBDtoSystemEnvNetwork;
import kr.openbase.adcsmart.service.dto.fault.OBDtoFaultCheckLog;
import kr.openbase.adcsmart.service.dto.fault.OBDtoFaultCheckResultContent;
import kr.openbase.adcsmart.service.dto.fault.OBDtoFaultCheckResultElement;
import kr.openbase.adcsmart.service.dto.fault.OBDtoFaultCheckStatus;
import kr.openbase.adcsmart.service.dto.fault.OBDtoFaultCheckTemplate;
import kr.openbase.adcsmart.service.impl.OBAdcManagementImpl;
import kr.openbase.adcsmart.service.impl.OBEnvManagementImpl;
import kr.openbase.adcsmart.service.impl.OBSystemAuditImpl;
import kr.openbase.adcsmart.service.impl.dto.OBHostInfo;
import kr.openbase.adcsmart.service.impl.fault.alteon.OBFaultCheckHandlerAlteon;
import kr.openbase.adcsmart.service.impl.fault.dto.OBDtoFaultCheckThreadInfo;
import kr.openbase.adcsmart.service.impl.fault.dto.OBDtoRespTimeAnalInfo;
import kr.openbase.adcsmart.service.impl.fault.f5.OBFaultCheckHandlerF5;
import kr.openbase.adcsmart.service.utility.OBDateTime;
import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.service.utility.OBFaultCheckMsg;
import kr.openbase.adcsmart.service.utility.OBParser;
import kr.openbase.adcsmart.service.utility.OBSystemLog;

public class OBFaultCheckWorker implements Runnable {
	private OBDtoFaultCheckThreadInfo threadInfo;
	private Integer hwCheckTotal = 0;
	private Integer hwCheckCount = 0;
	private Integer hwCheckFail = 0;
	private Integer packetDumpMaxWaitMsec = 60000;// 60sec
	private Long packetDumpMaxSize = 10000000L;// 10M
	private Integer systemFanMinRpm = 3500;
	private Integer systemFanMaxRpm = 15000;

	public OBFaultCheckWorker(OBDtoFaultCheckThreadInfo threadInfo) {
		setThreadInfo(threadInfo);
		// 환경 변수를 읽어 들인다.
		try {
			String propertyValue = OBParser.propertyToString(OBDefine.PROPERTY_FILE_NAME,
					OBDefine.PROPERTY_KEY_PKT_MAX_WAIT_MSEC);
			if (propertyValue != null && !propertyValue.isEmpty())
				this.packetDumpMaxWaitMsec = Integer.parseInt(propertyValue);
			propertyValue = OBParser.propertyToString(OBDefine.PROPERTY_FILE_NAME,
					OBDefine.PROPERTY_KEY_SYSTEM_FAN_MIN_RPM);
			if (propertyValue != null && !propertyValue.isEmpty())
				this.systemFanMinRpm = Integer.parseInt(propertyValue);
			propertyValue = OBParser.propertyToString(OBDefine.PROPERTY_FILE_NAME,
					OBDefine.PROPERTY_KEY_SYSTEM_FAN_MAX_RPM);
			if (propertyValue != null && !propertyValue.isEmpty())
				this.systemFanMaxRpm = Integer.parseInt(propertyValue);
//			ResourceBundle resource = ResourceBundle.getBundle("conf.adcsmart");//
//			propertyValue = resource.getString(OBDefine.PROPERTY_KEY_SYSTEM_FAN_MAX_RPM);
			propertyValue = OBParser.propertyToString(OBDefine.PROPERTY_FILE_NAME, OBDefine.PROPERTY_KEY_PKT_MAX_SIZE);
			if (propertyValue != null && !propertyValue.isEmpty())
				this.packetDumpMaxSize = Long.parseLong(propertyValue);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setThreadInfo(OBDtoFaultCheckThreadInfo threadInfo) {
		this.threadInfo = threadInfo;
	}

	public OBDtoFaultCheckThreadInfo getThreadInfo() {
		return threadInfo;
	}

	private HashMap<Integer, OBDtoElement> convertElementListToMap(ArrayList<OBDtoElement> list) throws OBException {
		HashMap<Integer, OBDtoElement> retVal = new HashMap<Integer, OBDtoElement>();

		for (OBDtoElement obj : list) {
			retVal.put(obj.getIndex(), obj);
		}
		return retVal;
	}

	private OBFaultCheckHandler getFaultCheckHandler(OBDtoAdcInfo adcInfo) throws OBException {
		OBFaultCheckHandler retVal = null;
		if (adcInfo.getAdcType() == OBDefine.ADC_TYPE_ALTEON) {
			retVal = new OBFaultCheckHandlerAlteon(adcInfo.getAdcType(), adcInfo.getSwVersion());
		} else if (adcInfo.getAdcType() == OBDefine.ADC_TYPE_F5) {
			retVal = new OBFaultCheckHandlerF5(adcInfo.getAdcType(), adcInfo.getSwVersion());
		} else {
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not suppported adc vendor");
		}
		return retVal;
	}

	/**
	 * HW 장애 감지.
	 * 
	 * @param logKey
	 * @param logInfo
	 * @param db
	 * @throws OBException
	 */
	private ArrayList<OBDtoFaultCheckResultElement> processFaultCheckHW(long logKey, OBDtoFaultCheckLog logInfo,
			OBDtoAdcInfo adcInfo, OBDtoFaultCheckTemplate templateInfo, long startTime, Object CLIObj,
			float progressWeight) throws OBException {
		try {
			OBDtoElement element = null;
			OBDtoFaultCheckResultElement checkResult = null;
			HashMap<Integer, OBDtoElement> hashMap = convertElementListToMap(templateInfo.getHwCheckItems());
			ArrayList<OBDtoFaultCheckResultElement> resultList = new ArrayList<OBDtoFaultCheckResultElement>();

			OBFaultCheckHandler ClassFaultCheckHW = null;// new OBFaultCheckHW(adcInfo.getAdcType(),
															// adcInfo.getSwVersion());
			ClassFaultCheckHW = getFaultCheckHandler(adcInfo);
			ClassFaultCheckHW.setParameter(logKey, logInfo, adcInfo, CLIObj);
			ClassFaultCheckHW.loadSnmpOidHWInfo();

			float progressRate = 0f;

			if (new OBFaultMngImpl().isFaultCheckCanceled(logKey) == true) {// 작업이 취소된 경우 처리.
				OBSystemLog.info(OBDefine.LOGFILE_SYSTEM, String.format("[%d/%d] hw  fault check was canceled.led.",
						this.threadInfo.getAdcIndex(), this.getThreadInfo().getCheckKey()));
				return resultList;
			}

			// power supply 검사
			element = hashMap.get(OBDefine.FAULT_CHECK_ELEMENT_HW_POWER);
			if (element != null && element.getState() == OBDefine.STATE_ENABLE) {
				this.hwCheckCount++;
				String curCheckMsg = String.format("%s(%d/%d)", OBFaultCheckMsg.HW_POWERSUPPLY_CHECK.getItemMsg(),
						this.hwCheckCount, this.hwCheckTotal);

				new OBFaultMngImpl().updateDiagnosisStatus(logKey, null, null, null, null, null, null, curCheckMsg,
						OBFaultCheckMsg.HW_POWERSUPPLY_CHECK.getActionMsg());

				// 장비의 상태 점검.
				checkResult = ClassFaultCheckHW.checkHWPowerSupply(element);
				if (checkResult.getStatus() == OBDtoFaultCheckResultElement.STATUS_FAIL)
					this.hwCheckFail++;

				// 결과 목록에 저장.
				resultList.add(checkResult);

				progressRate += calProgressRate(OBDefine.ELEMENT_TYPE_HW, OBDefine.FAULT_CHECK_ELEMENT_HW_POWER,
						progressWeight);
				new OBFaultMngImpl().updateDiagnosisStatus(logKey, (int) progressRate, null, this.hwCheckCount,
						this.hwCheckFail, null, null, null, checkResult.getMsgCLI());

				OBDateTime.Sleep(1000);// 장비의 부하를 줄이기 위해서 1초간 정지한다.
			}

			if (new OBFaultMngImpl().isFaultCheckCanceled(logKey) == true) {// 작업이 취소된 경우 처리.
				OBSystemLog.info(OBDefine.LOGFILE_SYSTEM, String.format("[%d/%d] fault check was canceled.",
						this.threadInfo.getAdcIndex(), this.getThreadInfo().getCheckKey()));
				return resultList;
			}

			// uptime 검사.
			element = hashMap.get(OBDefine.FAULT_CHECK_ELEMENT_HW_UPTIME);
			if (element != null && element.getState() == OBDefine.STATE_ENABLE) {
				this.hwCheckCount++;
				String curCheckMsg = String.format("%s(%d/%d)", OBFaultCheckMsg.HW_UPTIME_CHECK.getItemMsg(),
						this.hwCheckCount, this.hwCheckTotal);
				new OBFaultMngImpl().updateDiagnosisStatus(logKey, null, null, null, null, null, null, curCheckMsg,
						OBFaultCheckMsg.HW_UPTIME_CHECK.getActionMsg());

				// 장비의 상태 점검.
				checkResult = ClassFaultCheckHW.checkHWUptime(element);
				if (checkResult.getStatus() == OBDtoFaultCheckResultElement.STATUS_FAIL)
					this.hwCheckFail++;

				// 결과 목록에 저장.
				resultList.add(checkResult);

				// 현재 진행 상태 업데이트.
				progressRate += calProgressRate(OBDefine.ELEMENT_TYPE_HW, OBDefine.FAULT_CHECK_ELEMENT_HW_UPTIME,
						progressWeight);
				new OBFaultMngImpl().updateDiagnosisStatus(logKey, (int) progressRate, null, this.hwCheckCount,
						this.hwCheckFail, null, null, null, checkResult.getMsgCLI());

				OBDateTime.Sleep(1000);// 장비의 부하를 줄이기 위해서 1초간 정지한다.
			}

			if (new OBFaultMngImpl().isFaultCheckCanceled(logKey) == true) {// 작업이 취소된 경우 처리.
				OBSystemLog.info(OBDefine.LOGFILE_SYSTEM, String.format("[%d/%d] fault check was canceled.",
						this.threadInfo.getAdcIndex(), this.getThreadInfo().getCheckKey()));
				return resultList;
			}

			// license 검사
			element = hashMap.get(OBDefine.FAULT_CHECK_ELEMENT_HW_LICENSE);
			if (element != null && element.getState() == OBDefine.STATE_ENABLE) {
				this.hwCheckCount++;
				String curCheckMsg = String.format("%s(%d/%d)", OBFaultCheckMsg.HW_LICENSE_CHECK.getItemMsg(),
						this.hwCheckCount, this.hwCheckTotal);
				new OBFaultMngImpl().updateDiagnosisStatus(logKey, null, null, null, null, null, null, curCheckMsg,
						OBFaultCheckMsg.HW_LICENSE_CHECK.getActionMsg());

				// 장비의 상태 점검.
				checkResult = ClassFaultCheckHW.checkHWLicense(element);
				if (checkResult.getStatus() == OBDtoFaultCheckResultElement.STATUS_FAIL)
					this.hwCheckFail++;

				// 결과 목록에 저장.
				resultList.add(checkResult);

				// 현재 진행 상태 업데이트.
				progressRate += calProgressRate(OBDefine.ELEMENT_TYPE_HW, OBDefine.FAULT_CHECK_ELEMENT_HW_LICENSE,
						progressWeight);
				new OBFaultMngImpl().updateDiagnosisStatus(logKey, (int) progressRate, null, this.hwCheckCount,
						this.hwCheckFail, null, null, null, checkResult.getMsgCLI());

				OBDateTime.Sleep(1000);// 장비의 부하를 줄이기 위해서 1초간 정지한다.
			}

			if (new OBFaultMngImpl().isFaultCheckCanceled(logKey) == true) {// 작업이 취소된 경우 처리.
				OBSystemLog.info(OBDefine.LOGFILE_SYSTEM, String.format("[%d/%d] fault check was canceled.",
						this.threadInfo.getAdcIndex(), this.getThreadInfo().getCheckKey()));
				return resultList;
			}

			// interface 검사
			element = hashMap.get(OBDefine.FAULT_CHECK_ELEMENT_HW_INTERFACE);
			if (element != null && element.getState() == OBDefine.STATE_ENABLE) {
				this.hwCheckCount++;
				String curCheckMsg = String.format("%s(%d/%d)", OBFaultCheckMsg.HW_PORT_CHECK.getItemMsg(),
						this.hwCheckCount, this.hwCheckTotal);
				new OBFaultMngImpl().updateDiagnosisStatus(logKey, null, null, null, null, null, null, curCheckMsg,
						OBFaultCheckMsg.HW_PORT_CHECK.getActionMsg());

				// 장비의 상태 점검.
				checkResult = ClassFaultCheckHW.checkHWPortInterface(element, adcInfo.getIndex());
				if (checkResult.getStatus() == OBDtoFaultCheckResultElement.STATUS_FAIL)
					this.hwCheckFail++;

				// 결과 목록에 저장.
				resultList.add(checkResult);

				// 현재 진행 상태 업데이트.
				progressRate += calProgressRate(OBDefine.ELEMENT_TYPE_HW, OBDefine.FAULT_CHECK_ELEMENT_HW_INTERFACE,
						progressWeight);
				new OBFaultMngImpl().updateDiagnosisStatus(logKey, (int) progressRate, null, this.hwCheckCount,
						this.hwCheckFail, null, null, null, checkResult.getMsgCLI());

				OBDateTime.Sleep(1000);// 장비의 부하를 줄이기 위해서 1초간 정지한다.
			}

			if (new OBFaultMngImpl().isFaultCheckCanceled(logKey) == true) {// 작업이 취소된 경우 처리.
				OBSystemLog.info(OBDefine.LOGFILE_SYSTEM, String.format("[%d/%d] fault check was canceled.",
						this.threadInfo.getAdcIndex(), this.getThreadInfo().getCheckKey()));
				return resultList;
			}

			// cpu 검사
			element = hashMap.get(OBDefine.FAULT_CHECK_ELEMENT_HW_CPU);
			if (element != null && element.getState() == OBDefine.STATE_ENABLE) {
				this.hwCheckCount++;
				String curCheckMsg = String.format("%s(%d/%d)", OBFaultCheckMsg.HW_CPU_CHECK.getItemMsg(),
						this.hwCheckCount, this.hwCheckTotal);
				new OBFaultMngImpl().updateDiagnosisStatus(logKey, null, null, null, null, null, null, curCheckMsg,
						OBFaultCheckMsg.HW_CPU_CHECK.getActionMsg());

				// 장비의 상태 점검.
				checkResult = ClassFaultCheckHW.checkHWCpuStatus(element, templateInfo.getThresholdHWCpuUsage());
				if (checkResult.getStatus() == OBDtoFaultCheckResultElement.STATUS_FAIL)
					this.hwCheckFail++;

				// 결과 목록에 저장.
				resultList.add(checkResult);

				// 현재 진행 상태 업데이트.
				progressRate += calProgressRate(OBDefine.ELEMENT_TYPE_HW, OBDefine.FAULT_CHECK_ELEMENT_HW_CPU,
						progressWeight);
				new OBFaultMngImpl().updateDiagnosisStatus(logKey, (int) progressRate, null, this.hwCheckCount,
						this.hwCheckFail, null, null, null, checkResult.getMsgCLI());

				OBDateTime.Sleep(1000);// 장비의 부하를 줄이기 위해서 1초간 정지한다.
			}

			if (new OBFaultMngImpl().isFaultCheckCanceled(logKey) == true) {// 작업이 취소된 경우 처리.
				OBSystemLog.info(OBDefine.LOGFILE_SYSTEM, String.format("[%d/%d] fault check was canceled.",
						this.threadInfo.getAdcIndex(), this.getThreadInfo().getCheckKey()));
				return resultList;
			}

			// memory 검사
			element = hashMap.get(OBDefine.FAULT_CHECK_ELEMENT_HW_MEMORY);
			if (element != null && element.getState() == OBDefine.STATE_ENABLE) {
				this.hwCheckCount++;
				String curCheckMsg = String.format("%s(%d/%d)", OBFaultCheckMsg.HW_MEMORY_CHECK.getItemMsg(),
						this.hwCheckCount, this.hwCheckTotal);
				new OBFaultMngImpl().updateDiagnosisStatus(logKey, null, null, null, null, null, null, curCheckMsg,
						OBFaultCheckMsg.HW_MEMORY_CHECK.getActionMsg());

				// 장비의 상태 점검.
				checkResult = ClassFaultCheckHW.checkHWMemoryStatus(element, templateInfo.getThresholdHWMemoryUsage());
				if (checkResult.getStatus() == OBDtoFaultCheckResultElement.STATUS_FAIL)
					this.hwCheckFail++;

				// 결과 목록에 저장.
				resultList.add(checkResult);

				// 현재 진행 상태 업데이트.
				progressRate += calProgressRate(OBDefine.ELEMENT_TYPE_HW, OBDefine.FAULT_CHECK_ELEMENT_HW_MEMORY,
						progressWeight);
				new OBFaultMngImpl().updateDiagnosisStatus(logKey, (int) progressRate, null, this.hwCheckCount,
						this.hwCheckFail, null, null, null, checkResult.getMsgCLI());

				OBDateTime.Sleep(1000);// 장비의 부하를 줄이기 위해서 1초간 정지한다.
			}

			if (new OBFaultMngImpl().isFaultCheckCanceled(logKey) == true) {// 작업이 취소된 경우 처리.
				OBSystemLog.info(OBDefine.LOGFILE_SYSTEM, String.format("[%d/%d] fault check was canceled.",
						this.threadInfo.getAdcIndex(), this.getThreadInfo().getCheckKey()));
				return resultList;
			}

			// temperature 검사
			element = hashMap.get(OBDefine.FAULT_CHECK_ELEMENT_HW_TEMPERATURE);
			if (element != null && element.getState() == OBDefine.STATE_ENABLE) {
				this.hwCheckCount++;
				String curCheckMsg = String.format("%s(%d/%d)", OBFaultCheckMsg.HW_TEMP_NORM_CHECK.getItemMsg(),
						this.hwCheckCount, this.hwCheckTotal);
				new OBFaultMngImpl().updateDiagnosisStatus(logKey, null, null, null, null, null, null, curCheckMsg,
						OBFaultCheckMsg.HW_TEMP_NORM_CHECK.getActionMsg());

				// 장비의 상태 점검.
				checkResult = ClassFaultCheckHW.checkHWTemperature(element);
				if (checkResult.getStatus() == OBDtoFaultCheckResultElement.STATUS_FAIL)
					this.hwCheckFail++;

				// 결과 목록에 저장.
				resultList.add(checkResult);

				// 현재 진행 상태 업데이트.
				progressRate += calProgressRate(OBDefine.ELEMENT_TYPE_HW, OBDefine.FAULT_CHECK_ELEMENT_HW_TEMPERATURE,
						progressWeight);
				new OBFaultMngImpl().updateDiagnosisStatus(logKey, (int) progressRate, null, this.hwCheckCount,
						this.hwCheckFail, null, null, null, checkResult.getMsgCLI());

				OBDateTime.Sleep(1000);// 장비의 부하를 줄이기 위해서 1초간 정지한다.
			}

			if (new OBFaultMngImpl().isFaultCheckCanceled(logKey) == true) {// 작업이 취소된 경우 처리.
				OBSystemLog.info(OBDefine.LOGFILE_SYSTEM, String.format("[%d/%d] fault check was canceled.",
						this.threadInfo.getAdcIndex(), this.getThreadInfo().getCheckKey()));
				return resultList;
			}

			// fan 상태 검사
			element = hashMap.get(OBDefine.FAULT_CHECK_ELEMENT_HW_FAN);
			if (element != null && element.getState() == OBDefine.STATE_ENABLE) {
				this.hwCheckCount++;
				String curCheckMsg = String.format("%s(%d/%d)", OBFaultCheckMsg.HW_FAN_NORM_CHECK.getItemMsg(),
						this.hwCheckCount, this.hwCheckTotal);
				new OBFaultMngImpl().updateDiagnosisStatus(logKey, null, null, null, null, null, null, curCheckMsg,
						OBFaultCheckMsg.HW_FAN_NORM_CHECK.getActionMsg());

				// 장비의 상태 점검.
				checkResult = ClassFaultCheckHW.checkHWFanStatus(element, this.systemFanMinRpm, this.systemFanMaxRpm);
				if (checkResult.getStatus() == OBDtoFaultCheckResultElement.STATUS_FAIL)
					this.hwCheckFail++;

				// 결과 목록에 저장.
				resultList.add(checkResult);

				// 현재 진행 상태 업데이트.
				progressRate += calProgressRate(OBDefine.ELEMENT_TYPE_HW, OBDefine.FAULT_CHECK_ELEMENT_HW_FAN,
						progressWeight);
				new OBFaultMngImpl().updateDiagnosisStatus(logKey, (int) progressRate, null, this.hwCheckCount,
						this.hwCheckFail, null, null, null, checkResult.getMsgCLI());

				OBDateTime.Sleep(1000);// 장비의 부하를 줄이기 위해서 1초간 정지한다.
			}

			if (new OBFaultMngImpl().isFaultCheckCanceled(logKey) == true) {// 작업이 취소된 경우 처리.
				OBSystemLog.info(OBDefine.LOGFILE_SYSTEM, String.format("[%d/%d] fault check was canceled.",
						this.threadInfo.getAdcIndex(), this.getThreadInfo().getCheckKey()));
				return resultList;
			}

			// adc 로그 검사
			element = hashMap.get(OBDefine.FAULT_CHECK_ELEMENT_HW_ADCLOG);
			if (element != null && element.getState() == OBDefine.STATE_ENABLE) {
				this.hwCheckCount++;
				String curCheckMsg = String.format("%s(%d/%d)", OBFaultCheckMsg.HW_ADCLOG_CHECK.getItemMsg(),
						this.hwCheckCount, this.hwCheckTotal);
				new OBFaultMngImpl().updateDiagnosisStatus(logKey, null, null, null, null, null, null, curCheckMsg,
						OBFaultCheckMsg.HW_ADCLOG_CHECK.getActionMsg());

				// 장비의 상태 점검.
				checkResult = ClassFaultCheckHW.checkHWAdclog(element, templateInfo.getThresholdHWAdcLogCount());
				if (checkResult.getStatus() == OBDtoFaultCheckResultElement.STATUS_FAIL)
					this.hwCheckFail++;

				// 결과 목록에 저장.
				resultList.add(checkResult);

				// 현재 진행 상태 업데이트.
				progressRate += calProgressRate(OBDefine.ELEMENT_TYPE_HW, OBDefine.FAULT_CHECK_ELEMENT_HW_ADCLOG,
						progressWeight);
				new OBFaultMngImpl().updateDiagnosisStatus(logKey, (int) progressRate, null, this.hwCheckCount,
						this.hwCheckFail, null, null, null, checkResult.getMsgCLI());

				OBDateTime.Sleep(1000);// 장비의 부하를 줄이기 위해서 1초간 정지한다.
			}

			if (new OBFaultMngImpl().isFaultCheckCanceled(logKey) == true) {// 작업이 취소된 경우 처리.
				OBSystemLog.info(OBDefine.LOGFILE_SYSTEM, String.format("[%d/%d] fault check was canceled.",
						this.threadInfo.getAdcIndex(), this.getThreadInfo().getCheckKey()));
				return resultList;
			}

			// os 정보 추출.
			element = hashMap.get(OBDefine.FAULT_CHECK_ELEMENT_HW_OSINFO);
			if (element != null && element.getState() == OBDefine.STATE_ENABLE) {
				this.hwCheckCount++;
				String curCheckMsg = String.format("%s(%d/%d)", OBFaultCheckMsg.HW_OS_CHECK.getItemMsg(),
						this.hwCheckCount, this.hwCheckTotal);
				new OBFaultMngImpl().updateDiagnosisStatus(logKey, null, null, null, null, null, null, curCheckMsg,
						OBFaultCheckMsg.HW_OS_CHECK.getActionMsg());

				// 장비의 상태 점검.
				checkResult = ClassFaultCheckHW.checkHWOSInfo(element, adcInfo);
				if (checkResult.getStatus() == OBDtoFaultCheckResultElement.STATUS_FAIL)
					this.hwCheckFail++;

				// 결과 목록에 저장.
				resultList.add(checkResult);

				// 현재 진행 상태 업데이트.
				progressRate += calProgressRate(OBDefine.ELEMENT_TYPE_HW, OBDefine.FAULT_CHECK_ELEMENT_HW_OSINFO,
						progressWeight);
				new OBFaultMngImpl().updateDiagnosisStatus(logKey, (int) progressRate, null, this.hwCheckCount,
						this.hwCheckFail, null, null, null, checkResult.getMsgCLI());

				OBDateTime.Sleep(1000);// 장비의 부하를 줄이기 위해서 1초간 정지한다.
			}

			// 점검 결과를 DB에 저장.
			new OBFaultMonitoringDB().writeFaultLogSummary(logKey, resultList);
			return resultList;
		} catch (OBException e) {
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("[%d/%d] failed to check hw items. msg:%s",
					this.threadInfo.getAdcIndex(), this.getThreadInfo().getCheckKey(), e.getErrorMessage()));
			throw e;
		} catch (Exception e) {
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
					String.format("[%d/%d] failed to check hw items. msg:null pointer error",
							this.threadInfo.getAdcIndex(), this.getThreadInfo().getCheckKey()));
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

	/**
	 * L23 장애 감지.
	 * 
	 * @param logKey
	 * @param logInfo
	 * @param db
	 * @throws OBException
	 */
	private ArrayList<OBDtoFaultCheckResultElement> processFaultCheckL23(long logKey, OBDtoFaultCheckLog logInfo,
			OBDtoAdcInfo adcInfo, OBDtoFaultCheckTemplate templateInfo, long startTime, Object CLIObj,
			float progressWeight) throws OBException {
		try {
			OBDtoElement element = null;
			OBDtoFaultCheckResultElement checkResult = null;
			HashMap<Integer, OBDtoElement> hashMap = convertElementListToMap(templateInfo.getL23CheckItems());
			ArrayList<OBDtoFaultCheckResultElement> resultList = new ArrayList<OBDtoFaultCheckResultElement>();

			OBFaultCheckHandler ClassFaultCheckL23 = null;// new OBFaultCheckHW(adcInfo.getAdcType(),
															// adcInfo.getSwVersion());
			ClassFaultCheckL23 = getFaultCheckHandler(adcInfo);
			ClassFaultCheckL23.setParameter(logKey, logInfo, adcInfo, CLIObj);
			ClassFaultCheckL23.loadSnmpOidHWInfo();

			OBDtoFaultCheckStatus tmpLogStatus = new OBFaultMngImpl().getFaultCheckStatus(logKey);
			float progressRate = tmpLogStatus.getProgressRate();

			if (new OBFaultMngImpl().isFaultCheckCanceled(logKey) == true) {// 작업이 취소된 경우 처리.
				OBSystemLog.info(OBDefine.LOGFILE_SYSTEM, String.format("[%d/%d] fault check was canceled.",
						this.threadInfo.getAdcIndex(), this.getThreadInfo().getCheckKey()));
				return resultList;
			}

			// vlan info
			element = hashMap.get(OBDefine.FAULT_CHECK_ELEMENT_L23_VLAN);
			if (element != null && element.getState() == OBDefine.STATE_ENABLE) {
				this.hwCheckCount++;
				String curCheckMsg = String.format("%s(%d/%d)", OBFaultCheckMsg.L23_VLAN_CHECK.getItemMsg(),
						this.hwCheckCount, this.hwCheckTotal);
				new OBFaultMngImpl().updateDiagnosisStatus(logKey, null, null, null, null, null, null, curCheckMsg,
						OBFaultCheckMsg.L23_VLAN_CHECK.getActionMsg());

				// 장비의 상태 점검.
				checkResult = ClassFaultCheckL23.checkL23VlanInfo(element);
				if (checkResult.getStatus() == OBDtoFaultCheckResultElement.STATUS_FAIL)
					this.hwCheckFail++;

				// 결과 목록에 저장.
				resultList.add(checkResult);

				// 현재 진행 상태 업데이트.
				progressRate += calProgressRate(OBDefine.ELEMENT_TYPE_L23, OBDefine.FAULT_CHECK_ELEMENT_L23_VLAN,
						progressWeight);
				new OBFaultMngImpl().updateDiagnosisStatus(logKey, (int) progressRate, null, this.hwCheckCount,
						this.hwCheckFail, null, null, null, checkResult.getMsgCLI());

				OBDateTime.Sleep(1000);// 장비의 부하를 줄이기 위해서 1초간 정지한다.
			}

			if (new OBFaultMngImpl().isFaultCheckCanceled(logKey) == true) {// 작업이 취소된 경우 처리.
				OBSystemLog.info(OBDefine.LOGFILE_SYSTEM, String.format("[%d/%d] fault check was canceled.",
						this.threadInfo.getAdcIndex(), this.getThreadInfo().getCheckKey()));
				return resultList;
			}

			// stp info
			element = hashMap.get(OBDefine.FAULT_CHECK_ELEMENT_L23_STP);
			if (element != null && element.getState() == OBDefine.STATE_ENABLE) {
				this.hwCheckCount++;
				String curCheckMsg = String.format("%s(%d/%d)", OBFaultCheckMsg.L23_STP_CHECK.getItemMsg(),
						this.hwCheckCount, this.hwCheckTotal);
				new OBFaultMngImpl().updateDiagnosisStatus(logKey, null, null, null, null, null, null, curCheckMsg,
						OBFaultCheckMsg.L23_STP_CHECK.getActionMsg());

				// 장비의 상태 점검.
				checkResult = ClassFaultCheckL23.checkL23StpInfo(element);
				if (checkResult.getStatus() == OBDtoFaultCheckResultElement.STATUS_FAIL)
					this.hwCheckFail++;

				// 결과 목록에 저장.
				resultList.add(checkResult);

				// 현재 진행 상태 업데이트.
				progressRate += calProgressRate(OBDefine.ELEMENT_TYPE_L23, OBDefine.FAULT_CHECK_ELEMENT_L23_STP,
						progressWeight);
				new OBFaultMngImpl().updateDiagnosisStatus(logKey, (int) progressRate, null, this.hwCheckCount,
						this.hwCheckFail, null, null, null, checkResult.getMsgCLI());

				OBDateTime.Sleep(1000);// 장비의 부하를 줄이기 위해서 1초간 정지한다.
			}

			if (new OBFaultMngImpl().isFaultCheckCanceled(logKey) == true) {// 작업이 취소된 경우 처리.
				OBSystemLog.info(OBDefine.LOGFILE_SYSTEM, String.format("[%d/%d] fault check was canceled.",
						this.threadInfo.getAdcIndex(), this.getThreadInfo().getCheckKey()));
				return resultList;
			}

			// trunk info
			element = hashMap.get(OBDefine.FAULT_CHECK_ELEMENT_L23_TRUNK);
			if (element != null && element.getState() == OBDefine.STATE_ENABLE) {
				this.hwCheckCount++;
				String curCheckMsg = String.format("%s(%d/%d)", OBFaultCheckMsg.L23_TRUNK_CHECK.getItemMsg(),
						this.hwCheckCount, this.hwCheckTotal);
				new OBFaultMngImpl().updateDiagnosisStatus(logKey, null, null, null, null, null, null, curCheckMsg,
						OBFaultCheckMsg.L23_TRUNK_CHECK.getActionMsg());

				// 장비의 상태 점검.
				checkResult = ClassFaultCheckL23.checkL23TrunkInfo(element);
				if (checkResult.getStatus() == OBDtoFaultCheckResultElement.STATUS_FAIL)
					this.hwCheckFail++;

				// 결과 목록에 저장.
				resultList.add(checkResult);

				// 현재 진행 상태 업데이트.
				progressRate += calProgressRate(OBDefine.ELEMENT_TYPE_L23, OBDefine.FAULT_CHECK_ELEMENT_L23_TRUNK,
						progressWeight);
				new OBFaultMngImpl().updateDiagnosisStatus(logKey, (int) progressRate, null, this.hwCheckCount,
						this.hwCheckFail, null, null, null, checkResult.getMsgCLI());

				OBDateTime.Sleep(1000);// 장비의 부하를 줄이기 위해서 1초간 정지한다.
			}

			if (new OBFaultMngImpl().isFaultCheckCanceled(logKey) == true) {// 작업이 취소된 경우 처리.
				OBSystemLog.info(OBDefine.LOGFILE_SYSTEM, String.format("[%d/%d] fault check was canceled.",
						this.threadInfo.getAdcIndex(), this.getThreadInfo().getCheckKey()));
				return resultList;
			}

			// vrrp info
			element = hashMap.get(OBDefine.FAULT_CHECK_ELEMENT_L23_VRRP);
			if (element != null && element.getState() == OBDefine.STATE_ENABLE) {
				this.hwCheckCount++;
				String curCheckMsg = String.format("%s(%d/%d)", OBFaultCheckMsg.L23_VRRP_CHECK.getItemMsg(),
						this.hwCheckCount, this.hwCheckTotal);
				new OBFaultMngImpl().updateDiagnosisStatus(logKey, null, null, null, null, null, null, curCheckMsg,
						OBFaultCheckMsg.L23_VRRP_CHECK.getActionMsg());

				// 장비의 상태 점검.
				checkResult = ClassFaultCheckL23.checkL23VrrpInfo(element);
				if (checkResult.getStatus() == OBDtoFaultCheckResultElement.STATUS_FAIL)
					this.hwCheckFail++;

				// 결과 목록에 저장.
				resultList.add(checkResult);

				// 현재 진행 상태 업데이트.
				progressRate += calProgressRate(OBDefine.ELEMENT_TYPE_L23, OBDefine.FAULT_CHECK_ELEMENT_L23_VRRP,
						progressWeight);
				new OBFaultMngImpl().updateDiagnosisStatus(logKey, (int) progressRate, null, this.hwCheckCount,
						this.hwCheckFail, null, null, null, checkResult.getMsgCLI());

				OBDateTime.Sleep(1000);// 장비의 부하를 줄이기 위해서 1초간 정지한다.
			}

			if (new OBFaultMngImpl().isFaultCheckCanceled(logKey) == true) {// 작업이 취소된 경우 처리.
				OBSystemLog.info(OBDefine.LOGFILE_SYSTEM, String.format("[%d/%d] fault check was canceled.",
						this.threadInfo.getAdcIndex(), this.getThreadInfo().getCheckKey()));
				return resultList;
			}

			// routing info
			element = hashMap.get(OBDefine.FAULT_CHECK_ELEMENT_L23_ROUTING);
			if (element != null && element.getState() == OBDefine.STATE_ENABLE) {
				this.hwCheckCount++;
				String curCheckMsg = String.format("%s(%d/%d)", OBFaultCheckMsg.L23_ROUTING_CHECK.getItemMsg(),
						this.hwCheckCount, this.hwCheckTotal);
				new OBFaultMngImpl().updateDiagnosisStatus(logKey, null, null, null, null, null, null, curCheckMsg,
						OBFaultCheckMsg.L23_ROUTING_CHECK.getActionMsg());

				// 장비의 상태 점검.
				checkResult = ClassFaultCheckL23.checkL23RoutingInfo(element);
				if (checkResult.getStatus() == OBDtoFaultCheckResultElement.STATUS_FAIL)
					this.hwCheckFail++;

				// 결과 목록에 저장.
				resultList.add(checkResult);

				// 현재 진행 상태 업데이트.
				progressRate += calProgressRate(OBDefine.ELEMENT_TYPE_L23, OBDefine.FAULT_CHECK_ELEMENT_L23_ROUTING,
						progressWeight);
				new OBFaultMngImpl().updateDiagnosisStatus(logKey, (int) progressRate, null, this.hwCheckCount,
						this.hwCheckFail, null, null, null, checkResult.getMsgCLI());

				OBDateTime.Sleep(1000);// 장비의 부하를 줄이기 위해서 1초간 정지한다.
			}

			if (new OBFaultMngImpl().isFaultCheckCanceled(logKey) == true) {// 작업이 취소된 경우 처리.
				OBSystemLog.info(OBDefine.LOGFILE_SYSTEM, String.format("[%d/%d] fault check was canceled.",
						this.threadInfo.getAdcIndex(), this.getThreadInfo().getCheckKey()));
				return resultList;
			}

			// interface info
			element = hashMap.get(OBDefine.FAULT_CHECK_ELEMENT_L23_INTERFACE);
			if (element != null && element.getState() == OBDefine.STATE_ENABLE) {
				this.hwCheckCount++;
				String curCheckMsg = String.format("%s(%d/%d)", OBFaultCheckMsg.L23_INTERFACE_CHECK.getItemMsg(),
						this.hwCheckCount, this.hwCheckTotal);
				new OBFaultMngImpl().updateDiagnosisStatus(logKey, null, null, null, null, null, null, curCheckMsg,
						OBFaultCheckMsg.L23_INTERFACE_CHECK.getActionMsg());

				// 장비의 상태 점검.
				checkResult = ClassFaultCheckL23.checkL23InterfaceInfo(element);
				if (checkResult.getStatus() == OBDtoFaultCheckResultElement.STATUS_FAIL)
					this.hwCheckFail++;

				// 결과 목록에 저장.
				resultList.add(checkResult);

				// 현재 진행 상태 업데이트.
				progressRate += calProgressRate(OBDefine.ELEMENT_TYPE_L23, OBDefine.FAULT_CHECK_ELEMENT_L23_INTERFACE,
						progressWeight);
				new OBFaultMngImpl().updateDiagnosisStatus(logKey, (int) progressRate, null, this.hwCheckCount,
						this.hwCheckFail, null, null, null, checkResult.getMsgCLI());

				OBDateTime.Sleep(1000);// 장비의 부하를 줄이기 위해서 1초간 정지한다.
			}

			// 점검 결과를 DB에 저장.
			new OBFaultMonitoringDB().writeFaultLogSummary(logKey, resultList);
			return resultList;
		} catch (OBException e) {
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("[%d/%d] failed to check l2-l3 items. msg:%s",
					this.threadInfo.getAdcIndex(), this.getThreadInfo().getCheckKey(), e.getErrorMessage()));
			throw e;
		} catch (Exception e) {
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
					String.format("[%d/%d] failed to check l2-l3 items. msg:null pointer error",
							this.threadInfo.getAdcIndex(), this.getThreadInfo().getCheckKey()));
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

	/**
	 * L47 장애 감지.
	 * 
	 * @param logKey
	 * @param logInfo
	 * @param db
	 * @throws OBException
	 */
	private ArrayList<OBDtoFaultCheckResultElement> processFaultCheckL47(long logKey, OBDtoFaultCheckLog logInfo,
			OBDtoAdcInfo adcInfo, OBDtoFaultCheckTemplate templateInfo, long startTime, Object CLIObj,
			float progressWeight) throws OBException {
		try {
			OBDtoElement element = null;
			OBDtoFaultCheckResultElement checkResult = null;
			HashMap<Integer, OBDtoElement> hashMap = convertElementListToMap(templateInfo.getL47CheckItems());
			ArrayList<OBDtoFaultCheckResultElement> resultList = new ArrayList<OBDtoFaultCheckResultElement>();

			OBFaultCheckHandler ClassFaultCheckL47 = null;// new OBFaultCheckHW(adcInfo.getAdcType(),
															// adcInfo.getSwVersion());
			ClassFaultCheckL47 = getFaultCheckHandler(adcInfo);
			ClassFaultCheckL47.setParameter(logKey, logInfo, adcInfo, CLIObj);
			ClassFaultCheckL47.loadSnmpOidHWInfo();

			OBDtoFaultCheckStatus tmpLogStatus = new OBFaultMngImpl().getFaultCheckStatus(logKey);
			float progressRate = tmpLogStatus.getProgressRate();

			if (new OBFaultMngImpl().isFaultCheckCanceled(logKey) == true) {// 작업이 취소된 경우 처리.
				OBSystemLog.info(OBDefine.LOGFILE_SYSTEM, String.format("[%d/%d] fault check was canceled.",
						this.threadInfo.getAdcIndex(), this.getThreadInfo().getCheckKey()));
				return resultList;
			}

			// not used slb info
			element = hashMap.get(OBDefine.FAULT_CHECK_ELEMENT_L47_NOT_USED_CF);
			if (element != null && element.getState() == OBDefine.STATE_ENABLE) {
				this.hwCheckCount++;
				String curCheckMsg = String.format("%s(%d/%d)", OBFaultCheckMsg.L47_UNUSED_SLB_CHECK.getItemMsg(),
						this.hwCheckCount, this.hwCheckTotal);
				new OBFaultMngImpl().updateDiagnosisStatus(logKey, null, null, null, null, null, null, curCheckMsg,
						OBFaultCheckMsg.L47_UNUSED_SLB_CHECK.getActionMsg());

				// 장비의 상태 점검.
				checkResult = ClassFaultCheckL47.checkL47NotUsedSLB(element);
				if (checkResult.getStatus() == OBDtoFaultCheckResultElement.STATUS_FAIL)
					this.hwCheckFail++;

				// 결과 목록에 저장.
				resultList.add(checkResult);

				// 현재 진행 상태 업데이트.
				progressRate += calProgressRate(OBDefine.ELEMENT_TYPE_L47, OBDefine.FAULT_CHECK_ELEMENT_L47_NOT_USED_CF,
						progressWeight);
				new OBFaultMngImpl().updateDiagnosisStatus(logKey, (int) progressRate, null, this.hwCheckCount,
						this.hwCheckFail, null, null, null, checkResult.getMsgCLI());

				OBDateTime.Sleep(1000);// 장비의 부하를 줄이기 위해서 1초간 정지한다.
			}

			// 유휴 SLB info
			element = hashMap.get(OBDefine.FAULT_CHECK_ELEMENT_L47_SLEEP_VS);
			if (element != null && element.getState() == OBDefine.STATE_ENABLE) {
				this.hwCheckCount++;
				String curCheckMsg = String.format("%s(%d/%d)", OBFaultCheckMsg.L47_SLEEP_VSERVER_CHECK.getItemMsg(),
						this.hwCheckCount, this.hwCheckTotal);
				new OBFaultMngImpl().updateDiagnosisStatus(logKey, null, null, null, null, null, null, curCheckMsg,
						OBFaultCheckMsg.L47_SLEEP_VSERVER_CHECK.getActionMsg());

				// 장비의 상태 점검.
				checkResult = ClassFaultCheckL47.checkL47SleepSLB(element, templateInfo.getThresholdL47SleepVSDay());
				if (checkResult.getStatus() == OBDtoFaultCheckResultElement.STATUS_FAIL)
					this.hwCheckFail++;

				// 결과 목록에 저장.
				resultList.add(checkResult);

				// 현재 진행 상태 업데이트.
				progressRate += calProgressRate(OBDefine.ELEMENT_TYPE_L47, OBDefine.FAULT_CHECK_ELEMENT_L47_SLEEP_VS,
						progressWeight);
				new OBFaultMngImpl().updateDiagnosisStatus(logKey, (int) progressRate, null, this.hwCheckCount,
						this.hwCheckFail, null, null, null, checkResult.getMsgCLI());

				OBDateTime.Sleep(1000);// 장비의 부하를 줄이기 위해서 1초간 정지한다.
			}

			// 세션 테이블 검사.
			element = hashMap.get(OBDefine.FAULT_CHECK_ELEMENT_L47_SESSION_TABLE);
			if (element != null && element.getState() == OBDefine.STATE_ENABLE) {
				this.hwCheckCount++;
				String curCheckMsg = String.format("%s(%d/%d)",
						OBFaultCheckMsg.L47_ALLOCATION_FAILURE_CHECK.getItemMsg(), this.hwCheckCount,
						this.hwCheckTotal);
				new OBFaultMngImpl().updateDiagnosisStatus(logKey, null, null, null, null, null, null, curCheckMsg,
						OBFaultCheckMsg.L47_ALLOCATION_FAILURE_CHECK.getActionMsg());

				// 장비의 상태 점검.
				checkResult = ClassFaultCheckL47.checkL47SessionTable(element);
				if (checkResult.getStatus() == OBDtoFaultCheckResultElement.STATUS_FAIL)
					this.hwCheckFail++;

				// 결과 목록에 저장.
				resultList.add(checkResult);

				// 현재 진행 상태 업데이트.
				progressRate += calProgressRate(OBDefine.ELEMENT_TYPE_L47,
						OBDefine.FAULT_CHECK_ELEMENT_L47_SESSION_TABLE, progressWeight);
				new OBFaultMngImpl().updateDiagnosisStatus(logKey, (int) progressRate, null, this.hwCheckCount,
						this.hwCheckFail, null, null, null, checkResult.getMsgCLI());

				OBDateTime.Sleep(1000);// 장비의 부하를 줄이기 위해서 1초간 정지한다.
			}

			// 점검 결과를 DB에 저장.
			new OBFaultMonitoringDB().writeFaultLogSummary(logKey, resultList);

			return resultList;
		} catch (OBException e) {
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("[%d/%d] failed to check l4-l7 items. msg:%s",
					this.threadInfo.getAdcIndex(), this.getThreadInfo().getCheckKey(), e.getErrorMessage()));
			throw e;
		} catch (Exception e) {
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
					String.format("[%d/%d] failed to check l4-l7 items. msg:null pointer error",
							this.threadInfo.getAdcIndex(), this.getThreadInfo().getCheckKey()));
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

	/**
	 * SVC 장애 감지.
	 * 
	 * @param logKey
	 * @param logInfo
	 * @param db
	 * @throws OBException
	 */
	private ArrayList<OBDtoFaultCheckResultElement> processFaultCheckSvc(long logKey, OBDtoFaultCheckLog logInfo,
			OBDtoAdcInfo adcInfo, OBDtoFaultCheckTemplate templateInfo, long startTime, Object CLIObj,
			float progressWeight) throws OBException {// 시간이 많이 소요되는 관계로 단위 항목별 검사를 본 함수에서 직접 제어한다.
		try {
			OBDtoElement element = null;
			OBDtoFaultCheckResultElement checkResult = null;
			HashMap<Integer, OBDtoElement> hashMap = convertElementListToMap(templateInfo.getSvcCheckItems());
			ArrayList<OBDtoFaultCheckResultElement> resultList = new ArrayList<OBDtoFaultCheckResultElement>();

			if (new OBFaultMngImpl().isFaultCheckCanceled(logKey) == true) {// 작업이 취소된 경우 처리.
				OBSystemLog.info(OBDefine.LOGFILE_SYSTEM, String.format("[%d/%d] fault check was canceled.",
						this.threadInfo.getAdcIndex(), this.getThreadInfo().getCheckKey()));
				return resultList;
			}

			if (logInfo.getClientIP() == null || logInfo.getClientIP().isEmpty() || logInfo.getVsvcIndex() == null
					|| logInfo.getVsvcIndex().isEmpty()) {
				OBSystemLog.info(OBDefine.LOGFILE_SYSTEM, String.format("[%d/%d] invalid parameter",
						this.threadInfo.getAdcIndex(), this.getThreadInfo().getCheckKey()));
				new OBFaultMngImpl().updateDiagnosisStatus(logKey, null, null, null, null,
						OBDefine.FAULT_CHECK_STATUS_ABNORMAL, null, null,
						OBFaultCheckMsg.INVALID_PARAMETER.getSuccessMsg());
				return resultList;
			}

			OBFaultCheckHandler ClassFaultCheckSVC = null;// new OBFaultCheckHW(adcInfo.getAdcType(),
															// adcInfo.getSwVersion());
			ClassFaultCheckSVC = getFaultCheckHandler(adcInfo);
			ClassFaultCheckSVC.setParameter(logKey, logInfo, adcInfo, CLIObj);
			ClassFaultCheckSVC.loadSnmpOidHWInfo();
			int svcCheckTotal = 4;// resultList.size();
			int checkCount = 0;
			int svcProgressRate = 0;

			// 패킷 덤프.
			checkCount++;
			String curCheckMsg = String.format("%s(%d/%d)", OBFaultCheckMsg.SVC_DUMP_PCAP_INFO.getItemMsg(), checkCount,
					svcCheckTotal);
			new OBFaultMngImpl().updateDiagnosisStatus(logKey, null, null, null, null, null, null, curCheckMsg,
					OBFaultCheckMsg.SVC_DUMP_PCAP_INFO.getActionMsg());

			String vsvcIPAddress = new OBFaultMonitoringDB().getVSvcIPAddress(adcInfo.getIndex(), adcInfo.getAdcType(),
					logInfo.getVsvcIndex());
			String clientIPAddress = logInfo.getClientIP();

			OBDtoFaultCheckStatus tmpLogStatus = new OBFaultMngImpl().getFaultCheckStatus(logKey);
			if (tmpLogStatus.getProgressRate() == OBDefine.FAULT_CHECK_STATUS_CANCEL) {
				OBSystemLog.info(OBDefine.LOGFILE_SYSTEM,
						String.format("[%d/%d]  invalid status(%d)", this.threadInfo.getAdcIndex(),
								this.getThreadInfo().getCheckKey(), OBDefine.FAULT_CHECK_STATUS_CANCEL));
				return resultList;
			}
			float progressRate = tmpLogStatus.getProgressRate();

			// 이전에 수행되고 있는 프로세스가 있다면 종료한다.
			ClassFaultCheckSVC.stopSvcPacketDump();

			OBDateTime.Sleep(100);

			if (new OBFaultMngImpl().isFaultCheckCanceled(logKey) == true) {// 작업이 취소된 경우 처리.
				OBSystemLog.info(OBDefine.LOGFILE_SYSTEM, String.format("[%d/%d] fault check was canceled.",
						this.threadInfo.getAdcIndex(), this.getThreadInfo().getCheckKey()));
				return resultList;
			}

//			OBDtoReturnInfo retInfo = null; 
			Integer svcPort = new OBFaultMonitoringDB().getSvcPortNum(adcInfo, logInfo.getVsvcIndex());
			element = hashMap.get(OBDefine.FAULT_CHECK_ELEMENT_SVC_PKTDUMP);
			checkResult = ClassFaultCheckSVC.startSvcPacketDump(element, logKey, clientIPAddress, vsvcIPAddress,
					svcPort);

			// #3984-6 #4: 14.08.08 sw.jung 대상 서비스 내용 포함
			ArrayList<OBDtoFaultCheckResultContent> tempContentList = checkResult.getResultList();
			for (OBDtoFaultCheckResultContent tempContent : tempContentList) {
				tempContent.setDetail(String.format("Client IP: %s, Service: %s:%s\n\n%s", clientIPAddress,
						vsvcIPAddress, svcPort, tempContent.getDetail()));
			}

			if (checkResult.getStatus() != OBDtoFaultCheckResultElement.STATUS_SUCC)// retInfo.getRetCode()==OBDtoReturnInfo.CODE_FALSE)
			{// 패킷 시작에 실패한 경우.
				OBSystemLog.info(OBDefine.LOGFILE_SYSTEM, String.format("[%d/%d] failed to capture pkt.",
						this.threadInfo.getAdcIndex(), this.getThreadInfo().getCheckKey()));

				// #3984-6 #4: 14.08.08 sw.jung 대상 서비스 내용 포함
				checkResult.setMsgCLI(String.format("Client IP: %s, Service: %s:%s\n\n%s", clientIPAddress,
						vsvcIPAddress, svcPort, checkResult.getMsgCLI()));

				// 결과 목록에 저장.
				resultList.add(checkResult);

				// 현재 진행 상태 업데이트.
				progressRate += calProgressRate(OBDefine.ELEMENT_TYPE_SVC, OBDefine.FAULT_CHECK_ELEMENT_SVC_PKTDUMP,
						progressWeight);

				new OBFaultMngImpl().updateDiagnosisStatus(logKey, (int) progressRate, null, null, null,
						OBDefine.FAULT_CHECK_STATUS_NORMAL, null, null, checkResult.getMsgCLI());

				new OBFaultMonitoringDB().writeFaultLogSummary(logKey, resultList);
				return resultList;
			}

			OBDateTime.Sleep(1000);// 장비의 부하를 줄이기 위해서 1초간 정지한다.

//			String dumpFileName = (String) retInfo.getDetailInfo();

			if (new OBFaultMngImpl().isFaultCheckCanceled(logKey) == true) {// 작업이 취소된 경우 처리.
				OBSystemLog.info(OBDefine.LOGFILE_SYSTEM, String.format("[%d/%d] fault check was canceled.",
						this.threadInfo.getAdcIndex(), this.getThreadInfo().getCheckKey()));
				return resultList;
			}
			OBSystemLog.info(OBDefine.LOGFILE_SYSTEM, String.format("[%d/%d] wait for pkt dump",
					this.threadInfo.getAdcIndex(), this.getThreadInfo().getCheckKey()));
			// 덤프될때까지 기다린다.
			Long startTick = OBDateTime.toTimestamp(OBDateTime.now()).getTime();
			OBDtoReturnInfo procRetInfo = new OBDtoReturnInfo();
			procRetInfo.setDetailInfo("0");
			String dumpFileName = logKey + ".pcap";

			float pcapDumpRate = calProgressRate(OBDefine.ELEMENT_TYPE_SVC, OBDefine.FAULT_CHECK_ELEMENT_SVC_PKTDUMP,
					progressWeight);
			;
			while (true) {
				Long currentTick = OBDateTime.toTimestamp(OBDateTime.now()).getTime();
				long elaspeTime = Math.abs(currentTick - startTick);
				if (elaspeTime > this.packetDumpMaxWaitMsec) {// 최대 대기 시간까지 기다린 경우.
																// 강제 종료 후 상태 검사한다.
					ClassFaultCheckSVC.stopSvcPacketDump();

					checkResult = ClassFaultCheckSVC.isSvcPacketAvaliable(element, logKey);

					// #3984-6 #4: 14.08.08 sw.jung 대상 서비스 내용 포함
					tempContentList = checkResult.getResultList();
					for (OBDtoFaultCheckResultContent tempContent : tempContentList) {
						tempContent.setDetail(String.format("Client IP: %s, Service: %s:%s\n\n%s", clientIPAddress,
								vsvcIPAddress, svcPort, tempContent.getDetail()));
					}

					if (checkResult.getStatus() == OBDtoFaultCheckResultElement.STATUS_SUCC) {// 수집한 패킷이 있는지 검사한다.
						OBSystemLog.info(OBDefine.LOGFILE_SYSTEM, String.format("[%d/%d] completed to capture pkt",
								this.threadInfo.getAdcIndex(), this.getThreadInfo().getCheckKey()));
						break;
					}

					// #3984-6 #4: 14.08.08 sw.jung 대상 서비스 내용 포함
					checkResult.setMsgCLI(String.format("Client IP: %s, Service: %s:%s\n\n%s", clientIPAddress,
							vsvcIPAddress, svcPort, checkResult.getMsgCLI()));

					OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
							String.format("[%d/%d] failed to capture pkt. msg:%s", this.threadInfo.getAdcIndex(),
									this.getThreadInfo().getCheckKey(), checkResult.getMsgCLI()));

					// 결과 목록에 저장.
					resultList.add(checkResult);

					int progress = (int) (elaspeTime * 80 / this.packetDumpMaxWaitMsec);
					progressRate += calProgressRate(OBDefine.ELEMENT_TYPE_SVC, OBDefine.FAULT_CHECK_ELEMENT_SVC_PKTDUMP,
							progressWeight);
					new OBFaultMngImpl().updateDiagnosisStatus(logKey, progress, null, null, null,
							OBDefine.FAULT_CHECK_STATUS_NORMAL, null, null, checkResult.getMsgCLI());

					new OBFaultMonitoringDB().writeFaultLogSummary(logKey, resultList);

					return resultList;
				}

				// 파일 사이즈 검사한다.
				if (ClassFaultCheckSVC.isSvcPacketDumpSizeExceed(dumpFileName, this.packetDumpMaxSize) == true)
					break;
//				loopCnt++;

				procRetInfo = ClassFaultCheckSVC.isSvcPacketDumpProgress(dumpFileName);
				if (procRetInfo != null && procRetInfo.getRetCode() != null
						&& procRetInfo.getRetCode() == OBDtoReturnInfo.CODE_FALSE) {
					OBSystemLog.info(OBDefine.LOGFILE_SYSTEM, String.format("[%d/%d] pkt capture stopped",
							this.threadInfo.getAdcIndex(), this.getThreadInfo().getCheckKey()));
					break;
				}

				if (new OBFaultMngImpl().isFaultCheckCanceled(logKey) == true) {// 작업이 취소된 경우 처리.
					ClassFaultCheckSVC.stopSvcPacketDump();
					OBSystemLog.info(OBDefine.LOGFILE_SYSTEM, String.format("[%d/%d] fault check was canceled.",
							this.threadInfo.getAdcIndex(), this.getThreadInfo().getCheckKey()));
					return resultList;
				}

				OBDateTime.Sleep(3000);

				if (new OBFaultMngImpl().isFaultCheckCanceled(logKey) == true) {// 작업이 취소된 경우 처리.
					ClassFaultCheckSVC.stopSvcPacketDump();
					OBSystemLog.info(OBDefine.LOGFILE_SYSTEM, String.format("[%d/%d] fault check was canceled.",
							this.threadInfo.getAdcIndex(), this.getThreadInfo().getCheckKey()));
					return resultList;
				}

				svcProgressRate = (int) (elaspeTime * (int) PROGRESS_RATE_WEIGHT_TYPE_SVC[0]
						/ this.packetDumpMaxWaitMsec);
				int currentRate = (int) (pcapDumpRate * svcProgressRate / 100.0f);

				new OBFaultMngImpl().updateDiagnosisStatus(logKey, (int) (progressRate + currentRate), null, null, null,
						svcProgressRate, null, null, OBFaultCheckMsg.SVC_DUMP_PCAP_INFO.getActionMsg());
			}

			if (new OBFaultMngImpl().isFaultCheckCanceled(logKey) == true) {// 작업이 취소된 경우 처리.
				ClassFaultCheckSVC.stopSvcPacketDump();
				OBSystemLog.info(OBDefine.LOGFILE_SYSTEM, String.format("[%d/%d] fault check was canceled.",
						this.threadInfo.getAdcIndex(), this.getThreadInfo().getCheckKey()));
				return resultList;
			}

			ClassFaultCheckSVC.stopSvcPacketDump();

			svcProgressRate = (int) PROGRESS_RATE_WEIGHT_TYPE_SVC[0];
			progressRate += calProgressRate(OBDefine.ELEMENT_TYPE_SVC, OBDefine.FAULT_CHECK_ELEMENT_SVC_PKTDUMP,
					progressWeight);
			new OBFaultMngImpl().updateDiagnosisStatus(logKey, (int) progressRate, null, null, null, svcProgressRate,
					null, null, null);

			OBDateTime.Sleep(1000);// 장비의 부하를 줄이기 위해서 1초간 정지한다.

			// 덤프 파일을 다운로드한다.
			checkCount++;
			OBDtoSystemEnvNetwork envInfo = new OBEnvManagementImpl().getNetworkConfig();
			curCheckMsg = String.format("%s(%d/%d)", OBFaultCheckMsg.SVC_DOWNLOAD_PCAP_STATUS.getItemMsg(), checkCount,
					svcCheckTotal);
			if (new OBFaultMngImpl().isFaultCheckCanceled(logKey) == true) {// 작업이 취소된 경우 처리.
				OBSystemLog.info(OBDefine.LOGFILE_SYSTEM, String.format("[%d/%d] fault check was canceled.",
						this.threadInfo.getAdcIndex(), this.getThreadInfo().getCheckKey()));
				return resultList;
			}
			new OBFaultMngImpl().updateDiagnosisStatus(logKey, null, null, null, null, null, null, curCheckMsg,
					OBFaultCheckMsg.SVC_DOWNLOAD_PCAP_STATUS.getActionMsg());

			OBSystemLog.info(OBDefine.LOGFILE_SYSTEM, String.format("[%d/%d] try to download file(%s).",
					this.threadInfo.getAdcIndex(), this.getThreadInfo().getCheckKey(), dumpFileName));

			OBDtoReturnInfo retInfo = ClassFaultCheckSVC.downloadDumpFile(dumpFileName, dumpFileName,
					envInfo.getIpAddress());
			if (retInfo.getRetCode() == OBDtoReturnInfo.CODE_FALSE) {// 다운로드 실패한 경우.
				OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("[%d/%d] failed to downlaod file(%s).",
						this.threadInfo.getAdcIndex(), this.getThreadInfo().getCheckKey(), dumpFileName));
				checkResult = new OBDtoFaultCheckResultElement();
				checkResult.setObj(hashMap.get(OBDefine.FAULT_CHECK_ELEMENT_SVC_PKTDUMP));
				checkResult.setStatus(OBDtoFaultCheckResultElement.STATUS_FAIL);
				ArrayList<OBDtoFaultCheckResultContent> contentList = new ArrayList<OBDtoFaultCheckResultContent>();

				OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();

				// #3984-6 #4: 14.08.08 sw.jung 대상 서비스 내용 포함
				content.setDetail(String.format("Client IP: %s, Service: %s:%s\n\n%s", clientIPAddress, vsvcIPAddress,
						svcPort, retInfo.getRetValue()));

				content.setSummary(OBFaultCheckMsg.SVC_DOWNLOAD_PCAP_STATUS.getFailMsg());
				contentList.add(content);

				checkResult.setResultList(contentList);

				// #3984-6 #4: 14.08.08 sw.jung 대상 서비스 내용 포함
				checkResult.setMsgCLI(String.format("Client IP: %s, Service: %s:%s\n\n%s", clientIPAddress,
						vsvcIPAddress, svcPort, retInfo.getRetValue()));

				// 결과 목록에 저장.
				resultList.add(checkResult);

				// 현재 진행 상태 업데이트.
				progressRate += calProgressRate(OBDefine.ELEMENT_TYPE_SVC, OBDefine.FAULT_CHECK_ELEMENT_SVC_DOWNLOAD,
						progressWeight);
				new OBFaultMngImpl().updateDiagnosisStatus(logKey, (int) progressRate, null, null, null,
						OBDefine.FAULT_CHECK_STATUS_NORMAL, null, null, checkResult.getMsgCLI());

				new OBFaultMonitoringDB().writeFaultLogSummary(logKey, resultList);
				return resultList;
			}

			OBDateTime.Sleep(1000);// 장비의 부하를 줄이기 위해서 1초간 정지한다.

			if (new OBFaultMngImpl().isFaultCheckCanceled(logKey) == true) {// 작업이 취소된 경우 처리.
				OBSystemLog.info(OBDefine.LOGFILE_SYSTEM, String.format("[%d/%d] fault check was canceled.",
						this.threadInfo.getAdcIndex(), this.getThreadInfo().getCheckKey()));
				return resultList;
			}
			// 덤프 파일을 분석한다.-응답 시간을 분석한다.
			curCheckMsg = String.format("%s(%d/%d)", OBFaultCheckMsg.SVC_RESPONSE_TIME_STATUS.getItemMsg(), checkCount,
					svcCheckTotal);
			svcProgressRate += (int) PROGRESS_RATE_WEIGHT_TYPE_SVC[1];
			progressRate += calProgressRate(OBDefine.ELEMENT_TYPE_SVC, OBDefine.FAULT_CHECK_ELEMENT_SVC_DOWNLOAD,
					progressWeight);
			new OBFaultMngImpl().updateDiagnosisStatus(logKey, (int) progressRate, null, null, null, svcProgressRate,
					null, curCheckMsg, OBFaultCheckMsg.SVC_DOWNLOAD_PCAP_STATUS.getActionMsg());

			element = hashMap.get(OBDefine.FAULT_CHECK_ELEMENT_SVC_RESPONSE);
			String fullPathFileName = OBDefine.PKT_DUMP_FILE_PATH + dumpFileName;
			ArrayList<String> realHostList = convertHostInfoList(new OBFaultMonitoringDB()
					.getVSvcMemberInfo(adcInfo.getIndex(), adcInfo.getAdcType(), logInfo.getVsvcIndex()));

			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("[%d/%d] try to analyze response time(%s).",
					this.threadInfo.getAdcIndex(), this.getThreadInfo().getCheckKey(), fullPathFileName));

			checkResult = ClassFaultCheckSVC.checkSvcResponseTime(element, fullPathFileName, clientIPAddress,
					vsvcIPAddress, realHostList);

			// #3984-6 #4: 14.08.08 sw.jung 대상 서비스 내용 포함
			tempContentList = checkResult.getResultList();
			for (OBDtoFaultCheckResultContent tempContent : tempContentList) {
				tempContent.setDetail(String.format("Client IP: %s, Service: %s:%s\n\n%s", clientIPAddress,
						vsvcIPAddress, svcPort, tempContent.getDetail()));
			}

			resultList.add(checkResult);

			if (checkResult.getStatus() != OBDtoFaultCheckResultElement.STATUS_FAIL) {// 상세 데이터를 저장한다.
				OBSystemLog.info(OBDefine.LOGFILE_SYSTEM, String.format("[%d/%d] try to write response time data",
						this.threadInfo.getAdcIndex(), this.getThreadInfo().getCheckKey()));
				OBDtoRespTimeAnalInfo responseInfo = (OBDtoRespTimeAnalInfo) checkResult.getExtraInfo();
				new OBFaultMonitoringDB().writeFaultLogSummaryResponseTime(logKey,
						OBDateTime.toTimestamp(OBDateTime.now()), adcInfo.getIndex(), templateInfo.getSvcVSIndex(),
						templateInfo.getSvcClientIPAddress(), responseInfo);
			}

			// 덤프 파일을 분석한다. 패킷 유실 분석한다.
			checkCount++;
			curCheckMsg = String.format("%s(%d/%d)", OBFaultCheckMsg.SVC_PACKET_LOSS_CHECK.getItemMsg(), checkCount,
					svcCheckTotal);
			svcProgressRate += (int) PROGRESS_RATE_WEIGHT_TYPE_SVC[2];
			progressRate += calProgressRate(OBDefine.ELEMENT_TYPE_SVC, OBDefine.FAULT_CHECK_ELEMENT_SVC_DOWNLOAD,
					progressWeight);
			new OBFaultMngImpl().updateDiagnosisStatus(logKey, (int) progressRate, null, null, null, svcProgressRate,
					null, curCheckMsg, OBFaultCheckMsg.SVC_DUMP_PCAP_INFO.getActionMsg());

			element = hashMap.get(OBDefine.FAULT_CHECK_ELEMENT_SVC_PKTLOSS);
			OBSystemLog.info(OBDefine.LOGFILE_SYSTEM,
					String.format("[%d/%d] try to analyze loss pkt. fileName:%s, client:%s, vsvcIP:%s, real:%s",
							this.threadInfo.getAdcIndex(), this.getThreadInfo().getCheckKey(), fullPathFileName,
							clientIPAddress, vsvcIPAddress, realHostList));
			checkResult = ClassFaultCheckSVC.checkSvcPacketLoss(element, logKey, fullPathFileName, clientIPAddress,
					vsvcIPAddress, realHostList);

			// #3984-6 #4: 14.08.08 sw.jung 대상 서비스 내용 포함
			tempContentList = checkResult.getResultList();
			for (OBDtoFaultCheckResultContent tempContent : tempContentList) {
				tempContent.setDetail(String.format("Client IP: %s, Service: %s:%s\n\n%s", clientIPAddress,
						vsvcIPAddress, svcPort, tempContent.getDetail()));
			}

			resultList.add(checkResult);

			progressRate += calProgressRate(OBDefine.ELEMENT_TYPE_SVC, OBDefine.FAULT_CHECK_ELEMENT_SVC_PKTLOSS,
					progressWeight);
			new OBFaultMngImpl().updateDiagnosisStatus(logKey, (int) progressRate, null, null, null,
					OBDefine.FAULT_CHECK_STATUS_NORMAL, null, null,
					OBFaultCheckMsg.SVC_PACKET_LOSS_CHECK.getActionMsg());

			// 점검 결과를 DB에 저장.
			new OBFaultMonitoringDB().writeFaultLogSummary(logKey, resultList);
			return resultList;
		} catch (OBException e) {
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("[%d/%d] failed to check svc items. msg:%s",
					this.threadInfo.getAdcIndex(), this.getThreadInfo().getCheckKey(), e.getErrorMessage()));
			throw e;
		} catch (Exception e) {
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
					String.format("[%d/%d] failed to check svc items. msg:null pointer error",
							this.threadInfo.getAdcIndex(), this.getThreadInfo().getCheckKey()));
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

	private ArrayList<String> convertHostInfoList(ArrayList<OBHostInfo> realHostList) throws OBException {
		ArrayList<String> retVal = new ArrayList<String>();
		if (realHostList == null)
			return retVal;
		for (OBHostInfo info : realHostList) {
			retVal.add(info.getIpAddress());
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
//			threadInfo.setCheckKey(51969613044171L);
//			threadInfo.setAdcIndex(1);
//			OBFaultCheckWorker worker = new OBFaultCheckWorker(threadInfo);
//			worker.run();
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}

	@Override
	public void run() {// 장애 검사를 한다.
		OBSystemLog.info(OBDefine.LOGFILE_SYSTEM, String.format("[%d/%d] fault check start.",
				this.threadInfo.getAdcIndex(), this.getThreadInfo().getCheckKey()));

		OBDatabase db = new OBDatabase();
		try {
			long logKey = this.threadInfo.getCheckKey();
			String clientIP = this.threadInfo.getClientIP();
			int accntIndex = this.threadInfo.getAccntIndex();
			long startTime = OBDateTime.toTimestamp(OBDateTime.now()).getTime();
			Object CLIObj = null;
			OBDtoAdcInfo adcInfo = null;
			OBDtoFaultCheckLog logInfo = null;

			db.openDB();

			try {
				// 장애 진단 정보 추출.
				logInfo = new OBFaultMngImpl().getFaultCheckLogInfo(logKey);
				if (logInfo == null || logInfo.getLogKey() == null) {
					Integer timeDiff = (int) Math.abs(OBDateTime.toTimestamp(OBDateTime.now()).getTime() - startTime);
					new OBFaultMngImpl().updateDiagnosisStatus(logKey, OBDefine.FAULT_CHECK_STATUS_FAILURE, timeDiff,
							null, null, null, OBFaultCheckMsg.CHECK_STATUS.getProcessFailMsg(),
							OBException.getExceptionMessage(OBException.ERRCODE_GET_FAULT_CHECK_INFO),
							OBException.getExceptionMessage(OBException.ERRCODE_GET_FAULT_CHECK_INFO));
					new OBSystemAuditImpl().writeLog(accntIndex, clientIP, OBSystemAuditImpl.AUDIT_FAULT_CHECK_FAIL,
							OBException.getExceptionMessage(OBException.ERRCODE_GET_FAULT_CHECK_INFO));
					OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("[%d/%d] invalid faultCheckLog info",
							this.threadInfo.getAdcIndex(), this.getThreadInfo().getCheckKey()));
					return;
				}
			} catch (OBException e) {
				Integer timeDiff = (int) Math.abs(OBDateTime.toTimestamp(OBDateTime.now()).getTime() - startTime);
				new OBFaultMngImpl().updateDiagnosisStatus(logKey, OBDefine.FAULT_CHECK_STATUS_FAILURE, timeDiff, null,
						null, null, OBFaultCheckMsg.CHECK_STATUS.getProcessFailMsg(),
						OBException.getExceptionMessage(OBException.ERRCODE_GET_FAULT_CHECK_INFO),
						OBException.getExceptionMessage(OBException.ERRCODE_GET_FAULT_CHECK_INFO));
				new OBSystemAuditImpl().writeLog(accntIndex, clientIP, OBSystemAuditImpl.AUDIT_FAULT_CHECK_FAIL,
						OBException.getExceptionMessage(OBException.ERRCODE_GET_FAULT_CHECK_INFO));
				OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
						String.format("[%d/%d] failed to get faultCheckLog info. msg:%s", this.threadInfo.getAdcIndex(),
								this.getThreadInfo().getCheckKey(), e.getErrorMessage()));
				return;
			}

			try {
				adcInfo = new OBAdcManagementImpl().getAdcInfo(logInfo.getAdcIndex());
				if (adcInfo == null) {
					Integer timeDiff = (int) Math.abs(OBDateTime.toTimestamp(OBDateTime.now()).getTime() - startTime);
					new OBFaultMngImpl().updateDiagnosisStatus(logKey, OBDefine.FAULT_CHECK_STATUS_FAILURE, timeDiff,
							null, null, null, OBFaultCheckMsg.CHECK_STATUS.getProcessFailMsg(),
							OBException.getExceptionMessage(OBException.ERRCODE_ADC_INFO),
							OBException.getExceptionMessage(OBException.ERRCODE_ADC_INFO));
					new OBSystemAuditImpl().writeLog(accntIndex, clientIP, OBSystemAuditImpl.AUDIT_FAULT_CHECK_FAIL,
							OBException.getExceptionMessage(OBException.ERRCODE_ADC_INFO));
					OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("[%d/%d] invalid adcInfo",
							this.threadInfo.getAdcIndex(), this.getThreadInfo().getCheckKey()));
					return;
				}
			} catch (OBException e) {
				Integer timeDiff = (int) Math.abs(OBDateTime.toTimestamp(OBDateTime.now()).getTime() - startTime);
				new OBFaultMngImpl().updateDiagnosisStatus(logKey, OBDefine.FAULT_CHECK_STATUS_FAILURE, timeDiff, null,
						null, null, OBFaultCheckMsg.CHECK_STATUS.getProcessFailMsg(),
						OBException.getExceptionMessage(OBException.ERRCODE_ADC_INFO),
						OBException.getExceptionMessage(OBException.ERRCODE_ADC_INFO));
				new OBSystemAuditImpl().writeLog(accntIndex, clientIP, OBSystemAuditImpl.AUDIT_FAULT_CHECK_FAIL,
						OBException.getExceptionMessage(OBException.ERRCODE_ADC_INFO));
				OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("[%d/%d] failed to get adcInfo. msg:%s",
						this.threadInfo.getAdcIndex(), this.getThreadInfo().getCheckKey(), e.getErrorMessage()));
				return;
			}

			OBDtoFaultCheckTemplate templateInfo = null;
			try {
				templateInfo = new OBFaultMngImpl().getFaultCheckTemplateInfo(logInfo.getTemplateIndex());
				new OBFaultMonitoringDB().clearFaultLogSummary(logInfo.getLogKey());
				if (templateInfo == null) {
					Integer timeDiff = (int) Math.abs(OBDateTime.toTimestamp(OBDateTime.now()).getTime() - startTime);
					new OBFaultMngImpl().updateDiagnosisStatus(logKey, OBDefine.FAULT_CHECK_STATUS_FAILURE, timeDiff,
							null, null, null, OBFaultCheckMsg.CHECK_STATUS.getProcessFailMsg(),
							OBException.getExceptionMessage(OBException.ERRCODE_GET_FAULT_TEMPLATE_INFO),
							OBException.getExceptionMessage(OBException.ERRCODE_GET_FAULT_TEMPLATE_INFO));
					new OBSystemAuditImpl().writeLog(accntIndex, clientIP, OBSystemAuditImpl.AUDIT_FAULT_CHECK_FAIL2,
							adcInfo.getName(),
							OBException.getExceptionMessage(OBException.ERRCODE_GET_FAULT_TEMPLATE_INFO));
					OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("[%d/%d] invalid template info",
							this.threadInfo.getAdcIndex(), this.getThreadInfo().getCheckKey()));
					return;
				}
			} catch (OBException e) {
				Integer timeDiff = (int) Math.abs(OBDateTime.toTimestamp(OBDateTime.now()).getTime() - startTime);
				new OBFaultMngImpl().updateDiagnosisStatus(logKey, OBDefine.FAULT_CHECK_STATUS_FAILURE, timeDiff, null,
						null, null, OBFaultCheckMsg.CHECK_STATUS.getProcessFailMsg(),
						OBException.getExceptionMessage(OBException.ERRCODE_GET_FAULT_TEMPLATE_INFO),
						OBException.getExceptionMessage(OBException.ERRCODE_GET_FAULT_TEMPLATE_INFO));
				new OBSystemAuditImpl().writeLog(accntIndex, clientIP, OBSystemAuditImpl.AUDIT_FAULT_CHECK_FAIL2,
						adcInfo.getName(),
						OBException.getExceptionMessage(OBException.ERRCODE_GET_FAULT_TEMPLATE_INFO));
				OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("[%d/%d] failed to get template info. msg:%s",
						this.threadInfo.getAdcIndex(), this.getThreadInfo().getCheckKey(), e.getErrorMessage()));
				return;
			}

			this.hwCheckTotal = templateInfo.getHwCheckItemCount() + templateInfo.getL23CheckItemCount()
					+ templateInfo.getL47CheckItemCount();

			Float progressWeight = calProgressRateWeight(templateInfo.getHwCheckItems(),
					templateInfo.getL23CheckItems(), templateInfo.getL47CheckItems(), logInfo.getCheckItem(),
					templateInfo.getSvcCheckItems());

			String failItemNameList = "";
			int hwFailCnt = 0;
			int l23FailCnt = 0;
			int l47FailCnt = 0;
			int svcFailCnt = 0;

			// cli용 채널 오픈.
			try {
				CLIObj = new OBFaultMngImpl().openCLIChannel(adcInfo);
				if (CLIObj == null) {
					Integer timeDiff = (int) Math.abs(OBDateTime.toTimestamp(OBDateTime.now()).getTime() - startTime);
					new OBFaultMngImpl().updateDiagnosisStatus(logKey, OBDefine.FAULT_CHECK_STATUS_FAILURE, timeDiff,
							null, null, null, OBFaultCheckMsg.CHECK_STATUS.getProcessFailMsg(),
							OBException.getExceptionMessage(OBException.ERRCODE_ADC_CONNECTION),
							OBException.getExceptionMessage(OBException.ERRCODE_ADC_CONNECTION));
					new OBSystemAuditImpl().writeLog(accntIndex, clientIP, OBSystemAuditImpl.AUDIT_FAULT_CHECK_FAIL2,
							adcInfo.getName(), OBException.ERRCODE_ADC_CONNECTION);
					OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("[%d/%d] invalid cli channel info.",
							this.threadInfo.getAdcIndex(), this.getThreadInfo().getCheckKey()));
					return;
				}
			} catch (OBException e) {
				Integer timeDiff = (int) Math.abs(OBDateTime.toTimestamp(OBDateTime.now()).getTime() - startTime);
				new OBFaultMngImpl().updateDiagnosisStatus(logKey, OBDefine.FAULT_CHECK_STATUS_FAILURE, timeDiff, null,
						null, null, OBFaultCheckMsg.CHECK_STATUS.getProcessFailMsg(),
						OBException.getExceptionMessage(OBException.ERRCODE_ADC_CONNECTION),
						OBException.getExceptionMessage(OBException.ERRCODE_ADC_CONNECTION));
				new OBSystemAuditImpl().writeLog(accntIndex, clientIP, OBSystemAuditImpl.AUDIT_FAULT_CHECK_FAIL2,
						adcInfo.getName(), OBException.getExceptionMessage(OBException.ERRCODE_ADC_CONNECTION));
				OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("[%d/%d] failed to open cli channel. msg:%s",
						this.threadInfo.getAdcIndex(), this.getThreadInfo().getCheckKey(), e.getErrorMessage()));
				return;
			}

			// hw 검사
			try {
				ArrayList<OBDtoFaultCheckResultElement> hwRetVal = processFaultCheckHW(logKey, logInfo, adcInfo,
						templateInfo, startTime, CLIObj, progressWeight);
				for (OBDtoFaultCheckResultElement obj : hwRetVal) {
					if (obj.getStatus() == OBDtoFaultCheckResultElement.STATUS_FAIL) {
						hwFailCnt++;
						if (!failItemNameList.isEmpty())
							failItemNameList += ", ";
						failItemNameList += obj.getObj().getName();
					}
				}
			} catch (OBException e) {
				Integer timeDiff = (int) Math.abs(OBDateTime.toTimestamp(OBDateTime.now()).getTime() - startTime);
				new OBFaultMngImpl().updateDiagnosisStatus(logKey, OBDefine.FAULT_CHECK_STATUS_FAILURE, timeDiff, null,
						null, null, OBFaultCheckMsg.CHECK_STATUS.getProcessFailMsg(), e.getErrorMessage(),
						e.getErrorMessage());
				new OBSystemAuditImpl().writeLog(accntIndex, clientIP, OBSystemAuditImpl.AUDIT_FAULT_CHECK_FAIL,
						adcInfo.getName());
				OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("[%d/%d] failed to check hw items. msg:%s",
						this.threadInfo.getAdcIndex(), this.getThreadInfo().getCheckKey(), e.getErrorMessage()));
				return;
			}

			// l23 검사
			try {
				ArrayList<OBDtoFaultCheckResultElement> l23RetVal = processFaultCheckL23(logKey, logInfo, adcInfo,
						templateInfo, startTime, CLIObj, progressWeight);
				for (OBDtoFaultCheckResultElement obj : l23RetVal) {
					if (obj.getStatus() == OBDtoFaultCheckResultElement.STATUS_FAIL) {
						hwFailCnt++;
						if (!failItemNameList.isEmpty())
							failItemNameList += ", ";
						failItemNameList += obj.getObj().getName();
					}
				}
			} catch (OBException e) {
				Integer timeDiff = (int) Math.abs(OBDateTime.toTimestamp(OBDateTime.now()).getTime() - startTime);
				new OBFaultMngImpl().updateDiagnosisStatus(logKey, OBDefine.FAULT_CHECK_STATUS_FAILURE, timeDiff, null,
						null, null, OBFaultCheckMsg.CHECK_STATUS.getProcessFailMsg(), e.getErrorMessage(),
						e.getErrorMessage());
				new OBSystemAuditImpl().writeLog(accntIndex, clientIP, OBSystemAuditImpl.AUDIT_FAULT_CHECK_FAIL,
						adcInfo.getName());
				OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("[%d/%d] failed to check l2-l3 items. msg:%s",
						this.threadInfo.getAdcIndex(), this.getThreadInfo().getCheckKey(), e.getErrorMessage()));
				return;
			}

			// l47 검사
			try {
				ArrayList<OBDtoFaultCheckResultElement> l47RetVal = processFaultCheckL47(logKey, logInfo, adcInfo,
						templateInfo, startTime, CLIObj, progressWeight);
				for (OBDtoFaultCheckResultElement obj : l47RetVal) {
					if (obj.getStatus() == OBDtoFaultCheckResultElement.STATUS_FAIL) {
						hwFailCnt++;
						if (!failItemNameList.isEmpty())
							failItemNameList += ", ";
						failItemNameList += obj.getObj().getName();
					}
				}
			} catch (OBException e) {
				Integer timeDiff = (int) Math.abs(OBDateTime.toTimestamp(OBDateTime.now()).getTime() - startTime);
				new OBFaultMngImpl().updateDiagnosisStatus(logKey, OBDefine.FAULT_CHECK_STATUS_FAILURE, timeDiff, null,
						null, null, OBFaultCheckMsg.CHECK_STATUS.getProcessFailMsg(), e.getErrorMessage(),
						e.getErrorMessage());
				new OBSystemAuditImpl().writeLog(accntIndex, clientIP, OBSystemAuditImpl.AUDIT_FAULT_CHECK_FAIL,
						adcInfo.getName());
				OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("[%d/%d] failed to check l4-l7 items. msg:%s",
						this.threadInfo.getAdcIndex(), this.getThreadInfo().getCheckKey(), e.getErrorMessage()));
				return;
			}

			// svc 검사
			try {
				ArrayList<OBDtoFaultCheckResultElement> svcRetVal = processFaultCheckSvc(logKey, logInfo, adcInfo,
						templateInfo, startTime, CLIObj, progressWeight);
				for (OBDtoFaultCheckResultElement obj : svcRetVal) {
					if (obj.getStatus() == OBDtoFaultCheckResultElement.STATUS_FAIL) {
						hwFailCnt++;
						if (!failItemNameList.isEmpty())
							failItemNameList += ", ";
						failItemNameList += obj.getObj().getName();
					}
				}
			} catch (OBException e) {
				Integer timeDiff = (int) Math.abs(OBDateTime.toTimestamp(OBDateTime.now()).getTime() - startTime);
				new OBFaultMngImpl().updateDiagnosisStatus(logKey, OBDefine.FAULT_CHECK_STATUS_FAILURE, timeDiff, null,
						null, null, OBFaultCheckMsg.CHECK_STATUS.getProcessFailMsg(), e.getErrorMessage(),
						e.getErrorMessage());
				new OBSystemAuditImpl().writeLog(accntIndex, clientIP, OBSystemAuditImpl.AUDIT_FAULT_CHECK_FAIL,
						adcInfo.getName());
				OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("[%d/%d] failed to check svc items. msg:%s",
						this.threadInfo.getAdcIndex(), this.getThreadInfo().getCheckKey(), e.getErrorMessage()));
				return;
			}

			// 오픈된 채널을 종료한다.
			new OBFaultMngImpl().closeCLIChannel(adcInfo, CLIObj);

			try {
				if (new OBFaultMngImpl().isFaultCheckCanceled(logKey) == false) {// 진행중일 경우에만... 취소등의 사유에 대해서는 저장하지 않음.
																					// 결과 저장..
					int totalFailCnt = hwFailCnt + l23FailCnt + l47FailCnt;
					if (svcFailCnt > 0) {
						totalFailCnt++;
					}
					if (totalFailCnt > 0) {
						String summary = String.format(OBFaultCheckMsg.CHECK_STATUS.getFailMsg(), totalFailCnt);
						Integer timeDiff = (int) Math
								.abs(OBDateTime.toTimestamp(OBDateTime.now()).getTime() - startTime);
						new OBFaultMngImpl().updateDiagnosisStatus(logKey, OBDefine.FAULT_CHECK_STATUS_ABNORMAL,
								timeDiff, null, null, null, summary, summary, failItemNameList);
					} else {
						Integer timeDiff = (int) Math
								.abs(OBDateTime.toTimestamp(OBDateTime.now()).getTime() - startTime);
						new OBFaultMngImpl().updateDiagnosisStatus(logKey, OBDefine.FAULT_CHECK_STATUS_NORMAL, timeDiff,
								null, null, null, OBFaultCheckMsg.CHECK_STATUS.getSuccessMsg(),
								OBFaultCheckMsg.CHECK_STATUS.getSuccessMsg(),
								OBFaultCheckMsg.CHECK_STATUS.getSuccessMsg());
					}
					new OBSystemAuditImpl().writeLog(accntIndex, clientIP, OBSystemAuditImpl.AUDIT_FAULT_CHECK_SUCCESS,
							adcInfo.getName());
				} else {
					Integer timeDiff = (int) Math.abs(OBDateTime.toTimestamp(OBDateTime.now()).getTime() - startTime);
					new OBFaultMngImpl().updateDiagnosisStatus(logKey, OBDefine.FAULT_CHECK_STATUS_CANCEL, timeDiff,
							null, null, null, "", "", "");
				}
			} catch (OBException e) {
				Integer timeDiff = (int) Math.abs(OBDateTime.toTimestamp(OBDateTime.now()).getTime() - startTime);
				new OBFaultMngImpl().updateDiagnosisStatus(logKey, OBDefine.FAULT_CHECK_STATUS_FAILURE, timeDiff, null,
						null, null, OBFaultCheckMsg.CHECK_STATUS.getProcessFailMsg(), e.getErrorMessage(),
						e.getErrorMessage());
				new OBSystemAuditImpl().writeLog(accntIndex, clientIP, OBSystemAuditImpl.AUDIT_FAULT_CHECK_FAIL,
						adcInfo.getName());
				OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
						String.format("[%d/%d] failed to write check results. msg:%s", this.threadInfo.getAdcIndex(),
								this.getThreadInfo().getCheckKey(), e.getErrorMessage()));
				return;
			} catch (Exception e) {
				Integer timeDiff = (int) Math.abs(OBDateTime.toTimestamp(OBDateTime.now()).getTime() - startTime);
				new OBFaultMngImpl().updateDiagnosisStatus(logKey, OBDefine.FAULT_CHECK_STATUS_FAILURE, timeDiff, null,
						null, null, OBFaultCheckMsg.CHECK_STATUS.getProcessFailMsg(), e.getMessage(), e.getMessage());
				new OBSystemAuditImpl().writeLog(accntIndex, clientIP, OBSystemAuditImpl.AUDIT_FAULT_CHECK_FAIL,
						adcInfo.getName());
				OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
						String.format("[%d/%d] failed to write check results. msg:invalid null pointer error",
								this.threadInfo.getAdcIndex(), this.getThreadInfo().getCheckKey()));
				return;
			}
		} catch (Exception e) {
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("[%d/%d] failed to check fault diagnosis",
					this.threadInfo.getAdcIndex(), this.getThreadInfo().getCheckKey()));
			return;
		} finally {
			if (db != null)
				db.closeDB();
		}
		OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("[%d/%d] success to check fault diagnosis",
				this.threadInfo.getAdcIndex(), this.getThreadInfo().getCheckKey()));
	}

	private Float calProgressRateWeight(ArrayList<OBDtoElement> hwList, ArrayList<OBDtoElement> l23List,
			ArrayList<OBDtoElement> l47List, Integer iTemType, ArrayList<OBDtoElement> svcList) {
		Float retVal = 1.0f;
		float hwWeight = 0.0f;
		for (OBDtoElement obj : hwList) {
			if (obj.getState() == OBDefine.STATE_ENABLE) {
				try {
					hwWeight += PROGRESS_RATE_WEIGHT_TYPE_HW[obj.getIndex() - 1];
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		float l23Weight = 0.0f;
		for (OBDtoElement obj : l23List) {
			if (obj.getState() == OBDefine.STATE_ENABLE) {
				try {
					l23Weight += PROGRESS_RATE_WEIGHT_TYPE_L23[obj.getIndex() - 1];
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		float l47Weight = 0.0f;
		for (OBDtoElement obj : l47List) {
			if (obj.getState() == OBDefine.STATE_ENABLE) {
				try {
					l47Weight += PROGRESS_RATE_WEIGHT_TYPE_L47[obj.getIndex() - 1];
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		float svcWeight = 0.0f;
		for (OBDtoElement obj : svcList) {
			if ((iTemType & OBDtoFaultCheckLog.CHECK_ITEM_SVC) > 0) {
				try {
					svcWeight += PROGRESS_RATE_WEIGHT_TYPE_SVC[obj.getIndex() - 1];
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		retVal = hwWeight * PROGRESS_RATE_WEIGHT_TYPE[0] / 100.0f;
		retVal += l23Weight * PROGRESS_RATE_WEIGHT_TYPE[1] / 100.0f;
		retVal += l47Weight * PROGRESS_RATE_WEIGHT_TYPE[2] / 100.0f;
		retVal += svcWeight * PROGRESS_RATE_WEIGHT_TYPE[3] / 100.0f;

		if (retVal > 0)
			retVal = 100.0f / retVal;
		else
			retVal = 100.0f;
		return retVal;
	}

	private static final float PROGRESS_RATE_WEIGHT_TYPE[] = { 12.5f, 8.75f, 3.75f, 75.0f };
	private static final float PROGRESS_RATE_WEIGHT_TYPE_HW[] = { 11.1f, 11.1f, 11.1f, 11.1f, 11.1f, 11.1f, 11.1f,
			11.1f, 11.1f, 0.1f };
	private static final float PROGRESS_RATE_WEIGHT_TYPE_L23[] = { 16.5f, 16.5f, 16.5f, 16.5f, 16.5f, 16.5f };
	private static final float PROGRESS_RATE_WEIGHT_TYPE_L47[] = { 33.3f, 33.3f, 33.3f };
	private static final float PROGRESS_RATE_WEIGHT_TYPE_SVC[] = { 70.0f, 10.0f, 10.0f, 10.0f };

	private Float calProgressRate(Integer checkType, Integer itemIndex, float weight) {
		Float retVal = 0.0f;

		try {
			if (checkType == OBDefine.ELEMENT_TYPE_HW) {
				retVal = PROGRESS_RATE_WEIGHT_TYPE_HW[itemIndex - 1] * PROGRESS_RATE_WEIGHT_TYPE[checkType - 1] * weight
						/ 100.0f;
			} else if (checkType == OBDefine.ELEMENT_TYPE_L23) {
				retVal = PROGRESS_RATE_WEIGHT_TYPE_L23[itemIndex - 1] * PROGRESS_RATE_WEIGHT_TYPE[checkType - 1]
						* weight / 100.0f;
			} else if (checkType == OBDefine.ELEMENT_TYPE_L47) {
				retVal = PROGRESS_RATE_WEIGHT_TYPE_L47[itemIndex - 1] * PROGRESS_RATE_WEIGHT_TYPE[checkType - 1]
						* weight / 100.0f;
			} else if (checkType == OBDefine.ELEMENT_TYPE_SVC) {
				retVal = PROGRESS_RATE_WEIGHT_TYPE_SVC[itemIndex - 1] * PROGRESS_RATE_WEIGHT_TYPE[checkType - 1]
						* weight / 100.0f;
			}
		} catch (Exception e) {
		}
		return retVal;
	}

}
