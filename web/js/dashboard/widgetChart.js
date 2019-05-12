var WidgetChart = Class.create({
	initialize : function() 
	{
		this.ajaxManager = new FlowitAjax();
		this.OBajaxManager = new OBAjax();
	},	
	// 2. 장애 모니터링 현황
	GenerateFaultMonitoringChart : function(data, chartName, chartRefreshKey, monitoringPeriod)
	{
		var chartData = [];
		var chartDataList = [];
		if (data.dashFaultStatus != null)
		{
			chartDataList = data.dashFaultStatus.changeHistory;
		}		
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
		var chartOption =
		{
			 min : 0,
			 max : null,
			 linecolor : "#cb8c8",
			 chartname : chartName,
			 axistitle : VAR_COMMON_AFEW,
			 maxPos : null,
			 cursorColor : "#0f47c7",
			 interval : monitoringPeriod
		};
		if (chartRefreshKey == 1)
		{
			var chartobj = obchart.OBAreaChartViewer(chartData, chartOption);
			return chartobj;
		}
		else if (chartRefreshKey == 2 || chartRefreshKey == 3)
		{
			obchart.OBAllChartUpdate(chartName, chartData);
			return;
		}			
	},
	// 4. 설정 변경 현황
	GenerateAdcChangeHistoryChart : function(data, chartName, chartRefreshKey)
	{
		var chartData = [];
		var chartDataList = [];
		if (data.dashSlbChangeStatus != null)
		{
			chartDataList = data.dashSlbChangeStatus.changeHistory;
		}
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
					/*if (i == (chartDataList.length - 1))
					{
						if (data.endTime > chartDataList[chartDataList.length - 1].occurTime)
						{
							var endTime = parseDateTime(data.endTime);
							chartData.push({occurredTime:endTime});								 
						}
					}*/
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
		var chartOption =
		{
			 min : 0,
			 max : null,
			 linecolor : "#6cb8c8",
			 chartname : chartName,
			 axistitle : VAR_COMMON_AFEW,
			 maxPos : null,
			 cursorColor : "#0f47c7"
		};
		if (chartRefreshKey == 1)
		{
			var chartobj = obchart.OBSettingHistoryChartViewer(chartData, chartOption);		
			return chartobj;
		}
		else if (chartRefreshKey == 2 || chartRefreshKey == 3)
		{
			obchart.OBAllChartUpdate(chartName, chartData);
			return;
		}
	},
	// 8. ADC Concurrent session 추이 Chart (ADC Select 용)
	GenerateAdcConnectionChartData : function(data, chartName, chartRefreshKey, monitoringPeriod)
	{
		with(this)
		{
			var chartData = [];
			var chartDataList = [];
			
			if (data.widgetTarget.category < 3)
			{
				if (data.adcConnectionInfo != null)
				{
					chartDataList = data.adcConnectionInfo.adcConnectionDataList;
				}
				if (chartDataList.length > 0 && chartDataList != null)
				{
					 for ( var i = 0; i < chartDataList.length; i++)
					 {
						 if (i == 0)
						 {
							 if (data.startTime < chartDataList[0].occurredTime)
							 {
								 var startTime = parseDateTime(data.startTime);
								 chartData.push({occurredTime:startTime});
							 }
						 }
						var column = chartDataList[i];
						if (column)
						{
							if (column.connections < 0 )
							{				
							}
							else
							{
								var occurTime = column.occurredTime;
								var date = parseDateTime(occurTime);
								var firstValue = column.connections;
								var dataObject =
								{
									occurredTime : date,
									firstValue : firstValue
								};
								// add object to dataProvider array
								chartData.push(dataObject);
							}
						}
						if (i == (chartDataList.length - 1))
						{
							if (data.endTime > chartDataList[chartDataList.length - 1].occurredTime)
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
				var chartOption =
				{
					 min : 0,
					 max : null,
					 linecolor : "#6cb8c8",
					 chartname : chartName,
					 axistitle : VAR_COMMON_AFEW,
					 maxPos : null,
					 cursorColor : "#0f47c7",
					 title : "Concurrent session",
					 interval : monitoringPeriod
				};
				if (chartRefreshKey == 1)
				{
					var chartobj = obchart.OBAreaChartViewer(chartData, chartOption);
					return chartobj;
				}
				else if (chartRefreshKey == 2 || chartRefreshKey == 3)
				{
					obchart.OBAllChartUpdate(chartName, chartData, chartOption);
					return;
				}
			}
			// ADC 이하 VS 나 VService 를 선택한 Concurrent Session Data Chart Insert
			else
			{
				if (data.vsConcurrentInfo != null)
				{
					chartDataList = data.vsConcurrentInfo;
				}
				if (chartDataList.length > 0 && chartDataList != null)
				{
					 for ( var i = 0; i < chartDataList.length; i++)
					 {
						 if (i == 0)
						 {
							 if (data.startTime < chartDataList[0].connCurrValue.occurTime)
							 {
								 var startTime = parseDateTime(data.startTime);
								 chartData.push({occurredTime:startTime});
							 }
						 }
						var column = chartDataList[i];
						if (column)
						{
							if (column.bpsValue.value < 0 )
							{				
							}
							else
							{
								var occurTime = column.connCurrValue.occurTime;
								var date = parseDateTime(occurTime);
								var firstValue = column.connCurrValue.value;
								var dataObject =
								{
									occurredTime : date,
									firstValue : firstValue
								};
								// add object to dataProvider array
								chartData.push(dataObject);
							}
						}
						if (i == (chartDataList.length - 1))
						{
							if (data.endTime > chartDataList[chartDataList.length - 1].connCurrValue.occurTime)
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
				var chartOption =
				{
					 min : 0,
					 max : null,
					 linecolor : "#6cb8c8",
					 chartname : chartName,
					 axistitle : VAR_COMMON_AFEW,
					 maxPos : null,
					 cursorColor : "#0f47c7",
					 title : "Concurrent session",
					 interval : monitoringPeriod
				};
				if (chartRefreshKey == 1)
				{
					var chartobj = obchart.OBAreaChartViewer(chartData, chartOption);
					return chartobj;
				}
				else if (chartRefreshKey == 2 || chartRefreshKey == 3)
				{
					obchart.OBAllChartUpdate(chartName, chartData, chartOption);
					return;
				}
			}			
		}
	},
	// 8-2. ServiceGroupConcurrent session 추이 Chart (Multi Value 용)
	GenerateServiceGroupConnectionChartData : function(data, chartName, chartRefreshKey, monitoringPeriod)
	{
		with(this)
		{
			var chartData = [];
			var chartDataList = [];
			if (data.serviceGroupConcurrentInfo != null)
			{
				chartDataList = data.serviceGroupConcurrentInfo;
			}
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
						var Value1 = undefined;				    
						var Value2 = undefined;
						var Value3 = undefined;
						var Value4 = undefined;
						var Value5 = undefined;
						var Value6 = undefined;
						var Value7 = undefined;
						var Value8 = undefined;
						var Value9 = undefined;
						var Value10 = undefined;
						var ValueName1 = column.name1;
						var ValueName2 = column.name2;
						var ValueName3 = column.name3;
						var ValueName4 = column.name4;
						var ValueName5 = column.name5;
						var ValueName6 = column.name6;
						var ValueName7 = column.name7;
						var ValueName8 = column.name8;
						var ValueName9 = column.name9;
						var ValueName10 = column.name10;						
						
						
						if (column.value1 > -1 && column.value1 != null)
						{
							Value1 = column.value1;
						}						
						if (column.value2 > -1 && column.value2 != null)
						{
							Value2 = column.value2;
						}
						if (column.value3 > -1 && column.value3 != null)
						{
							Value3 = column.value3;
						}
						if (column.value4 > -1 && column.value4 != null)
						{
							Value4 = column.value4;
						}
						if (column.value5 > -1 && column.value5 != null)
						{
							Value5 = column.value5;
						}
						if (column.value6 > -1 && column.value6 != null)
						{
							Value6 = column.value6;
						}
						if (column.value7 > -1 && column.value7 != null)
						{
							Value7 = column.value7;
						}
						if (column.value8 > -1 && column.value8 != null)
						{
							Value8 = column.value8;
						}
						if (column.value9 > -1 && column.value9 != null)
						{
							Value9 = column.value9;
						}
						if (column.value10 > -1 && column.value10 != null)
						{
							Value10 = column.value10;
						}
						
						var	dataObject =
							{
								occurredTime : date,
								firstValue : Value1,							
								secondValue : Value2,
								thirdValue : Value3,
								fourthValue : Value4,
								fifthValue : Value5,
								sixthValue : Value6,
								seventhValue : Value7,
								eighthValue : Value8,
								ninthValue : Value9,
								tenthValue : Value10,							
								firstName : ValueName1,
								secondName : ValueName2,
								thirdName : ValueName3,
								fourthName : ValueName4,
								fifthName : ValueName5,
								sixthName : ValueName6,
								seventhName : ValueName7,
								eighthName : ValueName8,
								ninthName : ValueName9,
								tenthName : ValueName10
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
			var chartOption =
			{
				 min : 0,
				 max : null,
				 linecolor1 : "#6cb8c8",
				 linecolor2 : "#fbc51a",
				 linecolor3 : "#d65f3d",
				 linecolor4 : "#976e96",
				 linecolor5 : "#fb8e33",
				 linecolor6 : "#9cc239",
				 linecolor7 : "#998c57",
				 linecolor8 : "#d987ad",
				 linecolor9 : "#557aa4",
				 linecolor10 : "#d8aa3a",
				 chartname : chartName,
				 axistitle : VAR_COMMON_AFEW,
				 maxPos : null,
				 cursorColor : "#0f47c7",
				 interval : monitoringPeriod
			};
			if (chartRefreshKey == 1)
			{
				var chartobj = obchart.OBDashboardGroupChartViewer(chartData, chartOption);
				return chartobj;
			}
			else if (chartRefreshKey == 2 || chartRefreshKey == 3)
			{
				obchart.OBAllChartUpdate(chartName, chartData, chartOption);
				return;
			}
		
						
		}
	},
	// 8-3. ServiceGroupConcurrent session 추이 Chart (Multi Value 용 - SUM)
	GenerateServiceGroupConnectionSumChartData : function(data, chartName, chartRefreshKey, monitoringPeriod)
	{
		with(this)
		{
			var chartData = [];
			var chartDataList = [];
			if (data.serviceGroupConcurrentInfo != null)
			{
				chartDataList = data.serviceGroupConcurrentInfo;
			}
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
						var Value1 = undefined;				    
						var Value2 = undefined;
						var Value3 = undefined;
						var Value4 = undefined;
						var Value5 = undefined;
						var Value6 = undefined;
						var Value7 = undefined;
						var Value8 = undefined;
						var Value9 = undefined;
						var Value10 = undefined;
						var SumValue = 0;
						var ValueName1 = column.name1;
						var ValueName2 = column.name2;
						var ValueName3 = column.name3;
						var ValueName4 = column.name4;
						var ValueName5 = column.name5;
						var ValueName6 = column.name6;
						var ValueName7 = column.name7;
						var ValueName8 = column.name8;
						var ValueName9 = column.name9;
						var ValueName10 = column.name10;						
						
						
						if (column.value1 > -1 && column.value1 != null)
						{
							Value1 = column.value1;
							SumValue += Value1;
						}						
						if (column.value2 > -1 && column.value2 != null)
						{
							Value2 = column.value2;
							SumValue += Value2;
						}
						if (column.value3 > -1 && column.value3 != null)
						{
							Value3 = column.value3;
							SumValue += Value3;
						}
						if (column.value4 > -1 && column.value4 != null)
						{
							Value4 = column.value4;
							SumValue += Value4;
						}
						if (column.value5 > -1 && column.value5 != null)
						{
							Value5 = column.value5;
							SumValue += Value5;
						}
						if (column.value6 > -1 && column.value6 != null)
						{
							Value6 = column.value6;
							SumValue += Value6;
						}
						if (column.value7 > -1 && column.value7 != null)
						{
							Value7 = column.value7;
							SumValue += Value7;
						}
						if (column.value8 > -1 && column.value8 != null)
						{
							Value8 = column.value8;
							SumValue += Value8;
						}
						if (column.value9 > -1 && column.value9 != null)
						{
							Value9 = column.value9;
							SumValue += Value9;
						}
						if (column.value10 > -1 && column.value10 != null)
						{
							Value10 = column.value10;
							SumValue += Value10;
						}
						
						var	dataObject =
							{
								occurredTime : date,
								firstValue : Value1,							
								secondValue : Value2,
								thirdValue : Value3,
								fourthValue : Value4,
								fifthValue : Value5,
								sixthValue : Value6,
								seventhValue : Value7,
								eighthValue : Value8,
								ninthValue : Value9,
								tenthValue : Value10,							
								firstName : ValueName1,
								secondName : ValueName2,
								thirdName : ValueName3,
								fourthName : ValueName4,
								fifthName : ValueName5,
								sixthName : ValueName6,
								seventhName : ValueName7,
								eighthName : ValueName8,
								ninthName : ValueName9,
								tenthName : ValueName10,
								sumValue : SumValue
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
			var chartOption =
			{
				 min : 0,
				 max : null,
				 linecolor1 : "#6cb8c8",
				 linecolor2 : "#fbc51a",
				 linecolor3 : "#d65f3d",
				 linecolor4 : "#976e96",
				 linecolor5 : "#fb8e33",
				 linecolor6 : "#9cc239",
				 linecolor7 : "#998c57",
				 linecolor8 : "#d987ad",
				 linecolor9 : "#557aa4",
				 linecolor10 : "#d8aa3a",
				 linecolor11 : "gray",				 
				 chartname : chartName,
				 axistitle : VAR_COMMON_AFEW,
				 axistitle2 : VAR_COMMON_AFEW,
				 maxPos : null,
				 cursorColor : "#0f47c7",
				 interval : monitoringPeriod
			};
			if (chartRefreshKey == 1)
			{
				var chartobj = obchart.OBDashboardGroupSumChartViewer(chartData, chartOption);
				return chartobj;
			}
			else if (chartRefreshKey == 2 || chartRefreshKey == 3)
			{
				obchart.OBAllChartUpdate(chartName, chartData, chartOption);
				return;
			}
		
						
		}
	},
	//TODO
	// 9. ADC Throughtput 추이 Chart	(ADC Select 용)
	GeneratedcThroughtputChartData : function(data, chartName, chartRefreshKey, monitoringPeriod)
	{
		with(this)
		{
			var chartData = [];
			var chartDataList = [];
			
			if (data.widgetTarget.category < 3)
			{	
				if (data.adcThroughputInfo != null)
				{
					chartDataList = data.adcThroughputInfo.adcThroughputDataList;
				}
				if (chartDataList.length > 0 && chartDataList != null)
				{
					 for ( var i = 0; i < chartDataList.length; i++)
					 {
						 if (i == 0)
						 {
							 if (data.startTime < chartDataList[0].occurredTime)
							 {
								 var startTime = parseDateTime(data.startTime);
								 chartData.push({occurredTime:startTime});
							 }
						 }
						var column = chartDataList[i];
						if (column)
						{
							if (column.bps < 0 )
							{					
							}
							else
							{
								var occurTime = column.occurredTime;
								var date = parseDateTime(occurTime);
								var firstValue = column.bps;
								var dataObject =
								{
									occurredTime : date,
									firstValue : firstValue
								};						
							// add object to dataProvider array
							chartData.push(dataObject);
							}
						}
						if (i == (chartDataList.length - 1))
						{
							if (data.endTime > chartDataList[chartDataList.length - 1].occurredTime)
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
				var chartOption =
				{
					 min : 0,
					 max : null,
					 linecolor : "#6cb8c8",
					 chartname : chartName,
					 axistitle : "bps",
					 maxPos : null,
					 cursorColor : "#0f47c7",
					 title : "Throughtput",
					 interval : monitoringPeriod
				};
				if (chartRefreshKey == 1)
				{
					var chartobj = obchart.OBAreaChartViewer(chartData, chartOption);
					return chartobj; 
				}
				else if (chartRefreshKey == 2 || chartRefreshKey == 3)
				{
					obchart.OBAllChartUpdate(chartName, chartData, chartOption);
					return;
				}
			}
			// ADC 이하 VS 나 VService 를 선택한 Throughput Data Chart Insert
			else
			{
				if (data.vsThroughputInfo != null)
				{
					chartDataList = data.vsThroughputInfo;
				}
				if (chartDataList.length > 0 && chartDataList != null)
				{
					 for ( var i = 0; i < chartDataList.length; i++)
					 {
						 if (i == 0)
						 {
							 if (data.startTime < chartDataList[0].bpsValue.occurTime)
							 {
								 var startTime = parseDateTime(data.startTime);
								 chartData.push({occurredTime:startTime});
							 }
						 }
						var column = chartDataList[i].bpsValue;
						if (column)
						{
							if (column.bps < 0 )
							{					
							}
							else
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
						}
						if (i == (chartDataList.length - 1))
						{
							if (data.endTime > chartDataList[chartDataList.length - 1].bpsValue.occurTime)
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
				var chartOption =
				{
					 min : 0,
					 max : null,
					 linecolor : "#6cb8c8",
					 chartname : chartName,
					 axistitle : "bps",
					 maxPos : null,
					 cursorColor : "#0f47c7",
					 interval : monitoringPeriod
				};
				if (chartRefreshKey == 1)
				{
					var chartobj = obchart.OBAreaChartViewer(chartData, chartOption);
					return chartobj; 
				}
				else if (chartRefreshKey == 2 || chartRefreshKey == 3)
				{
					obchart.OBAllChartUpdate(chartName, chartData, chartOption);
					return;
				}
			}
		}
	},
	// 9-2. ADC Throughtput 추이 Chart	(Multi Value 용)
	GeneratedcServiceGroupThroughtputChartData : function(data, chartName, chartRefreshKey, monitoringPeriod)
	{	
		with(this)
		{
			var chartData = [];
			var chartDataList = [];
			if (data.serviceGroupThroughputInfo != null)
			{
				chartDataList = data.serviceGroupThroughputInfo;
			}
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
						var Value1 = undefined;				    
						var Value2 = undefined;
						var Value3 = undefined;
						var Value4 = undefined;
						var Value5 = undefined;
						var Value6 = undefined;
						var Value7 = undefined;
						var Value8 = undefined;
						var Value9 = undefined;
						var Value10 = undefined;
						var ValueName1 = column.name1;
						var ValueName2 = column.name2;
						var ValueName3 = column.name3;
						var ValueName4 = column.name4;
						var ValueName5 = column.name5;
						var ValueName6 = column.name6;
						var ValueName7 = column.name7;
						var ValueName8 = column.name8;
						var ValueName9 = column.name9;
						var ValueName10 = column.name10;						
						
						
						if (column.value1 > -1 && column.value1 != null)
						{
							Value1 = column.value1;
						}						
						if (column.value2 > -1 && column.value2 != null)
						{
							Value2 = column.value2;
						}
						if (column.value3 > -1 && column.value3 != null)
						{
							Value3 = column.value3;
						}
						if (column.value4 > -1 && column.value4 != null)
						{
							Value4 = column.value4;
						}
						if (column.value5 > -1 && column.value5 != null)
						{
							Value5 = column.value5;
						}
						if (column.value6 > -1 && column.value6 != null)
						{
							Value6 = column.value6;
						}
						if (column.value7 > -1 && column.value7 != null)
						{
							Value7 = column.value7;
						}
						if (column.value8 > -1 && column.value8 != null)
						{
							Value8 = column.value8;
						}
						if (column.value9 > -1 && column.value9 != null)
						{
							Value9 = column.value9;
						}
						if (column.value10 > -1 && column.value10 != null)
						{
							Value10 = column.value10;
						}
						
						var	dataObject =
							{
								occurredTime : date,
								firstValue : Value1,							
								secondValue : Value2,
								thirdValue : Value3,
								fourthValue : Value4,
								fifthValue : Value5,
								sixthValue : Value6,
								seventhValue : Value7,
								eighthValue : Value8,
								ninthValue : Value9,
								tenthValue : Value10,							
								firstName : ValueName1,
								secondName : ValueName2,
								thirdName : ValueName3,
								fourthName : ValueName4,
								fifthName : ValueName5,
								sixthName : ValueName6,
								seventhName : ValueName7,
								eighthName : ValueName8,
								ninthName : ValueName9,
								tenthName : ValueName10
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
			var chartOption =
			{
				 min : 0,
				 max : null,
				 linecolor1 : "#6cb8c8",
				 linecolor2 : "#fbc51a",
				 linecolor3 : "#d65f3d",
				 linecolor4 : "#976e96",
				 linecolor5 : "#fb8e33",
				 linecolor6 : "#9cc239",
				 linecolor7 : "#998c57",
				 linecolor8 : "#d987ad",
				 linecolor9 : "#557aa4",
				 linecolor10 : "#d8aa3a",
				 linecolor11 : "gray",
				 chartname : chartName,
				 axistitle : "bps",
				 maxPos : null,
				 cursorColor : "#0f47c7",
				 interval : monitoringPeriod
			};
			if (chartRefreshKey == 1)
			{
				var chartobj = obchart.OBDashboardGroupChartViewer(chartData, chartOption);
				return chartobj;
			}
			else if (chartRefreshKey == 2 || chartRefreshKey == 3)
			{
				obchart.OBAllChartUpdate(chartName, chartData, chartOption);
				return;
			}					
		}
	},
	// 9-3. ADC Throughtput 추이 Chart	(Multi Value 용 - SUM)
	GeneratedcServiceGroupThroughtputSumChartData : function(data, chartName, chartRefreshKey, monitoringPeriod)
	{	
		with(this)
		{
			var chartData = [];
			var chartDataList = [];
			if (data.serviceGroupThroughputInfo != null)
			{
				chartDataList = data.serviceGroupThroughputInfo;
			}
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
						var Value1 = undefined;				    
						var Value2 = undefined;
						var Value3 = undefined;
						var Value4 = undefined;
						var Value5 = undefined;
						var Value6 = undefined;
						var Value7 = undefined;
						var Value8 = undefined;
						var Value9 = undefined;
						var Value10 = undefined;
						var SumValue = 0;
						var ValueName1 = column.name1;
						var ValueName2 = column.name2;
						var ValueName3 = column.name3;
						var ValueName4 = column.name4;
						var ValueName5 = column.name5;
						var ValueName6 = column.name6;
						var ValueName7 = column.name7;
						var ValueName8 = column.name8;
						var ValueName9 = column.name9;
						var ValueName10 = column.name10;						
						
						
						if (column.value1 > -1 && column.value1 != null)
						{
							Value1 = column.value1;
							SumValue += Value1;
						}						
						if (column.value2 > -1 && column.value2 != null)
						{
							Value2 = column.value2;
							SumValue += Value2;
						}
						if (column.value3 > -1 && column.value3 != null)
						{
							Value3 = column.value3;
							SumValue += Value3;
						}
						if (column.value4 > -1 && column.value4 != null)
						{
							Value4 = column.value4;
							SumValue += Value4;
						}
						if (column.value5 > -1 && column.value5 != null)
						{
							Value5 = column.value5;
							SumValue += Value5;
						}
						if (column.value6 > -1 && column.value6 != null)
						{
							Value6 = column.value6;
							SumValue += Value6;
						}
						if (column.value7 > -1 && column.value7 != null)
						{
							Value7 = column.value7;
							SumValue += Value7;
						}
						if (column.value8 > -1 && column.value8 != null)
						{
							Value8 = column.value8;
							SumValue += Value8;
						}
						if (column.value9 > -1 && column.value9 != null)
						{
							Value9 = column.value9;
							SumValue += Value9;
						}
						if (column.value10 > -1 && column.value10 != null)
						{
							Value10 = column.value10;
							SumValue += Value10;
						}
						
						var	dataObject =
							{
								occurredTime : date,
								firstValue : Value1,							
								secondValue : Value2,
								thirdValue : Value3,
								fourthValue : Value4,
								fifthValue : Value5,
								sixthValue : Value6,
								seventhValue : Value7,
								eighthValue : Value8,
								ninthValue : Value9,
								tenthValue : Value10,							
								firstName : ValueName1,
								secondName : ValueName2,
								thirdName : ValueName3,
								fourthName : ValueName4,
								fifthName : ValueName5,
								sixthName : ValueName6,
								seventhName : ValueName7,
								eighthName : ValueName8,
								ninthName : ValueName9,
								tenthName : ValueName10,
								sumValue : SumValue
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
			var chartOption =
			{
				 min : 0,
				 max : null,
				 linecolor1 : "#6cb8c8",
				 linecolor2 : "#fbc51a",
				 linecolor3 : "#d65f3d",
				 linecolor4 : "#976e96",
				 linecolor5 : "#fb8e33",
				 linecolor6 : "#9cc239",
				 linecolor7 : "#998c57",
				 linecolor8 : "#d987ad",
				 linecolor9 : "#557aa4",
				 linecolor10 : "#d8aa3a",
				 linecolor11 : "gray",	
				 chartname : chartName,
				 axistitle : "bps",
				 axistitle2 : "bps",
				 maxPos : null,
				 cursorColor : "#0f47c7",
				 interval : monitoringPeriod
			};
			if (chartRefreshKey == 1)
			{
				var chartobj = obchart.OBDashboardGroupSumChartViewer(chartData, chartOption);
				return chartobj;
			}
			else if (chartRefreshKey == 2 || chartRefreshKey == 3)
			{
				obchart.OBAllChartUpdate(chartName, chartData, chartOption);
				return;
			}					
		}
	},
	// 10. CPU Chart
	GenerateCpuHistoryChart : function(data, chartName, chartRefreshKey, monitoringPeriod)
	{
		var chartData = [];
		var chartDataList = [];
		if (data.adcCpuInfo != null)
		{
			chartDataList = data.adcCpuInfo.adcCpuDataList;
		}		
		if (chartDataList.length > 0 && chartDataList != null)
		{
			for ( var i = 0; i < chartDataList.length; i++)
			{
				if (i == 0)
				{
					if (data.startTime < chartDataList[0].occurredTime)
					{
						var startTime = parseDateTime(data.startTime);
						chartData.push({occurredTime:startTime});
					}
				}
				var column = chartDataList[i];
				if (column)
				{
					if (column.usage < 0 )
					{				
					}
					else
					{
						var occurTime = column.occurredTime;
						var date = parseDateTime(occurTime);
						var firstValue = column.usage;
						var dataObject =
						{
							occurredTime : date,
							firstValue : firstValue
						};
						// add object to dataProvider array
						chartData.push(dataObject);
					}
				}
				if (i == (chartDataList.length - 1))
				{
					if (data.endTime > chartDataList[chartDataList.length - 1].occurredTime)
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
		var chartOption =
		{
			 min : 0,
			 max : 100,
			 linecolor : "#6cb8c8",
			 chartname : chartName,
			 axistitle : "%",
			 maxPos : null,
			 cursorColor : "#0f47c7",
			 title : "CPU",
			 interval : monitoringPeriod
		};
		if (chartRefreshKey == 1)
		{
			var chartobj = obchart.OBAreaChartViewer(chartData, chartOption);
			return chartobj;
		}
		else if (chartRefreshKey == 2 || chartRefreshKey == 3)
		{
			obchart.OBAllChartUpdate(chartName, chartData);
			return;
		}		
	},
	// 10 -2. CPU Chart (ADC 그룹)
	GenerateAdcGroupCpuHistoryChart : function(data, chartName, chartRefreshKey, monitoringPeriod)
	{
		with(this)
		{
			var chartData = [];
			var chartDataList = [];
			if (data.adcGroupCpuInfo != null)
			{
				chartDataList = data.adcGroupCpuInfo;
			}
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
						var Value1 = undefined;				    
						var Value2 = undefined;
						var Value3 = undefined;
						var Value4 = undefined;
						var Value5 = undefined;
						var Value6 = undefined;
						var Value7 = undefined;
						var Value8 = undefined;
						var Value9 = undefined;
						var Value10 = undefined;
						var ValueName1 = column.name1;
						var ValueName2 = column.name2;
						var ValueName3 = column.name3;
						var ValueName4 = column.name4;
						var ValueName5 = column.name5;
						var ValueName6 = column.name6;
						var ValueName7 = column.name7;
						var ValueName8 = column.name8;
						var ValueName9 = column.name9;
						var ValueName10 = column.name10;						
						
						
						if (column.value1 > -1 && column.value1 != null)
						{
							Value1 = column.value1;
						}						
						if (column.value2 > -1 && column.value2 != null)
						{
							Value2 = column.value2;
						}
						if (column.value3 > -1 && column.value3 != null)
						{
							Value3 = column.value3;
						}
						if (column.value4 > -1 && column.value4 != null)
						{
							Value4 = column.value4;
						}
						if (column.value5 > -1 && column.value5 != null)
						{
							Value5 = column.value5;
						}
						if (column.value6 > -1 && column.value6 != null)
						{
							Value6 = column.value6;
						}
						if (column.value7 > -1 && column.value7 != null)
						{
							Value7 = column.value7;
						}
						if (column.value8 > -1 && column.value8 != null)
						{
							Value8 = column.value8;
						}
						if (column.value9 > -1 && column.value9 != null)
						{
							Value9 = column.value9;
						}
						if (column.value10 > -1 && column.value10 != null)
						{
							Value10 = column.value10;
						}
						
						var	dataObject =
							{
								occurredTime : date,
								firstValue : Value1,							
								secondValue : Value2,
								thirdValue : Value3,
								fourthValue : Value4,
								fifthValue : Value5,
								sixthValue : Value6,
								seventhValue : Value7,
								eighthValue : Value8,
								ninthValue : Value9,
								tenthValue : Value10,							
								firstName : ValueName1,
								secondName : ValueName2,
								thirdName : ValueName3,
								fourthName : ValueName4,
								fifthName : ValueName5,
								sixthName : ValueName6,
								seventhName : ValueName7,
								eighthName : ValueName8,
								ninthName : ValueName9,
								tenthName : ValueName10								
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
			var chartOption =
			{
				 min : 0,
				 max : 100,
				 linecolor1 : "#6cb8c8",
				 linecolor2 : "#fbc51a",
				 linecolor3 : "#d65f3d",
				 linecolor4 : "#976e96",
				 linecolor5 : "#fb8e33",
				 linecolor6 : "#9cc239",
				 linecolor7 : "#998c57",
				 linecolor8 : "#d987ad",
				 linecolor9 : "#557aa4",
				 linecolor10 : "#d8aa3a",
				 chartname : chartName,
				 axistitle : "%",
				 maxPos : null,
				 cursorColor : "#0f47c7",
				 interval : monitoringPeriod
			};
			if (chartRefreshKey == 1)
			{
				var chartobj = obchart.OBDashboardGroupChartViewer(chartData, chartOption);
				return chartobj;
			}
			else if (chartRefreshKey == 2 || chartRefreshKey == 3)
			{
				obchart.OBAllChartUpdate(chartName, chartData, chartOption);
				return;
			}					
		}
	},
	// 11. Memory Chart
	GenerateMemoryHistoryChart : function(data, chartName, chartRefreshKey, monitoringPeriod)
	{
		var chartData = [];
		var chartDataList = [];
		if (data.adcMemoryInfo != null)
		{
			chartDataList = data.adcMemoryInfo.adcMemoryDataList;
		}	
		if (chartDataList.length > 0 && chartDataList != null)
		{
			for ( var i = 0; i < chartDataList.length; i++)
			{
				if (i == 0)
				{
					if (data.startTime < chartDataList[0].occurredTime)
					{
						var startTime = parseDateTime(data.startTime);
						chartData.push({occurredTime:startTime});
					}
				}
				var column = chartDataList[i];
				if (column)
				{
					if (column.usage < 0 )
					{				
					}
					else
					{
						var occurTime = column.occurredTime;
						var date = parseDateTime(occurTime);
						var firstValue = column.usage;
						var dataObject =
						{
							occurredTime : date,
							firstValue : firstValue
						};
						// add object to dataProvider array
						chartData.push(dataObject);
					}
				}
				if (i == (chartDataList.length - 1))
				{
					if (data.endTime > chartDataList[chartDataList.length - 1].occurredTime)
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
		var chartOption =
		{
			 min : 0,
			 max : 100,
			 linecolor : "#6cb8c8",
			 chartname : chartName,
			 axistitle : "%",
			 maxPos : null,
			 cursorColor : "#0f47c7",
			 title : "MEMORY",
			 interval : monitoringPeriod
		};
		if (chartRefreshKey == 1)
		{
			var chartobj = obchart.OBAreaChartViewer(chartData, chartOption);
			return chartobj;
		}
		else if (chartRefreshKey == 2 || chartRefreshKey == 3)
		{
			obchart.OBAllChartUpdate(chartName, chartData);
			return;
		}
	},
	// 11- 2. Memory Chart (ADC그룹)
	GenerateAdcGroupMemoryHistoryChart : function(data, chartName, chartRefreshKey, monitoringPeriod)
	{	
		with(this)
		{
			var chartData = [];
			var chartDataList = [];
			if (data.adcGroupMemoryInfo != null)
			{
				chartDataList = data.adcGroupMemoryInfo;
			}
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
						var Value1 = undefined;				    
						var Value2 = undefined;
						var Value3 = undefined;
						var Value4 = undefined;
						var Value5 = undefined;
						var Value6 = undefined;
						var Value7 = undefined;
						var Value8 = undefined;
						var Value9 = undefined;
						var Value10 = undefined;
						var ValueName1 = column.name1;
						var ValueName2 = column.name2;
						var ValueName3 = column.name3;
						var ValueName4 = column.name4;
						var ValueName5 = column.name5;
						var ValueName6 = column.name6;
						var ValueName7 = column.name7;
						var ValueName8 = column.name8;
						var ValueName9 = column.name9;
						var ValueName10 = column.name10;						
						
						
						if (column.value1 > -1 && column.value1 != null)
						{
							Value1 = column.value1;
						}						
						if (column.value2 > -1 && column.value2 != null)
						{
							Value2 = column.value2;
						}
						if (column.value3 > -1 && column.value3 != null)
						{
							Value3 = column.value3;
						}
						if (column.value4 > -1 && column.value4 != null)
						{
							Value4 = column.value4;
						}
						if (column.value5 > -1 && column.value5 != null)
						{
							Value5 = column.value5;
						}
						if (column.value6 > -1 && column.value6 != null)
						{
							Value6 = column.value6;
						}
						if (column.value7 > -1 && column.value7 != null)
						{
							Value7 = column.value7;
						}
						if (column.value8 > -1 && column.value8 != null)
						{
							Value8 = column.value8;
						}
						if (column.value9 > -1 && column.value9 != null)
						{
							Value9 = column.value9;
						}
						if (column.value10 > -1 && column.value10 != null)
						{
							Value10 = column.value10;
						}
						
						var	dataObject =
							{
								occurredTime : date,
								firstValue : Value1,							
								secondValue : Value2,
								thirdValue : Value3,
								fourthValue : Value4,
								fifthValue : Value5,
								sixthValue : Value6,
								seventhValue : Value7,
								eighthValue : Value8,
								ninthValue : Value9,
								tenthValue : Value10,							
								firstName : ValueName1,
								secondName : ValueName2,
								thirdName : ValueName3,
								fourthName : ValueName4,
								fifthName : ValueName5,
								sixthName : ValueName6,
								seventhName : ValueName7,
								eighthName : ValueName8,
								ninthName : ValueName9,
								tenthName : ValueName10								
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
			var chartOption =
			{
				 min : 0,
				 max : 100,
				 linecolor1 : "#6cb8c8",
				 linecolor2 : "#fbc51a",
				 linecolor3 : "#d65f3d",
				 linecolor4 : "#976e96",
				 linecolor5 : "#fb8e33",
				 linecolor6 : "#9cc239",
				 linecolor7 : "#998c57",
				 linecolor8 : "#d987ad",
				 linecolor9 : "#557aa4",
				 linecolor10 : "#d8aa3a",
				 chartname : chartName,
				 axistitle : "%",
				 maxPos : null,
				 cursorColor : "#0f47c7",
				 interval : monitoringPeriod
			};
			if (chartRefreshKey == 1)
			{
				var chartobj = obchart.OBDashboardGroupChartViewer(chartData, chartOption);
				return chartobj;
			}
			else if (chartRefreshKey == 2 || chartRefreshKey == 3)
			{
				obchart.OBAllChartUpdate(chartName, chartData, chartOption);
				return;
			}					
		}
	},
	// 12. 전체 ADC 트래픽 현황 Chart
	GenerateAllAdcTrafficChart : function(data, chartName, chartRefreshKey, monitoringPeriod)
    {
		var chartData = [];
		var chartDataList = [];
		if (data.allAdcTrafficInfo != null)
		{
			chartDataList = data.allAdcTrafficInfo;
		}		
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
						if (column.value < 0 )
						{				
						}
						else
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
		var chartOption =
		{
			 min : 0,
			 max : null,
			 linecolor : "#6cb8c8",
			 chartname : chartName,
			 axistitle : "bps",
			 maxPos : null,
			 cursorColor : "#0f47c7",
			 title : "전체그룹",
			 interval : monitoringPeriod
		};
		if (chartRefreshKey == 1)
		{
			var chartobj = obchart.OBAreaChartViewer(chartData, chartOption);			
			return chartobj;
		}
		else if (chartRefreshKey == 2 || chartRefreshKey == 3)
		{
			obchart.OBAllChartUpdate(chartName, chartData, chartOption);
			return;
		}		
	},
	// 13. VS 상태 변경 추이 Chart
	GenerateVsStatusChart : function(data, chartName, chartRefreshKey, monitoringPeriod)
	{		
		var chartData = [];
		if (data.vsStatusInfo.length > 0 &&
				 data.vsStatusInfo != null &&
				 data.vsStatusInfo != undefined)
		{
		var chartDataList = data.vsStatusInfo;		 
		 
			for ( var i = 0; i < chartDataList.length; i++)
			{
				if (i == 0)
				 {
					 if (data.startTime < chartDataList[0].occurredTime)
					 {
						 var startTime = parseDateTime(data.startTime);
						 chartData.push({occurredTime:startTime});
					 }
				 }
				var column = chartDataList[i];				
				if (column)
				{
					var occurTime = column.occurredTime;
					var date = parseDateTime(occurTime);
					var firstValue = column.vsAvailableCount;
		        	var secondValue = column.vsUnavailableCount;
		        	var thirdValue = column.vsDisableCount;
					
					var dataObject =
					{
						occurredTime : date,
						firstValue : firstValue,
						secondValue : secondValue,	
						thirdValue : thirdValue
					};
					// add object to dataProvider array
					chartData.push(dataObject);
				}
				if (i == (chartDataList.length - 1))
				{
					if (data.endTime > chartDataList[chartDataList.length - 1].occurredTime)
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
		var chartOption =
		{
			 min : 0,
			 max : null,
			 linecolor1 : "#6cb8c8",
			 linecolor2 : "#fbc51a",
			 linecolor3 : "#d65f3d",
			 chartname : chartName,
			 axistitle : VAR_COMMON_AFEW,
			 maxPos : null,
			 cursorColor : "#0f47c7",
			 interval : monitoringPeriod
		};
		if (chartRefreshKey == 1)
		{
			var chartobj = obchart.OBVsStatusChartViewer(chartData, chartOption);			
			return chartobj;
		}
		else if (chartRefreshKey == 2 || chartRefreshKey == 3)
		{
			obchart.OBAllChartUpdate(chartName, chartData);
			return;
		}		
	},
	/// 19. 응답시간 Chart
	GenerateResponseTimeHistoryChart : function(data, chartName, chartRefreshKey, monitoringPeriod)
	{
		with(this)
		{
			var ConfigDate = [];
			var maxData = [];
			var chartData = [];
			var chartDataList = [];
			if (data.responseTimeInfo != null)
			{
				chartDataList = data.responseTimeInfo;
			}
			//var chartConfigList = data.vsConnectionInfo.vsConfigEventList;			
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
			// Config Event, Array를 따로 넘길때.			
			/*if (chartConfigList.length > 0 && chartDataList != null)
			{
				for ( var i = 0; i < chartConfigList.length; i++)
				{		
					var column = chartConfigList[i];
					if (column)
					{
						var date = parseDateTime(column.occurredTime);	
						ConfigDate.push(date);
					}
				}
			}*/				
			
			var chartOption =
			{
				 min : 0,
				 max : null,
				 linecolor : "#6cb8c8",
				 chartname : chartName,
				 axistitle : "ms",
				 maxPos : Math.max.apply(null, $.map(maxData, function(o){return o.value;})),
				 cursorColor : "#0f47c7",
				 legend : true,
				 title : "responseTime",
				 interval : monitoringPeriod

			};
			if (chartRefreshKey == 1)
			{
				var chartobj = obchart.OBAreaChartViewer(chartData, chartOption);			
				return chartobj;
			}
			else if (chartRefreshKey == 2 || chartRefreshKey == 3)
			{
				obchart.OBAllChartUpdate(chartName, chartData, chartOption);
				return;
			}			
		}
	},
	/// 19 -2. 응답시간 Chart (Svc Group)
	GenerateSvcGroupResponseTimeHistoryChart : function(data, chartName, chartRefreshKey, monitoringPeriod)
	{	
		with(this)
		{
			var chartData = [];
			var chartDataList = [];
			if (data.serviceGroupResponseTimeInfo != null)
			{
				chartDataList = data.serviceGroupResponseTimeInfo;
			}
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
						var Value1 = undefined;				    
						var Value2 = undefined;
						var Value3 = undefined;
						var Value4 = undefined;
						var Value5 = undefined;
						var Value6 = undefined;
						var Value7 = undefined;
						var Value8 = undefined;
						var Value9 = undefined;
						var Value10 = undefined;
						var ValueName1 = column.name1;
						var ValueName2 = column.name2;
						var ValueName3 = column.name3;
						var ValueName4 = column.name4;
						var ValueName5 = column.name5;
						var ValueName6 = column.name6;
						var ValueName7 = column.name7;
						var ValueName8 = column.name8;
						var ValueName9 = column.name9;
						var ValueName10 = column.name10;						
						
						
						if (column.value1 > -1 && column.value1 != null)
						{
							Value1 = column.value1;
						}						
						if (column.value2 > -1 && column.value2 != null)
						{
							Value2 = column.value2;
						}
						if (column.value3 > -1 && column.value3 != null)
						{
							Value3 = column.value3;
						}
						if (column.value4 > -1 && column.value4 != null)
						{
							Value4 = column.value4;
						}
						if (column.value5 > -1 && column.value5 != null)
						{
							Value5 = column.value5;
						}
						if (column.value6 > -1 && column.value6 != null)
						{
							Value6 = column.value6;
						}
						if (column.value7 > -1 && column.value7 != null)
						{
							Value7 = column.value7;
						}
						if (column.value8 > -1 && column.value8 != null)
						{
							Value8 = column.value8;
						}
						if (column.value9 > -1 && column.value9 != null)
						{
							Value9 = column.value9;
						}
						if (column.value10 > -1 && column.value10 != null)
						{
							Value10 = column.value10;
						}
						
						var	dataObject =
							{
								occurredTime : date,
								firstValue : Value1,							
								secondValue : Value2,
								thirdValue : Value3,
								fourthValue : Value4,
								fifthValue : Value5,
								sixthValue : Value6,
								seventhValue : Value7,
								eighthValue : Value8,
								ninthValue : Value9,
								tenthValue : Value10,							
								firstName : ValueName1,
								secondName : ValueName2,
								thirdName : ValueName3,
								fourthName : ValueName4,
								fifthName : ValueName5,
								sixthName : ValueName6,
								seventhName : ValueName7,
								eighthName : ValueName8,
								ninthName : ValueName9,
								tenthName : ValueName10								
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
			var chartOption =
			{
				 min : 0,
				 max : null,
				 linecolor : "#6cb8c8",
				 chartname : chartName,
				 axistitle : "ms",
				 maxPos : null,
				 cursorColor : "#0f47c7",
				 interval : monitoringPeriod
			};
			if (chartRefreshKey == 1)
			{
				var chartobj = obchart.OBDashboardGroupChartViewer(chartData, chartOption);
				return chartobj;
			}
			else if (chartRefreshKey == 2 || chartRefreshKey == 3)
			{
				obchart.OBAllChartUpdate(chartName, chartData, chartOption);
				return;
			}					
		}
	},
	/// 19 -3. 응답시간 Chart (Svc Group - SUM)
	GenerateSvcGroupResponseTimeHistorySumChart : function(data, chartName, chartRefreshKey, monitoringPeriod)
	{
		with(this)
		{
			var chartData = [];
			var chartDataList = [];
			if (data.serviceGroupResponseTimeInfo != null)
			{
				chartDataList = data.serviceGroupResponseTimeInfo;
			}
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
						var Value1 = undefined;				    
						var Value2 = undefined;
						var Value3 = undefined;
						var Value4 = undefined;
						var Value5 = undefined;
						var Value6 = undefined;
						var Value7 = undefined;
						var Value8 = undefined;
						var Value9 = undefined;
						var Value10 = undefined;
						var SumValue = 0;
						var ValueName1 = column.name1;
						var ValueName2 = column.name2;
						var ValueName3 = column.name3;
						var ValueName4 = column.name4;
						var ValueName5 = column.name5;
						var ValueName6 = column.name6;
						var ValueName7 = column.name7;
						var ValueName8 = column.name8;
						var ValueName9 = column.name9;
						var ValueName10 = column.name10;						
						
						
						if (column.value1 > -1 && column.value1 != null)
						{
							Value1 = column.value1;
							SumValue += Value1;
						}						
						if (column.value2 > -1 && column.value2 != null)
						{
							Value2 = column.value2;
							SumValue += Value2;
						}
						if (column.value3 > -1 && column.value3 != null)
						{
							Value3 = column.value3;
							SumValue += Value3;
						}
						if (column.value4 > -1 && column.value4 != null)
						{
							Value4 = column.value4;
							SumValue += Value4;
						}
						if (column.value5 > -1 && column.value5 != null)
						{
							Value5 = column.value5;
							SumValue += Value5;
						}
						if (column.value6 > -1 && column.value6 != null)
						{
							Value6 = column.value6;
							SumValue += Value6;
						}
						if (column.value7 > -1 && column.value7 != null)
						{
							Value7 = column.value7;
							SumValue += Value7;
						}
						if (column.value8 > -1 && column.value8 != null)
						{
							Value8 = column.value8;
							SumValue += Value8;
						}
						if (column.value9 > -1 && column.value9 != null)
						{
							Value9 = column.value9;
							SumValue += Value9;
						}
						if (column.value10 > -1 && column.value10 != null)
						{
							Value10 = column.value10;
							SumValue += Value10;
						}
						
						var	dataObject =
							{
								occurredTime : date,
								firstValue : Value1,							
								secondValue : Value2,
								thirdValue : Value3,
								fourthValue : Value4,
								fifthValue : Value5,
								sixthValue : Value6,
								seventhValue : Value7,
								eighthValue : Value8,
								ninthValue : Value9,
								tenthValue : Value10,							
								firstName : ValueName1,
								secondName : ValueName2,
								thirdName : ValueName3,
								fourthName : ValueName4,
								fifthName : ValueName5,
								sixthName : ValueName6,
								seventhName : ValueName7,
								eighthName : ValueName8,
								ninthName : ValueName9,
								tenthName : ValueName10,
								sumValue : SumValue
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
			var chartOption =
			{
				 min : 0,
				 max : null,
				 linecolor : "#6cb8c8",
				 chartname : chartName,
				 axistitle : "ms",
				 maxPos : null,
				 cursorColor : "#0f47c7",
				 interval : monitoringPeriod
			};
			if (chartRefreshKey == 1)
			{
				var chartobj = obchart.OBDashboardGroupSumChartViewer(chartData, chartOption);
				return chartobj;
			}
			else if (chartRefreshKey == 2 || chartRefreshKey == 3)
			{
				obchart.OBAllChartUpdate(chartName, chartData, chartOption);
				return;
			}					
		}
	},
	/// 20. ConcurrentSessionDetail Chart
//	GenerateConcurrentSessionDetailChart : function(data, chartName, chartRefreshKey, name, monitoringPeriod)
	GenerateConcurrentSessionDetailChart : function(data, chartName, chartRefreshKey, monitoringPeriod)
	{
		with(this)
		{
			var chartData = [];
			var chartDataList = [];
			var maxData = [];
			if (data.sessionHistory != null)
			{
				chartDataList = data.sessionHistory.history;
			}
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
						if (column.flbSession < 0 && column.slbSession < 0)
						{					
						}
						else if (column.slbSession < 0)
						{
							var occurTime = column.occurTime;
							var date = parseDateTime(occurTime);							
							var secondValue = column.flbSession;
							var dataObject =
							{
								occurredTime : date,								
								secondValue : secondValue
							};
							var maxdataObject = 
							{
								value : secondValue
							};
							// add object to dataProvider array
							chartData.push(dataObject);
							maxData.push(maxdataObject);
						}
						else if (column.flbSession < 0)
						{
							var occurTime = column.occurTime;
							var date = parseDateTime(occurTime);
							var firstValue = column.slbSession;							
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
						else
						{
							var occurTime = column.occurTime;
							var date = parseDateTime(occurTime);
							var firstValue = column.slbSession;
							var secondValue = column.flbSession;
							var dataObject =
							{
								occurredTime : date,
								firstValue : firstValue,
								secondValue : secondValue,
								thirdValue : (firstValue + secondValue)
							};
							var maxdataObject = 
							{
								value : firstValue
							};
							// add object to dataProvider array
							chartData.push(dataObject);
							maxData.push(maxdataObject);
						}						
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
			var chartOption =
			{
				 min : 0,
				 max : null,
				 linecolor1 : "gray",
				 linecolor2 : "#6cb8c8",
				 linecolor3 : "#fbc51a",
				 chartname : chartName,
				 axistitle1 : VAR_COMMON_AFEW,
				 axistitle2 : VAR_COMMON_AFEW,
				 maxPos : null,
				 cursorColor : "#0f47c7",
//				 legendVal : name 
				 interval : monitoringPeriod
			};
			 
			if (chartRefreshKey == 1)
			{
				var chartobj = obchart.OBConnFlbSlbMultiChartViewer(chartData, chartOption);			
				return chartobj;
			}
			else if (chartRefreshKey == 2 || chartRefreshKey == 3)
			{
				obchart.OBAllChartUpdate(chartName, chartData, chartOption);
				return;
			}			
		}
	},
	/// 21. ConcurrentSessionFLB Chart
	GenerateConcurrentSessionFLBChart : function(data, chartName, chartRefreshKey, name, monitoringPeriod)
	{
		with(this)
		{
			var ConfigDate = [];
			var maxData = [];
			var chartData = [];
			var chartDataList = [];
			if (data.groupBpsConnHistoryInfo != null)
			{
				chartDataList = data.groupBpsConnHistoryInfo;
			}
			//var chartConfigList = data.vsConnectionInfo.vsConfigEventList;			
			if (chartDataList.length > 0 && chartDataList != null)
			{
				 for ( var i = 0; i < chartDataList.length; i++)
				 {
					if (i == 0)
					{
						if (data.startTime < chartDataList[0].connCurrValue.occurTime)
						{
							var startTime = parseDateTime(data.startTime);
							chartData.push({occurredTime:startTime});
						}
					}
					var column = chartDataList[i];
					if (column)
					{						
							var occurTime = column.connCurrValue.occurTime;
							var date = parseDateTime(occurTime);
							var firstValue = column.connCurrValue.value;							
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
						if (data.endTime > chartDataList[chartDataList.length - 1].connCurrValue.occurTime)
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

			var chartOption =
			{
				min : 0,
				max : null,
				linecolor : "#6cb8c8",
				chartname : chartName,
				axistitle : VAR_COMMON_AFEW,
				maxPos : null,
				cursorColor : "#0e7023",
				title : name,
				interval : monitoringPeriod
			};			
			 
			if (chartRefreshKey == 1)
			{
				var chartobj = obchart.OBAreaChartViewer(chartData, chartOption);			
				return chartobj;
			}
			else if (chartRefreshKey == 2 || chartRefreshKey == 3)
			{
				obchart.OBAllChartUpdate(chartName, chartData, chartOption);
				return;
			}			
		}
	},
	// 22. Alteon CPU SP Monitoring Chart
	GenerateCpuSPChart : function(data, chartName, chartRefreshKey)
	{	
		var chartData = [];
		var chartDataList = [];
		if (data.cpuSpConns != null)
		{
			chartDataList = data.cpuSpConns;
		}
		
		var maxData = [];
		
		var spValue1 = undefined; var spConns1 = undefined;
		var spValue2 = undefined; var spConns2 = undefined;
		var spValue3 = undefined; var spConns3 = undefined;
		var spValue4 = undefined; var spConns4 = undefined;
		var spValue5 = undefined; var spConns5 = undefined;
		var spValue6 = undefined; var spConns6 = undefined;
		var spValue7 = undefined; var spConns7 = undefined;
		var spValue8 = undefined; var spConns8 = undefined;
		var spValue9 = undefined; var spConns9 = undefined;
		var spValue10 = undefined; var spConns10 = undefined;
		var spValue11 = undefined; var spConns11 = undefined;
		var spValue12 = undefined; var spConns12 = undefined;
		var spValue13 = undefined; var spConns13 = undefined;
		var spValue14 = undefined; var spConns14 = undefined;
		var spValue15 = undefined; var spConns15 = undefined;
		var spValue16 = undefined; var spConns16 = undefined;
		var spValue17 = undefined; var spConns17 = undefined;
		var spValue18 = undefined; var spConns18 = undefined;
		var spValue19 = undefined; var spConns19 = undefined;
		var spValue20 = undefined; var spConns20 = undefined;
		var spValue21 = undefined; var spConns21 = undefined;
		var spValue22 = undefined; var spConns22 = undefined;
		var spValue23 = undefined; var spConns23 = undefined;
		var spValue24 = undefined; var spConns24 = undefined;
		var spValue25 = undefined; var spConns25 = undefined;
		var spValue26 = undefined; var spConns26 = undefined;
		var spValue27 = undefined; var spConns27 = undefined;
		var spValue28 = undefined; var spConns28 = undefined;
		var spValue29 = undefined; var spConns29 = undefined;
		var spValue30 = undefined; var spConns30 = undefined;
		var spValue31 = undefined; var spConns31 = undefined;
		var spValue32 = undefined; var spConns32 = undefined;
		
		if((chartDataList.cpu1Value > -1 && chartDataList.cpu1Value != null) || (chartDataList.cpu1Conns > -1 && chartDataList.cpu1Conns != null))
		{
			spValue1 = chartDataList.cpu1Value;	
			spConns1 = chartDataList.cpu1Conns;
			var dataObject = {
					"category": "MP",
					"column-1": spValue1,
//					"column-1": 55,
//					"column-2": spConns1
			    };
			chartData.push(dataObject);
			
//			var maxdataObject = 
//			{
//				"value" : spConns1
//			};				
//			maxData.push(maxdataObject);
		}						
		if((chartDataList.cpu2Value > -1 && chartDataList.cpu2Value != null) || (chartDataList.cpu2Conns > -1 && chartDataList.cpu2Conns != null))
		{
			spValue2 = chartDataList.cpu2Value;
			spConns2 = chartDataList.cpu2Conns;
			var dataObject1 = {
			    	"category": "SP1",
			    	"column-1": spValue2,
					"column-2": spConns2
//			    	"column-1": 45,
//					"column-2": 240000
			    };
			chartData.push(dataObject1);
			
			var maxdataObject1 = 
			{
				"value" : spConns2
			};				
			maxData.push(maxdataObject1);
		}
		if((chartDataList.cpu3Value > -1 && chartDataList.cpu3Value != null) || (chartDataList.cpu3Conns > -1 && chartDataList.cpu3Conns != null))
		{
			spValue3 = chartDataList.cpu3Value;
			spConns3 = chartDataList.cpu3Conns;
			var dataObject2 = {
			    	"category": "SP2",
			    	"column-1": spValue3,
					"column-2": spConns3
//			    	"column-1": 65,
//					"column-2": 340000
			    };
			chartData.push(dataObject2);
			
			var maxdataObject2 = 
			{
				"value" : spConns3
			};				
			maxData.push(maxdataObject2);
		}			
		if((chartDataList.cpu4Value > -1 && chartDataList.cpu4Value != null) || (chartDataList.cpu4Conns > -1 && chartDataList.cpu4Conns != null))
		{
			spValue4 = chartDataList.cpu4Value;
			spConns4 = chartDataList.cpu4Conns;
			var dataObject3 = {
			    	"category": "SP3",
			    	"column-1": spValue4,
					"column-2": spConns4
//			    	"column-1": 89,
//					"column-2": 250000
			    };
			chartData.push(dataObject3);
			
			var maxdataObject3 = 
			{
				"value" : spConns4
			};				
			maxData.push(maxdataObject3);
		}
		if((chartDataList.cpu5Value > -1 && chartDataList.cpu5Value != null) || (chartDataList.cpu5Conns > -1 && chartDataList.cpu5Conns != null))
		{
			spValue5 = chartDataList.cpu5Value;	
			spConns5 = chartDataList.cpu5Conns;
			var dataObject4 = {
			    	"category": "SP4",
			    	"column-1": spValue5,
					"column-2": spConns5
			    };
			chartData.push(dataObject4);
			
			var maxdataObject4 = 
			{
				"value" : spConns5
			};				
			maxData.push(maxdataObject4);
		}	
		if((chartDataList.cpu6Value > -1 && chartDataList.cpu6Value != null) || (chartDataList.cpu6Conns > -1 && chartDataList.cpu6Conns != null))
		{
			spValue6 = chartDataList.cpu6Value;	
			spConns6 = chartDataList.cpu6Conns;
			var dataObject5= {
			    	"category": "SP5",
			    	"column-1": spValue6,
					"column-2": spConns6
			    };
			chartData.push(dataObject5);
			
			var maxdataObject5 = 
			{
				"value" : spConns6
			};				
			maxData.push(maxdataObject5);
		}
		if((chartDataList.cpu7Value > -1 && chartDataList.cpu7Value != null) || (chartDataList.cpu7Conns > -1 && chartDataList.cpu7Conns != null))
		{
			spValue7 = chartDataList.cpu7Value;	
			spConns7 = chartDataList.cpu7Conns;
			var dataObject6= {
			    	"category": "SP6",
			    	"column-1": spValue7,
					"column-2": spConns7
			    };
			chartData.push(dataObject6);
			
			var maxdataObject6 = 
			{
				"value" : spConns7
			};				
			maxData.push(maxdataObject6);
		}
		if((chartDataList.cpu8Value > -1 && chartDataList.cpu8Value != null) || (chartDataList.cpu8Conns > -1 && chartDataList.cpu8Conns != null))
		{
			spValue8 = chartDataList.cpu8Value;	
			spConns8 = chartDataList.cpu8Conns;
			var dataObject7= {
			    	"category": "SP7",
			    	"column-1": spValue8,
					"column-2": spConns8
			    };
			chartData.push(dataObject7);
			
			var maxdataObject7 = 
			{
				"value" : spConns8
			};				
			maxData.push(maxdataObject7);
		}
		if((chartDataList.cpu9Value > -1 && chartDataList.cpu9Value != null) || (chartDataList.cpu9Conns > -1 && chartDataList.cpu9Conns != null))
		{
			spValue9 = chartDataList.cpu9Value;	
			spConns9 = chartDataList.cpu9Conns;
			var dataObject8= {
			    	"category": "SP8",
			    	"column-1": spValue9,
					"column-2": spConns9
			    };
			chartData.push(dataObject8);
			
			var maxdataObject8 = 
			{
				"value" : spConns9
			};				
			maxData.push(maxdataObject8);
		}
		if((chartDataList.cpu10Value > -1 && chartDataList.cpu10Value != null) || (chartDataList.cpu10Conns > -1 && chartDataList.cpu10Conns != null))
		{
			spValue10 = chartDataList.cpu10Value;	
			spConns10 = chartDataList.cpu10Conns;
			var dataObject9= {
			    	"category": "SP9",
			    	"column-1": spValue10,
					"column-2": spConns10
			    };
			chartData.push(dataObject9);
			
			var maxdataObject9 = 
			{
				"value" : spConns10
			};				
			maxData.push(maxdataObject9);
		}
		if((chartDataList.cpu11Value > -1 && chartDataList.cpu11Value != null) || (chartDataList.cpu11Conns > -1 && chartDataList.cpu11Conns != null))
		{
			spValue11 = chartDataList.cpu11Value;	
			spConns11 = chartDataList.cpu11Conns;
			var dataObject10= {
			    	"category": "SP10",
			    	"column-1": spValue11,
					"column-2": spConns11
			    };
			chartData.push(dataObject10);
			
			var maxdataObject10 = 
			{
				"value" : spConns11
			};				
			maxData.push(maxdataObject10);
		}
		if((chartDataList.cpu12Value > -1 && chartDataList.cpu12Value != null) || (chartDataList.cpu12Conns > -1 && chartDataList.cpu12Conns != null))
		{
			spValue12 = chartDataList.cpu12Value;	
			spConns12 = chartDataList.cpu12Conns;
			var dataObject11= {
			    	"category": "SP11",
			    	"column-1": spValue12,
					"column-2": spConns12
			    };
			chartData.push(dataObject11);
			
			var maxdataObject11 = 
			{
				"value" : spConns12
			};				
			maxData.push(maxdataObject11);
		}
		if((chartDataList.cpu13Value > -1 && chartDataList.cpu13Value != null) || (chartDataList.cpu13Conns > -1 && chartDataList.cpu13Conns != null))
		{
			spValue13 = chartDataList.cpu13Value;	
			spConns13 = chartDataList.cpu13Conns;
			var dataObject12= {
			    	"category": "SP12",
			    	"column-1": spValue13,
					"column-2": spConns13
			    };
			chartData.push(dataObject12);
			
			var maxdataObject12 = 
			{
				"value" : spConns13
			};				
			maxData.push(maxdataObject12);
		}
		if((chartDataList.cpu14Value > -1 && chartDataList.cpu14Value != null) || (chartDataList.cpu14Conns > -1 && chartDataList.cpu14Conns != null))
		{
			spValue14 = chartDataList.cpu14Value;	
			spConns14 = chartDataList.cpu14Conns;
			var dataObject13= {
			    	"category": "SP13",
			    	"column-1": spValue14,
					"column-2": spConns14
			    };
			chartData.push(dataObject13);
			
			var maxdataObject13 = 
			{
				"value" : spConns14
			};				
			maxData.push(maxdataObject13);
		}			
		if((chartDataList.cpu15Value > -1 && chartDataList.cpu15Value != null) || (chartDataList.cpu15Conns > -1 && chartDataList.cpu15Conns != null))
		{
			spValue15 = chartDataList.cpu15Value;	
			spConns15 = chartDataList.cpu15Conns;
			var dataObject14= {
			    	"category": "SP14",
			    	"column-1": spValue15,
					"column-2": spConns15
			    };
			chartData.push(dataObject14);
			
			var maxdataObject14 = 
			{
				"value" : spConns15
			};				
			maxData.push(maxdataObject14);
		}
		if((chartDataList.cpu16Value > -1 && chartDataList.cpu16Value != null) || (chartDataList.cpu16Conns > -1 && chartDataList.cpu16Conns != null))
		{
			spValue16 = chartDataList.cpu16Value;	
			spConns16 = chartDataList.cpu16Conns;
			var dataObject15= {
			    	"category": "SP15",
			    	"column-1": spValue16,
					"column-2": spConns16
			    };
			chartData.push(dataObject15);
			
			var maxdataObject15 = 
			{
				"value" : spConns16
			};				
			maxData.push(maxdataObject15);
		}
		if((chartDataList.cpu17Value > -1 && chartDataList.cpu17Value != null) || (chartDataList.cpu17Conns > -1 && chartDataList.cpu17Conns != null))
		{
			spValue17 = chartDataList.cpu17Value;	
			spConns17 = chartDataList.cpu17Conns;
			var dataObject16= {
			    	"category": "SP16",
			    	"column-1": spValue17,
					"column-2": spConns17
			    };
			chartData.push(dataObject16);
			
			var maxdataObject16 = 
			{
				"value" : spConns17
			};				
			maxData.push(maxdataObject16);
		}
		if((chartDataList.cpu18Value > -1 && chartDataList.cpu18Value != null) || (chartDataList.cpu18Conns > -1 && chartDataList.cpu18Conns != null))
		{
			spValue18 = chartDataList.cpu18Value;	
			spConns18 = chartDataList.cpu18Conns;
			var dataObject17= {
			    	"category": "SP17",
			    	"column-1": spValue18,
					"column-2": spConns18
			    };
			chartData.push(dataObject17);
			
			var maxdataObject17 = 
			{
				"value" : spConns18
			};				
			maxData.push(maxdataObject17);
		}
		if((chartDataList.cpu19Value > -1 && chartDataList.cpu19Value != null) || (chartDataList.cpu19Conns > -1 && chartDataList.cpu19Conns != null))
		{
			spValue19 = chartDataList.cpu19Value;	
			spConns19 = chartDataList.cpu19Conns;
			var dataObject18= {
			    	"category": "SP18",
			    	"column-1": spValue19,
					"column-2": spConns19
			    };
			chartData.push(dataObject18);
			
			var maxdataObject18 = 
			{
				"value" : spConns19
			};				
			maxData.push(maxdataObject18);
		}
		if((chartDataList.cpu20Value > -1 && chartDataList.cpu20Value != null) || (chartDataList.cpu20Conns > -1 && chartDataList.cpu20Conns != null))
		{
			spValue20 = chartDataList.cpu20Value;	
			spConns20 = chartDataList.cpu20Conns;
			var dataObject19= {
			    	"category": "SP19",
			    	"column-1": spValue20,
					"column-2": spConns20
			    };
			chartData.push(dataObject19);
			
			var maxdataObject19 = 
			{
				"value" : spConns20
			};				
			maxData.push(maxdataObject19);
		}						
		if((chartDataList.cpu21Value > -1 && chartDataList.cpu21Value != null) || (chartDataList.cpu21Conns > -1 && chartDataList.cpu21Conns != null))
		{
			spValue21 = chartDataList.cpu21Value;	
			spConns21 = chartDataList.cpu21Conns;
			var dataObject20 = {
					"category": "SP20",
					"column-1": spValue21,
					"column-2": spConns21
			    };
			chartData.push(dataObject20);
			
			var maxdataObject20 = 
			{
				"value" : spConns21
			};				
			maxData.push(maxdataObject20);
		}					
		if((chartDataList.cpu22Value > -1 && chartDataList.cpu22Value != null) || (chartDataList.cpu22Conns > -1 && chartDataList.cpu22Conns != null))
		{
			spValue22 = chartDataList.cpu22Value;
			spConns22 = chartDataList.cpu22Conns;
			var dataObject21 = {
			    	"category": "SP21",
			    	"column-1": spValue22,
					"column-2": spConns22
			    };
			chartData.push(dataObject21);
			
			var maxdataObject21 = 
			{
				"value" : spConns22
			};				
			maxData.push(maxdataObject21);
		}
		if((chartDataList.cpu23Value > -1 && chartDataList.cpu23Value != null) || (chartDataList.cpu23Conns > -1 && chartDataList.cpu23Conns != null))
		{
			spValue23 = chartDataList.cpu23Value;
			spConns23 = chartDataList.cpu23Conns;
			var dataObject22 = {
			    	"category": "SP22",
			    	"column-1": spValue23,
					"column-2": spConns23
			    };
			chartData.push(dataObject22);
			
			var maxdataObject22 = 
			{
				"value" : spConns23
			};				
			maxData.push(maxdataObject22);
		}			
		if((chartDataList.cpu24Value > -1 && chartDataList.cpu24Value != null) || (chartDataList.cpu24Conns > -1 && chartDataList.cpu24Conns != null))
		{
			spValue24 = chartDataList.cpu24Value;
			spConns24 = chartDataList.cpu24Conns;
			var dataObject23 = {
			    	"category": "SP23",
			    	"column-1": spValue24,
					"column-2": spConns24
			    };
			chartData.push(dataObject23);
			
			var maxdataObject23 = 
			{
				"value" : spConns24
			};				
			maxData.push(maxdataObject23);
		}
		if((chartDataList.cpu25Value > -1 && chartDataList.cpu25Value != null) || (chartDataList.cpu25Conns > -1 && chartDataList.cpu25Conns != null))
		{
			spValue25 = chartDataList.cpu25Value;	
			spConns25 = chartDataList.cpu25Conns;
			var dataObject24 = {
			    	"category": "SP24",
			    	"column-1": spValue25,
					"column-2": spConns25
			    };
			chartData.push(dataObject24);
			
			var maxdataObject24 = 
			{
				"value" : spConns25
			};				
			maxData.push(maxdataObject24);
		}	
		if((chartDataList.cpu26Value > -1 && chartDataList.cpu26Value != null) || (chartDataList.cpu26Conns > -1 && chartDataList.cpu26Conns != null))
		{
			spValue26 = chartDataList.cpu26Value;	
			spConns26 = chartDataList.cpu26Conns;
			var dataObject25= {
			    	"category": "SP25",
			    	"column-1": spValue26,
					"column-2": spConns26
			    };
			chartData.push(dataObject25);
			
			var maxdataObject25 = 
			{
				"value" : spConns26
			};				
			maxData.push(maxdataObject25);
		}
		if((chartDataList.cpu27Value > -1 && chartDataList.cpu27Value != null) || (chartDataList.cpu27Conns > -1 && chartDataList.cpu27Conns != null))
		{
			spValue27 = chartDataList.cpu27Value;	
			spConns27 = chartDataList.cpu27Conns;
			var dataObject26= {
			    	"category": "SP26",
			    	"column-1": spValue27,
					"column-2": spConns27
			    };
			chartData.push(dataObject26);
			
			var maxdataObject26 = 
			{
				"value" : spConns27
			};				
			maxData.push(maxdataObject26);
		}
		if((chartDataList.cpu28Value > -1 && chartDataList.cpu28Value != null) || (chartDataList.cpu28Conns > -1 && chartDataList.cpu28Conns != null))
		{
			spValue28 = chartDataList.cpu28Value;	
			spConns28 = chartDataList.cpu28Conns;
			var dataObject27= {
			    	"category": "SP27",
			    	"column-1": spValue28,
					"column-2": spConns28
			    };
			chartData.push(dataObject27);
			
			var maxdataObject27 = 
			{
				"value" : spConns28
			};				
			maxData.push(maxdataObject27);
		}
		if((chartDataList.cpu29Value > -1 && chartDataList.cpu29Value != null) || (chartDataList.cpu29Conns > -1 && chartDataList.cpu29Conns != null))
		{
			spValue29 = chartDataList.cpu29Value;	
			spConns29 = chartDataList.cpu29Conns;
			var dataObject28= {
			    	"category": "SP28",
			    	"column-1": spValue29,
					"column-2": spConns29
			    };
			chartData.push(dataObject28);
			
			var maxdataObject28 = 
			{
				"value" : spConns29
			};				
			maxData.push(maxdataObject28);
		}
		if((chartDataList.cpu30Value > -1 && chartDataList.cpu30Value != null) || (chartDataList.cpu30Conns > -1 && chartDataList.cpu30Conns != null))
		{
			spValue30 = chartDataList.cpu30Value;	
			spConns30 = chartDataList.cpu30Conns;
			var dataObject29= {
			    	"category": "SP29",
			    	"column-1": spValue30,
					"column-2": spConns30
			    };
			chartData.push(dataObject29);
			
			var maxdataObject29 = 
			{
				"value" : spConns30
			};				
			maxData.push(maxdataObject29);
		}
		if((chartDataList.cpu31Value > -1 && chartDataList.cpu31Value != null) || (chartDataList.cpu31Conns > -1 && chartDataList.cpu31Conns != null))
		{
			spValue31 = chartDataList.cpu31Value;	
			spConns31 = chartDataList.cpu31Conns;
			var dataObject30= {
			    	"category": "SP30",
			    	"column-1": spValue31,
					"column-2": spConns31
			    };
			chartData.push(dataObject30);
			
			var maxdataObject30 = 
			{
				"value" : spConns31
			};				
			maxData.push(maxdataObject30);
		}
		if((chartDataList.cpu32Value > -1 && chartDataList.cpu32Value != null) || (chartDataList.cpu32Conns > -1 && chartDataList.cpu32Conns != null))
		{
			spValue32 = chartDataList.cpu32Value;	
			spConns32 = chartDataList.cpu32Conns;
			var dataObject31= {
			    	"category": "SP31",
			    	"column-1": spValue32,
					"column-2": spConns32
			    };
			chartData.push(dataObject31);
			
			var maxdataObject31 = 
			{
				"value" : spConns32
			};				
			maxData.push(maxdataObject31);
		}
		
		if((chartDataList.spSessionMax == -1) || (chartDataList.spSessionMax == undefined))
		{
			var dataObject= {
			    	"category": "CPU",
			    	"column-1": undefined,
					"column-2": undefined
			    };
			chartData.push(dataObject);
		}
		
		var spUsageMin = 0;
		if(chartDataList.spUsageMin > 0)
		{
			spUsageMin = chartDataList.spUsageMin -  (chartDataList.spUsageMin * 0.00001);
		}
		else
		{
			spUsageMin = chartDataList.spUsageMin;
		}
						
		var spSessionMin = 0;
		if(chartDataList.spSessionMin > 0)
		{
			spSessionMin = chartDataList.spSessionMin -  (chartDataList.spSessionMin * 0.00001);
		}
		else
		{
			spSessionMin = chartDataList.spSessionMin;
		}
		
		var chartOption =
		{
//			 min : 0,
			 min : spUsageMin,
			 max : 100,
			 linecolor : "#6cb8c8",
			 chartname : chartName,
			 axistitle1 : "%",
			 axistitle2 : VAR_COMMON_AFEW,
			 
			 minSession : spSessionMin,
			 maxPos : Math.max.apply(null, $.map(maxData, function(o){return o.value;})),
			 spSessionMax : chartDataList.spSessionMax,
			 spSessionMaxUnit : chartDataList.spSessionMaxUnit,			
			 cursorColor : "#0f47c7"
		};

		if (chartRefreshKey == 1)
		{
			var chartobj = obchart.OBAreaChartSPDashViewer(chartData, chartOption);			
			return chartobj;
		}
		else if (chartRefreshKey == 2 || chartRefreshKey == 3)
		{
			obchart.OBAllChartSPCpuUpdate(chartName, chartData, chartOption);
			return;
		}		
	}
});