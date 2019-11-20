define([
    "ko",
    "jquery",
    "cookie",
    "moment",
    "el/modules/client/CourseRestClient",
    "el/modules/session/Session",
    "text!modules/views/enrollView.html",
    "bootstrap"
], function (ko, $, cookie, moment, courseClient, session, enrollView) {
    function CourseViewModel() {
        let self = this;

        self.idUser = session.idUser;
        self.courses = ko.observableArray([]);
        self.course = ko.observable();
        self.password = ko.observable();

        self.view = enrollView;
        self.model = {
            data : {
                password: self.password
            },
            handler: () => {
                self.enroll();
            }
        };

        self.init = () => {
            self.loadCourses();
        };

        self.loadCourses = () =>{
            courseClient.getAllCourses((data) =>{
                if(data != null && data.length > 0) {
                    data.forEach((element) =>{
                        element.startFormatted = moment(element.start).format("YYYY-MM-DD");
                        element.endFormatted = moment(element.end).format("YYYY-MM-DD");
                    });
                    self.courses(data);
                }
                $("#loading").hide();
                $("#main-content").show();
            });
        };

        self.enroll = () =>{
            courseClient.enrollCourse(self.idUser, self.course(), self.password(), (data) =>{
                //self.loadCourses();
            });
        };

        self.openModal = (course) => {
            self.course(course);
            if(!course.needsPassword){
                self.enroll();
            } else {
                $("#modal").modal("toggle");
            }

        };

        self.init();
    }
    return CourseViewModel;
});
