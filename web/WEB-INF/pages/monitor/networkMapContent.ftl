<!----- Contents List Start ----->
<#if langCode??>
  <#if "ko_KR" == langCode><#assign img_lang = ""></#if>
  <#if "en_US" == langCode><#assign img_lang = "_eng"></#if>
</#if>
<#if orderType??>
	<#if 34 == orderType></#if><!-- IP -->
	<#if 35 == orderType></#if><!-- 포트 -->
</#if>
<#if orderDir??>
	<#if 1 == orderDir></#if><!-- Asc -->
	<#if 2 == orderDir></#if><!-- Desc -->
</#if>
<div class="contents_area">
	<div>
		<img src="imgs/title${img_lang!""}/h3_networkMap.gif" class="title_h3"/>	
	<#if ((adc.type) == "Alteon")>
		<#if ((adc.isFlb) == 1) && (!(accountRole == 'vsAdmin') || !(accountRole == 'rsAdmin'))>
			<div class="title_h2_tab">
	    		<#if (lbType == 0)>
				<span class="tab1">
						<a class="adcSlbLnk" href="#">
							<input name="network.lbtype" type="hidden" value="2"/>
							<img src="imgs/meun${img_lang!""}/3depth_slb_0.png"/>	
						</a>					
				</span>	
				<span class="tabdiv">
						<img src="imgs/meun${img_lang!""}/3depth_div_1.png" />
				</span>								
				<span class="tab2">
						<a class="adcFlbLnk" href="#">
							<input name="network.lbtype" type="hidden" value="1"/>
							<img src="imgs/meun${img_lang!""}/3depth_flb2_0.png"/>							
						</a>								
				</span>		
				<span class="tabdiv">
						<img src="imgs/meun${img_lang!""}/3depth_div_1.png" />
				</span>								
				<span class="tab3">
					<a class="adcAllLnk" href="#">
						<input name="network.lbtype" type="hidden" value="0"/>
						<img src="imgs/meun${img_lang!""}/3depth_all_1.png"/>	
					</a>						
				</span>												
				<#elseif (lbType == 1)>
				<span class="tab1">
						<a class="adcSlbLnk" href="#">
							<input name="network.lbtype" type="hidden" value="2"/>
							<img src="imgs/meun${img_lang!""}/3depth_slb_1.png" />	
						</a>					
				</span>	
				<span class="tabdiv">
						<img src="imgs/meun${img_lang!""}/3depth_div_1.png" />
				</span>								
				<span class="tab2">
						<a class="adcFlbLnk" href="#">
							<input name="network.lbtype" type="hidden" value="1"/>
							<img src="imgs/meun${img_lang!""}/3depth_flb2_0.png" />							
						</a>
				</span>	
				<span class="tabdiv">
						<img src="imgs/meun${img_lang!""}/3depth_div_1.png" />
				</span>													
				<span class="tab3">
						<a class="adcAllLnk" href="#">
							<input name="network.lbtype" type="hidden" value="0"/>
							<img src="imgs/meun${img_lang!""}/3depth_all_0.png"/>	
						</a>						
				</span>										
				<#elseif (lbType == 2)>
				<span class="tab1">
						<a class="adcSlbLnk" href="#">
							<input name="network.lbtype" type="hidden" value="2"/>
							<img src="imgs/meun${img_lang!""}/3depth_slb_0.png" />	
						</a>					
				</span>	
				<span class="tabdiv">
						<img src="imgs/meun${img_lang!""}/3depth_div_1.png" />
				</span>								
				<span class="tab2">
						<a class="adcFlbLnk" href="#">
							<input name="network.lbtype" type="hidden" value="1"/>
							<img src="imgs/meun${img_lang!""}/3depth_flb2_1.png" />							
						</a>
				</span>										
				<span class="tab3">
						<a class="adcAllLnk" href="#">
							<input name="network.lbtype" type="hidden" value="0"/>
							<img src="imgs/meun${img_lang!""}/3depth_all_0.png" />	
						</a>						
				</span>								
				<#else>	
				</#if>
			</div>
		<#elseif (accountRole == 'vsAdmin') || (accountRole == 'rsAdmin')>
			<div class="title_h2_tab">
				<span class="tab1">
					<a class="adcSlbLnk" href="#">
						<input name="network.lbtype" type="hidden" value="2"/>
						<img src="imgs/meun${img_lang!""}/3depth_slb_1.png"/>	
					</a>					
				</span>	
				<span class="tabdiv">
						<img src="imgs/meun${img_lang!""}/3depth_div_1.png" />
				</span>								
			</div>
		<#else>
			<div class="title_h2_tab">
				<span class="tab1">
					<a class="adcSlbLnk" href="#">
						<input name="network.lbtype" type="hidden" value="2"/>
						<img src="imgs/meun${img_lang!""}/3depth_slb_1.png"/>	
					</a>					
				</span>		
				<span class="tabdiv">
						<img src="imgs/meun${img_lang!""}/3depth_div_2.png" />
				</span>							
				<span class="tab2">
					<a class="">
						<input name="network.lbtype" type="hidden" value="1"/>
						<img src="imgs/meun${img_lang!""}/3depth_flb2_2.png"/>							
					</a>								
				</span>	
				<span class="tabdiv">
						<img src="imgs/meun${img_lang!""}/3depth_div_2.png" />
				</span>								
				<span class="tab3">
					<a class="">
						<input name="network.lbtype" type="hidden" value="0"/>
						<img src="imgs/meun${img_lang!""}/3depth_all_2.png"/>	
					</a>						
				</span>				
			</div>
		</#if>
	</#if>					 
	</div>
	<div class="nulldata none"></div>
	<div class="successdata">
	<div class="control_Board cont_sch">
		<div style="padding: 2px 0px; margin-bottom: 6px;" > 		
	 		<table class="Board_networkmaotop" cellpadding="0" cellspacing="0">
				<colgroup>
					<col width="174px/">
					<col width="174px"/>                           
	                <col width="174px"/>                           
					<col width="174px"/>
				</colgroup>                    
				<tr>
					<td class="color11 align_cneter totalCountVs server_cnt css_textCursor">${(networkMap.totalCountVs)!0}</td>                            
					<td class="color2 align_cneter conn server_cnt css_textCursor"><span class="none status">1</span>${(networkMap.availableCountVs)!0}</td>                                 
					<td class="color3  align_cneter disconn server_cnt css_textCursor"><span class="none status">2</span>${(networkMap.unavailableCountVs)!0}
<!-- 
						<span class="server_cnt">
							<#assign unavailablePercentVs = (networkMap.unavailablePercentVs)!0>
					   	    <#if (unavailablePercentVs < 10)>
								<#assign colorClass = "color_fae005">
					  		<#else>
								<#assign colorClass = "color_e50c0c">
					  		</#if>
					 	  	<span class="none status">2</span>
					  	    <span class="${colorClass}">${(networkMap.unavailableCountVs)!0}</span>
						</span>	
-->											
					</td>                                                          
					<td class="color4  align_cneter disabled server_cnt css_textCursor"><span class="none status">0</span>${(networkMap.disabledCountVs)!0}</td>
	            </tr>    
			</table>
		</div>				
		<div class="pin_bg" >			
			<div class="pin">			
				<span class="inputTextposition_map" >
				<input name="searchTxt1" class="searchTxt inputText_search" type="text" value="${searchKey!}"/>
		 		</span>	 	
				<span class="btn_map">
				<a class="searchLnk" href="#">
					<img src="imgs/meun/btn_search_1.png" alt="${LANGCODEMAP["MSG_NETWORK_LISTSEARCH"]!}"/>
				</a>
				</span>
			</div>	
			<div class="pin"></div>			
	        <div class="pin align_right">	        
	        	<span class="align_middle sort_order">
			       		 ${LANGCODEMAP["MSG_NETWORK_ORDER"]!} 		
			       	<select name="" id="sortTypeArea" class="sortTypeArea">
						<option value="34_1">IP(${LANGCODEMAP["MSG_NETWORK_POP_VS_ASC"]!})</option>
						<option value="34_2">IP(${LANGCODEMAP["MSG_NETWORK_POP_VS_DESC"]!})</option>						
						<option value="35_1">${LANGCODEMAP["MSG_NETWORK_POP_VS_PORT"]!}(${LANGCODEMAP["MSG_NETWORK_POP_VS_ASC"]!})</option>
						<option value="35_2">${LANGCODEMAP["MSG_NETWORK_POP_VS_PORT"]!}(${LANGCODEMAP["MSG_NETWORK_POP_VS_DESC"]!})</option>						
					</select> 			       		 
					<!--<input class="orderType" type="text" value="34"></input>
					<input class="orderDir" type="text" value="1"></input>-->
<!-- 작업후 지워주세요	   	<span id="sortTypeArea">
			        	<a href="#" class="sortTypeIP">IP
			        		<span class="none orderType">34</span>
			        	</a>
			        	<a href="#" class="sortTypePort none"> ${LANGCODEMAP["MSG_NETWORK_POP_VS_PORT"]!}
			        		<span class="none orderType">35</span>
			        	</a>
			        </span>
			        -		        
			        <span id="sortAscArea" class="">
			        	<a href="#" class="Asc sortAsc">Ascending
			        		<span class="bu"><span class="none orderDir">1</span></span>
			        	</a>
			        	<a href="#" class="Desc sortDesc none">Descending
			        		<span class="bu"><span class="none orderDir">2</span></span>
			        	</a>
			        </span>
-->				        
		        </span>		        			        
		        <span>
		        <#if (lbType == 1)>
		        	<input type="button" class="exportCssLnk Btn_white" value="${LANGCODEMAP["MSG_ADCLOG_OUT_FILE"]!}"/>
		        </#if>
		      		<input type="button" class="Btn_white" id="refresh" value="${LANGCODEMAP["MSG_NETWORK_REFRESH"]!}">
				</span>	
			</div>	
		</div>
		<div class="columns">	
	 		<#include "networkMapTableInContent.ftl"/>
	 	</div>	 
	</div>
	<div class="searchNotMsg_noline none">${LANGCODEMAP["MSG_SEARCH_NOT_RESULT"]!}</div>
<!-- 그룹백업 팝업 시작-->
<div id="group-backup-pop" class="pop_type_wrap">
    <h2 class="backup-title"></h2>
    <div class="pop2_contents">        
        <div class="description condition">            
            <table class="Board_100" cellpadding="0" cellspacing="0" style="table-layout: fixed;">
                <tr class="StartLine">
                    <td colspan="1"></td>
                </tr>
                <tr class="ContentsHeadLine">
                    <th>${LANGCODEMAP["MSG_NETWORK_POP_REALSERVER"]!}</th>                                    
                </tr>
                <tr class="StartLine1">
                    <td colspan="1"></td>
                </tr>
            </table>
        	<div class="listHeight200">
            <table class="Board_100 backup-group" cellpadding="0" cellspacing="0" style="table-layout: fixed;">                                                                                                           
            </table>
            </div>            
        </div>        
        <div class="position_cT10"> 
           	<input type="button" class="popupclosebtn Btn_white" value="${LANGCODEMAP["MSG_NETWORK_POP_CLOSE"]!}"/>	                
    </div>
    <p class="close">
        <a href="#" title="${LANGCODEMAP["MSG_DIAG_ANAL_CLOSE"]!}"> 
            <img src="imgs/popup/btn_clse.gif" alt="${LANGCODEMAP["MSG_DIAG_ANAL_CLOSE"]!}" />
        </a>
    </p>
</div>
<!-- 그룹백업 팝업  끝-->
<!-- 트래픽 상세정보 -->
<div id="trafficDesc" class="pop_type_wrap"></div>
<div id="vs-description-pop" class="pop_type_wrap">
    <h2 class="vsDescription-title"></h2>
    <div class="pop2_contents">        
        <div class="description condition" align="center">            
                    설명: <input type="text" id="vsDescription" style="width:85%" value="">
        </div>  
        <p></p>
        <div class="position_cT10" align="center"> 
            <input type="button" class="popupclosebtn" value="확인"/>                 
            <input type="button" class="popupcancelbtn" value="취소"/>                 
    </div>
</div>
