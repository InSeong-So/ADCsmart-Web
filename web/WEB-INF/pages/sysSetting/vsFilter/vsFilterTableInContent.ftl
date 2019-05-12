<#setting number_format="0.####">
<#if orderType??>
    <#if 33 == orderType></#if><!--ORDER_TYPE_FIRST VS 상태 -->
    <#if 34 == orderType></#if><!--ORDER_TYPE_SECOND ADC 이름-->
    <#if 35 == orderType></#if><!--ORDER_TYPE_THIRD VS IP 주소-->
    <#if 36 == orderType></#if><!--ORDER_TYPE_FOURTH VS PORT-->
    <#if 37 == orderType></#if><!--ORDER_TYPE_FIFTH 표시/숨김-->
</#if>
<#if orderDir??>
	<#if 1 == orderDir></#if><!-- Asc -->
	<#if 2 == orderDir></#if><!-- Desc -->
</#if>
<colgroup>
    <col width="40px"/>
    <col width="60px"/>
    <col width="auto"/>
    <col width="140px"/>
    <col width="120px"/>
    <col width="auto"/>
</colgroup>
	<tr class="StartLine">
		<td colspan="6"></td>
	</tr>
	<tr class="ContentsHeadLine">
  		<th>
  			<span>
  				<input class="allFilterChk" type="checkbox"/>
  			</span>
  		</th>
  		<th>
  		    <span class="css_textCursor">
                <#if orderDir == 2 && orderType == 33>      
                    <a class="orderDir_Desc">VS 상태
                        <img src="imgs/Desc.gif"/>
                        <span class="none orderType">33</span>
                        <span class="none orderDir">1</span>                    
                    </a>                        
                <#elseif orderDir == 1 && orderType == 14>  
                    <a class="orderDir_Asc">VS 상태
                        <img src="imgs/Asc.gif"/>
                        <span class="none orderType">33</span>
                        <span class="none orderDir">2</span>        
                    </a>
                <#else>
                    <a class="orderDir_None">VS 상태
                        <img src="imgs/none.gif"/>
                        <span class="none orderType">33</span>
                        <span class="none orderDir">2</span>        
                    </a>
                </#if>
            </span> 
  		</th>
		<th>
			<span class="css_textCursor">
				<#if orderDir == 2 && orderType == 34>		
					<a class="orderDir_Desc">ADC이름
						<img src="imgs/Desc.gif"/>
						<span class="none orderType">34</span>
						<span class="none orderDir">1</span>					
					</a>						
				<#elseif orderDir == 1 && orderType == 34>	
					<a class="orderDir_Asc">ADC이름
						<img src="imgs/Asc.gif"/>
						<span class="none orderType">34</span>
						<span class="none orderDir">2</span>		
					</a>
				<#else>
					<a class="orderDir_None">ADC이름
						<img src="imgs/none.gif"/>
						<span class="none orderType">34</span>
						<span class="none orderDir">2</span>		
					</a>
				</#if>
			</span>	
		</th>
		<th>
            <span class="css_textCursor">
                <#if orderDir == 2 && orderType == 35>      
                    <a class="orderDir_Desc">VS IP 주소
                        <img src="imgs/Desc.gif"/>
                        <span class="none orderType">35</span>
                        <span class="none orderDir">1</span>                    
                    </a>                        
                <#elseif orderDir == 1 && orderType == 35>  
                    <a class="orderDir_Asc">VS IP 주소
                        <img src="imgs/Asc.gif"/>
                        <span class="none orderType">35</span>
                        <span class="none orderDir">2</span>        
                    </a>
                <#else>
                    <a class="orderDir_None">VS IP 주소
                        <img src="imgs/none.gif"/>
                        <span class="none orderType">35</span>
                        <span class="none orderDir">2</span>        
                    </a>
                </#if>
            </span> 
		</th>
		<th>
            <span class="css_textCursor">
                <#if orderDir == 2 && orderType == 36>      
                    <a class="orderDir_Desc">VS Port
                        <img src="imgs/Desc.gif"/>
                        <span class="none orderType">36</span>
                        <span class="none orderDir">1</span>                    
                    </a>                        
                <#elseif orderDir == 1 && orderType == 36>  
                    <a class="orderDir_Asc">VS Port
                        <img src="imgs/Asc.gif"/>
                        <span class="none orderType">36</span>
                        <span class="none orderDir">2</span>        
                    </a>
                <#else>
                    <a class="orderDir_None">VS Port
                        <img src="imgs/none.gif"/>
                        <span class="none orderType">36</span>
                        <span class="none orderDir">2</span>        
                    </a>
                </#if>
            </span> 		
		</th>
		<th>
            <span class="css_textCursor">
                <#if orderDir == 2 && orderType == 37>      
                    <a class="orderDir_Desc">표시/숨김
                        <img src="imgs/Desc.gif"/>
                        <span class="none orderType">37</span>
                        <span class="none orderDir">1</span>                    
                    </a>                        
                <#elseif orderDir == 1 && orderType == 37>  
                    <a class="orderDir_Asc">표시/숨김
                        <img src="imgs/Asc.gif"/>
                        <span class="none orderType">37</span>
                        <span class="none orderDir">2</span>        
                    </a>
                <#else>
                    <a class="orderDir_None">표시/숨김
                        <img src="imgs/none.gif"/>
                        <span class="none orderType">37</span>
                        <span class="none orderDir">2</span>        
                    </a>
                </#if>
            </span>         
		</th>
	</tr>
	<tr class="StartLine1">
		<td colspan="6"></td>
	</tr>			
</thead>
<#list vsFilterInfoList as thsVsGrp>
    <tr class="ContentsLine1 userList"> 	
        <td class="align_center">
            <span>
                <input class="filterChk" type="checkbox" value="${thsVsGrp.index}"/>
            </span>
        </td>
        
        <td class="align_center"><img src="imgs/icon/icon_vs_disconn.png" ></td>
		<td class="align_center textOver" title="${thsVsGrp.adcName!''}">${(thsVsGrp.adcName)!''}</td>     
		<td class="align_center textOver" title="${thsVsGrp.vsIPAddress!''}">${(thsVsGrp.vsIPAddress)!''}</td>     
		<td class="align_center textOver" title="${thsVsGrp.vsPort!''}">${(thsVsGrp.vsPort)!''}</td>     
        <#if thsVsGrp.showHideState == 2>  
            <td class="align_center"><img src="imgs/icon/bu_conn_off.png" ></td>
        <#else>
            <td class="align_center"><img src="imgs/icon/bu_conn_on.png" ></td>
        </#if>  
    
	</tr>
	<tr class="DivideLine">
		<td colspan="6"></td>
	</tr>
</#list>
<tr class="EndLine">
	<td colspan="6"></td>
</tr>