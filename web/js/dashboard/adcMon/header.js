var AdcSdsDashboardHeader = Class.create({
	initialize : function() 
	{
		this.ajaxManager = new FlowitAjax();
		this.owner = undefined;		
		this.content = new AdcSdsDashboardContent();
		this.content.owner = this;
		this.refreshTimer = undefined;
		this.refreshIntervalSeconds = 30;
		this.initFlag=false;
		this.selectedCategory=undefined; //0: 전체, 1: 그룹, 2: 개별 adc, 3:virtual server, 4:virtual service
		this.selectedIndex=undefined;
		this.selectedName=undefined;
		this.selectedVendor=undefined;		
		this.selectedVsIndex=undefined; 
	},
	load : function(params) 
	{
		this.initFlag=false;
		with (this) 
		{			
			ajaxManager.runHtml({
				url			: '/dashboard/adcMon/loadAdcSdsHeader.action',
				target		: '.listcontainer_monitor',
				data		: params,
				successFn	: function(data) 
				{
					loadVSStatus();
	
					registerAdcSdsHeaderEvents();
					content.loadFaultMonitoringChartContent();
					popup();
				}
			});
		}
	},	
	
	loadVSStatusRefresh : function(curCategory, curIndex, curName, curVendor) 
	{	
		with (this) 
		{		
			ajaxManager.runHtml({
				url			: '/dashboard/adcMon/loadVSSummary.action',
				target		: '.monitor_con1',
				data : 
				{
					"curCategory" 	: this.selectedCategory,
					"curIndex" 		: this.selectedIndex,
					"curName" 		: this.selectedName,
					"curVendor" 	: this.selectedVendor					
					
				},
				successFn	: function(data) 
				{
					loadAdcSdsHeader(curCategory, curIndex, curName, curVendor);
					applyContainEvents();
					applyContainCss();
					registerVsSummeryEvents();
					_resizeFont();
					
					registerAdcSdsHeaderEvents();
					content.loadFaultMonitoringGridContent();
				}
			});
		}
	},
	
	
	//selected 된 값으로 right list : refresh - 전체 / 그룹 / ADC
	loadVSStatus : function(curCategory, curIndex, curName, curVendor) 
	{	
		with (this) 
		{		
			ajaxManager.runHtml({
				url			: '/dashboard/adcMon/loadVSSummary.action',
				target		: '.monitor_con1',
				data : 
				{
					"curCategory" 	: this.selectedCategory,
					"curIndex" 		: this.selectedIndex,
					"curName" 		: this.selectedName,
					"curVendor" 	: this.selectedVendor					
					
				},
				successFn	: function(data) 
				{
					applyContainEvents();
					applyContainCss();
					registerVsSummeryEvents();
					_resizeFont();
				}
			});
		}
	},
	
	_resizeFont : function()
	{
		var adcTotalVSSummaryAvailText = $('.monitor_sum1_area').text();
		var adcTotalVSSummaryUnAvailText = $('.monitor_sum2_area').text();
		var adcTotalVSSummaryDisAbleText = $('.monitor_sum3_area').text();
		var adcTotalVSSummaryUnavailOverNDaysText = $('.monitor_sum4_area').text();
		
		if (adcTotalVSSummaryAvailText.length > 6)
		{
			$('#monitor_sum1').removeClass('monitor_sum1_area');
			$('#monitor_sum1').addClass('monitor_sum1_area_over');
		}		
		
		if (adcTotalVSSummaryUnAvailText.lenth > 6)
		{
			$('#monitor_sum2').removeClass('monitor_sum2_area');
			$('#monitor_sum2').addClass('monitor_sum2_area_over');
		}
		
		if (adcTotalVSSummaryDisAbleText.lenth > 6)
		{
			$('#monitor_sum3').removeClass('monitor_sum3_area');
			$('#monitor_sum3').addClass('monitor_sum3_area_over');
		}
		
		if (adcTotalVSSummaryUnavailOverNDaysText.lenth > 6)
		{
			$('#monitor_sum4').removeClass('monitor_sum4_area');
			$('#monitor_sum4').addClass('monitor_sum4_area_over');
		}		
	},	
		
	//refresh 시 tree는 마지막 선택 값으로 display
	loadAdcSdsHeader : function(curCategory, curIndex, curName, curVendor)
	{
		with (this) 
		{	
			ajaxManager.runJson({
				url			: '/dashboard/adcMon/loadAdcSdsHeaderRefresh.action',
				target		: '.listcontainer_monitor',
				data : 
				{
					"curCategory" 	: curCategory,
					"curIndex" 		: curIndex,
					"curName" 		: curName,
					"curVendor" 	: curVendor
				},
				successFn	: function(data) 
				{	
					//$(".sub").hide();
//					applyContainEvents();
				}
			});
		}
	},
	//Auto Refresh
	resetRefreshTimer : function() 
	{
		with (this) 
		{
			clearRefreshTimer();
			if (0 != refreshIntervalSeconds) 
			{
				refreshTimer = setInterval(function() 
				{
//					owner.load();
					loadVSStatusRefresh();
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
	popup : function ()
	{		
		$('.adcInfoMgmtLnk').click(function(e) 
		{
			e.preventDefault();
			selectedCategory = $(this).parent().parent().find('.objCategory').text();
			selectedIndex = $(this).parent().parent().find('.objIndex').text();
			selectedName = $(this).parent().parent().find('.objName').text();
			selectedVendor = $(this).parent().parent().find('.objVendor').text();
			
			var option = "height=280,width=380,scrollbars=no,toolbar=no,location=no,status=no,menubar=no,resizeable=no,left=0,top=0";
			var params = "curCategory=" + selectedCategory + "&curIndex=" +selectedIndex + "&curName=" +encodeURIComponent(selectedName) + "&curVendor=" + selectedVendor;
			
			window.open("/dashboard/adcMon/loadPopupContent.action?"+params ,"",option);
		});	
	},
	
	registerVsSummeryEvents : function()
	{
		$('.trafficConnection').click(function(e)
		{
			e.preventDefault();
			selectedVsIndex = $(this).parent().parent().parent().parent().find('.vsIndex').text();
			selectedVendor = $(this).parent().parent().parent().parent().find('.vendor').text();			
			selectedVsIp = $(this).parent().parent().parent().parent().find('.vIp').text();
			selectedVsPort = $(this).parent().parent().parent().parent().find('.vPort').text();
			
			
			var contents = "vsConnection";
			var option = "height=418,width=863,scrollbars=no,toolbar=no,location=no,status=no,menubar=no,resizeable=no,left=0,top=0";
			var params = "vsIndex=" + selectedVsIndex + "&curVendor=" + selectedVendor + "&popUpcontents=" + contents + "&vsIp=" + selectedVsIp + "&vsPort=" + selectedVsPort;
			
			window.open("/dashboard/adcMon/loadVsPopupPage.action?"+params ,"vsConnection",option);
		});
		$('.trafficThroughput').click(function(e)
		{
			e.preventDefault();
			selectedVsIndex = $(this).parent().parent().parent().parent().find('.vsIndex').text();
			selectedVendor = $(this).parent().parent().parent().parent().find('.vendor').text();			
			selectedVsIp = $(this).parent().parent().parent().parent().find('.vIp').text();
			selectedVsPort = $(this).parent().parent().parent().parent().find('.vPort').text();
			
			var contents = "vsThroughput";
			var option = "height=418,width=863,scrollbars=no,toolbar=no,location=no,status=no,menubar=no,resizeable=no,left=0,top=0";
			var params = "vsIndex=" + selectedVsIndex + "&curVendor=" + selectedVendor + "&popUpcontents=" + contents + "&vsIp=" + selectedVsIp + "&vsPort=" + selectedVsPort;	
			
			window.open("/dashboard/adcMon/loadVsPopupPage.action?"+params ,"vsThroughput",option);	
		});
	},
	
	
	registerAdcSdsHeaderEvents : function() 
	{
		// 선택한 ADC 또는 그룹에 대한 상세 정보 팝업 메세지 처리.
		$('.connectionGraphLnk').click(function(e) 
		{
			e.preventDefault();
			selectedCategory = $(this).parent().parent().parent().parent().find('.objCategory').text();
			selectedIndex = $(this).parent().parent().parent().parent().find('.objIndex').text();
			selectedName = $(this).parent().parent().parent().parent().find('.objName').text();
			// 전체를 클릭시엔 selectedName이 ""이기때문에 ADC 전체로 초기화 한다.
			if (selectedName == "")
			{
				selectedName = "ADC전체";
			}
			//selectedVendor = $(this).parent().parent().parent().parent().find('.objVendor').text();
			
			var contents = "adcConnection";
			var option = "height=418,width=863,scrollbars=no,toolbar=no,location=no,status=no,menubar=no,resizeable=no,left=0,top=0";
			var params = "curCategory=" + selectedCategory + "&curIndex=" +selectedIndex
							+ "&curName=" +encodeURIComponent(selectedName) /* + "&curVendor=" + selectedVendor*/
							+ "&popUpcontents=" + contents;
					
			window.open("/dashboard/adcMon/loadAdcPopupPage.action?"+params ,"adcConnection",option);
		});
		
		// throughput 그래프 이벤트 처리
		$('.throughputGraphLnk').click(function(e) 
		{
			e.preventDefault();
			
			selectedCategory = $(this).parent().parent().parent().parent().find('.objCategory').text();
			selectedIndex = $(this).parent().parent().parent().parent().find('.objIndex').text();
			selectedName = $(this).parent().parent().parent().parent().find('.objName').text();
			// 전체를 클릭시엔 selectedName이 ""이기때문에 ADC 전체로 초기화 한다.
			if (selectedName == "")
			{
				selectedName = "ADC전체";
			}
			//selectedVendor = $(this).parent().parent().parent().parent().find('.objVendor').text();
			
			var contents = "adcThroughput";
			var option = "height=418,width=863,scrollbars=no,toolbar=no,location=no,status=no,menubar=no,resizeable=no,left=0,top=0";
			var params = "curCategory=" + selectedCategory + "&curIndex=" +selectedIndex 
							+ "&curName=" +encodeURIComponent(selectedName) /* + "&curVendor=" + selectedVendor*/
							+ "&popUpcontents=" + contents;			
			
			window.open("/dashboard/adcMon/loadAdcPopupPage.action?"+params ,"adcThroughput",option);
		});
	},
	popupConnectionGraph : function()
	{
		with (this)
		{
			var option = "height=300, width=580, scrollbars=no, toolbar=no, status=no, menubar=no, left=0; top=0";
			var params = "curCategory=" + curCategory + "curIndex=" + curIndex + "curName=" + curName + "curVendor=" + curVendor;
			window.open("dashboard/adcMon/loadConnectionGraph.action?" + params, "", option);
			
			//창을 띄움
		}
	},
	applyContainCss : function()
	{
		initTable([ "#table2 tbody tr" ], [ 0 ], [ -1 ]);
//		$(".top .plusminus").attr('src', "/imgs/monitoring/ico_minus.png");
//		$(".top").attr('val', "minus");
		$(".sub1").show();
//		$(".sub").css('font-color') == 'white';
	},
	applyContainEvents : function() 
	{	
		with (this) 
		{
			if(this.initFlag==true)
				return;
			this.initFlag=true;

			$(".btnPlusMinus").click(function() 
			{
				var value = $(this).attr("val");	
				var group = $(this).attr("group");	
						
				if (value == "plus") 
				{
//					alert("plus인경우");
					// 전체 +로
//					$(".sub1 .plusminus").attr('src', "/imgs/monitoring/ico_plus.png");
					$(".sub1 .btnPlusMinus").attr("val", "plus");
					
					// + -> -
//					var this_img = $(this).find('img').attr('src');
//					$(this).find('img').attr('src',  "/imgs/monitoring/ico_minus.png");
					$(this).attr("val", "minus");

					if (group == "all") 
					{
						$(".sub1").show();
					}
					else 
					{
						// 전체 2단계 숨기기
						$(".sub2").hide();
						$("." + group).show();
					}
					
				}
				else 
				{
//					alert("minus인경우");
					var this_img = $(this).find('img').attr('src');
					
//					$(this).find('img').attr('src', this_img.replace('minus.png', 'plus.png'));
					$(this).attr("val", "plus");
					
					if (group == "all") 
					{
//						alert("group : " + group);
						$(".sub").hide();
//						$(".plusminus").attr('src', this_img.replace('minus.png', 'plus.png'));
						$(".btnPlusMinus").attr("val", "plus");
					}
					else 
					{
						$("." + group).hide();				
					}						
				}
			});

			
			$("#allGroup").click(function(e)
			{
				e.preventDefault();
				$(".sub").removeClass("selected");
				$(".sub").addClass("unselected");
				
				selectedCategory = $(this).parent().parent().find('.objCategory').text();
				selectedIndex = $(this).parent().parent().find('.objIndex').text();
				selectedName = $(this).parent().parent().find('.objName').text();
				selectedVendor = $(this).parent().parent().find('.objVendor').text();
//						onGroupChange();
				loadVSStatus(selectedCategory, selectedIndex, selectedName, selectedVendor);
				loadAdcSdsHeader(selectedCategory, selectedIndex, selectedName, selectedVendor);
				
				$(this).parent().parent().parent().removeClass("unselected");
//				$(this).parent().parent().parent().addClass("selected");
			});
			
			$(".adcGroups").click(function(e)
			{
				e.preventDefault();
				$(".top").removeClass("selected");
				$(".top").addClass("unselected");
				
				$(".sub").removeClass("selected");
				$(".sub").addClass("unselected");
				
				selectedCategory = $(this).parent().parent().find('.objCategory').text();
				selectedIndex = $(this).parent().parent().find('.objIndex').text();
				selectedName = $(this).parent().parent().find('.objName').text();
				selectedVendor = $(this).parent().parent().find('.objVendor').text();
//						onGroupChange();
				loadVSStatus(selectedCategory, selectedIndex, selectedName, selectedVendor);
//						$(this).parents.firstChild().addClass('on');
				loadAdcSdsHeader(selectedCategory, selectedIndex, selectedName, selectedVendor);
				
				$(this).parent().parent().parent().removeClass("unselected");
//				$(this).parent().parent().parent().addClass("selected");
			});
			
			$(".adcInfos").click(function(e)
			{
				e.preventDefault();
				$(".top").removeClass("selected");
				$(".top").addClass("unselected");
				
				$(".sub").removeClass("selected");
				$(".sub").addClass("unselected");

				selectedCategory = $(this).parent().parent().find('.objCategory').text();
				selectedIndex = $(this).parent().parent().find('.objIndex').text();
				selectedName = $(this).parent().parent().find('.objName').text();
				selectedVendor = $(this).parent().parent().find('.objVendor').text();

				loadVSStatus(selectedCategory, selectedIndex, selectedName, selectedVendor);
				loadAdcSdsHeader(selectedCategory, selectedIndex, selectedName, selectedVendor);
				
				$(this).parent().parent().parent().removeClass("unselected");
				$(this).parent().parent().parent().addClass("selected");				
			});

			function table_scroll(ele)
			{
				for ( var j = 0; j < ele.length; j++) 
				{
					$(ele[j]).each(function(i) 
					{
						if($(this).height() >= $(this).parent('.table_scr').height())
						{
							$(this).css('border', 'none');
							$(this).parent('.table_scr').addClass('br');
							$(this).css('width', $(this).parent('.table_scr').width()+"px");
						}else{
							$(this).css('width', $(this).parent('.table_scr').width()+"px");
						}
					});
				}
			}
			
			function table_width(ele)
			{
				for ( var j = 0; j < ele.length; j++) 
				{
					$(ele[j]).each(function(i) 
					{
						$(this).css('width', $(this).parent('.table_scr').width()+"px");
					});
				}
				
			}	
			
			//$('.monitor_con3_B .table200 .adcName').width(166);
		}		
	}	
});

