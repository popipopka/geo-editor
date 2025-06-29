package ru.vistar.geoeditor.core.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "Данные для обновления географического объекта")
public class GeoObjectUpdateRequest {

    @NotEmpty
    @Schema(description = "Координаты географического объекта")
    private List<GeoCoordsData> coords;
}