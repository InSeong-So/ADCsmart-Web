package kr.openbase.adcsmart.web.facade.system;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import kr.openbase.adcsmart.service.OBEnvManagement;
import kr.openbase.adcsmart.service.OBLicenseManagement;
import kr.openbase.adcsmart.service.dto.OBDtoAdcLogFilter;
import kr.openbase.adcsmart.service.dto.OBDtoExtraInfo;
import kr.openbase.adcsmart.service.dto.OBDtoLicense;
import kr.openbase.adcsmart.service.dto.OBDtoLicenseInfo;
import kr.openbase.adcsmart.service.dto.OBDtoScheduleBackupInfo;
import kr.openbase.adcsmart.service.dto.OBDtoSyncSystemTime;
import kr.openbase.adcsmart.service.dto.OBDtoSystemEnvAdditional;
import kr.openbase.adcsmart.service.dto.OBDtoSystemEnvNetwork;
import kr.openbase.adcsmart.service.dto.OBDtoSystemEnvView;
import kr.openbase.adcsmart.service.impl.OBAdcManagementImpl;
import kr.openbase.adcsmart.service.impl.OBEnvManagementImpl;
import kr.openbase.adcsmart.service.impl.OBLicenseImpl;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.web.facade.dto.AdcLogFilterDto;
import kr.openbase.adcsmart.web.facade.dto.LicenseInfoDto;
import kr.openbase.adcsmart.web.facade.dto.ScheduleBackupInfoDto;
import kr.openbase.adcsmart.web.facade.dto.SessionDto;
import kr.openbase.adcsmart.web.facade.dto.SystemEnvAdditionalDto;
import kr.openbase.adcsmart.web.facade.dto.SystemEnvNetworkDto;
import kr.openbase.adcsmart.web.facade.dto.SystemEnvViewDto;
import kr.openbase.adcsmart.web.util.OBDefineWeb;

@Component
public class ConfigSettingFacade {
	private transient Logger log = LoggerFactory.getLogger(ConfigSettingFacade.class);

	private OBEnvManagement envManage;
	private OBLicenseManagement licManage;
//	private OBAdcManagement adcManage;

	public ConfigSettingFacade() {
		envManage = new OBEnvManagementImpl(); // 환경설정
		licManage = new OBLicenseImpl(); // 라이선스
//		adcManage = new OBAdcManagementImpl();
	}

	public Integer getEnvLoginAccess() throws OBException, Exception {
		return envManage.getEnvLoginAccess();
	}

	// 환경설정 - 기본설정
	public List<SystemEnvNetworkDto> getNetworkConfigList() throws OBException, Exception {
		List<SystemEnvNetworkDto> eNetworks = new ArrayList<SystemEnvNetworkDto>();
//		   for (OBDtoSystemEnvNetwork eNetworkSvc : envManage.getSystemConfig()) //Can only iterate over an array or an instance of java.lang.Iterable
//		    eNetworks.add(toSystemEnvNetworkDto(eNetworkSvc));   
		OBDtoSystemEnvNetwork eNetworkSvc = envManage.getSystemConfig().getNetworkInfo();
		if (null != eNetworkSvc)
			eNetworks.add(toSystemEnvNetworkDto(eNetworkSvc));
		return eNetworks;
	}

	private SystemEnvNetworkDto toSystemEnvNetworkDto(OBDtoSystemEnvNetwork eNetworkSvc) throws OBException, Exception {
		SystemEnvNetworkDto eNetwork = new SystemEnvNetworkDto();
		eNetwork.setIpAddress(eNetworkSvc.getIpAddress());
		eNetwork.setNetmask(eNetworkSvc.getNetmask());
		eNetwork.setGateway(eNetworkSvc.getGateway());
		eNetwork.setHostName(eNetworkSvc.getHostName());

		return eNetwork;
	}

	// 환경설정 - 부가기능 설정
	public List<SystemEnvAdditionalDto> getAdditionalConfigList() throws OBException, Exception {
		List<SystemEnvAdditionalDto> eAdditionals = new ArrayList<SystemEnvAdditionalDto>();
		OBDtoSystemEnvAdditional eAdditionalSvc = envManage.getSystemConfig().getAdditionalInfo();
		if (null != eAdditionalSvc)
			eAdditionals.add(toSystemEnvAdditionalDto(eAdditionalSvc));
		return eAdditionals;
	}

	private SystemEnvAdditionalDto toSystemEnvAdditionalDto(OBDtoSystemEnvAdditional eAdditionalSvc)
			throws OBException, Exception {
		SystemEnvAdditionalDto eAdditional = new SystemEnvAdditionalDto();
//		eAdditional.setIntervalConfSync(eAdditionalSvc.getIntervalAdcConfSync());
		eAdditional.setIntervalAdcConfSync(eAdditionalSvc.getIntervalAdcConfSync());
		eAdditional.setIntervalSystemInfo(eAdditionalSvc.getIntervalSystemInfo());
		eAdditional.setIsTimeSync(eAdditionalSvc.getIsTimeSync());
		eAdditional.setSyslogPort(eAdditionalSvc.getSyslogPort());
		eAdditional.setTimeServerAddress(eAdditionalSvc.getTimeServerAddress());
		/*
		 * eAdditional.setAlertShowYn(eAdditionalSvc.getAlertShow() != null &&
		 * eAdditionalSvc.getAlertShow() == 1 ? true : false);
		 */
		eAdditional.setSyslogServerAddress(eAdditionalSvc.getSyslogServerAddress());
		// alteonAutoSave
		eAdditional.setAlteonAutoSaveYn(
				eAdditionalSvc.getAlteonAutoSave() != null && eAdditionalSvc.getAlteonAutoSave() == OBDefineWeb.CFG_ON
						? true
						: false);
		eAdditional.setAlarmPopupYn(
				eAdditionalSvc.getAlarmPopupYn() != null && eAdditionalSvc.getAlarmPopupYn() == OBDefineWeb.CFG_ON
						? true
						: false);

		eAdditional.setLoginAccessYn(eAdditionalSvc.getDoubleLoginAccess() != null
				&& eAdditionalSvc.getDoubleLoginAccess() == OBDefineWeb.CFG_ON ? true : false);

		// serviceResponsTime On/Off
		eAdditional.setServiceRespTime(eAdditionalSvc.getServiceResponseTime() != null
				&& eAdditionalSvc.getServiceResponseTime() == OBDefineWeb.CFG_ON ? true : false);

		// snmpTrap
		eAdditional.setSnmpTrap(eAdditionalSvc.getSnmpTrap());

//		eAdditional.setSnmpTrapServerAddress(eAdditionalSvc.getSnmpTrapServerAddress());
//        eAdditional.setSnmpTrapPort(eAdditional.getSnmpTrapPort());
//        eAdditional.setSnmpTrapCommunity(eAdditionalSvc.getSnmpTrapCommunity());
//        eAdditional.setSnmpTrapVersion(eAdditional.getSnmpTrapVersion());

		// respTimeSection과 respTimeInterval
		eAdditional.setRespTimeInterval(eAdditionalSvc.getRespTimeInterval());
		eAdditional.setRespTimeSection(
				eAdditionalSvc.getRespTimeSection() != null && eAdditionalSvc.getRespTimeSection() == OBDefineWeb.CFG_ON
						? true
						: false);

		// ADCsmart Snmp
		eAdditional.setSnmpCommunity(eAdditionalSvc.getSnmpCommunity());

		// sms 설정
		eAdditional.setSmsActionYn(
				eAdditionalSvc.getSmsActionYn() != null && eAdditionalSvc.getSmsActionYn() == OBDefineWeb.CFG_ON ? true
						: false);
		eAdditional.setSmsActionType(eAdditionalSvc.getSmsActionType());
		eAdditional.setSmsHPNumbers(eAdditionalSvc.getSmsHPNumbers());
		return eAdditional;
	}

	// 환경설정 - 화면 표시 설정
	public List<SystemEnvViewDto> getViewConfigList() throws OBException, Exception {
		List<SystemEnvViewDto> eViews = new ArrayList<SystemEnvViewDto>();
		OBDtoSystemEnvView eViewSvc = envManage.getSystemConfig().getViewInfo();
		if (null != eViewSvc)
			eViews.add(toSystemEnvViewDto(eViewSvc));
		return eViews;
	}

	private SystemEnvViewDto toSystemEnvViewDto(OBDtoSystemEnvView eViewSvc) throws OBException, Exception {
		SystemEnvViewDto eView = new SystemEnvViewDto();
		eView.setAutoLogoutTime(eViewSvc.getAutoLogoutTime());
		eView.setAutoRefrash(eViewSvc.getAutoRefrash());
		eView.setLogViewCount(eViewSvc.getLogViewCount());
		eView.setLogViewPeriodType(eViewSvc.getLogViewPeriodType());

		return eView;
	}

	// 환경설정 - 백업예약 설정
	public List<ScheduleBackupInfoDto> getScheduleBackupConfigList() throws OBException, Exception {
		List<ScheduleBackupInfoDto> schBackups = new ArrayList<ScheduleBackupInfoDto>();
		OBDtoScheduleBackupInfo schBackupSvc = envManage.getSystemConfig().getSchBackupInfo();
		if (null != schBackupSvc)
			schBackups.add(toScheduleBackupInfoDto(schBackupSvc));
		return schBackups;
	}

	private ScheduleBackupInfoDto toScheduleBackupInfoDto(OBDtoScheduleBackupInfo schBackupSvc)
			throws OBException, Exception {
		ScheduleBackupInfoDto schBackup = new ScheduleBackupInfoDto();
//		schBackup.setUseYn(schBackupSvc.getUseYN());
		schBackup.setUseYn(
				schBackupSvc.getUseYN() != null && schBackupSvc.getUseYN() == OBDefineWeb.CFG_SCH_BACKUP_ON ? true
						: false);
		schBackup.setIntervalDay(schBackupSvc.getIntervalDay());
		schBackup.setStartTime(schBackupSvc.getStartTime());
		schBackup.setEndTime(schBackupSvc.getEndTime());
		schBackup.setBackupTime(schBackupSvc.getBackupTime());
		schBackup.setBackOption(schBackupSvc.getOption());
//		schBackup.setLogDelYN(schBackupSvc.getLogDelYN());
		schBackup.setLogDelYn(
				schBackupSvc.getLogDelYN() != null && schBackupSvc.getLogDelYN() == OBDefineWeb.CFG_BACKUP_LOGDEL_ON
						? true
						: false);

		return schBackup;
	}

	public OBDtoSyncSystemTime getSyncSystemTimeConfig() throws OBException, Exception {
		return envManage.getSyncSystemTimeConfig();
	}

	// updateSystemConfig
	public void modifyConfigContent(SystemEnvNetworkDto enetwork, SessionDto session) throws OBException, Exception {
//			OBDtoExtraInfo extraInfo = session.toOBDtoExtraInfo();
//			extraInfo.setExtraMsg1(enetwork.getHostName());
//			envManage.setNetworkConfig(SystemEnvNetworkDto.toOBDtoSystemEnvNetwork(enetwork) , extraInfo);
	}

	public void modifyConfigContent(SystemEnvViewDto eview, SessionDto session) throws OBException, Exception {
		OBDtoExtraInfo extraInfo = session.toOBDtoExtraInfo();
//			extraInfo.setExtraMsg1(eNetwork.getHostName());
		envManage.setViewConfig(SystemEnvViewDto.toOBDtoSystemEnvView(eview), extraInfo);
	}

	public void modifyConfigContent(SystemEnvAdditionalDto eadditional, SessionDto session)
			throws OBException, Exception {
		OBDtoExtraInfo extraInfo = session.toOBDtoExtraInfo();
		envManage.setAdditionalConfig(SystemEnvAdditionalDto.toSystemEnvAdditional(eadditional), extraInfo);
	}

	public void modifyConfigContent(ScheduleBackupInfoDto sbackup, SessionDto session) throws OBException, Exception {
		OBDtoExtraInfo extraInfo = session.toOBDtoExtraInfo();
		envManage.setScheduleBackupInfo(ScheduleBackupInfoDto.toScheduleBackupInfo(sbackup), extraInfo);
	}

//	public void modifyConfigContent(AdcLogFilterDto lfilter, SessionDto session) throws Exception {
	public void modifyConfigContent(ArrayList<AdcLogFilterDto> addFilter, ArrayList<Integer> delFilter,
			SessionDto session) throws OBException, Exception {
		log.debug("addFilter: {}", addFilter);
		log.debug("delFilter: {}", delFilter);
		OBDtoExtraInfo extraInfo = session.toOBDtoExtraInfo();
//			extraInfo.setExtraMsg1(lfilter.getUserPattern());
//			OBDtoAdcLogFilter adcLogFilterSvc = AdcLogFilterDto.toAdcLogFilter(lfilter);

		// addFilter
		for (int i = 0; i < addFilter.size(); i++) {
			AdcLogFilterDto data = addFilter.get(i);
			extraInfo.setExtraMsg1(data.getUserPattern());
			OBDtoAdcLogFilter adcLogFilterSvc = AdcLogFilterDto.toAdcLogFilter(data);
			envManage.addAdcLogFitlerPattern(adcLogFilterSvc, extraInfo);
		}
		// delFilter
		if (delFilter.size() > 0) {
			envManage.delAdcLogFilterPattern(delFilter, extraInfo);
		}

//			if () {		//추가 후 적용
//				envManage.addAdcLogFitlerPattern(adcLogFilterSvc, extraInfo);
//			} else {	//삭제 후 적용
//				envManage.delAdcLogFilterPattern(arg0, extraInfo);
//			}
	}

	public boolean setSyncSystemTimeConfig(OBDtoSyncSystemTime syncSystemTimeInfo, SessionDto session)
			throws OBException, Exception {
		OBDtoExtraInfo extraInfo = session.toOBDtoExtraInfo();
		return envManage.setSyncSystemTimeConfig(syncSystemTimeInfo, extraInfo);
	}

	// ADC Log 필터링
	public List<AdcLogFilterDto> getAdcLogFilterList() throws OBException, Exception {
		List<AdcLogFilterDto> logFilters = new ArrayList<AdcLogFilterDto>();
		ArrayList<OBDtoAdcLogFilter> logFilterSvc = envManage.getAdcLogFilterPatternList();
//			if (null != logFilterSvc) logFilters.add(toAdcLogFilterDto(logFilterSvc));
//			log.debug("{}", logFilterSvc);

		OBDtoAdcLogFilter adcLogFilter = null;

		if (null != logFilterSvc) {
			for (int i = 0; i < logFilterSvc.size(); i++) {
				adcLogFilter = logFilterSvc.get(i);
				logFilters.add(toAdcLogFilterDto(adcLogFilter));
			}
		}
		return logFilters;
	}

	private AdcLogFilterDto toAdcLogFilterDto(OBDtoAdcLogFilter logFilterSvc) throws OBException, Exception {
		AdcLogFilterDto logFilter = new AdcLogFilterDto();
		logFilter.setIndex(logFilterSvc.getIndex());
		logFilter.setRegPattern(logFilterSvc.getRegPattern());
		logFilter.setUserPattern(logFilterSvc.getUserPattern());
		logFilter.setType(logFilterSvc.getType());
		return logFilter;
	}

	// 라이선스
	public LicenseInfoDto getLicenseGeneralInfo() throws OBException, Exception {
		try {
			OBDtoLicenseInfo licInfoSvc = licManage.getLicenseGeneralInfo();
			Integer usedAdcCnt = new OBAdcManagementImpl().getUsedAdcCnt();
			Integer usedVsCnt = new OBAdcManagementImpl().getUsedVSCnt();
			Integer usedUserCnt = new OBAdcManagementImpl().getUsedUserCnt();
			if (null != licInfoSvc) {
				return toLicenseInfoDto(licInfoSvc, usedAdcCnt, usedVsCnt, usedUserCnt);
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private LicenseInfoDto toLicenseInfoDto(OBDtoLicenseInfo licInfoSvc, Integer usedAdcCnt, Integer usedVSCnt,
			Integer usedUserCnt) {
		LicenseInfoDto licInfo = new LicenseInfoDto();

		switch (licInfoSvc.getLicStatus()) {
		case OBDtoLicense.LIC_SUCCESS:
			licInfo.setState(OBDefineWeb.getDefineWeb(OBDefineWeb.MSG_DEFINEWEB_LICSUCCESS));
			break;
		case OBDtoLicense.LIC_FORMAT_ERROR:
			licInfo.setState(OBDefineWeb.getDefineWeb(OBDefineWeb.MSG_DEFINEWEB_LICFORMAT_ERROR));
			break;
		case OBDtoLicense.LIC_FILE_ERROR:
			licInfo.setState(OBDefineWeb.getDefineWeb(OBDefineWeb.MSG_DEFINEWEB_LICFILE_ERROR));
			break;
		case OBDtoLicense.LIC_INTEGRITY_ERROR:
			licInfo.setState(OBDefineWeb.getDefineWeb(OBDefineWeb.MSG_DEFINEWEB_LICINTEGRITY_ERROR));
			break;
		case OBDtoLicense.LIC_TYPE_ERROR:
			licInfo.setState(OBDefineWeb.getDefineWeb(OBDefineWeb.MSG_DEFINEWEB_LICTYPE_ERROR));
			break;
		case OBDtoLicense.LIC_VERSION_ERROR:
			licInfo.setState(OBDefineWeb.getDefineWeb(OBDefineWeb.MSG_DEFINEWEB_LICVERSION_ERROR));
			break;
		case OBDtoLicense.LIC_IP_ERROR:
			licInfo.setState(OBDefineWeb.getDefineWeb(OBDefineWeb.MSG_DEFINEWEB_LICIP_ERROR));
			break;
		case OBDtoLicense.LIC_MAC_ERROR:
			licInfo.setState(OBDefineWeb.getDefineWeb(OBDefineWeb.MSG_DEFINEWEB_LICMAC_ERROR));
			break;
		case OBDtoLicense.LIC_TIME_ERROR:
			licInfo.setState(OBDefineWeb.getDefineWeb(OBDefineWeb.MSG_DEFINEWEB_LICTIME_ERROR));
			break;
		case OBDtoLicense.LIC_ARGUMENT_ERROR:
			licInfo.setState(OBDefineWeb.getDefineWeb(OBDefineWeb.MSG_DEFINEWEB_LICARGUMENT_ERROR));
			break;
		case OBDtoLicense.LIC_UNKOWN:
			licInfo.setState(OBDefineWeb.getDefineWeb(OBDefineWeb.MSG_DEFINEWEB_LICUNKOWN));
			break;
		}

		licInfo.setVersion(licInfoSvc.getVersion());
		licInfo.setModel(licInfoSvc.getModel());
		licInfo.setSerial(licInfoSvc.getSerial());
		licInfo.setMaxAdcNum(licInfoSvc.getMaxAdcNum() + "(" + usedAdcCnt + ")");
		licInfo.setMaxVSnum(licInfoSvc.getMaxVSnum() + "(" + usedVSCnt + ")");
		licInfo.setMaxUserNum(licInfoSvc.getMaxUserNum() + "(" + usedUserCnt + ")");
		licInfo.setPeriod(licInfoSvc.getPeriod());
		licInfo.setMacAddress(licInfoSvc.getMacAddress());
		licInfo.setIssueDate(licInfoSvc.getIssueDate());
		// licInfo.setLicState(licInfoSvc.getLicStatus());
		log.debug("{}", licInfo);

		return licInfo;
	}

//	public void updateLicenseConfig(LicenseInfoDto license, SessionDto session) throws Exception {
	// 오프라인 라이센스 업데이트
	public void updateLicenseConfig(String filename, SessionDto session) throws OBException, Exception {
		OBDtoExtraInfo extraInfo = session.toOBDtoExtraInfo();
//			licManage.offlineUpdateLicense("c:\\license.lic", extraInfo);
//			licManage.offlineUpdateLicense("\\opt\\adcsmart\\cfg", extraInfo);

//			licManage.isValidLicenseFormat(filename);
		licManage.offlineUpdateLicense(filename, extraInfo);
	}

	// 라이센스 포멧 형식을 검사
	public void validLicenseFormat(String fullPathName) throws OBException, Exception {
		licManage.isValidLicenseFormat(fullPathName);
		log.debug("{}", fullPathName);
	}

//	public void makeBasePath(String path) {
//		File dir = new File(path);
//		if(!dir.exists()) {
//			dir.mkdirs();
//		}
//	}
//	
	public String saveFile(File file, String basePath, String fileName) throws OBException, Exception {
		if (null == file || file.getName().equals("") || file.length() <= 0) {
			return null;
		}

//		makeBasePath(basePath);   //base 폴더 생성

//		String serverFullPath = basePath + "\\" + fileName;	//저장할 전체경로	
		String serverFullPath = basePath + "/" + fileName; // 저장할 전체경로
//		String serverFullPath = basePath + fileName;	//저장할 전체경로		

		log.debug("{}", serverFullPath);

		FileInputStream inputStream = new FileInputStream(file);
		FileOutputStream outputStream = new FileOutputStream(serverFullPath);

		int bytesRead = 0;
		byte[] buffer = new byte[1024];
		while ((bytesRead = inputStream.read(buffer, 0, 1024)) != -1) {
			outputStream.write(buffer, 0, bytesRead);
		}

		outputStream.close();
		inputStream.close();
		return serverFullPath;
	}

}
