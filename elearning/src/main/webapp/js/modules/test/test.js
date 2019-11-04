define([
    "ko",
    "jquery",
    "cookie",
    "el/modules/client/UserRestClient",
    "bootstrap"
], function(ko, $, cookie, userClient){
    function TestViewModel(){
        let self = this;
        let session = JSON.parse(cookie.get("session"));
        self.questions = ko.observableArray([]);
        self.counters = {
            visual : 0,
            auditivo : 0,
            kinestesico : 0
        };
        self.counter = 0;
        self.questionNumber = ko.pureComputed(() => {
            return self.counter;
        });
        self.currentQuestion = ko.observable();

        self.init = () =>{
            self.loadTest();
        };

        self.loadTest = () => {
            userClient.getTest((data) =>{
                if(data != null && data.length > 0){
                    self.questions(data);
                    self.currentQuestion(data[self.counter]);
                }
                $("#loading").hide();
                $("#maint-content").show();
            });
        };

        self.addToCounters = (answer) =>{
            switch (answer.learningType) {
                case 1:
                    self.counters.visual++;
                    break;

                case 2:
                    self.counters.auditivo++;
                    break;

                case 3:
                    self.counters.kinestesico++;
                    break;

            }
            self.counter++;
            if(counter < self.questions().length){
                self.currentQuestion(self.questions()[self.counter]);
            }
        };

        self.sendCounters = () =>{
            userClient.sendResponses(self.counters);
        };

        self.init();
    };
    return TestViewModel;
});