define(["jquery"], function ($) {
    class Request {

        constructor() {
            this.url = "http://aprendizaje.jl.serv.net.mx/";
            this.contentType = "application/json";
        }

        get(route, parameters, callback) {
            let url = this.url + route + '/' + parameters.join('/');

            $.ajax({
                url: url,
                method: "GET",
                headers: {
                    "Access-Control-Allow-Origin": "*"
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
                    "Access-Control-Allow-Origin": "*"
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
                    "Access-Control-Allow-Origin": "*"
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
                    "Access-Control-Allow-Origin": "*"
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
