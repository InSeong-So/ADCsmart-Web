var SdsDashboard = Class.create({
	initialize : function() {
		this.ajaxManager = new FlowitAjax();
		this.refreshTimer = undefined;
		this.alertTimer = undefined;
		this.refreshIntervalSeconds = 30;
		this.alertIntervalSeconds = 5;
		this.header = new SdsDashboardHeader();
		this.left = new SdsDashboardLeft();
		this.content = new SdsDashboardContent();
		this.header.owner = this;
		this.content.owner = this;
		this.alertWnd = new AlertWnd();
		
		this.loadAlertTime();
	},
	load : function()
	{
		with (this) 
		{
			_registerEvents();//경보알림 체크이벤트
			loadRefreshTime();
//			$('.dash_header h1').click(function() {
//				alertWnd.popUp(function() {
//					FlowitUtil.log('function on close');
//				});
//			});
		}
	},
	loadHeader : function()
	{
		with (this)
		{
			header.load();
		}
	},
	loadContent : function(restore)
	{
		with (this)
		{
			var params = {
				selectedType				: left.selectedType,
				groupIndex					: left.groupIndex,
				adc							: left.adc,
				statusGroup					: header.statusGroup,
				status						: header.status,
				vsUnavailableStatusMinDays	: header.vsUnavailableStatusMinDays,
				faultMaxDays				: header.faultMaxDays,
				restore						: restore
			};
			content.load(params);
		}
	},
	loadAlertPopup : function()
	{
		with (this)
		{
			clearAlertTimer();
			
			var _self = this;
			alertWnd.popUp(function() {
				_self.clearAlertTimer();
				if (0 != _self.alertIntervalSeconds)
				{
					_self.alertTimer = setInterval(function() {
						_self.loadAlertPopup();
					}, _self.alertIntervalSeconds * 1000);
				}
			});
		}
	},
	loadRefreshTime : function() 
	{
		with (this)
		{
			var _self = this;
			ajaxManager.runJson({
				url			: 'dashboard/sds/loadRefreshTime.action',
				successFn	: function(data) {
					_self.refreshIntervalSeconds = data.refreshTime;
					$('.update_time').text("최근 업데이트  " + data.lastUpdatedTime);
					left.loadLeftPane();
				}
			});
		}
	},
	loadAlertTime : function() 
	{
		with (this) {
			var _self = this;
			ajaxManager.runJson({
				url			: 'dashboard/sds/loadAlertTime.action',
				successFn	: function(data) {
					_self.alertIntervalSeconds = data.alertTime;
					loadAlertPopup();
				}
			});
		}
	},
	resetRefreshTimer : function()
	{
		with (this) {
			clearRefreshTimer();
			if (0 != refreshIntervalSeconds)
			{
				refreshTimer = setInterval(function() {
					load();
				}, refreshIntervalSeconds * 1000);
			}
		}
	},
	clearRefreshTimer : function()
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
	clearAlertTimer : function()
	{
		with (this)
		{
			if (alertTimer) 
			{
				clearInterval(alertTimer);
				alertTimer = undefined;
			}
		}
	},
	_enableAlert : function()
	{ //체크시 1
			with (this)
			{ 
				ajaxManager.runJson({
					url			: 'dashboard/sds/setAlertStatus.action',
					data : {
						'alertStatus' : 1
					},				
					successFn	: function(data)
					{
					}
				});
			}
	},	
	_disableAlert : function() 
	{ //체크 해제시 0
		with (this)
		{
			ajaxManager.runJson({
				url			: 'dashboard/sds/setAlertStatus.action',
				data : {
					'alertStatus' : 0
				},				
				successFn	: function(data)
				{
				}
			});
		}
	},	
	_registerEvents : function()
	{ //이벤트등록
		with (this) 
		{
			$('#checkbox').change(function(e) {
				e.preventDefault();
				
				var isChecked = $('#checkbox').is(':checked'); 
				
				if (isChecked == true)
				{ 	
					//$('.statusText').text("체크되었습니다.");	
					_enableAlert();
				}
				else
				{
					//$('.statusText').text("uncheck되었습니다.");
					_disableAlert();
				}
			});
		}
	}
});




