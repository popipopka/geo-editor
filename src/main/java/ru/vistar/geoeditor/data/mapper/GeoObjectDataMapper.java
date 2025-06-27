package ru.vistar.geoeditor.data.mapper;

import org.apache.ibatis.annotations.*;
import ru.vistar.geoeditor.core.model.GeoObject;

import java.util.List;
import java.util.Optional;

@Mapper
public interface GeoObjectDataMapper {
    @Select("""
            select *
            from objects
            where id = #{id}
            """)
    Optional<GeoObject> findById(Long id);

    @Select("""
            select *
            from objects
            order by id desc
            """)
    List<GeoObject> findAll();

    @Insert("""
                insert into objects (name, type, coords)
                values (#{name}, #{type}, #{coords})
            """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void create(GeoObject obj);

    @Update("""
            update objects
            set name = #{name},
                type = #{type},
                coords = #{coords}
            where id = #{id}
            """)
    void update(GeoObject obj);

    @Delete("""
            delete from objects
            where id = #{id}
            """)
    void delete(Long id);

    @Select("""
            select exists(
                select 1
                from objects
                where id = #{id}
            )
            """)
    boolean existsById(Long id);
} 