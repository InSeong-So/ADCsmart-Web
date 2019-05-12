var AdcSetting = Class.create({
	initialize : function() 
	{
		this.isInitialized = false;
		this.configCheck = new ConfigCheck();
		this.adc = 
		{
			index : undefined,
			name : undefined,
			type : undefined,
			mode : undefined
		};
		this.activeContent = undefined;
		this.objOnAdcChange = undefined;
		this.$unassignedAccountOptions = $();
		this.searchPurchaseDate = undefined;
//		this.initials = new Initials();
		this.selectedType = 'all';
		this.orderDir  = 1; //1는 오름차순
		this.orderType = 34;//34는 ADC이름
		this.status = 1;
		this.isFlb = undefined; //0 dis, 1 ena
		this.selectedAdcIndex = undefined;
		this.openGroupNames = undefined;
		this.isReachable = undefined;
		this.searchFlag = false;
	},
	isAdcSet : function() 
	{
		return this.adc.index !== undefined;
	},
	getAdc : function() 
	{
		return this.adc;
	},
	setAdc : function(adc) 
	{
		this.adc = adc;
	},
	getGroupIndex : function() 
	{
		return this.groupindex;
	},
	setGroupIndex : function(groupindex) 
	{
		this.groupindex = groupindex;
	},
	getGroupName : function() 
	{
		return this.groupname;
	},
	setGroupName : function(groupname) 
	{
		this.groupname = groupname;
	},
	getAdcVersion : function() 
	{
		return this.adcversion;
	},
	setAdcVersion : function(adcversion) 
	{
		this.adcversion = adcversion;
	},
	getAdcModel : function()
	{
		return this.adcModel;
	},
	setAdcModel : function(adcModel)
	{
		this.adcModel = adcModel;
	},
	getSelectIndex : function() 
	{
		return this.selectindex;
	},
	setSelectIndex : function(selectindex) 
	{
		this.selectindex = selectindex;
	},
	getAdcStatus : function()
	{
		return this.adcStatus;
	},
	setAdcStatus : function(adcStatus)
	{
		this.adcStatus = adcStatus;
	},
	getActiveContent : function() 
	{
		return this.activeContent;
	},
	setActiveContent : function(content) 
	{
		this.activeContent = content;
	},
	isActiveContent : function(content) 
	{
		return this.activeContent == content;
	},
	getIsFlb : function()
	{
		return this.isFlb;
	},
	setIsFlb : function(isFlb)
	{
		this.isFlb = isFlb;
	},
	setObjOnAdcChange : function(obj) 
	{
		this.objOnAdcChange = obj;
	},
	onAdcChange : function() 
	{
		FlowitUtil.log('adcSetting.onAdcChange()');
		adcSetting.loadAdcModifyContent(adcSetting.adc.index, adcSetting.adc.type);
	},
	_saveOpengroupNSelectedADCIndex : function()
	{
		with(this)
		{
			this.selectedAdcIndex = _getSelectedAdc().find('.adcIndex').text();
			FlowitUtil.log("selected adc index: ", selectedAdcIndex);
			this.openGroupNames = $('.snb .snb_tree .depth2').filter(function() 
			{
				return $(this).css('display') != 'none';
			}).map(function() {
				var groupWithAdcCount = $.trim($(this).prev().children('span').first().text());
				return groupWithAdcCount.substring(0, groupWithAdcCount.indexOf('('));
			});
		}		
	},
	loadContent : function(searchKey) 
	{
		with (this) 
		{
//			$('.listcontainer_open').parent().removeClass('none');
			$('.listcontainer_open').click();
			header.loadAdcSearchBar();
			_saveOpengroupNSelectedADCIndex();
			ajaxManager.runHtmlExt({
				url : "adcSetting/loadLeftPane.action",
				data : 
				{
					"searchKey" : searchKey,
					"orderType" : this.orderType,
					"orderDir" : this.orderDir
				},
				target: "#wrap .listcontainer",
				successFn : function(params) 
				{
					applyLeftCss();
					registerLeftEvents();
					
					_closeAllGroups();
					_openGroupsWithNames(openGroupNames);
					if (selectedAdcIndex)
					{
						_selectAdc(selectedAdcIndex);
					}
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_COMMON_ADCLISTLOAD, jqXhr);
				}	
			});
			
			$('.snb').removeClass('none');
			$('.snb_system').addClass('none');
			$('.snb_systools').addClass('none');
			
			$('.listcontainer').css('background-color', '#2d476d');			
			$('.pick').removeClass('none');
			$('.pick_system').addClass('none');			
			$('.pick_systools').addClass('none');
			$('.adc_search').removeClass('none');
			$('.adc_search_1').addClass('none');
		}
	},
	
	loadLeftPane : function(searchKey, taskQ, forcesUpdate, orderType, orderDir) 
	{
		with (this) 
		{
			if (!isInitialized || forcesUpdate) 
			{
				ajaxManager.runHtmlExt({
					url : "adcSetting/loadLeftPane.action",
					data : 
					{
						"searchKey" : searchKey,
						"orderType" : orderType,
						"orderDir" : orderDir,
						"adcListPageOption" : 1
					},
					target: "#wrap .listcontainer",
					successFn : function(params) 
					{
						applyLeftCss();
						registerLeftEvents();
						if (!isInitialized) 
						{
							isInitialized = true;
//							_openAllGroups();
//							_selectFirstAdc();
							
//							_selectAllGroups();
//							initials.loadContent();
//							header.loadContent();
						} 
						else if (forcesUpdate) 
						{
//							_openAllGroups();
							_selectAdc(adc.index);	
							
						}
					},
					errorFn : function(jqXhr)
					{
						$.obAlertAjaxError(VAR_COMMON_ADCLISTLOAD, jqXhr);
					}	
				}, taskQ);
			}
			
			$('.snb').removeClass('none');
			$('.snb_system').addClass('none');
			$('.snb_systools').addClass('none');
			
			$('.listcontainer').css('background-color', '#2d476d');			
			$('.pick').removeClass('none');
			$('.pick_system').addClass('none');			
			$('.pick_systools').addClass('none');
			$('.adc_search').removeClass('none');
			$('.adc_search_1').addClass('none');
		}
	},
	
	_drawSelectedAllBlock : function() 
	{
//		alert("1-1");
		var $allBlock = $('.snb_tree .allBlock');
		if ($allBlock.hasClass('on'))
			return;
			
		$allBlock.addClass('on');
	},	
	_getSelectedAdcBlock : function() 
	{
//		alert("2-1");
		var $adcBlock = $('.snb_tree .adcBlock');
		for (var i=0; i < $adcBlock.length; i++) 
		{
			var index = $adcBlock.eq(i).find('.adcIndex').text();
			if (index == this.adc.index)
				return $adcBlock.eq(i);
		}
		
		return null;
	},
	_clearSelectionOnAllNodes : function() 
	{
//		alert("adc click2");
		var $allSelectableNodes =  $('.snb_tree li p');
		$allSelectableNodes.removeClass('on');
	},
	
	applyLeftCss : function() 
	{
		with (this)
		{
//			alert(selectedType);
			if (selectedType == 'all') 
			{
//				alert("1");
				_drawSelectedAllBlock();
			} 
			else if (selectedType == 'adc') 
			{
//				alert("2");
				var $adcBlock = _getSelectedAdcBlock();
				if (!$adcBlock) 
				{
					selectedType = 'all';
					_drawSelectedAllBlock();
				} 
				else 
				{
					$adcBlock.addClass('on');
					var $groupMenu = $adcBlock.parents('.depth2');
					$groupMenu.css('display', 'block');
				}
			}
			// SNB 높이 설정
//			snbAutoHeight();
			
//			$('.snb .snb_tree .depth1 p').click(function()
//			{
//				$('.snb .snb_tree p').each(function()
//				{
//					$(this).removeClass('on');
//				});
//				
//				$(this).addClass('on');
//			});
			
			// 개별 ADC 클릭시 발생하는 클릭 이벤트 함수
			$('.snb .snb_tree .depth2 p').click(function()
			{
				$('.snb .snb_tree .depth1 p').each(function()
				{
					$(this).removeClass('on');
				});
								
				$('.snb_tree > li > ul').find('.allBlock').removeClass('on');
				$(this).addClass('on');
			});
			
			// Group 영역 클릭 이벤트
			$(".group_click_area").click(function(e)					
			{
				e.preventDefault();	
				
				// 그룹 선택 표현
				$(".groupNameArea").parent().parent().removeClass('on');
				$(this).parent().addClass('on');
				$('.snb_tree > li > ul').find('.allBlock').removeClass('on');
				
				_setGroupToClickedOne($(this));
				// 장애진단에 필요한 SelectIndex를 저장한다.
				adcSetting.setSelectIndex(1);	
								
				// adc 선택 해제
				var adc = {};
				adc.index = undefined;
				adc.name = undefined;
				adc.type = undefined;						
				adcSetting.setAdc(adc);				
				$('.adcBlock').removeClass('on');
								
//				$('.slbSettingSubSub').removeClass('none');
												
				if (objOnAdcChange == adcSetting)
				{
					//_setGroupToClickedOne($(this));
					try 
					{
//						if(!adcSetting.isAdcSet())
//						{
//							$('.adcSettingSubSub').removeClass('none');
//							$(".adcSettingSubSub").find('a').removeClass("select");
//						}
						adcSetting.loadAdcListContent();
					} 
					catch (e) 
					{						
					}
				}
				// 모니터링 ADC 선택시 
				else if (objOnAdcChange == monitorAppliance)
				{
					try
					{	
						adcMonitor.loadListContent();						
					}
					catch (e) 
					{
					}
				}
				// 모니터링 서비스 선택시 
				else if (objOnAdcChange == monitorServicePerfomance)
				{
					try
					{	
						serviceMonitor.loadListContent();						
					}
					catch (e) 
					{
					}
				}
				// 모니터링 Group 선택시
				else if(objOnAdcChange == groupMonitor)
				{
					try 
					{
						groupMonitor.loadListContent();
					} 
					catch (e) 
					{					
					}
				}
				// 모니터링 RealServer 선택시
				else if(objOnAdcChange == realServerMonitor)
				{
					try 
					{
						realServerMonitor.loadListContent();
					} 
					catch (e) 
					{					
					}
				}
				
				else if (objOnAdcChange == faultSetting)
				{
					//_setGroupToClickedOne($(this));
					try
					{
						faultSetting.loadFaultSettingContent();
					}
					catch (e)
					{						
					}
				}	// 그룹 진단설정 진입점
				else if (objOnAdcChange == faultHistory)
				{
					//_setGroupToClickedOne($(this));
					try
					{
						faultHistory.loadListContent();
					}
					catch (e)
					{						
					}
				}	
				else if (objOnAdcChange == faultAnalysis)
				{
					//_setGroupToClickedOne($(this));
					try 
					{
						faultAnalysis.loadListContent();
					} 
					catch (e) 
					{
					}
				}
				
				// GS 14.08.13 sw.jung: 그룹 선택시 초기화면 로드
				// 설정 > SLB설정
//				else if (objOnAdcChange == virtualServer)
//				{
//					header.loadContent();
//				}
				else if (objOnAdcChange == virtualServer)
				{
					//_setGroupToClickedOne($(this));
					try
					{
						$('.slbSettingSubSub').removeClass('none');
						$('.slbProfile').addClass('none');
						$('.slbNotice').addClass('none');
						
						if(header.getVsSettingTap() != 2)
						{
							$(".slbSettingSubSub").find('.slbVs a').attr("class", "choice");
							virtualServer.loadListContent();
						}
						else
						{							
							$(".slbSettingSubSub").find('.slbRs a').attr("class", "choice");						
							groupNode.loadListContent();
						}						
					}
					catch (e)
					{						
					}
				}	
				else if (objOnAdcChange == node)
				{
					//_setGroupToClickedOne($(this));
					try
					{
						$('.slbSettingSubSub').removeClass('none');
						$('.slbProfile').addClass('none');
						$('.slbNotice').addClass('none');
						$(".slbSettingSubSub").find('.slbRs a').attr("class", "choice");
						
						groupNode.loadListContent();
					}
					catch (e)
					{						
					}
				}	
				// 설정 > 설정이력
				else if (objOnAdcChange == adcHistory)
				{
					header.loadContent();
				}

				// 설정 > 경보설정
				else if (objOnAdcChange == adcAlert)
				{
					//_setGroupToClickedOne($(this));
					try 
					{
						groupAlert.loadGroupAlertListContent();
					} 
					catch (e) 
					{
					}
				}
				// 모니터링 > 장애
				else if (objOnAdcChange == faultMonitoring)
				{
					//_setGroupToClickedOne($(this));
					try
					{
						faultMonitoring.loadListContent();						
					} 
					catch (e) 
					{
					}
				}
				// 모니터링 > 경보
				else if (objOnAdcChange == alertMonitoring)
				{
					//_setGroupToClickedOne($(this));
					try
					{
						alertMonitoring.loadListContent();						
					} 
					catch (e) 
					{
					}
				}
				// 모니터링 > ADC 로그
				else if (objOnAdcChange == adcLog)
				{
					//_setGroupToClickedOne($(this));
					try
					{	
						adcLog.loadListContent();						
					}
					catch (e) 
					{
					}
				}	//그룹이 없는 경우 기본페이지
				else 
				{
					header.loadContent();
				}
				// 보고서
				if (objOnAdcChange == report)
				{
					try
					{
						report.loadListContent();
					}
					catch (e)
					{
					}
				}
			});
			
			// SNB Tree 클릭 이벤트  
			$('.snb .snb_tree .depth1 > li > p a').click(function(e) 
			{
				e.preventDefault();
	//			e.stopImmediatePropagation();
				var $sub_menu = $(this).parent().nextAll('ul');		
				
				if($sub_menu.css('display') == 'none') 
				{
					over($(this));
					$sub_menu.slideDown(200);
				} 
				else 
				{
					out($(this));
					$sub_menu.slideUp(200);
				}
			});
			
			// 각 Group 우측 화살표 눌렀을 시 펼쳐지는 클릭 이벤트 함수
			$(".GroupButton").click(function(e) 
			{
				e.preventDefault();
//				var $sub_menu = $(this).parent().parent().nextAll('ul');
//				var $sub_menu = $(this).parent().parent();
				var sub_menu_check = $(this).find('img').attr('src');
				if(sub_menu_check.match('_minus.png'))
				{
//					$sub_menu.slideUp(200);
					$(this).find('img').attr('src', 'imgs/layout/ico_plus.png');
				}
				else
				{
//					$sub_menu.slideDown(200);
					$(this).find('img').attr('src', 'imgs/layout/ico_minus.png');
				}						
			});
			
	//		$('.snb .snb_tree .depth2 > li > p').click(function(e) {
	//			$('.snb .snb_tree .depth2 > li').each(function() {
	//				$(this).children('p').removeClass('on');
	//			});
	//		});
			
			// SNB 메뉴 숨기기/보이기
			$('.snb .snb_close').click(function(e) {
				$('.listcontainer').animate({ 'left':'-230px' }, "fast");
				$('.contents').animate({ 'left':'0px' }, "fast");
				$('.snb_close').parent().addClass('none');
				$('.listcontainer_open').parent().removeClass('none');
				$('.LocationNavi').animate({ 'padding-left':'20px' }, "fast");
			});
			
			$('.listcontainer_open').click(function(e) {
				$('.listcontainer').animate({ 'left':'0px' }, "fast");
				$('.contents').animate({ 'left':'232px' }, "fast");
				$('.snb_close').parent().removeClass('none');
				$('.listcontainer_open').parent().addClass('none');
				$('.LocationNavi').animate({ 'padding-left':'252px' }, "fast");
			});
			
			/*
			$('.snb .snb_close').toggle(function(e) 
			{
				$('.listcontainer').animate({ 'left':'-230px' }, 100, function() 
				{
//					$('.snb .snb_close').attr('class', 'snb_open').children('img').attr({ 'src':'imgs/meun/btn_mov_rgt.png', 'alt':VAR_COMMON_MENUOPEN });
//					$('.listcontainer').css('margin-left', '10px');
					$('.listcontainer_open').parent().removeClass('none');
				});
			}, function() {
				$('.listcontainer .adc_search').animate({ 'left':'-230px' }, 100, function()
						{
//							$('.snb_open').attr('class', 'snb_close').children('img').attr({ 'src':'imgs/meun/btn_mov_lft.png', 'alt':VAR_COMMON_MENUCLOSE });
//							$('.listcontainer').css('margin-left', '215px');
							$('.listcontainer_open').parent().addClass('none');
						});					
			}, function() {
				$('.contents').animate({ 'left':'0px' }, 100, function()
				{
//					$('.snb_open').attr('class', 'snb_close').children('img').attr({ 'src':'imgs/meun/btn_mov_lft.png', 'alt':VAR_COMMON_MENUCLOSE });
//					$('.listcontainer').css('margin-left', '215px');
					$('.listcontainer_open').parent().addClass('none');
				});
			});
		
			$('.listcontainer_open').toggle(function(e) 
			{
				$('.listcontainer').animate({ 'left':'0px' }, 100, function() 
				{
//					$('.listcontainer_open').attr('class', 'snb_close').children('img').attr({ 'src':'imgs/meun/btn_mov_lft.png', 'alt':VAR_COMMON_MENUCLOSE });
//							$('.listcontainer').css('margin-left', '10px');
					$('.listcontainer_open').parent().addClass('none');
				});
			}, function() {
				$('.contents').animate({ 'left':'232px' }, 100, function()
				{
//					$('.snb .snb_close').attr('class', 'listcontainer_open').children('img').attr({ 'src':'imgs/meun/btn_mov_rgt.png', 'alt':VAR_COMMON_MENUOPEN });
//							$('.listcontainer').css('margin-left', '215px');
					$('.listcontainer_open').parent().removeClass('none');
				});
			});
			*/
			$('.snb .snb_tree .depth2 .adcName').parent().width(165);		// 183px is experimental #.
			$(window).trigger('resize');
		}
	},
	registerLeftEvents : function() 
	{
		with (this) 
		{
			$('.snb .snb_tree .adcListContentLnk').click(function(e) 
			{
				e.preventDefault();
				$(this).addClass('on');
				$(".groupNameArea").parent().parent().removeClass('on');
				$('.snb_tree > li .depth2').find('.adcBlock').removeClass('on');
				
				if (!objOnAdcChange)
				{
					setObjOnAdcChange(adcSetting);
				}
				// 장애진단에 필요한 SelectIndex를 저장한다.
				adcSetting.setSelectIndex(0);
				var adc = {};
				adc.index = undefined;
				adc.name = undefined;
				adc.type = undefined;						
				adcSetting.setAdc(adc);
				
//				$('.slbSettingSubSub').removeClass('none');
				
				// ADC관리일때 전체 버튼 이벤트 (GroupIndex를 0으로 세팅하여 전체 목록이 나온다)
				if (objOnAdcChange == adcSetting)
				{
					adcSetting.setGroupIndex(0);
					loadAdcListContent();					
				}
				// 장애진단일때 전체 버튼 이벤트
				if (objOnAdcChange == faultSetting)
				{
					adcSetting.setGroupIndex(0);
					faultSetting.loadFaultSettingContent();
				}			
				// 장애이력일때 전체 버튼 이벤트
				if (objOnAdcChange == faultHistory)
				{
					adcSetting.setGroupIndex(0);
					faultHistory.loadListContent();
				}	
				// 장애진단일때 전체 버튼 이벤트
				if (objOnAdcChange == faultAnalysis)
				{
					adcSetting.setGroupIndex(0);
					faultAnalysis.loadListContent();
				}
				
				// GS 14.08.13 sw.jung: 그룹 선택시 초기화면 로드
				// 설정 > SLB설정
//				if (objOnAdcChange == virtualServer)
//				{
//					adcSetting.setGroupIndex(0);
//					header.loadContent();
//				}
				
				if (objOnAdcChange == virtualServer)
				{
					$('.slbSettingSubSub').removeClass('none');
					$('.slbProfile').addClass('none');
					$('.slbNotice').addClass('none');
					
					adcSetting.setGroupIndex(0);
					if(header.getVsSettingTap() != 2)
					{
						$(".slbSettingSubSub").find('.slbVs a').attr("class", "choice");
						virtualServer.loadListContent();
					}
					else
					{							
						$(".slbSettingSubSub").find('.slbRs a').attr("class", "choice");
						groupNode.loadListContent();
					}		
					
//					$(".slbSettingSubSub").find('.slbVs a').attr("class", "choice");
				
//					adcSetting.setGroupIndex(0);
//					header.loadContent();
//					virtualServer.loadListContent();
				}
				
				if (objOnAdcChange == node)
				{
					$('.slbSettingSubSub').removeClass('none');
					$('.slbProfile').addClass('none');
					$('.slbNotice').addClass('none');
					$(".slbSettingSubSub").find('.slbRs a').attr("class", "choice");
				
					adcSetting.setGroupIndex(0);
//					header.loadContent();
					groupNode.loadListContent();
				}
				
				// 설정 > 설정이력
				if (objOnAdcChange == adcHistory)
				{
					adcSetting.setGroupIndex(0);
					header.loadContent();
				}
				// 경보설정일때 전체 버튼 이벤트
				if (objOnAdcChange == adcAlert)
				{
					adcSetting.setGroupIndex(0);
					groupAlert.loadGroupAlertListContent();
				}
				// 모니터링>장애일때 전체 버튼 이벤트
				if (objOnAdcChange == faultMonitoring)
				{
					adcSetting.setGroupIndex(0);
					faultMonitoring.loadListContent();
				}
				// 모니터링>경보일때 전체 버튼 이벤트
				if (objOnAdcChange == alertMonitoring)
				{
					adcSetting.setGroupIndex(0);
					alertMonitoring.loadListContent();
				}
				// 모니터링>ADC로그일때 전체 버튼 이벤트
				if (objOnAdcChange == adcLog)
				{
					adcSetting.setGroupIndex(0);
					adcLog.loadListContent();
				}
				// 모니터링 ADC 전체 모니터링 전체 버튼 이벤트
				if (objOnAdcChange == monitorAppliance)
				{
					adcSetting.setGroupIndex(0);
					adcMonitor.loadListContent();
				}
				// 모니터링 서비스 전체 모니터링 전체 버튼 이벤트
				if (objOnAdcChange == monitorServicePerfomance)
				{
					adcSetting.setGroupIndex(0);
					serviceMonitor.loadListContent();
				}
				// 모니터링 Group 전체 모니터링 전체 버튼 이벤트
				if (objOnAdcChange == groupMonitor)
				{
					adcSetting.setGroupIndex(0);
					groupMonitor.loadListContent();
				}
//				// 모니터링 RealServer 전체 모니터링 전체 버튼 이벤트
				if(objOnAdcChange == realServerMonitor)
				{
					adcSetting.setGroupIndex(0);
					realServerMonitor.loadListContent();
				}
				// 보고서 전체 버튼 이벤트
				if (objOnAdcChange == report)
				{
					adcSetting.setGroupIndex(0);
					report.loadListContent();
				}
				var AllGroupName = VAR_COMMON_ALLADC;
				header.retrievePickView(AllGroupName, "", 0);
			});
			
			$('.snb .snb_tree .initialLnk').click(function(e) 
			{
				e.preventDefault();
//				initials.loadContent();
//				alert("a12");
				header.loadContent();
				selectedType = 'all';
				$(this).removeClass("on");
				var $allBlock = $('.snb_tree .allBlock');
					
				$allBlock.addClass('on');
				_deselectAll();
				_closeAllGroups();
				adc.index = undefined;
				adc.name = undefined;
				adc.type = undefined;
				adc.mode = undefined;
			});
			
			$('.snb_tree .allBlock').click(function(e) 
			{
				e.preventDefault();
				selectedType = 'all';	
//				onAdcChange();				
			});
			// ListContaire 에서 ADC를  눌렀을때 이벤트 함수
			$('.snb_tree .adcBlock').click(function(e) 
			{
				FlowitUtil.log('adcBlock');
				// 장애진단에 필요한 SelectIndex를 저장한다.
				adcSetting.setSelectIndex(2);
				adcSetting.setGroupIndex(undefined);
				selectedType = 'adc';
				adc.index = $(this).find('.adcIndex').text();
				adc.name = $(this).find('.adcName').text();
				adc.type = $(this).find('.adcType').text();
				adc.mode = $(this).find('.opMode').text();
				groupindex = undefined;
				adcversion = $(this).find('.adcVersion').text();
				adcModel = $(this).find('.adcModel').text();
				adcStatus = $(this).find('.adcStatus').text();
				isFlb = $(this).find('.isFlb').text();
				setGroupIndex(groupindex);
				setAdcVersion(adcversion);
				setAdcModel(adcModel);
				setAdcStatus(adcStatus);
				setIsFlb(isFlb);
								
//				$('.menu3 .abcSettingSubSub').removeClass('none');
//				$('.menu3 .slbSettingSubSub').addClass('none');
				if (header.activeMenu === 'SlbSetting')
				{
					$('.slbSettingSubSub').removeClass('none');
					if(adcSetting.getAdc().type == "F5")
					{						
						$('.slbProfile').removeClass('none');
						$('.slbRs').removeClass('none');
						$('.slbNotice').removeClass('none');
					}
					else if (adcSetting.getAdc().type == "Alteon")
					{
						$('.slbProfile').addClass('none');
						$('.slbRs').removeClass('none');
						$('.slbNotice').removeClass('none');
					}
					else
					{
//						$('.slbSettingSubSub').addClass('none');
						$('.slbProfile').addClass('none');
						$('.slbRs').addClass('none');
						$('.slbNotice').addClass('none');
					}
				}	
				
				if (header.activeMenu === 'AdcSetting')
				{
					if(adcSetting.isAdcSet())
					{
						$('.adcSettingSubSub').removeClass('none');
						
						if(adcSetting.getAdc().type == "F5" || adcSetting.getAdc().type == "Alteon")
						{
							$(".adcConnect").removeClass('none');
						}
						else
						{
							$(".adcConnect").addClass('none');
						}
						
						if(header.getAdcSettingTap() == 0)
						{
							$(".adcSettingSubSub").find('a').removeClass("select");
							$(".adcSettingSubSub").find('a').removeClass("choice");
							$(".adcSettingSubSub").find('a:first').attr("class", "choice");
						}
						
						else if(header.getAdcSettingTap() == 1)
						{
							$(".adcSettingSubSub").find('a').removeClass("select");
							$(".adcSettingSubSub").find('a').removeClass("choice");					
							$(".adcSettingSubSub").find('.adcConnect a').attr("class", "choice");
						}						
					}
					else
					{
						$('.adcSettingSubSub').addClass('none');
					}
				}	
				
	//			onAdcChange();
			});
			// the entire adcs node
			$('.snb_tree > li > p').click(function(e) 
			{
				if($(this).hasClass('on'))
					return;
				
				_clearSelectionOnAllNodes();
				$(this).addClass('on');
				_closeAllGroups();
			});
			// adc node
			$('.snb_tree .depth2 > li > p').click(function(e) 
			{
				if($(this).hasClass('on'))				
					return;
				
				//alert("adc click");
				_clearSelectionOnAllNodes();
				$(this).addClass('on');
			});
	
			
			$('.snb .snb_tree .depth2 > li > p').click(function(e) 
			{
				_setAdcToClickedOne($(this));
//				try 
//				{
//					objOnAdcChange.onAdcChange();
//				} 
//				catch (e) 
//				{					
//				}
				
				// 모니터링 Group 선택시
				if(objOnAdcChange == groupMonitor)
				{
					try 
					{
						adcSetting.setGroupIndex(undefined);
						groupMonitor.loadListContent();
					} 
					catch (e) 
					{					
					}
				}
				// 모니터링 RealServer 선택시
				else if(objOnAdcChange == realServerMonitor)
				{
					try 
					{
						adcSetting.setGroupIndex(undefined);
						realServerMonitor.loadListContent();
					} 
					catch (e) 
					{					
					}
				}
				else
				{
					try
					{
						objOnAdcChange.onAdcChange();
					} 
					catch (e) 
					{					
					}
				}
			});
			
			// search event
//			$('.snb p.sch a.searchLnk').click(function (e) 
//			{
//				e.preventDefault();
//				var searchKey = $(this).siblings('input.searchTxt').val();
//				FlowitUtil.log('click:' + searchKey);
//				loadLeftPane(searchKey, undefined, true);
//			});
//			
//			$('.snb p.sch input.searchTxt').keydown(function(e) 
//			{		
//				if (e.which != 13)				
//					return;
//						
//				var searchKey = $(this).val();
//				FlowitUtil.log('click:' + searchKey);
//				loadLeftPane(searchKey, undefined, true);
//			});	

			$('.adc_search #adcSearchBtn').click(function (e) 
			{
				e.preventDefault();
//				var searchKey = $(this).siblings('.adc_search .inputTextposition input.searchTxt').val();
				var searchTxt = $('#adcSearchTxt');
				
				FlowitUtil.log('click:' + searchTxt.val());
				
				if (searchTxt.validate({
					name: 'ADC이름',
					type: 'name',
					lengthRange: [1,65]
				}))
					loadLeftPane(searchTxt.val(), undefined, true);
				else
					return;
			});
			
			$('#adcSearchTxt').keydown(function(e) 
			{				
				if (e.which != 13)				
					return;
						
				var searchTxt = $(this); 
				
				FlowitUtil.log('click:' + searchTxt.val());
				
				if (searchTxt.validate({
					name: 'ADC이름',
					type: 'name',
					lengthRange: [1,65]
				}))
					loadLeftPane(searchTxt.val(), undefined, true);
				else
					return;
			});	
		}
	},	
	refresh : function(isGroupRefresh) 
	{
		with (this) 
		{
			var selectedAdcIndex = _getSelectedAdc().find('.adcIndex').text();
			FlowitUtil.log("selected adc index: ", selectedAdcIndex);
			var openGroupNames = $('.snb .snb_tree .depth2').filter(function() 
			{
				return $(this).css('display') != 'none';
			}).map(function() {
				var groupWithAdcCount = $.trim($(this).prev().children('span').first().text());
				return groupWithAdcCount.substring(0, groupWithAdcCount.indexOf('('));
			});
			FlowitUtil.log("open group names: ", openGroupNames);
			taskQ.add(function() 
			{
				loadLeftPane(undefined, taskQ, true);
			});
			taskQ.add(function() 
			{
				_closeAllGroups();
				_openGroupsWithNames(openGroupNames, isGroupRefresh);
				if (selectedAdcIndex && !isGroupRefresh)
				{
					_selectAdc(selectedAdcIndex);
				
				}
				else
				{
//					_selectFirstAdc();
//					_selectAllGroups();
				}
				
				taskQ.notifyTaskDone();
			});
			taskQ.start();
		}
	},
	_getSelectedAdc : function() 
	{
		return $('.snb .snb_tree .depth2 > li > p.on');
	},
	_selectFirstAdc : function() 
	{
//		$('.snb_tree .depth2 > li').filter(':first').find('p').click();
		var $selectItem = $('.snb .snb_tree .depth2 > li').first().find('p');
		if($selectItem.attr('class') != 'on') 
		{
			$selectItem.addClass('on');
			this._setAdcToClickedOne($selectItem);
			this._openGroup($selectItem);
		}
	},
	_selectAdc : function(adcIndex) 
	{
		adcSetting.setSelectIndex(2);
		adcSetting.setGroupIndex(undefined);
		selectedType = 'adc';
		var $selectItem = $('.snb .snb_tree .depth2 .adcIndex').filter(function() {
			return $(this).text() === adcIndex;
		}).first().parent();
		
		if ($selectItem.length === 0)
			return;
		
		if(!$selectItem.hasClass('on')) 
		{
			this._deselectAll();
			$selectItem.addClass('on');
			this._setAdcToClickedOne($selectItem);
			this._openGroup($selectItem);
		}
	},
	_openGroup : function($adc) 
	{
		var $group = $adc.parents('.depth2').prev();
		if ($group.next().css('display') === 'none')
			$group.children('a').click();
	},
	_selectAllGroups : function()
	{
		var $selectGroup = $('.snb .snb_tree > li > p').first().find('a');
		//alert($selectGroup + " :: " + $selectGroup.text());
		if($selectGroup.attr('class') != 'on') 
		{
			//alert("1234");
			$selectGroup.addClass('on');
			this._setAdcToClickedOne($selectGroup);
//			this._openGroup($selectGroup);
		}
	},

		
	_openAllGroups : function() 
	{
//		$('.snb .snb_tree .depth1 > li').find('p a').click();
		$('.snb .snb_tree .depth2').css('display', 'block');
	},
	_openGroupsWithNames : function(openGroupNames, isGroupRefresh) 
	{
		var $openGroups = $('.snb .snb_tree .depth1 > li > p > span > span').filter(function() {
			var groupName = $(this).text().substring(0, $(this).text().indexOf('('));
			for (var i=0; i < openGroupNames.length; i++) 
			{
				if (openGroupNames[i] === groupName)
					return true;
			}
				
			return false;
		});
		
		if (isGroupRefresh)
		{
			if ($openGroups.length > 0)
			{
				$openGroups.parent().next().css('display', 'block');
				$($openGroups).click();
			}
			else
				$('.adcListContentLnk').click();
		}
		else
		{
			$openGroups.parent().parent().next().css('display', 'block');
		}
	},
	_closeAllGroups : function() 
	{
		$('.snb .snb_tree .depth2').css('display', 'none');
	},
	
	// ListContaier 에서 GROUP을 선택할 시 ADC의 정보를 담는 함수
	_setGroupToClickedOne : function($p)
	{
		if ($p.length == 0)
			return;
		var groupindex = "";
		var groupname = "";
		groupindex = $p.parent().find(".GroupIndex").text();
		groupname = $p.parent().find(".GroupName").text();
		this.setGroupIndex(groupindex);
		this.setGroupName(groupname);
		header.retrievePickView(groupname, "", 1);
	},	
	// ListContaier 에서 ADC를 선택할 시 ADC의 정보를 담는 함수
	_setAdcToClickedOne : function($p) 
	{
		if ($p.length == 0)
			return ;
		
//		var groupindex = "";
		var groupname = "";
//		groupindex = $p.parent().parent().parent().find(".GroupIndex").text();
		groupname = $p.parent().parent().parent().find(".GroupName").text();
//		this.setGroupIndex(groupindex);
		this.setGroupName(groupname);
			
		var adc = {};
		adc.index = $p.find(".adcIndex").text();
		adc.name = $p.find(".adcName").text();
		adc.type = $p.find(".adcType").text();
		adc.mode = $p.find(".opMode").text();
		
		adcStatus = $p.find(".adcStatus").text();
		adcversion = $p.find(".adcVersion").text();
		adcModel = $p.find(".adcModel").text();
		isFlb = $p.find('.isFlb').text();
		
		this.setAdc(adc);
		this.setAdcVersion(adcversion);
		this.setAdcModel(adcModel);
		this.setAdcStatus(adcStatus);
		this.setIsFlb(isFlb);
		header.retrievePickView(groupname, adc, 2);
//		FlowitUtil.log(Object.toJSON(this.getAdc()));
	},
	_deselectAll : function() 
	{
		$('.snb .snb_tree p').removeClass('on');
	},
	_deselectAllGroups : function () 
	{
		$('.snb .depth1 > li > p').removeClass('on');
	},
	_deselectAllAdcs : function() 
	{
		$('.snb .depth2 > li > p').removeClass('on');
	},
	clickTreeItem : function() 
	{
		var $selectItem = $(this);
		var $sub_menu = $selectItem.nextAll('ul');
		// tree메뉴 초기화 함수 
//		initTree($selectItem);
		
		if($sub_menu.length == 0)
		{
			$selectItem.addClass('on');
		}
		else if($sub_menu.css('display') == 'none')
		{
			$selectItem.addClass('on');
			$sub_menu.slideDown(200);
		}
		else
		{
			$sub_menu.slideUp(200);
		}
	},
	loadLeftPaneBySearch : function() 
	{
		with (this) 
		{			
			ajaxManager.runHtmlExt({
				url : "adcSetting/loadLeftPaneBySearch.action",
				target: "#leftPane",
				successFn : function(params) 
				{
					applyLeftCss();
					registerLeftEvents();
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_COMMON_ADCLISTLOAD, jqXhr);
				}	
			});
		}
	},
	loadAdcListContent : function(searchKey, groupindex, orderType, orderDir) 
	{
		with (this) 
		{
			if(searchKey && !validateDaterefresh())
			{
				return false;
			}
			ajaxManager.runHtmlExt({
				url : "adcSetting/loadAdcListContent.action",
				data : 
				{
					"searchKey" : searchKey == undefined ? "" : searchKey,
					"groupIndex" : groupindex == undefined ? "" : groupindex,
					"orderDir" : this.orderDir,
					"orderType" : this.orderType							
				},
				target: "#wrap .contents",
				successFn : function(params) 
				{
					header.setActiveMenu('AdcSetting');
					applyAdcListContentCss();
					registerAdcListContentEvents();
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_COMMON_ADCLISTLOAD, jqXhr);
				}	
			});
		}
	},
	// FLB 그룹 List Get
	loadFLBGroupMonitorContent : function(adcIndex)
	{
		with(this)
		{
			var params = 
			{
				"adc.index"	: adc.index			
			};
			ajaxManager.runHtmlExt({
				url			: "adcSetting/loadFlbGroupMonitorContent.action",
				data		: params,
				target		: "#flbGroupPopup",
				successFn	: function(data) 
				{
					showPopup({
						'id' : '#flbGroupPopup',
						'width' : '700px',
					});					
					registerFlbGroupMonitorContentEvents();
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_ADCSETTING_FLBGROUPINFOEXTRACT, jqXhr);
				}	
			});
		}
	},
	modifyFlbGroupMonitorContent : function(selecteAdcGroup)
	{
		with(this)
		{
			ajaxManager.runJsonExt({
				url : "adcSetting/modifyFlbGroupMonitorContent.action",
				data : 
				{
					"adc.index"	: adc.index,
					"selecteAdcGroup" : selecteAdcGroup
				},				
				successFn : function(data) 
				{
					if (!data.isSuccessful)
					{
						$.obAlertNotice(data.message);
						return;
					}			
					$.obAlertNotice(VAR_COMMON_REGISUCCESS);
					$('.popup_type1').remove();
					$('.cloneDiv').remove();
					loadAdcListContent();
					loadContent();
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_ADCSETTING_FLBINFOSAVEFAIL, jqXhr);
				}	
			});
		}
	},
	// FLB 팝업 Register Event
	registerFlbGroupMonitorContentEvents : function()
	{
		with(this)
		{
			$('.allGroupsChk').each(function(e)
			{				
				var CheckedCount = $(this).parent().parent().parent().parent().parent().find('.flbGroupchk').filter(':checked').length;							
				var $SelectCount = $('.pop_contents .SelectCount').filter(':last');
				$SelectCount.empty();
				$SelectCount.html(CheckedCount);
			});
			
			$('.closeLnk').click(function(e)
			{
				e.preventDefault();
				$('.popup_type1').remove();
				$('.cloneDiv').remove();
			});
			
			$('.completeLnk').click(function(e)
			{
				e.preventDefault();
				var selecteAdcGroup = $(this).parent().parent().parent().find('.flbGroupchk').filter(':checked').map(function() {
					return $(this).val();
				}).get();	
				modifyFlbGroupMonitorContent(selecteAdcGroup);				
			});
			$('.allGroupsChk').change(function(e) 
			{
				e.preventDefault();
				var isChecked = $(this).is(':checked');
				$('.flbTbody').find('.flbGroupchk').attr('checked', isChecked);
				
				var CheckedCount = $(this).parent().parent().parent().parent().parent().find('.flbGroupchk').filter(':checked').length;
				var $SelectCount = $('.pop_contents .SelectCount').filter(':last');
				$SelectCount.empty();
				$SelectCount.html(CheckedCount);
			});
			$('.flbGroupchk').change(function(e)
			{
				e.preventDefault();
				var isChecked = $(this).is(':checked');
				$(this).attr('checked', isChecked);
				
				var CheckedCount = $(this).parent().parent().parent().parent().parent().find('.flbGroupchk').filter(':checked').length;							
				var $SelectCount = $('.pop_contents .SelectCount').filter(':last');
				$SelectCount.empty();
				$SelectCount.html(CheckedCount);
			});
		}		
	},
	
	applyAdcListContentCss : function() 
	{
		// 마지막 li 태그 밑줄 스타일 적용
		$('.wrap .contain .content .accordion_type1 li:last').css('border-bottom','1px solid #676767');
		
		// 테이블 컬럼 정렬
		initTable(["#table1 tbody tr","#table2 tbody tr"],[2, 5, 7],[-1]);
	},
	registerAdcListContentEvents : function() 
	{
		with (this) 
		{
			if(this.searchFlag == true)
			{
				if($('.adcSetList').size() > 0)
				{
					$('.searchNotMsg').addClass("none");
				}
				else
				{
					$('.searchNotMsg').removeClass("none");
					$('.delAdcs').addClass("none");
				}
				searchFlag=false;
			}
			else
			{
				$('.delAdcs').removeClass("none");
				$('.searchNotMsg').addClass("none");
			}
			
			// acordion 슬라이드 효과
			$('.wrap .contain .content .accordion_type1 h2').click(function(e) 
			{
				/*if($(this).next().css('display') =='block')
				{
					$(this).next().slideUp(100);
					var src = $(this).nextAll('.open').find('img').attr('src');
					$(this).nextAll('.open').find('img').attr('src',src.replace('up2.png', 'down2.png'));
					slideUpAdcListAccordion.call(this);
				}
				else
				{
					$(this).parents('.accordion_type1').find('li').each(function(index, element) {
						$(this).children('div').slideUp(100);
						var src = $(this).find('.open img').attr('src');
						$(this).find('.open img').attr('src',src.replace('up2.png', 'down2.png'));
					});
					$(this).next('div').slideDown(300);
					var src = $(this).nextAll('.open').find('img').attr('src');
					$(this).nextAll('.open').find('img').attr('src',src.replace('down2.png', 'up2.png'));
					slideDownAdcListAccordion.call(this);					
				}*/
				if($(this).next().css('display') == 'block')
				{
					$(this).next().hide();	//slideUp(100);
					var src = $(this).nextAll('.open').find('img').attr('src');
					$(this).nextAll('.open').find('img').attr('src', src.replace('up2.png', 'down2.png'));
				}
				else
				{
					$(this).next('div').show();//slideDown(300);
					var src = $(this).nextAll('.open').find('img').attr('src');
					$(this).nextAll('.open').find('img').attr('src', src.replace('down2.png', 'up2.png'));
				}
			});
		
			$('.orderDir_Desc').click(function(e)
			{
				e.preventDefault();
				orderDir =  $(this).find('.orderDir').text();
				orderType =  $(this).find('.orderType').text();
				var searchKey = $('p.cont_sch input.searchTxt').val();
				FlowitUtil.log('click:' + searchKey);
				var groupindex = $(".snb").find(".GroupIndex").text();
				searchFlag=true;
				loadAdcListContent(searchKey, groupindex, orderType, orderDir);
			});
			
			$('.orderDir_Asc').click(function(e)
			{
				e.preventDefault();
				orderDir =  $(this).find('.orderDir').text();
				orderType =  $(this).find('.orderType').text();
				var searchKey = $('p.cont_sch input.searchTxt').val();
				FlowitUtil.log('click:' + searchKey);
				var groupindex = $(".snb").find(".GroupIndex").text();
				searchFlag=true;
				loadAdcListContent(searchKey, groupindex, orderType, orderDir);
			});
			
			$('.orderDir_None').click(function(e)
			{
				e.preventDefault();
				orderDir =  $(this).find('.orderDir').text();
				orderType =  $(this).find('.orderType').text();
				var searchKey = $('p.cont_sch input.searchTxt').val();
				FlowitUtil.log('click:' + searchKey);
				var groupindex = $(".snb").find(".GroupIndex").text();
				searchFlag=true;
				loadAdcListContent(searchKey, groupindex, orderType, orderDir);
			});
			
			$('.wrap .contain .content .accordion_type1 .open').click(function(e) {
				$(this).prevAll('h2').trigger('click');
			});
			
			$('#addAdcBtn').click(function(e)
			{
				loadAdcAddContent();
				$('.LocationNavi').addClass('none');
			});
			
			$('.adcIndexLnk').click(function(e) 
			{
				e.preventDefault();
				var adcIndex = $(this).children('span').text();
				var adcType = $(this).parent().parent().find('.adcType').val();
				var opMode = $(this).parent().parent().find('.opMode').val();
				adcSetting._selectAdc(adcIndex);
				
				adcSetting.setSelectIndex(2);
				loadAdcModifyContent(adcIndex, adcType, opMode);
				
				$('.adcSettingSubSub').removeClass('none');
				$('.slbSettingSubSub').addClass('none');
				$(".adcSettingSubSub").find('a').removeClass("select");
				$(".adcSettingSubSub").find('a').removeClass("choice");					
				$(".adcSettingSubSub").find('.adcModify a').attr("class", "choice");	
				
				if(adcSetting.getAdc().type == "F5" || adcSetting.getAdc().type == "Alteon")
				{
					$(".adcConnect").removeClass('none');
				}
				else
				{
					$(".adcConnect").addClass('none');
				}
				
			});
			
			$('.allAdcsChk').click(function(e) 
			{
				var isChecked = $(this).is(':checked');
				$(this).parent().parent().parent().parent().find('.adcChk').attr('checked', isChecked);
			});
			$('.delAdcs').off('click');  // 중복 이벤트 방지를 위한 등록 해제
			$('.delAdcs').click(function(e) 
			{
				e.preventDefault();
				with (this) 
				{
					var adcIndices = $(this).parent().parent().parent().parent().find('.adcChk').filter(':checked').map(function() {
						return $(this).val();
					}).get();														//alert("adcIndices : " + adcIndices);
					FlowitUtil.log(adcIndices);
					if (adcIndices.length == 0) 
					{   								//$.obAlertNotice("adcIndices, length : " + adcIndices + " :::: " + adcIndices.length);
						$.obAlertNotice(VAR_ADCSETTING_ADCDELSEL);
						return;
					}
					
					var chk = confirm(VAR_ADCSETTING_ADCDEL);
					if(chk) 
					{
						delAdcs(adcIndices);
					}
					else 
					{
						return false;
					}
				}
			});

			$('.monitorLnk').click(function(e) 
			{
				e.preventDefault();
				with (this) 
				{
					var adcIndex = $(this).parent().parent().children().first().find('input').val();
					FlowitUtil.log("index for monitor: %s", adcIndex);
					_selectAdc(adcIndex);
//					adcSetting.setSelectIndex(2);
//					networkMap.loadNetworkMapContent(adcSetting.getAdc());
//					$('.monitorMnu').click();
//					networkMap.loadNetworkMapContent(adcSetting.getAdc(), 0, undefined, undefined, taskQ);
					$('.monitorMnu').click();
					$('.monitorNetworkMnu').click();
				}
			});
			
			$('.forced_update_Lnk').click(function(e) 
			{
				e.preventDefault();
				var adcIndex = $(this).parent().parent().children().first().find('input').val();
				downloadSLBConfigForced(adcIndex);
			});
			
			$('.flbGroupBtn').click(function(e) 
			{				
				e.preventDefault();
				with (this) 
				{
					var adcIndex = $(this).parent().parent().children().first().find('input').val();					
					_selectAdc(adcIndex);
					loadFLBGroupMonitorContent();
				}
			});
			
			// search event
			$('p.cont_sch a.searchLnk').click(function (e) 
			{
				e.preventDefault();
				var searchKey = $('p.cont_sch input.searchTxt').val();
				FlowitUtil.log('click:' + searchKey);
				searchFlag=true;
				loadAdcListContent(searchKey);
			});
			
			$('p.cont_sch input.searchTxt').keydown(function(e) 
			{
				if (e.which != 13)
					return;
				
				var searchKey = $(this).val();
				FlowitUtil.log('click:' + searchKey);
				searchFlag=true;
				loadAdcListContent(searchKey);
			});
			
			//
			$('.saveLnk').click(function(e) 
			{
				e.preventDefault();
				var adcIndex = $(this).parent().parent().children().first().find('input').val();
				saveAdcConfigContent(adcIndex);
			});
			
			if (!searchPurchaseDate)
			{
				searchPurchaseDate = new Date();
	//			alert("searchPurchaseDate : " + searchPurchaseDate);
			}
			
			$('input[name="adcAdd.adc.purchaseDate"]').datepicker({
				maxDate: "0",
				dateFormat : "yy-mm-dd",
				showOn: "button",
				buttonImage: "imgs/meun/btn_calendar.png",
				buttonImageOnly: true,
				defaultDate: searchPurchaseDate				
			});
			
			_registerAdcNameAutoCompleteEvents();
			
			// group name click event; error since they cross-call each other.
	//		$('.accordion_type1 > li > h2').click(function(e) {
	//			var groupName = $(this).text().split('(')[0];
	//			FlowitUtil.log(groupName);
	//			var $groupNameOnLeftTree = $('.depth1 > li > p');
	//			FlowitUtil.log($groupNameOnLeftTree.text());
	//			for (var i=0; i < $groupNameOnLeftTree.length; i++) {
	//				if ($groupNameOnLeftTree.eq(i).text().split('(')[0] == groupName) {
	//					AdcSetting.clickTreeItem($groupNameOnLeftTree.eq(i));
	//					return;
	//				}
	//			}
	//		});
		}
	},
	saveAdcConfigContent : function(adcIndex) 
	{		
		with (this) 
		{
			ajaxManager.runJsonExt({
				url : "adcSetting/saveAdcConfigContent.action",
				data : 
				{
//					"adcIndex" : adcIndex,
					"adcAdd.adc.index" : adcIndex
				},				
				successFn : function(data) 
				{ //alert("1 : " + data.isSuccessful);
					if (!data.isSuccessful)
					{			//isSuccessful : false 인경우 message
//						$.obAlertNotice("2 : " + data.message);
						$.obAlertNotice(data.message);
						return;
					}			
					$.obAlertNotice(VAR_COMMON_REGISUCCESS);
					loadAdcListContent();
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_ADCSETTING_ADCINFOSAVEFAIL, jqXhr);
				}	
			});
		}
	},
	downloadSLBConfigForced : function(adcIndex)
	{
		with(this)
		{
			ajaxManager.runJsonExt({
				url : "adcSetting/downloadSLBConfigForced.action",
				data : 
				{
					"adcIndex" : adcIndex
				},
				successFn : function(data) 
				{
					if (!data.isSuccessful) 
					{
						if(data.extraKey == 1)
						{
							return;
						}
						else
						{
							$.obAlertNotice(data.message);						
							return;							
						}
					}
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_VS_VSECEFAIL, jqXhr);
				}	
			});
		}
	},
	validateDaterefresh : function()
	{
		var searchTxt = $('.control_Board input.searchTxt');
		return searchTxt.validate({
			name: '검색어',
			required: false,
			type: 'name',
			lengthRange: [1,65]
		});
	},
	slideUpAdcListAccordion : function() 
	{
		$(this).next().slideUp(100);
		var src = $(this).nextAll('.open').find('img').attr('src');
		$(this).nextAll('.open').find('img').attr('src',src.replace('up2.png', 'down2.png'));
	},
	slideDownAdcListAccordion : function() 
	{
		$(this).parents('.accordion_type1').find('li').each(function(index, element) 
		{
			//$(this).children('div').slideUp(100);
			$(this).css('display', 'none');
//			var src = $(this).find('.open img').attr('src');
//			$(this).find('.open img').attr('src',src.replace('up2.png', 'down2.png'));
		});
//		$(this).next('div').slideDown(300);
		$(this).parent().css('display', '');
//		var src = $(this).nextAll('.open').find('img').attr('src');
//		$(this).nextAll('.open').find('img').attr('src',src.replace('down2.png', 'up2.png'));
	},
	slideDownAllAdcGroupsOnContent : function() 
	{
		$('.content .accordion_type1 > li').each(function(index, element)
		{
//			$(this).children('div').slideDown(100);
			$(this).css('display', 'block');
			var src = $(this).find('.open img').attr('src');
			$(this).find('.open img').attr('src',src.replace('down2.png', 'up2.png'));
		});
	},
	delAdcs : function(adcIndices) 
	{
		with (this) 
		{
			ajaxManager.runJsonExt({
				url : "adcSetting/delAdcs.action",
				data : 
				{
					"adcIndices" : adcIndices,
				},
				successFn : function(data) 
				{
					if (!data.isSuccessful) 
					{
						$.obAlertNotice(data.message);
						return;
					}
					FlowitUtil.log('deleted successfully.');
					refresh(true);
					loadAdcListContent();
				},
				errorFn : function(jqXhr)
				{
					refresh(true);
					$.obAlertAjaxError(VAR_ADCSETTING_ADCINFODDELFAIL, jqXhr);
				}
			});
		}
	},
	loadAdcListContentBySearch : function() 
	{
		with (this) 
		{
//			FlowitUtil.getHTML({
//				async : false,
//				url : "adcSetting/loadAdcListContentBySearch.action",
//				target: "#wrap .contents",
//				successFn : function(params) {
//					applyAdcListContentCss();
//					registerAdcListContentEvents();
//				}
//			});
			
			ajaxManager.runHtmlExt({
				url : "adcSetting/loadAdcListContentBySearch.action",
				target: "#wrap .contents",
				successFn : function(params) 
				{
					applyAdcListContentCss();
					registerAdcListContentEvents();
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_COMMON_ADCLISTLOAD, jqXhr);
				}	
			});
		}
	},
	loadAdcAddContent : function() 
	{
		with (this) 
		{
			FlowitUtil.log('-- loadAdcAddContent');
//			FlowitUtil.getHTML({
//				async : false,
//				url : "adcSetting/loadAdcAddContent.action",
//				target: "#wrap .contents",
//				successFn : function(params) {
//					setActiveContent('AdcAddContent');
//					$unassignedAccountOptions = $();
//					registerAdcAddContentEvents(false);
//				}
//			});
			
			ajaxManager.runHtmlExt({
				url : "adcSetting/loadAdcAddContent.action",
				target: "#wrap .contents",
				successFn : function(params) 
				{
					setActiveContent('AdcAddContent');
					$unassignedAccountOptions = $();
					registerAdcAddContentEvents(false);
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_COMMON_ADCLISTLOAD, jqXhr);
				}	
			});
		}
	},
	registerAdcAddContentEvents : function(isModify) 
	{
		with (this) 
		{
			$('#adcAddFrm').submit(function() 
			{
				if (!validateAdcAdd(isModify))
				{
					return false;
				}
				
//				// from GS. #4012-1 #1 : 14.07.29 sw.jung GS로부터 이전
//				var testStatus = _calConnectionTestStatus(isModify);//gstest
//				if(testStatus.isNotTestItem==1)
//				{
//					if(!confirm(VAR_ADCSETTING_CONNTESTNOT))
//						return false;
//				}
				selectRegisteredAccountsForSubmit();
				
				var snmpPrivProto;
				if ($('input[name="adcAdd.adc.adcSnmpInfo.privPassword"]').val() != "")
				{
					snmpPrivProto = "des";
				}
				else
				{
					snmpPrivProto = "none";
				}
				
				var ssoMode;
				if ($('input[name="adcAdd.adc.ssoMode"]').is(':checked') == true)
				{
					ssoMode = "1";
				}
				else
				{
					ssoMode = "0";
				}
				
			    $(this).ajaxSubmit({
	//		    	async : false,
					dataType : 'json',
					url : isModify ? 'adcSetting/modifyAdc.action' : 'adcSetting/addAdc.action',
					data : 
					{
						"isReachable" : isReachable,
						"adcAdd.adc.adcSnmpInfo.privProto" : snmpPrivProto,
//						"adcAdd.adc.ssoMode" : ssoMode
					},
					success : function(data) 
					{
						if (!data.isSuccessful) 
						{
							//if(data.message)
							//{
							//	$.obAlertNotice(data.message);
							//	return false;
							//}
							// 라이선스 인증 실패시 2가지 케이스는 confirm 창이 나타남 15.02.24 yh.yang
							if(data.confirmMsg)
							{
								if(confirm(data.message) == true)
								{
									license.loadContent();
								}
								else
								{
									return false;
								}
							}
							else
							{
								if(data.extraKey == 1)
								{
									return;
								}
								else
								{
									$.obAlertNotice(data.message);						
									return;							
								}
							}							
							return false; // #3926-8 #1: 14.07.24 sw.jung 실패시 목록으로 이동하지 않음
						}
						FlowitUtil.log('registered successfully.');
						refresh(true);
						loadAdcListContent();
						// TODO: 네비게이션 갱신(전체로 빠지기)
//						$('.snb .snb_tree .adcListContentLnk').click();
						if(isModify)
						{
							var OpMode = $('input[name="adcAdd.adc.opModeHidden"]').val();
							updateSelectAdcInfo(OpMode);
						}						
					},
					error : function(jqXhr)
					{
						// #3926-8 #2: 14.07.24 sw.jung 라이센스 관련 오류 발생시 메세지 변경
						if (jqXhr.responseText.indexOf('license') > 0)
							$.obAlertAjaxError(VAR_ADCSETTING_INVALIDLICENSE, jqXhr);
						else // 기타 오류 발생시
							$.obAlertAjaxError(VAR_ADCSETTING_ADDMODIFYFAIL, jqXhr);
						refresh(true);
						loadAdcListContent();
					}
			    /*
			    	success : function(data) {
			    		if (!data.isSuccessful) {
			    			if(data.isOBException) {
			    				popUpException(data.adcexception);
			    			}
			    			else {
			    				alert(data.message);
			    				return;
			    			}
			    		}
			    		FlowitUtil.log('registered successfully.');
						refresh();
						loadAdcListContent();
			    	}*/
				});
		 
		        // always return false to prevent standard browser submit and page navigation
		        return false; 
		    }); 
			
	//		$('.adcType').change(function(e) {
	//			if ($(this).val() == 'F5')
	//				$('.standbySpn').css('display', 'none');
	//			else
	//				$('.standbySpn').css('display', 'inline');
	//		});
			
			var selectSnmpVersion = $('input[name="adcAdd.adc.adcSnmpInfo.version"]:checked').val();
			if (selectSnmpVersion == 2)
			{
				$('.snmpv2Version').removeClass("none");
				$('.snmpv3Version').addClass("none");
			}
			else if (selectSnmpVersion == 3)
			{
				$('.snmpv2Version').addClass("none");
				$('.snmpv3Version').removeClass("none");
			}
			else
			{
				$('.snmpv2Version').removeClass("none");
				$('.snmpv3Version').addClass("none");
			}
			
			var protocolChangeView = $('select[name="adcAdd.adc.connProtocol"]').val();
			if (protocolChangeView == 1)				
			{
				$('.connPortView').addClass('none');
				$('.protocolCheck').removeClass('none');
			}
			else
			{
				$('.connPortView').removeClass('none');
				$('.protocolCheck').addClass('none');
			}
			
			$('.adcConfigCheckLnk').click(function(e)
			{
				e.preventDefault();
				var adcIndex = $('input[name="adcAdd.adc.index"]').val();
				var adcType = $('input[name="adcAdd.adc.type"]').val();
				var opMode = $('input[name="adcAdd.adc.opModeHidden"]').val();
				//var snmpVer = $('input[name="adcAdd.adc.snmpVersion"]:checked').val();
				//var adcStatus = $('input[name="adcAdd.adc.status"]').val();
				
//				if(adcStatus != "available")
//				{
//					alert(VAR_CONFIGCHECK_ADCSETLOAD);
//					return false;
//				}
				configCheck.loadContent(adcIndex, adcType, opMode);
				if(adcType == "PiolinkUnknown")
				{
					$.obAlertNotice(VAR_ADCSETTING_ADCKINDNOTCLEAR);
				}
				else
				{
					header.setAdcSettingTap(1);
					configCheck.loadContent(adcIndex, adcType, opMode);
				}
			});
			// Diagnosis Mode 의 qm button event
			$('.diagnosis_qm').mouseover(function(e)
			{
				$('#diagnosis_notice').removeClass('none');
			});
			
			$('.diagnosis_qm').mouseleave(function(e)
			{
				$('#diagnosis_notice').addClass('none');
			});
			
			// Setting Mode 의 qm button event
			$('.setting_qm').mouseover(function(e)
			{
				$('#setting_notice').removeClass('none');
			});
			
			$('.setting_qm').mouseleave(function(e)
			{
				$('#setting_notice').addClass('none');
			});
			
			// Monitoring Mode 의 qm button event
			$('.monitoring_qm').mouseover(function(e)
			{
				$('#monitoring_notice').removeClass('none');
			});
			
			$('.monitoring_qm').mouseleave(function(e)
			{
				$('#monitoring_notice').addClass('none');
			});

			// Monitoring Mode 의 qm button event
			$('.mgmt_qm').mouseover(function(e)
			{
				$('#mgmt_notice').removeClass('none');
			});
			
			$('.mgmt_qm').mouseleave(function(e)
			{
				$('#mgmt_notice').addClass('none');
			});
			
			
			// ADC type RadioBtn 클릭시 발생하는 이벤트
			$('input[name="adcAdd.adc.type"]').change(function()
			{
				var selectAdcType = $('input[name="adcAdd.adc.type"]:checked').val();				
				
				removePeerIp(selectAdcType);
				removerConnServiceTelnet(selectAdcType);
				removeCliAccount(selectAdcType);
				removeModeDiagnosis(selectAdcType);
								
				if (selectAdcType == "Alteon")
				{					
					$('#MODE_MONITORING').attr("checked", "checked");
					$('#v3').removeAttr("disabled");
					
					$('.contents_area .mgmtChk').removeClass('none');
					$('.contents_area .ssoChk').removeClass('none');
				}
				else if (selectAdcType == "PiolinkUnknown")
				{					
					$('#MODE_MONITORING').attr("checked", "checked");
					$('#v2').attr("checked", "checked");
					$('#v2').change();
					
					$('.contents_area .mgmtChk').addClass('none');
					$('.contents_area .ssoChk').addClass('none');
				}
				else 
				{					
					$('#MODE_MONITORING').attr("checked", "checked");
					$('#v3').attr("disabled", "disabled");
					
					// 14.07.29 sw.jung Alteon->Other 전환시 v2 자동전환
					$('#v2').attr("checked", "checked");
					$('#v2').change();
					
					$('.contents_area .mgmtChk').addClass('none');
//					$('.contents_area .ssoChk').addClass('none');
					$('.contents_area .ssoChk').removeClass('none');
				}
				
				if(selectAdcType == 'Alteon')
				{
					if($('input[name="adcAdd.adc.opMode"]:checked').val() == 1)
					{
						$('#MODE_DIAGNOSIS').attr("checked", false);
						$('#MODE_SETTING').attr("checked", false);
						
						$('.loginField').removeClass('Lth2');
						$('.loginField').addClass('Lth1');
					}
					else if($('input[name="adcAdd.adc.opMode"]:checked').val() == 2)
					{
						$('#MODE_DIAGNOSIS').attr("checked", false);
						$('#MODE_MONITORING').attr("checked", false);
						
						$('.loginField').removeClass('Lth1');
						$('.loginField').addClass('Lth2');
					}
					else if($('input[name="adcAdd.adc.opMode"]:checked').val() == 3)
					{
						$('#MODE_SETTING').attr("checked", false);
						$('#MODE_MONITORING').attr("checked", false);
						
						$('.loginField').removeClass('Lth1');
						$('.loginField').addClass('Lth2');
					}

				}
				else
				{
					$('.loginField').removeClass('Lth1');
					$('.loginField').addClass('Lth2');
					
					if($('input[name="adcAdd.adc.opMode"]:checked').val() == 1)
					{
						$('#MODE_DIAGNOSIS').attr("checked", false);
						$('#MODE_SETTING').attr("checked", false);
					}
					else if($('input[name="adcAdd.adc.opMode"]:checked').val() == 2)
					{
						$('#MODE_DIAGNOSIS').attr("checked", false);
						$('#MODE_MONITORING').attr("checked", false);
					}
					else if($('input[name="adcAdd.adc.opMode"]:checked').val() == 3)
					{
						$('#MODE_SETTING').attr("checked", false);
						$('#MODE_MONITORING').attr("checked", false);
					}
				}
			});			

			//모드 클릭시 발생하는 이벤트 (알테온이고, 모니터링 모드일때 로그인에 필수항목 표시 제거)
			$('input[name="adcAdd.adc.opMode"]').change(function()
			{
				var hiddenArea = $('input[name="adcAdd.adc.opModeHidden"]');
				hiddenArea.val($('input[name="adcAdd.adc.opMode"]:checked').val());
				if($('input[name="adcAdd.adc.opMode"]:checked').val() == 1)
				{
					$('#MODE_DIAGNOSIS').attr("checked", false);
					$('#MODE_SETTING').attr("checked", false);
					
					if($('input[name="adcAdd.adc.type"]').val() == 'Alteon' || $('input[name="adcAdd.adc.type"]:checked').val() == 'Alteon') //알테온일때만
					{
						$('.loginField').removeClass('Lth2');
						$('.loginField').addClass('Lth1');
					}
				}
				else if($('input[name="adcAdd.adc.opMode"]:checked').val() == 2)
				{
					$('#MODE_DIAGNOSIS').attr("checked", false);
					$('#MODE_MONITORING').attr("checked", false);

					$('.loginField').removeClass('Lth1');
					$('.loginField').addClass('Lth2');
				}
				else if($('input[name="adcAdd.adc.opMode"]:checked').val() == 3)
				{
					$('#MODE_SETTING').attr("checked", false);
					$('#MODE_MONITORING').attr("checked", false);

					$('.loginField').removeClass('Lth1');
					$('.loginField').addClass('Lth2');
				}
			});
			
			$('.connServiceSelect').change(function()
			{//ADC 통신 포트 변경
				var selectServiceType = $('input[name="adcAdd.adc.connService"]:checked').val();
				if (selectServiceType == '23')
				{
					$('#telnet').removeAttr("disabled");
					$('#ssl').attr("disabled","disabled");
					$('.networkOk').addClass('none');
					$('.networkFail').addClass('none');
					$('.networkConfirm').removeClass('none');
					$('.conn_statNetwork').text('');
				}
				else if (selectServiceType == '22')
				{
					$('#ssl').removeAttr("disabled");
					$('#telnet').attr("disabled","disabled");
					$('.networkOk').addClass('none');
					$('.networkFail').addClass('none');
					$('.networkConfirm').removeClass('none');
					$('.conn_statNetwork').text('');
				}				
			});		
			
			$('.protocolChange').change(function()
			{
				if ($('select[name="adcAdd.adc.connProtocol"]').val() == 1)
				{
					$('.connPortView').addClass('none');
					$('.protocolCheck').removeClass('none');
				}
				else
				{
					$('.connPortView').removeClass('none');
					$('.protocolCheck').addClass('none');
				}
			});
			
			// snmp version radio 버튼 클릭시 발생하는 이벤트
			$('input[name="adcAdd.adc.adcSnmpInfo.version"]').change(function()
			{
				var selectSnmpVersion = $('input[name="adcAdd.adc.adcSnmpInfo.version"]:checked').val();

				hiddenSnmp(selectSnmpVersion, isModify);	
			});
			
			// snmp auth protocal change
			$('.authProtocolCbx').change(function()
			{
				svcValue = $('select[name="adcAdd.adc.adcSnmpInfo.AuthProto"] :selected').val();
			});
			
			// 전체 testConnect
			$('#testConnectionLnk').click(function(e) 
			{
				e.preventDefault();
				testConnection();
//				testConnection(isModify);
			});
			
			// Network Check
			$('.testConnectionNetworkLnk').click(function(e) 
			{
				e.preventDefault();
				status =  $(this).find('.status').text();
				testConnAdc(status, isModify);
			});
			// Login Check
			$('.testConnectionLoginLnk').click(function(e) 
			{
				e.preventDefault();
				status =  $(this).find('.status').text();
				testConnAdc(status, isModify);				
			});
			$('.testConnectionLoginF5Lnk').click(function(e) 
			{
				e.preventDefault();
				status =  $(this).find('.status').text();
				testConnAdc(status, isModify);
			});
			// SNMP Check
			$('.testConnectionSnmpLnk').click(function(e) 
			{
				e.preventDefault();
				status =  $(this).find('.status').text();
				testConnAdc(status, isModify);
			});
			// Syslog Check
			$('.testConnectionSyslogLnk').click(function(e) 
			{
				e.preventDefault();
				status =  $(this).find('.status').text();
				testConnAdc(status, isModify);
			});
			
			$('#adcGroupMgmtLnk').click(function(e) 
			{
				e.preventDefault();
				popUpAdcGroupMgmt();
			});
			
			$('.toAccountsSelectionLnk').click(function(e) 
			{
				e.preventDefault();
				moveAccountsToSelection();
			});
			
			$('.toAccountsDeselectionLnk').click(function(e) 
			{
				e.preventDefault();
				moveAccountsToDeselection();
			});
			
			$('#adcAddCancelBtn').click(function(e) 
			{
				e.preventDefault();
				refresh(true);
				loadAdcListContent();
			});
			
			$('#adcAddOkBtn').click(function(e) 
			{
				e.preventDefault();
				$('#adcAddFrm').submit();
			});
			
			// search event
			$('.accountSearchLnk').click(function (e) 
			{
				e.preventDefault();
				$('.control_Board input.searchTxt').val();
				var searchKey = $('.control_Board input.inputText_search').val();
				FlowitUtil.log('click:' + searchKey);
				_searchOnUnassignedAccounts(searchKey);
			});
			
			$('.inputText_search').keydown(function(e) 
			{
				if (e.which != 13)
					return;
				
				var searchKey = $(this).val();
				FlowitUtil.log('click:' + searchKey);
				_searchOnUnassignedAccounts(searchKey);
			});
			
			if (!searchPurchaseDate) 
			{
				searchPurchaseDate = new Date();
			}
			
			$('input[name="adcAdd.adc.purchaseDate"]').datepicker({
				maxDate: "0",
				dateFormat : "yy-mm-dd",
				showOn: "button",
				buttonImage: "imgs/meun/btn_calendar.png",
				buttonImageOnly: true,
				defaultDate: searchPurchaseDate				
			});	
			
			// button image 변경 - 4단계 값이 없는 경우 비활성화 , 값이 만족되면 gray button으로 활성화, 정상이면 green button으로 , 비정상이면 red button으로 변경	
			
			$('input[name="adcAdd.adc.ip"]').change(function()
			{
//				alert($('.defaultConn').show());
//				if ($('.defaultConn').show() == true)
//				{
//					$('.default').addClass("none");
//					$('.confirm').removeClass("none");
//				}
//				else
//				{
//					$('.default').removeClass("none");
//					$('.confirm').addClass("none");
//				}
				
//				var adcIpVal = $('input[name="adcAdd.adc.ip"]').val();				
//				
//				if (adcIpVal != "")
//				{
//					$('.default').addClass("none");
//					$('.confirm').removeClass("none");
//				}
//				else 
//				{
//					$('.default').removeClass("none");
//					$('.confirm').addClass("none");
//				}
				
//				var adcIpVal = $('input[name="adcAdd.adc.ip"]').val();				
//				
//				if (adcIpVal != "")
//				{
//					$('.default').addClass("none");
//					$('.confirm').removeClass("none");
//				}
//				else 
//				{
//					var html = "";
//					var bynarea = $(".test");
//					html = $(".test2 .yyh").filter(':last');
//					bynarea.empty();
//					bynarea.html(html);
//				}
			});	
					
			$('.defaultConn').click(function(e)
			{
				status =  $(this).find('.status').text();
				validateConnection(status);				
			});
			
			if (isModify)
			{
				if ($('input[name="adcAdd.adc.type"]').val() == "F5") 
				{
					$('.loginConfirm').addClass("none");
					$('.contents_area .mgmtChk').addClass('none');
//					$('.contents_area .ssoChk').addClass('none');
					$('.contents_area .ssoChk').removeClass('none');
				}
				else if ($('input[name="adcAdd.adc.type"]').val() == "Alteon")
				{
					$('.loginConfirm').removeClass("none");
					$('.contents_area .mgmtChk').removeClass('none');
					$('.contents_area .ssoChk').removeClass('none');
				}
				else
				{
					$('.loginConfirm').removeClass("none");
					$('.contents_area .mgmtChk').addClass('none');
					$('.contents_area .ssoChk').addClass('none');
				}
			}
			
			$('input[name="adcAdd.adc.mgmtMode"]').change(function()
			{
				var mgmtModeVal = $('input[name="adcAdd.adc.mgmtMode"]:checked').val();

				if(mgmtModeVal == 2)
				{
					$('.mgmt_notice').removeClass('none');
				}
				else
				{					
					$('.mgmt_notice').addClass('none');
				}
			});
	
			if($('input[name="adcAdd.adc.mgmtMode"]:checked').val() == 2)
			{
				$('.mgmt_notice').removeClass('none');
			}
			else
			{
				$('.mgmt_notice').addClass('none');
			}
			
			
//			if (isModify)
//			{
//				if ($('input[name="adcAdd.adc.type"]').val() == "Alteon") 
//				{
//					$('.loginConfirm').addClass("none");
//					$('#v3').removeAttr("disabled");
//					$('#v2').removeAttr("disabled");
//				}
//				else
//				{
//					if (selectSnmpVersion == 3)
//					{
//						$('.loginConfirm').removeClass("none");
//						$('#v3').removeAttr("disabled");
//						$('#v2').attr("disabled", "disabled");
//					}
//					else
//					{
//						$('.loginConfirm').addClass("none");
//						$('#v3').attr("disabled", "disabled");
//						$('#v2').removeAttr("disabled");
//					}
//				}
//			}
			
//			var selectSnmpVersion = $('input[name="adcAdd.adc.adcSnmpInfo.version"]:checked').val();			
//			hiddenSnmp(selectSnmpVersion, isModify);
			
			// IP 주소 입력시 SYSLOG IP 자동으로 입력
			var adcIp = $('input[name="adcAdd.adc.ip"]');					
//			adcIp.blur(function(e)
//			{
//				$('input[name="adcAdd.adc.syslogip"]').val($(adcIp).val());
//			});
		
			adcIp.blur(function(e)
			{
				$('input[name="adcAdd.adc.ipCp"]').val($(adcIp).val());
			});
			
			_registerAdcNameAutoCompleteEvents();
		}
	},
	updateSelectAdcInfo : function(OpMode)
	{
		//TODO ADC 수정 시 OP_MODE UPDATE에 대한 임시조치
		this.adc.mode = OpMode;		
	},
	removePeerIp : function(adcType)
	{		
		if (adcType == "PiolinkUnknown")
		{			
			 $('.contents_area .PeeripInputEvent').hide();
		}
		else if (adcType == "F5" || adcType == "Alteon")
		{
			$('.contents_area .PeeripInputEvent').show();
		}
		else
		{
			return false;
		}		
	},	
	removerConnServiceTelnet : function(adcType)
	{
		if(adcType == "F5")
		{
			$('.telnetConnService').addClass("none");
			$('#sslSelect').attr("checked", "checked");
			$('#ssl').removeAttr("disabled","disabled");			
		}
		else if(adcType == "PiolinkUnknown" || adcType == "Alteon")
		{
			$('.telnetConnService').removeClass("none");
			$('#telnetSelect').attr("checked", "checked");			
			$('#ssl').attr("disabled","disabled");
			$('#telnet').removeAttr("disabled","disabled");
		}
	},
	removeCliAccount : function(adcType)
	{
		if(adcType == "F5")
		{
			$('.contents_area .CliIdInputEvent').show();
			$('.contents_area .CliPasswdInputEvent').show();
			$('.loginConfirm').addClass("none");
		}
		else if(adcType == "PiolinkUnknown" || adcType == "Alteon")
		{
			 $('.contents_area .CliIdInputEvent').hide();
			 $('.contents_area .CliPasswdInputEvent').hide();
			 $('.loginConfirm').removeClass("none");
		}
	},
	removeModeDiagnosis : function(adcType)
	{
		if(adcType == "F5" || adcType == "Alteon")
		{
			$('.contents_area .mode1').show();			
		}
		else if(adcType == "PiolinkUnknown" )
		{
			 $('.contents_area .mode1').hide();			 
		}
	},
	displayVrrp : function(type) 
	{
		var $alteonSet = $('.alteonSet');
		if (type == 'Alteon') 
		{
			$alteonSet.show();
		} 
		else 
		{			
			$alteonSet.hide();
		}
	},
	
	hiddenSnmp : function(selectSnmpVersion, isModify)	
	{		
		if (isModify == true)
		{
			type = $('input[name="adcAdd.adc.type"]').val();
		}
		else
		{
			type = $('input[name="adcAdd.adc.type"]:checked').val();
		}
		
		if (type == 'Alteon') 
		{
			if (selectSnmpVersion == "2")
			{
				$('.snmpv2Version').removeClass("none");
				$('.snmpv3Version').addClass("none");
			}
			else if (selectSnmpVersion == "3")
			{
				$('.snmpv2Version').addClass("none");
				$('.snmpv3Version').removeClass("none");
			}
			else
			{
				$('.snmpv2Version').removeClass("none");
				$('.snmpv3Version').addClass("none");
			}
		}
		else
		{
			$('.snmpv2Version').removeClass("none");
			$('.snmpv3Version').addClass("none");
		}
	},	
	
	
	// from GS. #4012-1 #1,#3926-8 #3: 14.07.29 sw.jung 폼값 유효성 검사구문 개선(GS+추가)
	validateAdcAdd : function(isModify)
	{
		var result = $.validate(
			[
				// ip
				{
					target: $('input[name="adcAdd.adc.ip"]'),
					name: $('input[name="adcAdd.adc.ip"]').parent().parent().find('li').text(),
					required: true,
					type: "ip"
				},
				// peer ip
				{
					target: $('input[name="adcAdd.adc.peerip"]'),
					name: $('input[name="adcAdd.adc.peerip"]').parent().parent().find('li').text(),
					checked: !$('#PiolinkSelect').is(':checked'), // 추가: 파이오링크에선 pass
					type: "ip"
				},
				// 통신포트 Telnet(Alteon)
				{
					target: $('#telnet'),
					name: $('#telnet').parent().find('label').text(),
					checked: $('#telnetSelect').is(':checked'),
					required: true,
					type: "number",
					range: [1,65535]
				},
				// 통신포트 SSH
				{
					target: $('#ssl'),
					name: $('#ssl').parent().find('label').text(),
					checked: $('#sslSelect').is(':checked'),
					required: true,
					type: "number",
					range: [1,65535]
				},
				// 아이디
				{
					target: $('input[name="adcAdd.adc.accountId"]'),
					name: $('input[name="adcAdd.adc.accountId"]').parent().parent().find('li').text(),
					required: !(($('input[name="adcAdd.adc.type"]').val() == 'Alteon' || $('input[name="adcAdd.adc.type"]:checked').val() == 'Alteon' ) && $('#MODE_MONITORING').is(':checked')),
					type: "id",
					lengthRange: [1,255]
				},
				// 비밀번호
				{
					target: $('input[name="adcAdd.adc.password"]'),
					name: $('input[name="adcAdd.adc.password"]').parent().parent().parent().parent().parent().parent().find('li').text(),
					required:  !(($('input[name="adcAdd.adc.type"]').val() == 'Alteon' || $('input[name="adcAdd.adc.type"]:checked').val() == 'Alteon') && $('#MODE_MONITORING').is(':checked')),
					lengthRange: [1,127]
				},
				// CLI 아이디(F5)
				{
					target: $('input[name="adcAdd.adc.cliAccountId"]'),
					name: $('input[name="adcAdd.adc.cliAccountId"]').parent().parent().find('li').text(),
					checked: $('#F5Select').is(':checked'),
					required: true,
					type: "id",
					lengthRange: [1,255]
				},
				// CLI 비밀번호(F5)
				{
					target: $('input[name="adcAdd.adc.cliPassword"]'),
					name: $('input[name="adcAdd.adc.cliPassword"]').parent().parent().parent().parent().parent().parent().find('li').text(),
					checked: $('#F5Select').is(':checked'),
					required: true,
					lengthRange: [1,127]
				},
				// SNMP
				{
					target: $('input[name="adcAdd.adc.adcSnmpInfo.rcomm"]'),
					name: $('input[name="adcAdd.adc.adcSnmpInfo.rcomm"]').parent().parent().parent().parent().parent().parent().find('li').text(),
					checked: $('#v2').is(':checked'), // 추가: Alteon v3에서는 user와 password로 대체
					required: true,
//					type: "name",
					lengthRange: [1,255]
				},
				// 추가: user
				{
					target: $('input[name="adcAdd.adc.adcSnmpInfo.securityName"]'),
					name: $('input[name="adcAdd.adc.adcSnmpInfo.securityName"]').parent().parent().find('li').text(),
					checked: $('#v3').is(':checked'),
					required: true,
					type: "name",
					lengthRange: [1,255]
				},
				// 추가: authPassword
				{
					target: $('input[name="adcAdd.adc.adcSnmpInfo.authPassword"]'),
					name: $('input[name="adcAdd.adc.adcSnmpInfo.authPassword"]').parent().parent().find('li').text(),
					checked: $('#v3').is(':checked'),
					lengthRange: [8,127]
				},
				// 추가: privPassword
				{
					target: $('input[name="adcAdd.adc.adcSnmpInfo.privPassword"]'),
					name: $('input[name="adcAdd.adc.adcSnmpInfo.privPassword"]').parent().parent().parent().parent().parent().parent().find('li').text(),
					checked: $('#v3').is(':checked'),
					lengthRange: [8,127]
				},
				// SYSLOG IP
				{
					target: $('input[name="adcAdd.adc.syslogip"]'),
					name: $('input[name="adcAdd.adc.syslogip"]').parent().parent().parent().parent().parent().parent().find('li').text(),
					type: "ip"
				},
				// ADC이름
				{
					target: $('input[name="adcAdd.adc.name"]'),
					name: $('input[name="adcAdd.adc.name"]').parent().parent().find('li').text(),
					required: true,
					type: "name",
					lengthRange: [1,64]
				},
				// 그룹
				{
					target: $('select[name="adcAdd.adc.groupIndex"]'),
					name: $('select[name="adcAdd.adc.groupIndex"]').parent().parent().find('li').text(),
					required: true
				},
				// 구매날짜 TODO: 유효성 검사, 정말 필요 없나?
				// 설명
				{
					target: $('textarea[name="adcAdd.adc.description"]'),
					name: $('textarea[name="adcAdd.adc.description"]').parent().parent().find('li').text(),
					type: "name",
					lengthRange: [1,256]
				}
			]);
		
		if (result == false)
			return false;
		
		// 이하 연결여부 검사구문(수정본)
		var adcType = "";
		if (isModify == true)
		{
			adcType = $('input[name="adcAdd.adc.type"]').val();			
		}
		else
		{
			adcType = $('input[name="adcAdd.adc.type"]:checked').val();
		}
		
		if (adcType == "F5") 
		{ 			
			// 하나라도 비정상이면 
			if ( ($('.conn_statNetwork.off').length == 1) || ($('.conn_statSnmp.off').length == 2) || 
					($('.conn_statLogin_F5.off').length == 1) || ($('.conn_statSyslog.off').length == 1) )
			{
				this.isReachable = 2;
			}
			// 하나라도 연결테스트 진행하지 않은 경우
			else if ( ($('.conn_statNetwork.noConn').length == 1) && ($('.conn_statSnmp.noConn').length == 2) &&
				($('.conn_statLogin_F5.noConn').length == 1) && ($('.conn_statSyslog.noConn').length == 1) )
			{
				this.isReachable = 0;
				if(!confirm(VAR_ADCSETTING_CONNTESTNOT))
					return false;
			}	
			// 연결테스트 모두 정상인경우		
			else if ( ($('.conn_statNetwork.on').length == 1) && ($('.conn_statSnmp.on').length == 2) &&  
					($('.conn_statLogin_F5.on').length == 1) && ($('.conn_statSyslog.on').length == 1) )
			{
				this.isReachable = 1;
			}
			//네트워크 접근, 로그인, snmp 테스트가 정상인 경우 (모니터링 모드)
			else if(($('.conn_statNetwork.on').length == 1) && ($('.conn_statLogin_F5.on').length == 1) && ($('.conn_statSnmp.on').length == 2))
			{
				this.isReachable = 3;
			}
			else
			{
				this.isReachable = 0;
			}		
		}		
		else
		{			
			// 하나라도 비정상이면 
			if ( ($('.conn_statNetwork.off').length == 1) || ($('.conn_statSnmp.off').length == 2) || 
					($('.conn_statLogin.off').length == 1) || ($('.conn_statSyslog.off').length == 1) )
			{
				this.isReachable = 2;
			}
			// 하나라도 연결테스트 진행하지 않은 경우
			else if ( ($('.conn_statNetwork.noConn').length == 1) && ($('.conn_statSnmp.noConn').length == 2) &&
				($('.conn_statLogin.noConn').length == 1) && ($('.conn_statSyslog.noConn').length == 1) )
			{
				this.isReachable = 0;
				if(!confirm(VAR_ADCSETTING_CONNTESTNOT))
					return false;
			}	
			// 연결테스트 모두 정상인경우		
			else if ( ($('.conn_statNetwork.on').length == 1) && ($('.conn_statSnmp.on').length == 2) &&  
				($('.conn_statLogin.on').length == 1) && ($('.conn_statSyslog.on').length == 1) )
			{
				this.isReachable = 1;
			}
			//네트워크 접근, snmp 테스트가 정상인 경우 (모니터링 모드)
			else if(($('.conn_statNetwork.on').length == 1) && ($('.conn_statSnmp.on').length == 2))
			{
				this.isReachable = 3;
			}
			else
			{
				this.isReachable = 0;
			}
		}
		
		return true;
	},
//	validateAdcAdd : function(isModify) 
//	{
//		var adcType = "";
//		if (isModify == true)
//		{
//			adcType = $('input[name="adcAdd.adc.type"]').val();			
//		}
//		else
//		{
//			adcType = $('input[name="adcAdd.adc.type"]:checked').val();
//		}
//		
//		
//		if ($('input[name="adcAdd.adc.ip"]').val() == '') 
//		{
//			alert(VAR_COMMON_IPINPUT);
//			return false;
//		} 
//		
//		if (!getValidateIP($('input[name="adcAdd.adc.ip"]').val()))
//		{
//			alert(VAR_COMMON_IPFORMAT);
//			return false;
//		}
//		
//		if ($('input[name="adcAdd.adc.name"]').val() == '') 
//		{
//			alert(VAR_ADCSETTING_ADCNAMEINPUT);
//			return false;
//		}
//			
//				
//		if (!getValidateStringint($('input[name="adcAdd.adc.name"]').val(), 1, 64))
//		{
//			alert(VAR_COMMON_SPECIALCHAR);
//			return false;
//		}
//		// id
//		if (adcType == "F5")
//		{
//			if ($('input[name="adcAdd.adc.peerip"]').val() != '')
//			{
//				if (!getValidateIP($('input[name="adcAdd.adc.peerip"]').val()))
//				{
//					alert(VAR_COMMON_PEERIPFORMAT);
//					return false;
//				}
//			}
//			if (!getValidateF5ID($('input[name="adcAdd.adc.accountId"]').val(), 1, 255))
//			{
//				alert(VAR_F5_ID);
//				return false;
//			}
//			// password
//			if (!getValidateLength($('input[name="adcAdd.adc.password"]').val(), 1, 127))
//			{
//				alert(VAR_COMMON_LENGTHFORMAT);
//				return false;
//			}
//			if (!getValidateF5ID($('input[name="adcAdd.adc.cliAccountId"]').val(), 1, 255))
//			{
//				alert(VAR_ADCSETTING_CLILOGINIDINPUT);
//				return false;
//			}
//			// password
//			if (!getValidateLength($('input[name="adcAdd.adc.cliPassword"]').val(), 1, 127))
//			{
//				alert(VAR_ADCSETTING_CLIPWINPUT);
//				return false;
//			}
//			if (!getValidateF5Snmp($('input[name="adcAdd.adc..adcSnmpInfo.rcomm"]').val()))
//			{
//				alert(VAR_F5_SNMP);
//				return false;
//			}
//		}
//		else if (adcType == "Alteon")
//		{
//			if ($('input[name="adcAdd.adc.peerip"]').val() != '')
//			{
//				if (!getValidateIP($('input[name="adcAdd.adc.peerip"]').val()))
//				{
//					alert(VAR_COMMON_PEERIPFORMAT);
//					return false;
//				}
//			}
//			if (!getValidateLength($('input[name="adcAdd.adc.accountId"]').val(), 1, 8))
//			{
//				alert(VAR_COMMON_LENGTHFORMAT);
//				return false;
//			}
//			// password
//			if (!getValidateLength($('input[name="adcAdd.adc.password"]').val(), 1, 15))
//			{
//				alert(VAR_COMMON_LENGTHFORMAT);
//				return false;
//			}
//			
//			if ($('input[name="adcAdd.adc.adcSnmpInfo.version"]:checked').val()==2)
//			{
//				if (!getValidateLength($('input[name="adcAdd.adc.adcSnmpInfo.rcomm"]').val(), 1, 32))
//				{
//					alert(VAR_COMMON_LENGTHFORMAT);
//					return false;
//				}
//			}
//			else if ($('input[name="adcAdd.adc.adcSnmpInfo.version"]:checked').val()==3)
//			{
//				// snmp user
//				if (!getValidateLength($('input[name="adcAdd.adc.adcSnmpInfo.securityName"]').val(), 1, 32))
//				{
//					alert(VAR_COMMON_LENGTHFORMAT);
//					return false;
//				}
//				
//				if ($('input[name="adcAdd.adc.adcSnmpInfo.authPassword"]').val() != "") 
//				{		
//				
//					// snmp auth password
//					if (!getValidateLength($('input[name="adcAdd.adc.adcSnmpInfo.authPassword"]').val(), 8, 128))
//					{
//						alert(VAR_SYSSETTING_PASSWDRULEWRONG);
//						return false;
//					}
//				}
//				
//				if ($('input[name="adcAdd.adc.adcSnmpInfo.privPassword"]').val() != "") 
//				{				
//					// snmp priv password
//					if (!getValidateLength($('input[name="adcAdd.adc.adcSnmpInfo.privPassword"]').val(), 8, 128))
//					{
//						alert(VAR_SYSSETTING_PASSWDRULEWRONG);
//						return false;
//					}
//				}
//			}			
//		}
//		else
//		{
//			if (!getValidateLength($('input[name="adcAdd.adc.accountId"]').val(), 1, 128))
//			{
//				alert(VAR_SYSSETTING_IDRULEWRONG);
//				return false;
//			}
//			// password
//			if (!getValidateLength($('input[name="adcAdd.adc.password"]').val(), 1, 128))
//			{
//				alert(VAR_SYSSETTING_PASSWDRULEWRONG);
//				return false;
//			}
//		}
//		
//		// descriptions
//		if ($('.desc').val() != "")
//		{
//			if (!getValidateStringint($('.desc').val(), 1, 256))
//			{
//				alert('"' + "Description" + '" ' +VAR_COMMON_SPECIALCHAR);
//				return false;
//			}
//		}
//		
//		if($('#telnet').val() != null)
//		{
//			if (!getValidateNumberRange($('#telnet').val(), 1, 65535))
//			{
//				alert(VAR_ADCSETTING_PORTFORMAT);
//				return false;
//			}
//		}
//		if (!getValidateNumberRange($('#ssl').val(), 1, 65535))
//		{
//			alert(VAR_ADCSETTING_PORTFORMAT);
//			return false;
//		}
//
//		if ($('select[name="adcAdd.adc.groupIndex"] option').filter(':selected').length == 0) 
//		{
//			alert(VAR_ADCSETTING_ADCGROUPSEL);
//			return false;
//		}
//				
//		if (adcType == "F5") 
//		{ 			
//			// 하나라도 비정상이면 
//			if ( ($('.conn_statNetwork.off').length == 1) || ($('.conn_statSnmp.off').length == 1) || ($('.conn_statLogin_F5.off').length == 1) )
//			{
//				isReachable = 2;
//			}
//			// 하나라도 연결테스트 진행하지 않은 경우
//			else if ( ($('.conn_statNetwork.noConn').length == 1) && ($('.conn_statSnmp.noConn').length == 1) &&
//				($('.conn_statLogin_F5.noConn').length == 1) )
//			{
//				isReachable = 0;
//				if(!confirm(VAR_ADCSETTING_CONNTESTNOT))
//					return false;
//			}	
//			// 연결테스트 모두 정상인경우		
//			else if ( ($('.conn_statNetwork.on').length == 1) && ($('.conn_statSnmp.on').length == 1) &&  ($('.conn_statLogin_F5.on').length == 1) )
//			{
//				isReachable = 1;
//			} 	
//			else
//			{
//				isReachable = 0;
//			}		
//		}		
//		else
//		{			
//			// 하나라도 비정상이면 
//			if ( ($('.conn_statNetwork.off').length == 1) || ($('.conn_statSnmp.off').length == 1) || ($('.conn_statLogin.off').length == 1) )
//			{
//				isReachable = 2;
//			}
//			// 하나라도 연결테스트 진행하지 않은 경우
//			else if ( ($('.conn_statNetwork.noConn').length == 1) && ($('.conn_statSnmp.noConn').length == 1) &&
//				($('.conn_statLogin.noConn').length == 1) )
//			{
//				isReachable = 0;
//				if(!confirm(VAR_ADCSETTING_CONNTESTNOT))
//					return false;
//			}	
//			// 연결테스트 모두 정상인경우		
//			else if ( ($('.conn_statNetwork.on').length == 1) && ($('.conn_statSnmp.on').length == 1) &&  ($('.conn_statLogin.on').length == 1) )
//			{
//				isReachable = 1;
//			} 
//			else
//			{
//				isReachable = 0;
//			}
//		}
//
//		if ($('input[name="adcAdd.adc.syslogip"]').val() != "") 
//		{
//			if (!getValidateIP($('input[name="adcAdd.adc.syslogip"]').val())) 
//			{
//				alert(VAR_COMMON_IPFORMAT);
//				return false;
//			}
//		}
//		
//		return true;
//	},
//	// from GS. #4012-1 #1 :14.07.29 sw.jung GS로부터 이전
//	_calConnectionTestStatus : function(isModify) 
//	{// 2비트 단위로 데이터 저장함. gstest
//		var adcType = "";
//		if (isModify == true)
//		{
//			adcType = $('input[name="adcAdd.adc.type"]').val();			
//		}
//		else
//		{
//			adcType = $('input[name="adcAdd.adc.type"]:checked').val();
//		}
//		
//		var testStatus = 0;
//		var isEmptyTest = 0;
//		// 네트워크.
//		if ($('.conn_statNetwork.on').length == 1)
//		{// 성공인 경우.. noConn은 연결테스트 안한 경우.
//			testStatus += (1<<0);
//		}
//		else if ($('.conn_statNetwork.off').length == 1)
//		{// 실패인 경우.
//			testStatus += (1<<1);
//		}
//		else
//		{
//			isEmptyTest = 1;
//		}
//		if (adcType == "F5") 
//		{ 			
//			// 로그인 ....
//			if ($('.conn_statLogin_F5.on').length == 1)
//			{// 성공인 경우.. noConn은 연결테스트 안한 경우.
//				testStatus += (1<<2);
//			}
//			else if ($('.conn_statLogin_F5.off').length == 1)
//			{// 실패인 경우.
//				testStatus += (1<<3);
//			}
//			else
//			{
//				isEmptyTest = 1;
//			}
//		}		
//		else
//		{	
//			// 로그인 ....
//			if ($('.conn_statLogin.on').length == 1)
//			{// 성공인 경우.. noConn은 연결테스트 안한 경우.
//				testStatus += (1<<2);
//			}
//			else if ($('.conn_statLogin.off').length == 1)
//			{// 실패인 경우.
//				testStatus += (1<<3);
//			}
//			else
//			{
//				isEmptyTest = 1;
//			}
//		}
//
//		// snmp
//		if ($('.conn_statSnmp.on').length == 1)
//		{// 성공인 경우.. noConn은 연결테스트 안한 경우.
//			testStatus += (1<<4);
//		}
//		else if ($('.conn_statSnmp.off').length == 1)
//		{// 실패인 경우.
//			testStatus += (1<<5);
//		}
//		else
//		{
//			isEmptyTest = 1;
//		}
//		
//		// syslog
//		if ($('.conn_statSyslog.on').length == 1)
//		{// 성공인 경우.. noConn은 연결테스트 안한 경우.
//			testStatus += (1<<6);
//		}
//		else if ($('.conn_statSyslog.off').length == 1)
//		{// 실패인 경우.
//			testStatus += (1<<7);
//		}
//		else
//		{
//			isEmptyTest = 1;
//		}
//
//		return {"status": testStatus, "isNotTestItem": isEmptyTest};
//	},
	selectRegisteredAccountsForSubmit : function() 
	{
		$('#accountsSelectedSel > option').attr('selected', true);
	},
	testConnAdc : function(checkId, isModify)
	{	
		with (this) 
		{				
			if (!validateConnection(checkId, isModify))
			{
				return false;
			}
			
			if (isModify)
			{
				TestConnectionAdcType = $('input[name="adcAdd.adc.type"]').val();
			}
			else
			{
				TestConnectionAdcType = $('input[name="adcAdd.adc.type"]:checked').val();	
			}
				
			if (TestConnectionAdcType == "F5")
			{
				TestConnectionAdcConnService = "22";
				connPort = $('#ssl').val();
			}
			else 
			{
				if ($('input[name="adcAdd.adc.connService"]:checked').val() != null)
				{
					TestConnectionAdcConnService = $('input[name="adcAdd.adc.connService"]:checked').val();
					if(TestConnectionAdcConnService == '23')
					{//선택된 통신포트가 telnet 인 경우 
						connPort = $('#telnet').val();
					}
					else if(TestConnectionAdcConnService == '22')
					{//선택된 통신포트가 ssl 인 경우 
						connPort = $('#ssl').val();
					}
				}
				else if ($('input[name="adcAdd.adc.connService"]').val() !=null)
				{
					TestConnectionAdcConnService = $('input[name="adcAdd.adc.connService"]').val();
					if(TestConnectionAdcConnService == '23')
					{
						connPort = $('#telnet').val();
					}
					else if(TestConnectionAdcConnService == '22')
					{
						connPort = $('#ssl').val();
					}
				}
			}
						
			var snmpPrivProto = $('select[name="adcAdd.adc.adcSnmpInfo.authPassword"] :selected').val();
			if ($('input[name="adcAdd.adc.adcSnmpInfo.privPassword"]').val() != "")
			{
				snmpPrivProto = "des";
			}
			else
			{
				snmpPrivProto = "none";
			}
				
			//var snmpVersionValue = $('input[name="adcAdd.adc.snmpVersion"]:checked').val();
			//console.log("snmpVersionValue : " + snmpVersionValue);
			//console.log("snmpVersionValue.... : " + $('input[name="adcAdd.adc.snmpVersion"]:checked').val());
						
			ajaxManager.runJsonExt({
				url: "adcSetting/checkAdcConnection.action",
				data: 
				{
					"adcAdd.adc.index" : $('input[name="adcAdd.adc.index"]').val(),
					"adcAdd.adc.ip": $('input[name="adcAdd.adc.ip"]').val(),
					"adcAdd.adc.opMode": $('input[name="adcAdd.adc.opMode"]:checked').val(),
					"adcAdd.adc.accountId": $('input[name="adcAdd.adc.accountId"]').val(),
					"adcAdd.adc.password": $('input[name="adcAdd.adc.password"]').val(),
					"adcAdd.adc.cliAccountId": $('input[name="adcAdd.adc.cliAccountId"]').val(),
					"adcAdd.adc.cliPassword": $('input[name="adcAdd.adc.cliPassword"]').val(),
					"adcAdd.adc.type": TestConnectionAdcType,
					"adcAdd.adc.connService": TestConnectionAdcConnService,
					"adcAdd.adc.connPort": connPort,
					"adcAdd.adc.connProtocol" : $('select[name="adcAdd.adc.connProtocol"] :selected').val(),
					"adcAdd.adc.snmpCommunity": $('input[name="adcAdd.adc.snmpCommunity"]').val(),
					"adcAdd.adc.syslogip": $('input[name="adcAdd.adc.syslogip"]').val(),
					"checkId" : checkId,
//					"adcAdd.adc.snmpVersion" : snmpVersionValue,
					"adcAdd.adc.adcSnmpInfo.version" : $('input[name="adcAdd.adc.adcSnmpInfo.version"]:checked').val(),
					"adcAdd.adc.adcSnmpInfo.rcomm": $('input[name="adcAdd.adc.adcSnmpInfo.rcomm"]').val(),
					"adcAdd.adc.adcSnmpInfo.securityName" : $('input[name="adcAdd.adc.adcSnmpInfo.securityName"]').val(),
					"adcAdd.adc.adcSnmpInfo.authPassword" : $('input[name="adcAdd.adc.adcSnmpInfo.authPassword"]').val(),
					"adcAdd.adc.adcSnmpInfo.privPassword" : $('input[name="adcAdd.adc.adcSnmpInfo.privPassword"]').val(),
					"adcAdd.adc.adcSnmpInfo.authProto" : $('select[name="adcAdd.adc.adcSnmpInfo.authProto"] :selected').val(),
					"adcAdd.adc.adcSnmpInfo.privProto" : snmpPrivProto,
					"adcAdd.adc.mgmtMode" : $('input[name="adcAdd.adc.mgmtMode"]:checked').val(),
					"adcAdd.adc.ssoMode" : $('input[name="adcAdd.adc.ssoMode"]:checked').val()
				},
				successFn: function(data)
				{
					if (data.chkResult.checkID == 1) 
					{ 
						if (data.chkResult.status == 1) 
						{
							$('.conn_statNetwork').removeClass('off');
							$('.conn_statNetwork').removeClass('txt_connectRed');
							$('.conn_statNetwork').removeClass('noConn');
							$('.conn_statNetwork').addClass('on');
							$('.conn_statNetwork').addClass('txt_connectGreen');
							$('.conn_statNetwork').text(data.chkResult.summary);							
							//$('.conn_statNetwork').removeClass('off, txt_connectRed, noConn').addClass('on, txt_connectGreen').text(data.chkResult.summary);
							$('.networkConfirm').addClass("none");
							$('.networkOk').removeClass("none");
							$('.networkFail').addClass("none");
						}
						else if (data.chkResult.status == 2) 
						{
							$('.conn_statNetwork').removeClass('on');
							$('.conn_statNetwork').removeClass('txt_connectGreen');
							$('.conn_statNetwork').removeClass('noConn');
							$('.conn_statNetwork').addClass('off');
							$('.conn_statNetwork').addClass('txt_connectRed');
							$('.conn_statNetwork').text(data.chkResult.summary);							
							//$('.conn_statNetwork').removeClass('on, txt_connectGreen, noConn').addClass('off, txt_connectRed').text(data.chkResult.summary);
							$('.networkConfirm').addClass("none");
							$('.networkOk').addClass("none");
							$('.networkFail').removeClass("none");
						}
						else if (data.chkResult.status == 3) 
						{
							$('.conn_statNetwork').removeClass('on, off').addClass('noConn, txt_connectGray').text(data.chkResult.summary);
						}
					}				
					else if (data.chkResult.checkID == 2) 
					{ 
						var summaryClass; 
						var confirmClass;
						var okClass;
						var failClass;
						if (TestConnectionAdcType == "F5") 
						{
							summaryClass = $('.conn_statLogin_F5');
							confirmClass = $('.loginF5Confirm');
							okClass = $('.loginF5Ok');
							failClass = $('.loginF5Fail');							
						}
						else
						{
							summaryClass = $('.conn_statLogin');
							confirmClass = $('.loginConfirm');
							okClass = $('.loginOk');
							failClass = $('.loginFail');		
						}
						
						if (data.chkResult.status == 1) 
						{
							summaryClass.removeClass('off');
							summaryClass.removeClass('txt_connectRed');
							summaryClass.removeClass('noConn');
							summaryClass.addClass('on');
							summaryClass.addClass('txt_connectGreen');
							summaryClass.text(data.chkResult.summary);								
							//summaryClass.removeClass('off, txt_connectRed, noConn').addClass('on, txt_connectGreen').text(data.chkResult.summary);	
							confirmClass.addClass("none");
							okClass.removeClass("none");
							failClass.addClass("none");
						
							testConnAdc(3, isModify);
						}
						else if (data.chkResult.status == 2) 
						{
							summaryClass.removeClass('on');
							summaryClass.removeClass('txt_connectGreen');
							summaryClass.removeClass('noConn');
							summaryClass.addClass('off');
							summaryClass.addClass('txt_connectRed');
							summaryClass.text(data.chkResult.summary);	
							//summaryClass.removeClass('on, txt_connectGreen, noConn').addClass('off, txt_connectRed').text(data.chkResult.summary);
							confirmClass.addClass("none");
							okClass.addClass("none");
							failClass.removeClass("none");
						}
						else if (data.chkResult.status == 3) 
						{
							summaryClass.removeClass('on, off').addClass('noConn, txt_connectGray').text(data.chkResult.summary);
						}
						
						$('input[name="adcAdd.adc.version"]').val(data.chkResult.extraInfo);											
					}
					else if (data.chkResult.checkID == 3) 
					{
						if (data.chkResult.status == 1) 
						{
							$('.conn_statVersion').addClass('txt_connectGray').text(data.chkResult.extraInfo);
						}
						else if (data.chkResult.status == 2) 
						{
							$('.conn_statVersion').addClass('txt_connectRed').text(VAR_ADCSETTING_NOTSUPPORT);
						}
						else if (data.chkResult.status == 3) 
						{
							$('.conn_statVersion').addClass('txt_connectGray').text(VAR_ADCSETTING_NOTSUPPORT);
						}
						
						$('input[name="adcAdd.adc.version"]').val(data.chkResult.extraInfo);
					}
										
					else if (data.chkResult.checkID == 5) 
					{ 
						if (data.chkResult.status == 1) 
						{
							$('.conn_statSnmp').removeClass('off');
							$('.conn_statSnmp').removeClass('txt_connectRed');
							$('.conn_statSnmp').removeClass('noConn');
							$('.conn_statSnmp').addClass('on');
							$('.conn_statSnmp').addClass('txt_connectGreen');
							$('.conn_statSnmp').text(data.chkResult.summary);	
							//$('.conn_statSnmp').removeClass('off, txt_connectRed, noConn').addClass('on, txt_connectGreen').text(data.chkResult.summary);							
							$('.snmpConfirm').addClass("none");
							$('.snmpOk').removeClass("none");
							$('.snmpFail').addClass("none");
						}
						else if (data.chkResult.status == 2) 
						{
							$('.conn_statSnmp').removeClass('on');
							$('.conn_statSnmp').removeClass('txt_connectGreen');
							$('.conn_statSnmp').removeClass('noConn');
							$('.conn_statSnmp').addClass('off');
							$('.conn_statSnmp').addClass('txt_connectRed');
							$('.conn_statSnmp').text(data.chkResult.summary);		
							//$('.conn_statSnmp').removeClass('on, txt_connectGreen, noConn').addClass('off, txt_connectRed').text(data.chkResult.summary);
							$('.snmpConfirm').addClass("none");
							$('.snmpOk').addClass("none");
							$('.snmpFail').removeClass("none");
						}
						else if (data.chkResult.status == 3) 
						{
							$('.conn_statSnmp').removeClass('on, off').addClass('noConn, txt_connectGray').text(data.chkResult.summary);
						}						
					}
					else if (data.chkResult.checkID == 6) 
					{ 
					
						if (data.chkResult.status == 1) 
						{
							$('.conn_statSyslog').removeClass('off');
							$('.conn_statSyslog').removeClass('txt_connectRed');
							$('.conn_statSyslog').removeClass('noConn');
							$('.conn_statSyslog').addClass('on');
							$('.conn_statSyslog').addClass('txt_connectGreen');
							$('.conn_statSyslog').text(data.chkResult.summary);	
							//$('.conn_statSyslog').removeClass('off, txt_connectRed, noConn').addClass('on, txt_connectGreen').text(data.chkResult.summary);							
							$('.syslogConfirm').addClass("none");
							$('.syslogOk').removeClass("none");
							$('.syslogFail').addClass("none");
						}
						else if (data.chkResult.status == 2) 
						{
							$('.conn_statSyslog').removeClass('on');
							$('.conn_statSyslog').removeClass('txt_connectGreen');
							$('.conn_statSyslog').removeClass('noConn');
							$('.conn_statSyslog').addClass('off');
							$('.conn_statSyslog').addClass('txt_connectRed');
							$('.conn_statSyslog').text(data.chkResult.summary);	
							//$('.conn_statSyslog').removeClass('on, txt_connectGreen, noConn').addClass('off, txt_connectRed').text(data.chkResult.summary);
							$('.syslogConfirm').addClass("none");
							$('.syslogOk').addClass("none");
							$('.syslogFail').removeClass("none");
						}
						else if (data.chkResult.status == 3) 
						{
							$('.conn_statSyslog').removeClass('on, off').addClass('noConn, txt_connectGray').text(data.chkResult.summary);
						}	
					}
					else 
					{ 
						//$('.conn_statNetwork').removeClass('on').addClass('off').text(VAR_ADCSETTING_NOTSUPPORT);
					}
				},
				completeFn: function(textStatus)
				{
					FlowitUtil.log('testConnection: ' + textStatus);
				},
				errorFn: function(jqXhr)
				{
					$.obAlertAjaxError(VAR_ADCSETTING_CONNTESTFAIL, jqXhr);
				}
			});
		}		
	},
	
//	testConnection : function() 
//	{
//		with (this) 
//		{			
//			if (!validateTestConnection())
//			{
//				return false;
//			}
//			if ($('input[name="adcAdd.adc.type"]:checked').val() != null)
//			{
//				TestConnectionAdcType = $('input[name="adcAdd.adc.type"]:checked').val();
//			}
//			else if ($('input[name="adcAdd.adc.type"]').val() !=null)
//			{
//				TestConnectionAdcType = $('input[name="adcAdd.adc.type"]').val();
//			}
//			if ($('input[name="adcAdd.adc.connService"]:checked').val() != null)
//			{
//				TestConnectionAdcConnService = $('input[name="adcAdd.adc.connService"]:checked').val();
//				if(TestConnectionAdcConnService == '23')
//				{//선택된 통신포트가 telnet 인 경우 
//					connPort = $('#telnet').val();
//				}
//				else if(TestConnectionAdcConnService == '22')
//				{//선택된 통신포트가 ssl 인 경우 
//					connPort = $('#ssl').val();
//				}
//			}
//			else if ($('input[name="adcAdd.adc.connService"]').val() !=null)
//			{
//				TestConnectionAdcConnService = $('input[name="adcAdd.adc.connService"]').val();
//				if(TestConnectionAdcConnService == '23')
//				{
//					connPort = $('#telnet').val();
//				}
//				else if(TestConnectionAdcConnService == '22')
//				{
//					connPort = $('#ssl').val();
//				}
//			}
//			ajaxManager.runJsonExt({
//				url : "adcSetting/testConnection.action",
//				data : 
//				{						
//					"adcAdd.adc.ip" : $('input[name="adcAdd.adc.ip"]').val(),
//					"adcAdd.adc.accountId" : $('input[name="adcAdd.adc.accountId"]').val(),
//					"adcAdd.adc.password" : $('input[name="adcAdd.adc.password"]').val(),
//					"adcAdd.adc.cliAccountId" : $('input[name="adcAdd.adc.cliAccountId"]').val(),
//					"adcAdd.adc.cliPassword" : $('input[name="adcAdd.adc.cliPassword"]').val(),
////					"adcAdd.adc.type" : $('input[name="adcAdd.adc.type"]:checked').val(),
////					"adcAdd.adc.type" : testConnAdcType,
//					"adcAdd.adc.type" : TestConnectionAdcType,
//					"adcAdd.adc.connService" : TestConnectionAdcConnService,
//					"adcAdd.adc.connPort" :	connPort,
//	//				"adcAdd.adc.snmpCommunity" : $('select[name="adcAdd.adc.snmpCommunity"]').val()
//					"adcAdd.adc.snmpCommunity" : $('input[name="adcAdd.adc.snmpCommunity"]').val()
//				},
//				successFn : function(data) 
//				{
//					if (data.isReachable == 0) 
//					{	//alert("data.isReachable 0 : " + data.isReachable);
//						$('.conn_stat').removeClass('off').addClass('on').text(VAR_ADCSETTING_CONNTESTSUCCESS);
//					}
//					else if (data.isReachable == 1) 
//					{	//alert("data.isReachable 1 : " + data.isReachable);
//						$('.conn_stat').removeClass('on').addClass('off').text(VAR_ADCSETTING_NETWORKERROR);
//					}
//					else if (data.isReachable == 2) 
//					{	//alert("data.isReachable 2 : " + data.isReachable);
//						$('.conn_stat').removeClass('on').addClass('off').text(VAR_ADCSETTING_LOGINFAIL);
//					}
//					else if (data.isReachable == 3) 
//					{	//alert("data.isReachable 2 : " + data.isReachable);
//						$('.conn_stat').removeClass('on').addClass('off').text(VAR_ADCSETTING_SNMPERROR);
//					}
//					else if (data.isReachable == 5) 
//					{	//alert("data.isReachable 2 : " + data.isReachable);
//						$('.conn_stat').removeClass('on').addClass('off').text(VAR_ADCSETTING_CLILOGINFAIL);
//					}
//					else 
//					{	//alert("data.isReachable : 3 " + data.isReachable);
//						$('.conn_stat').removeClass('on').addClass('off').text(VAR_ADCSETTING_NOTSUPPORT);
//					}
//				},
//				completeFn : function(textStatus) 
//				{
//					FlowitUtil.log('testConnection: ' + textStatus);
//				},
//				errorFn : function(a,b,c)
//				{
//					alert(VAR_ADCSETTING_CONNTESTFAIL);
//				}				
//			});		
//		}
//	},
	validateConnection : function(checkId, isModify, version)
	{		
		var adcType = "";
		if (isModify == true)
		{
			adcType = $('input[name="adcAdd.adc.type"]').val();			
		}
		else
		{
			adcType = $('input[name="adcAdd.adc.type"]:checked').val();
		}
		
		if (checkId == 1)
		{
			if ($('input[name="adcAdd.adc.ip"]').val() == '') 
			{
				$.obAlertNotice(VAR_COMMON_IPINPUT);
				return false;
			} 
//			else if (!FlowitUtil.checkIp($('input[name="adcAdd.adc.ip"]').val())) 
//			{
//				$.obAlertNotice(VAR_COMMON_IPFORMAT);
//				return false;
//			} 
			if (!getValidateIP($('input[name="adcAdd.adc.ip"]').val()))
			{
				$.obAlertNotice(VAR_COMMON_IPFORMAT);
				return false;
			}
			
			if($('#telnet').val() != null)
			{
				if (!getValidateNumberRange($('#telnet').val(), 1, 65535))
				{
					$.obAlertNotice(VAR_ADCSETTING_PORTFORMAT);
					return false;
				}
			}
			if (!getValidateNumberRange($('#ssl').val(), 1, 65535))
			{
				$.obAlertNotice(VAR_ADCSETTING_PORTFORMAT);
				return false;
			}
		}
		else if (checkId == 2 || checkId == 3)
		{
			if (adcType == "F5") 
			{
				if ($('input[name="adcAdd.adc.peerip"]').val() != '') 
				{
					if (!getValidateIP($('input[name="adcAdd.adc.peerip"]').val())) 
					{
						$.obAlertNotice(VAR_COMMON_PEERIPFORMAT);
						return false;
					}
				}
				// #3926-4 #1: 14.07.21 sw.jung: obvalidation 활용 개선
//				if (!getValidateF5ID($('input[name="adcAdd.adc.accountId"]').val(), 1, 255)) 
//				{
//					alert(VAR_F5_ID);
//					return false;
//				}
				if (!$('input[name="adcAdd.adc.accountId"]').validate(
						{
							name: $('input[name="adcAdd.adc.accountId"]').parent().parent().find('li').text(),
							required: true,
							lengthRange: [1,255],
							regExp: /^[A-Za-z][a-zA-Z0-9._-]*?$/,
							msg: VAR_F5_ID 
						}))
				{
					return false;
				}
				// password
				if (!getValidateLength($('input[name="adcAdd.adc.password"]').val(), 1, 127)) 
				{
					$.obAlertNotice(VAR_COMMON_LENGTHFORMAT);
					return false;
				}
				
				// #3926-4 #3: 14.07.21 sw.jung: obvalidation 활용 개선
//				if (!getValidateF5ID($('input[name="adcAdd.adc.cliAccountId"]').val(), 1, 255)) 
//				{
//					alert(VAR_F5_ID);
//					return false;
//				}
				if (!$('input[name="adcAdd.adc.cliAccountId"]').validate(
						{
							name: $('input[name="adcAdd.adc.cliAccountId"]').parent().parent().find('li').text(),
							required: true,
							lengthRange: [1,255],
							regExp: /^[A-Za-z][a-zA-Z0-9._-]*?$/,
							msg: VAR_F5_ID 
						}))
				{
					return false;
				}
				// password
				if (!getValidateLength($('input[name="adcAdd.adc.cliPassword"]').val(), 1, 127)) 
				{
					$.obAlertNotice(VAR_ADCSETTING_CLIPWINPUT);
					return false;
				}
			}
			else if (adcType == "Alteon") 
			{
				if ($('input[name="adcAdd.adc.peerip"]').val() != '') 
				{
					if (!getValidateIP($('input[name="adcAdd.adc.peerip"]').val())) 
					{
						$.obAlertNotice(VAR_COMMON_PEERIPFORMAT);
						return false;
					}
				}
				if (!getValidateLength($('input[name="adcAdd.adc.accountId"]').val(), 1, 255)) 
				{
					$.obAlertNotice(VAR_COMMON_LENGTHFORMAT);
					return false;
				}
				// password
				if (!getValidateLength($('input[name="adcAdd.adc.password"]').val(), 1, 127)) 
				{
					$.obAlertNotice(VAR_COMMON_LENGTHFORMAT);
					return false;
				}
			}
			else
			{
				if (!getValidateLength($('input[name="adcAdd.adc.accountId"]').val(), 1, 127))
				{
					$.obAlertNotice(VAR_SYSSETTING_IDRULEWRONG);
					return false;
				}
				// password
				if (!getValidateLength($('input[name="adcAdd.adc.password"]').val(), 1, 127))
				{
					$.obAlertNotice(VAR_SYSSETTING_PASSWDRULEWRONG);
					return false;
				}
			}
			
//			if ($('input[name="adcAdd.adc.accountId"]').val() == '') 
//			{
//				if ($('input[name="adcAdd.adc.type"]:checked').val() == 'F5')
//				{	// F5일 경우만 아이디 검사
//					alert(VAR_ADCSETTING_LOGINIDINPUT);
//					return false;
//				}
//			} 
//			else if ($('input[name="adcAdd.adc.password"]').val() == '') 
//			{
//				if ($('input[name="adcAdd.adc.type"]:checked').val() == 'F5')
//				{ 	// F5일 경우만 비밀번호 검사
//					alert(VAR_ADCSETTING_PWINPUT);
//					return false;
//				}
//			}
//			
//			if ($('input[name="adcAdd.adc.cliAccountId"]').val() == '') 
//			{
//				if ($('input[name="adcAdd.adc.type"]:checked').val() == 'F5')
//				{	// F5일 경우만 아이디 검사
//					alert(VAR_ADCSETTING_CLILOGINIDINPUT);
//					return false;
//				}
//			} 
//			else if ($('input[name="adcAdd.adc.cliPassword"]').val() == '') 
//			{
//				if ($('input[name="adcAdd.adc.type"]:checked').val() == 'F5')
//				{ 	// F5일 경우만 비밀번호 검사
//					alert(VAR_ADCSETTING_CLIPWINPUT);
//					return false;
//				}
//			}
			
		}
		else if (checkId == 5)
		{
			if (adcType == "F5")
			{
				if (!getValidateF5Snmp($('input[name="adcAdd.adc.adcSnmpInfo.rcomm"]').val()))
				{
					$.obAlertNotice(VAR_F5_SNMP);
					return false;
				}
			}
			else if (adcType == "Alteon")
			{
				if ($('input[name="adcAdd.adc.adcSnmpInfo.version"]:checked').val()==2)
				{
					if (!getValidateLength($('input[name="adcAdd.adc.adcSnmpInfo.rcomm"]').val(), 1, 32))
					{
						$.obAlertNotice(VAR_COMMON_LENGTHFORMAT);
						return false;
					}
				}
				else if ($('input[name="adcAdd.adc.adcSnmpInfo.version"]:checked').val()==3)
				{
					// snmp user
					if (!getValidateLength($('input[name="adcAdd.adc.adcSnmpInfo.securityName"]').val(), 1, 32))
					{
						$.obAlertNotice(VAR_COMMON_LENGTHFORMAT);
						return false; 
					}
					
					// snmp auth password
					if ($('input[name="adcAdd.adc.adcSnmpInfo.authPassword"]').val() != "")
					{
						if (!getValidateLength($('input[name="adcAdd.adc.adcSnmpInfo.authPassword"]').val(), 8, 128))
						{
							$.obAlertNotice(VAR_SYSSETTING_PASSWD_RULEWRONG);
							return false;
						}
					}
					// snmp priv password
					if ($('input[name="adcAdd.adc.adcSnmpInfo.privPassword"]').val() != "")
					{
						if (!getValidateLength($('input[name="adcAdd.adc.adcSnmpInfo.privPassword"]').val(), 8, 128))
						{
							$.obAlertNotice(VAR_SYSSETTING_PASSWD_RULEWRONG);
							return false;
						}	
					}
				}
			}
				
//			if ($('input[name="adcAdd.adc.snmpCommunity"]').val() == '') 
//			{
//				alert(VAR_ADCSETTING_SNMPINPUT);
//				return false;
//			}
//						 
//			if (adcType = $('input[name="adcAdd.adc.type"]:checked').val() == "F5") 
//			{
//				if (!getValidateF5Snmp($('input[name="adcAdd.adc.snmpCommunity"]').val())) 
//				{
//					alert(VAR_F5_SNMP);
//					return false;
//				}				
//			}	
//			else
//			{
//				if (!getValidateLength($('input[name="adcAdd.adc.snmpCommunity"]').val(), 1, 32))
//				{
//					alert(VAR_COMMON_LENGTHFORMAT);
//					return false;
//				}			
//			} 
		}
		else if (checkId == 6)
		{
			if ($('input[name="adcAdd.adc.ipCp"]').val() == "" && $('input[name="adcAdd.adc.syslogip"]').val() == "")
			{
				$.obAlertNotice(VAR_COMMON_IPINPUT);
				return false;
			}
			
			if ($('input[name="adcAdd.adc.syslogip"]').val() != "")
			{
				if (!getValidateIP($('input[name="adcAdd.adc.syslogip"]').val())) 
				{
					$.obAlertNotice(VAR_COMMON_IPFORMAT);
					return false;
				}
//				if (!FlowitUtil.checkIp($('input[name="adcAdd.adc.syslogip"]').val())) 
//				{
//					alert(VAR_COMMON_IPFORMAT);
//					return false;
//				}
			}
		}
		
		return true;
	},
//	validateTestConnection : function() 
//	{
//		if ($('input[name="adcAdd.adc.ip"]').val() == '') 
//		{
//			alert(VAR_COMMON_IPINPUT);
//			return false;
//		} 
//		else if (!FlowitUtil.checkIp($('input[name="adcAdd.adc.ip"]').val())) 
//		{
//			alert(VAR_COMMON_IPFORMAT);
//			return false;
//		} 
//		else if ($('input[name="adcAdd.adc.accountId"]').val() == '') 
//		{
//			if ($('input[name="adcAdd.adc.type"]:checked').val() == 'F5')
//			{	// F5일 경우만 아이디 검사
//				alert(VAR_ADCSETTING_LOGINIDINPUT);
//				return false;
//			}
//			else
//			{
//				return true;
//			}
//		} 
//		else if ($('input[name="adcAdd.adc.password"]').val() == '') 
//		{
//			if ($('input[name="adcAdd.adc.type"]:checked').val() == 'F5')
//			{ 	// F5일 경우만 비밀번호 검사
//				alert(VAR_ADCSETTING_PWINPUT);
//				return false;
//			}
//			else
//			{
//				return true;
//			}
//		}
//		
//		return true;
//	},
	popUpAdcGroupMgmt : function(opt) 
	{
		with (this) 
		{
			showPopup({
				'id' : '#groupMgmtWnd',
				'width' : '510px',
				'height' : '340px'
			});
			
			var $pop = $('#groupMgmtWnd').clone();
//			var $pop = null;
//			//팝업창 열기
//			$pop = $('#groupMgmtWnd').clone();
//			$pop.addClass('cloneDiv');
//			$('body').append("<div class='popup_type1'></div>");
//			$('body').append($pop);
//			$pop.show();
//			
//			alert("window width : " + $(window).width());			//1167
//			alert("window height : " + $(window).height());			//666
//			alert("$pop width : " + $pop.width());					//494
//			alert("$pop height : " + $pop.height());				//208
//			
//			// 팝업창 정중앙 위치
//			$pop.css({
//				'left':($(window).width()-$pop.width())/2,
//				'top':($(window).height()-$pop.height())/2
//			});
			
			//팝업창 닫기
//			$pop.find('.closeLnk').click(function(e)
			$('.cloneDiv .closeLnk').click(function(e) 							
			{
				e.preventDefault();
				closeGroupMgmt(opt);
			});
	
			// 테이블 컬럼 정렬
			initTable([".table_type1 tbody tr"],[1,2],[-1]);
			
			loadGroupsToAdcGroupMgmt();
			registerAdcGroupMgmtEvents();
			$pop.find('input[name="adcGroup.name"]').focus();
//			$('#groupMgmtWnd').find('input[name="adcGroup.name"]').focus();
		}
	},
	loadGroupsToAdcGroupMgmt : function() 
	{
		with (this) 
		{
			FlowitUtil.log('-- loadGroupsToAdcGroupMgmt');
			ajaxManager.runJsonExt({
				url : "adcSetting/retrieveAdcGroups.action",
				successFn : function(data) 
				{
					FlowitUtil.log('retrieveAdcGroups: ' + Object.toJSON(data.adcGroups));
					fillAdcGroupsInGroupMgmt(data.adcGroups);
				},
				completeFn : function(textStatus) 
				{
					FlowitUtil.log('retrieveAdcGroups: ' + textStatus);
				},
				errorFn : function(a,b,c)
				{
					exceptionEvent();
				}	
			});
		}
	},
	registerAdcGroupMgmtEvents : function() 
	{
		with (this) 
		{
			$('.cloneDiv .allAdcGroupsChk').filter(':last').click(function(e) 
			{
				var isChecked = $(this).is(':checked');
				$('.cloneDiv input[name="adcGroupChk"]').attr('checked', isChecked);
			});
				
			$('.cloneDiv .addAdcGroupLnk').click(function(e) 
			{
				e.preventDefault();
				if (!validateAdcGroupAdd())
					return;
				
				addAdcGroup();
			});
			
			$('.cloneDiv .delAdcGroupsLnk').click(function(e) 
			{
				with (this) 
				{
					e.preventDefault();		
					
					var adcGroupIndices =$('.cloneDiv input[name="adcGroupChk"]').filter(':checked').map(function() {
						return $(this).val();
					}).get();
					FlowitUtil.log('delAdcGroups: ' + Object.toJSON(adcGroupIndices));
					if (adcGroupIndices.length == 0) 
					{
						$.obAlertNotice(VAR_ADCSETTING_ADCGROUPDETSEL);
						return;
					}
									
					var chk = confirm(VAR_ADCSETTING_ADCGROUPDEL);
					if (chk) 
					{
						delAdcGroups(adcGroupIndices);
					}
					else 
					{
						return false;
					}				
		//			delAdcGroups();
				}
			});
		}
	},
	validateAdcGroupAdd : function() 
	{
		if ($('.cloneDiv input[name="adcGroup.name"]').filter(':last').val() == '') 
		{
			$.obAlertNotice(VAR_ADCSETTING_GROUPNAMEINPUT);
			return false;
		} 
		
		if ($('.cloneDiv input[name="adcGroup.description"]').filter(':last').val() == '') 
		{
//			alert('설명을 입력하세요.');
//			return false;
		}
		
		if ($('.cloneDiv input[name="adcGroup.name"]').val() != "")
		{
			if (!getValidateStringint($('.cloneDiv input[name="adcGroup.name"]').val(), 1, 64))
			{
				$.obAlertNotice(VAR_COMMON_SPECIALCHAR);
				return false;
			}
		}
		
		if ($('.cloneDiv input[name="adcGroup.description"]').val() != "")
		{
			if (!getValidateStringint($('.cloneDiv input[name="adcGroup.description"]').val(), 1, 64))
			{
				$.obAlertNotice(VAR_COMMON_SPECIALCHAR);
				return false;
			}
		}
		
		return true;
	},
	addAdcGroup : function() 
	{
		with (this) 
		{
			var adcGroup = {};
			adcGroup.name = $('.cloneDiv input[name="adcGroup.name"]:last').val();
			adcGroup.description = $('.cloneDiv input[name="adcGroup.description"]:last').val();
			FlowitUtil.log('adcGroup: ' + Object.toJSON(adcGroup));
			ajaxManager.runJsonExt({
				url : "adcSetting/addAdcGroup.action",
				data : 
				{
					"adcGroup.name" :  adcGroup.name,
					"adcGroup.description" : adcGroup.description
				},
				successFn : function(data) 
				{
					FlowitUtil.log('--addAdcGroup: ' + Object.toJSON(data.adcGroups));
					if (!data.isSuccessful)
						$.obAlertNotice(data.message);
					
					clearAdcGroupAddFields();
					fillAdcGroupsInGroupMgmt(data.adcGroups);
				},
				completeFn: function(textStatus) 
				{
					FlowitUtil.log('addAdcGroup: ' + textStatus);
				},
				errorFn : function(a,b,c)
				{
					exceptionEvent();
				}	
			});
		}
	},
	clearAdcGroupAddFields : function() 
	{
		$('.cloneDiv input[name="adcGroup.name"]').filter(':last').val('');
		$('.cloneDiv input[name="adcGroup.description"]').filter(':last').val('');
		$('.cloneDiv .allAdcGroupsChk').filter(':last').attr('checked', false);
	},
	fillAdcGroupsInGroupMgmt : function(adcGroups) 
	{
		if (adcGroups == null)
		{
			return;
		}
		var $tbody = $('.cloneDiv .adcGroupTbd').filter(':last');
		$tbody.empty();
		var html = ''; 

		for (var i=0; i < adcGroups.length; i++) 
		{
			html += '<tr>';
			html += '<td class="align_center"><input name="adcGroupChk" type="checkbox" value="' + adcGroups[i].index + '"/></td>';
			html += '<td class="textOver align_left_P10">' + adcGroups[i].name + '</td>';
			html += '<td class="align_left_P10">' + adcGroups[i].description + '</td>';
			html += '</tr>';
		}
		$tbody.html(html);
	},
	delAdcGroups : function(adcGroupIndices) 
	{
		with (this) 
		{			
			ajaxManager.runJsonExt({
				url : "adcSetting/delAdcGroups.action",
				data : 
				{
					"adcGroupIndices" :  adcGroupIndices
				},
				successFn : function(data) 
				{
					FlowitUtil.log('addAdcGroup: ' + Object.toJSON(data.adcGroups));
					if (!data.isSuccessful)
						$.obAlertNotice(data.message);
					
					fillAdcGroupsInGroupMgmt(data.adcGroups);
				},
				completeFn: function(textStatus) 
				{
					FlowitUtil.log('addAdcGroup: ' + textStatus);
				},
				errorFn : function(jqXhr)
				{
					// #3984-6 #7: 14.08.08 sw.jung 기 사용중인 ADC 그룹 삭제에 대한 에러메세지 적용
					if (/has .+ ADCs/.test(jqXhr.responseText))
						$.obAlertAjaxError(VAR_ADCSETTING_ADCGROUPUSED, jqXhr);
					else
						$.obAlertAjaxError(VAR_ADCSETTING_ADCGROUPDELFAIL, jqXhr);
				}				
			});
		}
	},
	_registerAdcNameAutoCompleteEvents : function() 
	{
		with (this) 
		{
			ajaxManager.runJsonExt({
				url : "adcSetting/loadAdcNameList.action",
				successFn : function(data) 
				{
					$('#idAdcName').autocomplete
					 ({
						 source: data.adcNameList
					 });
				},
				completeFn: function(textStatus) 
				{
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_ADCSETTING_ADCINFOEXTRACT, jqXhr);
				}				
			});
		}
	},
//	closeGroupMgmt : function($pop)
	closeGroupMgmt : function(opt)
	{
		with (this) 
		{
			var adcGroups = $('.cloneDiv .adcGroupTbd:last tr').map(function() {
				var adcGroup = {};
				adcGroup.index = $(this).find('td:first > input').val();
				adcGroup.name = $(this).children('td').eq(1).text();
				adcGroup.description = $(this).children('td').eq(2).text();
				return adcGroup;
			}).get();
			
			fillGroupsInAdcAddContent(adcGroups);
			$('.popup_type1').remove();
			$('.cloneDiv').remove();
//			$pop.remove();
		}
	},
	fillGroupsInAdcAddContent : function(adcGroups) 
	{
		var selectGroupIndex = $('.group_list').val();
		var $select = $('.group_list');
		$select.empty();
		var html = '';
		for (var i=0; i < adcGroups.length; i++)
			html += '<option value="' + adcGroups[i].index + '"' + (adcGroups[i].index == selectGroupIndex ? 'selected="selected"' : '') + '>' + adcGroups[i].name + '</option>';
		$select.html(html);
	},
	moveAccountsToSelection : function() 
	{
		with (this) 
		{
			var $accountsSelected = $('#accountsSelectedSel');
			var $accountsDeselected = $('#accountsDeselectedSel');
			
			var $option = $accountsDeselected.children(':selected');
			FlowitUtil.log($option.size());
			if ($option.size() > 0)
				$accountsSelected.append($option);
			
			showSelectedAccountsCount();
		}
	},
	moveAccountsToDeselection : function() 
	{
		with (this) 
		{
			var $accountsSelected = $('#accountsSelectedSel');
			var $accountsDeselected = $('#accountsDeselectedSel');
			
			var $option = $accountsSelected.children(':selected');
			FlowitUtil.log($option.size());
			if ($option.size() > 0)
				$accountsDeselected.append($option);
			
			showSelectedAccountsCount();
		}
	},
	showSelectedAccountsCount : function() 
	{
//		$('.selectedAccountsCount').text($('#accountsSelectedSel').children().length + '개 선택됨');
		$('.selectedAccountsCount').text($('#accountsSelectedSel').children().length);
	},
	_searchOnUnassignedAccounts : function(searchKey) 
	{
		with (this) 
		{
			var $accountsDeselectedSel = $('#accountsDeselectedSel');

			if (searchKey != "")
			{
				if (!getValidateStringint(searchKey, 1, 64))
				{
					$.obAlertNotice(VAR_COMMON_SPECIALCHAR);
					return false;
				}
			}
			
			if (!searchKey) 
			{
				$accountsDeselectedSel.append($unassignedAccountOptions);
				$unassignedAccountOptions = $();
				return ;
			}
			
			_fillCbxWithSearchedAndSaveUnsearched(searchKey, $accountsDeselectedSel);
		}
	},
	_fillCbxWithSearchedAndSaveUnsearched : function(searchKey, $accountsDeselectedSel) 
	{
		with (this) 
		{
			$unassignedAccountOptions = $unassignedAccountOptions.add($accountsDeselectedSel.children().detach());
			var keyInLowerCase = searchKey.toLowerCase();
			FlowitUtil.log('keyInLowerCase: ' + keyInLowerCase);
			var $unsearchedOptions = $();
			$unassignedAccountOptions.each(function() 
			{
				var index = $(this).text().toLowerCase().indexOf(keyInLowerCase);
				FlowitUtil.log('index: ' + index);
				if (index == -1)
				{
					$unsearchedOptions = $unsearchedOptions.add($(this));
				}
				else
				{
					$accountsDeselectedSel.append($(this));
				}
			});
			
			$unassignedAccountOptions = $unsearchedOptions;
		}
	},
	monitoringModeHideSet : function()
	{
		with(this)
		{
			if($('input[name="adcAdd.adc.opMode"]:checked').val() == 1)
			{
				if($('input[name="adcAdd.adc.type"]').val() == 'Alteon')
				{
					$('.loginField').removeClass('Lth2');
					$('.loginField').addClass('Lth1');
				}
			}
			else
			{
				$('.loginField').removeClass('Lth1');
				$('.loginField').addClass('Lth2');
			}
		}
	},
	loadAdcModifyContent : function(adcIndex, adcType, opMode) 
	{
		with (this) 
		{
			if(header.getAdcSettingTap() == 1)
			{
				if (adcType == "Alteon" || adcType == "F5")
				{
					configCheck.loadContent(adcIndex, adcType, opMode);
					return;
				}
			}
			ajaxManager.runHtmlExt({
				url : "adcSetting/loadAdcModifyContent.action",
				data: 
				{
					"adcAdd.adc.index" : adcIndex
				},
				target: "#wrap .contents",
				successFn : function(params) 
				{
					monitoringModeHideSet();
					setActiveContent('AdcModifyContent');
					$unassignedAccountOptions = $();
					displayVrrp($('input[name="adcAdd.adc.type"]').val());
					registerAdcAddContentEvents(true);
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_ADCSETTING_ADCINFOEXTRACT, jqXhr);
				}
			});
		}
	}
});
