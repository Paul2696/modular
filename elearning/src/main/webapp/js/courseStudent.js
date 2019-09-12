$(document).ready(function(){
    coursesStudentViewModel.init();
    ko.applyBindings(coursesStudentViewModel);
});

let coursesStudentViewModel = {
    active : "Cursos",
    menu : ko.observableArray([]),
    courses : ko.observableArray([]),
    courseViewModel : CourseViewModel,
    professor : ko.observable(),
    start : ko.observable(),
    end : ko.observable(),
    needsPassword : ko.observable(false),
    password : ko.observable(""),
    session: parseSession(Cookies.getJSON("session")),

    init: function() {
            let self = this;
            //if(self.session && self.session.isSessionActive()) {
                testSession();
                self.menu(self.session.getSessionMenu());
                self.loadCourses();
            //} else {
            //    window.location.href = "/webapp/html/log_in.html";
            //}
        },

        loadCourses: function(){
          let self = this;
          self.courseViewModel.getAllCourses(function(data){
                if(data != null && data.length > 0){
                  self.professor(data[0].user.name);
                  self.start(data[0].start);
                  self.end(data[0].end);
                  self.courses(data);
                  self.needsPassword(data[0].password);
                }
          });
          $("#course").change(function(){
              let courseId = $("#course").val();
              for(let i = 0; i < self.courses().length; i++){
                  if(self.courses()[i].id == courseId){
                      self.professor(self.courses()[i].user.name);
                      self.start(self.courses()[i].start);
                      self.end(self.courses()[i].end);
                      self.needsPassword(self.courses()[i].password);
                      return;
                  }
              }
          });
        },



    enroll: function(){
        let self = this;
        self.courseViewModel.enrollCourse(self.session.idUser, $("#course").val(), self.password(), function(data){
            self.loadCourses();
        });
    },

    destroySession: function(){
        let self = this;
        self.session.destroySession();
    }
};