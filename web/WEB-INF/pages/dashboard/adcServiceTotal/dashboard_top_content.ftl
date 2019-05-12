<#if dashboardServiceData??>
<table class="table table-st">
	<tbody>
		<tr>
	  		<td>
	  			<div>
				    <span class="label vs_orange">V</span>
				    <span class="label vs_red">V</span>
	            </div>                    
	            <div>서비스(Virtual Server)</div>
	            <div>
	                <span>비정상 (${dashboardServiceData.vsAbnomal!'00'}/${dashboardServiceData.vsTotal!'00'})</span>
	                <span> 필터링 (${dashboardServiceData.vsFiltered!'00'})</sapn>
	            </div>
	        </td>
	        <td>
	            <div><img src="imgs/icon/icon_1d_conn.png" alt="available"></div>
	            <div>ADC</div>
	            <div>연결 (${dashboardServiceData.adcNomal!'00'})</div>              
	        </td>
	        <td>
	            <div><img src="imgs/icon/icon_1d_disconn.png" alt="available"></div>
	        	<div>ADC</div>
	            <div>연결안됨 (${dashboardServiceData.adcAbnomal!'00'})</div>             
	        </td>        
		</tr>
	</tbody>
</table>  
</#if>