<#setting number_format="0.####"> 

<#if orderType??>
	<#if 1 == orderType></#if><!-- 상태 -->				<#if 2 == orderType></#if><!-- 종류 -->
	<#if 3 == orderType></#if><!-- ADC이름 --> 			<#if 4 == orderType></#if><!-- State(이중화상태) - ADCACTIVEBACKUP -->
	<#if 5 == orderType></#if><!-- 장비명 --> 				<#if 6 == orderType></#if><!-- 버전 -->
	<#if 7 == orderType></#if><!-- Throughput --> 		<#if 8 == orderType></#if><!-- Concurrent Sessions -->
	<#if 9 == orderType></#if><!-- Uptime -->			<#if 10 == orderType></#if><!-- ADC IP -->
	<#if 11 == orderType></#if><!-- 최근업데이트  -->			<#if 12 == orderType></#if><!-- CPU -->
	<#if 13 == orderType></#if><!-- Memory -->			<#if 14 == orderType></#if><!-- Error Packets -->
	<#if 15 == orderType></#if><!-- Dropped Packets -->	<#if 16 == orderType></#if><!-- 인증서 -->
	<#if 17 == orderType></#if><!-- 인터페이스 사용 -->		<#if 18 == orderType></#if><!-- Filter 사용 -->
	<#if 19 == orderType></#if><!-- 서비스 사용-->			<#if 20 == orderType></#if><!-- ADC 로그 -->
	<#if 21 == orderType></#if><!-- 설정이력 -->
</#if>
<#if orderDir??>
	<#if 1 == orderDir></#if><!-- Asc -->
	<#if 2 == orderDir></#if><!-- Desc -->
</#if>	

<colgroup>		
	<col width="60px" />
	<col width="60px" />
	<col width="200px" />
	<col width="120px" />
	<col width="240px" />
	<col width="120px" />	
	<col width="190px" />
	<col width="150px" />			
	<col width="100px" />
	<!--<col width="140px" />-->
	<!--<col width="150px" />-->
	<!--<col width="80px" />-->
	<!--<col width="110px" />-->
	<!--<col width="140px" />-->
	<!--<col width="160px" />-->
	<!--<col width="130px" />-->
	<!--<col width="150px" />-->
	<!--<col width="120px" />-->
	<!--<col width="130px" />-->
	<!--<col width="130px" />-->
	<!--<col width="140px" />-->
</colgroup>
<thead>
	<tr class="ContentsHeadLine selColHead">
		<th class="bg_row2 default Rcolor">			
			<span class="css_textCursor">
				<#if orderDir == 2 && orderType == 1>
					<a class="orderDir_Desc orderHeader">${LANGCODEMAP["MSG_ADCSETTING_STATUS"]!}<img src="imgs/Desc.gif"/><span class="none orderType">1</span><span class="none orderDir">1</span></a>
				<#elseif orderDir == 1 && orderType == 1>
					<a class="orderDir_Asc orderHeader">${LANGCODEMAP["MSG_ADCSETTING_STATUS"]!}<img src="imgs/Asc.gif"/><span class="none orderType">1</span><span class="none orderDir">2</span></a>
				<#else>
					<a class="orderDir_None orderHeader">${LANGCODEMAP["MSG_ADCSETTING_STATUS"]!}<img src="imgs/none.gif"/><span class="none orderType">1</span><span class="none orderDir">2</span></a>
				</#if>
			</span>			
		</th>
		<th class="default">
			<span class="css_textCursor">
				<#if orderDir == 2 && orderType == 2>
					<a class="orderDir_Desc orderHeader">${LANGCODEMAP["MSG_ADCSETTING_TYPE"]!}<img src="imgs/Desc.gif"/><span class="none orderType">2</span><span class="none orderDir">1</span></a>
				<#elseif orderDir == 1 && orderType == 2>
					<a class="orderDir_Asc orderHeader">${LANGCODEMAP["MSG_ADCSETTING_TYPE"]!}<img src="imgs/Asc.gif"/><span class="none orderType">2</span><span class="none orderDir">2</span></a>
				<#else>
					<a class="orderDir_None orderHeader">${LANGCODEMAP["MSG_ADCSETTING_TYPE"]!}<img src="imgs/none.gif"/><span class="none orderType">2</span><span class="none orderDir">2</span></a>
				</#if>
			</span>	
		</th>
		<th class="default">
			<span class="css_textCursor">
				<#if orderDir == 2 && orderType == 3>
					<a class="orderDir_Desc orderHeader">${LANGCODEMAP["MSG_ADCSETTING_ADC_NAME"]!}<img src="imgs/Desc.gif"/><span class="none orderType">3</span><span class="none orderDir">1</span></a>
				<#elseif orderDir == 1 && orderType == 3>
					<a class="orderDir_Asc orderHeader">${LANGCODEMAP["MSG_ADCSETTING_ADC_NAME"]!}<img src="imgs/Asc.gif"/><span class="none orderType">3</span><span class="none orderDir">2</span></a>
				<#else>
					<a class="orderDir_None orderHeader">${LANGCODEMAP["MSG_ADCSETTING_ADC_NAME"]!}<img src="imgs/none.gif"/><span class="none orderType">3</span><span class="none orderDir">2</span></a>
				</#if>
			</span>	
		</th>
		<th class="default">
			<span class="css_textCursor">
				<#if orderDir == 2 && orderType == 4>
					<a class="orderDir_Desc orderHeader">${LANGCODEMAP["MSG_ADCSETTING_CONTINUOUS_STATUS"]!}<img src="imgs/Desc.gif"/><span class="none orderType">4</span><span class="none orderDir">1</span></a>
				<#elseif orderDir == 1 && orderType == 4>
					<a class="orderDir_Asc orderHeader">${LANGCODEMAP["MSG_ADCSETTING_CONTINUOUS_STATUS"]!}<img src="imgs/Asc.gif"/><span class="none orderType">4</span><span class="none orderDir">2</span></a>
				<#else>
					<a class="orderDir_None orderHeader">${LANGCODEMAP["MSG_ADCSETTING_CONTINUOUS_STATUS"]!}<img src="imgs/none.gif"/><span class="none orderType">4</span><span class="none orderDir">2</span></a>
				</#if>
			</span>	
		</th>
		<th class="default">
			<span class="css_textCursor">
				<#if orderDir == 2 && orderType == 5>
					<a class="orderDir_Desc orderHeader">${LANGCODEMAP["MSG_ADCSETTING_APPLIANCE_NAME"]!}<img src="imgs/Desc.gif"/><span class="none orderType">5</span><span class="none orderDir">1</span></a>
				<#elseif orderDir == 1 && orderType == 5>
					<a class="orderDir_Asc orderHeader">${LANGCODEMAP["MSG_ADCSETTING_APPLIANCE_NAME"]!}<img src="imgs/Asc.gif"/><span class="none orderType">5</span><span class="none orderDir">2</span></a>
				<#else>
					<a class="orderDir_None orderHeader">${LANGCODEMAP["MSG_ADCSETTING_APPLIANCE_NAME"]!}<img src="imgs/none.gif"/><span class="none orderType">5</span><span class="none orderDir">2</span></a>
				</#if>
			</span>	
		</th>
		<th class="default">
			<span class="css_textCursor">
				<#if orderDir == 2 && orderType == 6>
					<a class="orderDir_Desc orderHeader">${LANGCODEMAP["MSG_ADCSETTING_OSVERSION"]!}<img src="imgs/Desc.gif"/><span class="none orderType">6</span><span class="none orderDir">1</span></a>
				<#elseif orderDir == 1 && orderType == 6>
					<a class="orderDir_Asc orderHeader">${LANGCODEMAP["MSG_ADCSETTING_OSVERSION"]!}<img src="imgs/Asc.gif"/><span class="none orderType">6</span><span class="none orderDir">2</span></a>
				<#else>
					<a class="orderDir_None orderHeader">${LANGCODEMAP["MSG_ADCSETTING_OSVERSION"]!}<img src="imgs/none.gif"/><span class="none orderType">6</span><span class="none orderDir">2</span></a>
				</#if>
			</span>	
		</th>	
	<#if montotalAdc.condition.concurrentSession.isSelect()>
		<th>
	<#else>			
		<th class="none">
	</#if>
			<span class="css_textCursor">
				<#if orderDir == 2 && orderType == 8>
					<a class="orderDir_Desc orderHeader">${LANGCODEMAP["MSG_APPLIANCE_SESSION_COUNT"]!}(${LANGCODEMAP["MSG_APPLIANCE_COUNT"]!})<img src="imgs/Desc.gif"/><span class="none orderType">8</span><span class="none orderDir">1</span></a>
				<#elseif orderDir == 1 && orderType == 8>
					<a class="orderDir_Asc orderHeader">${LANGCODEMAP["MSG_APPLIANCE_SESSION_COUNT"]!}(${LANGCODEMAP["MSG_APPLIANCE_COUNT"]!})<img src="imgs/Asc.gif"/><span class="none orderType">8</span><span class="none orderDir">2</span></a>
				<#else>
					<a class="orderDir_None orderHeader">${LANGCODEMAP["MSG_APPLIANCE_SESSION_COUNT"]!}(${LANGCODEMAP["MSG_APPLIANCE_COUNT"]!})<img src="imgs/none.gif"/><span class="none orderType">8</span><span class="none orderDir">2</span></a>
				</#if>
			</span>	
		</th>				
	<#if montotalAdc.condition.throughput.isSelect()>
		<th>
	<#else>			
		<th class="none">
	</#if>
			<span class="css_textCursor">
				<#if orderDir == 2 && orderType == 7>
					<a class="orderDir_Desc orderHeader">${LANGCODEMAP["MSG_APPLIANCE_ADC_TRAFFIC"]!}(${LANGCODEMAP["MSG_PERFLIST_BPS"]!})<img src="imgs/Desc.gif"/><span class="none orderType">7</span><span class="none orderDir">1</span></a>
				<#elseif orderDir == 1 && orderType == 7>
					<a class="orderDir_Asc orderHeader">${LANGCODEMAP["MSG_APPLIANCE_ADC_TRAFFIC"]!}(${LANGCODEMAP["MSG_PERFLIST_BPS"]!})<img src="imgs/Asc.gif"/><span class="none orderType">7</span><span class="none orderDir">2</span></a>
				<#else>
					<a class="orderDir_None orderHeader">${LANGCODEMAP["MSG_APPLIANCE_ADC_TRAFFIC"]!}(${LANGCODEMAP["MSG_PERFLIST_BPS"]!})<img src="imgs/none.gif"/><span class="none orderType">7</span><span class="none orderDir">2</span></a>
				</#if>
			</span>					
		</th>	
	<#if montotalAdc.condition.uptimeAge.isSelect()>
		<th>
	<#else>			
		<th class="none">
	</#if>
			<span class="css_textCursor">
				<#if orderDir == 2 && orderType == 9>
					<a class="orderDir_Desc orderHeader">${LANGCODEMAP["MSG_ALERT_UPTIME"]!}<img src="imgs/Desc.gif"/><span class="none orderType">9</span><span class="none orderDir">1</span></a>
				<#elseif orderDir == 1 && orderType == 9>
					<a class="orderDir_Asc orderHeader">${LANGCODEMAP["MSG_ALERT_UPTIME"]!}<img src="imgs/Asc.gif"/><span class="none orderType">9</span><span class="none orderDir">2</span></a>
				<#else>
					<a class="orderDir_None orderHeader">${LANGCODEMAP["MSG_ALERT_UPTIME"]!}<img src="imgs/none.gif"/><span class="none orderType">9</span><span class="none orderDir">2</span></a>
				</#if>
			</span>	
		</th>	
	<#if montotalAdc.condition.adcIp.isSelect()>
		<th>
	<#else>			
		<th class="none">
	</#if>	
			<span class="css_textCursor">
				<#if orderDir == 2 && orderType == 10>
					<a class="orderDir_Desc orderHeader">ADC IP<img src="imgs/Desc.gif"/><span class="none orderType">10</span><span class="none orderDir">1</span></a>
				<#elseif orderDir == 1 && orderType == 10>
					<a class="orderDir_Asc orderHeader">ADC IP<img src="imgs/Asc.gif"/><span class="none orderType">10</span><span class="none orderDir">2</span></a>
				<#else>
					<a class="orderDir_None orderHeader">ADC IP<img src="imgs/none.gif"/><span class="none orderType">10</span><span class="none orderDir">2</span></a>
				</#if>
			</span>				
		</th>
	<#if montotalAdc.condition.configTime.isSelect()>
		<th>
	<#else>			
		<th class="none">
	</#if>	
			<span class="css_textCursor">
				<#if orderDir == 2 && orderType == 11>
					<a class="orderDir_Desc orderHeader">${LANGCODEMAP["MSG_GROUPMONITOR_LASTUPDATE"]!}<img src="imgs/Desc.gif"/><span class="none orderType">11</span><span class="none orderDir">1</span></a>
				<#elseif orderDir == 1 && orderType == 11>
					<a class="orderDir_Asc orderHeader">${LANGCODEMAP["MSG_GROUPMONITOR_LASTUPDATE"]!}<img src="imgs/Asc.gif"/><span class="none orderType">11</span><span class="none orderDir">2</span></a>
				<#else>
					<a class="orderDir_None orderHeader">${LANGCODEMAP["MSG_GROUPMONITOR_LASTUPDATE"]!}<img src="imgs/none.gif"/><span class="none orderType">11</span><span class="none orderDir">2</span></a>
				</#if> 
			</span>
		</th>
	<#if montotalAdc.condition.cpu.isSelect()>
		<th title="${LANGCODEMAP["MSG_GROUPMONITOR_ALTEON_SPAVG"]!}">
	<#else>			
		<th class="none" title="${LANGCODEMAP["MSG_GROUPMONITOR_ALTEON_SPAVG"]!}">
	</#if>	
	<!--
			<span class="css_textCursor">
				<#if orderDir == 2 && orderType == 12>
					<a class="orderDir_Desc orderHeader">CPU(%)<img src="imgs/Desc.gif"/><span class="none orderType">12</span><span class="none orderDir">1</span></a>
				<#elseif orderDir == 1 && orderType == 12>
					<a class="orderDir_Asc orderHeader">CPU(%)<img src="imgs/Asc.gif"/><span class="none orderType">12</span><span class="none orderDir">2</span></a>
				<#else>
					<a class="orderDir_None orderHeader">CPU(%)<img src="imgs/none.gif"/><span class="none orderType">12</span><span class="none orderDir">2</span></a>
				</#if> 
			</span>
	-->
		CPU(%)</th>
	<#if montotalAdc.condition.memory.isSelect()>
		<th>
	<#else>			
		<th class="none">
	</#if>	
			<span class="css_textCursor">
				<#if orderDir == 2 && orderType == 13>
					<a class="orderDir_Desc orderHeader">Memory(%)<img src="imgs/Desc.gif"/><span class="none orderType">13</span><span class="none orderDir">1</span></a>
				<#elseif orderDir == 1 && orderType == 13>
					<a class="orderDir_Asc orderHeader">Memory(%)<img src="imgs/Asc.gif"/><span class="none orderType">13</span><span class="none orderDir">2</span></a>
				<#else>
					<a class="orderDir_None orderHeader">Memory(%)<img src="imgs/none.gif"/><span class="none orderType">13</span><span class="none orderDir">2</span></a>
				</#if> 
			</span>
		</th>
	<#if montotalAdc.condition.errorPackets.isSelect()>
		<th>
	<#else>			
		<th class="none">
	</#if>
			<span class="css_textCursor">
				<#if orderDir == 2 && orderType == 14>
					<a class="orderDir_Desc orderHeader">Error Packets${LANGCODEMAP["MSG_GROUPMONITOR_EA"]!}<img src="imgs/Desc.gif"/><span class="none orderType">14</span><span class="none orderDir">1</span></a>
				<#elseif orderDir == 1 && orderType == 14>
					<a class="orderDir_Asc orderHeader">Error Packets${LANGCODEMAP["MSG_GROUPMONITOR_EA"]!}<img src="imgs/Asc.gif"/><span class="none orderType">14</span><span class="none orderDir">2</span></a>
				<#else>
					<a class="orderDir_None orderHeader">Error Packets${LANGCODEMAP["MSG_GROUPMONITOR_EA"]!}<img src="imgs/none.gif"/><span class="none orderType">14</span><span class="none orderDir">2</span></a>
				</#if> 
			</span>
		</th>
	<#if montotalAdc.condition.dropPackets.isSelect()>
		<th>
	<#else>			
		<th class="none">
	</#if>
			<span class="css_textCursor">
				<#if orderDir == 2 && orderType == 15>
					<a class="orderDir_Desc orderHeader">Dropped Packets${LANGCODEMAP["MSG_GROUPMONITOR_EA"]!}<img src="imgs/Desc.gif"/><span class="none orderType">15</span><span class="none orderDir">1</span></a>
				<#elseif orderDir == 1 && orderType == 15>
					<a class="orderDir_Asc orderHeader">Dropped Packets${LANGCODEMAP["MSG_GROUPMONITOR_EA"]!}<img src="imgs/Asc.gif"/><span class="none orderType">15</span><span class="none orderDir">2</span></a>
				<#else>
					<a class="orderDir_None orderHeader">Dropped Packets${LANGCODEMAP["MSG_GROUPMONITOR_EA"]!}<img src="imgs/none.gif"/><span class="none orderType">15</span><span class="none orderDir">2</span></a>
				</#if> 
			</span>
		</th>
	<#if montotalAdc.condition.sslCertValidDays.isSelect()>
		<th>
	<#else>			
		<th class="none">
	</#if>
			<span class="css_textCursor">
				<#if orderDir == 2 && orderType == 16>
					<a class="orderDir_Desc orderHeader">${LANGCODEMAP["MSG_GROUPMONITOR_CERTIFICATION"]!}${LANGCODEMAP["MSG_GROUPMONITOR_EA"]!}<img src="imgs/Desc.gif"/><span class="none orderType">16</span><span class="none orderDir">1</span></a>
				<#elseif orderDir == 1 && orderType == 16>
					<a class="orderDir_Asc orderHeader">${LANGCODEMAP["MSG_GROUPMONITOR_CERTIFICATION"]!}${LANGCODEMAP["MSG_GROUPMONITOR_EA"]!}<img src="imgs/Asc.gif"/><span class="none orderType">16</span><span class="none orderDir">2</span></a>
				<#else>
					<a class="orderDir_None orderHeader">${LANGCODEMAP["MSG_GROUPMONITOR_CERTIFICATION"]!}${LANGCODEMAP["MSG_GROUPMONITOR_EA"]!}<img src="imgs/none.gif"/><span class="none orderType">16</span><span class="none orderDir">2</span></a>
				</#if> 
			</span>
		</th>
	<#if montotalAdc.condition.interfaceCount.isSelect()>
		<th>
	<#else>			
		<th class="none">
	</#if>		
			<span class="css_textCursor">
				<#if orderDir == 2 && orderType == 17>
					<a class="orderDir_Desc orderHeader">${LANGCODEMAP["MSG_GROUPMONITOR_INTERFACEUSE"]!}${LANGCODEMAP["MSG_GROUPMONITOR_EA"]!}<img src="imgs/Desc.gif"/><span class="none orderType">17</span><span class="none orderDir">1</span></a>
				<#elseif orderDir == 1 && orderType == 17>
					<a class="orderDir_Asc orderHeader">${LANGCODEMAP["MSG_GROUPMONITOR_INTERFACEUSE"]!}${LANGCODEMAP["MSG_GROUPMONITOR_EA"]!}<img src="imgs/Asc.gif"/><span class="none orderType">17</span><span class="none orderDir">2</span></a>
				<#else>
					<a class="orderDir_None orderHeader">${LANGCODEMAP["MSG_GROUPMONITOR_INTERFACEUSE"]!}${LANGCODEMAP["MSG_GROUPMONITOR_EA"]!}<img src="imgs/none.gif"/><span class="none orderType">17</span><span class="none orderDir">2</span></a>
				</#if> 
			</span>
		</th>
	<#if montotalAdc.condition.filter.isSelect()>
		<th>
	<#else>			
		<th class="none">
	</#if>
			<span class="css_textCursor">
				<#if orderDir == 2 && orderType == 18>
					<a class="orderDir_Desc orderHeader">${LANGCODEMAP["MSG_GROUPMONITOR_FILTERUSE"]!}${LANGCODEMAP["MSG_GROUPMONITOR_EA"]!}<img src="imgs/Desc.gif"/><span class="none orderType">18</span><span class="none orderDir">1</span></a>
				<#elseif orderDir == 1 && orderType == 18>
					<a class="orderDir_Asc orderHeader">${LANGCODEMAP["MSG_GROUPMONITOR_FILTERUSE"]!}${LANGCODEMAP["MSG_GROUPMONITOR_EA"]!}<img src="imgs/Asc.gif"/><span class="none orderType">18</span><span class="none orderDir">2</span></a>
				<#else>
					<a class="orderDir_None orderHeader">${LANGCODEMAP["MSG_GROUPMONITOR_FILTERUSE"]!}${LANGCODEMAP["MSG_GROUPMONITOR_EA"]!}<img src="imgs/none.gif"/><span class="none orderType">18</span><span class="none orderDir">2</span></a>
				</#if> 
			</span>
		</th>
	<#if montotalAdc.condition.service.isSelect()>
		<th>
	<#else>			
		<th class="none">
	</#if>
			<span class="css_textCursor">
				<#if orderDir == 2 && orderType == 19>
					<a class="orderDir_Desc orderHeader">${LANGCODEMAP["MSG_GROUPMONITOR_SERVICEUSE"]!}${LANGCODEMAP["MSG_GROUPMONITOR_EA"]!}<img src="imgs/Desc.gif"/><span class="none orderType">19</span><span class="none orderDir">1</span></a>
				<#elseif orderDir == 1 && orderType == 19>
					<a class="orderDir_Asc orderHeader">${LANGCODEMAP["MSG_GROUPMONITOR_SERVICEUSE"]!}${LANGCODEMAP["MSG_GROUPMONITOR_EA"]!}<img src="imgs/Asc.gif"/><span class="none orderType">19</span><span class="none orderDir">2</span></a>
				<#else>
					<a class="orderDir_None orderHeader">${LANGCODEMAP["MSG_GROUPMONITOR_SERVICEUSE"]!}${LANGCODEMAP["MSG_GROUPMONITOR_EA"]!}<img src="imgs/none.gif"/><span class="none orderType">19</span><span class="none orderDir">2</span></a>
				</#if> 
			</span>
		</th>
	<#if montotalAdc.condition.adcLog24Hour.isSelect()>
		<th>
	<#else>			
		<th class="none">
	</#if>
			<span class="css_textCursor">
				<#if orderDir == 2 && orderType == 20>
					<a class="orderDir_Desc orderHeader">${LANGCODEMAP["MSG_GROUPMONITOR_ADCLOG"]!}${LANGCODEMAP["MSG_GROUPMONITOR_EA"]!}<img src="imgs/Desc.gif"/><span class="none orderType">20</span><span class="none orderDir">1</span></a>
				<#elseif orderDir == 1 && orderType == 20>
					<a class="orderDir_Asc orderHeader">${LANGCODEMAP["MSG_GROUPMONITOR_ADCLOG"]!}${LANGCODEMAP["MSG_GROUPMONITOR_EA"]!}<img src="imgs/Asc.gif"/><span class="none orderType">20</span><span class="none orderDir">2</span></a>
				<#else>
					<a class="orderDir_None orderHeader">${LANGCODEMAP["MSG_GROUPMONITOR_ADCLOG"]!}${LANGCODEMAP["MSG_GROUPMONITOR_EA"]!}<img src="imgs/none.gif"/><span class="none orderType">20</span><span class="none orderDir">2</span></a>
				</#if> 
			</span>
		</th>
	<#if montotalAdc.condition.slbConfig24Hour.isSelect()>
		<th>
	<#else>			
		<th class="none">
	</#if>
			<span class="css_textCursor">
				<#if orderDir == 2 && orderType == 21>
					<a class="orderDir_Desc orderHeader">${LANGCODEMAP["MSG_GROUPMONITOR_SETTTING_HISTORY"]!}${LANGCODEMAP["MSG_GROUPMONITOR_EA"]!}<img src="imgs/Desc.gif"/><span class="none orderType">21</span><span class="none orderDir">1</span></a>
				<#elseif orderDir == 1 && orderType == 21>
					<a class="orderDir_Asc orderHeader">${LANGCODEMAP["MSG_GROUPMONITOR_SETTTING_HISTORY"]!}${LANGCODEMAP["MSG_GROUPMONITOR_EA"]!}<img src="imgs/Asc.gif"/><span class="none orderType">21</span><span class="none orderDir">2</span></a>
				<#else>
					<a class="orderDir_None orderHeader">${LANGCODEMAP["MSG_GROUPMONITOR_SETTTING_HISTORY"]!}${LANGCODEMAP["MSG_GROUPMONITOR_EA"]!}<img src="imgs/none.gif"/><span class="none orderType">21</span><span class="none orderDir">2</span></a>
				</#if> 
			</span>
		</th>
	</tr>
</thead>		
<tbody>
	<tr class="ContentsLine1 adcList none">
		<td class="align_center status"></td>
		<td class="align_center adcType"></td>
		<td class="align_left_P10 textOver adcName"></td>                      
		<td class="align_left_P10 activeBackupState"></td>            
		<td class="align_left_P10 textOver model"></td>  
		<td class="align_center swVersion"></td>
	<#if montotalAdc.condition.concurrentSession.isSelect()>
		<td class="align_center concurrentSession"></td> 
	<#else>			
		<td class="align_center concurrentSession none"></td> 
	</#if>
	<#if montotalAdc.condition.throughput.isSelect()>
		<td class="align_center throughput"></td> 
	<#else>			
		<td class="align_center throughput none"></td> 
	</#if>
	<#if montotalAdc.condition.uptimeAge.isSelect()>
		<td class="align_right_P20 uptimeAge"></td> 
	<#else>			
		<td class="align_right_P20 uptimeAge none"></td> 
	</#if>
	<#if montotalAdc.condition.adcIp.isSelect()>
		<td class="align_left_P10 adcIp"></td> 
	<#else>			
		<td class="align_left_P10 adcIp none"></td> 
	</#if>
	<#if montotalAdc.condition.configTime.isSelect()>
		<td class="align_center configTime"></td> 
	<#else>			
		<td class="align_center configTime none"></td> 
	</#if>  
	<#if montotalAdc.condition.cpu.isSelect()>
		<td class="align_center cpu" title="Alteon은 SP 평균"></td> 
	<#else>			
		<td class="align_center cpu none" title="Alteon은 SP 평균"></td> 
	</#if>  
	<#if montotalAdc.condition.memory.isSelect()>
		<td class="align_center memory"></td> 
	<#else>			
		<td class="align_center memory none"></td> 
	</#if>         
	<#if montotalAdc.condition.errorPackets.isSelect()>
		<td class="align_center errorPackets"></td> 
	<#else>			
		<td class="align_center errorPackets none"></td> 
	</#if>
	<#if montotalAdc.condition.dropPackets.isSelect()>
		<td class="align_center dropPackets"></td> 
	<#else>			
		<td class="align_center dropPackets none"></td> 
	</#if> 
	<#if montotalAdc.condition.sslCertValidDays.isSelect()>
		<td class="align_center sslCertValidDays"></td> 
	<#else>			
		<td class="align_center sslCertValidDays none"></td> 
	</#if>
	<#if montotalAdc.condition.interfaceCount.isSelect()>
		<td class="align_center interfaceAvailable"></td>
	<#else>			
		<td class="align_center interfaceAvailable none"></td>
	</#if>
	<#if montotalAdc.condition.filter.isSelect()>
		<td class="align_center filterUse"></td>
	<#else>			
		<td class="align_center filterUse none"></td>
	</#if> 
	<#if montotalAdc.condition.service.isSelect()>
		<td class="align_center serviceAvailable"></td>
	<#else>			
		<td class="align_center serviceAvailable none"></td>
	</#if>
	<#if montotalAdc.condition.adcLog24Hour.isSelect()>
		<td class="align_center adcLog24Hour"></td>
	<#else>			
		<td class="align_center adcLog24Hour none"></td>
	</#if>      
	<#if montotalAdc.condition.slbConfig24Hour.isSelect()>
		<td class="align_center slbConfig24Hour"></td>
	<#else>			
		<td class="align_center slbConfig24Hour none"></td>
	</#if>
	</tr>
	<#list montotalAdc.adcList![] as theAdcList>
	<tr class="ContentsLine1 adcList">					
		<td class="align_center status">
			<#if theAdcList.status == 1>
				<img src="imgs/icon/icon_1d_conn.png" alt="available" />
			<#elseif theAdcList.status == 0>
				<img src="imgs/icon/icon_1d_disconn.png" alt="unavailable" />					
			<#else>
				<img src="imgs/icon/icon_1d_1.png" />					
			</#if>
		</td>
		<td class="align_center adcType">
			<#if (theAdcList.adcType) == 1>
				<img src="imgs/icon/adc/icon_f5_s.png" alt="f5" />	
			<#elseif (theAdcList.adcType) == 2>
				<img src="imgs/icon/adc/icon_alteon_s.png" alt="alteon" />
			<#elseif (theAdcList.adcType) == 3>
				<img src="imgs/icon/adc/icon_piolink_s.png" alt="PAS" />	
			<#elseif (theAdcList.adcType) == 4>
				<img src="imgs/icon/adc/icon_piolink_s.png" alt="PASK" />	
			<#elseif (theAdcList.adcType) == -2>
				<img src="imgs/icon/adc/icon_piolink_s.png" alt="PASK" />
			<#else>
				Unknown	
			</#if>
		</td>
		<td class="align_left_P10 textOver adcName" title="${(theAdcList.adcName)!''}">
			<a href="javascript:;" class="adcMonitoringLnk"><span style="display:none;">${(theAdcList.adcIndex)!''}</span>${(theAdcList.adcName)!''}</a>
		</td>                      
		<td class="align_left_P10 activeBackupState">
			<#if theAdcList.activeBackupState == 1>
				<img class="conn" src="imgs/icon/icon_active_4.png" alt="active">&nbsp;Active
			<#elseif theAdcList.activeBackupState == 2>
				<img class="conn" src="imgs/icon/icon_standby_4.png" alt="standby">&nbsp;Standby		
			<#elseif theAdcList.activeBackupState == 0>
				<img class="conn" src="imgs/icon/icon_alone_4.png" alt="alone">&nbsp;Unknown
			<#else>
				<img class="conn" src="imgs/icon/icon_alone_4.png" alt="alone"> Unknown				
			</#if>					
		</td>            
		<td class="align_left_P10 textOver model">
			<#if (theAdcList.model)?? >
				${(theAdcList.model)!''}
			<#else>
				-
			</#if>			
		</td>                        
		<td class="align_center swVersion">${(theAdcList.swVersion)!''}</td>	
	<#if montotalAdc.condition.concurrentSession.isSelect()>
		<td class="align_center concurrentSession">
	<#else>			
		<td class="align_center concurrentSession none">
	</#if>
			<#if (theAdcList.concurrentSession) == -1>
				-
			<#else>
				${(theAdcList.concurrentSessionUnit)!''}
			</#if>
		</td>
	<#if montotalAdc.condition.throughput.isSelect()>
		<td class="align_center throughput">
	<#else>			
		<td class="align_center throughput none"> 
	</#if>		
			<#if (theAdcList.throughput) == -1>
				-
			<#else>
				${(theAdcList.throughputUnit)!''}
			</#if>
		</td>
	<#if montotalAdc.condition.uptimeAge.isSelect()>
		<td class="align_right_P20 textOver uptimeAge" title="${(theAdcList.uptimeAge)!''}"> 
	<#else>			
		<td class="align_right_P20 textOver uptimeAge none" title="${(theAdcList.uptimeAge)!''}"> 
	</#if>
			<#if (theAdcList.uptimeAge)?? >
				${(theAdcList.uptimeAge)!''}
			<#else>
				-
			</#if>
		</td>	
	<#if montotalAdc.condition.adcIp.isSelect()>
		<td class="align_left_P10 adcIp">${(theAdcList.adcIp)!''}</td>
	<#else>			
		<td class="align_left_P10 adcIp none">${(theAdcList.adcIp)!''}</td>
	</#if> 			
	<#if montotalAdc.condition.configTime.isSelect()>
		<td class="align_center textOver configTime" title="${(theAdcList.configTime?string("yyyy-MM-dd HH:mm:ss"))!}"> 
	<#else>			
		<td class="align_center textOver configTime none" title="${(theAdcList.configTime?string("yyyy-MM-dd HH:mm:ss"))!}">
	</#if>
			<#if (theAdcList.configTime)??>
				${(theAdcList.configTime?string("yyyy-MM-dd HH:mm:ss"))!}
			<#else>
				-
			</#if>
		</td>    
	<#if montotalAdc.condition.cpu.isSelect()>
		<td class="align_center cpu" title="Alteon은 SP 평균">
	<#else>	
		<td class="align_center cpu none" title="Alteon은 SP 평균">
	</#if>    
		<#if (theAdcList.cpu)??>
			<#if (theAdcList.cpu) == -1>
				-
			<#else>
				${(theAdcList.cpu)!''}
			</#if>
		</#if>
		</td>   
	<#if montotalAdc.condition.memory.isSelect()>
		<td class="align_center memory">
	<#else>	
		<td class="align_center memory none">
	</#if>    
			<#if (theAdcList.memory) == -1>
				-
			<#else>
				${(theAdcList.memory)!''}
			</#if>
		</td>            
	<#if montotalAdc.condition.errorPackets.isSelect()>
		<td class="align_center errorPackets">${theAdcList.errorPackets?string(",##0.00")}</td>
	<#else>			
		<td class="align_center errorPackets none">${theAdcList.errorPackets?string(",##0.00")}</td>
	</#if> 
	<#if montotalAdc.condition.dropPackets.isSelect()>
		<td class="align_center dropPackets">${theAdcList.dropPackets?string(",##0.00")}</td> 
	<#else>			
		<td class="align_center dropPackets none">${theAdcList.dropPackets?string(",##0.00")}</td> 
	</#if>     
	<#if montotalAdc.condition.sslCertValidDays.isSelect()>
		<td class="align_center sslCertValidDays">
	<#else>			
		<td class="align_center sslCertValidDays none">
	</#if>
			<#if (theAdcList.adcType) == 1>
				${(theAdcList.sslCertValidDays)!''}
			<#else>
				-
			</#if>
		</td> 
	<#if montotalAdc.condition.interfaceCount.isSelect()>
		<td class="align_center interfaceAvailable">
	<#else>			
		<td class="align_center interfaceAvailable none">		 
	</#if> 
			<#if (theAdcList.interfaceAvailable) == -1>
				-
			<#else>
				${(theAdcList.interfaceAvailable)!''}
			</#if>
		</td>
	<#if montotalAdc.condition.filter.isSelect()>
		<td class="align_center filterUse flbInfoLnk">
	<#else>			
		<td class="align_center filterUse flbInfoLnk none"> 
	</#if>  
			<#if (theAdcList.filterUse) == -1>
				-
			<#else>
				${(theAdcList.filterUse)!''}
			</#if>			
		</td>
	<#if montotalAdc.condition.service.isSelect()>
		<td class="align_center serviceAvailable">
	<#else>			
		<td class="align_center serviceAvailable none"> 
	</#if>
			<#if (theAdcList.serviceAvailable) == -1>
				-
			<#else>
				${(theAdcList.serviceAvailable)!''}
			</#if>	
	</td>
	<#if montotalAdc.condition.adcLog24Hour.isSelect()>
		<td class="align_center adcLog24Hour">
	<#else>			
		<td class="align_center adcLog24Hour none">
	</#if> 
			<a href="javascript:;" class="adcLogLnk"><span style="display:none;">${(theAdcList.adcIndex)!''}</span>
				${theAdcList.adcLog24Hour?string(",##0.00")}
			</a>
		</td> 
	<#if montotalAdc.condition.slbConfig24Hour.isSelect()>
		<td class="align_center slbConfig24Hour"></td> 
	<#else>			
		<td class="align_center slbConfig24Hour none"></td> 
	</#if>  		 
	</tr>
	</#list>	
</tbody>