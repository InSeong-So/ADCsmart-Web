<table class="summary" cellspacing="0">
	<colgroup>
		<col/>
		<col width="1280px" />
		<col />
	</colgroup>
	<tr>
		<td class="td2"></td>
		<td class="td2" style="text-align:center">
		<div class="adcsum_area">
		  <ul>
			<li>
				<p class="status_tlt">
					<span class="none statusGroup">0</span>	
					<label class="adcsum_txt">ADC 요약</label>		
				</p>
			  </li>
					<ul>
						<li>
							<p class="server_cnt css_textCursor">
								<span class="none status">0</span>					
								<span class="adcsum_all_area" id="adcsum_all">${(statusSummary.adcTotalCount)!0}</span>	
							</p>
						</li>
		
						<li>								
							<p class="server_cnt css_textCursor">
								<span class="none status">1</span>					
								<span class="adcsum_1_area" id="adcsum_1">${(statusSummary.adcAvailableCount)!0}</span>
							</p>
						</li>
						<li>
							<p class="server_cnt css_textCursor">
								<span class="none status">2</span>				
								<span class="adcsum_0_area" id="adcsum_0">${(statusSummary.adcUnavailableCount)!0}</span>
							</p>
						</li>
					
					</ul>
				</li>
			</ul>	
		</div>
		<div class="faultsum_area">
			<ul>
				<li>
					<p class="status_tlt">
						<span class="none statusGroup">2</span>				
						<label class="faultsum_txt">장애 모니터링 통계</label>
					</p>
				</li>
				<p class="faultsum_select">
					<select name="faultMaxDays">
				    	<option value="0">전체</option>
				    	<option value="1">최근1일</option>
				    	<option value="7">최근7일</option>
				    	<option value="15">최근15일</option>
				    	<option value="30">최근30일</option>
			      	</select>
			  	</p>
				<ul>
					<li>
				  		<p class="server_cnt css_textCursor">
			        		<span class="none status">23</span>					
			        		<span class="faultsum_1_area" id="faultsum_1">${(statusSummary.faultWarning)!0}</span>
		          		</p>
			      	</li>
				  	<li>
				    	<p class="server_cnt css_textCursor">
				      		<span class="none status">21</span>						
				      		<span class="faultsum_all_area" id="faultsum_all">${(statusSummary.faultSolvedCount)!0}</span>
			        	</p>
			      	</li>
				  	<li>
				    	<p class="server_cnt css_textCursor">
				      		<span class="none status">22</span>					
				      		<span class="faultsum_0_area" id="faultsum_0">${(statusSummary.faultUnsolvedCount)!0}</span>
			        	</p>
			      	</li>				
			  	</ul>
			</ul>	
		</div>
		<div class="vssum_area">
			<ul>
				<li>
					<p class="status_tlt">
						<span class="none statusGroup">1</span>		
						<label class="vssum_txt">Virtual Server 통계</label>		
					</p>
					<ul>
						<li>
					    	<p class="server_cnt css_textCursor">
					      		<span class="none status">14</span>						
					      		<span class="vssum_2_area" id="vssum_2">${(statusSummary.vsDisableCount)!0}</span>
				        	</p>
				      	</li>				
					  	<li>								
					    	<p class="server_cnt css_textCursor">
					      		<span class="none status">10</span>						
					      		<span class="vssum_all_area" id="vssum_all">${(statusSummary.vsTotalCount)!0}</span>
				        	</p>
				      	</li>
					  	<li>
					    	<p class="server_cnt css_textCursor">
					      		<span class="none status">11</span>						
					      		<span class="vssum_1_area" id="vssum_1">${(statusSummary.vsAvailableCount)!0}</span>
				        	</p>
				      	</li>
					  	<li>
					    	<p class="server_cnt css_textCursor">
					      		<span class="none status">12</span>						
					      		<span class="vssum_0_area" id="vssum_0">${(statusSummary.vsUnavailableCount)!0}</span>
				        	</p>
				      	</li>			
				  	</ul>
				</li>
			</ul>	
		</div>
 		</td>
		<td class="td2"></td>
	</tr>
</table>
