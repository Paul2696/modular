define([
    "ko",
    "jquery",
    "cookie",
    "el/modules/client/CourseRestClient",
    "el/modules/session/Session",
    "chart",
    "bootstrap"
], function (ko, $, cookie, courseClient, session) {
    function StatisticsViewModel(){
        let self = this;
        self.currentCourse = ko.observable();
        self.courses = ko.observableArray([]);
        self.users = ko.observableArray([]);
        self.idUser = session.idUser;
        self.userType = session.userType;
        self.learningTypeCounters = {
            visual: 0,
            auditivo: 0,
            kinestesico: 0
        };

        self.init = () => {
            self.populateCourses();
        };

        self.populateCourses = () => {
            courseClient.getAllFromUser(self.idUser,self.userType, function(data){
                self.courses(data);
                if(data.length > 0) {
                    self.currentCourse(data[0]);
                    self.users(data[0].users)
                    self.getGraphic(self.users());
                }
                $("#loading").hide();
                $("#main-content").show();
            });
        };

        self.updateGraphic = (course) => {
            $("#loading").show();
            $("#main-content").hide();
            self.learningTypeCounters.visual = 0;
            self.learningTypeCounters.auditivo = 0;
            self.learningTypeCounters.kinestesico = 0;
            self.currentCourse(course);
            self.getGraphic(course.users);
            $("#loading").hide();
            $("#main-content").show();
        }

        self.getGraphic = (users) => {
            users.forEach((user) => {
                if(user.learningType == null){
                    return;
                }
                if(user.learningType.localeCompare("Visual") == 0){
                    self.learningTypeCounters.visual++;
                }
                if(user.learningType.localeCompare("Auditivo") == 0){
                    self.learningTypeCounters.auditivo++;
                }
                if(user.learningType.localeCompare("Kinestesico") == 0){
                    self.learningTypeCounters.kinestesico++;
                }
            });
            let ctx = document.getElementById("myChart").getContext('2d');
            let myChart = new Chart(ctx, {
                type: 'bar',
                data: {
                    labels: ["Visual", "Auditivo", "Kinestesico"],
                    datasets: [{
                        label: 'Numero de alumnos por tipo de aprendizaje',
                        data: [self.learningTypeCounters.visual, self.learningTypeCounters.auditivo, self.learningTypeCounters.kinestesico],
                        backgroundColor: [
                            'rgba(255, 99, 132, 0.2)',
                            'rgba(54, 162, 235, 0.2)',
                            'rgba(255, 206, 86, 0.2)',
                            'rgba(75, 192, 192, 0.2)',
                            'rgba(153, 102, 255, 0.2)',
                            'rgba(255, 159, 64, 0.2)'
                        ],
                        borderColor: [
                            'rgba(255,99,132,1)',
                            'rgba(54, 162, 235, 1)',
                            'rgba(255, 206, 86, 1)',
                            'rgba(75, 192, 192, 1)',
                            'rgba(153, 102, 255, 1)',
                            'rgba(255, 159, 64, 1)'
                        ],
                        borderWidth: 1
                    }]
                },
                options: {
                    scales: {
                        yAxes: [{
                            ticks: {
                                beginAtZero: true
                            }
                        }]
                    }
                }
            });
        };

        self.init();

    };
    return StatisticsViewModel;
});