var PortChangeWndPASK =  Class.create({
	initialize : function() 
	{
		this.$memberTRs;
	},
	popUp : function($memberTRs) 
	{
		this.$memberTRs = $memberTRs;
		with (this) 
		{
			showPopup({
				"id" : "#portChange",
				"width" : "494px"
			});
			_registerEvents();
		}
	},
	_registerEvents : function() 
	{
		with (this) 
		{			
			$('.cloneDiv .onOk').click(function(e) 
			{
				e.preventDefault();
				var portNo = $('.cloneDiv .portNo').val();
				FlowitUtil.log('portNo: ' + portNo);
				
				// from GS. #4012-1 #11, #3984-1 #1: 14.07.29 sw.jung 포트 유효성 검사 추가
//				if (!$.isNumeric(portNo)) {
//					alert(VAR_VPPORT_PORNNUM);
//					return;
//				}
				if (!$('.cloneDiv .portNo').validate(
					{
						name: $('.cloneDiv span').text(),
						required: true,
						type: "number",
						range: [1,65535]
					}))
					return;
				
				$memberTRs.children().filter(':nth-child(4)').text(portNo);
				$('.cloneDiv .close').click();
			});
			
			$('.cloneDiv .onCancel').click(function(e) 
			{
				e.preventDefault();
				$('.cloneDiv .close').click();
			});
		}
	}
});