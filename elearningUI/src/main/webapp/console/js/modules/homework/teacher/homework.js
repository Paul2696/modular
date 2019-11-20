 define([
     "ko",
     "jquery",
     "moment",
     "cookie",
     "el/modules/client/CourseRestClient",
     "el/modules/client/HomeworkRestClient",
     "el/modules/homework/Homework",
     "el/modules/session/Session",
     "text!modules/views/gradeHomeworkView.html",
     "bootstrap"
 ], function (ko, $, moment, cookie, courseClient, homeworkClient, Homework, session,  view) {
     function HomeworkViewModel() {
         let self = this;
         self.homework = ko.observableArray([]);
         self.courses = ko.observableArray([]);
         self.currentCourse = ko.observable();

         self.idUser = session.idUser;
         self.userType = session.userType;

         self.view = view;
         self.model = {
             data: {
                 responses: ko.observableArray([])
             },
             handler: () => {
                 self.sendGrades(this.data.responses);
             }
         };

         self.init = () => {
             self.populateCourses();
         };

         self.populateCourses = () => {
             courseClient.getAllFromUser(self.idUser,self.userType, function(data){
                 self.courses(data);
                 if(data.length > 0) {
                     self.currentCourse(data[0]);
                     self.populateHomeworkTable(data[0].homework);
                 }
                 $("#loading").hide();
                 $("#main-content").show();
             });
         };

         self.updateTable = (hw) => {
             self.populateHomeworkTable(hw);
         };

         self.populateHomeworkTable = (homework) => {
             if (homework.length > 0) {
                 homework.forEach((element) => {
                     let hw = Object.assign(new Homework(), element);
                     hw.endObservable(new Date(hw.end));
                     hw.editable(false);
                     if(hw.responses != null && hw.responses.length > 0) {
                         hw.responses.forEach((res) =>{
                             res.path = ko.pureComputed(()=>{
                                 return hw.idHomework +
                                     "/response/" +
                                     res.user.idUser +
                                     "/" +
                                     res.idHomeworkResponse + "/file"
                             });
                         });
                     }
                     self.homework.push(hw);
                 });
             }
         };

         self.create = () =>{
            self.homework.push(new Homework(self.currentCourse()));
         };

         self.createOrUpdate = (hw) => {
             if(hw.editable()) {
                 hw.editable(false);
                 hw.end = hw.endObservable();
                 if(hw.idHomework == null) {
                     homeworkClient.createHomework(hw,(data) =>{
                         console.log(data);
                         self.populateHomeworkTable(self.currentCourse());

                     });
                 } else {
                     homeworkClient.updateHomework(hw, (data) =>{
                         console.log(data);
                     });
                 }
             } else {
                 hw.editable(true);
             }
         };

         self.remove = (hw) => {
             homeworkClient.deleteHomework(hw.idHomework, function(data){
                 self.homework.remove(hw);
             });
         };

         self.grade = (hw) => {
             if(hw.responses != null && hw.responses.length > 0){
                 self.model.data.responses(hw.responses);
                 $("#modal").modal("toggle");
             }
         };


         self.sendGrades = (responses) =>{
            homeworkClient.sendGrades(responses, (data) =>{
                console.log(data);
            });
         };
         self.init();
     }
     return HomeworkViewModel;
 });
