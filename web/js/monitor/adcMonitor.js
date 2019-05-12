var AdcMonitor = Class.create({
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
//		this.filter_data = undefined;
		this.orderDir = 1;  // 기본 default는 asc
		this.orderType = 1;
//		this.array_data = {};
//		this.filterData = {};
		this.pageNavi = new PageNavigator();
		this.pageNavi.onChange(function(fromRow, toRow, orderType, orderDir)
		{
			fn.loadAdcMonitoringTableInListContent(fn.searchedKey, fromRow, toRow, orderType, orderDir, fn.changeFilter(), fn.selectedCol());
		});
		this.target = {};
		this.columnSet = undefined;
		this.selectedSet = undefined;
		this.setFlag = undefined;
		this.searchFlag = false;
		
		this.monitorContentEventsInit = false;
//		this.filterIndex = undefined;
//		this.filterValue = undefined;
//		this.filterTitile = undefined;
//		this.filterIsSelect = undefined;
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
//			this.monitorContentEventsInit = false;
			loadListContent();			
		}
	},	
	loadListContent : function(searchKey, orderDir, orderType)
	{
		with(this)
		{
			this.monitorContentEventsInit = false;
			setTarget();			
			var rowTotal = 0;
			ajaxManager.runJsonExt({
				url : "monitor/retrieveAdcMonTotal.action", 
				data :
				{
					"adcScope.level" : target.level,
					"adcScope.index" : target.index,
					"searchKey" : searchKey,
//					"selectedVal"		: JSON.stringify(adcMonitor.selectedSet)	//filter_data
//					"adcCondition.status.filter.index" : filterIndex,
//					"adcCondition.status.filter.value" : filterValue,
//					"adcCondition.status.filter.title" : filterTitile,
//					"adcCondition.status.filter.isSelect" : filterIsSelect,
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
					loadAdcMonitorListContent(searchKey);
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_APPLIANCE_LAOD, jqXhr);
				}
			});
		}
	},
	
	loadAdcMonitorListContent : function(searchKey, fromRow, toRow)
	{	
		with(this)
		{		
//			if(val != null)
//			{
//				for ( var int = 0; int < val.length; int++) 
//				{
//					var array_data = val[1].split("||");
//					filterIndex = array_data[0];
//					filterTitile = array_data[1];
//					filterValue = array_data[2];					
//					filterIsSelect = array_data[3];
//				}
//			}
					
//			setTarget();
//			selColData = this.columnSet;
//			filterData = this.selectedSet;
			ajaxManager.runHtmlExt({
				url : "monitor/loadApplianceMonitorList.action",
				data : 
				{
					"adcScope.level" 	: target.level,					
					"adcScope.index"	: target.index,
					"searchKey" 		: searchKey == null ? undefined : searchKey,
					"fromRow"			: fromRow === undefined ? pageNavi.getFirstRowOfCurrentPage() : fromRow,
					"toRow"				: toRow === undefined ? pageNavi.getLastRowOfCurrentPage() : toRow,
					"orderDir"			: this.orderDir,
					"orderType"			: this.orderType,
//					"selectedVal"		: JSON.stringify(adcMonitor.selectedSet)	//filter_data
//					"selectedCol"		: selColData === undefined ? "-1" : JSON.stringify(selColData),	//column_isSelect_data
//					"selectedVal"		: filterData === undefined ? "-1" : JSON.stringify(filterData)	//column_isSelect_data
//					"adcCondition.status.filter.index" : filterIndex,
//					"adcCondition.status.filter.value" : filterValue,
//					"adcCondition.status.filter.title" : filterTitile,
//					"adcCondition.status.filter.isSelect" : filterIsSelect,
//					"selectedVal" : val,
//					"statusFilterString" : Object.toJSON(_getStatusFilter())
				},
				target : "#wrap .contents",				
				successFn : function(params)
				{
					header.setActiveMenu("MonitorAppliance");
					searchedKey = searchKey;
					
//					$selColData.each(function(index)
//					{
//						if ($(this).is(':checked'))
//							showHidenCol($('#adclistTable'), index, true);
//						else
//							showHidenCol($('#adclistTable'), index, false);
//					});
					
//					console.log('page column load!!', adcMonitor.columnSet);
//					console.log('page filter load!!', adcMonitor.selectedSet);
					if (adcMonitor.columnSet)
					{
						$.each(adcMonitor.columnSet, function(index, value)
						{
							showHidenCol($('#adclistTable'), index, value);
						});
					}
					
					if(adcMonitor.selectedSet)
					{
						var isChecked;
						$.each(adcMonitor.selectedSet, function(groupIndex, groupSet){
//							if(index == 'group1')
//							{
//								filterCheck(index, true);
//								console.log(index, value, value.length);
//								//group1 ["0|전체|-1|true", "1|정상|1|false", "2|단절|0|false", first: function, contains: function, page: function, where: function, propValues: function] 3
//							}
							$.each(groupSet, function(index, value)
							{
								isChecked = $.parseJSON(value.split('\|')[3]);
								if (isChecked)
									$('[data-group=' + groupIndex + '] input[name=selChk]:eq(' + index + ')').attr('checked', 'checked');
								else
									$('[data-group=' + groupIndex + '] input[name=selChk]:eq(' + index + ')').removeAttr('checked');
							});
						});
						changeFilter();
						
						var isFlag= adcMonitor.setFlag;
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
						
						loadRefreshAdcMonListContent(searchKey, orderDir, orderType);
					}
					
					noticePageInfo();
					registerEvents();
					msgViewEvents();
				},
				completeFn : function()
				{
					pageNavi.refresh();
				},
				errorFn : function(jqXhr)
				{
//					$.obAlertAjaxError(VAR_APPLIANCE_LAOD, jqXhr);
					$.obAlertAjaxError(VAR_APPLIANCE_LAOD, jqXhr);
				}
			});
		}
	},
	
//	filterCheck : function(dataGroup, show)
//	{		
//		if($('.chklist').data('group') == dataGroup)
//		{
//			$('.chkFilter').find('input').attr('checked', 'checked');
//		}
//	},
	loadRefreshAdcMonListContent : function(searchKey, orderDir, orderType)
	{
		with(this)
		{
			var filterData = changeFilter();
			var selColData = selectedCol();
			var rowTotal = 0;
			
			if(!validateDaterefresh())
			{
				return false;
			}
			
			ajaxManager.runJsonExt({
				url : "monitor/retrieveAdcMonTotal.action", 
				data :
				{
					"adcScope.level" 	: target.level,					
					"adcScope.index"	: target.index,
					"searchKey" 		: searchKey == null ? undefined : searchKey,
					"orderDir"			: this.orderDir,
					"orderType"			: this.orderType,
					"selectedVal"		: JSON.stringify(filterData),	//filter_data
					"selectedCol"		: JSON.stringify(selColData)	//column_isSelect_data
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
					loadAdcMonitoringTableInListContent(searchKey, undefined, undefined, undefined, undefined, filterData, selColData);
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_APPLIANCE_LAOD, jqXhr);
				}
			});
		}
	},
	
	loadAdcMonitoringTableInListContent : function(searchKey, fromRow, toRow, orderDir, orderType, filterData, selColData)
	{
		//alert(array_data); //["0||전체||-1||false","1||정상||1||true","2||단절||0||false"]
//		console.log(filterData);
//		console.log(selColData);
//		if(filterData != null)
//			adcMonitor.changeFilter();
//		var filter_data = {};
//		if(filterData != undefined)
//			filter_data = JSON.stringify(filterData);
		with(this)
		{
			ajaxManager.runJsonExt({
				url : "monitor/loadAdcMonitoringTableInListContent.action",
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
					"selectedCol"		: JSON.stringify(selColData)	//column_isSelect_data
				},
				successFn : function(data)
				{
//					console.log(data);
					noticePageInfo();
					searchedKey = searchKey;
					refreshSelectFilterList();
					refreshHeaderSort($('#adclistTable'), orderType, orderDir); 
					pagePrevNextChange(data);
					msgViewEvents();
				},
//				completeFn : function()
//				{
//					pageNavi.refresh();
//				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_APPLIANCE_LAOD, jqXhr);
				}
			});
		}
	},
	
	pagePrevNextChange : function(data)
	{
		var rowTemplate = $('#adclistTable tbody .ContentsLine1.none').clone().removeClass('none');
//		var divideLineTemplate = '<tr class="DivideLine"><td colspan="20"></td></tr>';
//		var endLineTemplate = '<tr class="EndLine"><td colspan="20"></td></tr>';
//		rowTemplate.find('td').text('');
		
		$('#adclistTable tbody tr:not(.none)').remove();
		//console.log(rowTemplate, data);
		var row;
//		row = rowTemplate.clone();
		$.each(data.montotalAdc.adcList, function(index, rowData)
		{
			row = rowTemplate.clone();
			$.each(rowData, function(key, value)
			{
				var html = "";
				if(key == "status")
				{
					if(value == 1)
					{
						html += ('<img src="imgs/icon/icon_1d_conn.png" alt="available" />');
					}
					else if(value == 0)
					{
						html += ('<img src="imgs/icon/icon_1d_disconn.png" alt="unavaiulable" />');
					}
					else
					{
						html += ('<img src="imgs/icon/icon_1d_1.png" />');
					}
						
					row.find('td.' + key).empty().append(html);	
				}
				else if(key == "adcName")
				{
					html += '<a href="javascript:;" class="adcMonitoringLnk"><span style="display:none;">' + rowData.adcIndex + '</span>' + value + '</a>';
					row.find('td.' + key).empty().append(html);
				}
				else if(key == "activeBackupState")
				{
					if(value == 1)
					{
						html += ('<img src="imgs/icon/icon_active_4.png" alt="active" />&nbsp;Active');
					}
					else if(value == 2)
					{
						html += ('<img src="imgs/icon/icon_standby_4.png" alt="standby" />&nbsp;Standby');
					}
					else if(value == 0)
					{
						html += ('<img src="imgs/icon/icon_alone_4.png" alt="alone" />&nbsp;Unknown');
					}
					else
					{
						html += ('<img src="imgs/icon/icon_alone_4.png" alt="alone" />&nbsp;Unknown');
					}
						
					row.find('td.' + key).empty().append(html);	
				}
				else if(key == "adcType")
				{
					if(rowData.adcType == 1)
					{
						html += '<img src="imgs/icon/adc/icon_f5_s.png" alt="f5" />';
					}
					else if(rowData.adcType == 2)
					{
						html += '<img src="imgs/icon/adc/icon_alteon_s.png" alt="alteon" />';
					}
					else if(rowData.adcType == 3 || rowData.adcType == 4 || rowData.adcType == -2)
					{
						html += '<img src="imgs/icon/adc/icon_piolink_s.png" alt="PiolinkUnknown" />';
					}
					else
					{
					}
					row.find('td.' + key).empty().append(html);	
				}
				else if(key == "model")
				{
					if(value != null)
					{
						html += rowData.model;
					}
					else
					{
						html += '-';
					}
					row.find('td.' + key).empty().append(html);	
				}
				else if(key == "throughput")
				{					
					if(value == -1)
					{ 
						html += '-';
					}
					else
					{
//						html += '<a href="javascript:;" class="serviceMonitoringLnk"><span style="display:none;">' + rowData.adcIndex + '</span><span style="display:none;">2</span>' + rowData.throughputUnit + '</a>';
						html += rowData.throughputUnit;
					}
					row.find('td.' + key).empty().append(html);
				}
				else if(key == "concurrentSession")
				{
					if(value == -1)
					{ 
						html += '-';
					}
					else
					{
//						html += '<a href="javascript:;" class="serviceMonitoringLnk"><span style="display:none;">' + rowData.adcIndex + '</span><span style="display:none;">2</span>' + rowData.concurrentSessionUnit + '</a>';
						html += rowData.concurrentSessionUnit;
					}
					row.find('td.' + key).empty().append(html);
				}
				else if(key == "uptimeAge")
				{
					if (value != null)
					{
						html += addComma(value);
					}
					else
					{
						html += '-';
					}
					row.find('td.' + key).empty().append(html);
				}
//				else if(key == "adcIp")
//				{
//					html += '<a href="javascript:;" class="adcMonitoringLnk"><span style="display:none;">' + rowData.adcIndex + '</span>' + value+ '</a>';
//					row.find('td.' + key).empty().append(html);
//				}
				else if(key == "cpu" || key == "memory" || key == "errorPackets" || key == "dropPackets" || key == "sslTransaction" || key == "httpRequest"
					|| key == "interfaceAvailable" || key == "filterUse" || key == "serviceAvailable")
				{
					if(value == -1)
					{ 
						html += '-';
					}
					else
					{
//						html += '<a href="javascript:;" class="adcMonitoringLnk"><span style="display:none;">' + rowData.adcIndex + '</span>' + addComma(value) + '</a>';
						html += addComma(value);
					}
					row.find('td.' + key).empty().append(html);
				}			
				else if(key == "sslCertValidDays")
				{
//					console.log(rowData.adcType);
					if(rowData.adcType == 1)
					{
						html += value;
					}
					else
					{
						html += '-';
					}
					row.find('td.' + key).empty().append(html);
				}
				else if(key == "configTime")
				{
					if(value != null)
					{
						var configTime = value.split("T");
//						var date1 = dateSum[0];
//						var date2 = dateSum[1];
	//					html += parseDateTime(value);
						html += configTime[0] + ' ' + configTime[1];
					}
					else
					{
						html += '-';
					}
					row.find('td.' + key).empty().append(html);
				}
//				else if(key == "interfaceAvailable")
//				{
//					html += '<a href="javascript:;" class="interfaceLnk"><span style="display:none;">' + rowData.adcIndex + '</span>' + rowData.interfaceAvailable + '</a>';
//					row.find('td.' + key).empty().append(html);
//				}
//				else if(key == "filterUse")
//				{
//					html += '<a href="javascript:;" class="flbInfoLnk"><span style="display:none;">' + rowData.adcIndex + '</span>' + rowData.filterUse + '</a>';
//					row.find('td.' + key).empty().append(html);
//				}
//				else if(key == "serviceAvailable")
//				{					
//					html += '<a href="javascript:;" class="serviceMapLnk"><span style="display:none;">' + rowData.adcIndex + '</span>' + rowData.serviceAvailable + '</a>';
//					row.find('td.' + key).empty().append(html);
//				}
				else if(key == "adcLog24Hour")
				{
					if(value > 0)
						html += '<a href="javascript:;" class="adcLogLnk"><span style="display:none;">' + rowData.adcIndex + '</span>' + addThousandSeparatorCommas(value) + '</a>';
					else
						html += 0;
					row.find('td.' + key).empty().append(html);
				}
//				else if(key == "slbConfig24Hour")
//				{
//					html += '<a href="javascript:;" class="slbHistoryLnk"><span style="display:none;">' + rowData.adcIndex + '</span>' + value + '</a>';
//					row.find('td.' + key).empty().append(html);
//				}
				else
				{
					row.find('td.' + key).text(value);
				}
//				row.find('td.' + key).text(value);
			});
			$('#adclistTable tbody:first').append(row);
			
//			$('#adclistTable tbody:first').append(divideLineTemplate);
		});
		
//		$('#adclistTable tbody').append(endLineTemplate);		
	},
	// 전체/그룹 선택에 따른 네임 표현
	selectedNameFill : function()
	{
//		with(this)
//		{
//			var titleHeader = $('.title_name_area').filter(':last');
//			if()
//		}
	},
	
	// Selected Filter 
	refreshSelectFilterList : function () 
	{
		var filterBoard = $('.Filter_Board');
		var selectFilterList = $('.selectFilterList'); // 필터리스트 컨테이너
		var sel_filterTemplate = selectFilterList.find('.sel_filter:first'); // 템플릿
		selectFilterList.find('.sel_filter:not(.none)').remove(); // 기존 리스트 초기화
		$('.Filter_Board .chkFilterOption td:not(.none) .chkFilter input[type=checkbox]:checked').each(function(index) {
			var sel_filter = sel_filterTemplate.clone().attr('data-parent-id', $('.chkFilter input[type=checkbox]').index($(this)));
		    var filterLabel = filterBoard.find('thead th').eq(filterBoard.find('tbody tr:first td').index($(this).parents('td'))).text();
		    sel_filter.find('a').text(filterLabel + ':' + $(this).parent().find('label').text());
		    selectFilterList.append(sel_filter.removeClass('none'));
		});
		
		if ($('.sel_filter:not(.none)').length < 1)
		{
			$('.selectedOptionFilter').addClass('none');
		}
		else
		{
			$('.selectedOptionFilter').removeClass('none');
		}
	},
		
	// Sorting
	refreshHeaderSort : function (table, orderType, orderDir) 
	{
		var header = table.find('.ContentsHeadLine');
		header.find('.orderDir').text('2');
		header.find('img').attr('src', 'imgs/none.gif');
		
		var changedHeader = header.find('.orderType').filter(function() {
			return $(this).text() == orderType;
		}).parent();

		var nextDir = orderDir == 1 ? 2 : 1;
//			if(orderDir == 1)
//			{
//				changedHeader.find('img').attr('src', 'imgs/Asc.gif');
//				changedHeader.find('.orderDir').text(nextDir);
//			}
//			else
//			{
//				changedHeader.find('img').attr('src', 'imgs/Desc.gif');
//				changedHeader.find('.orderDir').text(nextDir);
//			}
			
			changedHeader.find('img').attr('src', adcMonitor.getSortImg(orderDir));
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
			$('.dataNotExistMsg').addClass("none");
			if($('.adcList').size() > 1)
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
			if($('.adcList').size() > 1)
			{
				$('.dataNotExistMsg').addClass("none");
			}
			else
			{
				$('.dataNotExistMsg').removeClass("none");
			}
		}
	},
	registerEvents : function()
	{
		with(this)
		{			
			$('.F5vender:first').removeClass('none');
			$('.Alteonvender:first').removeClass('none');
			$('.PioLinkvender:first').removeClass('none');
			
			$('.F5version:first').removeClass('none');
			$('.Alteonversion:first').removeClass('none');
			$('.PioLinkversion:first').removeClass('none');
			
			$('input[name="searchKey"]').attr("placeholder", "ADC Name");
			
			$('.btn a.searchLnk').click(function(e)
			{
				e.preventDefault();
//				var searchKey = $('input[name="searchKey"]').val();
//				loadAdcMonitoringTableInListContent(searchKey);
				var searchKey = $('input[name="searchKey"]').val();
				//loadAdcMonitoringTableInListContent(searchKey, "", "", orderDir, orderType);	
				searchFlag = true;
				loadRefreshAdcMonListContent(searchKey, orderDir, orderType);
			});
			
			$('.inputTextposition1 input.searchTxt').keydown(function(e)
			{
				if(e.which != 13)
					return;
				
				var searchKey = $(this).val();
//				loadAdcMonitoringTableInListContent(searchKey);
				searchFlag=true;
				loadRefreshAdcMonListContent(searchKey, orderDir, orderType);
			});
						
			$('.orderHeader').click(function(e)
			{
				e.preventDefault();
				orderDir = $(this).find('.orderDir').text();
				orderType = $(this).find('.orderType').text();
				var searchKey = $('input[name="searchKey"]').val();				
//				loadAdcMonitoringTableInListContent(searchKey, "", "", orderDir, orderType, changeFilter());
				searchFlag = true;
				loadRefreshAdcMonListContent(searchKey, orderDir, orderType);
				
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
		
			$('#exportLnk').click(function(e)
			{
				e.preventDefault();
				var searchKey = $('input[name="searchKey"]').val();
				_checkExportAdcMonDataExist(searchKey);
//				exportLnk(searchKey);
			});
			
			$('#refreshLnk').click(function(e)
			{
//				loadListContent();
				$('input[name="searchKey"]').val('');
				loadRefreshAdcMonListContent(null, orderDir, orderType);
			});	
						
			// 선택해제
//			$('.btn_desel').click(function(e)
			$('.selectFilter').on('click', '.deselBtn', function(e)
			{
				$('.chkFilter').children().removeAttr('checked');
				$('.chkFilter').removeClass('on');
				
				
//				if ($(this).find('input').not(':checked').val() != undefined)
//				{
//					$(this).parent().find('.chkFilterAll').children().attr('checked', 'checked');
//				}
//				else
//				{
//					$(this).parent().find('.chkFilterAll').children().removeAttr('checked');
//				}
				$('.chkFilterAll').find('input').attr("checked", "checked");
//				changeGroup($('.chkFilter').parent());
				
//				var filterDataa = {};
//				filterDataa = changeFilterDesel();
				
//				if($('.chkFilterAll').find('input').not(':checked').length == 0)
//				{
//					$('.selectedOptionFilter').addClass("none");
//				}
//				else
//				{
//					$('.selectedOptionFilter').removeClass("none");					
//				}
				
//				adcMonitor.loadAdcMonitoringTableInListContent($('input[name="searchKey"]').val(),"","","","",changeFilter(), selectedCol());		
				loadRefreshAdcMonListContent($('input[name="searchKey"]').val(), "", "", changeFilter(), selectedCol());
			});
			
			// 선택된 필터 삭제
			$('.selectFilterList').on('click', '.delBtn', function(e)
			{
				$('.chkFilter input[type=checkbox]').eq($(this).parent().data('parent-id')).removeAttr('checked');				
//				var filterDataa = {};
//				filterDataa = changeFilter();
				
//				if($('.chkFilterAll').find('input').not(':checked').length == 0)
//				{
//					$('.selectedOptionFilter').addClass("none");
//				}
//				else
//				{
//					$('.selectedOptionFilter').removeClass("none");					
//				}
				
//				adcMonitor.loadAdcMonitoringTableInListContent($('input[name="searchKey"]').val(),"","","","",changeFilter(), selectedCol());
				loadRefreshAdcMonListContent($('input[name="searchKey"]').val(), "", "", changeFilter(), selectedCol());
			});
			
//			$('.inputSelect').change(function(e)
//			{
//				var filterData = {};
//				var param;
//				$('.inputSelect').each(function(index) {
//					param = [];
//					$(this).find('option').each(function(index)
//					{
//						param.push([index, $(this).text(), $(this).val(), $(this).is(':selected')].join('|'));
//					});					  
//					console.log(param);
//					  
//					filterData[$(this).data('group')] = param; // 셀렉트박스의 data-group 속성을 읽어 해당 키에 배치
//				});
//								
//				var searchKey = $('input[name="searchKey"]').val();
//				adcMonitor.loadAdcMonitoringTableInListContent(searchKey,"","","","",filterData);
//				
//			});
									
			$('.chkFilter').change(function(e)
			{
//				var filterData = {};
//				var param;
//				$('.chklist').each(function(index) {
//					param = [];
//					
//					$(this).find('input').each(function(index)
//					{
//						index =$(this).parent().find('span').text();
//						param.push([index, $(this).parent().find('label').text(), $(this).val(), $(this).is(':checked')].join('|'));
////						console.log(param);						
//					});				
////					console.log(param);
//					filterData[$(this).data('group')] = param;					
//				});
					
				changeGroup($(this).parent());
//				if ($(this).find('input').not(':checked').val() != undefined)
//				{
//					$(this).parent().find('.chkFilterAll').children().attr('checked', 'checked');
//				}
//				else
//				{
//					$(this).parent().find('.chkFilterAll').children().removeAttr('checked');
//				}
				
//				var filterDataa = {};
//				filterDataa = changeFilter();
								
//				if($('.chkFilterAll').find('input').not(':checked').length == 0)
//				{
//					$('.selectedOptionFilter').addClass("none");
//				}
//				else
//				{
//					$('.selectedOptionFilter').removeClass("none");					
//				}
				
//				adcMonitor.loadAdcMonitoringTableInListContent($('input[name="searchKey"]').val(),"","","","",changeFilter());
				loadRefreshAdcMonListContent($('input[name="searchKey"]').val(), "", "", changeFilter(), selectedCol());
				
				
			});	

			$('.chkFilterUptime').click(function(e)
			{				
				$('.chkFilterUptime').removeClass('on');
				$(this).addClass('on');	
				
				var isClick= $(this).attr("isClick");
				if(isClick == 0)
				{
					$(this).attr("isClick", "1");
					$(this).addClass('on');	
				}
				else
				{
					$(this).attr("isClick", "0");
					$(this).removeClass('on');	
				}
				
				$('.chkFilterUptime:not(.on)').each(function()
				{
					$(this).attr("isClick", "0");
					$(this).removeClass('on');
				});
				
//				$(this).parent().find('a').each(function()
//				{
//					if($(this).parent().hasClass('on') == true)
//					{
//						$(this).parent().attr("isClick", "1");
//						$(this).parent().addClass('on');	
//					}
//					else
//					{
//						$(this).parent().attr("isClick", "0");
//						$(this).parent().removeClass('on');	
//					} 
//				});
						
				$(this).parent().find('input[type=checkbox]').each(function() 
				{
					if($(this).parent().hasClass('on') == true)
					{
						$(this).attr("checked", "checked");
//						$(this).parent().attr("isClick", "1");
//						$(this).parent().addClass('on');	
					}
					else
					{
						$(this).removeAttr("checked");
//						$(this).parent().attr("isClick", "0");
//						$(this).parent().removeClass('on');	
					} 
				});	
				
//				if($('.chkFilterAll').find('input').not(':checked').length == 0)
//				{
//					$('.selectedOptionFilter').addClass("none");
//				}
//				else
//				{
//					$('.selectedOptionFilter').removeClass("none");					
//				}
				
//				var filterDataa = {};
//				filterDataa = changeFilter();				
//				adcMonitor.loadAdcMonitoringTableInListContent($('input[name="searchKey"]').val(),"","","","",changeFilter());
				loadRefreshAdcMonListContent($('input[name="searchKey"]').val(), "", "", changeFilter(), selectedCol());
			});
			
			$('.chkFilterSsl').click(function(e)
			{				
				$('.chkFilterSsl').removeClass('on');
				$(this).addClass('on');
				
				var isClick= $(this).attr("isClick");
				if(isClick == 0)
				{
					$(this).attr("isClick", "1");
					$(this).addClass('on');	
				}
				else
				{
					$(this).attr("isClick", "0");
					$(this).removeClass('on');	
				}
				
				$('.chkFilterSsl:not(.on)').each(function()
				{
					$(this).attr("isClick", "0");
					$(this).removeClass('on');
				});
				
				$(this).parent().find('input[type=checkbox]').each(function() 
				{
					if($(this).parent().hasClass('on') == true)
					{
						$(this).attr("checked", "checked");
					}
					else
					{
						$(this).removeAttr("checked");
					} 
				});
				
//				if($('.chkFilterAll').find('input').not(':checked').length == 0)
//				{
//					$('.selectedOptionFilter').addClass("none");
//				}
//				else
//				{
//					$('.selectedOptionFilter').removeClass("none");					
//				}
								
//				var filterDataa = {};
//				filterDataa = changeFilter();
				
//				adcMonitor.loadAdcMonitoringTableInListContent($('input[name="searchKey"]').val(),"","","","",changeFilter());
				loadRefreshAdcMonListContent($('input[name="searchKey"]').val(), "", "", changeFilter(), selectedCol());
			});
			
			$('#selectColumn_pop').click(function (e)
			{
				e.preventDefault();		
				adcMonitor._loadSelColPop();			
			});
			
			//link
//			$('.adcMonitoringLnk').click(function(e)
			$('#adclistTable').on('click', '.adcMonitoringLnk', function(e)
			{
//				adcSetting.loadContent();	
//				monitorAppliance.loadApplianceMonitorContent();
//				adcSetting.setObjOnAdcChange(monitorAppliance);
//				header.setActiveMenu('MonitorAppliance');
//				e.preventDefault();
//				var adcSetting = 
//				{
//					index : $(this).children('span').text()
//				};		
				
//				monitorAppliance.loadContent(adcSetting);
				e.preventDefault();
				with (this) 
				{
					var adcIndex = $(this).children('span').text();
					adcSetting._selectAdc(adcIndex);
					adcSetting.setSelectIndex(2);	
					monitorAppliance.loadApplianceMonitorContent(adcSetting.getAdc());
//					$('.monitorApplianceMnu').click();
				}
			});
			
//			$('.serviceMonitoringLnk').click(function(e)
			$('#adclistTable').on('click', '.serviceMonitoringLnk', function(e)
			{
				e.preventDefault();
				with (this) 
				{
					var adcIndex = $(this).children('span:first').text();
					var adcCategory = $(this).children('span:nth-child(2)').text();
					adcSetting._selectAdc(adcIndex);
					adcSetting.setSelectIndex(adcCategory);
					monitorServicePerfomance.loadServicePerfomanceContent(undefined, undefined, adcSetting.getAdc());
//					monitorServicePerfomance.loadContent(adcSetting.getAdc(), undefined, undefined);
				}				
			});	
			
//			$('.interfaceLnk').click(function(e)
			$('#adclistTable').on('click', '.interfaceLnk', function(e)
			{
				e.preventDefault();
				with (this) 
				{
					var adcIndex = $(this).children('span').text();
					adcSetting._selectAdc(adcIndex);
					statistics.loadStatisticsContent (adcSetting.getAdc());
				}				
			});
			
			$('#adclistTable').on('click', '.serviceMapLnk', function(e)
			{
				e.preventDefault();
				with(this)
				{
					var adcIndex = $(this).children('span').text();
					adcSetting._selectAdc(adcIndex);
					networkMap.loadNetworkMapContent(adcSetting.getAdc());
				}
			});
			
			$('#adclistTable').on('click', '.flbInfoLnk', function(e)
			{
				e.preventDefault();
				with(this)
				{
					var adcIndex = $(this).children('span').text();
					adcSetting._selectAdc(adcIndex);
					flbInfo.loadListContent(adcSetting.getAdc());
				}
			});
			
			$('#adclistTable').on('click', '.adcLogLnk', function(e)
			{
				e.preventDefault();
				with(this)
				{
					var adcIndex = $(this).children('span').text();
					adcSetting._selectAdc(adcIndex);
					adcSetting.setGroupIndex(undefined);
					$('.logMnu').click();
					$('.adcLogMnu').click();
//					adcLog.loadListContent(adcSetting.getAdc());
				}
			});
			
			$('#adclistTable').on('click', '.slbHistoryLnk', function(e)
			{
				e.preventDefault();
				with(this)
				{
					var adcIndex = $(this).children('span').text();
					adcSetting._selectAdc(adcIndex);
					adcHistory.loadListContent(adcSetting.getAdc());
				}
			});
		}
	},
	
	changeGroup : function(group) 
	{
		var chkFilterAll = group.find('.chkFilterAll');
//		var checkedFilter = group.find('.chkFilter');
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
				
//				adcMonitor.setFlag = $('#filterAdd').attr("isFlag");
			});
			
			adcMonitor.selectedSet = filterData;
				
			if ($('.chkFilter').find('input:checked').length > 0)
				adcMonitor.setFlag = $('#filterAdd').attr("isFlag");
			
			
//			console.log('page filter checked!!', adcMonitor.selectedSet);
			
//			this.selectedSet = filterData;
			return filterData;
		}
	},
	
	selectedCol : function()
	{
		with(this)
		{
			var selColData = [];
//			var param;
			
//			$('.ContentsHeadLine').each(function(index) {
//				param = [];
//				$(this).find('th').each(function(index)
//				{
////					index =$(this).parent().find('span').text();				
//					param.push([index, $(this).attr("isselect")]);
//				});					  		
//				selColData[$(this).children().eq(index).data('column')] = param; // 셀렉트박스의 data-group 속성을 읽어 해당 키에 배치				
//			});
			
//			$('.ContentsHeadLine th').each(function(index) {
//				param = [];
//				param.push([index, $(this).attr("isselect")]);	
//				selColData[$(this).data('column')] = param;
//			});
			
			$('#adclistTable .ContentsHeadLine th').each(function()
//			$('.selColHead th').each(function()
			{
			  selColData.push(!$(this).hasClass('none'));
			});
//			var colSize = $('.ContentsHeadLine th');
//			for ( var i = 0; i < $('.ContentsHeadLine th').length; i++) {
//				param = [];
//				param.push(colSize.eq(i).attr("isselect"));				
//			}
			  		
//			selColData[$(this).data('column')] = param; // 셀렉트박스의 data-group 속성을 읽어 해당 키에 배치				

//			console.log(selColData);
			
//			this.columnSet = selColData;
			return selColData;
		}
	},
	
//	changeFilterDesel : function()
//	{
//		with(this)
//		{
//			var filterData = {};
//			var param;
//			$('.chklist').each(function(index) {
//				param = [];
//				$(this).find('input').each(function(index)
//				{
//					index =$(this).parent().find('span').text();					
//					if(index == 0)
//					{
//						param.push([index, $(this).parent().find('label').text(), $(this).val(), "true"].join('|'));
//					}
//					else
//					{
//						param.push([index, $(this).parent().find('label').text(), $(this).val(), $(this).is(':checked')].join('|'));
//					}
//				});					  		
//				filterData[$(this).data('group')] = param; // 셀렉트박스의 data-group 속성을 읽어 해당 키에 배치				
//			});
//			
//			return filterData;
//		}
//	},
	_loadSelColPop : function()
	{
		with(this)
		{
			$('#selCol-popup table tbody').html('');
			$('#adclistTable .ContentsHeadLine').find('th').each(function()			
			{
				$('#selCol-popup table tbody').append(
				['<tr>',
					 '<td class="align_center">',
						 '<input name="colChk" class="colChk" type="checkbox"' + ($(this).is('.default') ? 'checked="checked" disabled="disabled"' : ($(this).is('.none') ? '' : 'checked="checked"')) + '>',
					 '</td>',
					 '<td class="align_left_P10">',
					 	$(this).find('a').contents().filter(function(){return this.nodeType === 3;}).text().trim()
					 	+ $(this).contents().filter(function(){return this.nodeType === 3;}).text().trim(),
					 '</td>',
				 '</tr>'].join(''));
			});
			
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
			if ($('.cloneDiv input[name="colChk"]').not(":checked").length < 1)
				$('.cloneDiv .allChk').attr("checked", "checked");
			
			$('.cloneDiv .allChk').click(function(e)
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
			
//			$('.cloneDiv').on('click', '.colChk', function(e)
			$('.cloneDiv .colChk').click(function(e)
			{
//				if ($('.cloneDiv .colChk:checked').length > 9)
//				{
//					alert('최대 선택 컬럼개수를 초과하였습니다.');
//					e.preventDefault();
//					return;
//				}
				checkedOptionCount();
				
				if ($('.cloneDiv input[name="colChk"]').not(":checked").length > 0)
					$('.allChk').removeAttr("checked");
				else
					$('.allChk').attr("checked", "checked");
			});
						
//			$('.cloneDiv').on('click', '.onOk', function(e)
			$('.cloneDiv .onOk').click(function(e)
			{
				e.preventDefault();
				
				$(this).parents('#selCol-popup').find('.colChk').each(function(index)
				{
					if ($(this).is(':checked'))
						showHidenCol($('#adclistTable'), index, true);
					else
						showHidenCol($('#adclistTable'), index, false);
				});
				
				adcMonitor.columnSet = selectedCol();
				adcMonitor.selectedSet = changeFilter();
//				console.log('ok column ~', adcMonitor.columnSet);
//				console.log('ok filter ~', adcMonitor.selectedSet);
				// select column true/false json
//				ajaxManager.runJsonExt({
//					url : "monitor/isSelectFileSave",				
//					data : 
//					{						
//						"selectedVal"		: JSON.stringify(filterData)
//					},
//					successFn : function(data)
//					{
//						
//					},
//					errorFn : function(a,b,c)
//					{
//						alert(VAR_COMMON_EXPDATAEXIST);
//					}
//				});
								
				loadRefreshAdcMonListContent($('input[name="searchKey"]').val(), "", "", changeFilter(), selectedCol());				
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
				
//		var $checkedCount = $('input[name="colChk"]:checked').length
//		var $totalCount = $('.cloneDiv .colChk').length; 
//		var $totalCount = 9; // 선택할 수 있는 최대 개수 8개
		var $checkedCount =$('.cloneDiv .colChk:checked').length;
		
		$optionCheckedCount.empty();
		$optionCheckedCount.html($checkedCount);
		$optiontotalCount.empty();
//		$optiontotalCount.html("/" +$totalCount);
		
//		if($checkedCount > 8)
//		{
//			alert("최대 선택할 수 있는 옵션 개수는 8개까지입니다.");
//			return;
//		}
	},
	showHidenCol : function(table, col, show)
	{			
		// colgroup check
		var colElem = $(table).find('colgroup').contents().filter(function() {
				return this.nodeType == 1 || this.nodeType == 8;
			})[col];
		
		var currentShow = colElem.nodeType;
		if ((show && currentShow == 1) || (!show && currentShow == 8))
			return;
		
		if (show)
		{
//			$('.Filter_Board [data-colidx=' + col + '] input[type=checkbox]').removeAttr('disabled');
//			$('.Filter_Board [data-colidx=' + col + '] a').removeClass('disabled');
			$('.Filter_Board [data-colidx=' + col + ']').removeClass('none');
			$(colElem).replaceWith(colElem.nodeValue);
		}
		else
		{
//			$('.Filter_Board [data-colidx=' + col + '] input[type=checkbox]').attr('disabled', 'disabled');
//			$('.Filter_Board [data-colidx=' + col + '] a').addClass('disabled');
			$('.Filter_Board [data-colidx=' + col + ']').addClass('none');
			$(colElem).replaceWith(document.createComment(colElem.outerHTML));
		}
				
		// 해당 행 표시상태 변경
		$(table).find('tr').each(function() 
		{
			if (show) 
			{
				$(this).find('th').eq(col).removeClass('none');
				$(this).find('td').eq(col).removeClass('none');
				
//				$(this).find('th').eq(col).attr("isSelect", "true");
		    } 
			else 
			{
				$(this).find('th').eq(col).addClass('none');
				$(this).find('td').eq(col).addClass('none');
				
//				$(this).find('th').eq(col).attr("isSelect", "false");
		    }
		});
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
	
	_checkExportAdcMonDataExist : function(searchKey)
	{
		with(this)
		{
			var filterData = changeFilter();
			var selColData = selectedCol();
			
			ajaxManager.runJsonExt({
				url : "monitor/checkExportAdcMonDataExist.action",				
				data : 
				{
					"adcScope.level" 	: target.level,					
					"adcScope.index"	: target.index,
					"searchKey" 		: searchKey == null ? undefined : searchKey,
//					"fromRow"			: fromRow === undefined ? pageNavi.getFirstRowOfCurrentPage() : fromRow,
//					"torRow"			: toRow === undefined ? pageNavi.getLastRowOfCurrentPage() : toRow,
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
			var params = "adcScope.level=" + target.level + "&adcScope.index=" + target.index + "&searchKey=" + encodeURIComponent($('.control_Board input.searchTxt').val()) + 
						"&selectedVal=" + JSON.stringify(filterData) + "&selectedCol=" + JSON.stringify(selColData);
			var url = "monitor/downloadAdcMonList.action?" + params;
			$('#downloadFrame').attr('src', url);
		}
	},
	
	_getStatusFilter : function()
	{
		return $('#status').map(function()
		{
			var arrayVal = $(this).val();
			var arrayData = arrayVal.split("||");
			
			var status_index = arrayData[0];
			var status_title = arrayData[1];
			var status_value = arrayData[2];
			var status_isSelect = "true";
				
			return {
				"index" : status_index,
				"value" : status_value,
				"title" : status_title,
				"isSelect" : status_isSelect
			};
		}).get();
	}
});