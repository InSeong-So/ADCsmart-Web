package kr.openbase.adcsmart.web.report.jasper;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.service.utility.OBSystemLog;
import kr.openbase.adcsmart.web.report.fault.SystemFaultReportFactory;
import kr.openbase.adcsmart.web.report.faultdiagnosis.FaultDiagnosisFactory;
import kr.openbase.adcsmart.web.report.impl.ReportConstants;
import kr.openbase.adcsmart.web.report.inspection.InspectionReportFactory;
import kr.openbase.adcsmart.web.report.l4operation.L4Factory;
import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.engine.export.ooxml.JRPptxExporter;

/**
 * 이 클래스는 JasperReports를 이용한 보고서 생성과 관련한 기능을 제공한다.
 * @author oklee
 * @version 1.0
 */
public class JRCreator 
{
	private transient Logger log = LoggerFactory.getLogger(JRCreator.class);
	
	public String make(JRProperties properties) throws JRException 
	{
		String destFile = null;
		try 
		{
			Map<String, Object> params = createParams(properties);
			
			JasperPrint print = null;
			
			if (properties.getRptType().equals(ReportConstants.RPT_TYPE_SYSADMIN)){
				print = JasperFillManager.fillReport(properties.getSrcPath(), params, new JRBeanCollectionDataSource(InspectionReportFactory.getInspectionSummary(properties.getReportIndex())));
			}else if (properties.getRptType().equals(ReportConstants.RPT_TYPE_SYSFAULT)){
				print = JasperFillManager.fillReport(properties.getSrcPath(), params, new JRBeanCollectionDataSource(SystemFaultReportFactory.getSystemReport(properties.getReportIndex())));
			}else if (properties.getRptType().equals(ReportConstants.RPT_TYPE_L4OPERATION)){
				print = JasperFillManager.fillReport(properties.getSrcPath(), params, new JRBeanCollectionDataSource(L4Factory.getSystemReport(properties.getReportIndex())));
			}else if (properties.getRptType().equals(ReportConstants.RPT_TYPE_L4DAILY)){
				print = JasperFillManager.fillReport(properties.getSrcPath(), params, new JRBeanCollectionDataSource(L4Factory.getSystemReport(properties.getReportIndex())));
			}else if (properties.getRptType().equals(ReportConstants.RPT_TYPE_L4MONTHLY)){
				print = JasperFillManager.fillReport(properties.getSrcPath(), params, new JRBeanCollectionDataSource(L4Factory.getSystemReport(properties.getReportIndex())));
			}else if (properties.getRptType().equals(ReportConstants.RPT_TYPE_L4WEEKLY)){
				print = JasperFillManager.fillReport(properties.getSrcPath(), params, new JRBeanCollectionDataSource(L4Factory.getSystemReport(properties.getReportIndex())));
			}else if (properties.getRptType().equals(ReportConstants.RPT_TYPE_FAULTDIAGNOSIS)){
				print = JasperFillManager.fillReport(properties.getSrcPath(), params, new JRBeanCollectionDataSource(FaultDiagnosisFactory.getSystemReport(properties.getReportIndex())));
			}else{
				throw new OBException(OBException.ERRCODE_SYSTEM_PARAMETER);
			}
			
			destFile = exportReport(properties, print);
			OBSystemLog.info(OBDefine.LOGFILE_SYSTEM, String.format("report file : %s", destFile));
		} 
		catch (Exception e) 
		{
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("failed to generate report. error:%s", e.getMessage()));
			e.printStackTrace();
			log.error(e.getMessage());
			destFile = null;
		}

		return destFile != null ? destFile.substring(destFile.lastIndexOf("/")+1) : null;
	}
	
	private Map<String, Object> createParams(JRProperties property) 
	{
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("IMG_DIR", property.getImgPath());
		params.put("IMG_DIR_CUSTOM", property.getSubImgPath());
		params.put("SUBREPORT_DIR", property.getSubReportPath());
		params.put("REPORT_INDEX", property.getReportIndex());
		params.put("ACCUNT_INDEX", property.getAccntIndex());//파라미터 추가
		params.put("OUT_TYPE", property.getOutType());
		return params;
	}

	private String exportReport(JRProperties properties, JasperPrint print) throws JRException, IOException, Exception 
	{
		/* create an output file of 'jasper'*/
		
		try
		{
			String destPathFile = properties.getDestPathFile();
			createPathIfNotExists(destPathFile);
			@SuppressWarnings("rawtypes")
			JRAbstractExporter exporter = null;
			if (properties.getOutType().equals(ReportConstants.RPT_OUT_PDF)) 
			{
				exporter = new JRPdfExporter();
			} else if (properties.getOutType().equals(ReportConstants.RPT_OUT_RTF)) 
			{
				exporter = new JRRtfExporter();
			} 
			else if (properties.getOutType().equals(ReportConstants.RPT_OUT_PPTX)) 
			{
				exporter = new JRPptxExporter();
			} else {
				String msg = String.format("The output format %s isn't supported.", properties.getOutType());
				log.error(msg);
				throw new JRException(msg);
			}
				
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
			exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, destPathFile);
			exporter.exportReport();
			
			OBSystemLog.info(OBDefine.LOGFILE_SYSTEM, String.format("report file : %s", destPathFile));
			return destPathFile;
		}
		catch(OutOfMemoryError e) //exporter.exportReport()  에서rtf대용량 처리시 발생
		{
			e.printStackTrace();
			throw new Exception("Out of Memory");
			
		}
		catch(JRException e)
		{
			e.printStackTrace();
			throw e;
		}
		catch(IOException e)
		{
			e.printStackTrace();
			throw e;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw e;
		}
	}

	private void createPathIfNotExists(String pathFile) throws IOException 
	{
		String fullPath = FilenameUtils.getFullPath(pathFile);
		if (StringUtils.isEmpty(fullPath))
			return;
		
		FileUtils.forceMkdir(new File(fullPath));
	}
}
