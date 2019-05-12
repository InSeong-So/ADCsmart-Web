/**
 * 
 */
package kr.openbase.adcsmart.web.facade.dto;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import kr.openbase.adcsmart.service.dto.OBDtoAdcPerfConnection;
import kr.openbase.adcsmart.service.dto.OBDtoAdcPerformance;
import kr.openbase.adcsmart.web.util.NumberUtil;

/**
 * @author paul
 *
 */
public class PerfActiveConnectionInfoDto {
	
	private String maxActiveConnection;
	private String minActiveConnection;
	private String avgActiveConnection;
	private String curActiveConnection;
	private List<PerfActiveConnectionDataDto> perfActiveConnectionDataList;
	
	public PerfActiveConnectionInfoDto() {
		perfActiveConnectionDataList = new ArrayList<PerfActiveConnectionDataDto>();
	}
	
	/**
	 * @return the maxActiveConnection
	 */
	public String getMaxActiveConnection() {
		return maxActiveConnection;
	}
	
	/**
	 * @param maxActiveConnection the maxActiveConnection to set
	 */
	public void setMaxActiveConnection(String maxActiveConnection) {
		this.maxActiveConnection = maxActiveConnection;
	}
	
	/**
	 * @return the minActiveConnection
	 */
	public String getMinActiveConnection() {
		return minActiveConnection;
	}
	
	/**
	 * @param minActiveConnection the minActiveConnection to set
	 */
	public void setMinActiveConnection(String minActiveConnection) {
		this.minActiveConnection = minActiveConnection;
	}
	
	/**
	 * @return the avgActiveConnection
	 */
	public String getAvgActiveConnection() {
		return avgActiveConnection;
	}
	
	/**
	 * @param avgActiveConnection the avgActiveConnection to set
	 */
	public void setAvgActiveConnection(String avgActiveConnection) {
		this.avgActiveConnection = avgActiveConnection;
	}
	
	/**
	 * @return the curActiveConnection
	 */
	public String getCurActiveConnection() {
		return curActiveConnection;
	}
	
	/**
	 * @param curActiveConnection the curActiveConnection to set
	 */
	public void setCurActiveConnection(String curActiveConnection) {
		this.curActiveConnection = curActiveConnection;
	}
	
	/**
	 * @return the perfActiveConnectionDataList
	 */
	public List<PerfActiveConnectionDataDto> getPerfActiveConnectionDataList() {
		return perfActiveConnectionDataList;
	}
	
	/**
	 * @param perfActiveConnectionDataList the perfActiveConnectionDataList to set
	 */
	public void setPerfActiveConnectionDataList(
			List<PerfActiveConnectionDataDto> perfActiveConnectionDataList) {
		this.perfActiveConnectionDataList = perfActiveConnectionDataList;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PerfActiveConnectionInfoDto [maxActiveConnection="
				+ maxActiveConnection + ", minActiveConnection="
				+ minActiveConnection + ", avgActiveConnection="
				+ avgActiveConnection + ", curActiveConnection="
				+ curActiveConnection + ", perfActiveConnectionDataList="
				+ perfActiveConnectionDataList + "]";
	}
	
	public static PerfActiveConnectionInfoDto getPerfActiveConnectionInfo(OBDtoAdcPerformance adcPerformance)
			throws Exception {
		PerfActiveConnectionInfoDto perfActiveConnectionInfo = new PerfActiveConnectionInfoDto();
		perfActiveConnectionInfo.setMaxActiveConnection(NumberUtil.toStringWithUnit(adcPerformance.getConnMax(), ""));
		perfActiveConnectionInfo.setMinActiveConnection(NumberUtil.toStringWithUnit(adcPerformance.getConnMin(), ""));
		perfActiveConnectionInfo.setAvgActiveConnection(NumberUtil.toStringWithUnit(adcPerformance.getConnAvg(), ""));
		perfActiveConnectionInfo.setCurActiveConnection(NumberUtil.toStringWithUnit(adcPerformance.getConnCurr(), ""));
		if (!CollectionUtils.isEmpty(adcPerformance.getConnList())) {
			for (OBDtoAdcPerfConnection adcPerfConnection : adcPerformance.getConnList()) {
				perfActiveConnectionInfo.getPerfActiveConnectionDataList().add(
						PerfActiveConnectionDataDto.getPerfActiveConnectionData(adcPerfConnection));				
			}
		}
		return perfActiveConnectionInfo;
	}
	
}
