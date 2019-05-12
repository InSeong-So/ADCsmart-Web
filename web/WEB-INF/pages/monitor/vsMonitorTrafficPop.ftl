<div class="fixed-table-container_popup">
<div class="header-background50">
<table class="Board_popup" cellpadding="0" cellspacing="0" style="table-layout: fixed; width:100%">
	<caption>${LANGCODEMAP["MSG_NETWORK_POP_VS_TRAFFIC_DEFAULT"]!}</caption>
	<colgroup>
		<col width="180px">
		<col width="75px">
		<col width="170px">
		<col width="55px">
		<col width="55px">
		<col width="75px">
		<col width="55px">
		<col width="55px">
		<col width="75px">
		<col width="55px">
		<col width="55px">
		<col />
	</colgroup>
	<thead>
		<tr class="StartLine">
			<td colspan="12"></td>
		</tr>
		<tr class="ContentsHeadLine1">
			<th colspan="3" class="bg_row2 Rcolor">Virtual Server</th>
			<th colspan="3" class="Rcolor">bps</th>
			<th colspan="3" class="Rcolor">pps</th>
			<th colspan="3">${LANGCODEMAP["MSG_NETWORK_POP_BPSCONN"]!}</th>
		</tr>
	    <tr class="DivideLine">
			<th colspan="12"></th>
    		</tr>
	    <tr class="ContentsLine2">
	       	<th>${LANGCODEMAP["MSG_NETWORK_POP_VS_IP"]!}</th>
		  	<th>${LANGCODEMAP["MSG_NETWORK_POP_VS_PORT"]!}</th>
		  	<th class="Rcolor">${LANGCODEMAP["MSG_NETWORK_POP_VS_NAME"]!}</th>
			<th>in</th>
			<th>out</th>
			<th class="Rcolor">${LANGCODEMAP["MSG_NETWORK_POP_VS_TOTAL"]!}</th>
			<th>in</th>
			<th>out</th>
			<th class="Rcolor">${LANGCODEMAP["MSG_NETWORK_POP_VS_TOTAL"]!}</th>
			<th>${LANGCODEMAP["MSG_NETWORK_POP_VS_NOW"]!}</th>
			<th>${LANGCODEMAP["MSG_NETWORK_POP_VS_MAX"]!}</th>
			<th>${LANGCODEMAP["MSG_NETWORK_POP_VS_CUMULATIVE"]!}</th>
		</tr>
		<tr class="DivideLine">
			<td colspan="12"></td>
		</tr>
	</thead>
</table>
</div>
<div class="fixed-table-container-inner_noborder">		
<table class="Board_popup" cellpadding="0" cellspacing="0" style="table-layout: fixed; width:100%">
	<caption>${LANGCODEMAP["MSG_NETWORK_POP_VSTRAFFIC_DEFAULT"]!}</caption>
		<colgroup>
			<col width="180px">
			<col width="75px">
			<col width="170px">
			<col width="55px">
			<col width="55px">
			<col width="75px">
			<col width="55px">
			<col width="55px">
			<col width="75px">
			<col width="55px">
			<col width="55px">
			<col/>
		</colgroup>				
		<tbody>
		    <tr class="DivideLine">
		    	<td colspan="12" ></td>
		    </tr>
		    <tr class="ContentsLine1">
			    <td class="align_left_P5">${vsTrafficInfo.ipAddress!""}</td>
			    <td class="align_center">
			    	<#setting number_format="0.####">${vsTrafficInfo.port?replace("999", ", ")!0}
			    </td>
				<td class="align_left_P20 Rcolor textOver" title="${vsTrafficInfo.name!""}">${vsTrafficInfo.name!""}</td>
				<td class="align_center">${vsTrafficInfo.inBps!"-"}</td>
				<td class="align_center">${vsTrafficInfo.outBps!"-"}</td>
				<td class="align_center Rcolor">${vsTrafficInfo.totalBps!"-"}</td>
				<td class="align_center">${vsTrafficInfo.inPps!"-"}</td>
				<td class="align_center">${vsTrafficInfo.outPps!"-"}</td>
				<td class="align_center Rcolor">${vsTrafficInfo.totalPps!"-"}</td>
				<td class="align_center">${vsTrafficInfo.activeConnections!"-"}</td>
				<td class="align_center">${vsTrafficInfo.maxConnections!"-"}</td>
				<td class="align_center">${vsTrafficInfo.totalConnections!"-"}</td>
			</tr>
			<#if vsTrafficInfo.vsMemberTrafficInfoList??>
			<#list vsTrafficInfo.vsMemberTrafficInfoList as vsMemberTrafficInfo>
		    <tr class="DivideLine">
		    	<td colspan="12" ></td>
		    </tr>						
			<tr class="ContentsLine2">
				<td class="align_left_P20"><img src="imgs/icon/icon_member.png" alt="member" />  ${vsMemberTrafficInfo.ipAddress!""}</td>
				<td class="align_center">
					<#if (adc.type)?? && "F5" == adc.type>
						${vsMemberTrafficInfo.port!0}
					<#elseif (adc.type)?? && "PAS" == adc.type>
						${vsMemberTrafficInfo.port!0}
					<#elseif (adc.type)?? && "Alteon" == adc.type>
						<#if (vsMemberTrafficInfo.addPort)?? && (vsMemberTrafficInfo.port == 0)>
							${vsMemberTrafficInfo.addPort!0}
						<#else>
							${vsMemberTrafficInfo.port!0}
						</#if>
					<#else>
						-
					</#if>
				</td>
				<td class="align_left_P20 Rcolor textOver">-</td>
				<td class="align_center">${vsMemberTrafficInfo.inBps!"-"}</td>
				<td class="align_center">${vsMemberTrafficInfo.outBps!"-"}</td>
				<td class="align_center Rcolor">${vsMemberTrafficInfo.totalBps!"-"}</td>
				<td class="align_center">${vsMemberTrafficInfo.inPps!"-"}</td>
				<td class="align_center">${vsMemberTrafficInfo.outPps!"-"}</td>
				<td class="align_center Rcolor">${vsMemberTrafficInfo.totalPps!"-"}</td>
				<td class="align_center">${vsMemberTrafficInfo.activeConnections!"-"}</td>
				<td class="align_center">${vsMemberTrafficInfo.maxConnections!"-"}</td>
				<td class="align_center">${vsMemberTrafficInfo.totalConnections!"-"}</td>
			</tr>
			</#list>
			</#if>
		</tbody>
	<tr class="EndLine">
		<td colspan="12"></td>
	</tr>
</table>
</div>
</div>