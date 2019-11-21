define(["ko"], function (ko) {
    function Conversation() {
        let self = this;

        self.users = [];
        self.conversation = [];
        self.active = ko.observable();
    };
    return Conversation;
});