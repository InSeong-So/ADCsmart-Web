<#setting number_format="0.####">
<#if orderType??>
	<#if 14 == orderType></#if><!-- 그룹이름 -->
</#if>
<#if orderDir??>
	<#if 1 == orderDir></#if><!-- Asc -->
	<#if 2 == orderDir></#if><!-- Desc -->
</#if>
<colgroup>
    <col width="40px"/>
	<col width="auto"/>
	<col  width="250px"/>
</colgroup>
	<tr class="StartLine">
		<td colspan="3"></td>
	</tr>
	<tr class="ContentsHeadLine">
  		<th>
  			<span>
  				<input class="allSvcGrpChk" type="checkbox"/>
  			</span>
  		</th>
		<th>
			<span class="css_textCursor">
				<#if orderDir == 2 && orderType == 14>		
					<a class="orderDir_Desc">${LANGCODEMAP["MSG_SYSSETTING_SVCGROUP_NAME"]!}
						<img src="imgs/Desc.gif"/>
						<span class="none orderType">14</span>
						<span class="none orderDir">1</span>					
					</a>						
				<#elseif orderDir == 1 && orderType == 14>	
					<a class="orderDir_Asc">${LANGCODEMAP["MSG_SYSSETTING_SVCGROUP_NAME"]!}
						<img src="imgs/Asc.gif"/>
						<span class="none orderType">14</span>
						<span class="none orderDir">2</span>		
					</a>
				<#else>
					<a class="orderDir_None">${LANGCODEMAP["MSG_SYSSETTING_SVCGROUP_NAME"]!}
						<img src="imgs/none.gif"/>
						<span class="none orderType">14</span>
						<span class="none orderDir">2</span>		
					</a>
				</#if>
			</span>	
		</th>
		<th>${LANGCODEMAP["MSG_SYSSETTING_SVCGROUPSERVICE_COUNT"]!}</th>
	</tr>
	<tr class="StartLine1">
		<td colspan="3"></td>
	</tr>			
</thead>
<#list vsGrpInfoList as thsVsGrp>
	<tr class="ContentsLine1 userList">			
		<td class="align_center">
			<span>
				<input class="svcGrpChk" type="checkbox" value="${thsVsGrp.index!''}"/>
			</span>
		</td>
		<td class="BoardLink align_left_P10 textOver" title="${thsVsGrp.name!''}">
			<a class="accountIdLnk" href="#">${thsVsGrp.name!''}</a>
    	</td>
		<td class="align_center textOver" title="${thsVsGrp.count!''}">${(thsVsGrp.count)!''}&nbsp;${LANGCODEMAP["MSG_HISTORY_COUNT"]!}</td>     
	</tr>
	<tr class="DivideLine">
		<td colspan="3"></td>
	</tr>
</#list>
<tr class="EndLine">
	<td colspan="3"></td>
</tr>