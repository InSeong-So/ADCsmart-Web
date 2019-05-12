/**
 * 상단의 header 영역을 처리한다. 주로 alert message를 처리한다.
 */
var ClassLayoutHeader = Class.create(
{
	initialize : function(firstmenu) 
	{
		this.refreshIntervalSeconds = 10;	//  시간조절 초단위
		this.refreshTimer = undefined;
		this.CAlertWnd = new ClassAlertWnd();
		this.CFirstMenu = firstmenu;
	},	
//	getDashPopup : function() 
//	{
//		return this.dashPopup;
//	},
//	setDashPopup : function(dashPopup) 
//	{
//		this.dashPopup = dashPopup;
//	},
	loadContent : function() 
	{
		with (this) 
		{
			ajaxManager.runHtml({
				url : "layout/loadHeader.action",
				target: ".header",
				successFn : function(params) 
				{
					_registerEvents();
					_headerLoginEvents();
				}			 
			});
		}
	},
	_headerLoginEvents : function() 
	{
		with (this) 
		{			
			_clearRefreshTimer();
			
			if (0 != refreshIntervalSeconds) 
			{
				refreshTimer = setInterval(function() 
				{					
					_loadLoginCheck();
					_retrieveSettingsAndShowAlert();
				}, refreshIntervalSeconds * 1000);
			}
		}
	},
	_clearRefreshTimer : function() 
	{
		with (this) 
		{
			if (refreshTimer) 
			{
				clearInterval(refreshTimer);
				refreshTimer = undefined;
			}
		}
	},
	_loadLoginCheck : function() 
	{
		with (this) 
		{
			ajaxManagerOB.runJson({
				url : "layout/loadLoginCheck.action",
				target : ".header",
				successFn : function(params) 
				{					
				},
				errorFn: function(jqXhr, status, message)
				{
					// #5445: 에러 발생 시 아무 작업도 하지 않는다.
				}
			});		
		}		
	},	
	_retrieveSettingsAndShowAlert : function() 
	{// alert message를 처리한다.
		with (this) 
		{
			this.CAlertWnd.loadContent();
		}
	},
	_registerEvents : function() 
	{
		with(this)
		{		
			// Header logo 클릭시 발생하는 이벤트
			$('#header_logo').click(function()
			{
				_loadMainContent();
			});	
			
			$('.loginIdLnk').click(function(e) 
			{
				e.preventDefault();
				$accountIndex = $(this).children('span').eq(0).text();
//				FlowitUtil.log($accountIndex);
				sysSetting.loadLeftPane();
				sysSetting.loadUserModifyContent($accountIndex);
			});		
			$('#logoutLnk').click(function(e) 
			{
				e.preventDefault();
				location.href = 'member/logout.action';
				this.CFirstMenu.closeDashboardPopup();
//				if (getDashPopup() === undefined)
//				{				
//				}
//				else
//				{
//					getDashPopup().close();
//				}			
			});
		}
	},	
	_loadMainContent : function() 
	{
		with (this) 
		{			
			ajaxManager.runHtml({
				url : "layout/loadContent.action",				
				target: "#wrap .contents",
				successFn : function(params) 
				{
				}
			});
		}
	},
});
