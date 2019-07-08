package com.modular.persistence.dao;

public interface BaseDAO<E> {
    public void create(E entity) throws DataBaseException;
    public E get(int key) throws DataBaseException;
    public void update(E entity) throws DataBaseException;
    public void delete(E entity) throws DataBaseException;
}
