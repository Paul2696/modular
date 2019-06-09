package com.modular.rest;

import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

public class ElearningApplication extends Application {
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<>();
        classes.add(HelloResource.class);
        return classes;
    }
}
