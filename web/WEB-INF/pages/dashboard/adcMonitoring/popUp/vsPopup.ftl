<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<#if (popUpcontents =='vsConnection') >
	<title>Connection 그래프</title>
<#elseif (popUpcontents =='vsThroughput') >
	<title>Throughput 그래프</title>
</#if>
<link type="text/css" rel="stylesheet" href="/css/reset.css" />
<link type="text/css" rel="stylesheet" href="/css/comm.css" />
<link type="text/css" rel="stylesheet" href="/css/newContent.css" />
<link type="text/css" rel="stylesheet" href="/css/button.css" />
<link type="text/css" rel="stylesheet" href="/css/flowit-1.1.0.css" />
<script type="text/javascript" src="/js/extern/amchart/amcharts.js"></script>
<script type="text/javascript" src="/js/extern/amchart/pie.js"></script>
<script type="text/javascript" src="/js/extern/amchart/serial.js"></script>
<script type="text/javascript" src="/js/extern/log4javascript_lite.js"></script>
<script type="text/javascript" src="/js/extern/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="/js/extern/jquery-ui-1.10.2.custom.min.js"></script>
<script type="text/javascript" src="/js/extern/jquery.fixheadertable.min.js"></script>
<script type="text/javascript" src="/js/extern/jquery.ajaxmanager.min.js"></script>
<script type="text/javascript" src="/js/extern/jquery.form-custom.min.js"></script>
<script type="text/javascript" src="/js/extern/jquery.nimble.loader-2.0.1.js"></script>
<script type="text/javascript" src="/js/extern/flowit-1.2.0.min.js"></script>
<script type="text/javascript" src="/js/common.js"></script>
<script type="text/javascript" src="/js/dashboard/adcMon/popUp/vsPopup.js"></script>
<script type="text/javascript" src="/js/dashboard/adcMon/header.js"></script>
<script type="text/javascript" src="/js/extern/obchart-2.0.0.js"></script>
<script type="text/javascript">
	var vsPopup = new VsPopup();
	
	$(function(){
	     var vsIndex = "${vsIndex}";
	     var curVendor = "${curVendor}";
	     var popUpcontents = "${popUpcontents}";
	     obchart = new OBChart();
	     vsPopup.loadVsPopUpContents(vsIndex,curVendor,popUpcontents,false);     
	});
	     
</script>

</head>
<body>
	<!-- 실시간 세션 정보 팝업 -->
	<div id="realSessInfo" class="pop_type_wrap sessionPopup">	
	<#if (popUpcontents =='vsConnection') >
		<h2>Connection  VirtualServer IP : ${vsIp} PORT : ${vsPort}</h2>
	<#elseif (popUpcontents =='vsThroughput') >
		<h2>Throughput  VirtualServer Ip : ${vsIp} PORT : ${vsPort}</h2>
	</#if>
		<div class="pop_contents">
		</div>

		<p class="close_center">
			<span>
				<input type="button" class="Btn_white" onclick="closePopup()" value="닫기">
			</span>
		</p>
	</div>
<!-- //실시간 세션 정보 팝업 -->
</body>
</html>