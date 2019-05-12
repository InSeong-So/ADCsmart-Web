/**
 * 
 */
package kr.openbase.adcsmart.web.facade.dto;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import kr.openbase.adcsmart.service.dto.OBDtoAdcPerfSslTrans;
import kr.openbase.adcsmart.service.dto.OBDtoAdcPerformance;
import kr.openbase.adcsmart.web.util.NumberUtil;

public class PerfSslTransactionInfoDto
{
	
	private String maxTransaction;
	private String minTransaction;
	private String avgTransaction;
	private String curTransaction;
	private List<PerfSslTransactionDataDto> perfSslTransactionDataList;
	
	public PerfSslTransactionInfoDto()
	{
		perfSslTransactionDataList = new ArrayList<PerfSslTransactionDataDto>();
	}

	public String getMaxTransaction()
	{
		return maxTransaction;
	}

	public void setMaxTransaction(String maxTransaction)
	{
		this.maxTransaction = maxTransaction;
	}

	public String getMinTransaction()
	{
		return minTransaction;
	}

	public void setMinTransaction(String minTransaction)
	{
		this.minTransaction = minTransaction;
	}

	public String getAvgTransaction()
	{
		return avgTransaction;
	}

	public void setAvgTransaction(String avgTransaction)
	{
		this.avgTransaction = avgTransaction;
	}

	public String getCurTransaction()
	{
		return curTransaction;
	}

	public void setCurTransaction(String curTransaction)
	{
		this.curTransaction = curTransaction;
	}

	public List<PerfSslTransactionDataDto> getPerfSslTransactionDataList()
	{
		return perfSslTransactionDataList;
	}

	public void setPerfSslTransactionDataList(
			List<PerfSslTransactionDataDto> perfSslTransactionDataList)
	{
		this.perfSslTransactionDataList = perfSslTransactionDataList;
	}

	@Override
	public String toString() 
	{
		return "PerfSslTransactionInfoDto [maxTransaction=" + maxTransaction
				+ ", minTransaction=" + minTransaction + ", avgTransaction="
				+ avgTransaction + ", curTransaction=" + curTransaction
				+ ", perfSslTransactionDataList=" + perfSslTransactionDataList
				+ "]";
	}

	public static PerfSslTransactionInfoDto getPerfSslTransacitonInfo(OBDtoAdcPerformance adcPerformance) throws Exception 
	{
		PerfSslTransactionInfoDto perfSslTransactionInfo = new PerfSslTransactionInfoDto();
		
		perfSslTransactionInfo.setMaxTransaction(NumberUtil.toStringWithUnit(adcPerformance.getSslTransMax(), ""));
		perfSslTransactionInfo.setMinTransaction(NumberUtil.toStringWithUnit(adcPerformance.getSslTransMin(), ""));
		perfSslTransactionInfo.setAvgTransaction(NumberUtil.toStringWithUnit(adcPerformance.getSslTransAvg(), ""));
		perfSslTransactionInfo.setCurTransaction(NumberUtil.toStringWithUnit(adcPerformance.getSslTransCurr(), ""));
		
		if (!CollectionUtils.isEmpty(adcPerformance.getSshTransList()))
		{
			for (OBDtoAdcPerfSslTrans adcPerfSslTransaction : adcPerformance.getSshTransList()) 
			{
				perfSslTransactionInfo.getPerfSslTransactionDataList().add(PerfSslTransactionDataDto.getPerfSslTransactionData(adcPerfSslTransaction));
			}
		}
		return perfSslTransactionInfo;
	}

}
