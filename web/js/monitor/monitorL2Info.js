var MonitorL2Info = Class.create({
	initialize : function()
	{
		var fn = this;
		this.l2SearchOption = [];      	
		this.adc = {};
		this.orderDir  = 2; //2 :  내림차순
		this.orderType = 33;// 33 : occurTime
		this.searchFlag = false;
		this.pageNavi = new PageNavigator();
		this.pageNavi.onChange(function(fromRow, toRow, orderType, orderDir) 
		{
			fn.loadL2InfoTableInListContent(fn.l2SearchOption, fromRow, toRow, orderType, orderDir);
		});	
	},	
	onAdcChange : function() 
	{
		this.orderDir  = 2; //2 :  내림차순
		this.orderType = 33;// 33 : occurTime
		this.l2SearchOption = [];
		this.loadL2SearchContent();
		//this.loadListContent(option);
	},
	
	getIndex : function(index)
	{
		if (index == 1)
		{
			selectedIndex = adcSetting.getGroupIndex();
		}
		else if (index == 2)
		{
			selectedIndex = adcSetting.getAdc().index;
		}
		else
		{
			selectedIndex = 0;
		}
		return selectedIndex;
	},
	// 초기페이지 로드
	loadL2SearchContent : function()
	{		
		with(this)
		{
			if (!adc)
			{
				return;
			}
			var params = 
			{
				"adcObject.category"	: adcSetting.getSelectIndex(),
				"adcObject.index" 		: getIndex(adcSetting.getSelectIndex()),
				"adc.type" 				: adcSetting.getAdc().type,
				"adc.name" 				: adcSetting.getAdc().name,
				"orderObj.orderDirection" : this.orderDir,
				"orderObj.orderType" : this.orderType,
			};
			ajaxManager.runHtmlExt({
				url : "monitor/loadL2SearchContent.action",
				data : params,
				target : "#wrap .contents",				
				successFn : function(data)
				{				
					header.setActiveMenu('MonitorL2Info');					
					clickEvents();
				},
				completeFn : function()
				{
					pageNavi.updateRowTotal(0, orderType);	
					noticePageInfo();
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_L2_SEARCH, jqXhr);
				}
			});
		}
	},
	// L2 정보 ADC에서 DB로 SET (Return 값 무시)
	setL2InfoListToDB : function(l2SearchOption, fromRow, toRow)
	{
		with (this)
		{			
			ajaxManager.runJsonExt({
				url : "monitor/setL2InfoListToDB.action",			
				data : 
				{
					"adcObject.category"	: adcSetting.getSelectIndex(),
					"adcObject.index" 		: getIndex(adcSetting.getSelectIndex()),
					"adcObject.name"		: adcSetting.getGroupName(),
					"adc.type" 				: adcSetting.getAdc().type,
					"adc.name" 				: adcSetting.getAdc().name,
					"l2SearchKeyListString" : Object.toJSON(l2SearchOption),
					"searchObj.beginIndex" 	: 0,
					"searchObj.endIndex" 	: 1				
				},
				successFn : function(params)
				{
					header.setActiveMenu('MonitorL2Info');
					//searchedOption = searchOption;
					updateNavigator();
				},
				completeFn : function()
				{
					pageNavi.refresh();
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_L2_SEARCH, jqXhr);
				}
			});
		}
	},
	//L2 총 리스트 갯수 Get
	updateNavigator : function(searchOption, orderDir, orderType)
	{
		with (this)
		{		
			var rowTotal = 0;
			ajaxManager.runJsonExt({
				url : "monitor/loadL2InfoListTotal.action",
				data :
				{
					"adcObject.category"	: adcSetting.getSelectIndex(),
					"adcObject.index" 		: getIndex(adcSetting.getSelectIndex()),
					"adcObject.name"		: adcSetting.getGroupName()					
				},
				successFn : function(data)
				{
					if (!data.isSuccessful)
					{
						$.obAlertNotice(data.message);
						rowTotal = 100;
					}
					else
					{
						rowTotal  = data.rowTotal;
					}		
					pageNavi.updateRowTotal(rowTotal, orderType);
					loadL2InfoTableInListContent(searchOption);	
				},
				errorFn : function(jqXhr)
				{		
					$.obAlertAjaxError(VAR_L2_SEARCH, jqXhr);
				}
			});
		}
	},
	// L2 정보 Get ( 페이징 정렬 조건에 맞게 제공)
	loadL2InfoTableInListContent : function(searchOption, fromRow, toRow, orderType, orderDir) 
	{		
		with (this)
		{
			ajaxManager.runHtmlExt({
				url : "monitor/loadL2InfoTableInListContent.action",
				target : "table.l2Table",
				data : 
				{
					"adcObject.category"	: adcSetting.getSelectIndex(),
					"adcObject.index" 		: getIndex(adcSetting.getSelectIndex()),
					"adcObject.name"		: adcSetting.getGroupName(),
					"adc.type" 				: adcSetting.getAdc().type,
					"adc.name" 				: adcSetting.getAdc().name,					
					"searchObj.beginIndex" 	: fromRow === undefined ? pageNavi.getFirstRowOfCurrentPage() : fromRow,
					"searchObj.endIndex" 	: toRow === undefined ? pageNavi.getLastRowOfCurrentPage() : toRow,
					"orderObj.orderDirection" 	: orderDir,
					"orderObj.orderType" 		: orderType
				},
				successFn : function(params)
				{				
					noticePageInfo();
					registerListContentsEvents();
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_L2_LOAD, jqXhr);
				}
			});
		}
	},
	clickEvents : function()
	{
		with(this)
		{
			if (adcSetting.getAdcStatus() == "available") 
			{
				$('.searchNotMsg').addClass("none");
				$('.nulldataMsg').addClass("none");
				if($('.l2List').size() > 0)
				{
					$('.dataNotExistMsg').addClass("none");
				}
				else
				{
					$('.dataNotExistMsg').removeClass("none");
				}
			}
			
			// IP Check Box Event
			$('.contents_area .IPchk').click(function(e)
			{
			    if($(this).is(':checked'))
			    {
			    	$('input[name=searchKey_IP]').removeAttr("disabled");
			    }
			    else
			    {
			    	$('input[name=searchKey_IP]').attr("disabled", "disabled");
			    	$('input[name=searchKey_IP]').val(null);			    
			    }
			});
			// MAC Check Box Event
			$('.contents_area .MACchk').click(function(e)
			{
			    if($(this).is(':checked'))
			    {
			    	$('input[name=searchKey_MAC]').removeAttr("disabled");
			    }
			    else
			    {
			    	$('input[name=searchKey_MAC]').attr("disabled", "disabled");
			    	$('input[name=searchKey_MAC]').val(null);			    
			    }
			});
			// VLAN Check Box Event
			$('.contents_area .VLANchk').click(function(e)
			{
			    if($(this).is(':checked'))
			    {
			    	$('input[name=searchKey_VLAN]').removeAttr("disabled");
			    }
			    else
			    {
			    	$('input[name=searchKey_VLAN]').attr("disabled", "disabled");
			    	$('input[name=searchKey_VLAN]').val(null);
			    }
			});
			// IF Check Box Event
			$('.contents_area .IFchk').click(function(e)
			{
			    if($(this).is(':checked'))
			    {
			    	$('input[name=searchKey_Interface]').removeAttr("disabled");
			    }
			    else
			    {
			    	$('input[name=searchKey_Interface]').attr("disabled", "disabled");
			    	$('input[name=searchKey_Interface]').val(null);
			    }
			});
			
			if ((adcSetting.getAdc().type == "PAS") || (adcSetting.getAdc().type == "PASK"))
			{
				$('.IPchk, .MACchk, .VLANchk, .IFchk').attr("disabled", "disabled");
			}
			
			$('.contents_area .searchLnk').click(function(e)
			{
				e.preventDefault();
				with(this)
				{
					if (adcSetting.getAdc().type == "PAS")
					{
						//alert(VAR_COMMON_PASNOTSUPPORT);
						return;
					}
					else if (adcSetting.getAdc().type == "PASK")
					{
						//alert(VAR_COMMON_PASKNOTSUPPORT);
						return;
					}
					else
					{
						searchFlag = true;
						GenerateSearchKey();
					}					
				}			
			});
			
			$('.contents_area .exportCssLnk').click(function(e)
			{
				e.preventDefault();
				with(this)
				{
					if (adcSetting.getAdc().type == "PAS")
					{
						//alert(VAR_COMMON_PASNOTSUPPORT);
						return;
					}
					else if (adcSetting.getAdc().type == "PASK")
					{
						//alert(VAR_COMMON_PASKNOTSUPPORT);
						return;
					}
					else
					{
						checkExportL2InfoExist();
					}
				}			
			});
			
			$('.contents_area #searchTxt').keydown(function(e)
			{				
				if (e.which != 13)
				{
					return;
				}
				with(this)
				{
					if (adcSetting.getAdc().type == "PAS")
					{
						//alert(VAR_COMMON_PASNOTSUPPORT);
						return;
					}
					else if (adcSetting.getAdc().type == "PASK")
					{
						//alert(VAR_COMMON_PASKNOTSUPPORT);
						return;
					}
					else
					{
						searchFlag=true;
						GenerateSearchKey();
					}
				}
			});
			
			if (adcSetting.getAdcStatus() != "available" || adcSetting.getAdc().mode == 1) 
			{
				$('input[name="IPchk"]').attr("disabled", "disabled");
				$('input[name="MACchk"]').attr("disabled", "disabled");
				$('input[name="VLANchk"]').attr("disabled", "disabled");
				$('input[name="IFchk"]').attr("disabled", "disabled");	
				
				$('.searchNotMsg').addClass("none");
				$('.dataNotExistMsg').addClass("none");
				$('.imgOff').removeClass("none");
				$('.imgOn').addClass("none");
				
				if ($('.l2List').size() > 0 )
				{
					$('.disabledChk').removeClass("none");
					$('.nulldataMsg').addClass("none");
				}
				else
				{
					$('.disabledChk').addClass("none");
					$('.nulldataMsg').removeClass("none");
				}
			}
		}
	},
	registerListContentsEvents : function()
	{
		with (this)
		{			
			if(this.searchFlag == true)
			{
				$('.nulldataMsg').addClass("none");
				$('.dataNotExistMsg').addClass("none");
				if($('.l2List').size() > 0)
				{
					$('.searchNotMsg').addClass("none");
				}
				else
				{
					$('.searchNotMsg').removeClass("none");
				}
				searchFlag=false;
			}
			else
			{
				$('.nulldataMsg').addClass("none");
				$('.searchNotMsg').addClass("none");
				if($('.l2List').size() > 0)
				{
					$('.dataNotExistMsg').addClass("none");
				}
				else
				{
					$('.dataNotExistMsg').removeClass("none");
				}
			}
			
			$('.orderDir_Desc').click(function(e)
			{
				e.preventDefault();
				orderDir =  $(this).find('.orderDir').text();
				orderType =  $(this).find('.orderType').text();
				if (adcSetting.getAdc().type == "PAS")
				{
					//alert(VAR_COMMON_PASNOTSUPPORT);
					return;
				}
				else if (adcSetting.getAdc().type == "PASK")
				{
					//alert(VAR_COMMON_PASKNOTSUPPORT);
					return;
				}
				else
				{
					searchFlag = true;
					loadL2InfoTableInListContent(undefined, undefined, undefined, orderType, orderDir);
				}
			});
			
			$('.orderDir_Asc').click(function(e)
			{
				e.preventDefault();
				orderDir =  $(this).find('.orderDir').text();
				orderType =  $(this).find('.orderType').text();		
				if (adcSetting.getAdc().type == "PAS")
				{
					//alert(VAR_COMMON_PASNOTSUPPORT);
					return;
				}
				else if (adcSetting.getAdc().type == "PASK")
				{
					//alert(VAR_COMMON_PASKNOTSUPPORT);
					return;
				}
				else
				{
					searchFlag = true;
					loadL2InfoTableInListContent(undefined, undefined, undefined, orderType, orderDir);
				}
			});
			
			$('.orderDir_None').click(function(e)
			{
				e.preventDefault();
				orderDir =  $(this).find('.orderDir').text();
				orderType =  $(this).find('.orderType').text();
				if (adcSetting.getAdc().type == "PAS")
				{
					//alert(VAR_COMMON_PASNOTSUPPORT);
					return;
				}
				else if (adcSetting.getAdc().type == "PASK")
				{
					//alert(VAR_COMMON_PASKNOTSUPPORT);
					return;
				}
				else
				{
					searchFlag = true;
					loadL2InfoTableInListContent(undefined, undefined, undefined, orderType, orderDir);
				}
			});
		}
	},
	checkExportL2InfoExist : function()
	{
		with(this)
		{
			ajaxManager.runJsonExt({
				url : "monitor/checkExportL2InfoExist.action",
				data : 
				{
					"adcObject.category"	: adcSetting.getSelectIndex(),
					"adcObject.index" 		: getIndex(adcSetting.getSelectIndex()),
					"adcObject.name"		: adcSetting.getGroupName(),
					"adc.type" 				: adcSetting.getAdc().type,
					"adc.name" 				: adcSetting.getAdc().name,					
					"searchObj.beginIndex" 	: pageNavi.getFirstRowOfCurrentPage(),
					"searchObj.endIndex" 	: pageNavi.getLastRowOfCurrentPage(),
					"orderObj.orderDirection" 	: orderDir,
					"orderObj.orderType" 		: orderType
				},
				successFn : function(data)
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
	exportCss : function() 
	{
		with (this) 
		{
			var params = "adcObject.category=" + adcSetting.getSelectIndex() + "&adcObject.index=" + getIndex(adcSetting.getSelectIndex()) +
			"&adcObject.name=" + adcSetting.getGroupName() + "&adc.type=" + adcSetting.getAdc().type + "&adc.name=" + adcSetting.getAdc().name +
			"&searchObj.beginIndex=" + pageNavi.getFirstRowOfCurrentPage() + "&searchObj.endIndex=" + pageNavi.getLastRowOfCurrentPage() +
			"&orderObj.orderDirection=" + orderDir + "&orderObj.orderType=" + orderType;
			var url = "monitor/downloadL2Info.action?" + params;
			$('#downloadFrame').attr('src',url);	
		}
	},	
	// from GS. #4012-1 #6, #3984-4 #3: 14.07.29 sw.jung 유효성 검사 개선
	validateL2SearchKey : function()
	{
		return $.validate
			([
			  	{
			  		target: $('input[name=searchKey_IP]'),
			  		name: "IP",
			  		checked: $('.IPchk').is(':checked'),
			  		type: "ip_part"
			  	},
			  	{
			  		target: $('input[name=searchKey_MAC]'),
			  		name: "MAC",
			  		checked: $('.MACchk').is(':checked'),
			  		type: "mac_part"
			  	},
			  	{
			  		target: $('input[name=searchKey_VLAN]'),
			  		name: "VLAN",
			  		checked: $('.VLANchk').is(':checked'),
			  		type: "name"
			  	},
			  	{
			  		target: $('input[name=searchKey_Interface]'),
			  		name: "PORT",
			  		checked: $('.IFchk').is(':checked'),
			  		type: "name"
			  	}
			  ]);
	},
	// 검색 Validate
//	validateL2SearchKey : function()
//	{
//		with(this)
//		{
//			var searchKey_IP = $('input[name=searchKey_IP]').val();
//			var searchKey_MAC = $('input[name=searchKey_MAC]').val();
//			var searchKey_VLAN = $('input[name=searchKey_VLAN]').val();
//			var searchKey_Interface = $('input[name=searchKey_Interface]').val();
//			
//			if (searchKey_IP != "")
//			{
//				if (!getValidateSearchIP(searchKey_IP))
//				{
//					alert(VAR_COMMON_IPFORMAT);				// The IP address format is not valid.
//					return false;
//				}
//			}	
//			
//			if (searchKey_MAC != "")
//			{
//				if (!getValidateMac(searchKey_MAC))
//				{
//					alert(VAR_COMMON_MACFORMAT);			// The MAC format is not vaild.
//					return false;
//				}
//			}
//			
//			if (searchKey_VLAN != "")
//			{
//				if (!getValidateStringint(searchKey_VLAN, 1, 64))
//				{
//					alert(VAR_COMMON_SPECIALCHAR);			// Special characters cannot be entered. 
//					return false;
//				}
//			}
//			
//			if (searchKey_Interface != "")
//			{
//				if (!isValidStringLength(searchKey_Interface, 1, 64))
//				{
//					var data = VAR_COMMON_LENGTHFORMAT+"("+1+"~"+64+")";
//					alert(data);
//					$('.Board input.searchTxt').val('');
//					return false;
//				}
//
//				if (!isExistSpecialCharacter(searchKey_Interface))
//				{
//					alert(VAR_FAULTSETTING_SPECIALCHAR);
//					$('.Board input.searchTxt').val('');
//					return false;
//				}	
//				
////				if (!getValidateInterfacePort(searchKey_Interface))
////				{
////					alert(VAR_COMMON_NUMRANGE);				// The port that you entered is out of range. The permitted range is from 1 to 65535.
////					return false;
////				}
//			}
//			
//			return true;
//		}
//	},
	// 검색 Key 만드는 기능
	GenerateSearchKey : function()
	{
		with(this)
		{
			if(!validateL2SearchKey())
			{
				return false;
			}
			var l2SearchKey = [];
			if ($('.IPchk').is(':checked'))
			{
				var dataObjectIP =
				{
						content : $('input[name=searchKey_IP]').val(),
						type : 1
				};				
				l2SearchKey.push(dataObjectIP);
			};
			
			if ($('.MACchk').is(':checked'))
			{
				var dataObjectMAC =
				{
					content : $('input[name=searchKey_MAC]').val(),
					type : 2
				};				
				l2SearchKey.push(dataObjectMAC);
			};
			
			if ($('.VLANchk').is(':checked'))
			{
				var dataObjectVLAN =
				{
					content : $('input[name=searchKey_VLAN]').val(),
					type : 3
				};				
				l2SearchKey.push(dataObjectVLAN);
			};
			
			if ($('.IFchk').is(':checked'))
			{
				var dataObjectIF =
				{
					content : $('input[name=searchKey_Interface]').val(),
					type : 4
				};
				l2SearchKey.push(dataObjectIF);
			};
			
			l2SearchOption = l2SearchKey;	
			setL2InfoListToDB(l2SearchOption, null, null);
		}
	},
	noticePageInfo : function()
	{
		with(this)
		{
			var currentPage = pageNavi.getCurrentPage();
			var lastPage = pageNavi.getLastPage();
			var countTotal = pageNavi.getRowTotal();
			var targetCntHtml = $('.noticePageCountInfo').filter(':last');
			var targetPageHtml = $('.noticePageInfo').filter(':last');
			targetCntHtml.html(addThousandSeparatorCommas(countTotal));
			targetPageHtml.html("(" + addThousandSeparatorCommas(currentPage) + "/" + addThousandSeparatorCommas(lastPage) + VAR_COMMON_PAGE + ")");
		}
	}
});