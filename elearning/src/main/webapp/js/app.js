requirejs.config({
    baseUrl : "js",
    paths : {
        el: ".",
        jquery: "lib/jquery-3.3.1.min",
        cookie: "lib/js.cookie",
        moment: "lib/moment-with-locales.min",
        ko: "lib/knockout-latest",
        paging: "lib/knockout-paging",
        "jquery-ui" : "lib/jquery-ui/jquery-ui",
        text: "lib/text",
        bootstrap: "lib/bootstrap/bootstrap.min",
        router: "lib/vanilla-router.min"
    }
});

requirejs([
    "require",
    "ko",
    "jquery",
    "el/modules/session/Session",
    "el/AppRouter",
    "el/modules/components/KoComponents"],function (require, ko, $, session, Router) {
    let self = this;
    let s = session.testSession();
    self.mainViewModel = {
        router : Router,
        resource: ko.observable(),
        menu: session.getSessionMenu(s.userType),
        navHandler: function (element) {
            console.log(element);
            $("#loading").show();
            let module = self.mainViewModel.router.getModuleName(element.path);
            self.mainViewModel.router.navigateTo(element.path);
            require(["require", module], function () {
                let viewModel = require(module);
                ko.cleanNode($("#main-content").get(0));
                ko.applyBindings(new viewModel(), $("#main-content").get(0));
                $("#loading").hide();
            });
        }.bind(self)
    };
    ko.applyBindings(self.mainViewModel);
    self.mainViewModel.router.refresh();
});