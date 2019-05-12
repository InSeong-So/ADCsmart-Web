<#setting number_format="0.####">
<#if status??>
	<#if 10 == status><#assign statusName = "전체"></#if>
	<#if 11 == status><#assign statusName = "정상"></#if>
	<#if 12 == status><#assign statusName = "단절"><#assign statusClassName="font_yellow2"></#if>
	<#if 13 == status><#assign statusName = "단절 (7일 이상)"></#if>
	<#if 14 == status><#assign statusName = "꺼짐"></#if>
</#if>
<#if orderType??>
	<#if  2 == orderType></#if><!-- vsIpaddress -->
	<#if 18 == orderType></#if><!-- servicePort -->
	<#if 15 == orderType></#if><!-- status -->
	<#if 11 == orderType></#if><!-- occurTime -->
	<#if  8 == orderType></#if><!-- adcName -->
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
	<li class="title_h5">Virtual Server 요약</li>
	<span class="title_h2">전체 <span class="txt_blue">${vsSummaryCount.vsCount}</span> 건</span>

  	<ul class="tabs">
    	<li>
    		<a class="vsSummary css_textCursor">
    			<span class="none status">10</span>전체
    		</a>
    	</li>
    	<li>
    		<a class="vsSummary css_textCursor">
    			<span class="none status">11</span>정상
    		</a>
    	</li>
    	<li>
    		<a class="vsSummary css_textCursor">
    			<span class="none status">12</span>단절
    		</a>
    	</li>
    	<li>
    		<a class="vsSummary css_textCursor">
    			<span class="none status">14</span>꺼짐
    		</a>
    	</li>
	</ul>
	<br class="clearfix" />
	<div class="fixed-table-container">
    	<div class="header-background">
      	<table class="Board" cellspacing="0" style="table-layout:fixed;">
	        <colgroup>
				<col width="14%"/>
				<col width="5%"/>
				<col width="4%"/>
				<col width="13%"/>
				<col width="21%"/>				
				<col width="14%"/>
				<col width="11%"/>
				<col width="9%"/>
				<col width="9%"/>
				<!--				
				<col width="50px"/>					
	            <col width="80px"/>  
				-->       	                
			</colgroup>
         		<tr>
         			<input class ="none" value="2" name="orderDir_Desc"/>
         			<input class ="none" value="1" name="orderDir_Asc"/>	
		        	 	<th height="36" style="border-left:none">
						<span class="css_textCursor">
						<#if orderDir == 2 && orderType == 2>		
							<a class="orderDir_Desc">VS IP
								<img src="/imgs/Desc.gif"/>
								<span class="none orderType">2</span>
								<span class="none orderDir">1</span>					
							</a>						
						<#elseif orderDir == 1 && orderType == 2>	
							<a class="orderDir_Asc">VS IP
								<img src="/imgs/Asc.gif"/>
								<span class="none orderType">2</span>
								<span class="none orderDir">2</span>		
							</a>
						<#else>
							<a class="orderDir_None">VS IP
								<img src="/imgs/none.gif"/>
								<span class="none orderType">2</span>
								<span class="none orderDir">2</span>		
							</a>
						</#if>
						</span>			        			        			        			        			        	
		        	</th>
		           	<th>
						<span class="css_textCursor">
						<#if orderDir == 2 && orderType == 18>		
							<a class="orderDir_Desc">포트
								<img src="/imgs/Desc.gif"/>
								<span class="none orderType">18</span>
								<span class="none orderDir">1</span>					
							</a>						
							<#elseif orderDir == 1 && orderType == 18>	
							<a class="orderDir_Asc">포트
								<img src="/imgs/Asc.gif"/>
								<span class="none orderType">18</span>
								<span class="none orderDir">2</span>		
							</a>
						<#else>
							<a class="orderDir_None">포트
								<img src="/imgs/none.gif"/>
								<span class="none orderType">18</span>
								<span class="none orderDir">2</span>		
							</a>
						</#if>
						</span>			           	
		           	</th>
		           	<th>
						<span class="css_textCursor">
						<#if orderDir == 2 && orderType == 15>		
							<a class="orderDir_Desc">상태
								<img src="/imgs/Desc.gif"/>
								<span class="none orderType">15</span>
								<span class="none orderDir">1</span>					
							</a>						
						<#elseif orderDir == 1 && orderType == 15>	
							<a class="orderDir_Asc">상태
								<img src="/imgs/Asc.gif"/>
								<span class="none orderType">15</span>
								<span class="none orderDir">2</span>		
							</a>
						<#else>
							<a class="orderDir_None">상태
								<img src="/imgs/none.gif"/>
								<span class="none orderType">15</span>
								<span class="none orderDir">2</span>		
							</a>
						</#if>
						</span>			           	
		           	</th>
		           	<th>
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
		           	<th>
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
		           	<th>
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
		           	<th>
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
		           	<th>
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
		           	<th>
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
					<!--		           	
					<th>
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
					-->
					<!--		           		
					<th>
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
					-->
	          	</tr>
        	</table>
      		</div>
		<!--      	
		<div class="fixed-table-container-inner">
		-->
        	<table class="Board" cellspacing="0" style="table-layout:fixed;">
	        <colgroup>
				<col width="14%"/>
				<col width="5%"/>
				<col width="4%"/>
				<col width="13%"/>
				<col width="21%"/>				
				<col width="14%"/>
				<col width="11%"/>
				<col width="9%"/>
				<col width="9%"/>
				<!--				
				<col width="50px"/>					
	            <col width="80px"/>  
				-->       	                
			</colgroup>
        		<tbody>
        		<#if vsSummaryList??>
				<#list vsSummaryList as vsSummary>
					<#if 0 == vsSummary.status>
						<#assign imageFileName = "/imgs/icon/icon_vs_disabled.png">
						<#assign statusText = "Disabled">
					</#if>
					<#if 1 == vsSummary.status>
						<#assign imageFileName = "/imgs/icon/icon_vs_conn.png">
						<#assign statusText = "정상">
					</#if>
					<#if 2 == vsSummary.status>
						<#assign imageFileName = "/imgs/icon/icon_vs_disconn.png">
						<#assign statusText = "단절">
					</#if>
					<#if 3 == vsSummary.status>
						<#assign imageFileName = "/imgs/icon/icon_yellowDot.png">
						<#assign statusText = "비정상">
					</#if>
					
            		<tr class="vsSummaryListRow ContentsLine1" >              	
		              	<td class="text-align-left10 textOver">
							<span class="none vsType">${vsSummary.vsType}</span>
							<span class="none vsIndex">${vsSummary.index}</span>
							
							<#if 1 == vsSummary.vsType>≫</#if>
							${vsSummary.ip!""}
						</td>
						<td><span class="none vsPort">${vsSummary.port}</span>${vsSummary.port!}</td>
						<td class="textOver"><img src="${imageFileName}" alt="${statusText}"/></td>
						<td class="text-align-center textOver">
							<#if (vsSummary.updateTime)??>
								${vsSummary.updateTime!""}
							<#else>
								-
							</#if>
						</td>
						<td class="text-align-left10 textOver" title="${vsSummary.adcName!""}">
							<span class="none adcIndex">${vsSummary.adcIndex}</span>
							${vsSummary.adcName!""}
							<!--
							<#if vsSummary.adcType??>
								<#if 1 == vsSummary.adcType>
									<span class="none adcType">F5</span>
									<img src="/imgs/popup/icon_f5_s.png" alt="f5" class="adc_name" />
								</#if>
								<#if 2 == vsSummary.adcType>
									<span class="none adcType">Alteon</span>
									<img src="/imgs/popup/icon_alteon_s.png" alt="alteon" class="adc_name" />
								</#if>
								<#if 3 == vsSummary.adcType>
									<span class="none adcType">PAS</span>
									<img src="/imgs/popup/icon_pas_s.png" alt="pas" class="adc_name" />
								</#if>
								<#if 4 == vsSummary.adcType>
									<span class="none adcType">PASK</span>
									<img src="/imgs/popup/icon_pas_s.png" alt="pask" class="adc_name" />
								</#if>
							</#if>
							-->
						</td>
						<td class="text-align-left10 textOver" title="${vsSummary.model!""}">
							<#if (vsSummary.model)??>							 
								<#if (vsSummary.adcType)??>
									<#if 1 == vsSummary.adcType>
										<span class="none adcType">F5</span>
										<img src="/imgs/popup/icon_f5_s.png" alt="f5" class="adc_name" />
									</#if>
									<#if 2 == vsSummary.adcType>
										<span class="none adcType">Alteon</span>
										<img src="/imgs/popup/icon_alteon_s.png" alt="alteon" class="adc_name" />
									</#if>
									<#if 3 == vsSummary.adcType>
										<span class="none adcType">PAS</span>
										<img src="/imgs/popup/icon_pas_s.png" alt="pas" class="adc_name" />
									</#if>
									<#if 4 == vsSummary.adcType>
										<span class="none adcType">PASK</span>
										<img src="/imgs/popup/icon_pas_s.png" alt="pask" class="adc_name" />
									</#if>
								</#if>
								${vsSummary.model!""}
							<#else>
								<#if 1 == vsSummary.adcType>
									<span class="none adcType">F5</span>									
								</#if>
								<#if 2 == vsSummary.adcType>
									<span class="none adcType">Alteon</span>
								</#if>
								<#if 3 == vsSummary.adcType>
									<span class="none adcType">PAS</span>
								</#if>
								<#if 4 == vsSummary.adcType>
									<span class="none adcType">PASK</span>
								</#if>
								- 
							</#if>
						</td>
						<td>
							<#if (vsSummary.os)??>
								${vsSummary.os!""}
							<#else>
								-
							</#if>
						</td>
						<td>${vsSummary.connection!""}</td>
						<td>${vsSummary.throughput!""}</td>						
						<!--						
						<td>${vsSummary.cpuUsage!0}%</td>
						<td>${vsSummary.memoryUsage!0}%</td>
						-->
		            </tr>		            
				</#list>
				</#if>				          
		       	</tbody>
			</table>
			<!--		
			</div>
			-->
	</div> 
</div>
</div>

