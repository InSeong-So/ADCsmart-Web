var FaultDiagnosis = Class.create({
	initialize : function() 
	{		
		this.refreshIntervalSeconds = 3;
		this.diagnosisTimeTimer = undefined;
		this.chartTimeTimer = undefined;		
		this.diagnosisCpuChart = undefined;
		this.diagnosisMemoryChart = undefined;
		this.CpuUsageGaugeobj = undefined;
	    this.MemoryUsageGaugeobj = undefined;
	    this.ProgressBarobj = undefined;
	    this.diagnosisPopupAliveFlag = undefined;
	    this.logKeyObj = undefined;
	    this.adcName = undefined;
	},
	loadFaultDiagnosisPopup : function(faultCheckIndex, name)
	{
		with(this)
		{
			diagnosisPopupAliveFlag = true;			
			showPopup({
				'id' : '#modify_virtualService',
				'width' : '800px',
				'height' : '455px'
			});
			logKeyObj = faultCheckIndex[0].logKey;
			var categoryIndex = faultCheckIndex[0].obj.category;
			var adcIndex = faultCheckIndex[0].obj.index;
			adcName = name;
			
			$('.progress').css('display','');
			$('.complete').css('display','none');
			$('.cancel').css('display','none');
			$('.fail').css('display','none');									
			//GenerateGauge();
			GenerateProgressBar();
			popUpBtnClickEvent(adcName);
						
			if (faultCheckIndex.length > 1)
			{
				groupDiagnosis(faultCheckIndex);
			}
			else
			{
				this.diagnosisTimeTimer = setInterval(function ()
				{
					loadDiagnosisStateInfo(logKeyObj, false);
				}, 1000);
				displayRealTimeSystemUsage(categoryIndex, adcIndex);
			}			
		}
	},
	// DiagnosisTimer 초기화 부분
	clearDiagnosisTimer : function() 
	{
		with (this)
		{
			if (null != this.diagnosisTimeTimer)
			{
				clearInterval(this.diagnosisTimeTimer);
				this.diagnosisTimeTimer = null;
			}
		}
	},
	//TODO
	groupDiagnosis : function(faultCheckIndex)
	{
		with(this)
		{
			var $groupSelectArea = $('.pop_type_wrap .GroupSelectArea');
			$groupSelectArea.css('display', '');
			var $groupSelectBox = $('.pop_type_wrap .GroupSelectArea .adcCbx');
			$groupSelectBox.empty();
			var html = '';			
			for (var i=0; i < faultCheckIndex.length; i++)
			{
				html += '<option value="'+ faultCheckIndex[i].obj.index + '" id=" ' + faultCheckIndex[i].logKey + '">'
				+ faultCheckIndex[i].obj.name + '</option>';
			}
			html += '</select>';	
			$groupSelectBox.html(html);
			this.diagnosisTimeTimer = setInterval(function ()
			{
				loadDiagnosisStateInfo(faultCheckIndex[0].logKey, false);
			}, 1000);
			displayRealTimeSystemUsage(2, faultCheckIndex[0].obj.index);	
			
			$('.pop_type_wrap .adcCbx').change(function(e)
			{
				with(this)	//TODO With this 영향
				{
					clearDiagnosisTimer();
					clearChartTimer();
					$('.progress').css('display','');
					$('.complete').css('display','none');
					$('.cancel').css('display','none');
					$('.fail').css('display','none');					
					
					logKeyObj = parseInt($(this).children('option').filter(':selected').attr('id'));
					var SelectcategoryIndex = 2;
					var SelectadcIndex = $(this).children('option').filter(':selected').val();
					
					loadDiagnosisStateInfo(logKeyObj, true);
					diagnosisTimeTimer = setInterval(function ()
					{
						loadDiagnosisStateInfo(logKeyObj, true);
					}, 1000);
				}
				displayRealTimeSystemUsage(SelectcategoryIndex, SelectadcIndex);				
			});
		}
	},
	// 진단 진행 상태 데이터를 Get 한다.
	loadDiagnosisStateInfo : function(logKey, afterSelectedKey)
	{
		with(this)
		{
			var params = {};
			params["logkey"] = logKey;		
			ajaxManagerOB.runJsonExt({
				url			: "faultDiagnosis/loadDiagnosisStateInfo.action",
				data		: params,
				successFn	: function(data)
				{					
					updateDiagnosisState(data, afterSelectedKey);
				},
				completeFn	: function(data)
				{						
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_FAULTDIAGNOSIS_STATUSLOAD, jqXhr);
					clearDiagnosisTimer();
				}
			});
		}
	},
	// 진단 진행 상태 데이터를 UPDATE 한다.
	updateDiagnosisState : function(data, afterSelectedKey)
	{
		with (this)
		{
			// 시간 파싱
			var elapsedTime = new Date();
			elapsedTime.setHours(0, 0, 0, data.diagnosisStateInfo.elapsedTime);		
			var elapsedTimeHour = elapsedTime.getHours();
			if (elapsedTimeHour < 10)
			{
				elapsedTimeHour = '0' + elapsedTimeHour;
			}
			var elapsedTimeMin = elapsedTime.getMinutes();
			if (elapsedTimeMin < 10)
			{
				elapsedTimeMin = '0' + elapsedTimeMin;
			}
			var elapsedTimeSec = elapsedTime.getSeconds();
			if (elapsedTimeSec < 10)
			{
				elapsedTimeSec = '0' + elapsedTimeSec;
			}
			var diagnosisTime = (elapsedTimeHour + ":" + elapsedTimeMin + ":" + elapsedTimeSec);
						
			var elapsedTimeHour = elapsedTime.getHours();
			var elapsedTimeMin = elapsedTime.getMinutes();
			var elapsedTimeSec = elapsedTime.getSeconds();
			if (elapsedTimeHour <= 0)
			{
				if (elapsedTimeMin > 0)
				{					
					diagnosisTimeChk = elapsedTimeMin + " " + VAR_COMMON_MIN + elapsedTimeSec + " " + VAR_COMMON_SEC;
				}
				else
				{
					diagnosisTimeChk = elapsedTimeSec + " " + VAR_COMMON_SEC;
				}
			}			
			else 
			{
				if (elapsedTimeMin > 0)
				{					
					diagnosisTimeChk = elapsedTimeHour + " " + VAR_COMMON_HOUR + elapsedTimeMin + " " + VAR_COMMON_MIN + elapsedTimeSec + " " + VAR_COMMON_SEC;
				}
				else
				{					
					diagnosisTimeChk = elapsedTimeHour + " " + VAR_COMMON_HOUR + elapsedTimeSec + " " + VAR_COMMON_SEC;
				}
			}
			
			var diagnosisNotice = data.diagnosisStateInfo.currentCheckItem;
			var ProgressRate = data.diagnosisStateInfo.progressRate;
			var currentCheckString = data.diagnosisStateInfo.currentCheckItem;
			
			
			// Group Select 로 변경된 페이지를 띄워줘야할 시, (key = afterSelectedKey : boolen)
			if (afterSelectedKey == true)
			{
				var $descriptionArea = $('.pop_type_wrap .descriptionArea').filter(':last');
				$descriptionArea.empty();
				var html = '';
				html +=  '<div class="description">	<span class="txtblue">' + VAR_FAULTDIAGNOSIS_INFOLOADING + '</span></div>';								
				html +=  '<div class="progressbararea" style="width: 680px; float: left;"></div>';
				html +=  '<span class="diagnosisCancelLnk" style="margin-left: 10px;"><button type="button" class="Btn_blue">&nbsp;'+ VAR_FAULTDIAGNOSIS_CANCEL + '&nbsp;</button></span>';
				html +=  '<textarea rows="4" class="CliArea"></textarea>';
				$descriptionArea.html(html);
				GenerateProgressBar();
				popUpBtnClickEvent();
			}
			
			//ProgressBar 업데이트 부분						
			ProgressBarobj.update(ProgressRate);			
			
			// 진행시간과, 진행 Notice 업데이트 부분						
			var $diagnosisState = $('.pop_type_wrap .description').filter(':last');
			$diagnosisState.empty();
			var html = '';			
			if (diagnosisNotice == null)
			{
				diagnosisNotice = "";
			}
			html += diagnosisTime + " " + '<span class="txtblue">' + diagnosisNotice + '</span>';			
			$diagnosisState.html(html);			
						
			//CLI Text Area 업데이트 부분	
			var CliMessage = data.diagnosisStateInfo.cliApiMessage;					
			var $CLIAreaIn = $('.pop_type_wrap .CliArea').filter(':last');
			var $CLIArea = $('.pop_type_wrap .CliArea');			
			var html = '';			
			
			if (CliMessage == null)
			{
				CliMessage = "";
			}
			html += "&#13;" + "<br>" + CliMessage ;		
			$CLIAreaIn.append(html);	
			$CLIArea.scrollTop(9999999);		
			
			// HardWare 비정상 건수 & Service 진행률 업데이트 부분
			var HwFail = data.diagnosisStateInfo.hwCheckFailCount;
			var svcRate = data.diagnosisStateInfo.svcCheckRate;			
			var $HardWare = $('.pop_type_wrap .item1 p').filter(':last');
			var $Service = $('.pop_type_wrap .item2 p').filter(':last');
			$HardWare.empty();
			$Service.empty();
			$HardWare.append(HwFail);
			$Service.append(svcRate);
			
			if (ProgressRate == 100)	// 100 : 진단완료
			{
				clearDiagnosisTimer();
				completeDiagnosis(diagnosisTimeChk);
				return;
			}
			if (ProgressRate == 101)	// 101 : 진단실패
			{				
				clearDiagnosisTimer();
				failDiagnosis(currentCheckString);
				return;
			}
//			if (ProgressRate == 102)	// 102 : 진단취소
//			{			
//				clearDiagnosisTimer();
//				cancelDiagnosisMessage();
//				return;
//			}
		}
	},
	// 진단완료
	completeDiagnosis : function(diagnosisTimeChk)
	{
		with(this)
		{
			var $descriptionArea = $('.pop_type_wrap .descriptionArea').filter(':last');
			$descriptionArea.empty();
			var html = '';
			html +=  '<div class="description2"><span class="txt_bold">' + VAR_FAULTDIAGNOSIS_DONE + '</span><p>' + VAR_FAULTDIAGNOSIS_TIME
					+ diagnosisTimeChk + ")" +'</p></div>';
			$descriptionArea.html(html);
			diagnosisPopupAliveFlag = false;
			
			//selectBox에 '완료' 구문 추가
			var selectInfo = $('.cloneDiv select[name=adcSelectInGroup] option:selected');
			if(selectInfo.text().indexOf(VAR_COMMON_COMPLETE) == -1)
			{
				selectInfo.text(selectInfo.text() + ' ('+VAR_COMMON_COMPLETE+')');
			}
			
			var $diagnosisResultLnk = $('.pop_type_wrap .diagnosisResultLnk');
			$diagnosisResultLnk.css('display','');
			
			$('.progress').css('display','none');
			$('.complete').css('display','');
			$('.cancel').css('display','none');
			$('.fail').css('display','none');
			
		}
	},
	// 진단실패
	failDiagnosis : function(currentCheckString)
	{
		with(this)
		{
			var $descriptionArea = $('.pop_type_wrap .descriptionArea').filter(':last');
			$descriptionArea.empty();
			var html = '';
			html +=  '<div class="description2"><span class="txt_bold">' + VAR_FAULTDIAGNOSIS_FAIL +'</span>';
			html += '<p>' + currentCheckString +'</p></div>';
			$descriptionArea.html(html);
			diagnosisPopupAliveFlag = false;
			
			//selectBox에 '실패' 구문 추가
			var selectInfo = $('.cloneDiv select[name=adcSelectInGroup] option:selected');
			selectInfo.text(selectInfo.text() + ' ('+VAR_COMMON_FAIL+')');
			
			$('.progress').css('display','none');
			$('.complete').css('display','none');
			$('.cancel').css('display','none');
			$('.fail').css('display','');
		}
	},
	// 취소 된 진단 정보 다시 불러올때
	cancelDiagnosisMessage : function()
	{
		with(this)
		{
			var $descriptionArea = $('.pop_type_wrap .descriptionArea').filter(':last');
			$descriptionArea.empty();
			var html = '';
			html +=  '<div class="description2"><span class="txt_bold">' + VAR_FAULTDIAGNOSIS_ALREADYCANCEL + '</span><p></p></div>';
			$descriptionArea.html(html);
			diagnosisPopupAliveFlag = false;
			
			var $diagnosisResultLnk = $('.pop_type_wrap .diagnosisResultLnk');
			$diagnosisResultLnk.css('display','none');
			
			$('.progress').css('display','none');
			$('.complete').css('display','none');
			$('.cancel').css('display','');
			$('.fail').css('display','none');
		}
	},
	// 진단 취소 시그널 보내기
	cancelDiagnosis : function(logKey)
	{
		with(this)
		{
			var params = {};
			params["logkey"] = logKey;		
			ajaxManagerOB.runJsonExt
			({
				url			: "faultDiagnosis/cancelFaultCheck.action",
				data		: params,
				successFn	: function(data) 
				{					
				},
				completeFn	: function(data)
				{
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_FAULTDIAGNOSIS_CANCELFAIL, jqXhr);			
				}
			});
			var $descriptionArea = $('.pop_type_wrap .descriptionArea').filter(':last');
			$descriptionArea.empty();
			var html = '';
			html +=  '<div class="description2"><span class="txt_bold">' + VAR_FAULTDIAGNOSIS_CANCELSUCCESS + '</span></div><p></p></div>';
			$descriptionArea.html(html);
			diagnosisPopupAliveFlag = false;
			
			//취소된 항목은 disable, selectBox에 '취소' 구문 추가
			var selectVal = $('.cloneDiv select[name=adcSelectInGroup] option:selected').val();
			var selectInfo = $('.cloneDiv select[name=adcSelectInGroup] option:selected');
			selectInfo.text(selectInfo.text() + ' ('+VAR_COMMON_CANCEL+')');
			selectInfo.val(selectVal).attr('disabled','disabled');
			
			$('.progress').css('display','none');
			$('.complete').css('display','none');
			$('.cancel').css('display','');
			$('.fail').css('display','none');
		}
	},
	
	// ProgressBar 생성 및 초기화
	GenerateProgressBar : function()
	{
		with(this)
		{
			var $progressbar = $('.pop_type_wrap .progressbararea').filter(':last');
			$progressbar.empty();
			var html = '';			
			html += '<div id="Dia_progressBar"></div>';			
			$progressbar.html(html);			
			
			var ProgressBar = new Progress.bar({ id: "Dia_progressBar", autoRemove: false, backgroundSpeed: 5, type: "charge", showPercentage: true });
			ProgressBar.renderTo(document.getElementById('Dia_progressBar'));
			ProgressBar.update(0);
			
			ProgressBarobj = ProgressBar;
		}
	},	
	// 게이지 생성
	GenerateGauge: function(cpuValue1, memValue)
	{
		with(this)
		{
			var $cpuGauge = $('.pop_type_wrap .cpuGauge').filter(':last');
			$cpuGauge.empty();
			var html = '';			
			html += '<div id="g1" style="width: 160px; height: 130px; display: inline-block; margin: 0px;"></div>';			
			$cpuGauge.html(html);
			
			var $memoryGauge = $('.pop_type_wrap .memoryGauge').filter(':last');
			$memoryGauge.empty();
			var html = '';			
			html += '<div id="g2" style="width: 160px; height: 130px; display: inline-block; margin: 0px;"></div>';			
			$memoryGauge.html(html);

			var g1 = new JustGage({
		          id: "g1", 
		          value: cpuValue1, 
		          min: 0,
		          max: 100,
		          title: "Cpu Usage",
		          titleFontColor: "white",
		          label: "",
		          levelColorsGradient: false,
		          showInnerShadow : false,
		          startAnimationTime : -1
		        });		        
		        var g2 = new JustGage({
		          id: "g2", 
		          //value: getRandomInt(0, 100),
		          value: memValue,
		          min: 0,
		          max: 100,
		          title: "Memory Usage",
		          titleFontColor: "white",
		          label: "",
		          levelColorsGradient: false,
		          showInnerShadow : false,
		          startAnimationTime : -1
		        });		       
		        CpuUsageGaugeobj = g1;
		        MemoryUsageGaugeobj = g2;		        	       
		}
	},
	// 게이지 Update
	UpdateGage: function(cpuValue, memValue)
	{
		with(this)
		{		
			CpuUsageGaugeobj.refresh(cpuValue);
			MemoryUsageGaugeobj.refresh(memValue);	
		}
	},
	popUpBtnClickEvent : function(adcName)
	{
		with(this)
		{
			// 진단취소 Btn
			$('.pop_type_wrap .diagnosisCancelLnk').click(function(e)
			{
				e.preventDefault();				
				with(this)
				{
					clearDiagnosisTimer();
					cancelDiagnosis(logKeyObj);
				}				
			});
			// 진단결과 Btn
			$('.pop_type_wrap .diagnosisResultLnk').click(function(e)
			{
				e.preventDefault();
				
				with(this)
				{
					faultResult.loadResultList(logKeyObj, adcName);
					$('.popup_type1').remove();
					$('.cloneDiv').remove();
					clearDiagnosisTimer();
					clearChartTimer();
				}
			});			
			
			// 닫기 Btn
			$('.pop_type_wrap .diagnosisCloseLnk').click(function(e)
			{
				e.preventDefault();
				with(this)
				{
					if (diagnosisPopupAliveFlag == true)
					{
						$.obAlertNotice(VAR_FAULTDIAGNOSIS_PAGENOTCLOSE);
					}
					else
					{
						$('.popup_type1').remove();
						$('.cloneDiv').remove();
						clearDiagnosisTimer();
						clearChartTimer();
					}
				}
			});
		}
	},
	// 실시간 CPU & Memory Chart Core
	displayRealTimeSystemUsage : function (categoryIndex, adcIndex)
	{
		with(this)
		{
			var $cpuChart = $('.pop_type_wrap .cpuChart').filter(':last');
			$cpuChart.empty();
			var html = '';			
			html += '<div id="diagnosisCpuChart" style="width: 200px; height: 135px; margin-top: 0px; "></div>';			
			$cpuChart.html(html);
			
			var $memoryChart = $('.pop_type_wrap .memoryChart').filter(':last');
			$memoryChart.empty();
			var html = '';			
			html += '<div id="diagnosisMemoryChart" style="width: 200px; height: 135px; margin-top: 0px;"></div>';			
			$memoryChart.html(html);
			
			var min = 0;
			var max = 100;
			var chartname = "diagnosisCpuChart";
			var chartunit = "%";
			var adcType = "Alteon";
			diagnosisCpuChart = obchart.OBCreateRealTimeMultiValueChart(min, max, chartname, chartunit, adcType);
			
			var chartOption =
			{
				 min : 0,
				 max : 100,
				 linecolor : "#6cb8c8",
				 chartname : "diagnosisMemoryChart",
				 axistitle : "%",
				 maxPos : null,
				 cursorColor : "#0f47c7"
			};
			diagnosisMemoryChart = obchart.OBCreateRealTimeChart(chartOption);
			
			clearChartTimer();
			SetDiagnosisChartData(categoryIndex, adcIndex, diagnosisCpuChart, diagnosisMemoryChart);
						
			this.chartTimeTimer = setInterval(function () 
			{
				loadDiagnosisChartData(categoryIndex, adcIndex, diagnosisCpuChart, diagnosisMemoryChart);				
			}, 1000);
		}		
	},
	// ChartTimer 초기화 부분
	clearChartTimer : function() 
	{
		with (this)
		{
			if (null != this.chartTimeTimer)
			{
				clearInterval(this.chartTimeTimer);
				this.chartTimeTimer = null;
			}
		}
	},
	// CPU,Memory 실시간 차트의 초기화 데이터를 set 하는 부분, timer와 관계없이 처음에 한번만 차트에 데이터를 넣어준다.	
	SetDiagnosisChartData : function(categoryIndex, adcIndex, cpuChartObject, memoryChartObject) 
	{
		with (this) 
		{
			var params = {};
				params["adcObject.category"] = categoryIndex;
				params["adcObject.index"] = adcIndex;		
			ajaxManagerOB.runJsonExt
			({
				url			: "faultDiagnosis/loadDiagnosisChartData.action",
				data		: params,
				successFn	: function(data) 
				{	
					var startTime = parseDateTime(data.adcSystemUsageData.occurTime);									
					var endTime = parseDateTime(data.adcSystemUsageData.occurTime);					
					
					var minusSecond = (startTime.getSeconds()) - (obchart.varInitCount4Realtime * refreshIntervalSeconds);	// 갯수 x 
					startTime.setSeconds(minusSecond);					
					
					var column = data.adcSystemUsageData;
					var memValue = undefined;
					var cpuValue1 = undefined;
					
					if (column.memUsage > -1 && column.memUsage != null)
					{
						memValue = column.memUsage;
					}					
					if (column.cpu1Usage > -1 && column.cpu1Usage != null)
					{
						cpuValue1 = column.cpu1Usage;
					}
					var StartDataObject =
					{
						occurredTime : startTime,
						value : 0
					};
					obchart.OBSetRealTimeChart(cpuChartObject, StartDataObject);
					obchart.OBSetRealTimeChart(memoryChartObject, StartDataObject);
					
					var EndDataObject =
					{
						occurredTime : endTime,
						value : 0
					};
					obchart.OBSetRealTimeChart(cpuChartObject, EndDataObject);
					obchart.OBSetRealTimeChart(memoryChartObject, EndDataObject);
					GenerateGauge(cpuValue1, memValue);
				},
				completeFn	: function(data)
				{
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_COMMON_CPUMEMDATAEXTRACT, jqXhr);
					clearChartTimer();
				}
			});
		}
	},
	// 실시간 차트의 초기화 후 실제 움직이는 데이터가 들어가는부분, 상위 함수호출에서의 timer 로 반복된다.	
	loadDiagnosisChartData : function(categoryIndex, adcIndex, cpuChartObject, memoryChartObject) 
	{
		with (this) 
		{
			/*if (!데이터가 null 이 오는경우 웹에서 CPU/Memory Usage 조회 실패를 띄운다 . (부가기능))
			{
				clearChartTimer();
				return;
			}*/		
			var params = {};
			params["adcObject.category"] = categoryIndex;
			params["adcObject.index"] = adcIndex;			
			ajaxManagerOB.runJsonExt
			({
				url			: "faultDiagnosis/loadDiagnosisChartData.action",
				data		: params,
				successFn	: function(data) 
				{
					var column = data.adcSystemUsageData;
					var date = parseDateTime(column.occurTime);
					var memValue = undefined;	
					var cpuValue1 = undefined;
					var cpuValue2 = undefined;
					var cpuValue3 = undefined;
					var cpuValue4 = undefined;
					var cpuValue5 = undefined;
					var cpuValue6 = undefined;
					var cpuValue7 = undefined;
					var cpuValue8 = undefined;
					var cpuValue9 = undefined;
					var cpuValue10 = undefined;
					var cpuValue11 = undefined;
					var cpuValue12 = undefined;
					var cpuValue13 = undefined;
					var cpuValue14 = undefined;
					var cpuValue15 = undefined;
					var cpuValue16 = undefined;
					
					if (column.memUsage > -1 && column.memUsage != null)
					{
						memValue = column.memUsage;
					}					
					if (column.cpu1Usage > -1 && column.cpu1Usage != null)
					{
						cpuValue1 = column.cpu1Usage;
					}
					if (column.cpu2Usage > -1 && column.cpu2Usage != null)
					{
						cpuValue2 = column.cpu2Usage;
					}
					if (column.cpu3Usage > -1 && column.cpu3Usage != null)
					{
						cpuValue3 = column.cpu3Usage;
					}
					if (column.cpu4Usage > -1 && column.cpu4Usage != null)
					{
						cpuValue4 = column.cpu4Usage;
					}
					if (column.cpu5Usage > -1 && column.cpu5Usage != null)
					{
						cpuValue5 = column.cpu5Usage;
					}
					if (column.cpu6Usage > -1 && column.cpu6Usage != null)
					{
						cpuValue6 = column.cpu6Usage;
					}
					if (column.cpu7Usage > -1 && column.cpu7Usage != null)
					{
						cpuValue7 = column.cpu7Usage;
					}
					if (column.cpu8Usage > -1 && column.cpu8Usage != null)
					{
						cpuValue8 = column.cpu8Usage;
					}
					if (column.cpu9Usage > -1 && column.cpu9Usage != null)
					{
						cpuValue9 = column.cpu9Usage;
					}
					if (column.cpu10Usage > -1 && column.cpu10Usage != null)
					{
						cpuValue10 = column.cpu10Usage;
					}
					if (column.cpu11Usage > -1 && column.cpu11Usage != null)
					{
						cpuValue11 = column.cpu11Usage;
					}
					if (column.cpu12Usage > -1 && column.cpu12Usage != null)
					{
						cpuValue12 = column.cpu12Usage;
					}
					if (column.cpu13Usage > -1 && column.cpu13Usage != null)
					{
						cpuValue13 = column.cpu13Usage;
					}
					if (column.cpu14Usage > -1 && column.cpu14Usage != null)
					{
						cpuValue14 = column.cpu14Usage;
					}
					if (column.cpu15Usage > -1 && column.cpu15Usage != null)
					{
						cpuValue15 = column.cpu15Usage;
					}
					if (column.cpu16Usage > -1 && column.cpu16Usage != null)
					{
						cpuValue16 = column.cpu16Usage;
					}
					
					var cpuDataObject =
					{
						occurredTime : date,
						Value1 : cpuValue1,
						Value2 : cpuValue2,
						Value3 : cpuValue3,
						Value4 : cpuValue4,
						Value5 : cpuValue5,
						Value6 : cpuValue6,
						Value7 : cpuValue7,
						Value8 : cpuValue8,
						Value9 : cpuValue9,
						Value10 : cpuValue10,
						Value11 : cpuValue11,
						Value12 : cpuValue12,
						Value13 : cpuValue13,
						Value14 : cpuValue14,
						Value15 : cpuValue15,
						Value16 : cpuValue16
					};
					var memDataObject =
					{
						occurredTime : date,
						value : memValue
					};
					obchart.OBUpdateRealTimeChart(cpuChartObject, cpuDataObject);
					obchart.OBUpdateRealTimeChart(memoryChartObject, memDataObject);
					
					// chart Update 와 동시에 최신의 데이터로 Gauge도 업데이트한다.
					UpdateGage(cpuValue1, memValue);
				},
				completeFn	: function(data) 
				{
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_COMMON_CPUMEMDATAEXTRACT, jqXhr);
//					exceptionEvent();
					clearChartTimer();
				}
			});
		}
	},	
});