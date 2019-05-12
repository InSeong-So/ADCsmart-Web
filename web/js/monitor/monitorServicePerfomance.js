var MonitorServicePerfomance = Class.create({
	initialize : function()
	{
		this.adc;
		this.selectedVsIndex;	// Alteon일 경우엔 selectSvcIndex, Name, Port
		this.selectedVsName;	// Alteon일 경우엔 selectSvcIndex, Name, Port
		this.selectedPort;		// Alteon일 경우엔 selectSvcIndex, Name, Port		
		this.searchStartTime = undefined;
		this.searchEndTime = undefined;
		this.RealTimeBpsConnChart = undefined;
		this.selectedSearchTimeMode = undefined;
		this.selectedChartTapMode = undefined;	// responsChartTap, bpsConnChartTap
		this.refreshIntervalSeconds = 5;
		this.realTimeTimer = undefined;
		this.realTimeTimer1 = undefined;
		this.pageNavigator = new PageNavigator({ rowCountPerPage : 10 });
		this.orderDir = 2; // 내림차순 = 2
        this.orderType = undefined; // 9NINTH = 41
        this.prevInfo = undefined; // 실시간 Chart 두번째 Call 부터 보내는 Row 데이터
        this.owner = undefined;		
		this.realsvr = new MonitorServicePerfomanceRealServer();
		this.realsvr.owner = this;
        
        this.selectedVsNameKey = false;		// Seleted 된 vs 의 Member Page Load를 위한 Key
        var _self = this;
		this.pageNavigator.onChange(function(fromRow, toRow, orderType, orderDir) {
			_self.selectedVsIndex = undefined;
			_self.selectedPort = undefined;
			_self.loadSvcPerfInfoList(fromRow, toRow, orderType, orderDir);
		});
		this.searchFlag=false;
		this.bpsVal = 2;
		this.searchDateTime = undefined;
		this.preVal = -1;
		this.connVal = undefined;
		this.responseVal = undefined;
	},
	onAdcChange : function()
	{
		with(this)
		{
			selectedChartTapMode = "bpsConnChartTap";
//			preVal = 0;
			searchDateTime = undefined;
//			this.bpsVal = 2;
//			this.preVal = -1;
			loadServicePerfomanceContent();
		}
	},
	// 초기 페이지 Open
	loadContent : function(adcInfo, index, port)
	{
		if (adcSetting.getIsFlb() == 1 && header.getMonitorLbTap() == 2)
		{
			//TODO
			flbGroupPerfomance.loadGroupPerfomanceContent();
			return;
		}
		else
		{
			header.setMonitorLbTap(1);
		}
		if (index)
		{
			this.selectedVsIndex = index;
			this.selectedPort = port;
		}
//		this.adc = adcSetting.getAdc();
		with (this)
		{
			adc = adcInfo;
			selectedVsNameKey = false;
			if (!adc)
			{
				return;
			}
			var params = 
			{
					"adcObject.index" 		: adc.index,					
					"adcObject.category"	: adc.categoryIndex,
					"adcObject.desciption"  : adc.type,
					"adc.isFlb" : adcSetting.getIsFlb()
			};
			ajaxManager.runHtmlExt({
				url : "monitor/loadServicePerfomanceContent.action",
				data : params,
				target : "#wrap .contents",				
				successFn : function(data)
				{
					showhideBtn();
//					header.setActiveMenu('MonitorServicePerfomance');
					registServicePerfomanceContentEventsNew(adc);
					
					if (adcStatus === "available") 
					{
						restoreServicePerfomanceContent();
						updateNavigator(adc, index, port);
					}			
					else
					{
											
						if ($('.svcTrafficInfoList').size() > 0 )
						{
							restoreServicePerfomanceContent();
						}
						else
						{						
							$('.contents_area .nulldata').removeClass("none");
							if(langCode=="ko_KR")
							{
								$('.contents_area .nulldata').css('background-image', "url(imgs/monitoring/servicePerfomance_null.gif)");
							}
							else
							{
								$('.contents_area .nulldata').css('background-image', "url(imgs/monitoring_eng/servicePerfomance_null.gif)");
							}
							
							$('.contents_area .successdata').addClass("none");
						}
					}
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_SVCPERFOM_LAOD, jqXhr);
				}
			});
		}
	},
	
	loadServicePerfomanceContent : function(index, port, selDateTime, vsIp, flag)
	{
		if (adcSetting.getIsFlb() == 1 && header.getMonitorLbTap() == 2)
		{
			//TODO
			flbGroupPerfomance.loadGroupPerfomanceContent();
			return;
		}
		else
		{
			header.setMonitorLbTap(1);
		}
		if (index)
		{
			this.selectedVsIndex = index;
			this.selectedPort = port;
		}
		this.adc = adcSetting.getAdc();
		with (this)
		{
			selectedVsNameKey = false;
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
				url : "monitor/loadServicePerfomanceContent.action",
				data : params,
				target : "#wrap .contents",				
				successFn : function(data)
				{
					showhideBtn();
					header.setActiveMenu('MonitorServicePerfomance');
					noticePageInfo();
					registServicePerfomanceContentEvents(adc, selDateTime, vsIp, flag);
					restoreServicePerfomanceContent();
					updateNavigator(adc, index, port);
					
//					if (adcStatus === "available") 
//					{
//						restoreServicePerfomanceContent();
//						updateNavigator(adc, index, port);
//					}			
//					else
//					{
//						if ($('.svcTrafficInfoList').size() > 0 )
//						{
//							alert($('.svcTrafficInfoList').size());
//							restoreServicePerfomanceContent();
//							
//						}
//						else
//						{						
//							$('.contents_area .nulldata').removeClass("none");
//							$('.contents_area .nulldata').css('background-image', "url(imgs/monitoring/servicePerfomance_null.gif)");
//							$('.contents_area .successdata').addClass("none");
//						}
//					}
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_SVCPERFOM_LAOD, jqXhr);
				}
			});
		}
	},
	// Total 갯수 Get, Navigator 에 업데이트
	updateNavigator : function(adc, index, port) 
	{
		with (this)
		{
			if (!adc)
			{
				return;
			}			
			var params = {
				"adcObject.index" 		: adc.index,					
				"adcObject.category"	: adcSetting.getSelectIndex(),
				"searchOption.searchKey": $('#svcPerfSearchKey').val()
			};			
			var _self = this;
			ajaxManager.runJsonExt
			({
				url			: "monitor/retrieveSvcPerfInfoTotalCount.action",
				data		: params,
				successFn	: function(data)
				{
					var totalCount = 0;
					if (data.totalCount)
					{
						totalCount = data.totalCount;
					}					
					//$('.vsTotalCount').text("Total: " + totalCount + "개");
					pageNavigator.updateRowTotal(totalCount);
					if (index)
					{
						this.selectedVsIndex = index;
						this.selectedPort = port;
					}
					else
					{
						_self.selectedVsIndex = undefined;
						_self.selectedPort = undefined;		
					}								
					loadSvcPerfInfoList();
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_SVCPERFOM_LAOD, jqXhr);
//					exceptionEvent();
				}
			});
		}
	},
	// Table 데이터 성능 정보 Get
	loadSvcPerfInfoList : function(fromRow, toRow, orderType, orderDir)
	{
		with (this) 
		{
			if (!adc) 
			{
				return;
			} 
			var URL = "";
			// Alteon 과 F5 는 load 되는 HTML (ftl) 웹페이지가 다르기 때문에 java 메서드 호출은 같더라도,
			// Action을 달리 하여 구분한다.
			if (adc.type == "Alteon")
			{
				URL = "monitor/loadAlteonSvcPerfInfoList.action";				
			}
			else
			{
				URL = "monitor/loadSvcPerfInfoList.action";				
			}
			if (!orderType)
			{
				orderType = 46; // 초기 페이지는 CONN_CURR 로 초기화
			}
						
			var params = 
			{
				"adcObject.category"			: adcSetting.getSelectIndex(),
				"adcObject.index"				: adc.index,
				"adcObject.name"				: adc.name,
				"orderOption.orderType"			: orderType,
				"orderOption.orderDirection"	: orderDir,
				"searchOption.searchKey"		: $('#svcPerfSearchKey').val()
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
					registSvcPerfInfoListEvents(adc);
					pageNavigator.refresh();
					
					//orderType = undefined;
					var vsIndex = _self.selectedVsIndex;	
					var vsName = _self.selectedVsName;
					var port = _self.selectedPort;
					if (!_self.selectedVsIndex)// 선택된게 없을때 맨처음껄 선택한다.
					{
						vsIndex = $('#svc_table tbody tr').eq(0).attr('id');
						port = $('#svc_table tbody tr').eq(0).find('.vsPort').text().replace(",", "");
						vsName = $('#svc_table tbody tr').eq(0).find('.vsName').text();
					}					
					this.selectedVsIndex = vsIndex;
					this.selectedPort = port;
					if (selectedSearchTimeMode == "realTimeMode")
					{
						var categoryIndex = adcSetting.getSelectIndex();
						// 실시간일경우에는 자동으로 bps/Connection 으로 선택되도록 한다.
//						alert("bps/Connection History나 응답시간 History 데이터를 원하실 경우\n" +
//								"조회기간을 변경하시기 바랍니다.");
//						if (selectedChartTapMode != 'bpsConnChartTap')
//						{
//							$.obAlertNotice(VAR_SVCPERFOM_REALRESPONSENOTSUPPORT);
//							selectedChartTapMode = "bpsConnChartTap";
//						}
						
						$('.contents_area #bpsConnChartTap').css('background-color', '#666666');
						$('.contents_area #bpsConnChartTap').css('color', 'white');
						$('.contents_area #responsChartTap').css('background-color', 'white');
						$('.contents_area #responsChartTap').css('color', '#000000');
						
						$('.compareChk').addClass('none');
//						this.selectedChartTapMode = "bpsConnChartTap";
						
						if (adcSetting.getAdcStatus() != "available") 
						{						
							if ($('.svcTrafficInfoList').size() > 0 )
							{
								$('#realTime').attr("disabled", "disabled");
								displayRealTimeBpsConn(categoryIndex, adc, vsIndex, port, undefined, vsName, bpsVal, true);
								loadBpsMaxAvgHistoryInfo(vsIndex, port, undefined, vsName, bpsVal, preVal);			
//								loadBpsConnHistoryInfo(vsIndex, port, undefined, vsName, bpsVal, preVal);

								
//								if (selectedChartTapMode == 'connChartTap')
								if($('input[name="concurrSessionValChk"]').is(':checked') == true)
								{
									displayRealTimeConn(categoryIndex, adc, vsIndex, port, undefined, vsName, 0, false);
//									loadBpsMaxAvgHistoryInfo(vsIndex, port, undefined, vsName, bpsVal, preVal);		
									loadConnMaxAvgHistoryInfo(vsIndex, port, undefined, vsName, bpsVal, preVal, connVal);
//									loadConnHistoryInfo(vsIndex, port, undefined, vsName, bpsVal, preVal, connVal);
								}
								
								$('.searchNotMsg').addClass("none");
								$('.dataNotExistMsg').addClass("none");
								if($('.servicePerformList').size() > 0)
								{
									$('.nulldataMsg').addClass("none");
								}
								else
								{
									$('.nulldataMsg').removeClass("none");
								}
							}
							else
							{						
								$('.contents_area .nulldata').removeClass("none");
								if(langCode=="ko_KR")
								{
									$('.contents_area .nulldata').css('background-image', "url(imgs/monitoring/servicePerfomance_null.gif)");
								}
								else
								{
									$('.contents_area .nulldata').css('background-image', "url(imgs/monitoring_eng/servicePerfomance_null.gif)");
								}
								$('.contents_area .successdata').addClass("none");
							}
						}
						else
						{
							displayRealTimeBpsConn(categoryIndex, adc, vsIndex, port, undefined, vsName, bpsVal, true);
							loadBpsMaxAvgHistoryInfo(vsIndex, port, undefined, vsName, bpsVal, preVal);	
//							loadBpsConnHistoryInfo(vsIndex, port, undefined, vsName, bpsVal, preVal);
							
							if($('input[name="concurrSessionValChk"]').is(':checked') == true)
							{
								displayRealTimeConn(categoryIndex, adc, vsIndex, port, undefined, vsName, 0, false);
//								loadBpsMaxAvgHistoryInfo(vsIndex, port, undefined, vsName, bpsVal, preVal);
								loadConnMaxAvgHistoryInfo(vsIndex, port, undefined, vsName, bpsVal, preVal, connVal);
//								loadConnHistoryInfo(vsIndex, port, undefined, vsName, bpsVal, preVal, connVal);
							}
//							if (selectedChartTapMode == 'connChartTap')
//							{
//								displayRealTimeBpsConn(categoryIndex, adc, vsIndex, port, undefined, vsName, 4);
//							}
						}
					}
					else
					{
						if (selectedChartTapMode == 'bpsConnChartTap')
						{
							if (adcSetting.getAdcStatus() != "available") 
							{						
								if ($('.servicePerformList').size() > 0 )
								{
									$('#realTime').attr("disabled", "disabled");
									loadBpsConnHistoryInfo(vsIndex, port, undefined, vsName, bpsVal, preVal);
									
									$('.searchNotMsg').addClass("none");
									$('.dataNotExistMsg').addClass("none");
									
//									$('.defaultItem').addClass('none');
//									if($('.servicePerformList').size() > 0)
//									{
//										$('.nulldataMsg').addClass("none");
//									}
//									else
//									{
//										$('.nulldataMsg').removeClass("none");
//										$('.contents_area .nulldata').css('background-image', "url(imgs/monitoring/servicePerfomance_null.gif)");
//									}
								}
								else
								{	
									$('.contents_area .nulldata').removeClass("none");
									if(langCode=="ko_KR")
									{
										$('.contents_area .nulldata').css('background-image', "url(imgs/monitoring/servicePerfomance_null.gif)");
									}
									else
									{
										$('.contents_area .nulldata').css('background-image', "url(imgs/monitoring_eng/servicePerfomance_null.gif)");
									}
									$('.contents_area .successdata').addClass("none");
								}
							}
							else
							{	
								if ($('.svcTrafficInfoList').size() < 0 )
								{
									$('.contents_area .nulldata').removeClass("none");
									if(langCode=="ko_KR")
									{
										$('.contents_area .nulldata').css('background-image', "url(imgs/monitoring/servicePerfomance_null.gif)");
									}
									else
									{
										$('.contents_area .nulldata').css('background-image', "url(imgs/monitoring_eng/servicePerfomance_null.gif)");
									}
									$('.contents_area .successdata').addClass("none");
								}
								loadBpsConnHistoryInfo(vsIndex, port, undefined, vsName, bpsVal, preVal);
							}
						}
//						else if (selectedChartTapMode == 'connChartTap')
						if($('input[name="concurrSessionValChk"]').is(':checked') == true)							
						{
							if (adcSetting.getAdcStatus() != "available") 
							{						
								if ($('.svcTrafficInfoList').size() > 0 )
								{
									$('#realTime').attr("disabled", "disabled");
									connVal = 0;
									loadConnHistoryInfo(vsIndex, port, undefined, vsName, bpsVal, preVal, connVal);
									
									$('.searchNotMsg').addClass("none");
									$('.dataNotExistMsg').addClass("none");
									if($('.servicePerformList').size() > 0)
									{
										$('.nulldataMsg').addClass("none");
									}
									else
									{
										$('.nulldataMsg').removeClass("none");
										if(langCode=="ko_KR")
										{
											$('.contents_area .nulldata').css('background-image', "url(imgs/monitoring/servicePerfomance_null.gif)");
										}
										else
										{
											$('.contents_area .nulldata').css('background-image', "url(imgs/monitoring_eng/servicePerfomance_null.gif)");
										}
									}
								}
								else
								{						
									$('.contents_area .nulldata').removeClass("none");
									if(langCode=="ko_KR")
									{
										$('.contents_area .nulldata').css('background-image', "url(imgs/monitoring/servicePerfomance_null.gif)");
									}
									else
									{
										$('.contents_area .nulldata').css('background-image', "url(imgs/monitoring_eng/servicePerfomance_null.gif)");
									}
									$('.contents_area .successdata').addClass("none");
								}
							}
							else
							{
								if ($('.svcTrafficInfoList').size() < 0 )
								{
									$('.contents_area .nulldata').removeClass("none");
									if(langCode=="ko_KR")
									{
										$('.contents_area .nulldata').css('background-image', "url(imgs/monitoring/servicePerfomance_null.gif)");
									}
									else
									{
										$('.contents_area .nulldata').css('background-image', "url(imgs/monitoring_eng/servicePerfomance_null.gif)");
									}
									$('.contents_area .successdata').addClass("none");
								}
								connVal = 0;
								loadConnHistoryInfo(vsIndex, port, undefined, vsName, bpsVal, preVal, connVal);
							}
						}
						if ($('input[name="responseValChk"]').is(':checked') == true)	
						{
							if (adcSetting.getAdc().type == "PAS" || adcSetting.getAdc().type == "PASK")
							{
								// PAS, PASK 는 응답시간을 미지원하기때문에 bps 탭을 다시 Click 한다.
								selectedChartTapMode = "bpsConnChartTap";
								$('.contents_area #bpsConnChartTap').css('background-color', '#666666');
								$('.contents_area #bpsConnChartTap').css('color', 'white');
								$('.contents_area #responsChartTap').css('background-color', 'white');
								$('.contents_area #responsChartTap').css('color', '#000000');						
							}
							else
							{
								if (adcSetting.getAdcStatus() != "available") 
								{						
									if ($('.svcTrafficInfoList').size() > 0 )
									{
										responseVal = 1;
										$('#realTime').attr("disabled", "disabled");
										loadResponseTimeHistoryInfo(vsIndex, port, undefined, vsName, bpsVal, preVal, responseVal);
										
										$('.searchNotMsg').addClass("none");
										$('.dataNotExistMsg').addClass("none");
										if($('.servicePerformList').size() > 0)
										{
											$('.nulldataMsg').addClass("none");
										}
										else
										{
											$('.nulldataMsg').removeClass("none");
											if(langCode=="ko_KR")
											{
												$('.contents_area .nulldata').css('background-image', "url(imgs/monitoring/servicePerfomance_null.gif)");
											}
											else
											{
												$('.contents_area .nulldata').css('background-image', "url(imgs/monitoring_eng/servicePerfomance_null.gif)");
											}
										}
									}
									else
									{						
										$('.contents_area .nulldata').removeClass("none");
										if(langCode=="ko_KR")
										{
											$('.contents_area .nulldata').css('background-image', "url(imgs/monitoring/servicePerfomance_null.gif)");
										}
										else
										{
											$('.contents_area .nulldata').css('background-image', "url(imgs/monitoring_eng/servicePerfomance_null.gif)");
										}
										$('.contents_area .successdata').addClass("none");
									}
								}
								else
								{
									responseVal = 1;
									loadResponseTimeHistoryInfo(vsIndex, port, undefined, vsName, bpsVal, preVal, responseVal);
								}
							}							
						}						
					}	
					
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_SVCPERFOM_LAOD, jqXhr);
//					exceptionEvent();
				}
			});
		}		
	},
	
	restoreServicePerfomanceContent : function() 
	{
		with (this)
		{
			if (!selectedChartTapMode)
			{
				selectedChartTapMode = "bpsConnChartTap";
			}
			if(selectedChartTapMode == "responsChartTap")
			{
				$('#responsChartTap').css('background-color', '#666666');
				$('#responsChartTap').css('color', 'white');
				$('#bpsConnChartTap').css('background-color', 'white');
			}
			if(selectedChartTapMode == "bpsConnChartTap")
			{
				$('#bpsConnChartTap').css('background-color', '#666666');
				$('#bpsConnChartTap').css('color', 'white');
				$('#responsChartTap').css('background-color', 'white');
			}
			// 초기 TimeMode(존재하지 않는다면) 는 기간,시간 모드로 설정
			if (!selectedSearchTimeMode)
			{
				selectedSearchTimeMode = 'periodMode';
			}
			else
			{
				if(selectedSearchTimeMode == "periodMode")
				{				
					$('#realtime_btn').removeClass("inputText_realtime");
					$('#realtime_btn').addClass("inputText_realtime_off");
					$('#reservationtime').removeClass("inputText_calendar_off");
					$('#reservationtime').addClass("inputText_calendar");
				}
				else
				{
					$('#realtime_btn').removeClass("inputText_realtime_off");
					$('#realtime_btn').addClass("inputText_realtime");
					$('#reservationtime').removeClass("inputText_calendar");
					$('#reservationtime').addClass("inputText_calendar_off");
				}
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

			// 리뉴얼 DatePicker + Timepicker
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
										
					$('#selDate').attr('checked','checked');
					preVal = 3;
					
					if($('input[name="bpsValChk"]').is(':checked') == true)							
					{
						loadBpsConnHistoryInfo(selectedVsIndex, selectedPort, undefined, selectedVsName, bpsVal, preVal);
					}
					if($('input[name="concurrSessionValChk"]').is(':checked') == true)							
					{
						connVal = 0;
						loadConnHistoryInfo(selectedVsIndex, selectedPort, undefined, selectedVsName, bpsVal, preVal, connVal);
					}
					if ($('input[name="responseValChk"]').is(':checked') == true)	
					{
						responseVal = 1;
						loadResponseTimeHistoryInfo(selectedVsIndex, selectedPort, undefined, selectedVsName, bpsVal, preVal, responseVal);
					}
				}
			});
		}
	},
	// 초기페이지 로드 후 기본 form 이벤트 (검색조건, Refresh, 내보내기 Btn) 
	registServicePerfomanceContentEventsNew : function (adc)
	{
		with(this)
		{
			$('.flbGroupLnk').click(function(e) 
			{
				e.preventDefault();
//				header.setMonitorLbTap(2);
				flbGroupPerfomance.loadGroupPerfomanceContent();
			});			
			
			$('#responsChartTap').click(function() 
			{
				$(this).css('background-color', '#666666');
				$(this).css('color', 'white');
				$('#bpsConnChartTap').css('background-color', 'white');
				$('#bpsConnChartTap').css('color', '#000000');
				selectedChartTapMode = "responsChartTap";
				loadSvcPerfInfoList();
			});
			$('#bpsConnChartTap').click(function() 
			{			
				$(this).css('background-color', '#666666');
				$(this).css('color', 'white');
				$('#responsChartTap').css('background-color', 'white');
				$('#responsChartTap').css('color', '#000000');
				selectedChartTapMode = "bpsConnChartTap";
				loadSvcPerfInfoList();
			});
			
			$('#refresh').click(function(event) 
			{
				clearRealTimer();
				clearConnRealTimer();
				event.preventDefault();
				showhideBtn();
				loadSvcPerfInfoList();
			});
			$('#exportCssLnk').click(function(event) 
			{
				checkExportSvcPerfDataExistNew();
			});
		}
	},
	// 초기페이지 로드 후 기본 form 이벤트 (검색조건, Refresh, 내보내기 Btn) 
	registServicePerfomanceContentEvents : function (adc, selDateTime, vsIp, flag)
	{
		with(this)
		{
			$('.flbGroupLnk').click(function(e) 
			{
				e.preventDefault();
				header.setMonitorLbTap(2);
				flbGroupPerfomance.loadGroupPerfomanceContent();
			});			
			
			$('#responsChartTap').click(function() 
			{
				$(this).css('background-color', '#666666');
				$(this).css('color', 'white');
				$('#bpsConnChartTap').css('background-color', 'white');
				$('#bpsConnChartTap').css('color', '#000000');
				selectedChartTapMode = "responsChartTap";
				loadSvcPerfInfoList();
			});
			$('#bpsConnChartTap').click(function() 
			{				
				$(this).css('background-color', '#666666');
				$(this).css('color', 'white');
				$('#responsChartTap').css('background-color', 'white');
				$('#responsChartTap').css('color', '#000000');
				selectedChartTapMode = "bpsConnChartTap";
				loadSvcPerfInfoList();
			});
			// DatePick Tab Click
			$('#reservationtime').click(function(event) 
			{
				event.preventDefault();											
				selectedSearchTimeMode = "periodMode";
				$(this).removeClass("inputText_calendar_off");
				$(this).addClass("inputText_calendar");
				$('#realtime_btn').removeClass("inputText_realtime");
				$('#realtime_btn').addClass("inputText_realtime_off");
				
				$('#responseChk').removeAttr("disabled");
				$('.preCompare').removeAttr("disabled");
			});
			
			// RealTime Tab Click
			$('#realtime_btn').click(function(event) 
			{
				event.preventDefault();											
				selectedSearchTimeMode = $(this).val();
				$(this).removeClass("inputText_realtime_off");
				$(this).addClass("inputText_realtime");
				$('#reservationtime').removeClass("inputText_calendar");
				$('#reservationtime').addClass("inputText_calendar_off");
								
				$('#responseChk').attr("disabled", "disabled");	
								
				$('#responseChk').attr("checked", false);
				$('.responseChart').addClass('none');
				$('.preCompare').attr("disabled", true);
				selectedChartTapMode = 'bpsConnChartTap';
			});
			
			$('#refresh').click(function(event) 
			{			
				clearRealTimer();
				clearConnRealTimer();
				event.preventDefault();
				showhideBtn();
				loadSvcPerfInfoList();
			});
			
			$('.selectBps').click(function(e) 
			{
				selectedChartTapMode = "bpsConnChartTap";
				bpsVal = $(this).val();
//				if (bpsVal == 0)	// 전체 선택
//				{
////					loadSvcPerfInfoList();
//					$('.bpsInData').removeClass('none');
//					$('.bpsOutData').addClass('none');
//					$('.bpsTotData').addClass('none');
//				}
//				else if (bpsVal == 1) // 그룹 선택
//				{
////					loadSvcPerfInfoList();
//					$('.bpsInData').addClass('none');
//					$('.bpsOutData').removeClass('none');
//					$('.bpsTotData').addClass('none');
//				}
//				else if (bpsVal == 2) // 개별 선택
//				{
////					loadSvcPerfInfoList();
//					$('.bpsInData').addClass('none');
//					$('.bpsOutData').addClass('none');
//					$('.bpsTotData').removeClass('none');
//				}
//				else
//				{					
//				}
				
//				loadSvcPerfInfoList();
				if($('input[name="bpsValChk"]').is(':checked') == true)							
				{
					loadBpsConnHistoryInfo(selectedVsIndex, selectedPort, undefined, selectedVsName, bpsVal, preVal);
				}
				if($('input[name="concurrSessionValChk"]').is(':checked') == true)							
				{
					connVal = 0;
					loadConnHistoryInfo(selectedVsIndex, selectedPort, undefined, selectedVsName, bpsVal, preVal, connVal);
				}
				if ($('input[name="responseValChk"]').is(':checked') == true)	
				{
					responseVal = 1;
					loadResponseTimeHistoryInfo(selectedVsIndex, selectedPort, undefined, selectedVsName, bpsVal, preVal, responseVal);
				}
				itemCompare(bpsVal);
			});
			
			$('.preCompare').click(function(e) 
			{
				selectedChartTapMode = "bpsConnChartTap";
				selectedSearchTimeMode = 'periodMode';
				preVal = $(this).val();
				if (preVal == 0)	// 전일 선택
				{		
					searchDateTime = undefined;
					$('.compareChk').removeClass('none');
					
					var defaultDate = moment().format('YYYY-MM-DD');
					
					$("input[name='startTime']").val(getCompareDate(defaultDate, 1));
					
					
//					var currentDate = new Date(defaultDate);
//					var preDay = date.getDate();
//					currentDate.setDate(preDay - 1);
					
					
//					alert(currentDate + "::::::::::" + currentDate.getFullYear() + "-" + (currentDate.getMonth()+1) + "-" + currentDate.getDate());
//					alert(new Date(Date.parse(new Date()) - 1 * 1000 * 60 * 60 * 24)); //하루전
//					alert(defaultDate - 1 * 1000 * 60 * 60 * 24);
//					$("input[name='startTime']").val('2015-08-10');
				}
				else if (preVal == 1) // 전주 선택
				{
					searchDateTime = undefined;
					$('.compareChk').removeClass('none');
					
					$("input[name='startTime']").val(getCompareDate(moment().format('YYYY-MM-DD'), 7));
					
//					$("input[name='startTime']").val('2015-08-04');
				}
				else if (preVal == 2) // 전월 선택
				{
					searchDateTime = undefined;
					$('.compareChk').removeClass('none');
					
					$("input[name='startTime']").val(getCompareDate(moment().format('YYYY-MM-DD'), 30));
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
				
//				loadSvcPerfInfoList();
//				loadBpsConnHistoryInfo(selectedVsIndex, selectedPort, undefined, selectedVsName, bpsVal, preVal);
				if($('input[name="bpsValChk"]').is(':checked') == true)							
				{
					loadBpsConnHistoryInfo(selectedVsIndex, selectedPort, undefined, selectedVsName, bpsVal, preVal);
				}
				if($('input[name="concurrSessionValChk"]').is(':checked') == true)							
				{
					connVal = 0;
					loadConnHistoryInfo(selectedVsIndex, selectedPort, undefined, selectedVsName, bpsVal, preVal, connVal);
				}
				if ($('input[name="responseValChk"]').is(':checked') == true)	
				{
					responseVal = 1;
					loadResponseTimeHistoryInfo(selectedVsIndex, selectedPort, undefined, selectedVsName, bpsVal, preVal, responseVal);
				}
//				preCompare(preVal);
			});
//			$('input:radio').change(function()
//			{
//				alert("aa");
//				
//				$("input:radio[name='bps']:radio[value='VALUE']").attr("checked",true);
//				$("input:radio[name='bps']").removeAttr("checked");
//			});
			$('.bpsAllChk').change(function(event) 
			{			
				clearRealTimer();
				clearConnRealTimer();
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
//				var selectedValue = $('input:radio[name="bps"]:checked').val();
//				alert(selectedValue);
//				selectedChartTapMode = "bpsConnChartTap";
//				selectedChartVal = "bpsTotal";
//				loadSvcPerfInfoList();
				
//				if($('input[name="responseValChk"]').is(':checked') == true)
//				{
//					$('.preCompare').attr("disabled", true);
//				}
//				else
//				{
//					$('.preCompare').removeAttr("disabled");
//				}
				
//				loadBpsConnHistoryInfo(selectedVsIndex, selectedPort, undefined, selectedVsName, bpsVal, preVal);
				if($('input[name="bpsValChk"]').is(':checked') == true)							
				{
					loadBpsConnHistoryInfo(selectedVsIndex, selectedPort, undefined, selectedVsName, bpsVal, preVal);
				}
				if($('input[name="concurrSessionValChk"]').is(':checked') == true)							
				{
					connVal = 0;
					loadConnHistoryInfo(selectedVsIndex, selectedPort, undefined, selectedVsName, bpsVal, preVal, connVal);
				}
				if ($('input[name="responseValChk"]').is(':checked') == true)	
				{
					responseVal = 1;
					loadResponseTimeHistoryInfo(selectedVsIndex, selectedPort, undefined, selectedVsName, bpsVal, preVal, responseVal);
				}
			});
			
			$('#concurrSessionChk').change(function(event) 
			{			
				clearRealTimer();
				clearConnRealTimer();
				event.preventDefault();
//				selectedChartTapMode = "connChartTap";
				$('.preCompare').removeAttr("disabled");
				
				var isChecked = $(this).is(':checked');
				if(isChecked == true)
				{
					$('.connCurrChart').removeClass('none');
					connVal = 0;
//					$('.connCurrData').removeClass('none');
//					$('.bpsInData').addClass('none');
//					$('.bpsOutData').addClass('none');
//					$('.bpsTotData').addClass('none');
					loadConnHistoryInfo(selectedVsIndex, selectedPort, undefined, selectedVsName, bpsVal, preVal, connVal);
				}
				else
				{
					connVal = -1;
					$('.connCurrChart').addClass('none');
//					$('.connCurrData').addClass('none');
//					$('.bpsInData').addClass('none');
//					$('.bpsOutData').addClass('none');
//					$('.bpsTotData').addClass('none');
				}
				
//				if($('input[name="responseValChk"]').is(':checked') == true)
//				{
//					$('.preCompare').attr("disabled", true);
//				}
//				else
//				{
//					$('.preCompare').removeAttr("disabled");
//				}
					
//				loadSvcPerfInfoList();
//				loadBpsConnHistoryInfo(selectedVsIndex, selectedPort, undefined, selectedVsName, bpsVal, preVal);
				if($('input[name="bpsValChk"]').is(':checked') == true)							
				{
					loadBpsConnHistoryInfo(selectedVsIndex, selectedPort, undefined, selectedVsName, bpsVal, preVal);
				}
//				loadConnHistoryInfo(selectedVsIndex, selectedPort, undefined, selectedVsName, bpsVal, preVal, 0);
				if ($('input[name="responseValChk"]').is(':checked') == true)	
				{
					responseVal = 1;
					loadResponseTimeHistoryInfo(selectedVsIndex, selectedPort, undefined, selectedVsName, bpsVal, preVal, responseVal);
				}
			});
			$('#responseChk').change(function(event) 
			{			
				clearRealTimer();
				clearConnRealTimer();
				event.preventDefault();
//				selectedChartTapMode = "responsChartTap";
				
				var isChecked = $(this).is(':checked');
				if(isChecked == true)
				{
					$('.responseChart').removeClass('none');
					responseVal = 1;
//					$('.preCompare').attr("disabled", true);
					loadResponseTimeHistoryInfo(selectedVsIndex, selectedPort, undefined, selectedVsName, bpsVal, preVal, responseVal);
				}
				else
				{
					responseVal = -1;
					$('.responseChart').addClass('none');
//					$('.preCompare').removeAttr("disabled");
				}
				
//				loadSvcPerfInfoList();
//				loadBpsConnHistoryInfo(selectedVsIndex, selectedPort, undefined, selectedVsName, bpsVal, preVal);
				if($('input[name="bpsValChk"]').is(':checked') == true)							
				{
					loadBpsConnHistoryInfo(selectedVsIndex, selectedPort, undefined, selectedVsName, bpsVal, preVal);
				}
				if($('input[name="concurrSessionValChk"]').is(':checked') == true)							
				{
					connVal = 0;
					loadConnHistoryInfo(selectedVsIndex, selectedPort, undefined, selectedVsName, bpsVal, preVal, connVal);
				}				
			});
			
			
			$('#exportCssLnk').click(function(event) 
			{				
				checkExportSvcPerfDataExist();
			});
			
//			console.log(flag , vsIp);
			if(flag)
			{
				$('#svcPerfSearchKey').val(vsIp);
				searchFlag=true;
				updateNavigator(adcSetting.getAdc(), undefined, undefined);
			}
			
			if(selDateTime != undefined)
				$('#compareDateTime').val(selDateTime);
			
			$('#svcPerfSearchKey').keypress(function(e)
			{
				if (e.which == 13)
					$('#svcPerfSearchBtn').click();
			});
			
			$('#svcPerfSearchBtn').click(function(e)
			{
				e.preventDefault();
				$('#svcPerfSearchKey').val(($('#svcPerfSearchKey').val() + '').trim());
				searchFlag=true;
				updateNavigator(adcSetting.getAdc(), this.selectedVsIndex, this.selectedPort);
			});
			
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
			
			if(responseVal == 1)
			{
				$('input[name="responseValChk"]').attr('checked', true);
				$('.responseChart').removeClass('none');
			}
			else
			{
				$('input[name="responseValChk"]').attr('checked', false);
				$('.responseChart').addClass('none');
			}
			
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
			
			if(responseVal == 1)
			{
				$('input[name="responseValChk"]').attr('checked', true);
				$('.responseChart').removeClass('none');
			}
			else
			{
				$('input[name="responseValChk"]').attr('checked', false);
				$('.responseChart').addClass('none');
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
			
			if (this.selectedSearchTimeMode == "realTimeMode")
		    {
				$(this).removeClass("inputText_realtime_off");
				$(this).addClass("inputText_realtime");
				$('#reservationtime').removeClass("inputText_calendar");
				$('#reservationtime').addClass("inputText_calendar_off");
								
				$('#responseChk').attr("disabled", "disabled");	
								
				$('#responseChk').attr("checked", false);
				$('.responseChart').addClass('none');
				$('.preCompare').attr("disabled", true);
				selectedChartTapMode = 'bpsConnChartTap';
		    }
		}
	},
	// 성능정보에서 IP click 하면 팝업을 통해서 상세내용을 보여줌.
	loadVsTrafficInfo : function(vsIndex, vsIp, port, vsName, vsvcIndex)
	{
		with (this)
		{
			if (!adc || !vsIndex)
			{
				return;
			}
			var timeObject =
			{
				StartTime : searchStartTime,
				EndTime : searchEndTime,
				SearchTimeMode : selectedSearchTimeMode				
			};
			realsvr.load(vsIndex, vsIp, port, vsvcIndex, vsName, timeObject, bpsVal, preVal, connVal);			
		}
	},
	// 리스트 테이블 내에서의 이벤트 (row Click, Sorting) 
	registSvcPerfInfoListEvents : function(adc)
	{
		with(this)
		{
			if(this.searchFlag == true)
			{
				$('.nulldataMsg').addClass("none");
				$('.dataNotExistMsg').addClass("none");
				if($('.servicePerformList').size() > 0)
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
				if($('.servicePerformList').size() > 0)
				{
					$('.dataNotExistMsg').addClass("none");
					$('.defaultItem').removeClass('none');
				}
				else
				{
					$('.dataNotExistMsg').removeClass("none");
					$('.defaultItem').addClass('none');
				}
			}
				
			if (adcSetting.getAdcStatus() != "available") 
			{
				$('.searchNotMsg').addClass("none");
				$('.dataNotExistMsg').addClass("none");				
				if($('.servicePerformList').size() > 0)
				{
					$('.nulldataMsg').addClass("none");
					$('.defaultItem').removeClass('none');
				}
				else
				{
					$('.nulldataMsg').removeClass("none");
					$('.defaultItem').addClass('none');
				}
			}
			// name을 클릭하는 경우 ( trafficDetail 팝업을 띄운다.)
			$('.trafficDetailLnk').click(function(e)
			{
				e.preventDefault();
				var vsIndex = $(this).parent().parent().attr('id');
				var vsIp = $(this).parent().parent().find('#ipaddress').text();
				var port = $(this).parent().parent().find('.vsPort').text().replace(",", "");
				var vsvcIndex = $(this).parent().parent().find('.vsvcIndex').text();
				var vsName = $(this).parent().parent().find('.vsName').text();
//				alert(vsvcIndex);
				if (adc.type == "Alteon")
				{
					loadVsTrafficInfo(vsIndex, vsIp, port, vsName, vsvcIndex);
				}
				else
				{
					loadVsTrafficInfo(vsIndex, vsIp, port, vsName, undefined);
				}
				selectedVsNameKey = true;
			});
			//  List Table row click 시 발생하는 이벤트
			$('#svc_table tbody tr').click(function(e)
			{
				if (true ==selectedVsNameKey)
				{
					return;
				}				
				var vsIndex = $(this).attr('id');
				var vsName = $(this).find('.vsName').text();
				var port = $(this).find('.vsPort').text().replace(",", "");
				var categoryIndex = adcSetting.getSelectIndex();
				this.selectedVsIndex = vsIndex;
				this.selectedVsName = vsName;
				this.selectedPort = port;		
												
//				var searchTimeModeChecked = $('input[name="reservation"]:checked');
				showhideBtn();				
				// Radio Btn 이 '실시간'일 경우
//				if((searchTimeModeChecked.attr('id') == 'realtime_btn'))
				if (selectedSearchTimeMode == "realTimeMode")
				{
					// 실시간일경우에는 자동으로 bps/Connection 으로 선택되도록 한다.
					$('.contents_area #bpsConnChartTap').css('background-color', '#666666');
					$('.contents_area #responsChartTap').css('background-color', 'white');			
					this.selectedChartTapMode = "bpsConnChartTap";

					displayRealTimeBpsConn(categoryIndex, adc, vsIndex, port, $(this).index(), vsName, bpsVal, true);		
					
					if($('input[name="concurrSessionValChk"]').is(':checked') == true)
					{
						displayRealTimeBpsConn(categoryIndex, adc, vsIndex, port, $(this).index(), vsName, 0, false);		
					}
					
				}
				// Radio Btn 이 '시간' or '기간' 일경우
				else
				{
//					loadBpsConnHistoryInfo(vsIndex, port, $(this).index(), vsName, bpsVal, preVal);
					if($('input[name="bpsValChk"]').is(':checked') == true)							
					{
						loadBpsConnHistoryInfo(vsIndex, port, $(this).index(), vsName, bpsVal, preVal);
					}
					
					if($('input[name="concurrSessionValChk"]').is(':checked') == true)
					{
						connVal = 0;
						loadConnHistoryInfo(vsIndex, port, undefined, vsName, bpsVal, preVal, connVal);
					}
//					if(selectedChartTapMode == 'responsChartTap')
				    if($('input[name="responseValChk"]').is(':checked') == true)
					{
				    	responseVal = 1;
						loadResponseTimeHistoryInfo(vsIndex, port, $(this).index(), vsName, bpsVal, preVal, responseVal);
					}
					
//					if(selectedChartTapMode == 'responsChartTap')
//					{
//						loadResponseTimeHistoryInfo(vsIndex, port, $(this).index(), vsName);
//					}
//					else
//					{
//						loadBpsConnHistoryInfo(vsIndex, port, $(this).index(), vsName, bpsVal, preVal);
//					}										
				}		
			});
			$('.orderDir_Desc').click(function(e)
			{
				e.preventDefault();
				orderDir =  $(this).find('.orderDir').text();
				orderType =  $(this).find('.orderType').text();
				searchFlag=true;
				updateNavigator(adcSetting.getAdc(), this.selectedVsIndex, this.selectedPort);
			});
			
			$('.orderDir_Asc').click(function(e)
			{
				e.preventDefault();
				orderDir =  $(this).find('.orderDir').text();
				orderType =  $(this).find('.orderType').text();
				searchFlag=true;
				updateNavigator(adcSetting.getAdc(), this.selectedVsIndex, this.selectedPort);
			});
			
			$('.orderDir_None').click(function(e)
			{
				e.preventDefault();
				orderDir =  $(this).find('.orderDir').text();
				orderType =  $(this).find('.orderType').text();	
				searchFlag=true;
				updateNavigator(adcSetting.getAdc(), this.selectedVsIndex, this.selectedPort);
			});
		}		
	},	
	checkExportSvcPerfDataExist : function()
	{
		with(this)
		{
			if (!adc || !selectedSearchTimeMode)
			{
				return;
			}
			var params = {
					"adcObject.category"			: adcSetting.getSelectIndex(),
					"adcObject.index"				: adc.index,
					"adcObject.name"				: adc.name,				
					"vsIndex"	: selectedVsIndex,
					"svcPort" : selectedPort
				};
				params['selectedChartTapMode'] = selectedChartTapMode;
				params['searchTimeMode'] = selectedSearchTimeMode;				
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
						
//						if($('input[name="concurrSessionValChk"]').is(':checked') == true)
//						{
//							exportCss(bpsVal, preVal, 1);
//						}
//						else
//						{
//							exportCss(bpsVal, preVal, undefined);
//						}
						
						if($('input[name="responseValChk"]').is(':checked') == true)
						{
							responseVal = 1;
							exportCss(bpsVal, preVal, responseVal);
						}
						else
						{
							connVal = 0;
							exportCss(bpsVal, preVal, connVal);
						}
					},
					errorFn : function(jqXhr)
					{
						$.obAlertAjaxError(VAR_SVCPERFOM_EXPEXISTINSPECT, jqXhr);
//						exceptionEvent();
					}
				});			
		}		
	},	
	checkExportSvcPerfDataExistNew : function()
	{
		with(this)
		{
			if (!adc || !selectedSearchTimeMode)
			{
				return;
			}
			var params = {
					"adcObject.category"			: adc.categoryIndex(),
					"adcObject.index"				: adc.index,
					"adcObject.name"				: adc.name,				
					"vsIndex"	: selectedVsIndex,
					"svcPort" : selectedPort
				};
				params['selectedChartTapMode'] = selectedChartTapMode;
				params['searchTimeMode'] = selectedSearchTimeMode;				
				params['startTimeL'] = searchStartTime;
				params['endTimeL'] = searchEndTime;
				params['preCompare'] = preVal;
//				params['selectedTime'] = searchDateTime; 
				
				if(preVal == 3)
					params['selectedTime'] = $('#compareDateTime').val();
				else
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
						exportCss();
					},
					errorFn : function(jqXhr)
					{
						$.obAlertAjaxError(VAR_SVCPERFOM_EXPEXISTINSPECT, jqXhr);
//						exceptionEvent();
					}
				});			
		}		
	},	
	exportCss : function(bpsVal, preVal, val)
	{
		with (this)
		{
//			if (null != searchStartTime) // 검색을 한번이라도 했을 경우
//			{
//				$('#reservationtime').val(new Date(searchStartTime).format('yyyy-mm-dd HH:MM')
//						+" ~ "+ new Date(searchEndTime).format('yyyy-mm-dd HH:MM'));	
//			}
//			else // 검색을 한번도 하지않았을 경우
//			{
//				searchStartTime = moment().subtract(12, 'hour').format('YYYY-MM-DD HH:mm');
//				searchEndTime = moment().format('YYYY-MM-DD HH:mm');
//			}	
			
			var params = "adcObject.category=" + adcSetting.getSelectIndex() + 
							"&adcObject.index=" + adc.index +
							"&vsIndex=" + selectedVsIndex + 
							"&svcPort=" + selectedPort + 
							"&selectedChartTapMode=" + selectedChartTapMode +
							"&searchTimeMode=" + selectedSearchTimeMode +
							"&startTimeL=" + searchStartTime + 
							"&endTimeL=" + searchEndTime +
							"&preCompare=" + preVal +
							"&selectedTime=" + searchDateTime +
							"&adcObject.vendor=" + bpsVal + 
							"&adcObject.status=" + val; 			
			var url = "monitor/downloadSvcPerfInfo.action?" + params;
			$('#downloadFrame').attr('src', url);			
		}
	},
	showhideBtn : function()
	{
		with(this)
		{
			// 실시간 일때는 내보내기 버튼 Hide
			if (selectedSearchTimeMode == "realTimeMode")
			{
				$('.contents_area #exportCssLnk').hide();
			}
			else
			{
				$('.contents_area #exportCssLnk').show();
			}
			// PAS, PAS-K 일때는 응답시간 버튼 Hide
			if (adcSetting.getAdc().type == "PAS" || adcSetting.getAdc().type == "PASK")
			{
				$('.contents_area #responsChartTap').addClass("none");
			}
			else
			{
				$('.contents_area #responsChartTap').removeClass("neme");
			}
		}
			return true;
	},
	// 실시간 타이머 초기화
	clearRealTimer : function() 
	{
		with (this)
		{
			if (null != this.realTimeTimer)
			{
				clearInterval(this.realTimeTimer);
				this.realTimeTimer = null;
			}
		}
	},
	
	clearConnRealTimer : function() 
	{
		with (this)
		{
			if (null != this.realTimeTimer1)
			{
				clearInterval(this.realTimeTimer1);
				this.realTimeTimer1 = null;
			}
		}
	},
	// Select 테이블 Row
	selectSvcPerfMonitorTableRow : function(vsIndex, port, rowIndex, vsName)
	{
		var vsIp = '';
		var vsPort = '';
		$('#svc_table tbody tr').removeClass("vsMonitorRowSelection");		
		$('#svc_table tbody tr').each(function(index) {
			if ($(this).attr("id") === vsIndex && $(this).find(".vsPort").text().replace(",", "") === port) 
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
	listHeaderNameChanger : function(vsName)
	{
		var lisetHeaderName = vsName;
		var $tbody = $('.contents_area #lisetHeaderChange').filter(':last');		
		$tbody.empty();	
		var html = '';		
		html += '[' + lisetHeaderName + ']';		
		$tbody.html(html);		
	},	
	
	// bps&Connection Chart Data get
	loadBpsConnHistoryInfo : function(vsIndex, port, rowIndex, vsName, bpsVal, preVal)
	{
		this.selectedVsIndex = vsIndex;
		this.selectedPort = port;
		this.selectedVsName = vsName;
		with (this)
		{
			if (!adc || !vsIndex || !selectedSearchTimeMode)
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
//			console.log("searchStartTime", searchStartTime);
//			console.log("searchDateTime", searchDateTime);
			params['searchTimeMode'] = selectedSearchTimeMode;		
			params['startTimeL'] = searchStartTime;
			params['endTimeL'] = searchEndTime;
			params['preCompare'] = preVal;
			
			if(preVal == 3)
				params['selectedTime'] = $('#compareDateTime').val();
			else
				params['selectedTime'] = searchDateTime;  
//			console.log("params", params);
			
			ajaxManager.runJsonExt({
				url			: "monitor/loadBpsConnHistoryInfo.action",
				data		: params,
				successFn	: function(data)
				{	
					intervalMonitor = data.intervalMonitor;
					GenerateSvcPerfomanceBpsConnChart(data, bpsVal, preVal);
					loadBpsMaxAvgHistoryInfo(vsIndex, port, undefined, vsName, bpsVal, preVal);									
//					selectSvcPerfMonitorTableRow(vsIndex, port, rowIndex, vsName, bpsVal);	
				},
				completeFn	: function(data)
				{	
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_SVCPERFOM_LAOD, jqXhr);
					selectSvcPerfMonitorTableRow(vsIndex, port, rowIndex, vsName);
				}
			});
		}		
	},
	
	// bps Avg/Max Data get
	loadBpsMaxAvgHistoryInfo : function(vsIndex, port, rowIndex, vsName, bpsVal, preVal)
	{
		this.selectedVsIndex = vsIndex;
		this.selectedPort = port;
		this.selectedVsName = vsName;
		with (this)
		{
			if (!adc || !vsIndex || !selectedSearchTimeMode)
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
//			console.log("searchStartTime", searchStartTime);
//			console.log("searchDateTime", searchDateTime);
			params['searchTimeMode'] = selectedSearchTimeMode;		
			params['startTimeL'] = searchStartTime;
			params['endTimeL'] = searchEndTime;
			params['preCompare'] = preVal;
			if(preVal == 3)
				params['selectedTime'] = $('#compareDateTime').val();
			else
				params['selectedTime'] = searchDateTime;
//			console.log("params", params);
			
			ajaxManager.runHtmlExt({
				url			: "monitor/loadBpsMaxAvgHistoryInfo.action",
				data		: params,
				target		: "tbody.bpsAvgMax",	
				successFn	: function(data)
				{					
//					GenerateSvcPerfomanceBpsConnChart(data, bpsVal, preVal);				
					selectSvcPerfMonitorTableRow(vsIndex, port, rowIndex, vsName);
					
					registerEvent(bpsVal, preVal);
				},
				completeFn	: function(data)
				{	
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_SVCPERFOM_LAOD, jqXhr);
					selectSvcPerfMonitorTableRow(vsIndex, port, rowIndex, vsName);
				}
			});
		}		
	},		
	
	loadConnHistoryInfo : function(vsIndex, port, rowIndex, vsName, bpsVal, preVal, connVal)
	{
		this.selectedVsIndex = vsIndex;
		this.selectedPort = port;
		this.selectedVsName = vsName;
		with (this)
		{
			if (!adc || !vsIndex || !selectedSearchTimeMode)
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
			params['searchTimeMode'] = selectedSearchTimeMode;		
			params['startTimeL'] = searchStartTime;
			params['endTimeL'] = searchEndTime;
			params['preCompare'] = preVal;
			if(preVal == 3)
				params['selectedTime'] = $('#compareDateTime').val();
			else
				params['selectedTime'] = searchDateTime;
			
			ajaxManager.runJsonExt({
				url			: "monitor/loadConnHistoryInfo.action",
				data		: params,
				successFn	: function(data)
				{					
					intervalMonitor = data.intervalMonitor;
					GenerateSvcPerfomanceConnChart(data, preVal, connVal);				
					loadConnMaxAvgHistoryInfo(vsIndex, port, undefined, vsName, bpsVal, preVal, connVal);
//					selectSvcPerfMonitorTableRow(vsIndex, port, rowIndex, vsName);
				},
				completeFn	: function(data)
				{	
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_SVCPERFOM_LAOD, jqXhr);
					selectSvcPerfMonitorTableRow(vsIndex, port, rowIndex, vsName);
				}
			});
		}		
	},
	
	loadConnMaxAvgHistoryInfo : function(vsIndex, port, rowIndex, vsName, bpsVal, preVal, connVal)
	{
		this.selectedVsIndex = vsIndex;
		this.selectedPort = port;
		this.selectedVsName = vsName;
		with (this)
		{
			if (!adc || !vsIndex || !selectedSearchTimeMode)
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
//			console.log("searchStartTime", searchStartTime);
//			console.log("searchDateTime", searchDateTime);
			params['searchTimeMode'] = selectedSearchTimeMode;		
			params['startTimeL'] = searchStartTime;
			params['endTimeL'] = searchEndTime;
			params['preCompare'] = preVal;
			if(preVal == 3)
				params['selectedTime'] = $('#compareDateTime').val();
			else
				params['selectedTime'] = searchDateTime;
//			console.log("params", params);
			
			ajaxManager.runHtmlExt({
				url			: "monitor/loadConnMaxAvgHistoryInfo.action",
				data		: params,
				target		: "tbody.connAvgMax",	
				successFn	: function(data)
				{					
//					GenerateSvcPerfomanceBpsConnChart(data, bpsVal, preVal);				
					selectSvcPerfMonitorTableRow(vsIndex, port, rowIndex, vsName);	
					
					registerConnEvent(bpsVal, preVal, connVal);
				},
				completeFn	: function(data)
				{	
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_SVCPERFOM_LAOD, jqXhr);
					selectSvcPerfMonitorTableRow(vsIndex, port, rowIndex, vsName);
				}
			});
		}		
	},
	registerEvent : function(bpsVal, preVal, connVal)
	{
		if (bpsVal == 0)	// 전체 선택
		{
//			loadSvcPerfInfoList();
			$('.bpsInData').removeClass('none');
			$('.bpsOutData').addClass('none');
			$('.bpsTotData').addClass('none');
			$('.connCurrData').addClass('none');
		}
		else if (bpsVal == 1) // 그룹 선택
		{
//			loadSvcPerfInfoList();
			$('.bpsInData').addClass('none');
			$('.bpsOutData').removeClass('none');
			$('.bpsTotData').addClass('none');
			$('.connCurrData').addClass('none');
		}
		else if (bpsVal == 2) // 개별 선택
		{
//			loadSvcPerfInfoList();
			$('.bpsInData').addClass('none');
			$('.bpsOutData').addClass('none');
			$('.bpsTotData').removeClass('none');
			$('.connCurrData').addClass('none');
		}
		else 
		{
//			loadSvcPerfInfoList();
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
		
//		if(connVal == -1)
//			$('.connCurrData').removeClass('none');
//		else
//			$('.connCurrData').addClass('none');
		
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
//		$('.bpsInData').addClass('none');
//		$('.bpsOutData').addClass('none');
//		$('.bpsTotData').addClass('none');
		
//		if(connVal == 0)
//		{
//			$('.connCurrData').removeClass('none');
//		}
//		else
//		{
//			$('.connCurrData').addClass('none');
//		}
		
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
	registerResponseEvent : function(bpsVal, preVal, responseVal)
	{
//		$('.bpsInData').addClass('none');
//		$('.bpsOutData').addClass('none');
//		$('.bpsTotData').addClass('none');
		
		if(responseVal == 1)
		{
			$('.responseData').removeClass('none');
		}
		else
		{
			$('.responseData').addClass('none');	
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
	// 응답시간 Chart Data get
	loadResponseTimeHistoryInfo : function(vsIndex, port, rowIndex, vsName, bpsVal, preVal, responseVal)
	{
		this.selectedVsIndex = vsIndex;
		this.selectedPort = port;
		this.selectedVsName = vsName;
		with (this)
		{
			if (!adc || !vsIndex || !selectedSearchTimeMode)
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
			params['searchTimeMode'] = selectedSearchTimeMode;
			params['startTimeL'] = searchStartTime;
			params['endTimeL'] = searchEndTime;
			params['preCompare'] = preVal;
			if(preVal == 3)
				params['selectedTime'] = $('#compareDateTime').val();
			else
				params['selectedTime'] = searchDateTime;
			
			ajaxManager.runJsonExt({
				url			: "monitor/loadResponseTimeHistoryInfo.action",
				data		: params,
				successFn	: function(data)
				{			
					intervalMonitor = data.intervalMonitor;
					GenerateResponseTimeHistoryChart(data, preVal, responseVal);
					loadResponseMaxAvgHistoryInfo(vsIndex, port, undefined, vsName, bpsVal, preVal, responseVal);					
//					selectSvcPerfMonitorTableRow(vsIndex, port, rowIndex, vsName);
				},
				completeFn	: function(data)
				{
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_SVCPERFOM_LAOD, jqXhr);
					selectSvcPerfMonitorTableRow(vsIndex, port, rowIndex, vsName);
				}
			});
		}		
	},
	
	loadResponseMaxAvgHistoryInfo : function(vsIndex, port, rowIndex, vsName, bpsVal, preVal, responseVal)
	{
		this.selectedVsIndex = vsIndex;
		this.selectedPort = port;
		this.selectedVsName = vsName;
		with (this)
		{
			if (!adc || !vsIndex || !selectedSearchTimeMode)
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
			params['searchTimeMode'] = selectedSearchTimeMode;		
			params['startTimeL'] = searchStartTime;
			params['endTimeL'] = searchEndTime;
			params['preCompare'] = preVal;
			if(preVal == 3)
				params['selectedTime'] = $('#compareDateTime').val();
			else
				params['selectedTime'] = searchDateTime;
			
			ajaxManager.runHtmlExt({
				url			: "monitor/loadResponseMaxAvgHistoryInfo.action",
				data		: params,
				target		: "table.responseAvgMax",	
				successFn	: function(data)
				{					
//					GenerateSvcPerfomanceBpsConnChart(data, bpsVal, preVal);				
					selectSvcPerfMonitorTableRow(vsIndex, port, rowIndex, vsName);	
					
					registerResponseEvent(bpsVal, preVal, responseVal);
				},
				completeFn	: function(data)
				{	
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_SVCPERFOM_LAOD, jqXhr);
					selectSvcPerfMonitorTableRow(vsIndex, port, rowIndex, vsName);
				}
			});
		}		
	},
	
	/// bps Connection Chart Generate
	GenerateSvcPerfomanceBpsConnChart : function(data, value, preVal)
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
						
						if(value == 0)
						{
							if (column.bpsInValue > -1 && column.bpsInValue != null)
							{
								firstValue = column.bpsInValue;
								if(preVal != -1)
								{
									if (column.preBpsInValue > -1 && column.preBpsInValue != null)
									{
										secondValue = column.preBpsInValue;
									}
								}
							}
						}
						else if(value == 1)
						{
							if (column.bpsOutValue > -1 && column.bpsOutValue != null)
							{
								firstValue = column.bpsOutValue;
								if(preVal != -1)
								{
									if (column.preBpsOutValue > -1 && column.preBpsOutValue != null)
									{
										secondValue = column.preBpsOutValue;
									}
								}
							}
						}
						if(value == 2)
						{
							if (column.bpsTotValue > -1 && column.bpsTotValue != null)
							{
								firstValue = column.bpsTotValue;
								if(preVal != -1)
								{
									if (column.preBpsTotValue > -1 && column.preBpsTotValue != null)
									{
										secondValue = column.preBpsTotValue;
									}
								}
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
			
			var chartOption =
			{
				 min : 0,
				 max : null,
				 linecolor1 : "#6cb8c8",
				 linecolor2 : "gray",
				 linecolor3 : "#6cb8c8",
				 linecolor4 : "gray",
				 chartname : "SvcPerfBpsChart", //bps
				 axistitle1 : VAR_COMMON_BPS,
				 axistitle2 : VAR_COMMON_AFEW,
				 maxPos : null,
				 cursorColor : "#0f47c7",
				 bpsValue : value,
				 preValue : preVal,
//				 selectedDate : searchDateTime
				 selectedDate : $('#compareDateTime').val(),
				 interval : intervalMonitor,
				 adcStatus : adcSetting.getAdcStatus()
			};
			obchart.OBSvcPerfomanceBpsChartViewerExtends(chartData, chartOption);
        }			
	},
	GenerateSvcPerfomanceConnChart : function(data, preVal, connVal)
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
						}
						if (column.preConnCurrValue > -1 && column.preConnCurrValue != null)
						{
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
				 chartname : "SvcPerfConncurrChart",// ConcurrentSessions
				 axistitle1 : VAR_COMMON_BPS,
				 axistitle2 : VAR_COMMON_AFEW,
				 maxPos : null,
				 cursorColor : "#0e7023",
				 preValue : preVal,
				 connValue : connVal,
//				 selectedDate : searchDateTime
				 selectedDate : $('#compareDateTime').val(),
				 title : "Concurrent Sessions",
				 interval : intervalMonitor
			};
			obchart.OBSvcPerfomanceConnChartViewerExtends(chartData, chartOption);
        }			
	},
	/// 응답시간 Chart Generate
	GenerateResponseTimeHistoryChart : function(data, preVal, responseVal)
	{
		with(this)
		{
			var ConfigDate = [];
			var maxData = [];
			var chartData = [];
			var chartDataList = [];
			if (data.responseTimeInfo.bpsConnData != null)
			{
				chartDataList = data.responseTimeInfo.bpsConnData;
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
						var occurTime = column.occurTime;
						var date = parseDateTime(occurTime);
						var firstValue = undefined;
				        var secondValue = undefined;					
	
				        if (column.respTimeValue > -1 && column.respTimeValue != null)
						{
							firstValue = column.respTimeValue;
						}
				        if (column.preRespTimeValue > -1 && column.preRespTimeValue != null)
						{
							secondValue = column.preRespTimeValue;
						}
						var dataObject =
						{
							occurredTime : date,
							firstValue : firstValue,
							secondValue : secondValue
						};	
						var maxdataObject = 
						{
							value : firstValue
						};
						// add object to dataProvider array
						chartData.push(dataObject);
						maxData.push(maxdataObject);
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
				 chartname : "SvcPerfResponseChart",
				 axistitle : "ms",
				 maxPos : Math.max.apply(null, $.map(maxData, function(o){return o.value;})),
				 cursorColor : "#0e7023",
				 title : VAR_SVCMONITOR_RESPTIME,
				 preValue : preVal,
				 connValue : responseVal,
//				 selectedDate : searchDateTime
				 selectedDate : $('#compareDateTime').val(),
				 interval : intervalMonitor
			};
//			obchart.OBAreaChartViewer(chartData, chartOption);
			obchart.OBSvcPerfomanceConnChartViewerExtends(chartData, chartOption);
		}
	},
	displayRealTimeBpsConn : function(categoryIndex, adc, vsIndex, port, rowIndex, vsName, bpsVal, flag) 
	{
		this.selectedVsIndex = vsIndex;
		this.selectedPort = port;
		this.selectedVsName = vsName;
		with (this)
		{			
			
			var chartOption =
			{
				 min : 0,
				 max : null,
				 linecolor1 : "#6cb8c8",
				 linecolor2 : "#fbc51a",
				 linecolor3 : "#d65f3d",
				 linecolor4 : "#976e96",
				 chartname : "SvcPerfBpsChart",
				 axistitle1 : VAR_COMMON_BPS,
				 axistitle2 : VAR_COMMON_AFEW,
				 maxPos : null,
				 cursorColor : "#0e7023",
				 bpsValue : bpsVal
			};	
			RealTimeBpsConnChart = obchart.OBCreateBpsConnRealTimeChart(chartOption);

			clearRealTimer();
			
			selectSvcPerfMonitorTableRow(vsIndex, port, rowIndex, vsName);
			SetRealTimeBpsConnData(categoryIndex, adc, vsIndex, port, RealTimeBpsConnChart, bpsVal);
			// set up the chart to update every second
			this.realTimeTimer = setInterval(function () 
			{
				loadRealTimeBpsConnData(categoryIndex, adc, vsIndex, port, RealTimeBpsConnChart, bpsVal);
		    }, 1000 * refreshIntervalSeconds);			
		}
	},
	
	displayRealTimeConn : function(categoryIndex, adc, vsIndex, port, rowIndex, vsName, bpsVal) 
	{
		this.selectedVsIndex = vsIndex;
		this.selectedPort = port;
		this.selectedVsName = vsName;
		with (this)
		{			
			var chartOption =
			{
				 min : 0,
				 max : null,
				 linecolor1 : "#6cb8c8",
				 linecolor2 : "#fbc51a",
				 linecolor3 : "#d65f3d",
				 linecolor4 : "#976e96",
				 chartname : "SvcPerfConncurrChart",
				 axistitle2 : VAR_COMMON_AFEW,
				 maxPos : null,
				 cursorColor : "#0e7023",
				 bpsValue : bpsVal
			};	
			RealTimeConnChart = obchart.OBCreateConnRealTimeChart(chartOption);

			
			clearConnRealTimer();
			selectSvcPerfMonitorTableRow(vsIndex, port, rowIndex, vsName);
			SetRealTimeConnData(categoryIndex, adc, vsIndex, port, RealTimeConnChart, 0);
			
			// set up the chart to update every second
			this.realTimeTimer1 = setInterval(function () 
			{
				loadRealTimeConnData(categoryIndex, adc, vsIndex, port, RealTimeConnChart, 0);
		    }, 1000 * refreshIntervalSeconds);
		}
	},
	// Bps,Connection 실시간 차트의 초기화 데이터를 set 하는 부분, timer와 관계없이 처음에 한번만 차트에 데이터를 넣어준다.	
	SetRealTimeBpsConnData : function (categoryIndex, adc, vsIndex, port, RealTimeBpsConnChart)
	{
		with(this)
		{
			prevInfo = undefined;
			var params = {};
			params["adcObject.category"] = categoryIndex;
			params["adcObject.index"] = adc.index;
			params["vsIndex"] = vsIndex;
			params["svcPort"] = port;
			params["prevInfo"] = null;
			ajaxManagerOB.runJsonExt
			({
				url			: "monitor/loadRealTimeBpsConnInfo.action",
				data		: params,
				successFn	: function(data) 
				{
					prevInfo = data.realTimeBpsConnInfo;
					var startTime = parseDateTime(data.realTimeBpsConnInfo.bpsTotValue.occurTime);									
					var endTime = parseDateTime(data.realTimeBpsConnInfo.bpsTotValue.occurTime);					
					
					var minusSecond = (startTime.getSeconds()) - (obchart.varInitCount4Realtime * refreshIntervalSeconds);	// 갯수 x 
					startTime.setSeconds(minusSecond);					
	
					var StartDataObject =
					{
						occurredTime : startTime,
						value : 0
					};
					obchart.OBSetRealTimeChart(RealTimeBpsConnChart, StartDataObject);
					
					
					var EndDataObject =
					{
						occurredTime : endTime,
						value : 0
					};
					obchart.OBSetRealTimeChart(RealTimeBpsConnChart, EndDataObject);					
				},
				completeFn	: function(data)
				{
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_SVCPERFOM_LAOD, jqXhr);
					clearRealTimer();
					clearConnRealTimer();
				}			
			});	
		}
	},
	// Bps,Connection 실시간 차트의 초기화 데이터를 set 하는 부분, timer와 관계없이 처음에 한번만 차트에 데이터를 넣어준다.	
	SetRealTimeConnData : function (categoryIndex, adc, vsIndex, port, RealTimeConnChart)
	{
		with(this)
		{ 
			prevInfo = undefined;
			var params = {};
			params["adcObject.category"] = categoryIndex;
			params["adcObject.index"] = adc.index;
			params["adcObject.desciption"] = "dummy";
			params["vsIndex"] = vsIndex;
			params["svcPort"] = port;
			params["prevInfo"] = null;
			
			ajaxManagerOB.runJsonExt
			({
				url			: "monitor/loadRealTimeBpsConnInfo.action",
				data		: params,
				successFn	: function(data) 
				{
					prevInfo = data.realTimeBpsConnInfo;
					var startTime = parseDateTime(data.realTimeBpsConnInfo.bpsTotValue.occurTime);									
					var endTime = parseDateTime(data.realTimeBpsConnInfo.bpsTotValue.occurTime);					
					
					var minusSecond = (startTime.getSeconds()) - (obchart.varInitCount4Realtime * refreshIntervalSeconds);	// 갯수 x 
					startTime.setSeconds(minusSecond);					
	
					var StartDataObject =
					{
						occurredTime : startTime,
						value : 0
					}; 
					obchart.OBSetRealTimeChart(RealTimeConnChart, StartDataObject);
					
					
					var EndDataObject =
					{
						occurredTime : endTime,
						value : 0
					};
					obchart.OBSetRealTimeChart(RealTimeConnChart, EndDataObject);					
				},
				completeFn	: function(data)
				{
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_SVCPERFOM_LAOD, jqXhr);
					clearRealTimer();
					clearConnRealTimer();
				}			
			});	
		}
	},
	// 실시간 차트의 초기화 후 실제 움직이는 데이터가 들어가는부분, 상위 함수호출에서의 timer 로 반복된다.
	loadRealTimeBpsConnData : function (categoryIndex, adc, vsIndex, port, RealTimeBpsConnChart, bpsVal)
	{
		with(this)
		{
			var prevOccurTime = parseDateTime(prevInfo.totalBpsRawData.occurTime);
			prevOccurTime = prevOccurTime.getTime();			
			var params = {};
			params["adcObject.category"] = categoryIndex;
			params["adcObject.index"] = adc.index;
			params["vsIndex"] = vsIndex;
			params["svcPort"] = port;
			params["inBpsValue"] = prevInfo.inBpsRawData.value;
			params["outBpsValue"] = prevInfo.outBpsRawData.value;
			params["totalBpsValue"] = prevInfo.totalBpsRawData.value;
			params["totalPpsValue"] = prevInfo.totalPpsRawData.value;
			params["totalConnCurrValue"] = prevInfo.totalConnCurrRawData.value;
			params["totalConnMaxValue"] = prevInfo.totalConnMaxRawData.value;
			params["totalConnTotValue"] = prevInfo.totalConnTotRawData.value;
			params["prevOccurTime"] = prevOccurTime;
			ajaxManagerOB.runJsonExt
			({
				url			: "monitor/loadRealTimeBpsConnInfo.action",
				data		: params,
				successFn	: function(data) 
				{
					prevInfo = data.realTimeBpsConnInfo;
					var date = parseDateTime(data.realTimeBpsConnInfo.bpsTotValue.occurTime);					
					var firstValue = undefined;
//					var secondValue = undefined;
//					var thirdValue = undefined;
//					var fourthValue = undefined;
					
					if(bpsVal == 0)
					{
						firstValue = data.realTimeBpsConnInfo.bpsInValue.value;
					}
					else if(bpsVal == 1)
					{
						firstValue = data.realTimeBpsConnInfo.bpsOutValue.value;
					}
					else if(bpsVal == 2)
					{
						firstValue = data.realTimeBpsConnInfo.bpsTotValue.value;
					}
					else
					{
						firstValue = data.realTimeBpsConnInfo.connCurrValue.value;
					}
					
//					if (data.realTimeBpsConnInfo.bpsInValue.value != null)
//					{
//						firstValue = data.realTimeBpsConnInfo.bpsInValue.value;
//					}
//					if (data.realTimeBpsConnInfo.bpsOutValue.value != null)
//					{
//						secondValue = data.realTimeBpsConnInfo.bpsOutValue.value;
//					}
//					if (data.realTimeBpsConnInfo.bpsTotValue.value != null)
//					{
//						thirdValue = data.realTimeBpsConnInfo.bpsTotValue.value;
//					}
//					if (data.realTimeBpsConnInfo.connCurrValue.value != null)
//					{
//						fourthValue = data.realTimeBpsConnInfo.connCurrValue.value;
//					}					
					
					var cpuDataObject =
					{
						occurredTime : date,
						Value1 : firstValue//,
//						Value2 : secondValue,
//						Value3 : thirdValue,
//						Value4 : fourthValue
					};			
					obchart.OBUpdateRealTimeChart(RealTimeBpsConnChart, cpuDataObject);					
				},
				completeFn	: function(data) 
				{
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_SVCPERFOM_LAOD, jqXhr);
					clearRealTimer();
					clearConnRealTimer();
				},
			});			
		}
	},
	
	// 실시간 차트의 초기화 후 실제 움직이는 데이터가 들어가는부분, 상위 함수호출에서의 timer 로 반복된다.
	loadRealTimeConnData : function (categoryIndex, adc, vsIndex, port, RealTimeBpsConnChart, bpsVal)
	{
		with(this)
		{
			var prevOccurTime = parseDateTime(prevInfo.totalBpsRawData.occurTime);
			prevOccurTime = prevOccurTime.getTime();			
			var params = {};
			params["adcObject.category"] = categoryIndex;
			params["adcObject.index"] = adc.index;
			params["adcObject.desciption"] = "dummy";
			params["vsIndex"] = vsIndex;
			params["svcPort"] = port;
			params["inBpsValue"] = prevInfo.inBpsRawData.value;
			params["outBpsValue"] = prevInfo.outBpsRawData.value;
			params["totalBpsValue"] = prevInfo.totalBpsRawData.value;
			params["totalPpsValue"] = prevInfo.totalPpsRawData.value;
			params["totalConnCurrValue"] = prevInfo.totalConnCurrRawData.value;
			params["totalConnMaxValue"] = prevInfo.totalConnMaxRawData.value;
			params["totalConnTotValue"] = prevInfo.totalConnTotRawData.value;
			params["prevOccurTime"] = prevOccurTime;
			ajaxManagerOB.runJsonExt
			({
				url			: "monitor/loadRealTimeBpsConnInfo.action",
				data		: params,
				successFn	: function(data) 
				{
					prevInfo = data.realTimeBpsConnInfo;
					var date = parseDateTime(data.realTimeBpsConnInfo.bpsTotValue.occurTime);					
					var firstValue = undefined;
					
					if (data.realTimeBpsConnInfo.connCurrValue.value != null)
					{
						firstValue = data.realTimeBpsConnInfo.connCurrValue.value;
					}					
					
					var cpuDataObject =
					{
						occurredTime : date,
						Value1 : firstValue
					};			
					obchart.OBUpdateRealTimeChart(RealTimeConnChart, cpuDataObject);					
				},
				completeFn	: function(data) 
				{
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_SVCPERFOM_LAOD, jqXhr);
					clearRealTimer();
					clearConnRealTimer();
				},
			});			
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