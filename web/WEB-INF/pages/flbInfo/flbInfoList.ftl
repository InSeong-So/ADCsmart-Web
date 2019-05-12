<#setting number_format="0.####">
<#if orderType??>
	<#if 37 == orderType></#if><!-- index -->
	<#if 34 == orderType></#if><!-- state -->
	<#if 35 == orderType></#if><!-- action -->
	<#if 36 == orderType></#if><!-- group -->
</#if>
<#if orderDir??>
	<#if 1 == orderDir></#if><!-- Asc -->
	<#if 2 == orderDir></#if><!-- Desc -->
</#if>
<caption></caption>
		<colgroup>
			<col width="5.5%" />
			<col width="5.5%" />
			<col width="9%" />
			<col width="10%" />
			<col width="10%" />
			<col width="10%" />
			<col width="10%" />
			<col width="5%" />
			<col width="5%" />
			<col width="5%" />
			<col width="7%" />
			<col width="7%" />
			<col width="11%" />
		</colgroup>		
		<tr class="StartLine">
			<td colspan="13">
			</td>
		</tr>
		<tr class="ContentsHeadLine">
		<th class="align_center">
			<span class="css_textCursor">
				<#if orderOption.orderDirection == 2 && orderOption.orderType == 37>		
					<a class="orderDir_Desc">${LANGCODEMAP["MSG_FLBFILTERINFO_INDEX"]!}
						<img src="imgs/Desc.gif"/>
						<span class="none orderType">37</span>
						<span class="none orderDir">1</span>					
					</a>						
				<#elseif orderOption.orderDirection == 1 && orderOption.orderType == 37>	
					<a class="orderDir_Asc">${LANGCODEMAP["MSG_FLBFILTERINFO_INDEX"]!}
						<img src="imgs/Asc.gif"/>
						<span class="none orderType">37</span>
						<span class="none orderDir">2</span>		
					</a>
				<#else>
					<a class="orderDir_None">${LANGCODEMAP["MSG_FLBFILTERINFO_INDEX"]!}
						<img src="imgs/none.gif"/>
						<span class="none orderType">37</span>
						<span class="none orderDir">2</span>		
					</a>
				</#if>
			</span>		
		</th>
		<th class="align_center">
			<span class="css_textCursor">
				<#if orderOption.orderDirection == 2 && orderOption.orderType == 34>		
					<a class="orderDir_Desc">${LANGCODEMAP["MSG_FLBFILTERINFO_STATE"]!}
						<img src="imgs/Desc.gif"/>
						<span class="none orderType">34</span>
						<span class="none orderDir">1</span>					
					</a>						
				<#elseif orderOption.orderDirection == 1 && orderOption.orderType == 34>	
					<a class="orderDir_Asc">${LANGCODEMAP["MSG_FLBFILTERINFO_STATE"]!}
						<img src="imgs/Asc.gif"/>
						<span class="none orderType">34</span>
						<span class="none orderDir">2</span>		
					</a>
				<#else>
					<a class="orderDir_None">${LANGCODEMAP["MSG_FLBFILTERINFO_STATE"]!}
						<img src="imgs/none.gif"/>
						<span class="none orderType">34</span>
						<span class="none orderDir">2</span>		
					</a>
				</#if>
			</span>
		</th>	
		<th class="align_center">${LANGCODEMAP["MSG_FLBFILTERINFO_NAME"]!}</th>	
		<th class="align_center">${LANGCODEMAP["MSG_FLBFILTERINFO_SIP"]!}</th>	
		<th class="align_center">${LANGCODEMAP["MSG_FLBFILTERINFO_SMASK"]!}</th>	
		<th class="align_center">${LANGCODEMAP["MSG_FLBFILTERINFO_DIP"]!}</th>
		<th class="align_center">${LANGCODEMAP["MSG_FLBFILTERINFO_DMASK"]!}</th> 	
		<th class="align_center">${LANGCODEMAP["MSG_FLBFILTERINFO_PROTO"]!}</th>	
		<th class="align_center">${LANGCODEMAP["MSG_FLBFILTERINFO_SPORT"]!}</th>	
		<th class="align_center">${LANGCODEMAP["MSG_FLBFILTERINFO_DPORT"]!}</th>	
		<th class="align_center">
			<span class="css_textCursor">
				<#if orderOption.orderDirection == 2 && orderOption.orderType == 35>		
					<a class="orderDir_Desc">${LANGCODEMAP["MSG_FLBFILTERINFO_ACTION"]!}
						<img src="imgs/Desc.gif"/>
						<span class="none orderType">35</span>
						<span class="none orderDir">1</span>					
					</a>						
				<#elseif orderOption.orderDirection == 1 && orderOption.orderType == 35>	
					<a class="orderDir_Asc">${LANGCODEMAP["MSG_FLBFILTERINFO_ACTION"]!}
						<img src="imgs/Asc.gif"/>
						<span class="none orderType">35</span>
						<span class="none orderDir">2</span>		
					</a>
				<#else>
					<a class="orderDir_None">${LANGCODEMAP["MSG_FLBFILTERINFO_ACTION"]!}
						<img src="imgs/none.gif"/>
						<span class="none orderType">35</span>
						<span class="none orderDir">2</span>		
					</a>
				</#if>
			</span>
		</th>	
		<th class="align_center">
			<span class="css_textCursor">
				<#if orderOption.orderDirection == 2 && orderOption.orderType == 36>		
					<a class="orderDir_Desc">${LANGCODEMAP["MSG_FLBFILTERINFO_GROUP"]!}
						<img src="imgs/Desc.gif"/>
						<span class="none orderType">36</span>
						<span class="none orderDir">1</span>					
					</a>						
				<#elseif orderOption.orderDirection == 1 && orderOption.orderType == 36>	
					<a class="orderDir_Asc">${LANGCODEMAP["MSG_FLBFILTERINFO_GROUP"]!}
						<img src="imgs/Asc.gif"/>
						<span class="none orderType">36</span>
						<span class="none orderDir">2</span>		
					</a>
				<#else>
					<a class="orderDir_None">${LANGCODEMAP["MSG_FLBFILTERINFO_GROUP"]!}
						<img src="imgs/none.gif"/>
						<span class="none orderType">36</span>
						<span class="none orderDir">2</span>		
					</a>
				</#if>
			</span>
		</th>	
		<th class="align_center">${LANGCODEMAP["MSG_FLBFILTERINFO_REDIRECTION"]!}</th>
		</tr>
		<tr class="DivideLine">
			<td colspan="13"></td>
		</tr>
		<#if filterInfoList??>
			<#list filterInfoList as filterInfo>
				<#if 0 == filterInfo.state>
					<#assign imageFileName = "icon_gryDot.png">
					<#assign statusText = "Disabled">
				<#elseif 1 == filterInfo.state>
						<#assign imageFileName = "icon_greenDot.png">
						<#assign statusText = "${LANGCODEMAP['MSG_PERFALTEON_NORMAL']!}">
				<#elseif 2 == filterInfo.state>
						<#assign imageFileName = "icon_redDot.png">
						<#assign statusText = "${LANGCODEMAP['MSG_PERFALTEON_DISCONN']!}">
				<#elseif 3 == filterInfo.state>
						<#assign imageFileName = "icon_yellowDot.png">
						<#assign statusText = "${LANGCODEMAP['MSG_PERFALTEON_ABNORMAL']!}">
				</#if>	
				<tr class="ContentsLine1 trFlbInfoList">
				<td class="align_center">${filterInfo.filterId!"-"}</td>
				<td class="align_center"><img src="imgs/icon/${imageFileName}" alt="${statusText}" /></td>
				<td class="textOver" title="${filterInfo.name!''}">${filterInfo.name!"-"}</td>
				<td class="align_center textOver">${filterInfo.srcIP!"-"}</td>
				<td class="align_center textOver">${filterInfo.srcMask!"-"}</td>
				<td class="align_center textOver">${filterInfo.dstIP!"-"}</td>
				<td class="align_center textOver">${filterInfo.dstMask!"-"}</td>
				<td class="align_center">${filterInfo.protocol!"-"}</td>
				<#if filterInfo.srcPortFrom == filterInfo.srcPortTo>
					<td class="align_center">${filterInfo.srcPortFrom!"-"}</td>
				<#else>
					<td class="align_center">${filterInfo.srcPortFrom!"-"} - ${filterInfo.srcPortTo!"-"}</td>
				</#if>
				<#if filterInfo.dstPortFrom == filterInfo.dstPortTo>
					<td class="align_center">${filterInfo.dstPortFrom!"-"}</td>
				<#else>
					<td class="align_center">${filterInfo.dstPortFrom!"-"} - ${filterInfo.dstPortTo!"-"}</td>
				</#if>
				<td class="align_center">${filterInfo.action!"-"}</td>	
				<td class="align_center">${filterInfo.group!"-"}</td>	
				<td class="align_center">${filterInfo.redirection!"-"}</td>
				</tr>
				<tr class="DivideLine">
					<td colspan="13"></td>
				</tr>
			</#list>
		</#if>
						
		<tr class="EndLine">
			<td colspan="13"></td>
		</tr>