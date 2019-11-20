define(["el/modules/client/Request"], function (request) {
    let path = "elearning/api/user";
    let UserRestClient = {
        getUser : (idUser, callback) => {
            request.get(path, [idUser],callback);
        },

        getUsers : (callback) => {
            request.get(path, [],callback);
        },

        updateUser : (user, callback) => {
            let data = JSON.parse(JSON.stringify(user));
            data = JSON.stringify(data);
            request.put("elearning/api/user", [user.idUser], [], data, callback);
        },

        getTest : (callback) => {
          request.get(path + "/test", [], callback);
        },

        putAnswers : (answers, callback) => {
            let data = JSON.parse(JSON.stringify(answers));
            data = JSON.stringify(data);
            request.put(path + "/test", [answers.idUser], [], data, callback);
        },

        sendMessage : (sender, receiver, message, callback) => {
            let data = {message: message};
            data = JSON.stringify(data);
            request.post(path, [sender, "chat", receiver], [], data, callback);
        },

        getConversation: (sender, receiver, callback) => {
            request.get(path, [sender, "chat", receiver], callback);
        },
        getAllConversations: (sender, callback) => {
            request.get(path, [sender, "chat"], callback);
        }
    };

    return UserRestClient;
});