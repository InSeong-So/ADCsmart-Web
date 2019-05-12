<#if status??>
	<#if 23 == status><#assign statusName = "경고"></#if>
	<#if 21 == status><#assign statusName = "해결"></#if>
	<#if 22 == status><#assign statusName = "미해결"><#assign statusClassName="font_red"></#if>
</#if>
<#if orderType??>
	<#if 11 == orderType></#if><!-- occurTime -->
	<#if  8 == orderType></#if><!-- adcName -->
	<#if 16 == orderType></#if><!-- type -->
	<#if 21 == orderType></#if><!-- severity -->
	<#if 24 == orderType></#if><!-- cpuUsage -->
	<#if 25 == orderType></#if><!-- memUsage -->
</#if>
<#if orderDir??>
	<#if 1 == orderDir></#if><!-- Asc -->
	<#if 2 == orderDir></#if><!-- Desc -->
</#if>	
<div class="content_wrap">
	<li class="title_h5">장애 모니터링 요약</li>
		<span class="title_h2">전체 <span class="txt_blue">${(faultLogList![])?size}</span> 건</span>
	
	<ul class="tabs">
		<li>
			<a class="faultLog css_textCursor">
				<span class="none status">21</span>해결
			</a>
		</li>
		<li>
			<a class="faultLog css_textCursor">
				<span class="none status">22</span>미해결
			</a>
		</li>
    	<li>
    		<a class="faultLog css_textCursor">
    			<span class="none status">23</span>경고
    		</a>
    	</li>
	</ul>
	<br class="clearfix" />
	<div class="fixed-table-container">
		<div class="header-background">
        	<table cellspacing="0">
        		<colgroup>
        		<#if status == 23>         			
					<col width="17%"/>
					<col width="15%"/>
					<col width="57%">
					<col width="7%"/ >
					<col width="4%"/ >					
<!--					
					<col width="50px"/>					
					<col width="60px"/>
-->
         		<#else>
					<col width="13%"/>
					<col width="24%"/>
					<col width="15%"/> 
					<col width="14%"/> 
					<col width="10%"/>
					<col width="13%"/>
					<col width="7%"/ >
					<col width="4%"/ >					
<!--					
					<col width="50px"/>					
					<col width="60px"/>
-->
                </#if>                          
		  		</colgroup>
         		<tr>
         			<input class ="none" value="2" name="orderDir_Desc"/>
         			<input class ="none" value="1" name="orderDir_Asc"/>
         			<#if status == 23>         			
					
         			<#else>
           			<th height="36" style="border-left:none">
						<span class="css_textCursor">
						<#if orderDir == 2 && orderType == 16>		
							<a class="orderDir_Desc">장애구분
								<img src="/imgs/Desc.gif"/>
								<span class="none orderType">16</span>
								<span class="none orderDir">1</span>					
							</a>						
						<#elseif orderDir == 1 && orderType == 16>	
							<a class="orderDir_Asc">장애구분
								<img src="/imgs/Asc.gif"/>
								<span class="none orderType">16</span>
								<span class="none orderDir">2</span>		
							</a>
						<#else>
							<a class="orderDir_None">장애구분
								<img src="/imgs/none.gif"/>
								<span class="none orderType">16</span>
								<span class="none orderDir">2</span>		
							</a>
						</#if>
						</span>	          			       			     			
           			</th>
           			</#if>
           			
					<#if orderDir == 2 && orderType == 8>	
					<th height="36" style="border-left:none">	
						<span class="css_textCursor">						
							<a class="orderDir_Desc">ADC이름
								<img src="/imgs/Desc.gif"/>
								<span class="none orderType">8</span>
								<span class="none orderDir">1</span>					
							</a>
						</span>								
						</th>						
					<#elseif orderDir == 1 && orderType == 8>	
	       			<th height="36" style="border-left:none" >	
						<span class="css_textCursor">										
						<a class="orderDir_Asc">ADC이름
							<img src="/imgs/Asc.gif"/>
							<span class="none orderType">8</span>
							<span class="none orderDir">2</span>		
						</a>
						</span>							
						</th>
					<#else>
       				<th height="36" >		
						<span class="css_textCursor">									
						<a class="orderDir_None">ADC이름
							<img src="/imgs/none.gif"/>
							<span class="none orderType">8</span>
							<span class="none orderDir">2</span>	
						</a>
						</span>	
					</th>
					</#if>
					</span>	             			
           			
           			<#if status == 23>	           					
         			<#else>
           			<th>VS IP</th>
           			<th>Member IP</th>
           			<th>인터페이스</th>
           			</#if>
					<th>
						<span class="css_textCursor">
						<#if orderDir == 2 && orderType == 11>		
							<a class="orderDir_Desc">시간
								<img src="/imgs/Desc.gif"/>
								<span class="none orderType">11</span>
								<span class="none orderDir">1</span>					
							</a>						
						<#elseif orderDir == 1 && orderType == 11>	
							<a class="orderDir_Asc">시간
								<img src="/imgs/Asc.gif"/>
								<span class="none orderType">11</span>
								<span class="none orderDir">2</span>		
							</a>
						<#else>
							<a class="orderDir_None">시간
								<img src="/imgs/none.gif"/>
								<span class="none orderType">11</span>
								<span class="none orderDir">2</span>		
							</a>
						</#if>
						</span>	  					
					</th>
					<#if status == 23>	
           			<th>내용</th>				
         			<#else>
         			</#if>
           			<th>
						<span class="css_textCursor">
						<#if orderDir == 2 && orderType == 21>		
							<a class="orderDir_Desc">위험도
								<img src="/imgs/Desc.gif"/>
								<span class="none orderType">21</span>
								<span class="none orderDir">1</span>					
							</a>						
						<#elseif orderDir == 1 && orderType == 21>	
							<a class="orderDir_Asc">위험도
								<img src="/imgs/Asc.gif"/>
								<span class="none orderType">21</span>
								<span class="none orderDir">2</span>		
							</a>
						<#else>
							<a class="orderDir_None">위험도
								<img src="/imgs/none.gif"/>
								<span class="none orderType">21</span>
								<span class="none orderDir">2</span>		
							</a>
						</#if>
						</span>	             			
           			</th>
           			<th>상태</th>
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
<!--		<div class="fixed-table-container-inner">-->
        	<table class="Board" cellspacing="0" style="table-layout: fixed;">
        		<colgroup>
        		<#if status == 23>         			
					<col width="17%"/>
					<col width="15%"/>
					<col width="57%">
					<col width="7%"/ >
					<col width="4%"/ >					
<!--					
					<col width="50px"/>					
					<col width="60px"/>
-->
         		<#else>
					<col width="13%"/>
					<col width="24%"/>
					<col width="15%"/> 
					<col width="14%"/> 
					<col width="10%"/>
					<col width="13%"/>
					<col width="7%"/ >
					<col width="4%"/ >					
<!--					
					<col width="50px"/>					
					<col width="60px"/>
-->
                </#if>                          
		  		</colgroup>       
				<tbody>
				<#if faultLogList??>
				<#list faultLogList as faultLog>
					<#assign typeName = "">
					<#if 1 == faultLog.type><#assign typeName = "ADC">
					<#elseif 2 == faultLog.type><#assign typeName = "Virtual Server">
					<#elseif 3 == faultLog.type><#assign typeName = "Member">
					<#elseif 4 == faultLog.type><#assign typeName = "Interface">
					<!-- BOOT / ACTIVE TO STANDBY / STANDBY TO ACTIVE -->
					<#elseif 5 == faultLog.type><#assign typeName = "BOOT">
					<#elseif 6 == faultLog.type><#assign typeName = "ACTIVE TO STANDBY">
					<#elseif 7 == faultLog.type><#assign typeName = "STANDBY TO ACTIVE">
					<#else><#assign typeName = "etc"></#if>
            		<tr class="faultLogListRow ContentsLine1">
            			<#if status == 23>	
            			<td class="none text-align-center">
              				<span class="none faultType">${faultLog.type!0}</span>
              				<span class="none font_red typeNm">${typeName}</span>
              			</td>	
            			<#else>              			
              			<td class="text-align-center">
              				<span class="none faultType">${faultLog.type!0}</span>
              				<span class="font_red typeNm">${typeName}</span>
              			</td>
              			</#if>
						<td class="text-align-left10 textOver" title="${faultLog.adcName!""}">
							<span class="none adcIndex">${faultLog.adcIndex}</span>
							<span class="font_red">${faultLog.adcName!""}</span>							
							<#if faultLog.adcType??>
								<#if 1 == faultLog.adcType>
									<span class="none adcType">F5</span>
									<!--<img src="/imgs/popup/icon_f5_s.png" alt="f5" class="adc_name" />-->
								</#if>
								<#if 2 == faultLog.adcType>
									<span class="none adcType">Alteon</span>
									<!--<img src="/imgs/popup/icon_alteon_s.png" alt="alteon" class="adc_name" />-->
								</#if>
								<#if 3 == faultLog.adcType>
									<span class="none adcType">PAS</span>
									<!--<img src="/imgs/popup/icon_pas_s.png" alt="pas" class="adc_name" />-->
								</#if>
								<#if 4 == faultLog.adcType>
									<span class="none adcType">PASK</span>
									<!--<img src="/imgs/popup/icon_pas_s.png" alt="pask" class="adc_name" />-->
								</#if>
							</#if>						
						</td>
						<#if status == 23>						
						<#else>
						<td class="text-align-center">
							<span class="none vsIndex">${faultLog.vsIndex!""}</span>
							<span class="none vsPort">${faultLog.vsPort!" "}</span>${faultLog.vsIp!""}
						</td> 						
						<td class="text-align-center">${faultLog.memberIp!""}</td>
						<td class="text-align-center">${faultLog.interfaceNumber!""}</td>
						</#if>
						<td class="textOver text-align-center" title="${faultLog.occurredTime!""}">${faultLog.occurredTime!""}</td>
						<#if status == 23>
						<td class="textOver text-align-left10" title="${faultLog.event!""}">&nbsp;${faultLog.event!""}&nbsp;</td>						
						<#else>
						</#if>
						<td>
							<#if 0 == faultLog.level>높음</#if>
							<#if 1 == faultLog.level>중간</#if>
							<#if 2 == faultLog.level>낮음</#if>
						</td>
						<td>
							<#if faultLog.status??>
								<#if 1 == faultLog.status>
									<img src="/imgs/common/bu_conn_on.png" alt="done" class="done" />
								<#elseif 0== faultLog.status>
									<img src="/imgs/common/bu_conn_off.png" alt="not" class="done" />
								<#elseif 2== faultLog.status>
									<img src="/imgs/common/icon_warn.png" alt="not" class="done" />
								</#if>
							</#if>
						</td>
<!--						
						<td>${faultLog.cpuUsage!0}%</td>
              			<td>${faultLog.memoryUsage!0}%</td>
-->
            		</tr>            	
            	</#list>
            	</#if>
          		</tbody>
        	</table>
 <!--     	</div>-->
	</div> 
</div>
</div>
