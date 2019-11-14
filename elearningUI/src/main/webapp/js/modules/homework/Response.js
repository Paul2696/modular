define(["ko"], function (ko) {
    function Response(homework, idUser, textResponse, file) {
        let self = this;
        self.homework = homework;
        self.user = {
            idUser: idUser
        };
        self.grade = -1;
        self.sent = false;
        self.textResponse = textResponse;
        self.sended = new Date();
        self.file = ko.observable(file);
        if(file != null) {
            self.fileExtension = file.name.split(".").pop();
        }

        self.setValues = (response) => {
            self.idHomeworkResponse = response.idHomeworkResponse;
            self.grade = response.grade;
            self.sent = response.sent;
            self.sended = response.sended;
            self.fileExtension = response.fileExtension;
        }

    }
    return Response;
});