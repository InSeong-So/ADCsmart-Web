var FaultAnalysisPopup = Class.create({
	initialize : function()
	{
		this.refreshIntervalSeconds = 3;
		this.analysisTimeTimer = undefined;
		this.chartTimeTimer = undefined;
		this.analysisCpuChart = undefined;
		this.analysisMemoryChart = undefined;
		this.CpuUsageGaugeobj = undefined;
	    this.MemoryUsageGaugeobj = undefined;
		this.analysisPopupAliveFlag = undefined;
		this.logKeyObj = undefined;		
		this.logKeyList = undefined;
		this.owner = undefined;				
		this.adc = {};
		this.categoryIndex;
		this.logKey = undefined;
		this.refreshTimer = undefined;		 
		this.orderDir  = 2; //2 :  내림차순
		this.orderType = 11;// 11 : occurTime
		this.scheduleIndex = undefined;
		this.templateIndex = undefined;
		this.selectedIndex = undefined;		
		this.optionMaxSize = 0;
		this.optionMaxTime = 0; 
		this.optionMaxPkt = 0;
		this.curSelectedIndex = undefined;
		this.selectedColumnIndex = undefined;
		this.generateGaugeKey = false;
		
		this.MaxPktChkKey = 1;
		this.MaxTimeChkKey = 0;
		this.MaxSizeChkKey = 0;		// CheckBox Jquery 버그로 인해 Key로 임시 해결
	},	

	loadFaultAnalysisPopup : function(index, type)
	{
		with (this)
		{
			MaxPktChkKey = 1;
			MaxTimeChkKey = 0;
			MaxSizeChkKey = 0;
			analysisPopupAliveFlag = true;			
			showPopup({
				'id' : '#pktAddPop',
				'width' : '800px',
				'height' : '455px'
			});
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
			
			$('.cloneDiv .targetSelect').change(function(e)
			{
				var selectedVal = $('.cloneDiv .targetSelect').val();
								
				if(selectedVal == "portVal")
				{	
					$('.interfaceName').removeClass("none");
					$('.vlanName').addClass("none");
				}
				else
				{
					$('.interfaceName').addClass("none");
					$('.vlanName').removeClass("none");
				}
			});
			
			var fileNm = ""; 
			
			if (index == 2)
			{
				fileNm = adcSetting.getAdc().name;				
			}
			else if (index == 1)
			{
				fileNm = adcSetting.getGroupName();
			}
			else
			{
				fileNm = "all";
			}
			
			if (type == "F5")
			{
				$('.pktSize').removeClass("none");
				//$('.vlanF5').removeClass("none");
			}
			else
			{
				$('.pktSize').addClass("none");
				//$('.vlanF5').addClass("none");
			}	
			
			$('.cloneDiv input[name="fileName"]').val(fileNm + '_' + getTime() + '.pcap');
			
			// 패킷 수집 시작 Btn
			$('.startPktDump').click(function(e)
			{
				startPktDump(index);
			});
			
			// 패킷 수집 중지 Btn
			$('.stopPktdump').click(function(e)
			{
				e.preventDefault();
				with (this)
				{
					logKey = $(this).parent().parent().parent().find('.logKey').text();				
//					clearRefreshTimer();
					registerPktDump(index, selectedIndex, 0, logKey);
					clearAnalysisTimer();
					clearChartTimer();
				}
			});				
			
			// 패킷 수집 취소 Btn
			$('.cancelPktdump').click(function(e)
			{
				e.preventDefault();
				with (this)
				{					
					logKey = $(this).parent().parent().parent().find('.logKey').text();
//					clearRefreshTimer();					
					registerPktDump(index, selectedIndex, 1, logKey);	
					clearAnalysisTimer();
					clearChartTimer();
					generateGaugeKey = false;
				}
			});
		
			// 메인 닫기 Btn
			$('.closeMainLnk').click(function(e)
			{
				e.preventDefault();
				$('.popup_type1').remove();
				$('.cloneDiv').remove();
				this.generateGaugeKey = false;
			});	
			
			// 닫기 Btn
			$('.closeLnk').click(function(e)
			{
				e.preventDefault();
				with(this)
				{
					if (analysisPopupAliveFlag == true)
					{
						$.obAlertNotice(VAR_PKTPOP_INPROGRESS);
					}
					else
					{
						$('.popup_type1').remove();
						$('.cloneDiv').remove();
						clearAnalysisTimer();
						clearChartTimer();
						generateGaugeKey = false;
						owner.loadListContent();
					}
				}
			});	

			// 필터 추가
			$('.addFilter').click(function(e)
			{
				e.stopImmediatePropagation();
				_addFilterForm();
				_registSelectChangeEvent();
			});
			
			// 필터 삭제
			$('.delFilter').click(function(e)
			{
				e.stopImmediatePropagation();
//				if($('select').length == 1)
//				{
//					alert("Source_IP와 Destination_IP중 하나는 반드시 입력 하여야 합니다.");
//					return;
//				}
//				else
//				{
//					$('.addSlt').remove();
//					_checkKeyWordSelectBoxLength();
//				}	
			});	

			//필터 종류 선택
			$('.cloneDiv .defaultSelect').change(function(e)
			{
				//e.stopImmediatePropagation(); //노드에 기록된 중첩 이벤트를 막기 위해 사용
				var selectedVal = $('.cloneDiv .defaultSelect').val();

				if(selectedVal == "srcIpVal 0")//src_ip
				{	
					e.stopImmediatePropagation();
					// from GS. #4012-1 #2 14.07.29 sw.jung: 잘못된 유효성 검사 오류 해결
//					var nameVal = document.getElementsByName('srcIp');
					var nameVal = $('.cloneDiv input[name = "srcIp"]');
					if(nameVal.length>0)
					{					
						//alert(VAR_PKTPOP_SRCIPALREADYSEL);
						$('.cloneDiv select[name = "filterFirst"] option:eq(0)').attr("selected", "selected");
						var pArea = $('.cloneDiv .defaultArea').filter(':last');
						pArea.empty();
						var html = "";						
						html +='<input type="text"  name="srcIp" class="inputText width129 margin_t2b2">';							
						pArea.html(html);
						return;
					}	
					else
					{
						var pArea = $('.cloneDiv .defaultArea').filter(':last');
						pArea.empty();
						var html = "";						
						html +='<input type="text"  name="srcIp" class="inputText width129 margin_t2b2">';							
						pArea.html(html);
					}	
				}
				else if(selectedVal == "dstIpVal 1")//dst_Ip
				{
					e.stopImmediatePropagation();
					var nameVal = document.getElementsByName('dstIp');
					if(nameVal.length>0)
					{					
						//alert(VAR_PKTPOP_DSTIPALREAYSEL);
						$('.cloneDiv select[name = "filterFirst"] option:eq(0)').attr("selected", "selected");
						return;
					}	
					else
					{
						var pArea = $('.cloneDiv .defaultArea').filter(':last');
						pArea.empty();
						var html = "";							
						html +='<input type="text"  name="dstIp" class="inputText width129 margin_t2b2">';							
						pArea.html(html);
					}					
				}
				else if(selectedVal == "srcPortVal 2")//src_port
				{
					e.stopImmediatePropagation();
					var nameVal = document.getElementsByName('srcPort');
					if(nameVal.length>0)
					{
						$.obAlertNotice(VAR_PKTPOP_SRCPORTALREADYSEL);
						$('.cloneDiv select[name = "filterFirst"] option:eq(0)').attr("selected", "selected");
						return;
					}	
					else
					{
						var pArea = $('.cloneDiv .defaultArea').filter(':last');
						pArea.empty();
						var html = "";								
						html +='<input type="text"  name="srcPort" class="inputText width129 margin_t2b2">';							
						pArea.html(html);
					}
				}
				else if(selectedVal == "dstPortVal 3")//dst_port
				{
					e.stopImmediatePropagation();
					var nameVal = document.getElementsByName('dstPort');
					if(nameVal.length>0)
					{
						$.obAlertNotice(VAR_PKTPOP_DSTPORTALREADYSEL);
						$('.cloneDiv select[name = "filterFirst"] option:eq(0)').attr("selected", "selected");
						return;
					}	
					else
					{
						var pArea = $('.cloneDiv .defaultArea').filter(':last');
						pArea.empty();
						var html = "";							
						html +='<input type="text"  name="dstPort" class="inputText width129 margin_t2b2">';							
						pArea.html(html);
					}
				}
				else if(selectedVal == "protocolVal 4")//protocol
				{
					e.stopImmediatePropagation();
					var nameVal = document.getElementsByName('protocol');
					if(nameVal.length>0)
					{
						$.obAlertNotice(VAR_PKTPOP_PROTOCOLALREADYSEL);
						$('.cloneDiv select[name = "filterFirst"] option:eq(0)').attr("selected", "selected");
						var pArea = $('.cloneDiv .defaultArea').filter(':last');
						pArea.empty();
						var html = "";							
						html +='<input type="text"  name="srcIp" class="inputText width129 margin_t2b2">';							
						pArea.html(html);
						return;
					}	
					else
					{
						var pArea = $('.cloneDiv .defaultArea').filter(':last');
						pArea.empty();
						var html = "";								
						html +='<select name="protocol" class="inputSelect width134">';
						html +='<option value="TCP">' + VAR_COMMON_TCP + '</option>';
						html +='<option value="UDP">' + VAR_COMMON_UDP + '</option>';
						html +='<option value="ICMP">' + VAR_COMMON_ICMP + '</option>';
						html +='</select>';						
						pArea.html(html);
					}
				}
				else if(selectedVal == "hostVal 5")//host
				{
					e.stopImmediatePropagation();
					var nameVal = document.getElementsByName('host');
					if(nameVal.length>0)
					{
						$.obAlertNotice(VAR_PKTPOP_HOSTALREADYSEL);
						$('.cloneDiv select[name = "filterFirst"] option:eq(0)').attr("selected", "selected");
						var pArea = $('.cloneDiv .defaultArea').filter(':last');
						pArea.empty();
						var html = "";							
						html +='<input type="text"  name="srcIp" class="inputText width129 margin_t2b2">';							
						pArea.html(html);
						return;
					}	
					else
					{
						var pArea = $('.cloneDiv .defaultArea').filter(':last');
						pArea.empty();
						var html = "";							
						html +='<input type="text"  name="host" class="inputText width129 margin_t2b2">';							
						pArea.html(html);						
					}
				}
				else if(selectedVal == "portVal 6")//port
				{
					e.stopImmediatePropagation();
					var nameVal = document.getElementsByName('port');
					if(nameVal.length>0)
					{
						$.obAlertNotice(VAR_PKTPOP_PORTALREADYSEL);
						$('.cloneDiv select[name = "filterFirst"] option:eq(0)').attr("selected", "selected");
						var pArea = $('.cloneDiv .defaultArea').filter(':last');
						pArea.empty();
						var html = "";							
						html +='<input type="text"  name="srcIp" class="inputText width129 margin_t2b2">';							
						pArea.html(html);
						return;
					}	
					else
					{
						var pArea = $('.cloneDiv .defaultArea').filter(':last');
						pArea.empty();
						var html = "";							
						html +='<input type="text"  name="port" class="inputText width129 margin_t2b2">';							
						pArea.html(html);						
					}
				}
			});
			optionMaxPkt = $('.cloneDiv input[name="optionMaxPkt"]').val();
			optionMaxTime = $('.cloneDiv input[name="optionMaxTime"]').val();
			optionMaxSize = $('.cloneDiv #optionMaxSize').val();			
			//TODO
			$('.maxPktChk').change(function(e)
			{
				var isChecked = $(this).is(':checked');
				if (isChecked == true)
				{
					$('input[name="optionMaxPkt"]').removeAttr("disabled");
					optionMaxPkt = $('.cloneDiv #optionMaxPkt').val();
					MaxPktChkKey = 1;
				}
				else
				{
					$('input[name="optionMaxPkt"]').attr("disabled", "disabled");
					optionMaxPkt = 0;
					MaxPktChkKey = 0;
				}			
			});
			$('.maxTimeChk').change(function(e)
			{
				var isChecked = $(this).is(':checked');
				if (isChecked == true)
				{
					$('input[name="optionMaxTime"]').removeAttr("disabled");
					optionMaxTime = $('.cloneDiv #optionMaxTime').val();
					MaxTimeChkKey = 1;
				}
				else
				{
					$('input[name="optionMaxTime"]').attr("disabled", "disabled");
					optionMaxTime = 0;
					MaxTimeChkKey = 0;
				}
			});
			$('.pktSizeChk').change(function(e)
			{
				var isChecked = $(this).is(':checked');
				if (isChecked == true)
				{					
					$('input[name="optionMaxSize"]').removeAttr("disabled");
					optionMaxSize = $('.cloneDiv #optionMaxSize').val();
					MaxSizeChkKey = 1;
				}
				else
				{
					$('input[name="optionMaxSize"]').attr("disabled", "disabled");
					optionMaxSize = 0;
					MaxSizeChkKey = 0;
				}
			});			
		}
	},
	
	// AnalysisTimer 초기화 부분
	clearAnalysisTimer : function() 
	{
		with (this)
		{
			if (null != this.analysisTimeTimer)
			{
				clearInterval(this.analysisTimeTimer);
				this.analysisTimeTimer = null;
			}
		}
	},	
	//TODO
	// 진단 진행 상태 데이터를 Get 한다.
	startPktDump : function(index)
	{
		with(this)
		{
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
			
			if (MaxPktChkKey == 0 && MaxTimeChkKey == 0 && MaxSizeChkKey == 0)
			{
				$.obAlertNotice(VAR_PKTPOP_ONEMORE);
				return;
			}
			if (MaxPktChkKey == 1)
			{
				optionMaxPkt = $('.cloneDiv input[name="optionMaxPkt"]').val();	
			}
			else
			{
				optionMaxPkt = 0;
			}
			if (MaxTimeChkKey == 1)
			{
				optionMaxTime = $('.cloneDiv input[name="optionMaxTime"]').val();	
			}
			else
			{
				optionMaxTime = 0;
			}			
			
			if (MaxSizeChkKey == 1)
			{
				optionMaxSize = $('.cloneDiv input[name="optionMaxSize"]').val();			
			}
			else
			{
				optionMaxSize = 0;
			}
			
			if (!validationFilterChk())
				return false;
			if (!validateStartDump(index))
				return false;	
			
			var portName = "";
			if ((adcSetting.getAdc().type =="F5") && ($('.cloneDiv #targetSel option:selected').val() == "vlanVal"))
			{
				portName = $('.cloneDiv #vlanName option:selected').val();
			}
			else
			{
				portName = $('.cloneDiv #interfaceName option:selected').val();
			}
			//alert(portName);
			ajaxManager.runJsonExt({
				url	: "faultAnalysis/startPktdump.action",
				data : 
				{
					"adcObject.category"	: adcSetting.getSelectIndex(),
					"adcObject.index"		: selectedIndex,
					"adcObject.name"		: adcSetting.getGroupName(),
					"optionMaxSize"			: optionMaxSize,
					"optionMaxTime"			: optionMaxTime,
					"optionMaxPkt"			: optionMaxPkt,
					"fileName"				: $('.cloneDiv input[name="fileName"]').val().trim(), // 파일명 공백제거					
					"interfaceName"			: portName,
//					"vlanName"				: $('.cloneDiv #vlanName option:selected').val(),
                    "srcIp" 				: $('.cloneDiv input[name="srcIp"]').val(),
                    "dstIp" 				: $('.cloneDiv input[name="dstIp"]').val(),
                    "srcPort" 				: $('.cloneDiv input[name="srcPort"]').val(),
                    "dstPort" 				: $('.cloneDiv input[name="dstPort"]').val(),
                    "protocol" 				: $('.cloneDiv select[name="protocol"]').val(),
                    "port" 					: $('.cloneDiv input[name="port"]').val(),
                    "host"					: $('.cloneDiv input[name="host"]').val()
				},
				successFn	: function(data)
				{	
					if (!data.isSuccessful)
					{
						$.obAlertNotice(data.message);
						return;
					}
					
					var logkeyObj = [];
					//그룹인경우
//					if (adcSetting.getSelectIndex() != 2)
//					{						
						for (var i=0; i < data.pktDumpIndexList.length; i++ )
						{
							logkeyObj.push(data.pktDumpIndexList[i].logKey);						
						}				
				
						loadPktDumpStatusListContent(logkeyObj);
						loadPktDumpStatusInfoContent(logkeyObj);
//						loadPktDumpChartStatusListContent();
//					}
//					else
//					{
//						logkeyObj = data.pktDumpIndexList[0].logKey;
//						loadPktDumpStatusListContent(logkeyObj);
//					}
					
//					alert("logkeyObj : " + logkeyObj);
//					loadCpuChartInfo(logkeyObj);
//					updateAnalysisState(data);
//					loadPktDumpStatusListContent(data.pktDumpIndexList.);
//					loadPktDumpStatusListContent(2483411380130);
//					loadCpuChartInfo(2483411380130);
//					loadPktDumpChartContent(2483411380130);
					
				},
				completeFn	: function(data)
				{			
					analysisPopupAliveFlag = false;
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_PKTPOP_STARTFAIL, jqXhr);		
					clearAnalysisTimer();
				}
			});
		}
	},
	
	// 실시간 CPU & Memory Chart Core
	displayRealTimeSystemUsage : function (categoryIndex, adcIndex)
	{
		with(this)
		{
			var $cpuChart = $('.cpuChart').filter(':last');
			$cpuChart.empty();
			var html = '';			
			html += '<div id="analysisCpuChart" style="width: 200px; height: 135px; margin-top: 0px; "></div>';			
			$cpuChart.html(html);
			
			var $memoryChart = $('.memoryChart').filter(':last');
			$memoryChart.empty();
			var html = '';			
			html += '<div id="analysisMemoryChart" style="width: 200px; height: 135px; margin-top: 0px;"></div>';			
			$memoryChart.html(html);
			
			var min = 0;
			var max = 100;
			var chartname = "analysisCpuChart";
			var chartunit = "%";
			var adcType = "Alteon";
			analysisCpuChart = obchart.OBCreateRealTimeMultiValueChart(min, max, chartname, chartunit, adcType);
			
			var chartOption =
			{
				 min : 0,
				 max : 100,
				 linecolor : "#6cb8c8",
				 chartname : "analysisMemoryChart",
				 axistitle : "%",
				 maxPos : null,
				 cursorColor : "#0f47c7"
			};
			analysisMemoryChart = obchart.OBCreateRealTimeChart(chartOption);
			
			clearChartTimer();
			SetDiagnosisChartData(categoryIndex, adcIndex, analysisCpuChart, analysisMemoryChart);
						
			this.chartTimeTimer = setInterval(function () 
			{
				loadDiagnosisChartData(categoryIndex, adcIndex, analysisCpuChart, analysisMemoryChart);				
			}, 1000);
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
				url			: "faultAnalysis/loadCpuChartInfo.action",
				data		: params,
				successFn	: function(data) 
				{	
					var startTime = parseDateTime(data.pktDumpCpuMemoryInfo.occurTime);									
					var endTime = parseDateTime(data.pktDumpCpuMemoryInfo.occurTime);					
					
					var minusSecond = (startTime.getSeconds()) - (obchart.varInitCount4Realtime * refreshIntervalSeconds);	// 갯수 x 
					startTime.setSeconds(minusSecond);					

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
				},
				completeFn	: function(data)
				{
				},
				errorFn : function(jqXhr)
				{
//					exceptionEvent();
					$.obAlertAjaxError(VAR_PKTPOP_STARTFAIL, jqXhr);	
					clearChartTimer();
				}
			});
		}
	},
	
	//패킷 상태 load
	loadPktDumpStatusListContent : function(logList, flag)
	{
		with (this)
		{
			ajaxManager.runHtmlExt({
				url	: "faultAnalysis/loadPktDumpStatusListContent.action",
				target : ".pktDumpStatusTableData",
				data : 
				{
					"adcObject.category"	: adcSetting.getSelectIndex(),
					"adcObject.index" 		: selectedIndex,
					"adcObject.name"		: adcSetting.getGroupName(),
					"adc.type" 				: adcSetting.getAdc().type,
					"adc.name" 				: adcSetting.getAdc().name,
					"logKeyList" 			: logList
				},
				successFn : function(params)
				{
					// 새로 선택된 Index 가 없다면 첫 tr 에 컬러를 입히고, 있을때는 해당 tr 의 컬러를 입힌다.
					if (selectedColumnIndex === undefined)
					{
						$('.pktDmpTable tbody tr:first-child').addClass("vsMonitorRowSelection");
					}
					else
					{
						selectpktDmpTableRow(selectedColumnIndex);
					}
					
					analysisPopupAliveFlag = false;
					registerPktDump(adcSetting.getSelectIndex(), selectedIndex, flag ? flag : 2, logList);
					//curSelectedIndex = $('.pktDmpTable tbody tr').attr('id'); 
					// 패킷수집 load refresh
					refreshPktDumpAnalysisTableList(logList);
//					refreshPktDumpAnalysisTableList(logList, curSelectedIndex);	
				},		
				completeFn : function()
				{					
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_COMMON_STATUSEXTRACT, jqXhr);		
				}
			});
		}
	},
	
	loadPktDumpStatusInfoContent : function(logList)
	{
		with (this)
		{
			ajaxManager.runJsonExt({
				url	: "faultAnalysis/loadPktDumpStatusInfoContent.action",			
				data : 
				{
					"adcObject.category"	: adcSetting.getSelectIndex(),
					"adcObject.index" 		: selectedIndex,
					"adcObject.name"		: adcSetting.getGroupName(),
					"adc.type" 				: adcSetting.getAdc().type,
					"adc.name" 				: adcSetting.getAdc().name,
					"logKeyList" 			: logList
				},
				successFn : function(data)
				{	
					if (!data.isSuccessful)
					{
						$.obAlertNotice(data.message);
						return;
					}
					analysisPopupAliveFlag = false;	
					var selectedAdcIndex = data.pktDumpStatusInfoList[0].adcIndex;
//					registerPktDump(adcSetting.getSelectIndex(), selectedIndex, 2, logList);
					loadPktDumpChartStatusListContent(2, selectedAdcIndex);				
				},		
				completeFn : function()
				{					
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_COMMON_STATUSEXTRACT, jqXhr);		
				}
			});
		}
	},
		
	loadPktDumpChartStatusListContent : function(category, adcIdx)
	{
		with (this)
		{
			ajaxManager.runHtmlExt({
				url	: "faultAnalysis/loadPktDumpChartStatusListContent.action",
				target : ".pktDumpStatusCPUData",
				data : 
				{
					"adcObject.category"	: adcSetting.getSelectIndex(),
					"adcObject.index" 		: selectedIndex,
					"adcObject.name"		: adcSetting.getGroupName(),
					"adc.type" 				: adcSetting.getAdc().type,
					"adc.name" 				: adcSetting.getAdc().name
				},
				successFn : function(params)
				{		
					analysisPopupAliveFlag = false;		
					//GenerateGauge();
					displayRealTimeSystemUsage(category, adcIdx);		
				},				
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_COMMON_STATUSEXTRACT, jqXhr);		
				}
			});
		}
	},	
	
	// 패킷수집 중지
	stopPktDump : function(key, flag)
	{
		with(this)
		{
			var flagUrl = "";
			if (flag == 0)
			{
				flagUrl = "faultAnalysis/stopPktdump.action";
			}
			else if (flag == 1)
			{
				flagUrl = "faultAnalysis/cancelPktdump.action";
			}
			ajaxManager.runJsonExt({
				url	: flagUrl,
				data : 
				{
					"adcObject.category"	: adcSetting.getSelectIndex(),
					"adcObject.index" 		: selectedIndex,
					"logKeyText" : key
				},
				successFn	: function(data)
				{					
					if (!data.isSuccessful)
					{
						$.obAlertNotice(data.message);
						return;
					}
					analysisPopupAliveFlag = false;
//					clearRefreshTimer();
				},
				completeFn	: function(data)
				{					
					analysisPopupAliveFlag = false;
//					clearRefreshTimer();
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_PKTPOP_STOPCANCEL, jqXhr);		
					clearAnalysisTimer();
				}
			});
		}
	},
	
	// 패킷수집 취소
	cancelPkt : function(logList)
	{
		with(this)
		{
			ajaxManager.runJsonExt({
				url	: "faultAnalysis/stopPktdump.action",
				data : 
				{
					"adcObject.category"	: adcSetting.getSelectIndex(),
					"logKeyList" 			: logList
				},
				successFn	: function(data)
				{				
					if (data.isSuccessful)
					{
						$.obAlertNotice(data.message);
						return;
					}
					diagnosisPopupAliveFlag = false;
				},
				completeFn	: function(data)
				{
					diagnosisPopupAliveFlag = false;
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_PKTPOP_STOPFAIL, jqXhr);		
					clearAnalysisTimer();
				}
			});
		}
	},	
	// 실시간 차트의 초기화 후 실제 움직이는 데이터가 들어가는부분, 상위 함수호출에서의 timer 로 반복된다.
	loadDiagnosisChartData : function(categoryIndex, adcIndex, cpuChartObject, memoryChartObject) 
	{
		with (this) 
		{
			var params = {};
			params["adcObject.category"] = categoryIndex;
			params["adcObject.index"] = adcIndex;			
			ajaxManagerOB.runJsonExt
			({
				url			: "faultAnalysis/loadCpuChartInfo.action",
				data		: params,
				successFn	: function(data) 
				{
					var column = data.pktDumpCpuMemoryInfo;
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
					if(generateGaugeKey == false)
					{
						GenerateGauge(cpuValue1, memValue);
					}
					else
					{
						UpdateGage(cpuValue1, memValue);
					}				
				},
				completeFn	: function(data) 
				{
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_COMMON_CPUMEMDATAEXTRACT, jqXhr);		
					clearChartTimer();
				},
			});
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
	        generateGaugeKey = true;
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
	_getFilterIndex : function()
	{
		with(this)
		{
			var filter;
			filter = document.getElementsByName("filter2").length;
			if(filter <=0)//select2이 없는경우,
			{
				return 2;
			}
			filter = document.getElementsByName("filter3").length;
			if(filter <=0)//select3이 없는경우,
			{
				return 3;
			}
			filter = document.getElementsByName("filter4").length;
			if(filter <=0)//select4이 없는경우,
			{
				return 4;
			}
			filter = document.getElementsByName("filter5").length;
			if(filter <=0)//select5이 없는경우,
			{
				return 5;
			}
			filter = document.getElementsByName("filter6").length;
			if(filter <=0)//select6이 없는경우,
			{
				return 6;
			}
			
			return -1;
		}
	},
			
	//필터 추가
	_addFilterForm : function()
	{
		with(this)
		{
			var filterIndex = _getFilterIndex();
			if(filterIndex == -1)//error;
			{
//				alert("ooops. index : -1");
				return;
			}
			var $area = $('.pop_type_wrap .addSelectFilter').filter(':last');
			var html = "";

			var divSelectName = "divSelect"+filterIndex;
			var filterName = "filter"+filterIndex;
			var filterInputName = "filterInput"+filterIndex;
			var deleteName = "delete"+filterIndex;
			html +='<div class="'+divSelectName+'" >';
			html +='<select id="'+filterName+'" name="'+filterName+'" class="width134 defaultSelect" >';
			html +='<option value="srcIpVal 0">' + VAR_PKTPOP_SRCIP + '</option>';
			html +='<option value="dstIpVal 1">' + VAR_PKTPOP_DSTIP + '</option>';
			html +='<option value="srcPortVal 2">' + VAR_PKTPOP_SRCPORT + '</option>';
			html +='<option value="dstPortVal 3">' + VAR_PKTPOP_DSTPORT + '</option>';
			html +='<option value="protocolVal 4">' + VAR_PKTPOP_PROTOCOL + '</option>';
//			html +='<option value="hostVal 5">' + VAR_PKTPOP_HOST + '</option></select>&nbsp;';
			html +='<option value="hostVal 5">' + VAR_PKTPOP_HOST + '</option>'; // from GS. #4012-1 #2: 14.07.29 sw.jung: 추가필터에 port안나오는 현상 수정
			html +='<option value="portVal 6">Port</option></select>&nbsp;'; // TODO: Message가 없어 일단 하드코딩, 추후 추가
			html +='<span class="valueArea">';
			html +='<input type="text"  name="srcIp" id="'+filterInputName+'" class="inputText width129 margin_t2b2">';	
			html +='</span>&nbsp;';
			html +='<span title=' + '"' + VAR_PKTPOP_DEL + '"' + 'class="'+deleteName+'"  href="#">'; 
			html +='<img src="/imgs/btn' + getImgLang() + '/btn_delfilter.gif" /></span></div>';
 
//			var filter2 = document.getElementsByName("filter2").length;
//			var filter3 = document.getElementsByName("filter3").length;
//			var filter4 = document.getElementsByName("filter4").length;
//			var filter5 = document.getElementsByName("filter5").length;
//			var filter6 = document.getElementsByName("filter6").length;
//			
//			var $area = $('.pop_type_wrap .addSelectFilter').filter(':last');
//			var html = "";
//
//			if(filter2 <=0)//select2이 없는경우,
//			{
//				//select2로 추가
//				html +='<div class="divSelect2" >';
//				html +='<select id="filter2" name="filter2" class="width134 defaultSelect" >';
//				
//				html +='<option value="srcIpVal 0">' + VAR_PKTPOP_SRCIP + '</option>';
//				html +='<option value="dstIpVal 1">' + VAR_PKTPOP_DSTIP + '</option>';
//				html +='<option value="srcPortVal 2">' + VAR_PKTPOP_SRCPORT + '</option>';
//				html +='<option value="dstPortVal 3">' + VAR_PKTPOP_DSTPORT + '</option>';
//				html +='<option value="protocolVal 4">' + VAR_PKTPOP_PROTOCOL + '</option>';
//				html +='<option value="hostVal 5">' + VAR_PKTPOP_HOST + '</option></select>&nbsp;';
//				html +='<span class="valueArea">';
//				html +='<input type="text"  name="srcIp" id="filterInput2" class="inputText width129 margin_t2b2">';	
//				html +='</span>&nbsp;';
//				html +='<span title=' + '"' + VAR_PKTPOP_DEL + '"' + 'class="delete2"  href="#">'; 
//				if (langCode == "ko_KR")
//				{
//					html +='<img src="/imgs/btn/btn_delfilter.gif" /></span></div>';
//				}
//				else if (langCode == "en_US")
//				{
//					html +='<img src="/imgs/btn_eng/btn_delfilter.gif" /></span></div>';
//				}
//				else
//				{
//					html +='<img src="/imgs/btn/btn_delfilter.gif" /></span></div>';
//				}				
//			}
//			else
//			{
//				if(filter3 <=0) //select2이 있고 select3가 없는경우,
//				{
//					//select3로 추가
//					html +='<div class="divSelect3" >';
//					html +='<select id="filter3" name="filter3" class="width134 defaultSelect">';
//					html +='<option value="srcIpVal 0">' + VAR_PKTPOP_SRCIP + '</option>';
//					html +='<option value="dstIpVal 1">' + VAR_PKTPOP_DSTIP + '</option>';
//					html +='<option value="srcPortVal 2">' + VAR_PKTPOP_SRCPORT + '</option>';
//					html +='<option value="dstPortVal 3">' + VAR_PKTPOP_DSTPORT + '</option>';
//					html +='<option value="protocolVal 4">' + VAR_PKTPOP_PROTOCOL + '</option>';
//					html +='<option value="hostVal 5">' + VAR_PKTPOP_HOST + '</option></select>&nbsp;';
//					html +='<span class="valueArea">';
//					html +='<input type="text"  name="srcIp" id="filterInput3" class="inputText width129 margin_t2b2">';	
//					html +='</span>&nbsp;';
//					html +='<span title=' + '"' + VAR_PKTPOP_DEL + '"' + 'class="delete3"  href="#">';
//					if (langCode == "ko_KR")
//					{
//						html +='<img src="/imgs/btn/btn_delfilter.gif" /></span></div>';
//					}
//					else if (langCode == "en_US")
//					{
//						html +='<img src="/imgs/btn_eng/btn_delfilter.gif" /></span></div>';
//					}
//					else
//					{
//						html +='<img src="/imgs/btn/btn_delfilter.gif" /></span></div>';
//					}
//				}
//				else
//				{
//					if(filter4 <=0) //select3 있고, select4이 없는 경우
//					{
//						//select4으로 추가
//						html +='<div class="divSelect4" >';
//						html +='<select id="filter4" name="filter4" class="width134 defaultSelect">';
//						html +='<option value="srcIpVal 0">' + VAR_PKTPOP_SRCIP + '</option>';
//						html +='<option value="dstIpVal 1">' + VAR_PKTPOP_DSTIP + '</option>';
//						html +='<option value="srcPortVal 2">' + VAR_PKTPOP_SRCPORT + '</option>';
//						html +='<option value="dstPortVal 3">' + VAR_PKTPOP_DSTPORT + '</option>';
//						html +='<option value="protocolVal 4">' + VAR_PKTPOP_PROTOCOL + '</option>';
//						html +='<option value="hostVal 5">' + VAR_PKTPOP_HOST + '</option></select>&nbsp;';
//						html +='<span class="valueArea">';
//						html +='<input type="text"  name="srcIp" id="filterInput4" class="inputText width129 margin_t2b2">';	
//						html +='</span>&nbsp;';						
//						html +='<span title=' + '"' + VAR_PKTPOP_DEL + '"' + 'class="delete4"  href="#">';
//						if (langCode == "ko_KR")
//						{
//							html +='<img src="/imgs/btn/btn_delfilter.gif" /></span></div>';
//						}
//						else if (langCode == "en_US")
//						{
//							html +='<img src="/imgs/btn_eng/btn_delfilter.gif" /></span></div>';
//						}
//						else
//						{
//							html +='<img src="/imgs/btn/btn_delfilter.gif" /></span></div>';
//						}
//					}
//					else
//					{
//						if(filter5 <=0)//selct4 있고 select5가 없는경우
//						{
//							//select5로 추가
//							html +='<div class="divSelect5" >';
//							html +='<select id="filter5" name="filter5" class="width134 defaultSelect">';
//							html +='<option value="srcIpVal 0">' + VAR_PKTPOP_SRCIP + '</option>';
//							html +='<option value="dstIpVal 1">' + VAR_PKTPOP_DSTIP + '</option>';
//							html +='<option value="srcPortVal 2">' + VAR_PKTPOP_SRCPORT + '</option>';
//							html +='<option value="dstPortVal 3">' + VAR_PKTPOP_DSTPORT + '</option>';
//							html +='<option value="protocolVal 4">' + VAR_PKTPOP_PROTOCOL + '</option>';
//							html +='<option value="hostVal 5">' + VAR_PKTPOP_HOST + '</option></select>&nbsp;';
//							html +='<span class="valueArea">';
//							html +='<input type="text"  name="srcIp" id="filterInput5" class="inputText width129 margin_t2b2">';	
//							html +='</span>&nbsp;';							
//							html +='<span title=' + '"' + VAR_PKTPOP_DEL + '"' + 'class="delete5"  href="#">';
//							if (langCode == "ko_KR")
//							{
//								html +='<img src="/imgs/btn/btn_delfilter.gif" /></span></div>';
//							}
//							else if (langCode == "en_US")
//							{
//								html +='<img src="/imgs/btn_eng/btn_delfilter.gif" /></span></div>';
//							}
//							else
//							{
//								html +='<img src="/imgs/btn/btn_delfilter.gif" /></span></div>';
//							}
//						}
//						else
//						{
//							if(filter6 <=0)//selct5 있고 select6가 없는경우
//							{
//								//select6로 추가
//								html +='<div class="divSelect6" >';
//								html +='<select id="filter6" name="filter6" class="width134 defaultSelect">';
//								html +='<option value="srcIpVal 0">' + VAR_PKTPOP_SRCIP + '</option>';
//								html +='<option value="dstIpVal 1">' + VAR_PKTPOP_DSTIP + '</option>';
//								html +='<option value="srcPortVal 2">' + VAR_PKTPOP_SRCPORT + '</option>';
//								html +='<option value="dstPortVal 3">' + VAR_PKTPOP_DSTPORT + '</option>';
//								html +='<option value="protocolVal 4">' + VAR_PKTPOP_PROTOCOL + '</option>';
//								html +='<option value="hostVal 5">' + VAR_PKTPOP_HOST + '</option></select>&nbsp;';
//								html +='<span class="valueArea">';
//								html +='<input type="text"  name="srcIp" id="filterInput6" class="inputText width129 margin_t2b2">';	
//								html +='</span>&nbsp;';								
//								html +='<span title=' + '"' + VAR_PKTPOP_DEL + '"' + 'class="delete6"  href="#">';
//								if (langCode == "ko_KR")
//								{
//									html +='<img src="/imgs/btn/btn_delfilter.gif" /></span></div>';
//								}
//								else if (langCode == "en_US")
//								{
//									html +='<img src="/imgs/btn_eng/btn_delfilter.gif" /></span></div>';
//								}
//								else
//								{
//									html +='<img src="/imgs/btn/btn_delfilter.gif" /></span></div>';
//								}
//							}
//						}
//					}	
//				}	
//			}
			$area.append(html);
			_checkFilterSelectBoxLength();
			
			$('.delete2').click(function(e)
			{
				$('.divSelect2').remove();
				_checkFilterSelectBoxLength();	
			});

			$('.delete3').click(function(e)
			{
				$('.divSelect3').remove();
				_checkFilterSelectBoxLength();
			});	
			
			$('.delete4').click(function(e)
			{			
				$('.divSelect4').remove();
				_checkFilterSelectBoxLength();	
			});	
			
			$('.delete5').click(function(e)
			{
				$('.divSelect5').remove();
				_checkFilterSelectBoxLength();
			});	
			
			$('.delete6').click(function(e)
			{
				$('.divSelect6').remove();
				_checkFilterSelectBoxLength();
	
			});
		}		
	},	
	
	// 필터 selectBox 이벤트 처리
	_registSelectChangeEvent : function()
	{
		with(this)
		{			
			/*2번째 selectBox 변화시 처리*/
			$('select[name = "filter2"]').change(function(e)
			{
				e.stopImmediatePropagation();
				var selectedVal = $('select[name = "filter2"]').val();
				if(selectedVal == "srcIpVal 0")//src_ip
				{
					var nameVal = document.getElementsByName('srcIp');
					if(nameVal.length>0)
					{	
						e.stopImmediatePropagation();
						$.obAlertNotice(VAR_PKTPOP_SRCIPALREADYSEL);
						$('select[name = "filter2"] option:eq(0)').attr("selected", "selected");
						var pArea = $('.divSelect2 .valueArea').filter(':last');
						pArea.empty();
						var html = "";						
						html +='<input type="text"  name="srcIp" class="inputText width129 margin_t2b2">';							
						pArea.html(html);
						return;
					}	
					else
					{
						var pArea = $('.divSelect2 .valueArea').filter(':last');
						pArea.empty();
						var html = "";						
						html +='<input type="text"  name="srcIp" class="inputText width129 margin_t2b2">';							
						pArea.html(html);
					}	
				}
				else if(selectedVal == "dstIpVal 1")//dst_Ip
				{
					e.stopImmediatePropagation();
					var nameVal = document.getElementsByName('dstIp');
					if(nameVal.length>0)
					{
						$.obAlertNotice(VAR_PKTPOP_DSTIPALREAYSEL);
						$('select[name = "filtert2"] option:eq(0)').attr("selected", "selected");
						var pArea = $('.divSelect2 .valueArea').filter(':last');
						pArea.empty();
						var html = "";						
						html +='<input type="text"  name="srcIp" class="inputText width129 margin_t2b2">';							
						pArea.html(html);
						return;
					}	
					else
					{	
						var pArea = $('.divSelect2 .valueArea').filter(':last');
						pArea.empty();
						var html = "";						
						html +='<input type="text"  name="dstIp" class="inputText width129 margin_t2b2">';							
						pArea.html(html);
					}	
					
				}
				else if(selectedVal == "srcPortVal 2")//src_port
				{
					e.stopImmediatePropagation();
					var nameVal = document.getElementsByName('srcPort');
					if(nameVal.length>0)
					{
						$.obAlertNotice(VAR_PKTPOP_SRCPORTALREADYSEL);
						$('select[name = "filter2"] option:eq(0)').attr("selected", "selected");
						return;
					}	
					else
					{
						var pArea = $('.divSelect2 .valueArea').filter(':last');
						pArea.empty();
						var html = "";						
						html +='<input type="text"  name="srcPort" class="inputText width129 margin_t2b2">';							
						pArea.html(html);
					}
				}
				else if(selectedVal == "dstPortVal 3")//dst_port
				{
					e.stopImmediatePropagation();
					var nameVal = document.getElementsByName('dstPort');
					if(nameVal.length>0)
					{
						$.obAlertNotice(VAR_PKTPOP_DSTPORTALREADYSEL);
						$('select[name = "filter2"] option:eq(0)').attr("selected", "selected");
						var pArea = $('.divSelect2 .valueArea').filter(':last');
						pArea.empty();
						var html = "";						
						html +='<input type="text"  name="srcIp" class="inputText width129 margin_t2b2">';							
						pArea.html(html);
						return;
					}	
					else
					{
						var pArea = $('.divSelect2 .valueArea').filter(':last');
						pArea.empty();
						var html = "";						
						html +='<input type="text"  name="dstPort" class="inputText width129 margin_t2b2">';							
						pArea.html(html);
					}
				}
				else if(selectedVal == "protocolVal 4")//protocol
				{
					e.stopImmediatePropagation();
					var nameVal = document.getElementsByName('protocol');
					if(nameVal.length>0)
					{
						$.obAlertNotice(VAR_PKTPOP_PROTOCOLALREADYSEL);
						$('select[name = "filter2"] option:eq(0)').attr("selected", "selected");
						var pArea = $('.divSelect2 .valueArea').filter(':last');
						pArea.empty();
						var html = "";						
						html +='<input type="text"  name="srcIp" class="inputText width129 margin_t2b2">';							
						pArea.html(html);
						return;
					}	
					else
					{	
						var pArea = $('.divSelect2 .valueArea').filter(':last');
						pArea.empty();
						var html = "";						
						html +='<select name="protocol" class="inputSelect width129 margin_t2b2">';
						html +='<option value="TCP">' + VAR_COMMON_TCP + '</option>';
						html +='<option value="UDP">' + VAR_COMMON_UDP + '</option>';
						html +='<option value="ICMP">' + VAR_COMMON_ICMP + '</option>';
						html +='</select>';
						
						pArea.html(html);
					}
				}				
				
				else if(selectedVal == "hostVal 5")//host
				{
					e.stopImmediatePropagation();
					var nameVal = document.getElementsByName('host');
					if(nameVal.length>0)
					{
						e.stopImmediatePropagation();
						$.obAlertNotice(VAR_PKTPOP_HOSTALREADYSEL);
						$('select[name = "filter2"] option:eq(0)').attr("selected", "selected");
						var pArea = $('.divSelect2 .valueArea').filter(':last');
						pArea.empty();
						var html = "";						
						html +='<input type="text"  name="srcIp" class="inputText width129 margin_t2b2">';							
						pArea.html(html);
						return;
					}	
					else
					{	
						var pArea = $('.divSelect2 .valueArea').filter(':last');
						pArea.empty();
						var html = "";						
						html +='<input type="text"  name="host" class="inputText width129 margin_t2b2">';							
						pArea.html(html);
					}
				}
				
				else if(selectedVal == "portVal 6")//port
				{
					e.stopImmediatePropagation();
					var nameVal = document.getElementsByName('port');
					if(nameVal.length>0)
					{
						e.stopImmediatePropagation();
						$.obAlertNotice(VAR_PKTPOP_PORTALREADYSEL);
						$('select[name = "filter2"] option:eq(0)').attr("selected", "selected");
						var pArea = $('.divSelect2 .valueArea').filter(':last');
						pArea.empty();
						var html = "";						
						html +='<input type="text"  name="srcIp" class="inputText width129 margin_t2b2">';							
						pArea.html(html);
						return;
					}	
					else
					{	
						var pArea = $('.divSelect2 .valueArea').filter(':last');
						pArea.empty();
						var html = "";						
						html +='<input type="text"  name="port" class="inputText width129 margin_t2b2">';							
						pArea.html(html);
					}
				}				
			});
			/*3번째 selectBox 변화시 처리*/
			$('select[name = "filter3"]').change(function(e)
			{
				e.stopImmediatePropagation();

				var selectedVal = $('select[name = "filter3"]').val();
				if(selectedVal == "srcIpVal 0")//src_ip
				{
					e.stopImmediatePropagation();
					var nameVal = document.getElementsByName('srcIp');
					if(nameVal.length>0)
					{
						$.obAlertNotice(VAR_PKTPOP_SRCIPALREADYSEL);
						$('select[name = "filter3"] option:eq(0)').attr("selected", "selected");
						var pArea = $('.divSelect3 .valueArea').filter(':last');
						pArea.empty();
						var html = "";						
						html +='<input type="text"  name="srcIp" class="inputText width129 margin_t2b2">';							
						pArea.html(html);
						return;
					}	
					else
					{
						var pArea = $('.divSelect3 .valueArea').filter(':last');
						pArea.empty();
						var html = "";						
						html +='<input type="text"  name="srcIp" class="inputText width129 margin_t2b2">';							
						pArea.html(html);
					}	
				}
				else if(selectedVal == "dstIpVal 1")//dst_Ip
				{
					e.stopImmediatePropagation();
					var nameVal = document.getElementsByName('dstIp');
					if(nameVal.length>0)
					{
						$.obAlertNotice(VAR_PKTPOP_DSTIPALREAYSEL);
						$('select[name = "filter3"] option:eq(0)').attr("selected", "selected");
						var pArea = $('.divSelect3 .valueArea').filter(':last');
						pArea.empty();
						var html = "";						
						html +='<input type="text"  name="srcIp" class="inputText width129 margin_t2b2">';							
						pArea.html(html);
						return;
					}	
					else
					{
						var pArea = $('.divSelect3 .valueArea').filter(':last');
						pArea.empty();
						var html = "";						
						html +='<input type="text"  name="dstIp" class="inputText width129 margin_t2b2">';							
						pArea.html(html);
					}	
					
				}		
				else if(selectedVal == "srcPortVal 2")//src_port
				{
					e.stopImmediatePropagation();
					var nameVal = document.getElementsByName('srcPort');
					if(nameVal.length>0)
					{
						$.obAlertNotice(VAR_PKTPOP_SRCPORTALREADYSEL);
						$('select[name = "filtert3"] option:eq(0)').attr("selected", "selected");
						var pArea = $('.divSelect3 .valueArea').filter(':last');
						pArea.empty();
						var html = "";						
						html +='<input type="text"  name="srcIp" class="inputText width129 margin_t2b2">';							
						pArea.html(html);
						return;
					}	
					else
					{
						var pArea = $('.divSelect3 .valueArea').filter(':last');
						pArea.empty();
						var html = "";						
						html +='<input type="text"  name="srcPort" class="inputText width129 margin_t2b2">';							
						pArea.html(html);
					}
				}
				else if(selectedVal == "dstPortVal 3")//dst_port
				{
					e.stopImmediatePropagation();
					var nameVal = document.getElementsByName('dstPort');
					if(nameVal.length>0)
					{
						$.obAlertNotice(VAR_PKTPOP_DSTPORTALREADYSEL);
						$('select[name = "filter3"] option:eq(0)').attr("selected", "selected");
						var pArea = $('.divSelect3 .valueArea').filter(':last');
						pArea.empty();
						var html = "";						
						html +='<input type="text"  name="srcIp" class="inputText width129 margin_t2b2">';							
						pArea.html(html);
						return;
					}	
					else
					{
						var pArea = $('.divSelect3 .valueArea').filter(':last');
						pArea.empty();
						var html = "";						
						html +='<input type="text"  name="dstPort" class="inputText width129 margin_t2b2">';							
						pArea.html(html);
					}
				}
				else if(selectedVal == "protocolVal 4")//protocol
				{
					e.stopImmediatePropagation();
					var nameVal = document.getElementsByName('protocol');
					if(nameVal.length>0)
					{
						$.obAlertNotice(VAR_PKTPOP_PROTOCOLALREADYSEL);
						$('select[name = "filter3"] option:eq(0)').attr("selected", "selected");
						var pArea = $('.divSelect3 .valueArea').filter(':last');
						pArea.empty();
						var html = "";						
						html +='<input type="text"  name="srcIp" class="inputText width129 margin_t2b2">';							
						pArea.html(html);
						return;
					}	
					else
					{
						var pArea = $('.divSelect3 .valueArea').filter(':last');
						pArea.empty();
						var html = "";						
						html +='<select name="protocol" class="inputSelect width134">';
						html +='<option value="TCP">' + VAR_COMMON_TCP + '</option>';
						html +='<option value="UDP">' + VAR_COMMON_UDP + '</option>';
						html +='<option value="ICMP">' + VAR_COMMON_ICMP + '</option>';
						html +='</select>';
						
						pArea.html(html);
					}
				}
				else if(selectedVal == "hostVal 5")//host
				{
					e.stopImmediatePropagation();
					var nameVal = document.getElementsByName('host');
					if(nameVal.length>0)
					{
						e.stopImmediatePropagation();
						$.obAlertNotice(VAR_PKTPOP_HOSTALREADYSEL);
						$('select[name = "filter3"] option:eq(0)').attr("selected", "selected");
						var pArea = $('.divSelect3 .valueArea').filter(':last');
						pArea.empty();
						var html = "";						
						html +='<input type="text"  name="srcIp" class="inputText width129 margin_t2b2">';							
						pArea.html(html);
						return;
					}	
					else
					{	
						var pArea = $('.divSelect3 .valueArea').filter(':last');
						pArea.empty();
						var html = "";						
						html +='<input type="text"  name="host" class="inputText width129 margin_t2b2">';							
						pArea.html(html);
					}
				}
				else if(selectedVal == "portVal 6")//port
				{
					e.stopImmediatePropagation();
					var nameVal = document.getElementsByName('port');
					if(nameVal.length>0)
					{
						e.stopImmediatePropagation();
						$.obAlertNotice(VAR_PKTPOP_PORTALREADYSEL);
						$('select[name = "filter3"] option:eq(0)').attr("selected", "selected");
						var pArea = $('.divSelect3 .valueArea').filter(':last');
						pArea.empty();
						var html = "";						
						html +='<input type="text"  name="srcIp" class="inputText width129 margin_t2b2">';							
						pArea.html(html);
						return;
					}	
					else
					{	
						var pArea = $('.divSelect3 .valueArea').filter(':last');
						pArea.empty();
						var html = "";						
						html +='<input type="text"  name="port" class="inputText width129 margin_t2b2">';							
						pArea.html(html);
					}
				}				
			});
			/*4번째 selectBox 변화시 처리*/
			$('select[name = "filter4"]').change(function(e)
			{
				e.stopImmediatePropagation();

				var selectedVal = $('select[name = "filter4"]').val();
				if(selectedVal == "srcIpVal 0")//src_ip
				{
					e.stopImmediatePropagation();
					var nameVal = document.getElementsByName('srcIp');
					if(nameVal.length>0)
					{
						$.obAlertNotice(VAR_PKTPOP_SRCIPALREADYSEL);
						$('select[name = "filter4"] option:eq(0)').attr("selected", "selected");
						var pArea = $('.divSelect4 .valueArea').filter(':last');
						pArea.empty();
						var html = "";						
						html +='<input type="text"  name="srcIp" class="inputText width129 margin_t2b2">';							
						pArea.html(html);
						return;
					}	
					else
					{
						var pArea = $('.divSelect4 .valueArea').filter(':last');
						pArea.empty();
						var html = "";						
						html +='<input type="text"  name="srcIp" class="inputText width129 margin_t2b2">';							
						pArea.html(html);
					}	
				}
				else if(selectedVal == "dstIpVal 1")//dst_Ip
				{
					e.stopImmediatePropagation();
					var nameVal = document.getElementsByName('dstIp');
					if(nameVal.length>0)
					{
						$.obAlertNotice(VAR_PKTPOP_DSTIPALREAYSEL);
						$('select[name = "filter4"] option:eq(0)').attr("selected", "selected");
						var pArea = $('.divSelect4 .valueArea').filter(':last');
						pArea.empty();
						var html = "";						
						html +='<input type="text"  name="srcIp" class="inputText width129 margin_t2b2">';							
						pArea.html(html);
						return;
					}	
					else
					{
						var pArea = $('.divSelect4 .valueArea').filter(':last');
						pArea.empty();
						var html = "";						
						html +='<input type="text"  name="dstIp" class="inputText width129 margin_t2b2">';							
						pArea.html(html);
					}	
					
				}	
				else if(selectedVal == "srcPortVal 2")//src_port
				{
					e.stopImmediatePropagation();
					var nameVal = document.getElementsByName('srcPort');
					if(nameVal.length>0)
					{
						$.obAlertNotice(VAR_PKTPOP_SRCPORTALREADYSEL);
						$('select[name = "filter4"] option:eq(0)').attr("selected", "selected");
						var pArea = $('.divSelect4 .valueArea').filter(':last');
						pArea.empty();
						var html = "";						
						html +='<input type="text"  name="srcIp" class="inputText width129 margin_t2b2">';							
						pArea.html(html);
						return;
					}	
					else
					{
						var pArea = $('.divSelect4 .valueArea').filter(':last');
						pArea.empty();
						var html = "";						
						html +='<input type="text"  name="srcPort" class="inputText width129 margin_t2b2">';							
						pArea.html(html);
					}
				}
				else if(selectedVal == "dstPortVal 3")//dst_port
				{
					e.stopImmediatePropagation();
					var nameVal = document.getElementsByName('dstPort');
					if(nameVal.length>0)
					{
						$.obAlertNotice(VAR_PKTPOP_DSTPORTALREADYSEL);
						$('select[name = "filter4"] option:eq(0)').attr("selected", "selected");
						var pArea = $('.divSelect4 .valueArea').filter(':last');
						pArea.empty();
						var html = "";						
						html +='<input type="text"  name="srcIp" class="inputText width129 margin_t2b2">';							
						pArea.html(html);
						return;
					}	
					else
					{
						var pArea = $('.divSelect4 .valueArea').filter(':last');
						pArea.empty();
						var html = "";						
						html +='<input type="text"  name="dstPort" class="inputText width129 margin_t2b2">';							
						pArea.html(html);
					}
				}
				else if(selectedVal == "protocolVal 4")//protocol
				{
					e.stopImmediatePropagation();
					var nameVal = document.getElementsByName('protocol');
					if(nameVal.length>0)
					{
						$.obAlertNotice(VAR_PKTPOP_PROTOCOLALREADYSEL);
						$('select[name = "filter4"] option:eq(0)').attr("selected", "selected");
						var pArea = $('.divSelect4 .valueArea').filter(':last');
						pArea.empty();
						var html = "";						
						html +='<input type="text"  name="srcIp" class="inputText width129 margin_t2b2">';							
						pArea.html(html);
						return;
					}	
					else
					{
						var pArea = $('.divSelect4 .valueArea').filter(':last');
						pArea.empty();
						var html = "";						
						html +='<select name="protocol" class="inputSelect width134">';
						html +='<option value="TCP">' + VAR_COMMON_TCP + '</option>';
						html +='<option value="UDP">' + VAR_COMMON_UDP + '</option>';
						html +='<option value="ICMP">' + VAR_COMMON_ICMP + '</option>';
						html +='</select>';
						
						pArea.html(html);
					}
				}
				else if(selectedVal == "hostVal 5")//host
				{
					e.stopImmediatePropagation();
					var nameVal = document.getElementsByName('host');
					if(nameVal.length>0)
					{
						e.stopImmediatePropagation();
						$.obAlertNotice(VAR_PKTPOP_HOSTALREADYSEL);
						$('select[name = "filter4"] option:eq(0)').attr("selected", "selected");
						var pArea = $('.divSelect4 .valueArea').filter(':last');
						pArea.empty();
						var html = "";						
						html +='<input type="text"  name="srcIp" class="inputText width129 margin_t2b2">';							
						pArea.html(html);
						return;
					}	
					else
					{	
						var pArea = $('.divSelect4 .valueArea').filter(':last');
						pArea.empty();
						var html = "";						
						html +='<input type="text"  name="host" class="inputText width129 margin_t2b2">';							
						pArea.html(html);
					}
				}
				else if(selectedVal == "portVal 6")//port
				{
					e.stopImmediatePropagation();
					var nameVal = document.getElementsByName('port');
					if(nameVal.length>0)
					{
						e.stopImmediatePropagation();
						$.obAlertNotice(VAR_PKTPOP_PORTALREADYSEL);
						$('select[name = "filter4"] option:eq(0)').attr("selected", "selected");
						var pArea = $('.divSelect4 .valueArea').filter(':last');
						pArea.empty();
						var html = "";						
						html +='<input type="text"  name="srcIp" class="inputText width129 margin_t2b2">';							
						pArea.html(html);
						return;
					}	
					else
					{	
						var pArea = $('.divSelect4 .valueArea').filter(':last');
						pArea.empty();
						var html = "";						
						html +='<input type="text"  name="port" class="inputText width129 margin_t2b2">';							
						pArea.html(html);
					}
				}					
			});	
			/* 5번째 selectBox 변화시 처리 */			
			$('select[name = "filter5"]').change(function(e)
			{
				e.stopImmediatePropagation();
				var selectedVal = $('select[name = "filter5"]').val();
				if(selectedVal == "srcIpVal 0")//src_ip
				{
					e.stopImmediatePropagation();
					var nameVal = document.getElementsByName('srcIp');
					if(nameVal.length>0)
					{
						$.obAlertNotice(VAR_PKTPOP_SRCIPALREADYSEL);
						$('select[name = "filter5"] option:eq(0)').attr("selected", "selected");
						var pArea = $('.divSelect5 .valueArea').filter(':last');
						pArea.empty();
						var html = "";						
						html +='<input type="text"  name="srcIp" class="inputText width129 margin_t2b2">';							
						pArea.html(html);
						return;
					}	
					else
					{
						var pArea = $('.divSelect5 .valueArea').filter(':last');
						pArea.empty();
						var html = "";						
						html +='<input type="text"  name="srcIp" class="inputText width129 margin_t2b2">';							
						pArea.html(html);
					}	
				}
				else if(selectedVal == "dstIpVal 1")//dst_Ip
				{
					e.stopImmediatePropagation();
					var nameVal = document.getElementsByName('dstIp');
					if(nameVal.length>0)
					{
						$.obAlertNotice(VAR_PKTPOP_DSTIPALREAYSEL);
						$('select[name = "filter5"] option:eq(0)').attr("selected", "selected");
						var pArea = $('.divSelect5 .valueArea').filter(':last');
						pArea.empty();
						var html = "";						
						html +='<input type="text"  name="srcIp" class="inputText width129 margin_t2b2">';							
						pArea.html(html);
						return;
					}	
					else
					{
						var pArea = $('.divSelect5 .valueArea').filter(':last');
						pArea.empty();
						var html = "";						
						html +='<input type="text"  name="dstIp" class="inputText width129 margin_t2b2">';							
						pArea.html(html);
					}	
					
				}
				else if(selectedVal == "srcPortVal 2")//src_port
				{
					e.stopImmediatePropagation();
					var nameVal = document.getElementsByName('srcPort');
					if(nameVal.length>0)
					{
						$.obAlertNotice(VAR_PKTPOP_SRCPORTALREADYSEL);
						$('select[name = "filter5"] option:eq(0)').attr("selected", "selected");
						var pArea = $('.divSelect5 .valueArea').filter(':last');
						pArea.empty();
						var html = "";						
						html +='<input type="text"  name="srcIp" class="inputText width129 margin_t2b2">';							
						pArea.html(html);
						return;
					}	
					else
					{
						var pArea = $('.divSelect5 .valueArea').filter(':last');
						pArea.empty();
						var html = "";						
						html +='<input type="text"  name="srcPort" class="inputText width129 margin_t2b2">';							
						pArea.html(html);
					}
				}
				else if(selectedVal == "dstPortVal 3")//dst_port
				{
					e.stopImmediatePropagation();
					var nameVal = document.getElementsByName('dstPort');
					if(nameVal.length>0)
					{
						e.stopImmediatePropagation();
						$.obAlertNotice(VAR_PKTPOP_DSTPORTALREADYSEL);
						$('select[name = "filter5"] option:eq(0)').attr("selected", "selected");
						var pArea = $('.divSelect5 .valueArea').filter(':last');
						pArea.empty();
						var html = "";						
						html +='<input type="text"  name="srcIp" class="inputText width129 margin_t2b2">';							
						pArea.html(html);
						return;
					}	
					else
					{
						var pArea = $('.divSelect5 .valueArea').filter(':last');
						pArea.empty();
						var html = "";						
						html +='<input type="text"  name="dstPort" class="inputText width129 margin_t2b2">';							
						pArea.html(html);
					}
				}
				else if(selectedVal == "protocolVal 4")//protocol
				{
					e.stopImmediatePropagation();
					var nameVal = document.getElementsByName('protocol');
					if(nameVal.length>0)
					{
						e.stopImmediatePropagation();
						$.obAlertNotice(VAR_PKTPOP_PROTOCOLALREADYSEL);
						$('select[name = "filter5"] option:eq(0)').attr("selected", "selected");
						var pArea = $('.divSelect5 .valueArea').filter(':last');
						pArea.empty();
						var html = "";						
						html +='<input type="text"  name="srcIp" class="inputText width129 margin_t2b2">';							
						pArea.html(html);
						return;
					}	
					else
					{	
						var pArea = $('.divSelect5 .valueArea').filter(':last');
						pArea.empty();
						var html = "";						
						html +='<select name="protocol" class="inputSelect width134">';
						html +='<option value="TCP">' + VAR_COMMON_TCP + '</option>';
						html +='<option value="UDP">' + VAR_COMMON_UDP + '</option>';
						html +='<option value="ICMP">' + VAR_COMMON_ICMP + '</option>';
						html +='</select>';
						
						pArea.html(html);
					}
				}
				else if(selectedVal == "hostVal 5")//host
				{
					e.stopImmediatePropagation();
					var nameVal = document.getElementsByName('host');
					if(nameVal.length>0)
					{
						e.stopImmediatePropagation();
						$.obAlertNotice(VAR_PKTPOP_HOSTALREADYSEL);
						$('select[name = "filter5"] option:eq(0)').attr("selected", "selected");
						var pArea = $('.divSelect5 .valueArea').filter(':last');
						pArea.empty();
						var html = "";						
						html +='<input type="text"  name="srcIp" class="inputText width129 margin_t2b2">';							
						pArea.html(html);
						return;
					}	
					else
					{	
						var pArea = $('.divSelect5 .valueArea').filter(':last');
						pArea.empty();
						var html = "";						
						html +='<input type="text"  name="host" class="inputText width129 margin_t2b2">';							
						pArea.html(html);
					}
				}
				else if(selectedVal == "portVal 6")//port
				{
					e.stopImmediatePropagation();
					var nameVal = document.getElementsByName('port');
					if(nameVal.length>0)
					{
						e.stopImmediatePropagation();
						$.obAlertNotice(VAR_PKTPOP_PORTALREADYSEL);
						$('select[name = "filter5"] option:eq(0)').attr("selected", "selected");
						var pArea = $('.divSelect5 .valueArea').filter(':last');
						pArea.empty();
						var html = "";						
						html +='<input type="text"  name="srcIp" class="inputText width129 margin_t2b2">';							
						pArea.html(html);
						return;
					}	
					else
					{	
						var pArea = $('.divSelect5 .valueArea').filter(':last');
						pArea.empty();
						var html = "";						
						html +='<input type="text"  name="port" class="inputText width129 margin_t2b2">';							
						pArea.html(html);
					}
				}					
			});
			/* 6번째 selectBox 변화시 처리 */			
			$('select[name = "filter6"]').change(function(e)
			{
				e.stopImmediatePropagation();
				var selectedVal = $('select[name = "filter6"]').val();
				if(selectedVal == "srcIpVal 0")//src_ip
				{
					e.stopImmediatePropagation();
					var nameVal = document.getElementsByName('srcIp');
					if(nameVal.length>0)
					{
						$.obAlertNotice(VAR_PKTPOP_SRCIPALREADYSEL);
						$('select[name = "filter6"] option:eq(0)').attr("selected", "selected");
						var pArea = $('.divSelect6 .valueArea').filter(':last');
						pArea.empty();
						var html = "";						
						html +='<input type="text"  name="srcIp" class="inputText width129 margin_t2b2">';							
						pArea.html(html);
						return;
					}	
					else
					{
						var pArea = $('.divSelect6 .valueArea').filter(':last');
						pArea.empty();
						var html = "";						
						html +='<input type="text"  name="srcIp" class="inputText width129 margin_t2b2">';							
						pArea.html(html);
					}	
				}
				else if(selectedVal == "dstIpVal 1")//dst_Ip
				{
					e.stopImmediatePropagation();
					var nameVal = document.getElementsByName('dstIp');
					if(nameVal.length>0)
					{
						$.obAlertNotice(VAR_PKTPOP_DSTIPALREAYSEL);
						$('select[name = "filter6"] option:eq(0)').attr("selected", "selected");
						var pArea = $('.divSelect6 .valueArea').filter(':last');
						pArea.empty();
						var html = "";						
						html +='<input type="text"  name="srcIp" class="inputText width129 margin_t2b2">';							
						pArea.html(html);
						return;
					}	
					else
					{
						var pArea = $('.divSelect6 .valueArea').filter(':last');
						pArea.empty();
						var html = "";						
						html +='<input type="text"  name="dstIp" class="inputText width129 margin_t2b2">';							
						pArea.html(html);
					}	
					
				}	
				else if(selectedVal == "srcPortVal 2")//src_port
				{
					e.stopImmediatePropagation();
					var nameVal = document.getElementsByName('srcPort');
					if(nameVal.length>0)
					{
						$.obAlertNotice(VAR_PKTPOP_SRCPORTALREADYSEL);
						$('select[name = "filter6"] option:eq(0)').attr("selected", "selected");
						var pArea = $('.divSelect6 .valueArea').filter(':last');
						pArea.empty();
						var html = "";						
						html +='<input type="text"  name="srcIp" class="inputText width129 margin_t2b2">';							
						pArea.html(html);
						return;
					}	
					else
					{
						var pArea = $('.divSelect6 .valueArea').filter(':last');
						pArea.empty();
						var html = "";						
						html +='<input type="text"  name="srcPort" class="inputText width129 margin_t2b2">';							
						pArea.html(html);
					}
				}
				else if(selectedVal == "dstPortVal 3")//dst_port
				{
					e.stopImmediatePropagation();
					var nameVal = document.getElementsByName('dstPort');
					if(nameVal.length>0)
					{
						e.stopImmediatePropagation();
						$.obAlertNotice(VAR_PKTPOP_DSTPORTALREADYSEL);
						$('select[name = "filter6"] option:eq(0)').attr("selected", "selected");
						var pArea = $('.divSelect6 .valueArea').filter(':last');
						pArea.empty();
						var html = "";						
						html +='<input type="text"  name="srcIp" class="inputText width129 margin_t2b2">';							
						pArea.html(html);
						return;
					}	
					else
					{
						var pArea = $('.divSelect6 .valueArea').filter(':last');
						pArea.empty();
						var html = "";						
						html +='<input type="text"  name="dstPort" class="inputText width129 margin_t2b2">';							
						pArea.html(html);
					}
				}
				else if(selectedVal == "protocolVal 4")//protocol
				{
					e.stopImmediatePropagation();
					var nameVal = document.getElementsByName('protocol');
					if(nameVal.length>0)
					{
						e.stopImmediatePropagation();
						$.obAlertNotice(VAR_PKTPOP_PROTOCOLALREADYSEL);
						$('select[name = "filter6"] option:eq(0)').attr("selected", "selected");
						var pArea = $('.divSelect6 .valueArea').filter(':last');
						pArea.empty();
						var html = "";						
						html +='<input type="text"  name="srcIp" class="inputText width129 margin_t2b2">';							
						pArea.html(html);
						return;
					}	
					else
					{	
						var pArea = $('.divSelect6 .valueArea').filter(':last');
						pArea.empty();
						var html = "";						
						html +='<select name="protocol" class="inputSelect width134">';
						html +='<option value="TCP">' + VAR_COMMON_TCP + '</option>';
						html +='<option value="UDP">' + VAR_COMMON_UDP + '</option>';
						html +='<option value="ICMP">' + VAR_COMMON_ICMP + '</option>';
						html +='</select>';
						
						pArea.html(html);
					}
				}
				else if(selectedVal == "hostVal 5")//host
				{
					e.stopImmediatePropagation();
					var nameVal = document.getElementsByName('host');
					if(nameVal.length>0)
					{
						e.stopImmediatePropagation();
						$.obAlertNotice(VAR_PKTPOP_HOSTALREADYSEL);
						$('select[name = "filter6"] option:eq(0)').attr("selected", "selected");
						var pArea = $('.divSelect6 .valueArea').filter(':last');
						pArea.empty();
						var html = "";						
						html +='<input type="text"  name="srcIp" class="inputText width129 margin_t2b2">';							
						pArea.html(html);
						return;
					}	
					else
					{	
						var pArea = $('.divSelect6 .valueArea').filter(':last');
						pArea.empty();
						var html = "";						
						html +='<input type="text"  name="host" class="inputText width129 margin_t2b2">';							
						pArea.html(html);
					}
				}
				else if(selectedVal == "portVal 6")//port
				{
					e.stopImmediatePropagation();
					var nameVal = document.getElementsByName('port');
					if(nameVal.length>0)
					{
						e.stopImmediatePropagation();
						$.obAlertNotice(VAR_PKTPOP_PORTALREADYSEL);
						$('select[name = "filter6"] option:eq(0)').attr("selected", "selected");
						var pArea = $('.divSelect6 .valueArea').filter(':last');
						pArea.empty();
						var html = "";						
						html +='<input type="text"  name="srcIp" class="inputText width129 margin_t2b2">';							
						pArea.html(html);
						return;
					}	
					else
					{	
						var pArea = $('.divSelect6 .valueArea').filter(':last');
						pArea.empty();
						var html = "";						
						html +='<input type="text"  name="port" class="inputText width129 margin_t2b2">';							
						pArea.html(html);
					}
				}					
			});
		}		
	},		
	
	validationFilterChk : function()
	{
//		var selectedVal = $('#filter1 option:selected').val();
		var selectedVal = $('.cloneDiv #filter1 option:selected').val(); // from GS. #4012-1 #2: 14.07.29 sw.jung: 필터 유효성 오류 수정
		var array_data = selectedVal.split(" ");
		var selectVal = array_data[1];
		
		
//		var filterChk1 = $('#filter1 option:selected').val();
		var filterChk1 = selectVal;
		
		var filter2 = document.getElementsByName("filter2").length;
		var filter3 = document.getElementsByName("filter3").length;
		var filter4 = document.getElementsByName("filter4").length;
		var filter5 = document.getElementsByName("filter5").length;
		var filter6 = document.getElementsByName("filter6").length;
		
		var idx = 0;
		var arr = new Array();
		arr[idx] = filterChk1;
		idx++;
		
		if (filter2 > 0)
		{
			var selectedVal = $('#filter2 option:selected').val();
			var array_data = selectedVal.split(" ");
			var selectVal = array_data[1];
			
//			var val = $('#filter2 option:selected').val();
			var val = selectVal;
			arr[idx] = val;
			idx++;
		}
		if (filter3 > 0)
		{
			var selectedVal = $('#filter3 option:selected').val();
			var array_data = selectedVal.split(" ");
			var selectVal = array_data[1];
			
//			var val = $('#filter2 option:selected').val();
			var val = selectVal;
			arr[idx] = val;
			idx++;
		}
		if (filter4 > 0)
		{
			var selectedVal = $('#filter4 option:selected').val();
			var array_data = selectedVal.split(" ");
			var selectVal = array_data[1];
			
//			var val = $('#filter2 option:selected').val();
			var val = selectVal;
			arr[idx] = val;
			idx++;
		}
		if (filter5 > 0)
		{
			var selectedVal = $('#filter5 option:selected').val();
			var array_data = selectedVal.split(" ");
			var selectVal = array_data[1];
			
//			var val = $('#filter2 option:selected').val();
			var val = selectVal;
			arr[idx] = val;
			idx++;
		}
		if (filter6 > 0)
		{
			var selectedVal = $('#filter6 option:selected').val();
			var array_data = selectedVal.split(" ");
			var selectVal = array_data[1];
			
//			var val = $('#filter2 option:selected').val();
			var val = selectVal;
			arr[idx] = val;
			idx++;
		}
		
		for (var i=0; i<idx; i++) {
			var a = arr[i];
			for (var j=0; j<idx; j++) {
				if (j != i) {
					var b = arr[j];
					
					if (a == b) {
						$.obAlertNotice(VAR_PKTPOP_EXISTSAMESEARCHCOND);
						return false;
					}
					
				}
			}
		}
		return true;
	},
	
	validateStartDump : function()
	{
		var optionMaxPktValue = $('.cloneDiv input[name="optionMaxPkt"]').val();
		var optionMaxSizeValue = $('.cloneDiv input[name="optionMaxSize"]').val(); 
		var optionMaxTimeValue = $('.cloneDiv input[name="optionMaxTime"]').val();
		
//		if (FlowitUtil.checkNum(optionMaxPktValue) || FlowitUtil.checkNum(optionMaxPktValue) || FlowitUtil.checkNum(optionMaxPktValue))
//		{
//			alert(VAR_PKTPOP_ONLYNUMINPUT);
//			return false;
//		}
		
//		var maxPktChk = $('.maxPktChk').is(':checked');
		var maxPktChk = $('.cloneDiv .maxPktChk').is(':checked'); // from GS. #4012-1 #2 14.07.29 sw.jung: 체크박스 검사 회피 오류 수정
		if (maxPktChk && optionMaxPktValue == "") // 체크박스 검사 선행
		{
			$.obAlertNotice(VAR_PKTPOP_COUNTINPUT);
			return false;
		}
		
		if (adcSetting.getAdc().type == "F5" && adcSetting.getSelectIndex() == 2)
		{
			if (maxPktChk && !getValidateNumberRange(optionMaxPktValue, 1, 500000)) // 체크박스 검사 선행
			{
				$.obAlertNotice(VAR_PKTPOP_MAXNOTMORE_F5);				// Only numerical values are acceptable. 
				return false;
			}				
		}
		else
		{
			if (maxPktChk && !getValidateNumberRange(optionMaxPktValue, 1, 10000)) // 체크박스 검사 선행
			{
				$.obAlertNotice(VAR_PKTPOP_MAXNOTMORE_ALTEON);				// Only numerical values are acceptable. 
				return false;
			}
		}			
		
//		if (optionMaxPktValue > 10000)
//		{
//			alert(VAR_PKTPOP_MAXNOTMORE);			// The max number of packets is 10,000.
//			return false;
//		}
//		if (optionMaxPktValue < 1)
//		{
//			alert(VAR_PKTPOP_GREATERTHANZERO);		// The number of packets must be greater than zero.
//			return false;
//		}
		
		var maxTimeChk = $('.cloneDiv .maxTimeChk').is(':checked');
		if (maxTimeChk == true)
		{
			if (optionMaxTimeValue == "")
			{
				$.obAlertNotice(VAR_PKTPOP_TIMEINPUT);
				return false;
			}
		}
		
//		if (optionMaxTimeValue > 600)
		if (maxTimeChk && !getValidateNumberRange(optionMaxTimeValue, 1, 600)) // 체크박스 검사 선행
		{
			$.obAlertNotice(VAR_PKTPOP_TIMENOTEXCEEDMAX);		// The maximum time is 600 seconds.
			return false;
		}
		
		var maxSizeChk = $('.cloneDiv .pktSizeChk').is(':checked');
		if (maxSizeChk == true)
		{
			if (optionMaxSizeValue == "")
			{
				$.obAlertNotice(VAR_PKTPOP_CAPACITYINPUT);
				return false;
			}
		}
		
		if (optionMaxSizeValue == 0)
		{
			$.obAlertNotice(VAR_PKTPOP_CAPACITYGREATERTHANZERO);
			return false;
		}
		//if (optionMaxSizeValue > 10000)
		if (maxSizeChk && !getValidateNumberRange(optionMaxSizeValue, 1, 10000)) // 체크박스 검사 선행
		{
			$.obAlertNotice(VAR_PKTPOP_CAOACITYNOTEXCEEDMAX);		// The maximum capacity is 10,000 kbytes.
			return false;
		}
		
		// from GS. #4012-1 #2, #3926-4 #8: 14.07.29 sw.jung obvalidation 활용 개선
//		var fileName = $('.cloneDiv input[name="fileName"]').val().trim(); // 공백제거
//		if (fileName == "")
//		{
//			alert(VAR_PKTPOP_FILENAMEMUSTBEINPUT);		// The file name must be entered.
//			return false;
//		}
//		
//		if (!getValidateStringint(fileName, 1, 64))
//		{
//			alert(VAR_COMMON_SPECIALCHAR);				// You entered some special characters, which are not permissible for the required value.
//			return false;
//		}
		if (!$('.cloneDiv input[name="fileName"]').validate(
			{
				name: $('.cloneDiv input[name="fileName"]').parent().parent().find('li').text(),
				required: true,
				type:"name",
				lengthRange: [1,64]
			}))
		{
			return false;
		}
		
		var sourceIpVal = $('.cloneDiv input[name="srcIp"]').val();
		if(sourceIpVal != "" && sourceIpVal != undefined)
		{
//			if (!FlowitUtil.checkIp($('.cloneDiv input[name="srcIp"]').val())) 
			if (!getValidateIP(sourceIpVal))
			{
				$.obAlertNotice(VAR_PKTPOP_SRCIPFORMAT);
				return false;
			}
		}		
		var dstIpVal = $('.cloneDiv input[name="dstIp"]').val();
		if(dstIpVal != "" && dstIpVal != undefined )
		{
//			if(!FlowitUtil.checkIp($('.cloneDiv input[name="dstIp"]').val()))
			if (!getValidateIP(dstIpVal))
			{
				$.obAlertNotice(VAR_PKTPOP_DSTIPFORMAT);
				return false;
			}
		}
		var hostVal = $('.cloneDiv input[name="host"]').val();
		if(hostVal != "" && hostVal != undefined )
		{
//			if(!FlowitUtil.checkIp($('.cloneDiv input[name="host"]').val())) 
			if (!getValidateIP(hostVal))
			{
				$.obAlertNotice(VAR_PKTPOP_HOSTIPFORMAT);
				return false;
			}
		}
		var srcPortVal = $('.cloneDiv input[name="srcPort"]').val();
		if(srcPortVal != "" && srcPortVal != undefined )
		{
//			if (!FlowitUtil.checkPortNum($('.cloneDiv input[name="srcPort"]').val()))
			if (!getValidateNumberRange(srcPortVal, 1, 65535))
			{
				$.obAlertNotice(VAR_PKTPOP_SRCPORTFORMAT);
				return false;
			}
		}
		
		var dstPortVal = $('.cloneDiv input[name="dstPort"]').val();
		if(dstPortVal != "" && dstPortVal != undefined )
		{
//			if (!FlowitUtil.checkPortNum($('.cloneDiv input[name="dstPort"]').val()))
			if (!getValidateNumberRange(dstPortVal, 1, 65535))
			{
				$.obAlertNotice(VAR_PKTPOP_DSTPORTFORMAT);
				return false;
			}
		}	
	
		var portVal = $('.cloneDiv input[name="port"]').val();
		if(portVal != "" && portVal != undefined )
		{
//			if (FlowitUtil.checkPortNum(portVal)!=true)
			if (!getValidateNumberRange(portVal, 1, 65535))
			{
				$.obAlertNotice(VAR_PKTPOP_PORTFORMAT);
				return false;
			}
		}
		
		return true;
	},
	_checkFilterSelectBoxLength : function()
	{
		with(this)
		{
			if($('.addSelectFilter select').length >=5)
			{	
				var $s = $('.addFilter').filter(':last');
				$s.empty();
				var	 html ='';
			
				html += '<span title=' + '"' + VAR_PKTPOP_ADD + '" ' + 'class="addFilter" href="#">';
				html +='<img src="/imgs/btn' + getImgLang() + '/btn_addfilter_off.gif" /></span>';				
				$s.html(html);
			}
			else
			{
				var $s = $('.addFilter').filter(':last');
				$s.empty();
				var	 html ='';
			
				html += '<span title=' + '"' + VAR_PKTPOP_ADD + '" ' + 'class="addFilter" href="#">';
				html +='<img src="/imgs/btn' + getImgLang() + '/btn_addfilter.gif" /></span>';
				$s.html(html);
			}	
		}
	},
	
	registerPktDump : function(index, selIdx, flag, key, idx)
	{
		with (this)
		{	
			if (flag == 0)	// 중지
			{
				stopPktDump(key, 0);
				$('.progress').removeClass("none");
				$('.condition').addClass("none");
				$('.stopPkt').addClass("none");
				$('.cancelPkt').addClass("none");
				$('.closePkt').removeClass("none");
				$('.closeMainPkt').addClass("none");
			}
			else if (flag == 1) // 취소
			{
				stopPktDump(key, 1);
				$('.progress').addClass("none");
				$('.condition').removeClass("none");
				$('.stopPkt').addClass("none");
				$('.cancelPkt').addClass("none");
				$('.closePkt').removeClass("none");
				$('.closeMainPkt').addClass("none");
			}
			else if (flag == 2)
			{
				$('.condition').addClass("none");
				$('.progress').removeClass("none");
				$('.stopPkt').removeClass("none");
				$('.cancelPkt').removeClass("none");
				$('.closePkt').addClass("none");
				$('.closeMainPkt').addClass("none");
			}
			else if (flag == 3)
			{
				$('.progress').removeClass("none");
			}

			// #3984-4 #1: 14.07.28 sw.jung 패킷 수집화면 표 클릭시 비정상 갱신 일어나는 현상 수정(기능 제거)
			// table의 tr 선택시 
//			$('.pktDmpTable tbody tr').click(function(e)
//			{
//				with (this)
//				{
//					clearAnalysisTimer();
//					clearChartTimer();
//					selectedColumnIndex = $(this).attr("id");			
//					selectpktDmpTableRow(selectedColumnIndex);			
//					displayRealTimeSystemUsage(2, selectedColumnIndex);
//				}
//			});
												
			$('.downloadPktDumpLnk').click(function(e)
			{
				e.preventDefault();
				
				var logKey = $(this).parent().find('.pktDumpIndex').text(); 
				owner._checkDownloadPkDumpDataExist(logKey);
			});			
		}
	},
	// 선택한 ADC Index 를 가지고 일치하는 Row에 컬러를 입히는 함수
	selectpktDmpTableRow : function(selectAdcIndex)
	{	
		$('.pktDmpTable tbody tr').removeClass("vsMonitorRowSelection");
		$('.pktDmpTable tbody tr').each(function(index) {
			if ($(this).attr("id") === selectAdcIndex ) 
			{				
				$(this).addClass("vsMonitorRowSelection");							
			}
		});			
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
	
	refreshPktDumpAnalysisTableList : function(list)
	{
		with (this)
		{
			clearRefreshTimer();
			if (0 != refreshIntervalSeconds)
			{
				refreshTimer = setInterval(function()
				{
					if ($('.pktDmpTable').is(':visible'))
					{
						loadPktDumpStatusListContent(list, 3);
						updatePktStateInfo(list);
					}
					else
					{
						clearRefreshTimer();
					}
				}, refreshIntervalSeconds * 1000);
			}
		}
	},
	
	updatePktStateInfo : function(logList)
	{	
		with (this)
		{
			ajaxManager.runJsonExt({
				url			: "faultAnalysis/loadPktDumpStatusInfoContent.action",
				data		: 
				{
					"logKeyList" : logList
				},
				successFn	: function(data)
				{						
					if (!data.isSuccessful)
					{
						$.obAlertNotice(data.message);
						return;
					}
					updatePktState(data);
				},
				completeFn	: function(data)
				{							
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_COMMON_STATUSEXTRACT, jqXhr);		
				}
			});
		}
	},
	
	updatePktState : function(data)
	{
		with (this)
		{
//			var logKeyObj = [];
			var ProgressRate = "";
			for (var i=0; i<data.pktDumpStatusInfoList.length; i++)
			{
//				logKeyObj.push(data.pktDumpStatusInfoList[i]);
				ProgressRate = data.pktDumpStatusInfoList[i].progressRate;
//				alert("ProgressRate : " + ProgressRate);
//				alert("aaa : " + data.pktDumpStatusInfoList[i].progressRate);
				if (ProgressRate == 100 || ProgressRate == 101 || ProgressRate == 102)
				{
					analysisPopupAliveFlag = false;
					clearAnalysisTimer();
					clearChartTimer();
//					clearRefreshTimer();
					
					$('.stopPkt').addClass("none");
					$('.cancelPkt').addClass("none");
					$('.closePkt').removeClass("none");
					$('.closeMainPkt').addClass("none");
					
					return;
				}
				else
				{
					analysisPopupAliveFlag = false;
					$('.stopPkt').removeClass("none");
					$('.cancelPkt').removeClass("none");
					$('.closePkt').addClass("none");
					$('.closeMainPkt').addClass("none");
					return;
				}
			}			
		}
	},
});