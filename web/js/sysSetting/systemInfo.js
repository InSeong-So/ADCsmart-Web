var SystemInfo = Class.create({
	initialize : function() 
	{
		this.searchLastHours = 12;
		this.searchStartTime = undefined;
		this.searchEndTime = undefined;
		this.selectedSearchTimeMode = undefined;		
		this.refreshIntervalSeconds = 0;
		this.intervalMonitor = undefined;
	},
	loadContent : function() 
	{
		with (this) 
		{
			ajaxManager.runHtmlExt({
				url			: "sysSetting/loadSystemInfoContent.action",
				target		: "#wrap .contents",
				successFn	: function(data) 
				{
					registSystemInfoContentEvents();
					restoreSystemInfoContent();
					header.setActiveMenu('SysSettingSysInfo');
					loadSystemInfo();
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_SYSINFO_LOAD, jqXhr);
				}	
			});
		}
	},
	loadSystemInfoContent : function() 
	{
		with (this) 
		{
			ajaxManager.runHtmlExt({
				url			: "sysSetting/loadSystemInfoContent.action",
				target		: "#wrap .contents",
				successFn	: function(data) 
				{
					registSystemInfoContentEvents();
					restoreSystemInfoContent();
					header.setActiveMenu('SysSettingSysInfo');
					loadSystemInfo();					
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_SYSINFO_LOAD, jqXhr);
				}	
			});
		}
	},
	loadSystemInfo : function() 
	{
		with (this) 
		{
			var params =
			{				
				"startTimeL"		: searchStartTime,
				"endTimeL"			: searchEndTime
			};
			ajaxManager.runJsonExt({
				url			: "sysSetting/loadSystemInfo.action",
				data		: params,
				successFn	: function(data) 
				{		
					intervalMonitor = data.intervalMonitor;
					GenerateChart(data, intervalMonitor);
					if(data.systemCpuInfo)
					{
						if(!data.systemCpuInfo.intCurUsage)
						{
							$('#cpuNowUsage').text("0");
						}
						else
						{
							$('#cpuNowUsage').text(data.systemCpuInfo.intCurUsage);
						}
					}	
					if(data.systemMemoryInfo)
					{
						if(!data.systemMemoryInfo.curUsage)
						{
							$('#memoryNowUsage').text("0");
						}
						else
						{
							$('#memoryNowUsage').text(data.systemMemoryInfo.curUsage);											
						}
					}					
					if(data.systemHddInfo)
					{						
						$('#hdd_used_size').text(data.systemHddInfo.usedSize);
						$('#hdd_free_size').text(data.systemHddInfo.freeSize);
						
						$('#hdd_used_usage').text(data.systemHddInfo.usedUsage);
						$('#hdd_free_usage').text(data.systemHddInfo.freeUsage);
					}					
					if(data.systemDatabaseInfo)
					{						
						$('#db_log_size').text(data.systemDatabaseInfo.logSize);
						$('#db_index_size').text(data.systemDatabaseInfo.indexSize);
						$('#db_general_size').text(data.systemDatabaseInfo.generalSize);
						$('#db_etc_size').text(data.systemDatabaseInfo.etcSize);
						
						$('#db_log_usage').text(data.systemDatabaseInfo.logUsage);
						$('#db_index_usage').text(data.systemDatabaseInfo.indexUsage);
						$('#db_general_usage').text(data.systemDatabaseInfo.generalUsage);
						$('#db_etc_usage').text(data.systemDatabaseInfo.etcUsage);
					}
				},
				errorFn : function(a,b,c)
				{
					exceptionEvent();
				}	
			});
		}
	},
	restoreSystemInfoContent: function()
	{
		with (this)
		{					
			// DatePicker 초기값 Setting
			if (null != searchStartTime) // 검색을 한번이라도 했을 경우
			{
				$('#reservationtime').val(new Date(searchStartTime).format('yyyy-mm-dd HH:MM')
						+" ~ "+ new Date(searchEndTime).format('yyyy-mm-dd HH:MM'));	
			}
			else // 검색을 한번도 하지않았을 경우
			{
				$('#reservationtime').val(moment().subtract(12, 'hour').format('YYYY-MM-DD HH:mm')
						+" ~ "+ moment().format('YYYY-MM-DD HH:mm'));
			}
			
			// DateRangePicker
			if(langCode=="ko_KR")
			{
				$('#reservationtime').daterangepicker({					
					ranges: {					 
					 '최근 1시간' : [moment().subtract('hour', 1) ,moment()],
					 '최근 3시간' : [moment().subtract('hour', 3) ,moment()],
					 '최근 6시간' : [moment().subtract('hour', 6) ,moment()],
					 '최근 12시간' : [moment().subtract('hour', 12) ,moment()],
					 '최근 24시간' : [moment().subtract('hour', 24) ,moment()],
					 '오늘': [moment().startOf('days'), moment()],
					 '최근 30일': [moment().subtract('days', 30), moment()]					 
			      	},
				    startDate: moment().subtract(12, 'hour').format('YYYY-MM-DD HH:mm'),
					endDate: moment().format('YYYY-MM-DD HH:mm'),
				    opens: 'left', // 달력위치
	                timePicker: true,
	                timePickerIncrement: 30,
	                timePicker12Hour : false,
	                format: 'YYYY-MM-DD HH:mm'
	              }, function(start, end, label) {
//	            	  console.log(start.toISOString(), end.toISOString(), label);
	            	  searchStartTime = Number(start.format('x'));
	            	  searchEndTime = Number(end.format('x'));
	              });
			}
			else
			{
				// 리뉴얼 DatePicker
				$('#reservationtime').daterangepicker({					
					ranges: {					 
					 'Last hour' : [moment().subtract('hour', 1) ,moment()],
					 'Last 3 hours' : [moment().subtract('hour', 3) ,moment()],
					 'Last 6 hours' : [moment().subtract('hour', 6) ,moment()],
					 'Last 12 hours' : [moment().subtract('hour', 12) ,moment()],
					 'Last 1 Day' : [moment().subtract('hour', 24) ,moment()],
					 'Today': [moment().startOf('days'), moment()],
					 'Last 30 Days': [moment().subtract('days', 30), moment()]					 
			      	},
				    startDate: moment().subtract(12, 'hour').format('YYYY-MM-DD HH:mm'),
					endDate: moment().format('YYYY-MM-DD HH:mm'),
				    opens: 'left', // 달력위치
	                timePicker: true,
	                timePickerIncrement: 30,
	                timePicker12Hour : false,
	                format: 'YYYY-MM-DD HH:mm'
	              }, function(start, end, label) {
//	            	  console.log(start.toISOString(), end.toISOString(), label);
	            	  searchStartTime = Number(start.format('x'));
	            	  searchEndTime = Number(end.format('x'));
	              });
			}	
		}		
	},
	registSystemInfoContentEvents : function() 
	{
		with (this) 
		{
			$('#refresh').click(function(event) 
			{
				event.preventDefault();
				loadSystemInfoContent();
			});
		}
	},
	GenerateChart : function(data, intervalMonitor)
	{
		GenerateSysInfoCpuChartData(data);
		GenerateSysInfoMemoryChartData(data);			
		GenerateSysInfoHDDChartData(data);
		GenerateSysInfoDatabaseChartData(data);	
		// 시스템 정보, CPU 차트
		function GenerateSysInfoCpuChartData(data)
		{
			var chartData = [];
			var chartDataList = data.systemCpuInfo.systemCpuDataList;
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
			// TODO 차트옵션 Object 형으로 인자 변경 작업중.
			var chartOption =
			{
				 min : 0,
				 max : 100,
				 linecolor : "#6cb8c8",
				 chartname : "SysInfoCpuChart",
				 axistitle : "%",
				 maxPos : data.systemCpuInfo.intMaxUsage,
				 avgPos : data.systemCpuInfo.intAvgUsage,
				 cursorColor : "#0f47c7",
				 interval : intervalMonitor
			};
			obchart.OBSystemCpuMemoryChart(chartData, chartOption);
		}		
		// 시스템 정보, Memory 차트
		function GenerateSysInfoMemoryChartData(data)
		{
			var chartData = [];
			var chartDataList = data.systemMemoryInfo.systemMemoryDataList;
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
				 linecolor : "#6cb8c8",
				 chartname : "SysInfoMemoryChart",
				 axistitle : "%",
				 maxPos : data.systemMemoryInfo.maxUsage,
				 avgPos : data.systemMemoryInfo.avgUsage,
				 cursorColor : "#0f47c7",
				 interval : intervalMonitor
			};
			obchart.OBSystemCpuMemoryChart(chartData, chartOption);
		}
		// 시스템 정보, 하드 디스크 차트
		function GenerateSysInfoHDDChartData(data)
		{
			var chartData = undefined;
			var before30Usage = -1;
			var before30Size =  -1;
			if (null != data && null != data.systemHddInfo)
			{
				if (null != data.systemHddInfo.usedUsage30Before)
				{
					before30Usage = data.systemHddInfo.usedUsage30Before;
					before30Size = data.systemHddInfo.usedSize30Before;
				}
				chartData =
				{
					totalUsage : data.systemHddInfo.totalUsage,
					usedUsage : data.systemHddInfo.usedUsage,						
					freeUsage: data.systemHddInfo.freeUsage,
					totalSize: data.systemHddInfo.totalSize,
					usedSize: data.systemHddInfo.usedSize,
					freeSize: data.systemHddInfo.freeSize,
					before30Usage : before30Usage,
					before30Size : before30Size
				};
			}
			else 
			{
				chartData =
				{
					totalUsage : 0,
					usedUsage : 0,						
					freeUsage: 0,
					totalSize: 0,
					usedSize: 0,
					freeSize: 0,
					before30Usage : 0,
					before30Size : 0
				};
			}		
			var chartname = "SysInfoHDDChart";
			obchart.OBNewSystemHDDChartMaker(chartData, chartname);
		}
		// 시스템 정보, 데이터 베이스 차트
		function GenerateSysInfoDatabaseChartData(data)
		{
			var chartData = undefined;
			if (null != data && null != data.systemDatabaseInfo)
			{
				chartData =
					{
						TotalSize : data.systemDatabaseInfo.totalSize,
						TotalUsedSize : data.systemDatabaseInfo.totalUsedSize,						
						GeneralSize: data.systemDatabaseInfo.generalSize,
						IndexSize: data.systemDatabaseInfo.indexSize,
						LogSize: data.systemDatabaseInfo.logSize,
						EtcSize: data.systemDatabaseInfo.etcSize,						
						GeneralUsage: data.systemDatabaseInfo.generalUsage,
						IndexeUsage: data.systemDatabaseInfo.indexUsage,
						LogUsage: data.systemDatabaseInfo.logUsage,
						EtcUsage: data.systemDatabaseInfo.etcUsage
					};
			} 
			else 
			{
				chartData =
				{
					TotalSize: 0,
					TotalUsedSize: 0,					
					GeneralSize: 0,
					IndexSize: 0,
					LogSize: 0,
					EtcSize: 0,					
					GeneralUsage: 0,
					IndexeUsage: 0,
					LogUsage: 0,
					EtcUsage: 0
				};
			}
			var chartname = "SysInfoDatabaseChart";			
			obchart.OBNewSystemDatabaseChartMaker(chartData, chartname);
		}	
	}
});