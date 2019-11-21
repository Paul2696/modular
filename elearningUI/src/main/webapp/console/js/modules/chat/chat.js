define([
    "ko",
    "jquery",
    "el/modules/client/UserRestClient",
    "el/modules/chat/Conversation",
    "el/modules/chat/Message",
    "el/modules/session/Session",
    "bootstrap"
], function (ko, $, client, Conversation, Message, session) {
    function ChatClientViewModel() {
        let self = this;
        self.idUser = session.idUser;
        self.name = ko.observable();
        self.users = ko.observableArray([]);
        self.receiver = ko.observable();
        self.conversations = ko.observableArray([]);
        self.conversation = ko.observableArray([]);
        self.currentConversation = ko.observable();
        self.message = ko.observable();

        self.init = () => {
            self.getUsers();
            self.populateConversation();
            self.listenForMessages();
        };

        self.populateConversation = () => {
            client.getAllConversations(session.idUser, (data) =>{
                data.forEach((element)=>{
                    let filtered = element.users.filter((value, index, arr)=>{
                        if(value.idUser != session.idUser) {
                            self.name(value.name);
                        }
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
                data = data.filter((value) =>{
                    return value.idUser != self.idUser;
                });
                self.users(data);
            });
        };

        self.createConversation= (receiver) => {
            self.receiver(receiver);
            let conversation = self.conversationExists(receiver);
            if(!conversation) {
                let conversation = new Conversation();
                conversation.users.push(receiver);
                self.conversations.push(conversation);
            }
            self.loadConversation(conversation);
        };

        self.loadConversation = (conversation) =>{
            if(self.currentConversation() != null) {
                self.currentConversation().active(false);
            }
            self.currentConversation(conversation);
            conversation.active(true);
            self.receiver(conversation.users[0]);
            self.conversation(conversation.conversation);
        };

        self.sendMessage = () =>{
            let message = new Message();
            message.idSender = self.idUser;
            message.message = self.message();
            message.sender = self.name;
            message.receiver = self.receiver().name;
            client.sendMessage(self.idUser, self.receiver().idUser, self.message(), (data) =>{
                self.conversation.push(message);
                console.log(data);
                self.message("");
            });
        };

        self.conversationExists = (receiver) => {
            let result = false;
            self.conversations().forEach((element) => {
                  if(element.users[0].idUser == receiver.idUser) {
                      result = element;
                  }
            });
            return result;
        };
        self.listenForMessages = () => {
          setInterval(self.retrieveNewMessages, 2500);
        };

        self.retrieveNewMessages = () =>{
            client.getNewMessage(self.idUser, (data) =>{
                console.log(data)
            })
        };

        self.init();
    }

    return ChatClientViewModel;
});