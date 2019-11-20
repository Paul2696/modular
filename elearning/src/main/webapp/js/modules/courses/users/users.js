define([
    "ko",
    "jquery",
    "cookie",
    "el/modules/client/CourseRestClient",
    "el/modules/client/UserRestClient",
    "text!modules/views/usersInCourse.html",
    "bootstrap"
], function (ko, $, cookie, courseClient, userClient, view) {
    function UsersListViewModel(){
        let self = this;
        let session = JSON.parse(cookie.get("session"));
        self.usersList = ko.observableArray([]);
        self.user = ko.observable();
        self.view = view;

        self.init = () => {
            self.loadUsers();
        };

        self.loadUsers = () => {
            courseClient.getAllUsersFromCourse((1), (data) => {
                if(data != null && data.length > 0){
                    self.usersList(data);
                }
                $("#loading").hide();
                $("#main-content").show();
            });
        };

        self.loadProfile = (user) => {
                self.user(user);
                $("#modal").modal("toggle");
        };

        self.init();

        return UsersListViewModel;
    };
});