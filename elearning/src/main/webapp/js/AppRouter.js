define([
    "ko",
    "router",
    "jquery",
    "text!modules/courses/teacher/courses.html"
], function (ko, Router, $, coursesTeacher) {
    function AppRouter() {
      let paths = ["/courses/teacher"];
      let self = this;
      self.router = new Router({
          mode: "hash",
          page404: (path) => {
              console.log(path);
          }
      });
      self.router.addUriListener();
      paths.forEach(function (path){
          self.router.add(path, function () {
              $("#main-content").html(coursesTeacher);
          });
      });
      self.navigateTo = function (path) {
          self.router.navigateTo(path);
      };
      self.refresh = () => {
          self.router.refresh();
      };
    };
    return new AppRouter();
});