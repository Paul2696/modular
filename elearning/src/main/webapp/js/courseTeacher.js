$(document).ready(function(){
    configDatePicker();
    moment.locale("es");
    ko.applyBindings(coursesTeacherViewModel);
    coursesTeacherViewModel.init();
});

let coursesTeacherViewModel = {
    active : "Cursos",
    menu : ko.observableArray([]),
    courses : ko.observableArray([]),
    courseViewModel : CourseViewModel,
    session: null,
    status: ko.observable(),
    message: ko.observable(),

    init: function() {
            let self = this;
            //if(self.session && self.session.isSessionActive()) {
                self.session = testSession();
                self.menu(self.session.getSessionMenu());
                self.populateCoursesTable();
            //} else {
            //    window.location.href = "/webapp/html/log_in.html";
            //}
        },
    populateCoursesTable: function(){
        let self = this;
        self.courseViewModel.getAllFromUser(self.session.idUser, function(data){
            if(data != null){
                 for(let i = 0; i < data.length; i++){
                    data[i]["editable"] = ko.observable(false);
                    data[i]["startObservable"] = ko.observable(new Date(data[i].start.replace("-", "/")));
                    data[i]["endObservable"] = ko.observable(new Date(data[i].end.replace("-", "/")));
                    data[i]["password"] = ko.observable(data[i].password);
                    data[i]["editTextFields"] = function() {
                        if(this.editable()) {
                            this.editable(!this.editable());
                            this.start = this.startObservable();
                            this.end = this.endObservable();
                            this.start = moment(this.start).format("YYYY-MM-DD");
                            this.end = moment(this.end).format("YYYY-MM-DD");
                            self.courseViewModel.updateCourse(this, function(data){
                                self.status(data.code);
                                if(data.code == 200){
                                    self.message("Se ha modificado correctamente el curso");
                                    $("#alert").show();
                                }
                                else{
                                    self.message("Algo ha salido mal");
                                    $("#alert").show();
                                }
                            });
                        } else {
                            this.editable(!this.editable());
                        }
                    };
                    data[i]["remove"] = function() {
                        let course = this;
                         self.courseViewModel.deleteCourse(course.idCourse, function(data){
                               self.status(data.code);
                               if(data.code == 200){
                                   self.message("Se ha elminado correctamente el curso");
                                    $("#alert").show();
                                    self.courses.remove(course);
                               }
                               else{
                                    self.message("Algo ha salido mal");
                                    $("#alert").show();
                               }
                         });
                    };
                }
                self.courses(data);
            }
        });
    },

    create: function() {
        let self = this;
        let c = {
            name: "",
            start: new Date(),
            end: new Date(),
            password: "",
            editable: ko.observable(true),
            editTextFields: function() {
                if(this.editable()) {
                    let course = this;
                    this.editable(!this.editable());
                    this.start = this.startObservable();
                    this.end = this.endObservable();
                    this.start = moment(this.start).format("YYYY-MM-DD");
                    this.end = moment(this.end).format("YYYY-MM-DD");
                    self.courseViewModel.createCourse(this, function(data){
                        self.status(data.code);
                        if(data.code == 400 || data.code == 500){
                            self.message("Algo ha salido mal");
                            $("#alert").show();
                        }
                        else if(data.code == 200){
                           course.idCourse = data;
                           self.message("Se ha creado el curso correctamente");
                            $("#alert").show();
                        }
                    });
                } else {
                    this.editable(!this.editable());
                }
            },
            remove: function() {
                let course = this;
                 self.courseViewModel.deleteCourse(course.idCourse,  function(data){
                       self.courses.remove(course);
                 });
            }


        };
        c.startObservable = ko.observable(c.start);
        c.endObservable = ko.observable(c.end);
        self.courses.push(c);
    },
     destroySession: function(){
         let self = this;
         self.session.destroySession();
     }
};