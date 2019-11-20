define([
    "ko",
    "jquery",
    "el/modules/client/UserRestClient",
    "el/modules/session/Session",
    "bootstrap"
], function (ko, $, client, session) {
    function ChatClientViewModel() {
        let self = this;

        self.init = () => {

        };

        self.populateConversation = () => {
            client.getConversation(session.idUser, () =>{

            });
        };

        self.init();
    }

    return ChatClientViewModel;
});