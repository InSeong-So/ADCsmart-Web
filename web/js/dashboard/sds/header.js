var SdsDashboardHeader = Class.create({
	initialize : function() 
	{
		this.ajaxManager = new FlowitAjax();
		this.owner = undefined;
		this.statusGroup = undefined;	// statusGroup[0: adc, 1: vs, 2:fault]
		this.status = undefined;
//		this.vsUnavailableStatusMinDays = undefined;
		this.faultMaxDays = undefined;
//		this.content = new SdsDashboardContent();
	},
	load : function() 
	{
		with (this) 
		{
//			if (!vsUnavailableStatusMinDays)	{ vsUnavailableStatusMinDays = '7'; }
			if (!faultMaxDays)					
			{ 
				faultMaxDays = '1'; 
			}
			
			var params = 
			{
//				vsUnavailableStatusMinDays	: vsUnavailableStatusMinDays,
				faultMaxDays				: faultMaxDays
			};
			
			ajaxManager.runHtml({
				url			: 'dashboard/sds/loadHeader.action',
//				target		: '.accordion_type1',
				target		: '.contents_1',
				data		: params,
				successFn	: function(data) 
				{
					registEvents();
					applyHeaderCss();
//					resizingFont();
					_resizeFont();
					selectStatus();
					if (owner) 
					{
						owner.loadContent(true);
//						content.aa(params);
					}
				}
			});
		}
	},
/*	
	resizingFont : function()
	{
		var faultWarningText = $('.summary .faultsum_1_area').text();
		var $faultWarning = $('.summary .faultsum_1_area').filter(':last');		
		$faultWarning.empty();
		var html = '';
		
		if ( faultWarningText.length > 6)
		{
			html += '<span class="css작은거">${(statusSummary.faultWarning)!0}</span>';						
		}
		else
		{
			html += '${(statusSummary.faultWarning)!0}';	
		}		
		$faultWarning.html(html);
	},
*/
	_resizeFont : function()
	{
		//ADC 요약 - ADC가 많은 경우가 있을 때는 대비해서 추가해놓음.
		var adcTotalText = $('.adcsum_all_area').text(); // ADC 전체	
		var adcAvailableText = $('.adcsum_1_area').text(); // ADC 정상
		var adcUnavailableText = $('.adcsum_0_area').text(); // ADC 단절
		if (adcTotalText.length > 6)
		{
			$('#adcsum_all').removeClass('adcsum_all_area');
			$('#adcsum_all').addClass('adcsum_all_area_over');
		}
		
		if (adcAvailableText.length > 8)
		{
			$('#adcsum_1').removeClass('adcsum_1_area');
			$('#adcsum_1').addClass('adcsum_1_area_over');
		}
		
		if (adcUnavailableText.length > 8)
		{
			$('#adcsum_0').removeClass('adcsum_0_area');
			$('#adcsum_0').addClass('adcsum_0_area_over');
		}
		
		//장애 모니터링 통계
		var faultWarningText = $('.faultsum_1_area').text(); // 장애모니터링 경고	
		var faultUnSolvedText = $('.faultsum_0_area').text(); // 장애모니터링 미해결
		var faultSolvedText = $('.faultsum_all_area').text(); // 장애모니터링 해결
		if ( faultWarningText.length > 8)
		{
			$('#faultsum_1').removeClass('faultsum_1_area');
			$('#faultsum_1').addClass('faultsum_1_area_over');			
		}
		
		if (faultUnSolvedText.length > 8)
		{
			$('#faultsum_0').removeClass('faultsum_0_area');
			$('#faultsum_0').addClass('faultsum_0_area_over');
		}		
		
		if (faultSolvedText.length > 6)
		{
			$('#faultsum_all').removeClass('faultsum_all_area');
			$('#faultsum_all').addClass('faultsum_all_area_over');
		}
		
		//Virtual Server 통계
		var vsDisableText = $('.vssum_2_area').text(); //disable
		var vsTotalText = $('.vssum_all_area').text(); //total
		var vsAvailableText = $('.vssum_1_area').text(); //available
		var vsUnavailableText = $('.vssum_0_area').text(); //unavailable
		if (vsDisableText.length > 8)
		{
			$('#vssum_2').removeClass('vssum_2_area');
			$('#vssum_2').addClass('vssum_2_area_over');
		}
		
		if (vsTotalText.length > 6)
		{
			$('#vssum_all').removeClass('vssum_all_area');
			$('#vssum_all').addClass('vssum_all_area_over');
		}
		
		if (vsAvailableText.length > 8)
		{
			$('#vssum_1').removeClass('vssum_1_area');
			$('#vssum_1').addClass('vssum_1_area_over');
		}
		
		if (vsUnavailableText.length > 8)
		{
			$('#vssum_0').removeClass('vssum_0_area');
			$('#vssum_0').addClass('vssum_0_area_over');
		}
		
	},
	applyHeaderCss : function() 
	{
		with (this) 
		{
			//closeAccordionType1('.dash_content .accordion_type1');	// acordion 슬라이드 효과
		}
	},	
	registEvents : function() 
	{
		with (this)
		{
			$('.server_cnt').click(function(event) 
			{
				event.preventDefault();
				if ($(event.target).is("select")) 
				{
					return;
				}
				
				$('.server_cnt').each(function() 
				{
					$(this).parent().removeClass('on');
				});
				$('.status_tlt').each(function() 
				{
					$(this).parent().css('background', '');
				});
				$(this).parent().addClass('on');
				$(this).parentsUntil('div').find('.status_tlt').parent().css('background', '#1e273a');
				
				statusGroup = $(this).parentsUntil('div').find('.statusGroup').text();
				status = $(this).find('.status').text();
							
//				alert("statusGroup : " + statusGroup + " ------ " + "status : " + status);
				
				if (owner) 
				{
					owner.loadContent();
				}
			});
			
			/*$('select[name="vsUnavailableStatusMinDays"]').change(function() {
				vsUnavailableStatusMinDays = $(this).val();
				if (owner) {
					owner.load();
				}
			});*/
			
			$('select[name="faultMaxDays"]').change(function() 
			{
				faultMaxDays = $(this).val();
				if (owner) 
				{
					owner.load();
				}
			});
		}
	},
	selectStatus : function() 
	{
		with (this) 
		{
			if (!statusGroup)	
			{ 
				statusGroup = '0'; 
			}
			if (!status)		
			{ 
				status = '0'; 
			}
			
			$('.server_cnt').each(function() 
			{
				$(this).parent().removeClass('on');
			});
			$('.status_tlt').each(function() 
			{
				$(this).parent().css('background', '');
			});
			$('.server_cnt').each(function() 
			{
				if ($(this).parentsUntil('div').find('.statusGroup').text() == statusGroup) 
				{
					$(this).parentsUntil('div').find('.status_tlt').parent().css('background', '#1e273a');
				}
				if ($(this).find('.status').text() == status) 
				{
					$(this).parent().addClass('on');
				}
			});
			
//			alert("statusGroup : " + statusGroup + " ------ " + "status : " + status);
				
			//$('select[name="vsUnavailableStatusMinDays"]').val(vsUnavailableStatusMinDays);
			$('select[name="faultMaxDays"]').val(faultMaxDays);
		}
	}
});
