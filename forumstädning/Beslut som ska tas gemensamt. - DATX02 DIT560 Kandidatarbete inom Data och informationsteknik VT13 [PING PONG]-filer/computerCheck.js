/* -*- javascript -*- */ 

/**
 * $Id: computerCheck.js 52198 2012-12-19 12:26:44Z mango $
 */

/**
 * This class contains all necessary information on
 * client platform. Feel free to add other flags,
 * but try to use them as sparsely as possibly
 * in other files than this one.
 */

 function computerCheck(url, core) {
 	url = createURL(url);
	url += "flash=" + detectFlash() + "&";
 	url += "browserName=" + detectBrowserName() + "&";
 	url += "browserVersion=" + detectBrowserVersion() + "&";
 	url += "screen=" + detectScreenSize() + "&";
 	url += "realAudio="	+ detectRealAudio() + "&";
 	url += "quickTime=" + detectQuickTime();
 	document.location.href = url;
 }
 
 function detectFlash() {
 if (is.win && is.ie) {
 	return checkFlashIE();
 }
 	return detectPlugin('Shockwave', 'Flash');
 }

 function detectBrowserName() {
 //TODO Ska g�ras i en action-klass
 	var ua = navigator.userAgent;
 	var i = ua.indexOf("(");
 	var preparens ="";
 	
 	if (i >= 0) {
 		preparens = trim(ua.substring(0,i));
 		var j = preparens.indexOf("/");
 		if (j >= 0) {
 			preparens = preparens.substring(0,j);
 		}
 	}
 	else {
 		preparens = ua;
 	}
 	
 	return preparens;	
 }

 function detectBrowserVersion() {
 //TODO Ska g�ras i en action-klass
 	var ua = navigator.userAgent;
 	var i = ua.indexOf("/");
 	var j = ua.indexOf("(");
 	
 	if (i >= 0 && j >= 0) {
 		return trim(ua.substring(i+1, j));
 	}
 	else {
 		return ua;
 	}
 	
 }

 function detectScreenSize() {
 	return screen.availWidth + "x" + screen.availHeight; 	
 }

 function detectRealAudio() {
  if (is.win && is.ie) {
    return checkRealAudioIE();
  } else {
    if(!detectPlugin('realplayer')) {
      	if(!detectPlugin('realaudio')) {
			return detectPlugin('liveaudio');
      }
 	}
  }
  return true;
 }

 function detectQuickTime() {
  if(is.win && is.ie) {
    return checkQuickTimeIE();
  } else {
	return detectGenericMimeType("video/quicktime");
// 	return detectPlugin('QuickTime');
  }
 }
 
 function detectPlugin() {
 	//Allow for multiple checks in a single pass
	var daPlugins = detectPlugin.arguments;
	var pluginFound = false;
	
	//If plugins array is there and not fake
	if (navigator.plugins && navigator.plugins.length > 0) {
		var pluginArrayLength = navigator.plugins.length;
		// for each plugin...
		for (pluginArrayCounter=0; pluginArrayCounter < pluginArrayLength; pluginArrayCounter++) {
			//Loop through all desired names and check each against the current plugin name

			var numFound = 0;
			for (namesCounter=0; namesCounter < daPlugins.length; namesCounter++) {
	 			//if desired plugin name is found in either plugin name or description
				if ( (navigator.plugins[pluginArrayCounter].name.toLowerCase().indexOf(daPlugins[namesCounter].toLowerCase())>= 0) ||
					 (navigator.plugins[pluginArrayCounter].description.indexOf(daPlugins[namesCounter].toLowerCase()) >= 0) ) {
					 //This name was found
					numFound++;
 				}
			}
			//Now that we have checked all the required names against this one plugin,
			//if the number we found matches the total number provided then we were successful
			if(numFound == daPlugins.length) {
				pluginFound = true;
				//If we've found the plugin, we can stop looking through at the rest of the plugins.
				break;
			}
 		}
 	}
 	return pluginFound;
 }
 
 function detectGenericMimeType(name) {
  for (i = 0; i < navigator.mimeTypes.length; i++) {
    if (navigator.mimeTypes[i].type.toLowerCase().indexOf(name) != -1)
      return true;
  }
  return false;
}
 
 
 function createURL(url) {
 	//Check if the current URL containts any parameters. In that case just add "&".
	//Add "?" if no parameters are found.
	if (url.indexOf("?") >= 0) {
 		url += "&";
 	}
 	else {
 		url += "?";
 	}
 	return url;
 
 }
  
 //Utility function to trim spaces from both ends of a string
 function trim(inString) {
 	var retVal = "";
 	var start = 0;
 	
 	while ( (start < inString.length) && (inString.charAt(start) == ' ')) {
 		++start;
	}
	var end = inString.length;
	while ( (end > 0) && (inString.charAt(end - 1) == ' ') ) {
 		--end;
	}
	retVal = inString.substring(start, end);
	return retVal;
 }
 
 function Is() {
  var navNam = navigator.appName.toLowerCase();
  var navVer = parseFloat (navigator.appVersion);
  var agent  = navigator.userAgent.toLowerCase();
  this.major = parseInt( navigator.appVersion );

  this.khtml = agent.indexOf('khtml') != -1;
  this.ns    = navNam.indexOf('netscape') != -1;
  this.ns40  = agent.indexOf('4.0') != -1;
  this.ns4   = this.ns && (this.major == 4);
  this.ns6   = agent.indexOf('netscape6') != -1;
  this.ns7   = agent.indexOf('netscape/7') != -1;
  this.gecko = agent.indexOf('gecko') != -1;
  //this.mozilla = agent.substr(agent.indexOf(')')).indexOf('gecko') != -1 && !this.ns6;
  this.mozilla = agent.substr(agent.indexOf(')')).length == 16;

  this.ie    = agent.indexOf('msie') != -1; 
  this.ie4   = agent.indexOf('msie 4') != -1;
  this.ie5   = agent.indexOf('msie 5') != -1;
  this.ie55  = this.ie && navVer >= 5.5;
  this.ie6   = agent.indexOf('msie 6') != -1;
    
  this.domStd = !!document.getElementById; // double negation casts to boolean

  this.win   = agent.indexOf('win') != -1;
  this.mac   = agent.indexOf('mac') != -1;
  this.unix  = agent.indexOf('x11') != -1;
  this.useMacStyle = ! (this.win || this.ie5); // mac style also for X11

  this.opera    = agent.indexOf('opera') != -1;

}

var is = new Is();
 
 