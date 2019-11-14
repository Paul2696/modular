function CuestionarioViewModel(){
	var self = this;
	self.preguntas = ko.observableArray();
	self.tipo = ko.observable();
	self.codigo = ko.observable();
    menu = ko.observableArray([]);

    init: function() {
                var self = this;
                var session = parseSession(Cookies.getJSON("session"));
                if(session && session.isSessionActive()) {
                    self.menu(session.getSessionMenu());
                } else {
                    window.location.href = "/webapp/html/log_in.html";
                }
            }
}

var cuestionarioViewModel = new CuestionarioViewModel();
$(document).ready(function(){
	$.getJSON("http://localhost:8000/cuestionario/json", function(cuestionarioJSON){
		cuestionarioViewModel.preguntas(cuestionarioJSON.visual);
		ko.utils.arrayPushAll(cuestionarioViewModel.preguntas, cuestionarioJSON.auditivo);
		ko.utils.arrayPushAll(cuestionarioViewModel.preguntas, cuestionarioJSON.kinestesico);

	});
	ko.applyBindings(cuestionarioViewModel);
    cuestionarioViewModel.init();
});

function sendAnswers(){
		var respuestasJSON = {
			visual : [],
			auditivo : [],
			kinestesico : [],
		};
		var respuestas = $("input:checked");
		for (var i = 0; i < respuestas.length; i++) {
			if(i < 8){
				respuestasJSON.visual.push(respuestas[i].value);
			}else if(i >= 8 && i < 15){
				respuestasJSON.auditivo.push(respuestas[i].value);
			}else if(i >= 15){
				respuestasJSON.kinestesico.push(respuestas[i].value);
			}
		}
		if(!isChecked()){
			alert("Por favor, completa el cuestionario");
		}
		else{
		$.ajax({
				url : "http://localhost:8000/cuestionario/calificar",
				contentType : "application/json",
				data : JSON.stringify(respuestasJSON),
				method : "post",
				success : function(response){
					cuestionarioViewModel.tipo(data);
				}
			});
		}
		
	}

function isChecked(){
	var answered = document.forms[0];
	var counter = 0;
	var i;
	for(i = 0; i < answered.length; i++){
		if (answered[i].checked){
			counter++;
		}
	}
	return counter < 22;
}