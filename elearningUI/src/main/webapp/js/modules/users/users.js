define([
    "ko",
    "jquery",
    "cookie",
    "el/modules/client/CourseRestClient",
    "el/modules/client/UserRestClient",
    "text!modules/views/userProfile.html",
    "bootstrap"
], function (ko, $, cookie, courseClient, userClient, view) {
    function UsersListViewModel(){
        let self = this;
        let session = JSON.parse(cookie.get("session"));
        self.course = ko.observable();
        self.courses = ko.observableArray([]);
        self.idUser = session.idUser;
        self.usersList = ko.observableArray();

        self.view = view;
        self.model = {
            data: {
                user: ko.observableArray([])
            },
            handler: () => {

            }
        };

        self.init = () =>{
            self.loadCourses();
        };

        self.loadCourses = () =>{
            userClient.getUser(self.idUser,  (data) =>{
                if(data.courses != null && data.courses.length > 0){
                    self.courses(data.courses);
                    self.course(data.courses[0]);
                }
                $("#loading").hide();
                $("#main-content").show();
            });
        };

        self.updateList = (courses) => {
            courseClient.getCourses(courses.idCourse, (course) => {
                self.usersList([course.users]);
            });
        };

        self.loadProfile = (user) => {
            self.data.user([user]);
            $("#modal").modal("toggle");
        };

        self.init();

    };
    return UsersListViewModel;
});