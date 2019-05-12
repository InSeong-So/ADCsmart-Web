package kr.openbase.adcsmart.web.report.fault;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import kr.openbase.adcsmart.service.dto.OBDtoAdcInfo;
import kr.openbase.adcsmart.service.utility.OBDefine;

public class AdcDto {
	private Integer index;
	private String accountId;
	private String password;
	private String status;
	private String name;
	private String type;
	private String ip;
	private Integer groupIndex;
	private Date lastUpdateTime;
	private String version;
	private String description;
	private String snmpCommunity;
	private Boolean isStandby = false;
	
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
		if(adcFromSvc.getAdcType() == OBDefine.ADC_TYPE_ALTEON)
			adc.setType("Alteon");
		else if(adcFromSvc.getAdcType() == OBDefine.ADC_TYPE_F5)
			adc.setType("F5");
		else if(adcFromSvc.getAdcType() == OBDefine.ADC_TYPE_PIOLINK_PAS)
			adc.setType("Pas");
		else 
			adc.setType("unknown");
//		adc.setType(adcFromSvc.getAdcType() == Integer.valueOf(OBDefine.ADC_TYPE_F5) ? "F5" : "Alteon");
		adc.setIp(adcFromSvc.getAdcIpAddress());
		adc.setGroupIndex((int) adcFromSvc.getGroupIndex());
		adc.setVersion(adcFromSvc.getSwVersion());
		adc.setDescription(adcFromSvc.getDescription());
		adc.setLastUpdateTime(adcFromSvc.getApplyTime());
		adc.setSnmpCommunity(adcFromSvc.getSnmpRComm());
		adc.setIsStandby((adcFromSvc.getActivePairIndex() == null || adcFromSvc.getActivePairIndex() == 0) ? false : true);
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

	public static OBDtoAdcInfo toOBDtoAdcInfo(AdcDto adc)
	{
		OBDtoAdcInfo adcFromSvc = new OBDtoAdcInfo();
		adcFromSvc.setIndex(adc.getIndex() == null ? 0 : adc.getIndex());
		adcFromSvc.setAdcAccount(adc.getAccountId());
		adcFromSvc.setAdcPassword(adc.getPassword());
		adcFromSvc.setName(adc.getName());
		
		if (adc.getType() != null)
		{
			if(adc.getType().equals("Alteon"))
				adcFromSvc.setAdcType(OBDefine.ADC_TYPE_ALTEON);
			else if(adc.getType().equals("F5"))
				adcFromSvc.setAdcType(OBDefine.ADC_TYPE_F5);
			else if(adc.getType().equals("Pas"))
				adcFromSvc.setAdcType(OBDefine.ADC_TYPE_PIOLINK_PAS);
			else 
				adcFromSvc.setAdcType(OBDefine.ADC_TYPE_UNKNOWN);
		}

		adcFromSvc.setAdcIpAddress(adc.getIp());
		adcFromSvc.setGroupIndex(adc.getGroupIndex() == null ? 0 : adc.getGroupIndex());
		adcFromSvc.setSwVersion(adc.getVersion());
		adcFromSvc.setDescription(adc.getDescription());
		adcFromSvc.setSnmpRComm(adc.getSnmpCommunity());
		if (adc.getLastUpdateTime() != null)
			adcFromSvc.setApplyTime(new Timestamp(adc.getLastUpdateTime().getTime()));

		return adcFromSvc;
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

	@Override
	public String toString() {
		return "AdcDto [index=" + index + ", accountId=" + accountId + ", password=" + password + ", status=" + status
				+ ", name=" + name + ", type=" + type + ", ip=" + ip + ", groupIndex=" + groupIndex
				+ ", lastUpdateTime=" + lastUpdateTime + ", version=" + version + ", description=" + description
				+ ", snmpCommunity=" + snmpCommunity + ", isStandby=" + isStandby + "]";
	}
	
}
