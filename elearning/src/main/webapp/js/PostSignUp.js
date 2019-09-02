$(document).ready(function(){
    signUpViewModel.init();
	ko.applyBindings(signUpViewModel);
	$("#main-contact-form").submit(doPost);
});

var signUpViewModel = {
	name : ko.observable(),
	mail : ko.observable(),
	password : ko.observable(),
	menu : ko.observableArray([]),
	error : ko.observable(false),

	init : function(){
	    var self = this;
	    var session = parseSession(Cookies.getJSON("session"));
        if(session != null && session.isSessionActive()) {
            window.location.href = "../index.html";
         }
        self.menu(getNoUserMenu());
	},

	redirect: function(){
	    $("#ok").click(function(){
	        window.location.href="../index.html";
	    });
	}
};

function doPost(){
	var signUp = {
		"name" : signUpViewModel.name(),
		"email" : signUpViewModel.mail(),
		"password" : signUpViewModel.password()
	}

	var signUpJson = JSON.stringify(signUp);
	$.ajax({
		url : "http://localhost:8000/api/user",
		contentType : "application/json",
		data : signUpJson,
		method : "post",
		success : function(response){
		    signUpViewModel.error(false);
		    $("#alertSuccess").modal("show");
		    if(response.code == 200){
		        $("#message").html("Se ha registrado exitosamente");
		        signUpViewModel.redirect();
		    }
		    else if(response.code == 101){
		        $("#message").html("El usuario ya existe");
		    }
		    else{
		        $("#message").html("Error desconocido");
		    }
		}
	});
	return false;
}

