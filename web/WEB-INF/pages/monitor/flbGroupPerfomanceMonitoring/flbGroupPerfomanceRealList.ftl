<#if orderOption.orderType??>
	<#if 34 == orderOption.orderType></#if><!-- ip(Group id) -->
	<#if 35 == orderOption.orderType></#if><!-- Real_Name -->
	<#if 36 == orderOption.orderType></#if><!-- bps Total -->
	<#if 37 == orderOption.orderType></#if><!-- Concurrent Session -->
</#if>
<#if orderOption.orderDirection??>
	<#if 1 == orderOption.orderDirection></#if><!--Asc-->
	<#if 2 == orderOption.orderDirection></#if><!--Desc-->
</#if>
<caption></caption>
<colgroup>
	<col width="25%" />
	<col width="25%" />
	<col width="10%" />
	<col width="10%" />
	<col width="10%" />
	<col width="20%" />	
</colgroup>
<thead>
	<tr>				
		<td colspan="6" class="StartLine"></td>			
	</tr>
	<tr>
	    <th colspan="2" class="ContentsHeadLine bg_row2 Rcolor">Real Server</th>
		<th colspan="3" class="ContentsHeadLine Rcolor">bps</th>
		<th rowspan="3" class="ContentsHeadLine">
			<span class="css_textCursor">
				<#if orderOption.orderDirection == 2 && orderOption.orderType == 37>		
					<a class="orderDir_Desc">${LANGCODEMAP["MSG_PERFLIST_CONCURRENT"]!}
						<img src="imgs/Desc.gif"/>
						<span class="none orderType">37</span>
						<span class="none orderDir">1</span>
						<br>${LANGCODEMAP["MSG_PERFLIST_SESSIONS"]!}(${LANGCODEMAP["MSG_APPLIANCE_COUNT"]!})					
					</a>						
				<#elseif orderOption.orderDirection == 1 && orderOption.orderType == 37>	
					<a class="orderDir_Asc">${LANGCODEMAP["MSG_PERFLIST_CONCURRENT"]!}
						<img src="imgs/Asc.gif"/>
						<span class="none orderType">37</span>
						<span class="none orderDir">2</span>	
						<br>${LANGCODEMAP["MSG_PERFLIST_SESSIONS"]!}(${LANGCODEMAP["MSG_APPLIANCE_COUNT"]!})	
					</a>
				<#else>
					<a class="orderDir_None">${LANGCODEMAP["MSG_PERFLIST_CONCURRENT"]!}
						<img src="imgs/none.gif"/>
						<span class="none orderType">37</span>
						<span class="none orderDir">2</span>
						<br>${LANGCODEMAP["MSG_PERFLIST_SESSIONS"]!}(${LANGCODEMAP["MSG_APPLIANCE_COUNT"]!})		
					</a>
				</#if>
			</span>		
		</th>
	</tr>	
    <tr>
 		<td colspan="2" class="DivideLine1 Rcolor"></td>	
		<td colspan="3" class="DivideLine1"></td>		
    </tr>	
	<tr class="ContentsHeadLine1">	
	  	<th>
	  		<span class="css_textCursor">
				<#if orderOption.orderDirection == 2 && orderOption.orderType == 34>		
					<a class="orderDir_Desc">${LANGCODEMAP["MSG_PERFLIST_IP_GROUPID"]!}
						<img src="imgs/Desc.gif"/>
						<span class="none orderType">34</span>
						<span class="none orderDir">1</span>					
					</a>						
				<#elseif orderOption.orderDirection == 1 && orderOption.orderType == 34>	
					<a class="orderDir_Asc">${LANGCODEMAP["MSG_PERFLIST_IP_GROUPID"]!}
						<img src="imgs/Asc.gif"/>
						<span class="none orderType">34</span>
						<span class="none orderDir">2</span>		
					</a>
				<#else>
					<a class="orderDir_None">${LANGCODEMAP["MSG_PERFLIST_IP_GROUPID"]!}
						<img src="imgs/none.gif"/>
						<span class="none orderType">34</span>
						<span class="none orderDir">2</span>		
					</a>
				</#if>
			</span>
	  	</th>
	  	<th class="Rcolor">
	  		<span class="css_textCursor">
				<#if orderOption.orderDirection == 2 && orderOption.orderType == 35>		
					<a class="orderDir_Desc">${LANGCODEMAP["MSG_PERFLIST_NAME"]!}
						<img src="imgs/Desc.gif"/>
						<span class="none orderType">35</span>
						<span class="none orderDir">1</span>					
					</a>						
				<#elseif orderOption.orderDirection == 1 && orderOption.orderType == 35>	
					<a class="orderDir_Asc">${LANGCODEMAP["MSG_PERFLIST_NAME"]!}
						<img src="imgs/Asc.gif"/>
						<span class="none orderType">35</span>
						<span class="none orderDir">2</span>		
					</a>
				<#else>
					<a class="orderDir_None">${LANGCODEMAP["MSG_PERFLIST_NAME"]!}
						<img src="imgs/none.gif"/>
						<span class="none orderType">35</span>
						<span class="none orderDir">2</span>		
					</a>
				</#if>
			</span>
	  	</th>
	  	<th>In</th>
	  	<th>Out</th>
	  	<th class="Rcolor">
	  		<span class="css_textCursor">
				<#if orderOption.orderDirection == 2 && orderOption.orderType == 36>		
					<a class="orderDir_Desc">${LANGCODEMAP["MSG_PERFLIST_TOTAL"]!}
						<img src="imgs/Desc.gif"/>
						<span class="none orderType">36</span>
						<span class="none orderDir">1</span>					
					</a>						
				<#elseif orderOption.orderDirection == 1 && orderOption.orderType == 36>	
					<a class="orderDir_Asc">${LANGCODEMAP["MSG_PERFLIST_TOTAL"]!}
						<img src="imgs/Asc.gif"/>
						<span class="none orderType">36</span>
						<span class="none orderDir">2</span>		
					</a>
				<#else>
					<a class="orderDir_None">${LANGCODEMAP["MSG_PERFLIST_TOTAL"]!}
						<img src="imgs/none.gif"/>
						<span class="none orderType">36</span>
						<span class="none orderDir">2</span>		
					</a>
				</#if>
			</span>
		</th>
	</tr>
	<tr>
		<td colspan="6" class="StartLine1"></td>	
	</tr>
</thead>
<tbody class="">
	<tr class="ContentsLine1" id="${GroupMemberPerfInfoList.groupInfo.groupDbIndex!""}" idx = "-1">
		<td class="align_left_P10">${GroupMemberPerfInfoList.groupInfo.groupId!""}</td>
		<td class="align_left_P10 Rcolor">
			<span class="groupName">${GroupMemberPerfInfoList.groupInfo.groupName!""}</span>
		</td>
		<td class="align_center">${GroupMemberPerfInfoList.groupInfo.bpsIn!""}</td>
		<td class="align_center">${GroupMemberPerfInfoList.groupInfo.bpsOut!""}</td>
		<td class="align_center Rcolor">${GroupMemberPerfInfoList.groupInfo.bpsTotal!""}</td>
		<td class="align_center">${GroupMemberPerfInfoList.groupInfo.connCurr!""}</td>				
	</tr>
	
	<tr>
		<td colspan="6" class="DivideLine1"></td>				
	</tr>
	
	<#if GroupMemberPerfInfoList.memberList??>
	<#list GroupMemberPerfInfoList.memberList as memberList>
		<#assign imageFileName = "">
		<#assign statusText = "">
		<#if memberList.status??>
			<#if 0 == memberList.status>
				<#assign imageFileName = "icon_gryDot.png">
				<#assign statusText = "Disabled">
			<#elseif 1 == memberList.status>
				<#assign imageFileName = "icon_greenDot.png">
				<#assign statusText = "${LANGCODEMAP['MSG_PERFALTEON_NORMAL']!}">
			<#elseif 2 == memberList.status>
				<#assign imageFileName = "icon_redDot.png">
				<#assign statusText = "${LANGCODEMAP['MSG_PERFALTEON_DISCONN']!}">
			<#elseif 3 == memberList.status>
				<#assign imageFileName = "icon_yellowDot.png">
				<#assign statusText = "${LANGCODEMAP['MSG_PERFALTEON_ABNORMAL']!}">
			</#if>
		</#if>
	<tr class="ContentsLine2" id="${memberList.dbIndex!""}">
		<td class="align_left_P20">			
			<img src="imgs/icon/icon_member.png" alt="member" />
			<span class="memberIp">${memberList.ipAddress!""}</span>			
		</td>
		<td class="align_left_P10 Rcolor">${memberList.name!""}</td>
		<td class="align_center">${memberList.bpsIn!""}</td>
		<td class="align_center">${memberList.bpsOut!""}</td>
		<td class="align_center Rcolor">${memberList.bpsTotal!""}</td>
		<td class="align_center">${memberList.connCurr!""}</td>				
	</tr>
	<tr>
		<td colspan="6" class="DivideLine1"></td>				
	</tr>
	</#list>
	</#if>
	<tr>
		<td colspan="6" class="EndLine"></td>
	</tr>
</tbody>