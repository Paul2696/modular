define(["el/modules/client/Request"], function (request) {
    let CourseRestClient = {
        /**
         *
         */
        getCourses: function(idCourse, callback) {
            request.get("elearning/api/course", [idCourse], callback);
        },

        getAllCourses: function(callback){
            request.get("elearning/api/course", [], callback);
        },

        createCourse: function(course, callback) {
            let data = JSON.parse(JSON.stringify(course));
            data = JSON.stringify(data);
            request.post("elearning/api/course", [], [], data, callback);
        },

        updateCourse: function(course, callback){
            let data = JSON.parse(JSON.stringify(course));
            data = JSON.stringify(data);
            request.put("elearning/api/course", [course.idCourse], [], data, callback);
        },

        deleteCourse: function(course, callback){
            request.delete("elearning/api/course", [course.idCourse], callback);
        },

        enrollCourse: function(idUser, course, password, callback){
            let passwordObj = {
                "password" : password
            };
            let passwordJson = JSON.stringify(passwordObj);
            request.put("elearning/api/user", [idUser, "enroll", course.idCourse],[], passwordJson, callback);
        },

        getAllFromUser : function (idUser, userType, callback) {
            if(userType === 1) {
                request.get("elearning/api/course/teacher", [idUser], callback);
            } else if(userType === 2) {
                request.get("elearning/api/course/student", [idUser], callback);
            }


        },

        getAllUsersFromCourse : function (idCourse, callback) {
            request.get("elearning/api/course/", [idCourse, "users"], callback);
        }
    };

    return CourseRestClient;
});