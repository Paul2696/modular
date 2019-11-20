define([
    "ko",
    "jquery",
    "moment",
    "cookie",
    "el/modules/courses/Course",
    "el/modules/client/CourseRestClient",
    "el/modules/components/globalMessages/globalMessages",
    "el/modules/session/Session"
], function (ko, $, moment, cookie, Course, client, messages, session) {
    function CourseViewModel(){
        let self = this;
        self.start = ko.observable(new Date());
        self.courses = ko.observableArray([]);

        self.idUser = session.idUser;
        self.userType = session.userType;

        self.init = function() {
            self.populateCoursesTable();
        }

        self.populateCoursesTable = function () {
            client.getAllFromUser(self.idUser, self.userType, function(data){
                if(data != null && data.length > 0){
                    data.forEach(function (element) {
                        let course = Object.assign(new Course(), element);
                        course.hasPassword(course.password ? true :  false);
                        course.startObservable(new Date(course.start));
                        course.endObservable(new Date(course.end));
                        course.editable(false);
                        self.courses.push(course);
                    });
                }
                $("#loading").hide();
                $("#main-content").show();
            },self.error);
        };

        self.create = () => {
            self.courses.push(new Course(self.idUser));
        };

        self.createOrEdit = function (course, root) {
            if(course.editable()) {
                course.editable(false);
                course.start = moment(course.startObservable()).format("YYYY-MM-DD");
                course.end = moment(course.endObservable()).format("YYYY-MM-DD");
                if(course.idCourse != null) {
                    client.updateCourse(course, function (data) {
                        console.log(data);
                    });
                } else if(self.idCourse == null) {
                    client.createCourse(course, function (data) {
                        console.log(data);
                    });
                }
            } else {
                course.editable(true);
            }
        };

        self.remove = function (course) {
            if(confirm("Esta seguro de eliminar el curso")) {
                client.deleteCourse(course, function (res) {
                    console.log(res);
                    self.courses.remove(course);
                });
            }
        }

        self.init();
    };
    return CourseViewModel;
});