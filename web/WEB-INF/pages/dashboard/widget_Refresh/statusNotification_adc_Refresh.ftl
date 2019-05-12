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
		<div>
		<#if notifcationList??>
			<#list notifcationList as notification>
			<div class="status_notification" id="${notification.targetIndex!''}">
				<a class="adcMonitorLnk" href="#">
					<#if notification.status == 1>
						<span class="show adc_conn"></span>
					<#elseif notification.status == 2>
						<span class="show adc_caution"></span>
					<#elseif notification.status == 0>
						<span class="show adc_disconn"></span>
					<#else>
					</#if>
					<span title="${notification.targetName!""}">
						<p>${notification.targetName!''}</p>
						<p>${notification.targetIp!''}</p>
					</span>
				</a>
			</div>
			</#list>
		</#if>
	</div>
</div>