define(["ko"], function (ko) {
    function Homework(course) {
        let self = this;
        self.name = "";
        self.course = course;
        self.description = "";
        self.end = new Date();
        self.responses = [];
        self.endObservable = ko.observable(new Date());
        self.editable = ko.observable(true);
        self.file = ko.observable(null);
        self.hasResponse = ko.observable(false);

        self.setFile = (data, e) => {
            self.file(e.target.files[0]);
        };
    };
    return Homework;
});