var FaultResult = Class.create({
	initialize : function() 
	{		
		this.adc;
		this.categoryIndex;
		this.logKey;
		this.cpuFlag = false;
		this.isFlag = 0;
		this.isSvcFlag = 0;
	},
	loadResultList : function(logKeyRev, adcName, adcIndex) 
	{
		this.logKey = logKeyRev;
		this.adc = adcSetting.getAdc();				
		this.categoryIndex = adcSetting.getSelectIndex();
		
		with (this)
		{
			ajaxManager.runHtmlExt({
				url : "faultHistory/loadFaultResult.action",
				target : "#wrap .contents",
				data : 
				{
					"adcObject.category"	: categoryIndex,
					"adcObject.index" 		: adcIndex != undefined ?  adcIndex : adc.index,
					"adc.type" 				: adc.type,
					"adc.name" 				: adcName,
					"logKey" 				: logKey				
				},
				successFn : function(params)
				{	
					header.setActiveMenu("FaultHistory");
					_registerEvents();	
					
//					alert($('.objName').text());
//					var objName = $('.objName').text();
//					alert(objName.contains("CPU"));
					
					var objName = $('.objName').text();
					var str=objName.match("CPU");
					if (str != null)
					{
						loadCpuChartInfo(logKey);
					}
					
//					var objName = $('.objName').text();
//					var searchObjNm = /cpu/i;
//					var pos = objName.search(searchObjNm);					
//					if (pos != -1)
//					{
//						loadCpuChartInfo(logKey);
//					}
					
//					loadCpuChartInfo(logKey);

					loadProgressbarInfo(logKey);
					loadPacketLossInfo(logKey);
					
					$(".summaryTitle[isdetail=1]").parent().next("div").show();
					
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_FAULTRESULT_LOAD, jqXhr);
				}
			});
		}
	},	

	//gage, chart data를 위한 json
	loadCpuChartInfo : function(logKey)
	{
		with(this)
		{
			var params = {};			
				params["logKey"] = logKey;
				ajaxManager.runJsonExt({
					url			: "faultHistory/loadCpuChartInfo.action",
					data		: params,
					successFn	: function(data)
					{
//						intervalMonitor = data.intervalMonitor;
						GenerateCpuChart(data);	// 진입점 차단
						var DataLength = data.faultAdcCpuMemoryList.length -1;
						GenerateGauge(data.faultAdcCpuMemoryList[DataLength].cpu1Usage); // 게이지 Data 주소
					},
					completeFn	: function(data)
					{	
					},
					errorFn : function(jqXhr)
					{
						$.obAlertAjaxError(VAR_COMMON_CPUMEMDATAEXTRACT, jqXhr);
					}
				});			
		}
	},	
	
	//packetLossInfo data를 위한 json
	loadPacketLossInfo : function(logKey)
	{
		with(this)
		{
			var params = {};			
				params["logKey"] = logKey;
				ajaxManager.runJsonExt({
					url			: "faultHistory/loadPacketLossInfo.action",
					data		: params,
					successFn	: function(data)
					{
						//alert(data.packetResult);
//						alert(data.packetRossInfoFileName);
//						$("#packetImg").append("<img src='/imgs/icon/icon_result_info.png'/>");
						GeneratePacketLossInfo(data);	
					},
					completeFn	: function(data)
					{	
					},
					errorFn : function(jqXhr)
					{
						$.obAlertAjaxError(VAR_FAULTRESULT_PKTLOSSLOAD, jqXhr);
					}
				});			
		}
	},	
	
	//progressbar data를 위한 json
	loadProgressbarInfo : function(logKey)
	{
		with(this)
		{
			var params = {
					"adcObject.index" 		: adc.index,					
					"adcObject.category"	: categoryIndex
				};
//				params['startTime'] = $('input[name="startTime"]').val();
//				params['endTime'] = $('input[name="endTime"]').val();
				params["logKey"] = logKey;
				
				ajaxManager.runJsonExt({
					url			: "faultHistory/loadProgressbarInfo.action",
					data		: params,
					successFn	: function(data)
					{
						//ProgressBar 업데이트 부분						
						var prevRate = data.faultCheckResponseTimeInfo.prevRate;
						var currRate = data.faultCheckResponseTimeInfo.currRate;
						var avgRate = data.faultCheckResponseTimeInfo.avgRate;
						var currTime = data.faultCheckResponseTimeInfo.currTime;
						var prevTime = data.faultCheckResponseTimeInfo.prevTime;
						var avgTime = data.faultCheckResponseTimeInfo.avgTime;
						
//						var data1 = data.faultCheckResponseTimeInfo.prevRate;
//						var data2 = data.faultCheckResponseTimeInfo.currRate;
//						var data3 = data.faultCheckResponseTimeInfo.avgRate;
//						var data4 = data.faultCheckResponseTimeInfo.currTime;
//						var data5 = data.faultCheckResponseTimeInfo.prevTime;
//						var data6 = data.faultCheckResponseTimeInfo.avgTime;
						
//						alert(data1);
//						var test={
//							"prevRate" : prevRate,
//							"currRate" : currRate,
//							"avgRate" : avgRate,
//							"curTime" : currTime,
//							"prevTime" : prevTime,
//							"avgTime" : avgTime
//						};
//						alert(test.data1);
						
						GenerateProgressBar(prevRate, currRate, avgRate, currTime, prevTime, avgTime);
//						GenerateProgressBar(test);	
					},
					completeFn	: function(data)
					{	
					},
					errorFn : function(jqXhr)
					{
						$.obAlertAjaxError(VAR_FAULTRESULT_INPROGRESSPRINT, jqXhr);
					}
				});			
		}
	},	
	_checkDownloadSvcPkDumpDataExist : function(index)
	{
		with (this)
		{
			ajaxManager.runJsonExt
			({
				url :"faultHistory/checkSvcPktDumpDataExist.action",
				data :
				{
					"logKey" : index
				},
				successFn : function(data)
				{
//					alert(data.isSuccessful);
					if(!data.isSuccessful)
					{
						$.obAlertNotice(data.message);
						return;
					}
					downloadSvcPktDump(index);
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_FAULTRESULT_PKTDUMPFILEEXIST, jqXhr);
				}
			});
		}
	},
	
	downloadSvcPktDump : function(index)
	{
		with (this)
		{
			var url = "faultHistory/downloadSvcPktDump.action?logKey=" + index;
			$('#downloadFrame').attr('src', url);				
		}
	},
	
	_registerEvents : function()
	{
		with(this)
		{
			// 장애 이력 목록으로 전환
			$('.faultHistoryLnk').click(function(e) 
			{
				e.preventDefault();
				faultHistory.loadListContent();
			});	
			
			// 패킷덤프 파일 다운로드 
			$('.downloadPktDumpLnk').click(function(e)
			{
				var logKey = $(this).parent().find('.logKey').text();
//				alert(logKey);
				_checkDownloadSvcPkDumpDataExist(logKey);				
			});
			
			// 내보내기 
			$('.exportCssLnk').click(function(e)
			{
				e.preventDefault();
				_checkExportDataExist();	
//				exportCss();
			});
			
			// 진단결과 내용  복사
			$('.copy').click(function(e)
			{
				with (this)
				{
//					alert("copy");
//					$.copy($(this).text());
				}
			});
							
			// 진단결과 summary문구만 click 
			$('.summaryTitle').click(function(e)
			{
				var isdetail = $(this).attr("isdetail");
				
				if (isdetail == 0) 
				{
					var clr = $(this).parent().hasClass("txt1_ok");
					if (true == clr) 
					{
						$(this).parent().addClass("txt_green");
					}
					
					clr = $(this).parent().hasClass("txt1_info");
					if (true == clr) 
					{
						$(this).parent().addClass("txt_blue");
					}
					
					clr = $(this).parent().hasClass("txt1_None");
					if (true == clr) 
					{
						$(this).parent().addClass("txt_normal");
					}
					
					$(this).parent().next('div').show();
					$(this).attr("isdetail", "1");
//					$(this).parent().next('div').next('.gage1').show();
//					$(this).parent().next('div').next('.chart1').show();
				}
				else {
					
					$(this).parent().removeClass("txt_green");
					$(this).parent().removeClass("txt_blue");
					$(this).parent().removeClass("txt_normal");
					$(this).parent().next('div').hide();
					$(this).attr("isdetail", "0");
//					$(this).parent().next('div').next('.gage1').hide();
//					$(this).parent().next('div').next('.chart1').hide();
				}
								
			});
			
			/** 이부분은 우측 상단에 경우에 수를 가지고 모두보기 버튼 비활성화 시키려고 하는 부분. **/		
			
//			if (isflag == 1 || isSvcflag == 1)
//			{
//				alert("aaaa");
//			}
			
//			if ($('img.adcDiagnosisClick').attr('src', '/imgs/icon/arrows_up.png') == true || $('img.svcClick').attr('src', '/imgs/icon/arrows_up.png') == true)
//			{
//				alert("aaaa");
//				$('.resultAllHide').prop("disabled", true);
//				$('.resultAllHide').prop("disabled", true);
//			}

			/** 이부분은 모두보기 하는 부분. **/
			$('.allShow').click(function(e)
			{
				/** 기존 img 에 class 이름 **/
//				$('img.click').attr('src', '/imgs/icon/arrows_down.png');				
//				$('img.svcClick').attr('src', '/imgs/icon/arrows_down.png');
				
				/** 기존 img 에 class 이름 변경 **/
				$('img.adcDiagnosisClick').attr('src', '/imgs/icon/arrows_down.png');
				$('img.svcClick').attr('src', '/imgs/icon/arrows_down.png');
				
				/** 기존 h2,h3 다음 div를 찾아서 show **/
//				$("h2").next('div').show();
//				$("h3").next('div').show();
				
//				$(this).parents('.upDownAdc').next('div').show();
//				$(this).parents('.upDownSvc').next('div').show();
				
				/** 현재 show 하는 부분 변경 **/
				$(this).parents('.upDownAdc').parent().parent().parent().next('div').show();
				$(this).parents('.upDownSvc').parent().parent().parent().next('div').show();
				
				$('.txt1_ok').addClass('txt_green');
				$('.txt1_info').addClass('txt_blue');
				$('.txt1_None').addClass('txt_normal');
				
//				var clr = $('.summaryTitle').parent().hasClass("okStyle");
//				if (true == clr) 
//				{
//					$('.summaryTitle').parent().addClass("txt_green");
//				}
//				
//				var clr = $('.summaryTitle').parent().hasClass("infoStyle");
//				if (true == clr) 
//				{
//					$('.summaryTitle').parent().addClass("txt_blue");
//				}
				
				$('.summaryTitle').parent().next('div').show();
				$('.summaryTitle').attr("isdetail", "1");
				
				$(this).find(".arrow").toggleClass("arrow up");
				
				$('.resultAllShow').addClass("none");
				$('.resultAllHide').removeClass("none");	
			});
			
			$('.allHide').click(function(e)
			{
				
//				$("h2").next('div').hide();
//				$("h3").next('div').hide();
				
				$(this).parents('.upDownAdc').parent().parent().parent().next('div').hide();
				$(this).parents('.upDownSvc').next('div').hide();
				
				var clr = $('.summaryTitle').parent().hasClass("txt_green");
				if (true == clr) 
				{
					$('.summaryTitle').parent().removeClass("txt_green");
				}
				
				clr = $('.summaryTitle').parent().hasClass("txt_blue");
				if (true == clr) 
				{
					$('.summaryTitle').parent().removeClass("txt_blue");
				}
				
				clr = $('.summaryTitle').parent().hasClass("txt_normal");
				if (true == clr) 
				{
					$('.summaryTitle').parent().removeClass("txt_normal");
				}
				
				$('.summaryTitle').parent().next('div').hide();
				$('.summaryTitle').attr("isdetail", "0");
										
				$('img.adcDiagnosisClick').attr('src', '/imgs/icon/arrows_up.png');
				$('img.svcClick').attr('src', '/imgs/icon/arrows_up.png');
				
				$('.resultAllShow').removeClass("none");
				$('.resultAllHide').addClass("none");	
			});		
								
			// ADC 진단 show hide 기능
//			$('h2').click(function(e) {
			$('.adcDiagnosisClick').click(function(e)
			{
//				var isflag = $(this).parent().attr("isflag");
//				var isdetail1 = $('.adcFlag').attr("isflag");
				
//				alert("click");
//				$(this).parents().parent().parent().parent().parent().parent().next().hide();
//				$(this).parents('.upDownAdc').next().hide();
				
//				if($(this).next().css('display') == 'block')
				if($(this).parents('.upDownAdc').next().css('display') == 'block')
				{
//					$(this).next().hide();
					$(this).parents('.upDownAdc').next().hide();
//					$('img.click').attr('src', '/imgs/icon/arrows_down.png');
					$('img.adcDiagnosisClick').attr('src', '/imgs/icon/arrows_down.png');
					$('#adcFlag').attr("isflag", "1");
					$('#svcFlag').attr("isflag", "0");
				}
				else
				{
//					$(this).next('div').show();
					$(this).parents('.upDownAdc').next('div').show();
//					$('img.click').attr('src', '/imgs/icon/arrows_up.png');
					$('img.adcDiagnosisClick').attr('src', '/imgs/icon/arrows_up.png');
					$('#adcFlag').attr("isflag", "0");
					$('#svcFlag').attr("isflag", "0");
				}
				
				isFlag = $('#adcFlag').attr("isflag");
				if (isSvcFlag == undefined)
				{
					isSvcFlag = 0;
				}
				else
				{
					isSvcFlag = $('#svcFlag').attr("isflag");
				}
				disabledChk(isFlag, isSvcFlag);
			});
			
			// 서비스 진단 show hide 기능
//			$('h3').click(function(e) {
			$('.svcClick').click(function(e)
			{
//				var isSvcflag = $(this).parent().attr("isSvcflag");
				if($(this).parents('.upDownSvc').next().css('display') == 'block')
				{
//					$(this).next().hide();
					$(this).parents('.upDownSvc').next().hide();
					$('img.svcClick').attr('src', '/imgs/icon/arrows_down.png');
//					$(this).parent().attr("isSvcflag", "1");
//					$('#svcFlag').attr("isflag", "0");
					$('#svcFlag').attr("isflag", "1");
				}
				else
				{					
//					$(this).next('div').show();//slideDown(300);
					$(this).parents('.upDownSvc').next('div').show();
					$('img.svcClick').attr('src', '/imgs/icon/arrows_up.png');
//					$(this).parent().attr("isSvcflag", "0");
//					$('#svcFlag').attr("isflag", "1");
					$('#svcFlag').attr("isflag", "0");
				}	
//				
//				isFlag = $('#adcFlag').attr("isflag");
//				isSvcFlag = $('#svcFlag').attr("isflag");
//				disabledChk(isFlag, isSvcFlag);
				
				isFlag = $('#adcFlag').attr("isflag");
				if (isFlag == undefined)
				{
					isFlag = 0;
				}
				else
				{
					isFlag = $('#adcFlag').attr("isflag");
				}
				isSvcFlag = $('#svcFlag').attr("isflag");
				disabledChk(isFlag, isSvcFlag);
			});
			
			$('#resultPrint').click(function(e)
			{
				e.preventDefault();
				//_checkExportPerformDataExist();
				var mode = "popup";
				var close = "popup";
				var extraCss = "";
				var popTitle = "x15 Diagnosis Result print preview";
				var width = 1024;
				var height = 1024;
				var strict = true;
				var options = 
				{
					mode : mode,
					popClose : close,
					extraCss : extraCss,
					popTitle : popTitle,
					popWd : width,
					popHt : height,
					strict : strict
				};
	
	            $("div.printArea").printArea(options);	    				
			});
		}
	},	
	
	disabledChk : function(adcflag, svcflag)
	{
		with (this)
		{			
//			alert("isFlag : " + isFlag);
//			alert("isSvcFlag : " + isSvcFlag);
			if (isFlag != 0 || isSvcFlag != 0)			
			{
	//			$('.resultAllShow').prop("disabled", true);
				$('.resultAllShow').attr("disabled", "disabled");
	//			$('.resultAllShow').addClass("none");
	//			$('.resultAllHide').addClass("none");	
				
				if ($('.resultAllShow').hasClass("none") == true)
				{
					$('.resultAllShowDisable').addClass("none");
					$('.resultAllHideDisable').removeClass("none");
				}
				if ($('.resultAllHide').hasClass("none") == true)
				{
					$('.resultAllShowDisable').removeClass("none");
					$('.resultAllHideDisable').addClass("none");
				}
				
				$('.resultAllHide').addClass("none");
				$('.resultAllShow').addClass("none");
			}
			else
			{
				$('.resultAllShow').removeClass("none");
				$('.resultAllHide').addClass("none");
				$('.resultAllShowDisable').addClass("none");
				$('.resultAllHideDisable').addClass("none");
			}
		}
	},
	
	_checkExportDataExist : function()
	{
		with(this)
		{
			ajaxManager.runJsonExt
			({
				url : "faultHistory/checkResultDataExist.action",
				data : 
				{
					"adcObject.category"	: categoryIndex,
					"adcObject.index" 		: adc.index,
					"adc.type" 				: adc.type,
					"adc.name" 				: adc.name,
					"logKey" 				: logKey			
				},
				successFn : function(data)
				{
					if(!data.isSuccessful)
					{
						$.obAlertNotice(data.message);
						return;
					}
					exportCss(logKey);
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_FAULTRESULT_EXISTINPECT, jqXhr);
				}
			});
		}
	},
	exportCss : function(logkey)
	{
		with (this)
		{
//			var url = "monitor/downloadPerformanceF5.action?adc.index=" + adc.index +"&adcType=" + adc.type +"&searchTimeMode=" + selectedSearchTimeMode + "&hour=" + searchLastHours + "&startTime=" + $('input[name="startTime"]').val() + "&endTime=" + $('input[name="endTime"]').val();
			var url = "faultHistory/downloadResultData.action?adc.type=" + adc.type + "&logKey=" + logKey;
			$('#downloadFrame').attr('src', url);
		}
	},
	
	GenerateGauge: function(gaugeValue)
	{
		with(this)
		{
			var $cpuGauge = $('.contents_area .cpuGauge1').filter(':last');
			$cpuGauge.empty();
			var html = '';			
			html += '<div id="g1" style="width: 160px; height: 130px; display: inline-block; margin: 0px;"></div>';			
			$cpuGauge.html(html);
			
			var g1 = new JustGage({
	          id: "g1", 
	          value: gaugeValue, 
	          min: 0,
	          max: 100,
	          title: "Cpu Usage",
	          label: "",
	          levelColorsGradient: false,
	          showInnerShadow : false
	        });        	       
		}
	},	
	
//	GenerateProgressBar : function()
//	{
//		with(this)
//		{
//			var ProgressBar = new Progress.bar({ id: "curr_progressBar", autoRemove: false, type: "charge" });
//			ProgressBar.renderToCurr(document.getElementById('curr_progressBar'));
//			ProgressBar.updateCurr(100);
//			
//			var ProgressBar = new Progress.bar({ id: "avg_progressBar", autoRemove: false, type: "charge" });
//			ProgressBar.renderToAvg(document.getElementById('avg_progressBar'));
//			ProgressBar.updateAvg(80);
//			
//			var ProgressBar = new Progress.bar({ id: "prev_progressBar", autoRemove: false, backgroundSpeed: 5, type: "charge", showPercentage: true });
//			ProgressBar.renderToPrev(document.getElementById('prev_progressBar'));
//			ProgressBar.updatePrev(50);
//			
//			ProgressBarobj = ProgressBar;
//		}
//	},
	
	//서비스 성능응답시간 분석 progressbar
	GenerateProgressBar : function(data1, data2, data3, data4, data5, data6)
//	GenerateProgressBar : function(test)
	{
		with(this)
		{
			var $ProgressBarCurr = $('.progressbarareaCurr').filter(':last');
			$ProgressBarCurr.empty();
			var html = '';			
			html += '<div id="curr_progressBar" style="width: 90%; float: left;"></div>';
			html += '<div>&nbsp;<label>' + data4 + ' ms' + '</label></div>';
			$ProgressBarCurr.html(html);
			
			var $ProgressBarAvg = $('.progressbarareaAvg').filter(':last');
			$ProgressBarAvg.empty();
			var html = '';			
			html += '<div id="avg_progressBar" style="width: 90%; float: left;"></div>';	
			html += '<div>&nbsp;<label>' + data6 + '&nbsp;ms' + '</label></div>';
			$ProgressBarAvg.html(html);			
			
			
			var $ProgressBarPrev = $('.progressbarareaPrev').filter(':last');
			$ProgressBarPrev.empty();
			var html = '';			
			html += '<div id="prev_progressBar" style="width: 90%; float: left;"></div>';	
			html += '<div>&nbsp;<label>' + data5 + ' ms' + '</label></div>';
			$ProgressBarPrev.html(html);			
			
			var curr = document.getElementById('curr_progressBar');
			var avg = document.getElementById('avg_progressBar');
			var prev = document.getElementById('prev_progressBar'); 
			
//			alert("curr : " + curr);
//			alert("avg : " + avg);
//			alert("prev : " + prev);
			
			if (curr != null)
			{
				var ProgressBarCurr = new Progress.bar({ id: "curr_progressBar", autoRemove: false, type: "charge", showPercentage: true });
				ProgressBarCurr.renderToCurr(curr);
				ProgressBarCurr.updateCurr(data2);
			}
			
			if (avg != null)
			{
				var ProgressBarAvg = new Progress.bar({ id: "avg_progressBar", autoRemove: false, type: "charge", showPercentage: true });
				ProgressBarAvg.renderToAvg(avg);
				ProgressBarAvg.updateAvg(data3);
			}
			
			if (prev != null)
			{
				var ProgressBarPrev = new Progress.bar({ id: "prev_progressBar", autoRemove: false, backgroundSpeed: 5, type: "charge", showPercentage: true });
				ProgressBarPrev.renderToPrev(prev);
				ProgressBarPrev.updatePrev(data1);
			}
		}
	},
	
	GeneratePacketLossInfo : function(data)
	{	
		with (this)		
		{
			var imgPath = "<img src='/imgs/" + data.packetLossInfoFileName + "'/>";
//			var imgPath = "<img src='/imgs/" + "3581705991391.png" + "'/>";
			
//			alert("["+data.packetLossInfoFileName+"]");
			if (data.packetLossInfoFileName.length != 0 && data.packetLossInfoFileName != null)
			{
				$("#packetImg").append(imgPath);
				$('.downloadPktDumpLnk').removeClass('none');
			}
		}		
	},
	
	GenerateCpuChart : function(data)
	{
		with(this)
		{
			var $cpuChart = $('.contents_area .cpuChart1').filter(':last');
			$cpuChart.empty();
			var html = '';			
			html += '<div id="CpuHistroyChart" style="width: 100%; height: 135px; margin-top: 0px; "></div>';			
			$cpuChart.html(html);
			
			var chartData = [];
			var chartDataList = [];
			if (data.faultAdcCpuMemoryList != null)
			{
				chartDataList = data.faultAdcCpuMemoryList;
			}
			if (chartDataList.length > 0 && chartDataList != null)
			{
				 for ( var i = 0; i < chartDataList.length; i++)
				 {
					 if (i == 0)
					 {
						/* if (data.startTime < chartDataList[0].occurTime)
						 {
							 var startTime = parseDateTime(data.startTime);
							 chartData.push({occurredTime:startTime});
						 }*/
					 }
					var column = chartDataList[i];
					if (column)
					{
						var occurTime = column.occurTime;
						var date = parseDateTime(occurTime);
						var mpValue = column.cpu1Usage;
			        	var spValue1 = undefined;
						var spValue2 = undefined;
						var spValue3 = undefined;
						var spValue4 = undefined;
						var spValue5 = undefined;
						var spValue6 = undefined;
						var spValue7 = undefined;
						var spValue8 = undefined;
						var spValue9 = undefined;
						var spValue10 = undefined;
						var spValue11 = undefined;
						var spValue12 = undefined;
						var spValue13 = undefined;
						var spValue14 = undefined;
						var spValue15 = undefined;
						var spValue16 = undefined;
						var spValue17 = undefined;
						var spValue18 = undefined;
						var spValue19 = undefined;
						var spValue20 = undefined;
						var spValue21 = undefined;
						var spValue22 = undefined;
						var spValue23 = undefined;
						var spValue24 = undefined;
						var spValue25 = undefined;
						var spValue26 = undefined;
						var spValue27 = undefined;
						var spValue28 = undefined;
						var spValue29 = undefined;
						var spValue30 = undefined;
						var spValue31 = undefined;
						var spValue32 = undefined;
						
						if (column.cpu2Usage > -1 && column.cpu2Usage != null)
						{
							spValue1 = column.cpu2Usage;
						}
						if (column.cpu3Usage > -1 && column.cpu3Usage != null)
						{
							spValue2 = column.cpu3Usage;
						}
						if (column.cpu4Usage > -1 && column.cpu4Usage != null)
						{
							spValue3 = column.cpu4Usage;
						}
						if (column.cpu5Usage > -1 && column.cpu5Usage != null)
						{
							spValue4 = column.cpu5Usage;
						}
						if (column.cpu6Usage > -1 && column.cpu6Usage != null)
						{
							spValue5 = column.cpu6Usage;
						}
						if (column.cpu7Usage > -1 && column.cpu7Usage != null)
						{
							spValue6 = column.cpu7Usage;
						}
						if (column.cpu8Usage > -1 && column.cpu8Usage != null)
						{
							spValue7 = column.cpu8Usage;
						}
						if (column.cpu9Usage > -1 && column.cpu9Usage != null)
						{
							spValue8 = column.cpu9Usage;
						}
						if (column.cpu10Usage > -1 && column.cpu10Usage != null)
						{
							spValue9 = column.cpu10Usage;
						}
						if (column.cpu11Usage > -1 && column.cpu11Usage != null)
						{
							spValue10 = column.cpu11Usage;
						}
						if (column.cpu12Usage > -1 && column.cpu12Usage != null)
						{
							spValue11 = column.cpu12Usage;
						}
						if (column.cpu13Usage > -1 && column.cpu13Usage != null)
						{
							spValue12 = column.cpu13Usage;
						}
						if (column.cpu14Usage > -1 && column.cpu14Usage != null)
						{
							spValue13 = column.cpu14Value;
						}
						if (column.cpu15Value > -1 && column.cpu15Value != null)
						{
							spValue14= column.cpu15Value;
						}
						if (column.cpu16Value > -1 && column.cpu16Value != null)
						{
							spValue15 = column.cpu16Usage;
						}
						if (column.cpu17Usage > -1 && column.cpu17Usage != null)
						{
							spValue16 = column.cpu17Usage;
						}
						if (column.cpu18Usage > -1 && column.cpu18Usage != null)
						{
							spValue17 = column.cpu18Usage;
						}
						if (column.cpu19Usage > -1 && column.cpu19Usage != null)
						{
							spValue18 = column.cpu19Usage;
						}
						if (column.cpu20Usage > -1 && column.cpu20Usage != null)
						{
							spValue19 = column.cpu20Usage;
						}
						if (column.cpu21Usage > -1 && column.cpu21Usage != null)
						{
							spValue20 = column.cpu21Usage;
						}
						if (column.cpu22Usage > -1 && column.cpu22Usage != null)
						{
							spValue21 = column.cpu22Usage;
						}
						if (column.cpu23Usage > -1 && column.cpu23Usage != null)
						{
							spValue22 = column.cpu23Usage;
						}
						if (column.cpu24Usage > -1 && column.cpu24Usage != null)
						{
							spValue23 = column.cpu24Usage;
						}
						if (column.cpu25Usage > -1 && column.cpu25Usage != null)
						{
							spValue24 = column.cpu25Usage;
						}
						if (column.cpu26Usage > -1 && column.cpu26Usage != null)
						{
							spValue25 = column.cpu26Usage;
						}
						if (column.cpu27Usage > -1 && column.cpu27Usage != null)
						{
							spValue26 = column.cpu27Usage;
						}
						if (column.cpu28Usage > -1 && column.cpu28Usage != null)
						{
							spValue27 = column.cpu28Usage;
						}
						if (column.cpu29Usage > -1 && column.cpu29Usage != null)
						{
							spValue28 = column.cpu29Usage;
						}
						if (column.cpu30Usage > -1 && column.cpu30Usage != null)
						{
							spValue29 = column.cpu30Usage;
						}
						if (column.cpu31Usage > -1 && column.cpu31Usage != null)
						{
							spValue30 = column.cpu31Usage;
						}
						if (column.cpu32Usage > -1 && column.cpu32Usage != null)
						{
							spValue31 = column.cpu32Usage;
						}
						if (column.cpu33Usage > -1 && column.cpu33Usage != null)
						{
							spValue32 = column.cpu33Usage;
						}
						
						var beforeDataObject = undefined;
						var dataObject = undefined;
						if (adc.type == "Alteon")
						{
							beforeDataObject =
							{														
								Value1 : spValue1,
								Value2 : spValue2,
								Value3 : spValue3,
								Value4 : spValue4,
								Value5 : spValue5,
								Value6 : spValue6,
								Value7 : spValue7,
								Value8 : spValue8,
								Value9 : spValue9,
								Value10 : spValue10,
								Value11 : spValue11,
								Value12 : spValue12,
								Value13 : spValue13,
								Value14 : spValue14,
								Value15 : spValue15,
								Value16 : spValue16,
								Value17 : spValue17,
								Value18 : spValue18,
								Value19 : spValue19,
								Value20 : spValue20,
								Value21 : spValue21,
								Value22 : spValue22,
								Value23 : spValue23,
								Value24 : spValue24,
								Value25 : spValue25,
								Value26 : spValue26,
								Value27 : spValue27,
								Value28 : spValue28,
								Value29 : spValue29,
								Value30 : spValue30,
								Value31 : spValue31,
								Value32 : spValue32
							};
							var avgObj = sumObjectValue(beforeDataObject);
							dataObject =
							{
								occurredTime : date,
								firstValue : mpValue,
								secondValue : avgObj.avgNum,
								
								firstName : "MP Usage",
								secondName : "SP Avg",								
							};
						}
						else
						{
							beforeDataObject =
							{
								MPValue : mpValue,							
								Value1 : spValue1,
								Value2 : spValue2,
								Value3 : spValue3,
								Value4 : spValue4,
								Value5 : spValue5,
								Value6 : spValue6,
								Value7 : spValue7,
								Value8 : spValue8,
								Value9 : spValue9,
								Value10 : spValue10,
								Value11 : spValue11,
								Value12 : spValue12,
								Value13 : spValue13,
								Value14 : spValue14,
								Value15 : spValue15,
								Value16 : spValue16,
								Value17 : spValue17,
								Value18 : spValue18,
								Value19 : spValue19,
								Value20 : spValue20,
								Value21 : spValue21,
								Value22 : spValue22,
								Value23 : spValue23,
								Value24 : spValue24,
								Value25 : spValue25,
								Value26 : spValue26,
								Value27 : spValue27,
								Value28 : spValue28,
								Value29 : spValue29,
								Value30 : spValue30,
								Value31 : spValue31,
								Value32 : spValue32
							};
							var avgObj = sumObjectValue(beforeDataObject);	
							dataObject =
							{
									occurredTime : date,
									firstValue : avgObj.avgNum
							};
						}
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
			var chartOption =
			{
				 min : 0,
				 max : 100,
				 linecolor : "#4ca3f4",
				 chartname : "CpuHistroyChart",
				 axistitle : "%",
				 maxPos : null,
				 cursorColor : "#0e7023"
			};
		 
		 if (adc.type == "Alteon")
			 obchart.OBMultiLineChartViewer(chartData, chartOption);
		 else
			 obchart.OBAreaChartViewer(chartData, chartOption);
					
		}		
	},
});