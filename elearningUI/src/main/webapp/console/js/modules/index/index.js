define([
    "ko",
    "jquery",
    "moment",
    "cookie",
    "el/modules/session/Session",
    "bootstrap"
], function (ko, $, moment, cookie, session) {
    function IndexViewModel(){
        self.init = function() {
            self.index();
        }

        self.index = function () {
            $("#loading").hide();
            $("#main-content").show();
        };


        self.init();
    };
    return IndexViewModel;
});