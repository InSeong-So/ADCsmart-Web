var DashboardHeader = Class.create({
	initialize : function() 
	{
		this.ajaxManager = new FlowitAjax();
		this.OBajaxManager = new OBAjax();
		this.widgetChart = new WidgetChart();	
		
		this.G_widgetList = [];					// Widget List Json Data (전역 Data)		
		this.autoRefreshTimer = undefined;		// 위젯 Refresh Timer 
		this.refreshIntervalSeconds = 30;		// 위젯 Refresh Time 30sec
		this.maxIndex = undefined;				// 존재하는 위젯중 제일 큰 Index를 저장
		this.maxDataFlag = false;				// 위젯 Index 부여시, 존재하는 위젯의 Index를 한번만 불러오기 위해 사용
		this.widgetOrder = 0;					// 위젯 Index 저장, 1 로 시작하기위해 0으로 초기화
		this.widgetLoadCount = 0;				// Widget을 Load 할때 Count
		this.widgetAddCount = 0;				// Widget을 Add 할때 Count
		this.dashboardName = undefined;
		this.dashboardIndex = undefined;		// G_DashboardIndex, 사용 처 : Dashboard 삭제
		this.widgetDefaultSize = 405;			// Widget 기본 사이즈 Define
		this.loadPassKey = true;				// Dashboard load, widget 변경 사항이 있는지 확인하는 key
		
		this.G_refershChartObj = {};			// Memory Leak 해결을 위한 Refresh Chartobj
		this.G_widgetCount = 0;					// Load된 위젯 갯수, for 위젯 갯수 Limit
		this.widgetMaxCount = 18;				// Widget 최대 크기
		this.select_target_mode = undefined;	// select target 대상선택
		this.intervalMonitor = undefined;
	},
	load : function()
	{
		with (this)
		{
			//loadDashBoardHeader();
			loadLastDashBoardInfo();
			autoRefreshDashboard();
		}		
	},
	
	// 초기페이지, 제일 최근선택한 Dashboard data Get
	loadLastDashBoardInfo : function()
	{
		with(this)
		{
			OBajaxManager.runJson({
				url	: '/dashboard/loadLastDashboardInfo.action',
				data :
				{
				},
				successFn : function(data) 
				{
					var SelectedLastDashboardIndexKey = data.dashboardLastList[0].indexKey;
					dashboardIndex = SelectedLastDashboardIndexKey;
					intervalMonitor = data.intervalMonitor;
					loadDashboardInfo(SelectedLastDashboardIndexKey);					
				}				
			});
		}
	},
	
	// Dashboard Header 페이지 load (Get All Dashboard List, Get All Widget List)
	loadDashBoardHeader :function(widgetList, dashboardIndexKey)
	{
//		console.log("interval :", interval);
//		console.log("intervalMonitor :", intervalMonitor);
//		console.log("this.intervalMonitor :", this.intervalMonitor);
		with (this)
		{
			OBajaxManager.runHtml({
				url			: '/dashboard/loadHeader.action',
//				target		: '.DashBoardHeader',
				target		: "#wrap .contents",
				successFn	: function(params) 
				{					
					if (autoRefreshTimer == null || autoRefreshTimer === undefined)
					{
						autoRefreshDashboard();
					}
					loadDashboardContent(widgetList, dashboardIndexKey);					
					clickEvent();					
				}			
			});
		}
	},
	// Dashboard Header 페이지 Refresh, Dashboard Save 이후에 사용
	refreshDashBoardHeader :function(selecteDashboardName)
	{
		with (this)
		{
			OBajaxManager.runHtml({
				url			: '/dashboard/loadHeader.action',
//				target		: '.DashBoardHeader',
				target		: "#wrap .contents",
				successFn	: function(params) 
				{
					$('#DashboardList option').each(function() 
					{
						if ($(this).text() == selecteDashboardName)
						{
							var saveDashboardIndex = $(this).val();
							loadDashboardInfo(saveDashboardIndex);	
						}													
					});													
					clickEvent();					
				}			
			});
		}
	},
	// Dashboard Content 페이지 load (Get Data None)
	loadDashboardContent : function(widgetList, dashboardIndexKey)
	{
		with(this)
		{
			OBajaxManager.runHtml({
				url			: '/dashboard/loadDashboardContent.action',
				target		: 'div .dashboardContentArea',				
				successFn	: function(params) 
				{					
					widgetLoadCount = 0;
					widgetAddCount = 0;
					G_widgetCount = 0;
					onLoadWidget(widgetList);
					selectboxChange(dashboardIndexKey);
					dashboardIndex = dashboardIndexKey;
//					dashboardName = $('#DashboardList').children('option').filter('value=' + dashboardIndex).text();
					dashboardName = $('#DashboardList').children('option[value=' + dashboardIndex + ']').text();
					loadPassKey = true;				
					sortableUI();					
				}			
			});
		}
	},
	// 선택된 Dashboard Info Get
	loadDashboardInfo : function(inputIndex)
	{
		with (this)
		{
			OBajaxManager.runJson({
				url	: '/dashboard/loadDashboardInfo.action',
				data :
				{					
					"dashboardIndex" : inputIndex
				},
				successFn : function(data) 
				{		
					if (data.dashboardInfo != null)
					{
						widgetLoadCount = 0;
						widgetAddCount = 0;
						G_widgetList = data.dashboardInfo.widgetList;						
					}
					loadDashBoardHeader(G_widgetList, data.dashboardInfo.indexKey);
				}				
			});
		}
	},
	// Dashboard new save Popup
	loadNewSavePopup : function()
	{
		with(this)
		{
			showPopup({
				'id' : '#dashboardNewSavePopup',
				'width' : '290px',
				'height' : '455px'
			});
			
			// 새 이름으로 저장 Popup 저장 Btn
			$('.pop_type_wrap #dash_newSave').click(function(e)
			{
				e.preventDefault();				
				newSaveDashboardInfo();				
			});
			// 위젯 추가 닫기 Btn
			$('.pop_type_wrap #dash_newSave_close').click(function(e)
			{
				e.preventDefault();
				$('.popup_type1').remove();
				$('.cloneDiv').remove();
				onAutoRefreshForEvent();
			});		
		}
	},
	// Add Widget Popup
	loadWidgetPopup : function()
	{
		with(this)
		{
			showPopup({
				'id' : '#addWidgetPopup',
				'width' : '800px',
				'height' : '455px'
			});		
			
			// 전체 그룹 List Get (Basic Active)
			OBajaxManager.runJson({
				url	: '/dashboard/loadWidgetTargetInfo.action',
				data :
				{
					"widgetTarget.category" : 0
				},
				successFn : function(data) 
				{
					var html = '';
					if (data.widgetTargetList)
					{
						html += '<option value="0">Select&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>';
						html += '<option value="-1">' + VAR_DASH_GROUPA + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + '</option>';
						for (var i=0; i < data.widgetTargetList.length; i++) 
						{
							html += '<option value="' + data.widgetTargetList[i].index + '" categoryValue="' + data.widgetTargetList[i].category + '">' + data.widgetTargetList[i].name + '</option>';							
						}
						$('.pop_type_wrap #target_group').empty();
						$('.pop_type_wrap #target_adc').empty();
						$('.pop_type_wrap #target_vserver').empty();
						$('.pop_type_wrap #target_group').html(html);
						$('.pop_type_wrap #target_adc').html('<option value="0">ADC Select&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>');
						$('.pop_type_wrap #target_vserver').html('<option value="0">' + "VS Select&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + '</option>');
						$('.pop_type_wrap .list_container .list').empty();
						
						// 초기 페이지 open 시 기본 form 외 모든 추가 form 은 disabled
						$('.pop_type_wrap #target_group').attr("disabled", "disabled");
						$('.pop_type_wrap #target_adc').attr("disabled", "disabled");
						$('.pop_type_wrap #target_vserver').attr("disabled", "disabled");
						$('.pop_type_wrap .AllSelectTr').css('display', 'none');
						$('.pop_type_wrap .FlbGroupSelectTr').css('display', 'none');
						$('.pop_type_wrap .GroupSelectTr').css('display', '');
						$('.pop_type_wrap .VsSelectTr').css('display', '');						
						$('.pop_type_wrap .moreOptionHeader').css('display', 'none');
						$('.pop_type_wrap #moreInfo_Status').parent().parent().css('display', 'none');
						$('.pop_type_wrap #moreInfo_StatusVs').parent().parent().css('display', 'none');
						$('.pop_type_wrap #moreInfo_Time').parent().parent().css('display', 'none');
						$('.pop_type_wrap #moreInfo_Ordering').parent().parent().css('display', 'none');
						$('.pop_type_wrap #moreInfo_Chart').parent().parent().css('display', 'none');
						
						$('.pop_type_wrap .target_header_radio').css('display', 'none');
						$('.pop_type_wrap .list_container').css('display', 'none');
						
						var widgetSelectBox = $('.pop_type_wrap.cloneDiv select[name=widget_select]'); 
						widgetSelectBox.val(widgetSelectBox.find('option:first').val());
						widgetSelectBox.change();
						widgetSelectBox.focus();
					}				
				}				
			});
			targetChangeEvent();
			
			// 대상선택 Radio Btn 초기화.
			$('.cloneDiv #select_typeall').attr('checked', 'checked');		
			
			// MoreInfo 기능
			var $selected_status = $('.pop_type_wrap .selected_status').filter(':last');
			var $selected_statusVs = $('.pop_type_wrap .selected_statusVs').filter(':last');
			var $viewSelected_time = $('.pop_type_wrap .selected_time').filter(':last');
			var $selected_ordering = $('.pop_type_wrap .selected_ordering').filter(':last');
			var $selected_chart = $('.pop_type_wrap .selected_chart').filter(':last');
			
			$('.pop_type_wrap #moreInfo_Status').change(function(e)
			{
				var $html = '';
				$html = $(this).children('option').filter(':selected').val();
				$selected_status.empty();
				$selected_statusVs.empty();
				$viewSelected_time.empty();
				$selected_ordering.empty();
				$selected_chart.empty();
				$selected_status.html($html);
			});
			
			$('.pop_type_wrap #moreInfo_StatusVs').change(function(e)
			{
				var $html = '';
				$html = $(this).children('option').filter(':selected').val();
				$selected_status.empty();
				$selected_statusVs.empty();
				$viewSelected_time.empty();
				$selected_ordering.empty();
				$selected_chart.empty();
				$selected_statusVs.html($html);		
			});
			$('.pop_type_wrap #moreInfo_Time').change(function(e)
			{
				var $html = '';
				$html = $(this).children('option').filter(':selected').val();
				$selected_status.empty();
				$selected_statusVs.empty();
				$viewSelected_time.empty();
				$selected_ordering.empty();
				$selected_chart.empty();
				$viewSelected_time.html($html);		
			});
			$('.pop_type_wrap #moreInfo_Ordering').change(function(e)
			{
				var $html = '';
				$html = $(this).children('option').filter(':selected').val();
				$selected_status.empty();
				$selected_statusVs.empty();
				$viewSelected_time.empty();
				$selected_ordering.empty();
				$selected_chart.empty();
				$selected_ordering.html($html);		
			});
			$('.pop_type_wrap #moreInfo_Chart').change(function(e)
			{
				var $html = '';
				$html = $(this).children('option').filter(':selected').val();
				$selected_status.empty();
				$selected_statusVs.empty();
				$viewSelected_time.empty();
				$selected_ordering.empty();
				$selected_chart.empty();
				$selected_chart.html($html);		
			});
			
			// 대상 선택 Radio Btn Change 기능
			$('.cloneDiv input[name="target_type"]').change(function(e) 
			{
				e.preventDefault();
				var targetTypeValue = $(this).val();				
				widgetTagetSelectBoxChanger(targetTypeValue);
			});
			
			// 위젯 선택 Select List Change 기능
			$('.cloneDiv #widget_select').change(function(e)
			{
				e.preventDefault();
				var targetTypeValue = $('.cloneDiv input[name="target_type"]:checked').val();
				widgetTagetSelectBoxChanger(targetTypeValue);
			});
			
			// 위젯 추가 Btn
			$('.pop_type_wrap #widget_add').click(function(e)
			{
				var widgetAddKey = true;	// widgetAdd 일때는 Validate 함수에 True 전달
				if(!validateWidgetAdd(widgetAddKey, undefined, undefined))
				{
					return false;
				}
				e.preventDefault();				
				if(widgetMaxCount <= G_widgetCount)
				{
					$.obAlertNotice(VAR_DASH_THEMAX + " " + widgetMaxCount + " " + VAR_DASH_THEWAAVA);
					return false;
				}
				addWidget(G_widgetList);
			});
			// 위젯 추가 닫기 Btn
			$('.pop_type_wrap #widget_close').click(function(e)
			{
				e.preventDefault();
				$('.popup_type1').remove();
				$('.cloneDiv').remove();
				onAutoRefreshForEvent();
			});			
		}
	},
	//TODO target Select Box 분리 함수
	widgetTagetSelectBoxChanger : function(targetType)
	{
		with(this)
		{
			$('.pop_type_wrap .list_container .list').empty();
			
			// 요약에 선택된 그룹정보 Write, 위젯 추가 팝업에만 존재		
			var $html = '';
			var $viewWidget = $('.widget_summery_table .selected_widget').filter(':last');				
			$html = $('.pop_type_wrap #widget_select').children('option').filter(':selected').text();
			$viewWidget.html($html);
			
			// 선택한 Widget 의 미리보기 Path 연결 (type Index 사용) , 위젯 추가 팝업에만 존재
			var $DisplayPath = '';
			var $NoticeTxt = '';
			var $displayarea = $('.pop2_contents .thumbnail').filter(':last');
			var $widgetNotice = $('.pop2_contents .widgetNoticeTxt').filter(':last');
			$displayarea.empty();
			$widgetNotice.empty();
			var widgetIndex = $('.pop_type_wrap #widget_select').children('option').filter(':selected').val();				
			if (widgetIndex == 1)
			{
				$DisplayPath = '<img src="/imgs/dashboard' + getImgLang() + '/thumbnail_faultMonitoring.jpg" alt="' + VAR_DASH_PREVIEW + '" />';
				$NoticeTxt = VAR_DASH_NOTICE_TOTAL_NUMBER_OF_ALERTS;
			}
			else if (widgetIndex == 2)
			{
				$DisplayPath = '<img src="/imgs/dashboard' + getImgLang() + '/thumbnail_FaultMonitoringChart.jpg" alt="' + VAR_DASH_PREVIEW + '" />';
				$NoticeTxt = VAR_DASH_NOTICE_TOTAL_TRENDS_IN_ALERTS;
			}
			else if (widgetIndex == 3)
			{
				$DisplayPath = '<img src="/imgs/dashboard' + getImgLang() + '/thumbnail_faultMonitoringList.jpg" alt="' + VAR_DASH_PREVIEW + '" />';
				$NoticeTxt = VAR_DASH_NOTICE_TOTAL_MOST_RECENT_ALERTS;
			}
			else if (widgetIndex == 4)
			{
				$DisplayPath = '<img src="/imgs/dashboard' + getImgLang() + '/thumbnail_AdcChangeHistoryChart.jpg" alt="' + VAR_DASH_PREVIEW + '" />';
				$NoticeTxt = VAR_DASH_NOTICE_TOTAL_TRENDS_IN_CONFIGURATION_CHANGES;
			}
			else if (widgetIndex == 5)
			{
				$DisplayPath = '<img src="/imgs/dashboard' + getImgLang() + '/thumbnail_adcMonitoringList.jpg" alt="' + VAR_DASH_PREVIEW + '" />';
				$NoticeTxt = VAR_DASH_NOTICE_TOTAL_MOST_RECENT_SETTINGS_CHANGES;
			}
			else if (widgetIndex == 6)
			{
				$DisplayPath = '<img src="/imgs/dashboard' + getImgLang() + '/thumbnail_adcMonitoring.jpg" alt="' + VAR_DASH_PREVIEW + '" />';
				$NoticeTxt = VAR_DASH_NOTICE_TOTAL_ADC_SUMMARY;
			}
			else if (widgetIndex == 7)
			{
			$DisplayPath = '<img src="/imgs/dashboard' + getImgLang() + '/thumbnail_VsMonitoring.jpg" alt="' + VAR_DASH_PREVIEW + '" />';
			$NoticeTxt = VAR_DASH_NOTICE_TOTAL_VS_SUMMARY;
			}
			else if (widgetIndex == 8)
			{
				$DisplayPath = '<img src="/imgs/dashboard' + getImgLang() + '/thumbnail_AdcConnectionChart.jpg" alt="' + VAR_DASH_PREVIEW + '" />';
				$NoticeTxt = VAR_DASH_NOTICE_TOTAL_TRENDS_IN_CONCURRENT_SESSION;
			}
			else if (widgetIndex == 9)
			{
				$DisplayPath = '<img src="/imgs/dashboard' + getImgLang() + '/thumbnail_AdcThroughtputChart.jpg" alt="' + VAR_DASH_PREVIEW + '" />';
				$NoticeTxt = VAR_DASH_NOTICE_TOTAL_TRENDS_IN_THROUGHPUT;
			}
			else if (widgetIndex == 10)
			{
				$DisplayPath = '<img src="/imgs/dashboard' + getImgLang() + '/thumbnail_CpuHistoryChart.jpg" alt="' + VAR_DASH_PREVIEW + '" />';
				$NoticeTxt = VAR_DASH_NOTICE_TOTAL_TRENDS_IN_ADC_CPU_USAGE;
			}
			else if (widgetIndex == 11)
			{
				$DisplayPath = '<img src="/imgs/dashboard' + getImgLang() + '/thumbnail_MemoryHistoryChart.jpg" alt="' + VAR_DASH_PREVIEW + '" />';
				$NoticeTxt = VAR_DASH_NOTICE_TOTAL_TRENDS_IN_ADC_MEMORY_USAGE;
			}
			else if (widgetIndex == 12)
			{
				$DisplayPath = '<img src="/imgs/dashboard' + getImgLang() + '/thumbnail_AllAdcTrafficChart.jpg" alt="' + VAR_DASH_PREVIEW + '" />';
				$NoticeTxt = VAR_DASH_NOTICE_TOTAL_TOTAL_ADC_TRAFFIC;
			}
			else if (widgetIndex == 13)
			{
				$DisplayPath = '<img src="/imgs/dashboard' + getImgLang() + '/thumbnail_VsStatusChart.jpg" alt="' + VAR_DASH_PREVIEW + '" />';
				$NoticeTxt = VAR_DASH_NOTICE_TOTAL_CHANGES_IN_VS_STATUS;
			}
			else if (widgetIndex == 14)
			{
				$DisplayPath = '<img src="/imgs/dashboard' + getImgLang() + '/thumbnail_adcSummaryList.jpg" alt="' + VAR_DASH_PREVIEW + '" />';
				$NoticeTxt = VAR_DASH_NOTICE_TOTAL_ADC_LIST;
			}
			else if (widgetIndex == 15)
			{
				$DisplayPath = '<img src="/imgs/dashboard' + getImgLang() + '/thumbnail_vsSummaryList.jpg" alt="' + VAR_DASH_PREVIEW + '" />';
				$NoticeTxt = VAR_DASH_NOTICE_TOTAL_VS_LIST;
			}
			else if (widgetIndex == 16)
			{
				$DisplayPath = '<img src="/imgs/dashboard' + getImgLang() + '/thumbnail_top10ListVs.jpg" alt="' + VAR_DASH_PREVIEW + '" />';
				$NoticeTxt = VAR_DASH_NOTICE_TOTAL_TOP_10_VS_BY_THROUGHPUT;
			}
			else if (widgetIndex == 17)
			{
				$DisplayPath = '<img src="/imgs/dashboard' + getImgLang() + '/thumbnail_top5ListAdc.jpg" alt="' + VAR_DASH_PREVIEW + '" />';
				$NoticeTxt = VAR_DASH_NOTICE_TOTAL_TOP_10_ADC_BY_TRAFFIC;
			}
			else if (widgetIndex == 18)
			{
				$DisplayPath = '<img src="/imgs/dashboard' + getImgLang() + '/thumbnail_ApplicanceMonitor.jpg" alt="' + VAR_DASH_PREVIEW + '" />';
				$NoticeTxt = VAR_DASH_NOTICE_TOTAL_ADC_MONITORING;
			}
			else if (widgetIndex == 19)
			{
				$DisplayPath = '<img src="/imgs/dashboard' + getImgLang() + '/thumbnail_ResponseTimeHistoryChart.jpg" alt="' + VAR_DASH_PREVIEW + '" />';
				$NoticeTxt = VAR_DASH_NOTICE_TOTAL_TRENDS_IN_SERVICE_RESPONSE_TIME;
			}
			else if (widgetIndex == 20)
			{
				$DisplayPath = '<img src="/imgs/dashboard' + getImgLang() + '/thumbnail_AdcConnectionChart.jpg" alt="' + VAR_DASH_PREVIEW + '" />';
				$NoticeTxt = VAR_DASH_NOTICE_TOTAL_TRENDS_IN_CONCURRENT_DETAIL;
			}
			else if (widgetIndex == 21)
			{
				$DisplayPath = '<img src="/imgs/dashboard' + getImgLang() + '/thumbnail_AdcConnectionChart.jpg" alt="' + VAR_DASH_PREVIEW + '" />';
				$NoticeTxt = VAR_DASH_NOTICE_TOTAL_TRENDS_IN_CONCURRENT_FLBGROUP;
			}
			else if ((widgetIndex == 22) || (widgetIndex == 23))	// SP 모니터링 
			{
				$DisplayPath = '<img src="/imgs/dashboard' + getImgLang() + '/thumbnail_CpuSPSessionChart.jpg" alt="' + VAR_DASH_PREVIEW + '" />';
				$NoticeTxt = VAR_DASH_NOTICE_TOTAL_TRENDS_IN_ADC_CPU_SP_USAGE;					
			}
			else if (widgetIndex == 24)
			{
				$DisplayPath = '<img src="/imgs/dashboard' + getImgLang() + '/thumbnail_statusNotification_adc.jpg" alt="' + VAR_DASH_PREVIEW + '" />';
				$NoticeTxt = VAR_DASH_NOTICE_TOTAL_ADC_STATUS_NOTIFICATION;
			}
			else if (widgetIndex == 25)
			{
				$DisplayPath = '<img src="/imgs/dashboard' + getImgLang() + '/thumbnail_statusNotification_service.jpg" alt="' + VAR_DASH_PREVIEW + '" />';
				$NoticeTxt = VAR_DASH_NOTICE_TOTAL_VS_STATUS_NOTIFICATION;
			}
			else if (widgetIndex == 26)
			{
				$DisplayPath = '<img src="/imgs/dashboard' + getImgLang() + '/thumbnail_statusNotification_member.jpg" alt="' + VAR_DASH_PREVIEW + '" />';
				$NoticeTxt = VAR_DASH_NOTICE_TOTAL_MEMBER_STATUS_NOTIFICATION;
			}
			$displayarea.html($DisplayPath);
			$widgetNotice.html($NoticeTxt);
			
			// 추가 옵션 여는 기능
			var $moreInfoHeader = $('.pop_type_wrap .moreOptionHeader');
			var $moreInfoStatus = $('.pop_type_wrap #moreInfo_Status');
			var $moreInfoStatusVs = $('.pop_type_wrap #moreInfo_StatusVs');
			var $moreInfoTime = $('.pop_type_wrap #moreInfo_Time');
			var $moreInfoOrdering = $('.pop_type_wrap #moreInfo_Ordering');
			var $moreInfoChart = $('.pop_type_wrap #moreInfo_Chart');
			
			var $targetHeaderRadio = $('.pop_type_wrap .target_header_radio');	
			var $targetHeaderRadioVal = $('.cloneDiv input[name="target_type"]:checked').val();
					
			var widgetType = $('.pop_type_wrap #widget_select').children('option').filter(':selected').val();				
			if (widgetType == 1)	// 장애 모니터링 통계 // 현재 사용하지않는 위젯
			{
				$moreInfoHeader.css('display', '');
				$moreInfoStatus.parent().parent().css('display', 'none');
				$moreInfoStatusVs.parent().parent().css('display', 'none');
				$moreInfoTime.parent().parent().css('display', '');
				$moreInfoOrdering.parent().parent().css('display', 'none');
				$moreInfoChart.parent().parent().css('display', 'none');
				
				$targetHeaderRadio.css('display', 'none');
			}
			else if (widgetType == 8 || widgetType == 9)	// Concurrent session, Throughput chart 장애
			{
				if($targetHeaderRadioVal == "all")
				{
					$moreInfoHeader.css('display', 'none');
					$moreInfoChart.parent().parent().css('display', 'none');
				}
				else
				{
					$moreInfoHeader.css('display', '');
					$moreInfoChart.parent().parent().css('display', '');
				}
				
				$moreInfoStatus.parent().parent().css('display', 'none');
				$moreInfoStatusVs.parent().parent().css('display', 'none');
				$moreInfoTime.parent().parent().css('display', 'none');
				$moreInfoOrdering.parent().parent().css('display', 'none');
				
				$targetHeaderRadio.css('display', '');
			}
			
			else if (widgetType == 14)	// ADC 요약 목록
			{
				$moreInfoHeader.css('display', '');
				$moreInfoStatus.parent().parent().css('display', '');
				$moreInfoStatusVs.parent().parent().css('display', 'none');
				$moreInfoTime.parent().parent().css('display', 'none');
				$moreInfoOrdering.parent().parent().css('display', 'none');
				$moreInfoChart.parent().parent().css('display', 'none');
				
				$targetHeaderRadio.css('display', 'none');
			}
			else if (widgetType == 15)	// VS 요약 목록
			{
				$moreInfoHeader.css('display', '');
				$moreInfoStatus.parent().parent().css('display', 'none');
				$moreInfoStatusVs.parent().parent().css('display', '');
				$moreInfoTime.parent().parent().css('display', 'none');
				$moreInfoOrdering.parent().parent().css('display', 'none');
				$moreInfoChart.parent().parent().css('display', 'none');
				
				$targetHeaderRadio.css('display', 'none');
			}
			else if (widgetType == 16)	// VS TOP 10 사용량 목록
			{
				$moreInfoHeader.css('display', '');
				$moreInfoStatus.parent().parent().css('display', 'none');
				$moreInfoStatusVs.parent().parent().css('display', 'none');
				$moreInfoTime.parent().parent().css('display', 'none');
				$moreInfoOrdering.parent().parent().css('display', '');
				$moreInfoChart.parent().parent().css('display', 'none');
				
				$targetHeaderRadio.css('display', 'none');
			}
			else if (widgetType == 19)	// 응답시간 
			{
				if($targetHeaderRadioVal == "all")
				{
					$moreInfoHeader.css('display', 'none');
					$moreInfoChart.parent().parent().css('display', 'none');
				}
				else
				{
					$moreInfoHeader.css('display', '');
					$moreInfoChart.parent().parent().css('display', '');
				}
				
				$moreInfoStatus.parent().parent().css('display', 'none');
				$moreInfoStatusVs.parent().parent().css('display', 'none');
				$moreInfoTime.parent().parent().css('display', 'none');
				$moreInfoOrdering.parent().parent().css('display', 'none');
				
				$targetHeaderRadio.css('display', 'none'); //응답시간 그룹은 추후에
			}
			else if (widgetType == 24)	// ADC 상태 알리미
			{
				$moreInfoHeader.css('display', 'none');
				$moreInfoStatus.parent().parent().css('display', 'none');
				$moreInfoStatusVs.parent().parent().css('display', 'none');
				$moreInfoTime.parent().parent().css('display', 'none');
				$moreInfoOrdering.parent().parent().css('display', 'none');
				$moreInfoChart.parent().parent().css('display', 'none');
				
				$targetHeaderRadio.css('display', 'none');
			}
			else if (widgetType == 25)
			{
				$moreInfoHeader.css('display', 'none');
				$moreInfoStatus.parent().parent().css('display', 'none');
				$moreInfoStatusVs.parent().parent().css('display', 'none');
				$moreInfoTime.parent().parent().css('display', 'none');
				$moreInfoOrdering.parent().parent().css('display', 'none');
				$moreInfoChart.parent().parent().css('display', 'none');
				
				$targetHeaderRadio.css('display', '');
			}
			else if (widgetType == 26)
			{
				$moreInfoHeader.css('display', 'none');
				$moreInfoStatus.parent().parent().css('display', 'none');
				$moreInfoStatusVs.parent().parent().css('display', 'none');
				$moreInfoTime.parent().parent().css('display', 'none');
				$moreInfoOrdering.parent().parent().css('display', 'none');
				$moreInfoChart.parent().parent().css('display', 'none');
				
				$targetHeaderRadio.css('display', '');
			}
			else
			{
				$moreInfoHeader.css('display', 'none');
				$moreInfoStatus.parent().parent().css('display', 'none');
				$moreInfoStatusVs.parent().parent().css('display', 'none');
				$moreInfoTime.parent().parent().css('display', 'none');
				$moreInfoOrdering.parent().parent().css('display', 'none');
				$moreInfoChart.parent().parent().css('display', 'none');
			
				$targetHeaderRadio.css('display', 'none');
			}	
			// 상관없는 Target Disable 하는 기능 (targetArea 사용) FIXME 필히 모듈화가 필요한 부분. 코드 낭비가 심하다.
			if (targetType == "group")	 // 대상선택 그룹일 경우
			{
				// 전체 선택만 가능
				var widgetTargerArea = $('.pop_type_wrap #widget_select').children('option').filter(':selected').attr('id');				
				if (widgetTargerArea == 1)
				{
					$('.pop_type_wrap .AllSelectTr').css('display', '');
					$('.pop_type_wrap .GroupSelectTr').css('display', 'none');
					$('.pop_type_wrap .AdcSelectTr').css('display', '');
					//$('.pop_type_wrap #target_group').attr("disabled", "disabled");
					$('.pop_type_wrap #target_adc').attr("disabled", "disabled");
					$('.pop_type_wrap .VsSelectTr').css('display', '');
					$('.pop_type_wrap #target_vserver').attr("disabled", "disabled");
					$('.pop_type_wrap .FlbGroupSelectTr').css('display', 'none');
					$('.pop_type_wrap .ExtendGroupSelectTr').css('display', 'none');
					$('.pop_type_wrap .list_container').css('display', 'none');
					widgetTargetInfoClear();
				}
				// 전체 선택 및 그룹 선택 가능
				else if (widgetTargerArea == 2)
				{
					$('.pop_type_wrap .AllSelectTr').css('display', 'none');
					$('.pop_type_wrap .GroupSelectTr').css('display', '');
					$('.pop_type_wrap .AdcSelectTr').css('display', '');
					$('.pop_type_wrap #target_group').removeAttr("disabled");
					$('.pop_type_wrap #target_adc').attr("disabled", "disabled");
					$('.pop_type_wrap .VsSelectTr').css('display', '');
					$('.pop_type_wrap #target_vserver').attr("disabled", "disabled");
					$('.pop_type_wrap .FlbGroupSelectTr').css('display', 'none');
					$('.pop_type_wrap .ExtendGroupSelectTr').css('display', 'none');
					$('.pop_type_wrap .list_container').css('display', 'none');
					widgetTargetInfoClear();
					widgetTargetAllOff();
				}
				// ADC 까지 필수 선택
				else if (widgetTargerArea == 4)
				{
					$('.pop_type_wrap .AllSelectTr').css('display', 'none');
					$('.pop_type_wrap .GroupSelectTr').css('display', '');
					$('.pop_type_wrap .AdcSelectTr').css('display', '');
					$('.pop_type_wrap #target_group').removeAttr("disabled");
					$('.pop_type_wrap #target_adc').removeAttr("disabled");
					$('.pop_type_wrap .VsSelectTr').css('display', '');
					$('.pop_type_wrap #target_vserver').attr("disabled", "disabled");
					$('.pop_type_wrap .FlbGroupSelectTr').css('display', 'none');
					$('.pop_type_wrap .ExtendGroupSelectTr').css('display', 'none');
					$('.pop_type_wrap .list_container').css('display', 'none');
					widgetTargetInfoClear();
					widgetTargetAllOff();
				}
				// 전체, 그룹, ADC 까지 선택 가능
				else if (widgetTargerArea == 7)
				{
					$('.pop_type_wrap .AllSelectTr').css('display', 'none');
					$('.pop_type_wrap .GroupSelectTr').css('display', '');
					$('.pop_type_wrap .AdcSelectTr').css('display', '');
					$('.pop_type_wrap #target_group').removeAttr("disabled");
					$('.pop_type_wrap #target_adc').removeAttr("disabled");
					$('.pop_type_wrap .VsSelectTr').css('display', '');
					$('.pop_type_wrap #target_vserver').attr("disabled", "disabled");
					$('.pop_type_wrap .FlbGroupSelectTr').css('display', 'none');
					$('.pop_type_wrap .ExtendGroupSelectTr').css('display', 'none');
					$('.pop_type_wrap .list_container').css('display', 'none');
					widgetTargetInfoClear();
					widgetTargetAllOn();
				}
				// VS/Service 까지 필수 선택
				else if (widgetTargerArea == 8)
				{
					if(widgetType == 19)
					{
						$('.pop_type_wrap .AllSelectTr').css('display', 'none');
						$('.pop_type_wrap .GroupSelectTr').css('display', 'none');
						$('.pop_type_wrap .AdcSelectTr').css('display', 'none');
						$('.pop_type_wrap .VsSelectTr').css('display', 'none');
						$('.pop_type_wrap .FlbGroupSelectTr').css('display', 'none');					
						$('.pop_type_wrap .ExtendGroupSelectTr').css('display', '');
						$('.pop_type_wrap #target_extend_group').removeAttr("disabled");
						$('.pop_type_wrap .list_container').css('display', '');
						
						var categoryParam = 7;					
						// 선택 그룹 List Get
						OBajaxManager.runJson({
							url	: '/dashboard/loadWidgetTargetInfo.action',
							data :
							{
								"widgetTarget.category" : categoryParam
							},
							successFn : function(data) 
							{
								var html = '';
								if (data.widgetTargetList)
								{
									html += '<option value="0">Select&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>';								
									for (var i=0; i < data.widgetTargetList.length; i++) 
									{
										html += '<option value="' + data.widgetTargetList[i].index + '" categoryValue="' + data.widgetTargetList[i].category + '">' + data.widgetTargetList[i].name + '</option>';							
									}
									$('.pop_type_wrap #target_adc').empty();
									$('.pop_type_wrap #target_vserver').empty();
									$('.pop_type_wrap #target_flbGroup').empty();
									$('.pop_type_wrap #target_extend_group').empty();	
									$('.pop_type_wrap #target_extend_group').html(html);							
								}				
							}				
						});
					}
					else
					{
						$('.pop_type_wrap .AllSelectTr').css('display', 'none');
						$('.pop_type_wrap .GroupSelectTr').css('display', '');
						$('.pop_type_wrap .AdcSelectTr').css('display', '');
						$('.pop_type_wrap #target_group').removeAttr("disabled");
						$('.pop_type_wrap #target_adc').removeAttr("disabled");
						$('.pop_type_wrap .VsSelectTr').css('display', '');
						$('.pop_type_wrap #target_vserver').removeAttr("disabled");
						$('.pop_type_wrap .FlbGroupSelectTr').css('display', 'none');
						$('.pop_type_wrap .ExtendGroupSelectTr').css('display', 'none');
						$('.pop_type_wrap .list_container').css('display', 'none');
					}
					
					widgetTargetInfoClear();
					widgetTargetAllOff();
				}
				// ADC 필수선택, VS까지 선택 가능
				else if (widgetTargerArea == 9)
				{
					if(widgetType == 8 || widgetType == 9)
					{						
						$('.pop_type_wrap .AllSelectTr').css('display', 'none');
						$('.pop_type_wrap .GroupSelectTr').css('display', 'none');
						$('.pop_type_wrap .AdcSelectTr').css('display', 'none');
						$('.pop_type_wrap .VsSelectTr').css('display', 'none');
						$('.pop_type_wrap .FlbGroupSelectTr').css('display', 'none');					
						$('.pop_type_wrap .ExtendGroupSelectTr').css('display', '');
						$('.pop_type_wrap #target_extend_group').removeAttr("disabled");
						$('.pop_type_wrap .list_container').css('display', '');
						
						var categoryParam = 7;					
						// 선택 그룹 List Get
						OBajaxManager.runJson({
							url	: '/dashboard/loadWidgetTargetInfo.action',
							data :
							{
								"widgetTarget.category" : categoryParam
							},
							successFn : function(data) 
							{
								var html = '';
								if (data.widgetTargetList)
								{
									html += '<option value="0">Select&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>';								
									for (var i=0; i < data.widgetTargetList.length; i++) 
									{
										html += '<option value="' + data.widgetTargetList[i].index + '" categoryValue="' + data.widgetTargetList[i].category + '">' + data.widgetTargetList[i].name + '</option>';							
									}
									$('.pop_type_wrap #target_adc').empty();
									$('.pop_type_wrap #target_vserver').empty();
									$('.pop_type_wrap #target_flbGroup').empty();
									$('.pop_type_wrap #target_extend_group').empty();	
									$('.pop_type_wrap #target_extend_group').html(html);							
								}				
							}				
						});
					}
					else
					{
						$('.pop_type_wrap .AllSelectTr').css('display', 'none');
						$('.pop_type_wrap .GroupSelectTr').css('display', '');
						$('.pop_type_wrap .AdcSelectTr').css('display', '');
						$('.pop_type_wrap #target_group').removeAttr("disabled");
						$('.pop_type_wrap #target_adc').removeAttr("disabled");
						$('.pop_type_wrap .VsSelectTr').css('display', '');
						$('.pop_type_wrap #target_vserver').removeAttr("disabled");
						$('.pop_type_wrap .FlbGroupSelectTr').css('display', 'none');
						$('.pop_type_wrap .ExtendGroupSelectTr').css('display', 'none');
						$('.pop_type_wrap .list_container').css('display', 'none');
					}
					widgetTargetInfoClear();
					widgetTargetAllOff();
						
						
				}
				// FLB 그룹까지 필수 선택
				else if (widgetTargerArea == 10)
				{
					$('.pop_type_wrap .AllSelectTr').css('display', 'none');
					$('.pop_type_wrap .GroupSelectTr').css('display', '');
					$('.pop_type_wrap .AdcSelectTr').css('display', '');
					$('.pop_type_wrap #target_group').removeAttr("disabled");
					$('.pop_type_wrap #target_adc').removeAttr("disabled");					
					$('.pop_type_wrap .VsSelectTr').css('display', 'none');
					$('.pop_type_wrap .FlbGroupSelectTr').css('display', '');
					$('.pop_type_wrap #target_flbGroup').removeAttr("disabled");
					$('.pop_type_wrap .ExtendGroupSelectTr').css('display', 'none');
					$('.pop_type_wrap .list_container').css('display', 'none');
					widgetTargetInfoClear();
					widgetTargetAllOff();
				}
				// Alteon ADC만 Load
				else if (widgetTargerArea == 11)
				{
					$('.pop_type_wrap .AllSelectTr').css('display', 'none');
					$('.pop_type_wrap .GroupSelectTr').css('display', 'none');
					$('.pop_type_wrap .AdcSelectTr').css('display', '');					
					$('.pop_type_wrap #target_adc').removeAttr("disabled");
					$('.pop_type_wrap .VsSelectTr').css('display', 'none');					
					$('.pop_type_wrap .FlbGroupSelectTr').css('display', 'none');
					$('.pop_type_wrap .ExtendGroupSelectTr').css('display', 'none');
					$('.pop_type_wrap .list_container').css('display', 'none');
					
					OBajaxManager.runJson({
						url	: '/dashboard/loadWidgetTargetInfoAlteon.action',
						data :
						{
							"widgetTarget.category" : 1,
							"widgetTarget.index" : 2
						},
						successFn : function(data) 
						{
							var $adcSelectTarget = $('.pop_type_wrap .adcContent');
							var widgetTargetArea = $('.pop_type_wrap #widget_select').children('option').filter(':selected').attr('id');
							$adcSelectTarget.empty();
							if ( widgetTargetArea == 1 || widgetTargetArea == 2 || widgetTargetArea == 3)
							{
								$('<select name="" id="target_adc" class="widget_popup_Select" disabled="disabled"></select>').appendTo($adcSelectTarget);	
							}
							else
							{							
								$('<select name="" id="target_adc" class="widget_popup_Select"></select>').appendTo($adcSelectTarget);
							}								
							targetChangeEvent();

							var html = '';
							var target = data.widgetTargetList;
							html += '<option value="0">ADC Select&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>';
							for (var i=0; i < target.length; i++) 
							{
								html += '<option value="'+target[i].index+'" vendor="'+target[i].vendor+'">'+target[i].name+'</option>';
							}							
							$('.pop_type_wrap #target_adc').empty();
							$('.pop_type_wrap #target_vserver').empty();
							$('.pop_type_wrap #target_flbGroup').empty();
							$('.pop_type_wrap #target_extend_group').empty();							
							$('.pop_type_wrap #target_adc').html(html);
						}
					});
				}
				else if (widgetTargerArea == 12 || widgetTargerArea == 13 || widgetTargerArea == 14)
				{
					widgetTargetInfoClear();
					
					$('.pop_type_wrap .AllSelectTr').css('display', 'none');
					$('.pop_type_wrap .GroupSelectTr').css('display', 'none');
					$('.pop_type_wrap .AdcSelectTr').css('display', 'none');
					$('.pop_type_wrap .VsSelectTr').css('display', 'none');
					$('.pop_type_wrap .FlbGroupSelectTr').css('display', 'none');					
					$('.pop_type_wrap .ExtendGroupSelectTr').css('display', '');
					$('.pop_type_wrap #target_extend_group').removeAttr("disabled");
					$('.pop_type_wrap .list_container').css('display', '');
					
					var categoryParam = undefined;
					if (widgetTargerArea == 12)
						categoryParam = 0;
					else
						categoryParam = 7;
					
					// 선택 그룹 List Get
					OBajaxManager.runJson({
						url	: '/dashboard/loadWidgetTargetInfo.action',
						data :
						{
							"widgetTarget.category" : categoryParam
						},
						successFn : function(data) 
						{
							var html = '';
							if (data.widgetTargetList)
							{
								html += '<option value="0">Select&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>';								
								for (var i=0; i < data.widgetTargetList.length; i++) 
								{
									html += '<option value="' + data.widgetTargetList[i].index + '" categoryValue="' + data.widgetTargetList[i].category + '">' + data.widgetTargetList[i].name + '</option>';							
								}
								$('.pop_type_wrap #target_adc').empty();
								$('.pop_type_wrap #target_vserver').empty();
								$('.pop_type_wrap #target_flbGroup').empty();
								$('.pop_type_wrap #target_extend_group').empty();	
								$('.pop_type_wrap #target_extend_group').html(html);							
							}				
						}				
					});
				}
			}
			else
			{
				// 전체 선택만 가능
				var widgetTargerArea = $('.pop_type_wrap #widget_select').children('option').filter(':selected').attr('id');				
				if (widgetTargerArea == 1)
				{
					$('.pop_type_wrap .AllSelectTr').css('display', '');
					$('.pop_type_wrap .GroupSelectTr').css('display', 'none');
					$('.pop_type_wrap .AdcSelectTr').css('display', '');
					//$('.pop_type_wrap #target_group').attr("disabled", "disabled");
					$('.pop_type_wrap #target_adc').attr("disabled", "disabled");
					$('.pop_type_wrap .VsSelectTr').css('display', '');
					$('.pop_type_wrap #target_vserver').attr("disabled", "disabled");
					$('.pop_type_wrap .FlbGroupSelectTr').css('display', 'none');
					$('.pop_type_wrap .ExtendGroupSelectTr').css('display', 'none');
					$('.pop_type_wrap .list_container').css('display', 'none');
					widgetTargetInfoClear();
				}
				// 전체 선택 및 그룹 선택 가능
				else if (widgetTargerArea == 2)
				{
					$('.pop_type_wrap .AllSelectTr').css('display', 'none');
					$('.pop_type_wrap .GroupSelectTr').css('display', '');
					$('.pop_type_wrap .AdcSelectTr').css('display', '');
					$('.pop_type_wrap #target_group').removeAttr("disabled");
					$('.pop_type_wrap #target_adc').attr("disabled", "disabled");
					$('.pop_type_wrap .VsSelectTr').css('display', '');
					$('.pop_type_wrap #target_vserver').attr("disabled", "disabled");
					$('.pop_type_wrap .FlbGroupSelectTr').css('display', 'none');
					$('.pop_type_wrap .ExtendGroupSelectTr').css('display', 'none');
					$('.pop_type_wrap .list_container').css('display', 'none');
					widgetTargetInfoClear();
					widgetTargetAllOn();
				}
				// ADC 까지 필수 선택
				else if (widgetTargerArea == 4 || widgetTargerArea == 12)
				{
					$('.pop_type_wrap .AllSelectTr').css('display', 'none');
					$('.pop_type_wrap .GroupSelectTr').css('display', '');
					$('.pop_type_wrap .AdcSelectTr').css('display', '');
					$('.pop_type_wrap #target_group').removeAttr("disabled");
					$('.pop_type_wrap #target_adc').removeAttr("disabled");
					$('.pop_type_wrap .VsSelectTr').css('display', '');
					$('.pop_type_wrap #target_vserver').attr("disabled", "disabled");
					$('.pop_type_wrap .FlbGroupSelectTr').css('display', 'none');
					$('.pop_type_wrap .ExtendGroupSelectTr').css('display', 'none');
					$('.pop_type_wrap .list_container').css('display', 'none');
					widgetTargetInfoClear();
					widgetTargetAllOff();
				}
				// 그룹, ADC 까지 선택 가능
				else if (widgetTargerArea == 6)
				{
					$('.pop_type_wrap .AllSelectTr').css('display', 'none');
					$('.pop_type_wrap .GroupSelectTr').css('display', '');
					$('.pop_type_wrap .AdcSelectTr').css('display', '');
					$('.pop_type_wrap #target_group').removeAttr("disabled");
					$('.pop_type_wrap #target_adc').removeAttr("disabled");
					$('.pop_type_wrap .VsSelectTr').css('display', '');
					$('.pop_type_wrap #target_vserver').attr("disabled", "disabled");
					$('.pop_type_wrap .FlbGroupSelectTr').css('display', 'none');
					$('.pop_type_wrap .ExtendGroupSelectTr').css('display', 'none');
					$('.pop_type_wrap .list_container').css('display', 'none');
					widgetTargetInfoClear();
					widgetTargetAllOff();
				}
				// 전체, 그룹, ADC 까지 선택 가능
				else if (widgetTargerArea == 7)
				{
					$('.pop_type_wrap .AllSelectTr').css('display', 'none');
					$('.pop_type_wrap .GroupSelectTr').css('display', '');
					$('.pop_type_wrap .AdcSelectTr').css('display', '');
					$('.pop_type_wrap #target_group').removeAttr("disabled");
					$('.pop_type_wrap #target_adc').removeAttr("disabled");
					$('.pop_type_wrap .VsSelectTr').css('display', '');
					$('.pop_type_wrap #target_vserver').attr("disabled", "disabled");
					$('.pop_type_wrap .FlbGroupSelectTr').css('display', 'none');
					$('.pop_type_wrap .ExtendGroupSelectTr').css('display', 'none');
					$('.pop_type_wrap .list_container').css('display', 'none');
					widgetTargetInfoClear();
					widgetTargetAllOn();
				}
				// VS/Service 까지 필수 선택
				else if (widgetTargerArea == 8 || widgetTargerArea == 13)
				{
					$('.pop_type_wrap .AllSelectTr').css('display', 'none');
					$('.pop_type_wrap .GroupSelectTr').css('display', '');
					$('.pop_type_wrap .AdcSelectTr').css('display', '');
					$('.pop_type_wrap #target_group').removeAttr("disabled");
					$('.pop_type_wrap #target_adc').removeAttr("disabled");
					$('.pop_type_wrap .VsSelectTr').css('display', '');
					$('.pop_type_wrap #target_vserver').removeAttr("disabled");
					$('.pop_type_wrap .FlbGroupSelectTr').css('display', 'none');
					$('.pop_type_wrap .ExtendGroupSelectTr').css('display', 'none');
					$('.pop_type_wrap .list_container').css('display', 'none');
					widgetTargetInfoClear();
					widgetTargetAllOff();
				}
				// ADC 필수선택, VS까지 선택 가능
				else if (widgetTargerArea == 9)
				{
					$('.pop_type_wrap .AllSelectTr').css('display', 'none');
					$('.pop_type_wrap .GroupSelectTr').css('display', '');
					$('.pop_type_wrap .AdcSelectTr').css('display', '');
					$('.pop_type_wrap #target_group').removeAttr("disabled");
					$('.pop_type_wrap #target_adc').removeAttr("disabled");
					$('.pop_type_wrap .VsSelectTr').css('display', '');
					$('.pop_type_wrap #target_vserver').removeAttr("disabled");
					$('.pop_type_wrap .FlbGroupSelectTr').css('display', 'none');
					$('.pop_type_wrap .ExtendGroupSelectTr').css('display', 'none');
					$('.pop_type_wrap .list_container').css('display', 'none');
					widgetTargetInfoClear();
					widgetTargetAllOff();
				}
				// FLB 그룹까지 필수 선택
				else if (widgetTargerArea == 10)
				{
					$('.pop_type_wrap .AllSelectTr').css('display', 'none');
					$('.pop_type_wrap .GroupSelectTr').css('display', '');
					$('.pop_type_wrap .AdcSelectTr').css('display', '');
					$('.pop_type_wrap #target_group').removeAttr("disabled");
					$('.pop_type_wrap #target_adc').removeAttr("disabled");					
					$('.pop_type_wrap .VsSelectTr').css('display', 'none');
					$('.pop_type_wrap .FlbGroupSelectTr').css('display', '');
					$('.pop_type_wrap #target_flbGroup').removeAttr("disabled");
					$('.pop_type_wrap .ExtendGroupSelectTr').css('display', 'none');
					$('.pop_type_wrap .list_container').css('display', 'none');
					widgetTargetInfoClear();
					widgetTargetAllOff();
				}
				// Alteon ADC만 Load
				else if (widgetTargerArea == 11)
				{
					$('.pop_type_wrap .AllSelectTr').css('display', 'none');
					$('.pop_type_wrap .GroupSelectTr').css('display', 'none');
					$('.pop_type_wrap .AdcSelectTr').css('display', '');					
					$('.pop_type_wrap #target_adc').removeAttr("disabled");
					$('.pop_type_wrap .VsSelectTr').css('display', 'none');					
					$('.pop_type_wrap .FlbGroupSelectTr').css('display', 'none');
					$('.pop_type_wrap .ExtendGroupSelectTr').css('display', 'none');
					$('.pop_type_wrap .list_container').css('display', 'none');
					
					OBajaxManager.runJson({
						url	: '/dashboard/loadWidgetTargetInfoAlteon.action',
						data :
						{
							"widgetTarget.category" : 1,
							"widgetTarget.index" : 2
						},
						successFn : function(data) 
						{
							var $adcSelectTarget = $('.pop_type_wrap .adcContent');
							var widgetTargetArea = $('.pop_type_wrap #widget_select').children('option').filter(':selected').attr('id');
							$adcSelectTarget.empty();
							if ( widgetTargetArea == 1 || widgetTargetArea == 2 || widgetTargetArea == 3)
							{
								$('<select name="" id="target_adc" class="widget_popup_Select" disabled="disabled"></select>').appendTo($adcSelectTarget);	
							}
							else
							{							
								$('<select name="" id="target_adc" class="widget_popup_Select"></select>').appendTo($adcSelectTarget);
							}								
							targetChangeEvent();

							var html = '';
							var target = data.widgetTargetList;
							html += '<option value="0">ADC Select&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>';
							for (var i=0; i < target.length; i++) 
							{
								html += '<option value="'+target[i].index+'" vendor="'+target[i].vendor+'">'+target[i].name+'</option>';
							}							
							$('.pop_type_wrap #target_adc').empty();
							$('.pop_type_wrap #target_vserver').empty();
							$('.pop_type_wrap #target_flbGroup').empty();
							$('.pop_type_wrap #target_extend_group').empty();							
							$('.pop_type_wrap #target_adc').html(html);
						}
					});
				}
				else if (widgetTargerArea == 14)
				{
					$('.pop_type_wrap .AllSelectTr').css('display', 'none');
					$('.pop_type_wrap .GroupSelectTr').css('display', '');
					$('.pop_type_wrap .AdcSelectTr').css('display', '');					
					$('.pop_type_wrap .VsSelectTr').css('display', '');
					$('.pop_type_wrap #target_group').removeAttr("disabled");
					$('.pop_type_wrap #target_adc').removeAttr("disabled");
					$('.pop_type_wrap #target_vserver').removeAttr("disabled");
					$('.pop_type_wrap .FlbGroupSelectTr').css('display', 'none');
					$('.pop_type_wrap .ExtendGroupSelectTr').css('display', 'none');
					$('.pop_type_wrap .list_container').css('display', '');
					widgetTargetInfoClear();
					widgetTargetAllOff();					
				}				
			}				
			
			// Target Select Box 및 요약 부분 초기화
			function widgetTargetInfoClear()
			{
				var $viewGroup = $('.widget_summery_table .selected_group').filter(':last');					// Group 요약 Name
				var $viewGroup_index = $('.widget_summery_table .selected_group_index').filter(':last');		// Group 요약 Index
				var $viewADC = $('.widget_summery_table .selected_adc').filter(':last');						// ADC 요약 Name
				var $viewADC_index = $('.widget_summery_table .selected_adc_index').filter(':last');			// ADC 요약 Index
				var $viewVserver = $('.widget_summery_table .selected_vserver').filter(':last');				// Vserver 요약 Name
				var $viewVserver_index = $('.widget_summery_table .selected_vserver_index').filter(':last');	// Vserver 요약 Index	
				
				$viewGroup.html("-");
				$viewGroup_index.html(0);
				$viewADC.html("-");
				$viewADC_index.html(0);		
				$viewVserver.html("-");		
				$viewVserver_index.html(0);					
				$('.pop_type_wrap #target_adc').empty().html('<option value="0">ADC Select&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>');					
				$('.pop_type_wrap #target_vserver').empty().html('<option value="0">VS Select&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>');
				$('.pop_type_wrap #target_flbGroup').empty().html('<option value="0">FLB Group Select&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>');					
				$('.pop_type_wrap #target_extend_group').empty().html('<option value="0">FLB Group Select&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>');					
				$('.pop_type_wrap .list_container .list').empty();
				$('.pop_type_wrap #target_group').val(0).attr("selected", "selected");
				$('.pop_type_wrap #target_adc').val(0).attr("selected", "selected");
				$('.pop_type_wrap #target_vserver').val(0).attr("selected", "selected");
				$('.pop_type_wrap #target_flbGroup').val(0).attr("selected", "selected");
				$('.pop_type_wrap #target_extend_group').val(0).attr("selected", "selected");
				
			}
			// Group Select Box 에 "전체그룹" 활성화 ( 타겟 전체선택이 가능한 위젯 )
			function widgetTargetAllOn()
			{
				$('.pop_type_wrap #target_group option[value="-1"]').removeAttr("disabled");
			}
			// Group Select Box 에 "전체그룹" 비활성화 ( 타겟 전체선택이 불필요한 위젯 )
			function widgetTargetAllOff()
			{					
				$('.pop_type_wrap #target_group option[value="-1"]').attr("disabled", "disabled");
			}
		}
	},
	// 구조상 Select Box Change Event 는 targetChangeEvent로 따로 분리합니다. (add widget Popup Event More)
	targetChangeEvent : function()
	{
		with(this)
		{
			var $viewcategory_index = $('.widget_summery_table .selected_category').filter(':last');		// 선택한 Category 
			var $viewGroup = $('.widget_summery_table .selected_group').filter(':last');					// 선택한 Group Name
			var $viewGroup_index = $('.widget_summery_table .selected_group_index').filter(':last');		// 선택한 Group Index
			var $viewADC = $('.widget_summery_table .selected_adc').filter(':last');						// 선택한 ADC Name
			var $viewADC_index = $('.widget_summery_table .selected_adc_index').filter(':last');			// 선택한 ADC Index
			var $viewADC_vendor = $('.widget_summery_table .selected_adc_vendor').filter(':last');			// 선택한 ADC vendor
			var $viewVserver = $('.widget_summery_table .selected_vserver').filter(':last');				// 선택한 Vserver Name
			var $viewVserver_index = $('.widget_summery_table .selected_vserver_index').filter(':last');	// 선택한 Vserver Index					
			var $viewSelect_list = $('.widget_summery_table .selected_select_list').filter(':last');
			var $viewSelect_list_count = $('.widget_summery_table .selected_select_list_count').filter(':last');
			//그룹 Select Box Change 기능
			$('.pop_type_wrap #target_group').change(function(e)
			{
				e.preventDefault();
				// 요약에 선택된 그룹을 Write 하는 기능
				var $selectedGroupName = $(this).children('option').filter(':selected').text();
				var $selectedGroupVal = $(this).children('option').filter(':selected').val();
				
				if ($selectedGroupVal == 0)
				{
					$viewGroup.html("-");
					$viewGroup_index.html(0);
				}
				else
				{
					$viewGroup.html($selectedGroupName);
					$viewGroup_index.html($selectedGroupVal);
				}				
				$viewADC.html("-");
				$viewADC_index.html(0);
				$viewADC_vendor.html(0);
				$viewVserver.html("-");
				$viewVserver_index.html(0);
				$viewSelect_list.html();
				$viewSelect_list_count.html(0);
				
				// 선택된 그룹의 ADC List Get Json
				/*if ($(this).val() == -1)
					return;*/
				
				OBajaxManager.runJson({
					url	: '/dashboard/loadWidgetTargetInfo.action',
					data :
					{
						"widgetTarget.category" : 1,
						"widgetTarget.index" : $(this).val()
					},
					successFn : function(data) 
					{
						var $adcSelectTarget = $('.pop_type_wrap .adcContent');
						var widgetTargetArea = $('.pop_type_wrap #widget_select').children('option').filter(':selected').attr('id');
						$adcSelectTarget.empty();
						if ( widgetTargetArea == 1 || widgetTargetArea == 2 || widgetTargetArea == 3)
						{
							$('<select name="" id="target_adc" class="widget_popup_Select" disabled="disabled"></select>').appendTo($adcSelectTarget);	
						}
						else
						{							
							$('<select name="" id="target_adc" class="widget_popup_Select"></select>').appendTo($adcSelectTarget);
						}								
						targetChangeEvent();

						var html = '';
						var target = data.widgetTargetList;
						html += '<option value="0">ADC Select&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>';
						for (var i=0; i < target.length; i++) 
						{
							html += '<option value="'+target[i].index+'" vendor="'+target[i].vendor+'">'+target[i].name+'</option>';
						}							
						$('.pop_type_wrap #target_adc').empty();
						$('.pop_type_wrap #target_vserver').empty();						
						$('.pop_type_wrap #target_adc').html(html);						
						$('.pop_type_wrap #target_vserver').html('<option value="0">' + "VS Select&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + '</option>');											
					}				
				});
			});
			
			// ADC Select Box Change 기능
			$('.pop_type_wrap #target_adc').change(function(e)
			{
				e.preventDefault();
				// 요약에 선택된 ADC Write 하는 기능
				var $selectedAdcName = $(this).children('option').filter(':selected').text();
				var $selectedAdcVal = $(this).children('option').filter(':selected').val();
				var $selectedAdcVendor = $(this).children('option').filter(':selected').attr('vendor');
				
				if ($selectedAdcVal == 0)
				{
					$viewADC.html("-");
					$viewADC_index.html(0);
					$viewADC_vendor.html(0);
				}
				else
				{
					$viewADC.html($selectedAdcName);
					$viewADC_index.html($selectedAdcVal);
					$viewADC_vendor.html($selectedAdcVendor);
				}				
				$viewVserver.html("-");
				$viewVserver_index.html(0);
				$viewSelect_list.html();
				$viewSelect_list_count.html(0);
				
				var widgetTargerArea = $('.pop_type_wrap #widget_select').children('option').filter(':selected').attr('id');
				
				if ($(this).val() == 0)
				{
					var html = '<option value="0">' + "VS Select" + '</option>';
					$('.pop_type_wrap #target_vserver').html(html);
				}
				else if (widgetTargerArea == 10)
				{
					// 선택된 ADC의 FLB Group List get Json
					OBajaxManager.runJson({
						url	: '/dashboard/loadWidgetTargetFlbGroupInfo.action',
						data :
						{	
							"widgetTarget.category" : 2,
							"widgetTarget.index" : $(this).val()
						},
						successFn : function(data) 
						{
							var $flbGroupSelectTarget = $('.pop_type_wrap .flbGroupContent');
							var widgetTargetArea = $('.pop_type_wrap #widget_select').children('option').filter(':selected').attr('id');
							$flbGroupSelectTarget.empty();
							if (widgetTargetArea == 1 || widgetTargetArea == 2 || widgetTargetArea == 3 || widgetTargetArea == 4 || widgetTargetArea == 7 )
							{
								$('<select name="" id="target_flbGroup" class="widget_popup_Select" disabled="disabled"></select>').appendTo($flbGroupSelectTarget);		
							}
							else
							{
								$('<select name="" id="target_flbGroup" class="widget_popup_Select"></select>').appendTo($flbGroupSelectTarget);		
							}								
							targetChangeEvent();							
							var html = '';
							var target = data.widgetTargetList;						
							html += '<option value="0">' + "FLB Group Select" + '</option>';
							for (var i=0; i < target.length; i++) 
							{
								html += '<option value="' + target[i].strIndex + '">' + target[i].strIndex + "(" +target[i].name + ')</option>';							
							}							
													
							$('.pop_type_wrap #target_flbGroup').empty();
							$('.pop_type_wrap #target_flbGroup').html(html);
						}				
					});
				}
				else
				{
					// 선택된 ADC의 Vserver List Get Json
					OBajaxManager.runJson({
						url	: '/dashboard/loadWidgetTargetInfo.action',
						data :
						{	
							"widgetTarget.category" : 2,
							"widgetTarget.index" : $(this).val()
						},
						successFn : function(data) 
						{
							var $vsSelectTarget = $('.pop_type_wrap .vsContent');
							var widgetTargetArea = $('.pop_type_wrap #widget_select').children('option').filter(':selected').attr('id');
							$vsSelectTarget.empty();
							if (widgetTargetArea == 1 || widgetTargetArea == 2 || widgetTargetArea == 3 || widgetTargetArea == 4 || widgetTargetArea == 6 ||widgetTargetArea == 7 || widgetTargetArea == 12)
							{
								$('<select name="" id="target_vserver" class="widget_popup_Select" disabled="disabled"></select>').appendTo($vsSelectTarget);		
							}
							else
							{
								$('<select name="" id="target_vserver" class="widget_popup_Select"></select>').appendTo($vsSelectTarget);		
							}								
							targetChangeEvent();							
							var html = '';
							var target = data.widgetTargetList;						
							html += '<option value="0">' + "VS Select" + '</option>';
							for (var i=0; i < target.length; i++) 
							{
								html += '<option value="' + target[i].strIndex + '">' + target[i].name + ":" +target[i].extraInfo + '</option>';							
							}							
													
							$('.pop_type_wrap #target_vserver').empty();
							$('.pop_type_wrap #target_vserver').html(html);
						}				
					});
				}				
			});
			
			//  Vserver Select Box Change 기능
			$('.pop_type_wrap #target_vserver').change(function(e)
			{
				e.preventDefault();
				// 요약에 선택된 Vserver write
				var widgetTargerArea = $('.pop_type_wrap #widget_select').children('option').filter(':selected').attr('id');
				var $selectedVserverName = $(this).children('option').filter(':selected').text();
				var $selectedVserverVal = $(this).children('option').filter(':selected').val();				
				if($selectedVserverVal == 0)
				{
					$viewVserver.html("-");
					$viewVserver_index.html(0);
				}
				else
				{
					$viewVserver.html($selectedVserverName);
					$viewVserver_index.html($selectedVserverVal);
				}
				$viewSelect_list.html();
				$viewSelect_list_count.html(0);
				
				if (widgetTargerArea == 14) // VS/SERVICE 를 선택했을때 Member 정보 notice 가 필요할 경우 ( 14. 맴버 상태알리미)
				{
					var categoryParam = 5;
					OBajaxManager.runJson({
						url	: '/dashboard/loadWidgetTargetInfo.action',
						data :
						{	
							"widgetTarget.category" : categoryParam,
							"widgetTarget.strIndex" : $(this).val()
						},
						successFn : function(data) 
						{							
							var $vsSelectTarget = $('.pop_type_wrap .list_container .list');												
							targetChangeEvent();							
							var html = '';
							var target = data.widgetTargetList;
							for (var i=0; i < target.length; i++) 
							{
								html += '<div><input type="checkbox" class="list_checkbox" value="'+ target[i].strIndex+'"/>' + target[i].desciption + '</div>';							
							}													
							$vsSelectTarget.empty();
							$vsSelectTarget.html(html);							
							$('.pop_type_wrap .list_checkbox').change(function(e)
							{
								e.preventDefault();	
								var checkedItem = $('.cloneDiv .list_checkbox:checked').map(function () {
									  return this.value;
									}).get().toString();
								var chendItemLength = $('.cloneDiv .list_checkbox:checked').length;
								$viewSelect_list.html(checkedItem);
								$viewSelect_list_count.html(chendItemLength);
							});
						}				
					});
				}
			});
			
			//  FLB Group Box Change 기능
			$('.pop_type_wrap #target_flbGroup').change(function(e)
			{
				e.preventDefault();
				// 요약에 선택된 Vserver write
				var $selectedVserverName = $(this).children('option').filter(':selected').text();
				var $selectedVserverVal = $(this).children('option').filter(':selected').val();				
				if($selectedVserverVal == 0)
				{
					$viewVserver.html("-");
					$viewVserver_index.html(0);
				}
				else
				{
					$viewVserver.html($selectedVserverName);
					$viewVserver_index.html($selectedVserverVal);
				}
				$viewSelect_list.html();
				$viewSelect_list_count.html();
			});
		//  Extend Group Box Change 기능
			$('.pop_type_wrap #target_extend_group').change(function(e)
			{
				e.preventDefault();				
				//  선택된 그룹을 Write 하는 기능
				var $selectedExtendGroupName = $(this).children('option').filter(':selected').text();
				var $selectedExtendGroupVal = $(this).children('option').filter(':selected').val();				
				var $selectedExtendCategory = $(this).children('option').filter(':selected').attr('categoryvalue');
				
				if ($selectedExtendGroupVal == 0)
				{
					$viewcategory_index.html(0);
					$viewGroup.html("-");
					$viewGroup_index.html(0);
				}
				else
				{
					$viewcategory_index.html($selectedExtendCategory);
					$viewGroup.html($selectedExtendGroupName);
					$viewGroup_index.html($selectedExtendGroupVal);
				}				
				$viewADC.html("-");
				$viewADC_index.html(0);
				$viewADC_vendor.html(0);
				$viewVserver.html("-");
				$viewVserver_index.html(0);
				
				var widgetTargerArea = $('.pop_type_wrap #widget_select').children('option').filter(':selected').attr('id');
				var categoryParam = undefined;				
				if ($(this).val() == 0)
				{
					$('.pop_type_wrap .list_container .list').empty();
					$viewSelect_list.html();
					$viewSelect_list_count.html(0);
					return;
				}
				else if (widgetTargerArea == 8 || widgetTargerArea == 9)
				{
					categoryParam = 8;
				}
				else if (widgetTargerArea == 12)
				{
					categoryParam = 1;
				}
				else if (widgetTargerArea == 13)
				{
					categoryParam = 8;
				}
				else if (widgetTargerArea == 14)
				{
					categoryParam = 9;
				}
				else
				{
					return;
				}
				// 선택된 서비스 그룹의 서비스 리스트
				OBajaxManager.runJson({
					url	: '/dashboard/loadWidgetTargetInfo.action',
					data :
					{	
						"widgetTarget.category" : categoryParam,
						"widgetTarget.index" : $(this).val()
					},
					successFn : function(data) 
					{
						if (widgetTargerArea == 8 || widgetTargerArea == 9) // 응답시간, ConcurrentSession, Throughput
						{
							var $vsSelectTarget = $('.pop_type_wrap .list_container .list');												
							targetChangeEvent();							
							var html = '';
							var target = data.widgetTargetList;
							for (var i=0; i < target.length; i++) 
							{
								html += '<div id="' + target[i].strIndex + '">' + target[i].desciption + '</div>';							
							}
						}
						else if(widgetTargerArea == 12) //  ADC 상태 알리미 면서 그룹일 경우
						{
							var $vsSelectTarget = $('.pop_type_wrap .list_container .list');												
							targetChangeEvent();							
							var html = '';
							var target = data.widgetTargetList;
							for (var i=0; i < target.length; i++) 
							{
								html += '<div id="' + target[i].index + '">' + target[i].name + '</div>';							
							}	
						}
						else if (widgetTargerArea == 13) // 서비스 상태 알리미면서, 그룹일 경우
						{
							var $vsSelectTarget = $('.pop_type_wrap .list_container .list');												
							targetChangeEvent();							
							var html = '';
							var target = data.widgetTargetList;
							for (var i=0; i < target.length; i++) 
							{
								html += '<div id="' + target[i].strIndex + '">' + target[i].desciption + '</div>';							
							}	
						}
						else if(widgetTargerArea == 14) // 맴버 상태 알리미면서, 그룹일 경우
						{
							var $vsSelectTarget = $('.pop_type_wrap .list_container .list');												
							targetChangeEvent();							
							var html = '';
							var target = data.widgetTargetList;
							for (var i=0; i < target.length; i++) 
							{
								html += '<div><input type="checkbox" class="list_checkbox" value="'+ target[i].strIndex+'"/>' + target[i].desciption + '</div>';							
							}
						}
						else
						{
						}
												
						$vsSelectTarget.empty();
						$vsSelectTarget.html(html);
						$('.pop_type_wrap .list_checkbox').change(function(e)
						{
							e.preventDefault();	
							var checkedItem = $('.cloneDiv .list_checkbox:checked').map(function () {
								  return this.value;
								}).get().toString();
							var chendItemLength = $('.cloneDiv .list_checkbox:checked').length;
							$viewSelect_list.html(checkedItem);
							$viewSelect_list_count.html(chendItemLength);
						});
					}				
				});
			});		
		}
	},
	validateWidgetAdd : function(widgetAddKey, targetArea, Type)
	{
		with(this)
		{
			var widgetTargerArea = undefined;
			var widgetType = undefined;
			var $targetGroup = undefined;
			var $targetAdc = undefined;
			var $targetVserver = undefined;					
			var $moreInfoTime = undefined;
			var $moreInfoStatus = undefined;
			var $moreInfoStatusVs = undefined;
			var $moreInfoStatusOrdering = undefined;
			var $moreInfoChart = undefined;
			var $widgetSelectMode = undefined;			
			var $viewSelect_list_count = undefined;
			
			if (widgetAddKey == true)
			{
				widgetTargerArea = $('.pop_type_wrap #widget_select').children('option').filter(':selected').attr('id');
				widgetType = $('.pop_type_wrap #widget_select').children('option').filter(':selected').val();
				$targetGroup = $('.pop_type_wrap .selected_group_index').filter(':last').text();
				$targetAdc = $('.pop_type_wrap .selected_adc_index').filter(':last').text();
				$targetVserver = $('.pop_type_wrap .selected_vserver_index').filter(':last').text();							
				$moreInfoTime = $('.pop_type_wrap .selected_time').filter(':last').text();
				$moreInfoStatus = $('.pop_type_wrap .selected_status').filter(':last').text();
				$moreInfoStatusVs = $('.pop_type_wrap .selected_statusVs').filter(':last').text();
				$moreInfoStatusOrdering = $('.pop_type_wrap .selected_ordering').filter(':last').text();
				$moreInfoChart = $('.pop_type_wrap .selected_chart').filter(':last').text();
				$widgetSelectMode = $('.cloneDiv input[name="target_type"]:checked').val();				
				$viewSelect_list_count = $('.pop_type_wrap .selected_select_list_count').filter(':last').text();
			}
			else
			{
				widgetTargerArea = targetArea;
				widgetType = Type;
				$targetGroup = $('.pop_type_wrap .selected_group_index_Modify').filter(':last').text();
				if(widgetTargerArea == 11)
					$targetAdc = $('.pop_type_wrap .adcContent #target_adc').filter(':last').val();
				else
					$targetAdc = $('.pop_type_wrap .selected_adc_index_Modify').filter(':last').text();
				$targetVserver = $('.pop_type_wrap .selected_vserver_index_Modify').filter(':last').text();								
				$moreInfoTime = $('.pop_type_wrap .selected_time_Modify').filter(':last').text();
				$moreInfoStatus = $('.pop_type_wrap .selected_status_Modify').filter(':last').text();
				$moreInfoStatusVs = $('.pop_type_wrap .selected_statusVs_Modify').filter(':last').text();
				$moreInfoStatusOrdering = $('.pop_type_wrap .selected_ordering_Modify').filter(':last').text();
				$moreInfoChart = $('.pop_type_wrap .selected_chart_Modify').filter(':last').text();
				$widgetSelectMode = $('.cloneDiv input[name="target_type_Modify"]:checked').val();
				$viewSelect_list_count = $('.pop_type_wrap .selected_select_list_count_Modify').filter(':last').text();
				
			}			
			// 추가 옵션에 대한 Validate
			
			if (widgetType == 14)
			{
				if($moreInfoStatus == -1 || $moreInfoStatus == "")
				{
					$.obAlertNotice(VAR_DASH_ADDOSSEL);
					return false;
				}
				else
				{
					return true;
				}
			}
			if (widgetType == 15)
			{
				if($moreInfoStatusVs == -1 || $moreInfoStatusVs == "")
				{
					$.obAlertNotice(VAR_DASH_ADDOSSEL);
					return false;
				}
				else
				{
					return true;
				}
			}
			
			if (widgetType == 16)
			{
				if($moreInfoStatusOrdering == -1 || $moreInfoStatusOrdering == "")
				{
					$.obAlertNotice(VAR_DASH_ADDOOSEL);
					return false;
				}
				else
				{
					return true;
				}
			}			
			
			if (widgetTargerArea == 4)	// Target 이 오직 ADC만 충족 (ADC 필히 선택)
			{
				if($targetGroup == 0)
				{
					$.obAlertNotice(VAR_DASH_GROSEL);
				}
				else if($targetAdc == 0)
				{
					$.obAlertNotice(VAR_DASH_ADCSEL);
				}
				else
				{
					return true;
				}
				
			}
			else if (widgetTargerArea == 6 || widgetTargerArea == 12)	// Target 이 오직 ADC 까지 충족 (그룹 필히 선택)
			{
				if($targetGroup == 0)
				{
					$.obAlertNotice(VAR_DASH_GROSEL);
				}				
				else
				{
					return true;
				}
				
			}
			else if (widgetTargerArea == 8) // Target 이 오직 Vserver만 충족 (Vserver 필히 선택)
			{
				if ($widgetSelectMode == "group")
				{
					if($targetGroup == 0)
					{
						$.obAlertNotice(VAR_DASH_GROSEL);
					}
					
					if($moreInfoChart == -1 || $moreInfoChart == "")
					{
						$.obAlertNotice(VAR_DASH_ADDOSSEL);
						return false;
					}
					else
					{
						return true;
					}
				}
				else
				{
					if($targetGroup == 0)
					{
						$.obAlertNotice(VAR_DASH_GROSEL);
					}
					else if($targetAdc == 0)
					{
						$.obAlertNotice(VAR_DASH_ADCSEL);
					}
					else if($targetVserver == 0)
					{
						$.obAlertNotice(VAR_DASH_VSESEL);
					}
					else
					{
						return true;
					}
				}				
			}
			else if (widgetTargerArea == 13) // Target 이 오직 Vserver만 충족 (Vserver 필히 선택)
			{
				if ($widgetSelectMode == "group")
				{
					if($targetGroup == 0)
					{
						$.obAlertNotice(VAR_DASH_GROSEL);
						return false;
					}
					else
					{
						return true;
					}
				}
				else
				{
					if($targetGroup == 0)
					{
						$.obAlertNotice(VAR_DASH_GROSEL);
					}
					else if($targetAdc == 0)
					{
						$.obAlertNotice(VAR_DASH_ADCSEL);
					}
					else if($targetVserver == 0)
					{
						$.obAlertNotice(VAR_DASH_VSESEL);
					}
					else
					{
						return true;
					}
				}				
			}
			else if ( widgetTargerArea == 14) // Target 이 오직 Vserver만 충족 , member 최소 1개 최대 10개 선택
			{
				//TODO ALERT NOTICE 수정 요망
				if ($viewSelect_list_count < 1)
				{
					//TODO ALERT NOTICE 수정 요망
					$.obAlertNotice(VAR_DASH_MEMBERSEL);
					return false;
				}		
				if ($viewSelect_list_count > 10)		
				{
					//TODO ALERT NOTICE 수정 요망
					$.obAlertNotice(VAR_DASH_MEMBERSELCOUNTOVER);
					return false;
				}
				
				if ($widgetSelectMode == "group")
				{
					if($targetGroup == 0)
					{
						$.obAlertNotice(VAR_DASH_GROSEL);
						return false;
					}
					else
					{
						return true;
					}
				}
				else
				{
					if($targetGroup == 0)
					{
						$.obAlertNotice(VAR_DASH_GROSEL);
					}
					else if($targetAdc == 0)
					{
						$.obAlertNotice(VAR_DASH_ADCSEL);
					}
					else if($targetVserver == 0)
					{
						$.obAlertNotice(VAR_DASH_VSESEL);
					}
					else
					{
						return true;
					}
				}				
			}
			else if (widgetTargerArea == 16) // Target 이 오직 Vservice만 충족 (Vservice만 필히 선택)
			{
				if($targetGroup == 0)
				{
					$.obAlertNotice(VAR_DASH_GROSEL);
				}
				else if($targetAdc == 0)
				{
					$.obAlertNotice(VAR_DASH_ADCSEL);
				}
				else if($targetVserver == 0)
				{
					$.obAlertNotice(VAR_DASH_VSESEL);
				}				
				else
				{
					return true;
				}				
			}
			else if (widgetTargerArea == 11)	// Target 이 오직 ADC만 충족 (ADC 필히 선택)
			{
				if($targetAdc == 0)
				{
					$.obAlertNotice(VAR_DASH_ADCSEL);
				}
				else
				{
					return true;
				}
				
			}
			else if (widgetTargerArea == 9)	// Vserver/ service 까지 가능하지만 ADC 까지는 필히 선택
			{
				if ($widgetSelectMode == "group")
				{
					if($targetGroup == 0)
					{
						$.obAlertNotice(VAR_DASH_GROSEL);
					}
					if($moreInfoChart == -1 || $moreInfoChart == "")
					{
						$.obAlertNotice(VAR_DASH_ADDOSSEL);
						return false;
					}
					else
					{
						return true;
					}
				}
				else
				{
					if($targetGroup == 0)
					{
						$.obAlertNotice(VAR_DASH_GROSEL);
					}
					else if($targetAdc == 0)
					{
						$.obAlertNotice(VAR_DASH_ADCSEL);
					}
					else
					{
						return true;
					}
				}
				
				
			}
			else if (widgetTargerArea == 10)	// FLB Group 까지는 필히 선택
			{
				if($targetGroup == 0)
				{
					$.obAlertNotice(VAR_DASH_GROSEL);
				}
				else if($targetAdc == 0)
				{
					$.obAlertNotice(VAR_DASH_ADCSEL);
				}
				else if($targetVserver == 0)  // FLB Group 정보는 Vserver 에 담는다. view* 에서는 Vserver와 동일 레벨로 존재 하기때문에
				{
					$.obAlertNotice(VAR_DASH_FLBGROUP);
				}
				else
				{
					return true;
				}
				
			}
			else
			{
				return true;
			}
		}
	},
	// 위젯 대상 수정 팝업 시작
	// Widget Target Change Popup
	loadWidgetModifyPopup : function(index, widgetItemList)
	{
		with(this)
		{
			showPopup({
				'id' : '#widgetModifyPopup',
				'width' : '288px',
				'height' : '455px'
			});
			
			// 대상선택 Radio Btn 초기화.
			$('.cloneDiv #select_typeall_Modify').attr('checked', 'checked');
			
			// 위젯 Item 정보에서, 선택한 위젯 Type에 맞는 Widget Target Area 정보 추출
			var selectedWidgetType = $('.column #'+index+' .widgetType').val();
			var selectedWidgetTargetArea = undefined;
			for(var i=0; i<widgetItemList.length; i++)
			{
				if (widgetItemList[i].index == selectedWidgetType)
				{
					selectedWidgetTargetArea = widgetItemList[i].targetArea;
					break;
				}				
			}
			
			var targetTypeValue = $('.cloneDiv input[name="target_type_Modify"]:checked').val();
			widgetTargetModifySelectBoxChanger(index, widgetItemList, targetTypeValue);			
			
			// 전체 그룹 List Get (Basic Active)
			OBajaxManager.runJson({
				url	: '/dashboard/loadWidgetTargetInfo.action',
				data :
				{
					"widgetTarget.category" : 0
				},
				successFn : function(data) 
				{
					var html = '';					
					if (data.widgetTargetList)
					{
						html += '<option value="0">' + VAR_DASH_GROUP + " Select&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + '</option>';
						html += '<option value="-1">' + VAR_DASH_GROUPA + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + '</option>';
						for (var i=0; i < data.widgetTargetList.length; i++) 
						{
							html += '<option value="' + data.widgetTargetList[i].index + '">' + data.widgetTargetList[i].name + '</option>';							
						}
						$('.pop_type_wrap #target_group_Modify').empty();
						$('.pop_type_wrap #target_adc_Modify').empty();
						$('.pop_type_wrap #target_vserver_Modify').empty();						
						$('.pop_type_wrap #target_group_Modify').html(html);
						$('.pop_type_wrap #target_adc_Modify').html('<option value="0">' + "ADC Select&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + '</option>');
						$('.pop_type_wrap #target_vserver_Modify').html('<option value="0">' + "VS Select&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + '</option>');	
						$('.pop_type_wrap .list_container_Modify .list').empty();
					}
				}
			});			
			widgetModifyTargetChangeEvent(selectedWidgetTargetArea);		
			
			var $selected_status = $('.pop_type_wrap .selected_status_Modify').filter(':last');
			var $selected_statusVs = $('.pop_type_wrap .selected_statusVs_Modify').filter(':last');
			var $viewSelected_time = $('.pop_type_wrap .selected_time_Modify').filter(':last');
			var $viewSelected_Ordering = $('.pop_type_wrap .selected_ordering_Modify').filter(':last');
			var $viewSelected_Chart = $('.pop_type_wrap .selected_chart_Modify').filter(':last');
			
			$('.pop_type_wrap #moreInfo_Status_Modify').change(function(e)
			{
				var $html = '';
				$html = $(this).children('option').filter(':selected').val();
				$selected_status.empty();
				$selected_statusVs.empty();
				$viewSelected_time.empty();
				$viewSelected_Ordering.empty();
				$viewSelected_Chart.empty();
				$selected_status.html($html);
			});
			
			$('.pop_type_wrap #moreInfo_StatusVs_Modify').change(function(e)
			{
				var $html = '';
				$html = $(this).children('option').filter(':selected').val();
				$selected_status.empty();
				$selected_statusVs.empty();
				$viewSelected_time.empty();
				$viewSelected_Ordering.empty();
				$viewSelected_Chart.empty();
				$selected_statusVs.html($html);		
			});
			$('.pop_type_wrap #moreInfo_Time_Modify').change(function(e)
			{
				var $html = '';
				$html = $(this).children('option').filter(':selected').val();
				$selected_status.empty();
				$selected_statusVs.empty();
				$viewSelected_time.empty();
				$viewSelected_Ordering.empty();
				$viewSelected_Chart.empty();
				$viewSelected_time.html($html);		
			});
			$('.pop_type_wrap #moreInfo_Ordering_Modify').change(function(e)
			{
				var $html = '';
				$html = $(this).children('option').filter(':selected').val();
				$selected_status.empty();
				$selected_statusVs.empty();
				$viewSelected_time.empty();
				$viewSelected_Ordering.empty();
				$viewSelected_Chart.empty();
				$viewSelected_Ordering.html($html);		
			});
			$('.pop_type_wrap #moreInfo_Chart_Modify').change(function(e)
			{
				var $html = '';
				$html = $(this).children('option').filter(':selected').val();
				$selected_status.empty();
				$selected_statusVs.empty();
				$viewSelected_time.empty();
				$viewSelected_Ordering.empty();
				$viewSelected_Chart.empty();
				$viewSelected_Chart.html($html);		
			});
			
			// 위젯 수정 적용 Btn
			$('.pop_type_wrap #widget_modify').click(function(e)
			{
				e.preventDefault();				
				var widgetAddKey = false;	// widgetModify 일때는 Validate 함수에 false 전달
				if(!validateWidgetAdd(widgetAddKey, selectedWidgetTargetArea, selectedWidgetType))
				{
					return false;
				}
				modifyWidget(index, G_widgetList, G_refershChartObj, selectedWidgetTargetArea);
			});
			
			// 위젯 수정 닫기 Btn
			$('.pop_type_wrap #widget_modify_close').click(function(e)
			{
				e.preventDefault();
				$('.popup_type1').remove();
				$('.cloneDiv').remove();
				onAutoRefreshForEvent();
			});
			
			// 대상 선택 Radio Btn Change 기능
			$('.cloneDiv input[name="target_type_Modify"]').change(function(e) 
			{
				e.preventDefault();
				var targetTypeValue = $(this).val();				
				widgetTargetModifySelectBoxChanger(index, widgetItemList, targetTypeValue);
			});
		}
	},
	//TODO target Select Box 분리 함수 (위젯정보수정팝업)
	widgetTargetModifySelectBoxChanger : function(index, widgetItemList, targetType)
	{
		with(this)
		{
			$('.pop_type_wrap .list_container_Modify .list').empty();
			
			var $viewGroup = $('.widget_summery_table_Modify .selected_group_Modify').filter(':last');					// Group 요약 Name
			var $viewGroup_index = $('.widget_summery_table_Modify .selected_group_index_Modify').filter(':last');		// Group 요약 Index
			var $viewADC = $('.widget_summery_table_Modify .selected_adc_Modify').filter(':last');						// ADC 요약 Name
			var $viewADC_index = $('.widget_summery_table_Modify .selected_adc_index_Modify').filter(':last');			// ADC 요약 Index
			var $viewVserver = $('.widget_summery_table_Modify .selected_vserver_Modify').filter(':last');				// Vserver 요약 Name
			var $viewVserver_index = $('.widget_summery_table_Modify .selected_vserver_index_Modify').filter(':last');	// Vserver 요약 Index	
			
			var $moreInfoHeader = $('.pop_type_wrap .moreOptionHeader_Modify');
			var $moreInfoStatus = $('.pop_type_wrap #moreInfo_Status_Modify');
			var $moreInfoStatusVs = $('.pop_type_wrap #moreInfo_StatusVs_Modify');
			var $moreInfoTime = $('.pop_type_wrap #moreInfo_Time_Modify');
			var $moreInfoOrdering = $('.pop_type_wrap #moreInfo_Ordering_Modify');
			var $moreInfoChart = $('.pop_type_wrap #moreInfo_Chart_Modify');
			
			var $targetHeaderRadio = $('.pop_type_wrap .target_header_radio_Modify');	
			
			var $targetHeaderRadioVal = $('.cloneDiv input[name="target_type_Modify"]:checked').val();
			
			// 위젯 Item 정보에서, 선택한 위젯 Type에 맞는 Widget Target Area 정보 추출
			var selectedWidgetType = $('.column #'+index+' .widgetType').val();
			var selectedWidgetTargetArea = undefined;
			for(var i=0; i<widgetItemList.length; i++)
			{
				if (widgetItemList[i].index == selectedWidgetType)
				{
					selectedWidgetTargetArea = widgetItemList[i].targetArea;
					break;
				}				
			}			
			// 추가 옵션 여는 기능				
			var widgetType = selectedWidgetType;			
			if (widgetType == 1)
			{
				$moreInfoHeader.css('display', '');
				$moreInfoStatus.parent().parent().css('display', 'none');
				$moreInfoStatusVs.parent().parent().css('display', 'none');
				$moreInfoTime.parent().parent().css('display', '');
				$moreInfoOrdering.parent().parent().css('display', 'none');
				$moreInfoChart.parent().parent().css('display', 'none');
				
				$targetHeaderRadio.css('display', 'none');
			}
			else if (widgetType == 8 || widgetType == 9 )	// Concurrent session, Throughput chart 장애
			{
				if($targetHeaderRadioVal == "all")
				{
					$moreInfoHeader.css('display', 'none');
					$moreInfoChart.parent().parent().css('display', 'none');
				}
				else
				{
					$moreInfoHeader.css('display', '');
					$moreInfoChart.parent().parent().css('display', '');
				}
				$moreInfoStatus.parent().parent().css('display', 'none');
				$moreInfoStatusVs.parent().parent().css('display', 'none');
				$moreInfoTime.parent().parent().css('display', 'none');
				$moreInfoOrdering.parent().parent().css('display', 'none');
				
				
				$targetHeaderRadio.css('display', '');
			}
			
			else if (widgetType == 14)
			{
				$moreInfoHeader.css('display', '');
				$moreInfoStatus.parent().parent().css('display', '');
				$moreInfoStatusVs.parent().parent().css('display', 'none');
				$moreInfoTime.parent().parent().css('display', 'none');
				$moreInfoOrdering.parent().parent().css('display', 'none');
				$moreInfoChart.parent().parent().css('display', 'none');
				
				$targetHeaderRadio.css('display', 'none');
			}
			else if (widgetType == 15)
			{
				$moreInfoHeader.css('display', '');
				$moreInfoStatus.parent().parent().css('display', 'none');
				$moreInfoStatusVs.parent().parent().css('display', '');
				$moreInfoTime.parent().parent().css('display', 'none');
				$moreInfoOrdering.parent().parent().css('display', 'none');
				$moreInfoChart.parent().parent().css('display', 'none');
				
				$targetHeaderRadio.css('display', 'none');
			}
			else if (widgetType == 16)
			{
				$moreInfoHeader.css('display', '');
				$moreInfoStatus.parent().parent().css('display', 'none');
				$moreInfoStatusVs.parent().parent().css('display', 'none');
				$moreInfoTime.parent().parent().css('display', 'none');
				$moreInfoOrdering.parent().parent().css('display', '');
				$moreInfoChart.parent().parent().css('display', 'none');
				
				$targetHeaderRadio.css('display', 'none');
			}
			else if (widgetType == 19)	// 응답시간
			{
				if($targetHeaderRadioVal == "all")
				{
					$moreInfoHeader.css('display', 'none');
					$moreInfoChart.parent().parent().css('display', 'none');
				}
				else
				{
					$moreInfoHeader.css('display', '');
					$moreInfoChart.parent().parent().css('display', '');
				}
				
				$moreInfoStatus.parent().parent().css('display', 'none');
				$moreInfoStatusVs.parent().parent().css('display', 'none');
				$moreInfoTime.parent().parent().css('display', 'none');
				$moreInfoOrdering.parent().parent().css('display', 'none');
				
				$targetHeaderRadio.css('display', 'none'); //응답시간 그룹은 추후에
			}
			else if (widgetType == 24)
			{
				$moreInfoHeader.css('display', 'none');
				$moreInfoStatus.parent().parent().css('display', 'none');
				$moreInfoStatusVs.parent().parent().css('display', 'none');
				$moreInfoTime.parent().parent().css('display', 'none');
				$moreInfoOrdering.parent().parent().css('display', 'none');
				$moreInfoChart.parent().parent().css('display', 'none');
				
				$targetHeaderRadio.css('display', 'none');
			}
			else if (widgetType == 25)
			{
				$moreInfoHeader.css('display', 'none');
				$moreInfoStatus.parent().parent().css('display', 'none');
				$moreInfoStatusVs.parent().parent().css('display', 'none');
				$moreInfoTime.parent().parent().css('display', 'none');
				$moreInfoOrdering.parent().parent().css('display', 'none');
				$moreInfoChart.parent().parent().css('display', 'none');
				
				$targetHeaderRadio.css('display', '');
			}
			else if (widgetType == 26)
			{
				$moreInfoHeader.css('display', 'none');
				$moreInfoStatus.parent().parent().css('display', 'none');
				$moreInfoStatusVs.parent().parent().css('display', 'none');
				$moreInfoTime.parent().parent().css('display', 'none');
				$moreInfoOrdering.parent().parent().css('display', 'none');
				$moreInfoChart.parent().parent().css('display', 'none');
				
				$targetHeaderRadio.css('display', '');
			}
			else
			{
				$moreInfoHeader.css('display', 'none');
				$moreInfoStatus.parent().parent().css('display', 'none');
				$moreInfoStatusVs.parent().parent().css('display', 'none');
				$moreInfoTime.parent().parent().css('display', 'none');
				$moreInfoOrdering.parent().parent().css('display', 'none');
				$moreInfoChart.parent().parent().css('display', 'none');
				
				$targetHeaderRadio.css('display', 'none');
			}
			// 상관없는 Target Disable 하는 기능 (targetArea 사용) FIXME 필히 모듈화가 필요한 부분. 코드 낭비가 심하다.
			if (targetType == "group")	// 대상 선택이 그룹일 경우
			{
				// 상관없는 Target Disable 하는 기능 (targetArea 사용)
				// 전체 선택만 가능
				var widgetTargerArea = selectedWidgetTargetArea;				
				if (widgetTargerArea == 1)
				{
					$('.pop_type_wrap .AllSelectTr_Modify').css('display', '');
					$('.pop_type_wrap .GroupSelectTr_Modify').css('display', 'none');
					$('.pop_type_wrap .AdcSelectTr_Modify').css('display', '');
					//$('.pop_type_wrap #target_group_Modify').attr("disabled", "disabled");
					$('.pop_type_wrap #target_adc_Modify').attr("disabled", "disabled");
					$('.pop_type_wrap .VsSelectTr_Modify').css('display', '');
					$('.pop_type_wrap #target_vserver_Modify').attr("disabled", "disabled");
					$('.pop_type_wrap .FlbGroupSelectTr_Modify').css('display', 'none');
					$('.pop_type_wrap .ExtendGroupSelectTr_Modify').css('display', 'none');
					$('.pop_type_wrap .list_container_Modify').css('display', 'none');
					widgetTargetInfoClear();
				}
				// 전체 선택 및 그룹 선택 가능
				else if (widgetTargerArea == 2)
				{
					$('.pop_type_wrap .AllSelectTr_Modify').css('display', 'none');
					$('.pop_type_wrap .GroupSelectTr_Modify').css('display', '');
					$('.pop_type_wrap .AdcSelectTr_Modify').css('display', '');
					$('.pop_type_wrap #target_group_Modify').removeAttr("disabled");							
					$('.pop_type_wrap #target_adc_Modify').attr("disabled", "disabled");
					$('.pop_type_wrap .VsSelectTr_Modify').css('display', '');
					$('.pop_type_wrap #target_vserver_Modify').attr("disabled", "disabled");
					$('.pop_type_wrap .FlbGroupSelectTr_Modify').css('display', 'none');
					$('.pop_type_wrap .ExtendGroupSelectTr_Modify').css('display', 'none');
					$('.pop_type_wrap .list_container_Modify').css('display', 'none');
					widgetTargetInfoClear();
					widgetTargetAllOn_Modify();
				}
				// ADC 까지 필수 선택
				else if (widgetTargerArea == 4)
				{
					$('.pop_type_wrap .AllSelectTr_Modify').css('display', 'none');
					$('.pop_type_wrap .GroupSelectTr_Modify').css('display', '');
					$('.pop_type_wrap .AdcSelectTr_Modify').css('display', '');
					$('.pop_type_wrap #target_group_Modify').removeAttr("disabled");
					$('.pop_type_wrap #target_adc_Modify').removeAttr("disabled");
					$('.pop_type_wrap .VsSelectTr_Modify').css('display', '');
					$('.pop_type_wrap #target_vserver_Modify').attr("disabled", "disabled");
					$('.pop_type_wrap .FlbGroupSelectTr_Modify').css('display', 'none');
					$('.pop_type_wrap .ExtendGroupSelectTr_Modify').css('display', 'none');
					$('.pop_type_wrap .list_container_Modify').css('display', 'none');
					widgetTargetInfoClear();
					widgetTargetAllOff_Modify();
				}
				// 전체, 그룹, ADC 까지 선택 가능
				else if (widgetTargerArea == 7)
				{
					$('.pop_type_wrap .AllSelectTr_Modify').css('display', 'none');
					$('.pop_type_wrap .GroupSelectTr_Modify').css('display', '');
					$('.pop_type_wrap .AdcSelectTr_Modify').css('display', '');
					$('.pop_type_wrap #target_group_Modify').removeAttr("disabled");
					$('.pop_type_wrap #target_adc_Modify').removeAttr("disabled");
					$('.pop_type_wrap .VsSelectTr_Modify').css('display', '');
					$('.pop_type_wrap #target_vserver_Modify').attr("disabled", "disabled");
					$('.pop_type_wrap .FlbGroupSelectTr_Modify').css('display', 'none');
					$('.pop_type_wrap .ExtendGroupSelectTr_Modify').css('display', 'none');
					$('.pop_type_wrap .list_container_Modify').css('display', 'none');
					widgetTargetInfoClear();
					widgetTargetAllOn_Modify();
				}
				// VS/Service 까지 필수 선택
				else if (widgetTargerArea == 8)
				{
					if(widgetType == 19)
					{
						$('.pop_type_wrap .AllSelectTr_Modify').css('display', 'none');
						$('.pop_type_wrap .GroupSelectTr_Modify').css('display', 'none');							
						$('.pop_type_wrap .AdcSelectTr_Modify').css('display', 'none');
						$('.pop_type_wrap .VsSelectTr_Modify').css('display', 'none');
						$('.pop_type_wrap .FlbGroupSelectTr_Modify').css('display', 'none');							
						$('.pop_type_wrap .ExtendGroupSelectTr_Modify').css('display', '');
						$('.pop_type_wrap #target_extend_group_Modify').removeAttr("disabled");
						$('.pop_type_wrap .list_container_Modify').css('display', '');
						
						var categoryParam = undefined;						
							categoryParam = 7;							
						
						// 선택 그룹 List Get
						OBajaxManager.runJson({
							url	: '/dashboard/loadWidgetTargetInfo.action',
							data :
							{
								"widgetTarget.category" : categoryParam,
								"widgetTarget.desciption" : ""
							},
							successFn : function(data) 
							{
								var html = '';
								if (data.widgetTargetList)
								{
									html += '<option value="0">Select&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>';								
									for (var i=0; i < data.widgetTargetList.length; i++) 
									{
										html += '<option value="' + data.widgetTargetList[i].index + '" categoryValue="' + data.widgetTargetList[i].category + '">' + data.widgetTargetList[i].name + '</option>';							
									}										
									$('.pop_type_wrap #target_adc_Modify').empty();
									$('.pop_type_wrap #target_vserver_Modify').empty();
									$('.pop_type_wrap #target_flbGroup_Modify').empty();
									$('.pop_type_wrap #target_extend_group_Modify').empty();	
									$('.pop_type_wrap #target_extend_group_Modify').html(html);	
								}				
							}				
						});
					}
					else
					{
						$('.pop_type_wrap .AllSelectTr_Modify').css('display', 'none');
						$('.pop_type_wrap .GroupSelectTr_Modify').css('display', '');
						$('.pop_type_wrap .AdcSelectTr_Modify').css('display', '');
						$('.pop_type_wrap #target_group_Modify').removeAttr("disabled");
						$('.pop_type_wrap #target_adc_Modify').removeAttr("disabled");
						$('.pop_type_wrap .VsSelectTr_Modify').css('display', '');
						$('.pop_type_wrap #target_vserver_Modify').removeAttr("disabled");
						$('.pop_type_wrap .FlbGroupSelectTr_Modify').css('display', 'none');
						$('.pop_type_wrap .ExtendGroupSelectTr_Modify').css('display', 'none');
						$('.pop_type_wrap .list_container_Modify').css('display', 'none');
					}
					
					widgetTargetInfoClear();
					widgetTargetAllOff_Modify();
				}						
				// ADC 필수선택, VS까지 선택 가능
				else if (widgetTargerArea == 9)
				{
					if(widgetType == 8 || widgetType == 9)
					{
						$('.pop_type_wrap .AllSelectTr_Modify').css('display', 'none');
						$('.pop_type_wrap .GroupSelectTr_Modify').css('display', 'none');							
						$('.pop_type_wrap .AdcSelectTr_Modify').css('display', 'none');
						$('.pop_type_wrap .VsSelectTr_Modify').css('display', 'none');
						$('.pop_type_wrap .FlbGroupSelectTr_Modify').css('display', 'none');							
						$('.pop_type_wrap .ExtendGroupSelectTr_Modify').css('display', '');
						$('.pop_type_wrap #target_extend_group_Modify').removeAttr("disabled");
						$('.pop_type_wrap .list_container_Modify').css('display', '');
						
						var categoryParam = undefined;						
							categoryParam = 7;							
						
						// 선택 그룹 List Get
						OBajaxManager.runJson({
							url	: '/dashboard/loadWidgetTargetInfo.action',
							data :
							{
								"widgetTarget.category" : categoryParam,
								"widgetTarget.desciption" : ""
							},
							successFn : function(data) 
							{
								var html = '';
								if (data.widgetTargetList)
								{
									html += '<option value="0">Select&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>';								
									for (var i=0; i < data.widgetTargetList.length; i++) 
									{
										html += '<option value="' + data.widgetTargetList[i].index + '" categoryValue="' + data.widgetTargetList[i].category + '">' + data.widgetTargetList[i].name + '</option>';							
									}										
									$('.pop_type_wrap #target_adc_Modify').empty();
									$('.pop_type_wrap #target_vserver_Modify').empty();
									$('.pop_type_wrap #target_flbGroup_Modify').empty();
									$('.pop_type_wrap #target_extend_group_Modify').empty();	
									$('.pop_type_wrap #target_extend_group_Modify').html(html);	
								}				
							}				
						});
					}
					else
					{
						$('.pop_type_wrap .AllSelectTr_Modify').css('display', 'none');
						$('.pop_type_wrap .GroupSelectTr_Modify').css('display', '');
						$('.pop_type_wrap .AdcSelectTr_Modify').css('display', '');
						$('.pop_type_wrap #target_group_Modify').removeAttr("disabled");
						$('.pop_type_wrap #target_adc_Modify').removeAttr("disabled");
						$('.pop_type_wrap .VsSelectTr_Modify').css('display', '');
						$('.pop_type_wrap #target_vserver_Modify').removeAttr("disabled");
						$('.pop_type_wrap .FlbGroupSelectTr_Modify').css('display', 'none');
						$('.pop_type_wrap .ExtendGroupSelectTr_Modify').css('display', 'none');
						$('.pop_type_wrap .list_container_Modify').css('display', 'none');
					}
					
					widgetTargetInfoClear();
					widgetTargetAllOff_Modify();
				}
				// FLB 그룹까지 필수 선택
				else if (widgetTargerArea == 10) // 10 FLB Group
				{
					$('.pop_type_wrap .AllSelectTr_Modify').css('display', 'none');
					$('.pop_type_wrap .GroupSelectTr_Modify').css('display', '');
					$('.pop_type_wrap .AdcSelectTr_Modify').css('display', '');							
					$('.pop_type_wrap #target_group_Modify').removeAttr("disabled");
					$('.pop_type_wrap #target_adc_Modify').removeAttr("disabled");
					$('.pop_type_wrap .VsSelectTr_Modify').css('display', 'none');							
					$('.pop_type_wrap .FlbGroupSelectTr_Modify').css('display', '');
					$('.pop_type_wrap #target_flbGroup_Modify').removeAttr("disabled");
					$('.pop_type_wrap .ExtendGroupSelectTr_Modify').css('display', 'none');
					$('.pop_type_wrap .list_container_Modify').css('display', 'none');
					widgetTargetInfoClear();
					widgetTargetAllOff_Modify();
				}
				else if(widgetTargerArea == 11) // 11 Alteon ADC Only
				{
					$('.pop_type_wrap .AllSelectTr_Modify').css('display', 'none');
					$('.pop_type_wrap .GroupSelectTr_Modify').css('display', 'none');
					$('.pop_type_wrap .AdcSelectTr_Modify').css('display', '');
					$('.pop_type_wrap #target_group_Modify').removeAttr("disabled");
					$('.pop_type_wrap #target_adc_Modify').removeAttr("disabled");
					$('.pop_type_wrap .VsSelectTr_Modify').css('display', 'none');
					$('.pop_type_wrap .FlbGroupSelectTr_Modify').css('display', 'none');					
					$('.pop_type_wrap .list_container_Modify').css('display', 'none');
					
					OBajaxManager.runJson({
						url	: '/dashboard/loadWidgetTargetInfoAlteon.action',
						data :
						{
							"widgetTarget.category" : 1,
							"widgetTarget.index" : 2
						},
						successFn : function(data) 
						{
							var $adcSelectTarget = $('.pop_type_wrap .adcContent');
							var widgetTargetArea = $('.pop_type_wrap #widget_select').children('option').filter(':selected').attr('id');
							$adcSelectTarget.empty();
							if ( widgetTargetArea == 1 || widgetTargetArea == 2 || widgetTargetArea == 3)
							{
								$('<select name="" id="target_adc" class="widget_popup_Select" disabled="disabled"></select>').appendTo($adcSelectTarget);	
							}
							else
							{							
								$('<select name="" id="target_adc" class="widget_popup_Select"></select>').appendTo($adcSelectTarget);
							}								
							targetChangeEvent();

							var html = '';
							var target = data.widgetTargetList;
							html += '<option value="0">ADC Select&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>';
							for (var i=0; i < target.length; i++) 
							{
								html += '<option value="'+target[i].index+'" vendor="'+target[i].vendor+'">'+target[i].name+'</option>';
							}							
							$('.pop_type_wrap #target_adc_Modify').empty();
							$('.pop_type_wrap #VsSelectTr_Modify').empty();
							$('.pop_type_wrap #target_adc_Modify').html(html);						
							$('.pop_type_wrap #VsSelectTr_Modify').html('<option value="0">' + "VS Select&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + '</option>');															
						}				
					});
				}
				else if(widgetTargerArea == 12 || widgetTargerArea == 13 || widgetTargerArea == 14)							
				{
					widgetTargetInfoClear();					
					
					$('.pop_type_wrap .AllSelectTr_Modify').css('display', 'none');
					$('.pop_type_wrap .GroupSelectTr_Modify').css('display', 'none');							
					$('.pop_type_wrap .AdcSelectTr_Modify').css('display', 'none');
					$('.pop_type_wrap .VsSelectTr_Modify').css('display', 'none');
					$('.pop_type_wrap .FlbGroupSelectTr_Modify').css('display', 'none');							
					$('.pop_type_wrap .ExtendGroupSelectTr_Modify').css('display', '');
					$('.pop_type_wrap #target_extend_group_Modify').removeAttr("disabled");
					$('.pop_type_wrap .list_container_Modify').css('display', '');
					
					var categoryParam = undefined;
					if (widgetTargerArea == 12)
						categoryParam = 0;
					else
						categoryParam = 7;							
					
					// 선택 그룹 List Get
					OBajaxManager.runJson({
						url	: '/dashboard/loadWidgetTargetInfo.action',
						data :
						{
							"widgetTarget.category" : categoryParam,
							"widgetTarget.desciption" : ""
						},
						successFn : function(data) 
						{
							var html = '';
							if (data.widgetTargetList)
							{
								html += '<option value="0">Select&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>';								
								for (var i=0; i < data.widgetTargetList.length; i++) 
								{
									html += '<option value="' + data.widgetTargetList[i].index + '" categoryValue="' + data.widgetTargetList[i].category + '">' + data.widgetTargetList[i].name + '</option>';							
								}										
								$('.pop_type_wrap #target_adc_Modify').empty();
								$('.pop_type_wrap #target_vserver_Modify').empty();
								$('.pop_type_wrap #target_flbGroup_Modify').empty();
								$('.pop_type_wrap #target_extend_group_Modify').empty();	
								$('.pop_type_wrap #target_extend_group_Modify').html(html);	
							}				
						}				
					});
				}			
			}
			else
			{
				// 상관없는 Target Disable 하는 기능 (targetArea 사용)
				// 전체 선택만 가능
				var widgetTargerArea = selectedWidgetTargetArea;				
				if (widgetTargerArea == 1)
				{
					$('.pop_type_wrap .AllSelectTr_Modify').css('display', '');
					$('.pop_type_wrap .GroupSelectTr_Modify').css('display', 'none');
					$('.pop_type_wrap .AdcSelectTr_Modify').css('display', '');
					//$('.pop_type_wrap #target_group_Modify').attr("disabled", "disabled");
					$('.pop_type_wrap #target_adc_Modify').attr("disabled", "disabled");
					$('.pop_type_wrap .VsSelectTr_Modify').css('display', '');
					$('.pop_type_wrap #target_vserver_Modify').attr("disabled", "disabled");
					$('.pop_type_wrap .FlbGroupSelectTr_Modify').css('display', 'none');
					$('.pop_type_wrap .ExtendGroupSelectTr_Modify').css('display', 'none');
					$('.pop_type_wrap .list_container_Modify').css('display', 'none');
					widgetTargetInfoClear();
				}
				// 전체 선택 및 그룹 선택 가능
				else if (widgetTargerArea == 2)
				{
					$('.pop_type_wrap .AllSelectTr_Modify').css('display', 'none');
					$('.pop_type_wrap .GroupSelectTr_Modify').css('display', '');
					$('.pop_type_wrap .AdcSelectTr_Modify').css('display', '');
					$('.pop_type_wrap #target_group_Modify').removeAttr("disabled");							
					$('.pop_type_wrap #target_adc_Modify').attr("disabled", "disabled");
					$('.pop_type_wrap .VsSelectTr_Modify').css('display', '');
					$('.pop_type_wrap #target_vserver_Modify').attr("disabled", "disabled");
					$('.pop_type_wrap .FlbGroupSelectTr_Modify').css('display', 'none');
					$('.pop_type_wrap .ExtendGroupSelectTr_Modify').css('display', 'none');
					$('.pop_type_wrap .list_container_Modify').css('display', 'none');
					widgetTargetInfoClear();
					widgetTargetAllOff_Modify();
				}
				// ADC 까지 필수 선택
				else if (widgetTargerArea == 4 || widgetTargerArea == 12)
				{
					$('.pop_type_wrap .AllSelectTr_Modify').css('display', 'none');
					$('.pop_type_wrap .GroupSelectTr_Modify').css('display', '');
					$('.pop_type_wrap .AdcSelectTr_Modify').css('display', '');
					$('.pop_type_wrap #target_group_Modify').removeAttr("disabled");
					$('.pop_type_wrap #target_adc_Modify').removeAttr("disabled");
					$('.pop_type_wrap .VsSelectTr_Modify').css('display', '');
					$('.pop_type_wrap #target_vserver_Modify').attr("disabled", "disabled");
					$('.pop_type_wrap .FlbGroupSelectTr_Modify').css('display', 'none');
					$('.pop_type_wrap .ExtendGroupSelectTr_Modify').css('display', 'none');
					$('.pop_type_wrap .list_container_Modify').css('display', 'none');
					widgetTargetInfoClear();
					widgetTargetAllOff_Modify();
				}
				// 그룹, ADC 까지 선택 가능
				else if (widgetTargerArea == 6)
				{
					$('.pop_type_wrap .AllSelectTr_Modify').css('display', 'none');
					$('.pop_type_wrap .GroupSelectTr_Modify').css('display', '');
					$('.pop_type_wrap .AdcSelectTr_Modify').css('display', '');
					$('.pop_type_wrap #target_group_Modify').removeAttr("disabled");
					$('.pop_type_wrap #target_adc_Modify').removeAttr("disabled");
					$('.pop_type_wrap .VsSelectTr_Modify').css('display', '');
					$('.pop_type_wrap #target_vserver_Modify').attr("disabled", "disabled");
					$('.pop_type_wrap .FlbGroupSelectTr_Modify').css('display', 'none');
					$('.pop_type_wrap .ExtendGroupSelectTr_Modify').css('display', 'none');
					$('.pop_type_wrap .list_container_Modify').css('display', 'none');
					widgetTargetInfoClear();
					widgetTargetAllOn_Modify();
				}
				// 전체, 그룹, ADC 까지 선택 가능
				else if (widgetTargerArea == 7)
				{
					$('.pop_type_wrap .AllSelectTr_Modify').css('display', 'none');
					$('.pop_type_wrap .GroupSelectTr_Modify').css('display', '');
					$('.pop_type_wrap .AdcSelectTr_Modify').css('display', '');
					$('.pop_type_wrap #target_group_Modify').removeAttr("disabled");
					$('.pop_type_wrap #target_adc_Modify').removeAttr("disabled");
					$('.pop_type_wrap .VsSelectTr_Modify').css('display', '');
					$('.pop_type_wrap #target_vserver_Modify').attr("disabled", "disabled");
					$('.pop_type_wrap .FlbGroupSelectTr_Modify').css('display', 'none');
					$('.pop_type_wrap .ExtendGroupSelectTr_Modify').css('display', 'none');
					$('.pop_type_wrap .list_container_Modify').css('display', 'none');
					widgetTargetInfoClear();
					widgetTargetAllOn_Modify();
				}
				// VS/Service 까지 필수 선택
				else if (widgetTargerArea == 8 || widgetTargerArea == 13)
				{
					$('.pop_type_wrap .AllSelectTr_Modify').css('display', 'none');
					$('.pop_type_wrap .GroupSelectTr_Modify').css('display', '');
					$('.pop_type_wrap .AdcSelectTr_Modify').css('display', '');
					$('.pop_type_wrap #target_group_Modify').removeAttr("disabled");
					$('.pop_type_wrap #target_adc_Modify').removeAttr("disabled");
					$('.pop_type_wrap .VsSelectTr_Modify').css('display', '');
					$('.pop_type_wrap #target_vserver_Modify').removeAttr("disabled");
					$('.pop_type_wrap .FlbGroupSelectTr_Modify').css('display', 'none');
					$('.pop_type_wrap .ExtendGroupSelectTr_Modify').css('display', 'none');
					$('.pop_type_wrap .list_container_Modify').css('display', 'none');
					widgetTargetInfoClear();
					widgetTargetAllOff_Modify();
				}						
				// ADC 필수선택, VS까지 선택 가능
				else if (widgetTargerArea == 9)
				{
					$('.pop_type_wrap .AllSelectTr_Modify').css('display', 'none');
					$('.pop_type_wrap .GroupSelectTr_Modify').css('display', '');
					$('.pop_type_wrap .AdcSelectTr_Modify').css('display', '');
					$('.pop_type_wrap #target_group_Modify').removeAttr("disabled");
					$('.pop_type_wrap #target_adc_Modify').removeAttr("disabled");
					$('.pop_type_wrap .VsSelectTr_Modify').css('display', '');
					$('.pop_type_wrap #target_vserver_Modify').removeAttr("disabled");
					$('.pop_type_wrap .FlbGroupSelectTr_Modify').css('display', 'none');
					$('.pop_type_wrap .ExtendGroupSelectTr_Modify').css('display', 'none');
					$('.pop_type_wrap .list_container_Modify').css('display', 'none');
					widgetTargetInfoClear();
					widgetTargetAllOff_Modify();
				}
				// FLB 그룹까지 필수 선택
				else if (widgetTargerArea == 10) // 10 FLB Group
				{
					$('.pop_type_wrap .AllSelectTr_Modify').css('display', 'none');
					$('.pop_type_wrap .GroupSelectTr_Modify').css('display', '');
					$('.pop_type_wrap .AdcSelectTr_Modify').css('display', '');							
					$('.pop_type_wrap #target_group_Modify').removeAttr("disabled");
					$('.pop_type_wrap #target_adc_Modify').removeAttr("disabled");
					$('.pop_type_wrap .VsSelectTr_Modify').css('display', 'none');							
					$('.pop_type_wrap .FlbGroupSelectTr_Modify').css('display', '');
					$('.pop_type_wrap #target_flbGroup_Modify').removeAttr("disabled");
					$('.pop_type_wrap .ExtendGroupSelectTr_Modify').css('display', 'none');
					$('.pop_type_wrap .list_container_Modify').css('display', 'none');
					widgetTargetInfoClear();
					widgetTargetAllOff_Modify();
				}
				else if(widgetTargerArea == 11) // 11 Alteon ADC Only
				{
					$('.pop_type_wrap .AllSelectTr_Modify').css('display', 'none');
					$('.pop_type_wrap .GroupSelectTr_Modify').css('display', 'none');
					$('.pop_type_wrap .AdcSelectTr_Modify').css('display', '');
					$('.pop_type_wrap #target_group_Modify').removeAttr("disabled");
					$('.pop_type_wrap #target_adc_Modify').removeAttr("disabled");
					$('.pop_type_wrap .VsSelectTr_Modify').css('display', 'none');
					$('.pop_type_wrap .FlbGroupSelectTr_Modify').css('display', 'none');	
					$('.pop_type_wrap .ExtendGroupSelectTr_Modify').css('display', 'none');
					$('.pop_type_wrap .list_container_Modify').css('display', 'none');
					
					OBajaxManager.runJson({
						url	: '/dashboard/loadWidgetTargetInfoAlteon.action',
						data :
						{
							"widgetTarget.category" : 1,
							"widgetTarget.index" : 2
						},
						successFn : function(data) 
						{
							var $adcSelectTarget = $('.pop_type_wrap .adcContent_Modify');
							var widgetTargetArea = 11;//$('.pop_type_wrap #widget_select').children('option').filter(':selected').attr('id');
							$adcSelectTarget.empty();
							if ( widgetTargetArea == 1 || widgetTargetArea == 2 || widgetTargetArea == 3)
							{
								$('<select name="" id="target_adc_Modify" class="widget_popup_Select" disabled="disabled"></select>').appendTo($adcSelectTarget);	
							}
							else
							{							
								$('<select name="" id="target_adc_Modify" class="widget_popup_Select"></select>').appendTo($adcSelectTarget);
							}								
							widgetModifyTargetChangeEvent(selectedWidgetTargetArea);

							var html = '';
							var target = data.widgetTargetList;
							html += '<option value="0">ADC Select&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>';
							for (var i=0; i < target.length; i++) 
							{
								html += '<option value="'+target[i].index+'" vendor="'+target[i].vendor+'">'+target[i].name+'</option>';
							}							
							$('.pop_type_wrap #target_adc_Modify').empty();
							$('.pop_type_wrap #target_vserver_Modify').empty();
							$('.pop_type_wrap #target_flbGroup_Modify').empty();
							$('.pop_type_wrap #target_extend_group_Modify').empty();							
							$('.pop_type_wrap #target_adc_Modify').html(html);
						}				
					});
					
//					widgetTargetInfoClear();
//					widgetTargetAllOn_Modify();
				}
				else if(widgetTargerArea == 14)							
				{
					$('.pop_type_wrap .AllSelectTr_Modify').css('display', 'none');
					$('.pop_type_wrap .GroupSelectTr_Modify').css('display', '');							
					$('.pop_type_wrap .AdcSelectTr_Modify').css('display', '');
					$('.pop_type_wrap .VsSelectTr_Modify').css('display', '');
					$('.pop_type_wrap #target_group_Modify').removeAttr("disabled");
					$('.pop_type_wrap #target_adc_Modify').removeAttr("disabled");
					$('.pop_type_wrap #target_vserver_Modify').removeAttr("disabled");
					$('.pop_type_wrap .FlbGroupSelectTr_Modify').css('display', 'none');							
					$('.pop_type_wrap .ExtendGroupSelectTr_Modify').css('display', 'none');					
					$('.pop_type_wrap .list_container_Modify').css('display', '');
					widgetTargetInfoClear();
					widgetTargetAllOff_Modify();					
				}				
			}
			
			// Target Select Box 및 요약 부분 초기화
			function widgetTargetInfoClear()
			{
				$viewGroup.html("-");
				$viewGroup_index.html(0);
				$viewADC.html("-");			
				$viewADC_index.html(0);		
				$viewVserver.html("-");		
				$viewVserver_index.html(0);
				$('.pop_type_wrap #target_adc_Modify').empty().html('<option value="0">ADC Select&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>');					
				$('.pop_type_wrap #target_vserver_Modify').empty().html('<option value="0">VS Select&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>');
				$('.pop_type_wrap #target_flbGroup_Modify').empty().html('<option value="0">FLB Group Select&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>');					
				$('.pop_type_wrap #target_extend_group_Modify').empty().html('<option value="0">FLB Group Select&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>');					
				$('.pop_type_wrap .list_container_Modify .list').empty();
				$('.pop_type_wrap #target_group_Modify').val(0).attr("selected", "selected");
				$('.pop_type_wrap #target_adc_Modify').val(0).attr("selected", "selected");
				$('.pop_type_wrap #target_vserver_Modify').val(0).attr("selected", "selected");
				$('.pop_type_wrap #target_flbGroup_Modify').val(0).attr("selected", "selected");
				$('.pop_type_wrap #target_extend_group_Modify').val(0).attr("selected", "selected");
			}
			// Group Select Box 에 "전체그룹" 활성화 ( 타겟 전체선택이 가능한 위젯 )
			function widgetTargetAllOn_Modify()
			{				
				$('.pop_type_wrap #target_group_Modify option[value="-1"]').removeAttr("disabled");
			}
			// Group Select Box 에 "전체그룹" 비활성화 ( 타겟 전체선택이 불필요한 위젯 )
			function widgetTargetAllOff_Modify()
			{							
				$('.pop_type_wrap #target_group_Modify option[value="-1"]').attr("disabled", "disabled");
			}
		}	
	},
	
	// 구조상 Select Box Change Event 는 targetChangeEvent로 따로 분리합니다. (add widget Popup Event More)
	widgetModifyTargetChangeEvent : function(selectedWidgetTargetArea)
	{
		with(this)
		{
			var $viewcategory_index = $('.widget_summery_table_Modify .selected_category_Modify').filter(':last');			// 선택한 Category			
			var $viewGroup = $('.widget_summery_table_Modify .selected_group_Modify').filter(':last');					// Group 요약 Name
			var $viewGroup_index = $('.widget_summery_table_Modify .selected_group_index_Modify').filter(':last');		// Group 요약 Index
			var $viewADC = $('.widget_summery_table_Modify .selected_adc_Modify').filter(':last');						// ADC 요약 Name
			var $viewADC_index = $('.widget_summery_table_Modify .selected_adc_index_Modify').filter(':last');			// ADC 요약 Index
			var $viewADC_vendor = $('.widget_summery_table_Modify .selected_adc_vendor_Modify').filter(':last');			// ADC 요약 vendor
			var $viewVserver = $('.widget_summery_table_Modify .selected_vserver_Modify').filter(':last');				// Vserver 요약 Name
			var $viewVserver_index = $('.widget_summery_table_Modify .selected_vserver_index_Modify').filter(':last');	// Vserver 요약 Index					
			var $viewSelect_list = $('.widget_summery_table_Modify .selected_select_list_Modify').filter(':last');
			var $viewSelect_list_count = $('.widget_summery_table_Modify .selected_select_list_count_Modify').filter(':last');
			
			//그룹 Select Box Change 기능
			$('.pop_type_wrap #target_group_Modify').change(function(e)
			{
				e.preventDefault();
				// 요약에 선택된 그룹을 Write 하는 기능
				var $selectedGroupName = $(this).children('option').filter(':selected').text();
				var $selectedGroupVal = $(this).children('option').filter(':selected').val();
								
				if ($selectedGroupVal == 0)
				{
					$viewGroup.html("-");
					$viewGroup_index.html(0);
				}
				else
				{
					$viewGroup.html($selectedGroupName);
					$viewGroup_index.html($selectedGroupVal);
				}				
				$viewADC.html("-");
				$viewADC_index.html(0);
				$viewADC_vendor.html(0);
				$viewVserver.html("-");
				$viewVserver_index.html(0);			
				
				OBajaxManager.runJson({
					url	: '/dashboard/loadWidgetTargetInfo.action',
					data :
					{
						"widgetTarget.category" : 1,
						"widgetTarget.index" : $(this).val()
					},
					successFn : function(data) 
					{
						var $adcSelectTarget = $('.pop_type_wrap .adcContent_Modify');
						var widgetTargetArea = selectedWidgetTargetArea;
						$adcSelectTarget.empty();
						if ( widgetTargetArea == 1 || widgetTargetArea == 2 || widgetTargetArea == 3)
						{
							$('<select name="" id="target_adc_Modify" class="widget_popup_Select" disabled="disabled"></select>').appendTo($adcSelectTarget);	
						}
						else
						{							
							$('<select name="" id="target_adc_Modify" class="widget_popup_Select"></select>').appendTo($adcSelectTarget);
						}								
						widgetModifyTargetChangeEvent(selectedWidgetTargetArea);

						var html = '';
						var target = data.widgetTargetList;
						html += '<option value="0">' + "ADC Select&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + '</option>';
						for (var i=0; i < target.length; i++) 
						{
							html += '<option value="'+target[i].index+'" vendor="'+target[i].vendor+'">'+target[i].name+'</option>';
						}							
						$('.pop_type_wrap #target_adc_Modify').empty();
						$('.pop_type_wrap #target_vserver_Modify').empty();						
						$('.pop_type_wrap #target_adc_Modify').html(html);						
						$('.pop_type_wrap #target_vserver_Modify').html('<option value="0">' + "VS Select&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + '</option>');												
					}				
				});
			});
			
			// ADC Select Box Change 기능
			$('.pop_type_wrap #target_adc_Modify').change(function(e)
			{		
				e.preventDefault();
				// 요약에 선택된 ADC Write 하는 기능
				var $selectedAdcName = $(this).children('option').filter(':selected').text();
				var $selectedAdcVal = $(this).children('option').filter(':selected').val();
				var $selectedAdcVendor = $(this).children('option').filter(':selected').attr('vendor');
				
				if ($selectedAdcVal == 0)
				{
					$viewADC.html("-");
					$viewADC_index.html(0);
					$viewADC_vendor.html(0);
				}
				else
				{
					$viewADC.html($selectedAdcName);
					$viewADC_index.html($selectedAdcVal);
					$viewADC_vendor.html($selectedAdcVendor);
				}				
				$viewVserver.html("-");
				$viewVserver_index.html(0);				
				
				var widgetTargerArea = selectedWidgetTargetArea;
				
				if ($(this).val() == 0)
				{
					var html = '<option value="0">' + "VS Select" + '</option>';
					$('.pop_type_wrap #target_vserver_Modify').html(html);
				}
				else if (widgetTargerArea == 10)
				{				
					// 선택된 ADC의 FLB Group List get Json
					OBajaxManager.runJson({
						url	: '/dashboard/loadWidgetTargetFlbGroupInfo.action',
						data :
						{	
							"widgetTarget.category" : 2,
							"widgetTarget.index" : $(this).val()
						},
						successFn : function(data) 
						{
							var $flbGroupSelectTarget = $('.pop_type_wrap .flbGroupContent_Modify');
							var widgetTargetArea = selectedWidgetTargetArea;
							$flbGroupSelectTarget.empty();
							if (widgetTargetArea == 1 || widgetTargetArea == 2 || widgetTargetArea == 3 || widgetTargetArea == 4 || widgetTargetArea == 7)
							{
								$('<select name="" id="target_flbGroup_Modify" class="widget_popup_Select" disabled="disabled"></select>').appendTo($flbGroupSelectTarget);		
							}
							else
							{
								$('<select name="" id="target_flbGroup_Modify" class="widget_popup_Select"></select>').appendTo($flbGroupSelectTarget);		
							}								
							widgetModifyTargetChangeEvent(selectedWidgetTargetArea);							
							var html = '';
							var target = data.widgetTargetList;						
							html += '<option value="0">' + "FLB Group Select&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + '</option>';
							for (var i=0; i < target.length; i++) 
							{
								html += '<option value="' + target[i].strIndex + '">' + target[i].strIndex + "(" +target[i].name + ')</option>';							
							}							
													
							$('.pop_type_wrap #target_flbGroup_Modify').empty();
							$('.pop_type_wrap #target_flbGroup_Modify').html(html);
						}				
					});
				}
				else if (widgetTargerArea == 11) // 11 Alteon ADC Only
				{					
					OBajaxManager.runJson({
						url	: '/dashboard/loadWidgetTargetInfoAlteon.action',
						data :
						{
							"widgetTarget.category" : 1,
							"widgetTarget.index" : 2
						},
						successFn : function(data) 
						{
							var $adcSelectTarget = $('.pop_type_wrap .adcContent_Modify');
							var widgetTargetArea = 11;//$('.pop_type_wrap #widget_select').children('option').filter(':selected').attr('id');
							$adcSelectTarget.empty();
							if ( widgetTargetArea == 1 || widgetTargetArea == 2 || widgetTargetArea == 3)
							{
								$('<select name="" id="target_adc_Modify" class="widget_popup_Select" disabled="disabled"></select>').appendTo($adcSelectTarget);	
							}
							else
							{							
								$('<select name="" id="target_adc_Modify" class="widget_popup_Select"></select>').appendTo($adcSelectTarget);
							}								
							widgetModifyTargetChangeEvent(selectedWidgetTargetArea);

							var html = '';
							var target = data.widgetTargetList;
							html += '<option value="0">ADC Select&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>';
							for (var i=0; i < target.length; i++) 
							{
								html += '<option value="'+target[i].index+'" vendor="'+target[i].vendor+'">'+target[i].name+'</option>';
							}							
							$('.pop_type_wrap #target_adc_Modify').empty();
							$('.pop_type_wrap #target_vserver_Modify').empty();
							$('.pop_type_wrap #target_flbGroup_Modify').empty();
							$('.pop_type_wrap #target_extend_group_Modify').empty();							
							$('.pop_type_wrap #target_adc_Modify').html(html);
						}				
					});
					
//					widgetTargetInfoClear();
//					widgetTargetAllOn_Modify();
				}
				else
				{
					// 선택된 ADC의 Vserver List Get Json
					OBajaxManager.runJson({
						url	: '/dashboard/loadWidgetTargetInfo.action',
						data :
						{	
							"widgetTarget.category" : 2,
							"widgetTarget.index" : $(this).val()
						},
						successFn : function(data) 
						{
							var $vsSelectTarget = $('.pop_type_wrap .vsContent_Modify');
							var widgetTargetArea = selectedWidgetTargetArea;
							$vsSelectTarget.empty();
							if (widgetTargetArea == 1 || widgetTargetArea == 2 || widgetTargetArea == 3 || widgetTargetArea == 4 || widgetTargetArea == 6 || widgetTargetArea == 7 || widgetTargetArea == 12)
							{
								$('<select name="" id="target_vserver_Modify" class="widget_popup_Select" disabled="disabled"></select>').appendTo($vsSelectTarget);		
							}
							else
							{
								$('<select name="" id="target_vserver_Modify" class="widget_popup_Select"></select>').appendTo($vsSelectTarget);		
							}
								
							widgetModifyTargetChangeEvent(selectedWidgetTargetArea);
							
							var html = '';
							var target = data.widgetTargetList;						
							html += '<option value="0">' + "VS Select&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + '</option>';
							for (var i=0; i < target.length; i++) 
							{
								html += '<option value="' + target[i].strIndex + '">' + target[i].name + ":" +target[i].extraInfo + '</option>';							
							}							
													
							$('.pop_type_wrap #target_vserver_Modify').empty();
							$('.pop_type_wrap #target_vserver_Modify').html(html);											
						}				
					});
				}
			});
			
			//  Vserver Select Box Change 기능
			$('.pop_type_wrap #target_vserver_Modify').change(function(e)
			{
				e.preventDefault();
				// 요약에 선택된 Vserver write
				var widgetTargerArea = selectedWidgetTargetArea;
				var $selectedVserverName = $(this).children('option').filter(':selected').text();
				var $selectedVserverVal = $(this).children('option').filter(':selected').val();				
				if($selectedVserverVal == 0)
				{
					$viewVserver.html("-");
					$viewVserver_index.html(0);
				}
				else
				{
					$viewVserver.html($selectedVserverName);
					$viewVserver_index.html($selectedVserverVal);
				}
				$viewSelect_list.html();
				$viewSelect_list_count.html(0);
				if (widgetTargerArea == 14) // VS/SERVICE 를 선택했을때 Member 정보 notice 가 필요할 경우 ( 14. 맴버 상태알리미)
				{
					var categoryParam = 5;
					OBajaxManager.runJson({
						url	: '/dashboard/loadWidgetTargetInfo.action',
						data :
						{	
							"widgetTarget.category" : categoryParam,
							"widgetTarget.strIndex" : $(this).val()
						},
						successFn : function(data) 
						{							
							var $vsSelectTarget = $('.pop_type_wrap .list_container_Modify .list');												
							targetChangeEvent();							
							var html = '';
							var target = data.widgetTargetList;
							for (var i=0; i < target.length; i++) 
							{
								html += '<div><input type="checkbox" class="list_checkbox" value="'+ target[i].strIndex+'"/>' + target[i].desciption + '</div>';							
							}													
							$vsSelectTarget.empty();
							$vsSelectTarget.html(html);
							$('.pop_type_wrap .list_checkbox').change(function(e)
							{								
								e.preventDefault();	
								var checkedItem = $('.cloneDiv .list_checkbox:checked').map(function () {
									  return this.value;
									}).get().toString();
								var chendItemLength = $('.cloneDiv .list_checkbox:checked').length;
								$viewSelect_list.html(checkedItem);
								$viewSelect_list_count.html(chendItemLength);
							});
						}				
					});
				}
			});
			
		//  FLB Group Select Box Change 기능
			$('.pop_type_wrap #target_flbGroup_Modify').change(function(e)
			{
				e.preventDefault();
				// 요약에 선택된 Vserver write
				var $selectedVserverName = $(this).children('option').filter(':selected').text();
				var $selectedVserverVal = $(this).children('option').filter(':selected').val();				
				if($selectedVserverVal == 0)
				{
					$viewVserver.html("-");
					$viewVserver_index.html(0);
				}
				else
				{
					$viewVserver.html($selectedVserverName);
					$viewVserver_index.html($selectedVserverVal);
				}			
			});
		//  Extend Group Box Change 기능
			$('.pop_type_wrap #target_extend_group_Modify').change(function(e)
			{
				e.preventDefault();
				var $selectedExtendGroupName = $(this).children('option').filter(':selected').text();
				var $selectedExtendGroupVal = $(this).children('option').filter(':selected').val();				
				var $selectedExtendCategory = $(this).children('option').filter(':selected').attr('categoryvalue');
				
				if ($selectedExtendGroupVal == 0)
				{
					$viewcategory_index.html(0);
					$viewGroup.html("-");
					$viewGroup_index.html(0);
				}
				else
				{
					$viewcategory_index.html($selectedExtendCategory);
					$viewGroup.html($selectedExtendGroupName);
					$viewGroup_index.html($selectedExtendGroupVal);
				}				
				$viewADC.html("-");
				$viewADC_index.html(0);
				$viewADC_vendor.html(0);
				$viewVserver.html("-");
				$viewVserver_index.html(0);
				
				var widgetTargerArea = selectedWidgetTargetArea;
				var categoryParam = undefined;				
				if ($(this).val() == 0)
				{
					$('.pop_type_wrap .list_container .list').empty();
					$viewSelect_list.html();
					$viewSelect_list_count.html(0);
					return;
				}
				else if (widgetTargerArea == 8 || widgetTargerArea == 9)
				{
					categoryParam = 8;
				}
				else if (widgetTargerArea == 12)
				{
					categoryParam = 1;
				}
				else if (widgetTargerArea == 13)
				{
					categoryParam = 8;
				}
				else if (widgetTargerArea == 14)
				{
					categoryParam = 9;
				}
				else
				{
					return;
				}
				// 선택된 서비스 그룹의 서비스 리스트
				OBajaxManager.runJson({
					url	: '/dashboard/loadWidgetTargetInfo.action',
					data :
					{	
						"widgetTarget.category" : categoryParam,
						"widgetTarget.index" : $(this).val()
					},
					successFn : function(data) 
					{
						if (widgetTargerArea == 8 || widgetTargerArea == 9) // 응답시간, ConcurrentSession, Throughput
						{
							var $vsSelectTarget = $('.pop_type_wrap .list_container_Modify .list');												
							targetChangeEvent();							
							var html = '';
							var target = data.widgetTargetList;
							for (var i=0; i < target.length; i++) 
							{
								html += '<div id="' + target[i].strIndex + '">' + target[i].desciption + '</div>';							
							}	
						}
						else if(widgetTargerArea == 12) //  ADC 상태 알리미 면서 그룹일 경우
						{
							var $vsSelectTarget = $('.pop_type_wrap .list_container_Modify .list');												
							targetChangeEvent();							
							var html = '';
							var target = data.widgetTargetList;
							for (var i=0; i < target.length; i++) 
							{
								html += '<div id="' + target[i].index + '">' + target[i].name + '</div>';							
							}	
						}
						else if (widgetTargerArea == 13) // 서비스 상태 알리미면서, 그룹일 경우
						{
							var $vsSelectTarget = $('.pop_type_wrap .list_container_Modify .list');												
							targetChangeEvent();							
							var html = '';
							var target = data.widgetTargetList;
							for (var i=0; i < target.length; i++) 
							{
								html += '<div id="' + target[i].strIndex + '">' + target[i].desciption + '</div>';							
							}	
						}
						else if(widgetTargerArea == 14) // 맴버 상태 알리미면서, 그룹일 경우
						{
							var $vsSelectTarget = $('.pop_type_wrap .list_container_Modify .list');												
							targetChangeEvent();							
							var html = '';
							var target = data.widgetTargetList;
							for (var i=0; i < target.length; i++) 
							{
								html += '<div><input type="checkbox" class="list_checkbox" value="'+ target[i].strIndex+'"/>' + target[i].desciption + '</div>';							
							}
						}
						else
						{
						}
												
						$vsSelectTarget.empty();
						$vsSelectTarget.html(html);						
						$('.pop_type_wrap .list_checkbox').change(function(e)
						{								
							e.preventDefault();	
							var checkedItem = $('.cloneDiv .list_checkbox:checked').map(function () {
								  return this.value;
								}).get().toString();
							var chendItemLength = $('.cloneDiv .list_checkbox:checked').length;
							
							$viewSelect_list.html(checkedItem);
							$viewSelect_list_count.html(chendItemLength);
						});
					}				
				});
				
			});
		}
	},
	
	clickEvent : function()
	{
		with(this)
		{
			$('.refresh-btn').click(function(event)
			{							
				widgetLoadCount = 0;				
				$( "#col1" ).sortable("disable");
				refreshDashboardWidgetTarget(G_refershChartObj);
				updateRefreshDate();
			});		
			
			$('.autoRefreshChk').click(function(event)
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
			
			$('.lastDashboard1').click(function(event)
			{
				event.preventDefault();				
				with(this)
				{
					if(loadPassKey == false)
					{
						if(!refreshDashboardDialog())
						{
							return false;
						}
					}
					var SelectedLastDashboardIndexKey = $(this).attr('id');
					dashboardIndex = SelectedLastDashboardIndexKey;
					loadDashboardInfo(SelectedLastDashboardIndexKey);
				}							
			});
			
			$('.lastDashboard2').click(function(event)
			{
				event.preventDefault();				
				with(this)
				{
					if(loadPassKey == false)
					{
						if(!refreshDashboardDialog())
						{
							return false;
						}
					}
					var SelectedLastDashboardIndexKey = $(this).attr('id');
					dashboardIndex = SelectedLastDashboardIndexKey;
					loadDashboardInfo(SelectedLastDashboardIndexKey);
				}								
			});
			
			$('.lastDashboard3').click(function(event)
			{
				event.preventDefault();				
				with(this)
				{
					if(loadPassKey == false)
					{
						if(!refreshDashboardDialog())
						{
							return false;
						}
					}
					var SelectedLastDashboardIndexKey = $(this).attr('id');
					dashboardIndex = SelectedLastDashboardIndexKey;
					loadDashboardInfo(SelectedLastDashboardIndexKey);
				}
			});
			$('#dashboardNewSave').click(function(event)
			{
				event.preventDefault();
				var $col1 = $('#col1').sortable('toArray');				
				if ($col1.length == 0)
				{
					$.obAlertNotice(VAR_DASH_SAVAWNEXI);
					return;
				}
				offAutoRefreshForEvent();
				loadNewSavePopup();
			});
			$('#addWidget').click(function(event)
			{
				event.preventDefault();
				offAutoRefreshForEvent();
				loadWidgetPopup();
			});
			
			$('#dashboardSave').click(function(event)
			{
				event.preventDefault();
				with(this)
				{
					var $col1 = $('#col1').sortable('toArray');									
					var dashboardName_Selected = $('.contents_area #DashboardList').children('option').filter(':selected').text();
					var dashboardIndex_Selected = $('.contents_area #DashboardList').children('option').filter(':selected').val();										
					if ($col1.length == 0)
					{
						$.obAlertNotice(VAR_DASH_SAVAWNEXI);
						return;
					}
					
					if (0 == dashboardIndex_Selected || "" == dashboardIndex_Selected )
					{
						offAutoRefreshForEvent();
						loadNewSavePopup();					
					}
					else if (1 == dashboardIndex_Selected || 2 == dashboardIndex_Selected || 3 == dashboardIndex_Selected)
					{
						$.obAlertNotice(VAR_DASH_DEFBNSAVA);
						offAutoRefreshForEvent();
						loadNewSavePopup();
					}
					else
					{
						saveDashboardInfo(dashboardIndex_Selected, dashboardName_Selected);						
					}					
					widgetSaveIndex = undefined;
				}				
			});			
			
			$('#DashboardList').change(function(event)
			{
				event.preventDefault();
				with(this)
				{
					if(loadPassKey == false)
					{
						if(!refreshDashboardDialog())
						{
							selectboxChange(dashboardIndex);
							return false;							
						}
					}
					
					dashboardIndex = $(this).val();
//					dashboardName = $(this).children('option').filter('value=' + dashboardIndex).text(); // loadDashboardContent에서 처리됨
					if (dashboardIndex == 0)
					{
						G_widgetList = [];
						G_refershChartObj = {};
						G_widgetCount = 0;
						widgetOrder = 0;
						loadDashBoardHeader();		// 새로만들기				
					}
					else
					{
						G_refershChartObj = {};
						loadDashboardInfo(dashboardIndex);						
					}
				}
			});
			// 수동 refresh 버튼 삭제
			/*$('#refresh').click(function(event)
			{
				event.preventDefault();
				with(this)
				{									
					widgetLoadCount = 0;
					refreshDashboardWidget(G_widgetList, G_refershChartObj);
				}
			});*/
			
			$('#dashboardDelete').click(function(event)
			{
				event.preventDefault();
				if (undefined === dashboardIndex || null == dashboardIndex || "" == dashboardIndex)
				{
					$.obAlertNotice(VAR_DASH_BOADSEL);
					return false;
				}
				else if (1 == dashboardIndex)
				{
//					alert(VAR_DASH_ADCDNAVA);
					$.obAlertNotice(dashboardName + VAR_DASH_ADCDELNAVA);
					return false;
				}
				else if (2 == dashboardIndex)
				{
//					alert(VAR_DASH_ADCDELNAVA);
					$.obAlertNotice(dashboardName + VAR_DASH_ADCDELNAVA);
					return false;
				}
				else if (3 == dashboardIndex)
				{
//					alert(VAR_DASH_FAUDNAVA);
					$.obAlertNotice(dashboardName + VAR_DASH_ADCDELNAVA);
					return false;
				}
				
				var confirmVal = "";
				if (langCode == "ko_KR")
				{
					confirmVal = dashboardName + " " + VAR_DASH_BOADEL + "?";
				}
				else if (langCode == "en_US")
				{
					confirmVal = VAR_DASH_BOADEL + ' "' + dashboardName + '" ' + VAR_DASH_BOADELSEL;
				}
				else
				{
					confirmVal = dashboardName + " " + VAR_DASH_BOADEL + "?";
				}
				var retVal = confirm(confirmVal);
				if( retVal == false )
				{
					return false;
				}
				
				OBajaxManager.runJson({
					url	: '/dashboard/deleteDashboardInfo.action',
					data :
					{
						"dashboardIndex" : dashboardIndex,						
					},
					successFn : function(data) 
					{										
						if (langCode == "ko_KR")
						{
							$.obAlertNotice(dashboardName + " " + VAR_DASH_BOADSUC);
						}
						else if (langCode == "en_US")
						{
							$.obAlertNotice(VAR_DASH_BOADSUC + ' "' + dashboardName + '" ' + VAR_DASH_BOADSUCDEL);
						}
						else
						{
							$.obAlertNotice(dashboardName + " " + VAR_DASH_BOADSUC);
						}
												
						// 삭제 후 Set 되어있던 Dashboard Info 초기화
						dashboardIndex = undefined;
						dashboardName = undefined;
						G_widgetList = [];
						loadDashBoardHeader();
					}				
				});							
			});			
		}
	},
	// TODO Widget Add
	addWidget : function(widgetList)
	{
		with(this)
		{
			maxData = [];
			if (widgetList[0] != null && maxDataFlag == false)
			{
				for ( var i = 0; i < widgetList.length; i++)
				{
					var IndexValue = widgetList[i].index;
					var maxdataObject = 
					{
						value : IndexValue
					};
					maxData.push(maxdataObject);
				}
				maxIndex = Math.max.apply(null,maxData.map(function(o){return o.value;}));
				widgetOrder = maxIndex + 10000;
				maxDataFlag = true;				
			}
			else if (widgetList == null)
			{
				widgetOrder = (widgetOrder +10000);	
			}
			else
			{
				widgetOrder = (widgetOrder +10000);	
			}
			var $widgetType = $('.pop_type_wrap #widget_select').children('option').filter(':selected').val();
			var $widgetName = $('.pop_type_wrap #widget_select').children('option').filter(':selected').text();			
			var $widthMinSize = $('.pop_type_wrap #widget_select').children('option').filter(':selected').attr('widthMinSize');
			var $widthMaxSize = $('.pop_type_wrap #widget_select').children('option').filter(':selected').attr('widthMaxSize');
			var $heightMinSize = $('.pop_type_wrap #widget_select').children('option').filter(':selected').attr('heightMinSize');
			var $heightMaxSize = $('.pop_type_wrap #widget_select').children('option').filter(':selected').attr('heightMaxSize');
			var $targetCategory ='';
			var $targetIndex = '';
			var $targetStrIndex = '';
			var $targetName = '';
			var $moreWidgetInfo = undefined;
			
			var $targetCategoryIndex = $('.pop_type_wrap .selected_category').filter(':last').text();
			var $targetGroupName = $('.pop_type_wrap .selected_group').filter(':last').text();
			var $targetAdcName = $('.pop_type_wrap .selected_adc').filter(':last').text();
			var $targetVserverName = $('.pop_type_wrap .selected_vserver').filter(':last').text().split(":")[0];
			var $targetSelectList = $('.pop_type_wrap .selected_select_list').filter(':last').text();
			
			var $targetGroup = $('.pop_type_wrap .selected_group_index').filter(':last').text();		//index
			var $targetAdc = $('.pop_type_wrap .selected_adc_index').filter(':last').text();			//index
			var $targetVendor = $('.pop_type_wrap .selected_adc_vendor').filter(':last').text();
			var $targetVserver = $('.pop_type_wrap .selected_vserver_index').filter(':last').text();	//index			
			
			var $moreInfoStatus = $('.pop_type_wrap .selected_status').filter(':last').text();
			var $moreInfoStatusVs = $('.pop_type_wrap .selected_statusVs').filter(':last').text();
			var $moreInfoTime = $('.pop_type_wrap .selected_time').filter(':last').text();
			var $moreInfoOrdering = $('.pop_type_wrap .selected_ordering').filter(':last').text();
			var $moreInfoChart= $('.pop_type_wrap .selected_chart').filter(':last').text();

			// 추가 정보 선택 시
			if ($moreInfoStatus == null && $moreInfoTime == null && $moreInfoStatusVs == null && $moreInfoOrdering == null && $moreInfoChart == null)
			{
				$moreWidgetInfo = -1;
			}
			else if ($moreInfoStatus == -1 && $moreInfoTime == -1 && $moreInfoStatusVs == -1 && $moreInfoOrdering == -1 && $moreInfoChart == -1)
			{
				$moreWidgetInfo = -1;
			}
			else if ($moreInfoStatus == "" && $moreInfoTime == "" && $moreInfoStatusVs == "" && $moreInfoOrdering == "" && $moreInfoChart == "")
			{
				$moreWidgetInfo = -1;
			}
			else if ($moreInfoStatus != null && $moreInfoStatus != "" && $moreInfoStatus != -1)
			{
				$moreWidgetInfo = $moreInfoStatus;
			}
			else if ($moreInfoTime != null && $moreInfoTime != "" && $moreInfoTime != -1)
			{
				$moreWidgetInfo = $moreInfoTime;
			}
			else if ($moreInfoOrdering != null && $moreInfoOrdering != "" && $moreInfoOrdering != -1)
			{
				$moreWidgetInfo = $moreInfoOrdering;
			}
			else if ($moreInfoStatusVs != null && $moreInfoStatusVs != "" && $moreInfoStatusVs != -1)
			{
				$moreWidgetInfo = $moreInfoStatusVs;
			}	
			else if ($moreInfoChart != null && $moreInfoChart != "" && $moreInfoChart != -1)
			{
				$moreWidgetInfo = $moreInfoChart;
			}	
			else
			{
				$moreWidgetInfo = -1;
			}
			// 아무 대상도 선택되지 않았을때
			if ($targetGroup == 0 && $targetAdc == 0 && $targetVserver == 0)
			{
				$targetCategory = 0;
				$targetIndex = 0;
				$targetStrIndex = "";
				$targetName = VAR_DASH_ALLGROUP;				
			}
			// 그룹 전체가 선택되었을때
			else if ($targetGroup == -1)
			{
				$targetCategory = 0;
				$targetIndex = 0;
				$targetStrIndex = "";
				$targetName = VAR_DASH_ALLGROUP;				
			}
			// 그룹까지 선택되었을때
			else if ($targetAdc == 0 && $targetVserver == 0)
			{
				if ($targetCategoryIndex > 1)
					$targetCategory = $targetCategoryIndex;
				else
					$targetCategory = 1;

				$targetIndex = $targetGroup;
				$targetStrIndex = "";				
				$targetName = $targetGroupName;	

				
			}
			// ADC까지 선택되었을때
			else if ($targetVserver == 0)
			{
				$targetCategory = 2;
				$targetIndex = $targetAdc;
				$targetStrIndex = "";
				$targetName = $targetAdcName;				
			}
			// VServer / VService 까지 선택되었을때
			else
			{
				// 알테온 일때는 Vservice 이기때문에 Category 4
				if ($targetVendor == 2)
				{
					$targetCategory = 4;
				}
				else	// 나머지 ADC 에서는 Vserver 이기때문에 Category 3 
				{
					$targetCategory = 3;
				}				
				$targetIndex = 0;
				$targetStrIndex = $targetVserver;
				$targetName = $targetVserverName;				
			}		
			
			// Widget runHTML param Data : widgetData
			var widgetData = {
					"widgetInfo.index" : widgetOrder,
					"widgetInfo.type" : $widgetType,
					"widgetInfo.name" : $widgetName,
					"widgetTarget.category" : $targetCategory,
					"widgetTarget.strIndex" : $targetStrIndex,
					"widgetTarget.index" : $targetIndex,
					"widgetTarget.name" : $targetName,	
					"widgetTarget.desciption" : $targetSelectList,
					"widgetItem.widthMinSize" : $widthMinSize,
					"widgetItem.widthMaxSize" : $widthMaxSize,
					"widgetItem.heightMinSize" : $heightMinSize,
					"widgetItem.heightMaxSize" : $heightMaxSize,						
					"widgetInfo.moreInfoIndex" : $moreWidgetInfo					
				};
			
			if ($widgetType == 1 || $widgetType == 3 || $widgetType == 5 || $widgetType == 6 || $widgetType == 7
					|| $widgetType == 14 || $widgetType == 15 || $widgetType == 16  || $widgetType == 17 || $widgetType == 18
					|| $widgetType == 24 || $widgetType == 25 || $widgetType == 26)
			{				
				var dashUrl = "";
				if ($widgetType == 1)
				{
					dashUrl = '/dashboard/loadFaultMonitoring.action';
				}
				else if ($widgetType == 3)
				{
					dashUrl = '/dashboard/loadFaultMonitoringList.action';
				}
				else if ($widgetType == 5)
				{
					dashUrl = '/dashboard/loadAdcMonitoringList.action';
				}
				else if ($widgetType == 6)
				{
					dashUrl = '/dashboard/loadAdcMonitoring.action';
				}
				else if ($widgetType == 7)
				{
					dashUrl = '/dashboard/loadVsMonitoring.action';
				}
				else if ($widgetType == 14)
				{
					dashUrl = '/dashboard/loadAdcSummaryList.action';
				}
				else if ($widgetType == 15)
				{
					dashUrl = '/dashboard/loadVsSummaryList.action';
				}
				else if ($widgetType == 16)
				{
					dashUrl = '/dashboard/loadTop10VSInfo.action';
				}
				else if ($widgetType == 17)
				{
					dashUrl = '/dashboard/loadTop5AdcInfo.action';
				}
				else if ($widgetType == 18)
				{
					// 18 번 ADC모니터링 Widget 은 도중에 Model Number 를 Get 하는 json 으로 인해 함수를 분리합니다.
					return AddadcApplianceMonitorWidget(widgetData);									
				}
				else if ($widgetType == 24)
				{
					dashUrl = '/dashboard/loadAdcStatusNotificationInfo.action';
				}
				else if ($widgetType == 25)
				{
					dashUrl = '/dashboard/loadVsStatusNotificationInfo.action';
				}
				else if ($widgetType == 26)
				{
					dashUrl = '/dashboard/loadMemberStatusNotificationInfo.action';
				}
				
				ajaxManager.runHtmlExt({
					url : dashUrl,
					data: widgetData,				
					target: ".contents_area .widget_box",
					successFn : function(params) 
					{
						// Min Size 적용, 기본 Widget Size 405보다 크다면, Widget고유의 Min Size 를 적용한다.
						$('.contents_area .widget_box .portlet').css("width", $widthMinSize+ "px");						
						$('.contents_area .widget_box .portlet').css("height", $heightMinSize+ "px");
						
						var widgetElemnet = '';
						widgetElemnet = $('.contents_area .widget_box .portlet');						
						$(widgetElemnet).appendTo('#col1');						
						$('#col1').sortable('refresh');						
						
						// Widget 을 ADD 할때 Widget 의 Event 및 Option 들을 적용한다. (Widget을 Add할때 필히 적용해야 합니다.)
						addWidgetEvent(widgetOrder, $widthMinSize, $widthMaxSize, $heightMinSize, $heightMaxSize);
						$(".widget_table tbody tr:even").css("background","#f4f4f4");   // Table 위젯 짝수 bg 변경	
						
						// Widget Count 증가 (for 위젯 갯수 Limit)
						G_widgetCount ++;
						// loadPassKey  list, image 기반 위젯 추가 완료 시
						loadPassKey = false;
					},
					errorFn : function(a,b,c)
					{
						exceptionEvent();
					}
				});
			}
						
			else if ($widgetType == 2 || $widgetType == 4 || $widgetType == 10
					|| $widgetType == 11 || $widgetType == 8 || $widgetType == 9
						|| $widgetType == 12 || $widgetType == 13 || $widgetType == 19
						|| $widgetType == 20 || $widgetType == 21 || $widgetType == 22 || $widgetType == 23) 	// $widgetType == 22 : Alteon CPU SP Monitoring
			{
				ajaxManager.runHtmlExt({
					url : '/dashboard/loadAllChartWidget.action',
					data: widgetData,
					target: ".contents_area .widget_box",
					successFn : function(params) 
					{
						// Min Size 적용, 기본 Widget Size 405보다 크다면, Widget고유의 Min Size 를 적용한다.
						$('.contents_area .widget_box .portlet').css("width", $widthMinSize+ "px");
						$('.contents_area .widget_box .portlet').css("height", $heightMinSize+ "px");
						
						// Chart Area를 동적으로 생성한 후 Chart Area를 JS에서 HTML로 삽입한다.
						var chartName = "widgetChartAdd"+widgetAddCount;
						var $chartArea = $('.contents_area .widget_box .portlet .portlet-content-chart').filter(':last');
						var chartElement ='';
//						chartElement += '<div id="' +chartName+ '" style="width: 100%; height: 140px; margin-top: 0px; padding-top: 0px;"></div>';
						
						if($widgetType == 18 )
						{
							chartElement += '<div id="' +chartName+ '" style="width: 100%; height: 140px; margin-top: 0px; padding-top: 8px; padding-bottom: 5px;"></div>';
						}
						else
						{
							chartElement += '<div id="' +chartName+ '" style="width: 100%; height: 224px; margin-top: 0px; padding-top: 8px; padding-bottom: 5px;"></div>';
						}
							
//						if($widgetType == 23)
//						{
//							chartElement += '<div id="' +chartName+ '" style="width: 100%; height: 234px; margin-top: 0px; padding-top: 0px;"></div>';	
//						}
//						else if($widgetType == 8 || $widgetType == 9 || $widgetType == 10 || $widgetType == 11 || $widgetType == 19)
//						{
//							chartElement += '<div id="' +chartName+ '" style="width: 100%; height: 280px; margin-top: 0px; padding-top: 0px;"></div>';	
//						}
//						else
//						{
//							chartElement += '<div id="' +chartName+ '" style="width: 100%; height: 140px; margin-top: 0px; padding-top: 0px;"></div>';	
//						}
						$chartArea.empty();
						$chartArea.html(chartElement);	
						
						if ($widgetType == 2)
						{
							AddFaultMonitoringData(chartName, widgetOrder);
						}
						else if ($widgetType == 4)
						{
							AddAdcChangeHistoryData(chartName, widgetOrder);
						}
						else if ($widgetType == 8)
						{
							AddAdcConnectionHistoryData(chartName, widgetOrder);
						}
						else if ($widgetType == 9)
						{
							AddAdcThroughtputHistoryData(chartName, widgetOrder);
						}
						else if ($widgetType == 10)
						{
							AddCpuHistoryData(chartName, widgetOrder);
						}
						else if ($widgetType == 11)
						{
							AddMemoryHistoryData(chartName, widgetOrder);
						}
						else if ($widgetType == 12)
						{
							AddAllAdcTrafficChartData(chartName, widgetOrder);
						}
						else if ($widgetType == 13)
						{
							AddVsStatusChartData(chartName, widgetOrder);
						}
						else if ($widgetType == 19)
						{
							AddResponseTimeChartData(chartName, widgetOrder);							
						}
						else if ($widgetType == 20)
						{						
							var legendName = $('.cloneDiv #target_adc option:selected').text().trim();
							AddConcurrentSessionDetailData(chartName, widgetOrder, legendName);							
						}
						else if ($widgetType == 21)
						{			
							var legendName = $('.cloneDiv #target_flbGroup option:selected').text().trim();
							AddConcurrentSessionFlbData(chartName, widgetOrder, legendName);							
						}
						else if (($widgetType == 22) || ($widgetType == 23))
						{
							AddCpuSPChartData(chartName, widgetOrder);
						}
						var widgetElemnet = '';
						widgetElemnet = $('.contents_area .widget_box .portlet');						
						$(widgetElemnet).appendTo('#col1');
						
						$('#col1').sortable('refresh');
						
						// Widget 을 ADD 할때 Widget 의 Event 및 Option 들을 적용한다. (Widget을 Add할때 필히 적용해야 합니다.)
						addWidgetEvent(widgetOrder, $widthMinSize, $widthMaxSize, $heightMinSize, $heightMaxSize);
						registerWidgetEvent();	
						// Widget Count 증가 (for 위젯 갯수 Limit)
						G_widgetCount ++;
						// loadPassKey chart 기반 위젯 추가 완료 시
						loadPassKey = false;
					},
					errorFn : function(a,b,c)
					{
						exceptionEvent();
					}
				});
			}			
		}
	},
	
	registerWidgetEvent : function()
	{
		$('.adcMonitorLnk').click(function(e) 
		{
			e.preventDefault();
//			with (this) 
//			{
//				if($('.listcontainer_open').parent().hasClass("none"))
//					$('.listcontainer_open').click();
//				
//				var adcIndex = $(this).parent().attr('id');
//				console.log(adcIndex);
//				adcSetting._selectAdc(adcIndex);
//				$('.monitorMnu').click();
//				$('.monitorApplianceMnu').click();
//				
//				monitorAppliance.loadApplianceMonitorContent();
//				adcSetting.setObjOnAdcChange(monitorAppliance);
//				
//			}
			
//			if($('.listcontainer_open').parent().hasClass("none"))
//				$('.listcontainer_open').click();
			
			var adcIndex = $(this).parent().attr('id');
			adcSetting._selectAdc(adcIndex);
			
			$('.monitorMnu').click();
			$('.monitorApplianceMnu').click();
			
//			adcSetting.loadContent();
//			adcSetting.setGroupIndex(undefined);
//			monitorAppliance.loadApplianceMonitorContent();
//			adcSetting.setObjOnAdcChange(monitorAppliance);
			
//			$('.monitorMnu').click();
//			$('.monitorApplianceMnu').click();
//			networkMap.loadNetworkMapContent(adcSetting.getAdc());
		});		
		
		$('.serviceMonitorLnk').click(function(e) 
		{
			e.preventDefault();
/*			
			var vsIndex = $(this).parent().attr('id');
			var vsIpPort = $(this).parent().attr('idx');
			var adcIndex = vsIndex.split("_")[0];
			var vsIp = vsIpPort.split(":")[0];
			var vsPort = vsIpPort.split(":")[1];
			adcSetting._selectAdc(adcIndex);
						
			$('.monitorMnu').click();
//			$('.monitorServicePerfomanceMnu').click();
			
			adcSetting.loadContent();
//			monitorServicePerfomance.onAdcChange(vsIndex, vsPort);
//			monitorServicePerfomance.loadServicePerfomanceContent(vsIndex, vsPort);
			monitorServicePerfomance.loadServicePerfomanceContent(vsIndex, vsPort, undefined, vsIp, true);
			
//			monitorServicePerfomance.loadSvcPerfInfoList(1, 1, 2, undefined, vsIp);
			
					
*/			
			var vsIndex = $(this).parent().attr('id');
			var vsIpPort = $(this).parent().attr('idx');
			
			var adcIndex = vsIndex.split("_")[0];
			var vsIp = vsIpPort.split(":")[0];
			adcSetting._selectAdc(adcIndex);
			
			$('.monitorMnu').click();
			$('.contents').css('top', '112px');
//			$('.monitorNetworkMnu').click();
			
			adcSetting.loadContent();			
			networkMap.loadNetworkMapContent(adcSetting.getAdc(), 0, vsIp, undefined, taskQ);
			
		});		
		
		$('.memberMonitorLnk').click(function(e) 
		{
			e.preventDefault();
//			if($('.listcontainer_open').parent().hasClass("none"))
//				$('.listcontainer_open').click();
			
			var memberIndex = $(this).parent().attr('id');
			var memberIpPort = $(this).parent().attr('idx');
			
			var adcIndex = memberIndex.split("_")[0];
			var memberIp = memberIpPort.split(":")[0];
			adcSetting._selectAdc(adcIndex);
			
			$('.monitorMnu').click();
			$('.contents').css('top', '112px');
//			$('.monitorNetworkMnu').click();
			
			adcSetting.loadContent();
			networkMap.loadNetworkMapContent(adcSetting.getAdc(), 0, memberIp, undefined, taskQ);
//			adcSetting.setObjOnAdcChange(networkMap);			
		});	
	},
	// TODO Widget Load
	// Load 된 DashBaord 의 Widget 을 ReMake 하는 함수
	onLoadWidget : function(widgetList)
	{
		with (this)
		{
			if(widgetList == null)
				return;
			
			if(widgetList.length!=0 && widgetList.length==widgetLoadCount)
			{
				$(".widget_table tbody tr:even").css("background","#f4f4f4");    // Table 위젯 짝수 bg 변경
				widgetReplace(widgetList);
				// 전체 위젯 갯수 저장 (for 위젯 갯수 Limit)
				G_widgetCount = widgetLoadCount;
				return;
			}
			
			if(widgetList.length <= widgetLoadCount)
			{
				return;
			}
			
			var i = widgetLoadCount;			
			var ParamData = {
					"widgetInfo.index" : widgetList[i].index,
					"widgetInfo.type" : widgetList[i].type,
					"widgetInfo.name" : widgetList[i].name,													
					"widgetInfo.width" : widgetList[i].width,
					"widgetInfo.height" : widgetList[i].height,
					"widgetInfo.xPosition" : widgetList[i].xPosition,
					"widgetInfo.yPosition" : widgetList[i].yPosition,							
					"widgetTarget.category" : widgetList[i].targetObj.category,
					"widgetTarget.strIndex" : widgetList[i].targetObj.strIndex,
					"widgetTarget.index" : widgetList[i].targetObj.index,
					"widgetTarget.name" : widgetList[i].targetObj.name,
					"widgetTarget.desciption" : widgetList[i].targetObj.desciption,
					"widgetItem.widthMinSize" : widgetList[i].widthMin,
					"widgetItem.widthMaxSize" : widgetList[i].widthMax,
					"widgetItem.heightMinSize" : widgetList[i].heightMin,
					"widgetItem.heightMaxSize" : widgetList[i].heightMax,
					"widgetInfo.moreInfoIndex" : widgetList[i].moreInfoIndex
			};
			
			if (widgetList[i].type == 1 || widgetList[i].type == 3 || widgetList[i].type == 5 || widgetList[i].type == 6 
					|| widgetList[i].type == 7 || widgetList[i].type == 14 || widgetList[i].type == 15 
					|| widgetList[i].type == 16 || widgetList[i].type == 17 || widgetList[i].type == 18
					|| widgetList[i].type == 24 || widgetList[i].type == 25 || widgetList[i].type == 26)			
			{		
				var dashLoadUrl = "";
				if (widgetList[i].type == 1)
				{
					dashLoadUrl = "/dashboard/loadFaultMonitoring.action";
				}
				else if(widgetList[i].type == 3)
				{
					dashLoadUrl = "/dashboard/loadFaultMonitoringList.action";
				}
				else if(widgetList[i].type == 5)
				{
					dashLoadUrl = "/dashboard/loadAdcMonitoringList.action";
				}
				else if(widgetList[i].type == 6)
				{
					dashLoadUrl = "/dashboard/loadAdcMonitoring.action";
				}
				else if(widgetList[i].type == 7)
				{
					dashLoadUrl = "/dashboard/loadVsMonitoring.action";
				}
				else if(widgetList[i].type == 14)
				{
					dashLoadUrl = "/dashboard/loadAdcSummaryList.action";
				}
				else if(widgetList[i].type == 15)
				{
					dashLoadUrl = "/dashboard/loadVsSummaryList.action";
				}
				else if(widgetList[i].type == 16)
				{
					dashLoadUrl = "/dashboard/loadTop10VSInfo.action";
				}
				else if(widgetList[i].type == 17)
				{
					dashLoadUrl = "/dashboard/loadTop5AdcInfo.action";
				}
				else if (widgetList[i].type == 18)
				{
					// 18 번 ADC모니터링 Widget 은 도중에 Model Number 를 Get 하는 json 으로 인해 함수를 분리합니다.
					return ReloadadcApplianceMonitorWidget(widgetList[i], widgetList, ParamData);									
				}
				else if (widgetList[i].type == 24)
				{
					dashLoadUrl = '/dashboard/loadAdcStatusNotificationInfo.action';
				}
				else if (widgetList[i].type == 25)
				{
					dashLoadUrl = '/dashboard/loadVsStatusNotificationInfo.action';
				}
				else if (widgetList[i].type == 26)
				{
					dashLoadUrl = '/dashboard/loadMemberStatusNotificationInfo.action';
				}
				// List 및 Img 기반 Widget (Html Data Insert Type)
				ajaxManager.runHtmlExt({
					url : dashLoadUrl,
					data: ParamData,
					target: ".contents_area .widget_box",
					successFn : function(params) 
					{						
						// Load 된 Widget의 사이즈 Data를 이용하여 Widget Resizing
						reSizeWidget(widgetList, widgetLoadCount);						
						
						var widgetElemnet = '';
						widgetElemnet = $('.contents_area .widget_box .portlet');
						$(widgetElemnet).appendTo('#col4');
						// Load된 Widget 에도 각각 하나씩 Event 와 Option을 부여한다.
						addWidgetEvent(widgetList[i].index,
								widgetList[i].widthMin, widgetList[i].widthMax,
								widgetList[i].heightMin, widgetList[i].heightMax);
						
						widgetLoadCount++;
						onLoadWidget(widgetList);	
						registerWidgetEvent();	
					},
					errorFn : function(a,b,c)
					{
						exceptionEvent();
					}
				});
			}
			// Chart Widget (Json Data Insert Type)
			else if (widgetList[i].type == 2 || widgetList[i].type == 4 || widgetList[i].type == 8
						|| widgetList[i].type == 9 ||widgetList[i].type == 10 || widgetList[i].type == 11
							|| widgetList[i].type == 12 || widgetList[i].type == 13 || widgetList[i].type == 19
							|| widgetList[i].type == 20 || widgetList[i].type == 21 || widgetList[i].type == 22 || widgetList[i].type == 23)
			{
				ajaxManager.runHtmlExt({
					url : '/dashboard/loadAllChartWidget.action',
					data: ParamData,
					target: ".contents_area .widget_box",
					successFn : function(params) 
					{
						var chartName = "widgetChart"+widgetLoadCount;

						var $chartArea = $('.contents_area .widget_box .portlet .portlet-content-chart').filter(':last');			
						var chartElement ='';
//						chartElement += '<div id="' +chartName+ '" style="width: 100%; height: 140px; margin-top: 0px; padding-top: 0px;"></div>';
						
						if(widgetList[i].type == 18)
						{
							chartElement += '<div id="' +chartName+ '" style="width: 100%; height: 140px; margin-top: 0px; padding-top: 8px; padding-bottom: 5px;"></div>';
						}
						else
						{
							chartElement += '<div id="' +chartName+ '" style="width: 100%; height: 224px; margin-top: 0px; padding-top: 8px; padding-bottom: 5px;"></div>';
						}
						
//						if(widgetList[i].type == 23)
//						{
//							chartElement += '<div id="' +chartName+ '" style="width: 100%; height: 234px; margin-top: 0px; padding-top: 0px;"></div>';	
//						}
//						else if(widgetList[i].type == 8 || widgetList[i].type == 9 || widgetList[i].type == 10 || widgetList[i].type == 11 || widgetList[i].type == 19)
//						{
//							chartElement += '<div id="' +chartName+ '" style="width: 100%; height: 280px; margin-top: 0px; padding-top: 0px;"></div>';	
//						}
//						else
//						{
//							chartElement += '<div id="' +chartName+ '" style="width: 100%; height: 140px; margin-top: 0px; padding-top: 0px;"></div>';	
//						}
						$chartArea.empty();
						$chartArea.html(chartElement);					
						
						// Load 된 Widget의 사이즈 Data를 이용하여 Widget Resizing
						reSizeWidget(widgetList, widgetLoadCount);
						// Chart Data Load
						var chartRefreshKey = 1;	// Refersh 가 아닌 Load 일때는 Key 값 1 (int)
						if (widgetList[i].type == 2)
						{
							LoadFaultMonitoringData(chartName, widgetList, widgetList[i], chartRefreshKey);
						}
						else if (widgetList[i].type == 4)
						{
							LoadAdcChangeHistoryData(chartName, widgetList, widgetList[i], chartRefreshKey);
						}
						else if (widgetList[i].type == 8)
						{
							LoadAdcConnectionHistoryData(chartName, widgetList, widgetList[i], chartRefreshKey);
						}
						else if (widgetList[i].type == 9)
						{
							LoadAdcThroughtputHistoryData(chartName, widgetList, widgetList[i], chartRefreshKey);
						}
						else if (widgetList[i].type == 10)
						{
							LoadCpuHistoryData(chartName, widgetList, widgetList[i], chartRefreshKey);
						}
						else if (widgetList[i].type == 11)
						{
							LoadMemoryHistoryData(chartName, widgetList, widgetList[i], chartRefreshKey);
						}
						else if (widgetList[i].type == 12)
						{
							LoadAllAdcTrafficChartData(chartName, widgetList, widgetList[i], chartRefreshKey);
						}
						else if (widgetList[i].type == 13)
						{
							LoadVsStatusChartData(chartName, widgetList, widgetList[i], chartRefreshKey);
						}
						else if (widgetList[i].type == 19)
						{
							LoadResponseTimeChartData(chartName, widgetList, widgetList[i], chartRefreshKey);
						}
						else if (widgetList[i].type == 20)
						{
							LoadConcurrentSessionDetailData(chartName, widgetList, widgetList[i], chartRefreshKey);
						}
						else if (widgetList[i].type == 21)
						{
							LoadConcurrentSessionFlbData(chartName, widgetList, widgetList[i], chartRefreshKey);
						}
						else if ((widgetList[i].type == 22) || (widgetList[i].type == 23))
						{
							LoadCpuSPChartData(chartName, widgetList, widgetList[i], chartRefreshKey);
						}
						var widgetElemnet = '';
						widgetElemnet = $('.contents_area .widget_box .portlet');
						$(widgetElemnet).appendTo('#col4');
						// Load된 Widget 에도 각각 하나씩 Event 와 Option을 부여한다.					
						addWidgetEvent(widgetList[i].index,
								widgetList[i].widthMin, widgetList[i].widthMax,
								widgetList[i].heightMin, widgetList[i].heightMax);
					},
					errorFn : function(a,b,c)
					{
						exceptionEvent();
					}
				});
			}				
		}		
	},
	// Load 된 Widget 을 Size Data 를 사용하여 사이징하는 함수
	reSizeWidget : function(widgetList, widgetLoadCount)
	{
		with(this)
		{			
			var html = $('.contents_area .widget_box .portlet');
			html.css("width", widgetList[widgetLoadCount].widthMin + "px");
			html.css("height", widgetList[widgetLoadCount].heightMin + "px");
		}
	},
	// Refresh 된 Widget 을 Size Data 를 사용하여 사이징하는 함수
	reSizeRefreshWidget : function(widgetList, widgetLoadCount)
	{
		with(this)
		{		
			var html = $('.dashboardContentArea .printArea .column #'+widgetList[widgetLoadCount].index);
			html.css("width", widgetList[widgetLoadCount].widthMin + "px");
			html.css("height", widgetList[widgetLoadCount].heightMin + "px");
		}
	},	
	// Load 된 Widget 을 postion Data 를 사용하여 재 배치하는 함수
	widgetReplace : function(widgetList)
	{
		with (this)
		{
			var htmlWidgetList = $('.column .portlet');			
			for(var i=0; i < widgetList.length; i++)
			{
				if(widgetList[i].xPosition == 1)
				{
					$('#col1').append(htmlWidgetList[i]);
				}
				else if(widgetList[i].xPosition == 2)
				{
					$('#col2').append(htmlWidgetList[i]);
				}
				else if(widgetList[i].xPosition == 3)
				{
					$('#col3').append(htmlWidgetList[i]);
				}
			}			
			//allWidgetEvent();
		}
	},
	// refrsh 할 위젯 정보를 만드는 함수 TODO
	refreshDashboardWidgetTarget : function(refershChartObj)
	{
		with(this)
		{
			var $col1 = $('#col1').sortable('toArray');					
			
			var widgetListInput = [];
			//  Get Html Element Info for Insert Colum1 Data
			for (var i=0; i < $col1.length; i++)
			{			
				var col1WidgetIndex = $('#col1 .portlet')[i].id;
				var col1WidgetName = $('#col1 .portlet .widgetName')[i].value;
				var col1WidgetType = $('#col1 .portlet .widgetType')[i].value;
				var col1WidgetWidthMin = $('#col1 .portlet .widthMinSize')[i].value;
				var col1WidgetWidthMax = $('#col1 .portlet .widthMaxSize')[i].value;
				var col1WidgetHeightMin = $('#col1 .portlet .heightMinSize')[i].value;
				var col1WidgetHeightMax = $('#col1 .portlet .heightMaxSize')[i].value;				
				var col1WidgetWidth = $('#col1 .portlet')[i].offsetWidth;
				var col1WidgetHeight = $('#col1 .portlet')[i].offsetHeight;
				
				var col1WidgetTargetCategory = $('#col1 .portlet .targetCategory')[i].value;
				var col1WidgetTargetIndex = $('#col1 .portlet .targetIndex')[i].value;
				var col1WidgetTargetStrIndex = $('#col1 .portlet .targetStrIndex')[i].value;
				var col1WidgetTargetName = $('#col1 .portlet .targetName')[i].value;
				var col1WidgetTargetDesciption = $('#col1 .portlet .targetDesciption')[i].value;
				
				var co1WidgetMoreInfoIndex = $('#col1 .portlet .moreInfoIndex')[i].value;
								
				widgetListInput.push({
					index : col1WidgetIndex,
					name : col1WidgetName,
					type : col1WidgetType,
					targetObj : 
					{
						category : col1WidgetTargetCategory,
						index : col1WidgetTargetIndex,
						strIndex : col1WidgetTargetStrIndex,
						name : col1WidgetTargetName,
						desciption : col1WidgetTargetDesciption
					},
					width: col1WidgetWidth,
					widthMin : col1WidgetWidthMin,
					widthMax : col1WidgetWidthMax,
					height : col1WidgetHeight,
					heightMin : col1WidgetHeightMin,
					heightMax : col1WidgetHeightMax,
					xPosition : 1,
					yPosition : i + 1,
					moreInfoIndex : co1WidgetMoreInfoIndex
				});		
			}		
			refreshDashboardWidget(widgetListInput, refershChartObj);
		}
	},
	// TODO Dashboard Refresh Memory Leak 문제로 Refresh 는 기존의 Load Widget 이 아닌 하단 함수를 사용.
	refreshDashboardWidget : function(widgetList, refershChartObj)
	{
		with(this)
		{
			if(widgetList == null)
				return;			
			
			if(widgetList.length <= widgetLoadCount)
			{
				$(".widget_table tbody tr:even").css("background","#f4f4f4");
				return;
			}
			
			var i = widgetLoadCount;
			var ParamData = {
					"widgetInfo.index" : widgetList[i].index,
					"widgetInfo.type" : widgetList[i].type,
					"widgetInfo.name" : widgetList[i].name,													
					"widgetInfo.width" : widgetList[i].width,
					"widgetInfo.height" : widgetList[i].height,
					"widgetInfo.xPosition" : widgetList[i].xPosition,
					"widgetInfo.yPosition" : widgetList[i].yPosition,							
					"widgetTarget.category" : widgetList[i].targetObj.category,
					"widgetTarget.strIndex" : widgetList[i].targetObj.strIndex,
					"widgetTarget.index" : widgetList[i].targetObj.index,
					"widgetTarget.name" : widgetList[i].targetObj.name,
					"widgetTarget.desciption" : widgetList[i].targetObj.desciption,
					"widgetItem.widthMinSize" : widgetList[i].widthMin,
					"widgetItem.widthMaxSize" : widgetList[i].widthMax,
					"widgetItem.heightMinSize" : widgetList[i].heightMin,
					"widgetItem.heightMaxSize" : widgetList[i].heightMax,
					"widgetInfo.moreInfoIndex" : widgetList[i].moreInfoIndex
			};
			if (widgetList[i].type == 1 || widgetList[i].type == 3 || widgetList[i].type == 5 || widgetList[i].type == 6 
					|| widgetList[i].type == 7 || widgetList[i].type == 14 || widgetList[i].type == 15 
					|| widgetList[i].type == 16 || widgetList[i].type == 17 || widgetList[i].type == 18
					|| widgetList[i].type == 24 || widgetList[i].type == 25 || widgetList[i].type == 26)			
			{
				var dashLoadUrl = "";
				var targetName = ".dashboardContentArea .printArea .column #"+widgetList[i].index;
				if (widgetList[i].type == 1)
				{
					dashLoadUrl = "/dashboard/loadFaultMonitoring_Refresh.action";
				}
				else if(widgetList[i].type == 3)
				{
					dashLoadUrl = "/dashboard/loadFaultMonitoringList_Refresh.action";
				}
				else if(widgetList[i].type == 5)
				{
					dashLoadUrl = "/dashboard/loadAdcMonitoringList_Refresh.action";
				}
				else if(widgetList[i].type == 6)
				{
					dashLoadUrl = "/dashboard/loadAdcMonitoring_Refresh.action";
				}
				else if(widgetList[i].type == 7)
				{
					dashLoadUrl = "/dashboard/loadVsMonitoring_Refresh.action";
				}
				else if(widgetList[i].type == 14)
				{
					dashLoadUrl = "/dashboard/loadAdcSummaryList_Refresh.action";
				}
				else if(widgetList[i].type == 15)
				{
					dashLoadUrl = "/dashboard/loadVsSummaryList_Refresh.action";
				}
				else if(widgetList[i].type == 16)
				{
					dashLoadUrl = "/dashboard/loadTop10VSInfo_Refresh.action";
				}
				else if(widgetList[i].type == 17)
				{
					dashLoadUrl = "/dashboard/loadTop5AdcInfo_Refresh.action";
				}
				else if(widgetList[i].type == 18)
				{
					// 18 번 ADC모니터링 Widget 은 도중에 Model Number 를 Get 하는 json 으로 인해 함수를 분리합니다.
					return RefreshadcApplianceMonitorWidget(widgetList[i], widgetList, ParamData, targetName, refershChartObj, false);
				}
				else if(widgetList[i].type == 24)
				{
					dashLoadUrl = "/dashboard/loadAdcStatusNotificationInfo_Refresh.action";
				}
				else if(widgetList[i].type == 25)
				{
					dashLoadUrl = "/dashboard/loadVsStatusNotificationInfo_Refresh.action";
				}
				else if(widgetList[i].type == 26)
				{
					dashLoadUrl = "/dashboard/loadMemberStatusNotificationInfo_Refresh.action";
				}
				// List 및 Img 기반 Widget REFRESH (Html Data Insert Type)
				ajaxManager.runHtmlExt({
					url : dashLoadUrl,
					data: ParamData,
					target: targetName,
					successFn : function(params) 
					{
						// Load 된 Widget의 사이즈 Data를 이용하여 Widget Resizing
						reSizeRefreshWidget(widgetList, widgetLoadCount);
						// Load된 Widget 에도 각각 하나씩 Event 와 Option을 부여한다.
						addWidgetEvent(widgetList[i].index,
								widgetList[i].widthMin, widgetList[i].widthMax,
								widgetList[i].heightMin, widgetList[i].heightMax);
						widgetLoadCount++;
						refreshDashboardWidget(widgetList, refershChartObj);					
					},
					errorFn : function(a,b,c)
					{
						exceptionEvent();
					}
				});
			}
			// Chart Widget REFRESH (Json Data Insert Type)
			else if (widgetList[i].type == 2 || widgetList[i].type == 4 || widgetList[i].type == 8
						|| widgetList[i].type == 9 ||widgetList[i].type == 10 || widgetList[i].type == 11
							|| widgetList[i].type == 12 || widgetList[i].type == 13 || widgetList[i].type == 19
							|| widgetList[i].type == 20 || widgetList[i].type == 21 || widgetList[i].type == 22 || widgetList[i].type == 23)
			{
				var chartRefreshKey = 2;		// Refersh 일때는 Key 값 2 (int)
				// Chart Data Load
				if (widgetList[i].type == 2)
				{
					LoadFaultMonitoringData(refershChartObj, widgetList, widgetList[i], chartRefreshKey);
				}
				else if (widgetList[i].type == 4)
				{
					LoadAdcChangeHistoryData(refershChartObj, widgetList, widgetList[i], chartRefreshKey);
				}
				else if (widgetList[i].type == 8)
				{
					LoadAdcConnectionHistoryData(refershChartObj, widgetList, widgetList[i], chartRefreshKey);
				}
				else if (widgetList[i].type == 9)
				{
					LoadAdcThroughtputHistoryData(refershChartObj, widgetList, widgetList[i], chartRefreshKey);
				}
				else if (widgetList[i].type == 10)
				{
					LoadCpuHistoryData(refershChartObj, widgetList, widgetList[i], chartRefreshKey);
				}
				else if (widgetList[i].type == 11)
				{
					LoadMemoryHistoryData(refershChartObj, widgetList, widgetList[i], chartRefreshKey);
				}
				else if (widgetList[i].type == 12)
				{
					LoadAllAdcTrafficChartData(refershChartObj, widgetList, widgetList[i], chartRefreshKey);
				}
				else if (widgetList[i].type == 13)
				{
					LoadVsStatusChartData(refershChartObj, widgetList, widgetList[i], chartRefreshKey);
				}
				else if (widgetList[i].type == 19)
				{
					LoadResponseTimeChartData(refershChartObj, widgetList, widgetList[i], chartRefreshKey);
				}
				else if (widgetList[i].type == 20)
				{
					LoadConcurrentSessionDetailData(refershChartObj, widgetList, widgetList[i], chartRefreshKey);
				}
				else if (widgetList[i].type == 21)
				{
					LoadConcurrentSessionFlbData(refershChartObj, widgetList, widgetList[i], chartRefreshKey);
				}
				else if ((widgetList[i].type == 22) || (widgetList[i].type == 23))
				{
					LoadCpuSPChartData(refershChartObj, widgetList, widgetList[i], chartRefreshKey);
				}
			}
			$('#col1').sortable("enable");
		}
	},
	//TODO
	modifyWidget : function(index, widgetList, refershChartObj, selectedWidgetTargetArea)
	{
		with(this)
		{
			if(index == null)
				return;		
			
			var $targetCategory ='';
			var $targetIndex = '';
			var $targetStrIndex = '';
			var $targetName = '';
			var $moreWidgetInfo = undefined;
			var $targetAdcName = '';
			var $targetAdc = '';
			
			var $targetCategoryIndex = $('.pop_type_wrap .selected_category_Modify').filter(':last').text();
			var $targetGroupName = $('.pop_type_wrap .selected_group_Modify').filter(':last').text();
			if(selectedWidgetTargetArea == 11)
//				$targetAdcName = $('.pop_type_wrap .selected_adc').filter(':last').text();
				$targetAdcName = $('.pop_type_wrap .adcContent #target_adc').filter(':last').find('option:selected').text();
			else
				$targetAdcName = $('.pop_type_wrap .selected_adc_Modify').filter(':last').text();
			
			var $targetVserverName = $('.pop_type_wrap .selected_vserver_Modify').filter(':last').text().split(":")[0];			
			var $targetSelectList = $('.pop_type_wrap .selected_select_list_Modify').filter(':last').text();
			
			var $targetGroup = $('.pop_type_wrap .selected_group_index_Modify').filter(':last').text();		//index
			
			if(selectedWidgetTargetArea == 11)
//				$targetAdc = $('.pop_type_wrap .selected_adc_index').filter(':last').text();			//index
				$targetAdc = $('.pop_type_wrap .adcContent #target_adc').filter(':last').val();
			else
				$targetAdc = $('.pop_type_wrap .selected_adc_index_Modify').filter(':last').text();			//index
			
			var $targetVendor = $('.pop_type_wrap .selected_adc_vendor_Modify').filter(':last').text();
			var $targetVserver = $('.pop_type_wrap .selected_vserver_index_Modify').filter(':last').text();	//index			
			
			var $moreInfoStatus = $('.pop_type_wrap .selected_status_Modify').filter(':last').text();
			var $moreInfoStatusVs = $('.pop_type_wrap .selected_statusVs_Modify').filter(':last').text();
			var $moreInfoTime = $('.pop_type_wrap .selected_time_Modify').filter(':last').text();
			var $moreInfoOrdering = $('.pop_type_wrap .selected_ordering_Modify').filter(':last').text();
			var $moreInfoChart = $('.pop_type_wrap .selected_chart_Modify').filter(':last').text();

			// 추가 정보 선택 시
			if ($moreInfoStatus == null && $moreInfoTime == null && $moreInfoStatusVs == null && $moreInfoOrdering == null && $moreInfoChart == null)
			{
				$moreWidgetInfo = -1;
			}
			else if ($moreInfoStatus == -1 && $moreInfoTime == -1 && $moreInfoStatusVs == -1 && $moreInfoOrdering == -1 && $moreInfoChart == -1)
			{
				$moreWidgetInfo = -1;
			}
			else if ($moreInfoStatus == "" && $moreInfoTime == "" && $moreInfoStatusVs == "" && $moreInfoOrdering == "" && $moreInfoChart == "")
			{
				$moreWidgetInfo = -1;
			}
			else if ($moreInfoStatus != null && $moreInfoStatus != "" && $moreInfoStatus != -1)
			{
				$moreWidgetInfo = $moreInfoStatus;
			}
			else if ($moreInfoTime != null && $moreInfoTime != "" && $moreInfoTime != -1)
			{
				$moreWidgetInfo = $moreInfoTime;
			}
			else if ($moreInfoOrdering != null && $moreInfoOrdering != "" && $moreInfoOrdering != -1)
			{
				$moreWidgetInfo = $moreInfoOrdering;
			}
			else if ($moreInfoStatusVs != null && $moreInfoStatusVs != "" && $moreInfoStatusVs != -1)
			{
				$moreWidgetInfo = $moreInfoStatusVs;
			}
			else if ($moreInfoChart != null && $moreInfoChart != "" && $moreInfoChart != -1)
			{
				$moreWidgetInfo = $moreInfoChart;
			}	
			else
			{
				$moreWidgetInfo = -1;
			}
			// 아무 대상도 선택되지 않았을때
			if ($targetGroup == 0 && $targetAdc == 0 && $targetVserver == 0)
			{
				$targetCategory = 0;
				$targetIndex = 0;
				$targetStrIndex = "";
				$targetName = VAR_DASH_ALLGROUP;				
			}
			// 그룹 전체가 선택되었을때
			else if ($targetGroup == -1)
			{
				$targetCategory = 0;
				$targetIndex = 0;
				$targetStrIndex = "";
				$targetName = VAR_DASH_ALLGROUP;
			}
			// 그룹까지 선택되었을때
			else if ($targetAdc == 0 && $targetVserver == 0)
			{
				if ($targetCategoryIndex > 1)
					$targetCategory = $targetCategoryIndex;		
				else
					$targetCategory = 1;

				$targetIndex = $targetGroup;
				$targetStrIndex = "";
				$targetName = $targetGroupName;								
			}
			// ADC까지 선택되었을때
			else if ($targetVserver == 0)
			{
				$targetCategory = 2;
				$targetIndex = $targetAdc;
				$targetStrIndex = "";
				$targetName = $targetAdcName;				
			}
			// VServer / VService 까지 선택되었을때
			else
			{
				// 알테온 일때는 Vservice 이기때문에 Category 4
				if ($targetVendor == 2)
				{
					$targetCategory = 4;
				}
				else	// 나머지 ADC 에서는 Vserver 이기때문에 Category 3 
				{
					$targetCategory = 3;
				}				
				$targetIndex = 0;
				$targetStrIndex = $targetVserver;
				$targetName = $targetVserverName;				
			}			
			var ModifyWidgetInfo =
			{
				index : index,
				type : $('.column #'+index+' .widgetType').val(),
				name : $('.column #'+index+' .widgetName').val(),												
				width : $('.column #'+index+' .widgetWidth').val(),
				height : $('.column #'+index+' .widgetHeight').val(),
				xPosition : $('.column #'+index+' .widgetxPosition').val(),
				yPosition : $('.column #'+index+' .widgetyPosition').val(),
				targetObj : 
				{
					category : $targetCategory,
					index : $targetIndex,
					strIndex : $targetStrIndex,
					name : $targetName,
					desciption : $targetSelectList
				},										
				widthMinSize : $('.column #'+index+' .widthMinSize').val(),
				widthMaxSize : $('.column #'+index+' .widthMaxSize').val(),
				heightMinSize : $('.column #'+index+' .heightMinSize').val(),
				heightMaxSize : $('.column #'+index+' .heightMaxSize').val(),
				moreInfoIndex : $moreWidgetInfo
			};
			var ParamData = {
					"widgetInfo.index" : ModifyWidgetInfo.index,
					"widgetInfo.type" : ModifyWidgetInfo.type,
					"widgetInfo.name" : ModifyWidgetInfo.name,										
					"widgetInfo.width" : ModifyWidgetInfo.width,
					"widgetInfo.height" : ModifyWidgetInfo.height,
					"widgetInfo.xPosition" : ModifyWidgetInfo.xPosition,
					"widgetInfo.yPosition" : ModifyWidgetInfo.yPosition,						
					"widgetTarget.category" : ModifyWidgetInfo.targetObj.category,
					"widgetTarget.strIndex" : ModifyWidgetInfo.targetObj.strIndex,
					"widgetTarget.index" : ModifyWidgetInfo.targetObj.index,
					"widgetTarget.name" : ModifyWidgetInfo.targetObj.name,
					"widgetTarget.desciption" : ModifyWidgetInfo.targetObj.desciption,
					"widgetItem.widthMinSize" : ModifyWidgetInfo.widthMinSize,
					"widgetItem.widthMaxSize" : ModifyWidgetInfo.widthMaxSize,
					"widgetItem.heightMinSize" : ModifyWidgetInfo.heightMinSize,
					"widgetItem.heightMaxSize" : ModifyWidgetInfo.heightMaxSize,
					"widgetInfo.moreInfoIndex" : ModifyWidgetInfo.moreInfoIndex
			};
			if (ModifyWidgetInfo.type == 1 || ModifyWidgetInfo.type == 3 || ModifyWidgetInfo.type == 5 || ModifyWidgetInfo.type == 6 
					|| ModifyWidgetInfo.type == 7 || ModifyWidgetInfo.type == 14 || ModifyWidgetInfo.type == 15 
					|| ModifyWidgetInfo.type == 16 || ModifyWidgetInfo.type == 17 || ModifyWidgetInfo.type == 18
					|| ModifyWidgetInfo.type == 24 || ModifyWidgetInfo.type == 25 || ModifyWidgetInfo.type == 26)			
			{
				var dashLoadUrl = "";
				var targetName = ".dashboardContentArea .printArea .column #"+index;
				if (ModifyWidgetInfo.type == 1)
				{
					dashLoadUrl = "/dashboard/loadFaultMonitoring_Refresh.action";
				}
				else if(ModifyWidgetInfo.type == 3)
				{
					dashLoadUrl = "/dashboard/loadFaultMonitoringList_Refresh.action";
				}
				else if(ModifyWidgetInfo.type == 5)
				{
					dashLoadUrl = "/dashboard/loadAdcMonitoringList_Refresh.action";
				}
				else if(ModifyWidgetInfo.type == 6)
				{
					dashLoadUrl = "/dashboard/loadAdcMonitoring_Refresh.action";
				}
				else if(ModifyWidgetInfo.type == 7)
				{
					dashLoadUrl = "/dashboard/loadVsMonitoring_Refresh.action";
				}
				else if(ModifyWidgetInfo.type == 14)
				{
					dashLoadUrl = "/dashboard/loadAdcSummaryList_Refresh.action";
				}
				else if(ModifyWidgetInfo.type == 15)
				{
					dashLoadUrl = "/dashboard/loadVsSummaryList_Refresh.action";
				}
				else if(ModifyWidgetInfo.type == 16)
				{
					dashLoadUrl = "/dashboard/loadTop10VSInfo_Refresh.action";
				}
				else if(ModifyWidgetInfo.type == 17)
				{
					dashLoadUrl = "/dashboard/loadTop5AdcInfo_Refresh.action";
				}
				else if(ModifyWidgetInfo.type == 18)
				{
					// 18 번 ADC 장비 모니터링 Widget 은 도중에 Model Number 를 Get 하는 json 으로 인해 함수를 분리합니다.
					RefreshadcApplianceMonitorWidget(ModifyWidgetInfo, widgetList, ParamData, targetName, refershChartObj, true);
					//loadPassKey 위젯 수정 완료 시
					loadPassKey = false;
					// Widget Modify 후 팝업 제거
					$('.popup_type1').remove();
					$('.cloneDiv').remove();
					onAutoRefreshForEvent();
					return;
				}
				else if(ModifyWidgetInfo.type  == 24)
				{
					dashLoadUrl = "/dashboard/loadAdcStatusNotificationInfo_Refresh.action";
				}
				else if(ModifyWidgetInfo.type == 25)
				{
					dashLoadUrl = "/dashboard/loadVsStatusNotificationInfo_Refresh.action";
				}
				else if(ModifyWidgetInfo.type == 26)
				{
					dashLoadUrl = "/dashboard/loadMemberStatusNotificationInfo_Refresh.action";
				}
					
				// List 및 Img 기반 Widget REFRESH (Html Data Insert Type)
				ajaxManager.runHtmlExt({
					url : dashLoadUrl,
					data: ParamData,
					target: targetName,
					successFn : function(params) 
					{						
						// Load된 Widget 에도 각각 하나씩 Event 와 Option을 부여한다.
						addWidgetEvent(ModifyWidgetInfo.index,
								ModifyWidgetInfo.widthMin, ModifyWidgetInfo.widthMax,
								ModifyWidgetInfo.heightMin, ModifyWidgetInfo.heightMax);
						widgetLoadCount++;						
						$(".widget_table tbody tr:even").css("background","#f4f4f4");						
					},
					errorFn : function(a,b,c)
					{
						exceptionEvent();
					}
				});
			}
			// Chart Widget REFRESH (Json Data Insert Type)
			else if (ModifyWidgetInfo.type == 2 || ModifyWidgetInfo.type == 4 || ModifyWidgetInfo.type == 8
						|| ModifyWidgetInfo.type == 9 ||ModifyWidgetInfo.type == 10 || ModifyWidgetInfo.type == 11
							|| ModifyWidgetInfo.type == 12 || ModifyWidgetInfo.type == 13 || ModifyWidgetInfo.type == 19
							|| ModifyWidgetInfo.type == 20 || ModifyWidgetInfo.type == 21 || ModifyWidgetInfo.type == 22 || ModifyWidgetInfo.type == 23)
			{
				// Content 내Chart Refresh 이외 Header 정보 바꾸는 기능
				var widgetHeaderInfo = $('.column #'+index+' .sub_title').filter(':last');
				widgetHeaderInfo.empty();
				widgetHeaderInfo.html(ModifyWidgetInfo.targetObj.name);				
				$('.column #'+index+' .targetCategory').val(ModifyWidgetInfo.targetObj.category);
				$('.column #'+index+' .targetIndex').val(ModifyWidgetInfo.targetObj.index);
				$('.column #'+index+' .targetStrIndex').val(ModifyWidgetInfo.targetObj.strIndex);
				$('.column #'+index+' .targetName').val(ModifyWidgetInfo.targetObj.name);
				$('.column #'+index+' .moreInfoIndex').val(ModifyWidgetInfo.moreInfoIndex);
				
				var chartRefreshKey = 3;		// modify 일때는 Key 값 3 (int)
				// Chart Data Load
				if (ModifyWidgetInfo.type == 2)
				{
					LoadFaultMonitoringData(refershChartObj, widgetList, ModifyWidgetInfo, chartRefreshKey);
				}
				else if (ModifyWidgetInfo.type == 4)
				{
					LoadAdcChangeHistoryData(refershChartObj, widgetList, ModifyWidgetInfo, chartRefreshKey);
				}
				else if (ModifyWidgetInfo.type == 8)
				{
					LoadAdcConnectionHistoryData(refershChartObj, widgetList, ModifyWidgetInfo, chartRefreshKey);
				}
				else if (ModifyWidgetInfo.type == 9)
				{
					LoadAdcThroughtputHistoryData(refershChartObj, widgetList, ModifyWidgetInfo, chartRefreshKey);
				}
				else if (ModifyWidgetInfo.type == 10)
				{
					LoadCpuHistoryData(refershChartObj, widgetList, ModifyWidgetInfo, chartRefreshKey);
				}
				else if (ModifyWidgetInfo.type == 11)
				{
					LoadMemoryHistoryData(refershChartObj, widgetList, ModifyWidgetInfo, chartRefreshKey);
				}
				else if (ModifyWidgetInfo.type == 12)
				{
					LoadAllAdcTrafficChartData(refershChartObj, widgetList, ModifyWidgetInfo, chartRefreshKey);
				}
				else if (ModifyWidgetInfo.type == 13)
				{
					LoadVsStatusChartData(refershChartObj, widgetList, ModifyWidgetInfo, chartRefreshKey);
				}
				else if (ModifyWidgetInfo.type == 19)
				{
					LoadResponseTimeChartData(refershChartObj, widgetList, ModifyWidgetInfo, chartRefreshKey);
				}				
				else if (ModifyWidgetInfo.type == 20)
				{
					LoadConcurrentSessionDetailData(refershChartObj, widgetList, ModifyWidgetInfo, chartRefreshKey);
				}
				else if (ModifyWidgetInfo.type == 21)
				{
					LoadConcurrentSessionFlbData(refershChartObj, widgetList, ModifyWidgetInfo, chartRefreshKey);
				}
				else if ((ModifyWidgetInfo.type == 22) || (ModifyWidgetInfo.type == 23))
				{
					LoadCpuSPChartData(refershChartObj, widgetList, ModifyWidgetInfo, chartRefreshKey);
				}
			}
			//loadPassKey 위젯 수정 완료 시
			loadPassKey = false;
			// Widget Modify 후 팝업 제거
			$('.popup_type1').remove();
			$('.cloneDiv').remove();
			onAutoRefreshForEvent();
		}
	},
	//TODO NEW Save info
	newSaveDashboardInfo : function()
	{
		with(this)
		{
			var dashboardNameInput = $('.cloneDiv input[name="dashboardNameInput"]').val();						
			if (!$('.cloneDiv input[name="dashboardNameInput"]').validate(
				{
					name: "Dashboard",
					required: true,
//					type: "!special",
					type: "name",
					lengthRange: [1,64]
				}))
			{
				return;
			}			

			ajaxManager.runJson({
				url	: '/dashboard/loadDashboardListInfo.action',
				data :
				{					
				},
				successFn : function(data) 
				{
					var dashboardAllList = data.dashboardInfoList;
					var dashboardDuplicationKey = false;
					if(dashboardAllList.length != 0)
					{
						for (var i=0; i < dashboardAllList.length; i++)
						{
							if(dashboardAllList[i].name == dashboardNameInput)
							{
								dashboardDuplicationKey = true;
								break;								
							}						
							else
							{
								dashboardDuplicationKey = false;
							}
						}
					}
					else
					{
						dashboardDuplicationKey = false;
					}
					
					if(dashboardDuplicationKey == true)
					{
						$.obAlertNotice(dashboardNameInput + " " + VAR_DASH_ALRSBOA);						
						return;
					}
					else
					{
						saveDashboardInfo(0, dashboardNameInput);
					}
				}				
			});		
		}
	},
	saveDashboardInfo : function(dashboardSaveIndex, dashboardSaveName)
	{
		with (this)
		{
			var $col1 = $('#col1').sortable('toArray');					
			var widgetSaveIndex = 0;
			var widgetListInput = [];
			//  Get Html Element Info for Insert Colum1 Data
			for (var i=0; i < $col1.length; i++)
			{
				widgetSaveIndex = widgetSaveIndex +1;
				var col1WidgetIndex = widgetSaveIndex;
				var col1WidgetName = $('#col1 .portlet .widgetName')[i].value;
				var col1WidgetType = $('#col1 .portlet .widgetType')[i].value;
				var col1WidgetWidthMin = $('#col1 .portlet .widthMinSize')[i].value;
				var col1WidgetWidthMax = $('#col1 .portlet .widthMaxSize')[i].value;
				var col1WidgetHeightMin = $('#col1 .portlet .heightMinSize')[i].value;
				var col1WidgetHeightMax = $('#col1 .portlet .heightMaxSize')[i].value;				
				var col1WidgetWidth = $('#col1 .portlet')[i].offsetWidth;
				var col1WidgetHeight = $('#col1 .portlet')[i].offsetHeight;
				
				var col1WidgetTargetCategory = $('#col1 .portlet .targetCategory')[i].value;
				var col1WidgetTargetIndex = $('#col1 .portlet .targetIndex')[i].value;
				var col1WidgetTargetStrIndex = $('#col1 .portlet .targetStrIndex')[i].value;
				var col1WidgetTargetName = $('#col1 .portlet .targetName')[i].value;
				var col1WidgetTargetDesciption = $('#col1 .portlet .targetDesciption')[i].value;
				
				var co1WidgetMoreInfoIndex = $('#col1 .portlet .moreInfoIndex')[i].value;
								
				widgetListInput.push({
					index : col1WidgetIndex,
					name : col1WidgetName,
					type : col1WidgetType,
					targetObj : 
					{
						category : col1WidgetTargetCategory,
						index : col1WidgetTargetIndex,
						strIndex : col1WidgetTargetStrIndex,
						name : col1WidgetTargetName,
						desciption : col1WidgetTargetDesciption
					},
					width: col1WidgetWidth,
					widthMin : col1WidgetWidthMin,
					widthMax : col1WidgetWidthMax,
					height : col1WidgetHeight,
					heightMin : col1WidgetHeightMin,
					heightMax : col1WidgetHeightMax,
					xPosition : 1,
					yPosition : i + 1,
					moreInfoIndex : co1WidgetMoreInfoIndex
				});		
			}	
			
			if (!isValidStringLength(dashboardSaveName, 1, 64))
			{
				var data = VAR_COMMON_LENGTHFORMAT+"("+1+"~"+64+")";
				$.obAlertNotice(data);
				return ;
			}

			if (!isExistSpecialCharacter(dashboardSaveName))
			{
				$.obAlertNotice(VAR_FAULTSETTING_SPECIALCHAR);
				return ;
			}

			OBajaxManager.runJson({
				url	: '/dashboard/saveDashboardInfo.action',
				data :
				{
					"dashboardName" : dashboardSaveName,
					"dashboardIndex" : dashboardSaveIndex,
					"widgetListString" : Object.toJSON(widgetListInput)
				},
				successFn : function(data) 
				{
					var confirmVal = "";
					if (langCode == "ko_KR")
					{
						confirmVal = dashboardSaveName + " "+ VAR_DASH_BOASSUC;
					}
					else if (langCode == "en_US")
					{
						confirmVal = VAR_DASH_BOARD + ' "' + dashboardSaveName + '" ' + VAR_DASH_BOASSUC;
					}
					else
					{
						confirmVal = dashboardSaveName + " "+ VAR_DASH_BOASSUC;
					}
					
					$.obAlertNotice(confirmVal);
					$('.popup_type1').remove();
					$('.cloneDiv').remove();
					onAutoRefreshForEvent();
					refreshDashBoardHeader(dashboardSaveName);
				}				
			});
			widgetSaveIndex = undefined;
		}
	},
	// sortable 적용 함수
	sortableUI : function()
	{
		with (this)
		{
			$( "#col1" ).sortable(
			{
				//connectWith: "#col1, #col2, #col3",
				handle: '.portlet-header',
				start: function( event, ui ) {
					offAutoRefreshForEvent();
				},
				stop: function( event, ui ) {
					onAutoRefreshForEvent();
				}
			});		
			
			$( "#col1" ).disableSelection();
		}
	},
	// Add 후 Add 한 위젯에만 적용하는 Event 및 Option
	addWidgetEvent : function(index, widthMin, widthMax, heightMin, heightMax)
	{
		with(this)
		{
			/*$( "#"+ index ).hover(function()
			{				
			});
			*/
			/*$( "#"+ index + " .portlet-header .ui-icon-minusthick" ).click(function()
			{
				$( this ).toggleClass("ui-icon-minusthick").toggleClass("ui-icon-plusthick");
				$( this ).parents(".portlet:first").find(".portlet-content").toggle();
			});*/	// 완성전까지 보류 - 스틱기능
			$( '.column #'+ index + ' .portlet-header .ui-icon-closethick').click(function()
			{
				$(this).parents(".portlet").remove();
				$('#col1').sortable('refresh');				
				G_widgetCount = G_widgetCount -1;
				//loadPassKey 위젯 삭제시
				loadPassKey = false;
			});			
			$( '.column #'+ index + ' .portlet-header .ui-icon-gear' ).click(function(event)
			{
				event.preventDefault();
				offAutoRefreshForEvent();
				OBajaxManager.runJson({
					url	: '/dashboard/modifyWidgetInfo.action',
					data :
					{					
					},
					successFn : function(data) 
					{						
						loadWidgetModifyPopup(index, data.widgetItemList);		
					}						
				});
								
			});
			$('#col1').disableSelection();
			/*$("#" + index).resizable({
				minWidth: widthMin,
				maxWidth: widthMax,
				minHeight: heightMin,
				maxHeight: heightMax,
			});*/	// Resize 차단
			
		}
	},
	// Load 후 Load 된 전체 위젯에 적용하는 Event (현재 미사용)
	allWidgetEvent : function()
	{
		$( ".portlet-header .ui-icon-minusthick" ).click(function()
		{
			$( this ).toggleClass("ui-icon-minusthick").toggleClass("ui-icon-plusthick");
			$( this ).parents(".portlet:first").find(".portlet-content").toggle();			
		});
		$( ".portlet-header .ui-icon-closethick" ).click(function()
		{			
			$( this ).parents(".portlet").remove();
			$('#col1').sortable('refresh');			
		});
		//$("#col1, #col2, #col3").disableSelection();
		$(".portlet").resizable({
			/*handles: "se"*/
		});	
	},
	// Save 시 덮어씌울지 물어보는 Dialog , 삭제대기
	saveDashboardDialog : function(dashboardName_Textarea)
	{
		with(this)
		{
			var retVal = confirm(dashboardName_Textarea + VAR_DASH_BOANSAVE);
			if( retVal == true )
			{				   
				return true;
			}
			else
			{			
				$('.contents_area .dashboardName').val($('.contents_area #DashboardList').children('option').filter(':selected').text());				
				return false;
			}
		}
	},
	// refersh 나 Load 할때 유지되지 않는다고 물어보는 Dialog
	refreshDashboardDialog : function()
	{
		with(this)
		{
			var retVal = confirm(VAR_DASH_BOANOTSAVE);
			if( retVal == true )
			{				
				return true;
			}
			else
			{
				return false;
			}
		}
	},
	// Dashboard ReLoad 후 초기화 되는 DashboardSelectBox 를 다시 선택되도록 하는 함수
	selectboxChange : function(dashboardIndexKey)
	{
		$('#DashboardList option').each(function() 
		{
			if ($(this).val() == dashboardIndexKey)
			{
				$(this).attr("selected", "selected");
				return false;
			}
		});
	},
	// Dashboard ReLoad 후 초기화 되는 DashboardSelectBox 를 다시 선택되도록 하는 함수
	selectboxChangeDashboardName : function(dashboardName)
	{
		$('#DashboardList option').each(function() 
		{
			if ($(this).text() == dashboardName)
			{
				$(this).attr("selected", "selected");
				return false;
			}
		});
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
					widgetLoadCount = 0;
					//refreshDashboardWidget(G_widgetList, G_refershChartObj);
					$( "#col1" ).sortable("disable");
					refreshDashboardWidgetTarget(G_refershChartObj);
					updateRefreshDate();
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
		$('#refresh_timestamp').text(dateTime);
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
	},
	// 자동새로고침 Check Box 클릭 해제
	offAutoRefreshForEvent : function()
	{
		with(this)
		{
			var autoRefreshChk = $('.auto .autoRefreshChk');
			if(autoRefreshChk.is(':checked'))
			{
				autoRefreshChk.prop("checked", false);
				clearAutoRefreshTimer();
			}
			else
			{	
			}
		}
	},
	// 자동새로고침 Check Box 클릭
	onAutoRefreshForEvent : function()
	{
		with(this)
		{
			var autoRefreshChk = $('.auto .autoRefreshChk');
			if(autoRefreshChk.is(':checked') == false)
			{
				autoRefreshChk.prop("checked", true);
				autoRefreshDashboard();
			}
			else
			{				
			}
		}
	},
	
	/*
	 *  Widget Add & Refresh Area
	 */
	
	// MemoryChart Data Get *WidgetLoad*
	LoadMemoryHistoryData : function(chartName, widgetList, loadedWidgetList, chartRefreshKey)
	{
		with (this)
		{
			var params = undefined;
			if (chartRefreshKey == 1)
			{
				params = {
					"widgetTarget.category" : $('.portlet .targetCategory').val(),
					"widgetTarget.index" : $('.portlet .targetIndex').val(),
					"widgetTarget.extraInfo" : loadedWidgetList.index
				};
			}
			else
			{
				params = {
					"widgetTarget.category" : loadedWidgetList.targetObj.category,
					"widgetTarget.index" : loadedWidgetList.targetObj.index,
					"widgetTarget.extraInfo" : loadedWidgetList.index
				};
			}
			ajaxManager.runJson({
				url			: "/dashboard/loadMemoryHistoryInfo.action",
				data		: params,
				successFn	: function(data) 
				{
					if (chartRefreshKey == 1)
					{
						var chartobj = undefined;
						if(data.widgetTarget.category == 1)
						{
							chartobj = widgetChart.GenerateAdcGroupMemoryHistoryChart(data, chartName, chartRefreshKey, intervalMonitor);						
						}
						else
						{
							chartobj = widgetChart.GenerateMemoryHistoryChart(data, chartName, chartRefreshKey, intervalMonitor);
						}
						var loadedWidgetindex = loadedWidgetList.index;
						G_refershChartObj[loadedWidgetindex] = chartobj;
						widgetLoadCount++;
						onLoadWidget(widgetList);
					}
					if (chartRefreshKey == 2)
					{
						var loadedWidgetindex = loadedWidgetList.index;
						var chartObj = chartName[loadedWidgetindex];
						if(data.widgetTarget.category == 1)
						{
							widgetChart.GenerateAdcGroupMemoryHistoryChart(data, chartObj, chartRefreshKey, intervalMonitor);						
						}
						else
						{
							widgetChart.GenerateMemoryHistoryChart(data, chartObj, chartRefreshKey, intervalMonitor);
						}
//						var loadedWidgetindex = loadedWidgetList.index;
//						G_refershChartObj[loadedWidgetindex] = chartobj;
						widgetLoadCount++;
						refreshDashboardWidget(widgetList, chartName);
					}
					if (chartRefreshKey == 3)
					{
						var loadedWidgetindex = loadedWidgetList.index;
						var chartObj = chartName[loadedWidgetindex];
						if(data.widgetTarget.category == 1)
						{
							widgetChart.GenerateAdcGroupMemoryHistoryChart(data, chartObj, chartRefreshKey, intervalMonitor);						
						}
						else
						{
							widgetChart.GenerateMemoryHistoryChart(data, chartObj, chartRefreshKey, intervalMonitor);
						}
//						var loadedWidgetindex = loadedWidgetList.index;
//						G_refershChartObj[loadedWidgetindex] = chartobj;
					}
				},
				completeFn	: function(data)
				{					
				}
			});
		}
		return true;
	},
	// MemoryChart Data Get *WidgetAdd*
	AddMemoryHistoryData : function(chartName, addedwidgetIndex)
	{
		with (this)
		{			
			var params = {
				"widgetTarget.category" : $('.portlet .targetCategory').val(),
				"widgetTarget.index" : $('.portlet .targetIndex').val()				
			};
			ajaxManager.runJsonExt({
				url			: "/dashboard/loadMemoryHistoryInfo.action",
				data		: params,
				successFn	: function(data) 
				{
					var chartobj = undefined;
					if(data.widgetTarget.category == 1)
					{
						chartobj = widgetChart.GenerateAdcGroupMemoryHistoryChart(data, chartName, 1, intervalMonitor);						
					}
					else
					{
						chartobj = widgetChart.GenerateMemoryHistoryChart(data, chartName, 1, intervalMonitor);
					}				
					G_refershChartObj[addedwidgetIndex] = chartobj;
					widgetAddCount++;					
				},
				completeFn	: function(data)
				{					
				}
			});
		}
		return true;
	},
	// CpuChart Data Get *WidgetLoad*
	LoadCpuHistoryData : function(chartName, widgetList, loadedWidgetList, chartRefreshKey)
	{
		with (this)
		{
			var params = undefined;
			if (chartRefreshKey == 1)
			{
				params = {
						"widgetTarget.category" : $('.portlet .targetCategory').val(),
						"widgetTarget.index" : $('.portlet .targetIndex').val(),
						"widgetTarget.extraInfo" : loadedWidgetList.index
				};
			}
			else
			{
				params = {
						"widgetTarget.category" : loadedWidgetList.targetObj.category,
						"widgetTarget.index" : loadedWidgetList.targetObj.index,
						"widgetTarget.extraInfo" : loadedWidgetList.index
				};
			}
			ajaxManager.runJsonExt({
				url			: "/dashboard/loadCpuHistoryInfo.action",
				data		: params,
				successFn	: function(data) 
				{
					if (chartRefreshKey == 1)
					{
						var chartobj = undefined;
						if(data.widgetTarget.category == 1)
						{
							chartobj = widgetChart.GenerateAdcGroupCpuHistoryChart(data, chartName, chartRefreshKey, intervalMonitor);						
						}
						else
						{
							chartobj = widgetChart.GenerateCpuHistoryChart(data, chartName, chartRefreshKey, intervalMonitor);
						}
						var loadedWidgetindex = loadedWidgetList.index;
						G_refershChartObj[loadedWidgetindex] = chartobj;
						widgetLoadCount++;
						onLoadWidget(widgetList);
					}
					if (chartRefreshKey == 2)
					{
						var loadedWidgetindex = loadedWidgetList.index;
						var chartObj = chartName[loadedWidgetindex];
						if(data.widgetTarget.category == 1)
						{
							widgetChart.GenerateAdcGroupCpuHistoryChart(data, chartObj, chartRefreshKey, intervalMonitor);						
						}
						else
						{
							widgetChart.GenerateCpuHistoryChart(data, chartObj, chartRefreshKey, intervalMonitor);
						}
//						var loadedWidgetindex = loadedWidgetList.index;
//						G_refershChartObj[loadedWidgetindex] = chartobj;
						widgetLoadCount++;
						refreshDashboardWidget(widgetList, chartName);
					}
					if (chartRefreshKey == 3)
					{
						var loadedWidgetindex = loadedWidgetList.index;
						var chartObj = chartName[loadedWidgetindex];
						if(data.widgetTarget.category == 1)
						{
							widgetChart.GenerateAdcGroupCpuHistoryChart(data, chartObj, chartRefreshKey, intervalMonitor);						
						}
						else
						{
							widgetChart.GenerateCpuHistoryChart(data, chartObj, chartRefreshKey, intervalMonitor);
						}
//						var loadedWidgetindex = loadedWidgetList.index;
//						G_refershChartObj[loadedWidgetindex] = chartobj;
					}
				},
				completeFn	: function(data)
				{					
				}
			});
		}
		return true;
	},
	// CpuChart Data Get *WidgetAdd*
	AddCpuHistoryData : function(chartName, addedwidgetIndex)
	{
		with (this)
		{
		var params = {
				"widgetTarget.category" : $('.portlet .targetCategory').val(),
				"widgetTarget.index" : $('.portlet .targetIndex').val()				
			};		
		ajaxManager.runJsonExt({
				url : "/dashboard/loadCpuHistoryInfo.action",
				data : params,
				successFn : function(data)
				{
					var chartobj = undefined;
					if(data.widgetTarget.category == 1)
					{
						chartobj = widgetChart.GenerateAdcGroupCpuHistoryChart(data, chartName, 1, intervalMonitor);						
					}
					else
					{
						chartobj = widgetChart.GenerateCpuHistoryChart(data, chartName, 1, intervalMonitor);
					}
					G_refershChartObj[addedwidgetIndex] = chartobj;
					widgetAddCount++;
				},
				completeFn : function(data)
				{
					
				},
				errorFn : function(a,b,c)
				{
					exceptionEvent();
				}
			});
		}
		return true;
	},
	// FaultMonitoring Data Get *WidgetLoad*
	LoadFaultMonitoringData : function(chartName, widgetList, loadedWidgetList, chartRefreshKey)
	{
		with (this)
		{
			var params = undefined;
			if (chartRefreshKey == 1)
			{
				params = {
					"widgetTarget.index" : $('.portlet .targetIndex').val(),
					"widgetTarget.extraInfo" : loadedWidgetList.index
				};
			}
			else
			{
				params = {
					"widgetTarget.index" : loadedWidgetList.targetObj.index,
					"widgetTarget.extraInfo" : loadedWidgetList.index
				};
			}
			ajaxManager.runJsonExt({
				url			: "/dashboard/loadFaultMonitoringChart.action",
				data		: params,
				successFn	: function(data) 
				{
					if (chartRefreshKey == 1)
					{
						var chartobj = widgetChart.GenerateFaultMonitoringChart(data, chartName, chartRefreshKey, intervalMonitor);
						var loadedWidgetindex = loadedWidgetList.index;
						G_refershChartObj[loadedWidgetindex] = chartobj;
						widgetLoadCount++;
						onLoadWidget(widgetList);
					}
					if (chartRefreshKey == 2)
					{
						var loadedWidgetindex = loadedWidgetList.index;
						var chartObj = chartName[loadedWidgetindex];
						widgetChart.GenerateFaultMonitoringChart(data, chartObj, chartRefreshKey, intervalMonitor);
						widgetLoadCount++;
						refreshDashboardWidget(widgetList, chartName);
					}
					if (chartRefreshKey == 3)
					{
						var loadedWidgetindex = loadedWidgetList.index;
						var chartObj = chartName[loadedWidgetindex];
						widgetChart.GenerateFaultMonitoringChart(data, chartObj, chartRefreshKey, intervalMonitor);						
					}	
				},
				completeFn	: function(data)
				{					
				}
			});
		}
		return true;
	},	
	// FaultMonitoring Data Get *WidgetAdd*
	AddFaultMonitoringData : function(chartName, addedwidgetIndex)
	{
		with (this)
		{
			var params = {
					"widgetTarget.index" : $('.portlet .targetIndex').val()
			};
			ajaxManager.runJsonExt({
				url : "/dashboard/loadFaultMonitoringChart.action",
				data : params,
				successFn : function(data)
				{
					var chartobj = widgetChart.GenerateFaultMonitoringChart(data, chartName, 1, intervalMonitor);
					G_refershChartObj[addedwidgetIndex] = chartobj;
					widgetAddCount++;
				},
				completeFn : function(data)
				{
					
				},
				errorFn : function(a,b,c)
				{
					exceptionEvent();
				}
			});
		}
		return true;
	},
	// AdcChangeHistory Chart Data Get *WidgetLoad*
	LoadAdcChangeHistoryData : function(chartName, widgetList, loadedWidgetList, chartRefreshKey)
	{
		with (this)
		{
			var params = undefined;
			if (chartRefreshKey == 1)
			{
				params = {
					"widgetTarget.index" : $('.portlet .targetIndex').val(),
					"widgetTarget.extraInfo" : loadedWidgetList.index
				};
			}
			else
			{
				params = {
					"widgetTarget.index" : loadedWidgetList.targetObj.index,
					"widgetTarget.extraInfo" : loadedWidgetList.index
				};
			}
			ajaxManager.runJsonExt({
				url			: "/dashboard/loadAdcMonitoringChart.action",
				data		: params,
				successFn	: function(data) 
				{
					if (chartRefreshKey == 1)
					{
						var chartobj = widgetChart.GenerateAdcChangeHistoryChart(data, chartName, chartRefreshKey);
						var loadedWidgetindex = loadedWidgetList.index;
						G_refershChartObj[loadedWidgetindex] = chartobj;
						widgetLoadCount++;
						onLoadWidget(widgetList);
					}
					if (chartRefreshKey == 2)
					{
						var loadedWidgetindex = loadedWidgetList.index;
						var chartObj = chartName[loadedWidgetindex];
						widgetChart.GenerateAdcChangeHistoryChart(data, chartObj, chartRefreshKey);
						widgetLoadCount++;
						refreshDashboardWidget(widgetList, chartName);
					}
					if (chartRefreshKey == 3)
					{
						var loadedWidgetindex = loadedWidgetList.index;
						var chartObj = chartName[loadedWidgetindex];
						widgetChart.GenerateAdcChangeHistoryChart(data, chartObj, chartRefreshKey);						
					}
				},
				completeFn	: function(data)
				{					
				}
			});
		}
		return true;
	},	
	// AdcChangeHistory Chart Data Get *WidgetAdd*
	AddAdcChangeHistoryData : function(chartName, addedwidgetIndex)
	{
		with (this)
		{
			var params = {
					"widgetTarget.index" : $('.portlet .targetIndex').val()
			};
			ajaxManager.runJsonExt({
				url : "/dashboard/loadAdcMonitoringChart.action",
				data : params,
				successFn : function(data)
				{
					var chartobj = widgetChart.GenerateAdcChangeHistoryChart(data, chartName, 1);
					G_refershChartObj[addedwidgetIndex] = chartobj;
					widgetAddCount++;
				},
				completeFn : function(data)
				{
					
				},
				errorFn : function(a,b,c)
				{
					exceptionEvent();
				}
			});
		}
		return true;
	},
	// AllAdcTrafficChart Data Get *WidgetLoad*
	LoadAllAdcTrafficChartData : function(chartName, widgetList, loadedWidgetList, chartRefreshKey)
	{
		with (this)
		{
			var params = undefined;
			if (chartRefreshKey == 1)
			{
				params = {
					"widgetTarget.index" : $('.portlet .targetIndex').val(),
					"widgetTarget.extraInfo" : loadedWidgetList.index
				};
			}
			else
			{
				params = {
					"widgetTarget.index" : loadedWidgetList.targetObj.index,
					"widgetTarget.extraInfo" : loadedWidgetList.index
				};
			}
			ajaxManager.runJsonExt({
				url			: "/dashboard/loadAllAdcTrafficInfo.action",
				data		: params,
				successFn	: function(data) 
				{
					if (chartRefreshKey == 1)
					{
						var chartobj = widgetChart.GenerateAllAdcTrafficChart(data, chartName, chartRefreshKey, intervalMonitor);
						var loadedWidgetindex = loadedWidgetList.index;
						G_refershChartObj[loadedWidgetindex] = chartobj;
						widgetLoadCount++;
						onLoadWidget(widgetList);
					}
					if (chartRefreshKey == 2)
					{
						var loadedWidgetindex = loadedWidgetList.index;
						var chartObj = chartName[loadedWidgetindex];
//						console.log("AllTraffic ======== loadedWidgetindex : ", loadedWidgetindex, ", chartObj : ", chartObj);
						widgetChart.GenerateAllAdcTrafficChart(data, chartObj, chartRefreshKey, intervalMonitor);
						widgetLoadCount++;
						refreshDashboardWidget(widgetList, chartName);
					}
					if (chartRefreshKey == 3)
					{
						var loadedWidgetindex = loadedWidgetList.index;
						var chartObj = chartName[loadedWidgetindex];
						widgetChart.GenerateAllAdcTrafficChart(data, chartObj, chartRefreshKey, intervalMonitor);						
					}
				},
				completeFn	: function(data)
				{					
				}
			});
		}
		return true;
	},
	// AllAdcTrafficChart Data Get *WidgetAdd*
	AddAllAdcTrafficChartData : function(chartName, addedwidgetIndex)
	{
		with (this)
		{			
			var params = {
				"widgetTarget.index" : $('.portlet .targetIndex').val()				
			};
			ajaxManager.runJsonExt({
				url			: "/dashboard/loadAllAdcTrafficInfo.action",
				data		: params,
				successFn	: function(data) 
				{			
					var chartobj = widgetChart.GenerateAllAdcTrafficChart(data, chartName, 1, intervalMonitor);
					G_refershChartObj[addedwidgetIndex] = chartobj;
					widgetAddCount++;					
				},
				completeFn	: function(data)
				{					
				}
			});
		}
		return true;
	},
	// VsStatusChartData Data Get *WidgetLoad*
	LoadVsStatusChartData : function(chartName, widgetList, loadedWidgetList, chartRefreshKey)
	{
		with (this)
		{
			var params = undefined;
			if (chartRefreshKey == 1)
			{
				params = {
					"widgetTarget.index" : $('.portlet .targetIndex').val(),
					"widgetTarget.extraInfo" : loadedWidgetList.index
				};
			}
			else
			{
				params = {
					"widgetTarget.index" : loadedWidgetList.targetObj.index,
					"widgetTarget.extraInfo" : loadedWidgetList.index
				};
			}
			ajaxManager.runJsonExt({
				url			: "/dashboard/loadVsStatusDataInfo.action",
				data		: params,
				successFn	: function(data) 
				{
					if (chartRefreshKey == 1)
					{
						var chartobj = widgetChart.GenerateVsStatusChart(data, chartName, chartRefreshKey, intervalMonitor);
						var loadedWidgetindex = loadedWidgetList.index;
						G_refershChartObj[loadedWidgetindex] = chartobj;
						widgetLoadCount++;
						onLoadWidget(widgetList);
					}
					if (chartRefreshKey == 2)
					{
						var loadedWidgetindex = loadedWidgetList.index;
						var chartObj = chartName[loadedWidgetindex];
						widgetChart.GenerateVsStatusChart(data, chartObj, chartRefreshKey, intervalMonitor);
						widgetLoadCount++;
						refreshDashboardWidget(widgetList, chartName);
					}
					if (chartRefreshKey == 3)
					{
						var loadedWidgetindex = loadedWidgetList.index;
						var chartObj = chartName[loadedWidgetindex];
						widgetChart.GenerateVsStatusChart(data, chartObj, chartRefreshKey, intervalMonitor);						
					}	
				},
				completeFn	: function(data)
				{					
				}
			});
		}
		return true;
	},
	// VsStatusChartData Data Get *WidgetAdd*
	AddVsStatusChartData : function(chartName, addedwidgetIndex)
	{
		with (this)
		{			
			var params = {
				"widgetTarget.index" : $('.portlet .targetIndex').val()				
			};
			ajaxManager.runJsonExt({
				url			: "/dashboard/loadVsStatusDataInfo.action",
				data		: params,
				successFn	: function(data) 
				{					
					var chartobj = widgetChart.GenerateVsStatusChart(data, chartName, 1, intervalMonitor);
					G_refershChartObj[addedwidgetIndex] = chartobj;
					widgetAddCount++;					
				},
				completeFn	: function(data)
				{					
				}
			});
		}
		return true;
	},
	// LoadCpuSPChartData Data Get *WidgetLoad*
	LoadCpuSPChartData : function(chartName, widgetList, loadedWidgetList, chartRefreshKey)
	{
		with (this)
		{
			var params = undefined;
			if (chartRefreshKey == 1)
			{
				params = {
						"widgetTarget.category" : $('.portlet .targetCategory').val(),
						"widgetTarget.index" : $('.portlet .targetIndex').val(),
						"widgetTarget.strIndex" : $('.portlet .targetStrIndex').val(),
						"widgetTarget.extraInfo" : loadedWidgetList.index
				};
			}
			else
			{
				params = {
						"widgetTarget.category" : loadedWidgetList.targetObj.category,
						"widgetTarget.index" : loadedWidgetList.targetObj.index,
						"widgetTarget.strIndex" : loadedWidgetList.targetObj.strIndex,
						"widgetTarget.extraInfo" : loadedWidgetList.index
				};
			}
			ajaxManager.runJsonExt({
				url			: "/dashboard/loadCpuSpConnectionInfo.action",
				data		: params,
				successFn	: function(data) 
				{
					if (chartRefreshKey == 1)
					{
						var chartobj = widgetChart.GenerateCpuSPChart(data, chartName, chartRefreshKey);
						var loadedWidgetindex = loadedWidgetList.index;
						G_refershChartObj[loadedWidgetindex] = chartobj;
						widgetLoadCount++;
						onLoadWidget(widgetList);
					}
					if (chartRefreshKey == 2)
					{
						var loadedWidgetindex = loadedWidgetList.index;
						var chartObj = chartName[loadedWidgetindex];
//						console.log("SPCPU ======== loadedWidgetindex : ", loadedWidgetindex, ", chartObj : ", chartObj);
						widgetChart.GenerateCpuSPChart(data, chartObj, chartRefreshKey);
						widgetLoadCount++;
						refreshDashboardWidget(widgetList, chartName);
					}
					if (chartRefreshKey == 3)
					{
						var loadedWidgetindex = loadedWidgetList.index;
						var chartObj = chartName[loadedWidgetindex];
						widgetChart.GenerateCpuSPChart(data, chartObj, chartRefreshKey);						
					}	
				},
				completeFn	: function(data)
				{					
				}
			});
		}
		return true;
	},
	// cpuSPChartData Data Get *WidgetAdd*
	AddCpuSPChartData : function(chartName, addedwidgetIndex)
	{
		with (this)
		{			
			var params = {
					"widgetTarget.category" : $('.portlet .targetCategory').val(),
					"widgetTarget.index" : $('.portlet .targetIndex').val(),
					"widgetTarget.strIndex" : $('.portlet .targetStrIndex').val()		
			};
			ajaxManager.runJsonExt({
				url			: "/dashboard/loadCpuSpConnectionInfo.action",
				data		: params,
				successFn	: function(data) 
				{					
					var chartobj = widgetChart.GenerateCpuSPChart(data, chartName, 1, intervalMonitor);
					G_refershChartObj[addedwidgetIndex] = chartobj;
					widgetAddCount++;					
				},
				completeFn	: function(data)
				{					
				}
			});
		}
		return true;
	},
	// ADC Concurrent session 추이 Chart Data Get *WidgetLoad*
	LoadAdcConnectionHistoryData : function(chartName, widgetList, loadedWidgetList, chartRefreshKey)
	{
		with (this)
		{
			var params = undefined;
			if (chartRefreshKey == 1)
			{
				params = {
					"widgetTarget.category" : $('.portlet .targetCategory').val(),
					"widgetTarget.index" : $('.portlet .targetIndex').val(),
					"widgetTarget.strIndex" : $('.portlet .targetStrIndex').val(),
					"widgetTarget.extraInfo" : loadedWidgetList.index
				};
			}
			else
			{
				params = {
						"widgetTarget.category" : loadedWidgetList.targetObj.category,
						"widgetTarget.index" : loadedWidgetList.targetObj.index,
						"widgetTarget.strIndex" : loadedWidgetList.targetObj.strIndex,
						"widgetTarget.extraInfo" : loadedWidgetList.index
				};
			}
			ajaxManager.runJsonExt({
				url			: "/dashboard/loadAdcConcurrentSessionInfo.action",
				data		: params,
				successFn	: function(data) 
				{										
					if (chartRefreshKey == 1)
					{
						var chartobj = undefined;
						if(data.widgetTarget.category == 7)
						{							
//							if($('.portlet .moreInfoIndex').val() == 0)
							if(loadedWidgetList.moreInfoIndex == 0)
								chartobj = widgetChart.GenerateServiceGroupConnectionChartData(data, chartName, chartRefreshKey, intervalMonitor);
							else
								chartobj = widgetChart.GenerateServiceGroupConnectionSumChartData(data, chartName, chartRefreshKey, intervalMonitor);						
						}
						else
						{
							chartobj = widgetChart.GenerateAdcConnectionChartData(data, chartName, chartRefreshKey, intervalMonitor);
						}						
						var loadedWidgetindex = loadedWidgetList.index;
						G_refershChartObj[loadedWidgetindex] = chartobj;
						widgetLoadCount++;
						onLoadWidget(widgetList);
					}
					if (chartRefreshKey == 2)
					{
						var loadedWidgetindex = loadedWidgetList.index;
						var chartObj = chartName[loadedWidgetindex];
						if(data.widgetTarget.category == 7)
						{
//							if($('.portlet .moreInfoIndex').val() == 0)		
							if(loadedWidgetList.moreInfoIndex == 0)
//								chartobj = widgetChart.GenerateServiceGroupConnectionChartData(data, chartObj.div.id, 2);
								widgetChart.GenerateServiceGroupConnectionChartData(data, chartObj, chartRefreshKey, intervalMonitor);		
							else
//								chartobj = widgetChart.GenerateServiceGroupConnectionSumChartData(data, chartObj.div.id, 2);
								widgetChart.GenerateServiceGroupConnectionSumChartData(data, chartObj, chartRefreshKey, intervalMonitor);
							
//							var loadedWidgetindex = loadedWidgetList.index;
//							G_refershChartObj[loadedWidgetindex] = chartobj;
						}
						else
						{
							widgetChart.GenerateAdcConnectionChartData(data, chartObj, chartRefreshKey, intervalMonitor);
						}						
						widgetLoadCount++;
						refreshDashboardWidget(widgetList, chartName);
					}
					if (chartRefreshKey == 3)
					{
						var loadedWidgetindex = loadedWidgetList.index;
						var chartObj = chartName[loadedWidgetindex];
						if(data.widgetTarget.category == 7)
						{
							if(loadedWidgetList.moreInfoIndex == 0)
//								chartobj = widgetChart.GenerateServiceGroupConnectionChartData(data, chartObj.div.id, 3);					
								widgetChart.GenerateServiceGroupConnectionChartData(data, chartObj, chartRefreshKey, intervalMonitor);
							else
//								chartobj = widgetChart.GenerateServiceGroupConnectionSumChartData(data, chartObj.div.id, 3);
								widgetChart.GenerateServiceGroupConnectionSumChartData(data, chartObj, chartRefreshKey, intervalMonitor);
						}
						else
						{
//							chartobj = widgetChart.GenerateAdcConnectionChartData(data, chartObj.div.id, 3);
							widgetChart.GenerateAdcConnectionChartData(data, chartObj, 3, intervalMonitor);
						}
//						var loadedWidgetindex = loadedWidgetList.index;
//						G_refershChartObj[loadedWidgetindex] = chartobj;
					}
				},
				completeFn	: function(data)
				{					
				}
			});
		}
		return true;
	},	
	// ADC Concurrent session 추이 Chart Data Get *WidgetAdd*
	AddAdcConnectionHistoryData : function(chartName, addedwidgetIndex)
	{
		with (this)
		{
			var params = {
					"widgetTarget.category" : $('.portlet .targetCategory').val(),
					"widgetTarget.index" : $('.portlet .targetIndex').val(),
					"widgetTarget.strIndex" : $('.portlet .targetStrIndex').val()
			};
			ajaxManager.runJsonExt({
				url : "/dashboard/loadAdcConcurrentSessionInfo.action",
				data : params,
				successFn : function(data)
				{
					var chartobj = undefined;
					if(data.widgetTarget.category == 7)
					{
						if($('.pop_type_wrap .selected_chart').filter(':last').text().trim() == 0)							
							chartobj = widgetChart.GenerateServiceGroupConnectionChartData(data, chartName, 1, intervalMonitor);					
						else
							chartobj = widgetChart.GenerateServiceGroupConnectionSumChartData(data, chartName, 1, intervalMonitor);												
					}
					else
					{
						chartobj = widgetChart.GenerateAdcConnectionChartData(data, chartName, 1, intervalMonitor);
					}					
					G_refershChartObj[addedwidgetIndex] = chartobj;
					widgetAddCount++;
				},
				completeFn : function(data)
				{
					
				},
				errorFn : function(a,b,c)
				{
					exceptionEvent();
				}
			});
		}
		return true;
	},
	// ADC Throughtput 추이 Chart Data Get *WidgetLoad*
	LoadAdcThroughtputHistoryData : function(chartName, widgetList, loadedWidgetList, chartRefreshKey)
	{
		with (this)
		{
			var params = undefined;
			if (chartRefreshKey == 1)
			{
				params = {
					"widgetTarget.category" : $('.portlet .targetCategory').val(),
					"widgetTarget.index" : $('.portlet .targetIndex').val(),
					"widgetTarget.strIndex" : $('.portlet .targetStrIndex').val(),
					"widgetTarget.extraInfo" : loadedWidgetList.index
				};
			}
			else
			{
				params = {
					"widgetTarget.category" : loadedWidgetList.targetObj.category,
					"widgetTarget.index" : loadedWidgetList.targetObj.index,
					"widgetTarget.strIndex" : loadedWidgetList.targetObj.strIndex,
					"widgetTarget.extraInfo" : loadedWidgetList.index
				};
			}
			ajaxManager.runJsonExt({
				url			: "/dashboard/loadAdcThroughputInfo.action",
				data		: params,
				successFn	: function(data) 
				{
					if (chartRefreshKey == 1)
					{
						var chartobj = undefined;
						if(data.widgetTarget.category == 7)
						{
//							if($('.portlet .moreInfoIndex').val() == 0)
							if(loadedWidgetList.moreInfoIndex == 0)
								chartobj = widgetChart.GeneratedcServiceGroupThroughtputChartData(data, chartName, chartRefreshKey, intervalMonitor);						
							else
								chartobj = widgetChart.GeneratedcServiceGroupThroughtputSumChartData(data, chartName, chartRefreshKey, intervalMonitor);						
						}
						else
						{
							chartobj = widgetChart.GeneratedcThroughtputChartData(data, chartName, chartRefreshKey, intervalMonitor);
						}
						var loadedWidgetindex = loadedWidgetList.index;
						G_refershChartObj[loadedWidgetindex] = chartobj;
						widgetLoadCount++;
						onLoadWidget(widgetList);
					}
					if (chartRefreshKey == 2)
					{
						var loadedWidgetindex = loadedWidgetList.index;
						var chartObj = chartName[loadedWidgetindex];
						if(data.widgetTarget.category == 7)
						{
//							if($('.portlet .moreInfoIndex').val() == 0)
							if(loadedWidgetList.moreInfoIndex == 0)
								widgetChart.GeneratedcServiceGroupThroughtputChartData(data, chartObj, chartRefreshKey, intervalMonitor);						
							else
								widgetChart.GeneratedcServiceGroupThroughtputSumChartData(data, chartObj, chartRefreshKey, intervalMonitor);							
						}
						else
						{
							widgetChart.GeneratedcThroughtputChartData(data, chartObj, chartRefreshKey, intervalMonitor);
						}						
						widgetLoadCount++;
						refreshDashboardWidget(widgetList, chartName);
					}
					if (chartRefreshKey == 3)
					{
						var loadedWidgetindex = loadedWidgetList.index;
						var chartObj = chartName[loadedWidgetindex];
						if(data.widgetTarget.category == 7)
						{
//							if($('.portlet .moreInfoIndex').val() == 0)							
							if(loadedWidgetList.moreInfoIndex == 0)
								widgetChart.GeneratedcServiceGroupThroughtputChartData(data, chartObj, chartRefreshKey, intervalMonitor);						
							else
								widgetChart.GeneratedcServiceGroupThroughtputSumChartData(data, chartObj, chartRefreshKey, intervalMonitor);							
						}
						else
						{
							widgetChart.GeneratedcThroughtputChartData(data, chartObj.div.id, 3, intervalMonitor);
						}
					}
				},
				completeFn	: function(data)
				{					
				}
			});
		}
		return true;
	},	
	// ADC Throughtput 추이 Chart Data Get *WidgetAdd*
	AddAdcThroughtputHistoryData : function(chartName, addedwidgetIndex)
	{
		with (this)
		{
			var params = {
					"widgetTarget.category" : $('.portlet .targetCategory').val(),
					"widgetTarget.index" : $('.portlet .targetIndex').val(),
					"widgetTarget.strIndex" : $('.portlet .targetStrIndex').val()
			};
			ajaxManager.runJsonExt({
				url : "/dashboard/loadAdcThroughputInfo.action",
				data : params,
				successFn : function(data)
				{
					var chartobj = undefined;
					if(data.widgetTarget.category == 7)
					{							
						if($('.pop_type_wrap .selected_chart').filter(':last').text().trim() == 0)
							chartobj = widgetChart.GeneratedcServiceGroupThroughtputChartData(data, chartName, 1, intervalMonitor);						
						else
							chartobj = widgetChart.GeneratedcServiceGroupThroughtputSumChartData(data, chartName, 1, intervalMonitor);
					}
					else
					{
						chartobj = widgetChart.GeneratedcThroughtputChartData(data, chartName, 1, intervalMonitor);
					}
					G_refershChartObj[addedwidgetIndex] = chartobj;
					widgetAddCount++;
				},
				completeFn : function(data)
				{
					
				},
				errorFn : function(a,b,c)
				{
					exceptionEvent();
				}
			});
		}
		return true;
	},
	// ResponseTimeChartData Data Get *WidgetLoad*
	LoadResponseTimeChartData : function(chartName, widgetList, loadedWidgetList, chartRefreshKey)
	{
		with (this)
		{
			var params = undefined;
			if (chartRefreshKey == 1)
			{
				params = {
					"widgetTarget.category" : $('.portlet .targetCategory').val(),
					"widgetTarget.index" : $('.portlet .targetIndex').val(),
					"widgetTarget.strIndex" : $('.portlet .targetStrIndex').val(),
					"widgetTarget.extraInfo" : loadedWidgetList.index
				};
			}
			else
			{
				params = {
						"widgetTarget.category" : loadedWidgetList.targetObj.category,
						"widgetTarget.index" : $('.portlet .targetIndex').val(),
						"widgetTarget.strIndex" : loadedWidgetList.targetObj.strIndex,
						"widgetTarget.extraInfo" : loadedWidgetList.index
				};
			}
			ajaxManager.runJsonExt({
				url			: "/dashboard/loadResponseTimeHistoryInfo.action",
				data		: params,
				successFn	: function(data) 
				{
					if (chartRefreshKey == 1)
					{
						var chartobj = undefined;
						if(data.widgetTarget.category == 7)
						{
//							if($('.portlet .moreInfoIndex').val() == 0)	
							if(loadedWidgetList.moreInfoIndex == 0)
								chartobj = widgetChart.GenerateSvcGroupResponseTimeHistoryChart(data, chartName, chartRefreshKey, intervalMonitor);				
							else
								chartobj = widgetChart.GenerateSvcGroupResponseTimeHistorySumChart(data, chartName, chartRefreshKey, intervalMonitor);													
						}
						else
						{
							chartobj = widgetChart.GenerateResponseTimeHistoryChart(data, chartName, chartRefreshKey, intervalMonitor);
						}
						var loadedWidgetindex = loadedWidgetList.index;
						G_refershChartObj[loadedWidgetindex] = chartobj;
						widgetLoadCount++;
						onLoadWidget(widgetList);
					}
					if (chartRefreshKey == 2)
					{
						var loadedWidgetindex = loadedWidgetList.index;
						var chartObj = chartName[loadedWidgetindex];
						if(data.widgetTarget.category == 7)
						{
//							if($('.portlet .moreInfoIndex').val() == 0)							
							if(loadedWidgetList.moreInfoIndex == 0)
								widgetChart.GenerateSvcGroupResponseTimeHistoryChart(data, chartObj, chartRefreshKey, intervalMonitor);				
							else
								widgetChart.GenerateSvcGroupResponseTimeHistorySumChart(data, chartObj, chartRefreshKey, intervalMonitor);
						}
						else
						{
							widgetChart.GenerateResponseTimeHistoryChart(data, chartObj, chartRefreshKey, intervalMonitor);
						}
						
						widgetLoadCount++;
						refreshDashboardWidget(widgetList, chartName);
					}
					if (chartRefreshKey == 3)
					{
						var loadedWidgetindex = loadedWidgetList.index;
						var chartObj = chartName[loadedWidgetindex];
						if(data.widgetTarget.category == 7)
						{
							if(loadedWidgetList.moreInfoIndex == 0)
								widgetChart.GenerateSvcGroupResponseTimeHistoryChart(data, chartObj, chartRefreshKey, intervalMonitor);				
							else
								widgetChart.GenerateSvcGroupResponseTimeHistorySumChart(data, chartObj, chartRefreshKey, intervalMonitor);						
						}
						else
						{
							widgetChart.GenerateResponseTimeHistoryChart(data, chartObj, chartRefreshKey, intervalMonitor);	
						}											
					}
				},
				completeFn	: function(data)
				{					
				}
			});
		}
		return true;
	},
	// ResponseTimeChartData Data Get *WidgetAdd*
	AddResponseTimeChartData : function(chartName, addedwidgetIndex)
	{
		with (this)
		{	
			var params = {
				"widgetTarget.category" : $('.portlet .targetCategory').val(),
				"widgetTarget.index" : $('.portlet .targetIndex').val(),
				"widgetTarget.strIndex" : $('.portlet .targetStrIndex').val()
				
			};
			ajaxManager.runJsonExt({
				url			: "/dashboard/loadResponseTimeHistoryInfo.action",
				data		: params,
				successFn	: function(data) 
				{
					var chartobj = undefined;
					if(data.widgetTarget.category == 7)
					{
						if($('.pop_type_wrap .selected_chart').filter(':last').text().trim() == 0)
							chartobj = widgetChart.GenerateSvcGroupResponseTimeHistoryChart(data, chartName, 1, intervalMonitor);				
						else
							chartobj = widgetChart.GenerateSvcGroupResponseTimeHistorySumChart(data, chartName, 1, intervalMonitor);
					}
					else
					{
						chartobj = widgetChart.GenerateResponseTimeHistoryChart(data, chartName, 1, intervalMonitor);
					}					
					G_refershChartObj[addedwidgetIndex] = chartobj;
					widgetAddCount++;					
				},
				completeFn	: function(data)
				{
				}
			});
		}
		return true;
	},
	// ADC Concurrent session Detail 추이 Chart Data Get *WidgetLoad*
	LoadConcurrentSessionDetailData : function(chartName, widgetList, loadedWidgetList, chartRefreshKey)
	{
		with (this)
		{
			var params = undefined;
			if (chartRefreshKey == 1)
			{
				params = {
					"widgetTarget.category" : $('.portlet .targetCategory').val(),
					"widgetTarget.index" : $('.portlet .targetIndex').val(),
					"widgetTarget.strIndex" : $('.portlet .targetStrIndex').val(),
					"widgetTarget.extraInfo" : loadedWidgetList.index
				};
			}
			else
			{
				params = {
						"widgetTarget.category" : loadedWidgetList.targetObj.category,
						"widgetTarget.index" : loadedWidgetList.targetObj.index,
						"widgetTarget.strIndex" : loadedWidgetList.targetObj.strIndex,
						"widgetTarget.extraInfo" : loadedWidgetList.index
				};
			}
			ajaxManager.runJsonExt({
				url			: "/dashboard/loadConcurrentSessionDetailInfo.action",
				data		: params,
				successFn	: function(data) 
				{
					if (chartRefreshKey == 1)
					{
						var chartobj = widgetChart.GenerateConcurrentSessionDetailChart(data, chartName, chartRefreshKey, intervalMonitor);
						var loadedWidgetindex = loadedWidgetList.index;
						G_refershChartObj[loadedWidgetindex] = chartobj;
						widgetLoadCount++;
						onLoadWidget(widgetList);
					}
					if (chartRefreshKey == 2)
					{
						var loadedWidgetindex = loadedWidgetList.index;
						var chartObj = chartName[loadedWidgetindex];
						widgetChart.GenerateConcurrentSessionDetailChart(data, chartObj, chartRefreshKey, intervalMonitor);
						widgetLoadCount++;
						refreshDashboardWidget(widgetList, chartName);
					}
					if (chartRefreshKey == 3)
					{
						var loadedWidgetindex = loadedWidgetList.index;
						var chartObj = chartName[loadedWidgetindex];
						widgetChart.GenerateConcurrentSessionDetailChart(data, chartObj, chartRefreshKey, intervalMonitor);						
					}
				},
				completeFn	: function(data)
				{					
				}
			});
		}
		return true;
	},	
	// ADC Concurrent session Detail 추이 Chart Data Get *WidgetAdd*
	AddConcurrentSessionDetailData : function(chartName, addedwidgetIndex, name)
	{
		with (this)
		{	
			var params = {
				"widgetTarget.category" : $('.portlet .targetCategory').val(),
				"widgetTarget.index" : $('.portlet .targetIndex').val(),
				"widgetTarget.strIndex" : $('.portlet .targetStrIndex').val()
			};
			ajaxManager.runJsonExt({
				url			: "/dashboard/loadConcurrentSessionDetailInfo.action",
				data		: params,
				successFn	: function(data) 
				{					
					var chartobj = widgetChart.GenerateConcurrentSessionDetailChart(data, chartName, 1, name, intervalMonitor);
					G_refershChartObj[addedwidgetIndex] = chartobj;
					widgetAddCount++;					
				},
				completeFn	: function(data)
				{
				}
			});
		}
		return true;
	},
	// ADC Concurrent session FLB 추이 Chart Data Get *WidgetLoad*
	LoadConcurrentSessionFlbData : function(chartName, widgetList, loadedWidgetList, chartRefreshKey)
	{
		with (this)
		{
			var params = undefined;
			if (chartRefreshKey == 1)
			{
				params = {
					"widgetTarget.category" : $('.portlet .targetCategory').val(),
					"widgetTarget.index" : $('.portlet .targetIndex').val(),
					"widgetTarget.strIndex" : $('.portlet .targetStrIndex').val(),
					"widgetTarget.extraInfo" : loadedWidgetList.index
				};
			}
			else
			{
				params = {
						"widgetTarget.category" : loadedWidgetList.targetObj.category,
						"widgetTarget.index" : loadedWidgetList.targetObj.index,
						"widgetTarget.strIndex" : loadedWidgetList.targetObj.strIndex,
						"widgetTarget.extraInfo" : loadedWidgetList.index
				};
			}
			ajaxManager.runJsonExt({
				url			: "/dashboard/loadConcurrentSessionflbInfo.action",
				data		: params,
				successFn	: function(data) 
				{
					if (chartRefreshKey == 1)
					{
						var chartobj = widgetChart.GenerateConcurrentSessionFLBChart(data, chartName, chartRefreshKey, intervalMonitor);
						var loadedWidgetindex = loadedWidgetList.index;
						G_refershChartObj[loadedWidgetindex] = chartobj;
						widgetLoadCount++;
						onLoadWidget(widgetList);
					}
					if (chartRefreshKey == 2)
					{
						var loadedWidgetindex = loadedWidgetList.index;
						var chartObj = chartName[loadedWidgetindex];
						widgetChart.GenerateConcurrentSessionFLBChart(data, chartObj, chartRefreshKey, intervalMonitor);
						widgetLoadCount++;
						refreshDashboardWidget(widgetList, chartName);
					}
					if (chartRefreshKey == 3)
					{
						var loadedWidgetindex = loadedWidgetList.index;
						var chartObj = chartName[loadedWidgetindex];
						widgetChart.GenerateConcurrentSessionFLBChart(data, chartObj, chartRefreshKey, intervalMonitor);						
					}
				},
				completeFn	: function(data)
				{					
				}
			});
		}
		return true;
	},	
	// ADC Concurrent session FLB 추이 Chart Data Get *WidgetAdd*
	AddConcurrentSessionFlbData : function(chartName, addedwidgetIndex, name)
	{
		with (this)
		{	
			var params = {
				"widgetTarget.category" : $('.portlet .targetCategory').val(),
				"widgetTarget.index" : $('.portlet .targetIndex').val(),
				"widgetTarget.strIndex" : $('.portlet .targetStrIndex').val()
			};
			ajaxManager.runJsonExt({
				url			: "/dashboard/loadConcurrentSessionflbInfo.action",
				data		: params,
				successFn	: function(data) 
				{					
					var chartobj = widgetChart.GenerateConcurrentSessionFLBChart(data, chartName, 1, name, intervalMonitor);
					G_refershChartObj[addedwidgetIndex] = chartobj;
					widgetAddCount++;					
				},
				completeFn	: function(data)
				{
				}
			});
		}
		return true;
	},	
	
	AddadcApplianceMonitorWidget : function(widgetData)
	{
		with (this)
		{
			var SelectAdcModelNum = "";
			var SelectAdcType = "";
			var SelectAdcVersion = "";
	
			var params = 
			{
				"widgetTarget.index" 	: widgetData["widgetTarget.index"],				
				"widgetTarget.category"	: widgetData["widgetTarget.category"]
			};			
			ajaxManager.runJsonExt({
				url : "/dashboard/loadSelectedAdcInfo.action",
				data : params,								
				successFn : function(data)
				{
					SelectAdcModelNum = data.widgetTarget.extraInfo;
					SelectAdcType = data.widgetTarget.vendor;
					SelectAdcVersion = data.widgetTarget.strIndex;
					if (SelectAdcType == 2)	// Alteon
					{
						if (SelectAdcModelNum == 2208)
						{
							dashUrl = "/dashboard/Alteon2208Content.action";
						}
						else if (SelectAdcModelNum == 2216)
						{
							dashUrl = "/dashboard/Alteon2216Content.action";
						}
						else if (SelectAdcModelNum == 2424)
						{
							dashUrl = "/dashboard/Alteon2424Content.action";
						}
						else if (SelectAdcModelNum == 3408)
						{
							dashUrl = "/dashboard/Alteon3408Content.action";
						}
						else if (SelectAdcModelNum == 4408)
						{
							dashUrl = "/dashboard/Alteon4408Content.action";
						}
						else if (SelectAdcModelNum == 4024)
						{
							dashUrl = "/dashboard/Alteon4024Content.action";
						}
						else if (SelectAdcModelNum == 4416)
						{
							dashUrl = "/dashboard/Alteon4416Content.action";
						}
						else if (SelectAdcModelNum == 5224)
						{
							dashUrl = "/dashboard/Alteon5224Content.action";
						}
						else if (SelectAdcModelNum == 5412)
						{
							dashUrl = "/dashboard/Alteon5412Content.action";
						}
						else if (SelectAdcModelNum == 6420 || SelectAdcModelNum == 6421)
						{
							dashUrl = "/dashboard/Alteon6420Content.action";
						}
						else if (SelectAdcModelNum == 6024)
						{
							dashUrl = "/dashboard/Alteon6024Content.action";
						}
						else
						{
							dashUrl = "/dashboard/AlteonBasicContent.action";
						}
					}
					else if (SelectAdcType == 1)	// F5
					{
						if (SelectAdcModelNum == 1600)
						{
							dashUrl = "/dashboard/F5_1600Content.action";
						}
						else if ((SelectAdcModelNum == 2000) || (SelectAdcModelNum == 2200))
						{
							dashUrl = "/dashboard/F5_2000Content.action";
						}
						else if (SelectAdcModelNum == 2400)
						{
							dashUrl = "/dashboard/F5_2400Content.action";
						}
						else if (SelectAdcModelNum == 2600)
						{
							dashUrl = "/dashboard/F5_2600Content.action";
						}
						else if (SelectAdcModelNum == 2800)
						{
							dashUrl = "/dashboard/F5_2800Content.action";
						}
						else if (SelectAdcModelNum == 3600)
						{
							dashUrl = "/dashboard/F5_3600Content.action";
						}
						else if (SelectAdcModelNum == 3900)
						{
							dashUrl = "/dashboard/F5_3900Content.action";
						}
						else if ((SelectAdcModelNum == 4000) || (SelectAdcModelNum == 4200))
						{
							dashUrl = "/dashboard/F5_4000Content.action";
						}
						else if (SelectAdcModelNum == 4600)
						{
							dashUrl = "/dashboard/F5_4600Content.action";
						}
						else if (SelectAdcModelNum == 4800)
						{
							dashUrl = "/dashboard/F5_4800Content.action";
						}
						else if (SelectAdcModelNum == 5600)
						{
							dashUrl = "/dashboard/F5_5600Content.action";
						}
						else if (SelectAdcModelNum == 5800)
						{
							dashUrl = "/dashboard/F5_5800Content.action";
						}
						else if (SelectAdcModelNum == 6400)
						{
							dashUrl = "/dashboard/F5_6400Content.action";
						}
						else if (SelectAdcModelNum == 6900)
						{
							dashUrl = "/dashboard/F5_6900Content.action";
						}
						else if ((SelectAdcModelNum == 5000) || (SelectAdcModelNum == 5200) || (SelectAdcModelNum == 7000) || (SelectAdcModelNum == 7200))
						{
							dashUrl = "/dashboard/F5_7000Content.action";
						}
						else if (SelectAdcModelNum == 7600)
						{
							dashUrl = "/dashboard/F5_7600Content.action";
						}
						else if (SelectAdcModelNum == 7800)
						{
							dashUrl = "/dashboard/F5_7800Content.action";
						}
						else if (SelectAdcModelNum == 8400)
						{
							dashUrl = "/dashboard/F5_8400Content.action";
						}
						else if (SelectAdcModelNum == 8900)
						{
							dashUrl = "/dashboard/F5_8900Content.action";
						}
						else if ((SelectAdcModelNum == 10000) || (SelectAdcModelNum == 10200))
						{
							dashUrl = "/dashboard/F5_10000Content.action";
						}
						else if (SelectAdcModelNum == 10600)
						{
							dashUrl = "/dashboard/F5_10600Content.action";
						}
						else if (SelectAdcModelNum == 10800)
						{
							dashUrl = "/dashboard/F5_10800Content.action";
						}
						else
						{
							dashUrl = "/dashboard/F5_BasicContent.action";
						}
					}
					ajaxManager.runHtmlExt({
						url : dashUrl,
						data: widgetData,				
						target: ".contents_area .widget_box",
						successFn : function(params)
						{
							// Min Size 적용, 기본 Widget Size 405보다 크다면, Widget고유의 Min Size 를 적용한다.
							$('.contents_area .widget_box .portlet').css("width", widgetData["widgetItem.widthMinSize"]+ "px");						
							$('.contents_area .widget_box .portlet').css("height", widgetData["widgetItem.heightMinSize"]+ "px");
							
							var widgetElemnet = '';
							widgetElemnet = $('.contents_area .widget_box .portlet');						
							$(widgetElemnet).appendTo('#col1');						
							$('#col1').sortable('refresh');						
							
							// Widget 을 ADD 할때 Widget 의 Event 및 Option 들을 적용한다. (Widget을 Add할때 필히 적용해야 합니다.)
							addWidgetEvent(widgetData["widgetInfo.index"], widgetData["widgetItem.widthMinSize"],
									widgetData["widgetItem.widthMaxSize"],widgetData["widgetItem.heightMinSize"],
									widgetData["widgetItem.heightMaxSize"]);
							$(".widget_table tbody tr:even").css("background","#f4f4f4");   // Table 위젯 짝수 bg 변경	
							
							// Widget Count 증가 (for 위젯 갯수 Limit)
							G_widgetCount ++;
							
							// 장비모니터링 OS Version Insert 부분
							var versionArea = $('.version').filter(':last');
							versionArea.empty();
							versionArea.append(SelectAdcVersion);	
						},
						errorFn : function(a,b,c)
						{
							exceptionEvent();
						}
					});
				},
				errorFn : function(a,b,c)
				{
					return false;
				}
			});
		}
	},
	ReloadadcApplianceMonitorWidget : function(widgetList, allwidgetList, ParamData)
	{
		with (this)
		{
			var SelectAdcModelNum = "";
			var SelectAdcType = "";
			var SelectAdcVersion = "";
	
			var params = 
			{
				"widgetTarget.index" 	: widgetList.targetObj.index,				
				"widgetTarget.category"	: widgetList.targetObj.category
			};			
			ajaxManager.runJsonExt({
				url : "/dashboard/loadSelectedAdcInfo.action",
				data : params,								
				successFn : function(data)
				{
					SelectAdcModelNum = 0;
					SelectAdcType = 1;
					if (data.widgetTarget.extraInfo != null)
					{
						SelectAdcModelNum = data.widgetTarget.extraInfo;
					}
					if (data.widgetTarget.vendor != null)
					{
						SelectAdcType = data.widgetTarget.vendor;
					}
					if (data.widgetTarget.strIndex != null)
					{
						SelectAdcVersion = data.widgetTarget.strIndex;
					}
					
					if (SelectAdcType == 2)	// Alteon
					{
						if (SelectAdcModelNum == 2208)
						{
							dashUrl = "/dashboard/Alteon2208Content.action";
						}
						else if (SelectAdcModelNum == 2216)
						{
							dashUrl = "/dashboard/Alteon2216Content.action";
						}
						else if (SelectAdcModelNum == 2424)
						{
							dashUrl = "/dashboard/Alteon2424Content.action";
						}
						else if (SelectAdcModelNum == 3408)
						{
							dashUrl = "/dashboard/Alteon3408Content.action";
						}
						else if (SelectAdcModelNum == 4408)
						{
							dashUrl = "/dashboard/Alteon4408Content.action";
						}
						else if (SelectAdcModelNum == 4024)
						{
							dashUrl = "/dashboard/Alteon4024Content.action";
						}
						else if (SelectAdcModelNum == 4416)
						{
							dashUrl = "/dashboard/Alteon4416Content.action";
						}
						else if (SelectAdcModelNum == 5224)
						{
							dashUrl = "/dashboard/Alteon5224Content.action";
						}
						else if (SelectAdcModelNum == 5412)
						{
							dashUrl = "/dashboard/Alteon5412Content.action";
						}
						else if (SelectAdcModelNum == 6420 || SelectAdcModelNum == 6421)
						{
							dashUrl = "/dashboard/Alteon6420Content.action";
						}
						else if (SelectAdcModelNum == 6024)
						{
							dashUrl = "/dashboard/Alteon6024Content.action";
						}
						else
						{
							dashUrl = "/dashboard/AlteonBasicContent.action";
						}
					}
					else if (SelectAdcType == 1)	// F5
					{
						if (SelectAdcModelNum == 1600)
						{
							dashUrl = "/dashboard/F5_1600Content.action";
						}
						else if ((SelectAdcModelNum == 2000) || (SelectAdcModelNum == 2200))
						{
							dashUrl = "/dashboard/F5_2000Content.action";
						}
						else if (SelectAdcModelNum == 2400)
						{
							dashUrl = "/dashboard/F5_2400Content.action";
						}
						else if (SelectAdcModelNum == 2600)
						{
							dashUrl = "/dashboard/F5_2600Content.action";
						}
						else if (SelectAdcModelNum == 2800)
						{
							dashUrl = "/dashboard/F5_2800Content.action";
						}
						else if (SelectAdcModelNum == 3600)
						{
							dashUrl = "/dashboard/F5_3600Content.action";
						}
						else if (SelectAdcModelNum == 3900)
						{
							dashUrl = "/dashboard/F5_3900Content.action";
						}
						else if ((SelectAdcModelNum == 4000) || (SelectAdcModelNum == 4200))
						{
							dashUrl = "/dashboard/F5_4000Content.action";
						}
						else if (SelectAdcModelNum == 4600)
						{
							dashUrl = "/dashboard/F5_4600Content.action";
						}
						else if (SelectAdcModelNum == 4800)
						{
							dashUrl = "/dashboard/F5_4800Content.action";
						}
						else if (SelectAdcModelNum == 5600)
						{
							dashUrl = "/dashboard/F5_5600Content.action";
						}
						else if (SelectAdcModelNum == 5800)
						{
							dashUrl = "/dashboard/F5_5800Content.action";
						}
						else if (SelectAdcModelNum == 6400)
						{
							dashUrl = "/dashboard/F5_6400Content.action";
						}
						else if (SelectAdcModelNum == 6900)
						{
							dashUrl = "/dashboard/F5_6900Content.action";
						}
						else if ((SelectAdcModelNum == 5000) || (SelectAdcModelNum == 5200) || (SelectAdcModelNum == 7000) || (SelectAdcModelNum == 7200))
						{
							dashUrl = "/dashboard/F5_7000Content.action";
						}
						else if (SelectAdcModelNum == 7600)
						{
							dashUrl = "/dashboard/F5_7600Content.action";
						}
						else if (SelectAdcModelNum == 7800)
						{
							dashUrl = "/dashboard/F5_7800Content.action";
						}
						else if (SelectAdcModelNum == 8400)
						{
							dashUrl = "/dashboard/F5_8400Content.action";
						}
						else if (SelectAdcModelNum == 8900)
						{
							dashUrl = "/dashboard/F5_8900Content.action";
						}
						else if ((SelectAdcModelNum == 10000) || (SelectAdcModelNum == 10200))
						{
							dashUrl = "/dashboard/F5_10000Content.action";
						}
						else if (SelectAdcModelNum == 10600)
						{
							dashUrl = "/dashboard/F5_10600Content.action";
						}
						else if (SelectAdcModelNum == 10800)
						{
							dashUrl = "/dashboard/F5_10800Content.action";
						}
						else
						{
							dashUrl = "/dashboard/F5_BasicContent.action";
						}
					}
					ajaxManager.runHtmlExt({
						url : dashUrl,
						data: ParamData,	
						target: ".contents_area .widget_box",
						successFn : function(params)
						{
							// Load 된 Widget의 사이즈 Data를 이용하여 Widget Resizing							
							var html = $('.contents_area .widget_box .portlet');
							html.css("width", widgetList.widthMin + "px");
							html.css("height", widgetList.heightMin + "px");
							
							var widgetElemnet = '';
							widgetElemnet = $('.contents_area .widget_box .portlet');
							$(widgetElemnet).appendTo('#col4');
							// Load된 Widget 에도 각각 하나씩 Event 와 Option을 부여한다.
							addWidgetEvent(widgetList.index,
									widgetList.widthMin, widgetList.widthMax,
									widgetList.heightMin, widgetList.heightMax);
							
							// 장비모니터링 OS Version Insert 부분
							var versionArea = $('.column #'+widgetList.index +' .version').filter(':last');
							versionArea.empty();
							versionArea.append(SelectAdcVersion);
							
							widgetLoadCount++;
							onLoadWidget(allwidgetList);					
						},
						errorFn : function(a,b,c)
						{
							exceptionEvent();
						}
					});
				},
				errorFn : function(a,b,c)
				{
					return false;
				}
			});
		}
	},
	RefreshadcApplianceMonitorWidget : function(widgetList, allwidgetList, ParamData, targetName, refershChartObj, modifyKey)
	{
		with (this)
		{
			var SelectAdcModelNum = "";
			var SelectAdcType = "";
			var SelectAdcVersion = "";
	
			var params = 
			{
				"widgetTarget.index" 	: widgetList.targetObj.index,				
				"widgetTarget.category"	: widgetList.targetObj.category
			};			
			ajaxManager.runJsonExt({
				url : "/dashboard/loadSelectedAdcInfo.action",
				data : params,								
				successFn : function(data)
				{
					SelectAdcModelNum = 0;
					SelectAdcType = 1;
					if (data.widgetTarget.extraInfo != null)
					{
						SelectAdcModelNum = data.widgetTarget.extraInfo;
					}
					if (data.widgetTarget.vendor != null)
					{
						SelectAdcType = data.widgetTarget.vendor;
					}
					if (data.widgetTarget.strIndex != null)
					{
						SelectAdcVersion = data.widgetTarget.strIndex;
					}
					
					if (SelectAdcType == 2)	// Alteon
					{
						if (SelectAdcModelNum == 2208)
						{
							dashUrl = "/dashboard/Alteon2208Content_Refresh.action";
						}
						else if (SelectAdcModelNum == 2216)
						{
							dashUrl = "/dashboard/Alteon2216Content_Refresh.action";
						}
						else if (SelectAdcModelNum == 2424)
						{
							dashUrl = "/dashboard/Alteon2424Content_Refresh.action";
						}
						else if (SelectAdcModelNum == 3408)
						{
							dashUrl = "/dashboard/Alteon3408Content_Refresh.action";
						}
						else if (SelectAdcModelNum == 4408)
						{
							dashUrl = "/dashboard/Alteon4408Content_Refresh.action";
						}
						else if (SelectAdcModelNum == 4024)
						{
							dashUrl = "/dashboard/Alteon4024Content_Refresh.action";
						}
						else if (SelectAdcModelNum == 4416)
						{
							dashUrl = "/dashboard/Alteon4416Content_Refresh.action";
						}
						else if (SelectAdcModelNum == 5224)
						{
							dashUrl = "/dashboard/Alteon5224Content_Refresh.action";
						}
						else if (SelectAdcModelNum == 5412)
						{
							dashUrl = "/dashboard/Alteon5412Content_Refresh.action";
						}
						else if (SelectAdcModelNum == 6420 || SelectAdcModelNum == 6421)
						{
							dashUrl = "/dashboard/Alteon6420Content_Refresh.action";
						}
						else if (SelectAdcModelNum == 6024)
						{
							dashUrl = "/dashboard/Alteon6024Content_Refresh.action";
						}
						else
						{
							dashUrl = "/dashboard/AlteonBasicContent_Refresh.action";
						}
					}
					else if (SelectAdcType == 1)	// F5
					{
						if (SelectAdcModelNum == 1600)
						{
							dashUrl = "/dashboard/F5_1600Content_Refresh.action";
						}
						else if ((SelectAdcModelNum == 2000) || (SelectAdcModelNum == 2200))
						{
							dashUrl = "/dashboard/F5_2000Content_Refresh.action";
						}
						else if (SelectAdcModelNum == 2400)
						{
							dashUrl = "/dashboard/F5_2400Content_Refresh.action";
						}
						else if (SelectAdcModelNum == 2600)
						{
							dashUrl = "/dashboard/F5_2600Content_Refresh.action";
						}
						else if (SelectAdcModelNum == 2800)
						{
							dashUrl = "/dashboard/F5_2800Content_Refresh.action";
						}
						else if (SelectAdcModelNum == 3600)
						{
							dashUrl = "/dashboard/F5_3600Content_Refresh.action";
						}
						else if (SelectAdcModelNum == 3900)
						{
							dashUrl = "/dashboard/F5_3900Content_Refresh.action";
						}
						else if ((SelectAdcModelNum == 4000) || (SelectAdcModelNum == 4200))
						{
							dashUrl = "/dashboard/F5_4000Content_Refresh.action";
						}
						else if (SelectAdcModelNum == 4600)
						{
							dashUrl = "/dashboard/F5_4600Content_Refresh.action";
						}
						else if (SelectAdcModelNum == 4800)
						{
							dashUrl = "/dashboard/F5_4800Content_Refresh.action";
						}
						else if (SelectAdcModelNum == 5600)
						{
							dashUrl = "/dashboard/F5_5600Content_Refresh.action";
						}
						else if (SelectAdcModelNum == 5800)
						{
							dashUrl = "/dashboard/F5_5800Content_Refresh.action";
						}
						else if (SelectAdcModelNum == 6400)
						{
							dashUrl = "/dashboard/F5_6400Content_Refresh.action";
						}
						else if (SelectAdcModelNum == 6900)
						{
							dashUrl = "/dashboard/F5_6900Content_Refresh.action";
						}
						else if ((SelectAdcModelNum == 5000) || (SelectAdcModelNum == 5200) || (SelectAdcModelNum == 7000) || (SelectAdcModelNum == 7200))
						{
							dashUrl = "/dashboard/F5_7000Content_Refresh.action";
						}
						else if (SelectAdcModelNum == 7600)
						{
							dashUrl = "/dashboard/F5_7600Content_Refresh.action";
						}
						else if (SelectAdcModelNum == 7800)
						{
							dashUrl = "/dashboard/F5_7800Content_Refresh.action";
						}
						else if (SelectAdcModelNum == 8400)
						{
							dashUrl = "/dashboard/F5_8400Content_Refresh.action";
						}
						else if (SelectAdcModelNum == 8900)
						{
							dashUrl = "/dashboard/F5_8900Content_Refresh.action";
						}
						else if ((SelectAdcModelNum == 10000) || (SelectAdcModelNum == 10200))
						{
							dashUrl = "/dashboard/F5_10000Content_Refresh.action";
						}
						else if (SelectAdcModelNum == 10600)
						{
							dashUrl = "/dashboard/F5_10600Content_Refresh.action";
						}
						else if (SelectAdcModelNum == 10800)
						{
							dashUrl = "/dashboard/F5_10800Content_Refresh.action";
						}
						else
						{
							dashUrl = "/dashboard/F5_BasicContent_Refresh.action";
						}
					}
					ajaxManager.runHtmlExt({
						url : dashUrl,
						data: ParamData,	
						target: targetName,
						successFn : function(params)
						{
							// Load 된 Widget의 사이즈 Data를 이용하여 Widget Resizing
							var html = $('.dashboardContentArea .printArea .column #'+widgetList.index);
							html.css("width", widgetList.widthMin + "px");
							html.css("height", widgetList.heightMin + "px");
							// Load된 Widget 에도 각각 하나씩 Event 와 Option을 부여한다.
							addWidgetEvent(widgetList.index,
									widgetList.widthMin, widgetList.widthMax,
									widgetList.heightMin, widgetList.heightMax);
							
							// 장비모니터링 OS Version Insert 부분
							var versionArea = $('.column #'+widgetList.index +' .version').filter(':last');
							versionArea.empty();
							versionArea.append(SelectAdcVersion);
							
							$('#col1').sortable("enable");
							
							if(modifyKey == false)
							{
								widgetLoadCount++;
								refreshDashboardWidget(allwidgetList, refershChartObj);	
							}									
						},
						errorFn : function(a,b,c)
						{
							exceptionEvent();
						}
					});
				},
				errorFn : function(a,b,c)
				{
					return false;
				}
			});
		}
	}
});