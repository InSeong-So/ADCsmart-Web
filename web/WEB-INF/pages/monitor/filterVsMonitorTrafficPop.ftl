<div class="fixed-table-container_popup80">
<div class="header-background50">
<table class="Board_popup" cellpadding="0" cellspacing="0" style="table-layout: fixed; width:100%">
	<caption></caption>
	<colgroup>
		<col width="60px" />
		<col width="40px" />
		<col width="70px" />
		<col width="100px" />
		<col width="100px" />
		<col width="100px" />
		<col width="100px" />
		<col width="60px" />
		<col width="60px" />
		<col width="60px" />
		<col width="60px" />
		<col width="70px" />
	    <col width="auto"/>
	</colgroup>
	<thead>
		<tr class="StartLine">
			<td colspan="13"></td>
		</tr>
		<tr class="ContentsHeadLine1">
	       	<th>Index</th>
		  	<th>${LANGCODEMAP["MSG_NETWORK_POP_VS_STATUE"]!}</th>
		  	<th>${LANGCODEMAP["MSG_NETWORK_POP_VS_NAME"]!}</th>
			<th>Sip</th>
			<th>Smask</th>
			<th>Dip</th>
			<th>Dmask</th>
			<th>Proto</th>
			<th>Sport</th>
			<th>Dport</th>
			<th>${LANGCODEMAP["MSG_NETWORK_PORT"]!}</th>					
			<th>Action</th>
			<th>Redirection</th>					
		</tr>
		<tr class="DivideLine">
			<td colspan="13"></td>
		</tr>
	</thead>
</table>	
</div>
<div class="fixed-table-container-inner_noborder">		
<table class="Board_popup" cellpadding="0" cellspacing="0" style="table-layout: fixed; width:100%">
	<caption></caption>
		<colgroup>
			<col width="60px" />
			<col width="40px" />
			<col width="70px" />
			<col width="100px" />
			<col width="100px" />
			<col width="100px" />
			<col width="100px" />
			<col width="60px" />
			<col width="60px" />
			<col width="60px" />
			<col width="60px" />
			<col width="70px" />
		    <col width="auto"/>
		</colgroup>				
		<tbody>
		    <tr class="DivideLine">
		    	<td colspan="13" ></td>
		    </tr>
		    <#if filteVsTrafficInfo??>
			<#list filteVsTrafficInfo as flbFilterInfo>
		    <tr class="ContentsLine2">
			    <td class="align_center"><#setting number_format="0.####">${flbFilterInfo.filterId!""}</td>
			    <#if flbFilterInfo.state == 1>
			    	<td class="align_center"><img src="imgs/icon/icon_greenDot.png" /></td>
			    <#else>
			    	<td class="align_center"><img src="imgs/icon/icon_gryDot.png" /></td>
			    </#if>
				<td class="align_left_P20 textOver" title="${flbFilterInfo.name!''}">${flbFilterInfo.name!""}</td>
				<td class="align_center">${flbFilterInfo.srcIP!""}</td>
				<td class="align_center">${flbFilterInfo.srcMask!""}</td>
				<td class="align_center">${flbFilterInfo.dstIP!""}</td>
				<td class="align_center">${flbFilterInfo.dstMask!""}</td>
				<td class="align_center">${flbFilterInfo.protocol!""}</td>
				<#if flbFilterInfo.srcPortFrom == flbFilterInfo.srcPortTo>
					<td class="align_center">${flbFilterInfo.srcPortFrom!"-"}</td>
				<#else>
					<td class="align_center">${flbFilterInfo.srcPortFrom!"-"} - ${flbFilterInfo.srcPortTo!"-"}</td>
				</#if>
				<#if flbFilterInfo.dstPortFrom == flbFilterInfo.dstPortTo>
					<td class="align_center">${flbFilterInfo.dstPortFrom!"-"}</td>
				<#else>
					<td class="align_center">${flbFilterInfo.dstPortFrom!"-"} - ${flbFilterInfo.dstPortTo!"-"}</td>
				</#if>
				<td class="align_center">
				<#list flbFilterInfo.physicalPortList as physical>
					${physical?default("-")}<#if physical_has_next>,</#if>
				</#list>
				</td>
				<td class="align_center">${flbFilterInfo.action!""}</td>
				<td class="align_center">${flbFilterInfo.redirection!""}</td>						
			</tr>
		    <tr class="DivideLine">
		    	<td colspan="13" ></td>
		    </tr>						
			</#list>
			</#if>
		</tbody>
	<tr class="EndLine">
		<td colspan="13"></td>
	</tr>
</table>
</div> 	
</div>