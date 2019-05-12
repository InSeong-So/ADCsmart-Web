package kr.openbase.adcsmart.web.report.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import kr.openbase.adcsmart.service.dto.OBDtoAccount;
import kr.openbase.adcsmart.service.dto.OBDtoAdcInfo;
import kr.openbase.adcsmart.service.dto.OBReportInfo;
import kr.openbase.adcsmart.service.impl.OBAccountImpl;
import kr.openbase.adcsmart.service.impl.OBAdcManagementImpl;
import kr.openbase.adcsmart.service.impl.OBSystemAuditImpl;
import kr.openbase.adcsmart.service.utility.OBCommon;
import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.service.utility.OBSystemLog;
import kr.openbase.adcsmart.service.utility.OBUtility;
import kr.openbase.adcsmart.web.report.Report;
import kr.openbase.adcsmart.web.report.jasper.JRCreator;
import kr.openbase.adcsmart.web.report.jasper.JRProperties;
import kr.openbase.adcsmart.web.report.total.TotalCollectionMethod;
import kr.openbase.adcsmart.web.report.total.dto.TotalReportNormalDto;
import kr.openbase.adcsmart.web.report.total.service.TotalInspectionOperationImpl;
import kr.openbase.adcsmart.web.report.total.service.TotalReportUtil;
import net.sf.jasperreports.engine.JRException;

public class ReportImpl implements Report 
{
//	private transient Logger log = LoggerFactory.getLogger(ReportImpl.class);
	
	private ReportFacade reportFacade;
	
	public ReportImpl() 
	{
		reportFacade = new ReportFacade();
	}
	
	@Override
	public void generate() throws OBException
	{
		List<ReportDto> reports = reportFacade.getGenerationRequestedReports();
		for (int i=0; i < reports.size(); i++) 
		{
			ReportDto rpt = reports.get(i);
			try 
			{
				generateReport(rpt);

				new OBSystemAuditImpl().writeLog(0, "localhost", OBSystemAuditImpl.AUDIT_REPORT_CREATE_SUCCESS, rpt.getName());
			}
			catch (OBException e) 
			{
				new OBSystemAuditImpl().writeLog(0, "localhost", OBSystemAuditImpl.AUDIT_REPORT_CREATE_FAIL, rpt.getName());
				throw e;
			}
			catch (Exception e) 
			{
				new OBSystemAuditImpl().writeLog(0, "localhost", OBSystemAuditImpl.AUDIT_REPORT_CREATE_FAIL, rpt.getName());
				throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);
			}
		}
	}
	
	public void printException(Exception e){
		StackTraceElement[] ste=e.getStackTrace();
        String className=ste[0].getClassName();
        String meThodName=ste[0].getMethodName();
        int lineNumber=ste[0].getLineNumber();
        String fileName=ste[0].getFileName();
        OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("line:%s", className+"."+meThodName+" "+fileName+" "+lineNumber+"line"));
	}
	
	public boolean generateReport(ReportDto report) throws OBException 
	{
		try
		{
			String destFile=null;
			
			OBSystemLog.info(OBDefine.LOGFILE_SYSTEM, String.format("try to generate report. %s", report));
			reportFacade.setGenerationStatus(report.getIndex(), ReportConstants.STATUS_CREATING);

			if(report.getType().equals(ReportConstants.RPT_TYPE_SYSADMIN_TOTAL)){
				List<Integer> adcIndexList = report.getAdcIndex();
				List<TotalReportNormalDto> normalDtoList = new ArrayList<>();
				
				for(Integer index : adcIndexList){
					OBDtoAdcInfo adcInfo = new OBAdcManagementImpl().getAdcInfo(index);
					
					TotalCollectionMethod total = new TotalInspectionOperationImpl().getInstance(adcInfo);
					if(total == null) {
						continue;
					}
					TotalReportNormalDto nomalDto = total.getSystemInfo(new OBReportInfo());
					
					normalDtoList.add(nomalDto);
				}
				
				String resultHtml = TotalReportUtil.normalDtoToHtml(normalDtoList);
				boolean isSuccess = TotalReportUtil.htmlToPdf(resultHtml, report.getOutPathFile());
				
				if(isSuccess){
					destFile = report.getOutPathFile();
				}
			}else{
				JRProperties properties = createReportProperties(report);
				JRCreator jrCreator = new JRCreator();
				destFile = jrCreator.make(properties);
			}
			
			OBSystemLog.info(OBDefine.LOGFILE_SYSTEM, String.format("successfully generated report. destFile:%s", destFile));
			reportFacade.setGenerationStatus(report.getIndex(), destFile == null ? ReportConstants.STATUS_FAIL : ReportConstants.STATUS_COMPLETE);
			return destFile == null ? false : true;
		}
		catch(JRException e)//레포트 생성 시 상태변화 예외 처리
		{
			reportFacade.setGenerationStatus(report.getIndex(), ReportConstants.STATUS_FAIL);
			printException(e);
			throw new OBException(e.getMessage());
		}
		catch(OBException e)
		{
			reportFacade.setGenerationStatus(report.getIndex(), ReportConstants.STATUS_FAIL);
			printException(e);
			throw e;
		}
		catch(Exception e)
		{
			reportFacade.setGenerationStatus(report.getIndex(), ReportConstants.STATUS_FAIL);
			printException(e);
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);
		}
	}

	private  String siteImagePath() throws OBException
	{
		String locale = OBCommon.getLocalInfo().toLowerCase();
		if(locale.compareToIgnoreCase("ko_kr")==0)
			return "./img/";
		
		return "./img"+"_"+locale+"/";// for example: ./img_en_us/
//		if(OBParser.propertyToString(ReportConstants.PROPERTY_FILE_NAME, ReportConstants.SDS_SITE_IDENTIFYER).equals(ReportConstants.COMPARE_BOOLEAN_TRUE)) //삼성 싸이트 일 경우 레포트 이미지 폴더 경로
//		{
//			imgSubPath = ReportConstants.IMG_SUB_DIRECTORY_SDS;
//		}
//		return imgSubPath;
	}

	private  String getImagePath() throws OBException
	{
		String locale = OBCommon.getLocalInfo().toLowerCase();
		if(locale.compareToIgnoreCase("ko_kr")==0)
			return "./img/";
		
		return "./img"+"_"+locale+"/";// for example: ./img_en_us
	}
	
	private JRProperties createReportProperties(ReportDto report) throws OBException 
	{
		try
		{
			JRProperties properties = new JRProperties();
			properties.setDestPathFile(report.getOutPathFile());
			properties.setSubImgPath(siteImagePath());
			properties.setSubReportPath("./");
			properties.setImgPath(getImagePath());
			properties.setOutType(report.getOutType());
			properties.setReportIndex(report.getIndex());
			OBDtoAccount accountInfo =  new OBAccountImpl().getAccountInfo(report.getAccountId());
			properties.setAccntIndex(0);
			if(accountInfo != null)
				properties.setAccntIndex(accountInfo.getAccountIndex());
			
			if (report.getType().equals(ReportConstants.RPT_TYPE_SYSADMIN))
				properties.setSrcPath(FileUtils.toFile(getClass().getResource("/report/" + ReportConstants.RPT_FILENAME_SYSADMIN)).getPath());
			else if (report.getType().equals(ReportConstants.RPT_TYPE_SYSADMIN_TOTAL))
				properties.setSrcPath(FileUtils.toFile(getClass().getResource("/report/" + ReportConstants.RPT_FILENAME_SYSADMIN_TOTAL)).getPath());
			else if (report.getType().equals(ReportConstants.RPT_TYPE_SYSFAULT))
				properties.setSrcPath(FileUtils.toFile(getClass().getResource("/report/" + ReportConstants.RPT_FILENAME_SYSFAULT)).getPath());
			else if (report.getType().equals(ReportConstants.RPT_TYPE_L4OPERATION))
				properties.setSrcPath(FileUtils.toFile(getClass().getResource("/report/" + ReportConstants.RPT_FILENAME_L4OPERATION_BASIC)).getPath());
			else if (report.getType().equals(ReportConstants.RPT_TYPE_L4DAILY))
				properties.setSrcPath(FileUtils.toFile(getClass().getResource("/report/" + ReportConstants.RPT_FILENAME_L4OPERATION)).getPath());
			else if (report.getType().equals(ReportConstants.RPT_TYPE_L4MONTHLY))
				properties.setSrcPath(FileUtils.toFile(getClass().getResource("/report/" + ReportConstants.RPT_FILENAME_L4OPERATION)).getPath());// daily report와 동일한 jasper 파일 사용함. 
			else if (report.getType().equals(ReportConstants.RPT_TYPE_L4WEEKLY))
				properties.setSrcPath(FileUtils.toFile(getClass().getResource("/report/" + ReportConstants.RPT_FILENAME_L4OPERATION)).getPath());// daily report와 동일한 jasper 파일 사용함. 
			else if (report.getType().equals(ReportConstants.RPT_TYPE_FAULTDIAGNOSIS))
				properties.setSrcPath(FileUtils.toFile(getClass().getResource("/report/" + ReportConstants.RPT_FILENAME_FAULTDIAGNOSIS)).getPath());// daily report와 동일한 jasper 파일 사용함. 
			else 
			{
				throw new OBException(OBException.ERRCODE_SYSTEM_PARAMETER);
//				String msg = String.format("The report type %s isn't supported.", report.getType());
//				log.error(msg);
//				throw new IllegalArgumentException(msg);
			}
			properties.setRptType(report.getType());
			return properties;
		}
		catch(Exception e)
		{
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("보고서 생성 오류: %s", OBUtility.getStackTrace(e)));
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);
		}
	}

	@Override
	public void generate(String index) throws OBException
	{
//		log.info("aaa보고서 생성을 시작합니다.");
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. index:%s", index));
		ReportDto reportInfo = reportFacade.getGenerationRequestedReportInfo(index);
//		boolean isSuccessful = false;
		if(reportInfo != null)
		{
			try 
			{
//				OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("보고서 생성 시작: %s", reportInfo));
				if(generateReport(reportInfo)==true)
					new OBSystemAuditImpl().writeLog(0, "localhost", OBSystemAuditImpl.AUDIT_REPORT_CREATE_SUCCESS, reportInfo.getName());
				else
					new OBSystemAuditImpl().writeLog(0, "localhost", OBSystemAuditImpl.AUDIT_REPORT_CREATE_FAIL, reportInfo.getName());
			} 
			catch (OBException e) 
			{
				new OBSystemAuditImpl().writeLog(0, "localhost", OBSystemAuditImpl.AUDIT_REPORT_CREATE_FAIL, reportInfo.getName());
				throw e;
//				OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("aa보고서 생성 오류: %b, %s", isSuccessful, new OBUtility().getStackTrace(e)));
//				isSuccessful = false;
//				e.printStackTrace();
//				log.error(e.getMessage());
			}
			catch (Exception e) 
			{
				new OBSystemAuditImpl().writeLog(0, "localhost", OBSystemAuditImpl.AUDIT_REPORT_CREATE_FAIL, reportInfo.getName());
				throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);
//				OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("aa보고서 생성 오류: %b, %s", isSuccessful, new OBUtility().getStackTrace(e)));
//				isSuccessful = false;
//				e.printStackTrace();
//				log.error(e.getMessage());
			}
		}
		
//		log.info("{}/{} 보고서 생성 {}...", new Object[]{i+1, reports.size(), isSuccessful ? "성공" : "실패"});
		
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. index:%s", index));
	}
}
