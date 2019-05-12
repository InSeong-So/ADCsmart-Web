var Header = Class.create(
{
	initialize : function() 
	{
		this.refreshIntervalSeconds = 10;	//  시간조절 초단위
		this.refreshTimer = undefined;
		this.activeMenu;
		this.OBajaxManager = new OBAjax();
		this.alertWnd = new ClassAlertWnd();
		this.auditLog = new ClassAuditLog();
		this.monitorLbTap = 0; // 0 : 전체, 1 : SLB, 2 : FLB
		this.adcSettingTap = 0; // 0 : ADC 수정, 1 : ADC 설정 정보
		this.vsSettingTap = 0; // 0 : Vs, 1 : profile, 2 : realserver, 3 : notice
	},	
	getNoticeKey : function() 
	{
		return this.NoticeKey;
	},
	setNoticeKey : function(NoticeKey) 
	{
		this.NoticeKey = NoticeKey;
	},
	getActiveMenu : function() 
	{
		return this.activeMenu;
	},
	setActiveMenu : function(menu) 
	{
		this.activeMenu = menu;
		this.applyActiveMenuCss(); 
	},	
	getAccountRole : function() 
	{
		return this.accountRole;
	},
	setAccountRole : function(accountRole) 
	{
		this.accountRole = accountRole;
	},
	getAccountId : function()
	{
		return this.accountId;
	},
	setAccountId : function(accountId)
	{
		this.accountId = accountId;
	},
	getDashPopup : function() 
	{
		return this.dashPopup;
	},
	setDashPopup : function(dashPopup) 
	{
		this.dashPopup = dashPopup;
	},
	getMonitorLbTap : function() 
	{
		return this.monitorLbTap;
	},
	setMonitorLbTap : function(monitorLbTap) 
	{
		this.monitorLbTap = monitorLbTap;
	},
	getAdcSettingTap : function() 
	{
		return this.adcSettingTap;
	},
	setAdcSettingTap : function(adcSettingTap)
	{
		this.adcSettingTap = adcSettingTap;
	},
	getVsSettingTap : function()
	{
		return this.vsSettingTap;
	},
	setVsSettingTap : function(vsSettingTap)
	{
		this.vsSettingTap = vsSettingTap;
	},
	applyActiveMenuCss : function() 
	{
		with (this) 
		{			
			var clickedItem;
			
			//데시보드
			
			$('.dashboardMnu').click(function(e)
			{
				$('.contents').css('top', '70px');
			});
			$('.logo, .reportMnu, .menu2:not(.dashSub)').click(function(e)
//			$('.logo, .reportMnu, .contentsMenu').click(function(e)					
			{
				$('.contents').css('top', '112px');
			});
			
			if (activeMenu === "SysSettingMnu")
			{
				$(".systemSub ul").find('a').removeClass("select");
				$(".toolSub ul").find('a').removeClass("select");
			}
			else if (activeMenu === "ToolMnu")
			{
				$(".systemSub ul").find('a').removeClass("select");
				$(".toolSub ul").find('a').removeClass("select");
			}
			else if (activeMenu === "Dashboard" || activeMenu === "FaultMnu" || activeMenu === "SlbMnu" || activeMenu === "MonitorMnu" || activeMenu === "LogMnu")
			{				
				if (activeMenu === "FaultMnu")
				{
					$(".dashboardMnu").find('div').attr('id', 'dashboardimg');
					$(".faultMgmtMnu").find('div').attr('id', 'faultMgmtimgSelect');
					$(".monitorMnu").find('div').attr('id', 'monitorimg');
					$(".logMnu").find('div').attr('id', 'logimg');
					$(".slbMgmtMnu").find('div').attr('id', 'slbMgmtimg');
					$(".sysSettingMnu").find('div').attr('id', 'sysSettingimg');
					$(".toolMnu").find('div').attr('id', 'systoolsimg');
				}
				else if (activeMenu === "SlbMnu")
				{
					$(".dashboardMnu").find('div').attr('id', 'dashboardimg');
					$(".faultMgmtMnu").find('div').attr('id', 'faultMgmtimg');
					$(".monitorMnu").find('div').attr('id', 'monitorimg');
					$(".logMnu").find('div').attr('id', 'logimg');
					$(".slbMgmtMnu").find('div').attr('id', 'slbMgmtimgSelect');
					$(".sysSettingMnu").find('div').attr('id', 'sysSettingimg');
					$(".toolMnu").find('div').attr('id', 'systoolsimg');
				}
				else if(activeMenu === "MonitorMnu")
				{
					$(".dashboardMnu").find('div').attr('id', 'dashboardimg');
					$(".faultMgmtMnu").find('div').attr('id', 'faultMgmtimg');
					$(".monitorMnu").find('div').attr('id', 'monitorimgSelect');
					$(".logMnu").find('div').attr('id', 'logimg');
					$(".slbMgmtMnu").find('div').attr('id', 'slbMgmtimg');
					$(".sysSettingMnu").find('div').attr('id', 'sysSettingimg');
					$(".toolMnu").find('div').attr('id', 'systoolsimg');
				}
				else if(activeMenu === "LogMnu")
				{
					$(".faultMgmtMnu").find('div').attr('id', 'faultMgmtimg');
					$(".monitorMnu").find('div').attr('id', 'monitorimg');
					$(".logMnu").find('div').attr('id', 'logimgSelect');
					$(".slbMgmtMnu").find('div').attr('id', 'slbMgmtimg');
					$(".sysSettingMnu").find('div').attr('id', 'sysSettingimg');
					$(".toolMnu").find('div').attr('id', 'systoolsimg');
				}
				else if(activeMenu === "Dashboard")
				{
					$(".dashboardMnu").find('div').attr('id', 'dashboardimgSelect');
					$(".faultMgmtMnu").find('div').attr('id', 'faultMgmtimg');
					$(".monitorMnu").find('div').attr('id', 'monitorimg');
					$(".logMnu").find('div').attr('id', 'logimg');
					$(".slbMgmtMnu").find('div').attr('id', 'slbMgmtimg');
					$(".sysSettingMnu").find('div').attr('id', 'sysSettingimg');
					$(".toolMnu").find('div').attr('id', 'systoolsimg');
				}
				
//				$(".dashboardMnu").find('div').attr('id', 'dashboardimg');
				$(".reportMnu").find('div').attr('id', 'reportimg');

				$(".dashSub ul").find('a').removeClass("select");
				$(".faultSub ul").find('a').removeClass("select");
				$(".monitorSub ul").find('a').removeClass("select");
				$(".logSub ul").find('a').removeClass("select");
				$(".slbSettingSub ul").find('a').removeClass("select");
				$(".systemSub ul").find('a').removeClass("select");		
				$(".toolSub ul").find('a').removeClass("select");
			}
			//장애 진단 - 이력
			else if (activeMenu === 'FaultHistory')
			{				
				monitorServicePerfomance.clearRealTimer();
				$(".dashboardMnu").find('div').attr('id', 'dashboardimg');
				$(".faultMgmtMnu").find('div').attr('id', 'faultMgmtimgSelect');
				$(".monitorMnu").find('div').attr('id', 'monitorimg');
				$(".logMnu").find('div').attr('id', 'logimg');
				$(".slbMgmtMnu").find('div').attr('id', 'slbMgmtimg');
				$(".reportMnu").find('div').attr('id', 'reportimg');
				$(".sysSettingMnu").find('div').attr('id', 'sysSettingimg');
				$(".toolMnu").find('div').attr('id', 'systoolsimg');
				
				$(".faultSub ul").find('a').removeClass("select");
				$(".faultSub ul").find('a:first').attr("class", "select");
				$('.LocationNavi').removeClass('none');
//				$imgSrc = $('.faultImg').last();
//				$imgSrc.attr('src', 'imgs/meun/1M_Analysis_1.png');
			}	
			//장애 진단 - 패킷분석
			else if (activeMenu === 'FaultAnalysis')
			{				
				monitorServicePerfomance.clearRealTimer();
				clickedItem = $('.faultMgmtMnu');
				setOverFadeIn(clickedItem);
//				over(clickedItem.next('ul').find('.faultAnalysisMnu'));
				$(".faultSub ul").find('a').removeClass("select");
				$(".faultSub ul").find('.faultAnalysisMnu').attr("class", "select");
				$('.LocationNavi').removeClass('none');
			}
			// L2 검색
			else if (activeMenu === 'MonitorL2Info')
			{				
				monitorServicePerfomance.clearRealTimer();
				clickedItem = $('.faultMgmtMnu');
				setOverFadeIn(clickedItem);
//				over(clickedItem.next('ul').find('.monitorL2Mnu'));
				$(".faultSub ul").find('a').removeClass("select");
				$(".faultSub ul").find('.monitorL2Mnu').attr("class", "select");
				$('.LocationNavi').removeClass('none');
			}
			// 세션 검색
			else if (activeMenu === 'SessionMonitoring')
			{				
				monitorServicePerfomance.clearRealTimer();
				clickedItem = $('.faultMgmtMnu');
				setOverFadeIn(clickedItem);
//				over(clickedItem.next('ul').find('.sessionMonitoringMnu'));
				$(".faultSub ul").find('a').removeClass("select");
				$(".faultSub ul").find('.sessionMonitoringMnu').attr("class", "select");
				$('.LocationNavi').removeClass('none');
			}	
			
			//Monitoring
			// 장비 모니터링
			if (activeMenu === 'MonitorAppliance')
			{				
				monitorServicePerfomance.clearRealTimer();
				$(".dashboardMnu").find('div').attr('id', 'dashboardimg');
				$(".faultMgmtMnu").find('div').attr('id', 'faultMgmtimg');
				$(".monitorMnu").find('div').attr('id', 'monitorimgSelect');
				$(".logMnu").find('div').attr('id', 'logimg');
				$(".slbMgmtMnu").find('div').attr('id', 'slbMgmtimg');
				$(".reportMnu").find('div').attr('id', 'reportimg');
				$(".sysSettingMnu").find('div').attr('id', 'sysSettingimg');
				$(".toolMnu").find('div').attr('id', 'systoolsimg');
				$(".monitorSub ul").find('a').removeClass("select");
				$(".monitorSub ul").find('a:first').attr("class", "select");
				$('.LocationNavi').removeClass('none');
			}			
			// 서비스 성능 모니터링
			else if (activeMenu === 'MonitorServicePerfomance')
			{				
				monitorServicePerfomance.clearRealTimer();
				clickedItem = $('.monitorMnu');
				setOverFadeIn(clickedItem);
//				over(clickedItem.next('ul').find('.monitorServicePerfomanceMnu'));
				$(".monitorSub ul").find('a').removeClass("select");
				$(".monitorSub ul").find('.monitorServicePerfomanceMnu a').attr("class", "select");
				$('.LocationNavi').removeClass('none');
			}
			// 그룹 모니터링
			else if(activeMenu === "MonitorGroup")
			{
				monitorServicePerfomance.clearRealTimer();
				clickedItem = $('.monitorMnu');
				setOverFadeIn(clickedItem);
//				over(clickedItem.next('ul').find('.monitorGroupMnu'));
				$(".monitorSub ul").find('a').removeClass("select");
				$(".monitorSub ul").find('.monitorGroupMnu a').attr("class", "select");
				$('.LocationNavi').removeClass('none');
			}
			// RealServer 모니터링
			else if(activeMenu === "MonitorRealServer")
			{
				monitorServicePerfomance.clearRealTimer();
				clickedItem = $('.monitorMnu');
				setOverFadeIn(clickedItem);
//				over(clickedItem.next('ul').find('.monitorRealServerMnu'));
				$(".monitorSub ul").find('a').removeClass("select");
				$(".monitorSub ul").find('.monitorRealServerMnu a').attr("class", "select");
				$('.LocationNavi').removeClass('none');
			}
			// VS요약 모니터링
			else if (activeMenu === 'MonitorNetwork') 
			{				
				monitorServicePerfomance.clearRealTimer();	
				clickedItem = $('.monitorMnu');
				setOverFadeIn(clickedItem);
//				over(clickedItem.next('ul').find('.monitorNetworkMnu'));
				$(".monitorSub ul").find('a').removeClass("select");
				$(".monitorSub ul").find('.monitorNetworkMnu a').attr("class", "select");
				$('.LocationNavi').removeClass('none');
			}			
			// 인터페이스 모니터링
			else if (activeMenu === 'MonitorStatistics') 
			{				
				monitorServicePerfomance.clearRealTimer();	
				clickedItem = $('.monitorMnu');
				setOverFadeIn(clickedItem);
//				over(clickedItem.next('ul').find('.monitorStatisticsMnu'));
				$(".monitorSub ul").find('a').removeClass("select");
				$(".monitorSub ul").find('.monitorStatisticsMnu a').attr("class", "select");
				$('.LocationNavi').removeClass('none');
			}
			//FLB 정보
			else if (activeMenu === 'FlbInfo')
			{				
				monitorServicePerfomance.clearRealTimer();
				clickedItem = $('.monitorMnu');
				setOverFadeIn(clickedItem);
//				over(clickedItem.next('ul').find('.flbInfoMnu'));
				$(".monitorSub ul").find('a').removeClass("select");
				$(".monitorSub ul").find('.flbInfoMnu a').attr("class", "select");
				$('.LocationNavi').removeClass('none');
			}
			// 장애 모니터링
			else if (activeMenu === 'MonitorFault') 
			{				
				monitorServicePerfomance.clearRealTimer();
				clickedItem = $('.logMnu');
				setOverFadeIn(clickedItem);
				$(".logSub ul").find('a').removeClass("select");
				$(".logSub ul").find('.monitorFaultMnu a').attr("class", "select");
				$('.LocationNavi').removeClass('none');
			}
			// 경보 모니터링
			else if (activeMenu === 'MonitorAlert') 
			{				
				monitorServicePerfomance.clearRealTimer();
				clickedItem = $('.logMnu');
				setOverFadeIn(clickedItem);
//				over(clickedItem.next('ul').find('.monitorAlertMnu'));
				$(".logSub ul").find('a').removeClass("select");
				$(".logSub ul").find('.monitorAlertMnu a').attr("class", "select");
				$('.LocationNavi').removeClass('none');
			}	
			// ADC 로그
			else if (activeMenu === 'AdcLog')
			{				
				monitorServicePerfomance.clearRealTimer();
				clickedItem = $('.logMnu');
				setOverFadeIn(clickedItem);
//				over(clickedItem.next('ul').find('.adcLogMnu'));
				$(".logSub ul").find('a').removeClass("select");
				$(".logSub ul").find('.adcLogMnu a').attr("class", "select");
				$('.LocationNavi').removeClass('none');
			}
			
			// ADC 설정
			else if (activeMenu === 'AdcSetting')
			{				
				monitorServicePerfomance.clearRealTimer();
//				clickedItem = $('.slbMgmtMnu');
//				setOverFadeIn(clickedItem);
//				over(clickedItem.next('ul').find('.adcSettingMnu'));
//				$(this).children().children('ul').find('li:first').attr("class", "select");	
				$(".dashboardMnu").find('div').attr('id', 'dashboardimg');
				$(".faultMgmtMnu").find('div').attr('id', 'faultMgmtimg');
				$(".monitorMnu").find('div').attr('id', 'monitorimg');
				$(".logMnu").find('div').attr('id', 'logimg');
				$(".slbMgmtMnu").find('div').attr('id', 'slbMgmtimgSelect');
				$(".reportMnu").find('div').attr('id', 'reportimg');
				$(".sysSettingMnu").find('div').attr('id', 'sysSettingimg');
				$(".toolMnu").find('div').attr('id', 'systoolsimg');
				$(".slbSettingSub ul").find('a').removeClass("select");			
				$(".slbSettingSub ul").find('.adcSettingMnu a:first').attr("class", "select");
				$('.LocationNavi').removeClass('none');
				
				if (adcSetting.getSelectIndex() == 2)
				{
					$(".adcSettingSubSub").removeClass('none');
					
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
					else
					{
					}
				}
				else
				{
					$(".adcSettingSubSub").addClass('none');
				}
			}
			// 경보설정
			else if (activeMenu === 'AdcAlert')
			{				
				monitorServicePerfomance.clearRealTimer();
				clickedItem = $('.slbMgmtMnu');
				setOverFadeIn(clickedItem);
//				over(clickedItem.next('ul').find('.adcAlertMnu'));
//				$(this).children().children('ul').find('li:first');
				$(".slbSettingSub ul").find('a').removeClass("select");
//				$(".slbSettingSub ul").find('li:eq(3)').attr("class", "select");
				$(".slbSettingSub ul").find('.adcAlertMnu a').attr("class", "select");
				$('.LocationNavi').removeClass('none');
			}
			else if (activeMenu === "SlbSchedule")
			{
				monitorServicePerfomance.clearRealTimer();
				clickedItem = $('.slbMgmtMnu');
				setOverFadeIn(clickedItem);
//				over(clickedItem.next('ul').find('.slbHistoryMnu'));	
//				$(this).children().children('ul').find('li:first');
				$(".slbSettingSub ul").find('a').removeClass("select");
//				$(".slbSettingSub ul").find('li:eq(2)').attr("class", "select");
				$(".slbSettingSub ul").find('.slbScheduleMnu a').attr("class", "select");
				$('.LocationNavi').removeClass('none');
			}
			// SLB 설정
			else if (activeMenu === 'SlbSetting')
			{				
				monitorServicePerfomance.clearRealTimer();
				clickedItem = $('.slbMgmtMnu');
				setOverFadeIn(clickedItem);
				$(".slbSettingSub ul").find('a').removeClass("select");
				$(".slbSettingSub ul").find('.slbSettingMnu a').attr("class", "select");
				$('.LocationNavi').removeClass('none');
				
				if(header.getVsSettingTap() == 0)
				{
					$(".slbSettingSubSub").find('a').removeClass("select");
					$(".slbSettingSubSub").find('a').removeClass("choice");
					$(".slbSettingSubSub").find('a:first').attr("class", "choice");
				}
				
				else if(header.getVsSettingTap() == 1)
				{
					$(".slbSettingSubSub").find('a').removeClass("select");
					$(".slbSettingSubSub").find('a').removeClass("choice");					
					$(".slbSettingSubSub").find('.slbProfile a').attr("class", "choice");
				}
				else if(header.getVsSettingTap() == 2)
				{
					$(".slbSettingSubSub").find('a').removeClass("select");
					$(".slbSettingSubSub").find('a').removeClass("choice");
					$(".slbSettingSubSub").find('.slbRs a').attr("class", "choice");
				}
				else if(header.getVsSettingTap() == 3)
				{
					$(".slbSettingSubSub").find('a').removeClass("select");
					$(".slbSettingSubSub").find('a').removeClass("choice");
					$(".slbSettingSubSub").find('.slbNotice a').attr("class", "choice");
				}
				else
				{
//					$(".slbSettingSub ul").find('li').removeClass("select");
//					$(".slbSettingSub ul").find('li:eq(1)').attr("class", "select");
				}				
			}	

			// SLB 설정이력
			else if (activeMenu === 'SlbHistory')
			{				
				monitorServicePerfomance.clearRealTimer();
				clickedItem = $('.slbMgmtMnu');
				setOverFadeIn(clickedItem);
//				over(clickedItem.next('ul').find('.slbHistoryMnu'));	
//				$(this).children().children('ul').find('li:first');
				$(".slbSettingSub ul").find('a').removeClass("select");
//				$(".slbSettingSub ul").find('li:eq(2)').attr("class", "select");
				$(".slbSettingSub ul").find('.slbHistoryMnu a').attr("class", "select");
				$('.LocationNavi').removeClass('none');
			}

			//보고서
			else if (activeMenu === 'Report') 
			{				
				monitorServicePerfomance.clearRealTimer();
//				clickedItem = $('.reportMnu');			
//				setOverFadeIn(clickedItem);
				$(".slbMgmtMnu").find('div').attr('id', 'slbMgmtimg');
				$(".dashboardMnu").find('div').attr('id', 'dashboardimg');
				$(".faultMgmtMnu").find('div').attr('id', 'faultMgmtimg');
				$(".monitorMnu").find('div').attr('id', 'monitorimg');
				$(".logMnu").find('div').attr('id', 'logimg');
				$(".slbMgmtMnu").find('div').attr('id', 'slbMgmtimg');
				$(".reportMnu").find('div').attr('id', 'reportimgSelect');
				$(".sysSettingMnu").find('div').attr('id', 'sysSettingimg');
				$(".toolMnu").find('div').attr('id', 'systoolsimg');
				$('.LocationNavi').removeClass('none');
			}	
			
			else if (activeMenu === 'SysSettingUser') 
			{						
				monitorServicePerfomance.clearRealTimer();		
				
				$(".systemSub ul").find('a').removeClass("select");
//				$(".systemSub ul").find('a:first').attr("class", "select");
				$(".systemSub ul").find('.sysSettingUserMnu a').attr("class", "select");
				$('.snb_close').click();
				$('.listcontainer_open').parent().addClass('none');
			}
			else if (activeMenu === 'SysSettingVsFilter') 
			{						
				monitorServicePerfomance.clearRealTimer();		
				
				$(".systemSub ul").find('a').removeClass("select");
//				$(".systemSub ul").find('a:first').attr("class", "select");
				$(".systemSub ul").find('.sysSettingVsFilterMnu a').attr("class", "select");
				$('.snb_close').click();
				$('.listcontainer_open').parent().addClass('none');
			}			
			else if (activeMenu === 'SysSettingLogicalGroup')
			{
				monitorServicePerfomance.clearRealTimer();
				clickedItem = $('.sysSettingMnu');
				setOverFadeIn(clickedItem);
				//over(clickedItem.next('ul').find('.sysSettingBackMnu'));	
				$(".systemSub ul").find('a').removeClass("select");
				$(".systemSub ul").find('.sysSettingLogicalGroupMnu a').attr("class", "select");
				$('.snb_close').click();
				$('.listcontainer_open').parent().addClass('none');
			}
			else if (activeMenu === 'SysSettingBackup') 
			{				
				monitorServicePerfomance.clearRealTimer();
				clickedItem = $('.sysSettingMnu');
				setOverFadeIn(clickedItem);
				//over(clickedItem.next('ul').find('.sysSettingBackMnu'));	
				$(".systemSub ul").find('a').removeClass("select");
				$(".systemSub ul").find('.sysSettingBackupMnu a').attr("class", "select");
				$('.snb_close').click();
				$('.listcontainer_open').parent().addClass('none');
			}
			else if (activeMenu === 'SysSettingSysInfo') 
			{				
				monitorServicePerfomance.clearRealTimer();
				clickedItem = $('.sysSettingMnu');
				setOverFadeIn(clickedItem);
				//over(clickedItem.next('ul').find('.sysSettingSysInfoMnu'));	
				$(".systemSub ul").find('a').removeClass("select");
				$(".systemSub ul").find('.sysSettingSysInfoMnu a').attr("class", "select");
				$('.snb_close').click();
				$('.listcontainer_open').parent().addClass('none');
			}
			else if (activeMenu === 'SysSettingLic') 
			{				
				monitorServicePerfomance.clearRealTimer();
				clickedItem = $('.sysSettingMnu');
				setOverFadeIn(clickedItem);
				//over(clickedItem.next('ul').find('.sysSettingLicMnu'));	
				$(".systemSub ul").find('a').removeClass("select");
				$(".systemSub ul").find('.sysSettingLicMnu a').attr("class", "select");
				$('.snb_close').click();
				$('.listcontainer_open').parent().addClass('none');
			}
			else if (activeMenu === 'SysSettingAudit') 
			{				
				monitorServicePerfomance.clearRealTimer();
				clickedItem = $('.sysSettingMnu');
				setOverFadeIn(clickedItem);
				//over(clickedItem.next('ul').find('.sysSettingAuditMnu'));
				$(".systemSub ul").find('a').removeClass("select");
				$(".systemSub ul").find('.sysSettingAuditMnu a').attr("class", "select");
				$('.snb_close').click();
				$('.listcontainer_open').parent().addClass('none');
			}
			else if (activeMenu === 'SysSettingConfig') 
			{				
				monitorServicePerfomance.clearRealTimer();
				clickedItem = $('.sysSettingMnu');
				setOverFadeIn(clickedItem);
				//over(clickedItem.next('ul').find('.sysSettingConfigMnu'));	
				$(".systemSub ul").find('a').removeClass("select");
				$(".systemSub ul").find('.sysSettingConfigMnu a').attr("class", "select");
				$('.snb_close').click();
				$('.listcontainer_open').parent().addClass('none');
			}	
			else if (activeMenu === 'SysSettingLogContent')
			{
				monitorServicePerfomance.clearRealTimer();
				clickedItem = $('.sysSettingMnu');
				setOverFadeIn(clickedItem);
				//over(clickedItem.next('ul').find('.sysSettingConfigMnu'));	
				$(".systemSub ul").find('a').removeClass("select");
				$(".systemSub ul").find('.sysSettingLogContentMnu a').attr("class", "select");
//				$(".toolSub ul").find('a').removeClass("select");
//				$(".toolSub ul").find('.sysSettingLogContentMnu a').attr("class", "select");
				$('.snb_close').click();
				$('.listcontainer_open').parent().addClass('none');
			}
			else if (activeMenu === 'SysSettingResponseTime')
			{
				monitorServicePerfomance.clearRealTimer();
				clickedItem = $('.toolMnu');
				setOverFadeIn(clickedItem);
//				$('.systemSub ul').find('a').removeClass("select");
//				$('.systemSub ul').find('.sysSettingResponseTimeMnu a').attr("class", "select");
				$('.toolSub ul').find('a').removeClass("select");
				$('.toolSub ul').find('.sysSettingResponseTimeMnu a').attr("class", "select");
				$('.snb_close').click();
				$('.listcontainer_open').parent().addClass('none');
			}
					
			//도구 및 진단
			else if (activeMenu === 'JSToolsPortInfo') 
			{	
				monitorServicePerfomance.clearRealTimer();
				clickedItem = $('.sysToolMnu');
				setOverFadeIn(clickedItem);
				//over(clickedItem.next('ul').find('.sysToolPortInfoMnu'));	
			} 
			else if (activeMenu === 'JSToolsSessionInfo') 
			{	
				monitorServicePerfomance.clearRealTimer();
				clickedItem = $('.sysToolMnu');
				setOverFadeIn(clickedItem);
				//over(clickedItem.next('ul').find('.sysToolSessionInfoMnu'));	
			} 
			else if (activeMenu === 'JSToolsUnusedSlbInfo') 
			{	
				monitorServicePerfomance.clearRealTimer();
				clickedItem = $('.sysToolMnu');
				setOverFadeIn(clickedItem);
				//over(clickedItem.next('ul').find('.sysToolUnusedSlbInfoMnu'));	
			}
		}
	},

	//공통 부분 initInactiveMenuItems, over, fadeIn 함수화
	setOverFadeIn : function (clickedItem)
	{
		header.initInactiveMenuItems(clickedItem);
//		over(clickedItem);
		clickedItem.next('ul').stop(true, true).fadeIn();			
		return clickedItem;
	},
	
	initInactiveMenuItems : function($activeItem) 
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
		
		$('.menu1 > span').each(function() 
		{
//			out($(this).children('a'));

			if ($(this).children().children('ul').length > 0) 
			{
				if (!$activeItem || !$(this).children('a').is($activeItem)) 	//$(this).children('a').is($activeItem)  -> a.xxxMnu   /   $(this)  -> li
					$(this).children('ul').stop(true, true).fadeOut();
				
				// 2-depth menu
//				$(this).find('ul li a').each(function() 
//				{
//					out($(this));
//				});
			}
		});
	},
	isActiveMenu : function(menu) 
	{
		return this.activeMenu == menu;
	}, 
	loadHeader : function() 
	{
		with (this) 
		{
			ajaxManager.runHtml({
				url : "layout/loadHeader.action",
				target: ".header",
				successFn : function(params) 
				{
					headerLoginEvents();
//					applyCss(); 			//기존 코드에서 사용하던 event. 우선 주석처리함.
					registerEvents();		//기존 코드에서는 header에서 메뉴 event를 처리를 했기 때문에 load할때 함수 이벤트를 등록함.		
//					loadLeftNavi();			//현재는 1차메뉴(네비게이션바)에서 메뉴이벤트를 등록하여 처리함.	
					loadPickView();
					loadAccountRole();		// 현재 접속한 계정 정보 Get. (ex : ReadOnly)
//					loadAdcSearchBar();
//					loadNoticeInfo();
//					_retrieveSettingsAndShowAlert();
					alertWnd.loadContent();
//					loadNoticeList();
//					loadMainContent();
					
//					applyCss();
				}			 
			});
		}
	},	
//	applyCss : function() {
//		// 1depth 메뉴 롤오버 효과
//		$('.menu1').mouseover(function(){
//			var dn = $(this).children().children('a');
//			var dn_src =  dn.find('img').attr('src');
//			if (!dn_src.match('_on.png')) {				
//				initMenu();
//				over(dn);
//				$(this).children().attr('class', 'on');
//				// 2depth
//				if(dn.next().length > 0){
//					dn.next().stop(true,true).fadeIn();
//				}
//			}
//		});
//		
//		// 1 depth 메뉴와 2 depth 메뉴의 영역을 벗어 날 경우 이벤트
//		$('.menu1').mouseleave(function(){
//			$('.menu1 > span').each(function(){
//				if($(this).children().attr('class')=='on'){
//					
//					var dn = $(this).children().children('a');
//					var dn_src =  dn.find('img').attr('src');
//					if (!dn_src.match('_on.png')) {				
////						initMenu();
//						over(dn);
//						$(this).removeAttr('on');
//						// 2depth
//						if(dn.next().length > 0){
//							dn.next().stop(true,true).fadeOut();
//						}
//					}
//					$(this).children('a').trigger('mouseover');
//				}
//			});
//		});
//		
//		// 서브메뉴 롤오버 효과
//		$('.top > menu2').hover(function(){
//			over($(this));
//		},function(){
//			if($(this).parent().attr('class')!='on'){
//				out($(this));
//			}
//		});
//		
//		// 메뉴가 선택 되었을 경우 class='on' 스타일
//		$('.top > span').each(function(){
//			if($(this).attr('class') == 'on'){
//				$(this).children('a').trigger('mouseover');
//			}
//		});
//	},
	loadMainContent : function() 	// 로그인 후  메인페이지 및 ADCsmart Logo 선택 페이지
	{
//		adcSetting.loadContent();
		adcSetting.setGroupIndex(0);
		adcMonitor.loadListContent();
		adcSetting.setObjOnAdcChange(monitorAppliance);
		$('.menu2').addClass('none');
		$(".monitorSub").removeClass('none');
		
		
//		with (this) 
//		{			
//			ajaxManager.runHtml({
//				url : "layout/loadContent.action",				
//				target: "#wrap .contents",
//				successFn : function(params) 
//				{
//					adcSetting.loadContent();
//					if(!adcSetting.isAdcSet())
//					{
//						adcMonitor.loadListContent();
//					}
//					else
//					{
//						monitorAppliance.loadApplianceMonitorContent();
//					}
//					adcSetting.setObjOnAdcChange(monitorAppliance);	
//				}
//			});
//		}
	},	
	
	loadContent: function() 	// 대체할 수 있는 화면이 없는 경우 이동
	{
		with (this) 
		{			
//			ajaxManager.runHtml({
			ajaxManagerOB.runHtmlExt({
				url : "layout/loadContent.action",				
				target: "#wrap .contents",
				successFn : function(params) 
				{
//					header.setActiveMenu('Monitor');
				}
			});
		}
	},	
	
	loadLoginCheck : function() 
	{
		with (this) 
		{
			ajaxManagerOB.runJsonExt({
				url : "layout/loadLoginCheck.action",
				data : {},
				target : ".header",
				successFn : function(data) 
				{		
					if(!data.isSuccessful)
					{
						$.obAlertErrorClose(true);
					}
				},
				errorFn : function(a,b,c)
				{
					$.obAlertErrorClose(true);
				}
			});		
		}		
	},	
	loadLeftNavi : function()
	{
		with(this)
		{
			ajaxManager.runHtml({
				url : "layout/loadLeftNavi.action",
				target : "#wrap .nav",
				successFn : function(params)
				{
					registerEvents();	
				}
			});
		}
	},
	loadPickView : function()
	{	
		with(this)
		{
//			ajaxManager.runHtml({
			ajaxManagerOB.runHtmlExt({
				url : "layout/loadPickView.action",
//				target : "#wrap .pickView",
				target : ".LocationNavi",
				successFn : function(params)
				{
					
				}
			});
		}			
	},
	retrievePickView : function(group, adc, targetKey)
	{
		with(this)
		{
//			ajaxManager.runHtml({
			ajaxManagerOB.runHtmlExt({
				url : "layout/loadPickView.action",
//				target : "#wrap .pick",
				target : ".LocationNavi",
				data :
				{
				},
				successFn : function(params)
				{
					if(targetKey == 2)
					{
						registerPickView(group, adc);
					}
					else
					{
						registerPickViewGroup(group, targetKey);
					}
				}
			});
		}		
	},
	loadAccountRole : function()
	{
		with(this)
		{
			ajaxManager.runJsonExt({
				url : "base.action",
				successFn : function(data) 
				{
					setAccountRole(data.accountRole);
				},
				errorFn : function(a, b, c)
	            {
	            }
			});
		}
	},
	loadAdcSearchBar : function()
	{
		with(this)
		{
			ajaxManager.runHtml({
				url : "layout/loadAdcSearchBar.action",
				target : "#wrap .adcSearch",
				successFn : function(params)
				{					
				}
			});
		}		
	},
	registerPickView : function(group, adc)
	{
//		var adcTypeLogoCss = '';
//		if (adc == null)
//		{
//			return;
//		}
//		if (adc.type == "Alteon")
//		{
//			adcTypeLogoCss = "adclogoAlteon";
//		}
//		else if (adc.type == "F5")
//		{
//			adcTypeLogoCss = "adclogoF5";
//		}
//		else if (adc.type == "PAS")
//		{
//			adcTypeLogoCss = "adclogoPas";
//		}
//		else if ( adc.type == "PASK")
//		{
//			adcTypeLogoCss = "adclogoPask";
//		}
		$('.middle').removeClass('none');
		
		var $pickdiv = $('.LocationNavi').filter(':last');
		$pickdiv.empty();
		var html = ''; 		
		
		html += '<span><img src="imgs/layout/icon_folder_off.gif">&nbsp;<span class="GropName">' + group + '</span></span>';
		html += '<span class="middle"> > </span>';
//		html += '<span class="' + adcTypeLogoCss + '"></span>';
		html +=	'<span class="adcname">' + adc.name+'</span>';
		
		$pickdiv.append(html);
	},	
	registerPickViewGroup : function(groupName, targetKey)
	{	
		var $pickdiv = $('.LocationNavi').filter(':last');
		$pickdiv.empty();
		var html = '';
		
		if(targetKey == 0)
		{
			html += '<span><img src="imgs/layout/icon_folder_off.gif">&nbsp;<span class="GropName">전체</span></span>';
			
		}
		else
		{
			if (groupName == null)
			{
				return;
			}		
			html += '<span><img src="imgs/layout/icon_folder_off.gif">&nbsp;<span class="GropName">' + groupName + '</span></span>';		
		}		
		
		$pickdiv.append(html);
	},
	
	openSubMenu : function()
	{
		$('.menu1').parent().find('ul').css('visibility', 'visible');
	},
	
	registerEvents : function() 
	{
		with(this)
		{
/*			
			$('.menu1 > span').mouseover(function(){
				var dn = $(this).children('a');
				var dn_src =  dn.find('img').attr('src');
				if (!dn_src.match('_on.png')) {				
					initMenu();
					over(dn);
					$(this).attr('class', 'on');
					// 2depth
					if(dn.next().length > 0){
						dn.next().stop(true,true).fadeIn();
					}
				}
			});
			
			// 1 depth 메뉴와 2 depth 메뉴의 영역을 벗어 날 경우 이벤트
			$('.menu1 > span').mouseleave(function(){
				$('.menu1 > span').each(function(){
					if($(this).attr('class')=='on')
					{						
						var dn = $(this).children('a');
						var dn_src =  dn.find('img').attr('src');
						if (!dn_src.match('_on.png')) {				
//							initMenu();
							out(dn);
							$(this).removeAttr('on');
							$(this).removeClass('on');
							// 2depth
							if(dn.next().length > 0){
								dn.next().stop(true,true).fadeOut();
							}
						}
//						$(this).children('a').trigger('mouseover');
					}
				});
			});
			
			// 서브메뉴 롤오버 효과
			$('.menu1 .menu2').hover(function(){
				over($(this));
			},function(){
				if($(this).parent().attr('class')!='on'){
					out($(this));
				}
			});
			
			// 메뉴가 선택 되었을 경우 class='on' 스타일
			$('.menu1 > span').each(function(){
				if($(this).attr('class') == 'on'){
					$(this).children('a').trigger('mouseover');
				}
			});
			
*/			
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
		
		// Header logo 클릭시 발생하는 이벤트
		$('#header_logo').click(function()
		{
			header.loadMainContent();
			if($('.listcontainer_open').parent().hasClass("none"))
				$('.listcontainer_open').click();
			
			$('.systemSub').addClass('none');
			$('.toolSub').addClass('none');
			$('.LocationNavi').removeClass('none');			
		});	
		
// 1depth 메뉴 롤오버 효과
		
		$('.menu1 > span').mouseover(function(){
			
//			$('.menu2').addClass('none');
			
			$('.menu1 span a').find('div id').each(function(index, value) {
//				console.log(value);
//			   var dn = value;
			    if(!value.match('Select'))
				{
					$('.menu2').addClass('none');
				}
				else
				{
					$('.menu2').removeClass('none');
				}
//			   console.log(index, value);
			});
			
//			var dn = $(this).children('a');
//			var dn_src =  dn.find('img').attr('src');
//			if (!dn_src.match('_on.png')) {				
//				initMenu();
//				over(dn);
//				$(this).attr('class', 'on');
//				// 2depth
//				if(dn.next().length > 0){
//					dn.next().stop(true,true).fadeIn();
//				}
//			}
		});
		
//		$("#dashboardMnu").mouseover(function(){
//			$(".monitorSub").addClass('none');
//			$(".slbSettingSub").addClass('none');
//			$(".systemSub").addClass('none');
//			$(".faultSub").addClass('none');
//			$(".logSub").addClass('none');
//		});
//		$("#logMnu").mouseover(function(){
//			$(".monitorSub").addClass('none');
//			$(".slbSettingSub").addClass('none');
//			$(".systemSub").addClass('none');
//			$(".faultSub").addClass('none');
//			$(".logSub").removeClass('none');
//		});
//		$("#reportMnu").mouseover(function(){
//			$(".monitorSub").addClass('none');
//			$(".slbSettingSub").addClass('none');
//			$(".systemSub").addClass('none');
//			$(".faultSub").addClass('none');
//			$(".logSub").addClass('none');
//		});
//				
//		$("#monitorMnu").mouseover(function(){
//			$(".monitorSub").removeClass('none');
//			$(".slbSettingSub").addClass('none');
//			$(".systemSub").addClass('none');
//			$(".faultSub").addClass('none');
//			$(".logSub").addClass('none');
//		});
//
//		$("#monitorMnu").mouseleave(function () {
////			$('.top .menu1').each(function(){
////				if($(this).attr('class')=='on'){
////					$(this).children('a').trigger('mouseover');
////				}
////			});
//			$(".monitorSub").addClass('none');
////			$(".monitorSub").slideUp('slow');
//		});
		
		$("#dashboardMnu").click(function(e)
		{
			$(".dashSub").removeClass('none');
			$(".monitorSub").addClass('none');		
			$(".slbSettingSub").addClass('none');
			$(".systemSub").addClass('none');
			$(".toolSub").addClass('none');
			$(".faultSub").addClass('none');
			$(".slbSettingSubSub").addClass('none');
			$(".adcSettingSubSub").addClass('none');
			$(".logSub").addClass('none');
		});
		
		
		$("#monitorMnu").click(function(e)
		{
			$(".dashSub").addClass('none');
			$(".monitorSub").removeClass('none');
//			$(".monitorSub ul").find('li:first').attr("class", "select");		
			$(".slbSettingSub").addClass('none');
			$(".systemSub").addClass('none');
			$(".toolSub").addClass('none');
			$(".faultSub").addClass('none');
			$(".slbSettingSubSub").addClass('none');
			$(".adcSettingSubSub").addClass('none');
			$(".logSub").addClass('none');
		});
				
		$("#faultMgmtMnu").click(function()
		{
			$(".dashSub").addClass('none');
			$(".monitorSub").addClass('none');
			$(".slbSettingSub").addClass('none');
			$(".systemSub").addClass('none');
			$(".toolSub").addClass('none');
			$(".faultSub").removeClass('none');
			$(".slbSettingSubSub").addClass('none');
			$(".adcSettingSubSub").addClass('none');
			$(".logSub").addClass('none');
		});
		
		$("#logMnu").click(function()
		{
			$(".dashSub").addClass('none');
			$(".monitorSub").addClass('none');
			$(".slbSettingSub").addClass('none');
			$(".systemSub").addClass('none');
			$(".toolSub").addClass('none');
			$(".faultSub").addClass('none');
			$(".slbSettingSubSub").addClass('none');
			$(".adcSettingSubSub").addClass('none');
			$(".logSub").removeClass('none');
		});
		
		$("#slbMgmtMnu").click(function()
		{
			$(".dashSub").addClass('none');
			$(".monitorSub").addClass('none');
			$(".slbSettingSub").removeClass('none');
			$(".systemSub").addClass('none');
			$(".toolSub").addClass('none');
			$(".faultSub").addClass('none');
			$(".slbSettingSubSub").addClass('none');
			$(".adcSettingSubSub").addClass('none');
			$(".logSub").addClass('none');
		});
		
		// Left Navigation 데시보드 클릭시 발생하는 이벤트
		/*
		$('.dashboardMnu').click(function(e) 
		{
//			var DashboardPopup = dashboardLoader.loadDashboard();
//			setDashPopup(DashboardPopup);
			
			e.preventDefault();
//			$('.contents').css('top', '70px');			
			
			$('.snb_close').click();
			$('.listcontainer_open').parent().addClass('none');
			
			$(".dashboardMnu").find('div').attr('id', 'dashboardimgSelect');
			$(".faultMgmtMnu").find('div').attr('id', 'faultMgmtimg');
			$(".monitorMnu").find('div').attr('id', 'monitorimg');
			$(".logMnu").find('div').attr('id', 'logimg');
			$(".slbMgmtMnu").find('div').attr('id', 'slbMgmtimg');
			$(".reportMnu").find('div').attr('id', 'reportimg');
			$(".sysSettingMnu").find('div').attr('id', 'sysSettingimg');
			$(".toolMnu").find('div').attr('id', 'systoolsimg');
			
			$(".menu2").addClass('none');
			$('.LocationNavi').addClass('none');
			
//			header.setActiveMenu('DynamicDashboard');
//			dashboardLoader.loadAdcSummaryDashboard();			
		});	
		*/
		$("#dashboardMnu").click(function(){
			$(".dashSub").removeClass('none');
			$(".monitorSub").addClass('none');
			$(".slbSettingSub").addClass('none');
			$(".systemSub").addClass('none');
			$(".toolSub").addClass('none');
			$(".faultSub").addClass('none');
			$(".slbSettingSubSub").addClass('none');
			$(".adcSettingSubSub").addClass('none');
			$(".logSub").addClass('none');
			$('.LocationNavi').addClass('none');
		});
		
		$("#sysSettingMnu").click(function(){
			$(".dashSub").addClass('none');
			$(".monitorSub").addClass('none');
			$(".slbSettingSub").addClass('none');
			$(".systemSub").removeClass('none');
			$(".toolSub").addClass('none');
			$(".faultSub").addClass('none');
			$(".slbSettingSubSub").addClass('none');
			$(".adcSettingSubSub").addClass('none');
			$(".logSub").addClass('none');
		});
		$("#toolMnu").click(function(){
			$(".dashSub").addClass('none');
			$(".monitorSub").addClass('none');
			$(".slbSettingSub").addClass('none');
			$(".systemSub").addClass('none');
			$(".toolSub").removeClass('none');
			$(".faultSub").addClass('none');
			$(".slbSettingSubSub").addClass('none');
			$(".adcSettingSubSub").addClass('none');
			$(".logSub").addClass('none');
		});
		/*
		$('.slbSettingSub .abcSettingMnu').click(function(){
			$('.abcSettingSubSub').removeClass('none');
		});
		*/
		$('.slbSettingSub .slbSettingMnu').click(function(){
			$(".adcSettingSubSub").addClass('none');
			
//			if(adcSetting.getAdc().type == "F5")
//				$('.slbSettingSubSub').removeClass('none');
//			else
//				$('.slbSettingSubSub').addClass('none');
			
//			$('.slbSettingSubSub').removeClass('none');
//			if(adcSetting.getAdc().type == "F5")
//			{				
//				$('.slbProfile').removeClass('none');
//				$('.slbNotice').removeClass('none');
//			}
//			else if(adcSetting.getAdc().type == "Alteon")
//			{
////				$('.slbSettingSubSub').addClass('none');
//				$('.slbProfile').addClass('none');
//				$('.slbNotice').addClass('none');
//			}
//			else
//			{
//				$('.slbSettingSubSub').addClass('none');
//			}
			
			$('.slbSettingSubSub').removeClass('none');
			if(adcSetting.getAdc().type == "F5")
			{				
				$('.slbProfile').removeClass('none');
				$('.slbNotice').removeClass('none');
			}
			else if (adcSetting.getAdc().type == "Alteon")
			{
				$('.slbProfile').addClass('none');
				$('.slbNotice').removeClass('none');
			}
			else 
			{
				$('.slbProfile').addClass('none');
				$('.slbNotice').addClass('none');
			}
		});
		
		// Left Navigation 모니터링 클릭시 발생하는 이벤트
		$('.monitorMnu').click(function(e)
		{
			e.preventDefault();			
//			header.loadContent();
			header.setActiveMenu('MonitorMnu');
//			$('.listcontainer_open').click();
			/*
			if ($('.groupNameArea').size() == 0)
			{
//				adcSetting.loadContent();
//				monitorAppliance.loadApplianceMonitorContent();
				adcSetting.setObjOnAdcChange(monitorAppliance);
				header.loadContent();
//				header.setActiveMenu('MonitorAppliance');
				header.setActiveMenu('MonitorMnu');
			}
			else
			{
				adcSetting.loadContent();
				if(!adcSetting.isAdcSet())
				{
					adcMonitor.loadListContent();
				}
				else
				{
					monitorAppliance.loadApplianceMonitorContent();
				}
				adcSetting.setObjOnAdcChange(monitorAppliance);			
			}
//			if (!adcSetting.isAdcSet())
//			{
//				adcSetting.loadContent();
//				header.loadContent();
//				adcSetting.setObjOnAdcChange(monitorAppliance);
//				header.setActiveMenu('MonitorAppliance');
//			}
//			else
//			{
//				adcSetting.setSelectIndex(2);
//				registerPickView(adcSetting.getAdc());		
//				adcSetting.loadContent();
//				monitorAppliance.loadListContent();
//				adcSetting.setObjOnAdcChange(monitorAppliance);
//			}	
*/		});
		// 모니터링 : VS요약 클릭시 발생하는 이벤트
		$('.monitorNetworkMnu').click(function(e) 
		{
			e.preventDefault();	
			if($('.listcontainer_open').parent().hasClass("none"))
				$('.listcontainer_open').click();
			
			if (!adcSetting.isAdcSet())
			{
//				adcSetting.loadContent();
				header.loadContent();
				adcSetting.setObjOnAdcChange(networkMap);
				header.setActiveMenu('MonitorNetwork');
			}
			else
			{
				adcSetting.loadContent();
				networkMap.loadNetworkMapContent(adcSetting.getAdc(), 0, undefined, undefined, taskQ);
				adcSetting.setObjOnAdcChange(networkMap);
			}
		});
		// 모니터링 : 인터페이스 클릭시 발생하는 이벤트
		$('.monitorStatisticsMnu').click(function(e) 
		{
			e.preventDefault();	
			if($('.listcontainer_open').parent().hasClass("none"))
				$('.listcontainer_open').click();
			
			if (!adcSetting.isAdcSet())
			{
//				adcSetting.loadContent();
				header.loadContent();
				adcSetting.setObjOnAdcChange(statistics);
				header.setActiveMenu('MonitorStatistics');
			}
			else
			{
				adcSetting.loadContent();
				statistics.loadStatisticsContent(adcSetting.getAdc());
				adcSetting.setObjOnAdcChange(statistics);
			}
		});
		
		$('.logMnu').click(function(e)
		{
			e.preventDefault();			
			header.setActiveMenu('LogMnu');
		});
		// 모니터링 : 장애 클릭시 발생하는 이벤트
		$('.monitorFaultMnu').click(function(e) 
		{
			e.preventDefault();	
			if($('.listcontainer_open').parent().hasClass("none"))
				$('.listcontainer_open').click();
			
			if(!adcSetting.isAdcSet())
			{
				faultMonitoring.loadListContent();
				adcSetting.setObjOnAdcChange(faultMonitoring);
			}
			else
			{
				adcSetting.setGroupIndex(undefined);
				faultMonitoring.loadListContent();
				adcSetting.setObjOnAdcChange(faultMonitoring);
			}			

		});	
		// 모니터링 : 경보 클릭시 발생하는 이벤트
		$('.monitorAlertMnu').click(function(e) 
		{
			e.preventDefault();	
			if($('.listcontainer_open').parent().hasClass("none"))
				$('.listcontainer_open').click();
			if(!adcSetting.isAdcSet())
			{
				alertMonitoring.loadListContent();
				adcSetting.setObjOnAdcChange(alertMonitoring);
			}
			else
			{
				adcSetting.setGroupIndex(undefined);
				alertMonitoring.loadListContent();
				adcSetting.setObjOnAdcChange(alertMonitoring);
			}

		});	
		// 모니터링 : ADC로그 클릭시 발생하는 이벤트
		$('.adcLogMnu').click(function(e) 
		{
			e.preventDefault();	
			if($('.listcontainer_open').parent().hasClass("none"))
				$('.listcontainer_open').click();
			
			if(!adcSetting.isAdcSet())
			{
				adcLog.loadListContent();
				adcSetting.setObjOnAdcChange(adcLog);
			}
			else
			{
				adcSetting.setGroupIndex(undefined);
				adcLog.loadListContent();
				adcSetting.setObjOnAdcChange(adcLog);
			}
		});
		// 모니터링 : 장비 클릭시 발생하는 이벤트
		$('.monitorApplianceMnu').click(function(e)
		{
			e.preventDefault();
			if($('.listcontainer_open').parent().hasClass("none"))
				$('.listcontainer_open').click();
			/*
			if ($('.groupNameArea').size() == 0)
//			if (!adcSetting.isAdcSet())
			{
//				adcSetting.loadContent();				
				header.loadContent();
//				monitorAppliance.loadApplianceMonitorContent();
				adcSetting.setObjOnAdcChange(monitorAppliance);
				header.setActiveMenu('MonitorAppliance');
			}
			else
			{
//				adcSetting.loadContent();
				if(!adcSetting.isAdcSet())// || adcSetting.getGroupIndex() != undefined)
				{
					adcMonitor.loadListContent();
				}
				else
				{
					monitorAppliance.loadApplianceMonitorContent();
				}
				adcSetting.setObjOnAdcChange(monitorAppliance);
//				header.setActiveMenu('MonitorAppliance');
			}					
			*/
			if(!adcSetting.isAdcSet())
			{
				adcMonitor.loadListContent();
				adcSetting.setObjOnAdcChange(monitorAppliance);
			}
			else
			{
				adcSetting.setGroupIndex(undefined);
				monitorAppliance.loadApplianceMonitorContent();
				adcSetting.setObjOnAdcChange(monitorAppliance);
			}
		});	
		// 모니터링 : 서비스성능 클릭시 발생하는 이벤트
		$('.monitorServicePerfomanceMnu').click(function(e)
		{
			e.preventDefault();
			if($('.listcontainer_open').parent().hasClass("none"))
				$('.listcontainer_open').click();
			
			/*
			if ($('.groupNameArea').size() == 0)
			{
//				adcSetting.loadContent();				
				header.loadContent();
				adcSetting.setObjOnAdcChange(monitorServicePerfomance);
				header.setActiveMenu('MonitorServicePerfomance');
			}
			else
			{
//				adcSetting.loadContent();
				if(!adcSetting.isAdcSet())// || adcSetting.getGroupIndex() != undefined)
				{
					serviceMonitor.loadListContent();
				}
				else
				{
					monitorServicePerfomance.loadServicePerfomanceContent();
				}
				adcSetting.setObjOnAdcChange(monitorServicePerfomance);
			}
			*/
			if(!adcSetting.isAdcSet())
			{
				serviceMonitor.loadListContent();
				adcSetting.setObjOnAdcChange(monitorServicePerfomance);
			}
			else
			{
				adcSetting.setGroupIndex(undefined);
				monitorServicePerfomance.onAdcChange(); // 초기화 필요함.
				adcSetting.setObjOnAdcChange(monitorServicePerfomance);				
			}
		});
		// 모니터링 : Group
		$('.monitorGroupMnu').click(function(e)
		{
			e.preventDefault();
			if($('.listcontainer_open').parent().hasClass("none"))
				$('.listcontainer_open').click();
			
			if(!adcSetting.isAdcSet())
			{
				groupMonitor.loadListContent();
				adcSetting.setObjOnAdcChange(groupMonitor);
			}
			else
			{
				adcSetting.setGroupIndex(undefined);
				groupMonitor.loadListContent();
				adcSetting.setObjOnAdcChange(groupMonitor);
			}
		});
		// 모니터링 : RealServer
		$('.monitorRealServerMnu').click(function(e)
		{
			e.preventDefault();
			if($('.listcontainer_open').parent().hasClass("none"))
				$('.listcontainer_open').click();
			
			if(!adcSetting.isAdcSet())
			{
				realServerMonitor.loadListContent();
				adcSetting.setObjOnAdcChange(realServerMonitor);
			}
			else
			{
				adcSetting.setGroupIndex(undefined);
				realServerMonitor.loadListContent();
				adcSetting.setObjOnAdcChange(realServerMonitor);
			}
/*			
//			if ($('.groupNameArea').size() == 0)
			if (adcSetting.getSelectIndex() == 2 && adcSetting.isAdcSet())
			{
//				adcSetting.loadContent();				
				header.loadContent();
				adcSetting.setGroupIndex(undefined);
				adcSetting.setObjOnAdcChange(realServerMonitor);
				header.setActiveMenu('MonitorRealServer');
			}
			else
			{	
//				adcSetting.loadContent();
				if(!adcSetting.isAdcSet())
				{
					realServerMonitor.loadListContent();
					adcSetting.setObjOnAdcChange(realServerMonitor);
				}
				else
				{
					realServerMonitor.loadListContent();
					adcSetting.setObjOnAdcChange();
				}
			}*/
		});
		// 모니터링 : 세션검색 클릭시 발생하는 이벤트
		$('.sessionMonitoringMnu').click(function(e)
		{
			e.preventDefault();
			if($('.listcontainer_open').parent().hasClass("none"))
				$('.listcontainer_open').click();
			
			if (!adcSetting.isAdcSet())
			{
//				adcSetting.loadContent();
				header.loadContent();
				adcSetting.setObjOnAdcChange(sessionMonitoring);
				header.setActiveMenu('SessionMonitoring');
			}
			else
			{
				adcSetting.loadContent();
				sessionMonitoring.loadListContent();
				adcSetting.setObjOnAdcChange(sessionMonitoring);
			}
			
		});
		// 모니터링 : L2검색 클릭시 발생하는 이벤트
		$('.monitorL2Mnu').click(function(e)
		{
			e.preventDefault();
			if($('.listcontainer_open').parent().hasClass("none"))
				$('.listcontainer_open').click();
			
			if (!adcSetting.isAdcSet())
			{
//				adcSetting.loadContent();
				header.loadContent();
				adcSetting.setObjOnAdcChange(monitorL2Info);
				header.setActiveMenu('MonitorL2Info');
			}
			else
			{
				adcSetting.loadContent();
				monitorL2Info.loadL2SearchContent();
				adcSetting.setObjOnAdcChange(monitorL2Info);
			}			
		});		
		
		// Left Navigation ADC관리 클릭시 발생하는 이벤트
		$('.adcMgmtMnu').click(function(e) 
		{
			e.preventDefault();			
			if($('.listcontainer_open').parent().hasClass("none"))
				$('.listcontainer_open').click();
			
//			alert($('.groupNameArea').size());
//			if (!adcSetting.isAdcSet())
			if ($('.groupNameArea').size() == 0)
			{
				adcSetting.loadContent();
				adcSetting.loadAdcAddContent();
				adcSetting.setObjOnAdcChange(adcSetting);
				header.setActiveMenu('AdcSetting');
//				header.setActiveContent('AdcAddContent');
			}
			else
			{
				adcSetting.loadContent();
				adcSetting.loadAdcListContent();
				adcSetting.setObjOnAdcChange(adcSetting);
			}
		});
		
		$('.adcSettingMnu').click(function(e) 
		{		
			e.preventDefault();		
			if($('.listcontainer_open').parent().hasClass("none"))
			{
				$('.listcontainer_open').click();
			}
			
			$('.slbSettingSubSub').addClass('none');
				
			if(adcSetting.getSelectIndex() == 2)
				$('.adcSettingSubSub').removeClass('none');
			else
				$('.adcSettingSubSub').addClass('none');
			
			if (adcSetting.isAdcSet())
//			if (adcSetting.isAdcSet() && (adcSetting.getGroupIndex() ==0 || adcSetting.getGroupIndex() == undefined))
			{
				adcSetting.loadContent();
//				adcSetting.loadAdcAddContent();
				adcSetting.loadAdcModifyContent(adcSetting.adc.index, adcSetting.adc.type);
				adcSetting.setObjOnAdcChange(adcSetting);
				header.setActiveMenu('AdcSetting');
//				header.setActiveContent('AdcAddContent');				
			}
			else
			{
//				adcSetting.loadContent();
				adcSetting.loadAdcListContent();
				adcSetting.setObjOnAdcChange(adcSetting);
			}
		});
		
		//adc modify
		$('.adcModify').click(function(e)
		{
			e.preventDefault();	
			
			if (!adcSetting.isAdcSet() || $('.listcontainer .adcBlock.on:visible').length < 1)
			{
//				adcSetting.loadContent();
				header.loadContent();
				adcSetting.setObjOnAdcChange(adcSetting);
				header.setActiveMenu('AdcSetting');
			}
			else
			{
//				e.preventDefault();
//				registerPickView(adcSetting.getAdc());
				adcSetting.loadContent();
				header.setAdcSettingTap(0);
				adcSetting.loadAdcModifyContent(adcSetting.adc.index, adcSetting.adc.type);
				adcSetting.setObjOnAdcChange(adcSetting);
				
				$(".adcSettingSubSub").find('a').removeClass("choice");					
				$(".adcSettingSubSub").find('.adcModify a').attr("class", "choice");
			}
		});
		
		//adc connect
		$('.adcConnect').click(function(e)
		{
			e.preventDefault();	
			
			if (!adcSetting.isAdcSet() || $('.listcontainer .adcBlock.on:visible').length < 1)
			{
//				adcSetting.loadContent();
				header.loadContent();
				adcSetting.setObjOnAdcChange(adcSetting);
				header.setActiveMenu('AdcSetting');
			}
			else
			{
//				e.preventDefault();
//				registerPickView(adcSetting.getAdc());
				adcSetting.loadContent();
				header.setAdcSettingTap(1);
								
//				var opMode = 2;//$('.listcontainer').find('.opMode').val();				
//				configCheck.loadContent(adcSetting.adc.index, adcSetting.adc.type, adcSetting.mode);
				adcSetting.setObjOnAdcChange(adcSetting);
				
				$(".adcSettingSubSub").find('a').removeClass("choice");					
				$(".adcSettingSubSub").find('.adcConnect a').attr("class", "choice");				
			}
		});

		//경보설정 Click Event
		$('.adcAlertMnu').click(function(e) 
		{
			e.preventDefault();	
			if($('.listcontainer_open').parent().hasClass("none"))
				$('.listcontainer_open').click();
			$('.menu3').addClass('none');
			
			// 개별
			if (adcSetting.isAdcSet())
			{
				adcAlert.loadAdcAlertListContent();
				adcSetting.setObjOnAdcChange(adcAlert);	
			}
			else
			{				
				if (adcSetting.getGroupIndex() > 0 )
				{				
					groupAlert.loadGroupAlertListContent();
					adcSetting.setObjOnAdcChange(adcAlert);	
				}
				else
				{
					adcSetting.loadContent();
					groupAlert.loadGroupAlertListContent();
					adcSetting.setObjOnAdcChange(adcAlert);	
				}
			}	
			
			
			
			//if (!adcSetting.isAdcSet())
//			if ($('.groupNameArea').size() == 0)
			/*if (adcSetting.getSelectIndex() == 2 && adcSetting.isAdcSet())
			{
//				adcSetting.loadContent();
//				adcSetting.setObjOnAdcChange(adcAlert);
//				header.loadContent();
//				header.setActiveMenu('AdcAlert');
				adcSetting.loadContent();
				adcAlert.loadAdcAlertListContent();
				adcSetting.setObjOnAdcChange(adcAlert);	
			}
			else
			{
				if(adcSetting.getGroupIndex() === undefined && adcSetting.adc.index === undefined)
				{
					adcSetting.loadContent();
					groupAlert.loadGroupAlertListContent();
					adcSetting.setObjOnAdcChange(adcAlert);								
				}
				else if (adcSetting.getGroupIndex() === undefined)
				{
					adcSetting.loadContent();
					adcAlert.loadAdcAlertListContent();
					adcSetting.setObjOnAdcChange(adcAlert);	
				}
				else
				{
					adcSetting.loadContent();
					groupAlert.loadGroupAlertListContent();
					adcSetting.setObjOnAdcChange(adcAlert);	
				}
			}*/
		});
		
		// Left Navigation SLB관리 클릭시 발생하는 이벤트
		$('.slbMgmtMnu').click(function(e)
		{
			e.preventDefault();	
//			header.loadContent();
			header.setActiveMenu('SlbMnu');
//			$('.listcontainer_open').click();
			/*
			if ($('.groupNameArea').size() == 0)
			{
//				adcSetting.loadContent();
				adcSetting.setObjOnAdcChange(adcSetting);
				header.loadContent();
//				header.setActiveMenu('AdcSetting');
				header.setActiveMenu('SlbMnu');
			}			
			else
			{
				adcSetting.loadContent();
				adcSetting.loadAdcListContent();
				adcSetting.setObjOnAdcChange(adcSetting);
			}*/
		});		
		
		$('.slbScheduleMnu').click(function(e)
		{
			e.preventDefault();
			if ($('.listcontainer_open').parent().hasClass("none"))
				$('.listcontainer_open').click();
			$('.menu3').addClass('none');
			if (!adcSetting.isAdcSet() || $('.listcontainer .adcBlock.on:visible').length < 1)
			{
				header.loadContent();
				adcSetting.setObjOnAdcChange(slbSchedule);
				header.setActiveMenu('SlbSchedule');
			}
			else
			{
				adcSetting.setSelectIndex(2);
				adcSetting.loadContent();
				slbSchedule.loadListContent();
				adcSetting.setObjOnAdcChange(slbSchedule);
			}
		});
		
//		$('.slbSettingSub .slbSettingMnu')
		$('.slbSettingMnu').click(function(e) 
		{		
			e.preventDefault();	
			if($('.listcontainer_open').parent().hasClass("none"))
				$('.listcontainer_open').click();
			
			// GS 14.08.13 sw.jung: 그룹 선택시 초기화면 로드
			
			// 개별
//			if (adcSetting.isAdcSet())
//			{
//				node.loadListContent();
//				adcSetting.setObjOnAdcChange(node);	
//			}
//			else
//			{				
//				if (adcSetting.getGroupIndex() > 0 )
//				{				
//					groupNode.loadListContent();
//					adcSetting.setObjOnAdcChange(node);	
//				}
//				else
//				{
//					adcSetting.loadContent();
//					groupAlert.loadGroupAlertListContent();
//					adcSetting.setObjOnAdcChange(adcAlert);	
//				}
//			}	
						
//			if (!adcSetting.isAdcSet())
			if (!adcSetting.isAdcSet() || $('.listcontainer .adcBlock.on:visible').length < 1)
			{
//				adcSetting.loadContent();
				
				$('.slbSettingSubSub').removeClass('none');
								
				if(header.getVsSettingTap() == 2)
				{
//					node.loadListContent();
//					adcSetting.setObjOnAdcChange(node);
					if (adcSetting.getGroupIndex() > 0 )
					{				
						groupNode.loadListContent();
						adcSetting.setObjOnAdcChange(node);	
					}
					else
					{
						adcSetting.loadContent();
						groupNode.loadListContent();
						adcSetting.setObjOnAdcChange(node);	
					}
				}
				else 
				{
//					header.loadContent();
					virtualServer.loadListContent();
					adcSetting.setObjOnAdcChange(virtualServer);
				}
				
				
				header.setActiveMenu('SlbSetting');
			}
			else
			{
//				e.preventDefault();
//				registerPickView(adcSetting.getAdc());
//				registerPickView(group, adc, targetKey);
				adcSetting.setSelectIndex(2);
				adcSetting.loadContent();
				
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
//					$('.slbSettingSubSub').addClass('none');
					$('.slbProfile').addClass('none');
					$('.slbRs').addClass('none');
					$('.slbNotice').addClass('none');
				}
				
				if(adcSetting.getAdc().type == "Alteon")
				{
					if(header.getVsSettingTap() == 2)
					{
						node.loadListContent();
					}
					else if (header.getVsSettingTap() == 3)
					{
						noticeGrp.loadListContent();
					}
					else
					{
						virtualServer.loadListContent();
					}
				}
				else if(adcSetting.getAdc().type == "F5")
				{
					if(header.getVsSettingTap() == 0)
					{
						virtualServer.loadListContent();
					}
					else if(header.getVsSettingTap() == 1)
					{
						profile.loadProfileListContent();
					}
					else if(header.getVsSettingTap() == 2)
					{
						node.loadListContent();
					}
					else if (header.getVsSettingTap() == 3)
					{
						noticeGrp.loadListContent();
					}
				}
				else
				{
					virtualServer.loadListContent();
				}
				adcSetting.setObjOnAdcChange(virtualServer);
			}
		});
		
		$('.slbVs').click(function(e)
		{
			e.preventDefault();	
			
			header.setVsSettingTap(0);
			if (!adcSetting.isAdcSet() || $('.listcontainer .adcBlock.on:visible').length < 1)
			{
//				adcSetting.loadContent();
//				header.loadContent();
				virtualServer.loadListContent();
				adcSetting.setObjOnAdcChange(virtualServer);
				header.setActiveMenu('SlbSetting');
			}
			else
			{
//				e.preventDefault();
//				registerPickView(adcSetting.getAdc());
//				adcSetting.loadContent();
//				header.setVsSettingTap(0);
				virtualServer.loadListContent();
				adcSetting.setObjOnAdcChange(virtualServer);
				
//				$(".slbSettingSubSub").find('a').removeClass("choice");					
//				$(".slbSettingSubSub").find('.slbVs a').attr("class", "choice");
			}
			
//			header.setVsSettingTap(0);
			$(".slbSettingSubSub").find('a').removeClass("choice");					
			$(".slbSettingSubSub").find('.slbVs a').attr("class", "choice");
		});
		
		$('.slbProfile').click(function(e)
		{
			e.preventDefault();	
			
			if (!adcSetting.isAdcSet() || $('.listcontainer .adcBlock.on:visible').length < 1)
			{
//				adcSetting.loadContent();
				header.loadContent();
				adcSetting.setObjOnAdcChange(virtualServer);
				header.setActiveMenu('SlbSetting');
			}
			else
			{
//				e.preventDefault();
//				registerPickView(adcSetting.getAdc());
				adcSetting.loadContent();
				header.setVsSettingTap(1);
				profile.loadProfileListContent();
				adcSetting.setObjOnAdcChange(virtualServer);
				
//				$(".slbSettingSubSub ul").find('a').removeClass("choice");					
//				$(".slbSettingSub ul:eq(1)").find('.slbProfile').attr("class", "choice");			
				$(".slbSettingSubSub").find('a').removeClass("choice");					
				$(".slbSettingSubSub").find('.slbProfile a').attr("class", "choice");
			}
		});
		
		$('.slbRs').click(function(e)
		{
			e.preventDefault();	
			header.setVsSettingTap(2);
			if (!adcSetting.isAdcSet() || $('.listcontainer .adcBlock.on:visible').length < 1)
			{
//				adcSetting.loadContent();
//				node.loadListContent();
//				adcSetting.setObjOnAdcChange(node);
//				header.setActiveMenu('SlbSetting');
				
				if (adcSetting.getGroupIndex() > 0 )
				{				
					groupNode.loadListContent();
					adcSetting.setObjOnAdcChange(node);	
					header.setActiveMenu('SlbSetting');
				}
				else
				{
					adcSetting.loadContent();
					groupNode.loadListContent();
					adcSetting.setObjOnAdcChange(node);	
				}
			}
			else
			{
//				e.preventDefault();
//				registerPickView(adcSetting.getAdc());
				adcSetting.loadContent();
//				header.setVsSettingTap(2);
				node.loadListContent();
				adcSetting.setObjOnAdcChange(node);
				
//				$(".slbSettingSubSub ul").find('a').removeClass("choice");
//				$(".slbSettingSubSub ul:eq(1)").find('.slbRs').attr("class", "choice");
			}
			
//			header.setVsSettingTap(2);
			$(".slbSettingSubSub").find('a').removeClass("choice");					
			$(".slbSettingSubSub").find('.slbRs a').attr("class", "choice");
		});
		
		$('.slbNotice').click(function(e)
		{
			e.preventDefault();	
			
			if (!adcSetting.isAdcSet() || $('.listcontainer .adcBlock.on:visible').length < 1)
			{
//				adcSetting.loadContent();
				header.loadContent();
				adcSetting.setObjOnAdcChange(virtualServer);
				header.setActiveMenu('SlbSetting');
			}
			else
			{
//				e.preventDefault();
//				registerPickView(adcSetting.getAdc());
				adcSetting.loadContent();
				header.setVsSettingTap(3);
				noticeGrp.loadListContent();
				adcSetting.setObjOnAdcChange(virtualServer);
				
//				$(".slbSettingSubSub ul").find('a').removeClass("choice");
//				$(".slbSettingSubSub ul:eq(1)").find('.slbNotice').attr("class", "choice");
				$(".slbSettingSubSub").find('a').removeClass("choice");					
				$(".slbSettingSubSub").find('.slbNotice a').attr("class", "choice");
			}
		});
		
		$('.flbInfoMnu').click(function(e) 
		{		
			e.preventDefault();	
			if($('.listcontainer_open').parent().hasClass("none"))
				$('.listcontainer_open').click();
			
			if (!adcSetting.isAdcSet())
			{
//				adcSetting.loadContent();
				header.loadContent();
				adcSetting.setObjOnAdcChange(flbInfo);
				header.setActiveMenu('FlbInfo');
			}
			else
			{
//				e.preventDefault();
				adcSetting.loadContent();
				flbInfo.loadListContent();
				adcSetting.setObjOnAdcChange(flbInfo);
			}
		});
		
		$('.slbHistoryMnu').click(function(e) 
		{
			e.preventDefault();	
			if($('.listcontainer_open').parent().hasClass("none"))
				$('.listcontainer_open').click();
			$('.menu3').addClass('none');
			// GS 14.08.13 sw.jung: 그룹 선택시 초기화면 로드
//			if (!adcSetting.isAdcSet())
			if (!adcSetting.isAdcSet() || $('.listcontainer .adcBlock.on:visible').length < 1)
			{
//				adcSetting.loadContent();
				header.loadContent();
				adcSetting.setObjOnAdcChange(adcHistory);
				header.setActiveMenu('SlbHistory');
			}
			else
			{
//				e.preventDefault();
//				registerPickView(adcSetting.getAdc());
				adcSetting.loadContent();
				adcHistory.loadListContent();
				adcSetting.setObjOnAdcChange(adcHistory);
			}
		});		
		// Left Navigation 장애 진단 클릭시 발생하는 이벤트
		$('.faultMgmtMnu').click(function(e)
		{
			e.preventDefault();
//			header.loadContent();
			header.setActiveMenu('FaultMnu');
//			$('.listcontainer_open').click();
//			if (!adcSetting.isAdcSet())
//			{
////				adcSetting.loadContent();
//				header.loadContent();
//				adcSetting.setObjOnAdcChange(faultHistory);
////				header.setActiveMenu('FaultHistory');
//				header.setActiveMenu('FaultMnu');
//				$('.listcontainer_open').click();
//			}
//			else
//			{
//				adcSetting.loadContent();
//				faultHistory.loadListContent();
//				adcSetting.setObjOnAdcChange(faultHistory);
//				header.setActiveMenu('FaultHistory');
//			}
		});

		$('.faultHistoryMnu').click(function(e)
		{
			e.preventDefault();
			if($('.listcontainer_open').parent().hasClass("none"))
				$('.listcontainer_open').click();
			
//			if (!adcSetting.isAdcSet())
			if ($('.groupNameArea').size() == 0)
			{
//				adcSetting.loadContent();
				header.loadContent();
				adcSetting.setObjOnAdcChange(faultHistory);
				header.setActiveMenu('FaultHistory');
			}
			else
			{
//				adcSetting.loadContent();
				if(adcSetting.isAdcSet())
				{
					adcSetting.setSelectIndex(2);
				}
				faultHistory.loadListContent();
				adcSetting.setObjOnAdcChange(faultHistory);
				header.setActiveMenu('FaultHistory');
			}			
		});
		$('.faultAnalysisMnu').click(function(e)
		{
			e.preventDefault();
			if($('.listcontainer_open').parent().hasClass("none"))
				$('.listcontainer_open').click();
			
//			if (!adcSetting.isAdcSet())
			if ($('.groupNameArea').size() == 0)
			{
//				adcSetting.loadContent();
				header.loadContent();
				adcSetting.setObjOnAdcChange(faultAnalysis);
				header.setActiveMenu('FaultAnalysis');
			}
			else
			{
//				adcSetting.loadContent();
				if(adcSetting.isAdcSet())
				{
					adcSetting.setSelectIndex(2);
				}
				faultAnalysis.loadListContent();
				adcSetting.setObjOnAdcChange(faultAnalysis);
			}			
		});		
		
		// Left Navigation 보고서 클릭시 발생하는 이벤트
		$('.reportMnu').click(function(e) 
		{
			e.preventDefault();	
//			if (!adcSetting.isAdcSet())
			
			// 개별
			if (adcSetting.isAdcSet())
			{
				report.loadListContent();		
				adcSetting.setObjOnAdcChange(report);	
			}
			else
			{				
				if (adcSetting.getGroupIndex() > 0 )
				{				
					report.loadListContent();		
					adcSetting.setObjOnAdcChange(report);	
				}
				else
				{
					adcSetting.loadContent();
					report.loadListContent();		
					adcSetting.setObjOnAdcChange(report);	
				}
			}	
			
			
			/*if ($('.groupNameArea').size() == 0)
			{
//				adcSetting.loadContent();
				header.loadContent();
				adcSetting.setObjOnAdcChange(report);
				header.setActiveMenu('Report');				
			}
			else
			{
//				e.preventDefault();	
//				if (adcSetting.getGroupIndex == 0)				
//				{
//					adcSetting.setGroupIndex(undefined);
//				}
				adcSetting.loadContent();
				report.loadListContent();		
				adcSetting.setObjOnAdcChange(report);
			}*/
			if($('.listcontainer_open').parent().hasClass("none"))
				$('.listcontainer_open').click();
			
			$(".dashboardMnu").find('div').attr('id', 'dashboardimg');
			$(".faultMgmtMnu").find('div').attr('id', 'faultMgmtimg');
			$(".monitorMnu").find('div').attr('id', 'monitorimg');
			$(".logMnu").find('div').attr('id', 'logimg');
			$(".slbMgmtMnu").find('div').attr('id', 'slbMgmtimg');
			$(".reportMnu").find('div').attr('id', 'reportimgSelect');
			$(".sysSettingMnu").find('div').attr('id', 'sysSettingimg');
			$(".toolMnu").find('div').attr('id', 'systoolsimg');
			$(".menu2").addClass('none');
		});
		
		// Left Navigation 시스템관리 클릭시 발생하는 이벤트
		$('.sysSettingMnu').click(function(e) 
		{
//			header.loadPickViewImage();
			e.preventDefault();
//			sysSetting.loadLeftPane();
//			$('.snb_close').click();
//			$('.listcontainer_open').parent().addClass('none');
			$(".slbMgmtMnu").find('div').attr('id', 'slbMgmtimg');
			$(".dashboardMnu").find('div').attr('id', 'dashboardimg');
			$(".faultMgmtMnu").find('div').attr('id', 'faultMgmtimg');
			$(".monitorMnu").find('div').attr('id', 'monitorimg');
			$(".logMnu").find('div').attr('id', 'logimg');
			$(".slbMgmtMnu").find('div').attr('id', 'slbMgmtimg');
			$(".reportMnu").find('div').attr('id', 'reportimg');
			$(".sysSettingMnu").find('div').attr('id', 'sysSettingimgSelect');
			$(".toolMnu").find('div').attr('id', 'systoolsimg');
			$('.LocationNavi').addClass('none');
//			header.loadContent();
			header.setActiveMenu('SysSettingMnu');
//			$(".systemSub ul").find('a').removeClass("select");
//			$(".systemSub ul").find('a:first').attr("class", "select");
//			sysSetting.loadUserListContent();
//			adcSetting.setObjOnAdcChange();
		});
		
		$('.toolMnu').click(function(e) 
		{
			e.preventDefault();
			$(".slbMgmtMnu").find('div').attr('id', 'slbMgmtimg');
			$(".dashboardMnu").find('div').attr('id', 'dashboardimg');
			$(".faultMgmtMnu").find('div').attr('id', 'faultMgmtimg');
			$(".monitorMnu").find('div').attr('id', 'monitorimg');
			$(".logMnu").find('div').attr('id', 'logimg');
			$(".slbMgmtMnu").find('div').attr('id', 'slbMgmtimg');
			$(".reportMnu").find('div').attr('id', 'reportimg');
			$(".sysSettingMnu").find('div').attr('id', 'sysSettingimg');
			$(".toolMnu").find('div').attr('id', 'systoolsimgSelect');
			$('.LocationNavi').addClass('none');
			header.setActiveMenu('ToolMnu');
		});
		
		$('.sysSettingUserMnu').click(function(e)
		{
			$('.snb_close').click();
			$('.listcontainer_open').parent().addClass('none');
			sysSetting.loadUserListContent();
			adcSetting.setObjOnAdcChange();
		});
		$('.sysSettingVsFilterMnu').click(function(e)
		{
			$('.snb_close').click();
			$('.listcontainer_open').parent().addClass('none');
			vsFilterConfig.loadListContent();
			adcSetting.setObjOnAdcChange();
		});
		$('.sysSettingLogicalGroupMnu').click(function(e)
		{
			$('.snb_close').click();
			$('.listcontainer_open').parent().addClass('none');
			serviceGrpConfig.loadListContent();
			adcSetting.setObjOnAdcChange();
		});
		$('.sysSettingBackupMnu').click(function(e)
		{
			$('.snb_close').click();
			$('.listcontainer_open').parent().addClass('none');
			sysBackup.loadListContent();
			adcSetting.setObjOnAdcChange();
		});
		$('.sysSettingSysInfoMnu').click(function(e)
		{
			$('.snb_close').click();
			$('.listcontainer_open').parent().addClass('none');
			systemInfo.loadSystemInfoContent();
			adcSetting.setObjOnAdcChange();
		});
		$('.sysSettingLicMnu').click(function(e)
		{
			$('.snb_close').click();
			$('.listcontainer_open').parent().addClass('none');
			license.loadLicenseContent();
			adcSetting.setObjOnAdcChange();
		});
		$('.sysSettingAuditMnu').click(function(e)
		{
			$('.snb_close').click();
			$('.listcontainer_open').parent().addClass('none');
			auditLog.loadContent();
			adcSetting.setObjOnAdcChange();
		});
		$('.sysSettingLogContentMnu').click(function(e)
		{
			$('.snb_close').click();
			$('.listcontainer_open').parent().addClass('none');
			logContentInfo.loadContent();
			adcSetting.setObjOnAdcChange();
		});
		$('.sysSettingResponseTimeMnu').click(function(e)
		{
			$('.snb_close').click();
			$('.listcontainer_open').parent().addClass('none');
			respTime.loadContent();
			adcSetting.setObjOnAdcChange();
		});
		$('.sysSettingConfigMnu').click(function(e)
		{
			$('.snb_close').click();
			$('.listcontainer_open').parent().addClass('none');
			config.loadConfigContent();
			adcSetting.setObjOnAdcChange();
		});
		
		// Left Navigation 도구 및 진단 클릭시 발생하는 이벤트
		$('.sysToolMnu').click(function(e)
		{
			jsToolsMain.loadLeftPane();
			jsToolsMain.jsToolsPortInfo.loadContent();
			adcSetting.setObjOnAdcChange();
		});
		
		$('.sysSettingSysViewDumpMnu').click(function(e)
		{
			$('.snb_close').click();
			$('.listcontainer_open').parent().addClass('none');
			jsToolsMain.loadSysViewContent();
			adcSetting.setObjOnAdcChange();
		});
		
//		$('.dashboardMnu').click(function(e)
//		{
//			$('.snb_close').click();
//			$('.listcontainer_open').parent().addClass('none');
//			dashboardHeader.load();
//			adcSetting.setObjOnAdcChange();
//		});
		
		$('.dashDynamicMnu').click(function(e)
		{
			$('.snb_close').click();
			$('.listcontainer_open').parent().addClass('none');
			dashboardHeader.load();
			adcSetting.setObjOnAdcChange();
		});
		
		$('.dashServiceMnu').click(function(e)
		{
			$('.snb_close').click();
			$('.listcontainer_open').parent().addClass('none');
			dashboardServiceHeader.load();
			adcSetting.setObjOnAdcChange();
		});

		$('.loginIdLnk').click(function(e) 
		{
			e.preventDefault();
			$accountIndex = $(this).children('span').eq(0).text();
	
			$('.sysSettingMnu').click();
			$(".systemSub ul").find('a').removeClass("select");
			$(".systemSub ul").find('.sysSettingUserMnu a').attr("class", "select");
			
			$('.snb_close').click();
			$('.listcontainer_open').parent().addClass('none');

//			sysSetting.loadLeftPane();
			sysSetting.loadUserModifyContent($accountIndex);
		});		
		$('#logoutLnk').click(function(e) 
		{
			e.preventDefault();
			location.href = 'member/logout.action';
			if (getDashPopup() === undefined)
			{				
			}
			else
			{
				getDashPopup().close();
			}			
		});
		}
	},	
	headerLoginEvents : function() 
	{
		with (this) 
		{			
			clearRefreshTimer();
			
			if (0 != refreshIntervalSeconds) 
			{
				refreshTimer = setInterval(function() 
				{					
					loadLoginCheck();
					alertWnd.loadContent();
				}, refreshIntervalSeconds * 1000);
			}
		}
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
	}
});

