<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>ADCsmart - Openbase</title>

</head>

<body>
	<div class="content_wrap">
		<div class="content">
			<#if (varIsSDSSite??) && (varIsSDSSite) == true>
			<img src="imgs/icon/blankpage.png" class="css_logo_mainpage" />	
			<#else>
			<img src="imgs/icon/blankpage.png" class="css_logo_mainpage" />			
		    <div>${LANGCODEMAP["MSG_LAYOUT_ONLY_ONEADC_CONTENTMENU"]!} </br>${LANGCODEMAP["MSG_LAYOUT_WANT_ONEADC_SELECT"]!}</div>
	
			</#if>
		</div>  
	</div>	
</body>
</html>