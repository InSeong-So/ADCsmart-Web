<#setting number_format="0.####">
<div class="portlet ui-widget ui-widget-content ui-helper-clearfix ui-corner-all" id="${(widgetInfo.index)!''}">
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
	<div class="portlet-content" id="div_widgetscaleF5">
		<div class="F5_3600">
			<#if faultHWStatus.cpuStatusList?has_content>
				<#if faultHWStatus.cpuStatusList[0] == 1>
					<div class="cpu01 txt cGreen">CPU1</div>
				<#elseif faultHWStatus.cpuStatusList[0] == 2>
					<div class="cpu01 txt cRed">CPU1</div>
				<#else>
					<div class="cpu01 txt cBlack">-</div>
				</#if>
				
				<#if faultHWStatus.cpuStatusList[1] == 1>
					<div class="cpu02 txt cGreen">CPU2</div>
				<#elseif faultHWStatus.cpuStatusList[1] == 2>
					<div class="cpu02 txt cRed">CPU2</div>
				<#else>
					<div class="cpu02 txt cBlack">-</div>
				</#if>				
			<#else>
				<div class="cpu01 txt cBlack">-</div>
				<div class="cpu02 txt cBlack">-</div>
			</#if>
			
			<#if faultHWStatus.hddStatus?has_content>	
				<#if faultHWStatus.hddStatus == 1>
					<div class="hdd01 hGreen">HDD ${faultHWStatus.hddUsage}%</div>	
				<#elseif faultHWStatus.hddStatus == 2>
					<div class="hdd01 hRed">HDD ${faultHWStatus.hddUsage}%</div>
				<#else>
					<div class="hdd01 hBlack">-</div>		
				</#if>
			<#else>
				<div class="hdd01 hBlack">-</div>
			</#if>		
			
			<br class="clearfix" />	
			<div class="version">11.5.0 hf2</div>
			<br class="clearfix" />	
					
			<#if faultHWStatus.portStatusList?has_content>
			<table class="porttable">	
				<tr>				
					<td>
						<#if faultHWStatus.portStatusList[0].linkStatus == 1>
							<div class="pCopper1Green"></div>
						<#elseif faultHWStatus.portStatusList[0].linkStatus == 2>
							<div class="pCopper1Red"></div>
						<#elseif faultHWStatus.portStatusList[0].linkStatus == 3>
							<div class="pCopper1Black"></div>
						<#elseif faultHWStatus.portStatusList[0].linkStatus == 5>
							<div class="pCopper1Yellow"></div>
						<#else>
							<div class="pCopper1Off"></div>
						</#if>
						
						<#if faultHWStatus.portStatusList[1].linkStatus == 1>
							<div class="pCopper2Green"></div>
						<#elseif faultHWStatus.portStatusList[1].linkStatus == 2>
							<div class="pCopper2Red"></div>
						<#elseif faultHWStatus.portStatusList[1].linkStatus == 3>
							<div class="pCopper2Black"></div>
						<#elseif faultHWStatus.portStatusList[1].linkStatus == 5>
							<div class="pCopper2Yellow"></div>
						<#else>
							<div class="pCopper2Off"></div>
						</#if>		
					</td>
					<td class="port_m58">
						<#if faultHWStatus.portStatusList[2].linkStatus == 1>
							<div class="pCopper1Green"></div>
						<#elseif faultHWStatus.portStatusList[2].linkStatus == 2>
							<div class="pCopper1Red"></div>
						<#elseif faultHWStatus.portStatusList[2].linkStatus == 3>
							<div class="pCopper1Black"></div>
						<#elseif faultHWStatus.portStatusList[2].linkStatus == 5>
							<div class="pCopper1Yellow"></div>
						<#else>
							<div class="pCopper1Off"></div>
						</#if>
						
						<#if faultHWStatus.portStatusList[3].linkStatus == 1>
							<div class="pCopper2Green"></div>
						<#elseif faultHWStatus.portStatusList[3].linkStatus == 2>
							<div class="pCopper2Red"></div>
						<#elseif faultHWStatus.portStatusList[3].linkStatus == 3>
							<div class="pCopper2Black"></div>
						<#elseif faultHWStatus.portStatusList[3].linkStatus == 5>
							<div class="pCopper2Yellow"></div>
						<#else>
							<div class="pCopper2Off"></div>
						</#if>		
					</td>
					<td>
						<#if faultHWStatus.portStatusList[4].linkStatus == 1>
							<div class="pCopper1Green"></div>
						<#elseif faultHWStatus.portStatusList[4].linkStatus == 2>
							<div class="pCopper1Red"></div>
						<#elseif faultHWStatus.portStatusList[4].linkStatus == 3>
							<div class="pCopper1Black"></div>
						<#elseif faultHWStatus.portStatusList[4].linkStatus == 5>
							<div class="pCopper1Yellow"></div>
						<#else>
							<div class="pCopper1Off"></div>
						</#if>
						
						<#if faultHWStatus.portStatusList[5].linkStatus == 1>
							<div class="pCopper2Green"></div>
						<#elseif faultHWStatus.portStatusList[5].linkStatus == 2>
							<div class="pCopper2Red"></div>
						<#elseif faultHWStatus.portStatusList[5].linkStatus == 3>
							<div class="pCopper2Black"></div>
						<#elseif faultHWStatus.portStatusList[5].linkStatus == 5>
							<div class="pCopper2Yellow"></div>
						<#else>
							<div class="pCopper2Off"></div>
						</#if>		
					</td>
					<td class="port_m58">
						<#if faultHWStatus.portStatusList[6].linkStatus == 1>
							<div class="pCopper1Green"></div>
						<#elseif faultHWStatus.portStatusList[6].linkStatus == 2>
							<div class="pCopper1Red"></div>
						<#elseif faultHWStatus.portStatusList[6].linkStatus == 3>
							<div class="pCopper1Black"></div>
						<#elseif faultHWStatus.portStatusList[6].linkStatus == 5>
							<div class="pCopper1Yellow"></div>
						<#else>
							<div class="pCopper1Off"></div>
						</#if>
						
						<#if faultHWStatus.portStatusList[7].linkStatus == 1>
							<div class="pCopper2Green"></div>
						<#elseif faultHWStatus.portStatusList[7].linkStatus == 2>
							<div class="pCopper2Red"></div>
						<#elseif faultHWStatus.portStatusList[7].linkStatus == 3>
							<div class="pCopper2Black"></div>
						<#elseif faultHWStatus.portStatusList[7].linkStatus == 5>
							<div class="pCopper2Yellow"></div>
						<#else>
							<div class="pCopper2Off"></div>
						</#if>		
								</td>								
								<td class="single_bottom">
						<#if faultHWStatus.portStatusList[8].linkStatus == 1>
							<div class="pFiber1Green"></div>
						<#elseif faultHWStatus.portStatusList[8].linkStatus == 2>
							<div class="pFiber1Red"></div>
						<#elseif faultHWStatus.portStatusList[8].linkStatus == 3>
							<div class="pFiber1Black"></div>
						<#elseif faultHWStatus.portStatusList[8].linkStatus == 5>
							<div class="pFiber1Yellow"></div>
						<#else>
							<div class="pFiber1Off"></div>
						</#if>	
					</td>
					<td class="single_bottom">
						<#if faultHWStatus.portStatusList[9].linkStatus == 1>
							<div class="pFiber1Green"></div>
						<#elseif faultHWStatus.portStatusList[9].linkStatus == 2>
							<div class="pFiber1Red"></div>
						<#elseif faultHWStatus.portStatusList[9].linkStatus == 3>
							<div class="pFiber1Black"></div>
						<#elseif faultHWStatus.portStatusList[9].linkStatus == 5>
							<div class="pFiber1Yellow"></div>
						<#else>
							<div class="pFiber1Off"></div>
						</#if>		
					</td>			
					<td class="mgmt_w230">
						<#if faultHWStatus.portStatusList?contains("mgmt")>
							<#list faultHWStatus.portStatusList as list>
								<#if list.intfName == "mgmt" >
									<#if list.linkStatus == 1>
										<div class="pCopper1Green"></div>	
									<#elseif list.linkStatus == 2>
										<div class="pCopper1Red"></div>	
									<#elseif list.linkStatus == 3>
										<div class="pCopper1Black"></div>	
									<#elseif list.linkStatus == 5>
										<div class="pCopper1Yellow"></div>
									<#else>
										<div class="pCopper1Off"></div>
									</#if>	
								</#if>
							</#list>
						<#else>
							<div class="pCopper1Off"></div>
						</#if>		
					</td>																																								
				</tr>
			</table>								
		
			<#else>
			
			<table class="porttable">	
				<tr>				
					<td>
						<div class="pCopper1Off"></div>
						<div class="pCopper2Off"></div>		
					</td>
					<td class="port_m58">
						<div class="pCopper1Off"></div>
						<div class="pCopper2Off"></div>		
					</td>
					<td>
						<div class="pCopper1Off"></div>
						<div class="pCopper2Off"></div>		
					</td>
					<td class="port_m58">
						<div class="pCopper1Off"></div>
						<div class="pCopper2Off"></div>		
					</td>								
					<td class="single_bottom">
						<div class="pFiber1Off"></div>	
					</td>
					<td class="single_bottom">
						<div class="pFiber1Off"></div>		
					</td>			
					<td class="mgmt_w230">
						<div class="pCopper1Off"></div>			
					</td>																																								
				</tr>
			</table>								
						
			</#if>
		
			<#if faultHWStatus.powerSupplyStatusList?has_content>
				<#if faultHWStatus.powerSupplyStatusList[0] == 1>
					<div class="power01 poGreen"></div>
				<#elseif faultHWStatus.powerSupplyStatusList[0] == 2>
					<div class="power01 poRed"></div>
				<#else>
					<div class="power01 poBlack"></div>
				</#if>
				
				<#if faultHWStatus.powerSupplyStatusList[1] == 1>
					<div class="power02 poGreen"></div>	
				<#elseif faultHWStatus.powerSupplyStatusList[1] == 2>
					<div class="power02 poRed"></div>	
				<#else>
					<div class="power02 poBlack"></div>	
				</#if>
			<#else>
				<div class="power01 poBlack"></div>
				<div class="power02 poBlack"></div>	
			</#if>
				
			<#if faultHWStatus.fanStatusList?has_content>
				<#if faultHWStatus.fanStatusList[0] == 1>
					<div class="fan01 fGreen"></div>
				<#elseif faultHWStatus.fanStatusList[0] == 2>
					<div class="fan01 fRed"></div>
				<#else>
					<div class="fan01 fBlack"></div>
				</#if>
			<#else>
				<div class="fan01 fBlack"></div>
			</#if>				
		
			<div class="legend2"><img src="/imgs/monitoring/device/legend_1.gif"/></div>		
		</div>
	</div>
</div>