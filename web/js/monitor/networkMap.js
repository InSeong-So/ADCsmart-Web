var NetworkMap = Class.create({
	initialize : function() 
	{
		this.adc;
		this.searchedKey;
		this.status;
		// 0 = All, 1 = slb, 2 = flb
		this.lbClass = 1;
		this.vsIndex;
		this.port;
		this.searchFlag = false;
		this.orderDir = 1; // 내림차순 = 2
        this.orderType = 34; // 
	},
	setAdc : function(adc) 
	{
		this.adc.index = adc.index;
		this.adc.type = adc.type;
		this.adc.name = adc.name;
	},
	onAdcChange : function() 
	{
		with(this)
		{
			this.orderDir = 1; // 내림차순 = 2
	        this.orderType = 34; // 
			loadNetworkMapContent(adcSetting.getAdc());
		}
	},
	loadNetworkMapContent : function(adc, lbClass, searchKey, status, taskQ) 
	{
		this.adc = adc;
		this.searchedKey = searchKey;
		this.status = status;
		with (this) 
		{
			if (!adc) 
			{
				return;
			}
			if (adcSetting.getIsFlb() == 0)
			{
				lbClass = 1;
			}
			
//			if($('#sortTypeArea option:selected').val() != undefined)
//			{
//				var sortType = $('#sortTypeArea option:selected').val().trim();
//				orderType = sortType.split("_")[0];
//				orderDiretion = sortType.split("_")[1];
//			}
			var params = 
			{
				"adc.index"	: adc.index,
				"adc.type"	: adc.type,
				"adc.name"	: adc.name,
				"lbType"	: lbClass,
				"adc.isFlb" : adcSetting.getIsFlb(),
				"orderOption.orderType" : orderType,
				"orderOption.orderDirection" : orderDir
			};
			if (searchedKey) 
			{
				params["searchKey"] = searchedKey;
			}
			if (status) 
			{
				params["status"] = status;
			}
			
			ajaxManager.runHtml({
				url			: "monitor/loadNetworkMapContent.action",
				data		: params,
				target		: "#wrap .contents",
				successFn	: function(params) 
				{
					header.setActiveMenu('MonitorNetwork');
					applyNetworkMapContentCss();
					registNetworkMapContentEvents();					
					
//					if (status === undefined)
//					{
//						if (adcSetting.getAdcStatus() != "available")
//						{
//							if ($('.networkMapVsList').size() > 0)
//							{
//								
//							}
//							else
//							{
//								$('.contents_area .nulldata').removeClass("none");
//								if(langCode=="ko_KR")
//								{
//									$('.contents_area .nulldata').css('background-image', "url(imgs/monitoring/networkMap_null.gif)");
//								}
//								else
//								{
//									$('.contents_area .nulldata').css('background-image', "url(imgs/monitoring_eng/networkMap_null.gif)");
//								}
//								$('.contents_area .successdata').addClass("none");
//							}		
//						}
//					}
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_NETWORK_VSSUMMARYLOAD, jqXhr);
				}
			}, taskQ);
		}
	},
	loadNetworkMapTableInContent : function(adc, lbClass, searchKey, status, taskQ) 
	{
		this.adc = adc;
		this.searchedKey = searchKey;
		this.status = status;
		with (this) 
		{
			if (!adc) 
			{
				return;
			}
			if (adcSetting.getIsFlb() == 0)
			{
				lbClass = 1;
			}
			
//			var sortType = $('#sortTypeArea option:selected').val().trim();
//			orderType = sortType.split("_")[0];
//			orderDiretion = sortType.split("_")[1];
			
			var params = 
			{
				"adc.index"	: adc.index,
				"adc.type"	: adc.type,
				"adc.name"	: adc.name,
				"lbType"	: lbClass,
				"adc.isFlb" : adcSetting.getIsFlb(),
				"orderOption.orderType" : orderType,
				"orderOption.orderDirection" : orderDir
			};
			if (searchedKey) 
			{
				params["searchKey"] = searchedKey;
			}
			if (status) 
			{
				params["status"] = status;
			}
			
			ajaxManager.runHtml({
				url			: "monitor/loadNetworkMapTableInContent.action",
				data		: params,
				target: "div.columns",
				successFn	: function(params) 
				{
					registNetworkMapContentEvents();
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_NETWORK_VSSUMMARYLOAD, jqXhr);
				}
			}, taskQ);
		}
	},
	saveVSDescription : function(adcIndex, adcType, vsIndex, vsvcIndex, vsDescription)
	{
		with(this)
		{
			ajaxManager.runJsonExt({
				url:"monitor/saveVsDescription.action",
				data : 
				{
					"adcIndex" : adcIndex,
					"adcType" : adcType,
					"vsIndex" : vsIndex,
					"vsvcIndex" : vsvcIndex,
					"vsDescription" : vsDescription
				},
				successFn : function(data)
				{
					var sortType = $('#sortTypeArea option:selected').val().trim();
					orderType = sortType.split("_")[0];
					orderDir = sortType.split("_")[1];
					loadNetworkMapTableInContent(adc, lbClass, searchedKey, status, taskQ);
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError("VS 설명 저장에 실패했습니다.", jqXhr);
				}
			});
		}
	},	
	loadVsTrafficInfo : function(vsIndex, lbSelect, port) 
	{
		with (this) 
		{
			if (!adc || !vsIndex) 
			{
				return;
			}
			
//			var sortType = $('#sortTypeArea option:selected').val().trim();
//			orderType = sortType.split("_")[0];
//			orderDiretion = sortType.split("_")[1];
			
			var params = 
			{
				"adc.index"	: adc.index,
				"adc.type"	: adc.type,
				"adc.name"	: adc.name,
				"vsIndex"	: vsIndex,
				"flbSelect" : lbSelect,
				"orderOption.orderType" : orderType,
				"orderOption.orderDirection" : orderDir
			};
			if (0 === port || port) 
			{
				params["port"] = port;
			}
			ajaxManager.runHtml({
				url			: "monitor/loadVsTrafficInfo.action",
				data		: params,
				target		: "#trafficDesc",
				successFn	: function(data) 
				{
					showPopup({
						"id" : "#trafficDesc", 
						"width" : "1024px",/*"861px"*/
						"height" : "264px"							
					});
					
					$('.popupclosebtn').click(function(e)
					{
						e.preventDefault();
						$('.popup_type1').remove();
						$('.cloneDiv').remove();
					});
					
					if(lbSelect == 2)
					{
						loadTableVsTrafficInfo(vsIndex, port, "monitor/loadMemberVsTrafficInfo.action");
					}
					else
					{
						loadTableVsTrafficInfo(vsIndex, port, "monitor/loadVirtualVsTrafficInfo.action");
					}
//					applyVsTrafficInfoCss();
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_NETWORK_VSSUMMARYLOAD, jqXhr);
				}
			});
		}
	},
	loadTableVsTrafficInfo : function(vsIndex, port, option) 
	{
		with (this) 
		{
			if (!adc || !vsIndex) 
			{
				return;
			}
			var params = 
			{
				"adc.index"	: adc.index,
				"adc.type"	: adc.type,
				"adc.name"	: adc.name,
				"vsIndex"	: vsIndex,
			};
			if (0 === port || port) 
			{
				params["port"] = port;
			}
			ajaxManager.runHtml({
				url			: option,
				data		: params,
				target		: ".VsMonitorTrafficTable",
				successFn	: function(data) 
				{
					registVsTrafficContentEvents();
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_NETWORK_VSSUMMARYLOAD, jqXhr);
				}
			});
		}
	},

	popUpVsDescription : function(adcIndex, adcType, vsName, vsIndex, vsvcIndex, vsDescription) 
	{
		with (this) 
		{
			var titleArea = $(".vsDescription-title").filter(':last');
			titleArea.empty().html(vsName);
			var inputArea = $(".description").filter(':last');
			var inputText = '설명: <input type="text" id="vsDescription" style="width:85%" value="'+vsDescription+'">'
			inputArea.empty().html(inputText);
			
			showPopup({
				'id' : '#vs-description-pop',
				'width' : '400px'
			});
			$('.popupclosebtn').click(function(e)
			{
				var newDescription = $(this).parents('div').parents('div').find("#vsDescription").val();
				saveVSDescription(adcIndex, adcType, vsIndex, vsvcIndex, newDescription);
				e.preventDefault();
				$('.popup_type1').remove();
				$('.cloneDiv').remove();
			});
			
			$('.popupcancelbtn').click(function(e)
			{
				e.preventDefault();
				$('.popup_type1').remove();
				$('.cloneDiv').remove();
			});
		}
	},
	
	popUpBackup : function(vsname, port, BakupType, groupBakupId, backupList, backupStatus) 
	{
		with (this) 
		{
			var output = '';
			var empty = groupBakupId.text();
			var groupId = empty.match(/[^\_]\d*$/g);
			var titleArea = $(".backup-title").filter(':last');
			var actionArea = $(".backup-group").filter(':last');
			for ( var i = 0; i < backupList.length; i++) {
				output += '<tr class="ContentsLine">' + '<td align="center">' + backupList[i].textContent +
					'</td></tr><tr class="DivideLine"><td colspan="1"></td></tr>';
			}
			if(port == -1)
			{
				port = '';
			}
			else
			{
				port = ' port ' + port;
			}
			
			titleArea.empty().html(BakupType + groupId + ' (' +vsname + port + ')');
			actionArea.empty().html(output);
			showPopup({
				'id' : '#group-backup-pop',
				'width' : '400px'
			});
			$('.popupclosebtn').click(function(e)
			{
				e.preventDefault();
				$('.popup_type1').remove();
				$('.cloneDiv').remove();
			});
		}
	},
	applyNetworkMapContentCss : function() 
	{
		with (this) 
		{
			$('.table_type1').each(function() 
			{
				var tableId = $(this).attr('id');
				initTable([ "#" + tableId + " tbody tr" ], [ 0, 1 ], [ -1 ]);
			});
			
//			$('.server_cnt').css('background', 'white').css("cursor", "pointer");
			
			if (!status) 
			{
//				$('.server_cnt').eq(0).css('background', '#e5f2ff');
			} 
			else 
			{
				$('.server_cnt').each(function() 
				{
					if ($(this).find('.status').text() === String(status)) 
					{
//						alert($(this).find('.status').text());
//						alert("String(status) : " + String(status));
						if (String(status) == 1)
						{
//							$(this).css('background-image', 'url(../imgs/monitoring/bg_conn1.png)');
//							$('.totalCountVs').css('background-image', 'url(../imgs/monitoring/bg_totalCountVs0.png)');							
							$('.conn').addClass('color21');
							$('.conn').removeClass('color2');
							$('.totalCountVs').addClass('color1');
							$('.totalCountVs').removeClass('color11');
						}
						else if (String(status) == 2)
						{
//							$(this).css('background-image', 'url(../imgs/monitoring/bg_disconn1.png)');
//							$('.totalCountVs').css('background-image', 'url(../imgs/monitoring/bg_totalCountVs0.png)');
							$('.disconn').addClass('color31');
							$('.disconn').removeClass('color3');
							$('.totalCountVs').addClass('color1');
							$('.totalCountVs').removeClass('color11');
						}
						else if (String(status) == 0)
						{
							$('.disabled').addClass('color41');
							$('.disabled').removeClass('color4');
							$('.totalCountVs').addClass('color1');
							$('.totalCountVs').removeClass('color11');
						}
						else
						{
//							$(this).css('background-image', 'url(../imgs/monitoring/bg_totalCountVs1.png)');
						}
						return false;
					}
				});
			}
		
		}
	},
	applyVsTrafficInfoCss : function() 
	{
		with (this) 
		{
			initTable([ ".cloneDiv tbody tr" ], [ 0 ], [ -1 ]);
			$('.cloneDiv .vsMemeberIpTd').css("padding-left", "+=15");
			$('.vsRow').css("font-weight", "bold");
			$('#trafficDesc tbody tr').each(function() {
				$(this).find("td").eq(0).css("text-align", "center");
				$(this).find("td").eq(1).css("text-align", "left");
			});
		}
	},
	registNetworkMapContentEvents : function() 
	{
		with (this) 
		{
/*			
			$('.tab_nav > li > a').click(function(e) 
				{
				e.preventDefault();
				if ($(this).hasClass("networkMap")) 
				{
					monitorLoader.loadContent(0);
				} 
				else if ($(this).hasClass("vsMonitor")) 
				{
					monitorLoader.loadContent(1);
				} 
				else if ($(this).hasClass("systemPerformance")) 
				{
					monitorLoader.loadContent(2);
				}
				 
				else if ($(this).hasClass("performance")) 
				{
					clearTimer();
					monitorLoader.loadContent(3);
				}
				 
				else if ($(this).hasClass("statistics")) 
				{
					monitorLoader.loadContent(3);
				} 
				else if ($(this).hasClass("faultLog")) 
				{
					monitorLoader.loadContent(4);
				}
			});
*/			
			
			/*
			$('.wrap .contain .content .accordion_type1 h2').click(function(event) {
				if($(this).next().css('display') == 'block') {
					$(this).next().hide();	//slideUp(100);
					var src = $(this).nextAll('.open').find('img').attr('src');
					$(this).nextAll('.open').find('img').attr('src', src.replace('up2.png', 'down2.png'));
				} else {
					$(this).next('div').show();//slideDown(300);
					var src = $(this).nextAll('.open').find('img').attr('src');
					$(this).nextAll('.open').find('img').attr('src', src.replace('down2.png', 'up2.png'));
				}
			});
			
			$('.wrap .contain .content .accordion_type1 .open').click(function(event) {
				$(this).prevAll('h2').trigger('click');
			});
			*/
								
			if(this.searchFlag == true)
			{
				if($('.networkMapVsList').size() > 0)
				{
					$('.searchNotMsg_noline').addClass("none");
				}
				else
				{
					$('.searchNotMsg_noline').removeClass("none");
				}
				searchFlag=false;
			}
			else
			{
				$('.searchNotMsg_noline').addClass("none");
			}
			
			if(!$('.searchNotMsg_noline').hasClass("none"))
			{
				
			}
			else
			{
				if (adcSetting.getAdcStatus() != "available")
				{
					$('.searchNotMsg_noline').addClass("none");
					
					if ($('.networkMapVsList').size() > 0)
					{
						
					}
					else
					{
						$('.contents_area .nulldata').removeClass("none");
						if(langCode=="ko_KR")
						{
							$('.contents_area .nulldata').css('background-image', "url(imgs/monitoring/networkMap_null.gif)");
						}
						else
						{
							$('.contents_area .nulldata').css('background-image', "url(imgs/monitoring_eng/networkMap_null.gif)");
						}
						$('.contents_area .successdata').addClass("none");
					}		
				}
			}
			
			$('.control_Board a.searchLnk').click(function(event) 
			{
				event.preventDefault();	
				var searchKey = $('input[name="searchTxt1"]').val();	
				if (!validateSearchKey())
				{
					return false;
				}
				searchFlag=true;
				loadNetworkMapContent(adc, lbClass, searchKey, status);
			});

			$('.control_Board input.searchTxt').keydown(function(event) 
			{
				if (event.which != 13) 
				{
					return;
				}
				if (!validateSearchKey())
				{
					return false;
				}
				searchFlag=true;
				loadNetworkMapContent(adc, lbClass, $(this).val(), status);
			});
			$('.exportCssLnk').click(function(e) 
			{
				e.preventDefault();				
				_checkExportServiceMapDataExist();
//				exportCss(adc);
			});	
			$('#refresh').click(function(event) 
			{
				event.preventDefault();
				loadNetworkMapContent(adc, lbClass, $('.control_Board input.searchTxt').val(), status);
			});
			
			$('.server_cnt').click(function() 
			{
				var status = $(this).find('.status').text();
				searchFlag=true;
				loadNetworkMapContent(adc, lbClass, searchedKey, status);
			});
			
			$('.vsDescEdit').click(function() 
			{
//				var src = $(this).attr('src');
//                var setFlag = false;
//                if (src === 'imgs/btn/btn_config.png') {
//                    src = 'imgs/btn/btn_settingsave_on.png';
//                    //$(this).parents('th').find("#vsDescription").removeAttr("style");
//                    $(this).parents('th').find("#vsDescription").attr("readonly", false);
////                    $(this).parents('th').find("#vsDescription").focus();
//                } else {
//                    src = 'imgs/btn/btn_config.png';
//                    //$(this).parents('th').find("#vsDescription").attr("style", "background-color:transparent;border:0 solid black;text-align:left;");
//                    $(this).parents('th').find("#vsDescription").attr("readonly", true);
//                    setFlag = true;
//                }
//                $(this).attr('src', src);
//                if (setFlag === true) {
                var description = $(this).parents('tr').children('th').find("#vsDescription").val();
                var vsIndex = $(this).parents('tr').children('th').find(".vsIndex").text();
                var vsvcIndex = $(this).parents('tr').children('th').find(".vsvcIndex").text();
//                    var title = $(this).parents('th').children('a').children('span').find(".vsTitile").text();
                var vsTitle = $(this).parents('tr').children('th').find(".vsTitile").text();
            	popUpVsDescription(adc.index, adc.type, vsTitle, vsIndex, vsvcIndex, description);
//                    saveVSDescription(adc.index, adc.type, vsIndex, vsvcIndex, description);
//                }
			});

			$('.Board_networkmap thead tr th.align_left_P5 a.css_textCursor').click(function() 
			{
				vsIndex = $(this).parents('tr th').find(".vsIndex").text();
				port = $(this).parents('th').find(".port").text();
				var lbSelect = $(this).parents('th').find(".lbClass").text();
				FlowitUtil.log("clicked network map node head - vsIndex: %s, port: %s", vsIndex, port);
				loadVsTrafficInfo(vsIndex, lbSelect, port);
			});
			
			$('.rsGroupMoveLnk').click(function()
			{
				$('.groupname_info').removeClass('none');
			});
			
			$('.backupgroup').click(function() 
			{
				var vsname = $(this).find(".vsname").text();
				var port = $(this).find(".port").text();
				var groupBakupType = $(this).find(".groupBakupType").text();
				var groupBackupText = '';
				if(groupBakupType == 2)
				{
					groupBackupText = '[BackUp]Group: ';
				}
				else
				{
					groupBackupText = '[BackUp]GroupReal: ';
				}
				var groupBakupId = $(this).find(".groupBakupId");
				var backupList = $(this).find(".ipaddress");
				var backupStatus = $(this).find(".status");
				popUpBackup(vsname, port, groupBackupText, groupBakupId, backupList, backupStatus, lbClass);
			});
			
			$('.backupreal').click(function() 
			{
				var vsname = $(this).find(".vsname").text();
				var port = $(this).find(".port").text();
				var nodeBackupText = '[BackUp]Real: ';
				var nodeBakupId = $(this).find(".nodebackup");
				var backupList = $(this).find(".ipaddress");
				var backupStatus = $(this).find(".status");
				popUpBackup(vsname, port, nodeBackupText, nodeBakupId, backupList, backupStatus, lbClass);
			});			
			$('.adcAllLnk').click(function(e)
			{
				lbClass = 0;
				e.preventDefault();
				if (!adc) 
				{
					return;
				}
				loadNetworkMapContent(adc, lbClass, searchedKey, status, taskQ);
			});
			$('.adcSlbLnk').click(function(e)
			{
				lbClass = 1;
				e.preventDefault();
				if (!adc) 
				{
					return;
				}
				loadNetworkMapContent(adc, lbClass, searchedKey, status, taskQ);
			});
			$('.adcFlbLnk').click(function(e)
			{
				lbClass = 2;
				e.preventDefault();
				if (!adc) 
				{
					return;
				}
				loadNetworkMapContent(adc, lbClass, searchedKey, status, taskQ);
			});
			
			if(lbClass == 2)
			{
				$('.sort_order').addClass('none');
			}
			else
			{
				$('.sort_order').removeClass('none');
			}
			
//			$('.sortTypeArea a').click(function(e)
			$('.sortTypeArea').change(function(e)
			{
				e.preventDefault();
				with (this)
				{
//					var sortTypeArea = $('.sortTypeArea').filter(':last');
//					sortTypeArea.empty();
//					var html = "";
//					
//	//				if (sortTypeArea.val == 0)
//					if ($('.sortTypeArea').hasClass('on'))
//					{			
//						html +='<a href="#" class="sortType">포트</a>';
//						$('.sortTypeArea').removeClass('on');
//					}
//					else
//					{
//						html +='<a href="#" class="sortType">IP</a>';
//						$('.sortTypeArea').addClass('on');
//					}
//					sortTypeArea.html(html);
					/*
					var isFlag = $(this).attr("isFlag");
					if(isFlag == "false")
					{
						$(this).attr("isFlag", "true");
						$('.sortTypeIP').removeClass('none');
						$('.sortTypePort').addClass('none');
					}
					else
					{
						$(this).attr("isFlag", "false");
						$('.sortTypeIP').addClass('none');
						$('.sortTypePort').removeClass('none');
					}
					
//					orderType = $(this).find('.orderType').text();
//					orderDir = $(this).find('.orderDir').text();
					orderType = $(this).find('a:not(.none) .orderType').text();
//					orderDir = $(this).parent().find('.orderDir').text();
					orderDir = $(this).parent().find('a:not(.none) .orderDir').text();
					
					*/
					
					var sortType = $('#sortTypeArea option:selected').val().trim();
					orderType = sortType.split("_")[0];
					orderDir = sortType.split("_")[1];
					
//					$('.orderType').val(orderType);
//					$('.orderDir').val(orderDir);
					
					loadNetworkMapTableInContent(adc, lbClass, searchedKey, status, taskQ);
				}
			});
			$('#sortAscArea').click(function(e)
			{
				e.preventDefault();
//				var sortAscArea = $('.sortAscArea').filter(':last');
//				sortAscArea.empty();
//				
//				var html = "";
//				html +='<a href="#" class="sortAsc">Descending<span class="bu"></span></a>';
//				sortAscArea.html(html);
				
				var isFlag = $(this).attr("isFlag");
				if(isFlag == "false")
				{
					$(this).attr("isFlag", "true");
					$('.sortAsc').addClass('none');
					$('.sortDesc').removeClass('none');
				}
				else
				{
					$(this).attr("isFlag", "false");
					$('.sortAsc').removeClass('none');
					$('.sortDesc').addClass('none');					
				}
				
//				orderType = $(this).find('.orderType').text();
//				orderDir = $(this).find('.orderDir').text();
//				orderType = $(this).parent().find('.orderType').text();
				orderType = $(this).parent().find('a:not(.none) .orderType').text();
				orderDir = $(this).find('a:not(.none) .orderDir').text();				
				loadNetworkMapTableInContent(adc, lbClass, searchedKey, status, taskQ);
			});
		}
	},
	registVsTrafficContentEvents : function() 
	{
		with (this) 
		{
			$('.memberlnk').click(function(e)
			{
				TapClear();
				$('.memberlnk').css({'background-color' : '#666666','color': 'white'});
				option = "monitor/loadMemberVsTrafficInfo.action";
				loadTableVsTrafficInfo(vsIndex, port, option);
			});
			$('.filterlnk').click(function(e)
			{
				TapClear();
				$('.filterlnk').css({'background-color' : '#666666','color': 'white'});
				option = "monitor/loadFilterVsTrafficInfo.action";
				loadTableVsTrafficInfo(vsIndex, port, option);
			});
		}
	},
	validateSearchKey : function()
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
			
//			if (!getValidateStringint($('.control_Board input.searchTxt').val(), 1, 64))
//			{
//				alert(VAR_FAULTSETTING_SPECIALCHAR);
//				$('.control_Board input.searchTxt').val('');
//				return false;
//			}
		}
		
		return true;
	},
	TapClear : function()
	{
		with(this)
		{
			$('.memberlnk').css({'background-color': 'white', 'color': 'black'});
			$('.filterlnk').css({'background-color': 'white', 'color': 'black'});	
		}
	},
	adjustVirtualServiceInfoBox : function()			//사용되지 않았던 부분 
	{
		//nm_monitoring renewal 작업 이후 사용하지 않음.
		$('.nm_monitoring ul li').css("margin-bottom", "0px");
		
		var top = 0;
		var maxHeight = 0;
		$('.nm_monitoring ul li').each(function() 
		{
			if (top === $(this).position().top) 
			{
				if (maxHeight < $(this).outerHeight(true)) 
				{
					maxHeight = $(this).outerHeight(true);
				}
			} 
			else 
			{
				var $prevObject = $(this).prev();
				if ($prevObject.length) 
				{
					if (maxHeight > $prevObject.outerHeight(true)) 
					{
						var margin = maxHeight - $prevObject.outerHeight(true);
						$prevObject.css("margin-bottom", margin + "px");
					}
				}
				
				top = $(this).position().top;
				maxHeight = $(this).outerHeight(true);
			}
		});
	},
	_checkExportServiceMapDataExist : function()
	{
		with(this)
		{
			if(!adc)
			{
				return;
			}
			var params = 
			{
				"adc.index"	: adc.index,
				"adc.type"	: adc.type,
				"adc.name"	: adc.name,
				"lbType"	: lbClass,
				"adc.isFlb" : adcSetting.getIsFlb(),
				"orderOption.orderType" : orderType,
				"orderOption.orderDirection" : orderDir
			};
			
			ajaxManager.runJsonExt
			({
				url : "monitor/checkExportServiceMapExist.action",
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
					$.obAlertAjaxError(VAR_SVCPERFOM_EXPEXISTINSPECT, jqXhr);
				}
			});
		}
	},
//	exportCss : function(adc)
	exportCss : function()
	{
		with (this) 
		{
			var params = "adc.index=" + adc.index + "&lbType=" + lbClass + "&searchKey=" + encodeURIComponent($('.control_Board input.searchTxt').val());
			var url = "monitor/downloadServiceMap.action?" + params;
			$('#downloadFrame').attr('src',url);	
		}
	}
});