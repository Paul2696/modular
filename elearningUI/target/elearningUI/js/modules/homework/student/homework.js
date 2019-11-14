define([
    "ko",
    "jquery",
    "cookie",
    "moment",
    "el/modules/client/UserRestClient",
    "el/modules/client/HomeworkRestClient",
    "el/modules/homework/Response",
    "text!modules/views/uploadHomework.html",
    "bootstrap"
], function (ko, $, cookie, moment, userClient, homeworkClient, Response, modalView) {
    function HomeworkViewModel() {
        let self = this;
        self.homework = ko.observableArray();
        self.courses = ko.observableArray();
        self.course = ko.observable();

        let session = JSON.parse(cookie.get("session"));
        self.idUser = session.idUser;
        self.userType = session.userType;
        self.model = {
            data: new UploadFileModel(),
            handler: () => {
                self.send();
            }
        };
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
                if(element.responses != null &&  element.responses.length > 0){
                    element.gradedObservable(element.responses[0].graded);
                    if(element.responses[0].graded){
                        element.gradeObservable(element.responses[0].grade);
                    }
                    element.uploaded(true);
                }
            });
            self.homework(homework);
        };

        self.updateTable = (course) =>{
            self.populateHomeworkTable(course.homework);
        };

        self.openModal = (hw) =>{
            self.model.data.setHomework(hw);
            if(hw.responses != null && hw.responses.length > 0){
                self.model.data.textResponse(hw.responses[0].textResponse);
                //self.model.data.file(hw.responses[0].response);
            }
            $("#modal").modal("toggle");

        }

        self.send = () =>{
            let response = new Response(
                self.model.data.homework,
                self.idUser,
                self.model.data.textResponse(),
                self.model.data.file()
            );
            if(self.model.data.homework.uploaded()){
                response.setValues(self.model.data.homework.responses[0]);
                homeworkClient.updateStudentHomework(
                    response,
                    self.model.data.file(),
                    self.idUser,
                    function(){
                    self.loadCourses();
                });
            }
            else{
                homeworkClient.sendHomework(response, self.idUser,  function(){
                    self.loadCourses();
                });
            }
        };

        function UploadFileModel() {
          let self = this;
          self.homework = null;
          self.response = null;

          self.file = ko.observable(null);
          self.uploaded = ko.observable();
          self.textResponse = ko.observable();
          self.path = ko.observable();

          self.setHomework = (homework) => {
              self.homework = homework;
              self.uploaded(homework.uploaded());
              if (homework.uploaded()) {
                  self.response = homework.responses[0];
                  self.textResponse(homework.responses[0].textResponse);
                  self.path(
                      homework.idHomework +
                      "/response/" +
                      self.response.user.idUser +
                      "/" +
                      self.response.idHomeworkResponse + "/file");
              }
          };
        };
        self.init();
    }
    return HomeworkViewModel;
});