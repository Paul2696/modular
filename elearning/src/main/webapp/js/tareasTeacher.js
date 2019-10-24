let tareasTeacherViewModel = {
    active: "Tareas",
    menu: ko.observableArray([]),
    homework: ko.observableArray([]),
    homeworkViewModel: HomeworkViewModel,
    courseViewModel: CourseRestClient,
    courses: ko.observableArray(),
    currentCourse: ko.observable(),
    session: testSession(),//parseSession(Cookies.getJSON("session")),
    responses: ko.observableArray([]),
    message: ko.observable(),
    status: ko.observable(),

    /**
     *   function to initialize page data
     */
    init: function() {
        let self = this;
        //if(self.session && self.session.isSessionActive()) {
            self.menu(self.session.getSessionMenu());
            self.courseViewModel.getAllFromUser(self.session.idUser,self.session.userType, function(data){
              data = JSON.parse(data);
              self.courses(data);
              if(data.length > 0) {
                  self.currentCourse(data[0].idCourse);
                  self.homework(data[0].homework);
              }
          }, self.error);

        //} else {
        //    window.location.href = "/webapp/html/log_in.html";
        //}
    },
    /**
     *   function to populate the homework table
     */
    populateHomeworkTable: function(homework) {
        let self = this;
        for(let i = 0; i < homework.length; i++){
            homework[i]["limitObservable"] = ko.observable(new Date(data.homework[i].limit));
            homework[i]["editable"] = ko.observable(false);
            homework[i]["file"] = ko.observable();
            homework[i]["setFile"] = function(data, e){
                let hw = this;
                let file = e.target.files[0];
                hw.file(file);
            };
            homework[i]["editTextFields"] = function() {
                if(this.editable()) {
                    this.editable(!this.editable());
                    this.limit = this.limitObservable();

                    self.homeworkViewModel.updateHomework(this, self.session.getToken(), function(data){
                        self.status(data.code);
                        self.message("La tarea se ha actualizado correctamente");
                        $("#alert").show();
                    });
                } else {
                    this.editable(!this.editable());
                }
            };
            homework[i]["remove"] = function() {
                let homework = this;
                self.homeworkViewModel.deleteHomework(homework.idHomework, function(data){
                    self.status(data.code);
                    self.homework.remove(homework);
                    self.message("La tarea se ha eliminado correctamente");
                    $("#alert").show();
                }, self.error);
            };
            //delete data.homework[i].responses;
            if(homework[i].responses != null && homework[i].responses.length > 0){
                homework[i]["calificar"] = function(){
                    self.responses(this.responses);
                    $("#modalCalificar").modal("show");
                    $("#modalCalificar").on("hide.bs.modal", function(event){
                        self.setGrades(event);
                    });
                }
            }
            else{
                homework[i]["calificar"] = null;
            }
        }
    },

    save: function() {
        let self = this;
        self.homeworkViewModel.update()
    },

    create: function() {
        let self = this;
        let hw = {
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
                let hw = this;
                let file = e.target.files[0];
                hw.file(file);
            },
            editTextFields: function() {
                if(this.editable()) {
                    let tarea = this;
                    this.editable(!this.editable());
                    this.limit = this.limitObservable();
                    self.homeworkViewModel.createHomework(this,function(data){
                        self.status(data);
                        self.message("La tarea se ha creado correctamente");
                        $("#alert").show();
                        tarea.idHomework = data;
                        self.populateHomeworkTable(self.currentCourse());

                    }, this.file(), self.error);
                } else {
                    this.editable(!this.editable());
                }
            },
            remove: function() {
                 let homework = this;
                 self.homeworkViewModel.deleteHomework(homework.id, self.session.getToken(), function(data){
                       self.homework.remove(homework);
                 });
            }
        };
        hw.limitObservable = ko.observable(hw.limit);
        self.homework.push(hw);
    },

    setGrades: function(event){
        let self = this;
        if($(document.activeElement)[0] == $("#send")[0]){
            let grades = [];
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
        let self = this;
        self.session.destroySession();
    },

    error: function (data) {
        let self = this;
        self.status(data.status);
        self.message("Algo ha salido mal: " + data.responseText);
        $("#alert").show();
    }

};
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


function createTestSession() {
    return testSession();
}