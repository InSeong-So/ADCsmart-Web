var AlertMonitoring = Class.create({
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
		this.orderDir  = 2; //2는 내림차순
		this.orderType = 33;// 11은 occurTime
		this.target = {};
		this.pageNavi = new PageNavigator();
		this.OBajaxManager = new OBAjax();
		this.pageNavi.onChange(function(fromRow, toRow, orderDir, orderType) {			
			fn.loadLogTableInListContent(fn.searchedOption, fromRow, toRow, orderDir, orderType);
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
	loadListContent : function(searchOption, orderDir, orderType, flag)
	{
		with (this)
		{
			if (flag == undefined) {
				setTarget();
			}
			else{
				this.target.categoryIndex = 0;
				this.target.targetIndex = undefined;
			}			
			if(!validateDaterefresh())
			{
				return false;
			}					
			var rowTotal = 0;
			ajaxManager.runJson({
				url : "monitor/retrieveAlertLogCount.action",
				data :
				{
					"adcObject.category" : target.categoryIndex,
					"adcObject.index" : target.targetIndex,
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
					pageNavi.updateRowTotal(rowTotal, orderDir);
					noticePageInfo();
					loadFaultLogListContent(searchOption);
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_FAULT_LOGCOUNTEXTRACT, jqXhr);
				}
			});
		}
	},
	loadFaultLogListContent : function(searchOption, fromRow, toRow)
	{
		with (this)
		{
			ajaxManager.runHtml({
				url : "monitor/loadAlertMonitorContent.action",
				data :
				{
					"adcObject.category" : target.categoryIndex,
					"adcObject.index" : target.targetIndex,
					"beginIndex" : fromRow === undefined ? pageNavi.getFirstRowOfCurrentPage() : fromRow,
					"endIndex" : toRow === undefined ? pageNavi.getLastRowOfCurrentPage() : toRow,
					"searchKey" : searchOption ? searchOption.key : undefined,
					"startTimeL" : searchOption ? searchOption.startTimeL : undefined,
					"endTimeL" : searchOption ? searchOption.endTimeL : undefined,					
					"orderOption.orderDirection" : this.orderDir,
					"orderOption.orderType" : this.orderType
				},
				target: "#wrap .contents",
				successFn : function(params)
				{
					_modifyUserAlertTime();
					header.setActiveMenu('MonitorAlert');
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
					$.obAlertAjaxError(VAR_FAULT_LOGDATALOAD, jqXhr);
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
				url = "monitor/loadAlertTableInListContent.action";
			}
			else
			{
				url = "monitor/loadAlertTableInListGroupContent.action";
			}
			ajaxManager.runHtml({
				url : url,
				data :
				{
					"adcObject.category" : target.categoryIndex,
					"adcObject.index" : target.targetIndex,
					"beginIndex" : fromRow === undefined ? pageNavi.getFirstRowOfCurrentPage() : fromRow,
					"endIndex" : toRow === undefined ? pageNavi.getLastRowOfCurrentPage() : toRow,
					"searchKey" : searchOption ? searchOption.key : undefined,
					"startTimeL" : searchOption ? searchOption.startTimeL : undefined,
					"endTimeL" : searchOption ? searchOption.endTimeL : undefined,					
					"orderOption.orderDirection" : this.orderDir,
					"orderOption.orderType" : this.orderType	
				},
				target: "table.alertLogTable",
				successFn : function(params)
				{
					_modifyUserAlertTime();
					noticePageInfo();
					searchedOption = searchOption;	
					_registerListContentEvents();
//					noticeNameFill();

				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_FAULT_LOGDATALOAD, jqXhr);
				}
			});
		}
	},
	_loadDetailPopupContent : function(popupContent)
	{
		with(this)
		{
			showPopup({
				'id' : '#alertlog-popup',
				'width' : '800px',
				'height' : '340px'
			});
			
			var adcNameArea = $(".adcname-area").filter(':last');
			var occurTimeArea = $(".occurtime-area").filter(':last');
			var alertTimeArea = $(".alerttime-area").filter(':last');
			var typeArea = $(".type-area").filter(':last');
			var actionArea = $(".action-area").filter(':last');
			var eventArea = $(".event-area").filter(':last');
			
			var adcName = "";
			if (popupContent.adcName == -1)
			{
				adcName = adcSetting.adc.name;
			}
			else
			{
				adcName = popupContent.adcName;
			}
			
			var typeHtml = "";
			if (popupContent.type == 0)
			{
				if(popupContent.status == 1)
				{
					typeHtml += '<img src="/imgs/icon/icon_critical_none.gif" alt="' +VAR_FAULT_FAULT_RESERVE + '">';
					typeHtml += VAR_FAULT_FAULT_RESERVE;
				}
				else if(popupContent.status == 0)
				{
					typeHtml += '<img src="/imgs/icon/icon_critical.gif" alt="' +VAR_FAULT_FAULT_OCCUR+ '">';
					typeHtml += VAR_FAULT_FAULT_OCCUR;
				}
				else{}
			}
			else if (popupContent.type == 1)
			{
				typeHtml += '<img src="/imgs/icon/icon_warning.gif" alt="' +VAR_FAULT_WARNING+'">';
				typeHtml += VAR_FAULT_WARNING;
			}
			else
			{				
			}
			
			var actionHtml = "";
			if(popupContent.action == 1)
			{
				actionHtml += '<img src="/imgs/icon/icon_action_syslog.png" alt="syslog">';
				actionHtml += " syslog";
			}
			else
			{				
				actionHtml += "-";
			}
			
			adcNameArea.empty().html(adcName);
			alertTimeArea.empty().html(popupContent.alertTime);
			occurTimeArea.empty().html(popupContent.occurTime);
			typeArea.empty().html(typeHtml);
			actionArea.empty().html(actionHtml);			
			eventArea.empty().html(popupContent.eventText);
			
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
			
			$('.popuplink').click(function (e)
			{
				e.preventDefault();
				var popupContent = {
					occurTime : $(this).parent().parent().find(".alert-occurtime").val(),
					alertTime : $(this).parent().parent().find(".alert-alerttime").val(),
					type : $(this).parent().parent().find(".alert-type").val(),
					status : $(this).parent().parent().find(".alert-status").val(),
					action : $(this).parent().parent().find(".alert-actionsyslog").val(),
					adcName : $(this).parent().parent().find(".alert-adcname").val(),
					eventText : $(this).parent().parent().find(".alert-event").val()					
				};
				_loadDetailPopupContent(popupContent);			
			});
			
			$('.adcMonitoringLnk').on('click', function(e)
			{
				e.preventDefault();
				with (this) 
				{
					var adcIndex = $(this).children('span').text();
					adcSetting._selectAdc(adcIndex);
					adcSetting.setSelectIndex(2);	
										
					monitorAppliance.loadApplianceMonitorContent(adcSetting.getAdc());
				}
			});
			
			$('#refreshLnk').click(function(e)
			{				
				e.preventDefault();
				searchStartTime = undefined;
				searchEndTime = undefined;
				loadListContent();
			});
			
			// search event
			$('.control_Board a.searchLnk').click(function (e)
			{
				e.preventDefault();				
				var option = {
					"key" :	$('.control_Board input.searchTxt').val(),
					"startTimeL" : searchStartTime,
					"endTimeL" : searchEndTime
				};				
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
					"key" :	$('.control_Board input.searchTxt').val(),
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
			
			if(this.searchFlag == true)
			{
				$('.nulldataMsg').addClass("none");
				$('.dataNotExistMsg').addClass("none");
				if($('.adcAlertLogList').size() > 0)
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
				if($('.adcAlertLogList').size() > 0)
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
					if($('.adcAlertLogList').size() > 0)
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
			/*if ($('.adcAlertLogList').size() > 0 )
			{
				$('.disabledChk').removeClass("none");	// 네비게이션 보이기
				$('.nulldataMsg').addClass("none");		// 데이터표시할수없습니다 숨기기
				$('.refeshImgOn').removeClass("none");	// Refrsh 버튼 보이기
				$('.refeshImgOff').addClass("none");	// Refrsh 비활성화 버튼 숨기기
			}
			else
			{
				$('.disabledChk').addClass("none");	// 네비게이션 숨기기
				$('.nulldataMsg').removeClass("none");	// 데이터표시할수없습니다 보이기
				$('input[name="searchKey"]').attr("disabled", "disabled");	// searchkey 숨기기
				$('input[name="startTimeL"]').attr("disabled", "disabled");	// 달력 perid 숨기기
				$('input[name="endTimeL"]').attr("disabled", "disabled");	// 달력 perid 숨기기
				$('.imageChange').attr("src", "imgs/meun/btn_search_1_off.png");	// 서치버튼 비활성화
				$('.ui-datepicker-trigger').attr("src", "imgs/meun/btn_calendar_off.png");// 달력버튼 비활성화
				$('.refeshImgOff').removeClass("none");// Refrsh 비활성화 버튼 보이기
				$('.refeshImgOn').addClass("none");	// Refrsh 버튼 숨기기
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
	_modifyUserAlertTime : function()
	{
		with(this)
		{
			OBajaxManager.runJson({
				url : "/alert/modifyUserAlertTime.action",
				data : 
				{
				},
				successFn : function(data) 
				{					
				}
			});
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
			

		}		
		
		return true;
	},
	exportCss : function()
	{
		with (this)
		{
			if (!adcSetting.isAdcSet())
			{
				return;
			}

			var url = "faultLog/downloadFaultLog.action?adcIndex=" + adcSetting.getAdc().index;
			if (searchedOption.key)
				url += "&searchKey=" + encodeURIComponent(searchedOption.key);
			
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