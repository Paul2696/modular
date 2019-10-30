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
        router: "lib/vanilla-router.min",
        popper: "lib/bootstrap/popper.min",
        bootstrap: "lib/bootstrap/bootstrap.min"
    }
});

requirejs([
    "require",
    "ko",
    "jquery",
    "el/modules/session/Session",
    "el/AppRouter",
    "el/modules/components/KoComponents"
], function (require, ko, $, session, Router) {
    let self = this;
    let s = session.testSession();
    self.mainViewModel = {
        router : Router,
        resource: ko.observable(),
        menu: session.getSessionMenu(s.userType),
        navHandler: function (element) {
            console.log(element);
            $("#main-content").hide();
            $("#loading").show();
            ko.cleanNode($("#main-content").get(0));
            $("#main-content").html("");
            self.mainViewModel.router.navigateTo(element);
            let module = self.mainViewModel.router.getModuleName(element);
            require(["require", module], (require) =>{
                let viewModel = require(module);
                ko.applyBindings(new viewModel(), $("#main-content").get(0));
            });
        }.bind(self)
    };
    $("#main-content").hide();
    ko.applyBindings(self.mainViewModel);
    self.mainViewModel.navHandler(window.location.hash);
});