var FlbGroupPerfomanceRealServer = Class.create({
	initialize : function()
	{
		this.adc;
		this.groupIndex;
		this.selectedGroupIndex;
		this.selectedGroupName;	
		this.selectedMemberIndex;
		this.selectedMemberIp;	
		
		this.searchStartTime = undefined;
		this.searchEndTime = undefined;

		this.pageNavigator = new PageNavigator({ rowCountPerPage : 10 });
		this.orderDir = 2; // 내림차순 = 2
        this.orderType = 37;
        this.prevInfo = undefined; // 실시간 Chart 두번째 Call 부터 보내는 Row 데이터
        var _self = this;
		this.pageNavigator.onChange(function(fromRow, toRow, orderType, orderDir) {
			_self.selectedGroupIndex = undefined;
			_self.selectedGroupName = undefined;
			_self.selectedMemberIndex = undefined;
			_self.selectedMemberIp = undefined;
			_self.loadGroupMemberPerfInfoList(fromRow, toRow);
		});
		this.owner = undefined;
	},
	load : function(groupIndex, groupName, groupDisplayIndex, timeObject)
	{
		this.searchStartTime = timeObject.StartTime;
		this.searchEndTime = timeObject.EndTime;	
		this.selectedGroupIndex = groupIndex;
		this.groupIndex = groupIndex;
		this.selectedGroupName = groupName;		

		with(this)
		{
			loadGroupMemberPerfomanceContent(groupIndex, groupName, groupDisplayIndex);
		}
	},
	// 초기 페이지 Open
	loadGroupMemberPerfomanceContent : function(groupIndex, groupName, groupDisplayIndex)
	{
		this.adc = adcSetting.getAdc();
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
				"adcObject.desciption"  : adc.type
			};
			ajaxManager.runHtmlExt({
				url : "monitor/loadFLBGroupMemberPerfomanceContent.action",
				data : params,
				target : "#wrap .contents",				
				successFn : function(data)
				{					
					header.setActiveMenu('MonitorServicePerfomance');
					registGroupMemberPerfomanceContentEvents(adc);
					restoreGroupMemberServerContent();
					updateNavigator(groupIndex);
					updateHeaderNotice(groupIndex, groupName, groupDisplayIndex);
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_SVCPERFOM_MEMBERLOAD, jqXhr);
				}
			});
		}
	},
	// Total 갯수 Get, Navigator 에 업데이트
	updateNavigator : function(groupIndex) 
	{
		with (this)
		{
			if (!adc)
			{
				return;
			}
			var params =
			{
					"groupIndex" 	: groupIndex		
			};
			var _self = this;
			ajaxManager.runJsonExt
			({
				url			: "monitor/loadGroupMemberPerfTotalCount.action",
				data		: params,
				successFn	: function(data)
				{
					var totalMemberCount = 0;
					if (data.totalMemberCount)
					{
						totalMemberCount = data.totalMemberCount;
					}	
					pageNavigator.updateRowTotal(totalMemberCount);
					_self.selectedGroupIndex = undefined;
					_self.selectedGroupName = undefined;
					_self.selectedMemberIndex = undefined;
					_self.selectedMemberIp = undefined;
					
					loadGroupMemberPerfInfoList();
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_SVCPERFOM_MEMBERLOAD, jqXhr);
				}
			});
		}
	},
	// Table 데이터 성능 정보 Get
	loadGroupMemberPerfInfoList : function(fromRow, toRow, orderType, orderDir)
	{
		with (this) 
		{
			if (!adc) 
			{
				return;
			}
			if (!orderType)
			{
				orderType = 44;	
			}
			
			var params = 
			{	
				"groupIndex"					: groupIndex,
				"orderOption.orderType"			: orderType,
				"orderOption.orderDirection"	: orderDir				
			};
			params["searchOption.beginIndex"] = (undefined === fromRow ? pageNavigator.getFirstRowOfCurrentPage() : fromRow);
			params["searchOption.endIndex"] = (undefined === toRow ? pageNavigator.getLastRowOfCurrentPage() : toRow);
			
			var _self = this;
			ajaxManager.runHtmlExt
			({
				url		: "monitor/loadGroupMemberPerfInfoList.action",
				data	: params,
				target	: "table.groupMemberTrafficInfoList",
				successFn : function(data)
				{				
					registGroupMemberInfoListEvents();
					pageNavigator.refresh();
					
					orderType = undefined;
					var groupIndex = _self.selectedGroupIndex;
					var groupName = _self.selectedGroupName;
					var memberIndex = _self.selectedMemberIndex;
					var memberIp = _self.selectedMemberIp;

//					if (!_self.selectedGroupIndex && !_self.selectedMemberIndex)// 선택된게 없을때 맨처음껄 선택한다.
					if (!_self.selectedGroupIndex)// 선택된게 없을때 맨처음껄 선택한다.
					{
						groupIndex = $('#groupM_table tbody tr').eq(0).find('.groupIndex').text();
						groupName = $('#groupM_table tbody tr').eq(0).find('.groupIndex').text();
						memberIndex = $('#groupM_table tbody tr').eq(0).attr('id');
						memberIp = $('#groupM_table tbody tr').eq(0).find('.vsName').text();
					}			
					
					this.selectedGroupIndex = groupIndex;
					this.selectedGroupName = groupName;
					this.selectedMemberIndex = memberIndex;
					this.selectedMemberIp = memberIp;
					
					loadBpsConnHistoryInfo(groupIndex, undefined, groupName);												
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_SVCPERFOM_MEMBERLOAD, jqXhr);
				}
			});
		}		
	},
	updateHeaderNotice : function(groupDisplayIndex, groupName, groupDisplayIndex)
	{
		var $groupNotice = $('.contents_area .groupNotice').filter(':last');
		$groupNotice.empty();
		html = '';
		html = groupName + " (ID : " + groupDisplayIndex +")";
		$groupNotice.html(html);
	},
	restoreGroupMemberServerContent : function() 
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
	registGroupMemberPerfomanceContentEvents : function (adc)
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
				loadGroupMemberPerfInfoList();
			});	
			
			$('#exportMember').click(function(e) 
			{		
				e.preventDefault();
				checkExportGroupMemberPerfDataExist();
			});
			
			$('#serviceListLnk').click(function(e)
			{
				e.preventDefault();						
				owner.loadGroupPerfomanceContent(groupIndex);
			});
		}
	},	
	// 리스트 테이블 내에서의 이벤트 (row Click, Sorting) 
	registGroupMemberInfoListEvents : function()
	{
		with(this)
		{
			//  List Table row click 시 발생하는 이벤트
			$('#groupM_table tbody tr').click(function(e)
			{
				// Group 을 선택했을때
				if ( $(this).attr('idx') == "-1")
				{
					var groupIndex = $(this).attr('id');
					var groupName = $(this).find('.groupName').text();
					this.selectedGroupIndex = groupIndex;
					this.selectedGroupName = groupName;
					loadBpsConnHistoryInfo(groupIndex, $(this).index(), groupName);
				}
				// Member를 선택했을때
				else
				{
					var memberIndex = $(this).attr('id');
					var memberIp = $(this).find('.memberIp').text();	
					this.selectedMemberIndex = memberIndex;
					this.selectedGroupIndex = undefined;
					this.selectedGroupName = undefined;
					this.selectedMemberIp = memberIp;
					loadVsMemberHistoryInfo(memberIndex, $(this).index(), memberIp);
				}						
			});
			$('.orderDir_Desc').click(function(e)
			{
				e.preventDefault();
				orderDir =  $(this).find('.orderDir').text();
				orderType =  $(this).find('.orderType').text();					
				loadGroupMemberPerfInfoList();
			});
			
			$('.orderDir_Asc').click(function(e)
			{
				e.preventDefault();
				orderDir =  $(this).find('.orderDir').text();
				orderType =  $(this).find('.orderType').text();	
				loadGroupMemberPerfInfoList();
			});
			
			$('.orderDir_None').click(function(e)
			{
				e.preventDefault();
				orderDir =  $(this).find('.orderDir').text();
				orderType =  $(this).find('.orderType').text();	
				loadGroupMemberPerfInfoList();
			});			
		}		
	},
	
	checkExportGroupMemberPerfDataExist : function()
	{
		with(this)
		{
			if (!adc)
			{
				return;
			}
			var params = {
					"groupMemberIndex"	: this.selectedMemberIndex,
					"startTimeL"		: searchStartTime,
					"endTimeL"			: searchEndTime
				};
				ajaxManager.runJsonExt
				({
					url : "monitor/checkExportGroupMemberPerfDataExist.action",
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
			var params = "groupMemberIndex=" + this.selectedMemberIndex + "&startTimeL=" + searchStartTime + "&endTimeL=" + searchEndTime;			
			var url = "monitor/downloadGroupMemberPerfInfo.action?" + params;
			$('#downloadFrame').attr('src', url);			
		}
	},	
	// Select 테이블 Row
	selectgroupTableRow : function(groupIndex, rowIndex, groupName)
	{
		$('#groupM_table tbody tr').removeClass("vsMonitorRowSelection");		
		$('#groupM_table tbody tr').each(function(index) {
			if ($(this).attr("id") == groupIndex)
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
	// Select 테이블 Row
	selectMemberTableRow : function(MemberIndex, rowIndex, vsName, vsIp)
	{
		$('#groupM_table tbody tr').removeClass("vsMonitorRowSelection");		
		$('#groupM_table tbody tr').each(function(index) {
			if ($(this).attr("id") === MemberIndex)			
			{
				if (index === rowIndex || (!rowIndex && 0 !== rowIndex))
				{					
					$(this).addClass("vsMonitorRowSelection");
					return false;
				}
			}
		});
		// Header Notice 기능
		this.listHeaderNameChanger(vsIp);
	},	
	listHeaderNameChanger : function(vsIp)
	{
		var lisetHeaderName = vsIp;
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
				"startTimeL"    : searchStartTime,
				"endTimeL"	    : searchEndTime
			};
			ajaxManager.runJsonExt({
				url			: "monitor/loadGroupPerfBpsConnHistory.action",
				data		: params,
				successFn	: function(data)
				{		
					intervalMonitor = data.intervalMonitor;
					GenerateSvcPerfomanceBpsConnChart(data, intervalMonitor);
					selectgroupTableRow(groupIndex, rowIndex, groupName);
				},
				completeFn	: function(data)
				{	
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_SVCPERFOM_MEMBERLOAD, jqXhr);
					selectgroupTableRow(groupIndex, rowIndex, groupName);
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
			
			/* 
			 * IE8에서는 map() 지원하지 않음 
			 * The solution is jQuery.map
			 * a.map(function( ) { }); -> jQuery.map(a, function( ) { //what ever you want todo .. }
			 */
//			var maxPos = Math.max.apply(null,maxData.map(function(o){return o.value;})); 
//			var maxPos = Math.max.apply(null, $.map(maxData, function(o){return o.value;}));

			var chartOption =
			{
				 min : 0,
				 max : null,
				 linecolor1 : "#72c12c",
				 linecolor2 : "#067655",
				 chartname : "MemberPerfChart",
				 axistitle1 : VAR_COMMON_BPS,
				 axistitle2 : VAR_COMMON_AFEW,
				 maxPos : null,
				 cursorColor : "#0f47c7",
				 interval : interval
			};			
			obchart.OBSvcPerfomanceBpsConnChartViewer(chartData, chartOption);
        }			
	},
	// Member Chart Data Get
	loadVsMemberHistoryInfo : function(memberIndex, rowIndex, memberIp)
	{
		this.selectedMemberIndex = memberIndex;
		this.selectedMemberIp = memberIp;
		with (this)
		{
			if (!memberIndex)
			{
				return;
			}
			var params = {
				"groupMemberIndex"		: memberIndex	,
				"startTimeL"			: searchStartTime,
				"endTimeL"				: searchEndTime
			};
			ajaxManager.runJsonExt({
				url			: "monitor/loadGroupMemberPerfBpsConnHistory.action",
				data		: params,
				successFn	: function(data)
				{				
					intervalMonitor = data.intervalMonitor;
					GenerateMemberBpsConnChart(data, intervalMonitor);					
					selectMemberTableRow(memberIndex, rowIndex, undefined, memberIp);
				},
				completeFn	: function(data)
				{	
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_SVCPERFOM_MEMBERLOAD, jqXhr);
					selectMemberTableRow(memberIndex, rowIndex, undefined, memberIp);
				}
			});
		}		
	},	
	
	/// Member bps Connection Chart Generate
	GenerateMemberBpsConnChart : function(data, interval)
	{
		with(this)
		{
			var ConfigDate = [];
			var maxData = [];
			var chartData = [];
			var chartDataList = [];
			if (data.groupMemberBpsConnHistoryInfo != null)
			{
				chartDataList = data.groupMemberBpsConnHistoryInfo;
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
			
			/* 
			 * IE8에서는 map() 지원하지 않음 
			 * The solution is jQuery.map
			 * a.map(function( ) { }); -> jQuery.map(a, function( ) { //what ever you want todo .. }
			 */
//			var maxPos = Math.max.apply(null,maxData.map(function(o){return o.value;})); 
//			var maxPos = Math.max.apply(null, $.map(maxData, function(o){return o.value;}));			
			var chartOption =
			{
				 min : 0,
				 max : null,
				 linecolor1 : "#72c12c",
				 linecolor2 : "#067655",
				 chartname : "MemberPerfChart",
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