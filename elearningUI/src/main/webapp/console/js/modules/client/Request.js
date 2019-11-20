define(["jquery"], function ($) {
    class Request {

        constructor() {
            this.url = "http://localhost:8080/";
            this.contentType = "application/json";
        }

        get(route, parameters, callback) {
            let url = this.url + route + '/' + parameters.join('/');

            $.ajax({
                url: url,
                method: "GET",
                headers: {
                },
                success: function(response){
                    callback(response);
                },
                error: function (response) {
                    callback(response);
                }
            });
        }

        post(route, parameters, queryParam, data, callback, type, processData) {
            let url = this.url + route + '/' + parameters.join('/');
            let pd = true;
            if(processData != null){
                pd = processData;
            }
            if(type == null){
                type = this.contentType;
            }
            $.ajax({
                url: url,
                contentType: type,
                processData: pd,
                data: data,
                method: "POST",
                headers: {
                },
                success: function(response){
                    callback(response);
                },
                error: function (response) {
                    callback(response);
                }
            });
        }

        put(route, parameters, queryParam, data, callback, type, processData){
            let url = this.url + route + '/' + parameters.join('/');
            let pd = true;
            if(processData != null){
                pd = processData;
            }
            if(type != null){
                this.contentType = type;
            }
            $.ajax({
                url: url,
                contentType: this.contentType,
                processData: pd,
                data: data,
                method: "PUT",
                headers: {
                },
                success: function(response){
                    callback(response);
                },
                error: function (response) {
                    callback(response);
                }
            });
        }

        delete(route, parameters, callback){
            let url = this.url + route + '/' + parameters.join('/');
            $.ajax({
                url : url,
                method : "DELETE",
                headers : {
                },
                success : function (response) {
                    callback(response);
                },
                error : function (response) {
                    callback(response);
                }
            })
        }
    }

    return new Request();
});
