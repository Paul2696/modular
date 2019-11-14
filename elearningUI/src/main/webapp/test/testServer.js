var http = require('http');

//create a server object:
http.createServer(function (req, res) {
  console.log("Test server"); //write a response to the client
  console.log(req.method + " " + req.url);
  res.setHeader('Content-Type', 'application/json');
  res.setHeader('Access-Control-Allow-Origin', '*');
  res.setHeader('Access-Control-Allow-Methods', '*');
  res.setHeader('Access-Control-Allow-Headers', '*, X-Requested-With, Content-type');
  res.setHeader('Access-Control-Allow-Credentials', true);

  var response;

 if(req.method === "GET") {
     if(req.url.includes("/tareas/get")) {
        var args = req.url.split("/");
        response = getHomework(parseInt(args[3]), parseInt(args[4]));
     } else if (req.url.includes("/courses/get")) {
        response = getCourses();
     }
      res.write(response);
      console.log(response);
      res.end();//end the response
    } else if (req.method === "POST" ) {
        if(req.url.includes("/account/login")) {
            getBody(req, function(response){
                var auth = login(response);

                if(!auth) {
                    res.statusCode = 403;
                    res.write("No tienes acceso")
                    console.log("credenciales incorrectas");
                } else {
                     res.write(auth);
                     console.log(auth);
                }
                res.end();
            });
        } else if(req.url.includes("/tareas/new")) {
            getBody(req, function(body) {
                console.log(body);
                var o = createHomework(body);
                console.log("created homework ");
                res.write("homework created successfully");
                res.end();
            });
        } else if(req.url.includes("/tareas/update")) {
            getBody(req, function(body) {
                 console.log(body);
                 var o = updateHomework(body);
                 console.log("updated homework ");
                 res.write("homework updated successfully");
                 res.end();
            });
        } else if(req.url.includes("cursos/new/")) {
             console.log(body);
             var o = createCourse(body);
             console.log("created course ");
             res.write("course updated successfully");
             res.end();
        } else if(req.url.includes("cursos/update/")) {
             console.log(body);
             var o = updateCourse(body);
             console.log("updated homework");
             res.write("course updated successfully");
             res.end();
        }
  } else if (req.method === "OPTIONS") {
        res.write("ok");
        res.end();
  }

}).listen(8000); //the server object listens on port 8080

var homeworkTest1 = [{
        id: 1,
        titulo: "Tarea pendeja 1",
        course_id: 1,
        usr_id: 1,
        fecha_limite: new Date(),
        descripcion: "tarea sobre algo",
        tipo: "auditivo",
        closed: false
   }, {
        id: 2,
        titulo: "Tarea pendeja 2",
        course_id: 1,
        usr_id: 1,
        fecha_limite: new Date(),
        descripcion: "tarea sobre no se que",
        tipo: "visual",
        closed: false
   }, {
        id: 3,
       titulo: "Tarea pendeja 3",
       course_id: 1,
       usr_id: 1,
       fecha_limite: new Date(),
       descripcion: "tarea sobre algo mas",
       tipo: "kinestesico",
       closed: false
   },{
     id: 4,
     titulo: "Tarea pendeja 4",
     course_id: 2,
     usr_id: 1,
     fecha_limite: new Date(),
     descripcion: "tarea sobre algo",
     tipo: "auditivo",
     closed: false
    }, {
     id: 5,
     titulo: "Tarea pendeja 5",
     course_id: 2,
     usr_id: 1,
     fecha_limite: new Date(),
     descripcion: "tarea sobre no se que",
     tipo: "visual",
     closed: false
    }, {
     id: 6,
    titulo: "Tarea pendeja 6",
    course_id: 2,
    usr_id: 1,
    fecha_limite: new Date(),
    descripcion: "tarea sobre algo mas",
    tipo: "kinestesico",
    closed: false
}

];


var coursesTest1 = [
    {
        course_id: 1,
        name: "curso 1",
        start: new Date(),
        end: new Date(),
        descripcion: "curso idiota"
    },{
        course_id: 2,
        name: "curso 2",
        start: new Date(),
        end: new Date(),
        descripcion: "curso estupido"
    }, {
        course_id: 3,
        name: "curso 3",
        start: new Date(),
        end: new Date(),
        descripcion: "curso huevon"
    }

];

var account = {
    usr_id : 1,
    username: "pol",
    userType: 2,
    Session: Buffer.from("Hola amigo").toString('base64'),
    pass: "pass1"
};

function getHomework(usr_id, course_id) {
    var response = [];
    for(var i = 0; i < homeworkTest1.length; i++) {
        var t = homeworkTest1[i];
        console.log(t);
        if(t.course_id === course_id && t.usr_id === usr_id) {
            response.push(t);
        }
    }
    return JSON.stringify(response);
}

function getCourses() {
    return JSON.stringify(coursesTest1);
}

function login(credentials) {
    var credentials = JSON.parse(credentials);
    var user = credentials["code_mail"];
    var pass = credentials["password"];

    if(account.username === user && account.pass === pass) {
        return JSON.stringify(account);
    }
    return false;

}

function getBody(req, callback) {
    var body = [];
    req.on('data', (chunk) => {
      body.push(chunk);
    }).on('end', () => {
      body = Buffer.concat(body).toString();
      callback(body);
    });
}

function createHomework(homework) {
    var obj = JSON.parse(homework);
    obj["id"] = homeworkTest1.length + 1;
    console.log(obj);
    homeworkTest1.push(obj);
    return obj;
}

function updateHomework(homework) {
    var obj = JSON.parse(homework);
    for(var i = 0; i < homeworkTest1.length; i++) {
        if(obj.id === homeworkTest1[i].id ) {
            homeworkTest1[i] = obj;
            console.log(obj);
            return obj;
        }
    }
}

function createCourse(course) {
    var obj = JSON.parse(course);
    obj["course_id"] = coursesTest1.length + 1;
    console.log(obj);
    coursesTest1.push(obj);
    return obj;
}

function updateCourse(course) {
    var obj = JSON.parse(course);
    for(var i = 0; i < coursesTest1.length; i++) {
        if(obj.id === coursesTest1[i].id ) {
            coursesTest1[i] = obj;
            console.log(obj);
            return obj;
        }
    }
}

