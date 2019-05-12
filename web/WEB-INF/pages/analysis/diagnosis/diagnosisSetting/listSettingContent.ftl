<#setting number_format="0.####">
<#if langCode??>
  <#if "ko_KR" == langCode><#assign img_lang = ""></#if>
  <#if "en_US" == langCode><#assign img_lang = "_eng"></#if>
</#if>
<div class="contents_area">
	<div>
		<img src="imgs/title${img_lang!""}/h3_diagnosisSetup.gif" class="title_h3" />		
	</div>
	<form id="faultDiagnosisFrm" method="post" name="faultDiagnosisFrm">
	<!-- 1 --> 
	<table class="Board" cellpadding="0" cellspacing="0">
		<colgroup>
			<col width="200px"/>
			<col width="auto">
			<col width="10px"/>
			<col width="11%">			
		</colgroup>			
		<tr class="StartLine">
			<td colspan="4" ></td>
		</tr>			
		<tr>
			<th class="Lth1">			
				<li>${LANGCODEMAP["MSG_DIAG_SET_DIAGNOSISS_TARGET"]!}</li>		
			</th>
			<td class="Lth0">
				<#if adcObject.category == 0>
					<span class="txt_bold">${LANGCODEMAP["MSG_DIAG_SET_ALL_ADC"]!}</span>
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
					&nbsp;<span class="diagnosisTarget txt_bold">${adc.name!""}</span>
				<#else>
				</#if>
			</td>
			
			<!-- 진단속도 			
			<td class="D_speed" rowspan="5" >	
				<p>진단속도</p>
				<select class="inputSelect">
					<option value="">연속</option>
					<option value="">느리게</option>                     
					<option value="">보통</option>                     
					<option value=""> 빠르게</option>
				</select>
			</td>
			-->  
			
			<td rowspan="5" >
			</td>          
			<td class="align_center" rowspan="5" >
            	<a href="#" class="startFaultCheck"> 
					<img src="/imgs/diagnosis${img_lang!""}/btn_start.png" alt="${LANGCODEMAP["MSG_DIAG_SET_START_DIAGNOSIS"]!}"/>				      			
        	 	</a>   
        	 	<a href="#" class="registerFaultCheckSchedule none"> 
					<img src="/imgs/diagnosis${img_lang!""}/btn_reservation.png" alt="${LANGCODEMAP["MSG_DIAG_SET_RESERVE_DIAGNOSIS"]!}"/>
        	 	</a>        	 	        
			</td>           		
		</tr>
		<tr class="DivideLine">
			<td colspan="2"></td>
		</tr>
		<tr>
			<th class="Lth1">
				<li>${LANGCODEMAP["MSG_DIAG_SET_TEMPLETE"]!}</li>
			</th>
			<td class="Lth0">
				<select class="inputSelect width134" id="selectTemplate">
					<option value="0">${LANGCODEMAP["MSG_DIAG_SET_PLEASE_SELECT"]!}</option>
					<#if faultCheckTempleteList?has_content>
						<#list faultCheckTempleteList as theFault>					
							<#if theFault.index == templateIndex>
								<option value="${theFault.index}" selected="selected">${theFault.name}</option>
							<#else>
								<option value="${theFault.index}">${theFault.name!""}</option>
							</#if> 					
						</#list>	
					</#if>	
				</select>
			</td>
		</tr>
		<tr class="DivideLine">
			<td colspan="2"></td>
		</tr>		
		<tr>
			<th class="Lth1">
				<li>${LANGCODEMAP["MSG_DIAG_SET_RESERVE"]!}</li>
			</th>
			<td class="Lth0">
				<select id="schedulePeriod" class="inputSelect width134">					
					<option value="0">${LANGCODEMAP["MSG_CONSTANT_NOUSE"]!}</option>
					<option value="1">${LANGCODEMAP["MSG_CONSTANT_DAILY"]!}</option>                     
					<option value="2">${LANGCODEMAP["MSG_CONSTANT_EWEEK"]!}</option>                     
					<option value="3">${LANGCODEMAP["MSG_CONSTANT_MONTHLY"]!}</option>
					<option value="4">${LANGCODEMAP["MSG_CONSTANT_ONCE"]!}</option>					
				</select>	
				<!-- 매주 --> 
				<select class="inputSelect everyDayWeek none" id="everyDayWeek">
					<option value="1">${LANGCODEMAP["MSG_CONSTANT_SUN"]!}</option> 
					<option value="2">${LANGCODEMAP["MSG_CONSTANT_MON"]!}</option>
					<option value="3">${LANGCODEMAP["MSG_CONSTANT_TUE"]!}</option>
					<option value="4">${LANGCODEMAP["MSG_CONSTANT_WED"]!}</option>
					<option value="5">${LANGCODEMAP["MSG_CONSTANT_THU"]!}</option>
					<option value="6">${LANGCODEMAP["MSG_CONSTANT_FRI"]!}</option>
					<option value="7">${LANGCODEMAP["MSG_CONSTANT_SAT"]!}</option> 					
				</select>
				
				<!-- 매월 --> 	
				<select class="inputSelect everyDay none" id="everyDay">
					
  					<#if "ko_KR" == langCode>
					<option value="1">01${LANGCODEMAP["MSG_DIAG_SET_DAY"]!}</option><option value="2">02${LANGCODEMAP["MSG_DIAG_SET_DAY"]!}</option><option value="3">03${LANGCODEMAP["MSG_DIAG_SET_DAY"]!}</option><option value="4">04${LANGCODEMAP["MSG_DIAG_SET_DAY"]!}</option>
					<option value="5">05${LANGCODEMAP["MSG_DIAG_SET_DAY"]!}</option><option value="6">06${LANGCODEMAP["MSG_DIAG_SET_DAY"]!}</option><option value="7">07${LANGCODEMAP["MSG_DIAG_SET_DAY"]!}</option><option value="8">08${LANGCODEMAP["MSG_DIAG_SET_DAY"]!}</option>     
					<option value="9">09${LANGCODEMAP["MSG_DIAG_SET_DAY"]!}</option><option value="10">10${LANGCODEMAP["MSG_DIAG_SET_DAY"]!}</option><option value="11">11${LANGCODEMAP["MSG_DIAG_SET_DAY"]!}</option><option value="12">12${LANGCODEMAP["MSG_DIAG_SET_DAY"]!}</option> 
					<option value="13">13${LANGCODEMAP["MSG_DIAG_SET_DAY"]!}</option><option value="14">14${LANGCODEMAP["MSG_DIAG_SET_DAY"]!}</option><option value="15">15${LANGCODEMAP["MSG_DIAG_SET_DAY"]!}</option><option value="16">16${LANGCODEMAP["MSG_DIAG_SET_DAY"]!}</option>
					<option value="17">17${LANGCODEMAP["MSG_DIAG_SET_DAY"]!}</option><option value="18">18${LANGCODEMAP["MSG_DIAG_SET_DAY"]!}</option><option value="19">19${LANGCODEMAP["MSG_DIAG_SET_DAY"]!}</option><option value="20">20${LANGCODEMAP["MSG_DIAG_SET_DAY"]!}</option> 	
					<option value="21">21${LANGCODEMAP["MSG_DIAG_SET_DAY"]!}</option><option value="22">22${LANGCODEMAP["MSG_DIAG_SET_DAY"]!}</option><option value="23">23${LANGCODEMAP["MSG_DIAG_SET_DAY"]!}</option><option value="24">24${LANGCODEMAP["MSG_DIAG_SET_DAY"]!}</option>
					<option value="25">25${LANGCODEMAP["MSG_DIAG_SET_DAY"]!}</option><option value="26">26${LANGCODEMAP["MSG_DIAG_SET_DAY"]!}</option><option value="27">27${LANGCODEMAP["MSG_DIAG_SET_DAY"]!}</option><option value="28">28${LANGCODEMAP["MSG_DIAG_SET_DAY"]!}</option>     
					<option value="29">29${LANGCODEMAP["MSG_DIAG_SET_DAY"]!}</option><option value="30">30${LANGCODEMAP["MSG_DIAG_SET_DAY"]!}</option><option value="31">31${LANGCODEMAP["MSG_DIAG_SET_DAY"]!}</option>
					<#elseif "en_US" == langCode>
					<option value="1">${LANGCODEMAP["MSG_DIAG_SET_DAY"]!} 1</option><option value="2">${LANGCODEMAP["MSG_DIAG_SET_DAY"]!} 2</option><option value="3">${LANGCODEMAP["MSG_DIAG_SET_DAY"]!} 3</option><option value="4">${LANGCODEMAP["MSG_DIAG_SET_DAY"]!} 4</option>
					<option value="5">${LANGCODEMAP["MSG_DIAG_SET_DAY"]!} 5</option><option value="6">${LANGCODEMAP["MSG_DIAG_SET_DAY"]!} 6</option><option value="7">${LANGCODEMAP["MSG_DIAG_SET_DAY"]!} 7</option><option value="8">${LANGCODEMAP["MSG_DIAG_SET_DAY"]!} 8</option>     
					<option value="9">${LANGCODEMAP["MSG_DIAG_SET_DAY"]!} 9</option><option value="10">${LANGCODEMAP["MSG_DIAG_SET_DAY"]!} 10</option><option value="11">${LANGCODEMAP["MSG_DIAG_SET_DAY"]!} 11</option><option value="12">${LANGCODEMAP["MSG_DIAG_SET_DAY"]!} 12</option> 
					<option value="13">${LANGCODEMAP["MSG_DIAG_SET_DAY"]!} 13</option><option value="14">${LANGCODEMAP["MSG_DIAG_SET_DAY"]!} 14</option><option value="15">${LANGCODEMAP["MSG_DIAG_SET_DAY"]!} 15</option><option value="16">${LANGCODEMAP["MSG_DIAG_SET_DAY"]!} 16</option>
					<option value="17">${LANGCODEMAP["MSG_DIAG_SET_DAY"]!} 17</option><option value="18">${LANGCODEMAP["MSG_DIAG_SET_DAY"]!} 18</option><option value="19">${LANGCODEMAP["MSG_DIAG_SET_DAY"]!} 19</option><option value="20">${LANGCODEMAP["MSG_DIAG_SET_DAY"]!} 20</option> 	
					<option value="21">${LANGCODEMAP["MSG_DIAG_SET_DAY"]!} 21</option><option value="22">${LANGCODEMAP["MSG_DIAG_SET_DAY"]!} 22</option><option value="23">${LANGCODEMAP["MSG_DIAG_SET_DAY"]!} 13</option><option value="24">${LANGCODEMAP["MSG_DIAG_SET_DAY"]!} 24</option>
					<option value="25">${LANGCODEMAP["MSG_DIAG_SET_DAY"]!} 25</option><option value="26">${LANGCODEMAP["MSG_DIAG_SET_DAY"]!} 26</option><option value="27">${LANGCODEMAP["MSG_DIAG_SET_DAY"]!} 27</option><option value="28">${LANGCODEMAP["MSG_DIAG_SET_DAY"]!} 28</option>     
					<option value="29">${LANGCODEMAP["MSG_DIAG_SET_DAY"]!} 29</option><option value="30">${LANGCODEMAP["MSG_DIAG_SET_DAY"]!} 30</option><option value="31">${LANGCODEMAP["MSG_DIAG_SET_DAY"]!} 31</option> 					
				 	<#else>
				 	</#if>
				</select>
				
				<!-- 한번만 --> <!-- 14.07.12 날짜 빈 값 유효성 처리 목적에서 datepicker를 일반적 형태로 전환 -->
				<input type="text" name="everyOnce" id="everyOnce" class=" inputText_calendar2 everyOnce none" readonly/>
<!-- 				<img src="imgs/meun/btn_calendar.png" id="datepickerImage" class="datepickerImage none" />				 -->
								
				<!-- 매일 --> 									  				
				<select class="inputSelect everyHour none" id="everyHour">
					<option value="1">01</option><option value="2">02</option><option value="3">03</option><option value="4">04</option>
					<option value="5">05</option><option value="6">06</option><option value="7">07</option><option value="8">08</option>     
					<option value="9">09</option><option value="10">10</option><option value="11">11</option><option value="12">12</option>
					<option value="13">13</option><option value="14">14</option><option value="15">15</option><option value="16">16</option>
					<option value="17">17</option><option value="18">18</option><option value="19">19</option><option value="20">20</option>
					<option value="21">21</option><option value="22">22</option><option value="23">23</option><option value="24">24</option>
				</select>
				<span class="scheduleDay none">:</span>
				<select class="inputSelect everyMin none" id="everyMin">
					<option value="0">00</option>
					<option value="1">01</option><option value="2">02</option><option value="3">03</option><option value="4">04</option>
					<option value="5">05</option><option value="6">06</option><option value="7">07</option><option value="8">08</option>     
					<option value="9">09</option><option value="10">10</option><option value="11">11</option><option value="12">12</option> 
					<option value="13">13</option><option value="14">14</option><option value="15">15</option><option value="16">16</option>
					<option value="17">17</option><option value="18">18</option><option value="19">19</option><option value="20">20</option> 	
					<option value="21">21</option><option value="22">22</option><option value="23">23</option><option value="24">24</option>
					<option value="25">25</option><option value="26">26</option><option value="27">27</option><option value="28">28</option>     
					<option value="29">29</option><option value="30">30</option><option value="31">31</option><option value="32">32</option>
					<option value="33">33</option><option value="34">34</option><option value="35">35</option><option value="36">36</option>
					<option value="37">37</option><option value="38">38</option><option value="39">39</option><option value="40">40</option>
					<option value="41">41</option><option value="42">42</option><option value="43">43</option><option value="44">44</option>
					<option value="45">45</option><option value="46">46</option><option value="47">47</option><option value="48">48</option>     
					<option value="49">49</option><option value="50">50</option><option value="51">51</option><option value="52">52</option>
					<option value="53">53</option><option value="54">54</option><option value="55">55</option><option value="56">56</option>
					<option value="57">57</option><option value="58">58</option><option value="59">59</option>	 	                    
				</select>
			<!--
				<select class="inputSelect every none">
					<option value="am">오전</option>
					<option value="pm">오후</option>                     
				</select>	
			-->
											
			</td>
		</tr>      
		<tr class="EndLine2">
			<td colspan="4"></td>
		</tr>
	</table>	   
	<!-- 2 --> 
	<div class="title_h4_1">
		<li>
			<span class="customTemplate none">${LANGCODEMAP["MSG_DIAG_SET_USER_CUSTOM"]!}</span> 
			<span class="policyTemplate">${LANGCODEMAP["MSG_DIAG_SET_TEMPLETE_DOUBLE"]!}</span>
			<span class="txt_blue policyTemplateNm none"></span>
		</li>
	</div>
	<div class="settingTable">
	
		<#include "settingTableInListContent.ftl"/>
	
	</div>
	<br>
	<table class="Board" cellpadding="0" cellspacing="0">
		<tr class="StartLine1">
			<td></td>
		</tr>											
		<tr class="save">
			<th>
			   <label class="templateSave none">${LANGCODEMAP["MSG_DIAG_SET_USER_CUSTOM"]!}</label>
			   <label class="templateModify"> ${LANGCODEMAP["MSG_DIAG_SET_TEMPLETE_DOUBLE"]!}</label>
				<input type="text" id="templateNm" class="inputText width130" name="templateNm" />&nbsp;					
				
				<input type="button" class=" templateSave templeteSave Btn_white" value="${LANGCODEMAP["MSG_DIAG_SET_SAVE"]!}"/> 			
				<input type="button" class="templateModify none templeteModify Btn_white" value="${LANGCODEMAP["MSG_DIAG_SET_CHANGE"]!}"/>  
				<input type="button" class="templeteDel templateModify none Btn_white" value="${LANGCODEMAP["MSG_DIAG_SET_DELETE"]!}"/>   							
			</th>		
		</tr>
		<tr class="StartLine1">
			<td colspan="3"></td>
		</tr>
		<p>&nbsp;</p>									
		
	</table>
	</form>	
</div>
		
<!-- 진단시작 팝업  -->
<div id="modify_virtualService" class="pop_type_wrap">
	<div>
		<h2 class="progress">${LANGCODEMAP["MSG_DIAG_SET_DIAGNOSISS"]!}</h2>
		<h2 class="complete" style="display: none;">${LANGCODEMAP["MSG_DIAG_SET_COMPLETE"]!}</h2>
		<h2 class="cancel" style="display: none;">${LANGCODEMAP["MSG_DIAG_SET_CANCEL"]!}</h2>
		<h2 class="fail" style="display: none;">${LANGCODEMAP["MSG_DIAG_SET_FAIL"]!}</h2>
	</div>
	<div class="pop2_contents">
		<div class="GroupSelectArea" style="display: none;">
			${LANGCODEMAP["MSG_DIAG_SET_DIAGNOSISS_TARGET_DOUBLE"]!}<select name="adcSelectInGroup" id="" class="inputSelect adcCbx width134">
			</select>
		</div>
		<div class="descriptionArea align_middle">
			<!-- 진단중 -->
			<div class="description">
				00:00:00 <span class="txtblue">${LANGCODEMAP["MSG_DIAG_SET_NOTICE"]!}</span>
			</div>					
			<span class="progressbararea progressbararea_width" ></span>
			<span class="progressbararea_btn"><input type="button" class="diagnosisCancelLnk Btn_blue" value="${LANGCODEMAP["MSG_DIAG_SET_CANCEL"]!}"/> </span>		
			<textarea rows="4" class="CliArea"></textarea>
			<!-- 진단중 -->			
		</div>
		<div class="item1"><p>0</p></div>
		<div class="item2"><p>0</p></div>
		<table class="table_type12">
			<colgroup>
				<col width="50%" />
				<col width="50%" />
			</colgroup>
			<thead>
				<tr>
					<th>CPU</th>
					<th>Memory</th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td>
						<div class="gage"><div class="cpuGauge"><!--${LANGCODEMAP["MSG_DIAG_SET_NOW_VALUE"]!} --></div></div>
						<div class="chart">
							<div class="cpuChart"></div>								
						</div>
					</td>
					<td>
						<div class="gage"><div class="memoryGauge"><!--${LANGCODEMAP["MSG_DIAG_SET_NOW_VALUE"]!} --></div></div>
						<div class="chart">
							<div class="memoryChart"></div>
						</div>
					</td>
				</tr>
		</table>
		<div class="position_cT10">
		 	<input type="button" class="diagnosisResultLnk Btn_white" style="display: none;" value="${LANGCODEMAP["MSG_DIAG_SET_RESULT_VIEW"]!}"/> 
			<input type="button" class="diagnosisCloseLnk Btn_white" value="${LANGCODEMAP["MSG_DIAG_SET_CLOSE"]!}"/>  
		</div>
	</div>
	<p class="diagnosisCloseLnk" style="position:absolute; right:10px; top:6px;">
		<a href="#" title="${LANGCODEMAP["MSG_DIAG_SET_CLOSE"]!}"> <img src="imgs/popup/btn_clse.gif"
			alt="${LANGCODEMAP["MSG_DIAG_SET_CLOSE"]!}" />
		</a>
	</p>
</div>
