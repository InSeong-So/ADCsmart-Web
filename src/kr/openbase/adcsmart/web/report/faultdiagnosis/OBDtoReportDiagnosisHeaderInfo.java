package kr.openbase.adcsmart.web.report.faultdiagnosis;

import java.util.ArrayList;
import java.util.Date;

import kr.openbase.adcsmart.service.dto.OBDtoAdcName;
import kr.openbase.adcsmart.web.report.impl.OBDtoReportTextHdr;

public class OBDtoReportDiagnosisHeaderInfo
{
	private Date 							creationTime;
	private Date 							fromPeriod;
	private Date 							toPeriod;
	private String 							accountId;
	private ArrayList<OBDtoAdcName> 		adcs;
	
	private OBDtoReportTextHdr				textHdr;

	private Integer existADC;// ADC 검사 항목이 있는지?
	private Integer existSrv;// 서비스 검사 항목이 있는지?
	private Integer existRespTime;// 응답 시간 검사 항목이 있는지?
	public Date getCreationTime()
	{
		return creationTime;
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
	public ArrayList<OBDtoAdcName> getAdcs()
	{
		return adcs;
	}
	public void setAdcs(ArrayList<OBDtoAdcName> adcs)
	{
		this.adcs = adcs;
	}
	public OBDtoReportTextHdr getTextHdr()
	{
		return textHdr;
	}
	public void setTextHdr(OBDtoReportTextHdr textHdr)
	{
		this.textHdr = textHdr;
	}
	public Integer getExistADC()
	{
		return existADC;
	}
	public void setExistADC(Integer existADC)
	{
		this.existADC = existADC;
	}
	public Integer getExistSrv()
	{
		return existSrv;
	}
	public void setExistSrv(Integer existSrv)
	{
		this.existSrv = existSrv;
	}
	public Integer getExistRespTime()
	{
		return existRespTime;
	}
	public void setExistRespTime(Integer existRespTime)
	{
		this.existRespTime = existRespTime;
	}
}
