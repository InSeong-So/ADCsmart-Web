var AdcPopup = Class.create({
	initialize : function() 
	{
		this.ajaxManager = new FlowitAjax();
		this.owner = undefined;		
		this.refreshTimer = undefined;
		this.refreshIntervalSeconds = 30;
		
		this.selectedCategory=undefined; //0: 전체, 1: 그룹, 2: 개별 adc, 3:virtual server, 4:virtual service
		this.selectedIndex=undefined;
		this.selectedPopupContents=undefined;
		
		this.searchStartTime = undefined;
		this.searchEndTime = undefined;
		
	},
	loadAdcPopupContents : function(curCategory,curIndex,popUpcontents, refreshes)
	{
		this.selectedCategory=curCategory; //0: 전체, 1: 그룹, 2: 개별 adc, 3:virtual server, 4:virtual service
		this.selectedIndex=curIndex;
		this.selectedPopupContents = popUpcontents;
		
		with (this) 
		{
			var params = 
			{
					"curCategory"	: this.selectedCategory,
					"curIndex"	: this.selectedIndex,
					"popUpcontents" : this.selectedPopupContents,
					"refrehses"	: refreshes					
			};
			params['startTime'] = $('input[name="startTime"]').val();
			params['endTime'] = $('input[name="endTime"]').val();
			
			_self = this;
			ajaxManager.runHtml({
				url			: '/dashboard/adcMon/loadAdcPopupContents.action',
				data		: params,
				target		: '.pop_contents',
				successFn	: function(data) 
				{			
					applyContentCss();
					popUpEventContent();					
					loadPopupGraphInfo(refreshes);									
				}
			});		
		}
	},
	applyContentCss : function() 
	{
		with (this)
		{
			$('.gragh_sessnInfo').css("height", "+=153");
		}
	},
	popUpEventContent : function()
	{		
		with (this)
		{
			if (!searchStartTime)
			{
				searchStartTime = new Date();
			}
			if (!searchEndTime)
			{
				searchEndTime = new Date();
			}
			$('input[name="startTime"]').datepicker({
				maxDate: "0",
				dateFormat : "yy-mm-dd",
				showOn: "button",
				buttonImage: "/imgs/report/btn_calendar.gif",
				buttonImageOnly: true,
				defaultDate: searchStartTime
			});
			$('input[name="endTime"]').datepicker({
				maxDate: "0",
				dateFormat : "yy-mm-dd",
				showOn: "button",
				buttonImage: "/imgs/report/btn_calendar.gif",
				buttonImageOnly: true,
				defaultDate: searchEndTime
			});
			$('#startTime').val($.datepicker.formatDate('yy-mm-dd',searchStartTime));
			$('#endTime').val($.datepicker.formatDate('yy-mm-dd',searchEndTime));
			
			$('#refresh').click(function(event) {
				event.preventDefault();
				loadPopupGraphInfo(true);
			});
			
			if(this.selectedPopupContents == 'adcConnection')
			{
				$('#adcThroughput').hide();
			}
			else
			{
				$('#adcConnection').hide();
			}			
		}
	},	
	loadPopupGraphInfo : function(refreshes)
	{
		with (this) 
		{		
			var params =
			{
					"curCategory"	: this.selectedCategory,
					"curIndex"	: this.selectedIndex,
					"popUpcontents" : this.selectedPopupContents,
					"refreshes" : refreshes
			};
			params['startTime'] = $('input[name="startTime"]').val();
			params['endTime'] = $('input[name="endTime"]').val();
			
			ajaxManager.runJson({
				url			: '/dashboard/adcMon/loadAdcPopupInfo.action',
				data		: params,
				successFn	: function(data) 
				{
					UpdateNotice(data);
					GenerateChart(data);
				}
			});		
		}
	},
	UpdateNotice : function(data)
	{
		with(this)
		{	
			startDate = parseDateTimeString(data.startTime);
			endDate = parseDateTimeString(data.endTime);		
						
			var $tbody = $('.pop_contents .PopupNotice').filter(':last');
			$tbody.empty();
			var html = '';
			
			html += startDate + '&nbsp~&nbsp' + endDate;			
			$tbody.html(html);
		}
	},
	GenerateChart : function(data)
	{
		if (data.adcConnectionPopupGraph != null && data.adcThroughputPopupGraph == null)
		{
			GeneratePopupGroupConnectionChartData(data);
		}
		else if (data.adcThroughputPopupGraph != null && data.adcConnectionPopupGraph ==null)
		{
			GeneratepopupGroupThroughputChartData(data);
		}		
		// ADC 모니터링 팝업 CHART ( Group Connection )
		function GeneratePopupGroupConnectionChartData(data)
        {
			var maxData = [];
			var chartData = [];
			var chartDataList = data.adcConnectionPopupGraph;
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
						var maxdataObject = 
						{
							value : firstValue
						};
							// add object to dataProvider array
						chartData.push(dataObject);
						maxData.push(maxdataObject);
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
			var maxPos = Math.max.apply(null,jQuery.map(maxData, function(o){return o.value;}));
			var min = 0;
			var max = null;
			var linecolor = "#1C50B6";
			var chartname = "GeneratePopupGroupConnectionChartData";
			var chartunit = "cps";
			obchart.OBOneValueChartViewer(chartData, min, max, linecolor, chartname, chartunit, maxPos);
        }		
		// ADC 모니터링 팝업 CHART ( Group Throughput )
		function GeneratepopupGroupThroughputChartData(data)
        {
			var maxData = [];
			var chartData = [];
			var chartDataList = data.adcThroughputPopupGraph;
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
							var maxdataObject = 
							{
								value : firstValue
							};
							// add object to dataProvider array
							chartData.push(dataObject);
							maxData.push(maxdataObject);
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
			var maxPos = Math.max.apply(null,jQuery.map(maxData, function(o){return o.value;}));
			var min = 0;
			var max = null;
			var linecolor = "#1C50B6";
			var chartname = "GeneratePopupGroupThroughputChartData";
			var chartunit = "bps";
			obchart.OBOneValueChartViewer(chartData, min, max, linecolor, chartname, chartunit, maxPos);
        }		
	}		
});