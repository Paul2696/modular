define([
    "ko",
    "jquery",
    "cookie",
    "el/modules/client/UserRestClient",
    "el/modules/session/Session",
    "bootstrap"
], function(ko, $, cookie, userClient, session){
    function TestViewModel(){
        let self = this;

        self.questions = ko.observableArray([]);
        self.answers = {
            visual : 0,
            auditivo : 0,
            kinestesico : 0,
            idUser : session.idUser
        };
        self.counter = 0;
        self.questionNumber = ko.observable(self.counter);
        self.currentQuestion = ko.observable();
        self.showTest = ko.observable(true);
        self.view = view;
        self.model = {
            data : {
                learningType : ko.observable()
            },
            handler : () => {

            }
        }

        self.init = () =>{
            self.loadTest();
        };

        self.loadTest = () => {
            userClient.getUser((session.idUser), (data) =>{
               self.model.data.learningType(data.learningType);
                if(self.model.data.learningType() != null){
                    self.showTest(false);
                    $("#modal").modal("toggle");
                }
                else{
                    userClient.getTest((data) => {
                        if (data != null && data.length > 0) {
                            self.questions(self.shuffle(data));
                            self.currentQuestion(data[self.counter]);
                            self.counter++;
                            self.questionNumber(self.counter);
                        }
                    });
                }
                $("#loading").hide();
                $("#main-content").show();
            });
        };

        self.addToCounters = (answer) =>{
            switch (answer.learningType) {
                case 1:
                    self.answers.visual++;
                    break;

                case 2:
                    self.answers.auditivo++;
                    break;

                case 3:
                    self.answers.kinestesico++;
                    break;

            }
            if(self.counter < self.questions().length){
                self.currentQuestion(self.questions()[self.counter]);
            }
            self.counter++;
            self.questionNumber(self.counter);

            if(self.counter > 21){
                self.showTest(false);
                self.sendAnswers();
            }
        };

        self.sendAnswers = () =>{
            userClient.putAnswers(self.answers, (data) =>{
                self.model.data.learningType(data);
                $("#modal").modal("toggle");
            });
        };

        self.shuffle = (array) =>{
            let ctr = array.length, temp, index;
            while (ctr > 0) {
                index = Math.floor(Math.random() * ctr);
                ctr--;
                temp = array[ctr];
                array[ctr] = array[index];
                array[index] = temp;
            }
            return array;
        };

        self.init();
    };
    return TestViewModel;
});