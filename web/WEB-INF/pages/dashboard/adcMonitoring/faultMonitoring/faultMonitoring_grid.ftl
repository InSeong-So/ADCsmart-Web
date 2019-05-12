<div class="monitor_con3_2">
	<div class="monitor_con3_B">
		<table class="table200" cellpadding="0" cellspacing="0" style="table-layout:fixed;">
			<tr>
				<th class="table200_th">최근 장애 내역</th>
			</tr>
			<#list dashFaultStatus.logList![] as logInfo>					
				<li>	
					<tr>
						<td class="textOver table200_td text-align-left10" title="${logInfo.content!}">
							<#if logInfo.rank ==1> 
								<img src="../../../imgs/dashboard/num_1.png" alt="" class="mar_rgt5" />${logInfo.content!""}
							<#elseif logInfo.rank ==2> 
								<img src="../../../imgs/dashboard/num_2.png" alt="" class="mar_rgt5" />${logInfo.content!""}
							<#elseif logInfo.rank ==3> 
								<img src="../../../imgs/dashboard/num_3.png" alt="" class="mar_rgt5" />${logInfo.content!""} 
							<#else>
								<!-- 에러 -->
							</#if>
						</td>								
					</tr>		
				</li>
			</#list>
		</table>
	</div>
</div>