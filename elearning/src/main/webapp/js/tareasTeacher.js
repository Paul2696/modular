$(document).ready(function() {
    //createTestSession();
    configDatePicker();
    moment.locale("es");
    ko.applyBindings(tareasTeacherViewModel);
    $("#course").change(function() {
       tareasTeacherViewModel.populateHomeworkTable($(this).val());
       tareasTeacherViewModel.currentCourse(parseInt($(this).val()));
    });
    tareasTeacherViewModel.init();
});

var tareasTeacherViewModel = {
    active: "Tareas",
    menu: ko.observableArray([]),
    homework: ko.observableArray(),
    homeworkViewModel: HomeworkViewModel,
    courseViewModel: CourseViewModel,
    courses: ko.observableArray(),
    currentCourse: ko.observable(),
    session: parseSession(Cookies.getJSON("session")),
    responses: ko.observableArray([]),
    message: ko.observable(),
    status: ko.observable(),

    /**
     *   function to initialize page data
     */
    init: function() {
        var self = this;
        if(self.session && self.session.isSessionActive()) {
            self.menu(self.session.getSessionMenu());
            self.courseViewModel.getCourses(self.session.getToken(), function(data){
              self.courses(data.professor);
              self.currentCourse($("#course").val());
              self.populateHomeworkTable($("#course").val());
          });

        } else {
            window.location.href = "/webapp/html/log_in.html";
        }
    },
    /**
     *   function to populate the homework table
     */
    populateHomeworkTable: function(course_id) {
        var self = this;
        self.homeworkViewModel.getHomework(course_id, self.session.getToken(), function(data) {
            if(data != null){
                for(var i = 0; i < data.homework.length; i++){
                    data.homework[i]["limitObservable"] = ko.observable(new Date(data.homework[i].limit));
                    data.homework[i]["editable"] = ko.observable(false);
                    data.homework[i]["file"] = ko.observable();
                    data.homework[i]["setFile"] = function(data, e){
                        var hw = this;
                        var file = e.target.files[0];
                        hw.file(file);
                    };
                    data.homework[i]["editTextFields"] = function() {
                        if(this.editable()) {
                            this.editable(!this.editable());
                            this.limit = this.limitObservable();

                            self.homeworkViewModel.updateHomework(this, self.session.getToken(), function(data){
                                self.status(data.code);
                                if(data.code == 200){
                                    self.message("La tarea se ha actualizado correctamente");
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
                        data.homework[i]["remove"] = function() {
                            var homework = this;
                             self.homeworkViewModel.deleteHomework(homework.id, self.session.getToken(), function(data){
                                   self.status(data.code);
                                   if(data.code == 200){
                                      self.homework.remove(homework);
                                       self.message("La tarea se ha eliminado correctamente");
                                       $("#alert").show();
                                   }
                                   else{
                                       self.message("Algo ha salido mal");
                                       $("#alert").show();
                                   }
                             });
                        };
                    //delete data.homework[i].responses;
                    if(data.homework[i].responses != null && data.homework[i].responses.length > 0){
                        data.homework[i]["calificar"] = function(){
                            self.responses(this.responses);
                            $("#modalCalificar").modal("show");
                            $("#modalCalificar").on("hide.bs.modal", function(event){
                                self.setGrades(event);
                            });
                        }
                    }
                    else{
                        data.homework[i]["calificar"] = null;
                    }
                }

                self.homework(data.homework);
            }

        });
    },

    save: function() {
        var self = this;
        self.homeworkViewModel.update()
    },

    create: function() {
        var self = this;
        var hw = {
            title: "",
            course: self.currentCourse(),
            limit: new Date(),
            description: "",
            //tipo: "",
            closed: false,
            calificar: null,
            file: ko.observable(),
            editable: ko.observable(true),
            setFile: function(data, e){
                var hw = this;
                var file = e.target.files[0];
                hw.file(file);
            },
            editTextFields: function() {
                if(this.editable()) {
                    var tarea = this;
                    this.editable(!this.editable());
                    this.limit = this.limitObservable();
                    self.homeworkViewModel.createHomework(this, self.session.getToken(), function(data){
                        self.status(data.code);
                        if(data.code == 200){
                            self.message("La tarea se ha creado correctamente");
                            $("#alert").show();
                            tarea.id = data.homework.id;
                            self.populateHomeworkTable(self.currentCourse());
                        }
                        else{
                            self.message("Algo ha salido mal");
                            $("#alert").show();
                        }

                    }, this.file());
                } else {
                    this.editable(!this.editable());
                }
            },
            remove: function() {
                 var homework = this;
                 self.homeworkViewModel.deleteHomework(homework.id, self.session.getToken(), function(data){
                       self.homework.remove(homework);
                 });
            }
        };
        hw.limitObservable = ko.observable(hw.limit);
        self.homework.push(hw);
    },

    setGrades: function(event){
        var self = this;
        if($(document.activeElement)[0] == $("#send")[0]){
            var grades = [];
            self.responses().forEach(function(grade){
               if(grade != null){
                grades.push({
                    id: grade.id,
                    "grade": grade.grade
                });
               }
            });
            self.homeworkViewModel.sendGrades(grades, self.session.getToken(), function(){
                //TODO: IGual y se me ocurre algo
            });
        }

    },

    destroySession: function(){
        var self = this;
        self.session.destroySession();
    }

};

function createTestSession() {
    return testSession();
}