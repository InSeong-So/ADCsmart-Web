// 시스템관리 -> 감사로그
var ClassAuditLog = Class.create({
	initialize : function() 
	{
		var fn = this;
		this.searchedOption = 
		{
			"searchKey" : undefined,
			"startTimeL" : undefined,
			"endTimeL" : undefined
		};
		this.searchStartTime = undefined;
		this.searchEndTime = undefined;
		this.orderDir  = 2; //내림차순 = 2
		this.orderType = 11;//occurTime = 11
		this.searchFlag = false;
		this.pageNavi = new PageNavigator();
		this.pageNavi.onChange(function(fromRow, toRow, orderDir, orderType) 
		{
			FlowitUtil.log('fromRow: %s, toRow: %s, orderDir: %s, orderType: %s', fromRow, toRow, orderDir, orderType);
			fn._loadLogTableInListContent(fn.searchedOption, fromRow, toRow, orderDir, orderType);
		});
	},
	onAdcChange : function() 
	{
		var option = 
		{
				"searchKey" :	undefined,
				"startTimeL" : this.searchStartTime,
				"endTimeL" : this.searchEndTime
		};
		this.loadContent(option);
	},
	loadContent : function(searchOption, orderDir, orderType) 
	{
		with (this) 
		{
			if(!validateDaterefresh())
			{
				return false;
			}
			FlowitUtil.log("searchOption: %s, orderDir: %s, orderType: %s", searchOption, orderDir, orderType);
			var rowTotal = 0;
			ajaxManager.runJsonExt({
				url : "logAnalysis/retrieveAuditLogTotal.action",
				data : 
				{
					"searchKey" : searchOption ? searchOption.searchKey : undefined,
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
					pageNavi.updateRowTotal(rowTotal, orderDir);
					noticePageInfo();
					_loadAuditLogListContent(searchOption);
				},
                errorFn : function(jqXhr)
                {
                	$.obAlertAjaxError(VAR_SYSTEMLOGANALYSIS_EXTRACT, jqXhr);
                }
			});
		}
	},
	_loadAuditLogListContent : function(searchOption, fromRow, toRow) 
	{
		with (this) 
		{
			FlowitUtil.log("searchOption:%s, fromRow:%s, toRow:%s", searchOption, fromRow, toRow);
			ajaxManager.runHtmlExt({
				url : "logAnalysis/loadAuditLogListContent.action",
				data : {
					"fromRow" : fromRow === undefined ? pageNavi.getFirstRowOfCurrentPage() : fromRow,
					"toRow" : toRow === undefined ? pageNavi.getLastRowOfCurrentPage() : toRow,
					"searchKey" : searchOption ? searchOption.searchKey : undefined,
					"startTimeL" : searchOption ? searchOption.startTimeL : undefined,
					"endTimeL" : searchOption ? searchOption.endTimeL : undefined,
					"orderDir" : this.orderDir,
					"orderType" : this.orderType
				},
				target: "#wrap .contents",
				successFn : function(params) 
				{
//					header.setActiveMenu('AuditLog');
					header.setActiveMenu('SysSettingAudit');
					noticePageInfo();
					searchedOption = searchOption;
//					_applyListContentCss();
					_registerListContentEvents();
				},
				completeFn : function() 
				{
					pageNavi.refresh();
				},
                errorFn : function(jqXhr)
                {
                	$.obAlertAjaxError(VAR_SYSTEMLOGANALYSIS_EXTRACT, jqXhr);
                }
			});
		}
	},
	_loadLogTableInListContent : function(searchOption, fromRow, toRow, orderDir, orderType) 
	{
		with (this) 
		{
			FlowitUtil.log("searchOption:%s, fromRow:%s, toRow:%s", searchOption, fromRow, toRow);
			ajaxManager.runHtmlExt({
				url : "logAnalysis/loadLogTableInAuditLogListContent.action",
				data : {
					"fromRow" : fromRow === undefined ? pageNavi.getFirstRowOfCurrentPage() : fromRow,
					"toRow" : toRow === undefined ? pageNavi.getLastRowOfCurrentPage() : toRow,
					"searchKey" : searchOption ? searchOption.searchKey : undefined,
					"startTimeL" : searchOption ? searchOption.startTimeL : undefined,
					"endTimeL" : searchOption ? searchOption.endTimeL : undefined,
					"orderDir" : this.orderDir,
					"orderType" : this.orderType					
				},
				target: "table.Board",
				successFn : function(params) 
				{
					noticePageInfo();
					searchedOption = searchOption;
					_registerListContentEvents();
//					_applyListContentCss();
				}, 
                errorFn : function(jqXhr)
                {
                	$.obAlertAjaxError(VAR_SYSTEMLOGANALYSIS_EXTRACT, jqXhr);
                }
			});
		}
	},
	_loadDetailPopupContent : function(popupContent)
	{
		with(this)
		{
			showPopup({
				'id' : '#auditlog-popup',
				'width' : '800px',
				'height' : '340px'
			});
			
			var occurTimeArea = $(".occurtime-area").filter(':last');
			var generatorArea = $(".generator-area").filter(':last');
			var ipArea = $(".ip-area").filter(':last');
			var typeArea = $(".type-area").filter(':last');
			var levelArea = $(".level-area").filter(':last');
			var contentArea = $(".content-area").filter(':last');
			
			occurTimeArea.empty().html(popupContent.occurTime);
			generatorArea.empty().html(popupContent.generator);
			ipArea.empty().html(popupContent.ip);
			typeArea.empty().html(popupContent.type);			
			levelArea.empty().html(popupContent.level);
			contentArea.empty().html(popupContent.content);
			
			$('.popupclosebtn').click(function(e)
			{
				e.preventDefault();
				$('.popup_type1').remove();
				$('.cloneDiv').remove();
			});
			
		}
	},
	_registerListContentEvents : function() 
	{
		with (this) 
		{
			$('.msg_log').addClass('none');
			if(pageNavi.getRowTotal() >= 10000)
			{
				$('.msg_log').removeClass('none');
			}
			
			$('.popuplink').click(function (e)
			{
				e.preventDefault();
				var popupContent = {
					occurTime : $(this).parent().parent().find(".audit-occurtime").val(),
					generator : $(this).parent().parent().find(".audit-generator").val(),
					ip : $(this).parent().parent().find(".audit-ip").val(),
					type : $(this).parent().parent().find(".audit-type").val(),
					level : $(this).parent().parent().find(".audit-level").val(),
					content : $(this).parent().parent().find(".audit-content").val()					
				};
				_loadDetailPopupContent(popupContent);			
			});
			
			
			$('.orderDir_Desc').click(function(e)
			{
				e.preventDefault();
				orderDir =  $(this).find('.orderDir').text();
				orderType =  $(this).find('.orderType').text();
				var option = 
				{
					"searchKey" :	$('.control_Board input.searchTxt').val(),
					"startTimeL" : searchStartTime,
					"endTimeL" : searchEndTime
				};		
				searchFlag=true;
				loadContent(option , orderDir , orderType);
			});
			
			$('.orderDir_Asc').click(function(e)
			{
				e.preventDefault();
				orderDir =  $(this).find('.orderDir').text();
				orderType =  $(this).find('.orderType').text();
				var option = 
				{
					"searchKey" :	$('.control_Board input.searchTxt').val(),
					"startTimeL" : searchStartTime,
					"endTimeL" : searchEndTime
				};		
				searchFlag=true;
				loadContent(option , orderDir , orderType);
			});
			
			$('.orderDir_None').click(function(e)
			{
				e.preventDefault();
				orderDir =  $(this).find('.orderDir').text();
				orderType =  $(this).find('.orderType').text();
				var option = 
				{
					"searchKey" :	$('.control_Board input.searchTxt').val(),
					"startTimeL" : searchStartTime,
					"endTimeL" : searchEndTime
				};		
				searchFlag=true;
				loadContent(option , orderDir , orderType);
			});
			$('.exportCssLnk').click(function(e) 
			{
				e.preventDefault();
				var option = 
				{
					"searchKey" :	$('.control_Board input.searchTxt').val(),
					"startTimeL" : searchStartTime,
					"endTimeL" : searchEndTime
				};		
				_checkExportAuditDataExist(option);
			});		
			
			$('.refreshLnk').click(function(e) 
			{
				if(!validateDaterefresh())
				{
					return false;
				}
				e.preventDefault();
				searchStartTime = undefined;
				searchEndTime = undefined;
				loadContent();
			});
			
			// search event
			$('.control_Board a.searchLnk').click(function(e) 
			{
				e.preventDefault();
				var option = 
				{
					"searchKey" :	$('.control_Board input.searchTxt').val(),
					"startTimeL" : searchStartTime,
					"endTimeL" : searchEndTime
				};
			
				FlowitUtil.log('searchOption: %s', option);
				searchFlag=true;
				loadContent(option);
			});
			
			$('.control_Board input.searchTxt').keydown(function(e) 
			{
				if (e.which != 13)
				{
					return;
				}
				var option = 
				{
					"searchKey" :	$('.control_Board input.searchTxt').val(),
					"startTimeL" : searchStartTime,
					"endTimeL" : searchEndTime
				};
				FlowitUtil.log('searchOption: %s', option);
				searchFlag=true;
				loadContent(option);
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
			// DateRangePicker
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
			
			if(this.searchFlag == true)
			{
				$('.dataNotExistMsg').addClass("none");
				if($('.auditLogList').size() > 0)
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
				if($('.auditLogList').size() > 0)
				{
					$('.dataNotExistMsg').addClass("none");
				}
				else
				{
					$('.dataNotExistMsg').removeClass("none");
				}
			}
		}
	},
	validateDaterefresh : function()
	{
		if(($('.control_Board input.searchTxt').val() != "") && ($('.control_Board input.searchTxt').val() != null))
		{
			var search = $('.control_Board input.searchTxt').val();
	
			if(isValidStringLength(search, 1, 64) == false)
			{
				$.obAlertNotice(VAR_COMMON_LENGTHFORMAT+"("+1+"~"+64+")");
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
	_checkExportAuditDataExist : function(searchOption)
	{
		with(this)
		{
			ajaxManager.runJsonExt
			({
				url : "logAnalysis/checkAuditDataExist.action",
				data : 
				{
					"searchKey" : searchOption ? searchOption.searchKey : undefined,
					"startTimeL" : searchOption ? searchOption.startTimeL : undefined,
					"endTimeL" : searchOption ? searchOption.endTimeL : undefined							
				},
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
				}
			});
		}
	},
	exportCss : function() 
	{
		with (this) 
		{
			var params = "searchKey=" + encodeURIComponent($('.control_Board input.searchTxt').val()) + "&startTimeL=" + searchStartTime + "&endTimeL=" + searchEndTime;
			var url = "logAnalysis/downloadAuditLog.action?" + params;
			$('#downloadFrame').attr('src',url);	
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