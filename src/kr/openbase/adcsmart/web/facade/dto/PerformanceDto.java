/**
 * 
 */
package kr.openbase.adcsmart.web.facade.dto;

import kr.openbase.adcsmart.service.dto.OBDtoAdcPerformance;

public class PerformanceDto
{
	private PerfSslTransactionInfoDto perfSslTransactionInfo;
	private PerfHttpRequestInfoDto perfHttpRequestInfo;
	private PerfActiveConnectionInfoDto perfActiveConnectionInfo;
	private PerfThroughputInfoDto perfThroughputInfo;

	public PerfSslTransactionInfoDto getPerfSslTransactionInfo() 
	{
		return perfSslTransactionInfo;
	}

	public void setPerfSslTransactionInfo(PerfSslTransactionInfoDto perfSslTransactionInfo)
	{
		this.perfSslTransactionInfo = perfSslTransactionInfo;
	}

	public PerfHttpRequestInfoDto getPerfHttpRequestInfo() 
	{
		return perfHttpRequestInfo;
	}

	public void setPerfHttpRequestInfo(PerfHttpRequestInfoDto perfHttpRequestInfo)
	{
		this.perfHttpRequestInfo = perfHttpRequestInfo;
	}

	public PerfActiveConnectionInfoDto getPerfActiveConnectionInfo() 
	{
		return perfActiveConnectionInfo;
	}

	public void setPerfActiveConnectionInfo(PerfActiveConnectionInfoDto perfActiveConnectionInfo) 
	{
		this.perfActiveConnectionInfo = perfActiveConnectionInfo;
	}

	public PerfThroughputInfoDto getPerfThroughputInfo() 
	{
		return perfThroughputInfo;
	}
	
	public void setPerfThroughputInfo(PerfThroughputInfoDto perfThroughputInfo)
	{
		this.perfThroughputInfo = perfThroughputInfo;
	}

	@Override
	public String toString() 
	{
		return "PerformanceDto [perfSslTransactionInfo="
				+ perfSslTransactionInfo + ", perfHttpRequestInfo="
				+ perfHttpRequestInfo + ", perfActiveConnectionInfo="
				+ perfActiveConnectionInfo + ", perfThroughputInfo="
				+ perfThroughputInfo + "]";
	}
	
	public static PerformanceDto getPerformance(OBDtoAdcPerformance adcPerformance) throws Exception
	{
		PerformanceDto performance = new PerformanceDto();
		
		performance.setPerfSslTransactionInfo(PerfSslTransactionInfoDto.getPerfSslTransacitonInfo(adcPerformance));
		performance.setPerfHttpRequestInfo(PerfHttpRequestInfoDto.getPerfHttpRequestInfo(adcPerformance));
		performance.setPerfActiveConnectionInfo(PerfActiveConnectionInfoDto.getPerfActiveConnectionInfo(adcPerformance));
		performance.setPerfThroughputInfo(PerfThroughputInfoDto.getPerfThroughputInfoDto(adcPerformance));
		
		return performance;
	}
	
}
