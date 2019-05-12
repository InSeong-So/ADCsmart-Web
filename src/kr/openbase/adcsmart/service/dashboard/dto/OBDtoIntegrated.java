package kr.openbase.adcsmart.service.dashboard.dto;

import java.util.List;

public class OBDtoIntegrated {
	private String vsNomal;
	private Integer vsAbnomal;
	private Integer vsFiltered;
	private String vsOffDisable;
	private Integer vsTotal;

	private Integer adcNomal;
	private Integer adcAbnomal;

	private List<OBDtoADCList> adcs;

	@Override
	public String toString() {
		return "OBDtoIntegrated [vsNomal=" + vsNomal + ", vsAbnomal=" + vsAbnomal + ", vsFiltered=" + vsFiltered
				+ ", vsOffDisable=" + vsOffDisable + ", vsTotal=" + vsTotal + ", adcNomal=" + adcNomal + ", adcAbnomal="
				+ adcAbnomal + ", adcs=" + adcs + "]";
	}

	public String getVsNomal() {
		return vsNomal;
	}

	public void setVsNomal(String vsNomal) {
		this.vsNomal = vsNomal;
	}

	public Integer getVsAbnomal() {
		return vsAbnomal;
	}

	public void setVsAbnomal(Integer vsAbnomal) {
		this.vsAbnomal = vsAbnomal;
	}

	public Integer getVsFiltered() {
		return vsFiltered;
	}

	public void setVsFiltered(Integer vsFiltered) {
		this.vsFiltered = vsFiltered;
	}

	public String getVsOffDisable() {
		return vsOffDisable;
	}

	public void setVsOffDisable(String vsOffDisable) {
		this.vsOffDisable = vsOffDisable;
	}

	public Integer getAdcNomal() {
		return adcNomal;
	}

	public void setAdcNomal(Integer adcNomal) {
		this.adcNomal = adcNomal;
	}

	public Integer getAdcAbnomal() {
		return adcAbnomal;
	}

	public void setAdcAbnomal(Integer adcAbnomal) {
		this.adcAbnomal = adcAbnomal;
	}

	public List<OBDtoADCList> getAdcs() {
		return adcs;
	}

	public void setAdcs(List<OBDtoADCList> adcs) {
		this.adcs = adcs;
	}

	public Integer getVsTotal() {
		return vsTotal;
	}

	public void setVsTotal(Integer vsTotal) {
		this.vsTotal = vsTotal;
	}
}
