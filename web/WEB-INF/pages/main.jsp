<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<meta http-equiv="X-UA-Compatible" content="IE=edge" />   
		<title>ADCsmart - Openbase</title>
		<link rel="shortcut icon" href="imgs/favicon.ico" />
		<link type="text/css" rel="stylesheet" href="css/daterangepicker-bs3.css"/>				
		<link type="text/css" rel="stylesheet" href="css/flowit-1.1.0.css" />
		<link type="text/css" rel="stylesheet" href="css/button.css" />
		<link type="text/css" rel="stylesheet" href="css/progress.css" />
		<link type="text/css" rel="stylesheet" href="css/comm.css"/>
		<link type="text/css" rel="stylesheet" href="css/widget_dashboard.css" />
		<link type="text/css" rel="stylesheet" href="css/newContent.css"/>
		<link type="text/css" rel="stylesheet" href="css/reset.css"/>	
		<link type="text/css" rel="stylesheet" href="css/style_default.css" />
		<link type="text/css" rel="stylesheet" href="css/fixedheadertable.css"/>
		<link type="text/css" rel="stylesheet" href="css/obalert.css"/>
		<script type="text/javascript" src="js/extern/log4javascript_lite.js"></script>
		<script type="text/javascript" src="js/extern/jquery-1.9.1.min.js"></script>
		<script type="text/javascript" src="js/extern/jquery-ui-1.10.2.custom.min.js"></script> 
		<script type="text/javascript" src="js/extern/jquery-migrate-1.2.1.min.js"></script>
		<script type="text/javascript" src="js/extern/jquery.nimble.loader-2.0.1.js"></script>
		<script type="text/javascript" src="js/extern/jquery.fixheadertable.min.js"></script>
		<script type="text/javascript" src="js/extern/jquery.ajaxmanager.min.js"></script>
		<script type="text/javascript" src="js/extern/jquery.form-custom.min.js"></script>
		<script type="text/javascript" src="js/extern/jquery.i18n.properties-min-1.0.9.js"></script>
		<script type="text/javascript" src="js/extern/jquery.fixedheadertable.js"></script>
		<script type="text/javascript" src="js/extern/flowit-1.2.0.min.js"></script>
		<script type="text/javascript" src="js/extern/amchart/amcharts.js"></script>
		<script type="text/javascript" src="js/extern/amchart/pie.js"></script>
		<script type="text/javascript" src="js/extern/amchart/serial.js"></script>
		<script type="text/javascript" src="js/extern/obajax-1.2.0.js"></script>
		<script type="text/javascript" src="js/extern/justgage.1.0.1.js"></script>
		<script type="text/javascript" src="js/extern/raphael.2.1.0.min.js"></script>
		<script type="text/javascript" src="js/extern/progress.js"></script>
		<script type="text/javascript" src="js/extern/jquery.PrintArea.js"></script>
		<script type="text/javascript" src="js/extern/sha256.js"></script>
		<script type="text/javascript" src="js/extern/sha512.js"></script>
		<script type="text/javascript" src="js/common.js"></script>
		<script type="text/javascript" src="js/header.js"></script>
		<script type="text/javascript" src="js/monitor/monitorLoader.js"></script>
		<script type="text/javascript" src="js/monitor/networkMap.js"></script>
		<script type="text/javascript" src="js/monitor/alert/alertMonitoring.js"></script>
		<script type="text/javascript" src="js/monitor/fault/faultMonitoring.js"></script>
		<script type="text/javascript" src="js/monitor/statistics.js"></script>
		<script type="text/javascript" src="js/monitor/adcMonitor.js"></script>
		<script type="text/javascript" src="js/monitor/serviceMonitor.js"></script>
		<script type="text/javascript" src="js/monitor/groupMonitor.js"></script>
		<script type="text/javascript" src="js/monitor/realServerMonitor.js"></script>
		<script type="text/javascript" src="js/monitor/monitorAppliance.js"></script>
		<script type="text/javascript" src="js/monitor/monitorServicePerfomance.js"></script>
		<script type="text/javascript" src="js/monitor/monitorServicePerfomanceRealServer.js"></script>
		<script type="text/javascript" src="js/monitor/flbGroupPerfomance.js"></script>
		<script type="text/javascript" src="js/monitor/flbGroupPerfomanceRealServer.js"></script>		
		<script type="text/javascript" src="js/monitor/sessionMonitoring.js"></script>
		<script type="text/javascript" src="js/monitor/monitorL2Info.js"></script>
		<script type="text/javascript" src="js/monitor/adcLog.js"></script>
		<script type="text/javascript" src="js/monitor/flbInfo/flbInfo.js"></script>
		<script type="text/javascript" src="js/setting/alertSetting/adcAlert.js"></script>
		<script type="text/javascript" src="js/setting/alertSetting/groupAlert.js"></script>
		<script type="text/javascript" src="js/virtualserver/slbSchedule.js"></script>
		<script type="text/javascript" src="js/virtualserver/virtualServer.js"></script>
		<script type="text/javascript" src="js/virtualserver/alteonvs.js"></script>
		<script type="text/javascript" src="js/virtualserver/f5vs.js"></script>
		<script type="text/javascript" src="js/virtualserver/pasvs.js"></script>
		<script type="text/javascript" src="js/virtualserver/paskvs.js"></script>
		<script type="text/javascript" src="js/virtualserver/vservernamewnd.js"></script>
		<script type="text/javascript" src="js/virtualserver/portchangewnd.js"></script>
		<script type="text/javascript" src="js/virtualserver/portchangepaskwnd.js"></script>
		<script type="text/javascript" src="js/virtualserver/memberaddbatchwnd.js"></script>
		<script type="text/javascript" src="js/virtualserver/memberaddbatchpaswnd.js"></script>
		<script type="text/javascript" src="js/virtualserver/memberaddbatPASKchwnd.js"></script>
		<script type="text/javascript" src="js/virtualserver/profile.js"></script>
		<script type="text/javascript" src="js/virtualserver/persistencedetailwnd.js"></script>
		<script type="text/javascript" src="js/virtualserver/node.js"></script>
		<script type="text/javascript" src="js/virtualserver/groupNode.js"></script>
		<script type="text/javascript" src="js/virtualserver/noticeGrp.js"></script>
		<script type="text/javascript" src="js/dashboard/dashboardLoader.js"></script>
		<script type="text/javascript" src="js/adc/adcSetting.js"></script>
		<script type="text/javascript" src="js/adc/configCheck.js"></script>
		<script type="text/javascript" src="js/adc/adcHistory.js"></script>		
		<script type="text/javascript" src="js/tools/JSToolsMain.js"></script>
		<script type="text/javascript" src="js/tools/JSToolsPortInfo.js"></script>
		<script type="text/javascript" src="js/tools/JSToolsSlbSessionInfo.js"></script>
		<script type="text/javascript" src="js/tools/JSToolsUnusedSlbInfo.js"></script>
		<script type="text/javascript" src="js/respTime/respTime.js"></script>
		<script type="text/javascript" src="js/report/report.js"></script>		
		<script type="text/javascript" src="js/sysSetting/auditLog.js"></script>		
		<script type="text/javascript" src="js/sysSetting/config.js"></script>
		<script type="text/javascript" src="js/sysSetting/license.js"></script>
		<script type="text/javascript" src="js/sysSetting/pwchangewnd.js"></script>
		<script type="text/javascript" src="js/sysSetting/sysSetting.js"></script>
		<script type="text/javascript" src="js/sysSetting/systemInfo.js"></script>		
		<script type="text/javascript" src="js/sysSetting/logContentInfo.js"></script>		
		<script type="text/javascript" src="js/sysSetting/serviceGrpConfig.js"></script>
        <script type="text/javascript" src="js/sysSetting/vsFilterSetting.js"></script>
		<script type="text/javascript" src="js/etc/securityNotice.js"></script>
		<script type="text/javascript" src="js/extern/jquery.jstree.js"></script>
		<script type="text/javascript" src="js/extern/obchart-2.0.0.js"></script>
		<script type="text/javascript" src="js/extern/jquery.ticker.min.js"></script>
		<script type="text/javascript" src="js/faultDiagnosis/diagnosisSetting/faultSetting.js"></script>
		<script type="text/javascript" src="js/faultDiagnosis/diagnosisHistory/faultHistory.js"></script>
		<script type="text/javascript" src="js/faultDiagnosis/diagnosisHistory/faultResult.js"></script>
		<script type="text/javascript" src="js/faultDiagnosis/diagnosisSetting/faultDiagnosis.js"></script>
		<script type="text/javascript" src="js/faultDiagnosis/diagnosisAnalysis/faultAnalysis.js"></script>
		<script type="text/javascript" src="js/faultDiagnosis/diagnosisAnalysis/faultAnalysisPopup.js"></script>
		<script type="text/javascript" src="js/layout/alertwnd.js"></script>		
		<script type="text/javascript" src="js/message.js"></script>
		<script type="text/javascript" src="js/extern/textile.js"></script>
		<script type="text/javascript" src="js/extern/jquery.obvalidation.js"></script>
		<script type="text/javascript" src="js/extern/jquery.obalert.js"></script>
		<script type="text/javascript" src="js/extern/bootstrap.min.js"></script>
		<script type="text/javascript" src="js/extern/moment.min.js"></script>
		<script type="text/javascript" src="js/extern/daterangepicker.js"></script>
		<script type="text/javascript" src="js/dashboard/dashboardHeader.js"></script>		
		<script type="text/javascript" src="js/dashboard/widgetChart.js"></script>
		<script type="text/javascript" src="js/dashboard/adcServiceTotal/dashboardServiceHeader.js"></script>		
		
		<!-- angular 라이브러리  -->
		<script type="text/javascript" src="js/extern/angular.min.js"></script>
		<!-- controller include -->
		<script type="text/javascript" src="js/tools/sysViewDump.js"></script>
		
		
		<%@ taglib prefix="s" uri="/struts-tags" %>
	</head>
	<span class="Slider_open none"><a href="javascript:;" class="listcontainer_open"><img src="imgs/meun/btn_mov_rgt2.png" title="ADC목록 열기"></a></span>     
	<body>
		<div class="header"></div>
		<div id="wrap">
			<div class="listcontainer"></div>
			<div class="contents"></div>		
			
			<!-- 보안경고 팝업 -->
<!-- 			<div id="noticePop" class="pop_type_wrap"> -->
<!-- 				<h4>Warning</h4> -->
				
<%-- 				<s:if test ="varIsSDSSite"> --%>
<!-- 				<div class="pop_contents"> -->
<!-- 					<p> -->
<%-- 					<span class="pop_contents2"></span> --%>
<!-- 					</p>				 -->
<!-- 				</div> -->
<%-- 				</s:if> --%>
<%-- 				<s:else>			 --%>
<!-- 				<div class="pop_contents adcsmart"> -->
<!-- 					<p> -->
<%-- 					<span class="pop_contents2"></span> --%>
<!-- 					</p>				 -->
<!-- 				</div> -->
<%-- 				</s:else> --%>
	      
<!-- 				<div class="center mar_rgt10 mar_btm5"> -->
<%-- 					<span id="refresh"> --%>
<!-- 	      				<input class="Btn_white closeWndLnk" type="button" value="닫기"> -->
<%-- 					</span>             --%>
<!-- 				</div> -->
<%-- 				<span class="close none_pad"> --%>
<!-- 					<a class="closeWndLnk" href="#" title="닫기"><img src="imgs/popup/btn_clse.gif" alt="닫기" /></a> -->
<%-- 				</span> --%>
<!-- 			</div> -->
			<!-- //보안경고 팝업 -->	
			
		</div>
		<iframe id="downloadFrame" class="none"></iframe>
		<script type="text/javascript">
			var langCode="ko_KR";// 언어 코드. 기본은 한국어.
	 		var log = log4javascript.getNullLogger();
	//		var log = log4javascript.getDefaultLogger();
			var taskQ;
			var ajaxManager;
			var ajaxManagerOB;
			var header;
			var monitorLoader;
			var dashboardLoader;
			var dashboardHeader;
			var dashboardServiceHeader;
			var widgetChart;
			var adcSetting;
			var config;
			var adcHistory;
			var adcLog;
			var virtualServer;
			var profile;
			var node;
			var groupNode;
			var noticeGrp;
			var report;
			var sysSetting;
			var auditLog;
			var sysBackup;
			var systemInfo;
			var securityNoticeWnd;
			var config;
			var license;		
			var adcAlert;
			var groupAlert;
			var jsToolsMain;
			var obchart;
			var faultSetting;
			var faultHistory;
			var faultResult;
			var faultDiagnosis;
			var sessionMonitoring;
			var faultAnalysis;
			var faultAnalysisPopup;
			var networkMap;
			var statistics;
			var alertMonitoring;
			var faultMonitoring;
			var adcMonitor;
			var serviceMonitor;
			var groupMonitor;
			var realServerMonitor;
			var monitorAppliance;
			var monitorServicePerfomance;
			var monitorServicePerfomanceRealServer;
			var monitorL2Info;
			var flbGroupPerfomance;
			var flbGroupPerfomanceRealServer;
			var flbInfo;
			var logContentInfo;
			var configCheck;
			var respTime;
			var serviceGrpConfig;
						
			$(function() 
			{
				retrieveInitMessages();

				retrievePasswdResetWarningMsg();
								
			});
			
			function loadCssFile(langCode)
			{
				var filename = [];
				if(langCode == "en_US")
				{
					filename = ["css/eng/style_default_eng.css" , "css/eng/newContent_eng.css", "css/eng/reset_eng.css", "css/eng/comm_eng.css"];
				}
							
				for (var i = 0; i < filename.length; i++ )
				{	
					var fileref = document.createElement("link");
				    fileref.rel = "stylesheet";
				    fileref.type = "text/css";
				    fileref.href = filename[i];			
				    document.getElementsByTagName("head")[0].appendChild(fileref);
				}			  
			}
			
			/* 
			//추후 css 추가 후 default css 삭제 기능 필요함.
			function loadCssFile(langCode)
			{
				var filename = [];
				if(langCode == "ko_KR")
				{
					filename = ["css/style_default.css" , "css/newContent.css", "css/reset.css", "css/comm.css"];
				}
				else
				{
					filename = ["css/eng/style_default_eng.css" , "css/eng/newContent_eng.css", "css/eng/reset_eng.css", "css/eng/comm_eng.css"];
				}
							
				for (var i = 0; i < filename.length; i++ )
				{	
					var fileref = document.createElement("link");
				    fileref.rel = "stylesheet";
				    fileref.type = "text/css";
				    fileref.href = filename[i];			
				    document.getElementsByTagName("head")[0].appendChild(fileref);
				}			  
			}
			*/
/*			
			function loadCssFileTest(langCode)
		    {
				var filename = "";
				var filename1 = "";
				if(langCode == "ko_KR")
				{
					filename = "css/style_default.css";	
					filename1 = "css/newContent.css";
				}
				else 
				{
					filename = "css_eng/style_default.css";
					filename1 = "css_eng/newContent.css";
				}
				
				var fileref = ""; 
				fileref = document.createElement("link");
			    fileref.rel = "stylesheet";
			    fileref.type = "text/css";
			    fileref.href = filename;
			    document.getElementsByTagName("head")[0].appendChild(fileref);
			    
			    var fileref1 = "";
			    fileref1 = document.createElement("link");
			    fileref1.rel = "stylesheet";
			    fileref1.type = "text/css";
			    fileref1.href = filename1;		    
			    document.getElementsByTagName("head")[0].appendChild(fileref1);
		    }		
*/			
			function main()
			{
				taskQ = new FlowitTaskQ();
				ajaxManager = new FlowitAjax();
				ajaxManagerOB = new OBAjax();
				header = new Header();
				monitorLoader = new MonitorLoader();
				networkMap = new NetworkMap();
				statistics = new Statistics();
				alertMonitoring = new AlertMonitoring();
				faultMonitoring = new FaultMonitoring();
				adcMonitor = new AdcMonitor();
				serviceMonitor = new ServiceMonitor();
				groupMonitor = new GroupMonitor();
				realServerMonitor = new RealServerMonitor();
				monitorAppliance = new MonitorAppliance();
				monitorL2Info = new MonitorL2Info();
				monitorServicePerfomance = new MonitorServicePerfomance();
				monitorServicePerfomanceRealServer = new MonitorServicePerfomanceRealServer();
				flbGroupPerfomance = new FlbGroupPerfomance();
				flbGroupPerfomanceRealServer = new FlbGroupPerfomanceRealServer();				
				flbInfo = new FlbInfo();
				dashboardLoader = new DashboardLoader();
				dashboardHeader = new DashboardHeader();
				dashboardServiceHeader = new DashboardServiceHeader();
				widgetChart = new WidgetChart();	
				adcSetting = new AdcSetting();
				configCheck = new ConfigCheck()
				adcHistory = new AdcHistory();
				adcLog = new AdcLog();
				virtualServer = new VirtualServer();
				slbSchedule = new SlbSchedule();
				profile = new Profile();
				node = new Node();
				groupNode = new GroupNode();
				noticeGrp = new NoticeGrp();
				report = new Report();
				sysSetting = new SysSetting();
				auditlog = new ClassAuditLog();
				sysBackup = new SysBackup();
				systemInfo = new SystemInfo();
				securityNoticeWnd = new SecurityNoticeWnd();
				config = new Config();				//설정 - 환경설정
				license = new License();			//설정 - 라이선스	
				adcAlert = new AdcAlert();
				groupAlert = new GroupAlert();
				jsToolsMain = new JSToolsMain();
				obchart = new OBChart();
				faultSetting = new FaultSetting();
				faultHistory = new FaultHistory();
				faultResult = new FaultResult();
				faultDiagnosis = new FaultDiagnosis();
				sessionMonitoring = new SessionMonitoring();
				faultAnalysis = new FaultAnalysis();
				faultAnalysisPopup = new FaultAnalysisPopup();
				logContentInfo	= new LogContentInfo();
				configCheck = new ConfigCheck();
				respTime = new RespTime();
				serviceGrpConfig = new ServiceGrpConfig();
				vsFilterConfig = new VsFilterConfig();
				header.loadHeader();
				securityNoticeWnd.popUpIfPropertySet(function() 
				{
					var isPasswordReset = ${isPasswordReset};
					FlowitUtil.log('isPasswordReset:', isPasswordReset);
					
					taskQ.add(function() 
					{
						adcSetting.loadLeftPane(undefined, taskQ);
					});
					if (!isPasswordReset)
					{
						//alert("start");
						taskQ.add(function() {	//alert("start2");
							//initials.loadContent();
							//adcSetting.setObjOnAdcChange(monitorLoader);
							monitorLoader.loadContent(undefined, taskQ);
							//header.loadContent();
							header.loadMainContent();
							//initials.loadContent(undefined, taskQ);
						});
					}
					else
					{
						alert('비밀번호를 변경하십시오.');
						taskQ.add(function() {
							$('.loginIdLnk').click();
							taskQ.notifyTaskDone();
						});
					}				
					taskQ.start();
				});
			}

			function retrievePasswdResetWarningMsg() 
			{
				FlowitUtil.getJSON({
					url : "member/retrievePasswdResetWarningMsg.action",
					successFn : function(data) 
					{
						if(data.isPasswordResetWarningMsg){
							alert(data.isPasswordResetWarningMsg);
						}
					},
					completeFn: function(textStatus) 
					{
						log.debug('retrieveCookies: ' + textStatus);
					}
				});
			}

		    function retrieveInitMessages() 
		    {
		    	FlowitUtil.getJSON({
		    		url : "member/retrieveLangCode.action",
		    		successFn : function(data) 
		    		{
		    			langCode  = data.langCode;
		    			loadLanguageMessages(langCode);
		    			loadCssFile(langCode);
		    			main();
		    		},
		    		completeFn: function(textStatus) 
		    		{
		    		}
		    	});
		    }
		    
		    function loadLanguageMessages (langCode) 
		    {
		    	jQuery.i18n.properties({
		    	    name:'Messages', 
		    	    path:'bundle/',
		    	    mode:'both',
		    	    language:langCode, 
		    	    callback: function() {		
		    	    	
		    	    	// AJAX Alert
		    	    	VAR_AJAX_ALERT_0 					= jQuery.i18n.prop('VAR_AJAX_ALERT_0');										// 서버와 일시적으로 연결할 수 없습니다.  \n잠시 후에 새로고침(F5) 하시기 바랍니다
		    	    			    	    	
		    	    	// Common
		    	    	VAR_COMMON_REGISUCCESS 				= jQuery.i18n.prop('VAR_COMMON_REGISUCCESS');								// 성공적으로 등록하였습니다.	 
		    	    	VAR_COMMON_DATEERROR				= jQuery.i18n.prop('VAR_COMMON_DATEERROR');									// 종료날짜가 시작날짜 보다 작습니다. 기간을 다시 설정해 주십시오.
		    	    	VAR_COMMON_SYSTEMSETFAIL			= jQuery.i18n.prop('VAR_COMMON_SYSTEMSETFAIL'); 							// 시스템 설정 변경에 실패했습니다.	
		    	    	VAR_COMMON_IPINPUT 					= jQuery.i18n.prop('VAR_COMMON_IPINPUT');									// IP를 입력하세요.
		    	    	VAR_COMMON_PORTINPUT 				= jQuery.i18n.prop('VAR_COMMON_PORTINPUT');									// Port를 입력하세요.
		    	    	VAR_COMMON_PORTINPUTRANGE 			= jQuery.i18n.prop('VAR_COMMON_PORTINPUTRANGE');;							// 입력하신 포트는 범위에 맞지 않습니다. 가능한 포트 범위(0~65535)
		    	    	VAR_COMMON_IPFORMAT					= jQuery.i18n.prop('VAR_COMMON_IPFORMAT'); 									// 입력하신 IP의 형식이 올바르지 않습니다.
		    	    	VAR_COMMON_PEERIPFORMAT				= jQuery.i18n.prop('VAR_COMMON_PEERIPFORMAT'); 								// 입력하신 PeerIP의 형식이 올바르지 않습니다.
		    	    	VAR_COMMON_ADCLISTLOAD				= jQuery.i18n.prop('VAR_COMMON_ADCLISTLOAD');								// ADC 목록 로딩에 실패했습니다.
		    	    	VAR_COMMON_EXPDATAEXIST				= jQuery.i18n.prop('VAR_COMMON_EXPDATAEXIST'); 								// 내보내기 데이터 존재 유무 검사에 실패했습니다.
		    	    	VAR_COMMON_STATRTDATE				= jQuery.i18n.prop('VAR_COMMON_STATRTDATE'); 								// 시작날짜를 입력하세요.
						VAR_COMMON_ENDDATE					= jQuery.i18n.prop('VAR_COMMON_ENDDATE'); 									// 종료날짜를 입력하세요.
						VAR_COMMON_PASSWDINPUT				= jQuery.i18n.prop('VAR_COMMON_PASSWDINPUT'); 								// 비밀 번호를 입력하세요.
						VAR_COMMON_PASSWDCONFIRM			= jQuery.i18n.prop('VAR_COMMON_PASSWDCONFIRM'); 							// 비밀번호 확인을 입력하세요.
						VAR_COMMON_HISTORYDATAEXTRACT		= jQuery.i18n.prop('VAR_COMMON_HISTORYDATAEXTRACT'); 						// 패킷 수집 이력 데이터 추출에 실패하였습니다.
						VAR_COMMON_STATUSEXTRACT 			= jQuery.i18n.prop('VAR_COMMON_STATUSEXTRACT'); 							// 패킷 수집 상태 추출에 실패하였습니다.
						VAR_COMMON_PASNOTSUPPORT			= jQuery.i18n.prop('VAR_COMMON_PASNOTSUPPORT');								// PAS에서는 지원되지 않는 기능입니다.
						VAR_COMMON_PASKNOTSUPPORT			= jQuery.i18n.prop('VAR_COMMON_PASKNOTSUPPORT');							// PASK에서는 지원되지 않는 기능입니다.
						VAR_COMMON_F5NOTSUPPORT				= jQuery.i18n.prop('VAR_COMMON_F5NOTSUPPORT');								// F5에서는 지원되지 않는 기능입니다.
						VAR_COMMON_ALTEON23NOTSUPPORT		= jQuery.i18n.prop('VAR_COMMON_ALTEON23NOTSUPPORT');						// Alteon 2-3 series 에서는 지원되지 않는 기능입니다.
						VAR_COMMON_CPUMEMDATAEXTRACT 		= jQuery.i18n.prop('VAR_COMMON_CPUMEMDATAEXTRACT');							// CPU/메모리 데이터 추출에 실패하였습니다.
						VAR_COMMON_AFEW						= jQuery.i18n.prop('VAR_COMMON_AFEW');										// 개
						VAR_COMMON_BPS						= jQuery.i18n.prop('VAR_COMMON_BPS');										// bps
						VAR_COMMON_PPS						= jQuery.i18n.prop('VAR_COMMON_PPS');										// pps
						VAR_COMMON_PORT						= jQuery.i18n.prop('VAR_COMMON_PORT');										// 포트
						VAR_COMMON_IP						= jQuery.i18n.prop('VAR_COMMON_IP');										// IP
						VAR_COMMON_MENUOPEN					= jQuery.i18n.prop('VAR_COMMON_MENUOPEN'); 									// 메뉴 열기
						VAR_COMMON_MENUCLOSE				= jQuery.i18n.prop('VAR_COMMON_MENUCLOSE'); 								// 메뉴 닫기
						VAR_COMMON_ONLINE					= jQuery.i18n.prop('VAR_COMMON_ONLINE');									// 정상
						VAR_COMMON_OFFLINE					= jQuery.i18n.prop('VAR_COMMON_OFFLINE');									// 단절
						VAR_COMMON_DISABLED					= jQuery.i18n.prop('VAR_COMMON_DISABLED');									// 꺼짐
						VAR_COMMON_MACFORMAT 				= jQuery.i18n.prop('VAR_COMMON_MACFORMAT');									// 입력하신 MAC의 형식이 올바르지 않습니다.
						VAR_COMMON_LENGTHFORMAT				= jQuery.i18n.prop('VAR_COMMON_LENGTHFORMAT');								// 입력하신 값의 길이가 올바르지 않습니다. 							// 입력하신 값의 길이가 올바르지 않습니다.
						VAR_COMMON_SPECIALCHAR 				= jQuery.i18n.prop('VAR_COMMON_SPECIALCHAR');								// 특수문자가 포함되었습니다. 입력하신 값은 범위에 맞지 않습니다.						
						VAR_COMMON_NUMLENGTH 				= jQuery.i18n.prop('VAR_COMMON_NUMLENGTH');									// 숫자만 허용 가능합니다. 입력하신 값은 길이에 맞지 않습니다.
						VAR_COMMON_NUMRANGE 				= jQuery.i18n.prop('VAR_COMMON_NUMRANGE');									// 숫자만 허용 가능합니다. 입력하신 값은 범위에 맞지 않습니다.
						VAR_COMMON_EMAILFORMAT 				= jQuery.i18n.prop('VAR_COMMON_EMAILFORMAT');								// 입력하신 EAMIL 주소의 형식이 올바르지 않습니다.
						VAR_COMMON_PHONEFORMAT 				= jQuery.i18n.prop('VAR_COMMON_PHONEFORMAT');								// 입력하신 전화번호의 형식이 올바르지 않습니다.
						VAR_COMMON_IPPORTFORMAT 			= jQuery.i18n.prop('VAR_COMMON_IPPORTFORMA');								// 입력하신 IP:PORT의 형식이 올바르지 않습니다.
						VAR_COMMON_CANCEL					= jQuery.i18n.prop('VAR_COMMON_CANCEL');									// 취소
						VAR_COMMON_COMPLETE					= jQuery.i18n.prop('VAR_COMMON_COMPLETE');									// 완료
						VAR_COMMON_FAIL						= jQuery.i18n.prop('VAR_COMMON_FAIL');										// 실패
						
						// 단위
						VAR_COMMON_KBPS                     = jQuery.i18n.prop('VAR_COMMON_KBPS');                                   	// Kbps
						VAR_COMMON_MBPS                     = jQuery.i18n.prop('VAR_COMMON_MBPS');                                   	// Mbps
						VAR_COMMON_GBPS                     = jQuery.i18n.prop('VAR_COMMON_GBPS');                                   	// Gbps
						VAR_COMMON_TBPS                     = jQuery.i18n.prop('VAR_COMMON_TBPS');                                   	// Tbps
						VAR_COMMON_KPPS						= jQuery.i18n.prop('VAR_COMMON_KPPS'); 										// Kpps
						VAR_COMMON_MPPS						= jQuery.i18n.prop('VAR_COMMON_MPPS'); 										// Mpps
						VAR_COMMON_GPPS						= jQuery.i18n.prop('VAR_COMMON_GPPS');										// Gpps 
						VAR_COMMON_TPPS						= jQuery.i18n.prop('VAR_COMMON_TPPS');										// Tpps
						VAR_COMMON_TPS 	                    = jQuery.i18n.prop('VAR_COMMON_TPS');                                   	// tps
						VAR_COMMON_KTPS                     = jQuery.i18n.prop('VAR_COMMON_KTPS');                                   	// Ktps
						VAR_COMMON_MTPS                     = jQuery.i18n.prop('VAR_COMMON_MTPS');                                   	// Mtps
						VAR_COMMON_GTPS                     = jQuery.i18n.prop('VAR_COMMON_GTPS');                                   	// Gtps
						VAR_COMMON_TTPS                     = jQuery.i18n.prop('VAR_COMMON_TTPS');                                   	// Ttps
						VAR_COMMON_RPS 	                    = jQuery.i18n.prop('VAR_COMMON_RPS');                                   	// rps
						VAR_COMMON_KRPS                     = jQuery.i18n.prop('VAR_COMMON_KRPS');                                   	// Krps
						VAR_COMMON_MRPS                     = jQuery.i18n.prop('VAR_COMMON_MRPS');                                   	// Mrps
						VAR_COMMON_GRPS                     = jQuery.i18n.prop('VAR_COMMON_GRPS');                                   	// Grps
						VAR_COMMON_TRPS                     = jQuery.i18n.prop('VAR_COMMON_TRPS');                                   	// Trps
						
						// Protocol
						VAR_COMMON_TCP						= jQuery.i18n.prop('VAR_COMMON_TCP');										// TCP
						VAR_COMMON_UDP						= jQuery.i18n.prop('VAR_COMMON_UDP');										// UDP
						VAR_COMMON_ICMP						= jQuery.i18n.prop('VAR_COMMON_ICMP');										// ICMP
						
						// Time
						VAR_COMMON_HOUR 					= jQuery.i18n.prop('VAR_COMMON_HOUR');										// hours
						VAR_COMMON_MIN 						= jQuery.i18n.prop('VAR_COMMON_MIN');										// minutes
						VAR_COMMON_SEC 						= jQuery.i18n.prop('VAR_COMMON_SEC');										// seconds
						
		    	    	// Common VS
						VAR_COMMON_VSLFAIL	 				= jQuery.i18n.prop('VAR_COMMON_VSLFAIL');	 								// Virtual Server 데이터 로딩에 실패했습니다.
						VAR_COMMON_PVSAMFAIL				= jQuery.i18n.prop('VAR_COMMON_PVSAMFAIL');									// Peer 장비의 Virtual Server 추가/수정에 실패했습니다.
						VAR_COMMON_VSAMFAIL					= jQuery.i18n.prop('VAR_COMMON_VSAMFAIL');									// Virtual Server 추가/수정에 실패했습니다.
						VAR_COMMON_AVAIL					= jQuery.i18n.prop('VAR_COMMON_AVAIL');										// 사용 가능
						VAR_COMMON_NAVAIL					= jQuery.i18n.prop('VAR_COMMON_NAVAIL');									// 사용 불가능
						VAR_COMMON_VSPRDISAGREE				= jQuery.i18n.prop('VAR_COMMON_VSPRDISAGREE');								// 입력하신 가상서버 포트는 범위에 맞지 않습니다. 가능한 포트 범위(10~65534)
						VAR_COMMON_EMSELECT					= jQuery.i18n.prop('VAR_COMMON_EMSELECT');									// Enable을 원하는 Member를 선택하십시오.
						VAR_COMMON_DMSELECT					= jQuery.i18n.prop('VAR_COMMON_DMSELECT');									// Disable을 원하는 Member를 선택하십시오.
						VAR_COMMON_MDSELECT					= jQuery.i18n.prop('VAR_COMMON_MDSELECT');									// 삭제를 원하는 Member를 선택하십시오.
						VAR_COMMON_MDEL						= jQuery.i18n.prop('VAR_COMMON_MDEL');										// Member를 삭제 하시겠습니까?
						VAR_COMMON_PMSELECT					= jQuery.i18n.prop('VAR_COMMON_PMSELECT');									// 포트변경을 원하는 Member를 선택하십시오.
						VAR_COMMON_VNLFAIL					= jQuery.i18n.prop('VAR_COMMON_VNLFAIL');									// Virtual Server 이름 로딩에 실패했습니다.
						VAR_COMMON_VEIFAIL					= jQuery.i18n.prop('VAR_COMMON_VEIFAIL');									// Virtual Server 존재 유무 검사에 실패했습니다.
						VAR_COMMON_IIFORMAT					= jQuery.i18n.prop('VAR_COMMON_IIFORMAT');									// IP 주소 형식이 올바르지 않습니다.
						VAR_COMMON_VEFAIL					= jQuery.i18n.prop('VAR_COMMON_VEFAIL');									// Virtual Server 데이터 추출에 실패했습니다.
						VAR_COMMON_REFAIL					= jQuery.i18n.prop('VAR_COMMON_REFAIL');									// Real Server 데이터 추출에 실패했습니다.
						VAR_COMMON_ARMEMBER					= jQuery.i18n.prop('VAR_COMMON_ARMEMBER');									// 은(는) 이미 등록된 멤버입니다.
						VAR_COMMON_PINPUT					= jQuery.i18n.prop('VAR_COMMON_PINPUT');									// 포트정보를 입력하세요.
						VAR_COMMON_PEIFAIL					= jQuery.i18n.prop('VAR_COMMON_PEIFAIL');									// Peer 장비 존재 유무 검사에 실패했습니다.
						VAR_COMMON_PIFORMAT					= jQuery.i18n.prop('VAR_COMMON_PIFORMAT');									// 포트형식이 올바르지 않습니다.
						//VAR_COMMON_PORT					= jQuery.i18n.prop('VAR_COMMON_PORT');										// 의 포트
						VAR_COMMON_VIFORMAT					= jQuery.i18n.prop('VAR_COMMON_VIFORMAT');									// 입력하신 가상서버 IP는 올바른 형식이 아닙니다.
						VAR_COMMON_VNOTALLOWED				= jQuery.i18n.prop('VAR_COMMON_VNOTALLOWED');								// 입력하신 가상서버 이름은 허용되지 않습니다.
						VAR_COMMON_ALLADC					= jQuery.i18n.prop('VAR_COMMON_ALLADC');									// 전체 ADC
						VAR_COMMON_IPPORT_DUPLICATION		= jQuery.i18n.prop('VAR_COMMON_IPPORT_DUPLICATION');						// 중복된 IP Port 입니다. 다시 입력해주세요.
						
						// schedule
						VAR_SCHEDULE_REQ_SELECT 			= jQuery.i18n.prop('VAR_SCHEDULE_REQ_SELECT');								// 선택을 원하는 요청자를 선택하십시오.
						VAR_SCHEDULE_REQ_DSELECT			= jQuery.i18n.prop('VAR_SCHEDULE_REQ_DSELECT');								// 삭제를 원하시는 요청자를 선택하십시오.
						VAR_SCHEDULE_REQ_DEL				= jQuery.i18n.prop('VAR_SCHEDULE_REQ_DEL');									// 요청자를 삭제 하시겠습니까?
						
						// daterangepicker
						VAR_DATE_TODAY 						= jQuery.i18n.prop('VAR_DATE_TODAY');										// 오늘
						VAR_DATE_YESTERDAY 					= jQuery.i18n.prop('VAR_DATE_YESTERDAY');									// 최근 1일
						VAR_DATE_LAST7DAYS 					= jQuery.i18n.prop('VAR_DATE_LAST7DAYS');									// 최근 7일
						VAR_DATE_LAST30DAYS 				= jQuery.i18n.prop('VAR_DATE_LAST30DAYS');									// 최근 30일
						VAR_DATE_THISMONTH 					= jQuery.i18n.prop('VAR_DATE_THISMONTH');									// 이번 달
						VAR_DATE_LASTMONTH 					= jQuery.i18n.prop('VAR_DATE_LASTMONTH');									// 지난 달
						
						// SYSSETTING
		    	    	VAR_SYSSETTING_LOADUSERLISTCONTENT 	= jQuery.i18n.prop('VAR_SYSSETTING_LOADUSERLISTCONTENT'); 					// 사용자 목록 로딩에 실패했습니다. 
		    	    	VAR_SYSSETTING_LOADUSERADDCONTENT  	= jQuery.i18n.prop('VAR_SYSSETTING_LOADUSERADDCONTENT');  					// 사용자 관리 페이지 로딩에 실패했습니다.	  	
		    	    	VAR_SYSSETTING_USERADDMODIFY 		= jQuery.i18n.prop('VAR_SYSSETTING_USERADDMODIFY'); 						// 사용자 추가/편집에 실패했습니다.
		    	    	VAR_SYSSETTING_PASSWDRESET			= jQuery.i18n.prop('VAR_SYSSETTING_PASSWDRESET');							// 비밀번호를 초기화하시겠습니까?
		    	    	VAR_SYSSETTING_IDINPUT				= jQuery.i18n.prop('VAR_SYSSETTING_IDINPUT');								// 아이디를 입력하세요.
		    	    	VAR_SYSSETTING_IDRULEWRONG			= jQuery.i18n.prop('VAR_SYSSETTING_IDRULEWRONG');							// 아이디 규칙이 맞지 않습니다. \n 아이디는 알파벳으로 시작하고 알파벳과 숫자 조합의 5~16자 사이로 구성됩니다.	
		    	    	VAR_SYSSETTING_USERNAMEINPUT 		= jQuery.i18n.prop('VAR_SYSSETTING_USERNAMEINPUT');							// 사용자 이름을 입력하세요.
		    	    	VAR_SYSSETTING_PASSWDINPUT			= jQuery.i18n.prop('VAR_SYSSETTING_PASSWDINPUT');							// 비밀 번호를 입력하세요.
		    	    	VAR_SYSSETTING_PASSWDCONFIRMINPUT 	= jQuery.i18n.prop('VAR_SYSSETTING_PASSWDCONFIRMINPUT'); 					// 비밀번호 확인을 입력하세요.
		    	    	VAR_SYSSETTING_PASSWDMATCH			= jQuery.i18n.prop('VAR_SYSSETTING_PASSWDMATCH');							// 비밀번호와 비밀번호 확인 입력 값이 일치하지 않습니다.
		    	    	VAR_SYSSETTING_PASSWDRULEWRONG		= jQuery.i18n.prop('VAR_SYSSETTING_PASSWDRULEWRONG');						// 비밀번호 규칙이 맞지 않습니다. \n 비밀번호는 알파벳과 숫자 조합의 5~16자 사이로 구성됩니다.
		    	    	VAR_SYSSETTING_PASSWD_RULEWRONG		= jQuery.i18n.prop('VAR_SYSSETTING_PASSWD_RULEWRONG');						// 비밀번호 규칙이 맞지 않습니다. \n 비밀번호는 알파벳과 숫자 조합의 8~16자 사이로 구성됩니다.
		    	       	VAR_SYSSETTING_PASSWDRESET			= jQuery.i18n.prop('VAR_SYSSETTING_PASSWDRESET');							// 비밀번호를 초기화하였습니다.
		    	    	VAR_SYSSETTING_PASSWDRESETFAIL		= jQuery.i18n.prop('VAR_SYSSETTING_PASSWDRESETFAIL');						// 사용자 계정의 패스워드 초기화에 실패했습니다.
		    	    	VAR_SYSSETTING_ACCOUNTDELSEL		= jQuery.i18n.prop('VAR_SYSSETTING_ACCOUNTDELSEL');							// 삭제를 원하시는 계정을 선택하십시오.	    	    	
		    	    	VAR_SYSSETTING_ACCOUNTDEL 			= jQuery.i18n.prop('VAR_SYSSETTING_ACCOUNTDEL'); 							// 계정을 삭제 하시겠습니까?
		    	    	VAR_SYSSETTING_ACCOUNTDELFAIL		= jQuery.i18n.prop('VAR_SYSSETTING_ACCOUNTDELFAIL');						// 사용자 삭제에 실패했습니다.
		    	    	VAR_SYSSETTING_SYSTEMINFOLOAD		= jQuery.i18n.prop('VAR_SYSSETTING_SYSTEMINFOLOAD');						// 시스템 설정 정보 로딩에 실패했습니다.
		    	    	VAR_SYSSETTING_ADMINLOGLOAD			= jQuery.i18n.prop('VAR_SYSSETTING_ADMINLOGLOAD');							// 로그 파일 로딩에 실패했습니다.
		    	    	VAR_SYSSETTING_ACCOUNTIDCHECK       = jQuery.i18n.prop('VAR_SYSSETTING_ACCOUNTIDCHECK');                 	    // 중복된 아이디입니다. 다시 입력해주세요.
		    	    	VAR_SYSSETTING_SYSTEM_TIME_SYNC_FAIL= jQuery.i18n.prop('VAR_SYSSETTING_SYSTEM_TIME_SYNC_FAIL');					// 시스템 시간 동기화에 실패했습니다.
		    	    	VAR_SYSSETTING_NOVSADC 				= jQuery.i18n.prop('VAR_SYSSETTING_NOVSADC');								// RS선택에서 해당 ADC가 존재하지 않습니다.
		    	    	VAR_SYSSETTING_NORSADC 				= jQuery.i18n.prop('VAR_SYSSETTING_NORSADC');								// VS선택에서 해당 ADC가 존재하지 않습니다.
		    	    	VAR_SYSSETTING_RESPONSETIMELOAD		= jQuery.i18n.prop('VAR_SYSSETTING_RESPONSETIMELOAD');						// 구간 응답시간 Check 로딩에 실패했습니다.
		    	    	VAR_SYSSETTING_GROUPDELSEL			= jQuery.i18n.prop('VAR_SYSSETTING_GROUPDELSEL');							// 삭제를 원하시는 그룹을 선택하십시오.
		    	    	VAR_SYSSETTING_GROUPDEL 			= jQuery.i18n.prop('VAR_SYSSETTING_GROUPDEL');								// 서비스 그룹을 삭제 하시겠습니까?
		    	    	VAR_SYSSETTING_DASHGROUPDEL 		= jQuery.i18n.prop('VAR_SYSSETTING_DASHGROUPDEL');							// 대시보드에 등록된 서비스 그룹이 함께 삭제 됩니다.
		    	    	VAR_SYSSETTING_GROUPDELFAIL			= jQuery.i18n.prop('VAR_SYSSETTING_GROUPDELFAIL');							// 서비스 그룹 삭제에 실패했습니다.
		    	    	VAR_SYSSETTING_GROUPINFOLOAD		= jQuery.i18n.prop('VAR_SYSSETTING_GROUPINFOLOAD');							// 서비스 그룹 정보 로딩에 실패했습니다.
		    	    	VAR_SYSSETTING_GROUPNAME 			= jQuery.i18n.prop('VAR_SYSSETTING_GROUPNAME');								// 서비스 그룹이름을 입력하세요.
		    	    	VAR_SYSSETTING_LOGFILESEL 			= jQuery.i18n.prop('VAR_SYSSETTING_LOGFILESEL');							// 로그파일을 선택하세요.
		    	    	
		    	    	// SYSTOOLS
		    	    	VAR_SYSTOOLS_RESPADDFAIL			= jQuery.i18n.prop('VAR_SYSTOOLS_RESPADDFAIL');								// 구간 추가에 실패했습니다.
						VAR_SYSTOOLS_RESPLOADFAIL	 		= jQuery.i18n.prop('VAR_SYSTOOLS_RESPLOADFAIL');							// 구간 응답시간 데이터 로딩에 실패했습니다.
						VAR_SYSTOOLS_RESPDELSEL				= jQuery.i18n.prop('VAR_SYSTOOLS_RESPDELSEL');								// 삭제를 원하시는 구간을 선택하십시오.
						VAR_SYSTOOLS_RESPDEL				= jQuery.i18n.prop('VAR_SYSTOOLS_RESPDEL');									// 구간을 삭제 하시겠습니까?
						VAR_SYSTOOLS_RESPDELFAIL			= jQuery.i18n.prop('VAR_SYSTOOLS_RESPDELFAIL');								// 구간 삭제에 실패했습니다.	
						VAR_SYSTOOLS_TOP					= jQuery.i18n.prop('VAR_SYSTOOLS_TOP');										// 최상단입니다.
						VAR_SYSTOOLS_BOTTOM					= jQuery.i18n.prop('VAR_SYSTOOLS_BOTTOM');									// 최하단입니다.
						VAR_SYSTOOLS_RESPNAME				= jQuery.i18n.prop('VAR_SYSTOOLS_RESPNAME');								// 구간 이름을 입력하세요.
						VAR_SYSTOOLS_RANGEOVER				= jQuery.i18n.prop('VAR_SYSTOOLS_RANGEOVER');								// 추가 허용 개수가 초과되었습니다.
						VAR_SYSTOOLS_MOVEONEROWSEL			= jQuery.i18n.prop('VAR_SYSTOOLS_MOVEONEROWSEL');							// 이동하려는 행을 하나만 선택해주세요.
						VAR_SYSTOOLS_MOVEROWSEL				= jQuery.i18n.prop('VAR_SYSTOOLS_MOVEROWSEL');								// 이동 하려는 행을 선택해주세요.
						VAR_SYSTOOLS_DELROWSEL				= jQuery.i18n.prop('VAR_SYSTOOLS_DELROWSEL');								// 삭제 하려는 행을 선택해주세요.
						VAR_SYSTOOLS_ATLEASTONE				= jQuery.i18n.prop('VAR_SYSTOOLS_ATLEASTONE');								// 최소 하나의 멤버는 있어야 합니다.
		    	    	
		    	    	// SYSBACK
		    	    	VAR_SYSBACK_EXTRACTION				= jQuery.i18n.prop('VAR_SYSBACK_EXTRACTION'); 								// 백업 정보 추출에 실패했습니다.
		    	    	VAR_SYSBACK_LOAD					= jQuery.i18n.prop('VAR_SYSBACK_LOAD');										// 백업 정보 로딩에 실패했습니다.
		    	    	VAR_SYSBACK_DELSEL					= jQuery.i18n.prop('VAR_SYSBACK_DELSEL');									// 삭제를 원하시는 백업을 선택하십시오.
		    	    	VAR_SYSBACK_DEL						= jQuery.i18n.prop('VAR_SYSBACK_DEL');										// 백업을 삭제 하시겠습니까?
		    	    	VAR_SYSBACK_FILENAMEINPUT			= jQuery.i18n.prop('VAR_SYSBACK_FILENAMEINPUT'); 							// 백업 파일명을 입력하세요.
		    	    	VAR_SYSBACK_DOWNLOAD				= jQuery.i18n.prop('VAR_SYSBACK_DOWNLOAD'); 								// 백업 정보 다운로드에 실패했습니다.
		    	    	VAR_SYSBACK_DELFAIL					= jQuery.i18n.prop('VAR_SYSBACK_DELFAIL');									// 백업정보 삭제에 실패했습니다.
		    	    	VAR_SYSBACK_ADDMODIFYFAIL			= jQuery.i18n.prop('VAR_SYSBACK_ADDMODIFYFAIL'); 							// 백업 추가/변경에 실패했습니다.
		    	    	
		    	    	// SYSINFO
		    	    	VAR_SYSINFO_LOAD					= jQuery.i18n.prop('VAR_SYSINFO_LOAD');										// 시스템 정보 로딩에 실패했습니다.
		    	    	
						// 시스템관리 (라이선스) - license
						VAR_LICENSE_LOAD					= jQuery.i18n.prop('VAR_LICENSE_LOAD');										// 라이센스 정보 로딩에 실패했습니다.
						VAR_LICENSE_FILEUPLOAD				= jQuery.i18n.prop('VAR_LICENSE_FILEUPLOAD');								// 라이센스 파일 업로드에 실패했습니다.
						VAR_LICENSE_FILENAMEDIFFER			= jQuery.i18n.prop('VAR_LICENSE_FILENAMEDIFFER');							// 파일명이 다릅니다.
						VAR_LICENSE_NOTLICFILE				= jQuery.i18n.prop('VAR_LICENSE_NOTLICFILE');								// lic 파일이 아닙니다.
						VAR_LICENSE_NOFILE 					= jQuery.i18n.prop('VAR_LICENSE_NOFILE');									// 선택된 파일이 없습니다.
						
						// 시스템관리 (설정) - config
						VAR_CONFIG_LOAD 					= jQuery.i18n.prop('VAR_CONFIG_LOAD'); 										// 시스템 설정 정보 로딩에 실패했습니다.
						VAR_CONFIG_ADCLOGFILTERSEL 			= jQuery.i18n.prop('VAR_CONFIG_ADCLOGFILTERSEL'); 							// 삭제를 원하시는 ADC 로그 필터를 선택하십시오.
						VAR_CONFIG_ADCLOGFILTERDEL 			= jQuery.i18n.prop('VAR_CONFIG_ADCLOGFILTERDEL'); 							// ADC 로그 필터를 삭제 하시겠습니까?
						VAR_CONFIG_PATTERNINPUT 			= jQuery.i18n.prop('VAR_CONFIG_PATTERNINPUT'); 								// 패턴을 입력하세요.
						VAR_CONFIG_AGREETYPESEL 			= jQuery.i18n.prop('VAR_CONFIG_AGREETYPESEL'); 								// 일치타입을 선택하세요.
						VAR_CONFIG_SECTION 					= jQuery.i18n.prop('VAR_CONFIG_SECTION'); 									// 부분
						VAR_CONFIG_WHOLE 					= jQuery.i18n.prop('VAR_CONFIG_WHOLE'); 									// 전체
						VAR_CONFIG_NETMASKINPUT 			= jQuery.i18n.prop('VAR_CONFIG_NETMASKINPUT'); 								// Netmask를 입력하세요.
						VAR_CONFIG_GATEWAYINPUT 			= jQuery.i18n.prop('VAR_CONFIG_GATEWAYINPUT'); 								// Gateway를 입력하세요.
						VAR_CONFIG_SYSTEMNAMEINPUT 			= jQuery.i18n.prop('VAR_CONFIG_SYSTEMNAMEINPUT'); 							// 시스템명을 입력하세요.
						VAR_CONFIG_SYNCCYCLEINPUT 			= jQuery.i18n.prop('VAR_CONFIG_SYNCCYCLEINPUT'); 							// ADC 설정 동기화 주기를 입력하세요.
						VAR_CONFIG_LISTCOUNTINPUT			= jQuery.i18n.prop('VAR_CONFIG_LISTCOUNTINPUT');							// 목록 표시 개수를 입력하세요.
						VAR_CONFIG_ADCMONITCYCLE			= jQuery.i18n.prop('VAR_CONFIG_ADCMONITCYCLE');								// 주기는 30초에서 600초 사이의 시간을 입력하세요.
						VAR_CONFIG_VSSELECT					= jQuery.i18n.prop('VAR_CONFIG_VSSELECT');									// VS를 선택해주세요.
						
						// 경보 Alert
						VAR_ALERT_LOAD						= jQuery.i18n.prop('VAR_ALERT_LOAD');										// 경보 설정 데이터 로딩에 실패했습니다.
						VAR_ALERT_SETFAIL					= jQuery.i18n.prop('VAR_ALERT_SETFAIL');									// 경보 설정에 실패했습니다.
						VAR_ALERT_RESPABNORMAL				= jQuery.i18n.prop('VAR_ALERT_RESPABNORMAL'); 								// 응답시간이 비정상적입니다. 0~10000 사이값을 입력하십시오.
							
						// slb이력 AdcHistory
						VAR_HISTORY_LOAD 					= jQuery.i18n.prop('VAR_HISTORY_LOAD');										// 설정 이력 데이터 로딩에 실패했습니다.
						VAR_HISTORY_RECOVERINSPECTIO 		= jQuery.i18n.prop('VAR_HISTORY_RECOVERINSPECTIO');							// 설정 이력 복구 여부 검사에 실패했습니다.
						VAR_HISTORY_PEERSLBRECOVER 			= jQuery.i18n.prop('VAR_HISTORY_PEERSLBRECOVER'); 							// Peer 장비의 SLB 복구에 실패했습니다.
						VAR_HISTORY_PEERINFOEXTRACT		 	= jQuery.i18n.prop('VAR_HISTORY_PEERINFOEXTRACT');	 						// Peer 장비 정보 추출에 실패했습니다.
						VAR_HISTORY_SLBRECOVER 				= jQuery.i18n.prop('VAR_HISTORY_SLBRECOVER'); 								// SLB 복구에 실패했습니다.
						
						// AdcSetting				
						VAR_ADCSETTING_ADCDELSEL			= jQuery.i18n.prop('VAR_ADCSETTING_ADCDELSEL');								// 삭제를 원하시는 ADC를 선택하십시오.
						VAR_ADCSETTING_ADCDEL				= jQuery.i18n.prop('VAR_ADCSETTING_ADCDEL');								// ADC를 삭제 하시겠습니까?
						VAR_ADCSETTING_ADCINFOSAVEFAIL		= jQuery.i18n.prop('VAR_ADCSETTING_ADCINFOSAVEFAIL');						// ADC 정보 저장에 실패했습니다.
						VAR_ADCSETTING_ADCINFODDELFAIL		= jQuery.i18n.prop('VAR_ADCSETTING_ADCINFODDELFAIL');						// ADC 정보 삭제에 실패했습니다.
						VAR_ADCSETTING_ADDMODIFYFAIL		= jQuery.i18n.prop('VAR_ADCSETTING_ADDMODIFYFAIL');							// ADC 정보 추가/변경에 실패했습니다.
						VAR_ADCSETTING_ADCKINDNOTCLEAR		= jQuery.i18n.prop('VAR_ADCSETTING_ADCKINDNOTCLEAR');						// ADC 종류가 명확하지 않습니다.
						VAR_ADCSETTING_ADCNAMEINPUT			= jQuery.i18n.prop('VAR_ADCSETTING_ADCNAMEINPUT');							// ADC 이름을 입력하세요.
						VAR_ADCSETTING_PORTFORMAT			= jQuery.i18n.prop('VAR_ADCSETTING_PORTFORMAT');							// 입력하신 통신포트는 형식에 맞지 않습니다.
						VAR_ADCSETTING_PORTRANGE			= jQuery.i18n.prop('VAR_ADCSETTING_PORTRANGE');								// 입력하신 통신포트는 범위에 맞지 않습니다. \n 가능한 포트 범위(0~65535)
						VAR_ADCSETTING_ADCGROUPSEL			= jQuery.i18n.prop('VAR_ADCSETTING_ADCGROUPSEL');							// ADC 그룹을 선택하세요.
						VAR_ADCSETTING_CONNTESTNOT			= jQuery.i18n.prop('VAR_ADCSETTING_CONNTESTNOT');							// 연결 테스트를 수행하지 않았습니다. \n 등록을 원하시면 확인을 눌러 주십시오.       
						VAR_ADCSETTING_CONNTESTSUCCESS		= jQuery.i18n.prop('VAR_ADCSETTING_CONNTESTSUCCESS');						// 연결 테스트 성공
						VAR_ADCSETTING_NETWORKERROR			= jQuery.i18n.prop('VAR_ADCSETTING_NETWORKERROR');							// 연결 테스트 실패 (네트워크 오류)
						VAR_ADCSETTING_LOGINFAIL			= jQuery.i18n.prop('VAR_ADCSETTING_LOGINFAIL');								// 연결 테스트 실패 (로그인 실패)
						VAR_ADCSETTING_CONNTESTFAIL			= jQuery.i18n.prop('VAR_ADCSETTING_CONNTESTFAIL');							// 연결 테스트를 실패하였습니다.
						VAR_ADCSETTING_SNMPERROR			= jQuery.i18n.prop('VAR_ADCSETTING_SNMPERROR');								// 연결 테스트 실패 (SNMP Community 오류)
						VAR_ADCSETTING_NOTSUPPORT			= jQuery.i18n.prop('VAR_ADCSETTING_NOTSUPPORT');							// 연결 테스트 실패 (지원하지 않는 버전)
						VAR_ADCSETTING_CLILOGINFAIL			= jQuery.i18n.prop('VAR_ADCSETTING_CLILOGINFAIL');							// 연결 테스트 실패 (CLI 로그인 실패)
						VAR_ADCSETTING_LOGINIDINPUT			= jQuery.i18n.prop('VAR_ADCSETTING_LOGINIDINPUT');							// ADC 로그인 아이디를 입력하세요.
						VAR_ADCSETTING_PWINPUT				= jQuery.i18n.prop('VAR_ADCSETTING_PWINPUT');								// ADC 로그인 패스워드를 입력하세요.
						VAR_ADCSETTING_CLILOGINIDINPUT 		= jQuery.i18n.prop('VAR_ADCSETTING_CLILOGINIDINPUT');						// CLI 로그인 아이디를 입력하세요.      
						VAR_ADCSETTING_CLIPWINPUT 			= jQuery.i18n.prop('VAR_ADCSETTING_CLIPWINPUT');							// CLI 로그인 비밀번호를 입력하세요.     
						VAR_ADCSETTING_SNMPINPUT 			= jQuery.i18n.prop('VAR_ADCSETTING_SNMPINPUT');		 						// SNMP를 입력하세요.             
						VAR_ADCSETTING_SYSLOGIPINPUT		= jQuery.i18n.prop('VAR_ADCSETTING_SYSLOGIPINPUT');                      	// SYSLOGIP를 입력하세요.
						VAR_ADCSETTING_ADCGROUPDEL			= jQuery.i18n.prop('VAR_ADCSETTING_ADCGROUPDEL');							// ADC 그룹을 삭제 하시겠습니까?
						VAR_ADCSETTING_GROUPNAMEINPUT		= jQuery.i18n.prop('VAR_ADCSETTING_GROUPNAMEINPUT');						// 그룹명을 입력하세요.
						VAR_ADCSETTING_ADCGROUPDETSEL		= jQuery.i18n.prop('VAR_ADCSETTING_ADCGROUPDETSEL');						// 삭제를 원하시는 ADC 그룹을 선택하십시오.
						VAR_ADCSETTING_ADCGROUPDELFAIL		= jQuery.i18n.prop('VAR_ADCSETTING_ADCGROUPDELFAIL');						// ADC 그룹 삭제에 실패했습니다.
						VAR_ADCSETTING_ADCGROUPUSED			= jQuery.i18n.prop('VAR_ADCSETTING_ADCGROUPUSED');							// ADC가 등록되어 있는 그룹은 삭제할 수 없습니다.
						VAR_ADCSETTING_ADCINFOEXTRACT		= jQuery.i18n.prop('VAR_ADCSETTING_ADCINFOEXTRACT');						// ADC 정보 추출에 실패했습니다.
						VAR_ADCSETTING_FLBGROUPINFOEXTRACT	= jQuery.i18n.prop('VAR_ADCSETTING_FLBGROUPINFOEXTRACT');					// FLB 그룹 정보 추출에 실패하였습니다.
						VAR_ADCSETTING_FLBINFOSAVEFAIL		= jQuery.i18n.prop('VAR_ADCSETTING_FLBINFOSAVEFAIL');						// FLB 그룹 정보 저장에 실패했습니다.
						VAR_ADCSETTING_INVALIDLICENSE		= jQuery.i18n.prop('VAR_ADCSETTING_INVALIDLICENSE');						// ADC 정보를 추가할 수 없습니다. 라이선스를 확인해주십시오.
						VAR_FLBFILTERINFO_FILTERINFOEXTRACT		= jQuery.i18n.prop('VAR_FLBFILTERINFO_FILTERINFOEXTRACT');				// Filter 정보 추출에 실패했습니다.
						
						// ConfigCheck
						VAR_CONFIGCHECK_SNMPSTATUSACTIVITY	= jQuery.i18n.prop('VAR_CONFIGCHECK_SNMPSTATUSACTIVITY');					// 의 SNMP 상태를 활성화 합니다.
						VAR_CONFIGCHECK_RCOMMUNITYSYNC		= jQuery.i18n.prop('VAR_CONFIGCHECK_RCOMMUNITYSYNC');						// 의 SNMP Read Community String 을 동기화 합니다.
						VAR_CONFIGCHECK_SYSLOGACTIVITY		= jQuery.i18n.prop('VAR_CONFIGCHECK_SYSLOGACTIVITY');						// 의 SYSLOG 상태를 활성화 합니다.
						VAR_CONFIGCHECK_SYSLOGHOSTREGI		= jQuery.i18n.prop('VAR_CONFIGCHECK_SYSLOGHOSTREGI');						// 의 SYSLOG Host에 등록합니다.
						VAR_CONFIGCHECK_SNMPALLOWLISTREGI	= jQuery.i18n.prop('VAR_CONFIGCHECK_SNMPALLOWLISTREGI');					// 의 SNMP Allow List에 등록합니다.
						VAR_CONFIGCHECK_VSTATSTATUSACTIVITY	= jQuery.i18n.prop('VAR_CONFIGCHECK_VSTATSTATUSACTIVITY');					// 의 VSTAT 상태를 활성화 합니다.
						VAR_CONFIGCHECK_ADCSETLOAD			= jQuery.i18n.prop('VAR_CONFIGCHECK_ADCSETLOAD');							// ADC 설정 데이터 로딩에 실패했습니다.
						VAR_CONFIGCHECK_ADCSETFAIL			= jQuery.i18n.prop('VAR_CONFIGCHECK_ADCSETFAIL');							// ADC 설정에 실패했습니다.
						
						// adc로그, audit로그 logAnalysis
						VAR_LOGANALYSIS_EXTRACT				= jQuery.i18n.prop('VAR_LOGANALYSIS_EXTRACT'); 								// ADC 로그 추출에 실패했습니다.
						VAR_SYSTEMLOGANALYSIS_EXTRACT		= jQuery.i18n.prop('VAR_SYSTEMLOGANALYSIS_EXTRACT');						// 감사 로그 추출에 실패했습니다.
						
						// 보고서 - report
						VAR_REPORT_COUNTEXTRACT				= jQuery.i18n.prop('VAR_REPORT_COUNTEXTRACT');								// 보고서 개수 추출에 실패했습니다.
						VAR_REPORT_LOAD						= jQuery.i18n.prop('VAR_REPORT_LOAD');										// 보고서 데이터 로딩에 실패했습니다.
						VAR_REPORT_DELSEL					= jQuery.i18n.prop('VAR_REPORT_DELSEL');									// 삭제를 원하시는 보고서를 선택하십시오.
						VAR_REPORT_DEL						= jQuery.i18n.prop('VAR_REPORT_DEL');										// 보고서를 삭제 하시겠습니까?
						VAR_REPORT_VALIDATE					= jQuery.i18n.prop('VAR_REPORT_VALIDATE');									// 보고서 유효성 검사에 실패했습니다.
						VAR_REPORT_DELFAIL					= jQuery.i18n.prop('VAR_REPORT_DELFAIL');									// 보고서 삭제에 실패했습니다.
						VAR_REPORT_ADDFAIL					= jQuery.i18n.prop('VAR_REPORT_ADDFAIL');									// 보고서 추가에 실패했습니다.					
						VAR_REPORT_ADDMODIDFY 				= jQuery.i18n.prop('VAR_REPORT_ADDMODIDFY'); 								// 보고서 추가/변경에 실패했습니다.
						VAR_REPORT_SYSTEMOPER				= jQuery.i18n.prop('VAR_REPORT_SYSTEMOPER');								// 시스템운영보고서_
						VAR_REPORT_FAILANALYSIS				= jQuery.i18n.prop('VAR_REPORT_FAILANALYSIS');								// 장애분석보고서_
						VAR_REPORT_L4OPER					= jQuery.i18n.prop('VAR_REPORT_L4OPER');									// L4운영보고서_
						VAR_REPORT_L4OPERDAY				= jQuery.i18n.prop('VAR_REPORT_L4OPERDAY');									// L4운영보고서(일간)_
						VAR_REPORT_L4OPERWEEK				= jQuery.i18n.prop('VAR_REPORT_L4OPERWEEK');								// L4운영보고서(주간)_
						VAR_REPORT_L4OPERMONTH				= jQuery.i18n.prop('VAR_REPORT_L4OPERMONTH');								// L4운영보고서(월간)_					
						VAR_REPORT_NAMEINPUT				= jQuery.i18n.prop('VAR_REPORT_NAMEINPUT');									// 보고서 이름을 입력하세요.					
						VAR_REPORT_ATLEASTONE				= jQuery.i18n.prop('VAR_REPORT_ATLEASTONE');								// 최소한 1개 이상의 ADC 이름을 추가해 주십시오.
						VAR_REPORT_ADCNOTEXCLUDE			= jQuery.i18n.prop('VAR_REPORT_ADCNOTEXCLUDE');								// 해당 ADC는 제외 할 수 없습니다.
						VAR_REPORT_RESULTNOTSELECT			= jQuery.i18n.prop('VAR_REPORT_RESULTNOTSELECT');							// 진단결과가 선택되지 않았습니다.
						
						// 시스템관리 (비밀번호Pop) - pwchangewnd
						VAR_PW_CHANGESUCCESS				= jQuery.i18n.prop('VAR_PW_CHANGESUCCESS');									// 비밀번호 변경이 완료 되었습니다.
						VAR_PW_CHANGEFAIL					= jQuery.i18n.prop('VAR_PW_CHANGEFAIL');									// 비밀번호 변경에 실패했습니다.
						VAR_PW_INPUTWRONG					= jQuery.i18n.prop('VAR_PW_INPUTWRONG');									// 입력한 비밀번호가 틀립니다.
						VAR_PW_RULWRONG						= jQuery.i18n.prop('VAR_PW_RULWRONG');										// 비밀번호 규칙이 맞지 않습니다. \n 비밀번호는 알파벳과 숫자 조합의 5~16자 사이로 구성됩니다.
											
						// 패킷분석 faultAnalysis					
						VAR_PKT_HISTORYDEL					= jQuery.i18n.prop('VAR_PKT_HISTORYDEL');									// 패킷 수집 이력 삭제에 실패하였습니다.
						VAR_PKT_LISTDELSEL					= jQuery.i18n.prop('VAR_PKT_LISTDELSEL');									// 삭제를 원하시는 패킷 목록을 선택하십시오.
						VAR_PKT_DEL							= jQuery.i18n.prop('VAR_PKT_DEL');											// 패킷을 삭제 하시겠습니까?
						VAR_PKT_DOWNLOADFILEEXSITINPECT		= jQuery.i18n.prop('VAR_PKT_DOWNLOADFILEEXSITINPECT');						// 다운로드 파일 존재 유무 검사에 실패하였습니다.

						// faultAnlaysis - Popup
						VAR_PKTPOP_INPROGRESS				= jQuery.i18n.prop('VAR_PKTPOP_INPROGRESS');								// 패킷 수집 진행중이기 때문에 페이지를 닫을 수 없습니다.
						VAR_PKTPOP_SRCIPALREADYSEL			= jQuery.i18n.prop('VAR_PKTPOP_SRCIPALREADYSEL');							// Source IP는 이미 선택 되었습니다. 다른 키워드를 선택하세요.
						VAR_PKTPOP_DSTIPALREAYSEL	 		= jQuery.i18n.prop('VAR_PKTPOP_DSTIPALREAYSEL');							// Destination IP는 이미 선택 되었습니다. 다른 키워드를 선택하세요.
						VAR_PKTPOP_SRCPORTALREADYSEL 		= jQuery.i18n.prop('VAR_PKTPOP_SRCPORTALREADYSEL');							// Source Port는 이미 선택 되었습니다. 다른 키워드를 선택하세요.
						VAR_PKTPOP_PORTALREADYSEL 		    = jQuery.i18n.prop('VAR_PKTPOP_PORTALREADYSEL');    						// Port는 이미 선택 되었습니다. 다른 키워드를 선택하세요.
						VAR_PKTPOP_DSTPORTALREADYSEL 		= jQuery.i18n.prop('VAR_PKTPOP_DSTPORTALREADYSEL');							// Destination Port는 이미 선택 되었습니다. 다른 키워드를 선택하세요.
						VAR_PKTPOP_PROTOCOLALREADYSEL		= jQuery.i18n.prop('VAR_PKTPOP_PROTOCOLALREADYSEL');						// Protocol은 이미 선택 되었습니다. 다른 키워드를 선택하세요.
						VAR_PKTPOP_HOSTALREADYSEL	 		= jQuery.i18n.prop('VAR_PKTPOP_HOSTALREADYSEL');							// Host는 이미 선택 되었습니다. 다른 키워드를 선택하세요.
						VAR_PKTPOP_COUNTLIMITOPTION 		= jQuery.i18n.prop('VAR_PKTPOP_COUNTLIMITOPTION');							// 패킷 개수로 제한옵션은 필수 조건입니다. 체크를 해제할 수 없습니다.
						VAR_PKTPOP_STARTFAIL		 		= jQuery.i18n.prop('VAR_PKTPOP_STARTFAIL');									// 패킷 수집 시작에 실패하였습니다.					
						VAR_PKTPOP_STOPCANCEL 				= jQuery.i18n.prop('VAR_PKTPOP_STOPCANCEL');								// 패킷 수집 정지/취소에 실패하였습니다.
						VAR_PKTPOP_STOPFAIL 				= jQuery.i18n.prop('VAR_PKTPOP_STOPFAIL');									// 패킷 수집 정지에 실패하였습니다.					
						VAR_PKTPOP_DEL 						= jQuery.i18n.prop('VAR_PKTPOP_DEL');										// 삭제
						VAR_PKTPOP_EXISTSAMESEARCHCOND 		= jQuery.i18n.prop('VAR_PKTPOP_EXISTSAMESEARCHCOND');						// 동일한 검색조건이 존재합니다.
						VAR_PKTPOP_COUNTINPUT 				= jQuery.i18n.prop('VAR_PKTPOP_COUNTINPUT');								// 패킷 개수를 입력하세요.
						VAR_PKTPOP_ONLYNUMINPUT 			= jQuery.i18n.prop('VAR_PKTPOP_ONLYNUMINPUT');								// 숫자만 입력가능합니다.
						VAR_PKTPOP_MAXNOTMORE_ALTEON 		= jQuery.i18n.prop('VAR_PKTPOP_MAXNOTMORE_ALTEON');							// 패킷 개수는 최대  10,000개를 넘을 수 없습니다.
						VAR_PKTPOP_MAXNOTMORE_F5			= jQuery.i18n.prop('VAR_PKTPOP_MAXNOTMORE_F5');								// 패킷 개수는 최대  500,000개를 넘을 수 없습니다.
						VAR_PKTPOP_GREATERTHANZERO 			= jQuery.i18n.prop('VAR_PKTPOP_GREATERTHANZERO');							// 패킷 개수는 0보다는 커야합니다.
						VAR_PKTPOP_TIMEINPUT 				= jQuery.i18n.prop('VAR_PKTPOP_TIMEINPUT');									// 시간을 입력하세요.
						VAR_PKTPOP_TIMENOTEXCEEDMAX 		= jQuery.i18n.prop('VAR_PKTPOP_TIMENOTEXCEEDMAX');							// 시간은 최대  600초를 넘을 수 없습니다.
						VAR_PKTPOP_CAPACITYINPUT 			= jQuery.i18n.prop('VAR_PKTPOP_CAPACITYINPUT');								// 용량을 입력하세요.
						VAR_PKTPOP_CAOACITYNOTEXCEEDMAX 	= jQuery.i18n.prop('VAR_PKTPOP_CAOACITYNOTEXCEEDMAX');						// 용량은 최대  10000Kbytes를 넘을 수 없습니다.
						VAR_PKTPOP_CAPACITYGREATERTHANZERO 	= jQuery.i18n.prop('VAR_PKTPOP_CAPACITYGREATERTHANZERO');					// 용량은 0보다는 커야합니다.
						VAR_PKTPOP_FILENAMEMUSTBEINPUT 		= jQuery.i18n.prop('VAR_PKTPOP_FILENAMEMUSTBEINPUT');						// 파일명은 반드시 입력하여야 합니다.
						VAR_PKTPOP_SRCIPFORMAT 				= jQuery.i18n.prop('VAR_PKTPOP_SRCIPFORMAT');								// 입력하신 Souce_IP의 형식이 올바르지 않습니다.
						VAR_PKTPOP_DSTIPFORMAT 				= jQuery.i18n.prop('VAR_PKTPOP_DSTIPFORMAT');								// 입력하신 Destination_IP의 형식이 올바르지 않습니다.
						VAR_PKTPOP_SRCPORTFORMAT 			= jQuery.i18n.prop('VAR_PKTPOP_SRCPORTFORMAT');								// 입력하신 Source_Port의 형식이 올바르지 않습니다.
						VAR_PKTPOP_DSTPORTFORMAT 			= jQuery.i18n.prop('VAR_PKTPOP_DSTPORTFORMAT');								// 입력하신 Destination_Port의 형식이 올바르지 않습니다.
						VAR_PKTPOP_HOSTIPFORMAT 			= jQuery.i18n.prop('VAR_PKTPOP_HOSTIPFORMAT');								// 입력하신 Host_IP 형식이 올바르지 않습니다.
						VAR_PKTPOP_PORTFORMAT 				= jQuery.i18n.prop('VAR_PKTPOP_PORTFORMAT');								// 입력하신 Port의 형식이 올바르지 않습니다.
						VAR_PKTPOP_ADD 						= jQuery.i18n.prop('VAR_PKTPOP_ADD');										// 추가
						VAR_PKTPOP_SRCIP					= jQuery.i18n.prop('VAR_PKTPOP_SRCIP');										// Source IP
						VAR_PKTPOP_DSTIP					= jQuery.i18n.prop('VAR_PKTPOP_DSTIP');										// Destination IP
						VAR_PKTPOP_SRCPORT					= jQuery.i18n.prop('VAR_PKTPOP_SRCPORT');									// Source Port
						VAR_PKTPOP_DSTPORT					= jQuery.i18n.prop('VAR_PKTPOP_DSTPORT');									// Destination Port
						VAR_PKTPOP_PROTOCOL					= jQuery.i18n.prop('VAR_PKTPOP_PROTOCOL');									// Protocol
						VAR_PKTPOP_HOST						= jQuery.i18n.prop('VAR_PKTPOP_HOST');										// Host
						VAR_PKTPOP_ONEMORE					= jQuery.i18n.prop('VAR_PKTPOP_ONEMORE');									// 최소한 1개 이상의 옵션을 추가해 주십시오.
						
						// 진단이력 faultHistory
						VAR_FAULTHISTORY_COUNTEXTRACT		= jQuery.i18n.prop('VAR_FAULTHISTORY_COUNTEXTRACT');						// 장애 진단 이력 개수 추출에 실패했습니다.
						VAR_FAULTHISTORY_LOAD				= jQuery.i18n.prop('VAR_FAULTHISTORY_LOAD');								// 장애 진단 이력 데이터 로딩에 실패했습니다.
						VAR_FAULTHISTORY_SCHEDULELOAD		= jQuery.i18n.prop('VAR_FAULTHISTORY_SCHEDULELOAD');						// 장애 진단 예약 목록 로딩에 실패했습니다.
						VAR_FAULTHISTORY_SCHEDULEDEL		= jQuery.i18n.prop('VAR_FAULTHISTORY_SCHEDULEDEL');							// 장애 진단 예약 삭제에 실패했습니다.
						VAR_FAULTHISTORY_DELSEL				= jQuery.i18n.prop('VAR_FAULTHISTORY_DELSEL');								// 삭제를 원하시는 장애 진단 이력을 선택하십시오.
						VAR_FAULTHISTORY_DEL				= jQuery.i18n.prop('VAR_FAULTHISTORY_DEL');									// 장애 진단 이력을 삭제 하시겠습니까?
						VAR_FAULTHISTORY_DELFAIL			= jQuery.i18n.prop('VAR_FAULTHISTORY_DELFAIL');								// 장애 진단 이력 삭제에 실패했습니다.
					
						// 진단결과 faultResult
						VAR_FAULTRESULT_LOAD				= jQuery.i18n.prop('VAR_FAULTRESULT_LOAD');									// 장애 진단 이력 로딩에 실패했습니다.
						VAR_FAULTRESULT_PKTLOSSLOAD			= jQuery.i18n.prop('VAR_FAULTRESULT_PKTLOSSLOAD');							// 패킷 손실 정보 로딩에 실패했습니다.
						VAR_FAULTRESULT_INPROGRESSPRINT		= jQuery.i18n.prop('VAR_FAULTRESULT_INPROGRESSPRINT');						// 진행 상태 출력에 실패했습니다.
						VAR_FAULTRESULT_PKTDUMPFILEEXIST	= jQuery.i18n.prop('VAR_FAULTRESULT_PKTDUMPFILEEXIST');						// 패킷 덤프 파일 존재 유무 검사에 실패했습니다
						VAR_FAULTRESULT_EXISTINPECT			= jQuery.i18n.prop('VAR_FAULTRESULT_EXISTINPECT');							// 장애 결과 데이터 존재 유무 검사에 실패했습니다.
						
						// 진단 팝업 faultDiagnosis
						VAR_FAULTDIAGNOSIS_STATUSLOAD 		= jQuery.i18n.prop('VAR_FAULTDIAGNOSIS_STATUSLOAD');						// 장애 진단 상태 로딩에 실패했습니다.
						VAR_FAULTDIAGNOSIS_INFOLOADING		= jQuery.i18n.prop('VAR_FAULTDIAGNOSIS_INFOLOADING');						// 해당 진단 정보를 불러오는 중입니다.
						VAR_FAULTDIAGNOSIS_CANCEL			= jQuery.i18n.prop('VAR_FAULTDIAGNOSIS_CANCEL');							// 진단취소
						VAR_FAULTDIAGNOSIS_PAGENOTCLOSE		= jQuery.i18n.prop('VAR_FAULTDIAGNOSIS_PAGENOTCLOSE');						// 진단 진행중이기 때문에 페이지를 닫을 수 없습니다.
						VAR_FAULTDIAGNOSIS_DONE				= jQuery.i18n.prop('VAR_FAULTDIAGNOSIS_DONE');								// 진단을 마쳤습니다.
						VAR_FAULTDIAGNOSIS_TIME				= jQuery.i18n.prop('VAR_FAULTDIAGNOSIS_TIME');								// (진단시간 :
						VAR_FAULTDIAGNOSIS_FAIL				= jQuery.i18n.prop('VAR_FAULTDIAGNOSIS_FAIL');								// 진단에 실패하였습니다.
						VAR_FAULTDIAGNOSIS_ALREADYCANCEL	= jQuery.i18n.prop('VAR_FAULTDIAGNOSIS_ALREADYCANCEL');						// 이미 취소된 진단입니다.
						VAR_FAULTDIAGNOSIS_CANCELFAIL		= jQuery.i18n.prop('VAR_FAULTDIAGNOSIS_CANCELFAIL');						// 장애 진단 취소에 실패했습니다.
						VAR_FAULTDIAGNOSIS_CANCELSUCCESS	= jQuery.i18n.prop('VAR_FAULTDIAGNOSIS_CANCELSUCCESS');						// 진단을 취소하였습니다.
						VAR_FAULTDIAGNOSIS_GROUPSUPPORT		= jQuery.i18n.prop('VAR_FAULTDIAGNOSIS_GROUPSUPPORT');						// 그룹진단은 같은 벤더일때 지원합니다.
						VAR_FAULTDIAGNOSIS_MODECHECK 		= jQuery.i18n.prop('VAR_FAULTDIAGNOSIS_MODECHECK');							// 진단은 ADC 모드가 진단모드일때만 지원합니다.
						VAR_FAULTDIAGNOSIS_GROUPMODECHECK 	= jQuery.i18n.prop('VAR_FAULTDIAGNOSIS_GROUPMODECHECK');							// 그룹에 진단모드가 아닌 ADC가 포함되어 있습니다. 
						
						// 진단 faultSetting
						VAR_FAULTSETTING_LOAD 				= jQuery.i18n.prop('VAR_FAULTSETTING_LOAD');								// 장애 진단 페이지 로딩에 실패했습니다.
						VAR_FAULTSETTING_SCHEDULESUCC		= jQuery.i18n.prop('VAR_FAULTSETTING_SCHEDULESUCC');						// 진단 예약 설정에 성공하였습니다.
						VAR_FAULTSETTING_TEMPLATESAVESUCC 	= jQuery.i18n.prop('VAR_FAULTSETTING_TEMPLATESAVESUCC');					// 정책 템플릿 저장에 성공하였습니다.
						VAR_FAULTSETTING_TEMPLAGEMODIFYSUCC = jQuery.i18n.prop('VAR_FAULTSETTING_TEMPLAGEMODIFYSUCC');					// 정책 템플릿 수정에 성공하였습니다.
						VAR_FAULTSETTING_REGISCHEDULE	 	= jQuery.i18n.prop('VAR_FAULTSETTING_REGISCHEDULE');						// 이미 등록된 장애검사 예약입니다.
						VAR_FAULTSETTING_FAIL 				= jQuery.i18n.prop('VAR_FAULTSETTING_FAIL');								// 장애 진단에 실패했습니다.
						VAR_FAULTSETTING_TEMPLATEDELFAIL 	= jQuery.i18n.prop('VAR_FAULTSETTING_TEMPLATEDELFAIL');						// 장애 진단 템플릿 삭제에 실패했습니다.
						VAR_FAULTSETTING_SVCCLIENIPINPUT 	= jQuery.i18n.prop('VAR_FAULTSETTING_SVCCLIENIPINPUT');						// 서비스 진단 사용자 IP를 입력하십시오.
						VAR_FAULTSETTING_ITEMNOTSEL 		= jQuery.i18n.prop('VAR_FAULTSETTING_ITEMNOTSEL');							// 진단 항목을 선택하지 않았습니다.
						VAR_FAULTSETTING_SCHEDULEITEMNOTSEL = jQuery.i18n.prop('VAR_FAULTSETTING_SCHEDULEITEMNOTSEL');					// 예약 항목을 선택하지 않았습니다.
						VAR_FAULTSETTING_TEMPLATEITEMNOTSEL = jQuery.i18n.prop('VAR_FAULTSETTING_TEMPLATEITEMNOTSEL');					// 템플릿 저장 항목을 선택하지 않았습니다.
						VAR_FAULTSETTING_ADCLOGCOUNT 		= jQuery.i18n.prop('VAR_FAULTSETTING_ADCLOGCOUNT');							// ADC 로그 개수가 비정상적입니다. 1~100 사이의 정수로 입력하십시오.				
						VAR_FAULTSETTING_CLIENTIPINCCORECT	= jQuery.i18n.prop('VAR_FAULTSETTING_CLIENTIPINCCORECT');					// 입력하신 사용자 IP의 형식이 올바르지 않습니다.
						VAR_FAULTSETTING_TEMPLATENAMEINPUT 	= jQuery.i18n.prop('VAR_FAULTSETTING_TEMPLATENAMEINPUT');					// 템플릿 이름을 입력(지정) 하십시오.
						VAR_FAULTSETTING_ALREADYSAVENAME	= jQuery.i18n.prop('VAR_FAULTSETTING_ALREADYSAVENAME');						// 같은 템플릿 이름이 이미 저장되어 있습니다.
						VAR_FAULTSETTING_TMEPLATENAME 		= jQuery.i18n.prop('VAR_FAULTSETTING_TMEPLATENAME');						// 템플릿 이름이 비정상적입니다. 1~64자 사이의 문자열로 입력하십시오.					
						VAR_FAULTSETTING_SPECIALCHAR	 	= jQuery.i18n.prop('VAR_FAULTSETTING_SPECIALCHAR');							// 특수문자는 입력하실수 없습니다.
						VAR_FAULTSETTING_TEMPLATEDEL 		= jQuery.i18n.prop('VAR_FAULTSETTING_TEMPLATEDEL');							// 정책 템플릿를 삭제 하시겠습니까?
						VAR_FAULTSETTING_TEMPLATEMODIADD	= jQuery.i18n.prop('VAR_FAULTSETTING_TEMPLATEMODIADD');						// 진단 템플릿이 변경되었습니다.\n 저장 후 진단하시겠습니까?
								
						// 모니터링(경보) - AlertMonitoring
						VAR_FAULT_LOGCOUNTEXTRACT			= jQuery.i18n.prop('VAR_FAULT_LOGCOUNTEXTRACT');							// 장애 로그 개수 추출에 실패했습니다.
						VAR_FAULT_LOGDATALOAD				= jQuery.i18n.prop('VAR_FAULT_LOGDATALOAD');								// 장애 로그 데이터 로딩에 실패했습니다.
						VAR_FAULT_WARNING					= jQuery.i18n.prop('VAR_FAULT_WARNING');									// 경고 발생
						VAR_FAULT_FAULT						= jQuery.i18n.prop('VAR_FAULT_FAULT');										// 장애
						VAR_FAULT_FAULT_RESERVE				= jQuery.i18n.prop('VAR_FAULT_FAULT_RESERVE');								// 장애해결
						VAR_FAULT_FAULT_OCCUR				= jQuery.i18n.prop('VAR_FAULT_FAULT_OCCUR');								// 장애발생
						
						//모니터링(장비) - appliance
						VAR_APPLIANCE_LAOD 					= jQuery.i18n.prop('VAR_APPLIANCE_LAOD');									// ADC 장비 데이터 로딩에 실패했습니다.
						VAR_APPLIANCE_PERAVERAGE 			= jQuery.i18n.prop('VAR_APPLIANCE_PERAVERAGE'); 							// 전일평균
						VAR_APPLIANCE_SESSIONUSAGELOAD 		= jQuery.i18n.prop('VAR_APPLIANCE_SESSIONUSAGELOAD'); 						// 세션 사용량 추이 데이터 로딩에 실패했습니다.
						VAR_APPLIANCE_TRAFFICUSAGELOAD	 	= jQuery.i18n.prop('VAR_APPLIANCE_TRAFFICUSAGELOAD');						// 트래픽 사용량 추이 데이터 로딩에 실패했습니다.
						VAR_APPLIANCE_CPUUSAGELOAD 			= jQuery.i18n.prop('VAR_APPLIANCE_CPUUSAGELOAD'); 							// CPU 사용량 추이 데이터 로딩에 실패했습니다.
						VAR_APPLIANCE_MEMUSAGELOAD 			= jQuery.i18n.prop('VAR_APPLIANCE_MEMUSAGELOAD'); 							// 메모리 사용량 추이 데이터 로딩에 실패했습니다.
						VAR_APPLIANCE_PKTERRGENLOAD 		= jQuery.i18n.prop('VAR_APPLIANCE_PKTERRGENLOAD'); 							// 패킷 오류 발생량 추이 데이터 로딩에 실패했습니다.
						VAR_APPLIANCE_LOSSLOAD 				= jQuery.i18n.prop('VAR_APPLIANCE_LOSSLOAD'); 								// 손실 패킷량 추이 데이터 로딩에 실패했습니다.
						VAR_APPLIANCE_HTTPREQLOAD 			= jQuery.i18n.prop('VAR_APPLIANCE_HTTPREQLOAD');							// Http 요청량 추이 데이터 로딩에 실패했습니다.
						VAR_APPLIANCE_MAX 					= jQuery.i18n.prop('VAR_APPLIANCE_MAX');									// 최대값
						VAR_APPLIANCE_SPMAX 				= jQuery.i18n.prop('VAR_APPLIANCE_SPMAX');									// 임계치
						VAR_APPLIANCE_SPAVG 				= jQuery.i18n.prop('VAR_APPLIANCE_SPAVG');									// 평균
						
						//모니터링(L2) - L2
						VAR_L2_SEARCH 						= jQuery.i18n.prop('VAR_L2_SEARCH'); 										// L2 검색에 실패했습니다.
						VAR_L2_LOAD 						= jQuery.i18n.prop('VAR_L2_LOAD'); 											// L2 검색 데이터 로딩에 실패했습니다.
						
						// 모니터링(성능) - servicePerfomance
						VAR_SVCPERFOM_LAOD 					= jQuery.i18n.prop('VAR_SVCPERFOM_LAOD');									// 서비스 성능 데이터 로딩에 실패했습니다.
						VAR_SVCPERFOM_REALRESPONSENOTSUPPORT= jQuery.i18n.prop('VAR_SVCPERFOM_REALRESPONSENOTSUPPORT'); 				// 실시간 조회에서는 응답시간 조회를 지원하지 않습니다.
						VAR_SVCPERFOM_EXPEXISTINSPECT 		= jQuery.i18n.prop('VAR_SVCPERFOM_EXPEXISTINSPECT');						// 서비스 성능 데이터 내보내기 데이터 존재 유무 검사에 실패했습니다
						VAR_SVCPERFOM_MEMBERLOAD			= jQuery.i18n.prop('VAR_SVCPERFOM_MEMBERLOAD');								// Member 데이터 로딩에 실패했습니다.
						VAR_SVCMONITOR_RESPTIME				= jQuery.i18n.prop('VAR_SVCMONITOR_RESPTIME');								// 응답시간
	                         
						VAR_SVCPERFOM_ALLGRP_AVAILABLE_COUNTCHK = jQuery.i18n.prop('VAR_SVCPERFOM_ALLGRP_AVAILABLE_COUNTCHK');			// 선택가능한 서비스 개수는 5개입니다.
						// Group/realserver
						VAR_REALSERVER_LAODFAIL				= jQuery.i18n.prop('VAR_REALSERVER_LAODFAIL');								// RealServer 모니터링 데이터 로딩에 실패했습니다.
						VAR_GROUP_LAODFAIL 					= jQuery.i18n.prop('VAR_GROUP_LAODFAIL');									// GROUP 모니터링 데이터 로딩에 실패했습니다.
						VAR_GROUP_NAME 						= jQuery.i18n.prop('VAR_GROUP_NAME');										// GROUP 이름

						// 모니터링(VS요약) - networkMap
						VAR_NETWORK_VSSUMMARYLOAD 			= jQuery.i18n.prop('VAR_NETWORK_VSSUMMARYLOAD');							// VS 요약 데이터 로딩에 실패했습니다.	
						
						// 모니터링(세션) - session
						VAR_SESSIONMOR_SEDLFAIL				= jQuery.i18n.prop('VAR_SESSIONMOR_SEDLFAIL');								// 세션 데이터 로딩에 실패했습니다.                                
						VAR_SESSIONMOR_SEDCFAIL				= jQuery.i18n.prop('VAR_SESSIONMOR_SEDCFAIL');								// 세션 데이터 개수 추출에 실패했습니다.                           
						VAR_SESSIONMOR_SEDSFAIL				= jQuery.i18n.prop('VAR_SESSIONMOR_SEDSFAIL');								// 세션 데이터 검색에 실패했습니다.                                
						VAR_SESSIONMOR_SEDSOFAIL			= jQuery.i18n.prop('VAR_SESSIONMOR_SEDSOFAIL');								// 세션 데이터 정렬에 실패했습니다.                           
						VAR_SESSIONMOR_SRCASEL				= jQuery.i18n.prop('VAR_SESSIONMOR_SRCASEL');								// Client IP는 이미 선택 되었습니다. 다른 키워드를 선택하세요. 
						VAR_SESSIONMOR_VIPASEL				= jQuery.i18n.prop('VAR_SESSIONMOR_VIPASEL');								// Virtual IP는 이미 선택 되었습니다. 다른 키워드를 선택하세요.                           
						VAR_SESSIONMOR_DSTASEL				= jQuery.i18n.prop('VAR_SESSIONMOR_DSTASEL');								// Destination IP는 이미 선택 되었습니다. 다른 키워드를 선택하세요.  
						VAR_SESSIONMOR_REALASEL				= jQuery.i18n.prop('VAR_SESSIONMOR_REALASEL');								// Real IP는 이미 선택 되었습니다. 다른 키워드를 선택하세요.                           
						VAR_SESSIONMOR_SRCPALSEL			= jQuery.i18n.prop('VAR_SESSIONMOR_SRCPALSEL');								// Client Port는 이미 선택 되었습니다. 다른 키워드를 선택하세요.                             
						VAR_SESSIONMOR_DSTPASEL				= jQuery.i18n.prop('VAR_SESSIONMOR_DSTPASEL');								// Destination Port는 이미 선택 되었습니다. 다른 키워드를 선택하세요.
						VAR_SESSIONMOR_VPASEL				= jQuery.i18n.prop('VAR_SESSIONMOR_VPASEL');								// Virtual Port는 이미 선택 되었습니다. 다른 키워드를 선택하세요.						
						VAR_SESSIONMOR_REALPALSEL			= jQuery.i18n.prop('VAR_SESSIONMOR_REALPALSEL');							// Real Port는 이미 선택 되었습니다. 다른 키워드를 선택하세요.
						VAR_SESSIONMOR_PROALRSEL			= jQuery.i18n.prop('VAR_SESSIONMOR_PROALRSEL');								// Protocol은 이미 선택 되었습니다. 다른 키워드를 선택하세요.                                 
						VAR_SESSIONMOR_SRCIDOMI				= jQuery.i18n.prop('VAR_SESSIONMOR_SRCIDOMI');								// Client IP와 Virtual IP중 하나는 반드시 입력 하여야 합니다.  
						VAR_SESSIONMOR_SRCIIFORM			= jQuery.i18n.prop('VAR_SESSIONMOR_SRCIIFORM');								// 입력하신 Client IP의 형식이 올바르지 않습니다.                   
						VAR_SESSIONMOR_SRCINPUT				= jQuery.i18n.prop('VAR_SESSIONMOR_SRCINPUT');								// Client IP를 입력 하세요.                                        
						VAR_SESSIONMOR_DSTIFORM				= jQuery.i18n.prop('VAR_SESSIONMOR_DSTIFORM');								// 입력하신 Virtual IP의 형식이 올바르지 않습니다.             
						VAR_SESSIONMOR_DSTINPUT				= jQuery.i18n.prop('VAR_SESSIONMOR_DSTINPUT');								// Virtual IP를 입력 하세요.                                   
						VAR_SESSIONMOR_REALIFORM			= jQuery.i18n.prop('VAR_SESSIONMOR_REALIFORM');							// 입력하신 Virtual IP의 형식이 올바르지 않습니다.             
						VAR_SESSIONMOR_REALINPUT			= jQuery.i18n.prop('VAR_SESSIONMOR_REALINPUT');								// Virtual IP를 입력 하세요.                                   
						VAR_SESSIONMOR_SRCPIFORM			= jQuery.i18n.prop('VAR_SESSIONMOR_SRCPIFORM');								// 입력하신 Client Port의 형식이 올바르지 않습니다.                
						VAR_SESSIONMOR_DSTPIFORM			= jQuery.i18n.prop('VAR_SESSIONMOR_DSTPIFORM');								// 입력하신 Virtual Port의 형식이 올바르지 않습니다.
						VAR_SESSIONMOR_REALPIFORM			= jQuery.i18n.prop('VAR_SESSIONMOR_REALPIFORM');								// 입력하신 Real Port의 형식이 올바르지 않습니다.
						VAR_SESSIONMOR_ADD					= jQuery.i18n.prop('VAR_SESSIONMOR_ADD');									// 추가                                                            
						VAR_SESSIONMOR_DEL					= jQuery.i18n.prop('VAR_SESSIONMOR_DEL');									// 삭제   
						VAR_SESSIONMOR_CIP					= jQuery.i18n.prop('VAR_SESSIONMOR_CIP');									// Client IP
						VAR_SESSIONMOR_VIP					= jQuery.i18n.prop('VAR_SESSIONMOR_VIP');									// Virtual IP
						VAR_SESSIONMOR_REALIP				= jQuery.i18n.prop('VAR_SESSIONMOR_REALIP');								// Virtual IP
						VAR_SESSIONMOR_DIP					= jQuery.i18n.prop('VAR_SESSIONMOR_DIP');									// Destination IP
						VAR_SESSIONMOR_CPORT				= jQuery.i18n.prop('VAR_SESSIONMOR_CPORT');									// Client Port
						VAR_SESSIONMOR_VPORT				= jQuery.i18n.prop('VAR_SESSIONMOR_VPORT');									// Virtual Port
						VAR_SESSIONMOR_REALPORT				= jQuery.i18n.prop('VAR_SESSIONMOR_REALPORT');								// Real Port
						VAR_SESSIONMOR_DPORT				= jQuery.i18n.prop('VAR_SESSIONMOR_DPORT');									// Destination Port
						VAR_SESSIONMOR_PROTO				= jQuery.i18n.prop('VAR_SESSIONMOR_PROTO');									// Protocol
						VAR_SESSIONMOR_AGE					= jQuery.i18n.prop('VAR_SESSIONMOR_AGE');									// Age(분)
						// 모니터링(인터페이스) - statistics		
						VAR_STATISTICS_LOADFAIL				= jQuery.i18n.prop('VAR_STATISTICS_LOADFAIL');								// 인터페이스 정보 로딩에 실패했습니다.                           
						VAR_STATISTICS_USAGLOADFAIL			= jQuery.i18n.prop('VAR_STATISTICS_USAGLOADFAIL');							// 인터페이스별 사용량 그래프 로딩에 실패했습니다.                
						VAR_STATISTICS_AFEW					= jQuery.i18n.prop('VAR_STATISTICS_AFEW');									// 개                                                             
						VAR_STATISTICS_TRAFFICBPS			= jQuery.i18n.prop('VAR_STATISTICS_TRAFFICBPS');							// 트래픽(bps)                                                    
						VAR_STATISTICS_TRAFFICPPS			= jQuery.i18n.prop('VAR_STATISTICS_TRAFFICPPS');							// 트래픽(pps)                                                     
						VAR_STATISTICS_ERRGEN				= jQuery.i18n.prop('VAR_STATISTICS_ERRGEN');								// 오류 발생                                                      
						VAR_STATISTICS_LOSSGEN				= jQuery.i18n.prop('VAR_STATISTICS_LOSSGEN');								// 손실 발생
						VAR_STATISTICS_CHECKINSEL			= jQuery.i18n.prop('VAR_STATISTICS_CHECKINSEL');							// 확인 할 인터페이스를 선택 해 주세요.                    
						
						// virtualserver (alteon) - alteonvs
						VAR_ALT_VSDSEL						= jQuery.i18n.prop('VAR_ALT_VSDSEL'); 										// 삭제를 원하시는 Virtual Service를 선택하십시오.
						VAR_ALT_VSDEL						= jQuery.i18n.prop('VAR_ALT_VSDEL'); 										// Virtual Service를 삭제 하시겠습니까?
						VAR_ALT_IIINCOR						= jQuery.i18n.prop('VAR_ALT_IIINCOR');										// Index(ID)값이 잘못 입력되었습니다. 1~1024 사이의 정수로 입력하십시오. 
						VAR_ALT_RPRDISAG					= jQuery.i18n.prop('VAR_ALT_RPRDISAG'); 									// 입력하신 Real서버 포트는 범위에 맞지 않습니다. 가능한 포트 범위(0~65535)
						VAR_ALT_VLFAIL						= jQuery.i18n.prop('VAR_ALT_VLFAIL');										// Virtual GROUP 데이터 로딩에 실패했습니다.
						VAR_ALT_AIREAL						= jQuery.i18n.prop('VAR_ALT_AIREAL');										// 은(는) 이미 REAL SERVER 목록에 있습니다. REAL SERVER 목록에서 선택 하세요.
						VAR_ALT_SERDUP						= jQuery.i18n.prop('VAR_ALT_SERDUP');										// 중복된 서비스 포트입니다. 새로운 포트를 지정하세요. 
						VAR_ALT_REANNDEL					= jQuery.i18n.prop('VAR_ALT_REANNDEL');										// Alteon REAL SERVER Id가 없어서 삭제 됩니다.
						VAR_ALT_SER_INP						= jQuery.i18n.prop('VAR_ALT_SER_INP');										// 서비스 포트를 입력하세요.
						VAR_ALT_MEMINP						= jQuery.i18n.prop('VAR_ALT_MEMINP'); 										// 멤버를 등록하세요.
						VAR_ALT_NAMEDUPLICATE				= jQuery.i18n.prop('VAR_ALT_NAMEDUPLICATE'); 								// 이름이 중복되었습니다. 다시 입력해 주세요.
						
						// virtualserver (f5) - f5vs
						VAR_F5_SER_NINPUT					= jQuery.i18n.prop('VAR_F5_SER_NINPUT');									// 서비스포트가 입력되지 않았습니다.
						VAR_F5_GRO_NINPUT					= jQuery.i18n.prop('VAR_F5_GRO_NINPUT');									// GROUP 이름이 입력되지 않았습니다
						VAR_F5_NAPP							= jQuery.i18n.prop('VAR_F5_NAPP');											// 지정 안함
						VAR_F5_ID							= jQuery.i18n.prop('VAR_F5_ID');											// 입력하신 ID는 알파벳으로 시작하며, 문자는 .-_만 허용됩니다.
						VAR_F5_SNMP							= jQuery.i18n.prop('VAR_F5_SNMP');											// 잘못된 문자 입니다. 오직 특수문자는 해당 특수문자만 허용 합니다. 
						
						// virtualserver (pask) - paskvs
						VAR_PASK_PORTIFORMAT				= jQuery.i18n.prop('VAR_PASK_PORTIFORMAT');									// 포트형식이 올바르지 않습니다. 0~65545 사이의 정수로 입력하십시오.
						VAR_PASK_HEAEFAIL					= jQuery.i18n.prop('VAR_PASK_HEAEFAIL');									// Health Check 데이터 추출에 실패했습니다.
	
						// virtualserver (pas) - pasvs
						VAR_PAS_PORNINP						= jQuery.i18n.prop('VAR_PAS_PORNINP');										// 포트가 입력되지 않았습니다.
	
						// virtual_pop
						VAR_VPMEMBER_IPNFORMAT 				= jQuery.i18n.prop('VAR_VPMEMBER_IPNFORMAT');								// [IP주소]:[포트번호]의 형식이 아닙니다. 입력된 내용을 확인하시고 다시 입력하여 주시기 바랍니다.
						VAR_VPPERSI_PROEXTFAIL				= jQuery.i18n.prop('VAR_VPPERSI_PROEXTFAIL');								// 프로파일 데이터 추출에 실패했습니다.
						VAR_VPPORT_PORNNUM					= jQuery.i18n.prop('VAR_VPPORT_PORNNUM');									// 입력된 포트 값이 숫자가 아닙니다.
						VAR_VPVSER_SEARFAIL					= jQuery.i18n.prop('VAR_VPVSER_SEARFAIL');									// Virtual Server 검색에 실패했습니다.      
						VAR_VPVSER_LOADFAIL					= jQuery.i18n.prop('VAR_VPVSER_LOADFAIL');									// Virtual Server 데이터 로딩에 실패했습니다.
	
						// virtualserver - virtualserver
						VAR_VS_VSECEFAIL					= jQuery.i18n.prop('VAR_VS_VSECEFAIL');										// Virtual Server/Service 개수 추출에 실패했습니다.
						VAR_VS_VSELFAIL						= jQuery.i18n.prop('VAR_VS_VSELFAIL');										// Virtual Server 페이지 로딩에 실패했습니다.
						VAR_VS_ENAVSELE						= jQuery.i18n.prop('VAR_VS_ENAVSELE');										// Enable을 원하시는 Virtual Server를 선택하십시오.
						VAR_VS_ENAFAIL						= jQuery.i18n.prop('VAR_VS_ENAFAIL');										// Virtual Server Enable에 실패했습니다. 
						VAR_VS_PEEVEFAIL					= jQuery.i18n.prop('VAR_VS_PEEVEFAIL');										// Peer 장비의 Virtual Server Enable에 실패했습니다.
						VAR_VS_DISVSEL						= jQuery.i18n.prop('VAR_VS_DISVSEL');										// Disable을 원하시는 Virtual Server를 선택하십시오.
						VAR_VS_VSEDFAIL						= jQuery.i18n.prop('VAR_VS_VSEDFAIL');										// Virtual Server Disable에 실패했습니다.
						VAR_VS_VSEDSEL						= jQuery.i18n.prop('VAR_VS_VSEDSEL');										// 삭제를 원하시는 Virtual Server를 선택하십시오.
						VAR_VS_VSEDEL						= jQuery.i18n.prop('VAR_VS_VSEDEL');										// Virtual Server를 삭제 하시겠습니까?
						VAR_VS_VIRDFAIL						= jQuery.i18n.prop('VAR_VS_VIRDFAIL');										// Virtual Server 삭제에 실패했습니다.
						VAR_VS_PEEVDFAIL					= jQuery.i18n.prop('VAR_VS_PEEVDFAIL');										// Peer 장비의 Virtual Server 삭제에 실패했습니다.
						VAR_VS_PEEVDIFAIL					= jQuery.i18n.prop('VAR_VS_PEEVDIFAIL');									// Peer 장비의 Virtual Server Disable에 실패했습니다.
						VAR_VS_VSPORTRANGE					= jQuery.i18n.prop('VAR_VS_VSPORTRANGE');									// 포트의 범위가 맞지 않습니다. (가능한 포트 범위 0~65534)			
						VAR_VS_VSTIMEERROR					= jQuery.i18n.prop('VAR_VS_VSTIMEERROR');									// 설정에 실패했습니다.\n설정을 시도하기 전에 설정 변경 내역이 있습니다.			
						VAR_VS_HASNULLVALUE					= jQuery.i18n.prop('VAR_VS_HASNULLVALUE');									// 이전 설정에 복구 불가능한 설정이 존재합니다.\n복구를 진행할 수 없습니다.
						VAR_VS_INTERFACEAREA				= jQuery.i18n.prop('VAR_VS_INTERFACEAREA');									// VRRP Interface 추가 시 가능한 IP 범위를 초과하였습니다.
						
						// virtualserver (profile) - profile
						VAR_PROFILE_LFAIL					= jQuery.i18n.prop('VAR_PROFILE_LFAIL');									// 프로파일 데이터 로딩에 실패했습니다.
						VAR_PROFILE_DSELE					= jQuery.i18n.prop('VAR_PROFILE_DSELE');									// 삭제를 원하시는 프로파일을 선택하십시오.
						VAR_PROFILE_DEL						= jQuery.i18n.prop('VAR_PROFILE_DEL');										// 프로파일을 삭제 하시겠습니까?
						VAR_PROFILE_REGI_SUCC				= jQuery.i18n.prop('VAR_PROFILE_REGI_SUCC');								// 성공적으로 등록하였습니다.
						VAR_PROFILE_ADDFAIL					= jQuery.i18n.prop('VAR_PROFILE_ADDFAIL');									// 프로파일 추가에 실패했습니다. 
						VAR_PROFILE_NNOTALL					= jQuery.i18n.prop('VAR_PROFILE_NNOTALL');									// 입력하신 이름은 허용되지 않습니다.
						VAR_PROFILE_TIMPIALLOW				= jQuery.i18n.prop('VAR_PROFILE_TIMPIALLOW');								// Timeout값은 양의 정수만 허용됩니다.
						VAR_PROFILE_DELFAIL					= jQuery.i18n.prop('VAR_PROFILE_DELFAIL');									// 프로파일 삭제에 실패했습니다.
						VAR_PROFILE_NAME					= jQuery.i18n.prop('VAR_PROFILE_NAME');										// 프로파일 이름을 입력하세요.
						VAR_PROFILE_USED					= jQuery.i18n.prop('VAR_PROFILE_USED');										// 사용중인 프로파일은 삭제할 수 없습니다.
						VAR_PROFILE_NOTEXIST				= jQuery.i18n.prop('VAR_PROFILE_NOTEXIST');									// 존재하지 않는 프로파일입니다.
						
						// node (realserver)
						VAR_NODE_NAME_INPUT					= jQuery.i18n.prop('VAR_NODE_NAME_INPUT');									// RealServerGroup 이름을 입력하세요.
						VAR_NODE_GROUP_MOVENOT				= jQuery.i18n.prop('VAR_NODE_GROUP_MOVENOT'); 								// 이동할 Group이 없습니다.
						VAR_NODE_NOT_SELECTED				= jQuery.i18n.prop('VAR_NODE_NOT_SELECTED');								// 선택된 real server가 없습니다.
						VAR_NODE_SELECTED_ALREADY			= jQuery.i18n.prop('VAR_NODE_SELECTED_ALREADY');							// 선택된 real server 에는 이미
						VAR_NODE_STATUS_HAVE				= jQuery.i18n.prop('VAR_NODE_STATUS_HAVE');									// 상태가 있습니다.
						VAR_NODE_GROUP_MODIFYFAIL			= jQuery.i18n.prop('VAR_NODE_GROUP_MODIFYFAIL');							// Real Server Group 수정에 실패했습니다.
						VAR_NODE_GROUP_ADDFAIL				= jQuery.i18n.prop('VAR_NODE_GROUP_ADDFAIL');								// Real Server Group 추가에 실패했습니다.
						VAR_NODE_GROUP_DEL					= jQuery.i18n.prop('VAR_NODE_GROUP_DEL');									// Real Server 그룹을 삭제하시겠습니까?
						VAR_NODE_GROUP_DELFAIL				= jQuery.i18n.prop('VAR_NODE_GROUP_DELFAIL');								// Real Server Group 삭제에 실패했습니다.
						VAR_NODE_GROUP_FEWMOVE				= jQuery.i18n.prop('VAR_NODE_GROUP_FEWMOVE');								// 개의 Real Server를 선택한 그룹으로 이동하시겠습니까?
						VAR_NODE_GROUP_MOVEFAIL				= jQuery.i18n.prop('VAR_NODE_GROUP_MOVEFAIL');								// Real Server Group으로 이동에 실패했습니다.
						VAR_NODE_GROUP_STATUSMODIFY			= jQuery.i18n.prop('VAR_NODE_GROUP_STATUSMODIFY');							// 개의 Real Server 상태를 변경하시겠습니까?
						VAR_NODE_GROUP_STATUSMODIFYS		= jQuery.i18n.prop('VAR_NODE_GROUP_STATUSMODIFYS');							// 개의 Real Server 상태를 변경하시겠습니까?
						VAR_NODE_GROUP_SETFAIL				= jQuery.i18n.prop('VAR_NODE_GROUP_SETFAIL');								// real server 설정에  실패했습니다.
						VAR_NODE_SYNC_PROGRESSING 			= jQuery.i18n.prop('VAR_NODE_SYNC_PROGRESSING');							// 동기화를 진행하시겠습니까?
						VAR_NODE_SYNC_COMPLETE_RESET 		= jQuery.i18n.prop('VAR_NODE_SYNC_COMPLETE_RESET');							// 동기화가 완료가 되었습니다. 설정을 다시 진행하시기 바랍니다.
						
						// slbSchedule
						VAR_SCHEDULE_REQ_SELECT = jQuery.i18n.prop('VAR_SCHEDULE_REQ_SELECT');											// 선택을 원하는 요청자를 선택하십시오.
						VAR_SCHEDULE_REQ_DSELECT = jQuery.i18n.prop('VAR_SCHEDULE_REQ_DSELECT');										// 삭제를 원하는 요청자를 선택하십시오.
						VAR_SCHEDULE_REQ_DEL = jQuery.i18n.prop('VAR_SCHEDULE_REQ_DEL');												// 삭제를 원하시는 요청자를 선택하십시오.
						VAR_VS_SCHEDULEUSER_CNTFAIL = jQuery.i18n.prop('VAR_VS_SCHEDULEUSER_CNTFAIL');									// slb스케줄링 사용자 개수 추출에 실패했습니다.
						VAR_VS_SCHEDULEUSER_LOADFAIL = jQuery.i18n.prop('VAR_VS_SCHEDULEUSER_LOADFAIL');								// slb스케줄링 사용자 페이지 로딩에 실패했습니다.
						VAR_VS_SCHEDULEUSER_ADDFAIL = jQuery.i18n.prop('VAR_VS_SCHEDULEUSER_ADDFAIL');									// slb스케줄링 사용자 추가에 실패했습니다.
						VAR_VS_SCHEDULEUSER_DELFAIL = jQuery.i18n.prop('VAR_VS_SCHEDULEUSER_DELFAIL');									// slb스케줄링 사용자 삭제에 실패했습니다.
						VAR_VS_SCHEDULEUSER_MODIFYFAIL = jQuery.i18n.prop('VAR_VS_SCHEDULEUSER_MODIFYFAIL');							// slb스케줄링 사용자 변경에 실패했습니다.
						VAR_VS_SCHEDULEUSER_LOADFAIL = jQuery.i18n.prop('VAR_VS_SCHEDULEUSER_LOADFAIL');								// slb스케줄링 요청자 페이지 로딩에 실패했습니다.
						VAR_VS_SCHEDULEUSER_DELFAIL = jQuery.i18n.prop('VAR_VS_SCHEDULEUSER_DELFAIL');									// slb스케줄링 요청자 삭제에 실패했습니다.
						VAR_VS_SCHEDULERESP_LOADFAIL = jQuery.i18n.prop('VAR_VS_SCHEDULERESP_LOADFAIL');								// slb스케줄링 수신자 페이지 로딩에 실패했습니다.
						VAR_VS_SCHEDULECNTFAIL = jQuery.i18n.prop('VAR_VS_SCHEDULECNTFAIL');											// slb스케줄링 개수 추출에 실패했습니다.
						VAR_VS_SCHEDULELOADFAIL = jQuery.i18n.prop('VAR_VS_SCHEDULELOADFAIL');											// slb스케줄링 페이지 로딩에 실패했습니다.
						VAR_VS_SCHEDULEDELSEL = jQuery.i18n.prop('VAR_VS_SCHEDULEDELSEL');												// 삭제를 원하시는 스케줄을 선택하십시오.
						VAR_VS_SCHEDULEDEL = jQuery.i18n.prop('VAR_VS_SCHEDULEDEL');													// 스케줄을 삭제 하시겠습니까?
						VAR_VS_SCHEDULEDLFAIL = jQuery.i18n.prop('VAR_VS_SCHEDULEDLFAIL');												// 스케줄 삭제에 실패했습니다.
						VAR_VS_SCHEDULE_NAMEINPUT = jQuery.i18n.prop('VAR_VS_SCHEDULE_NAMEINPUT');										// 이름을 입력하세요.
						VAR_VS_SCHEDULE_TEAMINPUT = jQuery.i18n.prop('VAR_VS_SCHEDULE_TEAMINPUT');										// 부서/팀을 입력하세요.
						VAR_VS_SCHEDULE_PHONEINPUT = jQuery.i18n.prop('VAR_VS_SCHEDULE_PHONEINPUT');									// 전화번호를 입력하세요.
						VAR_VS_SCHEDULE_RECEIVERSEL = jQuery.i18n.prop('VAR_VS_SCHEDULE_RECEIVERSEL');									// 수신자를 선택하세요.
						VAR_VS_SCHEDULE_ADDFAIL = jQuery.i18n.prop('VAR_VS_SCHEDULE_ADDFAIL'); 											// slb스케줄링 등록에 실패했습니다.
						VAR_VS_SCHEDULE_MODIFYFAIL = jQuery.i18n.prop('VAR_VS_SCHEDULE_MODIFYFAIL'); 									// slb스케줄링 수정에 실패했습니다.
						
						// common JS
						VAR_COMM_CONTENT					= jQuery.i18n.prop('VAR_COMM_CONTENT');										// 내용
						VAR_COMM_ACTION						= jQuery.i18n.prop('VAR_COMM_ACTION');										// 조치
						VAR_COMM_ADCVSIPSTAT				= jQuery.i18n.prop('VAR_COMM_ADCVSIPSTAT');									// adc virtual server ip 및 상태
						
						// etc JS
						VAR_ETC_TASKRUN						= jQuery.i18n.prop('VAR_ETC_TASKRUN');										// 다른 작업이 실행 중입니다. 작업 완료 후 다시 실행해 주시기 바랍니다.
						VAR_ETC_FIRST						= jQuery.i18n.prop('VAR_ETC_FIRST');										// 처음으로
						VAR_ETC_PRE							= jQuery.i18n.prop('VAR_ETC_PRE');											// 이전페이지
						VAR_ETC_NEXT						= jQuery.i18n.prop('VAR_ETC_NEXT');											// 다음페이지
						VAR_ETC_LAST						= jQuery.i18n.prop('VAR_ETC_LAST');											// 마지막으로
						VAR_ETC_LISTCOUNT					= jQuery.i18n.prop('VAR_ETC_LISTCOUNT');									// 개
						VAR_ETC_LISTOPEN					= jQuery.i18n.prop('VAR_ETC_LISTOPEN');										// 목록 열기
						VAR_ETC_ALL							= jQuery.i18n.prop('VAR_ETC_ALL');											// 전체
						
						// obvalidation Message
						MSG_VALID_EMPTY 					= jQuery.i18n.prop('MSG_VALID_EMPTY');
						MSG_VALID_MISSING_TYPE 				= jQuery.i18n.prop('MSG_VALID_MISSING_TYPE');
						MSG_VALID_TYPE_DEFAULT	 			= jQuery.i18n.prop('MSG_VALID_TYPE_DEFAULT');
						MSG_VALID_TYPE_PREFIX_NOT 			= jQuery.i18n.prop('MSG_VALID_TYPE_PREFIX_NOT');
						MSG_VALID_TYPE_KO 					= jQuery.i18n.prop('MSG_VALID_TYPE_KO');
						MSG_VALID_TYPE_EN 					= jQuery.i18n.prop('MSG_VALID_TYPE_EN');
						MSG_VALID_TYPE_SPECIAL 				= jQuery.i18n.prop('MSG_VALID_TYPE_SPECIAL');
						MSG_VALID_TYPE_NUMBER 				= jQuery.i18n.prop('MSG_VALID_TYPE_NUMBER');
						MSG_VALID_TYPE_EMAIL 				= jQuery.i18n.prop('MSG_VALID_TYPE_EMAIL');
						MSG_VALID_TYPE_IP 					= jQuery.i18n.prop('MSG_VALID_TYPE_IP');
						MSG_VALID_TYPE_MAC 					= jQuery.i18n.prop('MSG_VALID_TYPE_MAC');
						MSG_VALID_TYPE_F5ID 				= jQuery.i18n.prop('MSG_VALID_TYPE_F5ID');
						MSG_VALID_TYPE_PHONENUM			 	= jQuery.i18n.prop('MSG_VALID_TYPE_PHONENUM');
						MSG_VALID_TYPE_NAME 				= jQuery.i18n.prop('MSG_VALID_TYPE_NAME');
						MSG_VALID_TYPE_EN_NAME 				= jQuery.i18n.prop('MSG_VALID_TYPE_EN_NAME');
						MSG_VALID_TYPE_ID	 				= jQuery.i18n.prop('MSG_VALID_TYPE_ID');
						MSG_VALID_TO_SMALL 					= jQuery.i18n.prop('MSG_VALID_TO_SMALL');
						MSG_VALID_TO_LARGE 					= jQuery.i18n.prop('MSG_VALID_TO_LARGE');
						MSG_VALID_OUT_OF_RANGE 				= jQuery.i18n.prop('MSG_VALID_OUT_OF_RANGE');
						MSG_VALID_TO_SHORT 					= jQuery.i18n.prop('MSG_VALID_TO_SHORT');
						MSG_VALID_TO_LONG 					= jQuery.i18n.prop('MSG_VALID_TO_LONG');
						MSG_VALID_OUT_OF_LENGTH_RANGE 		= jQuery.i18n.prop('MSG_VALID_OUT_OF_LENGTH_RANGE');
						MSG_VALID_NOT_EQUAL 				= jQuery.i18n.prop('MSG_VALID_NOT_EQUAL');
						MSG_VALID_MISS_MATCH 				= jQuery.i18n.prop('MSG_VALID_MISS_MATCH');
						MSG_ERROR_VALIDATION_FAIL 			= jQuery.i18n.prop('MSG_ERROR_VALIDATION_FAIL');
						
		    	    	// Accessing a simple value through the map
	    	        	//msg_hello = jQuery.i18n.prop('msg_hello');
		    	        // Accessing a value with placeholders through the map
		    	        //msg_world = jQuery.i18n.prop('msg_complex', 'John');
		    	        // Accessing a simple value through a JS variable
		    	        //alert(msg_hello +' '+ msg_world);
		    	        // Accessing a value with placeholders through a JS function
		    	        //alert(msg_complex('John'));
		    	    }
		    	});
		    }
		</script>
	</body>
</html>
