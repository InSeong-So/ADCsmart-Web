var Progress = {};

Progress.bar = (function(config) {
    if ("undefined" === typeof config) {
        console.log('Please give config object, like:\nnew Progress.bar({ id: "progress1", autoRemove: true, removeTimeout: 2000 , backgroundSpeed: 50, type: "discharge", showPercentage: true });');
        return;
    }
    // defaults
    config.type = config.type ? config.type : 'charge';
    config.id   = config.id   ? config.id   : 'progress' + Math.floor(Math.random()*9999);

    var outerDiv		= createOuterDiv();
    var percentageSpan	= createPercentageSpan();
    var innerDiv		= createInnerDiv();
    
    var innerDivPrev	= createInnerDivPrev();
    var innerDivAvg		= createInnerDivAvg();
    var innerDivCurr	= createInnerDivCurr();
    
    var intervals      = [];

    function createOuterDiv() {
        var outerDiv = document.createElement('div');
            outerDiv.setAttribute('class', 'outerDiv');
            outerDiv.setAttribute('id', config.id);
        return outerDiv;
    }

    function createPercentageSpan() {
        var percentageSpan = document.createElement('span');
            percentageSpan.innerHTML = config.type === 'charge' ? '0%' : '100%';
        return percentageSpan;
    }

    function createInnerDiv() {
        var innerDiv = document.createElement('div');
            innerDiv.setAttribute('class', 'innerDiv');
            innerDiv.style.width = config.type === 'charge' ? '0' : '100%';
        return innerDiv;
    }

    function createInnerDivAvg() {
        var innerDivAvg = document.createElement('div');
        	innerDivAvg.setAttribute('class', 'innerDiv_avg');
        	innerDivAvg.style.width = config.type === 'charge' ? '0' : '100%';
        return innerDivAvg;
    }
    
    function createInnerDivCurr() {
        var innerDivCurr = document.createElement('div');
        	innerDivCurr.setAttribute('class', 'innerDiv_curr');
        	innerDivCurr.style.width = config.type === 'charge' ? '0' : '100%';
        return innerDivCurr;
    }
    
    function createInnerDivPrev() {
        var innerDivPrev = document.createElement('div');
        	innerDivPrev.setAttribute('class', 'innerDiv_prev');
        	innerDivPrev.style.width = config.type === 'charge' ? '0' : '100%';
        return innerDivPrev;
    }
    
    function update(percent) {
        percent = percent > 100 ? 100 : percent < 0 ? 0 : percent;
        innerDiv.style.width = percent + '%';
        if (config.showPercentage) {
            percentageSpan.innerHTML = percent + '%';
        }
        checkForAutoRemoval(percent);
    }

    function updateCurr(percent) 
    {
    	if (percent == 0)
    	{
    		 percentageSpan.innerHTML = "No Data";
    	}
    	else
    	{
    		percent = percent > 100 ? 100 : percent < 0 ? 0 : percent;
    		innerDivCurr.style.width = percent + '%';
	        if (config.showPercentage) {
	            percentageSpan.innerHTML = percent + '%';
	        }
    	}
        checkForAutoRemoval(percent);
    }
    
    function updateAvg(percent) 
    {
    	if (percent == 0)
    	{
    		 percentageSpan.innerHTML = "No Data";
    	}
    	else
    	{
	        percent = percent > 100 ? 100 : percent < 0 ? 0 : percent;
	        innerDivAvg.style.width = percent + '%';
	        if (config.showPercentage) 
	        {
	            percentageSpan.innerHTML = percent + '%';
	        }
    	}
        checkForAutoRemoval(percent);
    }
    
    function updatePrev(percent) 
    {
    	if (percent == 0)
    	{
    		 percentageSpan.innerHTML = "No Data";
    	}
    	else
    	{
	        percent = percent > 100 ? 100 : percent < 0 ? 0 : percent;
	        innerDivPrev.style.width = percent + '%';
	        if (config.showPercentage) {
	            percentageSpan.innerHTML = percent + '%';
	        }
    	}
        checkForAutoRemoval(percent);
    }
    
    function renderTo(element) {
        if (config.showPercentage) {
            outerDiv.appendChild(percentageSpan);
        }
        outerDiv.appendChild(innerDiv);
        element.appendChild(outerDiv);
        animateBackground();
    }
    
    function renderToAvg(element) {
        if (config.showPercentage) {
        	outerDiv.appendChild(percentageSpan);
        }
        outerDiv.appendChild(innerDivAvg);
        element.appendChild(outerDiv);
        animateBackground();
    }
    
    function renderToCurr(element) {
        if (config.showPercentage) {
        	outerDiv.appendChild(percentageSpan);
        }
        outerDiv.appendChild(innerDivCurr);
        element.appendChild(outerDiv);
        animateBackground();
    }
    
    function renderToPrev(element) {
        if (config.showPercentage) {
        	outerDiv.appendChild(percentageSpan);
        }
        outerDiv.appendChild(innerDivPrev);
        element.appendChild(outerDiv);
        animateBackground();
    }
    
    function animateBackground() {
        if (!config.backgroundSpeed) {
            return;
        }
        var position = 0;
        intervals['backgroundAnimation'] = window.setInterval(function() {
            if (config.backgroundSpeed < 0) {
                innerDiv.style.backgroundPosition = ++position + 'px';
                innerDivAvg.style.backgroundPosition = ++position + 'px';
                innerDivCurr.style.backgroundPosition = ++position + 'px';
                innerDivPrev.style.backgroundPosition = ++position + 'px';
            } else {
                innerDiv.style.backgroundPosition = --position + 'px';
                innerDivAvg.style.backgroundPosition = --position + 'px';
                innerDivCurr.style.backgroundPosition = --position + 'px';
                innerDivPrev.style.backgroundPosition = --position + 'px';
            }
        }, config.backgroundSpeed);
    }

    function checkForAutoRemoval(percent) {
        if ('discharge' === config.type && (0 !== percent || true !== config.autoRemove)) {
            return;
        }
        if ('charge' === config.type && (100 !== percent || true !== config.autoRemove)) {
            return;
        }
        !config.removeTimeout ? remove() : window.setTimeout(remove, config.removeTimeout);
    }

    function remove(callback) {
        window.clearInterval(intervals['backgroundAnimation']);
        var renderedProgressBar = document.getElementById(config.id);
        renderedProgressBar.parentNode.removeChild(renderedProgressBar);
    }

    return {
        update:   update,
        updateCurr:   updateCurr,
        updateAvg:   updateAvg,
        updatePrev:   updatePrev,
        renderTo: renderTo,
        renderToAvg: renderToAvg,
        renderToCurr: renderToCurr,
        renderToPrev: renderToPrev,
        remove:   remove
    };
});