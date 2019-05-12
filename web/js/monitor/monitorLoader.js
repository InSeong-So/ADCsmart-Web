var MonitorLoader = Class.create({
	initialize : function() {
		this.selectedTab = 0;
		this.networkMap = new NetworkMap();
		this.statistics = new Statistics();
		this.alertMonitoring = new AlertMonitoring();		
	},
	getSelectedTab : function() 
	{
		return this.selectedTab;
	},
	setSelectedTab : function(selectedTab) 
	{
		this.selectedTab = selectedTab;
	},
	loadContent : function(index, taskQ) {
		with (this) {
			if (!adcSetting.isAdcSet()) {
				$('#wrap .contents').empty();
				if (taskQ)
					taskQ.notifyTaskDone();				
				return;
			}			
			FlowitUtil.log("index: %s", adcSetting.getAdc().index);
			if (undefined === index) {
				index = 0;
			}
			if (0 === index) {
				networkMap.loadNetworkMapContent(adcSetting.getAdc(), undefined, undefined, taskQ);
			} else if (3 === index) {
				statistics.loadStatisticsContent(adcSetting.getAdc());		
			} else if (4 === index) {
				alertMonitoring.loadListContent();
			}
			selectedTab = index;
		}
	},
	onAdcChange : function() {
		with (this) {
			if (!adcSetting.isAdcSet()) {
				return;
			}
			
			FlowitUtil.log("index: %s", adcSetting.getAdc().index);
			if (0 === this.selectedTab) {
				networkMap.loadNetworkMapContent(adcSetting.getAdc());
			} else if (3 === this.selectedTab) {
				statistics.loadStatisticsContent(adcSetting.getAdc());
			} else if (4 === this.selectedTab) {
				alertMonitoring.loadListContent();
			}
		}
	}
});