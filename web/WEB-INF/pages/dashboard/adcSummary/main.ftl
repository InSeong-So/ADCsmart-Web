<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="refresh" content="1800; url=/dashboard/sds/main.action" />
<title>Dashboard ADC 요약</title>
<link type="text/css" rel="stylesheet" href="../../css/reset.css" />
<link type="text/css" rel="stylesheet" href="../../css/comm.css" />
<link type="text/css" rel="stylesheet" href="../../css/popup.css" />
<link type="text/css" rel="stylesheet" href="../../css/style_dashboard.css" />
<link type="text/css" rel="stylesheet" href="../../css/content_dashboard.css" />
<link type="text/css" rel="stylesheet" href="../../css/button.css" />
<script type="text/javascript" src="../../js/extern/amchart/amcharts.js"></script>
<script type="text/javascript" src="../../js/extern/amchart/pie.js"></script>
<script type="text/javascript" src="../../js/extern/amchart/serial.js"></script>
<script type="text/javascript" src="../../js/extern/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="../../js/extern/jquery-ui-1.10.2.custom.min.js"></script>
<script type="text/javascript" src="../../js/extern/flowit-1.2.0.min.js"></script>
<script type="text/javascript" src="../../js/common.js"></script>
<script type="text/javascript" src="../../js/extern/jquery.fixheadertable.min.js"></script>
<script type="text/javascript" src="../../js/extern/jquery.ajaxmanager.min.js"></script>
<script type="text/javascript" src="../../js/extern/jquery.form-custom.min.js"></script>
<script type="text/javascript" src="../../js/extern/jquery.nimble.loader-2.0.1.js"></script>
<script type="text/javascript" src="../../js/extern/flowit-1.2.0.min.js"></script>
<script type="text/javascript" src="../../js/extern/obajax-1.2.0.js"></script>
<script type="text/javascript" src="../../js/dashboard/sds/dashboard.js"></script>
<script type="text/javascript" src="../../js/dashboard/sds/leftpanel.js"></script>
<script type="text/javascript" src="../../js/dashboard/sds/header.js"></script>
<script type="text/javascript" src="../../js/dashboard/sds/contents.js"></script>
<script type="text/javascript" src="../../js/extern/obchart-2.0.0.js"></script>
<script type="text/javascript" src="../../js/extern/jquery.ticker.min.js"></script>
<script type="text/javascript">
	/* $(function() {
		closeAccordionType1('.dash_content .accordion_type1');
	}); */
	
	var rowNum = null;
    var rowHeight = null;
    var table1 = null;
    
	var ajaxManager;
    var dashboard;
    
    var obchart;

    $(function() 
    {
	    //테이블 원본 저장
	    table1 = $('#table1').html();

	    // acordion 슬라이드 효과
	    closeAccordionType1('.dash_content .accordion_type1');

	    // table2 스타일 정의
	    $('#table1').fixheadertable({
	        height : 154,
	        colratio : [-1, 15, 5, 14, 30, 10, 10, 8, 8]
	    });
	    tableHeadSize('#table1', {
	        'col' : '8',
	        'hsize' : '4'
	    });
	    
	   initTable([ "#table1 tbody tr" ], [-1], [-1]);

	    // 대시보드 꽉찬화면 스타일 정의
	    /* $(window).resize(dash_resize);
	    $(window).trigger('resize'); */
	    
	    ajaxManager = new FlowitAjax();
	    ajaxManagerOB = new OBAjax();
	    obchart = new OBChart();
	    dashboard = new SdsDashboard();
	    dashboard.load();
    });
</script>
</head>
<body>	

	<div class="header">
		<span class="logo">
			<img src="../../imgs/dashboard/header_summary_logo.png"/>
		</span>
	</div>
    <div class="contents_1"></div>
	<div id="leftPane" class="listcontainer"></div>	
	<div class="contents_2 list_area"></div>
	<div class="contents_3 graph_area"></div>	
	<div class="contents_4">
		<form id="alertCheckfrm">
			<input type="checkbox" id="checkbox" value="true"  checked="checked"><span input type="text" class="txt_blue statusText">경고창표시</span>
		</form>
	</div>

	
	<!-- alert 팝업 -->
	<div id="dash_action" class="pop_type_wrap">
		<h2>
			<span class="f_left faultTitle"></span>
			<span class="date f_right"><!--발생일시 : --><span class="faultTime"></span></span>
		</h2>
		<div class="pop_contents">
			<ul>
				<span class="adcMsg">
					<li class="left">ADC</li>
						<table>
	            			<colgroup>
								<col width="30%"/>					
								<col width="70%"/>
							</colgroup>				
								<tr>
									<th class="thbg">IP</th>
									<th class="thbg">이름</th>
								</tr>
								<tr>
									<td class="tdip adcIp"></td>
									<td class="tdname adcName"></td>
								</tr>	
						</table>
				</span>
				<span class="secondMsg">			
					<li class="left">Virtual Server</li>
						<table>
				            <colgroup>
								<col width="30%"/>					
								<col width="70%"/>
							</colgroup>				
							<tr>
								<th class="thbg">IP</th>
								<th class="thbg">이름</th>
							</tr>
							<tr>						
								<td class="tdip vsIp"></td>
								<td class="tdname vsName"></td>
							</tr>
					
						</table>
				</span>
				<span class="3rdMsg">			
					<li class="left">Group member</li>
						<table>
				            <colgroup>
								<col width="100%"/>					
							</colgroup>				
							<tr>
								<th class="thbg">IP : PORT</th>
							</tr>
							<tr>
								<td class="tdname center"><span class="memberIp"></span></td>
							</tr>	
						</table>
				</span>				
				<span class="4rdMsg">			
					<li class="left">Link Down</li>
						<table>
				            <colgroup>
								<col width="30%"/>					
								<col width="50%"/>
								<col width="20%"/>
							</colgroup>				
							<tr>
								<th class="thbg">ADC IP</th>
								<th class="thbg">ADC 이름</th>
								<th class="thbg center">인터페이스</th>
							</tr>
							<tr>
								<td class="tdip adcIp"></td>
								<td class="tdname adcName"></td>
								<td class="tdname linkNo"></td>
							</tr>
						</table>
				</span>								
				<li class="faultBeep"></li>	
				<li class="guide_msg">총 경보 <strong class="remainingAlertCount"></strong>개가 있습니다.</li>
			</ul>
		</div>
		<div class="center">
			<span href="#" id="refresh">
	      		<input class="Btn_white okLnk" type="button" value="확인">
			</span> <br/><br/>		
<!--			<a class="okLnk" href="#" title="확인"><img src="../../imgs/popup/btn_confirm.gif" alt="확인"  /></a>-->
			<!--<a class="closeWndLnk" href="#" title="취소"><img src="../../imgs/popup/btn_cancel.gif" alt="취소"  /></a>-->
			<!--<span>경보 개수: <strong class="remainingAlertCount"></strong></span>-->
		</div>
		<!--<p class="close">
			<a class="closeWndLnk" href="#" title="닫기"><img src="../../imgs/popup/btn_clse.gif" alt="닫기" /></a>
		</p>-->
	</div>
</body>
</html>