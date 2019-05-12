<#if orderOption.orderType??>
	<#if 34 == orderOption.orderType></#if><!-- vsIpaddress -->
	<#if 35 == orderOption.orderType></#if><!-- port -->
	<#if 36 == orderOption.orderType></#if><!-- vsName -->
	<#if 37 == orderOption.orderType></#if><!-- response -->
	<#if 38 == orderOption.orderType></#if><!-- bpsIn -->	
	<#if 39 == orderOption.orderType></#if><!-- bpsOut -->	
	<#if 40 == orderOption.orderType></#if><!-- bpsTotal -->	
	<#if 46 == orderOption.orderType></#if><!-- ConcurrentSessions -->
</#if>
<#if orderOption.orderDirection??>
	<#if 1 == orderOption.orderDirection></#if><!--Asc-->
	<#if 2 == orderOption.orderDirection></#if><!--Desc-->
</#if>	
<caption></caption>
<colgroup>
	<col width="5%" />
	<col width="20%" />
	<col width="10%" />
	<col width="20%" />	
	<col width="12%" />
	<col width="7%" />
	<col width="7%" />
	<col width="8%" />
	<col width="11%" />
</colgroup>
<thead>
	<tr>
		<td colspan="9" class="StartLine"></td>			
	</tr>
	<tr>
		<th rowspan="3" class="ContentsHeadLine bg_row2 Rcolor">${LANGCODEMAP["MSG_PERFALTEON_STATUS"]!}</th>
	    <th colspan="3" class="ContentsHeadLine bg_row2 Rcolor">Virtual Service</th>
	    
	    <th rowspan="3" class="ContentsHeadLine bg_row2 Rcolor">
	    	<span class="css_textCursor">
	    		<#if orderOption.orderDirection == 2 && orderOption.orderType == 37>		
					<a class="orderDir_Desc">${LANGCODEMAP["MSG_PERFALTEON_RESPONSE_TIME"]!}
						<img src="imgs/Desc.gif"/>
						<span class="none orderType">37</span>
						<span class="none orderDir">1</span>
						<br>(ms)&nbsp;&nbsp;				
					</a>						
				<#elseif orderOption.orderDirection == 1 && orderOption.orderType == 37>	
					<a class="orderDir_Asc">${LANGCODEMAP["MSG_PERFALTEON_RESPONSE_TIME"]!}
						<img src="imgs/Asc.gif"/>
						<span class="none orderType">37</span>
						<span class="none orderDir">2</span>
						<br>(ms)&nbsp;&nbsp;	
					</a>
				<#else>
					<a class="orderDir_None">${LANGCODEMAP["MSG_PERFALTEON_RESPONSE_TIME"]!}
						<img src="imgs/none.gif"/>
						<span class="none orderType">37</span>
						<span class="none orderDir">2</span>
						<br>(ms)&nbsp;&nbsp;		
					</a>
				</#if>
	    	</span>	    
	    </th>
		<th colspan="3" class="ContentsHeadLine Rcolor">bps</th>
		<th rowspan="3" class="ContentsHeadLine">		
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
 		<td colspan="3" class="DivideLine1 Rcolor"></td>	
		<td colspan="3" class="DivideLine1"></td>		
    </tr>	
	<tr class="ContentsHeadLine1">
		<input class ="none" value="2" name="orderDir_Desc"/>
        <input class ="none" value="1" name="orderDir_Asc"/>
        <th>
			<span class="css_textCursor">
				<#if orderOption.orderDirection == 2 && orderOption.orderType == 34>		
					<a class="orderDir_Desc">IP
						<img src="imgs/Desc.gif"/>
						<span class="none orderType">34</span>
						<span class="none orderDir">1</span>					
					</a>						
				<#elseif orderOption.orderDirection == 1 && orderOption.orderType == 34>	
					<a class="orderDir_Asc">IP
						<img src="imgs/Asc.gif"/>
						<span class="none orderType">34</span>
						<span class="none orderDir">2</span>		
					</a>
				<#else>
					<a class="orderDir_None">IP
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
					<a class="orderDir_Desc">${LANGCODEMAP["MSG_PERFALTEON_PORT"]!}
						<img src="imgs/Desc.gif"/>
						<span class="none orderType">35</span>
						<span class="none orderDir">1</span>					
					</a>						
				<#elseif orderOption.orderDirection == 1 && orderOption.orderType == 35>	
					<a class="orderDir_Asc">${LANGCODEMAP["MSG_PERFALTEON_PORT"]!}
						<img src="imgs/Asc.gif"/>
						<span class="none orderType">35</span>
						<span class="none orderDir">2</span>		
					</a>
				<#else>
					<a class="orderDir_None">${LANGCODEMAP["MSG_PERFALTEON_PORT"]!}
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
					<a class="orderDir_Desc">${LANGCODEMAP["MSG_PERFALTEON_NAME"]!}
						<img src="imgs/Desc.gif"/>
						<span class="none orderType">36</span>
						<span class="none orderDir">1</span>					
					</a>						
				<#elseif orderOption.orderDirection == 1 && orderOption.orderType == 36>	
					<a class="orderDir_Asc">${LANGCODEMAP["MSG_PERFALTEON_NAME"]!}
						<img src="imgs/Asc.gif"/>
						<span class="none orderType">36</span>
						<span class="none orderDir">2</span>		
					</a>
				<#else>
					<a class="orderDir_None">${LANGCODEMAP["MSG_PERFALTEON_NAME"]!}
						<img src="imgs/none.gif"/>
						<span class="none orderType">36</span>
						<span class="none orderDir">2</span>		
					</a>
				</#if>
			</span>			  	
	  	</th>
	  	<th>
	  		<span class="css_textCursor">
				<#if orderOption.orderDirection == 2 && orderOption.orderType == 38>		
					<a class="orderDir_Desc">In
						<img src="imgs/Desc.gif"/>
						<span class="none orderType">38</span>
						<span class="none orderDir">1</span>					
					</a>						
				<#elseif orderOption.orderDirection == 1 && orderOption.orderType == 38>	
					<a class="orderDir_Asc">In
						<img src="imgs/Asc.gif"/>
						<span class="none orderType">38</span>
						<span class="none orderDir">2</span>		
					</a>
				<#else>
					<a class="orderDir_None">In
						<img src="imgs/none.gif"/>
						<span class="none orderType">38</span>
						<span class="none orderDir">2</span>		
					</a>
				</#if>
			</span>
		</th>
	  	<th>
	  		<span class="css_textCursor">
				<#if orderOption.orderDirection == 2 && orderOption.orderType == 39>		
					<a class="orderDir_Desc">Out
						<img src="imgs/Desc.gif"/>
						<span class="none orderType">39</span>
						<span class="none orderDir">1</span>					
					</a>						
				<#elseif orderOption.orderDirection == 1 && orderOption.orderType == 39>	
					<a class="orderDir_Asc">Out
						<img src="imgs/Asc.gif"/>
						<span class="none orderType">39</span>
						<span class="none orderDir">2</span>		
					</a>
				<#else>
					<a class="orderDir_None">Out
						<img src="imgs/none.gif"/>
						<span class="none orderType">39</span>
						<span class="none orderDir">2</span>		
					</a>
				</#if>
			</span>
	  	</th>
	  	<th class="Rcolor">
			<span class="css_textCursor">
				<#if orderOption.orderDirection == 2 && orderOption.orderType == 40>		
					<a class="orderDir_Desc">Total
						<img src="imgs/Desc.gif"/>
						<span class="none orderType">40</span>
						<span class="none orderDir">1</span>					
					</a>						
				<#elseif orderOption.orderDirection == 1 && orderOption.orderType == 40>	
					<a class="orderDir_Asc">Total
						<img src="imgs/Asc.gif"/>
						<span class="none orderType">40</span>
						<span class="none orderDir">2</span>		
					</a>
				<#else>
					<a class="orderDir_None">Total
						<img src="imgs/none.gif"/>
						<span class="none orderType">40</span>
						<span class="none orderDir">2</span>		
					</a>
				</#if>
			</span>			  	
	  	</th>			
	</tr>
	<tr>				
		<td colspan="9" class="StartLine1"></td>	
	</tr>
</thead>
<tbody class="vsTrafficInfoList">
	<#if svcPerfInfoList??>
		<#list svcPerfInfoList as svcPerfInfo>
			<#if 0 == svcPerfInfo.vsStatus>
				<#assign imageFileName = "icon_vs_disabled.png">
				<#assign statusText = "Disabled">
			<#elseif 1 == svcPerfInfo.vsStatus>
					<#assign imageFileName = "icon_vs_conn.png">
					<#assign statusText = "${LANGCODEMAP['MSG_PERFALTEON_NORMAL']!}">
			<#elseif 2 == svcPerfInfo.vsStatus>
					<#assign imageFileName = "icon_vs_disconn.png">
					<#assign statusText = "${LANGCODEMAP['MSG_PERFALTEON_DISCONN']!}">
			<#elseif 3 == svcPerfInfo.vsStatus>
					<#assign imageFileName = "icon_yellowDot.png">
					<#assign statusText = "${LANGCODEMAP['MSG_PERFALTEON_ABNORMAL']!}"></#if>
					
			<tr class="ContentsLine1 servicePerformList" id="${svcPerfInfo.vsIndex}">
				<td class="align_center Rcolor">
					<span class="align_center">
						<img src="imgs/icon/${imageFileName}" alt="${statusText}" />
					</span>
				</td>
	    		<td class="align_left_P10">
	    			<span class="vsvcIndex none">${svcPerfInfo.vsvcIndex}</span>
	    			<a href="#" class="trafficDetailLnk" id="ipaddress">${svcPerfInfo.vsIP!""}</a>
	    		</td>
	    		<td class="align_center">
	    			<span class="vsPort"><#setting number_format="0.####">${svcPerfInfo.vsPort!0}</span>
	    		</td>
				<td class="align_left_P10 Rcolor textOver">
					<span class="vsName" title="${svcPerfInfo.vsName!""}">${svcPerfInfo.vsName!""}</span>			
				</td>
				<td class="align_center Rcolor">${svcPerfInfo.responseTime!"-"}</td>
				<td class="align_center">${svcPerfInfo.bpsIn!"-"}</td>
				<td class="align_center">${svcPerfInfo.bpsOut!"-"}</td>
				<td class="align_center Rcolor">${svcPerfInfo.bpsTotal!"-"}</td>
				<td class="align_center">${svcPerfInfo.connCurr!"-"}</td>				
			</tr>
			<tr>		
				<td colspan="9" class="DivideLine1"></td>
			</tr>
		</#list>
	</#if>
	<tr>
		<td colspan="9" class="EndLine "></td>	
	</tr>
</tbody>
