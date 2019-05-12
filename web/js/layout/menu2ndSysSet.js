var ClassMenu2ndSysSet = Class.create({
	initialize : function() {

	},
	loadContent : function() {
		with (this) {
			_applyLeftCss();
			_registerLeftEvents();
			_loadDefaultMainContent();

			$('.snb').addClass('none');
			$('.snb_systools').addClass('none');
			$('.snb_system').removeClass('none');

			$('.listcontainer').css('background-color', '#2d3d5e');
			$('.pick').addClass('none');
			$('.pick_system').removeClass('none');
			$('.pick_systools').addClass('none');
			$('.adc_search').addClass('none');
			$('.adc_search_1').removeClass('none');
		}
	},
	_loadDefaultMainContent : function() {
		with (this) {
			ajaxManager.runHtml({
				url : "layout/loadContent.action",
				target : "#wrap .contents",
				successFn : function(params) {
				}
			});
		}
	},
	_applyLeftCss : function() {
		$('.snb_system .snb_tree p').click(function() {
			$('.snb_system .snb_tree p').each(function() {
				$(this).removeClass('on');
			});

			$(this).addClass('on');
		});

		// 클릭 이벤트
		$('.snb_system .snb_tree .depth1 > li > p a').click(function(e) {
			e.stopImmediatePropagation();
			var $sub_menu = $(this).parent().nextAll('ul');

			if ($sub_menu.css('display') == 'none') {
				over($(this));
				$sub_menu.slideDown(200);
			} else {
				out($(this));
				$sub_menu.slideUp(200);
			}
		});

		// SNB 메뉴 숨기기/보이기
		$('.snb_system .snb_close').toggle(
				function(e) {
					$('.snb_system').animate(
							{
								'left' : '-205px'
							},
							100,
							function() {
								$('.snb_system .snb_close').attr('class',
										'snb_open').children('img').attr({
									'src' : 'imgs/layout/btn_open.png',
									'alt' : VAR_COMMON_MENUOPEN
								});
								$('.content_wrap').css('margin-left', '10px');
							});
				},
				function() {
					$('.snb_system').animate(
							{
								'left' : '0px'
							},
							100,
							function() {
								$('.snb_system .snb_open').attr('class',
										'snb_close').children('img').attr({
									'src' : 'imgs/layout/btn_close.png',
									'alt' : VAR_COMMON_MENUCLOSE
								});
								$('.content_wrap').css('margin-left', '215px');
							});
				});

		$(window).trigger('resize');

		// 백그라운드 이미지 제거
		// $('.depth2 > li > p').css({'background-image':'url()'});
	},
	_registerLeftEvents : function() {
		with (this) {
			$('.userMgmtPgh111').click(function(e) {
				e.preventDefault();
				var CSysSetting = new SysSetting();
				CSysSetting.loadContent();
			});

			$('.vsFilterPgh').click(function(e) {
				e.preventDefault();
				vsFilterConfig.loadListContent();
			});

			$('.sysBackupPgh').click(function(e) {
				e.preventDefault();
				var CSysBackup = new ClassSysBackup();
				CSysBackup.loadContent();
			});

			$('.systemInfoPgh').click(function(e) {
				e.preventDefault();
				var CSystemInfo = new SystemInfo();
				CSystemInfo.loadContent();
			});

			$('.licensePgh').click(function(e) // 라이선스
			{
				e.preventDefault();
				var CLicense = new License();
				CLicense.loadContent();
			});

			$('.auditLogPgh').click(function(e) {
				e.preventDefault();
				var CAuditLog = new ClassAuditLog();
				CAuditLog.loadContent();
			});

			$('.configPgh').click(function(e) // 환경설정
			{
				e.preventDefault();
				var CConfig = new Config();
				CConfig.loadContent();
			});

			$('.roleMgmtPgh').click(function(e) {
				e.preventDefault();
				var CSysSetting = new SysSetting();
				CSysSetting.loadRoleMgmtContent();
			});

			$('.sysMgmtPgh').click(function(e) {
				e.preventDefault();
				var CSysSetting = new SysSetting();
				CSysSetting.loadSysMgmtContent();
			});

			// $('.productPgh').click(function(e) //제품관리
			// {
			// e.preventDefault();
			// var CConfig = new Config();
			// product.loadProductContent();
			// });
		}
	},
});
