let noUser = [
    "Inicio",
    "Iniciar Sesion",
    "Crear Cuenta"
];

let withUserStudent = [
    "Inicio",
    "Tareas",
    "Cursos"
];

let withUserTeacher = [
    {text: "Inicio", path: "#"},
    {text: "Cursos", path: "#courses/teacher"},
    {text: "Tareas", path: "#homework/teacher"}
];

let withUserAdmin = [
    "Inicio",
    "Usuarios"
];

define(["cookie"], function (Cookie) {
    let self = this;
    function Session(idUser, userType) {
        let self = this;
        self.idUser = idUser;
        self.userType = userType;
    };

    /**
     */
    self.isSessionActive =  function() {
        let cookie = Cookie.get("Session");
        return cookie !== null ? true : false;
    };
    /**
     */
    self.getSessionMenu = function(userType) {
        switch(userType) {
            case 3: return withUserStudent;
            case 2: return withUserTeacher;
            case 4: return noUser;
            case 1: return withUserAdmin;
        }
    };
    /**
     */
    self.destroySession = function() {
        Cookie.remove("session");
    }
    self.getNoUserMenu = function () {
        return noUser
    }

    self.testSession = function() {
        let session =  new Session(9,2);
        Cookie.set("session", "session");
        return session;
    }
    return self;
});