var TestUI = Class.create({
	initialize : function() 
	{
		this.ajaxManager = new FlowitAjax();
		this.OBajaxManager = new OBAjax();
		this.isSortable = undefined;
		this.sortableNumber = undefined;
		this.domCount = undefined;
	},
	load : function()
	{
		with (this)
		{
			OBajaxManager.runJson({
				url	: '/testUI/loadSortableInfo.action',
				data :
				{
				},
				successFn : function(data) 
				{
					isSortable = data.isSortable;
					sortableNumber = data.sortableNum;					
					loadtestUI();
				}				
			});
		}
	},
	loadtestUI :function(params)
	{
		with (this)
		{
			OBajaxManager.runHtmlExt({
				url			: '/testUI/loadtestUI.action',
				target		: '.testUI_page',
				data		: params,
				successFn	: function(params) 
				{
					if(isSortable == true)
					{
						onLoadSortable();
					}
					jqueryUI();
					GenerateChart();
					clickEvent();
				}			
			});
		}
	},
	clickEvent : function()
	{
		with (this)
		{
			$('#refresh').click(function(event)
			{					
				event.preventDefault();
				load();
			});
			
			$('#sortablesave').click(function(event)
			{					
				event.preventDefault();
				var newOrder = $('#col1').sortable('toArray').toString();
				var newOrder2 = $('#col2').sortable('toArray').toString();
				var newOrder3 = $('#col3').sortable('toArray').toString();
				var PositionData = (newOrder + "." + newOrder2 + "." + newOrder3);
				alert(PositionData);
				setSortableInfo(PositionData);				
			});
				
			
			$('#sizesave').click(function(event)
			{
				event.preventDefault();
				var SizeData = [];
				$('.column .portlet').each(function(index)
				{
					domCount = index;			
				});				
				
				for ( var i = 1; i <= (domCount + 1); i++)
				{
					var donWidth = $('.column #' + i ).css("width");
					var donHeight = $('.column #' + i ).css("height");
					alert(i + "번째DOM은 width : " + donWidth + "   height : " + donHeight);
					
					var dataObject =
					{
						width :	donWidth,
						height : donHeight
					};
					SizeData.push(dataObject);
				}			
			});
			$('#printpage').click(function(event)
			{
				event.preventDefault();
				//_checkExportPerformDataExist();
				var mode = "popup";
				var close = "popup";
				var extraCss = "";
				var popTitle = "adcsmart Page print preview";
				var width = 1024;
				var height = 1024;
				var strict = true;
				var options = {
								mode : mode,
								popClose : close,
								extraCss : extraCss,
								popTitle : popTitle,
								popWd : width,
								popHt : height,
								strict : strict
								};

	            $("div.printArea").printArea(options);	    				
			});
		}
	},
	onLoadSortable : function()
	{
		with (this)
		{		
			var orderArray = sortableNumber.split('.');
			var col1Array = orderArray[0].split(',');
			var col2Array = orderArray[1].split(',');
			var col3Array = orderArray[2].split(',');
			var listArray = $('.column .portlet');
			for (var i = 0; i < col1Array.length; i++)
			{
			   $('#col1').append(listArray[col1Array[i]-1]);
			}
			for (var i = 0; i < col2Array.length; i++)
			{
			   $('#col2').append(listArray[col2Array[i]-1]);
			}
			for (var i = 0; i < col3Array.length; i++)
			{
			   $('#col3').append(listArray[col3Array[i]-1]);
			}
			
			 var sizeData = [{
				 	width: "350px",
				 	height: "200px"	                
	            },
	            {
	            	width: "350px",
	            	height: "200px"	
	            },
	            {
	            	width: "284px",
	            	height: "170px"	
	            },
	            {
	            	width: "350px",
	            	height: "200px"	
	            },
	            {
	            	width: "284px",
	            	height: "80px"
	            },
	            {
		            width: "284px",
		            height: "80px"
	            },
	            {
		            width: "284px",
		            height: "196px"
		        }];
			$('.column .portlet').each(function(index)
			{
				domCount = index;			
			});			
			for ( var i = 1; i <= (domCount + 1); i++)
			{
				$('.column #' + i).css("width", sizeData[i - 1].width);
				$('.column #' + i).css("height", sizeData[i - 1].height);
				
			}
		}		
	},
	setSortableInfo : function(PositionData)
	{
		with (this)
		{
			OBajaxManager.runJson({
				url	: '/testUI/setSortableInfo.action',
				data : 
				{
					"sortableNum" : PositionData
				},
				successFn : function(data) 
				{
					alert("DOM 의 위치 저장이 완료되었습니다.");
				}				
			});
		}
	},	
	jqueryUI : function()
	{
		with (this)
		{
			$( "#col1, #col2, #col3" ).sortable(
			{
				connectWith: "#col1, #col2, #col3",				
			});	
						
			$( ".portlet" ).addClass( "ui-widget ui-widget-content ui-helper-clearfix ui-corner-all" )
							.find( ".portlet-header" )
							.addClass( "ui-widget-header ui-corner-all" )
							.prepend( "<span class='ui-icon ui-icon-minusthick'></span>");
							/*.end()
							.find( ".portlet-content" );*/			
			
			$( "#col1, #col2, #col3" ).sortable().disableSelection();			
			
			var $tabs = $( "#tabs" ).tabs();
			 
			var $tab_items = $( "ul:first li", $tabs ).droppable(
			{
				 accept: ".connectedSortable .column .portlet",
				 hoverClass: "ui-state-hover",
				 drop: function( event, ui )
				 {
					var $item = $( this );
					var $list = $( $item.find( "a" ).attr( "href" ) ).find( ".connectedSortable" );
					 
					ui.draggable.hide( "slow", function()
					{
						 $tabs.tabs( "option", "active", $tab_items.index( $item ) );
						 $( this ).appendTo( $list ).show( "slow" );
					});
				 }
			 });
					
			$( ".portlet-header .ui-icon" ).click(function()
			{
				$( this ).toggleClass("ui-icon-minusthick").toggleClass("ui-icon-plusthick");
				$( this ).parents(".portlet:first").find(".portlet-content").toggle();
			});
				$("#col1, #col2, #col3").disableSelection();
				$(".portlet").resizable();			
		}
	},
	GenerateChart : function()
	{
		testChart1();
		testChart2();
		testChart3();			
		
		 function testChart1()
         {
			 var chartData = [{
				 occurredTime: new Date (2012, 0, 11),
				 firstValue: 227	                
	            }, {
	            	occurredTime: new Date(2012, 0, 12),
					firstValue: 105
	            }, {
	            	occurredTime: new Date(2012, 0, 13),
					firstValue: 332
	            }, {
	            	occurredTime: new Date(2012, 0, 14),
					firstValue: 12
	            }, {
	            	occurredTime: new Date(2012, 0, 15),
					firstValue: 210
	            }, {
	            	occurredTime: new Date(2012, 0, 16),
					firstValue: 11
	            }, {
	            	occurredTime: new Date(2012, 0, 17),
					firstValue: 302
	            }, {
	            	occurredTime: new Date(2012, 0, 18),
					firstValue: 400
	            }, {
	            	occurredTime: new Date(2012, 0, 19),
					firstValue: 355
	            }, {
	            	occurredTime: new Date(2012, 0, 20),
					firstValue: 227
	            }, {
	            	occurredTime: new Date(2012, 0, 21),
					firstValue: 100
	            }, {
	            	occurredTime: new Date(2012, 0, 22),
					firstValue: 207
	            }, {
	            	occurredTime: new Date(2012, 0, 23),
					firstValue: 22
	            }, {
	            	occurredTime: new Date(2012, 0, 24),
					firstValue: 5
	            }];
			
			 var min = 0;
			 var max = null;
			 var linecolor = "#FF6600";
			 var chartname = "testchart1";
			 var chartunit = "cps";
			 obchart.OBOneValueChartViewer(chartData, min, max, linecolor, chartname, chartunit);
         }
		 function testChart2()
         {
			 var chartData = [{
				 occurredTime: new Date (2012, 0, 11),
				 firstValue: 02	                
	            }, {
	            	occurredTime: new Date(2012, 0, 12),
					firstValue: 33
	            }, {
	            	occurredTime: new Date(2012, 0, 13),
					firstValue: 620
	            }, {
	            	occurredTime: new Date(2012, 0, 14),
					firstValue: 10
	            }, {
	            	occurredTime: new Date(2012, 0, 15),
					firstValue: 300
	            }, {
	            	occurredTime: new Date(2012, 0, 16),
					firstValue: 227
	            }, {
	            	occurredTime: new Date(2012, 0, 17),
					firstValue: 302
	            }, {
	            	occurredTime: new Date(2012, 0, 18),
					firstValue: 400
	            }, {
	            	occurredTime: new Date(2012, 0, 19),
					firstValue: 452
	            }, {
	            	occurredTime: new Date(2012, 0, 20),
					firstValue: 227
	            }, {
	            	occurredTime: new Date(2012, 0, 21),
					firstValue: 100
	            }, {
	            	occurredTime: new Date(2012, 0, 22),
					firstValue: 258
	            }, {
	            	occurredTime: new Date(2012, 0, 23),
					firstValue: 22
	            }, {
	            	occurredTime: new Date(2012, 0, 24),
					firstValue: 5
	            }];
			
			 var min = 0;
			 var max = null;
			 var linecolor = "#FCD202";
			 var chartname = "testchart2";
			 var chartunit = "cps";
			 obchart.OBOneValueChartViewer(chartData, min, max, linecolor, chartname, chartunit);
         }
		 function testChart3()
         {
			 var chartData = [];
			 chartData.push({name: "F5", value: 50});
			 chartData.push({name: "ALTEON", value: 30});
			 chartData.push({name: "PAS", value: 15});
			 chartData.push({name: "PAS-K", value: 5});		 		
			
			 var linecolor = "#FCD202";
			 var chartname = "testchart3";
			 
			 obchart.OBPieChartViewer(chartData, linecolor, chartname);
         }		 
	}	
});