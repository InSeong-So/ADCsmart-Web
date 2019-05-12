var FaultHistory = Class.create({
	initialize : function()
	{
		var fn = this;
		this.searchedOption = 
		{
			"key" : undefined,
			"startTimeL" : undefined,
			"endTimeL" : undefined
		};	
		this.adc = {};
//		this.adc;
		this.categoryIndex;
		this.logKey = undefined;
		this.refreshTimer = undefined;
		this.refreshIntervalSeconds=10;  //진행중 경우 10초마다 refresh 
		this.curCategory=undefined; //0: 전체, 1: 그룹, 2: 개별 adc, 3:virtual server, 4:virtual service
		this.orderDir  = 2; //2 :  내림차순
		this.orderType = 33;// 33 : occurTime
		this.scheduleIndex = undefined;
		this.templateIndex = undefined;
		this.selectedIndex = undefined;
		this.scheduleType = 0;
		this.adcType = undefined;
		this.searchStartTime = undefined;
		this.searchEndTime = undefined;
		this.searchFlag = false;
		this.autoRefresh = false;
		this.pageNavi = new PageNavigator();
		this.pageNavi.onChange(function(fromRow, toRow, orderType, orderDir) 
		{
			fn.loadHistoryTableInListContent(fn.searchedOption, fromRow, toRow, orderType, orderDir);
		});			
	},
	getAdcType : function() 
	{
		return this.adcType;
	},
	setAdcType : function(adcType) 
	{
		this.adcType = adcType;
	},
	onAdcChange : function() 
	{
		this.orderDir  = 2; 	// 2 :  내림차순
		this.orderType = 33;	// 33 : occurTime
		var option = 
		{
			"key" :	undefined,
			"startTimeL" : this.searchStartTime,
			"endTimeL" : this.searchEndTime
		};
		this.loadListContent(option);
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
	loadListContent : function(searchOption, orderDir, orderType)
	{
		with (this)
		{	
			if(!validateDaterefresh())
			{
				return false;
			}
			var rowTotal = 0;
			ajaxManager.runJsonExt({
				url : "faultHistory/retrieveFaultHistoryTotal.action",
				data :
				{
					"adcObject.category"	: adcSetting.getSelectIndex(),
					"adcObject.index" 		: getIndex(adcSetting.getSelectIndex()),
					"adcObject.name"		: adcSetting.getGroupName(),
					"adc.type" 				: adcSetting.getAdc().type,
					"adc.name" 				: adcSetting.getAdc().name,
					"searchKey" 			: searchOption ? searchOption.key : undefined,
					"startTimeL" 			: searchOption ? searchOption.startTimeL : undefined,
					"endTimeL" 				: searchOption ? searchOption.endTimeL : undefined
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
					loadFaultListContent(searchOption);	
				},
				errorFn : function(jqXhr)
				{				
					$.obAlertAjaxError(VAR_FAULTHISTORY_COUNTEXTRACT, jqXhr);
				}	
			});
		}
	},
	loadFaultListContent : function(searchOption, fromRow, toRow)
	{
		with (this)
		{			
			ajaxManager.runHtmlExt({
				url : "faultHistory/loadFaultListContent.action",
				target : "#wrap .contents",
				data : 
				{
					"adcObject.category"	: adcSetting.getSelectIndex(),
					"adcObject.index" 		: getIndex(adcSetting.getSelectIndex()),
					"adcObject.name"		: adcSetting.getGroupName(),
					"adc.type" : adcSetting.getAdc().type,
					"adc.name" : adcSetting.getAdc().name,
					"searchKey" : searchOption ? searchOption.key : undefined,
					"startTimeL" : searchOption ? searchOption.startTimeL : undefined,
					"endTimeL" : searchOption ? searchOption.endTimeL : undefined,
					"fromRow" : fromRow === undefined ? pageNavi.getFirstRowOfCurrentPage() : fromRow,
					"toRow" : toRow === undefined ? pageNavi.getLastRowOfCurrentPage() : toRow,
					"orderObj.orderDirection" : orderDir,
					"orderObj.orderType" : orderType
				},
				successFn : function(params)
				{
					header.setActiveMenu('FaultHistory');
					noticePageInfo();
					searchedOption = searchOption;
					registerListContentsEvents();						
				},
				completeFn : function()
				{
					pageNavi.refresh();
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_FAULTHISTORY_LOAD, jqXhr);
				}
			});
		}
	},
	loadHistoryTableInListContent : function(searchOption, fromRow, toRow, orderType, orderDir) 
	{			
		with (this)
		{
			ajaxManager.runHtmlExt({
				url : "faultHistory/loadHistoryTableInListContent.action",
				target : "table.historyTable",
				data : 
				{
					"adcObject.category"	: adcSetting.getSelectIndex(),
					"adcObject.index" 		: getIndex(adcSetting.getSelectIndex()),
					"adcObject.name"		: adcSetting.getGroupName(),
					"adc.type" : adcSetting.getAdc().type,
					"adc.name" : adcSetting.getAdc().name,
					"searchKey" : searchOption ? searchOption.key : undefined,
					"startTimeL" : searchOption ? searchOption.startTimeL : undefined,
					"endTimeL" : searchOption ? searchOption.endTimeL : undefined,
					"fromRow" : fromRow === undefined ? pageNavi.getFirstRowOfCurrentPage() : fromRow,
					"toRow" : toRow === undefined ? pageNavi.getLastRowOfCurrentPage() : toRow,
					"orderObj.orderDirection" : orderDir,
					"orderObj.orderType" : orderType
				},
				successFn : function(params)
				{
					noticePageInfo();
					searchedOption = searchOption;
					registerListContentsEvents();
				},
				errorFn : function(jqXhr)
				{
//					exceptionEvent();
					$.obAlertAjaxError(VAR_FAULTHISTORY_LOAD, jqXhr);
				}
			});
		}
	},
	delHistorys : function(historyIndices)
	{
		with(this)
		{
			ajaxManager.runJsonExt({
				url : "faultHistory/delHistorys.action",
				data : 
				{
					"historyIndices" : historyIndices,
				},
				successFn : function(data) 
				{
					if (!data.isSuccessful)
					{
						$.obAlertNotice(data.message);
						return;
					}					
					loadListContent();
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_FAULTHISTORY_DELFAIL, jqXhr);
				}
			});
		}	
	},	
	loadScheduleInfo : function(hour, minute, week, month, day)
	{
		this.adc = adcSetting.getAdc();
		this.categoryIndex = adcSetting.getSelectIndex();	
				
		with (this)
		{			
			ajaxManager.runJsonExt({
				url : "faultHistory/loadScheduleInfo.action",
				target : "#wrap .contents",
				data : 
				{
					"adcObject.category"	: categoryIndex,
					"adcObject.index" 		: getIndex(adcSetting.getSelectIndex()),
					"adcObject.name"		: adcSetting.getGroupName(),
					"adc.type" 				: adc.type,
					"adc.name" 				: adc.name,
					"scheduleIndex" 		: scheduleIndex,
					"templateIndex"			: templateIndex,
					"scheduleType"			: scheduleType
				},
				successFn : function(data) 
				{
					registerListContentsEvents();
					faultSetting.loadFaultSchduleContent(templateIndex, scheduleType, hour, minute, week, month, day);				
				},
				
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_FAULTHISTORY_SCHEDULELOAD, jqXhr);
				}
			});
		}
	},
	
	delScheduleInfo : function()
	{
		this.adc = adcSetting.getAdc();
		this.categoryIndex = adcSetting.getSelectIndex();	
		
		if (adcSetting.getSelectIndex() == 1)
		{
			selectedIndex = adcSetting.getGroupIndex();
		}
		else if (adcSetting.getSelectIndex() == 2)
		{
			selectedIndex = adcSetting.getAdc().index;
		}
		else
		{
			selectedIndex = 0;
		}
		
		with (this)
		{			
			ajaxManager.runJsonExt({
				url : "faultHistory/deleteFaultScheduleInfo.action",
				target : "#wrap .contents",
				data : 
				{
					"adcObject.category"	: categoryIndex,
					"adcObject.index" 		: selectedIndex,
					"adcObject.name"		: adcSetting.getGroupName(),
					"adc.type" 				: adc.type,
					"adc.name" 				: adc.name,
					"scheduleIndex" 		: scheduleIndex			
				},				
				successFn : function(data) 
				{
					if (!data.isSuccessful)
					{
						$.obAlertNotice(data.message);
						return;
					}
					loadListContent();						
				},
				
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_FAULTHISTORY_SCHEDULEDEL, jqXhr);
				}
			});
		}
	},	
	groupAdcStatusCheck : function(a)
	{
		var flag = a;
		var checkAdcStatus='';
		var status;
		var temp=0;
		
		if(flag == 0)
		{
			checkAdcStatus = $('.depth1').find('ul .adcStatus'); // 현재 모든 adcStatus 값이 담긴 span 리스트
			
		}
		else if(flag == 1)
		{
			checkAdcStatus = $('.depth1 li:has(.groupBlock.on)').find('ul .adcStatus'); // 현재 선택된 그룹의 adcStatus 값이 담긴 span 리스트
		}
		
		status = checkAdcStatus.map(function(){return $(this).text();}); //checkAdcStatus 저장되어 있는 span의 text 값 리스트.
		
		for(var i=0; i<status.length; i++) // status에 저장되어 있는 text 값 순회하면서 비교
		{				 
			if(status[i] == "disable")
			{
				temp=temp+1;
			}
		}
		
		if(temp==status.length)
			return false;
		
		return true;
	},
	groupFaultBlockCheck : function()
	{
		var checkAdcType;
		var type;
		if($('.depth0 li:has(.allBlock.on)').length > 0) // 전체 진단 선택시 전체의 벤더 확인. 같은 벤더면 true, 아니면 false
		{
			checkAdcType = $('.depth1').find('ul .adcType'); // 현재 모든 adcType 값이 담긴 span 리스트
			
		}
		else if($('.depth1 li:has(.groupBlock.on)').length > 0) //그룹 진단 선택시 그룹의 벤더 확인. 같은 벤더면 true, 아니면 false 
		{
			checkAdcType = $('.depth1 li:has(.groupBlock.on)').find('ul .adcType'); // 현재 선택 된 그룹의 adcType 값이 담긴 span 리스트
		}
		else
		{
			return true;
		}
		
		type = checkAdcType.map(function(){return $(this).text();}); //checkAdcType에 저장되어 있는 span의 text 값 리스트.
		
		for(var i=0; i<type.length; i++) // type에 저장되어 있는 text 값 순회하면서 비교
		{				 
			if(i>0)
			{
				if(type[i] != type[i-1])
				{
					return false;
				}
			}
		}
		faultHistory.setAdcType(checkAdcType.first().text());
		return true;
	},
	groupFaultPASCheck : function()
	{
		var checkAdcType;
		var type;
		var num=0;
		checkAdcType = $('.depth1 li:has(.groupBlock.on)').find('ul .adcType'); 
		type = checkAdcType.map(function(){return $(this).text();});
		
		for(var i=0; i<type.length; i++) // type에 저장되어 있는 text 값 순회하면서 비교
		{				 
			if(type[i] == 'PAS' || type[i] == 'PASK')
			{
				num=num+1; 
			}
		}
		
		if(num == type.length)
			return false;
		
		return true;
	},
	groupExistNotDiagnosis : function()
	{
		var checkAdcType;
		var type;
		if($('.depth0 li:has(.allBlock.on)').length > 0) // 전체 진단 선택시 전체의 벤더 확인. 같은 벤더면 true, 아니면 false
		{
			checkAdcType = $('.depth1').find('ul .opMode'); // 현재 모든 adcType 값이 담긴 span 리스트
			
		}
		else if($('.depth1 li:has(.groupBlock.on)').length > 0) //그룹 진단 선택시 그룹의 벤더 확인. 같은 벤더면 true, 아니면 false 
		{
			checkAdcType = $('.depth1 li:has(.groupBlock.on)').find('ul .opMode'); // 현재 선택 된 그룹의 adcType 값이 담긴 span 리스트
		}
		else
		{
			return true;
		}
		
		type = checkAdcType.map(function(){return $(this).text();}); //checkAdcType에 저장되어 있는 span의 text 값 리스트.
		
		for(var i=0; i<type.length; i++) // type에 저장되어 있는 text 값 순회하면서 비교
		{				 
			if(type[i] != '3')
			{
				return false;
			}
		}
		
		return true;
	},
	registerListContentsEvents : function()
	{ 
		with (this)
		{		
			if($('.depth0 li:has(.allBlock.on)').length > 0) //전체가 선택되었을때.
			{
				if(!groupAdcStatusCheck(0)) //모든 ADC의 연결상태 확인. 모두 연결이 안되어있으면 false, 하나이상 연결되어 있으면 true.
				{
					$('.searchNotMsg').addClass("none");
					$('.dataNotExistMsg').addClass("none");
					$('.imgOff').removeClass("none");
					$('.imgOn').addClass("none");
					$('.allHistoryChk').attr("disabled", "disabled");
					$('.historyChk').attr("disabled", "disabled");
					
					if ($('.faultHistoryList').size() > 0)			// data 존재 여부 확인
					{
						$('.disabledChk').removeClass("none");
						$('.nulldataMsg').addClass("none");
						
						$('.refeshImgOn').removeClass("none");
						$('.refeshImgOff').addClass("none");						
					}
					else
					{
						$('.disabledChk').addClass("none");
						$('.nulldataMsg').removeClass("none");
						
						$('input[name="searchKey"]').attr("disabled", "disabled");
						$('input[name="reservation"]').attr("disabled", "disabled");
												
						$('.refeshImgOff').removeClass("none");
						$('.refeshImgOn').addClass("none");
						
						$('.imageChange').attr("src", "imgs/meun/btn_search_1_off.png");					
						$('.ui-datepicker-trigger').attr("src", "imgs/meun/btn_calendar_off.png");
					}	
				}
					
			}
			else if($('.depth1 li:has(.groupBlock.on)').length > 0)  //그룹이  선택되었을때.
			{
				if(!groupFaultPASCheck())	//그룹에 존재하는 ADC들이 PAS,PASK인지 확인. 모두 PAS,PASK이면 false.
				{
					$('.searchNotMsg').addClass("none");
					$('.dataNotExistMsg').addClass("none");
					$('.imgOff').removeClass("none");
					$('.imgOn').addClass("none");
					$('.allHistoryChk').attr("disabled", "disabled");
					$('.historyChk').attr("disabled", "disabled");
					
					$('.disabledChk').addClass("none");
					$('.nulldataMsg').removeClass("none");
					
					$('input[name="searchKey"]').attr("disabled", "disabled");
					$('input[name="reservation"]').attr("disabled", "disabled");
										
					$('.refeshImgOff').removeClass("none");
					$('.refeshImgOn').addClass("none");
					
					$('.imageChange').attr("src", "imgs/meun/btn_search_1_off.png");					
					$('.ui-datepicker-trigger').attr("src", "imgs/meun/btn_calendar_off.png");
				}
				else
				{
					if(!groupAdcStatusCheck(1)) //그룹에 존재하는 ADC의 연결상태 확인. 모두 연결이 안되어있으면 false, 하나이상 연결되어 있으면 true.
					{
						$('.searchNotMsg').addClass("none");
						$('.dataNotExistMsg').addClass("none");
						$('.imgOff').removeClass("none");
						$('.imgOn').addClass("none");
						$('.allHistoryChk').attr("disabled", "disabled");
						$('.historyChk').attr("disabled", "disabled");
						
						if ($('.faultHistoryList').size() > 0)			// data 존재 여부 확인
						{
							$('.disabledChk').removeClass("none");
							$('.nulldataMsg').addClass("none");
							
							$('.refeshImgOn').removeClass("none");
							$('.refeshImgOff').addClass("none");						
						}
						else
						{
							$('.disabledChk').addClass("none");
							$('.nulldataMsg').removeClass("none");
							
							$('input[name="searchKey"]').attr("disabled", "disabled");
							$('input[name="reservation"]').attr("disabled", "disabled");
														
							$('.refeshImgOff').removeClass("none");
							$('.refeshImgOn').addClass("none");
							
							$('.imageChange').attr("src", "imgs/meun/btn_search_1_off.png");					
							$('.ui-datepicker-trigger').attr("src", "imgs/meun/btn_calendar_off.png");
						}	
					}
				}
			}
			
			$('.allHistoryChk').click(function(e) 
			{
				var isChecked = $(this).is(':checked');
				$(this).parents('table.historyTable').find('.historyChk').attr('checked', isChecked);
			});
			
			$('.historyDelLnk').off('click');  // 중복 이벤트 방지를 위한 등록 해제
			$('.historyDelLnk').click(function(e) 
			{
				e.preventDefault();
				var historyIndices = $('.historyChk').filter(':checked').siblings('.logKey').map(function() 
				{
					return $(this).text();
				}).get();
				if (historyIndices.length == 0)
				{
					$.obAlertNotice(VAR_FAULTHISTORY_DELSEL);
					return;
				}			
				var chk = confirm(VAR_FAULTHISTORY_DEL);
				if (chk) 
				{
					delHistorys(historyIndices);
				}
				else
				{
					return false;
				}
			});
		
			$('.refreshLnk').click(function(e)
			{
				e.preventDefault();
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
					searchStartTime = undefined;
					searchEndTime = undefined;
					loadListContent();
				}				
			});
			
			// 진단설정 화면으로 전환
			$('.addFaultSet').click(function(e)
			{
				if(!groupFaultBlockCheck()) //전체 또는 그룹 선택시 벤더 확인.
				{
					$.obAlertNotice(VAR_FAULTDIAGNOSIS_GROUPSUPPORT);
					return;
				}
				else
				{
					if(adcSetting.getAdc().mode == 1 || adcSetting.getAdc().mode == 2)
					{// 개별 ADC모드가 진단모드가 아닐때  
						$.obAlertNotice(VAR_FAULTDIAGNOSIS_MODECHECK);
						return;
					}
					else
					{
						if(!groupExistNotDiagnosis()) //그룹에 진단모드가 아닌 ADC가 포함되어있는지 확인. 있으면 false
						{
							$.obAlertNotice(VAR_FAULTDIAGNOSIS_GROUPMODECHECK);
							return;
						}
						else
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
								faultSetting.loadFaultSettingContent();
							}
						}
					}
				}
			});
			
			//예약 Click -> 예약된 설정정보 페이지로 전환
			$('.scheduleLnk').click(function(e)
			{
				e.preventDefault();
				scheduleIndex = $(this).children('.scheduleIndex').text();
				templateIndex = $(this).children('.templateIndex').text();
				scheduleType = $(this).children('.scheduleType').text(); 
				var e_hour = $(this).children('.everyHour').text();				// 시
				var e_minute = $(this).children('.everyMinute').text(); 		// 분
				var e_week = $(this).children('.everyDayOfWeek').text(); 		// 요일
				var e_month = $(this).children('.everyMonth').text(); 			// 월
				var e_day = $(this).children('.everyDayOfMonth').text(); 		// 일
								
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
					loadScheduleInfo(e_hour, e_minute, e_week, e_month, e_day);
				}
			});
			
			$('.delSchedule').click(function(e)
			{
				scheduleIndex = $(this).data('index');
//				scheduleIndex = $(this).children('span').text();
//				scheduleIndex = $(this).parents('span').find('.scheduleIndex').text();
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
					delScheduleInfo();
				}				
			});
			
			//완료된 진단 내용 click -> 진단 결과 페이지로 전환
			$('.diagnosisResult').click(function(e)
			{
				e.preventDefault();
				logKey = $(this).parents('tr').find('.logKey').text();
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
					faultResult.loadResultList(logKey, $(this).text(), $(this).data('index'));		
				}							
			});
			
			$('.control_Board a.searchLnk').click(function(e)
			{
				e.preventDefault();	
							
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
					var option = 
					{
						"key" : $('.control_Board input.searchTxt').val(),
						"startTimeL" : searchStartTime,
						"endTimeL" : searchEndTime
					};
					searchFlag = true;
					loadListContent(option, orderDir, orderType);
				}					
			});
			
			$('.control_Board input.searchTxt').keydown(function(e)
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
					if (e.which != 13)
					{
						return;
					}

					var option = 
					{
						"key" : $('.control_Board input.searchTxt').val(),
						"startTimeL" : searchStartTime,
						"endTimeL" : searchEndTime
					};				
					searchFlag = true;
					loadListContent(option);
				}				
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
					loadListContent(option , orderDir , orderType);
				}				
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
					loadListContent(option , orderDir , orderType);
				}
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
					loadListContent(option , orderDir , orderType);
				}
			});
			
//			if ((adcSetting.getAdc().type == "PAS") || (adcSetting.getAdc().type == "PASK"))
//			{
//				$('input[name="searchKey"]').attr("disabled", "disabled");
//				$('input[name="startTime"]').attr("disabled", "disabled");
//				$('input[name="endTime"]').attr("disabled", "disabled");						
//			}

			//TODO
			if(this.autoRefresh == false)
			{
				if(this.searchFlag == true)
				{
					$('.nulldataMsg').addClass("none");
					$('.dataNotExistMsg').addClass("none");
					if($('.faultHistoryList').size() > 0)
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
					if($('.faultHistoryList').size() > 0)
					{
						$('.dataNotExistMsg').addClass("none");
					}
					else
					{
						$('.dataNotExistMsg').removeClass("none");
					}
				}
			}
			else
			{
				if($('.faultHistoryList').size() > 0)
				{
					$('.nulldataMsg').addClass("none");
					$('.searchNotMsg').addClass("none");
					$('.dataNotExistMsg').addClass("none");
				}
				else
				{
					$('.nulldataMsg').addClass("none");
					$('.searchNotMsg').addClass("none");
					$('.dataNotExistMsg').removeClass("none");
				}
				autoRefresh=false;
			}
			
			if (adcSetting.getSelectIndex() == 2)
			{		
				if ((adcSetting.getAdc().type == "PAS") || (adcSetting.getAdc().type == "PASK"))					
				{				
					$('input[name="searchKey"]').attr("disabled", "disabled");
					$('input[name="reservation"]').attr("disabled", "disabled");					
					
					$('.ui-datepicker-trigger').attr("src", "imgs/meun/btn_calendar_off.png");					
					$('.imageChange').attr("src", "imgs/meun/btn_search_1_off.png");	
					$('.imgOff').removeClass("none");
					$('.imgOn').addClass("none");
					$('.allHistoryChk').attr("disabled", "disabled");
					$('.historyChk').attr("disabled", "disabled");
					$('.refeshImgOff').removeClass("none");
					$('.refeshImgOn').addClass("none");					
				}
				
				if (adcSetting.getAdcStatus() != "available") 
				{
					$('.searchNotMsg').addClass("none");
					$('.dataNotExistMsg').addClass("none");
					$('.imgOff').removeClass("none");
					$('.imgOn').addClass("none");
					$('.allHistoryChk').attr("disabled", "disabled");
					$('.historyChk').attr("disabled", "disabled");
					
					if ($('.faultHistoryList').size() > 0)			// data 존재 여부 확인
					{
						$('.disabledChk').removeClass("none");
						$('.nulldataMsg').addClass("none");
						
						$('.refeshImgOn').removeClass("none");
						$('.refeshImgOff').addClass("none");						
					}
					else
					{
						$('.disabledChk').addClass("none");
						$('.nulldataMsg').removeClass("none");
						
						$('input[name="searchKey"]').attr("disabled", "disabled");
						$('input[name="reservation"]').attr("disabled", "disabled");						
						
						$('.refeshImgOff').removeClass("none");
						$('.refeshImgOn').addClass("none");
						
						$('.imageChange').attr("src", "imgs/meun/btn_search_1_off.png");					
						$('.ui-datepicker-trigger').attr("src", "imgs/meun/btn_calendar_off.png");
					}	
				}
				
				 // adc가 모니터링 모드이거나 설정 모드일때 차단
				if (adcSetting.getAdc().mode == 1 || adcSetting.getAdc().mode == 2)
				{
					$('.dataNotExistMsg').addClass("none");
					$('.imgOff').removeClass("none");
					$('.imgOn').addClass("none");
					$('.allHistoryChk').attr("disabled", "disabled");
					$('.historyChk').attr("disabled", "disabled");
					
					if ($('.faultHistoryList').size() > 0)			// data 존재 여부 확인
					{
						$('.disabledChk').removeClass("none");
						$('.nulldataMsg').addClass("none");
						
						$('.refeshImgOn').removeClass("none");
						$('.refeshImgOff').addClass("none");						
					}
					else
					{
						$('.disabledChk').addClass("none");
						$('.nulldataMsg').removeClass("none");
						
						$('input[name="searchKey"]').attr("disabled", "disabled");
						$('input[name="reservation"]').attr("disabled", "disabled");
						
						$('.refeshImgOff').removeClass("none");
						$('.refeshImgOn').addClass("none");
						
						$('.imageChange').attr("src", "imgs/meun/btn_search_1_off.png");					
						$('.ui-datepicker-trigger').attr("src", "imgs/meun/btn_calendar_off.png");
					}	
				}
			}		
		}
	},		
	validateDaterefresh : function()
	{
		if(($('.control_Board input.searchTxt').val() != "") && ($('.control_Board input.searchTxt').val() != null))
		{
			if (!isValidStringLength($('.control_Board input.searchTxt').val(), 1, 64))
			{
				var data = VAR_COMMON_LENGTHFORMAT+"("+1+"~"+64+")";
				$.obAlertNotice(data);
				$('.control_Board input.searchTxt').val('');
				return false;
			}

			if (!isExistSpecialCharacter($('.control_Board input.searchTxt').val()))
			{
				$.obAlertNotice(VAR_FAULTSETTING_SPECIALCHAR);
				$('.control_Board input.searchTxt').val('');
				return false;
			}
		}
		
		return true;
	},	
	clearRefreshTimer : function()
	{
		with (this)
		{
			if (refreshTimer)
			{
				clearInterval(refreshTimer);
				refreshTimer = undefined;
			}
		}
	},	
	refreshFaultList : function(searchOption, orderDir, orderType)
	{
		with (this)
		{
			clearRefreshTimer();
			if (0 != refreshIntervalSeconds)
			{
				refreshTimer = setInterval(function()
				{
					if ($('.historyTable').is(':visible'))
					{
						autoRefresh=true;
						var option = 
						{
								"key" :	undefined,
								"startTimeL" : searchStartTime,
								"endTimeL" : searchEndTime
						};
						loadListContent(option, orderDir, orderType);
					}
					else
					{
						clearRefreshTimer();
					}
				}, refreshIntervalSeconds * 1000);
			}
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