define(["el/modules/client/Request"], function (request) {
    let UserRestClient = {

        getUser : (idUser, callback) => {
            request.get("elearning/api/user", [idUser],callback);
        }
    };

    return UserRestClient;
});