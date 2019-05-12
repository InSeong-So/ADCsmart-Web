var FlbGroupPerfomance = Class.create({
	initialize : function()
	{
		this.adc;
		this.selectedGroupIndex;	// 선택된 GroupIndex
		this.selectedGroupName;		// 선택된 GroupName		
		this.searchStartTime = undefined;
		this.searchEndTime = undefined;
		this.pageNavigator = new PageNavigator({ rowCountPerPage : 10 });
		this.orderDir = 2; // 내림차순 = 2
        this.orderType = 40; // 9NINTH = 41
        this.owner = undefined;		
		this.flbRealsvr = new FlbGroupPerfomanceRealServer();
		this.flbRealsvr.owner = this;
        
        this.selectedGroupNameKey = false;		// Seleted 된 vs 의 Member Page Load를 위한 Key
        var _self = this;
		this.pageNavigator.onChange(function(fromRow, toRow, orderType, orderDir) {
			_self.selectedGroupIndex = undefined;
			_self.selectedGroupName= undefined;
			_self.loadGroupPerfInfoList(fromRow, toRow);
		});
	},
	onAdcChange : function()
	{
		with(this)
		{			
			loadGroupPerfomanceContent();
		}
	},
	// 초기 페이지 Open
	loadGroupPerfomanceContent : function(index)
	{
		if (header.getMonitorLbTap() != 2)
		{
			//TODO
			monitorServicePerfomance.loadServicePerfomanceContent();
			return;
		}
		if (index)
		{
			this.selectedGroupIndex = index;
		}
		this.adc = adcSetting.getAdc();
		with (this)
		{
			selectedGroupNameKey = false;
			if (!adc)
			{
				return;
			}
			var params = 
			{
					"targetObject.adcRange" 		: 2,					
					"targetObject.index"			: adc.index,
					"adcObject.desciption"  : adc.type
			};
			ajaxManager.runHtmlExt({
				url : "monitor/loadFLBGroupPerfomanceContent.action",
				data : params,
				target : "#wrap .contents",				
				successFn : function(data)
				{
					header.setActiveMenu('MonitorServicePerfomance');
					registGroupPerfomanceContentEvents(adc);
					restoreGroupPerfomanceContent();
					updateNavigator(adc, index);
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_SVCPERFOM_LAOD, jqXhr);
				}
			});
		}
	},
	
	// Total 갯수 Get, Navigator 에 업데이트
	updateNavigator : function(adc, index) 
	{
		with (this)
		{
			if (!adc)
			{
				return;
			}			
			var params = {
					"targetObject.adcRange" 		: 2,					
					"targetObject.index"			: adc.index					
			};			
			var _self = this;
			ajaxManager.runJsonExt
			({
				url			: "monitor/loadGroupPerfInfoTotalCount.action",
				data		: params,
				successFn	: function(data)
				{
					var totalCount = 0;
					if (data.totalCount)
					{
						totalCount = data.totalCount;
					}
					pageNavigator.updateRowTotal(totalCount);
					
					if (index)
					{
						this.selectedGroupIndex = index;						
					}
					else
					{
						_self.selectedGroupIndex = undefined;						
					}								
					loadGroupPerfInfoList();
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_SVCPERFOM_LAOD, jqXhr);
				}
			});
		}
	},
	// Table 데이터 성능 정보 Get
	loadGroupPerfInfoList : function(fromRow, toRow, orderType, orderDir)
	{
		with (this) 
		{
			if (!adc) 
			{
				return;
			}			
			var params = 
			{
				"targetObject.adcRange" 		: 2,					
				"targetObject.index"			: adc.index,
				"orderOption.orderType"			: orderType,
				"orderOption.orderDirection"	: orderDir
			};
			params["searchOption.beginIndex"] = (undefined === fromRow ? pageNavigator.getFirstRowOfCurrentPage() : fromRow);
			params["searchOption.endIndex"] = (undefined === toRow ? pageNavigator.getLastRowOfCurrentPage() : toRow);
			
			var _self = this;
			ajaxManager.runHtmlExt
			({
				url		: "monitor/loadGroupPerfInfoList.action",
				data	: params,
				target	: "table.GroupPerfInfoList",
				successFn : function(data)
				{	
					registGroupPerfInfoListEvents(adc);
					pageNavigator.refresh();
					
					orderType = undefined;
					var groupIndex = _self.selectedGroupIndex;	
					var groupName = _self.selectedGroupName;
					if (!_self.selectedGroupIndex)// 선택된게 없을때 맨처음껄 선택한다.
					{
						groupIndex = $('#group_table tbody tr').attr('id');
						groupName = $('#group_table tbody tr').eq(0).find('.groupName').text();
					}					
					this.selectedGroupIndex = groupIndex;
					loadBpsConnHistoryInfo(groupIndex, undefined, groupName);										
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_SVCPERFOM_LAOD, jqXhr);
				}
			});
		}		
	},
	restoreGroupPerfomanceContent : function() 
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
				    opens: 'right', // 달력위치
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
				    opens: 'right', // 달력위치
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
	// 초기페이지 로드 후 기본 form 이벤트 (검색조건, Refresh, 내보내기 Btn) 
	registGroupPerfomanceContentEvents : function (adc)
	{
		with(this)
		{
			$('.slbServiceLnk').click(function(e) 
			{
				e.preventDefault();
				header.setMonitorLbTap(1);
				monitorServicePerfomance.loadServicePerfomanceContent();
			});
			
			$('#refresh').click(function(event) 
			{
				event.preventDefault();
				loadGroupPerfInfoList();
			});
			$('#exportCssLnk').click(function(event) 
			{
				checkExportGroupPerfDataExist();
			});
		}
	},
	// 성능정보에서 IP click 하면 팝업이 아닌 새페이지로 보여줌
	loadVsTrafficInfo : function(groupIndex, groupName, groupDisplayIndex)
	{
		with (this)
		{
			if (!adc || !groupIndex)
			{
				return;
			}
			var timeObject =
			{
				StartTime : searchStartTime,
				EndTime : searchEndTime				
			};
			flbRealsvr.load(groupIndex, groupName, groupDisplayIndex, timeObject);
		}
	},
	// 리스트 테이블 내에서의 이벤트 (row Click, Sorting) 
	registGroupPerfInfoListEvents : function(adc)
	{
		with(this)
		{
			// name을 클릭하는 경우 ( trafficDetail 팝업을 띄운다.)
			$('.trafficDetailLnk').click(function(e)
			{
				e.preventDefault();
				var groupIndex = $(this).parent().parent().attr('id');
				var groupName = $(this).parent().parent().find('.groupName').text();
				var groupDisplayIndex = $(this).parent().parent().find('.groupIndex').text();

				if (adc.type == "Alteon")
				{
					loadVsTrafficInfo(groupIndex, groupName, groupDisplayIndex);
				}
				else
				{
					loadVsTrafficInfo(groupIndex, groupName, groupDisplayIndex);
				}
				selectedGroupNameKey = true;
			});
			//  List Table row click 시 발생하는 이벤트
			$('#group_table tbody tr').click(function(e)
			{
				if (true ==selectedGroupNameKey)
				{
					return;
				}
				//TODO
				var groupIndex = $(this).attr('id');
				var groupName = $(this).find('.groupName').text();
				this.selectedGroupIndex = groupIndex;
				this.selectedGroupName = groupName;				
				loadBpsConnHistoryInfo(groupIndex, $(this).index(), groupName);				
			});
			
			$('.orderDir_Desc').click(function(e)
			{
				e.preventDefault();
				orderDir =  $(this).find('.orderDir').text();
				orderType =  $(this).find('.orderType').text();					
				loadGroupPerfInfoList();
			});
			
			$('.orderDir_Asc').click(function(e)
			{
				e.preventDefault();
				orderDir =  $(this).find('.orderDir').text();
				orderType =  $(this).find('.orderType').text();	
				loadGroupPerfInfoList();
			});
			
			$('.orderDir_None').click(function(e)
			{
				e.preventDefault();
				orderDir =  $(this).find('.orderDir').text();
				orderType =  $(this).find('.orderType').text();	
				loadGroupPerfInfoList();
			});			
		}		
	},	
	checkExportGroupPerfDataExist : function()
	{
		with(this)
		{
			if (!adc)
			{
				return;
			}
			var params = {			
					"groupIndex"	: selectedGroupIndex,
					"startTimeL"	: searchStartTime,
					"endTimeL"		: searchEndTime
				};
				ajaxManager.runJsonExt
				({
					url : "monitor/checkExportGroupPerfDataExist.action",
					data : params,
					successFn : function(data)
					{
						if(!data.isSuccessful)
						{
							$.obAlertNotice(data.message);
							return;
						}
						exportCss();
					},
					errorFn : function(jqXhr)
					{
						$.obAlertAjaxError(VAR_SVCPERFOM_EXPEXISTINSPECT, jqXhr);
					}
				});			
		}		
	},	
	exportCss : function()
	{
		with (this)
		{
			var params = "groupIndex=" + selectedGroupIndex + "&startTimeL=" + searchStartTime + "&endTimeL=" + searchEndTime;			
			var url = "monitor/downloadGroupPerfInfo.action?" + params;
			$('#downloadFrame').attr('src', url);			
		}
	},	
	// Select 테이블 Row
	selectGroupPerfMonitorTableRow : function(groupIndex, rowIndex, groupName)
	{
		$('#group_table tbody tr').removeClass("vsMonitorRowSelection");		
		$('#group_table tbody tr').each(function(index) {
			//if ($(this).find('.groupIndex').text() === groupIndex) 
			if ($(this).attr('id') === groupIndex) 
			{
				if (index === rowIndex || (!rowIndex && 0 !== rowIndex))
				{					
					$(this).addClass("vsMonitorRowSelection");
					return false;
				}
			}
		});
		// Header Notice 기능
		this.listHeaderNameChanger(groupName);
	},	
	listHeaderNameChanger : function(groupName)
	{
		var lisetHeaderName = groupName;
		var $tbody = $('.contents_area #lisetHeaderChange').filter(':last');		
		$tbody.empty();	
		var html = '';		
		html += '[' + lisetHeaderName + ']';		
		$tbody.html(html);		
	},	
	
	// bps&Connection Chart Data get
	loadBpsConnHistoryInfo : function(groupIndex, rowIndex, groupName)
	{
		this.selectedGroupIndex = groupIndex;	
		this.selectedGroupName = groupName;
		with (this)
		{
			if (!adc || !groupIndex)
			{
				return;
			}
			var params = {
				"groupIndex"	: groupIndex,
				"startTimeL"	: searchStartTime,
				"endTimeL"		: searchEndTime
			};
			ajaxManager.runJsonExt({
				url			: "monitor/loadGroupPerfBpsConnHistory.action",
				data		: params,
				successFn	: function(data)
				{			
					intervalMonitor = data.intervalMonitor;
					GenerateSvcPerfomanceBpsConnChart(data, intervalMonitor);
					selectGroupPerfMonitorTableRow(groupIndex, rowIndex, groupName);
				},
				completeFn	: function(data)
				{	
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_SVCPERFOM_LAOD, jqXhr);
					selectGroupPerfMonitorTableRow(groupIndex, rowIndex, groupName);
				}
			});
		}		
	},
	
	/// bps Connection Chart Generate
	GenerateSvcPerfomanceBpsConnChart : function(data, interval)
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
						if (data.startTime < chartDataList[0].bpsValue.occurTime)
						{
							var startTime = parseDateTime(data.startTime);
							chartData.push({occurredTime:startTime});
						}
					}
					var column = chartDataList[i];
					if (column)
					{
						if (column.bpsValue.value < 0 && column.connCurrValue.value < 0)
						{					
						}
						else if (column.bpsValue.value < 0)
						{
							var occurTime = column.bpsValue.occurTime;
							var date = parseDateTime(occurTime);							
							var secondValue = column.connCurrValue.value;
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
						else if (column.connCurrValue.value < 0)
						{
							var occurTime = column.bpsValue.occurTime;
							var date = parseDateTime(occurTime);
							var firstValue = column.bpsValue.value;							
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
							var occurTime = column.bpsValue.occurTime;
							var date = parseDateTime(occurTime);
							var firstValue = column.bpsValue.value;
							var secondValue = column.connCurrValue.value;
							var dataObject =
							{
								occurredTime : date,
								firstValue : firstValue,
								secondValue : secondValue
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
				 linecolor1 : "#72c12c",
				 linecolor2 : "#067655",
				 chartname : "GroupPerfChart",
				 axistitle1 : VAR_COMMON_BPS,
				 axistitle2 : VAR_COMMON_AFEW,
				 maxPos : null,
				 cursorColor : "#0f47c7",
				 interval : interval
			};
			obchart.OBSvcPerfomanceBpsConnChartViewer(chartData, chartOption);
        }			
	}
});