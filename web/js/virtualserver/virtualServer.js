// @author: Hakmin Lee

var VirtualServer = Class.create({
	initialize : function()
	{
		var fn = this;
		this.adc = {};
		this.alteonVs = new AlteonVs();
		this.f5Vs = new F5Vs();
		this.pasVs = new PASVs();
		this.paskVs = new PASKVs();
		this.searchedKey = undefined;
		this.orderDir  = 2; //오름차순
		this.orderType = 40;//bps total
		this.lastselectedPageNumber = undefined;
		this.rowTotal = undefined;
		this.searchFlag = false;
		this.target = {};
		this.selectedVsIndexList = undefined;
		this.pageNavi = new PageNavigator();
		this.pageNavi.onChange(function(fromRow, toRow, orderType, orderDir)
		{
			FlowitUtil.log('fromRow: %s, toRow: %s, orderType: %s, orderDir: %s', fromRow, toRow, orderType, orderDir);
			fn.loadVirtualSvrTableInListContent(fn.searchedKey, fromRow, toRow, orderType, orderDir);
			fn.lastselectedPageNumber = fn.pageNavi.getCurrentPage();
		});
	},
	setTarget : function()
	{
		var grpIndex = adcSetting.getGroupIndex();
		var adcIndex = adcSetting.adc.index;
		if(grpIndex === undefined && adcIndex === undefined)
		{
			this.target.level = 0;
			this.target.index = undefined;
		}
		else if(grpIndex == 0)
		{
			this.target.level = grpIndex;
			this.target.index = undefined;
		}
		else if(grpIndex === undefined && adcIndex != null)
		{
			this.target.level = 2;
			this.target.index = adcIndex;
		}
		else
		{
			this.target.level = 1;
			this.target.index = grpIndex;
		}
	},
	onAdcChange : function()
	{
		this.orderDir  = 1; //오름차순
		this.orderType = 1;// ADC이름
		virtualServer.loadListContent();
	},
	getActiveContent : function()
	{
		return adcSetting.activeContent;
	},
	setActiveContent : function(content)
	{
		adcSetting.activeContent = content;
	},
	isActiveContent : function(content)
	{
		return adcSetting.activeContent == content;
	}, 
	setAdc : function(adcIndex, adcType, adcName)
	{
		this.adc.index = adcIndex;
		this.adc.type = adcType;
		this.adc.name = adcName;
		this.alteonVs.setAdc(adcIndex, adcType, adcName);
		this.f5Vs.setAdc(adcIndex, adcType, adcName);
		this.pasVs.setAdc(adcIndex, adcType, adcName);
	},	
	registerLeftEvents : function()
	{
		with (this)
		{
		// SNB Tree 클릭 이벤트
		$('.snb_tree:last p').click(function(e)
		{
			clickTreeItem.call(this);
		});
		
		// level 2 click event
		$('ul.snb_tree:last .depth2 > li > p').click(function(e)
		{
			FlowitUtil.log($(this).text());
		});
		
		// search event
//		$('p.sch a.searchLnk').click(function (e) {
//			e.preventDefault();
//			var searchKey = $(this).siblings('input.searchTxt').val();
//			FlowitUtil.log('click:' + searchKey);
//		});
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
	loadListContent : function(searchKey, refreshes)
	{
		
		if(header.getVsSettingTap() == 2)
		{
			node.loadListContent();
			return;
		}
		
		if (adcSetting.getAdc().type == "F5")
		{		
			if(header.getVsSettingTap() == 1)
			{
				profile.loadProfileListContent();
				return;
			}
//			else if(header.getVsSettingTap() == 2)
//			{
//				node.loadListContent();
//				return;
//			}
			else if (header.getVsSettingTap() == 3)
			{
				noticeGrp.loadListContent();
				return;
			}
		}
		else if (adcSetting.getAdc().type == "Alteon")
		{
			if (header.getVsSettingTap() == 3)
			{
				noticeGrp.loadListContent();
				return;
			}
		}
		
		with (this)
		{
			setTarget();
//			if (!adcSetting.isAdcSet())
//			{
//				$('#wrap .contents_area').empty();
//				return;
//			}
			if(!validateDaterefresh())
			{
				return false;
			}			
			ajaxManager.runJsonExt({
				url : "virtualServer/retrieveVirtualSvrTotal.action",
				data :
				{
					"adc.index" : adcSetting.getAdc().index,
					"adc.name" : adcSetting.getAdc().name,
					"adc.type" : adcSetting.getAdc().type,
					"adcScope.level" : target.level,
					"adcScope.index" : target.index,
					"searchKey" : searchKey
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
					if (target.level == 2)
						loadVirtualSvrListContent(searchKey, undefined, undefined, refreshes);
					else
						loadVsListContent(searchKey, undefined, undefined, refreshes);
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_VS_VSECEFAIL, jqXhr);
//					exceptionEvent();
				}	
			});
		}
	},
	
//	loadRefreshListContent : function(searchKey, refreshes, orderType, orderDir)
//	{
//		with (this)
//		{
//			if (!adcSetting.isAdcSet())
//			{
//				$('#wrap .contents_area').empty();
//				return;
//			}
//			
//			ajaxManager.runJsonExt({
//				url : "virtualServer/loadRefreshListContent.action",
//				data :
//				{
//					"adc.index" : adcSetting.getAdc().index,
//					"adc.name" : adcSetting.getAdc().name,
//					"adc.type" : adcSetting.getAdc().type,
//					"searchKey" : searchKey
//				},
//				successFn : function(data)
//				{
//					if (!data.isSuccessful)
//					{
//						alert(data.message);
//						return;
//					}
//					
//					loadVirtualSvrListContent(searchKey, undefined, undefined, refreshes);					
//				},
//				errorFn : function(a,b,c)
//				{
//					alert("Virtual Server/Service 개수 추출에 실패했습니다.");
//					exceptionEvent();
//				}	
//			});
//		}
//	},
	
	loadVirtualSvrListContent : function(searchKey, fromRow, toRow, refreshes)
	{
//		var startTime = new Date();	
//		alert(startTime);
//		alert("start Time : " + startTime.getTime());
//		FlowitUtil.log("startTime:%s", startTime.getTime());
		with (this)
		{
			if (!adcSetting.isAdcSet())
			{
				$('#wrap .contents_area').empty();
				return;
			}		
			
			var vsListContentURL="";
			if (adcSetting.getAdc().type === 'F5')
			{
				vsListContentURL = "virtualServer/loadF5VsListContent.action";
			}
			else if (adcSetting.getAdc().type === 'Alteon')
			{
				vsListContentURL = "virtualServer/loadAlteonVsListContent.action";
			}
			else if (adcSetting.getAdc().type === 'PAS')
			{
				vsListContentURL = "virtualServer/loadPASVsListContent.action";
			}
			else if (adcSetting.getAdc().type === 'PASK')
			{
				vsListContentURL = "virtualServer/loadPASKVsListContent.action";
			}
			else if (adcSetting.getAdc().type === 'PiolinkUnknown')
			{
				vsListContentURL = "virtualServer/loadPASVsListContent.action";
			}
			
			ajaxManager.runJsonExt({
				url : "virtualServer/loadRefreshListContent.action",
				data :
				{
					"adc.index" : adcSetting.getAdc().index,
					"adc.name" : adcSetting.getAdc().name,
					"adc.type" : adcSetting.getAdc().type,
					"adcScope.level" : target.level,
					"adcScope.index" : target.index,
					"refreshes" : !!refreshes,
					"searchKey" : searchKey,
					"orderDir" : this.orderDir,
					"orderType" : this.orderType,
					"adc.status" : adcSetting.status
				},
				successFn : function(data)
				{					
					if (!data.isSuccessful)
					{
						if(data.extraKey == 1)
						{
							return;
						}
						else
						{
							pageNavi.updateRowTotal(rowTotal, orderType, lastselectedPageNumber);
							$.obAlertNotice(data.message);						
							return;							
						}						
					}
					
					ajaxManager.runHtmlExt({
						url : vsListContentURL,
						data :
						{
							"adc.index" : adcSetting.getAdc().index,
							"adc.name" : adcSetting.getAdc().name,
							"adc.type" : adcSetting.getAdc().type,
							"adcScope.level" : target.level,
							"adcScope.index" : target.index,
							"fromRow" : fromRow === undefined ? pageNavi.getFirstRowOfCurrentPage() : fromRow,
							"toRow" : toRow === undefined ? pageNavi.getLastRowOfCurrentPage() : toRow,
							"searchKey" : searchKey,
							"refreshes" : !!refreshes,
							"orderDir" : data.orderDir,
							"orderType" : data.orderType
						},
						target: "#wrap .contents",
						successFn : function(params) 
						{
//							var twoTime = new Date();
//							alert("2ndTime : " + twoTime.getTime());
//							FlowitUtil.log("twoTime:%s", twoTime.getTime());
							
//							header.setActiveMenu('AdcSetting');
							header.setActiveMenu('SlbSetting');
							
							setActiveContent('VsListContent');
							noticePageInfo();
							searchedKey = searchKey;
							_applyListContentCss();
							_registerListContentEvents();
//							var threeTime = new Date();
//							alert("threeTime : " + threeTime.getTime());
//							alert(threeTime - twoTime);
//							FlowitUtil.log("threeTime:%s, gap:%s", threeTime.getTime(), threeTime-twoTime);
							
						},
						completeFn : function()
						{			
//							var endTime = new Date();
//							alert("end Time : " + endTime.getTime());
//							alert(endTime-startTime);
//							alert((endTime-startTime)/1000);
//							FlowitUtil.log("endTime:%s, gap:%s", endTime.getTime(), endTime-startTime);
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
					pageNavi.updateRowTotal(rowTotal, orderType, lastselectedPageNumber);
					$.obAlertAjaxError(VAR_VS_VSECEFAIL, jqXhr);
//					exceptionEvent();
				}	
			});
		}
	},
	loadVsListContent : function(searchKey, fromRow, toRow, refreshes)
	{
		with(this)
		{
			ajaxManager.runHtmlExt({
				url : "virtualServer/loadVsListContent.action",
				data :
				{
					"adc.index" : adcSetting.getAdc().index,
					"adc.name" : adcSetting.getAdc().name,
					"adc.type" : adcSetting.getAdc().type,
					"adcScope.level" : target.level,
					"adcScope.index" : target.index,
					"fromRow" : fromRow === undefined ? pageNavi.getFirstRowOfCurrentPage() : fromRow,
					"toRow" : toRow === undefined ? pageNavi.getLastRowOfCurrentPage() : toRow,
					"searchKey" : searchKey,
					"refreshes" : !!refreshes,
					"orderDir" : orderDir,
					"orderType" : orderType
				},
				target: "#wrap .contents",
				successFn : function(params) 
				{
					header.setActiveMenu('SlbSetting');
					
					setActiveContent('VsListContent');
					noticePageInfo();
					searchedKey = searchKey;
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
		}
	},
	
	loadVirtualSvrTableInListContent : function(searchKey, fromRow, toRow, refreshes, orderDir, orderType, failedVsIndexList)
	{
		with (this)
		{			
			var vsTableInListContentURL="";
			
			if (target.level == 2)
			{
				if (adcSetting.getAdc().type === 'F5')
				{
					vsTableInListContentURL = "virtualServer/loadVirtualSvrTableInF5ListContent.action";
				}
				else if (adcSetting.getAdc().type === 'Alteon')
				{
					vsTableInListContentURL = "virtualServer/loadVirtualSvrTableInAlteonListContent.action";
				}
				else if (adcSetting.getAdc().type === 'PAS')
				{
					vsTableInListContentURL = "virtualServer/loadVirtualSvrTableInPASListContent.action";
				}
				else if (adcSetting.getAdc().type === 'PASK')
				{
					vsTableInListContentURL = "virtualServer/loadVirtualSvrTableInPASKListContent.action";
				}
				else if (adcSetting.getAdc().type === 'PiolinkUnknown')
				{
					vsTableInListContentURL = "virtualServer/loadVirtualSvrTableInPASListContent.action";
				}
			}
			else
			{
				vsTableInListContentURL = "virtualServer/loadVsTableInListContent.action";
			}
			FlowitUtil.log("searchKey:%s, fromRow:%s, toRow:%s, refreshes:%s", searchKey, fromRow, toRow, refreshes);
			ajaxManager.runHtmlExt({
				url : vsTableInListContentURL,
				data :
				{
					"adc.index" : adcSetting.getAdc().index,
					"adc.name" : adcSetting.getAdc().name,
					"adc.type" : adcSetting.getAdc().type,
					"adcScope.level" : target.level,
					"adcScope.index" : target.index,
					"fromRow" : fromRow === undefined ? pageNavi.getFirstRowOfCurrentPage() : fromRow,
					"toRow" : toRow === undefined ? pageNavi.getLastRowOfCurrentPage() : toRow,
					"searchKey" : searchKey,
					"refreshes" : !!refreshes,
					"orderDir" : this.orderDir,
					"orderType" : this.orderType
				},
				target: "table.virtualSvrTable",
				successFn : function(params)
				{
					noticePageInfo();
					searchedKey = searchKey;
					_applyListContentCss();
					_registerListContentEvents();
					
					if (selectedVsIndexList != undefined)
					{
						for(var i=0; i < selectedVsIndexList.length; i++)
						{
							$('#rsGrpSelected tbody tr').find('.serverChk').each(function(index)
							{
								
								if($(this).val() == selectedVsIndexList[i])
								{
									$(this).attr('checked', 'checked');
								}
	//							else
	//							{
	//								$(this).removeAttr('checked');
	//							}							
							});
						}
					}
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
	_registerListContentEvents : function()
	{
		with (this)
		{
			$('.orderDir_Desc').click(function(e)
			{
				e.preventDefault();
				orderDir =  $(this).find('.orderDir').text();
				orderType =  $(this).find('.orderType').text();							
				var searchKey = $('input[name="searchKey"]').val();
				FlowitUtil.log('searchKey:' + searchKey);			
				searchFlag=true;
				loadListContent(searchKey ,false, orderDir , orderType);
			});
			
			$('.orderDir_Asc').click(function(e)
			{
				e.preventDefault();
				orderDir =  $(this).find('.orderDir').text();
				orderType =  $(this).find('.orderType').text();
				var searchKey = $('input[name="searchKey"]').val();
				FlowitUtil.log('searchKey:' + searchKey);			
				searchFlag=true;
				loadListContent(searchKey , false, orderDir , orderType);
			});
			
			$('.orderDir_None').click(function(e)
			{
				e.preventDefault();
				orderDir =  $(this).find('.orderDir').text();
				orderType =  $(this).find('.orderType').text();
				var searchKey = $('input[name="searchKey"]').val();
				FlowitUtil.log('searchKey:' + searchKey);		
				searchFlag=true;
				loadListContent(searchKey , false, orderDir , orderType);
			});
			
			$('.profileLnk').click(function(e)
			{
				e.preventDefault();
				header.setVsSettingTap(1);
				profile.loadProfileListContent();
			});
		
			$('.rServerLnk').click(function(e) 
			{
				e.preventDefault();
				header.setVsSettingTap(2);
				node.loadListContent();
			});
			
			$('.noticeServerLnk').click(function(e) 
			{
				e.preventDefault();
				header.setVsSettingTap(3);
				noticeGrp.loadListContent();
			});
			
			$('.addVirtualServerLnk').click(function(e)
			{
				if (adcSetting.getAdc().type == 'F5')
				{
					f5Vs.loadVsAddContent();
				}
				else if (adcSetting.getAdc().type == 'Alteon')
				{
					alteonVs.loadVsAddContent();
				}
				else if (adcSetting.getAdc().type == 'PAS')
				{
					pasVs.loadVsAddContent();
				}
				else if (adcSetting.getAdc().type == 'PASK')
				{
					paskVs.loadVsAddContent();
				}
				else if (adcSetting.getAdc().type == 'PiolinkUnknown')
				{
					pasVs.loadVsAddContent();
				}
			});
			
			$('.modifyVirtualServerLnk').click(function(e)
			{		
				if (target.level == 2)
				{
					if (adcSetting.getAdc().type == 'F5')
					{
						f5Vs.loadVsModifyContent($(this).parent().parent().find('.serverChk').val());
					}
					else if (adcSetting.getAdc().type == 'Alteon')
					{
						alteonVs.loadVsModifyContent($(this).parent().parent().find('.serverChk').val());
					}
					else if (adcSetting.getAdc().type == 'PAS')
					{
						pasVs.loadVsModifyContent($(this).parent().parent().find('.serverChk').val());
					}
					else if (adcSetting.getAdc().type == 'PASK')
					{
						paskVs.loadVsModifyContent($(this).parent().parent().find('.serverChk').val());
					}
					else if (adcSetting.getAdc().type == 'PiolinkUnknown')
					{
						pasVs.loadVsModifyContent($(this).parent().parent().find('.serverChk').val());
					}
				}
				else
				{
					var adcType = $(this).parent().parent().find('.session span').text().trim();
					var adcIndex = $(this).parent().parent().find('.adcName .adcIndex').text().trim();
					var adcName = $(this).parent().parent().find('.adcName .name').text().trim();
					
					adc.index = adcIndex;
					adc.type = adcType;
					adc.name = adcName;
					adcSetting._selectAdc(adcIndex);
					
					if (adcType == 1)
					{
						f5Vs.loadVsModifyContent($(this).parent().parent().find('.serverChk').val());
					}
					else if (adcType == 2)
					{
						alteonVs.loadVsModifyContent($(this).parent().parent().find('.serverChk').val());
					}	
					else if (adcType == 3)
					{
						pasVs.loadVsModifyContent($(this).parent().parent().find('.serverChk').val());
					}
					else if (adcType == 4)
					{
						paskVs.loadVsModifyContent($(this).parent().parent().find('.serverChk').val());
					}
					else
					{
						pasVs.loadVsModifyContent($(this).parent().parent().find('.serverChk').val());
					}
				}
			});
			
			$('.refreshLnk').click(function(e)
			{
				e.preventDefault();
				var searchedKey = '';
				loadListContent(searchedKey, true);
//				loadRefreshListContent(searchedKey, true);
			});
			
			$('.allServersChk').click(function(e)
			{
				var isChecked = $(this).is(':checked');
	//			$(this).parent().parent().parent().parent().find('.serverChk').attr('checked', isChecked);
				$(this).parent().parent().parent().parent().find('.serverChk').attr('checked', isChecked);
				
				$('#rsGrpSelected tbody tr .session').find('span').each(function(index)
				{
					if($(this).text().trim() == 1 || $(this).text().trim() == 2)
					{}
					else
						$(this).parent().parent().find('.serverChk').removeAttr("checked");
						
				});
			});
			
			$('.enableVssLnk').off('click'); // 중복 이벤트 방지를 위한 등록 해제 
			$('.enableVssLnk').click(function(e)
			{
				if(target.level == 2)
					enableServers();
				else
					enableGroupServers();
			});
			$('.disableVssLnk').off('click');  // 중복 이벤트 방지를 위한 등록 해제
			$('.disableVssLnk').click(function(e)
			{
				if(target.level == 2)
					disableServers();
				else
					disableGroupServers();				
			});
			
			$('.delVssLnk').off('click');  // 중복 이벤트 방지를 위한 등록 해제
			$('.delVssLnk').click(function(e)
			{	
				if(target.level == 2)
					delServers();
				else
					delGroupServers();
			});
			
//			$('.onGoToMonitoring').click(function(e)
//			{
//				$('.monitorMnu').click();//TODO
//				$('.monitorNetworkMnu').click();
//			});
			
			$('.onGoToMonitoring').click(function(e) 
			{
				e.preventDefault();
				with (this) 
				{
					var adcIndex =  $(this).parent().parent().parent().find('.adcIndex').text().trim();
					FlowitUtil.log("index for monitor: %s", adcIndex);
					adcSetting._selectAdc(adcIndex);
					$('.monitorMnu').click();
					$('.monitorNetworkMnu').click();
				}
			});
			
			
			// slb Scheduling page 이동
			$('.onGoToSchedule').click(function(e)
			{
				// TODO 선택한 VS 정보에 따른 SLB Schedule 상세정보 페이지로 이동
				$('.slbMgmtMnu').click();
				$('.slbScheduleMnu').click();
			});
			
			// search event
			$('.btn a.searchLnk').click(function (e)
			{
				e.preventDefault();
				var searchKey = $('input[name="searchKey"]').val();
				FlowitUtil.log('searchKey:' + searchKey);
				searchFlag=true;
				loadListContent(searchKey);
			});
			
			$('.inputTextposition1 input.searchTxt').keydown(function(e)
			{
				if (e.which != 13)
					return;
				
				var searchKey = $(this).val();
				FlowitUtil.log('searchKey:' + searchKey);
				searchFlag=true;
				loadListContent(searchKey);
			});
			
			if(this.searchFlag == true)
			{
				$('.nulldataMsg').addClass("none");
				$('.dataNotExistMsg').addClass("none");
				if($('.virtualServerList').size() > 0)
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
				$('.searchNotMsg').addClass("none");
				$('.nulldataMsg').addClass("none");
				if($('.virtualServerList').size() > 0)
				{
					$('.dataNotExistMsg').addClass("none");
				}
				else
				{
					$('.dataNotExistMsg').removeClass("none");
				}
			}
			
			if (target.level == 2)
			{
				if (adcSetting.getAdcStatus() != "available")
				{
					$('.searchNotMsg').addClass("none");
					$('.dataNotExistMsg').addClass("none");
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
						$('input[name="searchKey"]').attr("disabled", "disabled");
						$('.imageChange').attr("src", "imgs/meun/btn_search_1_off.png");
					}							
				}
				else
				{
					if (adcSetting.getAdc().mode == 1)
					{
						$('.imgOff').removeClass("none");
						$('.imgOn').addClass("none");
						
						$('.status_imgoff').addClass("none");
						$('.status_imgon').removeClass("none");				
						
						$('.allServersChk').attr("disabled", "disabled");
						$('.serverChk').attr("disabled", "disabled");				
											
					}
					else {}
				}
			}
			else
			{
				$('#rsGrpSelected tbody tr .session').find('span').each(function(index)
				{
					if($(this).text().trim() == 1 || $(this).text().trim() == 2)
					{}
					else
						$(this).parent().parent().find('.serverChk').attr("disabled", "disabled");						
				});
				
				$('#rsGrpSelected tbody tr .updateTime').find('span').each(function(index)
				{
					if($(this).text().trim() != 0)
					{}
					else
					{
						$(this).parent().parent().find('.serverChk').attr("disabled", "disabled");
						
						var vsStatus = $(this).parent().parent().find('.vsStatus').filter(':last');
						var imgHtml = "";
						imgHtml = '<img class="imgOn status_imgon" src="imgs/icon/icon_vs_disabled.png" alt="${LANGCODEMAP["MSG_VSALTEON_TABLEOPERATION"]!}" />';
						vsStatus.html(imgHtml);
					}
				});
				
			}
			
			/*
			else {}
			var selectedVersion = adcSetting.getAdcVersion();			
			if(selectedVersion.startsWith("29.5") == true)
			{
				$('.imgOff').removeClass("none");
				$('.imgOn').addClass("none");
				
				$('.allServersChk').attr("disabled", "disabled");
				$('.serverChk').attr("disabled", "disabled");
				$('.disabledChk').addClass("none");
				$('.notadc-msg').removeClass("none");
				$('input[name="searchKey"]').attr("disabled", "disabled");
				$('.imageChange').attr("src", "imgs/meun/btn_search_1_off.png");
				$('.virtualServerList').addClass("none");				
			}
			*/
			
		}
	},
	_getCheckedVs : function()
	{
		return $('.serverChk').filter(':checked').map(function() {			
			var vs_index = $(this).val();
			var adc_index = $(this).parent().parent().find('td.adcName .adcIndex').text().trim();
			var adc_type = $(this).parent().parent().find('td.session span').text().trim();
			var vs_ip = $(this).parent().parent().find('td.vsIp').text().trim();
			
			return {
				"vsIndex" : vs_index,
				"adcIndex" : adc_index,
				"adcType" : adc_type,
				"vsIp" : vs_ip
			};
		}).get();
	},
	enableServers : function()
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
					"adc.index" : adcSetting.getAdc().index,
					"adc.type" : adcSetting.getAdc().type,
					"adc.name" : adcSetting.getAdc().name,
					"adcScope.level" : target.level,
					"adcScope.index" : target.index,
					"virtualSvrIndices" : checkedServers,
//					"vsAdcCheckInString" : Object.toJSON(_getCheckedVs()),
					"orderDir" : this.orderDir,
					"orderType" : this.orderType
				},
				target : "#wrap .contents_area",
				successFn : function(params)
				{					
					loadVirtualSvrTableInListContent(searchedKey, undefined, undefined, false, this.orderDir, this.orderType);
					
					if(adcSetting.getAdc().type != "Alteon")	// Alteon 동기화 부분 제외 (Alteon 이 아닌 경우만)
						_enableServersPeer(checkedServers, this.orderDir, this.orderType);
//					if(target.level == 2)
//					{
//						if(adcSetting.getAdc().type != "Alteon")	// Alteon 동기화 부분 제외 (Alteon 이 아닌 경우만)
//							_enableServersPeer(checkedServers, this.orderDir, this.orderType);
//					}
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_VS_ENAFAIL, jqXhr);
//					exceptionEvent();
				}	
			});
		}
	},
	enableGroupServers : function()
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
				
//			_selectedVsListVal();
			
			ajaxManager.runJsonExt({
				url : "virtualServer/enableGroupVss.action",
				data :
				{
					"adcScope.level" : target.level,
					"adcScope.index" : target.index,
					"virtualSvrIndices" : checkedServers,
					"vsAdcCheckInString" : Object.toJSON(_getCheckedVs()),
					"orderDir" : this.orderDir,
					"orderType" : this.orderType
				},
				target : "#wrap .contents_area",
				successFn : function(data)
				{
					if (!data.isSuccessful)
					{
						alert(data.message);
						selectedVsIndexList = data.vsIndexList;
					}
					else
					{
						selectedVsIndexList = undefined;
					}
					
					loadVirtualSvrTableInListContent(searchedKey, undefined, undefined, false, this.orderDir, this.orderType);					
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_VS_ENAFAIL, jqXhr);
//					exceptionEvent();
				}	
			});
		}
	},
	_selectedVsListVal : function()
	{
		with (this)
		{
			var vsIndexList = new Array();
			$('#rsGrpSelected tbody tr input[type=checkbox]:checked').each(function(index)
			{						
				if ($(this).is(':checked'))
				{
					vsIndexList.push($(this).val().trim());
				}
				selectedVsIndexList = vsIndexList;
			});
		}
	},
	_enableServersPeer : function(checkedServers, orderDir, orderType)
	{
		with (this)
		{
			if (!adcSetting.isAdcSet())
			{
				$('#wrap .contents').empty();
				return;
			}
			
			FlowitUtil.log("index: %s", adcSetting.getAdc().index);
			ajaxManager.runJsonExt({
				url : "virtualServer/checkPairIndex.action",
				data :
				{
					"adc.index" : adcSetting.getAdc().index,
					"adc.name" : adcSetting.getAdc().name,
					"adc.type" : adcSetting.getAdc().type,
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
									"pairIndex" : data.pairIndex,
									"adc.index" : adcSetting.getAdc().index,
									"adc.name" : adcSetting.getAdc().name,
									"adc.type" : adcSetting.getAdc().type,
									"virtualSvrIndices" : checkedServers,
									"orderDir" : orderDir,
									"orderType" : orderType									
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
											"adc.index" : adcSetting.getAdc().index,
											"adc.type" : adcSetting.getAdc().type,
											"adc.name" : adcSetting.getAdc().name,
											"pairIndex" : data.pairIndex,
											"virtualSvrIndices" : checkedServers,
											"orderDir" : orderDir,
											"orderType" : orderType											
										},
										target: "#wrap .contents_area",
										successFn : function(params)
										{
											loadVirtualSvrTableInListContent(searchedKey, undefined, undefined, false, orderDir, orderType);
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
					loadVirtualSvrTableInListContent(searchedKey, undefined, undefined, false, orderDir, orderType);
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
	disableServers : function()
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
					"adc.index" : adcSetting.getAdc().index,
					"adc.type" : adcSetting.getAdc().type,
					"adc.name" : adcSetting.getAdc().name,
					"adcScope.level" : target.level,
					"adcScope.index" : target.index,
					"virtualSvrIndices" : checkedServers,
//					"vsAdcCheckInString" : Object.toJSON(_getCheckedVs()),
					"orderDir" : this.orderDir,
					"orderType" : this.orderType
				},
				target: "#wrap .contents_area",
				successFn : function(params)
				{
					loadVirtualSvrTableInListContent(searchedKey, undefined, undefined, false, this.orderDir, this.orderType);
										
					if(adcSetting.getAdc().type != "Alteon")	// Alteon 동기화 부분 제외 (Alteon 이 아닌 경우만)
						_disableServersPeer(checkedServers, this.orderDir, this.orderType);
//					if(target.level == 2)
//					{
//						if(adcSetting.getAdc().type != "Alteon")	// Alteon 동기화 부분 제외 (Alteon 이 아닌 경우만)
//							_disableServersPeer(checkedServers, this.orderDir, this.orderType);
//					}
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_VS_VSEDFAIL, jqXhr);
//					exceptionEvent();
				}	
			});
		}
	},		
	disableGroupServers : function()
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
				
//			_selectedVsListVal();
			
			ajaxManager.runJsonExt({
				url : "virtualServer/disableGroupVss.action",
				data :
				{
					"adcScope.level" : target.level,
					"adcScope.index" : target.index,
					"virtualSvrIndices" : checkedServers,
					"vsAdcCheckInString" : Object.toJSON(_getCheckedVs()),
					"orderDir" : this.orderDir,
					"orderType" : this.orderType
				},
				target : "#wrap .contents_area",
				successFn : function(data)
				{
					if (!data.isSuccessful)
					{
						alert(data.message);
						selectedVsIndexList = data.vsIndexList;
					}
					else
					{
						selectedVsIndexList = undefined;
					}
					
					loadVirtualSvrTableInListContent(searchedKey, undefined, undefined, false, this.orderDir, this.orderType);					
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_VS_ENAFAIL, jqXhr);
//					exceptionEvent();
				}	
			});
		}
	},
	delServers : function()
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
					"adc.index" : adcSetting.getAdc().index,
					"adc.type" : adcSetting.getAdc().type,
					"adc.name" : adcSetting.getAdc().name,
					"adcScope.level" : target.level,
					"adcScope.index" : target.index,
					"virtualSvrIndices" : checkedServers,
//					"vsAdcCheckInString" : Object.toJSON(_getCheckedVs()),
					"orderDir" : this.orderDir,
					"orderType" : this.orderType
				},
				target: "#wrap .contents_area",
				successFn : function(params)
				{
					loadVirtualSvrListContent(searchedKey, undefined, undefined, false, this.orderDir, this.orderType);
					
					if(adcSetting.getAdc().type != "Alteon")	// Alteon 동기화 부분 제외 (Alteon 이 아닌 경우만)
						_delServersPeer(checkedServers, this.orderDir, this.orderType);
					
//					if(target.level == 2)
//					{
//						if(adcSetting.getAdc().type != "Alteon")	// Alteon 동기화 부분 제외 (Alteon 이 아닌 경우만)
//							_delServersPeer(checkedServers, this.orderDir, this.orderType);
//					}					
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_VS_VIRDFAIL, jqXhr);
//					exceptionEvent();
				}	
			});
		}
	},
	delGroupServers : function()
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
				
//			_selectedVsListVal();
			
			ajaxManager.runJsonExt({
				url : "virtualServer/delGroupVss.action",
				data :
				{
					"adcScope.level" : target.level,
					"adcScope.index" : target.index,
					"virtualSvrIndices" : checkedServers,
					"vsAdcCheckInString" : Object.toJSON(_getCheckedVs()),
					"orderDir" : this.orderDir,
					"orderType" : this.orderType
				},
				target : "#wrap .contents_area",
				successFn : function(data)
				{
					if (!data.isSuccessful)
					{
						alert(data.message);
						selectedVsIndexList = data.vsIndexList;
					}
					else
					{
						selectedVsIndexList = undefined;
					}
					
					loadVirtualSvrTableInListContent(searchedKey, undefined, undefined, false, this.orderDir, this.orderType);					
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_VS_ENAFAIL, jqXhr);
//					exceptionEvent();
				}	
			});
		}
	},
	_delServersPeer : function(checkedServers, orderDir, orderType)
	{
		with (this)
		{
			if (!adcSetting.isAdcSet())
			{
				$('#wrap .contents_area').empty();
				return;
			}
			
			FlowitUtil.log("index: %s", adcSetting.getAdc().index);
			ajaxManager.runJsonExt({
				url : "virtualServer/checkPairIndex.action",
				data :
				{
					"adc.index" : adcSetting.getAdc().index,
					"adc.name" : adcSetting.getAdc().name,
					"adc.type" : adcSetting.getAdc().type,
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
									"pairIndex" : data.pairIndex,
									"adc.index" : adcSetting.getAdc().index,
									"adc.name" : adcSetting.getAdc().name,
									"adc.type" : adcSetting.getAdc().type,
									"virtualSvrIndices" : checkedServers,
									"orderDir" : orderDir,
									"orderType" : orderType
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
											"adc.index" : adcSetting.getAdc().index,
											"adc.type" : adcSetting.getAdc().type,
											"adc.name" : adcSetting.getAdc().name,
											"pairIndex" : data.pairIndex,
											"virtualSvrIndices" : checkedServers,
											"orderDir" : orderDir,
											"orderType" : orderType
										},
										target: "#wrap .contents_area",
										successFn : function(params)
										{
//											loadVirtualSvrListContent(searchedKey, undefined, undefined, false);
											loadListContent(searchedKey, false, orderDir, orderType);
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
					loadListContent(searchedKey, false, orderDir, orderType);
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
			if (adcSetting.getAdc().type === 'F5')
			{
				f5Vs.loadVsModifyContent(index);
			}
			else if (adcSetting.getAdc().type === 'Alteon')
			{
				alteonVs.loadVsModifyContent(index);
			}
			else if (adcSetting.getAdc().type === 'PAS')
			{
				pasVs.loadVsModifyContent(index);
			}
			else if (adcSetting.getAdc().type === 'PASK')
			{
				paskVs.loadVsModifyContent(index);
			}
			else if (adcSetting.getAdc().type === 'PiolinkUnknown')
			{
				pasVs.loadVsModifyContent(index);
			}
		}
	},
	validateDaterefresh : function()
	{
		if(($('.control_Board input.searchTxt').val() != "") && ($('.control_Board input.searchTxt').val() != null))
		{
			var search = $('.control_Board input.searchTxt').val();
			if (!isValidStringLength(search, 1, 64))
			{
				var data = VAR_COMMON_LENGTHFORMAT+"("+1+"~"+64+")";
				$.obAlertNotice(data);
				$('.control_Board input.searchTxt').val('');
				return false;
			}

			if (!isExistSpecialCharacter(search))
			{
				$.obAlertNotice(VAR_FAULTSETTING_SPECIALCHAR);
				$('.control_Board input.searchTxt').val('');
				return false;
			}			
//			var search = $('.control_Board input.searchTxt').val();
//			if(getValidateStringint(search, 1, 64) == false)
//			{
//				alert(VAR_FAULTSETTING_SPECIALCHAR);
//				$('.control_Board input.searchTxt').val('');
//				return false;
//			}			
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
			
			FlowitUtil.log("index: %s", adcSetting.getAdc().index);
			ajaxManager.runJsonExt({
				url : "virtualServer/checkPairIndex.action",
				data :
				{
					"adc.index" : adcSetting.getAdc().index,
					"adc.name" : adcSetting.getAdc().name,
					"adc.type" : adcSetting.getAdc().type,
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
									"pairIndex" : data.pairIndex,
									"adc.index" : adcSetting.getAdc().index,
									"adc.name" : adcSetting.getAdc().name,
									"adc.type" : adcSetting.getAdc().type,
									"virtualSvrIndices" : checkedServers,
									"orderDir" : orderDir,
									"orderType" : orderType
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
											"adc.index" : adcSetting.getAdc().index,
											"adc.type" : adcSetting.getAdc().type,
											"adc.name" : adcSetting.getAdc().name,
											"pairIndex" : data.pairIndex,
											"virtualSvrIndices" : checkedServers,
											"orderDir" : orderDir,
											"orderType" : orderType
										},
										target: "#wrap .contents_area",
										successFn : function(params)
										{
											loadVirtualSvrTableInListContent(searchedKey, undefined, undefined, false, orderDir, orderType);
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
					loadVirtualSvrTableInListContent(searchedKey, undefined, undefined, false, orderDir, orderType);
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