define([
    "ko",
    "jquery",
    "cookie",
    "el/modules/client/UserRestClient",
    "el/modules/session/Session",
    "bootstrap"
], function (ko, $, cookie, userClient, session) {
    function ProfileViewModel(){
        let self = this;
        self.idUser = session.idUser;
        self.user = ko.observable();
        self.editable = ko.observable(false);
        self.editPassword = ko.observable(false);


        self.init = () =>{
            self.loadProfile();
        };

        self.loadProfile = () =>{
            userClient.getUser(self.idUser,  (data) =>{
                if(data != null){
                    data.password = null
                    self.user(data);
                }
                $("#loading").hide();
                $("#main-content").show();
            });
        };

        self.updateData = () =>{
            if(self.editable()){
                self.editable(false);
            }
            else{
                self.editable(true);
            }
        };

        self.updateProfile = (user) =>{
            if(self.editable()){
                userClient.updateUser(user, (data) =>{
                    self.editable(false);
                })
            }
            else{
                self.editable(true);
            }
        }


        self.init();

    };
    return ProfileViewModel;
});