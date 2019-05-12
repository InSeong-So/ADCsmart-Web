<div class="Board">
		<div class="position_R10B5">
			<!-- <input type="radio" name="searchTimeMode" id="hoursMode" checked="checked" value="${LANGCODEMAP["MSG_CONSTANT_HOURS_MODE"]!}" />
			<select class="formtext_wdth115" id="lastHours">
				<option value="1">${LANGCODEMAP["MSG_APPLIANCE_ONE_TIME"]!}</option>
				<option value="3">${LANGCODEMAP["MSG_APPLIANCE_THREE_TIME"]!}</option>
				<option value="6">${LANGCODEMAP["MSG_APPLIANCE_SIX_TIME"]!}</option>
				<option value="12">${LANGCODEMAP["MSG_APPLIANCE_TWELVE_TIME"]!}</option>
				<option value="24">${LANGCODEMAP["MSG_APPLIANCE_TWNTYFOUR_TIME"]!}</option>
			</select>&nbsp;
			 -->
			 <input type="text" class="inputText_calendar"  name="reservation" id="reservationtime"  value="" readonly="">
			 <input type="button" id="refresh" class="Btn_white" value="${LANGCODEMAP["MSG_APPLIANCE_REFRESH"]!}"/>  					
 		</div>
    <br class="clearfix" />		

<!--  1	 -->
		<div class="device1">
			<div class="info">
				<div class="title">${LANGCODEMAP["MSG_APPLIANCE_SESSION_COUNT"]!}</div>
				<div class="number" id="SessionCurr">
					- <span class="unit">${LANGCODEMAP["MSG_APPLIANCE_COUNT"]!}</span>
				</div>
				<div class="average" id="SessionPrev">${LANGCODEMAP["MSG_APPLIANCE_PRE_AVG"]!} -${LANGCODEMAP["MSG_APPLIANCE_COUNT"]!}</div>
			</div>
			<div id="SessionHistoryChart" class="deviceChart" style="width: 100%; height: 130px;"></div>
		</div>		
		<div class="device2">			
			<div class="info">
				<div class="title">${LANGCODEMAP["MSG_APPLIANCE_ADC_TRAFFIC"]!}</div>
				<div class="number" id="BpsCurr">
					- <span class="unit">bps</span>
				</div>
				<div class="average" id="BpsPrev">${LANGCODEMAP["MSG_APPLIANCE_PRE_AVG"]!} -bps</div>
			</div>			
			<div id="BpsHistoryChart"  class="deviceChart"  style="width: 100%; height: 130px;"></div>			
		</div>
<!--  2	 -->		
		<div class="device1 f5Area_Content" style="display: none;">
			<div class="info">
					<div class="title">${LANGCODEMAP["MSG_APPLIANCE_SSLTANSACT"]!}</div>
					<div class="number" id="SSLCurr">
						- <span class="unit">tps</span>
					</div>
					<div class="average" id="SSLPrev">${LANGCODEMAP["MSG_APPLIANCE_PRE_AVG"]!} -tps</div>
			</div>	
			<div id="SSLTransactionChart" class="deviceChart"  style="width: 100%; height: 130px;"></div>				
		</div>	
		<div class="device2 f5Area_Content" style="display: none;">			
			<div class="info">
					<div class="title">${LANGCODEMAP["MSG_APPLIANCE_HTTPREQUEST"]!}</div>
					<div class="number" id="HTTPCurr">
						- <span class="unit">rps</span>
					</div>
					<div class="average" id="HTTPPrev">${LANGCODEMAP["MSG_APPLIANCE_PRE_AVG"]!} -rps</div>
			</div>			
			<div id="HTTPRequestChart"  class="deviceChart" style="width: 100%; height: 130px;"></div>			
		</div>		
<!--  3	 -->		
		<div class="device1">
			<div class="info">
				<div class="title">${LANGCODEMAP["MSG_APPLIANCE_CPU"]!}</div>
				<div class="number" id="CpuCurr">
					- <span class="unit">%</span>
				</div>
				<div class="average" id="CpuPrev">${LANGCODEMAP["MSG_APPLIANCE_PRE_AVG"]!} -${LANGCODEMAP["MSG_APPLIANCE_COUNT"]!}</div>
			</div>	
			<div id="CpuHistroyChart" class="deviceChart"  style="width: 100%; height: 130px;"></div>				
		</div>	
		<div class="device2">			
			<div class="info">
				<div class="title">${LANGCODEMAP["MSG_APPLIANCE_MEMORY"]!}</div>
				<div class="number" id="MemCurr">
					- <span class="unit">%</span>
				</div>
				<div class="average" id="MemPrev">${LANGCODEMAP["MSG_APPLIANCE_PRE_AVG"]!} -${LANGCODEMAP["MSG_APPLIANCE_COUNT"]!}</div>
			</div>			
			<div id="MemHistoryChart"  class="deviceChart"  style="width: 100%; height: 130px;"></div>			
		</div>			
<!--  4	 -->		
		<div class="device1">
			<div class="info">
				<div class="title">${LANGCODEMAP["MSG_APPLIANCE_ERROR_COUNT"]!}</div>
				<div class="number" id="PktECurr">
					- <span class="unit">${LANGCODEMAP["MSG_APPLIANCE_COUNT"]!}</span>
				</div>
				<div class="average" id="PktEPrev">${LANGCODEMAP["MSG_APPLIANCE_PRE_AVG"]!} -${LANGCODEMAP["MSG_APPLIANCE_COUNT"]!}</div>
			</div>	
			<div id="PktErrHistoryChart"  class="deviceChart" style="width: 100%; height: 130px;"></div>				
		</div>	
		<div class="device2" >			
			<div class="info">
				<div class="title">${LANGCODEMAP["MSG_APPLIANCE_LOSS_COUNT"]!}</div>
				<div class="number" id="PktDCurr">
					- <span class="unit">${LANGCODEMAP["MSG_APPLIANCE_COUNT"]!}</span>
				</div>
				<div class="average" id="PktDPrev">${LANGCODEMAP["MSG_APPLIANCE_PRE_AVG"]!} -${LANGCODEMAP["MSG_APPLIANCE_COUNT"]!}</div>
			</div>			
			<div id="PktDropHistoryChart"  class="deviceChart" style="width: 100%; height: 130px;"></div>			
		</div>					
</div>