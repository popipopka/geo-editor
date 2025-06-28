package ru.vistar.geoeditor.core.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Географические координаты")
public class GeoCoordsData {

    @Schema(description = "Широта", example = "55.7558")
    private double lat;

    @Schema(description = "Долгота", example = "37.6176")
    private double lon;
} 