define([
    "ko"
], function (ko){
    ko.components.register("nav-bar", {
        viewModel: {require: "modules/components/navBar/NavBarViewModel"},
        template: {require: "text!modules/components/navBar/navBar.html"}
    });

    return ko.components;
});