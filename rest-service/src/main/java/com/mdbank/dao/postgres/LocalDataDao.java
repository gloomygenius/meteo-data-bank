package com.mdbank.dao.postgres;

import com.mdbank.dao.AbstractDao;
import com.mdbank.model.LocalData;
import com.mdbank.model.Position;
import com.mdbank.model.metadata.MeteoParameter;
import com.mdbank.model.metadata.NetCdfParam;
import com.mdbank.util.Exceptional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.sql.*;
import java.time.Year;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

@Repository
public class LocalDataDao extends AbstractDao<LocalData, Long> {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private Supplier<Connection> connectionSupplier;

    protected LocalDataDao() {
        super("local_data_local_data_id_seq");
    }

    @PostConstruct
    public void init() {
        connectionSupplier = Exceptional.supplier(() -> jdbcTemplate.getDataSource().getConnection());
    }

    @Override
    public LocalData save(LocalData data) {
        Long generatedId = getNextId(connectionSupplier.get());
        Connection conn = connectionSupplier.get();
        Array array = Exceptional.tryCatch(() -> conn.createArrayOf("float", data.getArr().toArray()));
        executeUpdate(conn,
                "INSERT INTO local_data (local_data_id, parameter, position_id, year, arr) " +
                        "VALUES (?, ?, ?, ?, ?)",
                generatedId,
                data.getParameter().name(),
                data.getPosition().getId(),
                data.getYear().getValue(),
                array);

        data.setId(generatedId);
        return data;
    }

    @Override
    public LocalData update(LocalData data) {
        Connection connection = connectionSupplier.get();

        Array array = Exceptional.tryCatch(() -> connection.createArrayOf("float", data.getArr().toArray()));
        executeUpdate(connection,
                "UPDATE local_data SET(parameter, position_id, year, arr) = (?, ?, ?, ?) WHERE local_data_id=?",

                data.getParameter().name(),
                data.getPosition().getId(),
                data.getYear().getValue(),
                array,
                data.getId());

        return data;
    }

    @Override
    public LocalData getById(Long id) {
        List<LocalData> result = executeQuery(connectionSupplier.get(),
                "SELECT ld.local_data_id, ld.parameter, ld.year, ld.arr, p.position_id, p.latitude, p.longitude, p.altitude FROM local_data ld LEFT OUTER JOIN position p ON (ld.position_id = p.position_id) where ld.local_data_id=?;",
                (rs) -> new LocalData(
                        rs.getLong("local_data_id"),
                        NetCdfParam.valueOf(rs.getString("parameter")),
                        new Position(
                                rs.getLong("position_id"),
                                rs.getDouble("latitude"),
                                rs.getDouble("longitude"),
                                rs.getDouble("altitude")),
                        Year.of(rs.getInt("year")),
                        Arrays.asList((Float[]) rs.getArray("arr").getArray())),
                id);

        return result.size() > 0 ? result.get(0) : null;
    }

    @Override
    public LocalData delete(Long id) {
        LocalData byId = getById(id);
        executeUpdate(connectionSupplier.get(), "DELETE FROM local_data WHERE local_data_id=?", id);
        return byId;
    }

    public Optional<LocalData> getBy(Position position, NetCdfParam parameter, int year) {
        List<LocalData> localData = executeQuery(connectionSupplier.get(),
                "SELECT ld.local_data_id, ld.parameter, ld.year, ld.arr, p.position_id, p.latitude, p.longitude, p.altitude " +
                        "FROM local_data ld LEFT OUTER JOIN position p ON (ld.position_id = p.position_id) " +
                        "WHERE p.latitude=? AND p.longitude=? AND ld.parameter=? AND ld.year=?;",
                (rs) -> new LocalData(
                        rs.getLong("local_data_id"),
                        NetCdfParam.valueOf(rs.getString("parameter")),
                        new Position(
                                rs.getLong("position_id"),
                                rs.getDouble("latitude"),
                                rs.getDouble("longitude"),
                                rs.getDouble("altitude")),
                        Year.of(rs.getInt("year")),
                        Arrays.asList((Float[]) rs.getArray("arr").getArray())),
                position.getLatitude(),
                position.getLongitude(),
                parameter.name(),
                year);

        return localData.size() > 0 ? Optional.of(localData.get(0)) : Optional.empty();
    }
}
