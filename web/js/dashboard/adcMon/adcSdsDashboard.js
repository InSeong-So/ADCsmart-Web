var AdcSdsDashboard = Class.create({
	initialize : function() 
	{
		this.ajaxManager = new FlowitAjax();
		this.header = new AdcSdsDashboardHeader();
		this.header.owner = this;
	},
	load : function() 
	{
		with (this) 
		{
			loadRefreshTime();
		}
	},
	loadContent : function() 
	{	
		with (this) 
		{
//			var params = {
//				selectedType				: left.selectedType,
//				groupIndex					: left.groupIndex,
//				adc							: left.adc,
//				statusGroup					: header.statusGroup,
//				status						: header.status,
//				vsUnavailableStatusMinDays	: header.vsUnavailableStatusMinDays,
//				faultMaxDays				: header.faultMaxDays,
//				restore						: restore
//			};
//			content.loadAdcSdsFaultMonitoring(params);
//			content.loadAdcSdsFaultMonitoring();
		}
	},
	loadRefreshTime : function() 
	{
		with (this) 
		{
			var _self = this;
			ajaxManager.runJson({
				url			: '/dashboard/adcMon/loadRefreshTime.action',
				successFn	: function(data) 
				{
					_self.refreshIntervalSeconds = data.refreshTime;
//					this.refreshIntervalSeconds = data.refreshTime;
//					$('.update_time').text("최근 업데이트  " + data.lastUpdatedTime);
					$('.update_time').text(data.lastUpdatedTime);
//					content.loadAdcSdsFaultMonitoring();
					header.load();		//leftPane, all_Virtual Server Status (header.js / header.ftl)
				}
			});
		}
	}	
});




