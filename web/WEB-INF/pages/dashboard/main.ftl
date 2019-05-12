<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />        
<title>ADCsmart - Openbase: Dynamic DashBoard</title>
<link rel="shortcut icon" href="/imgs/favicon.ico" />
<link rel="stylesheet" href="/resources/demos/style.css" />
<link type="text/css" rel="stylesheet" href="/css/widget_dashboard.css" />
<link type="text/css" rel="stylesheet" href="/css/comm.css" />
<link type="text/css" rel="stylesheet" href="/css/newContent.css" />
<link type="text/css" rel="stylesheet" href="/css/flowit-1.1.0.css" />
<link type="text/css" rel="stylesheet" href="/css/style_default.css" />
<link type="text/css" rel="stylesheet" href="/css/button.css" />
<link type="text/css" rel="stylesheet" href="/css/jquery-ui-1.10.3.custom.css" />
<link type="text/css" rel="stylesheet" href="/css/obalert.css"/>
<script type="text/javascript" src="/js/extern/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="/js/extern/jquery-ui-1.10.2.custom.min.js"></script>
<script type="text/javascript" src="/js/extern/amchart/amcharts.js"></script>
<script type="text/javascript" src="/js/extern/amchart/pie.js"></script>
<script type="text/javascript" src="/js/extern/amchart/serial.js"></script>
<script type="text/javascript" src="/js/extern/flowit-1.2.0.min.js"></script>
<script type="text/javascript" src="/js/extern/obajax-1.2.0.js"></script>
<script type="text/javascript" src="/js/extern/jquery.fixheadertable.min.js"></script>
<script type="text/javascript" src="/js/extern/jquery.ajaxmanager.min.js"></script>
<script type="text/javascript" src="/js/extern/jquery.form-custom.min.js"></script>
<script type="text/javascript" src="/js/extern/jquery.nimble.loader-2.0.1.js"></script>
<script type="text/javascript" src="/js/common.js"></script>
<script type="text/javascript" src="/js/extern/obchart-2.0.0.js"></script>
<script type="text/javascript" src="/test/testjs/jquery.PrintArea.js"></script>
	
<script type="text/javascript" src="/js/dashboard/dashboardHeader.js"></script>
<script type="text/javascript" src="/js/dashboard/widgetChart.js"></script>
<script type="text/javascript" src="/js/message.js"></script>
<script type="text/javascript" src="/js/extern/jquery.i18n.properties-min-1.0.9.js"></script>
<script type="text/javascript" src="/js/extern/textile.js"></script>
<script type="text/javascript" src="/js/extern/jquery.obvalidation.js"></script>
<script type="text/javascript" src="/js/extern/jquery.obalert.js"></script>
<script type="text/javascript" src="/js/extern/moment.min.js"></script>

</head>
<script type="text/javascript">
	var langCode="ko_KR";// 언어 코드. 기본은 한국어.
	$(function()
	{
		var OBajaxManager;
		var ajaxManager;
		var dashboardHeader;
		var widgetChart;
		
		OBajaxManager = new OBAjax();
		ajaxManager = new FlowitAjax();
		obchart = new OBChart();
		dashboardHeader = new DashboardHeader();
		widgetChart = new WidgetChart();
		retrieveInitMessages();
		//dashboardHeader.load();
	});	
	
	function retrieveInitMessages() 
	{
		dashboardHeader = new DashboardHeader();
		FlowitUtil.getJSON({
			url : "/member/retrieveLangCode.action",
			successFn : function(data) 
			{
				langCode  = data.langCode;
				loadLanguageMessages(langCode);
				loadCssFile(langCode);
				dashboardHeader.load();
			},
			completeFn: function(textStatus) 
			{
			}
		});
	}
	
	function loadCssFile(langCode)
	{
		var filename = [];
		if(langCode == "en_US")
		{
			filename = ["/css/eng/widget_dashboard_eng.css", "/css/eng/style_default_eng.css", "/css/eng/newContent_eng.css"];
		}
		
		for (var i = 0; i < filename.length; i++)
		{
			var fileref = document.createElement("link");
			fileref.rel = "stylesheet";
			fileref.type = "text/css";
			fileref.href = filename[i];
			document.getElementsByTagName("head")[0].appendChild(fileref);
		}		
	}
	
	function loadLanguageMessages (langCode) 
    {
    	jQuery.i18n.properties({
    	    name:'Messages', 
    	    path:'/bundle/',
    	    mode:'both',
    	    language:langCode, 
    	    callback: function() {	
    	    	// 데시보드
				VAR_DASH_GROUP						= jQuery.i18n.prop('VAR_DASH_GROUP');				// 그룹
				VAR_DASH_GROUPA						= jQuery.i18n.prop('VAR_DASH_GROUPA');				// 그룹 전체
				VAR_DASH_PREVIEW					= jQuery.i18n.prop('VAR_DASH_PREVIEW');				// 미리보기
				VAR_DASH_THEMAX						= jQuery.i18n.prop('VAR_DASH_THEMAX');				// 하나의 대시보드엔 최대
				VAR_DASH_THEWAAVA					= jQuery.i18n.prop('VAR_DASH_THEWAAVA');			// 개의 위젯을 추가할 수 있습니다.
				VAR_DASH_ADDOPSEL					= jQuery.i18n.prop('VAR_DASH_ADDOPSEL');			// 추가 옵션탭의 기간을 선택하여 주십시오
				VAR_DASH_ADDOSSEL					= jQuery.i18n.prop('VAR_DASH_ADDOSSEL');			// 추가 옵션탭의 status를 선택하여 주십시오
				VAR_DASH_ADDOOSEL					= jQuery.i18n.prop('VAR_DASH_ADDOOSEL');			// 추가 옵션탭의 기준을 선택하여 주십시오
				VAR_DASH_GROSEL						= jQuery.i18n.prop('VAR_DASH_GROSEL');				// 그룹을 선택하여 주십시오
				VAR_DASH_ADCSEL						= jQuery.i18n.prop('VAR_DASH_ADCSEL');				// ADC를 선택하여 주십시오
				VAR_DASH_VSESEL						= jQuery.i18n.prop('VAR_DASH_VSESEL');				// Virtual Server를 선택하여 주십시오
				VAR_DASH_FLBGROUP					= jQuery.i18n.prop('VAR_DASH_FLBGROUP');			// FLB Group을 선택하여 주십시오				
				VAR_DASH_VSERSEL					= jQuery.i18n.prop('VAR_DASH_VSERSEL');				// Virtual Service를 선택하여 주십시오
				VAR_DASH_SAVAWNEXI					= jQuery.i18n.prop('VAR_DASH_SAVAWNEXI');			// 저장 가능한 대시보드 위젯이 존재하지 않습니다.
				VAR_DASH_DEFBNSAVA					= jQuery.i18n.prop('VAR_DASH_DEFBNSAVA');			// 기본으로 제공하는 대시보드는 새 이름으로 저장만 가능합니다.
				VAR_DASH_BORINPUT					= jQuery.i18n.prop('VAR_DASH_BORINPUT');			// 대시보드 이름을 입력하시기 바랍니다.
				VAR_DASH_BOADSEL					= jQuery.i18n.prop('VAR_DASH_BOADSEL');				// 삭제할 Dashboard를 선택해 주십시오
				VAR_DASH_ADCDNAVA					= jQuery.i18n.prop('VAR_DASH_ADCDNAVA');			// ADC 요약은 삭제할 수 없는 대시보드 입니다.
				VAR_DASH_ADCDELNAVA					= jQuery.i18n.prop('VAR_DASH_ADCDELNAVA');			// ADC 모니터링은 삭제할 수 없는 대시보드 입니다.
				VAR_DASH_FAUDNAVA					= jQuery.i18n.prop('VAR_DASH_FAUDNAVA');			// 장비 모니터링은 삭제할 수 없는 대시보드 입니다.
				VAR_DASH_BOADEL						= jQuery.i18n.prop('VAR_DASH_BOADEL');				// 대시보드를 삭제 하시겠습니까?
				VAR_DASH_BOADELSEL					= jQuery.i18n.prop('VAR_DASH_BOADELSEL');			// 
				VAR_DASH_BOADSUC					= jQuery.i18n.prop('VAR_DASH_BOADSUC');				// 대시보드가 삭제 되었습니다.
				VAR_DASH_BOADSUCDEL					= jQuery.i18n.prop('VAR_DASH_BOADSUCDEL');			// 
				VAR_DASH_ALLGROUP					= jQuery.i18n.prop('VAR_DASH_ALLGROUP');			// 전체 그룹
				VAR_DASH_ALRSBOA					= jQuery.i18n.prop('VAR_DASH_ALRSBOA');				// 은 이미 저장된 대시보드 이름입니다.
				VAR_DASH_BOARD						= jQuery.i18n.prop('VAR_DASH_BOARD');				//
				VAR_DASH_BOASSUC					= jQuery.i18n.prop('VAR_DASH_BOASSUC');				// 대시보드 저장이 완료되었습니다.
				VAR_DASH_BOANSAVE					= jQuery.i18n.prop('VAR_DASH_BOANSAVE');			// 새 이름으로 저장 하시겠습니까?
				VAR_DASH_BOANOTSAVE					= jQuery.i18n.prop('VAR_DASH_BOANOTSAVE');			// 대시보드가 저장되지 않았습니다. 계속 진행하시겠습니까?
				// Main alarmMessage PopupData 
				VAR_DASH_ADCDISCONN					= jQuery.i18n.prop('VAR_DASH_ADCDISCONN');			// ADC 끊김
				VAR_DASH_VSDISCONN					= jQuery.i18n.prop('VAR_DASH_VSDISCONN');			// Virtual Server 끊김
				VAR_DASH_GMDISCONN					= jQuery.i18n.prop('VAR_DASH_GMDISCONN');			// GROUP Member 끊김
				VAR_DASH_PMDISCONN					= jQuery.i18n.prop('VAR_DASH_PMDISCONN');			// Pool Member 끊김
				VAR_DASH_ADCLINKDOWN				= jQuery.i18n.prop('VAR_DASH_ADCLINKDOWN');			// ADC Link Down
				// Dashboard widget Notice
				VAR_DASH_NOTICE_TOTAL_NUMBER_OF_ALERTS						= jQuery.i18n.prop('VAR_DASH_NOTICE_TOTAL_NUMBER_OF_ALERTS');
				VAR_DASH_NOTICE_TOTAL_TRENDS_IN_ALERTS						= jQuery.i18n.prop('VAR_DASH_NOTICE_TOTAL_TRENDS_IN_ALERTS');
				VAR_DASH_NOTICE_TOTAL_MOST_RECENT_ALERTS					= jQuery.i18n.prop('VAR_DASH_NOTICE_TOTAL_MOST_RECENT_ALERTS');
				VAR_DASH_NOTICE_TOTAL_TRENDS_IN_CONFIGURATION_CHANGES		= jQuery.i18n.prop('VAR_DASH_NOTICE_TOTAL_TRENDS_IN_CONFIGURATION_CHANGES');
				VAR_DASH_NOTICE_TOTAL_MOST_RECENT_SETTINGS_CHANGES			= jQuery.i18n.prop('VAR_DASH_NOTICE_TOTAL_MOST_RECENT_SETTINGS_CHANGES');
				VAR_DASH_NOTICE_TOTAL_ADC_SUMMARY							= jQuery.i18n.prop('VAR_DASH_NOTICE_TOTAL_ADC_SUMMARY');
				VAR_DASH_NOTICE_TOTAL_VS_SUMMARY							= jQuery.i18n.prop('VAR_DASH_NOTICE_TOTAL_VS_SUMMARY');
				VAR_DASH_NOTICE_TOTAL_TRENDS_IN_CONCURRENT_SESSION			= jQuery.i18n.prop('VAR_DASH_NOTICE_TOTAL_TRENDS_IN_CONCURRENT_SESSION');
				VAR_DASH_NOTICE_TOTAL_TRENDS_IN_CONCURRENT_DETAIL			= jQuery.i18n.prop('VAR_DASH_NOTICE_TOTAL_TRENDS_IN_CONCURRENT_DETAIL');
				VAR_DASH_NOTICE_TOTAL_TRENDS_IN_CONCURRENT_FLBGROUP			= jQuery.i18n.prop('VAR_DASH_NOTICE_TOTAL_TRENDS_IN_CONCURRENT_FLBGROUP');
				VAR_DASH_NOTICE_TOTAL_TRENDS_IN_THROUGHPUT					= jQuery.i18n.prop('VAR_DASH_NOTICE_TOTAL_TRENDS_IN_THROUGHPUT');
				VAR_DASH_NOTICE_TOTAL_TRENDS_IN_ADC_CPU_USAGE				= jQuery.i18n.prop('VAR_DASH_NOTICE_TOTAL_TRENDS_IN_ADC_CPU_USAGE');
				VAR_DASH_NOTICE_TOTAL_TRENDS_IN_ADC_MEMORY_USAGE			= jQuery.i18n.prop('VAR_DASH_NOTICE_TOTAL_TRENDS_IN_ADC_MEMORY_USAGE');
				VAR_DASH_NOTICE_TOTAL_TOTAL_ADC_TRAFFIC						= jQuery.i18n.prop('VAR_DASH_NOTICE_TOTAL_TOTAL_ADC_TRAFFIC');
				VAR_DASH_NOTICE_TOTAL_CHANGES_IN_VS_STATUS					= jQuery.i18n.prop('VAR_DASH_NOTICE_TOTAL_CHANGES_IN_VS_STATUS');
				VAR_DASH_NOTICE_TOTAL_ADC_LIST								= jQuery.i18n.prop('VAR_DASH_NOTICE_TOTAL_ADC_LIST');
				VAR_DASH_NOTICE_TOTAL_VS_LIST								= jQuery.i18n.prop('VAR_DASH_NOTICE_TOTAL_VS_LIST');
				VAR_DASH_NOTICE_TOTAL_TOP_10_VS_BY_THROUGHPUT				= jQuery.i18n.prop('VAR_DASH_NOTICE_TOTAL_TOP_10_VS_BY_THROUGHPUT');
				VAR_DASH_NOTICE_TOTAL_TOP_10_ADC_BY_TRAFFIC					= jQuery.i18n.prop('VAR_DASH_NOTICE_TOTAL_TOP_10_ADC_BY_TRAFFIC');
				VAR_DASH_NOTICE_TOTAL_ADC_MONITORING						= jQuery.i18n.prop('VAR_DASH_NOTICE_TOTAL_ADC_MONITORING');
				VAR_DASH_NOTICE_TOTAL_TRENDS_IN_SERVICE_RESPONSE_TIME		= jQuery.i18n.prop('VAR_DASH_NOTICE_TOTAL_TRENDS_IN_SERVICE_RESPONSE_TIME');	    	    
    	    }
    	});
    }
</script>
<body onselectstart='return false'>
	<div class="DashBoardHeader">

	</div>
</body>
</html>