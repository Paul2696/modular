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
       let request = new Request("http://localhost:8000/");
       request.get("api/course/", [idCourse], callback);
    },

    getAllCourses: function(callback){
        let request = new Request("http://localhost:8000/");
        request.get("api/course/", [], callback);
    },

    createCourse: function(course, callback) {
        let data = JSON.parse(JSON.stringify(course));
        let request = new Request("http://localhost:8000/");
        request.post("api/course/", [], [], data, callback);
    },

    updateCourse: function(course, callback){
        let data = JSON.parse(JSON.stringify(course));
        let request = new Request("http://localhost:8000/");
        request.put("api/course/", [course.idCourse], [], data, callback);
    },

    deleteCourse: function(idCourse, callback){
        let request = new Request("http://localhost:8000/");
        request.delete("api/course/", [idCourse], callback);
    },

    enrollCourse: function(idUser, idCourse, password, callback){
        let passwordJson = {
            "password" : password
        };
        let request = new Request("http://localhost:8000/");
        request.put("api/user/", [idUser, "enroll", idCourse],[], passwordJson, callback);
    },

    getAllFromUser : function (idUser, callback) {
        let request = new Request("http://localhost:8000/");
        request.get("api/course", idUser, callback);
    }


};