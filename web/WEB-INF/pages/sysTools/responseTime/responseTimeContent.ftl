<#setting number_format="0.####">
<#if langCode??>
  <#if "ko_KR" == langCode><#assign img_lang = ""></#if>
  <#if "en_US" == langCode><#assign img_lang = "_eng"></#if>
</#if>
<div class="contents_area">
	<div> 
		<img src="imgs/title${img_lang!""}/h3_responseTimeCheck.gif" class="title_h3" />			 
	</div>
	<div class="Board">
		<div class="control_positionR">	
				<input type="text" class="inputText_calendar" id="reservationtime" name="reservation"  value="" readonly="" >
			<span>
		        <input type="button" id="refresh" class="Btn_white" value="${LANGCODEMAP["MSG_PERFCONT_REFRESH"]!}"/>
		    </span>		    												
		</div>
	</div>
	<br class="clearfix">	
	<div class="Board">			
	<div id="SvcPerfChart" style="width: 100%; height: 200px; "></div>			
	 </div>
	 <!----- Contents Page Start ----->
	<div class="control_Board">
		<div class="title_h4_31">
			<li>${LANGCODEMAP["MSG_SYSTOOL_RESPONSE_LIST"]!}</li>
		</div>			
			<div class="control_positionR">
	            <input type="button" id="addResponse" class="export_width Btn_white" value="${LANGCODEMAP["MSG_SYSTOOL_RESPONSE_PERIODADD"]!}"/>		
	        </div>	 

	</div>
	<div class="vs_trafficInfo">
		<table class="Board_out responseInfoList" id="resp_table" cellpadding="0" cellspacing="0" style="table-layout: fixed;">
		</table>
	</div>
	<div class="dataNotExistMsg none">${LANGCODEMAP["MSG_DATA_NOT_EXIST"]!}</div>
	<div class="nulldataMsg none">${LANGCODEMAP['MSG_COMMON_DATA_NULL']!}</div>
	<div class="searchNotMsg none">${LANGCODEMAP["MSG_SEARCH_NOT_RESULT"]!}</div>
	<!----- Contents List End ----->    
	
	<table class="Board_97 disabledChk" border="0">
		<tr height="5">
			<td></td>
			<td></td>
		</tr>
		<tr>		
			<td class="Lth0">
				<input type="button" class="imgOn delRespInfoLnk Btn_white_small" value="${LANGCODEMAP["MSG_SYSTOOL_RESPONSE_DEL"]!}"/>
			</td>
		</tr>
	</table>
</div>

<!-- 그룹 관리 팝업 -->
<div id="addResponseWnd" class="pop_type_wrap">
	<h2>${LANGCODEMAP["MSG_SYSTOOL_RESPONSE_TIMEADD"]!}</h2>
	<div class="pop2_contents">
		<div class="description">
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
							<tr class="respInfoTr" data-id="0">
								<td class="align_center">
									<input name="id" class="member" type="checkbox"/>
								</td>							
								<td class="align_center">							
									<input class="respIp inputText_table width100" type="text" name="respGroupInfo.respInfo[0].ipaddress" value="">
								</td>
								<td class="align_center">
									<input class="respPort inputText_table width40" type="text" name="respGroupInfo.respInfo[0].port" value="">
								</td>
								<td class="align_center">
									<select id="respType" class="respType">
						    			<!--<option value="0" selected="selected">ICMP</option>-->
						    			<option value="1" selected="selected">TCP</option>
						    			<option value="2">HTTP</option>
						    			<!--<option value="3">HTTPS</option>-->										
						   			</select>
						   			<input class="respType inputText_table width100" type="hidden" id="respTypeVal" name="respGroupInfo.respInfo[0].type" value="1">
								</td>	
								<td class="align_center">
									<input class="respPath inputText_table width280" type="text" name="respGroupInfo.respInfo[0].path" value="" disabled="disabled">
								</td>
								<td class="align_center">
									<input class="inputText_table width135"  type="text" name="respGroupInfo.respInfo[0].comment" value="">
								</td>
							</tr>
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
		</div>	
	</div>
	<p class="center mar_btm10">
		<input type="button" class="addResponseInfo Btn_white" value="${LANGCODEMAP["MSG_SYSTOOL_RESPONSE_CONFIRM"]!}"/>   			
		<input type="button" class="onCancel Btn_white" value="${LANGCODEMAP["MSG_SYSTOOL_RESPONSE_CANCEL"]!}"/>
	</p>	
	<p class="close">
		<a class="closeLnk" href="#" title="${LANGCODEMAP["MSG_ADCSETTING_CLOSE"]!}">
			<img src="imgs/popup/btn_clse.gif" alt="${LANGCODEMAP["MSG_ADCSETTING_CLOSE"]!}"/>
		</a>
	</p>
</div>

<div id="modifyResponseWnd" class="pop_type_wrap">	
	<h2>${LANGCODEMAP["MSG_SYSTOOL_RESPONSE_MODIFY"]!}</h2>
	<div class="pop2_contents">
		<div class="description">	
		</div>	
	</div>
	<p class="center mar_btm10">
		<input type="button" class="addResponseInfo Btn_white" value="${LANGCODEMAP["MSG_SYSTOOL_RESPONSE_CONFIRM"]!}"/>   			
		<input type="button" class="onCancel Btn_white" value="${LANGCODEMAP["MSG_SYSTOOL_RESPONSE_CANCEL"]!}"/>
	</p>	
	<p class="close">
		<a class="closeLnk" href="#" title="${LANGCODEMAP["MSG_ADCSETTING_CLOSE"]!}">
			<img src="imgs/popup/btn_clse.gif" alt="${LANGCODEMAP["MSG_ADCSETTING_CLOSE"]!}"/>
		</a>
	</p>
</div>