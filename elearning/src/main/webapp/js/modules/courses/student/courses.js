define([
    "ko",
    "jquery",
    "cookie",
    "moment",
    "el/modules/client/CourseRestClient",
    "text!modules/views/enrollView.html",
    "bootstrap"
], function (ko, $, cookie, moment, courseClient, enrollView) {
    function CourseViewModel() {
        let self = this;
        let session = JSON.parse(cookie.get("session"));
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
                        element.hasPassword = element.password ? true : false;
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
            if(!course.hasPassword){
                self.enroll();
            } else {
                $("#modal").modal("toggle");
            }

        };

        self.init();
    }
    return CourseViewModel;
});
