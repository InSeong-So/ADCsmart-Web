<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>adcsmart- UI TEST 입니다.</title>
<link rel="stylesheet" href="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css" />
<link rel="stylesheet" href="/resources/demos/style.css" />
<link type="text/css" rel="stylesheet" href="/test/testcss/testUI.css" />
<link type="text/css" rel="stylesheet" href="/css/comm.css" />
<link type="text/css" rel="stylesheet" href="/css/content.css" />
<link type="text/css" rel="stylesheet" href="/css/newContent.css" />
<link type="text/css" rel="stylesheet" href="/css/flowit-1.1.0.css" />
<link type="text/css" rel="stylesheet" href="/css/style_default.css" />
<link type="text/css" rel="stylesheet" href="/css/button.css" />
<script type="text/javascript" src="/js/extern/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="/js/extern/jquery-ui-1.10.2.custom.min.js"></script>
<script type="text/javascript" src="/js/extern/amcharts.js"></script>
<script type="text/javascript" src="/js/extern/flowit-1.2.0.min.js"></script>
<script type="text/javascript" src="/js/extern/obajax-1.2.0.js"></script>
<script type="text/javascript" src="/js/extern/jquery.fixheadertable.min.js"></script>
<script type="text/javascript" src="/js/extern/jquery.ajaxmanager.min.js"></script>
<script type="text/javascript" src="/js/extern/jquery.form-custom.min.js"></script>
<script type="text/javascript" src="/js/extern/jquery.nimble.loader-2.0.1.js"></script>
<script type="text/javascript" src="/js/common.js"></script>
<script type="text/javascript" src="/js/extern/obchart-2.0.0.js"></script>	
	
<script type="text/javascript" src="/test/testjs/testUI.js"></script>
<script type="text/javascript" src="/test/testjs/jquery.PrintArea.js"></script>
</head>
<script type="text/javascript">
	$(function()
	{
		var OBajaxManager;
		var ajaxManager;
		var testUI;
		
		OBajaxManager = new OBAjax();
		ajaxManager = new FlowitAjax();
		obchart = new OBChart();
		testUI = new TestUI();
		testUI.load();
	});	
</script>
<body>
	<div class="testUI_page">
	</div>
</body>
</html>