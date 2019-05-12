var Report = Class.create({
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
		this.refreshTimer = undefined;
		this.refreshIntervalSeconds=10;
		this.orderDir  = 2; //2는 내림차순
		this.orderType = 11;// 11은 occurTime
		this.$unassignedAdcOptions = $();
		this.pageNavi = new PageNavigator();
		this.pageNavi.onChange(function(fromRow, toRow, orderType, orderDir) 
		{
			FlowitUtil.log('fromRow: %s, toRow: %s, orderType: %s, orderDir: %s', fromRow, toRow, orderType, orderDir);
			fn.loadReportTableInListContent(fn.searchedOption, fromRow, toRow, orderType, orderDir);
		});
		this.refreshTimer = undefined;
		this.refreshIntervalSeconds=10;
		this.selectedIndex = undefined;
		this.searchFlag = false;
		this.autoRefresh = false;
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
//			if (!adcSetting.isAdcSet())
//			{
//				$('#wrap .contents').empty();
//				return;
//			}
			
			if(!validateDaterefresh())
			{
				return false;
			}
			
			selectedIndex = adcSetting.getSelectIndex();
			var adcIndex = undefined;
			var groupIndex = undefined;
			if (selectedIndex == 1)
				groupIndex = adcSetting.getGroupIndex();
			else if (selectedIndex == 2)
				adcIndex = adcSetting.getAdc().index;
			else
				groupIndex = 0;
			
			FlowitUtil.log("index: %s", adcSetting.getAdc().index);
			var rowTotal = 0;
			ajaxManager.runJsonExt({
				url : "report/retrieveReportsTotal.action",
				data : {
					"adc.index" : adcIndex,
//					"adc.name" : adcSetting.getAdc().name,
//					"adc.type" : adcSetting.getAdc().type,
					"adc.groupIndex" : groupIndex,
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
					loadReportListContent(searchOption);
					refreshReport(searchOption, orderType, orderDir);					
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_REPORT_COUNTEXTRACT, jqXhr);
//					exceptionEvent();
				}		
			});
		}
	},
	loadReportListContent : function(searchOption, fromRow, toRow) 
	{
		with (this)
		{
			FlowitUtil.log("searchOption:%s, fromRow:%s, toRow:%s", searchOption, fromRow, toRow);
//			if (!adcSetting.isAdcSet()) 
//			{
//				$('#wrap .contents').empty();
//				return;
//			}
			var adcIndex = undefined;
			var groupIndex = undefined;
			if (selectedIndex == 1)
				groupIndex = adcSetting.getGroupIndex();
			else if (selectedIndex == 2)
				adcIndex = adcSetting.getAdc().index;
			else
				groupIndex = 0;
			ajaxManager.runHtmlExt({
				url : "report/loadListContent.action",
				data :
				{
					"adc.index" : adcIndex,
//					"adc.name" : adcSetting.getAdc().name,
//					"adc.type" : adcSetting.getAdc().type,
					"adc.groupIndex" : groupIndex,
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
					header.setActiveMenu('Report');
					searchedOption = searchOption;
					noticePageInfo();
					//_applyListContentCss();
					_registerListContentEvents();
				},
				completeFn : function() 
				{
					pageNavi.refresh();
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_REPORT_LOAD, jqXhr);
				}	
			});
		}
	},
	refreshReport : function(searchOption, orderType, orderDir)
	{
		with (this)
		{
			clearRefreshTimer();
			if (0 != refreshIntervalSeconds)
			{
				refreshTimer = setInterval(function() 
				{
					if ($('.reportTable').is(':visible'))
					{
						autoRefresh=true;
						var option = 
						{
								"key" :	undefined,
								"startTimeL" : searchStartTime,
								"endTimeL" : searchEndTime
						};
						loadListContent(option, orderType, orderDir);
					} 
					else 
					{
						clearRefreshTimer();
					}					
				}, refreshIntervalSeconds * 1000);
			}
		}	
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
	loadReportTableInListContent : function(searchOption, fromRow, toRow, orderType, orderDir)
	{
		with (this) 
		{
			FlowitUtil.log("searchOption:%s, fromRow:%s, toRow:%s, orderType: %s, orderDir: %s", searchOption, fromRow, toRow, orderType, orderDir);
			var adcIndex = undefined;
			var groupIndex = undefined;
			if (selectedIndex == 1)
				groupIndex = adcSetting.getGroupIndex();
			else if (selectedIndex == 2)
				adcIndex = adcSetting.getAdc().index;
			else
				groupIndex = 0;
			ajaxManager.runHtmlExt({
				url : "report/loadReportTableInListContent.action",
				data : 
				{
					"adc.index" : adcIndex,
//					"adc.name" : adcSetting.getAdc().name,
//					"adc.type" : adcSetting.getAdc().type,
					"adc.groupIndex" : groupIndex,
					"fromRow" : fromRow === undefined ? pageNavi.getFirstRowOfCurrentPage() : fromRow,
					"toRow" : toRow === undefined ? pageNavi.getLastRowOfCurrentPage() : toRow,
					"searchKey" : searchOption ? searchOption.key : undefined,
					"startTimeL" : searchOption ? searchOption.startTimeL : undefined,
					"endTimeL" : searchOption ? searchOption.endTimeL : undefined,
					"orderType" : this.orderType,
					"orderDir" : this.orderDir
				},
				target: "table.reportTable",
				successFn : function(params)
				{
					noticePageInfo();
					searchedOption = searchOption;
					_registerListContentEvents();
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_REPORT_LOAD, jqXhr);
				}
			});
		}
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
				var option = 
				{
					"key" :	$('.control_Board input.searchTxt').val(),
					"startTimeL" : searchStartTime,
					"endTimeL" : searchEndTime
				};							
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
				loadListContent(option , orderDir , orderType);
			});
			
			$('.allReportsChk').click(function(e) 
			{
				var isChecked = $(this).is(':checked');
				$(this).parents('table.reportTable').find('.reportChk').attr('checked', isChecked);
			});
			
			$('.downloadLnk').click(function(e) 
			{
				e.preventDefault();
				var reportIndex = $(this).parent().parent().find('.reportIndex').text();
				FlowitUtil.log("reportIndex:", reportIndex);
				_checkDownloadReportDataExist(reportIndex);
			});
			
			$('.reportAddLnk').click(function(e) 
			{
				e.preventDefault();
				loadAddContent();
			});
			
			$('.reportsDelLnk').off('click');  // 중복 이벤트 방지를 위한 등록 해제
			$('.reportsDelLnk').click(function(e) 
			{
				e.preventDefault();
				var reportIndices = $('.reportChk').filter(':checked').siblings('.reportIndex').map(function() 
				{
					return $(this).text();
				}).get();
				FlowitUtil.log("reportIndices: ", reportIndices);
				if (reportIndices.length == 0)
				{
					$.obAlertNotice(VAR_REPORT_DELSEL);
					return;
				}			
				var chk = confirm(VAR_REPORT_DEL);
				if (chk) 
				{
					delReports(reportIndices);
				}
				else
				{
					return false;
				}
//				delReports(reportIndices);
			});
			$('#refresh').click(function(e)
			{
				if(!validateDaterefresh())
				{
					return false;
				}
				searchStartTime = undefined;
				searchEndTime = undefined;
				loadListContent();
			});
			// search event
//			$('.cont_sch a.searchLnk').click(function(e)			
			$('.btn .searchLnk').click(function (e) 
			{
				e.preventDefault(); 				
				var option = 
				{
						"key" :	$('.control_Board input.searchTxt').val(),
						"startTimeL" : searchStartTime,
						"endTimeL" : searchEndTime
				};
				FlowitUtil.log('searchOption: %s', option);
				searchFlag = true;
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
			
			$('input[name="fromPeriod"]').datepicker({
				maxDate: "0",
				dateFormat : "yy-mm-dd",
				showOn: "button",
				buttonImage: "imgs/meun/btn_calendar.png",
				buttonImageOnly: true
			});
			$('input[name="toPeriod"]').datepicker({
				maxDate: "0",
				dateFormat : "yy-mm-dd",
				showOn: "button",
				buttonImage: "imgs/meun/btn_calendar.png",
				buttonImageOnly: true
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
			
			//TODO
			if(this.autoRefresh == false)
			{
				if(this.searchFlag == true)
				{
					$('.nulldataMsg').addClass("none");
					$('.dataNotExistMsg').addClass("none");
					if($('.reportList').size() > 0)
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
					if($('.reportList').size() > 0)
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
				if($('.reportList').size() > 0)
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
			
			
			if (selectedIndex == 2 && adcSetting.getAdcStatus() != "available") 
			{
				$('input[name="reportSearchTxt"]').attr("disabled", "disabled");
				$('input[name="reservation"]').attr("disabled", "disabled");
				
				$('.imageChange').attr("src", "imgs/meun/btn_search_1_off.png");
				$('.ui-datepicker-trigger').attr("src", "imgs/meun/btn_calendar_off.png");
				
				$('.searchNotMsg').addClass("none");
				$('.dataNotExistMsg').addClass("none");
				$('.imgOff').removeClass("none");
				$('.imgOn').addClass("none");
				
				$('.allReportsChk').attr("disabled", "disabled");
				$('.reportChk').attr("disabled", "disabled");				
				
				if ($('.reportList').size() > 0 )
				{
					$('.disabledChk').removeClass("none");
					$('.nulldataMsg').addClass("none");					
				}
				else
				{
					$('.disabledChk').addClass("none");
					$('.nulldataMsg').removeClass("none");
				}				
			}		
		}
	},
	_checkDownloadReportDataExist : function(index)
	{
		with (this) 
		{
			ajaxManager.runJsonExt
			({
				url :"report/checkDownloadReportDataExist.action",				
				data : 
				{
					"reportIndex" : index
				},
				successFn : function(data) 
				{
					if(!data.isSuccessful)
					{
						$.obAlertNotice(data.message);
						return;
					}	
					downloadReport(index);
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_REPORT_VALIDATE, jqXhr);
				}	
			});
		}
		
	},
	downloadReport : function(index) 
	{
		with (this) 
		{
			var url = "report/downloadReport.action?reportIndex=" + index;
			$('#downloadFrame').attr('src', url);				
		}
	},
	delReports : function(reportIndices)
	{
		with (this) 
		{
			ajaxManager.runJsonExt({
				url : "report/delReports.action",
				data : 
				{
					"reportIndices" : reportIndices,
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
					$.obAlertAjaxError(VAR_REPORT_DELFAIL, jqXhr);
				}
			});
		}
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
	loadAddContent : function() 
	{
		with (this) 
		{
			ajaxManager.runHtmlExt({
				url : "report/loadAddContent.action",
				target: "#wrap .contents",
				data : 
				{
					"adcObject.category"	: adcSetting.getSelectIndex(),
					"adcObject.index" 		: getIndex(adcSetting.getSelectIndex())				
				},
				successFn : function(params)
				{
					registerReportAddContentEvents(false);
					initReportAddContentValues(false);
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_REPORT_ADDFAIL, jqXhr);
				}				
			});
		}
	},
	initReportAddContentValues : function(isModify)
	{
		with (this) 
		{
			$unassignedAdcOptions = $();
			console.log("selectedIndex : "+selectedIndex);
			
			if(selectedIndex == 1){
				$('select[name="reportAdd.reportType"] option:not([value=sysAdminReport])').remove();
			}
			else if (selectedIndex == 2)
			{
				if(adcSetting.getAdc().type == "PAS" || adcSetting.getAdc().type == "PASK")				
					$('select[name="reportAdd.reportType"] option:eq(2)').hide();
				else
					$('select[name="reportAdd.reportType"] option:eq(2)').show();
				
				_setReportNameByType($('select[name="reportAdd.reportType"]').val());
				_setCurrentAdcToSelected();
				$('select[name="reportAdd.reportType"] option:not([value=sysFalultReport],[value=adcDiagnosisReport],[value=l4OperationReport],[value=sysAdminReport])').remove();
				$('select[name="reportAdd.reportType"]').val('sysFalultReport').change();
			}
			else
			{
				$('select[name="reportAdd.reportType"] option:not([value=sysAdminReport],[value=sysAdminTotalReport])').remove();
				$('select[name="reportAdd.reportType"]').val('sysAdminReport').change();
			}
		}
	},
	registerReportAddContentEvents : function(isModify)
	{
		with (this) 
		{
			$('#reportAddFrm').submit(function() 
			{
				selectRegisteredAdcsForSubmit();
				if (!validateReportAdd())
					return false;
				
				var groupIndex = undefined;
				if (selectedIndex == 1)
					groupIndex = adcSetting.getGroupIndex();
				else if (selectedIndex != 2)
					groupIndex = 0;
				
			    $(this).ajaxSubmit({
			    	type : 'POST',
					dataType : 'json',
					url : isModify ? 'report/modifyReport.action' : 'report/addReport.action',
					data :
					{
						"reportAdd.logKey" 	: $('select[name="reportAdd.extraInfo"]').val(),
						"reportAdd.groupIndex" : groupIndex 
					},
					success : function(data)
					{
						if (data.isSuccessful) 
						{
							$.obAlertNotice(VAR_COMMON_REGISUCCESS);
							loadListContent();
						}
						else	
						{
							$.obAlertNotice(data.message);
						}
					},
					error : function(jqXhr)
					{
						$.obAlertAjaxError(VAR_REPORT_ADDMODIDFY, jqXhr);
					}
			    }); 		 
		        // always return false to prevent standard browser submit and page navigation
		        return false; 
		    });
			
			// ===========시스템 운영 보고서 및 장애 보고서용 datepicker 처리.===========
			// datepicker를 이용해서 데이터를 입력 받은 후 입력 받은 데이터를 텍스트 박스에 출력한다. 
			// 1. 이미지 href를 이용해서 button click 이벤트를 받으면 datepicker를 화면에 활성화 한다.
			//2. datepicker에서 입력 받은 데이터는 onSeclect 이벤트가 발생될 때 텍스트 박스에 데이트 정보를 출력한다.
			var $defaultFrom = $("<input type='text' />").hide().datepicker({
				maxDate: "+0",
				dateFormat: "yy-mm-dd",				
			    onSelect: function(dateText, inst) 
			    {
//					var day = $(this).datepicker('getDate').getDate();                 
//					var month = $(this).datepicker('getDate').getMonth() + 1;             
//					var year = $(this).datepicker('getDate').getFullYear();
//					
//					if(day<10)
//					{
//						day = "0"+day;
//					}
//					if(month<10)
//					{
//						month = "0"+month;
//					}
//					$("#id_all_txtbox_fromDate").val(year+"-"+month+"-"+day);// 텍스트 박스에 데이터 저장.					
					$("#id_all_txtbox_fromDate").val(dateText);// 텍스트 박스에 데이터 저장.
					$('#id_all_radiobox_customDate').attr('checked', 'checked');// 라디오 버튼 활성화.
			    }
			}).appendTo('body');
			$("#id_all_lnk_fromDate").click(function(e) 
			{
			    if ($defaultFrom.datepicker('widget').is(':hidden'))
			    {
			    	$defaultFrom.show().datepicker('show').hide();
			    	$defaultFrom.datepicker("widget").position({
			            my: "left top",
			            at: "right top",
			            of: this
			        });			    	
			    }
			    else
			    {
			    	$defaultFrom.hide();
			    }
			    //e.preventDefault();
			});
			
			var $defaultTo = $("<input type='text' />").hide().datepicker({
				maxDate: "+0",
				dateFormat: "yy-mm-dd",	
			    onSelect: function(dateText, inst)
			    {
					$("#id_all_txtbox_toDate").val(dateText);// 텍스트 박스에 데이터 저장.
					$('#id_all_radiobox_customDate').attr('checked', 'checked');// 라디오 버튼 활성화.
			    }
			}).appendTo('body');
			$("#id_all_lnk_toDate").click(function(e) 
			{
			    if ($defaultTo.datepicker('widget').is(':hidden')) 
			    {
			    	$defaultTo.show().datepicker('show').hide();
			    	$defaultTo.datepicker("widget").position({
			            my: "left top",
			            at: "right top",
			            of: this
			        });			    	
			    } 
			    else
			    {
			    	$defaultTo.hide();
			    }
			    //e.preventDefault();
			});
			var $daily = $("<input type='text' />").hide().datepicker({
				maxDate: "+0",
				dateFormat: "yy-mm-dd",	
			    onSelect: function(dateText, inst) 
			    {		
					$("#id_all_txtbox_fromDate, #id_all_txtbox_toDate").val(dateText);// 텍스트 박스에 데이터 저장.
					$('#id_all_radiobox_customDate').attr('checked', 'checked');// 라디오 버튼 활성화.
			    }
			}).appendTo('body');
			$("#daily").click(function(e) 
			{
			    if ($daily.datepicker('widget').is(':hidden'))
			    {
			    	$daily.show().datepicker('show').hide();
			    	$daily.datepicker("widget").position({
			            my: "left top",
			            at: "right top",
			            of: this
			        });

			    } 
			    else 
			    {
			    	$daily.hide();
			    }
			    //e.preventDefault();
			});			
// ===========시스템 운영 보고서 및 장애 보고서용 datepicker 처리. 주간 월간===========			
//			var $weekMonth = $("<input type='text' />").hide().datepicker({
//			    onSelect: function(dateText, inst) {
//			    	var reportType = $('select[name="reportAdd.reportType"]').val();
//			    	if(reportType === 'l4OpWeeklyReport')
//		    		{
//			    		alert("주간");
//			    		var day1 = $(this).datepicker('getDate').getDate();                 
//						var month1 = $(this).datepicker('getDate').getMonth() + 1;             
//						var year1 = $(this).datepicker('getDate').getFullYear();
//						$('input[name="reportAdd."]').val(year1+"-"+month1+"-"+"01");// 텍스트 박스에 데이터 저장.
//						$('input[name="reportAdd.endTimeL"]').val(year1+"-"+month1+"-"+day1);
//		    		}
//		
//		    		else if(reportType === 'l4OpMonthlyReport')
//	    			{
//		    			alert("월간");
//		    			var day1 = $(this).datepicker('getDate').getDate();                 
//						var month1 = $(this).datepicker('getDate').getMonth() + 1;             
//						var year1 = $(this).datepicker('getDate').getFullYear();
//						$('input[name="reportAdd."]').val(year1+"-"+month1+"-"+"01");// 텍스트 박스에 데이터 저장.
//						$('input[name="reportAdd.endTimeL"]').val(year1+"-"+month1+"-"+day1);
//	    			}
//			    }
//			}).appendTo('body');
//			$("#weekMonth").click(function(e) {
//			    if ($weekMonth.datepicker('widget').is(':hidden')) {
//			    	$weekMonth.show().datepicker('show').hide();
//			    	$weekMonth.datepicker("widget").position({
//			            my: "left top",
//			            at: "right top",
//			            of: this
//			        });
//
//			    } else {
//			    	$weekMonth.hide();
//			    }
//			    //e.preventDefault();
//			});
			
			$('select[name="reportAdd.reportType"]').change(function() 
			{
				_setReportNameByType($(this).val());
				_setReportFileType($(this).val());
			});
			
			$('.toAdcSelectionLnk').click(function(e) 
			{
				e.preventDefault();
				moveAdcsToSelection();
			});
			
			$('.toAdcDeselectionLnk').click(function(e) 
			{
				e.preventDefault();
				moveAdcsToDeselection();
			});
			
			$('.cancelLnk').click(function(e) 
			{
				e.preventDefault();
				loadListContent();
			});
			//TODO
			$('.chk_sysadmin_report').change(function() {
			
				 if(this.checked)
				 {
					 $('.okLnk').removeAttr("disabled");
				 }
				 else
				 {
					 $('.okLnk').attr("disabled","disabled");
				 }
			});
			
			$('.okLnk').click(function(e) 
			{
				e.preventDefault();
				
				if($('select[name="reportAdd.reportType"]').val() == "adcDiagnosisReport")
				{
					var extraInfo = $('select[name="reportAdd.extraInfo"]').val();
					if(!extraInfo)
					{
						$.obAlertNotice(VAR_REPORT_RESULTNOTSELECT);
						return false;
					}
				}
				
				// from GS. #4012-1 #8, #3926-4 #16: 14.07.29 sw.jung obvalidation 활용 개선
//				var name = $('input[name="reportAdd.name"]').val();
//				
//				if (!$.trim(name))
//				{
//					alert(VAR_REPORT_NAMEINPUT);
//					return false;
//				}
//				
//				if(getValidateStringint(name, 1, 64) == false)
//				{
//					alert(VAR_COMMON_SPECIALCHAR);
//					return false;
//				}
				if (!$('input[name="reportAdd.name"]').validate(
					{
						name: $('input[name="reportAdd.name"]').parent().parent().parent().find('li').text(),
						required: true,
						type: "name",
						lengthRange: [1,64]
					}))
				{
					return false;
				}
				
				$('#reportAddFrm').submit();
			});
			
			// search event
//			$('.usr_list .adcSearchLnk').click(function (e) 
			$('.btn1 .adcSearchLnk').click(function (e)
			{
				e.preventDefault();
				
//				var searchKey = $(this).siblings('.adcSearchTxt').val();
				var searchKey = $('.inputTextposition1 .inputText_search').val();
				FlowitUtil.log('click:' + searchKey);
				_searchOnUnassignedAdcs(searchKey);				
			});
			
//			$('.usr_list .adcSearchTxt').keydown(function(e)
			$('.inputTextposition1 .inputText_search').keydown(function(e) 
			{
				if (e.which != 13)
					return;
				
				var searchKey = $(this).val();
				FlowitUtil.log('click:' + searchKey);
				FlowitUtil.log('click:' + searchKey);
				_searchOnUnassignedAdcs(searchKey);
			});			
		}
	},
	selectRegisteredAdcsForSubmit : function()
	{
		$('.adcsSelectedSel > option').attr('selected', true);
	},
	
	_setReportFileType : function(reportType)
	{
//		$('.sysAdmin').addremoveClass("none");
		if((reportType == 'sysAdminReport') || (reportType == 'adcDiagnosisReport'))
		{
			$('#sysAdmin').removeClass("none");
			$('#sysAdminTotal').addClass("none");
			$('#sysFault').addClass("none");
			$('#select_PDF').attr('checked', 'checked');
		}
		else if(reportType == 'sysFalultReport')
		{
			$('#sysAdmin').addClass("none");
			$('#sysAdminTotal').addClass("none");
			$('#sysFault').removeClass("none");
			$('#select_PDF_fault').attr('checked', 'checked');
		}else if(reportType == 'sysAdminTotalReport')
		{
			$('#sysAdmin').addClass("none");
			$('#sysAdminTotal').removeClass("none");
			$('#sysFault').addClass("none");
			$('#select_PDF_total').attr('checked', 'checked');
		}	
	},
	
	_setReportNameByType : function(reportType)
	{
		var now = new Date();
		
		$('.reportGroup').removeClass('none');// 보고서 기간 화면 조정..
		
		if (reportType == 'sysAdminReport') 
		{
//			$('input[name="reportAdd.name"]').val(VAR_REPORT_SYSTEMOPER + dateFormat(now, "mmddHHMMss"));
			$('input[name="reportAdd.name"]').val("System Report_" + dateFormat(now, "mmddHHMMss"));
			//$('.reportGroupFromTo').addClass('none');
			//$('.reportGroupDate').addClass('none');
			$('.result').addClass("none");    
			$('.duration').addClass("none");                                    // 기간 삭제    junhyun.ok_GS
			$($('.duration').parent().find('.DivideLine')[0]).addClass('none');    // 종류, 기간 레코드 사이 라인 삭제  junhyun.ok_GS
			//$('.duration').removeClass("none");
				//TODO
				$('.alertMsg_Monitor_Mode').removeClass("none");
				if($('.chk_sysadmin_report').is(':checked'))
				 {
					 $('.okLnk').removeAttr("disabled");
				 }
				 else
				 {
					 $('.okLnk').attr("disabled","disabled");
				 }
		}
		else if(reportType == 'adcDiagnosisReport') 
		{
//			$('input[name="reportAdd.name"]').val(VAR_REPORT_ADCDIAGNOSISRESULT + dateFormat(now, "mmddHHMMss"));
			$('input[name="reportAdd.name"]').val("ADC Diagnosis Report_" + dateFormat(now, "mmddHHMMss"));
			$('.reportGroup').removeClass('none');// 보고서 기간 화면 조정..
			$('.reportGroupFromTo').addClass('none');
			$('.reportGroupDate').addClass('none');
			$('.result').removeClass("none");
			$('.duration').addClass("none");
			$($('.duration').parent().find('.DivideLine')[0]).removeClass('none');  // 종류, 기간 레코드 사이 라인 추가 junhyun.ok_GS
			$('.alertMsg_Monitor_Mode').addClass("none");
			$('.okLnk').removeAttr("disabled");
		}
		else if(reportType == 'sysFalultReport') 
		{
//			$('input[name="reportAdd.name"]').val(VAR_REPORT_FAILANALYSIS + dateFormat(now, "mmddHHMMss"));
			$('input[name="reportAdd.name"]').val("Alert Report_" + dateFormat(now, "mmddHHMMss"));
			$('.reportGroup').removeClass('none');// 보고서 기간 화면 조정..
			$('.reportGroupFromTo').addClass('none');
			$('.reportGroupDate').addClass('none');
			$('.result').addClass("none");
			$('.duration').removeClass("none");
			$($('.duration').parent().find('.DivideLine')[0]).removeClass('none'); // 종류, 기간 레코드 사이 라인 추가 junhyun.ok_GS
			$('.alertMsg_Monitor_Mode').addClass("none");
			$('.okLnk').removeAttr("disabled");
		}
		else if (reportType == 'l4OperationReport') 
		{
			$('input[name="reportAdd.name"]').val(VAR_REPORT_L4OPER + dateFormat(now, "mmddHHMMss"));
			$('.reportGroup').removeClass('none');// 보고서 기간 화면 조정..
			$('.reportGroupFromTo').addClass('none');
			$('.reportGroupDate').addClass('none');
			$('.alertMsg_Monitor_Mode').addClass("none");
			$('.okLnk').removeAttr("disabled");
		}
		else if (reportType == 'l4OpDailyReport') 
		{
			$('input[name="reportAdd.name"]').val(VAR_REPORT_L4OPERDAY + dateFormat(now, "mmddHHMMss"));
			$('.reportGroupDate').removeClass('none');// 보고서 기간 화면 조정..
			$('.reportGroupFromTo').addClass('none');
			$('.reportGroup').addClass('none');
			$('.alertMsg_Monitor_Mode').addClass("none");
			$('.okLnk').removeAttr("disabled");
		}
		else if (reportType == 'l4OpWeeklyReport')
		{
			$('input[name="reportAdd.name"]').val(VAR_REPORT_L4OPERWEEK + dateFormat(now, "mmddHHMMss"));
			$('.reportGroupFromTo').removeClass('none');// 보고서 기간 화면 조정..
			$('.reportGroupDate').addClass('none');
			$('.reportGroup').addClass('none');
			$('.alertMsg_Monitor_Mode').addClass("none");
			$('.okLnk').removeAttr("disabled");
		}
		else if (reportType == 'l4OpMonthlyReport')
		{
			$('input[name="reportAdd.name"]').val(VAR_REPORT_L4OPERMONTH + dateFormat(now, "mmddHHMMss"));
			$('.reportGroupFromTo').removeClass('none');// 보고서 기간 화면 조정..
			$('.reportGroupDate').addClass('none');
			$('.reportGroup').addClass('none');
			$('.alertMsg_Monitor_Mode').addClass("none");
			$('.okLnk').removeAttr("disabled");
		}
	},
	_setCurrentAdcToSelected : function()
	{
		$('.adcsDeselectedSel option[value="' + adcSetting.getAdc().index + '"]').attr('selected', 'selected');
		this.moveAdcsToSelection();
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
	validateReportAdd : function()
	{
//		if (!$.trim($('input[name="reportAdd.name"]').val()))
//		{
//			alert(VAR_REPORT_NAMEINPUT);
//			return false;
//		}
		
		if (!$('input[id="previousDate"]').is(':checked'))
		{
			if (!$.trim($('input[name="reportAdd.fromPeriod"]').val()))
			{
				$.obAlertNotice(VAR_COMMON_STATRTDATE);	
				return false;
			}
			
			if (!$.trim($('input[name="reportAdd.toPeriod"]').val()))
			{
				$.obAlertNotice(VAR_COMMON_ENDDATE);	
				return false;
			}
			
			if ($('input[name="reportAdd.fromPeriod"]').val() > $('input[name="reportAdd.toPeriod"]').val())
			{
				$.obAlertNotice(VAR_COMMON_DATEERROR);
				return false;
			}
			return true;
		}		
		return true;
	},
	moveAdcsToSelection : function()
	{
		with (this) 
		{
			var $adcsSelected = $('.adcsSelectedSel');
			var $adcsDeselected = $('.adcsDeselectedSel');
			
			var $option = $adcsDeselected.children(':selected');
			log.debug($option.size());
			if ($option.size() > 0)
			{
				$adcsSelected.append($option);
			}		
			showSelectedAdcsCount();
		}
	},
	showSelectedAdcsCount : function()
	{
//		$('.selectedAdcsCount').text($('.adcsSelectedSel').children().length + '개 선택됨');
		$('.selectedAdcsCount').text($('.adcsSelectedSel').children().length);
		if(selectedIndex == 2 && $('.adcsSelectedSel').children().length==0)
		{
			$.obAlertNotice(VAR_REPORT_ATLEASTONE);
		}		
	},
	moveAdcsToDeselection : function()
	{
		with (this)
		{
			var $adcsSelected = $('.adcsSelectedSel');
			var $adcsDeselected = $('.adcsDeselectedSel');
			
			var $option = $adcsSelected.children(':selected');
			
			if(adcSetting.getAdc().index==$option.val())
			{
				$.obAlertNotice(VAR_REPORT_ADCNOTEXCLUDE);
							
			}
			else
			{
				log.debug($option.size());
				if ($option.size() > 0)
				{
					$adcsDeselected.append($option);
				}		
				showSelectedAdcsCount();
			}
		}
	},
	_searchOnUnassignedAdcs : function(searchKey) 
	{
		with (this) 
		{
			var $adcsDeselectedSel = $('.adcsDeselectedSel');
			if (!searchKey)
			{
				$adcsDeselectedSel.append($unassignedAdcOptions);
				$unassignedAdcOptions = $();
				return ;
			}			
			_fillCbxWithSearchedAndSaveUnsearched(searchKey, $adcsDeselectedSel);
		}
	},
	_fillCbxWithSearchedAndSaveUnsearched : function(searchKey, $adcsDeselectedSel) 
	{
		with (this) 
		{
			$unassignedAdcOptions = $unassignedAdcOptions.add($adcsDeselectedSel.children().detach());
			var keyInLowerCase = searchKey.toLowerCase();
			log.debug('keyInLowerCase: ' + keyInLowerCase);
			var $unsearchedOptions = $();
			$unassignedAdcOptions.each(function() 
			{
				var index = $(this).text().toLowerCase().indexOf(keyInLowerCase);
				log.debug('index: ' + index);
				if (index == -1)
				{
					$unsearchedOptions = $unsearchedOptions.add($(this));
				}				
				else
				{
					$adcsDeselectedSel.append($(this));
				}				
			});			
			$unassignedAdcOptions = $unsearchedOptions;
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