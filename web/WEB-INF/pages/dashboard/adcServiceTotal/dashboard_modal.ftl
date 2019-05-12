 <div class="modal fade" id="dashboardModal" role="dialog" tabindex="-1" data-keyboard="false" data-backdrop="static">
    <div class="modal-dialog" style="z-index: inherit;">
    
      <!-- Modal content-->
      <div class="modal-content">
        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal">&times;</button>
          <h4 class="modal-title">
          	<i class="fa fa-server" aria-hidden="true"></i> 
          	<span id="dashboard-modal-title" adcIndex=''></span>
          	<small id="dashboard-modal-title-count"></small> 
          	
          	<div class="label vs_red">V</div>
          	<small id="dashboard-modal-title-red"></small>
          	
          	<div class="label vs_orange">V</div>
          	<small id="dashboard-modal-title-orange"></small>
          </h4>
        </div>
        
        <div class="modal-body">
            <div id="dashboard-modal-html"></div>
        </div>
        
        <div class="modal-footer">
          <button id="dashboard-modal-ok" type="button" class="btn btn-default btn-sm" data-dismiss="modal">확인</button>
        </div>
      </div>
      
    </div>
  </div><!-- /*  modal --> 
