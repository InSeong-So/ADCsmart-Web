/**
 * 
 */
package kr.openbase.adcsmart.web.facade.dto;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import kr.openbase.adcsmart.service.dto.OBDtoAdcPerfHttpReq;
import kr.openbase.adcsmart.service.dto.OBDtoAdcPerformance;
import kr.openbase.adcsmart.web.util.NumberUtil;

public class PerfHttpRequestInfoDto
{

	private String maxHttpRequest;
	private String minHttpRequest;
	private String avgHttpRequest;
	private String curHttpRequest;
	private List<PerfHttpRequestDataDto> perfHttpRequestDataList;
	
	public PerfHttpRequestInfoDto()
	{
		perfHttpRequestDataList = new ArrayList<PerfHttpRequestDataDto>();
	}

	public String getMaxHttpRequest() 
	{
		return maxHttpRequest;
	}

	public void setMaxHttpRequest(String maxHttpRequest)
	{
		this.maxHttpRequest = maxHttpRequest;
	}

	public String getMinHttpRequest()
	{
		return minHttpRequest;
	}

	public void setMinHttpRequest(String minHttpRequest) 
	{
		this.minHttpRequest = minHttpRequest;
	}

	public String getAvgHttpRequest()
	{
		return avgHttpRequest;
	}

	public void setAvgHttpRequest(String avgHttpRequest) 
	{
		this.avgHttpRequest = avgHttpRequest;
	}

	public String getCurHttpRequest()
	{
		return curHttpRequest;
	}

	public void setCurHttpRequest(String curHttpRequest)
	{
		this.curHttpRequest = curHttpRequest;
	}

	public List<PerfHttpRequestDataDto> getPerfHttpRequestDataList()
	{
		return perfHttpRequestDataList;
	}

	public void setPerfHttpRequestDataList(List<PerfHttpRequestDataDto> perfHttpRequestDataList) 
	{
		this.perfHttpRequestDataList = perfHttpRequestDataList;
	}
	
	@Override
	public String toString() 
	{
		return "PerfHttpRequestInfoDto [maxHttpRequest=" + maxHttpRequest
				+ ", minHttpRequest=" + minHttpRequest + ", avgHttpRequest="
				+ avgHttpRequest + ", curHttpRequest=" + curHttpRequest
				+ ", perfHttpRequestDataList=" + perfHttpRequestDataList + "]";
	}

	public static PerfHttpRequestInfoDto getPerfHttpRequestInfo(OBDtoAdcPerformance adcPerformance)throws Exception
	{
		PerfHttpRequestInfoDto perfHttpRequestInfo = new PerfHttpRequestInfoDto();
		
		perfHttpRequestInfo.setMaxHttpRequest(NumberUtil.toStringWithUnit(adcPerformance.getHttpReqMax(), ""));
		perfHttpRequestInfo.setMinHttpRequest(NumberUtil.toStringWithUnit(adcPerformance.getHttpReqMin(), ""));
		perfHttpRequestInfo.setAvgHttpRequest(NumberUtil.toStringWithUnit(adcPerformance.getHttpReqAvg(), ""));
		perfHttpRequestInfo.setCurHttpRequest(NumberUtil.toStringWithUnit(adcPerformance.getHttpReqCurr(), ""));
		
		if (!CollectionUtils.isEmpty(adcPerformance.getHttpReqList()))
		{
			for (OBDtoAdcPerfHttpReq adcPerfHttpReq : adcPerformance.getHttpReqList()) 
			{
				perfHttpRequestInfo.getPerfHttpRequestDataList().add(PerfHttpRequestDataDto.getPerfHttpRequestData(adcPerfHttpReq));
			}
		}
		return perfHttpRequestInfo;
	}
	
}
