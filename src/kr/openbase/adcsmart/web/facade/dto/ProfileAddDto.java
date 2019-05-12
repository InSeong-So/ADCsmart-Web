package kr.openbase.adcsmart.web.facade.dto;

import kr.openbase.adcsmart.service.dto.OBDtoAdcProfile;
import kr.openbase.adcsmart.web.util.OBDefineWeb;

public class ProfileAddDto {
	private String index;
	private Integer adcIndex;
	private String name;
	private String persistenceType;
	private String parentProfileName;
	private Boolean isMatchAcrossServices = false;
	private Long timeOutInSec;

	public static ProfileAddDto toProfileAddDto(OBDtoAdcProfile profileFromSvc) {
		ProfileAddDto profileAdd = new ProfileAddDto();
		profileAdd.setIndex(profileFromSvc.getIndex());
		profileAdd.setAdcIndex(profileFromSvc.getAdcIndex());
		profileAdd.setName(profileFromSvc.getProfileName());
		profileAdd.setPersistenceType("SourceAddressAffinity");
		profileAdd.setParentProfileName(profileFromSvc.getParentProfile());
		profileAdd.setIsMatchAcrossServices(
				profileFromSvc.getMatchAcrossServiceYN() == OBDefineWeb.CFG_ON ? true : false);
		profileAdd.setTimeOutInSec(profileFromSvc.getTimeout());
		return profileAdd;
	}

	public static OBDtoAdcProfile toOBDtoAdcProfile(ProfileAddDto profileAdd) {
		OBDtoAdcProfile profileFromSvc = new OBDtoAdcProfile();
		profileFromSvc.setIndex(profileAdd.getIndex());
		profileFromSvc.setAdcIndex(profileAdd.getAdcIndex());
		profileFromSvc.setProfileName(profileAdd.getName());
		profileFromSvc.setPersistenceType(1);
		profileFromSvc.setParentProfile(profileAdd.getParentProfileName());
		profileFromSvc.setMatchAcrossServiceYN(
				profileAdd.getIsMatchAcrossServices() ? OBDefineWeb.CFG_ON : OBDefineWeb.CFG_OFF);
		profileFromSvc.setTimeout((long) profileAdd.getTimeOutInSec());
		return profileFromSvc;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public Integer getAdcIndex() {
		return adcIndex;
	}

	public void setAdcIndex(Integer adcIndex) {
		this.adcIndex = adcIndex;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPersistenceType() {
		return persistenceType;
	}

	public void setPersistenceType(String persistenceType) {
		this.persistenceType = persistenceType;
	}

	public String getParentProfileName() {
		return parentProfileName;
	}

	public void setParentProfileName(String parentProfileName) {
		this.parentProfileName = parentProfileName;
	}

	public Boolean getIsMatchAcrossServices() {
		return isMatchAcrossServices;
	}

	public void setIsMatchAcrossServices(Boolean isMatchAcrossServices) {
		this.isMatchAcrossServices = isMatchAcrossServices;
	}

	public Long getTimeOutInSec() {
		return timeOutInSec;
	}

	public void setTimeOutInSec(Long timeOutInSec) {
		this.timeOutInSec = timeOutInSec;
	}

	@Override
	public String toString() {
		return "ProfileAddDto [index=" + index + ", adcIndex=" + adcIndex + ", name=" + name + ", persistenceType="
				+ persistenceType + ", parentProfileName=" + parentProfileName + ", isMatchAcrossServices="
				+ isMatchAcrossServices + ", timeOutInSec=" + timeOutInSec + "]";
	}
}
