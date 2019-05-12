var SlbSchedule = Class.create({
	initialize : function()
	{
		var fn = this;
		this.adc = {};
		this.alteonVs = new AlteonVs();
		this.f5Vs = new F5Vs();
		this.pasVs = new PASVs();
		this.paskVs = new PASKVs();
		this.vServerNameWnd = new VServerNameWnd();
		this.OBajaxManager = new OBAjax();
		this.searchedKey = undefined;
		this.orderDir  = 1; //오름차순
		this.orderType = 17;//예약시간
		this.lastselectedPageNumber = undefined;
		this.rowTotal = undefined;
		this.searchFlag = false;
		this.syncTime = undefined;	
		this.vsIndex = undefined;
		this.isModify = undefined;
		this.selectedReceiver = undefined;
		this.scheduleMin = 0;						
		this.scheduleHr = 0;
		this.orgSmReceiveList = undefined;
		this.orgSlbReceiverLists = undefined;
		this.slbReceiverLists = undefined; 
		this.pageNavi = new PageNavigator();
		this.pageNavi.onChange(function(fromRow, toRow, orderType, orderDir)
		{
			FlowitUtil.log('fromRow: %s, toRow: %s, orderType: %s, orderDir: %s', fromRow, toRow, orderType, orderDir);
			fn.loadSlbScheduleTableInListContent(fn.searchedKey, fromRow, toRow, orderType, orderDir);
			fn.lastselectedPageNumber = fn.pageNavi.getCurrentPage();
		});
		
		this.slbTypeIdx = undefined;
		this.pagePopNavi = new PageNavigator({
			pageNaviSelector:".pagePopNavigatorOn",
			pageRowCountCbxSelector:".pagePopRowCountCbxOn",
			pageNaviCssClass:"pagePopNavigatorOn",
			pageRowCountCbxCssClass:"pagePopRowCountCbxOn",
			rowCountPerPage: 10,
			availableRowCountPerPage:[10,20,30,40,50]
		});
		this.pagePopNavi.onChange(function(fromRow, toRow, orderType, orderDir)
		{
			fn.loadSlbUserTableInListContent(fn.searchedKey, fromRow, toRow, orderType, orderDir);
		});
		
		this.pagePopSlbNavi = new PageNavigator({
			pageNaviSelector:".pageNavigatorOn",
			pageRowCountCbxSelector:".pageRowCountCbxOn",
			pageNaviCssClass:"pageNavigatorOn",
			pageRowCountCbxCssClass:"pageRowCountCbxOn",
			rowCountPerPage: 10,
			availableRowCountPerPage:[10,20,30,40,50]
		});
		this.pagePopSlbNavi.onChange(function(fromRow, toRow, orderType, orderDir)
		{
			fn.loadSlbListTableInListContent(fn.searchedKey, fromRow, toRow, orderType, orderDir);
		});
		
	},
	onAdcChange : function()
	{
		this.orderDir  = 1; //오름차순
		this.orderType = 17;//예약시간
		this.orgSmReceiveList = undefined;
		this.orgSlbReceiverLists = undefined;
		this.slbReceiverLists = undefined; 
		slbSchedule.loadListContent();
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
	// slbScheduleList Count
	loadListContent : function(searchKey, refreshes)
	{
		with (this)
		{
			if (!adcSetting.isAdcSet())
			{
				$('#wrap .contents_area').empty();
				return;
			}
			if(!validateDaterefresh())
			{
				return false;
			}			
			ajaxManager.runJsonExt({
				url : "slbSchedule/retrieveSlbScheduleTotal.action",
				data :
				{
					"adc.index" : adcSetting.getAdc().index,
					"adc.name" : adcSetting.getAdc().name,
					"adc.type" : adcSetting.getAdc().type,
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
					loadScheduleListContent(searchKey, undefined, undefined, refreshes);					
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_VS_SCHEDULECNTFAIL, jqXhr);
				}	
			});
		}
	},
	/*
	sendSMS : function(userPhone)
	{
		with (this)
		{
			ajaxManager.runJsonExt({
				url : "slbSchedule/sendMeaage.action",
				data :
				{
					"userPhone" : userPhone
				},
				successFn : function(data)
				{
					if (!data.isSuccessful)
					{
						$.obAlertNotice(data.message);						
					}
					else
					{
						$.obAlertNotice(data.message);	
					}
					
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError("전송에 실패했습니다.", jqXhr);
				}
			});			
		}
	},
	*/
	/*
	loadRefreshListContent : function(searchKey, refreshes, orderType, orderDir)
	{
		with (this)
		{
			if (!adcSetting.isAdcSet())
			{
				$('#wrap .contents_area').empty();
				return;
			}
			
			ajaxManager.runJsonExt({
				url : "slbSchedule/loadRefreshListContent.action",
				data :
				{
					"adc.index" : adcSetting.getAdc().index,
					"adc.name" : adcSetting.getAdc().name,
					"adc.type" : adcSetting.getAdc().type,
					"searchKey" : searchKey
				},
				successFn : function(data)
				{
					if (!data.isSuccessful)
					{
						alert(data.message);
						return;
					}
					
					loadScheduleListContent(searchKey, undefined, undefined, refreshes);					
				},
				errorFn : function(a,b,c)
				{
					$.obAlertAjaxError(VAR_VS_SCHEDULECNTFAIL, jqXhr);
				}	
			});
		}
	},*/
	
	loadScheduleListContent : function(searchKey, fromRow, toRow, refreshes)
	{
		with (this)
		{
			if (!adcSetting.isAdcSet())
			{
				$('#wrap .contents_area').empty();
				return;
			}		
			
//			var vsListContentURL="";
//			if (adcSetting.getAdc().type === 'F5')
//			{
//				vsListContentURL = "slbSchedule/loadF5VsListContent.action";
//			}
//			else if (adcSetting.getAdc().type === 'Alteon')
//			{
//				vsListContentURL = "slbSchedule/loadAlteonVsListContent.action";
//			}
//			else if (adcSetting.getAdc().type === 'PAS')
//			{
//				vsListContentURL = "slbSchedule/loadPASVsListContent.action";
//			}
//			else if (adcSetting.getAdc().type === 'PASK')
//			{
//				vsListContentURL = "slbSchedule/loadPASKVsListContent.action";
//			}
//			else if (adcSetting.getAdc().type === 'PiolinkUnknown')
//			{
//				vsListContentURL = "slbSchedule/loadPASVsListContent.action";
//			}
			
			ajaxManager.runHtmlExt({
				url : "slbSchedule/loadSlbScheduleListContent.action", //vsListContentURL,
				data :
				{
					"adc.index" : adcSetting.getAdc().index,
					"adc.name" : adcSetting.getAdc().name,
					"adc.type" : adcSetting.getAdc().type,
					"fromRow" : fromRow === undefined ? pageNavi.getFirstRowOfCurrentPage() : fromRow,
					"toRow" : toRow === undefined ? pageNavi.getLastRowOfCurrentPage() : toRow,
					"searchKey" : searchKey,
					"refreshes" : !!refreshes,
					"orderDir" : this.orderDir,
					"orderType" : this.orderType
				},
				target: "#wrap .contents",
				successFn : function(params) 
				{
					header.setActiveMenu('SlbSchedule');					
					setActiveContent('VsListContent');
					noticePageInfo();
					searchedKey = searchKey;
//					_applyListContentCss();
					_registerListContentEvents();
					
				},
				completeFn : function()
				{			
					pageNavi.refresh();
//					var descField = $('.scheuleReceiveUser');
//					descField.val($.map(descField.val().split('|'), function(item)
//					{
//						return item.split(',')[0];
//					}).join(',')); // '샤오미,123213|테스트,123213' -> '샤오미,테스트'
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_VS_SCHEDULELOADFAIL, jqXhr);
				}
			});	
			
			/*ajaxManager.runJsonExt({
				url : "slbSchedule/loadRefreshListContent.action",
				data :
				{
					"adc.index" : adcSetting.getAdc().index,
					"adc.name" : adcSetting.getAdc().name,
					"adc.type" : adcSetting.getAdc().type,
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
						url : "slbSchedule/loadSlbScheduleListContent.action", //vsListContentURL,
						data :
						{
							"adc.index" : adcSetting.getAdc().index,
							"adc.name" : adcSetting.getAdc().name,
							"adc.type" : adcSetting.getAdc().type,
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
							header.setActiveMenu('SlbSchedule');
							
							setActiveContent('VsListContent');
							noticePageInfo();
							searchedKey = searchKey;
//							_applyListContentCss();
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
					pageNavi.updateRowTotal(rowTotal, orderType, lastselectedPageNumber);
					$.obAlertAjaxError(VAR_VS_VSECEFAIL, jqXhr);
				}	
			});*/
		}
	},
	// slbScheduleList paging
	loadSlbScheduleTableInListContent : function(searchKey, fromRow, toRow, refreshes, orderDir, orderType)
	{
		with (this)
		{			
//			var vsTableInListContentURL="";
//			
//			if (adcSetting.getAdc().type === 'F5')
//			{
//				vsTableInListContentURL = "slbSchedule/loadVirtualSvrTableInF5ListContent.action";
//			}
//			else if (adcSetting.getAdc().type === 'Alteon')
//			{
//				vsTableInListContentURL = "slbSchedule/loadVirtualSvrTableInAlteonListContent.action";
//			}
//			else if (adcSetting.getAdc().type === 'PAS')
//			{
//				vsTableInListContentURL = "slbSchedule/loadVirtualSvrTableInPASListContent.action";
//			}
//			else if (adcSetting.getAdc().type === 'PASK')
//			{
//				vsTableInListContentURL = "slbSchedule/loadVirtualSvrTableInPASKListContent.action";
//			}
//			else if (adcSetting.getAdc().type === 'PiolinkUnknown')
//			{
//				vsTableInListContentURL = "slbSchedule/loadVirtualSvrTableInPASListContent.action";
//			}
			ajaxManager.runHtmlExt({
				url : "slbSchedule/loadSlbScheduleTableInListContent.action", //vsTableInListContentURL,
				data :
				{
					"adc.index" : adcSetting.getAdc().index,
					"adc.name" : adcSetting.getAdc().name,
					"adc.type" : adcSetting.getAdc().type,
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
//					_applyListContentCss();
					_registerListContentEvents();
				},
				completeFn : function()
				{
					pageNavi.refresh();
					var descField = $('.scheuleReceiveUser');
					descField.val($.map(descField.val().split('|'), function(item)
					{
						return item.split(',')[0];
					}).join(',')); // '샤오미,123213|테스트,123213' -> '샤오미,테스트'
				},
				errorFn : function(a,b,c)
				{
					$.obAlertAjaxError(VAR_VS_SCHEDULELOADFAIL, jqXhr);
				}	
			});
		}
	},
//	_applyListContentCss : function()
//	{
//		// 테이블 컬럼 정렬
//		initTable([ "#statTable tbody tr" ], [ 3, 4 ], [ -1 ]);
//	},
	
	_registerListContentEvents : function()
	{
		with (this)
		{
//			$('.sendSMS').click(function(e)
//			{
//				
//			});
//			
//			$('.btnSms .sendSMS').click(function (e)
//			{
//				e.preventDefault();
//				var userPhone = $('input[name="userPhone"]').val();
//				FlowitUtil.log('userPhone:' + userPhone);
//				sendSMS(userPhone);
//			});			
			
			$('.orderDir_Desc').click(function(e)
			{
				e.preventDefault();
				orderDir =  $(this).find('.orderDir').text();
				orderType =  $(this).find('.orderType').text();							
				var searchKey = $('input[name="searchKey"]').val();
				FlowitUtil.log('searchKey:' + searchKey);			
				searchFlag=true;
				loadListContent(searchKey ,false, orderDir, orderType);
			});
			
			$('.orderDir_Asc').click(function(e)
			{
				e.preventDefault();
				orderDir =  $(this).find('.orderDir').text();
				orderType =  $(this).find('.orderType').text();
				var searchKey = $('input[name="searchKey"]').val();
				FlowitUtil.log('searchKey:' + searchKey);			
				searchFlag=true;
				loadListContent(searchKey , false, orderDir, orderType);
			});
			
			$('.orderDir_None').click(function(e)
			{
				e.preventDefault();
				orderDir =  $(this).find('.orderDir').text();
				orderType =  $(this).find('.orderType').text();
				var searchKey = $('input[name="searchKey"]').val();
				FlowitUtil.log('searchKey:' + searchKey);		
				searchFlag=true;
				loadListContent(searchKey , false, orderDir, orderType);
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
			
//			$('input[name="requestor"]').change(function()
//			{
//				alert($('input[name="requestor"]:checked').val());
//			});
						
//			$('#lastRequestUser').click(function(e)
//			{
//				loadLastResponseUserInfo();
//			});			
//			
//			$('#newRequestUser').click(function(e)
//			{
//				$('.slbUsrTable #slbUsrName').val('');
//				$('.slbUsrTable #slbUsrTeam').val('');
//				$('.slbUsrTable #slbUsrPhone').val('');
//			});
						
			// SLB 스케줄링 Add
			$('.addSlbScheduleLnk').click(function(e)
			{
				if($('.loginIdLnk').children('span').eq(2).text().trim() == "")
				{
					$.obAlertNotice("사용자의 전화번호가 누락되었습니다.");
					return false;
				}
				
				isModify = false;
				if (adcSetting.getAdc().type == 'F5')
				{					
					loadSlbScheduleF5AddContent(isModify);
				}
				else if (adcSetting.getAdc().type == 'Alteon')
				{
					loadSlbScheduleAlteonAddContent(isModify);
				}
				
//				if (adcSetting.getAdc().type == 'F5')
//				{
//					f5Vs.loadVsAddContent();
//				}
//				else if (adcSetting.getAdc().type == 'Alteon')
//				{
//					alteonVs.loadVsAddContent();
//				}
//				else if (adcSetting.getAdc().type == 'PAS')
//				{
//					pasVs.loadVsAddContent();
//				}
//				else if (adcSetting.getAdc().type == 'PASK')
//				{
//					paskVs.loadVsAddContent();
//				}
//				else if (adcSetting.getAdc().type == 'PiolinkUnknown')
//				{
//					pasVs.loadVsAddContent();
//				}
			});
			//
			$('.historyDiffLnk').click(function(e) 
			{
				e.preventDefault();
//				virtualSvrIndex = $(this).parents('tr').find('.virtualSvrIndex').text();
//				logSeq = $(this).parents('tr').find('.logSeq').text();
//				FlowitUtil.log('virtualSvrIndex: %s logSeq: %d', virtualSvrIndex, logSeq);
				
//				var scheduleVsIndex = $(this).parent().parent().find('.scheduleVsIndex').val();
				var vsIpAddress = $(this).parent().parent().find('.vsIpAddress').val();
				adcHistory.loadListContent(vsIpAddress);
//				adcHistory.loadDetail();
			});
			
			// SLB 스케줄링 Modify
			$('.modifySlbScheduleLnk').click(function(e)
			{			
				var scheduleIndex = $(this).parent().parent().find('.serverChk').val();
				var scheduleVsIndex = $(this).parent().parent().find('.scheduleVsIndex').val();
				var e_hour = $(this).parent().parent().find('.scheduleHour').val();				// 시
				var e_minute = $(this).parent().parent().find('.scheduleMin').val(); 		// 분
				
				vsIndex = scheduleVsIndex;
				isModify = true;
				if (adcSetting.getAdc().type == 'F5')
				{
//					f5Vs.loadVsModifyContent($(this).parent().parent().find('.serverChk').val());
					loadSlbScheduleF5ModifyContent(scheduleIndex, scheduleVsIndex, e_hour, e_minute);
				}
				else if (adcSetting.getAdc().type == 'Alteon')
				{
//					alteonVs.loadVsModifyContent($(this).parent().parent().find('.serverChk').val());
					
//					loadVsModifyContent(scheduleIndex);
					
					loadSlbScheduleAlteonModifyContent(scheduleIndex, scheduleVsIndex, e_hour, e_minute);
				}
//				else if (adcSetting.getAdc().type == 'PAS')
//				{
//					pasVs.loadVsModifyContent($(this).parent().parent().find('.serverChk').val());
//				}
//				else if (adcSetting.getAdc().type == 'PASK')
//				{
//					paskVs.loadVsModifyContent($(this).parent().parent().find('.serverChk').val());
//				}
//				else if (adcSetting.getAdc().type == 'PiolinkUnknown')
//				{
//					pasVs.loadVsModifyContent($(this).parent().parent().find('.serverChk').val());
//				}
			});
			
			$('.delSlbSchedule').off('click');
			$('.delSlbSchedule').click(function(e)
			{
				delSlbSchedules();
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
				$(this).parent().parent().parent().parent().find('.serverChk').attr('checked', isChecked);
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
	},	
	
	// SLB Schedule F5 Add 
	loadSlbScheduleF5AddContent : function(isModify) 
	{
		with (this) 
		{
			newPoolName = '';
			ajaxManager.runHtmlExt({
				url : "slbSchedule/loadSlbScheduleAddContent.action",
				data : 
				{
					"f5VsAdd.adcIndex" : adcSetting.getAdc().index,
					"adc.index" : adcSetting.getAdc().index,
					"adc.type" : adcSetting.getAdc().type,
					"adc.name" : adcSetting.getAdc().name
				},
				target: "#wrap .contents",
				successFn : function(params) 
				{
					virtualServer.setActiveContent('VsAddContent');
					f5Vs.$unassignedAccountOptions = $();
//					f5Vs.registerVsAddContentEvents(false);
					f5Vs._applyMemberTableCss();
//					_registerListContentEvents();
					registerF5VsAddContentEvents(isModify);
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_VS_SCHEDULELOADFAIL, jqXhr);
				}	
			});
		}
	},
	// SLB Schedule F5 Modify 
	loadSlbScheduleF5ModifyContent : function(index, vsIdx, hr, min) 
	{
		with (this) 
		{
			newPoolName = '';
			ajaxManager.runHtmlExt({
				url : "slbSchedule/loadSlbScheduleModifyContent.action",
				data : 
				{
					"f5VsAdd.index" : vsIdx,
					"f5VsAdd.adcIndex" : adcSetting.getAdc().index,
					"scheduleIndex" : index,
					"adc.index" : adcSetting.getAdc().index,
					"adc.type" : adcSetting.getAdc().type,
					"adc.name" : adcSetting.getAdc().name
				},
				target: "#wrap .contents",
				successFn : function(params) 
				{					
					f5Vs.$unassignedAccountOptions = $();
//					vsIndex = index;
//					f5Vs.registerVsAddContentEvents(true);
					f5Vs._applyMemberTableCss();
					
					scheduleHr = hr;
					scheduleMin = min;
					
					scheduleSet(hr, min);
					registerF5VsAddContentEvents(true);
					
					$('#scheduleDate').attr("disabled", "disabled");
					$('.scheduleHour').attr("disabled", "disabled");
					$('.scheduleMin').attr("disabled", "disabled");
					$('.scheduleNotice').attr("disabled", "disabled");
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_VS_SCHEDULELOADFAIL, jqXhr);
				}	
			});
		}
	},	
	scheduleSet : function(hr, min)
    {  
		$('#scheduleHour option[value="' + hr  + '"]').attr("selected", "");
		$('#scheduleMin option[value="' + min  + '"]').attr("selected", "");
    },
	// SLB Schedule Alteon Add 
	loadSlbScheduleAlteonAddContent : function(isModify)
	{
		with (this)
		{
			newPoolName = '';
			ajaxManager.runHtmlExt({
				url : "slbSchedule/loadSlbScheduleAddContent.action",
				data :
				{
					"alteonVsAdd.adcIndex" : adcSetting.getAdc().index,
					"adc.index" : adcSetting.getAdc().index,
					"adc.type" : adcSetting.getAdc().type,
					"adc.name" : adcSetting.getAdc().name
				},
				target: "#wrap .contents",
				successFn : function(params)
				{
//					virtualServer.setActiveContent('VsAddContent');
//					alteonVs.applyVsAddContentEvents();
//					alteonVs.registerVsAddContentEvents(false);
					registerAlteonVsAddContentEvents(isModify);
//					_registerListContentEvents(isModify);
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_VS_SCHEDULELOADFAIL, jqXhr);
//					exceptionEvent();
				}	
			});
		}
	},
	// SLB Schedule Alteon Modify 
	loadSlbScheduleAlteonModifyContent : function(index, vsIdx, hr, min) 
	{
		with (this)
		{
			newPoolName = '';
			ajaxManager.runHtmlExt({
				url : "slbSchedule/loadSlbScheduleModifyContent.action",
				data :
				{
					"alteonVsAdd.index" : vsIdx,
					"alteonVsAdd.adcIndex" : adcSetting.getAdc().index,
					"scheduleIndex" : index,
					"adc.index" : adcSetting.getAdc().index,
					"adc.type" : adcSetting.getAdc().type,
					"adc.name" : adcSetting.getAdc().name
				},
				target: "#wrap .contents",
				successFn : function(params)
				{
//					virtualServer.setActiveContent('VsModifyContent');
//					vsIndex = index;
//					alteonVs.applyVsAddContentEvents();
//					alteonVs.registerVsAddContentEvents(true);
//					registerAlteonVsAddContentEvents(true);
					
					scheduleHr = hr;
					scheduleMin = min;
					
					scheduleSet(hr, min);
					registerAlteonVsAddContentEvents(true);
//					_registerListContentEvents(isModify);
				},
				completeFn: function()
				{
//					var descField = $('input[name="schedule.smsReceive"]');
					var descField = $('.scheduleSmReceiveUsers');
					
					descField.val($.map(descField.text().split('|'), function(item)
					{
						return item.split(',')[0];
					}).join(',')); // '샤오미,123213|테스트,123213' -> '샤오미,테스트'
					
					$('#scheduleDate').attr("disabled", "disabled");
					$('.scheduleHour').attr("disabled", "disabled");
					$('.scheduleMin').attr("disabled", "disabled");
					$('.scheduleNotice').attr("disabled", "disabled");
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_VS_SCHEDULELOADFAIL, jqXhr);
//					exceptionEvent();
				}	
			});
		}
	},
	
	delSlbSchedules : function()
	{
		with (this)
		{
			var checkSchedules = _getCheckSchdules();
			if (checkSchedules.length == 0)
			{
				$.obAlertNotice(VAR_VS_SCHEDULEDELSEL);
				return;
			}
			
			var chk = confirm(VAR_VS_SCHEDULEDEL);
			if (chk == false)
			{
				return;
			}
			
			ajaxManager.runHtmlExt({
				url : "slbSchedule/delSlbSchedule.action",
				data : 
				{
					"adc.index" : adcSetting.getAdc().index,
					"adc.type" : adcSetting.getAdc().type,
					"adc.name" : adcSetting.getAdc().name,
					"scheduleIndexes" : checkSchedules,
					"orderDir" : this.orderDir,
					"orderType" : this.orderType
				},
				target : "#wrap .contents_area",
				successFn : function(params)
				{
					loadListContent();
//					loadScheduleListContent(searchKey, undefined, undefined, refreshes);	
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_VS_SCHEDULEDLFAIL, jqXhr);
				}
			});
		}
	},
	
	indexOfExt : function(list, item)
	{
		var len = list.length;
		
		for (var i = 0; i < len; i++) {
			if (list[i].hp === item.hp) {
//				break;
				return i;
//				return 0;
		    }
		}
		return -1;
	},
	registerAlteonVsAddContentEvents : function(isModify)
	{
		with (this)
		{
			// 예약요청자 리스트 pop-up
			$('.popUpRequestorNameWndLnk').off('click');
			$('.popUpRequestorNameWndLnk').click(function(e) 
			{
				with (this)
				{
					var requesteIdx = $(this).data('index');
					
					slbTypeIdx = requesteIdx;
					popUp(slbTypeIdx);
				}
			});
			
			// last RequestUser Info - slbSchedule table last request user info
			$('#requestorSel').off('click');
			$('#requestorSel').click(function(e)
			{
				loadLastResponseUserInfo();
			});
			// new Request User Info
			$('#newSel').off('click');
			$('#newSel').click(function(e)
			{
				$('.slbUsrTable #slbUsrName').val('');
				$('.slbUsrTable #slbUsrTeam').val('');
				$('.slbUsrTable #slbUsrPhone').val('');
			});

			if (!syncTime) 
			{
				syncTime = new Date();
			}	
			
			$('input[name="startTime"]').datepicker({
//			$('#scheduleDate').datepicker({
				minDate: "0",
				dateFormat : "yy-mm-dd",
				showOn: "button",
				buttonImage: "imgs/meun/btn_calendar.png",
				buttonImageOnly: true,
				defaultDate: syncTime,
				onSelect: function(dateText, inst)
				{					
					syncTime = $("input[name='startTime']").datepicker("getDate");					
				}
			});
			
			$('#scheduleHour').change(function() 
			{
				everyHr = $('#scheduleHour option:selected').val();
			});
			
			$('#scheduleMin').change(function() 
			{
				scheduleMin = $('#scheduleMin option:selected').val();
			});
					
//			var currentDate = new Date();
//			$('#scheduleDate').val(currentDate.getFullYear() + "-" + (currentDate.getMonth() + 1) + "-" + currentDate.getDate());
//			$('#scheduleDate').addClass('onSchedule onOnce none');
//			$('.ui-datepicker-trigger').addClass('onSchedule onOnce none');
			
			// 기존 slb 정보
			$('.slbList').off('click');
			$('.slbList').click(function(e)
			{
				$('#orgSlb').attr('checked', 'checked');
				$('#newSlb').removeAttr('checkekd');
					
//				$('.slbUsrReqList').addClass('none');
//				$('.slbUsrRespList').addClass('none');
//				$('.slbList').removeClass('none');
//				
//				$('#slbUsrReqList .reqList').addClass('none');
//				$('#slbUsrRespList .respList').addClass('none');
//				$('#slbList .vsList').removeClass('none');
				
				$('.slbListPopup').click();				
			});
			// new Slb info
			$('.newSlbInfo').off('click');
			$('.newSlbInfo').click(function(e)
			{
				$('#newSlb').attr('checked', 'checked');
				$('#orgSlb').removeAttr('checkekd');
				
				if(adcSetting.getAdc().type == "Alteon")
					loadSlbScheduleAlteonAddContent(false);
				else if (adcSetting.getAdc().type == "F5")
					loadSlbScheduleF5AddContent(false);
				else
					loadSlbScheduleAlteonAddContent(false);
			});
			
			$('.scheduleNotice').off('change');
			$('.scheduleNotice').change(function(e)
			{
				$('input[name="notice"]').val($(this).val());
			});
			
			$('.popUpVServerNameWndLnk').off('click');
			$('.popUpVServerNameWndLnk').click(function(e)
			{
				vServerNameWnd.popUp();
			});
			
			$('.slbListPopup').off('click');
			$('.slbListPopup').click(function(e)
			{
//				e.preventDefault();
//				$('#orgSlb').attr('checked', 'checked');
//				$('#newSlb').removeAttr('checkekd');
				
//				$('.slbUsrReqList').addClass('none');
//				$('.slbUsrRespList').addClass('none');
//				$('.slbList').removeClass('none');
//				
//				$('#slbUsrReqList .reqList').addClass('none');
//				$('#slbUsrRespList .respList').addClass('none');
//				$('#slbList .vsList').removeClass('none');
				
//				$('.popup_type1').remove();
//				$('.cloneDiv').remove();
				
				// popup으로 리스트 선택 가능하게 popup 
//								virtualServer.loadVirtualSvrListContent();     // 기존 리스트 페이지를 불러오는 부분으로 테스트
				with (this)
				{
//									var requesteIdx = $(this).data('index');
//									popUp(requesteIdx);
//					showPopup({
//						"id" : "#slbList",
//						"width" : "600px"
//					});
				
//							_loadVServerListContent(slbTypeIdx);
					loadSlbListCountContent(null, slbTypeIdx);
				}
			});
			
			$('.virtualServerAddCancelLnk').off('click');
			$('.virtualServerAddCancelLnk').click(function(e)
			{
				e.preventDefault();
				loadListContent();
			});
			
			$('.virtualServerAddOkLnk').off('click');
			$('.virtualServerAddOkLnk').click(function(e)
			{
				e.preventDefault();				
//				isModify = false;				
				$('#slbScheduleAddFrm').submit();
//								$('#alteonVsAddFrm').submit();
			});
			
			$('#slbScheduleAddFrm').off('submit');
			$('#slbScheduleAddFrm').submit(function()
			{
				FlowitUtil.log(Object.toJSON(alteonVs._getVirtualSvcs()));
				
				if (!_validateScheduleAdd())
					return false;
				if (!alteonVs._validateVsAdd())
					return false;
				
//				if(!isModify && $('#idVsName').val().trim() != '')
//				{
//					for(var i=0; i<vsNameCheck.length; i++) //이름 중복 체크. junhyun.ok_GS
//					{
//						if(vsNameCheck[i] == $('#idVsName').val())
//						{
//							$.obAlertNotice(VAR_ALT_NAMEDUPLICATE);
//							return false;
//						}
//					}
//				}
				if ($('input[name="slbInfo"]:checked').val() == 1)
					vsModify = true;
				else
					vsModify = false;
				
				var startTimeL = getManuallyTimeToLong();
//				var notice = $('input[name="schedule.notice"]').val();
				
				var adcType = "";
				if (adcSetting.getAdc().type == "Alteon")
					adcType = 2;
				else
					adcType = 1;
				
				// change type : 1 Add, change type :2 Modify
				if(vsModify)
					changeType = 2;
				else 
					changeType = 1;
				
				
				var smRequestUser = new Array();
				
				var userNameList = [];
				var userNm = $('#slbUsrName').val();
			    var userHp = $('#slbUsrPhone').val();
			    
			    var accountNm = $('.loginIdLnk').children('span').eq(1).text().trim();
			    var accountHp = $('.loginIdLnk').children('span').eq(2).text().trim();
			    
				var userInfo = new Object();
				var accountInfo = new Object();
						
				userInfo.name = userNm;
				userInfo.hp = userHp;
			    
				accountInfo.name = accountNm;
				accountInfo.hp = accountHp;
				
			    smRequestUser.push(userInfo);
				smRequestUser.push(accountInfo);
			    
			    
			    
				userNameList.push(userNm);
				
				
				
				//userNameList[0]
				//"smart55"
				//userNameList
				//["smart55"]
				//smReceiveList
				//["smart55", "test4", "test3", "test2"]
				
				if(orgSmReceiveList != null)
				{
					if(orgSmReceiveList.indexOf(userNameList[0]) < 0)
						orgSmReceiveList.push(userNameList[0]);
					
					if(indexOfExt(slbReceiverLists, smRequestUser[0]) < 0)
						slbReceiverLists.push(smRequestUser[0]);
					
					slbReceiverLists.push(smRequestUser[1]);
				}
				
				
				var myJsonString = JSON.stringify(slbReceiverLists);
				
				/*
				smRequestUser
				[Object
					hp: "01044448555"
					name: "smart55"
					__proto__: Object]
				smRequestUser[0]
				Object {name: "smart55", hp: "01044448555"}
				*/
				/*
				slbReceiverLists
				[Object
					hp: "01044448555"
					name: "smart55"
					__proto__: Object, 
				 Object
				 	hp: "01000001111"
				 	name: "test4"
				 	__proto__: Object, 
				 Object
				 	hp: "01011112222"
				 	name: "test3"
				 	__proto__: Object] 
				 */
				
//				if(slbReceiverLists.indexOf(indexOfExt(slbReceiverLists, smRequestUser[0])) < 0)
				
				
				$('#respDesc').val(orgSmReceiveList);
				$('.scheduleSmReceive').val(myJsonString);
				$('.scheduleSmReceiveUsers').text(orgSmReceiveList);
				
				
				var slbIndex = "";
				if(isModify)
					slbIndex = $('input[name="slbUser.index"]').val();
				else
//					slbIndex = $('.cloneDiv input[name="slbUser.index"]').val();
					slbIndex = $('input[name="slbUser.index"]').val();
				
				var params = 
				{
//					"slbUser.type" : 1,
					"slbUserIndex" : slbIndex, //$('.cloneDiv input[name="slbUser.index"]').val(),
					"slbUserType" : 1,
					"slbUserName" : $('input[name="schedule.name"]').val(),
					"slbUserTeam" : $('input[name="schedule.team"]').val(),
					"slbUserPhone" : $('input[name="schedule.phone"]').val(),
					"scheduleIndex" : $('input[name="schedule.index"]').val(),
					"schedule.originUser" : $('input[name="slbUser.index"]').val(),						
					"schedule.notice" : $('.scheduleNotice option:selected').val(),
//					"schedule.notice" : notice, //$('input[name="notice"]').val().trim(),
//					"smsReceiveCheckInString" : $.map($('.scheduleSmReceive').val().split('|'), function(item)
//					{
//						var attrs = item.split(',');
//						return {name: attrs[0], value: attrs[1]}; // [{name: '에이스', value: '0100000'}]
//					}),
//					"smsReceiveCheckInString11" : Object.toJSON(_getCheckedReceivers()),
//					"smsReceiveCheckInString" : JSON.stringify($('.scheduleSmReceive').val()),
//					"smsReceiveCheckInString" : $('.scheduleSmReceive').val(),
//					"smsReceiveCheckInString" : JSON.stringify(selectedReceiver),
//					"schedule.smsReceive" : $('.scheduleSmReceive').val(),
					"smsReceive" : $('.scheduleSmReceive').val(),
					"smsReceiveText" : $('.scheduleSmReceive').text(),
					"schedule.adcIndex" : adcSetting.getAdc().index,
					"schedule.adcType" : adcType, //adcSetting.getAdc().type,
					"schedule.changeType" : changeType,
					"schedule.changeYN" : 3,
					"startTimeL" : startTimeL,
					"schedule.reservedTime" : $('input[name="startTime"]').val(),
					"schedule.reservedHour" : $('.scheduleHour option:selected').val(),
					"schedule.reservedMin" : $('.scheduleMin option:selected').val(),
					"startTime" : $('input[name="startTime"]').val(),
					"hour" : $('.scheduleHour option:selected').val(),
					"min" : $('.scheduleMin option:selected').val(),					
					"alteonVsAdd.virtualSvcsInString" : Object.toJSON(alteonVs._getVirtualSvcs()),
					"alteonVsAdd.index" : vsIndex,
					"alteonVsAdd.adcIndex" : adcSetting.getAdc().index
				};
				
			    $(this).ajaxSubmit({
					dataType : 'json',
					url : isModify ? "slbSchedule/modifySlbSchedule.action" : "slbSchedule/addSlbSchedule.action",
					data : params,
					success : function(data)
					{
						if (!data.isSuccessful)
						{
							$.obAlertNotice(data.message);//중복 메세지 처리.
//							virtualServer.loadListContent();
							return;
						}
						loadListContent();
					},
					error : function(jqXhr)
					{
						if(isModify)
							$.obAlertAjaxError(VAR_VS_SCHEDULE_MODIFYFAIL, jqXhr);
						else
							$.obAlertAjaxError(VAR_VS_SCHEDULE_ADDFAIL, jqXhr);
					}
				}); 
		        // always return false to prevent standard browser submit and page navigation
		        return false; 
		    });
		
			$('input[name="alteonVsAdd.name"]').blur(function(e)
			{
				FlowitUtil.log('blur! -- ' + $(this).val());
				if (!$(this).val())
					return;
				
				alteonVs._showVirtualSvrNameAvailable($(this));
			});
			
			$('input[name="alteonVsAdd.ip"]').blur(function(e)
			{
				FlowitUtil.log('blur! -- ' + $(this).val());
//				if(FlowitUtil.checkIp($(this).val()))
//				{
//					_checkVSIPAddress($(this).val());
////					$(this).next().html('<img class="mar_rgt5" src="imgs/common/bu_conn_on.png"/>+VAR_COMMON_AVAIL);
				alteonVs._setAlteonId($(this).val());
//				} 
//				else
//				{
//					$(this).next().html('<img class="mar_rgt5" src="imgs/common/bu_conn_off.png"/>' +VAR_COMMON_NAVAIL);
//				}
			});		
			
			$('.addVirtualSvcLnk').off('click');
			$('.addVirtualSvcLnk').click(function(e)
			{
				FlowitUtil.log('addVirtualSvcLnk! -- ');
				e.preventDefault();
				var version = $('input[name="version"]').val().split(".");
				var popupSize = '';
				if(version[0] == '29')
				{
					popupSize = '1000px';
				}
				else
				{
					popupSize = '800px';
				}
				alteonVs._popUpVirtualSvc(popupSize);
				
			});
			
			$('.virtualSvcTbd').on('click', '.virtualSvcPortLnk', function(e)
			{
				e.preventDefault();
				var $tr = $(this).parent().parent();
				var version = $('input[name="version"]').val().split(".");
				var popupSize = '';
				if(version[0] == '29')
				{
					popupSize = '1000px';
				}
				else
				{
					popupSize = '800px';
				}
				alteonVs._popUpVirtualSvc(popupSize, $tr.parent().children().index($tr), $.parseJSON($tr.find('.virtualSvcsJson').text()));
			});
			
			$('.allVirtualSvcsChk').off('click');
			$('.allVirtualSvcsChk').click(function(e)
			{
				var isChecked = $(this).is(':checked');
				$(this).parent().parent().parent().parent().find('.virtualSvcsChk').attr('checked', isChecked);
			});
			
			$('.delVirtualSvcs').off('click');
			$('.delVirtualSvcs').click(function()
			{		//Virtual Service 삭제 (Alteon)
				with (this)
				{
					var chkDel = $(this).parent().parent().parent().parent().find('.virtualSvcsChk').filter(':checked').map(function()
					{
						return $(this).val(); 					//alert("chkDel : " + chkDel);
					}).get();
					
					if (chkDel.length == 0)
					{
						$.obAlertNotice(VAR_ALT_VSDSEL);
						return;
					}
					
					var $checkedTrs = $('.virtualSvcTbd > tr').filter(function(index)
					{
						return $(this).find('.virtualSvcsChk').is(':checked');
					});
					
					var chk = confirm(VAR_ALT_VSDEL);
					if (chk)
					{
						$checkedTrs.remove();
						$('.allVirtualSvcsChk').attr('checked', false);
						alteonVs._setVirtualSvcCount();
					}
					else
					{
						return false;
					}
	//				$checkedTrs.remove();
	//				$('.allVirtualSvcsChk').attr('checked', false);
	//				_setVirtualSvcCount();
				}
			});
			
			$('.interfacesCbx').change(function(e)
			{
				$('input[name="alteonVsAdd.interfaceNo"]').val($(this).val());
			});
			
//			$('.virtualServerAddCancelLnk').click(function(e)
//			{
//				e.preventDefault();
//				virtualServer.loadListContent();
//			});
			
//			$('.virtualServerAddOkLnk').click(function(e)
//			{
//				e.preventDefault();
//				$('#alteonVsAddFrm').submit();
//			});
			
			alteonVs._registerVsNameAutoCompleteEvents();
			
			if (adcSetting.getAdcStatus() != "available" || adcSetting.getAdc().mode == 1 || $('.cloneDiv input[name="rPort"]').val() == 0) 
			{
				$('.imgOff').removeClass("none");
				$('.imgOn').addClass("none");
				$('input[name="alteonVsAdd.ip"]').attr("disabled", "disabled");
				$('input[name="alteonVsAdd.name"]').attr("disabled", "disabled");
				$('.allVirtualSvcsChk').attr("disabled", "disabled");
				$('.virtualSvcsChk').attr("disabled", "disabled");
				$('.delVirtualSvcs ').addClass("none");
			}
			else
			{
				$('.imgOff').addClass("none");
				$('.imgOn').removeClass("none");
				$('input[name="alteonVsAdd.ip"]').removeAttr("disabled");
				$('input[name="alteonVsAdd.name"]').removeAttr("disabled");
				$('.allVirtualSvcsChk').removeAttr("disabled");
				$('.virtualSvcsChk').removeAttr("disabled");
				$('.delVirtualSvcs ').removeClass("none");
			}
//			var selectedVersion = adcSetting.getAdcVersion();			
//			if(selectedVersion.startsWith("29.5") == true)
//			{
//				$('.imgOff').removeClass("none");
//				$('.imgOn').addClass("none");
//			}
//			else
//			{
//				$('.imgOff').addClass("none");
//				$('.imgOn').removeClass("none");
//			}
		}
	},
	registerF5VsAddContentEvents : function(isModify) 
	{
		with (this) 
		{
			// 예약요청자 리스트 pop-up
			$('.popUpRequestorNameWndLnk').off('click');
			$('.popUpRequestorNameWndLnk').click(function(e) 
			{
				with (this)
				{
					var requesteIdx = $(this).data('index');
					
					slbTypeIdx = requesteIdx;
					popUp(slbTypeIdx);
				}
			});
			
			// last RequestUser Info - slbSchedule table last request user info
			$('#requestorSel').off('click');
			$('#requestorSel').click(function(e)
			{
				loadLastResponseUserInfo();
			});
			// new Request User Info
			$('#newSel').off('click');
			$('#newSel').click(function(e)
			{
				$('.slbUsrTable #slbUsrName').val('');
				$('.slbUsrTable #slbUsrTeam').val('');
				$('.slbUsrTable #slbUsrPhone').val('');
			});

			if (!syncTime) 
			{
				syncTime = new Date();
			}	
			
			$('input[name="startTime"]').datepicker({
//			$('#scheduleDate').datepicker({
				minDate: "0",
				dateFormat : "yy-mm-dd",
				showOn: "button",
				buttonImage: "imgs/meun/btn_calendar.png",
				buttonImageOnly: true,
				defaultDate: syncTime,
				onSelect: function(dateText, inst)
				{					
					syncTime = $("input[name='startTime']").datepicker("getDate");					
				}
			});
//			var currentDate = new Date();
//			$('#scheduleDate').val(currentDate.getFullYear() + "-" + (currentDate.getMonth() + 1) + "-" + currentDate.getDate());
//			$('#scheduleDate').addClass('onSchedule onOnce none');
//			$('.ui-datepicker-trigger').addClass('onSchedule onOnce none');
			
			// 기존 slb 정보
			$('.slbList').off('click');
			$('.slbList').click(function(e)
			{
				$('#orgSlb').attr('checked', 'checked');
				$('#newSlb').removeAttr('checkekd');
					
				$('.slbUsrReqList').addClass('none');
				$('.slbUsrRespList').addClass('none');
				$('.slbList').removeClass('none');
				
				$('#slbUsrReqList .reqList').addClass('none');
				$('#slbUsrRespList .respList').addClass('none');
				$('#slbList .vsList').removeClass('none');
				
				$('.slbListPopup').click();				
			});
			// new Slb info
			$('.newSlbInfo').off('click');
			$('.newSlbInfo').click(function(e)
			{
				$('#newSlb').attr('checked', 'checked');
				$('#orgSlb').removeAttr('checkekd');
				
				if(adcSetting.getAdc().type == "Alteon")
					loadSlbScheduleAlteonAddContent(false);
				else if (adcSetting.getAdc().type == "F5")
					loadSlbScheduleF5AddContent(false);
				else
					loadSlbScheduleAlteonAddContent(false);
			});
			
			$('.scheduleNotice').off('change');
			$('.scheduleNotice').change(function(e)
			{
				$('input[name="notice"]').val($(this).val());
			});
			
			$('.popUpVServerNameWndLnk').click(function(e)
			{
				vServerNameWnd.popUp();
			});
			
			$('.slbListPopup').off('click');
			$('.slbListPopup').click(function(e)
			{
				$('#orgSlb').attr('checked', 'checked');
				$('#newSlb').removeAttr('checkekd');
				
				$('.slbUsrReqList').addClass('none');
				$('.slbUsrRespList').addClass('none');
				$('.slbList').removeClass('none');
				
				$('#slbUsrReqList .reqList').addClass('none');
				$('#slbUsrRespList .respList').addClass('none');
				$('#slbList .vsList').removeClass('none');
				// popup으로 리스트 선택 가능하게 popup 
//								virtualServer.loadVirtualSvrListContent();     // 기존 리스트 페이지를 불러오는 부분으로 테스트
				with (this)
				{
//									var requesteIdx = $(this).data('index');
//									popUp(requesteIdx);
					showPopup({
						"id" : "#slbList",
						"width" : "600px"
					});
				
//							_loadVServerListContent(slbTypeIdx);
					loadSlbListCountContent(null, slbTypeIdx);
				}
			});
			
			$('.virtualServerAddCancelLnk').off('click');
			$('.virtualServerAddCancelLnk').click(function(e)
			{
				e.preventDefault();
				loadListContent();
			});
			
			$('.virtualServerAddOkLnk').off('click');
			$('.virtualServerAddOkLnk').click(function(e)
			{
				e.preventDefault();				
//				isModify = false;
				
				$('#slbScheduleAddFrm').submit();
//								$('#alteonVsAddFrm').submit();
			});
			
			$('#slbScheduleAddFrm').off('submit');
			$('#slbScheduleAddFrm').submit(function() 
			{
				if (!_validateScheduleAdd())
					return false;
				
				var f5VsModify = "";
				
				if ($('input[name="slbInfo"]:checked').val() == 1)
					f5VsModify = true;
				else
					f5VsModify = false;
				
				if (!f5Vs._validateVsAdd(f5VsModify))
					return false;		
				
				f5Vs._updatePoolIndexInPoolCbxByPoolNameInPoolText();
				FlowitUtil.log(Object.toJSON(f5Vs._getRegMembers()));
				f5Vs.selectRegisteredVlansForSubmit();
//				_vsAddModify(isModify);
				
				var startTimeL = getManuallyTimeToLong();
//				var notice = $('input[name="schedule.notice"]').val();
				
				var adcType = "";
				if (adcSetting.getAdc().type == "Alteon")
					adcType = 2;
				else
					adcType = 1;
				
				// change type : 1 Add, change type :2 Modify
				if(f5VsModify)
					changeType = 2;
				else 
					changeType = 1;		
				
				
				var smRequestUser = new Array();
				
				var userNameList = [];
				var userNm = $('#slbUsrName').val();
			    var userHp = $('#slbUsrPhone').val();			    
			    
			    var accountNm = $('.loginIdLnk').children('span').eq(1).text().trim();
			    var accountHp = $('.loginIdLnk').children('span').eq(2).text().trim();
			    
				var userInfo = new Object();
				var accountInfo = new Object();
						
				userInfo.name = userNm;
				userInfo.hp = userHp;
			    
				accountInfo.name = accountNm;
				accountInfo.hp = accountHp;
				
			    smRequestUser.push(userInfo);
				smRequestUser.push(accountInfo);
			    
				userNameList.push(userNm);
				
				if(orgSmReceiveList != null)
				{
					if(orgSmReceiveList.indexOf(userNameList[0]) < 0)
						orgSmReceiveList.push(userNameList[0]);
					
					if(indexOfExt(slbReceiverLists, smRequestUser[0]) < 0)
						slbReceiverLists.push(smRequestUser[0]);
					
					slbReceiverLists.push(smRequestUser[1]);
				}
								
				var myJsonString = JSON.stringify(slbReceiverLists);
												
				$('#respDesc').val(orgSmReceiveList);
				$('.scheduleSmReceive').val(myJsonString);
				$('.scheduleSmReceiveUsers').text(orgSmReceiveList);
				
				
				var slbIndex = "";
				if(isModify)
					slbIndex = $('input[name="slbUser.index"]').val();
				else
//					slbIndex = $('.cloneDiv input[name="slbUser.index"]').val();
					slbIndex = $('input[name="slbUser.index"]').val();
				
				var params = 
				{
//					"slbUser.index" : $('input[name="slbUser.index"]').val(),
//					"slbUser.name" : $('input[name="schedule.name"]').val(),
//					"slbUser.team" : $('input[name="schedule.team"]').val(),
//					"slbUser.phone" : $('input[name="schedule.phone"]').val(),
//					"slbUser.type" : 1,
					"slbUserIndex" : slbIndex, //$('.cloneDiv input[name="slbUser.index"]').val(),
					"slbUserType" : 1,
					"slbUserName" : $('input[name="schedule.name"]').val(),
					"slbUserTeam" : $('input[name="schedule.team"]').val(),
					"slbUserPhone" : $('input[name="schedule.phone"]').val(),
					"scheduleIndex" : $('input[name="schedule.index"]').val(),
					"schedule.originUser" : $('input[name="slbUser.index"]').val(),
					"schedule.notice" : $('.scheduleNotice option:selected').val(),
//						"schedule.notice" : notice, //$('input[name="notice"]').val().trim(),
					"smsReceive" : $('.scheduleSmReceive').val(),
					"smsReceiveText" : $('.scheduleSmReceive').text(),
//					"schedule.smsReceive" : $.map($('.scheduleSmReceive').val().split('|'), function(item)
//					{
//						var attrs = item.split(',');
//						return {name: attrs[0], value: attrs[1]}; // [{name: '에이스', value: '0100000'}]
//					}),
//					"schedule.smsReceive" : $.map($('.scheduleSmReceive').text().split('|'), function(item)
//					{
//						var attrs = item.split(',');
//						return {name: attrs[0], value: attrs[1]}; // [{name: '에이스', value: '0100000'}]
//					}),
					"schedule.adcIndex" : adcSetting.getAdc().index,
					"schedule.adcType" : adcType, //adcSetting.getAdc().type,
					"schedule.changeType" : changeType,
					"schedule.changeYN" : 3,
					"startTimeL" : startTimeL,
					"schedule.reservedTime" : $('input[name="startTime"]').val(),
					"schedule.reservedHour" : $('.scheduleHour option:selected').val(),
					"schedule.reservedMin" : $('.scheduleMin option:selected').val(),
					"startTime" : $('input[name="startTime"]').val(),
					"hour" : $('.scheduleHour option:selected').val(),
					"min" : $('.scheduleMin option:selected').val(),
//					"f5VsAdd.index" : vsIndex,
					"f5VsAdd.index" : vsIndex,
					"f5VsAdd.membersInString" : Object.toJSON(f5Vs._getRegMembers()),
					"f5VsAdd.adcIndex" : adcSetting.getAdc().index
				};
				
			    $(this).ajaxSubmit({
					dataType : 'json',
					url : isModify ? "slbSchedule/modifySlbSchedule.action" : "slbSchedule/addSlbSchedule.action",
					data : params,
					success : function(data) 
					{
						if (!data.isSuccessful)
						{
							$.obAlertNotice(data.message);//중복 메세지 처리.
//							virtualServer.loadListContent();
							return;
						}
						loadListContent();
					},
					error : function(jqXhr)
					{
						if(isModify)
							$.obAlertAjaxError(VAR_VS_SCHEDULE_MODIFYFAIL, jqXhr);
						else
							$.obAlertAjaxError(VAR_VS_SCHEDULE_ADDFAIL, jqXhr);
					}
				}); 
			    
		        // always return false to prevent standard browser submit and page navigation
		        return false; 
		    });
			
			$('#selectedMember').fixheadertable({			
				//caption     : 'My employees', 
			    colratio    : [50, 150, 40, 40, 120], 
			    height      : 200, 
			    width       : 420, 
			    zebra       : false,
			    resizeCol   : false,
			    minColWidth : 50 
			});	
			
			$('#memberList').fixheadertable({
			    colratio    : [150, 80], 
			    height      : 200, 
			    width       : 250, 
			    zebra       : true,
			    resizeCol   : true,
			    minColWidth : 50 			
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
			
			$('input[name="f5VsAdd.name"]').blur(function(e) 
			{
				FlowitUtil.log('blur! -- ' + $(this).val());
				if (!$(this).val())
					return;
				
				f5Vs._showVirtualSvrNameAvailable($(this));
			});
			
			$('input[name="f5VsAdd.ip"]').blur(function(e) 
			{
				FlowitUtil.log('blur! -- ' + $(this).val());
				if(FlowitUtil.checkIp($(this).val()))
					$(this).next().html('<img class="mar_rgt5" src="imgs/common/bu_conn_on.png"/>' + VAR_COMMON_AVAIL);
				else
					$(this).next().html('<img class="mar_rgt5" src="imgs/common/bu_conn_off.png"/>' + VAR_COMMON_NAVAIL);
			});
	
			$('input[name="f5VsAdd.port"]').blur(function(e) 
			{
				if (getValidateNumberRange(port, 0, 65535) == false)
				{	
					if ($(this).val() < 65535) 
					{	
						$(this).next().html('<input type=hidden class="mar_rgt5"/>');
					}
					else 
					{
						$(this).next().html('<img class="mar_rgt5" src="imgs/common/bu_conn_off.png"/>' + VAR_COMMON_NAVAIL);
						$.obAlertNotice(VAR_COMMON_SPECIALCHAR);
						$('input[name="f5VsAdd.port"]').val('');
						$('input[name="f5VsAdd.port"]').focus();
						$(this).next().html('<input type=hidden class="mar_rgt5"/>');
					}
				}
				else 
				{ 
					$(this).next().html('<img class="mar_rgt5" src="imgs/common/bu_conn_off.png"/>' + VAR_COMMON_NAVAIL);
//					$.obAlertNotice('입력하신 가상서버 포트는 올바른 방식이 아닙니다.');
					$.obAlertNotice(VAR_COMMON_SPECIALCHAR);
					$('input[name="f5VsAdd.port"]').val('');
					$('input[name="f5VsAdd.port"]').focus();
					$(this).next().html('<input type=hidden class="mar_rgt5"/>');
				}
			});
						
			$('.protocolCbx').change(function(e) 
			{
				$('input[name="f5VsAdd.port"]').val($(this).val());
			});
			
			if($('.poolCbx option:selected').val() != "") //SLB 설정 virtual service 추가시 Group 신규일때만 수정가능하고 기존 등록된 Group은 읽기만. junhyun.ok_GS
			{
				$('input[name="f5VsAdd.poolName"]').attr("readOnly", true);
			}
			else
			{
				$('input[name="f5VsAdd.poolName"]').attr("readOnly", false);
			}
			
			$('.poolCbx').change(function(e) 
			{
				FlowitUtil.log('selIndex: ' + $(this).prop('selectedIndex'));
				var $poolName = $(this).prop('selectedIndex') == 0 ? newPoolName : $(this).children('option').filter(':selected').text();
				$('input[name="f5VsAdd.poolName"]').val($poolName);
				if($('.poolCbx option:selected').val() != "") //SLB 설정 virtual service 추가시 Group 신규일때만 수정가능하고 기존 등록된 Group은 읽기만. junhyun.ok_GS
				{
					$('input[name="f5VsAdd.poolName"]').attr("readOnly", true);
				}
				else
				{
					$('input[name="f5VsAdd.poolName"]').attr("readOnly", false);
				}
				f5Vs._fillVirtualSvc($(this).val());
				f5Vs._fillAdcNodes($(this).val());
			});
			
			$('.memberAddLnk').click(function(e) 
			{
				e.preventDefault();
				f5Vs.moveMemberInputToMemberList();
			});
			
			$('.onMemberAddBatch').click(function(e)
			{
				e.preventDefault();
				f5Vs.memberAddBatchWnd.popUp();
			});
						
			$('.memberTbd').on('dblclick', '.regMemberTr', function(e)
			{				
				with (this) 
				{
					if (header.getAccountRole() === "readOnly" || header.getAccountRole() === "rsAdmin")
					{
						return;
					}
					else
					{						
						var ip = $(this).children().eq(1).text();
						$(this).remove();
						if (f5Vs._isNodeValidate(ip))
						{
							return false;
						}						
						$('.adcNodeTd').eq(0).parent().parent().append('<tr><td class="adcNodeTd ui-widget-content">' + ip + '</td></tr>');
						f5Vs._applyMemberTableCss();
						f5Vs._registerNodeListTableEvents();
						f5Vs._setPoolMemberCount();
						f5Vs._setAdcNodeCount();
					}					
				}
			});
			
			f5Vs._registerNodeListTableEvents();
			
//			$('.virtualServerAddCancelLnk').click(function(e) 
//			{
//				with (this) 
//				{
//				e.preventDefault();
//				virtualServer.loadListContent();
//				}
//			});
//
//			$('.virtualServerAddOkLnk').click(function(e)
//			{
//				e.preventDefault();
//				$('#f5VsAddFrm').submit();
//			});
			
			$(".inputSelect").change(function(){
				var $profileIndex = $('select[name="f5VsAdd.profileIndex"]').val();
				if ($profileIndex == '')
					 $('.persistenceDetailLnk').attr("style", "visibility: hidden");
				else
					$('.persistenceDetailLnk').attr("style", "visibility: visible");
			});
			
			$('.persistenceDetailLnk').click(function(e) 
			{
				e.preventDefault();
				var $profileIndex = $('select[name="f5VsAdd.profileIndex"]').val();
				if ($profileIndex == '')
					return;
				
				persistenceDetailWnd.setIndex($profileIndex);
				persistenceDetailWnd.popUp();
			});
			
			$('.allMembersChk').click(function(e) 
			{
				var isChecked = $(this).is(':checked');
				$('#selectedMember .memberChk').attr('checked', isChecked);
			});
			
			$('.enableMembersLnk').click(function(e) 
			{
				e.preventDefault();
				var $memberTRs = f5Vs._getCheckedMemberTRs();
				FlowitUtil.log($memberTRs.length);
				
				if ($memberTRs.length == 0) 
				{
					$.obAlertNotice(VAR_COMMON_EMSELECT);
					return;
				}
				
				$memberTRs.each(function() 
				{
					FlowitUtil.log($(this).html());
				});
				$memberTRs.find('td option[value="1"]').attr('selected', true);
			});
			
			$('.disableMembersLnk').click(function(e) 
			{
				e.preventDefault();
				var $memberTRs = f5Vs._getCheckedMemberTRs();
				
				if ($memberTRs.length == 0) 
				{
					$.obAlertNotice(VAR_COMMON_DMSELECT);
					return;
				}
				
				$memberTRs.find('td option[value="0"]').attr('selected', true);	
			});
			
			// Forced Offline 추가
			$('.forcedOffMembersLnk').click(function(e)
			{
				e.preventDefault();
				var $memberTRs = f5Vs._getCheckedMemberTRs();
				
				if($memberTRs.length == 0)
				{
					$.obAlertNotice(VAR_COMMON_FORCEDOFFLINEMSELECT);
				}
				
				$memberTRs.find('td option[value="2"]').attr('selected', true);
			});
			
			$('.delMembersLnk').click(function(e) 
			{		//Virtual Server Member 삭제(F5)
				with (this) 
				{
					e.preventDefault();
										
					var chkDel = $(this).parent().parent().parent().parent().find('.memberChk').filter(':checked').map(function() {
						return $(this).val();
					}).get();
					
					//alert("chkDel.length : " + chkDel.length);

					if (chkDel.length == 0) 
					{
						$.obAlertNotice(VAR_COMMON_MDSELECT);
						return;
					}
						
					var $memberTRs = f5Vs._getCheckedMemberTRs();
					
					var chk = confirm(VAR_COMMON_MDEL);
					if (chk) 
					{
						$memberTRs.remove();
						f5Vs._setPoolMemberCount();
					}
					else 
					{
						return false;
					}
//					$memberTRs.remove();
//					_setPoolMemberCount();
				}
			});
			
			$('.changeMemberPortsLnk').click(function(e) 
			{
				e.preventDefault();
				
				if (f5Vs._getCheckedMemberTRs().length == 0) 
				{
					$.obAlertNotice(VAR_COMMON_PMSELECT);
					return;
				}
				portChangeWnd.popUp(f5Vs._getCheckedMemberTRs());
			});
			
			// vlan and tunnels			
			$('.toValnTunnelSelectionLnk').click(function(e) 
			{
				e.preventDefault();
				f5Vs.moveValnTunnelToSelection();
			});
			
			$('.toValnTunnelDeselectionLnk').click(function(e) 
			{
				e.preventDefault();
				f5Vs.moveValnTunnelToDeselection();
			});
			
			$('.selectedVlanTunnelCount').text($('#vlanTunnelSelectedSel').children().length);
			
			$('.valnTunnel').change(function(){
				if($('.valnTunnel option:selected').val() != "0") 
				{
					$('.allVlan').removeClass('none');
				}
				else
				{
					$('.allVlan').addClass('none');
				}
			});
			
			
			if($('.valnTunnel option:selected').val() != "0") 
			{
				$('.allVlan').removeClass('none');
			}
			else
			{
				$('.allVlan').addClass('none');
			}		
						
//			$('input[name="vlanTunnelSearch"]').click(function()
//			{
//				
//			});
			
			// search event
			$('.vlanTunnelSearchLnk').click(function (e) 
			{
				e.preventDefault();
//						$('.Board input.searchTxt').val();
//						var searchKey = $('.Board input.inputText_search').val();
				
				var searchKey = $('input[name="vlanTunnelSearch"]').val();
				FlowitUtil.log('click:' + searchKey);
				f5Vs._searchOnUnassignedVlanTunnel(searchKey);
			});
			
			f5Vs._registerVsNameAutoCompleteEvents();
			
			
			if (adcSetting.getAdcStatus() != "available" || adcSetting.getAdc().mode == 1) 
			{
				$('.imgOff').removeClass("none");
				$('.imgOn').addClass("none");
				$('input').attr('readonly','readonly');
				$('select').attr('disabled','disabled');
				$('.memberTbd').off('dblclick');
				$('.adcNodeTd').off('click');
				$('.f5vsmodfiyOn').addClass("none");
				$('.contents_area input').off('blur');
			}
			else
			{
				$('.imgOff').addClass("none");
				$('.imgOn').removeClass("none");
				$('.f5vsmodfiyOn').removeClass("none");
			}
		}
	},
	
	getManuallyTimeToLong : function()
	{
		with(this)
		{
			var manuallyTime = new Date();
			var retVal;
			var splitDateObj = $('input[name="startTime"]').val().split("-");
			manuallyTime.setFullYear(splitDateObj[0]);
			manuallyTime.setMonth(splitDateObj[1] -1);
			manuallyTime.setDate(splitDateObj[2]);
			manuallyTime.setHours($('#scheduleHour').val());
			manuallyTime.setMinutes($('#scheduleMin').val());
			retVal = manuallyTime.getTime();
			return retVal;		
		}
	},
	/*_registerVsAddContentEvents : function(isModify)
	{
		with (this)
		{
			$('#slbScheduleAddFrm').submit(function()
			{
				FlowitUtil.log(Object.toJSON(alteonVs._getVirtualSvcs()));
				if (!alteonVs._validateVsAdd())
					return false;
				
				if(!isModify && $('#idVsName').val().trim() != '')
				{
					for(var i=0; i<vsNameCheck.length; i++) //이름 중복 체크. junhyun.ok_GS
					{
						if(vsNameCheck[i] == $('#idVsName').val())
						{
							$.obAlertNotice(VAR_ALT_NAMEDUPLICATE);
							return false;
						}
					}
				}
				
				var startTimeL = getManuallyTimeToLong();
				var notice = $('input[name="schedule.notice"]').val();
				
				var adcType = "";
				if (adcSetting.getAdc().type == "Alteon")
					adcType = 2;
				else
					adcType = 1;
				
				if(isModify)
					changeType = 2;
				else 
					changeType = 1;
				
				
				var params = 
				{
//					"slbUser.index" : $('input[name="slbUser.index"]').val(),
//					"slbUser.type" : $('input[name="slbUser.type"]').val(),
//					"slbUser.name" : $('input[name="slbUser.name"]').val(),
//					"slbUser.team" : $('input[name="slbUser.team"]').val(),
//					"slbUser.phone" : $('input[name="slbUser.phone"]').val(),					
//					"schedule.index" : $('input[name="slbUser.index"]').val(),					
//					"schedule.originUser" : $('input[name="slbUser.name"]').val() + ',' + $('input[name="slbUser.phone"]').val(),
//					"schedule.name" : $('input[name="slbUser.name"]').val(),
//					"schedule.team" : $('input[name="slbUser.team"]').val(),
//					"schedule.phone" : $('input[name="slbUser.phone"]').val(),
//					"slbUserIndex" : $('input[name="slbUser.index"]').val(),
					"schedule.originUser" : $('input[name="slbUser.index"]').val(),
//					"schedule.notice" : $('.scheduleNotice option:selected').val(),
					"schedule.notice" : notice, //$('input[name="notice"]').val().trim(),
					"schedule.smsReceive" : $('.scheduleSmReceive').val(),
					"schedule.adcIndex" : adcSetting.getAdc().index,
					"schedule.adcType" : adcType, //adcSetting.getAdc().type,
					"schedule.changeType" : changeType,
					"schedule.changeYN" : 2,
					"startTimeL" : startTimeL,
					"schedule.reservedTime" : $('input[name="startTime"]').val(),
					"schedule.reservedHour" : $('.scheduleHour option:selected').val(),
					"schedule.reservedMin" : $('.scheduleMin option:selected').val(),
					"startTime" : $('input[name="startTime"]').val(),
					"hour" : $('.scheduleHour option:selected').val(),
					"min" : $('.scheduleMin option:selected').val(),
					
//					"schedule.reservationTime" : $('#scheduleDate').val() + $('.scheduleHour option:selected').val() + $('.scheduleMin option:selected').val(),
					"alteonVsAdd.virtualSvcsInString" : Object.toJSON(alteonVs._getVirtualSvcs()),
					"alteonVsAdd.index" : vsIndex,
//					"f5VsAdd.index" : vsIndex,
//					"f5VsAdd.membersInString" : Object.toJSON(f5Vs._getRegMembers())
				};
				
				console.log("parameter : ", params);
				
				$(this).ajaxSubmit({
					dataType : 'json',
					url : isModify ? "slbSchedule/modifySlbSchedule.action" : "slbSchedule/addSlbSchedule.action",
					data : params,
					success : function(data)
					{
						if (!data.isSuccessful)
						{
							$.obAlertNotice(data.message);
							return;
						}
						
						ajaxManager.runJsonExt({
							url : "virtualServer/checkPairIndex.action",
							data :
							{
								"alteonVsAdd.adcIndex" 		: data.alteonVsAdd.adcIndex,
								"alteonVsAdd.alteonId" 		: data.alteonVsAdd.alteonId,
								"alteonVsAdd.index" 		: data.alteonVsAdd.index,
								"alteonVsAdd.interfaceNo" 	: data.alteonVsAdd.interfaceNo,
								"alteonVsAdd.ip" 			: data.alteonVsAdd.ip,
								"alteonVsAdd.name" 			: data.alteonVsAdd.name,
								"alteonVsAdd.vrrpState" 	: data.alteonVsAdd.vrrpState,
								"alteonVsAdd.routerId" 		: data.alteonVsAdd.routerId,
								"alteonVsAdd.vrId" 			: data.alteonVsAdd.vrId,
								"alteonVsAdd.virtualSvcs" 	: data.alteonVsAdd.virtualSvcs,
								"alteonVsAdd.virtualSvcsInString" : Object.toJSON(alteonVs._getVirtualSvcs()),
								"adc.index" : adcSetting.getAdc().index,
								"adc.name" : adcSetting.getAdc().name,
								"adc.type" : adcSetting.getAdc().type,
//								"f5VsAdd.membersInString" : Object.toJSON(f5Vs._getRegMembers()),
//								"f5VsAdd.adcIndex" : data.pairIndex,		
//								"f5VsAdd.index" : data.f5VsAdd.index,
//								"f5VsAdd.ip" : data.f5VsAdd.ip,
//								"f5VsAdd.name" : data.f5VsAdd.name,
//								"f5VsAdd.port" : data.f5VsAdd.port,
//								"f5VsAdd.poolName" : data.f5VsAdd.poolName,
//								"f5VsAdd.loadBalancingType" : data.f5VsAdd.loadBalancingType,
//								"f5VsAdd.healthCheckType" : data.f5VsAdd.healthCheckType,
//								"f5VsAdd.profileIndex" : data.f5VsAdd.profileIndex
							},
							successFn : function(data)
							{
								data.pairIndex = 0;
								if (data.pairIndex!='' && data.pairIndex!=null && data.pairIndex>0) 		// Alteon 동기화 부분 임시 false 처리 service에서 pairIndex 값 0 으로 return
								{
									var chk = confirm(data.message);
//									var chk = confirm("ADC Config 설정에 성공했습니다. \nADC 장비에 Peer 장비가 등록되어 있습니다. 동기화 하시겠습니까?");
									if (chk)
									{
										ajaxManager.runJsonExt({
											url : isModify ? 'virtualServer/modifyAlteonVsPeer.action' : 'virtualServer/addAlteonVsPeer.action',
											data : 
											{ 
												"pairIndex" : data.pairIndex,
												"alteonVsAdd.adcIndex" 		: data.alteonVsAdd.adcIndex,
												"alteonVsAdd.alteonId" 		: data.alteonVsAdd.alteonId,
												"alteonVsAdd.index" 		: data.alteonVsAdd.index,
												"alteonVsAdd.interfaceNo" 	: data.alteonVsAdd.interfaceNo,
												"alteonVsAdd.ip" 			: data.alteonVsAdd.ip,
												"alteonVsAdd.name" 			: data.alteonVsAdd.name,
												"alteonVsAdd.vrrpState" 	: data.alteonVsAdd.vrrpState,
												"alteonVsAdd.routerId" 		: data.alteonVsAdd.routerId,
												"alteonVsAdd.vrId" 			: data.alteonVsAdd.vrId,
												"alteonVsAdd.virtualSvcs" 	: data.alteonVsAdd.virtualSvcs,
												"alteonVsAdd.virtualSvcsInString" : Object.toJSON(alteonVs._getVirtualSvcs()),
//												"f5VsAdd.membersInString" : Object.toJSON(f5Vs._getRegMembers()),
//												"f5VsAdd.adcIndex" : data.pairIndex,		
//												"f5VsAdd.index" : data.f5VsAdd.index,
//												"f5VsAdd.ip" : data.f5VsAdd.ip,
//												"f5VsAdd.name" : data.f5VsAdd.name,
//												"f5VsAdd.port" : data.f5VsAdd.port,
//												"f5VsAdd.poolName" : data.f5VsAdd.poolName,
//												"f5VsAdd.loadBalancingType" : data.f5VsAdd.loadBalancingType,
//												"f5VsAdd.healthCheckType" : data.f5VsAdd.healthCheckType,
//												"f5VsAdd.profileIndex" : data.f5VsAdd.profileIndex
											},
											successFn : function(data) 
											{
												if (!data.isSuccessful)
												{
													$.obAlertNotice(data.message);
													loadListContent();
													return;
												}
												else
												{
													loadListContent();
													return false;
												}
											},
											errorFn : function(jqXhr)
											{
												$.obAlertAjaxError(VAR_COMMON_PVSAMFAIL, jqXhr);
												loadListContent();
											}
										});
										return false;
									}
								}
								loadListContent();
								return false;
							},
							errorFn : function(jqXhr)
							{
								$.obAlertAjaxError(VAR_COMMON_PEIFAIL, jqXhr);
							}	
						});
						
					},
					error : function(jqXhr)
					{
						// #3984-6 #9: 14.07.31 sw.jung 장비 설정 변경중 동기화 오류시 오류 메세지 개선
//						if (jqXhr.responseText.indexOf('Sync failed') > -1 || jqXhr.responseText.indexOf('Illegal null') > -1)
						if (jqXhr.responseText.indexOf('Sync failed') > -1)
						{
							var chk = confirm(VAR_NODE_SYNC_PROGRESSING);
							if(chk) 
							{
								ajaxManager.runJsonExt({
									url : "virtualServer/loadRefreshListContent.action",
									data :
									{
										"adc.index" : adcSetting.getAdc().index,
										"adc.name" : adcSetting.getAdc().name,
										"adc.type" : adcSetting.getAdc().type,	
										"refreshes" : true,
										"orderDir" : this.orderDir,
										"orderType" : this.orderType
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
												$.obAlertNotice(data.message);						
												return;							
											}	
										}	
										else
										{
											alert(VAR_NODE_SYNC_COMPLETE_RESET);
											loadAleonVsModifyContent(vsIndex);
										}
									},
									errorFn : function(jqXhr)
									{
										pageNavi.updateRowTotal(rowTotal, orderType, lastselectedPageNumber);
										$.obAlertAjaxError(VAR_VS_VSECEFAIL, jqXhr);
									}	
								});
							}
						}
						else if (jqXhr.responseText.indexOf('invalid interface num') > -1)
						{
							$.obAlertAjaxError(VAR_VS_INTERFACEAREA, jqXhr);
						}
						else
						{
							$.obAlertAjaxError(VAR_NODE_GROUP_SETFAIL, jqXhr);
						}
						
						$.obAlertAjaxError("SLB User 등록에 실패했습니다.", jqXhr);						
					}
				});	
				
				return false;
			});
		}
	},*/
	
	popUp : function(requesteIdx) 
	{
		with (this) 
		{
//			$('.slbUsrReqList').addClass('none');
//			$('.slbUsrRespList').addClass('none');
//			$('.slbList').addClass('none');
//			$('#slbList .vsList').addClass('none');

//			if (requesteIdx == 2)
//			{
//				showPopup({
//					"id" : "#slbUsrRespList",
//					"width" : "600px"
//				});
//				
//				$('.slbUsrRespList').removeClass('none');
//				$('#slbUsrReqList .reqList').addClass('none');
//				$('#slbUsrRespList .respList').removeClass('none');
//			}
//			else
//			{
//				showPopup({
//					"id" : "#slbUsrReqList",
//					"width" : "600px"
//				});
//				
//				$('.slbUsrReqList').removeClass('none');
//				$('#slbUsrReqList .reqList').removeClass('none');
//				$('#slbUsrRespList .respList').addClass('none');
//			}
			
			loadUserListContent(null, requesteIdx);
//			_loadVServerNames(requesteIdx);
//			_registerEvents();
		}
	},
	
	loadUserListContent : function(searchKey, requesteIdx)
	{
		with (this)
		{			
			OBajaxManager.runJsonExt({
				url : "slbSchedule/retrieveSlbUsrListTotal.action",
				data :
				{
					"slbUserType" : requesteIdx,
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
					
					if(rowTotal != 0)
					{
						pagePopNavi.updateRowTotal(rowTotal, orderType);
						loadSlbUserListContent(searchKey, requesteIdx, undefined, undefined);
					}
					else
					{
						if(requesteIdx == 1)
						{
							alert("등록된 요청자가 없습니다. 새로 등록해주세요.");
							return;
						}
						else
						{
							rowTotal = 1;
							pagePopNavi.updateRowTotal(rowTotal, orderType);
							loadSlbUserListContent(searchKey, requesteIdx, undefined, undefined);
						}
					}
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_VS_SCHEDULEUSER_CNTFAIL, jqXhr);
				}	
			});
		}
	},
	
	loadSlbUserListContent : function(searchKey, requesteIdx, fromRow, toRow) 
	{
		with (this) 
		{
			var targetId;
			if (requesteIdx == 1)
			{
				targetId = "#slbUsrReqList";
//				showPopup({
//					"id" : "#slbUsrReqList",
//					"width" : "494px"
//				});
//				
				$('.slbUsrReqList').removeClass('none');
				$('#slbUsrReqList .reqList').removeClass('none');
				$('#slbUsrRespList .respList').addClass('none');				
			}
			else
			{
				targetId = "#slbUsrRespList";
//				showPopup({
//					"id" : "#slbUsrRespList",
//					"width" : "494px"
//				});
//				
				$('.slbUsrRespList').removeClass('none');
				$('#slbUsrReqList .reqList').addClass('none');
				$('#slbUsrRespList .respList').removeClass('none');
			}
			
			ajaxManager.runHtmlExt({
				url : "slbSchedule/loadSlbUsrListContent.action",
				data : 
				{
					"slbUserType" : requesteIdx,
//					"fromRow" : fromRow === undefined ? pagePopNavi.getFirstRowOfCurrentPage() : fromRow,
//					"toRow" : toRow === undefined ? pagePopNavi.getLastRowOfCurrentPage() : toRow,
					"fromRow" : undefined,
					"toRow" : undefined,
					"searchKey" : searchKey,
					"orderDir" : this.orderDir,
					"orderType" : this.orderType
				},
				target: targetId + " .listWrap1",
				successFn : function(params) 
				{
					showPopup({
						"id" : targetId,
						"width" : "494px"
					});
				
					noticePopPageInfo(requesteIdx);
					_registerEvents(requesteIdx);
				},
				completeFn : function()
				{			
					pagePopNavi.refresh();
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_VS_SCHEDULEUSER_LOADFAIL, jqXhr);
				}	
			});
		}
	},
	
	loadSlbUserTableInListContent : function(searchKey, fromRow, toRow, orderType, orderDir)
	{
		with (this)
		{
			ajaxManager.runHtmlExt({
				url : "slbSchedule/loadSlbUsrTableInListContent.action",
				data : 
				{
					"slbUserType" : slbTypeIdx,
					"fromRow" : fromRow === undefined ? pagePopNavi.getFirstRowOfCurrentPage() : fromRow,
					"toRow" : toRow === undefined ? pagePopNavi.getLastRowOfCurrentPage() : toRow,
					"searchKey" : searchKey,
					"orderDir" : this.orderDir,
					"orderType" : this.orderType
				},
				target: "table.slbUserTable",
				successFn : function(params) 
				{
					noticePopPageInfo(slbTypeIdx);					
					_registerEvents(slbTypeIdx);
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_VS_SCHEDULEUSER_LOADFAIL, jqXhr);
				}	
			});
		}
	},
	
	_loadVServerNames : function(requesteIdx) 
	{
		with (this) 
		{			
			ajaxManager.runJsonExt({
				url : "slbSchedule/loadSlbUsrListJsonContent.action",
				data : 
				{
					"slbUserType" : requesteIdx 
				},
				successFn : function(data) 
				{
//					FlowitUtil.log('virtualServers: ' + Object.toJSON(data.virtualServers));
//					$('.cloneDiv .vServerNameTbd').html(_getVServerNameHtmlRows(data.virtualServers));
					
//					$('#slbUserList .pop_type_wrap').addClass('none');
//					$('#slbUserList2 .pop_type_wrap').addClass('none');
//					$('#slbList .pop_type_wrap').addClass('none');
					
					$('.popup_type1').addClass('none');
					$('.popup_type1:last').removeClass('none');
					
					if(requesteIdx == 0)
					{
						$('.cloneDiv .slbUsrTbd').html(_getSlbUsrHtmlRows(data.slbUserList));
//						var totalSlbUserCntHtml = $('.cloneDiv .slbUserListCnt').filter(':last');
//						totalSlbUserCntHtml.html("(" + data.slbUserList.length + ")");
						var totalSlbUserCntHtml = $('.cloneDiv .noticeReqPageCountInfo').filter(':last');
						totalSlbUserCntHtml.html(data.slbUserList.length);
						
//						$('#slbUserList .modify').addClass('none');
//						$('#slbUserList .add').addClass('none');
//						$('#slbUserList2 .list').addClass('none');
//						$('#slbUserList .list').removeClass('none');
					}
					else
					{
//						$('.cloneDiv .slbUsrRespTbd').html(_getSlbRespUsrHtmlRows(data.slbUserList));
						$('.cloneDiv .slbUsrTbd').html(_getSlbRespUsrHtmlRows(data.slbUserList));
//						var totalSlbUserCntHtml = $('.cloneDiv .noticeReqPageCountInfo').filter(':last');
//						totalSlbUserCntHtml.html("(" + data.slbUserList.length + ")");
						
						var totalSlbUserCntHtml = $('.cloneDiv .noticeRespPageCountInfo').filter(':last');
						totalSlbUserCntHtml.html(data.slbUserList.length);
												
//						$('#slbUserList2 .modify').addClass('none');
//						$('#slbUserList2 .add').addClass('none');
//						$('#slbUserList2 .list').removeClass('none');
					}
					_registerEvents();
//					_applyTableCss();
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_VS_SCHEDULEUSER_LOADFAIL, jqXhr);
//					exceptionEvent();
				}	
			});
		}
	},
	
	// 기존SLB 정보 Count
	loadSlbListCountContent : function(searchKey)
	{
		with (this)
		{			
			ajaxManager.runJsonExt({
				url : "slbSchedule/retrieveSlbListInfoTotal.action",
				data :
				{
					"adc.index" : adcSetting.getAdc().index,
					"adc.name" : adcSetting.getAdc().name,
					"adc.type" : adcSetting.getAdc().type,
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
					pagePopSlbNavi.updateRowTotal(rowTotal, orderType);
					loadSlbListInfoContent(searchKey, slbTypeIdx, undefined, undefined);			
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_VS_VSECEFAIL, jqXhr);
				}	
			});
		}
	},
	
	loadSlbListInfoContent : function(searchKey, fromRow, toRow, orderType, orderDir)
	{
		with (this)
		{
			$('#orgSlb').attr('checked', 'checked');
			$('#newSlb').removeAttr('checkekd');
			
			$('.slbUsrReqList').addClass('none');
			$('.slbUsrRespList').addClass('none');
			$('.slbList').removeClass('none');
			
			$('#slbUsrReqList .reqList').addClass('none');
			$('#slbUsrRespList .respList').addClass('none');
			$('#slbList .vsList').removeClass('none');
			
			$('.popup_type1').remove();
			$('.cloneDiv').remove();
			
			ajaxManager.runHtmlExt({
				url : "slbSchedule/loadSlbListInfoContent.action",
				data : 
				{
					"adc.index" : adcSetting.getAdc().index,
					"adc.name" : adcSetting.getAdc().name,
					"adc.type" : adcSetting.getAdc().type,
					"slbUserType" : slbTypeIdx,
					"fromRow" : fromRow === undefined ? pagePopSlbNavi.getFirstRowOfCurrentPage() : fromRow,
					"toRow" : toRow === undefined ? pagePopSlbNavi.getLastRowOfCurrentPage() : toRow,
					"searchKey" : searchKey,
					"orderDir" : this.orderDir,
					"orderType" : this.orderType
				},
				target: "table.slbListTable",
				successFn : function(params) 
				{
					showPopup({
						"id" : "#slbList",
						"width" : "600px"
					});
					
					noticePopSlbPageInfo(slbTypeIdx);
//					if(adcSetting.getAdc().type == "Alteon")
//						registerAlteonVsAddContentEvents(false);
//					else
//						registerF5VsAddContentEvents(false);
//					_registerVsAddContentEvents(false);
					_registerEvents(slbTypeIdx);
				},
				completeFn : function()
				{			
					pagePopSlbNavi.refresh();
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_VPVSER_LOADFAIL, jqXhr);
				}	
			});
		}
	},
	
	loadSlbListTableInListContent : function(searchKey, fromRow, toRow, orderType, orderDir)
	{
		with (this)
		{
			ajaxManager.runHtmlExt({
				url : "slbSchedule/loadSlbListTableInListContent.action",
				data : 
				{
					"slbUserType" : slbTypeIdx,
					"fromRow" : fromRow === undefined ? pagePopSlbNavi.getFirstRowOfCurrentPage() : fromRow,
					"toRow" : toRow === undefined ? pagePopSlbNavi.getLastRowOfCurrentPage() : toRow,
					"searchKey" : searchKey,
					"orderDir" : this.orderDir,
					"orderType" : this.orderType,
					"adc.index" : adcSetting.getAdc().index,
					"adc.name" : adcSetting.getAdc().name,
					"adc.type" : adcSetting.getAdc().type
				},
				target: "table.slbListTable",
				successFn : function(params) 
				{
					noticePopSlbPageInfo(slbTypeIdx);					
					_registerEvents(slbTypeIdx);
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_VPVSER_LOADFAIL, jqXhr);
				}	
			});
		}
	},
	
	_loadVServerListContent : function(slbTypeIdx)
	{
		with (this)
		{
			ajaxManager.runJsonExt({
				url : "slbSchedule/loadSlbListInfoContent.action",
				data : 
				{
					"adc.index" : adcSetting.getAdc().index,
					"adc.name" : adcSetting.getAdc().name,
					"adc.type" : adcSetting.getAdc().type,
					"searchKey" : ""
				},
				successFn : function(data)
				{
					$('.cloneDiv .slbListTbd').html(_getSlbListHtmlRows(data.virtualServers));
					_registerEvents(slbTypeIdx);
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_VS_VSECEFAIL, jqXhr);
				}
			});
		}
	},
	_getSlbUsrHtmlRows : function(slbUsrList) 
	{
		if(slbUsrList==null)
			return '';
		
		var html = '';
		for (var i=0; i < slbUsrList.length; i++) 
		{
			html += '<tr>';
			html += '<td class="slbUsrCheck align_center" isChk="0"><input class="slbUsrChk" name="slbUsrChk" type="checkbox" id="slbUsrChk" value="' + slbUsrList[i].index + '" /></td>';
			html += '<td class="slbUsrName align_left_P5">' + slbUsrList[i].name + '</td>';
			html += '<td class="slbUsrTeam align_left_P5">' + slbUsrList[i].team + '</td>';
			html += '<td class="slbUsrPhone align_center">' + slbUsrList[i].phone + '</td>';
			html += '<td class="align_center"><input type="button" class="modifySlbUser Btn_black_small" value="수정"></td>';			
			html += '</tr>';
		}		
		FlowitUtil.log('html: ' + html);
		
		return html;
	},
	_getSlbRespUsrHtmlRows : function(slbUsrList) 
	{
		if(slbUsrList==null)
			return '';
		
		var html = '';
		for (var i=0; i < slbUsrList.length; i++) 
		{
			html += '<tr>';			
			html += '<td class="slbUsrCheck align_center" isChk="0">';
			html += '<input class="slbUsrChk" name="slbUsrChk" type="checkbox" id="slbUsrChk" value="' + slbUsrList[i].index + '" />';
			html += '<input class="respUsrType" name="slbUsrType" type="hidden" id="slbUsrType" value="' + slbUsrList[i].type + '" />';
			html += '</td>';
			html += '<td class="slbUsrName align_left_P5">' + slbUsrList[i].name + '</td>';
			html += '<td class="slbUsrTeam align_left_P5">' + slbUsrList[i].team + '</td>';
			html += '<td class="slbUsrPhone align_center">' + slbUsrList[i].phone + '</td>';
			html += '<td class="align_center"><input type="button" class="modifySlbRespUser Btn_black_small" value="수정"></td>';			
			html += '</tr>';
		}
//		FlowitUtil.log('html: ' + html);
//		html += '<tr><td class="slbUsrCheck align_center" isChk="0"><input class="respUsrChk" name="slbUsrChk" type="checkbox" id="slbUsrChk" value=""/></td><td>1</td><td>2</td><td>3</td><td>4</td></tr>';
		return html;
	},
	
	_getSlbListHtmlRows : function(slbList) 
	{
		if(slbList==null)
			return '';
		
		var html = '';
		for (var i=0; i < slbList.length; i++) 
		{
			var status = "";
			html += '<tr>';			
			html += '<td class="slbUsrCheck align_center" isChk="0"><input class="slbListChk" name="slbListChk" type="checkbox" id="slbListChk" value="' + slbList[i].index + '" /></td>';
			
			
			if ((slbList[i].status) == 'block')
				status = '<img class="imgOn status_imgon" src="imgs/icon/icon_2d_3.png" />';
			else if ((slbList[i].status) == 'disable')
				status = '<img class="imgOn status_imgon" src="imgs/icon/icon_vs_disabled.png" />';
			else if ((slbList[i].status) == 'available')
				status = '<img class="imgOn status_imgon" src="imgs/icon/icon_vs_conn.png" />';
			else
				status = '<img class="imgOn status_imgon" src="imgs/icon/icon_vs_disconn.png" />';
		
		
			html += '<td class="slbListStatus align_center">' + status + '</td>';
			html += '<td class="slbListVsIp align_center"><a id="mar_lft5" class="modifyVirtualServerLnk" href="#">' + slbList[i].virtualIp + '</a></td>';
			html += '<td class="slbListPort align_left_P5">' + slbList[i].port + '</td>';
			html += '<td class="slbListName align_left_P5">' + slbList[i].name + '</td>';
			html += '</tr>';
		}
		
		return html;
	},
	
	validateSearchKey : function()
	{
		if ($('.cloneDiv .searchTxt').val() != "" && $('.cloneDiv .searchTxt').val() != undefined)
		{
			if (!getValidateStringint($('.cloneDiv .searchTxt').val(), 1, 1024))
			{
				$.obAlertNotice(VAR_COMMON_SPECIALCHAR);
				return false;
			}
		}
		
		return true;
	},
	
	_registerSlbEvents : function(slbTypeIdx)
	{
		$('.reqOnOk').off('click');
		$('.reqOnOk').click(function(e)
		{
			e.preventDefault();		
			var slbUsrIdx = $('.cloneDiv input[name="slbUser.index"]').val();
			if(slbUsrIdx != null)
				isModify = true;
			else
				isModify = false;
			$('#requestSlbFrm').submit();					
		});			
		
		$('#requestSlbFrm').off('submit');
		$('#requestSlbFrm').submit(function()
		{
			if(!slbSchedule._validateSlbUserAdd())
				return false;
			
			var params = 
			{
//				"slbUser.index" : $('.cloneDiv input[name="slbUser.index"]').val(),
//				"slbUser.type" : $('.cloneDiv input[name="slbUser.type"]').val()
//				"slbUser.name" : $('.cloneDiv .userName').val(),
//				"slbUser.team" : $('.cloneDiv .userTeam').val(),
//				"slbUser.phone" : $('.cloneDiv .userPhone').val()
			};
			$(this).ajaxSubmit({
				dataType : 'json',
				url : isModify ? 'slbSchedule/modifySlbUser.action' : 'slbSchedule/addSlbUser.action',
				data : params,
				success : function(data)
				{
					if (!data.isSuccessful)
					{
						$.obAlertNotice(data.message);
						return;
					}
					$('#slbUsrReqList .reqModify').addClass('none');
					$('#slbUsrReqList .reqAdd').addClass('none');
					$('#slbUsrReqList .reqList').removeClass('none');
					
					$('#slbUsrRespList .respAdd').addClass('none');
					$('#slbUsrRespList .respList').removeClass('none');
//					slbSchedule.popUp(requesteIdx);
//					popUp._loadVServerNames(requesteIdx);
					slbSchedule._loadVServerNames(slbTypeIdx);
				},
				error : function(jqXhr)
				{
					if(isModify)
						$.obAlertAjaxError(VAR_VS_SCHEDULEUSER_MODIFYFAIL, jqXhr);
					else
						$.obAlertAjaxError(VAR_VS_SCHEDULEUSER_ADDFAIL, jqXhr);
				}
			});	
			
			return false;
		});
		
		$('.reqOnCancel').off('click');
		$('.reqOnCancel').click(function(e)
		{
			$('#slbUsrReqList .reqModify').addClass('none');
			$('#slbUsrReqList .reqAdd').addClass('none');
			$('#slbUsrReqList .reqList').removeClass('none');
			
			
//			$('#slbUserList2 .modify').addClass('none');
			$('#slbUsrRespList .respAdd').addClass('none');
			$('#slbUsrRespList .respList').removeClass('none');
		});
		
		$('.respOnOk').off('click');
		$('.respOnOk').click(function(e)
		{
//			var slbUsrIdx = $('input[name="slbUser.index"]').val();
			var slbUsrIdx = $('.cloneDiv input[name="slbUser.index"]').val();
			if(slbUsrIdx == 0)
				isModify = false;
			else
				isModify = true;
			$('#respSlbUserAddFrm').submit();					
		});			
		
		$('#respSlbUserAddFrm').off('submit');
		$('#respSlbUserAddFrm').submit(function()
		{
			if(!slbSchedule._validateSlbUserAdd())
				return false;
			
			var params="";
			
			params = 
			{
//				"slbUser.index" : $('input[name="slbUser.index"]').val(),
//				"slbUser.type" : $('.cloneDiv input[name="slbUser.type"]').val()
				"slbUserIndex" : $('.cloneDiv input[name="slbUser.index"]').val(),
				"slbUserType" : $('.cloneDiv input[name="slbUser.type"]').val(),
				"slbUserName" : $('.cloneDiv input[name="slbUser.name"]').val(),
				"slbUserTeam" : $('.cloneDiv input[name="slbUser.team"]').val(),
				"slbUserPhone" : $('.cloneDiv input[name="slbUser.phone"]').val()				
			};
			
			/*if(isModify)
			{
				params = 
				{
	//				"slbUser.index" : $('input[name="slbUser.index"]').val(),
	//				"slbUser.type" : $('.cloneDiv input[name="slbUser.type"]').val()
					"slbUserIndex" : $('.cloneDiv input[name="slbUser.index"]').val(),
					"slbUserType" : $('.cloneDiv input[name="slbUser.type"]').val()			
				};
			}
			else
			{
				params = 
				{
	//				"slbUser.index" : $('input[name="slbUser.index"]').val(),
	//				"slbUser.type" : $('.cloneDiv input[name="slbUser.type"]').val()
					"slbUserIndex" : $('.cloneDiv input[name="slbUser.index"]').val(),
					"slbUserType" : $('.cloneDiv input[name="slbUser.type"]').val(),
					"slbUserName" : $('.cloneDiv input[name="slbUser.name"]').val(),
					"slbUserTeam" : $('.cloneDiv input[name="slbUser.team"]').val(),
					"slbUserPhone" : $('.cloneDiv input[name="slbUser.phone"]').val()				
				};
			}*/
			$(this).ajaxSubmit({
				dataType : 'json',
				url : isModify ? 'slbSchedule/modifySlbUser.action' : 'slbSchedule/addSlbUser.action',
				data : params,
				success : function(data)
				{
					if (!data.isSuccessful)
					{
						$.obAlertNotice(data.message);
						return;
					}
					else
					{
						$('#slbUsrReqList .reqModify').addClass('none');
						$('#slbUsrReqList .reqAdd').addClass('none');
						$('#slbUsrReqList .reqList').removeClass('none');
						
//						$('#slbUserList2 .modify').addClass('none');
						$('#slbUsrRespList .respAdd').addClass('none');
						$('#slbUsrRespList .respList').removeClass('none');
						slbSchedule._loadVServerNames(2);
//						slbSchedule.loadSlbUserListContent(null, 2);
					}					
				},
				error : function(jqXhr)
				{
					if(isModify)
						$.obAlertAjaxError(VAR_VS_SCHEDULEUSER_MODIFYFAIL, jqXhr);
					else
						$.obAlertAjaxError(VAR_VS_SCHEDULEUSER_ADDFAIL, jqXhr);
				}
			});	
			
			return false;
		});
		
		$('.respOnCancel').off('click');
		$('.respOnCancel').click(function(e)
		{
			$('#slbUsrReqList .reqModify').addClass('none');
			$('#slbUsrReqList .reqAdd').addClass('none');
			$('#slbUsrReqList .reqList').removeClass('none');
			
			
//			$('#slbUserList2 .modify').addClass('none');
			$('#slbUsrRespList .respAdd').addClass('none');
			$('#slbUsrRespList .respList').removeClass('none');
		});
	},
	
	_registerEvents : function(requesteIdx) 
	{
		with (this) 
		{
			$('.cloneDiv .closeLnk, .cloneDiv .closeWndLnk').off('click');
			$('.cloneDiv .closeLnk, .cloneDiv .closeWndLnk').click(function(e) 
			{
				e.preventDefault();
				
				$('.popup_type1').remove();
				$('.cloneDiv').remove();
			});
			
			// search event
			$('p.sch a.searchLnk').off('click');
			$('p.sch a.searchLnk').click(function (e) 
			{
				e.preventDefault();
				var searchKey = $('.cloneDiv .searchTxt').val();
				FlowitUtil.log('click:' + searchKey);				
				_loadVServerNames(searchKey);
			});
			
			$('.selectedLnk').off('click');
			$('.selectedLnk').click(function(e)
			{   
				e.preventDefault();
				
				var chkRequest = _getCheckedSlbUsers();
				
				if(chkRequest.length == 0)
				{
					$.obAlertNotice(VAR_SCHEDULE_REQ_SELECT);
					return;
				} 
				else if(chkRequest.length > 1)
				{
					$.obAlertNotice("선택요청자는 하나만 선택가능합니다.");
					return false;
				}
				else 
				{	
					$('.cloneDiv .slbUsrTbd tr').find('.slbUsrCheck').each(function(index) {
//						console.log(index)
//						console.log($(this).attr('isdetail'))
					 // console.log($('.reqestorChk').parent().attr('isdetail'))
					 // console.log($(this).parent().attr('isdetail'))
					//  console.log($('input[name="slbUsrChk"]').is(':checked'))
						if($(this).attr('isChk') == 1)
						{
//							console.log("aaaaaaaaaaaaaaaaaaaaa")
//							console.log($(this).parent().find('td:eq(1)').text())
//							console.log($(this).find('td:eq(1)').val())
							$('.slbUsrTable #slbUsrIndex').val($(this).parent().find('td .slbUsrChk').val());
							$('.slbUsrTable #slbUsrName').val($(this).parent().find('td:eq(1)').text());
							$('.slbUsrTable #slbUsrTeam').val($(this).parent().find('td:eq(2)').text());
							$('.slbUsrTable #slbUsrPhone').val($(this).parent().find('td:eq(3)').text());
					   }
					});
						
//					if($('input[name="slbUsrChk"]').is(':checked') == true)
//					{
//						$('.slbUsrTable #slbUsrName').val($(this).parent().parent().find('td:eq(1)').text());
//						$('.slbUsrTable #slbUsrTeam').val($(this).parent().parent().find('td:eq(2)').text());
//						$('.slbUsrTable #slbUsrPhone').val($(this).parent().parent().find('td:eq(3)').text());
//					}
					
//					$('#slbUserList .reqList').removeClass('none');
//					$('#slbUserList2 .respList').removeClass('none');
//					$('#slbList .vsList').removeClass('none');
					$('.popup_type1').remove();
					$('.cloneDiv').remove();
					$('.selectR').attr('checked', false);
					$('#selectList').attr('checked', true);
				}				
			});
			
			$('.slbUsrAllChk').off('click');
			$('.slbUsrAllChk').click(function(e)
			{				
//				e.preventDefault();
				var isChecked = $(this).is(':checked');
//				$('#selelctedReq .requestorChk').attr('checked', isChecked);
//				$('.cloneDiv .requestorChk').attr('checked', isChecked);
				$('.cloneDiv #selectedUsr .slbUsrChk').attr('checked', isChecked);
			});
			
			$('.slbUsrChk').off('click');
			$('.slbUsrChk').click(function(e)
			{
				var isChecked = $(this).is(':checked');
//				var isChk = $(this).parent().attr("isChk");				
				
				if(isChecked)
				{
					$(this).attr('checked', isChecked);
					$(this).parent().attr("isChk", "1");
				}
				else
				{
					$(this).removeAttr('checked');
					$(this).parent().attr("isChk", "0");
				}				
			});		
			
//			$('.allRespUsrChk').click(function(e)
//			{				
////						e.preventDefault();
//				var isChecked = $(this).is(':checked');
////						$('#selelctedReq .requestorChk').attr('checked', isChecked);
////						$('.cloneDiv .requestorChk').attr('checked', isChecked);
//				$('.cloneDiv #selectedResp .respUsrChk').attr('checked', isChecked);
//			});
			
			$('.addRespUsr').off('click');
			$('.addRespUsr').click(function(e)
			{
				$('#slbUsrRespList .respAdd').removeClass('none');
//				$('#slbUserList2 .modify').addClass('none');
				$('#slbUsrRespList .respList').addClass('none');
				
//				_registerSlbEvents();
				loadRespUserAddContent();
				
			});
					
			$('.modifySlbUser').off('click');
			$('.modifySlbUser').click(function(e)
			{
				
				
//				$('input[name="slbUser.index"]').val($(this).parent().parent().find('td:eq(0)').children().val());
//				$('input[name="slbUser.name"]').val($(this).parent().parent().find('td:eq(1)').text());
//				$('input[name="slbUser.team"]').val($(this).parent().parent().find('td:eq(2)').text());
//				$('input[name="slbUser.phone"]').val($(this).parent().parent().find('td:eq(3)').text());
//				$('input[name="slbUser.type"]').val(0);		
				
				var slbUsrIndex = $(this).parent().parent().find('.slbUsrChk').val();
				
				if(slbTypeIdx == 0)
				{
					$('#slbUsrRespList .respAdd').removeClass('none');
					$('#slbUsrRespList .respList').addClass('none');
					loadRespUserModifyContent(slbUsrIndex);
				}
				else
				{
					$('#slbUsrReqList .reqModify').removeClass('none');
					$('#slbUsrReqList .reqList').addClass('none');
					loadModifySlbUsers(slbUsrIndex);
				}
			});
			
			$('.modifySlbRespUser').off('click');
			$('.modifySlbRespUser').click(function(e)
			{
//				var slbUsrIndex = $(this).parent().find('.slbUsrChk').val();
//				loadRespUserModifyContent(slbUsrIndex);
				
				
//				$('#slbUserList2 .modify').removeClass('none');
//				$('#slbUserList2 .add').addClass('none');
				$('#slbUsrRespList .respAdd').removeClass('none');
				$('#slbUsrRespList .respList').addClass('none');
				
//				$('input[name="respName"]').val($(this).parent().parent().find('td:eq(1)').text());
//				$('input[name="respTeam"]').val($(this).parent().parent().find('td:eq(2)').text());
//				$('input[name="respPhone"]').val($(this).parent().parent().find('td:eq(3)').text());
				
//				$('input[name="slbUser.index"]').val($(this).parent().parent().find('td:eq(0)').children().val());
//				$('input[name="slbUser.name"]').val($(this).parent().parent().find('td:eq(1)').text());
//				$('input[name="slbUser.team"]').val($(this).parent().parent().find('td:eq(2)').text());
//				$('input[name="slbUser.phone"]').val($(this).parent().parent().find('td:eq(3)').text());
//				$('input[name="slbUser.type"]').val(1);	
				
				var slbUsrIndex = $(this).parent().parent().find('.respUsrChk').val().trim();
				loadRespUserModifyContent(slbUsrIndex);
				
			});
			
//			$('.selRespLnks').off('click');
//			$('.selRespLnks').click(function(e)
//			{
//				$('.cloneDiv .slbUsrTbd tr').map(function(row) 
//				{
//					if ($(this).find('.slbUsrChk:checked').length)
//						return $(this).find('td:eq(1)').text();
//				}).join(',');
//			});
				
			$('.selRespLnk').off('click');
			$('.selRespLnk').click(function(e)
			{
				var personArray = new Array();
				var smReceiveList = new Array();
				var userNameList;
				$('.cloneDiv .slbUsrTbd').each(function(index) {
					userNameList = [];
					$(this).parent().parent().parent().find('.slbUsrChk:checked').each(function(index)
					{
						var userNm = $(this).parent().parent().find('.slbUsrName').text();
					    var userHp = $(this).parent().parent().find('.slbUsrPhone').text();				
						var personInfo = new Object();
						
					    personInfo.name = userNm;
					    personInfo.hp = userHp;
					    
					    personArray.push(personInfo);
					    
					    
						userNameList.push(userNm);
					});					
					
					smReceiveList = userNameList;
				});
				slbSchedule.orgSmReceiveList = smReceiveList;
				slbReceiverLists = personArray;
//				var myJsonString = JSON.stringify(slbReceiverLists);
				slbSchedule.slbReceiverLists = slbReceiverLists;
				$('#respDesc').val(smReceiveList);
//				$('.scheduleSmReceive').val(myJsonString);
				$('.scheduleSmReceiveUsers').text(smReceiveList);
				$('.popup_type1').remove();
				$('.cloneDiv').remove();
			});
			/*
			$('.selRespLnk2233222').click(function(e)
			{
				var receiveUserList = new Array();
				var sm_receiveList = new Array();
				var smReceiveList = new Array();
				
				var filterData = {};
				var param;
				var userNameList;
				$('.cloneDiv .slbUsrTbd').each(function(index) {
					param = [];
					userNameList = [];
					$(this).parent().parent().parent().find('.slbUsrChk:checked').each(function(index)
					{
						var userNm = $(this).parent().parent().find('.slbUsrName').text();
					    var userHp = $(this).parent().parent().find('.slbUsrPhone').text();				
				   

				    	dataObjectIF = '"' + userNm  + '" : ' + userHp;
//				    	if(index != size)
//				    	{
//				    		dataObjectIF += ",";
//				    	}

					    
						param.push(dataObjectIF);	
						
						
						userNameList.push(userNm);
					});
					
					
//					filterData["receivers"] = param;
					
					// 에이스|01011112222,스마트|33334444,샤오미|01055556666
					smReceiveList = userNameList;
				});
				
				
//				$('.cloneDiv .slbUsrTbd tr').find('.slbUsrChk:checked').each(function(index) {
//					param = [];
//					var userNm = $(this).parent().parent().find('.slbUsrName').text();
//					var userHp = $(this).parent().parent().find('.slbUsrPhone').text();
//					
//					
//					param.push([index, userNm, userHp].join('|'));
//					
////					$(this).find('input').each(function(index)
////					{
////						index =$(this).parent().find('span').text();				
////						
////						param.push([index, $(this).parent().find('label').text(), $(this).val(), $(this).is(':checked')].join('|'));
////					});					  		
//					filterData[$(this).data('group')] = param; // 셀렉트박스의 data-group 속성을 읽어 해당 키에 배치
//					
////					adcMonitor.setFlag = $('#filzterAdd').attr("isFlag");
//				});
				
				
				
				
				// "에이스 : 01011112222", "스마트 : 33334444"
				
				// 요청자 정보
//						var requestUser = $('#slbUsrName').val().trim()+ ',' + $('#slbUsrPhone').val().trim() ;
//						var requestUserName = $('#slbUsrName').val().trim();
								
//				$('.cloneDiv .slbUsrTbd tr').find('.slbUsrChk:checked').each(function(index) {
//					 var userNm = $(this).parent().parent().find('.slbUsrName').text();
////		                     var userNmHp = $(this).parent().parent().find('.slbUsrName').text() + ',' + $(this).parent().parent().find('.slbUsrPhone').text();
//					 var userHp = $(this).parent().parent().find('.slbUsrPhone').text();
//					 
//					 console.log("userNm: ", userNm);
//					 console.log("userHp: ", userHp);
//					 
//					 smReceiveList.push(userNm);
//					 
//					 receiveUserList = userNm + " : " + userHp;
//					 sm_receiveList.push(receiveUserList);                    
////		                     receiveUserList.push(userInfoJson);
////		                     console.log("userInfoJson: ", userInfoJson);
//                     console.log("receiveUserList: ", receiveUserList);
//                     console.log("sm_receiveList: ", sm_receiveList);
////		                	 sm_receiveList.push(userNmHp);
//                     
//                     var myJsonString = JSON.stringify(sm_receiveList);
//                     console.log("myJsonString: ", myJsonString);
//				});
				
//				["에이스 : 01011112222","스마트 : 33334444","샤오미 : 01055556666"]
				
				$('#respDesc').val(smReceiveList);
				$('.scheduleSmReceive').val(param);
				$('.scheduleSmReceiveUsers').text(smReceiveList);
				$('.popup_type1').remove();
				$('.cloneDiv').remove();
				
			});
			*/
			/*$('.selRespLnk1').click(function(e)
			{
				var receiveUserList = new Array();
				var sm_receiveList = new Array();
				var smReceiveList = new Array();
				
				// 요청자 정보
//				var requestUser = $('#slbUsrName').val().trim()+ ',' + $('#slbUsrPhone').val().trim() ;
//				var requestUserName = $('#slbUsrName').val().trim();
								
				$('.cloneDiv .slbUsrTbd tr').find('.slbUsrChk:checked').each(function(index) {
//					 console.log(index);
					 
//					 console.log('nmae, phone', $(this).parent().parent().find('.slbUsrName').text(), $(this).parent().parent().find('.slbUsrPhone').text());
					 var userNm = $(this).parent().parent().find('.slbUsrName').text();
                     var userNmHp = $(this).parent().parent().find('.slbUsrName').text() + ',' + $(this).parent().parent().find('.slbUsrPhone').text();
//                     console.log('userNm : ', userNm);
//                     console.log('userNmHp : ', userNmHp);
                     
                     receiveUserList.push(userNm);
                	 sm_receiveList.push(userNmHp);
//                     if (index == 0)
//                     {
//                    	 receiveUserList.push(userNm);
//                    	 sm_receiveList.push(userNmHp);
//                     }
//                     else
//                     {
//                    	 receiveUserList.push(userNm + ',');
//                    	 sm_receiveList.push(userNmHp);
//                     }
//                     console.log('sm_receiveList', sm_receiveList);
					//smReceiveList.push($(this).parent().parent().find('.slbUsrName').text() + ',' + $(this).parent().parent().find('.slbUsrPhone').text() + '|');
				});
			
				for (var j=0; j < sm_receiveList.length; j++)
				{
					if(j != sm_receiveList.length -1)
						smReceiveList += sm_receiveList[j] + '|';
					else
						smReceiveList += sm_receiveList[j] ;
				}	
//				smReceiveList =	sm_receiveList[0]
//				eceiveUserList ["에이유,01000001111|", "에이스,01011112222|"]
							
				
				$('#respDesc').val(receiveUserList);
				
//				console.log("aaaaaaaaaaaaaaaaaaaaaaaaaa:", $('#respDesc:contains(requestUserName)'));
				
				$('.scheduleSmReceive').val(smReceiveList);
				
//				$('.scheduleSmReceive').text(smReceiveList);
//				$('.scheduleSmReceive').val(smReceiveList);
				$('.scheduleSmReceiveUsers').text(receiveUserList);
				
				
				
				$('.cloneDiv .slbUsrTbd tr').find('.slbUsrChk:checked').each(function(index) {
//					 console.log(index);
//					 console.log('Name:' + $(this).parent().parent().find('.slbUsrName').text()  + '(Hp:' + $(this).parent().parent().find('.slbUsrPhone').text() + ')');
//					 vsIndexList.push('Name:' + $(this).parent().parent().find('.slbUsrName').text()  + '(Hp:' + $(this).parent().parent().find('.slbUsrPhone').text() + ')');
					receiveUserList.push($(this).parent().parent().find('.slbUsrName').text());					
					smReceiveList.push($(this).parent().parent().find('.slbUsrName').text() + ',' + $(this).parent().parent().find('.slbUsrPhone').text() + '|');
				});
				if (receiveUserList != null)
					sm_receiveList = requestUser + ', ' + receiveUserList;
				else
					sm_receiveList = requestUser;
				
				//에이유,01000001111|,에이스,01011112222|
				
				$('#respDesc').val(sm_receiveList);
				$('.scheduleSmReceive').val(smReceiveList);
				
				
//				$('#slbUserList .reqList').removeClass('none');
//				$('#slbUserList2 .respList').removeClass('none');
//				$('#slbList .vsList').removeClass('none');
				$('.popup_type1').remove();
				$('.cloneDiv').remove();
				
			});
			*/
			$('.modifyVirtualServerLnk').off('click');
			$('.modifyVirtualServerLnk').click(function(e)
			{	
				e.preventDefault();
				with (this)
				{
					vsIndex = $(this).parent().parent().find('.slbUsrIndex').val();
//					vsIndex = $(this).parent().parent().parent().find('.slbListChk:checked').val();
					if (adcSetting.getAdc().type == 'F5')
					{
	//					f5Vs.loadVsModifyContent(vsIndex);
						with (this)
						{						
							newPoolName = '';
							ajaxManager.runHtmlExt({
								url : "slbSchedule/loadF5VsModifyContent.action",
								data :
								{
									"f5VsAdd.index" : vsIndex, //$(this).parent().parent().find('.slbListChk').val(),
									"f5VsAdd.adcIndex" : adcSetting.getAdc().index,
									"adc.index" : adcSetting.getAdc().index,
									"adc.type" : adcSetting.getAdc().type,
									"adc.name" : adcSetting.getAdc().name
								},
								target: "#wrap .slbF5Info",
								successFn : function(params)
								{
									virtualServer.setActiveContent('VsModifyContent');
	//								vsIndex = index;
	//								_registerVsAddContentEvents(true);
									registerF5VsAddContentEvents(isModify);
	//								_registerListContentEvents();
									f5Vs._applyMemberTableCss();
								},
								errorFn : function(jqXhr)
								{
									$.obAlertAjaxError(VAR_VS_SCHEDULEUSER_LOADFAIL, jqXhr);
	//								exceptionEvent();
								}	
							});						
						}
					}
					else if (adcSetting.getAdc().type == 'Alteon')
					{
	//					alteonVs.loadVsModifyContent($(this).parent().parent().find('.slbListChk').val());
						with (this)
						{						
							newPoolName = '';
							ajaxManager.runHtmlExt({
								url : "slbSchedule/loadAlteonVsModifyContent.action",
								data :
								{
									"alteonVsAdd.index" : vsIndex, //$(this).parent().parent().find('.slbListChk').val(),
									"alteonVsAdd.adcIndex" : adcSetting.getAdc().index,
									"adc.index" : adcSetting.getAdc().index,
									"adc.type" : adcSetting.getAdc().type,
									"adc.name" : adcSetting.getAdc().name
								},
								target: "#wrap .slbAlteonInfo",
								successFn : function(params)
								{
									virtualServer.setActiveContent('VsModifyContent');
	//								vsIndex = index;
	//								_registerVsAddContentEvents(true);
									registerAlteonVsAddContentEvents(true);
	//								_registerListContentEvents();
								},
								errorFn : function(jqXhr)
								{
									$.obAlertAjaxError(VAR_VS_SCHEDULEUSER_LOADFAIL, jqXhr);
	//								exceptionEvent();
								}	
							});						
						}
					}
//					else if (adcSetting.getAdc().type == 'PAS')
//					{
//						pasVs.loadVsModifyContent($(this).parent().parent().find('.slbListChk').val());
//					}
//					else if (adcSetting.getAdc().type == 'PASK')
//					{
//						paskVs.loadVsModifyContent($(this).parent().parent().find('.slbListChk').val());
//					}
//					else if (adcSetting.getAdc().type == 'PiolinkUnknown')
//					{
//						pasVs.loadVsModifyContent($(this).parent().parent().find('.slbListChk').val());
//					}
					
	//				$('.slbUserList').addClass('none');
	//				$('.slbUserList2').addClass('none');
	//				$('.slbList').addClass('none');
	//				
	//				$('.popup_type1').addClass('none');
	//				$('.popup_type1:last').removeClass('none');
					
					$('.slbUsrReqList').addClass('none');
					$('.slbUsrRespList').addClass('none');
					$('.slbList').addClass('none');
					$('.popup_type1').remove();
					$('.cloneDiv').remove();
				}
			});	
				
			
//			$('.onCancel').click(function(e)
//			{
//				$('#slbUserList .modify').addClass('none');
//				$('#slbUserList .add').addClass('none');
//				$('#slbUserList .list').removeClass('none');
//				
//				
//				$('#slbUserList2 .modify').addClass('none');
//				$('#slbUserList2 .add').addClass('none');
//				$('#slbUserList2 .list').removeClass('none');
//			});
			
			$('.delSlbUsersLnk').off('click');
			$('.delSlbUsersLnk').click(function(e)
			{
				e.preventDefault();
				
				with (this)
				{									
//					var chkDel = $(this).parent().parent().find('.requestorChk').filter(':checked').map(function() {
//						return $(this).val();
//					}).get();
				
					var chkDel = _getCheckedSlbUsers();
					
					if(chkDel.length == 0)
					{
						$.obAlertNotice(VAR_SCHEDULE_REQ_DSELECT);
						return;
					}
					
					var chk = confirm(VAR_SCHEDULE_REQ_DEL);
					if(chk)
					{
						delSlbUsers(chkDel);
					}
					else
					{
						return false;
					}
				}
			});
			
			if(requesteIdx == 1)
			{
				$('.cloneDiv .slbUsrTbd tr').find('.slbUsrName').each(function(index)
				{
					if ($(this).text().trim() == $('input[name="schedule.name"]').val().trim())
				    {
//				        console.log("check Index: " , $(this).parent().find('.slbUsrCheck'));
				        $(this).parent().find('.slbUsrChk').attr('checked', 'checked');
				    }
				});
			}
			else
			{
				$('.cloneDiv .slbUsrTbd tr').find('.slbUsrName').each(function(index)
				{
//							console.log("table td value : " , $(this).text());
//							console.log("request input value : " , $('#slbUsrName').text().trim());
//							console.log("input value : " , $('input[name="schedule.name"]').val().trim());
				    					
					if ($(this).text().trim() == $('input[name="schedule.name"]').val().trim())
				    {
//						        console.log("check Index: " , $(this).parent().find('.slbUsrCheck'));
				        $(this).parent().find('.slbUsrChk').attr('checked', 'checked');
				    }
					
//							var smResp = $('.scheduleSmReceive').val().trim();
					// tester,77778888|테스터,12345678
					var descField = new Array();
					descField = $('.scheduleSmReceiveUsers').text().trim().split(',');
					
					for ( var i = 0; i < descField.length; i++) 
					{						
						var name = descField[i].split(',')[0];
						
						if ($(this).text().trim() == name)
					    {
					        $(this).parent().find('.slbUsrChk').attr('checked', 'checked');
					    }
					}
					
					
//							descField = $('.scheduleSmReceive').val().trim().split('|');
					
					
//							var smRespUsers = $.map(descField.split('|'), function(item)
//							{
//								return item.split(',')[0];
//							}).join(','); // '샤오미,123213|테스트,123213' -> '샤오미,테스트'
//							
//							var smResp = new Array();
//							smResp.push(smRespUsers.split(','));
//							
//							console.log("", smResp);
										
//							for ( var i = 0; i < descField.length; i++) {
//							
//								var name = descField[i].split(',')[0];
//								var hp = descField[i].split(',')[1];
//								
//								if ($(this).text().trim() == name && $(this).parent().find('.slbUsrPhone').text() == hp)
//							    {
//							        $(this).parent().find('.slbUsrChk').attr('checked', 'checked');
//							    }
//							}
//							if ($('.scheduleSmReceive').val().trim())
//						    {				        
//						        $(this).parent().find('.slbUsrChk').attr('checked', 'checked');
//						    }					
				});
			}
		}
	},
	loadLastResponseUserInfo : function()
	{
		with (this)
		{
			ajaxManager.runJson({
				url : "slbSchedule/loadLastResponseUserInfo.action",
				data : 
				{
					
				},
				successFn : function(data)
				{
					$('.slbUsrTable #slbUsrName').val(data.slbUser.name);
					$('.slbUsrTable #slbUsrTeam').val(data.slbUser.team);
					$('.slbUsrTable #slbUsrPhone').val(data.slbUser.phone);
				}
			});
		}
	},
	
	loadRespUserAddContent : function() 
	{
		with (this) 
		{			
			ajaxManager.runHtmlExt({
				url : "slbSchedule/loadSlbRespUserAddContent.action",
//				target: "#wrap .contents",
				target: "#slbUsrRespList .respAdd",
				successFn : function(params) 
				{
					_registerSlbEvents(slbTypeIdx);	
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_VS_SCHEDULERESP_LOADFAIL, jqXhr);
				}
			});
		}
	},
	
	loadRespUserModifyContent : function(slbUsrIndex)
	{
		with (this)
		{
			ajaxManager.runHtmlExt({
				url : "slbSchedule/loadSlbRespUserModifyContent.action",
				data :
				{
					"slbUserIndex" : slbUsrIndex
				},				
				target: "#slbUsrRespList .respAdd",				
				successFn : function(params) 
				{					
					_registerSlbEvents(slbTypeIdx);					
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_VS_SCHEDULERESP_LOADFAIL, jqXhr);
				}
			});
		}
	},
	
	loadModifySlbUsers : function(slbUsrIndex)
	{
		with (this)
		{
			ajaxManager.runHtmlExt({
				url : "slbSchedule/loadSlbUserModifyContent.action",
				data :
				{
					"slbUserIndex" : slbUsrIndex
				},				
				target: "#slbUsrReqList .reqModify",				
				successFn : function(params) 
				{					
					_registerSlbEvents(slbTypeIdx);					
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_VS_SCHEDULEUSER_LOADFAIL, jqXhr);
				}
			});
		}
	},
	
	delSlbUsers : function(chkDel)
	{
		with (this)
		{			
			ajaxManager.runJsonExt({
				url : "slbSchedule/delSlbUser.action",
				data :
				{
					"slbUserIndexes" : chkDel
				},
				successFn : function(data)
				{
					if (!data.isSuccessful) 
					{
						$.obAlertNotice(data.message);
						return;
					}
					_loadVServerNames(slbTypeIdx);
//					loadUserListContent(null, slbTypeIdx);
//					slbSchedule.loadListContent();
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_VS_SCHEDULEUSER_DELFAIL, jqXhr);
				}
			});
		}
	},
	
	_getCheckedSlbUsers : function()
	{
//		var chkSlbUsers = $('.cloneDiv .delSlbUsersLnk').parent().parent().find('.requestorChk').filter(':checked').map(function() {
		var chkSlbUsers = $('.cloneDiv .slbUsrChk').filter(':checked').map(function() {
			return $(this).val();
		}).get();
		
		return chkSlbUsers;
	},
	
	_getCheckSchdules : function()
	{
		var servers = $('.serverChk').filter(':checked').map(function() {
			return $(this).val();
		}).get();
		FlowitUtil.log(Object.toJSON(servers));
		return servers;
	},	
	_getCheckedReceivers : function()
	{
//		$.map($('.scheduleSmReceive').val().split('|'), function(item)
//		{
//			var attrs = item.split(',');
//			return {name: attrs[0], value: attrs[1]}; // [{name: '에이스', value: '0100000'}]
//		});
				
//		return  $.map($('.scheduleSmReceive').val().split('|'), function(item)
//		{	
//			// "에이스,01011112222|스마트,33334444|샤오미,01055556666"
//			var smReceivers = item.split(',');
//			console.log("smReceivers: ", smReceivers);
//			// "에이스,01011112222|스마트,33334444|샤오미,01055556666"
//			
//			var sms_name = smReceivers[0];
//			var sms_hp = smReceivers[1];
//			
//			return {
//				"name" : sms_name,
//				"hp" : sms_hp
//			};
//		}).get();
		
		return  $('.scheduleSmReceive').map(function(item) {
			
			// "에이스,01011112222|스마트,33334444|샤오미,01055556666"
			var smResp = $(this).val().split('|');
			var smReceivers = smResp[item].split(',');
			console.log("smReceivers: ", smReceivers);
			// "에이스,01011112222|스마트,33334444|샤오미,01055556666"
			
			var sms_name = smReceivers[0];
			var sms_hp = smReceivers[1];
			
			return {
				"name" : sms_name,
				"hp" : sms_hp
			};
		}).get();
		
//		return  $('.nodeChk').filter(':checked').map(function() {
//			var sms_name = $(this).val();
//			var sms_hp = $(this).parent().parent().find('td.ip').text();
//			
//			return {
//				"name" : sms_name,
//				"hp" : sms_hp
//			};
//		}).get();		
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
		}
		return true;		
	},
	_validateScheduleAdd : function()
	{
		var requestName = $('input[name="schedule.name"]').val();
		if (requestName == '')	//중복된 아이디 체크 추가 junhyun.ok_GS
		{
			$.obAlertNotice(VAR_VS_SCHEDULE_NAMEINPUT);
			return false;	
		}
		if(isExistSpecialCharacter(requestName)==false)
		{
			$.obAlertNotice(VAR_FAULTSETTING_SPECIALCHAR);
			return false;
		}
		
		var requestTeam = $('input[name="schedule.team"]').val();
		if (requestTeam == '')	//중복된 아이디 체크 추가 junhyun.ok_GS
		{
			$.obAlertNotice(VAR_VS_SCHEDULE_TEAMINPUT);
			return false;	
		}
		if(isExistSpecialCharacter(requestTeam)==false)
		{
			$.obAlertNotice(VAR_FAULTSETTING_SPECIALCHAR);
			return false;
		}
		
		var requestPhone = $('input[name="schedule.phone"]').val();
		if (requestPhone == "")
		{
			$.obAlertNotice(VAR_VS_SCHEDULE_PHONEINPUT);
			return false;
		}
		if(!$('input[name="schedule.phone"]').validate(
		{
			phonenum: $('input[name="schedule.phone"]').parent().parent().find('li').text(),
			type: "phonenum"
		}))
		{
			return false;
		}
		
		var respUsers = $('.scheduleSmReceiveUsers').text();
		if (respUsers == "")
		{
			$.obAlertNotice(VAR_VS_SCHEDULE_RECEIVERSEL);
			return false;
		}
		
		var nowDate = new Date();
		var nowYMD = nowDate.format('yyyy-mm-dd');
		var nowHour = nowDate.format('HH');
		var nowMin = nowDate.format('MM');
		
		var scheduleDate = $('input[name="startTime"]').val();
		var scheduleHour = $('.scheduleHour option:selected').val();
		var scheduleMin = $('.scheduleMin option:selected').val();
		
		var arr1 = nowYMD.split('-');
		var arr2 = scheduleDate.split('-');
		var dat1 = new Date(arr1[0], arr1[1], arr1[2]);
		var dat2 = new Date(arr2[0], arr2[1], arr2[2]);
		
		
		
		if((dat2-dat1) == 0)
		{
//			alert("예약일은 현재날짜보다 커야합니다.");
			if(scheduleHour < nowHour)
			{
				if (scheduleMin < nowMin)
				{
					$.obAlertNotice("예약일은 현재날짜보다 커야합니다.");
					return false;
				}
			}
		}
		
		
		return true;
	},
	_validateSlbUserAdd : function()
	{
		var userName = $('.cloneDiv input[name="slbUser.name"]').val();
		if (userName == '')	//중복된 아이디 체크 추가 junhyun.ok_GS
		{
			$.obAlertNotice(VAR_VS_SCHEDULE_NAMEINPUT);
			return false;	
		}
		if(isExistSpecialCharacter(userName)==false)
		{
			$.obAlertNotice(VAR_FAULTSETTING_SPECIALCHAR);
			return false;
		}
		
		var userTeam = $('.cloneDiv input[name="slbUser.team"]').val();
		if (userTeam == '')	//중복된 아이디 체크 추가 junhyun.ok_GS
		{
			$.obAlertNotice(VAR_VS_SCHEDULE_TEAMINPUT);
			return false;	
		}
		if(isExistSpecialCharacter(userTeam)==false)
		{
			$.obAlertNotice(VAR_FAULTSETTING_SPECIALCHAR);
			return false;
		}
		
		var userPhone = $('.cloneDiv input[name="slbUser.phone"]').val();
		if (userPhone == "")
		{
			$.obAlertNotice(VAR_VS_SCHEDULE_PHONEINPUT);
			return false;
		}
		if(!$('.cloneDiv input[name="slbUser.phone"]').validate(
		{
			phonenum: $('.cloneDiv input[name="slbUser.phone"]').parent().parent().find('li').text(),
			type: "phonenum"
		}))
		{
			return false;
		}
		
		return true;
	},		
	isValidVirtualSvrName : function(virtualSvrName) 
	{ 
		return /^[a-zA-Z](\w|-|\.|\*|\/|\:|\?|\=|\@|\,|\&)*$/.test(virtualSvrName);
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
	},
	
	noticePopPageInfo : function(slbTypeIdx)
	{
		with(this)
		{
			var currentPage = pagePopNavi.getCurrentPage();
			var lastPage = pagePopNavi.getLastPage();
			var countTotal = pagePopNavi.getRowTotal();
			var targetCntHtml = "";
			var targetPageHtml = "";
									
			if (slbTypeIdx == 1)
			{
				targetCntHtml = $('.noticeReqPageCountInfo').filter(':last');				
				targetPageHtml = $('.noticeReqPageInfo').filter(':last');				
			}	
			else if (slbTypeIdx == 0)
			{
				targetCntHtml = $('.noticeRespPageCountInfo').filter(':last');
				targetPageHtml = $('.noticeRespPageInfo').filter(':last');
			}			
			else
			{
				targetCntHtml = $('.noticeSlbPageCountInfo').filter(':last');
				targetPageHtml = $('.noticeSlbPageInfo').filter(':last');
			}
			
			targetCntHtml.html(addThousandSeparatorCommas(countTotal));
			targetPageHtml.html("(" + addThousandSeparatorCommas(currentPage) + "/" + addThousandSeparatorCommas(lastPage) + VAR_COMMON_PAGE + ")");
		}
	},	
	noticePopSlbPageInfo : function(slbTypeIdx)
	{
		with(this)
		{
			var currentPage = pagePopSlbNavi.getCurrentPage();
			var lastPage = pagePopSlbNavi.getLastPage();
			var countTotal = pagePopSlbNavi.getRowTotal();
			var targetCntHtml = "";
			var targetPageHtml = "";									
			
			targetCntHtml = $('.noticeSlbPageCountInfo').filter(':last');
			targetPageHtml = $('.noticeSlbPageInfo').filter(':last');
			
			targetCntHtml.html(addThousandSeparatorCommas(countTotal));
			targetPageHtml.html("(" + addThousandSeparatorCommas(currentPage) + "/" + addThousandSeparatorCommas(lastPage) + VAR_COMMON_PAGE + ")");
		}
	}
});
