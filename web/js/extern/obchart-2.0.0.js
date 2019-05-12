var OBChart=Class.create(
{
	initialize:function()
	{
		this.varInitCount4Realtime = 50;	// data를 50개까지만 보여준다.
	},
	/*
	 * 모듈명 : OBAllChartUpdate
	 * 성격 : ADCsmartAX 전체 차트 Data Validate 함수
	 * 매개변수 : charObj, chartData
	 * 사용처 : ADCsmartAX 전체 차트
	 * */
	// 
	OBAllChartUpdate : function(charObj, chartData, chartOption)
	{
		with (this) 
		{ 
			charObj.dataProvider = chartData;
			if (chartOption)
			{
				charObj.chartCursor.cursorColor = chartOption.cursorColor;
				//charObj.valueAxes[0].axisColor = chartOption.linecolor;
				charObj.graphs[0].bulletColorR = chartOption.linecolor;
				charObj.graphs[0].bulletColorR = chartOption.linecolor;
				charObj.graphs[0].lineColor = chartOption.linecolor;
				charObj.graphs[0].lineColorR = chartOption.linecolor;
				charObj.graphs[0].connect = false;
				charObj.graphs[0].gapPeriod = chartOption.interval;
			}			
			charObj.validateData();
		}
	},
	/*
	 * 모듈명 : OBAllChartSPCpuUpdate
	 * 성격 : ADCsmartAX SP CPU 차트 Data Validate 함수
	 * 매개변수 : charObj, chartData
	 * 사용처 : ADCsmartAX SP CPU 차트
	 * */
	// 
	OBAllChartSPCpuUpdate : function(charObj, chartData, chartOption)
	{
		with (this) 
		{ 
			charObj.dataProvider = chartData;
			if (chartOption)
			{
				charObj.chartCursor.cursorColor = chartOption.cursorColor;
				//charObj.valueAxes[0].axisColor = chartOption.linecolor;
				charObj.graphs[0].bulletColorR = chartOption.linecolor;
				charObj.graphs[0].bulletColorR = chartOption.linecolor;
				charObj.graphs[0].lineColor = chartOption.linecolor;
				charObj.graphs[0].lineColorR = chartOption.linecolor;
				charObj.allLabels = [{"align": "right", 
					"bold": false, 
					"id": "Label-1", 
					"text": "[Session] critical value: " + chartOption.spSessionMaxUnit + chartOption.axistitle2,
					"x": "210",
					"y": "10"
				}];				
			}			
			charObj.validateData();
		}
	},
	// RealTimeChart를 초기 load 할때 처음 50개 Data를 set 한다.
	OBSetRealTimeChart : function(chartObject, chartData)
	{
		with (this) 
		{
			chartObject.dataProvider.push(chartData);
			chartObject.validateData();
		}		
	},
	// RealTimeChart Data를 Update 한다.
	OBUpdateRealTimeChart : function(chartObject, dataObject)
	{
		with (this) 
		{
			if(chartObject.dataProvider.length >= varInitCount4Realtime)
				chartObject.dataProvider.shift();
			chartObject.dataProvider.push(dataObject);
			chartObject.validateData();
		}
	},
	/*
	 * 모듈명 : OBCreateRealTimeChart
	 * 성격 : One Value 실시간 Chart Chart
	 * 매개변수 : chartOptionObj (min, max, linecolor, chartname, chartunit, cursorColor)
	 * 사용처 : 진단 팝업 Memory Chart, 패킷 분석 팝업 Memory Chart
	 * */
	OBCreateRealTimeChart : function(chartOptionObj)
	{
		with (this) 
		{
			function initChartData() 
			{
				try
				{
					var chartData = [];
				    return chartData;
				}
				finally
				{
					chartData = null;
				}
			}
			var chartOption = chartOptionObj;
			var chartData = initChartData();			
			
            // SERIAL CHART 옵션
            var chart = new AmCharts.AmSerialChart();
            chart.pathToImages = "/js/extern/amcharts_images/";
            
            chart.dataProvider = chartData;
            chart.categoryField = "occurredTime";
            chart.gridAboveGraphs= "true";            
            chart.autoMarginOffset = 5;

            // 대상이 없을때 보여지는 챠트  양 사이드 border            
            chart.plotAreaBorderAlpha =  1;
            chart.plotAreaBorderColor =  '#e1e1e1'; 
            
            chart.autoMargins =  false;
            chart.columnsAboveLines = true;
            chart.marginTop = 5;
            chart.marginBottom = 19;
            chart.marginLeft = 50;
            chart.marginRight = 20;             
            
            chart.fontFamily = "Arial";
            chart.fontSize = 10;
                          
            chart.zoomOutButton = 
            {
            	backgroundColor: '#000000',
	            backgroundAlpha: 0.05,
	            fontSize : 9
            };
            chart.zoomOutButtonImageSize = 14;
            chart.usePrefixes = true;
            chart.prefixesOfBigNumbers = [{number:1e+3,prefix:"k"},
                                          {number:1e+6,prefix:"M"},
                                          {number:1e+9,prefix:"G"}];

             // 처음 로드 화면에 zoom 기능을 사용하고 싶다면 ("dataUpdated", zoomChart); 로 add 후 조절은 zoomChart method 에서한다.
            //chart.addListener("dataUpdated", zoomChart);

            // Axes
            // X축 Category Axis (on the down)               
            var categoryAxis = chart.categoryAxis;
            categoryAxis.parseDates = true; // as our data is date-based, we set parseDates to true
            categoryAxis.minPeriod = "ss"; // our data is daily, so we set minPeriod to DD
            categoryAxis.dashLength = 2;
            categoryAxis.dateFormats = [{period:'ss',format:'JJ:NN:SS'},
                                        {period:'mm',format:'JJ:NN'},
                                        {period:'hh',format:'JJ:NN'},
                                        {period:'DD',format:'YYYY-MM-DD'},
                                        {period:'WW',format:'YYYY-MM-DD'},
                                        {period:'MM',format:'MM'},
                                        {period:'YYYY',format:'YYYY'}];
            categoryAxis.gridAlpha = 0.3;

            // Y축 value axis (on the left)
            var valueAxis1 = new AmCharts.ValueAxis();
            valueAxis1.stackType = "none";
            valueAxis1.tickLength = 0;
           // valueAxis1.axisColor = chartOption.linecolor;
            valueAxis1.dashLength = 2;
            valueAxis1.gridAlpha = 0.3;
            valueAxis1.axisThickness = 1;
            valueAxis1.usePrefixes = true;
            valueAxis1.prefixesOfBigNumbers = [
                                               {number:1e+3,prefix:"k"},
                                               {number:1e+6,prefix:"M"},
                                               {number:1e+9,prefix:"G"}];
            valueAxis1.minimum = chartOption.min;            
            //valueAxis1.titleColor = linecolor;
            valueAxis1.titleFontSize = 11;
            valueAxis1.unit = chartOption.axistitle;
            
            
            if (chartOption.max != null)
            {
            	valueAxis1.maximum = chartOption.max;
            }
            chart.addValueAxis(valueAxis1);             

            // GRAPHS
            //  graph 
            var graph = new AmCharts.AmGraph();
            graph.valueAxis = valueAxis1; // we have to indicate which value axis should be used
            graph.title = "red line";
            graph.valueField = "value";
            //graph1.bullet = "round";
            //graph1.hideBulletsCount = 5; 
            graph.type = "line";
            graph.fillAlphas = 0;
            graph.lineColor = chartOption.linecolor;
            graph.lineThickness = "1";
            graph.balloonText="[[value]]" + chartOption.axistitle;
            chart.addGraph(graph);                        

            // Balloon             
            var balloon = chart.balloon;
            balloon.adjustBorderColor = true;
            balloon.pointerWidth = 3;
            balloon.shadowAlpha = 0; 
            balloon.borderThickness = 1;
            balloon.fillAlpha = 0.95;
            balloon.horizontalPadding = 6;
            balloon.verticalPadding = -2; 
            balloon.fontSize = 8;              
            
            // CURSOR
            var chartCursor = new AmCharts.ChartCursor();
            chartCursor.cursorPosition = "middle";
            chartCursor.categoryBalloonDateFormat = "JJ:NN:SS";
            chartCursor.zoomable = true;
            chartCursor.cursorColor= chartOption.cursorColor;
            chart.addChartCursor(chartCursor);

             // WRITE
             chart.write(chartOption.chartname);          

         // this method is called when chart is first inited as we listen for "dataUpdated" event             
             function zoomChart()
	        {
	        	// different zoom methods can be used - zoomToIndexes, zoomToDates, zoomToCategoryValues
	        	chart.zoomToIndexes(10, 20);
	        }
	        
            try
			{
            	return chart;
			}
            finally
            {
            	chart = null;
            }
		}		
	},
	/*
	 * 모듈명 : OBCreateRealTimeMultiValueChart
	 * 성격 : Multi Value 실시간 Chart Chart
	 * 매개변수 : (min, max, chartname, chartunit1, chartunit2, adcType)
	 * 사용처 : 진단 팝업 CPU Chart, 패킷 분석 팝업 CPU Chart
	 * */
	OBCreateRealTimeMultiValueChart : function(min, max, chartname, chartunit1, chartunit2, adcType)
	{
		with (this) 
		{      
			function initChartData() 
			{
				try
				{
					var chartData = [];
				    return chartData;
				}
				finally
				{
					chartData = null;
				}
			}
//			var chartData = chartDatalist;
			var chartData = initChartData();			
			
            // SERIAL CHART 옵션
            var chart = new AmCharts.AmSerialChart();
            chart.pathToImages = "/js/extern/amcharts_images/";
            
            chart.dataProvider = chartData;
            chart.categoryField = "occurredTime";
            chart.gridAboveGraphs= "true";            
            chart.autoMarginOffset = 5;

            // 대상이 없을때 보여지는 챠트  양 사이드 border            
            chart.plotAreaBorderAlpha =  1;
            chart.plotAreaBorderColor =  '#e1e1e1'; 
            
            chart.autoMargins =  false;
            chart.columnsAboveLines = true;
            chart.marginTop = 5;
            chart.marginBottom = 19;
            chart.marginLeft = 50;
            chart.marginRight = 20;             
            
            chart.fontFamily = "Arial";
            chart.fontSize = 10;
                          
            chart.zoomOutButton = 
            {
            	backgroundColor: '#000000',
	            backgroundAlpha: 0.05,
	            fontSize : 9
            };
            chart.zoomOutButtonImageSize = 14;
            chart.usePrefixes = true;
            chart.prefixesOfBigNumbers = [{number:1e+3,prefix:"k"},
                                          {number:1e+6,prefix:"M"},
                                          {number:1e+9,prefix:"G"}];
             // 처음 로드 화면에 zoom 기능을 사용하고 싶다면 ("dataUpdated", zoomChart); 로 add 후 조절은 zoomChart method 에서한다.
            //chart.addListener("dataUpdated", zoomChart);

            // Ballon Option (Alteon CPU Chart 만 유효)
            var balloon = chart.balloon;
            // set properties
            var balloon = chart.balloon;
            balloon.adjustBorderColor = true;
            balloon.pointerWidth = 3;
            balloon.shadowAlpha = 0; 
            balloon.borderThickness = 1;
            balloon.fillAlpha = 0.95;
            balloon.horizontalPadding = 6;
            balloon.verticalPadding = -2; 
            balloon.fontSize = 8; 
            
            // Axes
            // X축 Category Axis (on the down)               
            var categoryAxis = chart.categoryAxis;
            categoryAxis.parseDates = true; // as our data is date-based, we set parseDates to true
            categoryAxis.minPeriod = "ss"; // our data is daily, so we set minPeriod to DD
            categoryAxis.dashLength = 2;
            categoryAxis.dateFormats = [{period:'ss',format:'JJ:NN:SS'},
                                        {period:'mm',format:'JJ:NN'},
                                        {period:'hh',format:'JJ:NN'},
                                        {period:'DD',format:'YYYY-MM-DD'},
                                        {period:'WW',format:'YYYY-MM-DD'},
                                        {period:'MM',format:'MM'},
                                        {period:'YYYY',format:'YYYY'}];
            categoryAxis.gridAlpha = 0.3;

            // Y축 value axis (on the left)
            var valueAxis1 = new AmCharts.ValueAxis();
            valueAxis1.stackType = "none";
            valueAxis1.tickLength = 0;
            valueAxis1.dashLength = 2;
            valueAxis1.gridAlpha = 0.3;
           // valueAxis1.axisColor = "#FFD300";
            valueAxis1.axisThickness = 1;
            valueAxis1.usePrefixes = true;
            valueAxis1.prefixesOfBigNumbers = [
                                               {number:1e+3,prefix:"k"},
                                               {number:1e+6,prefix:"M"},
                                               {number:1e+9,prefix:"G"}];
            valueAxis1.minimum = min;            
            //valueAxis1.titleColor = linecolor;
            valueAxis1.titleFontSize = 11;
            valueAxis1.unit = chartunit1;
            
            if (chartunit2 != null )
            {
            	// Y축 value axis2 (on the right)
                var valueAxis2 = new AmCharts.ValueAxis();
                valueAxis2.stackType = "none";
                valueAxis2.tickLength = 0;
              //  valueAxis2.axisColor = "#ff8c00";
                valueAxis2.axisThickness = 1;
                valueAxis2.usePrefixes = true;
                valueAxis2.prefixesOfBigNumbers = [
                                                   {number:1e+3,prefix:"k"},
                                                   {number:1e+6,prefix:"M"},
                                                   {number:1e+9,prefix:"G"}];
                valueAxis2.minimum = min;            
                //valueAxis1.titleColor = linecolor;
                valueAxis2.titleFontSize = 11;
                valueAxis2.unit = chartunit2;
            }
            
            if (max != null)
            {
            	valueAxis1.maximum = max;
            }
            chart.addValueAxis(valueAxis1);             

            // GRAPHS
            //  graph 1
            var graph1 = new AmCharts.AmGraph();
            graph1.valueAxis = valueAxis1; // we have to indicate which value axis should be used
            graph1.title = "line1";
            graph1.valueField = "Value1";
            //graph1.bullet = "round";
            //graph1.hideBulletsCount = 5; 
            graph1.type = "line";
            //graph1.fillAlphas = 0.4;
            graph1.lineColor = "#FFD300";
            graph1.lineThickness = "1";
            if (adcType == "Alteon")
            {
            	graph1.balloonText="MP Usage: [[value]]" + chartunit1;
            }
            else
            {
            	graph1.balloonText="CPU1 Usage: [[value]]" + chartunit1;
            }            
            chart.addGraph(graph1);
            
            // graph 2               
            var graph2 = new AmCharts.AmGraph();
            graph2.valueAxis = valueAxis1; // we have to indicate which value axis should be used
            graph2.title = "line2";
            graph2.valueField = "Value2";
            //graph2.bullet = "round";
            //graph2.hideBulletsCount = 5; 
            graph2.type = "line";
            //graph2.fillAlphas = 0.4;
            graph2.lineColor = "#ffb6c1";
            graph2.lineThickness = "1";
            if (adcType == "Alteon")
            {
            	graph2.balloonText="SP1 Usage: [[value]]" + chartunit1;
            }
            else
            {
            	graph2.balloonText="CPU2 Usage: [[value]]" + chartunit1;
            }
            // graph1.fillColors = "#C0CEE9";
            chart.addGraph(graph2);

            // graph 3
            var graph3 = new AmCharts.AmGraph();
            graph3.valueAxis = valueAxis1; // we have to indicate which value axis should be used
            graph3.valueField = "Value3";
            graph3.title = "line3";
            //graph3.bullet = "round";
            //graph3.hideBulletsCount = 5; 
            graph3.type = "line";
            //graph3.fillAlphas = 0.4;
            graph3.lineColor = "#9acd32";
            graph3.lineThickness = "1";
            if (adcType == "Alteon")
            {
            	graph3.balloonText="SP2 Usage: [[value]]" + chartunit1;
            }
            else
            {
            	graph3.balloonText="CPU3 Usage: [[value]]" + chartunit1;
            }            
            // graph1.fillColors = "#C0CEE9";
            chart.addGraph(graph3);
            
            // graph 4
            var graph4 = new AmCharts.AmGraph();
            graph4.valueAxis = valueAxis1; // we have to indicate which value axis should be used
            graph4.valueField = "Value4";
            graph4.title = "line4";
            //graph4.bullet = "round";
            //graph4.hideBulletsCount = 5; 
            graph4.type = "line";
            //graph4.fillAlphas = 0.4;
            graph4.lineColor = "#daa520";
            graph4.lineThickness = "1";
            if (adcType == "Alteon")
            {
            	graph4.balloonText="SP3 Usage: [[value]]" + chartunit1;
            }
            else
            {
            	graph4.balloonText="CPU4 Usage: [[value]]" + chartunit1;
            }            
            // graph1.fillColors = "#C0CEE9";
            chart.addGraph(graph4);
            
            // graph 5
            var graph5 = new AmCharts.AmGraph();
            graph5.valueAxis = valueAxis1; // we have to indicate which value axis should be used
            graph5.valueField = "Value5";
            graph5.title = "line5";
            //graph5.bullet = "round";
            //graph5.hideBulletsCount = 5; 
            graph5.type = "line";
            //graph5.fillAlphas = 0.4;
            graph5.lineColor = "#ff1493";
            graph5.lineThickness = "1";
            if (adcType == "Alteon")
            {
            	graph5.balloonText="SP4 Usage: [[value]]" + chartunit1;
            }
            else
            {
            	graph5.balloonText="CPU5 Usage: [[value]]" + chartunit1;
            }            
	        // graph1.fillColors = "#C0CEE9";
            chart.addGraph(graph5);
            
            // graph 6
            var graph6 = new AmCharts.AmGraph();
            graph6.valueAxis = valueAxis1; // we have to indicate which value axis should be used
            graph6.valueField = "Value6";
            graph6.title = "line6";
            //graph6.bullet = "round";
            //graph6.hideBulletsCount = 5; 
            graph6.type = "line";
            //graph6.fillAlphas = 0.4;
            graph6.lineColor = "#ffd700";
            graph6.lineThickness = "1";
            if (adcType == "Alteon")
            {
            	graph6.balloonText="SP5 Usage: [[value]]" + chartunit1;
            }
            else
            {
            	graph6.balloonText="CPU6 Usage: [[value]]" + chartunit1;
            }            
            // graph1.fillColors = "#C0CEE9";
            chart.addGraph(graph6);
            
            // graph 7
            var graph7 = new AmCharts.AmGraph();
            graph7.valueAxis = valueAxis1; // we have to indicate which value axis should be used
            graph7.valueField = "Value7";
            graph7.title = "line7";
            //graph7.bullet = "round";
            //graph7.hideBulletsCount = 5; 
            graph7.type = "line";
            //graph7.fillAlphas = 0.4;
            graph7.lineColor = "#ff00ff";
            graph7.lineThickness = "1";
            if (adcType == "Alteon")
            {
            	graph7.balloonText="SP6 Usage: [[value]]" + chartunit1;
            }
            else
            {
            	graph7.balloonText="CPU7 Usage: [[value]]" + chartunit1;
            }            
            // graph1.fillColors = "#C0CEE9";
            chart.addGraph(graph7);
            
            // graph 8
            var graph8 = new AmCharts.AmGraph();
            graph8.valueAxis = valueAxis1; // we have to indicate which value axis should be used
            graph8.valueField = "Value8";
            graph8.title = "line8";
            //graph8.bullet = "round";
            //graph8.hideBulletsCount = 5; 
            graph8.type = "line";
            //graph8.fillAlphas = 0.4;
            graph8.lineColor = "#4169e1";
            graph8.lineThickness = "1";
            if (adcType == "Alteon")
            {
            	graph8.balloonText="SP7 Usage: [[value]]" + chartunit1;
            }
            else
            {
            	graph8.balloonText="CPU8 Usage: [[value]]" + chartunit1;
            }           
            // graph1.fillColors = "#C0CEE9";
            chart.addGraph(graph8);
            
            // graph 9
            var graph9 = new AmCharts.AmGraph();
            graph9.valueAxis = valueAxis1; // we have to indicate which value axis should be used
            graph9.valueField = "Value9";
            graph9.title = "line9";
            //graph9.bullet = "round";
            //graph9.hideBulletsCount = 5; 
            graph9.type = "line";
            //graph9.fillAlphas = 0.4;
            graph9.lineColor = "#b5a642";
            graph9.lineThickness = "1";
            if (adcType == "Alteon")
            {
            	graph9.balloonText="SP8 Usage: [[value]]" + chartunit1;
            }
            else
            {
            	graph9.balloonText="CPU9 Usage: [[value]]" + chartunit1;
            }            
            // graph1.fillColors = "#C0CEE9";
            chart.addGraph(graph9);
            
            // graph 10
            var graph10 = new AmCharts.AmGraph();
            graph10.valueAxis = valueAxis1; // we have to indicate which value axis should be used
            graph10.valueField = "Value10";
            graph10.title = "line10";
            //graph10.bullet = "round";
            //graph10.hideBulletsCount = 5; 
            graph10.type = "line";
            //graph10.fillAlphas = 0.4;
            graph10.lineColor = "#ff6347";
            graph10.lineThickness = "1";
            if (adcType == "Alteon")
            {
            	graph10.balloonText="SP9 Usage: [[value]]" + chartunit1;
            }
            else
            {
            	graph10.balloonText="CPU10 Usage: [[value]]" + chartunit1;
            }            
            // graph1.fillColors = "#C0CEE9";
            chart.addGraph(graph10);
            
            // graph 11
            var graph11 = new AmCharts.AmGraph();
            graph11.valueAxis = valueAxis1; // we have to indicate which value axis should be used
            graph11.valueField = "Value11";
            graph11.title = "line11";
            //graph11.bullet = "round";
            //graph11.hideBulletsCount = 5; 
            graph11.type = "line";
            //graph11.fillAlphas = 0.4;
            graph11.lineColor = "#fa8072";
            graph11.lineThickness = "1";
            if (adcType == "Alteon")
            {
            	graph11.balloonText="SP10 Usage: [[value]]" + chartunit1;
            }
            else
            {
            	graph11.balloonText="CPU11 Usage: [[value]]" + chartunit1;
            }
            
            // graph1.fillColors = "#C0CEE9";
            chart.addGraph(graph11);
            
            // graph 12
            var graph12 = new AmCharts.AmGraph();
            graph12.valueAxis = valueAxis1; // we have to indicate which value axis should be used
            graph12.valueField = "Value12";
            graph12.title = "line12";
            //graph12.bullet = "round";
            //graph12.hideBulletsCount = 5; 
            graph12.type = "line";
            //graph12.fillAlphas = 0.4;
            graph12.lineColor = "#ff1493";
            graph12.lineThickness = "1";
            if (adcType == "Alteon")
            {
            	graph12.balloonText="SP11 Usage: [[value]]" + chartunit1;
            }
            else
            {
            	graph12.balloonText="CPU12 Usage: [[value]]" + chartunit1;
            }
            // graph1.fillColors = "#C0CEE9";
            chart.addGraph(graph12);
            
            // graph 13
            var graph13 = new AmCharts.AmGraph();
            graph13.valueAxis = valueAxis1; // we have to indicate which value axis should be used
            graph13.valueField = "Value13";
            graph13.title = "line13";
            //graph13.bullet = "round";
            //graph13.hideBulletsCount = 5; 
            graph13.type = "line";
            //graph13.fillAlphas = 0.4;
            graph13.lineColor = "#228b22";
            graph13.lineThickness = "1";
            if (adcType == "Alteon")
            {
            	graph13.balloonText="SP12 Usage: [[value]]" + chartunit1;
            }
            else
            {
            	graph13.balloonText="CPU13 Usage: [[value]]" + chartunit1;
            }            
            // graph1.fillColors = "#C0CEE9";
            chart.addGraph(graph13);
            
            // graph 14
            var graph14 = new AmCharts.AmGraph();
            graph14.valueAxis = valueAxis1; // we have to indicate which value axis should be used
            graph14.valueField = "Value14";
            graph14.title = "line14";
            //graph14.bullet = "round";
            //graph14.hideBulletsCount = 5; 
            graph14.type = "line";
            //graph14.fillAlphas = 0.4;
            graph14.lineColor = "#eee8aa";
            graph14.lineThickness = "1";
            if (adcType == "Alteon")
            {
            	graph14.balloonText="SP13 Usage: [[value]]" + chartunit1;
            }
            else
            {
            	graph14.balloonText="CPU14 Usage: [[value]]" + chartunit1;
            }
            
            // graph1.fillColors = "#C0CEE9";
            chart.addGraph(graph14);
            
            // graph 15
            var graph15 = new AmCharts.AmGraph();
            graph15.valueAxis = valueAxis1; // we have to indicate which value axis should be used
            graph15.valueField = "Value15";
            graph15.title = "line15";
            //graph15.bullet = "round";
            //graph15.hideBulletsCount = 5; 
            graph15.type = "line";
            //graph15.fillAlphas = 0.4;
            graph15.lineColor = "#f4a460";
            graph15.lineThickness = "1";
            if (adcType == "Alteon")
            {
            	graph15.balloonText="SP14 Usage: [[value]]" + chartunit1;
            }
            else
            {
            	graph15.balloonText="CPU15 Usage: [[value]]" + chartunit1;
            }            
            // graph1.fillColors = "#C0CEE9";
            chart.addGraph(graph15);
            
            // graph 16
            var graph16 = new AmCharts.AmGraph();
            graph16.valueAxis = valueAxis1; // we have to indicate which value axis should be used
            graph16.valueField = "Value16";
            graph16.title = "line16";
            //graph16.bullet = "round";
            //graph16.hideBulletsCount = 5; 
            graph16.type = "line";
            //graph16.fillAlphas = 0.4;
            graph16.lineColor = "#f08080";
            graph16.lineThickness = "1";
            if (adcType == "Alteon")
            {
            	graph16.balloonText="SP15 Usage: [[value]]" + chartunit1;
            }
            else
            {
            	graph16.balloonText="CPU16 Usage: [[value]]" + chartunit1;
            }            
            // graph1.fillColors = "#C0CEE9";
            chart.addGraph(graph16);

            // CURSOR
            var chartCursor = new AmCharts.ChartCursor();
            chartCursor.cursorPosition = "middle";
            chartCursor.categoryBalloonDateFormat = "JJ:NN:SS";
            chartCursor.zoomable = true;
            chartCursor.cursorColor= "#0000FF";
            chart.addChartCursor(chartCursor);

             // WRITE
             chart.write(chartname);          

         // this method is called when chart is first inited as we listen for "dataUpdated" event             
             function zoomChart()
	        {
	        	// different zoom methods can be used - zoomToIndexes, zoomToDates, zoomToCategoryValues
	        	chart.zoomToIndexes(10, 20);
	        }	      
            return chart;			
		}		
	},
	/*
	 * 모듈명 : OBCreateBpsConnRealTimeChart
	 * 성격 : Multi Value 실시간 Chart
	 * 매개변수 : chartOptionObj ( min, max, linecolor1, linecolor2, chartname, axistitle1, axistitle2, maxPos, cursorColor)
	 * 사용처 : 서비스 성능 모니터링 실시간 bps, Conn Chart
	 * 
	 * */	

	OBCreateBpsConnRealTimeChart : function(chartOptionObj)
	{
		with (this) 
		{      
			function initChartData() 
			{
				try
				{
					var chartData = [];
				    return chartData;
				}
				finally
				{
					chartData = null;
				}
			}
			var chartOption = chartOptionObj;
			var chartData = initChartData();			
			
            // SERIAL CHART 옵션
            var chart = new AmCharts.AmSerialChart();
            chart.pathToImages = "/js/extern/amcharts_images/";
            
            chart.dataProvider = chartData;
            chart.categoryField = "occurredTime";
            chart.gridAboveGraphs= "true";            
            chart.autoMarginOffset = 5;
            
            // 대상이 없을때 보여지는 챠트  양 사이드 border            
            chart.plotAreaBorderAlpha =  1;
            chart.plotAreaBorderColor =  '#e1e1e1'; 
            
            chart.autoMargins =  false;
            chart.columnsAboveLines = true;
            chart.marginTop = 5;
            chart.marginBottom = 19;
            chart.marginLeft = 50;
            chart.marginRight = 20;             
            
            chart.fontFamily = "Arial";
            chart.fontSize = 10;
                          
            chart.zoomOutButton = 
            {
            	backgroundColor: '#000000',
	            backgroundAlpha: 0.05,
	            fontSize : 9
            };
            chart.zoomOutButtonImageSize = 14;
            chart.usePrefixes = true;
            chart.prefixesOfBigNumbers = [{number:1e+3,prefix:"k"},
                                          {number:1e+6,prefix:"M"},
                                          {number:1e+9,prefix:"G"}];
             // 처음 로드 화면에 zoom 기능을 사용하고 싶다면 ("dataUpdated", zoomChart); 로 add 후 조절은 zoomChart method 에서한다.
            //chart.addListener("dataUpdated", zoomChart);

            // Axes
            // X축 Category Axis (on the down)               
            var categoryAxis = chart.categoryAxis;
            categoryAxis.parseDates = true; // as our data is date-based, we set parseDates to true
            categoryAxis.minPeriod = "ss"; // our data is daily, so we set minPeriod to DD
            categoryAxis.dashLength = 2;
            categoryAxis.dateFormats = [{period:'ss',format:'JJ:NN:SS'},
                                        {period:'mm',format:'JJ:NN'},
                                        {period:'hh',format:'JJ:NN'},
                                        {period:'DD',format:'YYYY-MM-DD'},
                                        {period:'WW',format:'YYYY-MM-DD'},
                                        {period:'MM',format:'MM'},
                                        {period:'YYYY',format:'YYYY'}];
            categoryAxis.gridAlpha = 0.3;            

            // Y축 value axis (on the left)
            var valueAxis1 = new AmCharts.ValueAxis();
            valueAxis1.stackType = "none";
            valueAxis1.tickLength = 0;
         //   valueAxis1.axisColor = chartOption.linecolor3;
            valueAxis1.axisThickness = 1;
            valueAxis1.dashLength = 2;
            categoryAxis.gridAlpha = 0.3;
            valueAxis1.usePrefixes = true;
            valueAxis1.prefixesOfBigNumbers = [
                                               {number:1e+3,prefix:"k"},
                                               {number:1e+6,prefix:"M"},
                                               {number:1e+9,prefix:"G"}];
            valueAxis1.minimum = chartOption.min;            
            //valueAxis1.titleColor = linecolor;
            valueAxis1.titleFontSize = 11;
            //valueAxis1.unit = chartunit1;
            
            if (chartOption.axistitle2 != null )
            {
            	// Y축 value axis2 (on the right)
                var valueAxis2 = new AmCharts.ValueAxis();
                valueAxis2.stackType = "none";
                valueAxis2.tickLength = 0;
            //    valueAxis2.axisColor = chartOption.linecolor4;
                valueAxis2.axisThickness = 1;
                valueAxis2.usePrefixes = true;
                valueAxis2.prefixesOfBigNumbers = [
                                                   {number:1e+3,prefix:"k"},
                                                   {number:1e+6,prefix:"M"},
                                                   {number:1e+9,prefix:"G"}];
                valueAxis2.minimum = chartOption.min;
                valueAxis2.position = "left";              
                valueAxis2.titleFontSize = 11;
                valueAxis2.unit = chartOption.axistitle2;
            }
            
            if (chartOption.max != null)
            {
            	valueAxis1.maximum = chartOption.max;
            }
            chart.addValueAxis(valueAxis1);             

                        
            //  graph 2
            var graph1 = new AmCharts.AmGraph();
            graph1.valueAxis = valueAxis1; // we have to indicate which value axis should be used
//            graph1.title = "bps In";
            
            if(chartOption.bpsValue == 0)
            {
            	graph1.title = "bps In";
           	 	graph1.lineColor = chartOption.linecolor1;
           	 	graph1.balloonText="bps In: [[value]]" + chartOption.axistitle1;            	 
            }
            else if(chartOption.bpsValue == 1)
            {
           	 	graph1.title = "bps Out";
           	 	graph1.lineColor = chartOption.linecolor2;
           	 	graph1.balloonText="bps Out: [[value]]" + chartOption.axistitle1;                 
            }
            else if(chartOption.bpsValue == 2)
            {
           	 	graph1.title = "bps Total"; 
           	 	graph1.lineColor = chartOption.linecolor3;
           	 	graph1.balloonText="bps Total: [[value]]" + chartOption.axistitle1;             	 
            }
            else
            {
            	graph1.title = "Connections"; 
           	 	graph1.lineColor = chartOption.linecolor4;
           	 	graph1.balloonText="Connections: [[value]]" + VAR_COMMON_AFEW;             	 
            }
            
            graph1.valueField = "Value1";            
            graph1.type = "line";           
//            graph1.lineColor = chartOption.linecolor1;
            graph1.lineThickness = "1";
//           	graph1.balloonText="bps In: [[value]] bps"; 
//           	graph1.hidden = true;
            chart.addGraph(graph1);
            /*
            //  graph 2
            var graph2 = new AmCharts.AmGraph();
            graph2.valueAxis = valueAxis1; // we have to indicate which value axis should be used
            graph2.title = "bps Out";
            graph2.valueField = "Value2";            
            graph2.type = "line";           
            graph2.lineColor = chartOption.linecolor2;
            graph2.lineThickness = "1";
           	graph2.balloonText="bps Out: [[value]] bps";
           	graph2.hidden = true;
            chart.addGraph(graph2);
            
            // GRAPHS
            //  graph 3
            var graph3 = new AmCharts.AmGraph();
            graph3.valueAxis = valueAxis1; // we have to indicate which value axis should be used
            graph3.title = "bps Total";
            graph3.valueField = "Value3";            
            graph3.type = "line";           
            graph3.lineColor = chartOption.linecolor3;
            graph3.lineThickness = "1";
           	graph3.balloonText="bps Total: [[value]] bps";           
            chart.addGraph(graph3);
            
            // graph 4              
            var graph4 = new AmCharts.AmGraph();
            graph4.valueAxis = valueAxis1; // we have to indicate which value axis should be used
            graph4.title = "Connections";
            graph4.valueField = "Value4";            
            graph4.type = "line";         
            graph4.lineColor = chartOption.linecolor4;
            graph4.lineThickness = "1";
            graph4.balloonText="Connections: [[value]]" + VAR_COMMON_AFEW;            
            chart.addGraph(graph4);
            */
            
            // CURSOR
            var chartCursor = new AmCharts.ChartCursor();
            chartCursor.cursorPosition = "middle";
            chartCursor.categoryBalloonDateFormat = "JJ:NN:SS";
            chartCursor.zoomable = true;
            chartCursor.cursorColor= chartOption.cursorColor;
            chart.addChartCursor(chartCursor);

            // Balloon             
            var balloon = chart.balloon;
            balloon.adjustBorderColor = true;
            balloon.pointerWidth = 3;
            balloon.shadowAlpha = 0; 
            balloon.borderThickness = 1;
            balloon.fillAlpha = 0.95;
            balloon.horizontalPadding = 6;
            balloon.verticalPadding = -2; 
            balloon.fontSize = 8;  
            
            // LEGEND
            var legend = new AmCharts.AmLegend();
            legend.align = "center";            
            legend.markerLabelGap = 5;           
            legend.position = "bottom";
            legend.valueWidth = 20;
            legend.verticalGap = 1;
            legend.valueText = "[[value]]";
            legend.markerSize = 11;
            chart.addLegend(legend);

             // WRITE
             chart.write(chartOption.chartname);          

         // this method is called when chart is first inited as we listen for "dataUpdated" event             
            function zoomChart()
	        {
	        	// different zoom methods can be used - zoomToIndexes, zoomToDates, zoomToCategoryValues
	        	chart.zoomToIndexes(10, 20);
	        }
	        
            try
			{
            	return chart;
			}
            finally
            {
            	chart = null;
            }
		}
	},	
	OBCreateConnRealTimeChart : function(chartOptionObj)
	{
		with (this) 
		{      
			function initChartData() 
			{
				try
				{
					var chartData = [];
				    return chartData;
				}
				finally
				{
					chartData = null;
				}
			}
			var chartOption = chartOptionObj;
			var chartData = initChartData();			
			
            // SERIAL CHART 옵션
            var chart = new AmCharts.AmSerialChart();
            chart.pathToImages = "/js/extern/amcharts_images/";
            
            chart.dataProvider = chartData;
            chart.categoryField = "occurredTime";
            chart.gridAboveGraphs= "true";            
            chart.autoMarginOffset = 5;
            
            // 대상이 없을때 보여지는 챠트  양 사이드 border            
            chart.plotAreaBorderAlpha =  1;
            chart.plotAreaBorderColor =  '#e1e1e1'; 
            
            chart.autoMargins =  false;
            chart.columnsAboveLines = true;
            chart.marginTop = 5;
            chart.marginBottom = 19;
            chart.marginLeft = 50;
            chart.marginRight = 20;             
            
            chart.fontFamily = "Arial";
            chart.fontSize = 10;
                          
            chart.zoomOutButton = 
            {
            	backgroundColor: '#000000',
	            backgroundAlpha: 0.05,
	            fontSize : 9
            };
            chart.zoomOutButtonImageSize = 14;
            chart.usePrefixes = true;
            chart.prefixesOfBigNumbers = [{number:1e+3,prefix:"k"},
                                          {number:1e+6,prefix:"M"},
                                          {number:1e+9,prefix:"G"}];
             // 처음 로드 화면에 zoom 기능을 사용하고 싶다면 ("dataUpdated", zoomChart); 로 add 후 조절은 zoomChart method 에서한다.
            //chart.addListener("dataUpdated", zoomChart);

            // Axes
            // X축 Category Axis (on the down)               
            var categoryAxis = chart.categoryAxis;
            categoryAxis.parseDates = true; // as our data is date-based, we set parseDates to true
            categoryAxis.minPeriod = "ss"; // our data is daily, so we set minPeriod to DD
            categoryAxis.dashLength = 2;
            categoryAxis.dateFormats = [{period:'ss',format:'JJ:NN:SS'},
                                        {period:'mm',format:'JJ:NN'},
                                        {period:'hh',format:'JJ:NN'},
                                        {period:'DD',format:'YYYY-MM-DD'},
                                        {period:'WW',format:'YYYY-MM-DD'},
                                        {period:'MM',format:'MM'},
                                        {period:'YYYY',format:'YYYY'}];
            categoryAxis.gridAlpha = 0.3;            

            // Y축 value axis (on the left)
            var valueAxis1 = new AmCharts.ValueAxis();
            valueAxis1.stackType = "none";
            valueAxis1.tickLength = 0;
         //   valueAxis1.axisColor = chartOption.linecolor3;
            valueAxis1.axisThickness = 1;
            valueAxis1.dashLength = 2;
            categoryAxis.gridAlpha = 0.3;
            valueAxis1.usePrefixes = true;
            valueAxis1.prefixesOfBigNumbers = [
                                               {number:1e+3,prefix:"k"},
                                               {number:1e+6,prefix:"M"},
                                               {number:1e+9,prefix:"G"}];
            valueAxis1.minimum = chartOption.min;            
            //valueAxis1.titleColor = linecolor;
            valueAxis1.titleFontSize = 11;
            //valueAxis1.unit = chartunit1;
            
            if (chartOption.axistitle2 != null )
            {
            	// Y축 value axis2 (on the right)
                var valueAxis2 = new AmCharts.ValueAxis();
                valueAxis2.stackType = "none";
                valueAxis2.tickLength = 0;
               // valueAxis2.axisColor = chartOption.linecolor4;
                valueAxis2.axisThickness = 1;
                valueAxis2.usePrefixes = true;
                valueAxis2.prefixesOfBigNumbers = [
                                                   {number:1e+3,prefix:"k"},
                                                   {number:1e+6,prefix:"M"},
                                                   {number:1e+9,prefix:"G"}];
                valueAxis2.minimum = chartOption.min;
                valueAxis2.position = "left";              
                valueAxis2.titleFontSize = 11;
                valueAxis2.unit = chartOption.axistitle2;
            }
            
            if (chartOption.max != null)
            {
            	valueAxis1.maximum = chartOption.max;
            }
            chart.addValueAxis(valueAxis1);             

                        
            //  graph 2
            var graph1 = new AmCharts.AmGraph();
            graph1.valueAxis = valueAxis1; // we have to indicate which value axis should be used
            graph1.title = "Connections";
            graph1.valueField = "Value1";            
            graph1.type = "line";           
            graph1.lineColor = chartOption.linecolor4;
            graph1.lineThickness = "1";
            graph1.balloonText="Connections: [[value]]" + VAR_COMMON_AFEW;  
            chart.addGraph(graph1);
            
            // CURSOR
            var chartCursor = new AmCharts.ChartCursor();
            chartCursor.cursorPosition = "middle";
            chartCursor.categoryBalloonDateFormat = "JJ:NN:SS";
            chartCursor.zoomable = true;
            chartCursor.cursorColor= chartOption.cursorColor;
            chart.addChartCursor(chartCursor);
            
            // LEGEND
            var legend = new AmCharts.AmLegend();
            legend.align = "center";            
            legend.markerLabelGap = 5;           
            legend.position = "bottom";
            legend.valueWidth = 20;
            legend.verticalGap = 1;
            legend.valueText = "[[value]]";
            legend.markerSize = 11;
            chart.addLegend(legend);

            // WRITE
            chart.write(chartOption.chartname);          

            // this method is called when chart is first inited as we listen for "dataUpdated" event             
            function zoomChart()
	        {
	        	// different zoom methods can be used - zoomToIndexes, zoomToDates, zoomToCategoryValues
	        	chart.zoomToIndexes(10, 20);
	        }
	        
            try
			{
            	return chart;
			}
            finally
            {
            	chart = null;
            }
		}
	},
	// 설정변경현황 전용 Chart View (GS 적용 모듈)
	OBSettingHistoryChartViewer: function(chartDatalist, chartOptionList)
	{
		with (this) 
		{   
			 var chartData = chartDatalist;
			 var chartOption = chartOptionList;
			 
             // SERIAL CHART 옵션
             var chart = new AmCharts.AmSerialChart();
             chart.pathToImages = "/js/extern/amcharts_images/";             
             chart.dataProvider = chartData;
             chart.categoryField = "occurredTime";
             chart.gridAboveGraphs= "true"; 
             
             // 대상이 없을때 보여지는 챠트  양 사이드 border            
             chart.plotAreaBorderAlpha =  1;
             chart.plotAreaBorderColor =  '#e1e1e1'; 
             
             chart.autoMargins =  false;
             chart.columnsAboveLines = true;
             chart.marginTop = 5;
             chart.marginBottom = 25;
             chart.marginLeft = 50;           
             
             chart.fontFamily = "Arial";
             chart.fontSize = 10;
             chart.zoomOutButton =
             {
	             backgroundColor: '#000000',
	             backgroundAlpha: 0.05,
	             fontSize : 9
             };
             chart.zoomOutButtonImageSize = 14;
             chart.usePrefixes = true;
             chart.prefixesOfBigNumbers = [{number:1e+3,prefix:"k"},
                                           {number:1e+6,prefix:"M"},
                                           {number:1e+9,prefix:"G"}];
             // 처음 로드 화면에 zoom 기능을 사용하고 싶다면 ("dataUpdated", zoomChart); 로 add 후 조절은 zoomChart method 에서한다.
             //chart.addListener("dataUpdated", zoomChart);

             // Axes
             // X축 Category Axis (on the down)               
             var categoryAxis = chart.categoryAxis;
             categoryAxis.parseDates = true; // as our data is date-based, we set parseDates to true
             categoryAxis.minPeriod = "ss"; // our data is daily, so we set minPeriod to DD
             categoryAxis.dashLength = 2;
             categoryAxis.dateFormats = [{period:'ss',format:'JJ:NN:SS'},
                                         {period:'mm',format:'JJ:NN'},
                                         {period:'hh',format:'JJ:NN'},
                                         {period:'DD',format:'MM-DD'},
                                         {period:'WW',format:'MM-DD'},
                                         {period:'MM',format:'MM-DD'},
                                         {period:'YYYY',format:'YYYY'}];
             categoryAxis.gridAlpha = 0.3;

             

             // Y축 value axis (on the left)
             var valueAxis = new AmCharts.ValueAxis();
             valueAxis.stackType = "none";
             valueAxis.tickLength = 0;
             valueAxis.dashLength = 2;
             valueAxis.gridAlpha = 0.3; 
             // valueAxis.axisColor = chartOption.linecolor;
             valueAxis.axisThickness = 1;
             valueAxis.usePrefixes = true;
             valueAxis.prefixesOfBigNumbers = [
                                                {number:1e+3,prefix:"k"},
                                                {number:1e+6,prefix:"M"},
                                                {number:1e+9,prefix:"G"}];
             valueAxis.minimum = chartOption.min;
             if (chartOption.max != null)
             {
            	 valueAxis.maximum = chartOption.max;             
             }
             valueAxis.title = chartOption.axistitle;
             valueAxis.titleBold = false;
             valueAxis.titleColor = "#666666";
             chart.addValueAxis(valueAxis);             

             // GRAPHS
             //  graph One Value
             var graph = new AmCharts.AmGraph();
             graph.valueAxis = valueAxis; // we have to indicate which value axis should be used
             graph.title = "firstValue";
             graph.valueField = "firstValue";
             graph.bullet = "round";
             graph.hideBulletsCount = 5; 
             graph.type = "line";
             graph.fillAlphas = 0.3;
             graph.lineColor = chartOption.linecolor;
             graph.lineThickness = "1";
             graph.balloonText=VAR_DASH_ALLGROUP + ": [[value]]" + chartOption.axistitle;   
//             graph.connect = false;
//             graph.gapPeriod = chartOption.interval;             
             chart.addGraph(graph);                         

             // CURSOR
             var chartCursor = new AmCharts.ChartCursor();
             chartCursor.cursorPosition = "middle";
             chartCursor.categoryBalloonDateFormat = "YYYY-MM-DD JJ:NN:SS";
             chartCursor.zoomable = true;
             chartCursor.cursorColor= chartOption.cursorColor;
             chart.addChartCursor(chartCursor);
             
             if (chartOption.maxPos != null && chartOption.maxPos > 0)
             {
            	 var guide = new AmCharts.Guide();
                 guide.value = chartOption.maxPos; 
                 guide.lineColor = "#CC0000";
                 //guide.dashLength = 5;
                 guide.label = "max : " + addCommaforNumber(chartOption.maxPos) + chartOption.axistitle;               
                 guide.inside = true;
                 guide.lineAlpha = 1;                 
                 valueAxis.addGuide(guide);
                 guide = null;
             }
             // SCROLLBAR (상단의 확대 축소 바)
             var chartScrollbar = new AmCharts.ChartScrollbar();
             chart.addChartScrollbar(chartScrollbar);
             chartScrollbar.dragIconHeight =  20;
             chartScrollbar.dragIconWidth = 20;
             chartScrollbar.scrollbarHeight = 10;          
             
             // Balloon             
             var balloon = chart.balloon;
             balloon.adjustBorderColor = true;
             balloon.pointerWidth = 3;
             balloon.shadowAlpha = 0; 
             balloon.borderThickness = 1;
             balloon.fillAlpha = 0.95;
             balloon.horizontalPadding = 6;
             balloon.verticalPadding = -2; 
             balloon.fontSize = 8;   
             
             // LEGEND
             var legend = new AmCharts.AmLegend();
             legend.autoMargins = false;
             legend.marginBottom =  -5;
             legend.marginTop =  -1; 
//             legend.backgroundColor ="#ff0000";
//             legend.backgroundAlpha ="0.9";
             legend.align = "center";            
             legend.markerLabelGap = 4;           
             legend.position = "bottom";
             legend.valueWidth = 50;
             legend.verticalGap = 1;
//             legend.valueText = "[[value]]";
//             legend.labelText = "[[title]]";
             legend.labelText = VAR_DASH_ALLGROUP;
             legend.markerSize = 10;
             legend.fontSize = 9;
//             if(chartOption.legend)
//            	 chart.addLegend(legend); 
//             chart.addLegend(legend);                

             // WRITE
             chart.write(chartOption.chartname);          

         // this method is called when chart is first inited as we listen for "dataUpdated" event
	         function zoomChart()
	         {
	             // different zoom methods can be used - zoomToIndexes, zoomToDates, zoomToCategoryValues
	             chart.zoomToIndexes(10, 20);
	         }
	         return chart;
		}
	},
	
	/*
	 * 모듈명: OBAreaChartSPViewer
	 * 매개변수 : chartDatalist, chartOptionList ( min, max, linecolor, chartname, axistitle, maxPos )
	 * 사용처 : ADC 장비 모니터링 시스템자원 상태 CPU -> SP 별 CPU, Session 량  : Alteon
	 * 
	 * */
	OBAreaChartSPViewer : function(chartDatalist, chartOptionList)	
	{
		with (this)
		{
			var chartData = chartDatalist;
			var chartOption = chartOptionList;
			
			// Serial Chart Option
			var chart = new AmCharts.AmSerialChart();
			chart.pathToImages = "/js/extern/amcharts_images/";
			chart.dataProvider = chartData;
			chart.categoryField = "category";
            chart.gridAboveGraphs= "true";			
			chart.columnSpacing = 13;
			chart.columnWidth = 0.78;
			chart.minSelectedTime = 6;
			chart.autoMarginOffset = 14;
			
            // 대상이 없을때 보여지는 챠트  양 사이드 border            
            chart.plotAreaBorderAlpha =  1;
            chart.plotAreaBorderColor =  '#e1e1e1'; 
            
            chart.autoMargins =  false;
            chart.columnsAboveLines = true;
            chart.marginTop = 5;
            chart.marginBottom = 19;
            chart.marginLeft = 50;
            chart.marginRight = 40;             
            
			chart.fontFamily = "Arial";
            chart.fontSize = 10;
            
            var balloon = chart.balloon;
            balloon.verticalPadding = 1;
            
            chart.usePrefixes = true;
			chart.prefixesOfBigNumbers = [{number:1e+3,prefix:"k"},
                                          {number:1e+6,prefix:"M"},
                                          {number:1e+9,prefix:"G"}];
			chart.addListener("clickGraphItem", handleClick);
			
			chart.allLabels = [{"align": "left", 
				                "bold": false, 
								"id": "Label-1", 
								"text": "[Session] critical value: " + chartOption.spSessionMaxUnit + chartOption.axistitle2,
								"x": "60",
								"y": "10"
					}];
	        	            
			// Axes
            // X축 Category Axis (on the down)  
			var categoryAxis = chart.categoryAxis;
//			categoryAxis.tickPosition = start;
			categoryAxis.dashLength = 2;
//			categoryAxis.fontSize = 10;
			categoryAxis.gridAlpha = 0.2;
			categoryAxis.gridCount = 2;
			categoryAxis.minorGridEnabled = true;
			categoryAxis.tickLength = 2;
			categoryAxis.titleBold = false;
			categoryAxis.titleFontSize = 0;
			
			// Y축 value axis1 (on the left) 
            var valueAxis1 = new AmCharts.ValueAxis();
            valueAxis1.stackType = "none";
            valueAxis1.tickLength = 0;
            valueAxis1.dashLength = 2;
            valueAxis1.gridAlpha = 0.3; 
           // valueAxis1.axisColor = chartOption.linecolor;
            valueAxis1.axisThickness = 1;
            valueAxis1.usePrefixes = true;
//            valueAxis1.prefixesOfBigNumbers = [
//                                               {number:1e+3,prefix:"k"},
//                                               {number:1e+6,prefix:"M"},
//                                               {number:1e+9,prefix:"G"}];
            valueAxis1.minimum = chartOption.min;									
            if (chartOption.max != null)
            {
            	valueAxis1.maximum = chartOption.max;             
            }
            valueAxis1.title = chartOption.axistitle1;
            valueAxis1.titleBold = false;
            valueAxis1.titleColor = "#666666";
            
			chart.addValueAxis(valueAxis1);
			
			// Y축 value axis2 (on the left) 
            var valueAxis2 = new AmCharts.ValueAxis();
            //            valueAxis2.stackType = "regular";
            valueAxis2.stackType = "none";
            valueAxis2.tickLength = 0;
            valueAxis2.gridAlpha = 0; 
         //   valueAxis2.axisColor = chartOption.linecolor;
            valueAxis2.axisThickness = 1;
            valueAxis2.usePrefixes = true;
            valueAxis2.position = "right";
            valueAxis2.prefixesOfBigNumbers = [
                                               {number:1e+3,prefix:"k"},
                                               {number:1e+6,prefix:"M"},
                                               {number:1e+9,prefix:"G"}];
            valueAxis2.minimum = chartOption.minSession;								
           
            valueAxis2.title = chartOption.axistitle2;
            valueAxis2.titleBold = false;
            valueAxis2.titleColor = "#666666";
            
			chart.addValueAxis(valueAxis2);			
						
			// GRAPHS
            // graph1
            var graph1 = new AmCharts.AmGraph();
            graph1.valueAxis = valueAxis1; // we have to indicate which value axis should be used
//            graph1.balloonText = "사용량: [[value]]" + chartOption.axistitle1;
            
            graph1.balloonText = "사용량: [[value]] %";
            graph1.color = "#FFFFFF";
            graph1.columnWidth = 0.55;
            graph1.fillAlphas = 0.3;
            graph1.id = "AmGraph-1";
            graph1.labelPosition = "middle";
            graph1.labelText = "";
            graph1.minDistance = 0;
            graph1.title = "사용량";
            graph1.type = "column";
            graph1.valueField = "column-1";           
            graph1.fillColors = "#6cb8c8";
            graph1.lineColor = "#6cb8c8";
            graph1.connect = false;
            graph1.gapPeriod = chartOption.interval;
            chart.addGraph(graph1);
            
            var graph2 = new AmCharts.AmGraph();
            graph2.valueAxis = valueAxis2; // we have to indicate which value axis should be used
//            graph1.balloonText = "사용량: [[value]]" + chartOption.axistitle1;
            
            graph2.balloonText = "Session: [[value]]" + chartOption.axistitle2;
            graph2.color = "#FFFFFF";
            graph2.columnWidth = 0.55;
            graph2.fillAlphas = 0.3;
            graph2.id = "AmGraph-2";
            graph2.labelPosition = "middle";
            graph2.labelText = "";
            graph2.minDistance = 0;
            graph2.title = "Session";
            graph2.type = "column";
            graph2.valueField = "column-2";
            
            graph2.fillColors = "#fbc51a";
            graph2.lineColor = "#fbc51a";
            graph2.connect = false;
            graph2.gapPeriod = chartOption.interval;
            chart.addGraph(graph2);
            
            // graph2
//            var graph2 = new AmCharts.AmGraph();
//            graph2.valueAxis = valueAxis2; // we have to indicate which value axis should be used
//            graph2.balloonText = "title: [[value]] : " + chartOption.axistitle2;
//            graph2.balloonText = "Session: [[value]]" + chartOption.axistitle2;
//            graph2.bullet = "round";
//            graph2.bulletAxis = "Not set";
//            graph2.bulletSize = 6;
//            graph2.columnWidth = 0;
//            graph2.id = "AmGraph-2";
//            graph2.minDistance = 0;
//            graph2.negativeBase = -3;
//            graph2.showAllValueLabels = true;
//            graph2.stackable = false;
//            graph2.switchable = false;
//            graph2.title = "Session";
//            graph2.valueAxis = "ValueAxis-2";
//            graph2.valueField = "column-2";
//            graph2.yAxis = "Not set";
//            graph2.type = "line";
//            graph2.type = "column";
//            graph2.lineColor = "#003592";
//            chart.addGraph(graph2);
                   
			// CURSOR
            var chartCursor = new AmCharts.ChartCursor();	
            chartCursor.bulletsEnabled = true;
            chartCursor.cursorColor = "#0F47C7";
            chartCursor.graphBulletAlpha = 0;
            chartCursor.cursorColor= chartOption.cursorColor;
            chart.addChartCursor(chartCursor);
				
//            alert(chartOption.spSessionMax);
//            alert(chartOption.maxPos);
            if (chartOption.spSessionMax != null && chartOption.spSessionMax > 0)
            {
            	if(chartOption.spSessionMax > chartOption.maxPos)
            	{
	            	var guide = new AmCharts.Guide();
	//                guide.value = chartOption.spSessionMax;
	                guide.value = chartOption.spSessionMax; 
	                guide.lineColor = "#CC0000";
	//                guide.dashLength = 5;
	//                guide.prefixesOfBigNumbers = [
	//                                                   {number:1e+3,prefix:"k"},
	//                                                   {number:1e+6,prefix:"M"},
	//                                                   {number:1e+9,prefix:"G"}];
	                
	//                guide.label = "max : " + addCommaforNumber(chartOption.spSessionMax) + chartOption.axistitle2;
	                guide.label = "max : " + chartOption.spSessionMaxUnit;   
	                guide.inside = true;
	                guide.lineAlpha = 1;                 
	                valueAxis2.addGuide(guide);
	                guide = null;
            	}
            }
            
            // Balloon             
            var balloon = chart.balloon;
            balloon.adjustBorderColor = true;
            balloon.pointerWidth = 3;
            balloon.shadowAlpha = 0; 
            balloon.borderThickness = 1;
            balloon.fillAlpha = 0.95;
            balloon.horizontalPadding = 6;
            balloon.verticalPadding = -2; 
            balloon.fontSize = 8;   
            
            // LEGEND
            var legend = new AmCharts.AmLegend();
            legend.align = "center";             
            legend.position = "bottom";            
            legend.verticalGap = 1;
            legend.autoMargins = false;
            legend.equalWidths = false;
            legend.marginBottom = -4;
            legend.marginLeft = 49;
            legend.markerLabelGap = 4;
            legend.markerSize = 8;
            legend.valueAlign = "left";
            legend.valueText = "";
            legend.valueWidth = 30;
            legend.verticalGap = 5;
            
            chart.addLegend(legend);
            
            // WRITE
            chart.write(chartOption.chartname);  
            /*            
			function handleClick(event)
	        {
//	            alert(event.item.category + ": " + event.item.values.value);	            
	            var spNum = event.item.category;
	            var spNumStr = spNum.replace("SP", "");
	            spNumStr++;
//	            monitorAppliance.loadDetailSPSessionInfo(false, spNumStr);
	            monitorAppliance.loadDetailSPSessionInfo(spNumStr);
	        }
            */		
	        return chart;
		}
	},

	handleClick : function(event)
    {          
        var spNum = event.item.category;
//        console.log(event.graph.connect);
//        console.log(event.graph.gapPeriod);
        var gapPeriod = event.graph.gapPeriod;
        if(spNum.substr(0,2) == "SP")
        {
        	var spNumStr = spNum.replace("SP", "");
            spNumStr++;
            monitorAppliance.loadDetailSPSessionInfo(spNumStr, gapPeriod);
        }
        else
        {
        	var spNumStr = spNum.replace("MP", "0");
            spNumStr++;
            monitorAppliance.loadDetailSPSessionInfo(spNumStr, gapPeriod);
//        	alert("MP는 추이가 제공되지 않습니다.");
//        	return false;
        }        
    },	
/*	
	handleClick : function(event)
    {
//        alert(event.item.category + ": " + event.item.values.value);	            
        var spNum = event.item.category;
        var spNumStr = spNum.replace("SP", "");
        spNumStr++;
//        monitorAppliance.loadDetailSPSessionInfo(false, spNumStr);
        monitorAppliance.loadDetailSPSessionInfo(spNumStr);
    },
*/
	/*
	 * 모듈명: OBAreaChartSPDashViewer
	 * 매개변수 : chartDatalist, chartOptionList ( min, max, linecolor, chartname, axistitle, maxPos )
	 * 사용처 : 대시보드  ADC CPU -> SP 별 CPU, Session 량 
	 * 
	 * */
	OBAreaChartSPDashViewer : function(chartDatalist, chartOptionList)
	{
		with (this)
		{
			var chartData = chartDatalist;
			var chartOption = chartOptionList;
			
			// Serial Chart Option
			var chart = new AmCharts.AmSerialChart();
			chart.pathToImages = "/js/extern/amcharts_images/";
			chart.dataProvider = chartData;
			chart.categoryField = "category";
            chart.gridAboveGraphs= "true";			
			chart.columnSpacing = 13;
			chart.columnWidth = 0.78;
			chart.minSelectedTime = 6;
			chart.autoMarginOffset = 14;
			
            // 대상이 없을때 보여지는 챠트  양 사이드 border            
            chart.plotAreaBorderAlpha =  1;
            chart.plotAreaBorderColor =  '#e1e1e1'; 
            
            chart.autoMargins =  false;
            chart.columnsAboveLines = true;
            chart.marginTop = 5;
            chart.marginBottom = 19;
            chart.marginLeft = 50;
            chart.marginRight = 40;             
            
			chart.fontFamily = "Arial";
            chart.fontSize = 10;
            
            var balloon = chart.balloon;
            balloon.verticalPadding = 1;
            
            chart.usePrefixes = true;
			chart.prefixesOfBigNumbers = [{number:1e+3,prefix:"k"},
                                          {number:1e+6,prefix:"M"},
                                          {number:1e+9,prefix:"G"}];
			
			if(chartOption.spSessionMax != undefined)
			{
				chart.allLabels = [{"align": "left", 
									"bold": false, 
									"id": "Label-1", 
									//"color": "#FF9900",
									"text": "[Session] critical value: " + chartOption.spSessionMaxUnit + chartOption.axistitle2,
//									"x": "200",
//									"y": "10"
									"x": "60",
									"y": "10"
						}];
			}           
			// Axes
            // X축 Category Axis (on the down)  
			var categoryAxis = chart.categoryAxis;
			categoryAxis.dashLength = 2;
//			categoryAxis.fontSize = 6;
			categoryAxis.gridAlpha = 0.2;
			categoryAxis.gridCount = 2;
			categoryAxis.minorGridEnabled = true;
			categoryAxis.tickLength = 2;
			categoryAxis.titleBold = false;
			categoryAxis.titleFontSize = 0;
			
			// Y축 value axis1 (on the left) 
            var valueAxis1 = new AmCharts.ValueAxis();      
            valueAxis1.stackType = "none";
            valueAxis1.tickLength = 0;
            valueAxis1.dashLength = 2;
            valueAxis1.gridAlpha = 0.3; 
         //   valueAxis1.axisColor = chartOption.linecolor;
            valueAxis1.axisThickness = 1;
            valueAxis1.usePrefixes = true;            
            valueAxis1.minimum = chartOption.min;									
            if (chartOption.max != null)
            {
            	valueAxis1.maximum = chartOption.max;             
            }
            valueAxis1.title = chartOption.axistitle1;
            valueAxis1.titleBold = false;
            valueAxis1.titleColor = "#666666";
            
			chart.addValueAxis(valueAxis1);
			
			// Y축 value axis2 (on the right) 
            var valueAxis2 = new AmCharts.ValueAxis();
            valueAxis2.stackType = "none";
            valueAxis2.tickLength = 0;
            valueAxis2.gridAlpha = 0; 
          //  valueAxis2.axisColor = chartOption.linecolor;
            valueAxis2.axisThickness = 1;
            valueAxis2.usePrefixes = true;
            valueAxis2.position = "right";
            valueAxis2.prefixesOfBigNumbers = [
                                               {number:1e+3,prefix:"k"},
                                               {number:1e+6,prefix:"M"},
                                               {number:1e+9,prefix:"G"}];
            valueAxis2.minimum = chartOption.minSession;									
           
            valueAxis2.title = chartOption.axistitle2;
            valueAxis2.titleBold = false;
            valueAxis2.titleColor = "#666666";
            
			chart.addValueAxis(valueAxis2);			
									
			// GRAPHS
            // graph1
            var graph1 = new AmCharts.AmGraph();
            graph1.valueAxis = valueAxis1; // we have to indicate which value axis should be used
//            graph1.balloonText = "사용량: [[value]]" + chartOption.axistitle1;
            
            graph1.balloonText = VAR_DASH_USAGE + ": [[value]] %";
            graph1.color = "#FFFFFF";
            graph1.columnWidth = 0.55;
            graph1.fillAlphas = 0.3;
            graph1.id = "AmGraph-1";
            graph1.labelPosition = "middle";
            graph1.labelText = "";
            graph1.minDistance = 0;
            graph1.title = VAR_DASH_USAGE;
            graph1.type = "column";
            graph1.valueField = "column-1";           
            graph1.fillColors = "#6cb8c8";
            graph1.lineColor = "#6cb8c8";
            chart.addGraph(graph1);
            
            // graph2
            var graph2 = new AmCharts.AmGraph();
            graph2.valueAxis = valueAxis2; // we have to indicate which value axis should be used
//            graph1.balloonText = "사용량: [[value]]" + chartOption.axistitle1;
            
            graph2.balloonText = "Session: [[value]]" + chartOption.axistitle2;
            graph2.color = "#FFFFFF";
            graph2.columnWidth = 0.55;
            graph2.fillAlphas = 0.3;
            graph2.id = "AmGraph-2";
            graph2.labelPosition = "middle";
            graph2.labelText = "";
            graph2.minDistance = 0;
            graph2.title = "Session";
            graph2.type = "column";
            graph2.valueField = "column-2";
            
            graph2.fillColors = "#fbc51a";
            graph2.lineColor = "#fbc51a";
            chart.addGraph(graph2);
//            var graph2 = new AmCharts.AmGraph();
//            graph2.valueAxis = valueAxis2; 
//            graph2.balloonText = "Session: [[value]]" + chartOption.axistitle2;
//            graph2.bullet = "round";
//            graph2.bulletSize = 6;
//            graph2.id = "AmGraph-2";
//            graph2.title = "Session";
//            graph2.valueField = "column-2";
//            graph2.type = "line";
//            graph2.lineColor = "#003592";
//            chart.addGraph(graph2);
                   
			// CURSOR
            var chartCursor = new AmCharts.ChartCursor();	
            chartCursor.bulletsEnabled = false;
            chartCursor.valueLineBalloonEnabled = true;         
            chartCursor.cursorColor = "#0F47C7";
            chartCursor.cursorColor= chartOption.cursorColor;
            chart.addChartCursor(chartCursor);				
//            alert(chartOption.spSessionMax);
//            alert(chartOption.maxPos);
            if (chartOption.spSessionMax != null && chartOption.spSessionMax > 0)
            {            	
            	if(chartOption.spSessionMax > chartOption.maxPos)
            	{
	            	var guide = new AmCharts.Guide();
	                guide.value = chartOption.spSessionMax; 
	                guide.lineColor = "#CC0000";
	                guide.label = "max : " + chartOption.spSessionMaxUnit;   
	                guide.inside = true;
	                guide.lineAlpha = 1;                 
	                valueAxis2.addGuide(guide);
	                guide = null;
            	}
            }

            // SCROLLBAR (상단의 확대 축소 바)
//           var chartScrollbar = new AmCharts.ChartScrollbar();
//           chart.addChartScrollbar(chartScrollbar);
//            chartScrollbar.dragIconWidth =  25;
//            chartScrollbar.graphType =  "line";
//            chartScrollbar.scrollbarHeight = 9; 
//            chartScrollbar. offset = -1;             
            
            // Balloon             
            var balloon = chart.balloon;
            balloon.adjustBorderColor = true;
            balloon.pointerWidth = 3;
            balloon.shadowAlpha = 0; 
            balloon.borderThickness = 1;
            balloon.fillAlpha = 0.95;
            balloon.horizontalPadding = 6;
            balloon.verticalPadding = -2; 
            balloon.fontSize = 8;   
            
            // LEGEND
            var legend = new AmCharts.AmLegend();
            legend.autoMargins = false;
            legend.marginBottom =  -5;
            legend.marginTop =  -1; 
//            legend.backgroundColor ="#ff0000";
//            legend.backgroundAlpha ="0.9";
            legend.align = "center";            
            legend.markerLabelGap = 4;           
            legend.position = "bottom";
            legend.valueWidth = 50;
            legend.verticalGap = 1;
//            legend.valueText = "[[value]]";
            legend.labelText = "[[title]]";
            legend.markerSize = 10;
            legend.fontSize = 9;
//            if(chartOption.legend)
           	 chart.addLegend(legend); 
            chart.addLegend(legend);                
            
            // WRITE
            chart.write(chartOption.chartname);
            
	        return chart;
		}
	},
	
	/*
	 * 모듈명: OBAreaChartSPDetailViewer
	 * 매개변수 : chartDatalist, chartOptionList ( min, max, linecolor, chartname, axistitle, maxPos )
	 * 사용처 : ADC 장비 모니터링 시스템자원 상태 CPU 
	 * 
	 * */
	
	OBAreaChartSPDetailViewer : function(chartDatalist, chartOptionList)
	{
		with (this)
		{
			var chartData = chartDatalist;
			var chartOption = chartOptionList;
			
			// Serial Chart Option
			var chart = new AmCharts.AmSerialChart();
			chart.pathToImages = "/js/extern/amcharts_images/";
			chart.dataProvider = chartData;
			chart.categoryField = "occurredTime";
            chart.gridAboveGraphs= "true";			
			chart.columnSpacing = 13;
			chart.columnWidth = 0.78;
			chart.minSelectedTime = 6;
			chart.autoMarginOffset = 14;

            // 대상이 없을때 보여지는 챠트  양 사이드 border            
            chart.plotAreaBorderAlpha =  1;
            chart.plotAreaBorderColor =  '#e1e1e1'; 
            
            chart.autoMargins =  false;
//            chart.columnsAboveLines = true;
            chart.marginTop = 5;
            chart.marginBottom = 19;
            chart.marginLeft = 50;
            chart.marginRight = 20;             
            
			chart.fontFamily = "Arial";
            chart.fontSize = 10;
            
            var balloon = chart.balloon;
            balloon.verticalPadding = 1;
            
            chart.usePrefixes = true;
			chart.prefixesOfBigNumbers = [{number:1e+3,prefix:"k"},
                                          {number:1e+6,prefix:"M"},
                                          {number:1e+9,prefix:"G"}];
						
//			chart.allLabels = [{"bold": true, 
//								"id": "Label-1", 
//								"text": "[Usage] Max/Avg: " + chartOption.maxPos + chartOption.axistitle1 + "/" + chartOption.avgPos + chartOption.axistitle1,
//								"x": 63,
//								"y": 30},
//								{"align": "right", 
//								 "bold": true, 
//								 "id": "Label-3", 
//								 "text": "[Session] Max/Avg: " + chartOption.maxSPSession + chartOption.axistitle2 + "/" + chartOption.avgSPSession + chartOption.axistitle2,
//								 "x": "90%",
//								 "y": "3"}								
//			];
			
			// Ballon Option (Alteon CPU Chart 만 유효)
//            var balloon = chart.balloon;             
//            balloon.adjustBorderColor = true;            
//            //balloon.cornerRadius = 5;            
//            balloon.verticalPadding = 0;
//            balloon.fontSize = 9;
//            
//            balloon.fixedPosition = true;
//            balloon.horizontalPadding = 5;
//            balloon.offsetY = 4;
//            balloon.verticalPadding = 0;
            
			// Axes
            // X축 Category Axis (on the down)  
			var categoryAxis = chart.categoryAxis;
//			categoryAxis.tickPosition = start;
			categoryAxis.parseDates = true; // as our data is date-based, we set parseDates to true
            categoryAxis.minPeriod = "ss"; // our data is daily, so we set minPeriod to DD            
            categoryAxis.dateFormats = [{period:'ss',format:'JJ:NN:SS'},
                                        {period:'mm',format:'JJ:NN'},
                                        {period:'hh',format:'JJ:NN'},
                                        {period:'DD',format:'YYYY-MM-DD'},
                                        {period:'WW',format:'YYYY-MM-DD'},
                                        {period:'MM',format:'MM'},
                                        {period:'YYYY',format:'YYYY'}];
            
			categoryAxis.dashLength = 2;
//			categoryAxis.fontSize = 6;
			categoryAxis.gridAlpha = 0.2;
			categoryAxis.gridCount = 2;
			categoryAxis.minorGridEnabled = true;
			categoryAxis.tickLength = 2;
			categoryAxis.titleBold = false;
			categoryAxis.titleFontSize = 0;
			
			// Y축 value axis1 (on the left) 
            var valueAxis1 = new AmCharts.ValueAxis();
            valueAxis1.stackType = "none";
            valueAxis1.tickLength = 0;
            valueAxis1.dashLength = 2;
            valueAxis1.gridAlpha = 0.3; 
         //   valueAxis1.axisColor = chartOption.linecolor;
            valueAxis1.axisThickness = 1;
            valueAxis1.usePrefixes = true;
//            valueAxis1.prefixesOfBigNumbers = [
//                                               {number:1e+3,prefix:"k"},
//                                               {number:1e+6,prefix:"M"},
//                                               {number:1e+9,prefix:"G"}];
            valueAxis1.minimum = chartOption.min;
            if (chartOption.max != null)
            {
            	valueAxis1.maximum = chartOption.max;             
            }
            valueAxis1.title = chartOption.axistitle1;
            valueAxis1.titleBold = false;
            valueAxis1.titleColor = "#666666";
            
			chart.addValueAxis(valueAxis1);
			
			// Y축 value axis2 (on the left) 
            var valueAxis2 = new AmCharts.ValueAxis();
            valueAxis2.stackType = "regular";
//            valueAxis2.stackType = "none";
            valueAxis2.tickLength = 0;
            valueAxis2.gridAlpha = 0; 
          //  valueAxis2.axisColor = chartOption.linecolor;
            valueAxis2.axisThickness = 1;
            valueAxis2.usePrefixes = true;
            valueAxis2.position = "right";
            valueAxis2.prefixesOfBigNumbers = [
                                               {number:1e+3,prefix:"k"},
                                               {number:1e+6,prefix:"M"},
                                               {number:1e+9,prefix:"G"}];
            valueAxis2.minimum = chartOption.min;
           
            valueAxis2.title = chartOption.axistitle2;
            valueAxis2.titleBold = false;
            valueAxis2.titleColor = "#666666";
            
			chart.addValueAxis(valueAxis2);			
						
			// GRAPHS
            // graph1
            var graph1 = new AmCharts.AmGraph();
            graph1.valueAxis = valueAxis1; // we have to indicate which value axis should be used
//            graph1.balloonText = "사용량: [[value]]" + chartOption.axistitle1;
            
            graph1.balloonText = VAR_DASH_USAGE + ": [[value]] %";
            graph1.color = "#FFFFFF";
            graph1.columnWidth = 0.55;
            graph1.fillAlphas = 0.3;
            graph1.id = "AmGraph-1";
            graph1.labelPosition = "middle";
            graph1.labelText = "";
            graph1.minDistance = 0;
            graph1.title = VAR_DASH_USAGE;
            graph1.type = "line";
//            graph1.type = "column";
            graph1.valueField = "column-1";
            graph1.fillColors = "#3366CC";
            graph1.lineColor = "#3366CC";
//            graph1.lineThickness = "1";
            graph1.connect = false;
            graph1.gapPeriod = chartOption.interval;
            chart.addGraph(graph1);
            
            // graph2
            
            var graph2 = new AmCharts.AmGraph();
            graph2.valueAxis = valueAxis2; // we have to indicate which value axis should be used
//            graph2.balloonText = "title: [[value]] : " + chartOption.axistitle2;
            graph2.balloonText = "Session: [[value]]";
//            graph2.bullet = "round";
            graph2.bulletAxis = "Not set";
            graph2.bulletSize = 6;
            graph2.columnWidth = 0;
            graph2.id = "AmGraph-2";
            graph2.minDistance = 0;
            graph2.negativeBase = -3;
            graph2.showAllValueLabels = true;
            graph2.stackable = false;
            graph2.switchable = false;
            graph2.title = "Session";
//            graph2.valueAxis = "ValueAxis-2";
            graph2.valueField = "column-2";
//            graph2.yAxis = "Not set";
            graph2.type = "line";
            //graph2.fillAlphas = 0;
            graph2.lineColor = "#FF9900";
            graph2.connect = false;
            graph2.gapPeriod = chartOption.interval;
            
            if(chartOption.cpuCheck > 1)            	
            	chart.addGraph(graph2);
                   
			// CURSOR
            var chartCursor = new AmCharts.ChartCursor();	
            chartCursor.bulletsEnabled = true;
            chartCursor.cursorColor = "#0F47C7";
            chartCursor.cursorPosition = "middle";
            chartCursor.categoryBalloonDateFormat = "YYYY-MM-DD JJ:NN:SS";
            chartCursor.graphBulletAlpha = 0;
//            chartCursor.zoomable = true;
            chartCursor.cursorColor= chartOption.cursorColor;
            
//            chartCursor.bulletSize = 6;
//            chartCursor.graphBulletAlpha = 0.02;
//            chartCursor.graphBulletSize = 2;
            
            chart.addChartCursor(chartCursor);
            
            if (chartOption.spSessionMax != null && chartOption.spSessionMax > 0)
            {
            	if(chartOption.spSessionMax > chartOption.maxSPSession)
            	{
	            	var guide = new AmCharts.Guide();
	                guide.value = chartOption.spSessionMax; 
	//                guide.lineColor = "#FF0000";
	                guide.lineColor = "#CC0000";
	                //guide.dashLength = 5;
	//                guide.prefixesOfBigNumbers = [
	//                                                   {number:1e+3,prefix:"k"},
	//                                                   {number:1e+6,prefix:"M"},
	//                                                   {number:1e+9,prefix:"G"}];
	           
	                guide.label = "[Session-ㅌㅌㅌㅌ]critical value : " + chartOption.spSessionMaxUnit + chartOption.axistitle2;               
	                guide.inside = true;
	                guide.lineAlpha = 1;   
	//                guide.lineThickness = 1;
	//                guide.angle = 0;
	//                guide.balloonColor = "";
	//                guide.balloonText = "";
	//                guide.fillAlpha = 1;
	//                guide.fillColor = "";
	//                guide.id = "Guide-1";
	//                guide.position = "right";
	//                guide.tickLength = 4;
	//                guide.value = 30000000;
	//                guide.valueAxis = "ValueAxis-2";
	                valueAxis2.addGuide(guide);
	                guide = null;
            	}
            }

            // Balloon             

            
            // LEGEND
            var legend = new AmCharts.AmLegend();
            legend.align = "center";             
            legend.position = "bottom";            
            legend.verticalGap = 1;            
            legend.autoMargins = false;
            legend.equalWidths = false;
            legend.marginBottom = -4;
            legend.marginLeft = 49;
            legend.markerLabelGap = 4;
            legend.markerSize = 8;
            legend.valueAlign = "left";
            legend.valueText = "";
            legend.valueWidth = 30;
            legend.verticalGap = 5;
            
            chart.addLegend(legend);
            
            // WRITE
            chart.write(chartOption.chartname); 
            
	        return chart;
		}
	},
	/*
	 * 모듈명 : OBAreaChartViewer
	 * 성격 : Line + Fill Alpha(area) chart
	 * 매개변수 : chartDatalist, chartOptionList ( min, max, linecolor, chartname, axistitle, maxPos )
	 * 사용처 : 1. 대시보드 위젯 (설정 변경 현황, 장애 모니터링 현황, 응답시간, 전체 ADC Throughput, ADC Memory, ADC CPU, Concurrent session, Throughput)
	 * 사용처 : 2. 서비스 성능 모니터링 (응답시간)
	 * 사용처 : 3. 장비 모니터링 ( Memory, Concurrent session, Throughtput, Error Packets, Dropped Packets, SSL Transactions, HTTP Requests, cursorColor )
	 *
	 * */
	OBChartViewerTest: function(chartDatalist, chartOptionList)
	{
		with (this) 
		{   
			var chart = AmCharts.makeChart(chartOptionList.chartname, {
			    "type": "serial",
			    "theme": "light",
			    "marginRight": 70,
			    "autoMarginOffset": 20,
			    "dataProvider": [{
			        "year": "1950",
			        "value": -0.307
			    }, {
			        "year": "1951",
			        "value": -0.168
			    }, {
			        "year": "1952",
			        "value": -0.073
			    }, {
			        "year": "1953",
			        "value": -0.027
			    }, {
			        "year": "1954",
			        "value": -0.251
			    }, {
			        "year": "1955",
			        "value": -0.281
			    }, {
			        "year": "1956",
			        "value": -0.348
			    }, {
			        "year": "1957",
			        "value": -0.074
			    }, {
			        "year": "1958",
			        "value": -0.011
			    }, {
			        "year": "1959",
			        "value": -0.074
			    }, {
			        "year": "1960",
			        "value": -0.124
			    }, {
			        "year": "1961",
			        "value": -0.024
			    }, {
			        "year": "1962",
			        "value": -0.022
			    }, {
			        "year": "1963",
			        "value": 0
			    }, {
			        "year": "1964",
			        "value": -0.296
			    }, {
			        "year": "1965",
			        "value": -0.217
			    }, {
			        "year": "1966",
			        "value": -0.147
			    }, {
			        "year": "1971",
			        "value": -0.19
			    }, {
			        "year": "1972",
			        "value": -0.056
			    }, {
			        "year": "1973",
			        "value": 0.077
			    }, {
			        "year": "1974",
			        "value": -0.213
			    }, {
			        "year": "1975",
			        "value": -0.17
			    }, {
			        "year": "1976",
			        "value": -0.254
			    }, {
			        "year": "1977",
			        "value": 0.019
			    }, {
			        "year": "1978",
			        "value": -0.063
			    }, {
			        "year": "1979",
			        "value": 0.05
			    }, {
			        "year": "1980",
			        "value": 0.077
			    }, {
			        "year": "1981",
			        "value": 0.12
			    }, {
			        "year": "1982",
			        "value": 0.011
			    }, {
			        "year": "1983",
			        "value": 0.177
			    }, {
			        "year": "1989",
			        "value": 0.104
			    }, {
			        "year": "1990",
			        "value": 0.255
			    }, {
			        "year": "1991",
			        "value": 0.21
			    }, {
			        "year": "1992",
			        "value": 0.065
			    }, {
			        "year": "1993",
			        "value": 0.11
			    }, {
			        "year": "1994",
			        "value": 0.172
			    }, {
			        "year": "1995",
			        "value": 0.269
			    }, {
			        "year": "1996",
			        "value": 0.141
			    }, {
			        "year": "1997",
			        "value": 0.353
			    }, {
			        "year": "1998",
			        "value": 0.548
			    }, {
			        "year": "1999",
			        "value": 0.298
			    }, {
			        "year": "2000",
			        "value": 0.267
			    }, {
			        "year": "2001",
			        "value": 0.411
			    }, {
			        "year": "2002",
			        "value": 0.462
			    }, {
			        "year": "2003",
			        "value": 0.47
			    }, {
			        "year": "2004",
			        "value": 0.445
			    }, {
			        "year": "2005",
			        "value": 0.47
			    }],
			    "balloon": {
			        "cornerRadius": 6
			    },
			    "valueAxes": [{
			        "axisAlpha": 0
			    }],
			    "graphs": [{
			        "balloonText": "[[category]]<br><b><span style='font-size:14px;'>[[value]] C</span></b>",
			        "bullet": "round",
			        "bulletSize": 6,
			        "connect": false,
			        "lineColor": "#b6d278",
			        "lineThickness": 2,
			        "negativeLineColor": "#487dac",
			        "valueField": "value",
			        "fillAlphas": 0.3
			    }],
			    "chartCursor": {
			        "categoryBalloonDateFormat": "YYYY",
			        "cursorAlpha": 0.1,
			        "cursorColor": "#000000",
			        "fullWidth": true,
			        "graphBulletSize": 2
			    },
			    "chartScrollbar": {},
			    "dataDateFormat": "YYYY",
			    "categoryField": "year",
			    "categoryAxis": {
			        "minPeriod": "YYYY",
			        "parseDates": true,
			        "minorGridEnabled": true
			    },
			    "export": {
			        "enabled": true
			    }
			});

			chart.addListener("dataUpdated", zoomChart);

			function zoomChart(){
			    chart.zoomToDates(new Date(1970, 0), new Date(1995, 0));
			}
		}
	},
	
	OBAreaChartViewerNew: function(chartDatalist, chartOptionList)
	{
		with (this) 
		{   
			var chart = AmCharts.makeChart(chartOptionList.chartname,
				{
					"type": "serial",
					"pathToImages": "/js/extern/amcharts_images/",
					"categoryField": "occurredTime",
//					"rotate": true,
					"gridAboveGraphs": true,

		             // 대상이 없을때 보여지는 챠트  양 사이드 border            
		             "plotAreaBorderAlpha":   1,
		             "plotAreaBorderColor": "#e1e1e1",
		             
					"autoMargins": false,
					"columnsAboveLines" : true,
					"marginTop": 5,
					"marginBottom": 23,
					"marginLeft" : 50,
					"marginRight": 20,
					"fontFamily" : "Arial",
		            "fontSize" : 10,
		            "zoomOutButton" :
		             {
			             backgroundColor: '#000000',
			             backgroundAlpha: 0.05,
			             fontSize : 9
		             },
		            "zoomOutButtonImageSize" : 14,
		            "usePrefixes" : true,
		            "prefixesOfBigNumbers" :
		            [{number:1e+3,prefix:"k"},
		               {number:1e+6,prefix:"M"},
		               {number:1e+9,prefix:"G"}
		            ],
//					"plotAreaBorderAlpha": 0.18,						
//					"theme": "none",
					
					"categoryAxis":
					{
						"parseDates" : true, 
						"minPeriod" : "ss",
						"dashLength" : 2,
						"dateFormats" : 
						[{period:'ss',format:'JJ:NN:SS'},
                             {period:'mm',format:'JJ:NN'},
                             {period:'hh',format:'JJ:NN'},
                             {period:'DD',format:'YYYY-MM-DD'},
                             {period:'WW',format:'YYYY-MM-DD'},
                             {period:'MM',format:'MM'},
                             {period:'YYYY',format:'YYYY'}
                       ],
						"gridAlpha": 0.3
//						"showFirstLabel": false
					},
								
					"dataProvider": chartDatalist,
									
						"graphs":
						[{
							"title": chartOptionList.title,
							"valueField": "firstValue",
							"bullet" : "round",
							"bulletSize" : 6,
//							"hideBulletsCount" : 5,
							"type": "line",
							"fillAlphas": 0.3,
							"lineColor": chartOptionList.linecolor,
							"lineThickness" : 1,
							"negativeLineColor": "#487dac",
							"balloonText": chartOptionList.title + ": [[value]]" + chartOptionList.axistitle,
//							"connect" : false,
							"gapPeriod" : chartOptionList.interval	
								
								
//							"columnWidth": 0.45,
//							"lineAlpha": 0,
//							"fillColors": "#6cb8c8",								
//							"id": "AmGraph-1",
//							"labelText": "",							
//							"minDistance": 0,								
//							"noStepRisers": true,
//							"showBalloon": true,
								
							}],						
						"valueAxes":
						[{
//							"axisAlpha": 0
							"stackType": 'none',
							"tickLength": 0,
							"dashLength": 2,
							"gridAlpha": 0.3,
							"axisThickness" : 1,
							"usePrefixes" :  true,
							"prefixesOfBigNumbers" :
								[
	                                {number:1e+3,prefix:"k"},
	                                {number:1e+6,prefix:"M"},
	                                {number:1e+9,prefix:"G"}
	                            ],
	                        "minimum": chartOptionList.min,    
							"maximum": chartOptionList.max,
							"title": chartOptionList.axistitle,							
							"titleBold": false,
							"titleColor" : "#666666"
							    
//							"axisTitleOffset": 2,
//							"id": "ValueAxis-1",
//							"position": "bottom",
//							"radarCategoriesEnabled": false,							
//							"unit": "%",							
//							"labelOffset": -2,
//							"minHorizontalGap": 74,
//							"minVerticalGap": 130,
							
						}],
						"chartCursor": {
//							"categoryBalloonDateFormat": "YYYY",
//					        "cursorAlpha": 0.1,
//					        "cursorColor": "#000000",
//					        "fullWidth": true,
//					        "graphBulletSize": 2
					        
							"cursorPosition" : "middle",
							"categoryBalloonDateFormat": "YYYY-MM-DD JJ:NN:SS",
							"zoomable" : true,
							"cursorColor" : chartOptionList.cursorColor
							
//					        "cursorAlpha": 0.1,					        
//					        "fullWidth": true,
//					        "graphBulletSize": 2
					    },
					    "chartScrollbar": {
					    	"dragIconWidth" : 25,
				            "graphType" : "line",
				            "scrollbarHeight" : 9, 
				            "offset" : -1  
					    	
					    },
					    "balloon": {
//					    	"cornerRadius": 6
					    	"adjustBorderColor" : true,
				            "pointerWidth" : 3,
				            "shadowAlpha" :  0,
				            "borderThickness" : 1,
				            "fillAlpha" : 0.95,
				            "horizontalPadding" : 6,
				            "verticalPadding" : -2,
				            "fontSize" : 8
					    	
					    },
					    "legend": {
					    	"autoMargins" : false,
				             "marginBottom" : -5,
				             "marginTop" : -1,           
				             "align" : "center",            
				             "markerLabelGap" : 5,           
				             "position" : "bottom",
				             "valueWidth" : 20,
				             "verticalGap" : 1,
				             "valueText" : "[[value]]",
				             "markerSize" : 11
					    	
					    },
					    "guide": {
					    	"value" : chartOptionList.maxPos,
			                 "lineColor" : "#CC0000",
			                 "label" : "max : " + addCommaforNumber(chartOptionList.maxPos) + chartOptionList.axistitle,               
			                 "inside" : true,
			                 "lineAlpha" : 1  
					    	
					    },
//					    "dataDateFormat": "YYYY",
//					    "categoryField": "year",
//					    "categoryAxis": {
//					        "minPeriod": "YYYY",
//					        "parseDates": true,
//					        "minorGridEnabled": true
//					    },
					    "export": {
					        "enabled": true
					    }
				}
			); 
			
//			chart.addListener("dataUpdated", zoomChart);
			// WRITE
            chart.write(chartOptionList.chartname);
			
			function zoomChart(){
			    chart.zoomToIndexes(10, 20);
			}
			
			return chart;
		}
	},
	OBAreaChartViewer: function(chartDatalist, chartOptionList)
	{
		with (this) 
		{   
			 var chartData = chartDatalist;
			 var chartOption = chartOptionList;
			 
             // SERIAL CHART 옵션
             var chart = new AmCharts.AmSerialChart();
             chart.pathToImages = "/js/extern/amcharts_images/";             
             chart.dataProvider = chartData;
             chart.categoryField = "occurredTime";
//             chart.gridAboveGraphs= "true"; 
             
             // 대상이 없을때 보여지는 챠트  양 사이드 border            
             chart.plotAreaBorderAlpha =  1;
             chart.plotAreaBorderColor =  '#e1e1e1'; 
             
             chart.autoMargins =  false;
             chart.columnsAboveLines = true;
             chart.marginTop = 5;
             chart.marginBottom = 23;
             chart.marginLeft = 50;
             chart.marginRight = 20;       
             
             chart.fontFamily = "Arial";
             chart.fontSize = 10;
             chart.zoomOutButton =
             {
	             backgroundColor: '#000000',
	             backgroundAlpha: 0.05,
	             fontSize : 9
             };
             chart.zoomOutButtonImageSize = 14;
             chart.usePrefixes = true;
             chart.prefixesOfBigNumbers = [{number:1e+3,prefix:"k"},
                                           {number:1e+6,prefix:"M"},
                                           {number:1e+9,prefix:"G"}];
             // 처음 로드 화면에 zoom 기능을 사용하고 싶다면 ("dataUpdated", zoomChart); 로 add 후 조절은 zoomChart method 에서한다.
             //chart.addListener("dataUpdated", zoomChart);

             // Axes
             // X축 Category Axis (on the down)               
             var categoryAxis = chart.categoryAxis;
             categoryAxis.parseDates = true; // as our data is date-based, we set parseDates to true
             categoryAxis.minPeriod = "ss"; // our data is daily, so we set minPeriod to DD
             categoryAxis.dashLength = 2;
             categoryAxis.dateFormats = [{period:'ss',format:'JJ:NN:SS'},
                                         {period:'mm',format:'JJ:NN'},
                                         {period:'hh',format:'JJ:NN'},
                                         {period:'DD',format:'YYYY-MM-DD'},
                                         {period:'WW',format:'YYYY-MM-DD'},
                                         {period:'MM',format:'MM'},
                                         {period:'YYYY',format:'YYYY'}];
             categoryAxis.gridAlpha = 0.3;             

             // Y축 value axis (on the left)
             var valueAxis = new AmCharts.ValueAxis();
             valueAxis.stackType = "none";
             valueAxis.tickLength = 0;
             valueAxis.dashLength = 2;
             valueAxis.gridAlpha = 0.3; 
             // valueAxis.axisColor = chartOption.linecolor;
             valueAxis.axisThickness = 1;
             valueAxis.usePrefixes = true;
             valueAxis.prefixesOfBigNumbers = [
                                                {number:1e+3,prefix:"k"},
                                                {number:1e+6,prefix:"M"},
                                                {number:1e+9,prefix:"G"}];
             valueAxis.minimum = chartOption.min;
             if (chartOption.max != null)
             {
            	 valueAxis.maximum = chartOption.max;             
             }
             valueAxis.title = chartOption.axistitle;
             valueAxis.titleBold = false;
             valueAxis.titleColor = "#666666";
             chart.addValueAxis(valueAxis);             

             // GRAPHS
             //  graph One Value
             var graph = new AmCharts.AmGraph();
             graph.valueAxis = valueAxis; // we have to indicate which value axis should be used
             graph.title = chartOption.title;
             graph.valueField = "firstValue";
             graph.bullet = "round";
             graph.hideBulletsCount = 5; 
             graph.type = "line";

             graph.fillAlphas = 0.3;

             graph.lineColor = chartOption.linecolor;
             graph.lineThickness = "1";
             graph.gapPeriod = chartOption.interval;
             graph.connect = false;
             graph.balloonText=chartOption.title + ": [[value]]" + chartOption.axistitle;
             chart.addGraph(graph);                    

//             console.log(chartOption.interval);
             // CURSOR
             var chartCursor = new AmCharts.ChartCursor();
             chartCursor.cursorPosition = "middle";
             chartCursor.categoryBalloonDateFormat = "YYYY-MM-DD JJ:NN:SS";
             chartCursor.zoomable = true;
             chartCursor.cursorColor= chartOption.cursorColor;
             chart.addChartCursor(chartCursor);
             
             if (chartOption.maxPos != null && chartOption.maxPos > 0)
             {
            	 var guide = new AmCharts.Guide();
                 guide.value = chartOption.maxPos; 
                 guide.lineColor = "#CC0000";
                 //guide.dashLength = 5;
                 guide.label = "max : " + addCommaforNumber(chartOption.maxPos) + chartOption.axistitle;               
                 guide.inside = true;
                 guide.lineAlpha = 1;                 
                 valueAxis.addGuide(guide);
                 guide = null;
             }
             // SCROLLBAR (상단의 확대 축소 바)
             var chartScrollbar = new AmCharts.ChartScrollbar();
             chart.addChartScrollbar(chartScrollbar);
             chartScrollbar.dragIconHeight =  20;
             chartScrollbar.dragIconWidth = 20;
             chartScrollbar.scrollbarHeight = 10;            
             
             // Balloon             
             var balloon = chart.balloon;
             balloon.adjustBorderColor = true;
             balloon.pointerWidth = 3;
             balloon.shadowAlpha = 0; 
             balloon.borderThickness = 1;
             balloon.fillAlpha = 0.95;
             balloon.horizontalPadding = 6;
             balloon.verticalPadding = -2; 
             balloon.fontSize = 8;   
             
             // LEGEND
			 var legend = new AmCharts.AmLegend();
             legend.autoMargins = false;
             legend.marginBottom =  -5;
             legend.marginTop =  -1;              
             legend.align = "center";            
             legend.markerLabelGap = 5;           
             legend.position = "bottom";
             legend.valueWidth = 20;
             legend.verticalGap = 1;
             legend.valueText = "[[value]]";
             legend.markerSize = 11;
//             chart.addLegend(legend);   
             

             // WRITE
             chart.write(chartOption.chartname);          

         // this method is called when chart is first inited as we listen for "dataUpdated" event
	         function zoomChart()
	         {
	             // different zoom methods can be used - zoomToIndexes, zoomToDates, zoomToCategoryValues
	             chart.zoomToIndexes(10, 20);
	         }
//	         console.log(chart);
	         return chart;
		}
	},
	
	// TODO
	// 구간응답시간 - multi chart (sum -> multi 로 변경)
	OBAreaResponseMultiChartViewer: function(chartDatalist, chartOptionList)
	{
		with (this) 
		{   
			 var chartData = chartDatalist;
			 var chartOption = chartOptionList;
			 
             // SERIAL CHART 옵션
             var chart = new AmCharts.AmSerialChart();
             chart.pathToImages = "/js/extern/amcharts_images/";             
             chart.dataProvider = chartData;
             chart.categoryField = "occurredTime";
             chart.gridAboveGraphs= "true"; 

             // 대상이 없을때 보여지는 챠트  양 사이드 border            
             chart.plotAreaBorderAlpha =  1;
             chart.plotAreaBorderColor =  '#e1e1e1';              
             
             chart.autoMargins =  false;
             chart.columnsAboveLines = true;
             chart.marginTop = 5;
             chart.marginBottom = 23;
             chart.marginLeft = 50;
             chart.marginRight = 20;             
             
             chart.fontFamily = "Arial";
             chart.fontSize = 10;
             chart.zoomOutButton =
             {
	             backgroundColor: '#000000',
	             backgroundAlpha: 0.05,
	             fontSize : 9
             };
             chart.verticalPadding = 1,
             chart.zoomOutButtonImageSize = 14;
             chart.usePrefixes = true;
             chart.prefixesOfBigNumbers = [{number:1e+3,prefix:"k"},
                                           {number:1e+6,prefix:"M"},
                                           {number:1e+9,prefix:"G"}];
             // 처음 로드 화면에 zoom 기능을 사용하고 싶다면 ("dataUpdated", zoomChart); 로 add 후 조절은 zoomChart method 에서한다.
             //chart.addListener("dataUpdated", zoomChart);
                                      
             // Axes
             // X축 Category Axis (on the down)               
             var categoryAxis = chart.categoryAxis;
             categoryAxis.parseDates = true; // as our data is date-based, we set parseDates to true
             categoryAxis.minPeriod = "ss"; // our data is daily, so we set minPeriod to DD
             categoryAxis.dashLength = 2;
             categoryAxis.dateFormats = [{period:'ss',format:'JJ:NN:SS'},
                                         {period:'mm',format:'JJ:NN'},
                                         {period:'hh',format:'JJ:NN'},
                                         {period:'DD',format:'YYYY-MM-DD'},
                                         {period:'WW',format:'YYYY-MM-DD'},
                                         {period:'MM',format:'MM'},
                                         {period:'YYYY',format:'YYYY'}];
             categoryAxis.gridAlpha = 0.3;             

             // Y축 value axis (on the left)
             var valueAxis = new AmCharts.ValueAxis();
//             valueAxis.stackType = "regular";
             valueAxis.stackType = "none";             
             valueAxis.tickLength = 0;
             valueAxis.dashLength = 2;
             valueAxis.gridAlpha = 0.3; 
             // valueAxis.axisColor = chartOption.linecolor;
             valueAxis.axisThickness = 1;
             valueAxis.usePrefixes = true;
             valueAxis.prefixesOfBigNumbers = [
                                                {number:1e+3,prefix:"k"},
                                                {number:1e+6,prefix:"M"},
                                                {number:1e+9,prefix:"G"}];
             valueAxis.minimum = chartOption.min;
             if (chartOption.max != null)
             {
            	 valueAxis.maximum = chartOption.max;             
             }
             valueAxis.title = chartOption.axistitle;
             valueAxis.titleBold = false;
             valueAxis.titleColor = "#666666";
             chart.addValueAxis(valueAxis);    
 /*            
             // Y축 value axis (on the right, hidden SUM using)
             var valueAxis2 = new AmCharts.ValueAxis();
             valueAxis2.stackType = "none";
             valueAxis2.tickLength = 0;             
            // valueAxis2.axisColor = chartOption.linecolor;
             valueAxis2.axisThickness = 0;
             valueAxis2.usePrefixes = true;
             valueAxis2.position = "right";
             valueAxis2.fontSize = 0;
             valueAxis2.gridAlpha = 0;
             
             valueAxis2.prefixesOfBigNumbers = [
                                                {number:1e+3,prefix:"k"},
                                                {number:1e+6,prefix:"M"},
                                                {number:1e+9,prefix:"G"}];
             valueAxis2.minimum = chartOption.min;
             if (chartOption.max != null)
             {
            	 valueAxis.maximum = chartOption.max;             
             }
             valueAxis2.title = chartOption.axistitle;
             valueAxis2.titleBold = false;
             valueAxis2.titleColor = "#666666";
             chart.addValueAxis(valueAxis2); 
*/
             // GRAPHS
             //  graph One Value
             var graph1 = new AmCharts.AmGraph();
             graph1.valueAxis = valueAxis; // we have to indicate which value axis should be used
             graph1.title = chartData[1].firstName;
             graph1.valueField = "firstValue";
             graph1.bullet = "round";
             graph1.hideBulletsCount = 5; 
             graph1.type = "line";
             graph1.fillAlphas = 1;
             graph1.lineColor = chartOption.linecolor1;
             graph1.lineThickness = "1";
             graph1.balloonText="[[firstName]] : [[value]]" + chartOption.axistitle;
             graph1.connect = false;
             graph1.gapPeriod = chartOption.interval;
             chart.addGraph(graph1);                        

             var graph2 = new AmCharts.AmGraph();
             graph2.valueAxis = valueAxis; // we have to indicate which value axis should be used
             graph2.title = chartData[1].secondName;
             graph2.valueField = "secondValue";
             graph2.bullet = "round";
             graph2.hideBulletsCount = 5; 
             graph2.type = "line";
             graph2.fillAlphas = 1;
             graph2.lineColor = chartOption.linecolor2;
             graph2.lineThickness = "1";
             graph2.balloonText="[[secondName]] : [[value]]" + chartOption.axistitle;
             graph2.connect = false;
             graph2.gapPeriod = chartOption.interval;
             if(chartData[1].secondName != null)
            	 graph2.visibleInLegend = true;
             else
            	 graph2.visibleInLegend = false;
             chart.addGraph(graph2); 
             
             var graph3 = new AmCharts.AmGraph();
             graph3.valueAxis = valueAxis; // we have to indicate which value axis should be used
             graph3.title = chartData[1].thirdName;
             graph3.valueField = "thirdValue";
             graph3.bullet = "round";
             graph3.hideBulletsCount = 5; 
             graph3.type = "line";
             graph3.fillAlphas = 1;
             graph3.lineColor = chartOption.linecolor3;
             graph3.lineThickness = "1";
             graph3.balloonText="[[thirdName]] : [[value]]" + chartOption.axistitle;
             graph3.connect = false;
             graph3.gapPeriod = chartOption.interval;
             if(chartData[1].thirdName != null)
            	 graph3.visibleInLegend = true;
             else
            	 graph3.visibleInLegend = false;
             chart.addGraph(graph3);   
             
             var graph4 = new AmCharts.AmGraph();
             graph4.valueAxis = valueAxis; // we have to indicate which value axis should be used
             graph4.title = chartData[1].fourthName;
             graph4.valueField = "fourthValue";
             graph4.bullet = "round";
             graph4.hideBulletsCount = 5; 
             graph4.type = "line";
             graph4.fillAlphas = 1;
             graph4.lineColor = chartOption.linecolor4;
             graph4.lineThickness = "1";
             graph4.balloonText="[[fourthName]] : [[value]]" + chartOption.axistitle;
             graph4.connect = false;
             graph4.gapPeriod = chartOption.interval;
             if(chartData[1].fourthName != null)
            	 graph4.visibleInLegend = true;
             else
            	 graph4.visibleInLegend = false;
             chart.addGraph(graph4);   
             
             var graph5 = new AmCharts.AmGraph();
             graph5.valueAxis = valueAxis; // we have to indicate which value axis should be used
             graph5.title = chartData[1].fifthName;
             graph5.valueField = "fifthValue";
             graph5.bullet = "round";
             graph5.hideBulletsCount = 5; 
             graph5.type = "line";
             graph5.fillAlphas = 1;
             graph5.lineColor = chartOption.linecolor5;
             graph5.lineThickness = "1";
             graph5.balloonText="[[fifthName]] : [[value]]" + chartOption.axistitle;
             graph5.connect = false;
             graph5.gapPeriod = chartOption.interval;
             if(chartData[1].fifthName != null)
            	 graph5.visibleInLegend = true;
             else
            	 graph5.visibleInLegend = false;
             chart.addGraph(graph5);   
             
             var graph6 = new AmCharts.AmGraph();
             graph6.valueAxis = valueAxis; // we have to indicate which value axis should be used
             graph6.title = chartData[1].sixthName;
             graph6.valueField = "sixthValue";
             graph6.bullet = "round";
             graph6.hideBulletsCount = 5; 
             graph6.type = "line";
             graph6.fillAlphas = 1;
             graph6.lineColor = chartOption.linecolor6;
             graph6.lineThickness = "1";
             graph6.balloonText="[[sixthName]] : [[value]]" + chartOption.axistitle;
             graph6.connect = false;
             graph6.gapPeriod = chartOption.interval;
             if(chartData[1].sixthName != null)
            	 graph6.visibleInLegend = true;
             else
            	 graph6.visibleInLegend = false;
             chart.addGraph(graph6);   
             
//             var graph7 = new AmCharts.AmGraph();
//             graph7.valueAxis = valueAxis2; // we have to indicate which value axis should be used
//             graph7.title = "SUM";
//             graph7.valueField = "sumValue";
//             graph7.bullet = "round";
//             graph7.hideBulletsCount = 5; 
//             graph7.type = "line";
//             graph7.fillAlphas = 0;
//             graph7.lineColor = chartOption.linecolor7;
//             graph7.lineThickness = "0";
//             graph7.balloonText="SUM : [[value]]" + chartOption.axistitle;
//             chart.addGraph(graph7); 

             // CURSOR
             var chartCursor = new AmCharts.ChartCursor();
             chartCursor.cursorPosition = "middle";
             chartCursor.categoryBalloonDateFormat = "YYYY-MM-DD JJ:NN:SS";
             chartCursor.zoomable = true;
             chartCursor.cursorColor= chartOption.cursorColor;
             chart.addChartCursor(chartCursor);
             
             if (chartOption.maxPos != null && chartOption.maxPos > 0)
             {
            	 var guide = new AmCharts.Guide();
                 guide.value = chartOption.maxPos; 
                 guide.lineColor = "#CC0000";
                 //guide.dashLength = 5;
                 guide.label = "max : " + addCommaforNumber(chartOption.maxPos) + chartOption.axistitle;               
                 guide.inside = true;
                 guide.lineAlpha = 1;                 
                 valueAxis.addGuide(guide);
                 guide = null;
             }

             // SCROLLBAR (상단의 확대 축소 바)
             var chartScrollbar = new AmCharts.ChartScrollbar();
             chart.addChartScrollbar(chartScrollbar);
             chartScrollbar.dragIconHeight =  20;
             chartScrollbar.dragIconWidth = 20;
             chartScrollbar.scrollbarHeight = 10;
             
             // Balloon             
             var balloon = chart.balloon;
             balloon.adjustBorderColor = true;
             balloon.pointerWidth = 3;
             balloon.shadowAlpha = 0; 
             balloon.borderThickness = 1;
             balloon.fillAlpha = 0.95;
             balloon.horizontalPadding = 6;
             balloon.verticalPadding = -2; 
             balloon.fontSize = 8;  
             
             // LEGEND
             var legend = new AmCharts.AmLegend();
             legend.autoMargins = false;
             legend.marginBottom =  -5;
             legend.marginTop =  -1; 
//             legend.backgroundColor ="#ff0000";
//             legend.backgroundAlpha ="0.9";
             legend.align = "center";            
             legend.markerLabelGap = 4;           
             legend.position = "bottom";
             legend.valueWidth = 50;
             legend.verticalGap = 1;
//             legend.valueText = "[[value]]";
             legend.labelText = "[[title]]";
             legend.markerSize = 10;
             legend.fontSize = 9;
             
//           legend.reversedOrder = true;       
             if(chartData[1].firstName != null)
            	 chart.addLegend(legend); 
            
             // WRITE
             chart.write(chartOption.chartname);          

         // this method is called when chart is first inited as we listen for "dataUpdated" event
	         function zoomChart()
	         {
	             // different zoom methods can be used - zoomToIndexes, zoomToDates, zoomToCategoryValues
	             chart.zoomToIndexes(10, 20);
	         }
	         return chart;
		}
	},
	
	/*
	 * 모듈명 : OBDashboardGroupChartViewer
	 * 성격 : Line chart
	 * 매개변수 : chartDatalist, chartOptionList ( min, max, linecolor, chartname, axistitle, maxPos )
	 * 사용처 : 1. 대시보드 위젯 (설정 변경 현황, 장애 모니터링 현황, 응답시간, 전체 ADC Throughput, ADC Memory, ADC CPU, Concurrent session, Throughput)
	 * 
	 *
	 * */
	
	// TODO 
	OBDashboardGroupChartViewer: function(chartDatalist, chartOptionList)
	{
		with (this) 
		{
			 var chartData = chartDatalist;
			 var chartOption = chartOptionList;
			 
             // SERIAL CHART 옵션
             var chart = new AmCharts.AmSerialChart();
             chart.pathToImages = "/js/extern/amcharts_images/";             
             chart.dataProvider = chartData;
             chart.categoryField = "occurredTime";
             chart.gridAboveGraphs= "true"; 
             
             // 대상이 없을때 보여지는 챠트  양 사이드 border            
             chart.plotAreaBorderAlpha =  1;
             chart.plotAreaBorderColor =  '#e1e1e1'; 
             
             chart.autoMargins =  false;
             chart.columnsAboveLines = true;
             chart.marginTop = 5;
             chart.marginBottom = 19;
             chart.marginLeft = 50;
             chart.marginRight = 20;             
             
             chart.fontFamily = "Arial";
             chart.fontSize = 10;
             chart.zoomOutButton =
             {
	             backgroundColor: '#000000',
	             backgroundAlpha: 0.05,
	             fontSize : 9
             };
             chart.zoomOutButtonImageSize = 14;
             chart.usePrefixes = true;
             chart.prefixesOfBigNumbers = [{number:1e+3,prefix:"k"},
                                           {number:1e+6,prefix:"M"},
                                           {number:1e+9,prefix:"G"}];
             // 처음 로드 화면에 zoom 기능을 사용하고 싶다면 ("dataUpdated", zoomChart); 로 add 후 조절은 zoomChart method 에서한다.
             //chart.addListener("dataUpdated", zoomChart);

             // Axes
             // X축 Category Axis (on the down)               
             var categoryAxis = chart.categoryAxis;
             categoryAxis.parseDates = true; // as our data is date-based, we set parseDates to true
             categoryAxis.minPeriod = "ss"; // our data is daily, so we set minPeriod to DD
             categoryAxis.dashLength = 2;
             categoryAxis.dateFormats = [{period:'ss',format:'JJ:NN:SS'},
                                         {period:'mm',format:'JJ:NN'},
                                         {period:'hh',format:'JJ:NN'},
                                         {period:'DD',format:'YYYY-MM-DD'},
                                         {period:'WW',format:'YYYY-MM-DD'},
                                         {period:'MM',format:'MM'},
                                         {period:'YYYY',format:'YYYY'}];
             categoryAxis.gridAlpha = 0.3;             

             // Y축 value axis (on the left)
             var valueAxis = new AmCharts.ValueAxis();
             valueAxis.stackType = "none";
             valueAxis.tickLength = 0;
             valueAxis.dashLength = 2;
             valueAxis.gridAlpha = 0.3; 
             // valueAxis.axisColor = chartOption.linecolor;
             valueAxis.axisThickness = 1;
             valueAxis.usePrefixes = true;
             valueAxis.prefixesOfBigNumbers = [
                                                {number:1e+3,prefix:"k"},
                                                {number:1e+6,prefix:"M"},
                                                {number:1e+9,prefix:"G"}];
             valueAxis.minimum = chartOption.min;
             if (chartOption.max != null)
             {
            	 valueAxis.maximum = chartOption.max;             
             }
             valueAxis.title = chartOption.axistitle;
             valueAxis.titleBold = false;
             valueAxis.titleColor = "#666666";
             chart.addValueAxis(valueAxis);             

             // GRAPHS
             //  graph One Value
             var graph1 = new AmCharts.AmGraph();
             graph1.valueAxis = valueAxis; // we have to indicate which value axis should be used
             graph1.title = chartData[1].firstName;
             graph1.valueField = "firstValue";
             graph1.bullet = "round";
//           graph1.bulletBorderThickness = 0;
//           graph1.bulletAlpha = 0.85;
//           graph1.bulletSize = 1;
             graph1.hideBulletsCount = 5; 
             graph1.type = "line";
             graph1.fillAlphas = 0;
             graph1.lineColor = chartOption.linecolor1;
             graph1.lineThickness = "1";
             graph1.balloonText="[[firstName]] : [[value]]" + chartOption.axistitle;
             graph1.connect = false; //연결옵션 false
             graph1.gapPeriod = chartOption.interval;
             chart.addGraph(graph1);  
             
             //  graph One Value
             var graph2 = new AmCharts.AmGraph();
             graph2.valueAxis = valueAxis; // we have to indicate which value axis should be used
             graph2.title = chartData[1].secondName;
             graph2.valueField = "secondValue";
             graph2.bullet = "round";
//           graph2.bulletBorderThickness = 0;
//           graph2.bulletAlpha = 0.85;
//           graph2.bulletSize = 1;
             graph2.hideBulletsCount = 5; 
             graph2.type = "line";
             graph2.fillAlphas = 0;
             graph2.lineColor = chartOption.linecolor2;
             graph2.lineThickness = "1";
             graph2.balloonText="[[secondName]] : [[value]]" + chartOption.axistitle;
             graph2.connect = false; //연결옵션 false
             graph2.gapPeriod = chartOption.interval;
             chart.addGraph(graph2);   
             
             //  graph One Value
             var graph3 = new AmCharts.AmGraph();
             graph3.valueAxis = valueAxis; // we have to indicate which value axis should be used
             graph3.title = chartData[1].thirdName;
             graph3.valueField = "thirdValue";
             graph3.bullet = "round";
//           graph3.bulletBorderThickness = 0;
//           graph3.bulletAlpha = 0.85;
//           graph3.bulletSize = 1;
             graph3.hideBulletsCount = 5; 
             graph3.type = "line";
             graph3.fillAlphas = 0;
             graph3.lineColor = chartOption.linecolor3;
             graph3.lineThickness = "1";
             graph3.balloonText="[[thirdName]] : [[value]]" + chartOption.axistitle;
             graph3.connect = false; //연결옵션 false
             graph3.gapPeriod = chartOption.interval;
             
             if(chartData[1].thirdName != null)
            	 graph3.visibleInLegend = true;
             else
            	 graph3.visibleInLegend = false;
                          
             chart.addGraph(graph3);   
             
             //  graph One Value
             var graph4 = new AmCharts.AmGraph();
             graph4.valueAxis = valueAxis; // we have to indicate which value axis should be used
             graph4.title = chartData[1].fourthName;
             graph4.valueField = "fourthValue";
             graph4.bullet = "round";
//           graph4.bulletBorderThickness = 0;
//           graph4.bulletAlpha = 0.85;
//           graph4.bulletSize = 1;
             graph4.hideBulletsCount = 5; 
             graph4.type = "line";
             graph4.fillAlphas = 0;
             graph4.lineColor = chartOption.linecolor4;
             graph4.lineThickness = "1";
             graph4.balloonText="[[fourthName]] : [[value]]" + chartOption.axistitle;
             graph4.connect = false; //연결옵션 false
             graph4.gapPeriod = chartOption.interval;
             
             if(chartData[1].fourthName != null)
            	 graph4.visibleInLegend = true;
             else
            	 graph4.visibleInLegend = false;
                          
             chart.addGraph(graph4);   
             
             //  graph One Value
             var graph5 = new AmCharts.AmGraph();
             graph5.valueAxis = valueAxis; // we have to indicate which value axis should be used
             graph5.title = chartData[1].fifthName;
             graph5.valueField = "fifthValue";
             graph5.bullet = "round";
//           graph5.bulletBorderThickness = 0;
//           graph5.bulletAlpha = 0.85;
//           graph5.bulletSize = 1;
             graph5.hideBulletsCount = 5; 
             graph5.type = "line";
             graph5.fillAlphas = 0;
             graph5.lineColor = chartOption.linecolor5;
             graph5.lineThickness = "1";
             graph5.balloonText="[[fifthName]] : [[value]]" + chartOption.axistitle;
             graph5.connect = false; //연결옵션 false
             graph5.gapPeriod = chartOption.interval;
             
             if(chartData[1].fifthName != null)
            	 graph5.visibleInLegend = true;
             else
            	 graph5.visibleInLegend = false;
                          
             chart.addGraph(graph5);   
             
             //  graph One Value
             var graph6 = new AmCharts.AmGraph();
             graph6.valueAxis = valueAxis; // we have to indicate which value axis should be used
             graph6.title = chartData[1].sixthName;
             graph6.valueField = "sixthValue";
             graph6.bullet = "round";
//           graph6.bulletBorderThickness = 0;
//           graph6.bulletAlpha = 0.85;
//           graph6.bulletSize = 1;
             graph6.hideBulletsCount = 5; 
             graph6.type = "line";
             graph6.fillAlphas = 0;
             graph6.lineColor = chartOption.linecolor6;
             graph6.lineThickness = "1";
             graph6.balloonText="[[sixthName]] : [[value]]" + chartOption.axistitle;
             graph6.connect = false; //연결옵션 false
             graph6.gapPeriod = chartOption.interval;
             
             if(chartData[1].sixthName != null)
            	 graph6.visibleInLegend = true;
             else
            	 graph6.visibleInLegend = false;
                          
             chart.addGraph(graph6);   
             
             //  graph One Value
             var graph7 = new AmCharts.AmGraph();
             graph7.valueAxis = valueAxis; // we have to indicate which value axis should be used
             graph7.title = chartData[1].seventhName;
             graph7.valueField = "seventhValue";
             graph7.bullet = "round";
//           graph7.bulletBorderThickness = 0;
//           graph7.bulletAlpha = 0.85;
//           graph7.bulletSize = 1;
             graph7.hideBulletsCount = 5; 
             graph7.type = "line";
             graph7.fillAlphas = 0;
             graph7.lineColor = chartOption.linecolor7;
             graph7.lineThickness = "1";
             graph7.balloonText="[[seventhName]] : [[value]]" + chartOption.axistitle;
             graph7.connect = false; //연결옵션 false
             graph7.gapPeriod = chartOption.interval;
             
             if(chartData[1].seventhName != null)
            	 graph7.visibleInLegend = true;
             else
            	 graph7.visibleInLegend = false;
                          
             chart.addGraph(graph7);   
             
             //  graph One Value
             var graph8 = new AmCharts.AmGraph();
             graph8.valueAxis = valueAxis; // we have to indicate which value axis should be used
             graph8.title = chartData[1].eighthName;
             graph8.valueField = "eighthValue";
             graph8.bullet = "round";
//           graph8.bulletBorderThickness = 0;
//           graph8.bulletAlpha = 0.85;
//           graph8.bulletSize = 1;
             graph8.hideBulletsCount = 5; 
             graph8.type = "line";
             graph8.fillAlphas = 0;
             graph8.lineColor = chartOption.linecolor8;
             graph8.lineThickness = "1";
             graph8.balloonText="[[eighthName]] : [[value]]" + chartOption.axistitle;
             graph8.connect = false; //연결옵션 false
             graph8.gapPeriod = chartOption.interval;
             
             if(chartData[1].eighthName != null)
            	 graph8.visibleInLegend = true;
             else
            	 graph8.visibleInLegend = false;
             
             chart.addGraph(graph8);   
             
             //  graph One Value
             var graph9 = new AmCharts.AmGraph();
             graph9.valueAxis = valueAxis; // we have to indicate which value axis should be used
             graph9.title = chartData[1].ninthName;
             graph9.valueField = "ninthValue";
             graph9.bullet = "round";
//           graph9.bulletBorderThickness = 0;
//           graph9.bulletAlpha = 0.85;
//           graph9.bulletSize = 1;
             graph9.hideBulletsCount = 5; 
             graph9.type = "line";
             graph9.fillAlphas = 0;
             graph9.lineColor = chartOption.linecolor9;
             graph9.lineThickness = "1";
             graph9.balloonText="[[ninthName]] : [[value]]" + chartOption.axistitle;
             graph9.connect = false; //연결옵션 false
             graph9.gapPeriod = chartOption.interval;
             
             if(chartData[1].ninthName != null)
            	 graph9.visibleInLegend = true;
             else
            	 graph9.visibleInLegend = false;
             
             chart.addGraph(graph9);   
             
             //  graph One Value
             var graph10 = new AmCharts.AmGraph();
             graph10.valueAxis = valueAxis; // we have to indicate which value axis should be used
             graph10.title = chartData[1].tenthName;
             graph10.valueField = "tenthValue";
             graph10.bullet = "round";
//           graph10.bulletBorderThickness = 0;
//           graph10.bulletAlpha = 0.85;
//           graph10.bulletSize = 1;
             graph10.hideBulletsCount = 5; 
             graph10.type = "line";
             graph10.fillAlphas = 0;
             graph10.lineColor = chartOption.linecolor10;
             graph10.lineThickness = "1";
             graph10.balloonText="[[tenthName]] : [[value]]" + chartOption.axistitle;
             graph10.connect = false; //연결옵션 false
             graph10.gapPeriod = chartOption.interval;
             
             if(chartData[1].tenthName != null)
            	 graph10.visibleInLegend = true;
             else
            	 graph10.visibleInLegend = false;
             
             chart.addGraph(graph10);
             
             // CURSOR
             var chartCursor = new AmCharts.ChartCursor();
             chartCursor.cursorPosition = "middle";
             chartCursor.categoryBalloonDateFormat = "YYYY-MM-DD JJ:NN:SS";
             chartCursor.zoomable = true;
             chartCursor.cursorColor= chartOption.cursorColor;
             chart.addChartCursor(chartCursor);
             
             if (chartOption.maxPos != null && chartOption.maxPos > 0)
             {
            	 var guide = new AmCharts.Guide();
                 guide.value = chartOption.maxPos; 
                 guide.lineColor = "#CC0000";
                 //guide.dashLength = 5;
                 guide.label = "max : " + addCommaforNumber(chartOption.maxPos) + chartOption.axistitle;               
                 guide.inside = true;
                 guide.lineAlpha = 1;                 
                 valueAxis.addGuide(guide);
                 guide = null;
             }            

             // SCROLLBAR (상단의 확대 축소 바)
             var chartScrollbar = new AmCharts.ChartScrollbar();
             chart.addChartScrollbar(chartScrollbar);
             chartScrollbar.dragIconHeight =  20;
             chartScrollbar.dragIconWidth = 20;
             chartScrollbar.scrollbarHeight = 10;
             
             // Balloon             
             var balloon = chart.balloon;
             balloon.adjustBorderColor = true;
             balloon.pointerWidth = 3;
             balloon.shadowAlpha = 0; 
             balloon.borderThickness = 1;
             balloon.fillAlpha = 0.95;
             balloon.horizontalPadding = 6;
             balloon.verticalPadding = -2; 
             balloon.fontSize = 8;                 

             // LEGEND
             var legend = new AmCharts.AmLegend();
             legend.autoMargins = false;
             legend.align = "center";            
             legend.markerLabelGap = 4;           
             legend.position = "bottom";
             legend.valueWidth = 20;
             legend.verticalGap = 1;
//             legend.valueText = "[[value]]";
             legend.labelText = "[[title]]";
             legend.markerSize = 10;
             legend.fontSize = 9;
             chart.addLegend(legend);

             // WRITE
             chart.write(chartOption.chartname);          

         // this method is called when chart is first inited as we listen for "dataUpdated" event
	         function zoomChart()
	         {
	             // different zoom methods can be used - zoomToIndexes, zoomToDates, zoomToCategoryValues
	             chart.zoomToIndexes(10, 20);
	         }
	         return chart;
		}
	},
	
	// TODO
	OBDashboardGroupSumChartViewer: function(chartDatalist, chartOptionList)
	{
		with (this) 
		{
			 var chartData = chartDatalist;
			 var chartOption = chartOptionList;
			 
             // SERIAL CHART 옵션
             var chart = new AmCharts.AmSerialChart();
             chart.pathToImages = "/js/extern/amcharts_images/";             
             chart.dataProvider = chartData;
             chart.categoryField = "occurredTime";
             chart.gridAboveGraphs= "true"; 
             
             // 대상이 없을때 보여지는 챠트  양 사이드 border            
             chart.plotAreaBorderAlpha =  1;
             chart.plotAreaBorderColor =  '#e1e1e1'; 
             
             chart.autoMargins =  false;
             chart.columnsAboveLines = true;
             chart.marginTop = 5;
             chart.marginBottom = 19;
             chart.marginLeft = 50;
             chart.marginRight = 50;             
             
             chart.fontFamily = "Arial";
             chart.fontSize = 10;
             chart.zoomOutButton =
             {
	             backgroundColor: '#000000',
	             backgroundAlpha: 0.05,
	             fontSize : 9    
	             
             };
             chart.verticalPadding = 1,
             chart.zoomOutButtonImageSize = 14;
             chart.usePrefixes = true;
             chart.prefixesOfBigNumbers = [{number:1e+3,prefix:"k"},
                                           {number:1e+6,prefix:"M"},
                                           {number:1e+9,prefix:"G"}];
             // 처음 로드 화면에 zoom 기능을 사용하고 싶다면 ("dataUpdated", zoomChart); 로 add 후 조절은 zoomChart method 에서한다.
             //chart.addListener("dataUpdated", zoomChart);
             
             // Axes
             // X축 Category Axis (on the down)               
             var categoryAxis = chart.categoryAxis;
             categoryAxis.parseDates = true; // as our data is date-based, we set parseDates to true
             categoryAxis.minPeriod = "ss"; // our data is daily, so we set minPeriod to DD
             categoryAxis.dashLength = 2;
             categoryAxis.dateFormats = [{period:'ss',format:'JJ:NN:SS'},
                                         {period:'mm',format:'JJ:NN'},
                                         {period:'hh',format:'JJ:NN'},
                                         {period:'DD',format:'YYYY-MM-DD'},
                                         {period:'WW',format:'YYYY-MM-DD'},
                                         {period:'MM',format:'MM'},
                                         {period:'YYYY',format:'YYYY'}];
             categoryAxis.gridAlpha = 0.3;             

             // Y축 value axis (on the left)
             var valueAxis = new AmCharts.ValueAxis();
             valueAxis.stackType = "regular";
             valueAxis.tickLength = 0;
             valueAxis.dashLength = 2;
             valueAxis.gridAlpha = 0.3; 
             // valueAxis.axisColor = chartOption.linecolor;
             valueAxis.axisThickness = 1;
             valueAxis.usePrefixes = true;
             valueAxis.prefixesOfBigNumbers = [
                                                {number:1e+3,prefix:"k"},
                                                {number:1e+6,prefix:"M"},
                                                {number:1e+9,prefix:"G"}];
             valueAxis.minimum = chartOption.min;
             if (chartOption.max != null)
             {
            	 valueAxis.maximum = chartOption.max;             
             }
             valueAxis.title = chartOption.axistitle;
             valueAxis.titleBold = false;
             valueAxis.titleColor = "#666666";
             chart.addValueAxis(valueAxis);
             
             // Y축 value axis (on the right, hidden SUM using)
             var valueAxis2 = new AmCharts.ValueAxis();
             valueAxis2.stackType = "none";
             valueAxis2.tickLength = 0; 
             valueAxis2.axisThickness = 1;
             valueAxis2.usePrefixes = true;
             valueAxis2.position = "right"; 
             valueAxis2.gridAlpha = 0;
//             valueAxis2.fontSize = 0;                          
             valueAxis2.prefixesOfBigNumbers = [
                                                {number:1e+3,prefix:"k"},
                                                {number:1e+6,prefix:"M"},
                                                {number:1e+9,prefix:"G"}];
             valueAxis2.minimum = chartOption.min;
             if (chartOption.max != null)
             {
            	 valueAxis.maximum = chartOption.max;             
             }
             valueAxis2.title = chartOption.axistitle2;
             valueAxis2.titleBold = false;
             valueAxis2.titleColor = "#666666";
             chart.addValueAxis(valueAxis2);  
                 

             // GRAPHS

             //  graph One Value
             var graph11 = new AmCharts.AmGraph();
             graph11.valueAxis = valueAxis2; // we have to indicate which value axis should be used
             graph11.title = "SUM";
             graph11.valueField = "sumValue";
             graph11.bullet = "round";
             graph11.hideBulletsCount = 5; 
             graph11.type = "line";
             graph11.fillAlphas = 0.3;
             graph11.lineColor = chartOption.linecolor11;
             graph11.lineThickness = "0";
             graph11.balloonText="SUM : [[value]]" + chartOption.axistitle;
             graph11.visibleInLegend = false;
             graph11.connect = false; //연결옵션 false
             graph11.gapPeriod = chartOption.interval;
             chart.addGraph(graph11);                
             
             var graph1 = new AmCharts.AmGraph();
             graph1.valueAxis = valueAxis; // we have to indicate which value axis should be used
             graph1.title = chartData[1].firstName;
             graph1.valueField = "firstValue";
             graph1.bullet = "round";
             graph1.hideBulletsCount = 5; 
             graph1.type = "line";
             graph1.fillAlphas = 1;
             graph1.lineColor = chartOption.linecolor1;
             graph1.lineThickness = "0";
             graph1.balloonText="[[firstName]] : [[value]]" + chartOption.axistitle;
             graph1.connect = false;
             graph1.gapPeriod = chartOption.interval;
             chart.addGraph(graph1);  
             
             var graph2 = new AmCharts.AmGraph();
             graph2.valueAxis = valueAxis; // we have to indicate which value axis should be used
             graph2.title = chartData[1].secondName;
             graph2.valueField = "secondValue";
             graph2.bullet = "round";
             graph2.hideBulletsCount = 5; 
             graph2.type = "line";
             graph2.fillAlphas = 1;
             graph2.lineColor = chartOption.linecolor2;
             graph2.lineThickness = "0";
             graph2.balloonText="[[secondName]] : [[value]]" + chartOption.axistitle;
             if(chartData[1].secondName != null)
            	 graph2.visibleInLegend = true;
             else
            	 graph2.visibleInLegend = false;
             
             graph2.connect = false;
             graph2.gapPeriod = chartOption.interval;
             chart.addGraph(graph2); 
             
             //  graph One Value
             var graph3 = new AmCharts.AmGraph();
             graph3.valueAxis = valueAxis; // we have to indicate which value axis should be used
             graph3.title = chartData[1].thirdName;
             graph3.valueField = "thirdValue";
             graph3.bullet = "round";
             graph3.hideBulletsCount = 5; 
             graph3.type = "line";
             graph3.fillAlphas = 1;
             graph3.lineColor = chartOption.linecolor3;
             graph3.lineThickness = "0";
             graph3.balloonText="[[thirdName]] : [[value]]" + chartOption.axistitle;
             if(chartData[1].thirdName != null)
            	 graph3.visibleInLegend = true;
             else
            	 graph3.visibleInLegend = false;
             
             graph3.connect = false;
             graph3.gapPeriod = chartOption.interval;
             chart.addGraph(graph3);   
             
             //  graph One Value
             var graph4 = new AmCharts.AmGraph();
             graph4.valueAxis = valueAxis; // we have to indicate which value axis should be used
             graph4.title = chartData[1].fourthName;
             graph4.valueField = "fourthValue";
             graph4.bullet = "round";
             graph4.hideBulletsCount = 5; 
             graph4.type = "line";
             graph4.fillAlphas = 1;
             graph4.lineColor = chartOption.linecolor4;
             graph4.lineThickness = "0";
             graph4.balloonText="[[fourthName]] : [[value]]" + chartOption.axistitle;
             if(chartData[1].fourthName != null)
            	 graph4.visibleInLegend = true;
             else
            	 graph4.visibleInLegend = false;
             
             graph4.connect = false;
             graph4.gapPeriod = chartOption.interval;
             chart.addGraph(graph4);   
             
             //  graph One Value
             var graph5 = new AmCharts.AmGraph();
             graph5.valueAxis = valueAxis; // we have to indicate which value axis should be used
             graph5.title = chartData[1].fifthName;
             graph5.valueField = "fifthValue";
             graph5.bullet = "round";
             graph5.hideBulletsCount = 5; 
             graph5.type = "line";
             graph5.fillAlphas = 1;
             graph5.lineColor = chartOption.linecolor5;
             graph5.lineThickness = "0";
             graph5.balloonText="[[fifthName]] : [[value]]" + chartOption.axistitle;
             if(chartData[1].fifthName != null)
            	 graph5.visibleInLegend = true;
             else
            	 graph5.visibleInLegend = false;
             
             graph5.connect = false;
             graph5.gapPeriod = chartOption.interval;
             chart.addGraph(graph5);   
             
             //  graph One Value
             var graph6 = new AmCharts.AmGraph();
             graph6.valueAxis = valueAxis; // we have to indicate which value axis should be used
             graph6.title = chartData[1].sixthName;
             graph6.valueField = "sixthValue";
             graph6.bullet = "round";
             graph6.hideBulletsCount = 5; 
             graph6.type = "line";
             graph6.fillAlphas = 1;
             graph6.lineColor = chartOption.linecolor6;
             graph6.lineThickness = "0";
             graph6.balloonText="[[sixthName]] : [[value]]" + chartOption.axistitle;
             if(chartData[1].sixthName != null)
            	 graph6.visibleInLegend = true;
             else
            	 graph6.visibleInLegend = false;
             
             graph6.connect = false;
             graph6.gapPeriod = chartOption.interval;
             chart.addGraph(graph6);   
             
             //  graph One Value
             var graph7 = new AmCharts.AmGraph();
             graph7.valueAxis = valueAxis; // we have to indicate which value axis should be used
             graph7.title = chartData[1].seventhName;
             graph7.valueField = "seventhValue";
             graph7.bullet = "round";
             graph7.hideBulletsCount = 5; 
             graph7.type = "line";
             graph7.fillAlphas = 1;
             graph7.lineColor = chartOption.linecolor7;
             graph7.lineThickness = "0";
             graph7.balloonText="[[seventhName]] : [[value]]" + chartOption.axistitle;
             if(chartData[1].seventhName != null)
            	 graph7.visibleInLegend = true;
             else
            	 graph7.visibleInLegend = false;
             
             graph7.connect = false;
             graph7.gapPeriod = chartOption.interval;
             chart.addGraph(graph7);   
             
             //  graph One Value
             var graph8 = new AmCharts.AmGraph();
             graph8.valueAxis = valueAxis; // we have to indicate which value axis should be used
             graph8.title = chartData[1].eighthName;
             graph8.valueField = "eighthValue";
             graph8.bullet = "round";
             graph8.hideBulletsCount = 5; 
             graph8.type = "line";
             graph8.fillAlphas = 1;
             graph8.lineColor = chartOption.linecolor8;
             graph8.lineThickness = "0";
             graph8.balloonText="[[eighthName]] : [[value]]" + chartOption.axistitle;
             if(chartData[1].eighthName != null)
            	 graph8.visibleInLegend = true;
             else
            	 graph8.visibleInLegend = false;
             
             graph8.connect = false;
             graph8.gapPeriod = chartOption.interval;
             chart.addGraph(graph8);   
             
             //  graph One Value
             var graph9 = new AmCharts.AmGraph();
             graph9.valueAxis = valueAxis; // we have to indicate which value axis should be used
             graph9.title = chartData[1].ninthName;
             graph9.valueField = "ninthValue";
             graph9.bullet = "round";
             graph9.hideBulletsCount = 5; 
             graph9.type = "line";
             graph9.fillAlphas = 1;
             graph9.lineColor = chartOption.linecolor9;
             graph9.lineThickness = "0";
             graph9.balloonText="[[ninthName]] : [[value]]" + chartOption.axistitle;
             if(chartData[1].ninthName != null)
            	 graph9.visibleInLegend = true;
             else
            	 graph9.visibleInLegend = false;
             
             graph9.connect = false;
             graph9.gapPeriod = chartOption.interval;
             chart.addGraph(graph9);   
             
             //  graph One Value
             var graph10 = new AmCharts.AmGraph();
             graph10.valueAxis = valueAxis; // we have to indicate which value axis should be used
             graph10.title = chartData[1].tenthName;
             graph10.valueField = "tenthValue";
             graph10.bullet = "round";
             graph10.hideBulletsCount = 5; 
             graph10.type = "line";
             graph10.fillAlphas = 1;
             graph10.lineColor = chartOption.linecolor10;
             graph10.lineThickness = "0";
             graph10.balloonText="[[tenthName]] : [[value]]" + chartOption.axistitle;
             if(chartData[1].tenthName != null)
            	 graph10.visibleInLegend = true;
             else
            	 graph10.visibleInLegend = false;
             
             graph10.connect = false;
             graph10.gapPeriod = chartOption.interval;
             chart.addGraph(graph10);
             
             // CURSOR
             var chartCursor = new AmCharts.ChartCursor();
             chartCursor.cursorPosition = "middle";
             chartCursor.categoryBalloonDateFormat = "YYYY-MM-DD JJ:NN:SS";
             chartCursor.zoomable = true;
             chartCursor.cursorColor= chartOption.cursorColor;             
             chartCursor.bulletsEnabled = true;
             chart.addChartCursor(chartCursor);
             
             if (chartOption.maxPos != null && chartOption.maxPos > 0)
             {
            	 var guide = new AmCharts.Guide();
                 guide.value = chartOption.maxPos; 
                 guide.lineColor = "#CC0000";
                 //guide.dashLength = 5;
                 guide.label = "max : " + addCommaforNumber(chartOption.maxPos) + chartOption.axistitle;               
                 guide.inside = true;
                 guide.lineAlpha = 1;                 
                 valueAxis.addGuide(guide);
                 guide = null;
             }
             
             // SCROLLBAR (상단의 확대 축소 바)
             var chartScrollbar = new AmCharts.ChartScrollbar();
             chart.addChartScrollbar(chartScrollbar);
             chartScrollbar.dragIconHeight =  20;
             chartScrollbar.dragIconWidth = 20;
             chartScrollbar.scrollbarHeight = 10;            
             
             // Balloon             
             var balloon = chart.balloon;
             balloon.adjustBorderColor = true;
             balloon.pointerWidth = 3;
             balloon.shadowAlpha = 0; 
             balloon.borderThickness = 1;
             balloon.fillAlpha = 0.95;
             balloon.horizontalPadding = 6;
             balloon.verticalPadding = -2; 
             balloon.fontSize = 8;                  

             // LEGEND
             var legend = new AmCharts.AmLegend();
             legend.autoMargins = false;
             legend.align = "center";            
             legend.markerLabelGap = 4;           
             legend.position = "bottom";
             legend.valueWidth = 20;
             legend.verticalGap = 1;
//             legend.valueText = "[[value]]";
             legend.labelText = "[[title]]";
             legend.markerSize = 10;
             legend.fontSize = 9;
             chart.addLegend(legend);
             

             // WRITE
             chart.write(chartOption.chartname);          

         // this method is called when chart is first inited as we listen for "dataUpdated" event
	         function zoomChart()
	         {
	             // different zoom methods can be used - zoomToIndexes, zoomToDates, zoomToCategoryValues
	             chart.zoomToIndexes(10, 20);
	         }
	         return chart;
		}
	},
	/*
	 * 모듈명 : OBMultiLineChartViewer
	 * 성격 : 최대 17개 MultiValue Chart
	 * 매개변수 : chartDatalist (value, name), chartOptionList (min, max, chartname, axistitle, maxPos, cursorColor)
	 * 사용처 : 모니터링 -> 인터페이스, ADC장비 > Alteon Summary Chart
	 * */
	OBMultiLineChartViewer : function(chartDatalist, chartOptionObj)
	{
		with (this) 
		{          
			var chartData = chartDatalist;	
			var chartOption = chartOptionObj;
             // SERIAL CHART 옵션
             var chart = new AmCharts.AmSerialChart();
             chart.pathToImages = "js/extern/amcharts_images/";
             
             chart.dataProvider = chartData;
             chart.categoryField = "occurredTime";
             chart.gridAboveGraphs= "true"; 
             
             // 대상이 없을때 보여지는 챠트  양 사이드 border            
             chart.plotAreaBorderAlpha =  1;
             chart.plotAreaBorderColor =  '#e1e1e1'; 
             
             chart.autoMargins =  false;
             chart.columnsAboveLines = true;
             chart.marginTop = 5;
             chart.marginBottom = 19;
             chart.marginLeft = 50;
             chart.marginRight = 20;             
             
             chart.fontFamily = "Arial";
             chart.fontSize = 10;
             chart.zoomOutButton =
             {
	             backgroundColor: '#000000',
	             backgroundAlpha: 0.05,
	             fontSize : 9
             };
             chart.zoomOutButtonImageSize = 14;
             chart.usePrefixes = true;
             chart.prefixesOfBigNumbers = [{number:1e+3,prefix:"k"},
                                           {number:1e+6,prefix:"M"},
                                           {number:1e+9,prefix:"G"}];
             // 처음 로드 화면에 zoom 기능을 사용하고 싶다면 ("dataUpdated", zoomChart); 로 add 후 조절은 zoomChart 에서한다.
             //chart.addListener("dataUpdated", zoomChart);

             // AXES
             // category axis (on the down)               
             var categoryAxis = chart.categoryAxis;
             categoryAxis.parseDates = true; // as our data is date-based, we set parseDates to true
             categoryAxis.minPeriod = "ss"; // our data is daily, so we set minPeriod to DD
             categoryAxis.dashLength = 2;
             categoryAxis.dateFormats = [{period:'ss',format:'JJ:NN:SS'},
                                         {period:'mm',format:'JJ:NN'},
                                         {period:'hh',format:'JJ:NN'},
                                         {period:'DD',format:'YYYY-MM-DD'},
                                         {period:'WW',format:'YYYY-MM-DD'},
                                         {period:'MM',format:'MM'},
                                         {period:'YYYY',format:'YYYY'}];
             categoryAxis.gridAlpha = 0.3;             

             // value axis (on the left)
             var valueAxis1 = new AmCharts.ValueAxis();
             valueAxis1.stackType = "none";
             valueAxis1.tickLength = 0;
             valueAxis1.dashLength = 2;
             valueAxis1.gridAlpha = 0.3; 
             //valueAxis1.axisColor = "#6cdaba";
             valueAxis1.axisThickness = 1;
             valueAxis1.minimum = chartOption.min;
             if (chartOption.max != null)
             {
            	 valueAxis1.maximum = chartOption.max;
             }             
             valueAxis1.title = chartOption.axistitle;
             valueAxis1.titleBold = false;
             valueAxis1.titleColor = "#666666";
             valueAxis1.usePrefixes = true;
             valueAxis1.prefixesOfBigNumbers = [{number:1e+3,prefix:"k"},
                                                {number:1e+6,prefix:"M"},
                                                {number:1e+9,prefix:"G"}];
             chart.addValueAxis(valueAxis1);             

             // GRAPHS
             //  graph 1
             var graph1 = new AmCharts.AmGraph();
             graph1.valueAxis = valueAxis1; // we have to indicate which value axis should be used
             graph1.title = "red line";
             graph1.valueField = "firstValue";
             graph1.bullet = "round";
             graph1.hideBulletsCount = 5; 
             graph1.type = "line";
             graph1.lineColor = "#6cb8c8";
             graph1.lineThickness = "1";
             graph1.balloonText="[[firstName]]: [[value]]" + chartOption.axistitle;
             chart.addGraph(graph1);
             
             // graph 2               
             var graph2 = new AmCharts.AmGraph();
             graph2.valueAxis = valueAxis1; // we have to indicate which value axis should be used
             graph2.title = "yellow line";
             graph2.valueField = "secondValue";
             graph2.bullet = "round";
             graph2.hideBulletsCount = 5; 
             graph2.type = "line";
             graph2.lineColor = "#fbc51a";
             graph2.lineThickness = "1";
             graph2.balloonText="[[secondName]]: [[value]]" + chartOption.axistitle;
             chart.addGraph(graph2);

             // graph 3
             var graph3 = new AmCharts.AmGraph();
             graph3.valueAxis = valueAxis1; // we have to indicate which value axis should be used
             graph3.valueField = "thirdValue";
             graph3.title = "green line";
             graph3.bullet = "round";
             graph3.hideBulletsCount = 5; 
             graph3.type = "line";
             graph3.lineColor = "#d65f3d";
             graph3.lineThickness = "1";
             graph3.balloonText="[[thirdName]]: [[value]]" + chartOption.axistitle;
             chart.addGraph(graph3);
             
             // graph 4
             var graph4 = new AmCharts.AmGraph();
             graph4.valueAxis = valueAxis1; // we have to indicate which value axis should be used
             graph4.valueField = "fourthValue";
             graph4.title = "green line";
             graph4.bullet = "round";
             graph4.hideBulletsCount = 5; 
             graph4.type = "line";
             graph4.lineColor = "#976e96";
             graph4.lineThickness = "1";
             graph4.balloonText="[[fourthName]]: [[value]]" + chartOption.axistitle;
             chart.addGraph(graph4);
             
             // graph 5
             var graph5 = new AmCharts.AmGraph();
             graph5.valueAxis = valueAxis1; // we have to indicate which value axis should be used
             graph5.valueField = "fifthValue";
             graph5.title = "green line";
             graph5.bullet = "round";
             graph5.hideBulletsCount = 5; 
             graph5.type = "line";
             graph5.lineColor = "#fb8e33";
             graph5.lineThickness = "1";
             graph5.balloonText="[[fifthName]]: [[value]]" + chartOption.axistitle;
             chart.addGraph(graph5);
             
          // graph 6
             var graph6 = new AmCharts.AmGraph();
             graph6.valueAxis = valueAxis1; // we have to indicate which value axis should be used
             graph6.valueField = "sixthValue";
             graph6.title = "green line";
             graph6.bullet = "round";
             graph6.hideBulletsCount = 5; 
             graph6.type = "line";
             graph6.lineColor = "#9cc239";
             graph6.lineThickness = "1";
             graph6.balloonText="[[sixthName]]: [[value]]" + chartOption.axistitle;
             chart.addGraph(graph6);
             
          // graph 7
             var graph7 = new AmCharts.AmGraph();
             graph7.valueAxis = valueAxis1; // we have to indicate which value axis should be used
             graph7.valueField = "seventhValue";
             graph7.title = "green line";
             graph7.bullet = "round";
             graph7.hideBulletsCount = 5; 
             graph7.type = "line";
             graph7.lineColor = "#998c57";
             graph7.lineThickness = "1";
             graph7.balloonText="[[seventhName]]: [[value]]" + chartOption.axistitle;
             chart.addGraph(graph7);
             
          // graph 8
             var graph8 = new AmCharts.AmGraph();
             graph8.valueAxis = valueAxis1; // we have to indicate which value axis should be used
             graph8.valueField = "eighthValue";
             graph8.title = "green line";
             graph8.bullet = "round";
             graph8.hideBulletsCount = 5; 
             graph8.type = "line";
             graph8.lineColor = "#d987ad";
             graph8.lineThickness = "1";
             graph8.balloonText="[[eighthName]]: [[value]]" + chartOption.axistitle;
             chart.addGraph(graph8);
             
          // graph 9
             var graph9 = new AmCharts.AmGraph();
             graph9.valueAxis = valueAxis1; // we have to indicate which value axis should be used
             graph9.valueField = "ninthValue";
             graph9.title = "green line";
             graph9.bullet = "round";
             graph9.hideBulletsCount = 5; 
             graph9.type = "line";
             graph9.lineColor = "#557aa4";
             graph9.lineThickness = "1";
             graph9.balloonText="[[ninthName]]: [[value]]" + chartOption.axistitle;
             chart.addGraph(graph9);
             
          // graph 10
             var graph10 = new AmCharts.AmGraph();
             graph10.valueAxis = valueAxis1; // we have to indicate which value axis should be used
             graph10.valueField = "tenthValue";
             graph10.title = "green line";
             graph10.bullet = "round";
             graph10.hideBulletsCount = 5; 
             graph10.type = "line";
             graph10.lineColor = "#d8aa3a";
             graph10.lineThickness = "1";
             graph10.balloonText="[[tenthName]]: [[value]]" + chartOption.axistitle;
             chart.addGraph(graph10);
             
          // graph 11
             var graph11 = new AmCharts.AmGraph();
             graph11.valueAxis = valueAxis1; // we have to indicate which value axis should be used
             graph11.valueField = "eleventhValue";
             graph11.title = "green line";
             graph11.bullet = "round";
             graph11.hideBulletsCount = 5; 
             graph11.type = "line";
             graph11.lineColor = "#ff8ba2";
             graph11.lineThickness = "1";
             graph11.balloonText="[[eleventhName]]: [[value]]" + chartOption.axistitle;
             chart.addGraph(graph11);
             
          // graph 12
             var graph12 = new AmCharts.AmGraph();
             graph12.valueAxis = valueAxis1; // we have to indicate which value axis should be used
             graph12.valueField = "twelfthValue";
             graph12.title = "green line";
             graph12.bullet = "round";
             graph12.hideBulletsCount = 5; 
             graph12.type = "line";
             graph12.lineColor = "#ff8ba2";
             graph12.lineThickness = "1";
             graph12.balloonText="[[twelfthName]]: [[value]]" + chartOption.axistitle;
             chart.addGraph(graph12);
             
          // graph 13
             var graph13 = new AmCharts.AmGraph();
             graph13.valueAxis = valueAxis1; // we have to indicate which value axis should be used
             graph13.valueField = "thirteenthValue";
             graph13.title = "green line";
             graph13.bullet = "round";
             graph13.hideBulletsCount = 5; 
             graph13.type = "line";
             graph13.lineColor = "#00cfbb";
             graph13.lineThickness = "1";
             graph13.balloonText="[[thirteenthName]]: [[value]]" + chartOption.axistitle;
             chart.addGraph(graph13);
             
          // graph 14
             var graph14 = new AmCharts.AmGraph();
             graph14.valueAxis = valueAxis1; // we have to indicate which value axis should be used
             graph14.valueField = "fourteenthValue";
             graph14.title = "green line";
             graph14.bullet = "round";
             graph14.hideBulletsCount = 5; 
             graph14.type = "line";
             graph14.lineColor = "#bfb9da";
             graph14.lineThickness = "1";
             graph14.balloonText="[[fourteenthName]]: [[value]]" + chartOption.axistitle;
             chart.addGraph(graph14);
             
          // graph 15
             var graph15 = new AmCharts.AmGraph();
             graph15.valueAxis = valueAxis1; // we have to indicate which value axis should be used
             graph15.valueField = "fifteenthValue";
             graph15.title = "green line";
             graph15.bullet = "round";
             graph15.hideBulletsCount = 5; 
             graph15.type = "line";
             graph15.lineColor = "#e26cb4";
             graph15.lineThickness = "1";
             graph15.balloonText="[[fifteenthName]]: [[value]]" + chartOption.axistitle;
             chart.addGraph(graph15);
             
          // graph 16
             var graph16 = new AmCharts.AmGraph();
             graph16.valueAxis = valueAxis1; // we have to indicate which value axis should be used
             graph16.valueField = "sixteenthValue";
             graph16.title = "green line";
             graph16.bullet = "round";
             graph16.hideBulletsCount = 5; 
             graph16.type = "line";
             graph16.lineColor = "#ffb970";
             graph16.lineThickness = "1";
             graph16.balloonText="[[sixteenthName]]: [[value]]" + chartOption.axistitle;
             chart.addGraph(graph16);
             
          // graph 17
             var graph17 = new AmCharts.AmGraph();
             graph17.valueAxis = valueAxis1; // we have to indicate which value axis should be used
             graph17.valueField = "seventeenthValue";
             graph17.title = "green line";
             graph17.bullet = "round";
             graph17.hideBulletsCount = 5; 
             graph17.type = "line";
             graph17.lineColor = "#28c6d1";
             graph17.lineThickness = "1";
             graph17.balloonText="[[seventeenthName]]: [[value]]" + chartOption.axistitle;
             chart.addGraph(graph17);

             // CURSOR
             var chartCursor = new AmCharts.ChartCursor();
             chartCursor.cursorPosition = "middle";
             chartCursor.categoryBalloonDateFormat = "YYYY-MM-DD JJ:NN:SS";
             chartCursor.zoomable = true;
             chartCursor.cursorColor= chartOption.cursorColor;
             chart.addChartCursor(chartCursor);

          // Balloon             
             var balloon = chart.balloon;
             balloon.adjustBorderColor = true;
             balloon.pointerWidth = 3;
             balloon.shadowAlpha = 0; 
             balloon.borderThickness = 1;
             balloon.fillAlpha = 0.95;
             balloon.horizontalPadding = 6;
             balloon.verticalPadding = -2; 
             balloon.fontSize = 8;             

             // SCROLLBAR (상단의 확대 축소 바)
             var chartScrollbar = new AmCharts.ChartScrollbar();
             chart.addChartScrollbar(chartScrollbar);
             chartScrollbar.dragIconHeight =  20;
             chartScrollbar.dragIconWidth = 20;
             chartScrollbar.scrollbarHeight = 10;

             // LEGEND
            /* var legend = new AmCharts.AmLegend();
             legend.marginLeft = 110;
             chart.addLegend(legend);*/

             // WRITE
             chart.write(chartOption.chartname);          

         // this method is called when chart is first inited as we listen for "dataUpdated" event
	         function zoomChart()
	         {
	             // different zoom methods can be used - zoomToIndexes, zoomToDates, zoomToCategoryValues
	             chart.zoomToIndexes(10, 20);
	         }
		}		
	},	
	/*
	 * 모듈명 : OBSvcPerfomanceBpsConnChartViewer
	 * 성격 : 최대 2개 Multi Line chart , legend 존재
	 * 매개변수 : chartDataObj, chartOptionObj ( min, max, linecolor1, linecolor2, chartname, axistitle1, axistitle2, maxPos, cursorColor)
	 * 사용처 : 1. 모니터링 > FLB bps , Connection 2 Value Chart
	 * 사용처 : 2. 모니터링 > FLB Member 성능 모니터링 > bps , Connection 2 Value Chart
	  */
	OBSvcPerfomanceBpsConnChartViewer : function(chartDataObj, chartOptionObj)
	{
		with (this) 
		{
			 var chartData = chartDataObj;
			 var chartOption = chartOptionObj;
             // SERIAL CHART 옵션
             var chart = new AmCharts.AmSerialChart();
             chart.pathToImages = "/js/extern/amcharts_images/";             
             chart.dataProvider = chartData;
             chart.categoryField = "occurredTime";
             chart.gridAboveGraphs= "true";
             
             // 대상이 없을때 보여지는 챠트  양 사이드 border            
             chart.plotAreaBorderAlpha =  1;
             chart.plotAreaBorderColor =  '#e1e1e1'; 
             
             chart.autoMargins =  false;
             chart.columnsAboveLines = true;
             chart.marginTop = 5;
             chart.marginBottom = 19;
             chart.marginLeft = 50;
             chart.marginRight = 20;             
             
             chart.fontFamily = "Arial";
             chart.fontSize = 10;
             chart.zoomOutButton =
             {
	             backgroundColor: '#000000',
	             backgroundAlpha: 0.05,
	             fontSize : 9
             };
             chart.zoomOutButtonImageSize = 14;
             chart.usePrefixes = true;
             chart.prefixesOfBigNumbers = [{number:1e+3,prefix:"k"},
                                           {number:1e+6,prefix:"M"},
                                           {number:1e+9,prefix:"G"}];
             // 처음 로드 화면에 zoom 기능을 사용하고 싶다면 ("dataUpdated", zoomChart); 로 add 후 조절은 zoomChart method 에서한다.
             //chart.addListener("dataUpdated", zoomChart);

             // Axes
             // X축 Category Axis (on the down)               
             var categoryAxis = chart.categoryAxis;
             categoryAxis.parseDates = true; // as our data is date-based, we set parseDates to true
             categoryAxis.minPeriod = "ss"; // our data is daily, so we set minPeriod to DD
             categoryAxis.dashLength = 2;
             categoryAxis.dateFormats = [{period:'ss',format:'JJ:NN:SS'},
                                         {period:'mm',format:'JJ:NN'},
                                         {period:'hh',format:'JJ:NN'},
                                         {period:'DD',format:'YYYY-MM-DD'},
                                         {period:'WW',format:'YYYY-MM-DD'},
                                         {period:'MM',format:'MM'},
                                         {period:'YYYY',format:'YYYY'}];
             categoryAxis.gridAlpha = 0.3;             

             // Y축 value axis (on the left)
             var valueAxis1 = new AmCharts.ValueAxis();
             valueAxis1.stackType = "none";
             valueAxis1.tickLength = 0;
             valueAxis1.dashLength = 2;
             valueAxis1.gridAlpha = 0.3;
           //  valueAxis1.axisColor = chartOption.linecolor1;
             valueAxis1.axisThickness = 1;
             valueAxis1.usePrefixes = true;
             valueAxis1.prefixesOfBigNumbers = [
                                                {number:1e+3,prefix:"k"},
                                                {number:1e+6,prefix:"M"},
                                                {number:1e+9,prefix:"G"}];
             valueAxis1.minimum = chartOption.min;
             if (chartOption.max != null)
             {
            	 valueAxis1.maximum = chartOption.max;             
             }             
             valueAxis1.title = chartOption.axistitle1;
             valueAxis1.titleBold = false;
             valueAxis1.titleColor = "#666666";
             chart.addValueAxis(valueAxis1);
             
             
          // Y축 value axis2 (on the right)
             var valueAxis2 = new AmCharts.ValueAxis();
             valueAxis2.stackType = "none";
             valueAxis2.tickLength = 0;
           //  valueAxis2.axisColor = chartOption.linecolor2;
             valueAxis2.axisThickness = 1;
             valueAxis2.usePrefixes = true;
             valueAxis2.position = "right";
             valueAxis2.gridAlpha = 0;
             valueAxis2.prefixesOfBigNumbers = [
                                                {number:1e+3,prefix:"k"},
                                                {number:1e+6,prefix:"M"},
                                                {number:1e+9,prefix:"G"}];
             valueAxis2.minimum = chartOption.min;
             if (chartOption.max != null)
             {
            	 valueAxis2.maximum = chartOption.max;             
             }             
             valueAxis2.title = chartOption.axistitle2;
             valueAxis2.titleBold = false;
             valueAxis2.titleColor = "#666666";
             chart.addValueAxis(valueAxis2); 

             // GRAPHS
             //  graph 1
             var graph1 = new AmCharts.AmGraph();
             graph1.valueAxis = valueAxis1; // we have to indicate which value axis should be used
             graph1.title = "bps";
             graph1.valueField = "firstValue";
             graph1.bullet = "round";
             graph1.hideBulletsCount = 5; 
             graph1.type = "line";
             //graph1.fillAlphas = 0.4;
             graph1.lineColor = chartOption.linecolor1;
             graph1.lineThickness = "1";
             graph1.balloonText="[[value]]" + chartOption.axistitle1;
             graph1.connect = false;
             graph1.gapPeriod = chartOption.interval;
             chart.addGraph(graph1);
             
         	 //  graph 2
             var graph2 = new AmCharts.AmGraph();
             graph2.valueAxis = valueAxis2; // we have to indicate which value axis should be used
             graph2.title = "Concurrent Sessions";
             graph2.valueField = "secondValue";
             graph2.bullet = "round";
             graph2.hideBulletsCount = 5; 
             graph2.type = "line";
             //graph2.fillAlphas = 0.4;
             graph2.lineColor = chartOption.linecolor2;
             graph2.lineThickness = "1";
             graph2.balloonText="Concurrent Sessions: [[value]]" + chartOption.axistitle2;
             graph2.connect = false;
             graph2.gapPeriod = chartOption.interval;
             chart.addGraph(graph2);

             // CURSOR
             var chartCursor = new AmCharts.ChartCursor();
             chartCursor.cursorPosition = "middle";
             chartCursor.categoryBalloonDateFormat = "YYYY-MM-DD JJ:NN:SS";
             chartCursor.zoomable = true;
             chartCursor.cursorColor= chartOption.cursorColor;
             chart.addChartCursor(chartCursor);
             
             /*if (chartOption.maxPos != null && chartOption.maxPos > 0)
             {
            	 var guide = new AmCharts.Guide();
                 guide.value = chartOption.maxPos; 
                 guide.lineColor = "#CC0000";
                 //guide.dashLength = 5;
                 guide.label = "max : " + addCommaforNumber(chartOption.maxPos) ;               
                 guide.inside = true;
                 guide.lineAlpha = 1;                 
                 valueAxis1.addGuide(guide);
                 guide = null;
             }*/
             
            /*if (ConfigDate != null && ConfigDate.length > 0)
            {
            	 for ( var i = 0; i < ConfigDate.length; i++)
            		 {
			             var guide2 = new AmCharts.Guide();
			             guide2.date = ConfigDate[i]; 
			             guide2.lineColor = "#0d67be";
			             //guide2.dashLength = 4;
			             guide2.label = "config";            
			             guide2.inside = true;
			             guide2.lineAlpha = 1;
			             guide2.labelRotation = "90";
			             guide2.fontSize = "8";
			             categoryAxis.addGuide(guide2);
			             guide2 = null;
            		 }
            }*/
            // SCROLLBAR (상단의 확대 축소 바)
            /* var chartScrollbar = new AmCharts.ChartScrollbar();
             chart.addChartScrollbar(chartScrollbar);*/
             
             // Balloon             
 
             
             // LEGEND
            var legend = new AmCharts.AmLegend();
            legend.align = "center";            
            legend.markerLabelGap = 5;           
            legend.position = "bottom";
            legend.valueWidth = 20;
            legend.verticalGap = 1;
            legend.valueText = "[[value]]";
            legend.markerSize = 11;
            chart.addLegend(legend);

             // WRITE
             chart.write(chartOption.chartname);          

         // this method is called when chart is first inited as we listen for "dataUpdated" event
	         function zoomChart()
	         {
	             // different zoom methods can be used - zoomToIndexes, zoomToDates, zoomToCategoryValues
	             chart.zoomToIndexes(10, 20);
	         }             
		}
	},
	/*
	 * 모듈명 : OBSvcPerfomanceBpsConnChartViewerExtends
	 * 성격 : 최대 4개 Multi Line chart , legend 존재
	 * 매개변수 : chartDataObj, chartOptionObj ( min, max, linecolor1, linecolor2, chartname, axistitle1, axistitle2, maxPos, cursorColor)
	 * 사용처 : 1. 모니터링 > 서비스 성능 bps , Connection 2 Value Chart
	 * 사용처 : 2. 모니터링 > 서비스 Member 성능 모니터링 > bps , Connection 2 Value Chart
	  */
	OBSvcPerfomanceBpsConnChartViewerExtends : function(chartDataObj, chartOptionObj)
	{
		with (this) 
		{
			 var chartData = chartDataObj;
			 var chartOption = chartOptionObj;
             // SERIAL CHART 옵션
             var chart = new AmCharts.AmSerialChart();
             chart.pathToImages = "/js/extern/amcharts_images/";             
             chart.dataProvider = chartData;
             chart.categoryField = "occurredTime";
             chart.gridAboveGraphs= "true";  

             // 대상이 없을때 보여지는 챠트  양 사이드 border            
             chart.plotAreaBorderAlpha =  1;
             chart.plotAreaBorderColor =  '#e1e1e1'; 
             
             chart.autoMargins =  false;
             chart.columnsAboveLines = true;
             chart.marginTop = 5;
             chart.marginBottom = 19;
             chart.marginLeft = 50;
             chart.marginRight = 20;             
             
             chart.fontFamily = "Arial";
             chart.fontSize = 10;
             chart.zoomOutButton =
             {
	             backgroundColor: '#000000',
	             backgroundAlpha: 0.05,
	             fontSize : 9
             };
             chart.zoomOutButtonImageSize = 14;
             chart.usePrefixes = true;
             chart.prefixesOfBigNumbers = [{number:1e+3,prefix:"k"},
                                           {number:1e+6,prefix:"M"},
                                           {number:1e+9,prefix:"G"}];
             // 처음 로드 화면에 zoom 기능을 사용하고 싶다면 ("dataUpdated", zoomChart); 로 add 후 조절은 zoomChart method 에서한다.
             //chart.addListener("dataUpdated", zoomChart);

             // Axes
             // X축 Category Axis (on the down)               
             var categoryAxis = chart.categoryAxis;
             categoryAxis.parseDates = true; // as our data is date-based, we set parseDates to true
             categoryAxis.minPeriod = "ss"; // our data is daily, so we set minPeriod to DD
             categoryAxis.dashLength = 2;
             categoryAxis.dateFormats = [{period:'ss',format:'JJ:NN:SS'},
                                         {period:'mm',format:'JJ:NN'},
                                         {period:'hh',format:'JJ:NN'},
                                         {period:'DD',format:'YYYY-MM-DD'},
                                         {period:'WW',format:'YYYY-MM-DD'},
                                         {period:'MM',format:'MM'},
                                         {period:'YYYY',format:'YYYY'}];
             categoryAxis.gridAlpha = 0.3;             

             // Y축 value axis (on the left)
             var valueAxis1 = new AmCharts.ValueAxis();
             valueAxis1.stackType = "none";
             valueAxis1.tickLength = 0;
             valueAxis1.dashLength = 2;
             valueAxis1.gridAlpha = 0.3;
          //   valueAxis1.axisColor = chartOption.linecolor3;
             valueAxis1.axisThickness = 1;
             valueAxis1.usePrefixes = true;
             valueAxis1.prefixesOfBigNumbers = [
                                                {number:1e+3,prefix:"k"},
                                                {number:1e+6,prefix:"M"},
                                                {number:1e+9,prefix:"G"}];
             valueAxis1.minimum = chartOption.min;
             if (chartOption.max != null)
             {
            	 valueAxis1.maximum = chartOption.max;             
             }             
             valueAxis1.title = chartOption.axistitle1;
             valueAxis1.titleBold = false;
             valueAxis1.titleColor = "#666666";
             chart.addValueAxis(valueAxis1);
             
             
          // Y축 value axis2 (on the right)
             var valueAxis2 = new AmCharts.ValueAxis();
             valueAxis2.stackType = "none";
             valueAxis2.tickLength = 0;
          //   valueAxis2.axisColor = chartOption.linecolor4;
             valueAxis2.axisThickness = 1;
             valueAxis2.usePrefixes = true;
             valueAxis2.position = "right";
             valueAxis2.gridAlpha = 0;
             valueAxis2.prefixesOfBigNumbers = [
                                                {number:1e+3,prefix:"k"},
                                                {number:1e+6,prefix:"M"},
                                                {number:1e+9,prefix:"G"}];
             valueAxis2.minimum = chartOption.min;
             if (chartOption.max != null)
             {
            	 valueAxis2.maximum = chartOption.max;             
             }             
             valueAxis2.title = chartOption.axistitle2;
             valueAxis2.titleBold = false;
             valueAxis2.titleColor = "#666666";
             chart.addValueAxis(valueAxis2); 

             // GRAPHS
             //  graph 1
             var graph1 = new AmCharts.AmGraph();
             graph1.valueAxis = valueAxis1; // we have to indicate which value axis should be used
             graph1.title = "bps In";
             graph1.valueField = "firstValue";
             graph1.bullet = "round";
             graph1.hideBulletsCount = 5; 
             graph1.type = "line";
             //graph1.fillAlphas = 0.4;
             graph1.lineColor = chartOption.linecolor1;
             graph1.lineThickness = "1";
             graph1.balloonText="bps In: [[value]]" + chartOption.axistitle1;
             graph1.hidden = true;
             chart.addGraph(graph1);
             
         //  graph 2
             var graph2 = new AmCharts.AmGraph();
             graph2.valueAxis = valueAxis1; // we have to indicate which value axis should be used
             graph2.title = "bps Out";
             graph2.valueField = "secondValue";
             graph2.bullet = "round";
             graph2.hideBulletsCount = 5; 
             graph2.type = "line";
             //graph2.fillAlphas = 0.4;
             graph2.lineColor = chartOption.linecolor2;
             graph2.lineThickness = "1";
             graph2.balloonText="bps Out: [[value]]" + chartOption.axistitle1;
             graph2.hidden = true;
             chart.addGraph(graph2);
             
             //  graph 3
             var graph3 = new AmCharts.AmGraph();
             graph3.valueAxis = valueAxis1; // we have to indicate which value axis should be used
             graph3.title = "bps Total";
             graph3.valueField = "thirdValue";
             graph3.bullet = "round";
             graph3.hideBulletsCount = 5; 
             graph3.type = "line";
             //graph3.fillAlphas = 0.4;
             graph3.lineColor = chartOption.linecolor3;
             graph3.lineThickness = "1";
             graph3.balloonText="bps Total: [[value]]" + chartOption.axistitle1;
             chart.addGraph(graph3);
             
             //  graph 4
             var graph4 = new AmCharts.AmGraph();
             graph4.valueAxis = valueAxis2; // we have to indicate which value axis should be used
             graph4.title = "Concurrent Sessions";
             graph4.valueField = "fourthValue";
             graph4.bullet = "round";
             graph4.hideBulletsCount = 5; 
             graph4.type = "line";
             //graph2.fillAlphas = 0.4;
             graph4.lineColor = chartOption.linecolor4;
             graph4.lineThickness = "1";
             graph4.balloonText="Concurrent Sessions: [[value]]" + chartOption.axistitle2;
             chart.addGraph(graph4);

             // CURSOR
             var chartCursor = new AmCharts.ChartCursor();
             chartCursor.cursorPosition = "middle";
             chartCursor.categoryBalloonDateFormat = "YYYY-MM-DD JJ:NN:SS";
             chartCursor.zoomable = true;
             chartCursor.cursorColor= chartOption.cursorColor;
             chart.addChartCursor(chartCursor);
             
             /*if (chartOption.maxPos != null && chartOption.maxPos > 0)
             {
            	 var guide = new AmCharts.Guide();
                 guide.value = chartOption.maxPos; 
                 guide.lineColor = "#CC0000";
                 //guide.dashLength = 5;
                 guide.label = "max : " + addCommaforNumber(chartOption.maxPos) ;               
                 guide.inside = true;
                 guide.lineAlpha = 1;                 
                 valueAxis1.addGuide(guide);
                 guide = null;
             }*/
             
            /*if (ConfigDate != null && ConfigDate.length > 0)
            {
            	 for ( var i = 0; i < ConfigDate.length; i++)
            		 {
			             var guide2 = new AmCharts.Guide();
			             guide2.date = ConfigDate[i]; 
			             guide2.lineColor = "#0d67be";
			             //guide2.dashLength = 4;
			             guide2.label = "config";            
			             guide2.inside = true;
			             guide2.lineAlpha = 1;
			             guide2.labelRotation = "90";
			             guide2.fontSize = "8";
			             categoryAxis.addGuide(guide2);
			             guide2 = null;
            		 }
            }*/
                               
             // Balloon             

             
             // LEGEND
            var legend = new AmCharts.AmLegend();
            legend.align = "center";            
            legend.markerLabelGap = 5;           
            legend.position = "bottom";
            legend.valueWidth = 20;
            legend.verticalGap = 1;
            legend.valueText = "[[value]]";
            legend.markerSize = 11;
            chart.addLegend(legend);

             // WRITE
             chart.write(chartOption.chartname);          

         // this method is called when chart is first inited as we listen for "dataUpdated" event
	         function zoomChart()
	         {
	             // different zoom methods can be used - zoomToIndexes, zoomToDates, zoomToCategoryValues
	             chart.zoomToIndexes(10, 20);
	         }             
		}
	},
	
	OBSvcPerfomanceBpsChartViewerExtends : function(chartDataObj, chartOptionObj) //서비스 모니터링  bps
	{
		with (this) 
		{
			 var chartData = chartDataObj;
			 var chartOption = chartOptionObj;
             // SERIAL CHART 옵션
             var chart = new AmCharts.AmSerialChart();
             chart.pathToImages = "/js/extern/amcharts_images/";             
             chart.dataProvider = chartData;
             chart.categoryField = "occurredTime";
             chart.gridAboveGraphs = "true"; 
             
             // 대상이 없을때 보여지는 챠트  양 사이드 border            
             chart.plotAreaBorderAlpha =  1;
             chart.plotAreaBorderColor =  '#e1e1e1';           
             
             chart.autoMargins =  false;
             //chart.columnsAboveLines = true;
             chart.marginTop = 5;
             chart.marginBottom = 19;
             chart.marginLeft = 50;
             chart.marginRight = 20;             
             
             chart.fontFamily = "Arial";
             chart.fontSize = 10;
             chart.zoomOutButton =
             {
	             backgroundColor: '#000000',
	             backgroundAlpha: 0.05,
	             fontSize : 9
             };
             chart.zoomOutButtonImageSize = 14;
             chart.usePrefixes = true;
             chart.prefixesOfBigNumbers = [{number:1e+3,prefix:"k"},
                                           {number:1e+6,prefix:"M"},
                                           {number:1e+9,prefix:"G"}];
             // 처음 로드 화면에 zoom 기능을 사용하고 싶다면 ("dataUpdated", zoomChart); 로 add 후 조절은 zoomChart method 에서한다.
             //chart.addListener("dataUpdated", zoomChart);

             // SCROLLBAR (상단의 확대 축소 바)
             var chartScrollbar = new AmCharts.ChartScrollbar();
             chart.addChartScrollbar(chartScrollbar);
             chartScrollbar.dragIconHeight =  20;
             chartScrollbar.dragIconWidth = 20;
             chartScrollbar.scrollbarHeight = 10;
             
             // Balloon             
             var balloon = chart.balloon;
             balloon.adjustBorderColor = true;
             balloon.pointerWidth = 3;
             balloon.shadowAlpha = 0; 
             balloon.borderThickness = 1;
             balloon.fillAlpha = 0.95;
             balloon.horizontalPadding = 6;
             balloon.verticalPadding = -2; 
             balloon.fontSize = 8;  
             
             // LEGEND
             var legend = new AmCharts.AmLegend();
             legend.autoMargins = false;
             legend.marginBottom =  -5;
             legend.marginTop =  -1; 
//             legend.backgroundColor ="#ff0000";
//             legend.backgroundAlpha ="0.9";
             legend.align = "center";            
             legend.markerLabelGap = 4;           
             legend.position = "bottom";
             legend.valueWidth = 50;
             legend.verticalGap = 1;
//             legend.valueText = "[[value]]";
             legend.labelText = "[[title]]";
             legend.markerSize = 10;
             legend.fontSize = 9;
             chart.addLegend(legend); 
             

             // Axes
             // X축 Category Axis (on the down)               
             var categoryAxis = chart.categoryAxis;             
             categoryAxis.parseDates = true; // as our data is date-based, we set parseDates to true
             categoryAxis.minPeriod = "ss"; // our data is daily, so we set minPeriod to DD
             categoryAxis.dashLength = 2;
             categoryAxis.dateFormats = [{period:'ss',format:'JJ:NN:SS'},
                                         {period:'mm',format:'JJ:NN'},
                                         {period:'hh',format:'JJ:NN'},
                                         {period:'DD',format:'YYYY-MM-DD'},
                                         {period:'WW',format:'YYYY-MM-DD'},
                                         {period:'MM',format:'MM'},
                                         {period:'YYYY',format:'YYYY'}];
             categoryAxis.gridAlpha = 0.3;             

             // Y축 value axis (on the left)
             var valueAxis1 = new AmCharts.ValueAxis();
             valueAxis1.stackType = "none";
             valueAxis1.tickLength = 0;
             valueAxis1.dashLength = 2;
             valueAxis1.gridAlpha = 0.3;
            // valueAxis1.axisColor = chartOption.linecolor1;
             valueAxis1.axisThickness = 1;
             valueAxis1.usePrefixes = true;
             valueAxis1.prefixesOfBigNumbers = [
                                                {number:1e+3,prefix:"k"},
                                                {number:1e+6,prefix:"M"},
                                                {number:1e+9,prefix:"G"}];
             valueAxis1.minimum = chartOption.min;
             if (chartOption.max != null)
             {
            	 valueAxis1.maximum = chartOption.max;             
             }             
             valueAxis1.title = chartOption.axistitle1;
             valueAxis1.titleBold = false;
             valueAxis1.titleColor = "#666666";
             chart.addValueAxis(valueAxis1);

             // GRAPHS
             //  graph 1
             var graph1 = new AmCharts.AmGraph();
             graph1.valueAxis = valueAxis1; // we have to indicate which value axis should be used
             graph1.valueField = "firstValue";
            // graph1.bullet = "round";
             //graph1.hideBulletsCount = 5; 
             graph1.type = "line";
             graph1.lineThickness = "1";
             graph1.bulletSize = 0.01;
             
             if(chartOption.adcStatus != "available")
             {
            	 graph1.gapPeriod = chartOption.interval;
            	 graph1.connect = false;
             }             
             
             if(chartOption.bpsValue == 0)
             {
            	 graph1.title = "bps In";
            	 graph1.lineColor = chartOption.linecolor1;
            	 graph1.balloonText="bps In: [[value]]" + chartOption.axistitle1;            	 
             }
             else if(chartOption.bpsValue == 1)
             {
            	 graph1.title = "bps Out";
            	 graph1.lineColor = chartOption.linecolor1;
            	 graph1.balloonText="bps Out: [[value]]" + chartOption.axistitle1;                 
             }
             else
             {
            	 graph1.title = "bps Total"; 
            	 graph1.lineColor = chartOption.linecolor1;
            	 graph1.balloonText="bps Total: [[value]]" + chartOption.axistitle1;             	 
             }
//             graph1.balloonText="bps In: [[value]]" + chartOption.axistitle1;
            
             
            // graph1.gapPeriod = chartOption.interval;
             
             chart.addGraph(graph1);
             
             
             //  graph 2
             var graph2 = new AmCharts.AmGraph();
             graph2.valueAxis = valueAxis1; // we have to indicate which value axis should be used
//             graph2.title = "bps Out";
             graph2.valueField = "secondValue";
             graph2.bullet = "round";
             graph2.hideBulletsCount = 5; 
//             graph2.type = "line";
             graph2.fillAlphas = 0.3;
//             graph2.lineColor = chartOption.linecolor2;
             graph2.lineThickness = "0";
//             graph2.balloonText="bps Out: [[value]]" + chartOption.axistitle1;
             
             
//             String preCompare = "";
//             
//             if(chartOption.preValue == 0)
//            	 preCompare = "전일";
//             else if(chartOption.preValue == 1)
//	 			 preCompare = "전주";
//			 else if(chartOption.preValue == 2)
//				 preCompare = "전월";
//			 else
//				 preCompare = "";		     
//            	 
//             graph2.title = preCompare;
//        	 graph2.lineColor = chartOption.linecolor2;
//        	 graph2.balloonText = preCompare" + : [[value]]" + chartOption.axistitle1;       
        	 
             graph2.lineColor = chartOption.linecolor2;
             
             if(chartOption.preValue == 0)
             {
            	graph2.title = VAR_SVCPERFOM_PREDAY;
        	 	graph2.balloonText=VAR_SVCPERFOM_PREDAY + ": [[value]]" + chartOption.axistitle1; 
             }
             else if(chartOption.preValue == 1)
             {
             	graph2.title = VAR_SVCPERFOM_PREWEEKS;
             	graph2.balloonText=VAR_SVCPERFOM_PREWEEKS + ": [[value]]" + chartOption.axistitle1; 
             }
             else if(chartOption.preValue == 2)
             {
             	graph2.title = VAR_SVCPERFOM_PREMONTH;
         	 	graph2.balloonText=VAR_SVCPERFOM_PREMONTH + ": [[value]]" + chartOption.axistitle1; 
             }
             else
             {
            	graph2.title = chartOption.selectedDate;
          	 	graph2.balloonText=chartOption.selectedDate + ": [[value]]" + chartOption.axistitle1; 
             }
             
             
             
//             if(chartOption.bpsValue == 0)
//             {
//            	 graph2.title = "preBps In";
//            	 graph2.lineColor = chartOption.linecolor2;
//            	 graph2.balloonText="preBps In: [[value]]" + chartOption.axistitle1;            	 
//             }
//             else if(chartOption.bpsValue == 1)
//             {
//            	 graph2.title = "preBps Out";
//            	 graph2.lineColor = chartOption.linecolor2;
//            	 graph2.balloonText="preBps Out: [[value]]" + chartOption.axistitle1;                 
//             }
//             else
//             {
//            	 graph2.title = "preBps Total"; 
//            	 graph2.lineColor = chartOption.linecolor2;
//            	 graph2.balloonText="preBps Total: [[value]]" + chartOption.axistitle1;             	 
//             }
             
             graph2.connect = false;
             graph2.gapPeriod = chartOption.interval;
             if(chartOption.preValue != -1)
             {
            	 chart.addGraph(graph2);
             }
             
             /*
             //  graph 3
             var graph3 = new AmCharts.AmGraph();
             graph3.valueAxis = valueAxis1; // we have to indicate which value axis should be used
             graph3.title = "bps Total";
             graph3.valueField = "thirdValue";
             graph3.bullet = "round";
             graph3.hideBulletsCount = 5; 
             graph3.type = "line";
             //graph3.fillAlphas = 0.4;
             graph3.lineColor = chartOption.linecolor3;
             graph3.lineThickness = "1";
             graph3.balloonText="bps Total: [[value]]" + chartOption.axistitle1;
             chart.addGraph(graph3);
             */
             // CURSOR
             var chartCursor = new AmCharts.ChartCursor();
             chartCursor.cursorPosition = "middle";
             chartCursor.categoryBalloonDateFormat = "YYYY-MM-DD JJ:NN:SS";
             chartCursor.zoomable = true;
             chartCursor.cursorColor= chartOption.cursorColor;
             chart.addChartCursor(chartCursor);
             
             // WRITE
             chart.write(chartOption.chartname);          

         // this method is called when chart is first inited as we listen for "dataUpdated" event
	         function zoomChart()
	         {
	             // different zoom methods can be used - zoomToIndexes, zoomToDates, zoomToCategoryValues
	             chart.zoomToIndexes(10, 20);
	         }
//	         console.log(chart);
		}
	},
	OBSvcPerfomanceConnChartViewerExtends : function(chartDataObj, chartOptionObj) //서비스 모니터링 Concurrent Session
	{
		with (this) 
		{
			 var chartData = chartDataObj;
			 var chartOption = chartOptionObj;
             // SERIAL CHART 옵션
             var chart = new AmCharts.AmSerialChart();
             chart.pathToImages = "/js/extern/amcharts_images/";             
             chart.dataProvider = chartData;
             chart.categoryField = "occurredTime";
             chart.gridAboveGraphs= "true";
             
             // 대상이 없을때 보여지는 챠트  양 사이드 border            
             chart.plotAreaBorderAlpha =  1;
             chart.plotAreaBorderColor =  '#e1e1e1'; 
             
             chart.autoMargins =  false;
             chart.columnsAboveLines = true;
             chart.marginTop = 10;
             chart.marginBottom = 23;
             chart.marginLeft = 50;
             chart.marginRight = 20;             
             
             chart.fontFamily = "Arial";
             chart.fontSize = 10;
             chart.zoomOutButton =
             {
	             backgroundColor: '#000000',
	             backgroundAlpha: 0.05,
	             fontSize : 9
             };
             chart.zoomOutButtonImageSize = 14;
             chart.usePrefixes = true;
             chart.prefixesOfBigNumbers = [{number:1e+3,prefix:"k"},
                                           {number:1e+6,prefix:"M"},
                                           {number:1e+9,prefix:"G"}];
             // 처음 로드 화면에 zoom 기능을 사용하고 싶다면 ("dataUpdated", zoomChart); 로 add 후 조절은 zoomChart method 에서한다.
             //chart.addListener("dataUpdated", zoomChart);


             // SCROLLBAR (상단의 확대 축소 바)
             var chartScrollbar = new AmCharts.ChartScrollbar();
             chart.addChartScrollbar(chartScrollbar);
             chartScrollbar.dragIconHeight =  20;
             chartScrollbar.dragIconWidth = 20;
             chartScrollbar.scrollbarHeight = 10;     
             
             // Balloon             
             var balloon = chart.balloon;
             balloon.adjustBorderColor = true;
             balloon.pointerWidth = 3;
             balloon.shadowAlpha = 0; 
             balloon.borderThickness = 1;
             balloon.fillAlpha = 0.95;
             balloon.horizontalPadding = 6;
             balloon.verticalPadding = -2; 
             balloon.fontSize = 8;  
             
             
             // LEGEND
             var legend = new AmCharts.AmLegend();
             legend.autoMargins = false;
             legend.marginBottom =  -5;
             legend.marginTop =  -1; 
//            legend.backgroundColor ="#ff0000";
//         	  legend.backgroundAlpha ="0.9";
             legend.align = "center";            
             legend.markerLabelGap = 4;           
             legend.position = "bottom";
             legend.valueWidth = 20;
             legend.verticalGap = 1;
//             legend.valueText = "[[value]]";
             legend.labelText = "[[title]]";
             legend.markerSize = 10;
             legend.fontSize = 9;
             chart.addLegend(legend);           

             // Axes
             // X축 Category Axis (on the down)               
             var categoryAxis = chart.categoryAxis;
             categoryAxis.parseDates = true; // as our data is date-based, we set parseDates to true
             categoryAxis.minPeriod = "ss"; // our data is daily, so we set minPeriod to DD
             categoryAxis.dashLength = 2;
             categoryAxis.dateFormats = [{period:'ss',format:'JJ:NN:SS'},
                                         {period:'mm',format:'JJ:NN'},
                                         {period:'hh',format:'JJ:NN'},
                                         {period:'DD',format:'YYYY-MM-DD'},
                                         {period:'WW',format:'YYYY-MM-DD'},
                                         {period:'MM',format:'MM'},
                                         {period:'YYYY',format:'YYYY'}];
             categoryAxis.gridAlpha = 0.3;             

             // Y축 value axis (on the left)
             var valueAxis1 = new AmCharts.ValueAxis();
             valueAxis1.stackType = "none";
             valueAxis1.tickLength = 0; 
             valueAxis1.dashLength = 2;
             valueAxis1.gridAlpha = 0.3;             
           //  valueAxis1.axisColor = chartOption.linecolor3;
             valueAxis1.axisThickness = 1;
             valueAxis1.usePrefixes = true;
//             valueAxis1.gridAlpha = 0;
             valueAxis1.prefixesOfBigNumbers = [
                                                {number:1e+3,prefix:"k"},
                                                {number:1e+6,prefix:"M"},
                                                {number:1e+9,prefix:"G"}];
             valueAxis1.minimum = chartOption.min;
             if (chartOption.max != null)
             {
            	 valueAxis1.maximum = chartOption.max;             
             }             
             if(chartOption.connValue == 0)
             {
            	 valueAxis1.title = chartOption.axistitle2;
             }
             else
             {
            	 valueAxis1.title = chartOption.axistitle;
             }
             
             valueAxis1.titleBold = false;
             valueAxis1.titleColor = "#666666";
             chart.addValueAxis(valueAxis1);
             
             // GRAPHS             
             //  graph 2
             var graph2 = new AmCharts.AmGraph();
             graph2.valueAxis = valueAxis1; // we have to indicate which value axis should be used
//             graph2.title = "preConcurrent Sessions";
             graph2.valueField = "secondValue";
             graph2.bullet = "round";
             graph2.hideBulletsCount = 5; 
             graph2.type = "line";
             graph2.fillAlphas = 0.3;
             graph2.lineColor = chartOption.linecolor4;
             graph2.lineThickness = "0";
             graph2.connect = false;
             graph2.gapPeriod = chartOption.interval;
//             graph2.balloonText="preConcurrent Sessions: [[value]]" + chartOption.axistitle2;
             
             var graph1 = new AmCharts.AmGraph();
             graph1.valueAxis = valueAxis1; // we have to indicate which value axis should be used
             graph1.title = chartOption.title;
//             if(chartOption.connValue == 0)
//             {
//            	 graph1.title = "Concurrent Sessions";
//             }
//             else
//             {
//            	 graph1.title = "Response Time";
//             }
             
             graph1.valueField = "firstValue";
             graph1.bullet = "round";
             graph1.hideBulletsCount = 5; 
             graph1.type = "line";
             //graph2.fillAlphas = 0.4;
             graph1.lineColor = chartOption.linecolor3;
             graph1.lineThickness = "1";
	     	 graph1.connect = false;
             graph1.gapPeriod = chartOption.interval;	                         
                         
             if(chartOption.connValue == 0)
             {
            	 graph1.balloonText=chartOption.title + ": [[value]]" + chartOption.axistitle2;
             }
             else
             {
            	 graph1.balloonText=chartOption.title + ": [[value]]" + chartOption.axistitle;
             }
             
             chart.addGraph(graph1);
             

             
             if(chartOption.preValue == 0)
             {
            	graph2.title = VAR_SVCPERFOM_PREDAY;
            	if(chartOption.connValue == 0)
                {
            		graph2.balloonText=VAR_SVCPERFOM_PREDAY + ": [[value]]" + chartOption.axistitle2; 
                }
            	else
            	{
            		graph2.balloonText=VAR_SVCPERFOM_PREDAY + ": [[value]]" + chartOption.axistitle;
            	}
             }
             else if(chartOption.preValue == 1)
             {
             	graph2.title = VAR_SVCPERFOM_PREWEEKS;
             	if(chartOption.connValue == 0)
                {
             		graph2.balloonText=VAR_SVCPERFOM_PREWEEKS + ": [[value]]" + chartOption.axistitle2; 
	            }
	         	else
	         	{
	         		graph2.balloonText=VAR_SVCPERFOM_PREWEEKS + ": [[value]]" + chartOption.axistitle;
	         	}
             }
             else if(chartOption.preValue == 2)
             {
             	graph2.title = VAR_SVCPERFOM_PREMONTH;
             	if(chartOption.connValue == 0)
                {
             		graph2.balloonText=VAR_SVCPERFOM_PREMONTH + ": [[value]]" + chartOption.axistitle2;
                }
             	else
             	{
             		graph2.balloonText=VAR_SVCPERFOM_PREMONTH + ": [[value]]" + chartOption.axistitle;
             	}
             }
             else
             {
            	graph2.title = chartOption.selectedDate;
            	if(chartOption.connValue == 0)
                {
              	 	graph2.balloonText=chartOption.selectedDate + ": [[value]]" + chartOption.axistitle2;            		
                }
            	else
            	{
            		graph2.balloonText=chartOption.selectedDate + ": [[value]]" + chartOption.axistitle;
            	}
             }
             
             if(chartOption.preValue != -1)
             {
            	 chart.addGraph(graph2);
             }

             // CURSOR
             var chartCursor = new AmCharts.ChartCursor();
             chartCursor.cursorPosition = "middle";
             chartCursor.categoryBalloonDateFormat = "YYYY-MM-DD JJ:NN:SS";
             chartCursor.zoomable = true;
             chartCursor.cursorColor= chartOption.cursorColor;
             chart.addChartCursor(chartCursor);             

             // WRITE
             chart.write(chartOption.chartname);          

         // this method is called when chart is first inited as we listen for "dataUpdated" event
	         function zoomChart()
	         {
	             // different zoom methods can be used - zoomToIndexes, zoomToDates, zoomToCategoryValues
	             chart.zoomToIndexes(10, 20);
	         }             
		}
	},
	/*
	 * 모듈명 : OBNewInterFaceChart
	 * 성격 : 최대 3개 Multi Line chart , legend 존재
	 * 매개변수 : chartDataObj, chartOptionObj ( min, max, linecolor1, linecolor2, linecolor3, chartname, axistitle1, maxPos, cursorColor)
	 * 사용처 : 1. 모니터링 > 서비스 성능 bps , Connection 2 Value Chart
	 * 사용처 : 2. 모니터링 > 서비스 Member 성능 모니터링 > bps , Connection 2 Value Chart
	  */
	OBNewInterFaceChart : function(chartDataObj, chartOptionObj) //모니터링 > ADC > 인터페이스
	{
		with (this) 
		{
			 var chartData = chartDataObj;
			 var chartOption = chartOptionObj;
             // SERIAL CHART 옵션
             var chart = new AmCharts.AmSerialChart();
             chart.pathToImages = "/js/extern/amcharts_images/";             
             chart.dataProvider = chartData;
             chart.categoryField = "occurredTime";
             chart.gridAboveGraphs= "true"; 
             
             // 대상이 없을때 보여지는 챠트  양 사이드 border            
             chart.plotAreaBorderAlpha =  1;
             chart.plotAreaBorderColor =  '#e1e1e1'; 
             
             chart.autoMargins =  false;
             chart.columnsAboveLines = true;
             chart.marginTop = 10;
             chart.marginBottom = 23;
             chart.marginLeft = 50;
             chart.marginRight = 20;             
             

             chart.fontFamily = "Arial";
             chart.fontSize = 10;
             chart.zoomOutButton =
             {
	             backgroundColor: '#000000',
	             backgroundAlpha: 0.05,
	             fontSize : 9
             };
             chart.zoomOutButtonImageSize = 14;
             chart.usePrefixes = true;
             chart.prefixesOfBigNumbers = [{number:1e+3,prefix:"k"},
                                           {number:1e+6,prefix:"M"},
                                           {number:1e+9,prefix:"G"}];
             // 처음 로드 화면에 zoom 기능을 사용하고 싶다면 ("dataUpdated", zoomChart); 로 add 후 조절은 zoomChart method 에서한다.
             //chart.addListener("dataUpdated", zoomChart);

             // Axes
             // X축 Category Axis (on the down)               
             var categoryAxis = chart.categoryAxis;
             categoryAxis.parseDates = true; // as our data is date-based, we set parseDates to true
             categoryAxis.minPeriod = "ss"; // our data is daily, so we set minPeriod to DD
             categoryAxis.dashLength = 2;
             categoryAxis.dateFormats = [{period:'ss',format:'JJ:NN:SS'},
                                         {period:'mm',format:'JJ:NN'},
                                         {period:'hh',format:'JJ:NN'},
                                         {period:'DD',format:'YYYY-MM-DD'},
                                         {period:'WW',format:'YYYY-MM-DD'},
                                         {period:'MM',format:'MM'},
                                         {period:'YYYY',format:'YYYY'}];
             categoryAxis.gridAlpha = 0.3;             

             // Y축 value axis (on the left)
             var valueAxis1 = new AmCharts.ValueAxis();
             valueAxis1.stackType = "none";
             valueAxis1.tickLength = 0;
             valueAxis1.dashLength = 2;
             valueAxis1.gridAlpha = 0.3;
            // valueAxis1.axisColor = chartOption.linecolor3;
             valueAxis1.axisThickness = 1;
             valueAxis1.usePrefixes = true;
             valueAxis1.prefixesOfBigNumbers = [
                                                {number:1e+3,prefix:"k"},
                                                {number:1e+6,prefix:"M"},
                                                {number:1e+9,prefix:"G"}];
             valueAxis1.minimum = chartOption.min;
             if (chartOption.max != null)
             {
            	 valueAxis1.maximum = chartOption.max;             
             }             
             valueAxis1.title = chartOption.axistitle;
             valueAxis1.titleBold = false;
             valueAxis1.titleColor = "#666666";
             chart.addValueAxis(valueAxis1);         
         

             // GRAPHS
             //  graph 3
             var graph3 = new AmCharts.AmGraph();
             graph3.valueAxis = valueAxis1; // we have to indicate which value axis should be used
             graph3.title = "Total";
             graph3.valueField = "thirdValue";
             graph3.bullet = "round";
             graph3.hideBulletsCount = 5; 
             graph3.type = "line";
             graph3.fillAlphas = 0.3;
             graph3.lineColor = chartOption.linecolor3;
             graph3.lineThickness = "0";
             graph3.balloonText="Total: [[value]]" + chartOption.axistitle;
             chart.addGraph(graph3);
             
             //  graph 1
             var graph1 = new AmCharts.AmGraph();
             graph1.valueAxis = valueAxis1; // we have to indicate which value axis should be used
             graph1.title = "In";
             graph1.valueField = "firstValue";
             graph1.bullet = "round";
             graph1.hideBulletsCount = 5; 
             graph1.type = "line";
             //graph1.fillAlphas = 0.4;
             graph1.lineColor = chartOption.linecolor1;
             graph1.lineThickness = "1";
             graph1.balloonText="In: [[value]]" + chartOption.axistitle;
             //graph1.hidden = true;
             chart.addGraph(graph1);
             
         //  graph 2
             var graph2 = new AmCharts.AmGraph();
             graph2.valueAxis = valueAxis1; // we have to indicate which value axis should be used
             graph2.title = "Out";
             graph2.valueField = "secondValue";
             graph2.bullet = "round";
             graph2.hideBulletsCount = 5; 
             graph2.type = "line";
             //graph2.fillAlphas = 0.4;
             graph2.lineColor = chartOption.linecolor2;
             graph2.lineThickness = "1";
             graph2.balloonText="Out: [[value]]" + chartOption.axistitle;
            // graph2.hidden = true;
             chart.addGraph(graph2);
             
             
             // CURSOR
             var chartCursor = new AmCharts.ChartCursor();
             chartCursor.cursorPosition = "middle";
             chartCursor.categoryBalloonDateFormat = "YYYY-MM-DD JJ:NN:SS";
             chartCursor.zoomable = true;
             chartCursor.cursorColor= chartOption.cursorColor;
             chart.addChartCursor(chartCursor); 
             
             

             // SCROLLBAR (상단의 확대 축소 바)
             var chartScrollbar = new AmCharts.ChartScrollbar();
             chart.addChartScrollbar(chartScrollbar);
             chartScrollbar.dragIconHeight =  20;
             chartScrollbar.dragIconWidth = 20;
             chartScrollbar.scrollbarHeight = 10;
             
             // Balloon             
             var balloon = chart.balloon;
             balloon.adjustBorderColor = true;
             balloon.pointerWidth = 3;
             balloon.shadowAlpha = 0; 
             balloon.borderThickness = 1;
             balloon.fillAlpha = 0.95;
             balloon.horizontalPadding = 6;
             balloon.verticalPadding = -2; 
             balloon.fontSize = 8;  
             
             // LEGEND
             var legend = new AmCharts.AmLegend();
             legend.autoMargins = false;
             legend.marginBottom =  -5;
             legend.marginTop =  -1; 
//             legend.backgroundColor ="#ff0000";
//             legend.backgroundAlpha ="0.9";
             legend.align = "center";            
             legend.markerLabelGap = 4;           
             legend.position = "bottom";
             legend.valueWidth = 50;
             legend.verticalGap = 1;
//             legend.valueText = "[[value]]";
             legend.labelText = "[[title]]";
             legend.markerSize = 10;
             legend.fontSize = 9;
             chart.addLegend(legend); 

             // WRITE
             chart.write(chartOption.chartname);          

         // this method is called when chart is first inited as we listen for "dataUpdated" event
	         function zoomChart()
	         {
	             // different zoom methods can be used - zoomToIndexes, zoomToDates, zoomToCategoryValues
	             chart.zoomToIndexes(10, 20);
	         }             
		}
	},
	/*
	 * 모듈명 : OBConnFlbSlbMultiChartViewer
	 * 성격 : 최대 2개 Multi Line chart , legend 존재
	 * 매개변수 : chartDataObj, chartOptionObj ( min, max, linecolor1, linecolor2, chartname, axistitle1, axistitle2, maxPos, cursorColor)
	 * 사용처 : 1. 모니터링 > 장비 모니터링  Concurrent Session Detail Chart, 대시보드
	  */
	OBConnFlbSlbMultiChartViewer : function(chartDataObj, chartOptionObj)
	{
		with (this) 
		{
			 var chartData = chartDataObj;
			 var chartOption = chartOptionObj;
             // SERIAL CHART 옵션
             var chart = new AmCharts.AmSerialChart();
             chart.pathToImages = "/js/extern/amcharts_images/";             
             chart.dataProvider = chartData;
             chart.categoryField = "occurredTime";
             chart.gridAboveGraphs= "true";             

             // 대상이 없을때 보여지는 챠트  양 사이드 border            
             chart.plotAreaBorderAlpha =  1;
             chart.plotAreaBorderColor =  '#e1e1e1'; 
             
             chart.autoMargins =  false;
             chart.columnsAboveLines = true;
             chart.marginTop = 5;
             chart.marginBottom = 19;
             chart.marginLeft = 50;
             chart.marginRight = 40;             
             
             chart.fontFamily = "Arial";
             chart.fontSize = 10;
             chart.zoomOutButton =
             {
	             backgroundColor: '#000000',
	             backgroundAlpha: 0.05,
	             fontSize : 9
             };
             chart.zoomOutButtonImageSize = 14;
             chart.usePrefixes = true;
             chart.prefixesOfBigNumbers = [{number:1e+3,prefix:"k"},
                                           {number:1e+6,prefix:"M"},
                                           {number:1e+9,prefix:"G"}];
             // 처음 로드 화면에 zoom 기능을 사용하고 싶다면 ("dataUpdated", zoomChart); 로 add 후 조절은 zoomChart method 에서한다.
             //chart.addListener("dataUpdated", zoomChart);

             // Axes
             // X축 Category Axis (on the down)               
             var categoryAxis = chart.categoryAxis;
             categoryAxis.parseDates = true; // as our data is date-based, we set parseDates to true
             categoryAxis.minPeriod = "ss"; // our data is daily, so we set minPeriod to DD
             categoryAxis.dashLength = 2;
             categoryAxis.dateFormats = [{period:'ss',format:'JJ:NN:SS'},
                                         {period:'mm',format:'JJ:NN'},
                                         {period:'hh',format:'JJ:NN'},
                                         {period:'DD',format:'YYYY-MM-DD'},
                                         {period:'WW',format:'YYYY-MM-DD'},
                                         {period:'MM',format:'MM'},
                                         {period:'YYYY',format:'YYYY'}];
             categoryAxis.gridAlpha = 0.3;             

             // Y축 value axis (on the left)
             var valueAxis1 = new AmCharts.ValueAxis();
             valueAxis1.stackType = "regular";
             valueAxis1.tickLength = 0;
             valueAxis1.dashLength = 2;
             valueAxis1.gridAlpha = 0.3;
           //  valueAxis1.axisColor = chartOption.linecolor1;
             valueAxis1.axisThickness = 1;
             valueAxis1.usePrefixes = true;
             valueAxis1.prefixesOfBigNumbers = [
                                                {number:1e+3,prefix:"k"},
                                                {number:1e+6,prefix:"M"},
                                                {number:1e+9,prefix:"G"}];
             valueAxis1.minimum = chartOption.min;
             if (chartOption.max != null)
             {
            	 valueAxis1.maximum = chartOption.max;             
             }             
             valueAxis1.title = chartOption.axistitle1;
             valueAxis1.titleBold = false;
             valueAxis1.titleColor = "#666666";
             chart.addValueAxis(valueAxis1);
             
          // Y축 value axis2 (on the right)
             var valueAxis2 = new AmCharts.ValueAxis();
             valueAxis2.stackType = "none";
             valueAxis2.tickLength = 0; 
             valueAxis2.gridAlpha = 0;              
            // valueAxis2.axisColor = chartOption.linecolor2;
             valueAxis2.axisThickness = 1;
             valueAxis2.usePrefixes = true;
             valueAxis2.position = "right";
             valueAxis2.prefixesOfBigNumbers = [
                                                {number:1e+3,prefix:"k"},
                                                {number:1e+6,prefix:"M"},
                                                {number:1e+9,prefix:"G"}];
             valueAxis2.minimum = chartOption.min;
             if (chartOption.max != null)
             {
            	 valueAxis2.maximum = chartOption.max;             
             }             
             valueAxis2.title = chartOption.axistitle2;
             valueAxis2.titleBold = false;
             valueAxis2.titleColor = "#666666";
             chart.addValueAxis(valueAxis2);
             
         //  graph1
             var graph1 = new AmCharts.AmGraph();
             graph1.valueAxis = valueAxis2; // we have to indicate which value axis should be used
             graph1.title = "SUM";
             graph1.valueField = "thirdValue";                    
             graph1.type = "line";
             graph1.fillAlphas = 0.3;
             graph1.lineColor = chartOption.linecolor1;
             graph1.lineThickness = "0";
             graph1.visibleInLegend = false;             
             graph1.balloonText="SUM: [[value]]" + chartOption.axistitle2;
             
             graph1.connect = false;
             graph1.gapPeriod = chartOption.interval;
             chart.addGraph(graph1);    
             
             
             // GRAPHS
             //  graph 2
             var graph2 = new AmCharts.AmGraph();
             graph2.valueAxis = valueAxis1; // we have to indicate which value axis should be used
             graph2.title = "SLB";
             graph2.valueField = "firstValue";
             graph2.type = "line";
             graph2.fillAlphas = 1;
             graph2.lineColor = chartOption.linecolor2;
             graph2.lineThickness = "0";
             graph2.balloonText="SLB: [[value]]" + chartOption.axistitle1;
             
             graph2.connect = false;
             graph2.gapPeriod = chartOption.interval;
             chart.addGraph(graph2);
             
         //  graph 3
             var graph3 = new AmCharts.AmGraph();
             graph3.valueAxis = valueAxis1; // we have to indicate which value axis should be used
             graph3.title = "FLB";
             graph3.valueField = "secondValue";
             graph3.type = "line";
             graph3.fillAlphas = 1;
             graph3.lineColor = chartOption.linecolor3;
             graph3.lineThickness = "0";
             graph3.balloonText="FLB: [[value]]" + chartOption.axistitle2;
             
             graph3.connect = false;
             graph3.gapPeriod = chartOption.interval;
             chart.addGraph(graph3);           

             // CURSOR
             var chartCursor = new AmCharts.ChartCursor();
             chartCursor.cursorPosition = "middle";
             chartCursor.categoryBalloonDateFormat = "YYYY-MM-DD JJ:NN:SS";
             chartCursor.zoomable = true;
             chartCursor.cursorColor= chartOption.cursorColor;
             chart.addChartCursor(chartCursor);
             
             /*if (chartOption.maxPos != null && chartOption.maxPos > 0)
             {
            	 var guide = new AmCharts.Guide();
                 guide.value = chartOption.maxPos; 
                 guide.lineColor = "#CC0000";
                 //guide.dashLength = 5;
                 guide.label = "max : " + addCommaforNumber(chartOption.maxPos) ;               
                 guide.inside = true;
                 guide.lineAlpha = 1;                 
                 valueAxis1.addGuide(guide);
                 guide = null;
             }*/
             
            /*if (ConfigDate != null && ConfigDate.length > 0)
            {
            	 for ( var i = 0; i < ConfigDate.length; i++)
            		 {
			             var guide2 = new AmCharts.Guide();
			             guide2.date = ConfigDate[i]; 
			             guide2.lineColor = "#0d67be";
			             //guide2.dashLength = 4;
			             guide2.label = "config";            
			             guide2.inside = true;
			             guide2.lineAlpha = 1;
			             guide2.labelRotation = "90";
			             guide2.fontSize = "8";
			             categoryAxis.addGuide(guide2);
			             guide2 = null;
            		 }
            }*/
             // SCROLLBAR (상단의 확대 축소 바)
             var chartScrollbar = new AmCharts.ChartScrollbar();
             chart.addChartScrollbar(chartScrollbar);
             chartScrollbar.dragIconHeight =  20;
             chartScrollbar.dragIconWidth = 20;
             chartScrollbar.scrollbarHeight = 10;           
             
             // Balloon             
             var balloon = chart.balloon;
             balloon.adjustBorderColor = true;
             balloon.pointerWidth = 3;
             balloon.shadowAlpha = 0; 
             balloon.borderThickness = 1;
             balloon.fillAlpha = 0.95;
             balloon.horizontalPadding = 6;
             balloon.verticalPadding = -2; 
             balloon.fontSize = 8;   
             
          // LEGEND
             var legend = new AmCharts.AmLegend();
             legend.autoMargins = false;
             legend.marginBottom =  -5;
             legend.marginTop =  -1;              
             legend.align = "center";            
             legend.markerLabelGap = 5;           
             legend.position = "bottom";
             legend.valueWidth = 20;
             legend.verticalGap = 1;
             legend.valueText = "[[value]]";
//             legend.valueText = chartOption.legendVal;
             legend.markerSize = 11;
             chart.addLegend(legend);   

             // WRITE
             chart.write(chartOption.chartname);          

         // this method is called when chart is first inited as we listen for "dataUpdated" event
	         function zoomChart()
	         {
	             // different zoom methods can be used - zoomToIndexes, zoomToDates, zoomToCategoryValues
	             chart.zoomToIndexes(10, 20);
	         }
	         return chart;
		}
	},
	/*
	 * 모듈명 : OBCPUChartViewerAlteon
	 * 성격 : 최대 33개 Multi Line chart, Alteon 전용
	 * 매개변수 : chartDatalist, min, max, chartname
	 * 사용처 : 1. 대시보드 위젯 (설정 변경 현황, 장애 모니터링 현황, 응답시간, 전체 ADC Throughput, ADC Memory, ADC CPU, Concurrent session, Throughput)	
	 * */	
	OBCPUChartViewerAlteon : function(chartDatalist, min, max, chartname)
	{
		with (this) 
		{          
			var chartData = chartDatalist;	

             // SERIAL CHART 옵션
             var chart = new AmCharts.AmSerialChart();
             chart.pathToImages = "js/extern/amcharts_images/";             
             chart.dataProvider = chartData;
             chart.categoryField = "occurredTime";
             chart.gridAboveGraphs= "true"; 

             // 대상이 없을때 보여지는 챠트  양 사이드 border            
             chart.plotAreaBorderAlpha =  1;
             chart.plotAreaBorderColor =  '#e1e1e1'; 
             
             chart.autoMargins = true;
             chart.columnsAboveLines = true;
             chart.marginTop = 5;
             chart.marginBottom = 25;
             chart.marginLeft = 50;
              
             chart.fontFamily = "Arial";
             chart.fontSize = 10;
             chart.zoomOutButton =
             {
	             backgroundColor: '#000000',
	             backgroundAlpha: 0.05,
	             fontSize : 9
             };
             chart.zoomOutButtonImageSize = 14;
             chart.usePrefixes = true;
             chart.prefixesOfBigNumbers = [{number:1e+3,prefix:"k"},
                                           {number:1e+6,prefix:"M"},
                                           {number:1e+9,prefix:"G"}];
             // 처음 로드 화면에 zoom 기능을 사용하고 싶다면 ("dataUpdated", zoomChart); 로 add 후 조절은 zoomChart 에서한다.
             //chart.addListener("dataUpdated", zoomChart);
             
             // Ballon Option (Alteon CPU Chart 만 유효)
             var balloon = chart.balloon;             
             balloon.adjustBorderColor = true;            
             //balloon.cornerRadius = 5;            
             balloon.verticalPadding = 0;
             balloon.fontSize = 9;
             
             // AXES
             // category axis (on the down)               
             var categoryAxis = chart.categoryAxis;
             categoryAxis.parseDates = true; // as our data is date-based, we set parseDates to true
             categoryAxis.minPeriod = "ss"; // our data is daily, so we set minPeriod to DD
             categoryAxis.dashLength = 2;
             categoryAxis.dateFormats = [{period:'ss',format:'JJ:NN:SS'},
                                         {period:'mm',format:'JJ:NN'},
                                         {period:'hh',format:'JJ:NN'},
                                         {period:'DD',format:'YYYY-MM-DD'},
                                         {period:'WW',format:'YYYY-MM-DD'},
                                         {period:'MM',format:'MM'},
                                         {period:'YYYY',format:'YYYY'}];
             categoryAxis.gridAlpha = 0.3;             

             // value axis (on the left)
             var valueAxis1 = new AmCharts.ValueAxis();
             valueAxis1.stackType = "none";
             valueAxis1.tickLength = 0;
             valueAxis1.dashLength = 2;
             valueAxis1.gridAlpha = 0.3;
            // valueAxis1.axisColor = "#6cdaba";
             valueAxis1.axisThickness = 1;
             valueAxis1.minimum = min;
             if (max != null)
             {
            	 valueAxis1.maximum = max;
             }             
             valueAxis1.title = "%";
             valueAxis1.titleBold = false;
             valueAxis1.titleColor = "#666666";
             valueAxis1.usePrefixes = true;
             valueAxis1.prefixesOfBigNumbers = [
                                                {number:1e+3,prefix:"k"},
                                                {number:1e+6,prefix:"M"},
                                                {number:1e+9,prefix:"G"}];
             chart.addValueAxis(valueAxis1);             

             // GRAPHS
             //  graph 1
             var graph1 = new AmCharts.AmGraph();
             graph1.valueAxis = valueAxis1; // we have to indicate which value axis should be used
             graph1.title = "MP Usage";
             graph1.valueField = "MPValue";
             graph1.bullet = "round";
             graph1.hideBulletsCount = 5; 
             graph1.type = "line";
             graph1.lineColor = "#6cdaba";
             graph1.lineThickness = "1";
             graph1.showBalloon = false;
             graph1.balloonText="MP Usage: [[value]] %";
             chart.addGraph(graph1);
             
             // graph 2               
             var graph2 = new AmCharts.AmGraph();
             graph2.valueAxis = valueAxis1; // we have to indicate which value axis should be used
             graph2.title = "SP1 Usage";
             graph2.valueField = "Value1";
             graph2.bullet = "round";
             graph2.hideBulletsCount = 5; 
             graph2.type = "line";
             graph2.lineColor = "#8cdbd8";
             graph2.lineThickness = "1";
             graph2.showBalloon = false;
             graph2.balloonText="SP1 Usage: [[value]] %";
             chart.addGraph(graph2);

             // graph 3
             var graph3 = new AmCharts.AmGraph();
             graph3.valueAxis = valueAxis1; // we have to indicate which value axis should be used
             graph3.valueField = "Value2";
             graph3.title = "SP2 Usage";
             graph3.bullet = "round";
             graph3.hideBulletsCount = 5; 
             graph3.type = "line";
             graph3.lineColor = "#fda4ba";
             graph3.lineThickness = "1";
             graph3.showBalloon = false;
             graph3.balloonText="SP2 Usage: [[value]] %";
             chart.addGraph(graph3);
             
             // graph 4
             var graph4 = new AmCharts.AmGraph();
             graph4.valueAxis = valueAxis1; // we have to indicate which value axis should be used
             graph4.valueField = "Value3";
             graph4.title = "SP3 Usage";
             graph4.bullet = "round";
             graph4.hideBulletsCount = 5; 
             graph4.type = "line";
             graph4.lineColor = "#f9e165";
             graph4.lineThickness = "1";
             graph4.showBalloon = false;
             graph4.balloonText="SP3 Usage: [[value]] %";
             chart.addGraph(graph4);
             
             // graph 5
             var graph5 = new AmCharts.AmGraph();
             graph5.valueAxis = valueAxis1; // we have to indicate which value axis should be used
             graph5.valueField = "Value4";
             graph5.title = "SP4 Usage";
             graph5.bullet = "round";
             graph5.hideBulletsCount = 5; 
             graph5.type = "line";
             graph5.lineColor = "#52d3a5";
             graph5.lineThickness = "1";
             graph5.showBalloon = false;
             graph5.balloonText="SP4 Usage: [[value]] %";
             chart.addGraph(graph5);
             
             // graph 6               
             var graph6 = new AmCharts.AmGraph();
             graph6.valueAxis = valueAxis1;
             graph6.title = "SP5 Usage";
             graph6.valueField = "Value5";
             graph6.bullet = "round";
             graph6.hideBulletsCount = 5; 
             graph6.type = "line";
             graph6.lineColor = "#9fbcdd";
             graph6.lineThickness = "1";
             graph6.showBalloon = false;
             graph6.balloonText="SP5 Usage: [[value]] %";
             chart.addGraph(graph6);

             // graph 7
             var graph7 = new AmCharts.AmGraph();
             graph7.valueAxis = valueAxis1;
             graph7.valueField = "Value6";
             graph7.title = "SP6 Usage";
             graph7.bullet = "round";
             graph7.hideBulletsCount = 5; 
             graph7.type = "line";
             graph7.lineColor = "#f170a7";
             graph7.lineThickness = "1";
             graph7.showBalloon = false;
             graph7.balloonText="SP6 Usage: [[value]] %";
             chart.addGraph(graph7);
             
             // graph 8
             var graph8 = new AmCharts.AmGraph();
             graph8.valueAxis = valueAxis1;
             graph8.valueField = "Value7";
             graph8.title = "SP7 Usage";
             graph8.bullet = "round";
             graph8.hideBulletsCount = 5; 
             graph8.type = "line";
             graph8.lineColor = "#ff9b6a";
             graph8.lineThickness = "1";
             graph8.showBalloon = false;
             graph8.balloonText="SP7 Usage: [[value]] %";
             chart.addGraph(graph8);
             
             // graph 9
             var graph9 = new AmCharts.AmGraph();
             graph9.valueAxis = valueAxis1; 
             graph9.valueField = "Value8";
             graph9.title = "SP8 Usage";
             graph9.bullet = "round";
             graph9.hideBulletsCount = 5; 
             graph9.type = "line";
             graph9.lineColor = "#73d790";
             graph9.lineThickness = "1";
             graph9.showBalloon = false;
             graph9.balloonText="SP8 Usage: [[value]] %";
             chart.addGraph(graph9);
             
          // graph 10               
             var graph10 = new AmCharts.AmGraph();
             graph10.valueAxis = valueAxis1;
             graph10.title = "SP9 Usage";
             graph10.valueField = "Value9";
             graph10.bullet = "round";
             graph10.hideBulletsCount = 5; 
             graph10.type = "line";
             graph10.lineColor = "#b5d1e4";
             graph10.lineThickness = "1";
             graph10.showBalloon = false;
             graph10.balloonText="SP9 Usage: [[value]] %";
             chart.addGraph(graph10);

             // graph 11
             var graph11 = new AmCharts.AmGraph();
             graph11.valueAxis = valueAxis1;
             graph11.valueField = "Value10";
             graph11.title = "SP10 Usage";
             graph11.bullet = "round";
             graph11.hideBulletsCount = 5; 
             graph11.type = "line";
             graph11.lineColor = "#ff8ba2";
             graph11.lineThickness = "1";
             graph11.showBalloon = false;
             graph11.balloonText="SP10 Usage: [[value]] %";
             chart.addGraph(graph11);
             
             // graph 12
             var graph12 = new AmCharts.AmGraph();
             graph12.valueAxis = valueAxis1;
             graph12.valueField = "Value11";
             graph12.title = "SP11 Usage";
             graph12.bullet = "round";
             graph12.hideBulletsCount = 5; 
             graph12.type = "line";
             graph12.lineColor = "#ff8ba2";
             graph12.lineThickness = "1";
             graph12.showBalloon = false;
             graph12.balloonText="SP11 Usage: [[value]] %";
             chart.addGraph(graph12);
             
             // graph 13
             var graph13 = new AmCharts.AmGraph();
             graph13.valueAxis = valueAxis1; 
             graph13.valueField = "Value12";
             graph13.title = "SP12 Usage";
             graph13.bullet = "round";
             graph13.hideBulletsCount = 5; 
             graph13.type = "line";
             graph13.lineColor = "#00cfbb";
             graph13.lineThickness = "1";
             graph13.showBalloon = false;
             graph13.balloonText="SP12 Usage: [[value]] %";
              chart.addGraph(graph13);
             
          // graph 14           
             var graph14 = new AmCharts.AmGraph();
             graph14.valueAxis = valueAxis1;
             graph14.title = "SP13 Usage";
             graph14.valueField = "Value13";
             graph14.bullet = "round";
             graph14.hideBulletsCount = 5; 
             graph14.type = "line";
             graph14.lineColor = "#bfb9da";
             graph14.lineThickness = "1";
             graph14.showBalloon = false;
             graph14.balloonText="SP13 Usage: [[value]] %";
             chart.addGraph(graph14);

             // graph 15
             var graph15 = new AmCharts.AmGraph();
             graph15.valueAxis = valueAxis1;
             graph15.valueField = "Value14";
             graph15.title = "SP14 Usage";
             graph15.bullet = "round";
             graph15.hideBulletsCount = 5; 
             graph15.type = "line";
             graph15.lineColor = "#e26cb4";
             graph15.lineThickness = "1";
             graph15.showBalloon = false;
             graph15.balloonText="SP14 Usage: [[value]] %";
             chart.addGraph(graph15);
             
             // graph 16
             var graph16 = new AmCharts.AmGraph();
             graph16.valueAxis = valueAxis1;
             graph16.valueField = "Value15";
             graph16.title = "SP15 Usage";
             graph16.bullet = "round";
             graph16.hideBulletsCount = 5; 
             graph16.type = "line";
             graph16.lineColor = "#ffb970";
             graph16.lineThickness = "1";
             graph16.showBalloon = false;
             graph16.balloonText="SP15 Usage: [[value]] %";
             chart.addGraph(graph16);
             
             // graph 17
             var graph17 = new AmCharts.AmGraph();
             graph17.valueAxis = valueAxis1; 
             graph17.valueField = "Value16";
             graph17.title = "SP16 Usage";
             graph17.bullet = "round";
             graph17.hideBulletsCount = 5; 
             graph17.type = "line";
             graph17.lineColor = "#28c6d1";
             graph17.lineThickness = "1";
             graph17.showBalloon = false;
             graph17.balloonText="SP16 Usage: [[value]] %";
             chart.addGraph(graph17);
             
             // graph 18
             var graph18 = new AmCharts.AmGraph();
             graph18.valueAxis = valueAxis1;
             graph18.valueField = "Value17";
             graph18.title = "SP17 Usage";
             graph18.bullet = "round";
             graph18.hideBulletsCount = 5; 
             graph18.type = "line";
             graph18.lineColor = "#e0bed4";
             graph18.lineThickness = "1";
             graph18.showBalloon = false;
             graph18.balloonText="SP17 Usage: [[value]] %";
             chart.addGraph(graph18);
             
             // graph 19
             var graph19 = new AmCharts.AmGraph();
             graph19.valueAxis = valueAxis1; 
             graph19.valueField = "Value18";
             graph19.title = "SP18 Usage";
             graph19.bullet = "round";
             graph19.hideBulletsCount = 5; 
             graph19.type = "line";
             graph19.lineColor = "#ffa98d";
             graph19.lineThickness = "1";
             graph19.showBalloon = false;
             graph19.balloonText="SP18 Usage: [[value]] %";
             chart.addGraph(graph19);
             
          // graph 20              
             var graph20 = new AmCharts.AmGraph();
             graph20.valueAxis = valueAxis1;
             graph20.title = "SP19 Usage";
             graph20.valueField = "Value19";
             graph20.bullet = "round";
             graph20.hideBulletsCount = 5; 
             graph20.type = "line";
             graph20.lineColor = "#c1de79";
             graph20.lineThickness = "1";
             graph20.showBalloon = false;
             graph20.balloonText="SP19 Usage: [[value]] %";
             chart.addGraph(graph20);

             // graph 21
             var graph21 = new AmCharts.AmGraph();
             graph21.valueAxis = valueAxis1;
             graph21.valueField = "Value20";
             graph21.title = "SP20 Usage";
             graph21.bullet = "round";
             graph21.hideBulletsCount = 5; 
             graph21.type = "line";
             graph21.lineColor = "#23adaa";
             graph21.lineThickness = "1";
             graph21.showBalloon = false;
             graph21.balloonText="SP20 Usage: [[value]] %";
             chart.addGraph(graph21);
             
             // graph 22
             var graph22 = new AmCharts.AmGraph();
             graph22.valueAxis = valueAxis1;
             graph22.valueField = "Value21";
             graph22.title = "SP21 Usage";
             graph22.bullet = "round";
             graph22.hideBulletsCount = 5; 
             graph22.type = "line";
             graph22.lineColor = "#cf92c4";
             graph22.lineThickness = "1";
             graph22.showBalloon = false;
             graph22.balloonText="SP21 Usage: [[value]] %";
             chart.addGraph(graph22);
             
             // graph 23
             var graph23 = new AmCharts.AmGraph();
             graph23.valueAxis = valueAxis1; 
             graph23.valueField = "Value22";
             graph23.title = "SP22 Usage";
             graph23.bullet = "round";
             graph23.hideBulletsCount = 5; 
             graph23.type = "line";
             graph23.lineColor = "#ff8571";
             graph23.lineThickness = "1";
             graph23.showBalloon = false;
             graph23.balloonText="SP22 Usage: [[value]] %";
             chart.addGraph(graph23);
             
          // graph 24              
             var graph24 = new AmCharts.AmGraph();
             graph24.valueAxis = valueAxis1;
             graph24.title = "SP23 Usage";
             graph24.valueField = "Value23";
             graph24.bullet = "round";
             graph24.hideBulletsCount = 5; 
             graph24.type = "line";
             graph24.lineColor = "#fbd958";
             graph24.lineThickness = "1";
             graph24.showBalloon = false;
             graph24.balloonText="SP23 Usage: [[value]] %";
             chart.addGraph(graph24);

             // graph 25
             var graph25 = new AmCharts.AmGraph();
             graph25.valueAxis = valueAxis1;
             graph25.valueField = "Value24";
             graph25.title = "SP24 Usage";
             graph25.bullet = "round";
             graph25.hideBulletsCount = 5; 
             graph25.type = "line";
             graph25.lineColor = "#64add5";
             graph25.lineThickness = "1";
             graph25.showBalloon = false;
             graph25.balloonText="SP24 Usage: [[value]] %";
             chart.addGraph(graph25);
             
             // graph 26
             var graph26 = new AmCharts.AmGraph();
             graph26.valueAxis = valueAxis1;
             graph26.valueField = "Value25";
             graph26.title = "SP25 Usage";
             graph26.bullet = "round";
             graph26.hideBulletsCount = 5; 
             graph26.type = "line";
             graph26.lineColor = "#f0b8d3";
             graph26.lineThickness = "1";
             graph26.showBalloon = false;
             graph26.balloonText="SP25 Usage: [[value]] %";
             chart.addGraph(graph26);
             
             // graph 27
             var graph27 = new AmCharts.AmGraph();
             graph27.valueAxis = valueAxis1; 
             graph27.valueField = "Value26";
             graph27.title = "SP26 Usage";
             graph27.bullet = "round";
             graph27.hideBulletsCount = 5; 
             graph27.type = "line";
             graph27.lineColor = "#fcd089";
             graph27.lineThickness = "1";
             graph27.showBalloon = false;
             graph27.balloonText="SP26 Usage: [[value]] %";
             chart.addGraph(graph27);
             
          // graph 28         
             var graph28 = new AmCharts.AmGraph();
             graph28.valueAxis = valueAxis1;
             graph28.title = "SP27 Usage";
             graph28.valueField = "Value27";
             graph28.bullet = "round";
             graph28.hideBulletsCount = 5; 
             graph28.type = "line";
             graph28.lineColor = "#95db7b";
             graph28.lineThickness = "1";
             graph28.showBalloon = false;
             graph28.balloonText="SP27 Usage: [[value]] %";
             chart.addGraph(graph28);

             // graph 29
             var graph29 = new AmCharts.AmGraph();
             graph29.valueAxis = valueAxis1;
             graph29.valueField = "Value28";
             graph29.title = "SP28 Usage";
             graph29.bullet = "round";
             graph29.hideBulletsCount = 5; 
             graph29.type = "line";
             graph29.lineColor = "#64a8b6";
             graph29.lineThickness = "1";
             graph29.showBalloon = false;
             graph29.balloonText="SP28 Usage: [[value]] %";
             chart.addGraph(graph29);
             
             // graph 30
             var graph30 = new AmCharts.AmGraph();
             graph30.valueAxis = valueAxis1;
             graph30.valueField = "Value29";
             graph30.title = "SP29 Usage";
             graph30.bullet = "round";
             graph30.hideBulletsCount = 5; 
             graph30.type = "line";
             graph30.lineColor = "#e26cb4";
             graph30.lineThickness = "1";
             graph30.showBalloon = false;
             graph30.balloonText="SP29 Usage: [[value]] %";
             chart.addGraph(graph30);
             
             // graph 31
             var graph31 = new AmCharts.AmGraph();
             graph31.valueAxis = valueAxis1; 
             graph31.valueField = "Value30";
             graph31.title = "SP30 Usage";
             graph31.bullet = "round";
             graph31.hideBulletsCount = 5; 
             graph31.type = "line";
             graph31.lineColor = "#ffb970";
             graph31.lineThickness = "1";
             graph31.showBalloon = false;
             graph31.balloonText="SP30 Usage: [[value]] %";
             chart.addGraph(graph31);
             
          // graph 32
             var graph32 = new AmCharts.AmGraph();
             graph32.valueAxis = valueAxis1; 
             graph32.valueField = "Value31";
             graph32.title = "SP31 Usage";
             graph32.bullet = "round";
             graph32.hideBulletsCount = 5; 
             graph32.type = "line";
             graph32.lineColor = "#e1e57c";
             graph32.lineThickness = "1";
             graph32.showBalloon = false;
             graph32.balloonText="SP31 Usage: [[value]] %";
             chart.addGraph(graph32);
             
          // graph 33
             var graph33 = new AmCharts.AmGraph();
             graph33.valueAxis = valueAxis1; 
             graph33.valueField = "Value32";
             graph33.title = "SP32 Usage";
             graph33.bullet = "round";
             graph33.hideBulletsCount = 5; 
             graph33.type = "line";
             graph33.lineColor = "#6c96ca";
             graph33.lineThickness = "1";
             graph33.showBalloon = false;
             graph33.balloonText="SP32 Usage: [[value]] %";
             chart.addGraph(graph33);
             
          // CURSOR
             var chartCursor = new AmCharts.ChartCursor();
             chartCursor.cursorPosition = "middle";
             chartCursor.categoryBalloonDateFormat = "YYYY-MM-DD JJ:NN:SS";
             chartCursor.zoomable = true;
             chartCursor.cursorColor= "#0e7023";
             chart.addChartCursor(chartCursor);

             // SCROLLBAR (상단의 확대 축소 바)
             var chartScrollbar = new AmCharts.ChartScrollbar();
             chart.addChartScrollbar(chartScrollbar);
             chartScrollbar.dragIconHeight =  20;
             chartScrollbar.dragIconWidth = 20;
             chartScrollbar.scrollbarHeight = 10;           
             
             // Balloon             
             var balloon = chart.balloon;
             balloon.adjustBorderColor = true;
             balloon.pointerWidth = 3;
             balloon.shadowAlpha = 0; 
             balloon.borderThickness = 1;
             balloon.fillAlpha = 0.95;
             balloon.horizontalPadding = 6;
             balloon.verticalPadding = -2; 
             balloon.fontSize = 8;   
             
             // LEGEND
             var legend = new AmCharts.AmLegend();
             legend.autoMargins = false;
             legend.marginBottom =  -5;
             legend.marginTop =  -1;              
             legend.align = "center";            
             legend.markerLabelGap = 5;           
             legend.position = "bottom";
             legend.valueWidth = 20;
             legend.verticalGap = 1;
             legend.valueText = "[[value]]";
             legend.markerSize = 11;
             chart.addLegend(legend);                

             // WRITE
             chart.write(chartname);          

         // this method is called when chart is first inited as we listen for "dataUpdated" event
	         function zoomChart()
	         {
	             // different zoom methods can be used - zoomToIndexes, zoomToDates, zoomToCategoryValues
	             chart.zoomToIndexes(10, 20);
	         }             
		}		
	},
	/*
	 * 모듈명 : OBCPUChartViewerNonAlteon
	 * 성격 : 최대 33개 Multi Line chart , Alteon 이외 ADC 전용
	 * 매개변수 : chartDatalist, min, max, chartname
	 * 사용처 : 	 
	 * */	
	OBCPUChartViewerNonAlteon : function(chartDatalist, min, max, chartname)
	{
		with (this) 
		{          
			var chartData = chartDatalist;	

             // SERIAL CHART 옵션
             var chart = new AmCharts.AmSerialChart();
             chart.pathToImages = "js/extern/amcharts_images/";             
             chart.dataProvider = chartData;
             chart.categoryField = "occurredTime";
             chart.gridAboveGraphs= "true"; 

             // 대상이 없을때 보여지는 챠트  양 사이드 border            
             chart.plotAreaBorderAlpha =  1;
             chart.plotAreaBorderColor =  '#e1e1e1'; 
             
             chart.autoMargins = true;
             chart.columnsAboveLines = true;
             chart.marginTop = 20;
             chart.marginBottom = 25;
             chart.marginLeft = 50;
              
             chart.fontFamily = "Arial";
             chart.fontSize = 10;
             chart.zoomOutButton =
             {
	             backgroundColor: '#000000',
	             backgroundAlpha: 0.05,
	             fontSize : 9
             };
             chart.zoomOutButtonImageSize = 14;
             chart.usePrefixes = true;
             chart.prefixesOfBigNumbers = [{number:1e+3,prefix:"k"},
                                           {number:1e+6,prefix:"M"},
                                           {number:1e+9,prefix:"G"}];
             // 처음 로드 화면에 zoom 기능을 사용하고 싶다면 ("dataUpdated", zoomChart); 로 add 후 조절은 zoomChart 에서한다.
             //chart.addListener("dataUpdated", zoomChart);
             
             // Ballon Option (Alteon CPU Chart 만 유효)
             var balloon = chart.balloon;             
             balloon.adjustBorderColor = true;            
             //balloon.cornerRadius = 5;            
             balloon.verticalPadding = 0;
             balloon.fontSize = 9;
             
             // AXES
             // category axis (on the down)               
             var categoryAxis = chart.categoryAxis;
             categoryAxis.parseDates = true; // as our data is date-based, we set parseDates to true
             categoryAxis.minPeriod = "ss"; // our data is daily, so we set minPeriod to DD
             categoryAxis.dashLength = 2;
             categoryAxis.dateFormats = [{period:'ss',format:'JJ:NN:SS'},
                                         {period:'mm',format:'JJ:NN'},
                                         {period:'hh',format:'JJ:NN'},
                                         {period:'DD',format:'YYYY-MM-DD'},
                                         {period:'WW',format:'YYYY-MM-DD'},
                                         {period:'MM',format:'MM'},
                                         {period:'YYYY',format:'YYYY'}];
             categoryAxis.gridAlpha = 0.3;             

             // value axis (on the left)
             var valueAxis1 = new AmCharts.ValueAxis();
             valueAxis1.stackType = "none";
             valueAxis1.tickLength = 0;
             valueAxis1.dashLength = 2;
             valueAxis1.gridAlpha = 0.3;
            // valueAxis1.axisColor = "#6cdaba";
             valueAxis1.axisThickness = 1;
             valueAxis1.minimum = min;
             if (max != null)
             {
            	 valueAxis1.maximum = max;
             }             
             valueAxis1.title = "%";
             valueAxis1.titleBold = false;
             valueAxis1.titleColor = "#666666";
             valueAxis1.usePrefixes = true;
             valueAxis1.prefixesOfBigNumbers = [
                                                {number:1e+3,prefix:"k"},
                                                {number:1e+6,prefix:"M"},
                                                {number:1e+9,prefix:"G"}];
             chart.addValueAxis(valueAxis1);             

             // GRAPHS
             //  graph 1
             var graph1 = new AmCharts.AmGraph();
             graph1.valueAxis = valueAxis1; // we have to indicate which value axis should be used
             graph1.title = "CPU1 Usage";
//             graph1.valueField = "MPValue";
             graph1.valueField = "Value1";
             graph1.bullet = "round";
             graph1.hideBulletsCount = 5; 
             graph1.type = "line";
             graph1.lineColor = "#6cb8c8";
             graph1.lineThickness = "1";
             graph1.showBalloon = false;
             graph1.balloonText="CPU1 Usage: [[value]] %";
             chart.addGraph(graph1);
             
             // graph 2               
             var graph2 = new AmCharts.AmGraph();
             graph2.valueAxis = valueAxis1; // we have to indicate which value axis should be used
             graph2.title = "CPU2 Usagee";
             graph2.valueField = "Value2";
             graph2.bullet = "round";
             graph2.hideBulletsCount = 5; 
             graph2.type = "line";
             graph2.lineColor = "#fbc51a";
             graph2.lineThickness = "1";
             graph2.showBalloon = false;
             graph2.balloonText="CPU2 Usage: [[value]] %";
             chart.addGraph(graph2);

             // graph 3
             var graph3 = new AmCharts.AmGraph();
             graph3.valueAxis = valueAxis1; // we have to indicate which value axis should be used
             graph3.valueField = "Value3";
             graph3.title = "CPU3 Usage";
             graph3.bullet = "round";
             graph3.hideBulletsCount = 5; 
             graph3.type = "line";
             graph3.lineColor = "#d65f3d";
             graph3.lineThickness = "1";
             graph3.showBalloon = false;
             graph3.balloonText="CPU3 Usage: [[value]] %";
             chart.addGraph(graph3);
             
             // graph 4
             var graph4 = new AmCharts.AmGraph();
             graph4.valueAxis = valueAxis1; // we have to indicate which value axis should be used
             graph4.valueField = "Value4";
             graph4.title = "CPU4 Usage";
             graph4.bullet = "round";
             graph4.hideBulletsCount = 5; 
             graph4.type = "line";
             graph4.lineColor = "#976e96";
             graph4.lineThickness = "1";
             graph4.showBalloon = false;
             graph4.balloonText="CPU4 Usage: [[value]] %";
             chart.addGraph(graph4);
             
             // graph 5
             var graph5 = new AmCharts.AmGraph();
             graph5.valueAxis = valueAxis1; // we have to indicate which value axis should be used
             graph5.valueField = "Value5";
             graph5.title = "CPU5 Usage";
             graph5.bullet = "round";
             graph5.hideBulletsCount = 5; 
             graph5.type = "line";
             graph5.lineColor = "#fb8e33";
             graph5.lineThickness = "1";
             graph5.showBalloon = false;
             graph5.balloonText="CPU5 Usage: [[value]] %";
             chart.addGraph(graph5);
             
             // graph 6               
             var graph6 = new AmCharts.AmGraph();
             graph6.valueAxis = valueAxis1;
             graph6.title = "CPU6 Usage";
             graph6.valueField = "Value6";
             graph6.bullet = "round";
             graph6.hideBulletsCount = 5; 
             graph6.type = "line";
             graph6.lineColor = "#9cc239";
             graph6.lineThickness = "1";
             graph6.showBalloon = false;
             graph6.balloonText="CPU6 Usage: [[value]] %";
             chart.addGraph(graph6);

             // graph 7
             var graph7 = new AmCharts.AmGraph();
             graph7.valueAxis = valueAxis1;
             graph7.valueField = "Value7";
             graph7.title = "CPU7 Usage";
             graph7.bullet = "round";
             graph7.hideBulletsCount = 5; 
             graph7.type = "line";
             graph7.lineColor = "#998c57";
             graph7.lineThickness = "1";
             graph7.showBalloon = false;
             graph7.balloonText="CPU7 Usage: [[value]] %";
             chart.addGraph(graph7);
             
             // graph 8
             var graph8 = new AmCharts.AmGraph();
             graph8.valueAxis = valueAxis1;
             graph8.valueField = "Value8";
             graph8.title = "CPU8 Usage";
             graph8.bullet = "round";
             graph8.hideBulletsCount = 5; 
             graph8.type = "line";
             graph8.lineColor = "#d987ad";
             graph8.lineThickness = "1";
             graph8.showBalloon = false;
             graph8.balloonText="CPU8 Usage: [[value]] %";
             chart.addGraph(graph8);
             
             // graph 9
             var graph9 = new AmCharts.AmGraph();
             graph9.valueAxis = valueAxis1; 
             graph9.valueField = "Value9";
             graph9.title = "CPU9 Usage";
             graph9.bullet = "round";
             graph9.hideBulletsCount = 5; 
             graph9.type = "line";
             graph9.lineColor = "#557aa4";
             graph9.lineThickness = "1";
             graph9.showBalloon = false;
             graph9.balloonText="CPU9 Usage: [[value]] %";
             chart.addGraph(graph9);
             
          // graph 10               
             var graph10 = new AmCharts.AmGraph();
             graph10.valueAxis = valueAxis1;
             graph10.title = "CPU10 Usage";
             graph10.valueField = "Value10";
             graph10.bullet = "round";
             graph10.hideBulletsCount = 5; 
             graph10.type = "line";
             graph10.lineColor = "#d8aa3a";
             graph10.lineThickness = "1";
             graph10.showBalloon = false;
             graph10.balloonText="CPU10 Usage: [[value]] %";
             chart.addGraph(graph10);

             // graph 11
             var graph11 = new AmCharts.AmGraph();
             graph11.valueAxis = valueAxis1;
             graph11.valueField = "Value11";
             graph11.title = "CPU11 Usage";
             graph11.bullet = "round";
             graph11.hideBulletsCount = 5; 
             graph11.type = "line";
             graph11.lineColor = "#ff8ba2";
             graph11.lineThickness = "1";
             graph11.showBalloon = false;
             graph11.balloonText="CPU11 Usage: [[value]] %";
             chart.addGraph(graph11);
             
             // graph 12
             var graph12 = new AmCharts.AmGraph();
             graph12.valueAxis = valueAxis1;
             graph12.valueField = "Value12";
             graph12.title = "CPU12 Usage";
             graph12.bullet = "round";
             graph12.hideBulletsCount = 5; 
             graph12.type = "line";
             graph12.lineColor = "#ff8ba2";
             graph12.lineThickness = "1";
             graph12.showBalloon = false;
             graph12.balloonText="CPU12 Usage: [[value]] %";
             chart.addGraph(graph12);
             
             // graph 13
             var graph13 = new AmCharts.AmGraph();
             graph13.valueAxis = valueAxis1; 
             graph13.valueField = "Value13";
             graph13.title = "CPU13 Usage";
             graph13.bullet = "round";
             graph13.hideBulletsCount = 5; 
             graph13.type = "line";
             graph13.lineColor = "#00cfbb";
             graph13.lineThickness = "1";
             graph13.showBalloon = false;
             graph13.balloonText="CPU13 Usage: [[value]] %";
              chart.addGraph(graph13);
             
          // graph 14           
             var graph14 = new AmCharts.AmGraph();
             graph14.valueAxis = valueAxis1;
             graph14.title = "CPU14 Usage";
             graph14.valueField = "Value14";
             graph14.bullet = "round";
             graph14.hideBulletsCount = 5; 
             graph14.type = "line";
             graph14.lineColor = "#bfb9da";
             graph14.lineThickness = "1";
             graph14.showBalloon = false;
             graph14.balloonText="CPU14 Usage: [[value]] %";
             chart.addGraph(graph14);

             // graph 15
             var graph15 = new AmCharts.AmGraph();
             graph15.valueAxis = valueAxis1;
             graph15.valueField = "Value15";
             graph15.title = "CPU15 Usage";
             graph15.bullet = "round";
             graph15.hideBulletsCount = 5; 
             graph15.type = "line";
             graph15.lineColor = "#e26cb4";
             graph15.lineThickness = "1";
             graph15.showBalloon = false;
             graph15.balloonText="CPU15 Usage: [[value]] %";
             chart.addGraph(graph15);
             
             // graph 16
             var graph16 = new AmCharts.AmGraph();
             graph16.valueAxis = valueAxis1;
             graph16.valueField = "Value16";
             graph16.title = "CPU16 Usage";
             graph16.bullet = "round";
             graph16.hideBulletsCount = 5; 
             graph16.type = "line";
             graph16.lineColor = "#ffb970";
             graph16.lineThickness = "1";
             graph16.showBalloon = false;
             graph16.balloonText="CPU16 Usage: [[value]] %";
             chart.addGraph(graph16);
             
             // graph 17
             var graph17 = new AmCharts.AmGraph();
             graph17.valueAxis = valueAxis1; 
             graph17.valueField = "Value17";
             graph17.title = "CPU17 Usage";
             graph17.bullet = "round";
             graph17.hideBulletsCount = 5; 
             graph17.type = "line";
             graph17.lineColor = "#28c6d1";
             graph17.lineThickness = "1";
             graph17.showBalloon = false;
             graph17.balloonText="CPU17 Usage: [[value]] %";
             chart.addGraph(graph17);
             
             // graph 18
             var graph18 = new AmCharts.AmGraph();
             graph18.valueAxis = valueAxis1;
             graph18.valueField = "Value18";
             graph18.title = "CPU18 Usage";
             graph18.bullet = "round";
             graph18.hideBulletsCount = 5; 
             graph18.type = "line";
             graph18.lineColor = "#e0bed4";
             graph18.lineThickness = "1";
             graph18.showBalloon = false;
             graph18.balloonText="CPU18 Usage: [[value]] %";
             chart.addGraph(graph18);
             
             // graph 19
             var graph19 = new AmCharts.AmGraph();
             graph19.valueAxis = valueAxis1; 
             graph19.valueField = "Value19";
             graph19.title = "CPU19 Usage";
             graph19.bullet = "round";
             graph19.hideBulletsCount = 5; 
             graph19.type = "line";
             graph19.lineColor = "#ffa98d";
             graph19.lineThickness = "1";
             graph19.showBalloon = false;
             graph19.balloonText="CPU19 Usage: [[value]] %";
             chart.addGraph(graph19);
             
          // graph 20              
             var graph20 = new AmCharts.AmGraph();
             graph20.valueAxis = valueAxis1;
             graph20.title = "CPU20 Usage";
             graph20.valueField = "Value20";
             graph20.bullet = "round";
             graph20.hideBulletsCount = 5; 
             graph20.type = "line";
             graph20.lineColor = "#c1de79";
             graph20.lineThickness = "1";
             graph20.showBalloon = false;
             graph20.balloonText="CPU20 Usage: [[value]] %";
             chart.addGraph(graph20);

             // graph 21
             var graph21 = new AmCharts.AmGraph();
             graph21.valueAxis = valueAxis1;
             graph21.valueField = "Value21";
             graph21.title = "CPU21 Usage";
             graph21.bullet = "round";
             graph21.hideBulletsCount = 5; 
             graph21.type = "line";
             graph21.lineColor = "#23adaa";
             graph21.lineThickness = "1";
             graph21.showBalloon = false;
             graph21.balloonText="CPU21 Usage: [[value]] %";
             chart.addGraph(graph21);
             
             // graph 22
             var graph22 = new AmCharts.AmGraph();
             graph22.valueAxis = valueAxis1;
             graph22.valueField = "Value22";
             graph22.title = "CPU22 Usage";
             graph22.bullet = "round";
             graph22.hideBulletsCount = 5; 
             graph22.type = "line";
             graph22.lineColor = "#cf92c4";
             graph22.lineThickness = "1";
             graph22.showBalloon = false;
             graph22.balloonText="CPU22 Usage: [[value]] %";
             chart.addGraph(graph22);
             
             // graph 23
             var graph23 = new AmCharts.AmGraph();
             graph23.valueAxis = valueAxis1; 
             graph23.valueField = "Value23";
             graph23.title = "CPU23 Usage";
             graph23.bullet = "round";
             graph23.hideBulletsCount = 5; 
             graph23.type = "line";
             graph23.lineColor = "#ff8571";
             graph23.lineThickness = "1";
             graph23.showBalloon = false;
             graph23.balloonText="CPU23 Usage: [[value]] %";
             chart.addGraph(graph23);
             
          // graph 24              
             var graph24 = new AmCharts.AmGraph();
             graph24.valueAxis = valueAxis1;
             graph24.title = "CPU24 Usage";
             graph24.valueField = "Value24";
             graph24.bullet = "round";
             graph24.hideBulletsCount = 5; 
             graph24.type = "line";
             graph24.lineColor = "#fbd958";
             graph24.lineThickness = "1";
             graph24.showBalloon = false;
             graph24.balloonText="CPU24 Usage: [[value]] %";
             chart.addGraph(graph24);

             // graph 25
             var graph25 = new AmCharts.AmGraph();
             graph25.valueAxis = valueAxis1;
             graph25.valueField = "Value25";
             graph25.title = "CPU25 Usage";
             graph25.bullet = "round";
             graph25.hideBulletsCount = 5; 
             graph25.type = "line";
             graph25.lineColor = "#64add5";
             graph25.lineThickness = "1";
             graph25.showBalloon = false;
             graph25.balloonText="CPU25 Usage: [[value]] %";
             chart.addGraph(graph25);
             
             // graph 26
             var graph26 = new AmCharts.AmGraph();
             graph26.valueAxis = valueAxis1;
             graph26.valueField = "Value26";
             graph26.title = "CPU26 Usage";
             graph26.bullet = "round";
             graph26.hideBulletsCount = 5; 
             graph26.type = "line";
             graph26.lineColor = "#f0b8d3";
             graph26.lineThickness = "1";
             graph26.showBalloon = false;
             graph26.balloonText="CPU26 Usage: [[value]] %";
             chart.addGraph(graph26);
             
             // graph 27
             var graph27 = new AmCharts.AmGraph();
             graph27.valueAxis = valueAxis1; 
             graph27.valueField = "Value27";
             graph27.title = "CPU27 Usage";
             graph27.bullet = "round";
             graph27.hideBulletsCount = 5; 
             graph27.type = "line";
             graph27.lineColor = "#fcd089";
             graph27.lineThickness = "1";
             graph27.showBalloon = false;
             graph27.balloonText="CPU27 Usage: [[value]] %";
             chart.addGraph(graph27);
             
          // graph 28         
             var graph28 = new AmCharts.AmGraph();
             graph28.valueAxis = valueAxis1;
             graph28.title = "CPU28 Usage";
             graph28.valueField = "Value28";
             graph28.bullet = "round";
             graph28.hideBulletsCount = 5; 
             graph28.type = "line";
             graph28.lineColor = "#95db7b";
             graph28.lineThickness = "1";
             graph28.showBalloon = false;
             graph28.balloonText="CPU28 Usage: [[value]] %";
             chart.addGraph(graph28);

             // graph 29
             var graph29 = new AmCharts.AmGraph();
             graph29.valueAxis = valueAxis1;
             graph29.valueField = "Value29";
             graph29.title = "CPU29 Usage";
             graph29.bullet = "round";
             graph29.hideBulletsCount = 5; 
             graph29.type = "line";
             graph29.lineColor = "#64a8b6";
             graph29.lineThickness = "1";
             graph29.showBalloon = false;
             graph29.balloonText="CPU29 Usage: [[value]] %";
             chart.addGraph(graph29);
             
             // graph 30
             var graph30 = new AmCharts.AmGraph();
             graph30.valueAxis = valueAxis1;
             graph30.valueField = "Value30";
             graph30.title = "CPU30 Usage";
             graph30.bullet = "round";
             graph30.hideBulletsCount = 5; 
             graph30.type = "line";
             graph30.lineColor = "#e26cb4";
             graph30.lineThickness = "1";
             graph30.showBalloon = false;
             graph30.balloonText="CPU30 Usage: [[value]] %";
             chart.addGraph(graph30);
             
             // graph 31
             var graph31 = new AmCharts.AmGraph();
             graph31.valueAxis = valueAxis1; 
             graph31.valueField = "Value31";
             graph31.title = "CPU31 Usage";
             graph31.bullet = "round";
             graph31.hideBulletsCount = 5; 
             graph31.type = "line";
             graph31.lineColor = "#ffb970";
             graph31.lineThickness = "1";
             graph31.showBalloon = false;
             graph31.balloonText="CPU31 Usage: [[value]] %";
             chart.addGraph(graph31);
             
          // graph 32
             var graph32 = new AmCharts.AmGraph();
             graph32.valueAxis = valueAxis1; 
             graph32.valueField = "Value32";
             graph32.title = "CPU32 Usage";
             graph32.bullet = "round";
             graph32.hideBulletsCount = 5; 
             graph32.type = "line";
             graph32.lineColor = "#e1e57c";
             graph32.lineThickness = "1";
             graph32.showBalloon = false;
             graph32.balloonText="CPU32 Usage: [[value]] %";
             chart.addGraph(graph32);
             
          // graph 33
             var graph33 = new AmCharts.AmGraph();
             graph33.valueAxis = valueAxis1; 
             graph33.valueField = "Value33";
             graph33.title = "CPU33 Usage";
             graph33.bullet = "round";
             graph33.hideBulletsCount = 5; 
             graph33.type = "line";
             graph33.lineColor = "#6c96ca";
             graph33.lineThickness = "1";
             graph33.showBalloon = false;
             graph33.balloonText="CPU33 Usage: [[value]] %";
             chart.addGraph(graph33);
             
          // CURSOR
             var chartCursor = new AmCharts.ChartCursor();
             chartCursor.cursorPosition = "middle";
             chartCursor.categoryBalloonDateFormat = "YYYY-MM-DD JJ:NN:SS";
             chartCursor.zoomable = true;
             chartCursor.cursorColor= "#0f47c7";
             chart.addChartCursor(chartCursor);

             // SCROLLBAR (상단의 확대 축소 바)
             var chartScrollbar = new AmCharts.ChartScrollbar();
             chart.addChartScrollbar(chartScrollbar);
             chartScrollbar.dragIconHeight =  20;
             chartScrollbar.dragIconWidth = 20;
             chartScrollbar.scrollbarHeight = 10;           
                         
          // LEGEND
             var legend = new AmCharts.AmLegend();
             legend.align = "center";            
             legend.markerLabelGap = 5;           
             legend.position = "bottom";
             legend.valueWidth = 20;
             legend.verticalGap = 1;
             legend.valueText = "[[value]]";
             legend.markerSize = 11;             
             chart.addLegend(legend);

             // WRITE
             chart.write(chartname);          

         // this method is called when chart is first inited as we listen for "dataUpdated" event
	         function zoomChart()
	         {
	             // different zoom methods can be used - zoomToIndexes, zoomToDates, zoomToCategoryValues
	             chart.zoomToIndexes(10, 20);
	         }             
		}		
	},
	
	/*
	 * 모듈명 : OBVsStatusChartViewer
	 * 성격 : 정상, 단절 , 꺼짐 3개 Value Line Chart, 특수 Chart , legend 존재
	 * 매개변수 : chartDatalist, chartOptionList (min, max, linecolor1, linecolor2, linecolor3, chartname, axistitle, maxPos, cursorColor)
	 * 사용처 : 대시보드 위젯 ( VS 상태 변경 )
	 * */
	OBVsStatusChartViewer : function(chartDatalist, chartOptionList)
	{
		with (this) 
		{          
			var chartData = chartDatalist;
			var chartOption = chartOptionList;

             // SERIAL CHART 옵션
             var chart = new AmCharts.AmSerialChart();
             chart.pathToImages = "/js/extern/amcharts_images/";             
             chart.dataProvider = chartData;
             chart.categoryField = "occurredTime";
             chart.gridAboveGraphs= "true";   

             // 대상이 없을때 보여지는 챠트  양 사이드 border            
             chart.plotAreaBorderAlpha =  1;
             chart.plotAreaBorderColor =  '#e1e1e1'; 
             
             chart.autoMargins =  false;
             chart.columnsAboveLines = true;
             chart.marginTop = 5;
             chart.marginBottom = 19;
             chart.marginLeft = 50;
             chart.marginRight = 20;             
             
             chart.fontFamily = "Arial";
             chart.fontSize = 10;
             chart.zoomOutButton =
             {
	             backgroundColor: '#000000',
	             backgroundAlpha: 0.05,
	             fontSize : 9
             };
             chart.zoomOutButtonImageSize = 14;
             chart.usePrefixes = true;
             chart.prefixesOfBigNumbers = [{number:1e+3,prefix:"k"},
                                           {number:1e+6,prefix:"M"},
                                           {number:1e+9,prefix:"G"}];
             // 처음 로드 화면에 zoom 기능을 사용하고 싶다면 ("dataUpdated", zoomChart); 로 add 후 조절은 zoomChart 에서한다.
             //chart.addListener("dataUpdated", zoomChart);

             // AXES
             // category axis (on the down)               
             var categoryAxis = chart.categoryAxis;
             categoryAxis.parseDates = true; // as our data is date-based, we set parseDates to true
             categoryAxis.minPeriod = "ss"; // our data is daily, so we set minPeriod to DD
             categoryAxis.dashLength = 2;
             categoryAxis.dateFormats = [{period:'ss',format:'JJ:NN:SS'},
                                         {period:'mm',format:'JJ:NN'},
                                         {period:'hh',format:'JJ:NN'},
                                         {period:'DD',format:'YYYY-MM-DD'},
                                         {period:'WW',format:'YYYY-MM-DD'},
                                         {period:'MM',format:'MM'},
                                         {period:'YYYY',format:'YYYY'}];
             categoryAxis.gridAlpha = 0.3;             

             // value axis (on the left)
             var valueAxis1 = new AmCharts.ValueAxis();
             valueAxis1.title = "개";
             valueAxis1.titleColor = "#666666";             
             valueAxis1.stackType = "none";
             valueAxis1.tickLength = 0;
             valueAxis1.dashLength = 2;
             valueAxis1.gridAlpha = 0.4; 
          //   valueAxis1.axisColor = chartOption.linecolor1;
             valueAxis1.axisThickness = 1;
             valueAxis1.minimum = chartOption.min;
             if (chartOption.max != null)
             {
            	 valueAxis1.maximum = chartOption.max;
             }
             valueAxis1.usePrefixes = true;
             valueAxis1.prefixesOfBigNumbers = [{number:1e+3,prefix:"k"},
                                                {number:1e+6,prefix:"M"},
                                                {number:1e+9,prefix:"G"}];
             chart.addValueAxis(valueAxis1);

             // GRAPHS
             //  graph 1
             var graph1 = new AmCharts.AmGraph();
             graph1.valueAxis = valueAxis1; // we have to indicate which value axis should be used
             graph1.title = VAR_COMMON_ONLINE;
             graph1.valueField = "firstValue";
             graph1.bullet = "round";
             graph1.hideBulletsCount = 5; 
             graph1.type = "line";
             //graph1.fillAlphas = 0.4;
             graph1.lineColor = chartOption.linecolor1;
             graph1.lineThickness = "1";
             graph1.balloonText= VAR_COMMON_ONLINE + ": [[value]]" + chartOption.axistitle;
            // graph1.fillColors = "#C0CEE9";
             
             graph1.connect = false;
             graph1.gapPeriod = chartOption.interval;
             chart.addGraph(graph1);
             
             // graph 2               
             var graph2 = new AmCharts.AmGraph();
             graph2.valueAxis = valueAxis1; // we have to indicate which value axis should be used
             graph2.title = VAR_COMMON_OFFLINE;
             graph2.valueField = "secondValue";
             graph2.bullet = "round";
             graph2.hideBulletsCount = 5; 
             graph2.type = "line";
            //graph2.fillAlphas = 0.4;
             graph2.lineColor = chartOption.linecolor2;
             graph2.lineThickness = "1";
             graph2.balloonText= VAR_COMMON_OFFLINE + ": [[value]]" + chartOption.axistitle;
            // graph1.fillColors = "#C0CEE9";
             
             graph2.connect = false;
             graph2.gapPeriod = chartOption.interval;
             chart.addGraph(graph2);  
             
             // graph 3               
             var graph3 = new AmCharts.AmGraph();
             graph3.valueAxis = valueAxis1; // we have to indicate which value axis should be used
             graph3.title = VAR_COMMON_DISABLED;
             graph3.valueField = "thirdValue";
             graph3.bullet = "round";
             graph3.hideBulletsCount = 5; 
             graph3.type = "line";
            //graph3.fillAlphas = 0.4;
             graph3.lineColor = chartOption.linecolor3;
             graph3.lineThickness = "1";
             graph3.balloonText= VAR_COMMON_DISABLED + ": [[value]]" + chartOption.axistitle;
            // graph1.fillColors = "#C0CEE9";
             
             graph3.connect = false;
             graph3.gapPeriod = chartOption.interval;
             chart.addGraph(graph3);

             // CURSOR
             var chartCursor = new AmCharts.ChartCursor();
             chartCursor.cursorPosition = "middle";
             chartCursor.categoryBalloonDateFormat = "YYYY-MM-DD JJ:NN:SS";
             chartCursor.zoomable = true;
             chartCursor.cursorColor= chartOption.cursorColor;
             chart.addChartCursor(chartCursor);       

             // SCROLLBAR (상단의 확대 축소 바)
             var chartScrollbar = new AmCharts.ChartScrollbar();
             chart.addChartScrollbar(chartScrollbar);
             chartScrollbar.dragIconHeight =  20;
             chartScrollbar.dragIconWidth = 20;
             chartScrollbar.scrollbarHeight = 10;            
             
             // Balloon             
             var balloon = chart.balloon;
             balloon.adjustBorderColor = true;
             balloon.pointerWidth = 3;
             balloon.shadowAlpha = 0; 
             balloon.borderThickness = 1;
             balloon.fillAlpha = 0.95;
             balloon.horizontalPadding = 6;
             balloon.verticalPadding = -2; 
             balloon.fontSize = 8;               

          // LEGEND
             var legend = new AmCharts.AmLegend();
             legend.autoMargins = false;
             legend.marginBottom =  -5;
             legend.marginTop =  -1;              
             legend.align = "center";            
             legend.markerLabelGap = 5;           
             legend.position = "bottom";
             legend.valueWidth = 20;
             legend.verticalGap = 1;
             legend.valueText = "[[value]]";
             legend.markerSize = 11;
             chart.addLegend(legend);
             
             // WRITE
             chart.write(chartOption.chartname);          

         // this method is called when chart is first inited as we listen for "dataUpdated" event
	         function zoomChart()
	         {
	             // different zoom methods can be used - zoomToIndexes, zoomToDates, zoomToCategoryValues
	             chart.zoomToIndexes(10, 20);
	         }
	         return chart;
		}		
	},
	/*
	 * 모듈명 : OBSystemCpuMemoryChart
	 * 성격 :  Fill Alpha(30%) chart, 2 Guide
	 * 매개변수 : chartDatalist, chartOptionList ( min, max, linecolor, chartname, axistitle, maxPos )
	 * 사용처 : 1. 시스템정보 (CPU, Memory)
	 *
	 * */
	OBSystemCpuMemoryChart: function(chartDatalist, chartOptionList)
	{
		with (this) 
		{   
			 var chartData = chartDatalist;
			 var chartOption = chartOptionList;
			 
             // SERIAL CHART 옵션
             var chart = new AmCharts.AmSerialChart();
             chart.pathToImages = "/js/extern/amcharts_images/";             
             chart.dataProvider = chartData;
             chart.categoryField = "occurredTime";
             chart.gridAboveGraphs= "true"; 
             
             // 대상이 없을때 보여지는 챠트  양 사이드 border            
             chart.plotAreaBorderAlpha =  1;
             chart.plotAreaBorderColor =  '#e1e1e1'; 
             
             chart.autoMargins =  false;
             chart.columnsAboveLines = true;
             chart.marginTop = 14;
             chart.marginBottom = 25;
             chart.marginLeft = 50;
             
             chart.fontFamily = "Arial";
             chart.fontSize = 10;
             chart.zoomOutButton =
             {
	             backgroundColor: '#000000',
	             backgroundAlpha: 0.05,
	             fontSize : 9
             };
             chart.zoomOutButtonImageSize = 14;
             chart.usePrefixes = true;
             chart.prefixesOfBigNumbers = [{number:1e+3,prefix:"k"},
                                           {number:1e+6,prefix:"M"},
                                           {number:1e+9,prefix:"G"}];
             // 처음 로드 화면에 zoom 기능을 사용하고 싶다면 ("dataUpdated", zoomChart); 로 add 후 조절은 zoomChart method 에서한다.
             //chart.addListener("dataUpdated", zoomChart);

             // Axes
             // X축 Category Axis (on the down)               
             var categoryAxis = chart.categoryAxis;
             categoryAxis.parseDates = true; // as our data is date-based, we set parseDates to true
             categoryAxis.minPeriod = "ss"; // our data is daily, so we set minPeriod to DD
             categoryAxis.dashLength = 2;
             categoryAxis.dateFormats = [{period:'ss',format:'JJ:NN:SS'},
                                         {period:'mm',format:'JJ:NN'},
                                         {period:'hh',format:'JJ:NN'},
                                         {period:'DD',format:'YYYY-MM-DD'},
                                         {period:'WW',format:'YYYY-MM-DD'},
                                         {period:'MM',format:'MM'},
                                         {period:'YYYY',format:'YYYY'}];
             categoryAxis.gridAlpha = 0.3;             

             // Y축 value axis (on the left)
             var valueAxis = new AmCharts.ValueAxis();
             valueAxis.stackType = "none";
             valueAxis.tickLength = 0;
             valueAxis.dashLength = 2;
             valueAxis.gridAlpha = 0.3; 
             // valueAxis.axisColor = chartOption.linecolor;
             valueAxis.axisThickness = 1;
             valueAxis.usePrefixes = true;
             valueAxis.prefixesOfBigNumbers = [
                                                {number:1e+3,prefix:"k"},
                                                {number:1e+6,prefix:"M"},
                                                {number:1e+9,prefix:"G"}];
             valueAxis.minimum = chartOption.min;
             if (chartOption.max != null)
             {
            	 valueAxis.maximum = chartOption.max;             
             }
             valueAxis.title = chartOption.axistitle;
             valueAxis.titleBold = false;
             valueAxis.titleColor = "#666666";
             chart.addValueAxis(valueAxis);             

             // GRAPHS
             //  graph One Value
             var graph = new AmCharts.AmGraph();
             graph.valueAxis = valueAxis; // we have to indicate which value axis should be used
             graph.title = "firstValue";
             graph.valueField = "firstValue";
             graph.bullet = "round";
             graph.hideBulletsCount = 5; 
             graph.type = "line";
             graph.fillAlphas = 0.3;
             graph.lineColor = chartOption.linecolor;
             graph.lineThickness = "1";
             graph.balloonText="[[value]]" + chartOption.axistitle;
             graph.connect = false;
             graph.gapPeriod = chartOption.interval;
             chart.addGraph(graph);                        

             // CURSOR
             var chartCursor = new AmCharts.ChartCursor();
             chartCursor.cursorPosition = "middle";
             chartCursor.categoryBalloonDateFormat = "YYYY-MM-DD JJ:NN:SS";
             chartCursor.zoomable = true;
             chartCursor.cursorColor= chartOption.cursorColor;
             chart.addChartCursor(chartCursor);
 
             // SCROLLBAR (상단의 확대 축소 바)
             var chartScrollbar = new AmCharts.ChartScrollbar();
             chart.addChartScrollbar(chartScrollbar);
             chart.addChartScrollbar(chartScrollbar);
             chartScrollbar.dragIconHeight =  20;
             chartScrollbar.dragIconWidth = 20;
             chartScrollbar.scrollbarHeight = 10;             
             
             // Balloon             
             var balloon = chart.balloon;
             balloon.adjustBorderColor = true;
             balloon.pointerWidth = 3;
             balloon.shadowAlpha = 0; 
             balloon.borderThickness = 1;
             balloon.fillAlpha = 1;
             balloon.horizontalPadding = 6;
             balloon.verticalPadding = -2; 
             balloon.fontSize = 8;  
             
             if (chartOption.maxPos != null && chartOption.maxPos > 0)
             {
            	 var guide = new AmCharts.Guide();
                 guide.value = chartOption.maxPos; 
                 guide.lineColor = "#CC0000";
                 //guide.dashLength = 5;
                 guide.label = "Max : " + addCommaforNumber(chartOption.maxPos) + chartOption.axistitle;               
                 guide.position = "left";
                 guide.boldLabel = true;
                 guide.inside = true;
                 guide.lineAlpha = 1;                 
                 valueAxis.addGuide(guide);
                 guide = null;
             }
             if (chartOption.avgPos != null && chartOption.avgPos > 0)
             {
            	 var guide = new AmCharts.Guide();
                 guide.value = chartOption.avgPos; 
                 guide.lineColor = "blue";
                 //guide.dashLength = 5;
                 guide.label = "Avg : " + addCommaforNumber(chartOption.avgPos) + chartOption.axistitle;               
                 guide.position = "right";
                 guide.boldLabel = true;
                 guide.inside = true;
                 guide.lineAlpha = 1;                 
                 valueAxis.addGuide(guide);
                 guide = null;
             }

             // WRITE
             chart.write(chartOption.chartname);          

         // this method is called when chart is first inited as we listen for "dataUpdated" event
	         function zoomChart()
	         {
	             // different zoom methods can be used - zoomToIndexes, zoomToDates, zoomToCategoryValues
	             chart.zoomToIndexes(10, 20);
	         }
	         return chart;
		}
	},
	OBNewSystemDatabaseChartMaker : function(chartDatalist, chartname)
	{
		with(this)
		{
			AmCharts.makeChart(chartname,
					{
						"type": "serial",
						"pathToImages": "js/extern/amcharts_images/",
						"categoryField": "category",
						"rotate": true,
						"gridAboveGraphs": true,						
						"autoMargins": false,
						"marginBottom": 50,
						"marginRight": 17,
						"marginTop": 32,
						"plotAreaBorderAlpha": 0.18,						
						"theme": "none",
						"usePrefixes": true,
						"gridAboveGraphs": true,
						"categoryAxis":
						{
							"gridAlpha": 0,
							"showFirstLabel": false
						},
						"allLabels":
						[{
								"align": "right",
								"alpha": 0.61,
								"bold": true,
								"id": "Label-1",
								"text": chartDatalist.TotalUsedSize,
								"x": "99%",
								"y": "87%"
						}],						
						"dataProvider":
						[{
								"category": "category 1",
								"column-1": chartDatalist.LogUsage,
								"column-2": chartDatalist.IndexeUsage,
								"column-3": chartDatalist.GeneralUsage,
								"column-4": chartDatalist.EtcUsage,
						}], // 1. Log , 2.Index, 3.General, 4.Etc						             
						"graphs":
						[{
								"balloonText": "[[title]]: "+ chartDatalist.LogSize +"([[value]]%)",
								"columnWidth": 0.45,					
								"fillAlphas": 1,
								"lineAlpha": 1,
								"fillColors": "#6cb8c8",
								"lineColor": "#6cb8c8",
								"id": "AmGraph-1",
								"labelText": "",							
								"minDistance": 0,								
								"noStepRisers": true,
								"showBalloon": true,
								"title": "LOG",
								"type": "column",
								"valueField": "column-1"
							},
							{
								"balloonText": "[[title]]: "+ chartDatalist.IndexSize + "([[value]]%)",
								"columnWidth": 0.45,
								"fillAlphas": 1,
								"lineAlpha": 1,
								"fillColors": "#fbc51a",
								"lineColor": "#fbc51a",
								"id": "AmGraph-2",
								"labelText": "",
								"minDistance": 0,
								"noStepRisers": true,
								"showBalloon": true,
								"title": "INDEX",
								"type": "column",
								"valueField": "column-2"
							},
							{
								"balloonText": "[[title]]: " + chartDatalist.GeneralSize + "([[value]]%)",
								"columnWidth": 0.45,
								"fillAlphas": 1,
								"lineAlpha": 1,
								"fillColors": "#d65f3d",
								"lineColor": "#d65f3d",
								"id": "AmGraph-3",
								"labelText": "",
								"minDistance": 0,
								"noStepRisers": true,
								"showBalloon": true,
								"title": "GENERAL",
								"type": "column",
								"valueField": "column-3"
							},
							{
								"balloonText": "[[title]]: " + chartDatalist.EtcSize + "([[value]]%)",
								"columnWidth": 0.45,
								"fillAlphas": 1,
								"lineAlpha": 1,
								"fillColors": "#976e96",
								"lineColor": "#976e96",
								"id": "AmGraph-4",
								"labelText": "",
								"minDistance": 0,
								"noStepRisers": true,
								"showBalloon": true,
								"title": "ETC",
								"type": "column",
								"valueField": "column-4"
							}],						
						"valueAxes":
						[{
								"axisTitleOffset": 2,
								"id": "ValueAxis-1",
								"maximum": 100,
								"minimum": 0,
								"position": "bottom",
								"radarCategoriesEnabled": false,
								"stackType": "100%",
								"unit": "%",
								"dashLength": 2,
								"gridAlpha": 0.3,
								"labelOffset": -2,
								"minHorizontalGap": 74,
								"minVerticalGap": 130,
								"tickLength": 4,
								"title": "",
								"titleBold": false
						}]
					}
				);
		}
	},
	OBNewSystemHDDChartMaker : function(chartDatalist, chartname)
	{
		with(this)
		{
			AmCharts.makeChart(chartname,
					{
						"type": "serial",
						"pathToImages": "js/extern/amcharts_images/",
						"categoryField": "category",
						"rotate": true,
						"gridAboveGraphs": true,
						"autoMargins": false,
						"marginBottom": 50,
						"marginRight": 17,
						"marginTop": 32,
						"plotAreaBorderAlpha": 0.18,						
						"theme": "none",
						"usePrefixes": true,
						"categoryAxis":
						{
							"gridAlpha": 0,
							"showFirstLabel": false
						},
						"allLabels":
						[{
								"align": "right",
								"alpha": 0.61,
								"bold": true,
								"id": "Label-1",
								"text": chartDatalist.totalSize,
								"x": "99%",
								"y": "87%"
						}],						
						"dataProvider":
						[{
								"category": "category 1",
								"column-1": chartDatalist.usedUsage,
								"column-2": chartDatalist.freeUsage								
						}], // 1. Used , 2.Free
						
							"guides":
								[{
									"fillAlpha": 0.13,
									"fillColor": "#FFE142",
									"id": "Guide-2",
									"inside": false,
									"label": "Warning",
									"lineAlpha": 1,
									"lineColor": "#F8DF0A",
									"position": "top",
									"tickLength": -1,
									"toValue": 70,
									"value": 90
								},
								{
									"angle": 0,
									"balloonText": "",
									"fillAlpha": 0.04,
									"fillColor": "#FF0000",
									"id": "Guide-3",
									"inside": false,
									"label": "Danger",
									"lineAlpha": 1,
									"lineColor": "#DE0A0A",
									"position": "top",
									"tickLength": 0,
									"toValue": 90,
									"value": 100
								},
								 {
									"angle": 0,
									"balloonColor": "#FF0000",
									"balloonText": "",
									"dashLength": 0,
									"date": "",
									"fillAlpha": 0.04,
									"id": "Guide-4",
									"inside": true,
									"label": "   (1 month: "+ chartDatalist.before30Size +" Increase)",
									"labelRotation": 0,
									"lineAlpha": 0,
									"lineThickness": 1,
									"position": "left",
									"tickLength": -1,
									"toAngle": 0,
									"toValue": chartDatalist.before30Usage,
									"value": chartDatalist.usedUsage
								}],						
						"graphs":
						[{
								"balloonText": "[[title]]: "+ chartDatalist.usedSize +"([[value]]%)",
								"columnWidth": 0.45,					
								"fillAlphas": 1,
								"lineAlpha": 1,
								"fillColors": "#6cb8c8",
								"lineColor": "#6cb8c8",
								"id": "AmGraph-1",
								"labelText": "",							
								"minDistance": 0,								
								"noStepRisers": true,
								"showBalloon": true,
								"title": "Used",
								"type": "column",
								"valueField": "column-1"
							},
							{
								"balloonText": "[[title]]: "+ chartDatalist.freeSize +"([[value]]%)",
								"columnWidth": 0.45,
								"fillAlphas": 1,
								"lineAlpha": 1,
								"fillColors": "#FFFFFF",
								"lineColor": "#dedddd",
								"id": "AmGraph-2",
								"labelText": "",
								"minDistance": 0,
								"noStepRisers": true,
								"showBalloon": true,
								"title": "Free",
								"type": "column",
								"valueField": "column-2"
							}],						
						"valueAxes":
						[{
								"axisTitleOffset": 2,
								"id": "ValueAxis-1",
								"maximum": 100,
								"minimum": 0,
								"position": "bottom",
								"radarCategoriesEnabled": false,
								"stackType": "100%",
								"unit": "%",
								"dashLength": 2,
								"gridAlpha": 0.25,
								"labelOffset": -2,
								"minHorizontalGap": 74,
								"minVerticalGap": 130,
								"tickLength": 4,
								"title": "",
								"titleBold": false
						}]
					}
				);
		}
	}
});