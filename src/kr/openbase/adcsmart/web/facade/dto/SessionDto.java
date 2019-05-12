package kr.openbase.adcsmart.web.facade.dto;

import kr.openbase.adcsmart.service.dto.OBDtoExtraInfo;

public class SessionDto {
	private Integer accountIndex;
	private String clientIp;
	
	public OBDtoExtraInfo toOBDtoExtraInfo() {
		OBDtoExtraInfo extraInfo = new OBDtoExtraInfo();
		extraInfo.setAccountIndex(accountIndex);
		extraInfo.setClientIPAddress(clientIp);
		return extraInfo;
	}
	
	public Integer getAccountIndex() {
		return accountIndex;
	}
	public void setAccountIndex(Integer accountIndex) {
		this.accountIndex = accountIndex;
	}
	public String getClientIp() {
		return clientIp;
	}
	public void setClientIp(String clientIp) {
		this.clientIp = clientIp;
	}
	@Override
	public String toString() {
		return "SessionDto [accountIndex=" + accountIndex + ", clientIp=" + clientIp + "]";
	}
	
}
