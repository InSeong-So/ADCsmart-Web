/**
 * <pre>
 * <Openbase Alert/Confirm Plugin>
 * #5491 alert(경고창) 프레임워크 개발
 * 
 * - 실행 스펙
 * $.obAlert({ // 경고창 실행 함수
 *   type: "alert|confirm", // Default: "alert", alert일 경우 확인 버튼만, confirm일 경우 확인/취소 버튼을 제공한다.
 *   backdrop: true|false, // Default: true, true일 경우 뒤에 옅은 검은 배경을 배치
 *   width: '500px', // Default: 500px, 경고창의 너비를 결정(높이는 컨텐츠 길이에 따라 결정)
 *   title: "제목", // Default: undefined, 없을 경우 제목줄 표시 안함
 *   content: "* Textile 형태의 문법\n=> (Redmine 사용 문법)", // Default: "", 비어있는 스트링
 *   occurTime: true|false, // Default: true, 경고창 발생 시간을 표시할지 여부
 *   detail: exception, // Default: undefined, 자세히 보기 토글을 통해 펼쳐볼 수 있는 항목. 스택 트레이스 등 출력하며, 없을 경우 토글 표시 안함
 *   mailTo: "oblab@openbase.co.kr", // Default: undefined, 메일주소가 정의되어 있을 경우 오류보고 보내기 버튼 활성, 없을 경우 표시 안함
 *   buttons : [{ // Default: Default: undefined, 정의되어 있을 경우 창 좌하단 커스텀 버튼 셋 생성
 *     label: "새로고침", // Default: "", 버튼에 표시될 이름
 *     onClick: function(){window.location.reload();}  // Default: undefined, 버튼 클릭시 동작할 함수
 *   }, ...]
 * });
 * 
 * - 함수
 * $.obAlert('close'); : 활성화된 obAlert을 닫는다.
 * </pre>
 * @author sw.jung
 */
$(function()
{
	var TEMPLATES =
	{
		backdrop: '<div class="ob-alert-backdrop"></div>',
		modal: '<div class="ob-alert-modal"></div>',
		title: '<div class="ob-alert-title"></div>',
		occurTime: '<div class="ob-alert-occurtime"></div>',
		content: '<div class="ob-alert-content"></div>',
		detailToggle: '<a href="javascript:;" class="ob-alert-detail-toggle"></a>',
		detail: '<pre class="ob-alert-detail"></pre>',
		buttons: '<div class="ob-alert-buttons"></div>',
		okButton: '<button type="button" class="ob-alert-button-default pull-right"></button>',
		cancelButton: '<button type="button" class="ob-alert-button-danger pull-right"></button>',
		mailToButton: '<button type="button" class="ob-alert-button-default"></button>',
		customButton: '<button type="button" class="ob-alert-button-default"></button>',
		clearfix: '<div class="clearfix"></div>'
	};
	
	var MESSAGES = // FIXME: i18n
	{
		MSG_ALERT_TITLE_ALERT: '경고',
		MSG_ALERT_TITLE_NOTICE: '알림',
		MSG_ALERT_TITLE_ERROR: '오류',
		MSG_ALERT_DETAIL: '자세히',
		MSG_ALERT_MAILTO: '오류보고',
		MSG_ALERT_OK: '확인',
		MSG_ALERT_CANCEL: '취소'
	};
	
	var DEFAULT_SETTINGS =
	{
		type: 'alert',
		backdrop: true,
		width: '500px',
		title: MESSAGES.MSG_ALERT_TITLE_DEFAULT,
		occurTime: true,
		content: undefined,
		detail: undefined,
		mailTo: undefined,
		buttons : []
	};
	
	var method =
	{
		open: function(options)
		{
			// 경고창 중복 생성 방지
			if ($('#obAlert').length > 0)
				return false;
			
			var defaultSettings = $.extend({}, DEFAULT_SETTINGS);
			var settings = $.extend(defaultSettings, options);
			init(settings);
		},
		close: function()
		{
			$('#obAlert').remove();
			$('#obBackdrop').remove();
//			if($('#obAlertDetail').text() == "")
//				window.location.href = '/index.html';
		},
		sessionClose: function()
		{
			window.location.href = '/index.html';
		},
		moveToCenter: function($modal)
		{
			var top, left;

		    top = '100px';
		    left = Math.max($(window).width() - $modal.outerWidth(), 0) / 2;

		    $modal.css({
		        top:top, 
		        left:left
		    });
		},
		sendMail: function(mailTo)
		{
			return function(e)
			{
				var title = $('#obAlertTitle').text();
				var occurTime = $('#obAlertOccurTime').text();
				var content = $('#obAlertContent').text();
				var detail = $('#obAlertDetail').text();
				$mailToForm = $(['<form method="POST" action="mailto:' + mailTo + '" enctype="text/plain">',
					                 '<input type="text" name="title" value="' + encodeURIComponent(title) + '">',
					                 '<input type="text" name="occurTime" value="' + encodeURIComponent(occurTime) + '">',
					                 '<input type="text" name="content" value="' + encodeURIComponent(content) + '">',
					                 '<input type="text" name="detail" value="' + encodeURIComponent(detail) + '">',
				                 '</form>'].join('')).submit();
			};
		}
	};
	
	var initTitle = function($modal, title)
	{
		var $title = undefined;
		if (title)
		{
			$title = $(TEMPLATES.title).attr('id', 'obAlertTitle');			
			$title.text(title);
			$modal.append($title);
		}
		return $title;
	};
	
	var initOccurTime = function($modal, occurTime)
	{
		var $occurTime = undefined;
		
		if (occurTime)
		{
			$occurTime = $(TEMPLATES.occurTime).attr('id', 'obAlertOccurTime');
			$occurTime.text(moment().format('YYYY-MM-DD HH:mm:ss'));
			$modal.append($occurTime);
			
			$modal.append($(TEMPLATES.clearfix));
		}
		return $occurTime;
	};
	
	var initContent = function($modal, content)
	{
		var $content = undefined;
		if (content)
		{
			$content = $(TEMPLATES.content).attr('id', 'obAlertContent');
			$content.html(textile(content));
			$modal.append($content);
		}
		return $content;
	};

	var initDetail = function($modal, detail)
	{
		var $detail = undefined;
		if (detail)
		{
			var $detailToggle = $(TEMPLATES.detailToggle);
			$detailToggle.append('<span>' + MESSAGES.MSG_ALERT_DETAIL + '</span>');
			$modal.append($detailToggle);
			$detailToggle.click(function()
			{
				$(this).toggleClass('on');
				if ($(this).hasClass('on'))
					$(this).next().show();
				else
					$(this).next().hide();
			});
			
			$detail = $(TEMPLATES.detail).attr('id', 'obAlertDetail').hide();
			if (typeof detail == 'object')
				$detail.text(JSON.stringify(detail, null, 2));
			else
				$detail.text(detail);
			$modal.append($detail);
		}
		return $detail;
	};
	
	var initButtons = function($modal, type, mailTo, buttons)
	{
		var $buttons = $(TEMPLATES.buttons);
		
		// init mailTo Button
		if (mailTo)
		{
			var $mailToButton = $(TEMPLATES.mailToButton);
			$mailToButton.html(MESSAGES.MSG_ALERT_MAILTO);
			$buttons.append($mailToButton);
			$mailToButton.click(method.sendMail(mailTo));
		}
		
		// init custom Button
		var $customButton;
		for (var i = 0; i < buttons.length; i ++)
		{
			$customButton = $(TEMPLATES.customButton);
			if (buttons[i].label)
				$customButton.text(buttons[i].label);
			$buttons.append($customButton);
			if (buttons[i].onClick)
				$customButton.click(buttons[i].onClick);
		}
		
		// init cancel Button
		if (type == 'confirm')
		{
			var $cancelButton = $(TEMPLATES.cancelButton);
			$cancelButton.text(MESSAGES.MSG_ALERT_CANCEL);
			$buttons.append($cancelButton);
			$cancelButton.click(method.close);
		}
		
		// init ok Button
		var $okButton = $(TEMPLATES.okButton);
		$okButton.text(MESSAGES.MSG_ALERT_OK);
		$buttons.append($okButton);
		$okButton.click(method.close);
		
		// clear float
		$buttons.append($(TEMPLATES.clearfix));
		
		$modal.append($buttons);
		
		return $buttons;
	};
	
	var init = function(settings)
	{
		var $body = $('body');
		var $modal = $(TEMPLATES.modal).attr('id','obAlert').css('width', settings.width);
		
		initTitle($modal, settings.title);
		initOccurTime($modal, settings.occurTime);
		initContent($modal, settings.content);
		initDetail($modal, settings.detail);
		initButtons($modal, settings.type, settings.mailTo, settings.buttons);
		method.moveToCenter($modal);
		
		if (settings.backdrop)
		{
			var $backdrop = $(TEMPLATES.backdrop).attr('id','obBackdrop');
			$body.append($backdrop);
		}

		$body.append($modal);
	};
	
	/**
	 * obAlert 호출 함수. 사용법은 본 문서 최상단에 위치한 doc 참고
	 */
	$.obAlert = function(options)
	{
		if (options == 'close')
			method.close();
		else
			method.open(options);
	};
	
	/**
	 * <pre>
	 * 일반 메세지 처리를 위한 프리셋
	 * 내용만 출력한다.
	 * </pre>
	 */
	$.obAlertNotice = function(message, options)
	{
		var noticeSettings =
		{
			title: MESSAGES.MSG_ALERT_TITLE_NOTICE,
			occurTime: false,
			content: message
		};
		$.obAlert($.extend(noticeSettings, options));
	};
	
	/**
	 * <pre>
	 * 에러 메세지 처리를 위한 프리셋
	 * 내용과 상세내용을 표시하는 obAlert
	 * </pre>
	 */
	$.obAlertError = function(message, detail, options)
	{
		var errorSettings =
		{
			title: MESSAGES.MSG_ALERT_TITLE_ERROR,
			content: message,
			detail: detail
		};
		$.obAlert($.extend(errorSettings, options));
	};
	
	$.obAlertErrorClose = function(options)
	{		
		if (options)
			method.sessionClose();
		else
			method.open(options);
	};
	
	/**
	 * <pre>
	 * ajax 에러 메세지 처리를 위한 프리셋
	 * 상세내용으로 jqXhr을 파싱, responseText가 있을 경우 해당 내용을 출력하며, 없을 시 jqXhr 객체 자체를 출력한다.
	 * </pre>
	 */
	$.obAlertAjaxError = function(message, jqXhr, options)
	{
		var detail = jqXhr;
		var responseText = jqXhr.responseText;
		if (responseText && responseText != '')
			detail = $.parseResponseText(responseText);
		
//		detail = window.location.href = '/index.html';
		
		if(jqXhr.responseText.indexOf('/index.html') > -1)
			$.obAlertErrorClose(true);
		else
			$.obAlertError(message, detail, options);
	};
	
	/**
	 * ajax의 ResponseText를 파싱해 메세지만 추출한다.
	 */
	$.parseResponseText = function(responseText)
	{
		return $.map($.parseHTML(responseText),function(node)
		{
			if (node.nodeName != 'STYLE' && node.nodeName != 'SCRIPT')
				return $(node).text();
		}).join('\n');
	};
});