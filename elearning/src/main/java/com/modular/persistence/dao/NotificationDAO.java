package com.modular.persistence.dao;

import com.modular.persistence.model.Notification;

import java.util.List;

public interface NotificationDAO extends BaseDAO<Notification> {
    List<Notification> getAllNotifications() throws DataBaseException;
}
