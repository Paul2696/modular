define([
    "ko",
    "jquery",
    "moment"
], function (ko, $, moment) {

});
const coursesTeacherViewModel = {
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
        self.courseViewModel.getAllFromUser(self.session.idUser, self.session.userType, function(data){
            if(data != null && data.length > 0){
                 for(let i = 0; i < data.length; i++){
                    data[i]["editable"] = ko.observable(false);
                    data[i]["startObservable"] = ko.observable(data[i].start);
                    data[i]["endObservable"] = ko.observable(data[i].end);
                    data[i]["hasPassword"] = ko.observable(data[i].password ? true:false);
                    data[i]["editTextFields"] = self.editCourse;
                    data[i]["remove"] = self.removeCourse;
                }
                self.courses(data);
            }
        },self.error);
    },

    create: function() {
        let self = this;
        let c = {
            name: "",
            user: { idUser : self.session.idUser},
            start: new Date(),
            end: new Date(),
            hasPassword : ko.observable(false),
            password: "",
            editable: ko.observable(true),
            editTextFields: self.editCourse,
            remove: self.removeCourse
        };
        c.startObservable = ko.observable(c.start);
        c.endObservable = ko.observable(c.end);
        self.courses.push(c);
    },
     destroySession: function(){
         let self = this;
         self.session.destroySession();
     },

    editCourse: function(course, root) {
        if(course.editable()) {
            let success = root.success.bind(root);
            let error = root.error.bind(root);
            course.editable(!course.editable());
            course.start = course.startObservable();
            course.end = course.endObservable();
            course.start = moment(course.start).format("YYYY-MM-DD");
            course.end = moment(course.end).format("YYYY-MM-DD");
            if(course.idCourse != null) {
                root.courseViewModel.updateCourse(course, success, error);
            } else if(course.idCourse == null) {
                root.courseViewModel.createCourse(course, success, error);
            }
        } else {
            course.editable(!course.editable());
        }
    },
    removeCourse : function(course, root) {
        if(confirm("Esta seguro de eliminar el curso")) {
            root.courseViewModel.deleteCourse(course.idCourse, function (data) {
                root.status(parseInt(data));
                if (parseInt(data) === 200) {
                    root.message("Se ha elminado correctamente");
                    $("#alert").show();
                    root.courses.remove(course);
                } else {
                    root.message("Ocurrio un error en el servidor");
                    $("#alert").show();
                }
            });
        }
    },
     success: function(data, course, message) {
         let self = this;
         self.status(200);
         self.message(message);
         $("#alert").show();
         self.populateCoursesTable();
     },

    error: function (data) {
        let self = this;
        //self.courses.pop();
        self.status(data.status);
        self.message("Algo ha salido mal: " + data.responseText);
        $("#alert").show();
    }


};