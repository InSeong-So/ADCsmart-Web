var MonitorServicePerfomanceRealServer = Class.create({
	initialize : function()
	{
		this.adc;
		this.vsIndex;
		this.vsvcIndex;
		this.selectedVsIndex;	// Alteon일 경우엔 selectSvcIndex, Name, Port	
		this.selectedPort;
		this.selectedMemberIndex;
		this.selectedMemberIp;
		this.selectedVsIp;		
		
		this.searchStartTime = undefined;
		this.searchEndTime = undefined;		
		this.selectedChartTapMode = undefined;	// responsChartTap, bpsConnChartTap

		this.pageNavigator = new PageNavigator({ rowCountPerPage : 10 });
		this.orderDir = 2; // 내림차순 = 2
        this.orderType = undefined; // 9NINTH = 38
        this.prevInfo = undefined; // 실시간 Chart 두번째 Call 부터 보내는 Row 데이터
        var _self = this;
		this.pageNavigator.onChange(function(fromRow, toRow, orderType, orderDir) {
			_self.selectedVsIndex = undefined;
			_self.selectedPort = undefined;
			_self.selectedMemberIndex = undefined;
			_self.selectedMemberIp = undefined;
			_self.selectedVsIp = undefined;
			_self.loadSvcMemberInfoList(fromRow, toRow, orderType, orderDir);
		});
		this.owner = undefined;
		this.bpsVal = undefined;
		this.searchDateTime = undefined;
		this.preVal = undefined;
		this.connVal = undefined;
		
	},
	load : function(vsIndex, vsIp, port, vsvcIndex, vsName, timeObject, bpsVal, preVal, connVal)
	{
		this.searchStartTime = timeObject.StartTime;
		this.searchEndTime = timeObject.EndTime;		
		this.vsIndex = vsIndex;
		this.vsvcIndex = vsvcIndex;
		this.selectedPort = port;
		this.bpsVal = bpsVal;
		this.preVal = preVal;
		this.connVal = connVal;
//		this.vsIp = vsIp;
		with(this)
		{
			selectedChartTapMode = "bpsConnChartTap";	
			searchDateTime = undefined;
			loadRealServerContent(vsIp, port, vsName);
		}
	},
	// 초기 페이지 Open
	loadRealServerContent : function(vsIp, port, vsName)
	{
		this.adc = adcSetting.getAdc();
		with (this)
		{
			if (!adc)
			{
				return;
			}
			var params = 
			{
				"adcObject.index" 		: adc.index,					
				"adcObject.category"	: adcSetting.getSelectIndex(),
				"adcObject.desciption"  : adc.type,
				"adc.isFlb" : adcSetting.getIsFlb()
			};
			ajaxManager.runHtmlExt({
				url : "monitor/loadRealServerContent.action",
				data : params,
				target : "#wrap .contents",				
				successFn : function(data)
				{					
					header.setActiveMenu('MonitorServicePerfomance');
					noticePageInfo();
					registRealServerContentEvents(adc);
					restoreRealServerContent();
					updateNavigator();
					updateHeaderNotice(vsIp, port, vsName);
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_SVCPERFOM_MEMBERLOAD, jqXhr);
				}
			});
		}
	},
	// Total 갯수 Get, Navigator 에 업데이트
	updateNavigator : function() 
	{
		with (this)
		{
			if (!adc)
			{
				return;
			}
			if (adc.type == "Alteon")
			{
				var params = {
						"adcObject.strIndex" 	: vsvcIndex,					
						"adcObject.category"	: 4
				};	
			}
			else
			{
				var params = {
						"adcObject.strIndex" 	: vsIndex,					
						"adcObject.category"	: 3
				};	
			}		

			var _self = this;
			ajaxManager.runJsonExt
			({
				url			: "monitor/retrieveVsMemberInfoTotalCount.action",
				data		: params,
				successFn	: function(data)
				{
					var totalRealCount = 0;
					if (data.totalRealCount)
					{
						totalRealCount = data.totalRealCount;
					}					
					//$('.vsTotalCount').text("Total: " + totalCount + "개");
					pageNavigator.updateRowTotal(totalRealCount);
					_self.selectedVsIndex = undefined;
					_self.selectedPort = undefined;
					_self.selectedMemberIndex = undefined;
					_self.selectedMemberIp = undefined;
					_self.selectedVsIp = undefined;
					
					loadSvcMemberInfoList();
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_SVCPERFOM_MEMBERLOAD, jqXhr);
//					exceptionEvent();
				}
			});
		}
	},
	// Table 데이터 성능 정보 Get
	loadSvcMemberInfoList : function(fromRow, toRow, orderType, orderDir)
	{
		with (this) 
		{
			if (!adc) 
			{
				return;
			}
			var URL = "";
			if (adc.type == "Alteon")
			{
				URL = "monitor/loadAlteonSvcMemberInfoList.action";				
				category = 4;
				strIndex = vsvcIndex;
			}
			else
			{
				URL = "monitor/loadSvcMemberInfoList.action";				
				category = 3;
				strIndex = vsIndex;
			}
			if (!orderType)
			{
				orderType = 46; // 초기 페이지는 CONN_CURR 로 초기화
			}
			var params = 
			{
				"adcObject.category"			: category,
				"adcObject.index"				: adc.index,
				"adcObject.strIndex"			: strIndex,
				"adcObject.name"				: adc.name,
				"adcObject.desciption"			: adc.type,
				"orderOption.orderType"			: orderType,
				"orderOption.orderDirection"	: orderDir				
			};
			params["searchOption.beginIndex"] = (undefined === fromRow ? pageNavigator.getFirstRowOfCurrentPage() : fromRow);
			params["searchOption.endIndex"] = (undefined === toRow ? pageNavigator.getLastRowOfCurrentPage() : toRow);
			
			var _self = this;
			ajaxManager.runHtmlExt
			({
				url		: URL,
				data	: params,
				target	: "table.svcTrafficInfoList",
				successFn : function(data)
				{				
					noticePageInfo();
					registSvcPerfInfoListEvents(category);
					pageNavigator.refresh();
					
					//orderType = undefined;
					var vsIndex = _self.selectedVsIndex;					
					var port = _self.selectedPort;
					var memberIndex = _self.selectedMemberIndex;
					var memberIp = _self.selectedMemberIp;
					var vsIp = _self.selectedVsIp;
//					if (!_self.selectedVsIndex && !_self.selectedMemberIndex)// 선택된게 없을때 맨처음껄 선택한다.
					if (!_self.selectedVsIndex)// 선택된게 없을때 맨처음껄 선택한다.
					{
						vsIndex = $('#svc_table tbody tr').eq(0).find('.vsIndex').text();
						memberIndex = $('#svc_table tbody tr').eq(0).attr('id');
//						memberIndex = $('#svc_table tbody tr').eq(0).find('.vsIndex').text();
//						alert("memberIndex : " + memberIndex);
						port = $('#svc_table tbody tr').eq(0).find('.vsPort').text().replace(",", "");
						vsName = $('#svc_table tbody tr').eq(0).find('.vsName').text();						
						//alert("vsIndex : " + vsIndex);
						//alert("port : " + port);
					}				
					
					this.selectedVsIndex = vsIndex;
					this.selectedPort = port;
					this.selectedMemberIndex = memberIndex;
//					this.selectedVsIp = vsIp;
					this.selectedMemberIp = memberIp;
					
//					if (adcSetting.getAdcStatus() != "available") 
//					{						
//						$('.defaultItem').addClass('none');
//						loadBpsConnHistoryInfo(vsIndex, port, undefined, vsName, bpsVal, preVal);
//					}
//					else
//					{
						$('.defaultItem').removeClass('none');
												
						loadBpsConnHistoryInfo(vsIndex, port, undefined, vsName, bpsVal, preVal);
						
						if(connVal == 0)
						{
							loadConnHistoryInfo(vsIndex, port, undefined, vsName, bpsVal, preVal, connVal);
						}
//					}
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_SVCPERFOM_MEMBERLOAD, jqXhr);
//					exceptionEvent();
				}
			});
		}		
	},
	updateHeaderNotice : function(vsIp, port, vsName)
	{
		var $vsNotice = $('.contents_area .vsNotice').filter(':last');
		$vsNotice.empty();
		html = '';
		html = vsName + " (IP : " + vsIp + "  " + VAR_COMMON_PORT + " : " + port +")";
		$vsNotice.html(html);
	},
	restoreRealServerContent : function() 
	{
		with (this)
		{
			if (!selectedChartTapMode)
			{
				selectedChartTapMode = "bpsConnChartTap";
			}			
			if(selectedChartTapMode == "bpsConnChartTap")
			{
				$('#bpsConnChartTap').css('background-color', '#666666');
				$('#bpsConnChartTap').css('color', 'white');
			}	
			
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

			// DateRangePicker
			if(langCode=="ko_KR")
			{
				$('#reservationtime').daterangepicker({					
					ranges: {					 
					 '최근 1시간' : [moment().subtract('hour', 1) ,moment()],
					 '최근 3시간' : [moment().subtract('hour', 3) ,moment()],
					 '최근 6시간' : [moment().subtract('hour', 6) ,moment()],
					 '최근 12시간' : [moment().subtract('hour', 12) ,moment()],
					 '최근 24시간' : [moment().subtract('hour', 24) ,moment()],
					 '오늘': [moment().startOf('days'), moment()],
					 '최근 30일': [moment().subtract('days', 30), moment()]					 
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
					 'Last hour' : [moment().subtract('hour', 1) ,moment()],
					 'Last 3 hours' : [moment().subtract('hour', 3) ,moment()],
					 'Last 6 hours' : [moment().subtract('hour', 6) ,moment()],
					 'Last 12 hours' : [moment().subtract('hour', 12) ,moment()],
					 'Last 1 Day' : [moment().subtract('hour', 24) ,moment()],
					 'Today': [moment().startOf('days'), moment()],
					 'Last 30 Days': [moment().subtract('days', 30), moment()]					 
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
			
			if (!searchDateTime) 
			{
				searchDateTime = new Date();
				$('#compareDateTime').val(new Date(searchDateTime).format('yyyy-mm-dd'));
			}
			
			$('input[name="selectedDate"]').datepicker({
				maxDate: "0",
				dateFormat : "yy-mm-dd",
				showOn: "button",
				buttonImage: "imgs/meun/btn_calendar.png",
				buttonImageOnly: true
			});

					
			$('input[name="startTime"]').datepicker({
				maxDate: "0",
				dateFormat : "yy-mm-dd",
				showOn: "button",
				buttonImage: "imgs/meun/btn_calendar.png",
				buttonImageOnly: true,
				defaultDate: searchDateTime,
				onSelect: function(dateText, inst)
				{
					$('#periodMode').attr('checked', 'checked');
					selectedSearchTimeMode = 'periodMode';//$('input[name="reservation"]:checked').val();
					searchDateTime = $("input[name='startTime']").datepicker("getDate");
				}
			});
		}
	},
	// 초기페이지 로드 후 기본 form 이벤트 (검색조건, Refresh, 내보내기 Btn) 
	registRealServerContentEvents : function (adc)
	{
		with(this)
		{
			$('.flbGroupLnk').click(function(e) 
			{
				e.preventDefault();
				header.setMonitorLbTap(2);
				flbGroupPerfomance.loadGroupPerfomanceContent();
			});
			
			$('#refresh').click(function(event) 
			{				
				event.preventDefault();
				loadSvcMemberInfoList();
			});	
			
			$('#exportMember').click(function(e) 
			{
				e.preventDefault();
				
				if (selectedMemberIndex)
				{
					checkExportMemberDataExist();					
				}
				else
				{
					checkExportSvcPerfDataExist();
				}
			});
			
			$('#serviceListLnk').click(function(e)
			{				
				e.preventDefault();			
				
//				Wed Aug 12 2015 15:45:21 GMT+0900 (대한민국 표준시)
				
//				var selDate = searchDateTime.getFullYear() + "-" + (searchDateTime.getMonth()+1) + "-" + searchDateTime.getDate();
				
//				console.log(selDate);
				owner.loadServicePerfomanceContent(vsIndex, selectedPort, parseDateTimeStringShort(searchDateTime));
			});
		}
	},	
	// 리스트 테이블 내에서의 이벤트 (row Click, Sorting) 
	registSvcPerfInfoListEvents : function(category)
	{
		with(this)
		{
		//  List Table row click 시 발생하는 이벤트
			$('#svc_table tbody tr').click(function(e)
			{
				var seletedTrIndex = $(this).attr('id');
				var seletedTrIp = $(this).find('.memberIp').text();	
				//alert(seletedTrIndex);//${vsMemberTrafficInfo.index!""}
				// Vs 를 선택했을때
				if ( $(this).attr('idx') == "-1")				
//				if (seletedTrIndex == -1)
				{
					var vsIndex = $(this).find('.vsIndex').text();					
					var port = $(this).find('.vsPort').text().replace(",", "");		
					var vsIp = $(this).find('.vsName').text();	
					this.selectedVsIndex = vsIndex;				
					this.selectedPort = port;
					this.selectedMemberIndex = undefined;
					this.selectedMemberIp = undefined;
					loadBpsConnHistoryInfo(vsIndex, port, $(this).index(), vsIp, bpsVal, preVal);
					
					if($('input[name="concurrSessionValChk"]').is(':checked') == true)
					{
						connVal = 0;
						loadConnHistoryInfo(vsIndex, port, undefined, vsName, bpsVal, preVal, connVal);
					}
				}
				// Member를 선택했을때
				else
				{
					this.selectedMemberIndex = seletedTrIndex;
					this.selectedVsIndex = undefined;				
					this.selectedPort = undefined;
					this.selectedMemberIp = seletedTrIp;
					loadVsMemberHistoryInfo(seletedTrIndex, $(this).index(), seletedTrIp, bpsVal, preVal);
					
					if($('input[name="concurrSessionValChk"]').is(':checked') == true)
					{
						connVal = 0;
						loadVsMemberConnHistoryInfo(seletedTrIndex, undefined, seletedTrIp, bpsVal, preVal, connVal);
					}
				}						
			});
			
			$('.selectBps').click(function(e) 
			{
				selectedChartTapMode = "bpsConnChartTap";
				bpsVal = $(this).val();

				if($('#svc_table tbody tr').eq(0).hasClass('vsMonitorRowSelection'))
				{
					loadBpsConnHistoryInfo(selectedVsIndex, selectedPort, undefined, selectedVsName, bpsVal, preVal);
					loadConnHistoryInfo(selectedVsIndex, selectedPort, undefined, selectedVsName, bpsVal, preVal, 0);
				}
				else
				{				
					loadVsMemberHistoryInfo(selectedMemberIndex, undefined, selectedMemberIp, bpsVal, preVal);
					loadVsMemberConnHistoryInfo(selectedMemberIndex, undefined, selectedMemberIp, bpsVal, preVal, 0);
				}
				itemCompare(bpsVal);
			});
			
			$('.selectBps').attr('checked', true);
			if (bpsVal == 0)	// bpsIn
			{
				$('#selectBpsIn').attr('checked', true);
			}
			else if (bpsVal == 1) // bpsOut
			{
				$('#selectBpsOut').attr('checked', true);
			}
			else if (bpsVal == 2) // bpsTot
			{
				$('#selectBpsTotal').attr('checked', true);
			}
			else
			{	
				$('#selectBpsTotal').attr('checked', true);
			}
			
			if(connVal == 0)
			{
				$('input[name="concurrSessionValChk"]').attr('checked', true);
				$('.connCurrChart').removeClass('none');
			}
			else
			{
				$('input[name="concurrSessionValChk"]').attr('checked', false);
				$('.connCurrChart').addClass('none');
			}
			
			$('.preCompare').attr('checked', true);
			if (preVal == 0)	// 전일 선택
			{						
				$('#preDay').attr('checked', true);
			}
			else if (preVal == 1) // 전주 선택
			{
				$('#preWeek').attr('checked', true);
			}
			else if (preVal == 2) // 전월 선택
			{
				$('#preMonth').attr('checked', true);
			}
			else if (preVal == 3) // 특정날짜 선택
			{
				$('#selDate').attr('checked', true);
			}
			else
			{
				$('#preNot').attr('checked', true);
			}
			
			$('.preCompare').click(function(e) 
			{
				selectedChartTapMode = "bpsConnChartTap";
				selectedSearchTimeMode = 'periodMode';
				preVal = $(this).val();
				if (preVal == 0)	// 전일 선택
				{		
					searchDateTime = undefined;
					$('.compareChk').removeClass('none');
				}
				else if (preVal == 1) // 전주 선택
				{
					searchDateTime = undefined;
					$('.compareChk').removeClass('none');
				}
				else if (preVal == 2) // 전월 선택
				{
					searchDateTime = undefined;
					$('.compareChk').removeClass('none');
				}
				else if (preVal == 3) // 특정날짜 선택
				{
					searchDateTime = $('#compareDateTime').val();
					$('.compareChk').removeClass('none');
				}
				else
				{
					$('.compareChk').addClass('none');
				}
				
				if($('#svc_table tbody tr').eq(0).hasClass('vsMonitorRowSelection'))
				{
					loadBpsConnHistoryInfo(selectedVsIndex, selectedPort, undefined, selectedVsName, bpsVal, preVal);
					loadConnHistoryInfo(selectedVsIndex, selectedPort, undefined, selectedVsName, bpsVal, preVal, 0);
				}
				else
				{				
					loadVsMemberHistoryInfo(selectedMemberIndex, undefined, selectedMemberIp, bpsVal, preVal);
					loadVsMemberConnHistoryInfo(selectedMemberIndex, undefined, selectedMemberIp, bpsVal, preVal, 0);
				}
			});
			
			$('.bpsAllChk').change(function(event) 
			{			
				event.preventDefault();
				$('.preCompare').removeAttr("disabled");
				var isChecked = $(this).is(":checked");
				
				if(isChecked == true)
				{					
					$('.selectBps').removeAttr('disabled');		
					$('.bpsChart').removeClass('none');
				}
				else
				{					
					$('.selectBps').attr('disabled', true);
					$('.bpsChart').addClass('none');
				}	
			});
			
			$('#concurrSessionChk').change(function(event) 
			{			
				event.preventDefault();
//				selectedChartTapMode = "connChartTap";
				$('.preCompare').removeAttr("disabled");
				
				var isChecked = $(this).is(':checked');
				if(isChecked == true)
				{
					$('.connCurrChart').removeClass('none');
				}
				else
				{
					$('.connCurrChart').addClass('none');
				}
					
				if($('#svc_table tbody tr').eq(0).hasClass('vsMonitorRowSelection'))
				{
					loadBpsConnHistoryInfo(selectedVsIndex, selectedPort, undefined, selectedVsName, bpsVal, preVal);
					loadConnHistoryInfo(selectedVsIndex, selectedPort, undefined, selectedVsName, bpsVal, preVal, 0);
				}
				else
				{				
					loadVsMemberHistoryInfo(selectedMemberIndex, undefined, selectedMemberIp, bpsVal, preVal);
					loadVsMemberConnHistoryInfo(selectedMemberIndex, undefined, selectedMemberIp, bpsVal, preVal, 0);
				}
			});
						
			$('.orderDir_Desc').click(function(e)
			{
				e.preventDefault();
				orderDir =  $(this).find('.orderDir').text();
				orderType =  $(this).find('.orderType').text();					
				loadSvcMemberInfoList();
			});
			
			$('.orderDir_Asc').click(function(e)
			{
				e.preventDefault();
				orderDir =  $(this).find('.orderDir').text();
				orderType =  $(this).find('.orderType').text();	
				loadSvcMemberInfoList();
			});
			
			$('.orderDir_None').click(function(e)
			{
				e.preventDefault();
				orderDir =  $(this).find('.orderDir').text();
				orderType =  $(this).find('.orderType').text();	
				loadSvcMemberInfoList();
			});			
		}		
	},
	checkExportSvcPerfDataExist : function()
	{
		with(this)
		{
			if (!adc)
			{
				return;
			}
			var params = {
					"adcObject.category"			: adcSetting.getSelectIndex(),
					"adcObject.index"				: adc.index,
					"adcObject.name"				: adc.name,				
					"vsIndex"	: this.vsIndex,
					"svcPort" : selectedPort
				};
				params['selectedChartTapMode'] = selectedChartTapMode;
				params['startTimeL'] = searchStartTime;
				params['endTimeL'] = searchEndTime;
				params['preCompare'] = preVal;
				params['selectedTime'] = searchDateTime;
				
				ajaxManager.runJsonExt
				({
					url : "monitor/checkExportSvcPerfDataExist.action",
					data : params,
					successFn : function(data)
					{
						if(!data.isSuccessful)
						{
							$.obAlertNotice(data.message);
							return;
						}
						exportCsv();
					},
					errorFn : function(jqXhr)
					{
						$.obAlertAjaxError(VAR_SVCPERFOM_EXPEXISTINSPECT, jqXhr);
//						exceptionEvent();
					}
				});			
		}		
	},	
	exportCsv : function(bpsVal, preVal, connVal)
	{
		with (this)
		{
			var params = "adcObject.category=" + adcSetting.getSelectIndex() + 
							"&adcObject.index=" + adc.index +
							"&vsIndex=" + this.vsIndex + 
							"&svcPort=" + selectedPort + 
							"&selectedChartTapMode=" + selectedChartTapMode +
							"&startTimeL=" + searchStartTime + 
							"&endTimeL=" + searchEndTime +
							"&preCompare=" + preVal +
							"&selectedTime=" + searchDateTime +
							"&adcObject.vendor=" + bpsVal + 
							"&adcObject.status=" + connVal; 						
			var url = "monitor/downloadSvcPerfInfo.action?" + params;
			$('#downloadFrame').attr('src', url);			
		}
	},
	checkExportMemberDataExist : function()
	{
		with(this)
		{
			if (!adc)
			{
				return;
			}
			var params = {
					"adcObject.category"			: adcSetting.getSelectIndex(),
					"adcObject.index"				: adc.index,
					"adcObject.name"				: adc.name,				
					"adcObject.strIndex"			: this.selectedMemberIndex,
					"vsIndex"						: vsIndex,
					"svcPort" 						: selectedPort
				};
		
				params['selectedChartTapMode'] = selectedChartTapMode;				
				params['startTimeL'] = searchStartTime;
				params['endTimeL'] = searchEndTime;
				params['preCompare'] = preVal;
				params['selectedTime'] = searchDateTime;
				
				ajaxManager.runJsonExt
				({
					url : "monitor/checkExportMemberDataExist.action",
					data : params,
					successFn : function(data)
					{
						if(!data.isSuccessful)
						{
							$.obAlertNotice(data.message);
							return;
						}
							exportCsvMember();
					},
					errorFn : function(jqXhr)
					{
						$.obAlertAjaxError(VAR_SVCPERFOM_REALRESPONSENOTSUPPORT, jqXhr);
//						exceptionEvent();
					}
				});			
		}		
	},	
	exportCsvMember : function(bpsVal, preVal, connVal)
	{
		with (this)
		{
			var params = "adcObject.category=" +adcSetting.getSelectIndex()+ 
							"&adcObject.index=" + adc.index + 
							"&adcObject.strIndex=" + this.selectedMemberIndex +
							"&vsIndex=" + vsIndex + "&svcPort=" + selectedPort + 
							"&selectedChartTapMode=" + selectedChartTapMode +
							"&startTimeL=" + searchStartTime + 
							"&endTimeL=" + searchEndTime +
							"&preCompare=" + preVal +
							"&selectedTime=" + searchDateTime +
							"&adcObject.vendor=" + bpsVal + 
							"&adcObject.status=" + connVal; 					
			var url = "monitor/downloadMemberInfo.action?" + params;
			this.selectedMemberIndex = undefined;
			$('#downloadFrame').attr('src', url);			
		}
	},	
	// Select 테이블 Row
	selectvsTableRow : function(vsIndex, port, rowIndex, vsName)
	{
		$('#svc_table tbody tr').removeClass("vsMonitorRowSelection");		
		$('#svc_table tbody tr').each(function(index) {
			if ($(this).find(".vsPort").text().replace(",", "") === port)
			{
				if (index === rowIndex || (!rowIndex && 0 !== rowIndex))
				{					
					$(this).addClass("vsMonitorRowSelection");
					vsIp = $(this).find('#ipaddress').text();
					vsPort = $(this).find('.vsPort').text();
					return false;
				}
			}
		});
		
		var headerName;
		if (vsName && vsName != '')
			headerName = vsName;
		else
		{
			headerName = vsIp;
			if (vsPort)
				headerName += ':' + vsPort;
		}
		// Header Notice 기능
		this.listHeaderNameChanger(headerName);
		this.vsNameChanger(vsName, vsIp);
	},	
	// Select 테이블 Row
	selectMemberTableRow : function(MemberIndex, port, rowIndex, vsName, vsIp)
	{
		$('#svc_table tbody tr').removeClass("vsMonitorRowSelection");		
		$('#svc_table tbody tr').each(function(index) {
			if ($(this).attr("id") === MemberIndex)			
			{
				if (index === rowIndex || (!rowIndex && 0 !== rowIndex))
				{					
					$(this).addClass("vsMonitorRowSelection");
					return false;
				}
			}
		});
		// Header Notice 기능
		this.listHeaderNameChanger(vsIp);
		this.vsNameChanger(vsIp);
	},	
	listHeaderNameChanger : function(vsIp)
	{
		var lisetHeaderName = vsIp;
		var $tbody = $('.contents_area #lisetHeaderChange').filter(':last');		
		$tbody.empty();	
		var html = '';		
		html += '[' + lisetHeaderName + ']';		
		$tbody.html(html);		
	},
	// bps&Connection Chart Data get
	// TODO 서비스 bps, conn
	loadBpsConnHistoryInfo : function(vsIndex, port, rowIndex, vsName, bpsVal, preVal)
	{
		this.selectedVsIndex = vsIndex;
		this.selectedPort = port;
		this.selectedVsName = vsName;
		with (this)
		{
			if (!adc || !vsIndex)
			{
				return;
			}
			var params = {
				"adcObject.category"			: adcSetting.getSelectIndex(),
				"adcObject.index"				: adc.index,			
				"adcObject.name"				: adc.name,
				"vsIndex"						: vsIndex
			};
			if (0 === port || port)
			{
				params["svcPort"] = port;
			};			
			params['startTimeL'] = searchStartTime;
			params['endTimeL'] = searchEndTime;
			params['preCompare'] = preVal;
			params['selectedTime'] = searchDateTime; 
			
			ajaxManager.runJsonExt({
				url			: "monitor/loadBpsConnHistoryInfo.action",
				data		: params,
				successFn	: function(data)
				{	
					intervalMonitor = data.intervalMonitor;
					GenerateSvcPerfomanceBpsConnChart(data, bpsVal, preVal, intervalMonitor);
					loadBpsConnMaxAvgHistoryInfo(vsIndex, port, undefined, vsName, bpsVal, preVal);					
//					selectvsTableRow(vsIndex, port, rowIndex, vsName);
				},
				completeFn	: function(data)
				{	
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_SVCPERFOM_MEMBERLOAD, jqXhr);
					selectvsTableRow(vsIndex, port, rowIndex, vsName);
				}
			});
		}		
	},
	
	// bps&Connection Avg/Max Data get
	// TODO 서비스 bps max/avg
	loadBpsConnMaxAvgHistoryInfo : function(vsIndex, port, rowIndex, vsName, bpsVal, preVal)
	{
		this.selectedVsIndex = vsIndex;
		this.selectedPort = port;
		this.selectedVsName = vsName;
		with (this)
		{
			if (!adc || !vsIndex)
			{
				return;
			}
			var params = {
				"adcObject.category"			: adcSetting.getSelectIndex(),
				"adcObject.index"				: adc.index,			
				"adcObject.name"				: adc.name,
				"vsIndex"						: vsIndex				
			};
			if (0 === port || port)
			{
				params["svcPort"] = port;
			};		
			params['startTimeL'] = searchStartTime;
			params['endTimeL'] = searchEndTime;
			params['preCompare'] = preVal;
			params['selectedTime'] = searchDateTime; 
			
			ajaxManager.runHtmlExt({
				url			: "monitor/loadBpsMaxAvgHistoryInfo.action",
				data		: params,
				target		: "tbody.bpsAvgMax",	
				successFn	: function(data)
				{									
					selectvsTableRow(vsIndex, port, rowIndex, vsName, bpsVal);
					
					registerEvent(bpsVal, preVal);					
				},
				completeFn	: function(data)
				{	
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_SVCPERFOM_LAOD, jqXhr);
					selectvsTableRow(vsIndex, port, rowIndex, vsName, bpsVal);
				}
			});
		}		
	},	
	// TODO 서비스 Conn
	loadConnHistoryInfo : function(vsIndex, port, rowIndex, vsName, bpsVal, preVal, connVal)
	{
		this.selectedVsIndex = vsIndex;
		this.selectedPort = port;
		this.selectedVsName = vsName;
		with (this)
		{
			if (!adc || !vsIndex)
			{
				return;
			}
			var params = {
				"adcObject.category"			: adcSetting.getSelectIndex(),
				"adcObject.index"				: adc.index,			
				"adcObject.name"				: adc.name,
				"vsIndex"						: vsIndex
			};
			if (0 === port || port)
			{
				params["svcPort"] = port;
			};
			params['startTimeL'] = searchStartTime;
			params['endTimeL'] = searchEndTime;
			params['preCompare'] = preVal;
			params['selectedTime'] = searchDateTime;
			
			ajaxManager.runJsonExt({
				url			: "monitor/loadConnHistoryInfo.action",
				data		: params,
				successFn	: function(data)
				{					
					intervalMonitor = data.intervalMonitor
					GenerateSvcPerfomanceConnChart(data, preVal, connVal, intervalMonitor);	
					loadConnMaxAvgHistoryInfo(vsIndex, port, rowIndex, vsName, bpsVal, preVal, connVal);								
				},
				completeFn	: function(data)
				{	
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_SVCPERFOM_LAOD, jqXhr);
					selectvsTableRow(vsIndex, port, rowIndex, vsName);
				}
			});
		}		
	},
	// TODO 서비스 conn max /avg
	loadConnMaxAvgHistoryInfo : function(vsIndex, port, rowIndex, vsName, bpsVal, preVal, connVal)
	{
		this.selectedVsIndex = vsIndex;
		this.selectedPort = port;
		this.selectedVsName = vsName;
		with (this)
		{
			if (!adc || !vsIndex)
			{
				return;
			}
			var params = {
				"adcObject.category"			: adcSetting.getSelectIndex(),
				"adcObject.index"				: adc.index,			
				"adcObject.name"				: adc.name,
				"vsIndex"						: vsIndex				
			};
			if (0 === port || port)
			{
				params["svcPort"] = port;
			};	
			params['startTimeL'] = searchStartTime;
			params['endTimeL'] = searchEndTime;
			params['preCompare'] = preVal;
			params['selectedTime'] = searchDateTime; 
			
			ajaxManager.runHtmlExt({
				url			: "monitor/loadConnMaxAvgHistoryInfo.action",
				data		: params,
				target		: "tbody.connAvgMax",	
				successFn	: function(data)
				{									
					selectvsTableRow(vsIndex, port, rowIndex, vsName, connVal);
					registerConnEvent(bpsVal, preVal, connVal);
				},
				completeFn	: function(data)
				{	
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_SVCPERFOM_LAOD, jqXhr);
					selectvsTableRow(vsIndex, port, rowIndex, vsName, bpsVal);
				}
			});
		}		
	},
	registerEvent : function(bpsVal, preVal, connVal)
	{
		if (bpsVal == 0)	// 전체 선택
		{
			$('.bpsInData').removeClass('none');
			$('.bpsOutData').addClass('none');
			$('.bpsTotData').addClass('none');
			$('.connCurrData').addClass('none');
		}
		else if (bpsVal == 1) // 그룹 선택
		{
			$('.bpsInData').addClass('none');
			$('.bpsOutData').removeClass('none');
			$('.bpsTotData').addClass('none');
			$('.connCurrData').addClass('none');
		}
		else if (bpsVal == 2) // 개별 선택
		{
			$('.bpsInData').addClass('none');
			$('.bpsOutData').addClass('none');
			$('.bpsTotData').removeClass('none');
			$('.connCurrData').addClass('none');
		}
		else 
		{
			$('.bpsInData').addClass('none');
			$('.bpsOutData').addClass('none');
			$('.bpsTotData').addClass('none');
			$('.connCurrData').addClass('none');
		}
		
		if($('input[name="concurrSessionValChk"]').is(':checked') == true)							
		{
			$('.connCurrData').removeClass('none');
		}
		else
		{
			$('.connCurrData').addClass('none');
		}
		
		var itemName = "";
		var $tbody = $('.contents_area #compareChange');	
		
		$tbody.empty();	
		var html = '';		
			
		if (preVal == 0)	// 전일 선택
		{		
			searchDateTime = undefined;
			$('.compareChk').removeClass('none');
			itemName = "전일";
		}
		else if (preVal == 1) // 전주 선택
		{
			searchDateTime = undefined;
			$('.compareChk').removeClass('none');
			itemName = "전주";
		}
		else if (preVal == 2) // 전월 선택
		{
			searchDateTime = undefined;
			$('.compareChk').removeClass('none');
			itemName = "전월";
		}
		else if (preVal == 3) // 특정날짜 선택
		{
			searchDateTime = $('#compareDateTime').val();
			$('.compareChk').removeClass('none');
			itemName = $('#compareDateTime').val();
		}
		else
		{
			$('.compareChk').addClass('none');
		}
		
		html += itemName;		
		$tbody.html(html);
	},
	
	registerConnEvent : function(bpsVal, preVal, connVal)
	{
		if(connVal == 0)
			$('.connCurrData').removeClass('none');
		else
			$('.connCurrData').addClass('none');
		
		var itemName = "";
		var $tbody = $('.contents_area #compareChange');	
		
		$tbody.empty();	
		var html = '';		
			
		if (preVal == 0)	// 전일 선택
		{		
			searchDateTime = undefined;
			$('.compareChk').removeClass('none');
			itemName = "전일";
		}
		else if (preVal == 1) // 전주 선택
		{
			searchDateTime = undefined;
			$('.compareChk').removeClass('none');
			itemName = "전주";
		}
		else if (preVal == 2) // 전월 선택
		{
			searchDateTime = undefined;
			$('.compareChk').removeClass('none');
			itemName = "전월";
		}
		else if (preVal == 3) // 특정날짜 선택
		{
			searchDateTime = $('#compareDateTime').val();
			$('.compareChk').removeClass('none');
			itemName = $('#compareDateTime').val();
		}
		else
		{
			$('.compareChk').addClass('none');
		}
		
		html += itemName;		
		$tbody.html(html);
	},
	/// bps Connection Chart Generate
	GenerateSvcPerfomanceBpsConnChart : function(data, value, preVal, interval)
	{
		with(this)
		{
			//var ConfigDate = [];		
			var chartData = [];
			var chartDataList = [];
			if (data.bpsConnInfo.bpsConnData != null)
			{
				chartDataList = data.bpsConnInfo.bpsConnData;
			}			
			if (chartDataList.length > 0 && chartDataList != null)
			{
				 for ( var i = 0; i < chartDataList.length; i++)
				 {
					if (i == 0)
					{
						if (data.startTime < chartDataList[0].occurTime)
						{
							var startTime = parseDateTime(data.startTime);
							chartData.push({occurredTime:startTime});
						}
					}
					var column = chartDataList[i];
					if (column)
					{					
						var date = parseDateTime(column.occurTime);						
				        var firstValue = undefined;
						var secondValue = undefined;
						
						if(value == 0)
						{
							if (column.bpsInValue > -1 && column.bpsInValue != null)
							{
								firstValue = column.bpsInValue;
								if(preVal != -1)
									secondValue = column.preBpsInValue;
							}
						}
						else if(value == 1)
						{
							if (column.bpsOutValue > -1 && column.bpsOutValue != null)
							{
								firstValue = column.bpsOutValue;
								if(preVal != -1)
									secondValue = column.preBpsOutValue;
							}
						}
						if(value == 2)
						{
							if (column.bpsTotValue > -1 && column.bpsTotValue != null)
							{
								firstValue = column.bpsTotValue;
								if(preVal != -1)
									secondValue = column.preBpsTotValue;
							}
						}
						var dataObject =
						{
							occurredTime : date,
							firstValue : firstValue,							
							secondValue : secondValue
						};						
						chartData.push(dataObject);							
					}
					if (i == (chartDataList.length - 1))
					{
						if (data.endTime > chartDataList[chartDataList.length - 1].occurTime)
						{
							var endTime = parseDateTime(data.endTime);
							chartData.push({occurredTime:endTime});								 
						}
					}
				 }
			}
			else
			{
				var startTime = parseDateTime(data.startTime);
				var endTime = parseDateTime(data.endTime);
				var dataObject =
				{
					occurredTime : startTime
				};
				chartData.push(dataObject);
				var dataObject =
				{
					occurredTime : endTime
				};
				chartData.push(dataObject);
			}			
			// Config Event, Array를 따로 넘길때.			
			/*if (chartConfigList.length > 0 && chartDataList != null)
			{
				for ( var i = 0; i < chartConfigList.length; i++)
				{		
					var column = chartConfigList[i];
					if (column)
					{
						var date = parseDateTime(column.occurredTime);	
						ConfigDate.push(date);
					}
				}
			}*/		
			
			/* 
			 * IE8에서는 map() 지원하지 않음 
			 * The solution is jQuery.map
			 * a.map(function( ) { }); -> jQuery.map(a, function( ) { //what ever you want todo .. }
			 */
//			var maxPos = Math.max.apply(null,maxData.map(function(o){return o.value;})); 
			var chartOption =
			{
				 min : 0,
				 max : null,
				 linecolor1 : "#6cb8c8",
				 linecolor2 : "gray",
				 linecolor3 : "#6cb8c8",
				 linecolor4 : "gray",
				 chartname : "MemberPerfChart",
				 axistitle1 : VAR_COMMON_BPS,
				 axistitle2 : VAR_COMMON_AFEW,
				 maxPos : null,
				 cursorColor : "#0f47c7",
				 bpsValue : value,
				 preValue : preVal,
//				 selectedDate : searchDateTime
				 selectedDate : $('#compareDateTime').val(),
				 interval : interval
			};
			obchart.OBSvcPerfomanceBpsChartViewerExtends(chartData, chartOption);
        }			
	},
	
	GenerateSvcPerfomanceConnChart : function(data, preVal, connVal, interval)
	{
		with(this)
		{	
			var chartData = [];
			var chartDataList = [];
			if (data.bpsConnInfo.bpsConnData != null)
			{
				chartDataList = data.bpsConnInfo.bpsConnData;
			}
			
			if (chartDataList.length > 0 && chartDataList != null)
			{
				 for ( var i = 0; i < chartDataList.length; i++)
				 {
					if (i == 0)
					{
						if (data.startTime < chartDataList[0].occurTime)
						{
							var startTime = parseDateTime(data.startTime);
							chartData.push({occurredTime:startTime});
						}
					}
					var column = chartDataList[i];
					if (column)
					{					
						var date = parseDateTime(column.occurTime);						
				        var firstValue = undefined;
				        var secondValue = undefined;
						
						if (column.connCurrValue > -1 && column.connCurrValue != null)
						{
							firstValue = column.connCurrValue;
							secondValue = column.preConnCurrValue;
						}
						var dataObject =
						{
							occurredTime : date,
							firstValue : firstValue,
							secondValue : secondValue
						};						
						chartData.push(dataObject);							
					}
					if (i == (chartDataList.length - 1))
					{
						if (data.endTime > chartDataList[chartDataList.length - 1].occurTime)
						{
							var endTime = parseDateTime(data.endTime);
							chartData.push({occurredTime:endTime});								 
						}
					}
				 }
			}
			else
			{
				var startTime = parseDateTime(data.startTime);
				var endTime = parseDateTime(data.endTime);
				var dataObject =
				{
					occurredTime : startTime
				};
				chartData.push(dataObject);
				var dataObject =
				{
					occurredTime : endTime
				};
				chartData.push(dataObject);
			}		
			var chartOption =
			{
				 min : 0,
				 max : null,
				 linecolor1 : "#6cb8c8",
				 linecolor2 : "gray",
				 linecolor3 : "#6cb8c8",
				 linecolor4 : "gray",
				 chartname : "MemberPerfConnChart",
				 axistitle : VAR_COMMON_AFEW,
				 axistitle1 : VAR_COMMON_BPS,
				 axistitle2 : VAR_COMMON_AFEW,
				 maxPos : null,
				 cursorColor : "#0e7023",
				 preValue : preVal,
				 connValue : connVal,
//				 selectedDate : searchDateTime
				 selectedDate : $('#compareDateTime').val(),
				 title : "Concurrent Sessions",
				 interval : interval
			};
			obchart.OBSvcPerfomanceConnChartViewerExtends(chartData, chartOption);
        }			
	},
	// Member bps Chart Data Get
	loadVsMemberHistoryInfo : function(memberIndex, rowIndex, memberIp, bpsVal, preVal)
	{
		this.selectedMemberIndex = memberIndex;
		this.selectedMemberIp = memberIp;
		with (this)
		{
			if (!memberIndex)
			{
				return;
			}
			var params = {
				"adcObject.category"			: 5,
				"adcObject.strIndex"			: memberIndex				
			};
			params['startTimeL'] = searchStartTime;
			params['endTimeL'] = searchEndTime;
			params['preCompare'] = preVal;
//			params['selectedTime'] = searchDateTime; 
			
			if(preVal == 3)
				params['selectedTime'] = $('#compareDateTime').val();
			else
				params['selectedTime'] = searchDateTime;  
			
			ajaxManager.runJsonExt({
				url			: "monitor/loadVsMemberHistoryInfo.action",
				data		: params,
				successFn	: function(data)
				{	
					intervalMonitor = data.intervalMonitor;
					GenerateMemberBpsChart(data, bpsVal, preVal, intervalMonitor);			
					loadVsMemberMaxAvgHistoryInfo(memberIndex, rowIndex, memberIp, bpsVal, preVal);							
//					selectMemberTableRow(memberIndex, undefined, rowIndex, undefined, memberIp);
				},
				completeFn	: function(data)
				{	
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_SVCPERFOM_MEMBERLOAD, jqXhr);
					selectMemberTableRow(memberIndex, undefined, rowIndex, undefined, memberIp);
				}
			});
		}		
	},	
	
	// member bps Avg/Max Data get
	loadVsMemberMaxAvgHistoryInfo : function(memberIndex, rowIndex, memberIp, bpsVal, preVal)
	{
		this.selectedMemberIndex = memberIndex;
		this.selectedMemberIp = memberIp;
		with (this)
		{
			if (!memberIndex)
			{
				return;
			}
			var params = {
				"adcObject.category"			: 5,
				"adcObject.strIndex"			: memberIndex				
			};
			params['startTimeL'] = searchStartTime;
			params['endTimeL'] = searchEndTime;
			params['preCompare'] = preVal;
//			params['selectedTime'] = searchDateTime;
			
			if(preVal == 3)
				params['selectedTime'] = $('#compareDateTime').val();
			else
				params['selectedTime'] = searchDateTime;  
			
			ajaxManager.runHtmlExt({
				url			: "monitor/loadVsMemberMaxAvgHistoryInfo.action",
				data		: params,
				target		: "tbody.bpsAvgMax",	
				successFn	: function(data)
				{									
					selectMemberTableRow(memberIndex, undefined, rowIndex, undefined, memberIp, bpsVal, preVal);
					
					registerEvent(bpsVal, preVal);
					registerConnEvent(bpsVal, preVal, 0);
				},
				completeFn	: function(data)
				{	
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_SVCPERFOM_LAOD, jqXhr);
					selectMemberTableRow(memberIndex, undefined, rowIndex, undefined, memberIp, bpsVal, preVal);
				}
			});
		}		
	},
	
	// Member bps Chart Data Get
	// TODO member conn 
	loadVsMemberConnHistoryInfo : function(memberIndex, rowIndex, memberIp, bpsVal, preVal, connVal)
	{
		this.selectedMemberIndex = memberIndex;
		this.selectedMemberIp = memberIp;
		with (this)
		{
			if (!memberIndex)
			{
				return;
			}
			var params = {
				"adcObject.category"			: 5,
				"adcObject.strIndex"			: memberIndex				
			};
			params['startTimeL'] = searchStartTime;
			params['endTimeL'] = searchEndTime;
			params['preCompare'] = preVal;
//			params['selectedTime'] = searchDateTime;
			
			if(preVal == 3)
				params['selectedTime'] = $('#compareDateTime').val();
			else
				params['selectedTime'] = searchDateTime;  
			
			ajaxManager.runJsonExt({
				url			: "monitor/loadVsMemberConnHistoryInfo.action",
				data		: params,
				successFn	: function(data)
				{					
					intervalMonitor = data.intervalMonitor;
					GenerateMemberConnChart(data, preVal, connVal, intervalMonitor);
					loadVsMemberConnMaxAvgHistoryInfo(memberIndex, rowIndex, memberIp, bpsVal, preVal, connVal);									
//					selectMemberTableRow(memberIndex, undefined, rowIndex, undefined, memberIp);
				},
				completeFn	: function(data)
				{	
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_SVCPERFOM_MEMBERLOAD, jqXhr);
//					selectMemberTableRow(memberIndex, undefined, rowIndex, undefined, memberIp);
				}
			});
		}		
	},
	
	// TODO member conn avg/max
	loadVsMemberConnMaxAvgHistoryInfo : function(memberIndex, rowIndex, memberIp, bpsVal, preVal, connVal)
	{
		this.selectedMemberIndex = memberIndex;
		this.selectedMemberIp = memberIp;
		with (this)
		{
			if (!memberIndex)
			{
				return;
			}
			var params = {
				"adcObject.category"			: 5,
				"adcObject.strIndex"			: memberIndex				
			};
			params['startTimeL'] = searchStartTime;
			params['endTimeL'] = searchEndTime;
			params['preCompare'] = preVal;
//			params['selectedTime'] = searchDateTime;
			
			if(preVal == 3)
				params['selectedTime'] = $('#compareDateTime').val();
			else
				params['selectedTime'] = searchDateTime;  
			
			ajaxManager.runHtmlExt({
				url			: "monitor/loadVsMemberConnMaxAvgHistoryInfo.action",
				data		: params,
				target		: "table.connAvgMax",	
				successFn	: function(data)
				{									
					selectMemberTableRow(memberIndex, undefined, rowIndex, undefined, memberIp, bpsVal, preVal);					
					registerConnEvent(bpsVal, preVal, connVal);
				},
				completeFn	: function(data)
				{	
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_SVCPERFOM_LAOD, jqXhr);
					selectMemberTableRow(memberIndex, undefined, rowIndex, undefined, memberIp, bpsVal, preVal);
				}
			});
		}		
	},
	
	/// Member bps Connection Chart Generate
	GenerateMemberBpsChart : function(data, value, preVal, interval)
	{
		with(this)
		{
			//var ConfigDate = [];		
			var chartData = [];
			var chartDataList = [];
			if (data.memberHistoryInfo.bpsConnData != null)
			{
				chartDataList = data.memberHistoryInfo.bpsConnData;
			}
			//var chartConfigList = data.vsConnectionInfo.vsConfigEventList;			
			if (chartDataList.length > 0 && chartDataList != null)
			{
				 for ( var i = 0; i < chartDataList.length; i++)
				 {
					if (i == 0)
					{
						if (data.startTime < chartDataList[0].occurTime)
						{
							var startTime = parseDateTime(data.startTime);
							chartData.push({occurredTime:startTime});
						}
					}
					var column = chartDataList[i];
					if (column)
					{					
						var date = parseDateTime(column.occurTime);						
				        var firstValue = undefined;
						var secondValue = undefined;
						
						if(value == 0)
						{
							if (column.bpsInValue > -1 && column.bpsInValue != null)
							{
								firstValue = column.bpsInValue;
								if(preVal != -1)
									secondValue = column.preBpsInValue;
							}
						}
						else if(value == 1)
						{
							if (column.bpsOutValue > -1 && column.bpsOutValue != null)
							{
								firstValue = column.bpsOutValue;
								if(preVal != -1)
									secondValue = column.preBpsOutValue;
							}
						}
						if(value == 2)
						{
							if (column.bpsTotValue > -1 && column.bpsTotValue != null)
							{
								firstValue = column.bpsTotValue;
								if(preVal != -1)
									secondValue = column.preBpsTotValue;
							}
						}
						var dataObject =
						{
							occurredTime : date,
							firstValue : firstValue,							
							secondValue : secondValue
						};						
						chartData.push(dataObject);							
					}
					if (i == (chartDataList.length - 1))
					{
						if (data.endTime > chartDataList[chartDataList.length - 1].occurTime)
						{
							var endTime = parseDateTime(data.endTime);
							chartData.push({occurredTime:endTime});								 
						}
					}
				 }
			}
			else
			{
				var startTime = parseDateTime(data.startTime);
				var endTime = parseDateTime(data.endTime);
				var dataObject =
				{
					occurredTime : startTime
				};
				chartData.push(dataObject);
				var dataObject =
				{
					occurredTime : endTime
				};
				chartData.push(dataObject);
			}			
			// Config Event, Array를 따로 넘길때.			
			/*if (chartConfigList.length > 0 && chartDataList != null)
			{
				for ( var i = 0; i < chartConfigList.length; i++)
				{		
					var column = chartConfigList[i];
					if (column)
					{
						var date = parseDateTime(column.occurredTime);	
						ConfigDate.push(date);
					}
				}
			}*/		
			
			/* 
			 * IE8에서는 map() 지원하지 않음 
			 * The solution is jQuery.map
			 * a.map(function( ) { }); -> jQuery.map(a, function( ) { //what ever you want todo .. }
			 */
//			var maxPos = Math.max.apply(null,maxData.map(function(o){return o.value;})); 			
			var chartOption =
			{
				 min : 0,
				 max : null,
				 linecolor1 : "#6cb8c8",
				 linecolor2 : "gray",
				 linecolor3 : "#6cb8c8",
				 linecolor4 : "gray",
				 chartname : "MemberPerfChart",
				 axistitle1 : VAR_COMMON_BPS,
				 axistitle2 : VAR_COMMON_AFEW,
				 maxPos : null,
				 cursorColor : "#0f47c7",
				 bpsValue : value,
				 preValue : preVal,
				 selectedDate : $('#compareDateTime').val(),
				 interval : interval
			};
			obchart.OBSvcPerfomanceBpsChartViewerExtends(chartData, chartOption);
			
			var chartOption =
			{
				 min : 0,
				 max : null,
				 linecolor1 : "#6cb8c8",
				 linecolor2 : "gray",
				 linecolor3 : "#6cb8c8",
				 linecolor4 : "gray",
				 chartname : "MemberPerfConnChart",
				 axistitle : VAR_COMMON_AFEW,
				 axistitle1 : VAR_COMMON_BPS,
				 axistitle2 : VAR_COMMON_AFEW,
				 maxPos : null,
				 cursorColor : "#0e7023",
				 preValue : preVal,
				 connValue : connVal,
//					 selectedDate : searchDateTime
				 selectedDate : $('#compareDateTime').val(),
				 title : "Concurrent Sessions",
				 interval : interval
			};
			obchart.OBSvcPerfomanceConnChartViewerExtends(chartData, chartOption);
        }			
	},
	GenerateMemberConnChart : function(data, interval)
	{
		with(this)
		{
			//var ConfigDate = [];		
			var chartData = [];
			var chartDataList = [];
			if (data.memberHistoryInfo.bpsConnData != null)
			{
				chartDataList = data.memberHistoryInfo.bpsConnData;
			}
			//var chartConfigList = data.vsConnectionInfo.vsConfigEventList;			
			if (chartDataList.length > 0 && chartDataList != null)
			{
				 for ( var i = 0; i < chartDataList.length; i++)
				 {
					if (i == 0)
					{
						if (data.startTime < chartDataList[0].occurTime)
						{
							var startTime = parseDateTime(data.startTime);
							chartData.push({occurredTime:startTime});
						}
					}
					var column = chartDataList[i];
					if (column)
					{					
						var date = parseDateTime(column.occurTime);						
				        var firstValue = undefined;
						var secondValue = undefined;
						
						if (column.connCurrValue > -1 && column.connCurrValue != null)
						{
							firstValue = column.connCurrValue;
							secondValue = column.preConnCurrValue;
						}
						var dataObject =
						{
							occurredTime : date,
							firstValue : firstValue,
							secondValue : secondValue
						};			
						chartData.push(dataObject);							
					}
					if (i == (chartDataList.length - 1))
					{
						if (data.endTime > chartDataList[chartDataList.length - 1].occurTime)
						{
							var endTime = parseDateTime(data.endTime);
							chartData.push({occurredTime:endTime});								 
						}
					}
				 }
			}
			else
			{
				var startTime = parseDateTime(data.startTime);
				var endTime = parseDateTime(data.endTime);
				var dataObject =
				{
					occurredTime : startTime
				};
				chartData.push(dataObject);
				var dataObject =
				{
					occurredTime : endTime
				};
				chartData.push(dataObject);
			}			
			// Config Event, Array를 따로 넘길때.			
			/*if (chartConfigList.length > 0 && chartDataList != null)
			{
				for ( var i = 0; i < chartConfigList.length; i++)
				{		
					var column = chartConfigList[i];
					if (column)
					{
						var date = parseDateTime(column.occurredTime);	
						ConfigDate.push(date);
					}
				}
			}*/		
			
			/* 
			 * IE8에서는 map() 지원하지 않음 
			 * The solution is jQuery.map
			 * a.map(function( ) { }); -> jQuery.map(a, function( ) { //what ever you want todo .. }
			 */
//			var maxPos = Math.max.apply(null,maxData.map(function(o){return o.value;})); 			
			var chartOption =
			{
				 min : 0,
				 max : null,
				 linecolor1 : "#6cb8c8",
				 linecolor2 : "gray",
				 linecolor3 : "#6cb8c8",
				 linecolor4 : "gray",
				 chartname : "MemberPerfConnChart",
				 axistitle : VAR_COMMON_AFEW,
				 axistitle1 : VAR_COMMON_BPS,
				 axistitle2 : VAR_COMMON_AFEW,
				 maxPos : null,
				 cursorColor : "#0e7023",
				 preValue : preVal,
				 connValue : connVal,
//					 selectedDate : searchDateTime
				 selectedDate : $('#compareDateTime').val(),
				 title : "Concurrent Sessions",
				 interval : interval
			};
			obchart.OBSvcPerfomanceConnChartViewerExtends(chartData, chartOption);
        }			
	},
	noticePageInfo : function()
	{
		with(this)
		{
			var currentPage = pageNavigator.getCurrentPage();
			var lastPage = pageNavigator.getLastPage();
			var countTotal = pageNavigator.getRowTotal();
			var targetCntHtml = $('.noticePageCountInfo').filter(':last');
			var targetPageHtml = $('.noticePageInfo').filter(':last');
			targetCntHtml.html(addThousandSeparatorCommas(countTotal));
			targetPageHtml.html("(" + addThousandSeparatorCommas(currentPage) + "/" + addThousandSeparatorCommas(lastPage) + VAR_COMMON_PAGE + ")");
		}
	},
	itemCompare : function(bpsVal)
	{
		var itemName = "";
		var $tbody = $('.contents_area #itemChange').filter(':last');	
		if(bpsVal == 0)
			itemName = "bps In";
		else if(bpsVal == 1)
			itemName = "bps Out";
		else if(bpsVal == 2)
			itemName = "bps Total";
	
		$tbody.empty();	
		var html = '';		
		html += itemName;		
		$tbody.html(html);		
	},	
	preCompare : function(preVal)
	{
		var itemName = "";
		var $tbody = $('.contents_area #compareChange').filter(':last');	
		if(preVal == 0)
			itemName = VAR_SVCPERFOM_PREDAY;
		else if(preVal == 1)
			itemName = VAR_SVCPERFOM_PREWEEKS;
		else if(preVal == 2)
			itemName = VAR_SVCPERFOM_PREMONTH;
		else if(preVal == 3)
			itemName = $('#compareDateTime').val();
		else 
			itemName = VAR_SVCPERFOM_PREDAY;			
	
		$tbody.empty();	
		var html = '';		
		html += itemName;		
		$tbody.html(html);		
	},
	vsNameChanger: function(vsName, vsIp)
	{
		var lisetHeaderName = "";
		if(vsName != "")
		{
			lisetHeaderName = vsName;
		}
		else
		{
			lisetHeaderName = vsIp;
		}
		var $tbody = $('.contents_area #vsNameChange');//.filter(':last');		
		$tbody.empty();	
		var html = '';		
		html += lisetHeaderName;		
		$tbody.html(html);		
	}
});