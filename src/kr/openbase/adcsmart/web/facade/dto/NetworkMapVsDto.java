/**
 * 
 */
package kr.openbase.adcsmart.web.facade.dto;

import java.util.List;

import kr.openbase.adcsmart.service.utility.OBDefine;

/**
 * @author paul
 *
 */
public class NetworkMapVsDto {

	private String index;
	private String vsvcIndex;// for alteon
	private String ipAddress;
	private String name;
	private Integer port;
	private Integer status;
	private Integer backupStatus;
	private Integer groupBakupType;
	private String groupBakupId;
	private Integer lbClass;
	private String loadBalancingType;
	private String healthCheckType;
	private List<NetworkMapVsMemberDto> networkMapVsMemberList;
	private List<NetworkMapVsBackupDto> networkMapVsBackupList;
	private String model;
	private String hostName;
	private String alteonId;
	private String groupId;
	private String rport;
	private String description;// = "여기는 VS 설명.. 테스트중...";

	public String getDescription() {
		return description;
	}

	public String getVsvcIndex() {
		return vsvcIndex;
	}

	public void setVsvcIndex(String vsvcIndex) {
		this.vsvcIndex = vsvcIndex;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getRport() {
		return rport;
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

	/**
	 * @return the index
	 */
	public String getIndex() {
		return index;
	}

	/**
	 * @param index the index to set
	 */
	public void setIndex(String index) {
		this.index = index;
	}

	/**
	 * @return the ipAddress
	 */
	public String getIpAddress() {
		return ipAddress;
	}

	/**
	 * @param ipAddress the ipAddress to set
	 */
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the port
	 */
	public Integer getPort() {
		return port;
	}

	/**
	 * @param port the port to set
	 */
	public void setPort(Integer port) {
		this.port = port;
	}

	/**
	 * @return the status
	 */
	public Integer getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}

	/**
	 * @return the networkMapVsMemberList
	 */
	public List<NetworkMapVsMemberDto> getNetworkMapVsMemberList() {
		return networkMapVsMemberList;
	}

	/**
	 * @param networkMapVsMemberList the networkMapVsMemberList to set
	 */
	public void setNetworkMapVsMemberList(List<NetworkMapVsMemberDto> networkMapVsMemberList) {
		this.networkMapVsMemberList = networkMapVsMemberList;
	}

	public Integer getLbClass() {
		return lbClass;
	}

	public void setLbClass(Integer lbClass) {
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

	public List<NetworkMapVsBackupDto> getNetworkMapVsBackupList() {
		return networkMapVsBackupList;
	}

	public void setNetworkMapVsBackupList(List<NetworkMapVsBackupDto> networkMapVsBackupList) {
		this.networkMapVsBackupList = networkMapVsBackupList;
	}

	public String getLoadBalancingType() {
		return loadBalancingType;
	}

	public void setLoadBalancingType(String loadBalancingType) {
		this.loadBalancingType = loadBalancingType;
	}

	public void setLoadBalancingType(Integer loadBalancingType) {
		if (loadBalancingType == null) {
			setLoadBalancingType("");
			return;
		}
		switch (loadBalancingType) {
		case OBDefine.LB_METHOD_ROUND_ROBIN:
			setLoadBalancingType("Round Robbin");
			break;
		case OBDefine.LB_METHOD_LEAST_CONNECTION_MEMBER:
			setLoadBalancingType("Least Connections");
			break;
		case OBDefine.LB_METHOD_HASH:
			setLoadBalancingType("Hash");
			break;
		case OBDefine.COMMON_NOT_ALLOWED:
			setLoadBalancingType("NOT_ALLOWED");
			break;
		default:
			setLoadBalancingType("");
		}
	}

	public String getHealthCheckType() {
		return healthCheckType;
	}

	public void setHealthCheckType(String healthCheckType) {
		this.healthCheckType = healthCheckType;
	}

//    public void setHealthCheckType(Integer healthCheckType)
//    {
//        if(healthCheckType==null) //null이 올 수 없지만 만약을 대비
//        {
//            setHealthCheckType("");
//            return;
//        }
//        switch (healthCheckType)
//        {
//        case OBDefine.HEALTH_CHECK.NONE:
//            setHealthCheckType("NONE"); break;
//        case OBDefine.HEALTH_CHECK.TCP:
//            setHealthCheckType("TCP");  break;
//        case OBDefine.HEALTH_CHECK.HTTP:
//            setHealthCheckType("HTTP"); break;
//        case OBDefine.HEALTH_CHECK.HTTPS:
//            setHealthCheckType("HTTPS");    break;
//        case OBDefine.HEALTH_CHECK.UDP:
//            setHealthCheckType("UDP");  break;
//        case OBDefine.HEALTH_CHECK.ICMP:
//            setHealthCheckType("ICMP"); break;
//        case OBDefine.HEALTH_CHECK.GATEWAY_ICMP:
//            setHealthCheckType("GATEWAY_ICMP"); break;
//        case OBDefine.HEALTH_CHECK.ARP:
//            setHealthCheckType("ARP");  break;
//        case OBDefine.HEALTH_CHECK.LINK:
//            setHealthCheckType("LINK"); break;
//        default:
//            setHealthCheckType("NOT_ALLOWED");
//        }
//    }

	@Override
	public String toString() {
		return "NetworkMapVsDto [index=" + index + ", vsvcIndex=" + vsvcIndex + ", ipAddress=" + ipAddress + ", name="
				+ name + ", port=" + port + ", status=" + status + ", backupStatus=" + backupStatus
				+ ", groupBakupType=" + groupBakupType + ", groupBakupId=" + groupBakupId + ", lbClass=" + lbClass
				+ ", loadBalancingType=" + loadBalancingType + ", healthCheckType=" + healthCheckType
				+ ", networkMapVsMemberList=" + networkMapVsMemberList + ", networkMapVsBackupList="
				+ networkMapVsBackupList + ", model=" + model + ", hostName=" + hostName + ", alteonId=" + alteonId
				+ ", groupId=" + groupId + ", rport=" + rport + ", description=" + description + "]";
	}
}
