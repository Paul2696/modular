class Request {

    constructor(url) {
        this.url = url;
        this.contentType = "application/json";
    }

    get(route, parameters, callback) {
        var url = this.url + route + '/' + parameters.join('/');

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
        var url = this.url + route + '/' + parameters.join('/');
        var pd = true;
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

    put(route, parameter, queryParam, data, callback, type, processData){
        var url = this.url + route + '/' + parameters.join('/');
        var pd = true;
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
        var url = this.url + route + '/' + parameters.join('/');
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