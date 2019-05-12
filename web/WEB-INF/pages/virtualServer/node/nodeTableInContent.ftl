<#setting number_format="0.####">
<#if orderType??>
	<#if 33 == orderType></#if><!-- IP -->
	<#if 34 == orderType></#if><!-- 이름 -->
	<#if 35 == orderType></#if><!-- Ratio -->
	<#if 36 == orderType></#if><!-- state -->
	<#if 38 == orderType></#if><!-- status -->
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
		    <col width="50px">
		    <col width="60px">
		    <#if adc.type == 'Alteon'>
		    <col width="60px">
		    </#if>
		    <col width="100px">	
		    <col width="20px">			    
		    <col width="310px">
			<col width="120px">
			<col width="140px">
			<col width="60px">
			<col width="250px">
		</colgroup>
		<thead>
			<tr class="ContentsHeadLine">
				 <th class="bg_row2">
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
				<th class="bg_row2">
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
				<#if adc.type == 'Alteon'>
				<th class="bg_row2">
					<span class="css_textCursor">
					<#if orderType ??>
						<#if orderDir == 2 && orderType == 36>		
							<a class="orderDir_Desc">index
								<img src="imgs/Desc.gif"/>
								<span class="none orderType">36</span>
								<span class="none orderDir">1</span>					
							</a>						
						<#elseif orderDir == 1 && orderType == 36>	
							<a class="orderDir_Asc">index
								<img src="imgs/Asc.gif"/>
								<span class="none orderType">36</span>
								<span class="none orderDir">2</span>		
							</a>
						<#else>
							<a class="orderDir_None">index
								<img src="imgs/none.gif"/>
								<span class="none orderType">36</span>
								<span class="none orderDir">2</span>		
							</a>
						</#if>
					<#else>
						<a class="orderDir_None">index
							<img src="imgs/none.gif"/>
							<span class="none orderType">36</span>
							<span class="none orderDir">2</span>		
						</a>
					</#if>
					</span>				
				</th>
				</#if>
				<th></th>
				<th class="bg_row2">
					<span class="css_textCursor">
					<#if orderGroupType ??>
						<#if orderGroupDir == 2 && orderGroupType == 37>		
							<a class="orderDir_Desc">${LANGCODEMAP["MSG_NODE_GROUP"]!}
								<img src="imgs/Desc.gif"/>
								<span class="none orderGroupType">37</span>
								<span class="none orderGroupDir">1</span>					
							</a>						
						<#elseif orderGroupDir == 1 && orderGroupType == 37>	
							<a class="orderDir_Asc">${LANGCODEMAP["MSG_NODE_GROUP"]!}
								<img src="imgs/Asc.gif"/>
								<span class="none orderGroupType">37</span>
								<span class="none orderGroupDir">2</span>		
							</a>
						<#else>
							<a class="orderDir_None">${LANGCODEMAP["MSG_NODE_GROUP"]!}
								<img src="imgs/none.gif"/>
								<span class="none orderGroupType">37</span>
								<span class="none orderGroupDir">2</span>		
							</a>
						</#if>
					<#else>
						<a class="orderDir_None">${LANGCODEMAP["MSG_NODE_GROUP"]!}
							<img src="imgs/none.gif"/>
							<span class="none orderGroupType">37</span>
							<span class="none orderGroupDir">2</span>		
						</a>
					</#if>
					</span>				
				</th>
				<th class="bg_row2">
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
				<th class="bg_row2">
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
				<th class="bg_row2">
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
			</tr>
		</thead>
		<tbody>
			<!----- Group 만들기 ----->
			<tr class="rsGrpName none">
				<td></td>
				<td></td>
				<#if adc.type == 'Alteon'>
				<td></td>
				</#if>
				<td></td>	
				<td></td>				
				<td>
					<input name="rsGrpNm" type="text" id="rsGrpNm" class="rsGrpNm inputText  width150" value="" spellCheck="false">				
					<span>
						<input type="button" id="rsGrpNmSave" class="rsGrpNmSave Btn_white_small" value="${LANGCODEMAP["MSG_VSALTEON_CONFIRM"]!}">
						<input type="button" id="rsGrpNmCancel" class="rsGrpNmCancel Btn_white_small" value="${LANGCODEMAP["MSG_VSALTEON_CANCEL"]!}">
					</span>				
				</td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
			</tr>
			
			<!----- Group 생성됨  ----->		
			<#list nodeGrpList as theNodeGrp>
			<#if theNodeGrp.available == 1>
			<tr class="Group" data-index="${theNodeGrp.groupIndex}">
				<td class="center"><input class="rsGrpChk" type="checkbox" ></td>
				<td class="center status">-</td>
				<td class="center"><span class="rsGroupState">-</span></td>
				<#if adc.type == 'Alteon'>
				<td class="center alteonId"></td>
				</#if>			
				<td>
					<span class="btnPlusMinus center plus" val="plus" data-parent="${theNodeGrp.groupIndex}"><img class="plusminus" src="/imgs/layout/plus.gif" alt=""/></span>						
				</td>						
				<td class="txt_bold textOver" title="${theNodeGrp.groupName!""}">
					<span style="display:none;">${(theNodeGrp.groupIndex)!''}</span><span class="rsGrpNmOrg">${theNodeGrp.groupName!""}&nbsp;(<span class="rsGroupCount">10</span>)</span>		
					<input name="rsGrpModifyNm" type="text" id="rsGrpNm" class="rsGrpNm groupModify inputText width150 none" value="${theNodeGrp.groupName!""}" spellCheck="false">
					<span class="imgbtn">
						<input type="button" id="rsGrpNmSave" class="rsGrpNmSave groupModify Btn_white_small none" value="${LANGCODEMAP["MSG_VSALTEON_CONFIRM"]!}">
						<input type="button" id="rsGrpNmCancel" class="rsGrpNmCancel groupModify Btn_white_small none" value="${LANGCODEMAP["MSG_VSALTEON_CANCEL"]!}">
						<img class="rsGroupModifyLnk conn none" src="imgs/layout/edit.png" alt="${LANGCODEMAP["MSG_DIAG_SET_CHANGE"]!}">
						<img class="rsGroupDelLnk conn none" src="imgs/layout/del.png" alt="${LANGCODEMAP["MSG_DIAG_SET_DELETE"]!}">
					</span>	
				</td>

				<td></td>
				<td></td>
				<td></td>
				<td></td>
			</tr>
			</#if>
			<#list nodeDetailGroup as theNode>
			<#if (theNode.groupIndex== theNodeGrp.groupIndex) >		
			<tr class="nodeList realServer none" data-parent="${theNodeGrp.groupIndex}">
				<#if accountRole == 'readOnly'>
					<td class="center">
						<input type="checkbox" id="node" value="${theNode.index}" disabled="disabled"/>
					</td>
				<#else>
					<td class="center">
						<input class="nodeChk" id="node" type="checkbox" value="${theNode.index}"/>
					</td>
				</#if>
				<td class="center status">			
					<#if ((theNode.status)!'') == 1>
					<input id="inputStatus" type="hidden" value="${theNode.status}">					
						<img src="imgs/icon/icon_2d_1.png" alt="available" />						
					<#elseif ((theNode.status)!'') == 0>	
					<input id="inputStatus" type="hidden" value="${theNode.status}">	
						<img src="imgs/icon/icon_2d_2.png" alt="unavailable" />					
					<#elseif ((theNode.status)!'') == 2>
					<input id="inputStatus" type="hidden" value="${theNode.status}">	
						<img src="imgs/icon/icon_2d_0.png" alt="disable" />							
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
				<#if adc.type == 'Alteon'>
				<td class="center alteonId">${(theNode.alteonID!"")}</td>
				</#if>
				<td><img src="/imgs/layout/subtree.gif" alt=""/></td>
				<td class="textOver" title="${theNode.groupName!""}">${theNode.groupName!""}</td>
				<td class="left ip">${theNode.ipAddress!""}</td>
				<td class="name">${theNode.name!""}</td>
				<td class="center ratio">${theNode.ratio!""}</td>	
				<td class="center">
			        <div class="Vsadmin_line VSadmin_sum">
			        	<span class="allowsize">${(theNode.vserverAllowed)?size!""}</span>/  
			            <span class="txt_red1 notallowsize">${(theNode.vserverNotAllowed)?size!""}</span>            
			        </div>
			        <div class="Vsadmin_line VSadmin_view">        
			            <span class="VSadmin_view css_textCursor" id="VSadmin_view" isdetail="0" title="${LANGCODEMAP["MSG_NODE_GROUP_DETAIL_VIEW"]!}"><img class="imgChange" id="imgChange" src="/imgs/icon/arrows_down.png"></span>
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
			</tr>	
			</#if>		
			</#list>
			</#list>			
						
			<!----- Group 없음 ----->
			
			<#list nodeDetail as theNode>
			<#if (theNode.type == 0) >	
			<tr class="nodeList">
				<#if accountRole == 'readOnly'>
					<td class="center">
						<input type="checkbox" id="node" value="${theNode.index}" disabled="disabled"/>
					</td>
				<#else>
					<td class="center">
						<input class="nodeChk" id="node" type="checkbox" value="${theNode.index}"/>
					</td>
				</#if>
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
				<#if adc.type == 'Alteon'>
				<td class="center alteonId">${(theNode.alteonID!"")}</td>
				</#if>
				<td></td>
				<td class="session"><span class="alteonID none">${(theNode.alteonID!"")}</span></td>
				<td class="left ip">${theNode.ipAddress!""}</td>
				<td class="name">${theNode.name!""}</td>
				<td class="center ratio">${theNode.ratio!""}</td>	
				<td class="center">
			        <div class="Vsadmin_line VSadmin_sum">
			        	<span class="allowsize">${(theNode.vserverAllowed)?size!""}</span>/  
			            <span class="txt_red1 notallowsize">${(theNode.vserverNotAllowed)?size!""}</span>            
			        </div>
			        <div class="Vsadmin_line VSadmin_view">        
			            <span class="VSadmin_view css_textCursor" id="VSadmin_view" isdetail="0" title="${LANGCODEMAP["MSG_NODE_GROUP_DETAIL_VIEW"]!}"><img class="imgChange" id="imgChange" src="/imgs/icon/arrows_down.png"></span>
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
			</tr>	
			</#if>		
			</#list>
				
		</tbody>
	<!--</table>-->