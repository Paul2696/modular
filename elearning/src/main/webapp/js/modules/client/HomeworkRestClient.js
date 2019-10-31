define(["el/modules/client/Request"], function (request) {
    let HomeworkRestClient = {

        /**
         * Function to create new homework in server
         */
        createHomework: function(data, callback) {
            let homeworkJson = JSON.stringify(data);
            let formData = new FormData();
            formData.append("json", homeworkJson);
            if(data.file() != null) {
                formData.append("file", data.file());
            }
            request.post(data,"elearning/api/homework/file", [], [], formData, callback, false, false);
        },
        /**
         * Function to update an existing homework
         */
        updateHomework: function(hw, callback) {
            let data = JSON.parse(JSON.stringify(hw));
            let homeworkJson = JSON.stringify(data);
            let formData = new FormData();
            if(hw.file() != null) {
                formData.append("file", hw.file());
            }
            formData.append("json", homeworkJson);
            request.put("elearning/api/homework", [hw.idHomework, "file"], [], formData, callback, false, false);
        },
        /**
         *Function to obtain all registered homework
         */
        getHomework: function(idCourse, success, error) {
            request.get("api/homework", [], success, error);
        },

        deleteHomework: function(hw_id, callback){
            request.delete("elearning/api/homework",[hw_id], callback);
        },

        sendHomework: function(data, idUser, callback){
            let json = {
                "homework": data.homework().idHomework,
                "answer": data.textResponse()
            }
            let homeworkJson = JSON.stringify(json);
            let formData = new FormData();
            formData.append("file", data.file());
            formData.append("request", homeworkJson);
            request.post("elearning/api/homework", [data.homework().idHomework, "response", idUser, "file"], [], formData, callback, false, false);
        },

        updateStudentHomework: function(idHomework, idUser, answer, file, callback){
            let json = {
                "homework": data.homework().idHomework,
                "answer": data.textResponse()
            }
            let homeworkJson = JSON.stringify(json);
            let formData = new FormData();
            formData.append("file", data.file());
            formData.append("request", homeworkJson);
            request.put("elearning/api/homework", [data.homework().idHomework, "response", idUser, "file"], [], formData, callback, false, false);
        },


        sendGrades: function(data, token, callback){
            let gradesObj = new Object();
            gradesObj.grades = data;
            let gradesJson = JSON.stringify(gradesObj);
            request.post("elearning/api/homework/response/grade", gradesJson, callback);
        }
    };
    return HomeworkRestClient;
});
