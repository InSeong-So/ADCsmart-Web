var ResponseTimeInfo = Class.create({
	initialize : function() 
	{	
		this.selectedIndex;
		this.selectedIpAddress;	
		this.selectedPort;				
		this.searchStartTime = undefined;
		this.searchEndTime = undefined;
		this.orderDir = 2;
		this.orderType = 14;
	},
	
	loadContent : function(index, ip, orderType, orderDir) 
	{		
		if (index)
		{
			this.selectedIndex = index;
			this.selectedIpAddress = ip;
		}
		
		with (this)
		{			
			var _self = this;
			ajaxManager.runHtmlExt({
				url : 'sysTools/loadResponseTimeContent.action',
				data : 
				{
				},
				target : "#wrap .contents", 
				successFn : function(params)
				{					
					header.setActiveMenu('SysSettingResponseTime');		
					
					if (index)
					{
						this.selectedIndex = index;
						this.selectedIpAddress = ip;
					}
					else
					{
						_self.selectedIndex = undefined;
						_self.selectedIpAddress = undefined;		
					}	
					
					loadRespGroupInfoList();
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_SYSSETTING_RESPONSETIMELOAD, jqXhr);
				}
			});		
		}
	},
		
	loadRespGroupInfoList : function(index, ip)
	{
		with (this) 
		{		
			ajaxManager.runHtmlExt({
				url		: "sysTools/loadResponseInfoList.action",
				data	: 
				{
					'startTimeL' : searchStartTime,
					'endTimeL' : searchEndTime,
					"orderType" : this.orderType,
					"orderDir" : this.orderDir
				},
				target	: "table.responseInfoList",
				successFn : function(data)
				{	
					registerRespListContents();
					
//					var vsIndex = this.selectedVsIndex;	 
//					var vsName = this.selectedVsName; 
//					var port = this.selectedPort; 
//					if (!this.selectedVsIndex)// 선택된게 없을때 맨처음껄 선택한다.
//					{
//						vsIndex = $('#resp_table tbody tr').eq(0).attr('id');
//						port = $('#resp_table tbody tr').eq(0).find('.vsPort').text().replace(",", "");
//						vsName = $('#resp_table tbody tr').eq(0).find('.vsName').text();
//					}					
//					this.selectedVsIndex = vsIndex;
//					this.selectedPort = port;
//					loadResponseTimeHistoryInfo(vsIndex, port, undefined, vsName);
//					loadResponseTimeHistoryInfo(null, null, undefined, null);
					
					
					var grpIndex = this.selectedIndex;	 
					var ipAddress = this.selectedIpAddress; 
					var port = this.selectedPort; 
					if (!this.selectedIndex)// 선택된게 없을때 맨처음껄 선택한다.
					{
						grpIndex = $('#resp_table tbody tr').eq(0).data('index');
						ipAddress = $('#resp_table tbody tr').eq(1).find('.ipAddress').text().trim();
						port = $('#resp_table tbody tr').eq(1).find('.port').text().replace(",", "").trim();
//						vsName = $('#resp_table tbody tr').eq(0).find('.vsName').text();
					}					
					this.selectedIndex = grpIndex;
					this.selectedPort = port;
					
//					if(grpIndex != undefined)
						loadResponseTimeHistoryInfo(grpIndex, ipAddress, port);
												
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_SVCPERFOM_LAOD, jqXhr);
				}
			});
		}		
	},
	
	registerRespListContents : function() 
	{
		with (this) 
		{
			$('#refresh').click(function(event) 
			{
				event.preventDefault();
//				loadContent();
				loadRespGroupInfoList();
			});	
		
			$('.orderDir_Desc').click(function(e)
			{
				e.preventDefault();
				orderDir =  $(this).find('.orderDir').text();
				orderType =  $(this).find('.orderType').text();
				var option = 
				{					
					"startTimeL" : searchStartTime,
					"endTimeL" : searchEndTime
				};				
				searchFlag=true;
//				loadContent(option , orderType , orderDir);	
				loadRespGroupInfoList();
			});
			
			$('.orderDir_Asc').click(function(e)
			{
				e.preventDefault();
				orderDir =  $(this).find('.orderDir').text();
				orderType =  $(this).find('.orderType').text();
				var option = 
				{					
					"startTimeL" : searchStartTime,
					"endTimeL" : searchEndTime
				};
				searchFlag=true;
//				loadContent(option , orderType , orderDir);
				loadRespGroupInfoList();
			});
			
			$('.orderDir_None').click(function(e)
			{
				e.preventDefault();
				orderDir =  $(this).find('.orderDir').text();
				orderType =  $(this).find('.orderType').text();
				var option = 
				{					
					"startTimeL" : searchStartTime,
					"endTimeL" : searchEndTime
				};
				searchFlag=true;
//				loadContent(option , orderType , orderDir);	
				loadRespGroupInfoList();
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
				    opens: 'left', // 달력위치
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
				    opens: 'left', // 달력위치
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
			
			//  List Table row click 시 발생하는 이벤트
			$('#resp_table tbody .Group').click(function(e)
			{		
//				e.preventDefault();
				var grpIndex = $(this).data('index');
//				var vsName = $(this).find('.vsName').text();
//				var port = $(this).find('.vsPort').text().replace(",", "");
				this.selectedIndex = grpIndex;
//				this.selectedVsName = vsName;
//				this.selectedPort = port;				
	
				loadResponseTimeHistoryInfo(grpIndex);
			});
			
			//TODO
			$('#addResponse').click(function(e)
			{
				e.preventDefault();
				popUpAddResponse(false, 0);
//				loadRespInfoAddContent(false, 0);
			});
			
			$('.modifyRespInfoLnk').click(function(e)
			{
//				e.preventDefault();
				e.stopPropagation();
				var respIndex = $(this).parent().parent().data('index');
//				popUpAddResponse(true, respIndex);
				loadRespInfoModifyContent(true, respIndex);
			});
			
			$('.allRespGrpChk').click(function(e)
			{
				var isChecked = $(this).is(':checked');
				
				$(this).parent().parent().parent().parent().find('.respGrpChk').attr('checked', isChecked);
			});
			
			$('.delRespInfoLnk').click(function(e)
			{
				e.preventDefault();
				delRespInfos();
			});
			
			$('.respGrpChk').click(function(e)
			{
				e.stopPropagation();
			});
			
			$('.btnPlusMinus').click(function(e)
			{
//				e.preventDefault();
				e.stopPropagation();
				var value = $(this).data('value');
				
				if(value == "plus")
				{
					$('.btnPlusMinus[data-parent=' + $(this).parent().data('index') + ']').children().attr('src', "/imgs/layout/minus.gif");
					$('.respInfo[data-parent=' + $(this).parent().data('index') + ']').removeClass('none');
					$(this).data("value", "minus");
				}
				else
				{
					$('.btnPlusMinus[data-parent=' + $(this).parent().data('index') + ']').children().attr('src', "/imgs/layout/plus.gif");
					$('.respInfo[data-parent=' + $(this).parent().data('index') + ']').addClass('none');
					$(this).data("value", "plus");
				}				
			});
			
//			if ($('.respList').size() > 0 )
			if ($('.responseInfoList tbody tr').size() > 0)
			{
				$('.dataNotExistMsg').addClass("none");					
			}
			else
			{
				$('.dataNotExistMsg').removeClass("none");
			}	
		}
	},
	// 응답시간 Chart Data get
//	loadResponseTimeHistoryInfo : function(vsIndex, port, rowIndex, vsName)
	loadResponseTimeHistoryInfo : function(grpIndex)
	{
		this.selectedIndex = grpIndex;
//		this.selectedPort = port;
//		this.selectedIpAddress = ipAddress;
		with (this)
		{
			var params = {
				"adcObject.category"			: adcSetting.getSelectIndex(),
//				"adcObject.index"				: adc.index,			
//				"adcObject.name"				: adc.name,
				"respIndex"						: grpIndex
			};
//			if (0 === port || port)
//			{
//				params["svcPort"] = port;
//			};		
			params['startTimeL'] = searchStartTime;
			params['endTimeL'] = searchEndTime;			
			
			ajaxManager.runJsonExt({
				url			: "sysTools/loadResponseTimeHistoryInfo.action",
				data		: params,
				successFn	: function(data)
				{		
					intervalMonitor = data.intervalMonitor;
					GenerateResponseTimeHistoryChart(data, intervalMonitor);
//					selectSvcPerfMonitorTableRow("5_/Common/any", 0, 1, "/Common/any");
					selectSvcPerfMonitorTableRow(grpIndex);
				},
				completeFn	: function(data)
				{
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_SVCPERFOM_LAOD, jqXhr);
					selectSvcPerfMonitorTableRow(grpIndex);
				}
			});
		}		
	},
	// Select 테이블 Row
	selectSvcPerfMonitorTableRow : function(grpIndex)
	{
//		var vsIp = '';
//		var vsPort = '';
		$('#resp_table tbody .Group').removeClass("vsMonitorRowSelection");		
		$('#resp_table tbody .Group').each(function(index) {
			if ($(this).data("index") === grpIndex) 
			{
				$(this).addClass("vsMonitorRowSelection");
				return false;
//				if (index === rowIndex || (!rowIndex && 0 !== rowIndex))
//				{					
//					$(this).addClass("vsMonitorRowSelection");
//					vsIp = $(this).find('#ipaddress').text();
//					vsPort = $(this).find('.vsPort').text();
//					return false;
//				}
			}
		});
		
//		var headerName;
//		if (vsName && vsName != '')
//			headerName = vsName;
//		else
//		{
//			headerName = vsIp;
//			if (vsPort)
//				headerName += ':' + vsPort;
//		}
		
	},
	/// 응답시간 Chart Generate
	GenerateResponseTimeHistoryChart : function(data, interval)
	{
		with(this)
		{
//			var ConfigDate = [];
//			var maxData = [];
			var chartData = [];
			var chartDataList = [];
			if (data.responseTimeInfo != null)
			{
				chartDataList = data.responseTimeInfo;
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
						var occurTime = column.occurTime;
						var date = parseDateTime(occurTime);
						var Value1 = undefined;							
						var Value2 = undefined;
						var Value3 = undefined;
						var Value4 = undefined;
						var Value5 = undefined;
						var Value6 = undefined;
//						var SumValue = 0;
						var ValueName1 = column.name1;
						var ValueName2 = column.name2;
						var ValueName3 = column.name3;
						var ValueName4 = column.name4;
						var ValueName5 = column.name5;
						var ValueName6 = column.name6;
	
						if (column.value1 > -1 && column.value1 != null)
						{
							Value1 = column.value1;
//							SumValue += Value1;
						}						
						if (column.value2 > -1 && column.value2 != null)
						{
							Value2 = column.value2;
//							SumValue += Value2;
						}
						if (column.value3 > -1 && column.value3 != null)
						{
							Value3 = column.value3;
//							SumValue += Value3;
						}
						if (column.value4 > -1 && column.value4 != null)
						{
							Value4 = column.value4;
//							SumValue += Value4;
						}
						if (column.value5 > -1 && column.value5 != null)
						{
							Value5 = column.value5;
//							SumValue += Value5;
						}
						if (column.value6 > -1 && column.value6 != null)
						{
							Value6 = column.value6;
//							SumValue += Value6;
						}
						
						var dataObject =
						{
							occurredTime : date,
							firstValue : Value1,		
							secondValue : Value2,
							thirdValue : Value3,
							fourthValue : Value4,
							fifthValue : Value5,
							sixthValue : Value6,
							firstName : ValueName1,
							secondName : ValueName2,
							thirdName : ValueName3,
							fourthName : ValueName4,
							fifthName : ValueName5,
							sixthName : ValueName6//,
//							sumValue : SumValue
						};
//						var maxdataObject = 
//						{
//							value : firstValue
//						};
						// add object to dataProvider array
						chartData.push(dataObject);
//						maxData.push(maxdataObject);
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
//				var startTime = data.startTime;
//				var endTime = data.endTime;
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
			 var chartOption =
				{
					 min : 0,
					 max : null,				 
					 linecolor1 : "#6cb8c8",
					 linecolor2 : "#fbc51a",
					 linecolor3 : "#d65f3d",
					 linecolor4 : "#976e96",
					 linecolor5 : "#fb8e33",
					 linecolor6 : "#9cc239",
//					 linecolor7 : "gray",  // sum
					 chartname : "SvcPerfChart",
					 axistitle : "ms",
					 maxPos : null, //Math.max.apply(null, $.map(maxData, function(o){return o.value;})),
					 cursorColor : "#0f47c7",
					 interval : interval
				};
			obchart.OBAreaResponseMultiChartViewer(chartData, chartOption);
		}
	},
	
	// TODO - 추가 팝업
	popUpAddResponse : function(isModify, idx)
	{
		with (this)
		{
			showPopup({
				'id' : '#addResponseWnd',
				'width' : '900px'
			});
			
			registerEvents(isModify);
			
//			if(isModify)
//				loadRespInfoModifyContent(isModify, idx);
//			else
//				loadRespInfoAddContent(isModify, idx);
		}
	},
	
	// TODO - 추가 팝업
	popUpModifyResponse : function(isModify, idx)
	{
		with (this)
		{
			showPopup({
				'id' : '#modifyResponseWnd',
				'width' : '900px'
			});
			
			registerEvents(isModify);
		}
	},
	
	registerEvents : function(isModify)
	{
		with(this)
		{
			$('.cloneDiv #respInfoAddFrm').submit(function()
			{
				if (!_validateRespInfoAdd())
					return false;
				
				if (!validIpPortSet())
//				if (!_validationFilterChk())
					return false;
				
				$(this).ajaxSubmit({
					dataType : 'json',
					url : isModify ? 'sysTools/setRespIntervalCheck.action' : 'sysTools/addRespIntervalCheck.action',
					data :
					{
//						"respGroupInfo.index" : $('input[name="respGroupInfo.index"]').val()
//						"respGroupInfo.respInfo.type" : $('.cloneDiv #respType option:selected').val()
						"respName" : $('.cloneDiv #respName').val()
					},
					success : function(data)
					{
						if (!data.isSuccessful)
						{
							$.obAlertNotice(data.message);//중복 메세지 처리.
							return;
						}
						
						$('.cloneDiv .close').click();
						loadContent();
					},
					error : function(jqXhr)
					{
						if(jqXhr.responseText.indexOf('JSONException') > -1) 
						{
							$('.cloneDiv .close').click();
							loadContent();
						}			
						else
						{
							$.obAlertAjaxError(VAR_SYSTOOLS_RESPADDFAIL, jqXhr);
						}
					}
				}); 
		        return false; 
		    });
			
			$('.cloneDiv .addResponseInfo').click(function(e)
			{
				$('.cloneDiv #respInfoAddFrm').submit();	
			});
	
			$('.cloneDiv .onCancel').click(function(e)
			{
				e.preventDefault();
				$('.popup_type1').remove();
				$('.cloneDiv').remove();
			});

			$('.cloneDiv .moveDown').click(function()
			{
				var checkedCount = $("input[name=id]:checked").length;
				if (checkedCount > 1 ) 
				{
					$.obAlertNotice(VAR_SYSTOOLS_MOVEROWSEL);
					return false;
				} 
				else if(checkedCount == 0) 
				{
					$.obAlertNotice(VAR_SYSTOOLS_MOVEROWSEL);
					return false;
				}
				else 
				{
					var element = $("input[name=id]:checked").parent().parent();
					moveRowDown(element);
				}
			});
			
			$('.cloneDiv .moveUp').click(function()
			{
				var checkedCount = $("input[name=id]:checked").length;
				if (checkedCount > 1 ) 
				{
					$.obAlertNotice(VAR_SYSTOOLS_MOVEROWSEL);
					return false;
				} 
				else if(checkedCount == 0) 
				{
					$.obAlertNotice(VAR_SYSTOOLS_MOVEROWSEL);
					return false;
				}
				else 
				{
					var element = $("input[name=id]:checked").parent().parent();
					moveRowUp(element);
				}
			});
			
			$('.cloneDiv .moveBottom').click(function()
			{
				var checkedCount = $("input[name=id]:checked").length;
				if (checkedCount > 1 ) 
				{
					$.obAlertNotice(VAR_SYSTOOLS_MOVEONEROWSEL);
					return false;
				} 
				else if(checkedCount == 0) 
				{
					$.obAlertNotice(VAR_SYSTOOLS_MOVEROWSEL);
					return false;
				}
				else 
				{
					var element = $("input[name=id]:checked").parent().parent();
					moveRowBottom(element);
				}
			});
			
			$('.cloneDiv .moveTop').click(function()
			{
				var checkedCount = $("input[name=id]:checked").length;
				if (checkedCount > 1 ) 
				{
					$.obAlertNotice(VAR_SYSTOOLS_MOVEONEROWSEL);
					return false;
				} 
				else if(checkedCount == 0) 
				{
					$.obAlertNotice(VAR_SYSTOOLS_MOVEROWSEL);
					return false;
				}
				else 
				{
					var element = $("input[name=id]:checked").parent().parent();
					moveRowTop(element);
				}
			});
			
			$('.cloneDiv .moveDel').click(function(e)
			{
				var checkedCount = $("input[name=id]:checked").length;
//				var trLength = $('.cloneDiv .respTbd tr').length;
								
				if(checkedCount == 0) 
				{
					$.obAlertNotice(VAR_SYSTOOLS_DELROWSEL);
					return false;
				}
				else 
				{
					var element = $("input[name=id]:checked").parent().parent();
//					
//					if(trLength == 1)
//					{
//						$.obAlertNotice(VAR_SYSTOOLS_ATLEASTONE);
//						return false;
//					}
//					else
//					{
						element.remove();
//					}
				}
				
				_setRespInfoCount();
				rowsReindex($('.cloneDiv .respInfoTr'));
			});
			
			//
			$('.cloneDiv #addMember').click(function(e)
			{
				e.preventDefault();
								
				var trCount = $('.cloneDiv .respInfoTr').size();
				
//				
//				if (trCount == 5) 
//				{
//					$('.imgOff').removeClass("none");
//					$('.imgOn').addClass("none");				
//				}
//				else
//				{
//					$('.imgOff').addClass("none");
//					$('.imgOn').removeClass("none");
//				}
				
				if(trCount > 5)
				{
					$.obAlertNotice(VAR_SYSTOOLS_RANGEOVER);
					return false;
				}
				else
				{
					var html = '';	 
					html += '<tr class="respInfoTr">';
//					if(trCount == 0)
//					{    
						html += '<td class="align_center"><input class="member" name="id" type="checkbox" /></td>';
						html += '<td class="align_center"><input class="respIp inputText_table width100" type="text" name="respGroupInfo.respInfo[' + trCount + '].ipaddress" value=""></td>';
						html += '<td class="align_center"><input class="respPort inputText_table width40" type="text" name="respGroupInfo.respInfo[' + trCount + '].port" value=""></td>';					
//						html += '<td class="align_center"><select id="respType"><option value="0"' + ('HTTP' ? 'selected="selected"' : '') +'>HTTP</option><option value="1"' + ('HTTPS' ? '' : 'selected="selected"') + '>HTTPS</option></select></td>';		
//						html += '<td class="align_center"><select id="respType"><option value="0"' + ('HTTP' ? 'selected="selected"' : '') +'>HTTP</option></select></td>';
						html += '<td class="align_center"><select id="respType" class="respType"><option value="1"' + ('TCP' ? 'selected="selected"' : '') +'>TCP</option><option value="2"' + ('HTTP' ? '' : 'selected="selected"') +'>HTTP</option></select>';
						html += '<input class="respType inputText_table width100" type="hidden" id="respTypeVal" name="respGroupInfo.respInfo[' + trCount + '].type" value="1"></td>';
						html += '<td class="align_center"><input class="respPath inputText_table width280" type="text" name="respGroupInfo.respInfo[' + trCount + '].path" value="" disabled="disabled"></td>';
						html += '<td class="align_center"><input class="inputText_table width135" type="text" name="respGroupInfo.respInfo[' + trCount + '].comment" value=""></td>';
//					}
//					else
//					{
//						html += '<td class="align_center"><input class="member" name="id" type="checkbox" /></td>';
//						html += '<td class="align_center"><input class="respIp inputText_table width100" type="text" class="ip" name="respGroupInfo.respInfo[' + (trCount+20) + '].ipaddress" value=""></td>';
//						html += '<td class="align_center"><input class="respIp inputText_table width40" type="text" class="ip" name="respGroupInfo.respInfo[' + (trCount+20) + '].port" value=""></td>';					
//						html += '<td class="align_center"><select id="respType"><option value="0"' + ('HTTP' ? 'selected="selected"' : '') +'>HTTP</option><option value="1"' + ('HTTPS' ? '' : 'selected="selected"') + '>HTTPS</option></select></td>';		
//						html += '<td class="align_center"><input class="respIp inputText_table width280" type="text" class="ip" name="respGroupInfo.respInfo[' + (trCount+20) + '].path" value=""></td>';
//						html += '<td class="align_center"><input class="respIp inputText_table width135" type="text" class="ip" name="respGroupInfo.respInfo[' + (trCount+20) + '].comment" value=""></td>';
//					}
					html += '</tr>';					
					
					$('.cloneDiv .respTbd').append(html);
					registerEventsAdd();
					
						//clone										
//						$.trClone = $('.cloneDiv .respTbd tr:last').clone().html().replace(/\[[0-9]+\]/g, '[' + (trCount + 10) + ']');
//						
//						$.newTr = $("<tr class='respInfoTr' data-id=" + trCount + ">"+$.trClone+"</tr>");
//	//					$.newTr.find('input').val(''); // tr 추가시 빈값으로 표시
//						$.newTr.find('input:not(.respType)').val('');
//						// append
//		                $('.cloneDiv .respTbd').append($.newTr);
					
					
					
					//clone										
//					$.trClone = $('.cloneDiv .respTbd tr:last').clone().html().replace(/\[[0-9]+\]/g, '[' + $('.cloneDiv .respTbd tr:last').data('id') + ']');
//					
//					$.newTr = $("<tr class='respInfoTr' data-id=" + trCount + ">"+$.trClone+"</tr>");
////					$.newTr.find('input').val(''); // tr 추가시 빈값으로 표시
//					$.newTr.find('input:not(.respType)').val('');
//					// append
//	                $('.cloneDiv .respTbd').append($.newTr);
					
				}				 	
					
				
				_setRespInfoCount();
				rowsReindex($('.cloneDiv .respInfoTr'));
			});
			
			$('.cloneDiv .allMembersChk').click(function(e)
			{
				var isChecked = $(this).is(':checked');
				$('.cloneDiv .member').attr("checked", isChecked);
				
			});
			
			$('.cloneDiv .respType').change(function(e)
			{
				e.preventDefault();
				
//					var type = $('.cloneDiv #respType option:selected').val();
//					$('.cloneDiv #respTypeVal').val(type);
//					var type = $(this).find('option:selected').val();
					
//					rowsReindex($('.cloneDiv .respInfoTr').find('.respType').val());
					
					$(this).parent().find('.respType').val($(this).find('option:selected').val());
					
					if ($(this).find('option:selected').val() == 1)
					{
						$(this).parent().parent().find('.respPath').attr("disabled", "disabled");						
					}
					else
					{
						$(this).parent().parent().find('.respPath').removeAttr("disabled");
					}
				
			});
			
		}
	},
	
	registerEventsAdd : function()
	{
		with (this)
		{
			$('.cloneDiv .respType').change(function(e)
			{
				e.preventDefault();
				
				$(this).parent().find('.respType').val($(this).find('option:selected').val());

				if ($(this).find('option:selected').val() == 1)
				{
					$(this).parent().parent().find('.respPath').attr("disabled", "disabled");						
				}
				else
				{
					$(this).parent().parent().find('.respPath').removeAttr("disabled");
				}				
			});
		}
	},
	rowsReindex: function(rows)
	{
		var tempSelect = undefined;
		rows.each(function(index, row)
		{
			$(row).find('[name]').each(function(childIndex, childElem)
			{
				tempSelect = $(childElem);
				tempSelect.attr('name', tempSelect.attr('name').replace(/\[[0-9]+\]/g, '[' + index + ']'));
			});
		});
	},
	validIpPortSet : function()
	{		
//		var respInfoName = $('.cloneDiv input[name="respGroupInfo.name"]').val();
//		if (respInfoName == "")
//		{
//			$.obAlertNotice(VAR_SYSTOOLS_RESPNAME);
//			return false;
//		}
//		else if (getValidateStringint(respInfoName, 1, 40) == false)
//		{
//			$.obAlertNotice(VAR_COMMON_SPECIALCHAR);
//			return false;
//		}	
				
		var ipPortSet = [];
		var flag = true;
//		$('.cloneDiv .respIp').each(function()
//		{
//			var respIp = $(this).val();
//			var respPort = $(this).parents('tr').find('.respPort').val();
//			
////		 	IP와 포트가 모두 존재하는 행에 한해 IP:Port형태 배열 생성
//			if (respIp && respPort)
//				ipPortSet.push(respIp + ':' + respPort);
//		});
		
		$('.cloneDiv .respInfoTr').each(function()
		{
			var respIp = $(this).find('.respIp').val();
			var respPort = $(this).find('.respPort').val();
			
//				 	IP와 포트가 모두 존재하는 행에 한해 IP:Port형태 배열 생성
			if (respIp && respPort)
				ipPortSet.push(respIp + ':' + respPort);
		});

//		console.log(ipPortSet);
		$.each(ipPortSet, function(index, value) {
			if (ipPortSet.indexOf(value) != index)
			{
				$.obAlertNotice(VAR_COMMON_IPPORT_DUPLICATION);//alert('IP:Port is Unique Key!');
				flag = false;
				return flag;
			}
		});
		
		return flag;
	},
	
	_validationFilterChk : function()
	{		
//		var defaultRespIpAddress = $('.cloneDiv .respTbd tr:first').find('.respIp').val();
		
//		var trCount = $('.cloneDiv .respInfoTr').size();  //3
//		var respInfoIpAddress = new Array();
//		for(var i=0; i<trCount; i++)
//		{
//			respInfoIpAddress[i] = $('.cloneDiv .respTbd tr').eq(i).find('.respIp').val();
//		}
//		
//		var duplicateCheck = respInfoIpAddress.reduce(function(a,b)
//		{
//			if(a.indexOf(b) < 0)
//				a.push(b)
//				return a;
//		},[]);
		
		var respInfoIpAddress1 = $('.cloneDiv .respTbd tr').eq(0).find('.respIp').val();		
		var respInfoIpAddress2 = $('.cloneDiv .respTbd tr').eq(1).find('.respIp').val();
		var respInfoIpAddress3 = $('.cloneDiv .respTbd tr').eq(2).find('.respIp').val();
		var respInfoIpAddress4 = $('.cloneDiv .respTbd tr').eq(3).find('.respIp').val();
		var respInfoIpAddress5 = $('.cloneDiv .respTbd tr').eq(4).find('.respIp').val();
		var respInfoIpAddress6 = $('.cloneDiv .respTbd tr').eq(5).find('.respIp').val();
		
		var respInfoPort1 = $('.cloneDiv .respTbd tr').eq(0).find('.respPort').val();		
		var respInfoPort2 = $('.cloneDiv .respTbd tr').eq(1).find('.respPort').val();
		var respInfoPort3 = $('.cloneDiv .respTbd tr').eq(2).find('.respPort').val();
		var respInfoPort4 = $('.cloneDiv .respTbd tr').eq(3).find('.respPort').val();
		var respInfoPort5 = $('.cloneDiv .respTbd tr').eq(4).find('.respPort').val();
		var respInfoPort6 = $('.cloneDiv .respTbd tr').eq(5).find('.respPort').val();
		
		if (respInfoIpAddress1 == "" )
		{
			$.obAlertNotice(VAR_COMMON_IPINPUT);
			return false;
		}
		else			
		{
			if (getValidateIP(respInfoIpAddress1) == false)
			{
				$.obAlertNotice(VAR_COMMON_IIFORMAT);
				return false;
			}
		}
		
		if (respInfoIpAddress2 == "" )
		{
			$.obAlertNotice(VAR_COMMON_IPINPUT);
			return false;
		}
		else			
		{
			if (getValidateIP(respInfoIpAddress2) == false)
			{
				$.obAlertNotice(VAR_COMMON_IIFORMAT);
				return false;
			}
		}	
		
		if (respInfoIpAddress3 == "" )
		{
			$.obAlertNotice(VAR_COMMON_IPINPUT);
			return false;
		}
		else			
		{
			if (getValidateIP(respInfoIpAddress3) == false)
			{
				$.obAlertNotice(VAR_COMMON_IIFORMAT);
				return false;
			}
		}	
		
		if (respInfoIpAddress4 == "" )
		{
			$.obAlertNotice(VAR_COMMON_IPINPUT);
			return false;
		}
		else			
		{
			if (getValidateIP(respInfoIpAddress4) == false)
			{
				$.obAlertNotice(VAR_COMMON_IIFORMAT);
				return false;
			}
		}	
		
		if (respInfoIpAddress5 == "" )
		{
			$.obAlertNotice(VAR_COMMON_IPINPUT);
			return false;
		}
		else			
		{
			if (getValidateIP(respInfoIpAddress5) == false)
			{
				$.obAlertNotice(VAR_COMMON_IIFORMAT);
				return false;
			}
		}	
		
		if (respInfoIpAddress6 == "" )
		{
			$.obAlertNotice(VAR_COMMON_IPINPUT);
			return false;
		}
		else			
		{
			if (getValidateIP(respInfoIpAddress6) == false)
			{
				$.obAlertNotice(VAR_COMMON_IIFORMAT);
				return false;
			}
		}	
		
		
		var trCount = $('.cloneDiv .respInfoTr').size();  
		
		for (var i=0; i<trCount; i++)
		{
			if(((respInfoIpAddress1==respInfoIpAddress2) && (respInfoPort1 == respInfoPort2)) || (respInfoIpAddress1==respInfoIpAddress3) && (respInfoPort1 == respInfoPort3)
				|| (respInfoIpAddress1==respInfoIpAddress4) && (respInfoPort1 == respInfoPort4) || (respInfoIpAddress1==respInfoIpAddress5) && (respInfoPort1 == respInfoPort5)
				|| (respInfoIpAddress1==respInfoIpAddress6) && (respInfoPort1 == respInfoPort6))
			{
				$.obAlertNotice(VAR_COMMON_IPPORT_DUPLICATION);
				return false;
			}
		}
		
		var trLength = $('.cloneDiv .respTbd tr').length;
		
		if(trLength == 0)
		{
			$.obAlertNotice(VAR_SYSTOOLS_ATLEASTONE);
			return false;
		}
		
		
//		_isMember : function(ip, port)
//		{
//			var regMemberIpTd = $('.regMemberTr').children(':nth-child(2)');	// regMemberIpTd 에 ip값 초기화
//			var regMemberPortTd = $('.regMemberTr').children(':nth-child(3)');	// regMemberPortTd dp port값 초기화
//			for (var i=0; i < regMemberIpTd.length; i++) {
//				if (regMemberIpTd.eq(i).text() == ip && regMemberPortTd.eq(i).text() == port)	// port 와 ip 값 비교
//					return true;
//			}
//			
//			return false;
//		},
		
		
//		var arr = new Array();
//		for(var i=0; i<trCount; i++)
//		{
//			var arr = $('.cloneDiv .respTbd tr').eq(i).find('.respIp').val();
//			var respInfoIpAddress = arr[];
//			
//			for(var j=0; j<trCount; j++)
//			{
//				if(j != i)
//				{
//					var b = arr[$('.cloneDiv .respTbd tr').eq(j).find('.respIp').val()];
//					
//					if (respInfoIpAddress == b)
//					{
//						alert("동일한 IP가 존재합니다.");
//						return false;
//					}
//				}
//			}			
//		}
		
		return true;
//		var respInfoIpAddress1 = $('.cloneDiv .respTbd tr').eq(0).find('.respIp').val();		
//		var respInfoIpAddress2 = $('.cloneDiv input[name="respGroupInfo.respInfo[1].ipaddress"]').val();
//		var respInfoIpAddress3 = $('.cloneDiv input[name="respGroupInfo.respInfo[2].ipaddress"]').val();
//		var respInfoIpAddress4 = $('.cloneDiv input[name="respGroupInfo.respInfo[3].ipaddress"]').val();
//		var respInfoIpAddress5 = $('.cloneDiv input[name="respGroupInfo.respInfo[4].ipaddress"]').val();
//		var respInfoIpAddress6 = $('.cloneDiv input[name="respGroupInfo.respInfo[5].ipaddress"]').val();
//		
//			
//		if ((defaultRespIpAddress == respInfoIpAddress1) || (defaultRespIpAddress == respInfoIpAddress2) || (defaultRespIpAddress == respInfoIpAddress3) ||
//				(defaultRespIpAddress == respInfoIpAddress4) || (defaultRespIpAddress == respInfoIpAddress5) || (defaultRespIpAddress == respInfoIpAddress6))
//		{
//			alert("동일한 IP가 존재합니다.");
//			return false;
//		}
		
	},
	_validateRespInfoAdd : function()
	{
		with(this)
		{
			// 구간 이름 필드 검사. 
			
			var flag = true;				
			
			var respInfoName = $('.cloneDiv input[name="respGroupInfo.name"]').val();
			if (respInfoName == "")
			{
				$.obAlertNotice(VAR_SYSTOOLS_RESPNAME);
				flag = false;
				return flag;
			}
			else if (getValidateStringint(respInfoName, 1, 40) == false)
			{
				$.obAlertNotice(VAR_COMMON_SPECIALCHAR);
				flag = false;
				return flag;
			}	
			
			$('.cloneDiv .respIp').each(function()
			{
				var respIp = $(this).val();
				
				if (respIp == "")
				{
					$.obAlertNotice(VAR_COMMON_IPINPUT);
					flag = false;
					return flag;
				}
				else if (getValidateIP(respIp) == false)
				{
					$.obAlertNotice(VAR_COMMON_IIFORMAT);
					flag = false;
					return flag;
				}	
			});
			
			$('.cloneDiv .respPort').each(function()
			{
				var respPort = $(this).val();
				
				if (respPort == "")
				{
					$.obAlertNotice(VAR_COMMON_PORTINPUT);
					flag = false;
					return flag;
				}
				else if (getValidateNumberRange(respPort, 0, 65535)==false)
				{
					$.obAlertNotice(VAR_COMMON_PORTINPUTRANGE);
					flag = false;
					return flag;
				}
			});
			
			if($('.cloneDiv .respInfoTr').size() < 1)
			{
				$.obAlertNotice(VAR_SYSTOOLS_ATLEASTONE);
				flag = false;
				return flag;
			}
			
//			var trLength = $('.cloneDiv .respTbd tr').length;
//			
//			if(trLength == 0)
//			{
//				$.obAlertNotice(VAR_SYSTOOLS_ATLEASTONE);
//				return false;
//			}
			
			return flag;
			
			
//			if (!$('input[name="alteonVsAdd.name"]').validate(
//				{
//					name: $('input[name="alteonVsAdd.name"]').parent().parent().find('li').text(),
//					type: "en_name",
//					lengthRange: [1,64]
//				}))
//			{
//				return false;
//			}
			
			// ip 필드 검사. 
/*			
			var trCount = $('.cloneDiv .respInfoTr').size();  //3
			var respInfoIpAddress = new Array();
			for(var i=0; i<trCount; i++)
			{
				respInfoIpAddress[i] = $('.cloneDiv .respTbd tr').eq(i).find('.respIp').val();
				if (respInfoIpAddress[i] == "")
				{
					$.obAlertNotice(VAR_COMMON_IPINPUT);
					return false;
				}
				else if (getValidateIP(respInfoIpAddress[i]) == false)
				{
					$.obAlertNotice(VAR_COMMON_IIFORMAT);
					return false;
				}	
			}
	*/		
//			var respInfoIp = $('.cloneDiv input[name="respGroupInfo.respInfo.ipaddress"]').val();
//			if (respInfoIp == "")
//			{
//				$.obAlertNotice(VAR_COMMON_IPINPUT);
//				return false;
//			}
//			else if (getValidateIP(respInfoIp) == false)
//			{
//				$.obAlertNotice(VAR_COMMON_IIFORMAT);
//				return false;
//			}	
						
//			return true;
		}
	},
	_setRespInfoCount : function()
	{
		$('.respInfoCount').text($('.cloneDiv .respInfoTr').size());
	},
	moveRowUp : function(element) 
	{
		if( element.prev().html() != null  && element.prev().attr("id") != "header")
		{
			var prevElement = element.prev();
			element.insertBefore(prevElement);
//			changNum();
//			this.swapRowName(element, prevElement);
			this.rowsReindex($('.cloneDiv .respInfoTr'));
		} 
		else 
		{
			$.obAlertNotice(VAR_SYSTOOLS_TOP);
			return false;
		}
	},
	
	moveRowDown : function(element) 
	{
		if( element.next().html() != null )
		{
			var nextElement = element.next();
			element.insertAfter(nextElement);
//			changNum();
//			this.swapRowName(element, nextElement);
			this.rowsReindex($('.cloneDiv .respInfoTr'));
		}  
		else 
		{
			$.obAlertNotice(VAR_SYSTOOLS_BOTTOM);
			return false;
		}
	},
	
	moveRowTop : function(element) 
	{
		if( element.prev().html() != null  && element.prev().attr("id") != "header")		
		{
//			element.insertBefore(element.prev());
//			row.insertAfter($("table tr:last"));
//			element.insertBefore(element.parent().find('tr:first'));
//			changNum();
			
			var prevElement = element.parent().find('tr:first');
			element.insertBefore(prevElement);
//			this.swapRowName(element, prevElement);
			this.rowsReindex($('.cloneDiv .respInfoTr'));
		} 
		else 
		{
			$.obAlertNotice(VAR_SYSTOOLS_TOP);
			return false;
		}
	},
	
	moveRowBottom : function(element) 
	{
		if( element.next().html() != null )
//		if(element.next().html() == undefined)
		{
//			element.insertAfter(element.next());
//			element.insertAfter(element.parent().find('tr:last'));		
//			changNum();
			
			var prevElement = element.parent().find('tr:last');
			element.insertAfter(prevElement);
//			this.swapRowName(element, prevElement);
			this.rowsReindex($('.cloneDiv .respInfoTr'));
		}  
		else 
		{
			$.obAlertNotice(VAR_SYSTOOLS_BOTTOM);
			return false;
		}
	},
	
	changNum : function() 
	{
//		var num = 0;
//		$('input[name=platform_num]').each(function(){
//			num++;
//			$(this).val(num);
//		});
		
		$('.cloneDiv #respIp').each(function(){
			num++;
			$(this).val(num);
		});
	},
//	swapRowName: function(row, otherRow)
//	{
//		var my = this;
//		row.find('[name]').each(function(index, elem)
//		{
//			my.swapName($(elem), otherRow.find('[name]').eq(index));
//		});
//	},
//	swapName: function(elem, otherElem)
//	{
//		var tempName = elem.attr('name');
//		elem.attr('name', otherElem.attr('name'));
//		otherElem.attr('name', tempName);
//	},
	loadRespAddContent : function()
	{
		with (this)
		{
			newPoolName = '';
			ajaxManager.runHtmlExt({
				url : "virtualServer/loadAlteonVsAddContent.action",
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
					virtualServer.setActiveContent('VsAddContent');
					applyVsAddContentEvents();
					registerVsAddContentEvents(false);
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_COMMON_VSLFAIL, jqXhr);
//					exceptionEvent();
				}	
			});
		}
	}, //TODO
//	loadRespInfoAddContent : function(isModify, index)
//	{
//		with (this)
//		{			
//			ajaxManager.runHtmlExt({
//				url : "sysTools/loadRespInfoAddContent.action",
//				data :
//				{
//					"respIndex" : 0
//				},
//				target: "#addResponseWnd.description",
//				successFn : function(params)
//				{
////					registerEvents(true);
////					loadContent();
//					popUpAddResponse(false);
////					registerEvents(isModify);
//				},
//				errorFn : function(jqXhr)
//				{
//					$.obAlertAjaxError(VAR_SYSTOOLS_RESPLOADFAIL, jqXhr);
//				}	
//			});
//		}
//	},
	loadRespInfoModifyContent : function(isModify, index)
	{
		with (this)
		{			
			ajaxManager.runHtmlExt({
				url : "sysTools/loadRespInfoModifyContent.action",
				data :
				{
					"respIndex" : index
				},
				target: "#modifyResponseWnd .description",
//				target: "#wrap .contents",
				successFn : function(params)
				{
//					virtualServer.setActiveContent('VsModifyContent');
//					applyVsAddContentEvents();
//					registerEvents(true);
//					loadContent();
					popUpModifyResponse(isModify);
//					registerEvents(isModify);
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_SYSTOOLS_RESPLOADFAIL, jqXhr);
				}	
			});
		}
	},
	delRespInfos : function()
	{
		with (this)
		{
			var checkedRespInfos = _getChekedRespInfos();
			if(checkedRespInfos.length == 0)
			{
				$.obAlertNotice(VAR_SYSTOOLS_RESPDELSEL);
				return;
			}
			
			var chk = confirm(VAR_SYSTOOLS_RESPDEL);
			if(chk == false)
			{
				return;
			}
			
			ajaxManager.runHtmlExt({
				url : "sysTools/delRespIntervalCheck.action",
				data : 
				{
					"respInfoIndexes" : checkedRespInfos
				},
				target : "#wrap .contents_area",
				successFn : function(params)
				{
					loadContent();
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_SYSTOOLS_RESPDELFAIL, jqXhr);
				}
				
			});
		}
	},
	_getChekedRespInfos : function()
	{
		var respInfos = $('.respGrpChk').filter(':checked').map(function() {
			return $(this).val();
		}).get();
		
		return respInfos;
	}
});