var FlbInfo = Class.create({
	initialize : function() 
	{
		this.adc;
		this.selectedPhysicalPort = undefined; 
		this.orderDir  = 1; // 오름차순 = 1
		this.orderType = 37; // PORT_NAME = 14
		this.pageNavigator = new PageNavigator();
		var _self = this;
		this.pageNavigator.onChange(function(fromRow, toRow, orderType, orderDir) {
			_self.loadFlbInfoTableList(fromRow, toRow);
		});
	},
	onAdcChange : function() 
	{
		with(this)
		{
			selectedPhysicalPort = undefined;		
			loadListContent(adcSetting.getAdc());			
		}
	},
	loadListContent : function(adc, refreshes)	// total Count Get
	{
		with(this)
		{
			this.adc = adcSetting.getAdc();
			if (!adc)
			{
				return;
			}					
			var params = {
				"adcObject.index"			: adc.index,			
				"searchOption.searchKey"	: this.selectedPhysicalPort				
			};	
			ajaxManager.runJsonExt
			({
				url			: "flbInfo/loadFlbInfoTotalCount.action",
				data		: params,
				successFn	: function(data)
				{
					var totalCount = 0;
					if (data.totalCount)
					{
						totalCount = data.totalCount;
					}
					pageNavigator.updateRowTotal(totalCount);					
					noticePageInfo();							
					loadFlbInfoContent(undefined, undefined, orderType, orderDir, refreshes);
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_FLBFILTERINFO_FILTERINFOEXTRACT, jqXhr);
				}
			});
		}		 
	},
	loadFlbInfoContent : function(fromRow, toRow, orderType, orderDir, refreshes) // total Count Get
	{
		with (this)
		{	
			if (!adc)
			{
				return;
			}
			ajaxManager.runJsonExt({
				url : "flbInfo/loadRefreshListContent.action",
				data :
				{
					"adc.index" : adcSetting.getAdc().index,
					"adc.name" : adcSetting.getAdc().name,
					"adc.type" : adcSetting.getAdc().type,
					"refreshes" : !!refreshes
				},
				successFn : function(data)
				{				
					if (!data.isSuccessful)
					{
						if(data.extraKey == 1)
						{
							return;
						}
						$.obAlertNotice(data.message);
						return;
					}
					var params =
					{
						"adcObject.index"				: adc.index,			
						"searchOption.searchKey"		: selectedPhysicalPort,
						"orderOption.orderType"			: orderType,
						"orderOption.orderDirection"	: orderDir
					};
					params["searchOption.beginIndex"] = (undefined === fromRow ? pageNavigator.getFirstRowOfCurrentPage() : fromRow);
					params["searchOption.endIndex"] = (undefined === toRow ? pageNavigator.getLastRowOfCurrentPage() : toRow);
					ajaxManager.runHtmlExt({
		
						url : "flbInfo/loadFlbInfoContent.action",				
						data		: params,
						target		: "#wrap .contents",
						successFn	: function(data)
						{
							header.setActiveMenu('FlbInfo');	
							noticePageInfo();
							registerFlbInfoContentEvents();
							restoreFlbInfoContent();							
						},
						completeFn : function()
						{
							if (selectedPhysicalPort)
							{
								$('#physicalport').val(selectedPhysicalPort).attr("selected", "selected");
							}
							pageNavigator.refresh();
						},						
						errorFn : function(jqXhr)
						{
							$.obAlertAjaxError(VAR_FLBFILTERINFO_FILTERINFOEXTRACT, jqXhr);
						}	
					});
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_VS_VSECEFAIL, jqXhr);
				}				
			});
		}
	},

	loadFlbInfoTableList : function(fromRow, toRow, orderType, orderDir, refreshes) // 독립적으로 사용할때.
	{
		with (this)
		{							
			var params =
			{
				"adcObject.index"			: adc.index,			
				"searchOption.searchKey"	:this.selectedPhysicalPort,
				"orderOption.orderType"			: orderType,
				"orderOption.orderDirection"	: orderDir
			};
			params["searchOption.beginIndex"] = (undefined === fromRow ? pageNavigator.getFirstRowOfCurrentPage() : fromRow);
			params["searchOption.endIndex"] = (undefined === toRow ? pageNavigator.getLastRowOfCurrentPage() : toRow);
			
			ajaxManager.runHtmlExt({

				url : "flbInfo/loadFlbInfoList.action",				
				data		: params,
				target		: ".contents_area #flb_table",
				successFn	: function(data)
				{			
					noticePageInfo();
					registerFlbInfoContentEvents();
					restoreFlbInfoContent();
				},
				completeFn : function()
				{
					pageNavigator.refresh();
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_FLBFILTERINFO_FILTERINFOEXTRACT, jqXhr);
				}
			});
		
		}
	},
	checkExportFlbInfoDataExist : function(fromRow, toRow)
	{
		with(this)
		{
			if (adc.type != "Alteon")
			{				
				return;
			}
			var params =
			{
				"adcObject.index"			: adc.index,			
				"searchOption.searchKey"	: this.selectedPhysicalPort,
				"orderOption.orderType"			: orderType,
				"orderOption.orderDirection"	: orderDir
			};
			params["searchOption.beginIndex"] = undefined;
			params["searchOption.endIndex"] = 1000000;
			
			ajaxManager.runJsonExt({

				url : "flbInfo/checkFlbInfoExist.action",				
				data		: params,				
				successFn	: function(data)
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
					$.obAlertAjaxError(VAR_COMMON_EXPDATAEXIST, jqXhr);
				}
			});
		}
	},
	exportCss : function(fromRow, toRow) 
	{
		with (this) 
		{
			var params = undefined;
			if (!this.selectedPhysicalPort)
			{
				params = "adcObject.index=" + adc.index
				+ "&searchOption.beginIndex=" + undefined
				+ "&searchOption.endIndex=" + 1000000
				+ "&orderOption.orderType=" + orderType
				+ "&orderOption.orderDirection"	+ orderDir;
			}
			else
			{				
				params = "adcObject.index=" + adc.index + "&searchOption.searchKey=" + this.selectedPhysicalPort
				+ "&searchOption.beginIndex=" + undefined
				+ "&searchOption.endIndex=" + 1000000
				+ "&orderOption.orderType=" + orderType
				+ "&orderOption.orderDirection"	+ orderDir;	
			}				
			var url = "flbInfo/downloadFlbInfoExist.action?" + params;
			$('#downloadFrame').attr('src', url);
		}
	},
	restoreFlbInfoContent : function()
	{
		with(this)
		{
			// Select Box Event In
			$('#physicalport').change(function(e)
			{
				e.preventDefault();
				if ($(this).val() == 0)
				{
					selectedPhysicalPort = undefined;
				}
				else
				{
					selectedPhysicalPort = $(this).val();
				}				 
				loadListContent();
			});
		}
	},
	registerFlbInfoContentEvents : function() 
	{
		with (this)
		{
			$('#refreshLnk').click(function(e)
			{
				e.preventDefault();
				if (adc.type != "Alteon")
				{
					return;
				}
				else
				{
					loadListContent(adc, true);
				}
			});
					
			$('#exportCssLnk').click(function(e)
			{
				e.preventDefault();				
				checkExportFlbInfoDataExist();
			});
			
			$('.orderDir_Desc').click(function(e)
			{
				e.preventDefault();
				orderDir =  $(this).find('.orderDir').text();
				orderType =  $(this).find('.orderType').text();					
				loadListContent();
			});
			
			$('.orderDir_Asc').click(function(e)
			{
				e.preventDefault();
				orderDir =  $(this).find('.orderDir').text();
				orderType =  $(this).find('.orderType').text();	
				loadListContent();
			});
			
			$('.orderDir_None').click(function(e)
			{
				e.preventDefault();
				orderDir =  $(this).find('.orderDir').text();
				orderType =  $(this).find('.orderType').text();	
				loadListContent();
			});

			if (adcSetting.getAdc().type != "Alteon") 
			{
				$('#physicalport').attr("disabled", "disabled");
				$('.refeshImgOff').removeClass("none");
				$('.refeshImgOn').addClass("none");	
				$('.disabledChk').addClass("none");
				$('.nulldataMsg').removeClass("none");				
				$('.imgOff').removeClass("none");
				$('.imgOn').addClass("none");
			}
			
			if (adcSetting.getAdcStatus() == "available") 
			{
				$('.nulldataMsg').addClass("none");
				if($('.trFlbInfoList').size() > 0)
				{
					$('.dataNotExistMsg').addClass("none");
				}
				else
				{
					$('.dataNotExistMsg').removeClass("none");
				}
			}
			
			if (adcSetting.getAdcStatus() != "available") 
			{
				$('.dataNotExistMsg').addClass("none");
				if($('.trFlbInfoList').size() > 0)
				{
					$('.nulldataMsg').addClass("none");
				}
				else
				{
					$('.nulldataMsg').removeClass("none");
				}
			}
			
			// 비활성화 기능 개선 예정
			/*if ($('.trFlbInfoList').size() > 0 )
			{
				$('.disabledChk').removeClass("none");
				$('.nulldataMsg').addClass("none");				
				$('.imgOff').addClass("none");
				$('.imgOn').removeClass("none");
			}
			else
			{
				$('#physicalport').attr("disabled", "disabled");
				$('.refeshImgOff').removeClass("none");
				$('.refeshImgOn').addClass("none");	
				$('.disabledChk').addClass("none");
				$('.nulldataMsg').removeClass("none");				
				$('.imgOff').removeClass("none");
				$('.imgOn').addClass("none");
			}*/
		}
	},
	noticePageInfo : function()
	{
		with(this)
		{
			var currentPage = pageNavigator.getCurrentPage();
			var lastPage = pageNavigator.getLastPage();
			var countTotal = pageNavigator.getRowTotal();
			var targetCntHtml = $('.noticePageCountInfo').filter(':last');
			var targetPageHtml = $('.noticePageInfo').filter(':last');
			targetCntHtml.html(addThousandSeparatorCommas(countTotal));
			targetPageHtml.html("(" + addThousandSeparatorCommas(currentPage) + "/" + addThousandSeparatorCommas(lastPage) + VAR_COMMON_PAGE + ")");
		}
	}
});