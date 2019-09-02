package com.modular.persistence.dao;

import com.modular.persistence.model.Notification;

import java.util.Set;

public interface NotificationDAO extends BaseDAO<Notification> {
    Set<Notification> getAllNotifications(int courseId) throws DataBaseException;
    boolean exists(int id);
}
