package com.mdbank.dao;

import com.mdbank.exception.DaoException;
import com.mdbank.util.ExFunction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface BaseDao<ENTITY, ID> {
    ENTITY save(ENTITY entity);

    ENTITY update(ENTITY entity);

    ENTITY getById(ID id);

    ENTITY delete(ID id);

    Class<ENTITY> getEntityClass();

    Class<ID> getIdClass();

    /**
     * Метод для запросов в базу данных
     *
     * @param sql      текст запроса
     * @param rsMapper обработчик результатов
     * @param params   параметры запроса
     * @param <T>      объект запроса
     * @return обобщённый объект запроса
     */
    default <T> List<T> executeQuery(Connection conn,
                                     String sql,
                                     ExFunction<ResultSet, T> rsMapper,
                                     Object... params) {
        List<T> list = new ArrayList<>();
        try (Connection connection = conn;
             PreparedStatement ps = connection.prepareStatement(sql)) {
            int cnt = 0;
            for (Object param : params) {
                ps.setObject(++cnt, param);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(rsMapper.apply(rs));
                }
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return list;
    }

    /**
     * Метод для обновления и удаления данных.
     *
     * @param sql    текст запроса
     * @param params параметры, передаваемые в запросе
     */
    default int executeUpdate(Connection conn,
                              String sql,
                              Object... params) {
        int affectedRows;
        try (Connection connection = conn;
             PreparedStatement ps = connection.prepareStatement(sql)) {
            int count = 0;
            for (Object param : params) {
                ps.setObject(++count, param);
            }
            affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating entity " + getEntityClass().getSimpleName() + "failed, no rows affected.");
            }

        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return affectedRows;
    }
}