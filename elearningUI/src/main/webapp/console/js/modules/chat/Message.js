define(["ko"], function (ko) {
    function Message() {
        let self = this;

        self.date = new Date();
        self.idSender = null;
        self.message = null;
        self.receiver = null;
        self.sender = null;
    };
    return Message;
});