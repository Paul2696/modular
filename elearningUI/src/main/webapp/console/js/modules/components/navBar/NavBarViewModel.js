define([
    "ko"
], function (ko){
    function NavigationBarViewModel(options) {
        let self = this;
        self.menu = ko.observableArray(options.menu);
        self.selected = ko.observable(options.menu[0].text);

        self.setMenu = function(menu) {
            self.menu(menu);
        };

        self.setSelected = function (selected) {
            self.selected(selected);
        };
        self.navBarHandler = function (element) {
            self.selected(element.text);
            options.handler(element.path);
        };
    }
    return NavigationBarViewModel;
});