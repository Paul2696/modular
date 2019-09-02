let HomeworkViewModel = {

	/**
	 * Function to create new homework in server
	 */
	createHomework: function(data, callback, file) {
        let homeworkJson = JSON.stringify(data);
        let formData = new FormData();
        formData.append("file", file);
        formData.append("request", homeworkJson);
        let request = new Request("http://localhost:8080/");
        request.post("api/homework/", [], [], formData, callback, false, false);
	},
	/**
	 * Function to update an existing homework
	 */
	updateHomework: function(hw, callback) {
        let data = JSON.parse(JSON.stringify(hw));
        let homeworkJson = JSON.stringify(data);
        let formData = new FormData();
        formData.append("file", hw.file());
        formData.append("request", homeworkJson);
	    let request = new Request("http://localhost:8000/");
	    request.put("api/homework/", [hw.idHomework], [], formData, callback, false, false);
	},
	/**
	 *Function to obtain all registered homework
	 */
	getHomework: function(idCourse, callback) {
	    let request = new Request("http://localhost:8000/");
	    request.get("api/homework/", [], callback);
    },

    deleteHomework: function(hw_id, callback){
        let request = new Request("http://localhost:8000/");
        request.delete("api/homework/",[hw_id], callback);
    },

    sendHomework: function(idHomework, idUser, answer, file, callback){
        let json = {
            "homework": idHomework,
            "answer": answer
        }
        let homeworkJson = JSON.stringify(json);
        let formData = new FormData();
        formData.append("file", file);
        formData.append("request", homeworkJson);
        let request = new Request("http://localhost:8000/");
        request.post("api/homework/", [idHomework, "response", idUser], [], formData, callback, false, false);
    },

    updateStudentHomework: function(idHomework, idUser, answer, file, callback){
        let json = {
            "id": idHomework,
            "answer": answer
        }
        let homeworkJson = JSON.stringify(json);
        let formData = new FormData();
        formData.append("file", file);
        formData.append("request", homeworkJson);
        let request = new Request("http://localhost:8000/");
        request.put("api/homework/", [idHomework, "response", idUser], [], formData, callback, false, false);
    },

    getResponsesHomework: function(id, token, callback){
        let request = new Request("http://localhost:8000/", token);
        request.get("homework/response/get", [id], callback);
    },

    sendGrades: function(data, token, callback){
        gradesObj = new Object;
        gradesObj.grades = data;
        let gradesJson = JSON.stringify(gradesObj);
        let request = new Request("http://localhost:8000/", token);
        request.post("homework/response/grade/", gradesJson, callback);
    }
};
