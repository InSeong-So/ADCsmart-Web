package kr.openbase.adcsmart.web.facade.dto;

import java.util.Date;

import kr.openbase.adcsmart.service.dto.OBDtoDashboardSdsMemberConnection;

public class VsMemberConnectionDataDto {
	
	private Date occurredTime;
	private String memberIp;
	private Integer memberPort;
	private Long connections;
	
	public Date getOccurredTime() {
		return occurredTime;
	}
	public void setOccurredTime(Date occurredTime) {
		this.occurredTime = occurredTime;
	}
	public String getMemberIp() {
		return memberIp;
	}
	public void setMemberIp(String memberIp) {
		this.memberIp = memberIp;
	}
	public Integer getMemberPort() {
		return memberPort;
	}
	public void setMemberPort(Integer memberPort) {
		this.memberPort = memberPort;
	}
	public Long getConnections() {
		return connections;
	}
	public void setConnections(Long connections) {
		this.connections = connections;
	}
	
	@Override
	public String toString() {
		return "VsMemberConnectionDataDto [occurredTime=" + occurredTime
				+ ", memberIp=" + memberIp + ", memberPort=" + memberPort
				+ ", connections=" + connections + "]";
	}
	
	public static VsMemberConnectionDataDto getVsMemberConnectionData(
			OBDtoDashboardSdsMemberConnection obDtoDashboardSdsMemberConnection) {
		VsMemberConnectionDataDto vsMemberConnectionData = new VsMemberConnectionDataDto();
		vsMemberConnectionData.setOccurredTime(obDtoDashboardSdsMemberConnection.getOccurTime());
		vsMemberConnectionData.setMemberIp(obDtoDashboardSdsMemberConnection.getMemberIp());
		vsMemberConnectionData.setMemberPort(obDtoDashboardSdsMemberConnection.getMemberPort());
		vsMemberConnectionData.setConnections(obDtoDashboardSdsMemberConnection.getConns());
		return vsMemberConnectionData;
	}
	
}
