define([
    "ko",
    "el/modules/components/datepicker/datePickerConfig"
], function (ko){
    ko.components.register("nav-bar", {
        viewModel: {require: "modules/components/navBar/NavBarViewModel"},
        template:  {require: "text!modules/components/navBar/navBar.html"}
    });
    ko.components.register("modal", {
        viewModel: {require: "modules/components/modal/ModalViewModel"},
        template:  {require: "text!modules/components/modal/modal.html"}
    });
    ko.components.register("usersModal", {
       viewModel: {require: "modules/components/users/users"},
       template: {require: "text!modules/components/modal/users.html"}
    });

    return ko.components;
});