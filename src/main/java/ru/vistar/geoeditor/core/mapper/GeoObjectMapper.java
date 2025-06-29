package ru.vistar.geoeditor.core.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import ru.vistar.geoeditor.core.dto.GeoObjectCreateRequest;
import ru.vistar.geoeditor.core.dto.GeoObjectData;
import ru.vistar.geoeditor.core.dto.GeoObjectUpdateRequest;
import ru.vistar.geoeditor.core.model.GeoObject;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GeoObjectMapper {
    GeoObjectData toData(GeoObject model);

    List<GeoObjectData> toDataList(List<GeoObject> models);

    void update(@MappingTarget GeoObject target, GeoObjectUpdateRequest source);

    GeoObject toModel(GeoObjectCreateRequest payload);
}
