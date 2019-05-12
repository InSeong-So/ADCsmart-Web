var LogContentInfo = Class.create({
	initialize : function() 
	{
//		this.adcLogSelVal = "alteon";
		this.adcLogSelVal = undefined;
	},
	
	loadContent : function() 
	{
		with (this)
		{			
			adcLogSelVal = undefined;
			ajaxManager.runHtmlExt({
				url : 'sysSetting/loadLogContent.action',
				data : 
				{
					"fileName" : adcLogSelVal
				},
				target : "#wrap .contents", 
				successFn : function(params)
				{					
					header.setActiveMenu('SysSettingLogContent');
					registerContents();
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_SYSSETTING_ADMINLOGLOAD, jqXhr);
				}
			});		
		}
	},
	
	loadLogTableInContent : function(adcLogSelVal) 
	{
		with (this)
		{
			ajaxManager.runHtmlExt({
				url : 'sysSetting/loadLogContent.action',
				data : 
				{
					"fileName" : adcLogSelVal
				},
				target : "#wrap .contents", 
				successFn : function(params)
				{					
					registerContents();
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_SYSSETTING_ADMINLOGLOAD, jqXhr);
				}
			});		
		}
	},
	
	registerContents : function() 
	{
		with (this) 
		{
			$('.refreshLnk').click(function(event) 
			{
				event.preventDefault();
				adcLogSel = $('#adcLogSel option:selected').val();
				
				if(adcLogSel == "")
				{
					$.obAlertNotice(VAR_SYSSETTING_LOGFILESEL);
					return false;
				}
				loadLogTableInContent(adcLogSel);
			});	
			
			$('#adcLogSel').change(function(event)
			{
				event.preventDefault();
				adcLogSelVal = $('#adcLogSel option:selected').val();
				loadLogTableInContent(adcLogSel);
			});
			
			if (!adcLogSelVal)
			{
				adcLogSelVal = $('#adcLogSel option:selected').val();
			}
			else
			{
				$('#adcLogSel option').each(function() 
				{
					if ($(this).val() == adcLogSelVal)
					{
						$(this).attr("selected", "selected");
						return false;
					}
				});
			}
			
			var textarea = document.getElementById('scrollBottom');
			textarea.scrollTop = textarea.scrollHeight;
		}
	}
});