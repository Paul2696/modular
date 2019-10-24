define([
    "require",
    "ko",
    "router",
    "jquery",
    "text!modules/courses/teacher/courses.html"
], function (require, ko, Router, $, coursesTeacher) {
    function AppRouter() {
      let self = this;
      self.paths = ["/courses/teacher"];
      self.viewModels = {"#courses/teacher" : "modules/courses/teacher/courses"};

      self.router = new Router({
          mode: "hash",
          page404: (path) => {
              console.log(path);
          }
      });
      self.router.addUriListener();
      self.paths.forEach((path) => {
          self.router.add(path, () => {
              $("#main-content").html(coursesTeacher);
          });
      });
      self.getModuleName = (path) => {
          return self.viewModels[path];
      };
      self.navigateTo = (path) => {
            self.router.navigateTo(path);
        };
      self.refresh = () => {
          self.router.refresh();
      };
    };
    return new AppRouter();
});