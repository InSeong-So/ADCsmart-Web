var MemberAddBatchWndPASK =  Class.create({
	initialize : function() {
	},
	popUp : function() {
		with (this) {
			showPopup({
				"id" : "#memberAddBatch",
				"width" : "494px"
			});
			_registerEvents();
		}
	},
	_registerEvents : function() {
		with (this) {
			$('.cloneDiv .onOk').click(function(e) {
				e.preventDefault();
				
				var ips = $('.cloneDiv textarea[name="ipsTxt"]').val();
				FlowitUtil.log(ips);
				var ipArray = ips.split('\n');
				FlowitUtil.log(ipArray.length);
				if (!_validate(ipArray)) {
					$.obAlertNotice(VAR_VPMEMBER_IPNFORMAT);
					return;
				}
				
				var ipAndPorts = _getIpAndPorts(ipArray);
				FlowitUtil.log(Object.toJSON(ipAndPorts));
				virtualServer.paskVs.addIpAndPortsToMemberTable(ipAndPorts);
				$('.cloneDiv .close').click();
			});
			
			$('.cloneDiv .onCancel').click(function(e) {
				e.preventDefault();
				$('.cloneDiv .close').click();
			});
		}
	},
	_validate : function(ips) {
		for (var i=0; i < ips.length; i++) {
			var ipAndPort = $.map($.trim(ips[i]).split(':'), function(val, i) {
				return $.trim(val);
			});
			
			FlowitUtil.log("ipAndPort: %s", ipAndPort);
			if (ipAndPort.length == 1 && !ipAndPort[0])		// skip empty string.
				continue;
			
			if (ipAndPort.length < 2 || !FlowitUtil.checkIp(ipAndPort[0]) || !$.isNumeric(ipAndPort[1]))
				return false;
		}
		
		return true;
	},
	_getIpAndPorts : function(ips) {
		return $.map(ips, function(val, i) {
			FlowitUtil.log(i + ': ' + val);
			if (!val)
				return undefined;
			
			var ipAndPort = $.map($.trim(val).split(':'), function(val, i) {
				return $.trim(val);
			});
			FlowitUtil.log(ipAndPort);
			
			if (ipAndPort.length < 2 || !FlowitUtil.checkIp(ipAndPort[0]) || !$.isNumeric(ipAndPort[1]))
				return undefined;
			
			return {
				"ip" : ipAndPort[0],
				"port" : ipAndPort[1]
			};
		});
	}
});
