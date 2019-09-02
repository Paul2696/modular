class MainModel {

    constructor() {
        this.url = "http://localhost:8000"
        try {
            this.session = parseSession(Cookies.getJSON("session"));
            this.logged_in = true
            this.requests = new Requests(
                this.url,
                this.session.token
            )
        }
        catch (error) {
            this.logged_in = false
            this.requests = new Requests(
                this.url,
                null
            )
            //Do something with error
        }

        this.viewModel = {
            menu: ko.observableArray([]),
            init: function () {
                if (this.logged_in) {
                    menu(this.session.getSessionMenu())
                } else {
                    menu(noUser)
                }
            }
        }
    }
}