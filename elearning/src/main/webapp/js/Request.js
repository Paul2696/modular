class Request {

    constructor(url) {
        this.url = url;
        this.contentType = "application/json";
    }

    get(route, parameters, success, error) {
        var url = this.url + route + '/' + parameters.join('/');

        $.ajax({
            url: url,
            method: "GET",
            headers: {
            },
            success: function(response){
                success(JSON.parse(response));
            },
            error: function (response) {
                error(response);
            }
        });
    }

    post(object, route, parameters, queryParam, data, success, error, type, processData) {
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
                success(response, object, "La creacion fue exitosa");
            },
            error: function (response) {
                error(response);
            }
        });
    }

    put(object, route, parameters, queryParam, data, success, error, type, processData){
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
                success(response, object, "La actualizacion fue exitosa");
            },
            error: function (response) {
                error(response);
            }
        });
    }

    delete(route, parameters, success, error){
        var url = this.url + route + '/' + parameters.join('/');
        $.ajax({
            url : url,
            method : "DELETE",
            headers : {
            },
            success : function (response) {
                success(response);
            },
            error : function (response) {
                error(response);
            }
        })
    }
}