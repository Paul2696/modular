define([
    "ko",
    "jquery",
    "el/modules/client/UserRestClient",
    "el/modules/session/Session",
    "bootstrap"
], function (ko, $, client, session) {
    function ChatClientViewModel() {
        let self = this;
        self.idUser = session.idUser;
        self.users = ko.observableArray([]);
        self.receiver = ko.observable();
        self.conversations = ko.observableArray([]);
        self.conversation = ko.observableArray([]);
        self.currentConversation = ko.observable();
        self.message = ko.observable();

        self.init = () => {
            self.getUsers();
            self.populateConversation();
        };

        self.populateConversation = () => {
            client.getAllConversations(session.idUser, (data) =>{
                data.forEach((element)=>{
                    let filtered = element.users.filter((value, index, arr)=>{
                        return value.idUser != session.idUser;
                    });
                    element.users = filtered;
                    element.active = ko.observable(false);
                });
                self.conversations(data);
                $("#loading").hide();
                $("#main-content").show();
            });
        };

        self.getUsers = () =>{
            client.getUsers((data) =>{
                self.users(data);
            });
        };

        self.createConversation= (receiver) => {
            self.receiver(receiver);
        };

        self.loadConversation = (conversation) =>{
            self.currentConversation().active(false);
            self.currentConversation(conversation);
            conversation.active(true);
            self.receiver(conversation.users[0]);
            self.conversation(conversation.conversation);
        };

        self.sendMessage = (conversation) =>{
            client.sendMessage(self.idUser, self.receiver().idUser, self.message(), (data) =>{
                console.log(data)
            });
        };

        self.init();
    }

    return ChatClientViewModel;
});