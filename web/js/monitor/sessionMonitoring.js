var SessionMonitoring = Class.create({
	
	initialize : function()
	{
		var fn = this;
        this.searchedOption =
        {
			"srcIp" : undefined,
			"dstIp" : undefined,
			"realIP" : undefined,
			"srcPort" : undefined,
			"dstPort" : undefined,
			"realPort" : undefined,
			"protocol" : undefined,
			"agingTime" : undefined,
        };
		this.adc;		
		this.rowTotal = 0;
		this.orderDir = 2; // 2는 내림차순
		this.orderType = 33; // 33은 SRC_IP
		this.selectedOption = "SLB";
		this.searchFlag = false;
		this.pageNavi = new PageNavigator();
		this.pageNavi.onChange(function(fromRow, toRow, orderType, orderDir)
		{
			FlowitUtil.log('fromRow : %s, toRow: %s, orderType: %s, orderDir: %s', fromRow, toRow, orderType, orderDir);
			fn.loadSessionTableListContent(fn.searchedOption, fromRow, toRow, orderType, orderDir);
		});
	},
	onAdcChange : function() 
	{
		this.orderDir  = 2; //2는 내림차순
		this.orderType = 33;// 33은 SRC_IP
		this.rowTotal = 0;		
		var option = 
		{
			"srcIp" : $('input[name=srcIp]').val(),
			"dstIp" : $('input[name=dstIp]').val(),
			"realIP" : $('input[name=realIP]').val(),
			"srcPort" : $('input[name=srcPort]').val(),
			"dstPort" : $('input[name=dstPort]').val(),
			"realPort" : $('input[name=realPort]').val(),
			"protocol" : $('select[name=protocol]').val(),
			"agingTime" : $('input[name=agingTime]').val()
		};			
		this.loadListContent(option);
	},
	loadListContent : function(searchOption, orderType, orderDir)
	{
		with(this)
		{
			ajaxManager.runHtmlExt({
				url : "monitor/loadDefaultPage.action",
				target : "#wrap .contents",
				data : 
				{
					"adcObj.index" : adcSetting.getAdc().index,
					"adcObj.category" : adcSetting.getSelectIndex(),
					"adcObj.desciption" : adcSetting.getAdcVersion(),
					"adc.type" : adcSetting.getAdc().type,
					"adc.name" : adcSetting.getAdc().name,
					"orderObj.orderDirection" : this.orderDir,
					"orderObj.orderType" : this.orderType,
					"selectedOption" : selectedOption
				},
				successFn : function(params)
				{
					header.setActiveMenu('SessionMonitoring');
					searchedOption = searchOption;
					registerListContentsEvents(searchOption);
					if (adcSetting.getAdc().type == "Alteon")
					{
						if (selectedOption == "SLB")
						{
							$('.contents_area #selectedSLB').attr('checked', true);
						}
						else
						{
							$('.contents_area #selectedFLB').attr('checked', true);
						}
					}				
				},
				completeFn : function()
				{
					pageNavi.updateRowTotal(0, orderType);
					noticePageInfo();
					//pageNavi.refresh();
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_SESSIONMOR_SEDLFAIL, jqXhr);
//					exceptionEvent();
				}
			});
		}
	},
	retriveCount : function(searchOption, orderType, orderDir)
	{
		with(this)
		{
			ajaxManager.runJsonExt({
				url:"monitor/retriveCount.action",
				data : 
				{
					"adcObj.index" : adcSetting.getAdc().index,
					"adcObj.category" : adcSetting.getSelectIndex(),
					"adcObj.desciption" : adcSetting.getAdcVersion(),
					"adc.type" : adcSetting.getAdc().type,
					"adc.name" : adcSetting.getAdc().name,
					"orderObj.orderDirection" : this.orderDir,
					"orderObj.orderType" : this.orderType,
					"srcIp" : $('input[name=srcIp]').val(),
					"dstIp" : $('input[name=dstIp]').val(),
					"realIP" : $('input[name=realIP]').val(),
					"srcPort" : $('input[name=srcPort]').val(),
					"dstPort" : $('input[name=dstPort]').val(),
					"realPort" : $('input[name=realPort]').val(),
					"protocol" : $('select[name=protocol]').val(),
					"agingTime" : $('input[name=agingTime]').val()
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
					pageNavi.updateRowTotal(rowTotal, orderType);
					loadSortingListContent(searchOption);
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_SESSIONMOR_SEDCFAIL, jqXhr);
//					exceptionEvent();
				}
			});
		}
	},	
	loadSessionSearchListContent : function(searchOption ,orderType, orderDir)
	{
		with(this)
		{
			if (!validateSessionKeyWordInput())
			{
				return false;
			} 
			ajaxManager.runJsonExt({
				url:"monitor/loadSessionSearchListContent.action",
				data : 
				{
					"adcObj.index" : adcSetting.getAdc().index,
					"adcObj.category" : adcSetting.getSelectIndex(),
					"adcObj.desciption" : adcSetting.getAdcVersion(),
					"adc.type" : adcSetting.getAdc().type,
					"adc.name" : adcSetting.getAdc().name,
					"orderObj.orderDirection" : this.orderDir,
					"orderObj.orderType" : this.orderType,
					"srcIp" : $('input[name=srcIp]').val(),
					"dstIp" : $('input[name=dstIp]').val(),
					"realIP" : $('input[name=realIP]').val(),
					"srcPort" : $('input[name=srcPort]').val(),
					"dstPort" : $('input[name=dstPort]').val(),
					"realPort" : $('input[name=realPort]').val(),
					"protocol" : $('select[name=protocol]').val(),
					"agingTime" : $('input[name=agingTime]').val(),
					"selectedOption" : selectedOption
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
					pageNavi.updateRowTotal(rowTotal, orderType);
					loadSortingListContent(searchOption);
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_SESSIONMOR_SEDSFAIL, jqXhr);
//					exceptionEvent();
				}
			});
		}
	},	
	loadSortingListContent : function(searchOption, fromRow, toRow, orderDir, orderType)
	{
		with(this)
		{
			if (!validateSessionKeyWordInput())
			{
				return false;
			} 
			ajaxManager.runHtmlExt({
				url : "monitor/loadSortingListContent.action",
				target : "table.sessionTable",
				data : 
				{
					"adcObj.index" : adcSetting.getAdc().index,
					"adcObj.category" : adcSetting.getSelectIndex(),
					"adcObj.desciption" : adcSetting.getAdcVersion(),
					"adc.type" : adcSetting.getAdc().type,
					"adc.name" : adcSetting.getAdc().name,
					"orderObj.orderDirection" : this.orderDir,
					"orderObj.orderType" : this.orderType,
					"fromRow" : fromRow === undefined ? pageNavi.getFirstRowOfCurrentPage() : fromRow,
					"toRow" : toRow === undefined ? pageNavi.getLastRowOfCurrentPage() : toRow,
					"srcIp" : $('input[name=srcIp]').val(),
					"dstIp" : $('input[name=dstIp]').val(),
					"realIP" : $('input[name=realIP]').val(),
					"srcPort" : $('input[name=srcPort]').val(),
					"dstPort" : $('input[name=dstPort]').val(),
					"realPort" : $('input[name=realPort]').val(),
					"protocol" : $('select[name=protocol]').val(),
					"agingTime" : $('input[name=agingTime]').val(),
					"selectedOption" : selectedOption
				},
				successFn : function(params)
				{
					header.setActiveMenu('SessionMonitoring');
					noticePageInfo();
					searchedOption = searchOption;
					registerListContentsEvents(searchOption);
				},
				completeFn : function()
				{
					pageNavi.refresh();
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_SESSIONMOR_SEDSOFAIL, jqXhr);
//					exceptionEvent();
				}
			});
		}
	},
	loadSessionTableListContent : function(searchOption, fromRow, toRow,orderType, orderDir)
	{
		with(this)
		{
			ajaxManager.runHtmlExt({
				url:"monitor/loadSessionTableListContent.action",
				target : "table.sessionTable",
				data : 
				{
					"adcObj.index" : adcSetting.getAdc().index,
					"adcObj.category" : adcSetting.getSelectIndex(),
					"adcObj.desciption" : adcSetting.getAdcVersion(),
					"adc.type" : adcSetting.getAdc().type,
					"adc.name" : adcSetting.getAdc().name,
					"orderObj.orderDirection" : this.orderDir,
					"orderObj.orderType" : this.orderType,
					"srcIp" : $('input[name=srcIp]').val(),
					"dstIp" : $('input[name=dstIp]').val(),
					"realIP" : $('input[name=realIP]').val(),
					"srcPort" : $('input[name=srcPort]').val(),
					"dstPort" : $('input[name=dstPort]').val(),
					"realPort" : $('input[name=realPort]').val(),
					"protocol" : $('select[name=protocol]').val(),
					"agingTime" : $('input[name=agingTime]').val(),
					"fromRow" : fromRow === undefined ? pageNavi.getFirstRowOfCurrentPage() : fromRow,
					"toRow" : toRow === undefined ? pageNavi.getLastRowOfCurrentPage() : toRow,
					"selectedOption" : selectedOption
				},
				successFn : function(data)
				{
					noticePageInfo();
					registerListContentsEvents();
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_SESSIONMOR_SEDLFAIL, jqXhr);
//					exceptionEvent();
				}
			});
		}
	},

	registerListContentsEvents : function(searchedOption, fromRow, toRow, orderType, orderDir)
	{
		with(this)
		{
			if (adcSetting.getAdcStatus() == "available") 
			{
				$('.searchOption').change(function(event)
				{
					event.preventDefault();
					selectedOption = $(this).val();
					loadListContent();
				});
				
				$('.exportCssLnk').click(function(event){
					if (adcSetting.getAdc().type == "PAS")
					{
						//alert(VAR_COMMON_PASNOTSUPPORT);
						return;
					}
					else if (adcSetting.getAdc().type == "PASK")
					{
						//alert(VAR_COMMON_PASKNOTSUPPORT);
						return;
					}
					else
					{
						_checkExportSessionInfoDataExist();
					}
				});
				
				$('.orderDir_Desc').click(function(e)
				{				
					e.preventDefault();
					orderDir =  $(this).find('.orderDir').text();
					orderType =  $(this).find('.orderType').text();
					var option = 
					{
						"fromRow" : fromRow === undefined ? pageNavi.getFirstRowOfCurrentPage() : fromRow,
						"toRow" : toRow === undefined ? pageNavi.getLastRowOfCurrentPage() : toRow,
						"srcIp" : $('input[name=srcIp]').val(),
						"dstIp" : $('input[name=dstIp]').val(),
						"realIP" : $('input[name=realIP]').val(),
						"srcPort" : $('input[name=srcPort]').val(),
						"dstPort" : $('input[name=dstPort]').val(),
						"realPort" : $('input[name=realPort]').val(),
						"protocol" : $('select[name=protocol]').val(),
						"agingTime" : $('input[name=agingTime]').val()
					};	
					if (adcSetting.getAdc().type == "PAS")
					{
						//alert(VAR_COMMON_PASNOTSUPPORT);
						return;
					}
					else if (adcSetting.getAdc().type == "PASK")
					{
						//alert(VAR_COMMON_PASKNOTSUPPORT);
						return;
					}
					else
					{
						searchFlag = true;
						loadSortingListContent(option, fromRow, toRow, orderType, orderDir);	
					}						
				});
						
				$('.orderDir_Asc').click(function(e)
				{				
					e.preventDefault();
					orderDir =  $(this).find('.orderDir').text();
					orderType =  $(this).find('.orderType').text();
					var option = 
					{
						"fromRow" : fromRow === undefined ? pageNavi.getFirstRowOfCurrentPage() : fromRow,
						"toRow" : toRow === undefined ? pageNavi.getLastRowOfCurrentPage() : toRow,
						"srcIp" : $('input[name=srcIp]').val(),
						"dstIp" : $('input[name=dstIp]').val(),
						"realIP" : $('input[name=realIP]').val(),
						"srcPort" : $('input[name=srcPort]').val(),
						"dstPort" : $('input[name=dstPort]').val(),
						"realPort" : $('input[name=realPort]').val(),
						"protocol" : $('select[name=protocol]').val(),
						"agingTime" : $('input[name=agingTime]').val()
					};
					if (adcSetting.getAdc().type == "PAS")
					{
						//alert(VAR_COMMON_PASNOTSUPPORT);
						return;
					}
					else if (adcSetting.getAdc().type == "PASK")
					{
						//alert(VAR_COMMON_PASKNOTSUPPORT);
						return;
					}
					else
					{
						searchFlag = true;
						loadSortingListContent(option, fromRow, toRow, orderType, orderDir);
					}							
				});
				
				$('.orderDir_None').click(function(e)
				{
					e.preventDefault();
					orderDir =  $(this).find('.orderDir').text();
					orderType =  $(this).find('.orderType').text();
					var option = 
					{
						"fromRow" : fromRow === undefined ? pageNavi.getFirstRowOfCurrentPage() : fromRow,
						"toRow" : toRow === undefined ? pageNavi.getLastRowOfCurrentPage() : toRow,
						"srcIp" : $('input[name=srcIp]').val(),
						"dstIp" : $('input[name=dstIp]').val(),
						"realIP" : $('input[name=realIP]').val(),
						"srcPort" : $('input[name=srcPort]').val(),
						"dstPort" : $('input[name=dstPort]').val(),
						"realPort" : $('input[name=realPort]').val(),
						"protocol" : $('select[name=protocol]').val(),
						"agingTime" : $('input[name=agingTime]').val()
					};	
					if (adcSetting.getAdc().type == "PAS")
					{
						//alert(VAR_COMMON_PASNOTSUPPORT);
						return;
					}
					else if (adcSetting.getAdc().type == "PASK")
					{
						//alert(VAR_COMMON_PASKNOTSUPPORT);
						return;
					}
					else
					{
						searchFlag = true;
						loadSortingListContent(option, fromRow, toRow, orderType, orderDir);
					}							
				});
				$('.searchLnkTxt').keypress(function(event) { 
				    return event.keyCode != 13;
				}); 
							
				$('.searchLnkTxt').keydown(function(e)
				{				
					e.stopImmediatePropagation();
					//e.preventDefault();
					//e.stopPropagation();
					
					if (e.which != 13)
					{
						return;
					}
					
					var option = 
					{
						"srcIp" : $('input[name=srcIp]').val(),
						"dstIp" : $('input[name=dstIp]').val(),
						"realIP" : $('input[name=realIP]').val(),
						"srcPort" : $('input[name=srcPort]').val(),
						"dstPort" : $('input[name=dstPort]').val(),
						"realPort" : $('input[name=realPort]').val(),
						"protocol" : $('select[name=protocol]').val(),
						"agingTime" : $('input[name=agingTime]').val()
					};	
					searchFlag=true;
					loadSessionSearchListContent(option);
					  							
				});
	
	            $('.searchLnk').click(function(e)
	            {    
					e.stopImmediatePropagation(); 
	                with (this)
	                {
	                	if (adcSetting.getAdc().type == "PAS")
	    				{
	    					//alert(VAR_COMMON_PASNOTSUPPORT);
	                		return;
	    				}
	    				else if (adcSetting.getAdc().type == "PASK")
	    				{
	    					//alert(VAR_COMMON_PASKNOTSUPPORT);
	    					return;
	    				}
	    				else
	    				{
							if (!validateSessionKeyWordInput())
							{
								return false;
							}            
							
							var option = 
							{
								"srcIp" : $('input[name=srcIp]').val(),
								"dstIp" : $('input[name=dstIp]').val(),
								"realIP" : $('input[name=realIP]').val(),
								"srcPort" : $('input[name=srcPort]').val(),
								"dstPort" : $('input[name=dstPort]').val(),
								"realPort" : $('input[name=realPort]').val(),
								"protocol" : $('select[name=protocol]').val(),
								"agingTime" : $('input[name=agingTime]').val()
							};	
							searchFlag=true;
							loadSessionSearchListContent(option);
	    				}
					}                
	           });			
	          
				$('.defaultSelect').change(function(e)
				{
					e.stopImmediatePropagation(); //노드에 기록된 중첩 이벤트를 막기 위해 사용
					var selectedVal = $('.defaultSelect').val();
					if(selectedVal == 0)//src_ip
					{	
						e.stopImmediatePropagation();
						var nameVal = document.getElementsByName('srcIp');
						if(nameVal.length>0)
						{					
							$.obAlertNotice(VAR_SESSIONMOR_SRCASEL);
							$('select[name = "select0"] option:eq(0)').attr("selected", "selected");
							return;
						}	
						else
						{
							var pArea = $('.addSlt .defaultArea').filter(':last');
							pArea.empty();
							var html = "";						
							html +='<input type="text"  name="srcIp" class="inputText width130">';							
							pArea.html(html);
						}	
					}
					else if(selectedVal == 1)//dst_Ip
					{
						e.stopImmediatePropagation();
						var nameVal = document.getElementsByName('dstIp');
						if(nameVal.length>0)
						{					
							if(selectedOption == 'SLB')
							{
								$.obAlertNotice(VAR_SESSIONMOR_VIPASEL);
							}
							else
							{
								$.obAlertNotice(VAR_SESSIONMOR_DSTASEL);
							}
							$('select[name = "select0"] option:eq(0)').attr("selected", "selected");
							return;
						}	
						else
						{
							var pArea = $('.addSlt .defaultArea').filter(':last');
							pArea.empty();
							var html = "";							
							html +='<input type="text"  name="dstIp" class="inputText width130">';							
							pArea.html(html);
						}					
					}	
					else if(selectedVal == 2)//real_Ip
					{
						e.stopImmediatePropagation();
						var nameVal = document.getElementsByName('realIP');
						if(nameVal.length>0)
						{					
							$.obAlertNotice(VAR_SESSIONMOR_REALASEL);
							$('select[name = "select0"] option:eq(0)').attr("selected", "selected");
							return;
						}	
						else
						{
							var pArea = $('.addSlt .defaultArea').filter(':last');
							pArea.empty();
							var html = "";							
							html +='<input type="text"  name="realIP" class="inputText width130">';							
							pArea.html(html);
						}					
					}				
					else if(selectedVal == 3)//src_port
					{
						e.stopImmediatePropagation();
						var nameVal = document.getElementsByName('srcPort');
						if(nameVal.length>0)
						{
							$.obAlertNotice(VAR_SESSIONMOR_SRCPALSEL);
							$('select[name = "select0"] option:eq(0)').attr("selected", "selected");
							return;
						}	
						else
						{
							var pArea = $('.addSlt .defaultArea').filter(':last');
							pArea.empty();
							var html = "";								
							html +='<input type="text"  name="srcPort" class="inputText width130">';							
							pArea.html(html);
						}
					}
					else if(selectedVal == 4)//dst_port
					{
						e.stopImmediatePropagation();
						var nameVal = document.getElementsByName('dstPort');
						if(nameVal.length>0)
						{
							if(selectedOption == 'SLB')
							{
								$.obAlertNotice(VAR_SESSIONMOR_VPASEL);
							}
							else
							{
								$.obAlertNotice(VAR_SESSIONMOR_DSTPASEL);
							}
							$('select[name = "select0"] option:eq(0)').attr("selected", "selected");
							return;
						}	
						else
						{
							var pArea = $('.addSlt .defaultArea').filter(':last');
							pArea.empty();
							var html = "";							
							html +='<input type="text"  name="dstPort" class="inputText width130">';							
							pArea.html(html);
						}
					}
					else if(selectedVal == 5)//real_port
					{
						e.stopImmediatePropagation();
						var nameVal = document.getElementsByName('realPort');
						if(nameVal.length>0)
						{
							$.obAlertNotice(VAR_SESSIONMOR_REALPASEL);
							$('select[name = "select0"] option:eq(0)').attr("selected", "selected");
							return;
						}	
						else
						{
							var pArea = $('.addSlt .defaultArea').filter(':last');
							pArea.empty();
							var html = "";							
							html +='<input type="text"  name="realPort" class="inputText width130">';							
							pArea.html(html);
						}
					}
					else if(selectedVal == 6)//protocol
					{
						e.stopImmediatePropagation();
						var nameVal = document.getElementsByName('protocol');
						if(nameVal.length>0)
						{
							$.obAlertNotice(VAR_SESSIONMOR_PROALRSEL);
							$('select[name = "select0"] option:eq(0)').attr("selected", "selected");
							return;
						}	
						else
						{
							var pArea = $('.addSlt .defaultArea').filter(':last');
							pArea.empty();
							var html = "";								
							html +='<select name="protocol" class="inputSelect width134">';
							html +='<option value="1">' + VAR_COMMON_TCP + '</option>';
							html +='<option value="2">' + VAR_COMMON_UDP + '</option>';
							html +='<option value="3">' + VAR_COMMON_ICMP + '</option>';
							html +='</select>';
							
							pArea.html(html);
						}
					}
					else if(selectedVal == 7)//age
					{
						e.stopImmediatePropagation();
						var nameVal = document.getElementsByName('agingTime');
						if(nameVal.length>0)
						{
							$.obAlertNotice(VAR_SESSIONMOR_AGESEL);
							$('select[name = "select0"] option:eq(0)').attr("selected", "selected");
							return;
						}	
						else
						{
							var pArea = $('.addSlt .defaultArea').filter(':last');
							pArea.empty();
							var html = "";								
							html +='<input type="text"  name="agingTime" class="inputText width130">';							
							pArea.html(html);
						}
					}
				});
				
				$('.addSelect').click(function(e)
				{
					e.stopImmediatePropagation();
					if (adcSetting.getAdc().type == "PAS")
					{
						//alert(VAR_COMMON_PASNOTSUPPORT);
						return;
					}
					else if (adcSetting.getAdc().type == "PASK")
					{
						//alert(VAR_COMMON_PASKNOTSUPPORT);
						return;
					}
					else
					{
						_addSKeyWordInputForm();
						_registSelectChangeEvent();
					}				
				});
				$('#delButton').click(function(e)
				{
					e.stopImmediatePropagation();
					if (adcSetting.getAdc().type == "PAS")
					{
						//alert(VAR_COMMON_PASNOTSUPPORT);
						return;
					}
					else if (adcSetting.getAdc().type == "PASK")
					{
						//alert(VAR_COMMON_PASKNOTSUPPORT);
						return;
					}
					else
					{					
						if($('select').length == 1)
						{
							$.obAlertNotice(VAR_SESSIONMOR_SRCIDOMI);
							return;
						}
						else
						{
							$('.addSlt').remove();
							_checkKeyWordSelectBoxLength();
						}	
					}				
				});	
				
				if(this.searchFlag == true)
				{
					$('.nulldataMsg').addClass("none");
					$('.dataNotExistMsg').addClass("none");
					if($('.sessionList').size() > 0)
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
					if($('.sessionList').size() > 0)
					{
						$('.dataNotExistMsg').addClass("none");
					}
					else
					{
						$('.dataNotExistMsg').removeClass("none");
					}
				}
				
			}
			if ((adcSetting.getAdc().type == "PAS") || (adcSetting.getAdc().type == "PASK"))
			{
				$('select[name="select0"]').attr("disabled", "disabled");
				$('input[name="srcIp"]').attr("disabled", "disabled");
			}
			
			if (adcSetting.getAdcStatus() != "available" || adcSetting.getAdc().mode == 1) 
			{
				$('.addSelect').off();
				$('select[name="select0"]').attr("disabled", "disabled");
				$('input[name="srcIp"]').attr("disabled", "disabled");
				
				$('.addKeyword').attr("src", "/imgs/btn/btn_addkeyword_off.gif");
				$('.delKeyword').attr("src", "/imgs/btn/btn_delkeyword_off.gif");
				
				$('.searchNotMsg').addClass("none");
				$('.dataNotExistMsg').addClass("none");
				$('.imgOff').removeClass("none");
				$('.imgOn').addClass("none");
				
				if ($('.sessionList').size() > 0 )
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
	// #3984-4 #3: 14.07.28 sw.jung 세션검색 유효성 검사 개선
	// from GS. #4012-1 #7, #3984-4 #3: 14.07.28 sw.jung 세션검색 유효성 검사 개선
	validateSessionKeyWordInput : function()
	{
		if (!$('input[name="srcIp"]').val() && !$('input[name="dstIp"]').val() && !$('input[name="realIP"]').val())
		{
			$.obAlertNotice(VAR_SESSIONMOR_SRCIDOMI);
            return false;
		}
		
		return $.validate
			([
			  	{
			  		target: $('input[name="srcIp"]'),
			  		name: $($('input[name="srcIp"]').parent().parent().find('select option:selected')[0]).text(),
			  		type: "ip"
			  	},
			  	{
			  		target: $('input[name="dstIp"]'),
			  		name: $($('input[name="dstIp"]').parent().parent().find('select option:selected')[0]).text(),
			  		type: "ip"
			  	},
			  	{
			  		target: $('input[name="realIP"]'),
			  		name: $($('input[name="realIP"]').parent().parent().find('select option:selected')[0]).text(),
			  		type: "ip"
			  	},
			  	{
			  		target: $('.selectKeyword input[name="srcPort"]'),
			  		name: $($('input[name="srcPort"]').parent().parent().find('select option:selected')[0]).text(),
			  		type: "number",
			  		range: [1,65535]
			  	},
			  	{
			  		target: $('.selectKeyword input[name="dstPort"]'),
			  		name: $($('input[name="dstPort"]').parent().parent().find('select option:selected')[0]).text(),
			  		type: "number",
			  		range: [1,65535]
			  	},
			  	{
			  		target: $('.selectKeyword input[name="realPort"]'),
			  		name: $($('input[name="realPort"]').parent().parent().find('select option:selected')[0]).text(),
			  		type: "number",
			  		range: [1,65535]
			  	},
				{
			  		target: $('.selectKeyword input[name="agingTime"]'),
			  		name: $($('input[name="agingTime"]').parent().parent().find('select option:selected')[0]).text(),
			  		type: "number",
			  		range: [1,65535]
			  	}
			  ]);
	},
//	validateSessionKeyWordInput : function()
//	{	
//		with(this)
//		{
//            var srcIp = document.getElementsByName('srcIp').length;
//            var dstIp = document.getElementsByName('dstIp').length;
//            var realIP = document.getElementsByName('realIP').length;
//            
//            if(srcIp == 0 && dstIp == 0 && realIP == 0)// srcIp나 dstIp 둘중 하나도 없는 경우를 검사한다.
//            {
//                 alert(VAR_SESSIONMOR_SRCIDOMI);
//                 return false;
//            }
//			
//			var srcIpVal = $('.selectKeyword input[name="srcIp"]').val();
//			var srcLength = $('.selectKeyword input[name="srcIp"]').length;			
//
//			if(srcLength >=1)
//			{
//				if(srcIpVal != "" && srcIpVal != undefined)
//				{
////					if (!FlowitUtil.checkIp(srcIpVal))
//					if (!getValidateIP(srcIpVal))
//					{
//						alert(VAR_SESSIONMOR_SRCIIFORM);
//						return false;
//					}
//				}
//				else
//				{
//					alert(VAR_SESSIONMOR_SRCINPUT);
//					return false;
//				}	
//			}				
//
//			var dstIpVal = $('.selectKeyword input[name="dstIp"]').val();
//			var dstIpLength = $('.selectKeyword input[name="dstIp"]').length;	
//			if(dstIpLength >=1)
//			{				
//				if(dstIpVal != "" && dstIpVal != undefined )
//				{					
////					if(!FlowitUtil.checkIp(dstIpVal))
//					if (!getValidateIP(dstIpVal))
//					{
//						alert(VAR_SESSIONMOR_DSTIFORM);
//						return false;
//					}
//				}
//				else
//				{
//					alert(VAR_SESSIONMOR_DSTINPUT);
//					return false;
//				}
//			}	
//			
//			var realIPVal = $('.selectKeyword input[name="realIP"]').val();
//			var realIPLength = $('.selectKeyword input[name="realIP"]').length;	
//			if(realIPLength >=1)
//			{				
//				if(realIPVal != "" && realIPVal != undefined )
//				{					
////					if(!FlowitUtil.checkIp(realIPVal))
//					if (!getValidateIP(realIPVal))
//					{
//						alert(VAR_SESSIONMOR_REALIFORM);
//						return false;
//					}
//				}
//				else
//				{
//					alert(VAR_SESSIONMOR_REALINPUT);
//					return false;
//				}
//			}	
//			var srcPortVal = $('.selectKeyword input[name="srcPort"]').val();
//			if(srcPortVal != undefined )
//			{
//				//alert(FlowitUtil.checkIp(srcPortVal));
////				if (FlowitUtil.checkPortNum(srcPortVal)!=true)
//				if (!getValidateNumberRange(srcPortVal, 1, 65535))
//				{
//					alert(VAR_SESSIONMOR_SRCPIFORM);
//					return false;
//				}
//			}
//			var dstPortVal = $('.selectKeyword input[name="dstPort"]').val();
//			if(dstPortVal != undefined )
//			{
//				//alert(FlowitUtil.checkIp(dstPortVal));
////				if (FlowitUtil.checkPortNum(dstPortVal)!=true) 
//				if (!getValidateNumberRange(dstPortVal, 1, 65535))
//				{
//					alert(VAR_SESSIONMOR_DSTPIFORM);
//					return false;
//				}
//			}	
//			var realPortVal = $('.selectKeyword input[name="realPort"]').val();
//			if(realPortVal != undefined )
//			{
//				//alert(FlowitUtil.checkIp(dstPortVal));
////				if (FlowitUtil.checkPortNum(realPortVal)!=true)
//				if (!getValidateNumberRange(realPortVal, 1, 65535))
//				{
//					alert(VAR_SESSIONMOR_REALPIFORM);
//					return false;
//				}
//			}	
//			return true;
//		}		
//	},

	_checkKeyWordSelectBoxLength : function()
	{
		with(this)
		{
			if($('select').length >=5)
			{	
				var $s = $('#addButton').filter(':last');
				$s.empty();
				var	 html ='';    	
				html += '<span title=' + '"' + VAR_SESSIONMOR_ADD + '"' + ' class="addSelect" href="#">';				
				html +='<img src="/imgs/btn' + getImgLang() + '/btn_addkeyword_off.gif" /></span>';
				$s.html(html);
			}
			else if($('select').length == 0)
			{
				$.obAlertNotice(VAR_SESSIONMOR_SRCIDOMI);
				return;
			}	
			else
			{
				var $s = $('#addButton').filter(':last');
				$s.empty();
				var	 html ='';
				html += '<span title=' + '"' + VAR_SESSIONMOR_ADD + '"' + ' class="addSelect" href="#">';
				html +='<img src="/imgs/btn' + getImgLang() + '/btn_addkeyword.gif" /></span>';
				$s.html(html);
				$('.addSelect').click(function(e)
				{
					e.stopImmediatePropagation();
					_addSKeyWordInputForm();	
					_registSelectChangeEvent();
				});
			}	
		}
	},
	
	_getSelectIndex : function()
	{
		with (this)
		{
			var select;
			select = document.getElementsByName("select0").length;
			if (select <= 0) // select0이 없는경우,
			{
				return 0;
			}
			select = document.getElementsByName("select1").length;
			if (select <= 0) // select0이 있고 select1이 없는경우,
			{
				return 1;
			}
			select = document.getElementsByName("select2").length;
			if (select <= 0) // select1이 있고 select2이 없는경우,
			{
				return 2;
			}
			select = document.getElementsByName("select3").length;
			if (select <= 0) // select2이 있고 select3이 없는경우,
			{
				return 3;
			}
			select = document.getElementsByName("select4").length;
			if (select <= 0) // select3이 있고 select4이 없는경우,
			{
				return 4;
			}
			
			return -1;			
		}
	},
	
	_addSKeyWordInputForm : function()
	{
		with(this)
		{
//			var select0 = document.getElementsByName("select0").length;
//			var select1 = document.getElementsByName("select1").length;
//			var select2 = document.getElementsByName("select2").length;
//			var select3 = document.getElementsByName("select3").length;
//			var select4 = document.getElementsByName("select4").length;
			
			var selectIndex = _getSelectIndex();
			if (selectIndex == -1) 
			{
				return;
			}
			
			var $area = $('.contents_area .addSearchKeyword').filter(':last');
			var html ='';
			
			var divClassName = "divSelect"+selectIndex;
			var selectName = "select"+selectIndex;
			var deleteName = "delete"+selectIndex;
			
			html +='<div class="' + divClassName + '">';
			html +='<select name="' + selectName + '"class="width130">';
			html +='<option value="0">' + VAR_SESSIONMOR_CIP + '</option>';
			if (selectedOption == "SLB")
			{
				html +='<option value="1">' + VAR_SESSIONMOR_VIP + '</option>';
			}
			else
			{
				html +='<option value="1">' + VAR_SESSIONMOR_DIP + '</option>';
			}
			html +='<option value="2">' + VAR_SESSIONMOR_REALIP + '</option>';
			html +='<option value="3">' + VAR_SESSIONMOR_CPORT + '</option>';
			if (selectedOption == "SLB")
			{
				html +='<option value="4">' + VAR_SESSIONMOR_VPORT + '</option>';
				html +='<option value="5">' + VAR_SESSIONMOR_REALPORT + '</option>';
			}
			else
			{
				html +='<option value="4">' + VAR_SESSIONMOR_DPORT  + '</option>';
			}			
			
			if (adcSetting.getAdc().type != "Alteon")
			{
				html +='<option value="6">' + VAR_SESSIONMOR_PROTO + '</option>';
			}
			if(adcSetting.getAdc().type == "Alteon")
			{
				html +='<option value="7">' + VAR_SESSIONMOR_AGE + '</option>';
			}
			
			html +='</select>&nbsp;&nbsp;';
			html +='<span class="valueArea">';
			html +='<input type="text"  name="srcIp" class="inputText width130">';	
			html +='</span>&nbsp;';
			html +='<span title=' + '"' + VAR_SESSIONMOR_DEL + '"' + 'class="' + deleteName + '"  href="#">';
			html +='<img src="/imgs/btn' + getImgLang() + '/btn_delkeyword.gif"/></span></div>';
			
/*			
			if(select0 <= 0)//select0이 없는경우
			{
				html +='<div class="divSelect0" >';
				html +='<select name="select0" >';
				html +='<option value="0">' + VAR_SESSIONMOR_CIP + '</option>';
				html +='<option value="1">' + VAR_SESSIONMOR_VIP + '</option>';
				html +='<option value="2">' + VAR_SESSIONMOR_REALIP + '</option>';
				html +='<option value="3">' + VAR_SESSIONMOR_CPORT + '</option>';
				html +='<option value="4">' + VAR_SESSIONMOR_VPORT + '</option>';
				html +='<option value="5">' + VAR_SESSIONMOR_REALPORT + '</option>';
				if (adcSetting.getAdc().type != "Alteon")
				{
					html +='<option value="6">' + VAR_SESSIONMOR_PROTO + '</option>';
				}
				html +='</select>&nbsp;&nbsp;';
				html +='<span class="valueArea">';
				html +='<input type="text"  name="srcIp" class="inputText width130">';	
				html +='</span>&nbsp;';
				html +='<span title=' + '"' + VAR_SESSIONMOR_DEL + '"' + 'class="delete0"  href="#">';
				
				if (langCode == "ko_KR")
				{
					html +='<img src="/imgs/btn/btn_delkeyword.gif"/></span></div>';
				}
				else if (langCode == "en_US")
				{
					html +='<img src="/imgs/btn_eng/btn_delkeyword.gif"/></span></div>';
				}
				else
				{
					html +='<img src="/imgs/btn/btn_delkeyword.gif"/></span></div>';
				}
			}
			else 
			{
				if(select1 <=0)// select0이 있고 select1이 없는경우,
				{
					//select1로 추가
					html +='<div class="divSelect1" >';
					html +='<select name="select1" >';
					html +='<option value="0">' + VAR_SESSIONMOR_CIP + '</option>';
					html +='<option value="1">' + VAR_SESSIONMOR_VIP + '</option>';
					html +='<option value="2">' + VAR_SESSIONMOR_REALIP + '</option>';
					html +='<option value="3">' + VAR_SESSIONMOR_CPORT + '</option>';
					html +='<option value="4">' + VAR_SESSIONMOR_VPORT + '</option>';
					html +='<option value="5">' + VAR_SESSIONMOR_REALPORT + '</option>';
					if (adcSetting.getAdc().type != "Alteon")
					{
						html +='<option value="6">' + VAR_SESSIONMOR_PROTO + '</option>';
					}
					html +='</select>&nbsp;&nbsp;';
					html +='<span class="valueArea">';
					html +='<input type="text"  name="srcIp" class="inputText width130">';	
					html +='</span>&nbsp;';
					html +='<span title=' + '"' + VAR_SESSIONMOR_DEL + '"' + 'class="delete1"  href="#">';
					if (langCode == "ko_KR")
					{
						html +='<img src="/imgs/btn/btn_delkeyword.gif"/></span></div>';
					}
					else if (langCode == "en_US")
					{
						html +='<img src="/imgs/btn_eng/btn_delkeyword.gif"/></span></div>';
					}
					else
					{
						html +='<img src="/imgs/btn/btn_delkeyword.gif"/></span></div>';
					}
				}
				else
				{
					if(select2 <=0) //select1이 있고 select2가 없는경우,
					{
						//select2로 추가
						html +='<div class="divSelect2" >';
						html +='<select name="select2" >';
						html +='<option value="0">' + VAR_SESSIONMOR_CIP + '</option>';
						html +='<option value="1">' + VAR_SESSIONMOR_VIP + '</option>';
						html +='<option value="2">' + VAR_SESSIONMOR_REALIP + '</option>';
						html +='<option value="3">' + VAR_SESSIONMOR_CPORT + '</option>';
						html +='<option value="4">' + VAR_SESSIONMOR_VPORT + '</option>';
						html +='<option value="5">' + VAR_SESSIONMOR_REALPORT + '</option>';
						if (adcSetting.getAdc().type != "Alteon")
						{
							html +='<option value="6">' + VAR_SESSIONMOR_PROTO + '</option>';
						}
						html +='</select>&nbsp;&nbsp;';
						html +='<span class="valueArea">';
						html +='<input type="text"  name="srcIp" class="inputText width130">';	
						html +='</span>&nbsp;';
						html +='<span title=' + '"' + VAR_SESSIONMOR_DEL + '"' + 'class="delete2"  href="#">';
						
						if (langCode == "ko_KR")
						{
							html +='<img src="/imgs/btn/btn_delkeyword.gif"/></span></div>';
						}
						else if (langCode == "en_US")
						{
							html +='<img src="/imgs/btn_eng/btn_delkeyword.gif"/></span></div>';
						}
						else
						{
							html +='<img src="/imgs/btn/btn_delkeyword.gif"/></span></div>';
						}
					}
					else
					{
						if(select3 <=0) //select2 있고, select3이 없는 경우
						{
							//select3으로 추가
							html +='<div class="divSelect3" >';
							html +='<select name="select3" >';
							html +='<option value="0">' + VAR_SESSIONMOR_CIP + '</option>';
							html +='<option value="1">' + VAR_SESSIONMOR_VIP + '</option>';
							html +='<option value="2">' + VAR_SESSIONMOR_REALIP + '</option>';
							html +='<option value="3">' + VAR_SESSIONMOR_CPORT + '</option>';
							html +='<option value="4">' + VAR_SESSIONMOR_VPORT + '</option>';
							html +='<option value="5">' + VAR_SESSIONMOR_REALPORT + '</option>';
							if (adcSetting.getAdc().type != "Alteon")
							{
								html +='<option value="6">' + VAR_SESSIONMOR_PROTO + '</option>';
							}
							html +='</select>&nbsp;&nbsp;';
							html +='<span class="valueArea">';
							html +='<input type="text"  name="srcIp" class="inputText width130">';	
							html +='</span>&nbsp;';
							html +='<span title=' + '"' + VAR_SESSIONMOR_DEL + '"' + 'class="delete3"  href="#">';
							if (langCode == "ko_KR")
							{
								html +='<img src="/imgs/btn/btn_delkeyword.gif"/></span></div>';
							}
							else if (langCode == "en_US")
							{
								html +='<img src="/imgs/btn_eng/btn_delkeyword.gif"/></span></div>';
							}
							else
							{
								html +='<img src="/imgs/btn/btn_delkeyword.gif"/></span></div>';
							}
						}
						else
						{
							if(select4 <=0)//selct3 있고 select4가 없는경우
							{
								//select4로 추가
								html +='<div class="divSelect4" >';
								html +='<select name="select4" >';
								html +='<option value="0">' + VAR_SESSIONMOR_CIP + '</option>';
								html +='<option value="1">' + VAR_SESSIONMOR_VIP + '</option>';
								html +='<option value="2">' + VAR_SESSIONMOR_REALIP + '</option>';
								html +='<option value="3">' + VAR_SESSIONMOR_CPORT + '</option>';
								html +='<option value="4">' + VAR_SESSIONMOR_VPORT + '</option>';
								html +='<option value="5">' + VAR_SESSIONMOR_REALPORT + '</option>';
								if (adcSetting.getAdc().type != "Alteon")
								{
									html +='<option value="6">' + VAR_SESSIONMOR_PROTO + '</option>';
								}
								html +='</select>&nbsp;&nbsp;';
								html +='<span class="valueArea">';
								html +='<input type="text"  name="srcIp" class="inputText width130">';	
								html +='</span>&nbsp;';
								html +='<span title=' + '"' + VAR_SESSIONMOR_DEL + '"' + 'class="delete4"  href="#">';
								if (langCode == "ko_KR")
								{
									html +='<img src="/imgs/btn/btn_delkeyword.gif"/></span></div>';
								}
								else if (langCode == "en_US")
								{
									html +='<img src="/imgs/btn_eng/btn_delkeyword.gif"/></span></div>';
								}
								else
								{
									html +='<img src="/imgs/btn/btn_delkeyword.gif"/></span></div>';
								}
							}
						}	
					}	
				}				
			}
*/			
			$area.append(html);
			_checkKeyWordSelectBoxLength();	
			$('.delete0').click(function(e)
			{
				e.stopImmediatePropagation();
				
				if($('select').length == 1)
				{
					$.obAlertNotice(VAR_SESSIONMOR_SRCIDOMI);
					return;
				}
				else
				{
					$('.divSelect0').remove();
					_checkKeyWordSelectBoxLength();	
				}	
			});
			
			$('.delete1').click(function(e)
			{
				e.stopImmediatePropagation();
				
				if($('select').length == 1)
				{
					$.obAlertNotice(VAR_SESSIONMOR_SRCIDOMI);
					return;
				}
				else
				{
					$('.divSelect1').remove();
					_checkKeyWordSelectBoxLength();	
				}	
			});
			$('.delete2').click(function(e)
			{
				e.stopImmediatePropagation();
				
				if($('select').length == 1)
				{
					$.obAlertNotice(VAR_SESSIONMOR_SRCIDOMI);
					return;
				}
				else
				{				
					$('.divSelect2').remove();
					_checkKeyWordSelectBoxLength();
				}	
			});	
			$('.delete3').click(function(e)
			{
				e.stopImmediatePropagation();
				
				if($('select').length == 1)
				{
					$.obAlertNotice(VAR_SESSIONMOR_SRCIDOMI);
					return;
				}
				else
				{
					$('.divSelect3').remove();
					_checkKeyWordSelectBoxLength();
				}	
			});	
			$('.delete4').click(function(e)
			{
				e.stopImmediatePropagation();
				
				if($('select').length == 1)
				{
					$.obAlertNotice(VAR_SESSIONMOR_SRCIDOMI);
					return;
				}
				else
				{
					$('.divSelect4').remove();
					_checkKeyWordSelectBoxLength();
				}	
			});	
		}
	},
	_registSelectChangeEvent : function()
	{
		with(this)
		{
/* 0번째 selectBox 변화시 처리 */			
			$('select[name = "select0"]').change(function(e)
			{
				e.stopImmediatePropagation();
				var selectedVal = $('select[name = "select0"]').val();
				if(selectedVal == 0)//src_ip
				{
					var nameVal = document.getElementsByName('srcIp');
					if(nameVal.length>0)
					{
						e.stopImmediatePropagation();
						$.obAlertNotice(VAR_SESSIONMOR_SRCASEL);
						$('select[name = "select0"] option:eq(0)').attr("selected", "selected");
						return;
					}	
					else
					{
						var pArea = $('.divSelect0 .valueArea').filter(':last');
						pArea.empty();
						var html = "";						
						html +='<input type="text"  name="srcIp" class="inputText width130">';							
						pArea.html(html);
					}	
				}
				else if(selectedVal == 1)//dst_Ip
				{
					e.stopImmediatePropagation();
					var nameVal = document.getElementsByName('dstIp');
					if(nameVal.length>0)
					{
						if(selectedOption == 'SLB')
						{
							$.obAlertNotice(VAR_SESSIONMOR_VIPASEL);
						}
						else
						{
							$.obAlertNotice(VAR_SESSIONMOR_DSTASEL);
						}
						$('select[name = "select0"] option:eq(0)').attr("selected", "selected");
						return;
					}	
					else
					{
						var pArea = $('.divSelect0 .valueArea').filter(':last');
						pArea.empty();
						var html = "";						
						html +='<input type="text"  name="dstIp" class="inputText width130">';							
						pArea.html(html);
					}						
				}	
				else if(selectedVal == 2)//real_Ip
				{
					e.stopImmediatePropagation();
					var nameVal = document.getElementsByName('realIP');
					if(nameVal.length>0)
					{
						$.obAlertNotice(VAR_SESSIONMOR_REALASEL);
						$('select[name = "select0"] option:eq(0)').attr("selected", "selected");
						return;
					}	
					else
					{
						var pArea = $('.divSelect0 .valueArea').filter(':last');
						pArea.empty();
						var html = "";						
						html +='<input type="text"  name="realIP" class="inputText width130">';							
						pArea.html(html);
					}						
				}					
				else if(selectedVal == 3)//src_port
				{
					e.stopImmediatePropagation();
					var nameVal = document.getElementsByName('srcPort');
					if(nameVal.length>0)
					{
						$.obAlertNotice(VAR_SESSIONMOR_SRCPALSEL);
						$('select[name = "select0"] option:eq(0)').attr("selected", "selected");
						return;
					}	
					else
					{
						var pArea = $('.divSelect0 .valueArea').filter(':last');
						pArea.empty();
						var html = "";						
						html +='<input type="text"  name="srcPort" class="inputText width130">';							
						pArea.html(html);
					}
				}
				else if(selectedVal == 4)//dst_port
				{
					e.stopImmediatePropagation();
					var nameVal = document.getElementsByName('dstPort');
					if(nameVal.length>0)
					{
						if(selectedOption == 'SLB')
						{
							$.obAlertNotice(VAR_SESSIONMOR_VPASEL);
						}
						else
						{
							$.obAlertNotice(VAR_SESSIONMOR_DSTPASEL);
						}
						$('select[name = "select0"] option:eq(0)').attr("selected", "selected");
						return;
					}	
					else
					{
						var pArea = $('.divSelect0 .valueArea').filter(':last');
						pArea.empty();
						var html = "";						
						html +='<input type="text"  name="dstPort" class="inputText width130">';							
						pArea.html(html);
					}
				}
				else if(selectedVal == 5)//real_port
				{
					e.stopImmediatePropagation();
					var nameVal = document.getElementsByName('realPort');
					if(nameVal.length>0)
					{
						$.obAlertNotice(VAR_SESSIONMOR_REALPASEL);
						$('select[name = "select0"] option:eq(0)').attr("selected", "selected");
						return;
					}	
					else
					{
						var pArea = $('.divSelect0 .valueArea').filter(':last');
						pArea.empty();
						var html = "";						
						html +='<input type="text"  name="realPort" class="inputText width130">';							
						pArea.html(html);
					}
				}				
				else if(selectedVal == 6)//protocol
				{
					e.stopImmediatePropagation();
					var nameVal = document.getElementsByName('protocol');
					if(nameVal.length>0)
					{
						$.obAlertNotice(VAR_SESSIONMOR_PROALRSEL);
						$('select[name = "select0"] option:eq(0)').attr("selected", "selected");
						return;
					}	
					else
					{	
						var pArea = $('.divSelect0 .valueArea').filter(':last');
						pArea.empty();
						var html = "";						
						html +='<select name="protocol" class="inputSelect width134">';
						html +='<option value="1">' + VAR_COMMON_TCP + '</option>';
						html +='<option value="2">' + VAR_COMMON_UDP + '</option>';
						html +='<option value="2">' + VAR_COMMON_ICMP + '</option>';
						html +='</select>';
						
						pArea.html(html);
					}
				}
				else if(selectedVal == 7)//real_port
				{
					e.stopImmediatePropagation();
					var nameVal = document.getElementsByName('agingTime');
					if(nameVal.length>0)
					{
						$.obAlertNotice(VAR_SESSIONMOR_AGESEL);
						$('select[name = "select0"] option:eq(0)').attr("selected", "selected");
						return;
					}	
					else
					{
						var pArea = $('.divSelect0 .valueArea').filter(':last');
						pArea.empty();
						var html = "";						
						html +='<input type="text"  name="agingTime" class="inputText width130">';							
						pArea.html(html);
					}
				}	
			});
/* 1번째 selectBox 변화시 처리 */			
			$('select[name = "select1"]').change(function(e)
			{
				e.stopImmediatePropagation();
				var selectedVal = $('select[name = "select1"]').val();
				if(selectedVal == 0)//src_ip
				{
					e.stopImmediatePropagation();
					var nameVal = document.getElementsByName('srcIp');
					if(nameVal.length>0)
					{

						$.obAlertNotice(VAR_SESSIONMOR_SRCASEL);
						$('select[name = "select1"] option:eq(0)').attr("selected", "selected");
						return;
					}	
					else
					{
						var pArea = $('.divSelect1 .valueArea').filter(':last');
						pArea.empty();
						var html = "";						
						html +='<input type="text"  name="srcIp" class="inputText width130">';							
						pArea.html(html);
					}	
				}
				else if(selectedVal == 1)//dst_Ip
				{
					e.stopImmediatePropagation();
					var nameVal = document.getElementsByName('dstIp');
					if(nameVal.length>0)
					{
						e.stopImmediatePropagation();
						if(selectedOption == 'SLB')
						{
							$.obAlertNotice(VAR_SESSIONMOR_VIPASEL);
						}
						else
						{
							$.obAlertNotice(VAR_SESSIONMOR_DSTASEL);
						}
						return;
					}	
					else
					{
						var pArea = $('.divSelect1 .valueArea').filter(':last');
						pArea.empty();
						var html = "";						
						html +='<input type="text"  name="dstIp" class="inputText width130">';							
						pArea.html(html);
					}					
				}	
				else if(selectedVal == 2)//real_Ip
				{
					e.stopImmediatePropagation();
					var nameVal = document.getElementsByName('realIP');
					if(nameVal.length>0)
					{
						e.stopImmediatePropagation();
						$.obAlertNotice(VAR_SESSIONMOR_REALASEL);
						return;
					}	
					else
					{
						var pArea = $('.divSelect1 .valueArea').filter(':last');
						pArea.empty();
						var html = "";						
						html +='<input type="text"  name="realIP" class="inputText width130">';							
						pArea.html(html);
					}					
				}		
				else if(selectedVal == 3)//src_port
				{
					e.stopImmediatePropagation();
					var nameVal = document.getElementsByName('srcPort');
					if(nameVal.length>0)
					{
						$.obAlertNotice(VAR_SESSIONMOR_SRCPALSEL);
						return;
					}	
					else
					{
						var pArea = $('.divSelect1 .valueArea').filter(':last');
						pArea.empty();
						var html = "";						
						html +='<input type="text"  name="srcPort" class="inputText width130">';							
						pArea.html(html);
					}
				}
				else if(selectedVal == 4)//dst_port
				{
					e.stopImmediatePropagation();
					var nameVal = document.getElementsByName('dstPort');
					if(nameVal.length>0)
					{
						e.stopImmediatePropagation();
						if(selectedOption == 'SLB')
						{
							$.obAlertNotice(VAR_SESSIONMOR_VPASEL);
						}
						else
						{
							$.obAlertNotice(VAR_SESSIONMOR_DSTPASEL);
						}
						return;
					}	
					else
					{
						var pArea = $('.divSelect1 .valueArea').filter(':last');
						pArea.empty();
						var html = "";						
						html +='<input type="text"  name="dstPort" class="inputText width130">';							
						pArea.html(html);
					}
				}
				else if(selectedVal == 5)//real_port
				{
					e.stopImmediatePropagation();
					var nameVal = document.getElementsByName('realPort');
					if(nameVal.length>0)
					{
						e.stopImmediatePropagation();
						$.obAlertNotice(VAR_SESSIONMOR_REALPASEL);
						return;
					}	
					else
					{
						var pArea = $('.divSelect1 .valueArea').filter(':last');
						pArea.empty();
						var html = "";						
						html +='<input type="text"  name="realPort" class="inputText width130">';							
						pArea.html(html);
					}
				}				
				else if(selectedVal == 6)//protocol
				{
					e.stopImmediatePropagation();
					var nameVal = document.getElementsByName('protocol');
					if(nameVal.length>0)
					{
						e.stopImmediatePropagation();
						$.obAlertNotice(VAR_SESSIONMOR_PROALRSEL);
						return;
					}	
					else
					{	
						var pArea = $('.divSelect1 .valueArea').filter(':last');
						pArea.empty();
						var html = "";						
						html +='<select name="protocol" class="inputSelect width134">';
						html +='<option value="1">' + VAR_COMMON_TCP + '</option>';
						html +='<option value="2">' + VAR_COMMON_UDP + '</option>';
						html +='<option value="2">' + VAR_COMMON_ICMP + '</option>';
						html +='</select>';
						
						pArea.html(html);
					}
				}
				else if(selectedVal == 7)//real_port
				{
					e.stopImmediatePropagation();
					var nameVal = document.getElementsByName('agingTime');
					if(nameVal.length>0)
					{
						e.stopImmediatePropagation();
						$.obAlertNotice(VAR_SESSIONMOR_AGESEL);
						return;
					}	
					else
					{
						var pArea = $('.divSelect1 .valueArea').filter(':last');
						pArea.empty();
						var html = "";						
						html +='<input type="text"  name="agingTime" class="inputText width130">';							
						pArea.html(html);
					}
				}
			});			
/*2번째 selectBox 변화시 처리*/
			$('select[name = "select2"]').change(function(e)
			{
				e.stopImmediatePropagation();
				var selectedVal = $('select[name = "select2"]').val();
				if(selectedVal == 0)//src_ip
				{
					var nameVal = document.getElementsByName('srcIp');
					if(nameVal.length>0)
					{	
						e.stopImmediatePropagation();
						$.obAlertNotice(VAR_SESSIONMOR_SRCASEL);
						$('select[name = "select2"] option:eq(0)').attr("selected", "selected");
						return;
					}	
					else
					{
						var pArea = $('.divSelect2 .valueArea').filter(':last');
						pArea.empty();
						var html = "";						
						html +='<input type="text"  name="srcIp" class="inputText width130">';							
						pArea.html(html);
					}	
				}
				else if(selectedVal == 1)//dst_Ip
				{
					e.stopImmediatePropagation();
					var nameVal = document.getElementsByName('dstIp');
					if(nameVal.length>0)
					{
						if(selectedOption == 'SLB')
						{
							$.obAlertNotice(VAR_SESSIONMOR_VIPASEL);
						}
						else
						{
							$.obAlertNotice(VAR_SESSIONMOR_DSTASEL);
						}
						$('select[name = "select2"] option:eq(0)').attr("selected", "selected");
						return;
					}	
					else
					{	
						var pArea = $('.divSelect2 .valueArea').filter(':last');
						pArea.empty();
						var html = "";						
						html +='<input type="text"  name="dstIp" class="inputText width130">';							
						pArea.html(html);
					}	
					
				}	
				else if(selectedVal == 2)//real_Ip
				{
					e.stopImmediatePropagation();
					var nameVal = document.getElementsByName('realIP');
					if(nameVal.length>0)
					{
						$.obAlertNotice(VAR_SESSIONMOR_REALASEL);
						$('select[name = "select2"] option:eq(0)').attr("selected", "selected");
						return;
					}	
					else
					{	
						var pArea = $('.divSelect2 .valueArea').filter(':last');
						pArea.empty();
						var html = "";						
						html +='<input type="text"  name="realIP" class="inputText width130">';							
						pArea.html(html);
					}	
					
				}					
				else if(selectedVal == 3)//src_port
				{
					e.stopImmediatePropagation();
					var nameVal = document.getElementsByName('srcPort');
					if(nameVal.length>0)
					{
						$.obAlertNotice(VAR_SESSIONMOR_SRCPALSEL);
						$('select[name = "select2"] option:eq(0)').attr("selected", "selected");
						return;
					}	
					else
					{
						var pArea = $('.divSelect2 .valueArea').filter(':last');
						pArea.empty();
						var html = "";						
						html +='<input type="text"  name="srcPort" class="inputText width130">';							
						pArea.html(html);
					}
				}
				else if(selectedVal == 4)//dst_port
				{
					e.stopImmediatePropagation();
					var nameVal = document.getElementsByName('dstPort');
					if(nameVal.length>0)
					{
						if(selectedOption == 'SLB')
						{
							$.obAlertNotice(VAR_SESSIONMOR_VPASEL);
						}
						else
						{
							$.obAlertNotice(VAR_SESSIONMOR_DSTPASEL);
						}
						$('select[name = "select2"] option:eq(0)').attr("selected", "selected");
						return;
					}	
					else
					{
						var pArea = $('.divSelect2 .valueArea').filter(':last');
						pArea.empty();
						var html = "";						
						html +='<input type="text"  name="dstPort" class="inputText width130">';							
						pArea.html(html);
					}
				}
				else if(selectedVal == 5)//real_port
				{
					e.stopImmediatePropagation();
					var nameVal = document.getElementsByName('realPort');
					if(nameVal.length>0)
					{
						$.obAlertNotice(VAR_SESSIONMOR_REALPASEL);
						$('select[name = "select2"] option:eq(0)').attr("selected", "selected");
						return;
					}	
					else
					{
						var pArea = $('.divSelect2 .valueArea').filter(':last');
						pArea.empty();
						var html = "";						
						html +='<input type="text"  name="realPort" class="inputText width130">';							
						pArea.html(html);
					}
				}				
				else if(selectedVal == 6)//protocol
				{
					e.stopImmediatePropagation();
					var nameVal = document.getElementsByName('protocol');
					if(nameVal.length>0)
					{
						$.obAlertNotice(VAR_SESSIONMOR_PROALRSEL);
						$('select[name = "select2"] option:eq(0)').attr("selected", "selected");
						return;
					}	
					else
					{	
						var pArea = $('.divSelect2 .valueArea').filter(':last');
						pArea.empty();
						var html = "";						
						html +='<select name="protocol" class="inputSelect width134">';
						html +='<option value="1">' + VAR_COMMON_TCP + '</option>';
						html +='<option value="2">' + VAR_COMMON_UDP + '</option>';
						html +='<option value="2">' + VAR_COMMON_ICMP + '</option>';
						html +='</select>';
						
						pArea.html(html);
					}
				}
				else if(selectedVal == 7)//real_port
				{
					e.stopImmediatePropagation();
					var nameVal = document.getElementsByName('agingTime');
					if(nameVal.length>0)
					{
						$.obAlertNotice(VAR_SESSIONMOR_AGESEL);
						$('select[name = "select2"] option:eq(0)').attr("selected", "selected");
						return;
					}	
					else
					{
						var pArea = $('.divSelect2 .valueArea').filter(':last');
						pArea.empty();
						var html = "";						
						html +='<input type="text"  name="agingTime" class="inputText width130">';							
						pArea.html(html);
					}
				}
			});
/*3번째 selectBox 변화시 처리*/
			$('select[name = "select3"]').change(function(e)
			{
				e.stopImmediatePropagation();

				var selectedVal = $('select[name = "select3"]').val();
				if(selectedVal == 0)//src_ip
				{
					e.stopImmediatePropagation();
					var nameVal = document.getElementsByName('srcIp');
					if(nameVal.length>0)
					{
						$.obAlertNotice(VAR_SESSIONMOR_SRCASEL);
						$('select[name = "select3"] option:eq(0)').attr("selected", "selected");
						return;
					}	
					else
					{
						var pArea = $('.divSelect3 .valueArea').filter(':last');
						pArea.empty();
						var html = "";						
						html +='<input type="text"  name="srcIp" class="inputText width130">';							
						pArea.html(html);
					}	
				}
				else if(selectedVal == 1)//dst_Ip
				{
					e.stopImmediatePropagation();
					var nameVal = document.getElementsByName('dstIp');
					if(nameVal.length>0)
					{
						if(selectedOption == 'SLB')
						{
							$.obAlertNotice(VAR_SESSIONMOR_VIPASEL);
						}
						else
						{
							$.obAlertNotice(VAR_SESSIONMOR_DSTASEL);
						}
						$('select[name = "select3"] option:eq(0)').attr("selected", "selected");
						return;
					}	
					else
					{
						var pArea = $('.divSelect3 .valueArea').filter(':last');
						pArea.empty();
						var html = "";						
						html +='<input type="text"  name="dstIp" class="inputText width130">';							
						pArea.html(html);
					}					
				}	
				else if(selectedVal == 2)//real_Ip
				{
					e.stopImmediatePropagation();
					var nameVal = document.getElementsByName('realIP');
					if(nameVal.length>0)
					{
						$.obAlertNotice(VAR_SESSIONMOR_REALASEL);
						$('select[name = "select3"] option:eq(0)').attr("selected", "selected");
						return;
					}	
					else
					{
						var pArea = $('.divSelect3 .valueArea').filter(':last');
						pArea.empty();
						var html = "";						
						html +='<input type="text"  name="realIP" class="inputText width130">';							
						pArea.html(html);
					}					
				}
				else if(selectedVal == 3)//src_port
				{
					e.stopImmediatePropagation();
					var nameVal = document.getElementsByName('srcPort');
					if(nameVal.length>0)
					{
						$.obAlertNotice(VAR_SESSIONMOR_SRCPALSEL);
						$('select[name = "select3"] option:eq(0)').attr("selected", "selected");
						return;
					}	
					else
					{
						var pArea = $('.divSelect3 .valueArea').filter(':last');
						pArea.empty();
						var html = "";						
						html +='<input type="text"  name="srcPort" class="inputText width130">';							
						pArea.html(html);
					}
				}
				else if(selectedVal == 4)//dst_port
				{
					e.stopImmediatePropagation();
					var nameVal = document.getElementsByName('dstPort');
					if(nameVal.length>0)
					{
						if(selectedOption == 'SLB')
						{
							$.obAlertNotice(VAR_SESSIONMOR_VPASEL);
						}
						else
						{
							$.obAlertNotice(VAR_SESSIONMOR_DSTPASEL);
						}
						$('select[name = "select3"] option:eq(0)').attr("selected", "selected");
						return;
					}	
					else
					{
						var pArea = $('.divSelect3 .valueArea').filter(':last');
						pArea.empty();
						var html = "";						
						html +='<input type="text"  name="dstPort" class="inputText width130">';							
						pArea.html(html);
					}
				}
				else if(selectedVal == 5)//real_port
				{
					e.stopImmediatePropagation();
					var nameVal = document.getElementsByName('realPort');
					if(nameVal.length>0)
					{
						$.obAlertNotice(VAR_SESSIONMOR_REALPASEL);
						$('select[name = "select3"] option:eq(0)').attr("selected", "selected");
						return;
					}	
					else
					{
						var pArea = $('.divSelect3 .valueArea').filter(':last');
						pArea.empty();
						var html = "";						
						html +='<input type="text"  name="realPort" class="inputText width130">';							
						pArea.html(html);
					}
				}
				else if(selectedVal == 6)//protocol
				{
					e.stopImmediatePropagation();
					var nameVal = document.getElementsByName('protocol');
					if(nameVal.length>0)
					{
						$.obAlertNotice(VAR_SESSIONMOR_PROALRSEL);
						$('select[name = "select3"] option:eq(0)').attr("selected", "selected");
						return;
					}	
					else
					{
						var pArea = $('.divSelect3 .valueArea').filter(':last');
						pArea.empty();
						var html = "";						
						html +='<select name="protocol" class="inputSelect width134">';
						html +='<option value="1">' + VAR_COMMON_TCP + '</option>';
						html +='<option value="2">' + VAR_COMMON_UDP + '</option>';
						html +='<option value="2">' + VAR_COMMON_ICMP + '</option>';
						html +='</select>';
						
						pArea.html(html);
					}
				}
				else if(selectedVal == 7)//real_port
				{
					e.stopImmediatePropagation();
					var nameVal = document.getElementsByName('agingTime');
					if(nameVal.length>0)
					{
						$.obAlertNotice(VAR_SESSIONMOR_AGESEL);
						$('select[name = "select3"] option:eq(0)').attr("selected", "selected");
						return;
					}	
					else
					{
						var pArea = $('.divSelect3 .valueArea').filter(':last');
						pArea.empty();
						var html = "";						
						html +='<input type="text"  name="agingTime" class="inputText width130">';							
						pArea.html(html);
					}
				}
			});
/*4번째 selectBox 변화시 처리*/
			$('select[name = "select4"]').change(function(e)
			{
				e.stopImmediatePropagation();

				var selectedVal = $('select[name = "select4"]').val();
				if(selectedVal == 0)//src_ip
				{
					e.stopImmediatePropagation();
					var nameVal = document.getElementsByName('srcIp');
					if(nameVal.length>0)
					{
						$.obAlertNotice(VAR_SESSIONMOR_SRCASEL);
						$('select[name = "select4"] option:eq(0)').attr("selected", "selected");
						return;
					}	
					else
					{
						var pArea = $('.divSelect4 .valueArea').filter(':last');
						pArea.empty();
						var html = "";						
						html +='<input type="text"  name="srcIp" class="inputText width130">';							
						pArea.html(html);
					}	
				}
				else if(selectedVal == 1)//dst_Ip
				{
					e.stopImmediatePropagation();
					var nameVal = document.getElementsByName('dstIp');
					if(nameVal.length>0)
					{
						if(selectedOption == 'SLB')
						{
							$.obAlertNotice(VAR_SESSIONMOR_VIPASEL);
						}
						else
						{
							$.obAlertNotice(VAR_SESSIONMOR_DSTASEL);
						}
						$('select[name = "select4"] option:eq(0)').attr("selected", "selected");
						return;
					}	
					else
					{
						var pArea = $('.divSelect4 .valueArea').filter(':last');
						pArea.empty();
						var html = "";						
						html +='<input type="text"  name="dstIp" class="inputText width130">';							
						pArea.html(html);
					}	
				}	
				else if(selectedVal == 2)//real_Ip
				{
					e.stopImmediatePropagation();
					var nameVal = document.getElementsByName('realIP');
					if(nameVal.length>0)
					{
						$.obAlertNotice(VAR_SESSIONMOR_REALASEL);
						$('select[name = "select4"] option:eq(0)').attr("selected", "selected");
						return;
					}	
					else
					{
						var pArea = $('.divSelect4 .valueArea').filter(':last');
						pArea.empty();
						var html = "";						
						html +='<input type="text"  name="realIP" class="inputText width130">';							
						pArea.html(html);
					}	
					
				}	
				else if(selectedVal == 3)//src_port
				{
					e.stopImmediatePropagation();
					var nameVal = document.getElementsByName('srcPort');
					if(nameVal.length>0)
					{
						$.obAlertNotice(VAR_SESSIONMOR_SRCPALSEL);
						$('select[name = "select4"] option:eq(0)').attr("selected", "selected");
						return;
					}	
					else
					{
						var pArea = $('.divSelect4 .valueArea').filter(':last');
						pArea.empty();
						var html = "";						
						html +='<input type="text"  name="srcPort" class="inputText width130">';							
						pArea.html(html);
					}
				}
				else if(selectedVal == 4)//dst_port
				{
					e.stopImmediatePropagation();
					var nameVal = document.getElementsByName('dstPort');
					if(nameVal.length>0)
					{
						if(selectedOption == 'SLB')
						{
							$.obAlertNotice(VAR_SESSIONMOR_VPASEL);
						}
						else
						{
							$.obAlertNotice(VAR_SESSIONMOR_DSTPASEL);
						}
						$('select[name = "select4"] option:eq(0)').attr("selected", "selected");
						return;
					}	
					else
					{
						var pArea = $('.divSelect4 .valueArea').filter(':last');
						pArea.empty();
						var html = "";						
						html +='<input type="text"  name="dstPort" class="inputText width130">';							
						pArea.html(html);
					}
				}
				else if(selectedVal == 5)//real_port
				{
					e.stopImmediatePropagation();
					var nameVal = document.getElementsByName('realPort');
					if(nameVal.length>0)
					{
						$.obAlertNotice(VAR_SESSIONMOR_REALPASEL);
						$('select[name = "select4"] option:eq(0)').attr("selected", "selected");
						return;
					}	
					else
					{
						var pArea = $('.divSelect4 .valueArea').filter(':last');
						pArea.empty();
						var html = "";						
						html +='<input type="text"  name="realPort" class="inputText width130">';							
						pArea.html(html);
					}
				}				
				else if(selectedVal == 6)//protocol
				{
					e.stopImmediatePropagation();
					var nameVal = document.getElementsByName('protocol');
					if(nameVal.length>0)
					{
						$.obAlertNotice(VAR_SESSIONMOR_PROALRSEL);
						$('select[name = "select4"] option:eq(0)').attr("selected", "selected");
						return;
					}	
					else
					{
						var pArea = $('.divSelect4 .valueArea').filter(':last');
						pArea.empty();
						var html = "";						
						html +='<select name="protocol" class="inputSelect width134">';
						html +='<option value="1">' + VAR_COMMON_TCP + '</option>';
						html +='<option value="2">' + VAR_COMMON_UDP + '</option>';
						html +='<option value="2">' + VAR_COMMON_ICMP + '</option>';
						html +='</select>';
						
						pArea.html(html);
					}
				}
				else if(selectedVal == 7)//real_port
				{
					e.stopImmediatePropagation();
					var nameVal = document.getElementsByName('agingTime');
					if(nameVal.length>0)
					{
						$.obAlertNotice(VAR_SESSIONMOR_AGESEL);
						$('select[name = "select4"] option:eq(0)').attr("selected", "selected");
						return;
					}	
					else
					{
						var pArea = $('.divSelect4 .valueArea').filter(':last');
						pArea.empty();
						var html = "";						
						html +='<input type="text"  name="agingTime" class="inputText width130">';							
						pArea.html(html);
					}
				}
			});	
		}		
	},

	_checkExportSessionInfoDataExist : function()
	{
		with(this)
		{
			ajaxManager.runJsonExt
			({
				url : "monitor/checkExportSessionInfoDataExist.action",
				data : 
				{
					"adcObj.index" : adcSetting.getAdc().index,
					"adcObj.category" : adcSetting.getSelectIndex(),
					"adc.type" : adcSetting.getAdc().type,
					"orderDir" : this.orderDir,
					"orderType" : this.orderType,			
					"srcIp" : $('input[name=srcIp]').val(),
					"dstIp" : $('input[name=dstIp]').val(),
					"realIP" : $('input[name=realIP]').val(),
					"srcPort" : $('input[name=srcPort]').val(),
					"dstPort" : $('input[name=dstPort]').val(),
					"realPort" : $('input[name=realPort]').val(),
					"protocol" : $('select[name=protocol]').val(),
					"agingTime" : $('input[name=agingTime]').val()
					
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
//					exceptionEvent();
				}
			});
		}
	},
	
	exportCss : function()
	{
		with (this)
		{
			if (!adcSetting.isAdcSet())
			{
				return;
			}

			var url = "monitor/downloadfaultSessionInfo.action?adcObj.category=" + adcSetting.getSelectIndex() +"&adcObj.index=" + adcSetting.getAdc().index +"&adc.type="+adcSetting.getAdc().type+ "&orderDir=" + this.orderDir + "&orderType=" + this.orderType + "&selectedOption=" + selectedOption;	
			$('#downloadFrame').attr('src', url);
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