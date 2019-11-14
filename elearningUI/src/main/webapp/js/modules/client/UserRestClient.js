define(["el/modules/client/Request"], function (request) {
    let UserRestClient = {

        getUser : (idUser, callback) => {
            request.get("elearning/api/user", [idUser],callback);
        },

        getTest : (callback) => {
          request.get("elearning/api/user/test", [], callback);
        },

        putAnswers : (answers, callback) => {
            let data = JSON.parse(JSON.stringify(answers));
            data = JSON.stringify(data);
            request.put("elearning/api/user/test", [answers.idUser], [], data, callback);
        }
    };

    return UserRestClient;
});