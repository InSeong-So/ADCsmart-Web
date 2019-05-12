// @author: Hakmin Lee

var ClassVirtualServer = Class.create({
	initialize : function()
	{
//		var fn = this;
		this.activeAdcInfo = {};
		this.searchedOption = 
		{
			"key" : undefined,
			"fromPeriod" : undefined,
			"toPeriod" : undefined,
			"orderType" : 1,//
			"orderDir" : 1,// 1는 오름차순
			"fromRow" : undefined,
			"toRow" : undefined
		};	
		
		this.alteonVs = new AlteonVs();
		this.f5Vs = new F5Vs();
		this.pasVs = new PASVs();
		this.paskVs = new PASKVs();
		this.searchedKey = undefined;
		this.pageNavi = new PageNavigator();
		this.pageNavi.onChange(function(fromRow, toRow, orderType, orderDir)
		{
			FlowitUtil.log('fromRow: %s, toRow: %s, orderType: %s, orderDir: %s', fromRow, toRow, orderType, orderDir);
			this.searchedOption.fromRow 	= fromRow;
			this.searchedOption.toRow 		= toRow;
			this.searchedOption.orderType 	= orderType;
			this.searchedOption.orderDir 	= orderDir;
			this._loadVirtualSvrTableInListContent(this.searchedOption.key, this.searchedOption.fromRow, this.searchedOption.toRow, this.searchedOption.orderType, this.searchedOption.orderDir);
		});
	},
	registerLeftEvents : function()
	{
		with (this)
		{
			$('.snb_tree:last p').click(function(e)
			{
				clickTreeItem.call(this);
			});
			
			// level 2 click event
			$('ul.snb_tree:last .depth2 > li > p').click(function(e)
			{
				FlowitUtil.log($(this).text());
			});
		}
	},
	clickTreeItem : function()
	{
		var $selectItem = $(this);
		var $sub_menu = $selectItem.nextAll('ul');
		// tree메뉴 초기화 함수 
		initTree($selectItem);
		
		if($sub_menu.length == 0)
		{
			$selectItem.addClass('on');
		}
		else if($sub_menu.css('display') == 'none')
		{
			$selectItem.addClass('on');
			$sub_menu.slideDown(200);
		}
		else
		{
			$sub_menu.slideUp(200);
		}
	},
	_loadDefaultMainContent : function() 
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
	loadListContent : function(activeAdcInfo, refreshes)
	{
		this.activeAdcInfo=activeAdcInfo;
		with (this)
		{
			if (!activeAdcInfo || activeAdcInfo.categoryIndex!=2)
			{// adc가 선택되어 있지 않으면 작업을 수행하지 않는다.
				_loadDefaultMainContent();
			}

			if(!_validateDaterefresh())
			{
				return false;
			}
			if(activeAdcInfo.index === undefined)
			{
				_loadDefaultMainContent();
				return;
			}				
			var rowTotal = 0;
			ajaxManager.runJsonExt({
				url : "virtualServer/retrieveVirtualSvrTotal.action",
				data :
				{
					"adc.index" : activeAdcInfo.index,
					"adc.name" : activeAdcInfo.name,
					"adc.type" : activeAdcInfo.type,
					"searchKey" : searchedOption.key
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
					FlowitUtil.log("row total is %s: ", rowTotal);
					pageNavi.updateRowTotal(rowTotal, orderType);
					_loadVirtualSvrListContent(refreshes);					
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_VS_VSECEFAIL, jqXhr);
//					exceptionEvent();
				}	
			});
		}
	},
	_loadVirtualSvrListContent : function(refreshFlag)
	{
		with (this)
		{
			var vsListContentURL="";
			if (activeAdcInfo.type === 'F5')
			{
				vsListContentURL = "virtualServer/loadF5VsListContent.action";
			}
			else if (activeAdcInfo.type === 'Alteon')
			{
				vsListContentURL = "virtualServer/loadAlteonVsListContent.action";
			}
			else if (activeAdcInfo.type === 'PAS')
			{
				vsListContentURL = "virtualServer/loadPASVsListContent.action";
			}
			else if (activeAdcInfo.type === 'PASK')
			{
				vsListContentURL = "virtualServer/loadPASKVsListContent.action";
			}
			else if (activeAdcInfo.type === 'PiolinkUnknown')
			{
				vsListContentURL = "virtualServer/loadPASVsListContent.action";
			}
			
			ajaxManager.runJsonExt({
				url : "virtualServer/loadRefreshListContent.action",
				data :
				{
					"adc.index" : activeAdcInfo.index,
					"adc.name" 	: activeAdcInfo.name,
					"adc.type" 	: activeAdcInfo.type,
					"refreshes" : !!refreshFlag,
					"searchKey" : searchKey,
					"orderDir" 	: searchedOption.orderDir,
					"orderType" : searchedOption.orderType,
					"adc.status": activeAdcInfo.status
				},
				successFn : function(data)
				{					
					if (!data.isSuccessful)
					{
						$.obAlertNotice(data.message);
						return;
					}
					
					ajaxManager.runHtmlExt({
						url : vsListContentURL,
						data :
						{
							"adc.index" : activeAdcInfo.index,
							"adc.name" : activeAdcInfo.name,
							"adc.type" : activeAdcInfo.type,
							"fromRow" : fromRow === undefined ? pageNavi.getFirstRowOfCurrentPage() : fromRow,
							"toRow" : toRow === undefined ? pageNavi.getLastRowOfCurrentPage() : toRow,
							"searchKey" : searchKey,
							"refreshes" : !!refreshFlag,
							"orderDir" : searchedOption.orderDir,
							"orderType" : searchedOption.orderType
						},
						target: "#wrap .contents",
						successFn : function(params) 
						{
//							header.setActiveMenu('AdcSetting');
//							searchedKey = searchKey;
							_applyListContentCss();
							_registerListContentEvents();
							
						},
						completeFn : function()
						{
							pageNavi.refresh();
						},
						errorFn : function(jqXhr)
						{
							$.obAlertAjaxError(VAR_VS_VSELFAIL, jqXhr);
						}
					});				
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_VS_VSECEFAIL, jqXhr);
//					exceptionEvent();
				}	
			});
		}
	},
	_loadVirtualSvrTableInListContent : function(refreshes)
	{
		with (this)
		{			
			var vsTableInListContentURL="";
			
			if (activeAdcInfo.type === 'F5')
			{
				vsTableInListContentURL = "virtualServer/loadVirtualSvrTableInF5ListContent.action";
			}
			else if (activeAdcInfo.type === 'Alteon')
			{
				vsTableInListContentURL = "virtualServer/loadVirtualSvrTableInAlteonListContent.action";
			}
			else if (activeAdcInfo.type === 'PAS')
			{
				vsTableInListContentURL = "virtualServer/loadVirtualSvrTableInPASListContent.action";
			}
			else if (activeAdcInfo.type === 'PASK')
			{
				vsTableInListContentURL = "virtualServer/loadVirtualSvrTableInPASKListContent.action";
			}
			else if (activeAdcInfo.type === 'PiolinkUnknown')
			{
				vsTableInListContentURL = "virtualServer/loadVirtualSvrTableInPASListContent.action";
			}
//			FlowitUtil.log("searchKey:%s, fromRow:%s, toRow:%s, refreshes:%s", searchKey, fromRow, toRow, refreshes);
			ajaxManager.runHtmlExt({
				url : vsTableInListContentURL,
				data :
				{
					"adc.index" : activeAdcInfo.index,
					"adc.name" 	: activeAdcInfo.name,
					"adc.type" 	: activeAdcInfo.type,
					"fromRow" 	: searchedOption.fromRow === undefined ? pageNavi.getFirstRowOfCurrentPage() : searchedOption.fromRow,
					"toRow" 	: searchedOption.toRow === undefined ? pageNavi.getLastRowOfCurrentPage() : searchedOption.toRow,
					"searchKey" : searchedOption.key,
					"refreshes" : !!refreshes,
					"orderDir" 	: searchedOption.orderDir,
					"orderType" : searchedOption.orderType
				},
				target: "table.virtualSvrTable",
				successFn : function(params)
				{
					_applyListContentCss();
					_registerListContentEvents();
				},
				completeFn : function()
				{
					pageNavi.refresh();
				},
				errorFn : function(a,b,c)
				{
//					exceptionEvent();
				}	
			});
		}
	},
	_applyListContentCss : function()
	{
		// 테이블 컬럼 정렬
		initTable([ "#statTable tbody tr" ], [ 3, 4 ], [ -1 ]);
	},
	_saveSearchOptions : function($p)
	{
		with (this)
		{
			if ($p.length == 0)
				return;			
			
			searchedOption.key = $('input[name="textfield3"]').val();
			searchedOption.orderType = $p.find('.orderDir').text();
			searchedOption.orderDir = $p.find('.orderType').text();
		}
	},
	_resetSearchOptionsSearchKey : function()
	{
		with (this)
		{
			searchedOption.key = '';
		}
	},
	_registerListContentEvents : function()
	{
		with (this)
		{
			$('.orderDir_Desc').click(function(e)
			{
				e.preventDefault();
				_saveSearchOptions($(this));
				loadListContent(activeAdcInfo, false);
			});
			
			$('.orderDir_Asc').click(function(e)
			{
				e.preventDefault();
				_saveSearchOptions($(this));
				loadListContent(activeAdcInfo, false);
			});
			
			$('.orderDir_None').click(function(e)
			{
				e.preventDefault();
				_saveSearchOptions($(this));
				loadListContent(activeAdcInfo, false);
			});
			
			$('.profileLnk').click(function(e)
			{
				e.preventDefault();
				profile.loadProfileListContent();
			});
		
			$('.addVirtualServerLnk').click(function(e)
			{
				if (activeAdcInfo.type == 'F5')
				{
					f5Vs.loadVsAddContent();
				}
				else if (activeAdcInfo.type == 'Alteon')
				{
					alteonVs.loadVsAddContent();
				}
				else if (activeAdcInfo.type == 'PAS')
				{
					pasVs.loadVsAddContent();
				}
				else if (activeAdcInfo.type == 'PASK')
				{
					paskVs.loadVsAddContent();
				}
				else if (activeAdcInfo.type == 'PiolinkUnknown')
				{
					pasVs.loadVsAddContent();
				}
			});
			
			$('.modifyVirtualServerLnk').click(function(e)
			{			
				if (activeAdcInfo.type == 'F5')
				{
					f5Vs.loadVsModifyContent($(this).parent().parent().find('.serverChk').val());
				}
				else if (activeAdcInfo.type == 'Alteon')
				{
					alteonVs.loadVsModifyContent($(this).parent().parent().find('.serverChk').val());
				}
				else if (activeAdcInfo.type == 'PAS')
				{
					pasVs.loadVsModifyContent($(this).parent().parent().find('.serverChk').val());
				}
				else if (activeAdcInfo.type == 'PASK')
				{
					paskVs.loadVsModifyContent($(this).parent().parent().find('.serverChk').val());
				}
				else if (activeAdcInfo.type == 'PiolinkUnknown')
				{
					pasVs.loadVsModifyContent($(this).parent().parent().find('.serverChk').val());
				}
			});
			
			$('.refreshLnk').click(function(e)
			{
				e.preventDefault();
				_resetSearchOptionsSearchKey();
				loadListContent(activeAdcInfo, true);
			});
			
			$('.allServersChk').click(function(e)
			{
				var isChecked = $(this).is(':checked');
	//			$(this).parent().parent().parent().parent().find('.serverChk').attr('checked', isChecked);
				$(this).parent().parent().parent().parent().find('.serverChk').attr('checked', isChecked);
			});
			
			$('.enableVssLnk').off('click'); // 중복 이벤트 방지를 위한 등록 해제 
			$('.enableVssLnk').click(function(e)
			{
				_enableServers();
			});
			$('.disableVssLnk').off('click');  // 중복 이벤트 방지를 위한 등록 해제
			$('.disableVssLnk').click(function(e)
			{
				_disableServers();
			});
			
			$('.delVssLnk').off('click');  // 중복 이벤트 방지를 위한 등록 해제
			$('.delVssLnk').click(function(e)
			{	
				_delServers();
			});
			
			$('.onGoToMonitoring').click(function(e)
			{
				$('.monitorNetworkMnu').click();
			});
			
			// search event
			$('.btn a.searchLnk').click(function (e)
			{
				e.preventDefault();
				_saveSearchOptions($(this));
//				var searchKey = $('input[name="searchKey"]').val();aaaaa
//				FlowitUtil.log('searchKey:' + searchKey);
				loadListContent(activeAdcInfo, false);
			});
			
			$('.inputTextposition input.searchTxt').keydown(function(e)
			{
				if (e.which != 13)
					return;
				_saveSearchOptions($(this));
//				var searchKey = $(this).val();
//				FlowitUtil.log('searchKey:' + searchKey);
				loadListContent(activeAdcInfo, false);
			});
			
			if (activeAdcInfo.adcStatus != "available")
			{
				$('input[name="searchKey"]').attr("disabled", "disabled");
				
				$('.imageChange').attr("src", "imgs/meun/btn_search_1_off.png");				
				$('.imgOff').removeClass("none");
				$('.imgOn').addClass("none");
				
				$('.allServersChk').attr("disabled", "disabled");
				$('.serverChk').attr("disabled", "disabled");
				
				if ($('.virtualServerList').size() > 0 )
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
	_enableServers : function()
	{
		with (this)
		{
			var checkedServers = _getCheckedServers();
			FlowitUtil.log(checkedServers);
			if (checkedServers.length == 0)
			{
				$.obAlertNotice(VAR_VS_ENAVSELE);
				return;
			}
				
			ajaxManager.runHtmlExt({
				url : "virtualServer/enableVss.action",
				data :
				{
					"adc.index" 		: activeAdcInfo.index,
					"adc.type" 			: activeAdcInfo.type,
					"adc.name" 			: activeAdcInfo.name,
					"virtualSvrIndices" : checkedServers,
					"orderDir" 			: searchedOption.orderDir,
					"orderType" 		: searchedOption.orderType
				},
				target : "#wrap .contents_area",
				successFn : function(params)
				{
					_loadVirtualSvrTableInListContent(false);
					_enableServersPeer(checkedServers);
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_VS_ENAFAIL, jqXhr);
//					exceptionEvent();
				}	
			});
		}
	},
	_enableServersPeer : function(checkedServers)
	{
		with (this)
		{
//			FlowitUtil.log("index: %s", activeAdcInfo.index);
			ajaxManager.runJsonExt({
				url : "virtualServer/checkPairIndex.action",
				data :
				{
					"adc.index" : activeAdcInfo.index,
					"adc.name" 	: adcSactiveAdcInfo.name,
					"adc.type" 	: activeAdcInfo.type,
					"orderDir" 	: searchedOption.orderDir,
					"orderType" : searchedOption.orderType
				},
				successFn : function(data)
				{
					if (data.pairIndex!='' && data.pairIndex!=null && data.pairIndex>0) 
					{
						var chk = confirm(data.message);
//						var chk = confirm("ADC Config 설정에 성공했습니다. \nADC 장비에 Peer 장비가 등록되어 있습니다. 동기화 하시겠습니까?");
						if (chk)
						{
							ajaxManager.runJsonExt({
								url : "virtualServer/isExistVirtualServer.action",
								data :
								{
									"pairIndex" : data.pairIndex,
									"adc.index" : adcSactiveAdcInfo.index,
									"adc.name" 	: adcSactiveAdcInfo.name,
									"adc.type" 	: adcSactiveAdcInfo.type,
									"virtualSvrIndices" : checkedServers,
									"orderDir" 	: searchedOption.orderDir,
									"orderType" : searchedOption.orderType									
								},
								successFn : function(data)
								{								
									if(data.isExistVSIndex==0)
									{// 동일한 설정이 없는 경우
										$.obAlertNotice(data.message);
										return;
									}
									
									// disable 작업을 진행한다.
									ajaxManager.runHtmlExt({
										url : "virtualServer/enableVssPeer.action",
										data :
										{
											"adc.index" 		: adcSactiveAdcInfo.index,
											"adc.type" 			: adcSactiveAdcInfo.type,
											"adc.name" 			: adcSactiveAdcInfo.name,
											"pairIndex" 		: data.pairIndex,
											"virtualSvrIndices" : checkedServers,
											"orderDir" 			: searchedOption.orderDir,
											"orderType" 		: searchedOption.orderType											
										},
										target: "#wrap .contents_area",
										successFn : function(params)
										{
											_loadVirtualSvrTableInListContent(false);
											return;
										},
										errorFn : function(jqXhr)
										{
											$.obAlertAjaxError(VAR_VS_PEEVEFAIL, jqXhr);
//											exceptionEvent();
										}
									});
								},
								errorFn : function(jqXhr)
								{
									$.obAlertAjaxError(VAR_COMMON_PEIFAIL, jqXhr);
//									exceptionEvent();
								}	
							});
						}
					}
					_loadVirtualSvrTableInListContent(false);
					return;
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_COMMON_PEIFAIL, jqXhr);
//					exceptionEvent();
				}	
			});
		}
	},
	_getCheckedServers : function()
	{
		var servers = $('.serverChk').filter(':checked').map(function() {
			return $(this).val();
		}).get();
		FlowitUtil.log(Object.toJSON(servers));
		return servers;
	},
	_disableServers : function()
	{
		with (this)
		{
			var checkedServers = _getCheckedServers();
			FlowitUtil.log(checkedServers);
			if (checkedServers.length == 0)
			{
				$.obAlertNotice(VAR_VS_DISVSEL);
				return;
			}
				
			ajaxManager.runHtmlExt({
				url : "virtualServer/disableVss.action",
				data :
				{
					"adc.index" 		: adcSactiveAdcInfo.index,
					"adc.type" 			: adcSactiveAdcInfo.type,
					"adc.name" 			: adcSactiveAdcInfo.name,
					"virtualSvrIndices" : checkedServers,
					"orderDir" 			: searchedOption.orderDir,
					"orderType" 		: searchedOption.orderType
				},
				target: "#wrap .contents_area",
				successFn : function(params)
				{
					_loadVirtualSvrTableInListContent(false);
					_disableServersPeer(checkedServers);
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_VS_VSEDFAIL, jqXhr);
//					exceptionEvent();
				}	
			});
		}
	},	
	_delServers : function()
	{
		with (this)
		{
			var checkedServers = _getCheckedServers();
			FlowitUtil.log(checkedServers);
			if (checkedServers.length == 0)
			{
				$.obAlertNotice(VAR_VS_VSEDSEL);
				return;
			}			

			var chk = confirm(VAR_VS_VSEDEL);
			if (chk == false)
			{
				return;
			}

			ajaxManager.runHtmlExt({
				url : "virtualServer/delVss.action",
				data :
				{
					"adc.index" 		: activeAdcInfo.index,
					"adc.type" 			: activeAdcInfo.type,
					"adc.name" 			: activeAdcInfo.name,
					"virtualSvrIndices" : checkedServers,
					"orderDir" 			: searchedOption.orderDir,
					"orderType" 		: searchedOption.orderType
				},
				target: "#wrap .contents_area",
				successFn : function(params)
				{
					_loadVirtualSvrListContent(false);
					_delServersPeer(checkedServers);
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_VS_VIRDFAIL, jqXhr);
//					exceptionEvent();
				}	
			});
		}
	},
	_delServersPeer : function(checkedServers)
	{
		with (this)
		{
//			if (!adcSetting.isAdcSet())
//			{
//				$('#wrap .contents_area').empty();
//				return;
//			}
//			
//			FlowitUtil.log("index: %s", activeAdcInfo.index);
			ajaxManager.runJsonExt({
				url : "virtualServer/checkPairIndex.action",
				data :
				{
					"adc.index" : activeAdcInfo.index,
					"adc.name" 	: activeAdcInfo.name,
					"adc.type" 	: activeAdcInfo.type,
					"orderDir" 	: searchedOption.orderDir,
					"orderType" : searchedOption.orderType
				},
				successFn : function(data)
				{
					if (data.pairIndex!='' && data.pairIndex!=null && data.pairIndex>0) 
					{
						var chk = confirm(data.message);
//						var chk = confirm("ADC Config 설정에 성공했습니다. \nADC 장비에 Peer 장비가 등록되어 있습니다. 동기화 하시겠습니까?");
						if (chk)
						{
							ajaxManager.runJsonExt({
								url : "virtualServer/isExistVirtualServer.action",
								data :
								{
									"pairIndex" 		: data.pairIndex,
									"adc.index" 		: activeAdcInfo.index,
									"adc.name" 			: activeAdcInfo.name,
									"adc.type" 			: activeAdcInfo.type,
									"virtualSvrIndices" : checkedServers,
									"orderDir" 			: searchedOption.orderDir,
									"orderType" 		: searchedOption.orderType
								},
								successFn : function(data)
								{								
									if(data.isExistVSIndex==0)
									{// 동일한 설정이 없는 경우
										$.obAlertNotice(data.message);
										return;
									}
									
									// disable 작업을 진행한다.
									ajaxManager.runHtmlExt({
										url : "virtualServer/delVssPeer.action",
										data :
										{
											"adc.index" 		: activeAdcInfo.index,
											"adc.type" 			: activeAdcInfo.type,
											"adc.name" 			: activeAdcInfo.name,
											"pairIndex" 		: data.pairIndex,
											"virtualSvrIndices" : checkedServers,
											"orderDir" 			: searchedOption.orderDir,
											"orderType" 		: searchedOption.orderType
										},
										target: "#wrap .contents_area",
										successFn : function(params)
										{
//											loadVirtualSvrListContent(searchedKey, undefined, undefined, false);
											loadListContent(false);
											return;
										},
										errorFn : function(jqXhr)
										{
											$.obAlertAjaxError(VAR_VS_PEEVDFAIL, jqXhr);
//											exceptionEvent();
										}
									});
								},
								errorFn : function(jqXhr)
								{
									$.obAlertAjaxError(VAR_COMMON_PEIFAIL, jqXhr);
//									exceptionEvent();
								}	
							});
						}
					}
					loadListContent(false);
					return;
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_COMMON_PEIFAIL, jqXhr);
//					exceptionEvent();
				}	
			});
		}
	},
	loadVsModifyContent : function(index)
	{
		with (this)
		{
			if (activeAdcInfo.type === 'F5')
			{
				f5Vs.loadVsModifyContent(index);
			}
			else if (activeAdcInfo.type === 'Alteon')
			{
				alteonVs.loadVsModifyContent(index);
			}
			else if (activeAdcInfo.type === 'PAS')
			{
				pasVs.loadVsModifyContent(index);
			}
			else if (activeAdcInfo.type === 'PASK')
			{
				paskVs.loadVsModifyContent(index);
			}
			else if (activeAdcInfo.type === 'PiolinkUnknown')
			{
				pasVs.loadVsModifyContent(index);
			}
		}
	},
	_validateDaterefresh : function()
	{
		if(($('.Board input.searchTxt').val() != "") && ($('.Board input.searchTxt').val() != null))
		{
			var search = $('.Board input.searchTxt').val();
			if(getValidateStringint(search, 1, 64) == false)
			{
				$.obAlertNotice(VAR_FAULTSETTING_SPECIALCHAR);
				$('.Board input.searchTxt').val('');
				return false;
			}			
		}
		return true;		
	},
	isValidVirtualSvrName : function(virtualSvrName) //alert(" 2 - virtualSvrName : " + virtualSvrName);
	{ 
		return /^[a-zA-Z](\w|-|\.|\*|\/|\:|\?|\=|\@|\,|\&)*$/.test(virtualSvrName);
	},
	_disableServersPeer : function(checkedServers, orderDir, orderType)
	{
		with (this)
		{
			if (!adcSetting.isAdcSet())
			{
				$('#wrap .contents_area').empty();
				return;
			}
			
			FlowitUtil.log("index: %s", activeAdcInfo.index);
			ajaxManager.runJsonExt({
				url : "virtualServer/checkPairIndex.action",
				data :
				{
					"adc.index" : activeAdcInfo.index,
					"adc.name" : activeAdcInfo.name,
					"adc.type" : activeAdcInfo.type,
					"orderDir" : orderDir,
					"orderType" : orderType
				},
				successFn : function(data)
				{
					if (data.pairIndex!='' && data.pairIndex!=null && data.pairIndex>0) 
					{
						var chk = confirm(data.message);
//						var chk = confirm("ADC Config 설정에 성공했습니다. \nADC 장비에 Peer 장비가 등록되어 있습니다. 동기화 하시겠습니까?");
						if (chk)
						{
							ajaxManager.runJsonExt({
								url : "virtualServer/isExistVirtualServer.action",
								data :
								{
									"pairIndex" 		: data.pairIndex,
									"adc.index" 		: activeAdcInfo.index,
									"adc.name" 			: activeAdcInfo.name,
									"adc.type" 			: activeAdcInfo.type,
									"virtualSvrIndices" : checkedServers,
									"orderDir" 			: searchedOption.orderDir,
									"orderType" 		: searchedOption.orderType
								},
								successFn : function(data)
								{								
									if(data.isExistVSIndex==0)
									{// 동일한 설정이 없는 경우
										$.obAlertNotice(data.message);
										return;
									}
									
									// disable 작업을 진행한다.
									ajaxManager.runHtmlExt({
										url : "virtualServer/disableVssPeer.action",
										data :
										{
											"adc.index" 		: activeAdcInfo.index,
											"adc.type" 			: activeAdcInfo.type,
											"adc.name" 			: activeAdcInfo.name,
											"pairIndex" 		: data.pairIndex,
											"virtualSvrIndices" : checkedServers,
											"orderDir" 			: searchedOption.orderDir,
											"orderType" 		: searchedOption.orderType
										},
										target: "#wrap .contents_area",
										successFn : function(params)
										{
											_loadVirtualSvrTableInListContent(false);
											return;
										},
										errorFn : function(jqXhr)
										{
											$.obAlertAjaxError(VAR_VS_PEEVDIFAIL, jqXhr);
//											exceptionEvent();
										}
									});
								},
								errorFn : function(jqXhr)
								{
									$.obAlertAjaxError(VAR_COMMON_PEIFAIL, jqXhr);
//									exceptionEvent();
								}	
							});
						}
					}
					_loadVirtualSvrTableInListContent(false);
					return;
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_COMMON_PEIFAIL, jqXhr);
					//$.obAlertAjaxError("main error", jqXhr);
//					exceptionEvent();
				}	
			});
		}
	},
});