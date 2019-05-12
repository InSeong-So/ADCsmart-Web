<script>
  	$(function(){
  		var vsArray = [];
  		var vsList = {};  		
  		
  		<#list dashboardServiceData.adcs as dashSvcData>
	    	<#if dashSvcData.vsList??>
        	<#list dashSvcData.vsList?sort_by("vsStatus") as dashSvc>
        		vsArray.push('${dashSvc.vsIP!''}-${dashSvc.vsPort!''}-${dashSvc.vsStatus!''}-${dashSvc.vsIndex!''}');
        	</#list>
			</#if>
			
  			vsList['${dashSvcData.index}'] = vsArray;
  			vsArray = [];
        </#list>
  		
  		var DashboardModalMain = function(){
  			var vsList = {}, that = this;
  			
	  		this.construct = function() {
				init();
				return this;
			};
			
			this.getVsList = function(adcIndex){
				return that.vsList[adcIndex];
			};
			
			this.setVsList = function(list){
				that.vsList = list;
			};
			
			init = function(){};
			
			return this.construct();
  		};
  		
  		var DashboardModalMain = new DashboardModalMain();
  		DashboardModalMain.setVsList(vsList);
  		
  		function getStatusStyleClass(status){
  			if(status == 2){
  				return "label vs_red";
  			}else{
  				return "label vs_orange";
  			}
  		}
  		
  		function getModalBodyHtml(vsObj){
  			var modalHtmlBody = ''; 
  			
  			if(vsObj){
				modalHtmlBody += "<td>";
				modalHtmlBody += "<div class='"+getStatusStyleClass(vsObj[2])+"'>V</div>";
				modalHtmlBody += "<span class='set_name dashboard-modal-vs-title' style='cursor:pointer;'";
				
				modalHtmlBody += " ip='"+vsObj[0]+"'";
				modalHtmlBody += " port='"+vsObj[1]+"'";
				modalHtmlBody += " index='"+vsObj[3]+"'";
				modalHtmlBody += "> ";
				modalHtmlBody += vsObj[0] +":"+vsObj[1];
				modalHtmlBody += "</span></td>";
			}
			return modalHtmlBody; 
  		}
  		
  		$('.adc-group').click(function(e){
			e.preventDefault();
			var vsList = DashboardModalMain.getVsList($(this).attr("index"));
			var orangeCount = 0;
			var redCount = 0;
			
			$("#dashboard-modal-title").html($(this).attr("name"));
			$("#dashboard-modal-title").attr('adcIndex',$(this).attr("index"));
			$("#dashboard-modal-title-count").text("("+vsList.length+")");
				
			var modalHtmlStart="<table class='table table-bordered table-condensed table-stmodal'><tbody>";
			var modalHtmlEnd="</tbody></table>";
			var modalHtmlBody = "";
			
			for(var i = 0; i<vsList.length/2; i++){
				modalHtmlBody += "<tr>";
				
				var vsObj = vsList[i*2].split('-');			
				modalHtmlBody += getModalBodyHtml(vsObj);
				
				if(vsObj[2] && vsObj[2] == 2){
					redCount++;
				}else{
					orangeCount++;
				}
				
				if(vsList[(i*2)+1]){
					var vsObj = vsList[(i*2)+1].split('-');	
					
					if(vsObj[2] && vsObj[2] == 2){
						redCount++;
					}else{
						orangeCount++;
					}		
					modalHtmlBody += getModalBodyHtml(vsObj);
				}
				
				modalHtmlBody += "</tr>";
			}
			
			$('#dashboard-modal-title-orange').text(": "+orangeCount);
			$('#dashboard-modal-title-red').text(": "+redCount);
			$('#dashboard-modal-html').html(modalHtmlStart+modalHtmlBody+modalHtmlEnd);
		});
  	});
  
  </script>    