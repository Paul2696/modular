let CourseViewModel = {
    //Properties
    name : ko.observable(),
    start : ko.observable(),
    description : ko.observable(),
    end : ko.observable(),
    pass : ko.observable(),
    courses : ko.observableArray(),

    //methods

    /**
     *
     */


    getCourses: function(idCourse, callback) {
       let request = new Request("http://localhost:8080/");
       request.get("elearning/api/course", [idCourse], callback);
    },

    getAllCourses: function(callback){
        let request = new Request("http://localhost:8080/");
        request.get("elearning/api/course", [], callback);
    },

    createCourse: function(course, callback) {
        let data = JSON.parse(JSON.stringify(course));
        let request = new Request("http://localhost:8080/");
        request.post("elearning/api/course", [], [], data, callback);
    },

    updateCourse: function(course, callback){
        let data = JSON.parse(JSON.stringify(course));
        let request = new Request("http://localhost:8080/");
        request.put("elearning/api/course", [course.idCourse], [], data, callback);
    },

    deleteCourse: function(idCourse, callback){
        let request = new Request("http://localhost:8080/");
        request.delete("elearning/api/course", [idCourse], callback);
    },

    enrollCourse: function(idUser, idCourse, password, callback){
        let passwordJson = {
            "password" : password
        };
        let request = new Request("http://localhost:8080/");
        request.put("elearning/api/user", [idUser, "enroll", idCourse],[], passwordJson, callback);
    },

    getAllFromUser : function (idUser, callback) {
        let request = new Request("http://localhost:8080/");
        request.get("elearning/api/course/user", [idUser], callback);
    }


};