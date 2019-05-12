var ClassMenu2ndAdcList = Class.create({
	initialize : function() 
	{
		this.isInitialized = false;
		this.configCheck = new ConfigCheck();
		this.selectedADC = 
		{
			index : undefined,
			name : undefined,
			type : undefined,//1: f5, 2: alteon, ....
			categoryIndex : undefined, // 2: ADC, 1: group, 0: all
			groupindex : undefined,
			groupname : undefined,
			adcStatus : undefined, 
//			adcversion : undefined, 
			adcModel : undefined
		};
		this.mainMenuName = undefined;
		this.subMenuName = undefined;
		this.activeContent = undefined;
		this.objOnAdcChange = undefined;
		this.$unassignedAccountOptions = $();
		this.searchPurchaseDate = undefined;
//		this.initials = new Initials();
		this.selectedType = 'all';
		this.orderDir  = 1; //1는 오름차순
		this.orderType = 34;//34는 ADC이름
		this.selectedAdcIndex = undefined;
		this.openGroupNames = undefined;
//		this.status = 1;
//		this.isFlb = undefined; //0 dis, 1 ena
	},
	_selectFirstAdc : function() 
	{
		with(this)
		{
			var $selectItem = $('.snb .snb_tree .depth2 > li').first().find('p');
			if($selectItem.attr('class') != 'on') 
			{
				$selectItem.addClass('on');
				_setAdcToClickedOne($selectItem);
				_openGroup($selectItem);
			}
		}
	},
	_openGroup : function($adc) 
	{
		with(this)
		{
			var $group = $adc.parents('.depth2').prev();
			if ($group.next().css('display') === 'none')
				$group.children('a').click();
		}
	},
	_selectAllGroups : function()
	{
		with(this)
		{
			var $selectGroup = $('.snb .snb_tree > li > p').first().find('a');
			if($selectGroup.attr('class') != 'on') 
			{
				$selectGroup.addClass('on');
				_setAdcToClickedOne($selectGroup);
			}
		}
	},
	_openAllGroups : function() 
	{
		with(this)
		{		
			$('.snb .snb_tree .depth2').css('display', 'block');
		}
	},
	_openGroupsWithNames : function(openGroupNames) 
	{
		with(this)
		{
			var $openGroups = $('.snb .snb_tree .depth1 > li > p > span').filter(function() {
				var groupName = $(this).text().substring(0, $(this).text().indexOf('('));
				for (var i=0; i < openGroupNames.length; i++) 
				{
					if (openGroupNames[i] === groupName)
						return true;
				}
					
				return false;
			}).parent();
			
			$openGroups.next().css('display', 'block');
		}
	},
	selectAdc : function(adcIndex) 
	{
		with(this)
		{
			if(adcIndex === undefined)
				return;
			
			var $selectItem = $('.snb .snb_tree .depth2 .adcIndex').filter(function(){
				return $(this).text() === adcIndex;
			}).first().parent();
			
			if ($selectItem.length === 0)
				return;
			
			if(!$selectItem.hasClass('on')) 
			{
				_deselectAll();
				$selectItem.addClass('on');
				_setAdcToClickedOne($selectItem);
				_openGroup($selectItem);
			}
		}
	},
	_getActiveAdcInfo : function() 
	{
		return this.selectedADC;
	},
	_setActiveAdcInfo : function(adc) 
	{
		this.selectedADC = adc;
	},
	_setMenuName : function(mainMenu, subMenu)
	{
		with(this)
		{
			this.mainMenuName = mainMenu;
			this.subMenuName = subMenu;
		}		
	},
	_getSelectedAdc : function() 
	{
		with(this)
		{
			return $('.snb .snb_tree .depth2 > li > p.on');
		}
	},
	_closeAllGroups : function() 
	{
		with(this)
		{
			$('.snb .snb_tree .depth2').css('display', 'none');
		}
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
	/// 선택된 ADC를 clear한 상태로 reload한다.
	reloadWithoutADCSelection : function() 
	{
		with(this)
		{
			_clearSelectionOnAllNodes();
			_loadADCList();
		}
	},
	loadContent : function(mainMenu, subMenu)
	{
		with(this)
		{
			_setMenuName(mainMenu, subMenu);
			
			_loadAdcSearchBar();
			_loadPickView();
			_loadADCList();
		}		
	},
	_loadAdcSearchBar : function()
	{
		with(this)
		{
			ajaxManager.runHtml({
				url : "layout/loadAdcSearchBar.action",
				target : "#wrap .adcSearch",
				successFn : function(params)
				{	
					_registerSearchBarEvents();
				}
			});
		}		
	},
	_registerSearchBarEvents : function() 
	{
		with (this) 
		{
			$('.adc_search .btn a.searchLnk').click(function (e) 
			{
				e.preventDefault();
				var searchKey = $('input[name="searchTxt"]').val();
				
				FlowitUtil.log('click:' + searchKey);
				_loadADCList(searchKey, undefined, true);
			});
			
			$('.adc_search .inputTextposition input.searchTxt').keydown(function(e) 
			{				
				if (e.which != 13)				
					return;// 13 means enter key..
						
				var searchKey = $(this).val(); 
				FlowitUtil.log('click:' + searchKey);
				_loadADCList(searchKey, undefined, true);
			});	
		}
	},	
	_loadPickView : function()
	{	
		with(this)
		{
			ajaxManager.runHtml({
				url : "layout/loadPickView.action",
				target : "#wrap .pickView",
				successFn : function(params)
				{
					registerPickView();
				}
			});
		}			
	},
	registerPickView : function()
	{
		with(this)
		{
			var adcTypeLogoCss = '';
			if (this.selectedADC == null || this.selectedADC.type == undefined)
			{
				return;
			}
			if (this.selectedADC.type == "Alteon")
			{
				adcTypeLogoCss = "adclogoAlteon";
			}
			else if (this.selectedADC.type == "F5")
			{
				adcTypeLogoCss = "adclogoF5";
			}
			else if (this.selectedADC.type == "PAS")
			{
				adcTypeLogoCss = "adclogoPas";
			}
			else if (this.selectedADC.type == "PASK")
			{
				adcTypeLogoCss = "adclogoPask";
			}
			
			var $pickdiv = $('.pick').filter(':last');
			$pickdiv.empty();
			var html = ''; 		
			
			html += '<span class="' + adcTypeLogoCss + '"></span>';
			html +=	'<span class="adcname">' + selectedADC.name+'</span>';
			
			$pickdiv.append(html);
		}
	},
	_loadADCList : function(searchKey, taskQ, forcesUpdate, orderType, orderDir) 
	{
		with (this) 
		{
			_saveOpengroupNSelectedADCIndex();
//			if (!isInitialized || forcesUpdate) 
			{
				ajaxManager.runHtmlExt({
					url : "adcSetting/loadLeftPane.action",
					data : 
					{
						"searchKey" : searchKey,
						"orderType" : orderType,
						"orderDir" : orderDir
					},
					target: "#wrap .listcontainer",
					successFn : function(params) 
					{
						_applyLeftCss();
						_registerLeftEvents();
						selectAdc(selectedADC.index);
						_loadMainContent();
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
	_loadMainContent : function() 
	{// main content 를 로딩한다.
		with(this)
		{
			if(mainMenuName===undefined || mainMenuName==='')
				return;
			
			if(mainMenuName==='monitorMnu')
			{// 모니터링 메뉴. 
				if(subMenuName===undefined || subMenuName==='')
					return;
				if(subMenuName==='monitorApplianceMnu')
				{// ADC 장비 선택시.
					var monitorAppliance = new MonitorAppliance();
					monitorAppliance.loadContent(_getActiveAdcInfo());
				}
				else if(subMenuName==='monitorServicePerfomanceMnu')
				{// 서비스 성능 선택시.
					var monitorServicePerfomance = new MonitorServicePerfomance();
					monitorServicePerfomance.loadServicePerfomanceContent();
				}
				else if(subMenuName==='monitorNetworkMnu')
				{// 서비스 요약 선택시.
					var networkMap = new NetworkMap();
					networkMap.loadNetworkMapContent(_getActiveAdcInfo(), 0, undefined, undefined, taskQ);
				}
				else if(subMenuName==='monitorStatisticsMnu')
				{// 인터페이스 선택시.
					var statistics = new Statistics();
					statistics.loadStatisticsContent(_getActiveAdcInfo());
				}
				else if(subMenuName==='flbInfoMnu')
				{// FLB 선택시.
					var flbInfo = new FlbInfo();
					flbInfo.loadListContent();
				}
				else if(subMenuName==='monitorFaultMnu')
				{// 장애 선택시.
					var alertMonitoring = new AlertMonitoring();
					alertMonitoring.loadListContent();
				}		
				else if(subMenuName==='adcLogMnu')
				{// ADC 로그 선택시.
					var adcLog = new AdcLog();
					adcLog.loadListContent();
				}					
			}
			else if(mainMenuName==='faultMgmtMnu')
			{// 분석 메뉴  선택시.
				if(subMenuName===undefined || subMenuName==='')
					return;
				if(subMenuName==='faultHistoryMnu')
				{// 진단  선택시.
					var faultHistory = new FaultHistory();
					faultHistory.loadListContent();
				}
				else if(subMenuName==='faultAnalysisMnu')
				{// 패킷 분석 선택시.
					var faultAnalysis = new FaultAnalysis();
					faultAnalysis.loadListContent();
				}	
				else if(subMenuName==='monitorL2Mnu')
				{// L2 검색  선택시.
					var monitorL2Info = new MonitorL2Info();
					monitorL2Info.loadL2SearchContent();
				}
				else if(subMenuName==='sessionMonitoringMnu')
				{// 세션 검색  선택시.
					var sessionMonitoring = new SessionMonitoring();
					sessionMonitoring.loadL2SearchContent();
				}
			}
			else if(mainMenuName==='slbMgmtMnu')
			{// 설정  메뉴  선택시.
				if(subMenuName===undefined || subMenuName==='')
					return;
				if(subMenuName==='adcSettingMnu')
				{// ADC 설정 선택시.
					var adcSetting = new ClassAdcSetting(this);
					adcSetting.loadContent(_getActiveAdcInfo());
				}
				else if(subMenuName==='slbSettingMnu')
				{// SLB 설정 선택시.
					var virtualServer = new ClassVirtualServer();
					virtualServer.loadListContent(_getActiveAdcInfo(), false);
				}	
				else if(subMenuName==='slbHistoryMnu')
				{// 설정 이력  선택시.
					var adcHistory = new ClassAdcHistory();
					adcHistory.loadListContent(_getActiveAdcInfo());
				}
				else if(subMenuName==='adcAlertMnu')
				{// 경보 설정 선택시.
					var adcAlert = new AdcAlert();
					adcAlert.loadAdcAlertListContent();
				}
			}
			else if(mainMenuName==='reportMnu')
			{// 보고서  메뉴  선택시.
				var report = new ClassReport();
				report.loadContent(_getActiveAdcInfo());	
				subMenuName='';
			}
			else if(mainMenuName==='sysSettingMnu')
			{// 시스템 관리  선택시.
				var sysSetting = new SysSetting();
				sysSetting.loadLeftPane();
				sysSetting.loadUserListContent();
				subMenuName='';
			}
		}
	},
	_applyLeftCssAllGroup : function() 
	{
		with(this)
		{
			var $allBlock = $('.snb_tree .allBlock');
			if ($allBlock.hasClass('on'))
				return;
				
			$allBlock.addClass('on');
		}
	},
	_applyLeftCssEachADC : function() 
	{
		with(this)
		{
			$('.snb .adcGroup').each(function()
			{
				$(this).removeClass('on');
			});
							
			$(this).addClass('on');
		}
	},
	_getSelectedAdcBlock : function() 
	{
		with(this)
		{
			var $adcBlock = $('.snb_tree .adcBlock');
			for (var i=0; i < $adcBlock.length; i++) 
			{
				var index = $adcBlock.eq(i).find('.adcIndex').text();
				if (index == selectedADC.index)
					return $adcBlock.eq(i);
			}
			
			return null;
		}
	},
	_applyLeftCss : function() 
	{
		with (this)
		{
			if (this.selectedType == 'all') 
			{
				_applyLeftCssAllGroup();
			} 
			else if (selectedType == 'adc') 
			{
				var $adcBlock = _getSelectedAdcBlock();
				if (!$adcBlock) 
				{
					selectedType = 'all';
					_applyLeftCssAllGroup();
				} 
				else 
				{
					$adcBlock.addClass('on');
					var $groupMenu = $adcBlock.parents('.depth2');
					$groupMenu.css('display', 'block');
				}
			}
			
			_closeAllGroups();
			_openGroupsWithNames(this.openGroupNames);
			if (this.selectedAdcIndex)
			{
				selectAdc(this.selectedAdcIndex);
			}
			
			$('.snb .snb_tree .depth2 .adcName').parent().width(165);		// 183px is experimental #.
			$(window).trigger('resize');
		}
	},
	_registerLeftEvents : function() 
	{
		with (this) 
		{
			$('.snb .snb_tree .adcListContentLnk').click(function(e) 
			{
				e.preventDefault();
				_loadMainContent();
			});
			
			// 각 Group 클릭시 발생하는 클릭 이벤트 함수
			$(".allGroups").click(function(e) 
			{			
				e.preventDefault();	
				_clearSelectionOnAllNodes();
				var $sub_menu = $(this).parent().nextAll('ul');		
				if($sub_menu.css('display') == 'none') 
				{					
					$sub_menu.slideDown(200);
					_setActiveAdcInfoGroup($(this));
				} 
				else 
				{					
					$sub_menu.slideUp(200);
					_setActiveAdcInfoGroupReset();
				}
				_loadMainContent();
			});
			
			$('.snb_tree .allBlock').click(function(e) 
			{// 전체를 클릭했을 경웅.
				e.preventDefault();
				_clearSelectionOnAllNodes();
				selectedType = 'all';	
				_setActiveAdcInfoAll();
				_loadMainContent();
			});
			// ListContaire 에서 ADC를  눌렀을때 이벤트 함수
			$('.snb_tree .adcBlock').click(function(e) 
			{
				_applyLeftCssEachADC();

//				if($(this).hasClass('on'))				
//					return;
				
				_clearSelectionOnAllNodes();
				$(this).addClass('on');
				
				var groupindex = $(this).parent().parent().parent().find('.GroupIndex').text();
				var groupname = $(this).parent().parent().parent().find('.GroupName').text();
				_setActiveAdcInfoEach($(this), groupindex, groupname);
				
				_loadMainContent();
				
//				objOnAdcChange.onAdcChange();
//				
//				FlowitUtil.log('adcBlock');
//				// 장애진단에 필요한 SelectIndex를 저장한다.
//				adcSetting.setSelectIndex(2);
//				selectedType = 'adc';
//				adc.index = $(this).find('.adcIndex').text();
//				adc.name = $(this).find('.adcName').text();
//				adc.type = $(this).find('.adcType').text();
//				groupindex = $(this).parent().parent().parent().find('.GroupIndex').text();
//				adcversion = $(this).find('.adcVersion').text();
//				adcModel = $(this).find('.adcModel').text();
//				adcStatus = $(this).find('.adcStatus').text();
//				isFlb = $(this).find('.isFlb').text();
//				setGroupIndex(groupindex);
//				setAdcVersion(adcversion);
//				setAdcModel(adcModel);
//				setAdcStatus(adcStatus);
//				setIsFlb(isFlb);
	//			onAdcChange();
			});
			// the entire adcs node
//			$('.snb_tree > li > p').click(function(e) 
//			{
//				if($(this).hasClass('on'))
//					return;
//				
//				_clearSelectionOnAllNodes();
//				$(this).addClass('on');
//				_closeAllGroups();
//			});
		}
	},	
//	onAdcChange : function() 
//	{
//		FlowitUtil.log('adcSetting.onAdcChange()');
//		adcSetting.loadAdcModifyContent(selectedADC.index, selectedADC.type);
//	},
	_clearSelectionOnAllNodes : function() 
	{
		var $allSelectableNodes =  $('.snb_tree li p');
		$allSelectableNodes.removeClass('on');
	},
//	refresh : function() 
//	{
//		with (this) 
//		{
//			var selectedAdcIndex = _getSelectedAdc().find('.adcIndex').text();
//			FlowitUtil.log("selected adc index: ", selectedAdcIndex);
//			var openGroupNames = $('.snb .snb_tree .depth2').filter(function() 
//			{
//				return $(this).css('display') != 'none';
//			}).map(function() {
//				var groupWithAdcCount = $.trim($(this).prev().children('span').first().text());
//				return groupWithAdcCount.substring(0, groupWithAdcCount.indexOf('('));
//			});
//			FlowitUtil.log("open group names: ", openGroupNames);
//			taskQ.add(function() 
//			{
//				loadLeftPane(undefined, taskQ, true);
//			});
//			taskQ.add(function() 
//			{
//				_closeAllGroups();
//				_openGroupsWithNames(openGroupNames);
//				if (selectedAdcIndex)
//				{
//					_selectAdc(selectedAdcIndex);
//				
//				}
//				else
//				{
////					_selectFirstAdc();
////					_selectAllGroups();
//				}
//				
//				taskQ.notifyTaskDone();
//			});
//			taskQ.start();
//		}
//	},



	
	// ListContaier 에서 GROUP을 선택할 시 ADC의 정보를 담는 함수
//	_setGroupToClickedOne : function($p)
//	{
//		with(this)
//		{
//			if ($p.length == 0)
//				return;
//			var groupindex1 = "";
//			var groupname1 = "";
//			groupindex = $p.parent().find(".GroupIndex").text();
//			groupname = $p.parent().find(".GroupName").text();
//			setGroupIndex(groupindex1);
//			setGroupName(groupname1);
//		}
//	},	
	_setActiveAdcInfoGroup : function($p) 
	{
		with(this)
		{
			if ($p.length == 0)
				return;
			
			var adcInfo = {};
			
			adcInfo.index = undefined;
			adcInfo.name = undefined;
			adcInfo.type = undefined;
			
			adcInfo.adcStatus = undefined;
//			adcInfo.adcversion = undefined;
			adcInfo.adcModel =undefined;
			
			adcInfo.categoryIndex = 1;
			adcInfo.groupindex =  $p.parent().find(".GroupIndex").text();
			adcInfo.groupname = $p.parent().find(".GroupName").text();
			
			_setActiveAdcInfo(adcInfo);
		}
	},
	_setActiveAdcInfoGroupReset : function() 
	{
		with(this)
		{
			var adcInfo = {};
			
			adcInfo.index = undefined;
			adcInfo.name = undefined;
			adcInfo.type = undefined;
			
			adcInfo.adcStatus = undefined;
//			adcInfo.adcversion = undefined;
			adcInfo.adcModel =undefined;
			
			adcInfo.categoryIndex = undefined;
			adcInfo.groupindex = undefined;
			adcInfo.groupname = undefined;
			
			_setActiveAdcInfo(adcInfo);
		}
	},
	_setActiveAdcInfoAll : function() 
	{
		with(this)
		{
			var adcInfo = {};
			
			adcInfo.index = undefined;
			adcInfo.name = undefined;
			adcInfo.type = undefined;
			
			adcInfo.adcStatus = undefined;
//			adcInfo.adcversion = undefined;
			adcInfo.adcModel =undefined;
			
			adcInfo.categoryIndex = 0;
			adcInfo.groupindex = undefined;
			adcInfo.groupname = undefined;
			
			_setActiveAdcInfo(adcInfo);
		}
	},
	_setActiveAdcInfoEach : function($p, groupIndex, groupName) 
	{
		with(this)
		{
			if ($p.length == 0)
				return ;
				
			var adcInfo = {};
			adcInfo.index = $p.find(".adcIndex").text();
			adcInfo.name = $p.find(".adcName").text();
			adcInfo.type = $p.find(".adcType").text();
			
			adcInfo.adcStatus = $p.find(".adcStatus").text();
//			adcInfo.adcversion = $p.find(".adcVersion").text();
			adcInfo.adcModel = $p.find(".adcModel").text();
			adcInfo.categoryIndex = 2;
			
			adcInfo.groupindex = groupIndex;
			adcInfo.groupname = groupName;
			
			_setActiveAdcInfo(adcInfo);
		}
	},
	// ListContaier 에서 ADC를 선택할 시 ADC의 정보를 담는 함수
	_setAdcToClickedOne : function($p) 
	{
		with(this)
		{
			if ($p.length == 0)
				return ;
				
			var adcInfo = {};
			adcInfo.index = $p.find(".adcIndex").text();
			adcInfo.name = $p.find(".adcName").text();
			adcInfo.type = $p.find(".adcType").text();
			
			adcInfo.adcStatus = $p.find(".adcStatus").text();
//			adcInfo.adcversion = $p.find(".adcVersion").text();
			adcInfo.adcModel = $p.find(".adcModel").text();
			adcInfo.categoryIndex = 2;
			
			adcInfo.groupindex = undefined;
			adcInfo.groupname = undefined;
			
			_setActiveAdcInfo(adcInfo);
		}
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
});
