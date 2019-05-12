<div class="alteon_2216">
	<#if faultHWStatus.cpuStatusList?has_content>
		<#if faultHWStatus.cpuStatusList[0] == 1>
			<div class="cpu01 txt cGreen">MP</div>
		<#elseif faultHWStatus.cpuStatusList[0] == 2>
			<div class="cpu01 txt cRed">MP</div>
		<#else>
			<div class="cpu01 txt cBlack">-</div>
		</#if>
		
		<#if faultHWStatus.cpuStatusList[1] == 1>
			<div class="cpu02 txt cGreen">SP1</div>
		<#elseif faultHWStatus.cpuStatusList[1] == 2>
			<div class="cpu02 txt cRed">SP1</div>
		<#else>
			<div class="cpu02 txt cBlack">-</div>
		</#if>
		
		<#if faultHWStatus.cpuStatusList[2] == 1>
			<div class="cpu03 txt cGreen">SP2</div>
		<#elseif faultHWStatus.cpuStatusList[2] == 2>
			<div class="cpu03 txt cRed">SP2</div>
		<#else>
			<div class="cpu03 txt cBlack">-</div>
		</#if>
	<#else>
		<div class="cpu01 txt cBlack">-</div>
		<div class="cpu02 txt cBlack">-</div>
		<div class="cpu03 txt cBlack">-</div>
	</#if>
		
	<#if faultHWStatus.hddStatus?has_content>	
		<#if faultHWStatus.hddStatus == 1>
			<div class="hdd01 hGreen">HDD ${faultHWStatus.hddUsage}%</div>	
		<#elseif faultHWStatus.hddStatus == 2>
			<div class="hdd01 hRed">HDD ${faultHWStatus.hddUsage}%</div>
		<#else>
			<div class="hdd01 hBlack">&nbsp;</div>		
		</#if>
	<#else>
		<div class="hdd01 hBlack">&nbsp;</div>
	</#if>	
	
	<br class="clearfix" />	
	<div class="version">OS 29.1.11.0.1</div>
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
			<td>
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
					<div class="port_m58 port08 pCopper2Black"></div>
				<#elseif faultHWStatus.portStatusList[7].linkStatus == 5>
					<div class="port_m58 port08 pCopper2Yellow"></div>
				<#else>
					<div class="port_m58 port08 pCopper2Off"></div>
				</#if>				
			</td>			
			<td>
				<#if faultHWStatus.portStatusList[8].linkStatus == 1>
					<div class="pCopper1Green"></div>
				<#elseif faultHWStatus.portStatusList[8].linkStatus == 2>
					<div class="pCopper1Red"></div>
				<#elseif faultHWStatus.portStatusList[8].linkStatus == 3>
					<div class="pCopper1Black"></div>
				<#elseif faultHWStatus.portStatusList[8].linkStatus == 5>
					<div class="pCopper1Yellow"></div>
				<#else>
					<div class="pCopper1Off"></div>
				</#if>
				
				<#if faultHWStatus.portStatusList[9].linkStatus == 1>
					<div class="pCopper2Green"></div>
				<#elseif faultHWStatus.portStatusList[9].linkStatus == 2>
					<div class="pCopper2Red"></div>
				<#elseif faultHWStatus.portStatusList[9].linkStatus == 3>
					<div class="pCopper2Black"></div>
				<#elseif faultHWStatus.portStatusList[9].linkStatus == 5>
					<div class="pCopper2Yellow"></div>
				<#else>
					<div class="pCopper2Off"></div>
				</#if>			
			</td>
			<td>
				<#if faultHWStatus.portStatusList[10].linkStatus == 1>
					<div class="pCopper1Green"></div>
				<#elseif faultHWStatus.portStatusList[10].linkStatus == 2>
					<div class="pCopper1Red"></div>
				<#elseif faultHWStatus.portStatusList[10].linkStatus == 3>
					<div class="pCopper1Black"></div>
				<#elseif faultHWStatus.portStatusList[10].linkStatus == 5>
					<div class="pCopper1Yellow"></div>
				<#else>
					<div class="pCopper1Off"></div>
				</#if>
				
				<#if faultHWStatus.portStatusList[11].linkStatus == 1>
					<div class="pCopper2Green"></div>
				<#elseif faultHWStatus.portStatusList[11].linkStatus == 2>
					<div class="pCopper2Red"></div>
				<#elseif faultHWStatus.portStatusList[11].linkStatus == 3>
					<div class="pCopper2Black"></div>
				<#elseif faultHWStatus.portStatusList[11].linkStatus == 5>
					<div class="pCopper2Yellow"></div>
				<#else>
					<div class="pCopper2Off"></div>
				</#if>		
			</td>
			<td>
				<#if faultHWStatus.portStatusList[12].linkStatus == 1>
					<div class="pCopper1Green"></div>
				<#elseif faultHWStatus.portStatusList[12].linkStatus == 2>
					<div class="pCopper1Red"></div>
				<#elseif faultHWStatus.portStatusList[12].linkStatus == 3>
					<div class="pCopper1Black"></div>
				<#elseif faultHWStatus.portStatusList[12].linkStatus == 5>
					<div class="pCopper1Yellow"></div>
				<#else>
					<div class="pCopper1Off"></div>
				</#if>
				
				<#if faultHWStatus.portStatusList[13].linkStatus == 1>
					<div class="pCopper2Green"></div>
				<#elseif faultHWStatus.portStatusList[13].linkStatus == 2>
					<div class="pCopper2Red"></div>
				<#elseif faultHWStatus.portStatusList[13].linkStatus == 3>
					<div class="pCopper2Black"></div>
				<#elseif faultHWStatus.portStatusList[13].linkStatus == 5>
					<div class="pCopper2Yellow"></div>
				<#else>
					<div class="pCopper2Off"></div>
				</#if>			
			</td>
			<td class="port_m203">
				<#if faultHWStatus.portStatusList[14].linkStatus == 1>
					<div class="pCopper1Green"></div>
				<#elseif faultHWStatus.portStatusList[14].linkStatus == 2>
					<div class="pCopper1Red"></div>
				<#elseif faultHWStatus.portStatusList[14].linkStatus == 3>
					<div class="pCopper1Black"></div>
				<#elseif faultHWStatus.portStatusList[14].linkStatus == 5>
					<div class="pCopper1Yellow"></div>
				<#else>
					<div class="pCopper1Off"></div>
				</#if>
				
				<#if faultHWStatus.portStatusList[15].linkStatus == 1>
					<div class="pCopper2Green"></div>
				<#elseif faultHWStatus.portStatusList[15].linkStatus == 2>
					<div class="port16 pCopper2Red"></div>
				<#elseif faultHWStatus.portStatusList[15].linkStatus == 3>
					<div class="port16 pCopper2Black"></div>
				<#elseif faultHWStatus.portStatusList[15].linkStatus == 5>
					<div class="port16 pCopper2Yellow"></div>
				<#else>
					<div class="port_m203 port16 pCopper2Off"></div>
				</#if>				
			</td>
			<td class="single_bottom">
				<#if faultHWStatus.portStatusList[16].linkStatus == 1>
					<div class="pFiber1Green"></div>
				<#elseif faultHWStatus.portStatusList[16].linkStatus == 2>
					<div class="pFiber1Red"></div>
				<#elseif faultHWStatus.portStatusList[16].linkStatus == 3>
					<div class="pFiber1Black"></div>
				<#elseif faultHWStatus.portStatusList[16].linkStatus == 5>
					<div class="pFiber1Yellow"></div>
				<#else>
					<div class="pFiber1Off"></div>
				</#if>			
			</td>
			<td class="single_bottom">
				<#if faultHWStatus.portStatusList[17].linkStatus == 1>
					<div class="pFiber1Green"></div>
				<#elseif faultHWStatus.portStatusList[17].linkStatus == 2>
					<div class="pFiber1Red"></div>
				<#elseif faultHWStatus.portStatusList[17].linkStatus == 3>
					<div class="pFiber1Black"></div>
				<#elseif faultHWStatus.portStatusList[17].linkStatus == 5>
					<div class="pFiber1Yellow"></div>
				<#else>
					<div class="pFiber1Off"></div>
				</#if>			
			</td>			
			<td class="mgmt_w56">
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
					<div class="pCopper1Green"></div>			
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
			<td>
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
			<td>
				<div class="pCopper1Off"></div>
				<div class="pCopper2Off"></div>		
			</td>
			<td>
				<div class="pCopper1Off"></div>
				<div class="pCopper2Off"></div>			
			</td>
			<td>
				<div class="pCopper1Off"></div>
				<div class="pCopper2Off"></div>			
			</td>
			<td class="port_m203">
				<div class="pCopper1Off"></div>
				<div class="pCopper2Off"></div>		
			</td>
			<td class="single_bottom">
				<div class="pFiber1Off"></div>			
			</td>
			<td class="single_bottom">
				<div class="pFiber1Off"></div>			
			</td>			
			<td class="mgmt_w56">
				<div class="pCopper1Off"></div>		
			</td>																	
			</tr>
			</table>	

	</#if>
	
	<#if faultHWStatus.powerSupplyStatusList?has_content>
		<div class="power01 poGreen"></div>
		<!--
		<#if faultHWStatus.powerSupplyStatusList[0] == 1>
			<div class="power01 poGreen"></div>
		<#elseif faultHWStatus.powerSupplyStatusList[0] == 2>
			<div class="power01 poRed"></div>
		<#else>
			<div class="power01 poBlack"></div>
		</#if>
		-->
	<#else>
		<div class="power01 poBlack"></div>
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
<div class="legend2"><img src="imgs/monitoring/device/legend_1.gif"/></div>		
</div>