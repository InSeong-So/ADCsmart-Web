var Statistics = Class.create({
	initialize : function() 
	{
		this.searchedKey = undefined;		
		this.searchLastHours = 12;
		this.searchStartTime = undefined;
		this.searchEndTime = undefined;
		this.selectedSearchTimeMode = undefined;
		this.refreshTimer = null;
		this.refreshIntervalSeconds = 0;
		this.adc;
		this.interfaceNameList = undefined;
		this.selectedTraffic = undefined;
		this.orderDir  = 1; // 오름차순 = 1
		this.orderType = 14; // PORT_NAME = 14
	},
	onAdcChange : function() 
	{
		with(this)
		{
			loadStatisticsContent(adcSetting.getAdc());
		}
	},
	loadStatisticsContent : function(adc, orderType, orderDir)
	{
		this.adc = adc;		
		with (this)
		{			
			if (!adc)
			{
				return;
			}
			var params =
			{
				"adc.index"	: adc.index,
				"adc.type"	: adc.type,
				"adc.name"	: adc.name,
				"orderType" : orderType,
				"orderDir"   : orderDir
			};
			ajaxManager.runHtmlExt({

				url : "monitor/loadStatisticsContent.action",				
				data		: params,
				target		: "#wrap .contents",
				successFn	: function(data)
				{
					header.setActiveMenu('MonitorStatistics');	
					
					if (adcSetting.getAdcStatus() != "available") 
					{
						if ($('.adcPortStatusList').size() > 0)
						{
							registerListContentEvents();
							restoreStatisticsContent();
							graphInfoChange();
							loadChartContents(adc,selectedTraffic,interfaceNameList);
						}
						else
						{
							$('.contents_area .nulldata').removeClass("none");
							$('.contents_area .nulldata').css('background-image', "url(imgs/monitoring/statistics_null.gif)");
							$('.contents_area .successdata').addClass("none");
						}
					}
					else
					{
						registerListContentEvents();
						restoreStatisticsContent();
						graphInfoChange();
						loadChartContents(adc,selectedTraffic,interfaceNameList);
					}					
				},
				completeFn : function()
				{
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_STATISTICS_LOADFAIL, jqXhr);
//					exceptionEvent();
				}	
			});
		} 
	},
	loadRefreshContents : function (adc,selectedTraffic,interfaceNameList, orderType, orderDir)
	{
		//TODO
		this.selectedTraffic = selectedTraffic;
		this.interfaceNameList = interfaceNameList;
		this.adc = adc;
		with(this)
		{
			if (!adc)
			{
				return;
			}
			var params =
			{
				"adc.index"	: adc.index,
				"adc.type"	: adc.type,
				"adc.name"	: adc.name,
				"orderType" : orderType,
				"orderDir"   : orderDir
			};
			ajaxManager.runHtmlExt({

				url : "monitor/loadRefreshContents.action",				
				data		: params,
				target		: "#wrap .contents",
				successFn	: function(data)
				{								
					registerListContentEvents();
					restoreStatisticsContent();
					refreshListCheck(interfaceNameList);
					loadChartContents(adc,selectedTraffic,interfaceNameList);
				},
				completeFn : function()
				{
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_STATISTICS_LOADFAIL, jqXhr);
//					exceptionEvent();
				}	
			});
		}		
	},	
	loadChartContents : function(adc,selectedTraffic,interfaceNameList)
	{
		this.selectedTraffic = selectedTraffic;
		this.interfaceNameList = interfaceNameList;
		this.adc = adc;		
		with(this)
		{
			var params =
			{
				"interfaceNameList" : this.interfaceNameList,	
				"selectedTraffic" : this.selectedTraffic,
				"adc.index" : this.adc.index,
			};
			params['searchTimeMode'] = selectedSearchTimeMode;
			params['hour'] = searchLastHours;
			params['startTime'] = $('input[name="startTime"]').val();
			params['endTime'] = $('input[name="endTime"]').val();
			
			ajaxManager.runJsonExt({
				url			:"monitor/loadGraphData.action",
				data		:params,
				successFn	:function(data)
				{
					GenerateStatisticsChartData(data, selectedTraffic);
					if(selectedSearchTimeMode == 'periodMode')
					{
						if(!validateDaterefresh())
						{
							return false;
						}
					}
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_STATISTICS_USAGLOADFAIL, jqXhr);
//					exceptionEvent();
				}			
			});
		}
	},		
		// 모니터링 ADC 인터페이스 차트
	GenerateStatisticsChartData : function(data, selectedTraffic)
	{
		var maxData = [];
		var avgData = 0;
		var chartData = [];
		var chartDataList = data.statistic5data;
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
					var first = undefined;
					var firstName = undefined;
					var second = undefined;
		        	var secondName = undefined;
		        	var third = undefined;
					var thirdName = undefined;
					var fourth = undefined;
					var fourthName = undefined;
					var fifth = undefined;
					var fifthName = undefined;
					var sixth = undefined;
					var sixthName = undefined;
					var seventh = undefined;
					var seventhName = undefined;
					var eighth = undefined;
					var eighthName = undefined;
					var ninth = undefined;
					var ninthName = undefined;
					var tenth = undefined;
					var tenthName = undefined;
					var eleventh = undefined;
					var eleventhName = undefined;
					var twelfth = undefined;
					var twelfthName = undefined;
					var thirteenth = undefined;
					var thirteenthName = undefined;
					var fourteenth = undefined;
					var fourteenthName = undefined;
					var fifteenth = undefined;
					var fifteenthName = undefined;
					var sixteenth = undefined;
					var sixteenthName = undefined;
					var seventeenth = undefined;
					var seventeenthName = undefined;
					var eighteenth = undefined;
					var eighteenthName = undefined;
					var nineteenth = undefined;
					var nineteenthName = undefined;
					var twentieth = undefined;
					var twentiethName = undefined;
					var twentyfirst = undefined;
					var twentyfirstName = undefined;
					var twentysecond = undefined;
					var twentysecondName = undefined;
					var twentythird = undefined;
					var twentythirdName = undefined;
					var twentyfourth = undefined;
					var twentyfourthName = undefined;
					var twentyfifth = undefined;
					var twentyfifthName = undefined;
					var twentysixth = undefined;
					var twentysixthName = undefined;
					var twentyseventh = undefined;
					var twentyseventhName = undefined;
					var twentyeighth = undefined;
					var twentyeighthName = undefined;
					var twentyninth = undefined;
					var twentyninthName = undefined;					
					var thirtieth = undefined;
					var thirtiethName = undefined;
					var thirtyfirst=undefined;
					var thirtyfirstName =undefined;					
					
					if (column.firstValue < 0)
					{				
					}
					else if (column.firstName != null && column.firstName.length > 0)
					{
						first = column.firstValue;
						firstName = column.firstName;
						maxData.push(column.firstValue);
						avgData = avgData + column.firstValue;
					}
					
					if (column.secondValue < 0)
					{				
					}
					else if (column.secondName != null && column.secondName.length > 0)
					{
			        	second = column.secondValue;
			        	secondName = column.secondName;
			        	maxData.push(column.secondValue);
			        	avgData = avgData + column.secondValue;
					}
					
					if (column.thirdValue < 0)
					{				
					}
					else if (column.thirdName != null && column.thirdName.length > 0)
					{
						third = column.thirdValue;
						thirdName = column.thirdName;
						maxData.push(column.thirdValue);
						avgData = avgData + column.thirdValue;
					}
					
					if (column.fourthValue < 0)
					{				
					}
					else if (column.fourthName != null && column.fourthName.length > 0)
					{
						fourth = column.fourthValue;
						fourthName = column.fourthName;
						maxData.push(column.fourthValue);
						avgData = avgData + column.thirdValue;
					}
					
					if (column.fifthValue < 0)
					{				
					}
					else if (column.fifthName != null && column.fifthName.length > 0)
					{
						fifth = column.fifthValue;
						fifthName = column.fifthName;
						maxData.push(column.fifthValue);
						avgData = avgData + column.thirdValue;
					}
					
					if (column.sixValue < 0)
					{				
					}
					else if (column.sixName != null && column.sixName.length > 0)
					{
						sixth = column.sixValue;
						sixthName = column.sixName;
						maxData.push(column.sixValue);
						avgData = avgData + column.thirdValue;
					}
					
					if (column.sevenValue < 0)
					{				
					}
					else if (column.sevenName != null && column.sevenName.length > 0)
					{
						seventh = column.sevenValue;
						seventhName = column.sevenName;
						maxData.push(column.sevenValue);
						avgData = avgData + column.sevenValue;
					}
					
					if (column.eightValue < 0)
					{				
					}
					else if (column.eightName != null && column.eightName.length > 0)
					{
						eighth = column.eightValue;
						eighthName = column.eightName;
						maxData.push(column.eightValue);
						avgData = avgData + column.eightValue;
					}
					
					if (column.nineValue < 0)
					{				
					}
					else if (column.nineName != null && column.nineName.length > 0)
					{
						ninth = column.nineValue;
						ninthName = column.nineName;
						maxData.push(column.nineValue);
						avgData = avgData + column.nineValue;
					}
					
					if (column.tenValue < 0)
					{				
					}
					else if (column.tenName != null && column.tenName.length > 0)
					{
						tenth = column.tenValue;
						tenthName = column.tenName;
						maxData.push(column.tenValue);
						avgData = avgData + column.tenValue;
					}
					
					if (column.elevenValue < 0)
					{				
					}
					else if (column.elevenName != null && column.elevenName.length > 0)
					{
						eleventh = column.elevenValue;
						eleventhName = column.elevenName;
						maxData.push(column.elevenValue);
						avgData = avgData + column.elevenValue;
					}
					
					if (column.twelveValue < 0)
					{				
					}
					else if (column.twelveName != null && column.twelveName.length > 0)
					{
						twelfth = column.twelveValue;
						twelfthName = column.twelveName;
						maxData.push(column.twelveValue);
						avgData = avgData + column.twelveValue;
					}
					
					if (column.thirteenValue < 0)
					{				
					}
					else if (column.thirteenName != null && column.thirteenName.length > 0)
					{
						thirteenth = column.thirteenValue;
						thirteenthName = column.thirteenName;
						maxData.push(column.thirteenValue);
						avgData = avgData + column.thirteenValue;
					}
					
					if (column.fourteenValue < 0)
					{				
					}
					else if (column.fourteenName != null && column.fourteenName.length > 0)
					{
						fourteenth = column.fourteenValue;
						fourteenthName = column.fourteenName;
						maxData.push(column.fourteenValue);
						avgData = avgData + column.fourteenValue;
					}
					
					if (column.fifteenValue < 0)
					{				
					}
					else if (column.fifteenName != null && column.fifteenName.length > 0)
					{
						fifteenth = column.fifteenValue;
						fifteenthName = column.fifteenName;
						maxData.push(column.fifteenValue);
						avgData = avgData + column.fifteenValue;
					}
					
					if (column.sixteenValue < 0)
					{				
					}
					else if (column.sixteenName != null && column.sixteenName.length > 0)
					{
						sixteenth = column.sixteenValue;
						sixteenthName = column.sixteenName;
						maxData.push(column.sixteenValue);
						avgData = avgData + column.sixteenValue;
					}
					
					if (column.seventeenValue < 0)
					{				
					}
					else if (column.seventeenName != null && column.seventeenName.length > 0)
					{
						seventeenth = column.seventeenValue;
						seventeenthName = column.seventeenName;
						maxData.push(column.seventeenValue);
						avgData = avgData + column.seventeenValue;
					}
					
					if (column.eighteenValue < 0)
					{				
					}
					else if (column.eighteenName != null && column.eighteenName.length > 0)
					{
						eighteenth = column.eighteenValue;
						eighteenthName = column.eighteenName;
						maxData.push(column.eighteenValue);
						avgData = avgData + column.eighteenValue;
					}
					
					if (column.nineteenValue < 0)
					{				
					}
					else if (column.nineteenName != null && column.nineteenName.length > 0)
					{
						nineteenth = column.nineteenValue;
						nineteenthName = column.nineteenName;
						maxData.push(column.nineteenValue);
						avgData = avgData + column.nineteenValue;
					}
					
					if (column.twentyValue < 0)
					{				
					}
					else if (column.twentyName != null && column.twentyName.length > 0)
					{
						twentieth = column.twentyValue;
						twentiethName = column.twentyName;
						maxData.push(column.twentyValue);
						avgData = avgData + column.twentyValue;
					}
					
					if (column.tOneValue < 0)
					{				
					}
					else if (column.tOneName != null && column.tOneName.length > 0)
					{
						twentyfirst = column.tOneValue;
						twentyfirstName = column.tOneName;
						maxData.push(column.tOneValue);
						avgData = avgData + column.tOneValue;
					}
					
					if (column.tTwoValue < 0)
					{				
					}
					else if (column.tTwoName != null && column.tTwoName.length > 0)
					{
						twentysecond = column.tTwoValue;
						twentysecondName = column.tTwoName;
						maxData.push(column.tTwoValue);
						avgData = avgData + column.tTwoValue;
					}
					
					if (column.tThreeValue < 0)
					{				
					}
					else if (column.tThreeName != null && column.tThreeName.length > 0)
					{
						twentythird = column.tThreeValue;
						twentythirdName = column.tThreeName;
						maxData.push(column.tThreeValue);
						avgData = avgData + column.tThreeValue;
					}
					
					if (column.tFourValue < 0)
					{				
					}
					else if (column.tFourName != null && column.tFourName.length > 0)
					{
						twentyfourth = column.tFourValue;
						twentyfourthName = column.tFourName;
						maxData.push(column.tFourValue);
						avgData = avgData + column.tFourValue;
					}
					
					if (column.tFiveValue < 0)
					{				
					}
					else if (column.tFiveName != null && column.tFiveName.length > 0)
					{
						twentyfifth = column.tFiveValue;
						twentyfifthName = column.tFiveName;
						maxData.push(column.tFiveValue);
						avgData = avgData + column.tFiveValue;
					}
					
					if (column.tSixValue < 0)
					{				
					}
					else if (column.tSixName != null && column.tSixName.length > 0)
					{
						twentysixth = column.tSixValue;
						twentysixthName = column.tSixName;
						maxData.push(column.tSixValue);
						avgData = avgData + column.tSixValue;
					}
					
					if (column.tSevenValue < 0)
					{				
					}
					else if (column.tSevenName != null && column.tSevenName.length > 0)
					{
						twentyseventh = column.tSevenValue;
						twentyseventhName = column.tSevenName;
						maxData.push(column.tSevenValue);
						avgData = avgData + column.tSevenValue;
					}
					
					if (column.tEightValue < 0)
					{				
					}
					else if (column.tEightName != null && column.tEightName.length > 0)
					{
						twentyeighth = column.tEightValue;
						twentyeighthName = column.tEightName;
						maxData.push(column.tEightValue);
						avgData = avgData + column.tEightValue;
					}
					
					if (column.tNineValue < 0)
					{				
					}
					else if (column.tNineName != null && column.tNineName.length > 0)
					{
						twentyninth = column.tNineValue;
						twentyninthName = column.tNineName;
						maxData.push(column.tNineValue);
						avgData = avgData + column.tNineValue;
					}
					
					if (column.thirtyValue < 0)
					{				
					}
					else if (column.thirtyName != null && column.thirtyName.length > 0)
					{
						thirtieth = column.thirtyValue;
						thirtiethName = column.thirtyName;
						maxData.push(column.thirtyValue);
						avgData = avgData + column.thirtyValue;
					}
					
					if (column.thrOneValue < 0)
					{				
					}
					else if (column.thrOneName != null && column.thrOneName.length > 0)
					{
						thirtyfirst = column.thrOneValue;
						thirtyfirstName = column.thrOneName;
						maxData.push(column.thrOneValue);
						avgData = avgData + column.thrOneValue;
					}					
							
					var dataObject =
					{
						occurredTime : date,
						firstValue : first,
						secondValue : second,
						thirdValue : third,
						fourthValue : fourth,
						fifthValue : fifth,
						sixthValue : sixth,
						seventhValue : seventh,
					 	eighthValue : eighth,
					 	ninthValue : ninth,
					 	tenthValue : tenth,
					 	eleventhValue : eleventh,
					 	twelfthValue : twelfth,
					 	thirteenthValue : thirteenth,
					 	fourteenthValue : fourteenth,
					 	fifteenthValue : fifteenth,
					 	sixteenthValue : sixteenth,
					 	seventeenthValue : seventeenth,
					 	eighteenthValue : eighteenth,
					 	nineteenthValue : nineteenth,
						twentiethValue : twentieth,
						twentyfirstValue : twentyfirst,
						twentysecondValue : twentysecond,
						twentythirdValue : twentythird,
						twentyfourthValue : twentyfourth,
						twentyfifthValue : twentyfifth,
						twentysixthValue : twentysixth,
						twentyseventhValue : twentyseventh, 
						twentyeighthValue : twentyeighth,
						twentyninthValue : twentyninth,
						thirtiethValue : thirtieth,
						thirtyfirstValue : thirtyfirst,
						
						firstName : firstName,
						secondName : secondName,
						thirdName : thirdName,
						fourthName : fourthName,
						fifthName : fifthName,
						sixthName : sixthName,
						seventhName : seventhName,
						eighthName : eighthName,
						ninthName : ninthName,
						tenthName : tenthName,
						eleventhName : eleventhName,
						twelfthName : twelfthName,
						thirteenthName : thirteenthName,
						fourteenthName : fourteenthName,
						fifteenthName : fifteenthName,
						sixteenthName : sixteenthName,
						seventeenthName : seventeenthName,
						eighteenthName : eighteenthName,
						nineteenthName : nineteenthName,
						twentiethName : twentiethName,
						twentyfirstName : twentyfirstName,
						twentysecondName : twentysecondName,
						twentythirdName : twentythirdName,
						twentyfourthName : twentyfourthName,
						twentyfifthName : twentyfifthName,
						twentysixthName : twentysixthName,
						twentyseventhName : twentyseventhName, 
						twentyeighthName : twentyeighthName,
						twentyninthName : twentyninthName,
						thirtiethName : thirtiethName,
						thirtyfirstName : thirtyfirstName
					};
					// add object to dataProvider array
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
				
		//var maxPos = Math.max.apply(null,jQuery.map(maxData, function(o){return o.value;}));
		var maxValue = Math.max.apply( Math, maxData);
		var avgValue = (avgData / maxData.length);
		var chartunit = "";
		if (selectedTraffic == "Bytes")
		{
			chartunit = VAR_COMMON_BPS;
		}
		else if (selectedTraffic == "Packets")
		{
			chartunit = VAR_COMMON_PPS;
		}
		else
		{
			chartunit = VAR_STATISTICS_AFEW;
		} 
		var chartOption =
		{
			 min : 0,
			 max : null,			
			 chartname : "StatisticsChart",
			 axistitle : chartunit,
			 maxPos : null,
			 cursorColor : "#0e7023"
		};
		obchart.OBMultiLineChartViewer(chartData, chartOption);
		this.displayNoticeData(maxValue, avgValue);
		},	
	
	restoreStatisticsContent : function()
	{
		with (this)
		{			
			if (!selectedTraffic)
			{
				selectedTraffic = $('input[name="searchTrafficMode"]:checked').val();
			}
			else
			{
				$('input[value="' + selectedTraffic + '"]').attr("checked", "checked");
				if (selectedTraffic == "Bytes")
				{
					var $tbody = $('.contents_area #TrafficModeCange').filter(':last');
					$tbody.empty();
					var html = '';
					
					html += VAR_STATISTICS_TRAFFICBPS;
					
					$tbody.html(html);
				}
				else if (selectedTraffic == "Packets")
				{
					var $tbody = $('.contents_area #TrafficModeCange').filter(':last');
					$tbody.empty();
					var html = '';
					
					html += VAR_STATISTICS_TRAFFICPPS;
					
					$tbody.html(html);
				}
				else if (selectedTraffic == "Errors")
				{
					var $tbody = $('.contents_area #TrafficModeCange').filter(':last');
					$tbody.empty();
					var html = '';
					
					html += VAR_STATISTICS_ERRGEN;
					
					$tbody.html(html);
				}
				else if (selectedTraffic == "Drops")
				{
					var $tbody = $('.contents_area #TrafficModeCange').filter(':last');
					$tbody.empty();
					var html = '';
					
					html += VAR_STATISTICS_LOSSGEN;
					
					$tbody.html(html);
				}	
			}
			
			if (!selectedSearchTimeMode)
			{
				selectedSearchTimeMode = $('input[name="searchTimeMode"]:checked').val();
			}
			else 
			{
				$('input[value="' + selectedSearchTimeMode + '"]').attr("checked", "checked");
			}
			if (!searchLastHours)
			{
				searchLastHours = $('#lastHours option:selected').val();
			}
			else
			{
				$('#lastHours option').each(function() 
				{
					if ($(this).val() == searchLastHours)
					{
						$(this).attr("selected", "selected");
						return false;
					}
				});
			}
			if (!searchStartTime)
			{
				searchStartTime = new Date();
			}
			if (!searchEndTime)
			{
				searchEndTime = new Date();
			}
			
			$('#startTime').click(function(e)
			{	
				$('#periodMode').attr('checked', 'checked');
			});
			
			$('#endTime').click(function(e)
			{	
				$('#periodMode').attr('checked', 'checked');
			});
			
			$('input[name="startTime"]').datepicker({
				maxDate: "0",
				dateFormat : "yy-mm-dd",
				showOn: "button",
				buttonImage: "imgs/meun/btn_calendar.png",
				buttonImageOnly: true,
				defaultDate: searchStartTime,
				onSelect: function(dateText, inst)
				{
					$('#periodMode').attr('checked', 'checked');
					selectedSearchTimeMode = $('input[name="searchTimeMode"]:checked').val();
					searchStartTime = $("input[name='startTime']").datepicker("getDate");
				}
			});
			
			$('input[name="endTime"]').datepicker({
				maxDate: "0",
				dateFormat : "yy-mm-dd",
				showOn: "button",
				buttonImage: "imgs/meun/btn_calendar.png",
				buttonImageOnly: true,
				defaultDate: searchEndTime,
				onSelect: function(dateText, inst)
				{
					$('#periodMode').attr('checked', 'checked');
					selectedSearchTimeMode = $('input[name="searchTimeMode"]:checked').val();
					searchEndTime = $("input[name='endTime']").datepicker("getDate");
				}
			});
			
			$('input[name="startTime"]').val($.datepicker.formatDate('yy-mm-dd', searchStartTime));
			$('input[name="endTime"]').val($.datepicker.formatDate('yy-mm-dd', searchEndTime));

			$('#lastHours').change(function() 
			{
				searchLastHours = $('#lastHours option:selected').val();
			});	
			
			$('input[name="searchTimeMode"]').change(function() 
			{
				var $searchTimeModeChecked = $('input[name="searchTimeMode"]:checked');
				if ($searchTimeModeChecked.attr('id') == 'periodMode')
				{
					var $targetTime = $('.auto_refresh_group input[name="time"]').eq(0);
					$targetTime.attr("checked", "checked");
					$targetTime.click();
				}
				selectedSearchTimeMode = $searchTimeModeChecked.val();
			});
			
			$('input[name="searchTrafficMode"]').change(function()
			{
				selectedTraffic = $('input[name="searchTrafficMode"]:checked').val();						
			});
		}
	},	
	registerListContentEvents : function() 
	{
		with (this)
		{
									
			$('#exportCssLnk').click(function(event){
				event.preventDefault();
				if(selectedSearchTimeMode == 'periodMode')
				{
					if(!validateDaterefresh())
					{
						return false;
					}
				}	
				_checkExportStatisticsDataExist();
				
			});
			$('.allChk').click(function(e){
				var isChecked = $(this).is(':checked');
				$(this).parents('table.Board').find('.indChk').attr('checked',isChecked);
				
			});
//			$('input[name="searchTimeMode"]').change(function() {
//				var $searchTimeModeChecked = $('input[name="searchTimeMode"]:checked');
//				if ($searchTimeModeChecked.attr('id') == 'periodMode')
//				{
//					var $targetTime = $('.auto_refresh_group input[name="time"]').eq(0);
//					$targetTime.attr("checked", "checked");
//					$targetTime.click();
//				}
//				selectedSearchTimeMode = $searchTimeModeChecked.val();
//			});
			$('#refresh').click(function(e)
			{
				if(selectedSearchTimeMode == 'periodMode')
				{
					if(!validateDaterefresh())
					{
						return false;
					}
				}				
				graphInfoChange();
				if(interfaceNameList == null) // 리스트에서 체크가 하나도 안되있는 경우
				{					
					return false;
				}
				e.preventDefault();		
				loadRefreshContents(adc, selectedTraffic, interfaceNameList);				
			});	
			$('.orderDir_Desc').click(function(e)
			{
				e.preventDefault();
				orderDir =  $(this).find('.orderDir').text();
				orderType =  $(this).find('.orderType').text();						
				loadStatisticsContent(adc , orderDir , orderType);
			});			
			$('.orderDir_Asc').click(function(e)
			{
				e.preventDefault();
				orderDir =  $(this).find('.orderDir').text();
				orderType =  $(this).find('.orderType').text();	
				loadStatisticsContent(adc , orderDir , orderType);
			});			
			$('.orderDir_None').click(function(e)
			{
				e.preventDefault();
				orderDir =  $(this).find('.orderDir').text();
				orderType =  $(this).find('.orderType').text();	
				loadStatisticsContent(adc , orderDir , orderType);
			});	
		}
	},
	displayNoticeData : function(maxValue, avgValue)
	{
		with(this)
		{
			if (maxValue == null || maxValue === undefined || avgValue == null || avgValue === undefined)
			{
				return;
			}
			if (maxValue < 0)
			{
				maxValue = "-";
			}
			if (avgValue < 0)
			{
				avgValue = "-";
			}
			var $max_notice_area = $('.contents_area #notice_chartdata_max').filter(':last');
			var $avg_notice_area = $('.contents_area #notice_chartdata_avg').filter(':last');
			$max_notice_area.empty();
			$avg_notice_area.empty();
			var html = '';
			
			if (selectedTraffic == "Bytes")
			{			
				if (maxValue < 1000 || maxValue == "-")
				{
					$max_notice_area.html(maxValue +'&nbsp;<span class="unit">' + VAR_COMMON_BPS + '</span>');
				}						
				else if (maxValue < 1000000)
				{
					maxValue = maxValue/1000;
					$max_notice_area.html(maxValue.toFixed(1) +'&nbsp;<span class="unit">' + VAR_COMMON_KBPS + '</span>');						
				}
				else if (maxValue < 1000000000)
				{
					maxValue = maxValue/1000000;
					$max_notice_area.html(maxValue.toFixed(1) +'&nbsp;<span class="unit">' + VAR_COMMON_MBPS + '</span>');	
				}
				else if (maxValue < 1000000000000)
				{
					maxValue = maxValue/1000000000;
					$max_notice_area.html(maxValue.toFixed(1) +'&nbsp;<span class="unit">' + VAR_COMMON_GBPS + '</span>');	
				}
				else if (maxValue < 1000000000000000)
				{
					maxValue = maxValue/1000000000000;
					$max_notice_area.html(maxValue.toFixed(1) +'&nbsp<span class="unit">' + VAR_COMMON_TBPS + '</span>');	
				}
				else
				{}
				
				if (avgValue < 1000 || avgValue == "-")
				{
					$avg_notice_area.html(avgValue.toFixed(1) +'&nbsp;<span class="unit">' + VAR_COMMON_BPS + '</span>');
				}						
				else if (avgValue < 1000000)
				{
					avgValue = avgValue/1000;
					$avg_notice_area.html(avgValue.toFixed(1) +'&nbsp;<span class="unit">' + VAR_COMMON_KBPS + '</span>');						
				}
				else if (avgValue < 1000000000)
				{
					avgValue = avgValue/1000000;
					$avg_notice_area.html(avgValue.toFixed(1) +'&nbsp;<span class="unit">' + VAR_COMMON_MBPS + '</span>');	
				}
				else if (avgValue < 1000000000000)
				{
					avgValue = avgValue/1000000000;
					$avg_notice_area.html(avgValue.toFixed(1) +'&nbsp;<span class="unit">' + VAR_COMMON_GBPS + '</span>');	
				}
				else if (avgValue < 1000000000000000)
				{
					avgValue = avgValue/1000000000000;
					$avg_notice_area.html(avgValue.toFixed(1) +'&nbsp<span class="unit">' + VAR_COMMON_TBPS + '</span>');	
				}
				else
				{}			
			}
			else if(selectedTraffic == "Packets")
			{
				if (maxValue < 1000 || maxValue == "-")
				{
					$max_notice_area.html(maxValue +'&nbsp;<span class="unit">' + VAR_COMMON_PPS + '</span>');
				}						
				else if (maxValue < 1000000)
				{
					maxValue = maxValue/1000;
					$max_notice_area.html(maxValue.toFixed(1) +'&nbsp;<span class="unit">' + VAR_COMMON_KPPS + '</span>');						
				}
				else if (maxValue < 1000000000)
				{
					maxValue = maxValue/1000000;
					$max_notice_area.html(maxValue.toFixed(1) +'&nbsp;<span class="unit">' + VAR_COMMON_MPPS + '</span>');	
				}
				else if (maxValue < 1000000000000)
				{
					maxValue = maxValue/1000000000;
					$max_notice_area.html(maxValue.toFixed(1) +'&nbsp;<span class="unit">' + VAR_COMMON_GPPS + '</span>');	
				}
				else if (maxValue < 1000000000000000)
				{
					maxValue = maxValue/1000000000000;
					$max_notice_area.html(maxValue.toFixed(1) +'&nbsp<span class="unit">' + VAR_COMMON_TPPS + '</span>');	
				}
				else
				{}
				
				if (avgValue < 1000 || avgValue == "-")
				{
					$avg_notice_area.html(avgValue.toFixed(1) +'&nbsp;<span class="unit">' + VAR_COMMON_PPS + '</span>');
				}						
				else if (avgValue < 1000000)
				{
					avgValue = avgValue/1000;
					$avg_notice_area.html(avgValue.toFixed(1) +'&nbsp;<span class="unit">' + VAR_COMMON_KPPS + '</span>');						
				}
				else if (avgValue < 1000000000)
				{
					avgValue = avgValue/1000000;
					$avg_notice_area.html(avgValue.toFixed(1) +'&nbsp;<span class="unit">' + VAR_COMMON_MPPS + '</span>');	
				}
				else if (avgValue < 1000000000000)
				{
					avgValue = avgValue/1000000000;
					$avg_notice_area.html(avgValue.toFixed(1) +'&nbsp;<span class="unit">' + VAR_COMMON_GPPS + '</span>');	
				}
				else if (avgValue < 1000000000000000)
				{
					avgValue = avgValue/1000000000000;
					$avg_notice_area.html(avgValue.toFixed(1) +'&nbsp<span class="unit">' + VAR_COMMON_TPPS + '</span>');	
				}
				else
				{}
			}
			else
			{
				$max_notice_area.html(maxValue +'&nbsp;<span class="unit">' + VAR_COMMON_AFEW + '</span>');	
				$avg_notice_area.html(avgValue +'&nbsp<span class="unit">' + VAR_COMMON_AFEW + '</span>');	
			}
			
		}
	},
	validateDaterefresh : function()
	{
		if ($('input[name="startTime"]').val() > $('input[name="endTime"]').val())
		{
			var toDate = new Date();
			var dayOfMonth = toDate.getDate();
			var fromDate = new Date();
			fromDate.setDate(dayOfMonth - 6);
			$('input[name="startTime"]').val(parseDateTimeStringShort(fromDate));
			$('input[name="endTime"]').val(parseDateTimeStringShort(toDate));
			this.searchStartTime = $("input[name='startTime']").datepicker("getDate");
			this.searchEndTime = $("input[name='endTime']").datepicker("getDate");
			$.obAlertNotice(VAR_COMMON_DATEERROR);
			return false;
		}		
		return true;		
	},
	graphInfoChange : function()
	{
		with (this)
		{
			selectedTraffic =$('input[name="searchTrafficMode"]:checked').val();	
			interfaceNameList =  $('.indChk').filter(':checked').map(function() {
				return $(this).val();
			}).get();
			FlowitUtil.log("interfaceNameList: ", interfaceNameList);
			
			if($('.statistic > tr').length == 0) //포트가 아예 없는 경우 
			{
				interfaceNameList = null;
				return;
			}	
			else
			{		
				if(interfaceNameList.length == 0) // 리스트에서 체크가 하나도 안되있는 경우
				{
					$.obAlertNotice(VAR_STATISTICS_CHECKINSEL);
					interfaceNameList = null;
					return;
				}
			}
		}
	},	
	refreshListCheck : function(interfaceNameList)
	{
		with (this)
		{		
			$('.indChk').attr('checked', false);		
			$('.indChk').each(function() 
			{
				for (var i=0; i < interfaceNameList.length; i++)
				{
					if ($(this).val() == interfaceNameList[i])
					{
						$(this).attr("checked", true);						
					}
				}
			});			
		}
	},
	exportCss : function()
	{
		var interfaceName = "";
		with(this)
		{
			for (var i=0; i < this.interfaceNameList.length; i++)
			{
				interfaceName += "&interfaceNameList[" + i + "]=" + encodeURIComponent(this.interfaceNameList[i]);
			}			
			var params = "selectedTraffic=" + this.selectedTraffic +
							"&adc.index=" + this.adc.index + "&searchTimeMode=" + selectedSearchTimeMode +
								"&hour=" + searchLastHours + "&startTime=" +$('input[name="startTime"]').val() +
									"&endTime=" + $('input[name="endTime"]').val();
			var url = "monitor/downloadStatistics.action?" + params + interfaceName;			
			$('#downloadFrame').attr('src',url);
			/*var option = "height=280,width=380,scrollbars=no,toolbar=no,location=no,status=no,menubar=no,resizeable=no,left=0,top=0";
			window.open(url,"",option);*/			
		}
	},
	/*exportCss : function()
	{
		with(this)
		{
			var url = "monitor/downloadStatistics.action?adc.index=" + adc.index +"&searchTimeMode=" + selectedSearchTimeMode + "&hour=" + searchLastHours + "&startTime=" + $('input[name="startTime"]').val() + "&endTime=" + $('input[name="endTime"]').val();
			$('#downloadFrame').attr('src',url);
		}
	},*/
	_checkExportStatisticsDataExist : function() //내보내기 시 내보낼 데이터 유무 체크 하여 없을 경우 알림 창
	{
		with(this)
		{
			var params = {
					"interfaceNameList" : this.interfaceNameList,	
					"selectedTraffic" : this.selectedTraffic,
					"adc.index" : this.adc.index,
				};
				params['searchTimeMode'] = selectedSearchTimeMode;
				params['hour'] = searchLastHours;
				params['startTime'] = $('input[name="startTime"]').val();
				params['endTime'] = $('input[name="endTime"]').val();

			ajaxManager.runJsonExt
			({
				url : "monitor/checkExportStatisticsDataExist.action",
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
					$.obAlertAjaxError(VAR_COMMON_EXPDATAEXIST, jqXhr);
//					exceptionEvent();
				}
			});
		}
	}
});