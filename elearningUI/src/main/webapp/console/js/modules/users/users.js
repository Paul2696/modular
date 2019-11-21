define([
    "ko",
    "jquery",
    "cookie",
    "el/modules/client/CourseRestClient",
    "el/modules/client/UserRestClient",
    "text!modules/views/userProfile.html",
    "el/modules/session/Session",
    "bootstrap"
], function (ko, $, cookie, courseClient, userClient, view, session) {
    function UsersListViewModel(){
        let self = this;
        self.currentCourse = ko.observable({
            name : "Cursos"
        });
        self.courses = ko.observableArray([]);
        self.idUser = session.idUser;
        self.usersList = ko.observableArray([]);

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
                    self.currentCourse(data.courses[0]);
                    self.updateList(self.currentCourse());
                }
                $("#loading").hide();
                $("#main-content").show();
            });
        };

        self.updateList = (course) => {
            courseClient.getCourses(course.idCourse, (data) => {
                self.currentCourse(course);
                self.usersList(data.users);
            });
        };

        self.loadProfile = (user) => {
            self.model.data.user([user]);
            $("#modal").modal("toggle");
        };

        self.init();

    };
    return UsersListViewModel;
});