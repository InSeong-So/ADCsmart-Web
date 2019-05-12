<#setting number_format="0.####">
<#if orderType??>
	<#if 33 == orderType></#if><!-- IP -->
	<#if 34 == orderType></#if><!-- 이름 -->
	<#if 35 == orderType></#if><!-- Ratio -->
	<#if 36 == orderType></#if><!-- state -->
	<#if 38 == orderType></#if><!-- status -->
	<#if 39 == orderType></#if><!-- adc 이름 -->
	<#if 40 == orderType></#if><!-- session -->
</#if>
<#if orderDir??>
	<#if 1 == orderDir></#if><!-- Asc -->
	<#if 2 == orderDir></#if><!-- Desc -->
</#if>
<#if orderGroupType??>
	<#if 37 == orderGroupType></#if><!-- IP -->
</#if>
<#if orderGroupDir??>
	<#if 1 == orderGroupDir></#if><!-- Asc -->
	<#if 2 == orderGroupDir></#if><!-- Desc -->
</#if>
<!----- Contents List Start ----->
	<!--<table class="Board_out" cellpadding="0" cellspacing="0" style="table-layout: fixed;">-->
		<colgroup>
		    <col width="60px">
		    <col width="60px">
		    <col width="100px">	  		    
			<col width="150px">
			<col width="200px">
			<col width="200px">
			<!--<col width="200px">--> <!--ratio-->
			<col width="200px">
			<#if (adcScope.level != 2)>
			<!--<col width="200px">-->
			<#else>
			<!--<col width="200px">-->
			</#if>
		</colgroup>
		<thead>
			<tr class="ContentsHeadLine">
				 <th class="bg_row2 selChk">
					<#if accountRole == 'readOnly'>
						<input class="allNodeChk" type="checkbox" disabled="disabled"/>
					<#else>	
						<input class="allNodeChk" type="checkbox"/>
					</#if>
				</th>
				<th class="bg_row2 default">
					<span class="css_textCursor">
					<#if orderType ??>
						<#if orderDir == 2 && orderType == 38>		
							<a class="orderDir_Desc">상태
								<img src="imgs/Desc.gif"/>
								<span class="none orderType">38</span>
								<span class="none orderDir">1</span>					
							</a>						
						<#elseif orderDir == 1 && orderType == 38>	
							<a class="orderDir_Asc">상태
								<img src="imgs/Asc.gif"/>
								<span class="none orderType">38</span>
								<span class="none orderDir">2</span>		
							</a>
						<#else>
							<a class="orderDir_None">상태
								<img src="imgs/none.gif"/>
								<span class="none orderType">38</span>
								<span class="none orderDir">2</span>		
							</a>
						</#if>
					<#else>
						<a class="orderDir_None">상태
							<img src="imgs/none.gif"/>
							<span class="none orderType">38</span>
							<span class="none orderDir">2</span>		
						</a>
					</#if>
					</span>				
				</th>
				<th class="bg_row2 default">
					<span class="css_textCursor">
					<#if orderType ??>
						<#if orderDir == 2 && orderType == 36>		
							<a class="orderDir_Desc">State
								<img src="imgs/Desc.gif"/>
								<span class="none orderType">36</span>
								<span class="none orderDir">1</span>					
							</a>						
						<#elseif orderDir == 1 && orderType == 36>	
							<a class="orderDir_Asc">State
								<img src="imgs/Asc.gif"/>
								<span class="none orderType">36</span>
								<span class="none orderDir">2</span>		
							</a>
						<#else>
							<a class="orderDir_None">State
								<img src="imgs/none.gif"/>
								<span class="none orderType">36</span>
								<span class="none orderDir">2</span>		
							</a>
						</#if>
					<#else>
						<a class="orderDir_None">State
							<img src="imgs/none.gif"/>
							<span class="none orderType">36</span>
							<span class="none orderDir">2</span>		
						</a>
					</#if>
					</span>				
				</th>				
				<th class="bg_row2 default">
					<span class="css_textCursor">
					<#if orderType ??>
						<#if orderDir == 2 && orderType == 33>		
							<a class="orderDir_Desc">Real Server IP
								<img src="imgs/Desc.gif"/>
								<span class="none orderType">33</span>
								<span class="none orderDir">1</span>					
							</a>						
						<#elseif orderDir == 1 && orderType == 33>	
							<a class="orderDir_Asc">Real Server IP
								<img src="imgs/Asc.gif"/>
								<span class="none orderType">33</span>
								<span class="none orderDir">2</span>		
							</a>
						<#else>
							<a class="orderDir_None">Real Server IP
								<img src="imgs/none.gif"/>
								<span class="none orderType">33</span>
								<span class="none orderDir">2</span>		
							</a>
						</#if>
					<#else>
						<a class="orderDir_None">Real Server IP
							<img src="imgs/none.gif"/>
							<span class="none orderType">33</span>
							<span class="none orderDir">2</span>		
						</a>
					</#if>
					</span>
				</th>
				<th class="bg_row2 default">
					<span class="css_textCursor">
					<#if orderType ??>
						<#if orderDir == 2 && orderType == 34>		
							<a class="orderDir_Desc">Real Server ${LANGCODEMAP["MSG_VSF5_NAME"]!}
								<img src="imgs/Desc.gif"/>
								<span class="none orderType">34</span>
								<span class="none orderDir">1</span>					
							</a>					
						<#elseif orderDir == 1 && orderType == 34>	
							<a class="orderDir_Asc">Real Server ${LANGCODEMAP["MSG_VSF5_NAME"]!}
								<img src="imgs/Asc.gif"/>
								<span class="none orderType">34</span>
								<span class="none orderDir">2</span>		
							</a>
						<#else>
							<a class="orderDir_None">Real Server ${LANGCODEMAP["MSG_VSF5_NAME"]!}
								<img src="imgs/none.gif"/>
								<span class="none orderType">34</span>
								<span class="none orderDir">2</span>		
							</a>
						</#if>
					<#else>
						<a class="orderDir_None">Real Server ${LANGCODEMAP["MSG_VSF5_NAME"]!}
							<img src="imgs/none.gif"/>
							<span class="none orderType">34</span>
							<span class="none orderDir">2</span>	
						</a>
					</#if>						
					</span>				
				</th>
				<th class="bg_row2 default">
					<span class="css_textCursor">
					<#if orderType ??>
						<#if orderDir == 2 && orderType == 40>		
							<a class="orderDir_Desc">${LANGCODEMAP["MSG_APPLIANCE_SESSION_COUNT"]!}(${LANGCODEMAP["MSG_APPLIANCE_COUNT"]!})
								<img src="imgs/Desc.gif"/>
								<span class="none orderType">40</span>
								<span class="none orderDir">1</span>					
							</a>					
						<#elseif orderDir == 1 && orderType == 40>	
							<a class="orderDir_Asc">${LANGCODEMAP["MSG_APPLIANCE_SESSION_COUNT"]!}(${LANGCODEMAP["MSG_APPLIANCE_COUNT"]!})
								<img src="imgs/Asc.gif"/>
								<span class="none orderType">40</span>
								<span class="none orderDir">2</span>		
							</a>
						<#else>
							<a class="orderDir_None">${LANGCODEMAP["MSG_APPLIANCE_SESSION_COUNT"]!}(${LANGCODEMAP["MSG_APPLIANCE_COUNT"]!})
								<img src="imgs/none.gif"/>
								<span class="none orderType">40</span>
								<span class="none orderDir">2</span>		
							</a>
						</#if>
					<#else>
						<a class="orderDir_None">${LANGCODEMAP["MSG_APPLIANCE_SESSION_COUNT"]!}(${LANGCODEMAP["MSG_APPLIANCE_COUNT"]!})
							<img src="imgs/none.gif"/>
							<span class="none orderType">40</span>
							<span class="none orderDir">2</span>	
						</a>
					</#if>						
					</span>				
				</th>
				<th class="bg_row2 none">
					<span class="css_textCursor">
					<#if orderType ??>
						<#if orderDir == 2 && orderType == 35>		
							<a class="orderDir_Desc">Ratio
								<img src="imgs/Desc.gif"/>
								<span class="none orderType">35</span>
								<span class="none orderDir">1</span>					
							</a>						
						<#elseif orderDir == 1 && orderType == 35>	
							<a class="orderDir_Asc">Ratio
								<img src="imgs/Asc.gif"/>
								<span class="none orderType">35</span>
								<span class="none orderDir">2</span>		
							</a>
						<#else>
							<a class="orderDir_None">Ratio
								<img src="imgs/none.gif"/>
								<span class="none orderType">35</span>
								<span class="none orderDir">2</span>		
							</a>
						</#if>
					<#else>
						<a class="orderDir_None">Ratio
							<img src="imgs/none.gif"/>
							<span class="none orderType">35</span>
							<span class="none orderDir">2</span>		
						</a>
					</#if>	
					</span>				
				</th>		
				<th class="bg_row2">Virtual Server (${LANGCODEMAP["MSG_ROLE_HAVE"]!}/ ${LANGCODEMAP["MSG_ROLE_NOT"]!})</th>
				<th class="bg_row2 none">
					<span class="css_textCursor">
					<#if orderType ??>
						<#if orderDir == 2 && orderType == 39>		
							<a class="orderDir_Desc">${LANGCODEMAP["MSG_ADCSETTING_ADC_NAME"]!}
								<img src="imgs/Desc.gif"/>
								<span class="none orderType">39</span>
								<span class="none orderDir">1</span>					
							</a>					
						<#elseif orderDir == 1 && orderType == 39>	
							<a class="orderDir_Asc">${LANGCODEMAP["MSG_ADCSETTING_ADC_NAME"]!}
								<img src="imgs/Asc.gif"/>
								<span class="none orderType">39</span>
								<span class="none orderDir">2</span>		
							</a>
						<#else>
							<a class="orderDir_None">${LANGCODEMAP["MSG_ADCSETTING_ADC_NAME"]!}
								<img src="imgs/none.gif"/>
								<span class="none orderType">39</span>
								<span class="none orderDir">2</span>		
							</a>
						</#if>
					<#else>
						<a class="orderDir_None">${LANGCODEMAP["MSG_ADCSETTING_ADC_NAME"]!}
							<img src="imgs/none.gif"/>
							<span class="none orderType">39</span>
							<span class="none orderDir">2</span>	
						</a>
					</#if>						
					</span>				
				</th>  			
			</tr>
		</thead>
		<tbody>			
			<tr class="ContentsLine1 nodeList_g none">
				<td class="center index"></td>
				<td class="center status"></td>
				<td class="center state"></td>
				<td class="ipAddress"></td>
				<td class="name"></td>
				<td class="center session"></td>
				<td class="center ratio none"></td>
				<td class="center vserverAllowed"></td>
			    <td class="adcName"></td>
			</tr>
			<#list nodeDetail as theNode>			
			<tr class="nodeList">				
				<td class="center index">				
				<#if ((theNode.adcStatus)?? && (theNode.adcStatus) == 0) || ((theNode.adcMode)?? && (theNode.adcMode) == 1) || (theNode.vserverAllowed)?size == 0>
					<input type="checkbox" id="node" class="nodeChk" value="${theNode.index!""}" disabled="disabled" />
				<#else>
					<input type="checkbox" id="node" class="nodeChk" value="${theNode.index!""}" />
				</#if>
				</td>
				<td class="center status">			
					<#if ((theNode.status)!'') == 1>
					<input id="inputStatus" type="hidden" value="${theNode.status}">					
						<img src="imgs/icon/icon_2d_1.png" alt="available" />						
					<#elseif ((theNode.status)!'') == 0>	
					<input id="inputStatus" type="hidden" value="${theNode.status}">	
						<img src="imgs/icon/icon_2d_0.png" alt="unavailable" />					
					<#elseif ((theNode.status)!'') == 2>
					<input id="inputStatus" type="hidden" value="${theNode.status}">	
						<img src="imgs/icon/icon_2d_2.png" alt="disable" />							
					<#else>
					<input id="inputStatus" type="hidden" value="${theNode.status}">	
						<img src="imgs/icon/icon_2d_0.png" />
					</#if>
				</td>
				<td class="center state">			
					<#if ((theNode.state)!'') == 1>
					<input id="inputState" type="hidden" value="${theNode.state}">
						<!--<img class="imgOn" src="imgs/icon/icon_2d_conn.png" alt="${LANGCODEMAP["MSG_VSF5_TABLEACTION"]!}" /><span> Enabled</span>-->
						<#if ((theNode.status)!'') == 2>
							<span><font color="red">Enabled</font></span>
						<#else>
							<span>Enabled</span>
						</#if>
					<#elseif ((theNode.state)!'') == 0>
					<input id="inputState"  type="hidden" value="${theNode.state}">
						<!--<img class="imgOn" src="imgs/icon/icon_2d_disconn.png"  alt="${LANGCODEMAP["MSG_VSF5_TABLEACTION"]!}" /><span> Disabled</span>-->
						<span>Disabled</span>
					<#elseif ((theNode.state)!'') == 2>
					<input id="inputState"  type="hidden" value="${theNode.state}">
						<!--<img class="imgOn" src="imgs/icon/icon_2d_disabled.png" alt="${LANGCODEMAP["MSG_VSF5_TABLEACTION"]!}" /><span> Forced Offline</span>-->
						<span> Forced Offline</span>
					<#else>
					<input id="inputState"  type="hidden" value="${theNode.state}">
						<!--<img class="imgOn" src="imgs/icon/icon_2d_disconn.png" alt="${LANGCODEMAP["MSG_VSF5_TABLEACTION"]!}" /><span> Disabled</span>-->
						<span> Disabled</span>
					</#if>
					<!--<img class="imgOff none" src="imgs/icon/icon_2d_disconn.png"  alt="${LANGCODEMAP["MSG_VSF5_TABLEACTION"]!}" /><span> Disabled</span>-->
				</td>
				<td class="left ipAddress">${theNode.ipAddress!""}</td>
				<td class="name">
				    <span class="adcType none">${(theNode.adcType!"")}</span>
				    ${theNode.name!""}
				</td>
				<td class="center session"><span class="alteonID none">${(theNode.alteonID!"")}</span>${(theNode.sessionUnit!"")}</td>
				<td class="center ratio none">${theNode.ratio!""}</td>	
				<td class="center vserverAllowed">
			        <div class="Vsadmin_line VSadmin_sum">
			        	<span class="allowsize">${(theNode.vserverAllowed)?size!""}</span>/  
			            <span class="txt_red1 notallowsize">${(theNode.vserverNotAllowed)?size!""}</span>            
			        </div>
			        <div class="Vsadmin_line VSadmin_view">        
			            <span class="VSadmin_view css_textCursor" id="VSadmin_view" isdetail="0" title="${LANGCODEMAP["MSG_NODE_GROUP_DETAIL_VIEW"]!}">
			            	<#if (theNode.vserverAllowed)?size == 0>
			            		<img class="imgChange" id="imgChange" src="/imgs/icon/arrows_dis.png">
			            	<#else>
			            		<img class="imgChange" id="imgChange" src="/imgs/icon/arrows_down.png">
			            	</#if>
			            </span>
			        </div>	        
			        <div class="VSadmin_list" style="display:none;">
			        	<#if ((theNode.vserverAllowed)?size >0)>
			        	<pre>${theNode.vsAllowList!""}</pre>
			        	</#if>	        	
			        	<#if ((theNode.vserverNotAllowed)?size >0)>
				        <span class="txt_red1">
				        	<pre>${theNode.vsNotAllowList!""}</pre>
				        </span>        
				        </#if>		      
			        </div>
			    </td> 
			    <td class="adcName none">
			    	<span class="adcIndex none">${(theNode.adcIndex!"0")}</span>
			    		<#if (theNode.adcType) == 1>
							<img src="imgs/icon/adc/icon_f5_s.png" alt="f5" />	
						<#elseif (theNode.adcType) == 2>
							<img src="imgs/icon/adc/icon_alteon_s.png" alt="alteon" />
						<#elseif (theNode.adcType) == 3>
							<img src="imgs/icon/adc/icon_piolink_s.png" alt="PiolinkUnknown" />	
						<#elseif (theNode.adcType) == 4>
							<img src="imgs/icon/adc/icon_piolink_s.png" alt="PiolinkUnknown" />	
						<#else>
							Unknown
						</#if>	
			    	${(theNode.adcName!"")}
			    </td>
			</tr>		
			</#list>
				
		</tbody>
	<!--</table>-->