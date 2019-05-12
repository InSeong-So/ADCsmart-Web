<#setting number_format="0.####">
<form id="respInfoAddFrm" method="post">
	<div style="display:none;">
		<input name="respGroupInfo.index" type="text" value="${(respGroupInfo.index)!''}"/>
	</div>
	<table class="Board_100" cellpadding="0" cellspacing="0">
		<colgroup>
			<col width="140px"/>
			<col >
		</colgroup>			
		<tr class="StartLine">
			<td colspan="2" ></td>
		</tr>			
		<tr>
			<th class="Lth2">
				<li>${LANGCODEMAP["MSG_SYSTOOL_RESPONSE_NAME"]!}</li>
			</th>
			<td class="Lth0">
				<input name="respGroupInfo.name" type="text" id="respName" class="inputText width250" value="${(respGroupInfo.name)!}"/> <span class="txt_gray2"> ${LANGCODEMAP["MSG_SYSTOOL_RESPONSE_NAMEVALIDATE"]!}</span>
				<!--<input name="respGroupInfo.index" type="text" class="respIndex inputText width250" value="${(respGroupInfo.index)!}"/>-->
			</td>
		</tr>
		<tr class="DivideLine">
			<td colspan="2"></td>
		</tr>
		<tr>
			<th class="Lth2">
				<li>Member</li>
			</th>
			<td class="Lth0">
		<div class="width_97">
		<#if (respGroupInfo)??>
			<span class="usrselected_th">Member (<span class="respInfoCount txt_blue">${(respGroupInfo.respInfo)?size}</span>)</span>
		<#else>
			<span class="usrselected_th">Member (<span class="respInfoCount txt_blue">1</span>)</span>
		</#if>
			<span class="usrselected_txt"><input type="button" class="imgOn Btn_white_small" id="addMember" value="${LANGCODEMAP["MSG_SYSTOOL_RESPONSE_ADD"]!}"></span>
			<!--<span class="usrselected_txt"><input type="button" class="imgOff none Btn_white_small" disabled="disabled" id="addMember" value=" 추가 "></span>-->				
		</div>	
				<table class="width_97Border table-content" id=""> 				
					<colgroup>
					<col width="30px" />
					<col width="112px" />
					<col width="51px" />
					<col width="73px" />
					<col width="290px" />
					<col width="auto" />				
					</colgroup>
					<thead>
					<tr id="header">
		                <th class="align_center">
		                	<input type="checkbox" class="allMembersChk" name="checkbox2" id="checkbox2" />
		                </th>
						<th class="align_center">IP</th>
						<th class="align_center">${LANGCODEMAP["MSG_SYSTOOL_RESPONSE_PORT"]!}</th>
						<th class="align_center">${LANGCODEMAP["MSG_SYSTOOL_RESPONSE_CHECK"]!}</th>	
						<th class="align_center">Path</th>
						<th class="align_center">${LANGCODEMAP["MSG_SYSTOOL_RESPONSE_DESC"]!}</th>											
					</tr>
					</thead>
				</table>
				<div class="table-wapper97height157">
				<table class="table_type11 table-content respList" id="respTable"> 				
					<colgroup>
					<col width="30px" />
					<col width="112px" />
					<col width="51px" />
					<col width="73px" />
					<col width="290px" />
					<col width="auto" />	
					</colgroup>				
					<tbody class="respTbd">
					
					<#list respGroupInfo.respInfo as theRespInfo>
					<tr class="respInfoTr" data-id="0">
						<td class="align_center">
							<input class="member" name="id" type="checkbox" />
							<input class="inputText_table width100" type="hidden" name="respGroupInfo.respInfo[${theRespInfo_index}].index" value="${theRespInfo.index!''}">
						</td>	
						<td class="align_center">							
							<input class="respIp inputText_table width100" type="text" name="respGroupInfo.respInfo[${theRespInfo_index}].ipaddress" value="${theRespInfo.ipaddress!''}" disabled="disabled">						
						</td>
						<td class="align_center">							
							<input class="respPort inputText_table width40" type="text" name="respGroupInfo.respInfo[${theRespInfo_index}].port" value="${theRespInfo.port!''}" disabled="disabled">							
						</td>
						<td class="align_center">
							<select id="respType" class="respType">
				    		<#if (theRespInfo.type == 1)>
								<option value="1" selected="selected">TCP</option>
				    			<option value="2">HTTP</option>			
				    		<#elseif (theRespInfo.type == 2)>
								<option value="1">TCP</option>
				    			<option value="2" selected="selected">HTTP</option>
				    		<#else>
								<option value="1">TCP</option>
				    			<option value="2" selected="selected">HTTP</option>
				    		</#if>										
				   			</select>
				   			<input class="respType inputText_table width100" type="hidden" id="respTypeVal" name="respGroupInfo.respInfo[${theRespInfo_index}].type" value="${theRespInfo.type!''}">
						</td>	
						<td class="align_center">							
							<input class="respPath inputText_table width280" type="text" name="respGroupInfo.respInfo[${theRespInfo_index}].path" value="${theRespInfo.path!''}" disabled="disabled">							
						</td>
						<td class="align_center">							
							<input class="inputText_table width135"  type="text" name="respGroupInfo.respInfo[${theRespInfo_index}].comment" value="${theRespInfo.comment!''}">							
						</td>																						
					</tr>	
					</#list>
					
					</tbody>
			   </table>
			   </div>
			   <div class="width_97 tableControl_bottom">
					<a href="#" class="moveTop"><img class="movebtn" id="moveTop" src="/imgs/btn/btn_moveTop.png" alt=""></a>
					<a href="#" class="moveUp"><img class="movebtn" id="moveUp" src="/imgs/btn/btn_moveUp.png" alt=""></a>
					<a href="#" class="moveDown"><img class="movebtn" id="moveDown" src="/imgs/btn/btn_moveDown.png" alt=""></a>
					<a href="#" class="moveBottom"><img class="movebtn" id="moveBottom" src="/imgs/btn/btn_moveBottom.png" alt=""></a>
					<a href="#" class="moveDel"><img class="movebtn" id="moveDel" src="/imgs/btn/btn_moveDel.png" alt=""></a>								
			  </div>			   				
			</td>
		</tr>
		<tr class="EndLine2">
			<td colspan="2"></td>
		</tr>			
	</table>
</form>