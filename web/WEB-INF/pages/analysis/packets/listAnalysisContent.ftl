<#setting number_format="0.####">
<#if langCode??>
	<#if "ko_KR" == langCode><#assign img_lang = ""></#if>
	<#if "en_US" == langCode><#assign img_lang = "_eng"></#if>
</#if>
<div class="contents_area">
	<div>			 
		<img src="imgs/title${img_lang!""}/h3_packet_collectionList.gif" class="title_h3"/>
	</div>
	<!-- 1 --> 
	<div class="control_Board">
		<span class="calendar">			
		<input type="text" class="inputText_calendar"  name="reservation" id="reservationtime"  value="" readonly="">
		</span>	
		<span class="inputTextposition">
		 <input class="searchTxt inputText_search" name="searchKey" type="text" value="${searchKey!}"/>
 		</span>
		<span class="btn imgOn">
			<a href="#" class="searchLnk">
				<img class="imageChange" src="imgs/meun/btn_search_1.png" alt="${LANGCODEMAP["MSG_DIAG_ANAL_SEARCH"]!}"/>				
			</a>
		</span>
		<span class="btn imgOff  none">
			<img class="imageChange" src="imgs/meun/btn_search_1_off.png" alt="${LANGCODEMAP["MSG_DIAG_ANAL_SEARCH"]!}"/>
		</span>
		<span class="total01"> 
			${LANGCODEMAP["MSG_APPLIANCE_TOTAL"]!} <span class="txt_bold noticePageCountInfo">00</span>${LANGCODEMAP["MSG_HISTORY_COUNT"]!} <span class="txt_bold noticePageInfo">(2/4 ${LANGCODEMAP["MSG_APPLIANCE_PAGE"]!})</span>
		</span>		
		<div class="control_positionR">
			<#if accountRole != 'readOnly'>
			<input type="button" class="imgOn pktAdd Btn_white" value="${LANGCODEMAP["MSG_DIAG_ANAL_ADD"]!}"/>  
			<input type="button" class="imgOff none Btn_white" disabled="disabled" value="${LANGCODEMAP["MSG_DIAG_ANAL_ADD"]!}"/> 
	        </#if>
			<input type="button" class="refeshImgOn refreshLnk Btn_white" value="${LANGCODEMAP["MSG_DIAG_ANAL_REFRESH"]!}"/> 
			<input type="button" class="refeshImgOff none Btn_white" disabled="disabled" value="${LANGCODEMAP["MSG_DIAG_ANAL_REFRESH"]!}"/> 			 	        
		</div>
	</div>
	
	<br class="clearfix" />
	<table class="Board pktDumpTable" cellpadding="0" cellspacing="0" style="table-layout:fixed;">
		<#include "analysisTableInListContent.ftl"/>				
	</table>
	<div class="dataNotExistMsg none">${LANGCODEMAP["MSG_DATA_NOT_EXIST"]!}</div>
	<div class="nulldataMsg none">데이터를 표시할 수 없습니다.</div>
	<div class="searchNotMsg none">${LANGCODEMAP["MSG_SEARCH_NOT_RESULT"]!}</div>
	<table class="Board_97 disabledChk" border="0">
		<colgroup>							                            
			<col width="5%"/>
			<col width="95%"/>	
		</colgroup>
		<tr height="5px">
			<td colspan="2"></td>
		</tr>
		<tr>
			<td class="center">
					<#if accountRole! == 'system' || accountRole! == 'config'>
					<input type="button" class="imgOn delPktDumpLnk Btn_white_small" value="${LANGCODEMAP["MSG_DIAG_ANAL_DELETE"]!}"/> 					
					</#if>
					<input type="button" class="imgOff none Btn_white_small" disabled="disabled" value="${LANGCODEMAP["MSG_DIAG_ANAL_DELETE"]!}"/>	
 			</td>
			<td></td>
		</tr>
	</table>
  	<div class="Board_97 disabledChk">  	
		<p class="pageNavigator"></p>
		<div id="select1" class="pageRowCountCbx"></div>
	</div>	
</div>


<!-- 패킷분석 팝업  시작 -->
<div id="pktAddPop" class="pop_type_wrap">
	<h2>${LANGCODEMAP["MSG_DIAG_ANAL_PACKET_COLLECT"]!} <!--  <span class="txt_alertTime_margin"><img src="imgs/icon/iocn_alert_time.png" /><span class="txt_alertTime"></span></span> --></h2>
	<div class="pop2_contents">		
		<div class="description condition">
			<table class="Board_100" cellpadding="0" cellspacing="0">
				<colgroup>
					<col width="20%" />
					<col width="25%" />
					<col width="39%" />
					<col width="16%">
				</colgroup>
				<tr class="StartLine">
					<td colspan="4"></td>
				</tr>
				<tr>
					<th class="Lth2">
						<li>ADC</li>
					</th>
					<td colspan="2" class="Lth0">
						<#if adcObject.category == 0>
							<span class="txt_bold">${LANGCODEMAP["MSG_DIAG_ANAL_ALL_ADC"]!}</span>
						<#elseif adcObject.category == 1>
							<span class="txt_bold"><img src="/imgs/layout/icon_folder_off.gif"/>&nbsp;${adcObject.name!""}</span>
						<#elseif adcObject.category == 2>
							<#if adc.type == 'Alteon'>
								<img src="/imgs/icon/adc/icon_alteon_s.png"/>
							<#elseif adc.type == 'F5'>
								<img src="/imgs/icon/adc/icon_f5_s.png"/>
							<#elseif adc.type == 'PAS'>
								<img src="/imgs/icon/adc/icon_piolink_s.png"/>
							<#elseif adc.type == 'PASK'>
								<img src="/imgs/icon/adc/icon_piolink_s.png"/>
							<#else>
								<img src="/imgs/icon/adc/icon_alteon_s.png"/>
							</#if>
							&nbsp;<span class="txt_bold">${adc.name!""}</span>
						<#else>
						</#if>
					</td>
					<td rowspan="11" class="Lth3">
						<a href="#" class="startPktDump" title="${LANGCODEMAP["MSG_DIAG_ANAL_START"]!}"> 
							<img src="imgs/diagnosis${img_lang!""}/btn_packet_start.png" alt="${LANGCODEMAP["MSG_DIAG_ANAL_START"]!}" />
						</a>					
					</td>
				</tr>
				<tr class="DivideLine">
					<td colspan="3"></td>
				</tr>
				<tr>
					<th class="Lth2">
						<li>${LANGCODEMAP["MSG_DIAG_ANAL_COLLECT_TARGET"]!}</li>
					</th>
					<td colspan="2" class="Lth0 pktDumpPortData">
						<span>
							<select name="targetSel" id="targetSel" class="inputSelect width134 targetSelect">
								<option value="portVal">${LANGCODEMAP["MSG_DIAG_ANAL_SELECT_PORT"]!}</option>
								<#if adcObject.category == 2>
									<#if adc.type == "F5">
										<option value="vlanVal">${LANGCODEMAP["MSG_DIAG_ANAL_SELECT_VLAN"]!}</option>
									<#else>
									</#if>
								</#if>
							</select>
						</span>
						<span class="interfaceName">
							<select name="interfaceName" id="interfaceName" class="inputSelect width134">
								<option value="">${LANGCODEMAP["MSG_DIAG_ANAL_SELECT_ALL"]!}</option>
							<#if portInterfaceNameList??>
							<#list portInterfaceNameList as thePortInterfaceName>
								<option value="${thePortInterfaceName.portName!''}">${thePortInterfaceName.portName!''}</option>
							</#list>
							</#if>
							</select>
						</span>
						<span class="vlanName none">
							<select name="vlanName" id="vlanName" class="inputSelect width134">
							<#if vlanInterfaceNameList??>
							<#list vlanInterfaceNameList as theVlanNameList>
								<option value="${theVlanNameList.name!''}">${theVlanNameList.name!''}</option>
							</#list>
							</#if>
							</select>							
						</span>					
					</td>
				</tr>
				<tr class="DivideLine">
					<td colspan="3"></td>
				</tr>
				<tr>
					<th class="Lth1">
						<li>${LANGCODEMAP["MSG_DIAG_ANAL_FILTER"]!}</li>
					</th>
					<td colspan="2" class="Lth0 filterAdd">
						<div>		
							<select name="filterFirst" id="filter1" class="width134 defaultSelect" >
								<option value="srcIpVal 0">${LANGCODEMAP["MSG_CONSTANT_SIP"]!}</option>								
								<option value="dstIpVal 1">${LANGCODEMAP["MSG_CONSTANT_DIP"]!}</option>
								<option value="srcPortVal 2">${LANGCODEMAP["MSG_CONSTANT_SPORT"]!}</option>
								<option value="dstPortVal 3">${LANGCODEMAP["MSG_CONSTANT_DPORT"]!}</option>
								<option value="protocolVal 4">${LANGCODEMAP["MSG_CONSTANT_PROTOCOL"]!}</option>
								<option value="hostVal 5">${LANGCODEMAP["MSG_CONSTANT_HOST"]!}</option>
								<option value="portVal 6">${LANGCODEMAP["MSG_CONSTANT_PORT"]!}</option>
							</select>
							<span class="defaultArea">														
								<input type="text" name="srcIp" id="filterInput1" class="inputText width129 margin_t2b2" /> 
							</span>		
							<span title="${LANGCODEMAP["MSG_DIAG_ANAL_SEARCH_KEYWORD_ADD"]!}" class="addFilter">
								<img src="/imgs/btn${img_lang!""}/btn_addfilter.gif" />
							</span>							
						</div>
						<div class="addSelectFilter" id="addSelectFilter"> </div>		
					</td>
				</tr>
				<tr class="DivideLine">
					<td colspan="3"></td>
				</tr>
				<tr>
					<th class="Lth2">
						<li>${LANGCODEMAP["MSG_DIAG_ANAL_FILE_NAME"]!}</li>
					</th>
					<td colspan="2" class="Lth0">
						<input type="text" name="fileName" id="fileName" class="inputText width266" value="${fileName!}" />
						<span class="example">${LANGCODEMAP["MSG_DIAG_ANAL_FILE_NAME_EX"]!}</span>
					</td>
				</tr>
				<tr class="DivideLine">
					<td colspan="3"></td>
				</tr>
				<tr>
					<th class="Lth1">
						<li>${LANGCODEMAP["MSG_DIAG_ANAL_OPTION"]!}</li>
					</th>
					<td class="Lth0-1">
						<img src="/imgs/bullet_02-1.gif" />
						<input name="maxPktChk" type="checkbox" class="maxPktChk" checked="checked"/> ${LANGCODEMAP["MSG_DIAG_ANAL_PACKET_COUNT_LIMIT"]!}
					</td>
					<td class="Lth0-12">
						<input type="text" name="optionMaxPkt" id="optionMaxPkt" class="inputText width50" value="${optionMaxPkt!}" /> ${LANGCODEMAP["MSG_DIAG_ANAL_COUNT"]!}
						<#if adcObject.category == 2>
							<#if adc.type == 'F5'>
								<span class="example">${LANGCODEMAP["MSG_DIAG_ANAL_MAX_PACKET_COUNT_F5"]!}</span>
							<#else>
								<span class="example">${LANGCODEMAP["MSG_DIAG_ANAL_MAX_PACKET_COUNT_ALTEON"]!}</span>
							</#if>						
						<#else>
							<span class="example">${LANGCODEMAP["MSG_DIAG_ANAL_MAX_PACKET_COUNT_ALTEON"]!}</span>
						</#if>
					</td>
				</tr>
				<tr>
					<th class="Lth2"></th>
					<td class="Lth0-1">
						<img src="/imgs/bullet_02-1.gif" />
						<input name="maxTimeChk" type="checkbox" class="maxTimeChk" /> ${LANGCODEMAP["MSG_DIAG_ANAL_TIME_LIMIT"]!}
					</td>
					<td class="Lth0-12">
						<input type="text" name="optionMaxTime" id="optionMaxTime" class="inputText width50" disabled="disabled" value="${optionMaxTime!}" />  ${LANGCODEMAP["MSG_DIAG_ANAL_SEC"]!} 
						<span class="example">${LANGCODEMAP["MSG_DIAG_ANAL_MAX_SEC"]!}</span>
					</td>
				</tr>
				<tr class="pktSize none">
					<th class="Lth2"></th>
					<td class="Lth0-1">
						<img src="/imgs/bullet_02-1.gif" />
						<input name="pktSizeChk" type="checkbox" class="pktSizeChk" /> ${LANGCODEMAP["MSG_DIAG_ANAL_VOLUME_LIMIT"]!}
					</td>
					<td class="Lth0-12">
						<input type="text" name="optionMaxSize" id="optionMaxSize" class="inputText width50" value="${optionMaxSize!}" disabled /> Kbytes 
						<span class="example">${LANGCODEMAP["MSG_DIAG_ANAL_MAX_VOLUME"]!}</span>
					</td>
				</tr>
				<tr class="EndLine2">
					<td colspan="4"></td>
				</tr>
			</table>
		</div>
		
		<div class="description progress none">
			<!--		
			<span class="txtblue">
				<#if adcObject.category == 0>
					<span class="txt_bold">전체 ADC</span>
				<#elseif adcObject.category == 1>
					<span class="txt_bold"><img src="/imgs/layout/icon_folder_off.gif"/>&nbsp;${adcObject.name!""}</span>
				<#elseif adcObject.category == 2>
					<#if adc.type == 'Alteon'>
						<img src="/imgs/icon/adc/icon_alteon_s.png"/>
					<#elseif adc.type == 'F5'>
						<img src="/imgs/icon/adc/icon_f5_s.png"/>
					<#elseif adc.type == 'PAS'>
						<img src="/imgs/icon/adc/icon_piolink_s.png"/>
					<#elseif adc.type == 'PASK'>
						<img src="/imgs/icon/adc/icon_piolink_s.png"/>
					<#else>
						<img src="/imgs/icon/adc/icon_alteon_s.png"/>
					</#if>
					&nbsp;<span class="txt_bold">${adc.name!""}</span>
				<#else>
				</#if>
				 패킷수집 중 입니다
			</span>		
			<span class="button blue ">			
			<button type="button" class="stopPktdump"">취소 </button>
			</span>
			-->		
			<div class="pktDumpStatusTableData pktDmpTable"></div>
			<div class="pktDumpStatusCPUData"></div>
			<!--	
			<div class="position_cT10">
			<span class="button white ">
				<button class="cancelCloseLnk" type="button">닫기</button>
			</span>			
			</div>	
			-->		
		</div>			
		<div class="position_cT10">
			<input type="button" class="cancelPkt none cancelPktdump Btn_white" value="${LANGCODEMAP["MSG_DIAG_ANAL_CANCEL"]!}"/>&nbsp;
			<input type="button" class="stopPkt none stopPktdump Btn_blue" value="${LANGCODEMAP["MSG_DIAG_ANAL_STOP"]!}"/>&nbsp;
			<input type="button" class="closePkt none closeLnk Btn_white" value="${LANGCODEMAP["MSG_DIAG_ANAL_CLOSE"]!}"/> &nbsp; 
			<input type="button" class="closeMainPkt closeMainLnk Btn_white" value="${LANGCODEMAP["MSG_DIAG_ANAL_CLOSE"]!}"/>  
		</div>
	</div>
</div>
<!-- 패킷분석 팝업  끝 -->


