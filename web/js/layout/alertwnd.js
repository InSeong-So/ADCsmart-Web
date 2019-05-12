var ClassAlertWnd =  Class.create({
	initialize : function() 
	{
		this.settings = undefined;
		this.alertType = undefined;
		this.pageNavigator = new PageNavigator({ rowCountPerPage : 10 });
		this.orderDir = 2; // 내림차순 = 2
        this.orderType = 33; // OccurTime = 33
		this.OBajaxManager = new OBAjax();
		this.tickerkey = 0; // 0 : Ticker 가 꺼있음, 1 : Ticker 가 켜있음
	},
	loadContent : function() 
	{// 진입점. 경보 설정이 되어 있는지 확인한다.
		with (this) 
		{
			alertType = undefined;
			OBajaxManager.runJsonExt({
				url : "/alert/loadAlertSettingData.action",
				data : 
				{
				},
				successFn : function(data) 
				{					
					if (!data.isSuccessful) 
					{						
//						alert(data.message);
						$("#alarmMessage").addClass("none");
						return;
					}
					settings = data.settings;
					// Alert이 설정되어 있지 않으면 return 종료
					if (settings.alertType == 0)
					{
						$("#alarmMessage").addClass("none");
						return;
					}
					// Alert 설정이 팝업일때
					else if (settings.alertType == 1)
					{
						$("#alarmMessage").addClass("none");
						_loadAlertPopupContent();
					}
					// Alert 설정이 Ticker 일때
					else if (settings.alertType == 2)
					{
						_loadTickerswitch();
					}					
				},
				errorFn : function(a,b,c)
				{					
				}
			});
		}
	},
	//Ticker function
	/*
	 *  tickerKey = 0 // Ticker Background off
	 *  tickerKey = 1 // Ticker Background on
	 *    
	 *  #alarmMessage = Ticker Background image
	 *  #ticker_notice_area = Ticker Notice Area
	 */
	_loadTickerswitch : function()
	{
		with(this)
		{
			if (tickerkey == 0)
			{
				_loadTickerBGContent();
			}
			else if (tickerkey == 1)
			{
				_loadTickerNoticeContent();
			}
			else{}
		}
	},
	// Ticker Background Loader 
	_loadTickerBGContent : function()
	{
		with (this)
		{
			OBajaxManager.runHtml({				
				url			: 'alert/loadTickerBGContent.action',
				target		: '.header #alarmMessage',
				successFn	: function(params) 
				{					
					_loadTickerNoticeContent();	
				}			
			});
		}
	},
	// Ticker Notice Area Loader
	_loadTickerNoticeContent : function()
	{
		with (this)
		{
			OBajaxManager.runHtml({				
				url			: 'alert/loadTickerNoticeContent.action',
				target		: '.header #alarmMessage #ticker_notice_area',
				successFn	: function(params) 
				{					
					_registerTickerEvent();		
				}			
			});
		}
	},
	//Ticker Display Event
	/*
	 *  tickerKey = 0 // Ticker Background off
	 *  tickerKey = 1 // Ticker Background on
	 *  countSet  = 0 // 경보 띄울 Data 없음
	 *  countSet  = 1 // 경보 띄울 Data 있음
	 *  
	 *  #alarmMessage = Ticker Background image
	 *  .refresh_ticker = Ticker Message Element
	 */
	_registerTickerEvent : function() 
	{
		with (this)
		{			
			var countSet = $(".ticker-countset").val();
			if (tickerkey == 0) // Ticker 배경이 떠있지 않은 상태
			{
				if(countSet == 0) // 데이터가없다면 Ticker 모두 off
				{
					$("#alarmMessage").addClass("none");
					$(".refresh_ticker").addClass("none");
				}
				else // 데이터가 있다면 Ticker를 새로 모두 on
				{
					$("#alarmMessage").removeClass("none");
					$(".refresh_ticker").removeClass("none");
					tickerkey = 1;
				}	
			}
			else // Ticker 배경이 떠있는 상태
			{
				if(countSet == 0) // 데이터가없다면 Ticker 모두 off
				{
					$("#alarmMessage").addClass("none");
					$(".refresh_ticker").addClass("none");
					tickerkey = 0;
				}
				else // 데이터가 있다면 Ticker Notice 만 Update
				{
					$(".refresh_ticker").removeClass("none");
				}
			}			
					
			$('.alertMsgPop').click(function(e)
			{
				$("#alarmMessage").addClass("none");
				_loadAlertPopupContent();
				tickerkey = 0;
			});
			
			$('#ticker_del_btn').click(function(e)
			{
				$("#alarmMessage").addClass("none");
				tickerkey = 0;
			});
		}
	},	
	_loadAlertPopupContent : function() 
	{ 
		with (this) 
		{
			//Alert 팝업 Load
			var params = 
			{
				"alertType"						: alertType,
				"orderOption.orderType"			: orderType,
				"orderOption.orderDirection"	: orderDir				
			};
			OBajaxManager.runHtmlExt({
				url : "alert/loadPopupAlertContent.action",
				data : params,
				target		: '.header .alert-popup-area',			
				successFn : function(data)
				{
					var countSet = $(".alert-countset").val();
					if(countSet == 0)
					{
					}
					else
					{
						_showPopup();
						_registerPopupEvents();

						if (settings.isBeepOn)
						{
							_beep();
						}
					}					
				},
				errorFn : function(jqXhr)
				{
					// #5445-15 에 의해 에러 처리 안함
//					$.obAlertAjaxError(VAR_HISTORY_LOAD, jqXhr);
				}
			});
		}
	},
	_refreshAlertPopupContent : function() 
	{
		with (this) 
		{
			//Alert 팝업 Load
			var params = 
			{
				"alertType"						: alertType,
				"orderOption.orderType"			: orderType,
				"orderOption.orderDirection"	: orderDir				
			};
			OBajaxManager.runHtmlExt({
				url : "alert/refreshPopupAlertContent.action",
				data : params,
				target		: '.alert-table-area',			
				successFn : function(data) 
				{									
					_registerPopupEvents();	
					_noticePageInfo();
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_HISTORY_LOAD, jqXhr);
				}
			});
		}
	},
	_showPopup : function() 
	{
		with (this) 
		{
			var popupOption = 
			{
					'id' : '#alert-popup',
					'width' : '1000px'	
			};
			var $pop = null;
			// 팝업창 열기
			$pop = $(popupOption['id']).clone(); //alert(popupOption['id']);
			$pop.addClass('cloneDiv_alert');
			$pop.css('width', popupOption['width']);
			if (popupOption['height'] != null) 
			{
				$pop.find('.pop_contents').css('height', popupOption['height']);
			}
			$('body').append("<div class='popup_type1 popup_type_alert'></div>");
			$('body').append($pop);
			$pop.show();// .draggable();
			
			// 팝업창 정중앙 위치
			$pop.css({
			    'left' : ($(window).width() - $pop.width()) / 2,
			    'top' : ($(window).height() - $pop.height()) / 2
			});
			$('.popup_type_alert').height($('#wrap').height());
			// 팝업창 닫기
			$pop.find('.close').click(function(e) 
			{
				$('.popup_type_alert').remove();
				$pop.remove();
			});
			header.clearRefreshTimer();
		}
	},
	_noticePageInfo : function()
	{
		with(this)
		{
			var countSet = $(".alert-resetCount").val();
			var targetCntHtml = $('.alertCount').filter(':last');
			targetCntHtml.html(countSet);
//			if(alertType == 1)
//				targetCntHtml.html(countSet);
//			if(alertType == 0)
//				targetCntHtml.html(countSet);
//			else
//				targetCntHtml.html(countSet);
		}
	},
	_registerPopupEvents : function() 
	{
		with (this) 
		{
			$('.alert-all').click(function() 
			{
				TapClear();
				$(this).css({'background-color' : '#666666','color': '#fff'});			
				alertType = undefined;
				_refreshAlertPopupContent();
			});
			
			$('.alert-fault').click(function() 
			{
				TapClear();
				$(this).css({'background-color' : '#666666','color': '#fff'});			
				alertType = 0;
				_refreshAlertPopupContent();
			});
			
			$('.alert-warning').click(function() 
			{
				TapClear();
				$(this).css({'background-color' : '#666666','color': '#fff'});			
				alertType = 1;
				_refreshAlertPopupContent();
			});		
			
			$('.cloneDiv_alert .moveWndLnk').click(function(e) 
			{ 
				dashboardServiceHeader.clearAutoRefreshTimer();
				e.preventDefault();
				$('.popup_type_alert').remove();
				$('.cloneDiv_alert').remove();
				adcSetting.loadContent();				
				alertMonitoring.loadListContent("", "", "", false);
				adcSetting.setObjOnAdcChange(alertMonitoring);
				header.headerLoginEvents();
			});
			
			$('.cloneDiv_alert .closeWndLnk').click(function(e) 
			{
				e.preventDefault();
				$('.popup_type_alert').remove();
				$('.cloneDiv_alert').remove();
				_modifyUserAlertTime();				
			});
			
			$('.orderDir_Desc').click(function(e)
			{
				e.preventDefault();
				orderDir =  $(this).find('.orderDir').text();
				orderType =  $(this).find('.orderType').text();
				_refreshAlertPopupContent();
			});
			
			$('.orderDir_Asc').click(function(e)
			{
				e.preventDefault();
				orderDir =  $(this).find('.orderDir').text();
				orderType =  $(this).find('.orderType').text();
				_refreshAlertPopupContent();
			});
			
			$('.orderDir_None').click(function(e)
			{
				e.preventDefault();
				orderDir =  $(this).find('.orderDir').text();
				orderType =  $(this).find('.orderType').text();						
				_refreshAlertPopupContent();
			});
		}
	},
	_beep : function() 
	{
		$('.cloneDiv_alert .alertBeep').append('<embed type="application/x-mplayer2" height="0" width="0" src="../../audio/73797_alarmclock.mp3" autostart="1"></embed>');
	},
	TapClear : function()
	{
		with(this)
		{
			$('.alert-all').css({'background-color': '#fff', 'color': 'black'});
			$('.alert-fault').css({'background-color': '#fff', 'color': 'black'});
			$('.alert-warning').css({'background-color': '#fff', 'color': 'black'});					
		}
	},
	_modifyUserAlertTime : function()
	{
		with(this)
		{
			OBajaxManager.runJson({
				url : "/alert/modifyUserAlertTime.action",
				data : 
				{
				},
				successFn : function(data) 
				{
					// Alert Timer 재작동
					header.headerLoginEvents();
				}
			});
		}
	}
});