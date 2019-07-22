package com.modular.rest;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.Path;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/api")
public class ElearningApp extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<>();
        classes.add(CourseEndpoint.class);
        classes.add(HomeworkEndpoint.class);
        classes.add(NotificationEndpoint.class);
        classes.add(UserEndpoint.class);
        return classes;
    }
}
