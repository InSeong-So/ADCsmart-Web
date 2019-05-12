package kr.openbase.adcsmart.web.report.impl;

public class ReportConstants 
{
	// Common
	public static final String STATUS_INIT = "init";
	public static final String STATUS_CREATING = "creating";
	public static final String STATUS_COMPLETE = "complete";
	public static final String STATUS_FAIL = "fail";
	// Report
	public static final String RPT_TYPE_SYSADMIN = "sysAdminReport";
	public static final String RPT_TYPE_SYSADMIN_ko = "시스템 운영 보고서";
	
	public static final String RPT_TYPE_SYSADMIN_TOTAL = "sysAdminTotalReport";
	public static final String RPT_TYPE_SYSADMIN_TOTAL_ko = "전체 운영 보고서";
	
	public static final String RPT_TYPE_SYSFAULT = "sysFalultReport";
	public static final String RPT_TYPE_SYSFAULT_ko = "장애 분석 보고서";
	
	public static final String RPT_TYPE_UNKNOWN = "unknownReport";

	public static final String RPT_TYPE_L4OPERATION = "l4OperationReport";
	public static final String RPT_TYPE_L4OPERATION_ko = "L4운영보고서";
	
	public static final String RPT_TYPE_L4DAILY 			= "l4OpDailyReport";
	public static final String RPT_TYPE_L4DAILY_ko 			= "L4운영보고서(일간)";
	public static final String RPT_TYPE_L4WEEKLY 			= "l4OpWeeklyReport";
	public static final String RPT_TYPE_L4WEEKLY_ko 		= "L4운영보고서(주간)";
	public static final String RPT_TYPE_L4MONTHLY 			= "l4OpMonthlyReport";
	public static final String RPT_TYPE_L4MONTHLY_ko 		= "L4운영보고서(월간)";
	
	public static final String RPT_TYPE_FAULTDIAGNOSIS 		= "ADCDiagnosisReport";
	public static final String RPT_TYPE_FAULTDIAGNOSIS_ko	= "ADC 진단 보고서";
	
	public static final String RPT_PERIOD_PREVIOUSDATE = "previousDate";
	public static final String RPT_PERIOD_CUSTUM = "custom";
	public static final String RPT_OUT_PDF = "pdf";
	public static final String RPT_OUT_RTF = "rtf";
	public static final String RPT_OUT_PPTX = "pptx";
	public static final String RPT_FILENAME_SYSADMIN 			= "SystemAdmin.jasper";
	public static final String RPT_FILENAME_SYSADMIN_TOTAL 	= "SystemAdminTotal.jasper";
	public static final String RPT_FILENAME_SYSFAULT 			= "SystemFault.jasper";
	public static final String RPT_FILENAME_L4OPERATION			= "L4Operation.jasper";
	public static final String RPT_FILENAME_L4OPERATION_BASIC	= "L4OperationBasic.jasper";
	public static final String RPT_FILENAME_FAULTDIAGNOSIS 		= "DiagnosisReport.jasper";
	
//	public static final String IMG_SUB_DIRECTORY_SDS = "./img/custom_sds/";
//	public static final String PROPERTY_FILE_NAME = "adcsmart.properties";
//	public static final String SDS_SITE_IDENTIFYER = "site.isSDSSite";
//	public static final String COMPARE_BOOLEAN_TRUE = "true";
//	public static final String COMPARE_BOOLEAN_FALSE = "false";
	
	public static final String NULL_TO_STRING = "-";  // 보고서에서 null 값 들어올 때 "-" 로 처리하도록 설정
	
}
