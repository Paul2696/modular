define([
    "ko",
    "jquery",
    "cookie",
    "moment",
    "el/modules/client/UserRestClient",
    "el/modules/client/HomeworkRestClient",
    "text!modules/views/uploadHomework.html",
    "bootstrap"
], function (ko, $, cookie, moment, userClient, homeworkClient, modalView) {
    function HomeworkViewModel() {
        let self = this;
        self.homework = ko.observableArray();
        self.courses = ko.observableArray();
        self.course = ko.observable();

        self.file = ko.observable();
        self.uploaded = ko.observable(false);

        let session = JSON.parse(cookie.get("session"));
        self.idUser = session.idUser;
        self.userType = session.userType;

        self.view = modalView;

        self.init = () => {
            self.loadCourses();
        };

        self.loadCourses = () =>{
            userClient.getUser(self.idUser,  (data) =>{
                if(data.courses != null && data.courses.length > 0){
                    self.courses(data.courses);
                    self.course(data.courses[0]);
                    self.populateHomeworkTable(data.courses[0].homework);
                }
                $("#loading").hide();
                $("#main-content").show();
            });
        };

        /**
         *   function to populate the homework table
         */
        self.populateHomeworkTable = (homework) =>{
            homework.forEach(function(element){
                element.formattedDate = ko.observable(moment(new Date(element.end)).format("YYYY-MM-DD"));
                element.gradeObservable = ko.observable("-");
                element.uploaded = ko.observable(false);
                element.gradedObservable = ko.observable(false);
                if(element.response != null &&  element.response.length > 0){
                    element.gradedObservable(element.response[0].graded);
                    if(element.response[0].graded){
                        element.gradeObservable(element.response[0].grade);
                    }
                    element.uploaded(true);
                    self.uploaded(true);
                }
            });
            self.homework(homework);
        };

        self.updateTable = (course) =>{
            self.populateHomeworkTable(course.homework);
        };

        self.openModal = (hw) =>{
            self.model.data.homework(hw);
            if(hw.response != null && hw.response.length > 0){
                self.model.data.content(hw.response[0].textResponse);
                self.model.data.file(hw.response[0].response);
            }
            $("#modal").modal("toggle");
            self.uploaded(hw.uploaded());

        }

        self.send = () =>{
            if(self.model.data.uploaded()){
                homeworkClient.updateStudentHomework(self.model.data, self.idUser,  function(){
                    self.loadCourses();
                });
            }
            else{
                homeworkClient.sendHomework(self.model.data, self.idUser,  function(){
                    self.loadCourses();
                });
            }
        };

        function UploadFileModel(file, uploaded, handler) {
          let self = this;
          self.data = {};
          self.data.file = file;
          self.data.uploaded = uploaded;

          self.data.homework = ko.observable();
          self.data.content = ko.observable();
          self.data.textResponse = ko.observable();

          self.handler = handler;
        };
        self.model = new UploadFileModel(self.file, self.uploaded, self.send);
        self.init();
    }
    return HomeworkViewModel;
});