<div class="monitor_con5_2">
	<div class="monitor_con5_B">
		<table class="table200" cellpadding="0" cellspacing="0">
			<tbody>				
				<tr>
					<th class="table200_th">최근 설정 변경 내역</th>
				</tr>
				<#list dashSlbChangeStatus.logList![] as logInfo>
				<li>
				<tr>
					<td class = "table200_td  text-align-left10 textOver" title="ADC이름:${logInfo.adcName!}&nbsp;IP주소:${logInfo.vsIP!}">
						<#if logInfo.rank ==1>
							<img src="../../../imgs/dashboard/num_1.png"> ${logInfo.content!""}
						<#elseif logInfo.rank ==2>
							<img src="../../../imgs/dashboard/num_2.png"> ${logInfo.content!""}
						<#else>
							<img src="../../../imgs/dashboard/num_3.png"> ${logInfo.content!""} 
						</#if>
					</td>
				</tr>
				</li> 
				</#list>
			</tbody>
		</table>
	</div>
</div>