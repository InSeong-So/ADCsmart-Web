var TestDiagramUI = Class.create({
	initialize : function() 
	{
		this.ajaxManager = new FlowitAjax();
	},

	loadDiagramFlow :function(params)
	{
		with (this)
		{
			ajaxManager.runHtmlExt({
				url			: '/testDiagramUI/loadDiagramFlow.action',
				target		: '.diagramFlow_page',
				data		: params,
				successFn	: function(params) 
				{
//					alert("ok");
				}			
			});
		}
	}
});