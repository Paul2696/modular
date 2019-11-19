define([
    "ko",
    "jquery",
    "cookie",
    "el/modules/client/CourseRestClient",
    "el/modules/client/UserRestClient",
    "text!modules/views/usersInCourse.html",
    "bootstrap"
], function (ko, $, cookie, courseClient, userClient, view) {
    function UsersListViewModel(options){
        let self = this;
        let session = JSON.parse(cookie.get("session"));
        self.course = options.course();
        self.usersList = ko.observableArray(options.course.users());
        self.user = ko.observable();
        self.view = view;

        self.loadProfile = (user) => {
                self.user(user);
                $("#modal").modal("toggle");
        };

        return UsersListViewModel;
    };
});