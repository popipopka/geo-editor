package ru.vistar.geoeditor.data.mapper.handler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.springframework.stereotype.Component;
import ru.vistar.geoeditor.core.model.GeoObjectType;

import java.sql.*;

@Component
@MappedTypes(GeoObjectType.class)
@MappedJdbcTypes(JdbcType.OTHER)
public class GeoObjectTypeHandler extends BaseTypeHandler<GeoObjectType> {
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, GeoObjectType parameter, JdbcType jdbcType) throws SQLException {
        ps.setObject(i, parameter.name(), Types.OTHER);
    }

    @Override
    public GeoObjectType getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String value = rs.getString(columnName);
        return GeoObjectType.valueOf(value);
    }

    @Override
    public GeoObjectType getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String value = rs.getString(columnIndex);
        return GeoObjectType.valueOf(value);
    }

    @Override
    public GeoObjectType getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String value = cs.getString(columnIndex);
        return GeoObjectType.valueOf(value);
    }
} 