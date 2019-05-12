var JSToolsPortInfo = Class.create({
	initialize : function() {
		this.isInitialized = false;
	},
	
	loadContent : function() 
	{
		with (this) {
			refreshLnk();
			isInitialized = true;
		}
	},
	
	registerContents : function() 
	{
		with (this) {
			$('.refreshLnk').click(function(event) {
				event.preventDefault();
				refreshLnk();
			});
			
			$('.exportCssLnk').click(function(event) {
				event.preventDefault();
				_checkExportDataExist();					
			});			
		}
	},
	_checkExportDataExist : function()
	{
		with(this)
		{
			ajaxManager.runJsonExt
			({
				url : "sysTools/checkPortUsageDataExist.action",
				
				successFn : function(data)
				{
					if(!data.isSuccessful)
					{
						$.obAlertNotice(data.message);
						return;
					}
					exportCss();
				},
				errorFn : function(a, b, c)
				{
					exceptionEvent();
				}
			});
		}
	},
	exportCss : function()
	{
		with (this)
		{
//			var url = "monitor/downloadPerformanceF5.action?adc.index=" + adc.index +"&adcType=" + adc.type +"&searchTimeMode=" + selectedSearchTimeMode + "&hour=" + searchLastHours + "&startTime=" + $('input[name="startTime"]').val() + "&endTime=" + $('input[name="endTime"]').val();
			var url = "sysTools/portUsageDownloadCsv.action";
			$('#downloadFrame').attr('src', url);
		}
	},
	refreshLnk : function()
	{
		with (this)
		{
			var params = {
					"isInitailized"			: isInitialized,
				};	
			ajaxManager.runHtml({
				url : 'sysTools/portUsageLoadContent.action',
				data		: params,
				target : "#wrap .contents", 
				successFn : function(params)
				{
					header.setActiveMenu('JSToolsPortInfo');
					registerContents();
				},
				errorFn : function(a,b,c)
				{
					exceptionEvent();
				}	
			});		
		}
	}
});