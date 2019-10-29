define([
    "ko",
    "jquery",
    "el/modules/client/CourseRestClient",
    "bootstrap"
], function (ko, $, courseClient) {
    function CourseViewModel() {
        let self = this;
        self.courses = ko.observableArray([]);
        self.course = ko.observable();

        self.init = () => {
            self.loadCourses();
        };

        self.loadCourses = () =>{
            courseClient.getAllCourses((data) =>{
                self.courses(data);
                $("#loading").hide();
                $("#main-content").show();
            });
        };

        self.enroll = () =>{
            self.courseViewModel.enrollCourse(self.session.idUser, $("#course").val(), self.password(), function(data){
                self.loadCourses();
            });
        };

        self.openModal = (course) => {
            self.course(course)
        };

        self.init();
    }
    return CourseViewModel;
});
