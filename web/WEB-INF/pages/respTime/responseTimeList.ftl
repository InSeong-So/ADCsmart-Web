<#setting number_format="0.####">
<#if orderType??>
	<#if 11 ==  orderType></#if>
</#if>
<#if orderDir??>
	<#if 1 == orderDir></#if><!-- Asc -->
	<#if 2 == orderDir></#if><!-- Desc -->
</#if>	
<!----- Contents List Start ----->
	<!--<table class="Board_out" cellpadding="0" cellspacing="0" style="table-layout: fixed;">-->
		<colgroup>	
			<col width="40px">
		    <col width="40px">			
		    <col width="200px">	    
		    <col width="110px">
		    <col width="90px">	    
			<col width="90px">
			<col width="90px">
			<col width="90px">
			<col width="90px">
			<col width="90px">		
			<col width="auto">
		</colgroup>
		<thead>
			<tr>
				<th class="center"><input class="allRespGrpChk" type="checkbox"></th>
				<th></th>
				<th>
					<span class="css_textCursor">
						<#if orderDir == 2 && orderType == 14>		
							<a class="orderDir_Desc">${LANGCODEMAP["MSG_SYSTOOL_RESPONSE_NAME"]!}
								<img src="imgs/Desc.gif"/>
								<span class="none orderType">14</span>
								<span class="none orderDir">1</span>					
							</a>						
						<#elseif orderDir == 1 && orderType == 14>	
							<a class="orderDir_Asc">${LANGCODEMAP["MSG_SYSTOOL_RESPONSE_NAME"]!}
								<img src="imgs/Asc.gif"/>
								<span class="none orderType">14</span>
								<span class="none orderDir">2</span>		
							</a>
						<#else>
							<a class="orderDir_None">${LANGCODEMAP["MSG_SYSTOOL_RESPONSE_NAME"]!}
								<img src="imgs/none.gif"/>
								<span class="none orderType">14</span>
								<span class="none orderDir">2</span>		
							</a>
						</#if>
					</span>	
				</th>
				<th>IP</th>
				<th>${LANGCODEMAP["MSG_SYSTOOL_RESPONSE_PORT"]!}</th>
				<th>${LANGCODEMAP["MSG_SYSTOOL_RESPONSE_CHECK"]!}</th>
				<th>Current</th>
				<th>Avg</th>				
				<th>Max</th>	
				<th>Min</th>		
				<th>${LANGCODEMAP["MSG_SYSTOOL_RESPONSE_DESC"]!}</th>  			
			</tr>
		</thead>
		<tbody>			
		<!----- 구간 Group ----->					
		<#list respGroupInfoList as theRespInfoList>
			<tr class="Group" data-index="${theRespInfoList.index}">	
				<td class="center"><input class="respGrpChk" type="checkbox" value="${theRespInfoList.index}"></td>
				<td class="btnPlusMinus center" data-value="plus" data-parent="${theRespInfoList.index}"><img class="plusminus" src="/imgs/layout/plus.gif" alt=""/></td>
				<td colspan="9">
					<a class="modifyRespInfoLnk" id="mar_lft5" href="#" target="_self">${theRespInfoList.name!''}</a>
				</td>
			</tr>	
				
			<!----- 구간 member ----->	
			<#list theRespInfoList.respInfo as theRespInfo>			
			<#if (theRespInfo.groupIndex== theRespInfoList.index) >	
			<tr class="respInfo none member" data-parent="${theRespInfoList.index}">			
				<td></td>
				<td class="center"><img src="/imgs/layout/subtree.gif" alt=""/></td>
				<td class="textOver" title="${theRespInfoList.name!''}">${theRespInfoList.name!''}</td>			
				<td class="left ip">
					<span class="ipAddress">${theRespInfo.ipaddress!''}</sapn>
				</td>
				<td class="center">
					<span class="port"><#setting number_format="0.####">${theRespInfo.port!'0'}</span>
				</td>
				<td class="center">
					<#if (theRespInfo.type == 0)>
						ICMP
					<#elseif (theRespInfo.type == 1)>
						TCP
					<#elseif (theRespInfo.type == 2)>
						HTTP
					<#elseif (theRespInfo.type == 3)>
						HTTPS
					<#else>
						HTTP
					</#if>
				</td>
				<td class="center">				
				<#if (theRespInfo.currRespTime) != -1>
					${theRespInfo.currRespTimeUnit!'0'}
				<#else>
					-
				</#if>	
				</td>
			    <td class="center">
			    <#if (theRespInfo.avgRespTime) != -1>
					${theRespInfo.avgRespTimeUnit!'0'}
				<#else>
					-
				</#if>	
				</td>	
				<td class="center">
			    <#if (theRespInfo.maxRespTime) != -1>
					${theRespInfo.maxRespTimeUnit!'0'}
				<#else>
					-
				</#if>	
				</td>	
				<td class="center">
			    <#if (theRespInfo.minRespTime) != -1>
					${theRespInfo.minRespTimeUnit!'0'}
				<#else>
					-
				</#if>	
				</td>	
			    <td class="textOver" title="${theRespInfo.comment!''}">${theRespInfo.comment!''}</td>			    			    
			</tr>
			</#if>
			</#list>
		</#list>						
		</tbody>