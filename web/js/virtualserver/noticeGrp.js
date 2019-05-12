var NoticeGrp = Class.create({
	initialize : function() 
	{
		var fn = this;
		this.adc;
		this.searchedKey = undefined;
		this.orderDir  = 2; // 2 :  내림차순
		this.orderType = 1;// 1 : VSNAME
		this.orderDirOff  = 2; // 2 :  내림차순
		this.orderTypeOff = 1;// 1 : VSNAME
		this.rowOnTotal;
		this.rowOffTotal;
		this.searchFlag = false;
		this.pageNavi = new PageNavigator();	
		this.pageNavi.onChange(function(fromRow, toRow, orderType, orderDir) 
		{
			fn.loadNoticeOffTableInListContent(fn.searchedKey, fromRow, toRow, orderType, orderDir);
		});
		
		this.pageOffNavi = new PageNavigator({
			pageNaviSelector:".pageNavigatorOn",
			pageRowCountCbxSelector:".pageRowCountCbxOn",
			pageNaviCssClass:"pageNavigatorOn",
			pageRowCountCbxCssClass:"pageRowCountCbxOn",
			rowCountPerPage : 5,
			availableRowCountsPerPage:[5,10,20,30,40,50]});
		this.pageOffNavi.onChange(function(fromRow, toRow, orderType, orderDir)
		{			
			fn.loadNoticeOnTableInListContent(fn.searchedKey, fromRow, toRow, orderType, orderDir);
		});
	},
	onAdcChange : function()
	{
		this.orderDir = 2; //내림차순
		this.orderType = 1; //NodeIp
		this.orderDirOff  = 2; // 2 :  내림차순
		this.orderTypeOff = 1;// 1 : VSNAME
		this.loadNoticeGrpListContent(searchedKey);
	},	
	loadListContent : function(searchKey, orderDir, orderType)
	{
		with (this)
		{
			if(!validateDaterefresh())
			{
				return false;
			}
			
			var rowOnTotal = 0;
			var rowOffTotal = 0;
			ajaxManager.runJsonExt({
				url : "virtualServer/retrieverNoticeListTotal.action",
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
						rowOnTotal = 100;
						rowOffTotal = 100;
					}
					else
					{
						rowOnTotal = data.rowOnTotal;
						rowOffTotal = data.rowOffTotal;						
					}
					
					pageNavi.updateRowTotal(rowOffTotal, orderType);
					pageOffNavi.updateRowTotal(rowOnTotal, orderType);
					this.rowOnTotal = rowOnTotal;
					this.rowOnTotal = rowOnTotal;
					loadNoticeGrpListContent(searchKey);
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_NOTICE_VSLOAD_FAIL, jqXhr);
				}
			});
		}
	},
//	loadNoticeGrpListContent : function(searchKey, fromRow, toRow)
	loadNoticeGrpListContent : function(searchKey, fromRow, toRow)
//	loadNoticeGrpListContent : function(searchKey) 
	{
		if (adcSetting.getAdc().type == "F5")
		{
			if(header.getVsSettingTap() == 0)
			{
				virtualServer.loadListContent();
				return;
			}
			else if (header.getVsSettingTap() == 1)
			{
				profile.loadProfileListContent();
				return;
			}
			else if(header.getVsSettingTap() == 2)
			{
				node.loadListContent();
				return;
			}
		}
		
		with (this) 
		{	
			ajaxManager.runHtmlExt({
				url : "virtualServer/loadNoticeListContent.action",
				data : 
				{
					"adc.index" : adcSetting.getAdc().index,
					"adc.type" : adcSetting.getAdc().type,
					"adc.name" : adcSetting.getAdc().name,
//					"searchKey" : searchKey == null ? undefined : searchKey,
					"searchKey" : searchKey,
					"fromRow" : fromRow === undefined ? pageOffNavi.getFirstRowOfCurrentPage() : fromRow,
					"toRow" : toRow === undefined ? pageOffNavi.getLastRowOfCurrentPage() : toRow,					
					"orderDir" : this.orderDir,
					"orderType" : this.orderType,
					"fromRowOff" : fromRow === undefined ? pageNavi.getFirstRowOfCurrentPage() : fromRow,
					"toRowOff" : toRow === undefined ? pageNavi.getLastRowOfCurrentPage() : toRow,					
					"orderDirOff" : this.orderDirOff,
					"orderTypeOff" : this.orderTypeOff
				},
				target: "#wrap .contents",
				successFn : function(params) 
				{
					header.setActiveMenu('SlbSetting');
					on_noticePageInfo();
					off_noticePageInfo();
					searchedKey = searchKey;
					registerNoticeGrpListContentEvents(true);
					_availableNoticeOnChk();
//					_fillNoticeCount(this.rowOnTotal, this.rowOffTotal);
//					_noticeRevertChk();
//					_noticeChangeChk();
//					loadNoticeOnTableInListContent(searchKey, orderType, orderDir);
//					loadNoticeOffTableInListContent(searchKey, orderTypeOff, orderDirOff);
				},
				completeFn : function()
				{
					pageNavi.refresh();
					pageOffNavi.refresh();
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_NOTICE_VSLOAD_FAIL, jqXhr);
				}
			});
		}
	},
//	loadNoticeGrpListOffContent : function(searchKey, fromRowOff, toRowOff)
//	{
//		if(header.getVsSettingTap() == 0)
//		{
//			virtualServer.loadListContent();
//			return;
//		}
//		else if (header.getVsSettingTap() == 1)
//		{
//			profile.loadProfileListContent();
//			return;
//		}
//		else if(header.getVsSettingTap() == 2)
//		{
//			node.loadListContent();
//			return;
//		}
//		
//		with (this) 
//		{	
//			ajaxManager.runHtmlExt({
//				url : "virtualServer/loadNoticeListContent.action",
//				data : 
//				{
//					"adc.index" : adcSetting.getAdc().index,
//					"adc.type" : adcSetting.getAdc().type,
//					"adc.name" : adcSetting.getAdc().name,
//					"searchKey" : searchKey == null ? undefined : searchKey,
//					"fromRowOff" : fromRowOff === undefined ? pageNavi.getFirstRowOfCurrentPageOn() : fromRowOff,
//					"toRowOff" : toRowOff === undefined ? pageNavi.getLastRowOfCurrentPageOn() : toRowOff,					
//					"orderDirOff" : this.orderDirOff,
//					"orderTypeOff" : this.orderTypeOff,
//				},
//				target: "#wrap .contents",
//				successFn : function(params) 
//				{
//					header.setActiveMenu('SlbSetting');
//					registerNoticeGrpListContentEvents(false);
//					_noticeRevertChk();
//					_noticeChangeChk();
////					loadNoticeOnTableInListContent(searchKey, orderType, orderDir);
////					loadNoticeOffTableInListContent(searchKey, orderTypeOff, orderDirOff);
//				},
////				completeFn : function()
////				{
////					pageNavi.refresh();
////				},
//				errorFn : function(a, b, c)
//				{
//					alert("공지 로드에 실패했습니다.");
//				}
//			});
//		}
//	},
	loadNoticeOnTableInListContent : function(searchKey, fromRow, toRow, orderType, orderDir)
//	loadNoticeGrpTableInListContent : function(searchKey, orderType, orderDir)
	{
		with (this)
		{			
			ajaxManager.runHtmlExt({
				url : "virtualServer/loadNoticeOnTableInListContent.action",
				data : 
				{
					"adc.index" : adcSetting.getAdc().index,
					"adc.name" : adcSetting.getAdc().name,
					"adc.type" : adcSetting.getAdc().type,
					"fromRow" : fromRow === undefined ? pageNavi.getFirstRowOfCurrentPage() : fromRow,
					"toRow" : toRow === undefined ? pageNavi.getLastRowOfCurrentPage() : toRow,
//					"searchKey" : searchKey == null ? undefined : searchKey,
					"searchKey" : searchKey,
//					"refreshes" : !!refreshes,
					"orderDir" : this.orderDir,
					"orderType" : this.orderType	
				},
				target : "table.noticeOnTable",
				successFn : function(params)
				{
					on_noticePageInfo();
					searckedKey = searchKey;
//					_noticeRevertChk();
//					_noticeChangeChk();
					registerNoticeGrpListContentEvents(true);
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_NOTICE_VSLOAD_FAIL, jqXhr);
				}
			});
		}
	},
	loadNoticeOffTableInListContent : function(searchKey, fromRow, toRow, orderType, orderDir)
	{
		with (this)
		{			
			ajaxManager.runHtmlExt({
				url : "virtualServer/loadNoticeOffTableInListContent.action",
				data : 
				{
					"adc.index" : adcSetting.getAdc().index,
					"adc.name" : adcSetting.getAdc().name,
					"adc.type" : adcSetting.getAdc().type,
					"fromRowOff" : fromRow === undefined ? pageOffNavi.getFirstRowOfCurrentPage() : fromRow,
					"toRowOff" : toRow === undefined ? pageOffNavi.getLastRowOfCurrentPage() : toRow,					
//					"searchKey" : searchKey == null ? undefined : searchKey,
					"searchKey" : searchKey,
					"orderDirOff" : this.orderDir,
					"orderTypeOff" : this.orderType,
				},
				target : "table.noticeOffTable",
				successFn : function(params)
				{
					off_noticePageInfo();
					searckedKey = searchKey;
//					_noticeRevertChk();
//					_noticeChangeChk();
					registerNoticeGrpListContentEvents(false);
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_NOTICE_SERVICE_LOAD_FAIL, jqXhr);
				}
			});
		}
	},
	_fillNoticeCount : function(rowOnTotal, rowOffTotal)
	{
		with (this)
		{
			var html = '';
			html += '<span>('+rowOnTotal+')</span>';
			
			var $noticeOnCount = $('.noticeOnCount');
			$noticeOnCount.append(html);
			
			var htmlOff = '';
			htmlOff += '<span>('+rowOffTotal+')</span>';
			
			var $noticeOffCount = $('.noticeOffCount');
			$noticeOffCount.append(htmlOff);
			
		}
	},
	_availableNoticeOnChk : function()
	{
		var vsServerChange = $('#noticeChangeCount tr.ContentsLine1.vsServerChangeList td');
		for (var i= 0; i< $('.vsServerChangeList').size(); i++) 
		{
			if (vsServerChange.find('.sPoolName').eq(i).text().trim() == "" || vsServerChange.find('.noticePool').eq(i).text().trim() == "-" 
				|| vsServerChange.find('.sPoolIndex').eq(i).text().trim() == vsServerChange.find('.nPoolIndex').eq(i).text().trim())
			{
				vsServerChange.find('#noticeChangeGrp').eq(i).attr("disabled", "disabled");
			}
		}
	},
	registerNoticeGrpListContentEvents : function(flag) 
	{
		with (this) 
		{	
			$('.orderDir_Desc').click(function(e)
			{
				e.preventDefault();
				orderDir =  $(this).find('.orderDir').text().trim();
				orderType =  $(this).find('.orderType').text().trim();
				var searchKey = $('input[name="textfield3"]').val();
				searchFlag=true;
				loadListContent(searchKey , orderDir , orderType);
			});
			
			$('.orderDir_Asc').click(function(e)
			{
				e.preventDefault();
				orderDir =  $(this).find('.orderDir').text().trim();
				orderType =  $(this).find('.orderType').text().trim();
				var searchKey = $('input[name="textfield3"]').val();	
				searchFlag=true;
				loadListContent(searchKey , orderDir , orderType);
			});
			
			$('.orderDir_None').click(function(e)
			{
				e.preventDefault();
				orderDir =  $(this).find('.orderDir').text().trim();
				orderType =  $(this).find('.orderType').text().trim();
				var searchKey = $('input[name="textfield3"]').val();	
				searchFlag=true;
				loadListContent(searchKey , orderDir , orderType);
			});
		
			$('.orderDirOff_Desc').click(function(e)
			{
				e.preventDefault();
				orderDirOff =  $(this).find('.orderDirOff').text().trim();
				orderTypeOff =  $(this).find('.orderTypeOff').text().trim();
				var searchKey = $('input[name="textfield3"]').val();	
				searchFlag=true;
				loadListContent(searchKey , orderDirOff , orderTypeOff);
			});
			
			$('.orderDirOff_Asc').click(function(e)
			{
				e.preventDefault();
				orderDirOff =  $(this).find('.orderDirOff').text().trim();
				orderTypeOff =  $(this).find('.orderTypeOff').text().trim();
				var searchKey = $('input[name="textfield3"]').val();	
				searchFlag=true;
				loadListContent(searchKey , orderDirOff , orderTypeOff);
			});
			
			$('.orderDirOff_None').click(function(e)
			{
				e.preventDefault();
				orderDirOff =  $(this).find('.orderDirOff').text().trim();
				orderTypeOff =  $(this).find('.orderTypeOff').text().trim();
				var searchKey = $('input[name="textfield3"]').val();		
				searchFlag=true;
				loadListContent(searchKey , orderDirOff , orderTypeOff);
			});
			
			$('.vServerLnk').click(function(e) 
			{
				e.preventDefault();
				header.setVsSettingTap(0);
				virtualServer.loadListContent();
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
				loadListContent();
			});
			
			$('.noticeGrpSetLnk').click(function(e)
			{
				noticePopup();
			});
			
			$('.refreshLnk').click(function(e)
			{
				downloadVSList();
			});
			
			$('.noticeChangeLnk').click(function(e)
			{
				e.preventDefault();
				var $nodeTRs = _getCheckNodeTRs();
				
				if ($nodeTRs.length == 0)
				{
					$.obAlertNotice(VAR_NOTICE_WANT_CHANGE_VSSEL);
					return;
				}				
				
				chkPairDevice(true);
//				setNoticeGrpChange(true);
			});
			
			$('.noticeOnTable tr:has(".servicePool"):not(:has(".sPoolIndex"))').find('td:first input[type=checkbox]').attr('disabled','disabled');
			$('.noticeOffTable tr:has(".noticePool"):not(:has(".nPoolIndex"))').find('td:first input[type=checkbox]').attr('disabled','disabled');
			
//			_noticeRevertChk();
//			_noticeChangeChk();
			
			$('.noticeRevertLnk').click(function(e)
			{
				e.preventDefault();
//				var $nodeTRs = _getCheckNotTRs();
				var $nodeTRs = $('.selectedNotice .noticeRevertGrpChk').filter(':checked').parent().parent();
				
				if ($nodeTRs.length == 0)
				{
					$.obAlertNotice(VAR_NOTICE_WANT_REVERT_VSSEL);
					return;
				}		
				
				chkPairDevice(false);
//				setNoticeGrpChange(false);				
			});
			
			$('.allNoitceRevertGrpChk').click(function(e) 
			{
				var isChecked = $(this).is(':checked');


				for (var i= 0; i< $('.vsServerRevertList').size(); i++) 
				{					
					if ($('#noticeCount tr.ContentsLine1.vsServerRevertList td').find('.servicePool').eq(i).text().trim() != "-")
					{
						$('#noticeCount tr.ContentsLine1.vsServerRevertList td').find('#noticeRevertGrp').eq(i).attr("checked", isChecked);	
					}
					else
					{
						$('#noticeCount tr.ContentsLine1.vsServerRevertList td').find('#noticeRevertGrp').eq(i).removeAttr("checked");	
					}					
				}
								
//				if (isChecked)
//				{
//					for (var i= 0; i< $('.vsServerRevertList').size(); i++) 
//					{
//						ischk = $('.noticeRevertGrpChk').eq(i).attr("ischk");
//						if (ischk == 1)
//						{
//							$('.noticeRevertGrp').attr('checked', isChecked);
//						}
//						else
//						{
//							$('.noticeRevertGrp').attr('checked', false);
//						}
//					}
//				}
//				else
//				{
//					for (var i= 0; i< $('.vsServerRevertList').size(); i++) 
//					{
//						ischk = $('.noticeRevertGrpChk').eq(i).attr("ischk");
//						if (ischk == 1)
//						{
//							$('.noticeRevertGrp').attr('checked', isChecked);
//						}
//						else
//						{
//							$('.noticeRevertGrp').attr('checked', true);
//						}
//					}
//				}
//				var ischk = $('.noticeRevertGrpChk').attr("ischk");
	
//					if (ischk == 1)
//					{						
//						$('.noticeRevertGrp').attr('checked', isChecked);
//					}
//					else
//					{
//						$('.noticeRevertGrp').attr('checked', false);
//					}

					
//				$(this).parent().parent().parent().parent().find('.noticeRevertGrpChk').attr('checked', isChecked);
			});
			

			$('.allNoticeGrpChk').click(function(e) 
			{
				var isChecked = $(this).is(':checked');
				
				for (var i= 0; i< $('.vsServerChangeList').size(); i++) 
				{					
					if (($('#noticeChangeCount tr.ContentsLine1.vsServerChangeList td').find('.noticePool').eq(i).text() != "-" &&
							$('#noticeChangeCount tr.ContentsLine1.vsServerChangeList td').find('.sPoolName').eq(i).text() != ""))
					{
//						alert("noticeChangeNot");
						$('#noticeChangeCount tr.ContentsLine1.vsServerChangeList td').find('#noticeChangeGrp').eq(i).attr("checked", isChecked);
						$('.noticeChangeGrp').eq(i).attr("ischk", 1);
					}
					else
					{
//						alert("noticeChangeYes");
						$('#noticeChangeCount tr.ContentsLine1.vsServerChangeList td').find('#noticeChangeGrp').eq(i).removeAttr("checked");
//						$('.allNoticeGrpChk').removeAttr("checked");
					}
					
				}
				
//				$(this).parent().parent().parent().parent().find('.noticeGrpChk').attr('checked', isChecked);
			});
						
			// search event
//			$('p.cont_sch a.searchLnk').click(function (e) {
			$('.btn a.searchLnk').click(function (e) 
			{
				e.preventDefault();
				var searchKey = $('input[name="searchKey"]').val();
				log.debug('click:' + searchKey);
				searchFlag=true;
				loadListContent(searchKey);
//				loadNoticeGrpTableInListContent(searchKey);
			});
			
			$('.inputTextposition1 input.searchTxt').keydown(function(e) 
			{
				if (e.which != 13)
					return;
				
				var searchKey = $(this).val();
				log.debug('click:' + searchKey);
				searchFlag=true;
				loadListContent(searchKey);
//				loadNoticeGrpTableInListContent(searchKey);
			});
						
			if (adcSetting.getAdc().mode == 1)
			{		
				$('.imgOff').removeClass("none");
				$('.imgOn').addClass("none");
				
//				$('.allNoitceRevertGrpChk').attr("disabled", "disabled");
//				$('.noticeRevertGrpChk').attr("disabled", "disabled");
				$('.allNoticeGrpChk').attr("disabled", "disabled");
				$('.noticeGrpChk').attr("disabled", "disabled");	
				
//				Button disabled 버튼으로 치환
//				$('.noticeRevertLnk')
//				$('.noticeChangeLnk')				
			}
			else
			{
//				$('.allNoitceRevertGrpChk').removeAttr("disabled");
//				$('.noticeRevertGrpChk').removeAttr("disabled");
				$('.allNoticeGrpChk').removeAttr("disabled");
				$('.noticeGrpChk').removeAttr("disabled");	
				
				$('.imgOff').addClass("none");
				$('.imgOn').removeClass("none");
			}
			
			if(this.searchFlag == true)
			{
//				$('.nullMsgOn').addClass("none");
				$('.nulldataMsg').addClass("none");
				$('.dataNotExistMsg').addClass("none");
				if($('.vsServerRevertList').size() > 0)
				{
					$('.msgOn').addClass("none");
					$('.searchOn').addClass("none");
				}
				else
				{
					$('.msgOn').addClass("none");
					$('.searchOn').removeClass("none");
				}
				
				if($('.vsServerChangeList').size() > 0)
				{
					$('.msgOff').addClass("none");
					$('.searchOff').addClass("none");
				}
				else
				{
					$('.msgOff').addClass("none");
					$('.searchOff').removeClass("none");
				}
				searchFlag=false;
			}
			else
			{
				$('.nulldataMsg').addClass("none");
				$('.dataNotExistMsg').addClass("none");
				$('.searchNotMsg').addClass("none");				
				
				if($('.vsServerRevertList').size() > 0)
				{
					$('.msgOn').addClass("none");
				}
				else
				{
					if (adcSetting.getAdcStatus() != "available")
					{							
						$('.nullMsgOn').removeClass("none");
						$('.msgOn').addClass("none");
					}
					else
					{
						$('.nullMsgOn').addClass("none");
						$('.msgOn').removeClass("none");
					}
				}
				
				if($('.vsServerChangeList').size() > 0)
				{
					$('.msgOff').addClass("none");
//					$('.nulldataMsg').addClass("none");
				}
				else
				{
					$('.msgOff').removeClass("none");
//					$('.nulldataMsg').removeClass("none");
				}
			}
				

			
			if (adcSetting.getAdcStatus() != "available")
			{		
				$('.searchNotMsg').addClass("none");
				$('.msgOn').addClass("none");
				$('.msgOff').addClass("none");
				$('.imgOff').removeClass("none");
				$('.imgOn').addClass("none");
				
				$('.allNoitceRevertGrpChk').attr("disabled", "disabled");
				$('.noticeRevertGrpChk').attr("disabled", "disabled");
				$('.allNoticeGrpChk').attr("disabled", "disabled");
				$('.noticeGrpChk').attr("disabled", "disabled");	
								
//				if ($('.vsServerRevertList').size() > 0 )
//				{
//					$('.disabledChk').removeClass("none");
//					$('.nulldataMsg').addClass("none");	   					
//				}
//				else
//				{
//					$('.disabledChk').addClass("none");
//					$('.nulldataMsg').removeClass("none");
//					$('input[name="searchKey"]').attr("disabled", "disabled");
//					$('.imageChange').attr("src", "imgs/meun/btn_search_1_off.png");
//				}
			}
//			
//			if ($('.vsServerRevertList').size() > 0 )
//			{
//				$('.nullMsgOn').addClass("none");	   					
//			}
//			else
//			{
//				$('.nullMsgOn').removeClass("none");
//			}
//			
//			if ($('.vsServerChangeList').size() > 0 )
//			{
//				$('.nulldataMsg').addClass("none");	   					
//			}
//			else
//			{
//				$('.nulldataMsg').removeClass("none");
//			}
		}
	},	
	_noticeRevertChk : function()
	{
//		var test1 = $('.vsServerRevertList');
		//$('.noticeOnTable tr:has(".servicePool"):not(:has(".sPoolIndex"))').find('td:first input[type=checkbox]').attr('disabled','disabled')
		for (var i= 0; i< $('.vsServerRevertList').size(); i++) 
		{	
//			if (test1[i].find('.servicePool').text() == "-"
			if ($('#noticeCount tr.ContentsLine1.vsServerRevertList td').find('.servicePool').eq(i).text() == "-")
			{
//				alert("noticeRevertNot");
				$('#noticeCount tr.ContentsLine1.vsServerRevertList td').find('#noticeRevertGrp').eq(i).attr("disabled", "disabled");	

//				$('#allCount tr.ContentsHeadLine').find('#allNoticeGrpChk').attr("disabled", "disabled");	
				$('.noticeRevertGrpChk').eq(i).attr("ischk", 1);
			}
//			else
//			{
////				alert("noticeRevertYes");
//				$('#noticeCount tr.ContentsLine1.vsServerRevertList td').find('#noticeRevertGrp').eq(i).removeAttr("disabled");
//			}
			
		}
	},
	_noticeChangeChk : function()
	{
		for (var i= 0; i< $('.vsServerChangeList').size(); i++) 
		{					
			if ($('#noticeChangeCount tr.ContentsLine1.vsServerChangeList td').find('.noticePool').eq(i).text() == "-")
			{
//				alert("noticeChangeNot");
				$('#noticeChangeCount tr.ContentsLine1.vsServerChangeList td').find('#noticeChangeGrp').eq(i).attr("disabled", "disabled");
				$('.noticeChangeGrp').eq(i).attr("ischk", 1);
			}
//			else
//			{
////				alert("noticeChangeYes");
//				$('#noticeChangeCount tr.ContentsLine1.vsServerChangeList td').find('#noticeChangeGrp').eq(i).removeAttr("disabled");
//			}
			
		}
	},
	
	// 장비 이중화 체크
	chkPairDevice : function(flag)
	{
		with (this)
		{
			ajaxManager.runJsonExt({
				url : "virtualServer/checkNoticePairIndex.action",
				data :
				{
					"adc.index" : adcSetting.getAdc().index,
					"adc.name" : adcSetting.getAdc().name,
					"adc.type" : adcSetting.getAdc().type
				},
				successFn : function(data)
				{								
					if (data.pairIndex!='' && data.pairIndex!=null && data.pairIndex>0) 
					{
						var pairIdx = data.pairIndex;
						var chk = confirm(data.message);								
						if (chk)
						{
							// 이중화 장비의 pool member 및 공지 그룹 설정 유무 확인
							$(this).ajaxSubmit({
								url : "virtualServer/existVServerNoticeOn.action",
								data : 
								{ 
									"adc.index" : data.pairIndex,
									"adc.name" : adcSetting.getAdc().name,
									"adc.type" : adcSetting.getAdc().type,
									"vsNoticeInString" : Object.toJSON(_getCheckedNoticePair(flag, data.pairIndex)),
									"isNoticeFlag" : flag,
									"orderDir" : this.orderDir,
									"orderType" : this.orderType									
								},
								success : function(data) 
								{
									if (!data.isSuccessful)
									{
										$.obAlertNotice(data.message);
//										loadListContent(searchedKey, this.orderType, this.orderDir);
										return;
									}
									else
									{
										// 이중화 장비 존재 active 장비 & standby 장비 모두 공지전환 작업 진행
										setNoticeGrpChange(flag, pairIdx, true);
										return false;
									}
								},
								error : function(jqXhr)
								{
									$.obAlertAjaxError(VAR_COMMON_VSAMFAIL, jqXhr);
//									loadListContent(searchedKey, this.orderType, this.orderDir);
								}
							});		
						}
						else
						{
							// 이중화 장비 존재하지만 active 장비만 공지전환 작업 진행
							setNoticeGrpChange(flag, null, false);
						}
//						loadListContent(searchedKey, this.orderType, this.orderDir);
					}
					else
					{
						setNoticeGrpChange(flag, null, false);
					}
					loadListContent(searchedKey, this.orderType, this.orderDir);
					return false;
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_COMMON_PEIFAIL, jqXhr);
				}	
			});
		}		
	},
	setNoticeGrpChange : function(flag, pairIndex, chkPair)
	{
		with (this)
		{
			var noticeUrl = "";
			if (flag)
			{
				noticeUrl = "virtualServer/setVServerNoticeOn.action";
			}
			else
			{
				noticeUrl = "virtualServer/setVServerNoticeOff.action";
			}
			
			var chkNoticeVal;
			if (adcSetting.getAdc().type == "Alteon")
				chkNoticeVal = Object.toJSON(_getCheckedAlteonNotice(flag));
			else
				chkNoticeVal = Object.toJSON(_getCheckedNotice(flag)); 
			
			ajaxManager.runJsonExt({
				url : noticeUrl,
				data : 
				{
					"adc.index" : adcSetting.getAdc().index,
					"adc.name" : adcSetting.getAdc().name,
					"adc.type" : adcSetting.getAdc().type,
//					"vsNoticeInString" : Object.toJSON(_getCheckedNotice(flag)),
					"vsNoticeInString" : chkNoticeVal,
					"isNoticeFlag" : flag,
					"orderDir" : this.orderDir,
					"orderType" : this.orderType
				},
				target : "#wrap .contents_area",
				successFn : function(data)
				{				
//					if (!data.isSuccessful)
//					{
//						$.obAlertNotice(data.message); //중복 메세지 처리.
//						return;
//					}
							
					if (chkPair)
					{
						$(this).ajaxSubmit({
							dataType : 'json',
							url : noticeUrl,
							data : 
							{ 
								"adc.index" : pairIndex,
								"adc.name" : adcSetting.getAdc().name,
								"adc.type" : adcSetting.getAdc().type,
								"vsNoticeInString" : Object.toJSON(_getCheckedNoticePair(flag, pairIndex)),
								"isNoticeFlag" : flag,
								"orderDir" : this.orderDir,
								"orderType" : this.orderType
							},
							success : function(data) 
							{
	//							if (!data.isSuccessful)
	//							{
	//								$.obAlertNotice(data.message);
	//								loadListContent(searchedKey, this.orderType, this.orderDir);
	//								return;
	//							}
	//							else
	//							{
	//								loadListContent(searchedKey, this.orderType, this.orderDir);
	//								return false;
	//							}
								loadListContent(searchedKey, this.orderType, this.orderDir);
							},
							error : function(jqXhr)
							{
								$.obAlertAjaxError(VAR_COMMON_VSAMFAIL, jqXhr);
								loadListContent(searchedKey, this.orderType, this.orderDir);
							}
						});
					}
				},				
				errorFn : function(jqXhr)
				{					
					if (jqXhr.responseText.indexOf('Sync finished') > -1)
					{
						alert(VAR_NODE_SYNC_STATUS_RESET + " - " + VAR_NODE_SYNC_COMPLETE_RESET);
					}
					else if (jqXhr.responseText.indexOf('Sync failed') > -1 || jqXhr.responseText.indexOf('Illegal null') > -1)
					{						
						$.obAlertAjaxError(VAR_VS_VSTIMEERROR, jqXhr);
					}
					else
					{
						if(flag == true)
						{
							$.obAlertAjaxError(VAR_NOTICE_CHANGE_FAIL, jqXhr);
						}
						else
						{
							$.obAlertAjaxError(VAR_NOTICE_RESTORE_FAIL, jqXhr);	
						}
					}
				}
			});
		}
	},
	
	_getCheckedNoticePair : function(flag, pairIndex)
	{
		var chk = "";
		if(flag)
		{
			chk = $('.noticeGrpChk');
		}
		else
		{
			chk = $('.noticeRevertGrpChk');
		}
		return  chk.filter(':checked').map(function() {
			var notice_index = $(this).val();
			var adc_index = notice_index.split("_")[0];
			var notice_pair_index = notice_index.replace(adc_index, pairIndex);
			var notice_vsStatus = $(this).parent().parent().find('td span.vsStatus').text();
			var notice_vsName = $(this).parent().parent().find('td.vsName').text();
			var notice_vsIp = $(this).parent().parent().find('td.vsIp').text();
			var notice_vsPort = $(this).parent().parent().find('td.vsPort').text();
			var notice_sPoolIndex = $(this).parent().parent().find('td span.sPoolIndex').text();
			var notice_nPoolIndex = $(this).parent().parent().find('td span.nPoolIndex').text();
			var notice_pair_nPoolIndex = notice_nPoolIndex.replace(adc_index, pairIndex);
			var notice_sPoolName = $(this).parent().parent().find('td span.sPoolName').text().trim();
			var notice_nPoolName = $(this).parent().parent().find('td span.nPoolName').text().trim();
			var notice_isNotice = flag;
			
			return {
				"index" : notice_pair_index,
				"vsStatus" : notice_vsStatus,
				"vsName" : notice_vsName,
				"virtualIp" : notice_vsIp,
				"servicePort" : notice_vsPort,
				"servicePoolIndex" : notice_sPoolIndex,
				"noticePoolIndex" : notice_pair_nPoolIndex,
				"servicePoolName" : notice_sPoolName,
				"noticePoolName" : notice_nPoolName,
				"isNotice" : notice_isNotice
			};
		}).get();
	},
	_getCheckedNotice : function(flag)
	{
		var chk = "";
		if(flag)
		{
			chk = $('.noticeGrpChk');
		}
		else
		{
			chk = $('.noticeRevertGrpChk');
		}
		return  chk.filter(':checked').map(function() {
			var notice_index = $(this).val();
			var notice_vsStatus = $(this).parent().parent().find('td span.vsStatus').text();
			var notice_vsName = $(this).parent().parent().find('td.vsName').text();
			var notice_vsIp = $(this).parent().parent().find('td.vsIp').text();
			var notice_vsPort = $(this).parent().parent().find('td.vsPort').text();
			var notice_sPoolIndex = $(this).parent().parent().find('td span.sPoolIndex').text();
			var notice_nPoolIndex = $(this).parent().parent().find('td span.nPoolIndex').text();
			var notice_sPoolName = $(this).parent().parent().find('td span.sPoolName').text().trim();
			var notice_nPoolName = $(this).parent().parent().find('td span.nPoolName').text().trim();
			var notice_isNotice = flag;
			
			return {
				"index" : notice_index,
				"vsStatus" : notice_vsStatus,
				"vsName" : notice_vsName,
				"virtualIp" : notice_vsIp,
				"servicePort" : notice_vsPort,
				"servicePoolIndex" : notice_sPoolIndex,
				"noticePoolIndex" : notice_nPoolIndex,
				"servicePoolName" : notice_sPoolName,
				"noticePoolName" : notice_nPoolName,
				"isNotice" : notice_isNotice
			};
		}).get();
	},
	_getCheckedAlteonNotice : function(flag)
	{
		var chk = "";
		if(flag)
		{
			chk = $('.noticeGrpChk');
		}
		else
		{
			chk = $('.noticeRevertGrpChk');
		}
		return  chk.filter(':checked').map(function() {
			var notice_index = $(this).val();
			var notice_vsStatus = $(this).parent().parent().find('td span.vsStatus').text();
			var notice_vsName = $(this).parent().parent().find('td.vsName').text();
			var notice_vsIp = $(this).parent().parent().find('td.vsIp').text();
			var notice_vsPort = $(this).parent().parent().find('td.vsPort').text();
			var notice_sPoolIndex = $(this).parent().parent().find('td span.sPoolIndex').text();
			var notice_nPoolIndex = $(this).parent().parent().find('td span.nPoolIndex').text();
			var notice_sPoolName = $(this).parent().parent().find('td span.sPoolName').text().trim();
			var notice_nPoolName = $(this).parent().parent().find('td span.nPoolName').text().trim();
			var notice_alteonID = $(this).parent().parent().find('td.alteonID').text();
			var notice_servicePoolAlteonID = $(this).parent().parent().find('td span.sPoolAlteonID').text().trim();
			var notice_isNotice = flag;
			
			return {
				"index" : notice_index,
				"vsStatus" : notice_vsStatus,
				"vsName" : notice_vsName,
				"virtualIp" : notice_vsIp,
				"servicePort" : notice_vsPort,
				"servicePoolIndex" : notice_sPoolIndex,
				"noticePoolIndex" : notice_nPoolIndex,
				"servicePoolName" : notice_sPoolName,
				"noticePoolName" : notice_nPoolName,
				"alteonID" : notice_alteonID,
				"servicePoolAlteonID" : notice_servicePoolAlteonID,
				"isNotice" : notice_isNotice
			};
		}).get();		
	},
//	_getCheckedServers : function()
//	{
//		var servers = $('.noticeGrpChk').filter(':checked').map(function() {
//			return $(this).val();
//		}).get();
//		FlowitUtil.log(Object.toJSON(servers));
//		return servers;
//	},
	_getCheckNodeTRs : function()
	{
		return $('.selectedNoticeGrp .noticeGrpChk').filter(':checked').parent().parent();
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
//				return false;
//			}			
		}
		
		return true;		
	},
	downloadVSList : function()
	{
		with (this) 
		{			
			ajaxManager.runJsonExt({
				url : "virtualServer/downloadVSList.action",
				data : 
				{
					"adc.index" : adcSetting.getAdc().index,
					"adc.type" : adcSetting.getAdc().type,
					"adc.name" : adcSetting.getAdc().name
				},
				successFn : function(data) 
				{
					if(!data.isSuccessful)
					{
						$.obAlertNotice(data.message);
					}
					else
					{
						loadNoticeGrpListContent();
					}
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_NOTICE_GROUPLOAD_FAIL, jqXhr);
				}	
			});
		}
	},
	noticePopup : function() 
	{
		with (this)
		{
			showPopup({
				"id" : "#noticeGrp",
				"width" : "494px"
			});
//			_loadDetail();
			newPoolName = '';
			_registerEvents();
			_updatePoolIndexInPoolCbxByPoolNameInPoolText();
		}
	},
	_loadDetail : function()
	{
		with (this) 
		{			
			ajaxManager.runJsonExt({
				url : "virtualServer/retrieveGrpMemList.action",
				data : 
				{
					"adc.index" : adcSetting.getAdc().index,
					"adc.type" : adcSetting.getAdc().type,
					"adc.name" : adcSetting.getAdc().name
				},
				successFn : function(data) 
				{
//					FlowitUtil.log('profileAdd: ' + Object.toJSON(data.profileAdd));
//					_fillDetail(data.profileAdd);
//					fillGrpMemList(data.noticeGrpList);
//					alert(data.noticeGrpList);
					
//					var noticeGrpObj = [];
////					
//					for (var i=0; i < data.noticeGrpList.length; i++ )
//					{
//						noticeGrpObj.push(data.noticeGrpList[i].poolName);						
//					}				
//					alert(noticeGrpObj);
//					$('.cloneDiv #selectGrpMemList').(noticeGrpObj);
//					$('.cloneDiv #selectGrpMemList').text(data.noticeGrpList);
//					_registerEvents();
					_updatePoolIndexInPoolCbxByPoolNameInPoolText();
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_NOTICE_ALLGROUPLOAD_FAIL, jqXhr);
//					exceptionEvent();
				}	
			});
		}
	},
	_updatePoolIndexInPoolCbxByPoolNameInPoolText : function() 
	{
		with (this) 
		{
//			var poolName = $('.cloneDiv input[name="grpPoolMemberName"]').val();
			var poolName = $('.vsServerChangeList .nPoolName').filter(':first').text();
			
//			if ($('.cloneDiv input[name="grpPoolMemberName"]').val() != null)
			if (poolName != null && poolName != "")	
			{
//				poolName = $('.nPoolName').eq(0).text();
				poolName = $('.nPoolName').filter(':last').text();
			}
			
			
			FlowitUtil.log(poolName);
			var pools = $('.cloneDiv .selectGrpMemList option').map(function() 
			{
				return {
					"index" : $(this).val(),
					"name" :$(this).text()
				};
			}).get();
			for (var i=0; i < pools.length; i++)
			{
//				FlowitUtil.log(Object.toJSON(pools[i]));
//				FlowitUtil.log(i + ": " + (pools[i].name == poolName));
				if (pools[i].name == poolName) 
				{
					var $poolOptionToSelect = $('.cloneDiv .selectGrpMemList option[value="' + pools[i].index + '"]');
					$poolOptionToSelect.attr('selected', true);
					
					$('.cloneDiv .selectGrpMemList').change();
					return;
				}
			}
			
			$('.cloneDiv .selectGrpMemList option').filter(':first').attr('selected', true);
		}
	},
//	fillGrpMemList : function(noticeGrpMems) 
//	{
//		if (noticeGrpMems == null)
//		{
//			return;
//		}
//		var $tbody = $('.cloneDiv .selectGrpMemList').filter(':last');
//		$tbody.empty();
//		var html = ''; 
//		html += '<option value="0">select group</option>';
//		for (var i=0; i < noticeGrpMems.length; i++) 
//		{
//			html += '<option value="' + noticeGrpMems[i].poolIndex + '">' + noticeGrpMems[i].poolName + '</option>';
//		}
//		$tbody.html(html);
//	},
	_registerEvents : function()
	{
		with (this) 
		{
			$('.cloneDiv .noticeOk').click(function(e) 
			{
				e.preventDefault();
				setNoticeGrp();
			});
			
			$('.cloneDiv .noticeCancel').click(function(e) 
			{
				e.preventDefault();
				$('.cloneDiv .close').click();
			});
			
			$('.cloneDiv .selectGrpMemList').change(function(e)
			{
				var $poolName = $(this).prop('selectedIndex') == 0 ? newPoolName : $(this).children('option').filter(':selected').text();
				$('.grpPoolMemberName').val($poolName);
			});
		}
	},
	setNoticeGrp : function()
	{
		with (this)
		{
//			alert("class : " + $('.cloneDiv .selectGrpMemList option:selected').val());
//			alert("id : " + $('.cloneDiv #selectGrpMemList option:selected').val());
			var selectedGrp = $('.cloneDiv .selectGrpMemList option:selected').text();
			var noticeGrp = $('.cloneDiv input[name="noticeGrpNmChk"]').val();
//			alert(selectedGrp + " :: " + noticeGrp);
//			if (!_checkNoticeGrpValidate())
		
//			if ($('.vsServerRevertList').length > 0)
//			{
//				$.obAlertNotice(VAR_NOTICE_VSSET_ARREADY);
//				return;
//			}
//			if (selectedGrp === noticeGrp)
//			{
//				$.obAlertNotice(VAR_NOTICE_ARREADY_SETGROUP);
//				return;
//			}
			ajaxManager.runJsonExt({
				url : "virtualServer/setNoticeGroup.action",
				data : 
				{
					"adc.index" : adcSetting.getAdc().index,
					"adc.name" : adcSetting.getAdc().name,
					"adc.type" : adcSetting.getAdc().type,
//					"nodeIndexList" : checkedNodes,
//					"state" : state,
//					"noticeInString" : Object.toJSON(_getNoticeGroups()),
					"noticeInString" : $('.cloneDiv .selectGrpMemList option:selected').val(),
//					"noticeInString" : selectedGrp,
					"orderDir" : this.orderDir,
					"orderType" : this.orderType
				},
				target : "#wrap .contents_area",
				successFn : function(data)
				{
					if (!data.isSuccessful) 
					{
						$.obAlertNotice(data.message);
						return;
					}
					
					$('.cloneDiv .close').click();
//					loadNoticeGrpListContent(searchedKey, this.orderType, this.orderDir); 
					loadListContent(searchedKey, this.orderType, this.orderDir);
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_NOTICE_GROUPSET_FAIL, jqXhr);
				}
			});
		}
	},
	on_noticePageInfo : function()
	{
		with(this)
		{
			var currentPage2 = pageOffNavi.getCurrentPage();
			var lastPage2 = pageOffNavi.getLastPage();
			var countTotal2 = pageOffNavi.getRowTotal();
			var targetCntHtml = $('.on_noticePageCountInfo').filter(':last');
			var targetPageHtml = $('.on_noticePageInfo').filter(':last');
			targetCntHtml.html(addThousandSeparatorCommas(countTotal2));
			targetPageHtml.html("(" + addThousandSeparatorCommas(currentPage2) + "/" + addThousandSeparatorCommas(lastPage2) + VAR_COMMON_PAGE + ")");
		}
	},
	off_noticePageInfo : function()
	{
		with(this)
		{
			var currentPage = pageNavi.getCurrentPage();
			var lastPage = pageNavi.getLastPage();
			var countTotal = pageNavi.getRowTotal();
			var targetCntHtml = $('.off_noticePageCountInfo').filter(':last');
			var targetPageHtml = $('.off_noticePageInfo').filter(':last');
			targetCntHtml.html(addThousandSeparatorCommas(countTotal));
			targetPageHtml.html("(" + addThousandSeparatorCommas(currentPage) + "/" + addThousandSeparatorCommas(lastPage) + VAR_COMMON_PAGE + ")");
		}
	}
//	_getNoticeGroups : function()
//	{
//		return  $('.selectGrpMemList : option').filter(':selected').map(function() {
//			var pool_index = $(this).val();
//			var _ipaddress = $(this).parent().parent().find('td.ip').text();
//			var node_name = $(this).parent().parent().find('td.name').text();
//			var node_ratio = $(this).parent().parent().find('td.ratio').text();
//			var node_state = $(this).parent().parent().find('select').children('option').filter(':selected').val();
//			return {
//				"index" : node_index,
//				"ipaddress" : node_ipaddress,
//				"name" : node_name,
//				"ratio" : node_ratio,
//				"state" : node_state
//			};
//		}).get();		
//	},
});
