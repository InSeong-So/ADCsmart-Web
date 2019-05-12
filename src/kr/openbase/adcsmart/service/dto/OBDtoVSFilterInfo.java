package kr.openbase.adcsmart.service.dto;

public class OBDtoVSFilterInfo {
	private String index;// adcip_vsip_vsport
	private int adcIndex;
	private String adcName;
	private int adcType;
	private String vsIndex;
	private String vsvcIndex;
	private String vsIPAddress;
	private int vsPort;
	private int vsStatus;
	private int showHideState;

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public int getAdcIndex() {
		return adcIndex;
	}

	public void setAdcIndex(int adcIndex) {
		this.adcIndex = adcIndex;
	}

	public String getAdcName() {
		return adcName;
	}

	public void setAdcName(String adcName) {
		this.adcName = adcName;
	}

	public int getAdcType() {
		return adcType;
	}

	public void setAdcType(int adcType) {
		this.adcType = adcType;
	}

	public String getVsIndex() {
		return vsIndex;
	}

	public void setVsIndex(String vsIndex) {
		this.vsIndex = vsIndex;
	}

	public String getVsvcIndex() {
		return vsvcIndex;
	}

	public void setVsvcIndex(String vsvcIndex) {
		this.vsvcIndex = vsvcIndex;
	}

	public String getVsIPAddress() {
		return vsIPAddress;
	}

	public void setVsIPAddress(String vsIPAddress) {
		this.vsIPAddress = vsIPAddress;
	}

	public int getVsPort() {
		return vsPort;
	}

	public void setVsPort(int vsPort) {
		this.vsPort = vsPort;
	}

	public int getVsStatus() {
		return vsStatus;
	}

	public void setVsStatus(int vsStatus) {
		this.vsStatus = vsStatus;
	}

	public int getShowHideState() {
		return showHideState;
	}

	public void setShowHideState(int showHideState) {
		this.showHideState = showHideState;
	}

	@Override
	public String toString() {
		return "OBDtoVSFilterInfo [index=" + index + ", adcIndex=" + adcIndex + ", adcName=" + adcName + ", adcType="
				+ adcType + ", vsIndex=" + vsIndex + ", vsvcIndex=" + vsvcIndex + ", vsIPAddress=" + vsIPAddress
				+ ", vsPort=" + vsPort + ", vsStatus=" + vsStatus + ", showHideState=" + showHideState + "]";
	}

}
