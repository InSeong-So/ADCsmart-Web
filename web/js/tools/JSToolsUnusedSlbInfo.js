var JSToolsUnusedSlbInfo = Class.create
({
	initialize : function() 
	{
		this.isInitialized = false;
		this.adcIndex = undefined;
		this.adcType = 0;
		this.searchType = 0;
//		this.adcType = undefined;
//		this.Id = undefined;
//		this.password = undefined;
	},
	loadContent : function() 
	{
		with (this) 
		{
			ajaxManager.runHtmlExt
			({
				url: 'sysTools/unUsedSlbLoadContent.action',
				target: "#wrap .contents",
				data : 
				{
					"adcIndex" : this.adcIndex,
					"searchType" : this.searchType
				}, 
				successFn : function(params) 
				{
					_registerAdcAddContentEvents();
					header.setActiveMenu('JSToolsUnusedSlbInfo');
				},
				errorFn : function(a,b,c)
				{
					exceptionEvent();
				}	
			});
			
		}
	},
	_registerAdcAddContentEvents : function() 
	{
		with (this) 
		{
			$('.searchLnk').click(function(event) 
			{
				adcIndex = $('select[name="adcIndex"]').val();
				searchType = $('select[name="searchType"]').val();
				loadContent();
				/*$('#uploadform').submit();*/
			});
			
			$('.exportCssLnk').click(function(event) 
			{
//				event.preventDefault();  
				_exportCss();
			});
			
			$('#adcType').change(function()
			{
				adcType = $('#adcType option:selected').val();
			});
			
			if (!adcType)
			{
				adcType = $('#adcType option:selected').val();
			}
			else
			{
				$('#adcType option').each(function() 
				{
					if ($(this).val() == adcType)
					{
						$(this).attr("selected", "selected");
						return false;
					}
				});
			}
			
			$('#searchType').change(function()
			{
				searchType = $('#searchType option:selected').val();
			});
			
			if (!searchType)
			{
				searchType = $('#searchType option:selected').val();
			}
			else
			{
				$('#searchType option').each(function() 
				{
					if ($(this).val() == searchType)
					{
						$(this).attr("selected", "selected");
						return false;
					}
				});
			}
		}
	},
	_exportCss : function() 
	{
		with (this) 
		{
			var url = "sysTools/unUsedSlbDownloadCsv.action";
			$('#downloadFrame').attr('src', url);
		}
	},
});
