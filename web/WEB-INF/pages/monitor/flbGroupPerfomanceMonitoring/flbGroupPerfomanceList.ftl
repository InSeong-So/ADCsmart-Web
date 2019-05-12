<#if orderOption.orderType??>
	<#if 34 == orderOption.orderType></#if><!-- GroupId -->
	<#if 35 == orderOption.orderType></#if><!-- GroupName -->
	<#if 36 == orderOption.orderType></#if><!-- Member 수 -->
	<#if 37 == orderOption.orderType></#if><!-- bps -->
	<#if 40 == orderOption.orderType></#if><!-- Concurrent Session -->
</#if>
<#if orderOption.orderDirection??>
	<#if 1 == orderOption.orderDirection></#if><!--Asc-->
	<#if 2 == orderOption.orderDirection></#if><!--Desc-->
</#if>
<caption></caption>
<colgroup>
	<col width="5%" />
	<col width="15%" />
	<col width="15%" />
	<col width="15%" />
	<col width="10%" />
	<col width="10%" />
	<col width="10%" />
	<col width="20%" />	
</colgroup>
<thead>
	<tr>				
		<td colspan="8" class="StartLine"></td>			
	</tr>
	<tr>
		<th rowspan="3" class="ContentsHeadLine bg_row2 Rcolor">${LANGCODEMAP["MSG_PERFLIST_STATUS"]!}</th>
	    <th colspan="3" class="ContentsHeadLine bg_row2 Rcolor">${LANGCODEMAP["MSG_PERFLIST_GROUP"]!}</th>
		<th colspan="3" class="ContentsHeadLine Rcolor">${LANGCODEMAP["MSG_PERFLIST_BPS"]!}</th>
		<th rowspan="3" class="ContentsHeadLine">
			<span class="css_textCursor">
				<#if orderOption.orderDirection == 2 && orderOption.orderType == 40>		
					<a class="orderDir_Desc">${LANGCODEMAP["MSG_PERFLIST_CONCURRENT"]!}
						<img src="imgs/Desc.gif"/>
						<span class="none orderType">40</span>
						<span class="none orderDir">1</span>	
						<br>${LANGCODEMAP["MSG_PERFLIST_SESSIONS"]!}(${LANGCODEMAP["MSG_APPLIANCE_COUNT"]!})				
					</a>						
				<#elseif orderOption.orderDirection == 1 && orderOption.orderType == 40>	
					<a class="orderDir_Asc">${LANGCODEMAP["MSG_PERFLIST_CONCURRENT"]!}
						<img src="imgs/Asc.gif"/>
						<span class="none orderType">40</span>
						<span class="none orderDir">2</span>	
						<br>${LANGCODEMAP["MSG_PERFLIST_SESSIONS"]!}(${LANGCODEMAP["MSG_APPLIANCE_COUNT"]!})	
					</a>
				<#else>
					<a class="orderDir_None">${LANGCODEMAP["MSG_PERFLIST_CONCURRENT"]!}
						<img src="imgs/none.gif"/>
						<span class="none orderType">40</span>
						<span class="none orderDir">2</span>
						<br>${LANGCODEMAP["MSG_PERFLIST_SESSIONS"]!}(${LANGCODEMAP["MSG_APPLIANCE_COUNT"]!})		
					</a>
				</#if>
			</span>
		</th>
	</tr>	
    <tr>
 		<td colspan="3" class="DivideLine1 Rcolor"></td>	
		<td colspan="3" class="DivideLine1"></td>		
    </tr>	
	<tr class="ContentsHeadLine1">
        <th>
        	<span class="css_textCursor">
				<#if orderOption.orderDirection == 2 && orderOption.orderType == 34>		
					<a class="orderDir_Desc">${LANGCODEMAP["MSG_PERFLIST_INDEX"]!}
						<img src="imgs/Desc.gif"/>
						<span class="none orderType">34</span>
						<span class="none orderDir">1</span>					
					</a>						
				<#elseif orderOption.orderDirection == 1 && orderOption.orderType == 34>	
					<a class="orderDir_Asc">${LANGCODEMAP["MSG_PERFLIST_INDEX"]!}
						<img src="imgs/Asc.gif"/>
						<span class="none orderType">34</span>
						<span class="none orderDir">2</span>		
					</a>
				<#else>
					<a class="orderDir_None">${LANGCODEMAP["MSG_PERFLIST_INDEX"]!}
						<img src="imgs/none.gif"/>
						<span class="none orderType">34</span>
						<span class="none orderDir">2</span>		
					</a>
				</#if>
			</span>       
		</th>	
	  	<th>
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
	  	<th class="Rcolor">
	  		<span class="css_textCursor">
				<#if orderOption.orderDirection == 2 && orderOption.orderType == 36>		
					<a class="orderDir_Desc">${LANGCODEMAP["MSG_PERFLIST_MEMBER_COUNT"]!}
						<img src="imgs/Desc.gif"/>
						<span class="none orderType">36</span>
						<span class="none orderDir">1</span>					
					</a>						
				<#elseif orderOption.orderDirection == 1 && orderOption.orderType == 36>	
					<a class="orderDir_Asc">${LANGCODEMAP["MSG_PERFLIST_MEMBER_COUNT"]!}
						<img src="imgs/Asc.gif"/>
						<span class="none orderType">36</span>
						<span class="none orderDir">2</span>		
					</a>
				<#else>
					<a class="orderDir_None">${LANGCODEMAP["MSG_PERFLIST_MEMBER_COUNT"]!}
						<img src="imgs/none.gif"/>
						<span class="none orderType">36</span>
						<span class="none orderDir">2</span>		
					</a>
				</#if>
			</span>
		</th>
	  	<th>In</th>
	  	<th>Out</th>
	  	<th class="Rcolor">
			<span class="css_textCursor">
				<#if orderOption.orderDirection == 2 && orderOption.orderType == 37>		
					<a class="orderDir_Desc">${LANGCODEMAP["MSG_PERFLIST_TOTAL"]!}
						<img src="imgs/Desc.gif"/>
						<span class="none orderType">37</span>
						<span class="none orderDir">1</span>					
					</a>						
				<#elseif orderOption.orderDirection == 1 && orderOption.orderType == 37>	
					<a class="orderDir_Asc">${LANGCODEMAP["MSG_PERFLIST_TOTAL"]!}
						<img src="imgs/Asc.gif"/>
						<span class="none orderType">37</span>
						<span class="none orderDir">2</span>		
					</a>
				<#else>
					<a class="orderDir_None">${LANGCODEMAP["MSG_PERFLIST_TOTAL"]!}
						<img src="imgs/none.gif"/>
						<span class="none orderType">37</span>
						<span class="none orderDir">2</span>		
					</a>
				</#if>
			</span>			  	
	  	</th>
	</tr>
	<tr>
		<td colspan="8" class="StartLine1"></td>	
	</tr>
</thead>
<tbody class="">
	<#if GroupPerfInfoList??>
		<#list GroupPerfInfoList as GroupPerfInfo>
			<#if 0 == GroupPerfInfo.groupStatus>
				<#assign imageFileName = "icon_flb_disabled.png">
				<#assign statusText = "Disabled">
			<#elseif 1 == GroupPerfInfo.groupStatus>
					<#assign imageFileName = "icon_flb_conn.png">
					<#assign statusText = "${LANGCODEMAP['MSG_PERFALTEON_NORMAL']!}">
			<#elseif 2 == GroupPerfInfo.groupStatus>
					<#assign imageFileName = "icon_flb_disconn.png">
					<#assign statusText = "${LANGCODEMAP['MSG_PERFALTEON_DISCONN']!}">
			<#elseif 3 == GroupPerfInfo.groupStatus>
					<#assign imageFileName = "icon_flb_disabled.png">
					<#assign statusText = "Disabled">
			</#if>
			<tr class="ContentsLine1" id="${GroupPerfInfo.groupDbIndex}">
				<td class="align_center Rcolor">
					<span class="align_center">
						<img src="imgs/icon/${imageFileName}" alt="${statusText}" />
					</span>
				</td>
				<td class="align_center">
					<a href="#" class="trafficDetailLnk groupIndex" id="">${GroupPerfInfo.groupId!""}</a>
				</td>
				<td class="align_left_P10">
					<span class="groupName">${GroupPerfInfo.groupName!""}</span>
				</td>
				<td class="align_center Rcolor textOver">
					<span class="memberCnt" title="">${GroupPerfInfo.memberCount!""}</span>			
				</td>
				<td class="align_center">${GroupPerfInfo.bpsIn!"-"}</td>
				<td class="align_center">${GroupPerfInfo.bpsOut!"-"}</td>
				<td class="align_center Rcolor">${GroupPerfInfo.bpsTotal!"-"}</td>
				<td class="align_center">${GroupPerfInfo.connCurr!"-"}</td>				
			</tr>
			<tr>
				<td colspan="8" class="DivideLine1"></td>				
			</tr>
		</#list>
	</#if>
	<tr>
		<td colspan="8" class="EndLine"></td>
	</tr>
</tbody>