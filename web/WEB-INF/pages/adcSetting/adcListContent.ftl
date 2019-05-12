<#if orderType??>
	<#if 33 == orderType></#if><!-- 상태 -->
	<#if 34 == orderType></#if><!-- ADC이름 -->
	<#if 35 == orderType></#if><!-- 종류 -->
	<#if 36 == orderType></#if><!-- IP -->
	<#if 37 == orderType></#if><!-- 최근업데이트 -->
	<#if 38 == orderType></#if><!-- 버전 -->
</#if>
<#if orderDir??>
	<#if 1 == orderDir></#if><!-- Asc -->
	<#if 2 == orderDir></#if><!-- Desc -->
</#if>	
<#if langCode??>
  <#if "ko_KR" == langCode><#assign img_lang = ""></#if>
  <#if "en_US" == langCode><#assign img_lang = "_eng"></#if>
</#if>
<!-- <div class="content_wrap"> -->
<div class="contents_area">
	<div>
		<img src="imgs/title${img_lang!""}/h3_adcList.gif" class="title_h3" />				
	</div>
	<div class="control_Board">
		<p class="cont_sch">
		<span class="inputTextposition1">
			<input name="textfield3" type="text" class="searchTxt inputText_search" id="textfield3" value="${searchKey!}" />
		</span>
		<span class="btn"> 
			<a class="searchLnk" href="#">
				<img src="imgs/meun/btn_search_1.png" alt="${LANGCODEMAP["MSG_ADCSETTING_SEARCH"]!}" />
			</a>
		</span>
		<#if accountRole == 'system'>
		<span class="control_positionR">
			<input type="button" id="addAdcBtn" class="btn_new Btn_white" value="${LANGCODEMAP["MSG_ADCSETTING_ADD"]!}"/>   
		</span>
		</#if>
		</p>
	</div>
	<br class="clearfix" />
	<#list adcGroupInfoMap?keys as key>
	<#assign theAdcGroup = adcGroupInfoMap[key]>
	<ul>
	<div class="title_h4_11">
		<li>${theAdcGroup.name!''}&nbsp;(${theAdcGroup.adcsize})</li>
	</div>
	<!----- Contents List Start ----->
	<table id="adclistTable" class="Board" cellpadding="0" cellspacing="0"  style="table-layout: fixed;">
		<caption>${theAdcGroup.name!''} ${LANGCODEMAP["MSG_ADCSETTING_APPLIANCE_CONDITION"]!}</caption>
		<colgroup>
			<col width="5%" />
			<col width="6%" />
			<col width="15%" />
			<col width="5%" />
			<col width="12%" />
			<col width="17%" />
			<col width="9%" />
			<col width="9%" />			
			<col width="12%" />
			<col width="10%" />
		</colgroup>
		<thead>
			<tr class="StartLine">
				<td colspan="10"></td>
			</tr>
			<tr class="ContentsHeadLine">
				<th>
					<#if accountRole == 'readOnly' || accountRole == 'vsAdmin' || accountRole == 'rsAdmin'>
						<input class="allAdcsChk" type="checkbox" disabled="disabled"/>
					<#else>
						<input class="allAdcsChk" type="checkbox" />
					</#if>
				</th>
				<th>${LANGCODEMAP["MSG_ADCSETTING_STATUS"]!}</th>
<!-- 				<th> -->
<!-- 					<span class="css_textCursor"> -->
<!-- 						<#if orderDir == 2 && orderType == 33>		 -->
<!-- 							<a class="orderDir_Desc">${LANGCODEMAP["MSG_ADCSETTING_STATUS"]!} -->
<!-- 								<img src="imgs/Desc.gif"/> -->
<!-- 								<span class="none orderType">33</span> -->
<!-- 								<span class="none orderDir">1</span>					 -->
<!-- 							</a>						 -->
<!-- 						<#elseif orderDir == 1 && orderType == 33>	 -->
<!-- 							<a class="orderDir_Asc">${LANGCODEMAP["MSG_ADCSETTING_STATUS"]!} -->
<!-- 								<img src="imgs/Asc.gif"/> -->
<!-- 								<span class="none orderType">33</span> -->
<!-- 								<span class="none orderDir">2</span>		 -->
<!-- 							</a> -->
<!-- 						<#else> -->
<!-- 							<a class="orderDir_None">${LANGCODEMAP["MSG_ADCSETTING_STATUS"]!} -->
<!-- 								<img src="imgs/none.gif"/> -->
<!-- 								<span class="none orderType">33</span> -->
<!-- 								<span class="none orderDir">2</span>		 -->
<!-- 							</a> -->
<!-- 						</#if> -->
<!-- 					</span>					 -->
<!-- 				</th> -->
				<th>
					<span class="css_textCursor">
						<#if orderDir == 2 && orderType == 34>		
							<a class="orderDir_Desc">${LANGCODEMAP["MSG_ADCSETTING_ADC_NAME"]!}
								<img src="imgs/Desc.gif"/>
								<span class="none orderType">34</span>
								<span class="none orderDir">1</span>					
							</a>						
						<#elseif orderDir == 1 && orderType == 34>	
							<a class="orderDir_Asc">${LANGCODEMAP["MSG_ADCSETTING_ADC_NAME"]!}
								<img src="imgs/Asc.gif"/>
								<span class="none orderType">34</span>
								<span class="none orderDir">2</span>		
							</a>
						<#else>
							<a class="orderDir_None">${LANGCODEMAP["MSG_ADCSETTING_ADC_NAME"]!}
								<img src="imgs/none.gif"/>
								<span class="none orderType">34</span>
								<span class="none orderDir">2</span>		
							</a>
						</#if>
					</span>					
				</th>
				<th>
					<span class="css_textCursor">
						<#if orderDir == 2 && orderType == 35>		
							<a class="orderDir_Desc">${LANGCODEMAP["MSG_ADCSETTING_TYPE"]!}
								<img src="imgs/Desc.gif"/>
								<span class="none orderType">35</span>
								<span class="none orderDir">1</span>					
							</a>						
						<#elseif orderDir == 1 && orderType == 35>	
							<a class="orderDir_Asc">${LANGCODEMAP["MSG_ADCSETTING_TYPE"]!}
								<img src="imgs/Asc.gif"/>
								<span class="none orderType">35</span>
								<span class="none orderDir">2</span>		
							</a>
						<#else>
							<a class="orderDir_None">${LANGCODEMAP["MSG_ADCSETTING_TYPE"]!}
								<img src="imgs/none.gif"/>
								<span class="none orderType">35</span>
								<span class="none orderDir">2</span>		
							</a>
						</#if>
					</span>					
				</th>
				<th>
					<span class="css_textCursor">
						<#if orderDir == 2 && orderType == 36>		
							<a class="orderDir_Desc">IP
								<img src="imgs/Desc.gif"/>
								<span class="none orderType">36</span>
								<span class="none orderDir">1</span>					
							</a>						
						<#elseif orderDir == 1 && orderType == 36>	
							<a class="orderDir_Asc">IP
								<img src="imgs/Asc.gif"/>
								<span class="none orderType">36</span>
								<span class="none orderDir">2</span>		
							</a>
						<#else>
							<a class="orderDir_None">IP
								<img src="imgs/none.gif"/>
								<span class="none orderType">36</span>
								<span class="none orderDir">2</span>		
							</a>
						</#if>
					</span>					
				</th>
				<th>
					<span class="css_textCursor">
						<#if orderDir == 2 && orderType == 37>		
							<a class="orderDir_Desc">${LANGCODEMAP["MSG_ADCSETTING_LAST_UPDATE"]!}
								<img src="imgs/Desc.gif"/>
								<span class="none orderType">37</span>
								<span class="none orderDir">1</span>					
							</a>						
						<#elseif orderDir == 1 && orderType == 37>	
							<a class="orderDir_Asc">${LANGCODEMAP["MSG_ADCSETTING_LAST_UPDATE"]!}
								<img src="imgs/Asc.gif"/>
								<span class="none orderType">37</span>
								<span class="none orderDir">2</span>		
							</a>
						<#else>
							<a class="orderDir_None">${LANGCODEMAP["MSG_ADCSETTING_LAST_UPDATE"]!}
								<img src="imgs/none.gif"/>
								<span class="none orderType">37</span>
								<span class="none orderDir">2</span>		
							</a>
						</#if>
					</span>					
				</th>
				<th>${LANGCODEMAP["MSG_ADCSETTING_VERSION"]!}</th>
<!-- 				<th> -->
<!-- 					<span class="css_textCursor"> -->
<!-- 						<#if orderDir == 2 && orderType == 38>		 -->
<!-- 							<a class="orderDir_Desc">${LANGCODEMAP["MSG_ADCSETTING_VERSION"]!} -->
<!-- 								<img src="imgs/Desc.gif"/> -->
<!-- 								<span class="none orderType">38</span> -->
<!-- 								<span class="none orderDir">1</span>					 -->
<!-- 							</a>						 -->
<!-- 						<#elseif orderDir == 1 && orderType == 38>	 -->
<!-- 							<a class="orderDir_Asc">${LANGCODEMAP["MSG_ADCSETTING_VERSION"]!} -->
<!-- 								<img src="imgs/Asc.gif"/> -->
<!-- 								<span class="none orderType">38</span> -->
<!-- 								<span class="none orderDir">2</span>		 -->
<!-- 							</a> -->
<!-- 						<#else> -->
<!-- 							<a class="orderDir_None">${LANGCODEMAP["MSG_ADCSETTING_VERSION"]!} -->
<!-- 								<img src="imgs/none.gif"/> -->
<!-- 								<span class="none orderType">38</span> -->
<!-- 								<span class="none orderDir">2</span>		 -->
<!-- 							</a> -->
<!-- 						</#if> -->
<!-- 					</span>					 -->
<!-- 				</th> -->
				<th>FLB</th>
				<th>${LANGCODEMAP["MSG_ADCSETTING_EXPLAIN"]!}</th>
				<th>Action</th>
			</tr>
			<tr class="StartLine1">
				<td colspan="10"></td>
			</tr>			
		</thead>	
		<tbody>
			<#list theAdcGroup.adcs![] as theAdc>
				<tr class="ContentsLine1 adcSetList">
					<#if accountRole == 'readOnly' || accountRole == 'vsAdmin' || accountRole == 'rsAdmin'>
						<td class="align_center"><input class="adcChk"	type="checkbox" value="${theAdc.index!''}" disabled="disabled"/></td>
					<#else>
						<td class="align_center"><input class="adcChk"	type="checkbox" value="${theAdc.index!''}"/></td>
					</#if>
					<td class="align_center"><img src="${((theAdc.status!'') == 'available')?string('imgs/icon/icon_1d_conn.png', 'imgs/icon/icon_1d_disconn.png')}"	/></td>
					<td class="align_left_10 textOver" title="${theAdc.name!''}"><a class="adcIndexLnk" href="#" target="_self"><span style="display:none;">${theAdc.index!''}</span>${theAdc.name!''}</a></td>
					<td class="align_center">
						<input type="hidden" class="adcType" value="${theAdc.type}" />
						<input type="hidden" class="opMode" value="${theAdc.opMode}" />
					<#if theAdc.type == 'F5'>												
						<img src="imgs/icon/adc/icon_f5_s.png" />
					<#elseif theAdc.type == 'Alteon'>
						<img src="imgs/icon/adc/icon_alteon_s.png" />
					<#else>
						<img src="imgs/icon/adc/icon_piolink_s.png" />
					</#if>			
					</td>
					<td class="align_left_P10">${theAdc.ip!''}</td>
					<td class="align_center textOver">${(theAdc.lastUpdateTime?string("yyyy-MM-dd HH:mm:ss"))!}</td>
					<td class="align_center">${theAdc.version!}</td>
					<#if theAdc.type == 'Alteon'>
						<#if theAdc.isFlb == 0>
							<td class="align_center">
							    <input type="button" class="flbGroupBtn Btn_white_small" value="${LANGCODEMAP["MSG_ADCSETTING_GROUP_SETTING"]!}"/>  
							</td>
						<#else>
							<td class="align_center">
                                <input type="button" class="flbGroupBtn Btn_black_small" value="${LANGCODEMAP["MSG_ADCSETTING_GROUP_SETTING"]!}"/> 							
							</td>
						</#if>
					<#else>
						<td class="align_center">
							-
						</td>
					</#if>
					<td class="align_left_10 textOver" title="${theAdc.description!''}">${theAdc.description!''}</td>
					<td class="align_center">	
									
					<#if theAdc.type == 'Alteon'>
						<#if accountRole == 'readOnly' || accountRole == 'vsAdmin' || accountRole == 'rsAdmin' 
										|| theAdc.status != 'available' || theAdc.opMode == 1> <!-- 권한이 readOnly, vsAdmin, rsAdmin 이거나 adc 단절, 모니터링모드인 경우 -->
							<span class="position_R1L1">	
								<img src="imgs/btn/btn_settingsave_off.png" alt="${LANGCODEMAP["MSG_ADCSETTING_SETTING_SAVE"]!}" title="${LANGCODEMAP["MSG_ADCSETTING_SETTING_SAVE"]!}"/>
							</span>
						<#else>
							<#if theAdc.lastUpdateTime?exists>							<!-- lasyUpdateTime 있는경우 -->
								<#if theAdc.saveTime?exists>							<!-- lasyUpdateTime, saveTime 모두있는경우 -->
									<#if (theAdc.saveTime < theAdc.lastUpdateTime)>		<!--  saveTime 보다 applyTime 이 나중일 경우  : 비교 -->							
										<a class="saveLnk" href="#">
											<span class="position_R1L1">
												<img src="imgs/btn/btn_settingsave_on.png" alt="${LANGCODEMAP["MSG_ADCSETTING_SETTING_SAVE"]!}" title="${LANGCODEMAP["MSG_ADCSETTING_SETTING_SAVE"]!}"/>
											</span>
										</a>
									<#else>
										<span class="position_R1L1">
											<img src="imgs/btn/btn_settingsave_off.png" alt="${LANGCODEMAP["MSG_ADCSETTING_SETTING_SAVE"]!}" title="${LANGCODEMAP["MSG_ADCSETTING_SETTING_SAVE"]!}"/>									
										</span>
									</#if>	
								<#else>			<!-- applyTime 있고, saveTime 없는경우 : 설정 저장가능.-->
									<a class="saveLnk" href="#">
										<span class="position_R1L1">																			
											<img src="imgs/btn/btn_settingsave_on.png" alt="${LANGCODEMAP["MSG_ADCSETTING_SETTING_SAVE"]!}" title="${LANGCODEMAP["MSG_ADCSETTING_SETTING_SAVE"]!}"/>
										</span>
									</a>
								</#if>
							<#else>							
								<span class="position_R1L1">
									<img src="imgs/btn/btn_settingsave_off.png" alt="${LANGCODEMAP["MSG_ADCSETTING_SETTING_SAVE"]!}" title="${LANGCODEMAP["MSG_ADCSETTING_SETTING_SAVE"]!}"/>
								</span>
							</#if>
						</#if>
					<#elseif theAdc.type == 'F5'>
					</#if>												
						<a class="monitorLnk" href="#">
						<span class="position_R1L1">
							<img src="imgs/btn/btn_monitoring_on.png" alt="${LANGCODEMAP["MSG_ADCSETTING_MONITORING"]!}" title="${LANGCODEMAP["MSG_ADCSETTING_MONITORING"]!}"/>
						</span>
						</a>
						<#if theAdc.status == 'available'>
							<a class="forced_update_Lnk" href="#">
								<span class="position_R1L1">
									<img src="imgs/btn/btn_update_on.png" alt="${LANGCODEMAP["MSG_ADCSETTING_FORCED_UPDATE"]!}" title="${LANGCODEMAP["MSG_ADCSETTING_FORCED_UPDATE"]!}"/>
								</span>
							</a>
						<#else>
							<span class="position_R1L1">
								<img src="imgs/btn/btn_update_off.png" alt="${LANGCODEMAP["MSG_ADCSETTING_FORCED_UPDATE"]!}" title="${LANGCODEMAP["MSG_ADCSETTING_FORCED_UPDATE"]!}"/>
							</span>
						</#if>
					</td>
				</tr>
				<tr class="DivideLine">
					<td colspan="10"></td>
				</tr>
			</#list>
			</tbody>
			<tr class="EndLine">
				<td colspan="10"></td>
			</tr>
			<tr>
				<td colspan="10">
					<div class="searchNotMsg none">${LANGCODEMAP["MSG_SEARCH_NOT_RESULT"]!}</div>
				</td>
			</tr>
			<tr class="ContentsLine3">
				<td class="center">
					<#if accountRole == 'system'>
					    <input type="button" class="delAdcs Btn_white_small" value="${LANGCODEMAP["MSG_ADCSETTING_DELETE"]!}"/>     
					</#if>
				</td>
				<td colspan="8"></td>
			</tr>
		</table>	
	</ul>
	</#list>
</div>
<div id="flbGroupPopup" class="pop_type_wrap">
</div>