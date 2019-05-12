<#if langCode??>
  <#if "ko_KR" == langCode><#assign img_lang = ""></#if>
  <#if "en_US" == langCode><#assign img_lang = "_eng"></#if>
</#if>
<div class="contents_area" >
	<div>
		<img src="imgs/title${img_lang!""}/h3_backupAdd.gif" class="title_h3"/>				 
	</div>
	<form id="sysBackupAddFrm" class="setting">
	<!-- 1 --> 
		<div class="title_h4">
			<li>${LANGCODEMAP["MSG_SYSSETTINGBAK_DEFAULT_SET"]!}</li>
		</div>
		<table class="Board" cellpadding="0" cellspacing="0">
			<colgroup>
				<col width="200px"/>
				<col >
			</colgroup>
			<tr class="DivideLine">
	        	<td colspan="2" class="StartLine"></td>
	        </tr>
			<tr>
				<th class="Lth1">
					<li>예약</li>
				</th>
				<td class="Lth0">
					<select id="scheduleType" name="sysBackupAdd.scheduleType" class="inputSelect width134">					
						<option value="0">${LANGCODEMAP["MSG_CONSTANT_NOUSE"]!}</option>
						<option value="1">${LANGCODEMAP["MSG_CONSTANT_DAILY"]!}</option>                     
						<option value="2">${LANGCODEMAP["MSG_CONSTANT_EWEEK"]!}</option>                     
						<option value="3">${LANGCODEMAP["MSG_CONSTANT_MONTHLY"]!}</option>
						<option value="4">${LANGCODEMAP["MSG_CONSTANT_ONCE"]!}</option>					
					</select>	
					<!-- 매주 --> 
					<select class="inputSelect onSchedule onWeekly none" name="sysBackupAdd.scheduleDayweek" id="scheduleDayWeek">
						<option value="1">${LANGCODEMAP["MSG_CONSTANT_SUN"]!}</option> 
						<option value="2">${LANGCODEMAP["MSG_CONSTANT_MON"]!}</option>
						<option value="3">${LANGCODEMAP["MSG_CONSTANT_TUE"]!}</option>
						<option value="4">${LANGCODEMAP["MSG_CONSTANT_WED"]!}</option>
						<option value="5">${LANGCODEMAP["MSG_CONSTANT_THU"]!}</option>
						<option value="6">${LANGCODEMAP["MSG_CONSTANT_FRI"]!}</option>
						<option value="7">${LANGCODEMAP["MSG_CONSTANT_SAT"]!}</option> 					
					</select>
					
					<!-- 매월 --> 	
					<select class="inputSelect onSchedule onMonthly none" name="sysBackupAdd.scheduleDay" id="scheduleDay">
						<option value="1">01${LANGCODEMAP["MSG_CONSTANT_DAY"]!}</option><option value="2">02${LANGCODEMAP["MSG_CONSTANT_DAY"]!}</option><option value="3">03${LANGCODEMAP["MSG_CONSTANT_DAY"]!}</option><option value="4">04${LANGCODEMAP["MSG_CONSTANT_DAY"]!}</option>
						<option value="5">05${LANGCODEMAP["MSG_CONSTANT_DAY"]!}</option><option value="6">06${LANGCODEMAP["MSG_CONSTANT_DAY"]!}</option><option value="7">07${LANGCODEMAP["MSG_CONSTANT_DAY"]!}</option><option value="8">08${LANGCODEMAP["MSG_CONSTANT_DAY"]!}</option>     
						<option value="9">09${LANGCODEMAP["MSG_CONSTANT_DAY"]!}</option><option value="10">10${LANGCODEMAP["MSG_CONSTANT_DAY"]!}</option><option value="11">11${LANGCODEMAP["MSG_CONSTANT_DAY"]!}</option><option value="12">12${LANGCODEMAP["MSG_CONSTANT_DAY"]!}</option> 
						<option value="13">13${LANGCODEMAP["MSG_CONSTANT_DAY"]!}</option><option value="14">14${LANGCODEMAP["MSG_CONSTANT_DAY"]!}</option><option value="15">15${LANGCODEMAP["MSG_CONSTANT_DAY"]!}</option><option value="16">16${LANGCODEMAP["MSG_CONSTANT_DAY"]!}</option>
						<option value="17">17${LANGCODEMAP["MSG_CONSTANT_DAY"]!}</option><option value="18">18${LANGCODEMAP["MSG_CONSTANT_DAY"]!}</option><option value="19">19${LANGCODEMAP["MSG_CONSTANT_DAY"]!}</option><option value="20">20${LANGCODEMAP["MSG_CONSTANT_DAY"]!}</option> 	
						<option value="21">21${LANGCODEMAP["MSG_CONSTANT_DAY"]!}</option><option value="22">22${LANGCODEMAP["MSG_CONSTANT_DAY"]!}</option><option value="23">23${LANGCODEMAP["MSG_CONSTANT_DAY"]!}</option><option value="24">24${LANGCODEMAP["MSG_CONSTANT_DAY"]!}</option>
						<option value="25">25${LANGCODEMAP["MSG_CONSTANT_DAY"]!}</option><option value="26">26${LANGCODEMAP["MSG_CONSTANT_DAY"]!}</option><option value="27">27${LANGCODEMAP["MSG_CONSTANT_DAY"]!}</option><option value="28">28${LANGCODEMAP["MSG_CONSTANT_DAY"]!}</option>     
						<option value="29">29${LANGCODEMAP["MSG_CONSTANT_DAY"]!}</option><option value="30">30${LANGCODEMAP["MSG_CONSTANT_DAY"]!}</option><option value="31">31${LANGCODEMAP["MSG_CONSTANT_DAY"]!}</option>
					</select>
					
					<!-- 한번만 --> <!-- 14.07.12 날짜 빈 값 유효성 처리 목적에서 datepicker를 일반적 형태로 전환 -->
					<input type="text" id="scheduleDate" class="inputText_calendar2 onSchedule onOnce none" name="sysBackupAdd.scheduleDate" readonly>
									
					<!-- 매일 -->
					<span id="scheduleTime" class="onSchedule onDaily onWeekly onMonthly onOnce none">
						<select class="inputSelect" name="sysBackupAdd.scheduleHour" id="scheduleHour">
							<option value="1">01</option><option value="2">02</option><option value="3">03</option><option value="4">04</option>
							<option value="5">05</option><option value="6">06</option><option value="7">07</option><option value="8">08</option>     
							<option value="9">09</option><option value="10">10</option><option value="11">11</option><option value="12">12</option>
							<option value="13">13</option><option value="14">14</option><option value="15">15</option><option value="16">16</option>
							<option value="17">17</option><option value="18">18</option><option value="19">19</option><option value="20">20</option>
							<option value="21">21</option><option value="22">22</option><option value="23">23</option><option value="24">24</option>
						</select>
						<span>:</span>
						<select class="inputSelect" name="sysBackupAdd.scheduleMinute" id="scheduleMin">
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
					</span>					  				
				</td>
			</tr>
			<tr class="DivideLine">
				<td colspan="2"></td>
			</tr>
			<tr class="onSchedule onNow">
				<th class="Lth2">
					<li>${LANGCODEMAP["MSG_SYSSETTINGBAK_FILE_NAME"]!}</li>
				</th>
				<td class="Lth0">
					<input type="text" name="sysBackupAdd.fileName"class="inputText">
				</td>
			</tr>
			<tr class="DivideLine onSchedule onNow">
				<td colspan="2"></td>
			</tr>
			<tr class="onSchedule onNow">
				<th class="Lth1">
					<li>${LANGCODEMAP["MSG_SYSSETTINGBAK_EXPLAIN"]!}</li>
				</th>
				<td class="Lth0">
				<textarea name="sysBackupAdd.description" cols="80%" class="desc valdescription" ></textarea>
				</td>
			</tr>
			<tr class="DivideLine onSchedule onNow">
				<td colspan="2"></td>
			</tr>
			<tr>
				<th class="Lth1">
					<li>${LANGCODEMAP["MSG_SYSSETTINGBAK_BAK_OPT"]!}</li>
				</th>
				<td class="Lth0">
					<input type="radio" id="all" name="sysBackupAdd.target" class="Radio" checked="checked" value="${LANGCODEMAP["MSG_SYSSETTINGBAK_TARGET_ALL"]!}"/>
						<label for="select_TARGET_ALL">${LANGCODEMAP["MSG_CONSTANT_ADCS_SET_LOG_INFO"]!}</label>&nbsp;
					<input type="radio" id="adcsmartSettings" name="sysBackupAdd.target" class="Radio" value="${LANGCODEMAP["MSG_SYSSETTINGBAK_TARGET_ADCSMARTSET"]!}"/>
						<label for="select_TARGET_ADCSMARTSETTINGS">${LANGCODEMAP["MSG_CONSTANT_ADCS_SET_INFO"]!}</label>&nbsp;						
					<input type="radio" id="adcsmart" name="sysBackupAdd.target" class="Radio" value="${LANGCODEMAP["MSG_SYSSETTINGBAK_TARGET_ADCSMARTLOGS"]!}"/>
						<label for="select_ADCSMARTLOGS">${LANGCODEMAP["MSG_CONSTANT_ADCS_LOG_INFO"]!}</label>&nbsp;						
					<input type="radio" id="Settings" name="sysBackupAdd.target" class="Radio" value="${LANGCODEMAP["MSG_SYSSETTINGBAK_TARGET_ADCSETTINGS"]!}"/>
						<label for="select_ADCSETTINGS">${LANGCODEMAP["MSG_CONSTANT_ADC_SET_INFO"]!}</label>
				</td>	
			</tr>
			<tr class="DivideLine">
				<td colspan="2"></td>
			</tr>
			<tr>
				<th class="Lth1">
					<li>${LANGCODEMAP["MSG_SYSSETTINGBAK_ADC_LOG_DEL"]!}</li>
				</th>
				<td class="Lth0">
					<input name ="sysBackupAdd.deletesLogs" type="checkbox" id="logDel" value="true"/> 
					${LANGCODEMAP["MSG_SYSSETTINGBAK_ADC_LOG_DEL_SEL"]!}
				</td>
			</tr>
			<tr class="EndLine2">
				<td colspan="2"></td>
			</tr>
			<tr>
				<td colspan="4">                            
					<div class="position_cT10">
						<input type="button" class="okLnk Btn_red" value="${LANGCODEMAP["MSG_SYSSETTINGBAK_COMPLETE"]!}"/> 
						<input type="button" class="cancelLnk Btn_white" value="${LANGCODEMAP["MSG_SYSSETTINGBAK_CANCEL"]!}"/> 											
					</div> 
				</td>
			</tr>                          
		</table> 
	</form>			   
</div>

