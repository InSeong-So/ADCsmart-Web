var ServiceMonitor = Class.create({
	initialize : function()
	{
		var fn = this;
		this.adc;
		this.categoryIndex;
		this.searchedOption = 
		{
			"searchKey" : undefined,
			"fromPeriod" : undefined,
			"toPeriod" : undefined
		};
		this.searchedKey = undefined;
		this.orderDir = 2;  // 기본 default는 asc
		this.orderType = 8;
//		this.pageNavi = new PageNavigator();
		this.pageNavi = new PageNavigator({ rowCountPerPage : 10 });
		this.pageNavi.onChange(function(fromRow, toRow, orderType, orderDir)
		{
			fn.loadServiceMonitoringTableInListContent(fn.searchedKey, fromRow, toRow, orderType, orderDir, fn.changeFilter(), fn.selectedCol());
//			fn.loadRefreshSvcMonListContent(fn.searchedKey, fromRow, toRow, fn.changeFilter(), fn.selectedCol());	
		});
		this.target = {};
		this.columnSet = undefined;
		this.selectedSet = undefined;
		this.setFlag = undefined;
		this.searchFlag=false;
		
		this.searchStartTime = undefined;
		this.searchEndTime = undefined;
				
		this.selectedVsIndexList = undefined; 		// selected vs List
		this.selectedVsNameList = undefined; 		// selected vs List
		this.intervalMonitor = undefined;
		this.selectedVsIndexOldList = undefined;
		
	},
	setTarget : function()
	{
		var grpIndex = adcSetting.getGroupIndex();
		var adcIndex = adcSetting.adc.index;
		if(grpIndex === undefined && adcIndex === undefined)
		{
			this.target.level = 0;
			this.target.index = undefined;
		}
		else if(grpIndex == 0)
		{
			this.target.level = grpIndex;
			this.target.index = undefined;
		}
		else if(grpIndex === undefined && adcIndex != null)
		{
			this.target.level = 2;
			this.target.index = adcIndex;
		}
		else
		{
			this.target.level = 1;
			this.target.index = grpIndex;
		}
	},
	onAdcChange : function()
	{
		with(this)
		{
			this.columnSet = undefined;
			this.selectedSet = undefined;
			this.setFlag = undefined;
			loadListContent();
		}
	},	
	loadListContent : function(searchKey, orderDir, orderType)
	{
		with(this)
		{
			setTarget();
			var rowTotal = 0;
			ajaxManager.runJsonExt({
				url : "monitor/retrieveServiceMonTotal.action", 
				data :
				{
					"adcScope.level" : target.level,
					"adcScope.index" : target.index,
					"searchKey" : searchKey
				},
				successFn : function(data)
				{
					if(!data.isSuccessful)
					{
						$.obAlertNotice(data.message);
						rowTotal = 100;
					}
					else
					{
						rowTotal = data.rowTotal;
					}
					
					pageNavi.updateRowTotal(rowTotal, orderType);
					loadServiceMonitorList(searchKey);
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_SVCPERFOM_LAOD, jqXhr);
				}
			});
		}
	},
	
	loadServiceMonitorList : function(searchKey, fromRow, toRow)
	{	
		with(this)
		{		
			ajaxManager.runHtmlExt({
				url : "monitor/loadServiceMonitorList.action",
				data : 
				{
					"adcScope.level" 	: target.level,					
					"adcScope.index"	: target.index,
					"searchKey" 		: searchKey == null ? undefined : searchKey,
					"fromRow"			: fromRow === undefined ? pageNavi.getFirstRowOfCurrentPage() : fromRow,
					"toRow"				: toRow === undefined ? pageNavi.getLastRowOfCurrentPage() : toRow,
					"orderDir"			: this.orderDir,
					"orderType"			: this.orderType
				},
				target : "#wrap .contents",				
				successFn : function(params)
				{
					header.setActiveMenu("MonitorServicePerfomance");
					searchedKey = searchKey;
					
					if(serviceMonitor.columnSet)
					{
						$.each(serviceMonitor.columnSet, function(index, value)
						{
							showHidenCol($('#serviceListTable'), index, value);
						});
					}
					
					if(serviceMonitor.selectedSet)
					{
						var isChecked;
						$.each(serviceMonitor.selectedSet, function(groupIndex, groupSet) {
							$.each(groupSet, function(index, value) {
								isChecked = $.parseJSON(value.split('\|')[3]);
								if(isChecked)
									$('[data-group=' + groupIndex + '] input[name=selChk]:eq(' + index + ')').attr('checked', 'checked');
								else
									$('[data-group=' + groupIndex + '] input[name=selChk]:eq(' + index + ')').removeAttr('checked');
							});
						});
						changeFilter();
						
						refreshSelectFilterList();
						
						if ($('.sel_filter:not(.none)').length < 1)
						{
							$('.selectedOptionFilter').addClass('none');
						}
						else
						{
							$('.selectedOptionFilter').removeClass('none');
						}
						
						var isFlag= serviceMonitor.setFlag;
						if (isFlag == "false")
						{
							$('#filterAdd').attr("isFlag", "true");
							$('.optionFilter').removeClass('none');
							$('#filterAdd').attr("class", "filter_option_btn_open");
						}
						else
						{
							$('#filterAdd').attr("isFlag", "false");
							$('.optionFilter').addClass('none');
							$('#filterAdd').attr("class", "filter_option_btn_close");
						}
						
						loadRefreshSvcMonListContent(searchKey, orderDir, orderType);
					}
					
					$('#serviceListTable tbody tr:not(.none)').eq(0).find('.vsColChk').attr("checked", true);
					$('#serviceListTable tbody tr:not(.none)').eq(1).find('.vsColChk').attr("checked", true);
					$('#serviceListTable tbody tr:not(.none)').eq(2).find('.vsColChk').attr("checked", true);
					$('#serviceListTable tbody tr:not(.none)').eq(3).find('.vsColChk').attr("checked", true);
					$('#serviceListTable tbody tr:not(.none)').eq(4).find('.vsColChk').attr("checked", true);
					
//					$('#serviceListTable tbody tr .adcType').find('span').each(function(index)
//					{
//						if($(this).text().trim() == 1 || $(this).text().trim() == 2)
//						{}
//						else
//							$(this).parent().parent().find('.vsColChk').attr("disabled", "disabled");
//							
//					});
					
					
					var vsIndexList = new Array();
					var vsNameList = new Array();
//					$('#serviceListTable tbody tr:not(.none)').find('.vsColChk').each(function(index)
					$('#serviceListTable tbody tr:not(.none) input[type=checkbox]:checked').each(function(index)
					{						
						if ($(this).is(':checked'))
						{
//							array_data.push($('#serviceListTable tbody tr:not(.none)').eq(index).find('.vsColChk').val());
							vsIndexList.push($(this).val().trim());
//							vsPortList.push($(this).parent().parent().find('.port').text().trim());
						}
						selectedVsIndexList = vsIndexList;
					});
					
					$('#serviceListTable tbody tr:not(.none)').find('.name').each(function(index)
//					$('#serviceListTable tbody tr:not(.none) input[type=checkbox]:checked').each(function(index)
					{						
						if ($(this).parent().find('.vsColChk').is(':checked'))
						{
							var vsName = $(this).text().trim();
							if(vsName != "")							
								vsNameList.push(vsName);
							else
								vsNameList.push($(this).parent().find('.vsIp').text().trim());
//								vsNameList.push($(this).text().trim());
						}
						selectedVsNameList = vsNameList;
					});
										
					if($('input[name="bpsValChk"]').is(':checked') == true)		
					{
//						if (adcSetting.getAdcStatus() != "available") 
//						{						
//							if ($('.serviceMonListTable').size() > 0 )
//							{
//								loadBpsConnHistoryInfo(vsIndex, port, undefined, vsName, bpsVal, preVal);
//								
//								$('.searchNotMsg').addClass("none");
//								$('.dataNotExistMsg').addClass("none");
//							}
//							else
//							{	
//								$('.contents_area .nulldata').removeClass("none");
//								if(langCode=="ko_KR")
//								{
//									$('.contents_area .nulldata').css('background-image', "url(imgs/monitoring/servicePerfomance_null.gif)");
//								}
//								else
//								{
//									$('.contents_area .nulldata').css('background-image', "url(imgs/monitoring_eng/servicePerfomance_null.gif)");
//								}
//								$('.contents_area .successdata').addClass("none");
//							}
							
//							loadBpsConnHistoryInfo(vs_index, port, undefined, vsName, bpsVal, preVal);
//							loadBpsConnHistoryInfo(vsIndexList, vsPortList, undefined, undefined, 2, -1);
							loadBpsConnHistoryInfo(vsIndexList, vsNameList, undefined);
//							loadBpsMaxAvgHistoryInfo(vsIndexList, vsNameList, undefined);	
//							loadBpsConnHistoryInfo('5_/Common/any', 0, undefined, '/Common/any', 2, -1);
//						}
//						else
//						{	
//							if ($('.serviceMonListTable').size() < 0 )
//							{
//								$('.contents_area .nulldata').removeClass("none");
//								if(langCode=="ko_KR")
//								{
//									$('.contents_area .nulldata').css('background-image', "url(imgs/monitoring/servicePerfomance_null.gif)");
//								}
//								else
//								{
//									$('.contents_area .nulldata').css('background-image', "url(imgs/monitoring_eng/servicePerfomance_null.gif)");
//								}
//								$('.contents_area .successdata').addClass("none");
//							}
//							loadBpsConnHistoryInfo(vsIndex, port, undefined, vsName, bpsVal, preVal);
//						}
					}
					/*if($('input[name="concurrSessionValChk"]').is(':checked') == true)							
					{
						if (adcSetting.getAdcStatus() != "available") 
						{						
//							if ($('.serviceMonListTable').size() > 0 )
//							{								
//								connVal = 0;
//								loadConnHistoryInfo(vsIndex, port, undefined, vsName, bpsVal, preVal, connVal);
//								
//								$('.searchNotMsg').addClass("none");
//								$('.dataNotExistMsg').addClass("none");									
//							}
//							else
//							{						
//								$('.contents_area .nulldata').removeClass("none");
//								if(langCode=="ko_KR")
//								{
//									$('.contents_area .nulldata').css('background-image', "url(imgs/monitoring/servicePerfomance_null.gif)");
//								}
//								else
//								{
//									$('.contents_area .nulldata').css('background-image', "url(imgs/monitoring_eng/servicePerfomance_null.gif)");
//								}
//								$('.contents_area .successdata').addClass("none");
//							}
							
//							loadConnHistoryInfo(vsIndex, port, undefined, vsName, bpsVal, preVal, connVal);
//							loadConnHistoryInfo('5_/Common/any', 0, undefined, '/Common/any', 2, -1, 0);
							loadConnHistoryInfo(vsIndexList, undefined);
						}
//						else
//						{
//							if ($('.serviceMonListTable').size() < 0 )
//							{
//								$('.contents_area .nulldata').removeClass("none");
//								if(langCode=="ko_KR")
//								{
//									$('.contents_area .nulldata').css('background-image', "url(imgs/monitoring/servicePerfomance_null.gif)");
//								}
//								else
//								{
//									$('.contents_area .nulldata').css('background-image', "url(imgs/monitoring_eng/servicePerfomance_null.gif)");
//								}
//								$('.contents_area .successdata').addClass("none");
//							}
//							connVal = 0;
//							loadConnHistoryInfo(vsIndex, port, undefined, vsName, bpsVal, preVal, connVal);
//						}
					}*/
					
					noticePageInfo();
					registerEvents();
					msgViewEvents();
					refreshSelectVsList(selectedVsIndexList, selectedVsNameList);
				},
				completeFn : function()
				{
					pageNavi.refresh();
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_SVCPERFOM_LAOD, jqXhr);
				}
			});
		}
	},
	
	loadRefreshSvcMonListContent : function(searchKey, orderDir, orderType)
	{
		with(this)
		{
			var filterData = changeFilter();
			var selcolData = selectedCol();
			var rowTotal = 0;
			
			if(!validateDaterefresh())
			{
				return false;
			}	
			
			ajaxManager.runJsonExt({
				url : "monitor/retrieveServiceMonTotal.action", 
				data :
				{
					"adcScope.level" 	: target.level,					
					"adcScope.index"	: target.index,
					"searchKey" 		: searchKey == null ? undefined : searchKey,
					"orderDir"			: this.orderDir,
					"orderType"			: this.orderType,
					"selectedVal"		: JSON.stringify(filterData),	//filter_data
					"selectedCol"		: JSON.stringify(selcolData)	//column_isSelect_data
				},
				successFn : function(data)
				{
					if(!data.isSuccessful)
					{
						$.obAlertNotice(data.message);
						rowTotal = 100;
					}
					else
					{
						rowTotal = data.rowTotal;
					}
					
					pageNavi.updateRowTotal(rowTotal, orderType);
					loadServiceMonitoringTableInListContent(searchKey, undefined, undefined, undefined, undefined, filterData, selcolData);					
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_SVCPERFOM_LAOD, jqXhr);
				}
			});
		}
	},
	
	loadServiceMonitoringTableInListContent : function(searchKey, fromRow, toRow, orderDir, orderType, filterData, selcolData)
	{
		with(this)
		{
			ajaxManager.runJsonExt({
				url : "monitor/loadServiceMonitoringTableInListContent.action",
				data : 
				{
					"adcScope.level" 	: target.level,					
					"adcScope.index"	: target.index,
					"searchKey" 		: searchKey == null ? undefined : searchKey,
					"fromRow"			: fromRow === undefined ? pageNavi.getFirstRowOfCurrentPage() : fromRow,
					"toRow"				: toRow === undefined ? pageNavi.getLastRowOfCurrentPage() : toRow,
					"orderDir"			: this.orderDir,
					"orderType"			: this.orderType,
					"selectedVal"		: JSON.stringify(filterData),	//filter_data
					"selectedCol"		: JSON.stringify(selcolData)	//column_isSelect_data
				},
				successFn : function(data)
				{
//					console.log(data);
					noticePageInfo();
					searchedKey = searchKey;
					
					refreshHeaderSort($('#serviceListTable'), orderType, orderDir); 
					pagePrevNextChange(data);
					msgViewEvents();
					
//					$('#serviceListTable tbody tr:not(.none)').eq(0).find('.vsColChk').attr("checked", true);
//					$('#serviceListTable tbody tr:not(.none)').eq(1).find('.vsColChk').attr("checked", true);
//					$('#serviceListTable tbody tr:not(.none)').eq(2).find('.vsColChk').attr("checked", true);
//					$('#serviceListTable tbody tr:not(.none)').eq(3).find('.vsColChk').attr("checked", true);
//					$('#serviceListTable tbody tr:not(.none)').eq(4).find('.vsColChk').attr("checked", true);
					
//					var vsIndexList = new Array();
//					var vsIndexOldList = new Array();
//					var vsNameList = new Array();
					
//					var vsIndexList = selectedVsIndexList;
					
//					$('#serviceListTable tbody tr .adcType').find('span').each(function(index)
//					{
//						if($(this).text().trim() == 1 || $(this).text().trim() == 2)
//						{}
//						else
//							$(this).parent().parent().find('.vsColChk').attr("disabled", "disabled");
//							
//					});
//TODO					
					selectedVsIndexOldList = selectedVsIndexList;
					vsNameList = selectedVsNameList;
					
					
					if ($('.sel_filter:not(.none)').data('parent-id') != null && selectedVsIndexList != undefined)		
					{
						$('#serviceListTable tbody tr:not(.none)').find('.vsColChk').each(function(index)
						{							
							if($(this).val() == selectedVsIndexList[index])
							{
								$(this).attr('checked', 'checked');
							}
							else
							{
								$(this).removeAttr('checked');
								if (selectedVsIndexList.indexOf($(this).val()) != -1)
								{
									var index = selectedVsIndexList.indexOf($(this).val());
									selectedVsIndexList.splice(index, 1);
								}
							}							
						});
					}
					else
					{			
						if(selectedVsIndexList != undefined)
						{
							for(var i=0; i < selectedVsIndexList.length; i++)
							{
								$('#serviceListTable tbody tr:not(.none)').find('.vsColChk').each(function(index)
								{
									if($(this).val() == selectedVsIndexList[i])
									{
										$(this).attr('checked', 'checked');
									}
		//							else
		//							{
		//								$(this).removeAttr('checked');
		//							}							
								});
							}
							selectedVsIndexOldList = selectedVsIndexList;
						}
					}
					
//					console.log("selectedVsIndexList : ", selectedVsIndexList);
//					console.log("selectedVsIndexOldList : " , selectedVsIndexOldList);
//					console.log("vsIndexList : ", vsIndexList);
					
					refreshSelectVsList(selectedVsIndexList, selectedVsNameList);
					if($('input[name="bpsValChk"]').is(':checked') == true)		
					{
//						var vsIndexList = new Array();
//						var vsNameList = new Array();
//						
//						$('#serviceListTable tbody tr:not(.none) input[type=checkbox]:checked').each(function(index)
//						{						
//							if ($(this).is(':checked'))
//							{
//								vsIndexList.push($(this).val().trim());
//							}
//						});
//						
//						$('#serviceListTable tbody tr:not(.none)').find('.name').each(function(index)
//						{						
//							if ($(this).parent().find('.vsColChk').is(':checked'))
//							{
//								vsNameList.push($(this).text().trim());
//							}
//						});
						
						//console.log(selectedVsIndexList);
						//console.log(selectedVsNameList);
						if(selectedVsIndexOldList == selectedVsIndexList)
						{}	
						else
						{
							loadBpsConnHistoryInfo(selectedVsIndexList, selectedVsNameList, undefined);
						}
					}
							
				},
//				completeFn : function()
//				{
//					pageNavi.refresh();
//				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_SVCPERFOM_LAOD, jqXhr);
				}
			});
		}
	},
	
	pagePrevNextChange : function(data)
	{
		var rowTemplate = $('#serviceListTable tbody .ContentsLine1.none').clone().removeClass('none');
		//var divideLineTemplate = '<tr class="DivideLine"><td colspan="17"></td></tr>';
		//var endLineTemplate = '<tr class="EndLine"><td colspan="17"></td></tr>';
//		rowTemplate.find('td').text('');
		
		$('#serviceListTable tbody tr:not(.none)').remove();
		//console.log(rowTemplate, data);
		var row;
//		row = rowTemplate.clone();
		$.each(data.montotalSvc.serviceList, function(index, rowData)
		{
			row = rowTemplate.clone();
			$.each(rowData, function(key, value)
			{
				var html = "";
				if(key == "index")
				{
					html += ('<input class="vsColChk" type="checkbox" value="' + rowData.index + '">');
					row.find('td.' + key).empty().append(html);
				}
				else if(key == "status")
				{
					if(value == -1)
					{
						html += ('<img src="imgs/icon/icon_yellowDot.png" />');
					}
					else if(value == 1)
					{
						html += ('<img src="imgs/icon/icon_vs_conn.png" alt="available" />');
					}
					else if(value == 2)
					{
						html += ('<img src="imgs/icon/icon_vs_disconn.png" alt="unavailable" />');
					}
					else if(value == 0)
					{
						html += ('<img src="imgs/icon/icon_vs_disabled.png" alt="disable" />');
					}
					else
					{
						html += ('<img src="imgs/icon/icon_2d_1.png" />');
					}
						
					row.find('td.' + key).empty().append(html);	
				}
				else if(key == "ip")
				{
					html += '<a href="javascript:;" class="serviceMonitoringLnk"><span style="display:none;">' + rowData.adcIndex + '</span><span style="display:none;">2</span><span class="vsIp">' + value + '</span></a>';
					row.find('td.' + key).empty().append(html);
				}
				else if(key == "name")
				{
//					html += '<td class="align_left_P10 textOver name" title="${(theSvcList.name)!''}" data-colidx="1">${(theSvcList.name)!''}</td>'
					html += value;
//					row.find('td.' + key).empty().append(html);
					row.find('td.' + key).empty().attr("title", value).append(html);
				}
//				else if(key == "name" || key == "ip" || key == "member")
//				{
//					html += '<a href="javascript:;" class="serviceMapLnk"><span style="display:none;">' + rowData.adcIndex + '</span>' + value + '</a>';
//					row.find('td.' + key).empty().append(html);
//				}
//				else if(key == "backup" || key == "noticeGroup")
//				{
//					if(rowData.adcType == 2)
//					{
//						if(value == 1)
//							html += '있음';
//						else
//							html += '없음';
//					}		
//					else
//					{
//						html += '-';
//					}
//					row.find('td.' + key).empty().append(html);
//				}
				else if(key == "noticeGroup")
				{
					if(value != null)
					{
						if(value == 1)
							html += VAR_SVCMONITOR_YES;
						else
							html += VAR_sVCMONITOR_NOT;
					}		
					else
					{
						html += '-';
					}
					row.find('td.' + key).empty().append(html);
				}
				else if(key == "bpsIn" || key == "bpsOut" || key == "bpsTotal" || key == "concurrentSession" || key == "member")
				{	
					if(value == -1)
					{
						html += '-';
					}
					else
					{
						if(key == "bpsIn")
						{
							html += rowData.bpsInUnit;
						}
						else if (key == "bpsOut")
						{
							html += rowData.bpsOutUnit;
						}
						else if (key == "bpsTotal")
						{
							html += rowData.bpsTotalUnit;
						}
						else if (key == "concurrentSession")
						{
							html += rowData.concurrentSessionUnit;
						}
						else if (key == "member")
						{
							html += rowData.member;
						}
					}
					row.find('td.' + key).empty().append(html);
				}
//				else if(key == "throughput")
//				{	
//					if(value == -1)
//					{
//						html += '-';
//					}
//					else
//					{
//						html += '<a href="javascript:;" class="serviceMonitoringLnk"><span style="display:none;">' + rowData.adcIndex + '</span><span style="display:none;">2</span>' + rowData.throughputUnit + '</a>';
//					}
//					row.find('td.' + key).empty().append(html);
//				}
//				else if(key == "concurrentSession")
//				{					
//					html += '<a href="javascript:;" class="serviceMonitoringLnk"><span style="display:none;">' + rowData.adcIndex + '</span><span style="display:none;">2</span>' + rowData.concurrentSessionUnit + '</a>';
//					row.find('td.' + key).empty().append(html);
//				}
				else if(key == "adcName")// || key == "adcIp")
				{	
					html += '<a href="javascript:;" class="adcMonitoringLnk"><span style="display:none;">' + rowData.adcIndex + '</span>' + value + '</a>';
					row.find('td.' + key).empty().attr("title", value).append(html);
				}
				else if(key == "adcType")
				{
					html += '<span style="display:none;">' + value + '</span>';
					if(rowData.adcType == 1)
					{						
						html += '<img src="imgs/icon/adc/icon_f5_s.png" alt="f5" />';
					}
					else if(rowData.adcType == 2)
					{
						html += '<img src="imgs/icon/adc/icon_alteon_s.png" alt="alteon" />';
					}
					else if(rowData.adcType == 3 || rowData.adcType == 4)
					{
						html += '<img src="imgs/icon/adc/icon_piolink_s.png" alt="PiolinkUnknown" />';
					}
					else
					{						
					}
					row.find('td.' + key).empty().append(html);	
				}
				else if(key == "groupName" || key == "loadbalancing" || key == "healthCheck")
				{
					if(value != null)
					{	
//						html += '<a href="javascript:;" class="groupLnk"><span style="display:none;">' + rowData.groupIndex + '</span>' + value + '</a>';
						html += value;
					}
					else
					{
						html += '-';
					}
					row.find('td.' + key).empty().append(html);
				}
				else if(key == "persistence")
				{
					if(rowData.adcType == 1)
					{		
						if(value != null)
						{
							html += value;
						}
						else
						{
							html += '-';
						}
					}
					else
					{
						html += '-';
					}
					row.find('td.' + key).empty().append(html);
				}
//				else if(key == "slbConfig24Hour")
//				{
//					html += '<a href="javascript:;" class="slbHistoryLnk"><span style="display:none;">' + rowData.adcIndex + '</span>' + value + '</a>';
//					row.find('td.' + key).empty().append(html);
//				}
				else if(key == "updateTime")
				{
					if(value != null)
					{
						var configTime = value.split("T");
						html += configTime[0] + ' ' + configTime[1];
					}
					else
					{
						html += '-';
					}
					row.find('td.' + key).empty().append(html);
				}
				else
				{
					row.find('td.' + key).text(value);
				}
			});
			$('#serviceListTable tbody:first').append(row);
			
			//$('#serviceListTable tbody:first').append(divideLineTemplate);
		});
		
		//$('#serviceListTable tbody').append(endLineTemplate);		
	},
	
	// Selected Filter 
	refreshSelectFilterList : function () 
	{
		var filterBoard = $('.Filter_Board');
		var selectFilterList = $('.selectFilterList'); // 필터리스트 컨테이너
		var sel_filterTemplate = selectFilterList.find('.sel_filter:first'); // 템플릿
		selectFilterList.find('.sel_filter:not(.none)').remove(); // 기존 리스트 초기화
		$('.chkFilter input[type=checkbox]:checked').each(function(index) {
			var sel_filter = sel_filterTemplate.clone().attr('data-parent-id', $('.chkFilter input[type=checkbox]').index($(this)));
		    var filterLabel = filterBoard.find('thead th').eq(filterBoard.find('tbody tr:first td').index($(this).parents('td'))).text();
		    sel_filter.find('a').text(filterLabel + ':' + $(this).parent().find('label').text());
		    selectFilterList.append(sel_filter.removeClass('none'));
		});
	},
//TODO		
	refreshSelectVsList : function (selVsIndexList, selVsNameList) 
	{
		var filterBoard = $('.serviceMonListTable');
		var selectVsList = $('.selectVsList'); // Vs리스트 컨테이너
		var sel_VsListTemplate = selectVsList.find('.sel_filter:first'); // 템플릿
		selectVsList.find('.sel_filter:not(.none)').remove(); // 기존 리스트 초기화
		
		if (selVsNameList != undefined)
		for(var i=0; i < selVsNameList.length; i++)
		{
			var sel_vsList = sel_VsListTemplate.clone().attr('data-index', selVsIndexList[i]);
		    var vsLabel = filterBoard.find('thead th .vsIp').text().trim();
		    sel_vsList.find('a').text(vsLabel + ':' + selVsNameList[i]);
		    selectVsList.append(sel_vsList.removeClass('none'));
		}
		
		if ($('.sel_filter:not(.none)').length < 1)
		{
			$('.selectedVsFilter').addClass('none');
		}
		else
		{
			$('.selectedVsFilter').removeClass('none');
		}
		
/*		
		$('.vsChk input[type=checkbox]:checked').each(function(index) {		
//			var sel_vsList = sel_VsListTemplate.clone().attr('data-parent-id', $('.vsColChk input[type=checkbox]').index($(this)));
//			var sel_vsList = sel_VsListTemplate.clone().attr('data-parent-id', index);
//		    var filterLabel = filterBoard.find('thead th').eq(filterBoard.find('tbody tr:first td').index($(this).parents('td'))).text();
			
//			var sel_vsList = sel_VsListTemplate.clone().attr('data-index', $('.vsChk input[type=checkbox]').parent().data('index'));
			var sel_vsList = sel_VsListTemplate.clone().attr('data-index', $('.vsChk input[type=checkbox]:checked').eq(index).val());
		    var vsLabel = filterBoard.find('thead th .vsIp').text().trim();
		    sel_vsList.find('a').text(vsLabel + ':' + $(this).parent().parent().find('.vsIp').text().trim());
		    selectVsList.append(sel_vsList.removeClass('none'));
		});*/
	},
	
	// Sorting
	refreshHeaderSort : function (table, orderType, orderDir) 
	{
		var header = table.find('.ContentsHeadLineOrder');
		header.find('.orderDir').text('2');
		header.find('img').attr('src', 'imgs/none.gif');
		
		var changedHeader = header.find('.orderType').filter(function() {
			return $(this).text() == orderType;
		}).parent();

		var nextDir = orderDir == 1 ? 2 : 1;
		
		changedHeader.find('img').attr('src', serviceMonitor.getSortImg(orderDir));
		changedHeader.find('.orderDir').text(nextDir);
	},

	getSortImg : function (orderDir) 
	{
		var sortImgs = 
		{
			1: 'imgs/Asc.gif',
			2: 'imgs/Desc.gif'
		};
		return sortImgs[orderDir];
	},	
	msgViewEvents : function()
	{
		if(this.searchFlag == true)
		{
			$('.nulldataMsg').addClass("none");
			$('.dataNotExistMsg').addClass("none");
			if($('.serviceList').size() > 1)
			{
				$('.searchNotMsg').addClass("none");
				$('.fixed-table-container-inner_xScroll').css('overflow-x','auto');
			}
			else
			{
				$('.searchNotMsg').removeClass("none");
				$('.fixed-table-container-inner_xScroll').css('overflow-x','hidden');
			}
			searchFlag=false;
		}
		else
		{
			$('.searchNotMsg').addClass("none");
			$('.nulldataMsg').addClass("none");
			if($('.serviceList').size() > 1)
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
			if (adcSetting.getAdcStatus() != "available") 
			{
				$('.searchNotMsg').addClass("none");
				$('.dataNotExistMsg').addClass("none");
				if($('.serviceList').size() > 1)
				{
					$('.nulldataMsg').addClass("none");
				}
				else
				{
					$('.nulldataMsg').removeClass("none");
				}
			}
		}
	},
	registerEvents : function()
	{
		with(this)
		{
			$('input[name="searchKey"]').attr("placeholder", "VS IP, VS Name");
			
			$('.btn a.searchLnk').click(function(e)
			{
				e.preventDefault();
				var searchKey = $('input[name="searchKey"]').val();
				searchFlag=true;
				loadRefreshSvcMonListContent(searchKey, orderDir, orderType);
			});
			
			$('.inputTextposition input.searchTxt').keydown(function(e)
			{
				if(e.which != 13)
					return;
				
				var searchKey = $(this).val();
				searchFlag=true;
				loadRefreshSvcMonListContent(searchKey, orderDir, orderType);
			});
			
			$('.orderHeader').click(function(e)
			{
				e.preventDefault();
				orderDir = $(this).find('.orderDir').text();
				orderType = $(this).find('.orderType').text();
				var searchKey = $('input[name="searchKey"]').val();
				searchFlag=true;
				loadRefreshSvcMonListContent(searchKey, orderDir, orderType);				
			});
								
			//필터옵션
			$('#filterAdd').click(function(e)
			{
				var isFlag= $(this).attr("isFlag");
				if (isFlag == "false")
				{
					$(this).attr("isFlag", "true");
					$('.optionFilter').removeClass('none');
					$(this).attr("class", "filter_option_btn_open");
				}
				else
				{
					$(this).attr("isFlag", "false");
					$('.optionFilter').addClass('none');
					$(this).attr("class", "filter_option_btn_close");
				}
			});
						
			$("#svcDownloadFrm").submit(function(e)
		    {
				var filterData = changeFilter();
				var selColData = selectedCol();	
		        	        
		        $('input[name="targetLevel"]').val(target.level);
		        $('input[name="targetIndex"]').val(target.index);
//		        $('input[name="searchKey"]').val($('.control_Board input.searchTxt').val());
//		        $('input[name="orderDir"]').val($(this).find('.orderDir').text());
//		        $('input[name="orderType"]').val($(this).find('.orderType').text());
		        $('input[name="selectedVal"]').val(JSON.stringify(filterData));
		        $('input[name="selectedCol"]').val(JSON.stringify(selColData));
		        			
		        var postData = $(this).serializeArray();
		        var formURL = 'monitor/downloadSvcMonList.action';
		        
		        $.ajax(
		        {
		            url : formURL,
		            type: "POST",
		            data : postData,
		            success:function(data, textStatus, jqXHR) 
		            {
		                $("#img").html('<pre><code class="prettyprint">'+data+'</code></pre>');

		            },
		            error: function(jqXHR, textStatus, errorThrown) 
		            {
		                $.obAlertAjaxError('Error', jqXhr);
		                document.getElementById('enquiry').style.visibility = 'hidden';
		            }
		        });
		        e.preventDefault(); //STOP default action
		    });
	
				
			$('#exportLnk').click(function(e)
			{
				e.preventDefault();
				var searchKey = $('input[name="searchKey"]').val();
				_checkExportSvcMonDataExist(searchKey);
//				$("#svcDownloadFrm").submit();				
			});
			
			$('#refreshLnk').click(function(e)
			{
//				loadListContent();
				$('input[name="searchKey"]').val('');
				loadRefreshSvcMonListContent(null, orderDir, orderType);				
			});	
			
			// 선택된 filter 전체해제
			$('.selectFilter').on('click', '.deselBtn', function(e)
			{
				$('.chkFilter').children().removeAttr('checked');
				$('.chkFilter').removeClass('on');
				
				$('.chkFilterAll').find('input').attr("checked", "checked");
				
//				if($('.chkFilterAll').find('input').not(':checked').length == 0)
//				{
//					$('.selectedOptionFilter').addClass("none");
//				}
//				else
//				{
//					$('.selectedOptionFilter').removeClass("none");					
//				}

				refreshSelectFilterList();
				
				if ($('.sel_filter:not(.none)').length < 1)
				{
					$('.selectedOptionFilter').addClass('none');
				}
				else
				{
					$('.selectedOptionFilter').removeClass('none');
				}
//				serviceMonitor.loadServiceMonitoringTableInListContent($('input[name="searchKey"]').val(),"","","","",changeFilter(), selectedCol());	
				loadRefreshSvcMonListContent($('input[name="searchKey"]').val(), "", "", changeFilter(), selectedCol());	
			});
			
			// 선택된 필터 개별 삭제
			$('.selectFilterList').on('click', '.delBtn', function(e)
			{
				$('.chkFilter input[type=checkbox]').eq($(this).parent().data('parent-id')).removeAttr('checked');				

//				if($('.chkFilterAll').find('input').not(':checked').length == 0)
//				{
//					$('.selectedOptionFilter').addClass("none");
//				}
//				else
//				{
//					$('.selectedOptionFilter').removeClass("none");					
//				}
				
				refreshSelectFilterList();
				
				if ($('.sel_filter:not(.none)').length < 1)
				{
					$('.selectedOptionFilter').addClass('none');
				}
				else
				{
					$('.selectedOptionFilter').removeClass('none');
				}
//				serviceMonitor.loadServiceMonitoringTableInListContent($('input[name="searchKey"]').val(),"","","","",changeFilter(), selectedCol());	
				loadRefreshSvcMonListContent($('input[name="searchKey"]').val(), "", "", changeFilter(), selectedCol());	
			});
							
			$('.chkFilter').change(function(e)
			{
				changeGroup($(this).parent());
				
				if($('.chkFilterAll').find('input').not(':checked').length == 0)
				{
					$('.selectedOptionFilter').addClass("none");
				}
				else
				{
					$('.selectedOptionFilter').removeClass("none");					
				}
				
				refreshSelectFilterList();
				
//				serviceMonitor.loadServiceMonitoringTableInListContent($('input[name="searchKey"]').val(), "", "", changeFilter());
				loadRefreshSvcMonListContent($('input[name="searchKey"]').val(), "", "", changeFilter(), selectedCol());				
				
			});	
//TODO 			
			
			$('#serviceListTable').on('click', '.vsColChk', function(e)
//			$('.vsColChk').change(function(e)
//			$('.selectVsList').on('click', '.vsColChk', function(e)
			{								
//				var checkedFilter = group.find('.chkFilter').find('input:checkbox:checked');
				
				var vsIndexList = new Array();
				var vsNameList = new Array();
			
				if(selectedVsIndexList != undefined)
				{
					vsIndexList = selectedVsIndexList;
					vsNameList = selectedVsNameList;
				}			
				
				var selectedVsIndex = $(this).val().trim();											// 2: "5_/Common/vs_192.168.200.232_80"							
				var selectedVsName = $(this).parent().parent().find('.name').text().trim();
				
				if (selectedVsName == null || selectedVsName == "")
				{
					selectedVsName = $(this).parent().parent().find('.ip .vsIp').text().trim();
				}
				
				
				var isChecked = $(this).is(':checked');
				
				if($(this).is(':checked'))
				{
					$(this).attr('checked', isChecked);
					
					$(this).parent().parent().parent().parent().find('.vsColChk:checked').each(function(index)
					{
//						console.log("checked value : " , $(this).val());
						
						if (selectedVsIndexList != undefined)
						{
							if(selectedVsIndexList.indexOf($(this).val()) == -1)
							{
								vsIndexList.push($(this).val());
								vsNameList.push(selectedVsName);
							}
						}
						else
						{
							vsIndexList.push($(this).val());
							vsNameList.push(selectedVsName);
						}
					/*	
						if (selectedVsNameList != undefined)
						{
//							if(selectedVsNameList.indexOf(selectedVsName) == -1)
//							{
//								vsNameList.push(selectedVsName);
//							}
							vsNameList.push(selectedVsName);
						}
						else
						{
							vsNameList.push(selectedVsName);
						}*/
					});
				}
				else   
				{
					$(this).attr('checked', false);
					
					if (selectedVsIndexList.indexOf(selectedVsIndex) > -1)
					{
						var index = selectedVsIndexList.indexOf(selectedVsIndex);
						vsIndexList.splice(index, 1);
					}
					
					if (selectedVsNameList.indexOf(selectedVsName) > -1)
					{
						var index = selectedVsNameList.indexOf(selectedVsName);
						vsNameList.splice(index, 1);
					}
//						vsIndexList.removeItem(selectedVsIndex, selectedVsIndexList);
					
//					for(var i=0; i<selectedVsIndexList.length; i++)
//					{
//						if (selectedVsIndexList.indexOf(selectedVsIndex) == 0)
//						{
//							
//							var deleteVsIndex = selectedVsIndexList[i];
//							var position = vsIndexList.indexOf(deleteVsIndex);
//							vsIndexList.splice(position, 1);
//						}
//					}			
				}
				if(vsIndexList.length > 5)
				{
					alert(VAR_SVCPERFOM_ALLGRP_AVAILABLE_COUNTCHK);
					$(this).attr('checked', false);
					
					var index = selectedVsIndexList.indexOf(selectedVsIndex);
					vsIndexList.splice(index, 1);
					vsNameList.splice(index, 1);
					
					return false;
				}
				
				/*if($('.vsChk').find('input').not(':checked').length == 0)
				{
					$('.selectedVsFilter').addClass("none");
				}
				else
				{
					$('.selectedVsFilter').removeClass("none");					
				}
				*/								
				refreshSelectVsList(vsIndexList, vsNameList);
				
				loadBpsConnHistoryInfo(vsIndexList, vsNameList, undefined);	
				
//					0: "5_/Common/vs_192.168.200.230_80"
//					1: "5_/Common/vs_192.168.200.231_80"
//					2: "5_/Common/vs_192.168.200.232_80"
//					3: "5_/Common/vs_192.168.200.233_80"
//					4: "5_/Common/vs_192.168.200.230"

//				var selectedVsIndex = $(this).val().trim();						// 2: "5_/Common/vs_192.168.200.232_80"
//				var selectedVsName = $(this).parent().parent().find('.name').text().trim();
//				$('#serviceListTable tbody tr:not(.none)').find('.vsColChk').each(function(index)
				
				// 기존 vs 에서 선택 해제
				// 선택 해제한 vsIndex가 체크되어 있는 vsIndex 에 존재하지 않으면 $(this).val()을 vsIndexList 에서 제거.					
				
//				$(this).parent().parent().parent().parent().find('.vsColChk:checked').each(function(index)
//				{
//					if($(this).eq(index).indexOf(selectedVsIndex) < 0)
//					{
//						var deleteVsIndex = selectedVsIndexList[index];
//						var position = vsIndexList.indexOf(deleteVsIndex);
//						vsIndexList.splice(position, 1);
//					}
//				});
				
				
//				if($(this).is(':checked') == false)
//				{					
//					$(this).parent().parent().parent().parent().find('.vsColChk:checked').each(function(index)
//					{
//						if($(this).eq(index).indexOf(selectedVsIndex) < 0)
//						{
//							var deleteVsIndex = selectedVsIndexList[index];
//							var position = vsIndexList.indexOf(deleteVsIndex);
//							vsIndexList.splice(position, 1);
//						}
//					});
//				}	
				
				
//				if(selectedVsIndexList.contains(selectedVsIndex))
//				{
//					vsIndexList = selectedVsIndexList;
//				}
//				else
//				{
//					var deleteVsIndex = selectedVsIndexList[index];
//					var position = vsIndexList.indexOf(deleteVsIndex);
//					vsIndexList.splice(position, 1);
//				}
				
				/*$(this).parent().parent().parent().parent().find('.vsColChk').each(function(index)
				{					
					if($(this).val() == selectedVsIndexList[index])
					{
						vsIndexList = selectedVsIndexList;
					}
					else
					{
						if($(this).is(':checked'))
							vsIndexList.push(selectedVsIndex);
						else
						{
							var deleteVsIndex = selectedVsIndexList[index];
							var position = vsIndexList.indexOf(deleteVsIndex);
							vsIndexList.splice(position, 1);
//							vsIndexList.remove(selectedVsIndexList[index]);
						}
						
					}
					//console.log(vsIndexList);
					
					
					if($(this).parent().parent().find('.name').text() == selectedVsNameList[index])
					{
						vsNameList = selectedVsNameList;
					}
					else
					{
//						vsNameList.remove(index);
//						vsNameList.push(selectedVsName);
						if($(this).is(':checked'))
							vsNameList.push(selectedVsName);
						else
						{
							var deleteVsName = selectedVsNameList[index];
							var position = vsIndexList.indexOf(deleteVsName);
							vsNameList.splice(position, 1);
						}
					}
					//console.log(vsNameList);
				});*/
				
				
				
//				var selectedVsIndex = $(this).val().trim();
//				var selectedVsName = $(this).parent().parent().find('.name').text().trim();
//				$('#serviceListTable tbody tr:not(.none) input[type=checkbox]:checked').each(function(index)
//				{
//					if($(this).val() == selectedVsIndexList[index])
//					{
//						vsIndexList = selectedVsIndexList;
//					}
//					else
//					{
//						vsIndexList.push(selectedVsIndex);
//					}
//					console.log(vsIndexList);
//					
//					if($(this).parent().parent().find('.name').text() == selectedVsNameList[index])
//					{
//						vsNameList = selectedVsNameList;
//					}
//					else
//					{
//						vsNameList.push(selectedVsName);
//					}
//					console.log(vsNameList);
//				});
				
//				$('#serviceListTable tbody tr:not(.none)').find('.name').each(function(index)
////						$('#serviceListTable tbody tr:not(.none) input[type=checkbox]:checked').each(function(index)
//				{		
//					if($(this).text() == selectedVsNameList[index])
//					{
//						vsNameList = selectedVsNameList;
//					}
//					else
//					{
//						vsNameList.push(selectedVsName);
//					}
//					console.log(vsNameList);
//					
////					if ($(this).parent().find('.vsColChk').is(':checked'))
////					{
////						vsNameList.push($(this).text().trim());
////					}
//				});
						
//				for(var i=0; i < selectedVsIndexList.length; i++)
//				{
//					if($(this).val() == selectedVsIndexList[i])
//						vsIndexList.push($(this).val().trim());
//				}
				
//				for(var i=0; i < selectedVsIndexList.length; i++)
//				{
//					$('#serviceListTable tbody tr:not(.none)').find('.vsColChk').each(function(index)
//					{
//						if($(this).val() == selectedVsIndexList[i])
//							vsIndexList.push($(this).val().trim());
//					});
//				}
//				for(var i=0; i < selectedVsIndexList.length; i++)
//				{	
////				$('#serviceListTable tbody tr:not(.none) input[type=checkbox]:checked').each(function(index)
//				$('#serviceListTable tbody tr:not(.none)').find('.vsColChk').each(function(index)
//				{						
//					if ($(this).is(':checked'))
//					{
////								array_data.push($('#serviceListTable tbody tr:not(.none)').eq(index).find('.vsColChk').val());
//						
//							if($(this).val() != selectedVsIndexList[i])
//								vsIndexList.push($(this).val().trim());
//						
////						vsIndexList.push($(this).val().trim());
////								vsPortList.push($(this).parent().parent().find('.port').text().trim());
//					}
//				});
//				}
				
//				$('#serviceListTable tbody tr:not(.none)').find('.name').each(function(index)
////						$('#serviceListTable tbody tr:not(.none) input[type=checkbox]:checked').each(function(index)
//				{						
//					if ($(this).parent().find('.vsColChk').is(':checked'))
//					{
//						vsNameList.push($(this).text().trim());
//					}
//				});
//				loadBpsConnHistoryInfo(vsIndexList, vsNameList, undefined);		
			});	
			
			// 선택된 vs 개별 삭제
			$('.selectVsList').on('click', '.delVsBtn', function(e)
			{
//				console.log($('.vsChk input[type="checkbox"]:checked').eq(0));
//				console.log($('.vsChk input[type="checkbox"]:checked').eq(1));
//				
//				console.log($(this).parent().data('parent-id'));
				
//				$('.vsChk input[type=checkbox]').eq($(this).parent().data('index')).removeAttr('checked');
				
				var selVsIndex = $(this).parent().data('index');
				
				$('.vsChk input[type=checkbox]:checked').each(function(index)
				{
					if(selVsIndex == $(this).val())
						$('.vsChk input[type=checkbox]:checked').eq(index).removeAttr('checked');
				});
				
//				if($(this).parent().data('index') == $('.vsChk input[type=checkbox]:checked').val())
//				{
//					$('.vsChk input[type=checkbox]:checked').each(function(index)
//					{
//						$('.vsChk input[type=checkbox]:checked').eq(index).removeAttr('checked');
//					});
//				}
					
				
//				if()
//				{
//					$('.vsChk input[type=checkbox]').eq(index).removeAttr('checked');
//				}
//				
//				$('.vsChk input[type=checkbox]:checked').each(function(index)
//				{
//					if($(this).val() == $('.sel_vsList').eq(index).data('index'))
//						$('.vsChk input[type=checkbox]').eq(index).removeAttr('checked');
//				});
				
//				$('.vsChk input[type=checkbox]').eq($(this).parent().data('index')).removeAttr('checked');		
											
				var vsIndexList = new Array();
				var vsNameList = new Array();
				
				$('#serviceListTable tbody tr:not(.none) input[type=checkbox]:checked').each(function(index)
				{						
					if ($(this).is(':checked'))
					{
//								array_data.push($('#serviceListTable tbody tr:not(.none)').eq(index).find('.vsColChk').val());
						vsIndexList.push($(this).val().trim());
//								vsPortList.push($(this).parent().parent().find('.port').text().trim());
					}
				});
				
				$('#serviceListTable tbody tr:not(.none)').find('.name').each(function(index)
//						$('#serviceListTable tbody tr:not(.none) input[type=checkbox]:checked').each(function(index)
				{						
					if ($(this).parent().find('.vsColChk').is(':checked'))
					{
						vsNameList.push($(this).text().trim());
					}
				});
				loadBpsConnHistoryInfo(vsIndexList, vsNameList, undefined);		
				
				refreshSelectVsList(vsIndexList, vsNameList);
				
				if ($('.sel_filter:not(.none)').length < 1)
				{
					$('.selectedVsFilter').addClass('none');
				}
				else
				{
					$('.selectedVsFilter').removeClass('none');
				}
			});
			
			// 선택된 vs 전체 선택 헤제
			$('.selectedVsFilter').on('click', '.deselVsBtn', function(e)
			{				
				$('.vsChk input[type=checkbox]:checked').removeAttr('checked');
				
				refreshSelectVsList();
				
				if ($('.sel_filter:not(.none)').length < 1)
				{
					$('.selectedVsFilter').addClass('none');
				}
				else
				{
					$('.selectedVsFilter').removeClass('none');
				}
					
				var vsIndexList = undefined;
				var vsNameList = undefined;
				
				loadBpsConnHistoryInfo(vsIndexList, vsNameList, undefined);		
			});
			// 적용
			$('.selectVs').on('click', '.applyBtn', function(e)
			{					
				var vsIndexList = new Array();
				var vsNameList = new Array();
//				$('#serviceListTable tbody tr:not(.none)').find('.vsColChk').each(function(index)
				$('#serviceListTable tbody tr:not(.none) input[type=checkbox]:checked').each(function(index)
				{						
					if ($(this).is(':checked'))
					{
//						array_data.push($('#serviceListTable tbody tr:not(.none)').eq(index).find('.vsColChk').val());
						vsIndexList.push($(this).val().trim());
//						vsPortList.push($(this).parent().parent().find('.port').text().trim());
					}
				});
				
				$('#serviceListTable tbody tr:not(.none)').find('.name').each(function(index)
//				$('#serviceListTable tbody tr:not(.none) input[type=checkbox]:checked').each(function(index)
				{						
					if ($(this).parent().find('.vsColChk').is(':checked'))
					{
						vsNameList.push($(this).text().trim());
					}
				});
				loadBpsConnHistoryInfo(vsIndexList, vsNameList, undefined);	
			});
			
//			$('.vsColChk').change(function(e)
//			{
//				changeGroup($(this).parent());
//				
//				if($('.chkFilterAll').find('input').not(':checked').length == 0)
//				{
//					$('.selectedVsFilter').addClass("none");
//				}
//				else
//				{
//					$('.selectedVsFilter').removeClass("none");					
//				}
//				
//				refreshSelectFilterList();	
//				
//			});	

			$('#selectColumn_pop').click(function (e)
			{
				e.preventDefault();		
				serviceMonitor._loadSelColPop();			
			});
			
			//link
			
			$('#serviceListTable').on('click', '.serviceMapLnk', function(e)
			{
				e.preventDefault();
				with (this) 
				{
					var adcIndex = $(this).children('span').text();
					adcSetting._selectAdc(adcIndex);
					networkMap.loadNetworkMapContent(adcSetting.getAdc());
				}				
			});
			
			$('#serviceListTable').on('click', '.adcMonitoringLnk', function(e)
			{			
				e.preventDefault();
				with (this) 
				{
					var adcIndex = $(this).children('span').text();
					adcSetting._selectAdc(adcIndex);
					monitorAppliance.loadApplianceMonitorContent(adcSetting.getAdc());
				}
			});
			
			$('#serviceListTable').on('click', '.serviceMonitoringLnk', function(e)
			{
				e.preventDefault();
				with (this) 
				{
					var adcIndex = $(this).children('span:first').text();
					var adcCategory = $(this).children('span:nth-child(2)').text();
					adcSetting._selectAdc(adcIndex);
					adcSetting.setSelectIndex(adcCategory);
					monitorServicePerfomance.loadServicePerfomanceContent(undefined, undefined, adcSetting.getAdc());
				}				
			});	
			
			$('#serviceListTable').on('click', '.slbHistoryLnk', function(e)
			{
				e.preventDefault();
				with(this)
				{
					var adcIndex = $(this).children('span').text();
					adcSetting._selectAdc(adcIndex);
					adcHistory.loadListContent(adcSetting.getAdc());
				}
			});
			
			$('#serviceListTable').on('click', '.groupLnk', function(e)
			{
				e.preventDefault();
				with(this)
				{
					groupMonitor.loadListContent();
				}
			});
			
			$('#bpsChk').change(function(event) 
			{			
				event.preventDefault();
				
				var isChecked = $(this).is(':checked');
				if(isChecked == true)
				{
					$('.bpsChart').removeClass('none');
//					loadConnHistoryInfo(selectedVsIndex, undefined);
				}
				else
				{
					$('.bpsChart').addClass('none');
				}
			});
			
			$('#concurrSessionChk').change(function(event) 
			{			
				event.preventDefault();
				
				var isChecked = $(this).is(':checked');
				if(isChecked == true)
				{
					$('.connCurrChart').removeClass('none');
//					loadConnHistoryInfo(selectedVsIndex, undefined);
				}
				else
				{
					$('.connCurrChart').addClass('none');
				}
				
//				if($('input[name="bpsValChk"]').is(':checked') == true)							
//				{
//					loadBpsConnHistoryInfo(selectedVsIndex, undefined);
//				}
			});
			
			// DatePicker Setting
			if (null != searchStartTime)
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
	            	  searchStartTime = Number(start.format('x'));
	            	  searchEndTime = Number(end.format('x'));
	              });
			}
			
//			$('#serviceListTable tbody tr:not(.none)').eq(0).find('.vsColChk').attr("checked", true);
//			$('#serviceListTable tbody tr:not(.none)').eq(1).find('.vsColChk').attr("checked", true);
//			$('#serviceListTable tbody tr:not(.none)').eq(2).find('.vsColChk').attr("checked", true);
//			$('#serviceListTable tbody tr:not(.none)').eq(3).find('.vsColChk').attr("checked", true);
//			$('#serviceListTable tbody tr:not(.none)').eq(4).find('.vsColChk').attr("checked", true);
		}
	},
	
	changeGroup : function(group) 
	{
		var chkFilterAll = group.find('.chkFilterAll');
		var checkedFilter = group.find('.chkFilter').find('input:checkbox:checked');
		if (checkedFilter.length < 1)
		{
			chkFilterAll.find('input[type=checkbox]').attr('checked', 'checked');
		}
		else
		{
			chkFilterAll.find('input[type=checkbox]').removeAttr('checked');
		}
	},
	
	changeFilter : function()
	{
		with(this)
		{
			var filterData = {};
			var param;
			$('.chklist').each(function(index) {
				param = [];
				$(this).find('input').each(function(index)
				{
					index =$(this).parent().find('span').text();				
					
					param.push([index, $(this).parent().find('label').text(), $(this).val(), $(this).is(':checked')].join('|'));
				});					  		
				filterData[$(this).data('group')] = param; // 셀렉트박스의 data-group 속성을 읽어 해당 키에 배치				
			});
			serviceMonitor.selectedSet = filterData;
						
			if ($('.chkFilter').find('input:checked').length > 0)				
				serviceMonitor.setFlag = $('#filterAdd').attr("isFlag");
			
			return filterData;
		}
	},
	
	selectedCol : function()
	{
		with(this)
		{
			var selColData = [];
			$('#serviceListTable tbody tr:first td').each(function()
//			$('.selColHead th').each(function()
			{
				selColData.push(!$(this).hasClass('none'));
			});
			
			return selColData;
		}
	},
	
	_loadSelColPop : function()
	{
		with(this)
		{
			$('#selCol-popup table tbody').html('');
			$('#serviceListTable .ContentsHeadLine').find('th').each(function()			
			{
				$('#selCol-popup table tbody').append(
				['<tr>',
					 '<td class="align_center">',
						 '<input name="colChk" class="colChk" type="checkbox"' + ($(this).is('.default') ? 'checked="checked" disabled="disabled"' : ($(this).is('.none') ? '' : 'checked="checked"')) + '>',
					 '</td>',
					 '<td class="align_left_P11">',
					 	$(this).find('a').contents().filter(function(){return this.nodeType === 3;}).text().trim()
					 	+ $(this).contents().filter(function(){return this.nodeType === 3;}).text().trim(),
					 '</td>',
				 '</tr>'].join(''));
			});
			
			$('#selCol-popup table tbody').find('tr').eq(0).remove();
			
			showPopup({
				'id' : '#selCol-popup',
				'width' : '400px'
			});
			
			_registerEvents();
			
		}
	},
	_registerEvents : function()
	{
		with(this)
		{	
			if ($('.cloneDiv input[name="colChk').not(":checked").length < 1)
				$('.allChk').attr("checked", "checked");
			
			$('.allChk').click(function(e)
			{
				var isChecked = $(this).is(':checked');
								
				if($(this).is('.default') == true)
				{
					$('.cloneDiv .colChk:not([disabled])').attr("checked", isChecked);
				}
				else
				{
					$('.cloneDiv .colChk:not([disabled])').attr("checked", isChecked);
				}
				checkedOptionCount();
			});
			
			checkedOptionCount();
			
			$('.cloneDiv .colChk').click(function(e)
			{
//				if ($('.cloneDiv .colChk:checked').length > 9)
//				{
//					alert('최대 선택 컬럼개수를 초과하였습니다.');
//					e.preventDefault();
//					return;
//				}
				checkedOptionCount();
				
				if ($('.cloneDiv input[name="colChk').not(":checked").length > 0)
					$('.allChk').removeAttr("checked");
				else
					$('.allChk').attr("checked", "checked");
			});
			
			$('.cloneDiv .onOk').click(function(e)
			{
				e.preventDefault();
				
				$(this).parents('#selCol-popup').find('.colChk').each(function(index)
				{
					if ($(this).is(':checked'))
						showHidenCol($('#serviceListTable'), index, true);
					else
						showHidenCol($('#serviceListTable'), index, false);
				});
				
				serviceMonitor.columnSet = selectedCol();
				serviceMonitor.selectedSet = changeFilter();
				
				loadRefreshSvcMonListContent($('input[name="searchKey"]').val(), "", "", changeFilter(), selectedCol());	
				$('.cloneDiv .close').click();
			});
			
			$('.cloneDiv .onCancel').click(function(e)
			{
				e.preventDefault();
				$('.popup_type1').remove();
				$('.cloneDiv').remove();
			});
		}
	},
	
	checkedOptionCount : function()
	{
		var $optionCheckedCount = $('.cloneDiv .checkedCount').filter(':last');
		var $optiontotalCount = $('.cloneDiv .totalCount').filter(':last');			
				
//		var $totalCount = $('.cloneDiv .colChk').length;
//		var $totalCount = 9; // 선택할 수 있는 최대 개수 8개
		var $checkedCount =$('.cloneDiv .colChk:checked').length;
		
		$optionCheckedCount.empty();
		$optionCheckedCount.html($checkedCount);
		$optiontotalCount.empty();
//		$optiontotalCount.html("/" +$totalCount);
	},
	showHidenCol : function(table, col, show)
	{	
		// colgroup check
		var colElems = $(table).find('colgroup').contents().filter(function() {
			return (this.nodeType == 8 && $(this.nodeValue).data('colidx') == col)
				|| (this.nodeType == 1 && $(this).data('colidx') == col)
		});
		
//		var colElem = $(table).find('colgroup').contents().filter(function() {
//			return this.nodeType == 1 || this.nodeType == 8;
//		})[col];
		
		// show가 true일 때 엘리먼트는 제외, show가 false일 때 주석 제외
//		var currentShow = colElem.nodeType;
//		if ((show && currentShow == 1) || (!show && currentShow == 8))
//			return;
		
		if (show)
		{
			$.each(colElems, function(index, colElem)
			{
				if (colElem.nodeType == 8)
					$(colElem).replaceWith(colElem.nodeValue);
			});
//			$(colElem).replaceWith(colElem.nodeValue);
			$(table).find('[data-colidx=' + col + ']').removeClass('none');
			$('.Filter_Board [data-colidx=' + col + ']').removeClass('none');
		}
		else
		{
			$.each(colElems, function(index, colElem)
			{
				if (colElem.nodeType == 1)
					$(colElem).replaceWith(document.createComment(colElem.outerHTML));
			});
//			$(colElem).replaceWith(document.createComment(colElem.outerHTML));
			$(table).find('[data-colidx=' + col + ']').addClass('none');
			$('.Filter_Board [data-colidx=' + col + ']').addClass('none');
		}
		// 해당 행 표시상태 변경
//		if (show) 
//		{
//			$(table).find('[data-colidx=' + col + ']').removeClass('none');
//		} 
//		else 
//		{
//			$(table).find('[data-colidx=' + col + ']').addClass('none');
//		}
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
			targetCntHtml.html(addComma(countTotal));
			targetPageHtml.html("(" + addComma(currentPage) + "/" + addComma(lastPage) + VAR_COMMON_PAGE + ")");
			
			
//			var titleHeader = $('.title_name_area').filter(':last');
//			if(target.level == 1)
//			{
//				titleHeader.empty();
//				titleHeader.html(adcSetting.getGroupName());
//			}
//			else
//			{
//				titleHeader.empty();
//			}
		}
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
		}
		return true;		
	},
	
//	exportLnks : function()
//	{
//		with(this)
//		{
//			$('#svcDownloadFrm').submit(function()
//			{
//				var filterData = changeFilter();
//				var selColData = selectedCol();	
//				var params = 
//				{				
//						"adcScope.level" 	: target.level,					
//						"adcScope.index"	: target.index,
//						"searchKey" 		: encodeURIComponent($('.control_Board input.searchTxt').val()),
//						"orderDir"			: this.orderDir,
//						"orderType"			: this.orderType,
//						"selectedVal"		: JSON.stringify(filterData),
//						"selectedCol"		: JSON.stringify(selColData)
//				};
//				$(this).ajaxSubmit({
//	//				dataType : 'json',
//					type : "POST",
//					url : 'monitor/downloadSvcMonList.action',
//					data : params,	
//					async: false,
//			        cache: false,
//			        contentType: false,
//			        processData: false,
//					success : function(data) 
//					{
//						FlowitUtil.log(Object.toJSON(data));
//						if (data.isSuccessful) 
//						{ 
//							alert(VAR_COMMON_REGISUCCESS);
//						} 
//						else
//						{
//							alert(data.message);
//						}	
//					},
//					error : function(a, b, c) 
//					{
//						alert(VAR_SYSSETTING_USERADDMODIFY);
//					}
//				});
//				return false;
//			});
//		}
//	},
//	exportLnk : function() 
//	{
//		with (this) 
//		{
//			var filterData = changeFilter();
//			var selColData = selectedCol();
//			ajaxManager.runJsonExt
//			({
//				url :"monitor/downloadSvcMonList.action",				
//				data : 
//				{
//					"adcScope.level" 	: target.level,					
//					"adcScope.index"	: target.index,
//					"searchKey" 		: encodeURIComponent($('.control_Board input.searchTxt').val()),
//					"orderDir"			: this.orderDir,
//					"orderType"			: this.orderType,
//					"selectedVal"		: JSON.stringify(filterData),
//					"selectedCol"		: JSON.stringify(selColData)
//				},
//				target: "#downloadLnk",
//				successFn : function(params) 
//				{
//					// '\ufeff': UTF-8 헤더문자
//					var blob = new Blob(['\ufeff' + params], {type: 'text/csv;charset=UTF-8;'});
//					var fileName = 'test.csv';
//					
//					navigator.saveBlob = navigator.saveBlob || navigator.msSaveBlob || navigator.mozSaveBlob || navigator.webkitSaveBlob;
//					window.saveAs = window.saveAs || window.webkitSaveAs || window.mozSaveAs || window.msSaveAs;
//					if (window.saveAs)
//					{
//						window.saveAs(blob, fileName);
//					}
//					else if (navigator.saveBlob)
//					{
//						navigator.saveBlob(blob, fileName);
//					}
//					else
//					{
//						var url = URL.createObjectURL(blob);
//						var link = document.createElement('a');
//						link.setAttribute('href', url);
//						link.setAttribute('download', fileName);
//						link.click();
//					}
//					
////					var params = "adcScope.level=" + target.level + "&adcScope.index=" + target.index + "&searchKey=" + encodeURIComponent($('.control_Board input.searchTxt').val()) + 
////						"&selectedVal=" + JSON.stringify(filterData) + "&selectedCol=" + JSON.stringify(selColData);
////					var url = "monitor/downloadSvcMonList.action?" + params;
////					$('#downloadFrame').attr('src', url);
//				},
//				errorFn : function(a,b,c)
//				{
//					alert(VAR_SYSBACK_DOWNLOAD);
//				}	
//			});
//		}
//	}
	
	_checkExportSvcMonDataExist : function(searchKey)
	{
		with(this)
		{
			var filterData = changeFilter();
			var selColData = selectedCol();
			ajaxManager.runJsonExt({
				url : "monitor/checkExportSvcMonDataExist.action",				
				data : 
				{
					"adcScope.level" 	: target.level,					
					"adcScope.index"	: target.index,
					"searchKey" 		: searchKey == null ? undefined : searchKey,
					"orderDir"			: this.orderDir,
					"orderType"			: this.orderType,
					"selectedVal"		: JSON.stringify(filterData),
					"selectedCol"		: JSON.stringify(selColData)
				},
				successFn : function(data)
				{
					if(!data.isSuccessful)
					{
						$.obAlertNotice(data.message);
						return;
					}
					exportLnk();
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_COMMON_EXPDATAEXIST, jqXhr);
				}
			});
		}
	},
	
	exportLnk : function()
	{
		with (this)
		{
			var filterData = changeFilter();
			var selColData = selectedCol();
			$.ajax(
			{
				url: 'monitor/downloadSvcMonList.action',
				type: 'POST',
				data:
				{
					"adcScope.level": target.level,
					"adcScope.index": target.index,
					"searchKey": encodeURIComponent($('.control_Board input.searchTxt').val()), 
					"selectedVal": JSON.stringify(filterData), 
					"selectedCol": JSON.stringify(selColData)
				},
				success: function(data)
				{
//					String baseName = "log";
//					String postfix = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
//					String extension = "csv";
//					String fileName = baseName + "_" + postfix + "." + extension;
//										
//					exportCsv(fileName, data);
					exportCsv('log_' + parseDateTimeStringFunc(new Date()) + '.csv', data);
				}
			});
//			
//			var params = "adcScope.level=" + target.level + "&adcScope.index=" + target.index + "&searchKey=" + encodeURIComponent($('.control_Board input.searchTxt').val()) + 
//						"&selectedVal=" + JSON.stringify(filterData) + 
//						"&selectedCol=" + JSON.stringify(selColData);					
//			var url = "monitor/downloadSvcMonList.action?" + params;
//			$('#downloadFrame').attr('src', url);
		}
	},
	exportCsv: function(fileName, csv)
	{
		if (!csv || csv == '')
			return $.obAlertNotice(VAR_COMMON_EXPDATAEXIST);

		// '\ufeff': UTF-8 헤더문자
		var blob = new Blob(['\ufeff' + csv], {type: 'text/csv;charset=UTF-8;'});

		navigator.saveBlob = navigator.saveBlob || navigator.msSaveBlob || navigator.mozSaveBlob || navigator.webkitSaveBlob;
		window.saveAs = window.saveAs || window.webkitSaveAs || window.mozSaveAs || window.msSaveAs;
		if (window.saveAs)
		{
			window.saveAs(blob, fileName);
		}
		else if (navigator.saveBlob)
		{
			navigator.saveBlob(blob, fileName);
		}
		else
		{
			var url = URL.createObjectURL(blob);
			var link = document.createElement('a');
			link.setAttribute('href', url);
			link.setAttribute('download', fileName);
			link.click();
		}
	},
	
	// bps&Connection Chart Data get
	loadBpsConnHistoryInfo : function(vsIndex, vsName, rowIndex)
	{
		this.selectedVsIndexList = vsIndex;		
		this.selectedVsNameList = vsName;
		
//		this.adc.index = 2;
//		this.adc.name = '192.168.200.12';
		with (this)
		{
//			if (!adc || !vsIndex || !selectedSearchTimeMode)
//			if (!vsIndex)
//			{
//				return;
//			}
			var params = {
//				"adcObject.category"			: adcSetting.getSelectIndex(),
//				"adcObject.index"				: 2, //adc.index,			
//				"adcObject.name"				: '192.168.200.12', //adc.name,
//				"vsIndex"						: vsIndex
				"adcObject.category"			: target.level,
				"adcObject.index"				: target.index,
				"adcObject.strIndex"			: vsIndex,
				"adcObject.name"				: vsName				
			};
//			if (0 === port || port)
//			{
//				params["svcPort"] = port;
//			};			
						
			ajaxManager.runJsonExt({
				url			: "monitor/loadMultiBpsInfo.action",
				data		: params,
				successFn	: function(data)
				{	
//					console.log(data);
					interval = data.intervalMonitor;
					GenerateSvcMultiPerfomanceBpsChart(data, interval);
//					loadBpsMaxAvgHistoryInfo(vsIndex, vsName, undefined);		
//					loadBpsMaxAvgHistoryInfo(vsIndex, port, undefined, vsName);		
					
					if($('input[name="concurrSessionValChk"]').is(':checked') == true)
					{
						GenerateSvcMultiPerfomanceConnChart(data, null, null, interval);				
//						loadConnMaxAvgHistoryInfo(vsIndex, vsName, undefined);
					}
					
//					console.log(data.bpsConnInfoData);
//					console.log(data.bpsConnInfoData.bpsInfo);
					
					_addBpsConnInfoAvgMaxToTbl(data.bpsConnInfoData);
					
				},
				completeFn	: function(data)
				{	
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_SVCPERFOM_LAOD, jqXhr);
//					selectSvcPerfMonitorTableRow(vsIndex, port, rowIndex, vsName);
				}
			});
		}		
	},
	
	_addBpsConnInfoAvgMaxToTbl : function(bpsConnInfo)
	{
		with (this)
		{
			var html = "";
			$('.bpsCurAvgMaxTr').empty();
			$('.connCurAvgMaxTr').empty();
			if (bpsConnInfo.bpsInfo != null)
			{
				for(var i=0; i< bpsConnInfo.bpsInfo.length; i++)
				{
					html = '<tr class="bpsCurAvgMaxTr">';
					html += '<td class="textOver"><span class="square chart_' + i + '"></span>' + bpsConnInfo.bpsInfo[i].name+ '</td>';
					html += '<td>' + bpsConnInfo.bpsInfo[i].currentUnit.replace("-1", "-") + '</td>';
					html += '<td>' + bpsConnInfo.bpsInfo[i].avgUnit.replace("-1", "-") + '</td>';
					html += '<td>' + bpsConnInfo.bpsInfo[i].maxUnit.replace("-1", "-") + '</td>';					
					html += '</tr>';
					$('.bpsCurAvgMaxTbd').append(html);
				}
			}
			if (bpsConnInfo.connInfo != null)
			{
				for(var i=0; i< bpsConnInfo.connInfo.length; i++)
				{
					html = '<tr class="connCurAvgMaxTr">';
					html += '<td class="textOver"><span class="square chart_' + i + '"></span>' + bpsConnInfo.connInfo[i].name+ '</td>';
					html += '<td>' + bpsConnInfo.connInfo[i].currentUnit.replace("-1", "-") + '</td>';
					html += '<td>' + bpsConnInfo.connInfo[i].avgUnit.replace("-1", "-") + '</td>';
					html += '<td>' + bpsConnInfo.connInfo[i].maxUnit.replace("-1", "-") + '</td>';					
					html += '</tr>';
					$('.connCurAvgMaxTbd').append(html);
				}
			}
		}
	},
	// bps Avg/Max Data get
	loadBpsMaxAvgHistoryInfo : function(vsIndex, vsName, rowIndex)
	{
		this.selectedVsIndexList = vsIndex;
		this.selectedVsNameList = vsName;
		with (this)
		{
//			if (!adc || !vsIndex || !selectedSearchTimeMode)
			if (!vsIndex)
			{
				return;
			}
			var params = {
//				"adcObject.category"			: adcSetting.getSelectIndex(),
//				"adcObject.index"				: 2, //adc.index,			
//				"adcObject.name"				: '192.168.200.12', //adc.name,
//				"vsIndex"						: vsIndex
				"adcObject.category"			: target.level,
				"adcObject.index"				: target.index,
				"adcObject.strIndex"			: vsIndex,
				"adcObject.name"				: vsName
			};
//			if (0 === port || port)
//			{
//				params["svcPort"] = port;
//			};
//			params['preCompare'] = preVal;
			
			ajaxManager.runHtmlExt({
				url			: "monitor/loadMultiBpsMaxAvgInfo.action",
				data		: params,
				target		: "table.bpsAvgMax",	
				successFn	: function(data)
				{					
//					GenerateSvcPerfomanceBpsConnChart(data, bpsVal, preVal);				
//					selectSvcPerfMonitorTableRow(vsIndex, port, rowIndex, vsName);
					
//					registerEvent(bpsVal);
					
					if($('input[name="concurrSessionValChk"]').is(':checked') == true)
					{				
						loadConnMaxAvgHistoryInfo(vsIndex, vsName, undefined);
					}
				},
				completeFn	: function(data)
				{	
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_SVCPERFOM_LAOD, jqXhr);
//					selectSvcPerfMonitorTableRow(vsIndex, port, rowIndex, vsName);
				}
			});
		}		
	},		
	
	loadConnHistoryInfo : function(vsIndex, vsName, rowIndex, interval)
	{
		this.selectedVsIndexList = vsIndex;
		this.selectedVsNameList = vsName;
		with (this)
		{
//			if (!adc || !vsIndex || !selectedSearchTimeMode)
			if (!vsIndex)
			{
				return;
			}
			var params = {
//				"adcObject.category"			: adcSetting.getSelectIndex(),
//				"adcObject.index"				: 2, //adc.index,			
//				"adcObject.name"				: '192.168.200.12', //adc.name,
//				"vsIndex"						: vsIndex
				"adcObject.category"			: target.level,
				"adcObject.index"				: target.index,
				"adcObject.strIndex"			: vsIndex,
				"adcObject.name"				: vsName
			};
//			if (0 === port || port)
//			{
//				params["svcPort"] = port;
//			};
//			params['preCompare'] = preVal;
			ajaxManager.runJsonExt({
				url			: "monitor/loadMulitConnInfo.action",
				data		: params,
				successFn	: function(data)
				{					
					GenerateSvcMultiPerfomanceConnChart(data);				
					loadConnMaxAvgHistoryInfo(vsIndex, vsName, undefined);
					if($('input[name="bpsValChk"]').is(':checked') == true)
					{
						GenerateSvcMultiPerfomanceBpsChart(data, interval);
						loadBpsMaxAvgHistoryInfo(vsIndex, vsName, undefined);	
					}
				},
				completeFn	: function(data)
				{	
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_SVCPERFOM_LAOD, jqXhr);
//					selectSvcPerfMonitorTableRow(vsIndex, port, rowIndex, vsName);
				}
			});
		}		
	},
	
	loadConnMaxAvgHistoryInfo : function(vsIndex, vsName, rowIndex)
	{
		this.selectedVsIndexList = vsIndex;
		this.selectedVsNameList = vsName;
		with (this)
		{
//			if (!adc || !vsIndex || !selectedSearchTimeMode)
			if (!vsIndex)
			{
				return;
			}
			var params = {
//				"adcObject.category"			: adcSetting.getSelectIndex(),
//				"adcObject.index"				: 2, //adc.index,			
//				"adcObject.name"				: '192.168.200.12', //adc.name,
//				"vsIndex"						: vsIndex
				"adcObject.category"			: target.level,
				"adcObject.index"				: target.index,
				"adcObject.strIndex"			: vsIndex,
				"adcObject.name"				: vsName
			};
//			if (0 === port || port)
//			{
//				params["svcPort"] = port;
//			};
//			params['preCompare'] = preVal;
			
			ajaxManager.runHtmlExt({
				url			: "monitor/loadMultiConnMaxAvgInfo.action",
				data		: params,
				target		: "table.connAvgMax",	
				successFn	: function(data)
				{								
//					selectSvcPerfMonitorTableRow(vsIndex, port, rowIndex, vsName);	
					
//					registerConnEvent(bpsVal, connVal);
				},
				completeFn	: function(data)
				{	
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_SVCPERFOM_LAOD, jqXhr);
//					selectSvcPerfMonitorTableRow(vsIndex, port, rowIndex, vsName);
				}
			});
		}		
	},
	registerEvent : function()
	{				
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
		html += itemName;		
		$tbody.html(html);
	},
	
	registerConnEvent : function()
	{
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
		
		html += itemName;		
		$tbody.html(html);
	},
	// Select 테이블 Row
	selectSvcPerfMonitorTableRow : function(vsIndex, port, rowIndex, vsName)
	{
		var vsIp = '';
		var vsPort = '';
		$('#serviceListTable tbody tr').removeClass("vsMonitorRowSelection");		
		$('#serviceListTable tbody tr').each(function(index) {
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
	},
	/// bps Connection Chart Generate
	GenerateSvcMultiPerfomanceBpsChart : function(data, interval)
	{
		with(this)
		{	
			var chartData = [];
			var chartDataList = [];
			if (data.bpsConnInfoData.bpsConn != null)
			{
				chartDataList = data.bpsConnInfoData.bpsConn;
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
						var Value1 = undefined;				    
						var Value2 = undefined;
						var Value3 = undefined;
						var Value4 = undefined;
						var Value5 = undefined;
						var Value6 = undefined;
						var Value7 = undefined;
						var Value8 = undefined;
						var Value9 = undefined;
						var Value10 = undefined;
//						var ValueName1 = column.name1;
//						var ValueName2 = column.name2;
//						var ValueName3 = column.name3;
//						var ValueName4 = column.name4;
//						var ValueName5 = column.name5;
//						var ValueName6 = column.name6;
//						var ValueName7 = column.name7;
//						var ValueName8 = column.name8;
//						var ValueName9 = column.name9;
//						var ValueName10 = column.name10;
						var ValueName1 = undefined;
						var ValueName2 = undefined;
						var ValueName3 = undefined;
						var ValueName4 = undefined;
						var ValueName5 = undefined;
						var ValueName6 = undefined;
						var ValueName7 = undefined;
						var ValueName8 = undefined;
						var ValueName9 = undefined;
						var ValueName10 = undefined;
						
				        for (var j = 0; j < column.bpsInValue.length; j ++)
				        {
				        	if (column.bpsInValue[j] > -1 && column.bpsInValue[j] != null)
				        	{
				        		bpsVal = column.bpsInValue[j];				 				        		
				        		eval('Value'+(j+1)+' = bpsVal;');
				        	}
				        	
//				        	if (column.bpsInValue[1] > -1 && column.bpsInValue[1] != null)
//							{
//				        		Value1 = column.bpsInValue[1];
//							}	
//				        	if (column.bpsInValue[2] > -1 && column.bpsInValue[2] != null)
//							{
//				        		Value2 = column.bpsInValue[2];
//							}
//				        	if (column.bpsInValue[3] > -1 && column.bpsInValue[3] != null)
//							{
//				        		Value3 = column.bpsInValue[3];
//							}
//				        	if (column.bpsInValue[4] > -1 && column.bpsInValue[4] != null)
//							{
//				        		Value4 = column.bpsInValue[4];
//							}
//				        	if (column.bpsInValue[5] > -1 && column.bpsInValue[5] != null)
//							{
//				        		Value5 = column.bpsInValue[5];
//							}
				        }	
				        
				        for (var k = 0; k < column.name.length; k++)
				        {
				        	if(column.name[k] != "" && column.name[k] != null)
				        	{
				        		bpsConnName = column.name[k];
				        		eval('ValueName'+(k+1)+' = bpsConnName;');
				        	}
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
							seventhValue : Value7,
							eighthValue : Value8,
							ninthValue : Value9,
							tenthValue : Value10,
							firstName : ValueName1,
							secondName : ValueName2,
							thirdName : ValueName3,
							fourthName : ValueName4,
							fifthName : ValueName5,
							sixthName : ValueName6,
							seventhName : ValueName7,
							eighthName : ValueName8,
							ninthName : ValueName9,
							tenthName : ValueName10
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
				 linecolor2 : "#fbc51a",
				 linecolor3 : "#d65f3d",
				 linecolor4 : "#976e96",
				 linecolor5 : "#fb8e33",
				 linecolor6 : "#9cc239",
				 linecolor7 : "#998c57",
				 linecolor8 : "#d987ad",
				 linecolor9 : "#557aa4",
				 linecolor10 : "#d8aa3a",
				 chartname : "SvcPerfBpsChart",
				 axistitle : "bps",
				 maxPos : null,
				 cursorColor : "#0f47c7",
				 interval : interval
			};
			obchart.OBDashboardGroupChartViewer(chartData, chartOption);
        }			
	},
	GenerateSvcMultiPerfomanceConnChart : function(data, preVal, connVal, interval)
	{
		with(this)
		{	
			var chartData = [];
			var chartDataList = [];
			if (data.bpsConnInfoData.bpsConn != null)
			{
				chartDataList = data.bpsConnInfoData.bpsConn;
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
						var Value1 = undefined;				    
						var Value2 = undefined;
						var Value3 = undefined;
						var Value4 = undefined;
						var Value5 = undefined;
						var Value6 = undefined;
						var Value7 = undefined;
						var Value8 = undefined;
						var Value9 = undefined;
						var Value10 = undefined;
						var ValueName1 = undefined;
						var ValueName2 = undefined;
						var ValueName3 = undefined;
						var ValueName4 = undefined;
						var ValueName5 = undefined;
						var ValueName6 = undefined;
						var ValueName7 = undefined;
						var ValueName8 = undefined;
						var ValueName9 = undefined;
						var ValueName10 = undefined;
//						var ValueName1 = column.name1;
//						var ValueName2 = column.name2;
//						var ValueName3 = column.name3;
//						var ValueName4 = column.name4;
//						var ValueName5 = column.name5;
//						var ValueName6 = column.name6;
//						var ValueName7 = column.name7;
//						var ValueName8 = column.name8;
//						var ValueName9 = column.name9;
//						var ValueName10 = column.name10;
						
				        for (var j = 0; j < column.connCurrValue.length; j ++)
				        {
				        	if (column.connCurrValue[j] > -1 && column.connCurrValue[j] != null)
				        	{
				        		connVal = column.connCurrValue[j];				 				        		
				        		eval('Value'+(j+1)+' = connVal;');
				        	}
				        	
//				        	if (column.bpsInValue[1] > -1 && column.bpsInValue[1] != null)
//							{
//				        		Value1 = column.bpsInValue[1];
//							}	
//				        	if (column.bpsInValue[2] > -1 && column.bpsInValue[2] != null)
//							{
//				        		Value2 = column.bpsInValue[2];
//							}
//				        	if (column.bpsInValue[3] > -1 && column.bpsInValue[3] != null)
//							{
//				        		Value3 = column.bpsInValue[3];
//							}
//				        	if (column.bpsInValue[4] > -1 && column.bpsInValue[4] != null)
//							{
//				        		Value4 = column.bpsInValue[4];
//							}
//				        	if (column.bpsInValue[5] > -1 && column.bpsInValue[5] != null)
//							{
//				        		Value5 = column.bpsInValue[5];
//							}
				        }	
				        
				        for (var k = 0; k < column.name.length; k ++)
				        {
				        	if (column.name[k] != "" && column.name[k] != null)
				        	{
				        		bpsConnName = column.name[k];				 				        		
				        		eval('ValueName'+(k+1)+' = bpsConnName;');
				        	}
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
							seventhValue : Value7,
							eighthValue : Value8,
							ninthValue : Value9,
							tenthValue : Value10,
							firstName : ValueName1,
							secondName : ValueName2,
							thirdName : ValueName3,
							fourthName : ValueName4,
							fifthName : ValueName5,
							sixthName : ValueName6,
							seventhName : ValueName7,
							eighthName : ValueName8,
							ninthName : ValueName9,
							tenthName : ValueName10
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
				 linecolor2 : "#fbc51a",
				 linecolor3 : "#d65f3d",
				 linecolor4 : "#976e96",
				 linecolor5 : "#fb8e33",
				 linecolor6 : "#9cc239",
				 linecolor7 : "#998c57",
				 linecolor8 : "#d987ad",
				 linecolor9 : "#557aa4",
				 linecolor10 : "#d8aa3a",
				 chartname : "SvcPerfConncurrChart",
				 axistitle : VAR_COMMON_AFEW,
				 maxPos : null,
				 cursorColor : "#0f47c7",
				 interval : interval
			};
			obchart.OBDashboardGroupChartViewer(chartData, chartOption);
        }		
	}
	
//	GenerateSvcMultiPerfomanceBpsChart : function(data)
//	{
//		with(this)
//		{	
//			var chartData = [];
//			var chartNameList = [];
//			var chartDataList = [];
//			if (data.bpsConnInfoData.curInfo != null)
//			{
//				chartNameList = data.bpsConnInfoData.curInfo;
//				chartDataList = data.bpsConnInfoData.bpsConn;
//			}		
//			if (chartNameList.length > 0 && chartNameList != null)
//			{
//				for ( var i = 0; i < chartNameList.length; i++)
//				{
//					for ( var j = 0; j < chartDataList.length; j++)
//					{
//						if (j == 0)
//						{
//							if (data.startTime < chartDataList[0].occurTime)
//							{
//								var startTime = parseDateTime(data.startTime);
//								chartData.push({occurredTime:startTime});
//							}
//						}
//						var column = chartDataList[j];
//						if (column)
//						{					
//							var date = parseDateTime(column.occurTime);						
//							var Value1 = undefined;				    
//							var Value2 = undefined;
//							var Value3 = undefined;
//							var Value4 = undefined;
//							var Value5 = undefined;
//							var Value6 = undefined;
//							var Value7 = undefined;
//							var Value8 = undefined;
//							var Value9 = undefined;
//							var Value10 = undefined;
//							var ValueName1 = chartNameList.name1;
//							var ValueName2 = chartNameList.name2;
//							var ValueName3 = chartNameList.name3;
//							var ValueName4 = chartNameList.name4;
//							var ValueName5 = chartNameList.name5;
//							var ValueName6 = chartNameList.name6;
//							var ValueName7 = chartNameList.name7;
//							var ValueName8 = chartNameList.name8;
//							var ValueName9 = chartNameList.name9;
//							var ValueName10 = chartNameList.name10;
//							
//					        for (var k = 0; k < column.bpsInValue.length; k ++)
//					        {
//					        	if (column.bpsInValue[k] > -1 && column.bpsInValue[k] != null)
//					        	{
//					        		bpsVal = column.bpsInValue[k];				 				        		
//					        		eval('Value'+(k+1)+' = bpsVal;');
//					        	}
//					        }						
//													
//							var dataObject =
//							{
//								occurredTime : date,
//								firstValue : Value1,							
//								secondValue : Value2,
//								thirdValue : Value3,
//								fourthValue : Value4,
//								fifthValue : Value5,
//								sixthValue : Value6,
//								seventhValue : Value7,
//								eighthValue : Value8,
//								ninthValue : Value9,
//								tenthValue : Value10,
//								firstName : ValueName1,
//								secondName : ValueName2,
//								thirdName : ValueName3,
//								fourthName : ValueName4,
//								fifthName : ValueName5,
//								sixthName : ValueName6,
//								seventhName : ValueName7,
//								eighthName : ValueName8,
//								ninthName : ValueName9,
//								tenthName : ValueName10
//							};						
//							chartData.push(dataObject);							
//						}
//						if (i == (chartDataList.length - 1))
//						{
//							if (data.endTime > chartDataList[chartDataList.length - 1].occurTime)
//							{
//								var endTime = parseDateTime(data.endTime);
//								chartData.push({occurredTime:endTime});								 
//							}
//						}
//					 }					
//				 }			
//				 
//			}
//			else
//			{
//				var startTime = parseDateTime(data.startTime);
//				var endTime = parseDateTime(data.endTime);
//				var dataObject =
//				{
//					occurredTime : startTime
//				};
//				chartData.push(dataObject);
//				var dataObject =
//				{
//					occurredTime : endTime
//				};
//				chartData.push(dataObject);
//			}		
//			
//			var chartOption =
//			{
//				 min : 0,
//				 max : null,
//				 linecolor1 : "#6cb8c8",
//				 linecolor2 : "#fbc51a",
//				 linecolor3 : "#d65f3d",
//				 linecolor4 : "#976e96",
//				 linecolor5 : "#fb8e33",
//				 linecolor6 : "#9cc239",
//				 linecolor7 : "#998c57",
//				 linecolor8 : "#d987ad",
//				 linecolor9 : "#557aa4",
//				 linecolor10 : "#d8aa3a",
//				 chartname : "SvcPerfBpsChart",
//				 axistitle : "bps",
//				 maxPos : null,
//				 cursorColor : "#0f47c7"
//			};
//			obchart.OBDashboardGroupChartViewer(chartData, chartOption);
//        }			
//	}
});