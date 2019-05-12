package kr.openbase.adcsmart.web.facade.dto;

import java.util.ArrayList;
import java.util.List;

import kr.openbase.adcsmart.service.dto.OBDtoAdcProfile;
import kr.openbase.adcsmart.web.util.OBDefineWeb;

public class ProfileDto {
	private String index;
	private String name;
	private Long timeOutInSec;
	private Boolean isMatchAcrossServices;

	public static ProfileDto toProfileDto(OBDtoAdcProfile profileFromSvc) {
		ProfileDto profile = new ProfileDto();
		profile.setIndex(profileFromSvc.getIndex());
		profile.setName(profileFromSvc.getProfileName());
		profile.setTimeOutInSec(profileFromSvc.getTimeout());
		profile.setIsMatchAcrossServices(profileFromSvc.getMatchAcrossServiceYN() == OBDefineWeb.CFG_ON ? true : false);
		return profile;
	}

	public static List<ProfileDto> toProfileDto(List<OBDtoAdcProfile> profilesFromSvc) {
		List<ProfileDto> profiles = new ArrayList<ProfileDto>();
		for (OBDtoAdcProfile profileFromSvc : profilesFromSvc)
			profiles.add(toProfileDto(profileFromSvc));

		return profiles;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getTimeOutInSec() {
		return timeOutInSec;
	}

	public void setTimeOutInSec(Long timeOutInSec) {
		this.timeOutInSec = timeOutInSec;
	}

	public Boolean getIsMatchAcrossServices() {
		return isMatchAcrossServices;
	}

	public void setIsMatchAcrossServices(Boolean isMatchAcrossServices) {
		this.isMatchAcrossServices = isMatchAcrossServices;
	}

	@Override
	public String toString() {
		return "ProfileDto [index=" + index + ", name=" + name + ", timeOutInSec=" + timeOutInSec
				+ ", isMatchAcrossServices=" + isMatchAcrossServices + "]";
	}

}
