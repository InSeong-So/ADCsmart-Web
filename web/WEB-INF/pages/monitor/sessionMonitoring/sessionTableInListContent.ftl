<colgroup>
	<#if adc.type == 'Alteon'>
	  	<col width="15%"/>
		<col width="10%"/>
		<col width="15%"/>
		<col width="15%"/>		
		<col width="15%"/>                         
		<col width="10%"/>                            
		<col width="10%"/>                         
		<col width="10%"/>
	<#else>
	  	<col width="15%"/>
		<col width="10%"/>
		<col width="15%"/>
		<col width="15%"/>		
		<col width="15%"/>                         
		<col width="10%"/>                            
		<col width="10%"/>                         
		<col width="10%"/>
	</#if>	
</colgroup>
<thead>                 
	<tr class="StartLine">
		<#if adc.type == 'Alteon'>
			<td colspan="8"></td>
		<#else>
			<td colspan="8"></td>
		</#if>	
	</tr>
	<tr class="ContentsHeadLine">
		<th>
			<span class="css_textCursor">
				<#if orderObj.orderDirection == 2 && orderObj.orderType == 33>		
					<a class="orderDir_Desc">${LANGCODEMAP["MSG_SESSION_CIP"]!}
						<img src="imgs/Desc.gif"/>
						<span class="none orderType">33</span>
						<span class="none orderDir">1</span>					
					</a>						
				<#elseif orderObj.orderDirection == 1 && orderObj.orderType == 33>	
					<a class="orderDir_Asc">${LANGCODEMAP["MSG_SESSION_CIP"]!}
						<img src="imgs/Asc.gif"/>
						<span class="none orderType">33</span>
						<span class="none orderDir">2</span>		
					</a>
				<#else>
					<a class="orderDir_None">${LANGCODEMAP["MSG_SESSION_CIP"]!}
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
					<a class="orderDir_Desc">${LANGCODEMAP["MSG_SESSION_CPORT"]!}
						<img src="imgs/Desc.gif"/>
						<span class="none orderType">34</span>
						<span class="none orderDir">1</span>					
					</a>						
				<#elseif orderObj.orderDirection == 1 && orderObj.orderType == 34>	
					<a class="orderDir_Asc">${LANGCODEMAP["MSG_SESSION_CPORT"]!}
						<img src="imgs/Asc.gif"/>
						<span class="none orderType">34</span>
						<span class="none orderDir">2</span>		
					</a>
				<#else>
					<a class="orderDir_None">${LANGCODEMAP["MSG_SESSION_CPORT"]!}
						<img src="imgs/none.gif"/>
						<span class="none orderType">34</span>
						<span class="none orderDir">2</span>		
					</a>
				</#if>
			</span>					
		</th>
		<#if selectedOption == 'SLB'>
			<th>
				<span class="css_textCursor">
					<#if orderObj.orderDirection == 2 && orderObj.orderType == 35>		
						<a class="orderDir_Desc">${LANGCODEMAP["MSG_SESSION_VIP"]!}
							<img src="imgs/Desc.gif"/>
							<span class="none orderType">35</span>
							<span class="none orderDir">1</span>					
						</a>						
					<#elseif orderObj.orderDirection == 1 && orderObj.orderType == 35>	
						<a class="orderDir_Asc">${LANGCODEMAP["MSG_SESSION_VIP"]!}
							<img src="imgs/Asc.gif"/>
							<span class="none orderType">35</span>
							<span class="none orderDir">2</span>		
						</a>
					<#else>
						<a class="orderDir_None">${LANGCODEMAP["MSG_SESSION_VIP"]!}
							<img src="imgs/none.gif"/>
							<span class="none orderType">35</span>
							<span class="none orderDir">2</span>		
						</a>
					</#if>			
				</span>					
			</th>
		<#else>
			<th>
				<span class="css_textCursor">
					<#if orderObj.orderDirection == 2 && orderObj.orderType == 35>		
						<a class="orderDir_Desc">${LANGCODEMAP["MSG_SESSION_DIP"]!}
							<img src="imgs/Desc.gif"/>
							<span class="none orderType">35</span>
							<span class="none orderDir">1</span>					
						</a>						
					<#elseif orderObj.orderDirection == 1 && orderObj.orderType == 35>	
						<a class="orderDir_Asc">${LANGCODEMAP["MSG_SESSION_DIP"]!}
							<img src="imgs/Asc.gif"/>
							<span class="none orderType">35</span>
							<span class="none orderDir">2</span>		
						</a>
					<#else>
						<a class="orderDir_None">${LANGCODEMAP["MSG_SESSION_DIP"]!}
							<img src="imgs/none.gif"/>
							<span class="none orderType">35</span>
							<span class="none orderDir">2</span>		
						</a>
					</#if>			
				</span>					
			</th>
		</#if>
		<#if selectedOption == 'SLB'>
			<th>		
				<span class="css_textCursor">
					<#if orderObj.orderDirection == 2 && orderObj.orderType == 36 >		
						<a class="orderDir_Desc">${LANGCODEMAP["MSG_SESSION_VPORT"]!}
							<img src="imgs/Desc.gif"/>
							<span class="none orderType">36</span>
							<span class="none orderDir">1</span>					
						</a>						
					<#elseif orderObj.orderDirection == 1 && orderObj.orderType == 36>	
						<a class="orderDir_Asc">${LANGCODEMAP["MSG_SESSION_VPORT"]!}
							<img src="imgs/Asc.gif"/>
							<span class="none orderType">36</span>
							<span class="none orderDir">2</span>		
						</a>
					<#else>
						<a class="orderDir_None">${LANGCODEMAP["MSG_SESSION_VPORT"]!}
							<img src="imgs/none.gif"/>
							<span class="none orderType">36</span>
							<span class="none orderDir">2</span>		
						</a>
					</#if>
				</span>					
			</th>		
		<#else>
			<th>		
				<span class="css_textCursor">
					<#if orderObj.orderDirection == 2 && orderObj.orderType == 36 >		
						<a class="orderDir_Desc">${LANGCODEMAP["MSG_SESSION_DPORT"]!}
							<img src="imgs/Desc.gif"/>
							<span class="none orderType">36</span>
							<span class="none orderDir">1</span>					
						</a>						
					<#elseif orderObj.orderDirection == 1 && orderObj.orderType == 36>	
						<a class="orderDir_Asc">${LANGCODEMAP["MSG_SESSION_DPORT"]!}
							<img src="imgs/Asc.gif"/>
							<span class="none orderType">36</span>
							<span class="none orderDir">2</span>		
						</a>
					<#else>
						<a class="orderDir_None">${LANGCODEMAP["MSG_SESSION_DPORT"]!}
							<img src="imgs/none.gif"/>
							<span class="none orderType">36</span>
							<span class="none orderDir">2</span>		
						</a>
					</#if>
				</span>					
			</th>
		</#if>		
		<th>
			<span class="css_textCursor">
				<#if orderObj.orderDirection == 2 && orderObj.orderType == 37>		
					<a class="orderDir_Desc">${LANGCODEMAP["MSG_SESSION_REALIP"]!}
						<img src="imgs/Desc.gif"/>
						<span class="none orderType">37</span>
						<span class="none orderDir">1</span>					
					</a>						
				<#elseif orderObj.orderDirection == 1 && orderObj.orderType == 37>	
					<a class="orderDir_Asc">${LANGCODEMAP["MSG_SESSION_REALIP"]!}
						<img src="imgs/Asc.gif"/>
						<span class="none orderType">37</span>
						<span class="none orderDir">2</span>		
					</a>
				<#else>
					<a class="orderDir_None">${LANGCODEMAP["MSG_SESSION_REALIP"]!}
						<img src="imgs/none.gif"/>
						<span class="none orderType">37</span>
						<span class="none orderDir">2</span>		
					</a>
				</#if>
			</span>					
		</th>
		<#if selectedOption == 'SLB'>
			<th>
				<span class="css_textCursor">
					<#if orderObj.orderDirection == 2 && orderObj.orderType == 36 >		
						<a class="orderDir_Desc">${LANGCODEMAP["MSG_SESSION_REALPORT"]!}
							<img src="imgs/Desc.gif"/>
							<span class="none orderType">36</span>
							<span class="none orderDir">1</span>					
						</a>						
					<#elseif orderObj.orderDirection == 1 && orderObj.orderType == 36>	
						<a class="orderDir_Asc">${LANGCODEMAP["MSG_SESSION_REALPORT"]!}
							<img src="imgs/Asc.gif"/>
							<span class="none orderType">36</span>
							<span class="none orderDir">2</span>		
						</a>
					<#else>
						<a class="orderDir_None">${LANGCODEMAP["MSG_SESSION_REALPORT"]!}
							<img src="imgs/none.gif"/>
							<span class="none orderType">36</span>
							<span class="none orderDir">2</span>		
						</a>
					</#if>
				</span>					
			</th>
		<#else>
			<th>
				<span class="css_textCursor">
					<#if orderObj.orderDirection == 2 && orderObj.orderType == 36 >		
						<a class="orderDir_Desc">${LANGCODEMAP["MSG_SESSION_ACTION"]!}
							<img src="imgs/Desc.gif"/>
							<span class="none orderType">36</span>
							<span class="none orderDir">1</span>					
						</a>						
					<#elseif orderObj.orderDirection == 1 && orderObj.orderType == 36>	
						<a class="orderDir_Asc">${LANGCODEMAP["MSG_SESSION_ACTION"]!}
							<img src="imgs/Asc.gif"/>
							<span class="none orderType">36</span>
							<span class="none orderDir">2</span>		
						</a>
					<#else>
						<a class="orderDir_None">${LANGCODEMAP["MSG_SESSION_ACTION"]!}
							<img src="imgs/none.gif"/>
							<span class="none orderType">36</span>
							<span class="none orderDir">2</span>		
						</a>
					</#if>
				</span>					
			</th>
		</#if>		
<!-----Alteon 전용	 ----->	
		<#if adc.type == 'Alteon'>
			<!--<th>Protocol</th>-->	
			<th>${LANGCODEMAP["MSG_SESSION_AGE_MINUTE"]!}</th>
			<th>SP</th>	
		<#else>
			<th>Protocol</th>	
			<th>CPU</th>	
		</#if>	
	</tr>
	<tr class="StartLine1">
		<#if adc.type == 'Alteon'>
			<td colspan="8"></td>	
		<#else>
			<td colspan="8"></td>
		</#if>	
	</tr>
</thead>
<tbody class="sessionListTable">
	<#list sessionList as sessionList>
	<tr class="ContentsLine3 sessionList">	
		<td class="align_left_P10 textOver" >${sessionList.srcIP!""}</td>
		<td class="align_center textOver" ><#setting number_format="0.####">${sessionList.srcPort!""}</td>
		<td class="align_left_P10 textOver" >${sessionList.dstIP!""}</td>
		<td class="align_center textOver" ><#setting number_format="0.####">${sessionList.dstPort!""}</td>	
		<td class="align_left_P10 textOver" >${sessionList.realIP!""}</td>
		<td class="align_center textOver" ><#setting number_format="0.####">${sessionList.realPort!""}</td>	
		<#if adc.type == 'Alteon'>			
			<td class="align_center textOver" >${sessionList.agingTime!""}</td>				
			<td class="align_center textOver" >${sessionList.spNumber!""}</td>
		<#else>
			<td class="align_center textOver" >${sessionList.protocol!""}</td>	
			<td class="align_center textOver" >${sessionList.spNumber!""}</td>
		</#if>
	</tr>

	<tr class="DivideLine">
		<#if adc.type == 'Alteon'>
			<td colspan="8"></td>	
		<#else>
			<td colspan="8"></td>
		</#if>	
	</tr>			
	</#list>
	<tr class="EndLine">
		<#if adc.type == 'Alteon'>
			<td colspan="8"></td>	
		<#else>
			<td colspan="8"></td>
		</#if>	
	</tr>				
</tbody>