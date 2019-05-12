var AdcSdsDashboardContent = Class.create({
	initialize : function() 
	{
		this.ajaxManager = new FlowitAjax();
		this.owner = undefined;
		
		this.AllAdcTrafficChartObj = undefined;
		this.VsTOP5ConnectionChartObj = undefined;
		this.FaultMonitoringChartObj = undefined;
		this.AdcChangeHistoryChartObj = undefined;
		
		this.AllAdcTrafficChartFlag = false;
		this.VsTOP5ConnectionChartFlag = false;
		this.FaultMonitoringChartFlag = false;
		this.AdcChangeHistoryChartFlag = false;
	},	
	// 장애 모니터링 현황 Chart HTML
	loadFaultMonitoringChartContent : function(params) 
	{
		with (this) 
		{
			_self = this;
			ajaxManager.runHtml({
				url			: '/dashboard/adcMon/loadFaultMonitoringChartContent.action',
				target		: '.faultmonitoring_chart_area',
				data		: params,
				successFn	: function(data) 
				{
					applyContentEvents();					
					loadAdcTrafficChartContent();
				}
			});
		}
	},
	// 전체 ADC 트래픽 현황 Chart HTML
	loadAdcTrafficChartContent : function(params) 
	{
		with (this) 
		{
			_self = this;
			ajaxManager.runHtml({
				url			: '/dashboard/adcMon/loadAdcTrafficChartContent.action',
				target		: '.allAdcMonitoring_chart_area',
				data		: params,
				successFn	: function(data) 
				{
					applyContentEvents();					
					loadHistoryMonitoringChartContent();
				}
			});
		}
	},
	// ADC 설정 변경 현황 Chart HTML
	loadHistoryMonitoringChartContent : function(params) 
	{
		with (this) 
		{
			_self = this;
			ajaxManager.runHtml({
				url			: '/dashboard/adcMon/loadHistoryMonitoringChartContent.action',
				target		: '.historymonitoring_chart_area',
				data		: params,
				successFn	: function(data) 
				{
					applyContentEvents();					
					loadTop5VsMonitoringChartContent();
				}
			});
		}
	},
	// Top5 Virtual Server Connection Chart HTML
	loadTop5VsMonitoringChartContent : function(params) 
	{
		with (this) 
		{	
			_self = this;
			ajaxManager.runHtml({
				url			: '/dashboard/adcMon/loadTop5VsMonitoringChartContent.action',
				target		: '.top5VsMonitoring_chart_area',
				data		: params,
				successFn	: function(data) 
				{
					applyContentEvents();
					loadFaultMonitoringGridContent();					
				}
			});
		}
	},
	// 장애 모니터링 현황 Grid HTML
	loadFaultMonitoringGridContent : function(params) 
	{
		with (this) 
		{
			_self = this;
			ajaxManager.runHtml({
				url			: '/dashboard/adcMon/loadFaultMonitoringGridContent.action',
				target		: '.faultmonitoring_grid_area',
				data		: params,
				successFn	: function(data) 
				{
					applyContentEvents();					
					loadAdcTrafficGridContent();
				}
			});
		}
	},	
	// 전체 ADC 트래픽 현황 Grid HTML
	loadAdcTrafficGridContent : function(params) 
	{
		with (this) 
		{
			_self = this;
			ajaxManager.runHtml({
				url			: '/dashboard/adcMon/loadAdcTrafficGridContent.action',
				target		: '.allAdcMonitoring_grid_area',
				data		: params,
				successFn	: function(data) 
				{
					applyContentEvents();					
					loadHistoryMonitoringGridContent();
				}
			});
		}
	},	
	// ADC 설정 변경 현황 Grid HTML
	loadHistoryMonitoringGridContent : function(params) 
	{
		with (this) 
		{
			_self = this;
			ajaxManager.runHtml({
				url			: '/dashboard/adcMon/loadHistoryMonitoringGridContent.action',
				target		: '.historymonitoring_grid_area',
				data		: params,
				successFn	: function(data) 
				{
					applyContentEvents();					
					loadTop5VsMonitoringGridContent();
				}
			});
		}
	},	
	// Top5 Virtual Server Connection Grid HTML
	loadTop5VsMonitoringGridContent : function(params) 
	{
		with (this) 
		{	
			_self = this;
			ajaxManager.runHtml({
				url			: '/dashboard/adcMon/loadTop5VsMonitoringGridContent.action',
				target		: '.top5VsMonitoring_grid_area',
				data		: params,
				successFn	: function(data) 
				{
					applyContentEvents();
					loadAdcSdsTrafficInfo();
					_self.owner.resetRefreshTimer();
				}
			});
		}
	},
	// ADC 모니터링 전체 Chart Data load Json
	loadAdcSdsTrafficInfo : function(params)
	{
		with (this)
		{
			ajaxManager.runJson({
				url			: '/dashboard/adcMon/loadAdcSdsTrafficInfo.action',
				data		: params,
				successFn	: function(data) 
				{
					GenerateAllAdcTrafficChartData(data);
					GenerateVsTOP5ConnectionChartData(data);
					GenerateFaultMonitoringChartData(data);
					GenerateAdcChangeHistoryChartData(data);				
				}
			});
		}
	},	
	applyContentEvents : function() 
	{
		with (this) 
		{		
			initTable([ "#table3 tbody tr", "#table6 tbody tr" ], [ 0 ], [ -1 ]);
			initTable([ "#table4 tbody tr" ], [  ], [ -1 ]);
			initTable([ "#table5 tbody tr" ], [ 0 ], [ -1 ]);
			initTable([ "#table7 tbody tr" ], [ 0], [ -1 ]);			
		}
	},			
		
	GenerateAllAdcTrafficChartData : function(data)
    {
		with(this)
		{
			var chartData = [];
			var chartDataList = data.dashTotalAdcTrafficGraph;
			if (chartDataList.length > 0 && chartDataList != null)
			{
				 for ( var i = 0; i < chartDataList.length; i++)
				 {
					 if (i == 0)
					 {
						 if (data.startTime < chartDataList[0].occurTime)
						 {
							 var startTime = parseDateTime(data.startTime);
							 chartData.push({occurredTime:startTime});
						 }
					 }
						var column = chartDataList[i];
						if (column)
						{
							var occurTime = column.occurTime;
							var date = parseDateTime(occurTime);
							var firstValue = column.value;
		
							var dataObject =
							{
								occurredTime : date,
								firstValue : firstValue
							};
							// add object to dataProvider array
							chartData.push(dataObject);					
						}
						if (i == (chartDataList.length - 1))
						{
							if (data.endTime > chartDataList[chartDataList.length - 1].occurTime)
							{
								var endTime = parseDateTime(data.endTime);
								chartData.push({occurredTime:endTime});								 
							}
						}
				 }
			}
			else
			{
					var startTime = parseDateTime(data.startTime);
					var endTime = parseDateTime(data.endTime);
					var dataObject =
					{
						occurredTime : startTime
					};
					chartData.push(dataObject);
					
					var dataObject =
						{
							occurredTime : endTime
						};
					chartData.push(dataObject);
			}
			var min = 0;
			var max = null;
			var linecolor = "#1C50B6";
			var chartname = "AllAdcTrafficChart";
			if (AllAdcTrafficChartFlag == false)
			{
				AllAdcTrafficChartObj = obchart.OBOneValueChartViewer(chartData, min, max, linecolor, chartname);
				AllAdcTrafficChartFlag = true;
			}
			else
			{
				obchart.OBAllChartUpdate(AllAdcTrafficChartObj, chartData);
			}
		}
	},	
	GenerateVsTOP5ConnectionChartData : function(data)
	{
		with(this)
		{
			var chartData = [];
			var chartDataList =  data.dashTop5VSConnecionList;
			if (chartDataList.length > 0 && chartDataList != null)
			{
				for ( var i = 0; i < chartDataList.length; i++)
				{
					if (i == 0)
					 {
						 if (data.startTime < chartDataList[0].occurTime)
						 {
							 var startTime = parseDateTime(data.startTime);
							 chartData.push({occurredTime:startTime});
						 }
					 }
					var column = chartDataList[i];
					if (column)
					{
						var occurTime = column.occurTime;
						var date = parseDateTime(occurTime);
						
						if (column.firstName != null && column.firstName.length > 0)
						{
						var firstValue = column.firstValue;
						var firstName = column.firstName;
						}
						if (column.secondName != null && column.secondName.length > 0)
						{
			        	var secondValue = column.secondValue;
			        	var secondName = column.secondName;
						}
						if (column.thirdName != null && column.thirdName.length > 0)
						{
						var thirdValue = column.thirdValue;
						var thirdName = column.thirdName;
						}
						if (column.fourthName != null && column.fourthName.length > 0)
						{
						var fourthValue = column.fourthValue;
						var fourthName = column.fourthName;
						}
						if (column.fifthName != null && column.fifthName.length > 0)
						{
						var fifthValue = column.fifthValue;
						var fifthName = column.fifthName;
						}
						
						var dataObject =
						{
							occurredTime : date,
							firstValue : firstValue,
							secondValue : secondValue,
							thirdValue : thirdValue,
							fourthValue : fourthValue,
							fifthValue : fifthValue,
							
							firstName : firstName,
				        	secondName : secondName,
							thirdName : thirdName,
							fourthName : fourthName,
							fifthName : fifthName
						};
						// add object to dataProvider array
						chartData.push(dataObject);
					}
					if (i == (chartDataList.length - 1))
					{
						if (data.endTime > chartDataList[chartDataList.length - 1].occurTime)
						{
							var endTime = parseDateTime(data.endTime);
							chartData.push({occurredTime:endTime});								 
						}
					}
				}
			 }
			 else
			 {
				var startTime = parseDateTime(data.startTime);
				var endTime = parseDateTime(data.endTime);
				var dataObject =
				{
					occurredTime : startTime
				};
				chartData.push(dataObject);
				var dataObject =
				{
					occurredTime : endTime
				};
				chartData.push(dataObject);				 
			}
			var min = 0;
			var max = null;
			var linecolor1 = "#ffa010";
			var linecolor2 = "#72b419";
			var linecolor3 = "#8080c0";
			var linecolor4 = "#40b0d8";
			var linecolor5 = "#508cff";
			var chartname = "VsTOP5ConnectionChart";
			if (VsTOP5ConnectionChartFlag  == false)
			{
				VsTOP5ConnectionChartObj = obchart.OBVsTOP5ConnectionChartViewer(chartData, min, max, linecolor1, linecolor2, linecolor3, linecolor4, linecolor5, chartname);
				VsTOP5ConnectionChartFlag = true;
			}
			else
			{
				obchart.OBAllChartUpdate(VsTOP5ConnectionChartObj, chartData);
			}
		}
	},		
	GenerateFaultMonitoringChartData : function(data)
    {
		with(this)
		{
			var chartData = [];
			var chartDataList = data.dashFaultStatus.changeHistory;
			if (chartDataList.length > 0 && chartDataList != null)
			{
				 for ( var i = 0; i < chartDataList.length; i++)
				 {
					 if (i == 0)
					 {
						 if (data.startTime < chartDataList[0].occurTime)
						 {
							 var startTime = parseDateTime(data.startTime);
							 chartData.push({occurredTime:startTime});
						 }
					 }
						var column = chartDataList[i];
						if (column)
						{
							var occurTime = column.occurTime;
							var date = parseDateTime(occurTime);
							var firstValue = column.value;
		
							var dataObject =
							{
								occurredTime : date,
								firstValue : firstValue
							};
							// add object to dataProvider array
							chartData.push(dataObject);					
						}
						if (i == (chartDataList.length - 1))
						{
							if (data.endTime > chartDataList[chartDataList.length - 1].occurTime)
							{
								var endTime = parseDateTime(data.endTime);
								chartData.push({occurredTime:endTime});								 
							}
						}
				 }
			}
			else
			{
				var startTime = parseDateTime(data.startTime);
				var endTime = parseDateTime(data.endTime);
				var dataObject =
				{
					occurredTime : startTime
				};
				chartData.push(dataObject);
				
				var dataObject =
				{
					occurredTime : endTime
				};
				chartData.push(dataObject);
				}
			var min = 0;
			var max = null;
			var linecolor = "#1C50B6";
			var chartname = "FaultMonitoringChart";
			if (FaultMonitoringChartFlag == false)
			{
				FaultMonitoringChartObj = obchart.OBOneValueSimpleChartViewer(chartData, min, max, linecolor, chartname);
				FaultMonitoringChartFlag = true;
			}
			else
			{
				obchart.OBAllChartUpdate(FaultMonitoringChartObj, chartData);
			}
		}
	},		
	GenerateAdcChangeHistoryChartData : function(data)
    {
		with(this)
		{
			var chartData = [];
			var chartDataList = data.dashSlbChangeStatus.changeHistory;
			if (chartDataList.length > 0 && chartDataList != null)
			{
				 for ( var i = 0; i < chartDataList.length; i++)
				 {
					 if (i == 0)
					 {
						 if (data.startTime < chartDataList[0].occurTime)
						 {
							 var startTime = parseDateTime(data.startTime);
							 chartData.push({occurredTime:startTime});
						 }
					 }
						var column = chartDataList[i];
						if (column)
						{
							var occurTime = column.occurTime;
							var date = parseDateTime(occurTime);
							var firstValue = column.value;
		
							var dataObject =
							{
								occurredTime : date,
								firstValue : firstValue
							};
							// add object to dataProvider array
							chartData.push(dataObject);					
						}
						if (i == (chartDataList.length - 1))
						{
							if (data.endTime > chartDataList[chartDataList.length - 1].occurTime)
							{
								var endTime = parseDateTime(data.endTime);
								chartData.push({occurredTime:endTime});								 
							}
						}
				 }
			}
			else
			{
				var startTime = parseDateTime(data.startTime);
				var endTime = parseDateTime(data.endTime);
				var dataObject =
				{
					occurredTime : startTime
				};
				chartData.push(dataObject);
				
				var dataObject =
				{
					occurredTime : endTime
				};
				chartData.push(dataObject);
			}
			var min = 0;
			var max = null;
			var linecolor = "#1C50B6";
			var chartname = "AdcChangeHistoryChart";
			if (AdcChangeHistoryChartFlag == false)
			{
				AdcChangeHistoryChartObj = obchart.OBOneValueSimpleChartViewer(chartData, min, max, linecolor, chartname);
				AdcChangeHistoryChartFlag = true;
			}
			else
			{
				obchart.OBAllChartUpdate(AdcChangeHistoryChartObj, chartData);
			}
		}
	}
});

//var AlertWnd =  Class.create({
//	initialize : function() 
//	{
//		this.settings = undefined;
//		this.alertInfo = undefined;
//		this.onCloseFn = undefined;
//	},
//	
//	popUp : function(onCloseFn) 
//	{
//		this.onCloseFn = onCloseFn;
//		this._retrieveSettingsAndShowAlert();
//	},
//	
//	_registerEvents : function() 
//	{
//		with (this)
//		{
//			$('.cloneDiv .okLnk').click(function(e) 
//			{
//				e.preventDefault();
//				_checkAlert();
////				_showNextAlertOrClose(onCloseFn);
//			});
//			
//			$('.cloneDiv .closeWndLnk').click(function(e) 
//			{
//				e.preventDefault();
////				_showNextAlertOrClose(onCloseFn);
//			});
//		}
//	},
//	
//	_retrieveSettingsAndShowAlert : function() 
//	{
//		with (this) 
//		{
//			ajaxManager.runJson({
//				url : "../../alert/retrieveSettings.action",
//				data : 
//				{},
//				successFn : function(data) 
//				{
//					if (!data.isSuccessful) 
//					{
//						alert(data.message);
//						return;
//					}
//
//					//FlowitUtil.log("settings: %o", data.settings);
//					settings = data.settings;
//					_showFirstAlert();
//				}
//			});
//		}
//	},
//	
//	_showFirstAlert : function() 
//	{
//		with (this) 
//		{
//			if (!settings.isAlertOn)
//			{
//				return;
//			}
//			
//			ajaxManager.runJson({
//				url : "../../alert/retrieveAlert.action",
//				data : 
//				{},
//				successFn : function(data) 
//				{
//					if (!data.isSuccessful) 
//					{
//						alert(data.message);
//						return;
//					}
//
//					//FlowitUtil.log("alertInfo: %o", data.alert);
//					alertInfo = data.alert;
//					if (alertInfo) 
//					{
//						_showPopupFrame();
//						if (settings.isBeepOn)
//							_beep();
//						
//						_showAlert();
//					} 
//					else if (onCloseFn) 
//					{
//						onCloseFn();
//					}
//				}
//			});
//		}
//	},
//	
//	_showPopupFrame : function() 
//	{
//		with (this) 
//		{
//			var $pop = null;
//			// 팝업창 열기
//			$pop = $("#dash_action").clone();
//			$pop.addClass('cloneDiv');
//			$pop.css('width', "494px");
//			$('body').append("<div class='popup_type1'></div>");
//			$('body').append($pop);
//			$pop.show();// .draggable();
//			// 팝업창 정중앙 위치
//			$pop.css({
//			    'left' : ($(window).width() - $pop.width()) / 2,
//			    'top' : ($(window).height() - $pop.height()) / 2
//			});
//			$('.popup_type1').height($('body').height());
//			_registerEvents();
//		}
//	},
//	
//	_showAlert : function() 
//	{
//		with(this) 
//		{
//			$('.secondMsg, .3rdMsg').addClass('none');
//			$('.cloneDiv .faultTitle').text(alertInfo.title);
//			$('.cloneDiv .faultTime').text(dateFormat(alertInfo.occurTime, "UTC:yyyy-mm-dd HH:MM:ss"));
//			$('.cloneDiv .adcName').text(alertInfo.adcName);
//			if (alertInfo.virtualSvrName || alertInfo.virtualSvrIp) {
//				$('.secondMsg').removeClass('none').html('Virtual Server [<span class="color_e50c0c">' + alertInfo.virtualSvrName + ', ' + alertInfo.virtualSvrIp + '</span>]');
//			} else if (alertInfo.linkNo || alertInfo.linkNo === 0) {
//				$('.secondMsg').removeClass('none').html('Link [<span class="color_e50c0c">' + alertInfo.linkNo + '</span>]');
//			}
//			
//			if (alertInfo.memberIp) {
//				$('.3rdMsg').removeClass('none').html('GROUP Member [<span class="color_e50c0c">' + alertInfo.memberIp + '</span>]');
//			}
//			
//			$('.cloneDiv .faultMsg').text(alertInfo.message);
//			$('.cloneDiv .remainingAlertCount').text(alertInfo.remainingAlertCount + 1);
//		}
//	},
//	
//	_beep : function() 
//	{
//		$('.cloneDiv .faultBeep').append('<embed type="application/x-mplayer2" height="0" width="0" src="../../audio/73797_alarmclock.mp3" autostart="1"></embed>');
//	},
//	
//	_checkAlert : function() 
//	{
//		with (this) 
//		{
//			if (!alertInfo)
//				return;
//			
//			ajaxManager.runJson({
//				url : "../../alert/checkAlert.action",
//				data : 
//				{
//					"alert.index" : alertInfo.index
//				},
//				successFn : function(data) 
//				{
//					if (!data.isSuccessful)
//						alert(data.message);
//				},
//				completeFn : function() 
//				{
//					_closePopupFrame();
//				}
//			});
//		}
//	},
//	
//	_closePopupFrame : function() 
//	{
//		with (this) 
//		{
//			$('.popup_type1').remove();
//			$('.cloneDiv').remove();
//			if (onCloseFn)
//			{
//				onCloseFn();
//			}
//		}
//	}
//});