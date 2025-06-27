package ru.vistar.geoeditor.data.mapper.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.springframework.stereotype.Component;
import ru.vistar.geoeditor.core.model.GeoCoords;

import java.sql.*;
import java.util.List;

@Component
@MappedTypes(List.class)
@MappedJdbcTypes(JdbcType.OTHER)
public class GeoCoordsListTypeHandler extends BaseTypeHandler<List<GeoCoords>> {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<GeoCoords> parameter, JdbcType jdbcType) throws SQLException {
        try {
            ps.setObject(i, OBJECT_MAPPER.writeValueAsString(parameter), Types.OTHER);
        } catch (JsonProcessingException e) {
            throw new SQLException("Error when converting to JSON: " + parameter, e);
        }
    }

    @Override
    public List<GeoCoords> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return parse(rs.getString(columnName));
    }

    @Override
    public List<GeoCoords> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return parse(rs.getString(columnIndex));
    }

    @Override
    public List<GeoCoords> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return parse(cs.getString(columnIndex));
    }

    private List<GeoCoords> parse(String json) throws SQLException {
        try {
            return OBJECT_MAPPER.readValue(json, new TypeReference<List<GeoCoords>>() {});
        } catch (JsonProcessingException e) {
            throw new SQLException("Error when parsing JSON: " + json, e);
        }
    }
} 