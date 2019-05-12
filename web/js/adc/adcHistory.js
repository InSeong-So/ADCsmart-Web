var AdcHistory = Class.create({
	initialize : function() 
	{
		var fn = this;
		this.searchedOption = 
		{
				"key" : undefined,
				"startTimeL" : undefined,
				"endTimeL" : undefined
		};
		this.searchStartTime = undefined;
		this.searchEndTime = undefined;
		this.virtualSvrIndex = undefined;
		this.logSeq = undefined;
		this.orderDir  = 2; //2는 내림차순
		this.orderType = 11;// 11은 occurTime
		this.searchFlag = false;
		this.pageNavi = new PageNavigator();
		this.pageNavi.onChange(function(fromRow, toRow, orderType, orderDir) 
		{
			FlowitUtil.log('fromRow: %s, toRow: %s, orderType: %s, orderDir: %s', fromRow, toRow, orderType, orderDir);
			fn.loadHistoryTableInListContent(fn.searchedOption, fromRow, toRow, orderType, orderDir);
		});		
	},
	onAdcChange : function() 
	{
		this.orderDir  = 2; //2는 내림차순
		this.orderType = 11;// 11은 occurTime
		var option = 
		{
			"key" :	undefined,
			"startTimeL" : this.searchStartTime,
			"endTimeL" : this.searchEndTime
		};
		this.loadListContent(option);
	},
	loadListContent : function(searchOption, orderType, orderDir) 
	{
		with (this) 
		{
			FlowitUtil.log("searchOption: %s, orderType: %s, orderDir: %s", searchOption, orderType, orderDir);
			if (!adcSetting.isAdcSet()) 
			{
				$('#wrap .contents').empty();
				return;
			}
			
			if(!validateDaterefresh())
			{
				return false;
			}
			
			FlowitUtil.log("index: %s", adcSetting.getAdc().index);
			var rowTotal = 0;
			ajaxManager.runJsonExt({
				url : "adc/history/retrieveHistoryListTotal.action",
				data : 
				{
					"adc.index" : adcSetting.getAdc().index,
					"adc.name" : adcSetting.getAdc().name,
					"adc.type" : adcSetting.getAdc().type,
					"searchKey" : searchOption ? searchOption.key : undefined,
					"startTimeL" : searchOption ? searchOption.startTimeL : undefined,
					"endTimeL" : searchOption ? searchOption.endTimeL : undefined	
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
					loadAdcHistoryListContent(searchOption);
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_HISTORY_LOAD, jqXhr);
				}
			});
		}
	},
	loadAdcHistoryListContent : function(searchOption, fromRow, toRow) 
	{
		with (this) 
		{
			FlowitUtil.log("searchOption:%o, fromRow:%s, toRow:%s", searchOption, fromRow, toRow);
			if (!adcSetting.isAdcSet()) 
			{
				$('#wrap .contents').empty();
				return;
			}
			
			ajaxManager.runHtmlExt({
				url : "adc/history/loadListContent.action",
				data : 
				{
					"adc.index" : adcSetting.getAdc().index,
					"adc.name" : adcSetting.getAdc().name,
					"adc.type" : adcSetting.getAdc().type,
					"fromRow" : fromRow === undefined ? pageNavi.getFirstRowOfCurrentPage() : fromRow,
					"toRow" : toRow === undefined ? pageNavi.getLastRowOfCurrentPage() : toRow,
					"searchKey" : searchOption ? searchOption.key : undefined,
					"startTimeL" : searchOption ? searchOption.startTimeL : undefined,
					"endTimeL" : searchOption ? searchOption.endTimeL : undefined,
					"orderDir" : this.orderDir,
					"orderType" : this.orderType
				},
				target: "#wrap .contents",
				successFn : function(params) 
				{
//					header.setActiveMenu('AdcHistory');
					header.setActiveMenu('SlbHistory');
					noticePageInfo();
					searchedOption = searchOption;
					_applyListContentCss();
					_registerListContentEvents();
				},
				completeFn : function() 
				{
					pageNavi.refresh();
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_HISTORY_LOAD, jqXhr);
				}
			});
		}
	},
	loadHistoryTableInListContent : function(searchOption, fromRow, toRow, orderType, orderDir) 
	{
		with (this) 
		{
			FlowitUtil.log("searchOption:%s, fromRow:%s, toRow:%s, orderType: %s, orderDir: %s", searchOption, fromRow, toRow, orderType, orderDir);
			ajaxManager.runHtmlExt({
				url : "adc/history/loadHistoryTableInListContent.action",
				data : 
				{
					"adc.index" : adcSetting.getAdc().index,
					"adc.name" : adcSetting.getAdc().name,
					"adc.type" : adcSetting.getAdc().type,
					"fromRow" : fromRow === undefined ? pageNavi.getFirstRowOfCurrentPage() : fromRow,
					"toRow" : toRow === undefined ? pageNavi.getLastRowOfCurrentPage() : toRow,
					"searchKey" : searchOption ? searchOption.key : undefined,
					"startTimeL" : searchOption ? searchOption.startTimeL : undefined,
					"endTimeL" : searchOption ? searchOption.endTimeL : undefined,
					"orderType" : this.orderType,
					"orderDir" : this.orderDir
							
				},
				target: "table.historyTable",
				successFn : function(params) 
				{
					noticePageInfo();
					searchedOption = searchOption;
					_applyListContentCss();
					_registerListContentEvents();
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_HISTORY_LOAD, jqXhr);
				}
			});
		}
	},
	_applyListContentCss : function() 
	{
		initTable([ "#table1 tbody tr" ], [ 1, 2, 3 ], [ -1 ]);
		$('#table1 tbody tr').hover(function() {
			$(this).css('background-color','').addClass('on');
		}, function() {
			if ($(this).index() % 2 == 0)
				$(this).css('background-color','rgb(243, 244, 247)');
				
			$(this).removeClass('on');
		});		
		
	},
/*	selectStatus : function() 
	{
		with (this) 
		{
			if (!orderType)	
			{ 
				orderType = '11'; 
			}
			if (!orderDir)		
			{ 
				orderDir = '2'; 
			}
		}
	},*/
	_registerListContentEvents : function() 
	{
		with (this) 
		{
			if(this.searchFlag == true)
			{
				$('.nulldataMsg').addClass("none");
				$('.dataNotExistMsg').addClass("none");
				if($('.history-tr').size() > 0)
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
				if($('.history-tr').size() > 0)
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
				if($('.history-tr').size() > 0)
				{
					$('.nulldataMsg').addClass("none");
				}
				else
				{
					$('.nulldataMsg').removeClass("none");
				}
			}			
			
			$('.historyDiffLnk').click(function(e) 
			{
				e.preventDefault();
				virtualSvrIndex = $(this).parents('tr').find('.virtualSvrIndex').text();
				logSeq = $(this).parents('tr').find('.logSeq').text();
				FlowitUtil.log('virtualSvrIndex: %s logSeq: %d', virtualSvrIndex, logSeq);
				loadDetail();
			});
			
			$('.orderDir_Desc').click(function(e)
			{
				e.preventDefault();
				orderDir =  $(this).find('.orderDir').text();
				orderType =  $(this).find('.orderType').text();
				var option = 
				{
					"key" :	$('.control_Board input.searchTxt').val(),
					"startTimeL" : searchStartTime,
					"endTimeL" : searchEndTime
				};		
				searchFlag=true;
				loadListContent(option , orderDir , orderType);
			});
			
			$('.orderDir_Asc').click(function(e)
			{
				e.preventDefault();
				orderDir =  $(this).find('.orderDir').text();
				orderType =  $(this).find('.orderType').text();
				var option = 
				{
					"key" :	$('.control_Board input.searchTxt').val(),
					"startTimeL" : searchStartTime,
					"endTimeL" : searchEndTime
				};			
				searchFlag=true;
				loadListContent(option , orderDir , orderType);
			});
			
			$('.orderDir_None').click(function(e)
			{
				e.preventDefault();
				orderDir =  $(this).find('.orderDir').text();
				orderType =  $(this).find('.orderType').text();
				var option = 
				{
					"key" :	$('.control_Board input.searchTxt').val(),
					"startTimeL" : searchStartTime,
					"endTimeL" : searchEndTime
				};			
				searchFlag=true;
				loadListContent(option , orderDir , orderType);
			});
			
			$('.settingLnk').click(function(e) 
			{
				FlowitUtil.log('settingLnk!');
				e.preventDefault();
				var virtualSvrIndex = $(this).parents('tr').find('.virtualSvrIndex').text();
				virtualServer.loadVsModifyContent(virtualSvrIndex);
//				adcSetting.setObjOnAdcChange(adcSetting);
				header.setActiveMenu('SlbSetting');
			});
			
			$('.monitorLnk').click(function(e) 
			{
				FlowitUtil.log('monitorLnk!');
				e.preventDefault();
//				monitorLoader.loadContent();
//				adcSetting.setObjOnAdcChange(monitorLoader);
				$('.monitorMnu').click();
				$('.monitorNetworkMnu').click();
			});
			
			$('.refreshLnk').click(function(e) 
			{
				e.preventDefault();
				if(!validateDaterefresh())
				{
					return false;
				}
				searchStartTime = undefined;
				searchEndTime = undefined;
				
				
				loadListContent();
			});
			
			// search event
			$('.control_Board a.searchLnk').click(function(e) 
			{
				e.preventDefault();
				var option = 
				{
//					"key" :	$('input[name="textfield3"]').val(),
					"key" : $('.control_Board input.searchTxt').val(),
					"startTimeL" : searchStartTime,
					"endTimeL" : searchEndTime
				};
				
				FlowitUtil.log('searchOption: %s', option);
				searchFlag=true;
				loadListContent(option);
			});
			
			$('.control_Board input.searchTxt').keydown(function(e) 
			{
				if (e.which != 13)
				{
					return;
				}
				
				var option = 
				{
					"key" :	$(this).val(),						
					"startTimeL" : searchStartTime,
					"endTimeL" : searchEndTime
				};
				
				FlowitUtil.log('searchOption: %s', option);
				searchFlag=true;
				loadListContent(option);
			});	
			
			// DatePicker 초기값 Setting
			if (null != searchStartTime) // 검색을 한번이라도 했을 경우
			{
				$('#reservationtime').val(new Date(searchStartTime).format('yyyy-mm-dd HH:MM')
						+" ~ "+ new Date(searchEndTime).format('yyyy-mm-dd HH:MM'));	
			}
			else // 검색을 한번도 하지않았을 경우
			{
				$('#reservationtime').val(moment().subtract(12, 'hour').format('YYYY-MM-DD HH:mm')
						+" ~ "+ moment().format('YYYY-MM-DD HH:mm'));
			}
			// 리뉴얼 DatePicker
			if(langCode=="ko_KR")
			{
				$('#reservationtime').daterangepicker({					
					ranges: {
				         '오늘': [moment().startOf('days'), moment()],
				         '최근 1일': [moment().subtract('days', 1), moment()],
				         '최근 7일': [moment().subtract('days', 7), moment()],
				         '최근 30일': [moment().subtract('days', 30), moment()],
				         '이번 달': [moment().startOf('month'), moment().endOf('month')],
				         '지난 달': [moment().subtract('month', 1).startOf('month'), moment().subtract('month', 1).endOf('month')]
				      },
				    startDate: moment().subtract(12, 'hour').format('YYYY-MM-DD HH:mm'),
					endDate: moment().format('YYYY-MM-DD HH:mm'),
				    opens: 'right', // 달력위치
	                timePicker: true,
	                timePickerIncrement: 30,
	                timePicker12Hour : false,
	                format: 'YYYY-MM-DD HH:mm'
	              }, function(start, end, label) {
//	            	  console.log(start.toISOString(), end.toISOString(), label);
	            	  searchStartTime = Number(start.format('x'));
	            	  searchEndTime = Number(end.format('x'));
	              });
			}
			else
			{
				// 리뉴얼 DatePicker
				$('#reservationtime').daterangepicker({					
					ranges: {
				         'Today': [moment().startOf('days'), moment()],
				         'Yesterday': [moment().subtract('days', 1), moment()],
				         'Last 7 Days': [moment().subtract('days', 7), moment()],
				         'Last 30 Days': [moment().subtract('days', 30), moment()],
				         'This Month': [moment().startOf('month'), moment().endOf('month')],
				         'Last Month': [moment().subtract('month', 1).startOf('month'), moment().subtract('month', 1).endOf('month')]
				      },
				    startDate: moment().subtract(12, 'hour').format('YYYY-MM-DD HH:mm'),
					endDate: moment().format('YYYY-MM-DD HH:mm'),
				    opens: 'right', // 달력위치
	                timePicker: true,
	                timePickerIncrement: 30,
	                timePicker12Hour : false,
	                format: 'YYYY-MM-DD HH:mm'
	              }, function(start, end, label) {
//	            	  console.log(start.toISOString(), end.toISOString(), label);
	            	  searchStartTime = Number(start.format('x'));
	            	  searchEndTime = Number(end.format('x'));
	              });
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
			
//			if(getValidateStringint(search, 1, 64) == false)
//			{
//				alert(VAR_FAULTSETTING_SPECIALCHAR);
//				$('.control_Board input.searchTxt').val('');
//				return false;
//			}			
		}	
		return true;
	},
	loadDetail : function() 
	{
		with (this) 
		{
			FlowitUtil.log("adc.index:%s, virtualSvrIndex:%s", adcSetting.getAdc().index, this.virtualSvrIndex);
			ajaxManager.runHtmlExt({
				url : "adc/history/loadHistoryDetail.action",
				data : 
				{
					"adc.index" : adcSetting.getAdc().index,
					"adc.name" : adcSetting.getAdc().name,
					"adc.type" : adcSetting.getAdc().type,
					"virtualSvrIndex" : this.virtualSvrIndex,
					"logSeq" :  this.logSeq
				},
				target: "#wrap .contents",
				successFn : function(params) 
				{
//					_applyCss();
					_registerEvents();
					_applyListContentCss();
					historyChange(logSeq);
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_HISTORY_LOAD, jqXhr);
				}	
			});
		}
	},
//	_applyCss : function() 
//	{
//		// 리스트형 테이블 초기화
//		$('.ipList').fixheadertable({
//			height : 125
//		});
//
////		tableHeadSize('#lastedMember', {
////		    'col' : '2',
////		    'hsize' : '5'
////		});
////		tableHeadSize('#orgMember', {
////		    'col' : '2',
////		    'hsize' : '5'
////		});
//		
////		$('#lastedMember, #orgMember').each(function() 
////		{
////			tableHeadSizeWith$table($(this), {
////			    'col' : $(this).hasClass('f5MemberInHistoryDetail') ? '3' : '2',
////			    'hsize' : '5'
////			});
////		});
//		
////		// 리스트형 테이블 행의 첫번째 열 배경색 지정
////		$('#orgMember > tbody > tr').each(function() 
////		{
////			$(this).children(":eq(0)").addClass('c1');
////			$(this).children(":eq(1)").addClass('c2');
////		});
////		$('#lastedMember > tbody > tr').each(function() 
////		{
////			$(this).children(":eq(0)").addClass('c1');
////			$(this).children(":eq(1)").addClass('c2');
////		});
////		
////		// 리스트 마지막 행 밑줄 적용
////		$('#orgMember > tbody > tr:last-child td').each(function() 
////		{
////			$(this).css('border-bottom', '1px solid #B5B5B5');
////		});
////		$('#lastedMember > tbody > tr:last-child td').each(function() 
////		{
////			$(this).css('border-bottom', '1px solid #B5B5B5');
////		});
//
//		// 리스트형 테이블 행 롤오버 효과
//		$('.ipList tbody tr:not(.empty)').hover(function() 
//		{
//			$(this).css({
//			    'background-color' : '#ccc',
//			    'cursor' : 'pointer'
//			});
//			$(this).children(":eq(1)").css('background-color', 'inherit');
//		}, function() {
//			$(this).removeAttr('style');
//			$(this).children(":eq(1)").css('background-color', '#e7e8ed');
//		});
//
//		// 테이블 컬럼 정렬
//		initTable([ "#lastedMember tr, #orgMember tr" ], [ 0, 1, 2 ], [ -1 ]);
//	},
	_registerEvents : function() 
	{
		with (this) 
		{
			$('.revertLnk').click(function(e) 
			{
				e.preventDefault();
				revertIfRevertable(this.virtualSvrIndex);
			});
			
			$('.adcHistoryLnk').click(function(e) 
			{				
				e.preventDefault();
				searchStartTime = undefined;
				searchEndTime = undefined;
				loadListContent();
			});
			$('#history tbody tr').click(function(e) 
			{
				e.preventDefault();
				virtualSvrIndex = $(this).find('.virtualSvrIndex').text();
				logSeq = $(this).find('.logSeq').text();
				FlowitUtil.log('virtualSvrIndex: %s logSeq: %d', virtualSvrIndex, logSeq);
				loadDetail();
			});
			
			if (adcSetting.getAdcStatus() != "available" || adcSetting.getAdc().mode == 1) 
			{
				$('.imgOff').removeClass("none");
				$('.imgOn').addClass("none");				
			}
			else
			{
				$('.imgOff').addClass("none");
				$('.imgOn').removeClass("none");
			}
		}
	},
	historyChange : function(logSeq)
	{
		$('#history tbody tr').removeClass("vsMonitorRowSelection");		
		$('#history tbody tr').each(function(index) {
			if ($(this).find('.logSeq').text() === logSeq) 
			{
				FlowitUtil.log("logSeq: %d", logSeq);
				$(this).addClass("vsMonitorRowSelection");
				return false;

			}
		});
	},
	revertIfRevertable : function(virtualSvrIndex) 
	{
		with (this) 
		{
			ajaxManager.runJsonExt({
				url : "adc/history/isRevertable.action",
				data : 
				{
					"adc.index" : adcSetting.getAdc().index,
					"adc.name" : adcSetting.getAdc().name,
					"adc.type" : adcSetting.getAdc().type,
					"virtualSvrIndex" : virtualSvrIndex,
					"logSeq" :  this.logSeq
				},
				successFn : function(data) 
				{
					if (!data.isSuccessful)
					{
						$.obAlertNotice(data.message);
					}
					else 
					{
						var chk = confirm(data.message);//'복구를 진행할 경우 이전 설정으로 변경됩니다. 복구를 진행하시겠습니까?'
						if(chk)
						{
							_revert(virtualSvrIndex);						
						}
					}
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_HISTORY_RECOVERINSPECTION, jqXhr);
				}	
			});
		}
	},
	_revert : function(virtualSvrIndex) 
	{
		with (this) 
		{
			ajaxManager.runJsonExt({
				url : "adc/history/revert.action",
				data : 
				{
					"adc.index" : adcSetting.getAdc().index,
					"adc.name" : adcSetting.getAdc().name,
					"adc.type" : adcSetting.getAdc().type,
					"virtualSvrIndex" : virtualSvrIndex
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
						
						ajaxManager.runJsonExt({
							url : "adc/history/checkPairIndex.action",
							data :
							{
								"adc.index" 			: adcSetting.getAdc().index,
								"adc.name" 				: adcSetting.getAdc().name,
								"adc.type" 				: adcSetting.getAdc().type,
								"lastLogHistoryIndex" 	: data.lastLogHistoryIndex,
								"virtualSvrIndex" 		: virtualSvrIndex
							},
							successFn : function(data)
							{
								if (data.pairIndex!='' && data.pairIndex!=null && data.pairIndex>0) 
								{
									var chk = confirm(data.message);
//									var chk = confirm("ADC Config 설정에 성공했습니다. \nADC 장비에 Peer 장비가 등록되어 있습니다. 동기화 하시겠습니까?");
									if (chk)
									{
										ajaxManager.runJsonExt({
											url : "adc/history/revertPeer.action",
											data : 
											{ 
												"adc.index" 			: adcSetting.getAdc().index,
												"adc.name" 				: adcSetting.getAdc().name,
												"adc.type" 				: adcSetting.getAdc().type,
												"pairIndex" 			: data.pairIndex,
												"lastLogHistoryIndex" 	: data.lastLogHistoryIndex,
												"virtualSvrIndex" 		: virtualSvrIndex
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
													$.obAlertNotice(data.message);
													loadListContent();
													return false;
												}
											},
											errorFn : function(jqXhr)
											{
												$.obAlertAjaxError(VAR_HISTORY_PEERSLBRECOVER, jqXhr);
												loadListContent();
											}
										});
										return false;
									}
								}
//								else
//								{
//									alert(data.message);
//								}
								loadListContent();
								return false;
							},
							errorFn : function(jqXhr)
							{
								$.obAlertAjaxError(VAR_HISTORY_PEERINFOEXTRACT, jqXhr);
							}	
						});
					}
				},
                errorFn : function(jqXhr)
                {
                	// #3984-9: 14.08.04 sw.jung 장비 설정 변경중 동기화 오류시 오류 메세지 개선
                	if (jqXhr.responseText.indexOf('Sync failed') > -1)
                		$.obAlertAjaxError(VAR_VS_VSTIMEERROR, jqXhr);
                	else if (jqXhr.responseText.indexOf('Illegal null') > -1)
                		$.obAlertAjaxError(VAR_VS_HASNULLVALUE, jqXhr);
                	else
                		$.obAlertAjaxError(VAR_HISTORY_SLBRECOVER, jqXhr);
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
