<colgroup>
  	<col width="25%"/>
	<col width="25%"/>
	<col width="25%"/>
	<col width="25%"/>
</colgroup>
<thead>                 
	<tr class="StartLine">
		<td colspan="4"></td>
	</tr>
	<tr class="ContentsHeadLine">
		<th>
			<span class="css_textCursor">
				<#if orderObj.orderDirection == 2 && orderObj.orderType == 33>		
					<a class="orderDir_Desc">IP
						<img src="imgs/Desc.gif"/>
						<span class="none orderType">33</span>
						<span class="none orderDir">1</span>					
					</a>						
				<#elseif orderObj.orderDirection == 1 && orderObj.orderType == 33>	
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
				<#if orderObj.orderDirection == 2 && orderObj.orderType == 34>		
					<a class="orderDir_Desc">MAC
						<img src="imgs/Desc.gif"/>
						<span class="none orderType">34</span>
						<span class="none orderDir">1</span>					
					</a>						
				<#elseif orderObj.orderDirection == 1 && orderObj.orderType == 34>	
					<a class="orderDir_Asc">MAC
						<img src="imgs/Asc.gif"/>
						<span class="none orderType">34</span>
						<span class="none orderDir">2</span>		
					</a>
				<#else>
					<a class="orderDir_None">MAC
						<img src="imgs/none.gif"/>
						<span class="none orderType">34</span>
						<span class="none orderDir">2</span>		
					</a>
				</#if>
			</span>					
		</th>
		<th>
			<span class="css_textCursor">
				<#if orderObj.orderDirection == 2 && orderObj.orderType == 35>		
					<a class="orderDir_Desc">VLAN
						<img src="imgs/Desc.gif"/>
						<span class="none orderType">35</span>
						<span class="none orderDir">1</span>					
					</a>						
				<#elseif orderObj.orderDirection == 1 && orderObj.orderType == 35>	
					<a class="orderDir_Asc">VLAN
						<img src="imgs/Asc.gif"/>
						<span class="none orderType">35</span>
						<span class="none orderDir">2</span>		
					</a>
				<#else>
					<a class="orderDir_None">VLAN
						<img src="imgs/none.gif"/>
						<span class="none orderType">35</span>
						<span class="none orderDir">2</span>		
					</a>
				</#if>
			</span>					
		</th>
		<th>
			<span class="css_textCursor">
				<#if orderObj.orderDirection == 2 && orderObj.orderType == 36 >		
					<a class="orderDir_Desc">PORT
						<img src="imgs/Desc.gif"/>
						<span class="none orderType">36</span>
						<span class="none orderDir">1</span>					
					</a>						
				<#elseif orderObj.orderDirection == 1 && orderObj.orderType == 36>	
					<a class="orderDir_Asc">PORT
						<img src="imgs/Asc.gif"/>
						<span class="none orderType">36</span>
						<span class="none orderDir">2</span>		
					</a>
				<#else>
					<a class="orderDir_None">PORT
						<img src="imgs/none.gif"/>
						<span class="none orderType">36</span>
						<span class="none orderDir">2</span>		
					</a>
				</#if>
			</span>					
		</th>
	</tr>
	<tr class="StartLine1">
		<td colspan="4"></td>
	</tr>
</thead>
<tbody class="sessionListTable">
	<#if l2InfoList??>
		<#list l2InfoList as l2Info>
			<tr class="ContentsLine3 l2List">	
				<td class="align_left_P10 textOver" >${l2Info.ipAddress!"-"}</td>
				<td class="align_left_P10 textOver" >${l2Info.macAddress!"-"}</td>
				<td class="align_center textOver" >${l2Info.vlanInfo!"-"}</td>
				<td class="align_center textOver" >
					<#if l2Info.interfaceInfo == "0">	
						-
					<#else>
						${l2Info.interfaceInfo!"-"}
					</#if>
				</td> 
			</tr>
			<tr class="DivideLine">
				<td colspan="4"></td>
			</tr>
		</#list>
	</#if>	
	<tr class="EndLine">
		<td colspan="4"></td>
	</tr>		
</tbody>