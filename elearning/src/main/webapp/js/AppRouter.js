define([
    "require",
    "ko",
    "router",
    "jquery",
], function (require, ko, Router, $) {
    function AppRouter() {
      let self = this;
      self.paths = ["/courses/teacher","/homework/teacher"];
      self.viewModels = {
          "#courses/teacher"  : "modules/courses/teacher/courses",
          "#homework/teacher" : "modules/homework/teacher/homework",
          "#homework/student" : "modules/homework/student/homework",
          "#courses/student"  : "modules/courses/student/courses",
          "#course/users" : "modules/courses/users/users",
          "#test" : "modules/test/test"
      };
      self.views = {
          "#courses/teacher"  : "text!modules/courses/teacher/courses.html",
          "#homework/teacher" : "text!modules/homework/teacher/homework.html",
          "#homework/student" : "text!modules/homework/student/homework.html",
          "#courses/student"  : "text!modules/courses/student/courses.html",
          "#course/users" : "text!modules/courses/users/users.html",
          "#test" : "text!modules/test/test.html"
      };

      self.router = new Router({
          mode: "hash",
          page404: (path) => {
              console.log(path);
          }
      });
      self.router.addUriListener();
      self.paths.forEach((path) => {
          self.router.add(path, () =>{});
      });
      self.getModuleName = (path) => {
          return self.viewModels[path];
      };
      self.navigateTo = (path) => {
          self.router.navigateTo(path);
          require([self.views[path]], (view) =>{
              $("#main-content").html(view);
          });
        };
      self.refresh = () => {
          self.router.refresh();
      };
    };
    return new AppRouter();
});