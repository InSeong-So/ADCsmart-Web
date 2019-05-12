package kr.openbase.adcsmart.service.dto;

import java.util.ArrayList;

public class OBDtoNetworkMap {// alteon의 경우에는 service 단위이다.
	private String vsIndex;
	private String vsvcIndex;// for alteon
	private String vsName;
	private String vsIPAddress;
	private Integer servicePort;// for alteon
	private Integer serviceStatus;
	private Integer backupStatus;
	private Integer groupBakupType;
	private String groupBakupId;
	private Integer loadBalancingType;
	private String healthCheckType;
	private String model;
	private String hostName;
	private String alteonId;
	private String groupId;
	private String rport;
	private String vsDescription;

	public String getRport() {
		return rport;
	}

	public String getVsvcIndex() {
		return vsvcIndex;
	}

	public String getVsDescription() {
		return vsDescription;
	}

	public void setVsDescription(String vsDescription) {
		this.vsDescription = vsDescription;
	}

	public void setVsvcIndex(String vsvcIndex) {
		this.vsvcIndex = vsvcIndex;
	}

	public void setRport(String rport) {
		this.rport = rport;
	}

	public String getAlteonId() {
		return alteonId;
	}

	public void setAlteonId(String alteonId) {
		this.alteonId = alteonId;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	private ArrayList<OBDtoNetworkMapMember> memberList;
	private ArrayList<OBDtoNetworkMapBackup> backupList;

	private int lbClass; // OBDefine.ADC_LB_CLASS. ... virtual server(slb) 정보인지, filter(flb) 정보인지 식별

	@Override
	public String toString() {
		return "OBDtoNetworkMap [vsIndex=" + vsIndex + ", vsName=" + vsName + ", vsIPAddress=" + vsIPAddress
				+ ", servicePort=" + servicePort + ", serviceStatus=" + serviceStatus + ", backupStatus=" + backupStatus
				+ ", groupBakupType=" + groupBakupType + ", groupBakupId=" + groupBakupId + ", loadBalancingType="
				+ loadBalancingType + ", healthCheckType=" + healthCheckType + ", model=" + model + ", hostName="
				+ hostName + ", alteonId=" + alteonId + ", groupId=" + groupId + ", rport=" + rport + ", memberList="
				+ memberList + ", backupList=" + backupList + ", lbClass=" + lbClass + "]";
	}

	public Integer getLoadBalancingType() {
		return loadBalancingType;
	}

	public void setLoadBalancingType(Integer loadBalancingType) {
		this.loadBalancingType = loadBalancingType;
	}

	public String getVsIndex() {
		return vsIndex;
	}

	public String getHealthCheckType() {
		return healthCheckType;
	}

	public void setHealthCheckType(String healthCheckType) {
		this.healthCheckType = healthCheckType;
	}

	public void setVsIndex(String vsIndex) {
		this.vsIndex = vsIndex;
	}

	public String getVsName() {
		return vsName;
	}

	public void setVsName(String vsName) {
		this.vsName = vsName;
	}

	public String getVsIPAddress() {
		return vsIPAddress;
	}

	public void setVsIPAddress(String vsIPAddress) {
		this.vsIPAddress = vsIPAddress;
	}

	public Integer getServicePort() {
		return servicePort;
	}

	public void setServicePort(Integer servicePort) {
		this.servicePort = servicePort;
	}

	public Integer getServiceStatus() {
		return serviceStatus;
	}

	public void setServiceStatus(Integer serviceStatus) {
		this.serviceStatus = serviceStatus;
	}

	public ArrayList<OBDtoNetworkMapMember> getMemberList() {
		return memberList;
	}

	public void setMemberList(ArrayList<OBDtoNetworkMapMember> memberList) {
		this.memberList = memberList;
	}

	public int getLbClass() {
		return lbClass;
	}

	public void setLbClass(int lbClass) {
		this.lbClass = lbClass;
	}

	public Integer getBackupStatus() {
		return backupStatus;
	}

	public void setBackupStatus(Integer backupStatus) {
		this.backupStatus = backupStatus;
	}

	public Integer getGroupBakupType() {
		return groupBakupType;
	}

	public void setGroupBakupType(Integer groupBakupType) {
		this.groupBakupType = groupBakupType;
	}

	public String getGroupBakupId() {
		return groupBakupId;
	}

	public void setGroupBakupId(String groupBakupId) {
		this.groupBakupId = groupBakupId;
	}

	public ArrayList<OBDtoNetworkMapBackup> getBackupList() {
		return backupList;
	}

	public void setBackupList(ArrayList<OBDtoNetworkMapBackup> backupList) {
		this.backupList = backupList;
	}
}
