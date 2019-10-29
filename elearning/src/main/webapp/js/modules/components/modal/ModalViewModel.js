define(["ko"], function (ko) {
    function ModalViewModel(options) {
        let self = this;
        self.title = ko.observable(options.title);
        self.course = options.course;
        self.handler = () =>{

        };
    }
    return ModalViewModel;
});