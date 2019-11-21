let noUser = [
    "Inicio",
    "Iniciar Sesion",
    "Crear Cuenta"
];

let withUserStudent = [
    {text: "Inicio", path: "#"},
    {text: "Cursos", path: "#courses/student"},
    {text: "Lista de compañeros", path: "#users"},
    {text: "Tareas", path: "#homework/student"},
    {text: "Test de Tipo de Aprendizaje", path: "#test"},
    {text: "Chat", path: "#chat"}
];

let withUserTeacher = [
    {text: "Inicio", path: "#"},
    {text: "Cursos", path: "#courses/teacher"},
    {text: "Estadisticas de Alumnos", path: "#statistics"},
    {text: "Tareas", path: "#homework/teacher"},
    {text: "Chat", path: "#chat"}
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

    self.init = () =>{
        let cookie = Cookie.get("session");
        if(cookie == null) {
           // window.location.href = "/elearningUI/html/log_in.html";
        } else {
            let values = cookie.split("|");
            self.idUser = parseInt(values[0]);
            self.userType = parseInt(values[1]);
        }
    };

    /**
     */
    self.isSessionActive =  function() {
        let cookie = Cookie.get("session");
        return cookie !== null ? true : false;
    };
    /**
     */
    self.getSessionMenu = function() {
        switch(self.userType) {
            case 2: return withUserStudent;
            case 1: return withUserTeacher;
            case 3: return noUser;
            case 4: return withUserAdmin;
        }
    };
    /**
     */

    self.testSession = function() {
        let session =  new Session(4,1);
        Cookie.set("session", session);
        return session;
    };

    self.init();
    return self;
});