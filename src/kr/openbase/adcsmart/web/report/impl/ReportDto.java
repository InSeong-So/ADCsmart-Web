package kr.openbase.adcsmart.web.report.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import kr.openbase.adcsmart.service.dto.OBDtoAdcName;
import kr.openbase.adcsmart.service.dto.OBReportInfo;

public class ReportDto 
{
	private String index;
	private Date creationTime;
	private String creationStatus;
	private String name;
	private Date fromPeriod;
	private Date toPeriod;
	private String type;
	private List<String> adcNames;
	private List<Integer> adcIndex;
	private String accountId;
	private String outPathFile;
	private String outType;
	private String extraInfo;
	
	public static ReportDto toReportDto(OBReportInfo reportFromSvc) 
	{
		ReportDto report = new ReportDto();
		report.setIndex(reportFromSvc.getIndex());
		report.setCreationTime(reportFromSvc.getOccurTime());
		switch (reportFromSvc.getStatus()) 
		{
		case OBReportInfo.STATUS_INIT:
			report.setCreationStatus(ReportConstants.STATUS_INIT);	break;
		case OBReportInfo.STATUS_RUNNING:
			report.setCreationStatus(ReportConstants.STATUS_CREATING);	break;
		case OBReportInfo.STATUS_COMPLETE:
			report.setCreationStatus(ReportConstants.STATUS_COMPLETE);	break;
		case OBReportInfo.STATUS_ERROR:
			report.setCreationStatus(ReportConstants.STATUS_FAIL);	break;
		}
		report.setName(reportFromSvc.getName());
		report.setFromPeriod(reportFromSvc.getBeginTime());
		report.setToPeriod(reportFromSvc.getEndTime());
		switch (reportFromSvc.getRptType()) 
		{
		case OBReportInfo.RPT_TYPE_SYSTEM_OP:
			report.setType(ReportConstants.RPT_TYPE_SYSADMIN);	break;
		case OBReportInfo.RPT_TYPE_SYSTEM_OP_TOTAL:
			report.setType(ReportConstants.RPT_TYPE_SYSADMIN_TOTAL);	break;
		case OBReportInfo.RPT_TYPE_SYSTEM_FAULT:
			report.setType(ReportConstants.RPT_TYPE_SYSFAULT);	break;
		case OBReportInfo.RPT_TYPE_L4_OPERATION:
			report.setType(ReportConstants.RPT_TYPE_L4OPERATION);	break;
		case OBReportInfo.RPT_TYPE_L4_DAILY:
			report.setType(ReportConstants.RPT_TYPE_L4DAILY);	break;
		case OBReportInfo.RPT_TYPE_L4_MONTHLY:
			report.setType(ReportConstants.RPT_TYPE_L4MONTHLY);	break;
		case OBReportInfo.RPT_TYPE_L4_WEEKLY:
			report.setType(ReportConstants.RPT_TYPE_L4WEEKLY);	break;
		case OBReportInfo.RPT_TYPE_ADC_DIAGNOSIS:
			report.setType(ReportConstants.RPT_TYPE_FAULTDIAGNOSIS);	break;
		default:
			report.setType(ReportConstants.RPT_TYPE_UNKNOWN);
		};
		
		List<String> adcNames = new ArrayList<String>();
		List<Integer> adcIndex = new ArrayList<>();
		if (!CollectionUtils.isEmpty(reportFromSvc.getAdcList())) 
		{
			for (OBDtoAdcName e : reportFromSvc.getAdcList()){
				adcNames.add(e.getName());
				adcIndex.add(e.getIndex());
			}
		}
		
		report.setExtraInfo(reportFromSvc.getExtraInfo());
		
		report.setAdcNames(adcNames);
		report.setAdcIndex(adcIndex);
		report.setAccountId(reportFromSvc.getAccountID());
		report.setOutPathFile(reportFromSvc.getFileName());
		if (reportFromSvc.getFileType() == null)
			report.setOutType(ReportConstants.RPT_OUT_PDF);
		else if (reportFromSvc.getFileType() == OBReportInfo.FILE_TYPE_PDF)
			report.setOutType(ReportConstants.RPT_OUT_PDF);
		else if (reportFromSvc.getFileType() == OBReportInfo.FILE_TYPE_RTF)
			report.setOutType(ReportConstants.RPT_OUT_RTF);
		else if (reportFromSvc.getFileType() == OBReportInfo.FILE_TYPE_PPT)
			report.setOutType(ReportConstants.RPT_OUT_PPTX);
		else
			report.setOutType(ReportConstants.RPT_OUT_PDF);
		
		return report;
	}
	
	public static List<ReportDto> toReportDto(List<OBReportInfo> reportsFromSvc) 
	{
		List<ReportDto> reports = new ArrayList<ReportDto>();
		if (reportsFromSvc != null) 
		{
			for (OBReportInfo e : reportsFromSvc)
				reports.add(toReportDto(e));
		}
		
		return reports;
	}
	
	public String getExtraInfo()
	{
		return extraInfo;
	}

	public void setExtraInfo(String extraInfo)
	{
		this.extraInfo = extraInfo;
	}

	public String getIndex() 
	{
		return index;
	}

	public void setIndex(String index) 
	{
		this.index = index;
	}

	public Date getCreationTime() 
	{
		return creationTime;
	}
	public void setCreationTime(Date creationTime) 
	{
		this.creationTime = creationTime;
	}
	public String getCreationStatus() 
	{
		return creationStatus;
	}
	public void setCreationStatus(String creationStatus) 
	{
		this.creationStatus = creationStatus;
	}
	public String getName() 
	{
		return name;
	}
	public void setName(String name) 
	{
		this.name = name;
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
	public String getType() 
	{
		return type;
	}
	public void setType(String type) 
	{
		this.type = type;
	}
	public List<String> getAdcNames() 
	{
		return adcNames;
	}

	public void setAdcNames(List<String> adcNames)
	{
		this.adcNames = adcNames;
	}

	public String getAccountId()
	{
		return accountId;
	}
	public void setAccountId(String accountId) 
	{
		this.accountId = accountId;
	}
	
	public String getOutPathFile() 
	{
		return outPathFile;
	}

	public void setOutPathFile(String outPathFile) 
	{
		this.outPathFile = outPathFile;
	}

	public String getOutType() 
	{
		return outType;
	}

	public void setOutType(String outType) 
	{
		this.outType = outType;
	}

	public List<Integer> getAdcIndex() {
		return adcIndex;
	}

	public void setAdcIndex(List<Integer> adcIndex) {
		this.adcIndex = adcIndex;
	}

	@Override
	public String toString()
	{
		return "ReportDto [index=" + index + ", creationTime=" + creationTime
				+ ", creationStatus=" + creationStatus + ", name=" + name
				+ ", fromPeriod=" + fromPeriod + ", toPeriod=" + toPeriod
				+ ", type=" + type + ", adcNames=" + adcNames + ", accountId="
				+ accountId + ", outPathFile=" + outPathFile + ", outType="
				+ outType + "]";
	}
	
}
