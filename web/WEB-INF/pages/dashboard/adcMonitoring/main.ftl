<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="refresh" content="1800; url=/dashboard/adcMon/adcMonMain.action" />
<title>Dashboard ADC 모니터링</title>
<link type="text/css" rel="stylesheet" href="../../css/reset.css" />
<link type="text/css" rel="stylesheet" href="../../css/comm.css" />
<!--
<link type="text/css" rel="stylesheet" href="../../css/content.css" />
-->
<link type="text/css" rel="stylesheet" href="../../css/popup.css" />
<link type="text/css" rel="stylesheet" href="../../css/flowit-1.1.0.css" />
<link type="text/css" rel="stylesheet" href="../../css/style_dashboard.css" />
<link type="text/css" rel="stylesheet" href="../../css/content_dashboard.css" />
<script type="text/javascript" src="../../js/extern/amchart/amcharts.js"></script>
<script type="text/javascript" src="../../js/extern/amchart/pie.js"></script>
<script type="text/javascript" src="../../js/extern/amchart/serial.js"></script>
<script type="text/javascript" src="../../js/extern/log4javascript_lite.js"></script>
<script type="text/javascript" src="../../js/extern/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="../../js/extern/jquery-ui-1.10.2.custom.min.js"></script>
<script type="text/javascript" src="../../js/extern/jquery.fixheadertable.min.js"></script>
<script type="text/javascript" src="../../js/extern/jquery.ajaxmanager.min.js"></script>
<script type="text/javascript" src="../../js/extern/jquery.form-custom.min.js"></script>
<script type="text/javascript" src="../../js/extern/jquery.nimble.loader-2.0.1.js"></script>
<script type="text/javascript" src="../../js/extern/flowit-1.2.0.min.js"></script>
<script type="text/javascript" src="../../js/common.js"></script>
<script type="text/javascript" src="../../js/extern/obchart-2.0.0.js"></script>
<script type="text/javascript" src="../../js/dashboard/adcMon/adcSdsDashboard.js"></script>
<script type="text/javascript" src="../../js/dashboard/adcMon/content.js"></script>
<script type="text/javascript" src="../../js/dashboard/adcMon/header.js"></script>

<script type="text/javascript">
	$(function() 
	{
		//initTable([ "#table2 tbody tr" ], [ 0, 1, 3, 4 ], [ -1 ]);
		//initTable([ "#table3 tbody tr", "#table6 tbody tr" ], [ 0 ], [ -1 ]);
		//initTable([ "#table4 tbody tr" ], [ 1, 2 ], [ -1 ]);
		//initTable([ "#table5 tbody tr" ], [ 0, 1 ], [ -1 ]);
		//initTable([ "#table7 tbody tr" ], [ 0, 1, 2, 3 ], [ -1 ]);
		
		var ajaxManager;
		var adcSdsDashboard;		
		
		ajaxManager = new FlowitAjax();
		obchart = new OBChart();
		adcSdsDashboard = new AdcSdsDashboard();
		adcSdsDashboard.load();
	});
	
</script>
</head>
<body>
	<div class="dashboard_wrap">
		<div class="header_monitor">
			<span class="logo">		
				<a class="css_textCursor"><img src="/imgs/dashboard/header_monitor_logo.png"></a>
			</span>	
		</div>
		<div class="listcontainer_monitor"></div>
		<!-- 장애 모니터링 현황(1주일) -->
		<div class="monitor_con3_1 faultmonitoring_chart_area"></div>
		<div class="monitor_con3_2 faultmonitoring_grid_area"></div>		
		<!-- ADC 설정 변경 현황(1주일) -->
		<div class="monitor_con5_1 historymonitoring_chart_area"></div>
		<div class="monitor_con5_2 historymonitoring_grid_area"></div>		 
		
		<div class="monitor_con1"></div>	
		<!--전체 ADC 트래픽 현황 & Top5 Virtual Server Connection Chart -->
		<div class="monitor_con4 allAdcMonitoring_chart_area"></div>
		<div class="monitor_con6 top5VsMonitoring_chart_area"></div>		
		<!--전체 ADC 트래픽 현황 & Top5 Virtual Server Connection Grid -->
		<div class="monitor_con4-1 allAdcMonitoring_grid_area"></div>
		<div class="monitor_con6-1 top5VsMonitoring_grid_area"></div>
		
<!--	</div>	-->		
<!--	<div class="dash_content dash_summary">		-->
<!--	<div class="dash_content">
			<div class="section5">
				<p class="contains1"></p>			
				<p class="contains2"></p>
			</div>			
			<div class="summary_cont">
				<div class="list_area"></div>
				<div class="graph_area"></div>	
				<div class="list_area2"></div>
				<div class="graph_area2"></div>			
			</div>				
		</div>	
-->
	</div>
</body>
</html>