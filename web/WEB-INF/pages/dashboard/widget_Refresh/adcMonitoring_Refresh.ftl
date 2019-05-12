<#setting number_format="0.####">
	<div class="portlet-header ui-widget-header ui-corner-all">${(widgetInfo.name)!''} -
		<span class="sub_title"> ${(widgetTarget.name)!''}</span>
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
	<div class="portlet-content">	
		<div class="adcmonitoring">
			<#setting number_format="">
			<div class="Total">${(statusSummary.adcTotalCount)!0}</div>
			<#if statusSummary.adcAvailable10Count == 1>
				<div class="Green_10">${(statusSummary.adcAvailableCount)!0}</div>
			<#elseif statusSummary.adcAvailable10Count == 2>
				<div class="Green_20">${(statusSummary.adcAvailableCount)!0}</div>
			<#elseif statusSummary.adcAvailable10Count == 3>
				<div class="Green_30">${(statusSummary.adcAvailableCount)!0}</div>
			<#elseif statusSummary.adcAvailable10Count == 4>
				<div class="Green_40">${(statusSummary.adcAvailableCount)!0}</div>
			<#elseif statusSummary.adcAvailable10Count == 5>
				<div class="Green_50">${(statusSummary.adcAvailableCount)!0}</div>
			<#elseif statusSummary.adcAvailable10Count == 6>
				<div class="Green_60">${(statusSummary.adcAvailableCount)!0}</div>
			<#elseif statusSummary.adcAvailable10Count == 7>
				<div class="Green_70">${(statusSummary.adcAvailableCount)!0}</div>
			<#elseif statusSummary.adcAvailable10Count == 8>
				<div class="Green_80">${(statusSummary.adcAvailableCount)!0}</div>
			<#elseif statusSummary.adcAvailable10Count == 9>
				<div class="Green_90">${(statusSummary.adcAvailableCount)!0}</div>
			<#elseif statusSummary.adcAvailable10Count == 10>
				<div class="Green_100">${(statusSummary.adcAvailableCount)!0}</div>
			<#else>
				<div class="Green_0">${(statusSummary.adcAvailableCount)!0}</div>
			</#if>
			
			<#if statusSummary.adcUnavailable10Count == 1>
				<div class="Gray_10">${(statusSummary.adcUnavailableCount)!0}</div>
			<#elseif statusSummary.adcUnavailable10Count == 2>
				<div class="Gray_20">${(statusSummary.adcUnavailableCount)!0}</div>
			<#elseif statusSummary.adcUnavailable10Count == 3>
				<div class="Gray_30">${(statusSummary.adcUnavailableCount)!0}</div>
			<#elseif statusSummary.adcUnavailable10Count == 4>
				<div class="Gray_40">${(statusSummary.adcUnavailableCount)!0}</div>
			<#elseif statusSummary.adcUnavailable10Count == 5>
				<div class="Gray_50">${(statusSummary.adcUnavailableCount)!0}</div>
			<#elseif statusSummary.adcUnavailable10Count == 6>
				<div class="Gray_60">${(statusSummary.adcUnavailableCount)!0}</div>
			<#elseif statusSummary.adcUnavailable10Count == 7>
				<div class="Gray_70">${(statusSummary.adcUnavailableCount)!0}</div>
			<#elseif statusSummary.adcUnavailable10Count == 8>
				<div class="Gray_80">${(statusSummary.adcUnavailableCount)!0}</div>
			<#elseif statusSummary.adcUnavailable10Count == 9>
				<div class="Gray_90">${(statusSummary.adcUnavailableCount)!0}</div>
			<#elseif statusSummary.adcUnavailable10Count == 10>
				<div class="Gray_100">${(statusSummary.adcUnavailableCount)!0}</div>
			<#else>
				<div class="Gray_0">${(statusSummary.adcUnavailableCount)!0}</div>
			</#if>			
			
			<br class="clearfix">						
			<div class="title">${LANGCODEMAP["MSG_DASHBOARD_ALL"]!}</div>
			<div class="title">${LANGCODEMAP["MSG_DASHBOARD_ENABLE"]!}</div>	
			<div class="title">${LANGCODEMAP["MSG_DASHBOARD_DISABLE"]!}</div>							
		</div>			
	</div>