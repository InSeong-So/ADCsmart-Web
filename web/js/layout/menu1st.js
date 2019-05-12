/*
 * 1차 메뉴를 구성 처리한다.
 */
var ClassMenu1st = Class.create(
{
	initialize : function() 
	{
		this.refreshIntervalSeconds = 10;	//  시간조절 초단위
		this.refreshTimer = undefined;
		this.dashboardPopup = undefined;
		this.OBAjaxManager = new OBAjax();
		this.CMenu2ndADC = new ClassMenu2ndAdcList();
		this.CMenu2ndSysSet = new ClassMenu2ndSysSet();
		this.activeMainMenu = undefined;
		this.activeSubMenu = undefined;
	},	
	getActiveMainMenu : function() 
	{
		with(this)
			return this.activeMainMenu;
	},
	getActiveSubMenu : function() 
	{
		with(this)
			return this.activeSubMenu;
	},
	setActiveMainMenu : function(menu) 
	{
		with(this)
			this.activeMainMenu = menu;
	},	
	setActiveSubMenu : function(menu) 
	{
		with(this)
			this.activeSubMenu = menu;
	},		
	_getDashboardPopup : function() 
	{
		with(this)
		{
			return this.dashboardPopup;
		}
	},
	_setDashboardPopup : function(dashPopup) 
	{
		with(this)
		{
			this.dashboardPopup = dashPopup;
		}
	},
	loadContent : function()
	{
		with(this)
		{
			ajaxManager.runHtml({
				url : "layout/loadLeftNavi.action",
				target : "#wrap .nav",
				successFn : function(params)
				{
					_registerEvents();	
					CMenu2ndADC.loadContent('', '');
				}
			});
		}
	},
	closeDashboardPopup : function()
	{// dashboard를 close한다. layoutheader의 logout 버튼을 클릭했을 경우에 호출된다.
		with(this)
		{
			if (_getDashboardPopup() !== undefined)
			{				
				_getDashboardPopup().close();
			}
		}
	},
	_registerEvents : function() 
	{
		with(this)
		{		
			$('#menu ul').hide();
			$('#menu li a').click(function()
			{
				var checkElement = $(this).next();			
			
				if((checkElement.is('ul')) && (checkElement.is(':visible'))) 
				{				
					$('#menu ul:visible').slideUp(300);
					return false;
				}
				if((checkElement.is('ul')) && (!checkElement.is(':visible'))) 
				{
					$('#menu ul:visible').slideUp(300);
					checkElement.slideDown(300);
					return false;
				}
			});
		
		
			// Left Navigation 데시보드 클릭시 발생하는 이벤트
			$('.dashboardMnu').click(function(e) 
			{
				setActiveMainMenu('dashboardMnu');
				setActiveSubMenu('');
				_applyActiveMenuCss('.dashboardMnu', '.dynamicDashboardMnu'); 
				
				var DashboardPopup = dashboardLoader.loadDashboard();
				_setDashboardPopup(DashboardPopup);
			});	
			
//------------------------------ 모니터링 메뉴 클릭 -------------------------------		
			// Left Navigation 모니터링 클릭시 발생하는 이벤트
			$('.monitorMnu').click(function(e)
			{
				e.preventDefault();		
				setActiveMainMenu('monitorMnu');
				setActiveSubMenu('monitorApplianceMnu');	
				_applyActiveMenuCss('.monitorMnu', '.monitorApplianceMnu'); 
				CMenu2ndADC.loadContent('monitorMnu', 'monitorApplianceMnu');
				setActiveMainMenu('monitorMnu');
				setActiveSubMenu('monitorApplianceMnu');
			});
			// 모니터링 : ADC장비 클릭시 발생하는 이벤트
			$('.monitorApplianceMnu').click(function(e)
			{
				e.preventDefault();
				setActiveMainMenu('monitorMnu');	
				setActiveSubMenu('monitorApplianceMnu');
				_applyActiveMenuCss('.monitorMnu', '.monitorApplianceMnu'); 
				CMenu2ndADC.loadContent('monitorMnu', 'monitorApplianceMnu');
			});	
			// 모니터링 : 서비스성능 클릭시 발생하는 이벤트
			$('.monitorServicePerfomanceMnu').click(function(e)
			{
				e.preventDefault();
				setActiveMainMenu('monitorMnu');	
				setActiveSubMenu('monitorMnu', 'monitorServicePerfomanceMnu');
				_applyActiveMenuCss('.monitorMnu', '.monitorServicePerfomanceMnu'); 
				CMenu2ndADC.loadContent('monitorMnu', 'monitorServicePerfomanceMnu');
			});
			// 모니터링 : 서비스요약 클릭시 발생하는 이벤트
			$('.monitorNetworkMnu').click(function(e) 
			{
				e.preventDefault();	
				setActiveMainMenu('monitorMnu');	
				setActiveSubMenu('monitorMnu', 'monitorNetworkMnu');			
				_applyActiveMenuCss('.monitorMnu', '.monitorNetworkMnu'); 
				CMenu2ndADC.loadContent('monitorMnu', 'monitorNetworkMnu');
			});
			
			// 모니터링 : 인터페이스 클릭시 발생하는 이벤트
			$('.monitorStatisticsMnu').click(function(e) 
			{
				e.preventDefault();	
				setActiveMainMenu('monitorMnu');	
				setActiveSubMenu('monitorStatisticsMnu');				
				_applyActiveMenuCss('.monitorMnu', '.monitorStatisticsMnu'); 		
				CMenu2ndADC.loadContent('monitorMnu', 'monitorStatisticsMnu');
			});
			// FLB 정보 메뉴 클릭시.
			$('.flbInfoMnu').click(function(e) 
			{		
				e.preventDefault();
				setActiveMainMenu('monitorMnu');	
				setActiveSubMenu('flbInfoMnu');					
				_applyActiveMenuCss('.monitorMnu', '.flbInfoMnu'); 
				CMenu2ndADC.loadContent('monitorMnu', 'flbInfoMnu');
			});
			// 모니터링 : 장애 메뉴 클릭시 발생하는 이벤트
			$('.monitorFaultMnu').click(function(e) 
			{
				e.preventDefault();	
				setActiveMainMenu('monitorMnu');	
				setActiveSubMenu('monitorFaultMnu');					
				_applyActiveMenuCss('.monitorMnu', '.monitorFaultMnu'); 
				CMenu2ndADC.loadContent('monitorMnu', 'monitorFaultMnu');
			});		
			// 모니터링 : ADC 로그 메뉴 클릭시 발생하는 이벤트
			$('.adcLogMnu').click(function(e) 
			{
				e.preventDefault();	
				setActiveMainMenu('monitorMnu');	
				setActiveSubMenu('adcLogMnu');				
				_applyActiveMenuCss('.monitorMnu', '.adcLogMnu'); 
				CMenu2ndADC.loadContent('monitorMnu', 'adcLogMnu');
			});
	
	//------------------------------ 분석 메뉴 클릭 -------------------------------		
			// 분석 : 메뉴 클릭시 발생하는 이벤트
			$('.faultMgmtMnu').click(function(e)
			{
				e.preventDefault();
				setActiveMainMenu('faultMgmtMnu');	
				setActiveSubMenu('faultHistoryMnu');
	
				_applyActiveMenuCss('.faultMgmtMnu', '.faultHistoryMnu'); 
				CMenu2ndADC.loadContent('faultMgmtMnu', 'faultHistoryMnu');
			});
			// 분석 : 진단 메뉴 클릭시 발생하는 이벤트
			$('.faultHistoryMnu').click(function(e)
			{
				e.preventDefault();
				setActiveMainMenu('faultMgmtMnu');	
				setActiveSubMenu('faultHistoryMnu');
				_applyActiveMenuCss('.faultMgmtMnu', '.faultHistoryMnu'); 
				CMenu2ndADC.loadContent('faultMgmtMnu', 'faultHistoryMnu');
			});
			// 분석 : 패킷 메뉴 클릭시 발생하는 이벤트
			$('.faultAnalysisMnu').click(function(e)
			{
				e.preventDefault();
				setActiveMainMenu('faultMgmtMnu');	
				setActiveSubMenu('faultMgmtMnu', 'faultAnalysisMnu');
				_applyActiveMenuCss('.faultMgmtMnu', '.faultAnalysisMnu'); 
				CMenu2ndADC.loadContent('faultMgmtMnu', 'faultAnalysisMnu');
			});	
			// 분석 : L2검색 클릭시 발생하는 이벤트
			$('.monitorL2Mnu').click(function(e)
			{
				e.preventDefault();
				setActiveMainMenu('faultMgmtMnu');	
				setActiveSubMenu('monitorL2Mnu');			
				_applyActiveMenuCss('.faultMgmtMnu', '.monitorL2Mnu'); 
				CMenu2ndADC.loadContent('faultMgmtMnu', 'monitorL2Mnu');
			});		
			// 분석 : 세션검색 클릭시 발생하는 이벤트
			$('.sessionMonitoringMnu').click(function(e)
			{
				e.preventDefault();
				setActiveMainMenu('faultMgmtMnu');	
				setActiveSubMenu('sessionMonitoringMnu');				
				_applyActiveMenuCss('.faultMgmtMnu', '.sessionMonitoringMnu'); 
				CMenu2ndADC.loadContent('faultMgmtMnu', 'sessionMonitoringMnu');
			});
			
	//------------------------------ 설정 메뉴 클릭 -------------------------------
			// 설정 :설정 메뉴 클릭시 발생하는 이벤트
			$('.slbMgmtMnu').click(function(e)
			{
				e.preventDefault();	
				setActiveMainMenu('slbMgmtMnu');	
				setActiveSubMenu('adcSettingMnu');				
				_applyActiveMenuCss('.slbMgmtMnu', '.adcSettingMnu'); 
				CMenu2ndADC.loadContent('slbMgmtMnu', 'adcSettingMnu');
			});		
			// 설정 :ADC 설정 메뉴 클릭시 발생하는 이벤트
			$('.adcSettingMnu').click(function(e) 
			{		
				e.preventDefault();
				setActiveMainMenu('slbMgmtMnu');	
				setActiveSubMenu('adcSettingMnu');	
				_applyActiveMenuCss('.slbMgmtMnu', '.adcSettingMnu'); 
				CMenu2ndADC.loadContent('slbMgmtMnu', 'adcSettingMnu');
			});
			// 설정 :SLB 설정 메뉴 클릭시 발생하는 이벤트
			$('.slbSettingMnu').click(function(e) 
			{		
				e.preventDefault();	
				setActiveMainMenu('slbMgmtMnu');	
				setActiveSubMenu('slbSettingMnu');	
				_applyActiveMenuCss('.slbMgmtMnu', '.slbSettingMnu'); 
				CMenu2ndADC.loadContent('slbMgmtMnu', 'slbSettingMnu');
			});
			// 설정 :설정이력 설정 메뉴 클릭시 발생하는 이벤트
			$('.slbHistoryMnu').click(function(e) 
			{
				e.preventDefault();	
				setActiveMainMenu('slbMgmtMnu');	
				setActiveSubMenu('slbHistoryMnu');				
				_applyActiveMenuCss('.slbMgmtMnu', '.slbHistoryMnu'); 
				CMenu2ndADC.loadContent('slbMgmtMnu', 'slbHistoryMnu');
			});		
			// 설정 :경보 설정 메뉴 클릭시 발생하는 이벤트
			$('.adcAlertMnu').click(function(e) 
			{
				e.preventDefault();	
				setActiveMainMenu('slbMgmtMnu');	
				setActiveSubMenu('adcAlertMnu');				
				_applyActiveMenuCss('.slbMgmtMnu', '.adcAlertMnu'); 
				CMenu2ndADC.loadContent('slbMgmtMnu', 'adcAlertMnu');
			});
			
	//------------------------------ 보고서 메뉴 클릭 -------------------------------		
			// 보고서 :보고서 메뉴 클릭시 발생하는 이벤트
			$('.reportMnu').click(function(e) 
			{
				e.preventDefault();	
				setActiveMainMenu('reportMnu');	
				setActiveSubMenu('');				
				_applyActiveMenuCss('.reportMnu'); 
				CMenu2ndADC.loadContent('reportMnu', '');
			});		
			
	//------------------------------ 시스템 관리 메뉴 클릭 -------------------------------	
			// Left Navigation 시스템관리 클릭시 발생하는 이벤트
			$('.sysSettingMnu').click(function(e) 
			{
				e.preventDefault();
				setActiveMainMenu('sysSettingMnu');	
				setActiveSubMenu('');	
				_applyActiveMenuCss('.sysSettingMnu'); 
				CMenu2ndSysSet.loadContent();
			});
		}
	},	
	_applyActiveMenuCss : function(mainItemName1, subItemName1) 
	{//  선택된 메뉴임을 표시하기 위한 색깔 변경 작업을 실시한다.
		with (this) 
		{	
//			monitorServicePerfomance.clearRealTimer();
			var clickedItem = $(mainItemName1);
			clickedItem = _setOverFadeIn(clickedItem);
			if(subItemName1 !== undefined)
			{
				over(clickedItem.next('ul').find(subItemName1));
			}
		}
	},
	//공통 부분 initInactiveMenuItems, over, fadeIn 함수화
	_setOverFadeIn : function (clickedItem)
	{
		with (this)
		{
			_initInactiveMenuItems(clickedItem);
			over(clickedItem);
			clickedItem.next('ul').stop(true, true).fadeIn();			
			return clickedItem;
		}
	},	
	_initInactiveMenuItems : function($activeItem) 
	{
		with (this)
		{
			// 1-depth menu
			$('.topSnb > li').each(function() 
			{
				out($(this).children('a'));
	
				if ($(this).children('ul').length > 0) 
				{
					if (!$activeItem || !$(this).children('a').is($activeItem)) 	//$(this).children('a').is($activeItem)  -> a.xxxMnu   /   $(this)  -> li
						$(this).children('ul').stop(true, true).fadeOut();
					
					// 2-depth menu
					$(this).find('ul li a').each(function() 
					{
						out($(this));
					});
				}
			});
		}
	},
	loadMainContent : function() 
	{
		with (this) 
		{			
			ajaxManager.runHtml({
				url : "layout/loadContent.action",				
				target: "#wrap .contents",
				successFn : function(params) 
				{
//					setActiveMenu('Monitor');
				},
				errorFn : function(params) 
				{	
				}
			});
		}
	},		
	_loadLoginCheck : function() 
	{
		with (this) 
		{
			ajaxManagerOB.runJson({
				url : "layout/loadLoginCheck.action",
				target : ".header",
				successFn : function(params) 
				{					
				},
				errorFn : function(params) 
				{
//					alert("");
				}
			});		
		}		
	},	
});
