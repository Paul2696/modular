 define([
     "ko",
     "jquery",
     "moment",
     "cookie",
     "el/modules/client/CourseRestClient",
     "el/modules/client/HomeworkRestClient",
     "el/modules/homework/Homework",
 ], function (ko, $, moment, cookie, courseClient, homeworkClient, Homework) {
     function HomeworkViewModel() {
         let self = this;
         self.homework = ko.observableArray([]);
         self.courses = ko.observableArray([]);
         self.currentCourse = ko.observable();
         let session = JSON.parse(cookie.get("session"));
         self.idUser = session.idUser;
         self.userType = session.userType;

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

         self.updateTable = (hw, event) => {
             if (event.originalEvent) {
                 self.populateHomeworkTable(hw);
             }
         };

         self.populateHomeworkTable = (homework) => {
             if (homework.length > 0) {
                 homework.forEach((element) => {
                     let hw = Object.assign(new Homework(), element);
                     hw.endObservable(new Date(hw.end));
                     hw.editable(false);
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
                 self.responses(this.responses);
                 $("#modalCalificar").modal("show");
                 $("#modalCalificar").on("hide.bs.modal", function(event){
                     self.setGrades(event);
                 });
             }
         };


         self.setGrades = (event) =>{
             if($(document.activeElement)[0] == $("#send")[0]){
                 let grades = [];
                 self.responses().forEach(function(grade){
                     if(grade != null){
                         grades.push({
                             id: grade.id,
                             "grade": grade.grade
                         });
                     }
                 });
                 self.homeworkViewModel.sendGrades(grades, self.session.getToken(), function(){
                     //TODO: IGual y se me ocurre algo
                 });
             }

         }
         self.init();
     }
     return HomeworkViewModel;
 });
