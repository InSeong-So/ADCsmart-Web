<#setting number_format="0.####">
<#if status??>
	<#if 0 == status><#assign statusName = "전체"></#if>
	<#if 1 == status><#assign statusName = "정상"></#if>
	<#if 2 == status><#assign statusName = "단절"><#assign statusClassName="font_red"></#if>
</#if>
<#if status??>
	<#if 0 == status><#assign statusName = "전체"></#if>
	<#if 1 == status><#assign statusName = "정상"></#if>
	<#if 2 == status><#assign statusName = "단절"><#assign statusClassName="font_red"></#if>
</#if>
<#if orderType??>
	<#if  8 == orderType></#if><!-- adcName -->
	<#if  9 == orderType></#if><!-- adcIpaddress -->
	<#if 10 == orderType></#if><!-- adcStatus -->
	<#if 11 == orderType></#if><!-- occurTime -->
	<#if 22 == orderType></#if><!-- productName -->
	<#if 23 == orderType></#if><!-- version -->
	<#if  6 == orderType></#if><!-- cps -->
	<#if  4 == orderType></#if><!-- bps -->
	<#if 24 == orderType></#if><!-- cpuUsage -->
	<#if 25 == orderType></#if><!-- memUsage -->
</#if>
<#if orderDir??>
	<#if 1 == orderDir></#if><!-- Asc -->
	<#if 2 == orderDir></#if><!-- Desc -->
</#if>	
<div class="content_wrap">
	    <li class="title_h5">ADC 모니터링 요약</li>
		<span class="title_h2">전체 <span class="txt_blue">${(adcSummaryList![])?size}</span> 건</span></span>	
	<ul class="tabs">
  		<li>
  			<a class="adcSummary css_textCursor" id="all">
  				<span class="none status">0</span>전체
  			</a>
  		</li>							
   		<li>
   			<a class="adcSummary css_textCursor" id="available">
   				<span class="none status">1</span>정상
   			</a>
   		</li>
   		<li>
   			<a class="adcSummary css_textCursor" id="unAvailable">
   				<span class="none status">2</span>단절
   			</a>
   		</li>
	</ul>
	<br class="clearfix" />		
	<div class="fixed-table-container">
    	<div class="header-background">
        	<table cellspacing="0">
	         	<colgroup>
					<col width="11%"/>
					<col width="10%"/>
					<col width="4%"/>
					<col width="9%"/>
					<col width="5%"/>
					<col width="5%"/> 
					<col width="5%"/>
					<col width="5%"/>
					<col width="10%"/>
					<col width="9%"/>
					<col width="8%"/>
					<col width="8%"/>
					<col width="5%"/>					
					<col width="6%"/>
				</colgroup>
         		<tr>
         			<input class ="none" value="2" name="orderDir_Desc"/>
					<input class ="none" value="1" name="orderDir_Asc"/>
           			<th rowspan="2" style="border-left:none">
						<span class="css_textCursor">
						<#if orderDir == 2 && orderType == 8>		
							<a class="orderDir_Desc">ADC이름
								<img src="/imgs/Desc.gif"/>
								<span class="none orderType">8</span>
								<span class="none orderDir">1</span>					
							</a>						
						<#elseif orderDir == 1 && orderType == 8>	
							<a class="orderDir_Asc">ADC이름
								<img src="/imgs/Asc.gif"/>
								<span class="none orderType">8</span>
								<span class="none orderDir">2</span>		
							</a>
						<#else>
							<a class="orderDir_None">ADC이름
								<img src="/imgs/none.gif"/>
								<span class="none orderType">8</span>
								<span class="none orderDir">2</span>		
							</a>
						</#if>
						</span>	          			
           			</th>
		           	<th rowspan="2">
						<span class="css_textCursor">
						<#if orderDir == 2 && orderType == 9>		
							<a class="orderDir_Desc">IP
								<img src="/imgs/Desc.gif"/>
								<span class="none orderType">9</span>
								<span class="none orderDir">1</span>					
							</a>						
						<#elseif orderDir == 1 && orderType == 9>	
							<a class="orderDir_Asc">IP
								<img src="/imgs/Asc.gif"/>
								<span class="none orderType">9</span>
								<span class="none orderDir">2</span>		
							</a>
						<#else>
							<a class="orderDir_None">IP
								<img src="/imgs/none.gif"/>
								<span class="none orderType">9</span>
								<span class="none orderDir">2</span>		
							</a>
						</#if>
						</span>			           	
		           	</th>
		           	<th rowspan="2">
						<#if orderDir == 2 && orderType == 10>		
							<a class="orderDir_Desc">상태
								<img src="/imgs/Desc.gif"/>
								<span class="none orderType">10</span>
								<span class="none orderDir">1</span>					
							</a>						
						<#elseif orderDir == 1 && orderType == 10>	
							<a class="orderDir_Asc">상태
								<img src="/imgs/Asc.gif"/>
								<span class="none orderType">10</span>
								<span class="none orderDir">2</span>		
							</a>
						<#else>
							<a class="orderDir_None">상태
								<img src="/imgs/none.gif"/>
								<span class="none orderType">10</span>
								<span class="none orderDir">2</span>		
							</a>
						</#if>
						</span>			           	
		           	</th>
		           	<th rowspan="2">
						<span class="css_textCursor">
						<#if orderDir == 2 && orderType == 11>		
							<a class="orderDir_Desc">업데이트시간
								<img src="/imgs/Desc.gif"/>
								<span class="none orderType">11</span>
								<span class="none orderDir">1</span>					
							</a>						
						<#elseif orderDir == 1 && orderType == 11>	
							<a class="orderDir_Asc">업데이트시간
								<img src="/imgs/Asc.gif"/>
								<span class="none orderType">11</span>
								<span class="none orderDir">2</span>		
							</a>
						<#else>
							<a class="orderDir_None">업데이트시간
								<img src="/imgs/none.gif"/>
								<span class="none orderType">11</span>
								<span class="none orderDir">2</span>		
							</a>
						</#if>
						</span>			           	
		           	</th>
		           	<th colspan="4">VS개수</th>
		           	<th rowspan="2">
						<span class="css_textCursor">
						<#if orderDir == 2 && orderType == 22>		
							<a class="orderDir_Desc">제품이름
								<img src="/imgs/Desc.gif"/>
								<span class="none orderType">22</span>
								<span class="none orderDir">1</span>					
							</a>						
						<#elseif orderDir == 1 && orderType == 22>	
							<a class="orderDir_Asc">제품이름
								<img src="/imgs/Asc.gif"/>
								<span class="none orderType">22</span>
								<span class="none orderDir">2</span>		
							</a>
						<#else>
							<a class="orderDir_None">제품이름
								<img src="/imgs/none.gif"/>
								<span class="none orderType">22</span>
								<span class="none orderDir">2</span>		
							</a>
						</#if>
						</span>			           	
		           	</th>
		           	<th rowspan="2">
						<span class="css_textCursor">
						<#if orderDir == 2 && orderType == 23>		
							<a class="orderDir_Desc">버전
								<img src="/imgs/Desc.gif"/>
								<span class="none orderType">23</span>
								<span class="none orderDir">1</span>					
							</a>						
						<#elseif orderDir == 1 && orderType == 23>	
							<a class="orderDir_Asc">버전
								<img src="/imgs/Asc.gif"/>
								<span class="none orderType">23</span>
								<span class="none orderDir">2</span>		
							</a>
						<#else>
							<a class="orderDir_None">버전
								<img src="/imgs/none.gif"/>
								<span class="none orderType">23</span>
								<span class="none orderDir">2</span>		
							</a>
						</#if>
						</span>			           	
		           	</th>
		           	<th rowspan="2">
						<span class="css_textCursor">
						<#if orderDir == 2 && orderType == 6>		
							<a class="orderDir_Desc">Connection
								<img src="/imgs/Desc.gif"/>
								<span class="none orderType">6</span>
								<span class="none orderDir">1</span>					
							</a>						
						<#elseif orderDir == 1 && orderType == 6>	
							<a class="orderDir_Asc">Connection
								<img src="/imgs/Asc.gif"/>
								<span class="none orderType">6</span>
								<span class="none orderDir">2</span>		
							</a>
						<#else>
							<a class="orderDir_None">Connection
								<img src="/imgs/none.gif"/>
								<span class="none orderType">6</span>
								<span class="none orderDir">2</span>		
							</a>
						</#if>
						</span>			           	
		           	</th>
		           	<th rowspan="2">
						<span class="css_textCursor">
						<#if orderDir == 2 && orderType == 4>		
							<a class="orderDir_Desc">Throughput
								<img src="/imgs/Desc.gif"/>
								<span class="none orderType">4</span>
								<span class="none orderDir">1</span>					
							</a>						
						<#elseif orderDir == 1 && orderType == 4>	
							<a class="orderDir_Asc">Throughput
								<img src="/imgs/Asc.gif"/>
								<span class="none orderType">4</span>
								<span class="none orderDir">2</span>		
							</a>
						<#else>
							<a class="orderDir_None">Throughput
								<img src="/imgs/none.gif"/>
								<span class="none orderType">4</span>
								<span class="none orderDir">2</span>		
							</a>
						</#if>
						</span>			           
		           	</th>
		           	<th rowspan="2">
						<span class="css_textCursor">
						<#if orderDir == 2 && orderType == 24>		
							<a class="orderDir_Desc">CPU
								<img src="/imgs/Desc.gif"/>
								<span class="none orderType">24</span>
								<span class="none orderDir">1</span>					
							</a>						
						<#elseif orderDir == 1 && orderType == 24>	
							<a class="orderDir_Asc">CPU
								<img src="/imgs/Asc.gif"/>
								<span class="none orderType">24</span>
								<span class="none orderDir">2</span>		
							</a>
						<#else>
							<a class="orderDir_None">CPU
								<img src="/imgs/none.gif"/>
								<span class="none orderType">24</span>
								<span class="none orderDir">2</span>		
							</a>
						</#if>
						</span>			           	
		           	</th>
		           	<th rowspan="2">
						<span class="css_textCursor">
						<#if orderDir == 2 && orderType == 25>		
							<a class="orderDir_Desc">Memory
								<img src="/imgs/Desc.gif"/>
								<span class="none orderType">25</span>
								<span class="none orderDir">1</span>					
							</a>						
						<#elseif orderDir == 1 && orderType == 25>	
							<a class="orderDir_Asc">Memory
								<img src="/imgs/Asc.gif"/>
								<span class="none orderType">25</span>
								<span class="none orderDir">2</span>		
							</a>
						<#else>
							<a class="orderDir_None">Memory
								<img src="/imgs/none.gif"/>
								<span class="none orderType">25</span>
								<span class="none orderDir">2</span>		
							</a>
						</#if>
						</span>			           	
		           	</th>
          		</tr>
          		<tr>
	            	<th>전체</th>
	              	<th>정상</th>
	              	<th>단절</th>
	              	<th>꺼짐</th>
	            </tr>
        	</table>
      	</div>
<!--      	<div class="fixed-table-container-inner">-->
      	<table class="Board" cellspacing="0" style="table-layout:fixed;">
	         	<colgroup>
					<col width="11%"/>
					<col width="10%"/>
					<col width="4%"/>
					<col width="9%"/>
					<col width="5%"/>
					<col width="5%"/> 
					<col width="5%"/>
					<col width="5%"/>
					<col width="10%"/>
					<col width="9%"/>
					<col width="8%"/>
					<col width="8%"/>
					<col width="5%"/>					
					<col width="6%"/>
				</colgroup>
        	<tbody>
        	 	<#if adcSummaryList??>
				<#list adcSummaryList as adcSummary>
					<#if 0 == adcSummary.status>
						<#assign imageFileName = "/imgs/icon/icon_1d_2.png">
						<#assign statusText = "Disabled">
					</#if>
					<#if 1 == adcSummary.status>
						<#assign imageFileName = "/imgs/icon/icon_1d_1.png">
						<#assign statusText = "정상">
					</#if>
					<#if 2 == adcSummary.status>
						<#assign imageFileName = "/imgs/icon/icon_1d_2.png">
						<#assign statusText = "단절">
					</#if>
					<#if 3 == adcSummary.status>
						<#assign imageFileName = "/imgs/icon/icon_1d_2.png">
						<#assign statusText = "비정상">
					</#if>
					
	        		<tr class="adcSummaryListRow ContentsLine1">
	              		<td class="text-align-left10 textOver" title="${adcSummary.name}">${adcSummary.name!""}
							<span class="none adcIndex">${adcSummary.index!}</span>
							<!--
							&nbsp;&nbsp;${adcSummary.name!""}
							<#if adcSummary.type??>
								<#if 1 == adcSummary.type>
									<img src="/imgs/icon/adc/icon_f5_s.png" alt="f5" class="adc_name" />
								</#if>
								<#if 2 == adcSummary.type>
									<img src="/imgs/icon/adc/icon_alteon_s.png" alt="alteon" class="adc_name" />
								</#if>
								<#if 3 == adcSummary.type>
									<img src="/imgs/icon/adc/icon_pas_s.png" alt="pas" class="adc_name" />
								</#if>
								<#if 4 == adcSummary.type>
									<img src="/imgs/icon/adc/icon_pas_s.png" alt="pask" class="adc_name" />
								</#if>
							</#if>
							-->
						</td>
	             		<td class="text-align-center textOver">${(adcSummary.ip)!""}</td>
	             		<td class="text-align-center"><img src="${imageFileName!""}" alt="${statusText}"/></td>
	              		<td class="text-align-center textOver">	              		
	              			<#if (adcSummary.updateTime)??>
	              				${adcSummary.updateTime!""}
	              			<#else>
	              				-
	              			</#if>
	              		</td>
	              		<td>${adcSummary.vsTotalCount!0}</td>
		              	<td>${adcSummary.vsAvailableCount!0}</td>
		              	<td>${adcSummary.vsUnavailableCount!0}</td>
		              	<td>${adcSummary.vsDisableCount!0}</td>
		              	<td class="text-align-left10 textOver" title="${adcSummary.model!}">
		              		<#if (adcSummary.model)??>
								<#if (adcSummary.type)??>
									<#if 1 == adcSummary.type>
										<img src="/imgs/popup/icon_f5_s.png" alt="f5" class="adc_name" />
									</#if>
									<#if 2 == adcSummary.type>
										<img src="/imgs/popup/icon_alteon_s.png" alt="alteon" class="adc_name" />
									</#if>
									<#if 3 == adcSummary.type>
										<img src="/imgs/popup/icon_pas_s.png" alt="pas" class="adc_name" />
									</#if>
									<#if 4 == adcSummary.type>
										<img src="/imgs/popup/icon_pas_s.png" alt="pask" class="adc_name" />
									</#if>
								</#if>
								${(adcSummary.model)!""}
							<#else>
								-
							</#if>
						</td>
		              	<td>
		              		<#if (adcSummary.os)??>
		              			${(adcSummary.os)!""}
		              		<#else>
		              			-
		              		</#if>
		              	</td>
	              		<td>${adcSummary.connection!0}</td>
	              		<td>${adcSummary.throughput!0}</td>
	              		<td>
	              			<#if ((adcSummary.cpuUsage) > 0)>
	              				${adcSummary.cpuUsage!0}%
	              			<#else>
	              				-
	              			</#if>
	              		</td>
						<td>
							<#if ((adcSummary.memoryUsage) > 0) >
	              				${adcSummary.memoryUsage!0}%
	              			<#else>
	              				-
	              			</#if>							
						</td>
	            	</tr>		           
	          	</#list>
				</#if>		
         	 </tbody>
        </table>
		</div>
	</div> 
</div>