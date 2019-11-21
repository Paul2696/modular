$(document).ready(function(){
    signUpViewModel.init();
	ko.applyBindings(signUpViewModel);
	$("#main-contact-form").submit(doPost);
});

var signUpViewModel = {
	name : ko.observable(),
	email : ko.observable(),
	password : ko.observable(),
	error : ko.observable(false),

	init : function(){
	    var self = this;
	}
};

function redirect(){
	window.location.href="/elearningUI/html/log_in.html";
}

function doPost(){
	var signUp = {
		name : signUpViewModel.name(),
		email : signUpViewModel.email(),
		password : signUpViewModel.password(),
		userType : {
			idUserType : $("#userType").val()
		}
	}

	var signUpJson = JSON.stringify(signUp);
	$.ajax({
		url : "http://localhost:8080/elearning/signup/user",
		contentType : "application/json",
		data : signUpJson,
		method : "post",
		success : function(response){
		   redirect();
		}
	});
	return false;
}

