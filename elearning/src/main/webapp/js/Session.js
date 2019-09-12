var noUser = [
	{menu : "Inicio", url : "/webapp/index.html"},
	{menu : "Iniciar Sesion", url : "/webapp/html/log_in.html"},
	{menu : "Crear Cuenta", url : "/webapp/html/registro.html"}
];

var withUserStudent = [
	{menu : "Inicio", url : "/webapp/index.html"},
	{menu : "Tareas", url : "/webapp/html/student/tareas.html"},
	{menu : "Cursos", url : "/webapp/html/student/cursos.html"}
	//{menu : "Test", url : "/webapp/html/student/cuestionario.html"},
];

var withUserTeacher = [
	{menu : "Inicio", url : "/webapp/index.html"},
	{menu : "Cursos", url : "/webapp/html/teacher/cursos.html"},
	{menu : "Tareas", url : "/webapp/html/teacher/tareas.html"}
];

var withUserAdmin = [
	{menu : "Inicio", url : "/webapp/index.html"},
	{menu : "Usuarios", url : "/webapp/html/admin/usuarios.html"}
];

var withUserSuperUser = [
    {menu : "Inicio", url: "/webapp/index.html"},
    {menu : "Cursos Prof", url: "/webapp/html/teacher/cursos.html"},
    {menu : "Tareas Prof", url: "/webapp/html/teacher/tareas.html"},
    {menu : "Tareas Est", url: "/webapp/html/student/tareas.html"},
    {menu : "Cursos Est", url: "/webapp/html/student/cursos.html"},
];

function Session(idUser, userType) {
    var self = this;
    self.idUser = idUser;
    self.userType = userType;

    //Functions

    /**
    */
    self.isSessionActive =  function() {
        var cookie = Cookies.get("Session");
        return cookie !== null ? true : false;


    };
    /**
     */
    self.getSessionMenu = function() {
        switch(userType) {
            case 3: return withUserStudent;
            case 0: return withUserSuperUser;
            case 2: return withUserTeacher;
            case 4: return noUser;
            case 1: return withUserAdmin;
        }
    };
    /**
     */

    self.destroySession = function() {
        Cookies.remove("session");
        window.location.href = "/webapp/html/log_in.html";
    }

    self.getToken = function(){
        return self.token;
    }
}

function getNoUserMenu() {
     return noUser
 }

function testSession() {
    var session =  new Session(1,1);
    Cookies.set("session", "session");
    return session;
}

function parseSession(obj) {
    if(obj != null) {
        return new Session(obj.token, obj.userType);
    } else {
        return null;
    }
}