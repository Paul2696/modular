let noUser = [
    "Inicio",
    "Iniciar Sesion",
    "Crear Cuenta"
];

let withUserStudent = [
    {text: "Inicio", path: "#"},
    {text: "Cursos", path: "#courses/student"},
    {text: "Tareas", path: "#homework/student"},
    {text: "Test de Aprendizaje", path: "#test"}
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
        let cookie = Cookie.get("session");
        return cookie !== null ? true : false;
    };
    /**
     */
    self.getSessionMenu = function(userType) {
        switch(userType) {
            case 2: return withUserStudent;
            case 1: return withUserTeacher;
            case 3: return noUser;
            case 4: return withUserAdmin;
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
        let session =  new Session(3,2);
        Cookie.set("session", session);
        return session;
    }
    return self;
});