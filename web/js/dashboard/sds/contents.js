var SdsDashboardContent = Class.create({
	initialize : function() 
	{
		this.ajaxManager = new FlowitAjax();
		this.owner = undefined;		
		this.awaitingTimer = undefined;
		this.selectedAdcIndex = undefined;
		this.selectedVsIndex = undefined;
		this.selectedVsPort = undefined;
		this.searchIntervalHours = 12;
		this.orderDir  = 2; //2는 내림차순
		this.orderType = 8;// adcName = 8
		this.adcMonitoringRefreshFlag = false;
		this.vsMonitoringRefreshFlag = false;
		this.AdcTrafficChartObj = undefined;
		this.AdcSystemUsageChartObj = undefined;
		this.VsStatusChartObj = undefined;
		this.VsTrafficChartObj = undefined;
		this.VsSystemUsageChartObj = undefined;
		this.vsMemberConnectionChartObj = undefined;
		this.AdcTrafficChartFlag = false;
		this.AdcSystemUsageChartFlag = false;
		this.VsStatusChartFlag = false;
		this.VsTrafficChartFlag = false;
		this.VsSystemUsageChartFlag = false;
		this.vsMemberConnectionChartFlag = false;
	},
	load : function(params, orderType, orderDir)
	{
		with (this) 
		{
			if (!params.restore) 
			{
				selectedAdcIndex = undefined;
				selectedVsIndex = undefined;
				selectedVsPort = undefined;
			}
			if (!params.statusGroup || '0' == params.statusGroup) 
			{
				orderType = 8; //ADCNAME
				loadAdcSummary(params.selectedType, params.groupIndex, params.adc, params.status, params.faultMaxDays, orderType, orderDir);					
			} 
			else if ('1' == params.statusGroup) 
			{
				orderType = 2;//VSIPADRESS
				loadVsSummary(params.selectedType, params.groupIndex, params.adc, params.status, params.faultMaxDays, orderType, orderDir);
			} 
			else if ('2' == params.statusGroup) 
			{
				orderType = 11; // OCCURTIME
				loadFaultLog(params.selectedType, params.groupIndex, params.adc, params.status, params.faultMaxDays, orderType, orderDir);
			}
		}
	},	
	registerAdc : function(params)
	{
		$('.allAdc').click(function(params)
		{
			status = $(this).find('.status').text();
			loadAdcSummary(selectedType, groupIndex, adc, status, faultMaxDays,orderType, orderDir);		
		});
	},	
	loadAdcSummary : function(selectedType, groupIndex, adc, status, faultMaxDays, orderType, orderDir) 
	{
		with (this) 
		{
			if (!selectedType) 
			{
				return;
			}
			
			var params = 
			{
				'selectedNode.nodeType' : selectedType,
				'orderType'	: orderType,
				'orderDir' : orderDir			
			};
			if (groupIndex) 
			{
				params['selectedNode.groupIndex'] = groupIndex;
			}
			if (adc && adc.index) 
			{
				params['selectedNode.adcIndex'] = adc.index;
			}		

			if (status) 
			{
				params['status'] = status;
			}			
			if (faultMaxDays)
			{
				params['faultMaxDays'] = faultMaxDays;
			}
			
			ajaxManager.runHtml({
				url			: 'dashboard/sds/loadAdcSummary.action',
				target		: '.list_area',
				data		: params,
				successFn	: function(data) 
				{
					applyAdcSummaryCss(params);
					registAdcSummaryEvents(selectedType, groupIndex, adc, status, faultMaxDays, orderType, orderDir);
					restoreAdcMonitoring();
					$('.adcSummary').click(function(params)
					{
						status = $(this).find('.status').text();						
						loadAdcSummary(selectedType, groupIndex, adc, status, faultMaxDays, orderType, orderDir);		
					});					
				}
			});
		}
	},
	loadVsSummary : function(selectedType, groupIndex, adc, status, faultMaxDays, orderType, orderDir)
//	loadVsSummary : function(selectedType, groupIndex, adc, status, vsUnavailableStatusMinDays) 
	{
		with (this) {
			if (!selectedType)
			{
				return;
			}
			
			var params = 
			{
				'selectedNode.nodeType' : selectedType,
				'orderType'	: orderType,
				'orderDir' : orderDir				
			};
			if (groupIndex) 
			{
				params['selectedNode.groupIndex'] = groupIndex;
			}
			if (adc && adc.index) 
			{
				params['selectedNode.adcIndex'] = adc.index;
			}
			if (status) 
			{
				params['status'] = status;
			}
			if (faultMaxDays)
			{
				params['faultMaxDays'] = faultMaxDays;
			}
			
			ajaxManager.runHtml({
				url			: 'dashboard/sds/loadVsSummary.action',
				target		: '.list_area',
				data		: params,
				successFn	: function(data) 
				{
					applyVsSummaryCss(params);
					registVsSummaryEvents(selectedType, groupIndex, adc, status, faultMaxDays, orderType, orderDir);
					restoreVsMonitoring();
					$('.vsSummary').click(function(params)
					{
						status = $(this).find('.status').text();
						loadVsSummary(selectedType, groupIndex, adc, status, faultMaxDays, orderType, orderDir);		
					});	
				}
			});
		}
	},
	loadFaultLog : function(selectedType, groupIndex, adc, status, faultMaxDays, orderType, orderDir) 
	{
		with (this) 
		{
			if (!selectedType) 
			{
				return;
			}
			
			var params = 
			{
				'selectedNode.nodeType' : selectedType,
				'orderType'	: orderType,
				'orderDir' : orderDir	
			};
			if (groupIndex) 
			{
				params['selectedNode.groupIndex'] = groupIndex;
			}
			if (adc) 
			{
				params['selectedNode.adcIndex'] = adc.index;
			}
			if (status) 
			{
				params['status'] = status;
			}
			if (faultMaxDays) 
			{
				params['faultMaxDays'] = faultMaxDays;
			}
			ajaxManager.runHtml({
				url			: 'dashboard/sds/loadFaultLog.action',
				target		: '.list_area',
				data		: params,
				successFn	: function(data) 
				{
					applyFaultLogCss(params);
					registFaultLogEvents(selectedType, groupIndex, adc, status, faultMaxDays, orderType, orderDir);
					if ($('.faultLogListRow').size() > 0) 
					{
						$('.faultLogListRow').eq(0).click();
					} 
					else 
					{
						// 최초 한번 Load 된 후에는 adcMonitoringRefreshFlag 가 True로 변경되며,
						// Refresh 함수를 타도록 한다. (Chart Data Insert)
						if(adcMonitoringRefreshFlag == false)
						{
							loadAdcMonitoring();
						}
						else
						{
							loadAdcMonitoringRefresh();
						}
					}
					$('.faultLog').click(function(params)
					{
						status = $(this).find('.status').text();
						loadFaultLog(selectedType, groupIndex, adc, status, faultMaxDays, orderType, orderDir);		
					});	
				}
			});
		}
	},
	loadAdcMonitoring : function(adc) 
	{
		var $chartarea = $('.content_wrap .chart_area').filter(':last');		
		$chartarea.empty();
		with (this) 
		{			
			_self = this;
			ajaxManager.runHtml({
				url			: 'dashboard/sds/loadAdcMonitoring.action',
				target		: '.graph_area',				
				successFn	: function(data) 
				{
					loadSelectAdcInfo(adc);
					loadAdcTrafficInfo(adc);
					loadAdcSystemUsageInfo(adc);
					loadVsStatusDataList(adc);
					selectAdcSummaryListRow(adc);
					_self.owner.resetRefreshTimer();					
				}
			});
			adcMonitoringRefreshFlag = true;
			vsMonitoringRefreshFlag = false;
			VsTrafficChartFlag = false;
			VsSystemUsageChartFlag = false;
			vsMemberConnectionChartFlag = false;
		}
	},	
	loadAdcMonitoringRefresh : function(adc)
	{
		with(this)
		{
			loadSelectAdcInfo(adc);
			loadAdcTrafficInfo(adc);
			loadAdcSystemUsageInfo(adc);
			loadVsStatusDataList(adc);
			selectAdcSummaryListRow(adc);
			_self.owner.resetRefreshTimer();
		}
	},
	loadVsMonitoring : function(adc, vsType, vsIndex, vsPort) 
	{
		var $chartarea = $('.content_wrap .chart_area').filter(':last');		
		$chartarea.empty();
		with (this) 
		{
			ajaxManager.runHtml({
				url			: 'dashboard/sds/loadVsMonitoring.action',
				target		: '.graph_area',				
				successFn	: function(params) 
				{
					loadSelectVsInfo(adc, vsType, vsIndex, vsPort);
					loadVsTrafficInfo(adc, vsType, vsIndex, vsPort);
					loadVsSystemUsageInfo(adc);
					loadVsMemberConnectionDataList(adc, vsType, vsIndex, vsPort);
					selectVsSummaryListRow(vsIndex, vsPort);
					_self.owner.resetRefreshTimer();
				}
			});
			vsMonitoringRefreshFlag = true;
			adcMonitoringRefreshFlag = false;
			AdcTrafficChartFlag = false;
			AdcSystemUsageChartFlag = false;
			VsStatusChartFlag = false;			
		}
	},
	loadVsMonitoringRefresh : function(adc, vsType, vsIndex, vsPort)
	{
		with(this)
		{
			loadSelectVsInfo(adc, vsType, vsIndex, vsPort);
			loadVsTrafficInfo(adc, vsType, vsIndex, vsPort);
			loadVsSystemUsageInfo(adc);
			loadVsMemberConnectionDataList(adc, vsType, vsIndex, vsPort);
			selectVsSummaryListRow(vsIndex, vsPort);
			_self.owner.resetRefreshTimer();
		}
	},
	loadSelectAdcInfo : function(adc)
	{
		with(this)
		{
			var params = {};
			if (adc && adc.index) 
			{
				params['adc.index'] = adc.index;
			}	
			_self = this;
			ajaxManagerOB.runJson({
				url			: 'dashboard/sds/loadSelectAdcInfo.action',				
				data		: params,
				successFn	: function(data) 
				{
					if (data.adcInfo != null)
					{
						var selectAdcIp = data.adcInfo.ip;
						
						var $tbody = $('.content_wrap .title_h6').filter(':last');
						$tbody.empty();
						var html = '';
						
						html += "<li>선택한 ADC 이름:" + selectAdcIp + "</li>";
						
						$tbody.html(html);
					}
					else if (data.adcInfo == null)
					{
						var $tbody = $('.content_wrap .title_h6').filter(':last');
						$tbody.empty();
						var html = '';
						
						html += "<li>선택한 ADC 이름:</li>";
						
						$tbody.html(html);
						
						var $chartbody1 = $('.content_wrap .grapharea1').filter(':last');
						var $chartbody2 = $('.content_wrap .grapharea2').filter(':last');
						var $chartbody3 = $('.content_wrap .grapharea3').filter(':last');
						$chartbody1.empty();$chartbody2.empty();$chartbody3.empty();
						adcMonitoringRefreshFlag = false;
						AdcTrafficChartFlag = false;
						AdcSystemUsageChartFlag = false;
						VsStatusChartFlag = false;
					}
				}
			});			
		}		
	},
	loadSelectVsInfo : function(adc, vsType, vsIndex, vsPort)
	{
		with(this)
		{
			var params = {};			
			if (adc && adc.index) 
			{
				params['adc.index'] = adc.index;
			}
			if (vsType) 
			{
				params.vsType = vsType;
			}
			if (vsIndex) 
			{
				params.vsIndex = vsIndex;
			}
			if (vsPort) 
			{
				params.vsPort = vsPort;
			}
			_self = this;
			ajaxManagerOB.runJson({
				url			: 'dashboard/sds/loadSelectVsInfo.action',				
				data		: params,
				successFn	: function(data) 
				{
					if (data.vsInfo != null)
					{					
						var selectVsIp = data.vsInfo.ip;
						var selectVsName = data.vsInfo.name;
						
						var $tbody = $('.content_wrap .title_h6').filter(':last');
						$tbody.empty();
						var html = '';
						
						html += "<li>선택한 Virtual Server IP(이름) :" + selectVsIp + "("+ selectVsName +")</li>";
						
						$tbody.html(html);
					}
					else if (data.vsInfo == null)
					{
						var $tbody = $('.content_wrap .title_h6').filter(':last');
						$tbody.empty();
						var html = '';
						
						html += "<li>선택한 Virtual Server IP(이름) :</li>";
						
						$tbody.html(html);
						
						var $chartbody1 = $('.content_wrap .grapharea1').filter(':last');
						var $chartbody2 = $('.content_wrap .grapharea2').filter(':last');
						var $chartbody3 = $('.content_wrap .grapharea3').filter(':last');
						$chartbody1.empty();$chartbody2.empty();$chartbody3.empty();
						vsMonitoringRefreshFlag = false;
						VsTrafficChartFlag = false;
						VsSystemUsageChartFlag = false;
						vsMemberConnectionChartFlag = false;
					}
				}
			});			
		}		
	},	
	loadAdcTrafficInfo : function(adc) 
	{
		with (this) 
		{
			//FlowitUtil.log('adcIndex: %s', adc.index);
			if (!adc || !adc.index) 
			{
				return;
			}
			var params = { 'adc.index'	: adc.index };
			if (0 === searchIntervalHours || searchIntervalHours) 
			{
				params.hour = searchIntervalHours;
			}
			ajaxManager.runJson({
				url			: 'dashboard/sds/loadAdcTrafficInfo.action',
				data		: params,
				successFn	: function(data) 
				{
					GenerateAdcTrafficChartData(data);
				}
			});	
		}
	},
	loadVsTrafficInfo : function(adc, vsType, vsIndex, vsPort) 
	{
		with (this)
		{
			if (!vsType || !adc || !adc.index || !vsIndex) 
			{
				return;
			}
			var params = 
			{
				'adc.index'	: adc.index,
				'vsType'	: vsType,
				'vsIndex'	: vsIndex,
				'vsPort'	: vsPort
			};
			if (0 === searchIntervalHours || searchIntervalHours) 
			{
				params.hour = searchIntervalHours;
			}
			ajaxManager.runJson({
				url			: 'dashboard/sds/loadVsTrafficInfo.action',
				data		: params,
				successFn	: function(data) 
				{
					GenerateVsTrafficChartData(data);
				}
			});
		}
	},
	loadAdcSystemUsageInfo : function(adc) 
	{
		with (this) 
		{
			//FlowitUtil.log('adcIndex: %s', adc.index);
			if (!adc || !adc.index) 
			{
				return;
			}
			var params = { 'adc.index' : adc.index };
			if (0 === searchIntervalHours || searchIntervalHours) 
			{
				params.hour = searchIntervalHours;
			}
			ajaxManager.runJson({
				url			: 'dashboard/sds/loadAdcSystemUsageInfo.action',
				data		: params,
				successFn	: function(data) 
				{
					GenerateAdcSystemUsageChartData(data);
				}
			});	
		}
	},
	loadVsSystemUsageInfo : function(adc) 
	{
		with (this) 
		{
			//FlowitUtil.log('adcIndex: %s', adc.index);
			if (!adc || !adc.index) 
			{
				return;
			}
			var params = { 'adc.index' : adc.index };
			if (0 === searchIntervalHours || searchIntervalHours) 
			{
				params.hour = searchIntervalHours;
			}
			ajaxManager.runJson({
				url			: 'dashboard/sds/loadAdcSystemUsageInfo.action',
				data		: params,
				successFn	: function(data) 
				{
					GenerateVsSystemUsageChartData(data);
				}
			});	
		}
	},
	loadVsStatusDataList : function(adc) 
	{
		with (this) 
		{
			//FlowitUtil.log('adcIndex: %s', adc.index);
			if (!adc || !adc.index) 
			{
				return;
			}
			var params = { 'adc.index' : adc.index };
			if (0 === searchIntervalHours || searchIntervalHours) 
			{
				params.hour = searchIntervalHours;
			}
			ajaxManager.runJson({
				url			: 'dashboard/sds/loadVsStatusDataList.action',
				data		: params,
				successFn	: function(data) 
				{
					GenerateVsStatusChartData(data);
				}
			});	
		}
	},
	loadVsMemberConnectionDataList : function(adc, vsType, vsIndex, vsPort) 
	{
		with (this) 
		{
			//FlowitUtil.log('adcIndex: %s, vsType: %s, vsIndex: %s, vsPort: %s', adc.index, vsType, vsIndex, vsPort);
			if (!adc || !adc.index || !vsIndex) 
			{
				return;
			}
			var params = 
			{
				'adc.index'	: adc.index,
				'vsType'	: vsType,
				'vsIndex'	: vsIndex,
				'vsPort'	: vsPort
			};
			if (0 === searchIntervalHours || searchIntervalHours) 
			{
				params.hour = searchIntervalHours;
			}
			ajaxManager.runJson({
				url			: 'dashboard/sds/loadVsMemberConnectionDataList.action',
				data		: params,
				successFn	: function(data) 
				{
					GeneratevsMemberConnectionChartData(data);
				}
			});
		}
	},
	restoreAdcMonitoring : function() 
	{
		with (this) 
		{
			var found = undefined;
			if (selectedAdcIndex) 
			{
				$('.adcSummaryListRow').each(function() 
				{
					if ($(this).find('.adcIndex').text() == selectedAdcIndex) 
					{
						found = true;
						return false;
					}
				});
				if (found) 
				{
					// 최초 한번 Load 된 후에는 adcMonitoringRefreshFlag 가 True로 변경되며,
					// Refresh 함수를 타도록 한다. (Chart Data Insert)
					if (adcMonitoringRefreshFlag == false)
					{
						loadAdcMonitoring({ 'index' : selectedAdcIndex });
					}
					else
					{
						loadAdcMonitoringRefresh({ 'index' : selectedAdcIndex });
					}
				}
			}
			if (!found) 
			{
				if ($('.adcSummaryListRow').size() > 0) 
				{
					var adcIndex = $('.adcSummaryListRow').eq(0).find('.adcIndex').text();
					//FlowitUtil.log('adcIndex: %s', adcIndex);
					// 최초 한번 Load 된 후에는 adcMonitoringRefreshFlag 가 True로 변경되며,
					// Refresh 함수를 타도록 한다. (Chart Data Insert)
					if (adcMonitoringRefreshFlag == false)
					{
						loadAdcMonitoring({ 'index' : adcIndex });
					}
					else
					{
						loadAdcMonitoringRefresh({ 'index' : adcIndex });
					}
				} 
				else 
				{
					if (adcMonitoringRefreshFlag == false)
					{
						loadAdcMonitoring();
					}
					else
					{
						loadAdcMonitoringRefresh();
					}
				}
			}
		}
	},
	restoreVsMonitoring : function() 
	{
		with (this) 
		{
			var found = undefined;
			if (selectedVsIndex) 
			{
				$('.vsSummaryListRow').each(function() 
				{
					if ($(this).find('.vsIndex').text() == selectedVsIndex) 
					{
						var vsPort = $.trim($(this).find('.vsPort').text());
						if (!selectedVsPort || vsPort == selectedVsPort) 
						{
							var adcType = $(this).find('.adcType').text();
							var adcIndex = $(this).find('.adcIndex').text();
							var vsType = $(this).find('.vsType').text();
							var vsIndex = $(this).find('.vsIndex').text();
							
							//FlowitUtil.log('adcType: %s, adcIndex: %s, vsType: %s, vsIndex: %s, vsPort: %s', adcType, adcIndex, vsType, vsIndex, vsPort);
							if ('F5' == adcType) 
							{
								if (vsMonitoringRefreshFlag == false)
								{
									loadVsMonitoring({ 'index' : adcIndex }, vsType, vsIndex);
								}
								else
								{
									loadVsMonitoringRefresh({ 'index' : adcIndex }, vsType, vsIndex);
								}
												
							} 
							else if ('Alteon' == adcType) 
							{
								if (vsMonitoringRefreshFlag == false)
								{
									loadVsMonitoring({ 'index' : adcIndex }, vsType, vsIndex, vsPort);
								}
								else
								{
									loadVsMonitoringRefresh({ 'index' : adcIndex }, vsType, vsIndex, vsPort);
								}
							}
							else if ('PAS' == adcType) 
							{
								if (vsMonitoringRefreshFlag == false)
								{
									loadVsMonitoring({ 'index' : adcIndex }, vsType, vsIndex);
								}
								else
								{
									loadVsMonitoringRefresh({ 'index' : adcIndex }, vsType, vsIndex);
								}
							} 
							else if ('PASK' == adcType) 
							{
								if (vsMonitoringRefreshFlag == false)
								{
									loadVsMonitoring({ 'index' : adcIndex }, vsType, vsIndex);
								}
								else
								{
									loadVsMonitoringRefresh({ 'index' : adcIndex }, vsType, vsIndex);
								}
							} 
							found = true;
							return false;
						}
					}
				});
			}
			if (!found) 
			{
				if ($('.vsSummaryListRow').size() > 0) 
				{
					var $row = $('.vsSummaryListRow').eq(0);
					var adcType = $row.find('.adcType').text();
					var adcIndex = $row.find('.adcIndex').text();
					var vsType = $row.find('.vsType').text();
					var vsIndex = $row.find('.vsIndex').text();
					var vsPort = $.trim($row.find('.vsPort').text());
					//FlowitUtil.log('adcType: %s, adcIndex: %s, vsType: %s, vsIndex: %s, vsPort: %s', adcType, adcIndex, vsType, vsIndex, vsPort);
					if ('F5' == adcType) 
					{
						if (vsMonitoringRefreshFlag == false)
						{
							loadVsMonitoring({ 'index' : adcIndex }, vsType, vsIndex, vsPort);
						}
						else
						{
							loadVsMonitoringRefresh({ 'index' : adcIndex }, vsType, vsIndex, vsPort);
						}
					} 
					else if ('Alteon' == adcType) 
					{
						if (vsMonitoringRefreshFlag == false)
						{
							loadVsMonitoring({ 'index' : adcIndex }, vsType, vsIndex, vsPort);
						}
						else
						{
							loadVsMonitoringRefresh({ 'index' : adcIndex }, vsType, vsIndex, vsPort);
						}
					}
					else if ('PAS' == adcType) 
					{
						if (vsMonitoringRefreshFlag == false)
						{
							loadVsMonitoring({ 'index' : adcIndex }, vsType, vsIndex, vsPort);
						}
						else
						{
							loadVsMonitoringRefresh({ 'index' : adcIndex }, vsType, vsIndex, vsPort);
						}
					} 
					else if ('PASK' == adcType) 
					{
						if (vsMonitoringRefreshFlag == false)
						{
							loadVsMonitoring({ 'index' : adcIndex }, vsType, vsIndex, vsPort);
						}
						else
						{
							loadVsMonitoringRefresh({ 'index' : adcIndex }, vsType, vsIndex, vsPort);
						}
					} 
				} 
				else 
				{
					if (vsMonitoringRefreshFlag == false)
					{
						loadVsMonitoring();
					}
					else
					{
						loadVsMonitoringRefresh();
					}
				}
			}		
		}
	},
	selectAdcSummaryListRow : function(adc) 
	{
		with (this) 
		{
			var selected = undefined;
			$('.adcSummaryListRow').removeClass("dashboardLogRowSelection");
			$('.adcSummaryListRow').each(function() 
			{
				if (adc.index == $(this).find('.adcIndex').text()) 
				{
					$(this).addClass("dashboardLogRowSelection");
					selected = true;
					return false;
				}
			});
			if (selected) 
			{
				selectedAdcIndex = adc.index;				
			}
			else 
			{
				selectedAdcIndex = undefined;
			}
		}
	},
	selectVsSummaryListRow : function(vsIndex, vsPort) 
	{
		with (this) 
		{
			var selected = undefined;
			$('.vsSummaryListRow').removeClass("dashboardLogRowSelection");
			$('.vsSummaryListRow').each(function() 
			{
				if ($(this).find('.vsIndex').text() == vsIndex) 
				{
					if (!vsPort || $.trim($(this).find('.vsPort').text()) == vsPort) 
					{
						$(this).addClass("dashboardLogRowSelection");
						selected = true;
						return false;
					}
				}
			});
			if (selected) 
			{
				selectedVsIndex = vsIndex;
				selectedVsPort = vsPort;
			} 
			else 
			{
				selectedVsIndex = undefined;
				selectedVsPort = undefined;
			}
		}
	},
	// ADC 요약 JS 차트 함수 시작.
	
	// ADC TrafficChart (Connection / Throughput)
	GenerateAdcTrafficChartData : function(data)
	{
		with(this)
		{
			var chartData = [];
			if (data.adcConnectionInfo !=null &&
					data.adcConnectionInfo.adcConnectionDataList.length > 0 &&
					data.adcConnectionInfo.adcConnectionDataList != null &&
					data.adcConnectionInfo.adcConnectionDataList != undefined )
			{
			
			 var chartDataList = data.adcConnectionInfo.adcConnectionDataList;
			 var chartDataList2 = data.adcThroughputInfo.adcThroughputDataList;
			 
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
					var column2 = chartDataList2[i];
					if (column && column2)
					{
						if (column.connections < 0 && column2.bps < 0)
						{						
						}
						else if (column.connections < 0)
						{
							var occurTime = column.occurredTime;
							var date = parseDateTime(occurTime);
							var secondValue = column2.bps;
							var dataObject =
							{
								occurredTime : date,							
								secondValue : secondValue										
							};
							// add object to dataProvider array
							chartData.push(dataObject);
						}
						else if (column2.bps < 0)
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
						else
						{
							var occurTime = column.occurredTime;
							var date = parseDateTime(occurTime);
							var firstValue = column.connections;
							var secondValue = column2.bps;

							var dataObject =
							{
								occurredTime : date,
								firstValue : firstValue,
								secondValue : secondValue										
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
							chartData.push({occurredTime:endTime, firstValue:null, secondValue:null});								 
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
			var linecolor1 = "#FF6600";
			var linecolor2 = "#3C58BC";
			var chartname = "AdcSummaryDashBoardTrafficChart";
			if (AdcTrafficChartFlag == false)
			{
				AdcTrafficChartObj = obchart.OBAdcSummaryDashBoardTrafficChartViewer(chartData, min, max, linecolor1, linecolor2, chartname);
				AdcTrafficChartFlag = true;
			}
			else
			{
				obchart.OBAllChartUpdate(AdcTrafficChartObj, chartData);				
			}
		}
	},
	// ADC SystemUsageChartData (CPU / Memory)
	GenerateAdcSystemUsageChartData : function(data)
	{
		with(this)
		{
		 var chartData = [];
		 if (data.adcCpuInfo !=null &&
				 data.adcCpuInfo.adcCpuDataList.length > 0 &&
				 data.adcCpuInfo.adcCpuDataList != null &&
				 data.adcCpuInfo.adcCpuDataList != undefined)
		 {
		 var chartDataList = data.adcCpuInfo.adcCpuDataList;
		 var chartDataList2 = data.adcMemoryInfo.adcMemoryDataList;
		 
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
				var column2 = chartDataList2[i];
				if (column && column2)
				{
					if (column.usage < 0 && column2.usage < 0)
					{						
					}
					else if (column.usage < 0)
					{
						var occurTime = column.occurredTime;
						var date = parseDateTime(occurTime);
						var secondValue = column2.usage;
						var dataObject =
						{
							occurredTime : date,							
							secondValue : secondValue										
						};
						// add object to dataProvider array
						chartData.push(dataObject);
					}
					else if (column2.usage < 0)
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
					else
					{
						var occurTime = column.occurredTime;
						var date = parseDateTime(occurTime);					
						var firstValue = column.usage;
			        	var secondValue = column2.usage;
			        	var dataObject =
						{
							occurredTime : date,
							firstValue : firstValue,
							secondValue : secondValue										
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
						chartData.push({occurredTime:endTime, firstValue:null, secondValue:null});								 
					}
				}
			}
		 }else
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
			var max = 100;
			var linecolor1 = "#FF0000";
			var linecolor2 = "#02BC1C";
			var chartname = "AdcSummaryDashBoardSystemUsageChart";
			if (AdcSystemUsageChartFlag == false)
			{
				AdcSystemUsageChartObj = obchart.OBAdcSummaryDashBoardSystemUsageChartViewer(chartData, min, max, linecolor1, linecolor2, chartname);
				AdcSystemUsageChartFlag = true;
			}
			else
			{
				obchart.OBAllChartUpdate(AdcSystemUsageChartObj, chartData);			
			}
		}
	},
	
	//vsStatusChart (정상 / 단절 / 꺼짐)
	GenerateVsStatusChartData : function(data)
	{
		with(this)
		{
			var chartData = [];
			if (data.vsStatusDataList.length > 0 &&
					 data.vsStatusDataList != null &&
					 data.vsStatusDataList != undefined)
			{
			var chartDataList = data.vsStatusDataList;		 
			 
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
			var min = 0;
			var max = null;
			var linecolor1 = "#02BC1C";
			var linecolor2 = "#FF0000"; 
			var linecolor3 = "#a29e62";
			var chartname = "AdcSummaryDashBoardVsStatusChart";
			if (VsStatusChartFlag == false)
			{
				VsStatusChartObj = obchart.OBAdcSummaryDashBoardVsStatusChartViewer(chartData, min, max, linecolor1, linecolor2, linecolor3, chartname);
				VsStatusChartFlag = true;
			}
			else
			{
				obchart.OBAllChartUpdate(VsStatusChartObj, chartData);
			}
		}			
	},
	// VS TrafficChart (Connection / Throughput)
	GenerateVsTrafficChartData : function(data)
	{
		with(this)
		{
			var chartData = [];
			if (data.adcConnectionInfo !=null &&
					data.adcConnectionInfo.adcConnectionDataList.length > 0 &&
					data.adcConnectionInfo.adcConnectionDataList != null &&
					data.adcConnectionInfo.adcConnectionDataList != undefined )
			{
			
			 var chartDataList = data.adcConnectionInfo.adcConnectionDataList;
			 var chartDataList2 = data.adcThroughputInfo.adcThroughputDataList;
			 
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
					var column2 = chartDataList2[i];
					if (column && column2)
					{
						if (column.connections < 0 && column2.bps < 0)
						{						
						}
						else if (column.connections < 0)
						{
							var occurTime = column.occurredTime;
							var date = parseDateTime(occurTime);
							var secondValue = column2.bps;
							var dataObject =
							{
								occurredTime : date,							
								secondValue : secondValue										
							};
							// add object to dataProvider array
							chartData.push(dataObject);
						}
						else if (column2.bps < 0)
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
						else
						{
							var occurTime = column.occurredTime;
							var date = parseDateTime(occurTime);
							var firstValue = column.connections;
							var secondValue = column2.bps;

							var dataObject =
							{
								occurredTime : date,
								firstValue : firstValue,
								secondValue : secondValue										
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
							chartData.push({occurredTime:endTime, firstValue:null, secondValue:null});								 
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
			var linecolor1 = "#FF6600";
			var linecolor2 = "#3C58BC";
			var chartname = "AdcSummaryDashBoardTrafficChart";
			if (VsTrafficChartFlag == false)
			{
				VsTrafficChartObj = obchart.OBAdcSummaryDashBoardTrafficChartViewer(chartData, min, max, linecolor1, linecolor2, chartname);
				VsTrafficChartFlag = true;
			}
			else
			{
				obchart.OBAllChartUpdate(VsTrafficChartObj, chartData);				
			}
		}
	},
	// VS SystemUsageChartData (CPU / Memory)
	GenerateVsSystemUsageChartData : function(data)
	{
		with(this)
		{
		 var chartData = [];
		 if (data.adcCpuInfo !=null &&
				 data.adcCpuInfo.adcCpuDataList.length > 0 &&
				 data.adcCpuInfo.adcCpuDataList != null &&
				 data.adcCpuInfo.adcCpuDataList != undefined)
		 {
		 var chartDataList = data.adcCpuInfo.adcCpuDataList;
		 var chartDataList2 = data.adcMemoryInfo.adcMemoryDataList;
		 
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
				var column2 = chartDataList2[i];
				if (column && column2)
				{
					if (column.usage < 0 && column2.usage < 0)
					{					
					}
					else if (column.usage < 0)
					{
						var occurTime = column.occurredTime;
						var date = parseDateTime(occurTime);
						var secondValue = column2.usage;
						var dataObject =
						{
							occurredTime : date,							
							secondValue : secondValue										
						};
						// add object to dataProvider array
						chartData.push(dataObject);
					}
					else if (column2.usage < 0)
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
					else
					{
						var occurTime = column.occurredTime;
						var date = parseDateTime(occurTime);					
						var firstValue = column.usage;
			        	var secondValue = column2.usage;
			        	var dataObject =
						{
							occurredTime : date,
							firstValue : firstValue,
							secondValue : secondValue										
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
						chartData.push({occurredTime:endTime, firstValue:null, secondValue:null});								 
					}
				}
			}
		 }else
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
			var max = 100;
			var linecolor1 = "#FF0000";
			var linecolor2 = "#02BC1C";
			var chartname = "AdcSummaryDashBoardSystemUsageChart";
			if (VsSystemUsageChartFlag == false)
			{
				VsSystemUsageChartObj = obchart.OBAdcSummaryDashBoardSystemUsageChartViewer(chartData, min, max, linecolor1, linecolor2, chartname);
				VsSystemUsageChartFlag = true;
			}
			else
			{
				obchart.OBAllChartUpdate(VsSystemUsageChartObj, chartData);			
			}
		}
	},
	
	// vsMemberConnectionChart (Member Connection 통계)
	GeneratevsMemberConnectionChartData : function(data)
	{
		with(this)
		{
		var chartData = [];
		if (data.vsMemberConnectionDataList.length > 0 &&
				 data.vsMemberConnectionDataList != null &&
				 data.vsMemberConnectionDataList != undefined)
		{
		var chartDataList = data.vsMemberConnectionDataList;		 
		 
		for ( var i = 0; i < chartDataList.length; i++)
		{
			var column = chartDataList[i];				
			if (column)
			{
				var memberIP = column.memberIp;
				//var date = parseDateTime(occurTime);
				var firstValue = column.connections;		        	
				var dataObject =
				{
					occurredTime : memberIP,
					firstValue : firstValue,						
				};
				// add object to dataProvider array
				chartData.push(dataObject);
				}
			}
		}
		else
		{
			var dataObject =
				{
					occurredTime : "None Data",
					firstValue : null,						
				};
				// add object to dataProvider array
				chartData.push(dataObject);			 			 
		}
			var min = 0;
			var max = null;
			var linecolor1 = "#1C50B6";			
			var chartname = "AdcSummaryDashBoardVsMemberConnectionChart";
			
			if (vsMemberConnectionChartFlag == false)
			{
				vsMemberConnectionChartObj = obchart.OBAdcSummaryDashBoardVsMemberConnectionChartViewer(chartData, min, max, linecolor1, chartname);
				vsMemberConnectionChartFlag = true;
			}
			else
			{
				obchart.OBAllChartUpdate(vsMemberConnectionChartObj, chartData);
			}
		}
	},
	applyAdcSummaryCss : function(params) 
	{
		with (this) 
		{
			//테이블 원본 저장
			table1 = $('#table1').html();
			// table2 스타일 정의
			$('#table1').fixheadertable({
				height : 154,
				colratio : [ -1, 15, 5, 14, 30, 10, 10, 8, 8 ]
			});
			tableHeadSize('#table1', {
				'col' : '8',
				'hsize' : '7'
			});
			initTable([ '#table1 tbody tr' ], [ -1 ], [ -1 ]);
			
			if (params.status == null) 
			{
				$('.adcSummary').eq(0).css('background-color', 'white');
			} 
			else 
			{
				$('.adcSummary').each(function() 
				{
					if ($(this).find('.status').text() === String(params.status)) 
					{
						$(this).css('background-color', '#61d0fb');
					}
				});
			}
		}
	},
	applyVsSummaryCss : function(params) 
	{
		with (this) 
		{
			// 테이블 원본 저장
			table1 = $('#table1').html();
			// table2 스타일 정의
			$('#table1').fixheadertable({
				height : 145,
				colratio : [ -1, 14, 5, 5, 17, 11, 11, 21, 8, 8 ]
			});
			tableHeadSize('#table1', {
				'col' : '9',
				'hsize' : '7'
			});
			initTable([ '#table1 tbody tr' ], [ -1 ], [ -1 ]);
			
			if (params.status == null) 
			{
				$('.vsSummary').eq(0).css('background-color', 'white');
			} 
			else 
			{
				$('.vsSummary').each(function() 
				{
					if ($(this).find('.status').text() === String(params.status)) 
					{
						$(this).css('background-color', '#61d0fb');
//						return false;
					}
				});
			}
		}
	},
	applyFaultLogCss : function(params) 
	{
		with (this) 
		{
			// 테이블 원본 저장
			table1 = $('#table1').html();
			// table2 스타일 정의
			$('#table1').fixheadertable({
				height : 154,
				colratio : [ -1, 12, 18, 16, 17, 7, 15, 7, 8 ]
			});
			tableHeadSize('#table1', {
				'col' : '8',
				'hsize' : '7'
			});
			initTable([ '#table1 tbody tr' ], [ 7 ], [ -1 ]);
			
			if (params.status == null) 
			{
				$('.faultLog').eq(0).css('background-color', 'white');
			} 
			else 
			{
				$('.faultLog').each(function() 
				{
					if ($(this).find('.status').text() === String(params.status)) 
					{
						$(this).css('background-color', '#61d0fb');
//						return false;
					}
				});
			}
		}
	},
	registAdcSummaryEvents : function(selectedType, groupIndex, adc, status, faultMaxDays, orderType, orderDir) 
	{
		with (this) 
		{
			$('.adcSummaryListRow').click(function() 
			{
				selectedAdcIndex = undefined;
				
				var adc = { 'index' : $(this).find('.adcIndex').text() };
				//FlowitUtil.log('adcIndex: %s', adc.index);
				// 최초 한번 Load 된 후에는 adcMonitoringRefreshFlag 가 True로 변경되며,
				// Refresh 함수를 타도록 한다. (Chart Data Insert)
				if (adcMonitoringRefreshFlag == false)
				{
					loadAdcMonitoring(adc);
				}
				else
				{
					loadAdcMonitoringRefresh(adc);
				}
			});
			$('.orderDir_Desc').click(function(e)
			{
				e.preventDefault();							
				orderDir =  $(this).find('.orderDir').text();
				orderType =  $(this).find('.orderType').text();	
				loadAdcSummary(selectedType, groupIndex, adc, status, faultMaxDays, orderType, orderDir);
			});
					
			$('.orderDir_Asc').click(function(e)
			{
				e.preventDefault();					
				orderDir =  $(this).find('.orderDir').text();
				orderType =  $(this).find('.orderType').text();	
				loadAdcSummary(selectedType, groupIndex, adc, status, faultMaxDays, orderType, orderDir);
			});
			
			$('.orderDir_None').click(function(e)
			{
				e.preventDefault();					
				orderDir =  $(this).find('.orderDir').text();
				orderType =  $(this).find('.orderType').text();	
				loadAdcSummary(selectedType, groupIndex, adc, status, faultMaxDays, orderType, orderDir);
			});
		}
	},
	registVsSummaryEvents : function(selectedType, groupIndex, adc, status, faultMaxDays, orderType, orderDir) 
	{
		with (this) 
		{
			$('.vsSummaryListRow').click(function() 
			{
				selectedAdcIndex = undefined;
				selectedVsIndex = undefined;
				selectedVsPort = undefined;
				
				var adcType = $(this).find('.adcType').text();
				var adcIndex = $(this).find('.adcIndex').text();
				var vsType = $(this).find('.vsType').text();
				var vsIndex = $(this).find('.vsIndex').text();
				if ('F5' == adcType) 
				{
					//FlowitUtil.log('F5 - vsType: %s, adcIndex: %s, vsIndex: %s', vsType, adcIndex, vsIndex);
					if (vsMonitoringRefreshFlag == false)
					{
						loadVsMonitoring({ 'index' : adcIndex }, vsType, vsIndex, vsPort);
					}
					else
					{
						loadVsMonitoringRefresh({ 'index' : adcIndex }, vsType, vsIndex, vsPort);
					}					
				} 
				else if ('Alteon' == adcType) 
				{
					var vsPort = $.trim($(this).find('.vsPort').text());
					//FlowitUtil.log('Alteon - vsType: %s, adcIndex: %s, vsIndex: %s, vsPort: %s', vsType, adcIndex, vsIndex, vsPort);
					if (vsMonitoringRefreshFlag == false)
					{
						loadVsMonitoring({ 'index' : adcIndex }, vsType, vsIndex, vsPort);
					}
					else
					{
						loadVsMonitoringRefresh({ 'index' : adcIndex }, vsType, vsIndex, vsPort);
					}	
				}
				else if ('PAS' == adcType) 
				{
					//FlowitUtil.log('PAS - vsType: %s, adcIndex: %s, vsIndex: %s', vsType, adcIndex, vsIndex);
					if (vsMonitoringRefreshFlag == false)
					{
						loadVsMonitoring({ 'index' : adcIndex }, vsType, vsIndex, vsPort);
					}
					else
					{
						loadVsMonitoringRefresh({ 'index' : adcIndex }, vsType, vsIndex, vsPort);
					}					
				} 
				else if ('PASK' == adcType) 
				{
					//FlowitUtil.log('PASK - vsType: %s, adcIndex: %s, vsIndex: %s', vsType, adcIndex, vsIndex);
					if (vsMonitoringRefreshFlag == false)
					{
						loadVsMonitoring({ 'index' : adcIndex }, vsType, vsIndex, vsPort);
					}
					else
					{
						loadVsMonitoringRefresh({ 'index' : adcIndex }, vsType, vsIndex, vsPort);
					}						
				} 
			});
			$('.orderDir_Desc').click(function(e)
			{
				e.preventDefault();
				
				orderDir =  $(this).find('.orderDir').text();
				orderType =  $(this).find('.orderType').text();	
				loadVsSummary(selectedType, groupIndex, adc, status, faultMaxDays,orderType, orderDir);
			});
					
			$('.orderDir_Asc').click(function(e)
			{
				e.preventDefault();

				orderDir =  $(this).find('.orderDir').text();
				orderType =  $(this).find('.orderType').text();	
				loadVsSummary(selectedType, groupIndex, adc, status, faultMaxDays,orderType, orderDir);
			});
			
			$('.orderDir_None').click(function(e)
			{
				e.preventDefault();

				orderDir =  $(this).find('.orderDir').text();
				orderType =  $(this).find('.orderType').text();	
				loadVsSummary(selectedType, groupIndex, adc, status, faultMaxDays,orderType, orderDir);
			});

		}
	},
	registFaultLogEvents : function(selectedType, groupIndex, adc, status, faultMaxDays, orderType, orderDir) 
	{
		with (this) 
		{
			$('.faultLogListRow').click(function() 
			{
				var selected = false;
				var faultType = $(this).find('.faultType').text();	
				var typeNm = $(this).find('.typeNm').text(); 
				//alert("typeNm : " + typeNm);
				if ("5" == faultType || "6" == faultType || "7" == faultType || "1" == faultType || "4" == faultType || "etc" == typeNm) 
				{
					var adc = { 'index' : $(this).find('.adcIndex').text() };
					//FlowitUtil.log('adcIndex: %s', adc.index);
					// 최초 한번 Load 된 후에는 adcMonitoringRefreshFlag 가 True로 변경되며,
					// Refresh 함수를 타도록 한다. (Chart Data Insert)
					if (adcMonitoringRefreshFlag == false)
					{
						loadAdcMonitoring(adc);
					}
					else
					{
						loadAdcMonitoringRefresh(adc);
					}
					
					selected = true;
				} 
				else 
				{
					var adcType = $(this).find('.adcType').text();
					var adcIndex = $(this).find('.adcIndex').text();
					var vsType = "0";
					var vsIndex = $(this).find('.vsIndex').text();
					if ('F5' == adcType) 
					{
//						FlowitUtil.log('F5 - vsType: %s, adcIndex: %s, vsIndex: %s', vsType, adcIndex, vsIndex);
						if (vsMonitoringRefreshFlag == false)
						{
							loadVsMonitoring({ 'index' : adcIndex }, vsType, vsIndex);
						}
						else
						{
							loadVsMonitoringRefresh({ 'index' : adcIndex }, vsType, vsIndex);
						}	
						selected = true;
					} 
					else if ('Alteon' == adcType ) 
					{
						//var vsPort = $(this).find('.vsPort').text();
						//FlowitUtil.log('Alteon - vsType: %s, adcIndex: %s, vsIndex: %s', vsType, adcIndex, vsIndex);
						if (vsMonitoringRefreshFlag == false)
						{
							loadVsMonitoring({ 'index' : adcIndex }, vsType, vsIndex);
						}
						else
						{
							loadVsMonitoringRefresh({ 'index' : adcIndex }, vsType, vsIndex);
						}	
						selected = true;
					}
					else if ('PAS' == adcType) 
					{
//						FlowitUtil.log('F5 - vsType: %s, adcIndex: %s, vsIndex: %s', vsType, adcIndex, vsIndex);
						if (vsMonitoringRefreshFlag == false)
						{
							loadVsMonitoring({ 'index' : adcIndex }, vsType, vsIndex);
						}
						else
						{
							loadVsMonitoringRefresh({ 'index' : adcIndex }, vsType, vsIndex);
						}	
						selected = true;
					} 
					else if ('PASK' == adcType) 
					{
						//FlowitUtil.log('PAS - vsType: %s, adcIndex: %s, vsIndex: %s', vsType, adcIndex, vsIndex);
						if (vsMonitoringRefreshFlag == false)
						{
							loadVsMonitoring({ 'index' : adcIndex }, vsType, vsIndex);
						}
						else
						{
							loadVsMonitoringRefresh({ 'index' : adcIndex }, vsType, vsIndex);
						}	
						selected = true;
					} 
				}
				if (selected) 
				{
					$('.faultLogListRow').removeClass("dashboardLogRowSelection");
					$(this).addClass("dashboardLogRowSelection");	// temporary
				}			
			});
			$('.orderDir_Desc').click(function(e)
			{
				e.preventDefault();
				
				orderDir =  $(this).find('.orderDir').text();
				orderType =  $(this).find('.orderType').text();	
				loadFaultLog(selectedType, groupIndex, adc, status, faultMaxDays,orderType, orderDir);
			});
					
			$('.orderDir_Asc').click(function(e)
			{
				e.preventDefault();
				
				orderDir =  $(this).find('.orderDir').text();
				orderType =  $(this).find('.orderType').text();	
				loadFaultLog(selectedType, groupIndex, adc, status, faultMaxDays,orderType, orderDir);
			});
			
			$('.orderDir_None').click(function(e)
			{
				e.preventDefault();
				
				orderDir =  $(this).find('.orderDir').text();
				orderType =  $(this).find('.orderType').text();	
				loadFaultLog(selectedType, groupIndex, adc, status, faultMaxDays,orderType, orderDir);
			});
		}
	}
});

