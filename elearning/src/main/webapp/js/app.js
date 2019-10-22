requirejs.config({
    baseUrl : "js",
    paths : {
        el: ".",
        jquery: "lib/jquery-3.3.1.min",
        cookie: "lib/js.cookie",
        moment: "lib/moment-with-locales.min",
        ko: "lib/knockout-3.4.2",
        paging: "lib/knockout-paging",
        "jquery-ui" : "lib/jquery-ui/jquery-ui",
        text: "lib/text",
        bootstrap: "lib/bootstrap/bootstrap.min",
        router: "lib/vanilla-router.min"
    }
});

requirejs([
    "ko",
    "el/modules/session/Session",
    "el/AppRouter",
    "el/modules/components/KoComponents"],function (ko, session, Router) {
    let self = this;
    let s = session.testSession();
    self.mainViewModel = {
        router : Router,
        resource: ko.observable(),
        viewModel: null,
        menu: session.getSessionMenu(s.userType),
        navHandler: function (element) {
            console.log(element);
            self.mainViewModel.router.navigateTo(element.path);
        }.bind(self)
    };
    ko.applyBindings(self.mainViewModel);
    self.mainViewModel.router.refresh();
});