define(["el/modules/client/Request"], function (request) {
    let UserRestClient = {

        getUser : (idUser, callback) => {
            request.get("elearning/api/user", [idUser],callback);
        },

        getTest : (callback) => {
          let x = [{
              question : "que estatura tengo?",
              answers : [{
                  answer : "Ah no quieres hablar eh?",
                  learningType : 1
              },
                  {
                      answer : "esta es otra pregunta",
                      learningType : 2
                  },
                  {
                      answer : "Y la ultima",
                      learningType : 3
                  }]
          },
              {
                  question : "otra pregunta",
                  answers : [{
                      answer : "prueba de transicion",
                      learningType : 1
                  },
                      {
                          answer : "cosas",
                          learningType : 2
                      },
                      {
                          answer : "Y la ultima",
                          learningType : 3
                      }]
              }];
          callback(x);
        }
    };

    return UserRestClient;
});