package com.modular.rest.provider;

import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.ext.Provider;

@Provider
@Consumes("application/json")
@Produces("application/json")
public class JacksonProvider extends JacksonJaxbJsonProvider {
}
