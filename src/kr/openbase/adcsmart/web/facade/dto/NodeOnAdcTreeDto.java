package kr.openbase.adcsmart.web.facade.dto;

public class NodeOnAdcTreeDto {
	private String nodeType;	// all, a group, or an adc
	private String groupIndex;
	private Integer adcIndex;
	private String adcName;
	private String  adcType;
	
	public String getNodeType() {
		return nodeType;
	}
	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}
	public String getGroupIndex() {
		return groupIndex;
	}
	public void setGroupIndex(String groupIndex) {
		this.groupIndex = groupIndex;
	}
	public Integer getAdcIndex() {
		return adcIndex;
	}
	public void setAdcIndex(Integer adcIndex) {
		this.adcIndex = adcIndex;
	}
	public String getAdcName() {
		return adcName;
	}
	public void setAdcName(String adcName) {
		this.adcName = adcName;
	}
	public String getAdcType() {
		return adcType;
	}
	public void setAdcType(String adcType) {
		this.adcType = adcType;
	}
	@Override
	public String toString() {
		return "NodeOnAdcTreeDto [nodeType=" + nodeType + ", groupIndex=" + groupIndex + ", adcIndex=" + adcIndex
				+ ", adcName=" + adcName + ", adcType=" + adcType + "]";
	}
	
}
