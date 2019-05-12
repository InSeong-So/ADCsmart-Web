package kr.openbase.adcsmart.web.report.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import kr.openbase.adcsmart.service.dto.OBDtoAdcName;
import kr.openbase.adcsmart.service.dto.report.OBDtoRptTitle;
import kr.openbase.adcsmart.service.utility.OBMessages;
import kr.openbase.adcsmart.web.report.fault.AdcDto;

import org.apache.commons.collections.CollectionUtils;

public class SystemReportDto 
{
	private Date 							creationTime;
	private Date 							fromPeriod;
	private Date 							toPeriod;
	private String 							accountId;
	private ArrayList<AdcDto> 				adcs;
	
	private SystemReportDtoTextHdr			textHdr;
	
	public static SystemReportDto toSystemReportDto(OBDtoRptTitle reportTitleFromSvc) 
	{
		SystemReportDto report = new SystemReportDto();
		report.setCreationTime(reportTitleFromSvc.getOccurTime());
		report.setFromPeriod(reportTitleFromSvc.getBeginTime());
		report.setToPeriod(reportTitleFromSvc.getEndTime());
		report.setAccountId(reportTitleFromSvc.getUserID());
		SystemReportDtoTextHdr textHdr=new SystemReportDtoTextHdr();
		textHdr.setOccurTime(OBMessages.getMessage(OBMessages.MSG_RPT_SYSTEM_INFO_OCCURTIME));//"보고서 생성 시간");
		textHdr.setPeriod(OBMessages.getMessage(OBMessages.MSG_RPT_SYSTEM_INFO_PERIOD));//"보고서 기간");
		textHdr.setUserName(OBMessages.getMessage(OBMessages.MSG_RPT_SYSTEM_INFO_USERNAME));//"사용자");
		textHdr.setTargetAdc(OBMessages.getMessage(OBMessages.MSG_RPT_SYSTEM_INFO_TARGETADC));//"보고서 대상(ADC)");
		report.setTextHdr(textHdr);

		if (!CollectionUtils.isEmpty(reportTitleFromSvc.getAdcList())) 
		{
			ArrayList<AdcDto> adcs = new ArrayList<AdcDto>(reportTitleFromSvc.getAdcList().size());
			for (OBDtoAdcName e : reportTitleFromSvc.getAdcList()) 
			{
				AdcDto adc = new AdcDto();
				adc.setIndex(e.getIndex());
				adc.setName(e.getName());
				adcs.add(adc);
			}
			
			report.setAdcs(adcs);
		}
		
		return report;
	}
	
	public Date getCreationTime() 
	{
		return creationTime;
	}
	public SystemReportDtoTextHdr getTextHdr()
	{
		return textHdr;
	}

	public void setTextHdr(SystemReportDtoTextHdr textHdr)
	{
		this.textHdr = textHdr;
	}

	public void setAdcs(ArrayList<AdcDto> adcs)
	{
		this.adcs = adcs;
	}

	public void setCreationTime(Date creationTime) 
	{
		this.creationTime = creationTime;
	}
	public Date getFromPeriod() 
	{
		return fromPeriod;
	}
	public void setFromPeriod(Date fromPeriod) 
	{
		this.fromPeriod = fromPeriod;
	}
	public Date getToPeriod()
	{
		return toPeriod;
	}
	public void setToPeriod(Date toPeriod) 
	{
		this.toPeriod = toPeriod;
	}

	public String getAccountId() 
	{
		return accountId;
	}

	public void setAccountId(String accountId) 
	{
		this.accountId = accountId;
	}

	public List<AdcDto> getAdcs() 
	{
		return adcs;
	}
	
	@Override
	public String toString()
	{
		return "SystemReportDto [creationTime=" + creationTime + ", fromPeriod=" + fromPeriod + ", toPeriod=" + toPeriod + ", accountId=" + accountId + ", adcs=" + adcs + ", textHdr=" + textHdr + ", getCreationTime()=" + getCreationTime() + ", getTextHdr()=" + getTextHdr() + ", getFromPeriod()=" + getFromPeriod() + ", getToPeriod()=" + getToPeriod() + ", getAccountId()=" + getAccountId() + ", getAdcs()=" + getAdcs() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString() + "]";
	}
	
}
