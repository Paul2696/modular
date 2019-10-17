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


    getCourses: function(idCourse, success, error) {
       let request = new Request("http://localhost:8080/");
       request.get("elearning/api/course", [idCourse], success, error);
    },

    getAllCourses: function(callback){
        let request = new Request("http://localhost:8080/");
        request.get("elearning/api/course", [], callback);
    },

    createCourse: function(course, success, error) {
        let data = JSON.parse(JSON.stringify(course));
        data = JSON.stringify(data);
        let request = new Request("http://localhost:8080/");
        request.post(course,"elearning/api/course", [], [], data, success, error);
    },

    updateCourse: function(course, success, error){
        let data = JSON.parse(JSON.stringify(course));
        data = JSON.stringify(data);
        let request = new Request("http://localhost:8080/");
        request.put(course,"elearning/api/course", [course.idCourse], [], data, success, error);
    },

    deleteCourse: function(idCourse, callback){
        let request = new Request("http://localhost:8080/");
        request.delete("elearning/api/course", [idCourse], callback);
    },

    enrollCourse: function(idUser, idCourse, password, callback){
        let passwordObj = {
            "password" : password
        };
        let passwordJson = JSON.stringify(passwordObj);
        let request = new Request("http://localhost:8080/");
        request.put("elearning/api/user", [idUser, "enroll", idCourse],[], passwordJson, callback);
    },

    getAllFromUser : function (idUser, userType, success, error) {
        let request = new Request("http://localhost:8080/");
        if(userType === 1) {
            request.get("elearning/api/course/teacher", [idUser], success, error);
        } else if(userType === 2) {
            request.get("elearning/api/course/student", [idUser], success, error);
        }


    }


};