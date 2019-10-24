define([
    "ko",
    "moment",
    "el/modules/client/CourseRestClient"
],function (ko, moment, client) {
    function Course(idUser) {
        let self = this;
        self.idCourse = null;
        self.name = "";
        self.user = {"idUser": idUser};
        self.start = new Date();
        self.end = new Date();
        self.password = "";
        self.editable = ko.observable(true);
        self.hasPassword = ko.observable(false);
        self.startObservable = ko.observable(self.start);
        self.endObservable = ko.observable(self.end);
    }
    return Course;
});