var RealServerMonitor = Class.create({
	initialize : function()
	{
		var fn = this;
		this.searchedKey = undefined;
		this.orderDir = 1;  // 기본 default는 asc
		this.orderType = 1;
		this.pageNavi = new PageNavigator();
		this.pageNavi.onChange(function(fromRow, toRow, orderType, orderDir)
		{
			fn.loadRealServerMonitoringTableInListContent(fn.searchedKey, fromRow, toRow, orderType, orderDir, fn.changeFilter(), fn.selectedCol());
		});
		this.target = {};
		this.columnSet = undefined;
		this.selectedSet = undefined;
		this.setFlag = undefined;
		this.searchFlag = false;
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
				url : "monitor/retrieveRealServerMonTotal.action",
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
					loadRealServerMonitorList(searchKey);
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_REALSERVER_LAODFAIL, jqXhr);
				}
			});
		}
	},
	loadRealServerMonitorList : function(searchKey, fromRow, toRow)
	{
		with(this)
		{
			ajaxManager.runHtmlExt({
				url : "monitor/loadRealServerMonitorList.action",
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
					header.setActiveMenu("MonitorRealServer");					
					searchedKey = searchKey;
					
					if(realServerMonitor.columnSet)
					{
						$.each(realServerMonitor.columnSet, function(index, value)
						{
							showHidenCol($('#realserverListTable'), index, value);
						});
					}
					
					if(realServerMonitor.selectedSet)
					{
						var isChecked;
						$.each(realServerMonitor.selectedSet, function(groupIndex, groupSet) {
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
						
						var isFlag= realServerMonitor.setFlag;
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
						
						loadRealServerRefreshMonListContent(searchKey, orderDir, orderType);
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
					$.obAlertAjaxError(VAR_REALSERVER_LAODFAIL, jqXhr);
				}
			});
		}
	},
	
	loadRealServerRefreshMonListContent : function(searchKey, orderDir, orderType)
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
				url : "monitor/retrieveRealServerMonTotal.action", 
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
					loadRealServerMonitoringTableInListContent(searchKey, undefined, undefined, undefined, undefined, filterData, selcolData);
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_REALSERVER_LAODFAIL, jqXhr);
				}
			});
		}
	},
	
	loadRealServerMonitoringTableInListContent : function(searchKey, fromRow, toRow, orderDir, orderType, filterData, selcolData)
	{
		with(this)
		{
			var i =0 ;
//			console.log('1: ' + i++);
			ajaxManager.runJsonExt({
				url : "monitor/loadRealServerMonitoringTableInListContent.action",
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
					
					refreshHeaderSort($('#realserverListTable'), orderType, orderDir); 
					pagePrevNextChange(data);
					msgViewEvents();
				},
				completeFn : function()
				{
					pageNavi.refresh();
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_REALSERVER_LAODFAIL, jqXhr);
				}
			});
		}
	},

	pagePrevNextChange : function(data)
	{
		var rowTemplate = $('#realserverListTable tbody .ContentsLine1.none').clone().removeClass('none');
//		var divideLineTemplate = '<tr class="DivideLine"><td colspan="9"></td></tr>';
//		var endLineTemplate = '<tr class="EndLine"><td colspan="9"></td></tr>';
//		rowTemplate.find('td').text('');
		
		$('#realserverListTable tbody tr:not(.none)').remove();
		//console.log(rowTemplate, data);
		var row;
//		row = rowTemplate.clone();
		$.each(data.montotalRs.realList, function(index, rowData)
		{
			row = rowTemplate.clone();
			$.each(rowData, function(key, value)
			{
				var html = "";
				if(key == "status")
				{
					if(value == -1)
					{
						html += ('<img src="imgs/icon/icon_2d_1.png" />');
					}
					else if(value == 1)
					{
						html += ('<img src="imgs/icon/icon_2d_1.png" alt="available" />');
					}
					else if(value == 0)
					{
						html += ('<img src="imgs/icon/icon_2d_2.png" alt="unavailable" />');
					}
					else if(value == 2)
					{
						html += ('<img src="imgs/icon/icon_2d_0.png" alt="disable" />');
					}
					else
					{
						html += ('<img src="imgs/icon/icon_2d_0.png" />');
					}
						
					row.find('td.' + key).empty().append(html);	
				}
				else if(key == "state")
				{
					if(value == 1)
					{
						html += 'Enabled';
					}
					else if(value == 0)
					{
						html += 'Disabled';
					}
					else
					{
						html += 'Forced Offline';
					}
					row.find('td.' + key).empty().append(html);
				}
				else if(key == "name")
				{
					if(value == "" || value == "null")
					{
						html += '-';	
					}
					else
					{
						html += value == null ? "-" : value;
					}
					
//					if(rowData.adcType == 1)
//					{
//						html += '-';
//					}
//					else
//					{
//						html += value;
//					}
					row.find('td.' + key).empty().append(html);
				}
				else if(key == "used")
				{
					if(value == 1)
					{
						html += VAR_REALSERVER_USED;
					}
					else
					{
						html += VAR_REALSERVER_NOTUSED;
					}
					row.find('td.' + key).empty().append(html);
				}
//				else if(key == "group")
//				{
//					html += '<a href="javascript:;" class="serviceMapLnk"><span style="display:none;">' + rowData.adcIndex + '</span>' + value + '</a>';
//					row.find('td.' + key).empty().append(html);
//				}
				else if(key == "adcName")// || key == "adcIp")
				{
					html += '<a href="javascript:;" class="adcMonitoringLnk"><span style="display:none;">' + rowData.adcIndex + '</span>' + value + '</a>';
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
					else if(rowData.adcType == 3 || rowData.adcType == 4)
					{
						html += '<img src="imgs/icon/adc/icon_piolink_s.png" alt="PiolinkUnknown" />';
					}
					else
					{						
					}
					row.find('td.' + key).empty().append(html);	
				}
				else if(key == "group" || key == "ratio")
				{
					if(value == -1)
					{ 
						html += '-';
					}
					else
					{
						html += addComma(value);
					}
					row.find('td.' + key).empty().append(html);
				}		
				else
				{
					row.find('td.' + key).text(value);
				}
			});
			$('#realserverListTable tbody:first').append(row);
			
//			$('#realserverListTable tbody:first').append(divideLineTemplate);
		});
		
//		$('#realserverListTable tbody').append(endLineTemplate);		
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
			if($('.realserverList').size() > 1)
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
			if($('.realserverList').size() > 1)
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
				if($('.realserverList').size() > 1)
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
			$('input[name="searchKey"]').attr("placeholder", "Real Server IP, Name");
			
			$('.btn a.searchLnk').click(function(e)
			{
				e.preventDefault();
				var searchKey = $('input[name="searchKey"]').val();
				searchFlag=true;
				loadRealServerRefreshMonListContent(searchKey, orderDir, orderType);
			});
			
			$('.inputTextposition1 input.searchTxt').keydown(function(e)
			{
				if(e.which != 13)
					return;
				
				var searchKey = $(this).val();
				searchFlag=true;
				loadRealServerRefreshMonListContent(searchKey, orderDir, orderType);
			});
			
			$('.orderHeader').click(function(e)
			{
				e.preventDefault();
				orderDir = $(this).find('.orderDir').text();
				orderType = $(this).find('.orderType').text();
				var searchKey = $('input[name="searchKey"]').val();		
				searchFlag=true;
				loadRealServerRefreshMonListContent(searchKey, orderDir, orderType);				
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
				_checkExportRealServerMonDataExist(searchKey);
			});
			
			$('#refreshLnk').click(function(e)
			{
//				loadListContent();
				$('input[name="searchKey"]').val('');
				loadRealServerRefreshMonListContent(null, orderDir, orderType);
			});	
			
			// 선택해제
			$('.selectFilter').on('click', '.deselBtn', function(e)
			{
				$('.chkFilter').children().removeAttr('checked');
				$('.chkFilter').removeClass('on');
				
				$('.chkFilterAll').find('input').attr("checked", "checked");

				refreshSelectFilterList();
				
				if ($('.sel_filter:not(.none)').length < 1)
				{
					$('.selectedOptionFilter').addClass('none');
				}
				else
				{
					$('.selectedOptionFilter').removeClass('none');
				}
//				realServerMonitor.loadRealServerMonitoringTableInListContent($('input[name="searchKey"]').val(),"","","","",changeFilter(), selectedCol());
				loadRealServerRefreshMonListContent($('input[name="searchKey"]').val(), "", "", changeFilter(), selectedCol());	
			});
			
			// 선택된 필터 삭제
			$('.selectFilterList').on('click', '.delBtn', function(e)
			{
				$('.chkFilter input[type=checkbox]').eq($(this).parent().data('parent-id')).removeAttr('checked');				

				refreshSelectFilterList();
				
				if ($('.sel_filter:not(.none)').length < 1)
				{
					$('.selectedOptionFilter').addClass('none');
				}
				else
				{
					$('.selectedOptionFilter').removeClass('none');
				}
//				realServerMonitor.loadRealServerMonitoringTableInListContent($('input[name="searchKey"]').val(),"","","","",changeFilter(), selectedCol());		
				loadRealServerRefreshMonListContent($('input[name="searchKey"]').val(), "", "", changeFilter(), selectedCol());	
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
				loadRealServerRefreshMonListContent($('input[name="searchKey"]').val(), "", "", changeFilter(), selectedCol());				
				
			});	

			$('#selectColumn_pop').click(function (e)
			{
				e.preventDefault();		
				realServerMonitor._loadSelColPop();			
			});
			
			//link
			
			$('#realserverListTable').on('click', '.serviceMapLnk', function(e)
			{
				e.preventDefault();
				with (this) 
				{
					var adcIndex = $(this).children('span').text();
					adcSetting._selectAdc(adcIndex);
					networkMap.loadNetworkMapContent(adcSetting.getAdc());
				}				
			});
			
			$('#realserverListTable').on('click', '.adcMonitoringLnk', function(e)
			{			
				e.preventDefault();
				with (this) 
				{
					var adcIndex = $(this).children('span').text();
					adcSetting._selectAdc(adcIndex);
					monitorAppliance.loadApplianceMonitorContent(adcSetting.getAdc());
				}
			});
			
			$('#realserverListTable').on('click', '.serviceMonitoringLnk', function(e)
			{
				e.preventDefault();
				with (this) 
				{
					var adcIndex = $(this).children('span').text();
					adcSetting._selectAdc(adcIndex);
					monitorServicePerfomance.loadServicePerfomanceContent(adcSetting.getAdc());
				}				
			});	
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
			realServerMonitor.selectedSet = filterData;
			
			if ($('.chkFilter').find('input:checked').length > 0)				
				realServerMonitor.setFlag = $('#filterAdd').attr("isFlag");
			
			return filterData;
		}
	},
	
	selectedCol : function()
	{
		with(this)
		{
			var selColData = [];
			$('#realserverListTable .ContentsHeadLine th').each(function()
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
			$('#realserverListTable .ContentsHeadLine').find('th').each(function()			
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
				
				if ($('.cloneDiv input[name="colChk"]').not(":checked").length > 0)
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
						showHidenCol($('#realserverListTable'), index, true);
					else
						showHidenCol($('#realserverListTable'), index, false);
				});
				
				realServerMonitor.columnSet = selectedCol();
				realServerMonitor.selectedSet = changeFilter();
				
				loadRealServerRefreshMonListContent($('input[name="searchKey"]').val(), "", "", changeFilter(), selectedCol());	
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
		var colElem = $(table).find('colgroup').contents().filter(function() {
				return this.nodeType == 1 || this.nodeType == 8;
			})[col];
		
		var currentShow = colElem.nodeType;
		if ((show && currentShow == 1) || (!show && currentShow == 8))
			return;
		
		if (show)
		{
//			$('.Filter_Board [data-colidx=' + col + '] input[type=checkbox]').removeAttr('disabled');
			$(colElem).replaceWith(colElem.nodeValue);
			$('.Filter_Board [data-colidx=' + col + ']').removeClass('none');
		}
		else
		{
//			$('.Filter_Board [data-colidx=' + col + '] input[type=checkbox]').attr('disabled', 'disabled');
			$(colElem).replaceWith(document.createComment(colElem.outerHTML));
			$('.Filter_Board [data-colidx=' + col + ']').addClass('none');
		}
		
		// 해당 행 표시상태 변경
		$(table).find('tr').each(function() 
		{
			if (show) 
			{
				$(this).find('th').eq(col).removeClass('none');
				$(this).find('td').eq(col).removeClass('none');
		    } 
			else 
			{
				$(this).find('th').eq(col).addClass('none');
				$(this).find('td').eq(col).addClass('none');
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
			targetCntHtml.html(addThousandSeparatorCommas(countTotal));
			targetPageHtml.html("(" + addThousandSeparatorCommas(currentPage) + "/" + addThousandSeparatorCommas(lastPage) + VAR_COMMON_PAGE + ")");
			
			
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
	
	_checkExportRealServerMonDataExist : function(searchKey)
	{
		with(this)
		{
			var filterData = changeFilter();
			var selColData = selectedCol();
			ajaxManager.runJsonExt({
				url : "monitor/checkExportRealServerMonDataExist.action",				
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
			var params = "adcScope.level=" + target.level + "&adcScope.index=" + target.index + "&searchKey=" + encodeURIComponent($('.control_Board input.searchTxt').val()) + 
				"&selectedVal=" + encodeURIComponent(JSON.stringify(filterData)) + "&selectedCol=" + encodeURIComponent(JSON.stringify(selColData));
			var url = "monitor/downloadRealServerMonList.action?" + params;
			$('#downloadFrame').attr('src', url);
		}
	}
});