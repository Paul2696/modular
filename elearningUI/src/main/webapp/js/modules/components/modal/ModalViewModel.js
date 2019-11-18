define(["ko", "jquery"], function (ko, $) {
    function ModalViewModel(options) {
        let self = this;
        self.title = ko.observable(options.title);
        self.view = options.view;
        self.data = options.model.data;
        self.handler = options.model.handler;
        self.btnStr = options.btnStr;
        $("#body").html(self.view);
    }
    return ModalViewModel;
});