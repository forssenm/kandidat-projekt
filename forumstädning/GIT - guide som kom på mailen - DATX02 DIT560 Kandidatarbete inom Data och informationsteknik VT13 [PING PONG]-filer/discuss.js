
/**
 * Adds the reply message as a quote in the new message.
 */
function doQuote() {
	var lines = getElementById('replyMessage').value.split("\n");
	var quoteText = "";
	for (var i = 0; i < lines.length; i++) {
		quoteText += "> " + lines[i] + "\n";		
	}
	var contentElement = getElementById('content');		
	contentElement.value = contentElement.value + quoteText + "\n\n";
	contentElement.focus();
}

function popupDiscussPrintableView(url, courseId) {

	var popup = window.open(url, 'ppDiscussPrintView' + courseId, 'height=450,width=600,resizable,scrollbars,menubar');	
	if (popup) {
		popup.focus();
	}
	return false;
}

function popupDiscussSearchResult(url, courseId) {	
	
	window.name = 'ppbasewin' + courseId;		
	
	var popup = window.open(url, 'ppdiscussSearchResult' + courseId, 'height=450,width=420,resizable,scrollbars');
	
	if (popup) {
		popup.focus();
	}

	return false;
}

function courseDiscussPointOfTime() {
	
	var container = document.getElementById('field-set-timestamp');
	var select = document.getElementById('pointOfTime');
	
	if (select.options[select.selectedIndex].value == 10) {
		container.className = 'block';
	} else {
		container.className = 'hide';
	}	
}
function showAdvancedSearch() {		
		var advanced = document.getElementById('advanced-search');
		var simple = document.getElementById('simple-search');
		var searchMode = document.getElementById('searchMode');		
		var content = document.getElementById('content');
		
		advanced.className='block';
		simple.className='hide';
		
		searchMode.value = 2;	
		content.focus();
		
		return false;
	}
	
	function showSimpleSearch() {
		
		var advanced = document.getElementById('advanced-search');
		var simple = document.getElementById('simple-search');
		var searchMode = document.getElementById('searchMode');
		
		advanced.className='hide';
		simple.className='block';
		
		searchMode.value = 1;		
		
		return false;
	}

