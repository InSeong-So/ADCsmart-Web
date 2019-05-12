package kr.openbase.adcsmart.web.facade.dto;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import kr.openbase.adcsmart.service.dto.OBDtoAdcInfo;
import kr.openbase.adcsmart.service.dto.OBDtoAdcSnmpInfo;
import kr.openbase.adcsmart.service.utility.OBCipherAES;
import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.web.util.NumberUtil;
import kr.openbase.adcsmart.web.util.OBDefineWeb;

public class AdcDto {
	private Integer index;
	private String accountId;
	private String password;
	private String cliAccountId;
	private String cliPassword;
	private String status;
	private String name;
	private String type;
	private String ip;
	private String peerip;
	private Integer groupIndex;
	private Date lastUpdateTime;
	private String version;
	private String description;
	private String snmpCommunity;
	private Boolean isStandby = false;
	private Date saveTime;
	private Date purchaseDate; // 장비 구매일
	private Integer connService;
	private Integer connPort;
	private Date lastDisconnTime; // 가장 최근에 단절된 시간
	private Integer activeStandbyState; // active/standby 상태
	private Integer activeStandbyDir; // active/standby 장비를 icon을 이용해서 묶어 표현한다. 장비를 묶기 위한 용도로 사용됨.
	private Integer model;
	private String syslogip; // Secodary syslog IP
	private Integer isFlb;
	private Integer opMode; // 운영모드
	private AdcSnmpInfoDto adcSnmpInfo;
	private Integer mgmtMode; // 데이터 모드
	private boolean ssoModeType = false; // false : TACACS 모드 설정 off (default)
	private Integer ssoMode; // TACACS 모드 설정
	private Integer connProtocol; // protocol (TCP / ICMP)

	public static AdcDto toAdcDto(OBDtoAdcInfo adcFromSvc) {
		AdcDto adc = new AdcDto();
		adc.setIndex(adcFromSvc.getIndex());
		adc.setAccountId(adcFromSvc.getAdcAccount());
		adc.setPassword(adcFromSvc.getAdcPasswordDecrypt());
		if (adcFromSvc.getStatus() == null)
			adc.setStatus("unknown");

		switch (adcFromSvc.getStatus()) {
		case OBDefine.STATUS_AVAILABLE:
			adc.setStatus("available");
			break;
		case OBDefine.STATUS_DISABLE:
			adc.setStatus("disable");
			break;
		case OBDefine.STATUS_UNAVAILABLE:
			adc.setStatus("unavailable");
			break;
		case OBDefine.STATUS_BLOCK:
			adc.setStatus("block");
			break;
		default:
			adc.setStatus("unknown");
		}

		adc.setName(adcFromSvc.getName());

		if (adcFromSvc.getAdcType() == Integer.valueOf(OBDefine.ADC_TYPE_F5)) {
			adc.setCliAccountId(adcFromSvc.getAdcCliAccount());
			adc.setCliPassword(adcFromSvc.getAdcCliPasswordDecrypt());
			adc.setType("F5");
		} else if (adcFromSvc.getAdcType() == Integer.valueOf(OBDefine.ADC_TYPE_ALTEON)) {
			adc.setCliAccountId(adcFromSvc.getAdcAccount());
			adc.setCliPassword(adcFromSvc.getAdcCliPasswordDecrypt());
			adc.setType("Alteon");
		} else if (adcFromSvc.getAdcType() == Integer.valueOf(OBDefine.ADC_TYPE_PIOLINK_PAS)) {
			adc.setType("PAS");
		} else if (adcFromSvc.getAdcType() == Integer.valueOf(OBDefine.ADC_TYPE_PIOLINK_PASK)) {
			adc.setType("PASK");
		} else if (adcFromSvc.getAdcType() == Integer.valueOf(OBDefine.ADC_TYPE_PIOLINK_UNKNOWN)) {
			adc.setType("PiolinkUnknown");
		}

		adc.setIp(adcFromSvc.getAdcIpAddress());
		adc.setPeerip(adcFromSvc.getPeerAdcIPAddress());
		adc.setGroupIndex((int) adcFromSvc.getGroupIndex());
		adc.setVersion(adcFromSvc.getSwVersion());
		adc.setDescription(adcFromSvc.getDescription());
		adc.setLastUpdateTime(adcFromSvc.getApplyTime());
		adc.setIsStandby(
				(adcFromSvc.getActivePairIndex() == null || adcFromSvc.getActivePairIndex() == 0) ? false : true);
		adc.setSaveTime(adcFromSvc.getSaveTime());
		adc.setPurchaseDate(adcFromSvc.getPurchaseDate());
		adc.setConnService(adcFromSvc.getConnService());
		adc.setConnPort(adcFromSvc.getConnPort());
		adc.setOpMode(adcFromSvc.getOpMode());

		if (adcFromSvc.getLastDisconnectedTime() != null) {
			adc.setLastDisconnTime(adcFromSvc.getLastDisconnectedTime());
		} else {
			adc.setLastDisconnTime(new Date());
		}
		adc.setActiveStandbyState(adcFromSvc.getActiveStandbyState());
		adc.setActiveStandbyDir(adcFromSvc.getActiveStandbyDirection());
		if (adcFromSvc.getModel() != null) {
			adc.setModel(NumberUtil.extractNumbertoString(adcFromSvc.getModel()));
		}
		adc.setSyslogip(adcFromSvc.getSyslogIpAddress());
		adc.setIsFlb(adcFromSvc.getRoleFlbYn());

		// snmp

		AdcSnmpInfoDto adcSnmpInfo = new AdcSnmpInfoDto();
		adcSnmpInfo.setVersion(adcFromSvc.getSnmpInfo().getVersion());
		if (adcFromSvc.getSnmpInfo().getVersion() == 2) {
			adcSnmpInfo.setRcomm(adcFromSvc.getSnmpInfo().getRcomm());
		} else if (adcFromSvc.getSnmpInfo().getVersion() == 3) {
			adcSnmpInfo.setSecurityName(adcFromSvc.getSnmpInfo().getSecurityName());
			adcSnmpInfo.setAuthPassword(adcFromSvc.getSnmpInfo().getAuthPasswordDecrypt());
			adcSnmpInfo.setPrivPassword(adcFromSvc.getSnmpInfo().getPrivPasswordDecrypt());
			adcSnmpInfo.setAuthProto(adcFromSvc.getSnmpInfo().getAuthProto());
			adcSnmpInfo.setPrivProto(adcFromSvc.getSnmpInfo().getPrivProto());
		}
		adc.setAdcSnmpInfo(adcSnmpInfo);

		adc.setMgmtMode(adcFromSvc.getMgmtMode());
		adc.setSsoMode(adcFromSvc.getSsoMode());
		adc.setSsoModeType(
				adcFromSvc.getSsoMode() != null && adcFromSvc.getSsoMode() == OBDefineWeb.CFG_ON ? true : false);
		adc.setConnProtocol(adcFromSvc.getConnProtocol());

		return adc;
	}

	public static List<AdcDto> toAdcDto(List<OBDtoAdcInfo> adcsFromSvc) {
		if (adcsFromSvc == null)
			return null;

		List<AdcDto> adcs = new ArrayList<AdcDto>();
		for (OBDtoAdcInfo adcFromSvc : adcsFromSvc)
			adcs.add(toAdcDto(adcFromSvc));

		return adcs;
	}

	public static OBDtoAdcInfo toOBDtoAdcInfo(AdcDto adc) throws OBException {
		OBDtoAdcInfo adcFromSvc = new OBDtoAdcInfo();
		adcFromSvc.setIndex(adc.getIndex() == null ? 0 : adc.getIndex());
		adcFromSvc.setAdcAccount(adc.getAccountId());
		adcFromSvc.setAdcPassword(adc.getPassword());
//		adcFromSvc.setAdcPassword(new OBCipherAES().Encrypt(adc.getPassword()));
		adcFromSvc.setAdcCliAccount(adc.getAccountId());
		adcFromSvc.setAdcCliPassword(adc.getPassword());
//		adcFromSvc.setAdcCliPassword(new OBCipherAES().Encrypt(adc.getPassword()));
		adcFromSvc.setName(adc.getName());
		adcFromSvc.setOpMode(adc.getOpMode());

		if (adc.getType() != null) {
			if (adc.getType().equals("F5")) {
				adcFromSvc.setAdcCliAccount(adc.getCliAccountId());
//				adcFromSvc.setAdcCliPassword(new OBCipherAES().Encrypt(adc.getCliPassword()));
				adcFromSvc.setAdcCliPassword(adc.getCliPassword());
				adcFromSvc.setAdcType(OBDefine.ADC_TYPE_F5);
			} else if (adc.getType().equals("Alteon")) {
				adcFromSvc.setAdcType(OBDefine.ADC_TYPE_ALTEON);
			} else if (adc.getType().equals("PAS")) {
				adcFromSvc.setAdcType(OBDefine.ADC_TYPE_PIOLINK_PAS);
			} else if (adc.getType().equals("PASK")) {
				adcFromSvc.setAdcType(OBDefine.ADC_TYPE_PIOLINK_PASK);
			} else if (adc.getType().equals("PiolinkUnknown")) {
				adcFromSvc.setAdcType(OBDefine.ADC_TYPE_PIOLINK_UNKNOWN);
			} else {
				adcFromSvc.setAdcType(OBDefine.ADC_TYPE_UNKNOWN);
			}
		}
		adcFromSvc.setAdcIpAddress(adc.getIp());
		adcFromSvc.setPeerAdcIPAddress(adc.getPeerip());

		if (adc.getConnService().equals(OBDefine.SERVICE.TELNET)) {
			adcFromSvc.setConnService(OBDefine.SERVICE.TELNET);
		} else if (adc.getConnService().equals(OBDefine.SERVICE.SSH)) {
			adcFromSvc.setConnService(OBDefine.SERVICE.SSH);
		} else {
			adcFromSvc.setConnService(OBDefine.SERVICE.NOT_DEFINED);
		}

		adcFromSvc.setConnPort(adc.getConnPort());
		adcFromSvc.setGroupIndex(adc.getGroupIndex() == null ? 0 : adc.getGroupIndex());
		adcFromSvc.setSwVersion(adc.getVersion());
		adcFromSvc.setDescription(adc.getDescription());
		if (adc.getLastUpdateTime() != null)
			adcFromSvc.setApplyTime(new Timestamp(adc.getLastUpdateTime().getTime()));
		if (adc.getSaveTime() != null)
			adcFromSvc.setSaveTime(new Timestamp(adc.getSaveTime().getTime()));
		if (adc.getPurchaseDate() != null)
			adcFromSvc.setPurchaseDate(new Timestamp(adc.getPurchaseDate().getTime()));
		adcFromSvc.setSyslogIpAddress(adc.getSyslogip());

		// snmp

		OBDtoAdcSnmpInfo adcSnmpInfoFromSvc = new OBDtoAdcSnmpInfo();
		adcSnmpInfoFromSvc.setVersion(adc.getAdcSnmpInfo().getVersion());

		if (adc.getAdcSnmpInfo().getVersion() == 3) {
			adcSnmpInfoFromSvc.setSecurityName(adc.getAdcSnmpInfo().getSecurityName());
			if (adc.getAdcSnmpInfo().getAuthPassword() == null
					|| adc.getAdcSnmpInfo().getAuthPassword().trim().length() == 0) {
				adcSnmpInfoFromSvc.setAuthPassword("");
			} else {
				adcSnmpInfoFromSvc.setAuthPassword(new OBCipherAES().Encrypt(adc.getAdcSnmpInfo().getAuthPassword()));
			}

			if (adc.getAdcSnmpInfo().getPrivPassword() == null
					|| adc.getAdcSnmpInfo().getPrivPassword().trim().length() == 0) {
				adcSnmpInfoFromSvc.setPrivPassword("");
			} else {
				adcSnmpInfoFromSvc.setPrivPassword(new OBCipherAES().Encrypt(adc.getAdcSnmpInfo().getPrivPassword()));
			}
			adcSnmpInfoFromSvc.setAuthProto(adc.getAdcSnmpInfo().getAuthProto());
			adcSnmpInfoFromSvc.setPrivProto(adc.getAdcSnmpInfo().getPrivProto());
		}
//		else if (adc.getAdcSnmpInfo().getVersion() == 2)
		else {
			adcSnmpInfoFromSvc.setRcomm(adc.getAdcSnmpInfo().getRcomm());
		}

		adcFromSvc.setSnmpInfo(adcSnmpInfoFromSvc);

		adcFromSvc.setMgmtMode(adc.getMgmtMode());
//		adcFromSvc.setSsoMode(adc.getSsoMode());		
		adcFromSvc.setSsoMode(adc.isSsoModeType() ? OBDefineWeb.CFG_ON : OBDefineWeb.CFG_OFF);
		adcFromSvc.setConnProtocol(adc.getConnProtocol());
		return adcFromSvc;
	}

	public String getPeerip() {
		return peerip;
	}

	public void setPeerip(String peerip) {
		this.peerip = peerip;
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getCliAccountId() {
		return cliAccountId;
	}

	public void setCliAccountId(String cliAccountId) {
		this.cliAccountId = cliAccountId;
	}

	public String getCliPassword() {
		return cliPassword;
	}

	public void setCliPassword(String cliPassword) {
		this.cliPassword = cliPassword;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Integer getGroupIndex() {
		return groupIndex;
	}

	public void setGroupIndex(Integer groupIndex) {
		this.groupIndex = groupIndex;
	}

	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getIsStandby() {
		return isStandby;
	}

	public void setIsStandby(Boolean isStandby) {
		this.isStandby = isStandby;
	}

	public String getSnmpCommunity() {
		return snmpCommunity;
	}

	public void setSnmpCommunity(String snmpCommunity) {
		this.snmpCommunity = snmpCommunity;
	}

	public Date getSaveTime() {
		return saveTime;
	}

	public void setSaveTime(Date saveTime) {
		this.saveTime = saveTime;
	}

	public Date getPurchaseDate() {
		return purchaseDate;
	}

	public void setPurchaseDate(Date purchaseDate) {
		this.purchaseDate = purchaseDate;
	}

	public Integer getConnService() {
		return connService;
	}

	public void setConnService(Integer connService) {
		this.connService = connService;
	}

	public Integer getConnPort() {
		return connPort;
	}

	public void setConnPort(Integer connPort) {
		this.connPort = connPort;
	}

	public Date getLastDisconnTime() {
		return lastDisconnTime;
	}

	public void setLastDisconnTime(Date lastDisconnTime) {
		this.lastDisconnTime = lastDisconnTime;
	}

	public Integer getActiveStandbyState() {
		return activeStandbyState;
	}

	public void setActiveStandbyState(Integer activeStandbyState) {
		this.activeStandbyState = activeStandbyState;
	}

	public Integer getActiveStandbyDir() {
		return activeStandbyDir;
	}

	public void setActiveStandbyDir(Integer activeStandbyDir) {
		this.activeStandbyDir = activeStandbyDir;
	}

	public Integer getModel() {
		return model;
	}

	public void setModel(Integer model) {
		this.model = model;
	}

	public String getSyslogip() {
		return syslogip;
	}

	public void setSyslogip(String syslogip) {
		this.syslogip = syslogip;
	}

	public Integer getIsFlb() {
		return isFlb;
	}

	public void setIsFlb(Integer isFlb) {
		this.isFlb = isFlb;
	}

	public AdcSnmpInfoDto getAdcSnmpInfo() {
		return adcSnmpInfo;
	}

	public void setAdcSnmpInfo(AdcSnmpInfoDto adcSnmpInfo) {
		this.adcSnmpInfo = adcSnmpInfo;
	}

	public Integer getOpMode() {
		return opMode;
	}

	public void setOpMode(Integer opMode) {
		this.opMode = opMode;
	}

	public Integer getMgmtMode() {
		return mgmtMode;
	}

	public void setMgmtMode(Integer mgmtMode) {
		this.mgmtMode = mgmtMode;
	}

	public Integer getSsoMode() {
		return ssoMode;
	}

	public void setSsoMode(Integer ssoMode) {
		this.ssoMode = ssoMode;
	}

	public boolean isSsoModeType() {
		return ssoModeType;
	}

	public void setSsoModeType(boolean ssoModeType) {
		this.ssoModeType = ssoModeType;
	}

	public Integer getConnProtocol() {
		return connProtocol;
	}

	public void setConnProtocol(Integer connProtocol) {
		this.connProtocol = connProtocol;
	}

	@Override
	public String toString() {
		return "AdcDto [index=" + index + ", accountId=" + accountId + ", password=" + password + ", cliAccountId="
				+ cliAccountId + ", cliPassword=" + cliPassword + ", status=" + status + ", name=" + name + ", type="
				+ type + ", ip=" + ip + ", peerip=" + peerip + ", groupIndex=" + groupIndex + ", lastUpdateTime="
				+ lastUpdateTime + ", version=" + version + ", description=" + description + ", snmpCommunity="
				+ snmpCommunity + ", isStandby=" + isStandby + ", saveTime=" + saveTime + ", purchaseDate="
				+ purchaseDate + ", connService=" + connService + ", connPort=" + connPort + ", lastDisconnTime="
				+ lastDisconnTime + ", activeStandbyState=" + activeStandbyState + ", activeStandbyDir="
				+ activeStandbyDir + ", model=" + model + ", syslogip=" + syslogip + ", isFlb=" + isFlb + ", opMode="
				+ opMode + ", adcSnmpInfo=" + adcSnmpInfo + ", mgmtMode=" + mgmtMode + ", ssoModeType=" + ssoModeType
				+ ", ssoMode=" + ssoMode + ", connProtocol=" + connProtocol + "]";
	}
}
