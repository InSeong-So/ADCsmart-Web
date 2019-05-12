var SdsDashboardLeft = Class.create({
	initialize : function() {
		this.selectedType = 'all';
		this.groupIndex = undefined;
		this.adc = {};
	},
	onAdcChange : function() {
		with (this) {
			FlowitUtil.log('selectedType:%s, groupIndex:%s, adc:%o', selectedType, groupIndex, adc);
			dashboard.loadContent();
		}
	},
	loadLeftPane : function(searchKey, taskQ) {
		with (this) {
			ajaxManager.runHtml({
				url : 'dashboard/sds/loadLeftPane.action',
				data : {
					'searchKey' : searchKey
				},
				target: '#leftPane',
				successFn : function(params) {
					applyLeftCss();
					registerLeftEvents();
					dashboard.loadHeader();
				}
			}, taskQ);
		}
	},
	applyLeftCss : function() {
		with (this) {
			if (selectedType == 'all') {
				_drawSelectedAllBlock();
			} else if (selectedType == 'group') {
				var $groupBlock = _getSelectedGroupBlock();
				if (!$groupBlock) {
					FlowitUtil.log('could not find the selected group; change the selected type to all.');
					selectedType = 'all';
					_drawSelectedAllBlock();
				} else {
					$groupBlock.addClass('on');
					var $subMenu = $groupBlock.parent().children('ul');
					$subMenu.css('display', 'block');
				}
			} else if (selectedType == 'adc') {
				var $adcBlock = _getSelectedAdcBlock();
				if (!$adcBlock) {
					FlowitUtil.log('could not find the selected adc; change the selected type to all.');
					selectedType = 'all';
					_drawSelectedAllBlock();
				} else {
					$adcBlock.addClass('on');
					var $groupMenu = $adcBlock.parents('.depth2');
					$groupMenu.css('display', 'block');
				}
			}
			
			// the entire adcs node
			$('.snb_tree > li > p').click(function(e) {
				if($(this).hasClass('on'))
					return ;
				
				_clearSelectionOnAllNodes();
				$(this).addClass('on');
				_closeAllGroups();
			});
			
			// group node
			$('.snb_tree .depth1 > li > p').click(function(e) {
				_clearSelectionOnAllNodes();
				if($(this).next().css('display') != 'none') {
					$(this).addClass('on');
					return ;
				}
				
				_closeAllGroups();
				var $subMenu = $(this).parent().children('ul');
				$(this).addClass('on');
				$subMenu.slideDown(200);
			});
	
			// adc node
			$('.snb_tree .depth2 > li > p').click(function(e) {
				if($(this).hasClass('on'))
					return ;
				
				_clearSelectionOnAllNodes();
				$(this).addClass('on');
			});
	
			$('.snb .snb_tree .depth2 .adcName').parent().width(183);		// 183px is experimental #.
			
			$(window).trigger('resize');
		}
	},
	_clearSelectionOnAllNodes : function() {
		var $allSelectableNodes =  $('.snb_tree li p');
		$allSelectableNodes.removeClass('on');
	},
	_closeAllGroups : function() {
		var $subMenus = $('.snb_tree .depth1 > li > ul');
		$subMenus.each(function() {
			if($(this).css('display') != 'none')
				$(this).slideUp(200);
		});
	},
	_drawSelectedAllBlock : function() {
		var $allBlock = $('.snb_tree .allBlock');
		if ($allBlock.hasClass('on'))
			return;
			
		$allBlock.addClass('on');
	},
	_getSelectedGroupBlock : function() {
		var $groupBlock = $('.snb_tree .groupBlock');
		for (var i=0; i < $groupBlock.length; i++) {
			var index = $groupBlock.eq(i).find('.groupIndex').text();
			if (index == this.groupIndex)
				return $groupBlock.eq(i);
		}
		
		return null;
	},
	_getSelectedAdcBlock : function() {
		var $adcBlock = $('.snb_tree .adcBlock');
		for (var i=0; i < $adcBlock.length; i++) {
			var index = $adcBlock.eq(i).find('.adcIndex').text();
			if (index == this.adc.index)
				return $adcBlock.eq(i);
		}
		
		return null;
	},
	registerLeftEvents : function() 
	{
		with (this) 
		{
//			$('.dashboard_snb > .title').click(function(e) 
//			{
//				loadLeftPane();
//			});
			
			$('.snb_tree .allBlock').click(function(e) 
			{
				e.preventDefault();
				selectedType = 'all';
				onAdcChange();
			});

			$('.snb_tree .groupBlock').click(function(e) 
			{
				e.preventDefault();
				selectedType = 'group';
				groupIndex = $(this).find('.groupIndex').text();
				onAdcChange();
			});
			
			$('.snb_tree .adcBlock').click(function(e) {
				FlowitUtil.log('adcBlock');
				selectedType = 'adc';
				adc.index = $(this).find('.adcIndex').text();
				adc.name = $(this).find('.adcName').text();
				adc.type = $(this).find('.adcType').text();
				onAdcChange();
			});
/*			
			$('.adc_search .btn a.searchLnk').click(function (e) 
			{
				e.preventDefault();
//						var searchKey = $(this).siblings('.adc_search .inputTextposition input.searchTxt').val();
				var searchKey = $('input[name="searchTxt"]').val();
				
				FlowitUtil.log('click:' + searchKey);
				loadLeftPane(searchKey, undefined, true);
			});
			
			$('.adc_search .inputTextposition input.searchTxt').keydown(function(e) 
			{				
				if (e.which != 13)				
					return;
						
				var searchKey = $(this).val(); 
				FlowitUtil.log('click:' + searchKey);
				loadLeftPane(searchKey, undefined, true);
			});	
*/
			
		}
	}
});