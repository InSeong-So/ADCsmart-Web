package kr.openbase.adcsmart.web.facade.dto;

public class ServiceMapVsDescDto {
	private Integer adcIndex;
	private Integer adcType;// "Alteon, F5...."
	private String vsIndex;
	private String vsvcIndex;
	private String description;

	public ServiceMapVsDescDto() {
	}

	public ServiceMapVsDescDto(Integer adcIndex, Integer adcType, String vsIndex, String vsvcIndex,
			String description) {
		this.adcIndex = adcIndex;
		this.adcType = adcType;
		this.vsIndex = vsIndex;
		this.vsvcIndex = vsvcIndex;
		this.description = description;
	}

	@Override
	public String toString() {
		return "ServiceMapVsDescDto [adcIndex=" + adcIndex + ", adcType=" + adcType + ", vsIndex=" + vsIndex
				+ ", vsvcIndex=" + vsvcIndex + ", description=" + description + "]";
	}

	public Integer getAdcIndex() {
		return adcIndex;
	}

	public void setAdcIndex(Integer adcIndex) {
		this.adcIndex = adcIndex;
	}

	public Integer getAdcType() {
		return adcType;
	}

	public void setAdcType(Integer adcType) {
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
