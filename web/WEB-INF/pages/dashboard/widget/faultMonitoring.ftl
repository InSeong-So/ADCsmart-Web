<#setting number_format="0.####">
<div class="portlet ui-widget ui-widget-content ui-helper-clearfix ui-corner-all" id="${(widgetInfo.index)!''}">
	<div class="portlet-header ui-widget-header ui-corner-all">${(widgetInfo.name)!''} -
	<span class="sub_title"> ${(widgetTarget.name)!''}</span>
	
	<#if (widgetInfo.moreInfoIndex) == 0>
		<span class="sub_title">[${LANGCODEMAP["MSG_WIDGET_ALL"]!}]</span>
	<#elseif (widgetInfo.moreInfoIndex) == 1>
		<span class="sub_title">[${LANGCODEMAP["MSG_WIDGET_ONE_DAY"]!}]</span>
	<#elseif (widgetInfo.moreInfoIndex) == 7>
		<span class="sub_title">[${LANGCODEMAP["MSG_WIDGET_SEVEN_DAY"]!}]</span>
	<#elseif (widgetInfo.moreInfoIndex) == 15>
		<span class="sub_title">[${LANGCODEMAP["MSG_WIDGET_FIFTEEN_DAY"]!}]</span>
	<#elseif (widgetInfo.moreInfoIndex) == 30>
		<span class="sub_title">[${LANGCODEMAP["MSG_WIDGET_THIRTY_DAT"]!}]</span>
	</#if>
		<a class="" href="#"><span class="ui-icon ui-icon-closethick "></span></a>
		<a class="" href="#"><span class="ui-icon ui-icon-gear "></span></a>
		<div style="display: none;">
			<input name="widgetInfo.name" class="widgetName" type="text" value="${widgetInfo.name!''}"/>
			<input name="widgetInfo.type" class="widgetType" type="text" value="${widgetInfo.type!''}"/>
			<input name="widgetInfo.width" class="widgetWidth" type="text" value="${widgetInfo.width!''}"/>
			<input name="widgetInfo.height" class="widgetHeight" type="text" value="${widgetInfo.height!''}"/>
			<input name="widgetInfo.xPosition" class="widgetxPosition" type="text" value="${widgetInfo.xPosition!''}"/>
			<input name="widgetInfo.yPosition" class="widgetyPosition" type="text" value="${widgetInfo.yPosition!''}"/>
			<input name="widgetInfo.moreInfoIndex" class="moreInfoIndex" type="text" value="${widgetInfo.moreInfoIndex!''}"/>
			
			<input name="widgetTarget.category" class="targetCategory" type="text" value="${widgetTarget.category!''}"/>
			<input name="widgetTarget.index" class="targetIndex" type="text" value="${widgetTarget.index!''}"/>
			<input name="widgetTarget.strIndex" class="targetStrIndex" type="text" value="${widgetTarget.strIndex!''}"/>
			<input name="widgetTarget.name" class="targetName" type="text" value="${widgetTarget.name!''}"/>			
			<input name="widgetTarget.desciption" class="targetDesciption" type="text" value="${widgetTarget.desciption!''}"/>
			
			<input name="widgetItem.widthMinSize" class="widthMinSize" type="text" value="${widgetItem.widthMinSize!''}"/>
			<input name="widgetItem.widthMaxSize" class="widthMaxSize" type="text" value="${widgetItem.widthMaxSize!''}"/>
			<input name="widgetItem.heightMinSize" class="heightMinSize" type="text" value="${widgetItem.heightMinSize!''}"/>
			<input name="widgetItem.heightMaxSize" class="heightMaxSize" type="text" value="${widgetItem.heightMaxSize!''}"/>
		</div>
	</div>
	<div class="portlet-content1">	
	<!--<div id="" class="tabbg">
		 <ul class="Duration_tabs">	
	   		<li>
	  			<a class="adcSummary css_textCursor" id="responsChartTap">
	  				<span class="none status">4</span>최근 30일
	  			</a>
	  		</li>	
	   		<li>
	  			<a class="adcSummary css_textCursor" id="responsChartTap">
	  				<span class="none status">3</span>최근 15일
	  			</a>
	  		</li>	
	   		<li>
	  			<a class="adcSummary css_textCursor" id="responsChartTap">
	  				<span class="none status">2</span>최근 7일
	  			</a>
	  		</li>
	   		<li>
	  			<a class="adcSummary css_textCursor" id="responsChartTap">
	  				<span class="none status">0</span>최근 1일
	  			</a>
	  		</li>	  			  		  				  		
	   		<li>
	   			<a class="adcSummary css_textCursor" id="bpsConnChartTap" style="background-color: #f3f3f3;">
	   				<span class="none status">1</span>전체
	   			</a>
	   		</li>	  			
		</ul>
	
	</div> -->
		<div class="faultmonitoring">
						<#setting number_format="">
						<div class="one">${(statusSummary.faultWarning)!0}</div>
						<div class="two">${(statusSummary.faultSolvedCount)!0}</div>
						<div class="three">${(statusSummary.faultUnsolvedCount)!0}</div>		
		</div>			
	</div>
</div>