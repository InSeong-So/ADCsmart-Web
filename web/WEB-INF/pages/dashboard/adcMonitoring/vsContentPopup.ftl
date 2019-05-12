<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<#if (popUpcontents =='connection') >
	<title> Connection 그래프</title>
<#elseif (popUpcontents =='throughput') >
	<title> Throughput 그래프</title>
</#if>
<link type="text/css" rel="stylesheet" href="/css/reset.css" />
<link type="text/css" rel="stylesheet" href="/css/comm.css" />
<link type="text/css" rel="stylesheet" href="/css/content.css" />
<link type="text/css" rel="stylesheet" href="/css/flowit-1.1.0.css" />
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


<script type="text/javascript">
	var vsPopup = new VsPopup();
	
	$(function(){
	    
	     var curCategory = "${curCategory}";
	     var curIndex = "${curIndex}";
	     var curName = "${curName}";
	     var curVendor = "${curVendor}";
	     var popUpcontents = "${popUpcontents}";
	     vsPopup.loadVsContents(curCategory,curIndex,curName,curVendor,popUpcontents);     
	});
	     
</script>

</head>
<body>
	<!-- 실시간 세션 정보 팝업 -->
	<div id="testPop" class="pop_type_wrap sessionPopup">	
	<#if (popUpcontents =='connection') >
		<h2>Connection</h2>
	<#elseif (popUpcontents =='throughput') >
		<h2>Throughput</h2>
	</#if>
		<div class="pop_contents">
		<table id="table1" class="form_type1 mar_top10">
		<caption>Virtual Server 모니터링</caption>
		<colgroup>
			<col width="23%" />
			<col width="77%" />
		</colgroup>
		<tbody>
			<tr>
				<th class="bor_top">조회 기간</th>
				<td class="bor_top">
				<span class="search_period">
						<input id = "startTime" name="startTime" class="formtext_wdth115 mar_rgt5"  type="text" title="조회 기간선택"/>
						&nbsp;~&nbsp;
						<input id = "endTime" name="endTime" class="formtext_wdth115 mar_rgt5" type="text" title="조회 기간선택"/>
						
				</span>
				<a class="f_right mar_rgt7" id="refresh" href="#"><img src="/imgs/common/btn_refresh.gif" alt="새로고침" /></a>
				</td>
			</tr>
		</tbody>
	</table>
	</div>
	<div class="gragh_sessnInfo">
		<h3>그래프</h3>
	</div>
		<p class="right mar_rgt10 mar_btm5">
			<a href="#" onclick="closePopup()" title="닫기"><img src="/imgs/popup/btn_close.gif" alt="닫기"/></a>
		</p>
	</div>
	<!-- //실시간 세션 정보 팝업 -->
</body>
</html>