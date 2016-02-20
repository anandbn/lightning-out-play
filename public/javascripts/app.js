
function forceInit() {
	force.init(config);
};

function forceLogin(key) {
	forceInit();
	force.login(function(success) {
		var oauth = force.getOauth();
		setupLightning();
	});	
}

var _lightningReady = false;

function setupLightning(callback) {
	var appName = config.loApp;
	var oauth = force.getOauth();
    if (!oauth) {
        alert("Please login to Salesforce.com first!");
        return;
    }

	if (_lightningReady) {
		if (typeof callback === "function") {
			callback();
		}
	} else {
	    //Use a proxy URL to avoid dealing with CORS
	    var url = "/salesforce"; 

	    // Transform the URL for Lightning
	    //var url = oauth.instanceUrl.replace("my.salesforce", "lightning.force");

	    //Set the instance Url in a cookie
	    setCookie("instanceUrl",oauth.instanceUrl.replace("my.salesforce", "lightning.force"));

	    $Lightning.use(appName, 
	        function() {
				_lightningReady = true;
				document.getElementById("chatterFeedButton").style.display = "";
				document.getElementById("recordViewButton").style.display = "";
				document.getElementById("customComponentBtn").style.display = "";
				if (typeof callback === "function") {
					callback();
				}
	        }, url, oauth.access_token);
	}
}

function createChatterFeed(type, subjectId) {
    setupLightning(function() {
		$Lightning.createComponent("forceChatter:feed", {type: type, subjectId: subjectId}, "chatterFeed"); 
    });
}


function customComponent() {
    setupLightning(function() {
		$Lightning.createComponent("c:HelloWorld", {}, "customComponent"); 
    });
}


function setCookie(cname, cvalue) {
    document.cookie = cname + "=" + cvalue + "; " ;
}
