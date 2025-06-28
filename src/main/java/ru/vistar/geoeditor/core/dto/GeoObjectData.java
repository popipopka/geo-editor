package ru.vistar.geoeditor.core.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import ru.vistar.geoeditor.core.model.GeoObjectType;
import java.util.List;

@Data
@Schema(description = "Географический объект")
public class GeoObjectData {
    @Schema(description = "Уникальный идентификатор объекта", example = "1")
    private Long id;

    @Schema(description = "Название географического объекта", example = "Московский Кремль")
    private String name;

    @Schema(description = "Тип географического объекта")
    private GeoObjectType type;

    @Schema(description = "Координаты объекта")
    private List<GeoCoordsData> coords;
} 