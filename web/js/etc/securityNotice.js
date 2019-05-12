var SecurityNoticeWnd =  Class.create({
	initialize : function() 
	{
		this.isPropertySet = false;
	},
	popUpIfPropertySet : function(onCloseFn) 
	{
		with (this) 
		{
			ajaxManager.runJson({
				url : "loadConfigContent.action",
				data : 
				{
				},
				successFn : function(data) 
				{		//alert(data.isSecurityNoticeShown);
					FlowitUtil.log("isSecurityNoticeShown is : ", data.isShown);
					if (data.isShown)
					{
						_popUp(onCloseFn, data);
					}
					else 
					{
						if (onCloseFn)
							onCloseFn();
					}
				}
			});
		}
	},
	_popUp : function(onCloseFn, data) 
	{
		with (this) 
		{
			showPopup({
				'id' : '#noticePop',
				'width' : '460px',
				'height' : '185px'
			});
//			var $pop = null;
//			// 팝업창 열기
//			$pop = $("#noticePop").clone();
//			$pop.addClass('cloneDiv');
//			$pop.css('width', "460px");  // 380에서 수정 0805
////			$pop.css('height', "285px");  //내용에 따라서 팝업의 height가 늘어나지만 현재는 내용에 따라서 $pop.height() 값을 customize 해야함.
//			$('body').append("<div class='popup_type1'></div>");
//			$('body').append($pop);
//			$pop.show();// .draggable();			
//			
//			$pop.find('.pop_contents').css('height', opt['height']);
//			
//			alert("window width : " + $(window).width());			//1263
//			alert("window height : " + $(window).height());			//839
//			alert("$pop width : " + $pop.width());					//460
//			alert("$pop height : " + $pop.height());				//112
//			// 팝업창 정중앙 위치
//			$pop.css({
//			    'left' : ($(window).width() - $pop.width()) / 2,
//			    'top' : ($(window).height() - $pop.height()) / 2
//			});
//			$('.popup_type1').height($('body').height());
			_registerEvents(onCloseFn);
			$('.cloneDiv .pop_contents .pop_contents2').html(data.content);
			$('.cloneDiv .closeWndLnk').focus();
		}
	},
	_registerEvents : function(onCloseFn)
	{
		$('.cloneDiv .closeWndLnk').click(function(e)
		{
			e.preventDefault();
			$('.popup_type1').remove();
			$('.cloneDiv').remove();
			if (onCloseFn)
				onCloseFn();
		});		 
		/*document.onkeydown = function(e)	// keyCode 13번 = Enter 키
		{
			if (event.keyCode == 13)
			{
				$('.popup_type1').remove();
				$('.cloneDiv').remove();				
				if (onCloseFn)
					onCloseFn(); 
			}			
		};*/
	}
});
