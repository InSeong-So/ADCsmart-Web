package kr.openbase.adcsmart.web.facade.dto;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import kr.openbase.adcsmart.service.dto.OBDtoAdcName;
import kr.openbase.adcsmart.service.dto.OBReportInfo;
import kr.openbase.adcsmart.web.util.OBDefineWeb;

import org.apache.commons.collections.CollectionUtils;

public class ReportAddDto 
{
	private String index;
	private String reportType;
	private String periodType;
	private Date previousDate;
	private Date fromPeriod;
	private Date toPeriod;
	private String outType;
	private String name;
	private List<Integer> adcs;
	private Integer accountIndex;
	private String accountId;
	private Date creationTime;
//	private String fileName;
	private String logKey;
	private Integer groupIndex;
	
	public ReportAddDto() 
	{
		periodType = OBDefineWeb.RPT_PERIOD_PREVIOUSDATE;
		previousDate = getYesterday();
		creationTime = new Date();
	}
	
	private Date getYesterday() 
	{
		Calendar yesterday = Calendar.getInstance();
		yesterday.add(Calendar.DAY_OF_MONTH, -1);
		return yesterday.getTime();
	}
	
	public static OBReportInfo toOBReportInfo(ReportAddDto report) throws Exception 
	{
		OBReportInfo reportFromSvc = new OBReportInfo();
		reportFromSvc.setIndex(report.getIndex());
		reportFromSvc.setGroupIndex(report.getGroupIndex());
		reportFromSvc.setAccountIndex(report.getAccountIndex());
		reportFromSvc.setAccountID(report.getAccountId());
		reportFromSvc.setBeginTime(report.getFromPeriod());
		reportFromSvc.setEndTime(report.getToPeriod());
		reportFromSvc.setName(report.getName());
		reportFromSvc.setOccurTime(report.getCreationTime());
//		reportFromSvc.setFileName(report.getFileName());
		reportFromSvc.setExtraInfo(report.getLogKey());
		
		if (report.getOutType().equals(OBDefineWeb.RPT_OUT_PDF))
			reportFromSvc.setFileType(OBReportInfo.FILE_TYPE_PDF);
		else if (report.getOutType().equals(OBDefineWeb.RPT_OUT_RTF))
			reportFromSvc.setFileType(OBReportInfo.FILE_TYPE_RTF);
		else if (report.getOutType().equals(OBDefineWeb.RPT_OUT_PPTX))
			reportFromSvc.setFileType(OBReportInfo.FILE_TYPE_PPT);
		else 
			throw new IllegalArgumentException(OBDefineWeb.getDefineWeb(OBDefineWeb.MSG_NOT_SUPPORT_REPORT_OUT));
		
		if (report.getReportType().equals(OBDefineWeb.MSG_DEFINEWEB_RPT_TYPE_SYSADMIN))
			reportFromSvc.setRptType(OBReportInfo.RPT_TYPE_SYSTEM_OP);
		else if (report.getReportType().equals(OBDefineWeb.MSG_DEFINEWEB_RPT_TYPE_SYSFAULT))
			reportFromSvc.setRptType(OBReportInfo.RPT_TYPE_SYSTEM_FAULT);
		else if (report.getReportType().equals(OBDefineWeb.MSG_DEFINEWEB_RPT_TYPE_SYSADMIN_TOTAL))
			reportFromSvc.setRptType(OBReportInfo.RPT_TYPE_SYSTEM_OP_TOTAL);
		else if (report.getReportType().equals(OBDefineWeb.MSG_DEFINEWEB_RPT_TYPE_ADC_DIAGNOSIS))
			reportFromSvc.setRptType(OBReportInfo.RPT_TYPE_ADC_DIAGNOSIS);
		else if (report.getReportType().equals(OBDefineWeb.MSG_DEFINEWEB_RPT_TYPE_L4DAILY))
			reportFromSvc.setRptType(OBReportInfo.RPT_TYPE_L4_DAILY);
		else if (report.getReportType().equals(OBDefineWeb.MSG_DEFINEWEB_RPT_TYPE_L4WEEKLY))
			reportFromSvc.setRptType(OBReportInfo.RPT_TYPE_L4_WEEKLY);
		else if (report.getReportType().equals(OBDefineWeb.MSG_DEFINEWEB_RPT_TYPE_L4MONTHLY))
			reportFromSvc.setRptType(OBReportInfo.RPT_TYPE_L4_MONTHLY);
		else if (report.getReportType().equals(OBDefineWeb.MSG_DEFINEWEB_RPT_TYPE_L4OPERATION))
			reportFromSvc.setRptType(OBReportInfo.RPT_TYPE_L4_OPERATION);
		else
			throw new IllegalArgumentException(OBDefineWeb.getDefineWeb(OBDefineWeb.MSG_NOT_SUPPORT_REPORT));
		
		
		if (!CollectionUtils.isEmpty(report.getAdcs())) 
		{
			ArrayList<OBDtoAdcName> adcInfosFromSvc = new ArrayList<OBDtoAdcName>();
			for (Integer e : report.getAdcs()) 
			{
				OBDtoAdcName adcInfoFromSvc = new OBDtoAdcName();
				adcInfoFromSvc.setIndex(e);
				adcInfosFromSvc.add(adcInfoFromSvc);
			}
			
			reportFromSvc.setAdcList(adcInfosFromSvc);
		}
		
		return reportFromSvc;
	}

	public String getIndex()
	{
		return index;
	}

	public void setIndex(String index)
	{
		this.index = index;
	}

	public String getReportType()
	{
		return reportType;
	}

	public void setReportType(String reportType)
	{
		this.reportType = reportType;
	}

	public String getPeriodType()
	{
		return periodType;
	}

	public void setPeriodType(String periodType)
	{
		this.periodType = periodType;
	}

	public Date getPreviousDate()
	{
		return previousDate;
	}

	public void setPreviousDate(Date previousDate)
	{
		this.previousDate = previousDate;
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

	public String getOutType()
	{
		return outType;
	}

	public void setOutType(String outType)
	{
		this.outType = outType;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public List<Integer> getAdcs()
	{
		return adcs;
	}

	public void setAdcs(List<Integer> adcs)
	{
		this.adcs = adcs;
	}

	public Integer getAccountIndex()
	{
		return accountIndex;
	}

	public void setAccountIndex(Integer accountIndex)
	{
		this.accountIndex = accountIndex;
	}

	public String getAccountId()
	{
		return accountId;
	}

	public void setAccountId(String accountId)
	{
		this.accountId = accountId;
	}

	public Date getCreationTime()
	{
		return creationTime;
	}

	public void setCreationTime(Date creationTime)
	{
		this.creationTime = creationTime;
	}

	public String getLogKey()
	{
		return logKey;
	}

	public void setLogKey(String logKey)
	{
		this.logKey = logKey;
	}
	
	public Integer getGroupIndex()
    {
        return groupIndex;
    }

    public void setGroupIndex(Integer groupIndex)
    {
        this.groupIndex = groupIndex;
    }

    @Override
    public String toString()
    {
        return "ReportAddDto [index=" + index + ", reportType=" + reportType
                + ", periodType=" + periodType + ", previousDate="
                + previousDate + ", fromPeriod=" + fromPeriod + ", toPeriod="
                + toPeriod + ", outType=" + outType + ", name=" + name
                + ", adcs=" + adcs + ", accountIndex=" + accountIndex
                + ", accountId=" + accountId + ", creationTime=" + creationTime
                + ", logKey=" + logKey + ", groupIndex=" + groupIndex + "]";
    }
}
