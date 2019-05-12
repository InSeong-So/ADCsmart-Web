var AdcLog = Class.create({
	initialize : function() 
	{
		var fn = this;
		this.searchedOption = 
		{
			"searchKey" : undefined,
			"startTimeL" : undefined,
			"endTimeL" : undefined
		};
		this.searchStartTime = undefined;
		this.searchEndTime = undefined;
		this.orderDir  = 2; //내림차순 = 2
		this.orderType = 11;//occurTime = 11
		this.target = {};
		this.extraContentKey = 0;	// 0 = Eng , 1 = Kor
		this.selectedLogType = 0;	// 0 = all, 1 = ADC, 2 = ADCsmart
		this.selectedLogLevel = "all"; // "CRIT" "WARNING" "DEBUG" "EMERG" "ERROR" "ALERT" "AlteonOS" "NOTICE" "INFO" "ERR"" +
		this.pageNavi = new PageNavigator();
		this.pageNavi.onChange(function(fromRow, toRow, orderType, orderDir) 
		{
			fn.loadLogTableInListContent(fn.searchedOption, fromRow, toRow, orderType, orderDir);
		});
		this.searchFlag = false;
	},
	setTarget : function()
	{
		var GroupIndex = adcSetting.getGroupIndex();
		var adcIndex = adcSetting.adc.index;
		if(GroupIndex === undefined && adcIndex === undefined)
		{
			this.target.categoryIndex = 0;
			this.target.targetIndex = undefined;
		}
		else if(GroupIndex == 0)
		{
			this.target.categoryIndex = GroupIndex;
			this.target.targetIndex = undefined;
		}
		else if(GroupIndex === undefined && adcIndex != null)
		{
			this.target.categoryIndex = 2;			
			this.target.targetIndex = adcIndex;			
		}
		else
		{
			this.target.categoryIndex = 1;
			this.target.targetIndex = GroupIndex;
		}
	},
	setExtraContentKey : function(key)
	{
		this.extraContentKey = key;
	},

	setSelectedLogType : function(logType)
	{
		this.selectedLogType = logType;
	},
	
	setSelectedLogLevel : function(logLevel)
	{
		this.selectedLogLevel = logLevel;
	},
	
	onAdcChange : function() 
	{
		this.orderType = 11;//occurTime = 11
		this.orderDir  = 2; //내림차순 = 2
		var option = 
		{
			"searchKey" : undefined,
			"startTimeL" : this.searchStartTime,
			"endTimeL" : this.searchEndTime
		};
		this.loadListContent(option);
	},

	loadListContent : function(searchOption, orderType, orderDir) 
	{
		with (this) 
		{
			setTarget();
			if(!validateDaterefresh())
			{
				return false;
			}		
			var rowTotal = 0;
			ajaxManager.runJsonExt({				
				url : "logAnalysis/retrieveAdcLogTotal.action",
				data : 
				{
					"adcObject.category" : target.categoryIndex,
					"adcObject.index" : target.targetIndex,					
					"searchKey" : searchOption ? searchOption.searchKey : undefined,
					"startTimeL" : searchOption ? searchOption.startTimeL : undefined,
					"endTimeL" : searchOption ? searchOption.endTimeL : undefined,
					"selectOption.logType" : this.selectedLogType,
					"selectOption.logLevel" : this.selectedLogLevel
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
					noticePageInfo();
					loadAdcLogListContent(searchOption);
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_LOGANALYSIS_EXTRACT, jqXhr);
				}	
			});
		}
	},
	loadAdcLogListContent : function(searchOption, fromRow, toRow) 
	{
		with (this) 
		{		
			ajaxManager.runHtmlExt({
				url : "logAnalysis/loadAdcLogListContent.action",
				data : 
				{
					"adcObject.category" : target.categoryIndex,
					"adcObject.index" : target.targetIndex,					
					"fromRow" : fromRow === undefined ? pageNavi.getFirstRowOfCurrentPage() : fromRow,
					"toRow" : toRow === undefined ? pageNavi.getLastRowOfCurrentPage() : toRow,
					"searchKey" : searchOption ? searchOption.searchKey : undefined,
					"startTimeL" : searchOption ? searchOption.startTimeL : undefined,
					"endTimeL" : searchOption ? searchOption.endTimeL : undefined,
					"orderOption.orderType" : this.orderType,
					"orderOption.orderDirection" : this.orderDir,
					"extraContentKey" : this.extraContentKey,
					"selectOption.logType" : this.selectedLogType,
					"selectOption.logLevel" : this.selectedLogLevel
				},
				target: "#wrap .contents",
				successFn : function(params) 
				{
					header.setActiveMenu('AdcLog');
					noticePageInfo();
					searchedOption = searchOption;
					_registerListContentEvents();
//					noticeNameFill();
				},
				completeFn : function() 
				{
					pageNavi.refresh();
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_LOGANALYSIS_EXTRACT, jqXhr);
				}				
			});
		}
	},	
	loadLogTableInListContent : function(searchOption, fromRow, toRow, orderType, orderDir) 
	{		
		with (this) 
		{
			var url = undefined;
			if (target.categoryIndex == 2)
			{
				url = "logAnalysis/loadLogTableInAdcLogListContent.action";
			}
			else
			{
				url = "logAnalysis/loadLogTableInGroupLogListContent.action";
			}
			ajaxManager.runHtmlExt({
				url : url,
				data : 
				{
					"adcObject.category" : target.categoryIndex,
					"adcObject.index" : target.targetIndex,
					"fromRow" : fromRow === undefined ? pageNavi.getFirstRowOfCurrentPage() : fromRow,
					"toRow" : toRow === undefined ? pageNavi.getLastRowOfCurrentPage() : toRow,
					"searchKey" : searchOption ? searchOption.searchKey : undefined,
					"startTimeL" : searchOption ? searchOption.startTimeL : undefined,
					"endTimeL" : searchOption ? searchOption.endTimeL : undefined,
					"orderOption.orderType" : this.orderType,
					"orderOption.orderDirection" : this.orderDir,
					"extraContentKey" : this.extraContentKey,
					"selectOption.logType" : this.selectedLogType,
					"selectOption.logLevel" : this.selectedLogLevel
				},
				target: "table.Board",
				successFn : function(params) 
				{
					if (extraContentKey == 1)
					{
						$(".change-content").attr('checked', 'checked');
					}
					noticePageInfo();
					searchedOption = searchOption;
					_registerListContentEvents();
//					noticeNameFill();
				},
                errorFn : function(jqXhr)
                {
                	$.obAlertAjaxError(VAR_LOGANALYSIS_EXTRACT, jqXhr);
                }
			});
		}
	},
	_loadDetailPopupContent : function(popupContent)
	{
		with(this)
		{
			showPopup({
				'id' : '#adclog-popup',
				'width' : '800px',
				'height' : '340px'
			});
			
			var adcNameArea = $(".adcname-area").filter(':last');
			var occurTimeArea = $(".occurtime-area").filter(':last');			
			var levelArea = $(".level-area").filter(':last');
			var contentArea = $(".content-area").filter(':last');			
			
			var adcName = "";
			if (popupContent.adcName == -1)
			{
				adcName = adcSetting.adc.name;
			}
			else
			{
				adcName = popupContent.adcName;
			}		
			
			occurTimeArea.empty().html(popupContent.occurTime);
			adcNameArea.empty().html(adcName);			
			levelArea.empty().html(popupContent.level);			
			contentArea.empty().html("<pre>" + popupContent.content + "</pre>");
			
			$('.popupclosebtn').click(function(e)
			{
				e.preventDefault();
				$('.popup_type1').remove();
				$('.cloneDiv').remove();
			});
			
		}
	},
	_registerListContentEvents : function() 
	{
		with (this) 
		{
			$('.msg_log').addClass('none');
			if(pageNavi.getRowTotal() >= 10000)
			{
				$('.msg_log').removeClass('none');
			}
			
			$('.logtype-selecte').change(function (e)
			{
				e.preventDefault();
				setSelectedLogType($(this).val());
				loadListContent(searchedOption);
			});
			
			$('.loglevel-selecte').change(function (e)
			{
				e.preventDefault();
				setSelectedLogLevel($(this).val());
				loadListContent(searchedOption);
			});		
			
			$('.popuplink').click(function (e)
			{
				e.preventDefault();
				var popupContent = {
					occurTime : $(this).parent().parent().find(".adclog-occurtime").val(),
					adcName : $(this).parent().parent().find(".adclog-adcname").val(),
					logType : $(this).parent().parent().find(".adclog-type").val(),
					level : $(this).parent().parent().find(".adclog-loglevel").val(),
					content : $(this).parent().parent().find(".adclog-content").val()									
				};
				_loadDetailPopupContent(popupContent);			
			});
			
			$('.change-content').change(function (e)
			{
				e.preventDefault();
				var isChecked = $(this).is(':checked');
				var key = undefined;
				if (isChecked == true)
				{
					key = 1;
				}
				else if (isChecked == false)
				{
					key = 0;
				}
				setExtraContentKey(key);
				loadLogTableInListContent(searchedOption);
			});
			
			$('.orderDir_Desc').click(function(e)
			{
				e.preventDefault();
				orderDir =  $(this).find('.orderDir').text();
				orderType =  $(this).find('.orderType').text();
				var option = 
				{
					"searchKey" :	$('.control_Board input.searchTxt').val(),
					"startTimeL" : searchStartTime,
					"endTimeL" : searchEndTime
				};				
				searchFlag=true;
				loadListContent(option , orderType , orderDir);				
			});
			
			$('.orderDir_Asc').click(function(e)
			{
				e.preventDefault();
				orderDir =  $(this).find('.orderDir').text();
				orderType =  $(this).find('.orderType').text();
				var option = 
				{
					"searchKey" :	$('.control_Board input.searchTxt').val(),
					"startTimeL" : searchStartTime,
					"endTimeL" : searchEndTime
				};
				searchFlag=true;
				loadListContent(option , orderType , orderDir);
			});
			
			$('.orderDir_None').click(function(e)
			{
				e.preventDefault();
				orderDir =  $(this).find('.orderDir').text();
				orderType =  $(this).find('.orderType').text();
				var option = 
				{
					"searchKey" :	$('.control_Board input.searchTxt').val(),
					"startTimeL" : searchStartTime,
					"endTimeL" : searchEndTime
				};			
				searchFlag=true;
				loadListContent(option , orderType , orderDir);
			});

			$('.exportCssLnk').click(function(e) 
			{
				e.preventDefault();	
				var option = 
				{
					"searchKey" :	$('.control_Board input.searchTxt').val(),
					"startTimeL" : searchStartTime,
					"endTimeL" : searchEndTime
				};		
				_checkExportAdcDataExist(option);
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
				selectedLogType = 0;
				selectedLogLevel = "all";
				loadListContent();
			});
			
			// search event
			$('.control_Board a.searchLnk').click(function(e) 
			{
				var option = 
				{
					"searchKey" :	$('.control_Board input.searchTxt').val(),
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
					"searchKey" :	$(this).val(),
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
			
			if(this.searchFlag == true)
			{
				$('.nulldataMsg').addClass("none");
				$('.dataNotExistMsg').addClass("none");
				if($('.adcLogList').size() > 0)
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
				if($('.adcLogList').size() > 0)
				{
					$('.dataNotExistMsg').addClass("none");
				}
				else
				{
					$('.dataNotExistMsg').removeClass("none");
				}
			}

			if (adcSetting.getSelectIndex() == 2)
			{
				if (adcSetting.getAdcStatus() != "available") 
				{
					$('.searchNotMsg').addClass("none");
					$('.dataNotExistMsg').addClass("none");
					if($('.adcLogList').size() > 0)
					{
						$('.nulldataMsg').addClass("none");
					}
					else
					{
						$('.nulldataMsg').removeClass("none");
					}
				}
			}
			
			
			// 비활성화 기능 개선 예정
			/*if ($('.adcLogList').size() > 0 )
			{
				$('.disabledChk').removeClass("none");	// 네비게이션 보이기
				$('.nulldataMsg').addClass("none");		// 데이터표시할수없습니다 숨기기
				$('.refeshImgOn').removeClass("none");	// Refrsh 버튼 보이기
				$('.refeshImgOff').addClass("none");	// Refrsh 비활성화 버튼 숨기기
				$('.imgOff').addClass("none");
				$('.imgOn').removeClass("none");				
				
			}
			else
			{
				$('.imgOff').removeClass("none");
				$('.imgOn').addClass("none");
				$('.disabledChk').addClass("none");	// 네비게이션 숨기기
				$('.nulldataMsg').removeClass("none");	// 데이터표시할수없습니다 보이기
				$('input[name="searchKey"]').attr("disabled", "disabled");	// searchkey 숨기기
				$('input[name="startTimeL"]').attr("disabled", "disabled");	// 달력 perid 숨기기
				$('input[name="endTimeL"]').attr("disabled", "disabled");	// 달력 perid 숨기기
				$('.imageChange').attr("src", "imgs/meun/btn_search_1_off.png");	// 서치버튼 비활성화
				$('.ui-datepicker-trigger').attr("src", "imgs/meun/btn_calendar_off.png");// 달력버튼 비활성화
				$('.refeshImgOff').removeClass("none");// Refrsh 비활성화 버튼 보이기
				$('.refeshImgOn').addClass("none");	// Refrsh 버튼 숨기기
				$('.loglevel-selecte').attr("disabled", "disabled");
			}*/
		}
	},
	noticeNameFill : function()
	{
		with(this)
		{
			var titleHeader = $('.title-name-area').filter(':last');
			if (target.categoryIndex == 0)
			{
				titleHeader.empty();
				titleHeader.html("전체 ADC");				
			}
			else if (target.categoryIndex == 1)
			{
				titleHeader.empty();
				titleHeader.html(adcSetting.getGroupName());
			}
			else
			{
				titleHeader.empty();				
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
	_checkExportAdcDataExist : function(searchOption)
	{
		with(this)
		{
			
			ajaxManager.runJsonExt
			({
				url : "logAnalysis/checkAdcDataExist.action",
				data : 
				{
					"adcObject.category" : target.categoryIndex,
					"adcObject.index" : target.targetIndex,					
					"searchKey" : searchOption ? searchOption.searchKey : undefined,
					"startTimeL" : searchOption ? searchOption.startTimeL : undefined,
					"endTimeL" : searchOption ? searchOption.endTimeL : undefined,
					"extraContentKey" : this.extraContentKey,
					"selectOption.logType" : this.selectedLogType,
					"selectOption.logLevel" : this.selectedLogLevel
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
			var params = "adcObject.category=" + target.categoryIndex + "&adcObject.index=" + target.targetIndex + "&startTimeL=" + searchStartTime
			+ "&endTimeL=" + searchEndTime + "&searchKey=" + $('.control_Board input.searchTxt').val()
			+ "&extraContentKey=" + this.extraContentKey + "&selectOption.logType=" + this.selectedLogType + "&selectOption.logLevel=" + this.selectedLogLevel;		
			var url = "logAnalysis/downloadAdcLog.action?" + params;
			$('#downloadFrame').attr('src', url);
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