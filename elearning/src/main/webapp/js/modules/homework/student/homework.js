$(document).ready(function() {
    //createTestSession();
    configDatePicker();
    ko.applyBindings(tareasStudentViewModel);
    tareasStudentViewModel.init();
});

let tareasStudentViewModel = {
    active: "Tareas",
    menu: ko.observableArray([]),
    homework: ko.observableArray(),
    homeworkViewModel: HomeworkRestClient,
    content: ko.observable(""),
    courseViewModel: CourseRestClient,
    courses: ko.observableArray(),
    file: ko.observable(),
    uploaded: ko.observable(false),
    session: parseSession(Cookies.getJSON("session")),

    /**
     *   function to initialize page data
     */
    init: function() {
        let self = this;
        //if(self.session && self.session.isSessionActive()) {
            self.menu(self.session.getSessionMenu());
            self.loadCourses();
        //} else {
        //    window.location.href = "/webapp/html/log_in.html";
        //}
    },

    loadCourses: function(){
        let self = this;
         self.courseViewModel.getUser(function(data) {
            if(data.courses != null && data.courses.length > 0){
                self.courses(data.courses);
                self.populateHomeworkTable(data.courses[0]);
            }
        });
    },

    /**
     *   function to populate the homework table
     */
    populateHomeworkTable: function(homework) {
        let self = this;
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

    updateTable : function(homework, event){
        if(event.originalEvent){
            populateHomeworkTable(homework);
        }
    },

    send: function(data){
        let self = this;
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
    },
    destroySession: function(){
        let self = this;
        self.session.destroySession();
    },

    cleanObservables: function(){
        tareasStudentViewModel.content("");
        tareasStudentViewModel.file(null);
        tareasStudentViewModel.uploaded(false);
    }


};

function createTestSession() {
    return testSession();
}