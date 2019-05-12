<#if orderOption.orderType??>
	<#if 33 == orderOption.orderType></#if><!-- MemberIPAddress -->
	<#if 34 == orderOption.orderType></#if><!-- port -->
	<#if 35 == orderOption.orderType></#if><!-- MemberName -->
	<#if 36 == orderOption.orderType></#if><!-- bpsIn -->	
	<#if 37 == orderOption.orderType></#if><!-- bpsOut -->	
	<#if 38 == orderOption.orderType></#if><!-- bpsTotal -->	
	<#if 46 == orderOption.orderType></#if><!-- ConcurrentSessions -->
</#if>
<#if orderOption.orderDirection??>
	<#if 1 == orderOption.orderDirection></#if><!--Asc-->
	<#if 2 == orderOption.orderDirection></#if><!--Desc-->
</#if>	
<caption></caption>
<colgroup>
	<col width="10px" />
	<col width="15%" />
	<col width="10%" />
	<col width="20%" />
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
	    <th colspan="4" class="ContentsHeadLine Rcolor">Virtual Service</th>
		<th colspan="3" class="ContentsHeadLine Rcolor">bps</th>
		<th rowspan="3" class="ContentsHeadLine ">		
			<span class="css_textCursor">
				<#if orderOption.orderDirection == 2 && orderOption.orderType == 46>		
					<a class="orderDir_Desc">Concurrent
						<img src="imgs/Desc.gif"/>
						<span class="none orderType">46</span>
						<span class="none orderDir">1</span>
						<br>Sessions(${LANGCODEMAP["MSG_APPLIANCE_COUNT"]!})					
					</a>						
				<#elseif orderOption.orderDirection == 1 && orderOption.orderType == 46>	
					<a class="orderDir_Asc">Concurrent
						<img src="imgs/Asc.gif"/>
						<span class="none orderType">46</span>
						<span class="none orderDir">2</span>	
						<br>Sessions(${LANGCODEMAP["MSG_APPLIANCE_COUNT"]!})
					</a>
				<#else>
					<a class="orderDir_None">Concurrent
						<img src="imgs/none.gif"/>
						<span class="none orderType">46</span>
						<span class="none orderDir">2</span>
						<br>Sessions(${LANGCODEMAP["MSG_APPLIANCE_COUNT"]!})		
					</a>
				</#if>
			</span>						
		</th>			
	</tr>	
    <tr>
 		<td colspan="4" class="DivideLine1"></td>	
 		<td colspan="3" class="DivideLine1"></td>		 		 				
    </tr>	
	<tr class="ContentsHeadLine1">
		<input class ="none" value="2" name="orderDir_Desc"/>
        <input class ="none" value="1" name="orderDir_Asc"/>
        <th colspan="2" >
			<span class="css_textCursor">
				<#if orderOption.orderDirection == 2 && orderOption.orderType == 33>		
					<a class="orderDir_Desc">IP
						<img src="imgs/Desc.gif"/>
						<span class="none orderType">33</span>
						<span class="none orderDir">1</span>					
					</a>						
				<#elseif orderOption.orderDirection == 1 && orderOption.orderType == 33>	
					<a class="orderDir_Asc">IP
						<img src="imgs/Asc.gif"/>
						<span class="none orderType">33</span>
						<span class="none orderDir">2</span>		
					</a>
				<#else>
					<a class="orderDir_None">IP
						<img src="imgs/none.gif"/>
						<span class="none orderType">33</span>
						<span class="none orderDir">2</span>		
					</a>
				</#if>
			</span>
		</th>	
	  	<th>
			<span class="css_textCursor">
				<#if orderOption.orderDirection == 2 && orderOption.orderType == 34>		
					<a class="orderDir_Desc">${LANGCODEMAP["MSG_PERFALTEON_PORT"]!}
						<img src="imgs/Desc.gif"/>
						<span class="none orderType">34</span>
						<span class="none orderDir">1</span>					
					</a>						
				<#elseif orderOption.orderDirection == 1 && orderOption.orderType == 34>	
					<a class="orderDir_Asc">${LANGCODEMAP["MSG_PERFALTEON_PORT"]!}
						<img src="imgs/Asc.gif"/>
						<span class="none orderType">34</span>
						<span class="none orderDir">2</span>		
					</a>
				<#else>
					<a class="orderDir_None">${LANGCODEMAP["MSG_PERFALTEON_PORT"]!}
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
					<a class="orderDir_Desc">${LANGCODEMAP["MSG_PERFALTEON_NAME"]!}
						<img src="imgs/Desc.gif"/>
						<span class="none orderType">35</span>
						<span class="none orderDir">1</span>					
					</a>						
				<#elseif orderOption.orderDirection == 1 && orderOption.orderType == 35>	
					<a class="orderDir_Asc">${LANGCODEMAP["MSG_PERFALTEON_NAME"]!}
						<img src="imgs/Asc.gif"/>
						<span class="none orderType">35</span>
						<span class="none orderDir">2</span>		
					</a>
				<#else>
					<a class="orderDir_None">${LANGCODEMAP["MSG_PERFALTEON_NAME"]!}
						<img src="imgs/none.gif"/>
						<span class="none orderType">35</span>
						<span class="none orderDir">2</span>		
					</a>
				</#if>
			</span>			  	
	  	</th>
	  	<th>
	  		<span class="css_textCursor">
				<#if orderOption.orderDirection == 2 && orderOption.orderType == 36>		
					<a class="orderDir_Desc">In
						<img src="imgs/Desc.gif"/>
						<span class="none orderType">36</span>
						<span class="none orderDir">1</span>					
					</a>						
				<#elseif orderOption.orderDirection == 1 && orderOption.orderType == 36>	
					<a class="orderDir_Asc">In
						<img src="imgs/Asc.gif"/>
						<span class="none orderType">36</span>
						<span class="none orderDir">2</span>		
					</a>
				<#else>
					<a class="orderDir_None">In
						<img src="imgs/none.gif"/>
						<span class="none orderType">36</span>
						<span class="none orderDir">2</span>		
					</a>
				</#if>
			</span>
	  	</th>
	  	<th>
	  		<span class="css_textCursor">
				<#if orderOption.orderDirection == 2 && orderOption.orderType == 37>		
					<a class="orderDir_Desc">Out
						<img src="imgs/Desc.gif"/>
						<span class="none orderType">37</span>
						<span class="none orderDir">1</span>					
					</a>						
				<#elseif orderOption.orderDirection == 1 && orderOption.orderType == 37>	
					<a class="orderDir_Asc">Out
						<img src="imgs/Asc.gif"/>
						<span class="none orderType">37</span>
						<span class="none orderDir">2</span>		
					</a>
				<#else>
					<a class="orderDir_None">Out
						<img src="imgs/none.gif"/>
						<span class="none orderType">37</span>
						<span class="none orderDir">2</span>		
					</a>
				</#if>
			</span>
	  	</th>
	  	<th class="Rcolor">
			<span class="css_textCursor">
				<#if orderOption.orderDirection == 2 && orderOption.orderType == 38>		
					<a class="orderDir_Desc">${LANGCODEMAP["MSG_PERFALTEON_TOTAL"]!}
						<img src="imgs/Desc.gif"/>
						<span class="none orderType">38</span>
						<span class="none orderDir">1</span>					
					</a>						
				<#elseif orderOption.orderDirection == 1 && orderOption.orderType == 38>	
					<a class="orderDir_Asc">${LANGCODEMAP["MSG_PERFALTEON_TOTAL"]!}
						<img src="imgs/Asc.gif"/>
						<span class="none orderType">38</span>
						<span class="none orderDir">2</span>		
					</a>
				<#else>
					<a class="orderDir_None">${LANGCODEMAP["MSG_PERFALTEON_TOTAL"]!}
						<img src="imgs/none.gif"/>
						<span class="none orderType">38</span>
						<span class="none orderDir">2</span>		
					</a>
				</#if>
			</span>			  	
	  	</th>
	<tr>
		<td colspan="8" class="StartLine1"></td>	
	</tr>
</thead>
<tbody>
    <tr class="ContentsLine1" id="${vsTrafficInfo.vsIndex!""}" idx = "-1">
	    <td colspan="2" class="align_left_P10" id="ipaddress">${vsTrafficInfo.ipAddress!""}</td>
	    <td class="align_center">
	    	<span class="vsPort"><#setting number_format="0.####">${vsTrafficInfo.port!0}</span>
	    	<span class="vsIndex" style="display: none;">${vsTrafficInfo.vsIndex!""}</span>
	    	<span class="vsName" style="display: none;">${vsTrafficInfo.name!""}</span>
	    </td>
		<td class="align_left_P10 Rcolor textOver" title="${vsTrafficInfo.name!""}">${vsTrafficInfo.name!""}</td>
		<td class="align_center">${vsTrafficInfo.inBps!"-"}</td>
		<td class="align_center">${vsTrafficInfo.outBps!"-"}</td>
		<td class="align_center Rcolor">${vsTrafficInfo.totalBps!"-"}</td>
		<td class="align_center">${vsTrafficInfo.activeConnections!"-"}</td>
	</tr>
    <tr class="DivideLine">
    	<td colspan="8" ></td>	
	
	<#if vsTrafficInfo.vsMemberTrafficInfoList??>
	<#list vsTrafficInfo.vsMemberTrafficInfoList as vsMemberTrafficInfo>
		<#assign imageFileName = "">
		<#assign statusText = "">
		<#if vsMemberTrafficInfo.status??>
			<#if 0 == vsMemberTrafficInfo.status>
				<#assign imageFileName = "icon_gryDot.png">
				<#assign statusText = "Disabled">
			<#elseif 1 == vsMemberTrafficInfo.status>
				<#assign imageFileName = "icon_greenDot.png">
				<#assign statusText = "${LANGCODEMAP['MSG_PERFALTEON_NORMAL']!}">
			<#elseif 2 == vsMemberTrafficInfo.status>
				<#assign imageFileName = "icon_redDot.png">
				<#assign statusText = "${LANGCODEMAP['MSG_PERFALTEON_DISCONN']!}">
			<#elseif 3 == vsMemberTrafficInfo.status>
				<#assign imageFileName = "icon_yellowDot.png">
				<#assign statusText = "${LANGCODEMAP['MSG_PERFALTEON_ABNORMAL']!}">
			</#if>
		</#if>

    </tr>						
	<tr class="ContentsLine1" id="${vsMemberTrafficInfo.index!""}">
    	<td></td>	
		<td class="align_left_P10">
			<img src="imgs/icon/icon_member.png" alt="member" /> ${vsMemberTrafficInfo.ipAddress!""}
			<span class="memberIp" style="display: none;">${vsMemberTrafficInfo.ipAddress!""}</span>
		</td>
		<td class="align_center">
		<#if (vsMemberTrafficInfo.addPort)?? && (vsMemberTrafficInfo.port == 0)>
			${vsMemberTrafficInfo.addPort!0}
		<#else>
			${vsMemberTrafficInfo.port!0}
		</#if>			
			<!--
			<#if (adc.type)?? && "F5" == adc.type>
				${vsMemberTrafficInfo.port!0}
			<#elseif (adc.type)?? && "PAS" == adc.type>
				${vsMemberTrafficInfo.port!0}
			<#else>
				-
			</#if>
			-->
		</td>
		<td class="align_left_P20 Rcolor textOver">-</td>
		<td class="align_center">${vsMemberTrafficInfo.inBps!"-"}</td>
		<td class="align_center">${vsMemberTrafficInfo.outBps!"-"}</td>
		<td class="align_center Rcolor">${vsMemberTrafficInfo.totalBps!"-"}</td>
		<td class="align_center">${vsMemberTrafficInfo.activeConnections!"-"}</td>
	</tr>	
    <tr class="DivideLine" >
    	<td colspan="8" ></td>
    </tr>
	</#list>
	</#if>
    <tr class="EndLine" >
    	<td colspan="8" ></td>
    </tr>	
</tbody>
