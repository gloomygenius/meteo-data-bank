package com.mdbank.dao;

import com.mdbank.util.ReflectionUtils;

import java.sql.Connection;

public abstract class AbstractDao<T, K> implements BaseDao<T, K> {
    private Class<T> entityClass;
    private Class<K> idClass;
    private String sequenceName;

    @SuppressWarnings("unchecked")
    protected AbstractDao(String sequenceName) {
        entityClass = ReflectionUtils.getFirstTypeParameterClass(this.getClass());
        idClass = ReflectionUtils.getSecondTypeParameterClass(this.getClass());
        this.sequenceName = sequenceName;
    }

    @Override
    public Class<T> getEntityClass() {
        return entityClass;
    }

    @Override
    public Class<K> getIdClass() {
        return idClass;
    }

    protected K getNextId(Connection connection) {
        return executeQuery(connection, "SELECT NEXTVAL(?);", rs -> rs.getObject(1, idClass), sequenceName).get(0);
    }
}
