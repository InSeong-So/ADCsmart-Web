var DashboardServiceHeader = Class.create({
	initialize : function() 
	{
		this.ajaxManager = new FlowitAjax();
		this.OBajaxManager = new OBAjax();
		
		this.autoRefreshTimer = undefined;		// 위젯 Refresh Timer 
		this.refreshIntervalSeconds = 30;		// 위젯 Refresh Time 30sec		
	},
	load : function()
	{
		with (this)
		{
			loadDashBoardHeader();
			
			if(this.autoRefreshTimer == undefined){
				autoRefreshDashboard();
			}
		}		
	},
		
	loadDashBoardHeader :function()
	{
		with (this)
		{
			OBajaxManager.runHtml({
				url			: '/dashboard/service/loadDashHeader.action',
				target		: "#wrap .contents",
				successFn	: function(params) 
				{	
					updateRefreshDate();
					registerEvents();					
				}			
			});
		}
	},
	// Dashboard Content 페이지 load (Get Data None)
	loadDashboardContent : function()
	{
		with(this)
		{
			OBajaxManager.runHtml({
				url			: '/dashboard/service/loadDashContent.action',
				target		: 'div .dashboardContentArea1',				
				successFn	: function(params) 
				{					
//					widgetLoadCount = 0;
//					widgetAddCount = 0;
//					G_widgetCount = 0;
//					onLoadWidget(widgetList);
//					selectboxChange(dashboardIndexKey);
//					dashboardIndex = dashboardIndexKey;
////					dashboardName = $('#DashboardList').children('option').filter('value=' + dashboardIndex).text();
//					dashboardName = $('#DashboardList').children('option[value=' + dashboardIndex + ']').text();
//					loadPassKey = true;				
//					sortableUI();
					registerEvents();
				}			
			});
		}
	},
	
	registerEvents : function()
	{
		with (this)
		{
			$('.adcsmartMain').click(function(e)
			{
				e.preventDefault();
				header.loadMainContent();
				if($('.listcontainer_open').parent().hasClass("none"))
					$('.listcontainer_open').click();
				
				$('.systemSub').addClass('none');
				$('.toolSub').addClass('none');
				$('.LocationNavi').removeClass('none');	
			});
			
			$('.openServiceList').on('click', function(e)
			{
		        e.preventDefault();
		        if ($(window).width() < 769) 
		        {
		            $('.set_name').toggleClass("show_name");
					$('.adc-group-none').toggleClass("show_name");
					$('.bundle-no').toggleClass("show_name");
					$('.bundle-detail').toggleClass("show_name");		
		        } 
		        else 
		        {
		            $('.set_name').toggleClass("hide_name");
					$('.adc-group-none').toggleClass("hide_name");
					$('.bundle-no').toggleClass("hide_name");	
					$('.bundle-detail').toggleClass("hide_name");					
				}				
			});	
			
			$('.adcMonitoringLnk').on('click', function(e)
			{
				clearAutoRefreshTimer();
				e.preventDefault();
				with (this) 
				{
					var adcIndex = $(this).parent().attr('index');
					adcSetting._selectAdc(adcIndex);
					adcSetting.setSelectIndex(2);	
					
					if($('.listcontainer_open').parent().hasClass("none"))
						$('.listcontainer_open').click();
					
					$('.systemSub').addClass('none');
					$('.toolSub').addClass('none');
					$('.LocationNavi').removeClass('none');	
					
					monitorAppliance.loadApplianceMonitorContent(adcSetting.getAdc());
				}
				
			});
			
			// 대쉬보드 모달 vs 리스트 클릭
			$('#dashboard-modal-html').on('click','.dashboard-modal-vs-title',function(e){
				clearAutoRefreshTimer();
				e.preventDefault();
				
				var vsIp = $(this).attr('ip');
				var vsPort = $(this).attr('port');
				var vsIndex = $(this).attr('index');
				var adcIndex = $("#dashboard-modal-title").attr('adcIndex');
				adc = {
						index : adcIndex,
						name : undefined,
						type : undefined,
						mode : undefined
				};
				
				adcSetting._selectAdc(adcIndex);
				
				adcSetting.loadContent();
				networkMap.loadNetworkMapContent(adcSetting.getAdc(), 0, vsIp, undefined, taskQ);
				monitorServicePerfomance.updateNavigator(adc, vsIndex, vsPort, vsIp);
			});
			
			$('.autoRefreshChk').click(function(e)
			{						
				if($(this).is(':checked'))
			    {
					autoRefreshDashboard();
			    }
			    else
			    {
			    	clearAutoRefreshTimer();
			    }
			});
			
			$('.adc-group').click(function(e){
				clearAutoRefreshTimer();
			});
			
			$('#dashboard-modal-ok').click(function(e){
				autoRefreshDashboard();
			});
			
			// Header logo 클릭시 발생하는 이벤트
			$('#dashboard-logo').click(function(e){
				clearAutoRefreshTimer();
				
				header.loadMainContent();
				if($('.listcontainer_open').parent().hasClass("none"))
					$('.listcontainer_open').click();
				
				$('.systemSub').addClass('none');
				$('.toolSub').addClass('none');
				$('.LocationNavi').removeClass('none');		
				
				
			});
		}
	},
	
	// 자동 새로고침 시작
	autoRefreshDashboard : function()
	{
		with(this)
		{
			if (0 != refreshIntervalSeconds)
			{
				autoRefreshTimer = setInterval(function()
				{
					loadDashBoardHeader();					
				}, refreshIntervalSeconds * 1000);
			}
		}
	},
	// 자동새로고침 시간 설정
	updateRefreshDate : function()
	{
		var today = new Date();
		var date = today.getFullYear()+'-'+(today.getMonth()+1)+'-'+today.getDate();
		var time = today.getHours() + ":" + today.getMinutes() + ":" + today.getSeconds();
		var dateTime = 'Last Updated : '+date+' '+time;
		$('#dashboard-refresh-timestamp').text(dateTime);
	},
	
	// 자동새로고침 초기화
	clearAutoRefreshTimer : function() 
	{
		with (this)
		{
			if (null != this.autoRefreshTimer)
			{
				clearInterval(this.autoRefreshTimer);
				this.autoRefreshTimer = null;
			}
		}
	}
});