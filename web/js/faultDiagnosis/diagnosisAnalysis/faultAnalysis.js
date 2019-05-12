var FaultAnalysis = Class.create({
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
		this.anlaysisPopup = new FaultAnalysisPopup();
		this.anlaysisPopup.owner = this;
		this.adc = {};
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
		this.pageNavi = new PageNavigator();
		this.adcModelNum = undefined;
		this.searchFlag = false;
		this.pageNavi.onChange(function(fromRow, toRow, orderType, orderDir) 
		{
			fn.loadAnalysisTableInListContent(fn.searchedOption, fromRow, toRow, orderType, orderDir);
		});	
	},	
	onAdcChange : function() 
	{
		this.orderDir  = 2; //2 :  내림차순
		this.orderType = 33;// 33 : occurTime
		var option = 
		{
			"key" :	undefined,
			"startTimeL" : this.searchStartTime,
			"endTimeL" : this.searchEndTime
		};
		this.loadListContent(option);
	},
	loadListContent : function(searchOption, orderDir, orderType)
	{
		with (this)
		{	
			// #3984-6 #5: 14.07.30 sw.jung 패킷분석 추가 후 오류 발생에 대한 처리
			if(searchOption && !validateDaterefresh())
			{
				return false;
			}
			
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
			this.categoryIndex = adcSetting.getSelectIndex();	
			this.adcModelNum = adcSetting.getAdcModel();
						
			var rowTotal = 0;
			ajaxManager.runJsonExt({
				url : "faultAnalysis/retrieveFaultAnalysisTotal.action",
				data :
				{
					"adcObject.category"	: categoryIndex,
					"adcObject.index" 		: selectedIndex,
					"adcObject.name"		: adcSetting.getGroupName(),
					"adcObject.desciption"  : adcSetting.getAdcStatus(),
					"adc.type" : adcSetting.getAdc().type,
					"adc.name" : adcSetting.getAdc().name,
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
					pageNavi.updateRowTotal(rowTotal, orderType);
					loadFaultAnalysisListContent(searchOption);
				},
				errorFn : function(jqXhr)
				{				
					$.obAlertAjaxError(VAR_COMMON_HISTORYDATAEXTRACT, jqXhr);		
				}	
			});
		}
	},
	loadFaultAnalysisListContent : function(searchOption, fromRow, toRow)
	{
		with (this)
		{
			this.categoryIndex = adcSetting.getSelectIndex();
			ajaxManager.runHtmlExt({
				url : "faultAnalysis/loadFaultAnalysisListContent.action",
				target : "#wrap .contents",
				data : 
				{
					"adcObject.category"	: categoryIndex,
//					"adcObject.index" 		: adcSetting.getAdc().index,
					"adcObject.index" 		: selectedIndex,
					"adcObject.name"		: adcSetting.getGroupName(),
					"adcObject.desciption"  : adcSetting.getAdcStatus(),
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
					header.setActiveMenu('FaultAnalysis');
					noticePageInfo();
					searchedOption = searchOption;
					registerListContentsEvents(categoryIndex, adcSetting.getAdc().type);
				},
				completeFn : function()
				{
					pageNavi.refresh();
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_COMMON_HISTORYDATAEXTRACT, jqXhr);
				}
			});
		}
	},
	loadAnalysisTableInListContent : function(searchOption, fromRow, toRow, orderType, orderDir)
//	loadAnalysisTableInListContent : function(fromRow, toRow, orderType, orderDir) 
	{			
		with (this)
		{
//			FlowitUtil.log("searchOption:%s, fromRow:%s, toRow:%s, orderType: %s, orderDir: %s", searchOption, fromRow, toRow, orderType, orderDir);
			this.categoryIndex = adcSetting.getSelectIndex();
			ajaxManager.runHtmlExt({
				url : "faultAnalysis/loadAnalysisTableInListContent.action",
				target : "table.pktDumpTable",
				data : 
				{
					"adcObject.category"	: categoryIndex,
//					"adcObject.index" 		: adcSetting.getAdc().index,
					"adcObject.index" 		: selectedIndex,
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
					registerListContentsEvents(categoryIndex, adcSetting.getAdc().type);
//					loadScheduleList();
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_COMMON_HISTORYDATAEXTRACT, jqXhr);
				}
			});
		}
	},
		
	delPktDumps : function(searchOption, pktDumpIndices)
	{
		with (this)
		{
			ajaxManager.runJsonExt({
				url : "faultAnalysis/delPktDumps.action",
				data : 
				{
					"adcObject.category"	: categoryIndex,
					"adcObject.index" 		: selectedIndex,
					"adcObject.name"		: adcSetting.getGroupName(),
					"adc.type" 				: adc.type,
					"adc.name" 				: adc.name,
					"pktDumpIndices" : pktDumpIndices
				},
				successFn : function(data)
				{
					if (!data.isSuccessful)
					{
						$.obAlertNotice(data.message);
						return;
					}
					loadListContent(searchOption);
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_PKT_HISTORYDEL, jqXhr);
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
	registerListContentsEvents : function(index, type)
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
					$('.allPktDumpChk').attr("disabled", "disabled");
					$('.pktDumpChk').attr("disabled", "disabled");
					
					if ($('.faultAnalysisList').size() > 0 )
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
						
						$('.imageChange').attr("src", "imgs/meun/btn_search_1_off.png");
						$('.ui-datepicker-trigger').attr("src", "imgs/meun/btn_calendar_off.png");
						
						$('.refeshImgOff').removeClass("none");
						$('.refeshImgOn').addClass("none");						
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
						$('.allPktDumpChk').attr("disabled", "disabled");
						$('.pktDumpChk').attr("disabled", "disabled");
						
						if ($('.faultAnalysisList').size() > 0 )
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
							
							$('.imageChange').attr("src", "imgs/meun/btn_search_1_off.png");
							$('.ui-datepicker-trigger').attr("src", "imgs/meun/btn_calendar_off.png");
							
							$('.refeshImgOff').removeClass("none");
							$('.refeshImgOn').addClass("none");						
						}
					}
				}
			}
			
			//패킷 수집추가 팝업
			$('.pktAdd').click(function(e)
			{		
				if(!groupFaultBlockCheck()) //전체 또는 그룹 선택시 벤더 확인.
				{
					$.obAlertNotice(VAR_PKT_GROUPSUPPORT);
					return;
				}
				else
				{
//					alert(index);
//					loadFaultAnalysisPopup(index);
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
					else if ((adcSetting.getAdc().type == "Alteon") && (adcModelNum.substr(0, 1) == 2 || adcModelNum.substr(0, 1) == 3))
					{
//						var adcModelNum = adcSetting.getAdcModel();
//						alert(adcModelNum);
//						alert(adcModelNum.substr(1,1));
						$.obAlertNotice(VAR_COMMON_ALTEON23NOTSUPPORT);					
					}
					else
					{
						anlaysisPopup.loadFaultAnalysisPopup(index, type);
					}	
				}
			});
			
			//전체 선택시
			$('.allPktDumpChk').click(function(e)
			{
				var isChecked = $(this).is(':checked');
				$(this).parents('table.pktDumpTable').find('.pktDumpChk').attr('checked', isChecked);
			});
			
			$('.delPktDumpLnk').off('click'); // 중복 이벤트 방지를 위한 등록 해제
			//패킷 목록 삭제			
			$('.delPktDumpLnk').click(function(e)
			{
//				alert("delPktDumpLnk");
				e.preventDefault();
				var pktDumpIndices = $('.pktDumpChk').filter(':checked').siblings('.pktDumpIndex').map(function()
				{
					return $(this).text();
				}).get();
				
//				alert($(this).text());
//				alert(pktDumpIndices);
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
				else if ((adcSetting.getAdc().type == "Alteon") && (adcModelNum.substr(0, 1) == 2 || adcModelNum.substr(0, 1) == 3))
				{
					{
						$.obAlertNotice(VAR_COMMON_ALTEON23NOTSUPPORT);
					}					
				}				
				else
				{
					if (pktDumpIndices.length == 0)
					{
						$.obAlertNotice(VAR_PKT_LISTDELSEL);
						return;
					}
					
					var option = 
					{
						"key" : $('.control_Board input.searchTxt').val(),
						"startTimeL" : searchStartTime,
						"endTimeL" : searchEndTime
					};
					
					var chk = confirm(VAR_PKT_DEL);
					if (chk)
					{					
						delPktDumps(option, pktDumpIndices);
					}
					else 
					{
						return false;
					}
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
				else if ((adcSetting.getAdc().type == "Alteon") && (adcModelNum.substr(0, 1) == 2 || adcModelNum.substr(0, 1) == 3))
				{
					$.obAlertNotice(VAR_COMMON_ALTEON23NOTSUPPORT);					
				}
				else
				{
					if(!validateDaterefresh())
					{
						return false;
					}
					searchStartTime = undefined;
					searchEndTime = undefined;
					loadListContent();
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
				else if ((adcSetting.getAdc().type == "Alteon") && (adcModelNum.substr(0, 1) == 2 || adcModelNum.substr(0, 1) == 3))
				{
					$.obAlertNotice(VAR_COMMON_ALTEON23NOTSUPPORT);					
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
				// from GS. #4012-1 #3 14.07.29 sw.jung: 검색필드에 불필요한 유효성검사 제거
//				if (adcSetting.getAdc().type == "PAS")
//				{
//					//alert(VAR_COMMON_PASNOTSUPPORT);
//					return;
//				}
//				else if (adcSetting.getAdc().type == "PASK")
//				{
//					//alert(VAR_COMMON_PASKNOTSUPPORT);
//					return;
//				}
//				else if ((adcSetting.getAdc().type == "Alteon") && (adcModelNum.substr(0, 1) == 2 || adcModelNum.substr(0, 1) == 3))
//				{
//					alert(VAR_COMMON_ALTEON23NOTSUPPORT);					
//				}
//				else
//				{
//				}				
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
				else if ((adcSetting.getAdc().type == "Alteon") && (adcModelNum.substr(0, 1) == 2 || adcModelNum.substr(0, 1) == 3))
				{
					$.obAlertNotice(VAR_COMMON_ALTEON23NOTSUPPORT);					
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
				else if ((adcSetting.getAdc().type == "Alteon") && (adcModelNum.substr(0, 1) == 2 || adcModelNum.substr(0, 1) == 3))
				{
					$.obAlertNotice(VAR_COMMON_ALTEON23NOTSUPPORT);					
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
				else if ((adcSetting.getAdc().type == "Alteon") && (adcModelNum.substr(0, 1) == 2 || adcModelNum.substr(0, 1) == 3))
				{
					$.obAlertNotice(VAR_COMMON_ALTEON23NOTSUPPORT);					
				}
				else
				{
					searchFlag = true;
					loadListContent(option , orderDir , orderType);
				}
			});
			$('.downloadPktDumpLnk').click(function(e)
			{
				e.preventDefault();
				var logKey = $(this).parent().parent().find('.pktDumpIndex').text();
				
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
				else if ((adcSetting.getAdc().type == "Alteon") && (adcModelNum.substr(0, 1) == 2 || adcModelNum.substr(0, 1) == 3))
				{
					$.obAlertNotice(VAR_COMMON_ALTEON23NOTSUPPORT);					
				}
				else
				{
					_checkDownloadPkDumpDataExist(logKey);
				}				
			});
			
			if(this.searchFlag == true)
			{
				$('.nulldataMsg').addClass("none");
				$('.dataNotExistMsg').addClass("none");
				if($('.faultAnalysisList').size() > 0)
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
				if($('.faultAnalysisList').size() > 0)
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
				if ((adcSetting.getAdc().type == "PAS") || (adcSetting.getAdc().type == "PASK") ||
						((adcSetting.getAdc().type == "Alteon") && (adcModelNum.substr(0, 1) == 2 || adcModelNum.substr(0, 1) == 3)))
				{				
					$('input[name="searchKey"]').attr("disabled", "disabled");
					$('input[name="reservation"]').attr("disabled", "disabled");					
					
					$('.ui-datepicker-trigger').attr("src", "imgs/meun/btn_calendar_off.png");
					$('.imgOff').removeClass("none");
					$('.imgOn').addClass("none");
					$('.refeshImgOff').removeClass("none");
					$('.refeshImgOn').addClass("none");
					$('.allPktDumpChk').attr("disabled", "disabled");
					$('.pktDumpChk').attr("disabled", "disabled");
				}
				
				if (adcSetting.getAdcStatus() != "available") 
				{
					$('.searchNotMsg').addClass("none");
					$('.dataNotExistMsg').addClass("none");
					$('.imgOff').removeClass("none");
					$('.imgOn').addClass("none");
					$('.allPktDumpChk').attr("disabled", "disabled");
					$('.pktDumpChk').attr("disabled", "disabled");
					
					if ($('.faultAnalysisList').size() > 0 )
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
						
						$('.imageChange').attr("src", "imgs/meun/btn_search_1_off.png");
						$('.ui-datepicker-trigger').attr("src", "imgs/meun/btn_calendar_off.png");
						
						$('.refeshImgOff').removeClass("none");
						$('.refeshImgOn').addClass("none");						
					}
				}
				 // adc가 모니터링 모드이거나 설정 모드일때 차단				
				if (adcSetting.getAdc().mode == 1 || adcSetting.getAdc().mode == 2) 
				{
					$('.searchNotMsg ').addClass("none");
					$('.dataNotExistMsg').addClass("none");
					$('.imgOff').removeClass("none");
					$('.imgOn').addClass("none");
					$('.allPktDumpChk').attr("disabled", "disabled");
					$('.pktDumpChk').attr("disabled", "disabled");
					
					if ($('.faultAnalysisList').size() > 0 )
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
						
						$('.imageChange').attr("src", "imgs/meun/btn_search_1_off.png");
						$('.ui-datepicker-trigger').attr("src", "imgs/meun/btn_calendar_off.png");
						
						$('.refeshImgOff').removeClass("none");
						$('.refeshImgOn').addClass("none");						
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
	_checkDownloadPkDumpDataExist : function(index)
	{
		with (this) 
		{
			ajaxManager.runJsonExt
			({
				url :"faultAnalysis/checkDownloadPktDumpDataExist.action",				
				data : 
				{
					"logKey" : index
				},
				successFn : function(data) 
				{
					if(!data.isSuccessful)
					{
						$.obAlertNotice(data.message);
						return;
					}	
					downloadPktDump(index);
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_PKT_DOWNLOADFILEEXSITINPECT, jqXhr);
				}	
			});
		}
		
	},
	downloadPktDump : function(index) 
	{
		with (this) 
		{
			var url = "faultAnalysis/downloadPktDump.action?logKey=" + index;
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