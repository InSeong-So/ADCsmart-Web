package kr.openbase.adcsmart.web.facade.report;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import kr.openbase.adcsmart.service.OBReport;
import kr.openbase.adcsmart.service.dto.OBDtoExtraInfo;
import kr.openbase.adcsmart.service.dto.OBReportInfo;
import kr.openbase.adcsmart.service.impl.report.OBReportImpl;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.web.facade.dto.ReportAddDto;
import kr.openbase.adcsmart.web.facade.dto.ReportDto;
import kr.openbase.adcsmart.web.facade.dto.SessionDto;
import kr.openbase.adcsmart.web.util.OBDefineWeb;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ReportFacade 
{
	private static transient Logger log = LoggerFactory.getLogger(ReportFacade.class);
	
//	@Value("#{settings['report.saveDir']}") 
//	private String reportSaveDir;
	
	private OBReport reportSvc;

	public ReportFacade() 
	{
		reportSvc = new OBReportImpl();
	}
	
	public ReportDto getReport(String index) throws OBException, Exception 
	{
			log.debug("report index:{}", index);
			OBReportInfo reportFromSvc = reportSvc.getReportInfo(index);
			log.debug("{}", reportFromSvc);
			return ReportDto.toReportDto(reportFromSvc);
	}
	
	public List<ReportDto> getReports(Integer accountIndex, Integer adcIndex, Integer groupIndex, String searchKey, Date fromPeriod , Date toPeriod, Integer fromRow, Integer toRow, Integer orderType, Integer orderDir) throws OBException, Exception 
	{
			List<ReportDto> reports = new ArrayList<ReportDto>();
			log.debug("accountIndex:{}, adcIndex:{}, groupIndex:{}, searchKey:{}, fromPeriod:{} , toPeriod: {}, fromRow:{}, toRow:{} ", new Object[]{accountIndex, adcIndex, groupIndex, searchKey, fromPeriod, toPeriod, fromRow, toRow});
			List<OBReportInfo> reportsFromSvc = reportSvc.getReportInfoList(accountIndex, adcIndex, groupIndex, StringUtils.isEmpty(searchKey) ? null : searchKey, fromPeriod, toPeriod, fromRow, toRow, orderType, orderDir);
			log.debug("{}", reportsFromSvc);
			reports.addAll(ReportDto.toReportDto(reportsFromSvc));
			log.debug("{}", reports);
			return reports;		
	}
	
	public int getReportsTotal(Integer accountIndex, Integer adcIndex, Integer groupIndex, String searchKey, Date fromPeriod, Date toPeriod) throws OBException, Exception 
	{
			return reportSvc.getReportInfoListCount(accountIndex, adcIndex, groupIndex, StringUtils.isEmpty(searchKey) ? null : searchKey, fromPeriod, toPeriod);
	}
	
	public void addReport(ReportAddDto reportAdd, SessionDto session) throws OBException, Exception 
	{
			adjustPeriod(reportAdd);
			reportAdd.setAccountIndex(session.getAccountIndex());
//			reportAdd.setFileName(createOutFileName(reportAdd));
			OBReportInfo reportFromSvc = ReportAddDto.toOBReportInfo(reportAdd);
			log.debug("{}", reportFromSvc);
			OBDtoExtraInfo extraInfo = session.toOBDtoExtraInfo();
			extraInfo.setExtraMsg1(reportAdd.getName());
			String reportIndex = reportSvc.addReport(reportFromSvc, extraInfo);
			log.debug("reportIndex: {}", reportIndex);
//			reportAdd.setIndex(reportIndex);
//			List<SystemAdminSummaryDto> summaryList = SystemAdminReportFactory.getSystemAdminSummary(reportIndex, reportAdd.getAdcs());
//			System.out.println(summaryList);
//			createReport(reportAdd);
	}
	
	private void adjustPeriod(ReportAddDto reportAdd) throws OBException, Exception 
	{
		if (reportAdd.getPeriodType().equals(OBDefineWeb.RPT_PERIOD_PREVIOUSDATE))
		{
			reportAdd.setFromPeriod(reportAdd.getPreviousDate());
			reportAdd.setToPeriod(reportAdd.getPreviousDate());
		}
		
		Calendar period = Calendar.getInstance();
		if(!reportAdd.getReportType().equals(OBDefineWeb.MSG_DEFINEWEB_RPT_TYPE_ADC_DIAGNOSIS))
		{			
			period.setTime(reportAdd.getFromPeriod());
			period.set(Calendar.HOUR_OF_DAY, 0);
			period.set(Calendar.MINUTE, 0);
			period.set(Calendar.SECOND, 0);
			reportAdd.setFromPeriod(period.getTime());
			period.setTime(reportAdd.getToPeriod());
			period.set(Calendar.HOUR_OF_DAY, 23);
			period.set(Calendar.MINUTE, 59);
			period.set(Calendar.SECOND, 59);
			reportAdd.setToPeriod(period.getTime());
		}
		else
		{
			reportAdd.setFromPeriod(period.getTime());
			reportAdd.setToPeriod(period.getTime());
		}
	}
	
//	private String createOutFileName(ReportAddDto reportAdd) throws Exception {
//		String fileName = reportSaveDir;
//		fileName += reportAdd.getName();
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
//		fileName += "_" + sdf.format(new Date());
//		fileName += "." + reportAdd.getOutType();
//		return fileName;
//	}
//	
//	public void createReport(ReportAddDto reportAdd) throws Exception {
//		JRProperties properties = createReportProperties(reportAdd);
//		log.debug("{}", properties);
//		JRCreator jrCreator = new JRCreator();
//		reportSvc.setStatus(reportAdd.getIndex(), OBReportInfo.STATUS_RUNNING);
//		String destFile = jrCreator.make(properties);
//		log.debug("destFile:{}", destFile);
//		reportSvc.setStatus(reportAdd.getIndex(), destFile == null ? OBReportInfo.STATUS_ERROR : OBReportInfo.STATUS_COMPLETE);
//	}
//
//	private JRProperties createReportProperties(ReportAddDto reportAdd) throws OBException {
//		OBReportInfo reportFromSvc = reportSvc.getReportInfo(reportAdd.getIndex());
//		log.debug("{}", reportFromSvc);
//		ReportDto report = ReportDto.toReportDto(reportFromSvc);
//		log.debug("{}", report);
//		JRProperties properties = new JRProperties();
//		properties.setDestPathFile(report.getOutPathFile());
//		properties.setSubReportPath("./");
//		properties.setImgPath("./img/");
//		properties.setOutType(reportAdd.getOutType());
//		properties.setReportIndex(report.getIndex());
//		if (reportAdd.getReportType().equals(AdcConstants.RPT_TYPE_SYSADMIN))
//			properties.setSrcPath(FileUtils.toFile(getClass().getResource("/report/" + AdcConstants.RPT_FILENAME_SYSADMIN)).getPath());
//		else if (reportAdd.getReportType().equals(AdcConstants.RPT_TYPE_SYSFAULT))
//			properties.setSrcPath(FileUtils.toFile(getClass().getResource("/report/" + AdcConstants.RPT_FILENAME_SYSFAULT)).getPath());
//		else {
//			String msg = String.format("The output format %s isn't supported.", reportAdd.getOutType());
//			log.error(msg);
//			throw new IllegalArgumentException(msg);
//		}
//		return properties;
//	}
	
	public void delReports(List<String> reportIndices, SessionDto session) throws OBException, Exception 
	{
			OBDtoExtraInfo extraInfo = session.toOBDtoExtraInfo();
			extraInfo.setExtraMsg1(reportIndices.toString());
			reportSvc.delReport(new ArrayList<String>(reportIndices), extraInfo);
	}
}
