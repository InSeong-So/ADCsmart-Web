// @author: Hakmin Lee

var JSToolsMain = Class.create({
	initialize : function() 
	{
		this.isInitialized = false;
		this.jsToolsPortInfo = new JSToolsPortInfo();
		this.jsToolsSlbSessionInfo = new JSToolsSlbSessionInfo();
		this.jsToolsUnusedSlbInfo = new JSToolsUnusedSlbInfo();
		this.$unassignedAdcOptions = $();
		this.refreshTimer = undefined;
		this.refreshIntervalSeconds=3;
//		this.autoRefresh = false;
		this.flag = 1;
	},
	loadLeftPane : function() 
	{
		with (this) 		
		{
			if (!isInitialized) 
			{
				isInitialized = true;
				header.setActiveMenu('JSToolsMain');
				applyLeftCss();
				registerLeftEvents();
			}
			loadFirtMenu();// 좌측 메뉴의 첫번째 로딩.
			
			$('.snb').addClass('none');
			$('.snb_system').addClass('none');
			$('.snb_systools').removeClass('none');
					
			$('.listcontainer').css('background-color', '#2d3d5e');
			$('.pick').addClass('none');
			$('.pick_system').addClass('none');
			$('.pick_systools').removeClass('none');
			$('.adc_search').addClass('none');
			$('.adc_search_1').removeClass('none');
		}
	},
	applyLeftCss : function() 
	{
		// SNB 높이 
//		$(window).resize(function() {
//			$('.snb_systools').height($(window).height() - $('.snb_systools').position().top);
//		});
		
		$('.snb_systools .snb_tree p').click(function()
		{
			$('.snb_systools .snb_tree p').each(function()
			{
				$(this).removeClass('on');
			});
			
			$(this).addClass('on');
		});
		
		// SNB Tree 클릭 이벤트
		$('.snb_systools .snb_tree .depth1 > li > p a').click(function(e) 
		{
			e.stopImmediatePropagation();
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
		
		// SNB 메뉴 숨기기/보이기
		$('.snb_systools .snb_close').toggle(function(e) 
		{
			$('.snb_systools').animate({ 'left':'-205px' }, 100, function() 
			{
				$('.snb_systools .snb_close').attr('class', 'snb_open').children('img').attr({ 'src':'imgs/layout/btn_open.png', 'alt':'메뉴 열기' });
				$('.content_wrap').css('margin-left', '10px');
			});
		}, function() {
			$('.snb_systools').animate({ 'left':'0px' }, 100, function() 
			{
				$('.snb_systools .snb_open').attr('class', 'snb_close').children('img').attr({ 'src':'imgs/layout/btn_close.png', 'alt':'메뉴 닫기' });
				$('.content_wrap').css('margin-left', '215px');
			});
		});
		
		$(window).trigger('resize');
		
		// 백그라운드 이미지 제거
//		$('.depth2 > li > p').css({'background-image':'url()'});
	},
	registerLeftEvents : function() 
	{
		with (this) 
		{
			$('.toolsPortInfo').click(function(e) 
			{
				jsToolsPortInfo.loadContent();
			});
			
			$('.toolsSlbSessionInfo').click(function(e) 
			{
				jsToolsSlbSessionInfo.initialize();// 이전 설정을 초기화 한다.
				jsToolsSlbSessionInfo.loadContent();
			});
			
			$('.toolsUnusedSlb').click(function(e) 
			{
				jsToolsUnusedSlbInfo.loadListContent();
			});
			
			$('.toolsVSUsage').click(function(e) 
			{
				$.obAlertNotice("toolsVSUsagexxx");
//				sysBackup.loadListContent();
			});
		}
	},
	setLeftTreeToTopMenu : function() 
	{
		$('.snb_systools .snb_tree p').each(function()
		{
			$(this).removeClass('on');
		});
		
		$('.snb_systools .snb_tree p.toolsPortInfo').addClass('on');
	},
	loadFirtMenu : function(searchKey) 
	{
		with (this) 
		{
			setLeftTreeToTopMenu();
			jsToolsPortInfo.loadContent();			
		}
	},
	loadSysViewContent : function(flag) 
	{
//		console.log('!!!!!!!!!!!!!!!!test!');
		with (this)
		{	
			ajaxManager.runHtmlExt({
				url : 'sysTools/sysView/loadSysViewContent.action',
//				url : 'sysSetting/angular/loadAngularContent.action',
				data : 
				{
					
				},
				target : "#wrap .contents", 
				successFn : function(params)
				{					
					header.setActiveMenu('JSToolsSysViewInfo');
					
					refreshSysView(flag);
					registerEvent(flag);
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError("error", jqXhr);
				}
			});		
		}
	},
	registerEvent : function(flag)
	{
		with (this)
		{			
			if (flag == 0)
				$('.startDump').attr("disabled", "disabled");
			else
				$('.startDump').removeAttr("disabled");
				
			$('.startDump').click(function(e) 
			{
				e.preventDefault();				
				startDump();
			});
			
			$('.runDumpDownload').click(function(e) 
			{
				e.preventDefault();		
//				$('.startDump').attr("flag", "1");
//				$('.startDump').removeAttr("disabled");
				dumpDownload();
//				$('.runDumpDownload').attr("disabled", "disabled");
			});
		}
	},
	startDump : function()
	{
		with (this)
		{			
			ajaxManager.runJsonExt({
				url : 'sysTools/sysView/dumpFileCreate.action',
				data : 
				{
					
				},
				target : "#wrap .contents", 
				successFn : function(data)
				{					
//					registerEvent();
					$('.startDump').attr("flag", "0");
					$('.startDump').attr("disabled", "disabled");	
					$('.runDumpDownload').attr("disabled", "disabled");
					flag = $('.startDump').attr("flag")
					loadSysViewContent(flag); 
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError("error", jqXhr);
				}
			});
		}
	},
	dumpDownload : function()
	{
		with (this) 
		{
			var url = "sysTools/runDumpDownload.action";
			$('#downloadFrame').attr('src', url);			
			
			$('.startDump').attr("flag", "1");
			$('.startDump').removeAttr("disabled");	
			$('.runDumpDownload').attr("disabled", "disabled");
		}
	},
	refreshSysView : function(flag)
	{
		with (this)
		{
			clearRefreshTimer();
			if (0 != refreshIntervalSeconds)
			{
				refreshTimer = setInterval(function() 
				{
					if (isFileCheck())
					{
						autoRefresh=true;						
						loadSysViewContent();
					} 
					else 
					{
						clearRefreshTimer();
//						autoRefresh=true;						
//						loadSysViewContent();
					}					
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
	},
	isFileCheck : function()
	{
		with (this)
		{	
			ajaxManager.runJsonExt({
				url : 'sysTools/sysView/isDumpDownloadFileExist.action',
				data : 
				{
					
				},
				target : "#wrap .contents", 
				successFn : function(data)
				{					
					if(data.isSuccessful)
					{
						$('.runDumpDownload').removeAttr("disabled");
//						$('.startDump').attr("flag", "0");
//						$('.startDump').attr("disabled", "disabled");
//						$('.downloadFile').children().removeAttr("disabled");
					}
					else
					{
						$('.runDumpDownload').attr("disabled", "disabled");
//						$('.startDump').attr("flag", "1");
//						$('.startDump').removeAttr("disabled");
//						$('.downloadFile').children().attr("disabled", "disabled");
					}
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError("error", jqXhr);
				}
			});		
		}
	}
});
