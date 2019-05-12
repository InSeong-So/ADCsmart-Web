package kr.openbase.adcsmart.service.dashboard.dto;

import java.util.ArrayList;

public class OBDtoADCList {
	private Integer index;
	private String name;
	private String ipaddress;
	private Integer type;
	private Integer status;
	private Integer vsCount = 0;
	private Integer vsPartDownCount = 0;
	private Integer vsDownCount = 0;
	private Integer vsFilteredCount = 0;
	private Integer vsTotalCount = 0;
	private ArrayList<OBDtoVSList> vsList;

	@Override
	public String toString() {
		return "OBDtoADCList [index=" + index + ", name=" + name + ", ipaddress=" + ipaddress + ", type=" + type
				+ ", status=" + status + ", vsCount=" + vsCount + ", vsPartDownCount=" + vsPartDownCount
				+ ", vsDownCount=" + vsDownCount + ", vsFilteredCount=" + vsFilteredCount + ", vsTotalCount="
				+ vsTotalCount + ", vsList=" + vsList + "]";
	}

	public ArrayList<OBDtoVSList> getVsList() {
		return vsList;
	}

	public Integer getVsFilteredCount() {
		return vsFilteredCount;
	}

	public void setVsFilteredCount(Integer vsFilteredCount) {
		this.vsFilteredCount = vsFilteredCount;
	}

	public void setVsList(ArrayList<OBDtoVSList> vsList) {
		this.vsList = vsList;
	}

	public Integer getVsCount() {
		return vsCount;
	}

	public void setVsCount(Integer vsCount) {
		this.vsCount = vsCount;
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIpaddress() {
		return ipaddress;
	}

	public void setIpaddress(String ipaddress) {
		this.ipaddress = ipaddress;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getStatus() {
		return status;
	}

	public Integer getVsPartDownCount() {
		return vsPartDownCount;
	}

	public void setVsPartDownCount(Integer vsPartDownCount) {
		this.vsPartDownCount = vsPartDownCount;
	}

	public Integer getVsDownCount() {
		return vsDownCount;
	}

	public void setVsDownCount(Integer vsDownCount) {
		this.vsDownCount = vsDownCount;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getVsTotalCount() {
		return vsTotalCount;
	}

	public void setVsTotalCount(Integer vsTotalCount) {
		this.vsTotalCount = vsTotalCount;
	}

}
