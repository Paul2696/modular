define([
    "ko",
    "jquery",
    "el/modules/client/CourseRestClient"
], function (ko, $, courseClient) {
    function HomeworkViewModel() {
        let self = this;
        self.homework = ko.observableArray();
        self.courses = ko.observableArray();
        self.file = ko.observable();
        self.uploaded = ko.observable(false);


        self.init = () => {
            self.loadCourses();
        };

        self.loadCourses = () =>{
            courseClient.getUser(function(data) {
                if(data.courses != null && data.courses.length > 0){
                    self.courses(data.courses);
                    self.populateHomeworkTable(data.courses[0]);
                }
            });
        };

        /**
         *   function to populate the homework table
         */
        self.populateHomeworkTable = (homework) =>{
            homework.forEach(function(element){
                element.gradeObservable = ko.observable("-");
                element.uploaded = ko.observable(false);
                element.gradedObservable = ko.observable(false);
                if(element.response != null &&  element.response.length > 0){
                    element.gradedObservable(element.response[0].graded);
                    if(element.response[0].graded){
                        element.gradeObservable(element.response[0].grade);
                    }
                    element.uploaded(true);
                    self.uploaded(true);
                }
                element.openModal = function(){
                    if(element.response != null && element.response.length > 0){
                        self.content(element.response[0].answer);
                        self.file(element.response[0].file);
                    }
                    $("#modalEnroll").modal("show");
                    self.uploaded(element.uploaded());
                    $("#modalEnroll").on("hide.bs.modal", function(){
                        self.send(element);
                        $("#modalEnroll").unbind("hide.bs.modal");
                    });
                }
            });
            self.homework(data.homework);
        },

        self.updateTable = (homework, event) =>{
            if(event.originalEvent){
                populateHomeworkTable(homework);
            }
        },

        self.send = (data) =>{
            if($(document.activeElement)[0] == $("#send")[0]){
                let archivo = $("#fileupload").prop("files")[0];
                if(data.uploaded()){
                    self.homeworkViewModel.updateStudentHomework(data.id, self.content(), true, archivo, self.session.getToken(), function(){
                        self.cleanObservables();
                        self.loadCourses();
                    });
                }
                else{
                    self.homeworkViewModel.sendHomework(data.id, self.content(), true, self.session.getToken(), archivo, function(){
                        self.cleanObservables();
                        self.loadCourses();
                    });
                }
            }
        }
    }
    return HomeworkViewModel;
});