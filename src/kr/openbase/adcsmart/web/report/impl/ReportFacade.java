package kr.openbase.adcsmart.web.report.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kr.openbase.adcsmart.service.OBReport;
import kr.openbase.adcsmart.service.dto.OBReportInfo;
import kr.openbase.adcsmart.service.impl.report.OBReportImpl;
import kr.openbase.adcsmart.service.utility.OBException;

public class ReportFacade 
{
	private transient Logger log = LoggerFactory.getLogger(ReportFacade.class);
	
	private OBReport reportSvc;
	
	public ReportFacade() 
	{
		reportSvc = new OBReportImpl();
	}
	
	public List<ReportDto> getGenerationRequestedReports() throws OBException 
	{
		try 
		{
			List<OBReportInfo> reportsFromSvc = reportSvc.getReportInfoList(OBReportInfo.STATUS_INIT);
//			log.debug("{}", reportsFromSvc);
			List<ReportDto> reports = ReportDto.toReportDto(reportsFromSvc);
//			log.debug("{}", reports);
			return reports;
		}
		catch (OBException e) 
		{
			throw e;
		}
//		return ListUtils.EMPTY_LIST;
	}
	
	public ReportDto getGenerationRequestedReportInfo(String rptIndex) throws OBException 
	{
		try 
		{
			OBReportInfo reportsFromSvc = reportSvc.getReportInfo(rptIndex);
			log.debug("{}", reportsFromSvc);
			ReportDto reportInfo = ReportDto.toReportDto(reportsFromSvc);
			log.debug("{}", reportInfo);
			return reportInfo;
		} 
		catch (OBException e) 
		{
			throw e;
		}
	}
	
	public void setGenerationStatus(String reportIndex, String status) throws OBException 
	{
		try 
		{
//			log.debug("reportIndex:{}, status: {}", reportIndex, status);
			if (status.equals(ReportConstants.STATUS_INIT))
				reportSvc.updateReportStatus(reportIndex, OBReportInfo.STATUS_INIT);
			else if (status.equals(ReportConstants.STATUS_CREATING))
				reportSvc.updateReportStatus(reportIndex, OBReportInfo.STATUS_RUNNING);
			else if (status.equals(ReportConstants.STATUS_COMPLETE))
				reportSvc.updateReportStatus(reportIndex, OBReportInfo.STATUS_COMPLETE);
			else if (status.equals(ReportConstants.STATUS_FAIL))
				reportSvc.updateReportStatus(reportIndex, OBReportInfo.STATUS_ERROR);
		}
		catch (OBException e) 
		{
			throw e;
		}
	}
}
