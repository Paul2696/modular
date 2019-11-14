$(document).ready(function(){
    logInViewModel.init();
	ko.applyBindings(logInViewModel);
	$("#main-contact-form").submit(logInViewModel.login);
});

var logInViewModel = {
	code_mail : ko.observable(),
	password : ko.observable(),
	menu : ko.observableArray([]),

	init: function(){
	    var self = this;
	    var session = parseSession(Cookies.getJSON("session"));
        if(session != null && session.isSessionActive()) {
            window.location.href = "../index.html";
         }
        self.menu(getNoUserMenu());
    },

	/**
	 * function to login
	 */
	login: function() {
		var logIn = {
    		"username" : logInViewModel.code_mail(),
    		"password" : logInViewModel.password()
    	}


    	var loginJson = JSON.stringify(logIn);
    	$.ajax({
    		url : "http://localhost:8000/account/login/",
    		contentType : "application/json",
    		data : loginJson,
    		method : "POST",
    		success : function(response){
                //TODO: check real value names with deibid backend and update testServer
                Cookies.set("session", new Session(response["token"], response["type"]) );
                //console.log("success");
                window.location.href = "../index.html";
    		},
    		error: function(response) {
    		    $("#alertSuccess").modal("show");
                if(response.responseJSON.code == 100){
                    $("#message").html("Usuario o contrasenia incorrectos");
                }
    		}
    	});

    	return false;
	}
};
